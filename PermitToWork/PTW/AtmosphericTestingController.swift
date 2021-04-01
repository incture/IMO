//
//  AtmosphericTestingController.swift
//  Murphy IOP
//
//  Created by Soumya Singh on 26/03/18.
//  Copyright © 2018 soumyaIncture Technologies. All rights reserved.
//

import UIKit

class AtmosphericTestingController: UIViewController, UITextFieldDelegate {
    
    
    @IBOutlet var testTable: UITableView!
    var selectStatus : Bool = false
    var switchStatus : Bool = false
    var sectionsLabel = ["Area and/or equipment to be tested", "Required Tests", "Test Frequency"]
    var tableContent = [["Detector Used","Date of last Calibration"],["O2(19.5-23%)", "LELs(< 5%)", "H2S(< 10 ppm)", "Other"],["Prior to work Commencing","Each work period or every hours","Continuous gas monitoring required"]]
    var requiredTest = [false, false, false,false]
    var testFrequency = [false, false, false]
    var detectorUsed : String = ""
    var dateOfLastCalibration : Date?
    
    let atmosphericTesting = AtmosphericTesting()
    var hotPermit = HotPermit()
    var coldPermit : ColdPermit?
    var confineSpacePermit = ConfinedSpacePermit()
    private var eachWorkPeriodOn = false
    var isPermitReview = true
    
    var selectedWorkTypeCell: [Int: Bool]?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //createNavBar()
        
        self.selectedWorkTypeCell = [Int: Bool]()
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                //navigationTitle.title = "JSA - Review"
                isPermitReview = true
            }
            else if val == "false"
            {
                //navigationTitle.title = "JSA"
                isPermitReview = false
            }
        }
        else
        {
            //navigationTitle.title = "JSA"
            isPermitReview = false
        }
        
        let permitNib = UINib(nibName: "PermitCell", bundle: nil)
        testTable.register(permitNib, forCellReuseIdentifier: "PermitCell")
        let workTypeNib = UINib(nibName: "WorkTypeCell", bundle: nil)
        testTable.register(workTypeNib, forCellReuseIdentifier: "WorkTypeCell")
        let frequencyNib = UINib(nibName: "FrequencyCell", bundle: nil)
        if let Cdata = UserDefaults.standard.data(forKey: "coldPermit"){
            
            self.coldPermit = NSKeyedUnarchiver.unarchiveObject(with: Cdata) as? ColdPermit
            
        }
        testTable.register(frequencyNib, forCellReuseIdentifier: "FrequencyCell")
        testTable.tableFooterView = UIView()
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func createNavBar(){
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0)
        navigationItem.title = "Atmospheric Test Record"
        
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onPreviousPress(_ sender: UIButton) {
        dismissScreen()
    }
    
    @IBAction func onNextPress(_ sender: UIButton) {
        
       // var validation = self.validateFields()
//        if isPermitReview
//        {
//            validation.isValid = true
//        }
        
//        if !validation.isValid {
//
//            let alertController = UIAlertController(title: nil, message: validation.message, preferredStyle: .alert)
//            let action = UIAlertAction(title: "OK", style: .default, handler: nil)
//            alertController.addAction(action)
//            self.present(alertController, animated: true, completion: nil)
//
//
//        } else {
        
            if JSAObject.currentFlow == .CWP{
                if self.eachWorkPeriodOn {
                    if JSAObject.atmosphericTesting.noHours == 0 {
                        self.showEachHoursAlert()
                    } else {
                        self.showTestResultController()
                    }
                } else {
                    self.showTestResultController()
                }
            }
            else if JSAObject.currentFlow == .HWP{
                if self.eachWorkPeriodOn {
                    if JSAObject.atmosphericTesting.noHours == 0 {
                        self.showEachHoursAlert()
                    } else {
                        self.showTestResultController()
                    }
                } else {
                    self.showTestResultController()
                }
            }
            else if JSAObject.currentFlow == .CSEP{
                if self.eachWorkPeriodOn {
                    if JSAObject.atmosphericTesting.noHours == 0 {
                        self.showEachHoursAlert()
                    } else {
                        self.showTestResultController()
                    }
                } else {
                    self.showTestResultController()
                }
            }
       // }
        
        
    }
    
    func showTestResultController() {
        
        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "TestResultController") as! TestResultController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func onHomePress(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        JSAObject = JSA()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        if JSAObject.currentFlow == .CWP {
            if textField.tag == 0 {
                JSAObject.atmosphericTesting.detectorUsed = textField.text!
            }
            else if textField.tag == 1{
                if self.dateOfLastCalibration != nil{
                    let date = self.dateOfLastCalibration!.toDateFormat(.long)
                    JSAObject.atmosphericTesting.dateOfLastCallibration = date.convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
                }
            }
            else if textField.tag == 100{
                JSAObject.atmosphericTesting.areaOrEquipmentTotest = textField.text!
            }
            else{
                JSAObject.atmosphericTesting.Other = textField.text!
            }
        } else if JSAObject.currentFlow == .HWP {
            if textField.tag == 0 {
                JSAObject.atmosphericTesting.detectorUsed = textField.text!
            }
            else if textField.tag == 1{
                if self.dateOfLastCalibration != nil{
                    let date = self.dateOfLastCalibration!.toDateFormat(.long)
                    JSAObject.atmosphericTesting.dateOfLastCallibration = date.convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
                }
            }
            else if textField.tag == 100{
                JSAObject.atmosphericTesting.areaOrEquipmentTotest = textField.text!
            }
            else{
                JSAObject.atmosphericTesting.Other = textField.text!
            }
        }
        else if JSAObject.currentFlow == .CSEP {
            if textField.tag == 0 {
                JSAObject.atmosphericTesting.detectorUsed = textField.text!
            }
            else if textField.tag == 1{
                if self.dateOfLastCalibration != nil{
                let date = self.dateOfLastCalibration!.toDateFormat(.long)
                JSAObject.atmosphericTesting.dateOfLastCallibration = date.convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
                }
            }
            else if textField.tag == 100{
                JSAObject.atmosphericTesting.areaOrEquipmentTotest = textField.text!
            }
            else{
                JSAObject.atmosphericTesting.Other = textField.text!
            }
        }
        
        
        
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
}

