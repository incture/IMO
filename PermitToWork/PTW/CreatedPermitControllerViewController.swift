//
//  CreatedPermitControllerViewController.swift
//  
//
//  Created by Soumya Singh on 10/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit
import CoreData

class CreatedPermitControllerViewController: UIViewController, UISearchBarDelegate {
    
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var locationText: UIBarButtonItem!
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var tableView: UITableView!
    @IBOutlet var homeButton: UIButton!
    @IBOutlet var JSAButton: UIButton!
    @IBOutlet var HWbutton: UIButton!
    @IBOutlet var CWButton: UIButton!
    @IBOutlet var CSEButton: UIButton!
    @IBOutlet var flowStart: UIButton!
    var refreshControl: UIRefreshControl!
    var emptyLabel = UILabel()
    var locationStr = ""
    //var senderController : TaskListController?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    let context = PTWCoreData.shared.managedObjectContext
    
    @IBOutlet weak var locationBtn: UIBarButtonItem!
    var status : Int = 0
    
    
    var listData = [JSAList]()
    var CWList = [PermitList]()
    var HWList = [PermitList]()
    var CSEList = [PermitList]()
    
    
    var isFilteringJSA : Bool = false
    var isFilteringCW : Bool = false
    var isFilteringHW : Bool = false
    var isFilteringCSE : Bool = false
    
    // People
    var location = [String]()
    var searchLocation = [String]()
    var peopleListArr = [[People]]()
    var allPeople = [People]()
    var searchPeople = [People]()
    var searchPeopleList = [[People]]()
    
    // To datewise segregate JSA
    var sectionData = [String]()
    var finalArray = [[JSAList]]()
    var searchJSAList = [JSAList]()
    var searchSectionData = [String]()
    var searchSegregatedData = [[JSAList]]()
    
    // To filter
    var filterJSAList = [JSAList]()
    var filterSectionData = [String]()
    var filterSegregatedData = [[JSAList]]()
    
    
    
    
    // Cold Work
    var cwpSectionData = [String]()
    var cwpFinalArray = [[PermitList]]()
    var searchCWList = [PermitList]()
    var searchCWSectionData = [String]()
    var searchSegregatedCWData = [[PermitList]]()
    
    var filterCWList = [PermitList]()
    var filterCWSectionData = [String]()
    var filterCWSegregatedData = [[PermitList]]()
    
    
    // Hot Work
    var hwpSectionData = [String]()
    var hwpFinalArray = [[PermitList]]()
    var searchHWList = [PermitList]()
    var searchHWSectionData = [String]()
    var searchSegregatedHWData = [[PermitList]]()
    
    var filterHWList = [PermitList]()
    var filterHWSectionData = [String]()
    var filterHWSegregatedData = [[PermitList]]()
    
    // Confined Space
    var cseSectionData = [String]()
    var cseFinalArray = [[PermitList]]()
    var searchCSEList = [PermitList]()
    var searchCSESectionData = [String]()
    var searchSegregatedCSEData = [[PermitList]]()
    
    var filterCSEList = [PermitList]()
    var filterCSESectionData = [String]()
    var filterCSESegregatedData = [[PermitList]]()
    
