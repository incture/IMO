//
//  HierarchyTableViewCell.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 27/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

protocol HierarchyTableViewCellDelegate: class {
    func didButtonTapped(cell: HierarchyTableViewCell)
}

class HierarchyTableViewCell: UITableViewCell {
 
    @IBOutlet weak var HierarchyButton: UIButton!
    
    weak var delegate: HierarchyTableViewCellDelegate?
    @IBAction func NavigateInside(_ sender: Any) {
        self.delegate?.didButtonTapped(cell: self)
    }
    
 
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}


