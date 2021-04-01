//
//  WorkTypeController.swift
//  Murphy IOP
//
//  Created by Soumya Singh on 25/03/18.
//  Copyright © 2018 soumyaIncture Technologies. All rights reserved.
//

import UIKit

class WorkTypeController: UIViewController, UITextFieldDelegate {
    
    
    @IBOutlet var workTypeTable: UITableView!
    @IBOutlet weak var navigationTitle: UINavigationItem!
    var workTypes = ["Critical/Complex Lift","Crane/lifting device","Ground disturbance/excavation","Handling hazardous chemicals","Working at height","Painting/blasting","Working on pressurized systems","Erecting or dismantling scaffolding","Breaking containment of closed operating system","Working in close proximity to hazardous energy source (e.g. power lines, flare)","Removal of idle equipment for servicing and repair", "Higher risk electrical work","Description of Work *", "Other"]
    
    var hotWorkTypes = ["Cutting","Welding","Electrical powered equipment","Grinding","Abrasive Blasting","Other","Description of Work *"]
    
    var confinedWorkTypes = ["Tank","Vessel","Excavation/trench","Pit","Tower","Other","Reason for Confined Space Entry"]
    
    var docsRequired = ["Atmospheric Test Record","LOTO","Procedure","P&ID/Drawing","Temporary Defeat","Rescue Plan","SDS(MSDS)","Fire Watch Checklist","Lift Plan","SIMOP Deviation","Safe Work Practice", "Certificates", "Others"]
    
    var hotPermitdocsRequired = ["Energy Isolation Auth (LOTO)","SIMOPS Auth","Procedure","SDS (MSDS)","Temporary Defeat","Rescue Plan","Safe Work Practice","Fire Watch Checklist","Lift Plan","P&ID / Drawing","Other"]
    
    var confinedPermitdocsRequired = ["Energy Isolation Auth. (LOTO)","SIMOPS Auth.","Procedure","SDS (MSDS)","Temporary Defeat","Rescue Plan","Safe Work Practice","Fire Watch Checklist","Lift Plan","P&ID / Drawing","Other"]
    
    var isWorkType : Bool = true
    var isPermitReview = true
    
    var selectedWorkTypeCell: [Int: Bool]?
    
    override func viewDidLoad(){
        super.viewDidLoad()
        
        self.registerKeyBoardNotification()
        createNavBar()
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
        
        let nib = UINib(nibName: "WorkTypeCell", bundle: nil)
        workTypeTable.register(nib, forCellReuseIdentifier: "WorkTypeCell")
        let textNib = UINib(nibName: "PermitCell", bundle: nil)
        workTypeTable.register(textNib, forCellReuseIdentifier: "PermitCell")
        workTypeTable.tableFooterView = UIView()
        
        // Do any additional setup after loading the view.
    }
    
    deinit {
        self.unregisterKeyBoardNotification()
    }
    
