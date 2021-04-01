//
//  CommonHeaderCell.swift
//  Task-Management
//
//  Created by Soumya Singh on 17/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class CommonHeaderCell: UITableViewCell {

    @IBOutlet var contentLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(text : String)
    {
        contentLabel.text = text
    }
    
}
