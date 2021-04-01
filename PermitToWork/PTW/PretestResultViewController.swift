//
//  PretestResultViewController.swift
//  Murphy_PWT_iOS
//
//  Created by Parul Thakur77 on 10/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit
import Foundation

class PretestResultViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var doneButton: UIBarButtonItem!
    @IBOutlet weak var deleteButton: UIBarButtonItem!
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var preTestTableView: UITableView!
    var permitStr = UserDefaults.standard.string(forKey: "TestResult")
    var PreTestArray = ["O2","Toxic (TYPE)","Toxic (RESULT)","Flammable Gas","Others (TYPE)","Others (RESULT)","Date","Time"]
    var currentModel = TestsModel()
    var index : Int?
    var indexpath : IndexPath?
    var isJsaPreview = true
    var isO2Valid = true
    var isToxicValid = true
    var isOthersValid = true
    private var time: Date?
    private var date: Date?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let nib = UINib(nibName: "PermitCell", bundle: nil)
        preTestTableView.register(nib, forCellReuseIdentifier: "PermitCell")
        preTestTableView.tableFooterView = UIView()
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                //navigationTitle.title = "Stop the job - Review"
                isJsaPreview = true
                deleteButton.isEnabled = false
                
            }
            else if val == "false"
            {
                //navigationTitle.title = "Stop the job"
                isJsaPreview = false
                deleteButton.isEnabled = true
                
            }
        }
        else
        {
            //navigationTitle.title = "Stop the job"
            isJsaPreview = false
            deleteButton.isEnabled = true
            
        }
        
        if indexpath != nil
        {
            if JSAObject.currentFlow == .CWP
            {
                if indexpath?.section == 0
                {
                    currentModel = JSAObject.testResult.preStartTests[(indexpath?.row)!]
                }
                else if indexpath?.section == 1
                {
                    currentModel = JSAObject.testResult.workPeriodTests[(indexpath?.row)!]
                }
                
            }
            else if JSAObject.currentFlow == .HWP
            {
                if indexpath?.section == 0
                {
                    currentModel = JSAObject.testResult.preStartTests[(indexpath?.row)!]
                }
                else if indexpath?.section == 1
                {
                    currentModel = JSAObject.testResult.workPeriodTests[(indexpath?.row)!]
                }
            }
            else if JSAObject.currentFlow == .CSEP
            {
                if indexpath?.section == 0
                {
                    currentModel = JSAObject.testResult.preStartTests[(indexpath?.row)!]
                }
                else if indexpath?.section == 1
                {
                    currentModel = JSAObject.testResult.workPeriodTests[(indexpath?.row)!]
                }
            }
        }
        
        
        if permitStr == "1" || indexpath?.section == 0
        {
            navigationTitle.title = "Pre Start Test"
        }
        else if permitStr == "2" || indexpath?.section == 1
        {
            navigationTitle.title = "Work Period Test"
        }
        
        if indexpath == nil
        {
            deleteButton.isEnabled = false
            doneButton.isEnabled = true
        }
        else
        {
            deleteButton.isEnabled = true
            doneButton.isEnabled = false
        }
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.hideKeyboardWhenTappedAround()
        if currentUser.isReadOnly == true{
            let button: UIButton = UIButton(type: UIButton.ButtonType.custom)
            self.doneButton.customView = button
            self.deleteButton.customView = button
        }
    }
    
    
    @IBAction func onBackPress(_ sender: Any)
    {
        UserDefaults.standard.removeObject(forKey: "TestResult")
        UserDefaults.standard.synchronize()
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onDeletePress(_ sender: Any)
    {
        
        if indexpath?.section == 0
        {
            UserDefaults.standard.removeObject(forKey: "TestResult")
            UserDefaults.standard.synchronize()
            if JSAObject.currentFlow == .CWP
            {
                JSAObject.testResult.preStartTests.remove(at: (indexpath?.row)!)
            }
            else if JSAObject.currentFlow == .HWP
            {
                JSAObject.testResult.preStartTests.remove(at: (indexpath?.row)!)
            }
            else
            {
                JSAObject.testResult.preStartTests.remove(at: (indexpath?.row)!)
            }
            self.navigationController?.popViewController(animated: true)
        }
        else if indexpath?.section == 1
        {
            UserDefaults.standard.removeObject(forKey: "TestResult")
            UserDefaults.standard.synchronize()
            if JSAObject.currentFlow == .CWP
            {
                JSAObject.testResult.workPeriodTests.remove(at: (indexpath?.row)!)
            }
            else if JSAObject.currentFlow == .HWP
            {
                JSAObject.testResult.workPeriodTests.remove(at: (indexpath?.row)!)
            }
            else
            {
                JSAObject.testResult.workPeriodTests.remove(at: (indexpath?.row)!)
            }
            self.navigationController?.popViewController(animated: true)
        }
        else
        {
            
        }
        
    }
    @IBAction func onCheckPress(_ sender: Any)
    {
        
        let validation = self.validateFields()
        
        if permitStr == "1"
        {
            
            if !validation.isValid || !isOthersValid || !isToxicValid || !isO2Valid {
                
                let alertController = UIAlertController(title: nil, message: validation.message, preferredStyle: .alert)
                let action = UIAlertAction(title: "OK", style: .default, handler: nil)
                alertController.addAction(action)
                self.present(alertController, animated: true, completion: nil)
            }
            else
            {
                if JSAObject.currentFlow == .CWP
                {
                    JSAObject.testResult.preStartTests.append(currentModel)
                }
                else if JSAObject.currentFlow == .HWP
                {
                    JSAObject.testResult.preStartTests.append(currentModel)
                }
                else
                {
                    JSAObject.testResult.preStartTests.append(currentModel)
                }
                self.navigateToBack()
            }
        }
        else
        {
            if !validation.isValid || !isOthersValid || !isToxicValid || !isO2Valid {
                
                let alertController = UIAlertController(title: nil, message: validation.message, preferredStyle: .alert)
                let action = UIAlertAction(title: "OK", style: .default, handler: nil)
                alertController.addAction(action)
                self.present(alertController, animated: true, completion: nil)
            }
            else
            {
                if JSAObject.currentFlow == .CWP
                {
                    JSAObject.testResult.workPeriodTests.append(currentModel)
                }
                else if JSAObject.currentFlow == .HWP
                {
                    JSAObject.testResult.workPeriodTests.append(currentModel)
                }
                else
                {
                    JSAObject.testResult.workPeriodTests.append(currentModel)
                }
                self.navigateToBack()
            }
            
        }
        
        
    }
    
    func navigateToBack() {
        
        UserDefaults.standard.removeObject(forKey: "TestResult")
        UserDefaults.standard.synchronize()
        self.navigationController?.popViewController(animated: true)
    }
    
    
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        
        if textField.tag == 0{
           textField.keyboardType = .decimalPad
        }
        else if textField.tag == 1{
            textField.keyboardType = .default
        }
        else if textField.tag == 2{
            textField.keyboardType = .decimalPad
        }
        else if textField.tag == 3{
            textField.keyboardType = .default
        }
        else if textField.tag == 4{
            textField.keyboardType = .default
        }
        else if textField.tag == 5{
            textField.keyboardType = .decimalPad
        }
        else if textField.tag == 6{
            textField.keyboardType = .default
        }
        else{
            textField.keyboardType = .default
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        if textField.tag == 0{
            if textField.text != ""
            {
                if Float(textField.text!) != nil || Int(textField.text!) != nil
                {
                    currentModel.O2 = Float(textField.text!)!
                    isO2Valid = true
                }
                else
                {
                    isO2Valid = false
                    let alertController = UIAlertController(title: "", message: "Invalid number", preferredStyle: .alert)
                    let action = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alertController.addAction(action)
                    self.present(alertController, animated: true, completion: nil)
                }
                
            }
        }
        else if textField.tag == 1{
            currentModel.toxicType = textField.text!
        }
        else if textField.tag == 2{
            if textField.text != ""
            {
                if Float(textField.text!) != nil || Int(textField.text!) != nil
                {
                    isToxicValid = true
                    currentModel.toxicResult = Float(textField.text!)!
                }
                else
                {
                    isToxicValid = false
                    let alertController = UIAlertController(title: "", message: "Invalid number", preferredStyle: .alert)
                    let action = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alertController.addAction(action)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
            
//            if textField.text != ""
//            {
//                currentModel.toxicResult = Float(textField.text!)!
//            }
        }
        else if textField.tag == 3{
            currentModel.flammableGas = textField.text!
        }
        else if textField.tag == 4{
            currentModel.othersType = textField.text!
        }
        else if textField.tag == 5{
            if textField.text != ""
            {
                if Float(textField.text!) != nil || Int(textField.text!) != nil
                {
                    isOthersValid = true
                    currentModel.othersResult = Float(textField.text!)!
                }
                else
                {
                    isOthersValid = false
                    let alertController = UIAlertController(title: "", message: "Invalid number", preferredStyle: .alert)
                    let action = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alertController.addAction(action)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
            
//            if textField.text != ""
//            {
//                currentModel.othersResult = Float(textField.text!)!
//            }
            
        }
        else if textField.tag == 6 {
            //currentModel.Date = textField.text!
        }
        else{
            //currentModel.Time = self.getFormattedTime(date: self.time)
        }
    }
    
   
}
extension PretestResultViewController: UITableViewDataSource, UITableViewDelegate
{
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return PreTestArray.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        cell.valueTextField.tag = indexPath.row
        cell.valueTextField.delegate = self
        cell.valueTextField.isDatePicker = false
        
        if (JSAObject.CWP.header.status.lowercased() == "" || JSAObject.HWP.header.status.lowercased() == "" || JSAObject.CSEP.header.status.lowercased() == "") && isJsaPreview == false
        {
            cell.isUserInteractionEnabled = true
        }
        else
        {
            cell.isUserInteractionEnabled = false
        }
        
        //if index != nil{
            if indexPath.row == 0{
                cell.setData(key: PreTestArray[indexPath.row], value: String(currentModel.O2))
            }
            else if indexPath.row == 1{
                cell.setData(key: PreTestArray[indexPath.row], value: currentModel.toxicType)
            }
            else if indexPath.row == 2{
                cell.setData(key: PreTestArray[indexPath.row], value: currentModel.toxicResult.description)
            }
            else if indexPath.row == 3{
                cell.setData(key: PreTestArray[indexPath.row], value: currentModel.flammableGas)
            }
            else if indexPath.row == 4{
                cell.setData(key: PreTestArray[indexPath.row], value: currentModel.othersType)
            }
            else if indexPath.row == 5{
                cell.setData(key: PreTestArray[indexPath.row], value: currentModel.othersResult.description)
            }
            else if indexPath.row == 6{
                cell.valueTextField.isDatePicker = true
                cell.valueTextField.datePickerMode = .date
                let date = currentModel.Date.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)
                if date != nil{
                    cell.setData(key: PreTestArray[indexPath.row], value: date!)
                }
                else{
                    cell.setData(key: PreTestArray[indexPath.row], value: "")
                }
            }
            else{
                cell.valueTextField.isDatePicker = true
                cell.valueTextField.datePickerMode = .time
                let date = currentModel.Time.convertToDateString(format: .short, currentDateStringFormat: .hourMinuteSeconds, shouldConvertFromUTC: true)
                if date != nil{
                    cell.setData(key: PreTestArray[indexPath.row], value: date!)
                }
                else{
                    cell.setData(key: PreTestArray[indexPath.row], value: "")
                }
            }
//        }
//        else{
//            cell.setData(key: PreTestArray[indexPath.row], value: "")
//        }
        cell.selectionStyle = .none
        if currentUser.isReadOnly == true{
            cell.isUserInteractionEnabled = false
        }
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 55
    }
}

extension PretestResultViewController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
        if textField.tag == 6 {
            self.date = date
            if self.date != nil{
                currentModel.Date = date.toDateFormat(.long).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
            textField.text = date.toDateFormat(.dayMonthYear)
            }
        }
        else{
            self.time = date
            if self.time != nil{
                currentModel.Time = date.toDateFormat(.hourMinuteSeconds).convertToDateString(format: .hourMinuteSeconds, currentDateStringFormat: .hourMinuteSeconds, shouldConvertToUTC: true)!
                textField.text = date.toDateFormat(.short)
            }
        }
    }
    
    func getFormattedTime(date: Date?) -> String {
        
        guard let value = date else { return "" }
        let formattedValue = value.hour!.description + ":" + value.minutes!.description + ":" + value.seconds!.description
        return formattedValue
        
    }
}


extension PretestResultViewController  {
    
    func validateFields() -> (isValid: Bool, message: String) {
        
        guard !currentModel.Date.isEmpty else {
            return (false, "Please enter date")
        }
        guard !currentModel.Time.isEmpty else {
            return (false, "Please enter time")
        }
        return (true, "")
    }
}
