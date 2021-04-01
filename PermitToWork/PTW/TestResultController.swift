//
//  TestResultController.swift
//  IOP_iOS
//
//  Created by Soumya Singh on 27/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class TestResultController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet var segmentControl: UISegmentedControl!
    @IBOutlet weak var nextButton: UIButton!

    @IBOutlet weak var editBtn: UIBarButtonItem!
    @IBOutlet var testView: UITableView!
    var permitStr = UserDefaults.standard.string(forKey: "Permit")
    var testResult = TestResult()
    var testResultSections = ["Pre-Start Tests","Work Period Tests"]
    var testerDetail = ["Comments/Special Precaution", "Name", "Serial No"]
    
    var hotPermit = HotPermit()
    var coldPermit = ColdPermit()
    var confineSpacePermit = ConfinedSpacePermit()
    var preTest = [TestsModel]()
    var workPeriodTest = [TestsModel]()
    var isPermitReview = true
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                //navigationTitle.title = "JSA - Review"
                isPermitReview = true
                editBtn.isEnabled = false
            }
            else if val == "false"
            {
                //navigationTitle.title = "JSA"
                isPermitReview = false
                editBtn.isEnabled = true
            }
        }
        else
        {
            //navigationTitle.title = "JSA"
            isPermitReview = false
            editBtn.isEnabled = true
        }
        
        let nib = UINib(nibName: "TestResultCell", bundle: nil)
        testView.register(nib, forCellReuseIdentifier: "TestResultCell")
        let testNib = UINib(nibName: "PermitCell", bundle: nil)
        testView.register(testNib, forCellReuseIdentifier: "PermitCell")
        testView.tableFooterView = UIView()
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        testView.reloadData()
        if currentUser.isReadOnly == true{
            let button: UIButton = UIButton(type: UIButton.ButtonType.custom)
            self.editBtn.customView = button
           // self.nextButton.isHidden = true
            self.nextButton.setTitle("DONE", for: .normal)
        }
    }
    
    func createNavBar(){
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationController?.navigationBar.barTintColor = UIColor(red: 24/255.0, green: 43/255.0, blue: 89/255.0, alpha: 1.0)
        navigationItem.title = "Test Results"
        
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.leftBarButtonItem = backItem
    }
    
    @objc func dismissScreen()
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onEditScreen(_ sender: Any)
    {
        if currentUser.isReadOnly !=  true{
        let alert = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        if (JSAObject.currentFlow == .CWP && JSAObject.CWP.header.status.uppercased() != "APPROVED") || JSAObject.currentFlow == .HWP && JSAObject.HWP.header.status.uppercased() != "APPROVED" || JSAObject.currentFlow == .CSEP && JSAObject.CSEP.header.status.uppercased() != "APPROVED"
        {
            alert.addAction(UIAlertAction(title: "Add Pre Start Tests", style: .default , handler:{ (UIAlertAction)in
                
                UserDefaults.standard.set("1", forKey: "TestResult")
                UserDefaults.standard.synchronize()
                let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PretestResultViewController") as! PretestResultViewController
                self.navigationController?.pushViewController(vc, animated: true)
                
            }))
        }

        if JSAObject.currentFlow == .CWP && JSAObject.CWP.header.status.uppercased() == "APPROVED" || JSAObject.currentFlow == .HWP && JSAObject.HWP.header.status.uppercased() == "APPROVED" || JSAObject.currentFlow == .CSEP && JSAObject.CSEP.header.status.uppercased() == "APPROVED"
        {
            alert.addAction(UIAlertAction(title: "Add Work Period Tests", style: .default , handler:{ (UIAlertAction)in
                UserDefaults.standard.set("2", forKey: "TestResult")
                UserDefaults.standard.synchronize()
                let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PretestResultViewController") as! PretestResultViewController
                self.navigationController?.pushViewController(vc, animated: true)
            }))
        }

        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
            print("User click Dismiss button")
        }))
        
        self.present(alert, animated: true, completion: {
            print("completion block")
        })
        }
    }
    
    
    @IBAction func onSegmentSwitch(_ sender: UISegmentedControl) {
        testView.reloadData()
    }
    
    @IBAction func onPreviousPress(_ sender: Any) {
        dismissScreen()
    }
    
    @IBAction func onPressHome(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        //self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func onNextPress(_ sender: UIButton) {
        
        //var validation = self.validateFields()
        if currentUser.isReadOnly != true{
        if isPermitReview
        {
           // validation.isValid = true
            if JSAObject.currentFlow == .CWP
            {
                
                let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PicSignOffViewController") as! PicSignOffViewController
                self.navigationController?.pushViewController(vc, animated: true)
                
            }
            else
            {
                let alert = UIAlertController(title: nil, message: "I acknowledge I have reviewed the Permit, I understand my roles and responsibilities and I will comply with the instructions for this task", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                    print("User click Dismiss button")
                }))
                
                alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                    
                    if JSAObject.currentFlow == .HWP
                    {
                        JSAObject.addNewpeople.hasSignedHWP = 1
                        JSAObject.addNewpeople.isCheckedHWP = 0
                        if JSAObject.addNewpeople.isCheckedCWP == 1 && JSAObject.addNewpeople.hasSignedCWP != 1
                        {
                            JSAObject.currentFlow = .CWP
                            pushToPermitController = true
                            guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                                return
                            }
                            print(viewControllers)
                            self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                        }
                        else if JSAObject.addNewpeople.isCheckedCSE == 1 && JSAObject.addNewpeople.hasSignedCSE != 1
                        {
                            JSAObject.currentFlow = .CSEP
                            pushToPermitController = true
                            guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                                return
                            }
                            print(viewControllers)
                            self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                        }
                        else
                        {
                            if JSAObject.addPeopleIndex != nil{
                                JSAObject.peopleList.remove(at: JSAObject.addPeopleIndex!)
                                JSAObject.addPeopleIndex = nil
                            }
                            JSAObject.peopleList.append(JSAObject.addNewpeople)
                            UserDefaults.standard.set("false", forKey: "JSAPreview")
                            UserDefaults.standard.synchronize()
                            pushToPermitController = false
                            guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                                return
                            }
                            print(viewControllers)
                            self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                            
                            
                            // JSAObject.peopleList.append(JSAObject.addNewpeople)
                        }
                        
                    }
                    else if JSAObject.currentFlow == .CSEP
                    {
                        JSAObject.addNewpeople.hasSignedCSE = 1
                        JSAObject.addNewpeople.isCheckedCSE = 0
                        if JSAObject.addNewpeople.isCheckedCWP == 1 && JSAObject.addNewpeople.hasSignedCWP != 1
                        {
                            JSAObject.currentFlow = .CWP
                            pushToPermitController = true
                            guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                                return
                            }
                            print(viewControllers)
                            self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                        }
                        else if JSAObject.addNewpeople.isCheckedHWP == 1 && JSAObject.addNewpeople.hasSignedHWP != 1
                        {
                            JSAObject.currentFlow = .HWP
                            pushToPermitController = true
                            guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                                return
                            }
                            print(viewControllers)
                            self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                        }
                        else
                        {
                            if JSAObject.addPeopleIndex != nil{
                                JSAObject.peopleList.remove(at: JSAObject.addPeopleIndex!)
                                JSAObject.addPeopleIndex = nil
                            }
                            JSAObject.peopleList.append(JSAObject.addNewpeople)
                            UserDefaults.standard.set("false", forKey: "JSAPreview")
                            UserDefaults.standard.synchronize()
                            pushToPermitController = false
                            guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                                return
                            }
                            print(viewControllers)
                            self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                        }
                    }
                    
                }))
                
                self.present(alert, animated: true, completion: {
                    print("completion block")
                })
            }
            
        }
        else
        {
//            if !validation.isValid {
//
//                let alertController = UIAlertController(title: nil, message: validation.message, preferredStyle: .alert)
//                let action = UIAlertAction(title: "OK", style: .default, handler: nil)
//                alertController.addAction(action)
//                self.present(alertController, animated: true, completion: nil)
//            } else {
            
            
            
                if JSAObject.currentFlow == .CWP
                {
                    
                    if JSAObject.CWP.header.status == ""
                    {
                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PicSignOffViewController") as! PicSignOffViewController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
//                    else if JSAObject.CWP.header.status.lowercased() == "submitted"
//                    {
//                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PicSignOffViewController") as! PicSignOffViewController
//                        self.navigationController?.pushViewController(vc, animated: true)
//                    }
                    else if JSAObject.CWP.header.status.lowercased() == "approved"
                    {
                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitCloseOutController") as! PermitCloseOutController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    
                    
//                    JSAObject.hasCWP = 1
//                    if JSAObject.isHWP == 1
//                    {
//                        JSAObject.currentFlow = .HWP
//                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
//                        self.navigationController?.pushViewController(vc, animated: true)
//
//                    }
//                    else if JSAObject.isCSP == 1
//                    {
//                        JSAObject.currentFlow = .CSEP
//                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
//                        self.navigationController?.pushViewController(vc, animated: true)
//                    }
//                    else
//                    {
//                        if JSAObject.CWP.header.status == ""
//                        {
//                            let vc = UIStoryboard(name: "CreateJSA", bundle: Bundle.main).instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
//                            self.navigationController?.pushViewController(vc, animated: true)
//                        }
//                        else if JSAObject.CWP.header.status.lowercased() == "submitted"
//                        {
//                            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PicSignOffViewController") as! PicSignOffViewController
//                            self.navigationController?.pushViewController(vc, animated: true)
//                        }
//                        else if JSAObject.CWP.header.status.lowercased() == "approved"
//                        {
//                            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitCloseOutController") as! PermitCloseOutController
//                            self.navigationController?.pushViewController(vc, animated: true)
//                        }
//                    }
                    
                }
                else if JSAObject.currentFlow == .HWP
                {
                    JSAObject.hasHWP = 1
                    if JSAObject.isCSP == 1
                    {
                        JSAObject.currentFlow = .CSEP
                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    else
                    {
                        if JSAObject.HWP.header.status == ""
                        {
                            let vc = UIStoryboard(name: "CreateJSA", bundle: Bundle.main).instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                        else if JSAObject.HWP.header.status.lowercased() == "submitted"
                        {
                            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "ApprovalToAuthority") as! ApprovalToAuthority
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                        else if JSAObject.HWP.header.status.lowercased() == "approved"
                        {
                            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "FireWatchViewController") as! FireWatchViewController
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                    }
                    
                }
                else if JSAObject.currentFlow == .CSEP
                {
                    JSAObject.hasCSP = 1
                    if JSAObject.CSEP.header.status == ""
                    {
                        let vc = UIStoryboard(name: "CreateJSA", bundle: Bundle.main).instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    else if JSAObject.CSEP.header.status.lowercased() == "submitted"
                    {
                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "ApprovalToAuthority") as! ApprovalToAuthority
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    else if JSAObject.CSEP.header.status.lowercased() == "approved"
                    {
                        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "FireWatchViewController") as! FireWatchViewController
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                }
            //}
            
        }
        }else if currentUser.isReadOnly == true{
            self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        textField.endEditing(true)
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        if JSAObject.currentFlow == .CWP{
            if textField.tag == 0{
                JSAObject.testResult.specialPrecaution = textField.text!
            }
            else if textField.tag == 1{
                JSAObject.testResult.Name = textField.text!
            }
            else if textField.tag == 2{
                JSAObject.testResult.serialNumber = textField.text!
            }
        }
            
        else if JSAObject.currentFlow == .HWP{
            if textField.tag == 0{
                JSAObject.testResult.specialPrecaution = textField.text!
            }
            else if textField.tag == 1{
                JSAObject.testResult.Name = textField.text!
            }
            else if textField.tag == 2{
                JSAObject.testResult.serialNumber = textField.text!
            }
        }
        else if JSAObject.currentFlow == .CSEP{
            if textField.tag == 0{
                JSAObject.testResult.specialPrecaution = textField.text!
            }
            else if textField.tag == 1{
                JSAObject.testResult.Name = textField.text!
            }
            else if textField.tag == 2{
                JSAObject.testResult.serialNumber = textField.text!
            }
        }
    }
}

extension TestResultController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        
        if segmentControl.selectedSegmentIndex == 0{
            return 1
        }
        else{
            
            if JSAObject.currentFlow == .CWP{
                if JSAObject.CWP.header.status == ""{
                    return 1
                }
                else{
                    return 2
                }
            }
            else if JSAObject.currentFlow == .CSEP{
                if JSAObject.CSEP.header.status == ""{
                    return 1
                }
                else{
                    return 2
                }
            }
            else{
                if JSAObject.HWP.header.status == ""{
                    return 1
                }
                else{
                    return 2
                }
            }
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if segmentControl.selectedSegmentIndex == 0{
            return 3
        }
        else{
            if JSAObject.currentFlow == .CWP{
                if section == 0{
                    return JSAObject.testResult.preStartTests.count
                }
                else{
                    return JSAObject.testResult.workPeriodTests.count
                }
            }
            else  if JSAObject.currentFlow == .CSEP{
                if section == 0{
                    return JSAObject.testResult.preStartTests.count
                }
                else{
                    return JSAObject.testResult.workPeriodTests.count
                }
            }
            else{
                
                if section == 0{
                    return JSAObject.testResult.preStartTests.count
                }
                else{
                    return JSAObject.testResult.workPeriodTests.count
                }
            }
            
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if segmentControl.selectedSegmentIndex == 1{
            let cell = tableView.dequeueReusableCell(withIdentifier: "TestResultCell")! as! TestResultCell
            
            if JSAObject.currentFlow == .CWP{
                if indexPath.section == 0{
                    cell.setData(test: JSAObject.testResult.preStartTests[indexPath.row])
                }
                else{
                    cell.setData(test: JSAObject.testResult.workPeriodTests[indexPath.row])
                }
            }
            else if JSAObject.currentFlow == .HWP{
                if indexPath.section == 0{
                    cell.setData(test: JSAObject.testResult.preStartTests[indexPath.row])
                }
                else{
                    cell.setData(test: JSAObject.testResult.workPeriodTests[indexPath.row])
                }
            }
            else{
                if indexPath.section == 0{
                    cell.setData(test: JSAObject.testResult.preStartTests[indexPath.row])
                }
                else{
                    cell.setData(test: JSAObject.testResult.workPeriodTests[indexPath.row])
                }
            }
            cell.clipsToBounds = true
            cell.selectionStyle = .none
            
            if isPermitReview
            {
                cell.isUserInteractionEnabled = false
            }
            else
            {
                cell.isUserInteractionEnabled = true
            }
//            if currentUser.isReadOnly == true{
//                cell.isUserInteractionEnabled = false
//            }
            
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
            cell.clipsToBounds = true
            cell.valueTextField.delegate = self
            cell.valueTextField.tag = indexPath.row
            if JSAObject.currentFlow == .CWP{
                if indexPath.row == 0{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.specialPrecaution)
                }
                else if indexPath.row == 1{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.Name)
                }
                else if indexPath.row == 2{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.serialNumber)
                }
            }
            else if JSAObject.currentFlow == .HWP{
                if indexPath.row == 0{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.specialPrecaution)
                }
                else if indexPath.row == 1{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.Name)
                }
                else if indexPath.row == 2{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.serialNumber)
                }
            }
            else{
                if indexPath.row == 0{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.specialPrecaution)
                }
                else if indexPath.row == 1{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.Name)
                }
                else if indexPath.row == 2{
                    cell.setData(key: testerDetail[indexPath.row], value: JSAObject.testResult.serialNumber)
                }
            }
            cell.selectionStyle = .none
            if currentUser.isReadOnly == true{
                cell.isUserInteractionEnabled = false
            }
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if segmentControl.selectedSegmentIndex == 1{
            let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PretestResultViewController") as! PretestResultViewController
            vc.index = indexPath.row
            vc.indexpath = indexPath
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 55
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if segmentControl.selectedSegmentIndex == 0{
            return 0
        }
        else{
            return 35
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if segmentControl.selectedSegmentIndex == 1{
            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
            let label = UILabel(frame: CGRect(x: 16, y: -8, width: headerView.frame.size.width, height: 45))
            
            label.text = testResultSections[section]
            label.font = UIFont.boldSystemFont(ofSize: 14)
           // label.textColor = UIColor.darkGray
            headerView.backgroundColor = UIColor(named: "BlackColor")//UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
            headerView.addSubview(label)
            return headerView
        }
        else{
            return UIView()
        }
    }
}

