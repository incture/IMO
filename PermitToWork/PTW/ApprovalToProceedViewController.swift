//
//  ApprovalToAuthority.swift
//  
//
//  Created by Parul Thakur77 on 02/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class ApprovalToAuthority: UIViewController,UITextFieldDelegate {
    
    @IBOutlet var approvalandAuthTable: UITableView!
    var selectStatus : Bool = false
    var approvalDate : Date = Date()
    var picDate : Date = Date()
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    let context = PTWCoreData.shared.managedObjectContext
    
    var sectionsLabel = ["","For PIC","For Permit Holder","Permit Distribution", "Confirmation"]
    var sectionsLabelCSE = ["","For PIC","For Superintendant","For Permit Holder","Permit Distribution", "Confirmation"]
    
    var tableContent = [["Is the work safe to perform as planned?"],["Pre-job walkthrough conducted by","Name","Date/Time"],["Name", "Date/Time","Energy isolations have been demonstrated to me and I accept this permit. All required controls will be implemented. I will notify the PIC when job is completed"],["Control Board / Point", "Worksite", "SIMOPS", "Others"],["I have reviewed this work and can confirm it will not conflict with other activities. Work can commence when the permit conditions are fulfilled. I have reviewed this work permit with area operator and the work can proceed as planned"]]
    
    var tableContentForCSE = [["Is the work safe to perform as planned?"],["Pre-job walkthrough conducted by","Name","Date/Time"],["Superintendant Name","Date/Time"],["Name", "Date/Time","Energy isolations have been demonstrated to me and I accept this permit. All required controls will be implemented. I will notify the PIC when job is completed"],["Control Board / Point", "Worksite", "SIMOPS", "Others"],["I have reviewed this work and can confirm it will not conflict with other activities. Work can commence when the permit conditions are fulfilled. I have reviewed this work permit with area operator and the work can proceed as planned"]]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //createNavBar()
        let permitNib = UINib(nibName: "PermitCell", bundle: nil)
        approvalandAuthTable.register(permitNib, forCellReuseIdentifier: "PermitCell")
        let workTypeNib = UINib(nibName: "WorkTypeCell", bundle: nil)
        approvalandAuthTable.register(workTypeNib, forCellReuseIdentifier: "WorkTypeCell")
        let frequencyTypeNib = UINib(nibName: "FrequencyCell", bundle: nil)
        approvalandAuthTable.register(frequencyTypeNib, forCellReuseIdentifier: "FrequencyCell")
        approvalandAuthTable.tableFooterView = UIView()
        if JSAObject.currentFlow == .HWP{
            CWObject = JSAObject.HWP
        }
        else if JSAObject.currentFlow == .CSEP{
            CWObject = JSAObject.CSEP
        }
        
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func createNavBar()
    {
        
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0)
        navigationItem.title = "Approval & Authority"
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    //MARK: - Loader
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        //       UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    @IBAction func onReviewPress(_ sender: UIButton) {
        
        if ConnectionCheck.isConnectedToNetwork(){
            if JSAObject.status.lowercased() == "approved"{
                let alertController = UIAlertController.init(title: "", message:"Do you want to Review this permit?" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "Agree", style: UIAlertAction.Style.default, handler: { (action) in
                    
                    // CAll Approved API
                    DispatchQueue.main.async {
                        self.setDataForApproval()
                    }
                })
                alertController.addAction(okAction)
                let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                    
                })
                alertController.addAction(cancelAction)
                self.present(alertController, animated: true, completion: nil)
            }
            else{
                let alertController = UIAlertController.init(title: "", message:"JSA should be reviewed a Permit is reviewed." , preferredStyle: UIAlertController.Style.alert)
                
                let cancelAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                    
                })
                alertController.addAction(cancelAction)
                self.present(alertController, animated: true, completion: nil)
            }
        }
        else{
            let alertController = UIAlertController.init(title: "Device should be online for Reviewing.", message:nil , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: { (action) in
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @IBAction func onAddPeoplePress(_ sender: UIButton) {
        
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    
    @IBAction func onCancelPress(_ sender: UIButton)
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onHomePress(_ sender: UIBarButtonItem) {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 0{
            CWObject.signOff.walkthrough = textField.text!
        }
        else if textField.tag == 1{
            CWObject.signOff.name = textField.text!
        }
        else if textField.tag == 2{
            CWObject.signOff.dateTime = textField.text!
        }
        else if textField.tag == 3{
            //CWObject.signOff.name = textField.text!
        }
        else if textField.tag == 4{
            CWObject.signOff.approvalDate = textField.text!
        }
        else if textField.tag == 5{
            CWObject.signOff.others = textField.text!
        }
    }
}

extension ApprovalToAuthority: UITableViewDataSource, UITableViewDelegate {
    
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return sectionsLabel.count
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableContent[section].count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell1 = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        let cell2 = tableView.dequeueReusableCell(withIdentifier: "WorkTypeCell")! as! WorkTypeCell
        let cell3 = tableView.dequeueReusableCell(withIdentifier: "FrequencyCell")! as! FrequencyCell
        
        if indexPath.section == 0
        {
            
            cell3.clipsToBounds = true
            cell3.setLabel(data : tableContent[indexPath.section][indexPath.row])
            cell3.permitSwitch.tag = indexPath.row
            if CWObject.signOff.isSafeContinue == 0{
                cell3.setSwitchStatus(status: false)
            }
            else{
                cell3.setSwitchStatus(status: true)
            }
            cell3.permitSwitch.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
            cell3.selectionStyle = .none
            return cell3
            
        }
        else if indexPath.section == 1
        {
            
            cell1.clipsToBounds = true
            cell1.keyLabel.numberOfLines = 0
            cell1.valueTextField.delegate = self
            cell1.valueTextField.tag = indexPath.row
            if indexPath.row == 0{
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: CWObject.signOff.walkthrough)
            }
            else if indexPath.row == 1{
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: CWObject.signOff.name)
            }
            else if indexPath.row == 2{
                cell1.valueTextField.datePickerMode = .dateAndTime
                cell1.valueTextField.isDatePicker = true
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: CWObject.signOff.dateTime)
            }
            cell1.selectionStyle = .none
            return cell1
            
        }
        else if indexPath.section == 2
        {
            
            if indexPath.row != 2{
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.delegate = self
                if indexPath.row == 0{
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: currentUser.name!)
                    cell1.valueTextField.tag = 3
                    cell1.isUserInteractionEnabled = false
                }
                else{
                    cell1.valueTextField.tag = 4
                    cell1.valueTextField.datePickerMode = .dateAndTime
                    cell1.valueTextField.isDatePicker = true
                    cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: CWObject.signOff.approvalDate)
                }
                cell1.selectionStyle = .none
                return cell1
            }
            else{
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton.tag = 4
                cell2.setSelectionStatus(status:  CWObject.signOff.energyIsolations)
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.selectionStyle = .none
                return cell2
            }
            
            
        }
        else if indexPath.section == 3{
            
            if indexPath.row != 3{
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton.tag = indexPath.row
                if indexPath.row == 0{
                    cell2.setSelectionStatus(status:  CWObject.signOff.controlBoard)
                }
                else if indexPath.row == 1{
                    cell2.setSelectionStatus(status:  CWObject.signOff.worksite)
                }
                else{
                    cell2.setSelectionStatus(status:  CWObject.signOff.SIMOPS)
                }
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.selectionStyle = .none
                return cell2
            }
            else{
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.delegate = self
                cell1.valueTextField.tag = 5
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: CWObject.signOff.others)
                cell1.selectionStyle = .none
                return cell1
            }
        }
        else{
            cell2.clipsToBounds = true
            cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
            cell2.checkButton.tag = 3
            cell2.setSelectionStatus(status: CWObject.signOff.ihaveReviewedThisWork)
            cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
            cell2.selectionStyle = .none
            return cell2
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 45))
        let label = UILabel(frame: CGRect(x: 16, y: 5, width: headerView.frame.size.width, height: 35))
        label.text = sectionsLabel[section]
        label.font = UIFont.boldSystemFont(ofSize: 16)
        
        if section == 1 || section == 2{
            headerView.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
            label.textColor = UIColor.white
        }
        else{
            headerView.backgroundColor = UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
            label.textColor = UIColor.black
        }
        
        headerView.addSubview(label)
        return headerView
    }
    
    public func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        
        if section == 0
        {
            return 0.0
        }
        else if section == 3{
            return 35.0
        }
        else{
            return 40.0
        }
        
    }
    
    //    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
    //
    //            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 150))
    //            let label = UILabel(frame: CGRect(x: 16, y: 4, width: headerView.frame.size.width, height: 65))
    //            label.numberOfLines = 0
    //            label.text = "NOTE ** \nI confirm that all personnel have been accounted for and the work has been restored toa safe and tidy working condition."
    //            label.font = UIFont.boldSystemFont(ofSize: 10)
    //            label.textColor = UIColor.red
    //            headerView.backgroundColor = UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
    //            headerView.addSubview(label)
    //            return headerView
    //    }
    
    
    @objc func buttonSelected(_ sender: AnyObject)
    {
        
        var buttonStatus : Bool?
        
        if JSAObject.currentFlow == .HWP{
            if sender.tag == 0{
                if CWObject.signOff.controlBoard == 0{
                    buttonStatus = true
                    CWObject.signOff.controlBoard = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.controlBoard = 0
                }
            }
            else if sender.tag == 1{
                if CWObject.signOff.worksite == 0{
                    buttonStatus = true
                    CWObject.signOff.worksite = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.worksite = 0
                }
            }
            else if sender.tag == 2{
                if CWObject.signOff.SIMOPS == 0{
                    buttonStatus = true
                    CWObject.signOff.SIMOPS = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.SIMOPS = 0
                }
            }
            else if sender.tag == 3 {
                if CWObject.signOff.ihaveReviewedThisWork == 0{
                    buttonStatus = true
                    CWObject.signOff.ihaveReviewedThisWork = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.ihaveReviewedThisWork = 0
                }
            }
            else if sender.tag == 4 {
                if CWObject.signOff.ihaveReviewedThisWork == 0{
                    buttonStatus = true
                    CWObject.signOff.ihaveReviewedThisWork = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.ihaveReviewedThisWork = 0
                }
            }
            else if sender.tag == 5 {
                if CWObject.signOff.energyIsolations == 0{
                    buttonStatus = true
                    CWObject.signOff.energyIsolations = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.energyIsolations = 0
                }
            }
        }
        else {
            if sender.tag == 0{
                if CWObject.signOff.controlBoard == 0{
                    buttonStatus = true
                    CWObject.signOff.controlBoard = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.controlBoard = 0
                }
            }
            else if sender.tag == 1{
                if CWObject.signOff.worksite == 0{
                    buttonStatus = true
                    CWObject.signOff.worksite = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.worksite = 0
                }
            }
            else if sender.tag == 2{
                if CWObject.signOff.SIMOPS == 0{
                    buttonStatus = true
                    CWObject.signOff.SIMOPS = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.SIMOPS = 0
                }
            }
            else if sender.tag == 3 {
                if CWObject.signOff.ihaveReviewedThisWork == 0{
                    buttonStatus = true
                    CWObject.signOff.ihaveReviewedThisWork = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.ihaveReviewedThisWork = 0
                }
            }
            else if sender.tag == 4 {
                if CWObject.signOff.ihaveReviewedThisWork == 0{
                    buttonStatus = true
                    CWObject.signOff.ihaveReviewedThisWork = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.ihaveReviewedThisWork = 0
                }
            }
            else if sender.tag == 5 {
                if CWObject.signOff.energyIsolations == 0{
                    buttonStatus = true
                    CWObject.signOff.energyIsolations = 1
                }
                else{
                    buttonStatus = false
                    CWObject.signOff.energyIsolations = 0
                }
            }
        }
        
        
        if buttonStatus! {
            sender.setImage(UIImage (named: "checked"), for: .normal)
        }else {
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
        }
        
        
        
    }
    
    @objc func onSwitch(_ sender: UISwitch)
    {
        
        var switchStatus : Bool?
        if JSAObject.currentFlow == .HWP{
            if sender.tag == 0{
                if CWObject.signOff.isSafeContinue == 0{
                    switchStatus = true
                    CWObject.signOff.isSafeContinue = 1
                }
                else{
                    switchStatus = false
                    CWObject.signOff.isSafeContinue = 0
                }
            }
        }
        else{
            if sender.tag == 0{
                if CWObject.signOff.isSafeContinue == 0{
                    switchStatus = true
                    CWObject.signOff.isSafeContinue = 1
                }
                else{
                    switchStatus = false
                    CWObject.signOff.isSafeContinue = 0
                }
            }
        }
        
        sender.isOn = switchStatus!
        
    }
    
}

