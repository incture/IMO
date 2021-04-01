//
//  NotificatioController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 07/03/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class NotificatioController: UIViewController {

    var FailedTicketsList = [FailedTicket]()
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    
    @IBOutlet var notificationTable: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        let formNib = UINib(nibName: "NotificationCellTableViewCell", bundle: nil)
       notificationTable.register(formNib, forCellReuseIdentifier: "NotificationCellTableViewCell")
        createNavBar()
        getFailedTickets()
        notificationTable.tableFooterView = UIView()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "Notifications"
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
        
    }
    
    @objc func dismissScreen()
    {
        self.dismiss(animated: true, completion: nil)
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        self.notificationTable.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
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
    
    func convertTimeStamp(timeStamp : String) -> String
    {
        let time = timeStamp[6 ..< 16]
        let unixDate = Double(time)
        let date = Date(timeIntervalSince1970: unixDate!)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "IST") //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "dd MMM yyyy" //Specify your format that you want
        let strDate = dateFormatter.string(from: date)
        return strDate
    }
    
    //200 success
    func getFailedTickets()
    {
        
        self.loaderStart()
        let user = UserDefaults.standard.string(forKey: "id")
        let url = "\(BaseUrl.apiURL)/com.OData.dest/DFT_Header_DuplicateSet?$filter=ServiceProviderId eq '\(user!)'&$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
    
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
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
                                if let result = d.value(forKey: "results") as? [NSDictionary]{
                                    self.FailedTicketsList.removeAll()
                                    for each in result{
                                        
                                        let failedTicket = FailedTicket(JSON:each)
                                        let createDate = failedTicket.CreatedDate
                                        failedTicket.CreatedDate = self.convertTimeStamp(timeStamp: createDate!)
                                        let createTime = failedTicket.CreatedTime
                                        failedTicket.CreatedTime = self.decodeTime(timeString: createTime!)
                                        self.FailedTicketsList.append(failedTicket)
                                        
                                    }
                                    self.notificationTable.reloadData()
                                    self.loaderStop()
                                }
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

extension NotificatioController : UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
       return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return FailedTicketsList.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "NotificationCellTableViewCell")! as! NotificationCellTableViewCell
        cell.setData(ticket: FailedTicketsList[indexPath.row])
        cell.clipsToBounds = true
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 129
    }
    

}
