//
//  AllTicketsController.swift
//  DFT
//
//  Created by Soumya Singh on 01/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//




import UIKit
import SAPFiori
import NotificationBannerSwift

class AllTicketsController: UIViewController, UISearchBarDelegate {

    @IBOutlet var listTable: UITableView!
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var floatingButton: UIButton!
    
    
    var limitCount : Int = 10
    var skipCount : Int = 0
    var reachability = Reachability()
    var isMoreDataAvailable : Bool?
    var ticketsList = [FieldTicket]()
    var dateSegregatedTicketList = [[FieldTicket]]()
    var ticketDateList = [String]()
    var filterTicketDateList = [String]()
    var filteredTicketList = [FieldTicket]()
    var dateSegregatedFilteredTicketList = [[FieldTicket]]()
    var filterApplied : String?
    var isFilterApplied : Bool?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var searchList = [FieldTicket]()
    var refreshControl: UIRefreshControl!
    var dateSegregatedSearchTicketList = [[FieldTicket]]()
    var searchTicketDateList = [String]()
    let context = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
    var emptyLabel = UILabel()
    var isSynchingInBackground : Bool = false
    static let shared = AllTicketsController()
    var isFromNots : Bool = false
    var banner = NotificationBanner(title: "Background sync completed", subtitle: "", style: .success)
    var failedBanner = NotificationBanner(title: "Ticket creation failed", subtitle: "", style: .danger)
    var startBanner = NotificationBanner(title: "Background sync started", subtitle: "", style: .info)
    
