//
//  TicketCell.swift
//  DFT
//
//  Created by Soumya Singh on 01/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class TicketCell: UITableViewCell {

    @IBOutlet var upperView: UIView!
    @IBOutlet var markerView: UIView!
    @IBOutlet var ticketTitleLabel: UILabel!
    @IBOutlet var timeLabel: UILabel!
    @IBOutlet var departmentLabel: UILabel!
    @IBOutlet var locationLabel: UILabel!
    @IBOutlet var departmentKeyValue: UILabel!
    @IBOutlet var locationKeyValue: UILabel!
    @IBOutlet var vendorRefNoKeyValue: UILabel!
    @IBOutlet var vendorRefLabel: UILabel!
    @IBOutlet var vendorNameKey: UILabel!
    @IBOutlet var vendorNameLabel: UILabel!
    @IBOutlet var extraLabelKey: UILabel!
    @IBOutlet var extraLabelValue: UILabel!
    @IBOutlet weak var heightConstraint1: NSLayoutConstraint!
    @IBOutlet weak var heightConstraint2: NSLayoutConstraint!
    override func awakeFromNib() {
        super.awakeFromNib()
        upperView.layer.borderColor = UIColor.lightGray.cgColor
        upperView.layer.borderWidth = 0.5
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(ticket : FieldTicket)
    {
        ticketTitleLabel.text = ticket.FieldTicketNum
        departmentLabel.text = ticket.Department
        locationKeyValue.sizeToFit()
        if ticket.Location != ""{
            locationLabel.text = ticket.Location
        }
        else{
            locationLabel.text = "   -   "
        }
        vendorRefLabel.text = ticket.AribaSesNumber
        if ticket.Status == TicketStatus.Create.rawValue{
            markerView.backgroundColor = UIColor(red : 246/255, green : 142/255, blue : 86/255, alpha : 1)
        }
        else if ticket.Status == TicketStatus.Reject.rawValue{
            markerView.backgroundColor = UIColor.red
        }
        else if ticket.Status == TicketStatus.Review.rawValue{
            markerView.backgroundColor = UIColor(red : 77/255, green : 184/255, blue : 255/255, alpha : 1)
        }
        else if ticket.Status == TicketStatus.Verify.rawValue{
            markerView.backgroundColor = UIColor(red : 77/255, green : 184/255, blue : 255/255, alpha : 1)
        }
        else {
            markerView.backgroundColor = UIColor.darkGray
        }
        let ticketTime = decodeTime(timeString: ticket.UIcreationTime!)
        timeLabel.text = ticketTime
        vendorNameKey.text = ""
        vendorNameLabel.text = ""
        extraLabelKey.text = ""
        extraLabelValue.text = ""
        heightConstraint1.constant = 0
        heightConstraint2.constant = 0
        
    }
    
    func setDataForTasks(task : Task)
    {
        ticketTitleLabel.text = task.FieldTicketNum
        departmentLabel.text = task.Department
        if task.CreatedByName == nil {
            locationLabel.text = task.CreatedByID
        }
        else{
            locationLabel.text = task.CreatedByName
        }
        locationKeyValue.text = "Created By :"
        vendorRefNoKeyValue.text = "Vendor Ref No:"
        vendorRefLabel.text = task.vendorRefNum
        vendorNameKey.text = "Vendor ID:"
        vendorNameLabel.text = task.vendorID
        extraLabelKey.text = "Vendor Name:"
        extraLabelValue.text = task.vendorName
        timeLabel.text = task.CreatedTime
        markerView.backgroundColor = UIColor(red : 246/255, green : 142/255, blue : 86/255, alpha : 1)
        
    }
    
    
    func decodeTime(timeString : String) -> String{
        
        
        let hour = timeString[2 ..< 4] // returns String "o, worl"
        let minutes = timeString[5 ..< 7]
        let seconds = timeString[8 ..< 10]
        let timeData = hour + ":" + minutes + ":" + seconds
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss"
        let myDate = dateFormatter.date(from: timeData)!
        dateFormatter.dateFormat = "hh:mm a"
        let somedateString = dateFormatter.string(from: myDate)
        return somedateString
    }
}

