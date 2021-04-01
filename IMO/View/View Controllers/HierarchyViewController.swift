//
//  HierarchyViewController.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 27/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import NotificationCenter
//protocol HierarchyViewDelegate: class {
//    func didSelectItem(Location: String)
//}


class HierarchyViewController: UIViewController {
    var locationtype : String? = nil
    var location : String? = nil
    var navigate : String? = nil
    var locationText : String? = nil
    var CompleteLocation : String = ""
    
    var Field : HierarchyItem? = nil
    var Facility : HierarchyItem? = nil
    var WellPad : HierarchyItem? = nil
    var Well : HierarchyItem? = nil
    
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var GenericArray : [GenericItem] = []{
        didSet{
            if GenericArray.count != 0 {
            self.title = (GenericArray[0] as! HierarchyItem).ItemType
            }
        }
    }
    var SelectedIndex: IndexPath? {
        didSet{
            if SelectedIndex != nil{
                let doneButt: UIBarButtonItem = UIBarButtonItem.init(title: "Done", style: .done, target: self, action: #selector(SelectionDone(sender:)))
                self.navigationItem.rightBarButtonItem = doneButt
                doneButt.isEnabled = true
            }
            else{
                let doneButt: UIBarButtonItem = UIBarButtonItem.init(title: "", style: .done, target: self, action: nil)
                self.navigationItem.rightBarButtonItem = doneButt
                doneButt.isEnabled = false
            }
        }
    }
    @IBOutlet weak var HierarchyTable: UITableView!

    let context = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
  
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.hidesBackButton = true
        navigationItem.leftBarButtonItem = UIBarButtonItem(image: UIImage(named :"Back"), style: .plain, target: self, action: #selector(onBackPress))
        HierarchyTable.delegate = self
        HierarchyTable.dataSource = self
        self.title = locationtype
        
        if locationtype != "Well Pad"{
        self.HierarchyTable.setEditing(true, animated: true)
        self.HierarchyTable.allowsMultipleSelectionDuringEditing = true
        }
        self.HierarchyTable.tableFooterView = UIView()
        HierarchyService(location: location, locationType: locationtype, navigate: navigate)
        // Do any additional setup after loading the view.
    }
    override func viewDidDisappear(_ animated: Bool) {
        if let temp = SelectedIndex {
        self.HierarchyTable.deselectRow(at: temp , animated: true)
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func onBackPress(){
        self.navigationController?.popViewController(animated: true)
    }
    
    
    //200 success
    func HierarchyService(location: String!, locationType: String!, navigate: String!){
        
        self.loaderStart()
        let url = "\(BaseUrl.apiURL)/JavaAPI_Dest/TaskManagement_Rest/murphy/location/getLocation"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        let param  = NSMutableDictionary()
        param.setValue(location, forKey: "location")
        param.setValue(locationType, forKey: "locationType")
        param.setValue(navigate, forKey: "navigate")
        var locationName = ""
        if locationType == "Field"{
            locationName = Field?.ItemValue1! ?? ""
        }
        else if  locationType == "Facility"{
            locationName = Facility?.ItemValue1! ?? ""
        }
        else if  locationType == "Well Pad"{
            locationName = WellPad?.ItemValue1! ?? ""
        }
        else if  locationType == "Well"{
            locationName = Well?.ItemValue1! ?? ""
        }
        if ConnectionCheck.isConnectedToNetwork(){

            var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
            urlRequest.httpMethod = "post"
            do{
              //changed dictinory to string:any
                let requestBody = try JSONSerialization.data(withJSONObject: param as Dictionary, options: .prettyPrinted)
                urlRequest.httpBody = requestBody
                 urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
                //urlRequest.httpBody = param as? Dictionary
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
                            self.loaderStop()
                            let locationService = LocationService(context:self.context)
                            _ = locationService.create(locationType: locationType, locationName: locationName , response: JSON as! NSDictionary)
                            locationService.saveChanges()
                            self.parseResponse(JSON: JSON as AnyObject)
                            
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
        else{
            let locationService = LocationService(context:self.context)
            let predicate1 = NSPredicate(format: "locationType == %@", locationType)
            let predicate2 = NSPredicate(format: "locationName == %@", locationName)
            let searchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2]
            )
            var dbResponse = locationService.get(withPredicate: searchPredicate)
            if dbResponse.count > 0{
                self.parseResponse(JSON: dbResponse[0].response as AnyObject)
            }
            else{
                let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
            
            self.loaderStop()
        }
    }
    func openHierarchy(location: String, locationtype : String , navigate : String, locationText : String , selected : HierarchyItem){
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc2 : HierarchyViewController = storyboard.instantiateViewController(withIdentifier: "HierarchyViewController") as! HierarchyViewController
        
        vc2.modalPresentationStyle = .overCurrentContext
        vc2.location = location
        vc2.locationtype = locationtype
        vc2.navigate = navigate
        vc2.locationText = locationText
        vc2.CompleteLocation = CompleteLocation + locationText
        
        switch locationtype {
        case "Field":
            vc2.Field = selected
        case "Facility":
            vc2.Field = self.Field
            vc2.Facility = selected
        case "Well Pad":
            vc2.Field = self.Field
            vc2.Facility = self.Facility
            vc2.WellPad = selected

        default:
            print("error in Open Hierarchy func")
        }
        if ConnectionCheck.isConnectedToNetwork(){
            self.navigationController?.pushViewController(vc2, animated: true)
        }
        else{
            let locationService = LocationService(context:self.context)
            let predicate1 = NSPredicate(format: "locationType == %@", locationtype)
            let predicate2 = NSPredicate(format: "locationName == %@", locationText)
            let searchPredicate = NSCompoundPredicate(
                type: .and,
                subpredicates: [predicate1, predicate2]
            )
            let dbResponse = locationService.get(withPredicate: searchPredicate)
            if dbResponse.count > 0{
                self.navigationController?.pushViewController(vc2, animated: true)
            }
            else{
                let alertController = UIAlertController.init(title: "", message:"Data not available!" , preferredStyle: UIAlertController.Style.alert)
                let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                alertController.addAction(okAction)
                self.present(alertController, animated: true, completion: nil)
            }
        }
    }
    
    // Notify Create Ticket Controller about the Location Selected
    @objc func SelectionDone(sender:UIButton!){
        let SelectedItem = GenericArray[(SelectedIndex?.row)!] as! HierarchyItem
        self.CompleteLocation = CompleteLocation + SelectedItem.ItemValue1!
        if SelectedItem.ItemType == "Well"{
            self.Well = SelectedItem
        }
        switch SelectedItem.ItemType!{
        case "Field":
            self.Field = SelectedItem
        case "Facility":
            self.Facility = SelectedItem
        case "Well Pad":
            self.WellPad = SelectedItem
        case "Well":
            self.Well = SelectedItem
        default:
            print("error in selection done")
        }
        
        let object : [String : AnyObject?] = ["CompleteLocation" : CompleteLocation as AnyObject,"Field" : Field! , "Facility" : Facility  , "WellPad" : WellPad, "Well" : Well ]
        print(object)
        NotificationCenter.default.post(name: .DidSelectLocation, object: object)
        self.navigationController?.popToRootViewController(animated: true)
    }
}
extension HierarchyViewController: UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return GenericArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = HierarchyTable.dequeueReusableCell(withIdentifier: "HierarchyTableViewCell") as! HierarchyTableViewCell
        let CurrentItem : HierarchyItem = GenericArray[indexPath.row] as! HierarchyItem
        if CurrentItem.ItemType == "Well"{
            cell.HierarchyButton.setTitle("   " + CurrentItem.ItemValue1!, for: .normal)
        }
        else{
            cell.HierarchyButton.setTitle(CurrentItem.ItemValue1!, for: .normal)
        }
        
        if CurrentItem.ItemType != "Well" && CurrentItem.ItemType != "Compressor"{
            cell.editingAccessoryType = UITableViewCell.AccessoryType.disclosureIndicator
        }
        else{
            cell.editingAccessoryType = .none
        }
        cell.delegate = self
        return cell
    }
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        if let temp = self.SelectedIndex{
            if temp == indexPath{
                self.SelectedIndex = nil
            }
        }
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let SelectedItem :HierarchyItem = GenericArray[indexPath.row] as! HierarchyItem

            if let temp = self.SelectedIndex{
                if temp == indexPath{
                    self.SelectedIndex = nil
                }
                self.HierarchyTable.deselectRow(at:temp , animated: true)
                
            }
            self.SelectedIndex = indexPath
            self.HierarchyTable.selectRow(at: indexPath, animated: true, scrollPosition: .none)
    }
    
}
extension HierarchyViewController{
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
    
