//
//  NotificationCellTableViewCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 07/03/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class NotificationCellTableViewCell: UITableViewCell {

    @IBOutlet var errorMessageLabel: UILabel!
    @IBOutlet var timeLabel: UILabel!
    @IBOutlet var departmentLabel: UILabel!
    @IBOutlet var locationLabel: UILabel!
    @IBOutlet var locationKeyLabelValue: UILabel!
    @IBOutlet var createdOnLabel: UILabel!
    @IBOutlet var vendorRefLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(ticket : FailedTicket){
        errorMessageLabel.text = ticket.errorMessage
        timeLabel.text = ticket.CreatedTime
        departmentLabel.text = ticket.Department
        locationLabel.text = ticket.Location
        if ticket.Department == Department.Operations.rawValue{
            locationKeyLabelValue.text = "Field"
        }
        createdOnLabel.text = ticket.CreatedDate
        vendorRefLabel.text = ticket.vendorRefNo
    }
    
}
