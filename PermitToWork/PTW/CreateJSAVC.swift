//
//  CreateJSAVC.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 20/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

var createJSAController = CreateJSAVC()

@available(iOS 10.0, *)
class CreateJSAVC: UIViewController , UITableViewDelegate,UITableViewDataSource, UITextViewDelegate ,UITextFieldDelegate{
    
    @IBOutlet weak var addPeopleBottomBar: UIView!
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var navigationBar: UINavigationBar!
    @IBOutlet weak var createJSATableView: UITableView!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnPrevious: UIButton!
    @IBOutlet weak var btnAddPeople: UIButton!

    @IBOutlet var tableHeightConstraint: NSLayoutConstraint!
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    let context = PTWCoreData.shared.managedObjectContext
    private let facilityCellIdentifier = String(describing: JSAFacilityTableViewCell.self)
    var selectedJSA : String = ""
    let defaults = UserDefaults.standard
    var facilityOrSite : String = ""
    var permitNumber : String = ""
    var jsaPermitNumber : String = ""
    var taskDescription : String = ""
    let identifyMostSeriousPotentialInjury : String = ""
    var setOfLocations = [Location]()
    var jsaOFFLineArray  = [String]()
    var jsaArray = ["Location","Task Description","Identify the most serious potential injury for the task being performed"]
    
    var isJsaPreview = false
    var isJsaApproved = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        JSAObject.createJSA.facility = currentLocation.facilityOrSite
        JSAObject.createJSA.permitType = "JSA"
        
