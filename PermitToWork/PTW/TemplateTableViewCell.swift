//
//  TemplateTableViewCell.swift
//  PermitToWork
//
//  Created by Rajat Jain on 28/06/21.
//

import UIKit

class TemplateTableViewCell: UITableViewCell {

    @IBOutlet var keyLabel: UILabel!
    @IBOutlet var selectButton: UIButton!
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
            selectButton.setImage(UIImage(named : "unchecked"), for: .normal)
        }
        else{
            selectButton.setImage(UIImage(named : "checked"), for: .normal)
        }
    }
    
    
}
