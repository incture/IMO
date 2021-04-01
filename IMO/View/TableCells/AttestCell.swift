//
//  AttestCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 14/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class AttestCell: UITableViewCell {

    @IBOutlet var attestBurtton: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        attestBurtton.layer.cornerRadius = 30
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
