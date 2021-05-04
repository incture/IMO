//
//  AppDelegate.swift
//  
//
//  Created by prakash on 23/09/20.
//



import SAPFiori
import SAPFioriFlows
import SAPFoundation
import SAPCommon
import UserNotifications
//import Fabric
//import Crashlytics
import SAPOData
import CoreData
import Reachability
import NotificationBannerSwift
//import FirebaseCore
import AVFoundation


@UIApplicationMain
class AppDelegate:UIResponder, UIApplicationDelegate, UISplitViewControllerDelegate, UNUserNotificationCenterDelegate {
    var window: UIWindow?
    /// Logger instance initialization
    private let logger = Logger.shared(named: "AppDelegateLogger")
    private var flowProvider = OnboardingFlowProvider()
    //let reachability = Reachability()!

    /// Delegate implementation of the application in a custom class
    var onboardingErrorHandler: OnboardingErrorHandler?

    /// Application controller instance for the application
    var sessionManager: OnboardingSessionManager<ApplicationOnboardingSession>!
    var ptwContext = PTWCoreData.shared.managedObjectContext
    var didRequestForPushNotificationOperation: (() -> Void)?
    var sessionManagerPMapp: OnboardingContext!


    func application(_: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        self.initializeLogUploader()
        self.initializeUsageCollection()
       // FirebaseApp.configure()
        //Fabric.with([Crashlytics.self])
        let notificationOption = launchOptions?[.remoteNotification]

        // 1
        if let notification = notificationOption as? [String: AnyObject],
          let aps = notification["aps"] as? [String: AnyObject] {

          // 2
          print("aps==========\(aps)")
            //self.didRecieveBypassPushNotiifcation(response: notification )
            //self.didReciveARcallingresponse(response: notification)
        }
        // Set a FUIInfoViewController as the rootViewController, since there it is none set in the Main.storyboard
        // Also, hide potentially sensitive data of the real application screen during onboarding
        self.window = UIWindow(frame: UIScreen.main.bounds)
        window?.makeKeyAndVisible()
        self.window!.rootViewController = FUIInfoViewController.createInstanceFromStoryboard()
        
        // Read more about Logging: https://help.sap.com/viewer/fc1a59c210d848babfb3f758a6f55cb1/Latest/en-US/879aaebaa8e6401dac100ea9bb8b817d.html
        //        Logger.root.logLevel = .debug
        
        self.initializeOnboarding()
        
        // Customize the UI to align SAP style
        // Read more: https://help.sap.com/doc/978e4f6c968c4cc5a30f9d324aa4b1d7/Latest/en-US/Documents/Frameworks/SAPFiori/Extensions/UINavigationBar.html
        DispatchQueue.main.async {
           // UINavigationBar().applyFioriStyle()
            let navigationBarAppearace = UINavigationBar.appearance()
            
            navigationBarAppearace.tintColor = UIColor.white
            
            navigationBarAppearace.barTintColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
            
            navigationBarAppearace.isTranslucent = false
            
            navigationBarAppearace.titleTextAttributes = [NSAttributedString.Key.foregroundColor:UIColor.white]
       // NUISettings.initWithStylesheet(name: "murphy")
        }
        
//        NotificationCenter.default.addObserver(self, selector: #selector(self.switchNetwork),name: .reachabilityChanged,object: reachability)
//        do{
//            try reachability.startNotifier()
//        }catch{
//            print("could not start reachability notifier")
//        }
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
     //   self.sockeSetup()
        return true
    }

    func applicationDidEnterBackground(_: UIApplication) {
        // Hides the application UI by presenting a splash screen, @see: ApplicationUIManager.hideApplicationScreen
       
        DispatchQueue.main.async {
//            SpeechService.shared.stopAudio()
//            TouhlessHandler.shared.buttonEnabled =  false
//            TouhlessHandler.shared.stopViewWillAppear()
        }
        
        OnboardingSessionManager.shared.lock { _ in }
    }

    func applicationWillEnterForeground(_: UIApplication) {
        // Triggers to show the passcode screen
        OnboardingSessionManager.shared.unlock { error in
            guard let error = error else {
                return
            }

            self.onboardingErrorHandler?.handleUnlockingError(error)
        }
    }

    func application(_: UIApplication, supportedInterfaceOrientationsFor _: UIWindow?) -> UIInterfaceOrientationMask {
        // Onboarding is only supported in portrait orientation
        switch OnboardingFlowController.presentationState {
        case .onboarding, .restoring:
            return .portrait
        default:
            return .allButUpsideDown
        }
    }
    
   //  MARK: - Core Data stack
    
    lazy var applicationDocumentsDirectory: NSURL = {
        // The directory the application uses to store the Core Data store file. This code uses a directory named "in.cherrywork.Workbox" in the application's documents Application Support directory.
        let urls = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return urls[urls.count-1] as NSURL
    }()
    
    lazy var managedObjectModel: NSManagedObjectModel = {
        // The managed object model for the application. This property is not optional. It is a fatal error for the application not to be able to find and load its model.
        let modelURL = Bundle.main.url(forResource: "PTW-EntityModel", withExtension: "momd")!
        return NSManagedObjectModel(contentsOf: modelURL)!
    }()
    
    lazy var persistentStoreCoordinator: NSPersistentStoreCoordinator = {
        // The persistent store coordinator for the application. This implementation creates and returns a coordinator, having added the store for the application to it. This property is optional since there are legitimate error conditions that could cause the creation of the store to fail.
        // Create the coordinator and store
        let coordinator = NSPersistentStoreCoordinator(managedObjectModel: self.managedObjectModel)
        let url = self.applicationDocumentsDirectory.appendingPathComponent("SingleViewCoreData.sqlite")
        var failureReason = "There was an error creating or loading the application's saved data."
        do {
            try coordinator.addPersistentStore(ofType: NSSQLiteStoreType, configurationName: nil, at: url, options: nil)
        } catch {
            // Report any error we got.
            var dict = [String: AnyObject]()
            dict[NSLocalizedDescriptionKey] = "Failed to initialize the application's saved data" as AnyObject?
            dict[NSLocalizedFailureReasonErrorKey] = failureReason as AnyObject?
            
            dict[NSUnderlyingErrorKey] = error as NSError
            let wrappedError = NSError(domain: "YOUR_ERROR_DOMAIN", code: 9999, userInfo: dict)
            // Replace this with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            NSLog("Unresolved error \(wrappedError), \(wrappedError.userInfo)")
            abort()
        }
        
        return coordinator
    }()
    
