//
//  HazardCategoryViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 21/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

@available(iOS 10.0, *)
class HazardCategoryViewController: UIViewController , UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var noteLabel: UILabel!
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var hazardCategoryTableView: UITableView!
    
     var hazardControl : HazardVCViewController?
     var hazardCategoryArray = [String]()
     var hazardCategoryImageArr = [String]()
    var selectedCategory = [Int]()
    var selectedIndex : Int?
    var note = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        
        note = "HAZARDS are identified in the table below with an icon and description. Appropriate CONTROLS are listed in the check boxes below. Use this table to determine the HAZARDS that are present for the task and identify the CONTROLS to be implemented.\n\nREMEMBER: THIS TABLE DOES NOT INCLUDE ALL POSSIBLE HAZARDS. IT IS EXPECTED THAT THE REQUIRED PPE FOR THE ACTIVITY AND WORK CONDITIONS WILL BE USED."
        noteLabel.text = note
        hazardCategoryTableView.tableFooterView = UIView()
        hazardCategoryArray = ["Pressurized Equipment", "Poor Lighting/Visiblity", "Personnel","Confined Space Entry","Simultaneous Operations","Ignition Sources","Hazard Substances","Potential Spills","Weather","High Noise","Dropped Objects","Lifting Equipment","Work At Heights","Portable Electrical Equipment","Moving Equipment","Manual Handling","Equipment And Tools","Slips, Trips and Falls","High Energy / High Voltage","Excavations","Mobile Equipment"]
        
        hazardCategoryImageArr = ["pressurizedEquipment","poorLighting","personnel","confinedSpace","simultaneousOperations","ignitionSources","hazardousSubstances","potentialSpills","weather","highNoise","droppedObject","liftingEquipment","workatHeights","portableElectricalEquipment","movingObjects","manualHandling","equipmentandTools","slipsTripsandFalls","highEnergyHighVoltage","excavations","mobileEquipment"]
        
        // calculating selected categories
        
        for (index,val) in JSAObject.hazardCategories.categories.enumerated()
        {
            for val1 in val
            {
                if val1 == 1
                {
                    selectedCategory.append(index)
                    break
                }
            }
        }
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "Hazard Categories - Review"
                
            }
            else if val == "false"
            {
                navigationTitle.title = "Hazard Categories"
                
            }
        }
        else
        {
            navigationTitle.title = "Hazard Categories"
            
        }

    }
    

    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return hazardCategoryArray.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        
        let cell:HazardCetegoryTableViewCell = (hazardCategoryTableView.dequeueReusableCell(withIdentifier: "HazardCetegoryTableViewCell") as! HazardCetegoryTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        
        if selectedCategory.contains(indexPath.row)
        {
            cell.contentView.backgroundColor = #colorLiteral(red: 0.003921568627, green: 0.1490196078, blue: 0.3529411765, alpha: 1)
        }
        else
        {
            cell.contentView.backgroundColor = UIColor(named: "BlackColor")
        }
        
        cell.nameLabel?.text = hazardCategoryArray[indexPath.row]
        cell.categoryImg.image = UIImage(named: hazardCategoryImageArr[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 80
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedIndex = indexPath.row
        hazardControl?.cardCount = selectedIndex!
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    

}
