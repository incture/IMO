//
//  JSACardCell.swift
//  Murphy_PWT_iOS
//
//  Created by Soumya Singh on 10/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class JSACardCell: UITableViewCell {
    
    @IBOutlet weak var containerVW: UIView!
    @IBOutlet var nameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        containerVW.layer.borderWidth = 0.5
        containerVW.layer.borderColor = UIColor.lightGray.cgColor
      
    }

    func setLabel(data : People){
        if data.fullName != ""{
            nameLabel.text = data.fullName
        }
        else{
             nameLabel.text = data.firstName + " " + data.lastName
        }
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