    func createNavBar(){
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0) 
        if isWorkType{
            if JSAObject.currentFlow == .CSEP
            {
                navigationTitle.title = "Type of CSE"
            }
            else{
                navigationTitle.title = "Type of Work"
            }
        }
        else{
            if JSAObject.currentFlow == .CSEP
            {
                navigationTitle.title = "Documents"
            }
            else
            {
                navigationTitle.title = "Documents Required"
            }
        }
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onNextPress(_ sender: UIButton) {
        if isWorkType{
            
            var validation = self.validateFields()
            
            if isPermitReview
            {
                validation.isValid = true
            }
            
            if !validation.isValid {
                
                let alertController = UIAlertController(title: nil, message: validation.message, preferredStyle: .alert)
                let action = UIAlertAction(title: "OK", style: .default, handler: nil)
                alertController.addAction(action)
                self.present(alertController, animated: true, completion: nil)
                
            } else {
                let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "WorkTypeController") as! WorkTypeController
                vc.isWorkType = false
                self.navigationController?.pushViewController(vc, animated: true)
            }
        }
        else{
            
            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "AtmosphericTestingController") as! AtmosphericTestingController
            self.navigationController?.pushViewController(vc, animated: true)
            
        }
    }
    
    @IBAction func onPreviousPress(_ sender: UIButton) {
        dismissScreen()
    }
    
    
    @IBAction func onHomePress(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        //self.navigationController?.popToRootViewController(animated: true)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        
        if textField.tag == 1{
            if JSAObject.currentFlow == .CWP {
                JSAObject.CWP.workTypeCW.descriptionOfWork = textField.text!
            } else if JSAObject.currentFlow == .HWP {
                JSAObject.HWP.workTypeHW.otherText = textField.text!
            }
            else{
                JSAObject.CSEP.workTypeCSE.other = textField.text!
            }
            
        }
        else if textField.tag == 2{
            if JSAObject.currentFlow == .CWP {
                JSAObject.CWP.workTypeCW.otherText = textField.text!
            } else if JSAObject.currentFlow == .HWP {
                JSAObject.HWP.workTypeHW.descriptionOfWork = textField.text!
            }
            else{
                JSAObject.CSEP.workTypeCSE.reasonForCSE = textField.text!
            }
            
        }
        else if textField.tag == 3{
            if JSAObject.currentFlow == .CWP {
                JSAObject.CWP.docs.certificate = textField.text!
            } else if JSAObject.currentFlow == .HWP {
                JSAObject.HWP.docs.otherText = textField.text!
            }
            else{
                JSAObject.CSEP.docs.otherText = textField.text!
            }
            
        }
        else if textField.tag == 4{
            JSAObject.CWP.docs.otherText = textField.text!
        }
        //        else if textField.tag == 5 {
        //            JSAObject.HWP.workTypeHW.otherText = textField.text!
        //        } else if textField.tag == 6 {
        //            JSAObject.HWP.workTypeHW.descriptionOfWork = textField.text!
        //        }
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}

extension WorkTypeController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if isWorkType{
            
