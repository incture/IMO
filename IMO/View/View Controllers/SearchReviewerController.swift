//
//  SearchReviewerController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 09/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit


//class GenericItem {
//    var ItemNumber : String?
//    var ItemName : String?
//    init(item : NSObject) {
//        self.ItemName = item.value(forKey: "LTEXT") as! String
//        self.ItemNumber = item.value(forKey: "KOSTL") as! String
//    }
//    }

class SearchReviewerController: UIViewController, UISearchBarDelegate {
    
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var searchTableView: UITableView!
    @IBOutlet var cancelButton: UIButton!
    
    @IBOutlet var clearButton: UIButton!
    
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var idArray = [String]()
    var emailArray = [String]()
    var displayNameArray = [String]()
    var sesApproverArray = [String]()
    var poNumberArray = [String]()
    //
    var GenericArray = [GenericItem]()
    var ccArray = [String]()
    var searchList = [String]()
    var emptyLabel = UILabel()
    var sender : CreateTicketController?
    var reviewerSender : AttestController?
    var isProvider : Bool?
    var isSesApprover : Bool = false
    var ReviewerList = [MurphyEngineer]()
    var vendorID : String?
    //
    var typeofSearch: String?
    override func viewDidLoad() {
        super.viewDidLoad()
        // createNavBar()
        customizeUI()
        self.searchTableView.register(UITableViewCell.self, forCellReuseIdentifier: "cellReuseIdentifier")
        searchBar.delegate = self
        searchBar.tintColor = UIColor(red: 26.0/255, green: 78.0/255, blue: 126.0/255, alpha: 1)
        cancelButton.setImage(UIImage(named : "delete")?.withRenderingMode(.alwaysTemplate), for: .normal)
        cancelButton.tintColor = UIColor.white
        //searchBar.barTintColor = UIColor.lightGray
        self.searchTableView.tableFooterView = UIView()
        //downloadUserList()
        searchBar.becomeFirstResponder()
        if isProvider! == false && isSesApprover == false{
            searchBar.placeholder = "Search for PO number"
            clearButton.isEnabled = true
            clearButton.isHidden = false
        }
        else if isProvider! == false && isSesApprover == true{
            searchBar.placeholder = "Search for SES Approver"
            clearButton.isEnabled = false
            clearButton.isHidden = true
        }
        else{
            clearButton.isEnabled = false
            clearButton.isHidden = true
        }
        if let searchTextField = self.searchBar.value(forKey: "searchField") as? UITextField {
            searchTextField.textColor = .black
            searchTextField.backgroundColor = .white
        }
       
        
        // Do any additional setup after loading the view.
    }
    
    func handleTap(_ sender: UITapGestureRecognizer) {
        
    }
    