    var selectedJSA = ""
    var selectedPermit = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let Pnib = UINib(nibName: "PermitCardCell", bundle: nil)
        tableView.register(Pnib, forCellReuseIdentifier: "PermitCardCell")
        let Jnib = UINib(nibName: "JSACardCell", bundle: nil)
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        //refreshControl.addTarget(self, action: #selector(TaskListController.refresh), for: .valueChanged)
        tableView.addSubview(refreshControl)
        tableView.register(Jnib, forCellReuseIdentifier: "JSACardCell")
        tableView.backgroundColor = UIColor.groupTableViewBackground
        tableView.separatorStyle = .none
        tableView.tableFooterView = UIView()
        searchBar.delegate = self
        flowStart.layer.cornerRadius = 25.0
        flowStart.layer.masksToBounds = true
        self.navigationController?.navigationBar.isHidden = true
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool)
    {
        UserDefaults.standard.set("false", forKey: "JSAPreview")
        UserDefaults.standard.synchronize()
        
        isFilteringJSA = false
        if currentLocation.facilityOrSite == "" {
            
            let alert = UIAlertController(title: nil, message: "Please select a location!", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: { action in
                let vc = UIStoryboard(name: "start", bundle: Bundle.main).instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
                isFromCreateJSA = false
                self.navigationController?.pushViewController(vc, animated: true)
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
            
        }
        else{
            
            if currentLocation.muwi != "" || currentLocation.facility != ""
            {
                self.reloadLocation()
            }
            locationText.title = currentLocation.facilityOrSite
        }
        
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if #available(iOS 13.0, *) {
            if let searchTextField = self.searchBar.value(forKey: "searchField") as? UITextField {
                //searchTextField.textColor = .black
                // searchTextField.backgroundColor = .white
            }
        }
        locationBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        // btnTwoLine?.titleLabel?.lineBreakMode = NSLineBreakMode.ByWordWrapping;
    }
    
    @objc func refresh() {
        self.refreshControl?.endRefreshing()
        reloadLocation()
    }
    
    func reloadLocation()
    {
        
        if currentLocation.muwi != ""
        {
            
            // locationStr = "\(BaseUrl.apiURL)/com.iop.ptw/getListofActive_Worker.xsjs?muwi=\(currentLocation.muwi)&facility=\(currentLocation.facilityOrSite)"
            locationStr = IMOEndpoints.getListOfActiveWorkers+"muwi=\(currentLocation.muwi)&facility=\(currentLocation.facilityOrSite)"
        }
        else
        {
            //locationStr = "\(BaseUrl.apiURL)/com.iop.ptw/getListofActive_Worker.xsjs?facility=\(currentLocation.facilityOrSite)"
            locationStr = IMOEndpoints.getListOfActiveWorkers+"facility=\(currentLocation.facilityOrSite)"
        }
        if ConnectionCheck.isConnectedToNetwork(){
            getPeopleListData()
            getJSAList()
            getPermitList()
        }
        else{
            getOfflinePeopleListData()
            getOfflineJSAList()
            getOfflinePermitList()
        }
    }
    
    
    @IBAction func onBackPress(_ sender: UIBarButtonItem) {
        self.navigationController?.navigationBar.isHidden = false
        self.navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func onHomePress(_ sender: UIButton) {
        self.navigationTitle.title = "Home"
        locationText.tintColor = UIColor.white
        status = 0
        flowStart.isHidden = true
        locationBtn.isEnabled = true
        locationBtn.image = UIImage(named : "location")
        locationBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        self.tableView.reloadData()
        setColours(button: sender)
    }
    
    @IBAction func onJSAPress(_ sender: UIButton) {
        self.navigationTitle.title = "Job Safety Analysis"
        //        getJSAList()
        //        getPermitList()
        status = 1
        flowStart.isHidden = false
        locationBtn.image = UIImage(named : "filter")
        locationText.tintColor = UIColor.clear
        locationBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
        setColours(button: sender)
        if currentUser.isReadOnly == true{
            flowStart.isHidden = true
        }
    }
    
    @IBAction func onHWPress(_ sender: UIButton) {
        self.navigationTitle.title = "Hot Work"
        status = 2
        flowStart.isHidden = true
        locationBtn.image = UIImage(named : "filter")
        locationText.tintColor = UIColor.clear
        locationBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
        setColours(button: sender)
        if currentUser.isReadOnly == true{
            flowStart.isHidden = true
        }
    }
    
    @IBAction func onCWPress(_ sender: UIButton) {
        self.navigationTitle.title = "Cold Work"
        status = 3
        flowStart.isHidden = true
        locationBtn.image = UIImage(named : "filter")
        locationText.tintColor = UIColor.clear
        locationBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
        setColours(button: sender)
        if currentUser.isReadOnly == true{
            flowStart.isHidden = true
        }
    }
    
    @IBAction func onCSEPress(_ sender: UIButton) {
        self.navigationTitle.title = "Confined Space Entry"
        status = 4
        flowStart.isHidden = true
        locationBtn.image = UIImage(named : "filter")
        locationText.tintColor = UIColor.clear
        locationBtn.tintColor = UIColor.white.withAlphaComponent(1.0)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
        setColours(button: sender)
    }
    
    @IBAction func onLocationPress(_ sender: Any)
    {
        if status == 0{
            let vc = UIStoryboard(name: "start", bundle: Bundle.main).instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
            isFromCreateJSA = false
            self.navigationController?.pushViewController(vc, animated: true)
        }
        else {
            let alert = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
            alert.addAction(UIAlertAction(title: "Submitted", style: .default , handler:{ (UIAlertAction)in
                if self.status == 1{
                    self.isFilteringJSA = true
                }
                else if self.status == 2{
                    self.isFilteringHW = true
                }
                else if self.status == 3{
                    self.isFilteringCW = true
                }
                else if self.status == 4{
                    self.isFilteringCSE = true
                }
                self.filter(value: "submitted")
            }))
            
            alert.addAction(UIAlertAction(title: "Approved", style: .default , handler:{ (UIAlertAction)in
                if self.status == 1{
                    self.isFilteringJSA = true
                }
                else if self.status == 2{
                    self.isFilteringHW = true
                }
                else if self.status == 3{
                    self.isFilteringCW = true
                }
                else if self.status == 4{
                    self.isFilteringCSE = true
                }
                self.filter(value: "approved")
            }))
            
            if status == 2 || status == 3 || status == 4{
                alert.addAction(UIAlertAction(title: "Closed", style: .default , handler:{ (UIAlertAction)in
                    if self.status == 1{
                        self.isFilteringJSA = false
                    }
                    else if self.status == 2{
                        self.isFilteringHW = true
                    }
                    else if self.status == 3{
                        self.isFilteringCW = true
                    }
                    else if self.status == 4{
                        self.isFilteringCSE = true
                    }
                    self.filter(value: "closed")
                }))
            }
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                
                //
                if self.status == 1{
                    self.isFilteringJSA = false
                    self.filterJSAList.removeAll()
                    self.filterSectionData.removeAll()
                    self.filterSegregatedData.removeAll()
                }
                else if self.status == 2{
                    self.isFilteringHW = false
                    self.filterHWList.removeAll()
                    self.filterHWSectionData.removeAll()
                    self.filterHWSegregatedData.removeAll()
                }
                else if self.status == 3{
                    self.isFilteringCW = false
                    self.filterCWList.removeAll()
                    self.filterCWSectionData.removeAll()
                    self.filterCWSegregatedData.removeAll()
                }
                else if self.status == 4{
                    self.isFilteringCSE = false
                    self.filterCSEList.removeAll()
                    self.filterCSESectionData.removeAll()
                    self.filterCSESegregatedData.removeAll()
                }
                self.tableView.reloadData()
                print("User click Dismiss button")
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
    }
    
    @IBAction func onPlusPress(_ sender: UIButton) {
        
        // plus button
        
        UserDefaults.standard.set("false", forKey: "JSAPreview")
        UserDefaults.standard.set("false", forKey: "JSAApproved")
        UserDefaults.standard.set("true", forKey: "JSACreate")
        UserDefaults.standard.set("true", forKey: "CurrentUser")
        UserDefaults.standard.synchronize()
        JSAObject = JSA()
        JSAObject.currentFlow = .JSA
        
        //delete userdefaults for jsa local values
        
        UserDefaults.standard.removeObject(forKey: "RiskAssesmentViewController")
        UserDefaults.standard.removeObject(forKey: "RiskAssesmentViewControllerTable")
        UserDefaults.standard.removeObject(forKey: "RiskAssesmentViewControllerSwitch")
        UserDefaults.standard.removeObject(forKey: "HazardVCViewController")
        UserDefaults.standard.removeObject(forKey: "PotentialHazardVC")
        UserDefaults.standard.removeObject(forKey: "SavedStringJobArray")
        UserDefaults.standard.removeObject(forKey: "ProposedPeopleJSA")
        UserDefaults.standard.removeObject(forKey: "AddedPeopleJSA")
        UserDefaults.standard.synchronize()
        
        
        
        
        let alert = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        alert.addAction(UIAlertAction(title: "Create template", style: .default , handler:{ (UIAlertAction)in
            creatingTemplate = true
            if #available(iOS 10.0, *) {
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreateJSAVC") as! CreateJSAVC
                self.modalPresentationStyle = .overFullScreen
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                //            let navigationController = UINavigationController(rootViewController : dashBoardVC)
                //            self.present(navigationController, animated: false, completion: nil)
            } else {
                // Fallback on earlier versions
            }
        }))
        
        alert.addAction(UIAlertAction(title: "Create JSA from template", style: .default , handler:{ (UIAlertAction)in
            let dashBoardVC = Storyboard.initialBoard.instantiateViewController(withIdentifier: "JSATemplateListViewController") as! JSATemplateListViewController
            self.modalPresentationStyle = .overFullScreen
            self.navigationController?.pushViewController(dashBoardVC, animated: true)
        }))
        alert.addAction(UIAlertAction(title: "Create new JSA", style: .default , handler:{ (UIAlertAction)in
            if #available(iOS 10.0, *) {
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreateJSAVC") as! CreateJSAVC
                self.modalPresentationStyle = .overFullScreen
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                //            let navigationController = UINavigationController(rootViewController : dashBoardVC)
                //            self.present(navigationController, animated: false, completion: nil)
            } else {
                // Fallback on earlier versions
            }
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
            
            print("User click Dismiss button")
        }))
        
        self.present(alert, animated: true, completion: {
            print("completion block")
        })
        
        
        
        
        /*
         if #available(iOS 10.0, *) {
         let dashBoardVC = Storyboard.initialBoard.instantiateViewController(withIdentifier: "JSACreateOptionsViewController") as! JSACreateOptionsViewController
         self.modalPresentationStyle = .overFullScreen
         self.navigationController?.pushViewController(dashBoardVC, animated: true)
         //            let navigationController = UINavigationController(rootViewController : dashBoardVC)
         //            self.present(navigationController, animated: false, completion: nil)
         } else {
         // Fallback on earlier versions
         }*/
    }
    
    
    func setColours(button : UIButton){
        homeButton.setTitleColor(UIColor.darkGray, for: .normal)
        JSAButton.setTitleColor(UIColor.darkGray, for: .normal)
        HWbutton.setTitleColor(UIColor.darkGray, for: .normal)
        CWButton.setTitleColor(UIColor.darkGray, for: .normal)
        CSEButton.setTitleColor(UIColor.darkGray, for: .normal)
        // button.setTitleColor(UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0), for: .normal)
        button.setTitleColor(UIColor(named: "PlusBtnColor"), for: .normal)
        
    }
    // MARK: - Loader initializations
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        //   UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        DispatchQueue.main.async {
            self.indicator.stopAnimating()
        }
        
    }
    
    // MARK: - Searchbar Delegate
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        //searchBar.showsCancelButton = true
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchBar.setShowsCancelButton(false, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        onSearch(searchText: searchBar.text!)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        onSearch(searchText: searchBar.text!)
        searchBar.resignFirstResponder()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar){
        searchBar.resignFirstResponder()
        searchBar.text = ""
        //  listTable.reloadData()
    }
    
    // MARK: - Filter on static data
    func filter(value : String){
        
        
        if status == 1{
            filterJSAList.removeAll()
            filterSectionData.removeAll()
            filterSegregatedData.removeAll()
            for jsa in listData{
                if jsa.status.lowercased() == value{
                    filterJSAList.append(jsa)
                }
            }
            
            for val in filterJSAList
            {
                var dateStr = val.lastUpdatedDate
                if dateStr == ""
                {
                    dateStr = val.createdDate
                }
                filterSectionData.append(dateStr)
            }
            var tempArr = [String]()
            for val in filterSectionData
            {
                if !tempArr.contains(val)
                {
                    tempArr.append(val)
                }
            }
            filterSectionData = tempArr
            
            for val in filterSectionData
            {
                var arr = [JSAList]()
                for val1 in filterJSAList
                {
                    if val1.lastUpdatedDate.contains(val)
                    {
                        arr.append(val1)
                    }
                }
                filterSegregatedData.append(arr)
            }
            print(filterSegregatedData)
            
        }
        else if status == 2{
            
            filterHWList.removeAll()
            filterHWSectionData.removeAll()
            filterHWSegregatedData.removeAll()
            for permit in HWList{
                if permit.status.lowercased() == value{
                    filterHWList.append(permit)
                }
            }
            
            for val in filterHWList
            {
                var dateStr = val.lastUpdatedDate
                if dateStr == ""
                {
                    dateStr = val.createdDate
                }
                filterHWSectionData.append(dateStr)
            }
            var tempArr = [String]()
            for val in filterHWSectionData
            {
                if !tempArr.contains(val)
                {
                    tempArr.append(val)
                }
            }
            filterHWSectionData = tempArr
            
            for val in filterHWSectionData
            {
                var arr = [PermitList]()
                for val1 in filterHWList
                {
                    if val1.lastUpdatedDate.contains(val)
                    {
                        arr.append(val1)
                    }
                }
                filterHWSegregatedData.append(arr)
            }
            print(filterHWSegregatedData)
            
        }
        else if status == 3{
            
            
            filterCWList.removeAll()
            filterCWSectionData.removeAll()
            filterCWSegregatedData.removeAll()
            for permit in CWList{
                if permit.status.lowercased() == value{
                    filterCWList.append(permit)
                }
            }
            
            for val in filterCWList
            {
                var dateStr = val.lastUpdatedDate
                if dateStr == ""
                {
                    dateStr = val.createdDate
                }
                filterCWSectionData.append(dateStr)
            }
            var tempArr = [String]()
            for val in filterCWSectionData
            {
                if !tempArr.contains(val)
                {
                    tempArr.append(val)
                }
            }
            filterCWSectionData = tempArr
            
            for val in filterCWSectionData
            {
                var arr = [PermitList]()
                for val1 in filterCWList
                {
                    if val1.lastUpdatedDate.contains(val)
                    {
                        arr.append(val1)
                    }
                }
                filterCWSegregatedData.append(arr)
            }
            print(filterCWSegregatedData)
            
        }
        else if status == 4{
            filterCSEList.removeAll()
            filterCSESectionData.removeAll()
            filterCSESegregatedData.removeAll()
            for permit in CSEList{
                if permit.status.lowercased() == value{
                    filterCSEList.append(permit)
                }
            }
            
            for val in filterCSEList
            {
                var dateStr = val.lastUpdatedDate
                if dateStr == ""
                {
                    dateStr = val.createdDate
                }
                filterCSESectionData.append(dateStr)
            }
            var tempArr = [String]()
            for val in filterCSESectionData
            {
                if !tempArr.contains(val)
                {
                    tempArr.append(val)
                }
            }
            filterCSESectionData = tempArr
            
            for val in filterCSESectionData
            {
                var arr = [PermitList]()
                for val1 in filterCSEList
                {
                    if val1.lastUpdatedDate.contains(val)
                    {
                        arr.append(val1)
                    }
                }
                filterCSESegregatedData.append(arr)
            }
            print(filterCSESegregatedData)
        }
        tableView.reloadData()
    }
    
    
    // // MARK: - Search on static data
    func onSearch(searchText : String)
    {
        if status == 0{
            let searchPredicate = NSPredicate(format: "SELF.fullName CONTAINS[c] %@" , searchText)
            let array = (allPeople as NSArray).filtered(using: searchPredicate)
            self.searchPeople = array as! [People]
            segregateSearchPeople()
            self.tableView.reloadData()
        }
        else if status == 1{
            let searchPredicate = NSPredicate(format: "SELF.permitNumber CONTAINS[c] %@" , searchText)
            let array = (listData as NSArray).filtered(using: searchPredicate)
            self.searchJSAList = array as! [JSAList]
            segregateJSAForSearch()
        }
        else if status == 2{
            let searchPredicate = NSPredicate(format: "SELF.permitNumber CONTAINS[c] %@" , searchText)
            let array = (HWList as NSArray).filtered(using: searchPredicate)
            self.searchHWList = array as! [PermitList]
            segregateHWForSearch()
            
        }
        else if status == 3{
            let searchPredicate = NSPredicate(format: "SELF.permitNumber CONTAINS[c] %@" , searchText)
            let array = (CWList as NSArray).filtered(using: searchPredicate)
            self.searchCWList = array as! [PermitList]
            segregateCWForSearch()
            
        }
        else if status == 4{
            let searchPredicate = NSPredicate(format: "SELF.permitNumber CONTAINS[c] %@" , searchText)
            let array = (CSEList as NSArray).filtered(using: searchPredicate)
            self.searchCSEList = array as! [PermitList]
            segregateCSEForSearch()
        }
    }
    
    
    func segregateSearchPeople(){
        
        searchLocation.removeAll()
        searchPeopleList.removeAll()
        for each in self.searchPeople{
            searchLocation.append(each.location)
        }
        var tempArr = [String]()
        for val in searchLocation
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        searchLocation = tempArr
        for val in searchLocation
        {
            var arr = [People]()
            for val1 in searchPeople
            {
                if val1.location.contains(val)
                {
                    arr.append(val1)
                }
            }
            searchPeopleList.append(arr)
        }
        print(searchPeopleList)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
    func segregateCSEForSearch()
    {
        searchCSESectionData.removeAll()
        searchSegregatedCSEData.removeAll()
        for val in searchCSEList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            
            searchCSESectionData.append(dateStr)
            
        }
        var tempArr = [String]()
        for val in searchCSESectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        searchCSESectionData = tempArr
        
        for val in searchCSESectionData
        {
            var arr = [PermitList]()
            for val1 in searchCSEList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            searchSegregatedCSEData.append(arr)
        }
        print(searchSegregatedCSEData)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
    func segregateHWForSearch()
    {
        searchHWSectionData.removeAll()
        searchSegregatedHWData.removeAll()
        for val in searchHWList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            
            searchHWSectionData.append(dateStr)
            
        }
        var tempArr = [String]()
        for val in searchSectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        searchHWSectionData = tempArr
        
        for val in searchHWSectionData
        {
            var arr = [PermitList]()
            for val1 in searchHWList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            searchSegregatedHWData.append(arr)
        }
        print(searchSegregatedHWData)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
    
    
    func segregateCWForSearch()
    {
        searchCWSectionData.removeAll()
        searchSegregatedCWData.removeAll()
        for val in searchCWList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            
            searchCWSectionData.append(dateStr)
            
        }
        var tempArr = [String]()
        for val in searchCWSectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        searchCWSectionData = tempArr
        
        for val in searchCWSectionData
        {
            var arr = [PermitList]()
            for val1 in searchCWList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            searchSegregatedCWData.append(arr)
        }
        print(searchSegregatedCWData)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
    func segregateJSAForSearch()
    {
        
        searchSectionData.removeAll()
        searchSegregatedData.removeAll()
        for val in searchJSAList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            // let date = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .monthDateYearLong, shouldConvertFromUTC: true)
            searchSectionData.append(dateStr)
            //   val.lastUpdatedDate = dateStr//.convertToDateString(format: .monthDateYearLong, currentDateStringFormat: .long, shouldConvertFromUTC: true)!
        }
        var tempArr = [String]()
        for val in searchSectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        searchSectionData = tempArr
        
        for val in searchSectionData
        {
            var arr = [JSAList]()
            for val1 in searchJSAList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            searchSegregatedData.append(arr)
        }
        print(searchSegregatedData)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
    
    func parseJSAdata()
    {
        
        sectionData.removeAll()
        finalArray.removeAll()
        for val in listData
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            val.longFormatDate = dateStr
            let date = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)
            sectionData.append(date ?? "")
            
            val.lastUpdatedDate = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true) ?? ""
        }
        
        
        if !ConnectionCheck.isConnectedToNetwork(){
            var convertedArray = [Date]()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd, MMM yyyy"// yyyy-MM-dd"
            
            for val in listData {
                let date = dateFormatter.date(from: val.lastUpdatedDate)
                if let date = date {
                    convertedArray.append(date)
                }
            }
            let sortedDate = convertedArray.sorted(by: { $0.compare($1) == .orderedDescending })
            let tempListData = listData.sorted(by: { ($0.longFormatDate).compare($1.longFormatDate) == .orderedDescending })
            listData.removeAll()
            listData.append(contentsOf: tempListData)
            sectionData.removeAll()
            for each in sortedDate{
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "dd, MMM yyyy"
                let date = dateFormatter.string(from: each)
                sectionData.append(date)
            }
            
        }
        
        var tempArr = [String]()
        for val in sectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        
        sectionData = tempArr
        
        for val in sectionData
        {
            var arr = [JSAList]()
            for val1 in listData
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            finalArray.append(arr)
        }
        
        print(finalArray)
       
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
        
    }
    
    func parsePermitData()
    {
        cwpSectionData.removeAll()
        cwpFinalArray.removeAll()
        hwpSectionData.removeAll()
        hwpFinalArray.removeAll()
        cseSectionData.removeAll()
        cseFinalArray.removeAll()
        
        //Cold Work
        for val in CWList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            val.longFormatDate = dateStr
            let date = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)
            if date == nil{
                cwpSectionData.append(dateStr)
            }
            else{
                cwpSectionData.append(date!)
                val.lastUpdatedDate = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)!
            }
            
        }
        
        if !ConnectionCheck.isConnectedToNetwork(){
            var convertedArray = [Date]()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd, MMM yyyy"// yyyy-MM-dd"
            
            for val in CWList {
                let date = dateFormatter.date(from: val.lastUpdatedDate)
                if let date = date {
                    convertedArray.append(date)
                }
            }
            let sortedDate = convertedArray.sorted(by: { $0.compare($1) == .orderedDescending })
            let tempListData = CWList.sorted(by: { ($0.longFormatDate).compare($1.longFormatDate) == .orderedDescending })
            CWList.removeAll()
            CWList.append(contentsOf: tempListData)
            cwpSectionData.removeAll()
            for each in sortedDate{
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "dd, MMM yyyy"
                let date = dateFormatter.string(from: each)
                cwpSectionData.append(date)
            }
            
        }
        
        var tempArr = [String]()
        for val in cwpSectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        cwpSectionData = tempArr
        
        for val in cwpSectionData
        {
            var arr = [PermitList]()
            for val1 in CWList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            cwpFinalArray.append(arr)
        }
        print(cwpFinalArray)
        
        
        //Hot Work
        for val in HWList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            val.longFormatDate = dateStr
            let date = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)
            if date == nil{
                hwpSectionData.append(dateStr)
            }
            else{
                hwpSectionData.append(date!)
                val.lastUpdatedDate = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)!
            }
            
        }
        
        if !ConnectionCheck.isConnectedToNetwork(){
            var convertedArray = [Date]()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd, MMM yyyy"// yyyy-MM-dd"
            
            for val in HWList {
                let date = dateFormatter.date(from: val.lastUpdatedDate)
                if let date = date {
                    convertedArray.append(date)
                }
            }
            let sortedDate = convertedArray.sorted(by: { $0.compare($1) == .orderedDescending })
            let tempListData = HWList.sorted(by: { ($0.longFormatDate).compare($1.longFormatDate) == .orderedDescending })
            HWList.removeAll()
            HWList.append(contentsOf: tempListData)
            hwpSectionData.removeAll()
            for each in sortedDate{
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "dd, MMM yyyy"
                let date = dateFormatter.string(from: each)
                hwpSectionData.append(date)
            }
            
        }
        
        tempArr = [String]()
        for val in hwpSectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        hwpSectionData = tempArr
        
        for val in hwpSectionData
        {
            var arr = [PermitList]()
            for val1 in HWList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            hwpFinalArray.append(arr)
        }
        print(hwpFinalArray)
        
        //CSE Work
        for val in CSEList
        {
            var dateStr = val.lastUpdatedDate
            if dateStr == ""
            {
                dateStr = val.createdDate
            }
            val.longFormatDate = dateStr
            let date = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)
            if date == nil{
                cseSectionData.append(dateStr)
            }
            else{
                cseSectionData.append(date!)
                val.lastUpdatedDate = dateStr.convertToDateString(format: .dayMonthYear, currentDateStringFormat: .long, shouldConvertFromUTC: true)!
            }
            
        }
        
        if !ConnectionCheck.isConnectedToNetwork(){
            var convertedArray = [Date]()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd, MMM yyyy"// yyyy-MM-dd"
            
            for val in CSEList {
                let date = dateFormatter.date(from: val.lastUpdatedDate)
                if let date = date {
                    convertedArray.append(date)
                }
            }
            let sortedDate = convertedArray.sorted(by: { $0.compare($1) == .orderedDescending })
            let tempListData = CSEList.sorted(by: { ($0.longFormatDate).compare($1.longFormatDate) == .orderedDescending })
            CSEList.removeAll()
            CSEList.append(contentsOf: tempListData)
            cseSectionData.removeAll()
            for each in sortedDate{
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "dd, MMM yyyy"
                let date = dateFormatter.string(from: each)
                cseSectionData.append(date)
            }
            
        }
        
        tempArr = [String]()
        for val in cseSectionData
        {
            if !tempArr.contains(val)
            {
                tempArr.append(val)
            }
        }
        cseSectionData = tempArr
        
        for val in cseSectionData
        {
            var arr = [PermitList]()
            for val1 in CSEList
            {
                if val1.lastUpdatedDate.contains(val)
                {
                    arr.append(val1)
                }
            }
            cseFinalArray.append(arr)
        }
        print(cseFinalArray)
        
        self.tableView.reloadData()
    }
    
    // MARK: - People List API
    func getOfflinePeopleListData()
    {
        let peopleListService = PeopleListService(context: self.context)
        var facilityOrSiteValue : String?
        self.peopleListArr.removeAll()
        self.location.removeAll()
        if currentLocation.hierarchyLevel == "facility"{
            facilityOrSiteValue = currentLocation.facilityOrSite
        }
        else{
            facilityOrSiteValue = currentLocation.muwi
        }
        let searchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue!)
        let peopleList = peopleListService.get(withPredicate: searchPredicate)
        if peopleList.count > 0{
            let peopleListValue = peopleList[0].getPeopleList()
            self.peopleListArr = peopleListValue.peopleArray
            self.location = peopleListValue.locationArray
            DispatchQueue.main.async {
                self.tableView.reloadData()
            }
        }
        
    }
    
    
    //getting response
    func getPeopleListData(){
        
        DispatchQueue.main.async {
            self.loaderStart()
        }
        let encodedUrl = locationStr.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl ?? "")!)
        urlRequest.httpMethod = "get"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    if let jsonDict = JSON as? NSDictionary {
                        let jsonArr = jsonDict.value(forKey: "data") as? NSArray ?? NSArray()
                        print("jsonArr",jsonArr)
                        self.peopleListArr.removeAll()
                        self.location.removeAll()
                        self.allPeople.removeAll()
                        
                        if jsonArr.count == 0
                        {
                            self.loaderStop()
                        }else{
                            DispatchQueue.main.async {
                                let peopleListService = PeopleListService(context: self.context)
                                var facilityOrSiteValue : String?
                                if currentLocation.hierarchyLevel == "facility"{
                                    facilityOrSiteValue = currentLocation.facilityOrSite
                                }
                                else{
                                    facilityOrSiteValue = currentLocation.muwi
                                }
                                let searchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue!)
                                let peopleList = peopleListService.get(withPredicate: searchPredicate)
                                if peopleList.count > 0{
                                    for each in peopleList{
                                        peopleListService.delete(id: each.objectID)
                                    }
                                }
                                for each in jsonArr{
                                    if let dtoDict = each as? NSDictionary{
                                        var peopleList = [People]()
                                        if let tempArr = dtoDict["ptwPeopleList"] as? [NSDictionary] {
                                            for arr in tempArr {
                                                let people = People(JSON : arr as NSDictionary)
                                                let firstName = arr["firstName"] as? String ?? ""
                                                let lastName = arr["lastName"]! as? String ?? ""
                                                people.fullName = firstName + " " + lastName
                                                peopleList.append(people)
                                            }
                                           
                                            if let location = dtoDict["facilityOrSite"] as? String{
                                                for each in peopleList{
                                                    each.location = location
                                                    self.allPeople.append(each)
                                                }
                                                self.peopleListArr.append(peopleList)
                                                self.location.append(location)
                                            }
                                        }
                                        
                                    }
                                }
                                let peopleAddedList = PeopleAddedList()
                                peopleAddedList.locationArray = self.location
                                peopleAddedList.peopleArray = self.peopleListArr
                                if currentLocation.hierarchyLevel == "facility"{
                                    facilityOrSiteValue = currentLocation.facilityOrSite
                                }
                                else{
                                    facilityOrSiteValue = currentLocation.muwi
                                }
                                
                                print("locationCentralArr",self.peopleListArr)
                                _ = peopleListService.create(peopleList: peopleAddedList, facilityOrSite: facilityOrSiteValue!)
                                peopleListService.saveChanges()
                                self.tableView.reloadData()
                                self.loaderStop()
                            }
                        }
                    }
                    
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    self.loaderStop()
                    DispatchQueue.main.async {
                        let message = error!.localizedDescription
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                    }
                    
                }
        }.resume()
    }
}
// MARK: - TableView Extension