            if JSAObject.currentFlow == .CWP
            {
                return workTypes.count
            }
            else if JSAObject.currentFlow == .HWP
            {
                return hotWorkTypes.count
            }
            else
            {
                return confinedWorkTypes.count
            }
        }
        else
        {
            
            if JSAObject.currentFlow == .CWP
            {
                return docsRequired.count
            }
            else if JSAObject.currentFlow == .HWP
            {
                return hotPermitdocsRequired.count
            }
            else
            {
                return confinedPermitdocsRequired.count
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        var cell = WorkTypeCell()
        var cellDesc = PermitCell()
        
        
        
        // Configuring Cells
        if isWorkType{
            // cwp has 12,13 as text fields in work type
            if indexPath.row != 12 && indexPath.row != 13 {
                
                // hwp has 5,6 as text fields in work 
                if (JSAObject.currentFlow == .HWP && (indexPath.row == 5 || indexPath.row == 6)) || (JSAObject.currentFlow == .CSEP && (indexPath.row == 5 || indexPath.row == 6)){
                    
                    // For 5 and 6 in HW an CSEP
                    cellDesc = self.configurePermitType(indexPath: indexPath)
                } else {
                    // For CW and HW worktypes with tick
                    cell = self.configureWorkType(indexPath: indexPath)
                }
            }
            else{
                // for text cellls in CW
                cellDesc = self.configurePermitType(indexPath: indexPath)
            }
        }
        else{//Docs setting
            if JSAObject.currentFlow == .CWP{
                if indexPath.row != 11 && indexPath.row != 12{
                    cell = self.configureWorkType(indexPath: indexPath)
                }
                else{
                    cellDesc = self.configurePermitType(indexPath: indexPath)
                }
            }
            else if JSAObject.currentFlow == .HWP{
                if indexPath.row != 10{
                    cell = self.configureWorkType(indexPath: indexPath)
                }
                else{
                    cellDesc = self.configurePermitType(indexPath: indexPath)
                }
            }
            else if JSAObject.currentFlow == .CSEP{
                if indexPath.row != 10{
                    cell = self.configureWorkType(indexPath: indexPath)
                }
                else{
                    cellDesc = self.configurePermitType(indexPath: indexPath)
                }
            }
        }
        
        self.handleCellInteraction(cell: cell, cellDesc: cellDesc)
        /// Setting Data
        if isWorkType{
            
            
            var displayLabels = workTypes
            if JSAObject.currentFlow == .HWP {
                displayLabels = hotWorkTypes
            }
            else if JSAObject.currentFlow == .CSEP {
                displayLabels = confinedWorkTypes
            }
            
            let  status = self.getWorkTypeData(indexPath: indexPath)
            if indexPath.row < 12 {
                
                if JSAObject.currentFlow == .HWP  && (indexPath.row == 5 || indexPath.row == 6) {
                    var value = JSAObject.HWP.workTypeHW.otherText
                    cellDesc.valueTextField.tag = 1
                    if indexPath.row == 6 {
                        value =  JSAObject.HWP.workTypeHW.descriptionOfWork
                        cellDesc.valueTextField.tag = 2
                    }
                    cellDesc.setData(key: displayLabels[indexPath.row], value: value)
                    if currentUser.isReadOnly == true{
                        cellDesc.isUserInteractionEnabled = false
                    }
                    return cellDesc
                }
                else if JSAObject.currentFlow == .CSEP  && (indexPath.row == 5 || indexPath.row == 6) {
                    var value = JSAObject.CSEP.workTypeCSE.other
                    cellDesc.valueTextField.tag = 1
                    if indexPath.row == 6 {
                        value =  JSAObject.CSEP.workTypeCSE.reasonForCSE
                        cellDesc.valueTextField.tag = 2
                    }
                    cellDesc.setData(key: displayLabels[indexPath.row], value: value)
                    if currentUser.isReadOnly == true{
                        cellDesc.isUserInteractionEnabled = false
                    }
                    return cellDesc
                }
                else {
                    cell.setData(labelValue: displayLabels[indexPath.row])
                    cell.setSelectionStatus(status: status)
                }
                
            } else if indexPath.row == 12{
                cellDesc.setData(key: displayLabels[indexPath.row], value: JSAObject.CWP.workTypeCW.descriptionOfWork)
                cellDesc.valueTextField.tag = 1
                if currentUser.isReadOnly == true{
                    cellDesc.isUserInteractionEnabled = false
                }
                return cellDesc
            }
            else if indexPath.row == 13{
                cellDesc.setData(key: displayLabels[indexPath.row], value: JSAObject.CWP.workTypeCW.otherText)
                cellDesc.valueTextField.tag = 2
                if currentUser.isReadOnly == true{
                    cellDesc.isUserInteractionEnabled = false
                }
                return cellDesc
            }
                
            else if JSAObject.currentFlow == .HWP
            {
                //Hot Worktype here
            }
            else
            {
                // confined workType here
            }
        }
        else{
            
            
            var docs = JSAObject.CWP.docs
            if JSAObject.currentFlow == .CWP{
                if indexPath.row <= 10 {
                    self.setDocumentData(to: cell, indexPath: indexPath, docs: docs)
                    
                } else if indexPath.row == 11{
                    cellDesc.setData(key: docsRequired[indexPath.row], value: docs.certificate)
                    cellDesc.valueTextField.tag = 3
                    if currentUser.isReadOnly == true{
                        cellDesc.isUserInteractionEnabled = false
                    }
                    return cellDesc
                }
                else if indexPath.row == 12{
                    cellDesc.setData(key: docsRequired[indexPath.row], value: docs.otherText)
                    cellDesc.valueTextField.tag = 4
                    if currentUser.isReadOnly == true{
                        cellDesc.isUserInteractionEnabled = false
                    }
                    return cellDesc
                }
            }
            else if JSAObject.currentFlow == .HWP
            {
                docs = JSAObject.HWP.docs
                if indexPath.row < 10 {
                    self.setDocumentData(to: cell, indexPath: indexPath, docs: docs)
                    
                } else if indexPath.row == 10{
                    cellDesc.setData(key: hotPermitdocsRequired[indexPath.row], value: docs.otherText)
                    cellDesc.valueTextField.tag = 3
                    if currentUser.isReadOnly == true{
                        cellDesc.isUserInteractionEnabled = false
                    }
                    return cellDesc
                }
                
            }
            else
            {
                docs = JSAObject.CSEP.docs
                if indexPath.row < 10 {
                    self.setDocumentData(to: cell, indexPath: indexPath, docs: docs)
                    
                } else if indexPath.row == 10{
                    cellDesc.setData(key: hotPermitdocsRequired[indexPath.row], value: docs.otherText)
                    cellDesc.valueTextField.tag = 3
                    if currentUser.isReadOnly == true{
                        cellDesc.isUserInteractionEnabled = false
                    }
                    return cellDesc
                }
            }
            
        }
        cell.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
        cell.selectionStyle = .none
        if currentUser.isReadOnly == true{
            cell.isUserInteractionEnabled = false
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 55
    }
    
    
    private func handleCellInteraction(cell: WorkTypeCell, cellDesc: PermitCell) {
        
        if isPermitReview || self.getJSAStatus() == "approved"
        {
            cell.isUserInteractionEnabled = false
            cellDesc.isUserInteractionEnabled = false
        }
        else
        {
            cell.isUserInteractionEnabled = true
            cellDesc.isUserInteractionEnabled = true
        }
        
    }
    
    private func configureWorkType(indexPath: IndexPath) -> WorkTypeCell {
        
        let cell = self.workTypeTable.dequeueReusableCell(withIdentifier: "WorkTypeCell")! as! WorkTypeCell
        cell.checkButton.tag = indexPath.row
        cell.delegate = self
        cell.clipsToBounds = true
        return cell
    }
    
    private func configurePermitType(indexPath: IndexPath) -> PermitCell {
        
        let cellDesc = self.workTypeTable.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        cellDesc.clipsToBounds = true
        cellDesc.valueTextField.tag = indexPath.row
        cellDesc.valueTextField.delegate = self
        return cellDesc
    }
}


// MARK:- Data Validation methods

extension WorkTypeController  {
    
    func validateFields() -> (isValid: Bool, message: String) {
        
        guard self.selectedWorkTypeCell!.filter({$0.value == true }).count > 0 else {
            return (false, "Please choose atleast one type of work.")
        }
        
        var descriptionOfWork = JSAObject.CWP.workTypeCW.descriptionOfWork
        if JSAObject.currentFlow == .HWP {
            descriptionOfWork = JSAObject.HWP.workTypeHW.descriptionOfWork
        }
        else if JSAObject.currentFlow == .CSEP {
            descriptionOfWork = "nk"
        }
        guard !descriptionOfWork.isEmpty else {
            return (false, "Please enter description of work")
        }
        
        
        return (true, "")
        
        
    }
}

extension WorkTypeController: WorkTypeCellDelegate {}

// MARK:- Keyboard Handle methods

extension WorkTypeController {
    
    
    func registerKeyBoardNotification() {
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
    }
    
    func unregisterKeyBoardNotification() {
        
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func keyboardWillShow(_ notification:Notification) {
        
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            //            let keyboardFrame = self.workTypeTable.convert(keyboardSize, from: nil)
            self.workTypeTable.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: keyboardSize.height, right: 0)
        }
    }
    @objc func keyboardWillHide(_ notification:Notification) {
        
        if let _ = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            self.workTypeTable.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        }
    }
}


