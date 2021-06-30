//
//  FrequencyCell.swift
// 
//  Created by Soumya Singh on 26/03/18.
//  Copyright © 2018 soumyaIncture Technologies. All rights reserved.
//

import UIKit

class FrequencyCell: UITableViewCell {

    @IBOutlet var titleLabel: UILabel!
    @IBOutlet var permitSwitch: UISwitch!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setLabel(data : String){
        titleLabel.text = data
    }
    
    func setSwitchStatus(status : Bool){
        permitSwitch.isOn = status
    }
}
