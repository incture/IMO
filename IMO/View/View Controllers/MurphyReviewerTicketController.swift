//
//  MurphyReviewerTicketController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 10/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

import QuickLook

class MurphyReviewerTicketController: UIViewController {
    
    
    @IBOutlet var ticketNumberLabel: UILabel!
    @IBOutlet var vendorName: UILabel!
    @IBOutlet var vendorID: UILabel!
    @IBOutlet var vendorAddress: UILabel!
    @IBOutlet var segmentControl: UISegmentedControl!
    @IBOutlet var listTable: UITableView!
    @IBOutlet var nextButton: UIButton!
    @IBOutlet var tableViewConstraint: NSLayoutConstraint!
    @IBOutlet var backButton: UIButton!
    
    var ticket : FieldTicket?
//    var labelValues = ["Vendor Reference No", "Create Date", "Department", "Field", "Facility", "", "", "Job Performed By"]
    //MOhan
 var labelValues = ["Vendor Reference No", "Department", "Location", "Start", "End", "Job Performed By", "Create Date"]
    var ticketNumber : String = ""
    //var vendor_Reference_Num : String = ""
    var Department : String = ""
   // var location : String = ""
    var create_Date : String = ""
    var start_date : String = ""
    var start_time : String = ""
    var end_date : String = ""
    var end_time : String = ""
    var attachmentArray = [Attachment]()
    var isServiceHit : Bool = false
    var instanceID : String?
    //var facility : String = ""
    var vendorIDvalue : String?
    var vendorNamevalue : String?
    var vendorAddressvalue : String?
    var comment = Comment()
    var isComment : Bool?
    var hasComment : Bool = false
    var fileUrl : URL?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let formNib = UINib(nibName: "FormCell", bundle: nil)
        listTable.register(formNib, forCellReuseIdentifier: "FormCell")
        let form2Nib = UINib(nibName: "Form1Cell", bundle: nil)
        listTable.register(form2Nib, forCellReuseIdentifier: "Form1Cell")
        let imageNib = UINib(nibName: "ImageCell", bundle: nil)
        listTable.register(imageNib, forCellReuseIdentifier: "ImageCell")
        let commentNib = UINib(nibName: "CommentsCell", bundle: nil)
        listTable.register(commentNib, forCellReuseIdentifier: "CommentsCell")
        
