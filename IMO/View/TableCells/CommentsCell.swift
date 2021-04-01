//
//  CommentsCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 16/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class CommentsCell: UITableViewCell {

    @IBOutlet var commentView: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        commentView.layer.borderWidth = 0.5
        commentView.layer.borderColor = UIColor.lightGray.cgColor
        
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(comment : Comment)
    {
        commentView.text = comment.Comments
    }
    
}
