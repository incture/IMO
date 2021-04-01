//
// AppDelegate.swift
// MurphyDFT-Final
//
// Created by SAP Cloud Platform SDK for iOS Assistant application on 01/02/18
//

import SAPFiori
import SAPFioriFlows
import SAPFoundation
import SAPCommon
import CoreData
import UserNotifications
import SAPOData
import NotificationBannerSwift

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UISplitViewControllerDelegate, UNUserNotificationCenterDelegate, XMLParserDelegate, SAPURLSessionDelegate {
    

    var window: UIWindow?
    let reachability = Reachability()!
    var csrfToken : String?
    var postData = [[String: Any]]()
    var parser = XMLParser()
    var element = NSString()
    var ticketName = [String]()
    var workFlowCount : Int = 0
    var department = [String]()
    var location = [String]()
    var reviewerId = [String]()
    var reviewerMail = [String]()
    var responseType = [String]()
    var responseMessage = [String]()
    var workflowResponse = [String]()
    var restCSRFToken = [String]()
   // var workflowTriggered = [Bool]()
    var tempIdArray = [String]()
    var count : Int?
    var id : Int?
    var isSynchronizingInBackend : Bool?

   // var workflowArray = [WorkFlowData]()
    var SyncAttemptsMade : Int?
    typealias FinishedWorkflow = () -> ()
    typealias FinishedEccCSRF = () -> ()
    typealias FinisgedECCCreation = () -> ()
    typealias FinishedWorkfowTokenFetch = () -> ()
    var onboardingErrorHandler: OnboardingErrorHandler?
    var sessionManager: OnboardingSessionManager<ApplicationOnboardingSession>!
    private var flowProvider = OnboardingFlowProvider()
    var sessionManagerDFT: OnboardingContext!

    private let logger = Logger.shared(named: "AppDelegateLogger")
    var zmurpurchasegroupsrvEntities: ZMURPURCHASEGROUPSRVEntities<OnlineODataProvider>!
    
    func onboarded(onboardingContext: OnboardingContext, onboarding: Bool) {
        self.setRootViewController()
                let authenticationURL = onboardingContext.info[.authenticationURL] as! URL
                self.configureOData(onboardingContext.sapURLSession, authenticationURL)

      //  self.configureOData(onboardingContext.sapURLSession, onboardingContext.authenticationURL!)
        let sapcpmsSettingsParameters = onboardingContext.info[.sapcpmsSettingsParameters] as! SAPcpmsSettingsParameters
        self.uploadLogs(onboardingContext.sapURLSession, sapcpmsSettingsParameters)
        self.registerForRemoteNotification(onboardingContext.sapURLSession, sapcpmsSettingsParameters)
    }
    
    
    internal func application(_: UIApplication, didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        // Set a FUIInfoViewController as the rootViewController, since there it is none set in the Main.storyboard
        //FirebaseApp.configure()
       // NUISettings.initWithStylesheet(name: "murphy")
        self.window = UIWindow(frame: UIScreen.main.bounds)
        window = UIWindow()
        window?.makeKeyAndVisible()
        self.window!.rootViewController = FUIInfoViewController.createSplashScreenInstanceFromStoryboard()
        
        do {
            // Attaches a LogUploadFileHandler instance to the root of the logging system
            try SAPcpmsLogUploader.attachToRootLogger()
        } catch {
            self.logger.error("Failed to attach to root logger.", error: error)
        }
        
//        NotificationCenter.default.addObserver(self, selector: #selector(self.switchNetwork),name: .reachabilityChanged,object: reachability)
//        do{
//            try reachability.startNotifier()
//        }catch{
//            print("could not start reachability notifier")
//        }
        //        let settings = UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
        //        UIApplication.shared.registerUserNotificationSettings(notificationSettings)
        
//        Fabric.with([Crashlytics.self])
        self.initializeOnboarding()
        Logger.root.logLevel = .debug
        DispatchQueue.main.async {
           // UINavigationBar().applyFioriStyle()
            let navigationBarAppearace = UINavigationBar.appearance()
            
            navigationBarAppearace.tintColor = UIColor.white
            
            navigationBarAppearace.barTintColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
            
            navigationBarAppearace.isTranslucent = false
            
            navigationBarAppearace.titleTextAttributes = [NSAttributedString.Key.foregroundColor:UIColor.white]
       // NUISettings.initWithStylesheet(name: "murphy")
        }
        if #available(iOS 13.0, *) {
            DispatchQueue.main.async {
            UIApplication.shared.keyWindow?.addSubview(statusBarUIView!)
            }
        }else{
            DispatchQueue.main.async {
                let statusBar: UIView = UIApplication.shared.value(forKey: "statusBar") as! UIView
                if statusBar.responds(to:#selector(setter: UIView.backgroundColor)) {
                    statusBar.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
                }
            }
        }
      //  UINavigationBar.applyFioriStyle()
//        OnboardingManager.shared.isSynchronizingInBackend = false
//        OnboardingManager.shared.delegate = self
//        OnboardingManager.shared.onboardOrRestore()
        
        return true
    }
    
    // To only support portrait orientation during onboarding
    func application(_: UIApplication, supportedInterfaceOrientationsFor _: UIWindow?) -> UIInterfaceOrientationMask {
        switch OnboardingFlowController.presentationState {
        case .onboarding, .restoring:
            return .portrait
        default:
            return .portrait
        }
    }
    
    // Delegate to OnboardingManager.
    func applicationDidEnterBackground(_: UIApplication) {
        self.saveContext()
        OnboardingSessionManager.shared.lock { _ in }

        }

    // Delegate to OnboardingManager.
    func applicationWillEnterForeground(_: UIApplication) {
        OnboardingSessionManager.shared.unlock { error in
            guard let error = error else {
                return
            }

            self.onboardingErrorHandler?.handleUnlockingError(error)
        }
    }

    func applicationWillTerminate(_: UIApplication) {
        self.saveContext()
    }
    
    //    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
    //
    //        let status = reachability.connection
    //        switch  (status) {
    //
    //        case .none:
    //            print("No connectivity")
    //
    //        case .cellular, .wifi :
    //            print("Let's sync")
    //            OfflineTicketCreation()
    //        }
    //    }
    
