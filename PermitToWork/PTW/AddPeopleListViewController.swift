//
//  AddPeopleListViewController.swift
//  Murphy_PWT_iOS
//
//  Created by Parul Thakur77 on 11/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class AddPeopleListViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var updateBottomView: UIView!
    @IBOutlet weak var approveBottomView: UIView!
    @IBOutlet weak var addPeopleListTableview: UITableView!
    
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    var addPeopleArray = [People]()
    let defaults = UserDefaults.standard
    var isApproveProcess = false
    var isUpdateProcess = false
    var isJSAApproved = false
    var isPermit = false
    var isRequestingApprove = false
    let context = PTWCoreData.shared.managedObjectContext
    let progressHUD = CustomActivityIndicator(text: "Updating...")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        addPeopleListTableview.tableFooterView = UIView()
        self.view.addSubview(progressHUD)
        self.progressHUD.hide()
        if let val = UserDefaults.standard.value(forKey: "JSACreate") as? String
        {
            if val == "true"
            {
                isApproveProcess = true
                approveBottomView.isHidden = true
                updateBottomView.isHidden = true
                
            }
            else if val == "false"
            {
                if JSAObject.status.lowercased() == "submitted"
                {
                    isApproveProcess = true
                    approveBottomView.isHidden = true
                    updateBottomView.isHidden = false
                    isUpdateProcess = false
                    isJSAApproved = false
                }
                else if JSAObject.status.lowercased() == "approved"{
                    isApproveProcess = false
                    approveBottomView.isHidden = true
                    updateBottomView.isHidden = false
                    isUpdateProcess = false
                    isJSAApproved = true
                    
                }
                else
                {
                    isApproveProcess = false
                    approveBottomView.isHidden = true
                    updateBottomView.isHidden = true
                    isUpdateProcess = false
                    isJSAApproved = false
                }
            }
        }
        
        if (JSAObject.currentFlow == .CWP && (JSAObject.CWP.header.status.lowercased() == "submitted" || JSAObject.CWP.header.status.lowercased() == "approved")) || (JSAObject.currentFlow == .HWP && (JSAObject.HWP.header.status.lowercased() == "submitted" || JSAObject.HWP.header.status.lowercased() == "approved")) || (JSAObject.currentFlow == .CSEP && (JSAObject.CSEP.header.status.lowercased() == "submitted" || JSAObject.CSEP.header.status.lowercased() == "approved"))
        {
            isPermit = true
            approveBottomView.isHidden = true
            updateBottomView.isHidden = false
        }
   
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if pushToPermitController
        {
            pushToPermitController = false
            let dashBoardVC = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
            navigationController?.pushViewController(dashBoardVC, animated: false)
        }
        addPeopleArray = JSAObject.peopleList
        self.addPeopleListTableview.reloadData()
    }
    
    func loaderStart(){
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
     //   UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return addPeopleArray.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:AddPeopleListTableViewCell = (addPeopleListTableview.dequeueReusableCell(withIdentifier: "AddPeopleListTableViewCell") as! AddPeopleListTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        
        let fullname = "\(addPeopleArray[indexPath.row].firstName) \(addPeopleArray[indexPath.row].lastName)"
        
        cell.nameLabel?.text = fullname
        cell.detailLabel?.text = "\(addPeopleArray[indexPath.row].designation)"
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleViewController") as! AddPeopleViewController
        dashBoardVC.index = indexPath.row
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    
    internal func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        
        
        //        if editingStyle == .delete {
        //            print("Deleted")
        //
        //            self.addPeopleArray.remove(at: indexPath.row)
        //            self.addPeopleListTableview.deleteRows(at: [indexPath], with: .automatic)
        //        }
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60
    }
    @IBAction func onPreviousPress(_ sender: Any)
    {
        
        self.navigationController?.popViewController(animated: true)
        //        if JSAObject.currentFlow == .CWP{
        //
        //            self.navigationController?.popViewController(animated: true)
        //
        //        }
        //        else{
        //        guard let viewControllers: [StopJobVCViewController] = self.navigationController?.viewControllers.filter({$0 is StopJobVCViewController}) as? [StopJobVCViewController], let stopJobVC = viewControllers.filter({$0.isJsaPreview == false}).first   else {
        //            return
        //        }
        //        self.navigationController?.popToViewController(stopJobVC, animated: false)
        //        }
        
    }
    
    
    // Submit Press
    @IBAction func onNextPress(_ sender: Any)
    {
        
        if JSAObject.currentFlow == .CWP {
            JSAObject.hasCWP = 1
        }
        else if JSAObject.currentFlow == .HWP {
            JSAObject.hasHWP = 1
        }
        else if JSAObject.currentFlow == .CSEP {
            JSAObject.hasCSP = 1
        }
        if JSAObject.peopleList.count == 0
        {
            let alert = UIAlertController(title: nil, message: "You can not create a JSA without adding a person.", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if ConnectionCheck.isConnectedToNetwork()
            {
                let paragraphStyle = NSMutableParagraphStyle()
                paragraphStyle.alignment = NSTextAlignment.left
                let attributedMessage: NSMutableAttributedString = NSMutableAttributedString(
                    string: "The Work Team has assessed the worksite conditions and confirms:\n\n\n▪ The JSA addresses the applicable hazards and necessary standards\n▪ The Team has the appropriate resources (people & equipment) to do the job safely\n▪ Others that could be affected by the work have been informed\n▪ Energy isolation (if applicable) has been VERIFIED and DEMONSTRATED", // your string message here
                    attributes: [
                        NSAttributedString.Key.paragraphStyle: paragraphStyle,
                        NSAttributedString.Key.font: UIFont.systemFont(ofSize: 13.0)
                    ]
                )
                
                if JSAObject.currentFlow == .JSA
                {
                    let alert = UIAlertController(title: "Do you want to submit and approve this JSA?", message: nil, preferredStyle: .alert)
                    alert.setValue(attributedMessage, forKey: "attributedMessage")
                    alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                        print("User click Dismiss button")
                    }))
                    
                    alert.addAction(UIAlertAction(title: "Yes", style: .default , handler:{ (UIAlertAction)in
                        self.createRequest()
                    }))
                    self.present(alert, animated: true, completion: {
                        print("completion block")
                    })
                    
                    
                }
                else if JSAObject.currentFlow == .CWP
                {
                    
                    let alert = UIAlertController(title: "Do you want to submit & approve Cold Work Permit and JSA?", message: nil, preferredStyle: .alert)
                    alert.setValue(attributedMessage, forKey: "attributedMessage")
                    alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                        print("User click Dismiss button")
                    }))
                    alert.addAction(UIAlertAction(title: "Yes", style: .default , handler:{ (UIAlertAction)in
                        self.createRequest()
                    }))
                    
                    self.present(alert, animated: true, completion: {
                        print("completion block")
                    })
                    
                }
                else if JSAObject.currentFlow == .HWP
                {
                    let alert = UIAlertController(title: "Do you want to submit Hot Work Permit, Cold Work Permit and JSA & approve Cold Work Permit and JSA?", message: nil, preferredStyle: .alert)
                    alert.setValue(attributedMessage, forKey: "attributedMessage")
                    alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                        print("User click Dismiss button")
                    }))
                    
                    alert.addAction(UIAlertAction(title: "Yes", style: .default , handler:{ (UIAlertAction)in
                        
                        self.createRequest()
                        
                    }))
                    
                    self.present(alert, animated: true, completion: {
                        print("completion block")
                    })
                }
                else if JSAObject.currentFlow == .CSEP
                {
                    let alert = UIAlertController(title: nil, message: "Do you want to submit Confined Space Permit, Hot Work Permit, Cold Work Permit and JSA & approve Cold Work Permit and JSA?", preferredStyle: .alert)
                    alert.setValue(attributedMessage, forKey: "attributedMessage")
                    alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                        print("User click Dismiss button")
                    }))
                    
                    alert.addAction(UIAlertAction(title: "Yes", style: .default , handler:{ (UIAlertAction)in
                        
                        self.createRequest()
                        
                    }))
                    
                    self.present(alert, animated: true, completion: {
                        print("completion block")
                    })
                }
            }
            else
            {
                let alertController = UIAlertController.init(title: "Device should be online for Creating JSA.", message:nil , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: { (action) in
                })
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
            
        }
    }
    
    @IBAction func onClickUpdate(_ sender: UIButton) {
        
        // update check for 12 hours
        var flag  = true
        
        let dateComponentsFormatter = DateComponentsFormatter()
        dateComponentsFormatter.allowedUnits = [.year,.month,.weekOfMonth,.day,.hour,.minute,.second]
        
        dateComponentsFormatter.maximumUnitCount = 1
        dateComponentsFormatter.unitsStyle = .full
        dateComponentsFormatter.string(from: Date(), to: Date(timeIntervalSinceNow: 4000000))
        
        var date1 = JSAObject.approvedDate
        if date1 != ""
        {
            date1 = date1.convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertFromUTC: true)!
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
            let approvedDate = dateFormatter.date(from: date1)
            
            let date = Date()
            let seconds = date.seconds(from: approvedDate!)
            
            if seconds > 12*60*60
            {
                flag = false
            }
            
        }
        else
        {
            
            print("JSA not approved yet")
        }
        
        if flag  // JSA is active
        {
            if isPermit // If permit is updated
            {
                let alertController = UIAlertController.init(title: "", message:"Do you want to update this Permit?" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "Yes", style: UIAlertAction.Style.default, handler: { (action) in
                    
                    // CAll update API
                    self.updatePermit()
                })
                let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                    
                })
                alertController.addAction(cancelAction)
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
                
            }
            else // if jsa is updated
            {
                // if update contains CWP create, the check for online
                if JSAObject.hasCWP == 1 && JSAObject.isCWP == 1
                {
                    if ConnectionCheck.isConnectedToNetwork()
                    {
                        let alertController = UIAlertController.init(title: "", message:"Do you want to update this JSA?" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "Yes", style: UIAlertAction.Style.default, handler: { (action) in
                            
                            // CAll update API
                            self.updateRequest()
                        })
                        let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                            
                        })
                        alertController.addAction(cancelAction)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    else
                    {
                        let alertController = UIAlertController.init(title: "Device should be online for creating Cold Work Permit .", message:nil , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: { (action) in
                        })
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                }
                else
                {
                    let alertController = UIAlertController.init(title: "", message:"Do you want to update this JSA?" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "Yes", style: UIAlertAction.Style.default, handler: { (action) in
                        
                        // CAll update API
                        self.updateRequest()
                    })
                    let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                        
                    })
                    alertController.addAction(cancelAction)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                
            }
        }
        else
        {
            let alertController = UIAlertController.init(title: "", message:"JSA is inactive, you are not allowed to update" , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "Yes", style: UIAlertAction.Style.default, handler: { (action) in
                
                //                self.updateRequest()
            })
            let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                
            })
            alertController.addAction(cancelAction)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
        
    }
    
    @IBAction func onClickApprove(_ sender: UIButton) {
        
        if ConnectionCheck.isConnectedToNetwork(){
        
            let paragraphStyle = NSMutableParagraphStyle()
            paragraphStyle.alignment = NSTextAlignment.left
            let attributedMessage: NSMutableAttributedString = NSMutableAttributedString(
                string: "The Work Team has assessed the worksite conditions and confirms:\n\n\n▪ The JSA addresses the applicable hazards and necessary standards\n▪ The Team has the appropriate resources (people & equipment) to do the job safely\n▪ Others that could be affected by the work have been informed\n▪ Energy isolation (if applicable) has been VERIFIED and DEMONSTRATED", // your string message here
                attributes: [
                    NSAttributedString.Key.paragraphStyle: paragraphStyle,
                    NSAttributedString.Key.font: UIFont.systemFont(ofSize: 13.0)
                ]
            )
            
            let alertController = UIAlertController.init(title: "", message:nil , preferredStyle: UIAlertController.Style.alert)
            alertController.setValue(attributedMessage, forKey: "attributedMessage")
            let okAction = UIAlertAction.init(title: "Agree", style: UIAlertAction.Style.default, handler: { (action) in
                
                self.isRequestingApprove = true
                self.updateRequest()
                // CAll Approved API
                
            })
            alertController.addAction(okAction)
            let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: { (action) in
                
            })
            alertController.addAction(cancelAction)
            self.present(alertController, animated: true, completion: nil)
        }
        else{
            let alertController = UIAlertController.init(title: "Device should be online for Reviewing.", message:nil , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: { (action) in
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @IBAction func onHomePress(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        JSAObject = JSA()
        //self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func onEditPress(_ sender: Any)
    {
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleViewController") as! AddPeopleViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    
    // MARK: - DATA Parsing methods
    
    // ********************************* UPDATE COLD PERMIT *********************************
    
    func setDataToUpdateColdPermit() -> [String:Any]
    {
        
        var permitHeaderArray = [[String:Any]]()
        var permitRequiredDocumentsArray = [[String:Any]]()
        var permitTestResultsArray = [[String:Any]]()
        var permitAtmosphericTestArray = [[String:Any]]()
        var peopleArray = [[String: Any]]()
        var permitApprovalArray = [[String:Any]]()
        
        //people list
        let jsaPeopleArray = JSAObject.peopleList
        
        for people in jsaPeopleArray
        {
            let dict : [String:Any] = [
                "firstName":people.firstName as String,
                "lastName":people.lastName as String,
                "contactNumber":people.contactNumber as String,
                "hasSignedJSA":1,
                "hasSignedCWP":people.hasSignedCWP as Int,
                "hasSignedHWP":people.hasSignedHWP as Int,
                "hasSignedCSE":people.hasSignedCSE as Int
            ]
            peopleArray.append(dict)
        }
        
        if JSAObject.CWP.header.status == "APPROVED"
        {
            permitApprovalArray = [[
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "isWorkSafeToPerform":1,
                "prejobWalkthroughBy":JSAObject.CWP.signOff.walkthrough.utf8EncodedString(),
                "approvedBy":"",
                "approvalDate":"",
                "controlBoardDistribution":JSAObject.CWP.signOff.controlBoard,
                "worksiteDistribution":JSAObject.CWP.signOff.worksite,
                "simopsDistribution":JSAObject.CWP.signOff.SIMOPS,
                "otherDistribution":JSAObject.CWP.signOff.others,
                "picName":JSAObject.CWP.signOff.name,
                "picDate":JSAObject.CWP.signOff.dateTime.convertToDateToMilliseconds(),
                "superitendentName":"dwdd",
                "superitendentDate":"22/02/12"
                ]]
        }
        else
        {
            permitApprovalArray = []
        }
        
        
        let date = JSAObject.CWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
        
        
        let time = JSAObject.CWP.header.startTime
        //        let newTime = time.convertToDateString(format: .hourMinuteSeconds, currentDateStringFormat: .short)
        let plannedDateTime = date + " " + time
        
        
        //header
        let header = [
            "isCWP":1 ,
            "isHWP":0,
            "isCSE":0,
            "plannedDateTime":plannedDateTime.convertToDateToMilliseconds(),
            "location":JSAObject.CWP.header.location,
            "createdBy": JSAObject.CWP.header.permitHolder,
            "contractorPerformingWork":JSAObject.CWP.header.contractorPerformingWork.utf8EncodedString(),
            "estimatedTimeOfCompletion":JSAObject.CWP.header.estimatedTimeOfCompletion.convertToDateToMilliseconds(),
            "equipmentID":JSAObject.CWP.header.equipmentID.utf8EncodedString(),
            "workOrderNumber":JSAObject.CWP.header.workOrderNumber,
            "status":JSAObject.CWP.header.status,
            "permitNumber": JSAObject.CWP.header.permitNo
            ] as [String:Any]
        permitHeaderArray.append(header)
        
        //Required  Docs
        let recDoc = [
            "isCWP":1,
            "isHWP":0,
            "isCSE":0,
            "atmosphericTestRecord":JSAObject.CWP.docs.atmosphericTest as Int,
            "loto":JSAObject.CWP.docs.Loto as Int,
            "procedure":JSAObject.CWP.docs.Procedure as Int,
            "pAndIdOrDrawing":JSAObject.CWP.docs.PnID as Int,
            "certificate":JSAObject.CWP.docs.certificate.utf8EncodedString(),
            "temporaryDefeat":JSAObject.CWP.docs.tempDefeat as Int,
            "rescuePlan":JSAObject.CWP.docs.rescuePlan as Int,
            "sds":JSAObject.CWP.docs.sds as Int,
            "otherWorkPermitDocs":JSAObject.CWP.docs.otherText.utf8EncodedString(),
            "fireWatchChecklist":JSAObject.CWP.docs.fireWatch as Int,
            "liftPlan":JSAObject.CWP.docs.liftPlan as Int,
            "simopDeviation":JSAObject.CWP.docs.simop as Int,
            "safeWorkPractice":JSAObject.CWP.docs.safeWorkPractice as Int,
            "serialNo" : JSAObject.CWP.docs.serialNo as Int
            ] as [String:Any]
        permitRequiredDocumentsArray.append(recDoc)
        
        // permit Test Results
        for val in JSAObject.testResult.preStartTests
        {
            let testResults = [
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "preStartOrWorkTest":"PRESTART",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        // permit Work Period
        for val in JSAObject.testResult.workPeriodTests
        {
            let testResults = [
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "preStartOrWorkTest":"WORKPERIOD",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        
        // final parameters
        let finalDict : [String:Any] = [
            "ptwHeaderDtoList":permitHeaderArray,
            
            "ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            
            "ptwApprovalDtoList":[],
            
            "ptwTestRecordDto":[
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
                "dateOfLastCalibration":JSAObject.atmosphericTesting.dateOfLastCallibration.convertToDateToMilliseconds(),
                "testingFrequency":JSAObject.atmosphericTesting.testFrequency as String,
                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
                "isO2":JSAObject.atmosphericTesting.O2 as Int,
                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String,
                "serialNo" : JSAObject.atmosphericTesting.serialNo as Int
                ] as [String:Any],
            
            "ptwTestResultsDtoList":permitTestResultsArray,
            "ptwPeopleDtoList": peopleArray,
            
            "ptwCwpWorkTypeDto":[
                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String,
                "permitNumber" : JSAObject.CWP.header.permitNo
                ] as [String:Any],
            "ptwHwpWorkTypeDto":[
                "cutting":0,
                "wielding":0,
                "electricalPoweredEquipment":0,
                "grinding":0,
                "abrasiveBlasting":0,
                "otherTypeOfWork":"",
                "descriptionOfWorkToBePerformed":"",
                "permitNumber" : JSAObject.CWP.header.permitNo
            ],
            "ptwCseWorkTypeDto":[
                "tank":0,
                "vessel":0,
                "excavation": 0,
                "pit":0,
                "tower":0,
                "other":"",
                "reasonForCSE":"",
                "permitNumber" : JSAObject.CWP.header.permitNo
            ]
        ]
        
        return finalDict
    }
    
    
    
    
    // ********************************* UPDATE HOT PERMIT *********************************
    
    func setDataToUpdateHotPermit() -> [String:Any]
    {
        var permitHeaderArray = [[String:Any]]()
        var permitRequiredDocumentsArray = [[String:Any]]()
        var permitTestResultsArray = [[String:Any]]()
        var permitAtmosphericTestArray = [[String:Any]]()
        var peopleArray = [[String: Any]]()
        var permitApprovalArray = [[String:Any]]()
        
        //people list
        let jsaPeopleArray = JSAObject.peopleList
        
        for people in jsaPeopleArray
        {
            let dict : [String:Any] = [
                "firstName":people.firstName as String,
                "lastName":people.lastName as String,
                "contactNumber":people.contactNumber as String,
                "hasSignedJSA":1,
                "hasSignedCWP":people.hasSignedCWP as Int,
                "hasSignedHWP":people.hasSignedHWP as Int,
                "hasSignedCSE":people.hasSignedCSE as Int
            ]
            peopleArray.append(dict)
        }
        
        if JSAObject.CWP.header.status == "APPROVED"
        {
            permitApprovalArray = [[
                "isCWP":0,
                "isHWP":1,
                "isCSE":0,
                "isWorkSafeToPerform":1,
                "prejobWalkthroughBy":JSAObject.CWP.signOff.walkthrough,
                "approvedBy": "",
                "approvalDate":"22/02/12",
                "controlBoardDistribution":JSAObject.CWP.signOff.controlBoard,
                "worksiteDistribution":JSAObject.CWP.signOff.worksite,
                "simopsDistribution":JSAObject.CWP.signOff.SIMOPS,
                "otherDistribution":"dad",
                "picName":JSAObject.CWP.signOff.name,
                "picDate":JSAObject.CWP.signOff.dateTime.convertToDateToMilliseconds(),
                "superitendentName":"dwdd",
                "superitendentDate":"22/02/12"
                ]]
        }
        else
        {
            permitApprovalArray = []
        }
        
        
        let date = JSAObject.HWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
        
        
        let time = JSAObject.HWP.header.startTime
        //let newTime = time.convertToDateString(format: .hourMinuteSeconds, currentDateStringFormat: .short)
        let plannedDateTime = date + " " + time
        
        
        //header
        let header = [
            "isCWP":0,
            "isHWP":1,
            "isCSE":0,
            "plannedDateTime":plannedDateTime.convertToDateToMilliseconds(),
            "location":JSAObject.HWP.header.location,
            "createdBy": JSAObject.HWP.header.permitHolder,
            "contractorPerformingWork":JSAObject.HWP.header.contractorPerformingWork.utf8EncodedString(),
            "estimatedTimeOfCompletion":JSAObject.HWP.header.estimatedTimeOfCompletion.convertToDateToMilliseconds(),
            "equipmentID":JSAObject.HWP.header.equipmentID.utf8EncodedString(),
            "workOrderNumber":JSAObject.HWP.header.workOrderNumber.utf8EncodedString(),
            "status":JSAObject.HWP.header.status,
            "permitNumber": JSAObject.HWP.header.permitNo
            ] as [String:Any]
        permitHeaderArray.append(header)
        
        //Required  Docs
        let recDoc = [
            "isCWP":0,
            "isHWP":1,
            "isCSE":0,
            "atmosphericTestRecord":JSAObject.HWP.docs.atmosphericTest as Int,
            "loto":JSAObject.HWP.docs.Loto as Int,
            "procedure":JSAObject.HWP.docs.Procedure as Int,
            "pAndIdOrDrawing":JSAObject.HWP.docs.PnID as Int,
            "certificate":JSAObject.HWP.docs.certificate.utf8EncodedString(),
            "temporaryDefeat":JSAObject.HWP.docs.tempDefeat as Int,
            "rescuePlan":JSAObject.HWP.docs.rescuePlan as Int,
            "sds":JSAObject.HWP.docs.sds as Int,
            "otherWorkPermitDocs":JSAObject.HWP.docs.otherText.utf8EncodedString(),
            "fireWatchChecklist":JSAObject.HWP.docs.fireWatch as Int,
            "liftPlan":JSAObject.HWP.docs.liftPlan as Int,
            "simopDeviation":JSAObject.HWP.docs.simop as Int,
            "safeWorkPractice":JSAObject.HWP.docs.safeWorkPractice as Int,
            "serialNo" : JSAObject.HWP.docs.serialNo as Int
            ] as [String:Any]
        permitRequiredDocumentsArray.append(recDoc)
        
        // permit Test Results
        for val in JSAObject.testResult.preStartTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":1,
                "isCSE":0,
                "preStartOrWorkTest":"PRESTART",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        // permit Work Period
        for val in JSAObject.testResult.workPeriodTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":1,
                "isCSE":0,
                "preStartOrWorkTest":"WORKPERIOD",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        
        // final parameters
        let finalDict : [String:Any] = [
            "ptwHeaderDtoList":permitHeaderArray,
            
            "ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            
            "ptwApprovalDtoList":[],
            
            "ptwTestRecordDto":[
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
                "dateOfLastCalibration":JSAObject.atmosphericTesting.dateOfLastCallibration.convertToDateToMilliseconds(),
                "testingFrequency":JSAObject.atmosphericTesting.testFrequency as String,
                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
                "isO2":JSAObject.atmosphericTesting.O2 as Int,
                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String,
                "serialNo" : JSAObject.atmosphericTesting.serialNo as Int
                ] as [String:Any],
            
            "ptwTestResultsDtoList":permitTestResultsArray,
            "ptwPeopleDtoList": peopleArray,
            
            "ptwCwpWorkTypeDto":[
                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String,
                "permitNumber" : JSAObject.CWP.header.permitNo
                ] as [String:Any],
            "ptwHwpWorkTypeDto":[
                "cutting":JSAObject.HWP.workTypeHW.cutting,
                "wielding":JSAObject.HWP.workTypeHW.welding,
                "electricalPoweredEquipment":JSAObject.HWP.workTypeHW.electricalPoweredEquipment,
                "grinding":JSAObject.HWP.workTypeHW.grinding,
                "abrasiveBlasting":JSAObject.HWP.workTypeHW.abrasiveBlasting,
                "otherTypeOfWork":JSAObject.HWP.workTypeHW.otherText.utf8EncodedString(),
                "descriptionOfWorkToBePerformed":JSAObject.HWP.workTypeHW.descriptionOfWork.utf8EncodedString(),
                "permitNumber" : JSAObject.permitNumber
                ] as [String:Any],
            "ptwCseWorkTypeDto":[
                "tank":0,
                "vessel":0,
                "excavation": 0,
                "pit":0,
                "tower":0,
                "other":"",
                "reasonForCSE":"",
                "permitNumber" : JSAObject.CWP.header.permitNo
                ] as [String:Any],
            ]
        
        return finalDict
    }
    
    // ********************************* UPDATE CSE PERMIT *********************************
    
    func setDataToUpdateCSEPermit() -> [String:Any]
    {
        var permitHeaderArray = [[String:Any]]()
        var permitRequiredDocumentsArray = [[String:Any]]()
        var permitTestResultsArray = [[String:Any]]()
        var permitAtmosphericTestArray = [[String:Any]]()
        var peopleArray = [[String: Any]]()
        var permitApprovalArray = [[String:Any]]()
        
        //people list
        let jsaPeopleArray = JSAObject.peopleList
        
        for people in jsaPeopleArray
        {
            let dict : [String:Any] = [
                "firstName":people.firstName as String,
                "lastName":people.lastName as String,
                "contactNumber":people.contactNumber as String,
                "hasSignedJSA":1,
                "hasSignedCWP":people.hasSignedCWP as Int,
                "hasSignedHWP":people.hasSignedHWP as Int,
                "hasSignedCSE":people.hasSignedCSE as Int
            ]
            peopleArray.append(dict)
        }
        
        if JSAObject.CSEP.header.status == "APPROVED"
        {
            permitApprovalArray = [[
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "isWorkSafeToPerform":1,
                "prejobWalkthroughBy":JSAObject.CSEP.signOff.walkthrough,
                "approvedBy":"sumit",
                "approvalDate":"22/02/12",
                "controlBoardDistribution":JSAObject.CSEP.signOff.controlBoard,
                "worksiteDistribution":JSAObject.CSEP.signOff.worksite,
                "simopsDistribution":JSAObject.CSEP.signOff.SIMOPS,
                "otherDistribution":"dad",
                "picName":JSAObject.CSEP.signOff.name,
                "picDate":JSAObject.CSEP.signOff.dateTime.convertToDateToMilliseconds(),
                "superitendentName":"dwdd",
                "superitendentDate":"22/02/12"
                ]]
        }
        else
        {
            permitApprovalArray = []
        }
        
        
        let date = JSAObject.CSEP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
        
        
        let time = JSAObject.CSEP.header.startTime
        //let newTime = time.convertToDateString(format: .hourMinuteSeconds, currentDateStringFormat: .short)
        let plannedDateTime = date + " " + time
        
        
        //header
        let header = [
            "isCWP":0,
            "isHWP":0,
            "isCSE":1,
            "plannedDateTime":plannedDateTime.convertToDateToMilliseconds(),
            "location":JSAObject.CSEP.header.location,
            "createdBy": JSAObject.CSEP.header.permitHolder,
            "contractorPerformingWork":JSAObject.CSEP.header.contractorPerformingWork.utf8EncodedString(),
            "estimatedTimeOfCompletion":JSAObject.CSEP.header.estimatedTimeOfCompletion.convertToDateToMilliseconds(),
            "equipmentID":JSAObject.CSEP.header.equipmentID.utf8EncodedString(),
            "workOrderNumber":JSAObject.CSEP.header.workOrderNumber.utf8EncodedString(),
            "status":JSAObject.CSEP.header.status,
            "permitNumber": JSAObject.CSEP.header.permitNo
            ] as [String:Any]
        permitHeaderArray.append(header)
        
        //Required  Docs
        let recDoc = [
            "isCWP":0,
            "isHWP":0,
            "isCSE":1,
            "atmosphericTestRecord":JSAObject.CSEP.docs.atmosphericTest as Int,
            "loto":JSAObject.CSEP.docs.Loto as Int,
            "procedure":JSAObject.CSEP.docs.Procedure as Int,
            "pAndIdOrDrawing":JSAObject.CSEP.docs.PnID as Int,
            "certificate":JSAObject.CSEP.docs.certificate.utf8EncodedString() ,
            "temporaryDefeat":JSAObject.CSEP.docs.tempDefeat as Int,
            "rescuePlan":JSAObject.CSEP.docs.rescuePlan as Int,
            "sds":JSAObject.CSEP.docs.sds as Int,
            "otherWorkPermitDocs":JSAObject.CSEP.docs.otherText.utf8EncodedString() ,
            "fireWatchChecklist":JSAObject.CSEP.docs.fireWatch as Int,
            "liftPlan":JSAObject.CSEP.docs.liftPlan as Int,
            "simopDeviation":JSAObject.CSEP.docs.simop as Int,
            "safeWorkPractice":JSAObject.CSEP.docs.safeWorkPractice as Int,
            "serialNo" : JSAObject.CSEP.docs.serialNo as Int
            ] as [String:Any]
        permitRequiredDocumentsArray.append(recDoc)
        
        // permit Test Results
        for val in JSAObject.testResult.preStartTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "preStartOrWorkTest":"PRESTART",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        // permit Work Period
        for val in JSAObject.testResult.workPeriodTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "preStartOrWorkTest":"WORKPERIOD",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        // final parameters
        let finalDict : [String:Any] = [
            "ptwHeaderDtoList":permitHeaderArray,
            
            "ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            
            "ptwApprovalDtoList":[],
            
            "ptwTestRecordDto":[
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
                "dateOfLastCalibration":JSAObject.atmosphericTesting.dateOfLastCallibration.convertToDateToMilliseconds(),
                "testingFrequency":JSAObject.atmosphericTesting.testFrequency as String,
                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
                "isO2":JSAObject.atmosphericTesting.O2 as Int,
                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String,
                "serialNo" : JSAObject.atmosphericTesting.serialNo as Int
                ] as [String:Any],
            
            "ptwTestResultsDtoList":permitTestResultsArray,
            "ptwPeopleDtoList": peopleArray,
            
            "ptwCwpWorkTypeDto":[
                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String,
                "permitNumber" : JSAObject.CWP.header.permitNo
                ] as [String:Any],
            "ptwHwpWorkTypeDto":[
                "cutting":JSAObject.HWP.workTypeHW.cutting as Int,
                "wielding":JSAObject.HWP.workTypeHW.welding as Int,
                "electricalPoweredEquipment":JSAObject.HWP.workTypeHW.electricalPoweredEquipment as Int,
                "grinding":JSAObject.HWP.workTypeHW.grinding as Int,
                "abrasiveBlasting":JSAObject.HWP.workTypeHW.abrasiveBlasting as Int,
                "otherTypeOfWork":JSAObject.HWP.workTypeHW.otherText.utf8EncodedString()  as String,
                "descriptionOfWorkToBePerformed":JSAObject.HWP.workTypeHW.descriptionOfWork.utf8EncodedString() as String,
                "permitNumber" : JSAObject.CWP.header.permitNo  as Int
                ] as [String:Any],
            "ptwCseWorkTypeDto":[
                "tank":JSAObject.CSEP.workTypeCSE.tank as Int,
                "vessel":JSAObject.CSEP.workTypeCSE.vessel as Int,
                "excavation": JSAObject.CSEP.workTypeCSE.excavation as Int,
                "pit":JSAObject.CSEP.workTypeCSE.pit as Int,
                "tower":JSAObject.CSEP.workTypeCSE.tower as Int,
                "other":JSAObject.CSEP.workTypeCSE.other.utf8EncodedString() as String,
                "reasonForCSE":JSAObject.CSEP.workTypeCSE.reasonForCSE.utf8EncodedString() as String,
                "permitNumber" : JSAObject.CSEP.header.permitNo as Int
                ] as [String:Any],
            ]
        
        return finalDict
    }
    
    func setDataToUpdatePermit() -> [String:Any]
    {
        var val = [String:Any]()
        if JSAObject.hasCWP == 1 && JSAObject.currentFlow == .CWP
        {
            val = setDataToUpdateColdPermit()
        }
        if JSAObject.hasHWP == 1 && JSAObject.currentFlow == .HWP
        {
            val = setDataToUpdateHotPermit()
        }
        if JSAObject.hasCSP == 1 && JSAObject.currentFlow == .CSEP
        {
            val = setDataToUpdateCSEPermit()
        }
        return val
    }
    
    func setDataToUpdateJSA() -> [String:Any]
    {
        
        //************************* parsing JSA ********************************
        var isActive = 0
        if isJSAApproved
        {
            isActive = 1
        }
        else
        {
            isActive = 2
        }
        JSAObject.updatedBy = currentUser.name!
        let currentDate = Date().toDateFormat(.long, isUTCTimeZone: false)
        JSAObject.updatedDate = currentDate.convertToDateToMilliseconds()
        
        //people list
        var peopleDict : [String:Any] = ["ptwPeopleDtoList": [[String: Any]]()]
        let jsaPeopleArray = JSAObject.peopleList
        var newArray = [[String: Any]]()
        for people in jsaPeopleArray
        {
            let dict : [String:Any] = [
                "firstName":people.firstName as String,
                "lastName":people.lastName as String,
                "contactNumber":people.contactNumber as String,
                "hasSignedJSA":1,
                "hasSignedCWP":people.hasSignedCWP as Int,
                "hasSignedHWP":people.hasSignedHWP as Int,
                "hasSignedCSE":people.hasSignedCSE as Int
            ]
            newArray.append(dict)
        }
        peopleDict["ptwPeopleDtoList"] = newArray
        
        
        //Potential hazard list
        var hazardDict : [String:Any] = ["jsaStepsDtoList": [[String: Any]]()]
        let potentialHazardArray = JSAObject.potentialHazards
        var newHazardArray = [[String: Any]]()
        for hazard in potentialHazardArray
        {
            let dict : [String:Any] = [
                "taskSteps":hazard.taskStep.utf8EncodedString() as String,
                "potentialHazards":hazard.potentialHazards.utf8EncodedString() as String,
                "hazardControls":hazard.hazardControls.utf8EncodedString() as String,
                "personResponsible":hazard.personResponsible.utf8EncodedString() as String
            ]
            newHazardArray.append(dict)
        }
        hazardDict["jsaStepsDtoList"] = newHazardArray
        
        
        //Stop the job list
        var stopDict : [String:Any] = ["jsaStopTriggerDtoList": [[String: Any]]()]
        let stopArray = JSAObject.stopTheJob
        var newStopArray = [[String: Any]]()
        var newLocationArray = [[String: Any]]()
        for stop in stopArray
        {
            let dict : [String:Any] = [
                "lineDescription": stop.utf8EncodedString(),
                ]
            newStopArray.append(dict)
        }
        stopDict["jsaStopTriggerDtoList"] = newStopArray
        
        
        for each  in JSAObject.location{
            let dict : [String:Any] = [
                "facilityOrSite": each.facilityOrSite,
                "hierachyLevel": each.hierarchyLevel,
                "facility": each.facility,
                "muwi": each.muwi,
                "serialNo" : each.serialNo
            ]
            newLocationArray.append(dict)
        }
        
        
        //************************* parsing PERMIT ********************************
        
        
        var permitHeaderArray = [[String:Any]]()
        var permitRequiredDocumentsArray = [[String:Any]]()
        var permitTestResultsArray = [[String:Any]]()
        var permitAtmosphericTestArray = [[String:Any]]()
        var permitApprovalArray = [[String:Any]]()
        
        
        //************ parsing COLD WORK *******************
        
        
        if JSAObject.hasCWP == 1 && JSAObject.isCWP == 1
        {
            let date = JSAObject.CWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.CWP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.CWP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = date //+ " " + JSAObject.CWP.header.estimatedTimeOfCompletion
            }
            
            //header
            let header = [
                "isCWP":1 ,
                "isHWP":0,
                "isCSE":0,
                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
                "location":JSAObject.CWP.header.location,
                "createdBy": JSAObject.CWP.header.permitHolder,
                "contractorPerformingWork":JSAObject.CWP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
                "equipmentID":JSAObject.CWP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.CWP.header.workOrderNumber.utf8EncodedString(),
                "status":"APPROVED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "atmosphericTestRecord":JSAObject.CWP.docs.atmosphericTest as Int,
                "loto":JSAObject.CWP.docs.Loto as Int,
                "procedure":JSAObject.CWP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.CWP.docs.PnID as Int,
                "certificate":JSAObject.CWP.docs.certificate.utf8EncodedString() as String,
                "temporaryDefeat":JSAObject.CWP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.CWP.docs.rescuePlan as Int,
                "sds":JSAObject.CWP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.CWP.docs.otherText.utf8EncodedString() as String,
                "fireWatchChecklist":JSAObject.CWP.docs.fireWatch as Int,
                "liftPlan":JSAObject.CWP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.CWP.docs.simop as Int,
                "safeWorkPractice":JSAObject.CWP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
            //Approval
            let CWApproval = [
                "permitNumber" : CWObject.header.permitNo,
                "prejobWalkthroughBy": CWObject.signOff.walkthrough,
                "isWorkSafeToPerform": CWObject.signOff.isSafeContinue,
                "controlBoardDistribution": CWObject.signOff.controlBoard,
                "worksiteDistribution": CWObject.signOff.worksite,
                "otherDistribution": CWObject.signOff.others,
                "approvalDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                "picName": CWObject.signOff.name,
                "picDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                "superitendentName": "",
                "isHWP": 0,
                "simopsDistribution": CWObject.signOff.SIMOPS,
                "isCSE": 0,
                "approvedBy": currentUser.name! as Any ,
                "superitendentDate": "",
                "isCWP": 1
                ] as [String : Any]
            permitApprovalArray.append(CWApproval)
            
        }
        
        //************ parsing HOT WORK *******************
        
        if JSAObject.hasHWP == 1 && JSAObject.isHWP == 1
        {
            let date = JSAObject.HWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.HWP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.HWP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = date + " " + time //+ " " + JSAObject.HWP.header.estimatedTimeOfCompletion//Remove the Time
            }
            
            //header
            let header = [
                "isCWP":0 ,
                "isHWP":1,
                "isCSE":0,
                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
                "location":JSAObject.HWP.header.location,
                "createdBy": JSAObject.HWP.header.permitHolder,
                "contractorPerformingWork":JSAObject.HWP.header.contractorPerformingWork,
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
                "equipmentID":JSAObject.HWP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.HWP.header.workOrderNumber,
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":0,
                "isHWP":1,
                "isCSE":0,
                "atmosphericTestRecord":JSAObject.HWP.docs.atmosphericTest as Int,
                "loto":JSAObject.HWP.docs.Loto as Int,
                "procedure":JSAObject.HWP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.HWP.docs.PnID as Int,
                "certificate":JSAObject.HWP.docs.certificate.utf8EncodedString(),
                "temporaryDefeat":JSAObject.HWP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.HWP.docs.rescuePlan as Int,
                "sds":JSAObject.HWP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.HWP.docs.otherText.utf8EncodedString(),
                "fireWatchChecklist":JSAObject.HWP.docs.fireWatch as Int,
                "liftPlan":JSAObject.HWP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.HWP.docs.simop as Int,
                "safeWorkPractice":JSAObject.HWP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
        }
        
        //************ parsing CSE WORK *******************
        
        
        if JSAObject.hasCSP == 1 && JSAObject.isCSP == 1
        {
            let date = JSAObject.CSEP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.CSEP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.CSEP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = date + " " + time //+ " " + JSAObject.CSEP.header.estimatedTimeOfCompletion//Remove the Time
            }
            
            //header
            let header = [
                "isCWP":0 ,
                "isHWP":0,
                "isCSE":1,
                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
                "location":JSAObject.CSEP.header.location,
                "createdBy": JSAObject.CSEP.header.permitHolder,
                "contractorPerformingWork":JSAObject.CSEP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
                "equipmentID":JSAObject.CSEP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.CSEP.header.workOrderNumber.utf8EncodedString(),
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "atmosphericTestRecord":JSAObject.CSEP.docs.atmosphericTest as Int,
                "loto":JSAObject.CSEP.docs.Loto as Int,
                "procedure":JSAObject.CSEP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.CSEP.docs.PnID as Int,
                "certificate":JSAObject.CSEP.docs.certificate.utf8EncodedString(),
                "temporaryDefeat":JSAObject.CSEP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.CSEP.docs.rescuePlan as Int,
                "sds":JSAObject.CSEP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.CSEP.docs.otherText.utf8EncodedString(),
                "fireWatchChecklist":JSAObject.CSEP.docs.fireWatch as Int,
                "liftPlan":JSAObject.CSEP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.CSEP.docs.simop as Int,
                "safeWorkPractice":JSAObject.CSEP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
        }
        
        
        // final parameters
        
        for val in JSAObject.testResult.preStartTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "preStartOrWorkTest":"PRESTART",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date.convertToDateToMilliseconds(),//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        let finalDict : [String:Any] = [
            "jsaheaderDto": [
                "hasCWP": JSAObject.hasCWP,
                "hasHWP": JSAObject.hasHWP,
                "hasCSE": JSAObject.hasCSP,
                "taskDescription": JSAObject.createJSA.taskDescription.utf8EncodedString(),
                "identifyMostSeriousPotentialInjury": JSAObject.createJSA.injuryDescription.utf8EncodedString(),
                "isActive": isActive,
                "status": JSAObject.status,
                "permitNumber": JSAObject.permitNumber
                
            ],
            
            "jsaReviewDto":[
                "createdBy":JSAObject.createdBy,
                "createdDate":JSAObject.createdDate.convertToDateToMilliseconds(),
                "approvedBy":JSAObject.approvedBy,
                "approvedDate":JSAObject.approvedDate.convertToDateToMilliseconds(),
                "lastUpdatedBy":JSAObject.updatedBy,
                "lastUpdatedDate":JSAObject.updatedDate,
                "permitNumber": JSAObject.permitNumber
            ],
            
            "jsaRiskAssesmentDto":[
                "mustModifyExistingWorkPractice":JSAObject.riskAssesment.mustExistingWork,
                "hasContinuedRisk":JSAObject.riskAssesment.afterMitigation,
                "permitNumber": JSAObject.permitNumber
            ],
            
            "jsappeDto":[
                "hardHat":JSAObject.riskAssesment.hardHat,
                "safetyBoot":JSAObject.riskAssesment.safetyShoes,
                "goggles":JSAObject.riskAssesment.goggles,
                "faceShield":JSAObject.riskAssesment.faceShield,
                "safetyGlasses":JSAObject.riskAssesment.safetyGlasses,
                "singleEar":JSAObject.riskAssesment.single,
                "doubleEars":JSAObject.riskAssesment.double,
                "respiratorTypeDescription":JSAObject.riskAssesment.respiratorType.utf8EncodedString(),
                "needSCBA":JSAObject.riskAssesment.SCBA,
                "needDustMask":JSAObject.riskAssesment.dustMask,
                "cottonGlove":JSAObject.riskAssesment.cotton,
                "leatherGlove":JSAObject.riskAssesment.leather,
                "impactProtection":JSAObject.riskAssesment.impactProtection,
                "gloveDescription":JSAObject.riskAssesment.other.utf8EncodedString(),
                "chemicalGloveDescription":JSAObject.riskAssesment.chemical.utf8EncodedString(),
                "fallProtection":JSAObject.riskAssesment.fallProtection,
                "fallRestraint":JSAObject.riskAssesment.fallRestraint,
                "chemicalSuit":JSAObject.riskAssesment.chemicalSuit,
                "apron":JSAObject.riskAssesment.apron,
                "flameResistantClothing":JSAObject.riskAssesment.flameResistantClothing,
                "otherPPEDescription":JSAObject.riskAssesment.otherPPE.utf8EncodedString(),
                "needFoulWeatherGear":JSAObject.riskAssesment.foulWeatherGear,
                "haveConsentOfTaskLeader":1,
                "companyOfTaskLeader":"",
                "permitNumber": JSAObject.permitNumber
            ],
            
            "ptwPeopleDtoList": newArray ,
            "jsaHazardsPressurizedDto":[
                "presurizedEquipment": JSAObject.hazardCategories.categories[0][0] as Int,
                "performIsolation":JSAObject.hazardCategories.categories[0][0] as Int,
                "depressurizeDrain":JSAObject.hazardCategories.categories[0][1] as Int,
                "relieveTrappedPressure":JSAObject.hazardCategories.categories[0][2] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[0][3] as Int,
                "anticipateResidual":JSAObject.hazardCategories.categories[0][4] as Int,
                "secureAllHoses":JSAObject.hazardCategories.categories[0][5] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsVisibilityDto":[
                "poorLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "provideAlternateLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "waitUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][1] as Int,
                "deferUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][2] as Int,
                "knowDistanceFromPoles":JSAObject.hazardCategories.categories[1][3] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            
            "jsaHazardsPersonnelDto":[
                "personnel":JSAObject.hazardCategories.categories[2][0] as Int,
                "performInduction":JSAObject.hazardCategories.categories[2][0] as Int,
                "mentorCoachSupervise":JSAObject.hazardCategories.categories[2][1] as Int,
                "verifyCompetencies":JSAObject.hazardCategories.categories[2][2] as Int,
                "addressLimitations":JSAObject.hazardCategories.categories[2][3] as Int,
                "manageLanguageBarriers":JSAObject.hazardCategories.categories[2][4] as Int,
                "wearSeatBelts":JSAObject.hazardCategories.categories[2][5] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            
            "jsaHazardscseDto":[
                "confinedSpaceEntry":JSAObject.hazardCategories.categories[3][0] as Int,
                "discussWorkPractice":JSAObject.hazardCategories.categories[3][0] as Int,
                "conductAtmosphericTesting":JSAObject.hazardCategories.categories[3][1] as Int,
                "monitorAccess":JSAObject.hazardCategories.categories[3][2] as Int,
                "protectSurfaces":JSAObject.hazardCategories.categories[3][3] as Int,
                "prohibitMobileEngine":JSAObject.hazardCategories.categories[3][4] as Int,
                "provideObserver":JSAObject.hazardCategories.categories[3][5] as Int,
                "developRescuePlan":JSAObject.hazardCategories.categories[3][6] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsSimultaneousDto":
                [
                    "simultaneousOperations":JSAObject.hazardCategories.categories[4][0] as Int,
                    "followSimopsMatrix":JSAObject.hazardCategories.categories[4][0] as Int,
                    "mocRequiredFor":JSAObject.hazardCategories.categories[4][1] as Int,
                    "interfaceBetweenGroups":JSAObject.hazardCategories.categories[4][2] as Int,
                    "useBarriersAnd":JSAObject.hazardCategories.categories[4][3] as Int,
                    "havePermitSigned":JSAObject.hazardCategories.categories[4][4] as Int,
                    "permitNumber": JSAObject.permitNumber
                    ] as [String:Int],
            "jsaHazardsIgnitionDto":[
                "ignitionSources":JSAObject.hazardCategories.categories[5][0] as Int,
                "removeCombustibleMaterials":JSAObject.hazardCategories.categories[5][0] as Int,
                "provideFireWatch":JSAObject.hazardCategories.categories[5][1] as Int,
                "implementAbrasiveBlastingControls":JSAObject.hazardCategories.categories[5][2] as Int,
                "conductContinuousGasTesting":JSAObject.hazardCategories.categories[5][3] as Int,
                "earthForStaticElectricity":JSAObject.hazardCategories.categories[5][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsSubstancesDto":[
                "hazardousSubstances":JSAObject.hazardCategories.categories[6][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[6][0] as Int,
                "followSdsControls":JSAObject.hazardCategories.categories[6][1] as Int,
                "implementHealthHazardControls":JSAObject.hazardCategories.categories[6][2] as Int,
                "testMaterial":JSAObject.hazardCategories.categories[6][3] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsSpillsDto":[
                "potentialSpills":JSAObject.hazardCategories.categories[7][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[7][0] as Int,
                "connectionsInGoodCondition":JSAObject.hazardCategories.categories[7][1] as Int,
                "spillContainmentEquipment":JSAObject.hazardCategories.categories[7][2] as Int,
                "haveSpillCleanupMaterials":JSAObject.hazardCategories.categories[7][3] as Int,
                "restrainHosesWhenNotInUse":JSAObject.hazardCategories.categories[7][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            
            "jsaHazardsWeatherDto":[
                "weather":JSAObject.hazardCategories.categories[8][0] as Int,
                "controlsForSlipperySurface":JSAObject.hazardCategories.categories[8][0] as Int,
                "heatBreak":JSAObject.hazardCategories.categories[8][1] as Int,
                "coldHeaters":JSAObject.hazardCategories.categories[8][2] as Int,
                "lightning":JSAObject.hazardCategories.categories[8][3] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsHighNoiseDto":[
                "highNoise":JSAObject.hazardCategories.categories[9][0] as Int,
                "wearCorrectHearing":JSAObject.hazardCategories.categories[9][0] as Int,
                "manageExposureTimes":JSAObject.hazardCategories.categories[9][1] as Int,
                "shutDownEquipment":JSAObject.hazardCategories.categories[9][2] as Int,
                "useQuietTools":JSAObject.hazardCategories.categories[9][3] as Int,
                "soundBarriers":JSAObject.hazardCategories.categories[9][4] as Int,
                "provideSuitableComms":JSAObject.hazardCategories.categories[9][5] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsDroppedDto":[
                "droppedObjects":JSAObject.hazardCategories.categories[10][0] as Int,
                "markRestrictEntry":JSAObject.hazardCategories.categories[10][0] as Int,
                "useLiftingEquipmentToRaise":JSAObject.hazardCategories.categories[10][1] as Int,
                "secureTools":JSAObject.hazardCategories.categories[10][2] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsLiftingDto":[
                "liftingEquipment":JSAObject.hazardCategories.categories[11][0] as Int,
                "confirmEquipmentCondition":JSAObject.hazardCategories.categories[11][0] as Int,
                "obtainApprovalForLifts":JSAObject.hazardCategories.categories[11][1] as Int,
                "haveDocumentedLiftPlan":JSAObject.hazardCategories.categories[11][2] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsHeightsDto":[
                "workAtHeights":JSAObject.hazardCategories.categories[12][0] as Int,
                "discussWorkingPractice":JSAObject.hazardCategories.categories[12][0] as Int,
                "verifyFallRestraint":JSAObject.hazardCategories.categories[12][1] as Int,
                "useFullBodyHarness":JSAObject.hazardCategories.categories[12][2] as Int,
                "useLockTypeSnaphoooks":JSAObject.hazardCategories.categories[12][3] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsElectricalDto":[
                "portableElectricalEquipment":JSAObject.hazardCategories.categories[13][0] as Int,
                "inspectToolsForCondition":JSAObject.hazardCategories.categories[13][0] as Int,
                "implementGasTesting":JSAObject.hazardCategories.categories[13][1] as Int,
                "protectElectricalLeads":JSAObject.hazardCategories.categories[13][2] as Int,
                "identifyEquipClassification":JSAObject.hazardCategories.categories[13][3] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsMovingDto":[
                "movingEquipment":JSAObject.hazardCategories.categories[14][0] as Int,
                "confirmMachineryIntegrity":JSAObject.hazardCategories.categories[14][0] as Int,
                "provideProtectiveBarriers":JSAObject.hazardCategories.categories[14][1] as Int,
                "observerToMonitorProximityPeopleAndEquipment":JSAObject.hazardCategories.categories[14][2] as Int,
                "lockOutEquipment":JSAObject.hazardCategories.categories[14][3] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[14][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsManualDto":[
                "manualHandling":JSAObject.hazardCategories.categories[15][0] as Int,
                "assessManualTask":JSAObject.hazardCategories.categories[15][0] as Int,
                "limitLoadSize":JSAObject.hazardCategories.categories[15][1] as Int,
                "properLiftingTechnique":JSAObject.hazardCategories.categories[15][2] as Int,
                "confirmStabilityOfLoad":JSAObject.hazardCategories.categories[15][3] as Int,
                "getAssistanceOrAid":JSAObject.hazardCategories.categories[15][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            
            
            "jsaHazardsToolsDto":[
                "equipmentAndTools":JSAObject.hazardCategories.categories[16][0] as Int,
                "inspectEquipmentTool":JSAObject.hazardCategories.categories[16][0] as Int,
                "brassToolsNecessary":JSAObject.hazardCategories.categories[16][1] as Int,
                "useProtectiveGuards":JSAObject.hazardCategories.categories[16][2] as Int,
                "useCorrectTools":JSAObject.hazardCategories.categories[16][3] as Int,
                "checkForSharpEdges":JSAObject.hazardCategories.categories[16][4] as Int,
                "applyHandSafetyPrinciple":JSAObject.hazardCategories.categories[16][5] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsFallsDto":[
                "slipsTripsAndFalls":JSAObject.hazardCategories.categories[17][0] as Int,
                "identifyProjections":JSAObject.hazardCategories.categories[17][0] as Int,
                "flagHazards":JSAObject.hazardCategories.categories[17][1] as Int,
                "secureCables":JSAObject.hazardCategories.categories[17][2] as Int,
                "cleanUpLiquids":JSAObject.hazardCategories.categories[17][3] as Int,
                "barricadeHoles":JSAObject.hazardCategories.categories[17][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsVoltageDto":[
                "highVoltage":JSAObject.hazardCategories.categories[18][0] as Int,
                "restrictAccess":JSAObject.hazardCategories.categories[18][0] as Int,
                "dischargeEquipment":JSAObject.hazardCategories.categories[18][1] as Int,
                "observeSafeWorkDistance":JSAObject.hazardCategories.categories[18][2] as Int,
                "useFlashBurn":JSAObject.hazardCategories.categories[18][3] as Int,
                "useInsulatedGloves":JSAObject.hazardCategories.categories[18][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsExcavationdDto":[
                "excavations":JSAObject.hazardCategories.categories[19][0] as Int,
                "haveExcavationPlan":JSAObject.hazardCategories.categories[19][0] as Int,
                "locatePipesByHandDigging":JSAObject.hazardCategories.categories[19][1] as Int,
                "deEnergizeUnderground":JSAObject.hazardCategories.categories[19][2] as Int,
                "cseControls":JSAObject.hazardCategories.categories[19][3] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            "jsaHazardsMobileDto":[
                "mobileEquipment":JSAObject.hazardCategories.categories[20][0] as Int,
                "assessEquipmentCondition":JSAObject.hazardCategories.categories[20][0] as Int,
                "controlAccess":JSAObject.hazardCategories.categories[20][1] as Int,
                "monitorProximity":JSAObject.hazardCategories.categories[20][2] as Int,
                "manageOverheadHazards":JSAObject.hazardCategories.categories[20][3] as Int,
                "adhereToRules":JSAObject.hazardCategories.categories[20][4] as Int,
                "permitNumber": JSAObject.permitNumber
                ] as [String:Int],
            
            "jsaStepsDtoList": newHazardArray ,
            
            "jsaStopTriggerDtoList": newStopArray,
            
            "ptwHeaderDtoList":permitHeaderArray,
            
            "ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            "ptwApprovalDtoList":permitApprovalArray,
            "ptwCloseOutDtoList":[],
            
//            "ptwTestRecordDto":permitAtmosphericTestArray,
//
//            "ptwTestResultsDtoList":permitTestResultsArray,
            
            "ptwTestRecordDto":[
                "isCWP":0,
                "isHWP":0,
                "isCSE":0,
                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
                "dateOfLastCalibration": JSAObject.atmosphericTesting.dateOfLastCallibration.convertToDateToMilliseconds(),
                "testingFrequency":JSAObject.atmosphericTesting.testFrequency.utf8EncodedString() as String,
                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
                "isO2":JSAObject.atmosphericTesting.O2 as Int,
                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String
                ] as [String:Any],
            
            "ptwTestResultsDtoList":permitTestResultsArray,
            
            "ptwCwpWorkTypeDto":[
                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String
                ] as [String:Any],
            "ptwHwpWorkTypeDto":[
                "cutting":JSAObject.HWP.workTypeHW.cutting as Int,
                "wielding":JSAObject.HWP.workTypeHW.welding as Int,
                "electricalPoweredEquipment":JSAObject.HWP.workTypeHW.electricalPoweredEquipment as Int,
                "grinding":JSAObject.HWP.workTypeHW.grinding as Int,
                "abrasiveBlasting":JSAObject.HWP.workTypeHW.abrasiveBlasting as Int,
                "otherTypeOfWork":JSAObject.HWP.workTypeHW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.HWP.workTypeHW.descriptionOfWork.utf8EncodedString() as String
            ],
            "ptwCseWorkTypeDtor":[
                "tank":JSAObject.CSEP.workTypeCSE.tank as Int,
                "vessel":JSAObject.CSEP.workTypeCSE.vessel as Int,
                "excavation": JSAObject.CSEP.workTypeCSE.excavation as Int,
                "pit":JSAObject.CSEP.workTypeCSE.pit as Int,
                "tower":JSAObject.CSEP.workTypeCSE.tower as Int,
                "other":JSAObject.CSEP.workTypeCSE.other.utf8EncodedString() as String,
                "reasonForCSE":JSAObject.CSEP.workTypeCSE.reasonForCSE.utf8EncodedString() as String
                ] as [String:Any],
            
            "jsaLocationDtoList" : newLocationArray
        ]
        
        
        print("final dictionary - \(finalDict)")
        return finalDict
        
    }
    
    
    func setDataToCreateJSA() -> [String:Any]
    {
        // **********JSA parsing***************
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
        //let currentDate = dateFormatter.string(from: Date())
        
        let currentDate = Date().toDateFormat(.long, isUTCTimeZone: true)
        JSAObject.createdDate = currentDate.convertToDateToMilliseconds()
        JSAObject.createdBy = currentUser.name!
        JSAObject.status = "APPROVED"
        JSAObject.updatedDate = currentDate.convertToDateToMilliseconds()

        
        //people list
        var peopleDict : [String:Any] = ["ptwPeopleDtoList": [[String: Any]]()]
        let jsaPeopleArray = JSAObject.peopleList
        var newArray = [[String: Any]]()
        for people in jsaPeopleArray
        {
            let dict : [String:Any] = [
                "firstName":people.firstName as String,
                "lastName":people.lastName as String,
                "contactNumber":people.contactNumber as String,
                "hasSignedJSA":1,
                "hasSignedCWP":people.hasSignedCWP as Int,
                "hasSignedHWP":people.hasSignedHWP as Int,
                "hasSignedCSE":people.hasSignedCSE as Int
            ]
            newArray.append(dict)
        }
        peopleDict["ptwPeopleDtoList"] = newArray
        
        
        //Potential hazard list
        var hazardDict : [String:Any] = ["jsaStepsDtoList": [[String: Any]]()]
        let potentialHazardArray = JSAObject.potentialHazards
        var newHazardArray = [[String: Any]]()
        for hazard in potentialHazardArray
        {
            let dict : [String:Any] = [
                "taskSteps":hazard.taskStep.utf8EncodedString() as String,
                "potentialHazards":hazard.potentialHazards.utf8EncodedString() as String,
                "hazardControls":hazard.hazardControls.utf8EncodedString() as String,
                "personResponsible":hazard.personResponsible.utf8EncodedString() as String
            ]
            newHazardArray.append(dict)
        }
        hazardDict["jsaStepsDtoList"] = newHazardArray
        
        
        //Stop the job list
        var stopDict : [String:Any] = ["jsaStopTriggerDtoList": [[String: Any]]()]
        let stopArray = JSAObject.stopTheJob
        var newStopArray = [[String: Any]]()
        var newLocationArray = [[String: Any]]()
        if stopArray.count != 0
        {
            for stop in stopArray
            {
                let dict : [String:Any] = [
                    "lineDescription": stop.utf8EncodedString(),
                    ]
                newStopArray.append(dict)
            }
        }
        else
        {
            let dict : [String:Any] = [
                "lineDescription": "",
                ]
            newStopArray.append(dict)
        }
        
        stopDict["jsaStopTriggerDtoList"] = newStopArray
        
        //location
        for each  in JSAObject.location{
            let dict : [String:Any] = [
                "facilityOrSite": each.facilityOrSite,
                "hierachyLevel": each.hierarchyLevel,
                "facility": each.facility,
                "muwi": each.muwi,
                "serialNo" : each.serialNo
            ]
            newLocationArray.append(dict)
        }
        
        //********************* permit parsing***********************
        
        
        var permitHeaderArray = [[String:Any]]()
        var permitRequiredDocumentsArray = [[String:Any]]()
        var permitTestResultsArray = [[String:Any]]()
        var permitApprovalArray = [[String:Any]]()
        
        // ********************************** COLD WORK ********************************
        if JSAObject.hasCWP == 1
        {
            let date = JSAObject.CWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.CWP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.CWP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = JSAObject.CWP.header.estimatedTimeOfCompletion
            }
            
            //header
            let header = [
                "isCWP":1 ,
                "isHWP":0,
                "isCSE":0,
                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
                "location":JSAObject.CWP.header.location,
                "createdBy": JSAObject.CWP.header.permitHolder,
                "contractorPerformingWork":JSAObject.CWP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
                "equipmentID":JSAObject.CWP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.CWP.header.workOrderNumber.utf8EncodedString(),
                "status":"APPROVED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "atmosphericTestRecord":JSAObject.CWP.docs.atmosphericTest as Int,
                "loto":JSAObject.CWP.docs.Loto as Int,
                "procedure":JSAObject.CWP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.CWP.docs.PnID as Int,
                "certificate":JSAObject.CWP.docs.certificate.utf8EncodedString() ,
                "temporaryDefeat":JSAObject.CWP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.CWP.docs.rescuePlan as Int,
                "sds":JSAObject.CWP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.CWP.docs.otherText.utf8EncodedString() ,
                "fireWatchChecklist":JSAObject.CWP.docs.fireWatch as Int,
                "liftPlan":JSAObject.CWP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.CWP.docs.simop as Int,
                "safeWorkPractice":JSAObject.CWP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
            //Approval
            let CWApproval = [
                "permitNumber" : CWObject.header.permitNo,
                "prejobWalkthroughBy": CWObject.signOff.walkthrough,
                "isWorkSafeToPerform": CWObject.signOff.isSafeContinue,
                "controlBoardDistribution": CWObject.signOff.controlBoard,
                "worksiteDistribution": CWObject.signOff.worksite,
                "otherDistribution": CWObject.signOff.others,
                "approvalDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                "picName": CWObject.signOff.name,
                "picDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
                "superitendentName": "",
                "isHWP": 0,
                "simopsDistribution": CWObject.signOff.SIMOPS,
                "isCSE": 0,
                "approvedBy": currentUser.name! as Any ,
                "superitendentDate": "",
                "isCWP": 1
                ] as [String : Any]
            permitApprovalArray.append(CWApproval)
            
            
        }  // ********************************** HOT WORK ********************************
        if JSAObject.hasHWP == 1
        {
            let date = JSAObject.HWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.HWP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.HWP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = JSAObject.HWP.header.estimatedTimeOfCompletion
            }
            
            //header
            let header = [
                "isCWP":0 ,
                "isHWP":1,
                "isCSE":0,
                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
                "location":JSAObject.HWP.header.location,
                "createdBy": JSAObject.HWP.header.permitHolder,
                "contractorPerformingWork":JSAObject.HWP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
                "equipmentID":JSAObject.HWP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.HWP.header.workOrderNumber.utf8EncodedString(),
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":0,
                "isHWP":1,
                "isCSE":0,
                "atmosphericTestRecord":JSAObject.HWP.docs.atmosphericTest as Int,
                "loto":JSAObject.HWP.docs.Loto as Int,
                "procedure":JSAObject.HWP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.HWP.docs.PnID as Int,
                "certificate":JSAObject.HWP.docs.certificate.utf8EncodedString() ,
                "temporaryDefeat":JSAObject.HWP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.HWP.docs.rescuePlan as Int,
                "sds":JSAObject.HWP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.HWP.docs.otherText.utf8EncodedString() ,
                "fireWatchChecklist":JSAObject.HWP.docs.fireWatch as Int,
                "liftPlan":JSAObject.HWP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.HWP.docs.simop as Int,
                "safeWorkPractice":JSAObject.HWP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
        } // ********************************** CSE WORK ********************************
        if JSAObject.hasCSP == 1
        {
            let date = JSAObject.CSEP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.CSEP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.CSEP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = date //+ " " + JSAObject.CSEP.header.estimatedTimeOfCompletion
            }
            
            //header
            let header = [
                "isCWP":0 ,
                "isHWP":0,
                "isCSE":1,
                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
                "location":JSAObject.CSEP.header.location,
                "createdBy": JSAObject.CSEP.header.permitHolder,
                "contractorPerformingWork":JSAObject.CSEP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
                "equipmentID":JSAObject.CSEP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.CSEP.header.workOrderNumber.utf8EncodedString(),
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "atmosphericTestRecord":JSAObject.CSEP.docs.atmosphericTest as Int,
                "loto":JSAObject.CSEP.docs.Loto as Int,
                "procedure":JSAObject.CSEP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.CSEP.docs.PnID as Int,
                "certificate":JSAObject.CSEP.docs.certificate.utf8EncodedString() ,
                "temporaryDefeat":JSAObject.CSEP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.CSEP.docs.rescuePlan as Int,
                "sds":JSAObject.CSEP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.CSEP.docs.otherText.utf8EncodedString() ,
                "fireWatchChecklist":JSAObject.CSEP.docs.fireWatch as Int,
                "liftPlan":JSAObject.CSEP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.CSEP.docs.simop as Int,
                "safeWorkPractice":JSAObject.CSEP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
        }
        
        
        // final parameters
        
        for val in JSAObject.testResult.preStartTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "preStartOrWorkTest":"PRESTART",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date.convertToDateToMilliseconds(),//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        let finalDict : [String:Any] = [
            "jsaheaderDto": [
                "hasCWP": JSAObject.hasCWP,
                "hasHWP": JSAObject.hasHWP,
                "hasCSE": JSAObject.hasCSP,
                "taskDescription": JSAObject.createJSA.taskDescription.utf8EncodedString() as String,
                "identifyMostSeriousPotentialInjury": JSAObject.createJSA.injuryDescription.utf8EncodedString() as String,
                "isActive": 2,
                "status": JSAObject.status
                
            ],
            
            "jsaReviewDto":[
                "createdBy":JSAObject.createdBy,
                "createdDate":JSAObject.createdDate,
                "approvedBy":JSAObject.createdBy,
                "approvedDate":JSAObject.createdDate,
                "lastUpdatedBy":JSAObject.updatedBy,
                "lastUpdatedDate":JSAObject.updatedDate
            ],
            
            "jsaRiskAssesmentDto":[
                "mustModifyExistingWorkPractice":JSAObject.riskAssesment.mustExistingWork,
                "hasContinuedRisk":JSAObject.riskAssesment.afterMitigation
            ],
            
            "jsappeDto":[
                "hardHat":JSAObject.riskAssesment.hardHat,
                "safetyBoot":JSAObject.riskAssesment.safetyShoes,
                "goggles":JSAObject.riskAssesment.goggles,
                "faceShield":JSAObject.riskAssesment.faceShield,
                "safetyGlasses":JSAObject.riskAssesment.safetyGlasses,
                "singleEar":JSAObject.riskAssesment.single,
                "doubleEars":JSAObject.riskAssesment.double,
                "respiratorTypeDescription":JSAObject.riskAssesment.respiratorType.utf8EncodedString(),
                "needSCBA":JSAObject.riskAssesment.SCBA,
                "needDustMask":JSAObject.riskAssesment.dustMask,
                "cottonGlove":JSAObject.riskAssesment.cotton,
                "leatherGlove":JSAObject.riskAssesment.leather,
                "impactProtection":JSAObject.riskAssesment.impactProtection,
                "gloveDescription":JSAObject.riskAssesment.other.utf8EncodedString(),
                "chemicalGloveDescription":JSAObject.riskAssesment.chemical.utf8EncodedString(),
                "fallProtection":JSAObject.riskAssesment.fallProtection,
                "fallRestraint":JSAObject.riskAssesment.fallRestraint,
                "chemicalSuit":JSAObject.riskAssesment.chemicalSuit,
                "apron":JSAObject.riskAssesment.apron,
                "flameResistantClothing":JSAObject.riskAssesment.flameResistantClothing,
                "otherPPEDescription":JSAObject.riskAssesment.otherPPE.utf8EncodedString(),
                "needFoulWeatherGear":JSAObject.riskAssesment.foulWeatherGear,
                "haveConsentOfTaskLeader":1,
                "companyOfTaskLeader":""
            ],
            
            "ptwPeopleDtoList": newArray ,
            "jsaHazardsPressurizedDto":[
                "presurizedEquipment": JSAObject.hazardCategories.categories[0][0] as Int,
                "performIsolation":JSAObject.hazardCategories.categories[0][0] as Int,
                "depressurizeDrain":JSAObject.hazardCategories.categories[0][1] as Int,
                "relieveTrappedPressure":JSAObject.hazardCategories.categories[0][2] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[0][3] as Int,
                "anticipateResidual":JSAObject.hazardCategories.categories[0][4] as Int,
                "secureAllHoses":JSAObject.hazardCategories.categories[0][5] as Int
                ] as [String:Int],
            "jsaHazardsVisibilityDto":[
                "poorLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "provideAlternateLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "waitUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][1] as Int,
                "deferUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][2] as Int,
                "knowDistanceFromPoles":JSAObject.hazardCategories.categories[1][3] as Int
                ] as [String:Int],
            
            "jsaHazardsPersonnelDto":[
                "personnel":JSAObject.hazardCategories.categories[2][0] as Int,
                "performInduction":JSAObject.hazardCategories.categories[2][0] as Int,
                "mentorCoachSupervise":JSAObject.hazardCategories.categories[2][1] as Int,
                "verifyCompetencies":JSAObject.hazardCategories.categories[2][2] as Int,
                "addressLimitations":JSAObject.hazardCategories.categories[2][3] as Int,
                "manageLanguageBarriers":JSAObject.hazardCategories.categories[2][4] as Int,
                "wearSeatBelts":JSAObject.hazardCategories.categories[2][5] as Int
                ] as [String:Int],
            
            "jsaHazardscseDto":[
                "confinedSpaceEntry":JSAObject.hazardCategories.categories[3][0] as Int,
                "discussWorkPractice":JSAObject.hazardCategories.categories[3][0] as Int,
                "conductAtmosphericTesting":JSAObject.hazardCategories.categories[3][1] as Int,
                "monitorAccess":JSAObject.hazardCategories.categories[3][2] as Int,
                "protectSurfaces":JSAObject.hazardCategories.categories[3][3] as Int,
                "prohibitMobileEngine":JSAObject.hazardCategories.categories[3][4] as Int,
                "provideObserver":JSAObject.hazardCategories.categories[3][5] as Int,
                "developRescuePlan":JSAObject.hazardCategories.categories[3][6] as Int
                ] as [String:Int],
            "jsaHazardsSimultaneousDto":
                [
                    "simultaneousOperations":JSAObject.hazardCategories.categories[4][0] as Int,
                    "followSimopsMatrix":JSAObject.hazardCategories.categories[4][0] as Int,
                    "mocRequiredFor":JSAObject.hazardCategories.categories[4][1] as Int,
                    "interfaceBetweenGroups":JSAObject.hazardCategories.categories[4][2] as Int,
                    "useBarriersAnd":JSAObject.hazardCategories.categories[4][3] as Int,
                    "havePermitSigned":JSAObject.hazardCategories.categories[4][4] as Int
                    ] as [String:Int],
            "jsaHazardsIgnitionDto":[
                "ignitionSources":JSAObject.hazardCategories.categories[5][0] as Int,
                "removeCombustibleMaterials":JSAObject.hazardCategories.categories[5][0] as Int,
                "provideFireWatch":JSAObject.hazardCategories.categories[5][1] as Int,
                "implementAbrasiveBlastingControls":JSAObject.hazardCategories.categories[5][2] as Int,
                "conductContinuousGasTesting":JSAObject.hazardCategories.categories[5][3] as Int,
                "earthForStaticElectricity":JSAObject.hazardCategories.categories[5][4] as Int
                ] as [String:Int],
            "jsaHazardsSubstancesDto":[
                "hazardousSubstances":JSAObject.hazardCategories.categories[6][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[6][0] as Int,
                "followSdsControls":JSAObject.hazardCategories.categories[6][1] as Int,
                "implementHealthHazardControls":JSAObject.hazardCategories.categories[6][2] as Int,
                "testMaterial":JSAObject.hazardCategories.categories[6][3] as Int
                ] as [String:Int],
            "jsaHazardsSpillsDto":[
                "potentialSpills":JSAObject.hazardCategories.categories[7][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[7][0] as Int,
                "connectionsInGoodCondition":JSAObject.hazardCategories.categories[7][1] as Int,
                "spillContainmentEquipment":JSAObject.hazardCategories.categories[7][2] as Int,
                "haveSpillCleanupMaterials":JSAObject.hazardCategories.categories[7][3] as Int,
                "restrainHosesWhenNotInUse":JSAObject.hazardCategories.categories[7][4] as Int
                ] as [String:Int],
            
            "jsaHazardsWeatherDto":[
                "weather":JSAObject.hazardCategories.categories[8][0] as Int,
                "controlsForSlipperySurface":JSAObject.hazardCategories.categories[8][0] as Int,
                "heatBreak":JSAObject.hazardCategories.categories[8][1] as Int,
                "coldHeaters":JSAObject.hazardCategories.categories[8][2] as Int,
                "lightning":JSAObject.hazardCategories.categories[8][3] as Int
                ] as [String:Int],
            "jsaHazardsHighNoiseDto":[
                "highNoise":JSAObject.hazardCategories.categories[9][0] as Int,
                "wearCorrectHearing":JSAObject.hazardCategories.categories[9][0] as Int,
                "manageExposureTimes":JSAObject.hazardCategories.categories[9][1] as Int,
                "shutDownEquipment":JSAObject.hazardCategories.categories[9][2] as Int,
                "useQuietTools":JSAObject.hazardCategories.categories[9][3] as Int,
                "soundBarriers":JSAObject.hazardCategories.categories[9][4] as Int,
                "provideSuitableComms":JSAObject.hazardCategories.categories[9][5] as Int
                ] as [String:Int],
            "jsaHazardsDroppedDto":[
                "droppedObjects":JSAObject.hazardCategories.categories[10][0] as Int,
                "markRestrictEntry":JSAObject.hazardCategories.categories[10][0] as Int,
                "useLiftingEquipmentToRaise":JSAObject.hazardCategories.categories[10][1] as Int,
                "secureTools":JSAObject.hazardCategories.categories[10][2] as Int
                ] as [String:Int],
            "jsaHazardsLiftingDto":[
                "liftingEquipment":JSAObject.hazardCategories.categories[11][0] as Int,
                "confirmEquipmentCondition":JSAObject.hazardCategories.categories[11][0] as Int,
                "obtainApprovalForLifts":JSAObject.hazardCategories.categories[11][1] as Int,
                "haveDocumentedLiftPlan":JSAObject.hazardCategories.categories[11][2] as Int
                ] as [String:Int],
            "jsaHazardsHeightsDto":[
                "workAtHeights":JSAObject.hazardCategories.categories[12][0] as Int,
                "discussWorkingPractice":JSAObject.hazardCategories.categories[12][0] as Int,
                "verifyFallRestraint":JSAObject.hazardCategories.categories[12][1] as Int,
                "useFullBodyHarness":JSAObject.hazardCategories.categories[12][2] as Int,
                "useLockTypeSnaphoooks":JSAObject.hazardCategories.categories[12][3] as Int
                ] as [String:Int],
            "jsaHazardsElectricalDto":[
                "portableElectricalEquipment":JSAObject.hazardCategories.categories[13][0] as Int,
                "inspectToolsForCondition":JSAObject.hazardCategories.categories[13][0] as Int,
                "implementGasTesting":JSAObject.hazardCategories.categories[13][1] as Int,
                "protectElectricalLeads":JSAObject.hazardCategories.categories[13][2] as Int,
                "identifyEquipClassification":JSAObject.hazardCategories.categories[13][3] as Int
                ] as [String:Int],
            "jsaHazardsMovingDto":[
                "movingEquipment":JSAObject.hazardCategories.categories[14][0] as Int,
                "confirmMachineryIntegrity":JSAObject.hazardCategories.categories[14][0] as Int,
                "provideProtectiveBarriers":JSAObject.hazardCategories.categories[14][1] as Int,
                "observerToMonitorProximityPeopleAndEquipment":JSAObject.hazardCategories.categories[14][2] as Int,
                "lockOutEquipment":JSAObject.hazardCategories.categories[14][3] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[14][4] as Int
                ] as [String:Int],
            "jsaHazardsManualDto":[
                "manualHandling":JSAObject.hazardCategories.categories[15][0] as Int,
                "assessManualTask":JSAObject.hazardCategories.categories[15][0] as Int,
                "limitLoadSize":JSAObject.hazardCategories.categories[15][1] as Int,
                "properLiftingTechnique":JSAObject.hazardCategories.categories[15][2] as Int,
                "confirmStabilityOfLoad":JSAObject.hazardCategories.categories[15][3] as Int,
                "getAssistanceOrAid":JSAObject.hazardCategories.categories[15][4] as Int
                ] as [String:Int],
            
            
            "jsaHazardsToolsDto":[
                "equipmentAndTools":JSAObject.hazardCategories.categories[16][0] as Int,
                "inspectEquipmentTool":JSAObject.hazardCategories.categories[16][0] as Int,
                "brassToolsNecessary":JSAObject.hazardCategories.categories[16][1] as Int,
                "useProtectiveGuards":JSAObject.hazardCategories.categories[16][2] as Int,
                "useCorrectTools":JSAObject.hazardCategories.categories[16][3] as Int,
                "checkForSharpEdges":JSAObject.hazardCategories.categories[16][4] as Int,
                "applyHandSafetyPrinciple":JSAObject.hazardCategories.categories[16][5] as Int
                ] as [String:Int],
            "jsaHazardsFallsDto":[
                "slipsTripsAndFalls":JSAObject.hazardCategories.categories[17][0] as Int,
                "identifyProjections":JSAObject.hazardCategories.categories[17][0] as Int,
                "flagHazards":JSAObject.hazardCategories.categories[17][1] as Int,
                "secureCables":JSAObject.hazardCategories.categories[17][2] as Int,
                "cleanUpLiquids":JSAObject.hazardCategories.categories[17][3] as Int,
                "barricadeHoles":JSAObject.hazardCategories.categories[17][4] as Int
                ] as [String:Int],
            "jsaHazardsVoltageDto":[
                "highVoltage":JSAObject.hazardCategories.categories[18][0] as Int,
                "restrictAccess":JSAObject.hazardCategories.categories[18][0] as Int,
                "dischargeEquipment":JSAObject.hazardCategories.categories[18][1] as Int,
                "observeSafeWorkDistance":JSAObject.hazardCategories.categories[18][2] as Int,
                "useFlashBurn":JSAObject.hazardCategories.categories[18][3] as Int,
                "useInsulatedGloves":JSAObject.hazardCategories.categories[18][4] as Int
                ] as [String:Int],
            "jsaHazardsExcavationdDto":[
                "excavations":JSAObject.hazardCategories.categories[19][0] as Int,
                "haveExcavationPlan":JSAObject.hazardCategories.categories[19][0] as Int,
                "locatePipesByHandDigging":JSAObject.hazardCategories.categories[19][1] as Int,
                "deEnergizeUnderground":JSAObject.hazardCategories.categories[19][2] as Int,
                "cseControls":JSAObject.hazardCategories.categories[19][3] as Int
                ] as [String:Int],
            "jsaHazardsMobileDto":[
                "mobileEquipment":JSAObject.hazardCategories.categories[20][0] as Int,
                "assessEquipmentCondition":JSAObject.hazardCategories.categories[20][0] as Int,
                "controlAccess":JSAObject.hazardCategories.categories[20][1] as Int,
                "monitorProximity":JSAObject.hazardCategories.categories[20][2] as Int,
                "manageOverheadHazards":JSAObject.hazardCategories.categories[20][3] as Int,
                "adhereToRules":JSAObject.hazardCategories.categories[20][4] as Int
                ] as [String:Int],
            
            "jsaStepsDtoList": newHazardArray ,
            
            "jsaStopTriggerDtoList": newStopArray,
            
            "ptwHeaderDtoList":permitHeaderArray,
            
            "ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            
            "ptwApprovalDtoList": permitApprovalArray,
            
            "ptwCloseOutDtoList":[],
            
//            "ptwTestRecordDto":permitAtmosphericTestArray,
//
//            "ptwTestResultsDtoList":permitTestResultsArray,
            
            "ptwTestRecordDto":[
                "isCWP":0,
                "isHWP":0,
                "isCSE":0,
                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
                "dateOfLastCalibration": JSAObject.atmosphericTesting.dateOfLastCallibration.convertToDateToMilliseconds(),
                "testingFrequency":JSAObject.atmosphericTesting.testFrequency.utf8EncodedString() as String,
                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
                "isO2":JSAObject.atmosphericTesting.O2 as Int,
                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String
                ] as [String:Any],
            
            "ptwTestResultsDtoList":permitTestResultsArray,
            
            "ptwCwpWorkTypeDto":[
                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String
                ] as [String:Any],
            "ptwHwpWorkTypeDto":[
                "cutting":JSAObject.HWP.workTypeHW.cutting as Int,
                "wielding":JSAObject.HWP.workTypeHW.welding as Int,
                "electricalPoweredEquipment":JSAObject.HWP.workTypeHW.electricalPoweredEquipment as Int,
                "grinding":JSAObject.HWP.workTypeHW.grinding as Int,
                "abrasiveBlasting":JSAObject.HWP.workTypeHW.abrasiveBlasting as Int,
                "otherTypeOfWork":JSAObject.HWP.workTypeHW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.HWP.workTypeHW.descriptionOfWork.utf8EncodedString() as String
                ] as [String:Any],
            "ptwCseWorkTypeDto":[
                "tank":JSAObject.CSEP.workTypeCSE.tank as Int,
                "vessel":JSAObject.CSEP.workTypeCSE.vessel as Int,
                "excavation": JSAObject.CSEP.workTypeCSE.excavation as Int,
                "pit":JSAObject.CSEP.workTypeCSE.pit as Int,
                "tower":JSAObject.CSEP.workTypeCSE.tower as Int,
                "other":JSAObject.CSEP.workTypeCSE.other.utf8EncodedString() as String,
                "reasonForCSE":JSAObject.CSEP.workTypeCSE.reasonForCSE.utf8EncodedString() as String
                ] as [String:Any],
            "jsaLocationDtoList" : newLocationArray
        ]
        
        
        print("final dictionary - \(finalDict)")
        return finalDict
        
    }
    
    
    // MARK: - API calls
    
    
    func approveRequest()
    {
        
        if ConnectionCheck.isConnectedToNetwork(){
            var fullName : String = ""
            if currentUser.firstname == nil{
                fullName = UserDefaults.standard.string(forKey: "name")!
            }
            else{
                fullName = "\(currentUser.firstname!)%20\(currentUser.lastname!)"
            }
            DispatchQueue.main.async {
            self.loaderStart()
            }
            let stringURL = IMOEndpoints.approveJSA + "?jsaPermitNumber=\(JSAObject.jsaPermitNumber)&status=APPROVED&approvedBy=\(fullName)" //"\(BaseUrl.apiURL)/com.iop.ptw/ApproveJSA.xsjs?jsaPermitNumber=\(JSAObject.jsaPermitNumber)&status=APPROVED&approvedBy=\(fullName)"
            print(stringURL)
            let header = ["Content-Type" : "application/json"]
            let request = NSMutableURLRequest(url: NSURL(string: stringURL)! as URL, cachePolicy: .useProtocolCachePolicy, timeoutInterval: 60.0)
            request.httpMethod = "PUT"
            request.allHTTPHeaderFields = header
            request.httpBody = nil
            
           // let session = URLSession.shared
            let dataTask = ImoPtwNetworkManager.shared.urlSession.dataTask(with: request as URLRequest, completionHandler: { (data, response, error) -> Void in
                
                if response != nil
                {
                    let httpResponse = response as? HTTPURLResponse
                    let statusCode = httpResponse?.statusCode
                    print(statusCode ?? 0)
                    if (error != nil) {
                        print(error ?? "Error Found")
                    } else {
                        
                        DispatchQueue.main.async {
                            self.loaderStop()
                        }
                        
                        if (httpResponse?.statusCode)! >= 200 && (httpResponse?.statusCode)! <= 220
                        {
                            do {
                                let jsonDict = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as? NSDictionary

                                DispatchQueue.main.async {

                                let msg = jsonDict?.value(forKey: "Success") as! String
                                let jsaDetailService = JSADetailModelService(context: self.context)
                                let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
                                let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
                                if jsaDetail.count>0{
                                    jsaDetailService.delete(id: jsaDetail[0].objectID)
                                }
                                JSAObject.status = "approved"
                                _ = jsaDetailService.create(jsa: JSAObject)
                                jsaDetailService.saveChanges()
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
                            //print("request timed out")
                            
                            DispatchQueue.main.async {
                                let alertController = UIAlertController.init(title: "", message:"\(String(describing: httpResponse!.statusCode)) - Server Error" , preferredStyle: UIAlertController.Style.alert)
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
                        let alertController = UIAlertController.init(title: "", message:"Connection timed out." , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        //alertController.view.tintColor = GAConstantColor.GAThemeDarkPurple
                        self.present(alertController, animated: true, completion: nil)
                    }
                }
                
                
            })
            dataTask.resume()
        }
        else{
            
            let jsaListService = JSAModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let jsaListArray = jsaListService.get(withPredicate: searchPredicate)
            var jsaList = JSAList()
            if jsaListArray.count > 0{
                jsaList = jsaListArray[0].getJSA()
                jsaList.status = "approved"
                jsaListService.delete(id: jsaListArray[0].objectID)
                _ = jsaListService.create(listData: jsaList, permitNumber: JSAObject.permitNumber, facilityOrSite: currentLocation.facilityOrSite)
                jsaListService.saveChanges()
            }
            
            let jsaDetailService = JSADetailModelService(context: self.context)
            //let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
            if jsaDetail.count>0{
                jsaDetailService.delete(id: jsaDetail[0].objectID)
            }
            JSAObject.status = "approved"
            _ = jsaDetailService.create(jsa: JSAObject)
            jsaDetailService.saveChanges()
            
            let actionService = ActionModelService(context: self.context)
            _ = actionService.create(postData:["jsaPermitNumber": JSAObject.jsaPermitNumber], permitNumber: JSAObject.permitNumber, actionType: ActionType.JSAApprove.rawValue)
            actionService.saveChanges()
            let alertController = UIAlertController.init(title: "", message:"JSA approved offline. Connect the device to internet to sync." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                JSAObject = JSA()
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    func createRequest()
    {
        let params = setDataToCreateJSA()
        if ConnectionCheck.isConnectedToNetwork(){
            DispatchQueue.main.async {
            self.loaderStart()
            }
            print(params)
            let urlString : String = IMOEndpoints.createService
           //let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/Create_Service.xsjs"
            var urlRequest = URLRequest(url: URL(string: urlString)!)
            urlRequest.httpMethod = "post"
            urlRequest.httpBody = self.getHttpBodayData(params: params)
            urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
            print("****urlRequest.httpBody***")
            print(urlRequest.httpBody)
            print("****urlRequest.httpBody***")
            ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                    
                if error == nil{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        
                        
                        DispatchQueue.main.async {

                        if let jsonDict2 = JSON as? NSDictionary {
                            print("***jsonDict2***")
                            print(jsonDict2)
                            print("***jsonDict2***")
                            var jsonDict = NSDictionary()
                            jsonDict = ((jsonDict2["data"]  as? NSDictionary)!)
                            print("***jsonDict***")
                            print(jsonDict)
                            print("***jsonDict***")
//                            if JSAObject.permitNumber == 0 && JSAObject.hasCWP == 1
//                            {
//                                // call JSA & CWP combined Approve API
//                            }
//                            else if JSAObject.permitNumber == 0
//                            {
//                                // call JSA Approve API
////                                self.approveRequest()
//                            }
//                            else if JSAObject.hasCWP == 1
//                            {
//                                // call CWP Approve API
//                            }
//                            else
//                            {
                            //renamed Success to success
                                let msg = jsonDict.value(forKey: "success") as! String
                                    self.loaderStop()
                                    let alertController = UIAlertController.init(title: "", message:msg , preferredStyle: UIAlertController.Style.alert)
                                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                                        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                                        JSAObject = JSA()
                                    })
                                    alertController.addAction(okAction)
                                    self.present(alertController, animated: true, completion: nil)
                                }
//                            }
    
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }}else{
                        DispatchQueue.main.async {
                            self.loaderStop()
                            let message = error!.localizedDescription
//                            if response?.httpStatusCode != nil {
//                                message = "\( response?.httpStatusCode) - \(String(describing: err))"
//                            }
                            let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                        
                    }
            }.resume()
        }
        else{
            // check for Permits
            // create JSAList object
            JSAObject.addPeopleIndex = nil
            let value = UserDefaults.standard.string(forKey: "offlinenumber")
            var counter = 0
            if value != nil{
                counter = Int(value!)! - 1
            }
            var facilityOrSiteValue : String = ""
            if currentLocation.hierarchyLevel == "facility"{
                facilityOrSiteValue = currentLocation.facilityOrSite
            }
            else{
                facilityOrSiteValue = currentLocation.muwi
            }
            JSAObject.permitNumber = counter
            JSAObject.isHWP = 0
            JSAObject.isCWP = 0
            JSAObject.isCSP = 0
            let list = JSAList()
            list.jsaPermitNumber = "Not assigned yet"
            list.permitNumber = String(counter)
            list.localPermitInteger = String(counter)
            list.taskDescription = JSAObject.createJSA.taskDescription
            list.status = "submitted"
            list.createdBy = currentUser.name!
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
            list.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
            list.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
            var locationValue = [String]()
            for each in JSAObject.location{
                locationValue.append(each.facilityOrSite)
            }
            list.fieldOrSite = locationValue
            if JSAObject.hasCWP.boolValue{
                list.CWP = "CWP"
                let coldPermit = PermitList()
                coldPermit.jsaPermitNumber = "Not assigned yet"
                coldPermit.ptwPermitNumber = "Not assigned yet"
                coldPermit.permitNumber = String(counter)
                coldPermit.localPermitInteger = String(counter)
                coldPermit.status = "submitted"
                JSAObject.CWP.header.status = "submitted"
                coldPermit.createdBy = currentUser.name!
                coldPermit.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                coldPermit.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                coldPermit.fieldOrSite = locationValue
                
                let permitListService = PermitModelService(context: self.context)
                _ = permitListService.create(detailData: coldPermit, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue, facilityOrSite: facilityOrSiteValue)
                permitListService.saveChanges()
                
                let permitDetailService = PermitDetailModelService(context: self.context)
                _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue)
                permitDetailService.saveChanges()
                
            }
            if JSAObject.hasHWP.boolValue{
                list.HWP = "HWP"
                let hotPermit = PermitList()
                hotPermit.jsaPermitNumber = "Not assigned yet"
                hotPermit.ptwPermitNumber = "Not assigned yet"
                hotPermit.permitNumber = String(counter)
                hotPermit.localPermitInteger = String(counter)
                hotPermit.status = "submitted"
                JSAObject.HWP.header.status = "submitted"
                hotPermit.createdBy = currentUser.name!
                hotPermit.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                hotPermit.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                hotPermit.fieldOrSite = locationValue
                
                let permitListService = PermitModelService(context: self.context)
                _ = permitListService.create(detailData: hotPermit, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue, facilityOrSite: facilityOrSiteValue)
                permitListService.saveChanges()
                
                
                let permitDetailService = PermitDetailModelService(context: self.context)
                _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue)
                permitDetailService.saveChanges()
            }
            if JSAObject.hasCSP.boolValue{
                list.CSE = "CSE"
                let confinedSpacePermit = PermitList()
                confinedSpacePermit.jsaPermitNumber = "Not assigned yet"
                confinedSpacePermit.ptwPermitNumber = "Not assigned yet"
                confinedSpacePermit.permitNumber = String(counter)
                confinedSpacePermit.localPermitInteger = String(counter)
                confinedSpacePermit.status = "submitted"
                JSAObject.CSEP.header.status = "submitted"
                confinedSpacePermit.createdBy = currentUser.name!
                confinedSpacePermit.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
                confinedSpacePermit.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
                confinedSpacePermit.fieldOrSite = locationValue
                
                let permitListService = PermitModelService(context: self.context)
                _ = permitListService.create(detailData: confinedSpacePermit, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue, facilityOrSite: facilityOrSiteValue)
                permitListService.saveChanges()
                
                let permitDetailService = PermitDetailModelService(context: self.context)
                _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue)
                permitDetailService.saveChanges()
            }
            
            let jsaListService = JSAModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let jsaDetail = jsaListService.get(withPredicate: searchPredicate)
            if jsaDetail.count > 0{
                for each in jsaDetail{
                    jsaListService.delete(id: each.objectID)
                }
            }
            _ = jsaListService.create(listData: list, permitNumber: JSAObject.permitNumber, facilityOrSite: facilityOrSiteValue)
            jsaListService.saveChanges()
            // create Permit List objects (if any)
            // create JSAObject
            // create Permit Objects (if any)
            // create Action to create JSA
            
            let jsaDetailService = JSADetailModelService(context: self.context)
            _ = jsaDetailService.create(jsa: JSAObject)
            jsaDetailService.saveChanges()
            
            
            let peopleListService = PeopleListService(context: self.context)
            let locationSearchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue)
            let peopleList = peopleListService.get(withPredicate: locationSearchPredicate)
            if peopleList.count > 0{
                let peopleListValue = peopleList[0].getPeopleList()
                let location = peopleListValue.locationArray
                var counter1 : Int?
                var count = -1
                for each in location{
                    count += 1
                    if each == currentLocation.facilityOrSite{
                        counter1 = count
                    }
                }
                var newPeopleArray = [People]()
                for each in peopleListValue.peopleArray[counter1!]{
                    if Int(each.permitNumber) != JSAObject.permitNumber{
                        newPeopleArray.append(each)
                    }
                }
                
                for each in JSAObject.peopleList{
                    //rajat stopped the string casting
                    each.permitNumber = counter //(describing: counter)
                }
                newPeopleArray.append(contentsOf: JSAObject.peopleList)
                peopleListValue.peopleArray[counter1!] = newPeopleArray
                //let peopleList = PeopleList()
                let peopleAddedList = PeopleAddedList()
                peopleAddedList.locationArray = peopleListValue.locationArray
                peopleAddedList.peopleArray = peopleListValue.peopleArray
                if currentLocation.hierarchyLevel == "facility"{
                    peopleList[0].facilityOrSite = currentLocation.facilityOrSite
                }
                else{
                    peopleList[0].facilityOrSite = currentLocation.muwi
                }
                peopleList[0].setPeopleList(peopleList: peopleAddedList)
                peopleListService.update(withPredicate: locationSearchPredicate, peopleListModel: peopleAddedList)
                peopleListService.saveChanges()
            }
            else{
                for each in JSAObject.peopleList{
                    //rajat stopped the string casting
                    each.permitNumber = counter //(describing: counter)
                }
                let peopleAddedList = PeopleAddedList()
                peopleAddedList.locationArray.append(currentLocation.facilityOrSite)
                peopleAddedList.peopleArray.append(JSAObject.peopleList)
                _ = peopleListService.create(peopleList: peopleAddedList, facilityOrSite: facilityOrSiteValue)
            }
            
            peopleListService.saveChanges()
            
            UserDefaults.standard.set(String(counter),forKey: "offlinenumber")
            UserDefaults.standard.synchronize()
            
            let actionService = ActionModelService(context: self.context)
            _ = actionService.create(postData:params as NSDictionary, permitNumber: JSAObject.permitNumber, actionType: ActionType.JSACreate.rawValue)
            actionService.saveChanges()
            
            let alertController = UIAlertController.init(title: "", message:"JSA created offline. Connect the device to internet to sync." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                JSAObject = JSA()
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
            
        }
        
    }
    
    func updateRequest()
    {
        let params = setDataToUpdateJSA()
        if ConnectionCheck.isConnectedToNetwork(){
            if !self.isRequestingApprove {
                DispatchQueue.main.async {
                self.loaderStart()
                }
            }
            else{
                DispatchQueue.main.async {
                    self.progressHUD.show()
                }
            }
            print(params)
            //print(finalDict)
            let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/JSA_Update_Service.xsjs"
            var urlRequest = URLRequest(url: URL(string: urlString)!)
            urlRequest.httpMethod = "post"
            urlRequest.httpBody = self.getHttpBodayData(params: params)
            urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
            ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                    
                if error == nil{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        
                        DispatchQueue.main.async {
    
                        if let jsonDict = JSON as? NSDictionary {
                            let msg = jsonDict.value(forKey: "Success") as! String
                            let jsaDetailService = JSADetailModelService(context: self.context)
                            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
                            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
                            if jsaDetail.count>0{
                                jsaDetailService.delete(id: jsaDetail[0].objectID)
                            }
                            _ = jsaDetailService.create(jsa: JSAObject)
                            jsaDetailService.saveChanges()
                            if !self.isRequestingApprove {
                                    self.loaderStop()
                                    let alertController = UIAlertController.init(title: "", message:msg , preferredStyle: UIAlertController.Style.alert)
                                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                                        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                                        JSAObject = JSA()
                                    })
                                    alertController.addAction(okAction)
                                    self.present(alertController, animated: true, completion: nil)
                            }
                            else{
                                self.progressHUD.hide()
//                                self.approveRequest()
                            }
                            }
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }}else{
                        DispatchQueue.main.async {
                            self.loaderStop()
                            let message = error!.localizedDescription
//                            if response.response?.statusCode != nil {
//                                message = "\(response.response!.statusCode) - \(String(describing: response.result.value))"
//                            }
                            let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                    }
            }.resume()
        }
        else{
            
            JSAObject.addPeopleIndex = nil
            let jsaDetailService = JSADetailModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
            if jsaDetail.count>0{
                jsaDetailService.delete(id: jsaDetail[0].objectID)
            }
            var facilityOrSiteValue : String = ""
            if currentLocation.hierarchyLevel == "facility"{
                facilityOrSiteValue = currentLocation.facilityOrSite
            }
            else{
                facilityOrSiteValue = currentLocation.muwi
            }
            
            let permitListService = PermitModelService(context: self.context)
            let permitDetailService = PermitDetailModelService(context: self.context)
            let permitDetail = permitListService.get(withPredicate: searchPredicate)
            var hasCWP = false
            var hasHWP = false
            var hasCSEP = false
            var coldPermit : PermitModel?
            var hotPermit : PermitModel?
            var csePermit : PermitModel?
            if permitDetail.count>0{
                for each in permitDetail{
                    if each.permitType == PermitType.CWP.rawValue && (each.facilityOrSite == currentLocation.facility || each.facilityOrSite == currentLocation.muwi){
                        hasCWP = true
                        coldPermit = each
                    }
                    else if each.permitType == PermitType.HWP.rawValue && (each.facilityOrSite == currentLocation.facility || each.facilityOrSite == currentLocation.muwi){
                        hasHWP = true
                        hotPermit = each
                    }
                    else if each.permitType == PermitType.CSEP.rawValue && (each.facilityOrSite == currentLocation.facility || each.facilityOrSite == currentLocation.muwi){
                        hasCSEP = true
                        csePermit = each
                    }
                }
            }
            let jsaListService = JSAModelService(context: self.context)
            let jsaListData = jsaListService.get(withPredicate: searchPredicate)
            var currentJSAListData = [JSAList]()
            var locationString = [String]()
            if jsaListData.count > 0{
                for each in jsaListData{
                    jsaListService.delete(id: each.objectID)
                    locationString.append(each.facilityOrSite!)
                    currentJSAListData.append(each.getJSA())
                }
            }
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
            var locationValue = [String]()
            for each in JSAObject.location{
                locationValue.append(each.facilityOrSite)
            }
            JSAObject.isHWP = 0
            JSAObject.isCWP = 0
            JSAObject.isCSP = 0
            if hasCWP == false && JSAObject.hasCWP.boolValue{
                JSAObject.CWP.header.status = "submitted"
                for each in currentJSAListData{
                    if Int(each.permitNumber)! > 0{
                        each.CWP = "CWP" + "\(each.permitNumber)"
                    }
                    else{
                        each.CWP = "CWP"
                    }
                }
                //JSAObject.hasCWP = 1
                _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue)
                
                let coldPermit = PermitList()
                coldPermit.jsaPermitNumber = String(JSAObject.permitNumber)
                coldPermit.ptwPermitNumber = String(JSAObject.permitNumber)
                coldPermit.permitNumber = String(JSAObject.permitNumber)
                coldPermit.localPermitInteger = ""
                coldPermit.status = "submitted"
                coldPermit.createdBy = currentUser.name!
                coldPermit.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                coldPermit.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                coldPermit.fieldOrSite = locationValue
                _ = permitListService.create(detailData: coldPermit, permitNumber: JSAObject.permitNumber, permitType: PermitType.CWP.rawValue, facilityOrSite: facilityOrSiteValue)
            }
            if hasHWP == false && JSAObject.hasHWP.boolValue{
                JSAObject.HWP.header.status = "submitted"
                for each in currentJSAListData{
                    if Int(each.permitNumber)! > 0{
                        each.HWP = "HWP" + "\(each.permitNumber)"
                    }
                    else{
                        each.HWP = "HWP"
                    }
                }
                //JSAObject.hasHWP = 1
                _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue)
                
                let hotPermit = PermitList()
                hotPermit.jsaPermitNumber = String(JSAObject.permitNumber)
                hotPermit.ptwPermitNumber = String(JSAObject.permitNumber)
                hotPermit.permitNumber = String(JSAObject.permitNumber)
                hotPermit.localPermitInteger = ""
                hotPermit.status = "submitted"
                hotPermit.createdBy = currentUser.name!
                hotPermit.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                hotPermit.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                hotPermit.fieldOrSite = locationValue
                _ = permitListService.create(detailData: hotPermit, permitNumber: JSAObject.permitNumber, permitType: PermitType.HWP.rawValue, facilityOrSite: facilityOrSiteValue)
            }
            if hasCSEP == false && JSAObject.hasCSP.boolValue{
                JSAObject.CSEP.header.status = "submitted"
                for each in currentJSAListData{
                    if Int(each.permitNumber)! > 0{
                        each.CSE = "CSE" + "\(each.permitNumber)"
                    }
                    else{
                        each.CSE = "CSE"
                    }
                }
                //JSAObject.hasCSP = 1
                _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue)
                
                let confinedSpacePermit = PermitList()
                confinedSpacePermit.jsaPermitNumber = String(JSAObject.permitNumber)
                confinedSpacePermit.ptwPermitNumber = String(JSAObject.permitNumber)
                confinedSpacePermit.permitNumber = String(JSAObject.permitNumber)
                confinedSpacePermit.localPermitInteger = ""
                confinedSpacePermit.status = "submitted"
                confinedSpacePermit.createdBy = currentUser.name!
                confinedSpacePermit.createdDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                confinedSpacePermit.lastUpdatedDate = dateFormatter.string(from: Date()).convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!.convertToDateToMilliseconds()
                confinedSpacePermit.fieldOrSite = locationValue
                _ = permitListService.create(detailData: confinedSpacePermit, permitNumber: JSAObject.permitNumber, permitType: PermitType.CSEP.rawValue, facilityOrSite: facilityOrSiteValue)
            }
            
            if jsaListData.count > 0{
                for each in locationString{
                    _ = jsaListService.create(listData: currentJSAListData[0], permitNumber: Int(currentJSAListData[0].permitNumber)!, facilityOrSite: each)
                }
            }
            _ = jsaDetailService.create(jsa: JSAObject)
            jsaDetailService.saveChanges()
            
            permitDetailService.saveChanges()
            permitListService.saveChanges()
            
            let peopleListService = PeopleListService(context: self.context)
            let locationSearchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue)
            let peopleList = peopleListService.get(withPredicate: locationSearchPredicate)
            if peopleList.count > 0{
                let peopleListValue = peopleList[0].getPeopleList()
                let location = peopleListValue.locationArray
                var counter : Int?
                var count = -1
                for each in location{
                    count += 1
                    if each == currentLocation.facilityOrSite{
                        counter = count
                    }
                }
                
                var newPeopleArray = [People]()
                for each in peopleListValue.peopleArray[counter!]{
                    if Int(each.permitNumber) != JSAObject.permitNumber{
                        newPeopleArray.append(each)
                    }
                }
                newPeopleArray.append(contentsOf: JSAObject.peopleList)
                peopleListValue.peopleArray[counter!] = newPeopleArray
                //let peopleList = PeopleList()
                let peopleAddedList = PeopleAddedList()
                peopleAddedList.locationArray = peopleListValue.locationArray
                peopleAddedList.peopleArray = peopleListValue.peopleArray
                if currentLocation.hierarchyLevel == "facility"{
                    peopleList[0].facilityOrSite = currentLocation.facilityOrSite
                }
                else{
                    peopleList[0].facilityOrSite = currentLocation.muwi
                }
                peopleList[0].setPeopleList(peopleList: peopleAddedList)
                peopleListService.update(withPredicate: locationSearchPredicate, peopleListModel: peopleAddedList)
            }
            
            if JSAObject.permitNumber > 0{
                let actionService = ActionModelService(context: self.context)
                _ = actionService.create(postData:params as NSDictionary, permitNumber: JSAObject.permitNumber, actionType: ActionType.JSAUpdate.rawValue)
                actionService.saveChanges()
            }
            
            let alertController = UIAlertController.init(title: "", message:"JSA updated offline. Connect the device to internet to sync." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                JSAObject = JSA()
            })
            alertController.addAction(okAction)
            if !self.isRequestingApprove {
                self.present(alertController, animated: true, completion: nil)
            }
            else{
//                approveRequest()
            }
            
        }
    }
    
    func updatePermit()
    {
        let params = setDataToUpdatePermit()
        if ConnectionCheck.isConnectedToNetwork(){
            DispatchQueue.main.async {
            self.loaderStart()
            }
            print(params)
            let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/UpdatePermit.xsjs"
            var urlRequest = URLRequest(url: URL(string: urlString)!)
            urlRequest.httpMethod = "post"
            urlRequest.httpBody = self.getHttpBodayData(params: params)
            urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
            ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                    
                if error == nil{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        DispatchQueue.main.async {

                        if let jsonDict = JSON as? NSDictionary {
                            let msg = jsonDict.value(forKey: "Success") as! String
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
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }}else{
                        DispatchQueue.main.async {
                            self.loaderStop()
                            let message = error!.localizedDescription
//                            if response.response?.statusCode != nil {
//                                message = "\(response.response!.statusCode) - \(String(describing: response.result.value))"
//                            }
                            let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                        
                    }
            }.resume()
        }
        else{
            
            let permitDetailService = PermitDetailModelService(context: self.context)
            var permitType : String = ""
            JSAObject.addPeopleIndex = nil
            if JSAObject.currentFlow == .CWP{
                permitType = PermitType.CWP.rawValue
            }
            else if JSAObject.currentFlow == .HWP{
                permitType = PermitType.HWP.rawValue
            }
            else if  JSAObject.currentFlow == .CSEP{
                permitType = PermitType.CSEP.rawValue
            }
            
            let jsaDetailService = JSADetailModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
            //var currentJSAObject = JSA()
            if jsaDetail.count>0{
                jsaDetailService.delete(id: jsaDetail[0].objectID)
               // currentJSAObject = jsaDetail[0].getJSA()
            }
            
            //let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: JSAObject.permitNumber))
            let permitDetail = permitDetailService.get(withPredicate: searchPredicate)
            if permitDetail.count>0{
                for each in permitDetail{
                    if permitType == "HOT" && each.permitType == PermitType.HWP.rawValue{
                        permitDetailService.delete(id: each.objectID)
                        //currentJSAObject.HWP = JSAObject.HWP
                    }
                    else if permitType == "COLD" && each.permitType == PermitType.CWP.rawValue{
                        permitDetailService.delete(id: each.objectID)
                      //  currentJSAObject.CWP = JSAObject.CWP
                    }
                    else if permitType == "CSE" && each.permitType == PermitType.CSEP.rawValue{
                        permitDetailService.delete(id: each.objectID)
                      //  currentJSAObject.CSEP = JSAObject.CSEP
                    }
                }
            }
            _ = jsaDetailService.create(jsa: JSAObject)
            jsaDetailService.saveChanges()
            
            _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: permitType)
            permitDetailService.saveChanges()
            
            if JSAObject.permitNumber > 0{
                let actionService = ActionModelService(context: self.context)
                _ = actionService.create(postData:params as NSDictionary, permitNumber: JSAObject.permitNumber, actionType: ActionType.PermitUpdate.rawValue)
                actionService.saveChanges()
            }
            
            let alertController = UIAlertController.init(title: "", message:"Permit updated offline. Connect the device to internet to sync." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                JSAObject = JSA()
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
            
        }
    }
}