    lazy var managedObjectContext: NSManagedObjectContext = {
        // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.) This property is optional since there are legitimate error conditions that could cause the creation of the context to fail.
        let coordinator = self.persistentStoreCoordinator
        var managedObjectContext = NSManagedObjectContext(concurrencyType: .mainQueueConcurrencyType)
        managedObjectContext.persistentStoreCoordinator = coordinator
        return managedObjectContext
    }()
}

// Convenience accessor for the AppDelegate
extension AppDelegate {
    static var shared: AppDelegate {
        return (UIApplication.shared.delegate as! AppDelegate)
    }
}

// MARK: – Onboarding related functionality

// MARK: OnboardingSessionManager helper extension

extension OnboardingSessionManager {
    static var shared: OnboardingSessionManager<ApplicationOnboardingSession>! {
        return AppDelegate.shared.sessionManager
    }
}

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

        self.initializeRemoteNotification()
        self.uploadLogs()
        self.uploadUsageReport()
        
        guard let value = self.sessionManager.onboardingSession?.settingsParameters else { return  }
        let samlObserver = SAMLObserver(settingsParameters: value)
        if !(self.sessionManager.onboardingSession?.sapURLSession.isRegistered(samlObserver) ?? true) {
            self.sessionManager.onboardingSession?.sapURLSession.register(samlObserver)
        }
        if ConnectionCheck.isConnectedToNetwork(){
            self.reachabilityChanged()
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

// MARK: - Remote notification handling

extension AppDelegate {
    // Read more about Remote Notifications on mobile services: https://help.sap.com/doc/978e4f6c968c4cc5a30f9d324aa4b1d7/Latest/en-US/Documents/Frameworks/SAPFoundation/Remote%20Notifications.html

    func initializeRemoteNotification() {
        // Registering for remote notifications
        UIApplication.shared.registerForRemoteNotifications()
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .badge, .sound]) { _, _ in
            // Enable or disable features based on authorization.
        }
        center.delegate = self
    }

    func uploadDeviceTokenForRemoteNotification(_ deviceToken: Data) {
        guard let session = sessionManager.onboardingSession else {
            // Onboarding not yet performed
            return
        }
        let parameters = SAPcpmsRemoteNotificationParameters(deviceType: "iOS")
        session.registerDeviceToken(deviceToken: deviceToken, withParameters: parameters) { error in
            if let error = error {
                self.logger.error("Register DeviceToken failed", error: error)
                return
            }
            self.logger.info("Register DeviceToken succeeded")
        }
    }

    // MARK: AppDelegate method implementations for remote notification handling

    func application(_: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        var token = ""
        for i in 0..<deviceToken.count {
            token = token + String(format: "%02.2hhx", arguments: [deviceToken[i]])
        }
        print(token)
        self.uploadDeviceTokenForRemoteNotification(deviceToken)
    }

    func application(_: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        self.logger.error("Failed to register for Remote Notification", error: error)
    }

    // Called to let your app know which action was selected by the user for a given notification.
    func userNotificationCenter(_: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        self.logger.info("App opened via user selecting notification: \(response.notification.request.content.body)")
        // Here is where you want to take action to handle the notification, maybe navigate the user to a given screen.
        //self.didRecieveBypassPushNotiifcation(response: response.notification.request.content.userInfo as! [String:AnyObject] )
        //self.didReciveARcallingresponse(response: response.notification.request.content.userInfo as! [String:AnyObject])
        if UIApplication.shared.applicationState == .active{
            self.didRequestForPushNotificationOperation?()
        }
        completionHandler()
    }

    // Called when a notification is delivered to a foreground app.
    func userNotificationCenter(_: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        self.logger.info("Remote Notification arrived while app was in foreground: \(notification.request.content.body)")
        // Currently we are presenting the notification alert as the application were in the background.
        // If you have handled the notification and do not want to display an alert, call the completionHandler with empty options: completionHandler([])
        if let data = notification.request.content.userInfo as? [String: Any] {
           // self.arNotificationResponse(notificationData: data)
        }
        
        completionHandler([.alert, .sound])
    }
    func applicationWillTerminate(_ application: UIApplication) {
        UserDefaults.standard.removeObject(forKey: "onboardingTrue")
        UserDefaults.standard.synchronize()
        
    }
}

// MARK: - Log upload initialization and handling

// Read more about Log upload: https://help.sap.com/doc/978e4f6c968c4cc5a30f9d324aa4b1d7/Latest/en-US/Documents/Frameworks/SAPFoundation/Log%20Upload.html
extension AppDelegate {
    private func initializeLogUploader() {
        do {
            // Attaches a LogUploadFileHandler instance to the root of the logging system
            try SAPcpmsLogUploader.attachToRootLogger()
        } catch {
            self.logger.error("Failed to attach to root logger.", error: error)
        }
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
}

// MARK: - Usage collection initialization and upload

extension AppDelegate {
    private func initializeUsageCollection() {
        do {
            // Required call to configure OSlifecycle notification, specify data collection items during event triggers, and configure usage store behavior.
            try UsageBroker.shared.start()
        } catch {
            self.logger.error("Failed to initialize usage collection.", error: error)
        }
    }

