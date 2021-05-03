//
//  LocationViewController.swift
//  Murphy_PWT_iOS
//
//  Created by Parul Thakur77 on 13/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit
//import ALTextInputBar
import Photos

var addedLocation = Location()
var isFromCreateJSA = true
class LocationViewController: UIViewController , UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var checkBtn: UIBarButtonItem!
    var locationCentralArr = [String]()
    var locationArrayDisplay = [String]()
    var selectStatus : Bool = false
    var facilityStr : String = ""
    var facilityDisplay : String = ""
    var selectedIndex : Int = 0
//    var senderController : TaskListController?
    let context = PTWCoreData.shared.managedObjectContext
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    
    @IBOutlet var locationTableView: UITableView!
    
    
    override func viewWillAppear(_ animated: Bool) {
        //        self.locationArrayDisplay.removeAll()
        //        self.locationCentralArr.removeAll()
        
    }
    override func viewDidLoad() {
        addedLocation = Location()
        super.viewDidLoad()
        checkBtn.isEnabled = false
        checkBtn.tintColor = UIColor.red.withAlphaComponent(0.0)
        let permitNib = UINib(nibName: "LocationCell", bundle: nil)
        locationTableView.register(permitNib, forCellReuseIdentifier: "LocationCell")
        if ConnectionCheck.isConnectedToNetwork(){
            self.getLocationData()
        }
        else{
            self.getOfflineLocationData()
        }
        locationTableView.tableFooterView = UIView()
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
      //  UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        DispatchQueue.main.async {
            self.indicator.stopAnimating()
        }
    }
    
    func getOfflineLocationData(){
        
        let facilityService = FacilityListService(context:self.context)
        self.locationArrayDisplay.removeAll()
        self.locationCentralArr.removeAll()
        let allFacilities = facilityService.getAll()
        for each in allFacilities{
            self.locationArrayDisplay = each.facility as! [String]
            self.locationCentralArr = [String]()
        }
        self.locationTableView.reloadData()
        
    }
    
    func getLocationData()
    {
        DispatchQueue.main.async {
            self.loaderStart()
        }
        let postData : [String : Any] = ["locationType":"Facility",
                                         "navigate":"",
                                         "location":""]
        let header = ["Content-Type" : "Application/json"]
        
        //        Alamofire.request("https://taskmanagementrestdfe0918b2.us2.hana.ondemand.com/TaskManagement_Rest/murphy/location/getLocation"
        let urlString : String = IMOEndpoints.locationService
            //"\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/location/getLocation"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "post"
        urlRequest.httpBody = self.getHttpBodayData(params: postData)
        urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
        
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    if let jsonDict = JSON as? NSDictionary {
                        if let dtoDict = jsonDict["dto"] as? NSDictionary{
                            
                            if let tempArr = dtoDict["locationHierarchy"] as? [[String:Any]] {
                                self.locationCentralArr.removeAll()
                                self.locationArrayDisplay.removeAll()
                                let facilityService = FacilityListService(context:self.context)
                                facilityService.deleteAll()
                                for arr in tempArr {
                                    let latDisplay = arr["locationText"]!
                                    let lat = arr["location"]!
                                    self.locationCentralArr.append(lat as! String)
                                    self.locationArrayDisplay.append(latDisplay as! String)
                                }
                                _ = facilityService.create(facility: self.locationArrayDisplay)
                                facilityService.saveChanges()
                                DispatchQueue.main.async {
                                    self.loaderStop()
                                    self.locationTableView.reloadData()
                                }
                            }
                            
                        }
                    }
                    
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
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
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return locationArrayDisplay.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LocationCell")! as! LocationCell
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        cell.setData(labelValue: locationArrayDisplay[indexPath.row])
        cell.checkButton.tag = indexPath.row
        cell.accessoryType = .disclosureIndicator
        cell.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
        
        if indexPath.row == selectedIndex && selectStatus == true
        {
            //checkBtn.isEnabled = true
            //checkBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
            cell.checkButton.setImage(UIImage (named: "checked"), for: .normal)
            //selectStatus = false
        }
        else
        {
            cell.checkButton.setImage(UIImage (named: "unchecked"), for: .normal)
        }
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 60
    }
    
    @objc func buttonSelected(_ sender: UIButton)
    {
        checkBtn.isEnabled = true
        checkBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        
        if ConnectionCheck.isConnectedToNetwork(){
            if self.locationCentralArr.count > 0{
                facilityStr = self.locationCentralArr[sender.tag]
            }
        }
        facilityDisplay = self.locationArrayDisplay[sender.tag]
        
        if selectedIndex == sender.tag
        {
            if selectStatus == true
            {
                checkBtn.isEnabled = false
                checkBtn.tintColor = UIColor.white.withAlphaComponent(0.0)
                sender.setImage(UIImage (named: "unchecked"), for: .normal)
                selectStatus = false
            }
            else
            {
                if ConnectionCheck.isConnectedToNetwork(){
                    if self.locationCentralArr.count > 0{
                        facilityStr = self.locationCentralArr[sender.tag]
                    }
                }
                facilityDisplay = self.locationArrayDisplay[sender.tag]
                checkBtn.isEnabled = true
                checkBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
                sender.setImage(UIImage (named: "checked"), for: .normal)
                selectStatus = true
            }
            
        }
        else
        {
            if ConnectionCheck.isConnectedToNetwork(){
                if self.locationCentralArr.count > 0{
                    facilityStr = self.locationCentralArr[sender.tag]
                }
            }
            facilityDisplay = self.locationArrayDisplay[sender.tag]
            checkBtn.isEnabled = true
            checkBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
            sender.setImage(UIImage (named: "checked"), for: .normal)
            selectStatus = true
            
        }
        selectedIndex = sender.tag
        locationTableView.reloadData()
    }
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if !isFromCreateJSA{
            currentLocation.facilityOrSite = locationArrayDisplay[indexPath.row]
            currentLocation.hierarchyLevel = "facility"
            currentLocation.facility = locationArrayDisplay[indexPath.row]
            currentLocation.muwi = ""
            if ConnectionCheck.isConnectedToNetwork(){
                currentLocation.facilityCode = locationCentralArr[indexPath.row]
            }
        }
        else{
            addedLocation.facilityOrSite = locationArrayDisplay[indexPath.row]
            addedLocation.hierarchyLevel = "facility"
            addedLocation.facility = locationArrayDisplay[indexPath.row]
            addedLocation.muwi = ""
            if ConnectionCheck.isConnectedToNetwork(){
                addedLocation.facilityCode = locationCentralArr[indexPath.row]
            }
        }
        let dashBoardVC = UIStoryboard(name: "start", bundle: Bundle.main).instantiateViewController(withIdentifier: "WellPadViewController") as! WellPadViewController
        if ConnectionCheck.isConnectedToNetwork(){
            self.navigationController?.pushViewController(dashBoardVC, animated: true)
        }
        else{
            let wellPadservice = WellPadListService(context:self.context)
            
            var facility : String?
            if !isFromCreateJSA{
                facility = currentLocation.facility
            }else{
                facility = addedLocation.facility
            }
            let searchPredicate = NSPredicate(format: "facility == %@", facility!)
            let wellPadArr = wellPadservice.get(withPredicate: searchPredicate)
            if wellPadArr.count > 0{
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }
            else{
                let alert = UIAlertController(title: "", message: "Data not available", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
                
                self.present(alert, animated: true, completion: {
                    print("completion block")
                })
            }
        }
        
    }
    @IBAction func onCheckPress(_ sender: Any)
    {
        if selectStatus == true
        {
            if !isFromCreateJSA{
                currentLocation.facilityOrSite = facilityDisplay
                currentLocation.hierarchyLevel = "facility"
                currentLocation.facility = facilityDisplay
                if ConnectionCheck.isConnectedToNetwork(){
                    currentLocation.facilityCode = facilityStr
                }
                currentLocation.muwi = ""
                currentLocation.wellcheck = false
            }
            else{
                addedLocation.facilityOrSite = facilityDisplay
                addedLocation.hierarchyLevel = "facility"
                addedLocation.facility = facilityDisplay
                if ConnectionCheck.isConnectedToNetwork(){
                    addedLocation.facilityCode = facilityStr
                }
                addedLocation.muwi = ""
                addedLocation.wellcheck = false
            }
            if !ConnectionCheck.isConnectedToNetwork(){
                let peopleListService = PeopleListService(context: self.context)
                let searchPredicate = NSPredicate(format: "facilityOrSite == %@", currentLocation.facility)
                let peopleList = peopleListService.get(withPredicate: searchPredicate)
                if peopleList.count == 0{
                    let alertController = UIAlertController.init(title: "", message:"Data not available for this location" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    self.navigationController?.popViewController(animated: true)
                }
            }
            else{
                self.navigationController?.popViewController(animated: true)
            }
        }
        else
        {
            let alertController = UIAlertController.init(title: "", message:"No facility is selected." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
        addedLocation = Location()
        currentLocation = Location()
    }
    
    
}

extension UIViewController{
    func getHttpBodayData(params:[String:Any]) -> Data?{
        let data = try! JSONSerialization.data(withJSONObject: params, options: JSONSerialization.WritingOptions.prettyPrinted)
        let json = NSString(data: data, encoding: String.Encoding.utf8.rawValue)
        if let json = json {
            
        }
        
        return json!.data(using: String.Encoding.utf8.rawValue)
    }
}