// MARK:- Cell Data set and get methods

extension WorkTypeController {
    
    func setDocumentData(to cell: WorkTypeCell, indexPath: IndexPath, docs: DocReqd)  {
        
        if JSAObject.currentFlow == .CWP{
            
            if indexPath.row == 0{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.atmosphericTest)
            }
            else if indexPath.row == 1{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.Loto)
            }
            else if indexPath.row == 2{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.Procedure)
            }
            else if indexPath.row == 3{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.PnID)
            }
            else if indexPath.row == 4{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.tempDefeat)
            }
            else if indexPath.row == 5{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.rescuePlan)
            }
            else if indexPath.row == 6{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.sds)
            }
            else if indexPath.row == 7{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.fireWatch)
            }
            else if indexPath.row == 8{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.liftPlan)
            }
            else if indexPath.row == 9{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.simop)
            }
            else if indexPath.row == 10{
                cell.setData(labelValue: docsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.safeWorkPractice)
            }
        }
        else if JSAObject.currentFlow == .HWP{
            if indexPath.row == 0{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.Loto)
            }
            else if indexPath.row == 1{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.simop)
            }
            else if indexPath.row == 2{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.Procedure)
            }
            else if indexPath.row == 3{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.sds)
            }
            else if indexPath.row == 4{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.tempDefeat)
            }
            else if indexPath.row == 5{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.rescuePlan)
            }
            else if indexPath.row == 6{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.safeWorkPractice)
            }
            else if indexPath.row == 7{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.fireWatch)
            }
            else if indexPath.row == 8{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.liftPlan)
            }
            else if indexPath.row == 9{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.PnID)
            }
        }
        else {
            if indexPath.row == 0{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.Loto)
            }
            else if indexPath.row == 1{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.simop)
            }
            else if indexPath.row == 2{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.Procedure)
            }
            else if indexPath.row == 3{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.sds)
            }
            else if indexPath.row == 4{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.tempDefeat)
            }
            else if indexPath.row == 5{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.rescuePlan)
            }
            else if indexPath.row == 6{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.safeWorkPractice)
            }
            else if indexPath.row == 7{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.fireWatch)
            }
            else if indexPath.row == 8{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.liftPlan)
            }
            else if indexPath.row == 9{
                cell.setData(labelValue: hotPermitdocsRequired[indexPath.row])
                cell.setSelectionStatus(status: docs.PnID)
            }
        }
        
    }
    
    
    func getWorkTypeData(indexPath: IndexPath) -> Int {
        
        if JSAObject.currentFlow == .CWP {
            return self.getColdWorkData(indexPath: indexPath)
        } else if JSAObject.currentFlow == .HWP {
            return self.getHotWorkData(indexPath: indexPath)
        } else {
            return self.getCSEData(indexPath: indexPath)
        }
    }
    
    
    private func getCSEData(indexPath: IndexPath) -> Int {
        
        if indexPath.row == 0{
            return JSAObject.CSEP.workTypeCSE.tank
        }
        else if indexPath.row == 1{
            return JSAObject.CSEP.workTypeCSE.vessel
        }
        else if indexPath.row == 2{
            return JSAObject.CSEP.workTypeCSE.excavation
            
        }
        else if indexPath.row == 3{
            return JSAObject.CSEP.workTypeCSE.pit
        }
        else if indexPath.row == 4{
            return JSAObject.CSEP.workTypeCSE.tower
        }else {
            return 0
        }
    }
    
    private func getHotWorkData(indexPath: IndexPath) -> Int {
        
        if indexPath.row == 0{
            return JSAObject.HWP.workTypeHW.cutting
        }
        else if indexPath.row == 1{
            return JSAObject.HWP.workTypeHW.welding
        }
        else if indexPath.row == 2{
            return JSAObject.HWP.workTypeHW.electricalPoweredEquipment
            
        }
        else if indexPath.row == 3{
            return JSAObject.HWP.workTypeHW.grinding
        }
        else if indexPath.row == 4{
            return JSAObject.HWP.workTypeHW.abrasiveBlasting
        }
        else if indexPath.row == 5{
            return JSAObject.HWP.workTypeHW.otherCheck
            
        } else {
            return 0
        }
    }
    
    private func getColdWorkData(indexPath: IndexPath) -> Int {
        
        
        if indexPath.row == 0{
            return JSAObject.CWP.workTypeCW.criticalLift
        }
        else if indexPath.row == 1{
            return JSAObject.CWP.workTypeCW.Crane
        }
        else if indexPath.row == 2{
            return JSAObject.CWP.workTypeCW.groundDist
        }
        else if indexPath.row == 3{
            return JSAObject.CWP.workTypeCW.handlingChem
        }
        else if indexPath.row == 4{
            return JSAObject.CWP.workTypeCW.workAtHeight
        }
        else if indexPath.row == 5{
            return JSAObject.CWP.workTypeCW.paintBlast
        }
        else if indexPath.row == 6 {
            return JSAObject.CWP.workTypeCW.workOnPressurizedSystems
        }
        else if indexPath.row == 7{
            return JSAObject.CWP.workTypeCW.erectingScaffolding
        }
        else if indexPath.row == 8{
            return JSAObject.CWP.workTypeCW.breakingContainment
        }
        else if indexPath.row == 9{
            return JSAObject.CWP.workTypeCW.closeProximity
        }
        else if indexPath.row == 10{
            return JSAObject.CWP.workTypeCW.removalOfIdleEquip
        }
        else if indexPath.row == 11{
            return JSAObject.CWP.workTypeCW.higherRisk
        }
        else {
            return 0
        }
    }
    
}



