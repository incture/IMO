//
//  HazardVCViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 21/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

@available(iOS 10.0, *)
class HazardVCViewController: UIViewController, UITableViewDataSource,UITableViewDelegate {
    
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var addButton: UIBarButtonItem!
    @IBOutlet weak var hazardTableView: UITableView!
//     let context  = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnPrevious: UIButton!
    @IBOutlet var tableHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var rightArrowButton: UIButton!
    @IBOutlet weak var leftArrowButton: UIButton!
    
    let tableCellHeight: CGFloat = 55.0
    
    var selectStatus : Bool = false
   // var responseData = [ZJSA_HAZARDS]()
    var viewMode = true
    var dataAvailable = true
    var cardCount = 0
    var localArray = [[Int]]()
    var sectionArray = ["Pressurized Equipment", "Poor Lighting/Visibility", "Personnel","Confined Space Entry","Simultaneous Operations","Ignition Sources","Hazardous Substances","Potential Spills","Weather","High Noise","Dropped Objects","Lifting Equipment","Work At Heights","Portable Electrical Equipment","Moving Equipment","Manual Handling","Equipment And Tools","Slips, Trips and Falls","High Energy / High Voltage","Excavations","Mobile Equipment"]
    
    var hazardCategoryImageArr = ["pressurizedEquipment","poorLighting","personnel","confinedSpace","simultaneousOperations","ignitionSources","hazardousSubstances","potentialSpills","weather","highNoise","droppedObject","liftingEquipment","workatHeights","portableElectricalEquipment","movingObjects","manualHandling","equipmentandTools","slipsTripsandFalls","highEnergyHighVoltage","excavations","mobileEquipment"]
    
//    var items = [["Perform isolation - LOTO building or defeat":"", "Depressurize, drain, purge, and vent":"", "Relieve trapped pressure":"","Do not work in the line of fire":"","Anticipate residual pressure or fluids":"","Secure all hoses":""]
//        , ["Provide alternate lighting":"", "Wait or defer until visibility improves":"", "Defer until visiblity improves":"","Know distance from poles and other equipment while driving":""]
//        , ["Perform induction or training for new workers":"", "Mentor / coach/ supervise":"", "Verify competencies, skills and experience":"","Address applicable limitations(fatigue,exhaustion)":"", "Manage language barries":"","Seat belts driving":""]
//        ,["Discuss confined space entry safe work pratice":"","Conduct atmospheric testing":"","Monitor access or entry":"","Protect surfaces from inadvertent contact":"","Prohibit mobile engines near confined spaces":"","Provide Observer":"","Develop resuce plan":""]
//        ,["Follow SIMOPS matrix":"","MOC required for deviation from SIMOPS restrictions":"","Interface between groups":"","Use barriers and signs to segregate activities":"","Have permit signed by leader of affected groups":""]
//        ,["Remove, isolate, or contain combustible materials":"","Provide firefighting equipment and fire watch":"","Implement abrasive blasting controls":"","Conduct continuous gas testing":"","Bond or earth for static electricity for cathodic protection":""]
//        ,["Drain or purge equipment":"","Follow SDS controls":"","Implement health hazard controls (Lead,Asbestos, Benzene,H2S,Iron Sulfide,Sulfur Dioxide, NORM)":"","Test or analyze material":""]
//        ,["Drain equipment":"","Hoses, connections in good condition":"","Spill containment equipment":"","Have spill cleanup materials and equipment on hand":"","Restrain and isolate hoses when not in use":""]
//        ,["Implement controls for slippery surfaces":"","Heat - hydration, breaks":"","Cold - PPE, heaters":"","Lightning - tool selection,defer work":""]
//        ,["Wear correct hearing PPE":"","Manage exposure times":"","Shut down equipment":"","Use quiet tools":"","Sound barries or curtains":"","Provide or use suitable communication techniques":""]
//        ,["Use signs and barriers to restrict entry or access underwork at elevation":"","Use lifting equipment to raise tools to or from the work platform":"","Secure tools(tie-off)":""]
//        ,["Confirm lifting equipment condition and certification":"","Obtain approval for lifts over processsing equipment":"","Have a document and approved lift plan":""]
//        ,["Discuss working at heights safe practice":"","Verify fall restraint and arrest equipment certification":"","Full body harness is required":"","Locking type snaphooks must be used":""]
//        ,["Inspect equipment and tools for condition and test date currency":"","Implement continuous gas testing":"","Protect electrical leads from impact or damage":"","Identify equip. classification":""]
//        ,["Confirm machinery guard integrity":"","Provide protective barriers":"","Observer to monitor proximity of people and equipment":"","Shut down or lock out equipment":"","Do not work in the line of fire":""]
//        ,["Assess manual handling task":"","Limit load size":"","Proper lifting technique":"","Confirm stability of load and work platform":"","Get assitance or mechanical aid to avoid pinch points":""]
//        ,["Inspect equipment and tools":"","Brass tools necessary":"","Use protective guards":"","Use correct tools and equipment for task":"","Protect or remove sharp edges":"","Apply hand safety principles":""]
//        ,["Identify and shield uneven surface or projections":"","Flag hazards as necessary":"","Secure or cover cables,cords and tubing":"","Clean up liquids":"","Barricade or rope off openings and holes":""]
//        ,["Restrict access to authorized personnel only":"","Discharge equipment and make electrically dead":"","Observe safe work distances for live cables":"","Use flash burn PPE":"","Use insulated gloves,tools,and mats,GFCI's.":""]
//        ,["Have an excavation plan or safe work pratice":"","Locate underground pipes or cables by hand digging":"","De-energize underground services":"","Implement confined space entry controls":""]
//        ,["Assess equipment condition ":"","Implement controls on users or access":"","Limit and monitor proximity to live equipment on users or access":"","Manage overhead hazards":"","Adhere to road / site rules":""]]
    
