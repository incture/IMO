//
//  AttestController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 10/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import PDFKit
import NotificationBannerSwift

enum Account_Category : String {
    
    case K = "K - Cost Center"
    case P = "P - Project"
    case U = "U - Unknown"
    case F = "F - Order"
    case UnknownType
    
    static let allValues = [K.rawValue, P.rawValue]//, U.rawValue, F.rawValue]
    
}


class AttestController: UIViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource, XMLParserDelegate {
    
    
    @IBOutlet var attestView: UITableView!
    @IBOutlet var floatingButton: UIButton!
    
    // var labelValues = ["Murphy PO No", "Murphy WO No", "Accounting Category", "WBS", "Cost Center", "SES Approver *"]
    var picker = UIPickerView()
    var toolBar = UIToolbar()
    var bgView = UIView()
    
    var ticket : FieldTicket?
    var poText : String = ""
    var poNumber : String = ""{
        didSet{
            if poNumber != "" &&  self.ticket?.Status != TicketStatus.Review.rawValue{
                
                if Cost_Object == "Work Order"{
                    self.getPOdetails(poNumber: poNumber)
                    TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","SES Approver" , "DigiSign"]
                }
                else if Cost_Object == "Purchase Order"{
                    self.getPOdetails(poNumber: poNumber)
                }
            }
        }
    }
    var woText : String = ""
    var woNumber : String = ""{
        didSet{
            if Cost_Object  == "Work Order"{
                if woNumber != "" && self.ticket?.Status != TicketStatus.Review.rawValue{
                    self.GetPOforWO()
                }
            }
        }
    }
    var acc_category : String = ""{
        didSet{
            
            if acc_category != "" && self.ticket?.Status != TicketStatus.Review.rawValue{
                if Cost_Object == "Work Order" {
                    switch acc_category{
                    case "P":
                        TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","WBS","SES Approver" , "DigiSign"]
                        break
                    case "K":
                        TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","Cost Center","SES Approver" , "DigiSign"]
                        break
                    default:
                        TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","SES Approver" , "DigiSign"]
                        print("error while setting acc_category")
                        
                    }
                    
                }
                else if Cost_Object == "Purchase Order" {
                    switch acc_category{
                    case "P":
                        TableLabelValues = ["Cost Object","PO" ,"Accounting Category","WBS","SES Approver" , "DigiSign"]
                        break
                    case "K":
                        TableLabelValues = ["Cost Object", "PO" ,"Accounting Category","Cost Center","SES Approver" , "DigiSign"]
                        break
                    default:
                        TableLabelValues = ["Cost Object", "PO" ,"Accounting Category", "SES Approver" , "DigiSign"]
                        print("error while setting acc_category")
                        
                    }
                }
            }
        }
    }
    var wbs : String = ""
    var wbsText : String = ""
    var cost_centre : String = ""
    var cost_centreText : String = ""
    var sesApprover : String = ""
    var reviewedBy : String = ""
    var reviewedDate : String = ""
    var isFetchedFromService : Bool = false
    var reason : String = ""
    var csrfToken : String?
    var fieldTicketNumber : String?
    var vendorID : String?
    var parser = XMLParser()
    var element = NSString()
    var ticketName : String?
    var instanceID : String?
    var workflowCall = 0
    var responseType : String?
    var responseMessage : String?
    var errorMessage : String?
    var attachmentArray = [Attachment]()
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var isVerified : Bool?
    var senderController : MurphyReviewerTicketController?
    var csrfCount : Int = 0
    var attachPost = [[String : Any]]()
    var restCsrfCount : Int?
    var statusLocallyAltered : Bool = false
    var restBanner = NotificationBanner(title: "Background sync completed", subtitle: "", style: .success)
    //Initial Table Values
    var TableLabelValues = ["Cost Object", "SES Approver" , "DigiSign"]
    var Cost_Object_Values = ["Purchase Order", "Work Order","WBS","Cost Center"]
    var Cost_Object : String = ""{
        didSet{
            if self.ticket?.Status != TicketStatus.Review.rawValue{
                switch Cost_Object {
                case "Purchase Order":
                    TableLabelValues = ["Cost Object","PO" ,"SES Approver" , "DigiSign"]
                    if self.ticket?.serviceType == "Salt water Disposal"{
                        self.POSearchSaltWater(searchText: "")
                    }
                    else{
                        self.POSearch(searchText: "")
                    }
                    break
                case "Work Order":
                    TableLabelValues = ["Cost Object", "WO","SES Approver" , "DigiSign"]
                    break
                case "WBS":
                    TableLabelValues = ["Cost Object","WBS","SES Approver" , "DigiSign"]
                    break
                case "Cost Center":
                    TableLabelValues = ["Cost Object", "Cost Center","SES Approver" , "DigiSign"]
                    getCostCentre()
                    break
                default:
                    break
                }
                self.flushSelectedValues()
                self.attestView.reloadData()
            }
            
        }
    }
    var POforWOArray : [GenericItem] = []{
        didSet{
                TableLabelValues = ["Cost Object","WO","PO" ,"SES Approver" , "DigiSign"]
        }
    }
    var MandatoryFieldsFilled : Bool = false
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let formNib = UINib(nibName: "FormCell", bundle: nil)
        attestView.register(formNib, forCellReuseIdentifier: "FormCell")
        let signNib = UINib(nibName: "SignatureVerificationCell", bundle: nil)
        attestView.register(signNib, forCellReuseIdentifier: "SignatureVerificationCell")
        attestView.tableFooterView = UIView()
        floatingButton.layer.cornerRadius = 30
        floatingButton.isEnabled = false
        floatingButton.isHidden = true
        attestView.keyboardDismissMode = UIScrollView.KeyboardDismissMode.onDrag
        createNavBar()
        generatePicker()
        if self.ticket?.costObject != ""{
            
        }
        // This is a rejected ticket
        if self.ticket?.Status == TicketStatus.Review.rawValue{
            self.Cost_Object = self.ticket?.costObject ?? ""
            self.acc_category = self.ticket?.accounting_categ ?? ""
            self.wbs = (self.ticket?.wbsElement) ?? ""
            self.wbsText = self.ticket?.wbsElement ?? ""
            self.cost_centreText = self.ticket?.costCenterText ?? ""
            self.cost_centre = self.ticket?.costCenter ?? ""
            self.poNumber = self.ticket?.poNumber ?? ""
            self.poText = self.ticket?.poText ?? ""
            self.woNumber = self.ticket?.workOrder ?? ""
            self.woText = self.ticket?.workText ?? ""
            self.sesApprover = self.ticket?.sesApprover ?? ""
            if self.acc_category == "K" || self.acc_category == "P"{
                self.isFetchedFromService = true
            }
            if self.ticket?.serviceType == "Salt Water Disposal"{
                Cost_Object = "Purchase Order"
                
            }
            switch Cost_Object {
            case "Purchase Order":
                switch acc_category{
                case "P":
                    TableLabelValues = ["Cost Object","PO" ,"Accounting Category","WBS","SES Approver" , "DigiSign"]
                case "K":
                    TableLabelValues = ["Cost Object", "PO" ,"Accounting Category","Cost Center","SES Approver" , "DigiSign"]
                case "U","F":
                    TableLabelValues = ["Cost Object", "PO" ,"Accounting Category","SES Approver" , "DigiSign"]
                default:
                    TableLabelValues = ["Cost Object", "PO" , "SES Approver" , "DigiSign"]
                    print("error while setting acc_category")
                }
            case "Work Order":
                switch acc_category{
                case "P":
                    TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","WBS","SES Approver" , "DigiSign"]
                case "K":
                    TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","Cost Center","SES Approver" , "DigiSign"]
                case "U","F":
                    TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","SES Approver" , "DigiSign"]
                default:
                    TableLabelValues = ["Cost Object", "WO", "PO" ,"SES Approver" ,"Accounting Category", "DigiSign"]
                    print("error while setting acc_category")
                    
                }
            case "WBS":
                TableLabelValues = ["Cost Object","WBS","SES Approver" , "DigiSign"]
            case "Cost Center":
                TableLabelValues = ["Cost Object", "Cost Center","SES Approver" , "DigiSign"]
            default:
                break
            }
            
            self.attestView.reloadData()
        }
        if self.ticket?.serviceType == "Salt Water Disposal"{
            Cost_Object = "Purchase Order"
            self.POSearchSaltWater(searchText: "")
        }
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "Verification"
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
        
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    @objc func dismissScreen()
    {
        if statusLocallyAltered {
            ticket?.Status = TicketStatus.Review.rawValue
        }
        self.dismiss(animated: false, completion: nil)
    }
    