        getTicketDetail()
        let font = UIFont.systemFont(ofSize: 16)
        segmentControl.setTitleTextAttributes([NSAttributedString.Key.font: font],
                                              for: .normal)
        segmentControl.layer.cornerRadius = 0.0
        segmentControl.layer.borderColor = UIColor.white.cgColor
        segmentControl.layer.borderWidth = 2.0
        segmentControl.layer.masksToBounds = true
        tableViewConstraint.constant = 0
        nextButton.isHidden = true
        nextButton.isEnabled = false
        backButton.setImage(UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), for: .normal)
        backButton.tintColor = UIColor.white
        listTable.tableFooterView = UIView()
        ticketNumberLabel.text = ticketNumber
        isComment = false
        
        let titleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor.black]
        let selectTitleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor.white]
        
        segmentControl.setTitleTextAttributes(titleTextAttributes, for: .normal)
        segmentControl.setTitleTextAttributes(selectTitleTextAttributes, for: .selected)
        

        // Do any additional setup after loading the view.
    
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func doubleDismiss()
    {
        self.dismiss(animated: false, completion: nil)
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.listTable.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
      //  UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
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
    
    func convertTimeStamp(timeStamp : String) -> String
    {
        let unixDate = Double(timeStamp)
        let date = Date(timeIntervalSince1970: unixDate!)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "IST") //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "dd MMM yyyy" //Specify your format that you want
        let strDate = dateFormatter.string(from: date)
        return strDate
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
    
    @IBAction func onSegmentControlSwitch(_ sender: UISegmentedControl) {
        
        if sender.selectedSegmentIndex == 0{
            
            tableViewConstraint.constant = 0
            nextButton.isHidden = true
            nextButton.isEnabled = false
            listTable.reloadData()
        }
        else{
            
            tableViewConstraint.constant = 60
            nextButton.isHidden = false
            nextButton.isEnabled = true
            listTable.reloadData()
        }
        
    }
    
    @IBAction func onNextPress(_ sender: UIButton) {
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "AttestController") as! AttestController
        splitViewController.attachmentArray = self.attachmentArray
        splitViewController.instanceID = self.instanceID
        splitViewController.fieldTicketNumber = self.ticketNumber
        splitViewController.ticket = self.ticket
        splitViewController.senderController = self
        splitViewController.vendorID = self.ticket?.vendorID
        let navController = UINavigationController(rootViewController: splitViewController)
        self.present(navController, animated: true, completion: nil)
    }
    
    @IBAction func onBackPress(_ sender: UIButton) {
        
        self.dismiss(animated: false, completion: nil)
    }
    
    
    //api success 200
    func getTicketDetail() {
        
        //?$expand=HEADERTOATTACHMENTNAV,HEADERTOCOMMENTNAV,HEADERTOACTIVITYNAV"
        //let user = UserDefaults.standard.string(forKey: "id")
        
        
        DispatchQueue.main.async {
            self.loaderStart()

        }
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet(FieldTicketNum='\(ticketNumber)')?$expand=HEADERTOATTACHMENTNAV,HEADERTOCOMMENTNAV,HEADERTOACTIVITYNAV,HEADERTOSALTWATERNAV&$format=json"
        let encodedUrl = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            
            
           
            self!.isServiceHit = true
            if error == nil  {
                
                do{
                    guard let data = data else { return }
                    
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                    //print(JSON)
                    DispatchQueue.main.async {
                        if let jsonDict = JSON as? NSDictionary {
                            
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                
                                self!.ticket = FieldTicket(JSON : d)
                                if self!.ticket?.serviceType == "Salt Water Disposal"{
                                    self!.labelValues = ["Vendor Reference No", "Service Type", "Department", "Location", "Quantity", "Unit","Start", "End", "Job Performed By", "Create Date"]
                                }
                                else{
                                    self!.labelValues = ["Vendor Reference No", "Department", "Location", "Start", "End", "Job Performed By", "Create Date"]
                                }
                                self!.Department = d.value(forKey: "Department") as! String
                                if let create_date = d.value(forKey: "UIcreatedOn"){
                                    self!.create_Date = create_date as? String ?? ""
                                }
                                self!.start_date = d.value(forKey: "StartDate") as? String ?? ""
                                self!.ticket?.start_timestamp = self!.start_date
                                self!.start_time = d.value(forKey: "StartTime") as? String ?? ""
                                self!.end_date = d.value(forKey: "EndDate") as? String ?? ""
                                self!.ticket?.end_timestamp = self!.end_date
                                self!.end_time = d.value(forKey: "EndTime") as? String ?? ""
                                if let SaltWaterNav = d.value(forKey: "HEADERTOSALTWATERNAV") as? NSDictionary{
                                    
                                    self!.ticket?.quantity = SaltWaterNav.value(forKey: "Quantity") as? String ?? ""
                                    self!.ticket?.unit = SaltWaterNav.value(forKey: "Unit") as? String ?? ""
                                    
                                }
                                if let commentset = d.value(forKey: "HEADERTOCOMMENTNAV") as? NSDictionary{
                                    
                                    if let result = commentset.value(forKey : "results") as? [NSDictionary]{
                                        if result.count > 0{
                                            
                                            self!.comment.Comments = (result[0].value(forKey: "Comments") as? String)!
                                            self!.comment.commentedBy = (result[0].value(forKey: "CommentByName") as? String)!
                                            self!.comment.commentedById = (result[0].value(forKey: "CommentedBy") as? String)!
                                            if self!.ticket?.CreatedByName == self!.comment.commentedBy{
                                                self!.hasComment = true
                                                if let commentDate = result[0].value(forKey: "CommentedOn") as? String{
                                                    let timestamp = commentDate[6 ..< 16]
                                                    let dateValue = self!.convertTimeStamp(timeStamp: timestamp)
                                                    self!.comment.commentDate = dateValue
                                                }
                                                self!.isComment = true
                                            }
                                        }
                                    }
                                }
                                if let attachmentset = d.value(forKey: "HEADERTOATTACHMENTNAV") as? NSDictionary{
                                    
                                    let result = attachmentset.value(forKey: "results") as? [NSDictionary]
                                    self!.attachmentArray.removeAll()
                                    for each in result!{
                                        
                                        let attach = Attachment()
                                        attach.attachmentContent = (each.value(forKey: "AttachmentContent") as? String) ?? ""
                                        attach.attachmentName = (each.value(forKey: "AttachmentName") as? String) ?? ""
                                        attach.createdOn = (each.value(forKey: "CreatedOn") as? String) ?? ""
                                        attach.attachmentLengthFromService = (each.value(forKey: "AttachmentLength") as? String) ?? ""
                                        attach.ServiceProviderId = (each.value(forKey: "ServiceProviderId") as? String) ?? ""
                                        attach.ticketNumber = (each.value(forKey: "FieldTicketNum") as? String) ?? ""
                                        attach.attachmentID = (each.value(forKey: "AttachmentId") as? String) ?? ""
                                        attach.createdBy = (each.value(forKey: "CreatedBy") as? String) ?? ""
                                        attach.updatedBy = (each.value(forKey: "UpdatedBy") as? String) ?? ""
                                        attach.updatedOn = (each.value(forKey: "UpdatedOn") as? String) ?? ""
                                        attach.sharepointUrl = (each.value(forKey: "SharepointUrl") as? String) ?? ""
                                        attach.createdByName = (each.value(forKey: "CreatedByName") as? String) ?? ""
                                        attach.attachmentType = (each.value(forKey: "AttachmentType") as? String) ?? ""
                                        // attach.deleteFlag = (each.value(forKey: "Deleteflag") as? String) ?? ""
                                        let ticketDate = attach.createdOn
                                        let timestamp = ticketDate[6 ..< 16]
                                        let dateValue = self!.convertTimeStamp(timeStamp: timestamp)
                                        attach.create_Date = dateValue
                                        self!.attachmentArray.append(attach)
                                    }
                                    self?.listTable.dataSource = self
                                    self?.listTable.delegate = self
                                    self!.listTable.reloadData()
                                    self!.vendorID.text = self!.ticket?.vendorID
                                    self!.vendorName.text = self!.ticket?.vendorName
                                    self!.vendorAddress.text = self!.ticket?.vendorAddress
                                    self!.loaderStop()
                                }
                                
                                
                            }
                            
                        }
                        
                    }
                }
                catch{
                    print("x\(error.localizedDescription)")
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
    
    
}//class

// MARK: - QuickLook Delegates and Datasource

extension MurphyReviewerTicketController : QLPreviewControllerDelegate, QLPreviewControllerDataSource{
    func numberOfPreviewItems(in controller: QLPreviewController) -> Int {
        
        return 1
    }
    
    func previewController(_ controller: QLPreviewController, previewItemAt index: Int) -> QLPreviewItem {
        
        return fileUrl! as QLPreviewItem
    }
    
    public func previewController(_ controller: QLPreviewController, shouldOpen url: URL, for item: QLPreviewItem) -> Bool{
        return true
    }
    
    func viewPDF() {
        let ql = QLPreviewController()
        ql.dataSource = self
        ql.delegate = self
        present(ql, animated: true, completion: nil)
    }
    
    func createImageToView(index : Int)
    {
        let imageBase64 = attachmentArray[index].attachmentContent
        let imageData = Data(base64Encoded: imageBase64 , options: .ignoreUnknownCharacters)
        let convertedData : Data = imageData!
        guard
            var documentsURL = (FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)).last
            
            else {
                return
        }
        print(attachmentArray[index].attachmentName)
        documentsURL.appendPathComponent(attachmentArray[index].attachmentName)
        do {
            try convertedData.write(to: documentsURL)
        } catch {
            //handle write error here
        }
        
        self.fileUrl = documentsURL
        self.viewPDF()
    }
}

