//
//  LauncherController.swift
//  Task-Management
//
//  Created by Soumya Singh on 13/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import SAPFiori
import SAPFioriFlows
import SAPFoundation
import SAPCommon
import Speech
import Combine

typealias JSON = [String: Any]

var currentUser = User()
class LauncherController: UIViewController {
    @IBOutlet weak var marqueLabel: MarqueeLabel!
    @IBOutlet weak var marqueeHeight: NSLayoutConstraint!
    @IBOutlet var board: UITableView!
    
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.whiteLarge)
    var hasTMrole : Bool?
    let progressHUD = CustomActivityIndicator(text: "Preparing...")
    //For Recursive GetallUsers call
    
    var oUserListStartIndex = 1
    var oCountFlag = 0
    var oCallCountFlag = 0
    
    var prevCommand = ""
    var sessionID:String?
    var location:String = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        self.loadOfflineStores()
        marqueeHeight.constant = 0
        print("Ios Version========\n\(self.getOSInfo())\n=========")
        self.navigationController?.navigationBar.isHidden = false
        createNavBar()
        UIApplication.shared.keyWindow!.addSubview(progressHUD)
        progressHUD.hide()
        let nib = UINib(nibName: "DashBoardCell", bundle: nil)
        board.register(nib, forCellReuseIdentifier: "DashBoardCell")
        board.separatorStyle = .none
        // checkVersion()
        DispatchQueue.global(qos: .background).async {
            UserDefaults.standard.set(nil, forKey: "sapIDUserDict")
            self.gettingAllUsers()
        }
        if ConnectionCheck.isConnectedToNetwork(){
            gettingUser()
            currentUser.email = UserDefaults.standard.string(forKey: "email")
            currentUser.name = UserDefaults.standard.string(forKey: "name")
            let fullname = currentUser.name?.components(separatedBy: " ")
            currentUser.firstname = fullname?[0]
            currentUser.lastname = fullname?[1]
        }
        else{
            currentUser.email = UserDefaults.standard.string(forKey: "email")
            currentUser.name = UserDefaults.standard.string(forKey: "name")
            let fullname = currentUser.name?.components(separatedBy: " ")
            currentUser.firstname = fullname?[0]
            currentUser.lastname = fullname?[1]
        }
        _ = Timer.scheduledTimer(timeInterval: 100, target: self, selector: #selector(self.update), userInfo: nil, repeats: true)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        marqueeHeight.constant = 0
    }
    
    
    func getOSInfo()->String {
        let os = ProcessInfo().operatingSystemVersion
        return String(os.majorVersion) + "." + String(os.minorVersion)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.sessionID = nil
    }
    
    @objc func update() {
        
      
    print("********Timer*********")
       // let urlString = "\(BaseUrl.apiURL)/GetLoggedInUser_Dest/services/userapi/attributes"
        let urlString = "\(BaseUrl.apiURL)/mobileservices/application/com.incture.ptw/roleservice/application/com.incture.ptw/v2/Me"
        //let urlString = "\(BaseUrl.apiURL)/UserManagement_Dest/services/userapi/attributes"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        let task = ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            guard self != nil else { return }
            guard error == nil else {
                return
            }
            do{
                guard let data = data else { return }
                let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
            }catch{
                print(error.localizedDescription)
            }
        }
        task.resume()
        
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "My Apps"
        let filterItem = UIBarButtonItem.init(image: UIImage(named : "menu")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.openProfile))
        navigationItem.leftBarButtonItem = filterItem
        
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        //      UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    func presentViewFor(_ role: String, isAlanEnabled: Bool = false) {
        
        if role == "PTW"
        {
            let vc = UIStoryboard(name: "start", bundle: Bundle.main).instantiateViewController(withIdentifier: "CreatedPermitControllerViewController") as! CreatedPermitControllerViewController
            vc.modalPresentationStyle = .fullScreen
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
}