//    func performBasicAuthentication(username: String, password: String, completion: @escaping (Bool, String?) -> Void) {
//        
//        
    //let sapUrlSession = SAPURLSession(delegate: self)
//        
//        sapUrlSession.register(SAPcpmsObserver(settingsParameters:SAPcpmsSettingsParameters(backendURL: URL(string:BaseUrl.apiURL)!, applicationID: "com.dft.native")))
//        
//        var request = URLRequest(url: URL(string:BaseUrl.apiURL+"com.dft.native")!)
//        request.httpMethod = "GET"
//        
//
//        let dataTask = sapUrlSession.dataTask(with: request) { data, response, error in
//            guard let response = response as? HTTPURLResponse, response.statusCode == 200 else {
//                let message: String
//                if let error = error {
//                    
//                    print("Error while basic authentication: \(error)")
//                    message = error.localizedDescription
//                    
//                } else {
//                    message = "Check your credentials!"
//                }
//                
//                DispatchQueue.main.async {
//                    
//                    completion(false, message)
//                }
//                return
//
//            }
//
//            print("Response returned: \(HTTPURLResponse.localizedString(forStatusCode: response.statusCode))")
//            
//            
//            
//            // We should check if we got SAML challenge from the server or not
//            if self.isSAMLChallenge(response) {
//                
//                print("Logon process failure. It seems you got SAML authentication challenge.")
//                
//                
//                
//                let message = "Logon process failure. It seems you got SAML authentication challenge."
//                
//                
//                
//                DispatchQueue.main.async {
//                    
//                    completion(false, message)
//                    
//                }
//                
//            }
//                
//            else {
//                
//                print("Logged in successfully.")
//                
//                //self.urlSession = sapUrlSession
//                
//                
//                
//                DispatchQueue.main.async {
//                    
//                    DispatchQueue.main.async {
//                        
//                        completion(true, nil)
//                        
//                    }
//                    
//                }
//                
//            }
//            
//        }
//        
//        
//        
//        dataTask.resume()
//        
//    }
    

    
    private func setRootViewController() {
        DispatchQueue.main.async {
            let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DashBoard") as! DashBoard
            //            splitViewController.delegate = self
            //            splitViewController.modalPresentationStyle = .currentContext
            //            splitViewController.preferredDisplayMode = .allVisible
            let navController = UINavigationController(rootViewController: splitViewController)
            self.window!.rootViewController = navController
        }
    }
    
    // MARK: - Split view
    func splitViewController(_: UISplitViewController, collapseSecondary _: UIViewController, onto _: UIViewController) -> Bool {
        // The first Collection will be selected automatically, so we never discard showing the secondary ViewController
        return false
    }
    
    // MARK: - Remote Notification handling
    private var deviceToken: Data?
    
    func application(_: UIApplication, willFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        UIApplication.shared.registerForRemoteNotifications()
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .badge, .sound]) { _, _ in
            // Enable or disable features based on authorization.
        }
        center.delegate = self
        return true
    }
    
    // Called to let your app know which action was selected by the user for a given notification.
    func userNotificationCenter(_: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        self.logger.info("App opened via user selecting notification: \(response.notification.request.content.body)")
        // Here is where you want to take action to handle the notification, maybe navigate the user to a given screen.
        completionHandler()
    }
    
    // Called when a notification is delivered to a foreground app.
    func userNotificationCenter(_: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        self.logger.info("Remote Notification arrived while app was in foreground: \(notification.request.content.body)")
        // Currently we are presenting the notification alert as the application were in the background.
        // If you have handled the notification and do not want to display an alert, call the completionHandler with empty options: completionHandler([])
        completionHandler([.alert, .sound])
    }
    
    func registerForRemoteNotification(_ urlSession: SAPURLSession, _ settingsParameters: SAPcpmsSettingsParameters) {
        guard let deviceToken = self.deviceToken else {
            // Device token has not been acquired
            return
        }
        
        
        let remoteNotificationClient = SAPcpmsRemoteNotificationClient(sapURLSession: urlSession, settingsParameters: settingsParameters)
        remoteNotificationClient.registerDeviceToken(deviceToken) { error in
            if let error = error {
                self.logger.error("Register DeviceToken failed", error: error)
                return
            }
            self.logger.info("Register DeviceToken succeeded")
        }
    }
    
    func application(_: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        self.deviceToken = deviceToken
        var token = ""
        for i in 0..<deviceToken.count {
            token = token + String(format: "%02.2hhx", arguments: [deviceToken[i]])
        }
        print(token)
    }
    
    func application(_: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        self.logger.error("Failed to register for Remote Notification", error: error)
    }
    
    // MARK: - Log uploading
    
    // This function is invoked on every application start, but you can reuse it to manually triger the logupload.
    private func uploadLogs(_ urlSession: SAPURLSession, _ settingsParameters: SAPcpmsSettingsParameters) {
        SAPcpmsLogUploader.uploadLogs(sapURLSession: urlSession, settingsParameters: settingsParameters) { error in
            if let error = error {
                self.logger.error("Error happened during log upload.", error: error)
                return
            }
            self.logger.info("Logs have been uploaded successfully.")
        }
    }
    
    // MARK: - Core Data stack
    
