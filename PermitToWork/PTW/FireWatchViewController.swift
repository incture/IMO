//
//  FireWatchViewController.swift
// 
//
//  Created by Parul Thakur77 on 11/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class FireWatchViewController: UIViewController, UITextFieldDelegate {
    var permitStr = UserDefaults.standard.string(forKey: "Permit")
    @IBOutlet var fireWatchtableView: UITableView!
    var selectStatus : Bool = false
    var dateNtime : Date?
    var printName : String?
    var dateValue : String?
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    //    var switchArr = ["Work associated with this permit has been completed"]
    //    var closeOut = ["Work associated with this permit has been completed","Permit Holder name","Work Status Comments"]
    
    
    var hotPermit = ["Fire Watch (Print Name) ", "Date/Time ", "I inspected the work areas 30 minutes after the work was completed and the areas were found to be safe from fire."]
    var closedSpace = ["Trained/Designated Safety Standby", "Trained/Designated Rescue Team Members (Minimum 2)", "Rescue Equipment Required"]
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        let permitNib = UINib(nibName: "PermitCell", bundle: nil)
        fireWatchtableView.register(permitNib, forCellReuseIdentifier: "PermitCell")
        let workTypeNib = UINib(nibName: "WorkTypeCell", bundle: nil)
        fireWatchtableView.register(workTypeNib, forCellReuseIdentifier: "WorkTypeCell")
        let frequencyNib = UINib(nibName: "FrequencyCell", bundle: nil)
        fireWatchtableView.register(frequencyNib, forCellReuseIdentifier: "FrequencyCell")
        fireWatchtableView.tableFooterView = UIView()
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func createNavBar(){
        if JSAObject.currentFlow == .HWP
        {
            navigationTitle.title = "Fire Watch"
        }
        else if JSAObject.currentFlow == .CSEP
        {
            navigationTitle.title = "Rescue Plan"
        }
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0)
       
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 0{
            CWObject.firewatch.fireWatch = textField.text!
        }
        else if textField.tag == 1{
            CWObject.firewatch.DatenTime = textField.text!
        }
        
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onPreviousPress(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onNextPress(_ sender: Any)
    {
        let validation = self.validateFields()
        if !validation.isValid {
            
            let alertController = UIAlertController(title: nil, message: validation.message, preferredStyle: .alert)
            let action = UIAlertAction(title: "OK", style: .default, handler: nil)
            alertController.addAction(action)
            self.present(alertController, animated: true, completion: nil)
        } else {
        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitCloseOutController") as! PermitCloseOutController
        self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    @IBAction func onHomePress(_ sender: Any)
    {
    self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        //self.navigationController?.popToRootViewController(animated: true)
    }
    
}
extension FireWatchViewController: UITableViewDataSource, UITableViewDelegate {
    
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if JSAObject.currentFlow == .HWP{
            return hotPermit.count
        }
        else{
            return closedSpace.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell1 = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        let cell2 = tableView.dequeueReusableCell(withIdentifier: "WorkTypeCell")! as! WorkTypeCell
        
        if indexPath.row == 2
        {
            cell2.clipsToBounds = true
            cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
            if JSAObject.currentFlow == .HWP{
                cell2.setData(labelValue: hotPermit[indexPath.row])
            }
            else{
                cell2.setData(labelValue: closedSpace[indexPath.row])
            }
            cell2.selectionStyle = .none
            return cell2
        }
        else
        {
            
            cell1.clipsToBounds = true
            cell1.keyLabel.numberOfLines = 0
            if JSAObject.currentFlow == .HWP{
                if  indexPath.row == 0{
                    cell1.setData(key: hotPermit[indexPath.row], value: CWObject.firewatch.fireWatch)
                }
                else{
                     cell1.setData(key: hotPermit[indexPath.row], value: CWObject.firewatch.DatenTime)
                }
                
            }
            else{
                if  indexPath.row == 0{
                    cell1.setData(key: closedSpace[indexPath.row], value: CWObject.firewatch.fireWatch)
                }
                else{
                    cell1.setData(key: closedSpace[indexPath.row], value: CWObject.firewatch.DatenTime)
                }
            }
            cell1.valueTextField.tag = indexPath.row
            cell1.valueTextField.delegate = self
            if indexPath.row == 1 && JSAObject.currentFlow == .HWP{
                cell1.valueTextField.isDatePicker = true
                cell1.valueTextField.datePickerMode = .dateAndTime
            }
            cell1.selectionStyle = .none
            return cell1
        }
    }
    
   
   
    
    @objc func buttonSelected(_ sender: AnyObject)
    {
        
        var buttonStatus : Bool?
            if CWObject.firewatch.iInspected == 0{
                buttonStatus = true
                CWObject.firewatch.iInspected = 1
            }
            else{
                buttonStatus = false
                CWObject.firewatch.iInspected = 0
            }
    
        if buttonStatus! {
            sender.setImage(UIImage (named: "checked"), for: .normal)
        }else {
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
        }
        
    }
    
    func validateFields() -> (isValid: Bool, message: String) {
        
        if JSAObject.currentFlow == .HWP{
            guard !CWObject.firewatch.fireWatch.isEmpty else {
                return (false, "Please enter Name")
            }
            guard !CWObject.firewatch.DatenTime.isEmpty else {
                return (false, "Please enter Date")
            }
            guard CWObject.firewatch.iInspected == 1 else {
                return (false, "Please Confirm!")
            }
            
            return (true, "")
        }
        else{
            guard !CWObject.firewatch.fireWatch.isEmpty else {
                return (false, "Please enter name")
            }
            guard !CWObject.firewatch.DatenTime.isEmpty else {
                return (false, "Please enter serial number")
            }
            guard CWObject.firewatch.iInspected == 1 else {
                return (false, "Please Confirm!")
            }
            
            return (true, "")
        }
    }
}

extension FireWatchViewController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
        if JSAObject.currentFlow == .HWP{
            if textField.tag == 1 {
                CWObject.firewatch.DatenTime = date.toDateFormat(.long)
                dateNtime = date
                textField.text = date.toDateFormat(.dayMonthYearwithTime)
            }
        }
        else{
            if textField.tag == 1 {
                CWObject.firewatch.DatenTime = date.toDateFormat(.long)
                dateNtime = date
                textField.text = date.toDateFormat(.dayMonthYearwithTime)
            }
        }
        
    }
}
