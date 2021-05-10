//
//  PermitCloseOutController.swift
//  IOP_iOS
//
//  Created by Soumya Singh on 27/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class PermitCloseOutController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet var tableView: UITableView!
    var selectStatus : Bool = false
    var dateNtime : Date?
    var permitStr = UserDefaults.standard.string(forKey: "Permit")
    var sectionsLabel = ["For Permit Holder", "For PIC"]
    let context = PTWCoreData.shared.managedObjectContext
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    //    var switchArr = ["Work associated with this permit has been completed"]
    //    var closeOut = ["Work associated with this permit has been completed","Permit Holder name","Work Status Comments"]
    
    
    var tableContent = [["Work associated with this permit has been completed","Name","Work Status Comments","I confirm that all personnel have been accounted for and the work has been restored to a safe and tidy working condition"],["Name", "Time / Date", "I am satisfied that the worksite has been restored to a safe and tidy working conditions. All isolations relevant to this permit must follow the de-isolation procedure prior to reenergizing or a long term isolation must be applied"]]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        let permitNib = UINib(nibName: "PermitCell", bundle: nil)
        tableView.register(permitNib, forCellReuseIdentifier: "PermitCell")
        let workTypeNib = UINib(nibName: "WorkTypeCell", bundle: nil)
        tableView.register(workTypeNib, forCellReuseIdentifier: "WorkTypeCell")
        let frequencyNib = UINib(nibName: "FrequencyCell", bundle: nil)
        tableView.register(frequencyNib, forCellReuseIdentifier: "FrequencyCell")
        tableView.tableFooterView = UIView()
        if JSAObject.currentFlow == .HWP{
            CWObject = JSAObject.HWP
        }
        else if JSAObject.currentFlow == .CSEP{
            CWObject = JSAObject.CSEP
        }
        else if JSAObject.currentFlow == .CWP{
            CWObject = JSAObject.CWP
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
    
    
    @IBAction func onAddPeoplePress(_ sender: UIButton) {
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    
    
    @IBAction func onCloseOutPress(_ sender: UIButton) {
        
        
        if CWObject.closeOut.permitHolderConfirmation == 0{
            
            
            let alertController = UIAlertController.init(title: "", message:" Permit Holder should sign." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
        else if CWObject.closeOut.PICconfirmation == 0{
            let alertController = UIAlertController.init(title: "", message:" PIC should sign." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
            
        }
        else{
            
            let alertController = UIAlertController.init(title: "", message:"Do you want to close this permit" , preferredStyle: UIAlertController.Style.alert)
            let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: nil)
            let agreeAction = UIAlertAction.init(title: "Agree", style: UIAlertAction.Style.default, handler: {action in
                self.setDataForCloseOut()
            })
            alertController.addAction(cancelAction)
            alertController.addAction(agreeAction)
            self.present(alertController, animated: true, completion: nil)
            
        }
        
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        //      UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        DispatchQueue.main.async {
            self.indicator.stopAnimating()
        }
        
    }
    
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 0{
            CWObject.closeOut.closedBy = textField.text!
        }
        else if textField.tag == 1{
            CWObject.closeOut.workStatusComments = textField.text!
        }
        else if textField.tag == 2{
            CWObject.closeOut.picName = textField.text!
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    //    @IBAction func onNextPress(_ sender: Any)
    //    {
    //        if (permitStr == "1")
    //        {
    //            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PicSignOffViewController") as! PicSignOffViewController
    //            self.navigationController?.pushViewController(vc, animated: true)
    //        }
    //        else
    //        {
    //            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "ApprovalToAuthority") as! ApprovalToAuthority
    //            self.navigationController?.pushViewController(vc, animated: true)
    //        }
    //    }
    @IBAction func onHomePress(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        //self.navigationController?.popToRootViewController(animated: true)
    }
    
}

extension PermitCloseOutController: UITableViewDataSource, UITableViewDelegate {
    
    
    
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
            if indexPath.row == 0
            {
                cell3.clipsToBounds = true
                cell3.setLabel(data: tableContent[indexPath.section][indexPath.row])
                cell3.permitSwitch?.addTarget(self, action: #selector(onSwitch), for: .valueChanged)
                cell3.selectionStyle = .none
                return cell3
            }
            else if indexPath.row == 3
            {
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.checkButton?.tag = 1
                cell2.selectionStyle = .none
                return cell2
            }
            else
            {
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.delegate = self
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: "")
                if indexPath.row == 1{
                    cell1.valueTextField.tag = 0
                }
                else if indexPath.row == 2{
                    cell1.valueTextField.tag = 1
                }
                cell1.selectionStyle = .none
                return cell1
            }
        }
        else{
            if indexPath.row == 2
            {
                cell2.clipsToBounds = true
                cell2.setData(labelValue: tableContent[indexPath.section][indexPath.row])
                cell2.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
                cell2.checkButton?.tag = 2
                cell2.selectionStyle = .none
                return cell2
            }
            else
            {
                cell1.clipsToBounds = true
                cell1.keyLabel.numberOfLines = 0
                cell1.valueTextField.delegate = self
                cell1.setData(key: tableContent[indexPath.section][indexPath.row], value: "")
                if indexPath.row == 0{
                    cell1.valueTextField.tag = 2
                }
                else if indexPath.row == 1{
                    cell1.valueTextField.tag = 3
                    cell1.valueTextField.isDatePicker = true
                    cell1.valueTextField.datePickerMode = .date
                }
                cell1.selectionStyle = .none
                return cell1
            }
            
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 45))
        let label = UILabel(frame: CGRect(x: 16, y: 5, width: headerView.frame.size.width, height: 35))
        label.text = sectionsLabel[section]
        label.font = UIFont.boldSystemFont(ofSize: 16)
        label.textColor = UIColor.white
        headerView.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
        //        headerView.backgroundColor = UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        return headerView
    }
    
    //    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    //        return 55
    //    }
    
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
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 45
    }
    
    @objc func buttonSelected(_ sender: AnyObject)
    {
        
        var buttonStatus : Bool?
        
        if sender.tag == 1{
            if CWObject.closeOut.permitHolderConfirmation == 0{
                buttonStatus = true
                CWObject.closeOut.permitHolderConfirmation = 1
            }
            else{
                buttonStatus = false
                CWObject.closeOut.permitHolderConfirmation = 0
            }
        }
        else{
            if CWObject.closeOut.PICconfirmation == 0{
                buttonStatus = true
                CWObject.closeOut.PICconfirmation = 1
            }
            else{
                buttonStatus = false
                CWObject.closeOut.PICconfirmation = 0
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
            if CWObject.closeOut.workCompleted == 0{
                switchStatus = true
                CWObject.closeOut.workCompleted = 1
            }
            else{
                switchStatus = false
                CWObject.closeOut.workCompleted = 0
            }
        }
        sender.isOn = switchStatus!
        
    }
}