extension TestResultController  {
    
    func validateFields() -> (isValid: Bool, message: String) {
        
        if JSAObject.currentFlow == .CWP{
            guard !JSAObject.testResult.Name.isEmpty else {
                return (false, "Please enter name")
            }
            guard !JSAObject.testResult.serialNumber.isEmpty else {
                return (false, "Please enter serial number")
            }
            if JSAObject.CWP.header.status.lowercased() != "approved"
            {
                guard JSAObject.testResult.preStartTests.count > 0 else {
                    return (false, "Please add atleast one pre start test")
                }
            }
            
            
            return (true, "")
        }
        else if JSAObject.currentFlow == .HWP{
            guard !JSAObject.testResult.Name.isEmpty else {
                return (false, "Please enter name")
            }
            guard !JSAObject.testResult.serialNumber.isEmpty else {
                return (false, "Please enter serial number")
            }
            if JSAObject.CWP.header.status.lowercased() != "approved"
            {
                guard JSAObject.testResult.preStartTests.count > 0 else {
                    return (false, "Please add atleast one pre start test")
                }
            }
            return (true, "")
        }
            
        else{
            guard !JSAObject.testResult.Name.isEmpty else {
                return (false, "Please enter name")
            }
            guard !JSAObject.testResult.serialNumber.isEmpty else {
                return (false, "Please enter serial number")
            }
            if JSAObject.CWP.header.status.lowercased() != "approved"
            {
                guard JSAObject.testResult.preStartTests.count > 0 else {
                    return (false, "Please add atlest one pre start test")
                }
            }
            
            
            return (true, "")
        }
    }
}

