//
//  SearchViewController.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 17/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

protocol SearchViewControllerDelegate: class {
    func didSelect(searchItem: GenericItem, typeOfSearch: String)
    func didSelect1(searchItem: GenericItem, typeOfSearch: String)
}

class SearchViewController: UIViewController {
    
    var typeofSearch: String?
    weak var delegate: SearchViewControllerDelegate?
    var vendorID : String?
    //For MurphyReviewerSearch
    var Field : HierarchyItem = HierarchyItem(item: NSDictionary())
    var Facility : HierarchyItem = HierarchyItem(item: NSDictionary())
    var WellPad : HierarchyItem = HierarchyItem(item: NSDictionary())
    var Well : HierarchyItem = HierarchyItem(item: NSDictionary())
    
//    var Field : String? = "MUR-US-EFS-CT00"
//    var Facility : String? = "MUR-US-EFS-CT00-KBCF"
//    var WellPad : String? = "MUR-US-EFS-CT00-KBWP-P015"
//    var Well : String? = "MUR-US-EFS-CT00-KBWP-P015-W02"
    var Department : String? = "Well Work"
    var GenericArray : [GenericItem] = []
    var SelectedItemArray:[GenericItem] = []
    var selectedItem : GenericItem?
    var ticket = FieldTicket()
    //var request : DataRequest?
    var reviewers:String?

    @IBOutlet weak var searchbarHeightConstraint: NSLayoutConstraint!
    var completion: (() -> Void)?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    let context = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
    /// Throttle engine
    private var throttler: Throttler? = nil
    
    /// Throttling interval
    public var throttlingInterval: Int? = 1 {
        didSet {
            guard let interval = throttlingInterval else {
                self.throttler = nil
                return
            }
            self.throttler = Throttler(seconds: interval)
        }
    }
    
    
    @IBOutlet weak var SearchBar: UISearchBar!
    @IBOutlet weak var ResultsTable: UITableView!
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
    func createNavBar() {
        
        
        navigationController?.navigationBar.tintColor = UIColor.white
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back"), style: UIBarButtonItem.Style.plain, target: self, action: #selector(dismissController))
        navigationItem.leftBarButtonItems = [backItem]
    }
    