    func createNavBar() {
        
        
        navigationController?.navigationBar.tintColor = UIColor.white
        
        navigationController?.navigationBar.barTintColor = UIColor(red: 1.0/255, green: 38/255, blue: 90/255, alpha: 1)
        let backItem = UIBarButtonItem.init(image: UIImage(named : "delete"), style: UIBarButtonItem.Style.plain, target: self, action: #selector(dismissController))
        
        navigationItem.leftBarButtonItems = [backItem]
        if isProvider! == false && isSesApprover == false{
            navigationItem.title = "PO lookup"
            
        }
        else if isProvider! == false && isSesApprover == true{
            navigationItem.title = "SES Approver Search"
        }
        else{
            navigationItem.title = "Reviewer Search"
        }
        
        if typeofSearch == "CostCenter"{
            self.navigationItem.title = "Cost Center"
        }
    }
    
    @objc func dismissController()
    {
        self.dismiss(animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onBackPress(_ sender: UIButton){
        self.dismiss(animated: false, completion: nil)
    }
    
    
    @IBAction func onClearPress(_ sender: Any) {
       // reviewerSender?.clearPOfield()
        self.dismiss(animated: false, completion: nil)
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
    
    func customizeUI()
    {
        self.view.backgroundColor = UIColor.darkGray.withAlphaComponent(0.4)
        
        let shadowPath = UIBezierPath(rect: self.searchBar.bounds)
        searchBar.layer.shadowColor = UIColor.black.cgColor
        searchBar.layer.shadowOffset = CGSize(width: 0.0, height: 5.0)
        searchBar.layer.shadowOpacity = 0.5
        searchBar.layer.shadowPath = shadowPath.cgPath
        
    }
    
    //SES_Approver_SearchSet?$filter=FirstName eq 'mon'
    
    
    func sesApproverSearch(searchText : String){
        
        self.loaderStart()
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/SES_Approver_SearchSet?$filter=FirstName eq '\(searchText)'&$format=json"
       // let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        
        
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
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self!.sesApproverArray.removeAll()
                                    for each in result{
                                        let element = each.value(forKey: "EmailId") as? String
                                        self!.sesApproverArray.append(element!)
                                    }
                                    self!.loaderStop()
                                    self!.searchTableView.reloadData()
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
    
    func CostCenterSearch(searchText : String){
        
        self.loaderStart()
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest//Cost_CenterSet?$filter=KOSTL eq '\(searchText)'&$format=json"
        let encodedUrl = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            
            
            DispatchQueue.main.async {
                self!.loaderStop()
            }
            if error == nil  {
                DispatchQueue.main.async {
                    do{
                        guard let data = data else { return }
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self!.GenericArray.removeAll()
                                    for each in result{
                                        let item = CostCenter(item: each)
                                        self!.GenericArray.append(item)
                                    }
                                    self!.loaderStop()
                                    self!.searchTableView.reloadData()
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
    
    
    func POrunningSearch(searchText : String){
        
        self.loaderStart()
        let user = UserDefaults.standard.string(forKey: "id")
        let url = "\(BaseUrl.apiURL)/com.OData.dest/GetPObyVendorSet?$filter=substringof('\(searchText)',EBELN) and substringof('\(self.vendorID!)',LIFNR)&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        
        
        //
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            var numberOfObjects = 0
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
                                    self.poNumberArray.removeAll()
                                    for each in result{
                                        let element = each.value(forKey: "EBELN") as? String
                                        self.poNumberArray.append(element!)
                                    }
                                    self.loaderStop()
                                    self.searchTableView.reloadData()
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
    
//    func downloadUserList()
//    {
//        if let data = UserDefaults.standard.data(forKey: "murphyengineer"),
//            let reviewerData = NSKeyedUnarchiver.unarchiveObject(with: data) as? [MurphyEngineer]{
//            ReviewerList.removeAll()
//            self.displayNameArray.removeAll()
//            self.idArray.removeAll()
//            self.emailArray.removeAll()
//            for element in reviewerData{
//
//                let murphyReviewer : GenericItem = MurphyReviewer(item: element)
//                GenericArray.append(murphyReviewer)
//                murphyReviewer.emailID = element.emailID!
//                murphyReviewer.displayName = element.displayName!
//                murphyReviewer.id = element.id!
//                ReviewerList.append(murphyReviewer)
//                self.emailArray.append(element.emailID!)
//                self.displayNameArray.append(element.displayName!)
//                self.idArray.append(element.id!)
//            }
//
//        }
//    }
//
    func searchUser(searchText : String)
    {
        let searchPredicate = NSPredicate(format: "SELF CONTAINS[c] %@", searchText)
        
        self.searchList.removeAll()
        let array = (self.displayNameArray as NSArray).filtered(using: searchPredicate)
        self.searchList = array as! [String]
        self.searchTableView.reloadData()
    }
    
    
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        //searchBar.showsCancelButton = true
        searchBar.setShowsCancelButton(false, animated: true)
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchBar.setShowsCancelButton(false, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        if (searchBar.text?.count)! < 20{
            if isProvider!{
                searchUser(searchText : searchText)
            }
            else{
//                if isSesApprover == true{
                    switch typeofSearch!{
                    case "CostCenter":
                        CostCenterSearch(searchText: searchText)
                    default:
                    sesApproverSearch(searchText : searchText)
                    }
//                }
//                else{
//                    if (searchBar.text?.characters.count)! > 2 && (searchBar.text?.characters.count)! != 0{
//                        POrunningSearch(searchText: searchText)
//                    }
//                    else{
//                        self.poNumberArray.removeAll()
//                        self.searchTableView.reloadData()
//                    }
                }
            }
        
        print("searchText \(searchText)")
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        print("searchText \(String(describing: searchBar.text))")
        if (searchBar.text?.count)! < 20{
            if isProvider!{
                searchUser(searchText : searchBar.text!)
            }
            else{
                if isSesApprover == true{
                    sesApproverSearch(searchText : searchBar.text!)
                }
                else{
                    POrunningSearch(searchText: searchBar.text!)
                }
            }
        }
        searchBar.resignFirstResponder()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar){
        searchBar.resignFirstResponder()
        
    }
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}

extension SearchReviewerController: UITableViewDelegate, UITableViewDataSource {
    
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        var count : Int?
        if isProvider! == true && isSesApprover == false{
            count = searchList.count
        }
                else{
            switch typeofSearch!{
            case "CostCenter":
                count = GenericArray.count
            
            default:
                break
                
            }
        }
        
        if count == 0{
            emptyLabel.text = "No Data"
            emptyLabel.textColor = UIColor.gray
            emptyLabel.textAlignment = NSTextAlignment.center
            self.searchTableView.backgroundView = emptyLabel
            self.searchTableView.separatorStyle = UITableViewCell.SeparatorStyle.none
        }
        else{
            emptyLabel.text = ""
            
        }
        return count!
        
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        
        return 45
        
    }
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        view.tintColor = UIColor.white
        
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 45))
        let label = UILabel(frame: CGRect(x: 16, y: 0, width: headerView.frame.size.width, height: 45))
        label.text = "Search List"
        label.font = UIFont.boldSystemFont(ofSize: 14)
        label.textColor = UIColor.black
        headerView.backgroundColor = UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        return headerView
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
     
        if isProvider! == true && isSesApprover == false{
            let cell:UITableViewCell = self.searchTableView.dequeueReusableCell(withIdentifier: "cellReuseIdentifier")! as UITableViewCell
            cell.textLabel?.font = UIFont.init(name: "Helvetica", size: 14) //UIFont.systemFont(ofSize: 20, weight: UIFontWeightSemibold)
            cell.textLabel?.textColor = UIColor.darkGray
            cell.textLabel?.text = searchList[indexPath.row]
            return cell
        }
        else {
            switch typeofSearch!{
            case "CostCenter":
                let cell : CCTableViewCell = self.searchTableView.dequeueReusableCell(withIdentifier: "CCcell")! as! CCTableViewCell
                
                cell.textLabel?.font = UIFont.init(name: "Helvetica", size: 14) //UIFont.systemFont(ofSize: 20, weight: UIFontWeightSemibold)
                cell.textLabel?.textColor = UIColor.darkGray
                cell.CCIdLabel.text = GenericArray[indexPath.row].ItemValue1!
                cell.CCNameLabel.text = GenericArray[indexPath.row].ItemValue2!
                  return cell
            default:
                let cell : CCTableViewCell = self.searchTableView.dequeueReusableCell(withIdentifier: "CCcell")! as! CCTableViewCell
                 return cell
            }
           
        }
        
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return 35
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if isProvider! == true && isSesApprover == false{
            var selectedReviewer : MurphyEngineer?
            for each in ReviewerList{
                if each.displayName == searchList[indexPath.row]{
                    selectedReviewer = each
                }
            }
           // sender?.setDataFromRunningSearch(reviewer: selectedReviewer!)
        }
        else if isProvider! == false && isSesApprover == true{
            reviewerSender?.setSESApprover(sesApprover: sesApproverArray[indexPath.row])
        }
        else{
           // reviewerSender?.setPONumber(poNumber: poNumberArray[indexPath.row])
        }
        self.dismiss(animated: false, completion: nil)
    }
    
}
