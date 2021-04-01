//
//  RejectController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 20/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
class RejectController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet var alertView: UIView!
    @IBOutlet var reasonInputField: UITextField!
    @IBOutlet var titleLabel: UILabel!
    @IBOutlet var descriptionLabel: UILabel!

    var titleText : String?
    var descriptionText : String?
    var placeholderText : String?
    var fieldMandatory : Bool?
    var senderController : AttestController?
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        reasonInputField.delegate = self
        if fieldMandatory! == true{
            titleText = "Reject Ticket"
            descriptionText = "Input the reason for rejecting."
            placeholderText = "reason"
            
        }
        else {
            titleText = "Verify Ticket"
            descriptionText = "Add a comment."
            placeholderText = "comment (optional)"
            
        }
        customizeUI()
        updateTexts()
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
        // Dispose of any resources that can be recreated.
    }
    
    
    func updateTexts(){
        
        reasonInputField.placeholder = placeholderText
        titleLabel.text = titleText
        descriptionLabel.text = descriptionText
    }
    
    func customizeUI()
    {
        self.view.backgroundColor = UIColor.darkGray.withAlphaComponent(0.4)
        
        let border = CALayer()
        let width =  CGFloat(2.0)
        border.borderColor = UIColor(red: 68.0/255, green: 94.0/255, blue: 117.0/255, alpha: 1).cgColor
        border.frame = CGRect(x: 0, y: reasonInputField.frame.size.height - width, width : reasonInputField.frame.size.width, height: reasonInputField.frame.size.height)
        border.borderWidth = width
        reasonInputField.layer.addSublayer(border)
        reasonInputField.layer.masksToBounds = true
        self.view.layoutSubviews()
        let shadowPath = UIBezierPath(rect: self.alertView.bounds)
        alertView.layer.shadowColor = UIColor.black.cgColor
        alertView.layer.shadowOffset = CGSize(width: 0.0, height: 5.0)
        alertView.layer.shadowOpacity = 0.5
        alertView.layer.shadowPath = shadowPath.cgPath
        
    }
    
    func dismisscontroller()
    {
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func ConfirmButton(_ sender: UIButton) {
        
        if fieldMandatory == false{
            senderController?.getReasonToUpdateTicket(isVerified : true, reasonText : reasonInputField.text!)
            self.dismisscontroller()
            
        }
        else{
            
            if reasonInputField.text != ""{
                senderController?.getReasonToUpdateTicket(isVerified : false, reasonText : reasonInputField.text!)
                self.dismisscontroller()
                
            }
            else{
                
                let alertController = UIAlertController.init(title: "", message: "Enter a message to reject" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
        }
        
    }
    
    @IBAction func okButton(_ sender: UIButton) {
        
        self.dismisscontroller()
        
    }
    
    public func textFieldShouldReturn(_ textField: UITextField) -> Bool
    {
        
        textField.resignFirstResponder()
        return true
    }
    
}