extension MurphyReviewerTicketController : UITextFieldDelegate{
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        let locationController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "LocationPopUpController") as! LocationPopUpController
        if self.ticket?.field == "" && self.ticket?.Department == "Operations"{
           locationController.field = (self.ticket?.Location)!
        }
        else{
            locationController.field = (self.ticket?.field)!
        }
        locationController.facility = (self.ticket?.facility)!
        locationController.wellPad = (self.ticket?.wellPad)!
        locationController.well = (self.ticket?.well)!
        locationController.modalPresentationStyle = .overCurrentContext
        self.present(locationController, animated: false, completion: nil)
        self.view.endEditing(true)
    }
}

// MARK: - TableView Delegates and Datasource

extension MurphyReviewerTicketController : UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if segmentControl.selectedSegmentIndex == 0{
            if isServiceHit{
                return labelValues.count
            }
            else{
                return 0
            }
        }
        else{
            if hasComment{
                return attachmentArray.count + 1
            }
            else{
                return attachmentArray.count
            }
        }
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if segmentControl.selectedSegmentIndex == 0{

            let cell = tableView.dequeueReusableCell(withIdentifier: "FormCell")! as! FormCell
                cell.selectionStyle = .none
                cell.inputField.isUserInteractionEnabled = false
            switch labelValues[indexPath.row] {
            case "Vendor Reference No":
                cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.AribaSesNumber) ?? "")
            case "Service Type":
                cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.serviceType) ?? "")
            case "Quantity":
                cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.quantity) ?? "")
            case "Unit":
                cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.unit) ?? "")
            case "Create Date":
                if create_Date != ""{
                    let timestamp = create_Date[6 ..< 16]
                    let dateValue = convertTimeStamp(timeStamp: timestamp)
                    cell.setData(labelData: labelValues[indexPath.row], fieldValue: dateValue)
                }else{
                    cell.setData(labelData: labelValues[indexPath.row], fieldValue: "")

                }
            case "Department":
                cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.Department) ?? "")
            case "Location":
                if self.ticket?.field == "" && self.ticket?.Department == "Operations"{
                    cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.Location)! + "/" + (self.ticket?.facility ?? "") )
                    cell.inputField.isUserInteractionEnabled = true
                    cell.inputField.delegate = self
                }
                else{
                    cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.Location) ?? "")
                    cell.inputField.isUserInteractionEnabled = true
                    cell.inputField.delegate = self
                }
            case "Job Performed By":
                cell.setData(labelData: labelValues[indexPath.row], fieldValue: (self.ticket?.JobPerformedBy) ?? "")
                
            case "Start":
                let cell2 = tableView.dequeueReusableCell(withIdentifier: "Form1Cell")! as! Form1Cell
                if start_date != ""{
                    let timestamp = start_date[6 ..< 16]
                    let dateValue = convertTimeStamp(timeStamp: timestamp)
                    
                    var timeValue : String?
                    if ticket!.timeFlag == "B" || ticket!.timeFlag == "S"{
                        timeValue = decodeTime(timeString: start_time)
                    }
                    else{
                        timeValue = "-"
                    }
                    
                    cell2.setData(dateKey: "Start Date", timeKey: "Start Time", dateInput: dateValue, timeInput: timeValue ?? "")
                }else{
                    cell2.setData(dateKey: "Start Date", timeKey: "Start Time", dateInput: "", timeInput: "")

                }
                cell2.dateValue.isUserInteractionEnabled = false
                cell2.timeValue.isUserInteractionEnabled = false
                cell2.selectionStyle = .none
                return cell2
            case "End":
                let cell2 = tableView.dequeueReusableCell(withIdentifier: "Form1Cell")! as! Form1Cell
                if end_date != ""{
                let timestamp = end_date[6 ..< 16]
                let dateValue = convertTimeStamp(timeStamp: timestamp)
                var timeValue : String?
                if ticket!.timeFlag == "B" || ticket!.timeFlag == "E"{
                    timeValue = decodeTime(timeString: end_time)
                }
                else{
                    timeValue = "-"
                }
                cell2.setData(dateKey: "End Date", timeKey: "End Time", dateInput: dateValue, timeInput: timeValue ?? "")
                }else{
                    cell2.setData(dateKey: "End Date", timeKey: "End Time", dateInput: "", timeInput: "")
                }
                cell2.dateValue.isUserInteractionEnabled = false
                cell2.timeValue.isUserInteractionEnabled = false
                cell2.selectionStyle = .none
                return cell2
            default:
                print("Defaulted")
            }
            return cell
            
        }
        else if segmentControl.selectedSegmentIndex == 1{
            if hasComment && indexPath.row == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: "CommentsCell")! as! CommentsCell
                cell.isUserInteractionEnabled = false
                cell.selectionStyle = .none
                cell.setData(comment : self.comment)
                return cell
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "ImageCell")! as! ImageCell
                cell.selectionStyle = .none
                var index : Int?
                if hasComment{
                    index = indexPath.row - 1
                }
                else{
                    index = indexPath.row
                }
                cell.setData(attachment : attachmentArray[index!])
                return cell
            }
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if segmentControl.selectedSegmentIndex == 0{
            switch labelValues[indexPath.row] {
            case "":
                return 79
            default:
                return 61
            }
        }
        else{
            if hasComment{
                if indexPath.row == 0{
                    return 150
                }
                else{
                    return 110
                }
            }
            else{
                return 110
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if segmentControl.selectedSegmentIndex == 1 {
            var index : Int?
            if hasComment{
                index = indexPath.row - 1
            }
            else{
                index = indexPath.row
            }
            createImageToView(index: index!)
        }
        
    }
}
