//
//  ProfileCell.swift
//  DFT
//
//  Created by Soumya Singh on 29/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class ProfileCell: UITableViewCell {

    
    @IBOutlet var roundedView: UIView!
    @IBOutlet var titleLabel: UILabel!
    @IBOutlet var key1Label: UILabel!
    @IBOutlet var value1Label: UILabel!
    @IBOutlet var key2Label: UILabel!
    @IBOutlet var value2Label: UILabel!
    @IBOutlet var key3Label: UILabel!
    @IBOutlet var value3Label: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        roundedView.layer.cornerRadius = 10
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(titleValue : String, key1 : String, value1 : String, key2 : String, value2 : String, key3 : String, value3 : String )
    {
        titleLabel.text = titleValue
        key1Label.text = key1
        value1Label.text = value1
        key2Label.text = key2
        value2Label.text = value2
        key3Label.text = key3
        value3Label.text = value3
    }
    
}