extension LauncherController : UITableViewDelegate, UITableViewDataSource{
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if currentUser.apps != nil
        {
            return (currentUser.apps?.count)!
        }
        else
        {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "DashBoardCell")! as! DashBoardCell
        cell.selectionStyle = .none
        cell.setData(indexPath: indexPath.row)
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0{
            return 220
        }
        else{
            return 220
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let role = currentUser.apps![indexPath.row]
        presentViewFor(role)
    }
    
}

extension LauncherController {
    
    func doubleDismiss()
    {
        DispatchQueue.main.async {
            UIApplication.shared.applicationIconBadgeNumber = 0
        }
        
        let context = PTWCoreData.shared.managedObjectContext
        let facilityService = FacilityListService(context: context)
        let wellPadService = WellPadListService(context: context)
        let wellService = WellListService(context: context)
        let peopleListService = PeopleListService(context: context)
        let jsaListService = JSAModelService(context: context)
        let permitListService = PermitModelService(context: context)
        let jsaDetailService = JSADetailModelService(context: context)
        let permitDetailService = PermitDetailModelService(context: context)
        let actionService = ActionModelService(context: context)
        
        facilityService.deleteAll()
        facilityService.saveChanges()
        
        wellPadService.deleteAll()
        wellPadService.saveChanges()
        
        wellService.deleteAll()
        wellService.saveChanges()
        
        peopleListService.deleteAll()
        peopleListService.saveChanges()
        
        jsaListService.deleteAll()
        jsaListService.saveChanges()
        
        permitListService.deleteAll()
        permitListService.saveChanges()
        
        jsaDetailService.deleteAll()
        jsaDetailService.saveChanges()
        
        permitDetailService.deleteAll()
        permitDetailService.saveChanges()
        
        actionService.deleteAll()
        actionService.saveChanges()
        
        UserDefaults.standard.removeObject(forKey: "id")
        UserDefaults.standard.removeObject(forKey: "name")
        UserDefaults.standard.removeObject(forKey: "email")
        UserDefaults.standard.removeObject(forKey: "userRoles")
        UserDefaults.standard.removeObject(forKey: "vid")
        UserDefaults.standard.removeObject(forKey: "vname")
        UserDefaults.standard.removeObject(forKey: "vaddress")
        UserDefaults.standard.removeObject(forKey: "vadminid")
        UserDefaults.standard.removeObject(forKey: "vemailid")
        UserDefaults.standard.removeObject(forKey: "offlinenumber")
        UserDefaults.standard.removeObject(forKey: "onboardingTrue")
        
        
        UserDefaults.standard.synchronize()
        
        
        guard let onboardingSession = OnboardingSessionManager.shared.onboardingSession else {
            return
        }
        
        onboardingSession.logout(completionHandler: { error in
            DispatchQueue.main.async {
                let controller = OnboardingSessionManager.shared.onboardingController
                let onboardingID = onboardingSession.onboardingID
                controller.resetFlow(for: onboardingID, completionHandler: { (error) in
                    self.removeSession()
                })
            }
        })
    }
    public func removeSession() {
        
        OnboardingSessionManager.shared.removeSession { _ in
            DispatchQueue.main.async {
                let applicationUIManager = OnboardingSessionManager.shared.presentationDelegate as? ApplicationUIManager
                URLCache.shared.removeAllCachedResponses()
                HTTPCookieStorage.shared.removeCookies(since: Date.distantPast)
                applicationUIManager?.releaseRootFromMemory()
                UIApplication.shared.keyWindow?.rootViewController = FUIInfoViewController.createSplashScreenInstanceFromStoryboard()
                AppDelegate.shared.onboardUser()
            }
        }
    }
    @objc func openProfile(){
        let splitViewController = UIStoryboard(name: "launcher", bundle: Bundle.main).instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
        splitViewController.senderController = self
        self.present(splitViewController, animated: true, completion: nil)
    }
    