    @objc func dismissController(){
        self.navigationController?.popViewController(animated: true)
        //self.dismiss(animated: true, completion: nil)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboardWhenTappedAround()
        switch typeofSearch! {
        case "CostCenter":
            self.SearchBar.isHidden = false
            self.title = "Cost Center"
            self.SearchBar.placeholder = "Type here to search and select"
        case "POForWO":
            self.title = "PO Selection"
            self.SearchBar.isHidden = true
            searchbarHeightConstraint.constant = 0
            self.SearchBar.placeholder = "Type here to search and select"
        case "SESApprover":
            self.SearchBar.isHidden = false
            self.title = "SES Approver"
            self.SearchBar.placeholder = "Type here to search and select"
        case "PO":
            self.SearchBar.isHidden = false
            self.title = "Murphy PO No"
            self.SearchBar.placeholder = "Type here to search and select"
        case "WO":
            self.SearchBar.isHidden = false
            self.title = "Murphy WO No"
            self.SearchBar.placeholder = "Type here to search and select"
        case "WBS":
            self.SearchBar.isHidden = false
            self.title = "WBS"
            self.SearchBar.placeholder = "Type here to search and select"
        case "MurphyReviewer":
            self.SearchBar.isHidden = true
            searchbarHeightConstraint.constant = 0
            self.title = "Murphy Reviewer"
            self.SearchBar.placeholder = "Type here to search and select"
            self.ResultsTable.setEditing(true, animated: true)
            self.ResultsTable.allowsMultipleSelectionDuringEditing = true
            self.ResultsTable.allowsSelection = false
            let rightButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(doneAction))
                       self.navigationItem.rightBarButtonItem  = rightButton
            self.GetMurphyReviewerList()
         
      //      self.ResultsTable.isUserInteractionEnabled = false
        default:
            break
        }
        createNavBar()
        ResultsTable.delegate = self
        ResultsTable.dataSource = self
        SearchBar.delegate = self
        if typeofSearch! != "MurphyReviewer" && typeofSearch! != "POForWO"{
            SearchBar.becomeFirstResponder()
        }
        ResultsTable.separatorInset = .zero
        ResultsTable.tableFooterView = UIView()
        if let searchTextField = self.SearchBar.value(forKey: "searchField") as? UITextField {
            searchTextField.textColor = .black
            searchTextField.backgroundColor = .white
        }
        // Do any additional setup after loading the view.
    }
    @objc func doneAction(){
           if SelectedItemArray.count > 0{
               selectedItem = SelectedItemArray[0]
               selectedItem?.ItemValue1 = SelectedItemArray.compactMap{$0.ItemValue1}.joined(separator: ",")
               selectedItem?.ItemValue2 = SelectedItemArray.compactMap{$0.ItemValue2}.joined(separator: ",")
               selectedItem?.ItemValue3 = SelectedItemArray.compactMap{$0.ItemValue3}.joined(separator: ",")
               if typeofSearch! == "MurphyReviewer"{
                   self.delegate?.didSelect1(searchItem: selectedItem!, typeOfSearch: typeofSearch!)
               }
               self.navigationController?.popViewController(animated: true)
           }else{
            let alertController = UIAlertController.init(title: "", message:"Please select atleast one Reviewer." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
       }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}

extension SearchViewController:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if GenericArray.count == 0{
            if typeofSearch! == "PO" && (self.SearchBar.text?.count)! < 3 {
                return 1
            }else{
                let rect = CGRect(origin: CGPoint(x: 0,y :0), size: CGSize(width: self.view.bounds.size.width, height: self.view.bounds.size.height))
                let messageLabel = UILabel(frame: rect)
                messageLabel.text = "No Results"
                messageLabel.textColor = UIColor.black
                messageLabel.numberOfLines = 0;
                messageLabel.textAlignment = .center;
                messageLabel.font = UIFont(name: "TrebuchetMS", size: 15)
                messageLabel.sizeToFit()
                ResultsTable.backgroundView = messageLabel
            }
        }else{
            ResultsTable.backgroundView = nil
        }
        return GenericArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch typeofSearch! {
        case "CostCenter":
            let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
            cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
            cell.label2.text = GenericArray[indexPath.row].ItemValue2 ?? ""
            cell.label3.text = ""
            return cell
        case "SESApprover":
            let cell: SearchCell1TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell1TableViewCell") as! SearchCell1TableViewCell
            cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
            return cell
        case "PO":
            if (SearchBar.text?.count)! >= 3 {
                let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
                cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
                cell.label2.text = GenericArray[indexPath.row].ItemValue2 ?? ""
                cell.label3.text = GenericArray[indexPath.row].ItemValue3 ?? ""
                return cell
            }
            else{
                let cell: CustomPOTableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "CustomPOTableViewCell") as! CustomPOTableViewCell
                cell.POInputField.delegate = self
                 return cell
            }
           
        case "WO":
            let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
            cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
            cell.label2.text = GenericArray[indexPath.row].ItemValue2 ?? ""
            cell.label3.text = ""
            return cell
        case "WBS":
            let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
            cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
            cell.label2.text = GenericArray[indexPath.row].ItemValue2 ?? ""
            cell.label3.text = ""
            return cell
        case "MurphyReviewer":
            let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
            cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
            cell.label2.text = GenericArray[indexPath.row].ItemValue2 ?? ""
            cell.label3.text = GenericArray[indexPath.row].ItemValue3 ?? ""
            if reviewers != ""{
            if SelectedItemArray.contains(where:{$0.ItemValue1 == GenericArray[indexPath.row].ItemValue1} ){
                  self.ResultsTable.selectRow(at: indexPath, animated: true, scrollPosition: .none)
             }
            }else{
                 self.ResultsTable.selectRow(at: indexPath, animated: true, scrollPosition: .none)
            }
            return cell
        case "POForWO":
            let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
            cell.label1.text = GenericArray[indexPath.row].ItemValue1 ?? ""
            cell.label2.text = GenericArray[indexPath.row].ItemValue2 ?? ""
            cell.label3.text = GenericArray[indexPath.row].ItemValue3 ?? ""
            return cell

        default:
            print("error in cell for row at")
            let cell: SearchCell2TableViewCell = ResultsTable.dequeueReusableCell(withIdentifier: "SearchCell2TableViewCell") as! SearchCell2TableViewCell
            cell.label1.text = "Error"
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //if (SearchBar.text!.count) >= 3 {
        selectedItem = GenericArray[indexPath.row]
        if typeofSearch! == "MurphyReviewer"{
           // self.delegate?.didSelect1(searchItem: selectedItem!, typeOfSearch: typeofSearch!)
      //      self.ResultsTable.selectRow(at: indexPath, animated: true, scrollPosition: .none)
            SelectedItemArray.append(selectedItem!)
        }
        else{
            self.delegate?.didSelect(searchItem: selectedItem!, typeOfSearch: typeofSearch!)
            self.navigationController?.popViewController(animated: true)

        }
        //}
    }
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        if typeofSearch! == "MurphyReviewer"{
             selectedItem = GenericArray[indexPath.row]
            SelectedItemArray = SelectedItemArray.filter { $0.ItemValue1 != selectedItem?.ItemValue1 }
        }
    }
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.SearchBar.resignFirstResponder()
    }
    func tableView(_ tableView: UITableView, willSelectRowAt indexPath: IndexPath) -> IndexPath? {
        if typeofSearch! == "PO" && SearchBar.text?.count == 0 {
            return nil
        }
        else{
            return indexPath
        }
    }
    
    
}
extension SearchViewController: UISearchBarDelegate{
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        //searchBar.showsCancelButton = true
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        if searchText.count >= 3 {
            self.throttlingInterval = 3
            switch typeofSearch! {
            case "CostCenter":
//                self.CostCenterSearch(searchText: searchText)
                
                guard let throttler = self.throttler else {
                    self.CostCenterSearch(searchText: searchText)
                    return
                }
                throttler.throttle {
                    DispatchQueue.main.async {
                        self.CostCenterSearch(searchText: searchText)
                    }
                }
            
            case "SESApprover":
                self.SESApproverSearch(searchText: searchText)
            case "PO":
                if self.ticket.serviceType == "Salt Water Disposal"{
                    self.POSearchSaltWater(searchText: searchText)
                }
                else{
                    self.POSearch(searchText: searchText)
                }
            case "WO":
                self.WOSearch(searchText: searchText)
            case "WBS":
                self.WBSSearch(searchText: searchText)
          //  case "MurphyReviewer":
              // self.GetMurphyReviewerList(searchText )

            default:
                break
            }
        }
        else{
//            if searchText.count == 0{
//                searchBar.resignFirstResponder()
//            }
            self.GenericArray = []
            self.ResultsTable.reloadData()
            
        }
    }
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
        
    }
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.setShowsCancelButton(false, animated: true)
        if (searchBar.text?.count)! >= 3 {
            self.throttlingInterval = 3
            switch typeofSearch! {
            case "CostCenter":
                
                guard let throttler = self.throttler else {
                    self.CostCenterSearch(searchText: searchBar.text!)
                    return
                }
                throttler.throttle {
                    DispatchQueue.main.async {
                        self.CostCenterSearch(searchText: searchBar.text!)
                    }
                }
                
            case "SESApprover":
                self.SESApproverSearch(searchText: searchBar.text!)
            case "PO":
                if self.ticket.serviceType == "Salt Water Disposal"{
                    self.POSearchSaltWater(searchText: searchBar.text!)
                }
                else{
                    self.POSearch(searchText: searchBar.text!)
                }
                
            case "WO":
                self.WOSearch(searchText: searchBar.text!)
            case "WBS":
                self.WBSSearch(searchText: searchBar.text!)
                
            default:
                break
            }
        }
        else{
            if searchBar.text?.count == 0{
                searchBar.resignFirstResponder()
            }
            self.GenericArray = []
            self.ResultsTable.reloadData()
            
        }
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar){
        searchBar.setShowsCancelButton(false, animated: true)
        searchBar.text = ""
        searchBar.resignFirstResponder()
        
    }
}
//API Calls
extension SearchViewController{
    func CostCenterSearch(searchText : String){
        
        //self.request?.cancel()
        self.loaderStart()
        let url = "\(BaseUrl.apiURL)/com.OData.dest//Cost_CenterSet?$filter=KOSTL eq '\(searchText)'&$skip=0&$top=50&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.GenericArray.removeAll()
                                    for each in result{
                                        let item = CostCenter(item: each)
                                        self.GenericArray.append(item)
                                    }
                                    self.loaderStop()
                                    self.ResultsTable.reloadData()
                                }
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    func SESApproverSearch(searchText : String){
        
        self.loaderStart()
        //self.request?.cancel()
        let url = "\(BaseUrl.apiURL)/com.OData.dest/SES_Approver_SearchSet?$filter=FirstName eq '\(searchText)'&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                       if let jsonDict = JSON as? NSDictionary {
                           if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                               if let result = d.value(forKey: "results") as? [NSDictionary]{
                                   self.GenericArray.removeAll()
                                   for each in result{
                                       let item = SESApprover(item: each)
                                       self.GenericArray.append(item)
                                   }
                                   self.loaderStop()
                                   self.ResultsTable.reloadData()
                               }
                           }
                           
                       }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    func POSearch(searchText : String){
        
        self.loaderStart()
       // self.request?.cancel()
        let timestamp = self.ticket.start_timestamp[6 ..< 16]
        let dateValue = convertTimeStamp(timeStamp: timestamp)
        var facilityLocation = ""
        var location = ""
        if self.ticket.Department == "Operations"{
            facilityLocation = self.ticket.facilityCode!
            
            if self.ticket.wellPad == ""{
                location = self.ticket.facilityCode!
            }
            else if self.ticket.well == ""{
                location = self.ticket.wellPadCode!
            }
            else{
                location = self.ticket.wellCode!
            }
        }
        
        //PO_Number eq '\(searchText)' and Vendor eq '\(self.vendorID!)' and Location eq 'LOCATION' and Start_Date eq datetime'2016-03-15T00:00:00' yyyy-MM-ddTHH:mm:ss
        let url = "\(BaseUrl.apiURL)/com.OData.dest/GetPObyVendorSet?$filter=PO_Number eq '\(searchText)' and Vendor eq '\(self.vendorID!)' and Location eq '\(facilityLocation)' and HierLocation eq '\(location)' and Start_Date eq datetime'\(dateValue)'&$skip=0&$top=50&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        self.loaderStop()
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.GenericArray.removeAll()
                                    for each in result{
                                        let element = PO(item:each)
                                        self.GenericArray.append(element)
                                    }
                                    self.ResultsTable.reloadData()
                                }
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
        let s_timestamp = self.ticket.start_timestamp[6 ..< 16]
        let s_dateValue = convertTimeStamp(timeStamp: s_timestamp)
        let e_timestamp = self.ticket.end_timestamp[6 ..< 16]
        let e_dateValue = convertTimeStamp(timeStamp: e_timestamp)
        //\(self.vendorID!)
        let url = "\(BaseUrl.apiURL)/com.OData.dest/GetPOForSaltWaterSet?$filter=PO_Number eq '\(searchText)' and Vendor eq '\(self.vendorID!)' and Start_Date eq datetime'\(s_dateValue)' and End_Date eq datetime'\(e_dateValue)' and Material_Group eq 'S71131498'&$skip=0&$top=50&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
       
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.GenericArray.removeAll()
                                    for each in result{
                                        let element = PO(item:each)
                                        self.GenericArray.append(element)
                                    }
                                    self.loaderStop()
                                    self.ResultsTable.reloadData()
                                }
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    func WOSearch(searchText : String){
        
        //self.request?.cancel()
        self.loaderStart()
        let url = "\(BaseUrl.apiURL)/com.OData.dest/WO_RunningSearchSet?$filter=WO_Number eq '\(searchText)'&$skip=0&$top=50&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.GenericArray.removeAll()
                                    for each in result{
                                        let element = WO(item:each)
                                        self.GenericArray.append(element)
                                    }
                                    self.loaderStop()
                                    self.ResultsTable.reloadData()
                                }
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    func WBSSearch(searchText : String){
        
        self.loaderStart()
        //self.request?.cancel()
        let url = "\(BaseUrl.apiURL)/com.OData.dest/WBSSet?$filter=posid eq '\(searchText)'&$skip=0&$top=50&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.GenericArray.removeAll()
                                    for each in result{
                                        let element = WBS(item:each)
                                        self.GenericArray.append(element)
                                    }
                                    self.loaderStop()
                                    self.ResultsTable.reloadData()
                                }
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    func ValidatePO(PONum : String){
        
        self.loaderStart()
        //self.request?.cancel()
        let url = "\(BaseUrl.apiURL)/com.OData.dest/ValidatePOSet(PO_Number='\(PONum)')?$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        self.loaderStop()
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if d.value(forKey: "MESSAGE") as! String == "PO Number Exists."{
                                    self.selectedItem = PO(item: d)
                                    self.delegate?.didSelect(searchItem: self.selectedItem!, typeOfSearch: self.typeofSearch!)
                                    self.navigationController?.popViewController(animated: true)
                                    
                                }
                                else{
                                    let message = d.value(forKey: "MESSAGE") as! String
                                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                                    alertController.addAction(okAction)
                                    self.present(alertController, animated: true, completion: nil)
                                    self.loaderStop()
                                }
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    func GetMurphyReviewerList()
    {
        
        self.loaderStart()
        //self.request?.cancel()
        let url = "\(BaseUrl.apiURL)/com.dft.xsjs/RulesProject/GetReviewerByLocation.xsjs"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        let payload : [String : String] = ["field_code" : self.Field.ItemValue2 ?? "" , "facility_code" : self.Facility.ItemValue2 ?? "" , "wellpad_code": self.WellPad.ItemValue2 ?? "" , "well_code" : self.Well.ItemValue2 ?? "" , "department" : self.Department!]
        if ConnectionCheck.isConnectedToNetwork(){
            
            var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
            urlRequest.httpMethod = "post"
            do{
                let requestBody = try JSONSerialization.data(withJSONObject: payload, options: .fragmentsAllowed)
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
                            self.loaderStop()
                            let reviewerService = ReviewerService(context:self.context)
                            _ = reviewerService.create(department: self.Department!, field: self.Field.ItemValue1 ?? "", facility: self.Facility.ItemValue1 ?? "", wellPad: self.WellPad.ItemValue1 ?? "", well: self.Well.ItemValue1 ?? "", response: JSON as! NSArray)
                            reviewerService.saveChanges()
                            if let jsonArray = JSON as? NSArray {
                                self.GenericArray.removeAll()
                                self.SelectedItemArray.removeAll()
                                for i in jsonArray{
                                    self.GenericArray.append(MurphyReviewer(item: i as! [String: String]))
                                    self.SelectedItemArray.append(MurphyReviewer(item: i as! [String: String]))
                                    if (self.reviewers != ""){
                                        self.SelectedItemArray.removeAll()
                                        for reviewer in (self.reviewers?.components(separatedBy: ","))!{
                                            for (_,value) in self.GenericArray.enumerated(){
                                                if value.ItemValue1 == reviewer{
                                                    self.SelectedItemArray.append(value)
                                                }
                                            }
                                        }
                                    }
                                    self.ResultsTable.reloadData()
                                }
                                
                            }
                            
                        }catch {
                            print(error.localizedDescription, "StatusCode: \(response!)")
                        }
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
        else{
            self.loaderStop()
            let reviewerService = ReviewerService(context:self.context)
            var dbResponse = [Reviewer]()
            
            let predicate1 = NSPredicate(format: "department == %@", self.Department!)
            let predicate2 = NSPredicate(format: "field == %@", self.Field.ItemValue1 ?? "")
            let predicate3 = NSPredicate(format: "facility == %@", self.Facility.ItemValue1 ?? "")
            let predicate4 = NSPredicate(format: "wellPad == %@", self.WellPad.ItemValue1 ?? "")
            let predicate5 = NSPredicate(format: "well == %@", self.Well.ItemValue1 ?? "")
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
            if self.Well.ItemValue1 == nil && self.WellPad.ItemValue1 == nil && self.Facility.ItemValue1 == nil && self.Field.ItemValue1 != nil
            {
                dbResponse = reviewerService.get(withPredicate: fieldSearchPredicate)
                if dbResponse.count == 0{
                    let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    parseReviewer(response: dbResponse[0].response!)
                }
            }
            else if self.Well.ItemValue1 == nil && self.WellPad.ItemValue1 == nil && self.Facility.ItemValue1 != nil && self.Field.ItemValue1 != nil{
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
                        parseReviewer(response: dbResponse[0].response!)
                    }
                }
                else{
                    parseReviewer(response: dbResponse[0].response!)
                }
            }
            else if self.Well.ItemValue1 == nil && self.WellPad.ItemValue1 != nil && self.Facility.ItemValue1 != nil && self.Field.ItemValue1 != nil{
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
                            parseReviewer(response: dbResponse[0].response!)
                        }
                    }
                    else{
                        parseReviewer(response: dbResponse[0].response!)
                    }
                }
                else{
                    parseReviewer(response: dbResponse[0].response!)
                }
            }
            else if self.Well.ItemValue1 != nil && self.WellPad.ItemValue1 != nil && self.Facility.ItemValue1 != nil && self.Field.ItemValue1 != nil{
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
                                parseReviewer(response: dbResponse[0].response!)
                            }
                        }
                        else{
                            parseReviewer(response: dbResponse[0].response!)
                        }
                    }
                    else{
                        parseReviewer(response: dbResponse[0].response!)
                    }
                }
                else{
                    parseReviewer(response: dbResponse[0].response!)
                }
            }
        }
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
    

}
extension SearchViewController: UITextFieldDelegate{

    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.keyboardType = .numberPad
        addToolBar(textField: textField)
    }
    