    var items = [["Perform isolation - LOTO blinding or defeat", "Depressurize, drain, purge, and vent", "Relieve trapped pressure","Do not work in the line of fire","Anticipate residual pressure or fluids","Secure all hoses"]
        , ["Provide alternate lighting", "Wait or defer until visibility improves", "Defer until visibility improves","Know distance from poles and other equipment while driving"]
        , ["Perform induction or training for new workers", "Mentor / coach/ supervise", "Verify competencies, skills and experience","Address applicable limitations(fatigue, exhaustion)", "Manage language barriers","Seat belts while driving"]
        ,["Discuss confined space entry safe work pratice","Conduct atmospheric testing","Monitor access or entry","Protect surfaces from inadvertent contact","Prohibit mobile engines near confined spaces","Provide Observer","Develop rescue plan"]
        ,["Follow SIMOPS matrix","MOC required for deviation from SIMOPS restrictions","Interface between groups","Use barriers and signs to segregate activities","Have permit signed by leader of affected groups"]
        ,["Remove, isolate, or contain combustible materials","Provide firefighting equipment and fire watch","Implement abrasive blasting controls","Conduct continuous gas testing","Bond or earth for static electricity for cathodic protection"]
        ,["Drain or purge equipment","Follow SDS controls","Implement health hazard controls (Lead, Asbestos, Benzene, H2S, Iron Sulfide, Sulfur Dioxide, NORM)","Test or analyze material"]
        ,["Drain equipment","Hoses, connections in good condition","Spill containment equipment","Have spill cleanup materials and equipment on hand","Restrain and isolate hoses when not in use"]
        ,["Implement controls for slippery surfaces","Heat - hydration, breaks","Cold - PPE, heaters","Lightning - tool selection,defer work"]
        ,["Wear correct hearing PPE","Manage exposure times","Shut down equipment","Use \"quiet\" tools","Sound barries or curtains","Provide or use suitable communication techniques"]
        ,["Use signs and barriers to restrict entry or access underwork at elevation","Use lifting equipment to raise tools to or from the work platform","Secure tools(tie-off)"]
        ,["Confirm lifting equipment condition and certification","Obtain approval for lifts over processing equipment","Have a documented and approved lift plan"]
        ,["Discuss working at heights safe work practice","Verify fall restraint and arrest equipment certification","Full body harness is required","Locking type snaphooks must be used"]
        ,["Inspect equipment and tools for condition and test date currency","Implement continuous gas testing","Protect electrical leads from impact or damage","Identify equip. classification"]
        ,["Confirm machinery guard integrity","Provide protective barriers","Observer to monitor proximity of people and equipment","Shut down or lock out equipment","Do not work in the line of fire"]
        ,["Assess manual handling task","Limit load size","Proper lifting technique","Confirm stability of load and work platform","Get assistance or mechanical aid to avoid pinch points"]
        ,["Inspect equipment and tools","Brass tools necessary","Use protective guards","Use correct tools and equipment for task","Protect or remove sharp edges","Apply hand safety principles"]
        ,["Identify and shield uneven surface or projections","Flag hazards as necessary","Secure or cover cables,cords and tubing","Clean up liquids","Barricade or rope off openings and holes"]
        ,["Restrict access to authorized personnel only","Discharge equipment and make electrically dead","Observe safe work distances for live cables","Use flash burn PPE","Use insulated gloves, tools and mats, GFCI's."]
        ,["Have excavation plan or safe work pratice","Locate underground pipes or cables by hand digging","De-energize underground services","Implement confined space entry controls"]
        ,["Assess equipment condition ","Implement controls on users or access","Limit and monitor proximity to live equipment on users or access","Manage overhead hazards","Adhere to road / site rules"]]
    