extension ApprovalToAuthority{
    
    func setDataForApproval(){
        
        var finalDict = [String : Any]()
        if JSAObject.currentFlow == .HWP{
            
            finalDict = [
                "status" : "APPROVED",
                "ptwApprovalDto": [
                    "permitNumber" : CWObject.header.permitNo,
                    "preJobWalkthroughBy": CWObject.signOff.walkthrough,
                    "isWorkSafeToPerform": CWObject.signOff.isSafeContinue,
                    "controlBoardDistribution": CWObject.signOff.controlBoard,
                    "workSiteDistribution": CWObject.signOff.worksite,
                    "otherDistribution": CWObject.signOff.others,
                    "approvalDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                    "picName": CWObject.signOff.name,
                    "picDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                    "superitendentName": "",
                    "isHWP": 1,
                    "simopsDistribution": CWObject.signOff.SIMOPS,
                    "isCSE": 0,
                    "approvedBy": currentUser.name! as Any ,
                    "superitendentDate": "",
                    "isCWP": 0
                ] as [String : Any]
            ] as [String : Any]
            
        }
        else{
            
            finalDict = [
                "status" : "APPROVED",
                "ptwApprovalDto": [
                    "permitNumber" : CWObject.header.permitNo,
                    "preJobWalkthroughBy": CWObject.signOff.walkthrough,
                    "isWorkSafeToPerform": CWObject.signOff.isSafeContinue,
                    "controlBoardDistribution": CWObject.signOff.controlBoard,
                    "workSiteDistribution": CWObject.signOff.worksite,
                    "otherDistribution": CWObject.signOff.others,
                    "approvalDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                    "picName": CWObject.signOff.name,
                    "picDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                    "superitendentName": "",
                    "isHWP": 0,
                    "simopsDistribution": CWObject.signOff.SIMOPS,
                    "isCSE": 1,
                    "approvedBy": currentUser.name! as Any ,
                    "superitendentDate": "",
                    "isCWP": 0
                ] as [String : Any]
            ] as [String : Any]
        }
        
        if ConnectionCheck.isConnectedToNetwork(){
            DispatchQueue.main.async {
                self.loaderStart()
            }
            let header = ["Content-Type" : "Application/json"]

            let url  = IMOEndpoints.approvePermit
       //     let url =  "\(BaseUrl.apiURL)/com.iop.ptw/ApprovePermit.xsjs"
            let urlRequest = RequestURL.shared.urlRequest(for: url, method: "POST", body: finalDict)
            let task = ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
                guard let self = self else { return }
                guard error == nil else {
                    DispatchQueue.main.async {
                        self.loaderStop()
                        let message = error?.localizedDescription
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                    }
                    return
                }
                do{
                    guard let data = data else { return }
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    print("*****")
                    print(JSON)
                    print("*****")
                    if let jsonDict = JSON as? NSDictionary {
                        let msg = jsonDict.value(forKey: "data") as! String
                        let permitListService = PermitDetailModelService(context: self.context)
                        let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
                        let permitModelList = permitListService.get(withPredicate: searchPredicate)
                        var permitListObject = JSA()
                        for each in permitModelList{
                            if each.permitType == PermitType.HWP.rawValue{
                                each.getPermitData().HWP.header.status = "approved"
                                permitListObject = each.getPermitData()
                                permitListService.delete(id: each.objectID)
                                _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue)
                                
                            }
                            else if each.permitType == PermitType.CSEP.rawValue{
                                each.getPermitData().CSEP.header.status = "approved"
                                permitListObject = each.getPermitData()
                                permitListService.delete(id: each.objectID)
                                _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue)
                            }
                        }
                        permitListService.saveChanges()
                        DispatchQueue.main.async {
                            self.loaderStop()
                            let alertController = UIAlertController.init(title: "", message:msg , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                                JSAObject = JSA()
                            })
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                        
                    }
                }
                catch{
                    print(error.localizedDescription)
                }
            }
            task.resume()
            
        }
        else{
            let actionService = ActionModelService(context: self.context)
            _ = actionService.create(postData:finalDict as NSDictionary, permitNumber: JSAObject.permitNumber, actionType: ActionType.PermitApprove.rawValue)
            actionService.saveChanges()
            
            let permitListService = PermitModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let permitModelList = permitListService.get(withPredicate: searchPredicate)
            var permitListObject = PermitList()
            for each in permitModelList{
                if JSAObject.currentFlow == .HWP && each.permitType == PermitType.HWP.rawValue{
                    permitListObject = each.getPermitList()
                    permitListObject.status = "approved"
                    permitListService.delete(id: each.objectID)
                    _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue, facilityOrSite: currentLocation.facilityOrSite)
                }
                else if JSAObject.currentFlow == .CSEP && each.permitType == PermitType.CSEP.rawValue{
                    permitListObject = each.getPermitList()
                    permitListObject.status = "approved"
                    permitListService.delete(id: each.objectID)
                    _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue, facilityOrSite: currentLocation.facilityOrSite)
                }
                
                permitListService.saveChanges()
            }
            DispatchQueue.main.async {
                let alertController = UIAlertController.init(title: "", message:"Permit approved offline. Connect the device to internet to sync." , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                    self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                    JSAObject = JSA()
                })
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
            
        }
    }
}

extension ApprovalToAuthority: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
        if JSAObject.currentFlow == .HWP  {
            
            if textField.tag == 2 {
                CWObject.signOff.dateTime = date.toDateFormat(.long)
                picDate = date
                textField.text = date.toDateFormat(.dayMonthYearwithTime)
            }
            else if textField.tag == 4 {
                CWObject.signOff.dateTime = date.toDateFormat(.long)
                approvalDate = date
                textField.text = date.toDateFormat(.dayMonthYearwithTime)
            }
        }
        else{
            if textField.tag == 2 {
                CWObject.signOff.dateTime = date.toDateFormat(.long)
                picDate = date
                textField.text = date.toDateFormat(.dayMonthYearwithTime)
            }
            else if textField.tag == 4 {
                CWObject.signOff.dateTime = date.toDateFormat(.long)
                approvalDate = date
                textField.text = date.toDateFormat(.dayMonthYearwithTime)
            }
        }
        
    }
}

