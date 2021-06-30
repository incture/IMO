//
//  PicSignOffViewController.swift
// 
//
//  Created by Parul Thakur77 on 11/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class PicSignOffViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet var picSigntableView: UITableView!
    var selectStatus : Bool = false
    let context = PTWCoreData.shared.managedObjectContext
    var sectionsLabel = ["","For PIC", "Permit Distribution", "Confirmation"]
    var dateNtime : Date?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    //    var switchArr = ["Work associated with this permit has been completed"]
    //    var closeOut = ["Work associated with this permit has been completed","Permit Holder name","Work Status Comments"]
    
    
   var tableContent = [["Is the work safe to perform as planned?"],["Pre-job walkthrough conducted by","Name","Date/Time"],["Control Board / Point", "Worksite", "SIMOPS", "Others"],["I have reviewed this work and can confirm it will not conflict with other activities. Work can commence when the permit conditions are fulfilled. I have reviewed this work permit with area operator and the work can proceed as planned"]]
    var isPermitReview = true
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        let permitNib = UINib(nibName: "PermitCell", bundle: nil)
        picSigntableView.register(permitNib, forCellReuseIdentifier: "PermitCell")
        let workTypeNib = UINib(nibName: "WorkTypeCell", bundle: nil)
        picSigntableView.register(workTypeNib, forCellReuseIdentifier: "WorkTypeCell")
        let frequencyNib = UINib(nibName: "FrequencyCell", bundle: nil)
        picSigntableView.register(frequencyNib, forCellReuseIdentifier: "FrequencyCell")
        picSigntableView.tableFooterView = UIView()
        if JSAObject.currentFlow == .CWP{
            CWObject = JSAObject.CWP
        }
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                isPermitReview = true
            }
            else if val == "false"
            {
                isPermitReview = false
            }
        }
        else
        {
            isPermitReview = false
        }
        
        
        // Do any additional setup after loading the view.
    }
    
    func createNavBar(){
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0)
        navigationItem.title = "Permit Close Out"
        
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onPreviousPress(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func onClickNext(_ sender: UIButton) {
        
        if isPermitReview
        {
            // validation.isValid = true
            let alert = UIAlertController(title: nil, message: "I acknowledge I have reviewed the Permit, I understand my roles and responsibilities and I will comply with the instructions for this task", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                
                if JSAObject.currentFlow == .CWP
                {
                    JSAObject.addNewpeople.hasSignedCWP = 1
                    JSAObject.addNewpeople.isCheckedCWP = 0
                    if JSAObject.addNewpeople.isCheckedHWP == 1 && JSAObject.addNewpeople.hasSignedHWP != 1
                    {
                        JSAObject.currentFlow = .HWP
                        pushToPermitController = true
                        guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                            return
                        }
                        print(viewControllers)
                        self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                    }
                    else if JSAObject.addNewpeople.isCheckedCSE == 1  && JSAObject.addNewpeople.hasSignedCSE != 1
                    {
                        JSAObject.currentFlow = .CSEP
                        pushToPermitController = true
                        guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                            return
                        }
                        print(viewControllers)
                        self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                    }
                    else
                    {
                        if JSAObject.addPeopleIndex != nil{
                            JSAObject.peopleList.remove(at: JSAObject.addPeopleIndex!)
                            JSAObject.addPeopleIndex = nil
                        }
                        JSAObject.peopleList.append(JSAObject.addNewpeople)
                        UserDefaults.standard.set("false", forKey: "JSAPreview")
                        UserDefaults.standard.synchronize()
                        pushToPermitController = false
                        guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                            return
                        }
                        print(viewControllers)
                        self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                    }
                }
            }))
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
            
        }
        else
        {
            if JSAObject.currentFlow == .CWP
            {
                JSAObject.hasCWP = 1
                if JSAObject.isHWP == 1
                {
                    JSAObject.currentFlow = .HWP
                    let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
                    self.navigationController?.pushViewController(vc, animated: true)
                    
                }
                else if JSAObject.isCSP == 1
                {
                    JSAObject.currentFlow = .CSEP
                    let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
                    self.navigationController?.pushViewController(vc, animated: true)
                }
                else
                {
                    if JSAObject.CWP.header.status == ""
                    {
                        let vc = UIStoryboard(name: "CreateJSA", bundle: Bundle.main).instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    else if JSAObject.CWP.header.status.lowercased() == "approved"
                    {
                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitCloseOutController") as! PermitCloseOutController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                }
                
            }
        }
    }
    
    @IBAction func onReviewPress(_ sender: Any) {
        
        if ConnectionCheck.isConnectedToNetwork(){
        if JSAObject.status.lowercased() == "approved"{
            let alertController = UIAlertController.init(title: "", message:"Do you want to Review this permit?" , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "Agree", style: UIAlertAction.Style.default, handler: { (action) in
                    
                    // CAll Approved API
                    self.setDataForApproval()
                    
                })
                alertController.addAction(okAction)
            let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                    
                })
                alertController.addAction(cancelAction)
                self.present(alertController, animated: true, completion: nil)
            }
            else{
            let alertController = UIAlertController.init(title: "", message:"JSA should be reviewed before a Permit is reviewed." , preferredStyle: UIAlertController.Style.alert)
            
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
    
    
    @IBAction func onAddPeoplePress(_ sender: Any) {
        
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
        
    }
    
    @IBAction func onHomePress(_ sender: Any)
    {
    self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
       // self.navigationController?.popToRootViewController(animated: true)
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
            
        }
        else if textField.tag == 3{
            CWObject.signOff.others = textField.text!
        }
    }
    
}
extension PicSignOffViewController: UITableViewDataSource, UITableViewDelegate {
    
    
    
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
            cell3.permitSwitch.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
            if CWObject.signOff.isSafeContinue == 0{
                cell3.permitSwitch.isOn = false
            }
            else{
                 cell3.permitSwitch.isOn = true
            }
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
        else if indexPath.section == 2{
            
            if indexPath.row != 3{
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton.tag = indexPath.row
                if indexPath.row == 0{
                    cell2.setSelectionStatus(status: CWObject.signOff.controlBoard)
                }
                else if indexPath.row == 1{
                    cell2.setSelectionStatus(status: CWObject.signOff.worksite)
                }
                else{
                    cell2.setSelectionStatus(status: CWObject.signOff.SIMOPS)
                }
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.selectionStyle = .none
                return cell2
            }
            else{
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.tag = indexPath.row
                cell1.valueTextField.delegate = self
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: CWObject.signOff.others)
                cell1.selectionStyle = .none
                return cell1
            }
            
        }
        else{
            
            cell2.clipsToBounds = true
            cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
            cell2.checkButton.tag = 4
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
            headerView.backgroundColor = UIColor(named: "BlackColor")//UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
            label.textColor = UIColor(named: "ImageTintColor")
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
        else
        {
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
   else {
            if CWObject.signOff.ihaveReviewedThisWork == 0{
                buttonStatus = true
                CWObject.signOff.ihaveReviewedThisWork = 1
            }
            else{
                buttonStatus = false
                CWObject.signOff.ihaveReviewedThisWork = 0
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
        sender.isOn = switchStatus!
        
    }
    
    func setDataForApproval()
    {
        let finalDict = [
            "status" : "APPROVED",
            "ptwApprovalDtoList": [
                "permitNumber" : CWObject.header.permitNo,
                "preJobWalkthroughBy": CWObject.signOff.walkthrough,
                "isWorkSafeToPerform": CWObject.signOff.isSafeContinue,
                "controlBoardDistribution": CWObject.signOff.controlBoard,
                "worksiteDistribution": CWObject.signOff.worksite,
                "otherDistribution": CWObject.signOff.others,
                "approvalDate": CWObject.signOff.dateTime,
                "picName": CWObject.signOff.name,
                "picDate": CWObject.signOff.dateTime,
                "superitendentName": "",
                "isHWP": 0,
                "simopsDistribution": CWObject.signOff.SIMOPS,
                "isCSE": 0,
                "approvedBy": currentUser.name! as Any ,
                "superitendentDate": "",
                "isCWP": 1
                ] as [String : Any]
            ] as [String : Any]
        
        if ConnectionCheck.isConnectedToNetwork(){
            DispatchQueue.main.async {
                self.loaderStart()
            }
        let postData = try? JSONSerialization.data(withJSONObject: finalDict, options: [])
        
            let stringURL = IMOEndpoints.approvePermit
        //let stringURL = "\(BaseUrl.apiURL)/com.iop.ptw/ApprovePermit.xsjs"
        print(stringURL)
        
        let Username = "IOP"
        let Password = "Incture1234567891013"
        let credentialDataHeader = "\(Username):\(Password)".data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))!
        
        let base64CredentialsHeader = credentialDataHeader.base64EncodedString()
        
        let header = ["Content-Type" : "application/json"]
        
        //let postData = try? JSONSerialization.data(withJSONObject: params, options: [])
        let request = NSMutableURLRequest(url: NSURL(string: stringURL)! as URL, cachePolicy: .useProtocolCachePolicy, timeoutInterval: 60.0)
        request.httpMethod = "POST"
        request.allHTTPHeaderFields = header
        request.httpBody = postData! as Data
        
            let session = ImoPtwNetworkManager.shared.urlSession
        //loaderStart()
        
            let dataTask = session?.dataTask(with: request as URLRequest, completionHandler: { (data, response, error) -> Void in
            
            if response != nil
            {
                let httpResponse = response as? HTTPURLResponse
                let statusCode = httpResponse?.statusCode
                print(statusCode ?? "")
                if (error != nil) {
                    print(error)
                    //self.loaderStop()
                } else {
                    
                    DispatchQueue.main.async {
                        self.loaderStop()
                    }
                    
                    if (httpResponse?.statusCode)! >= 200 && (httpResponse?.statusCode)! <= 220
                    {
                        do {
                            let jsonDict = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as? NSDictionary
                            let msg = jsonDict?.value(forKey: "Success") as! String
                            let permitListService = PermitDetailModelService(context: self.context)
                            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
                            let permitModelList = permitListService.get(withPredicate: searchPredicate)
                            var permitListObject = JSA()
                            for each in permitModelList{
                                if each.permitType == PermitType.CWP.rawValue{
                                   each.getPermitData().CWP.header.status = "approved"
                                   permitListObject = each.getPermitData()
                                   permitListService.delete(id: each.objectID)
                                }
                            }
                            _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue)
                            permitListService.saveChanges()
                            DispatchQueue.main.async {
                                let alertController = UIAlertController.init(title: "", message:msg , preferredStyle: UIAlertController.Style.alert)
                                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                                    self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                                    JSAObject = JSA()
                                })
                                alertController.addAction(okAction)
                                self.present(alertController, animated: true, completion: nil)
                            }
                            
                        } catch {}
                    }
                    else
                    {
                        DispatchQueue.main.async {
                            let alertController = UIAlertController.init(title: "", message:"Server Error" , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            //alertController.view.tintColor = GAConstantColor.GAThemeDarkPurple
                            self.present(alertController, animated: true, completion: nil)
                        }
                    }
                }
            }
            else
            {
                print("request timed out")
                DispatchQueue.main.async {
                    self.loaderStop()
                }
                DispatchQueue.main.async {
                    let alertController = UIAlertController.init(title: "", message:"Connection timed out." , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    //alertController.view.tintColor = GAConstantColor.GAThemeDarkPurple
                    self.present(alertController, animated: true, completion: nil)
                }
            }
            
            
        })
            dataTask?.resume()
        
        
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
                if JSAObject.currentFlow == .CWP && each.permitType == PermitType.CWP.rawValue{
                    permitListObject = each.getPermitList()
                    permitListObject.status = "approved"
                    permitListService.delete(id: each.objectID)
                    _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue, facilityOrSite: currentLocation.facilityOrSite)
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
    
    //MARK: - Loader
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
    //    UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    
}

extension PicSignOffViewController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
    
        guard JSAObject.currentFlow == .CWP else {
            return
        }
        if textField.tag == 2 {
            CWObject.signOff.dateTime = date.toDateFormat(.long)
            dateNtime = date
            textField.text = date.toDateFormat(.dayMonthYearwithTime)
        }
        
    }
}
