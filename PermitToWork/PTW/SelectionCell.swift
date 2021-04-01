//
//  SelectionCell.swift
//  Task-Management
//
//  Created by Soumya Singh on 17/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class SelectionCell: UITableViewCell {

    @IBOutlet var keyLabel: UILabel!
    @IBOutlet var selectImage: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    func setData(labelValue : String, selected : Int){
        
        keyLabel.text = labelValue
        if selected == 0{
            selectImage.image = UIImage(named : "unchecked")
        }
        else{
            selectImage.image = UIImage(named : "checked")
        }
    }
    
}
