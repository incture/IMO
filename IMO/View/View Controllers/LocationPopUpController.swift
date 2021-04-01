//
//  LocationPopUpController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 21/08/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class LocationPopUpController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableViewHeight: NSLayoutConstraint!
    
    var tableLabels = ["Field", "Facility", "WellPad", "Well"]
    var descriptionValues = [String]()
    var field : String = ""
    var facility : String = ""
    var wellPad : String = ""
    var well : String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.handleTap))
        self.view.addGestureRecognizer(tap)
        
        let formNib = UINib(nibName: "LocationCell", bundle: nil)
        tableView.register(formNib, forCellReuseIdentifier: "LocationCell")
        if field != ""{
            descriptionValues.append(field)
        }
        if facility != ""{
            descriptionValues.append(facility)
        }
        if wellPad != ""{
            descriptionValues.append(wellPad)
        }
        if well != ""{
            descriptionValues.append(well)
        }
        tableViewHeight.constant = CGFloat(70 * descriptionValues.count)
        tableView.isUserInteractionEnabled = false
        customizeUI()
        self.tableView.reloadData()
        

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @objc func handleTap(sender: UITapGestureRecognizer? = nil) {
        self.dismiss(animated: false, completion: nil)
    }
    
    func customizeUI()
    {
        self.view.backgroundColor = UIColor.darkGray.withAlphaComponent(0.4)
        let shadowPath = UIBezierPath(rect: self.tableView.bounds)
        tableView.layer.shadowColor = UIColor.black.cgColor
        tableView.layer.shadowOffset = CGSize(width: 0.0, height: 5.0)
        tableView.layer.shadowOpacity = 0.5
        tableView.layer.shadowPath = shadowPath.cgPath
        
    }
}

 // MARK: - TableView Delegates and Datasource
extension LocationPopUpController : UITableViewDelegate, UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       
        return descriptionValues.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "LocationCell")! as! LocationCell
        cell.setData(label: tableLabels[indexPath.row], data: descriptionValues[indexPath.row])
        cell.selectionStyle = .none
        return cell
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
}