// MARK:- BUtton selection method

extension WorkTypeController {
    
    
    func getJSAStatus() -> String {
        
        
        switch JSAObject.currentFlow {
        case .CWP:
            return JSAObject.CWP.header.status.lowercased()
        case .HWP:
            return JSAObject.HWP.header.status.lowercased()
        case .CSEP:
            return JSAObject.CSEP.header.status.lowercased()
        default:
            return ""
        }
        
    }
    
    @objc func buttonSelected(_ sender: UIButton) {
        
        var buttonStatus : Bool = false
        if JSAObject.currentFlow == .CWP {
            buttonStatus = self.handleColdWorkButtonSelected(sender)
        } else if JSAObject.currentFlow == .HWP {
            buttonStatus = self.handleHotWorkButtonSelected(sender)
        }
        else if JSAObject.currentFlow == .CSEP {
            buttonStatus = self.handleCSEButtonSelected(sender)
        }
        
        self.selectedWorkTypeCell?[sender.tag] = buttonStatus
        if buttonStatus {
            sender.setImage(UIImage (named: "checked"), for: .normal)
        }else {
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
        }
        
    }
    
    @objc func handleColdWorkButtonSelected(_ sender: UIButton) -> Bool {
        
        if isWorkType {
            if sender.tag == 0{
                JSAObject.CWP.workTypeCW.criticalLift = JSAObject.CWP.workTypeCW.criticalLift.negatedValue
                return JSAObject.CWP.workTypeCW.criticalLift.boolValue
            }
            else if sender.tag == 1{
                JSAObject.CWP.workTypeCW.Crane = JSAObject.CWP.workTypeCW.Crane.negatedValue
                return JSAObject.CWP.workTypeCW.Crane.boolValue
            }
            else if sender.tag == 2{
                JSAObject.CWP.workTypeCW.groundDist = JSAObject.CWP.workTypeCW.groundDist.negatedValue
                return JSAObject.CWP.workTypeCW.groundDist.boolValue
                
            }
            else if sender.tag == 3{
                JSAObject.CWP.workTypeCW.handlingChem = JSAObject.CWP.workTypeCW.handlingChem.negatedValue
                return JSAObject.CWP.workTypeCW.handlingChem.boolValue
            }
            else if sender.tag == 4{
                JSAObject.CWP.workTypeCW.workAtHeight = JSAObject.CWP.workTypeCW.workAtHeight.negatedValue
                return JSAObject.CWP.workTypeCW.workAtHeight.boolValue
            }
            else if sender.tag == 5{
                JSAObject.CWP.workTypeCW.paintBlast = JSAObject.CWP.workTypeCW.paintBlast.negatedValue
                return JSAObject.CWP.workTypeCW.paintBlast.boolValue
                
            }
            else if sender.tag == 6{
                JSAObject.CWP.workTypeCW.workOnPressurizedSystems = JSAObject.CWP.workTypeCW.workOnPressurizedSystems.negatedValue
                return JSAObject.CWP.workTypeCW.workOnPressurizedSystems.boolValue
            }
            else if sender.tag == 7{
                JSAObject.CWP.workTypeCW.erectingScaffolding = JSAObject.CWP.workTypeCW.erectingScaffolding.negatedValue
                return JSAObject.CWP.workTypeCW.erectingScaffolding.boolValue
            }
            else if sender.tag == 8{
                JSAObject.CWP.workTypeCW.breakingContainment = JSAObject.CWP.workTypeCW.breakingContainment.negatedValue
                return JSAObject.CWP.workTypeCW.breakingContainment.boolValue
            }
            else if sender.tag == 9{
                JSAObject.CWP.workTypeCW.closeProximity = JSAObject.CWP.workTypeCW.closeProximity.negatedValue
                return JSAObject.CWP.workTypeCW.closeProximity.boolValue
            }
            else if sender.tag == 10{
                JSAObject.CWP.workTypeCW.removalOfIdleEquip = JSAObject.CWP.workTypeCW.removalOfIdleEquip.negatedValue
                return JSAObject.CWP.workTypeCW.removalOfIdleEquip.boolValue
            }
            else if sender.tag == 11{
                JSAObject.CWP.workTypeCW.higherRisk = JSAObject.CWP.workTypeCW.higherRisk.negatedValue
                return JSAObject.CWP.workTypeCW.higherRisk.boolValue
            } else {
                return false
            }
            
        } else {
            return self.handleDocumentSelected(sender: sender, docs: JSAObject.CWP.docs)
        }
    }
    
    
    @objc func handleHotWorkButtonSelected(_ sender: UIButton) -> Bool {
        
        if isWorkType {
            if sender.tag == 0{
                JSAObject.HWP.workTypeHW.cutting = JSAObject.HWP.workTypeHW.cutting.negatedValue
                return JSAObject.HWP.workTypeHW.cutting.boolValue
            }
            else if sender.tag == 1{
                JSAObject.HWP.workTypeHW.welding = JSAObject.HWP.workTypeHW.welding.negatedValue
                return JSAObject.HWP.workTypeHW.welding.boolValue
            }
            else if sender.tag == 2{
                JSAObject.HWP.workTypeHW.electricalPoweredEquipment = JSAObject.HWP.workTypeHW.electricalPoweredEquipment.negatedValue
                return JSAObject.HWP.workTypeHW.electricalPoweredEquipment.boolValue
                
            }
            else if sender.tag == 3{
                JSAObject.HWP.workTypeHW.grinding = JSAObject.HWP.workTypeHW.grinding.negatedValue
                return JSAObject.HWP.workTypeHW.grinding.boolValue
            }
            else if sender.tag == 4{
                JSAObject.HWP.workTypeHW.abrasiveBlasting = JSAObject.HWP.workTypeHW.abrasiveBlasting.negatedValue
                return JSAObject.HWP.workTypeHW.abrasiveBlasting.boolValue
            }
            else if sender.tag == 5{
                JSAObject.HWP.workTypeHW.otherCheck = JSAObject.HWP.workTypeHW.otherCheck.negatedValue
                return JSAObject.HWP.workTypeHW.otherCheck.boolValue
                
            } else {
                return false
            }
            
        } else {
            return self.handleDocumentSelected(sender: sender, docs: JSAObject.HWP.docs)
        }
    }
    
    
    @objc func handleCSEButtonSelected(_ sender: UIButton) -> Bool {
        
        if isWorkType {
            if sender.tag == 0{
                JSAObject.CSEP.workTypeCSE.tank = JSAObject.CSEP.workTypeCSE.tank.negatedValue
                return JSAObject.CSEP.workTypeCSE.tank.boolValue
            }
            else if sender.tag == 1{
                JSAObject.CSEP.workTypeCSE.vessel = JSAObject.CSEP.workTypeCSE.vessel.negatedValue
                return JSAObject.CSEP.workTypeCSE.vessel.boolValue
            }
            else if sender.tag == 2{
                JSAObject.CSEP.workTypeCSE.excavation = JSAObject.CSEP.workTypeCSE.excavation.negatedValue
                return JSAObject.CSEP.workTypeCSE.excavation.boolValue
                
            }
            else if sender.tag == 3{
                JSAObject.CSEP.workTypeCSE.pit = JSAObject.CSEP.workTypeCSE.pit.negatedValue
                return JSAObject.CSEP.workTypeCSE.pit.boolValue
            }
            else if sender.tag == 4{
                JSAObject.CSEP.workTypeCSE.tower = JSAObject.CSEP.workTypeCSE.tower.negatedValue
                return JSAObject.CSEP.workTypeCSE.tower.boolValue
            } else {
                return false
            }
            
        } else {
            return self.handleDocumentSelected(sender: sender, docs: JSAObject.CSEP.docs)
        }
    }
    
