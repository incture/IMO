//
//  CreatePermitViewController.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 22/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class CreatePermitViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate{
    
    
    @IBOutlet weak var createPermitTableView: UITableView!
    
    var createPermitArray = [String]()
    var selectStatus = [true, false,false]
    var senderTag : Int = 0
    override func viewDidLoad() {
        super.viewDidLoad()
        createPermitArray = ["Cold Work Permit","Hot Work Permit","Confined Space Permit"]
        
        if JSAObject.hasCWP == 1
        {
             
        }
        else
        {
            JSAObject.isCWP = 1
        }
        
        
    }
    
    public func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return createPermitArray.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell:CreatePermitTableViewCell = (createPermitTableView.dequeueReusableCell(withIdentifier: "CreatePermitTableViewCell") as! CreatePermitTableViewCell?)!
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        if indexPath.row == 0{
            cell.checkButton.setImage(UIImage(named : "checked"), for: .normal)
            cell.isUserInteractionEnabled = false
        }
        else if indexPath.row == 1{
            
            if JSAObject.hasHWP == 1
            {
                cell.checkButton.setImage(UIImage(named : "checked"), for: .normal)
                cell.isUserInteractionEnabled = false
            }
        }
        else
        {
            
            if JSAObject.hasCSP == 1
            {
                cell.checkButton.setImage(UIImage(named : "checked"), for: .normal)
                cell.isUserInteractionEnabled = false
            }
        }
        
        cell.nameLabel?.text = createPermitArray[indexPath.row]
        cell.checkButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
        cell.checkButton.tag = indexPath.row
        return cell
    }
    
    @objc func buttonSelected(_ sender: AnyObject)
    {
        if sender.tag == 0 {
            sender.setImage(UIImage (named: "checked"), for: .normal)
            if JSAObject.hasCWP == 0
            {
                JSAObject.isCWP = 1
            }
            
        }
        else if sender.tag == 1 {
            
            if JSAObject.hasHWP != 1
            {
                if !selectStatus[sender.tag]{
                    sender.setImage(UIImage (named: "checked"), for: .normal)
                    selectStatus[sender.tag] = true
                    JSAObject.isHWP = 1
                }
                else{
                    sender.setImage(UIImage (named: "unchecked"), for: .normal)
                    selectStatus[sender.tag] = false
                    JSAObject.isHWP = 0
                }
            }
            else
            {
                
            }
            
        }
        else
        {
            if JSAObject.hasCSP != 1
            {
                if !selectStatus[sender.tag]{
                    sender.setImage(UIImage (named: "checked"), for: .normal)
                    selectStatus[sender.tag] = true
                    JSAObject.isCSP = 1
                }
                else{
                    sender.setImage(UIImage (named: "unchecked"), for: .normal)
                    selectStatus[sender.tag] = false
                    JSAObject.isCSP = 0
                }
            }
            else
            {
                
            }
        }
        
    }
    
    
    @IBAction func onNotNowPress(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        //self.navigationController?.popToRootViewController(animated: true)
    }
    
    @IBAction func OnCreatePress(_ sender: UIButton)
    {
        print("indexpath",senderTag)
        
        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
        if JSAObject.isCWP == 1
        {
            JSAObject.currentFlow = .CWP
            self.navigationController?.pushViewController(vc, animated: true)
        }
        else if JSAObject.isHWP == 1
        {
            JSAObject.currentFlow = .HWP
            self.navigationController?.pushViewController(vc, animated: true)
        }
        else if JSAObject.isCSP == 1
        {
            JSAObject.currentFlow = .CSEP
            self.navigationController?.pushViewController(vc, animated: true)
        }
        else
        {
            let alert = UIAlertController(title: "", message: "Please select a new Permit to proceed", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
            
            self.present(alert, animated: true, completion: {
                print("completion block")
            })
        }
        
//        let vc = UIStoryboard(name: "Permit", bundle: Bundle.main).instantiateViewController(withIdentifier: "PermitController") as! PermitController
//        if selectStatus[0]{
//            JSAObject.checkedCW = true
//        }
//        else{
//            JSAObject.checkedCW = false
//        }
//        if selectStatus[1]{
//            JSAObject.checkedHW = true
//        }
//        else{
//            JSAObject.checkedHW = false
//        }
//        if selectStatus[2]{
//            JSAObject.checkedCSE = true
//        }
//        else{
//            JSAObject.checkedCSE = false
//        }
//        if !JSAObject.checkedCW{
//            let alert = UIAlertController(title: "", message: "Please select Cold Work Permit", preferredStyle: .alert)
//
//            alert.addAction(UIAlertAction(title: "OK", style: .default , handler: nil))
//
//            self.present(alert, animated: true, completion: {
//                print("completion block")
//            })
//
//        }
//        else{
//            JSAObject.currentFlow = .CWP
//            self.navigationController?.pushViewController(vc, animated: true)
//        }
        
    }
}
