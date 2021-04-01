//
//  AddNewJobViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 26/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class AddNewJobViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var triggerTextField: UITextField!
    @IBOutlet var doneNavButton: UIBarButtonItem!
    @IBOutlet var deleteNavButton: UIBarButtonItem!
    var index : Int?
    var currentJob : String = ""
    var tempArray = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        triggerTextField.delegate = self
        if index != nil{
            currentJob = JSAObject.stopTheJob[index!]
            triggerTextField.text = currentJob
            deleteNavButton.isEnabled = true
            doneNavButton.isEnabled = false
        }
        else{
            deleteNavButton.isEnabled = false
            doneNavButton.isEnabled = true
        }
        tempArray = JSAObject.stopTheJob
        if let create = UserDefaults.standard.string(forKey: "JSACreate"), let preview = UserDefaults.standard.string(forKey: "JSAPreview") {
            if (create == "true" || JSAObject.status.lowercased() == "submitted") && preview == "false"{
                triggerTextField.isUserInteractionEnabled = true
            }
            else{
                navigationTitle.title = "Trigger Details - Review"
                triggerTextField.isUserInteractionEnabled = false
                deleteNavButton.isEnabled = false
            }
        }
        else{
            triggerTextField.isUserInteractionEnabled = false
            deleteNavButton.isEnabled = false
        }
        
    }
    
    @IBAction func doneBtn(_ sender: Any)
    {
        
        if triggerTextField.text == ""
        {
            let alert = UIAlertController(title: nil, message: "Trigger field cannot be empty", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if let str = triggerTextField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 50 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    tempArray.append(self.triggerTextField.text!)
                    JSAObject.stopTheJob = tempArray
                    self.navigationController?.popViewController(animated: true)
                }
            }
           
        }
    }
    
    @IBAction func onDeletePress(_ sender: Any) {
        if index != nil{
            JSAObject.stopTheJob.remove(at: index!)
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    //MARK: - text field delegates
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        textField.endEditing(true)
        return true
    }

}
