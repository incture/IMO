//
//  RiskAssessmentCell.swift
//  Task-Management
//
//  Created by Soumya Singh on 17/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

protocol RiskAssessmentKeyboardDismissProtocol {
    func dismissKeyboard()
}

class RiskAssessmentCell: UITableViewCell, UITextFieldDelegate{
    
    @IBOutlet var cellHeightConstraint: NSLayoutConstraint!
    @IBOutlet var riskView: UITableView!
    var delegate : RiskAssessmentKeyboardDismissProtocol?
    var sender : RiskAssesmentViewController?
    
    
    var sectionArray = ["",
                        "",
                        "Eye Protection",
                        "Hearing Protection",
                        "",
                        "",
                        "",
                        "Gloves",
                        "",
                        "",
                        ""]
    var items = [[],
                 ["Hard Hat", "Safety Shoes / Boots"],
                 ["Safety Glasses", "Face Shield", "Goggles"],
                 ["Single", "Double"],
                 ["Respirator Type", "SCBA", "Dust Mask"],
                 ["Fall protection", "Fall Restraint"],
                 ["Flame Resistant Clothing"],
                 ["Cotton", "Leather", "Impact Protection","Other","Chemical"],
                 ["Chemical Suit", "Apron"],
                 ["Foul Weather Gear"],
                 ["Other PPE"]]
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        riskView.delegate = self
        riskView.dataSource = self
        let textNib = UINib(nibName: "PermitCell", bundle: nil)
        riskView.register(textNib, forCellReuseIdentifier: "PermitCell")
        let selectNib = UINib(nibName: "SelectionCell", bundle: nil)
        riskView.register(selectNib, forCellReuseIdentifier: "SelectionCell")
        let switchNib = UINib(nibName: "FrequencyCell", bundle: nil)
        riskView.register(switchNib, forCellReuseIdentifier: "FrequencyCell")
        let headerNib = UINib(nibName: "CommonHeaderCell", bundle: nil)
        riskView.register(headerNib, forCellReuseIdentifier: "CommonHeaderCell")
        riskView.tableFooterView = UIView()
        //  riskView.separatorColor = UIColor.clear
        riskView.estimatedRowHeight = 44.0
        riskView.rowHeight = UITableView.automaticDimension
        riskView.isScrollEnabled = false
        //        riskView.isHidden = true
        