extension CreatedPermitControllerViewController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        if status == 0{
            if searchBar.text == ""{
                if location.count == 0{
                    self.emptyLabel.text = "No Data"
                    self.emptyLabel.textColor = UIColor.gray
                    self.emptyLabel.textAlignment = NSTextAlignment.center
                    self.tableView.backgroundView = self.emptyLabel
                }
                else{
                    self.emptyLabel.text = ""
                }
                return self.location.count
            }
            else{
                if searchLocation.count == 0{
                    self.emptyLabel.text = "No Data"
                    self.emptyLabel.textColor = UIColor.gray
                    self.emptyLabel.textAlignment = NSTextAlignment.center
                    self.tableView.backgroundView = self.emptyLabel
                }
                else{
                    self.emptyLabel.text = ""
                }
                return searchLocation.count
            }
        }
        else if status == 1
        {
            if searchBar.text == ""{
                
                if isFilteringJSA == true{
                    if filterSectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    return filterSectionData.count
                    
                }
                else{
                    
                    if sectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    
                    return sectionData.count
                }
            }
            else{
                
                if searchSectionData.count == 0{
                    self.emptyLabel.text = "No Data"
                    self.emptyLabel.textColor = UIColor.gray
                    self.emptyLabel.textAlignment = NSTextAlignment.center
                    self.tableView.backgroundView = self.emptyLabel
                }
                else{
                    self.emptyLabel.text = ""
                }
                return searchSectionData.count
            }
        }
        else if status == 2
        {
            if searchBar.text == ""{
                if isFilteringHW == true{
                    if filterHWSectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    return filterHWSectionData.count
                    
                }
                else{
                    
                    if hwpSectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    
                    return hwpSectionData.count
                }
            }
            else{
                if searchHWSectionData.count == 0{
                    self.emptyLabel.text = "No Data"
                    self.emptyLabel.textColor = UIColor.gray
                    self.emptyLabel.textAlignment = NSTextAlignment.center
                    self.tableView.backgroundView = self.emptyLabel
                }
                else{
                    self.emptyLabel.text = ""
                }
                return searchHWSectionData.count
            }
        }
        else if status == 3
        {
            if searchBar.text == ""{
                
                if isFilteringCW == true{
                    if filterCWSectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    return filterCWSectionData.count
                    
                }
                else{
                    
                    if cwpSectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    
                    return cwpSectionData.count
                }
            }
            else{
                
                if searchCWSectionData.count == 0{
                    self.emptyLabel.text = "No Data"
                    self.emptyLabel.textColor = UIColor.gray
                    self.emptyLabel.textAlignment = NSTextAlignment.center
                    self.tableView.backgroundView = self.emptyLabel
                }
                else{
                    self.emptyLabel.text = ""
                }
                return searchCWSectionData.count
            }
            
        }
        else
        {
            if searchBar.text == ""{
                
                if isFilteringCSE == true{
                    if filterCSESectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    return filterCSESectionData.count
                    
                }
                else{
                    
                    if cseSectionData.count == 0{
                        self.emptyLabel.text = "No Data"
                        self.emptyLabel.textColor = UIColor.gray
                        self.emptyLabel.textAlignment = NSTextAlignment.center
                        self.tableView.backgroundView = self.emptyLabel
                    }
                    else{
                        self.emptyLabel.text = ""
                    }
                    return cseSectionData.count
                }
            }
            else{
                if searchCWSectionData.count == 0{
                    self.emptyLabel.text = "No Data"
                    self.emptyLabel.textColor = UIColor.gray
                    self.emptyLabel.textAlignment = NSTextAlignment.center
                    self.tableView.backgroundView = self.emptyLabel
                }
                else{
                    self.emptyLabel.text = ""
                }
                
                return searchCWSectionData.count
            }
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if status == 0{
            if searchBar.text == ""{
                return peopleListArr[section].count
            }
            else{
                return searchPeopleList[section].count
            }
        }
        else if status == 1{
            if searchBar.text == ""{
                if isFilteringJSA == true{
                    return filterSegregatedData[section].count
                }
                return finalArray[section].count
            }
            else{
                
                return searchSegregatedData[section].count
            }
        }
        else if status == 2{
            if searchBar.text == ""{
                if isFilteringHW == true{
                    return filterHWSegregatedData[section].count
                }
                return hwpFinalArray[section].count
            }
            else{
                return searchSegregatedHWData[section].count
            }
        }
        else if status == 3{
            if searchBar.text == ""{
                if isFilteringCW == true{
                    return filterCWSegregatedData[section].count
                }
                return cwpFinalArray[section].count
            }
            else{
                return searchSegregatedCWData[section].count
            }
        }
        else{
            if searchBar.text == ""{
                if isFilteringCSE == true{
                    return filterCSESegregatedData[section].count
                }
                return cseFinalArray[section].count
            }
            else{
                return searchSegregatedCSEData[section].count
            }
        }
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if status == 0{
            let cell = tableView.dequeueReusableCell(withIdentifier: "JSACardCell")! as! JSACardCell
            cell.clipsToBounds = true
            cell.selectionStyle = .none
            if searchBar.text == ""{
                if peopleListArr.count > 0{
                    cell.setLabel(data: peopleListArr[indexPath.section][indexPath.row])
                }
            }
            else{
                cell.setLabel(data: searchPeopleList[indexPath.section][indexPath.row])
            }
            return cell
        }
        else if status == 1{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCardCell")! as! PermitCardCell
            cell.clipsToBounds = true
            if searchBar.text == ""{
                if isFilteringJSA{
                    cell.setData(jsa: filterSegregatedData[indexPath.section][indexPath.row])
                }
                else{
                    cell.setData(jsa: finalArray[indexPath.section][indexPath.row])
                }
            }
            else{
                
                cell.setData(jsa: searchSegregatedData[indexPath.section][indexPath.row])
            }
            cell.selectionStyle = .none
            return cell
        }
        else if status == 2{
            let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCardCell")! as! PermitCardCell
            cell.clipsToBounds = true
            if searchBar.text == ""{
                if isFilteringHW{
                    cell.setCWPData(jsa: filterHWSegregatedData[indexPath.section][indexPath.row])
                }
                else{
                    cell.setCWPData(jsa: hwpFinalArray[indexPath.section][indexPath.row])
                }
            }
            else{
                cell.setCWPData(jsa: searchSegregatedHWData[indexPath.section][indexPath.row])
            }
            cell.selectionStyle = .none
            return cell
        }
        else if status == 3{
            let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCardCell")! as! PermitCardCell
            cell.clipsToBounds = true
            if searchBar.text == ""{
                if isFilteringCW{
                    cell.setCWPData(jsa: filterCWSegregatedData[indexPath.section][indexPath.row])
                }
                else{
                    cell.setCWPData(jsa: cwpFinalArray[indexPath.section][indexPath.row])
                }
                
            }
            else{
                cell.setCWPData(jsa: searchSegregatedCWData[indexPath.section][indexPath.row])
            }
            cell.selectionStyle = .none
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCardCell")! as! PermitCardCell
            cell.clipsToBounds = true
            if searchBar.text == ""{
                if isFilteringCSE{
                    cell.setCWPData(jsa: filterCSESegregatedData[indexPath.section][indexPath.row])
                }
                else{
                    cell.setCWPData(jsa: cseFinalArray[indexPath.section][indexPath.row])
                }
                
            }
            else{
                cell.setCWPData(jsa: searchSegregatedCSEData[indexPath.section][indexPath.row])
            }
            cell.selectionStyle = .none
            return cell
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 96
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
        let label = UILabel(frame: CGRect(x: 16, y: -8, width: headerView.frame.size.width, height: 45))
        if status == 0{
            if searchBar.text == ""{
                label.text = location[section]
            }
            else{
                label.text = searchLocation[section]
            }
            
        }
        else if status == 1{
            if searchBar.text == ""{
                if isFilteringJSA{
                    label.text = filterSectionData[section]
                }
                else{
                    label.text = sectionData[section]
                }
            }
            else{
                label.text = searchSectionData[section]
            }
        }
        else if status == 2{
            if searchBar.text == ""{
                if isFilteringHW{
                    label.text = filterHWSectionData[section]
                }
                else{
                    label.text = hwpSectionData[section]
                }
            }
            else{
                label.text = searchHWSectionData[section]
            }
        }
        else if status == 3{
            if searchBar.text == ""{
                if isFilteringCW{
                    label.text = filterCWSectionData[section]
                }
                else{
                    label.text = cwpSectionData[section]
                }
            }
            else{
                label.text = searchCWSectionData[section]
            }
        }
        else{
            if searchBar.text == ""{
                if isFilteringCW{
                    label.text = filterCSESectionData[section]
                }
                else{
                    label.text = cseSectionData[section]
                }
            }
            else{
                label.text = searchCSESectionData[section]
            }
        }
        
        label.font = UIFont.boldSystemFont(ofSize: 14)
        //label.textColor = UIColor.darkGray
        headerView.backgroundColor = UIColor(named: "BlackColor")//UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        return headerView
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if status == 0{
            let vc = Storyboard.initialBoard.instantiateViewController(withIdentifier: "UserDetail") as! UserDetail
            self.modalPresentationStyle = .overFullScreen
            if searchBar.text == ""{
                vc.user = peopleListArr[indexPath.section][indexPath.row]
            }
            else{
                vc.user = searchPeopleList[indexPath.section][indexPath.row]
            }
            
            self.present(vc, animated: false, completion: nil)
        }
        else if status == 1
        {
            if searchBar.text == ""{
                if isFilteringJSA{
                    selectedJSA = filterSegregatedData[indexPath.section][indexPath.row].permitNumber
                }
                else{
                    selectedJSA = finalArray[indexPath.section][indexPath.row].permitNumber
                }
            }
            else{
                selectedJSA = searchSegregatedData[indexPath.section][indexPath.row].permitNumber
            }
            
            UserDefaults.standard.set("false", forKey: "JSAApproved")
            UserDefaults.standard.set("false",forKey: "JSAPreview")
            UserDefaults.standard.set("false", forKey: "JSACreate")
            UserDefaults.standard.synchronize()
            JSAObject = JSA()
            JSAObject.currentFlow = .JSA
            DispatchQueue.main.async {
                
                let vc = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreateJSAVC") as! CreateJSAVC
                self.modalPresentationStyle = .overFullScreen
                vc.selectedJSA = self.selectedJSA
                let jsaDetailService = JSADetailModelService(context: self.context)
                let selectedPermitNumber = Int16(self.selectedJSA) ?? 0
                let searchPredicate = NSPredicate(format:"permitNumber == %@", NSNumber(value: Int(selectedPermitNumber)))
                let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
                if ConnectionCheck.isConnectedToNetwork(){
                    self.navigationController?.pushViewController(vc, animated: true)
                }
                else{
                    if jsaDetail.count > 0{
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    else{
                        let alert = UIAlertController(title: "", message: "Detail not available", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
                        
                        self.present(alert, animated: true, completion: {
                            print("completion block")
                        })
                    }
                }
            }
            
        }
        else if status == 2
        {
            
            UserDefaults.standard.set("false", forKey: "JSAApproved")
            UserDefaults.standard.set("false",forKey: "JSAPreview")
            UserDefaults.standard.set("false", forKey: "JSACreate")
            UserDefaults.standard.synchronize()
            JSAObject = JSA()
            JSAObject.currentFlow = .HWP
            CWObject = ColdPermit()
            
            var selectedPermit = ""
            var status = ""
            if searchBar.text == ""{
                if isFilteringHW{
                    selectedPermit = filterHWSegregatedData[indexPath.section][indexPath.row].permitNumber
                    status = filterHWSegregatedData[indexPath.section][indexPath.row].status
                }
                else{
                    selectedPermit = hwpFinalArray[indexPath.section][indexPath.row].permitNumber
                    status = hwpFinalArray[indexPath.section][indexPath.row].status
                }
            }
            else{
                selectedPermit = searchSegregatedHWData[indexPath.section][indexPath.row].permitNumber
                status = searchSegregatedHWData[indexPath.section][indexPath.row].status
            }
            if status.lowercased() != "closed"
            {
                let vc = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
                self.modalPresentationStyle = .overFullScreen
                vc.selectedCWPermit = selectedPermit
                vc.permitType = "HOT"
                if ConnectionCheck.isConnectedToNetwork(){
                    self.navigationController?.pushViewController(vc, animated: true)
                }
                else{
                    let permitDetailService = PermitDetailModelService(context: self.context)
                    let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedPermit)!))
                    let permitDetail = permitDetailService.get(withPredicate: searchPredicate)
                    var flag : Bool = false
                    if permitDetail.count>0{
                        for each in permitDetail{
                            if each.permitType == PermitType.HWP.rawValue{
                                flag = true
                                self.navigationController?.pushViewController(vc, animated: true)
                                break
                            }
                        }
                    }
                    if flag == false{
                        let alert = UIAlertController(title: "", message: "Detail not available", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
                        
                        self.present(alert, animated: true, completion: {
                            print("completion block")
                        })
                    }
                }
            }
            
        }
        else if status == 3
        {
            
            UserDefaults.standard.set("false", forKey: "JSAApproved")
            UserDefaults.standard.set("false",forKey: "JSAPreview")
            UserDefaults.standard.set("false", forKey: "JSACreate")
            UserDefaults.standard.synchronize()
            JSAObject = JSA()
            JSAObject.currentFlow = .CWP
            CWObject = ColdPermit()
            var selectedPermit = ""
            var status = ""
            if searchBar.text == ""{
                if isFilteringCW{
                    selectedPermit = filterCWSegregatedData[indexPath.section][indexPath.row].permitNumber
                    status = filterCWSegregatedData[indexPath.section][indexPath.row].status
                }
                else{
                    selectedPermit = cwpFinalArray[indexPath.section][indexPath.row].permitNumber
                    status = cwpFinalArray[indexPath.section][indexPath.row].status
                }
            }
            else{
                selectedPermit = searchSegregatedCWData[indexPath.section][indexPath.row].permitNumber
                status = searchSegregatedCWData[indexPath.section][indexPath.row].status
            }
            if status.lowercased() != "closed"
            {
                let vc = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
                self.modalPresentationStyle = .overFullScreen
                vc.selectedCWPermit = selectedPermit
                vc.permitType = "COLD"
                if ConnectionCheck.isConnectedToNetwork(){
                    self.navigationController?.pushViewController(vc, animated: true)
                }
                else{
                    let permitDetailService = PermitDetailModelService(context: self.context)
                    let searchPredicate = NSPredicate(format: "permitNumber == %@", selectedPermit)
                    let permitDetail = permitDetailService.get(withPredicate: searchPredicate)
                    var flag : Bool = false
                    if permitDetail.count>0{
                        for each in permitDetail{
                            if each.permitType == PermitType.CWP.rawValue{
                                flag = true
                                self.navigationController?.pushViewController(vc, animated: true)
                                break
                            }
                        }
                    }
                    if flag == false{
                        let alert = UIAlertController(title: "", message: "Detail not available", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
                        
                        self.present(alert, animated: true, completion: {
                            print("completion block")
                        })
                    }
                }
            }
            
        }
        else if status == 4
        {
            
            UserDefaults.standard.set("false", forKey: "JSAApproved")
            UserDefaults.standard.set("false",forKey: "JSAPreview")
            UserDefaults.standard.set("false", forKey: "JSACreate")
            UserDefaults.standard.synchronize()
            JSAObject = JSA()
            JSAObject.currentFlow = .CSEP
            CWObject = ColdPermit()
            var selectedPermit = ""
            var status = ""
            if searchBar.text == ""{
                if isFilteringCSE{
                    selectedPermit = filterCSESegregatedData[indexPath.section][indexPath.row].permitNumber
                    status = filterCSESegregatedData[indexPath.section][indexPath.row].status
                }
                else{
                    selectedPermit = cseFinalArray[indexPath.section][indexPath.row].permitNumber
                    status = cseFinalArray[indexPath.section][indexPath.row].status
                }
            }
            else{
                selectedPermit = searchSegregatedCSEData[indexPath.section][indexPath.row].permitNumber
                status = searchSegregatedCSEData[indexPath.section][indexPath.row].status
            }
            if status.lowercased() != "closed"
            {
                let vc = Storyboard.permit.instantiateViewController(withIdentifier: "PermitController") as! PermitController
                self.modalPresentationStyle = .overFullScreen
                vc.selectedCWPermit = selectedPermit
                vc.permitType = "CSE"
                if ConnectionCheck.isConnectedToNetwork(){
                    self.navigationController?.pushViewController(vc, animated: true)
                }
                else{
                    let permitDetailService = PermitDetailModelService(context: self.context)
                    let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedPermit)!))
                    let permitDetail = permitDetailService.get(withPredicate: searchPredicate)
                    var flag : Bool = false
                    if permitDetail.count>0{
                        for each in permitDetail{
                            if each.permitType == PermitType.CSEP.rawValue{
                                flag = true
                                self.navigationController?.pushViewController(vc, animated: true)
                                break
                            }
                        }
                    }
                    if flag == false{
                        let alert = UIAlertController(title: "", message: "Detail not available", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
                        
                        self.present(alert, animated: true, completion: {
                            print("completion block")
                        })
                    }
                }
            }
            
        }
    }
}