//    lazy var applicationDocumentsDirectory: NSURL = {
//        // The directory the application uses to store the Core Data store file. This code uses a directory named "in.cherrywork.Workbox" in the application's documents Application Support directory.
//        let urls = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
//        return urls[urls.count-1] as NSURL
//    }()
//
//    lazy var managedObjectModel: NSManagedObjectModel = {
//        // The managed object model for the application. This property is not optional. It is a fatal error for the application not to be able to find and load its model.
//        let modelURL = Bundle.main.url(forResource: "Cherrywork", withExtension: "momd")!
//        return NSManagedObjectModel(contentsOf: modelURL)!
//    }()
//
//    lazy var persistentStoreCoordinator: NSPersistentStoreCoordinator = {
//        // The persistent store coordinator for the application. This implementation creates and returns a coordinator, having added the store for the application to it. This property is optional since there are legitimate error conditions that could cause the creation of the store to fail.
//        // Create the coordinator and store
//        let coordinator = NSPersistentStoreCoordinator(managedObjectModel: self.managedObjectModel)
//        let url = self.applicationDocumentsDirectory.appendingPathComponent("SingleViewCoreData.sqlite")
//        let mOptions = [NSMigratePersistentStoresAutomaticallyOption: true,
//                        NSInferMappingModelAutomaticallyOption: true]
//        var failureReason = "There was an error creating or loading the application's saved data."
//        do {
//            try coordinator.addPersistentStore(ofType: NSSQLiteStoreType, configurationName: nil, at: url, options: mOptions)
//        } catch {
//            // Report any error we got.
//            var dict = [String: AnyObject]()
//            dict[NSLocalizedDescriptionKey] = "Failed to initialize the application's saved data" as AnyObject?
//            dict[NSLocalizedFailureReasonErrorKey] = failureReason as AnyObject?
//
//            dict[NSUnderlyingErrorKey] = error as NSError
//            let wrappedError = NSError(domain: "YOUR_ERROR_DOMAIN", code: 9999, userInfo: dict)
//            // Replace this with code to handle the error appropriately.
//            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
//            NSLog("Unresolved error \(wrappedError), \(wrappedError.userInfo)")
//            abort()
//        }
//
//        return coordinator
//    }()
//
//    lazy var managedObjectContext: NSManagedObjectContext = {
//        // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.) This property is optional since there are legitimate error conditions that could cause the creation of the context to fail.
//        let coordinator = self.persistentStoreCoordinator
//        var managedObjectContext = NSManagedObjectContext(concurrencyType: .mainQueueConcurrencyType)
//        managedObjectContext.persistentStoreCoordinator = coordinator
//        return managedObjectContext
//    }()
    
    
    // MARK: - Core Data stack
    
    lazy var persistentContainer: NSPersistentContainer = {
        /*
         The persistent container for the application. This implementation
         creates and returns a container, having loaded the store for the
         application to it. This property is optional since there are legitimate
         error conditions that could cause the creation of the store to fail.
         */
        let container = NSPersistentContainer(name: "Cherrywork")
        
        let description = NSPersistentStoreDescription()
        description.shouldInferMappingModelAutomatically = true
        description.shouldMigrateStoreAutomatically = true
        container.persistentStoreDescriptions = [description]
        
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error as NSError? {
                // Replace this implementation with code to handle the error appropriately.
                // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                
                /*
                 Typical reasons for an error here include:
                 * The parent directory does not exist, cannot be created, or disallows writing.
                 * The persistent store is not accessible, due to permissions or data protection when the device is locked.
                 * The device is out of space.
                 * The store could not be migrated to the current model version.
                 Check the error message to determine what the actual problem was.
                 */
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        })
        return container
    }()
    
    var managedObjectContext: NSManagedObjectContext {
        get {
            return self.persistentContainer.viewContext
        }
    }
    
    // MARK: - Core Data Saving support
    
    func saveContext () {
        let context = persistentContainer.viewContext
        if context.hasChanges {
            do {
                try context.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                NSLog("Unresolved error \(nserror), \(nserror.userInfo)")
                abort()
            }
        }
    }
    
    
    // MARK: - Configure OData
    private func configureOData(_ urlSession: SAPURLSession, _ serviceRoot: URL) {
        let odataProvider = OnlineODataProvider(serviceName: "ZMURPURCHASEGROUPSRVEntities", serviceRoot: serviceRoot, sapURLSession: urlSession)
        // Disables version validation of the backend OData service
        // TODO: Should only be used in demo and test applications
        odataProvider.serviceOptions.checkVersion = false
        self.zmurpurchasegroupsrvEntities = ZMURPURCHASEGROUPSRVEntities(provider: odataProvider)
        // To update entity force to use X-HTTP-Method header
        
        self.zmurpurchasegroupsrvEntities.provider.networkOptions.tunneledMethods.append("MERGE")
    }
    
    

    func getOSInfo()->String {
        let os = ProcessInfo().operatingSystemVersion
        //return String(os.majorVersion) + "." + String(os.minorVersion) + "." + String(os.patchVersion)
        return String(os.majorVersion) + "." + String(os.minorVersion)
    }


    
    //200
    func checkVersion(){
        //Prod
        // "https://appdownloaddee8964f1.us2.hana.ondemand.com/AppDownload/app/download?fileType=APK"
        //QA and Dev
        // "https://appdownloaddfe0918b2.us2.hana.ondemand.com/AppDownload/qa/app/download?fileType=IPA"
       // https://hseincidentappdownloadee8964f1.us2.hana.ondemand.com/AppDownload/murphyDort/appFile/download?fileType=IPA&application=DFT
        let header = [ "x-csrf-token" : "fetch"]
        // running rules
        //https://mobile-dee8964f1.us2.hana.ondemand.com/JavaAPI_Dest/MurphyHierarchy/murphy/appFile/download?fileType=IPA&application=DFT
        
        let urlString = "\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/appFile/download?fileType=IPA&application=\(CurrentApp.appStandard)&iosVersion=" + self.getOSInfo()
        

//        let urlString = "\(BaseUrl.apiURL)/com.appUpdate.dest/AppDownload/murphyDortappFile/download?fileType=IPA&application=\(CurrentApp.appStandard)"
//        let urlString = "\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/appFile/download?fileType=IPA&application=\(CurrentApp.appStandard)&osVersion=" + self.getOSInfo()
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async{
                    let value = String(data: data ?? Data() , encoding: .utf8)
                    self!.convertToJSON(value: value!)
                }
            }
            else{
                DispatchQueue.main.async{
                    let message = "Internet connection not available."
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: nil)
                    alertController.addAction(okAction)
                    if var topController = UIApplication.shared.keyWindow?.rootViewController {
                        while let presentedViewController = topController.presentedViewController {
                            topController = presentedViewController
                        }
                        topController.present(alertController, animated: true, completion: nil)
                        // topController should now be your topmost view controller
                    }
                }
            }        }
        task.resume()
       
    }
    
    func convertToJSON(value : String){
        let data = value.data(using: .utf8)
        
        do{
            if let json = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary {
                let url = json.value(forKey: "url") as? String
                let serviceVersion = json.value(forKey: "version") as? String
                
                let nsObject: AnyObject? = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as AnyObject
                let currentVersion = nsObject as? String
                if let version = serviceVersion{
                    let intDBVersion = Float(version)
                    let intCurrentVersion = Float(currentVersion!)
                    if intDBVersion! > intCurrentVersion!{
                        let alertController = UIAlertController.init(title: "App Update", message:"version \(String(describing: version)) available" , preferredStyle: UIAlertController.Style.alert)
                        let updateAction = UIAlertAction.init(title: "Install", style: UIAlertAction.Style.default, handler: { action in
                            
                            UIApplication.shared.open(URL(string: url!)!, options: [:], completionHandler: nil)
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
                    else{
                        
                    }
                }
            }
        }
        catch {
            print(error.localizedDescription)
        }
    }
    
    
    @objc func reachabilityChanged() {
        
        print("Network not reachable")
        
        if ConnectionCheck.isConnectedToNetwork(){
            checkVersion()
            OfflineTicketCreation()

            //_ = Timer.scheduledTimer(timeInterval: 100, target: self, selector: #selector(self.update), userInfo: nil, repeats: true)
        }
//        if OnboardingManager.shared.state == OnboardingManager.State.running {
//           // triggererLeftoverWorkflow()
//        }
    }
    
    
    @objc func update(){
        print("********Timer*********")
        
        let urlString = "\(BaseUrl.apiURL)/com.getloggeduser.dest/services/userapi/attributes"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
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
                        print(String(data: data, encoding: .utf8))
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
                }
            }
        }
        task.resume()
        
      
    }
    
    func triggererLeftoverWorkflow(){
        var untriggeredDFTs = [WorkFlowData]()
        untriggeredDFTs.removeAll()
        let storedUntriggered = UserDefaults.standard.data(forKey: "untriggered")
        if storedUntriggered != nil {
            let untriggered : [WorkFlowData] = (NSKeyedUnarchiver.unarchiveObject(with: storedUntriggered!) as? [WorkFlowData])!
            //UserDefaults.standard.synchronize()
            if untriggered.count > 0{
                for each in untriggered{
                    refetch(workflowDta: each)
                }
                
            }
        }
    }
    
    // MARK: Trigger again
    func refetch(workflowDta : WorkFlowData){
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
                        self!.retrigger(workflowDta: workflowDta)
                    }
                    else{
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed!" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil )
                        alertController.addAction(okAction)
                    }
                }
            }
        }
        task.resume()
        
      
    }
    
    func retrigger(workflowDta : WorkFlowData){
        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
        

        let urlString = "\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/workflow-instances"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "post"
        do{
            let requestBody = try JSONSerialization.data(withJSONObject: workflowDta.postValue, options: .fragmentsAllowed)
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
                                var untriggeredDFTs = [WorkFlowData]()
                                untriggeredDFTs.removeAll()
                                let storedUntriggered = UserDefaults.standard.data(forKey: "untriggered")
                                if storedUntriggered != nil {
                                    let untriggered : [WorkFlowData] = (NSKeyedUnarchiver.unarchiveObject(with: storedUntriggered!) as? [WorkFlowData])!
                                    UserDefaults.standard.synchronize()
                                    untriggeredDFTs.append(contentsOf: untriggered)
                                    var removeIndex : Int?
                                    for each in untriggeredDFTs{
                                        if each.ticketName == workflowDta.ticketName{
                                            print("removed tempId \(each.ticketName)")
                                            removeIndex = untriggeredDFTs.index(of: each)
                                        }
                                    }
                                }
                                let encodedData = NSKeyedArchiver.archivedData(withRootObject: untriggeredDFTs)
                                UserDefaults.standard.set(encodedData, forKey: "untriggered")
                                UserDefaults.standard.synchronize()
                            }
                            else{
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
                }
            }
            
            
        }
        task.resume()
        
      
    }
    
    // MARK: Get Offline Tickets from database
    
    func OfflineTicketCreation(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            responseMessage.removeAll()
            ticketName.removeAll()
            responseType.removeAll()
            tempIdArray.removeAll()
            department.removeAll()
            location.removeAll()
            reviewerId.removeAll()
            reviewerMail.removeAll()
            //workflowTriggered.removeAll()
            workflowResponse.removeAll()
            restCSRFToken.removeAll()
            //workflowArray.removeAll()
            postData.removeAll()
            count = 0
            let actionService = ActService(context:self.managedObjectContext)
            let actionListArray = actionService.getAll()
            for each in actionListArray{
                responseMessage.append("")
                ticketName.append("")
                responseType.append("")
                department.append("")
                location.append("")
                reviewerId.append("")
                reviewerMail.append("")
                workflowResponse.append("")
                restCSRFToken.append("")
//                workflowTriggered.append(false)
//                workflowArray.append(WorkFlowData())
                postData.append([String : Any]())
            }
            if actionListArray.count > 0{
                AppDelegate.shared.isSynchronizingInBackend = true
                SyncAttemptsMade = actionListArray.count
            }
            for each in actionListArray{
                let dataVal = each.postObj
                let postCurrentData = NSKeyedUnarchiver.unarchiveObject(with: dataVal! as Data) as? [String : Any]
                self.postData[count!] = postCurrentData!
                let combiValue = each.actionType
                let splitValue = combiValue?.components(separatedBy: "|")
                
               //combiValue?.trimmingCharacters(in: .whitespaces)
               // combiValue?.replacingOccurrences(of: <#T##StringProtocol#>, with: <#T##StringProtocol#>)
                
                self.department[count!] = splitValue![0]
                self.location[count!] = splitValue![1]
                let tempId = each.extraParam
                tempIdArray.append(tempId!)
                self.reviewerId[count!] = each.empId!
                self.reviewerMail[count!] = each.dateSync!
                
                getCSRFforCreateTicket(countValue: count!)
                self.count = self.count! + 1
            }
        }
    }
    // MARK: - XML parsing
    
    func parser(_ parser: XMLParser, didStartElement elementName: String, namespaceURI: String?, qualifiedName qName: String?, attributes attributeDict: [String : String] = [:]) {
        
        element = elementName as NSString
       // print(element)
    }
    
    func parser(_ parser: XMLParser, foundCharacters string: String) {
        
        if element.isEqual(to: "d:FieldTicketNum") {
            if string != "" {
                self.ticketName[self.id!] = string
                //self.ticketName.insert(string, at: self.id!)
                //self.ticketName.append(string)
            }
            else{
                self.ticketName[self.id!] = ""
            }
            
        }
        if element.isEqual(to: "d:Type") {
            self.responseType[self.id!] = string
            // self.responseType.insert(string, at: self.id!)
            // self.responseType.append(string)
        }
        if element.isEqual(to: "d:Message") {
            self.responseMessage[self.id!] = string
            // self.responseMessage.insert(string, at: self.id!)
            // self.responseMessage.append(string)
        }
    }
    
    func parser(_ parser: XMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?) {
        
    }
    
    
    // MARK: - To create Tickets
    func getCSRFforCreateTicket(countValue : Int)
    {
        
        let header = [ "x-csrf-token" : "fetch"]
        


        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async{
                    
                    if let response = response as? HTTPURLResponse{
                        if let xcsrf = response.allHeaderFields["x-csrf-token"] as? String {
                            
                            self!.csrfToken = xcsrf
                            print(xcsrf)
                        }
                    }
                    if self!.csrfToken != nil{
                        self!.createTicketServiceCall(countValue: countValue)
                    }
                    else{
                        
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed!" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        // self.present(alertController, animated: true, completion: nil)
                    }
                }
            }
            
        }
        task.resume()

    }
    
    func createTicketServiceCall(countValue : Int)
    {
        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
        
        
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "post"
        do{
            let requestBody = try JSONSerialization.data(withJSONObject: postData[countValue], options: .fragmentsAllowed)
            urlRequest.httpBody = requestBody
        }
        catch{
            print("error in creating the data object from json")
        }

        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async{
                    
                    let xmlValue = String(data: data!, encoding: .utf8)
                    let data = xmlValue?.data(using: .utf8)
                    self!.id = countValue
                    self!.parser = XMLParser(data: data!)
                    self!.parser.delegate = self
                    self!.parser.parse()
                    
                    if self!.responseType[countValue] == "S" || self!.responseType[countValue] == "E"{
                        if self!.responseType[countValue] == "S" {
                            self!.workFlowTokenFetch(count: countValue)
                        }
                        let storedUnsynced = UserDefaults.standard.data(forKey: "unsynced")
                        var unsyncedTickets : [FieldTicket] = (NSKeyedUnarchiver.unarchiveObject(with: storedUnsynced!) as? [FieldTicket])!
                        var removeIndex : Int?
                        for each in unsyncedTickets{
                            print("tempId \(each.tempId)")
                            if each.tempId == self!.tempIdArray[countValue]{
                                print("removed tempId \(each.tempId)")
                                removeIndex = unsyncedTickets.index(of: each)
                            }
                        }
                        
                        let actionService = ActService(context:self!.managedObjectContext)
                        let actionListArray = actionService.getAll()
                        var currentTicket : ActList?
                        var contextId : NSManagedObjectID?
                        for each in actionListArray{
                            if each.extraParam == self!.tempIdArray[countValue]{
                                currentTicket = each
                            }
                        }
                        contextId = currentTicket?.objectID
                        actionService.delete(id: contextId!)
                        if removeIndex != nil{
                            unsyncedTickets.remove(at: removeIndex!)
                        }
                        let encodedData = NSKeyedArchiver.archivedData(withRootObject: unsyncedTickets)
                        UserDefaults.standard.set(encodedData, forKey: "unsynced")
                        UserDefaults.standard.synchronize()
                        
                    }
                    else{
                        
                    }
                    let actionService = ActService(context:self!.managedObjectContext)
                    let actionListAvailable = actionService.getAll()
                    if actionListAvailable.count == 0{
                        var createdTickets = ""
                        var cnt = 0
                        for each in self!.ticketName{
                            if each != ""{
                                createdTickets = createdTickets + each + " "
                                cnt += 1
                            }
                        }
                        let failedCount = self!.ticketName.count - cnt
                        AllTicketsController.shared.showBanner(syncedTickets: createdTickets, failedCount: failedCount)
                    }
                    
                }
            }
            
        }
        task.resume()
        
      
    }
    
    func workFlowTokenFetch(count : Int)
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
                            
                             self!.restCSRFToken[count] = xcsrf
                            print(xcsrf)
                        }
                    }
                    
                    if self!.restCSRFToken[count] != ""{
                        print("rest CSRF Token = \(self!.restCSRFToken[count])")
                        self!.triggerOverflow(countLocal: count)
                    }
                    else{
                        
                        
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed!" , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil )
                        alertController.addAction(okAction)
                        // self.present(alertController, animated: true, completion: nil)
                    }
                }
            }
            
        }
        task.resume()
        
     
        
    }
    
    
    func triggerOverflow(countLocal : Int) {
        
        let header = ["x-csrf-token" : restCSRFToken[countLocal], "Content-Type": "application/json"]
        let currentDate = Date()
        let vRno = self.postData[countLocal]["AribaSESNo"] as? String
        let contextualData : [String : Any] = ["appId" : "com.dft.native",
                                               "fieldTicketId" : self.ticketName[countLocal],
                                               "dep" : self.department[countLocal],
                                               "loc" : self.location[countLocal],
                                               "isApprovedByMurphyEngineer": "false",
                                               "cBN" : UserDefaults.standard.string(forKey: "name")!,
                                               "workflowTriggerTimeStamp" : String(describing: currentDate),
                                               "vRNo" : vRno!,
                                               "vSId" : UserDefaults.standard.string(forKey: "vid")!,
                                               "vendorId" : UserDefaults.standard.string(forKey: "vadminid")!,
                                               "vendorName" :  UserDefaults.standard.string(forKey: "vname")!,
                                               "vendorEmailId" : UserDefaults.standard.string(forKey: "vemailid")!,
                                               "murphyEngineerId" : self.reviewerId[countLocal] ,
                                               "murphyEngineerEmailId" : self.reviewerMail[countLocal] ]
        let postData = [ "definitionId" : "digital_field_ticket_workflow", "context" : contextualData as [String : Any]] as [String : Any]
        
        

        let urlString = "\(BaseUrl.apiURL)/bpmworkflowruntime/rest/v1/workflow-instances"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "post"
        urlRequest.allHTTPHeaderFields = header
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
                                
                                print(" Workflow triggered for \(self.ticketName[countLocal])")
                                //  self.workflowTriggered.append(true)
                                //                            var untriggeredDFTs = [WorkFlowData]()
                                //                            untriggeredDFTs.removeAll()
                                //                            let storedUntriggered = UserDefaults.standard.data(forKey: "untriggered")
                                //                            if storedUntriggered != nil {
                                //                                let untriggered : [WorkFlowData] = (NSKeyedUnarchiver.unarchiveObject(with: storedUntriggered!) as? [WorkFlowData])!
                                //                                UserDefaults.standard.synchronize()
                                //                                untriggeredDFTs.append(contentsOf: untriggered)
                                //                                var removeIndex : Int?
                                //                                for each in untriggeredDFTs{
                                //                                    if each.ticketName == self.ticketName[countLocal]{
                                //                                        print("removed tempId \(each.ticketName)")
                                //                                        removeIndex = untriggeredDFTs.index(of: each)
                                //                                    }
                                //                                }
                                //                            }
                                //                            let encodedData = NSKeyedArchiver.archivedData(withRootObject: untriggeredDFTs)
                                //                            UserDefaults.standard.set(encodedData, forKey: "untriggered")
                                //                            UserDefaults.standard.synchronize()
                            }
                            else{
                                //  self.workflowTriggered.append(false)
                            }
                            //                        var untriggeredDFTs = [WorkFlowData]()
                            //                        untriggeredDFTs.removeAll()
                            //                        let storedUntriggered = UserDefaults.standard.data(forKey: "untriggered")
                            //                        if storedUntriggered != nil {
                            //                            let untriggered : [WorkFlowData] = (NSKeyedUnarchiver.unarchiveObject(with: storedUntriggered!) as? [WorkFlowData])!
                            //                            UserDefaults.standard.synchronize()
                            //                            untriggeredDFTs.append(contentsOf: untriggered)
                            //                        }
                            //                        if self.SyncAttemptsMade == 0 && untriggeredDFTs.count > 0 {
                            //                            self.triggererLeftoverWorkflow()
                            //                        }
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
                }
            }
            
            
        }
        task.resume()
      
    }
}

