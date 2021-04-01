//
//  IdentifyCell.swift
//  Task-Management
//
//  Created by Soumya Singh on 24/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class IdentifyCell: UITableViewCell {
        
    @IBOutlet weak var reasonView: UITextView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        reasonView.layer.borderWidth = 0.5
        reasonView.layer.borderColor = #colorLiteral(red: 0.3333333433, green: 0.3333333433, blue: 0.3333333433, alpha: 1)
        reasonView.layer.cornerRadius = 6.0
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
