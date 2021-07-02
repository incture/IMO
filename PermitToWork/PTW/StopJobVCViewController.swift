//
//  StopJobVCViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 22/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class StopJobVCViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate{

    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var confirmPeopleBottonView: UIView!
    @IBOutlet weak var addButton: UIBarButtonItem!
    @IBOutlet weak var potentialHazardTableView: UITableView!
    @IBOutlet weak var btnAddPeople: UIButton!
    @IBOutlet weak var btnCreatePeople: UIButton!

    @IBOutlet weak var nextButton: UIButton!
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    var isJsaPreview = false
    var isJsaApproved = false
    var stopJobArray = [String]()
    var note = "Note: I / We will STOP THE JOB if any of the following occur:"
    let defaults = UserDefaults.standard
    override func viewDidLoad() {
        super.viewDidLoad()
    
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "Stop the job - Review"
                isJsaPreview = true
                
               
            }
            else if val == "false"
            {
                navigationTitle.title = "Stop the job"
                isJsaPreview = false
                
            }
        }
        else
        {
            navigationTitle.title = "Stop the job"
            isJsaPreview = false
            
        }
        
        if JSAObject.status.lowercased() == "approved"
        {
            isJsaApproved = true
           
        }
        else
        {
            isJsaApproved = false
           
        }
        
        if creatingTemplate == false{
            if isJsaPreview == true && isJsaApproved == true
            {
                addButton.isEnabled = false
                addButton.customView?.alpha = 0.0
                confirmPeopleBottonView.isHidden = false
            }
            else if isJsaPreview == false && isJsaApproved == true
            {
                addButton.isEnabled = false
                addButton.customView?.alpha = 0.0
                confirmPeopleBottonView.isHidden = true
            }
            else if isJsaPreview == true && isJsaApproved == false
            {
                addButton.isEnabled = false
                addButton.customView?.alpha = 0.0
                confirmPeopleBottonView.isHidden = false
            }
            else
            {
                addButton.isEnabled = true
                addButton.customView?.alpha = 1.0
                confirmPeopleBottonView.isHidden = true
            }
        }
        else{ // while creating template
            addButton.isEnabled = true
            addButton.customView?.alpha = 1.0
            confirmPeopleBottonView.isHidden = false
            nextButton.setTitle("SAVE TEMPLATE", for: .normal)
        }
        
       
       
    }

    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let stringTwo = "STOP THE JOB"
        let range = (note as NSString).range(of: stringTwo)
        let attributedText = NSMutableAttributedString.init(string: note)
        attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.red , range: range)
        noteLabel.attributedText = attributedText
        potentialHazardTableView.reloadData()
        if currentUser.isReadOnly == true{
            self.btnAddPeople.isHidden = true
           //self.btnCreatePeople.isHidden = true
            self.btnCreatePeople.setTitle("DONE", for: .normal)
            let button: UIButton = UIButton(type: UIButton.ButtonType.custom) 
            self.addButton.customView = button
        }
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    func loaderStart()
    {
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

        return JSAObject.stopTheJob.count
        
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:StopJobTableViewCell = (potentialHazardTableView.dequeueReusableCell(withIdentifier: "StopJobTableViewCell") as! StopJobTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        
        cell.nameLabel?.text = JSAObject.stopTheJob[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let VC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddNewJobViewController") as! AddNewJobViewController
        VC.index = indexPath.row
        self.navigationController?.pushViewController(VC, animated: true)
    }
    
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func previousButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
            JSAObject = JSA()
        //self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func addNewJob(_ sender: Any)
    {
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddNewJobViewController") as! AddNewJobViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    
    
    @IBAction func addPeopleBtn(_ sender: UIButton)
    {
        var flag = false
        var cflag = false
        if let currentUser = UserDefaults.standard.value(forKey: "CurrentUser") as? String
        {
            if currentUser == "true"
            {
                cflag = true
            }
            else
            {
                cflag = false
            }
        }
        else
        {
            cflag = false
        }
        if let val = UserDefaults.standard.value(forKey: "JSACreate") as? String
        {
            if val == "true"
            {
                flag = true
            }
            else
            {
                flag = false
            }
        }
        else
        {
            flag = false
        }
        
        if flag && cflag
        {
            let alert = UIAlertController(title: "JSA Review", message: "Pending confirmation by the Task Leader of site conditions, I agree that the attached JSA identifies significant Task Steps, Hazards and Controls", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                UserDefaults.standard.set("false", forKey: "CurrentUser")
                UserDefaults.standard.set("false", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                let people = People()
                let name = currentUser.name!
                let nameArray = name.components(separatedBy: " ")
                people.firstName = nameArray[0]
                people.lastName = nameArray[1]
                people.fullName = name
                people.contactNumber = ""
                people.designation = ""
                if !ConnectionCheck.isConnectedToNetwork(){
                    people.hasSignedJSA = 1
                }
                if JSAObject.permitNumber != 0{
                  
                    people.permitNumber = (JSAObject.permitNumber)
                }
                else{
                    let value = UserDefaults.standard.string(forKey: "offlinenumber")
                    var counter = 0
                    if value != nil{
                        counter = Int(value!)! - 1
                    }
                   
                    people.permitNumber = (counter)
                }
                
                JSAObject.peopleList.append(people)
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if currentUser.isReadOnly != true{
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }
        }
    }
    
    @IBAction func cancelButtonAction(segue: UIStoryboardSegue) {
        
    }
    
    
    @IBAction func createPermitBtn(_ sender: UIButton)
    {
        if JSAObject.peopleList.count == 0
        {
            let alert = UIAlertController(title: "JSA Review", message: "Pending confirmation by the Task Leader of site conditions, I agree that the attached JSA identifies significant Task Steps, Hazards and Controls", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                UserDefaults.standard.set("false", forKey: "CurrentUser")
                UserDefaults.standard.set("false", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                let people = People()
                people.firstName = currentUser.firstname!
                people.lastName = currentUser.lastname!
                people.contactNumber = ""
                people.designation = ""
                people.hasSignedJSA = 1
                
                JSAObject.peopleList.append(people)
                
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreatePermitViewController") as! CreatePermitViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if currentUser.isReadOnly != true{
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreatePermitViewController") as! CreatePermitViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }else if currentUser.isReadOnly == true{
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                    JSAObject = JSA()
            }
        }
        
    }
    
    @IBAction func onClickSubmit(_ sender: UIButton) {
        
        if creatingTemplate == false {
            let alert = UIAlertController(title: nil, message: "I acknowledge I have reviewed the JSA, I understand my roles and responsibilities and I will comply with the instructions for this task", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                
    //            if let val = UserDefaults.standard.data(forKey: "ProposedPeopleJSA")
    //            {
                    JSAObject.addNewpeople.hasSignedJSA = 1
                    if JSAObject.addNewpeople.isCheckedCWP == 1
                    {
                        JSAObject.currentFlow = .CWP
                        pushToPermitController = true
                        guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                            return
                        }
                        print(viewControllers)
                        self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                        
    //                    , let peopleList = viewControllers.filter({$0.isJsaPreview == false}).first
                    }
                    else if JSAObject.addNewpeople.isCheckedHWP == 1
                    {
                        JSAObject.currentFlow = .HWP
                        pushToPermitController = true
                        guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                            return
                        }
                        print(viewControllers)
                        self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                    }
                    else if JSAObject.addNewpeople.isCheckedCSE == 1
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
                        
                        UserDefaults.standard.set("false", forKey: "JSAPreview")
                        UserDefaults.standard.synchronize()
                        
                        JSAObject.peopleList.append(JSAObject.addNewpeople)
                        guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                            return
                        }
                        print(viewControllers)
                        self.navigationController?.popToViewController(viewControllers.first!, animated: false)
    //                    let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
    //                    self.navigationController?.pushViewController(dashBoardVC, animated: true)
                    }
                
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else{//While creating template
            //post API
            let params = setDataToCreateTemplate()
            print(self.getBodyInJSON(params: params))
            if ConnectionCheck.isConnectedToNetwork(){
                DispatchQueue.main.async {
                    self.loaderStart()
                }
                let urlString : String = IMOEndpoints.createTemplate
                var urlRequest = URLRequest(url: URL(string: urlString)!)
                urlRequest.httpMethod = "post"
                urlRequest.httpBody = self.getHttpBodayData(params: params)
                urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
                print("****payload***")
                print(self.getBodyInJSON(params: params))
                print("****payload***")
                ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                        
                    if error == nil{
                        guard let data = data else {
                            return
                        }
                        do{
                            let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                            
                            DispatchQueue.main.async {

                            if let jsonDict = JSON as? NSDictionary {

                               // var jsonDict = NSDictionary()
                               // jsonDict = ((jsonDict2["data"]  as? NSDictionary ?? NSDictionary()))
                                //renamed Success to success
                                    let msg = jsonDict.value(forKey: "message") as! String
                                        self.loaderStop()
                                        let alertController = UIAlertController.init(title: "", message:msg , preferredStyle: UIAlertController.Style.alert)
                                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { (action) in
                                            self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                                            JSAObject = JSA()
                                            creatingTemplate = false
                                        })
                                        alertController.addAction(okAction)
                                        self.present(alertController, animated: true, completion: nil)
                                    }
                            }
                            
                        }catch {
                            print(error.localizedDescription, "StatusCode: \(response!)")
                        }
                        
                    }else{
                            DispatchQueue.main.async {
                                self.loaderStop()
                                let message = error!.localizedDescription
                                let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                                alertController.addAction(okAction)
                                self.present(alertController, animated: true, completion: nil)
                            }
                            
                        }
                }.resume()
            }
        }
    }
    
    func setDataToCreateTemplate() -> [String:Any]
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
//        var peopleDict : [String:Any] = ["ptwPeopleDtoList": [[String: Any]]()]
//        let jsaPeopleArray = JSAObject.peopleList
//        var newArray = [[String: Any]]()
//        for people in jsaPeopleArray
//        {
//            let dict : [String:Any] = [
//                "firstName":people.firstName as String,
//                "lastName":people.lastName as String,
//                "contactNumber":people.contactNumber as String,
//                "hasSignedJSA":1,
//                "hasSignedCWP":people.hasSignedCWP as Int,
//                "hasSignedHWP":people.hasSignedHWP as Int,
//                "hasSignedCSE":people.hasSignedCSE as Int
//            ]
//            newArray.append(dict)
//        }
//        peopleDict["ptwPeopleDtoList"] = newArray
        
        
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
        hazardDict["jsaStepsDto"] = newHazardArray
        
        
        //Stop the job list
        var stopDict : [String:Any] = ["jsaStopTriggerDtoList": [[String: Any]]()]
        let stopArray = JSAObject.stopTheJob
        var newStopArray = [[String: Any]]()
       // var newLocationArray = [[String: Any]]()
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
        
//        //location
//        for each  in JSAObject.location{
//            let dict : [String:Any] = [
//                "facilityOrSite": each.facilityOrSite,
//                "hierachyLevel": each.hierarchyLevel,
//                "facility": each.facility,
//                "muwi": each.muwi,
//                "serialNo" : each.serialNo
//            ]
//            newLocationArray.append(dict)
//        }
        
        //********************* permit parsing***********************
        
        
//        var permitHeaderArray = [[String:Any]]()
//        var permitRequiredDocumentsArray = [[String:Any]]()
//        var permitTestResultsArray = [[String:Any]]()
//        var permitApprovalArray = [[String:Any]]()
        
        // ********************************** COLD WORK ********************************
      //  if JSAObject.hasCWP == 1
//        {
//            let date = JSAObject.CWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
//
//            let time = JSAObject.CWP.header.startTime
//            let plannedDateTime = date + " " + time
//
//            var estimatedTimeOfCompletion = ""
//
//            if !JSAObject.CWP.header.estimatedTimeOfCompletion.isEmpty {
//
//                estimatedTimeOfCompletion = JSAObject.CWP.header.estimatedTimeOfCompletion
//            }
//
//            //header
//            let header = [
//                "isCWP":1 ,
//                "isHWP":0,
//                "isCSE":0,
//                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
//                "location":JSAObject.CWP.header.location,
//                "createdBy": JSAObject.CWP.header.permitHolder,
//                "contractorPerformingWork":JSAObject.CWP.header.contractorPerformingWork.utf8EncodedString(),
//                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
//                "equipmentID":JSAObject.CWP.header.equipmentID.utf8EncodedString(),
//                "workOrderNumber":JSAObject.CWP.header.workOrderNumber.utf8EncodedString(),
//                "status":"APPROVED"
//                ] as [String:Any]
//            permitHeaderArray.append(header)
//
//            //Required  Docs
//            let recDoc = [
//                "isCWP":1,
//                "isHWP":0,
//                "isCSE":0,
//                "atmosphericTestRecord":JSAObject.CWP.docs.atmosphericTest as Int,
//                "loto":JSAObject.CWP.docs.Loto as Int,
//                "procedure":JSAObject.CWP.docs.Procedure as Int,
//                "pAndIdOrDrawing":JSAObject.CWP.docs.PnID as Int,
//                "certificate":JSAObject.CWP.docs.certificate.utf8EncodedString() ,
//                "temporaryDefeat":JSAObject.CWP.docs.tempDefeat as Int,
//                "rescuePlan":JSAObject.CWP.docs.rescuePlan as Int,
//                "sds":JSAObject.CWP.docs.sds as Int,
//                "otherWorkPermitDocs":JSAObject.CWP.docs.otherText.utf8EncodedString() ,
//                "fireWatchChecklist":JSAObject.CWP.docs.fireWatch as Int,
//                "liftPlan":JSAObject.CWP.docs.liftPlan as Int,
//                "simopDeviation":JSAObject.CWP.docs.simop as Int,
//                "safeWorkPractice":JSAObject.CWP.docs.safeWorkPractice as Int
//                ] as [String:Any]
//            permitRequiredDocumentsArray.append(recDoc)
//
//            //Approval
//            let CWApproval = [
//                "permitNumber" : CWObject.header.permitNo,
//                "preJobWalkthroughBy": CWObject.signOff.walkthrough,
//                "isWorkSafeToPerform": CWObject.signOff.isSafeContinue,
//                "controlBoardDistribution": CWObject.signOff.controlBoard,
//                "worksiteDistribution": CWObject.signOff.worksite,
//                "otherDistribution": CWObject.signOff.others,
//                "approvalDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
//                "picName": CWObject.signOff.name,
//                "picDate": CWObject.signOff.dateTime.convertToDateToMilliseconds(),
//                "superitendentName": "",
//                "isHWP": 0,
//                "simopsDistribution": CWObject.signOff.SIMOPS,
//                "isCSE": 0,
//                "approvedBy": currentUser.name! as Any ,
//                "superitendentDate": "",
//                "isCWP": 1
//                ] as [String : Any]
//            permitApprovalArray.append(CWApproval)
//
//
//        }  // ********************************** HOT WORK ********************************
       // if JSAObject.hasHWP == 1
//        {
//            let date = JSAObject.HWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
//
//            let time = JSAObject.HWP.header.startTime
//            let plannedDateTime = date + " " + time
//
//            var estimatedTimeOfCompletion = ""
//
//            if !JSAObject.HWP.header.estimatedTimeOfCompletion.isEmpty {
//
//                estimatedTimeOfCompletion = JSAObject.HWP.header.estimatedTimeOfCompletion
//            }
//
//            //header
//            let header = [
//                "isCWP":0 ,
//                "isHWP":1,
//                "isCSE":0,
//                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
//                "location":JSAObject.HWP.header.location,
//                "createdBy": JSAObject.HWP.header.permitHolder,
//                "contractorPerformingWork":JSAObject.HWP.header.contractorPerformingWork.utf8EncodedString(),
//                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
//                "equipmentID":JSAObject.HWP.header.equipmentID.utf8EncodedString(),
//                "workOrderNumber":JSAObject.HWP.header.workOrderNumber.utf8EncodedString(),
//                "status":"SUBMITTED"
//                ] as [String:Any]
//            permitHeaderArray.append(header)
//
//            //Required  Docs
//            let recDoc = [
//                "isCWP":0,
//                "isHWP":1,
//                "isCSE":0,
//                "atmosphericTestRecord":JSAObject.HWP.docs.atmosphericTest as Int,
//                "loto":JSAObject.HWP.docs.Loto as Int,
//                "procedure":JSAObject.HWP.docs.Procedure as Int,
//                "pAndIdOrDrawing":JSAObject.HWP.docs.PnID as Int,
//                "certificate":JSAObject.HWP.docs.certificate.utf8EncodedString() ,
//                "temporaryDefeat":JSAObject.HWP.docs.tempDefeat as Int,
//                "rescuePlan":JSAObject.HWP.docs.rescuePlan as Int,
//                "sds":JSAObject.HWP.docs.sds as Int,
//                "otherWorkPermitDocs":JSAObject.HWP.docs.otherText.utf8EncodedString() ,
//                "fireWatchChecklist":JSAObject.HWP.docs.fireWatch as Int,
//                "liftPlan":JSAObject.HWP.docs.liftPlan as Int,
//                "simopDeviation":JSAObject.HWP.docs.simop as Int,
//                "safeWorkPractice":JSAObject.HWP.docs.safeWorkPractice as Int
//                ] as [String:Any]
//            permitRequiredDocumentsArray.append(recDoc)
//
//        } // ********************************** CSE WORK ********************************
        //if JSAObject.hasCSP == 1
//        {
//            let date = JSAObject.CSEP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
//
//            let time = JSAObject.CSEP.header.startTime
//            let plannedDateTime = date + " " + time
//
//            var estimatedTimeOfCompletion = ""
//
//            if !JSAObject.CSEP.header.estimatedTimeOfCompletion.isEmpty {
//
//                estimatedTimeOfCompletion = date //+ " " + JSAObject.CSEP.header.estimatedTimeOfCompletion
//            }
//
//            //header
//            let header = [
//                "isCWP":0 ,
//                "isHWP":0,
//                "isCSE":1,
//                "plannedDateTime": plannedDateTime.convertToDateToMilliseconds() ,
//                "location":JSAObject.CSEP.header.location,
//                "createdBy": JSAObject.CSEP.header.permitHolder,
//                "contractorPerformingWork":JSAObject.CSEP.header.contractorPerformingWork.utf8EncodedString(),
//                "estimatedTimeOfCompletion":estimatedTimeOfCompletion.convertToDateToMilliseconds(),
//                "equipmentID":JSAObject.CSEP.header.equipmentID.utf8EncodedString(),
//                "workOrderNumber":JSAObject.CSEP.header.workOrderNumber.utf8EncodedString(),
//                "status":"SUBMITTED"
//                ] as [String:Any]
//            permitHeaderArray.append(header)
//
//            //Required  Docs
//            let recDoc = [
//                "isCWP":0,
//                "isHWP":0,
//                "isCSE":1,
//                "atmosphericTestRecord":JSAObject.CSEP.docs.atmosphericTest as Int,
//                "loto":JSAObject.CSEP.docs.Loto as Int,
//                "procedure":JSAObject.CSEP.docs.Procedure as Int,
//                "pAndIdOrDrawing":JSAObject.CSEP.docs.PnID as Int,
//                "certificate":JSAObject.CSEP.docs.certificate.utf8EncodedString() ,
//                "temporaryDefeat":JSAObject.CSEP.docs.tempDefeat as Int,
//                "rescuePlan":JSAObject.CSEP.docs.rescuePlan as Int,
//                "sds":JSAObject.CSEP.docs.sds as Int,
//                "otherWorkPermitDocs":JSAObject.CSEP.docs.otherText.utf8EncodedString() ,
//                "fireWatchChecklist":JSAObject.CSEP.docs.fireWatch as Int,
//                "liftPlan":JSAObject.CSEP.docs.liftPlan as Int,
//                "simopDeviation":JSAObject.CSEP.docs.simop as Int,
//                "safeWorkPractice":JSAObject.CSEP.docs.safeWorkPractice as Int
//                ] as [String:Any]
//            permitRequiredDocumentsArray.append(recDoc)
//
//        }
        
        
        // final parameters
        
       // for val in JSAObject.testResult.preStartTests
//        {
//            let testResults = [
//                "isCWP":0,
//                "isHWP":0,
//                "isCSE":1,
//                "preStartOrWorkTest":"PRESTART",
//                "oxygenPercentage":val.O2,
//                "toxicType":val.toxicType.utf8EncodedString(),
//                "toxicResult":val.toxicResult,
//                "flammableGas":val.flammableGas.utf8EncodedString(),
//                "othersType":val.othersType.utf8EncodedString(),
//                "othersResult":val.othersResult,
//                "date":val.Date.convertToDateToMilliseconds(),//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!,
//                "time":val.Time
//                ] as [String:Any]
//            permitTestResultsArray.append(testResults)
//        }
        
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
            
//            "jsaReviewDto":[
//                "createdBy":JSAObject.createdBy,
//                "createdDate":JSAObject.createdDate,
//                "approvedBy":JSAObject.createdBy,
//                "approvedDate":JSAObject.createdDate,
//                "lastUpdatedBy":JSAObject.updatedBy,
//                "lastUpdatedDate":JSAObject.updatedDate
//            ],
            
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
            
          //  "ptwPeopleDtoList": newArray ,
            
            //13
            "jsaHazardsPressurizedDto":[
                "presurizedEquipment": JSAObject.hazardCategories.categories[0][0] as Int,
                "performIsolation":JSAObject.hazardCategories.categories[0][0] as Int,
                "depressurizeDrain":JSAObject.hazardCategories.categories[0][1] as Int,
                "relieveTrappedPressure":JSAObject.hazardCategories.categories[0][2] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[0][3] as Int,
                "anticipateResidual":JSAObject.hazardCategories.categories[0][4] as Int,
                "secureAllHoses":JSAObject.hazardCategories.categories[0][5] as Int
                ] as [String:Int],
            //18
            "jsaHazardsVisibilityDto":[
                "poorLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "provideAlternateLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "waitUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][1] as Int,
                "deferUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][2] as Int,
                "knowDistanceFromPoles":JSAObject.hazardCategories.categories[1][3] as Int
                ] as [String:Int],
            //12
            "jsaHazardsPersonnelDto":[
                "personnel":JSAObject.hazardCategories.categories[2][0] as Int,
                "performInduction":JSAObject.hazardCategories.categories[2][0] as Int,
                "mentorCoachSupervise":JSAObject.hazardCategories.categories[2][1] as Int,
                "verifyCompetencies":JSAObject.hazardCategories.categories[2][2] as Int,
                "addressLimitations":JSAObject.hazardCategories.categories[2][3] as Int,
                "manageLanguageBarriers":JSAObject.hazardCategories.categories[2][4] as Int,
                "wearSeatBelts":JSAObject.hazardCategories.categories[2][5] as Int
                ] as [String:Int],
            //21
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
            //14
            "jsaHazardsSimultaneousDto":
                [
                    "simultaneousOperations":JSAObject.hazardCategories.categories[4][0] as Int,
                    "followSimopsMatrix":JSAObject.hazardCategories.categories[4][0] as Int,
                    "mocRequiredFor":JSAObject.hazardCategories.categories[4][1] as Int,
                    "interfaceBetweenGroups":JSAObject.hazardCategories.categories[4][2] as Int,
                    "useBarriersAnd":JSAObject.hazardCategories.categories[4][3] as Int,
                    "havePermitSigned":JSAObject.hazardCategories.categories[4][4] as Int
                    ] as [String:Int],
            //7
            "jsaHazardsIgnitionDto":[
                "ignitionSources":JSAObject.hazardCategories.categories[5][0] as Int,
                "removeCombustibleMaterials":JSAObject.hazardCategories.categories[5][0] as Int,
                "provideFireWatch":JSAObject.hazardCategories.categories[5][1] as Int,
                "implementAbrasiveBlastingControls":JSAObject.hazardCategories.categories[5][2] as Int,
                "conductContinuousGasTesting":JSAObject.hazardCategories.categories[5][3] as Int,
                "earthForStaticElectricity":JSAObject.hazardCategories.categories[5][4] as Int
                ] as [String:Int],
            //16
            "jsaHazardsSubstancesDto":[
                "hazardousSubstances":JSAObject.hazardCategories.categories[6][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[6][0] as Int,
                "followSdsControls":JSAObject.hazardCategories.categories[6][1] as Int,
                "implementHealthHazardControls":JSAObject.hazardCategories.categories[6][2] as Int,
                "testMaterial":JSAObject.hazardCategories.categories[6][3] as Int
                ] as [String:Int],
            //15
            "jsaHazardsSpillsDto":[
                "potentialSpills":JSAObject.hazardCategories.categories[7][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[7][0] as Int,
                "connectionsInGoodCondition":JSAObject.hazardCategories.categories[7][1] as Int,
                "spillContainmentEquipment":JSAObject.hazardCategories.categories[7][2] as Int,
                "haveSpillCleanupMaterials":JSAObject.hazardCategories.categories[7][3] as Int,
                "restrainHosesWhenNotInUse":JSAObject.hazardCategories.categories[7][4] as Int
                ] as [String:Int],
            //20
            "jsaHazardsWeatherDto":[
                "weather":JSAObject.hazardCategories.categories[8][0] as Int,
                "controlsForSlipperySurface":JSAObject.hazardCategories.categories[8][0] as Int,
                "heatBreak":JSAObject.hazardCategories.categories[8][1] as Int,
                "coldHeaters":JSAObject.hazardCategories.categories[8][2] as Int,
                "lightning":JSAObject.hazardCategories.categories[8][3] as Int
                ] as [String:Int],
            //6
            "jsaHazardsHighNoiseDto":[
                "highNoise":JSAObject.hazardCategories.categories[9][0] as Int,
                "wearCorrectHearing":JSAObject.hazardCategories.categories[9][0] as Int,
                "manageExposureTimes":JSAObject.hazardCategories.categories[9][1] as Int,
                "shutDownEquipment":JSAObject.hazardCategories.categories[9][2] as Int,
                "useQuietTools":JSAObject.hazardCategories.categories[9][3] as Int,
                "soundBarriers":JSAObject.hazardCategories.categories[9][4] as Int,
                "provideSuitableComms":JSAObject.hazardCategories.categories[9][5] as Int
                ] as [String:Int],
            //1
            "jsaHazardsDroppedDto":[
                "droppedObjects":JSAObject.hazardCategories.categories[10][0] as Int,
                "markRestrictEntry":JSAObject.hazardCategories.categories[10][0] as Int,
                "useLiftingEquipmentToRaise":JSAObject.hazardCategories.categories[10][1] as Int,
                "secureTools":JSAObject.hazardCategories.categories[10][2] as Int
                ] as [String:Int],
            //8
            "jsaHazardsLiftingDto":[
                "liftingEquipment":JSAObject.hazardCategories.categories[11][0] as Int,
                "confirmEquipmentCondition":JSAObject.hazardCategories.categories[11][0] as Int,
                "obtainApprovalForLifts":JSAObject.hazardCategories.categories[11][1] as Int,
                "haveDocumentedLiftPlan":JSAObject.hazardCategories.categories[11][2] as Int
                ] as [String:Int],
            //5
            "jsaHazardsHeightsDto":[
                "workAtHeights":JSAObject.hazardCategories.categories[12][0] as Int,
                "discussWorkingPractice":JSAObject.hazardCategories.categories[12][0] as Int,
                "verifyFallRestraint":JSAObject.hazardCategories.categories[12][1] as Int,
                "useFullBodyHarness":JSAObject.hazardCategories.categories[12][2] as Int,
                "useLockTypeSnaphoooks":JSAObject.hazardCategories.categories[12][3] as Int
                ] as [String:Int],
            //2
            "jsaHazardsElectricalDto":[
                "portableElectricalEquipment":JSAObject.hazardCategories.categories[13][0] as Int,
                "inspectToolsForCondition":JSAObject.hazardCategories.categories[13][0] as Int,
                "implementGasTesting":JSAObject.hazardCategories.categories[13][1] as Int,
                "protectElectricalLeads":JSAObject.hazardCategories.categories[13][2] as Int,
                "identifyEquipClassification":JSAObject.hazardCategories.categories[13][3] as Int
                ] as [String:Int],
            //11
            "jsaHazardsMovingDto":[
                "movingEquipment":JSAObject.hazardCategories.categories[14][0] as Int,
                "confirmMachineryIntegrity":JSAObject.hazardCategories.categories[14][0] as Int,
                "provideProtectiveBarriers":JSAObject.hazardCategories.categories[14][1] as Int,
                "observerToMonitorProximityPeopleAndEquipment":JSAObject.hazardCategories.categories[14][2] as Int,
                "lockOutEquipment":JSAObject.hazardCategories.categories[14][3] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[14][4] as Int
                ] as [String:Int],
            //9
            "jsaHazardsManualDto":[
                "manualHandling":JSAObject.hazardCategories.categories[15][0] as Int,
                "assessManualTask":JSAObject.hazardCategories.categories[15][0] as Int,
                "limitLoadSize":JSAObject.hazardCategories.categories[15][1] as Int,
                "properLiftingTechnique":JSAObject.hazardCategories.categories[15][2] as Int,
                "confirmStabilityOfLoad":JSAObject.hazardCategories.categories[15][3] as Int,
                "getAssistanceOrAid":JSAObject.hazardCategories.categories[15][4] as Int
                ] as [String:Int],
            
            //17
            "jsaHazardsToolsDto":[
                "equipmentAndTools":JSAObject.hazardCategories.categories[16][0] as Int,
                "inspectEquipmentTool":JSAObject.hazardCategories.categories[16][0] as Int,
                "brassToolsNecessary":JSAObject.hazardCategories.categories[16][1] as Int,
                "useProtectiveGuards":JSAObject.hazardCategories.categories[16][2] as Int,
                "useCorrectTools":JSAObject.hazardCategories.categories[16][3] as Int,
                "checkForSharpEdges":JSAObject.hazardCategories.categories[16][4] as Int,
                "applyHandSafetyPrinciple":JSAObject.hazardCategories.categories[16][5] as Int
                ] as [String:Int],
            //4
            "jsaHazardsFallsDto":[
                "slipsTripsAndFalls":JSAObject.hazardCategories.categories[17][0] as Int,
                "identifyProjections":JSAObject.hazardCategories.categories[17][0] as Int,
                "flagHazards":JSAObject.hazardCategories.categories[17][1] as Int,
                "secureCables":JSAObject.hazardCategories.categories[17][2] as Int,
                "cleanUpLiquids":JSAObject.hazardCategories.categories[17][3] as Int,
                "barricadeHoles":JSAObject.hazardCategories.categories[17][4] as Int
                ] as [String:Int],
            //19
            "jsaHazardsVoltageDto":[
                "highVoltage":JSAObject.hazardCategories.categories[18][0] as Int,
                "restrictAccess":JSAObject.hazardCategories.categories[18][0] as Int,
                "dischargeEquipment":JSAObject.hazardCategories.categories[18][1] as Int,
                "observeSafeWorkDistance":JSAObject.hazardCategories.categories[18][2] as Int,
                "useFlashBurn":JSAObject.hazardCategories.categories[18][3] as Int,
                "useInsulatedGloves":JSAObject.hazardCategories.categories[18][4] as Int
                ] as [String:Int],
            //3
            "jsaHazardsExcavationdDto":[
                "excavations":JSAObject.hazardCategories.categories[19][0] as Int,
                "haveExcavationPlan":JSAObject.hazardCategories.categories[19][0] as Int,
                "locatePipesByHandDigging":JSAObject.hazardCategories.categories[19][1] as Int,
                "deEnergizeUnderground":JSAObject.hazardCategories.categories[19][2] as Int,
                "cseControls":JSAObject.hazardCategories.categories[19][3] as Int
                ] as [String:Int],
            //10
            "jsaHazardsMobileDto":[
                "mobileEquipment":JSAObject.hazardCategories.categories[20][0] as Int,
                "assessEquipmentCondition":JSAObject.hazardCategories.categories[20][0] as Int,
                "controlAccess":JSAObject.hazardCategories.categories[20][1] as Int,
                "monitorProximity":JSAObject.hazardCategories.categories[20][2] as Int,
                "manageOverheadHazards":JSAObject.hazardCategories.categories[20][3] as Int,
                "adhereToRules":JSAObject.hazardCategories.categories[20][4] as Int
                ] as [String:Int],
            
            //22 renamed
            "jsaStepsDto": newHazardArray ,
            //23 renmaed
            "jsaStopTriggerDto": newStopArray,
            
            //"ptwHeaderDtoList":permitHeaderArray,
            
            //"ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            
            //"ptwApprovalDtoList": permitApprovalArray,
            
            //"ptwCloseOutDtoList":[],
            
//            "ptwTestRecordDto":permitAtmosphericTestArray,
//
//            "ptwTestResultsDtoList":permitTestResultsArray,
            
//            "ptwTestRecordDto":[
//                "isCWP":0,
//                "isHWP":0,
//                "isCSE":0,
//                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
//                "dateOfLastCalibration": JSAObject.atmosphericTesting.dateOfLastCallibration.convertToDateToMilliseconds(),
//                "testingFrequency":JSAObject.atmosphericTesting.testFrequency.utf8EncodedString() as String,
//                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
//                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
//                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
//                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
//                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
//                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
//                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
//                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
//                "isO2":JSAObject.atmosphericTesting.O2 as Int,
//                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
//                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
//                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String
//                ] as [String:Any],
            
            //"ptwTestResultsDtoList":permitTestResultsArray,
            
//            "ptwCwpWorkTypeDto":[
//                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
//                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
//                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
//                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
//                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
//                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
//                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
//                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
//                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
//                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
//                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
//                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
//                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
//                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String
//                ] as [String:Any],
//            "ptwHwpWorkTypeDto":[
//                "cutting":JSAObject.HWP.workTypeHW.cutting as Int,
//                "wielding":JSAObject.HWP.workTypeHW.welding as Int,
//                "electricalPoweredEquipment":JSAObject.HWP.workTypeHW.electricalPoweredEquipment as Int,
//                "grinding":JSAObject.HWP.workTypeHW.grinding as Int,
//                "abrasiveBlasting":JSAObject.HWP.workTypeHW.abrasiveBlasting as Int,
//                "otherTypeOfWork":JSAObject.HWP.workTypeHW.otherText.utf8EncodedString() as String,
//                "descriptionOfWorkToBePerformed":JSAObject.HWP.workTypeHW.descriptionOfWork.utf8EncodedString() as String
//                ] as [String:Any],
//            "ptwCseWorkTypeDto":[
//                "tank":JSAObject.CSEP.workTypeCSE.tank as Int,
//                "vessel":JSAObject.CSEP.workTypeCSE.vessel as Int,
//                "excavation": JSAObject.CSEP.workTypeCSE.excavation as Int,
//                "pit":JSAObject.CSEP.workTypeCSE.pit as Int,
//                "tower":JSAObject.CSEP.workTypeCSE.tower as Int,
//                "other":JSAObject.CSEP.workTypeCSE.other.utf8EncodedString() as String,
//                "reasonForCSE":JSAObject.CSEP.workTypeCSE.reasonForCSE.utf8EncodedString() as String
//                ] as [String:Any],
            //"jsaLocationDtoList" : newLocationArray,
            "name" : JSAObject.templateName
        ]
        
        
        print("final dictionary - \(finalDict)")
        return finalDict
        
    }
    
    
}
