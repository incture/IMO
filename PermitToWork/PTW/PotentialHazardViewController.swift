//
//  PotentialHazardViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 21/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class PotentialHazardViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate {
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var addButton: UIBarButtonItem!
    @IBOutlet weak var potentialHazardTableView: UITableView!
    let defaults = UserDefaults.standard
    var potentailHazardArray = [[String]]()
    var isJsaPreview = false
    var isJsaApproved = false
    var note = "Complete Task Steps / Potential Hazards / Hazard Controls PRIOR to the JSA Review"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let stringTwo = "PRIOR"
        let range = (note as NSString).range(of: stringTwo)
        let attributedText = NSMutableAttributedString.init(string: note)
        attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.red , range: range)
        noteLabel.attributedText = attributedText
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if let val = UserDefaults.standard.value(forKey: "JSAPreview") as? String
        {
            if val == "true"
            {
                navigationTitle.title = "Potential Hazards - Review"
                isJsaPreview = true
                
                
            }
            else if val == "false"
            {
                navigationTitle.title = "Potential Hazards"
                isJsaPreview = false
                
                
            }
        }
        else
        {
            navigationTitle.title = "Potential Hazards"
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
        self.potentialHazardTableView.reloadData()
        
        if isJsaPreview || isJsaApproved
        {
            addButton.isEnabled = false
            addButton.customView?.alpha = 0.0
        }
        else
        {
            addButton.isEnabled = true
            addButton.customView?.alpha = 1.0
        }
        if currentUser.isReadOnly == true{
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
        //        if let val = JSAObject.potentialHazards.count
        //        {
        //            return val
        //        }
        //        else
        //        {
        //            return 0
        //        }
        
        
        return JSAObject.potentialHazards.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 77.0
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:PotentialHazardTableViewCell = (potentialHazardTableView.dequeueReusableCell(withIdentifier: "PotentialHazardTableViewCell") as! PotentialHazardTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        
        cell.nameLabel?.text = "Task Step - \(JSAObject.potentialHazards[indexPath.row].taskStep)"
        let str = "Hazard Control - \(JSAObject.potentialHazards[indexPath.row].hazardControls)"
        cell.detailLabel?.text = str
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let VC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddNewHazardViewController") as! AddNewHazardViewController
        VC.index = indexPath.row
        self.navigationController?.pushViewController(VC, animated: true)
    }
    
    @IBAction func backButton(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        JSAObject = JSA()
        creatingTemplate = false
        //self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func addNewHazard(_ sender: Any)
    {
        let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "AddNewHazardViewController") as! AddNewHazardViewController
        self.navigationController?.pushViewController(dashBoardVC, animated: true)
    }
    @IBAction func previousBtn(_ sender: UIButton)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func nextBtn(_ sender: UIButton)
    {
        
        if JSAObject.potentialHazards.count == 0
        {
            let alert = UIAlertController(title: nil, message: "Please add a potential hazard to proceed.", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        else
        {
            if #available(iOS 10.0, *) {
                let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "HazardVCViewController") as! HazardVCViewController
                self.navigationController?.pushViewController(dashBoardVC, animated: true)
            } else {
                // Fallback on earlier versions
            }
        }
        
        
        
        
    }
    
}
