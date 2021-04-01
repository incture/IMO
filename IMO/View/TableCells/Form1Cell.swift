//
//  Form1Cell.swift
//  DFT
//
//  Created by Soumya Singh on 31/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class Form1Cell: UITableViewCell {

    
    @IBOutlet var dateLabel: UILabel!
    @IBOutlet var timeLabel: UILabel!
    @IBOutlet var dateValue: ICTextField!
    @IBOutlet var timeValue: ICTextField!
    override func awakeFromNib() {
        super.awakeFromNib()
        dateValue.borderStyle = .none
        timeValue.borderStyle = .none
        // Initialization code
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        //set cell to initial state here
        //set like button to initial state - title, font, color, etc.
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
   
    
    func setData(dateKey : String, timeKey : String, dateInput : String, timeInput : String){
        
        dateLabel.text = dateKey
        timeLabel.text = timeKey
        if dateInput == "" {
            dateValue.placeholder = "-- Select--"
        }
        else{
            dateValue.text = dateInput
        }
        if timeInput == ""{
            timeValue.placeholder = "-- Select--"
        }
        else{
            timeValue.text = timeInput
        }
        
    }
    
    func setDateNTime(dateInput : String, timeInput : String)
    {
        dateValue.text = dateInput
        timeValue.text = timeInput
    }
    
    func setDate(dateInput : String)
    {
        dateValue.text = dateInput
    }
    
    func setTime(timeInput : String)
    {
        timeValue.text = timeInput
    }
}