//    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
//
//        //Validate PO
//        if let str = textField.text {
//            if str.count > 10{
//                let alertController = UIAlertController.init(title: "", message:"PO can't be over 10 characters!" , preferredStyle: UIAlertControllerStyle.alert)
//                let okAction = UIAlertAction.init(title: "OK", style: UIAlertActionStyle.cancel, handler: nil)
//                alertController.addAction(okAction)
//                self.present(alertController, animated: true, completion: nil)
//
//            }
//        else{
//            self.ValidatePO(PONum: textField.text!)
//            textField.resignFirstResponder()
//
//        }
//        }
//        return true
//    }
    
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        //Validate PO
        if let str = textField.text {
            if str.count > 10{
                let alertController = UIAlertController.init(title: "", message:"PO can't be over 10 characters!" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
                
            }
            else{
                if let str = textField.text {
                    if str.count > 0{
                        self.ValidatePO(PONum: textField.text!)
                        textField.resignFirstResponder()
                    }
                }
            }
        }
    }
    
    func addToolBar(textField: UITextField) {
        let toolBar = UIToolbar()
        toolBar.barStyle = .default
        toolBar.isTranslucent = true
        toolBar.barTintColor = UIColor(red: 77/255, green: 184/255, blue: 255/255, alpha: 1)
        let doneButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(donePressed))
        let cancelButton = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(cancelPressed))
        let spaceButton = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        
        
        toolBar.isUserInteractionEnabled = true
        toolBar.sizeToFit()
        
        textField.delegate = self
        textField.inputAccessoryView = toolBar
    }
    
    @objc func donePressed() {
        view.endEditing(true)
    }
    
    @objc func cancelPressed() {
        view.endEditing(true) // or do something
    }
}

extension SearchViewController {
    func parseReviewer(response : NSArray)
    {
        self.GenericArray.removeAll()
        for i in response{
            
            self.GenericArray.append(MurphyReviewer(item: i as! [String: String]))
            self.ResultsTable.reloadData()
        }
    }
}
