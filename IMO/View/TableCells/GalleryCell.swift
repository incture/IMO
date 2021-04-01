//
//  GalleryCell.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 03/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class GalleryCell: UICollectionViewCell {
    
    @IBOutlet var galleryImge: UIImageView!
    var senderController : CreateTicketController?
    var currentIndex : Int?
    @IBAction func CancelSelectionPressed(_ sender: UIButton) {
        senderController?.getDeletedIndex(index: currentIndex!)
    }
    
    func setThumbnail( thumbImage : UIImage, sender : CreateTicketController, index : Int ){
        galleryImge.image = thumbImage
        senderController = sender
        currentIndex = index
    }
}