    func parser(_ parser: XMLParser, didStartElement elementName: String, namespaceURI: String?, qualifiedName qName: String?, attributes attributeDict: [String : String] = [:]) {
        
        element = elementName as NSString
        print(element)
    }
    
    func parser(_ parser: XMLParser, foundCharacters string: String) {
        
        if element.isEqual(to: "d:FieldTicketNum") {
            if string != "" && string != nil{
                self.ticketName = string
            }
        }
        if element.isEqual(to: "d:Type") {
            self.responseType = string
        }
        if element.isEqual(to: "d:Message") {
            self.responseMessage = string
        }
        if element.isEqual(to: "message") {
            self.errorMessage = string
        }
        
    }
    
    func parser(_ parser: XMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?) {
        
    }
    
    //    func setPONumber(poNumber : String)
    //    {
    //        let indexpath = IndexPath(item: 0, section: 0)
    //        let currentCell : FormCell = attestView.cellForRow(at: indexpath as IndexPath) as! FormCell
    //        currentCell.setValue(fieldValue: poNumber)
    //        self.poNumber = poNumber
    //        getPOdetails(poNumber : poNumber)
    //    }
    
    func setSESApprover(sesApprover : String)
    {
        //        let indexpath = IndexPath(item: 5, section: 0)
        //        let currentCell : FormCell = attestView.cellForRow(at: indexpath as IndexPath) as! FormCell
        //        currentCell.setValue(fieldValue: sesApprover)
        self.sesApprover = sesApprover
        self.attestView.reloadData()
    }
    