    private func handleDocumentSelected(sender: UIButton, docs: DocReqd) -> Bool {
        
        
        if JSAObject.currentFlow == .CWP{
            
            if sender.tag == 0{
                docs.atmosphericTest = docs.atmosphericTest.negatedValue
                return docs.atmosphericTest.boolValue
            }
            else if sender.tag == 1{
                docs.Loto = docs.Loto.negatedValue
                return docs.Loto.boolValue
            }
            else if sender.tag == 2{
                docs.Procedure = docs.Procedure.negatedValue
                return docs.Procedure.boolValue
            }
            else if sender.tag == 3{
                docs.PnID = docs.PnID.negatedValue
                return docs.PnID.boolValue
            }
            else if sender.tag == 4{
                docs.tempDefeat = docs.tempDefeat.negatedValue
                return docs.tempDefeat.boolValue
            }
            else if sender.tag == 5{
                docs.rescuePlan =  docs.rescuePlan.negatedValue
                return docs.rescuePlan.boolValue
            }
            else if sender.tag == 6{
                docs.sds = docs.sds.negatedValue
                return docs.sds.boolValue
            }
            else if sender.tag == 7{
                docs.fireWatch = docs.fireWatch.negatedValue
                return docs.fireWatch.boolValue
            }
            else if sender.tag == 8 {
                docs.liftPlan = docs.liftPlan.negatedValue
                return docs.liftPlan.boolValue
            }
            else if sender.tag == 9{
                docs.simop = docs.simop.negatedValue
                return docs.simop.boolValue
            }
            else if sender.tag == 10{
                docs.safeWorkPractice = docs.safeWorkPractice.negatedValue
                return docs.safeWorkPractice.boolValue
            }else {
                return false
            }
        }
        else {
            
            if sender.tag == 0{
                docs.Loto = docs.Loto.negatedValue
                return docs.Loto.boolValue
            }
            else if sender.tag == 1{
                docs.simop = docs.simop.negatedValue
                return docs.simop.boolValue
            }
            else if sender.tag == 2{
                docs.Procedure = docs.Procedure.negatedValue
                return docs.Procedure.boolValue
            }
            else if sender.tag == 3{
                docs.sds = docs.sds.negatedValue
                return docs.sds.boolValue
            }
            else if sender.tag == 4{
                docs.tempDefeat = docs.tempDefeat.negatedValue
                return docs.tempDefeat.boolValue
            }
            else if sender.tag == 5{
                docs.rescuePlan =  docs.rescuePlan.negatedValue
                return docs.rescuePlan.boolValue
            }
            else if sender.tag == 6{
                docs.safeWorkPractice = docs.safeWorkPractice.negatedValue
                return docs.safeWorkPractice.boolValue
            }
            else if sender.tag == 7{
                docs.fireWatch = docs.fireWatch.negatedValue
                return docs.fireWatch.boolValue
            }
            else if sender.tag == 8 {
                docs.liftPlan = docs.liftPlan.negatedValue
                return docs.liftPlan.boolValue
            }
            else if sender.tag == 9{
                docs.PnID = docs.PnID.negatedValue
                return docs.PnID.boolValue
            }else {
                return false
            }
        }
        
    }
    
    
}
