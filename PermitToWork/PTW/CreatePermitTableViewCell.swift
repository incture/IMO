//
//  CreatePermitTableViewCell.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 22/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class CreatePermitTableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel: UILabel!
     @IBOutlet weak var checkButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
    func setStatus(status : Bool){
        if status{
            self.checkButton.setImage(UIImage(named : "checked"), for: .normal)
        }
        else{
            self.checkButton.setImage(UIImage(named : "unchecked"), for: .normal)
        }
    }

}
