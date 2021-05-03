//
//  WellPadViewController.swift
//  Murphy_PWT_iOS
//
//  Created by Parul Thakur77 on 13/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit
//import ALTextInputBar
import Photos

class WellPadViewController: UIViewController , UITableViewDataSource,UITableViewDelegate{

    @IBOutlet var wellPadTableView: UITableView!
    var wellPadArr = [String]()
    var wellPadArrDisplay = [String]()
   // var senderController : TaskListController?
    var emptyLabel = UILabel()
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    let context = PTWCoreData.shared.managedObjectContext
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        let permitNib = UINib(nibName: "WellCell", bundle: nil)
        wellPadTableView.register(permitNib, forCellReuseIdentifier: "WellCell")
        wellPadTableView.tableFooterView = UIView()
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.wellPadArr.removeAll()
        self.wellPadArrDisplay.removeAll()
        if ConnectionCheck.isConnectedToNetwork(){
            getWellPadData()
        }
        else{
            getOfflineWellPadData()
        }
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
    //    UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        DispatchQueue.main.async {
            self.indicator.stopAnimating()
        }
    }
    
    func getOfflineWellPadData()
    {
        let wellPadservice = WellPadListService(context:self.context)
        self.wellPadArrDisplay.removeAll()
        self.wellPadArr.removeAll()
        var facility : String?
        if !isFromCreateJSA{
            facility = currentLocation.facility
        }else{
            facility = addedLocation.facility
        }
        let searchPredicate = NSPredicate(format: "facility == %@", facility!)
        let wellPadArr = wellPadservice.get(withPredicate: searchPredicate)
        self.wellPadArrDisplay = wellPadArr[0].wellPads as! [String]
        self.wellPadArr = [String]()
        self.wellPadTableView.reloadData()
    }
    
    func getWellPadData()
    {
       // print(JSAObject.location.facility)
        DispatchQueue.main.async {
            self.loaderStart()
        }
        var postData = [String : Any]()
        if !isFromCreateJSA{
         postData   = ["locationType":"Facility",
                                         "navigate":"CHILD",
                                         "location":currentLocation.facilityCode]
        }else{
            postData   = ["locationType":"Facility",
                          "navigate":"CHILD",
                          "location":addedLocation.facilityCode]
        }
        
        print(postData)
        
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
                                let wellPadService = WellPadListService(context: self.context)
                                wellPadService.deleteAll()
                                for arr in tempArr {
                                    let latDisplay = arr["locationText"]!
                                    let lat = arr["location"]!
                                    let childExist = arr["childExist"]! as? String
                                    print(lat)
                                    if childExist == "TRUE"{
                                        self.wellPadArr.append(lat as! String)
                                        self.wellPadArrDisplay.append(latDisplay as! String)
                                    }
                                }
                                var facility : String?
                                if !isFromCreateJSA{
                                    facility = currentLocation.facility
                                }else{
                                    facility = addedLocation.facility
                                }
                                _ = wellPadService.create(facility: facility!, wellPads : self.wellPadArrDisplay)
                                wellPadService.saveChanges()
                                DispatchQueue.main.async {
                                    self.loaderStop()
                                    self.wellPadTableView.reloadData()
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
    
    @IBAction func onBackPress(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if wellPadArrDisplay.count == 0{
            self.emptyLabel.text = "No Data"
            self.emptyLabel.textColor = UIColor.gray
            self.emptyLabel.textAlignment = NSTextAlignment.center
            self.wellPadTableView.backgroundView = self.emptyLabel
            
        }
        else{
            self.emptyLabel.text = ""
        }
        return wellPadArrDisplay.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "WellCell")! as! WellCell
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        cell.setData(labelValue: wellPadArrDisplay[indexPath.row])
        cell.accessoryType = .disclosureIndicator
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 60
    }
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
       // JSAObject.location.facility = wellPadArr[indexPath.row]
        let vc = UIStoryboard(name: "start", bundle: Bundle.main).instantiateViewController(withIdentifier: "WellViewController") as! WellViewController
        var wellPad : String?
        if !isFromCreateJSA{
            currentLocation.wellPad = wellPadArrDisplay[indexPath.row]
            if ConnectionCheck.isConnectedToNetwork(){
                currentLocation.wellPadCode = wellPadArr[indexPath.row]
            }
            wellPad = currentLocation.wellPad
        }
        else{
            addedLocation.wellPad = wellPadArrDisplay[indexPath.row]
            if ConnectionCheck.isConnectedToNetwork(){
                addedLocation.wellPadCode = wellPadArr[indexPath.row]
            }
            wellPad = addedLocation.wellPad
        }
        if ConnectionCheck.isConnectedToNetwork(){
             self.navigationController?.pushViewController(vc, animated: true)
        }
        else{
            let wellList = WellListService(context:self.context)
            let searchPredicate = NSPredicate(format: "wellPad == %@", wellPad!)
            let wellPadArr = wellList.get(withPredicate: searchPredicate)
            if wellPadArr.count > 0{
                self.navigationController?.pushViewController(vc, animated: true)
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
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
        addedLocation = Location()
    }
}