extension AtmosphericTestingController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return sectionsLabel.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableContent[section].count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell1 = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        let cell2 = tableView.dequeueReusableCell(withIdentifier: "WorkTypeCell")! as! WorkTypeCell
        let cell3 = tableView.dequeueReusableCell(withIdentifier: "FrequencyCell")! as! FrequencyCell
        
        cell2.delegate = self
        
        if JSAObject.currentFlow == .CWP{
            if isPermitReview || JSAObject.CWP.header.status.lowercased() == "approved"
            {
                cell1.isUserInteractionEnabled = false
                cell2.isUserInteractionEnabled = false
                cell3.isUserInteractionEnabled = false
            }
            else
            {
                cell1.isUserInteractionEnabled = true
                cell2.isUserInteractionEnabled = true
                cell3.isUserInteractionEnabled = true
            }
            
            
            
            if indexPath.section == 0{
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.tag = indexPath.row
                cell1.valueTextField.delegate = self
                cell1.valueTextField.isDatePicker = false
                if indexPath.row == 0{
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: JSAObject.atmosphericTesting.detectorUsed)
                }
                else{
                    cell1.valueTextField.isDatePicker = true
                    cell1.valueTextField.datePickerMode = .date
                    var value = JSAObject.atmosphericTesting.dateOfLastCallibration
                    if !value.isEmpty, let dateOfLastCallibration = value.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC : true) {
                        value = dateOfLastCallibration
                    }
                    
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: value)
                }
                cell1.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell1.isUserInteractionEnabled = false
                }
                return cell1
            }
            else if indexPath.section == 1{
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton.tag = indexPath.row
                if indexPath.row == 0{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.O2)
                }
                else if indexPath.row == 1{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.Lels)
                }
                else if indexPath.row == 2{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.H2S)
                }
                else{
                    cell1.clipsToBounds = true
                    cell1.keyLabel.numberOfLines = 0
                    cell1.valueTextField.tag = indexPath.row
                    cell1.valueTextField.delegate = self
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: JSAObject.atmosphericTesting.Other)
                    cell1.selectionStyle = .none
                    if currentUser.isReadOnly == true{
                        cell1.isUserInteractionEnabled = false
                    }
                    return cell1
                }
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell2.isUserInteractionEnabled = false
                }
                return cell2
            }
            else{
                cell3.clipsToBounds = true
                cell3.setLabel(data: tableContent[indexPath.section][indexPath.row])
                cell3.permitSwitch.tag = indexPath.row
                if indexPath.row == 0{
                    if JSAObject.atmosphericTesting.priorToWorkCommencing == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        cell3.permitSwitch.isOn = true
                    }
                }
                else if indexPath.row == 1{
                    if JSAObject.atmosphericTesting.eachWorkPeriod == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        let data = tableContent[indexPath.section][indexPath.row] + ": " + JSAObject.atmosphericTesting.noHours.description
                        cell3.setLabel(data: data)
                        
                        cell3.permitSwitch.isOn = true
                        
                    }
                }
                else{
                    if JSAObject.atmosphericTesting.continuousMonitoringreqd == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        cell3.permitSwitch.isOn = true
                    }
                }
                cell3.permitSwitch.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
                cell3.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell3.isUserInteractionEnabled = false
                }
                return cell3
            }
        }
        else if JSAObject.currentFlow == .HWP {
            if isPermitReview || JSAObject.HWP.header.status.lowercased() == "approved"
            {
                cell1.isUserInteractionEnabled = false
                cell2.isUserInteractionEnabled = false
                cell3.isUserInteractionEnabled = false
            }
            else
            {
                cell1.isUserInteractionEnabled = true
                cell2.isUserInteractionEnabled = true
                cell3.isUserInteractionEnabled = true
            }
            
            
            
            if indexPath.section == 0{
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.tag = indexPath.row
                cell1.valueTextField.delegate = self
                cell1.valueTextField.isDatePicker = false
                if indexPath.row == 0{
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: JSAObject.atmosphericTesting.detectorUsed)
                }
                else{
                    cell1.valueTextField.isDatePicker = true
                    cell1.valueTextField.datePickerMode = .date
                    var value = JSAObject.atmosphericTesting.dateOfLastCallibration
                    if !value.isEmpty, let dateOfLastCallibration = value.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC : true) {
                        value = dateOfLastCallibration
                    }
                    
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: value)
                    
                }
                cell1.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell1.isUserInteractionEnabled = false
                }
                return cell1
            }
            else if indexPath.section == 1{
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton.tag = indexPath.row
                if indexPath.row == 0{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.O2)
                }
                else if indexPath.row == 1{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.Lels)
                }
                else if indexPath.row == 2{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.H2S)
                }
                else{
                    cell1.clipsToBounds = true
                    cell1.keyLabel.numberOfLines = 0
                    cell1.valueTextField.tag = indexPath.row
                    cell1.valueTextField.delegate = self
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: JSAObject.atmosphericTesting.Other)
                    cell1.selectionStyle = .none
                    if currentUser.isReadOnly == true{
                        cell1.isUserInteractionEnabled = false
                    }
                    return cell1
                }
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell2.isUserInteractionEnabled = false
                }
                return cell2
            }
            else{
                cell3.clipsToBounds = true
                cell3.setLabel(data: tableContent[indexPath.section][indexPath.row])
                cell3.permitSwitch.tag = indexPath.row
                if indexPath.row == 0{
                    if JSAObject.atmosphericTesting.priorToWorkCommencing == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        cell3.permitSwitch.isOn = true
                    }
                }
                else if indexPath.row == 1{
                    if JSAObject.atmosphericTesting.eachWorkPeriod == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        let data = tableContent[indexPath.section][indexPath.row] + ": " + JSAObject.atmosphericTesting.noHours.description
                        cell3.setLabel(data: data)
                        
                        cell3.permitSwitch.isOn = true
                        
                    }
                }
                else{
                    if JSAObject.atmosphericTesting.continuousMonitoringreqd == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        cell3.permitSwitch.isOn = true
                    }
                }
                cell3.permitSwitch.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
                cell3.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell3.isUserInteractionEnabled = false
                }
                return cell3
            }
        }
        else{
            if isPermitReview || JSAObject.CSEP.header.status.lowercased() == "approved"
            {
                cell1.isUserInteractionEnabled = false
                cell2.isUserInteractionEnabled = false
                cell3.isUserInteractionEnabled = false
            }
            else
            {
                cell1.isUserInteractionEnabled = true
                cell2.isUserInteractionEnabled = true
                cell3.isUserInteractionEnabled = true
            }
            
            
            
            if indexPath.section == 0{
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.tag = indexPath.row
                cell1.valueTextField.delegate = self
                cell1.valueTextField.isDatePicker = false
                if indexPath.row == 0{
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: JSAObject.atmosphericTesting.detectorUsed)
                }
                else{
                    cell1.valueTextField.isDatePicker = true
                    cell1.valueTextField.datePickerMode = .date
                    var value = JSAObject.atmosphericTesting.dateOfLastCallibration
                    if !value.isEmpty, let dateOfLastCallibration = value.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC : true) {
                        value = dateOfLastCallibration
                    }
                    
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: value)
                }
                cell1.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell1.isUserInteractionEnabled = false
                }
                return cell1
            }
            else if indexPath.section == 1{
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton.tag = indexPath.row
                if indexPath.row == 0{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.O2)
                }
                else if indexPath.row == 1{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.Lels)
                }
                else if indexPath.row == 2{
                    cell2.setSelectionStatus(status: JSAObject.atmosphericTesting.H2S)
                }
                else{
                    cell1.clipsToBounds = true
                    cell1.keyLabel.numberOfLines = 0
                    cell1.valueTextField.tag = indexPath.row
                    cell1.valueTextField.delegate = self
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: JSAObject.atmosphericTesting.Other)
                    cell1.selectionStyle = .none
                    if currentUser.isReadOnly == true{
                        cell1.isUserInteractionEnabled = false
                    }
                    return cell1
                }
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell2.isUserInteractionEnabled = false
                }
                return cell2
            }
            else{
                cell3.clipsToBounds = true
                cell3.setLabel(data: tableContent[indexPath.section][indexPath.row])
                cell3.permitSwitch.tag = indexPath.row
                if indexPath.row == 0{
                    if JSAObject.atmosphericTesting.priorToWorkCommencing == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        cell3.permitSwitch.isOn = true
                    }
                }
                else if indexPath.row == 1{
                    if JSAObject.atmosphericTesting.eachWorkPeriod == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        let data = tableContent[indexPath.section][indexPath.row] + ": " + JSAObject.atmosphericTesting.noHours.description
                        cell3.setLabel(data: data)
                        
                        cell3.permitSwitch.isOn = true
                        
                    }
                }
                else{
                    if JSAObject.atmosphericTesting.continuousMonitoringreqd == 0{
                        cell3.permitSwitch.isOn = false
                    }
                    else{
                        cell3.permitSwitch.isOn = true
                    }
                }
                cell3.permitSwitch.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
                cell3.selectionStyle = .none
                if currentUser.isReadOnly == true{
                    cell3.isUserInteractionEnabled = false
                }
                return cell3
            }
        }
        
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0{
            return 70
        }
        else{
            return 55
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
        headerView.isUserInteractionEnabled = true
        var label = UILabel(frame: CGRect(x: 16, y: -8, width: headerView.frame.size.width/2, height: 45))
        label.text = sectionsLabel[section]
        label.font = UIFont.boldSystemFont(ofSize: 16)
        label.textColor = UIColor.white
        
        if section == 0{
            
            label = UILabel(frame: CGRect(x: 16, y: 4, width: headerView.frame.size.width/2, height: 45))
            label.text = sectionsLabel[section]
            label.font = UIFont.boldSystemFont(ofSize: 16)
            label.textColor = UIColor.white
            
            label.numberOfLines = 0
            let textField = UITextField(frame: CGRect(x: label.frame.maxX , y: 4, width: headerView.frame.size.width/2 - 24, height: 45))
            textField.tag = 100
            textField.borderStyle = .none
            textField.delegate = self
            textField.font = textField.font?.withSize(14)
            textField.placeholder = "Enter Area/Equipment"
            textField.textAlignment = .right
            textField.textColor = .white
            textField.placeHolderColor = .darkGray
            if JSAObject.currentFlow == .CWP {
                if isPermitReview || JSAObject.CWP.header.status.lowercased() == "approved"{
                    textField.isUserInteractionEnabled = false
                }else{
                    textField.isUserInteractionEnabled = true
                }
               textField.text = JSAObject.atmosphericTesting.areaOrEquipmentTotest
            } else if JSAObject.currentFlow == .HWP {
                if isPermitReview || JSAObject.HWP.header.status.lowercased() == "approved"{
                    textField.isUserInteractionEnabled = false
                }else{
                    textField.isUserInteractionEnabled = true
                }
                textField.text = JSAObject.atmosphericTesting.areaOrEquipmentTotest
            }
            else if JSAObject.currentFlow == .CSEP {
                if isPermitReview || JSAObject.CSEP.header.status.lowercased() == "approved"{
                    textField.isUserInteractionEnabled = false
                }else{
                    textField.isUserInteractionEnabled = true
                }
                textField.text = JSAObject.atmosphericTesting.areaOrEquipmentTotest
            }
            headerView.addSubview(textField)
        }
        headerView.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)//UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        
        if section == 0{
            return 50
        }else{
            return 30
        }
    }
    
    @objc func buttonSelected(_ sender: UIButton)
    {
        var buttonStatus : Bool?
        if JSAObject.currentFlow == .CWP{
            
            if sender.tag == 0{
                if JSAObject.atmosphericTesting.O2 == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.O2 = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.O2 = 0
                }
            }
            else if sender.tag == 1{
                if JSAObject.atmosphericTesting.Lels == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.Lels = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.Lels = 0
                }
            }
            else if sender.tag == 2{
                // else{
                if JSAObject.atmosphericTesting.H2S == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.H2S = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.H2S = 0
                }
            } else {
                if JSAObject.atmosphericTesting.other == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.other = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.other = 0
                }
            }
        }
        else if JSAObject.currentFlow == .HWP{
            if sender.tag == 0{
                if JSAObject.atmosphericTesting.O2 == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.O2 = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.O2 = 0
                }
            }
            else if sender.tag == 1{
                if JSAObject.atmosphericTesting.Lels == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.Lels = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.Lels = 0
                }
            }
            else if sender.tag == 2{
                // else{
                if JSAObject.atmosphericTesting.H2S == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.H2S = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.H2S = 0
                }
            } else {
                if JSAObject.atmosphericTesting.other == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.other = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.other = 0
                }
            }
        }
        else if JSAObject.currentFlow == .CSEP{
            if sender.tag == 0{
                if JSAObject.atmosphericTesting.O2 == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.O2 = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.O2 = 0
                }
            }
            else if sender.tag == 1{
                if JSAObject.atmosphericTesting.Lels == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.Lels = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.Lels = 0
                }
            }
            else if sender.tag == 2{
                // else{
                if JSAObject.atmosphericTesting.H2S == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.H2S = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.H2S = 0
                }
            } else {
                if JSAObject.atmosphericTesting.other == 0{
                    buttonStatus = true
                    JSAObject.atmosphericTesting.other = 1
                }
                else{
                    buttonStatus = false
                    JSAObject.atmosphericTesting.other = 0
                }
            }
        }
        
        self.selectedWorkTypeCell?[sender.tag] = buttonStatus
        if buttonStatus! {
            sender.setImage(UIImage (named: "checked"), for: .normal)
        }else {
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
        }
    }
    
    @objc func onSwitch(_ sender: UISwitch)
    {
        var switchStatus : Bool?
        
        if JSAObject.currentFlow == .CWP{
            if sender.tag == 0{
                if JSAObject.atmosphericTesting.priorToWorkCommencing == 0{
                    switchStatus = true
                    JSAObject.atmosphericTesting.priorToWorkCommencing = 1
                }
                else{
                    switchStatus = false
                    JSAObject.atmosphericTesting.priorToWorkCommencing = 0
                }
            }
            else if sender.tag == 1{
                if JSAObject.atmosphericTesting.eachWorkPeriod == 0{
                    switchStatus = true
                    self.eachWorkPeriodOn = switchStatus!
                    JSAObject.atmosphericTesting.eachWorkPeriod = 1
                    self.showEachHoursAlert()
                }
                else{
                    switchStatus = false
                    self.eachWorkPeriodOn = switchStatus!
                    JSAObject.atmosphericTesting.eachWorkPeriod = 0
                    self.updateNuberOf(hours: "0")
                }
                
            }
            else{
                if JSAObject.atmosphericTesting.continuousMonitoringreqd == 0{
                    switchStatus = true
                    JSAObject.atmosphericTesting.continuousMonitoringreqd = 1
                }
                else{
                    switchStatus = false
                    JSAObject.atmosphericTesting.continuousMonitoringreqd = 0
                }
            }
        }
        else if JSAObject.currentFlow == .HWP{
            if sender.tag == 0{
                if JSAObject.atmosphericTesting.priorToWorkCommencing == 0{
                    switchStatus = true
                    JSAObject.atmosphericTesting.priorToWorkCommencing = 1
                }
                else{
                    switchStatus = false
                    JSAObject.atmosphericTesting.priorToWorkCommencing = 0
                }
            }
            else if sender.tag == 1{
                if JSAObject.atmosphericTesting.eachWorkPeriod == 0{
                    switchStatus = true
                    self.eachWorkPeriodOn = switchStatus!
                    JSAObject.atmosphericTesting.eachWorkPeriod = 1
                    self.showEachHoursAlert()
                }
                else{
                    switchStatus = false
                    self.eachWorkPeriodOn = switchStatus!
                    JSAObject.atmosphericTesting.eachWorkPeriod = 0
                    self.updateNuberOf(hours: "0")
                }
                
            }
            else{
                if JSAObject.atmosphericTesting.continuousMonitoringreqd == 0{
                    switchStatus = true
                    JSAObject.atmosphericTesting.continuousMonitoringreqd = 1
                }
                else{
                    switchStatus = false
                    JSAObject.atmosphericTesting.continuousMonitoringreqd = 0
                }
            }
        }
        else if JSAObject.currentFlow == .CSEP{
            if sender.tag == 0{
                if JSAObject.atmosphericTesting.priorToWorkCommencing == 0{
                    switchStatus = true
                    JSAObject.atmosphericTesting.priorToWorkCommencing = 1
                }
                else{
                    switchStatus = false
                    JSAObject.atmosphericTesting.priorToWorkCommencing = 0
                }
            }
            else if sender.tag == 1{
                if JSAObject.atmosphericTesting.eachWorkPeriod == 0{
                    switchStatus = true
                    self.eachWorkPeriodOn = switchStatus!
                    JSAObject.atmosphericTesting.eachWorkPeriod = 1
                    self.showEachHoursAlert()
                }
                else{
                    switchStatus = false
                    self.eachWorkPeriodOn = switchStatus!
                    JSAObject.atmosphericTesting.eachWorkPeriod = 0
                    self.updateNuberOf(hours: "0")
                }
                
            }
            else{
                if JSAObject.atmosphericTesting.continuousMonitoringreqd == 0{
                    switchStatus = true
                    JSAObject.atmosphericTesting.continuousMonitoringreqd = 1
                }
                else{
                    switchStatus = false
                    JSAObject.atmosphericTesting.continuousMonitoringreqd = 0
                }
            }
        }
        sender.isOn = switchStatus!
    }
}