    // MARK: - Offline Storage
    
//    func SaveOffline(location : String?,locationType: String?,navigate: String?,response: NSDictionary?){
//        //let LocationResponse: Data = NSKeyedArchiver.archivedData(withRootObject: response!)
//        let Locationservice = LocationService(context: context)
//        let predicate: NSPredicate = NSPredicate(format: "SELF.location == %@", argumentArray: [location ?? "Fields",locationType ?? "",navigate ?? ""])
//        let result = Locationservice.get(withPredicate: predicate)
//        if result.count == 0 {
//            _ = Locationservice.create(location:location ?? "Fields" ,locationType: locationType ?? "", navigate: navigate ?? "",response : response!)
//            Locationservice.saveChanges()
//           // print("\(location) data saved for offline")
//        }
//
//    }
//    func getSavedLocation(location : String?,locationType: String?,navigate: String?) -> NSDictionary {
//        print("Fetching Data from offline Store")
//        let predicate: NSPredicate = NSPredicate(format: "SELF.location == %@", argumentArray: [location ?? "Fields",locationType ?? "",navigate ?? ""])
//        let Locationservice = LocationService(context: context)
//        let result = Locationservice.get(withPredicate: predicate)
//        if result.count != 0 {
//        let response = result[0].value(forKey: "response")
//        let unarchivedResponse  = NSKeyedUnarchiver.unarchiveObject(with: response as! Data) as! AnyObject
//        self.parseResponse(JSON: unarchivedResponse)
//            return unarchivedResponse as! NSDictionary}
//        else {
//            return NSDictionary()
//        }
//    }
   
    
    
