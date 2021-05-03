//
//  UserDetail.swift
//  Task-Management
//
//  Created by Soumya Singh on 26/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class UserDetail: UIViewController, UITableViewDataSource, UITableViewDelegate{

    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var userView: UITableView!
    var user : People?
    var contact : String?
    //rajat chnaged to Int
    var permitNumber : Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        backButton.setImage(UIImage(named : "back")?.withRenderingMode(.alwaysTemplate),for: .normal)
        backButton.tintColor = UIColor.white
        userImage.image = UIImage(named : "user")?.withRenderingMode(.alwaysTemplate)
        userImage.tintColor = UIColor.white
        let nib = UINib(nibName: "PermitCell", bundle: nil)
        userView.register(nib, forCellReuseIdentifier: "PermitCell")
        userView.tableFooterView = UIView()
        contact = (user?.contactNumber)!
        permitNumber = (user?.permitNumber)!
        userName.text = (user?.fullName)!
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onBackPress(_ sender: UIButton) {
        self.dismiss(animated: false, completion: nil)
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return 2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "PermitCell")! as! PermitCell
        cell.clipsToBounds = true
        cell.isUserInteractionEnabled = false
        if indexPath.row == 0{
            cell.setData(key: "Permit Number", value: String(describing: permitNumber!) )
        }
        else{
            cell.setData(key: "Contact Number", value: contact! )
            //cell.valueTextField.placeholder = "-"
        }
        return cell
    }

}


