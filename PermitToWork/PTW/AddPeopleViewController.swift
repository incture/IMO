//
//  AddPeopleViewController.swift
// 
//
//  Created by Parul Thakur77 on 04/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class AddPeopleViewController: UIViewController, UITextFieldDelegate {

    
    @IBOutlet weak var bottomLine: UILabel!
    @IBOutlet weak var firstNameTF: UITextField!
    @IBOutlet weak var contactNOTF: VSTextField!
    @IBOutlet weak var lastNameTF: UITextField!
    @IBOutlet weak var deleteButton: UIBarButtonItem!
    @IBOutlet weak var bottomBar: UIView!
    
    @IBOutlet weak var coldWorkButton: UIButton!
    @IBOutlet weak var coldWorkLabel: UILabel!
    @IBOutlet weak var hotWorkLabel: UILabel!
    @IBOutlet weak var hotWorkButton: UIButton!
    @IBOutlet weak var confinedSpaceButton: UIButton!
    @IBOutlet weak var confinedSpaceLabel: UILabel!
    
    
    var index : Int?
    var isEditable = true
    var totalArray = [[String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if index != nil{
            
            JSAObject.addPeopleIndex = index!
            JSAObject.addNewpeople = JSAObject.peopleList[index!]
            
            firstNameTF.isUserInteractionEnabled = false
            lastNameTF.isUserInteractionEnabled = false
            contactNOTF.isUserInteractionEnabled = false
            isEditable = false
            
            firstNameTF.text = JSAObject.peopleList[index!].firstName
            lastNameTF.text = JSAObject.peopleList[index!].lastName
            contactNOTF.text = JSAObject.peopleList[index!].contactNumber
            
            deleteButton.isEnabled = true
            
        }
        else
        {
            deleteButton.isEnabled = false
        }

        firstNameTF.delegate = self
        contactNOTF.delegate = self
        lastNameTF.delegate = self
        contactNOTF.formatting = .phoneNumber
        
        if JSAObject.hasCWP == 1 && JSAObject.currentFlow != .JSA
        {
            coldWorkLabel.isHidden = false
            coldWorkButton.isHidden = false
        }
        else
        {
            coldWorkLabel.isHidden = true
            coldWorkButton.isHidden = true
        }
        if JSAObject.hasHWP == 1 && JSAObject.currentFlow != .JSA
        {
            hotWorkLabel.isHidden = false
            hotWorkButton.isHidden = false
        }
        else
        {
            hotWorkLabel.isHidden = true
            hotWorkButton.isHidden = true
        }
        if JSAObject.hasCSP == 1 && JSAObject.currentFlow != .JSA
        {
            confinedSpaceLabel.isHidden = false
            confinedSpaceButton.isHidden = false
        }
        else
        {
            confinedSpaceLabel.isHidden = true
            confinedSpaceButton.isHidden = true
        }
        

        
        
        if JSAObject.addNewpeople.hasSignedCWP == 1
        {
            coldWorkButton.setImage(UIImage(named:"checked"), for: .normal)
            coldWorkButton.isEnabled = false
        }
        else
        {
            coldWorkButton.setImage(UIImage(named:"unchecked"), for: .normal)
            coldWorkButton.isEnabled = true
        }
        
        if JSAObject.addNewpeople.hasSignedHWP == 1
        {
            hotWorkButton.setImage(UIImage(named:"checked"), for: .normal)
            hotWorkButton.isEnabled = false
        }
        else
        {
            hotWorkButton.setImage(UIImage(named:"unchecked"), for: .normal)
            hotWorkButton.isEnabled = true
        }
        
        if JSAObject.addNewpeople.hasSignedCSE == 1
        {
            confinedSpaceButton.setImage(UIImage(named:"checked"), for: .normal)
            confinedSpaceButton.isEnabled = false
        }
        else
        {
            confinedSpaceButton.setImage(UIImage(named:"unchecked"), for: .normal)
            confinedSpaceButton.isEnabled = true
        }
        
        
        
        if JSAObject.currentFlow != .CWP && JSAObject.isCWP == 0
        {
            coldWorkLabel.textColor = UIColor.lightGray
            coldWorkButton.isEnabled = false
        }
        if JSAObject.currentFlow != .HWP && JSAObject.isHWP == 0
        {
            hotWorkLabel.textColor = UIColor.lightGray
            hotWorkButton.isEnabled = false
        }
        if JSAObject.currentFlow != .CSEP && JSAObject.isCSP == 0
        {
            confinedSpaceLabel.textColor = UIColor.lightGray
            confinedSpaceButton.isEnabled = false
        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.hideKeyboardWhenTappedAround()
        
//        if JSAObject.currentFlow == .JSA{
//            coldWorkButton.isHidden = true
//            coldWorkButton.isEnabled = false
//            coldWorkLabel.isHidden = true
//        }
//        else if JSAObject.currentFlow == .CWP{
//            coldWorkButton.isHidden = false
//            coldWorkButton.isEnabled = true
//            coldWorkLabel.isHidden = false
//            coldWorkLabel.text = "Cold Work"
//        }
        
    }

    @objc func textDidChange(textField: UITextField) {
        
        if textField.text!.count == 4 {
            textField.text = textField.text! + "-"
        } else if textField.text!.count == 9  {
            textField.text = textField.text! + "-"
        }
    }
    
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.endEditing(true)
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
    }
    
    
    
    @IBAction func onClickDelete(_ sender: Any) {
        
        JSAObject.peopleList.remove(at: index!)
        JSAObject.addNewpeople = People()
        JSAObject.addPeopleIndex = nil
        self.navigationController?.popViewController(animated: true)
        
    }
    @IBAction func backButton(_ sender: UIBarButtonItem) {
        JSAObject.addNewpeople = People()
        JSAObject.addPeopleIndex = nil
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func previousBtn(_ sender: UIButton)
    {
        JSAObject.addNewpeople = People()
        JSAObject.addPeopleIndex = nil
       self.navigationController?.popViewController(animated: true)
    }
    @IBAction func nextBtn(_ sender: UIButton)
    {
        var flag = true
        if firstNameTF.text == ""
        {
            flag = false
            let alert = UIAlertController(title: "", message: "Enter first name", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else if lastNameTF.text == ""
        {
            flag = false
            let alert = UIAlertController(title: "", message: "Enter last name", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
        if contactNOTF.text != ""
        {
            if isValidPhoneNumber(testStr: contactNOTF.text!)
            {
                
            }
            else
            {
                flag = false
                let alert = UIAlertController(title: "", message: "Contact number is invalid", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                
                self.present(alert, animated: true, completion: {
                    print("completion block")
                })
            }
        }
        else
        {
            if firstNameTF.text! != currentUser.firstname {
                flag = false
                let alert = UIAlertController(title: "", message: "Enter contact number", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                
                self.present(alert, animated: true, completion: {
                    print("completion block")
                })
            }
            else{
                flag = true
            }
        }
        
        if flag
        {
            JSAObject.addNewpeople.fullName = firstNameTF.text! + " " + lastNameTF.text!
            JSAObject.addNewpeople.firstName = firstNameTF.text!
            JSAObject.addNewpeople.lastName = lastNameTF.text!
            JSAObject.addNewpeople.contactNumber = contactNOTF.text!
            if JSAObject.permitNumber != 0{
                JSAObject.addNewpeople.permitNumber = JSAObject.permitNumber
            }
            else{
                let value = UserDefaults.standard.string(forKey: "offlinenumber")
                var counter = 0
                if value != nil{
                    counter = Int(value!)! - 1
                }
                JSAObject.addNewpeople.permitNumber = (counter)
            }
            if !ConnectionCheck.isConnectedToNetwork(){
                if JSAObject.addPeopleIndex == 0 && index == nil{
                    JSAObject.addPeopleIndex = nil
                }
            }
            if JSAObject.addNewpeople.hasSignedJSA == 0
            {
                UserDefaults.standard.set("true", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                if #available(iOS 10.0, *) {
                    
                    let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "RiskAssesmentViewController") as! RiskAssesmentViewController
//                    let navigationController = UINavigationController(rootViewController: dashBoardVC)
                    self.navigationController?.pushViewController(dashBoardVC, animated: true)

                } else {
                    // Fallback on earlier versions
                }
            }
            else if JSAObject.addNewpeople.isCheckedCWP == 1
            {
                UserDefaults.standard.set("true", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                
                //go to permit controller
                JSAObject.currentFlow = .CWP
                let dashBoardVC = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
//                let navigationController = UINavigationController(rootViewController: dashBoardVC)
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                
            }
            else if JSAObject.addNewpeople.isCheckedHWP == 1
            {
                UserDefaults.standard.set("true", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                
                //go to permit controller
                JSAObject.currentFlow = .HWP
                let dashBoardVC = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
                //                let navigationController = UINavigationController(rootViewController: dashBoardVC)
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }
            else if JSAObject.addNewpeople.isCheckedCSE == 1
            {
                UserDefaults.standard.set("true", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                
                //go to permit controller
                JSAObject.currentFlow = .CSEP
                let dashBoardVC = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
                //                let navigationController = UINavigationController(rootViewController: dashBoardVC)
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }
            else
            {
                let alert = UIAlertController(title: "", message: "User already exist", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                
                self.present(alert, animated: true, completion: {
                    print("completion block")
                })
            }
            
        }
    
    }
    
    
    @IBAction func onClickCSE(_ sender: UIButton) {
        
        
        if JSAObject.addNewpeople.isCheckedCWP == 1 || JSAObject.addNewpeople.hasSignedCWP == 1{
            if JSAObject.addNewpeople.hasSignedCSE == 0
            {
                if confinedSpaceButton.isSelected
                {
                    confinedSpaceButton.isSelected = false
                    confinedSpaceButton.setImage(UIImage(named: "unchecked"), for: .normal)
                    JSAObject.addNewpeople.isCheckedCSE = 0
                    
                }
                else
                {
                    confinedSpaceButton.isSelected = true
                    confinedSpaceButton.setImage(UIImage(named: "checked"), for: .normal)
                    JSAObject.addNewpeople.isCheckedCSE = 1
                    
                }
            }
        }
        else{
            let alert = UIAlertController(title: "", message: "Please add the Person to Cold Work Permit first.", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
    }
    @IBAction func onClickHotWork(_ sender: UIButton) {
        if JSAObject.addNewpeople.isCheckedCWP == 1 || JSAObject.addNewpeople.hasSignedCWP == 1 {
            if JSAObject.addNewpeople.hasSignedHWP == 0
            {
                if hotWorkButton.isSelected
                {
                    hotWorkButton.isSelected = false
                    hotWorkButton.setImage(UIImage(named: "unchecked"), for: .normal)
                    JSAObject.addNewpeople.isCheckedHWP = 0
                    
                }
                else
                {
                    hotWorkButton.isSelected = true
                    hotWorkButton.setImage(UIImage(named: "checked"), for: .normal)
                    JSAObject.addNewpeople.isCheckedHWP = 1
                    
                }
            }
        }
        else{
            let alert = UIAlertController(title: "", message: "Please add the Person to Cold Work Permit first.", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
    }
    @IBAction func onClickColdWork(_ sender: UIButton) {
        if JSAObject.addNewpeople.hasSignedCWP == 0
        {
            if coldWorkButton.isSelected
            {
                coldWorkButton.isSelected = false
                coldWorkButton.setImage(UIImage(named: "unchecked"), for: .normal)
                JSAObject.addNewpeople.isCheckedCWP = 0
                
            }
            else
            {
                coldWorkButton.isSelected = true
                coldWorkButton.setImage(UIImage(named: "checked"), for: .normal)
                JSAObject.addNewpeople.isCheckedCWP = 1
                
            }
        }
        
    }
    
    func isValidPhoneNumber(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
        let RegEx = "^([2-9][0-8][0-9])([2-9][0-9]{2})([0-9]{4})"
        let Test = NSPredicate(format:"SELF MATCHES %@", RegEx)
        return Test.evaluate(with: testStr)
    }

}
