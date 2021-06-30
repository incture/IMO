//
//  PermitCardCell.swift
//  
//
//  Created by Soumya Singh on 10/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class PermitCardCell: UITableViewCell {

    @IBOutlet weak var associatedPermitLabel: UILabel!
    @IBOutlet var timeLabel: UILabel!
    @IBOutlet var markerLabel: UILabel!
    @IBOutlet var subjectLabel: UILabel!
    @IBOutlet var permitValue: UILabel!
    @IBOutlet var locationValue: UILabel!
    @IBOutlet var associatedPermitWidthConstraint: NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        markerLabel.layer.cornerRadius = 7.5
        markerLabel.layer.masksToBounds = true
        markerLabel.clipsToBounds = true
        self.contentView.layer.cornerRadius = 10
        self.contentView.layer.masksToBounds = true
        // Initialization code
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 4, left: 8, bottom: 4, right: 8))
        self.backgroundColor = UIColor.groupTableViewBackground
        if self.traitCollection.userInterfaceStyle == .dark {
            self.contentView.backgroundColor = UIColor(named: "BlackColor")
        }else{
            self.contentView.backgroundColor = .white
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func localToUTC(date: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd hh:mm:S"
        dateFormatter.calendar = NSCalendar.current
        dateFormatter.timeZone = TimeZone.current
        
        let dt = dateFormatter.date(from: date)
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        dateFormatter.dateFormat = "dd, MMM hh:mm a"
        
        return dateFormatter.string(from: dt!)
    }
    
    func UTCToLocal(date: String) -> String {
//        let dateFormatter = DateFormatter()
//        dateFormatter.dateFormat = "yyyy-mm-dd HH:mm"
//        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
//
//        let dt = dateFormatter.date(from: date)
//        dateFormatter.timeZone = TimeZone.current
//        dateFormatter.dateFormat = "dd, MMM hh:mm a"
//
//        return dateFormatter.string(from: dt!)
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        let date1 = dateFormatter.date(from: date)
        
        let dateformatter1 = DateFormatter()
        dateformatter1.timeZone = TimeZone.current
        dateformatter1.timeStyle = .short
        let finalDate = dateformatter1.string(from: date1!)
        print(finalDate)
        return finalDate
    }
    
    func setData(jsa : JSAList){
        
        let strDate = jsa.longFormatDate
        let time = strDate.convertToDateString(format: .short, currentDateStringFormat: .long, shouldConvertFromUTC: true)
        
        if jsa.status.lowercased() == "submitted"{
            markerLabel.backgroundColor = UIColor(red : 246/255, green : 142/255, blue : 86/255, alpha : 1.0)
        }
        else{
            markerLabel.backgroundColor = UIColor(red : 137/255, green : 199/255, blue : 87/255, alpha : 1.0)
        }
        timeLabel.text = time
        
        let loc = jsa.fieldOrSite.joined(separator: ",")
        locationValue.text = loc
        if jsa.jsaPermitNumber == "Not assigned yet"{
            subjectLabel.text = "Not yet assigned - " + jsa.taskDescription.utf8DecodedString()
        }
        else{
            subjectLabel.text = jsa.permitNumber + " - " + jsa.taskDescription.utf8DecodedString()
        }
       
        var finalString = ""
        if jsa.CWP != ""
        {
            finalString = finalString.appending("\(jsa.CWP),")
        }
        if jsa.HWP != ""
        {
            finalString = finalString.appending("\(jsa.HWP),")
        }
        if jsa.CSE != ""
        {
            finalString = finalString.appending("\(jsa.CSE),")
        }
        
        if finalString.count > 0
        {
            finalString.removeLast()
        }
        
        permitValue.text = finalString
        associatedPermitLabel.text = "Associated Permits - "
    }
    
    func setCWPData(jsa : PermitList){
        
        let strDate = jsa.longFormatDate
        let time = strDate.convertToDateString(format: .short, currentDateStringFormat: .long, shouldConvertFromUTC: true)
        
        if jsa.status.lowercased() == "submitted"{
            markerLabel.backgroundColor = UIColor(red : 246/255, green : 142/255, blue : 86/255, alpha : 1.0)
        }
        else if jsa.status.lowercased() == "closed"{
            markerLabel.backgroundColor = UIColor.red
        }
        else
        {
            markerLabel.backgroundColor = UIColor(red : 137/255, green : 199/255, blue : 87/255, alpha : 1.0)
        }
        timeLabel.text = time
        
        let loc = jsa.fieldOrSite.joined(separator: ",")
        locationValue.text = loc
        if Int(jsa.permitNumber)! < 1{
            subjectLabel.text = "Not yet assigned"
        }
        else{
            subjectLabel.text = jsa.permitNumber
        }        
        permitValue.text = "\(jsa.createdBy)"
        associatedPermitLabel.text = "Permit holder - "
    }
    
}