//Mohan
extension AppDelegate {
    /// Setup an onboarding session instance
    func initializeOnboarding() {
        let presentationDelegate = ApplicationUIManager(window: self.window!)
        self.onboardingErrorHandler = OnboardingErrorHandler()
        self.sessionManager = OnboardingSessionManager(presentationDelegate: presentationDelegate, flowProvider: self.flowProvider, delegate: self.onboardingErrorHandler)
        presentationDelegate.showSplashScreenForOnboarding { _ in }

        self.onboardUser()
    }

    /// Start demo mode
    func startDemoMode() {
        let alertController = UIAlertController(
            title: LocalizedStrings.AppDelegate.startDemoModeTitle,
            message: LocalizedStrings.AppDelegate.startDemoModeMessage,
            preferredStyle: .alert
        )
        alertController.addAction(
            UIAlertAction(title: LocalizedStrings.AppDelegate.startDemoModeRestartTitle, style: .default) { _ in
                self.onboardUser()
        })

        DispatchQueue.main.async {
            guard let topViewController = ModalUIViewControllerPresenter.topPresentedViewController() else {
                fatalError("Invalid UI state")
            }
            topViewController.present(alertController, animated: true)
        }
    }

    /// Application specific code after successful onboard
    func afterOnboard() {
        guard let _ = self.sessionManager.onboardingSession else {
            fatalError("Invalid state")
        }
        let authenticationURL = sessionManager.onboardingSession?.onboardingContext().info[.authenticationURL] as? URL

        self.configureOData(sessionManagerDFT.sapURLSession, authenticationURL!)
        guard let value = self.sessionManager.onboardingSession?.settingsParameters else { return  }
        let samlObserver = SAMLObserver(settingsParameters: value)
        if !(self.sessionManager.onboardingSession?.sapURLSession.isRegistered(samlObserver) ?? true) {
            self.sessionManager.onboardingSession?.sapURLSession.register(samlObserver)
        }
        self.setRootViewController()
        self.uploadLogs()
        reachabilityChanged()
        UIApplication.shared.registerForRemoteNotifications()

    }
    private func uploadLogs() {
        guard let session = self.sessionManager.onboardingSession else {
            // Onboarding not yet performed
            return
        }
        // Upload logs after onboarding
        SAPcpmsLogUploader.uploadLogs(session) { error in
            if let error = error {
                self.logger.error("Error happened during log upload.", error: error)
                return
            }
            self.logger.info("Logs have been uploaded successfully.")
        }
    }
    /// Start onboarding a user
    func onboardUser() {
        self.sessionManager.open { error in
            if let error = error {
                self.onboardingErrorHandler?.handleOnboardingError(error)
                return
            }
            self.afterOnboard()
        }
    }
}


extension OnboardingSessionManager {
    static var shared: OnboardingSessionManager<ApplicationOnboardingSession>! {
        return AppDelegate.shared.sessionManager
    }
}
extension AppDelegate {
    static var shared: AppDelegate {
        return (UIApplication.shared.delegate as! AppDelegate)
    }
}

var statusBarUIView: UIView? {

if #available(iOS 13.0, *) {
    let tag = 3848245

    let keyWindow = UIApplication.shared.connectedScenes
        .map({$0 as? UIWindowScene})
        .compactMap({$0})
        .first?.windows.first

    if let statusBar = keyWindow?.viewWithTag(tag) {
        return statusBar
    } else {
        let height = keyWindow?.windowScene?.statusBarManager?.statusBarFrame ?? .zero
        let statusBarView = UIView(frame: height)
        statusBarView.tag = tag
        statusBarView.backgroundColor =  UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
        statusBarView.layer.zPosition = 999999

        keyWindow?.addSubview(statusBarView)
        return statusBarView
    }

}
    return nil
}

