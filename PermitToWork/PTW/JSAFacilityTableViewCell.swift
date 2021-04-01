//
//  JSAFacilityTableViewCell.swift
//  Task-Management
//
//  Created by Mahabaleshwar Hegde on 20/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class JSAFacilityTableViewCell: UITableViewCell {

    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var heightConstraint: NSLayoutConstraint!
    @IBOutlet weak var addButton: UIButton!

    var sender : CreateJSAVC?
    var index : Int?
    
    weak var collectionViewDelegate: UICollectionViewDelegate? {
        didSet {
            self.collectionView.delegate = self.collectionViewDelegate
        }
    }
    
    var locations: [Location] = [] {
        didSet {
            self.collectionView.reloadData()
            self.contentView.layoutIfNeeded()
            self.collectionView.performBatchUpdates(nil, completion: {
                (result) in
                 self.heightConstraint.constant = self.collectionView!.contentSize.height
                self.collectionView.reloadData()
            })
            JSAObject.location = locations
            
            var facilities = 0
            var wells = 0
            for each in locations{
                if each.hierarchyLevel == "well"{
                    wells+=1
                }
                else{
                    facilities+=1
                }
            }
            if locations.count % 2 == 1{
                let heightConst = (locations.count + 1)/2 - 1
                if locations.first?.hierarchyLevel == "well"{
                    self.heightConstraint.constant = CGFloat(60) + CGFloat(60 * heightConst)
                }
                else{
                    self.heightConstraint.constant = CGFloat(60) + CGFloat(60 * heightConst)
                }
            }
            else{
                let heightConst = (locations.count)/2 - 1
                if locations.first?.hierarchyLevel == "well"{
                    self.heightConstraint.constant = CGFloat(60) + CGFloat(60 * heightConst)
                }
                else{
                    self.heightConstraint.constant = CGFloat(60) + CGFloat(60 * heightConst)
                }
            }
           if currentUser.isReadOnly == true {
                self.addButton.isHidden = true
            }
        }
    }
    var flowLayout: UICollectionViewFlowLayout {
        return self.collectionView.collectionViewLayout as! UICollectionViewFlowLayout
    }
    
    
    private let identifier = String(describing: JSALocationCollectionViewCell.self)
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        self.collectionView.isScrollEnabled = true
        self.collectionView.dataSource = self
        let nib = UINib(nibName: identifier, bundle: nil)
        self.collectionView.register(nib, forCellWithReuseIdentifier: identifier)
        self.flowLayout.estimatedItemSize = CGSize(width: 10.0, height: 20.0)
        self.flowLayout.itemSize = UICollectionViewFlowLayout.automaticSize
       
        
    }
}

extension JSAFacilityTableViewCell: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return locations.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: identifier, for: indexPath) as! JSALocationCollectionViewCell
        cell.senderController = sender
        cell.locationLabel.preferredMaxLayoutWidth = 80
        cell.deleteButton.tag = indexPath.row        
        cell.locationLabel.text = self.locations[indexPath.row].facilityOrSite
        return cell
        
    }
}


extension JSAFacilityTableViewCell {
    
    @IBAction func addAction(sender: UIButton) {
        
        let vc = UIStoryboard(name: "start", bundle: Bundle.main).instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
        isFromCreateJSA = true
        createJSAController = self.sender!
        self.sender?.navigationController?.pushViewController(vc, animated: true)
        
    }
}
