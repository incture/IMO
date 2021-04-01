//
//  FormCell.swift
//  DFT
//
//  Created by Soumya Singh on 31/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class FormCell: UITableViewCell {

    @IBOutlet var titleLabel: UILabel!
    @IBOutlet var inputField: ICTextField!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        inputField.borderStyle = .none
        inputField.textAlignment = .right
        // Initialization code
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        
        //set cell to initial state here
        //set like button to initial state - title, font, color, etc.
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setPlaceHolder(labelData : String, i : Int){
        
        titleLabel.text = labelData
        if i == 0{
            inputField.placeholder = "(EgABC490)"
        }
        else if i == 1{
            inputField.placeholder = "--Select--"
        }
        else if i == 2{
            inputField.placeholder = "--Select--"
        }
        else if i == 3{
            inputField.placeholder = "--Select--"
        }
        else if i == 4{
            inputField.placeholder = "--Select--"
        }
        else if i == 5{
            inputField.placeholder = "--Select--"
        }
        else{
            inputField.placeholder = ""
        }
    }
    
    func setPlaceholderOnly(i : Int){
        
        if i == 0{
            inputField.placeholder = "(EgABC490)"
        }
        else if i == 1{
            inputField.placeholder = "--Select--"
        }
        else if i == 2{
            inputField.placeholder = "--Select--"
        }
        else if i == 3{
            inputField.placeholder = "--Select--"
        }
        else if i == 4{
            inputField.placeholder = "--Select--"
        }
        else if i == 5{
            inputField.placeholder = "--Select--"
        }
        else{
            inputField.placeholder = ""
        }
    
    }
    
    func setPlaceholderForAttest(labelData : String, i : Int){
        
        titleLabel.text = labelData
        if i == 0{
            inputField.placeholder = "--Select--"
        }
        else if i == 1{
            inputField.placeholder = ""
        }
        else if i == 2{
            inputField.placeholder = "--Select--"
        }
        else if i == 3{
            inputField.placeholder = ""
        }
        else if i == 4{
            inputField.placeholder = ""
        }
        else if i == 5{
            inputField.placeholder = "--Select--"
        }
        else{
            inputField.placeholder = ""
        }
        
    }
    
    
    func setData(labelData : String, fieldValue : String){
        
        titleLabel.text = labelData
        inputField.text = fieldValue
    }
    
    func setValue(fieldValue : String){
        
        inputField.text = fieldValue
    }
    
    
}
