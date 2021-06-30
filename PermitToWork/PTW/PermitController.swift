//
//  PermitController.swift
// 
//
//  Created by Soumya Singh on 23/03/18.
//  Copyright © 2018 soumyaIncture Technologies. All rights reserved.
//

import UIKit

class PermitController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet var permitForm: UITableView!
    var selectedCWPermit = ""
    var permitType = ""
    let context = PTWCoreData.shared.managedObjectContext
    var isPermitReview = false
    
    @IBOutlet weak var navigationTitleText: UINavigationItem!
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    var permitStr = UserDefaults.standard.string(forKey: "Permit")
    
    // For CWP and HWP values are same
    var coldPermitValues = ["Planned Date of Work","Start Time","Location","Permit Holder","Contractor Performing Work","Estimated Time of Completion","Equipment ID/Tag","Work Order No. (if applicable)"]
    
    var confinedPermitValues = ["Planned Date of Work","Start Time","Permit Holder","Date","Contractor Performing Work","Estimated Time of Completion","Equipment ID/Tag","Work Order No. (if applicable)"]
    
    var hotPermit = HotPermit()
    var coldPermit = ColdPermit()
    var confineSpacePermit = ConfinedSpacePermit()
    
    private var startTime: Date?
    private var estimatedTimeOfWork: Date?
    private var plannedDateTime: Date?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        self.registerKeyBoardNotification()
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
        
        if selectedCWPermit == ""
        {
            
        }
        else
        {
            if ConnectionCheck.isConnectedToNetwork(){
                readJSA()
            }
            else{
                
                let jsaDetailService = JSADetailModelService(context: self.context)
                let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedCWPermit)!))
                let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
                if jsaDetail.count>0{
                    JSAObject = jsaDetail[0].getJSA()
                }
                JSAObject.addPeopleIndex = nil
                let permitDetailService = PermitDetailModelService(context: self.context)
                let permitDetail = permitDetailService.get(withPredicate: searchPredicate)
                if permitDetail.count>0{
                    for each in permitDetail{
                        if permitType == "HOT" && each.permitType == PermitType.HWP.rawValue{
                            JSAObject.currentFlow = .HWP
                            JSAObject.HWP = each.getPermitData().HWP
                        }
                        else if permitType == "COLD" && each.permitType == PermitType.CWP.rawValue{
                            JSAObject.currentFlow = .CWP
                            JSAObject.CWP = each.getPermitData().CWP
                        }
                        else if permitType == "CSE" && each.permitType == PermitType.CSEP.rawValue{
                            JSAObject.currentFlow = .CSEP
                            JSAObject.CSEP = each.getPermitData().CSEP
                        }
                    }
                    
                }
                DispatchQueue.main.async {
                   
                    self.permitForm.reloadData()
                    
                }
            }
            
        }

        let nib = UINib(nibName: "PermitCell", bundle: nil)
        permitForm.register(nib, forCellReuseIdentifier: "PermitCell")
        permitForm.tableFooterView = UIView()
        // Do any additional setup after loading the view.
    }
    
    func createNavBar(){
        
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0) 
        
        if JSAObject.currentFlow == .CWP
        {
            navigationTitleText.title = "Cold Work Permit"
//            var title = "Cold Work Permit"
//            if readCWObject.TOPTWHEADERValue.indices.contains(0) {
//                title = title + " " + readCWObject.TOPTWHEADERValue[0].ptwPermitNumber
//            }
//            navigationTitleText.title = title
        }
        else if JSAObject.currentFlow == .HWP
        {
            navigationTitleText.title = "Hot Work Permit"
        }
        else
        {
            navigationTitleText.title = "Confined Space Permit"
        }
        self.navigationItem.hidesBackButton = true
        