    var isJsaPreview = false
    var isJsaApproved = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "Hazard Controls - Review"
                isJsaPreview = true
//                addButton.isEnabled = false
//                addButton.customView?.alpha = 0.0
                
            }
            else if val == "false"
            {
                navigationTitle.title = "Hazard Controls"
                isJsaPreview = false
//                addButton.isEnabled = true
//                addButton.customView?.alpha = 1.0
                
            }
        }
        else
        {
            navigationTitle.title = "Hazard Controls"
            isJsaPreview = false
//            addButton.isEnabled = false
//            addButton.customView?.alpha = 0.0
            
        }
        
        if JSAObject.status.lowercased() == "approved"
        {
            isJsaApproved = true
            addButton.isEnabled = false
            addButton.customView?.alpha = 0.0
            
        }
        else
        {
            isJsaApproved = false
            addButton.isEnabled = true
            addButton.customView?.alpha = 1.0
            
        }
        
        if JSAObject.hazardCategories.categories.count != 0
        {
            localArray = JSAObject.hazardCategories.categories
        }
        else
        {
            //initialising empty array
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
        
        hazardTableView.layer.masksToBounds = true
        hazardTableView.layer.borderColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0).cgColor
        hazardTableView.layer.borderWidth = 2.0
        hazardTableView.layer.cornerRadius = 10.0
        
        //gesture recognisers
        let swipeRight = UISwipeGestureRecognizer(target: self, action: #selector(handleGesture))
        swipeRight.direction = UISwipeGestureRecognizer.Direction.right
        self.view.addGestureRecognizer(swipeRight)
        
        let swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(handleGesture))
        swipeLeft.direction = UISwipeGestureRecognizer.Direction.left
        self.view.addGestureRecognizer(swipeLeft)
        let optionsArray = items[cardCount]
        let arrayCount = optionsArray
        
        UIView.animate(withDuration: 0, animations: {
            self.hazardTableView.layoutIfNeeded()
        }) { (complete) in
            var heightOfTableView: CGFloat = 0.0
            // Get visible cells and sum up their heights
            //let cells = self.hazardTableView.visibleCells
            for _ in arrayCount {
                heightOfTableView += self.tableCellHeight
            }
            // Edit heightOfTableViewConstraint's constant to update height of table view
            self.tableHeightConstraint.constant = heightOfTableView + self.tableCellHeight
        }
        
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        self.hazardTableView.reloadData()
        let optionsArray = items[cardCount]
        let arrayCount = optionsArray
        UIView.animate(withDuration: 0, animations: {
            self.hazardTableView.layoutIfNeeded()
        }) { (complete) in
            var heightOfTableView: CGFloat = 0.0
            // Get visible cells and sum up their heights
           // let cells = self.hazardTableView.visibleCells
            for _ in arrayCount {
                heightOfTableView += self.tableCellHeight
                
            }
            // Edit heightOfTableViewConstraint's constant to update height of table view
            self.tableHeightConstraint.constant = heightOfTableView + 50
        }
        if cardCount == 0 {
            self.leftArrowButton.isHidden = true
            self.rightArrowButton.isHidden = false
        } else if cardCount == sectionArray.count - 1 {
            self.leftArrowButton.isHidden = false
            self.rightArrowButton.isHidden = true
        } else {
            self.leftArrowButton.isHidden = false
            self.rightArrowButton.isHidden = false
        }
        if currentUser.isReadOnly == true{
            let button: UIButton = UIButton(type: UIButton.ButtonType.custom)
            self.addButton.customView = button
        }
    }
    
    
    
    @objc func handleGesture(gesture: UISwipeGestureRecognizer)
    {
       // if let swipeGesture = gesture as? UISwipeGestureRecognizer{
            switch gesture.direction {
            case UISwipeGestureRecognizer.Direction.right:
                cardCount -= 1
                if cardCount < 0{
                    cardCount = 0
                }
                if cardCount < sectionArray.count  && cardCount >= 0{
                    hazardTableView.reloadData()
                }
                print("right swipe")
                self.onLeftAction()
            case UISwipeGestureRecognizer.Direction.left:
                cardCount += 1
                if cardCount >= sectionArray.count - 1{
                    cardCount = sectionArray.count - 1
                }
                if cardCount < sectionArray.count && cardCount >= 0{
                    hazardTableView.reloadData()
                }
                self.onRightAction()
                print("left swipe")
            default:
                print("other swipe")
            }
        let optionsArray = items[cardCount]
        let arrayCount = optionsArray
        UIView.animate(withDuration: 0, animations: {
            self.hazardTableView.layoutIfNeeded()
        }) { (complete) in
            var heightOfTableView: CGFloat = 0.0
            // Get visible cells and sum up their heights
            //let cells = self.hazardTableView.visibleCells
            for _ in arrayCount {
                heightOfTableView += self.tableCellHeight
            }
            // Edit heightOfTableViewConstraint's constant to update height of table view
            self.tableHeightConstraint.constant = heightOfTableView + self.tableCellHeight
        }
       // }
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
        return 1
    }
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return items[cardCount].count
    }
    
    public func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String?
    {
        if section < sectionArray.count {
            return sectionArray[cardCount] //sectionArray.keys[section]
        }
        
        return nil
    }

    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:HazardTableViewCell = (hazardTableView.dequeueReusableCell(withIdentifier: "HazardTableViewCell") as! HazardTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        let optionsArray = items[cardCount]
        let textVal = optionsArray[indexPath.row]
        cell.nameLabel?.text = textVal
        cell.checkButton?.mySection = indexPath.section
        
        let str = localArray[cardCount][indexPath.row]
        if str == 1
        {
            cell.checkButton.setImage(UIImage(named:"checked"), for: .normal)
        }
        else
        {
            cell.checkButton.setImage(UIImage(named:"unchecked"), for: .normal)
        }
        
//        cell.checkButton?.setImage(UIImage (named: "checked"), for: .selected)
//        cell.checkButton?.setImage(UIImage (named: "unchecked"), for: .normal)
//
//        if (optionsArray[textVal] == "true") {
//            cell.checkButton?.isSelected = true
//        }else {
//
//            cell.checkButton?.isSelected = false
//        }
        
        cell.checkButton?.addTarget(self, action: #selector(buttonSelected(_:)), for: .touchUpInside)
        cell.checkButton.tag = indexPath.row
        
        if isJsaPreview || isJsaApproved
        {
            cell.checkButton.isUserInteractionEnabled = false
        }
        else
        {
            cell.checkButton.isUserInteractionEnabled = true
        }
        
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0{
            return tableCellHeight
        }
        else{
            return tableCellHeight
        }
    }
    

    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 55))
        let label = UILabel(frame: CGRect(x: 16, y: 4, width: headerView.frame.size.width, height: 45))
        label.text = sectionArray[cardCount]
        label.font = UIFont.boldSystemFont(ofSize: 16)
        label.textColor = UIColor.white