    @IBOutlet var statusHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet var tableViewStatusConstraint: NSLayoutConstraint!
    override func viewDidLoad() {
        super.viewDidLoad()
        
     
        NotificationCenter.default.addObserver(self, selector: #selector(self.changeBar),name: .reachabilityChanged,object: reachability)
        do{
            try reachability?.startNotifier()
        }catch{
            print("could not start reachability notifier")
        }
        searchBar.delegate = self
        searchBar.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
        let nib = UINib(nibName: "TicketCell", bundle: nil)
        listTable.register(nib, forCellReuseIdentifier: "TicketCell")
        listTable.rowHeight = UITableView.automaticDimension
        listTable.estimatedRowHeight = 50
        listTable.separatorStyle = UITableViewCellSeparatorStyle.none
        floatingButton.layer.cornerRadius = 30
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        refreshControl.addTarget(self, action: #selector(AllTicketsController.refresh), for: .valueChanged)
        listTable.addSubview(refreshControl)
        isFilterApplied = false
        // activityIndicator.startAnimating()
        let attributes:[NSAttributedString.Key: Any] = [
            .foregroundColor: UIColor.white,
            .font: UIFont.systemFont(ofSize: 17)
        ]
        UIBarButtonItem.appearance(whenContainedInInstancesOf: [UISearchBar.self]).setTitleTextAttributes(attributes, for: .normal)
        // Do any additional setup after loading the view.
    }
    
    @objc func changeBar(){
        if ConnectionCheck.isConnectedToNetwork(){
            statusHeightConstraint.constant = 0
            tableViewStatusConstraint.constant = 56
            
        }
        else{
            statusHeightConstraint.constant = 20
            tableViewStatusConstraint.constant = 76
        }
        self.view.setNeedsLayout()
    }
    
    @objc func refresh() {
        if ConnectionCheck.isConnectedToNetwork(){
            if AppDelegate.shared.isSynchronizingInBackend == true{
                
                let alertController = UIAlertController.init(title: "", message:"Background sync in progress. Please wait..." , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: {action in
                    self.refreshControl?.endRefreshing()
                })
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
            else{
                self.refreshControl?.endRefreshing()
                getAllTickets()
            }
        }
        else{
            let alertController = UIAlertController.init(title: "", message:"This Device is offline." , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: {action in
                self.refreshControl?.endRefreshing()
            })
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
        
        
    }
    
    func showStartBanner(){
        startBanner.show()
    }
    
    func showBanner(syncedTickets : String, failedCount : Int){
        AppDelegate.shared.isSynchronizingInBackend = false
        banner = NotificationBanner(title: "Background sync completed", subtitle: "\(syncedTickets) created", style: .info)
        banner.show()
        if failedCount > 0{
            failedBanner = NotificationBanner(title: "Ticket creation failed", subtitle: "\(failedCount) failed attempts", style: .danger)
            failedBanner.show()
        }
//        failedBanner.onTap = {
//            let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "NotificatioController") as! NotificatioController
//            let navController = UINavigationController(rootViewController: splitViewController)
//            self.present(navController, animated: true, completion: nil)
//        }
    }
    

    
    func formatStringToDate(providedDate : String) -> Date{
        
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
        dateFormatter.dateFormat = "dd MMM yyyy"
        let date = dateFormatter.date(from: providedDate)!
        return date
    }
    
    func formatDate(providedDate : Date) -> String{
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy"
        let formattedDate = formatter.string(from: providedDate)
        return formattedDate
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        DispatchQueue.main.async {
            
            self.indicator.startAnimating()
        }
        
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.createNavBar()
        if ConnectionCheck.isConnectedToNetwork(){
                self.getAllTickets()
        }
        else{
           getAllOfflineTickets()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
    
    func convertZformatToTimestamp(date : String) -> String{
        
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        let date = dateFormatter.date(from: date)!
        let dateString = String(describing: Double(date.timeIntervalSince1970))
        return dateString
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "My DFTs"
        let filterItem = UIBarButtonItem.init(image: UIImage(named : "Filter")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.openFilter))
        let infoItem = UIBarButtonItem.init(image: UIImage(named : "Info")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.openInfo))
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.rightBarButtonItems = [filterItem, infoItem]
        navigationItem.leftBarButtonItem = backItem
        if let searchTextField = self.searchBar.value(forKey: "searchField") as? UITextField {
            searchTextField.textColor = .black
            searchTextField.backgroundColor = .white
          
        }
        
    }
    
   
    
    func getAllUnsyncedTickets(){
        self.ticketsList.removeAll()
        self.ticketDateList.removeAll()
        var unsyncedTickets = [FieldTicket]()
        let storedUnsynced = UserDefaults.standard.data(forKey: "unsynced")
        if storedUnsynced != nil{
            unsyncedTickets = (NSKeyedUnarchiver.unarchiveObject(with: storedUnsynced!) as? [FieldTicket])!
            for element in unsyncedTickets{
                
                let ticketDate = self.convertZformatToTimestamp(date: element.UIcreatedOn!)
                print(ticketDate)
                let timestamp = ticketDate
                let unixDate = Double(timestamp)
                element.createCalendarDate = Date(timeIntervalSince1970: unixDate!)
                let dateValue = self.convertTimeStamp(timeStamp: timestamp)
                element.UIcreatedOn = dateValue
                let create_time = element.UIcreationTime
                element.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
                self.ticketDateList.append(dateValue)
                self.ticketsList.append(element)
                self.skipCount = self.skipCount + 1
            }
            
            self.ticketDateList = Array(Set(self.ticketDateList))
            var date = [Date]()
            for each in self.ticketDateList{
                let localDate = self.formatStringToDate(providedDate: each)
                date.append(localDate)
            }
            let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
            var stringDateArray = [String]()
            for each in sortedDate{
                
                let date = self.formatDate(providedDate: each)
                stringDateArray.append(date)
            }
            self.ticketDateList.removeAll()
            self.ticketDateList.append(contentsOf: stringDateArray)
            self.dateSegregatedTicketList.removeAll()
            
            for element in self.ticketDateList{
                
                var ticketListData = [FieldTicket]()
                for each in self.ticketsList{
                    
                    if each.UIcreatedOn == element{
                        ticketListData.append(each)
                    }
                }
                //ticketListData = ticketListData.reversed()
                var dateValue = [Date]()
                for each in ticketListData{
                    
                    dateValue.append(each.createTimestampTime!)
                }
                let sortedDateValue  = dateValue.sorted(by: { $0.compare($1) == .orderedDescending })
                var taskSortedData = [FieldTicket]()
                for each in sortedDateValue{
                    for element in ticketListData{
                        
                        if each == element.createTimestampTime{
                            taskSortedData.append(element)
                        }
                    }
                }
                self.dateSegregatedTicketList.append(ticketListData)
                
            }
            if dateSegregatedTicketList.count == 0{
                self.emptyLabel.text = "No Data"
                self.emptyLabel.textColor = UIColor.gray
                self.emptyLabel.textAlignment = NSTextAlignment.center
                self.listTable.backgroundView = self.emptyLabel
                self.listTable.reloadData()
            }
            else{
                self.emptyLabel.text = ""
            }
            self.listTable.reloadData()
        }
    }
    
    func getAllOfflineCreatedTickets(){
        
        let taskService = TaskService(context:self.context)
        let tasksListArray = taskService.getAll()
        self.ticketsList.removeAll()
        self.ticketDateList.removeAll()
        for element in tasksListArray{
            
            let dataVal = element.taskListObject
            let ticket: FieldTicket? = NSKeyedUnarchiver.unarchiveObject(with: dataVal as! Data) as? FieldTicket
            //let ticket = FieldTicket(JSON : element)
            let ticketDate = ticket?.UIcreatedOn
            print(ticketDate)
            let timestamp = ticketDate![6 ..< 16]
            let unixDate = Double(timestamp)
            ticket?.createCalendarDate = Date(timeIntervalSince1970: unixDate!)
            let dateValue = self.convertTimeStamp(timeStamp: timestamp)
            ticket?.UIcreatedOn = dateValue
            let create_time = ticket?.UIcreationTime
            ticket?.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
            self.ticketDateList.append(dateValue)
            self.ticketsList.append(ticket!)
            self.skipCount = self.skipCount + 1
        }
        
        self.ticketDateList = Array(Set(self.ticketDateList))
        var date = [Date]()
        for each in self.ticketDateList{
            let localDate = self.formatStringToDate(providedDate: each)
            date.append(localDate)
        }
        let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
        var stringDateArray = [String]()
        for each in sortedDate{
            
            let date = self.formatDate(providedDate: each)
            stringDateArray.append(date)
        }
        self.ticketDateList.removeAll()
        self.ticketDateList.append(contentsOf: stringDateArray)
        self.dateSegregatedTicketList.removeAll()
        
        for element in self.ticketDateList{
            
            var ticketListData = [FieldTicket]()
            for each in self.ticketsList{
                
                if each.UIcreatedOn == element{
                    ticketListData.append(each)
                }
            }
            var dateValue = [Date]()
            for each in ticketListData{
                
                dateValue.append(each.createTimestampTime!)
            }
            let sortedDateValue  = dateValue.sorted(by: { $0.compare($1) == .orderedDescending })
            var taskSortedData = [FieldTicket]()
            for each in sortedDateValue{
                for element in ticketListData{
                    
                    if each == element.createTimestampTime{
                        taskSortedData.append(element)
                    }
                }
            }
            //ticketListData = ticketListData.reversed()
            self.dateSegregatedTicketList.append(taskSortedData)
            
        }
        if dateSegregatedTicketList.count == 0{
            self.emptyLabel.text = "No Data"
            self.emptyLabel.textColor = UIColor.gray
            self.emptyLabel.textAlignment = NSTextAlignment.center
            self.listTable.backgroundView = self.emptyLabel
            self.listTable.reloadData()
        }
        else{
            self.emptyLabel.text = ""
        }
        self.listTable.reloadData()
    }
    
    func getAllOfflineTickets()
    {
        let taskService = TaskService(context:self.context)
        let tasksListArray = taskService.getAll()
        self.ticketsList.removeAll()
        self.ticketDateList.removeAll()
        var unsyncedTickets = [FieldTicket]()
        let storedUnsynced = UserDefaults.standard.data(forKey: "unsynced")
        if storedUnsynced != nil{
             unsyncedTickets = (NSKeyedUnarchiver.unarchiveObject(with: storedUnsynced!) as? [FieldTicket])!
            for element in unsyncedTickets{
                
                let ticketDate = convertZformatToTimestamp(date: element.UIcreatedOn!)
                print(ticketDate)
                let timestamp = ticketDate
                let unixDate = Double(timestamp)
                element.createCalendarDate = Date(timeIntervalSince1970: unixDate!)
                let dateValue = self.convertTimeStamp(timeStamp: timestamp)
                element.UIcreatedOn = dateValue
                let create_time = element.UIcreationTime
                element.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
                self.ticketDateList.append(dateValue)
                self.ticketsList.append(element)
                self.skipCount = self.skipCount + 1
            }
        }
        for element in tasksListArray{
            
            let dataVal = element.taskListObject
            let ticket: FieldTicket? = NSKeyedUnarchiver.unarchiveObject(with: dataVal as! Data) as? FieldTicket
            //let ticket = FieldTicket(JSON : element)
            let ticketDate = ticket?.UIcreatedOn
            print(ticketDate)
            let timestamp = ticketDate![6 ..< 16]
            let unixDate = Double(timestamp)
            ticket?.createCalendarDate = Date(timeIntervalSince1970: unixDate!)
            let dateValue = self.convertTimeStamp(timeStamp: timestamp)
            ticket?.UIcreatedOn = dateValue
            let create_time = ticket?.UIcreationTime
            ticket?.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
            self.ticketDateList.append(dateValue)
            self.ticketsList.append(ticket!)
            self.skipCount = self.skipCount + 1
        }
        
        self.ticketDateList = Array(Set(self.ticketDateList))
        var date = [Date]()
        for each in self.ticketDateList{
            let localDate = self.formatStringToDate(providedDate: each)
            date.append(localDate)
        }
        let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
        var stringDateArray = [String]()
        for each in sortedDate{
            
            let date = self.formatDate(providedDate: each)
            stringDateArray.append(date)
        }
        self.ticketDateList.removeAll()
        self.ticketDateList.append(contentsOf: stringDateArray)
        self.dateSegregatedTicketList.removeAll()
        
        for element in self.ticketDateList{
            
            var ticketListData = [FieldTicket]()
            for each in self.ticketsList{
                
                if each.UIcreatedOn == element{
                    ticketListData.append(each)
                }
            }
            var dateValue = [Date]()
            for each in ticketListData{
                
                dateValue.append(each.createTimestampTime!)
            }
            let sortedDateValue  = dateValue.sorted(by: { $0.compare($1) == .orderedDescending })
            var taskSortedData = [FieldTicket]()
            for each in sortedDateValue{
                for element in ticketListData{
                    
                    if each == element.createTimestampTime{
                        taskSortedData.append(element)
                    }
                }
            }
            self.dateSegregatedTicketList.append(taskSortedData)
            
        }
        if dateSegregatedTicketList.count == 0{
            self.emptyLabel.text = "No Data"
            self.emptyLabel.textColor = UIColor.gray
            self.emptyLabel.textAlignment = NSTextAlignment.center
            self.listTable.backgroundView = self.emptyLabel
            self.listTable.reloadData()
        }
        else{
            self.emptyLabel.text = ""
        }
        self.listTable.reloadData()
    }
    
    //200
    //success
    func getAllTickets() {
        
        loaderStart()
        self.skipCount = 0
        let user = UserDefaults.standard.string(forKey: "id")
        
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet?$filter=ServiceProviderId%20eq%20%27\(user!)%27%20and%20Status%20eq%20%27Ticket%20Created%27&$top=\(limitCount)&$skip=\(skipCount)&$format=json"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            let numberOfObjects = 0
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                       // print("api 2--\(JSON)")
                        if let jsonDict = JSON as? NSDictionary {
                            
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.ticketsList.removeAll()
                                    self.ticketDateList.removeAll()
                                    let taskService = TaskService(context:self.context)
                                    taskService.deleteAll()
                                    taskService.saveChanges()
                                    for element in result{
                                        
                                        print(element)
                                        let ticket = FieldTicket(JSON : element)
                                        let itemData: Data = NSKeyedArchiver.archivedData(withRootObject: ticket)
                                        _ = taskService.create(instanceId: ticket.FieldTicketNum!, taskType: "spare", empId: "spare" , dateSync: "06102017", taskListObject: itemData as NSData)
                                        taskService.saveChanges()
                                        
                                        let ticketDate = ticket.UIcreatedOn
                                        //print(ticketDate)
                                        let timestamp = ticketDate![6 ..< 16]
                                        let unixDate = Double(timestamp)
                                        ticket.createCalendarDate = Date(timeIntervalSince1970: unixDate!)
                                        let dateValue = self.convertTimeStamp(timeStamp: timestamp)
                                        ticket.UIcreatedOn = dateValue
                                        let create_time = ticket.UIcreationTime
                                        ticket.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
                                        print(ticket.CreationTime!)
                                        self.ticketDateList.append(dateValue)
                                        self.ticketsList.append(ticket)
                                        self.skipCount = self.skipCount + 1
                                    }
                                    
                                    self.ticketDateList = Array(Set(self.ticketDateList))
                                    var date = [Date]()
                                    for each in self.ticketDateList{
                                        let localDate = self.formatStringToDate(providedDate: each)
                                        date.append(localDate)
                                    }
                                    let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
                                    var stringDateArray = [String]()
                                    for each in sortedDate{
                                        
                                        let date = self.formatDate(providedDate: each)
                                        stringDateArray.append(date)
                                    }
                                    self.ticketDateList.removeAll()
                                    self.ticketDateList.append(contentsOf: stringDateArray)
                                    self.dateSegregatedTicketList.removeAll()
                                    
                                    for element in self.ticketDateList{
                                        
                                        var ticketListData = [FieldTicket]()
                                        for each in self.ticketsList{
                                            
                                            if each.UIcreatedOn == element{
                                                ticketListData.append(each)
                                            }
                                        }
                                        var dateValue = [Date]()
                                        for each in ticketListData{
                                            
                                            dateValue.append(each.createTimestampTime!)
                                        }
                                        let sortedDateValue  = dateValue.sorted(by: { $0.compare($1) == .orderedDescending })
                                        var taskSortedData = [FieldTicket]()
                                        for each in sortedDateValue{
                                            for element in ticketListData{
                                                
                                                if each == element.createTimestampTime{
                                                    taskSortedData.append(element)
                                                }
                                            }
                                        }
                                        
                                        self.dateSegregatedTicketList.append(taskSortedData)
                                        
                                    }
                                    
                                    if self.dateSegregatedTicketList.count == 0{
                                        self.emptyLabel.text = "No Data"
                                        self.emptyLabel.textColor = UIColor.gray
                                        self.emptyLabel.textAlignment = NSTextAlignment.center
                                        self.listTable.backgroundView = self.emptyLabel
                                        self.listTable.reloadData()
                                    }
                                    else{
                                        self.emptyLabel.text = ""
                                    }
                                    self.listTable.reloadData()
                                    // self.listTable.setContentOffset(CGPoint(x : 0, y : self.listTable.contentInset.top), animated: false)
                                    
                                    if(numberOfObjects < self.limitCount){
                                        self.isMoreDataAvailable = false
                                    }else{
                                        self.isMoreDataAvailable = true
                                    }
                                }
                            }
                            self.loaderStop()
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
    
    func getMoreTickets() {
        
        loaderStart()
        self.skipCount = 0
        let user = UserDefaults.standard.string(forKey: "id")
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet?$filter=ServiceProviderId%20eq%20%27\(user!)%27%20and%20Status%20eq%20%27Ticket%20Created%27&$top=\(limitCount)&$skip=\(skipCount)&$format=json"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
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
                                    var ticketData = [FieldTicket]()
                                    for element in result{
                                        
                                        let ticket = FieldTicket(JSON : element)
                                        let ticketDate = ticket.CreatedOn
                                        print(ticketDate)
                                        let timestamp = ticketDate![6 ..< 16]
                                        let dateValue = self.convertTimeStamp(timeStamp: timestamp)
                                        ticket.CreatedOn = dateValue
                                        self.ticketsList.append(ticket)
                                        self.skipCount = self.skipCount + 1
                                    }
                                    
                                    self.ticketDateList = Array(Set(self.ticketDateList))
                                    var date = [Date]()
                                    for each in self.ticketDateList{
                                        let localDate = self.formatStringToDate(providedDate: each)
                                        date.append(localDate)
                                    }
                                    let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
                                    var stringDateArray = [String]()
                                    for each in sortedDate{
                                        
                                        let date = self.formatDate(providedDate: each)
                                        stringDateArray.append(date)
                                    }
                                    self.ticketDateList.removeAll()
                                    self.ticketDateList.append(contentsOf: stringDateArray)
                                    self.dateSegregatedTicketList.removeAll()
                                    for element in self.ticketDateList{
                                        var count = 0
                                        var ticketListData = [FieldTicket]()
                                        for each in self.ticketsList{
                                            
                                            if each.CreatedOn == element{
                                                ticketListData.append(each)
                                            }
                                        }
                                        self.dateSegregatedTicketList.append(ticketListData)
                                        count = count + 1
                                    }
                                    self.listTable.reloadData()
                                    if(numberOfObjects < self.limitCount){
                                        self.isMoreDataAvailable = false
                                    }else{
                                        self.isMoreDataAvailable = true
                                    }
                                }
                            }
                            self.loaderStop()
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
    
    
    func getFilteredTaskList(filter : String)
    {
        self.filteredTicketList.removeAll()
        self.filterTicketDateList.removeAll()
        for element in self.ticketsList{
            
            if element.Status == filter{
                self.filteredTicketList.append(element)
                self.filterTicketDateList.append(element.CreatedOn!)
            }
        }
        self.filterTicketDateList = Array(Set(self.filterTicketDateList))
        self.dateSegregatedFilteredTicketList.removeAll()
        for element in self.filterTicketDateList{
            var count = 0
            var ticketListData = [FieldTicket]()
            for each in self.filteredTicketList{
                
                if each.CreatedOn == element{
                    ticketListData.append(each)
                }
            }
            self.dateSegregatedFilteredTicketList.append(ticketListData)
            count = count + 1
        }
        listTable.reloadData()
    }
    
    func searchUser(searchText : String)
    {
        let searchPredicate = NSPredicate(format: "SELF.FieldTicketNum CONTAINS[c] %@", searchText)
        self.searchList.removeAll()
        let taskService = TaskService(context:self.context)
        let tasksListArray = taskService.getAll()
        var offlineTicketList = [FieldTicket]()
        for element in tasksListArray{
            
            let dataVal = element.taskListObject
            let ticket: FieldTicket? = NSKeyedUnarchiver.unarchiveObject(with: dataVal! as Data) as? FieldTicket
            offlineTicketList.append(ticket!)
            
        }
        let array = (offlineTicketList as NSArray).filtered(using: searchPredicate)
        let offlineSearchList = array as! [FieldTicket]
        self.searchTicketDateList.removeAll()
        for each in offlineSearchList{
            let ticketDate = each.UIcreatedOn
            print(ticketDate)
            let timestamp = ticketDate![6 ..< 16]
            let dateValue = self.convertTimeStamp(timeStamp: timestamp)
            each.UIcreatedOn = dateValue
            let create_time = each.UIcreationTime
            each.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
            print(each.createTimestampTime!)
            self.searchTicketDateList.append(dateValue)
            self.searchList.append(each)
        }
        
        self.searchTicketDateList = Array(Set(self.searchTicketDateList))
        var date = [Date]()
        for each in self.searchTicketDateList{
            let localDate = self.formatStringToDate(providedDate: each)
            date.append(localDate)
        }
        let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
        var stringDateArray = [String]()
        for each in sortedDate{
            
            let date = self.formatDate(providedDate: each)
            stringDateArray.append(date)
        }
        self.dateSegregatedSearchTicketList.removeAll()
        self.searchTicketDateList.removeAll()
        self.searchTicketDateList.append(contentsOf: stringDateArray)
        for element in self.searchTicketDateList{
            var count = 0
            var ticketListData = [FieldTicket]()
            for each in self.searchList{
                
                if each.UIcreatedOn == element{
                    ticketListData.append(each)
                }
            }
            var dateValue = [Date]()
            for each in ticketListData{
                
                dateValue.append(each.createTimestampTime!)
            }
            let sortedDateValue  = dateValue.sorted(by: { $0.compare($1) == .orderedDescending })
            var taskSortedData = [FieldTicket]()
            for each in sortedDateValue{
                for element in ticketListData{
                    
                    if each == element.createTimestampTime{
                        taskSortedData.append(element)
                    }
                }
            }
            self.dateSegregatedSearchTicketList.append(taskSortedData)
            count = count + 1
        }
        if self.dateSegregatedSearchTicketList.count == 0{
            self.emptyLabel.text = "No Data"
            self.emptyLabel.textColor = UIColor.gray
            self.emptyLabel.textAlignment = NSTextAlignment.center
            self.listTable.backgroundView = self.emptyLabel
            self.listTable.reloadData()
        }
        else{
            self.emptyLabel.text = ""
        }
        listTable.reloadData()
    }
    
    //200
    //success
    func getAllSearchTickets(searchText : String)
    {
        loaderStart()
        self.skipCount = 0
        let user = UserDefaults.standard.string(forKey: "id")
       
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/DFT_HeaderSet?$filter=ServiceProviderId%20eq%20%27\(user!)%27%20and%20Status%20eq%20%27Ticket%20Created%27%20and%20FieldTicketNum%20eq%20%27\(searchText)%27&$format=json"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
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
                                    self.searchList.removeAll()
                                    self.searchTicketDateList.removeAll()
                                    //var ticketData = [FieldTicket]()
                                    for element in result{
                                        print(element)
                                        let ticket = FieldTicket(JSON : element)
                                        let ticketDate = ticket.UIcreatedOn
                                        //print(ticketDate)
                                        let timestamp = ticketDate![6 ..< 16]
                                        let dateValue = self.convertTimeStamp(timeStamp: timestamp)
                                        ticket.UIcreatedOn = dateValue
                                        let create_time = ticket.UIcreationTime
                                        ticket.createTimestampTime = self.decodeTimeToTimestamp(timeString: create_time!)
                                        print(ticket.createTimestampTime!)
                                        self.searchTicketDateList.append(dateValue)
                                        self.searchList.append(ticket)
                                        self.skipCount = self.skipCount + 1
                                        numberOfObjects = numberOfObjects + 1
                                    }
                                    
                                    self.searchTicketDateList = Array(Set(self.searchTicketDateList))
                                    var date = [Date]()
                                    for each in self.searchTicketDateList{
                                        let localDate = self.formatStringToDate(providedDate: each)
                                        date.append(localDate)
                                    }
                                    let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
                                    var stringDateArray = [String]()
                                    for each in sortedDate{
                                        
                                        let date = self.formatDate(providedDate: each)
                                        stringDateArray.append(date)
                                    }
                                    self.searchTicketDateList.removeAll()
                                    self.searchTicketDateList.append(contentsOf: stringDateArray)
                                    self.dateSegregatedSearchTicketList.removeAll()
                                    for element in self.searchTicketDateList{
                                        var count = 0
                                        var ticketListData = [FieldTicket]()
                                        for each in self.searchList{
                                            
                                            if each.UIcreatedOn == element{
                                                ticketListData.append(each)
                                            }
                                        }
                                        var dateValue = [Date]()
                                        for each in ticketListData{
                                            
                                            dateValue.append(each.createTimestampTime!)
                                        }
                                        let sortedDateValue  = dateValue.sorted(by: { $0.compare($1) == .orderedDescending })
                                        var taskSortedData = [FieldTicket]()
                                        for each in sortedDateValue{
                                            for element in ticketListData{
                                                
                                                if each == element.createTimestampTime{
                                                    taskSortedData.append(element)
                                                }
                                            }
                                        }
                                        self.dateSegregatedSearchTicketList.append(taskSortedData)
                                        count = count + 1
                                    }
                                    if self.dateSegregatedSearchTicketList.count == 0{
                                        self.emptyLabel.text = "No Data"
                                        self.emptyLabel.textColor = UIColor.gray
                                        self.emptyLabel.textAlignment = NSTextAlignment.center
                                        self.listTable.backgroundView = self.emptyLabel
                                        self.listTable.reloadData()
                                    }
                                    else{
                                        self.emptyLabel.text = ""
                                    }
                                    self.listTable.reloadData()
                                    if(numberOfObjects < self.limitCount){
                                        self.isMoreDataAvailable = false
                                    }else{
                                        self.isMoreDataAvailable = true
                                    }
                                }
                            }
                            self.loaderStop()
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
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        //searchBar.showsCancelButton = true
       
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        listTable.reloadData()
        searchBar.setShowsCancelButton(false, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
            if (searchBar.text?.count)! < 20{
                if ConnectionCheck.isConnectedToNetwork(){
                    //self.listTable.scrollToTop(animated: false)
                    getAllSearchTickets(searchText: searchText)
                }
                else{
                  //  self.listTable.scrollToTop(animated: false)
                    searchUser(searchText: searchText)
                }
            }
        print("searchText \(searchText)")
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        print("searchText \(String(describing: searchBar.text))")
            if (searchBar.text?.count)! < 20{
                if ConnectionCheck.isConnectedToNetwork(){
                    self.listTable.scrollToTop(animated: false)
                    getAllSearchTickets(searchText: searchBar.text!)
                }
                else{
                    self.listTable.scrollToTop(animated: false)
                    searchUser(searchText: searchBar.text!)
                }
        }
        searchBar.resignFirstResponder()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar){
        searchBar.resignFirstResponder()
        searchBar.text = ""
        listTable.reloadData()
    }
    
    @objc func dismissScreen()
    {
        self.dismiss(animated: true, completion: nil)
    }
    
    @objc func openInfo(){
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "NotificatioController") as! NotificatioController
        let navController = UINavigationController(rootViewController: splitViewController)
        self.present(navController, animated: true, completion: nil)
    }
    
    @objc func openFilter()
    {
        let attribute : [NSAttributedStringKey : Any] = [NSAttributedStringKey.font: UIFont.boldSystemFont(ofSize: 16),
                                                         NSAttributedStringKey.foregroundColor : UIColor.darkGray]
        
        let attributedText = NSAttributedString(string: "Filters", attributes: attribute)
        let alertController = UIAlertController(title: "", message: nil, preferredStyle: .actionSheet)
        alertController.setValue(attributedText, forKey: "attributedTitle")
        
        let create = UIAlertAction(title: "Pending", style: .default, handler: { (action) -> Void in
            
            if ConnectionCheck.isConnectedToNetwork(){
                self.listTable.scrollToTop(animated: false)
                self.getAllTickets()
            }
            else{
                self.listTable.scrollToTop(animated: false)
                self.getAllOfflineCreatedTickets()
            }
            self.emptyLabel.text = ""
        
        })

        let unsynced = UIAlertAction(title: TicketStatus.Unsync.rawValue, style: .default, handler: { (action) -> Void in
            self.filterApplied = TicketStatus.Unsync.rawValue
             if ConnectionCheck.isConnectedToNetwork(){
                self.dateSegregatedTicketList.removeAll()
                self.emptyLabel.text = "No Data"
                self.emptyLabel.textColor = UIColor.gray
                self.emptyLabel.textAlignment = NSTextAlignment.center
                self.listTable.backgroundView = self.emptyLabel
                self.listTable.reloadData()
                
            }
             else{
                self.listTable.scrollToTop(animated: false)
                self.getAllUnsyncedTickets()
            }
            
        })
        
        let cancel = UIAlertAction(title: "Cancel", style: .cancel , handler: { (action) -> Void in
            self.isFilterApplied = false
            if ConnectionCheck.isConnectedToNetwork(){
                self.listTable.scrollToTop(animated: false)
                self.getAllTickets()
            }
            else{
                self.listTable.scrollToTop(animated: false)
                self.getAllOfflineTickets()
            }
            self.emptyLabel.text = ""
            
            print("Cancel button tapped")
        })
        alertController.addAction(create)
//        alertController.addAction(verify)
//        alertController.addAction(review)
//        alertController.addAction(reject)
        alertController.addAction(unsynced)
        alertController.addAction(cancel)
        
        self.navigationController!.present(alertController, animated: true, completion: nil)
        
    }
    
    
    @IBAction func onPlusPress(_ sender: UIButton) {
        
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "CreateTicketController") as! CreateTicketController
        //splitViewController.delegate = self
        // splitViewController.modalPresentationStyle = .currentContext
        // splitViewController.preferredDisplayMode = .allVisible
        let navController = UINavigationController(rootViewController: splitViewController)
        navController.modalPresentationStyle = .fullScreen
        self.present(navController, animated: true, completion: nil)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

    func loadingCell() -> UITableViewCell{
        let cell = UITableViewCell(style: .default, reuseIdentifier: nil)
        cell.tag = -10
        cell.backgroundColor = listTable.backgroundColor
        let activityIndicator = UIActivityIndicatorView(style: .gray)
        activityIndicator.center = CGPoint(x: UIScreen.main.bounds.midX, y: cell.bounds.midY)
        cell.addSubview(activityIndicator)
        activityIndicator.startAnimating()
        return cell
    }
}

extension AllTicketsController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
//        if isFilterApplied == true{
//            return self.dateSegregatedFilteredTicketList.count
//        }
        if searchBar.text != ""{
            
            return self.dateSegregatedSearchTicketList.count
        }
        else{
        
            return self.dateSegregatedTicketList.count
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        if isFilterApplied == true{
//            return self.dateSegregatedFilteredTicketList[section].count
//        }
         if searchBar.text != ""{
            
            return dateSegregatedSearchTicketList[section].count
        }
        else{
            
            return self.dateSegregatedTicketList[section].count
        }
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "TicketCell")! as! TicketCell
        cell.clipsToBounds = true
        cell.selectionStyle = .none
//         if isFilterApplied == true{
//            cell.setData(ticket : dateSegregatedFilteredTicketList[indexPath.section][indexPath.row])
//        }
         if searchBar.text != ""{
            cell.setData(ticket : dateSegregatedSearchTicketList[indexPath.section][indexPath.row])
        }
         else{
            cell.setData(ticket : dateSegregatedTicketList[indexPath.section][indexPath.row])
        }
        return cell
    }
//    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
//        return 125
//    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
        let label = UILabel(frame: CGRect(x: 16, y: -8, width: headerView.frame.size.width, height: 45))
//        if isFilterApplied == true{
//
//            let currentDate = self.formatStringToDate(providedDate: filterTicketDateList[section])
//            if NSCalendar.current.isDateInToday(currentDate){
//                label.text = "Today"
//            }
//            else if NSCalendar.current.isDateInYesterday(currentDate){
//                label.text = "Yesterday"
//            }
//            else{
//                label.text = filterTicketDateList[section]
//            }
//        }
        if searchBar.text != ""{
            let currentDate = self.formatStringToDate(providedDate: searchTicketDateList  [section])
            if NSCalendar.current.isDateInToday(currentDate){
                label.text = "Today "
            }
            else if NSCalendar.current.isDateInYesterday(currentDate){
                label.text = "Yesterday "
            }
            else{
                label.text = searchTicketDateList[section]
            }
        }
        else{
            let currentDate = self.formatStringToDate(providedDate: ticketDateList[section])
            if NSCalendar.current.isDateInToday(currentDate){
                label.text = "Today"
            }
            else if NSCalendar.current.isDateInYesterday(currentDate){
                label.text = "Yesterday "
            }
            else{
                label.text = ticketDateList[section]
            }
        }
        
