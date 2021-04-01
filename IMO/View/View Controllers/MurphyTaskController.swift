//
//  MurphyTaskController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 13/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class MurphyTaskController: UIViewController, UISearchBarDelegate {

    
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var tableView: UITableView!
    
    var csrfToken : String?
    var allTasks = [Task]()
    var allTaskDates = [String]()
    var dateSegregratedTasks = [[Task]]()
    var searchList = [Task]()
    var searchTicketDateList = [String]()
    var refreshControl: UIRefreshControl!
    var dateSegregatedSearchTicketList = [[Task]]()
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var emptyLabel = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        searchBar.delegate = self
        let formNib = UINib(nibName: "TicketCell", bundle: nil)
        tableView.register(formNib, forCellReuseIdentifier: "TicketCell")
        createNavBar()
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        refreshControl.addTarget(self, action: #selector(MurphyTaskController.refresh), for: .valueChanged)
        tableView.addSubview(refreshControl)
        tableView.tableFooterView = UIView()
        tableView.separatorStyle = .none
        if let searchTextField = self.searchBar.value(forKey: "searchField") as? UITextField {
            searchTextField.textColor = .black
            searchTextField.backgroundColor = .white
        }
        // Do any additional setup after loading the view.
    }

    override func viewDidAppear(_ animated: Bool) {
        getTasks()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func refresh() {
        getTasks()
        self.refreshControl?.endRefreshing()
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "My Tasks"
        //   let filterItem = UIBarButtonItem.init(image: UIImage(named : "Filter")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.openFilter))
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        //  navigationItem.rightBarButtonItem = filterItem
        navigationItem.leftBarButtonItem = backItem
        
    }
    
    @objc func dismissScreen(){
        
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
    
    
    //200 success
    func getTasks()
    {
        self.loaderStart()
        
        let urlString = "\(BaseUrl.apiURL)/bpmworkflowruntime/odata/tcm/TaskCollection?$format=json"
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
                        if let jsonDict = JSON as? NSDictionary {
                            
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                self.allTasks.removeAll()
                                self.allTaskDates.removeAll()
                                self.dateSegregratedTasks.removeAll()
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    for each in result{
                                        
                                        if  (each.value(forKey: "TaskDefinitionID") as! String).contains("digital_field_ticket_workflow"){    //Continue only if taskdefinitionID  is valid
                                            let task = Task(JSON: each)
                                            // let taskData = dateSegregratedTasks[indexPath.section][indexPath.row].FieldTicketNum!
                                            let taskData = task.FieldTicketNum!
                                            // let infoData = taskData.components(separatedBy: "|")
                                            let contextData = taskData.components(separatedBy: ":")
                                            let contextValue = contextData[1].components(separatedBy: "/")
                                            let fieldTicketNum = contextValue[0].trimmingCharacters(in: .whitespacesAndNewlines)
                                            let createdByName = contextValue[4].trimmingCharacters(in: .newlines)
                                            let Department = contextValue[1].trimmingCharacters(in: .newlines)
                                            var vendorRefNum : String = ""
                                            var vendorID : String = ""
                                            var vendorName : String = ""
                                            if contextValue.count > 5{
                                                vendorRefNum = contextValue[6].trimmingCharacters(in: .newlines)
                                                vendorID = contextValue[5].trimmingCharacters(in: .newlines)
                                            }
                                            if contextValue.count > 7{
                                                vendorName = contextValue[7].trimmingCharacters(in: .newlines)
                                            }
                                            task.Department = Department
                                            task.CreatedByName = createdByName
                                            task.FieldTicketNum = fieldTicketNum
                                            task.vendorID = vendorID
                                            task.vendorRefNum = vendorRefNum
                                            task.vendorName = vendorName
                                            let ticketDate = task.CreatedDate
                                            print(ticketDate! ?? "")
                                            let timestamp = ticketDate![6 ..< 16]
                                            let unixDate = Double(timestamp)
                                            task.createdCalendarDate = Date(timeIntervalSince1970: unixDate!)
                                            let dateValue = self.convertTimeStamp(timeStamp: timestamp)
                                            let createTime = self.formatTime(providedDate: timestamp)
                                            task.CreatedDate = dateValue
                                            task.CreatedTime = createTime
                                            self.allTaskDates.append(dateValue)
                                            self.allTasks.append(task)
                                            print(each.value(forKey: "TaskDefinitionID") as! String)
                                        }
                                    }
                                    self.allTaskDates = Array(Set(self.allTaskDates))
                                    var date = [Date]()
                                    for each in self.allTaskDates{
                                        let localDate = self.formatStringToDate(providedDate: each)
                                        date.append(localDate)
                                    }
                                    let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
                                    var stringDateArray = [String]()
                                    for each in sortedDate{
                                        
                                        let date = self.formatDate(providedDate: each)
                                        stringDateArray.append(date)
                                    }
                                    self.allTaskDates.removeAll()
                                    self.allTaskDates.append(contentsOf: stringDateArray)
                                    self.dateSegregratedTasks.removeAll()
                                    for element in self.allTaskDates{
                                        var count = 0
                                        var taskData = [Task]()
                                        for each in self.allTasks{
                                            
                                            if each.CreatedDate == element{
                                                taskData.append(each)
                                            }
                                        }
                                        var date = [Date]()
                                        for each in taskData{
                                            date.append(each.createdCalendarDate!)
                                        }
                                        date = Array(Set(date))
                                        let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
                                        var taskSortedData = [Task]()
                                        for each in sortedDate{
                                            for element in taskData{
                                                if each == element.createdCalendarDate{
                                                    taskSortedData.append(element)
                                                }
                                            }
                                        }
                                        self.dateSegregratedTasks.append(taskSortedData)
                                        count = count + 1
                                    }
                                    
                                }
                                self.tableView.reloadData()
                                self.loaderStop()
                            }
                        }
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
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
    
    func formatTime(providedDate : String) -> String{
        let unixDate = Double(providedDate)
        let date = Date(timeIntervalSince1970: unixDate!)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "IST") //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "hh:mm a"
        let formattedDate = dateFormatter.string(from: date)
        return formattedDate
    }
    
    func formatTimefromDate(providedDate : Date) -> String{
        let dateFormatter = DateFormatter() //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "hh:mm a"
        let formattedDate = dateFormatter.string(from: providedDate)
        return formattedDate
    }
    
    func searchUser(searchText : String)
    {
        let searchPredicate = NSPredicate(format: "SELF.FieldTicketNum CONTAINS[c] %@", searchText)
        self.searchList.removeAll()
        let array = (self.allTasks as NSArray).filtered(using: searchPredicate)
        self.searchList = array as! [Task]
        self.searchTicketDateList.removeAll()
        for each in searchList{
            self.searchTicketDateList.append(each.CreatedDate!)
        }
        
        self.searchTicketDateList = Array(Set(self.searchTicketDateList))
        var date = [Date]()
        for each in searchTicketDateList{
            date.append(formatStringToDate(providedDate: each))
        }
        let sortedDate  = date.sorted(by: { $0.compare($1) == .orderedDescending })
        self.searchTicketDateList.removeAll()
        for each in sortedDate{
             self.searchTicketDateList.append(formatDate(providedDate: each))
        }
        self.dateSegregatedSearchTicketList.removeAll()
        for element in self.searchTicketDateList{
            var count = 0
            var searchTaskdata = [Task]()
            for each in self.searchList{
                
                if each.CreatedDate == element{
                    searchTaskdata.append(each)
                }
                
            }
            searchTaskdata = searchTaskdata.sorted(by: { ($0.createdCalendarDate)?.compare($1.createdCalendarDate!) == .orderedDescending })
            self.dateSegregatedSearchTicketList.append(searchTaskdata)
            count = count + 1
        }
        tableView.reloadData()
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        //searchBar.showsCancelButton = true
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        tableView.reloadData()
        searchBar.setShowsCancelButton(false, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
            if (searchBar.text?.count)! < 20{
                searchUser(searchText: searchText)
            }
        print("searchText \(searchText)")
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        print("searchText \(String(describing: searchBar.text))")
            if (searchBar.text?.count)! < 20 {
                searchUser(searchText: searchBar.text!)
            }
        searchBar.resignFirstResponder()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar){
        searchBar.resignFirstResponder()
        searchBar.text = ""
        tableView.reloadData()
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

extension MurphyTaskController : UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        var count : Int?
        if searchBar.text == ""{
            count = dateSegregratedTasks.count
        }
        else{
            count = dateSegregatedSearchTicketList.count
        }
        if count == 0{
            emptyLabel.text = "No Data"
            emptyLabel.textColor = UIColor.gray
            emptyLabel.textAlignment = NSTextAlignment.center
            self.tableView.backgroundView = emptyLabel
            self.tableView.separatorStyle = UITableViewCell.SeparatorStyle.none
        }
        else{
            emptyLabel.text = ""
        }
        return count!
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var count : Int?
        if searchBar.text == ""{
            count = dateSegregratedTasks[section].count
        }
        else{
            count = dateSegregatedSearchTicketList[section].count
        }
        return count!
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "TicketCell")! as! TicketCell
        cell.clipsToBounds = true
        cell.selectionStyle = .none
        if searchBar.text == ""{
            cell.setDataForTasks(task: dateSegregratedTasks[indexPath.section][indexPath.row])
        }
        else{
            cell.setDataForTasks(task: dateSegregatedSearchTicketList[indexPath.section][indexPath.row])
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 185
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
        let label = UILabel(frame: CGRect(x: 16, y: -8, width: headerView.frame.size.width, height: 45))
        if searchBar.text == ""{
            let currentDate = self.formatStringToDate(providedDate: allTaskDates[section])
            if NSCalendar.current.isDateInToday(currentDate){
                label.text = "Today"
            }
            else if NSCalendar.current.isDateInYesterday(currentDate){
                label.text = "Yesterday "
            }
            else{
                label.text = allTaskDates[section]
            }
        }
        else{
            let currentDate = self.formatStringToDate(providedDate: searchTicketDateList[section])
            if NSCalendar.current.isDateInToday(currentDate){
                label.text = "Today"
            }
            else if NSCalendar.current.isDateInYesterday(currentDate){
                label.text = "Yesterday "
            }
            else{
                label.text = searchTicketDateList[section]
            }
        }
        label.font = UIFont.boldSystemFont(ofSize: 14)
        label.textColor = UIColor.darkGray
        headerView.backgroundColor = UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        return headerView
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MurphyReviewerTicketController") as! MurphyReviewerTicketController
        if searchBar.text == ""{
            splitViewController.ticketNumber = dateSegregratedTasks[indexPath.section][indexPath.row].FieldTicketNum!
            splitViewController.instanceID = dateSegregratedTasks[indexPath.section][indexPath.row].instanceID!
        }
        else{
            splitViewController.ticketNumber = dateSegregatedSearchTicketList[indexPath.section][indexPath.row].FieldTicketNum!
            splitViewController.instanceID = dateSegregatedSearchTicketList[indexPath.section][indexPath.row].instanceID!
          //  cell.setDataForTasks(task: dateSegregatedSearchTicketList[indexPath.section][indexPath.row])
        }
        let navController = UINavigationController(rootViewController: splitViewController)
        navController.modalPresentationStyle = .fullScreen
        
        self.present(navController, animated: true, completion: nil)
    }
}
