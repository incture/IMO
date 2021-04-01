//
//  ApprovalToproceedTableViewCell.swift
//  Murphy_PWT_iOS
//
//  Created by Parul Thakur77 on 02/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class ApprovalToproceedTableViewCell: UITableViewCell {
    @IBOutlet var keyLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func setData(labelValue : String){
        
        keyLabel.text = labelValue
    }
    
}