        label.font = UIFont.boldSystemFont(ofSize: 14)
        label.textColor = UIColor.darkGray
        headerView.backgroundColor = UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        return headerView
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
    
    func decodeTimeToTimestamp(timeString : String) -> Date{
        
        
        let hour = timeString[2 ..< 4] // returns String "o, worl"
        let minutes = timeString[5 ..< 7]
        let seconds = timeString[8 ..< 10]
        let timeData = hour + ":" + minutes + ":" + seconds
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss"
        let myDate = dateFormatter.date(from: timeData)!
        return myDate
//        dateFormatter.dateFormat = "hh:mm a"
//        let somedateString = dateFormatter.string(from: myDate)
//        return somedateString
    }
}

extension String {
    
    subscript (i: Int) -> Character {
        return self[index(startIndex, offsetBy: i)]
    }
    
    subscript (i: Int) -> String {
        return String(self[i] as Character)
    }
    
    subscript (r: Range<Int>) -> String {
        let start = index(startIndex, offsetBy: r.lowerBound)
        let end = index(startIndex, offsetBy: r.upperBound)
        return String(self[(start ..< end)])
    }
}

extension UITableView {
    func scrollToTop(animated: Bool) {
        setContentOffset(CGPoint.zero, animated: animated)
    }
}

extension UIViewController{
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}
