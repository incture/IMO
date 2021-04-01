//
//  CustomPOTableViewCell.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 18/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class CustomPOTableViewCell: UITableViewCell {

    @IBOutlet weak var POInputField: UITextField!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