extension PermitCloseOutController{
    
    func setDataForCloseOut(){
        
        
        var closeOut = [String : Any]()
        if JSAObject.currentFlow == .CWP{
            closeOut = [
                "permitNumber" : CWObject.header.permitNo,
                "isHWP": 0,
                "isCSE": 0,
                "approvedBy": currentUser.name! as Any ,
                "isCWP": 1,
                "picName": CWObject.closeOut.picName,
                "workCompleted": CWObject.closeOut.workCompleted,
                "closedBy": CWObject.closeOut.closedBy,
                "closedDate": CWObject.closeOut.closedDate.convertToDateToMilliseconds(),
                "workStatusComments": CWObject.closeOut.workStatusComments
            ] as [String : Any]
        }
        else if JSAObject.currentFlow == .HWP{
            closeOut = [
                "permitNumber" : CWObject.header.permitNo,
                "isHWP": 1,
                "isCSE": 0,
                "approvedBy": currentUser.name! as Any ,
                "isCWP": 0,
                "picName": CWObject.closeOut.picName,
                "workCompleted": CWObject.closeOut.workCompleted,
                "closedBy": CWObject.closeOut.closedBy,
                "closedDate": CWObject.closeOut.closedDate.convertToDateToMilliseconds(),
                "workStatusComments": CWObject.closeOut.workStatusComments
            ] as [String : Any]
        }
        else{
            closeOut = [
                "permitNumber" : CWObject.header.permitNo,
                "isHWP": 0,
                "isCSE": 1,
                "approvedBy": currentUser.name! as Any ,
                "isCWP": 0,
                "picName": CWObject.closeOut.picName,
                "workCompleted": CWObject.closeOut.workCompleted,
                "closedBy": CWObject.closeOut.closedBy,
                "closedDate": CWObject.closeOut.closedDate.convertToDateToMilliseconds(),
                "workStatusComments": CWObject.closeOut.workStatusComments
            ] as [String : Any]
        }
        
        var testResultDict = [[String : Any]]()
        for each in JSAObject.testResult.preStartTests{
            var testResult = [String : Any]()
            if JSAObject.currentFlow == .CWP{
                testResult = [
                    "isCWP": 1,
                    "isHWP": 0,
                    "isCSE": 0,
                    "preStartOrWorkTest": "PRESTART",
                    "oxygenPercentage": each.O2 as Any,
                    "toxicType": each.toxicType as Any,
                    "toxicResult": each.toxicResult as Any,
                    "flammableGas": each.flammableGas as Any,
                    "othersType": each.othersType as Any,
                    "othersResult": each.othersResult as Any,
                    "date": each.Date.convertToDateToMilliseconds() as Any,
                    "time": each.Time as Any
                ] as [String : Any]
            }
            else if JSAObject.currentFlow == .HWP{
                testResult = [
                    "isCWP": 0,
                    "isHWP": 1,
                    "isCSE": 0,
                    "preStartOrWorkTest": "PRESTART",
                    "oxygenPercentage": each.O2 as Any,
                    "toxicType": each.toxicType as Any,
                    "toxicResult": each.toxicResult as Any,
                    "flammableGas": each.flammableGas as Any,
                    "othersType": each.othersType as Any,
                    "othersResult": each.othersResult as Any,
                    "date": each.Date.convertToDateToMilliseconds() as Any,
                    "time": each.Time as Any
                ] as [String : Any]
            }
            else{
                testResult = [
                    "isCWP": 0,
                    "isHWP": 0,
                    "isCSE": 1,
                    "preStartOrWorkTest": "PRESTART",
                    "oxygenPercentage": each.O2 as Any,
                    "toxicType": each.toxicType as Any,
                    "toxicResult": each.toxicResult as Any,
                    "flammableGas": each.flammableGas as Any,
                    "othersType": each.othersType as Any,
                    "othersResult": each.othersResult as Any,
                    "date": each.Date.convertToDateToMilliseconds() as Any,
                    "time": each.Time as Any
                ] as [String : Any]
                
            }
            
            testResultDict.append(testResult)
            
        }
        
        for each in JSAObject.testResult.workPeriodTests{
            
            var testResult = [String : Any]()
            if JSAObject.currentFlow == .CWP{
                testResult = [
                    "isCWP": 1,
                    "isHWP": 0,
                    "isCSE": 0,
                    "preStartOrWorkTest": "WORKPERIOD",
                    "oxygenPercentage": each.O2 as Any,
                    "toxicType": each.toxicType as Any,
                    "toxicResult": each.toxicResult as Any,
                    "flammableGas": each.flammableGas as Any,
                    "othersType": each.othersType as Any,
                    "othersResult": each.othersResult as Any,
                    "date": each.Date.convertToDateToMilliseconds() as Any,
                    "time": each.Time as Any
                ] as [String : Any]
            }
            else if JSAObject.currentFlow == .HWP{
                testResult = [
                    "isCWP": 0,
                    "isHWP": 1,
                    "isCSE": 0,
                    "preStartOrWorkTest": "WORKPERIOD",
                    "oxygenPercentage": each.O2 as Any,
                    "toxicType": each.toxicType as Any,
                    "toxicResult": each.toxicResult as Any,
                    "flammableGas": each.flammableGas as Any,
                    "othersType": each.othersType as Any,
                    "othersResult": each.othersResult as Any,
                    "date": each.Date.convertToDateToMilliseconds() as Any,
                    "time": each.Time as Any
                ] as [String : Any]
            }
            else{
                testResult = [
                    "isCWP": 0,
                    "isHWP": 0,
                    "isCSE": 1,
                    "preStartOrWorkTest": "WORKPERIOD",
                    "oxygenPercentage": each.O2 as Any,
                    "toxicType": each.toxicType as Any,
                    "toxicResult": each.toxicResult as Any,
                    "flammableGas": each.flammableGas as Any,
                    "othersType": each.othersType as Any,
                    "othersResult": each.othersResult as Any,
                    "date": each.Date.convertToDateToMilliseconds() as Any,
                    "time": each.Time as Any
                ] as [String : Any]
            }
            
            testResultDict.append(testResult)
            
        }
        
        var closeOutDict = [[String : Any]]()
        closeOutDict.append(closeOut)
        
        
        let finalDict = [
            "status" : "CLOSED",
            "ptwCloseOutDtoList": closeOutDict,
            "ptwTestResultsDtoList": testResultDict
        ] as [String : Any]
        
        if ConnectionCheck.isConnectedToNetwork(){
            self.loaderStart()
            let urlString : String = IMOEndpoints.closeOut
            //let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/CloseOutService.xsjs"
            var urlRequest = URLRequest(url: URL(string: urlString)!)
            urlRequest.httpMethod = "post"
            urlRequest.httpBody = self.getHttpBodayData(params: finalDict)
            urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
            print(finalDict)
            
            ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                self.loaderStop()
                
                if error == nil{
                    guard let data = data else {
                        return
                    }
                    do{
                        
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        
                        if let jsonDict = JSON as? NSDictionary {
                            DispatchQueue.main.async {
                                let message = jsonDict.value(forKey: "data") as? String
                                let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: {action in
                                    self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                                })
                                alertController.addAction(okAction)
                                self.present(alertController, animated: true, completion: nil)
                            }
                        }
                        
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }}else{
                        DispatchQueue.main.async {
                            let message = error!.localizedDescription
                            let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                        
                    }
            }.resume()
        }
        else{
            let actionService = ActionModelService(context: self.context)
            _ = actionService.create(postData:finalDict as NSDictionary, permitNumber: JSAObject.permitNumber, actionType: ActionType.PermitCloseOut.rawValue)
            actionService.saveChanges()
            
            let jsaDetailService = JSADetailModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
            var newJSAObject = JSA()
            newJSAObject = jsaDetail[0].getJSA()
            if jsaDetail.count>0{
                jsaDetailService.delete(id: jsaDetail[0].objectID)
            }
            //newJSAObject.atmosphericTesting = JSAObject
            _ = jsaDetailService.create(jsa: JSAObject)
            jsaDetailService.saveChanges()
            
            let permitListService = PermitModelService(context: self.context)
            let permitModelList = permitListService.get(withPredicate: searchPredicate)
            var permitListObject = PermitList()
            for each in permitModelList{
                if JSAObject.currentFlow == .HWP && each.permitType == PermitType.HWP.rawValue{
                    permitListObject = each.getPermitList()
                    permitListObject.status = "closed"
                    permitListService.delete(id: each.objectID)
                    _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue, facilityOrSite: currentLocation.facilityOrSite)
                }
                else if JSAObject.currentFlow == .CSEP && each.permitType == PermitType.CSEP.rawValue{
                    permitListObject = each.getPermitList()
                    permitListObject.status = "closed"
                    permitListService.delete(id: each.objectID)
                    _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue, facilityOrSite: currentLocation.facilityOrSite)
                }
                else if JSAObject.currentFlow == .CWP && each.permitType == PermitType.CWP.rawValue{
                    permitListObject = each.getPermitList()
                    permitListObject.status = "closed"
                    permitListService.delete(id: each.objectID)
                    _ = permitListService.create(detailData: permitListObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue, facilityOrSite: currentLocation.facilityOrSite)
                }
                
                permitListService.saveChanges()
            }
            
            let alertController = UIAlertController.init(title: "", message:"Permit closed offline. Connect the device to internet to sync." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                JSAObject = JSA()
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
    }
    
}

extension PermitCloseOutController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
        if textField.tag == 3 {
            CWObject.closeOut.closedDate = date.toDateFormat(.long)
            dateNtime = date
            textField.text = date.toDateFormat(.dayMonthYearwithTime)
        }
        
    }
}