extension AtmosphericTestingController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
        if textField.tag == 1{
            self.dateOfLastCalibration = date
            textField.text = date.toDateFormat(.dayMonthYear)
        }
    }
}


extension AtmosphericTestingController  {
    
    func validateFields() -> (isValid: Bool, message: String) {
        
        if JSAObject.currentFlow == .HWP {
            
            guard !JSAObject.atmosphericTesting.detectorUsed.isEmpty else {
                return (false, "Please enter detector used")
            }
            guard self.selectedWorkTypeCell!.filter({$0.value == true }).count > 0 else {
                return (false, "Please select atleast one required test")
            }
            
        } else if JSAObject.currentFlow == .CWP {
            
            guard !JSAObject.atmosphericTesting.detectorUsed.isEmpty else {
                return (false, "Please enter detector used")
            }
            guard self.selectedWorkTypeCell!.filter({$0.value == true }).count > 0 else {
                return (false, "Please select atleast one required test")
            }
        }
        else if JSAObject.currentFlow == .CSEP {
            
            guard !JSAObject.atmosphericTesting.detectorUsed.isEmpty else {
                return (false, "Please enter detector used")
            }
            guard self.selectedWorkTypeCell!.filter({$0.value == true }).count > 0 else {
                return (false, "Please select atleast one required test")
            }
        }
        
        
        
        return (true, "")
        
    }
    
    func showEachHoursAlert() {
        
        let message = "Please enter number of hours"
        let alertController = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        let action = UIAlertAction(title: "OK", style: .default) { _ in
            let text = alertController.textFields!.first!.text!
            if text.isEmpty {
                self.showEachHoursAlert()
            } else {
                self.updateNuberOf(hours: text)
            }
        }
        let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(cancel)
        alertController.addAction(action)
        alertController.addTextField { (textField) in
            textField.keyboardType = .numberPad
            textField.placeholder = message
        }
        self.present(alertController, animated: true, completion: nil)
    }
    
    func updateNuberOf(hours: String) {
        
        if JSAObject.currentFlow == .CWP{
            JSAObject.atmosphericTesting.noHours = Int(hours)!
        }
        else  if JSAObject.currentFlow == .CSEP{
            JSAObject.atmosphericTesting.noHours = Int(hours)!
        }
        else{
            JSAObject.atmosphericTesting.noHours = Int(hours)!
        }
        DispatchQueue.main.async {
        self.testTable.reloadData()
        }
    }
}


extension AtmosphericTestingController: WorkTypeCellDelegate {}
