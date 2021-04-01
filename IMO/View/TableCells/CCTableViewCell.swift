//
//  CCTableViewCell.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 16/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class CCTableViewCell: UITableViewCell {

    @IBOutlet weak var CCIdLabel: UILabel!
    @IBOutlet weak var CCNameLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
