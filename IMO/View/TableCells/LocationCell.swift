//
//  LocationCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 21/08/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class LocationCell: UITableViewCell {

    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var dataLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(label : String, data : String)
    {
        descriptionLabel.text = label
        dataLabel.text = data
    }
    
}
