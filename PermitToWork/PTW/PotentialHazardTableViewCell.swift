//
//  PotentialHazardTableViewCell.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 21/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class PotentialHazardTableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var detailLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
