//
//  StopJobVCViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 22/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class StopJobVCViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate{

    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var confirmPeopleBottonView: UIView!
    @IBOutlet weak var addButton: UIBarButtonItem!
    @IBOutlet weak var potentialHazardTableView: UITableView!
    @IBOutlet weak var btnAddPeople: UIButton!
    @IBOutlet weak var btnCreatePeople: UIButton!

    
    var isJsaPreview = false
    var isJsaApproved = false
    var stopJobArray = [String]()
    var note = "Note: I / We will STOP THE JOB if any of the following occur:"
    let defaults = UserDefaults.standard
    override func viewDidLoad() {
        super.viewDidLoad()
    
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "Stop the job - Review"
                isJsaPreview = true
                
               
            }
            else if val == "false"
            {
                navigationTitle.title = "Stop the job"
                isJsaPreview = false
                
            }
        }
        else
        {
            navigationTitle.title = "Stop the job"
            isJsaPreview = false
            
        }
        
        if JSAObject.status.lowercased() == "approved"
        {
            isJsaApproved = true
           
        }
        else
        {
            isJsaApproved = false
           
        }
        
        if isJsaPreview == true && isJsaApproved == true
        {
            addButton.isEnabled = false
            addButton.customView?.alpha = 0.0
            confirmPeopleBottonView.isHidden = false
        }
        else if isJsaPreview == false && isJsaApproved == true
        {
            addButton.isEnabled = false
            addButton.customView?.alpha = 0.0
            confirmPeopleBottonView.isHidden = true
        }
        else if isJsaPreview == true && isJsaApproved == false
        {
            addButton.isEnabled = false
            addButton.customView?.alpha = 0.0
            confirmPeopleBottonView.isHidden = false
        }
        else
        {
            addButton.isEnabled = true
            addButton.customView?.alpha = 1.0
            confirmPeopleBottonView.isHidden = true
        }
       
    }

    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let stringTwo = "STOP THE JOB"
        let range = (note as NSString).range(of: stringTwo)
        let attributedText = NSMutableAttributedString.init(string: note)
        attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.red , range: range)
        noteLabel.attributedText = attributedText
        potentialHazardTableView.reloadData()
        if currentUser.isReadOnly == true{
            self.btnAddPeople.isHidden = true
           //self.btnCreatePeople.isHidden = true
            self.btnCreatePeople.setTitle("DONE", for: .normal)
            let button: UIButton = UIButton(type: UIButton.ButtonType.custom) 
            self.addButton.customView = button
        }
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {

        return JSAObject.stopTheJob.count
        
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:StopJobTableViewCell = (potentialHazardTableView.dequeueReusableCell(withIdentifier: "StopJobTableViewCell") as! StopJobTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        
        cell.nameLabel?.text = JSAObject.stopTheJob[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let VC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddNewJobViewController") as! AddNewJobViewController
        VC.index = indexPath.row
        self.navigationController?.pushViewController(VC, animated: true)
    }
    
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func previousButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
            JSAObject = JSA()
        //self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func addNewJob(_ sender: Any)
    {
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddNewJobViewController") as! AddNewJobViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    
    
    @IBAction func addPeopleBtn(_ sender: UIButton)
    {
        var flag = false
        var cflag = false
        if let currentUser = UserDefaults.standard.value(forKey: "CurrentUser") as? String
        {
            if currentUser == "true"
            {
                cflag = true
            }
            else
            {
                cflag = false
            }
        }
        else
        {
            cflag = false
        }
        if let val = UserDefaults.standard.value(forKey: "JSACreate") as? String
        {
            if val == "true"
            {
                flag = true
            }
            else
            {
                flag = false
            }
        }
        else
        {
            flag = false
        }
        
        if flag && cflag
        {
            let alert = UIAlertController(title: "JSA Review", message: "Pending confirmation by the Task Leader of site conditions, I agree that the attached JSA identifies significant Task Steps, Hazards and Controls", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                UserDefaults.standard.set("false", forKey: "CurrentUser")
                UserDefaults.standard.set("false", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                let people = People()
                let name = currentUser.name!
                let nameArray = name.components(separatedBy: " ")
                people.firstName = nameArray[0]
                people.lastName = nameArray[1]
                people.fullName = name
                people.contactNumber = ""
                people.designation = ""
                if !ConnectionCheck.isConnectedToNetwork(){
                    people.hasSignedJSA = 1
                }
                if JSAObject.permitNumber != 0{
                    //rajat stopped the string casting
                    people.permitNumber = (JSAObject.permitNumber)
                }
                else{
                    let value = UserDefaults.standard.string(forKey: "offlinenumber")
                    var counter = 0
                    if value != nil{
                        counter = Int(value!)! - 1
                    }
                    //rajat stopped the casting
                    people.permitNumber = (counter)
                }
                
                JSAObject.peopleList.append(people)
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if currentUser.isReadOnly != true{
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }
        }
    }
    
    @IBAction func cancelButtonAction(segue: UIStoryboardSegue) {
        
    }
    
    
    @IBAction func createPermitBtn(_ sender: UIButton)
    {
        if JSAObject.peopleList.count == 0
        {
            let alert = UIAlertController(title: "JSA Review", message: "Pending confirmation by the Task Leader of site conditions, I agree that the attached JSA identifies significant Task Steps, Hazards and Controls", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
                UserDefaults.standard.set("false", forKey: "CurrentUser")
                UserDefaults.standard.set("false", forKey: "JSAPreview")
                UserDefaults.standard.synchronize()
                let people = People()
                people.firstName = currentUser.firstname!
                people.lastName = currentUser.lastname!
                people.contactNumber = ""
                people.designation = ""
                people.hasSignedJSA = 1
                
                JSAObject.peopleList.append(people)
                
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreatePermitViewController") as! CreatePermitViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
                
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if currentUser.isReadOnly != true{
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreatePermitViewController") as! CreatePermitViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            }else if currentUser.isReadOnly == true{
                self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
                    JSAObject = JSA()
            }
        }
        
    }
    
    @IBAction func onClickSubmit(_ sender: UIButton) {
        
        let alert = UIAlertController(title: nil, message: "I acknowledge I have reviewed the JSA, I understand my roles and responsibilities and I will comply with the instructions for this task", preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler:{ (UIAlertAction)in
            print("User click Dismiss button")
        }))
        
        alert.addAction(UIAlertAction(title: "Agree", style: .default , handler:{ (UIAlertAction)in
            
//            if let val = UserDefaults.standard.data(forKey: "ProposedPeopleJSA")
//            {
                JSAObject.addNewpeople.hasSignedJSA = 1
                if JSAObject.addNewpeople.isCheckedCWP == 1
                {
                    JSAObject.currentFlow = .CWP
                    pushToPermitController = true
                    guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                        return
                    }
                    print(viewControllers)
                    self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                    
//                    , let peopleList = viewControllers.filter({$0.isJsaPreview == false}).first
                }
                else if JSAObject.addNewpeople.isCheckedHWP == 1
                {
                    JSAObject.currentFlow = .HWP
                    pushToPermitController = true
                    guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                        return
                    }
                    print(viewControllers)
                    self.navigationController?.popToViewController(viewControllers.first!, animated: false)
                }
                else if JSAObject.addNewpeople.isCheckedCSE == 1
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
                    
                    UserDefaults.standard.set("false", forKey: "JSAPreview")
                    UserDefaults.standard.synchronize()
                    
                    JSAObject.peopleList.append(JSAObject.addNewpeople)
                    guard let viewControllers: [AddPeopleListViewController] = self.navigationController?.viewControllers.filter({$0 is AddPeopleListViewController}) as? [AddPeopleListViewController]   else {
                        return
                    }
                    print(viewControllers)
                    self.navigationController?.popToViewController(viewControllers.first!, animated: false)
//                    let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddPeopleListViewController") as! AddPeopleListViewController
//                    self.navigationController?.pushViewController(dashBoardVC, animated: true)
                }
            
        }))
        
        self.present(alert, animated: true, completion: {
            print("completion block")
        })
    }
    
}