//        headerView.backgroundColor = UIColor(red: 105/255.0, green: 142/255.0, blue: 192/255.0, alpha: 1.0)
        
        headerView.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
        headerView.addSubview(label)
        

        let button1 = UIButton (frame: CGRect(x: headerView.frame.size.width - 55, y: 8, width: 35, height: 35))

        button1.setImage(UIImage (named: hazardCategoryImageArr[cardCount]), for: UIControl.State.normal)
        headerView.addSubview(button1)
        
        //        button.setImage(UIImage Array(hazardCategoryImageArr.keys[cardCount]), scale: .normal),
//        let button:MyButton = MyButton(frame: CGRect(x: 310, y: 15, width: 25, height: 25))
//        self.button.setImage(UIImage(named: "plus"), for: .normal)
       // headerView.addSubview(button)
//        let keyName = Array(sectionArray.keys)[cardCount]
//
//        let status = sectionArray[keyName]
//
//
//        let button:MyButton = MyButton(frame: CGRect(x: 310, y: 15, width: 20, height: 20))
//        DispatchQueue.main.async {
//            button.setImage(UIImage (named: "checked"), for: .selected)
//            button.setImage(UIImage (named: "unchecked"), for: .normal)
//
//            if (status == "true") {
//                button.isSelected = true
//            }else {
//
//                button.isSelected = false
//            }
//        }
//        button.tag = section
//        button.addTarget(self, action:#selector(headerButtonSelected), for: .touchUpInside)
//
//        headerView.addSubview(button)

        return headerView
    }


    @objc func buttonSelected(_ sender: UIButton)
    {
        
        var indexPath : IndexPath?
        if let cell = sender.superview?.superview as? HazardTableViewCell {
            indexPath = hazardTableView.indexPath(for: cell)
        }
        
        if (localArray[cardCount][(indexPath?.row)!] == 0) {
            sender.setImage(UIImage (named: "checked"), for: .normal)
            //let index = sender.indexPath
            localArray[cardCount][(indexPath?.row)!] = 1
        }else {
            
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
            localArray[cardCount][(indexPath?.row)!] = 0
        }
        
        
        print(sender.tag)
        let myBut = sender as! MyButton

        let sectionVal = myBut.mySection

        var optionsArray = items[cardCount]
        let textVal = optionsArray[sender.tag]

//        if (myBut.isSelected ==  false) {
//            //sender.setImage(UIImage (named: "checked"), for: .normal)
//            myBut.isSelected = true
//            optionsArray[textVal] = "true"
//        }else {
//            //sender.setImage(UIImage (named: "unchecked"), for: .normal)
//            myBut.isSelected = false
//            optionsArray[textVal] = "false"
//        }

//        items[sectionVal] = optionsArray
    }
    
