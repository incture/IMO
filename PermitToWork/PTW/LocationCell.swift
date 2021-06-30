//
//  LocationCell.swift
//  
//
//  Created by Parul Thakur77 on 13/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class LocationCell: UITableViewCell {

    @IBOutlet var keyLabel: UILabel!
    @IBOutlet var checkButton: UIButton!
    
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
    
    func setSelectionStatus(status : Bool)
    {
        if (status ==  false) {
            checkButton.setImage(UIImage (named: "unchecked"), for: .normal)
        }else {
            checkButton.setImage(UIImage (named: "checked"), for: .normal)
        }
    }
    
}
