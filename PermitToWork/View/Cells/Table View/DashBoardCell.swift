//
//  DashBoardCell.swift
//  DFT
//
//  Created by Soumya Singh on 23/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class DashBoardCell: UITableViewCell {

    @IBOutlet var dashBoardImage: UIImageView!
    @IBOutlet var dashBoardLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(indexPath : Int){
//        if currentUser.apps?.indices.contains(indexPath) ==  false {
//            currentUser.apps?.append("Bypass")
//        }
        
        let role = currentUser.apps![indexPath]
        if role == "FIELD"
        {
            dashBoardImage.image = UIImage(named :"Dash1")
            dashBoardLabel.text = "  IOP Task Management"
        }
        else if role == "PTW"
        {
            dashBoardImage.image = UIImage(named :"Dash2")
            dashBoardLabel.text = "  Permit to Work"
        }
        else if role == "Notification"
        {
            dashBoardImage.image = UIImage(named :"notificationTile")
            dashBoardLabel.text = "  PM  Notification"
        }
        else if role == "Work Order"
        {
            dashBoardImage.image = UIImage(named :"workOrderDash")
            dashBoardLabel.text = "  PM Work Order"
        }
        else if role == "Bypass Log"
        {
            dashBoardImage.image = UIImage(named :"ByPassLog_Dash")
            dashBoardLabel.text = "  Bypass Log"
        }
        else if role == "Energy Isolation"
        {
            dashBoardImage.image = UIImage(named :"EnergyIsolation")
            dashBoardLabel.text = "  Energy Isolation"
        }
        else if role == "Location History"{
            dashBoardImage.image = UIImage(named :"LocationHsrty")
            dashBoardLabel.text = "  Location History"

        }
        else if role == "HSE Field Guide"{
            dashBoardImage.image = UIImage(named :"HSE_Field_Guied")
            dashBoardLabel.text = "  HSE Field Guide"
            
        } else if role == "ARVideo Calling" {
            dashBoardImage.image = UIImage(named :"Artboard")
            dashBoardLabel.text = "  Remote Assistant"
        }else if role == "Equipment Stats" {
            dashBoardImage.image = UIImage(named :"machinescanner")
            dashBoardLabel.text = "  Equipment Stats"
        }

//        } else if role == "Bypass" {
//            dashBoardImage.image = UIImage(named :"ByPassLog_Dash")
//            dashBoardLabel.text = "  Bypass Log"
//        }
//        else if role == "EnergyIsolation" {
//            dashBoardImage.image = UIImage(named :"EnergyIsolation")
//            dashBoardLabel.text = "  Energy Isolation"
//        }
            
        else {
            dashBoardImage.image = UIImage(named :"InvestigateDash")
            dashBoardLabel.text = "  IOP Investigation"
        }
//        if indexPath == 0{
//            dashBoardImage.image = UIImage(named :"Dash1")
//            dashBoardLabel.text = "  IOP Task Management"
//        }
//        else if indexPath == 1{
//            dashBoardImage.image = UIImage(named :"Dash1")
//            dashBoardLabel.text = "  IOP Task Management"
//        }
//        else{
//            dashBoardImage.image = UIImage(named :"Dash2")
//            dashBoardLabel.text = "  Permit to Work"
//        }
    }
}