        addPeopleBottomBar.isHidden = true
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "JSA - Review"
                isJsaPreview = true
            }
            else if val == "false"
            {
                navigationTitle.title = "JSA"
                isJsaPreview = false
            }
        }
        else
        {
            navigationTitle.title = "JSA"
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
        
        if JSAObject.status == ""{
            setOfLocations.append(currentLocation)
        }
        
        createJSATableView.tableFooterView = UIView()
        self.createJSATableView.dataSource = self
        self.createJSATableView.delegate = self
        self.createJSATableView.estimatedRowHeight = 44
        self.createJSATableView.rowHeight = UITableView.automaticDimension
        let nib = UINib(nibName: facilityCellIdentifier, bundle: nil)
        self.createJSATableView.register(nib, forCellReuseIdentifier: facilityCellIdentifier)
        let identifyNib = UINib(nibName: "IdentifyCell", bundle: nil)
        self.createJSATableView.register(identifyNib, forCellReuseIdentifier: "IdentifyCell")
        self.navigationController?.navigationBar.isHidden = true
        if isJsaApproved == false{
            if selectedJSA != ""{
                if ConnectionCheck.isConnectedToNetwork(){
                    self.readJSA()
                }
                else{
                    let jsaDetailService = JSADetailModelService(context: self.context)
                    let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedJSA)!))
                    let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
                    if jsaDetail.count>0{
                        JSAObject = jsaDetail[0].getJSA()
                    }
                    JSAObject.currentFlow = .JSA
                    createJSATableView.reloadData()
                }
            }
            else
            {
                self.navigationTitle.title = "JSA"
            }
        }
        
        let tapgesture = UITapGestureRecognizer(target: self, action: #selector(onViewTapped(gesture:)))
        self.view.addGestureRecognizer(tapgesture)
        
        
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        
        if isJsaPreview || isJsaApproved
        {
            setOfLocations = JSAObject.location
        }
        else{
            var previousLocationSet = [Location]()
            if JSAObject.status.lowercased() == "submitted"{
                previousLocationSet = JSAObject.location
            }
            else{
                
                previousLocationSet = setOfLocations
            }
            setOfLocations.removeAll()
            setOfLocations.append(contentsOf: previousLocationSet)
            if addedLocation.facilityOrSite != ""{
                setOfLocations.append(addedLocation)
            }
            addedLocation = Location()
            //  JSAObject.location.removeAll()
            JSAObject.location = setOfLocations
            createJSATableView.reloadData()
        }
        if currentUser.isReadOnly == true{
            btnAddPeople.isHidden = true
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
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.endEditing(true)
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        //localDict[textField.tag] = textField.text!
        
        switch textField.tag
        {
        
        case 1:
            if let str = textField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    textField.text = ""
                }
                else{
                    
                    JSAObject.createJSA.taskDescription = textField.text!
                }
            }
            
            break
        case 2:
            if let str = textField.text {
                if str.count > 100{
                    let alertController = UIAlertController.init(title: "", message:"Text cannot have more than 100 chracters" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    textField.text = ""
                }
                else{
                    let str = textField.text!.replacingOccurrences(of: "'", with: "\'")
                    let replaced = str.replacingOccurrences(of: "\"", with: "\\\"")
                    JSAObject.createJSA.injuryDescription = replaced
                }
            }
            
            break
        default:
            print("It is nothing");
        }
        
    }
    
    // MARK:- UItextViewDelegate
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if(text == "\n") {
            textView.resignFirstResponder()
            textView.endEditing(true)
            return false
        }
        return true
    }
    
    
    func textViewDidChange(_ textView: UITextView) {
        
        //        if jsatextView.textColor == UIColor.lightGray {
        //            jsatextView.text = ""
        //            jsatextView.textColor = UIColor.black
        //        }
        //        if textView.text.contains("Add Tex")
        //        {
        //            let str = textView.text.replacingOccurrences(of: "Add Tex", with: "")
        //            textView.text = str
        //            textView.textColor = UIColor.black
        //        }
        //        else if textView.text.contains("Add Text")
        //        {
        //            let str = textView.text.replacingOccurrences(of: "Add Tex", with: "")
        //            textView.text = str
        //            textView.textColor = UIColor.black
        //        }
        //        else
        //        {
        //            textView.textColor = UIColor.black
        //        }
        //
        //        if textView.text.isEmpty
        //        {
        //            textView.text = "Add Text"
        //            textView.textColor = UIColor.lightGray
        //        }
        
        
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        
        //        if textView.textColor == UIColor.lightGray {
        //            textView.text = ""
        //            textView.textColor = UIColor.black
        //        }
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        
        //        if textView.text.isEmpty {
        //            textView.text = "Identify the most serious potential injury for the task being performed"
        //            textView.textColor = UIColor.lightGray
        //        }
        
        JSAObject.createJSA.injuryDescription = textView.text!
    }
    
    
    func userAlreadyExist(kUsernameKey: String) -> Bool {
        return UserDefaults.standard.object(forKey: kUsernameKey) != nil
    }
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        
        return 3
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        
        if indexPath.row == 0 {
            if isJsaPreview || isJsaApproved
            {
                let facilityCell = self.configureFacilityCell(for: indexPath)
                facilityCell.isUserInteractionEnabled = false
                facilityCell.selectionStyle = .none
                return facilityCell
            }
            else{
                let facilityCell = self.configureFacilityCell(for: indexPath)
                facilityCell.isUserInteractionEnabled = true
                facilityCell.selectionStyle = .none
                return facilityCell
            }
            
            
        } else if indexPath.row == 1{
            
            let cell:CreateJSATableViewCell = (createJSATableView.dequeueReusableCell(withIdentifier: "CreateJSATableViewCell") as! CreateJSATableViewCell?)!
            cell.selectionStyle = UITableViewCell.SelectionStyle.none
            cell.nameLabel?.text = jsaArray[indexPath.row] + "*"
            cell.statusLabel.tag = indexPath.row
            cell.statusLabel.delegate = self
            
            if isJsaPreview || isJsaApproved
            {
                cell.isUserInteractionEnabled = false
                cell.statusLabel.text = JSAObject.createJSA.taskDescription
            }
            else
            {
                cell.isUserInteractionEnabled = true
                cell.statusLabel.text = JSAObject.createJSA.taskDescription
            }
            
            
            return cell
        }
        else{
            let cell:IdentifyCell = (createJSATableView.dequeueReusableCell(withIdentifier: "IdentifyCell") as! IdentifyCell?)!
            cell.reasonView.delegate = self
            cell.reasonView.text = "Add text"
            //cell.reasonView.textColor = UIColor.lightGray
            cell.selectionStyle = UITableViewCell.SelectionStyle.none
            if isJsaPreview || isJsaApproved
            {
                cell.isUserInteractionEnabled = false
                cell.reasonView.text = JSAObject.createJSA.injuryDescription
            }
            else
            {
                cell.isUserInteractionEnabled = true
                cell.reasonView.text = JSAObject.createJSA.injuryDescription
            }
//            if JSAObject.createJSA.injuryDescription != "Add Text"{
//                //cell.reasonView.textColor = UIColor.darkGray
//            }
//            else{
//                cell.reasonView.textColor = UIColor.darkGray
//            }
            return cell
        }
        
    }
    
    @objc
    func onViewTapped(gesture: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    
    @IBAction func onClickAddPeople(_ sender: UIButton) {
        if currentUser.isReadOnly != true{
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
        }
    }
    
    @IBAction func onHomePress(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
        JSAObject = JSA()
    }
    @IBAction func previousBtn(_ sender: UIButton)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func nextBtn(_ sender: UIButton)
    {
        self.view.endEditing(true)
        if JSAObject.createJSA.injuryDescription == "Add Text"
        {
            JSAObject.createJSA.injuryDescription = ""
        }
        if JSAObject.createJSA.taskDescription == ""
        {
            let alert = UIAlertController(title: "", message: "Please Enter Task Description", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
        else if JSAObject.createJSA.facility == ""
        {
            let alert = UIAlertController(title: nil, message: "Please Enter Facility", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
        else if JSAObject.createJSA.permitNo.count > 100 || JSAObject.createJSA.facility.count > 100 ||
                    JSAObject.createJSA.taskDescription.count > 100
        {
            let alert = UIAlertController(title: nil, message: "Entered text should not be greater than 100", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "RiskAssesmentViewController") as! RiskAssesmentViewController
            self.navigationController?.pushViewController(dashBoardVC, animated: true)
        }
    }
    
}


extension CreateJSAVC {
    
    func configureFacilityCell(for indexPath: IndexPath) -> JSAFacilityTableViewCell {
        let cell = self.createJSATableView.dequeueReusableCell(withIdentifier: facilityCellIdentifier, for: indexPath) as! JSAFacilityTableViewCell
        cell.locations = JSAObject.location
        cell.sender = self
        cell.collectionViewDelegate = self
        cell.frame = self.createJSATableView.bounds
        return cell
    }
}

extension CreateJSAVC: UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let cell = collectionView.cellForItem(at: indexPath) as? JSALocationCollectionViewCell else { return }
        print(cell.locationLabel.text!)
    }
}

// MARK : Read Object parsing

extension CreateJSAVC {
    
    func readJSA(){
        DispatchQueue.main.async {
            self.loaderStart()
        }
        
        let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/JSA_Details_By_PermitNumber.xsjs?permitNumber=\(selectedJSA)"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    DispatchQueue.main.async{
                    if let jsonDict = JSON as? NSDictionary {
                        readJSAObject = ReadJSA(JSON:jsonDict)
                        self.readModelToJSAModel()
                            self.loaderStop()
                    }
                    }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }   }else{
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
    
    func getAtmosphericTest(){
        
        let urlString : String = "\(BaseUrl.apiURL)/com.iop.ptw/getPTWRecord_Result.xsjs?permitNumber=\(selectedJSA)"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    DispatchQueue.main.async{
                    if let jsonDict = JSON as? NSDictionary {
                        let atmosphericObject = TOPTWTESTREC(JSON : jsonDict.value(forKey: "TOPTWTESTREC") as! NSDictionary)
                        var testsObject = [TOPTWTESTRES]()
                        let testDictionary = jsonDict.value(forKey: "TOPTWTESTRES") as! [NSDictionary]
                        for each in testDictionary{
                            let testObject = TOPTWTESTRES(JSON : each)
                            testsObject.append(testObject)
                        }
                        
                        //atmospheric test
                        JSAObject.atmosphericTesting.serialNo = atmosphericObject.serialNo
                        JSAObject.atmosphericTesting.areaOrEquipmentTotest = atmosphericObject.areaToBeTested.utf8DecodedString()
                        JSAObject.atmosphericTesting.detectorUsed = atmosphericObject.detectorUsed.utf8DecodedString()
                        JSAObject.atmosphericTesting.dateOfLastCallibration = atmosphericObject.DateOfLastCalibration
                        JSAObject.atmosphericTesting.O2 = atmosphericObject.isO2
                        JSAObject.atmosphericTesting.Lels = atmosphericObject.isLELS
                        JSAObject.atmosphericTesting.H2S = atmosphericObject.isH2S
                        JSAObject.atmosphericTesting.Other = atmosphericObject.Other.utf8DecodedString()
                        JSAObject.atmosphericTesting.priorToWorkCommencing = atmosphericObject.priorToWorkCommencing
                        JSAObject.atmosphericTesting.eachWorkPeriod = atmosphericObject.eachWorkPeriod
                        JSAObject.atmosphericTesting.noHours = atmosphericObject.everyHour
                        JSAObject.atmosphericTesting.continuousMonitoringreqd = atmosphericObject.continuousGasMonitoring
                        JSAObject.atmosphericTesting.testFrequency = atmosphericObject.testingFrequency.utf8DecodedString()
                        
                        
                        //test results
                        JSAObject.testResult.Name = atmosphericObject.gasTester.utf8DecodedString()
                        JSAObject.testResult.specialPrecaution = atmosphericObject.gasTesterComments.utf8DecodedString()
                        JSAObject.testResult.serialNumber = atmosphericObject.deviceSerialNo.utf8DecodedString()
                        
                        for val in testsObject
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
                        
                        let jsaDetailService = JSADetailModelService(context: self.context)
                        let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(self.selectedJSA)!))
                        let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
                        if jsaDetail.count>0{
                            jsaDetailService.delete(id: jsaDetail[0].objectID)
                        }
                        _ = jsaDetailService.create(jsa: JSAObject)
                        jsaDetailService.saveChanges()
                    }
                    }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
            }else{
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
        
        self.navigationTitle.title = "JSA - \(JSAObject.permitNumber)"
        
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
        
        if JSAObject.status.lowercased() == "approved"
        {
            isJsaApproved = true
            addPeopleBottomBar.isHidden = false
            
        }
        else
        {
            isJsaApproved = false
            addPeopleBottomBar.isHidden = true
        }
        
        if JSAObject.hasCWP == 1 && (JSAObject.hasHWP == 0 || JSAObject.hasCSP == 0){
            getAtmosphericTest()
        }
        else{
            
            let jsaDetailService = JSADetailModelService(context: self.context)
            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedJSA)!))
            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
            if jsaDetail.count>0{
                jsaDetailService.delete(id: jsaDetail[0].objectID)
            }
            _ = jsaDetailService.create(jsa: JSAObject)
            jsaDetailService.saveChanges()
        }
        DispatchQueue.main.async {
            self.createJSATableView.reloadData()
        }
        
    }
    
}
