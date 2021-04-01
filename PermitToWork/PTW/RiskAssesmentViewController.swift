
//
//  RiskAssesmentViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 21/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

@available(iOS 10.0, *)

class RiskAssesmentViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate, RiskAssessmentKeyboardDismissProtocol{
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var eitherLbl: UILabel!
    @IBOutlet weak var riskAssesmentTableView: UITableView!
    @IBOutlet var modifySwitch: UISwitch?
    @IBOutlet var continueSwitch: UISwitch?
    @IBOutlet var assView : UIView?
    //    let context  = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
    let defaults = UserDefaults.standard
    var riskAssesmentArray = [String]()
    var selectStatus : Bool = false
    var switchModifyStatus : Bool = false
    var switchContinueStatus : Bool = false
    var riskAssOFFLineArray  = [String]()
    var switchStatus : String = ""
    var sectionArray = ["", "", "HEARING PROTECTION","Respirator Type","","","EYE PROTECTION","GLOVES","","",""]
    
    var localArray = [[Int]]()
    var topSwitch = [Int]()
    var items = [["Hard Hat"],["Safety Shoes / Boots"],["Single", "Double"],["SCBA", "Dusk Mask"],["Fall Protection", "Fall Restraint"],["Flame Resistant Cloting"],["Safety Glasses", "FACE Shield", "Goggles"],["Cotton", "Leather", "Impact Protection","Others","Chemical"],["Chemical Suit", "Apron"],["Foul Weather Gear"],["Other PPE"]]
    var isJsaPreview = false
    var isJsaApproved = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let textNib = UINib(nibName: "RiskAssessmentCell", bundle: nil)
        riskAssesmentTableView.register(textNib, forCellReuseIdentifier: "RiskAssessmentCell")
        riskAssesmentTableView.estimatedRowHeight = 44.0
        riskAssesmentTableView.rowHeight = UITableView.automaticDimension
        riskAssesmentTableView.separatorColor = UIColor.clear
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "Risk Assessment - Review"
                isJsaPreview = true
                modifySwitch?.isUserInteractionEnabled = false
                continueSwitch?.isUserInteractionEnabled = false
                
            }
            else if val == "false"
            {
                navigationTitle.title = "Risk Assessment"
                isJsaPreview = false
                modifySwitch?.isUserInteractionEnabled = true
                continueSwitch?.isUserInteractionEnabled = true
            }
        }
        else
        {
            navigationTitle.title = "Risk Assessment"
            isJsaPreview = false
            modifySwitch?.isUserInteractionEnabled = true
            continueSwitch?.isUserInteractionEnabled = true
        }
        
        
        
        if JSAObject.status.lowercased() == "approved"
        {
            isJsaApproved = true
            modifySwitch?.isUserInteractionEnabled = false
            continueSwitch?.isUserInteractionEnabled = false
            
        }
        else
        {
            isJsaApproved = false
            modifySwitch?.isUserInteractionEnabled = true
            continueSwitch?.isUserInteractionEnabled = true
        }
        
        if let val = UserDefaults.standard.array(forKey: "RiskAssesmentViewControllerTable") as? [[Int]]
        {
            //localArray = val
            topSwitch = UserDefaults.standard.array(forKey: "RiskAssesmentViewControllerSwitch") as! [Int]
        }
        else
        {
            topSwitch = [0, 0]
            
            for value in items
            {
                var arr = [Int]()
                for _ in value
                {
                    arr.append(0)
                }
                localArray.append(arr)
            }
        }
        
        if topSwitch[0] == 1
        {
            modifySwitch?.isOn = true
        }
        else
        {
            modifySwitch?.isOn = false
        }
        
        if topSwitch[1] == 1
        {
            continueSwitch?.isOn = true
        }
        else
        {
            continueSwitch?.isOn = false
        }
        
        
        // riskAssesmentArray = ["Hard Hat","Safety Shoes / Boots","Flame Resistant Cloting","Foul Weather Gear","Fall Protection","Hearing Protection - Single","Hearing Protection - Double","SCBA","Dust Mask","Safety Glasses","Face Shield","Goggles","Gloves - Cotton","Gloves - Leather","Golves - Impact Protection","Golves - Other","Golves - Chemical","Chemical Suit","Apron","Gloves - Impact Protection","Other PPE"]
        assView?.layer.borderWidth = 1.0
        assView?.layer.cornerRadius = 10.0
        assView?.layer.borderColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0).cgColor
        //        eitherLbl.layer.cornerRadius = 10.0
        //
        riskAssesmentTableView.layer.masksToBounds = true
        //        riskAssesmentTableView.layer.borderColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0).cgColor
        //        riskAssesmentTableView.layer.borderWidth = 1.0
        //        riskAssesmentTableView.layer.cornerRadius = 10.0
        
        //  assView?.layer.borderColor = (UIColor(red: 64/255.0, green: 75/255.0, blue: 110/255.0, alpha: 1.0) as! CGColor)
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool)
    {
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                isJsaPreview = true
                modifySwitch?.isUserInteractionEnabled = false
                continueSwitch?.isUserInteractionEnabled = false
                riskAssesmentTableView.allowsSelection = false
                
            }
            else if val == "false"
            {
                isJsaPreview = false
                modifySwitch?.isUserInteractionEnabled = true
                continueSwitch?.isUserInteractionEnabled = true
                riskAssesmentTableView.allowsSelection = true
            }
        }
        else
        {
            isJsaPreview = false
            modifySwitch?.isUserInteractionEnabled = false
            continueSwitch?.isUserInteractionEnabled = false
        }
        
        if JSAObject.status.lowercased() == "approved"
        {
            isJsaApproved = true
            
        }
        else
        {
            isJsaApproved = false
        }
        
        if let val = UserDefaults.standard.array(forKey: "RiskAssesmentViewControllerTable") as? [[Int]]
        {
            //localArray = val
            topSwitch = UserDefaults.standard.array(forKey: "RiskAssesmentViewControllerSwitch") as! [Int]
        }
        else
        {
            topSwitch = [0, 0]
            
            for value in items
            {
                var arr = [Int]()
                for _ in value
                {
                    arr.append(0)
                }
                localArray.append(arr)
            }
        }
        
        if topSwitch[0] == 1
        {
            modifySwitch?.isOn = true
        }
        else
        {
            modifySwitch?.isOn = false
        }
        
        if topSwitch[1] == 1
        {
            continueSwitch?.isOn = true
        }
        else
        {
            continueSwitch?.isOn = false
        }
        DispatchQueue.main.async {
            self.riskAssesmentTableView.reloadData()
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillShowNotification , object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillHideNotification , object: nil)
    }
    
    func userAlreadyExist(kUsernameKey: String) -> Bool {
        return UserDefaults.standard.object(forKey: kUsernameKey) != nil
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        // return items.count
        return 2
    }
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if section == 0{
            return 1
        }
        else {
            return 10
        }
    }
    
    //    public func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String?
    //    {
    //        if section < sectionArray.count {
    //            return sectionArray[section]
    //        }
    //
    //        return nil
    //    }
    
    public func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        if section == 0
        {
            return 0.0
        }
        else
        {
            return 30.0
        }
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:RiskAssessmentCell = (tableView.dequeueReusableCell(withIdentifier: "RiskAssessmentCell") as! RiskAssessmentCell?)!
        cell.selectionStyle = .none
        cell.delegate = self
        cell.sender = self
        
        if isJsaPreview || isJsaApproved
        {
            cell.riskView.isUserInteractionEnabled = false
        }
        else
        {
            cell.riskView.isUserInteractionEnabled = true
        }
        