//    @objc func headerButtonSelected(_ sender: AnyObject)
//    {
//        print(sender.tag)
//        let myBut = sender as! MyButton
//        let keyName = sectionArray[sender.tag]
//        if (myBut.isSelected ==  false) {
//            myBut.isSelected = true
//            sectionArray[keyName] = "true"
//        }else {
//            myBut.isSelected = false
//            sectionArray[keyName] = "false"
//        }
//        
//    }


    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        JSAObject = JSA()//self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func addHazardCat(_ sender: Any)
    {
        JSAObject.hazardCategories.categories = localArray
        
        let VC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "HazardCategoryViewController") as! HazardCategoryViewController
        VC.hazardCategoryArray = sectionArray
        VC.hazardControl = self
        self.navigationController?.pushViewController(VC, animated: true)
    }
    @IBAction func previousBtn(_ sender: UIButton)
    {
       self.navigationController?.popViewController(animated: true)
       JSAObject.hazardCategories.categories = localArray
    }
    @IBAction func nextBtn(_ sender: UIButton)
    {

        var flag = false
        for val in localArray
        {
            for val1 in val
            {
                if val1 == 1
                {
                    flag = true
                    break
                }
            }
            if flag == true
            {
                break
            }
        }
        
        if flag == false
        {
            let alert = UIAlertController(title: "", message: "Minimum 1 hazard have to be selected.", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler:{ (UIAlertAction)in
                print("User click Dismiss button")
            }))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            JSAObject.hazardCategories.categories = localArray
            
            let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "StopJobVCViewController") as! StopJobVCViewController
            self.navigationController?.pushViewController(dashBoardVC, animated: true)
        }
    }
    
    
    @IBAction func onRightButtonPress(_ sender: Any) {
        

        cardCount += 1
        if cardCount > sectionArray.count - 1{
            cardCount = sectionArray.count - 1
        }
        
        self.onRightAction()
        let optionsArray = items[cardCount]
        let arrayCount = optionsArray
        
        DispatchQueue.main.async {
            if self.cardCount < self.sectionArray.count - 1 && self.cardCount >= 0{
                self.hazardTableView.reloadData()
            }
        }
        
       
        
       
        UIView.animate(withDuration: 0, animations: {
            self.hazardTableView.layoutIfNeeded()
        }) { (complete) in
            var heightOfTableView: CGFloat = 0.0
            // Get visible cells and sum up their heights
            let cells = self.hazardTableView.visibleCells
            for cell in arrayCount {
                heightOfTableView += self.tableCellHeight
            }
            // Edit heightOfTableViewConstraint's constant to update height of table view
            self.tableHeightConstraint.constant = heightOfTableView + self.tableCellHeight
            self.hazardTableView.reloadData()
        }
        
    }
    
    func onRightAction() {
        
        if cardCount == sectionArray.count - 1 {
            self.rightArrowButton.isHidden = true
            self.leftArrowButton.isHidden = false
        } else {
            self.rightArrowButton.isHidden = false
            self.leftArrowButton.isHidden = false
        }
    }
    
    func onLeftAction() {
        
        if cardCount == 0 {
            self.leftArrowButton.isHidden = true
            self.rightArrowButton.isHidden = false
        } else {
            self.leftArrowButton.isHidden = false
            self.rightArrowButton.isHidden = false
        }
    }
    
    @IBAction func onLeftButtonPress(_ sender: Any) {
        
        cardCount -= 1
        if cardCount < 0{
            cardCount = 0
        }
        
        self.onLeftAction()
        
        let optionsArray = items[cardCount]
        let arrayCount = optionsArray
        if cardCount < sectionArray.count - 1 && cardCount >= 0{
            hazardTableView.reloadData()
        }
        UIView.animate(withDuration: 0, animations: {
            self.hazardTableView.layoutIfNeeded()
        }) { (complete) in
            var heightOfTableView: CGFloat = 0.0
            // Get visible cells and sum up their heights
            let cells = self.hazardTableView.visibleCells
            for cell in arrayCount {
                heightOfTableView += self.tableCellHeight
            }
            // Edit heightOfTableViewConstraint's constant to update height of table view
            self.tableHeightConstraint.constant = heightOfTableView + self.tableCellHeight
        }
    }
}

class MyButton :  UIButton {
    
    var myRow: Int = 0
    var mySection: Int = 0
}
