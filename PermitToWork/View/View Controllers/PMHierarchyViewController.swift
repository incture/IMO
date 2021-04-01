//
//  PMHierarchyViewController.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 27/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import SAPFiori
import NotificationCenter
//protocol HierarchyViewDelegate: class {
//    func didSelectItem(Location: String)
//}

protocol GenericItem {
    var ItemValue1:String?{get set}
    var ItemValue2:String?{get set}
    var ItemValue3:String?{get set}
    
    static var identifier : String{ get}
}

class HierarchyItem:GenericItem {
    var ItemValue3: String?
    
    static var identifier: String{
        get{
            return "HierarchyItem"
        }
    }
    var ItemValue1: String?
    var ItemValue2: String?
    var ItemType : String?
    init(item : NSDictionary) {
        self.ItemValue1 = item.value(forKey: "locationText") as? String
        self.ItemValue2 = item.value(forKey: "location") as? String
        self.ItemType = item.value(forKey: "locationType") as? String
    }
}
//class PMHierarchyViewController: UIViewController  , SAPFioriLoadingIndicator{
//    var loadingIndicator: FUILoadingIndicatorView?
//    var locationtype : String? = nil
//    var location : String? = nil
//    var navigate : String? = nil
//    var locationText : String? = nil
//    var CompleteLocation : String = ""
//    var parentVC: UIViewController?
//    var locationChange:String?
//
//    var Field : HierarchyItem? = nil
//    var Facility : HierarchyItem? = nil
//    var WellPad : HierarchyItem? = nil
//    var Well : HierarchyItem? = nil
//    var Leg : HierarchyItem? = nil
//    var Flare : HierarchyItem? = nil
//    var Compressor : HierarchyItem? = nil
//    weak var currentNotification : PMNotification?
//    weak var searchNotification : PMSearchNotification?
//    weak var searchWorkOrderData : PMWorkOrderSearch?
//    weak var workOrderDetails : PMWorkorderDetails?
//    var GenericArray : [GenericItem] = []
//    //{
////        didSet{
////            if GenericArray.count != 0 {
////                DispatchQueue.main.async {
////                    if self.GenericArray.count > 0{
////                        if (self.GenericArray.first as! HierarchyItem).ItemType != nil{
////                            if let title = (self.GenericArray[0] as! HierarchyItem).ItemType{
////                                self.title = title
////                            }
////                        }
////                    }
////                }
////            }
////        }
////    }
//    var SelectedIndex: IndexPath? {
//        didSet{
//            if SelectedIndex != nil{
//                let doneButt: UIBarButtonItem = UIBarButtonItem.init(title: "Done", style: .done, target: self, action: #selector(SelectionDone(sender:)))
//                self.navigationItem.rightBarButtonItem = doneButt
//                self.navigationItem.rightBarButtonItem?.tintColor = .white
//                doneButt.isEnabled = true
//            }
//            else{
//                let doneButt: UIBarButtonItem = UIBarButtonItem.init(title: "", style: .done, target: self, action: nil)
//                self.navigationItem.rightBarButtonItem = doneButt
//                self.navigationItem.rightBarButtonItem?.tintColor = .white
//                doneButt.isEnabled = false
//            }
//        }
//    }
//    
//    @IBOutlet var HierarchyTable: UITableView!
//    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        self.navigationItem.hidesBackButton = true
//        self.navigationController?.navigationBar.isHidden = false
//        self.view.endEditing(true)
//        navigationItem.leftBarButtonItem = UIBarButtonItem(image: UIImage(named :"back"), style: .plain, target: self, action: #selector(onBackPress))
//        navigationItem.leftBarButtonItem?.tintColor = .white
//        HierarchyTable.delegate = self
//        HierarchyTable.dataSource = self
//        DispatchQueue.main.async {
//            self.title = self.locationtype
//        }
//        if locationtype != "Well Pad"{
//            self.HierarchyTable.setEditing(true, animated: true)
//            self.HierarchyTable.allowsMultipleSelectionDuringEditing = true
//        }
//        self.HierarchyTable.tableFooterView = UIView()
//        if self.navigate == nil{
//            if currentUser.country == "CA"{
//            self.location = "MUR-CA"
//            self.locationtype = "BASE"
//            self.navigate = "CHILD"
//            }else{
//                self.location = "MUR-US-EFS"
//                self.locationtype = "BASE"
//                self.navigate = "CHILD"
//            }
//        }
//        HierarchyService(location: location, locationType: locationtype, navigate: navigate)
//        // Do any additional setup after loading the view.
//    }
//    override func viewDidDisappear(_ animated: Bool) {
//        if let temp = SelectedIndex {
//            self.HierarchyTable.deselectRow(at: temp , animated: true)
//        }
//    }
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//        // Dispose of any resources that can be recreated.
//    }
//    
//    @objc func onBackPress(){
//        if parentVC is BypassListViewController {
//            if let viewControllers = self.navigationController?.viewControllers, viewControllers.count != 1 {
//                self.navigationController?.popViewController(animated: true)
//                return
//            }
//            self.dismiss(animated: true, completion: nil)
//            return
//        }
//        self.navigationController?.popViewController(animated: true)
//    }
//    
//    func HierarchyService(location: String!, locationType: String!, navigate: String!){
//        
////                self.loaderStart()
//        var url : String = ""
////        if currentUser.country == "CA"{
////            url = MPConfig.shared.baseURL +  "CanadaJavaDest/MurphyHierarchy/murphy/location/getLocation"
////        }
////        else{
//            url = MPConfig.shared.baseURL +  "JavaAPI_Dest/TaskManagement_Rest/murphy/location/getLocation"
//       // }
//        DispatchQueue.main.async {
//        self.showFioriLoadingIndicator()
//        }
//        
//        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
//        let param  = NSMutableDictionary()
//        param.setValue(location ?? "", forKey: "location")
//        param.setValue(locationType ?? "", forKey: "locationType")
//        param.setValue(navigate ?? "", forKey: "navigate")
//        var locationName = ""
//        if locationType == "Field"{
//            locationName = Field?.ItemValue1! ?? ""
//        }
//        else if  locationType == "Facility"{
//            locationName = Facility?.ItemValue1! ?? ""
//        }
//            
//        else if locationType == "Leg"{
//            locationName = Leg?.ItemValue1! ?? ""
//        }
//        else if  locationType == "Well Pad"{
//            locationName = WellPad?.ItemValue1! ?? ""
//        }
//        else if  locationType == "Well"{
//            locationName = Well?.ItemValue1! ?? ""
//        }
//        if ConnectionCheck.isConnectedToNetwork(){
//            let urlRequest = RequestURL.shared.urlRequest(for: encodedUrl!, method: "POST", body: param as? [String : Any])
//            let task = PMNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//                guard let self = self else { return }
//                DispatchQueue.main.async {
//                self.hideFioriLoadingIndicator()
//                }
//                guard error == nil else {
//                    DispatchQueue.main.async {
//                    let message = error?.localizedDescription
//                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
//                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
//                    alertController.addAction(okAction)
//                    self.present(alertController, animated: true, completion: nil)
//                    }
//                    return
//                }
//                do{
//                    guard let data = data else { return }
//                    DispatchQueue.main.async {
//                    self.hideFioriLoadingIndicator()
//                    }
//                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? [String:Any]
//                    print(JSON as Any)
//                    self.parseResponse(JSON: JSON as AnyObject)
//
//                }catch{
//                    print(error.localizedDescription)
//                }
//           }
//            task.resume()
//
//        }
//    }
//    func openHierarchy(location: String, locationtype : String , navigate : String, locationText : String , selected : HierarchyItem){
//        let storyboard : UIStoryboard = UIStoryboard(name: "PMMain", bundle: nil)
//        let vc2 : PMHierarchyViewController = storyboard.instantiateViewController(withIdentifier: "PMHierarchyViewController") as! PMHierarchyViewController
//        if locationChange != nil{
//            vc2.locationChange = locationChange
//        }
//        
//        vc2.modalPresentationStyle = .fullScreen
//        vc2.location = location
//        vc2.locationtype = locationtype
//        vc2.navigate = navigate
//        vc2.locationText = locationText
//        vc2.CompleteLocation = locationText
//        vc2.currentNotification = self.currentNotification
//        vc2.workOrderDetails = self.workOrderDetails
//        vc2.searchNotification = self.searchNotification
//        vc2.searchWorkOrderData = self.searchWorkOrderData
//        vc2.parentVC = parentVC
//        switch locationtype {
//        case "Field":
//            vc2.Field = selected
//        case "Facility":
//            vc2.Field = self.Field
//            vc2.Facility = selected
//        case "Leg":
//            vc2.Field = self.Field
//            vc2.Facility = self.Facility
//            vc2.Leg = selected
//        case "Well Pad":
//            vc2.Field = self.Field
//            vc2.Facility = self.Facility
//            vc2.Leg = self.Leg
//            vc2.WellPad = selected
//            
//        default:
//            print("error in Open Hierarchy func")
//        }
//        if ConnectionCheck.isConnectedToNetwork(){
//            self.navigationController?.pushViewController(vc2, animated: true)
//        }
//        
//    }
//    
//    // Notify Create Ticket Controller about the Location Selected
//    @objc func SelectionDone(sender:UIButton!){
//        let SelectedItem = GenericArray[(SelectedIndex?.row)!] as! HierarchyItem
//        self.CompleteLocation =  SelectedItem.ItemValue1!
//        if SelectedItem.ItemType == "Well"{
//            self.Well = SelectedItem
//        }
//        switch SelectedItem.ItemType!{
//        case "Field":
//            self.Field = SelectedItem
//        case "Facility":
//            self.Facility = SelectedItem
//        case "Well Pad":
//            self.WellPad = SelectedItem
//        case "Leg":
//            self.Leg = SelectedItem
//        case "Well":
//            self.Well = SelectedItem
//        case "Meter":
//            self.Flare = SelectedItem
//        case "Compressor":
//            self.Compressor = SelectedItem
//        default:
//            print("error in selection done")
//        }
//        
//        let object : [String : AnyObject?] = ["CompleteLocation" : CompleteLocation as AnyObject,"Field" : Field! , "Facility" : Facility  , "WellPad" : WellPad, "Well" : Well, "Leg" : Leg,"Flare":Flare,"Compressor":Compressor ]
//        print(object)
//        NotificationCenter.default.post(name: .DidSelectLocation, object: object)
//        self.currentNotification?.functionalLocation = SelectedItem.ItemValue2!
//        self.currentNotification?.locationDescription = SelectedItem.ItemValue1!
//        self.searchNotification?.functionalLocation = SelectedItem.ItemValue2!
//        self.searchNotification?.flDescription = SelectedItem.ItemValue1!
//        self.searchWorkOrderData?.functionalLocation = SelectedItem.ItemValue2!
//        self.searchWorkOrderData?.flDesc = SelectedItem.ItemValue1!
//        self.workOrderDetails?.functionalLocation = SelectedItem.ItemValue2!
//        self.workOrderDetails?.funcDescription = SelectedItem.ItemValue1!
//        
//        if workOrderDetails != nil{
//            for each in (self.navigationController?.viewControllers)!{
//                if each is PMWorkOrderDetaisViewController
//                {
//                    self.navigationController?.popToViewController(each, animated: true)
//                }
//            }
//        }
//        else{
//            if parentVC is BypassListViewController {
//                
//                self.dismiss(animated: true, completion: nil)
//            }
//            if parentVC is CreateEnergyIsoViewController {
//                self.dismiss(animated: true, completion: nil)
//            } else {
//            self.navigationController?.popToRootViewController(animated: true)
//            }
//        }
//        
//    }
//}
//extension PMHierarchyViewController: UITableViewDelegate,UITableViewDataSource {
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        return GenericArray.count
//    }
//    
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = HierarchyTable.dequeueReusableCell(withIdentifier: "PMHierarchyTableViewCell") as! PMHierarchyTableViewCell
//        let CurrentItem : HierarchyItem = GenericArray[indexPath.row] as! HierarchyItem
//        let tit = GenericArray[0] as? HierarchyItem
//        self.title = tit?.ItemType ?? ""
//        cell.editingAccessoryType = .none
//        if CurrentItem.ItemType == "Well"{
//            cell.HierarchyButton.setTitle("   " + CurrentItem.ItemValue1!, for: .normal)
//        }
//            
//        else if ((parentVC is BypassListViewController) ||  parentVC is EnergyIsolationViewController) && (CurrentItem.ItemType == "Field" || CurrentItem.ItemType == "Well Pad") && (self.locationChange == "AddBypass" || self.locationChange == "AddIsolation"){
//            if currentUser.isCanadianUser,CurrentItem.ItemType == "Well Pad",parentVC is BypassListViewController{
//                self.HierarchyTable.setEditing(true, animated: true)
//            }else{
//            self.HierarchyTable.setEditing(false, animated: true)
//            }
//            cell.HierarchyArrow.isHidden = false
//            cell.HierarchyButton.setTitle(CurrentItem.ItemValue1!, for: .normal)
//        }
//        else{
//            cell.HierarchyButton.setTitle(CurrentItem.ItemValue1!, for: .normal)
//        }
//        if CurrentItem.ItemType != "Well" && CurrentItem.ItemType != "Compressor" && CurrentItem.ItemType != "Meter"{
//            //cell.editingAccessoryType = UITableViewCell.AccessoryType.disclosureIndicator
//            cell.HierarchyArrow.isHidden = false
//
//        }
//        else{
//            cell.editingAccessoryType = .none
//            cell.HierarchyArrow.isHidden = true
//        }
//       
//        cell.delegate = self
//        return cell
//    }
//    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
//        if let temp = self.SelectedIndex{
//            if temp == indexPath{
//                self.SelectedIndex = nil
//            }
//        }
//    }
//    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        let SelectedItem :HierarchyItem = GenericArray[indexPath.row] as! HierarchyItem
//        if let temp = self.SelectedIndex{
//            if temp == indexPath{
//                self.SelectedIndex = nil
//            }
//            self.HierarchyTable.deselectRow(at:temp , animated: true)
//        }
//        self.SelectedIndex = indexPath
//        if (parentVC is BypassListViewController ||  parentVC is EnergyIsolationViewController) && (SelectedItem.ItemType == "Field" || SelectedItem.ItemType == "Well Pad") && (self.locationChange == "AddBypass" || self.locationChange == "AddIsolation"){
//            if currentUser.isCanadianUser,SelectedItem.ItemType == "Well Pad",parentVC is BypassListViewController{
//                
//            }else{
//            self.SelectedIndex = nil
//            }
//        }
//        self.HierarchyTable.selectRow(at: indexPath, animated: true, scrollPosition: .none)
//    }
//}
//extension PMHierarchyViewController{
//    //    func loaderStart()
//    //    {
//    //        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
//    //        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
//    //        indicator.bounds = UIScreen.main.bounds
//    //        UIApplication.shared.keyWindow!.addSubview(indicator)
//    //        indicator.bringSubview(toFront: view)
//    //        UIApplication.shared.isNetworkActivityIndicatorVisible = true
//    //        indicator.startAnimating()
//    //    }
//    //
//    //    func loaderStop()
//    //    {
//    //        indicator.stopAnimating()
//    //    }
//
//    // MARK: - Offline Storage
//
//    //    func SaveOffline(location : String?,locationType: String?,navigate: String?,response: NSDictionary?){
//    //        //let LocationResponse: Data = NSKeyedArchiver.archivedData(withRootObject: response!)
//    //        let Locationservice = LocationService(context: context)
//    //        let predicate: NSPredicate = NSPredicate(format: "SELF.location == %@", argumentArray: [location ?? "Fields",locationType ?? "",navigate ?? ""])
//    //        let result = Locationservice.get(withPredicate: predicate)
//    //        if result.count == 0 {
//    //            _ = Locationservice.create(location:location ?? "Fields" ,locationType: locationType ?? "", navigate: navigate ?? "",response : response!)
//    //            Locationservice.saveChanges()
//    //           // print("\(location) data saved for offline")
//    //        }
//    //
//    //    }
//    //    func getSavedLocation(location : String?,locationType: String?,navigate: String?) -> NSDictionary {
//    //        print("Fetching Data from offline Store")
//    //        let predicate: NSPredicate = NSPredicate(format: "SELF.location == %@", argumentArray: [location ?? "Fields",locationType ?? "",navigate ?? ""])
//    //        let Locationservice = LocationService(context: context)
//    //        let result = Locationservice.get(withPredicate: predicate)
//    //        if result.count != 0 {
//    //        let response = result[0].value(forKey: "response")
//    //        let unarchivedResponse  = NSKeyedUnarchiver.unarchiveObject(with: response as! Data) as! AnyObject
//    //        self.parseResponse(JSON: unarchivedResponse)
//    //            return unarchivedResponse as! NSDictionary}
//    //        else {
//    //            return NSDictionary()
//    //        }
//    //    }
//
//
//
//    func parseResponse(JSON : AnyObject){
//        if let jsonDict = JSON as? NSDictionary {
//            print(jsonDict)
//            if let d = jsonDict.value(forKey: "dto") as? NSDictionary{
//                self.GenericArray.removeAll()
//                let locationHierarchy = d.value(forKey:"locationHierarchy") as! NSArray
//                for each in locationHierarchy{
//                    let item : GenericItem = HierarchyItem(item: each as! NSDictionary)
//                    if locationSingle.shared.locationValue == "Location History"{
//                        self.GenericArray.append(item)
//                    }
//                    else{
//                    if (each as! NSDictionary).value(forKey: "locationType") as? String != "Compressor"{
//                        self.GenericArray.append(item)
//                    }
//                    }
//                }
//            }
//        }
//        if self.GenericArray.count == 0 {
//            DispatchQueue.main.async {
//
//            let alert = UIAlertController(title: "", message: "No Data Found", preferredStyle: UIAlertController.Style.alert)
//            let okAction = UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: {
//                _ in self.navigationController?.popViewController(animated: true)
//            })
//            alert.addAction(okAction)
//            self.present(alert, animated: true)
//            }
//        }
//        else{
//            DispatchQueue.main.async {
//                self.HierarchyTable.reloadData()
//            }
//        }
//    }
//}
//extension PMHierarchyViewController: HierarchyTableViewCellDelegate{
//    func didButtonTapped(cell: PMHierarchyTableViewCell) {
//        let indexPath = HierarchyTable.indexPath(for: cell)
//        let SelectedItem : HierarchyItem = GenericArray[(indexPath?.row)!] as! HierarchyItem
//        if SelectedItem.ItemType != "Well" &&  SelectedItem.ItemType != "Meter" && SelectedItem.ItemType != "Compressor"{
////            if locationSingle.shared.locationValue == "Location History" && SelectedItem.ItemType == "Compressor"{
//            self.openHierarchy(location: SelectedItem.ItemValue2!,locationtype : SelectedItem.ItemType!,navigate :"CHILD",locationText: SelectedItem.ItemValue1!, selected : SelectedItem)
//           // }
//        }
//        else if SelectedItem.ItemType == "Well"{
//            self.Well = SelectedItem
//
//            let object : [String : AnyObject?] = ["CompleteLocation" : CompleteLocation as AnyObject,"Field" : Field , "Facility" : Facility, "WellPad" : WellPad , "Well" : Well , "Leg" : Leg]
//            print(object)
//            self.CompleteLocation =  SelectedItem.ItemValue1!
//
//            NotificationCenter.default.post(name: .DidSelectLocation, object: object)
//            self.currentNotification?.functionalLocation = SelectedItem.ItemValue2 ?? ""
//            self.currentNotification?.locationDescription = SelectedItem.ItemValue1 ?? ""
//
//            self.searchNotification?.functionalLocation = SelectedItem.ItemValue2 ?? ""
//            self.searchNotification?.flDescription = SelectedItem.ItemValue1 ?? ""
//
//            self.searchWorkOrderData?.functionalLocation = SelectedItem.ItemValue2 ?? ""
//            self.searchWorkOrderData?.flDesc = SelectedItem.ItemValue1 ?? ""
//
//            self.workOrderDetails?.functionalLocation = SelectedItem.ItemValue2!
//            self.workOrderDetails?.funcDescription = SelectedItem.ItemValue1!
//
//            if workOrderDetails != nil{
//                for each in (self.navigationController?.viewControllers)!{
//                    if each is PMWorkOrderDetaisViewController
//                    {
//                        self.navigationController?.popToViewController(each, animated: true)
//                    }
//                }
//            }
//            else{
//                if parentVC is BypassListViewController {
//                    self.dismiss(animated: true, completion: nil)
//                }
//                self.navigationController?.popToRootViewController(animated: true)
//            }
//
//        }
//    }
//}

extension Notification.Name {
    
    static let DidSelectLocation = Notification.Name(rawValue: "didSelectLocation")
}