//        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
//        navigationItem.leftBarButtonItem = backItem
    }
    
    deinit {
        self.unregisterKeyBoardNotification()
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    
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
        DispatchQueue.main.async {
            self.indicator.stopAnimating()
        }
    }
    
    @IBAction func onNextPress(_ sender: UIButton) {
        
        var header: PermitHeader = JSAObject.CWP.header
        if JSAObject.currentFlow == .HWP {
            header = JSAObject.HWP.header
        }
        else if JSAObject.currentFlow == .CSEP{
            header = JSAObject.CSEP.header
        }
       var validation = self.validateFields(header: header)
       //header.plannedDateTime = header.plannedDateTime.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .yearMonthDateHypenSeparator, shouldConvertToUTC: true)!
    
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
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    @IBAction func onCancelPress(_ sender: UIButton)
    {
        if JSAObject.currentFlow == .HWP && JSAObject.isCWP.boolValue{
            JSAObject.currentFlow = .CWP
        }
        else if JSAObject.currentFlow == .CSEP && JSAObject.isHWP.boolValue{
            JSAObject.currentFlow = .HWP
        }
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onBackPress(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onHomePress(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        var header = JSAObject.CWP.header
        
        if JSAObject.currentFlow == .HWP {
            header = JSAObject.HWP.header
        }
        else if JSAObject.currentFlow == .CSEP {
            header = JSAObject.CSEP.header
        }
        
        
        if textField.tag == 0{
            if plannedDateTime != nil{
            let date = self.plannedDateTime!.toDateFormat(.yearMonthDateHypenSeparator)
                 header.plannedDateTime = date
//            header.plannedDateTime = date.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .yearMonthDateHypenSeparator, shouldConvertToUTC: true)!
            }
        }
        else if textField.tag == 1{
            if self.startTime != nil{
                let date = self.startTime!.toDateFormat(.hourMinuteSeconds)
                header.startTime = date.convertToDateString(format: .hourMinuteSeconds, currentDateStringFormat: .hourMinuteSeconds, shouldConvertToUTC: true)!
            }
            
        }
            //        else if textField.tag == 3{
            //            permitHeader.location = textField.text ?? ""
            //        }
        else if textField.tag == 3{
            //                JSAObject.CWP.header.permitHolder = textField.text ?? ""
        }
            //            else if textField.tag == 5{
            //                JSAObject.CWP.header.DateValue = textField.text ?? ""
            //            }
        else if textField.tag == 4{
            header.contractorPerformingWork = textField.text ?? ""
        }
        else if textField.tag == 5{
            if self.estimatedTimeOfWork != nil{
                let date = self.estimatedTimeOfWork!.toDateFormat(.long)
                header.estimatedTimeOfCompletion = date.convertToDateString(format: .long, currentDateStringFormat: .long, shouldConvertToUTC: true)!
            }
        }
        else if textField.tag == 6{
            header.equipmentID = textField.text ?? ""
        }
        else if textField.tag == 7{
            header.workOrderNumber = textField.text ?? ""
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
//    func getFormattedTime(date: Date?) -> String {
//        
//        guard let value = date else { return "" }
//        let formattedValue = value.hour!.description + ":" + value.minutes!.description + ":" + value.seconds!.description
//        return formattedValue
//        
//    }
}

extension PermitController: UITableViewDataSource, UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if JSAObject.currentFlow == .CWP || JSAObject.currentFlow == .HWP
        {
            return coldPermitValues.count
        }
        else
        {
            return confinedPermitValues.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        cell.clipsToBounds = true
        cell.valueTextField.tag = indexPath.row
        cell.valueTextField.delegate = self
        var permitHeder = JSAObject.CWP.header
        if JSAObject.currentFlow == .CWP {
            permitHeder = JSAObject.CWP.header
            if isPermitReview || JSAObject.CWP.header.status.lowercased() == "approved"
            {
                cell.isUserInteractionEnabled = false
            }
            else
            {
                cell.isUserInteractionEnabled = true
            }
        }
        else if JSAObject.currentFlow == .HWP {
            permitHeder = JSAObject.HWP.header
            if isPermitReview || JSAObject.HWP.header.status.lowercased() == "approved"
            {
                cell.isUserInteractionEnabled = false
            }
            else
            {
                cell.isUserInteractionEnabled = true
            }
        }
        else if JSAObject.currentFlow == .CSEP {
            permitHeder = JSAObject.CSEP.header
            if isPermitReview || JSAObject.CSEP.header.status.lowercased() == "approved"
            {
                cell.isUserInteractionEnabled = false
            }
            else
            {
                cell.isUserInteractionEnabled = true
            }
        }
        
        
        self.displayData(forCell: cell, indexPath: indexPath, header: permitHeder)
        cell.selectionStyle = .none
         if currentUser.isReadOnly == true{
            cell.isUserInteractionEnabled = false
        }
       
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 55
    }
    
    
    private func displayData(forCell cell: PermitCell, indexPath: IndexPath , header: PermitHeader) {
        
        
            cell.valueTextField.isDatePicker = false
            //            if indexPath.row == 0{
            //                cell.isUserInteractionEnabled = false
            //                cell.setData(key: coldPermitValues[indexPath.row], value: "\(JSAObject.CWP.header.permitNo)")
            //            }
            if indexPath.row == 0 {
                //                cell.isUserInteractionEnabled = true
                cell.valueTextField.isDatePicker = true
                cell.valueTextField.datePickerMode = .date
               // cell.valueTextField.datePicker.minimumDate = Date()
                let time = header.plannedDateTime.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .yearMonthDateHypenSeparator, shouldConvertFromUTC: true)
               
                if time != nil{
                    cell.setData(key: coldPermitValues[indexPath.row], value: String(describing: time!))
                }
                else{
                    cell.setData(key: coldPermitValues[indexPath.row], value: "")
                }
            }
            else if indexPath.row == 1 {
                //                cell.isUserInteractionEnabled = true
                cell.valueTextField.isDatePicker = true
                cell.valueTextField.datePickerMode = .time
                let time = header.startTime.convertToDateString(format: .short, currentDateStringFormat: .hourMinuteSeconds, shouldConvertFromUTC: true)
                if time != nil{
                    cell.setData(key: coldPermitValues[indexPath.row], value: String(describing: time!))
                }
                else{
                    cell.setData(key: coldPermitValues[indexPath.row], value: "")
                }
            }
            else if indexPath.row == 2 {
                cell.isUserInteractionEnabled = false
                var locations = ""
                if header.location == ""
                {
                    locations = JSAObject.location.map({$0.facilityOrSite}).joined(separator: ", ")
                    header.location = locations
                }
                else
                {
                    locations = header.location
                }
                
                cell.setData(key: coldPermitValues[indexPath.row], value: locations)
            }
            else if indexPath.row == 3 {
                cell.isUserInteractionEnabled = false
                var name = header.permitHolder
                if name.isEmpty {
                    name = currentUser.name ?? ""
                    header.permitHolder = name
                }
                cell.setData(key: coldPermitValues[indexPath.row], value: name)
            }
                //            else if indexPath.row == 4 {
                //                cell.isUserInteractionEnabled = true
                //                cell.setData(key: coldPermitValues[indexPath.row], value: JSAObject.CWP.header.DateValue)
                //            }
            else if indexPath.row == 4 {
                //                cell.isUserInteractionEnabled = true
                cell.setData(key: coldPermitValues[indexPath.row], value: header.contractorPerformingWork)
            }
            else if indexPath.row ==  5 {
                cell.valueTextField.isDatePicker = true
                cell.valueTextField.datePickerMode = .time
                //                cell.isUserInteractionEnabled = true
                var value = header.estimatedTimeOfCompletion
                if !value.isEmpty, let estimatedTimeOfCompletion = value.convertToDateString(format: .short, currentDateStringFormat: .long, shouldConvertFromUTC: true){//value.convertToDateString(format: .short, currentDateStringFormat: .long){
                    value = String(describing: estimatedTimeOfCompletion)
                }
                
                cell.setData(key: coldPermitValues[indexPath.row], value: value)
            }
            else if indexPath.row == 6 {
                //                cell.isUserInteractionEnabled = true
                cell.setData(key: coldPermitValues[indexPath.row], value: header.equipmentID)
            }
            else {
                //                cell.isUserInteractionEnabled = true
                cell.setData(key: coldPermitValues[indexPath.row], value: header.workOrderNumber)
            }
    
        
    }
    
}

extension PermitController{
    
    // MARK : Read Object parsing

    func readPermit(permitType: String) {
        //loaderStart()

        
        //"https://ptwb0ot37y8l6.hana.ondemand.com/ptw/permitdetails/get?permitNumber=135&permitType=COLD"
        let urlString : String =  IMOEndpoints.getPermitDetails + "permitNumber=\(selectedCWPermit)&permitType=\(permitType)"
       // let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/GetPermitDetails.xsjs?permitNumber=\(selectedCWPermit)&permitType=\(permitType)"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            self.loaderStop()

            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    if let val = JSON as? NSDictionary {
                        if let jsonDict = val.object(forKey: "data") as? NSDictionary
                        {
                            readCWObject = ReadPermit.init(JSON: jsonDict)
                            if self.permitType == "COLD"
                            {
                                self.readModelToHWPModel()
                            }
                            else if self.permitType == "HOT"
                            {
                                self.readModelToHWPModel()
                            }
                            else if self.permitType == "CSE"
                            {
                                self.readModelToHWPModel()
                            }
                            
                        }
                        
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    DispatchQueue.main.async {
                        //self.loaderStop()
                        let message = error!.localizedDescription
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    
                }
        }.resume()
    }
    
    
    func readModelToHWPModel(){
        // Cold Work Header Values
        CWObject.header.permitNo = readCWObject.TOPTWHEADERValue[0].permitNumber
        if readCWObject.TOPTWHEADERValue[0].plannedDateTime != ""
        {
            let date = readCWObject.TOPTWHEADERValue[0].plannedDateTime.convertToDate(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .long, shouldConvertFromUTC: true)
            let time = date?.localTime
            let plannedDate = date?.toDateFormat(.monthDateYearHiphenSeparator)
            
//            CWObject.header.plannedDateTime = plannedDate!
//            CWObject.header.startTime = time!
            
                        CWObject.header.plannedDateTime = readCWObject.TOPTWHEADERValue[0].plannedDateTime.components(separatedBy: " ")[0]
                        CWObject.header.startTime = readCWObject.TOPTWHEADERValue[0].plannedDateTime.components(separatedBy: " ")[1]
            
            
        }
        
        CWObject.header.location = readCWObject.TOPTWHEADERValue[0].location
        
        if CWObject.header.location == ""
        {
            CWObject.header.location = currentLocation.facilityOrSite
        }
        
        CWObject.header.permitHolder = readCWObject.TOPTWHEADERValue[0].createdBy
        CWObject.header.contractorPerformingWork = readCWObject.TOPTWHEADERValue[0].contractorPerformingWork.utf8DecodedString()
        CWObject.header.estimatedTimeOfCompletion = readCWObject.TOPTWHEADERValue[0].estimatedTimeOfCompletion
        CWObject.header.equipmentID = readCWObject.TOPTWHEADERValue[0].equipmentID.utf8DecodedString()
        CWObject.header.workOrderNumber = readCWObject.TOPTWHEADERValue[0].workOrderNumber
        CWObject.header.status = readCWObject.TOPTWHEADERValue[0].status
        
        // Cold Work Type
        CWObject.workTypeCW.criticalLift = readCWObject.TOPTWCWPWORKValue.criticalOrComplexLift
        CWObject.workTypeCW.Crane = readCWObject.TOPTWCWPWORKValue.craneOrLiftingDevice
        CWObject.workTypeCW.groundDist = readCWObject.TOPTWCWPWORKValue.groundDisturbanceOrExcavation
        CWObject.workTypeCW.handlingChem = readCWObject.TOPTWCWPWORKValue.handlingHazardousChemicals
        CWObject.workTypeCW.workAtHeight = readCWObject.TOPTWCWPWORKValue.workingAtHeight
        CWObject.workTypeCW.paintBlast = readCWObject.TOPTWCWPWORKValue.paintingOrBlasting
        CWObject.workTypeCW.workOnPressurizedSystems = readCWObject.TOPTWCWPWORKValue.workingOnPressurizedSystems
        CWObject.workTypeCW.erectingScaffolding = readCWObject.TOPTWCWPWORKValue.erectingOrDismantlingScaffolding
        CWObject.workTypeCW.breakingContainment = readCWObject.TOPTWCWPWORKValue.breakingContainmentOfClosedOperatingSystem
        CWObject.workTypeCW.closeProximity = readCWObject.TOPTWCWPWORKValue.workingInCloseToHazardousEnergy
        CWObject.workTypeCW.removalOfIdleEquip = readCWObject.TOPTWCWPWORKValue.removalOfIdleEquipmentForRepair
        CWObject.workTypeCW.higherRisk = readCWObject.TOPTWCWPWORKValue.higherRiskElectricalWork
        CWObject.workTypeCW.descriptionOfWork = readCWObject.TOPTWCWPWORKValue.descriptionOfWorkToBePerformed.utf8DecodedString()
        CWObject.workTypeCW.otherText = readCWObject.TOPTWCWPWORKValue.otherTypeOfWork.utf8DecodedString()
        
        // Hot Work Type
        
        CWObject.workTypeHW.cutting = readCWObject.TOPTWHWPWORKValue.cutting
        CWObject.workTypeHW.grinding = readCWObject.TOPTWHWPWORKValue.grinding
        CWObject.workTypeHW.welding = readCWObject.TOPTWHWPWORKValue.welding
        CWObject.workTypeHW.abrasiveBlasting = readCWObject.TOPTWHWPWORKValue.abrasiveBlasting
        CWObject.workTypeHW.electricalPoweredEquipment = readCWObject.TOPTWHWPWORKValue.electricalPoweredEquipment
        CWObject.workTypeHW.descriptionOfWork = readCWObject.TOPTWHWPWORKValue.descriptionOfWork.utf8DecodedString()
        CWObject.workTypeHW.otherText = readCWObject.TOPTWHWPWORKValue.otherText.utf8DecodedString()
        
        //Confined Space Work Type
        
        CWObject.workTypeCSE.tank = readCWObject.TOPTWCSEWORKValue.tank
        CWObject.workTypeCSE.vessel = readCWObject.TOPTWCSEWORKValue.vessel
        CWObject.workTypeCSE.excavation = readCWObject.TOPTWCSEWORKValue.excavation
        CWObject.workTypeCSE.pit = readCWObject.TOPTWCSEWORKValue.pit
        CWObject.workTypeCSE.tower = readCWObject.TOPTWCSEWORKValue.tower
        CWObject.workTypeCSE.other = readCWObject.TOPTWCSEWORKValue.other.utf8DecodedString()
        CWObject.workTypeCSE.reasonForCSE = readCWObject.TOPTWCSEWORKValue.reasonForCSE.utf8DecodedString()
        
        
        // required docs
        CWObject.docs.serialNo = readCWObject.TOPTWREQDOCValue[0].serialNo
        CWObject.docs.atmosphericTest = readCWObject.TOPTWREQDOCValue[0].atmosphericTestRecord
        CWObject.docs.Loto = readCWObject.TOPTWREQDOCValue[0].loto
        CWObject.docs.Procedure = readCWObject.TOPTWREQDOCValue[0].procedure
        CWObject.docs.PnID = readCWObject.TOPTWREQDOCValue[0].pAndIdOrDrawing
        CWObject.docs.tempDefeat = readCWObject.TOPTWREQDOCValue[0].temporaryDefeat
        //CWObject.docs.JSA = readCWObject.TOPTWREQDOCValue[0].JSA
        CWObject.docs.rescuePlan = readCWObject.TOPTWREQDOCValue[0].rescuePlan
        CWObject.docs.otherText = readCWObject.TOPTWREQDOCValue[0].otherWorkPermitDocs.utf8DecodedString()
        CWObject.docs.sds = readCWObject.TOPTWREQDOCValue[0].sds
        CWObject.docs.fireWatch = readCWObject.TOPTWREQDOCValue[0].fireWatchChecklist
        CWObject.docs.liftPlan = readCWObject.TOPTWREQDOCValue[0].liftPlan
        CWObject.docs.simop = readCWObject.TOPTWREQDOCValue[0].simopDeviation
        CWObject.docs.safeWorkPractice = readCWObject.TOPTWREQDOCValue[0].safeWorkPractice
        CWObject.docs.certificate = readCWObject.TOPTWREQDOCValue[0].certificate.utf8DecodedString()
        
        //atmospheric test
        JSAObject.atmosphericTesting.serialNo = readCWObject.TOPTWTESTRECValue.serialNo
        JSAObject.atmosphericTesting.areaOrEquipmentTotest = readCWObject.TOPTWTESTRECValue.areaToBeTested.utf8DecodedString()
        JSAObject.atmosphericTesting.detectorUsed = readCWObject.TOPTWTESTRECValue.detectorUsed.utf8DecodedString()
        JSAObject.atmosphericTesting.dateOfLastCallibration = readCWObject.TOPTWTESTRECValue.DateOfLastCalibration
        JSAObject.atmosphericTesting.O2 = readCWObject.TOPTWTESTRECValue.isO2
        JSAObject.atmosphericTesting.Lels = readCWObject.TOPTWTESTRECValue.isLELS
        JSAObject.atmosphericTesting.H2S = readCWObject.TOPTWTESTRECValue.isH2S
        JSAObject.atmosphericTesting.Other = readCWObject.TOPTWTESTRECValue.Other.utf8DecodedString()
        JSAObject.atmosphericTesting.priorToWorkCommencing = readCWObject.TOPTWTESTRECValue.priorToWorkCommencing
        JSAObject.atmosphericTesting.eachWorkPeriod = readCWObject.TOPTWTESTRECValue.eachWorkPeriod
        JSAObject.atmosphericTesting.noHours = readCWObject.TOPTWTESTRECValue.everyHour
        JSAObject.atmosphericTesting.continuousMonitoringreqd = readCWObject.TOPTWTESTRECValue.continuousGasMonitoring
        JSAObject.atmosphericTesting.testFrequency = readCWObject.TOPTWTESTRECValue.testingFrequency.utf8DecodedString()
        
        
        //test results
        JSAObject.testResult.Name = readCWObject.TOPTWTESTRECValue.gasTester.utf8DecodedString()
        JSAObject.testResult.specialPrecaution = readCWObject.TOPTWTESTRECValue.gasTesterComments.utf8DecodedString()
        JSAObject.testResult.serialNumber = readCWObject.TOPTWTESTRECValue.deviceSerialNo.utf8DecodedString()
        
        for val in readCWObject.TOPTWTESTRESValue
        {
            let test = TestsModel()
            test.O2 = val.oxygenPercentage
            test.toxicType = val.toxicType.utf8DecodedString()
            test.toxicResult = val.toxicResult
            test.flammableGas = val.flammableGas.utf8DecodedString()
            test.othersType = val.othersType.utf8DecodedString()
            test.othersResult = val.othersResult
            test.Date = val.date
            test.Time = val.time
            
            if val.preStartOrWorkTest == "PRESTART"
            {
                JSAObject.testResult.preStartTests.append(test)
            }
            else if val.preStartOrWorkTest == "WORKPERIOD"
            {
                JSAObject.testResult.workPeriodTests.append(test)
            }
        }
        
        // people
        var peopleList = [People]()
        for val in readCWObject.TOPTWPEOPLEArrayValue
        {
            
            let people = People()
            people.firstName = val.firstName
            people.lastName = val.lastName
            people.contactNumber = val.contactNumber
            people.hasSignedCSE = val.hasSignedCSE
            people.hasSignedJSA = val.hasSignedJSA
            people.hasSignedCWP = val.hasSignedCWP
            people.hasSignedHWP = val.hasSignedHWP
            peopleList.append(people)
            
        }
        CWObject.peopleList = peopleList
        JSAObject.peopleList = peopleList
        
        let jsaDetailService = JSADetailModelService(context: self.context)
        let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedCWPermit)!))
        let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
        if jsaDetail.count>0{
            jsaDetailService.delete(id: jsaDetail[0].objectID)
        }
        
        _ = jsaDetailService.create(jsa: JSAObject)
        jsaDetailService.saveChanges()
        
        let permitDetailService = PermitDetailModelService(context: self.context)
        let permitDetail = permitDetailService.get(withPredicate: searchPredicate)
        if permitDetail.count>0{
            for each in permitDetail{
                if permitType == "HOT" && each.permitType == PermitType.HWP.rawValue{
                    permitDetailService.delete(id: each.objectID)
                }
                else if permitType == "COLD" && each.permitType == PermitType.CWP.rawValue{
                    permitDetailService.delete(id: each.objectID)
                }
                else if permitType == "CSE" && each.permitType == PermitType.CSEP.rawValue{
                    permitDetailService.delete(id: each.objectID)
                }
            }
        }
        var permitTypeDB = ""
        if permitType == "HOT"
        {
            JSAObject.HWP = CWObject
            JSAObject.hasHWP = 1
            permitTypeDB = PermitType.HWP.rawValue
        }
        else if permitType == "COLD"
        {
            JSAObject.CWP = CWObject
            JSAObject.hasCWP = 1
            permitTypeDB = PermitType.CWP.rawValue
        }
        else if permitType == "CSE"
        {
            JSAObject.CSEP = CWObject
            JSAObject.hasCSP = 1
            permitTypeDB = PermitType.CSEP.rawValue
        }
        
        _  = permitDetailService.create(detailData: JSAObject, permitNumber: JSAObject.permitNumber, permitType: permitTypeDB)
        permitDetailService.saveChanges()
        DispatchQueue.main.async {
            self.createNavBar()
            self.loaderStop()
            self.permitForm.reloadData()
        }
    }
    
    //MARK: - Read JSA methods
    
    func readJSA(){
        DispatchQueue.main.async {
            self.loaderStart()
        }
        //let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/JSA_Details_By_PermitNumber.xsjs?permitNumber=\(selectedCWPermit)"
        
        
        let urlString : String = IMOEndpoints.getJSAByPermitNumber + "permitNumber=\(selectedCWPermit)"
        
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            self.loaderStop()

            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    
                    
                   /* if let jsonDict2 = JSON as? NSDictionary {
                       
                        var jsonDict = [NSDictionary]()
                        jsonDict = (jsonDict2["data"]  as? [NSDictionary] ?? [])*/
                    
                    
                    if let jsonDict = JSON as? NSDictionary {
                        
                        readJSAObject = ReadJSA(JSON:jsonDict.value(forKey: "data") as? NSDictionary ?? NSDictionary())
                        self.readModelToJSAModel()
                        DispatchQueue.main.async {
                           // self.loaderStop()
                        }
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    DispatchQueue.main.async {
                       // self.loaderStop()
                        let message = error!.localizedDescription
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    
                }
        }.resume()
    }
    
    func readModelToJSAModel(){
        // Header Values
        JSAObject.createdBy = (readJSAObject.TOJSAREVIEWValue?.createdBy)!
        JSAObject.createdDate = (readJSAObject.TOJSAREVIEWValue?.createdDate)!
        JSAObject.updatedDate = (readJSAObject.TOJSAREVIEWValue?.lastUpdatedDate)!
        JSAObject.updatedBy = (readJSAObject.TOJSAREVIEWValue?.lastUpdatedBy)!
        JSAObject.approvedBy = (readJSAObject.TOJSAREVIEWValue?.approvedBy)!
        JSAObject.approvedDate = (readJSAObject.TOJSAREVIEWValue?.approvedDate)!
        JSAObject.hasCSP = readJSAObject.TOJSAHEADERValue!.hasCSE
        JSAObject.hasCWP = readJSAObject.TOJSAHEADERValue!.hasCWP
        JSAObject.hasHWP = readJSAObject.TOJSAHEADERValue!.hasHWP
        JSAObject.status = readJSAObject.TOJSAHEADERValue!.status
        JSAObject.permitNumber = readJSAObject.TOJSAHEADERValue!.permitNumber
        JSAObject.jsaPermitNumber = readJSAObject.TOJSAHEADERValue!.jsapermitNumber
        
        // create JSA
        
        JSAObject.createJSA.injuryDescription = readJSAObject.TOJSAHEADERValue!.identifyMostSeriousPotentialInjury.utf8DecodedString()
        JSAObject.createJSA.taskDescription = readJSAObject.TOJSAHEADERValue!.taskDescription.utf8DecodedString()
        JSAObject.createJSA.jsaPermit = readJSAObject.TOJSAHEADERValue!.jsapermitNumber
        //JSAObject.createJSA.permitNo = readJSAObject.TOJSAHEADERValue!.permitNumber
        // JSAObject.createJSA.facility = readJSAObject.TOJSALOCATIONValue!.facility
        
        // Risk Assesment
        
        JSAObject.riskAssesment.mustExistingWork = (readJSAObject.TOJSARISKASSValue?.mustModifyExistingWorkPractice)!
        JSAObject.riskAssesment.afterMitigation = (readJSAObject.TOJSARISKASSValue?.hasContinuedRisk)!
        JSAObject.riskAssesment.hardHat = (readJSAObject.TOJSE_PPEValue?.hardHat)!
        JSAObject.riskAssesment.safetyShoes = (readJSAObject.TOJSE_PPEValue?.safetyBoot)!
        JSAObject.riskAssesment.single = (readJSAObject.TOJSE_PPEValue?.singleEar)!
        JSAObject.riskAssesment.double = (readJSAObject.TOJSE_PPEValue?.doubleEars)!
        JSAObject.riskAssesment.SCBA = (readJSAObject.TOJSE_PPEValue?.needSCBA)!
        JSAObject.riskAssesment.dustMask = (readJSAObject.TOJSE_PPEValue?.needDustMask)!
        JSAObject.riskAssesment.fallProtection = (readJSAObject.TOJSE_PPEValue?.fallProtection)!
        JSAObject.riskAssesment.fallRestraint = (readJSAObject.TOJSE_PPEValue?.fallRestraint)!
        JSAObject.riskAssesment.flameResistantClothing = (readJSAObject.TOJSE_PPEValue?.flameResistantClothing)!
        JSAObject.riskAssesment.safetyGlasses = (readJSAObject.TOJSE_PPEValue?.safetyGlasses)!
        JSAObject.riskAssesment.faceShield = (readJSAObject.TOJSE_PPEValue?.faceShield)!
        JSAObject.riskAssesment.goggles = (readJSAObject.TOJSE_PPEValue?.goggles)!
        JSAObject.riskAssesment.cotton = (readJSAObject.TOJSE_PPEValue?.cottonGlove)!
        JSAObject.riskAssesment.leather = (readJSAObject.TOJSE_PPEValue?.leatherGlove)!
        JSAObject.riskAssesment.impactProtection = (readJSAObject.TOJSE_PPEValue?.impactProtection)!
        JSAObject.riskAssesment.chemicalSuit = (readJSAObject.TOJSE_PPEValue?.chemicalSuit)!
        JSAObject.riskAssesment.apron = (readJSAObject.TOJSE_PPEValue?.apron)!
        JSAObject.riskAssesment.foulWeatherGear = (readJSAObject.TOJSE_PPEValue?.needFoulWeatherGear)!
        JSAObject.riskAssesment.respiratorType = (readJSAObject.TOJSE_PPEValue?.respiratorTypeDescription)!.utf8DecodedString()
        JSAObject.riskAssesment.otherPPE = (readJSAObject.TOJSE_PPEValue?.otherPPEDescription)!.utf8DecodedString()
        JSAObject.riskAssesment.other = (readJSAObject.TOJSE_PPEValue?.gloveDescription)!.utf8DecodedString()
        JSAObject.riskAssesment.chemical = (readJSAObject.TOJSE_PPEValue?.chemicalGloveDescription)!.utf8DecodedString()
        
        
        // Hazard control
        
        //Pressureized Equipment
        JSAObject.hazardCategories.categories[0][0] = (readJSAObject.TOJSAHAZARDPRESSValue?.performIsolation)!
        JSAObject.hazardCategories.categories[0][1] = (readJSAObject.TOJSAHAZARDPRESSValue?.depressurizeDrain)!
        JSAObject.hazardCategories.categories[0][2] = (readJSAObject.TOJSAHAZARDPRESSValue?.relieveTrappedPressure)!
        JSAObject.hazardCategories.categories[0][3] = (readJSAObject.TOJSAHAZARDPRESSValue?.doNotWorkInLineOfFire)!
        JSAObject.hazardCategories.categories[0][4] = (readJSAObject.TOJSAHAZARDPRESSValue?.anticipateResidual)!
        JSAObject.hazardCategories.categories[0][5] = (readJSAObject.TOJSAHAZARDPRESSValue?.secureAllHoses)!
        
        //poor lighting
        JSAObject.hazardCategories.categories[1][0] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.provideAlternateLighting)!
        JSAObject.hazardCategories.categories[1][1] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.waitUntilVisibilityImprove)!
        JSAObject.hazardCategories.categories[1][2] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.deferUntilVisibilityImprove)!
        JSAObject.hazardCategories.categories[1][3] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.knowDistanceFromPoles)!
        
        //personnel
        JSAObject.hazardCategories.categories[2][0] = (readJSAObject.TOJSAHAZARDPERSONValue?.performInduction)!
        JSAObject.hazardCategories.categories[2][1] = (readJSAObject.TOJSAHAZARDPERSONValue?.mentorCoachSupervise)!
        JSAObject.hazardCategories.categories[2][2] = (readJSAObject.TOJSAHAZARDPERSONValue?.verifyCompetencies)!
        JSAObject.hazardCategories.categories[2][3] = (readJSAObject.TOJSAHAZARDPERSONValue?.addressLimitations)!
        JSAObject.hazardCategories.categories[2][4] = (readJSAObject.TOJSAHAZARDPERSONValue?.manageLanguageBarriers)!
        JSAObject.hazardCategories.categories[2][5] = (readJSAObject.TOJSAHAZARDPERSONValue?.wearSeatBelts)!
        
        //confined space entry
        JSAObject.hazardCategories.categories[3][0] = (readJSAObject.TOJSAHAZARDCSEValue?.discussWorkPractice)!
        JSAObject.hazardCategories.categories[3][1] = (readJSAObject.TOJSAHAZARDCSEValue?.conductAtmosphericTesting)!
        JSAObject.hazardCategories.categories[3][2] = (readJSAObject.TOJSAHAZARDCSEValue?.monitorAccess)!
        JSAObject.hazardCategories.categories[3][3] = (readJSAObject.TOJSAHAZARDCSEValue?.protectSurfaces)!
        JSAObject.hazardCategories.categories[3][4] = (readJSAObject.TOJSAHAZARDCSEValue?.prohibitMobileEngine)!
        JSAObject.hazardCategories.categories[3][5] = (readJSAObject.TOJSAHAZARDCSEValue?.provideObserver)!
        JSAObject.hazardCategories.categories[3][6] = (readJSAObject.TOJSAHAZARDCSEValue?.developRescuePlan)!
        
        //simultaneous operations
        JSAObject.hazardCategories.categories[4][0] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.followSimopsMatrix)!
        JSAObject.hazardCategories.categories[4][1] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.mocRequiredFor)!
        JSAObject.hazardCategories.categories[4][2] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.interfaceBetweenGroups)!
        JSAObject.hazardCategories.categories[4][3] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.useBarriersAnd)!
        JSAObject.hazardCategories.categories[4][4] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.havePermitSigned)!
        
        //ignition sources
        JSAObject.hazardCategories.categories[5][0] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.removeCombustibleMaterials)!
        JSAObject.hazardCategories.categories[5][1] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.provideFireWatch)!
        JSAObject.hazardCategories.categories[5][2] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.implementAbrasiveBlastingControls)!
        JSAObject.hazardCategories.categories[5][3] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.conductContinuousGasTesting)!
        JSAObject.hazardCategories.categories[5][4] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.earthForStaticElectricity)!
        
        //hazardous substances
        JSAObject.hazardCategories.categories[6][0] = (readJSAObject.TOJSAHAZARDSUBSValue?.drainEquipment)!
        JSAObject.hazardCategories.categories[6][1] = (readJSAObject.TOJSAHAZARDSUBSValue?.followSdsControls)!
        JSAObject.hazardCategories.categories[6][2] = (readJSAObject.TOJSAHAZARDSUBSValue?.implementHealthHazardControls)!
        JSAObject.hazardCategories.categories[6][3] = (readJSAObject.TOJSAHAZARDSUBSValue?.testMaterial)!
        
        //potential Spills
        JSAObject.hazardCategories.categories[7][0] = (readJSAObject.TOJSAHAZARDSPILLValue?.drainEquipment)!
        JSAObject.hazardCategories.categories[7][1] = (readJSAObject.TOJSAHAZARDSPILLValue?.connectionsInGoodCondition)!
        JSAObject.hazardCategories.categories[7][2] = (readJSAObject.TOJSAHAZARDSPILLValue?.spillContainmentEquipment)!
        JSAObject.hazardCategories.categories[7][3] = (readJSAObject.TOJSAHAZARDSPILLValue?.haveSpillCleanupMaterials)!
        JSAObject.hazardCategories.categories[7][4] = (readJSAObject.TOJSAHAZARDSPILLValue?.restrainHosesWhenNotInUse)!
        
        //Weather
        JSAObject.hazardCategories.categories[8][0] = (readJSAObject.TOJSAHAZARDWEATHERValue?.controlsForSlipperySurface)!
        JSAObject.hazardCategories.categories[8][1] = (readJSAObject.TOJSAHAZARDWEATHERValue?.heatBreak)!
        JSAObject.hazardCategories.categories[8][2] = (readJSAObject.TOJSAHAZARDWEATHERValue?.coldHeaters)!
        JSAObject.hazardCategories.categories[8][3] = (readJSAObject.TOJSAHAZARDWEATHERValue?.lightning)!
        
        //High Noise
        JSAObject.hazardCategories.categories[9][0] = (readJSAObject.TOJSAHAZARDNOISEValue?.wearCorrectHearing)!
        JSAObject.hazardCategories.categories[9][1] = (readJSAObject.TOJSAHAZARDNOISEValue?.manageExposureTimes)!
        JSAObject.hazardCategories.categories[9][2] = (readJSAObject.TOJSAHAZARDNOISEValue?.shutDownEquipment)!
        JSAObject.hazardCategories.categories[9][3] = (readJSAObject.TOJSAHAZARDNOISEValue?.useQuietTools)!
        JSAObject.hazardCategories.categories[9][4] = (readJSAObject.TOJSAHAZARDNOISEValue?.soundBarriers)!
        JSAObject.hazardCategories.categories[9][5] = (readJSAObject.TOJSAHAZARDNOISEValue?.provideSuitableComms)!
        
        //Dropped Objects
        JSAObject.hazardCategories.categories[10][0] = (readJSAObject.TOJSAHAZARDDROPPEDValue?.markRestrictEntry)!
        JSAObject.hazardCategories.categories[10][1] = (readJSAObject.TOJSAHAZARDDROPPEDValue?.useLiftingEquipmentToRaise)!
        JSAObject.hazardCategories.categories[10][2] = (readJSAObject.TOJSAHAZARDDROPPEDValue?.secureTools)!
        
        //Lifting Equipment
        JSAObject.hazardCategories.categories[11][0] = (readJSAObject.TOJSAHAZARDLIFTValue?.confirmEquipmentCondition)!
        JSAObject.hazardCategories.categories[11][1] = (readJSAObject.TOJSAHAZARDLIFTValue?.obtainApprovalForLifts)!
        JSAObject.hazardCategories.categories[11][2] = (readJSAObject.TOJSAHAZARDLIFTValue?.haveDocumentedLiftPlan)!
        
        //work at heights
        JSAObject.hazardCategories.categories[12][0] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.discussWorkingPractice)!
        JSAObject.hazardCategories.categories[12][1] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.verifyFallRestraint)!
        JSAObject.hazardCategories.categories[12][2] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.useFullBodyHarness)!
        JSAObject.hazardCategories.categories[12][3] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.useLockTypeSnaphoooks)!
        
        //portable electrical equipment
        JSAObject.hazardCategories.categories[13][0] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.inspectToolsForCondition)!
        JSAObject.hazardCategories.categories[13][1] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.implementGasTesting)!
        JSAObject.hazardCategories.categories[13][2] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.protectElectricalLeads)!
        JSAObject.hazardCategories.categories[13][3] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.identifyEquipClassification)!
        
        //moving equipment
        JSAObject.hazardCategories.categories[14][0] = (readJSAObject.TOJSAHAZARDMOVINGValue?.confirmMachineryIntegrity)!
        JSAObject.hazardCategories.categories[14][1] = (readJSAObject.TOJSAHAZARDMOVINGValue?.provideProtectiveBarriers)!
        JSAObject.hazardCategories.categories[14][2] = (readJSAObject.TOJSAHAZARDMOVINGValue?.observerToMonitorProximityPeopleAndEquipment)!
        JSAObject.hazardCategories.categories[14][3] = (readJSAObject.TOJSAHAZARDMOVINGValue?.lockOutEquipment)!
        JSAObject.hazardCategories.categories[14][4] = (readJSAObject.TOJSAHAZARDMOVINGValue?.doNotWorkInLineOfFire)!
        
        //manual handling
        JSAObject.hazardCategories.categories[15][0] = (readJSAObject.TOJSAHAZARDMANUALValue?.assessManualTask)!
        JSAObject.hazardCategories.categories[15][1] = (readJSAObject.TOJSAHAZARDMANUALValue?.limitLoadSize)!
        JSAObject.hazardCategories.categories[15][2] = (readJSAObject.TOJSAHAZARDMANUALValue?.properLiftingTechnique)!
        JSAObject.hazardCategories.categories[15][3] = (readJSAObject.TOJSAHAZARDMANUALValue?.confirmStabilityOfLoad)!
        JSAObject.hazardCategories.categories[15][4] = (readJSAObject.TOJSAHAZARDMANUALValue?.getAssistanceOrAid)!
        
        //equipment & Tools
        JSAObject.hazardCategories.categories[16][0] = (readJSAObject.TOJSAHAZARDTOOLSValue?.inspectEquipmentTool)!
        JSAObject.hazardCategories.categories[16][1] = (readJSAObject.TOJSAHAZARDTOOLSValue?.brassToolsNecessary)!
        JSAObject.hazardCategories.categories[16][2] = (readJSAObject.TOJSAHAZARDTOOLSValue?.useProtectiveGuards)!
        JSAObject.hazardCategories.categories[16][3] = (readJSAObject.TOJSAHAZARDTOOLSValue?.useCorrectTools)!
        JSAObject.hazardCategories.categories[16][4] = (readJSAObject.TOJSAHAZARDTOOLSValue?.checkForSharpEdges)!
        JSAObject.hazardCategories.categories[16][5] = (readJSAObject.TOJSAHAZARDTOOLSValue?.applyHandSafetyPrinciple)!
        
        //Slip Trips & Tools
        JSAObject.hazardCategories.categories[17][0] = (readJSAObject.TOJSAHAZARDFALLSValue?.identifyProjections)!
        JSAObject.hazardCategories.categories[17][1] = (readJSAObject.TOJSAHAZARDFALLSValue?.flagHazards)!
        JSAObject.hazardCategories.categories[17][2] = (readJSAObject.TOJSAHAZARDFALLSValue?.secureCables)!
        JSAObject.hazardCategories.categories[17][3] = (readJSAObject.TOJSAHAZARDFALLSValue?.cleanUpLiquids)!
        JSAObject.hazardCategories.categories[17][4] = (readJSAObject.TOJSAHAZARDFALLSValue?.barricadeHoles)!
        
        //High Energy
        JSAObject.hazardCategories.categories[18][0] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.restrictAccess)!
        JSAObject.hazardCategories.categories[18][1] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.dischargeEquipment)!
        JSAObject.hazardCategories.categories[18][2] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.observeSafeWorkDistance)!
        JSAObject.hazardCategories.categories[18][3] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.useFlashBurn)!
        JSAObject.hazardCategories.categories[18][4] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.useInsulatedGloves)!
        
        //Excavations
        JSAObject.hazardCategories.categories[19][0] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.haveExcavationPlan)!
        JSAObject.hazardCategories.categories[19][1] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.locatePipesByHandDigging)!
        JSAObject.hazardCategories.categories[19][2] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.deEnergizeUnderground)!
        JSAObject.hazardCategories.categories[19][3] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.cseControls)!
        
        //mobile equipment
        JSAObject.hazardCategories.categories[20][0] = (readJSAObject.TOJSAHAZARDMOBILEValue?.assessEquipmentCondition)!
        JSAObject.hazardCategories.categories[20][1] = (readJSAObject.TOJSAHAZARDMOBILEValue?.controlAccess)!
        JSAObject.hazardCategories.categories[20][2] = (readJSAObject.TOJSAHAZARDMOBILEValue?.monitorProximity)!
        JSAObject.hazardCategories.categories[20][3] = (readJSAObject.TOJSAHAZARDMOBILEValue?.manageOverheadHazards)!
        JSAObject.hazardCategories.categories[20][4] = (readJSAObject.TOJSAHAZARDMOBILEValue?.adhereToRules)!
        
        //potential hazards
        
        for val in readJSAObject.TOJSASTEPSArrayValue
        {
            let obj = PotentialHazard()
            obj.taskStep = val.taskSteps.utf8DecodedString()
            obj.potentialHazards = val.potentialHazards.utf8DecodedString()
            obj.hazardControls = val.hazardControls.utf8DecodedString()
            obj.personResponsible = val.personResponsible.utf8DecodedString()
            
            JSAObject.potentialHazards.append(obj)
        }
        
        //stop the job
        
        for val in readJSAObject.TOJSASTOPArrayValue
        {
            var triggerArray = [String]()
            if val.lineDescription != ""
            {
                //                let obj = Trigger()
                //                obj.triggers.append(val.lineDescription)
                triggerArray.append(val.lineDescription.utf8DecodedString())
            }
            JSAObject.stopTheJob.append(contentsOf: triggerArray)
        }
        
        
        //location
        JSAObject.location.removeAll()
        for each in readJSAObject.TOJSALOCATIONValue {
            
            let location = Location()
            location.facilityOrSite = each.facilityOrSite
            location.hierarchyLevel = each.hierachyLevel
            location.facility = each.facility
            location.muwi = each.muwi
            location.serialNo = each.serialNo
            JSAObject.location.append(location)
            
        }
        
        
        // people
        var peopleList = [People]()
        for val in readJSAObject.TOPTWPEOPLEArrayValue
        {
            
            let people = People()
            people.firstName = val.firstName
            people.lastName = val.lastName
            people.contactNumber = val.contactNumber
            people.hasSignedCSE = val.hasSignedCSE
            people.hasSignedJSA = val.hasSignedJSA
            people.hasSignedCWP = val.hasSignedCWP
            people.hasSignedHWP = val.hasSignedHWP
            peopleList.append(people)
            
        }
        JSAObject.peopleList = peopleList
        
        readPermit(permitType: permitType)
        
    }
}


extension PermitController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
    
        if textField.tag == 0 {
            self.plannedDateTime = date
            textField.text = date.toDateFormat(.dayMonthYear)
        }
        else if textField.tag == 1  {
            self.startTime = date
            textField.text = date.toDateFormat(.short)
        } else if textField.tag == 5 {
            self.estimatedTimeOfWork = date
            textField.text = date.toDateFormat(.short)
        }
    }
}


extension PermitController  {
    
    func validateFields(header: PermitHeader) -> (isValid: Bool, message: String) {
        
        guard header.plannedDateTime.count > 0 else {
            return (false, "Please enter planned date of work")
        }
        guard header.startTime.count > 0 else {
            return (false, "Please enter start time")
        }
        
        return (true, "")
        
    }
}


extension PermitController {
    
    
    func registerKeyBoardNotification() {
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
    }
    
    func unregisterKeyBoardNotification() {
        
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func keyboardWillShow(_ notification:Notification) {
        
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
//            let keyboardFrame = self.permitForm.convert(keyboardSize, from: nil)
            self.permitForm.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: keyboardSize.height, right: 0)
        }
    }
    @objc func keyboardWillHide(_ notification:Notification) {
        
        if let _ = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            self.permitForm.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        }
    }
}

