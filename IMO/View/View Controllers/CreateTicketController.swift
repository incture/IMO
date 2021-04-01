//
//  CreateTicketController.swift
//  DFT
//
//  Created by Soumya Singh on 31/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import Photos
import SAPFiori
import NohanaImagePicker
import NotificationBannerSwift
import NotificationCenter

enum Department : String {
    
    case Operations = "Operations"
    case Maintenance = "Maintenance"
    case Well_Work = "Well Work"
    case Capital_projects = "Capital Projects"
    case Drilling = "Drilling"
    case Completions = "Completions"
    case UnknownType 
    
    static let allValues = [Operations, Maintenance, Well_Work, Capital_projects, Drilling, Completions]
    static let displayValues = ["Operations", "Maintenance", "Well Work", "Capital Projects", "Drilling", "Completions"]
    
    init(value : String?){
        if let unwrappedValue = value{
            if let unwrappedType = Department(rawValue: unwrappedValue){
                self = unwrappedType
            }
            else{
                self = .UnknownType
            }
        }
        else{
            self = .UnknownType
        }
    }
    
}


class CreateTicketController: UIViewController, UITextViewDelegate, UITextFieldDelegate, NohanaImagePickerControllerDelegate, XMLParserDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var segmentControl: UISegmentedControl!
    @IBOutlet var CommentsTextView: UITextView!
    @IBOutlet var attachmentView: UIView!
    @IBOutlet var tableView: UITableView!
    @IBOutlet var galleryView: UICollectionView!
    @IBOutlet var tableBottomConstraint: NSLayoutConstraint!
    @IBOutlet var submitButton: UIButton!
    @IBOutlet var conditionLabel: UILabel!
    
    var picker = UIPickerView()
    var locationPicker = UIPickerView()
    var FacilityPicker = UIPickerView()
    var MurphyReviewerPicker = UIPickerView()
    var datePicker = UIDatePicker()
    var timePicker = UIDatePicker()
    var toolBar = UIToolbar()
    var toolBarDate = UIToolbar()
    var toolBarTime = UIToolbar()
    var toolBarLocation = UIToolbar()
    var toolBarReviewer = UIToolbar()
    var toolBarFacility = UIToolbar()
    var csrfToken : String?
    var selectedImages = [PHAsset]()
    var convertedImages = [UIImage]()
    var imageCount : Int = 0
    var locationView = UIView()
    var facilityView = UIView()
    var MurphyReviewerView = UIView()
    var bgView = UIView()
    var datebgView = UIView()
    var timebgView = UIView()
    var rules = [Rules]()
    var isUpdated : Bool = false
    
    var selectedLocation : String = ""
    var selectedFacility : String = ""
    var selectedMurphyReviewer = MurphyEngineer()
    var selectedReviewersName : String = ""
    var locations = [String]()
    var facility = [String]()
    var attachments = [Attachment]()
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    
    //Mohan
    var selectedVendorRefNo : String = ""
    var selectedField : HierarchyItem? = nil
    var selectedFacility2 : HierarchyItem? = nil
    var selectedWellPad : HierarchyItem? = nil
    var selectedWell : HierarchyItem? = nil
    var selectedDepartment : String = ""
    var serviceTypeList : [String] = []{
        didSet{
            if serviceTypeList.count > 0{
                selectedServiceType = serviceTypeList[0]
            }else{
                selectedServiceType = ""
            }
        }
    }
   // var pickerSelectedTemp : String? = nil
    var selectedServiceType : String = ""{
        didSet{
            switch selectedServiceType {
            case "General DFT":
                self.TableLabels = ["Vendor Ref No *","Service Type *", "Department *", "Location *","Murphy Reviewer *" ,"Start Date", "End Date"]
            case "Salt Water Disposal":
                self.TableLabels = ["Vendor Ref No *","Service Type *", "Department *", "Location *","Murphy Reviewer *" , "Quantity *", "Unit *","Start Date", "End Date"]
            default:
                self.TableLabels = ["Vendor Ref No *","Service Type *", "Department *", "Location *","Murphy Reviewer *" ,"Start Date", "End Date"]
            }
            
            self.tableView.reloadData()
        }
    }
    
    var TableLabels = ["Vendor Ref No *", "Department *", "Location *","Murphy Reviewer *" ,"Create Date","Start Date", "End Date"]
    
    var value = ["", "", "", "", "", "", "","", ""]
    var selectedIndex : Int = 0
    var createdDate : String = ""
    var createdTime : String = ""
    var startDate : String = ""{
        didSet{
            self.tableView.reloadData()
        }
    }
    var enddate : String = ""{
        didSet{
            self.tableView.reloadData()
        }
    }
    var startTime : String = ""{
        didSet{
            self.tableView.reloadData()
        }
    }
    var endTime : String = ""{
        didSet{
            self.tableView.reloadData()
        }
    }
    var startDateStamp = Date()
    var endDateStamp = Date()
    var startTimeStamp = Date()
    var endTimeStamp = Date()
    var UIstartDate : String = ""
    var UIendDate : String = ""
    var UIstartTime : String = ""
    var UIendTime : String = ""
    var errorMessage : String?
    var editingBegan : Bool = false
    var reviewers = [MurphyEngineer]()
    var reviewersName = [String]()
    let currentTicket = CreateTicket()
    var parser = XMLParser()
    var element = NSString()
    var ticketName : String?
    var workFlowCount = 0
    var pickedCameraImage = UIImage()
    var imagePickerController = UIImagePickerController()
    var postData = [String : Any]()
    let context = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
    var isGalleryOpenedOnce : Bool = false
    var timeFlag : String = "B"
    var responseType : String?
    var responseMessage : String?
    var csrfCount : Int?
    var restCsrfCount : Int?
    var departmentValues = [String]()
    let reachability = Reachability()
    var restBanner = NotificationBanner(title: "Background sync completed", subtitle: "", style: .success)
    
    @IBOutlet var statusBarHeightConstraint: NSLayoutConstraint!
    @IBOutlet var tableViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet var attachmentViewHeightConstraint: NSLayoutConstraint!
    
    
    // MARK:- Default
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        createNavBar()
        CommentsTextView.text = "Add optional comments if necessary"
        CommentsTextView.textColor = UIColor.lightGray
        createdDate = ""
        createdTime = ""
        startDate = ""
        enddate = ""
        endTime = ""
        let formNib = UINib(nibName: "FormCell", bundle: nil)
        tableView.register(formNib, forCellReuseIdentifier: "FormCell")
        let form2Nib = UINib(nibName: "Form1Cell", bundle: nil)
        tableView.register(form2Nib, forCellReuseIdentifier: "Form1Cell")
        let addNib = UINib(nibName: "AddCell", bundle: nil)
        galleryView.register(addNib, forCellWithReuseIdentifier: "AddCell")
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = 50
        tableView.tableFooterView = UIView()
        attachmentView.isHidden = true
        tableBottomConstraint.constant = 0
        submitButton.isHidden = true
        submitButton.isEnabled = false
        tableView.isHidden = false
        conditionLabel.isHidden = true
        departmentValues = UserDefaults.standard.array(forKey: "department") as! [String]
        let font = UIFont.systemFont(ofSize: 16)
        segmentControl.setTitleTextAttributes([NSAttributedStringKey.font: font],
                                              for: .normal)
        self.segmentControl.layer.cornerRadius = 0
        NotificationCenter.default.addObserver(self, selector: #selector(self.changeBar),name: .reachabilityChanged,object: reachability)
        do{
            try reachability?.startNotifier()
        }catch{
            print("could not start reachability notifier")
        }
        CommentsTextView.delegate = self
        CommentsTextView.layer.borderWidth = 0.5
        CommentsTextView.layer.borderColor = UIColor.lightGray.cgColor
        imagePickerController.delegate = self
        segmentControl.layer.cornerRadius = 0.0
        segmentControl.layer.borderColor = UIColor.white.cgColor
        segmentControl.layer.borderWidth = 2.0
        segmentControl.layer.masksToBounds = true
        galleryView.contentInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        tableView.keyboardDismissMode = UIScrollView.KeyboardDismissMode.onDrag
        convertedImages.removeAll()
        
        
        if let tem = UserDefaults.standard.array(forKey: "serviceTypesList"){
            serviceTypeList = tem  as! [String]
            print("service types available \(serviceTypeList)")
        }else {
            print("No serviceType")
            serviceTypeList = []
        }
        if serviceTypeList.count == 1 && serviceTypeList[0] == "General DFT"{
            self.TableLabels = ["Vendor Ref No *", "Department *", "Location *","Murphy Reviewer *" ,"Start Date", "End Date"]
            self.tableView.reloadData()
        }
        NotificationCenter.default.addObserver(self, selector: #selector(didSelectLocation(_:)), name: Notification.Name("didSelectLocation"), object: nil )
        let titleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor.black]
        let selectTitleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor.white]
        
        segmentControl.setTitleTextAttributes(titleTextAttributes, for: .normal)
        segmentControl.setTitleTextAttributes(selectTitleTextAttributes, for: .selected)
              
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.tableView.reloadData()
    }
    override func viewWillAppear(_ animated: Bool) {
        self.view.endEditing(true)
        self.title = "Create DFT"
    }
    override func viewWillDisappear(_ animated: Bool) {
        self.title = ""
        self.view.window?.endEditing(true)
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
     // MARK:- Loader classes
    
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
    
    // MARK:- Location Selected
    
    @objc func didSelectLocation(_ notification:Notification)  {
        let notifObj = notification.object as! [String: AnyObject?]
        self.currentTicket.Location = (notifObj["CompleteLocation"] as? String)!
        var concatLocation = ""
        if let temp = notifObj["Field"]! {
            self.selectedField = temp as? HierarchyItem
            concatLocation = concatLocation + self.selectedField!.ItemValue1!
        }
        if let temp = notifObj["Facility"]! {
            self.selectedFacility2 = temp as? HierarchyItem
            concatLocation = concatLocation + "/ " + self.selectedFacility2!.ItemValue1!
        }
        if let temp = notifObj["WellPad"]! {
            self.selectedWellPad = temp as? HierarchyItem
            concatLocation = concatLocation + "/ " + self.selectedWellPad!.ItemValue1!
        }
        if let temp = notifObj["Well"]! {
            self.selectedWell = temp as? HierarchyItem
            concatLocation = concatLocation + "/ " + self.selectedWell!.ItemValue1!
        }
        if selectedWell?.ItemValue1 != nil{
            concatLocation = selectedField!.ItemValue1! + "/../../ " + selectedWell!.ItemValue1!
        }
        else if selectedWell?.ItemValue1 == nil && selectedWellPad?.ItemValue1 != nil{
            concatLocation = selectedField!.ItemValue1! + "/../ " + selectedWellPad!.ItemValue1!
        }
        else if selectedWell?.ItemValue1 == nil && selectedWellPad?.ItemValue1 == nil && selectedFacility2?.ItemValue1 != nil{
            concatLocation = selectedField!.ItemValue1! + "/ " + selectedFacility2!.ItemValue1!
        }
        else if selectedWell?.ItemValue1 == nil && selectedWellPad?.ItemValue1 == nil && selectedFacility2?.ItemValue1 != nil && selectedField?.ItemValue1 != nil{
            concatLocation = selectedField!.ItemValue1!
        }
        self.selectedLocation = concatLocation
        self.selectedMurphyReviewer = MurphyEngineer()
        currentTicket.Location = concatLocation
        self.tableView.reloadData()
    }
    
    // MARK:- Basic UI manipulations
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "Create DFT"
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
        
    }
    
    @objc func dismissScreen()
    {
        let alertController = UIAlertController.init(title: nil, message:"Are you sure you want to navigate back?" , preferredStyle: UIAlertController.Style.alert)
        let YesAction = UIAlertAction.init(title: "Yes", style: UIAlertAction.Style.default, handler: { action in
            self.dismiss(animated: true, completion: nil)
        })
        alertController.addAction(YesAction)
        let NoAction = UIAlertAction.init(title: "No", style: UIAlertAction.Style.cancel, handler: nil)
        alertController.addAction(NoAction)
        self.present(alertController, animated: true, completion: nil)
        
    }
    
    @objc func changeBar(){
        if ConnectionCheck.isConnectedToNetwork(){
            statusBarHeightConstraint.constant = 0
            tableViewHeightConstraint.constant = 0
            attachmentViewHeightConstraint.constant = 0
            
        }
        else{
            statusBarHeightConstraint.constant = 20
            tableViewHeightConstraint.constant = 20
            attachmentViewHeightConstraint.constant = 20
        }
        self.view.setNeedsLayout()
    }
    
     // MARK:- Open Location Hierarchy
    func openHierarchy(){
        //self.view.endEditing(true)
        self.selectedField = nil
        self.selectedFacility2 = nil
        self.selectedWellPad = nil
        self.selectedWell = nil
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : HierarchyViewController = storyboard.instantiateViewController(withIdentifier: "HierarchyViewController") as! HierarchyViewController
        
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.location = "MUR-US-EFS"
        vc2.locationtype = "BASE"
        vc2.navigate = "CHILD"
        if ConnectionCheck.isConnectedToNetwork(){
            self.navigationController?.pushViewController(vc2, animated: true)
        }
        else{
            let locationService = LocationService(context:self.context)
            let predicate1 = NSPredicate(format: "locationType == %@", "Field")
            let predicate2 = NSPredicate(format: "locationName == %@", "")
            let searchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2]
            )
            let dbResponse = locationService.get(withPredicate: searchPredicate)
            if dbResponse.count > 0{
                self.navigationController?.pushViewController(vc2, animated: true)
            }
            else{
                let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
        }
        
    }
    
     // MARK:- TextView Delegates
    func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.textColor == UIColor.lightGray {
            textView.text = nil
            textView.textColor = UIColor.black
        }
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if text == "\n" {
            if textView.text == ""{
                CommentsTextView.textColor = UIColor.lightGray
                CommentsTextView.text = "Add optional comments if necessary"
            }
            else if let str = textView.text {
                if str.count > 256{
                    let alertController = UIAlertController.init(title: "", message:"Comment can't be more than 256 characters!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                    CommentsTextView.text = str.truncate(length: 256)
                    
                }
            }
            textView.resignFirstResponder()
            return false
        }
        return true
    }
    
    
     // MARK:- TextField Delegates
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    

    func textFieldDidBeginEditing(_ textField: UITextField) {
        
        if textField.tag < 100{
            let celltype = TableLabels[textField.tag]
            
            switch celltype {
                
            case "Vendor Ref No *":
                print("")
                break
            case "Service Type *" ,"Department *":
//                picker.reloadAllComponents()
//                textField.inputView = bgView
//                bgView.tag = textField.tag
                
                break
            case "Location *":
                
//                textField.resignFirstResponder()
//                textField.endEditing(true)
                
                self.openHierarchy()
                break
            case "Murphy Reviewer *":
                if self.selectedLocation != "" && self.selectedDepartment != ""{
                    textField.resignFirstResponder()
                    textField.endEditing(true)
                    self.openSearchReviewer()
                }
                else{
                    let alertController = UIAlertController.init(title: "", message:"Please select the Department and Location to proceed!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                break
            case "Quantity *":
                textField.keyboardType = .decimalPad
                break
            default:
                print("Error in textfielddidbeginediting")
            }
            
        }
        else{
            
            if textField.tag == 101 || textField.tag == 201{
//                textField.inputView = datebgView
//                datebgView.tag = textField.tag
            }else if textField.tag == 102 || textField.tag == 202{
//                textField.inputView = timebgView
//                timebgView.tag = textField.tag
            }
            //textField.becomeFirstResponder()
        }
        
    }
    
    internal func textFieldDidEndEditing(_ textField: UITextField, reason: UITextField.DidEndEditingReason) {
        
        if textField.tag < 100 {
            
            let celltype = TableLabels[textField.tag]
            
            switch celltype {
            case "Vendor Ref No *":
                if let str = textField.text {
                    if str.count > 16{
                        textField.text = str.truncate(length: 16)
                        value[textField.tag] = textField.text!
                        let alertController = UIAlertController.init(title: "", message:"Vendor refrence No. can't be more than 16 characters!" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    else{
                        value[textField.tag] = textField.text!
                        self.selectedVendorRefNo = textField.text!
                    }
                }
                break
            case "Quantity *":
                if let str = textField.text {
                    if str != "" && (str.count < 1 || !isDecimalNumber(testStr: str) || Double(str)! <= 0.0) {
                        
                        let alertController = UIAlertController.init(title: "", message:"Please enter valid data" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    else{
                        value[textField.tag] = textField.text!
                        self.currentTicket.Quantity = textField.text!
                    }
                }
            case "Service Type *" ,"Department *":
                break
                
            case "Location *":
                
                //self.openHierarchy()
                break
                
            default:
                value[textField.tag] = textField.text!
            }
        }
        else{
            print("Handled!")
        }
       // textField.resignFirstResponder()
        textField.endEditing(true)
    }
    
    
     // MARK:- XML parser delegates
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
    
    
     // MARK:- Photo Upload functionality
    func openGallery()
    {
        if isGalleryOpenedOnce == false{
            checkIfAuthorizedToAccessPhotos { isAuthorized in
                DispatchQueue.main.async(execute: {
                    if isAuthorized {
                        self.isGalleryOpenedOnce = true
                        self.openOtherGallery()
                    } else {
                        let alert = UIAlertController(title: "Error", message: "Denied access to photos.", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                    }
                })
            }
            
        }
        else{
            self.openOtherGallery()
        }
    }
    
    func openCamera()
    {
        if convertedImages.count < 3{
            self.imagePickerController.sourceType = UIImagePickerController.SourceType.camera
            self.imagePickerController.allowsEditing = true
            self.present(self.imagePickerController, animated : true, completion : nil)
        }
        else{
            let alertController = UIAlertController.init(title: "", message:"Maximum 3 attachments allowed." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]){
        pickedCameraImage = info[.editedImage]  as! UIImage
        self.dismiss(animated: true) { () -> Void in
            self.convertedImages.append(self.pickedCameraImage)
            self.galleryView.reloadData()
        }
    }
    
    public func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        self.dismiss(animated: true)
    }
    
    func attachmentOptions(){
        
        let attribute : [NSAttributedStringKey : Any] = [NSAttributedStringKey.font: UIFont.boldSystemFont(ofSize: 16),
                                                         NSAttributedStringKey.foregroundColor : UIColor.darkGray]
        
        let attributedText = NSAttributedString(string: "Attachment Options", attributes: attribute)
        let alertController = UIAlertController(title: "", message: nil, preferredStyle: .actionSheet)
        alertController.setValue(attributedText, forKey: "attributedTitle")
        
        let camera = UIAlertAction(title: "Open Camera", style: .default, handler: { (action) -> Void in
            
            self.openCamera()
        })
        
        
        let gallery_images = UIAlertAction(title: "Open gallery", style: .default, handler: { (action) -> Void in
            
            self.openGallery()
            
        })
        
        let cancel = UIAlertAction(title: "Cancel", style: .cancel , handler: { (action) -> Void in
            
            print("Ok button tapped")
        })
        alertController.addAction(camera)
        alertController.addAction(gallery_images)
        alertController.addAction(cancel)
        
        self.navigationController!.present(alertController, animated: true, completion: nil)
        
    }
    
    func checkIfAuthorizedToAccessPhotos(_ handler: @escaping (_ isAuthorized: Bool) -> Void) {
        switch PHPhotoLibrary.authorizationStatus() {
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization { status in
                DispatchQueue.main.async {
                    switch status {
                    case .authorized:
                        handler(true)
                    default:
                        handler(false)
                    }
                }
            }
        case .restricted:
            handler(false)
        case .denied:
            handler(false)
        case .authorized:
            handler(true)
        default :
            return
        }
    }
    
    func openOtherGallery(){
        
        
        let imagePicker = NohanaImagePickerController()
        // Set the maximum number of selectable images
        let maxcount = 3
        let selectedCount = self.convertedImages.count
        imagePicker.maximumNumberOfSelection = maxcount - selectedCount
        
        if maxcount - selectedCount == 0{
            let alertController = UIAlertController.init(title: "", message:"Maximum 3 attachments allowed." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        else{
            
            // Set the cell size
            imagePicker.numberOfColumnsInPortrait = 2
            imagePicker.numberOfColumnsInLandscape = 3
            
            // Show Moment
            imagePicker.shouldShowMoment = true
            imagePicker.shouldShowEmptyAlbum = false
            
            // Hide toolbar
            // imagePicker.shouldShowEmptyAlbum = true
            // Disable to pick asset
            imagePicker.canPickAsset = { (asset:Asset) -> Bool in
                return true
            }
            
            imagePicker.delegate = self
            self.present(imagePicker, animated: true, completion: nil)
        }
    }
    
    func nohanaImagePickerDidCancel(_ picker: NohanaImagePickerController) {
        
        picker.dismiss(animated: true, completion: nil)
    }
    
    func nohanaImagePicker(_ picker: NohanaImagePickerController, didFinishPickingPhotoKitAssets pickedAssts :[PHAsset]) {
        self.selectedImages.removeAll()
        self.selectedImages.append(contentsOf: pickedAssts)
        convertToUIImage()
        //        for each in pickedAssts{
        //
        //        }
        picker.dismiss(animated: true, completion: nil)
    }
    
    
    
    func convertToUIImage(){
        //convertedImages.removeAll()
        for images in selectedImages{
            
            let image = getAssetThumbnail(asset: images)
            convertedImages.append(image)
        }
        self.galleryView.reloadData()
    }
    
    func getAssetThumbnail(asset: PHAsset) -> UIImage {
        
        let options = PHImageRequestOptions()
        options.deliveryMode = PHImageRequestOptionsDeliveryMode.highQualityFormat
        options.isSynchronous = true
        options.isNetworkAccessAllowed = true
        var thumbnail = UIImage()
        
        options.progressHandler = {  (progress, error, stop, info) in
            print("progress: \(progress)")
        }
        
        PHImageManager.default().requestImage(for: asset, targetSize: view.frame.size, contentMode: PHImageContentMode.aspectFit, options: options, resultHandler: {
            (image, info) in
            thumbnail = image!
            
            //            print("dict: \(String(describing: info))")
            //            print("image size: \(String(describing: image?.size))")
        })
        //        let manager = PHImageManager.default()
        //        let option = PHImageRequestOptions()
        //        var thumbnail = UIImage()
        //        option.isSynchronous = true
        //CGSize(width: 20, height: 20)
        //        manager.requestImage(for: asset, targetSize: CGSize(width: 20, height: 20), contentMode: .aspectFit, options: option, resultHandler: {(result, info)->Void in
        //            thumbnail = result!
        //        })
        return thumbnail
    }
    
     // MARK:- Submit Button Press
    
    @IBAction func submitButtonPressed(_ sender: UIButton) {
        
        getFormValues()
        
    }
     // MARK:- Segment Control Switch
    @IBAction func onSegmentSwitch(_ sender: UISegmentedControl) {
        
        if sender.selectedSegmentIndex == 0{
            
            attachmentView.isHidden = true
            tableBottomConstraint.constant = 0
            submitButton.isHidden = true
            submitButton.isEnabled = false
            tableView.isHidden = false
            conditionLabel.isHidden = true
            self.view.endEditing(true)
        }
        else{
            attachmentView.isHidden = false
            tableBottomConstraint.constant = 60
            submitButton.isHidden = false
            submitButton.isEnabled = true
            tableView.isHidden = true
            conditionLabel.isHidden = false
            self.view.endEditing(true)
        }
    }
    
     // MARK:- Get value for ticket Creation
    func getAttachments()
    {
        
        // self.convertToUIImage()
        self.attachments.removeAll()
        
        if convertedImages.count == 0{
            
            let alertController = UIAlertController.init(title: "", message:"Please add minimum of 1 Attachment." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        else{
            var count = 0
            for image in convertedImages{
                
                let attachment = Attachment()
                let imageData:NSData = image.jpegData(compressionQuality: 0.2)! as NSData
                let imageSize = imageData.length
                let value = ByteCountFormatter.string(fromByteCount: Int64(imageSize), countStyle: .file)
                print(value)
                let strBase64:String = imageData.base64EncodedString(options: .lineLength64Characters)
                attachment.attachmentContent = strBase64
                attachment.attachmentLength = strBase64.lengthOfBytes(using: .utf8)
                attachment.attachmentName = UserDefaults.standard.string(forKey: "id")! + "_" + String(describing: count) + "_" + String(describing: Date()) + ".jpeg"
                attachment.attachmentType = "image/jpeg"
                attachment.ServiceProviderId = UserDefaults.standard.string(forKey: "id")!
                attachments.append(attachment)
                count += 1
            }
            createPayload()
            
        }
    }
    
    func getFormValues()
    {
        
            currentTicket.CreatedDate = createdDate
            currentTicket.CreationTime = createdTime
        
            if startTime == "" && endTime == ""{
                timeFlag = "X"
            }
            else if startTime != "" && endTime == ""{
                timeFlag = "S"
            }
            else if startTime == "" && endTime != ""{
                timeFlag = "E"
            }
            else if startTime != "" && endTime != ""{
            }
        
        for i in 0...TableLabels.count-1 {
            switch TableLabels[i] {
            case "Vendor Ref No *":
                currentTicket.VendorRefNo = self.selectedVendorRefNo
            case "Service Type *":
                currentTicket.ServiceType = self.selectedServiceType
            case "Department *":
                currentTicket.Department = self.selectedDepartment
            case "Location *":
                currentTicket.Location = self.selectedLocation
            case "Murphy Reviewer *":
                currentTicket.MurphyReviewer = self.selectedMurphyReviewer
            case "Quantity *":
                print("")
                //currentTicket.Quantity = self.selectedServiceType
            case "Unit *":
                currentTicket.Unit = "BBL"
            case "Job Performed By *":
                currentTicket.jobPerformedBy = UserDefaults.standard.string(forKey: "id")!
                
            default:
                print("error in  GetformValues Method")
            }
        }
        if currentTicket.VendorRefNo == "" || currentTicket.Department == "" || currentTicket.startDate == "" || currentTicket.endDate == "" || currentTicket.MurphyReviewer.emailID == ""{
            
            let alertController = UIAlertController.init(title: "", message:"Please fill mandatory fields." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
            
        }
        else{
            if timeFlag == "X"{
                startTime = "PT00H00M00S"
                endTime = "PT00H00M00S"
            }
            else if timeFlag == "S"{
                endTime = "PT00H00M00S"
            }
            else if timeFlag == "E"{
                startTime = "PT00H00M00S"
            }
            currentTicket.startTime = startTime
            currentTicket.endTime = endTime
            getAttachments()
        }
    }
    
}

// MARK: - TableView Delegate
extension CreateTicketController : UITableViewDelegate, UITableViewDataSource{
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return TableLabels.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellType = TableLabels[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "FormCell")! as! FormCell
        cell.preservesSuperviewLayoutMargins = false
        cell.separatorInset = UIEdgeInsets.zero
        cell.layoutMargins = UIEdgeInsets.zero
        cell.inputField.delegate = self
        cell.selectionStyle = .none
        cell.contentView.backgroundColor = UIColor.white
        cell.inputField.isUserInteractionEnabled = true
        cell.inputField.textColor = UIColor.black
        cell.inputField.placeholder = "--Select--"
        cell.inputField.tag = indexPath.row
        
        let cell2 = tableView.dequeueReusableCell(withIdentifier: "Form1Cell")! as! Form1Cell
        cell2.preservesSuperviewLayoutMargins = false
        cell2.separatorInset = UIEdgeInsets.zero
        cell2.layoutMargins = UIEdgeInsets.zero
        cell2.dateValue.delegate = self
        cell2.timeValue.delegate = self
        cell2.selectionStyle = .none
        
        
        switch cellType {
            
        case "Vendor Ref No *":
            cell.inputField.placeholder = "(EgABC490)"
            cell.setData(labelData: "Vendor Ref No *", fieldValue: self.selectedVendorRefNo)
            break
        case "Service Type *":
            cell.setData(labelData: "Service Type *", fieldValue: self.selectedServiceType )
            if serviceTypeList.count == 1{
                cell.isUserInteractionEnabled = false
                cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
            }
            else{
                cell.inputField.isDropDown = true
                cell.inputField.dropDownSource = serviceTypeList
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
            }
            break
        case "Department *":
            cell.inputField.isDropDown = true
            cell.inputField.dropDownSource = departmentValues
            cell.setData(labelData: "Department *", fieldValue: self.selectedDepartment)
            break
        case "Location *":
            cell.setData(labelData: "Location *", fieldValue: self.selectedLocation)
            break
        case "Murphy Reviewer *":
            if self.selectedLocation != "" && self.selectedDepartment != ""{
                cell.isUserInteractionEnabled = true
                cell.contentView.backgroundColor = UIColor.white
            }
            else{
                cell.isUserInteractionEnabled = false
                cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
            }
            cell.setData(labelData: "Murphy Reviewer *", fieldValue: self.selectedMurphyReviewer.displayName )
            break
        case "Start Date":
            cell2.setData(dateKey: "Start Date *", timeKey: "Start Time", dateInput: UIstartDate, timeInput: UIstartTime)
            cell2.dateValue.tag = 101
            cell2.dateValue.isDatePicker = true
            cell2.dateValue.datePickerMode = .date
            cell2.timeValue.tag = 102
            cell2.timeValue.isDatePicker = true
            cell2.timeValue.datePickerMode = .time
            return cell2
        case "End Date":
            cell2.setData(dateKey: "End Date *", timeKey: "End Time", dateInput: UIendDate, timeInput: UIendTime)
            cell2.dateValue.tag = 201
            cell2.dateValue.isDatePicker = true
            cell2.dateValue.datePickerMode = .date
            cell2.dateValue.isDatePicker = true
            cell2.timeValue.tag = 202
            cell2.timeValue.isDatePicker = true
            cell2.timeValue.datePickerMode = .time
            return cell2
        case "Quantity *":
            cell.inputField.placeholder = "##"
            cell.setData(labelData: "Quantity *", fieldValue: currentTicket.Quantity)
            break
        case "Unit *":
            cell.inputField.isUserInteractionEnabled = false
            cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
            cell.inputField.textColor = UIColor.gray
            cell.setData(labelData: "Unit *", fieldValue: "BBL")
            break
        case "Job Performed By *":
            cell.inputField.isUserInteractionEnabled = false
            cell.contentView.backgroundColor = UIColor(red : 239/255, green : 241/255, blue : 244/255, alpha : 1)
            cell.inputField.textColor = UIColor.gray
            cell.setData(labelData: "Job Performed By *", fieldValue: UserDefaults.standard.string(forKey: "name")!)
            break
            
        default:
            print("error")
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        let cellType = TableLabels[indexPath.row] 
        switch cellType {
        case "Start Date", "End Date":
            return 80
        default:
            return 60
        }
    }
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
    }
    
}

// MARK: - CollectionView Delegate
extension CreateTicketController : UICollectionViewDelegate, UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return convertedImages.count + 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if indexPath.row == convertedImages.count {
            let cell = galleryView.dequeueReusableCell(withReuseIdentifier: "AddCell", for: indexPath)
            return cell
        }
        else{
            let cell : GalleryCell = galleryView.dequeueReusableCell(withReuseIdentifier: "GalleryCell", for: indexPath) as! GalleryCell
            cell.setThumbnail(thumbImage : convertedImages[indexPath.row], sender: self,index : indexPath.row)
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.row == convertedImages.count{
            attachmentOptions()
        }
    }
    
    func getDeletedIndex(index : Int){
        
        convertedImages.remove(at: index)
        galleryView.reloadData()
    }
    
}

// MARK: - Pickerview Delegate

extension CreateTicketController {
//    func numberOfComponents(in pickerView: UIPickerView) -> Int {
//        return 1
//
//    }
//
//    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
//
//        switch TableLabels[bgView.tag] {
//        case "Service Type *" :
//            return serviceTypeList.count
//        case "Department *" :
//            return departmentValues.count
//        default:
//            return 0
//        }
//    }
//
//    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
//
//        switch TableLabels[bgView.tag] {
//        case "Service Type *" :
//            return serviceTypeList[row]
//        case "Department *" :
//            return departmentValues[row]
//        default:
//            return "Error"
//        }
//    }
//
//    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
//
//        self.selectedIndex = row
//
//    }
    
    
    func openSearchReviewer()
    {
       // self.view.endEditing(true)
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : SearchViewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController
        vc2.delegate = self
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.typeofSearch = "MurphyReviewer"
        vc2.reviewers = selectedMurphyReviewer.displayName
        vc2.Field = self.selectedField ?? HierarchyItem(item: NSDictionary())
        vc2.Facility = self.selectedFacility2 ?? HierarchyItem(item: NSDictionary())
        vc2.WellPad = self.selectedWellPad ?? HierarchyItem(item: NSDictionary())
        vc2.Well = self.selectedWell ?? HierarchyItem(item: NSDictionary())
        vc2.Department = self.selectedDepartment
        if ConnectionCheck.isConnectedToNetwork(){
            self.navigationController?.pushViewController(vc2, animated: true)
        }
        else{
            let reviewerService = ReviewerService(context:self.context)
            var dbResponse = [Reviewer]()
            
            let predicate1 = NSPredicate(format: "department == %@", self.selectedDepartment)
            let predicate2 = NSPredicate(format: "field == %@", self.selectedField?.ItemValue1 ?? "")
            let predicate3 = NSPredicate(format: "facility == %@", self.selectedFacility2?.ItemValue1 ?? "")
            let predicate4 = NSPredicate(format: "wellPad == %@", self.selectedWellPad?.ItemValue1 ?? "")
            let predicate5 = NSPredicate(format: "well == %@", self.selectedWell?.ItemValue1 ?? "")
            let predicate6 = NSPredicate(format: "facility == %@", "")
            let predicate7 = NSPredicate(format: "wellPad == %@", "")
            let predicate8 = NSPredicate(format: "well == %@", "")
            
            let wellSearchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2, predicate3, predicate4, predicate5]
            )
            let wellPadSearchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2, predicate3, predicate4, predicate8]
            )
            let facilitySearchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2, predicate3, predicate7, predicate8]
            )
            let fieldSearchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2, predicate6, predicate7, predicate8]
            )
            if self.selectedWell?.ItemValue1 == nil && self.selectedWellPad?.ItemValue1 == nil && self.selectedFacility2?.ItemValue1 == nil && self.selectedField?.ItemValue1 != nil
            {
                dbResponse = reviewerService.get(withPredicate: fieldSearchPredicate)
                if dbResponse.count == 0{
                    let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    self.navigationController?.pushViewController(vc2, animated: true)
                }
            }
            else if self.selectedWell?.ItemValue1 == nil && self.selectedWellPad?.ItemValue1 == nil && self.selectedFacility2?.ItemValue1 != nil && self.selectedField?.ItemValue1 != nil{
                dbResponse = reviewerService.get(withPredicate: facilitySearchPredicate)
                if dbResponse.count == 0{
                    dbResponse = reviewerService.get(withPredicate: fieldSearchPredicate)
                    if dbResponse.count == 0{
                        let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    else{
                        self.navigationController?.pushViewController(vc2, animated: true)
                    }
                }
                else{
                    self.navigationController?.pushViewController(vc2, animated: true)
                }
            }
            else if self.selectedWell?.ItemValue1 == nil && self.selectedWellPad?.ItemValue1 != nil && self.selectedFacility2?.ItemValue1 != nil && self.selectedField?.ItemValue1 != nil{
                dbResponse = reviewerService.get(withPredicate: wellPadSearchPredicate)
                if dbResponse.count == 0{
                    dbResponse = reviewerService.get(withPredicate: facilitySearchPredicate)
                    if dbResponse.count == 0{
                        dbResponse = reviewerService.get(withPredicate: fieldSearchPredicate)
                        if dbResponse.count == 0{
                            let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                        else{
                            self.navigationController?.pushViewController(vc2, animated: true)
                        }
                    }
                    else{
                        self.navigationController?.pushViewController(vc2, animated: true)
                    }
                }
                else{
                    self.navigationController?.pushViewController(vc2, animated: true)
                }
            }
            else if self.selectedWell?.ItemValue1 != nil && self.selectedWellPad?.ItemValue1 != nil && self.selectedFacility2?.ItemValue1 != nil && self.selectedField?.ItemValue1 != nil{
                dbResponse = reviewerService.get(withPredicate: wellSearchPredicate)
                if dbResponse.count == 0{
                    dbResponse = reviewerService.get(withPredicate: wellPadSearchPredicate)
                    if dbResponse.count == 0{
                        dbResponse = reviewerService.get(withPredicate: facilitySearchPredicate)
                        if dbResponse.count == 0{
                            dbResponse = reviewerService.get(withPredicate: fieldSearchPredicate)
                            if dbResponse.count == 0{
                                let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                                alertController.addAction(okAction)
                                self.present(alertController, animated: true, completion: nil)
                            }
                            else{
                                self.navigationController?.pushViewController(vc2, animated: true)
                            }
                        }
                        else{
                            self.navigationController?.pushViewController(vc2, animated: true)
                        }
                    }
                    else{
                        self.navigationController?.pushViewController(vc2, animated: true)
                    }
                }
                else{
                    self.navigationController?.pushViewController(vc2, animated: true)
                }
            }
        }
        
        
    }
    
    func formatTime(providedDate : Date) -> String{
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm a"
        let formattedDate = formatter.string(from: providedDate)
        return formattedDate
    }
    
    func formatDate(providedDate : Date) -> String{
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy"
        let formattedDate = formatter.string(from: providedDate)
        return formattedDate
    }
    
    func encodeTime(fromTime : Date) -> String
    {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm:ss"
        let formattedDate = formatter.string(from: fromTime)
        var splitDate = formattedDate.components(separatedBy: ":")
        let returnTime = "PT"+splitDate[0]+"H"+splitDate[1]+"M"+splitDate[2]+"S"
        return returnTime
    }
    
    func decodeTime(timeString : String) -> String{
        
        let hour = timeString[2 ..< 4] // returns String "o, worl"
        let minutes = timeString[5 ..< 7]
        let seconds = timeString[8 ..< 10]
        let timeData = hour + ":" + minutes + ":" + seconds
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss"
        let myDate = dateFormatter.date(from: timeData)!
        dateFormatter.dateFormat = "hh:mm a"
        let somedateString = dateFormatter.string(from: myDate)
        return somedateString
    }
    
    
    func isDecimalNumber(testStr:String) -> Bool {
        let numRegex = "^(?:|0|[1-9]\\d*)(?:\\.\\d*)?$"
        let numTest = NSPredicate(format:"SELF MATCHES %@", numRegex)
        return numTest.evaluate(with: testStr)
        
    }
    
    func createPayload(){
        
        let currentDate : Date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        createdDate = formatter.string(from: currentDate)
        createdTime = encodeTime(fromTime: currentDate)
        currentTicket.CreatedDate = createdDate
        currentTicket.CreationTime = createdTime
        var attachPost = [[String : Any]]()
        csrfCount = 0
        for element in attachments{
            //"Deleteflag":"",
            let item = ["AttachmentLength" : String(element.attachmentLength), "AttachmentName" : element.attachmentName, "AttachmentContent" : element.attachmentContent, "ServiceProviderId" : element.ServiceProviderId, "AttachmentType" : element.attachmentType, "CreatedBy" : UserDefaults.standard.string(forKey: "id")!, "UpdatedBy" : "","CreatedByName":UserDefaults.standard.string(forKey: "name")!] as [String : Any]
            attachPost.append(item)
        }
        var commentArray = [[String : Any]]()
        var comments = CommentsTextView.text!
        let commentPost = ["CommentedBy" : UserDefaults.standard.string(forKey: "id")!, "Comments" : comments, "CommentByName":UserDefaults.standard.string(forKey: "name")! ]
        if comments == "Add optional comments if necessary"{
            comments = ""
        }
        else{
            commentArray.append(commentPost)
        }
        let retNav = ["Type" : "", "Message" : ""]
        
        var activityArray = [[String : Any]]()
        let activityPost = ["ActionType" : ActionType.Create.rawValue, "ProcessId" : "", "TaskId" : "", "Status" : TicketStatus.Create.rawValue , "UserId" : "", "CreatedBy" : UserDefaults.standard.string(forKey: "id")!, "UpdatedBy" : "", "CreatedByName":UserDefaults.standard.string(forKey: "name")!]
        activityArray.append(activityPost)
        let saltwaterArray: [String: Any] = ["Quantity":currentTicket.Quantity ,
                                             "Unit":currentTicket.Unit ]
       
        postData = ["CreatedBy" : UserDefaults.standard.string(forKey: "id")! ,
                    "CreatedByName": UserDefaults.standard.string(forKey: "name")!,
                    "TimeFlag" : timeFlag,
                    "AribaSESNo" : currentTicket.VendorRefNo,
                    "StartDate" : currentTicket.startDate,
                    "SESNumber":"",
                    "WorkOrderNo":"",
                    "EndDate": currentTicket.endDate,
                    "MurphyFieldReviewer":selectedMurphyReviewer.emailID,
                    "MurphyFieldReviewerId":selectedMurphyReviewer.id,
                    "MurphyFieldReviewerName": selectedMurphyReviewer.displayName,
                    "ReviewedBy":"",
                    "MurphySesApprover" : "",
                    "ServiceProviderId" : UserDefaults.standard.string(forKey: "id")!,
                    "ServiceProviderMail": UserDefaults.standard.string(forKey: "email")!,
                    "Department" : currentTicket.Department,
                    "Location" : currentTicket.Location,
                    "Service_Type": "SALTWATER",
                    "StartTime" : currentTicket.startTime,
                    "EndTime" : currentTicket.endTime,
                    "MurphyPoNumber" : "",
                    "AccountingCategory" : "",
                    "CostCenter" : "",
                    "VendorId" : UserDefaults.standard.string(forKey: "vid")!,
                    "VendorAdminId" : UserDefaults.standard.string(forKey: "vadminid")!,
                    "Status" : TicketStatus.Create.rawValue ,
                    "UIcreationTime":currentTicket.CreationTime,
                    "UIcreatedOn":currentTicket.CreatedDate,
                    "JobPerformedBy":UserDefaults.standard.string(forKey: "name")!,
                    "Version":"",
                    "DeviceType":"iOS",
                    "DeviceId":"",
                    "WbsElement":"",
                    "CostObject":"",
                    "PO_Text": "",
                    "WO_Text": "",
                    "WBS_Text": "",
                    "CC_Text": "",
                    "MergeFlag":"UNMERGED",
                    "HEADERTOATTACHMENTNAV" : attachPost,
                    "HEADERTORETURNNAV" : retNav,
                    "HEADERTOCOMMENTNAV" : commentArray,
                    "HEADERTOACTIVITYNAV" : activityArray ,
                    "HEADERTOSALTWATERNAV": saltwaterArray
            ] as [String : Any]
        postData["Field"] = self.selectedField?.ItemValue1! ?? ""
        postData["FieldCode"] = self.selectedField?.ItemValue2! ?? ""
        postData["Facility"] = self.selectedFacility2?.ItemValue1! ?? ""
        postData["FacilityCode"] = self.selectedFacility2?.ItemValue2! ?? ""
        postData["WellPad"] = self.selectedWellPad?.ItemValue1! ?? ""
        postData["WellpadCode"] = self.selectedWellPad?.ItemValue2! ?? ""
        postData["Well"] = self.selectedWell?.ItemValue1! ?? ""
        postData["WellCode"] = self.selectedWell?.ItemValue2! ?? ""

        if self.selectedServiceType != "Salt Water Disposal"{
            postData.removeValue(forKey: "HEADERTOSALTWATERNAV")
            postData["Service_Type"] = ""
        }
        if ConnectionCheck.isConnectedToNetwork(){
            getCSRFforCreateTicket()
        }
        else{
            let actionService = ActService(context:self.context)
            let actionData: NSData = NSKeyedArchiver.archivedData(withRootObject: postData) as NSData
            
            var unsyncedDFTs = [FieldTicket]()
            unsyncedDFTs.removeAll()
            let storedUnsynced = UserDefaults.standard.data(forKey: "unsynced")
            if storedUnsynced != nil {
                let unsyncedTickets : [FieldTicket] = (NSKeyedUnarchiver.unarchiveObject(with: storedUnsynced!) as? [FieldTicket])!
                let count = unsyncedTickets.count
                UserDefaults.standard.set(count, forKey: "uncount")
                UserDefaults.standard.synchronize()
                unsyncedDFTs.append(contentsOf: unsyncedTickets)
            }
            
            let depLoc = currentTicket.Department + "|" + (postData["Field"] as! String)
            let tempId = String(UserDefaults.standard.integer(forKey: "uncount") + 1)
            _ = actionService.create(extraParam: tempId, actionType: depLoc , empId: selectedMurphyReviewer.id, dateSync: selectedMurphyReviewer.emailID, PostObj: actionData as NSData)
            actionService.saveChanges()
            
            
            let fieldTicket = FieldTicket()
            fieldTicket.Department = currentTicket.Department
            fieldTicket.Location = currentTicket.Location
            fieldTicket.UIcreatedOn = currentTicket.CreatedDate
            fieldTicket.UIcreationTime = currentTicket.CreationTime
            fieldTicket.FieldTicketNum = "Not Yet Assigned"
            fieldTicket.Status = TicketStatus.Unsync.rawValue
            fieldTicket.tempId = tempId
            fieldTicket.AribaSesNumber = currentTicket.VendorRefNo
            unsyncedDFTs.append(fieldTicket)
            let encodedData = NSKeyedArchiver.archivedData(withRootObject: unsyncedDFTs)
            UserDefaults.standard.set(encodedData, forKey: "unsynced")
            UserDefaults.standard.synchronize()
            
            
            let alertController = UIAlertController.init(title: "Offline Action Performed", message:" Action will be updated once the system goes online!" , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                self.dismiss(animated: true, completion: nil)})
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
            
        }
    }
  
    //200 success
    //everything is fine but banner is not showing
    func getCSRFforCreateTicket()
    {
        self.loaderStart()
        self.submitButton.isUserInteractionEnabled = false
        let header = [ "x-csrf-token" : "fetch"]
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "get"

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
                        self!.createTicketServiceCall()
                    }
                    else{
                        self!.csrfCount = self!.csrfCount! + 1
                        if self!.csrfCount == 3{
                            self!.getCSRFforCreateTicket()
                        }
                        else{
                            let message = "CSRF fetch failed!"
                            self!.submitButton.isUserInteractionEnabled = true
                            self!.loaderStop()
                            let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                            alertController.addAction(okAction)
                            self!.present(alertController, animated: true, completion: nil)
                        }
                    }
                    
                    
                    
                }
                
            }
            else{
                DispatchQueue.main.async {
                    self!.loaderStop()
                    self!.submitButton.isUserInteractionEnabled = true
                    let message = "No internet Connection Available. The previously created data is lost. Try recreating."
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
       
    }
    
    
    //201 success
    func createTicketServiceCall()
    {
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
               DispatchQueue.main.async{
                    print(response)
                   // print("****\(String(describing: response.result.value))****")
                    let xmlValue = String(data: data!, encoding: .utf8)
                    if xmlValue != nil{
                        let data1 = xmlValue?.data(using: .utf8)
                        self!.parser = XMLParser(data: data1!)
                        self!.parser.delegate = self
                        self!.parser.parse()
                        print("****end****")
                        if self!.responseType == "S"{
                            self!.workFlowTokenFetch()
                            let alertController = UIAlertController.init(title: "", message: "Digital Field Ticket \(self!.ticketName!) created" , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                                self!.dismiss(animated: true, completion: nil)})
                            alertController.addAction(okAction)
                            self!.present(alertController, animated: true, completion: nil)
                        }
                        else{
                            var message = self!.responseMessage
                            if self!.responseMessage == nil{
                                message = self!.errorMessage
                            }
                            self!.submitButton.isUserInteractionEnabled = true
                            let alertController = UIAlertController.init(title: "", message: message , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                                //self.dismiss(animated: true, completion: nil)
                                
                            })
                            alertController.addAction(okAction)
                            self!.present(alertController, animated: true, completion: nil)
                        }
                    }
                }
            }
            else{
                DispatchQueue.main.async{
                    let message = "No internet Connection Available. The previously created data is lost. Try recreating."
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }
            DispatchQueue.main.async{
                self!.loaderStop()
            }
        }
        task.resume()
       
    }
    
    
    //api success 200
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
                        
                        self!.csrfToken  = xcsrf
                        print(xcsrf)
                    }
                }
                
                if self!.csrfToken != nil{
                    self!.triggerOverflow()
                }
                else{
                    self!.restCsrfCount = self!.restCsrfCount! + 1
                    if self!.restCsrfCount == 3{
                        self!.workFlowTokenFetch()
                    }
                    else{
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                            self!.dismissScreen()})
                        alertController.addAction(okAction)
                        self!.present(alertController, animated: true, completion: nil)
                    }
                    
                }
                
                
                }
            }
            else{
                DispatchQueue.main.async{
                let message = "No internet Connection Available. The previously created data is lost. Try recreating."
                let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                alertController.addAction(okAction)
                self!.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
        
        
        
        /*
        
        Aramofire.request("\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/xsrf-token", method: .get, parameters: nil, encoding: URLEncoding.default, headers: header)
            .responseString{ response in
                
                if ConnectionCheck.isConnectedToNetwork(){
                    self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
                    if self.csrfToken != nil{
                        self.triggerOverflow()
                    }
                    else{
                        self.restCsrfCount = self.restCsrfCount! + 1
                        if self.restCsrfCount == 3{
                            self.workFlowTokenFetch()
                        }
                        else{
                            let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed" , preferredStyle: UIAlertController.Style.alert)
                            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                                self.dismissScreen()})
                            alertController.addAction(okAction)
                            self.present(alertController, animated: true, completion: nil)
                        }
                        
                    }
                }
                else{
                    let message = "No internet Connection Available. The previously created data is lost. Try recreating."
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
        }*/
        
    }
    
    //201
    //check for bannerf
    //success
    func triggerOverflow() {
        
        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
        let currentDate = Date()
        let contextualData : [String : Any] = ["appId" : "com.dft.native",
                                               "fieldTicketId" : self.ticketName!,
                                               "dep" : currentTicket.Department,
                                               "loc" : self.selectedField?.ItemValue1! ?? "",
                                               "isApprovedByMurphyEngineer": "false",
                                               "cBN" : UserDefaults.standard.string(forKey: "name")!,
                                               "workflowTriggerTimeStamp" : String(describing: currentDate),
                                               "vRNo" : currentTicket.VendorRefNo,
                                               "vSId" : UserDefaults.standard.string(forKey: "vid")!,
                                               "vendorId" : UserDefaults.standard.string(forKey: "vadminid")!,
                                               "vendorName" :  UserDefaults.standard.string(forKey: "vname")!,
                                               "vendorEmailId" : UserDefaults.standard.string(forKey: "vemailid")!,
                                               "murphyEngineerId" : selectedMurphyReviewer.id ,
                                               "murphyEngineerEmailId" : selectedMurphyReviewer.emailID  ]
        print(contextualData)
        let postData = [ "definitionId" : "digital_field_ticket_workflow", "context" : contextualData as [String : Any]] as [String : Any]
        
        let urlString = "\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/workflow-instances"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields  = header
        urlRequest.httpMethod = "post"
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
                                self.restBanner = NotificationBanner(title: "Task sent for Review", subtitle: "", style: .success)
                            }
                            else{
                           self.restBanner = NotificationBanner(title: "Error received", subtitle: "status \(String(describing: status))", style: .danger)
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
    
}//class ends
extension CreateTicketController: SearchViewControllerDelegate {
    func didSelect(searchItem: GenericItem, typeOfSearch: String) {
        //do Something
    }
    
    
    func didSelect1(searchItem: GenericItem, typeOfSearch: String) {
        switch typeOfSearch {
            
        case "MurphyReviewer":
            let SelMurphyRev = MurphyEngineer()
            SelMurphyRev.emailID = searchItem.ItemValue2!
            SelMurphyRev.id = searchItem.ItemValue3!
            SelMurphyRev.displayName = searchItem.ItemValue1!
            self.selectedMurphyReviewer = SelMurphyRev
            
        default:
            print("error in didselect searchviewdelegate")
        }
        self.tableView.reloadData()
    }
    
}

extension String {
    /*
     Truncates the string to the specified length number of characters and appends an optional trailing string if longer.
     - Parameter length: Desired maximum lengths of a string
     - Parameter trailing: A 'String' that will be appended after the truncation.
     
     - Returns: 'String' object.
     */
    func truncate(length: Int) -> String {
        return (self.count > length) ? self.prefix(length) + "" : self
    }
}

extension CreateTicketController: UIDatePickerDelegate {
    
    func datePickerValueChanged(date: Date, textField: ICTextField) {
        
        if textField.tag == 101 || textField.tag == 201 {
            //self.date = date
            if date != nil{
                let dateValue = date.toDateFormat(.longT)
                textField.text = date.toDateFormat(.dayMonthYear)
                if textField.tag == 101{
                    startDateStamp = date
                    if currentTicket.endDate != "" && startDateStamp > endDateStamp{
                        
                        let alertController = UIAlertController.init(title: "", message:"Start Date should not be later than End Date" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                        
                    }
                    else{
                        currentTicket.startDate = dateValue
                        UIstartDate = textField.text!
                    }
                }
                else{
                    endDateStamp = date
                    if currentTicket.startDate != "" && startDateStamp > endDateStamp{
                        let alertController = UIAlertController.init(title: "", message:"Start Date should not be later than End Date" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    else{
                        currentTicket.endDate = dateValue
                        UIendDate = textField.text!
                    }
                }
            }
        }
        else if textField.tag == 102 || textField.tag == 202 {
            //self.time = date
            if date != nil{
                let timeValue = encodeTime(fromTime: date)
                textField.text = date.toDateFormat(.short)
                if textField.tag == 102{
                    startTimeStamp = date
                    if currentTicket.startDate == currentTicket.endDate && endTime != "" && startTimeStamp > endTimeStamp{
                        
                        let alertController = UIAlertController.init(title: "", message:"Start Time should not be later than End Time when Date is same." , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                        
                    }
                    else{
                        startTime = timeValue
                        UIstartTime = textField.text!
                    
                    }
                }
                else{
                    endTimeStamp = date
                    if currentTicket.startDate == currentTicket.endDate && startTime != "" && startTimeStamp > endTimeStamp{
                        
                        let alertController = UIAlertController.init(title: "", message:"Start Time should not be later than End Time when Date is same." , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                        
                    }
                    else{
                        endTime = timeValue
                        UIendTime = textField.text!
                        
                    }
                }
            }
        }
    }
}

extension CreateTicketController : UIDropDownDelegate{
    
    func datePickerDidSelect(row: Int, tag : Int){
        
        if TableLabels[tag] == "Service Type *"{
            
            self.currentTicket.ServiceType = self.serviceTypeList[row]
            self.selectedServiceType = self.serviceTypeList[row]
            
        }
        else{
            self.selectedDepartment = self.departmentValues[row]
            self.currentTicket.Department = self.departmentValues[row]
        }
        tableView.reloadData()
    }
}
