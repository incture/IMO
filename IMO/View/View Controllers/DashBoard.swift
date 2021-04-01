//
//  DashBoard.swift
//  DFT
//
//  Created by Soumya Singh on 24/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import SAPFiori
import SAPFioriFlows
import SAPFoundation
import SAPCommon
//import Firebase
//import FirebaseDatabase

class DashBoard: UIViewController {
    
    @IBOutlet var board: UITableView!
    var csrfToken : String?
    var displayName : String?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var rulesCall : Bool?
    var profileCall : Bool?
    var allUserCall : Bool?
    var count : Int?
    var isSignatureStored : Bool?
    var isServiceProvider : Bool?
    var OperationRules = [Rules]()
    var MaintenanceRules = [Rules]()
    var CapitalProjectsRules = [Rules]()
    var DrillingRules = [Rules]()
    var WellworkRules = [Rules]()
    var CompletionsRules = [Rules]()
    //Added by Mohan
    //For access restriction
    var userhasAccess : Bool = false
    //For Recursive GetallUsers call
    var oUserListStartIndex = 1
    var oCountFlag = 0
    var oCallCountFlag = 0
    var murphyEngineer = [MurphyEngineer]()
    let context = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
   // var ref:DatabaseReference?

    
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        let nib = UINib(nibName: "DashBoardCell", bundle: nil)
        board.register(nib, forCellReuseIdentifier: "DashBoardCell")
        if ConnectionCheck.isConnectedToNetwork(){
            gettingUser()
            isServiceProvider = true
           
        //    _ = Timer.scheduledTimer(timeInterval: 100, target: self, selector: #selector(self.update), userInfo: nil, repeats: true)
            self.loaderStart()
            
        }
        else{
            isServiceProvider = UserDefaults.standard.bool(forKey: "isServiceProvider")
        }
        rulesCall = false
        profileCall = false
        allUserCall = false
        count = 0
        board.tableFooterView = UIView()
        board.separatorStyle = .none
        board.reloadData()
        
    }
    
    // api
    @objc func update(){
        
        
        let urlString = "\(BaseUrl.apiURL)/com.getloggeduser.dest/services/userapi/attributes"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            guard self != nil else { return }
            guard error == nil else {
                return
            }
            do{
                guard let data = data else { return }
                let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                print(JSON as Any)
            }catch{
                print(error.localizedDescription)
            }
        }
        task.resume()
       
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "My Apps"
        let filterItem = UIBarButtonItem.init(image: UIImage(named : "MenuIcon")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.openProfile))
        navigationItem.leftBarButtonItem = filterItem
        
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
    
    func checkForLoading()
    {
        if rulesCall! && profileCall! && allUserCall! {
            self.loaderStop()
            if isServiceProvider! == false{
                getSignature()
            }
        }
    }
    
    func doubleDismiss()
    {
        UserDefaults.standard.removeObject(forKey: "id")
        UserDefaults.standard.removeObject(forKey: "name")
        UserDefaults.standard.removeObject(forKey: "vid")
        UserDefaults.standard.removeObject(forKey: "vadminid")
        UserDefaults.standard.removeObject(forKey: "vaddress")
        UserDefaults.standard.removeObject(forKey: "vname")
        UserDefaults.standard.removeObject(forKey: "vemail")
        UserDefaults.standard.removeObject(forKey: "uncount")
        UserDefaults.standard.removeObject(forKey: "unsynced")
        UserDefaults.standard.removeObject(forKey: "murphyengineer")
        UserDefaults.standard.removeObject(forKey: "isServiceProvider")
        UserDefaults.standard.removeObject(forKey: Department.Operations.rawValue)
        UserDefaults.standard.removeObject(forKey: Department.Maintenance.rawValue)
        UserDefaults.standard.removeObject(forKey: Department.Capital_projects.rawValue)
        UserDefaults.standard.removeObject(forKey: Department.Well_Work.rawValue)
        UserDefaults.standard.removeObject(forKey: Department.Completions.rawValue)
        UserDefaults.standard.removeObject(forKey: Department.Drilling.rawValue)
        UserDefaults.standard.removeObject(forKey: "unsynced")
        UserDefaults.standard.removeObject(forKey: "uncount")
        //Mohan
        UserDefaults.standard.removeObject(forKey: "serviceTypesList")
        UserDefaults.standard.synchronize()
        self.dismissScreen()
        let taskListService = TaskService(context: self.context)
        taskListService.deleteAll()
        taskListService.saveChanges()
        let actionService = ActService(context:self.context)
        actionService.deleteAll()
        taskListService.saveChanges()
        let locationService = LocationService(context:self.context)
        locationService.deleteAll()
        locationService.saveChanges()
        let reviewerService = ReviewerService(context:self.context)
        reviewerService.deleteAll()
        reviewerService.saveChanges()
        
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
    func backFromSignaturescreen()
    {
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MurphyTaskController") as! MurphyTaskController
        let navController = UINavigationController(rootViewController: splitViewController)
        self.present(navController, animated: true, completion: nil)
    }
    
    
    //200
    //success
    func getSignature() {
        
        let id = UserDefaults.standard.string(forKey: "id")
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/Dft_SignatureSet(ApproverId='\(id!)')?$format=json"
        //let encodedUrl = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        //???
        
        var urlRequest = URLRequest(url: URL(string: urlString)!)
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
                                let base64String = d.value(forKey: "DigitalSignature") as? String
                                
                                if base64String == "EMPTY"{
                                    
                                    self!.isSignatureStored = false
                                    UserDefaults.standard.set(base64String, forKey: "digisign")
                                    UserDefaults.standard.synchronize()
                                }
                                else{
                                    self!.isSignatureStored = true
                                    UserDefaults.standard.set(base64String, forKey: "digisign")
                                    UserDefaults.standard.synchronize()
                                    let data: Data = Data(base64Encoded: base64String! , options: .ignoreUnknownCharacters)!
                                    // turn  Decoded String into Data
                                    let dataImage = UIImage(data:data,scale:1.0)
                                    
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
                    self!.loaderStop()
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
    
    
    @objc func dismissScreen()
    {
        self.dismiss(animated: false, completion: nil)
    }
    
    @objc func openProfile(){
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
        splitViewController.isServiceProvider = isServiceProvider!
        splitViewController.senderController = self
        self.present(splitViewController, animated: true, completion: nil)
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

extension DashBoard{
    
    
    //200
    //success
//    func gettingAllUsers(){
//
//        let urlString = "\(BaseUrl.apiURL)/com.cloudidp.dest/service/scim/Users?startIndex=" + String(self.oUserListStartIndex)
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.httpMethod = "get"
//
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//
//            guard self != nil else { return }
//            if error == nil  {
//                DispatchQueue.main.async {
//                    do{
//                        guard let data = data else { return }
//                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
//                        //print(JSON)
//                        if let jsonDict = JSON as? NSDictionary{
//                            let oCallCount :Int = jsonDict.value(forKey: "totalResults") as? Int ?? 0 / 100
//                            self!.oCallCountFlag = self!.oCallCountFlag + 1
//                            let usersArray = jsonDict.value(forKey: "Resources") as? [NSDictionary]
//                            for user in usersArray!{
//
//                                let murphyUser = MurphyEngineer()
//                                let emailGroup = user.value(forKey: "emails") as? [NSDictionary]
//                                murphyUser.emailID = (emailGroup![0].value(forKey: "value") as? String) ?? ""
//                                murphyUser.id = (user.value(forKey: "id") as? String) ?? ""
//                                if let name = user.value(forKey: "name") as? NSDictionary{
//
//                                    let firstName = name.value(forKey: "givenName") as? String
//                                    let lastName = name.value(forKey: "familyName") as? String
//                                    murphyUser.displayName = (firstName ?? "") + " " + (lastName ?? "")
//
//                                }
//
//                                if let groups = user.value(forKey: "groups") as? [NSDictionary]{
//                                    for element in groups{
//                                        let roleValue = element.value(forKey: "value") as? String
//                                        if roleValue == "DFTMurphyReviewer"{
//                                            self!.murphyEngineer.append(murphyUser)
//                                        }
//                                    }
//
//                                }
//                            }
//                            if self!.oCallCountFlag == oCallCount + 1 {
//                                let encodedData = NSKeyedArchiver.archivedData(withRootObject: self!.murphyEngineer)
//                                UserDefaults.standard.set(encodedData, forKey: "murphyengineer")
//                                UserDefaults.standard.synchronize()
//
//                            }else if self!.oCountFlag == 0{
//                                self!.oCountFlag = self!.oCountFlag + 1
//                                for _ in 0...oCallCount-1 {
//                                    self!.oUserListStartIndex = self!.oUserListStartIndex + 100;
//                                  //  DispatchQueue.global(qos: .background).async {
//                                        self?.gettingAllUsers()
//                                   // }
//                                }
//
//                            }
//                        }
//                    }
//                    catch{
//                        print("x\(error.localizedDescription)")
//                    }
//
//                }
//
//            }
//            else{
//                DispatchQueue.main.async {
//                    self!.loaderStop()
//                    let message = error!.localizedDescription
//                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
//                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
//                    alertController.addAction(okAction)
//                    self!.present(alertController, animated: true, completion: nil)
//
//                }
//            }
//        }
//        task.resume()
//
//
//
//
//    }
    
    //success
    //200
    func gettingUser(){self.loaderStart()
        
        
        let urlString = "\(BaseUrl.apiURL)/com.getloggeduser.dest/services/userapi/attributes"
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
                        if let jsonDict = JSON as? NSDictionary{
                            let id = jsonDict.value(forKey: "userId") as? String
                            let email = jsonDict.value(forKey: "email") as? String
                            let firstname = jsonDict.value(forKey: "firstName") as? String
                            let lastname = jsonDict.value(forKey: "lastName") as? String
                            UserDefaults.standard.set(id, forKey: "id")
                            UserDefaults.standard.set(email, forKey: "email")
                            UserDefaults.standard.synchronize()
                            self?.gettingUserDetail(id: id!)
                        }
                    }
                    catch{
                        print("x\(error.localizedDescription)")
                    }
                    
                }
                
            }
            else{
                DispatchQueue.main.async {
                    let message = error?.localizedDescription
                    self!.loaderStop()
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self?.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()
       
    }
    
    
    //200
    //success
    func gettingUserDetail(id : String){
        //Get user Detail
        
        
        let urlString = "\(BaseUrl.apiURL)/com.cloudidp.dest/service/scim/Users/" + id
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"

        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    //print("api 3--\(JSON)")
                    
                    if let jsonDict = JSON as? NSDictionary{
                        
                        if let name = jsonDict.value(forKey: "name") as? NSDictionary{
                            let firstName = name.value(forKey: "givenName") as? String
                            let lastName = name.value(forKey: "familyName") as? String
                            self.displayName = firstName! + " " + lastName!
                            UserDefaults.standard.set(self.displayName, forKey: "name")
                            if let groupValues = jsonDict.value(forKey: "groups") as? [NSDictionary]{
                                
                                for each in groupValues{
                                    if each.value(forKey: "value") as? String == "DFTMurphyReviewer"{
                                        self.isServiceProvider = false
                                        UserDefaults.standard.set(false, forKey: "isServiceProvider")
                                        UserDefaults.standard.synchronize()
                                    }
                                    if each.value(forKey: "value") as? String == "DFTMurphyReviewer" || each.value(forKey: "value") as? String == "DFTServiceProvider"{
                                        self.userhasAccess = true
                                        UserDefaults.standard.set(true, forKey: "userhasAccess")
                                        UserDefaults.standard.synchronize()
                                    }
                                    
                                }
                                
                                
                            }
                            if let addresses = jsonDict.value(forKey : "addresses") as? [[String: String]]{
                                let country = addresses[0]["country"]
                                UserDefaults.standard.set(country , forKey : "UserCountry")
                            }
                            
                            if self.userhasAccess == false {
                                self.loaderStop()
                                let alertController = UIAlertController.init(title: "Alert", message:"User does not have sufficient credentials to perform this action!" , preferredStyle: UIAlertController.Style.alert)
                                let okAction = UIAlertAction.init(title: "Logout", style: UIAlertAction.Style.cancel, handler: {action -> Void in self.doubleDismiss()})
                                alertController.addAction(okAction)
                                self.present(alertController, animated: true, completion: nil)
                                
                            }
                            if self.isServiceProvider == true{
                                UserDefaults.standard.set(true, forKey: "isServiceProvider")
                                UserDefaults.standard.synchronize()
                            //    self.gettingAllUsers()
                                //self.fetchingCSRF()
                                self.getRulesXSJS()
                                if let customValues = jsonDict.value(forKey: "urn:sap:cloud:scim:schemas:extension:custom:2.0:User") as? NSDictionary{
                                    
                                    
                                    let attribute = customValues.value(forKey: "attributes") as? [NSDictionary]
                                    let vendorID = attribute![0].value(forKey: "value") as? String
                                    let vendorName = attribute![1].value(forKey: "value") as? String
                                    //  let vendorAddress = attribute![2].value(forKey: "value") as? String
                                    var vendorAdminID : String = ""
                                    if attribute!.count > 3{
                                        if let vendAdminID = attribute![3].value(forKey: "value") {
                                            vendorAdminID = (vendAdminID as? String) ?? ""
                                        }
                                        else{
                                            vendorAdminID = ""
                                        }
                                    }
                                    var vendorEmailID : String = ""
                                    if attribute!.count > 4{
                                        if let vendEmailID = attribute![4].value(forKey: "value") {
                                            vendorEmailID = (vendEmailID as? String) ?? ""
                                        }
                                        else{
                                            vendorEmailID = ""
                                        }
                                    }
                                    var serviceTypesList : [String] = []
                                    for attr in attribute!{
                                        if attr.value(forKey: "name")! as! String == "customAttribute8"{
                                            let serviceTypes = attr.value(forKey: "value") as? String ?? ""
                                            
                                            if serviceTypes.contains(String("General DFT")){
                                                serviceTypesList.append("General DFT")
                                            }
                                            if serviceTypes.contains(String("Salt Water Disposal")){
                                                serviceTypesList.append("Salt Water Disposal")
                                            }
                                            if serviceTypes == ""{
                                                serviceTypesList.append("General DFT")
                                            }
                                            
                                        }
                                    }
                                    if serviceTypesList.count == 0{
                                        serviceTypesList.append("General DFT")
                                    }
                                    UserDefaults.standard.set(serviceTypesList, forKey: "serviceTypesList")
                                    UserDefaults.standard.set( vendorID, forKey: "vid")
                                    UserDefaults.standard.set(vendorName, forKey: "vname")
                                    //  UserDefaults.standard.set(vendorAddress, forKey: "vaddress")
                                    UserDefaults.standard.set(vendorAdminID, forKey: "vadminid")
                                    UserDefaults.standard.set(vendorEmailID, forKey: "vemailid")
                                    UserDefaults.standard.synchronize()
                                    // self.profileCall = true
                                    //self.loaderStop()
                                }
                                
                            }else{
                                self.getSignature()
                            }
                            
                        }
                        //self.logFireBaseEvent()
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }
                
            }else{
                
                DispatchQueue.main.async{
                    //self.profileCall = true
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

    func getOSInfo()->String {
        let os = ProcessInfo().operatingSystemVersion
        //return String(os.majorVersion) + "." + String(os.minorVersion) + "." + String(os.patchVersion)
        return String(os.majorVersion) + "." + String(os.minorVersion)
    }
    //200
    //success
    func getRulesXSJS()
    {
      
        
        let urlString = "\(BaseUrl.apiURL)/com.dft.xsjs/RulesProject/getAllDepartment.xsjs?$format=json"
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
                        var departmentValues = [String]()
                                           if let jsonDict = JSON as? NSDictionary {
                                               
                                               if let AllDepartments = jsonDict.value(forKey: "AllDepartment") as? [NSDictionary]{
                                                   
                                                   for i in AllDepartments{
                                                       departmentValues.append(i.value(forKey: "department") as? String ?? "")
                                                   }
                                                   departmentValues = Array(Set(departmentValues))
                                                   UserDefaults.standard.set(departmentValues, forKey: "department")
                                                   UserDefaults.standard.synchronize()
                                                   
                                               }
                                           }
                        self!.loaderStop()
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
    
    
//    func checkVersion(){
//        //Prod
//        // "https://appdownloaddee8964f1.us2.hana.ondemand.com/AppDownload/app/download?fileType=APK"
//        //QA and Dev
//        // "https://appdownloaddee8964f1.us2.hana.ondemand.com/AppDownload/qa/app/download?fileType=IPA"
//        let header = [ "x-csrf-token" : "fetch"]
//        // For QA, should be DFTQA
//        Arlamofire.request("\(BaseUrl.apiURL)/com.appUpdate.dest/appFile/download?fileType=IPA&application=DFTQA", method: .get, parameters: nil, encoding: URLEncoding.default, headers: header)
//            .responseString { response in
//
//
//                if ConnectionCheck.isConnectedToNetwork(){
//                    let value = response.result.value
//                    self.convertToJSON(value: value!)
//                }
//                else{
//                    let message = "Internet connection not available."
//                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertControllerStyle.alert)
//                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertActionStyle.default, handler: nil)
//                    alertController.addAction(okAction)
//                    self.present(alertController, animated: true, completion: nil)
//                }
//
//        }
//    }
    
    func convertToJSON(value : String){
        let data = value.data(using: .utf8)
        
        do{
            if let json = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary {
                let url = json.value(forKey: "url") as? String
                let version = json.value(forKey: "version") as? String
                
                let nsObject: AnyObject? = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as AnyObject
                let currentVersion = nsObject as? String
                let intDBVersion = Float(version ?? "0")
                let intCurrentVersion = Float(currentVersion ?? "0")
                if intDBVersion! > intCurrentVersion!{
                    let alertController = UIAlertController.init(title: "App Update", message:"version \(String(describing: version!)) available" , preferredStyle: UIAlertController.Style.alert)
                    let updateAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: { action in
                        
                        UIApplication.shared.open(URL(string: url ?? "")!, options: [:], completionHandler: nil)
                    })
                    alertController.addAction(updateAction)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
        }
        catch {
            print(error.localizedDescription)
        }
    }
    
    
}

extension DashBoard : UITableViewDelegate, UITableViewDataSource{
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1// harcoded here
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "DashBoardCell")! as! DashBoardCell
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
        self.userhasAccess = UserDefaults.standard.bool(forKey: "userhasAccess")
        if self.userhasAccess  {
            if isServiceProvider! == true{
                let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "AllTicketsController") as! AllTicketsController
                let navController = UINavigationController(rootViewController: splitViewController)
                navController.modalPresentationStyle = .fullScreen
                self.present(navController, animated: true, completion: nil)
            }
            else{
                
                if UserDefaults.standard.string(forKey: "digisign")! == "EMPTY"{
                    
                    let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DigitalSignatureController") as! DigitalSignatureController
                    splitViewController.dashBoardSender = self
                    splitViewController.isUpdate = false
                    let navController = UINavigationController(rootViewController: splitViewController)
                    navController.modalPresentationStyle = .fullScreen
                    self.present(navController, animated: true, completion: nil)
                    
                }
                else{
                    
                    let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MurphyTaskController") as! MurphyTaskController
                    let navController = UINavigationController(rootViewController: splitViewController)
                    navController.modalPresentationStyle = .fullScreen
                    self.present(navController, animated: true, completion: nil)
                }
            }
            
        }
        else{
            
            let alertController = UIAlertController.init(title: "Alert", message:"Sorry, You don't have access to this App, Please Logout." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "Logout", style: UIAlertAction.Style.cancel, handler: {action -> Void in self.doubleDismiss()})
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
            
            
        }
    }
    
}