    func parseResponse(JSON : AnyObject){
        if let jsonDict = JSON as? NSDictionary {
            if let d = jsonDict.value(forKey: "dto") as? NSDictionary{
                self.GenericArray.removeAll()
                let locationHierarchy = d.value(forKey:"locationHierarchy") as! NSArray
                for each in locationHierarchy{
                    let item : GenericItem = HierarchyItem(item: each as! NSDictionary)
                    if (each as! NSDictionary).value(forKey: "locationType") as? String != "Compressor"{
                        self.GenericArray.append(item)
                    }
                }
                self.HierarchyTable.reloadData()
            }
            
        }
    }
}
extension HierarchyViewController: HierarchyTableViewCellDelegate{
    func didButtonTapped(cell: HierarchyTableViewCell) {
        let indexPath = HierarchyTable.indexPath(for: cell)
        let SelectedItem : HierarchyItem = GenericArray[(indexPath?.row)!] as! HierarchyItem
        if SelectedItem.ItemType != "Well" && SelectedItem.ItemType != "Compressor"{
                self.openHierarchy(location: SelectedItem.ItemValue2!,locationtype : SelectedItem.ItemType!,navigate :"CHILD",locationText: SelectedItem.ItemValue1!, selected : SelectedItem)
        }
        else if SelectedItem.ItemType == "Well"{
                self.Well = SelectedItem
            
            let object : [String : AnyObject?] = ["CompleteLocation" : CompleteLocation as AnyObject,"Field" : Field , "Facility" : Facility, "WellPad" : WellPad , "Well" : Well]
            print(object)
            self.CompleteLocation = CompleteLocation + SelectedItem.ItemValue1!
            
            NotificationCenter.default.post(name: .DidSelectLocation, object: object)
            
            self.navigationController?.popToRootViewController(animated: true)
        }
    }
}

