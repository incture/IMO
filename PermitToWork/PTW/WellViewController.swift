//
//  WellViewController.swift
//
//
//  Created by Parul Thakur77 on 13/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit
//import ALTextInputBar
import Photos

class WellViewController: UIViewController  , UITableViewDataSource,UITableViewDelegate{
    @IBOutlet weak var checkBtn: UIBarButtonItem!
    @IBOutlet var wellTableView: UITableView!
    var wellArr = [String]()
    var wellArrDisplay = [String]()
    var wellMuwiArr = [String]()
    var muwiStr : String = ""
    var wellStr = ""
    var wellDisplay = ""
    var selectedIndex : Int = 0
    var selectStatus : Bool = false
    //var senderController : TaskListController?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    let context = PTWCoreData.shared.managedObjectContext
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        checkBtn.isEnabled = false
        checkBtn.tintColor = UIColor.red.withAlphaComponent(0.0)
        
        let permitNib = UINib(nibName: "LocationCell", bundle: nil)
        wellTableView.register(permitNib, forCellReuseIdentifier: "LocationCell")
        wellTableView.tableFooterView = UIView()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.wellArr.removeAll()
        self.wellArrDisplay.removeAll()
        self.wellMuwiArr.removeAll()
        if ConnectionCheck.isConnectedToNetwork(){
            self.getWellData()
        }
        else{
            self.getOfflineWellData()
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
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
    
    func getOfflineWellData()
    {
        let wellListService = WellListService(context:self.context)
        self.wellArrDisplay.removeAll()
        self.wellMuwiArr.removeAll()
        self.wellArr.removeAll()
        var wellPad : String?
        if !isFromCreateJSA{
            wellPad = currentLocation.wellPad
        }else{
            wellPad = addedLocation.wellPad
        }
        let searchPredicate = NSPredicate(format: "wellPad == %@", wellPad!)
        let wellList = wellListService.get(withPredicate: searchPredicate)
        self.wellArrDisplay = wellList[0].wellList as! [String]
        self.wellMuwiArr = wellList[0].muwiList as! [String]
        self.wellArr = [String]()
        self.wellTableView.reloadData()
    }
    
    func getWellData()
    {
        DispatchQueue.main.async {
            self.loaderStart()
        }
        var postData = [String : Any]()
        
        if !isFromCreateJSA{
            postData = ["locationType":"Well Pad",
                        "navigate":"CHILD",
                        "location":currentLocation.wellPadCode]
        }else{
            postData = ["locationType":"Well Pad",
                        "navigate":"CHILD",
                        "location":addedLocation.wellPadCode]
        }
        print(postData)
        let urlString : String = IMOEndpoints.locationService 
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
                                let wellListService = WellListService(context: self.context)
                                wellListService.deleteAll()
                                for arr in tempArr {
                                    let latDisplay = arr["locationText"]!
                                    let lat = arr["location"]!
                                    let muwi = arr["muwi"]! as? String
                                    print(lat)
                                    if muwi != "<null>" && muwi != "" && muwi != nil{
                                        self.wellArr.append(lat as! String)
                                        self.wellArrDisplay.append(latDisplay as! String)
                                        self.wellMuwiArr.append(muwi!)
                                    }
                                }
                                 if !isFromCreateJSA{
                                   _ = wellListService.create(facility: currentLocation.facility, wellPad: currentLocation.wellPad, wells: self.wellArrDisplay, muwi: self.wellMuwiArr)
                                }
                                 else{
                                    _ = wellListService.create(facility: addedLocation.facility, wellPad: addedLocation.wellPad, wells: self.wellArrDisplay, muwi: self.wellMuwiArr)
                                }
                                wellListService.saveChanges()
                                DispatchQueue.main.async {
                                    self.loaderStop()
                                    self.wellTableView.reloadData()
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
        addedLocation = Location()
    }
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return wellArrDisplay.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LocationCell")! as! LocationCell
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        cell.setData(labelValue: wellArrDisplay[indexPath.row])
        cell.checkButton.tag = indexPath.row
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
                    wellStr = self.wellArr[sender.tag]
                }
                wellDisplay = self.wellArrDisplay[sender.tag]
                muwiStr = self.wellMuwiArr[sender.tag]
                checkBtn.isEnabled = true
                checkBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
                sender.setImage(UIImage (named: "checked"), for: .normal)
                selectStatus = true
            }
            
        }
        else
        {
            if ConnectionCheck.isConnectedToNetwork(){
                wellStr = self.wellArr[sender.tag]
            }
            wellDisplay = self.wellArrDisplay[sender.tag]
            muwiStr = self.wellMuwiArr[sender.tag]
            checkBtn.isEnabled = true
            checkBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
            sender.setImage(UIImage (named: "checked"), for: .normal)
            selectStatus = true
            
        }
        selectedIndex = sender.tag
        wellTableView.reloadData()
    }
    
    @IBAction func onCheckPress(_ sender: Any)
    {
        if selectStatus == true{
            
            if !isFromCreateJSA {
                currentLocation.hierarchyLevel = "well"
                currentLocation.well = wellDisplay
                if ConnectionCheck.isConnectedToNetwork(){
                    currentLocation.wellCode = wellStr
                }
                currentLocation.muwi = muwiStr
                currentLocation.wellcheck = true
                currentLocation.facilityOrSite = wellDisplay
            }
            else{
                addedLocation.hierarchyLevel = "well"
                addedLocation.well = wellDisplay
                if ConnectionCheck.isConnectedToNetwork(){
                    addedLocation.wellCode = wellStr
                }
                addedLocation.muwi = muwiStr
                addedLocation.wellcheck = true
                addedLocation.facilityOrSite = wellDisplay
            }
            if !ConnectionCheck.isConnectedToNetwork(){
                let peopleListService = PeopleListService(context: self.context)
                let searchPredicate = NSPredicate(format: "facilityOrSite == %@", currentLocation.muwi)
                let peopleList = peopleListService.get(withPredicate: searchPredicate)
                if peopleList.count == 0{
                    let alertController = UIAlertController.init(title: "", message:"Data not available for this location" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
                else{
                    if !isFromCreateJSA {
                        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                    }
                    else{
                        self.navigationController?.popToViewController(createJSAController, animated: true)
                    }
                }
            }
            else{
                if !isFromCreateJSA {
                    self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                }
                else{
                    self.navigationController?.popToViewController(createJSAController, animated: true)
                }
            }
            
        }
        else
        {
            let alertController = UIAlertController.init(title: "", message:"No well is selected." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
    }
}
