//
//  JSALocationCollectionViewCell.swift
//  Task-Management
//
//  Created by Mahabaleshwar Hegde on 20/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class JSALocationCollectionViewCell: UICollectionViewCell {

    @IBOutlet weak var locationLabel: ICLabel!
    @IBOutlet var deleteButton: UIButton!
    var senderController : CreateJSAVC?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        self.layer.cornerRadius = 4.0
        self.layer.borderWidth = 0.5
      //  self.layer.borderColor = UIColor.blue.cgColor
    }

    @IBAction func onDeletePress(_ sender: UIButton) {
        
        if sender.tag != 0{
            let alert = UIAlertController(title: nil, message: "Do you want to delete this location?", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: { action in
                JSAObject.location.remove(at: sender.tag)
                self.senderController?.createJSATableView.reloadData()
            }))
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel , handler: nil))
            
            senderController?.present(alert, animated: true, completion: {
                print("completion block")
            })
            
           
        }
        else{
            let alert = UIAlertController(title: nil, message: "Can't delete default location", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            senderController?.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
        
    }
}