//        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
//        {
//            if val == "true"{
//                cell.riskView.isUserInteractionEnabled = false
//            }
//        }
        if indexPath.section == 0{
            cell.riskView.tag = 0
            cell.cellHeightConstraint.constant = 200
        }
        else{
            
            if isJsaPreview || isJsaApproved
            {
                cell.riskView.isUserInteractionEnabled = false
            }
            else
            {
                cell.riskView.isUserInteractionEnabled = true
            }
            
//            if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
//            {
//                if val == "true"{
//                    cell.riskView.isUserInteractionEnabled = false
//                }
//            }
            cell.riskView.tag = indexPath.row + 1
            if indexPath.row == 0{
                cell.cellHeightConstraint.constant = 90
            }
            else if indexPath.row == 1{
                cell.cellHeightConstraint.constant = 165
            }
            else if indexPath.row == 2{
                cell.cellHeightConstraint.constant = 130
            }
            else if indexPath.row == 3{
                cell.cellHeightConstraint.constant = 135
            }
            else if indexPath.row == 4{
                cell.cellHeightConstraint.constant = 90
            }
            else if indexPath.row == 5{
                cell.cellHeightConstraint.constant = 45
            }
            else if indexPath.row == 6{
                cell.cellHeightConstraint.constant = 260
            }
            else if indexPath.row == 7{
                cell.cellHeightConstraint.constant = 90
            }
            else if indexPath.row == 8{
                cell.cellHeightConstraint.constant = 45
            }
            else{
                cell.cellHeightConstraint.constant = 45
            }
        }
        cell.setData()
        //        cell.selectionStyle = UITableViewCellSelectionStyle.none
        //        cell.nameLabel?.text = self.items[indexPath.section][indexPath.row]
        //        let str = localArray[indexPath.section][indexPath.row]
        //        if str == 1
        //        {
        //            cell.checkButton.setImage(UIImage(named:"checked"), for: .normal)
        //        }
        //        else
        //        {
        //            cell.checkButton.setImage(UIImage(named:"unchecked"), for: .normal)
        //        }
        //        cell.checkButton?.addTarget(self, action: #selector(buttonSelected(_:)), for: .touchUpInside)
        //
        //        if isJsaPreview
        //        {
        //            cell.isUserInteractionEnabled = false
        //        }
        //        else
        //        {
        //            cell.isUserInteractionEnabled = true
        //        }
        
        return cell
    }
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
        let label = UILabel(frame: CGRect(x: 16, y: -8, width: headerView.frame.size.width, height: 45))
        label.text = "PPE-REQUIRED DURING THIS TASK"
        label.font = UIFont.boldSystemFont(ofSize: 16)
        //label.textColor = UIColor.darkGray
        //        headerView.backgroundColor = UIColor(red: 105/255.0, green: 142/255.0, blue: 192/255.0, alpha: 1.0)
        
        headerView.backgroundColor = UIColor(named: "BlackColor")//UIColor(red: 242/255.0, green: 242/255.0, blue: 242/255.0, alpha: 1.0)
        headerView.addSubview(label)
        
        return headerView
    }
    
    @IBAction func SwitchModify(_ sender: UISwitch)
    {
        if (self.modifySwitch?.isOn)!
        {
            switchModifyStatus = true
            //switchStatus = 1
        }
        else
        {
            switchModifyStatus = false
            //switchStatus = 0
        }
    }
    @IBAction func SwitchContinue(_ sender: UISwitch)
    {
        if (self.continueSwitch?.isOn)!
        {
            switchContinueStatus = true
            //switchStatus = 1
        }
        else
        {
            switchContinueStatus = false
            //switchStatus = 0
        }
    }
    
    @objc func buttonSelected(_ sender: UIButton)
    {
        var indexPath : IndexPath?
        if let cell = sender.superview?.superview as? RiskAssesmentTableViewCell {
            indexPath = riskAssesmentTableView.indexPath(for: cell)
        }
        
        if (localArray[(indexPath?.section)!][(indexPath?.row)!] == 0) {
            sender.setImage(UIImage (named: "checked"), for: .normal)
            //let index = sender.indexPath
            localArray[(indexPath?.section)!][(indexPath?.row)!] = 1
            selectStatus = true
        }else {
            
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
            localArray[(indexPath?.section)!][(indexPath?.row)!] = 0
            selectStatus = false
        }
    }
    
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        JSAObject = JSA()
        //self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func previousBtn(_ sender: UIButton)
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    override func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
    @IBAction func nextBtn(_ sender: UIButton)
    {
        var flag = true
        if JSAObject.riskAssesment.hardHat == 0
        {
            flag = false
            let alert = UIAlertController(title: "", message: "Hard Hat is mandatory", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        if JSAObject.riskAssesment.safetyGlasses == 0
        {
            flag = false
            let alert = UIAlertController(title: "", message: "Safety Glasses is mandatory", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        if JSAObject.riskAssesment.safetyShoes == 0
        {
            flag = false
            let alert = UIAlertController(title: "", message: "Safety Shoes is mandatory", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        if JSAObject.riskAssesment.flameResistantClothing == 0
        {
            flag = false
            let alert = UIAlertController(title: "", message: "Flame Resistant Clothing is mandatory", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
            
        }
//        if !userAlreadyExist(kUsernameKey: "RISKASSOFFLINEARRAY")
//        {
//            riskAssOFFLineArray += [switchStatus]
//            print("RISKASSOFFLINEARRAY",riskAssOFFLineArray)
        //}
        //        let headerService = ZJSARISKASSSESService(context: self.context)
        //        headerService.deleteAll()
        //        headerService.saveChanges()
        //        _ = headerService.create(permitNumber: 35, mustModifyExistingWorkPractice: switchModifyStatus , hasContinuedRisk: switchContinueStatus)
        //
        
//        UserDefaults.standard.set(localArray, forKey: "RiskAssesmentViewControllerTable")
//        UserDefaults.standard.set(topSwitch, forKey: "RiskAssesmentViewControllerSwitch")
//        UserDefaults.standard.synchronize()
    
    
        if flag == true
        {
            let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "PotentialHazardViewController") as! PotentialHazardViewController
            self.navigationController?.pushViewController(dashBoardVC, animated: true)
        }
  
    }
}

extension RiskAssesmentViewController
{
    
    @objc func keyboardWillShow(notification: NSNotification) {
        
        
        guard let keyboardFrame = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue else {
            return
        }
        
        let keyboardHeight: CGFloat
        if #available(iOS 11.0, *) {
            keyboardHeight = keyboardFrame.cgRectValue.height - self.view.safeAreaInsets.bottom
        } else {
            keyboardHeight = keyboardFrame.cgRectValue.height
        }
        
        let finalHeight = keyboardHeight - 20
        
        let contentInsets = UIEdgeInsets(top: 0.0, left: 0.0, bottom: finalHeight, right: 0.0)
        
        riskAssesmentTableView.contentInset = contentInsets
        
//        if let info = notification.userInfo, let keyboardSize = (info[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue.size {
//            // Need to calculate keyboard exact size due to Apple suggestions
//
//
//            let finalHeight = keyboardSize.height - 20
//
//            let contentInsets = UIEdgeInsetsMake(0.0, 0.0, finalHeight, 0.0)
//
//            riskAssesmentTableView.contentInset = contentInsets
//
//        }
    }
    
    // Resize Table if keyboard hide notification comes
    @objc func keyboardWillHide(notification: NSNotification) {
        // Once keyboard disappears, restore original positions
        let contentInsets = UIEdgeInsets(top: 0.0, left: 0.0, bottom: 0.0, right: 0.0)
        riskAssesmentTableView.contentInset = contentInsets
    }
}


extension Date {
    /// Returns the amount of years from another date
    func years(from date: Date) -> Int {
        return Calendar.current.dateComponents([.year], from: date, to: self).year ?? 0
    }
    /// Returns the amount of months from another date
    func months(from date: Date) -> Int {
        return Calendar.current.dateComponents([.month], from: date, to: self).month ?? 0
    }
    /// Returns the amount of weeks from another date
    func weeks(from date: Date) -> Int {
        return Calendar.current.dateComponents([.weekOfMonth], from: date, to: self).weekOfMonth ?? 0
    }
    /// Returns the amount of days from another date
    func days(from date: Date) -> Int {
        return Calendar.current.dateComponents([.day], from: date, to: self).day ?? 0
    }
    /// Returns the amount of hours from another date
    func hours(from date: Date) -> Int {
        return Calendar.current.dateComponents([.hour], from: date, to: self).hour ?? 0
    }
    /// Returns the amount of minutes from another date
    func minutes(from date: Date) -> Int {
        return Calendar.current.dateComponents([.minute], from: date, to: self).minute ?? 0
    }
    /// Returns the amount of seconds from another date
    func seconds(from date: Date) -> Int {
        return Calendar.current.dateComponents([.second], from: date, to: self).second ?? 0
    }
    /// Returns the amount of nanoseconds from another date
    func nanoseconds(from date: Date) -> Int {
        return Calendar.current.dateComponents([.nanosecond], from: date, to: self).nanosecond ?? 0
    }
    /// Returns the a custom time interval description from another date
    func offset(from date: Date) -> String {
        if years(from: date)   > 0 { return "\(years(from: date))y"   }
        if months(from: date)  > 0 { return "\(months(from: date))M"  }
        if weeks(from: date)   > 0 { return "\(weeks(from: date))w"   }
        if days(from: date)    > 0 { return "\(days(from: date))d"    }
        if hours(from: date)   > 0 { return "\(hours(from: date))h"   }
        if minutes(from: date) > 0 { return "\(minutes(from: date))m" }
        if seconds(from: date) > 0 { return "\(seconds(from: date))s" }
        if nanoseconds(from: date) > 0 { return "\(nanoseconds(from: date))ns" }
        return ""
    }
}

