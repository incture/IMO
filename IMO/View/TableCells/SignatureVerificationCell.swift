//
//  SignatureVerificationCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 16/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class SignatureVerificationCell: UITableViewCell {

    @IBOutlet var DigiImageView: UIImageView!
    @IBOutlet var disclaimerView: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        let htmlText = "<ul style = 'color : #868686 ; font-size : 14;'><li><i>All Commercial Terms are to be in accordance to Murphy Oil MSA and originating Work Order.</i></li><li><i>Field/Wellsite Operations Stamp is a confirmation of services/materials received only.</i></li></ul>"
        let attrStr = try! NSAttributedString(
            data: htmlText.data(using: String.Encoding.unicode, allowLossyConversion: true)!,
            options: [ .documentType: NSAttributedString.DocumentType.html],
            documentAttributes: nil)
        disclaimerView.attributedText = attrStr
        
        let signString = UserDefaults.standard.string(forKey: "digisign")
        let data: Data = Data(base64Encoded: signString! , options: .ignoreUnknownCharacters)!
        // turn  Decoded String into Data
        let dataImage = UIImage(data:data,scale:1.0)
        self.DigiImageView.image = dataImage
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