    private func uploadUsageReport() {
        guard self.sessionManager.onboardingSession != nil else {
            // Onboarding not yet performed
            return
        }
        // Upload usage report after onboarding
        UsageBroker.shared.upload()
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
extension AppDelegate{

    
    // MARK: - Core Data Saving support
    
    func saveContext () {
        if managedObjectContext.hasChanges {
            do {
                try managedObjectContext.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                NSLog("Unresolved error \(nserror), \(nserror.userInfo)")
                abort()
            }
        }
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

    // MARK: - Configure OData
    private func configureOData(_ urlSession: SAPURLSession, _ serviceRoot: URL) {
      //  let odataProvider = OnlineODataProvider(serviceName: "ZMURDIGITALFIELDTICKETSRVEntities", serviceRoot: serviceRoot, sapURLSession: urlSession)
        // Disables version validation of the backend OData service
        // TODO: Should only be used in demo and test applications
      //  odataProvider.serviceOptions.checkVersion = false
       // self.zmurdigitalfieldticketsrvEntities = ZMURDIGITALFIELDTICKETSRVEntities(provider: odataProvider)
        // To update entity force to use X-HTTP-Method header
    //    self.zmurdigitalfieldticketsrvEntities.provider.networkOptions.tunneledMethods.append("MERGE")
    }
    
    // MARK: - Configure Reachability
    @objc func switchNetwork() {

        print("Network switched")
//        if ConnectionCheck.isConnectedToNetwork(){
//            if OnboardingManager.shared.state == OnboardingManager.State.running{
//                OnboardingManager.shared.lockApplication()
//                OnboardingManager.shared.applicationWillEnterForeground()
//            }
//        }
//        else{
//            Alamofire.SessionManager.default.session.getTasksWithCompletionHandler { (sessionDataTask, uploadData, downloadData) in
//                sessionDataTask.forEach { $0.cancel() }
//                uploadData.forEach { $0.cancel() }
//                downloadData.forEach { $0.cancel() }
//            }
//        }
    }

    @objc func reachabilityChanged() {

        print("Network not reachable")

        //checkVersion()
     //   if OnboardingManager.shared.state == OnboardingManager.State.running {
            //syncTaskManagementActions()
        
        
        //rajat commented to avoid "other error"
            //syncPTWActions()
            self.didRequestForPushNotificationOperation?()
       // }
    }
    // MARK: Get Offline Actions from database PTW
    func syncPTWActions() {
        if ConnectionCheck.isConnectedToNetwork() {
            
            let actionService = ActionModelService(context: self.ptwContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.JSACreate.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncJSAUpdate()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.JSACreate.rawValue{
                    let jsaDetailService = JSADetailModelService(context: self.ptwContext)
                    let searchPredicateWithPermitNumber = NSPredicate(format: "permitNumber == %@", NSNumber(value: action.permitNumber))
                    let jsaDetail = jsaDetailService.get(withPredicate: searchPredicateWithPermitNumber)
                    let postBody = self.setDataToCreateJSA(JSAObject: jsaDetail[0].getJSA())
                    if count == actionListArray.count{
                        last = true
                    }
                    ptwPostBody(type : ActionType.JSACreate.rawValue, postData : postBody , action : action, isLast : last)
                }
            }
        }
    }
    
    func syncJSAUpdate(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionModelService(context: self.ptwContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.JSAUpdate.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncJSAApprove()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.JSAUpdate.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    ptwPostBody(type : ActionType.JSAUpdate.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncJSAApprove(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionModelService(context: self.ptwContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.JSAApprove.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncPermitUpdate()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.JSAApprove.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    postJSAApproval(type : ActionType.JSAApprove.rawValue, postData : action.postData as! [String : Any], action: action , isLast : last)
                }
            }
        }
    }
    
    func syncPermitUpdate(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionModelService(context: self.ptwContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.PermitUpdate.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncPermitApprove()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.PermitUpdate.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    ptwPostBody(type : ActionType.PermitUpdate.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncPermitApprove(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionModelService(context: self.ptwContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.PermitApprove.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncPermitCloseOut()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.PermitApprove.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    ptwPostBody(type : ActionType.PermitApprove.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncPermitCloseOut(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionModelService(context: self.ptwContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.PermitCloseOut.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                cleanUpDB()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.PermitCloseOut.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    ptwPostBody(type : ActionType.PermitCloseOut.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func cleanUpDB(){
        let jsaDetailService = JSADetailModelService(context: self.ptwContext)
        let allJSADetail = jsaDetailService.getAll()
        for each in allJSADetail{
            if each.getJSA().permitNumber < 1{
                jsaDetailService.delete(id: each.objectID)
            }
        }
        jsaDetailService.saveChanges()
    }
    
    func postJSAApproval(type : String, postData : [String : Any], action : ActionModel, isLast : Bool){
        
        var url : String?
        let jsaPermitnumber = postData["jsaPermitNumber"] as? String
        if type == ActionType.JSAApprove.rawValue{
            url = "\(BaseUrl.apiURL)/com.iop.ptw/ApproveJSA.xsjs?jsaPermitNumber=\(jsaPermitnumber!)&status=APPROVED&approvedBy=\(currentUser.name!)"
        }
        let encodedUrl = url?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl ?? "")!)
        urlRequest.httpMethod = "put"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    DispatchQueue.main.async {
                    if let jsonDict = JSON as? NSDictionary {
                        let msg = jsonDict.value(forKey: "Success") as! String
                        let actionService = ActionModelService(context:self.ptwContext)
                        actionService.delete(id: action.objectID)
                        if type == ActionType.JSAApprove.rawValue && isLast{
                            self.syncPermitUpdate()
                        }
                    }
                }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    print(error!.localizedDescription)
                }
        }.resume()
        
    }
    
    func ptwPostBody(type : String, postData : [String : Any], action : ActionModel, isLast : Bool){
        var url : String = ""
        if type == ActionType.JSACreate.rawValue{
            url = "\(BaseUrl.apiURL)/com.iop.ptw/Create_Service.xsjs"
        }
        else if type == ActionType.JSAUpdate.rawValue{
            url = "\(BaseUrl.apiURL)/com.iop.ptw/JSA_Update_Service.xsjs"
        }
        else if type == ActionType.PermitUpdate.rawValue{
            url = "\(BaseUrl.apiURL)/com.iop.ptw/UpdatePermit.xsjs"
        }
        else if type == ActionType.PermitApprove.rawValue{
            url = "\(BaseUrl.apiURL)/com.iop.ptw/ApprovePermit.xsjs"
        }
        else if type == ActionType.PermitCloseOut.rawValue{
            url = "\(BaseUrl.apiURL)/com.iop.ptw/CloseOutService.xsjs"
        }
        
        var urlRequest = URLRequest(url: URL(string: url)!)
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
                    DispatchQueue.main.async {
                    if let jsonDict = JSON as? NSDictionary {
                        _ = jsonDict.value(forKey: "Success") as! String
                        let actionService = ActionModelService(context:self.ptwContext)
                        actionService.delete(id: action.objectID)
                        if type == ActionType.JSACreate.rawValue && isLast{
                            UserDefaults.standard.set("0", forKey: "offlinenumber")
                            self.syncJSAUpdate()
                        }
                        else if type == ActionType.JSAUpdate.rawValue && isLast{
                            self.syncJSAApprove()
                        }
                        else if type == ActionType.PermitUpdate.rawValue && isLast{
                            self.syncPermitApprove()
                        }
                        else if type == ActionType.PermitApprove.rawValue && isLast{
                            self.syncPermitCloseOut()
                        }
                        else if type == ActionType.PermitCloseOut.rawValue && isLast{
                            self.cleanUpDB()
                        }
                        
                    }
                }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    print(error!.localizedDescription)
                    
                }
        }.resume()
    }
    // MARK: Get Offline Actions from database TM
   /*
    func syncTaskManagementActions(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionService(context:self.managedObjectContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.Attachment.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncComments()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.Attachment.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                   postBody(type : ActionType.Attachment.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    
    func syncComments(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionService(context:self.managedObjectContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.Comment.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncInProgress()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.Comment.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    postBody(type : ActionType.Comment.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncInProgress(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionService(context:self.managedObjectContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.InProgress.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncNonDispatch()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.InProgress.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                    postBody(type : ActionType.InProgress.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncNonDispatch(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionService(context:self.managedObjectContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.NonDispatch.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncReturn()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.NonDispatch.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                     postBody(type : ActionType.NonDispatch.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncReturn(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionService(context:self.managedObjectContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.ReturnTask.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            if actionListArray.count == 0 {
                syncResolve()
            }
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.ReturnTask.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                     postBody(type : ActionType.ReturnTask.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func syncResolve(){
        if ConnectionCheck.isConnectedToNetwork(){
            
            let actionService = ActionService(context:self.managedObjectContext)
            let searchPredicate = NSPredicate(format: "actionType == %@", ActionType.ResolveTask.rawValue)
            let actionListArray = actionService.get(withPredicate: searchPredicate)
            var count = 0
            var last = false
            for action in actionListArray{
                count += 1
                if action.actionType == ActionType.ResolveTask.rawValue{
                    if count == actionListArray.count{
                        last = true
                    }
                     postBody(type : ActionType.ResolveTask.rawValue, postData : action.postData as! [String : Any], action : action, isLast : last)
                }
            }
        }
    }
    
    func postBody(type : String, postData : [String : Any], action : Action, isLast : Bool){
        var url : String = ""
        if type == ActionType.Attachment.rawValue || type == ActionType.Comment.rawValue{
            url = "\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/collaboration/create"
        }
        else if type == ActionType.NonDispatch.rawValue{
             url = "\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/nonDispatch/complete"
        }
        else{
             url = "\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/tasks/updateStatus"
        }
        
        var urlRequest = URLRequest(url: URL(string: url)!)
        urlRequest.httpMethod = "post"
        urlRequest.httpBody = self.getHttpBodayData(params: postData)
        urlRequest.addValue("Application/json", forHTTPHeaderField: "Content-Type")
        
        PMNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
                
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    DispatchQueue.main.async {
                    if let jsonDict = JSON as? NSDictionary {
                        if let status = jsonDict["status"] as? String{
                            if status == "SUCCESS"{
                                
                                let actionService = ActionService(context:self.managedObjectContext)
                                actionService.delete(id: action.objectID)
                                if type == ActionType.Attachment.rawValue && isLast{
                                    self.syncComments()
                                }
                                else if type == ActionType.Comment.rawValue && isLast{
                                    self.syncInProgress()
                                }
                                else if type == ActionType.InProgress.rawValue && isLast{
                                    self.syncNonDispatch()
                                }
                                else if type == ActionType.NonDispatch.rawValue && isLast{
                                    self.syncReturn()
                                }
                                else if type == ActionType.ReturnTask.rawValue && isLast{
                                    self.syncResolve()
                                }
                                else if type == ActionType.ResolveTask.rawValue && isLast{
                                   // self.banner.show()
                                }
                            }
                        }
                    }
                }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    DispatchQueue.main.async {
                  
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
//                    self.present(alertController, animated: true, completion: nil)
//                    self.loaderStop()
                    }
                }
        }.resume()
    }
    */
    func setDataToCreateJSA(JSAObject : JSA) -> [String:Any]
    {
        // **********JSA parsing***************
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
        //let currentDate = dateFormatter.string(from: Date())
        
        let currentDate = Date().toDateFormat(.long, isUTCTimeZone: true)
        JSAObject.createdDate = currentDate
        JSAObject.createdBy = currentUser.name!
        JSAObject.status = "SUBMITTED"
        
        
        //people list
        var peopleDict : [String:Any] = ["ptwPeopleDtoList": [[String: Any]]()]
        let jsaPeopleArray = JSAObject.peopleList
        var newArray = [[String: Any]]()
        for people in jsaPeopleArray
        {
            let dict : [String:Any] = [
                "firstName":people.firstName as String,
                "lastName":people.lastName as String,
                "contactNumber":people.contactNumber as String,
                "hasSignedJSA":1,
                "hasSignedCWP":people.hasSignedCWP as Int,
                "hasSignedHWP":people.hasSignedHWP as Int,
                "hasSignedCSE":people.hasSignedCSE as Int
            ]
            newArray.append(dict)
        }
        peopleDict["ptwPeopleDtoList"] = newArray
        
        
        //Potential hazard list
        var hazardDict : [String:Any] = ["jsaStepsDtoList": [[String: Any]]()]
        let potentialHazardArray = JSAObject.potentialHazards
        var newHazardArray = [[String: Any]]()
        for hazard in potentialHazardArray
        {
            let dict : [String:Any] = [
                "taskSteps":hazard.taskStep.utf8EncodedString() as String,
                "potentialHazards":hazard.potentialHazards.utf8EncodedString() as String,
                "hazardControls":hazard.hazardControls.utf8EncodedString() as String,
                "personResponsible":hazard.personResponsible.utf8EncodedString() as String
            ]
            newHazardArray.append(dict)
        }
        hazardDict["jsaStepsDtoList"] = newHazardArray
        
        
        //Stop the job list
        var stopDict : [String:Any] = ["jsaStopTriggerDtoList": [[String: Any]]()]
        let stopArray = JSAObject.stopTheJob
        var newStopArray = [[String: Any]]()
        var newLocationArray = [[String: Any]]()
        if stopArray.count != 0
        {
            for stop in stopArray
            {
                let dict : [String:Any] = [
                    "lineDescription": stop.utf8EncodedString(),
                    ]
                newStopArray.append(dict)
            }
        }
        else
        {
            let dict : [String:Any] = [
                "lineDescription": "",
                ]
            newStopArray.append(dict)
        }
        
        stopDict["jsaStopTriggerDtoList"] = newStopArray
        
        //location
        for each  in JSAObject.location{
            let dict : [String:Any] = [
                "facilityOrSite": each.facilityOrSite,
                "hierachyLevel": each.hierarchyLevel,
                "facility": each.facility,
                "muwi": each.muwi,
                "serialNo" : each.serialNo
            ]
            newLocationArray.append(dict)
        }
        
        //********************* permit parsing***********************
        
        
        var permitHeaderArray = [[String:Any]]()
        var permitRequiredDocumentsArray = [[String:Any]]()
        var permitTestResultsArray = [[String:Any]]()
        var permitAtmosphericTestArray = [[String:Any]]()
        
        // ********************************** COLD WORK ********************************
        if JSAObject.hasCWP == 1
        {
            let date = JSAObject.CWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.CWP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.CWP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = JSAObject.CWP.header.estimatedTimeOfCompletion
            }
            
            
            //header
            let header = [
                "isCWP":1 ,
                "isHWP":0,
                "isCSE":0,
                "plannedDateTime": plannedDateTime ,
                "location":JSAObject.CWP.header.location,
                "createdBy": JSAObject.CWP.header.permitHolder,
                "contractorPerformingWork":JSAObject.CWP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion,
                "equipmentID":JSAObject.CWP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.CWP.header.workOrderNumber.utf8EncodedString(),
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":1,
                "isHWP":0,
                "isCSE":0,
                "atmosphericTestRecord":JSAObject.CWP.docs.atmosphericTest as Int,
                "loto":JSAObject.CWP.docs.Loto as Int,
                "procedure":JSAObject.CWP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.CWP.docs.PnID as Int,
                "certificate":JSAObject.CWP.docs.certificate.utf8EncodedString() ,
                "temporaryDefeat":JSAObject.CWP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.CWP.docs.rescuePlan as Int,
                "sds":JSAObject.CWP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.CWP.docs.otherText.utf8EncodedString() ,
                "fireWatchChecklist":JSAObject.CWP.docs.fireWatch as Int,
                "liftPlan":JSAObject.CWP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.CWP.docs.simop as Int,
                "safeWorkPractice":JSAObject.CWP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
    
            
        }  // ********************************** HOT WORK ********************************
        if JSAObject.hasHWP == 1
        {
            let date = JSAObject.HWP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.HWP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.HWP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = JSAObject.HWP.header.estimatedTimeOfCompletion
            }
            
            //header
            let header = [
                "isCWP":0 ,
                "isHWP":1,
                "isCSE":0,
                "plannedDateTime": plannedDateTime ,
                "location":JSAObject.HWP.header.location,
                "createdBy": JSAObject.HWP.header.permitHolder,
                "contractorPerformingWork":JSAObject.HWP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion,
                "equipmentID":JSAObject.HWP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.HWP.header.workOrderNumber.utf8EncodedString(),
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":0,
                "isHWP":1,
                "isCSE":0,
                "atmosphericTestRecord":JSAObject.HWP.docs.atmosphericTest as Int,
                "loto":JSAObject.HWP.docs.Loto as Int,
                "procedure":JSAObject.HWP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.HWP.docs.PnID as Int,
                "certificate":JSAObject.HWP.docs.certificate.utf8EncodedString() ,
                "temporaryDefeat":JSAObject.HWP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.HWP.docs.rescuePlan as Int,
                "sds":JSAObject.HWP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.HWP.docs.otherText.utf8EncodedString() ,
                "fireWatchChecklist":JSAObject.HWP.docs.fireWatch as Int,
                "liftPlan":JSAObject.HWP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.HWP.docs.simop as Int,
                "safeWorkPractice":JSAObject.HWP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
        } // ********************************** CSE WORK ********************************
        if JSAObject.hasCSP == 1
        {
            let date = JSAObject.CSEP.header.plannedDateTime//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!
            
            let time = JSAObject.CSEP.header.startTime
            let plannedDateTime = date + " " + time
            
            var estimatedTimeOfCompletion = ""
            
            if !JSAObject.CSEP.header.estimatedTimeOfCompletion.isEmpty {
                
                estimatedTimeOfCompletion = date //+ " " + JSAObject.CSEP.header.estimatedTimeOfCompletion
            }
            
            //header
            let header = [
                "isCWP":0 ,
                "isHWP":0,
                "isCSE":1,
                "plannedDateTime": plannedDateTime ,
                "location":JSAObject.CSEP.header.location,
                "createdBy": JSAObject.CSEP.header.permitHolder,
                "contractorPerformingWork":JSAObject.CSEP.header.contractorPerformingWork.utf8EncodedString(),
                "estimatedTimeOfCompletion":estimatedTimeOfCompletion,
                "equipmentID":JSAObject.CSEP.header.equipmentID.utf8EncodedString(),
                "workOrderNumber":JSAObject.CSEP.header.workOrderNumber.utf8EncodedString(),
                "status":"SUBMITTED"
                ] as [String:Any]
            permitHeaderArray.append(header)
            
            //Required  Docs
            let recDoc = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "atmosphericTestRecord":JSAObject.CSEP.docs.atmosphericTest as Int,
                "loto":JSAObject.CSEP.docs.Loto as Int,
                "procedure":JSAObject.CSEP.docs.Procedure as Int,
                "pAndIdOrDrawing":JSAObject.CSEP.docs.PnID as Int,
                "certificate":JSAObject.CSEP.docs.certificate.utf8EncodedString() ,
                "temporaryDefeat":JSAObject.CSEP.docs.tempDefeat as Int,
                "rescuePlan":JSAObject.CSEP.docs.rescuePlan as Int,
                "sds":JSAObject.CSEP.docs.sds as Int,
                "otherWorkPermitDocs":JSAObject.CSEP.docs.otherText.utf8EncodedString() ,
                "fireWatchChecklist":JSAObject.CSEP.docs.fireWatch as Int,
                "liftPlan":JSAObject.CSEP.docs.liftPlan as Int,
                "simopDeviation":JSAObject.CSEP.docs.simop as Int,
                "safeWorkPractice":JSAObject.CSEP.docs.safeWorkPractice as Int
                ] as [String:Any]
            permitRequiredDocumentsArray.append(recDoc)
            
        }
        
        
        // final parameters
        
        // final parameters
        
        for val in JSAObject.testResult.preStartTests
        {
            let testResults = [
                "isCWP":0,
                "isHWP":0,
                "isCSE":1,
                "preStartOrWorkTest":"PRESTART",
                "oxygenPercentage":val.O2,
                "toxicType":val.toxicType.utf8EncodedString(),
                "toxicResult":val.toxicResult,
                "flammableGas":val.flammableGas.utf8EncodedString(),
                "othersType":val.othersType.utf8EncodedString(),
                "othersResult":val.othersResult,
                "date":val.Date,//.convertToDateString(format: .yearMonthDateHypenSeparator, currentDateStringFormat: .monthDateYearHiphenSeparator, shouldConvertFromUTC: true)!,
                "time":val.Time
                ] as [String:Any]
            permitTestResultsArray.append(testResults)
        }
        
        let finalDict : [String:Any] = [
            "jsaheaderDto": [
                "hasCWP": JSAObject.hasCWP,
                "hasHWP": JSAObject.hasHWP,
                "hasCSE": JSAObject.hasCSP,
                "taskDescription": JSAObject.createJSA.taskDescription.utf8EncodedString() as String,
                "identifyMostSeriousPotentialInjury": JSAObject.createJSA.injuryDescription.utf8EncodedString() as String,
                "isActive": 2,
                "status": JSAObject.status
                
            ],
            
            "jsaReviewDto":[
                "createdBy":JSAObject.createdBy,
                "createdDate":JSAObject.createdDate,
                "approvedBy":JSAObject.approvedBy,
                "approvedDate":JSAObject.approvedDate,
                "lastUpdatedBy":JSAObject.updatedBy,
                "lastUpdatedDate":JSAObject.updatedDate
            ],
            
            "jsaRiskAssesmentDto":[
                "mustModifyExistingWorkPractice":JSAObject.riskAssesment.mustExistingWork,
                "hasContinuedRisk":JSAObject.riskAssesment.afterMitigation
            ],
            
            "jsappeDto":[
                "hardHat":JSAObject.riskAssesment.hardHat,
                "safetyBoot":JSAObject.riskAssesment.safetyShoes,
                "goggles":JSAObject.riskAssesment.goggles,
                "faceShield":JSAObject.riskAssesment.faceShield,
                "safetyGlasses":JSAObject.riskAssesment.safetyGlasses,
                "singleEar":JSAObject.riskAssesment.single,
                "doubleEars":JSAObject.riskAssesment.double,
                "respiratorTypeDescription":JSAObject.riskAssesment.respiratorType.utf8EncodedString(),
                "needSCBA":JSAObject.riskAssesment.SCBA,
                "needDustMask":JSAObject.riskAssesment.dustMask,
                "cottonGlove":JSAObject.riskAssesment.cotton,
                "leatherGlove":JSAObject.riskAssesment.leather,
                "impactProtection":JSAObject.riskAssesment.impactProtection,
                "gloveDescription":JSAObject.riskAssesment.other.utf8EncodedString(),
                "chemicalGloveDescription":JSAObject.riskAssesment.chemical.utf8EncodedString(),
                "fallProtection":JSAObject.riskAssesment.fallProtection,
                "fallRestraint":JSAObject.riskAssesment.fallRestraint,
                "chemicalSuit":JSAObject.riskAssesment.chemicalSuit,
                "apron":JSAObject.riskAssesment.apron,
                "flameResistantClothing":JSAObject.riskAssesment.flameResistantClothing,
                "otherPPEDescription":JSAObject.riskAssesment.otherPPE.utf8EncodedString(),
                "needFoulWeatherGear":JSAObject.riskAssesment.foulWeatherGear,
                "haveConsentOfTaskLeader":1,
                "companyOfTaskLeader":""
            ],
            
            "ptwPeopleDtoList": newArray ,
            "jsaHazardsPressurizedDto":[
                "presurizedEquipment": JSAObject.hazardCategories.categories[0][0] as Int,
                "performIsolation":JSAObject.hazardCategories.categories[0][0] as Int,
                "depressurizeDrain":JSAObject.hazardCategories.categories[0][1] as Int,
                "relieveTrappedPressure":JSAObject.hazardCategories.categories[0][2] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[0][3] as Int,
                "anticipateResidual":JSAObject.hazardCategories.categories[0][4] as Int,
                "secureAllHoses":JSAObject.hazardCategories.categories[0][5] as Int
                ] as [String:Int],
            "jsaHazardsVisibilityDto":[
                "poorLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "provideAlternateLighting":JSAObject.hazardCategories.categories[1][0] as Int,
                "waitUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][1] as Int,
                "deferUntilVisibilityImprove":JSAObject.hazardCategories.categories[1][2] as Int,
                "knowDistanceFromPoles":JSAObject.hazardCategories.categories[1][3] as Int
                ] as [String:Int],
            
            "jsaHazardsPersonnelDto":[
                "personnel":JSAObject.hazardCategories.categories[2][0] as Int,
                "performInduction":JSAObject.hazardCategories.categories[2][0] as Int,
                "mentorCoachSupervise":JSAObject.hazardCategories.categories[2][1] as Int,
                "verifyCompetencies":JSAObject.hazardCategories.categories[2][2] as Int,
                "addressLimitations":JSAObject.hazardCategories.categories[2][3] as Int,
                "manageLanguageBarriers":JSAObject.hazardCategories.categories[2][4] as Int,
                "wearSeatBelts":JSAObject.hazardCategories.categories[2][5] as Int
                ] as [String:Int],
            
            "jsaHazardscseDto":[
                "confinedSpaceEntry":JSAObject.hazardCategories.categories[3][0] as Int,
                "discussWorkPractice":JSAObject.hazardCategories.categories[3][0] as Int,
                "conductAtmosphericTesting":JSAObject.hazardCategories.categories[3][1] as Int,
                "monitorAccess":JSAObject.hazardCategories.categories[3][2] as Int,
                "protectSurfaces":JSAObject.hazardCategories.categories[3][3] as Int,
                "prohibitMobileEngine":JSAObject.hazardCategories.categories[3][4] as Int,
                "provideObserver":JSAObject.hazardCategories.categories[3][5] as Int,
                "developRescuePlan":JSAObject.hazardCategories.categories[3][6] as Int
                ] as [String:Int],
            "jsaHazardsSimultaneousDto":
                [
                    "simultaneousOperations":JSAObject.hazardCategories.categories[4][0] as Int,
                    "followSimopsMatrix":JSAObject.hazardCategories.categories[4][0] as Int,
                    "mocRequiredFor":JSAObject.hazardCategories.categories[4][1] as Int,
                    "interfaceBetweenGroups":JSAObject.hazardCategories.categories[4][2] as Int,
                    "useBarriersAnd":JSAObject.hazardCategories.categories[4][3] as Int,
                    "havePermitSigned":JSAObject.hazardCategories.categories[4][4] as Int
                    ] as [String:Int],
            "jsaHazardsIgnitionDto":[
                "ignitionSources":JSAObject.hazardCategories.categories[5][0] as Int,
                "removeCombustibleMaterials":JSAObject.hazardCategories.categories[5][0] as Int,
                "provideFireWatch":JSAObject.hazardCategories.categories[5][1] as Int,
                "implementAbrasiveBlastingControls":JSAObject.hazardCategories.categories[5][2] as Int,
                "conductContinuousGasTesting":JSAObject.hazardCategories.categories[5][3] as Int,
                "earthForStaticElectricity":JSAObject.hazardCategories.categories[5][4] as Int
                ] as [String:Int],
            "jsaHazardsSubstancesDto":[
                "hazardousSubstances":JSAObject.hazardCategories.categories[6][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[6][0] as Int,
                "followSdsControls":JSAObject.hazardCategories.categories[6][1] as Int,
                "implementHealthHazardControls":JSAObject.hazardCategories.categories[6][2] as Int,
                "testMaterial":JSAObject.hazardCategories.categories[6][3] as Int
                ] as [String:Int],
            "jsaHazardsSpillsDto":[
                "potentialSpills":JSAObject.hazardCategories.categories[7][0] as Int,
                "drainEquipment":JSAObject.hazardCategories.categories[7][0] as Int,
                "connectionsInGoodCondition":JSAObject.hazardCategories.categories[7][1] as Int,
                "spillContainmentEquipment":JSAObject.hazardCategories.categories[7][2] as Int,
                "haveSpillCleanupMaterials":JSAObject.hazardCategories.categories[7][3] as Int,
                "restrainHosesWhenNotInUse":JSAObject.hazardCategories.categories[7][4] as Int
                ] as [String:Int],
            
            "jsaHazardsWeatherDto":[
                "weather":JSAObject.hazardCategories.categories[8][0] as Int,
                "controlsForSlipperySurface":JSAObject.hazardCategories.categories[8][0] as Int,
                "heatBreak":JSAObject.hazardCategories.categories[8][1] as Int,
                "coldHeaters":JSAObject.hazardCategories.categories[8][2] as Int,
                "lightning":JSAObject.hazardCategories.categories[8][3] as Int
                ] as [String:Int],
            "jsaHazardsHighNoiseDto":[
                "highNoise":JSAObject.hazardCategories.categories[9][0] as Int,
                "wearCorrectHearing":JSAObject.hazardCategories.categories[9][0] as Int,
                "manageExposureTimes":JSAObject.hazardCategories.categories[9][1] as Int,
                "shutDownEquipment":JSAObject.hazardCategories.categories[9][2] as Int,
                "useQuietTools":JSAObject.hazardCategories.categories[9][3] as Int,
                "soundBarriers":JSAObject.hazardCategories.categories[9][4] as Int,
                "provideSuitableComms":JSAObject.hazardCategories.categories[9][5] as Int
                ] as [String:Int],
            "jsaHazardsDroppedDto":[
                "droppedObjects":JSAObject.hazardCategories.categories[10][0] as Int,
                "markRestrictEntry":JSAObject.hazardCategories.categories[10][0] as Int,
                "useLiftingEquipmentToRaise":JSAObject.hazardCategories.categories[10][1] as Int,
                "secureTools":JSAObject.hazardCategories.categories[10][2] as Int
                ] as [String:Int],
            "jsaHazardsLiftingDto":[
                "liftingEquipment":JSAObject.hazardCategories.categories[11][0] as Int,
                "confirmEquipmentCondition":JSAObject.hazardCategories.categories[11][0] as Int,
                "obtainApprovalForLifts":JSAObject.hazardCategories.categories[11][1] as Int,
                "haveDocumentedLiftPlan":JSAObject.hazardCategories.categories[11][2] as Int
                ] as [String:Int],
            "jsaHazardsHeightsDto":[
                "workAtHeights":JSAObject.hazardCategories.categories[12][0] as Int,
                "discussWorkingPractice":JSAObject.hazardCategories.categories[12][0] as Int,
                "verifyFallRestraint":JSAObject.hazardCategories.categories[12][1] as Int,
                "useFullBodyHarness":JSAObject.hazardCategories.categories[12][2] as Int,
                "useLockTypeSnaphoooks":JSAObject.hazardCategories.categories[12][3] as Int
                ] as [String:Int],
            "jsaHazardsElectricalDto":[
                "portableElectricalEquipment":JSAObject.hazardCategories.categories[13][0] as Int,
                "inspectToolsForCondition":JSAObject.hazardCategories.categories[13][0] as Int,
                "implementGasTesting":JSAObject.hazardCategories.categories[13][1] as Int,
                "protectElectricalLeads":JSAObject.hazardCategories.categories[13][2] as Int,
                "identifyEquipClassification":JSAObject.hazardCategories.categories[13][3] as Int
                ] as [String:Int],
            "jsaHazardsMovingDto":[
                "movingEquipment":JSAObject.hazardCategories.categories[14][0] as Int,
                "confirmMachineryIntegrity":JSAObject.hazardCategories.categories[14][0] as Int,
                "provideProtectiveBarriers":JSAObject.hazardCategories.categories[14][1] as Int,
                "observerToMonitorProximityPeopleAndEquipment":JSAObject.hazardCategories.categories[14][2] as Int,
                "lockOutEquipment":JSAObject.hazardCategories.categories[14][3] as Int,
                "doNotWorkInLineOfFire":JSAObject.hazardCategories.categories[14][4] as Int
                ] as [String:Int],
            "jsaHazardsManualDto":[
                "manualHandling":JSAObject.hazardCategories.categories[15][0] as Int,
                "assessManualTask":JSAObject.hazardCategories.categories[15][0] as Int,
                "limitLoadSize":JSAObject.hazardCategories.categories[15][1] as Int,
                "properLiftingTechnique":JSAObject.hazardCategories.categories[15][2] as Int,
                "confirmStabilityOfLoad":JSAObject.hazardCategories.categories[15][3] as Int,
                "getAssistanceOrAid":JSAObject.hazardCategories.categories[15][4] as Int
                ] as [String:Int],
            
            
            "jsaHazardsToolsDto":[
                "equipmentAndTools":JSAObject.hazardCategories.categories[16][0] as Int,
                "inspectEquipmentTool":JSAObject.hazardCategories.categories[16][0] as Int,
                "brassToolsNecessary":JSAObject.hazardCategories.categories[16][1] as Int,
                "useProtectiveGuards":JSAObject.hazardCategories.categories[16][2] as Int,
                "useCorrectTools":JSAObject.hazardCategories.categories[16][3] as Int,
                "checkForSharpEdges":JSAObject.hazardCategories.categories[16][4] as Int,
                "applyHandSafetyPrinciple":JSAObject.hazardCategories.categories[16][5] as Int
                ] as [String:Int],
            "jsaHazardsFallsDto":[
                "slipsTripsAndFalls":JSAObject.hazardCategories.categories[17][0] as Int,
                "identifyProjections":JSAObject.hazardCategories.categories[17][0] as Int,
                "flagHazards":JSAObject.hazardCategories.categories[17][1] as Int,
                "secureCables":JSAObject.hazardCategories.categories[17][2] as Int,
                "cleanUpLiquids":JSAObject.hazardCategories.categories[17][3] as Int,
                "barricadeHoles":JSAObject.hazardCategories.categories[17][4] as Int
                ] as [String:Int],
            "jsaHazardsVoltageDto":[
                "highVoltage":JSAObject.hazardCategories.categories[18][0] as Int,
                "restrictAccess":JSAObject.hazardCategories.categories[18][0] as Int,
                "dischargeEquipment":JSAObject.hazardCategories.categories[18][1] as Int,
                "observeSafeWorkDistance":JSAObject.hazardCategories.categories[18][2] as Int,
                "useFlashBurn":JSAObject.hazardCategories.categories[18][3] as Int,
                "useInsulatedGloves":JSAObject.hazardCategories.categories[18][4] as Int
                ] as [String:Int],
            "jsaHazardsExcavationdDto":[
                "excavations":JSAObject.hazardCategories.categories[19][0] as Int,
                "haveExcavationPlan":JSAObject.hazardCategories.categories[19][0] as Int,
                "locatePipesByHandDigging":JSAObject.hazardCategories.categories[19][1] as Int,
                "deEnergizeUnderground":JSAObject.hazardCategories.categories[19][2] as Int,
                "cseControls":JSAObject.hazardCategories.categories[19][3] as Int
                ] as [String:Int],
            "jsaHazardsMobileDto":[
                "mobileEquipment":JSAObject.hazardCategories.categories[20][0] as Int,
                "assessEquipmentCondition":JSAObject.hazardCategories.categories[20][0] as Int,
                "controlAccess":JSAObject.hazardCategories.categories[20][1] as Int,
                "monitorProximity":JSAObject.hazardCategories.categories[20][2] as Int,
                "manageOverheadHazards":JSAObject.hazardCategories.categories[20][3] as Int,
                "adhereToRules":JSAObject.hazardCategories.categories[20][4] as Int
                ] as [String:Int],
            
            "jsaStepsDtoList": newHazardArray ,
            
            "jsaStopTriggerDtoList": newStopArray,
            
            "ptwHeaderDtoList":permitHeaderArray,
            
            "ptwRequiredDocumentDtoList":permitRequiredDocumentsArray,
            
            "ptwApprovalDtoList": [],
            
            "ptwCloseOutDtoList":[],
            
            "ptwTestRecordDto":[
                "isCWP":0,
                "isHWP":0,
                "isCSE":0,
                "detectorUsed":JSAObject.atmosphericTesting.detectorUsed.utf8EncodedString() as String,
                "dateOfLastCalibration": JSAObject.atmosphericTesting.dateOfLastCallibration,
                "testingFrequency":JSAObject.atmosphericTesting.testFrequency.utf8EncodedString() as String,
                "continuousGasMonitoring":JSAObject.atmosphericTesting.continuousMonitoringreqd as Int,
                "priorToWorkCommencing":JSAObject.atmosphericTesting.priorToWorkCommencing as Int,
                "eachWorkPeriod":JSAObject.atmosphericTesting.eachWorkPeriod as Int,
                "everyHour":JSAObject.atmosphericTesting.noHours as Int,
                "gasTester":JSAObject.testResult.Name.utf8EncodedString() as String,
                "gasTesterComments":JSAObject.testResult.specialPrecaution.utf8EncodedString() as String,
                "areaTobeTested":JSAObject.atmosphericTesting.areaOrEquipmentTotest.utf8EncodedString() as String,
                "deviceSerialNo":JSAObject.testResult.serialNumber.utf8EncodedString() as String,
                "isO2":JSAObject.atmosphericTesting.O2 as Int,
                "isLELs":JSAObject.atmosphericTesting.Lels as Int,
                "isH2S":JSAObject.atmosphericTesting.H2S as Int,
                "other":JSAObject.atmosphericTesting.Other.utf8EncodedString() as String
                ] as [String:Any],
            
            "ptwTestResultsDtoList":permitTestResultsArray,
            
            "ptwCwpWorkTypeDto":[
                "criticalOrComplexLift":JSAObject.CWP.workTypeCW.criticalLift as Int,
                "craneOrLiftingDevice":JSAObject.CWP.workTypeCW.Crane as Int,
                "groundDisturbanceOrExcavation":JSAObject.CWP.workTypeCW.groundDist as Int,
                "handlingHazardousChemicals":JSAObject.CWP.workTypeCW.handlingChem as Int,
                "workingAtHeight":JSAObject.CWP.workTypeCW.workAtHeight as Int,
                "paintingOrBlasting":JSAObject.CWP.workTypeCW.paintBlast as Int,
                "workingOnPressurizedSystems":JSAObject.CWP.workTypeCW.workOnPressurizedSystems as Int,
                "erectingOrDismantlingScaffolding":JSAObject.CWP.workTypeCW.erectingScaffolding as Int,
                "breakingContainmentOfClosedOperatingSystem":JSAObject.CWP.workTypeCW.breakingContainment as Int,
                "workingInCloseToHazardousEnergy":JSAObject.CWP.workTypeCW.closeProximity as Int,
                "removalOfIdleEquipmentForRepair":JSAObject.CWP.workTypeCW.removalOfIdleEquip as Int,
                "higherRiskElectricalWork":JSAObject.CWP.workTypeCW.higherRisk as Int,
                "otherTypeOfWork":JSAObject.CWP.workTypeCW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.CWP.workTypeCW.descriptionOfWork.utf8EncodedString() as String
                ] as [String:Any],
            "ptwHwpWorkTypeDto":[
                "cutting":JSAObject.HWP.workTypeHW.cutting as Int,
                "wielding":JSAObject.HWP.workTypeHW.welding as Int,
                "electricalPoweredEquipment":JSAObject.HWP.workTypeHW.electricalPoweredEquipment as Int,
                "grinding":JSAObject.HWP.workTypeHW.grinding as Int,
                "abrasiveBlasting":JSAObject.HWP.workTypeHW.abrasiveBlasting as Int,
                "otherTypeOfWork":JSAObject.HWP.workTypeHW.otherText.utf8EncodedString() as String,
                "descriptionOfWorkToBePerformed":JSAObject.HWP.workTypeHW.descriptionOfWork.utf8EncodedString() as String
                ] as [String:Any],
            "ptwCseWorkTypeDto":[
                "tank":JSAObject.CSEP.workTypeCSE.tank as Int,
                "vessel":JSAObject.CSEP.workTypeCSE.vessel as Int,
                "excavation": JSAObject.CSEP.workTypeCSE.excavation as Int,
                "pit":JSAObject.CSEP.workTypeCSE.pit as Int,
                "tower":JSAObject.CSEP.workTypeCSE.tower as Int,
                "other":JSAObject.CSEP.workTypeCSE.other.utf8EncodedString() as String,
                "reasonForCSE":JSAObject.CSEP.workTypeCSE.reasonForCSE.utf8EncodedString() as String
                ] as [String:Any],
            "jsaLocationDtoList" : newLocationArray
        ]
        
        
        print("final dictionary - \(finalDict)")
        return finalDict
        
    }

}

extension AppDelegate{
    func getHttpBodayData(params:[String:Any]) -> Data?{
        let data = try! JSONSerialization.data(withJSONObject: params, options: JSONSerialization.WritingOptions.prettyPrinted)
        let json = NSString(data: data, encoding: String.Encoding.utf8.rawValue)
        if let json = json {
            
        }
        
        return json!.data(using: String.Encoding.utf8.rawValue)
    }
}