// MARK: - JSA permit API call
extension CreatedPermitControllerViewController{
    
    func getOfflineJSAList(){
        
        let jsaListService = JSAModelService(context: self.context)
        var facilityOrSiteValue : String?
        if currentLocation.hierarchyLevel == "facility"{
            facilityOrSiteValue = currentLocation.facilityOrSite
        }
        else{
            facilityOrSiteValue = currentLocation.muwi
        }
        let searchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue!)
        let jsaModelList = jsaListService.get(withPredicate: searchPredicate)
        self.listData.removeAll()
        for each in jsaModelList{
            self.listData.append(each.getJSA())
        }
        self.parseJSAdata()
    }
    
    func getJSAList(){
        
        //let url = "\(BaseUrl.apiURL)/com.iop.ptw/GetJSAbyLocation.xsjs?muwi='\(currentLocation.muwi)'&facility='\(currentLocation.facilityOrSite)'"
        
        
        var url = ""
        if currentLocation.muwi != ""{
            url = IMOEndpoints.getJSAByLocation+"muwi=\(currentLocation.muwi)&facility=\(currentLocation.facilityOrSite)"
        }
        else{
            url = IMOEndpoints.getJSAByLocation+"facility=\(currentLocation.facilityOrSite)"
        }
        
        
        
        
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl ?? "")!)
        urlRequest.httpMethod = "get"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    
                    print("**************************")
                    print(JSON)
                    print("**************************")
                    DispatchQueue.main.async {
                        
                        if let jsonDict2 = JSON as? NSDictionary {
                            
                            var jsonDict = [NSDictionary]()
                            jsonDict = (jsonDict2["data"]  as? [NSDictionary] ?? [])
                            self.listData.removeAll()
                            let jsaListService = JSAModelService(context: self.context)
                            var facilityOrSiteValue : String?
                            if currentLocation.hierarchyLevel == "facility"{
                                facilityOrSiteValue = currentLocation.facilityOrSite
                            }
                            else{
                                facilityOrSiteValue = currentLocation.muwi
                            }
                            let searchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue!)
                            let jsaValue = jsaListService.get(withPredicate: searchPredicate)
                            
                            for each in jsaValue{
                                jsaListService.delete(id: each.objectID)
                            }
                            for each in jsonDict{
                                let jsa = JSAList(JSON : each)
                                print(jsa)
                                _ = jsaListService.create(listData: jsa, permitNumber: Int(jsa.permitNumber) ?? 0, facilityOrSite: facilityOrSiteValue!)
                                self.listData.append(jsa)
                            }
                            jsaListService.saveChanges()
                            self.parseJSAdata()
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
                        self.present(alertController, animated: true, completion: nil)
                    }
                    
                }
        }.resume()
    }
    
    func getOfflinePermitList(){
        let permitListService = PermitModelService(context: self.context)
        var facilityOrSiteValue : String?
        if currentLocation.hierarchyLevel == "facility"{
            facilityOrSiteValue = currentLocation.facilityOrSite
        }
        else{
            facilityOrSiteValue = currentLocation.muwi
        }
        let searchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue!)
        let permitModelList = permitListService.get(withPredicate: searchPredicate)
        self.HWList.removeAll()
        self.CWList.removeAll()
        self.CSEList.removeAll()
        for each in permitModelList{
            if each.permitType == PermitType.CWP.rawValue{
                self.CWList.append(each.getPermitList())
            }
            else if each.permitType == PermitType.HWP.rawValue{
                self.HWList.append(each.getPermitList())
            }
            else if each.permitType == PermitType.CSEP.rawValue{
                self.CSEList.append(each.getPermitList())
            }
        }
        self.parsePermitData()
    }
    
    func getPermitList(){
        
        var url = ""
        if currentLocation.muwi != ""{
            url = IMOEndpoints.getPermits + currentLocation.facilityOrSite + "&muwi='\(currentLocation.muwi)'"
        }
        else{
            url = IMOEndpoints.getPermits + currentLocation.facilityOrSite
        }
        
        
        
        //"\(BaseUrl.apiURL)/com.iop.ptw/GetPermitsbyLocation.xsjs?muwi='\(currentLocation.muwi)'&facility='\(currentLocation.facilityOrSite)'"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl ?? "")!)
        urlRequest.httpMethod = "get"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                    DispatchQueue.main.async {
                        self.HWList.removeAll()
                        self.CWList.removeAll()
                        self.CSEList.removeAll()
                        if let jsonDict = JSON?.value(forKey: "data") as? NSDictionary {
                            let permitListService = PermitModelService(context: self.context)
                            var facilityOrSiteValue : String?
                            if currentLocation.hierarchyLevel == "facility"{
                                facilityOrSiteValue = currentLocation.facilityOrSite
                            }
                            else{
                                facilityOrSiteValue = currentLocation.muwi
                            }
                            let searchPredicate = NSPredicate(format: "facilityOrSite == %@", facilityOrSiteValue!)
                            let permitModelList = permitListService.get(withPredicate: searchPredicate)
                            for each in permitModelList{
                                permitListService.delete(id: each.objectID)
                            }
                            if let val = jsonDict.value(forKey: "cwp") as? [NSDictionary]
                            {
                                for each in val
                                {
                                    let value = PermitList.init(JSON: each)
                                    DispatchQueue.main.async {
                                        _ = permitListService.create(detailData: value, permitNumber: Int(value.permitNumber)!, permitType: PermitType.CWP.rawValue, facilityOrSite: facilityOrSiteValue!)
                                    }
                                    self.CWList.append(value)
                                }
                            }
                            if let val = jsonDict.value(forKey: "hwp") as? [NSDictionary]
                            {
                                for each in val
                                {
                                    let value = PermitList.init(JSON: each)
                                    DispatchQueue.main.async {
                                        _ = permitListService.create(detailData: value, permitNumber: Int(value.permitNumber)!, permitType: PermitType.HWP.rawValue, facilityOrSite: facilityOrSiteValue!)
                                    }
                                    self.HWList.append(value)
                                }
                            }
                            if let val = jsonDict.value(forKey: "cse") as? [NSDictionary]
                            {
                                for each in val
                                {
                                    let value = PermitList.init(JSON: each)
                                    DispatchQueue.main.async {
                                        _ = permitListService.create(detailData: value, permitNumber: Int(value.permitNumber)!, permitType: PermitType.CSEP.rawValue, facilityOrSite: facilityOrSiteValue!)
                                    }
                                    self.CSEList.append(value)
                                }
                            }
                            permitListService.saveChanges()
                            self.parsePermitData()
                        }
                    }
                    
                }catch{
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    DispatchQueue.main.async {
                        let message = error!.localizedDescription
                        
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    
                }
        }.resume()
    }
    
}
