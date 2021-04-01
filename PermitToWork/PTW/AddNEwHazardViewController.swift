//
//  AddNewHazardViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 22/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class AddNewHazardViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var taskStepLabel: UITextField!
    @IBOutlet weak var potentialHazards: UITextField!
    @IBOutlet weak var personResponsible: UITextField!
    @IBOutlet weak var hazardControls: UITextField!
    
    @IBOutlet var doneNavButton: UIBarButtonItem!
    @IBOutlet var deleteNavButton: UIBarButtonItem!
    
    var index : Int?
    var hazard = PotentialHazard()
    
    var tempArray = [""]
//    var totalhazards = [[String]]()
//    var hazardArray = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()

        if index != nil{
            hazard = JSAObject.potentialHazards[index!]
            taskStepLabel.text = hazard.taskStep
            potentialHazards.text = hazard.potentialHazards
            personResponsible.text = hazard.personResponsible
            hazardControls.text = hazard.hazardControls
            doneNavButton.isEnabled = false
            deleteNavButton.isEnabled = true
            
        }
        else{
            doneNavButton.isEnabled = true
            deleteNavButton.isEnabled = false
        }
        taskStepLabel.delegate = self
        potentialHazards.delegate = self
        personResponsible.delegate = self
        hazardControls.delegate = self
        
        if let create = UserDefaults.standard.string(forKey: "JSACreate"), let preview = UserDefaults.standard.string(forKey: "JSAPreview") {
                if (create == "true" || JSAObject.status.lowercased() == "submitted") && preview == "false"{
                    taskStepLabel.isUserInteractionEnabled = true
                    potentialHazards.isUserInteractionEnabled = true
                    personResponsible.isUserInteractionEnabled = true
                    hazardControls.isUserInteractionEnabled = true
                    
                }
                else{
                    navigationTitle.title = "Hazard Details - Review"
                    taskStepLabel.isUserInteractionEnabled = false
                    potentialHazards.isUserInteractionEnabled = false
                    personResponsible.isUserInteractionEnabled = false
                    hazardControls.isUserInteractionEnabled = false
                    deleteNavButton.isEnabled = false
                }
            }
        else{
            taskStepLabel.isUserInteractionEnabled = false
            potentialHazards.isUserInteractionEnabled = false
            personResponsible.isUserInteractionEnabled = false
            hazardControls.isUserInteractionEnabled = false
            deleteNavButton.isEnabled = false
        }
        if currentUser.isReadOnly == true{
            let button: UIButton = UIButton(type: UIButton.ButtonType.custom)
            self.doneNavButton.customView = button
            self.deleteNavButton.customView = button
        }
        
    
    }
    @IBAction func doneBtn(_ sender: Any)
    {
        var flag = true
        if taskStepLabel.text == ""
        {
            flag = false
            let alert = UIAlertController(title: nil, message: "Please enter Task Step", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else if potentialHazards.text! == ""
        {
            flag = false
            let alert = UIAlertController(title: nil, message: "Please enter Potential Hazard", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else if hazardControls.text! == ""
        {
            flag = false
            let alert = UIAlertController(title: nil, message: "Please enter Hazard Control", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else if personResponsible.text! == ""
        {
            flag = false
            let alert = UIAlertController(title: nil, message: "Please enter person responsible", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
        if flag == true
        {
            let potentialhazard = PotentialHazard()
            if let str = taskStepLabel.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    potentialhazard.taskStep = taskStepLabel.text!
                }
            }
            
            if let str = taskStepLabel.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    potentialhazard.potentialHazards = potentialHazards.text!
                }
            }
            
            if let str = taskStepLabel.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    potentialhazard.hazardControls = hazardControls.text!
                }
            }
            
            if let str = taskStepLabel.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    potentialhazard.personResponsible = personResponsible.text!
                }
            }
            
            JSAObject.potentialHazards.append(potentialhazard)
            self.navigationController?.popViewController(animated: true)
        }
     
    }
    
    
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func onDeletePress(_ sender: Any) {
        
        if index != nil{
            JSAObject.potentialHazards.remove(at: index!)
            self.navigationController?.popViewController(animated: true)
            
        }
    }
    
    //MARK: - text field delegates
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.endEditing(true)
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
//        if textField == taskStepLabel
//        {
//            hazardArray[0] = textField.text!
//        }
//        else if textField == potentialHazards
//        {
//            hazardArray[1] = textField.text!
//        }
//        else if textField == personResponsible
//        {
//            hazardArray[3] = textField.text!
//        }
//        else
//        {
//            hazardArray[2] = textField.text!
//        }
    }


}