    func convertTimeStamp(timeStamp : String) -> String
    {
        let unixDate = Double(timeStamp)
        let date = Date(timeIntervalSince1970: unixDate!)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone.current //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "dd MMM yyyy"
        let strDate = dateFormatter.string(from: date)
        let updatedDate = dateFormatter.date(from: strDate)
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss" //Specify your format that you want
        let stamp_date = dateFormatter.string(from: updatedDate!)
        return stamp_date
    }
    
    
    //200
    func POSearch(searchText : String){
        
        self.loaderStart()
        let timestamp = self.ticket?.start_timestamp[6 ..< 16]
        let dateValue = convertTimeStamp(timeStamp: timestamp!)
        var facilityLocation = ""
        var location = ""
        if self.ticket?.Department == "Operations"{
            facilityLocation = (self.ticket?.facilityCode ?? "")
            
            if self.ticket?.wellPad == ""{
                location = (self.ticket?.facilityCode ?? "")
            }
            else if self.ticket?.well == ""{
                location = (self.ticket?.wellPadCode ?? "")
            }
            else{
                location = (self.ticket?.wellCode ?? "")
            }
        }
        
        //PO_Number eq '\(searchText)' and Vendor eq '\(self.vendorID!)' and Location eq 'LOCATION' and Start_Date eq datetime'2016-03-15T00:00:00' yyyy-MM-ddTHH:mm:ss
        let url = "\(BaseUrl.apiURL)/com.OData.dest/GetPObyVendorSet?$filter=PO_Number eq '\(searchText)' and Vendor eq '\(self.vendorID!)' and Location eq '\(facilityLocation)' and HierLocation eq '\(location)' and Start_Date eq datetime'\(dateValue)'&$skip=0&$top=2&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    //print(JSON)
                    
                    DispatchQueue.main.async{
                        self.loaderStop()
                    }
                    if let jsonDict = JSON as? NSDictionary {
                        if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                            if let result = d.value(forKey: "results") as? [NSDictionary]{
                                if result.count == 1{
                                    let element = PO(item:result[0])
                                    self.poNumber = element.ItemValue1 ?? ""
                                    self.poText = element.ItemValue2 ?? ""
                                    self.attestView.reloadData()
                                }
                            }
                        }
                        
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                
                DispatchQueue.main.async{
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    self.loaderStop()
                }
            }
            
            
        }
        task.resume()
    
    }
    
    func POSearchSaltWater(searchText : String){
        self.loaderStart()
        //self.request?.cancel()
        let s_timestamp = self.ticket?.start_timestamp[6 ..< 16]
        let s_dateValue = convertTimeStamp(timeStamp: s_timestamp!)
        let e_timestamp = self.ticket?.end_timestamp[6 ..< 16]
        let e_dateValue = convertTimeStamp(timeStamp: e_timestamp!)
        //\(self.vendorID!)
        let url = "\(BaseUrl.apiURL)/com.OData.dest/GetPOForSaltWaterSet?$filter=PO_Number eq '\(searchText)' and Vendor eq '\(self.vendorID!)' and Start_Date eq datetime'\(s_dateValue)' and End_Date eq datetime'\(e_dateValue)' and Material_Group eq 'S71131498'&$skip=0&$top=2&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    //print(JSON)
                    
                    DispatchQueue.main.async{
                        self.loaderStop()
                    }
                    if let jsonDict = JSON as? NSDictionary {
                        if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                            if let result = d.value(forKey: "results") as? [NSDictionary]{
                                if result.count == 1{
                                    let element = PO(item:result[0])
                                    self.poNumber = element.ItemValue1 ?? ""
                                    self.poText = element.ItemValue2 ?? ""
                                    self.attestView.reloadData()
                                }
                            }
                        }
                        
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                
                DispatchQueue.main.async{
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    self.loaderStop()
                }
            }
        }
        task.resume()
        
        
    }
    
    //200
    func getCostCentre() {
        self.loaderStart()
        var locationCode = ""
        if self.ticket?.facility == ""{
            locationCode = (self.ticket?.fieldCode ?? "")
        }
        else if self.ticket?.wellPad == ""{
            locationCode = (self.ticket?.facilityCode ?? "")
        }
        else if self.ticket?.well == ""{
            locationCode = (self.ticket?.wellPadCode ?? "")
        }
        else{
            locationCode = (self.ticket?.wellCode ?? "")
        }
        let url = "\(BaseUrl.apiURL)/com.OData.dest/Get_Cost_CenterSet?$filter=Location eq '\(locationCode)'&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    //print(JSON)
                    
                    DispatchQueue.main.async{
                        self.loaderStop()
                        
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.cost_centre = result[0].value(forKey: "Cost_center") as? String ?? ""
                                    self.cost_centreText = result[0].value(forKey: "Cost_center_text") as? String ?? ""
                                    self.attestView.reloadData()
                                }
                            }
                        }
                        
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                
                DispatchQueue.main.async{
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    self.loaderStop()                       }
            }
        }
        task.resume()
       
    }
    
    //200
    func getPOdetails(poNumber : String) {
        
        isFetchedFromService = false
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/GetPODetailsSet(EBELN='\(poNumber)')?$format=json"
        let encodedUrl = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if error == nil  {
                DispatchQueue.main.async {
                    do{
                        guard let data = data else { return }
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                self!.acc_category = (d.value(forKey: "Acc_Ass_category") as? String) ?? ""
                                self!.cost_centre = (d.value(forKey: "Cost_center") as? String) ?? ""
                                self!.cost_centreText = (d.value(forKey: "Cost_center_text") as? String) ?? ""
                                self!.wbs = (d.value(forKey: "WBS") as? String) ?? ""
                                self!.wbsText = (d.value(forKey: "WBS_text") as? String) ?? ""
                                self!.attestView.reloadData()
                                
                                
                                if self!.acc_category == ""{
                                    if self!.Cost_Object == "Work Order"{
                                        self!.TableLabelValues = ["Cost Object", "WO", "PO" ,"Accounting Category","SES Approver" , "DigiSign"]
                                    }
                                    else{
                                        self!.TableLabelValues = ["Cost Object", "PO" ,"Accounting Category","SES Approver" , "DigiSign"]
                                    }
                                    self!.attestView.reloadData()
                                }
                                // if self.acc_category != "U" {
                                self!.isFetchedFromService = true
                                //  }
                                //                            else{
                                //                                 self.isFetchedFromService = false
                                //                            }
                                print(self!.acc_category)
                                print(self!.cost_centre)
                                print(self!.wbs)
                                print(self!.woNumber)
                            }
                            
                        }
                    }
                    catch{
                        print("x\(error.localizedDescription)")
                    }
                    
                }
                
            }
            else{
                DispatchQueue.main.async {
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
       
    }
    
    @IBAction func onDigiSigPress(_ sender: UIButton) {
        
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ImageVerifyController") as! ImageVerifyController
        let navController = UINavigationController(rootViewController: splitViewController)
        self.present(navController, animated: true, completion: nil)
    }
    
    
    @IBAction func attestButtonPressed(_ sender: UIButton) {
        if statusLocallyAltered {
            ticket?.Status = TicketStatus.Review.rawValue
        }
        self.view.endEditing(true)
        self.CheckMandatoryFields()
        if MandatoryFieldsFilled{
            
            //attestImagesAndUpdate()
            openCommentController(isVerified : true)
            
            //            PdfMergeTestService()
        }
        else{
            let alertController = UIAlertController.init(title: "", message:"Please fill mandatory fields and give valid cost objects." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
    }
    
    
    @IBAction func rejectPressed(_ sender: UIButton) {
        if statusLocallyAltered {
            ticket?.Status = TicketStatus.Review.rawValue
        }
        self.view.endEditing(true)
        //        if self.sesApprover == ""{
        //
        //            let alertController = UIAlertController.init(title: "", message:"Please fill mandatory fields." , preferredStyle: UIAlertControllerStyle.alert)
        //            let okAction = UIAlertAction.init(title: "OK", style: UIAlertActionStyle.cancel, handler: nil)
        //            alertController.addAction(okAction)
        //            self.present(alertController, animated: true, completion: nil)
        //        }
        //        else{
        openCommentController(isVerified : false)
        //  }
        
    }
    
    func openCommentController(isVerified : Bool)
    {
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "RejectController") as! RejectController
        splitViewController.fieldMandatory = !isVerified
        splitViewController.senderController = self
        splitViewController.modalPresentationStyle = .overCurrentContext
        self.present(splitViewController, animated: true, completion: nil)
    }
    
    func getReasonToUpdateTicket(isVerified : Bool, reasonText : String)
    {
        reason = reasonText
        if isVerified{
            getCSRFforCreateTicket(status : TicketStatus.Verify, action: ActionType.Verify)
        }
        else{
            getCSRFforCreateTicket(status : TicketStatus.Reject, action: ActionType.Reject)
        }
    }
    
    
    func openApproverSearch(){
        
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.typeofSearch = "SESApprover"
        vc2.ticket = self.ticket!
        self.navigationController?.pushViewController(vc2, animated: true)
        
    }
    func openPOforWOSearch(){
        
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.GenericArray = self.POforWOArray
        vc2.typeofSearch = "POForWO"
        vc2.ticket = self.ticket ?? FieldTicket()
        vc2.vendorID = self.vendorID ?? ""
        self.navigationController?.pushViewController(vc2, animated: true)
    }
    func openPOSearch(){
        
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.typeofSearch = "PO"
        vc2.ticket = self.ticket ?? FieldTicket()
        vc2.vendorID = self.vendorID ?? ""
        self.navigationController?.pushViewController(vc2, animated: true)
    }
    func openWOSearch(){
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.typeofSearch = "WO"
        vc2.ticket = self.ticket ?? FieldTicket()
        vc2.vendorID = self.vendorID ?? ""
        self.navigationController?.pushViewController(vc2, animated: true)
    }
    func openWBSSearch(){
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.typeofSearch = "WBS"
        vc2.ticket = self.ticket ?? FieldTicket()
        vc2.vendorID = self.vendorID ?? ""
        self.navigationController?.pushViewController(vc2, animated: true)
        
    }
    func openCCSearch(){
        
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.typeofSearch = "CostCenter"
        vc2.ticket = self.ticket ?? FieldTicket()
        self.navigationController?.pushViewController(vc2, animated: true)
    }
    
    
    func currentDate() -> String{
        let todayDate = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy"
        let formattedDate = formatter.string(from: todayDate)
        return formattedDate
    }
    
    func generatePicker()
    {
        picker = UIPickerView(frame: CGRect(x:0, y: toolBar.frame.size.height, width : view.frame.width, height : 300))
        picker.backgroundColor = UIColor.white
        picker.showsSelectionIndicator = true
        picker.delegate = self
        picker.dataSource = self
        picker.tag = 1
        // let toolBar = UIToolbar()
        toolBar.frame = CGRect(x: 0, y: 0, width: picker.frame.width, height: 44)
        toolBar.barStyle = UIBarStyle.default
        toolBar.isTranslucent = true
        toolBar.tintColor = UIColor(red: 77/255, green: 184/255, blue: 255/255, alpha: 1)
        toolBar.barTintColor = UIColor(red: 77/255, green: 184/255, blue: 255/255, alpha: 1)
        toolBar.sizeToFit()
        
        bgView = UIView(frame: CGRect(x: 0, y: 200, width: view.frame.width, height: 300))// + toolBar.frame.size.height))

        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.plain, target: self, action: #selector(AttestController.donePicker))
        doneButton.tintColor = UIColor.white
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Cancel", style: UIBarButtonItem.Style.plain, target: self, action: #selector(AttestController.cancelPicker))
        cancelButton.tintColor = UIColor.white
       
        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        toolBar.isUserInteractionEnabled = true
        bgView.addSubview(picker)
        bgView.addSubview(toolBar)

    }

    @objc func donePicker()
    {
        if ticket?.Status == TicketStatus.Review.rawValue{
            ticket?.Status = TicketStatus.Create.rawValue
            self.statusLocallyAltered = true
        }
        if TableLabelValues[bgView.tag] == "Cost Object"{
            self.Cost_Object = Cost_Object_Values[picker.selectedRow(inComponent: 0)]
            flushSelectedValues()
        }else if TableLabelValues[bgView.tag] == "PO" {
            //  self.poNumber = POforWOArray[picker.selectedRow(inComponent: 0)].ItemValue1!
        }
        else{
            if Account_Category.allValues[picker.selectedRow(inComponent: 0)] == Account_Category.K.rawValue{
                self.acc_category = "K"
            }
            else if Account_Category.allValues[picker.selectedRow(inComponent: 0)] == Account_Category.P.rawValue {
                self.acc_category = "P"
            }
        }
        self.view.endEditing(true)
        bgView.removeFromSuperview()
        self.attestView.reloadData()
    }

    @objc func cancelPicker()
    {
        self.view.endEditing(true)
        bgView.removeFromSuperview()
        //        picker.isHidden = true
        //        picker.removeFromSuperview()
    }
    
    func isValidEmail(testStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    func isNumber(testStr:String) -> Bool {
        let numRegex = "^[0-9]+$"
        let numTest = NSPredicate(format:"SELF MATCHES %@", numRegex)
        return numTest.evaluate(with: testStr)
        
    }
    
    func isValidWbs(testStr:String) -> Bool {
        let numRegex = "^[0-9\\.\\-]+$"
        let numTest = NSPredicate(format:"SELF MATCHES %@", numRegex)
        return numTest.evaluate(with: testStr)
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if textField.tag == 5{
            
            return true
        }
        else{
            return true
        }
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        
        switch TableLabelValues[textField.tag] {
        case "Cost Object":
            if self.ticket?.serviceType != "Salt Water Disposal"{
                bgView.tag = textField.tag
                picker.reloadAllComponents()

                textField.inputView = bgView
            }
        case "PO":
            if Cost_Object == "Purchase Order"{
                if self.ticket?.Status == TicketStatus.Review.rawValue{
                    self.ticket?.Status = TicketStatus.Create.rawValue
                    self.statusLocallyAltered = true
                }
                textField.resignFirstResponder()
                textField.endEditing(true)
                self.openPOSearch()
            }
            else{
                textField.resignFirstResponder()
                textField.endEditing(true)
                self.openPOforWOSearch()
            }
        case "WO":
            if Cost_Object == "Work Order"{
                if self.ticket?.Status == TicketStatus.Review.rawValue{
                    self.ticket?.Status = TicketStatus.Create.rawValue
                    self.statusLocallyAltered = true
                }
                textField.resignFirstResponder()
                textField.endEditing(true)
                self.openWOSearch()
            }
            else{
                bgView.tag = textField.tag
                textField.inputView = bgView
            }
        case "Accounting Category":
            if (Cost_Object == "Work Order" || Cost_Object == "Purchase Order") && (self.acc_category != "K" && self.acc_category != "P") {
                bgView.tag = textField.tag
                picker.reloadAllComponents()
                textField.inputView = bgView
            }
            
        case "Cost Center":
            if Cost_Object == "Cost Center" || Cost_Object == "Work Order" || Cost_Object == "Purchase Order"{
                textField.resignFirstResponder()
                textField.endEditing(true)
                self.openCCSearch()
            }
        case "WBS":
            if Cost_Object == "WBS" || Cost_Object == "Work Order" || Cost_Object == "Purchase Order"{
                textField.resignFirstResponder()
                textField.endEditing(true)
                self.openWBSSearch()
            }
        case "SES Approver":
            textField.resignFirstResponder()
            textField.endEditing(true)
            self.openApproverSearch()
        default:
            print("Error in text field did begin editing")
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        self.view.endEditing(true)
        return true
    }
    
    internal func textFieldDidEndEditing(_ textField: UITextField, reason: UITextField.DidEndEditingReason) {
        
        switch TableLabelValues[textField.tag] {
        default:
            print("Not required")
            
        }
        self.view.endEditing(true)
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if TableLabelValues[bgView.tag] == "Cost Object"{
            return Cost_Object_Values.count
        }
        else if TableLabelValues[bgView.tag] == "PO" && Cost_Object == "Work Order"{
            return POforWOArray.count
        } else if TableLabelValues[bgView.tag] == "Accounting Category"{
            return Account_Category.allValues.count
        }
        else{
            return Account_Category.allValues.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if TableLabelValues[bgView.tag] == "Cost Object"{
            return Cost_Object_Values[row]
        }else if TableLabelValues[bgView.tag] == "PO"{
            return POforWOArray[row].ItemValue1
        }else if TableLabelValues[bgView.tag] == "Accounting Category"{
            return Account_Category.allValues[row]
        }
        else{
            return Account_Category.allValues[row]
            
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
    }
    
}

extension AttestController: SearchViewControllerDelegate {
    func didSelect1(searchItem: GenericItem, typeOfSearch: String) {
        //do Something
    }
    
    
    func didSelect(searchItem: GenericItem, typeOfSearch: String) {
        switch typeOfSearch {
        case "CostCenter":
            self.cost_centre = searchItem.ItemValue1 ?? ""
            self.cost_centreText = searchItem.ItemValue2 ?? ""
        case "PO":
            self.poText = searchItem.ItemValue2 ?? ""
            self.poNumber = searchItem.ItemValue1 ?? ""
        case "WO":
            self.woNumber = searchItem.ItemValue1 ?? ""
            self.woText = searchItem.ItemValue2 ?? ""
        case "WBS":
            self.wbs = searchItem.ItemValue1 ?? ""
            self.wbsText = searchItem.ItemValue2 ?? ""
        case "SESApprover":
            self.sesApprover = searchItem.ItemValue1 ?? ""
        case "POForWO":
            self.poText = searchItem.ItemValue2 ?? ""
            self.poNumber = searchItem.ItemValue1 ?? ""
        default:
            print("error in didselect searchviewdelegate")
        }
        self.attestView.reloadData()
    }
    
}

extension AttestController : UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return TableLabelValues.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "FormCell")! as! FormCell
        cell.selectionStyle = .none
        cell.inputField.delegate = self
        cell.inputField.tag = indexPath.row
        cell.inputField.placeholder = ""
        switch TableLabelValues[indexPath.row] {
        case "Cost Object":
            if self.ticket?.serviceType != "Salt Water Disposal"{
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.setData(labelData : TableLabelValues[indexPath.row] + " *", fieldValue: self.Cost_Object)
                cell.inputField.placeholder = "--Select--"
            }
            else{
                cell.isUserInteractionEnabled = false
                cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
                cell.setData(labelData : TableLabelValues[indexPath.row] + " *", fieldValue: self.Cost_Object)
            }
            break
        case "PO":
            //cell.setData(labelData : TableLabelValues[indexPath.row], fieldValue: self.poText)
            if Cost_Object == "Purchase Order" {
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "--Select--"
                cell.setData(labelData: TableLabelValues[indexPath.row] + " *", fieldValue: self.poNumber)
            }
            else if Cost_Object == "Work Order" && self.woNumber != ""{
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "--Select--"
                cell.setData(labelData: TableLabelValues[indexPath.row], fieldValue: self.poText)
                if self.POforWOArray.count == 0 {
                    cell.inputField.placeholder = "--NoData--"
                }
                else if self.POforWOArray.count == 1{
                    cell.inputField.text = self.poNumber//self.POforWOArray[0].ItemValue2
                    //                    self.poNumber = self.POforWOArray[0].ItemValue1!
                    //                    self.poText = self.POforWOArray[0].ItemValue2!
                    cell.setData(labelData: TableLabelValues[indexPath.row], fieldValue: self.poNumber)
                }
                else{
                    cell.setData(labelData: TableLabelValues[indexPath.row], fieldValue: self.poNumber)
                }
            }
            break
        case "WO":
            cell.setData(labelData : TableLabelValues[indexPath.row], fieldValue: self.woNumber)
            if Cost_Object == "Work Order" {
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "--Select--"
                cell.setData(labelData: TableLabelValues[indexPath.row] + " *", fieldValue: self.woNumber)
            }
        case "Accounting Category":
            if isFetchedFromService && self.acc_category != "U" {
                cell.isUserInteractionEnabled = false
                cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
            }
            else{
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "--Select--"
            }
            cell.setData(labelData : TableLabelValues[indexPath.row], fieldValue: self.acc_category)
            break
        case "WBS":
            if Cost_Object == "WBS" {
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "##"
                cell.setData(labelData: TableLabelValues[indexPath.row] + " *", fieldValue: self.wbs)
                if self.wbs == "" {
                    cell.inputField.placeholder = "##"
                }
            }
            else if isFetchedFromService && ((Cost_Object == "Purchase Order" && self.acc_category == "P") || (Cost_Object == "Work Order"  && self.acc_category == "P")){
                cell.isUserInteractionEnabled = false
                cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
                cell.setData(labelData: TableLabelValues[indexPath.row], fieldValue: self.wbs)
            }
            else{
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "--Select--"
                cell.setData(labelData: TableLabelValues[indexPath.row] + " *", fieldValue: self.wbs)
                if self.wbs == "" {
                    cell.inputField.placeholder = "--Select--"
                }
            }
            break
            
        case "Cost Center":
            if Cost_Object == "Cost Center" {
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.setData(labelData: TableLabelValues[indexPath.row] + " *", fieldValue: self.cost_centre)
                if self.cost_centre == "" {
                    cell.inputField.placeholder = "--Select--"
                }
            }
            else if isFetchedFromService && ((Cost_Object == "Purchase Order" && self.acc_category == "K") || (Cost_Object == "Work Order"  && self.acc_category == "K")){
                cell.isUserInteractionEnabled = false
                cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
                cell.setData(labelData: TableLabelValues[indexPath.row] , fieldValue: self.cost_centre)
            }
            else{
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
                cell.inputField.placeholder = "--Select--"
                cell.setData(labelData: TableLabelValues[indexPath.row] + " *", fieldValue: self.cost_centre)
                if self.cost_centre == "" {
                    cell.inputField.placeholder = "--Select--"
                }
            }
            break
            
        case "SES Approver":
            cell.setData(labelData : TableLabelValues[indexPath.row] + " *", fieldValue: self.sesApprover)
            cell.isUserInteractionEnabled = true
            cell.contentView.backgroundColor = UIColor.white
            if self.sesApprover == "" {
                cell.inputField.placeholder = "--Select--"
            }
            break
        case "DigiSign":
            let Signcell = tableView.dequeueReusableCell(withIdentifier: "SignatureVerificationCell")! as! SignatureVerificationCell
            Signcell.selectionStyle = .none
            return Signcell
        default:
            print("Invalid Cell")
        }
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if TableLabelValues[indexPath.row] == "DigiSign"{
            return 430
        }
        else{
            return 65
        }
    }
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.attestView.endEditing(true)
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.title = "Verification"
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.title = ""
    }
    
}

extension AttestController{
    
    
    func resizeImage(image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size
        
        let widthRatio  = targetSize.width  / size.width
        let heightRatio = targetSize.height / size.height
        
        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width : size.width * heightRatio, height : size.height * heightRatio)
        } else {
            newSize = CGSize(width : size.width * widthRatio, height : size.height * widthRatio)
        }
        
        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRect(x : 0,y : 0, width : newSize.width,height : newSize.height)
        
        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }
    
    func resizeImageWithAspectWidth(image: UIImage,scaledToMaxWidth width:CGFloat,maxHeight height :CGFloat) -> UIImage
    {
        let oldWidth = image.size.width;
        let oldHeight = image.size.height;
        
        let scaleFactor = (oldWidth > oldHeight) ? width / oldWidth : height / oldHeight;
        
        let newHeight = oldHeight * scaleFactor;
        let newWidth = oldWidth * scaleFactor;
        let newSize = CGSize(width : newWidth, height : newHeight);
        
        return resizeImage(image: image, targetSize: newSize);
    }
    
    //        func resizeImageWithAspectHeight(image: UIImage,scaledToMaxWidth width:CGFloat,maxHeight height :CGFloat) -> UIImage
    //        {
    //            let oldWidth = image.size.width;
    //            let oldHeight = image.size.height;
    //
    //            let scaleFactor = (oldWidth > oldHeight) ? width / oldWidth : height / oldHeight;
    //
    //            let newHeight = oldHeight * scaleFactor;
    //            let newWidth = oldWidth * scaleFactor;
    //            let newSize = CGSize(width : newWidth, height : newHeight);
    //
    //            return resizeImage(image: image, targetSize: newSize);
    //        }
    
    func getSigning(image1: UIImage, image2: UIImage) -> UIImage {
        
        //        let convSize = CGSize(width : image2.size.width, height : 50)//image1.size.height * 4)
        //        let name = resizeImage(image: image1, targetSize: convSize)
        let size = CGSize(width : image1.size.width, height : image1.size.height + image2.size.height)
        
        UIGraphicsBeginImageContext(size)
        
        image1.draw(in: CGRect(x : 0,y : 0,width : size.width, height : image1.size.height))
        image2.draw(in: CGRect(x : 0,y : image1.size.height, width : size.width, height : image2.size.height))
        var finalImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return finalImage!
    }
    
    func getMixedImg(image1: UIImage, image2: UIImage) -> UIImage {
        
        //        let convSize = CGSize(width : image1.size.width, height : 300)//image2.size.height)
        //        let sign = resizeImage(image: image2, targetSize: convSize)
        let size = CGSize(width : image1.size.width, height : image1.size.height + image2.size.height)
        UIGraphicsBeginImageContext(size)
        
        image1.draw(in: CGRect(x : 0,y : 0,width : size.width, height : image1.size.height))
        image2.draw(in: CGRect(x : 0,y : image1.size.height, width : size.width, height : image2.size.height))
        let finalImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return finalImage!
    }
    
    func getFinalImage(image1: UIImage, image2: UIImage) -> UIImage {
        
        //        let convSize = CGSize(width : image1.size.width, height : 300)//image2.size.height)
        //        let sign = resizeImage(image: image2, targetSize: convSize)
        let size = CGSize(width : image1.size.width, height : image1.size.height + image2.size.height)
        UIGraphicsBeginImageContext(size)
        
        image1.draw(in: CGRect(x : 0,y : 0,width : size.width, height : image1.size.height))
        image2.draw(in: CGRect(x : 0,y : image1.size.height, width : size.width, height : image2.size.height))
        let finalImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return finalImage!
    }
    
    
    func getTicketNum() -> UIImage{
        let text = "   DFT # - \(ticket!.FieldTicketNum!)                                                                                    "
        let attributes = [
            NSAttributedString.Key.foregroundColor: UIColor.darkGray,
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 10
            )
        ]
        let textSize = text.size(withAttributes: attributes)
        
        let renderer = UIGraphicsImageRenderer(size: textSize)
        let image = renderer.image(actions: { context in
            let rect = CGRect(origin: .zero, size: textSize)
            text.draw(in: rect, withAttributes: attributes)
        })
        return image
    }
    
    func getMurphyRepName() -> UIImage{
        let text = "            Murphy Representative            "
        let attributes = [
            NSAttributedString.Key.foregroundColor: UIColor.darkGray,
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 20
            )
        ]
        let textSize = text.size(withAttributes: attributes)
        
        let renderer = UIGraphicsImageRenderer(size: textSize)
        let image = renderer.image(actions: { context in
            let rect = CGRect(origin: .zero, size: textSize)
            text.draw(in: rect, withAttributes: attributes)
        })
        return image
    }
    
    
    
    
    
    func getDisclaimer() -> UIImage{
        
        let htmlText = "<ul style = 'color : #868686 ; font-size : 12 ;'><li><i>All Commercial Terms are to be in accordance to <br>Murphy Oil MSA and originating Work Order.</i></li><li><i>Field/Wellsite Operations Stamp is a confirmation <br>of services/materials received only.</i></li></ul>"
        let attrStr = try! NSAttributedString(
            data: htmlText.data(using: String.Encoding.unicode, allowLossyConversion: true)!,
            options: [ .documentType: NSAttributedString.DocumentType.html],
            documentAttributes: nil)
        let disclaimerSize = attrStr.size()
        let attributes = [
            NSAttributedString.Key.foregroundColor: UIColor.lightGray,
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 12
            )
        ]
        
        let disclaimerRendered = UIGraphicsImageRenderer(size: disclaimerSize)
        let discImage = disclaimerRendered.image(actions: { context in
            let rect = CGRect(origin: .zero, size: disclaimerSize)
            attrStr.draw(in: rect)
        })
        return discImage
    }
    
    func fixedWidthConversion(image : UIImage, fixedWidth : CGFloat) -> UIImage{
        // let fixedWidth: CGFloat = 200
        let newHeight = fixedWidth * image.size.height / image.size.width
        let convertedImage = resizeImage(image: image, targetSize: CGSize(width: fixedWidth, height: newHeight))
        return convertedImage
    }
    
    func fixedHeightConversion(image : UIImage, fixedHeight : CGFloat) -> UIImage{
        // let fixedWidth: CGFloat = 200
        let newWidth = fixedHeight * image.size.width / image.size.height
        let convertedImage = resizeImage(image: image, targetSize: CGSize(width: newWidth, height: fixedHeight))
        return convertedImage
    }
    
    func getFullSignImage() -> UIImage{
        
        
        let signString = UserDefaults.standard.string(forKey: "digisign")
        let signData: Data = Data(base64Encoded: signString! , options: .ignoreUnknownCharacters)!
        // turn  Decoded String into Data
        let signImage = UIImage(data:signData,scale:1.0)
        //  let updatedDisclaimer = resizeImageWithAspectWidth(image: getDisclaimer(),scaledToMaxWidth: (signImage?.size.width)!,maxHeight: (signImage?.size.width)!)
        let updatedSignature = resizeImageWithAspectWidth(image: signImage!,scaledToMaxWidth: getDisclaimer().size.width,maxHeight: (signImage?.size.height)!)
        let discNsign = getMixedImg(image1: updatedSignature, image2: getDisclaimer())
        let updatedName = resizeImageWithAspectWidth(image: getMurphyRepName(),scaledToMaxWidth: discNsign.size.width,maxHeight: discNsign.size.height)
        let fullImage = getSigning(image1: updatedName, image2: discNsign)
        
        return fullImage
        //return discNsign
    }
    
    func resizeImageWithHeight(image: UIImage, newHeight: CGFloat) -> UIImage {
        
        let scale = newHeight / image.size.height
        let newWidth = image.size.width * scale
        UIGraphicsBeginImageContext(CGSize(width : newWidth,height : newHeight))
        image.draw(in : CGRect(x : 0, y : 0, width : newWidth,height : newHeight))
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }
    
    func createSignaturePDF() -> NSData? {
        
        let signString = UserDefaults.standard.string(forKey: "digisign")
        let signData: Data = Data(base64Encoded: signString! , options: .ignoreUnknownCharacters)!
        let signImage = UIImage(data:signData,scale:1.0)
        let sigResized = fixedWidthConversion(image: signImage!, fixedWidth: (signImage?.size.width)!/3)
        let leftOverWidth = (signImage?.size.width)! - sigResized.size.width
        let inset = UIEdgeInsets(top: 0, left: leftOverWidth/2, bottom: 0, right: leftOverWidth/2)
        let newImage = sigResized.imageWithInsets(insets: inset)
        let ticketAdd = fixedWidthConversion(image: getTicketNum(), fixedWidth: (newImage?.size.width)!)
        let murphyAdd = fixedWidthConversion(image: getMurphyRepName(), fixedWidth: (newImage?.size.width)!)
        print("nameAdd height \(String(describing: murphyAdd.size.height))")
        //            let signAdd = fixedWidthConversion(image: sigResized, fixedWidth: (dataImage?.size.width)!)
        print("signAdd height \(String(describing: newImage?.size.height))")
        let discAdd = fixedWidthConversion(image: getDisclaimer(), fixedWidth: (newImage?.size.width)!)
        let inter0 = getMixedImg(image1: ticketAdd, image2: murphyAdd)
        let inter1 = getMixedImg(image1: inter0, image2: newImage!)
        print("inter2 height \(String(describing: inter1.size.height))")
        var finalImage = getMixedImg(image1: inter1, image2: discAdd)
        let finalImageData = finalImage.jpeg(UIImage.JPEGQuality(rawValue: 0.75)!)
        finalImage = UIImage(data:finalImageData!,scale:0.3)!
        let finalImageCompressed = fixedWidthConversion(image: finalImage, fixedWidth: 595.2)
        //   let pdfData = createPDFDataFromImage(image: finalImage)
        
        let pdfData = NSMutableData()
        let pdfConsumer = CGDataConsumer(data: pdfData as CFMutableData)!
        
        var mediaBox = CGRect.init(x: 0, y: 0, width: finalImageCompressed.size.width, height: finalImageCompressed.size.height)
        
        let pdfContext = CGContext(consumer: pdfConsumer, mediaBox: &mediaBox, nil)!
        
        pdfContext.beginPage(mediaBox: &mediaBox)
        pdfContext.draw(finalImageCompressed.cgImage!, in: mediaBox)
        pdfContext.endPage()
        
        return pdfData
    }
    
    //    func createPDFDataFromImage(image: UIImage) -> NSMutableData {
    //        let pdfData = NSMutableData()
    //        let imgView = UIImageView.init(image: image)
    //        let imageRect = CGRect(x: 0, y: 0, width: image.size.width, height: image.size.height)
    //        UIGraphicsBeginPDFContextToData(pdfData, imageRect, nil)
    //        UIGraphicsBeginPDFPage()
    //        let context = UIGraphicsGetCurrentContext()
    //        imgView.layer.render(in: context!)
    //        UIGraphicsEndPDFContext()
    //
    //        //try saving in doc dir to confirm:
    //        let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).last
    //        let path = dir?.appendingPathComponent("file\(NSDate().timeIntervalSince1970).pdf")
    //
    //        do {
    //            try pdfData.write(to: path!, options: NSData.WritingOptions.atomic)
    //        } catch {
    //            print("error catched")
    //        }
    //
    //        return pdfData
    //    }
    
    
    func mergePdfFiles(sourcePdfFiles:NSData) -> String {
        
        let destPDFFile = createSignaturePDF()
        let testfile = sourcePdfFiles
        let date = Date().timeIntervalSince1970
        
        // Create final Url
        guard var finalUrl = (FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)).last
            
            else {
                return ""
        }
        print(finalUrl)
        finalUrl.appendPathComponent("final_\(date).pdf")
        //let mySource = "final_\(date).pdf"
        do {
            try testfile.write(to: finalUrl)
        } catch {
            //handle write error here
        }
        
        
        // Create source Url
        guard var sourceURL = (FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)).last
            
            else {
                return ""
        }
        print(sourceURL)
        sourceURL.appendPathComponent("source_\(date).pdf")
        do {
            try sourcePdfFiles.write(to: sourceURL)
        } catch {
            //handle write error here
        }
        // Create destination Url
        guard
            var destURL = (FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)).last
            else {
                return ""
        }
        print(destURL)
        destURL.appendPathComponent("dest_\(date).pdf")
        do {
            try destPDFFile?.write(to: destURL)
        } catch {
            //handle write error here
        }
        
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory = paths[0] as! NSString
        let pathForPDF = documentsDirectory.appending("/" + "final_\(date).pdf")
        guard UIGraphicsBeginPDFContextToFile(pathForPDF, CGRect.zero, nil) else {
            return ""
        }
        
        guard let destContext = UIGraphicsGetCurrentContext() else {
            return ""
        }
        
        let sourcePdfUrl = sourceURL as NSURL
        guard let pdfSourceRef = CGPDFDocument(sourcePdfUrl) else {
            // guard let pdfSourceRef = CGPDFDocument(sourcePdfUrl) else {
            return ""
        }
        for i in 1 ... pdfSourceRef.numberOfPages {
            if let page = pdfSourceRef.page(at: i) {
                var mediaBox = page.getBoxRect(.mediaBox)
                destContext.beginPage(mediaBox: &mediaBox)
                destContext.drawPDFPage(page)
                destContext.endPage()
            }
        }
        
        let pdfUrl = destURL as NSURL
        guard let pdfRef = CGPDFDocument(pdfUrl) else {
            return ""
        }
        for i in 1 ... pdfRef.numberOfPages {
            if let page = pdfRef.page(at: i) {
                var mediaBox = page.getBoxRect(.mediaBox)
                destContext.beginPage(mediaBox: &mediaBox)
                destContext.drawPDFPage(page)
                destContext.endPage()
            }
        }
        destContext.closePDF()
        UIGraphicsEndPDFContext()
        //let pdfFile = destPDFFile
        let pdfFile = NSData(contentsOf: finalUrl as URL)
        let pdfBase64:String = pdfFile!.base64EncodedString(options: NSData.Base64EncodingOptions.lineLength64Characters)
        return pdfBase64
    }
    
    
    func attestImagesAndUpdate()
    {
        for each in self.attachmentArray{
            
            let image = each.attachmentContent
            var data: NSData = NSData(base64Encoded: image , options: .ignoreUnknownCharacters)!
            // turn  Decoded String into Data
            let imageConst = Double(600000)/Double(data.length)
            print("converted Image \(data.length) bytes")
            //var size : Float?
            let contentNameSplit = each.attachmentName.components(separatedBy: ".")
            let contentType = contentNameSplit[(contentNameSplit.count) - 1]
            if contentType == "pdf" || contentType == "PDF"{
                data = NSData(base64Encoded: image , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters)!
                let pdfBase64 = mergePdfFiles(sourcePdfFiles: data)
                each.attachmentContent = pdfBase64
                each.attachmentLength = pdfBase64.lengthOfBytes(using: .utf8)
            }
            else{
                let dataImage = UIImage(data: data as Data)!
                var convertedData = Data()
                if imageConst >= 1.0{
                    convertedData = dataImage.jpeg(UIImage.JPEGQuality.highest)!
                }
                else if imageConst >= 0.75 && imageConst < 1.0{
                    convertedData = dataImage.jpeg(UIImage.JPEGQuality.high)!
                }
                else if imageConst >= 0.5 && imageConst < 0.75{
                    convertedData = dataImage.jpeg(UIImage.JPEGQuality.medium)!
                }
                else if imageConst >= 0.25 && imageConst < 0.5{
                    convertedData = dataImage.jpeg(UIImage.JPEGQuality.low)!
                }
                else if imageConst < 0.25{
                    convertedData = dataImage.jpeg(UIImage.JPEGQuality.lowest)!
                }
                print("converted Image \(convertedData.count) bytes")
                let convertedImage = UIImage(data: convertedData as Data)!
                
                let signString = UserDefaults.standard.string(forKey: "digisign")
                let signData: Data = Data(base64Encoded: signString! , options: .ignoreUnknownCharacters)!
                // turn  Decoded String into Data
                
                print("dataImage height \(String(describing: convertedImage.size.height))")
                // let oldSize = dataImage.des
                let signImage = UIImage(data:signData,scale:1.0)
                let sigResized = fixedHeightConversion(image: signImage!, fixedHeight: (convertedImage.size.height)/3)
                let leftOverWidth = (convertedImage.size.width) - sigResized.size.width
                let inset = UIEdgeInsets(top: 0, left: leftOverWidth/2, bottom: 0, right: leftOverWidth/2)
                let newImage = sigResized.imageWithInsets(insets: inset)
                let ticketAdd = fixedWidthConversion(image: getTicketNum(), fixedWidth: (newImage?.size.width)!)
                let murphyAdd = fixedWidthConversion(image: getMurphyRepName(), fixedWidth: (convertedImage.size.width))
                print("nameAdd height \(String(describing: murphyAdd.size.height))")
                //            let signAdd = fixedWidthConversion(image: sigResized, fixedWidth: (dataImage?.size.width)!)
                print("signAdd height \(String(describing: newImage?.size.height))")
                let discAdd = fixedWidthConversion(image: getDisclaimer(), fixedWidth: (convertedImage.size.width))
                let inter0 = getMixedImg(image1: convertedImage, image2: ticketAdd)
                //                let inter00 = getMixedImg(image1: inter0, image2: getDetailsTable())  //Added by Mohan
                let inter1 = getMixedImg(image1: inter0, image2: murphyAdd)
                print("inter1 height \(String(describing: inter1.size.height))")
                let inter2 = getMixedImg(image1: inter1, image2: newImage!)
                print("inter2 height \(String(describing: inter2.size.height))")
                let finalImage = getMixedImg(image1: inter2, image2: discAdd)
                
                //Added by Mohan
                //                UIImageWriteToSavedPhotosAlbum(finalImage, self, #selector(image(_:didFinishSavingWithError:contextInfo:)), nil)
                
                print("finalImage height \(String(describing: finalImage.size.height))")
                let imageData:NSData = finalImage.jpegData(compressionQuality: 1)! as NSData
                let signeCompressedImage = UIImage(data:imageData as Data)!
                var finalCompressedData = Data()
                let finalConst = Double(600000)/Double(imageData.length)
                if finalConst >= 1.0{
                    finalCompressedData = signeCompressedImage.jpeg(UIImage.JPEGQuality.highest)!
                }
                else if finalConst >= 0.75 && finalConst < 1.0{
                    finalCompressedData = signeCompressedImage.jpeg(UIImage.JPEGQuality.high)!
                }
                else if finalConst >= 0.5 && finalConst < 0.75{
                    finalCompressedData = signeCompressedImage.jpeg(UIImage.JPEGQuality.medium)!
                }
                else if finalConst >= 0.25 && finalConst < 0.5{
                    finalCompressedData = signeCompressedImage.jpeg(UIImage.JPEGQuality.low)!
                }
                else if finalConst < 0.25{
                    finalCompressedData = signeCompressedImage.jpeg(UIImage.JPEGQuality.lowest)!
                }
                let imageSize = finalCompressedData.count
                let value = ByteCountFormatter.string(fromByteCount: Int64(imageSize), countStyle: .file)
                print(value)
                let strBase64:String = finalCompressedData.base64EncodedString(options: .lineLength64Characters)
                
                each.attachmentContent = strBase64
                each.attachmentLength = strBase64.lengthOfBytes(using: .utf8)
            }
        }
        
        self.attachPost.removeAll()
        for element in self.attachmentArray{
            
            let item = ["Deleteflag":false,
                        "AttachmentLength" : String(element.attachmentLength),
                        "AttachmentName" : element.attachmentName,
                        "AttachmentContent" : element.attachmentContent,
                        "ServiceProviderId" : element.ServiceProviderId,
                        "AttachmentType" : element.attachmentType,
                        "CreatedBy" : element.createdBy,
                        "UpdatedBy" : element.updatedBy,
                        "CreatedByName":element.createdByName,
                        "CreatedOn":element.createdOn,
                        "FieldTicketNum":element.ticketNumber,
                        "AttachmentId":element.attachmentID,
                        "SharepointUrl":element.sharepointUrl
                ] as [String : Any]
            
            attachPost.append(item)
        }
    }
    func flushSelectedValues() {
        self.poNumber = ""
        self.poText = ""
        self.woNumber = ""
        self.woText = ""
        self.acc_category = ""
        self.wbs = ""
        self.wbsText = ""
        self.cost_centre = ""
        self.cost_centreText = ""
        // self.sesApprover = ""
        
    }
    func CheckMandatoryFields(){
        if self.sesApprover != "" && self.Cost_Object != "" {
            switch self.Cost_Object{
            case "Purchase Order":
                if self.poNumber != "" && self.acc_category != "U" {
                    self.MandatoryFieldsFilled = true
                }
                else{
                    self.MandatoryFieldsFilled = false
                }
            case "Work Order":
                if self.woNumber != "" && self.poNumber != "" && self.acc_category != "U"{
                    self.MandatoryFieldsFilled = true
                }
                else if self.woNumber != "" && self.poNumber == "" {
                    self.MandatoryFieldsFilled = true
                }
                else{
                    self.MandatoryFieldsFilled = false
                }
            case "WBS":
                if self.wbs != ""{
                    self.MandatoryFieldsFilled = true
                }
                else{
                    self.MandatoryFieldsFilled = false
                }
            case "Cost Center":
                if self.cost_centre != ""{
                    self.MandatoryFieldsFilled = true
                }
                else{
                    self.MandatoryFieldsFilled = false
                }
            default:
                print("Invalid Cost Object")
                
            }
        }
        else{
            self.MandatoryFieldsFilled = false
        }
    }
    
    
    //200
    func GetPOforWO() {
        
        self.loaderStart()
        self.poNumber = ""
        self.poText = ""
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/GetPObyWOSet?$filter=WO_Number eq'\(self.woNumber)'&$format=json"
        let encodedUrl = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if error == nil  {
                DispatchQueue.main.async {
                    do{
                        guard let data = data else { return }
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self!.POforWOArray.removeAll()
                                    for each in result{
                                        let element = PO(item:each)
                                        self!.POforWOArray.append(element)
                                    }
                                    if self!.POforWOArray.count == 1{
                                        self!.poNumber = self!.POforWOArray[0].ItemValue1 ?? ""
                                        self!.poText = self!.POforWOArray[0].ItemValue2 ?? ""
                                    }
                                    if self!.POforWOArray.count == 0{
                                        self!.TableLabelValues = ["Cost Object","WO","SES Approver" , "DigiSign"]
                                    }
                                    self!.loaderStop()
                                    self!.attestView.reloadData()
                                }
                            }
                            
                        }
                    }
                    catch{
                        print("x\(error.localizedDescription)")
                    }
                    
                }
                
            }
            else{
                DispatchQueue.main.async {
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                    self!.loaderStop()
                }
            }
        }
        task.resume()
     
        
    }

    
 //timeout error and now 404
    func signAttachment(){
        
       
        let urlString = "\(BaseUrl.apiURL)/com.mergefile/updateAttachmentContent?approverId=\((self.ticket?.murphyEngineerId!)!))&fieldTicketNo=\((self.ticket?.FieldTicketNum!)!)"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            guard self != nil else { return }
            if error == nil  {
                DispatchQueue.main.async {
                    do{
                        guard let data = data else { return }
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                        //print(JSON)
                        
                    }
                    catch{
                        print("x\(error.localizedDescription)")
                    }
                    
                }
                
            }
        }
        task.resume()
       
      
    }
    
    
    func encodeTime() -> String
    {
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm:ss"
        let formattedDate = formatter.string(from: date)
        var splitDate = formattedDate.components(separatedBy: ":")
        let returnTime = "PT"+splitDate[0]+"H"+splitDate[1]+"M"+splitDate[2]+"S"
        return returnTime
    }
    
    //200
    func getCSRFforCreateTicket(status : TicketStatus, action : ActionType)
    {
        self.loaderStart()
        let header = [ "x-csrf-token" : "fetch"]

        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        urlRequest.allHTTPHeaderFields = header
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async {
                    if let response = response as? HTTPURLResponse{
                        if let xcsrf = response.allHeaderFields["x-csrf-token"] as? String {
                            
                            self!.csrfToken = xcsrf
                            print(xcsrf)
                        }
                    }
                    if self!.csrfToken != nil{
                        self!.createTicketServiceCall(status : status, action: action)
                    }
                    else{
                        self!.csrfCount = self!.csrfCount + 1
                        self!.loaderStop()
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                            self!.dismissScreen()})
                        alertController.addAction(okAction)
                        self!.present(alertController, animated: true, completion: nil)
                        
                    }
                    
                }
            }
            else{
                DispatchQueue.main.async {
                    let alertController = UIAlertController.init(title: "", message:"Internet connection is not available" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                        self!.dismissScreen()})
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
       
    }
    
    //201
    func createTicketServiceCall(status : TicketStatus, action : ActionType)
    {
        if status == TicketStatus.Verify{
            isVerified = true
        }
        else{
            isVerified = false
            // self.attachPost = [[:]]
        }
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        let formattedDate = formatter.string(from: date)
        var commentArray = [[String : Any]]()
        if reason != ""{
            let commentPost = ["FiledTicketNum" : self.fieldTicketNumber!, "CommentedBy" : UserDefaults.standard.string(forKey: "id")!, "Comments" : reason, "CommentByName":UserDefaults.standard.string(forKey: "name")! ]
            commentArray.append(commentPost)
        }
        var activityArray = [[String : Any]]()
        let retNav = ["Type" : "", "Message" : ""]
        let activityPost = ["FieldTicketNum" : self.fieldTicketNumber!, "ActionType" : action.rawValue, "ProcessId" : "", "TaskId" : "", "Status" : status.rawValue , "UserId" : "", "CreatedBy" : UserDefaults.standard.string(forKey: "id")!, "UpdatedBy" : UserDefaults.standard.string(forKey: "id")!, "CreatedByName":UserDefaults.standard.string(forKey: "name")!]
        activityArray.append(activityPost)
        let saltwaterArray: [String: Any] = ["Quantity":ticket!.quantity,
                                             "Unit":ticket!.unit ]
        var postData : [String:Any]?
        
        postData = ["FieldTicketNum" : self.fieldTicketNumber!,
                    "TimeFlag" : ticket!.timeFlag! ,
                    "CreatedBy" : ticket!.createdBy! ,
                    "CreatedByName": ticket!.CreatedByName!,
                    "CreatedOn": ticket!.CreatedOn!,
                    "UpdatedBy" : UserDefaults.standard.string(forKey: "id")!,
                    "AribaSESNo" : ticket!.AribaSesNumber!,
                    "StartDate" : ticket!.startDate!,
                    "SESNumber":ticket!.sesNumber!,
                    "WorkOrderNo":self.woNumber,
                    "Service_Type": "SALTWATER",
                    "Field": ticket!.field!,
                    "FieldCode": ticket!.fieldCode!,
                    "Facility": ticket!.facility!,
                    "FacilityCode": ticket!.facilityCode!,
                    "WellPad": ticket!.wellPad!,
                    "WellpadCode":  ticket!.wellPadCode!,
                    "Well": ticket!.well!,
                    "WellCode": ticket!.wellCode!,
                    "EndDate": ticket!.endDate!,
                    "MurphyFieldReviewer":ticket!.murphyEngineerEmail!,
                    "MurphyFieldReviewerId":ticket!.murphyEngineerId!,
                    "MurphyFieldReviewerName": ticket!.murphyEngineerName!,
                    "ReviewedDate":formattedDate,
                    "ReviewedBy":UserDefaults.standard.string(forKey: "name")!,
                    "MurphySesApprover" : self.sesApprover,
                    "ServiceProviderId" : ticket!.ServiceProviderId!,
                    "ServiceProviderMail" : ticket!.ServiceProviderMail!,
                    "Department" : ticket!.Department!,
                    "Location" : ticket!.Location!,
                    "StartTime" : ticket!.startTime!,
                    "EndTime" : ticket!.endTime!,
                    "MurphyPoNumber" : self.poNumber,
                    "AccountingCategory" : self.acc_category,
                    "CostCenter" : self.cost_centre,
                    "VendorId" : ticket!.vendorID!,
                    "VendorAdminId" : ticket!.vendorAdminID!,
                    "VendorName" : ticket!.vendorName!,
                    "VendorAddress" : ticket!.vendorAddress!,
                    "Status" : status.rawValue ,
                    "UIcreationTime":ticket!.UIcreationTime!,
                    "UIcreatedOn":ticket!.UIcreatedOn!,
                    "UIupdatedOn":formattedDate,
                    "UIupdateTime":encodeTime(),
                    "Version":"",
                    "CreationTime":ticket!.CreationTime!,
                    "DeviceType":ticket!.deviceType!,
                    "DeviceId":ticket?.deviceId! ?? "",
                    "JobPerformedBy":ticket?.JobPerformedBy! ?? "",
                    "WbsElement":self.wbs,
                    "CostObject":self.Cost_Object,
                    "PO_Text": self.poText,
                    "WO_Text": self.woText,
                    "WBS_Text": self.wbsText,
                    "CC_Text": self.cost_centreText,
                    "MergeFlag":"UNMERGED",
                    "HEADERTOATTACHMENTNAV":[],
                    "HEADERTORETURNNAV" : retNav,
                    "HEADERTOCOMMENTNAV" : commentArray,
                    "HEADERTOACTIVITYNAV" : activityArray,
                    "HEADERTOSALTWATERNAV": saltwaterArray] as [String: Any]
        if status == TicketStatus.Reject{
            postData?.removeValue(forKey: "HEADERTOATTACHMENTNAV")
        }
        if ticket?.serviceType != "Salt Water Disposal"{
            postData!["Service_Type"] = ""
            postData?.removeValue(forKey: "HEADERTOSALTWATERNAV")
        }
        
        print(postData)
        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
        
        
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "post"
        do{
            let requestBody = try JSONSerialization.data(withJSONObject: postData, options: .fragmentsAllowed)
            urlRequest.httpBody = requestBody
        }
        catch{
            print("error in creating the data object from json")
        }
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async {
                
                    let xmlValue = String(data: data!, encoding: .utf8)
                    let data = xmlValue?.data(using: .utf8)
                    self!.parser = XMLParser(data: data!)
                    self!.parser.delegate = self
                    self!.parser.parse()
                    
                    if self!.responseType == "S"{
                        self!.workFlowTokenFetch()
                        var messageContent : String?
                        self!.signAttachment()
                        if self!.isVerified! {
                            messageContent = "Digital Field Ticket \(self!.ticketName!) Verified"
                        }
                        else{
                            messageContent = "Digital Field Ticket \(self!.ticketName!) Rejected"
                        }
                        let alertController = UIAlertController.init(title: "", message:messageContent , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                            self!.dismissScreen()
                            self!.senderController?.doubleDismiss()
                        })
                        alertController.addAction(okAction)
                        self!.present(alertController, animated: true, completion: nil)
                        
                    }
                    else{
                        var message : String?
                        if self!.responseMessage != nil && self!.responseMessage != ""{
                            message = self!.responseMessage
                        }
                        else{
                            message = self!.errorMessage
                        }
                        let alertController = UIAlertController.init(title: "", message: message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self!.present(alertController, animated: true, completion: nil)
                    }
                    
                }
                
            }
            else{
                DispatchQueue.main.async {
                    let alertController = UIAlertController.init(title: "", message: "Internet Connection is offline.Task failed, please retry." , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }
            DispatchQueue.main.async {
                self!.loaderStop()
            }
        }
        task.resume()
      
    }
    
    //200
    func workFlowTokenFetch()
    {
        let header = [ "x-csrf-token" : "fetch"]
        
        
        let urlString = "\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/xsrf-token"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async{
                    if let response = response as? HTTPURLResponse{
                        if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
                            
                            self!.csrfToken = xcsrf
                            print(xcsrf)
                        }
                    }
                    if self!.csrfToken != nil{
                        self!.updateWorkflowStatus()
                    }
                    else{
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed! rest" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                            self!.dismissScreen()})
                        alertController.addAction(okAction)
                        self!.present(alertController, animated: true, completion: nil)
                    }
                }
            }
            else{
                DispatchQueue.main.async{
                    let alertController = UIAlertController.init(title: "", message:"Internet connection is not available." , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                        self!.dismissScreen()})
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
   
        
    }

    // now 204
    func updateWorkflowStatus() {
        
        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
        
        var status : String?
        if isVerified! {
            status = "true"
        }
        else{
            status = "false"
        }
        let contextualData : [String : Any] = [
            "isApprovedByMurphyEngineer": status!,
            "comment": reason
        ]
        let postData = [ "status" : "COMPLETED", "context" : contextualData as [String : Any]] as [String : Any]
        print(postData)
        print(instanceID!)
        print ("\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/task-instances/\(instanceID!)")
        
        
        let urlString = "\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/task-instances/\(instanceID!)"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "PATCH"
        urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        do{
            let requestBody = try JSONSerialization.data(withJSONObject: postData, options: .fragmentsAllowed)
            urlRequest.httpBody = requestBody
        }
        catch{
            print("error in creating the data object from json")
        }
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary{
                            
                            let status = jsonDict.value(forKey: "status") as? String
                            if status == "RUNNING"{
                                self.restBanner = NotificationBanner(title: "Workflow triggered", subtitle: "", style: .success)
                            }
                            else{
                                self.restBanner = NotificationBanner(title: "Error received", subtitle: "status \(status)", style: .danger)
                            }
                            
                            self.restBanner.show()
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
                }
            }else{
                DispatchQueue.main.async{
                    let message = error!.localizedDescription
                    self.restBanner = NotificationBanner(title: "Error received", subtitle: message, style: .info)
                    self.restBanner.show()
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
        
   
       
        
    }
}

extension UIImage {
    func imageWithInsets(insets: UIEdgeInsets) -> UIImage? {
        UIGraphicsBeginImageContextWithOptions(
            CGSize(width: self.size.width + insets.left + insets.right,
                   height: self.size.height), false, self.scale)
        let _ = UIGraphicsGetCurrentContext()
        let origin = CGPoint(x: insets.left, y: insets.top)
        self.draw(at: origin)
        let imageWithInsets = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return imageWithInsets
    }
}

extension UIImage {
    enum JPEGQuality: CGFloat {
        case lowest  = 0
        case low     = 0.25
        case medium  = 0.5
        case high    = 0.75
        case highest = 1
    }
    
    /// Returns the data for the specified image in JPEG format.
    /// If the image object’s underlying image data has been purged, calling this function forces that data to be reloaded into memory.
    /// - returns: A data object containing the JPEG data, or nil if there was a problem generating the data. This function may return nil if the image has no data or if the underlying CGImageRef contains data in an unsupported bitmap format.
    func jpeg(_ quality: JPEGQuality) -> Data? {
        return self.jpegData(compressionQuality: quality.rawValue)
    }
}
