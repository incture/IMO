//
//  PermitCell.swift
//  Murphy IOP
//
//  Created by Soumya Singh on 23/03/18.
//  Copyright © 2018 soumyaIncture Technologies. All rights reserved.
//

import UIKit

class PermitCell: UITableViewCell {

    
    @IBOutlet var keyLabel: UILabel!
    @IBOutlet var valueTextField: ICTextField!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        valueTextField.borderStyle = .none
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(key : String, value : String){
        keyLabel.text = key
        valueTextField.text = value
    }
    
}
