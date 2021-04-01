//
//  WorkTypeCell.swift
//  Murphy IOP
//
//  Created by Soumya Singh on 23/03/18.
//  Copyright © 2018 soumyaIncture Technologies. All rights reserved.
//

import UIKit


protocol WorkTypeCellDelegate: class {
    
    var selectedWorkTypeCell: [Int: Bool]? { get set }
    
    func onCheckButtonSet(cell: WorkTypeCell, button: UIButton, value: Bool)
}

class WorkTypeCell: UITableViewCell {

    @IBOutlet var keyLabel: UILabel!
    @IBOutlet var checkButton: UIButton!
    
    weak var delegate: WorkTypeCellDelegate?
    
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
    
    func setSelectionStatus(status : Int)
    {
        if (status ==  0) {
            self.delegate?.onCheckButtonSet(cell: self, button: self.checkButton, value: false)
            checkButton.setImage(UIImage (named: "unchecked"), for: .normal)
        }else {
            self.delegate?.onCheckButtonSet(cell: self, button: self.checkButton, value: true)
            checkButton.setImage(UIImage (named: "checked"), for: .normal)
        }
    }
    
}


extension WorkTypeCellDelegate  {
    
    func onCheckButtonSet(cell: WorkTypeCell, button: UIButton, value: Bool) {
        self.selectedWorkTypeCell?[button.tag] = value
    }
}
