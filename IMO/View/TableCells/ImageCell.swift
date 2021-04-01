//
//  ImageCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 14/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class ImageCell: UITableViewCell {

    @IBOutlet var attachmentImage: UIImageView!
    @IBOutlet var attachmentName: UILabel!
    @IBOutlet var uploadDate: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(attachment : Attachment)
    {
        attachmentName.text = attachment.attachmentName
        uploadDate.text = "Uploaded on " + attachment.create_Date
        let image = attachment.attachmentContent
        let contentNameSplit = attachment.attachmentName.components(separatedBy: ".")
        let contentType = contentNameSplit[(contentNameSplit.count) - 1]
        if contentType == "pdf" || contentType == "PDF"{
        
        }
        else{
            let data: NSData = NSData(base64Encoded: image , options: .ignoreUnknownCharacters)!
            // turn  Decoded String into Data
            let dataImage = UIImage(data: data as Data)
            self.attachmentImage.image = dataImage
        }
        
    }
    
}