        riskView.layer.borderColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0).cgColor
        riskView.layer.borderWidth = 1.0
        riskView.layer.cornerRadius = 10.0
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    func setData(){
        riskView.reloadData()
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField.tag == 4{
            
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 1{
            if let str = textField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.sender?.present(alertController, animated: true, completion: nil)
                }
                else{
                    JSAObject.riskAssesment.respiratorType = textField.text!
                }
            }
            
        }
        else if textField.tag == 2{
            if let str = textField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.sender?.present(alertController, animated: true, completion: nil)
                }
                else{
                    JSAObject.riskAssesment.other = textField.text!
                }
            }
            
        }
        else if textField.tag == 3{
            if let str = textField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.sender?.present(alertController, animated: true, completion: nil)
                }
                else{
                   JSAObject.riskAssesment.chemical = textField.text!
                }
            }
            
        }
        else{
            if let str = textField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.sender?.present(alertController, animated: true, completion: nil)
                }
                else{
                    JSAObject.riskAssesment.otherPPE = textField.text!
                }
            }
            
            
        }
        textField.endEditing(true)
        
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        textField.endEditing(true)
        return true
    }
    
    
}
extension RiskAssessmentCell : UITableViewDataSource, UITableViewDelegate{
    
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
//    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
//        return 45
//    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if tableView.tag == 0{
            return 3
        }
        else if tableView.tag == 1{
            return 2
        }
        else if tableView.tag == 2{
            return 4
        }
        else if tableView.tag == 3{
            return 3
        }
        else if tableView.tag == 4{
            return 3
        }
        else if tableView.tag == 5{
            return 2
        }
        else if tableView.tag == 6{
            return 1
        }
        else if tableView.tag == 7{
            return 6
        }
        else if tableView.tag == 8{
            return 2
        }
        else if tableView.tag == 9{
            return 1
        }
        else{//} if tableView.tag == 10{
            return 1
        }
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if tableView.tag == 0{
            if indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: "CommonHeaderCell")! as! CommonHeaderCell
                cell.selectionStyle = .none
                cell.contentLabel.numberOfLines = 0
                cell.setData(text: "IF YES TO EITHER OF BELOW, ATTACH MOC OR RISK ASSESSMENT BEFORE PROCEEDING ")
                return cell
            }
            else {
                let cell = tableView.dequeueReusableCell(withIdentifier: "FrequencyCell")! as! FrequencyCell
                cell.selectionStyle = .none
                cell.titleLabel.numberOfLines = 0
                cell.permitSwitch.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
                if indexPath.row == 1{
                    cell.setLabel(data: "Must existing work practices be modified to perform this work?")
                    cell.permitSwitch.tag = 0
                    if JSAObject.riskAssesment.mustExistingWork == 0{
                        cell.setSwitchStatus(status: false)
                    }
                    else{
                        cell.setSwitchStatus(status: true)
                    }
                }
                else{
                    cell.setLabel(data: "After mitigation, are there any hazards that continue to present a potentially significant risk?")
                    cell.permitSwitch.tag = 1
                    if JSAObject.riskAssesment.afterMitigation == 0{
                        cell.setSwitchStatus(status: false)
                    }
                    else{
                        cell.setSwitchStatus(status: true)
                    }
                }
                return cell
            }
        }
        else if tableView.tag == 1{
            let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
            cell.selectionStyle = .none
            if indexPath.row == 0{
                cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.hardHat)
            }
            else{
                cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.safetyShoes)
            }
            return cell
            
        }
        else if tableView.tag == 2{
            if indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: "CommonHeaderCell")! as! CommonHeaderCell
                cell.selectionStyle = .none
                cell.setData(text: sectionArray[tableView.tag])
                return cell
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
                cell.selectionStyle = .none
                if indexPath.row == 1{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.safetyGlasses)
                }
                else if indexPath.row == 2{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.faceShield)
                }
                else{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.goggles)
                }
                return cell
            }
            
        }
        else if tableView.tag == 3{
            if indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: "CommonHeaderCell")! as! CommonHeaderCell
                cell.selectionStyle = .none
                cell.setData(text: sectionArray[tableView.tag])
                return cell
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
                cell.selectionStyle = .none
                if indexPath.row == 1{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.single)
                }
                else{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.double)
                }
                return cell
            }
            
        }
        else if tableView.tag == 4{
            if indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
                cell.selectionStyle = .none
                cell.valueTextField.delegate = self
                cell.valueTextField.tag = 1
                cell.setData(key: items[tableView.tag][indexPath.row], value: JSAObject.riskAssesment.respiratorType)
                return cell
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
                cell.selectionStyle = .none
                if indexPath.row == 1{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.SCBA)
                }
                else{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.dustMask)
                }
                return cell
            }
            
        }
        else if tableView.tag == 5{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
            cell.selectionStyle = .none
            if indexPath.row == 0{
                cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.fallProtection)
            }
            else{
                cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.fallRestraint)
            }
            return cell
            
            
        }
        else if tableView.tag == 6{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
            cell.selectionStyle = .none
            cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.flameResistantClothing)
            return cell
            
        }
        else if tableView.tag == 7{
            
            
            if indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: "CommonHeaderCell")! as! CommonHeaderCell
                cell.selectionStyle = .none
                cell.setData(text: sectionArray[tableView.tag])
                return cell
            }
            else if indexPath.row == 4 || indexPath.row == 5  {
                let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
                cell.selectionStyle = .none
                cell.valueTextField.delegate = self
                
                if indexPath.row == 4{
                    cell.valueTextField.tag = 2
                    cell.setData(key: items[tableView.tag][indexPath.row - 1], value: JSAObject.riskAssesment.other)
                }
                else{
                    cell.valueTextField.tag = 3
                    cell.setData(key: items[tableView.tag][indexPath.row - 1], value: JSAObject.riskAssesment.chemical)
                }
                return cell
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
                cell.selectionStyle = .none
                if indexPath.row == 1{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.cotton)
                }
                else if indexPath.row == 2{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.leather)
                }
                else if indexPath.row == 3{
                    cell.setData(labelValue: items[tableView.tag][indexPath.row - 1], selected: JSAObject.riskAssesment.impactProtection)
                }
                return cell
            }
            
        }
        else if tableView.tag == 8{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
            cell.selectionStyle = .none
            if indexPath.row == 0{
                cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.chemicalSuit)
            }
            else{
                cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.apron)
            }
            return cell
            
        }
        else if tableView.tag == 9{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "SelectionCell")! as! SelectionCell
            cell.selectionStyle = .none
            cell.setData(labelValue: items[tableView.tag][indexPath.row], selected: JSAObject.riskAssesment.foulWeatherGear)
            return cell
        }
        else{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
            cell.selectionStyle = .none
            cell.valueTextField.delegate = self
            cell.valueTextField.tag = 4
            cell.setData(key: items[tableView.tag][indexPath.row], value: JSAObject.riskAssesment.otherPPE)
            return cell
        }
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        //RiskAssesmentViewController().view.endEditing(true)
        
       if tableView.tag == 1{
            let cell : SelectionCell = tableView.cellForRow(at: indexPath) as! SelectionCell
            if indexPath.row == 0{
                if JSAObject.riskAssesment.hardHat == 0{
                    cell.selectImage.image = UIImage(named : "checked")
                    JSAObject.riskAssesment.hardHat = 1
                }
                else{
                    cell.selectImage.image = UIImage(named : "unchecked")
                    JSAObject.riskAssesment.hardHat = 0
                }
            }
            else{
                if JSAObject.riskAssesment.safetyShoes == 0{
                    cell.selectImage.image = UIImage(named : "checked")
                    JSAObject.riskAssesment.safetyShoes = 1
                }
                else{
                    cell.selectImage.image = UIImage(named : "unchecked")
                    JSAObject.riskAssesment.safetyShoes = 0
                }
            }
        }
        else if tableView.tag == 2{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if indexPath.row == 1{
                    if JSAObject.riskAssesment.safetyGlasses == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.safetyGlasses = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.safetyGlasses = 0
                    }
                }
                else if indexPath.row == 2{
                    if JSAObject.riskAssesment.faceShield == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.faceShield = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.faceShield = 0
                    }
                }
                else{
                    if JSAObject.riskAssesment.goggles == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.goggles = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.goggles = 0
                    }
                }
            }
        }
        else if tableView.tag == 3{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if indexPath.row == 1{
                    if JSAObject.riskAssesment.single == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.single = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.single = 0
                    }
                }
                else{
                    if JSAObject.riskAssesment.double == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.double = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.double = 0
                    }
                }
            }
        }
        else if tableView.tag == 4{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if indexPath.row == 1{
                    if JSAObject.riskAssesment.SCBA == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.SCBA = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.SCBA = 0
                    }
                }
                else{
                    if JSAObject.riskAssesment.dustMask == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.dustMask = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.dustMask = 0
                    }
                }
            }
        }
        else if tableView.tag == 5{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if indexPath.row == 0{
                    if JSAObject.riskAssesment.fallProtection == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.fallProtection = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.fallProtection = 0
                    }
                }
                else{
                    if JSAObject.riskAssesment.fallRestraint == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.fallRestraint = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.fallRestraint = 0
                    }
                }
            }
        }
        else if tableView.tag == 6{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if JSAObject.riskAssesment.flameResistantClothing == 0{
                    cell.selectImage.image = UIImage(named : "checked")
                    JSAObject.riskAssesment.flameResistantClothing = 1
                }
                else{
                    cell.selectImage.image = UIImage(named : "unchecked")
                    JSAObject.riskAssesment.flameResistantClothing = 0
                }
            }
        }
        else if tableView.tag == 7{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if indexPath.row == 1{
                    if JSAObject.riskAssesment.cotton == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.cotton = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.cotton = 0
                    }
                }
                else if indexPath.row == 2{
                    if JSAObject.riskAssesment.leather == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.leather = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.leather = 0
                    }
                }
                else if indexPath.row == 3{
                    if JSAObject.riskAssesment.impactProtection == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.impactProtection = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.impactProtection = 0
                    }
                }
            }
        }
        else if tableView.tag == 8{
        if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if indexPath.row == 0{
                    if JSAObject.riskAssesment.chemicalSuit == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.chemicalSuit = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.chemicalSuit = 0
                    }
                }
                else{
                    if JSAObject.riskAssesment.apron == 0{
                        cell.selectImage.image = UIImage(named : "checked")
                        JSAObject.riskAssesment.apron = 1
                    }
                    else{
                        cell.selectImage.image = UIImage(named : "unchecked")
                        JSAObject.riskAssesment.apron = 0
                    }
                }
            }
        }
        else if tableView.tag == 9{
            if let cell : SelectionCell = tableView.cellForRow(at: indexPath) as? SelectionCell{
                if JSAObject.riskAssesment.foulWeatherGear == 0{
                    cell.selectImage.image = UIImage(named : "checked")
                    JSAObject.riskAssesment.foulWeatherGear = 1
                }
                else{
                    cell.selectImage.image = UIImage(named : "unchecked")
                    JSAObject.riskAssesment.foulWeatherGear = 0
                }
            }
        }
        
        delegate?.dismissKeyboard()
        
    }
    
    @objc func onSwitch(_ sender: UISwitch)
    {
        
        var switchStatus : Bool?
        
        if sender.tag == 0{
            if JSAObject.riskAssesment.mustExistingWork == 0{
                switchStatus = true
                JSAObject.riskAssesment.mustExistingWork = 1
            }
            else{
                switchStatus = false
                JSAObject.riskAssesment.mustExistingWork = 0
            }
        }
        else{
            if JSAObject.riskAssesment.afterMitigation == 0{
                switchStatus = true
                JSAObject.riskAssesment.afterMitigation = 1
            }
            else{
                switchStatus = false
                JSAObject.riskAssesment.afterMitigation = 0
            }
        }
        sender.isOn = switchStatus!
        
        delegate?.dismissKeyboard()
        
    }
    
}