    func convertToJSON(json : NSDictionary){
        
        do{
            if let url = json.value(forKey: "url") as? String,  let version = json.value(forKey: "version") as? String {
                
                let nsObject: AnyObject? = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as AnyObject
                let currentVersion = nsObject as? String
                let intDBVersion = Float(version)
                let intCurrentVersion = Float(currentVersion!)
                if intDBVersion! > intCurrentVersion!{
                    DispatchQueue.main.async {
                        self.progressHUD.hide()
                        let alertController = UIAlertController.init(title: "App Update", message:"version \(String(describing: version)) available" , preferredStyle: UIAlertController.Style.alert)
                        let updateAction = UIAlertAction.init(title: "UPDATE", style: UIAlertAction.Style.default, handler: { action in
                            
                            UIApplication.shared.open(URL(string: url)!, options: [:], completionHandler: nil)
                        })
                        alertController.addAction(updateAction)
                        if var topController = UIApplication.shared.keyWindow?.rootViewController {
                            while let presentedViewController = topController.presentedViewController {
                                topController = presentedViewController
                            }
                            topController.present(alertController, animated: true, completion: nil)
                            // topController should now be your topmost view controller
                        }
                    }
                }
                else{
                    
                }
            }
        }
        catch {
            print(error.localizedDescription)
        }
    }
    func gettingAllUsers(){
        //Get user Detail
        //self.loaderStart()
        
        let urlString : String = "\(BaseUrl.apiURL)/UserManagement_Dest/service/scim/Users?startIndex=" + String(self.oUserListStartIndex)
        let url = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: url!)!)
        urlRequest.httpMethod = "get"
        
        let task = ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    if let  _ = UserDefaults.standard.value(forKey: "sapIDUserDict") as? [String : String] {}else {
                        UserDefaults.standard.set([String : String](), forKey: "sapIDUserDict")
                    }
                    var sapIDUserDict = UserDefaults.standard.value(forKey: "sapIDUserDict") as! [String : String]
                    if let jsonDict = JSON as? NSDictionary{
                        let oCallCount :Int = jsonDict.value(forKey: "totalResults") as! Int / 100
                        self.oCallCountFlag = self.oCallCountFlag + 1
                        let usersArray = jsonDict.value(forKey: "Resources") as? [NSDictionary]
                        for user in usersArray!{
                            let sapID = user["userName"] as? String ?? ""
                            guard let name = user["name"] as? JSON else {return}
                            let givenName = name["givenName"] as? String ?? ""
                            let familyName = name["familyName"] as? String ?? ""
                            let idpName = givenName + " " + familyName
                            sapIDUserDict[sapID] = idpName
                        }
                        UserDefaults.standard.set(sapIDUserDict, forKey: "sapIDUserDict")
                        if self.oCallCountFlag == oCallCount + 1 {
                            
                        }else if self.oCountFlag == 0{
                            self.oCountFlag = self.oCountFlag + 1
                            for _ in 0...oCallCount-1 {
                                self.oUserListStartIndex = self.oUserListStartIndex + 100;
                                self.gettingAllUsers();
                            }
                            
                        }
                    }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                
                DispatchQueue.main.async{
                    self.loaderStop()
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
            
            
        }
        task.resume()
        
        
    }
    
    func gettingUser(){
        if UserDefaults.standard.value(forKey: "notifcation") != nil{
        }else{
            DispatchQueue.main.async {
                self.progressHUD.show()
            }
        }
        
        let urlString = "\(BaseUrl.apiURL)/mobileservices/application/com.incture.ptw/roleservice/application/com.incture.ptw/v2/Me"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        let task = ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        if let jsonDict = JSON as? NSDictionary{
                        
                            let emails = jsonDict.value(forKey: "emails") as? [NSDictionary]
                            let email = emails?.first?.value(forKey: "value") as? String
                            let id = jsonDict.value(forKey: "id") as? String
                            let firstName = (jsonDict.value(forKey: "others") as? NSDictionary)?.value(forKey: "firstName") as? String
                            let lastName = (jsonDict.value(forKey: "others") as? NSDictionary)?.value(forKey: "lastName") as? String
                            let loginName = (jsonDict.value(forKey: "others") as? NSDictionary)?.value(forKey: "login_name") as? String
                            
                            
                            UserDefaults.standard.set(id, forKey: "id")
                            UserDefaults.standard.set(email, forKey: "email")
                            UserDefaults.standard.set(loginName, forKey : "LoginName")
                        UserDefaults.standard.synchronize()
                        currentUser.email = email
                        currentUser.loginName = loginName
                        
                        currentUser.fullName = (firstName ?? "") + " " + (lastName ?? "")
                        self.gettingUserDetail(id: id!)
                        
                    }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                DispatchQueue.main.async{
                    self.loaderStop()
                    let message = error?.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
            
            
        }
        task.resume()
    }
    func gettingUserDetail(id : String){
        //Get user Detail
        
        currentUser.roles?.removeAll()
        currentUser.apps?.removeAll()
        let urlString = "\(BaseUrl.apiURL)/UserManagement_Dest/service/scim/Users/" + id
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        let task = ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    
                    var arr : [String] = []
                    var roles : [String] = []
                    DispatchQueue.main.async {
                        self.progressHUD.hide()
                    }
                    if let jsonDict = JSON as? NSDictionary{
                        
                        if let name = jsonDict.value(forKey: "name") as? NSDictionary{
                            
                            let firstName = name.value(forKey: "givenName") as? String
                            let lastName = name.value(forKey: "familyName") as? String
                            let displayName = firstName! + " " + lastName!
                            UserDefaults.standard.set(displayName, forKey: "name")
                            currentUser.name = displayName
                            currentUser.firstname = firstName
                            currentUser.lastname = lastName
                            DispatchQueue.main.async {
                                
                                if let addresses = jsonDict.value(forKey : "addresses") as? [[String: String]]{
                                    let country = addresses[0]["country"]
                                    //                                if country == "CA"{
                                    currentUser.country = country ?? "US"
                                    //                                }
                                    //TODO:- else US users
                                    
                                    UserDefaults.standard.set(country , forKey : "UserCountry")
                                }
                                
                                if let groupValues = jsonDict.value(forKey: "groups") as? [NSDictionary]{
                                    let rolesValues:[String] = groupValues.compactMap{$0["display"] as? String}
                                    for value in rolesValues{
                                        if value.lowercased().contains("_obx") || value.lowercased().contains("_pro") || value.lowercased().contains("_als"){
                                            
                                            self.location = value.components(separatedBy: "_").last ?? ""
                                        }
                                    }
                                    
                                    var userRoles = [String]()
                                    print(groupValues)
                                    for each in groupValues{
                                        userRoles.append(each.value(forKey: "value") as? String ?? "")
                                    }
                            
                                    if userRoles.contains("IOP_PTW_Field_CF"){
                                        roles.append("IOP_PTW_Field")
                                        arr.append("PTW")
                                    }
                                    //commented for now
//                                    if userRoles.contains("IOP_MobileReadOnly"){
//                                        arr.removeAll()
//                                        arr.append("PTW")
//                                        currentUser.isReadOnly = true
//                                    }
                                
                                    print(userRoles)
                                }
                                currentUser.apps = arr
                                currentUser.roles = roles
                                UserDefaults.standard.synchronize()
                                
                                if (jsonDict.value(forKey: "urn:sap:cloud:scim:schemas:extension:custom:2.0:User") as? NSDictionary) != nil{
                                }
                                DispatchQueue.main.async {
                                    if currentUser.country == "CA"{
                                        currentUser.isCanadianUser = true
                                    }else{
                                        
                                        currentUser.isCanadianUser = false
                                    }
                                    self.board.reloadData()
                                }

                            }
                        }
                    }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                DispatchQueue.main.async{
                    self.loaderStop()
                    let message = error!.localizedDescription
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

extension LauncherController {
    func loadOfflineStores() {
    }
}
