//
//  ProfileViewController.swift
//  DFT
//
//  Created by Soumya Singh on 29/01/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import Letters

let offset_HeaderStop:CGFloat = 80.0 // At this offset the Header stops its transformations
let offset_B_LabelHeader:CGFloat = 25.0 // At this offset the Black label reaches the Header
let distance_W_LabelHeader:CGFloat = -40.0 // The distance between the bottom of the Header and the top of the White Label

class ProfileViewController: UIViewController, UIScrollViewDelegate {
    
    @IBOutlet var shadowView: UIView!
    @IBOutlet var tableView: UITableView!
    @IBOutlet var scrollView:UIScrollView!
    @IBOutlet var avatarImage:UIImageView!
    @IBOutlet var header:UIView!
    @IBOutlet var headerLabel:UILabel!
    @IBOutlet var headerImageView:UIImageView!
    @IBOutlet var headerBlurImageView:UIImageView!
    
    @IBOutlet var floatingButton: UIButton!
    @IBOutlet var backButton: UIButton!
    
    @IBOutlet var userName: UILabel!
    @IBOutlet var emailID: UILabel!
    var blurredHeaderImageView:UIImageView?
    var displayName : String?
    var vendorID : String?
    var vendorName : String?
    var vendorAddress : String?
    var vendorAdminID : String?
    var vendorEmailID : String?
    var id : String?
    var senderController : LauncherController?
    
    var isLoaded : Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let nib = UINib(nibName: "ProfileCell", bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: "ProfileCell")
        scrollView.delegate = self
        tableView.separatorStyle = .none
        userName.text = ""
        emailID.text = ""
        headerLabel.text = ""
        headerLabel.isHidden = true
        avatarImage.layer.borderWidth = 5
        avatarImage.layer.masksToBounds = false
        avatarImage.layer.borderColor = UIColor.white.cgColor
        avatarImage.layer.cornerRadius = avatarImage.frame.height/2
        avatarImage.clipsToBounds = true
        setValues()
        floatingButton.isEnabled = false
        floatingButton.isHidden = true
        floatingButton.layer.cornerRadius = 30
        shadowView.layer.cornerRadius = 10
        backButton.setImage(UIImage(named : "back")?.withRenderingMode(.alwaysTemplate), for: .normal)
        backButton.tintColor = UIColor.white
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        // Header - Image
        headerImageView = UIImageView(frame: header.bounds)
        headerImageView?.image = UIImage(named: "backImage")
        //  headerImageView.backgroundColor = UIColor.blue
        headerImageView?.contentMode = UIView.ContentMode.scaleAspectFill
        header.insertSubview(headerImageView, belowSubview: headerLabel)
        //        let image_frame = CGRect(x : 0, y : shadowView.frame.maxY, width : view.frame.width, height : shadowView.frame.height)
        //        let scrollImageView = UIImageView(frame: image_frame)
        //         scrollImageView.image = UIImage(named: "Dash1")
        //        //headerImageView.backgroundColor = UIColor.blue
        //        scrollImageView.contentMode = UIViewContentMode.scaleAspectFill
        //        scrollView.insertSubview(scrollImageView, belowSubview: shadowView)
        //        let singleTap = UITapGestureRecognizer(target: self, action: #selector(handleTap(_:)))
        //        singleTap.cancelsTouchesInView = false
        //        singleTap.numberOfTapsRequired = 1
        //        scrollView.addGestureRecognizer(singleTap)
        self.view.backgroundColor = UIColor.groupTableViewBackground
        tableView.backgroundColor = UIColor.groupTableViewBackground
        
        // Header - Blurred Image
        
        headerBlurImageView = UIImageView(frame: header.bounds)
        //     headerBlurImageView?.image = UIImage(named: "Dash1")?.blurredImage(withRadius: 10, iterations: 20, tintColor: UIColor.clear)
        headerBlurImageView?.contentMode = UIView.ContentMode.scaleAspectFill
        headerBlurImageView?.alpha = 0.0
        header.insertSubview(headerBlurImageView, belowSubview: headerLabel)
        header.clipsToBounds = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
//    @objc func handleTap(_ recognizer: UITapGestureRecognizer) {
//        
//        if recognizer.state == .ended {
//            if UserDefaults.standard.string(forKey: "digisign")! == "EMPTY"{
//                
//                let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DigitalSignatureController") as! DigitalSignatureController
//                splitViewController.isUpdate = true
//                let navController = UINavigationController(rootViewController: splitViewController)
//                self.present(navController, animated: true, completion: nil)
//            }
//            else{
//                let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ImageVerifyController") as! ImageVerifyController
//                let navController = UINavigationController(rootViewController: splitViewController)
//                self.present(navController, animated: true, completion: nil)
//            }
//        }
//        recognizer.cancelsTouchesInView = false
//        
//    }
    
    @IBAction func onBackPress(_ sender: UIButton) {
        
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func onLogoutPress(_ sender: UIButton) {
        
        let alertController = UIAlertController.init(title: "", message:"Are you sure you want to Logout?" , preferredStyle: UIAlertController.Style.alert)
        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.default, handler: { action in
            currentUser.isReadOnly = false
            currentUser.apps = nil
           // TouhlessHandler.shared.showTouchLess = false
          //  TouhlessHandler.shared.buttonEnabled = false
            self.dismiss(animated: false, completion: nil)
            self.senderController?.doubleDismiss()
            currentLocation.facilityOrSite = ""
           // locationSingle.shared.locationDictionary = nil
         
        })
        
        alertController.addAction(okAction)
        let cancelAction = UIAlertAction.init(title: "Cancel", style: UIAlertAction.Style.cancel, handler: nil)
        alertController.addAction(cancelAction)
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    func setValues()
    {
        self.id = UserDefaults.standard.string(forKey: "id") ?? ""
        self.vendorID = UserDefaults.standard.string(forKey: "vid") ?? ""
        self.vendorName = UserDefaults.standard.string(forKey: "vname") ?? ""
        self.vendorAddress = UserDefaults.standard.string(forKey: "vaddress") ?? ""
        self.vendorAdminID = UserDefaults.standard.string(forKey: "vadminid") ?? ""
        self.vendorEmailID = UserDefaults.standard.string(forKey: "vemailid") ?? ""
        self.displayName = UserDefaults.standard.string(forKey: "name") ?? ""
        self.emailID.text = UserDefaults.standard.string(forKey: "email") ?? ""
        self.userName.text = UserDefaults.standard.string(forKey: "name") ?? ""
        // self.headerLabel.text = UserDefaults.standard.string(forKey: "name")!
        self.avatarImage.setImage(string: self.displayName, color: UIColor(red : 1/255, green : 38/255, blue : 90/255, alpha : 1), circular: true, textAttributes: [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 32), NSAttributedString.Key.foregroundColor: UIColor.white])
        self.tableView.reloadData()
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        
        let offset = scrollView.contentOffset.y
        var avatarTransform = CATransform3DIdentity
        var headerTransform = CATransform3DIdentity
        headerLabel.isHidden = false
        // PULL DOWN -----------------
        
        if offset < 0 {
            
            let headerScaleFactor:CGFloat = -(offset) / header.bounds.height
            let headerSizevariation = ((header.bounds.height * (1.0 + headerScaleFactor)) - header.bounds.height)/2.0
            headerTransform = CATransform3DTranslate(headerTransform, 0, headerSizevariation, 0)
            headerTransform = CATransform3DScale(headerTransform, 1.0 + headerScaleFactor, 1.0 + headerScaleFactor, 0)
            
            header.layer.transform = headerTransform
        }
            
            // SCROLL UP/DOWN ------------
            
        else {
            
            // Header -----------
            self.headerLabel.text = UserDefaults.standard.string(forKey: "name") ?? ""
            headerTransform = CATransform3DTranslate(headerTransform, 0, max(-offset_HeaderStop, -offset), 0)
            
            //  ------------ Label
            
            let labelTransform = CATransform3DMakeTranslation(0, max(-distance_W_LabelHeader, offset_B_LabelHeader - offset), 0)
            headerLabel.layer.transform = labelTransform
            
            //  ------------ Blur
            
            headerBlurImageView?.alpha = min (1.0, (offset - offset_B_LabelHeader)/distance_W_LabelHeader)
            
            // Avatar -----------
            
            let avatarScaleFactor = (min(offset_HeaderStop, offset)) / avatarImage.bounds.height / 0.6 // Slow down the animation
            let avatarSizeVariation = ((avatarImage.bounds.height * (1.0 + avatarScaleFactor)) - avatarImage.bounds.height) / 2.0
            avatarTransform = CATransform3DTranslate(avatarTransform, 0, avatarSizeVariation, 0)
            avatarTransform = CATransform3DScale(avatarTransform, 1.0 - avatarScaleFactor, 1.0 - avatarScaleFactor, 0)
            
            if offset <= offset_HeaderStop {
                
                if avatarImage.layer.zPosition < header.layer.zPosition{
                    header.layer.zPosition = 0
                }
                
            }else {
                if avatarImage.layer.zPosition >= header.layer.zPosition{
                    header.layer.zPosition = 2
                }
            }
        }
        
        // Apply Transformations
        
        header.layer.transform = headerTransform
        avatarImage.layer.transform = avatarTransform
    }
}
    
    
//    @IBAction func onDigitalSignPress(_ sender: UIButton) {
//        if UserDefaults.standard.string(forKey: "digisign")! == "EMPTY"{
//
//            let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DigitalSignatureController") as! DigitalSignatureController
//            splitViewController.isUpdate = true
//            let navController = UINavigationController(rootViewController: splitViewController)
//            self.present(navController, animated: true, completion: nil)
//
//        }
//        else{
//
//            let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ImageVerifyController") as! ImageVerifyController
//            let navController = UINavigationController(rootViewController: splitViewController)
//            self.present(navController, animated: true, completion: nil)
//
//        }
//    }
//
//    override var preferredStatusBarStyle: UIStatusBarStyle{
//        return UIStatusBarStyle.lightContent
//    }
//}

extension ProfileViewController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            return 1
    
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileCell")! as! ProfileCell
       // if indexPath.row == 0{
            cell.setData(titleValue: "Basic Info", key1: "User ID", value1: id!, key2: "Contact Number", value2: "-", key3: "", value3: "")
        //}
        cell.selectionStyle = .none
        cell.clipsToBounds = true
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
       // if indexPath.row == 0{
            return 167
            
//        }
//        else if indexPath.row == 1 && isServiceProvider! == true{
//            return 217
//        }
//        else /*if indexPath.row == 1 && isServiceProvider! == false*/{
//            return 70
//        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        if indexPath.row == 1 && isServiceProvider! == false{
//            let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ImageVerifyController") as! ImageVerifyController
//            let navController = UINavigationController(rootViewController: splitViewController)
//            self.present(navController, animated: true, completion: nil)
//        }
    }
}

extension ProfileViewController {
    //    func scrollViewDidScroll(_ scrollView: UIScrollView) {
    //
    //
    //        let offset = scrollView.contentOffset.y
    //        var avatarTransform = CATransform3DIdentity
    //        var headerTransform = CATransform3DIdentity
    //
    //        // PULL DOWN -----------------
    //
    //        if offset < 0 {
    //
    //            let headerScaleFactor:CGFloat = -(offset) / headerView.bounds.height
    //            let headerSizevariation = ((headerView.bounds.height * (1.0 + headerScaleFactor)) - headerView.bounds.height)/2.0
    //            headerTransform = CATransform3DTranslate(headerTransform, 0, headerSizevariation, 0)
    //            headerTransform = CATransform3DScale(headerTransform, 1.0 + headerScaleFactor, 1.0 + headerScaleFactor, 0)
    //
    //            headerView.layer.transform = headerTransform
    //        }
    //
    //            // SCROLL UP/DOWN ------------
    //
    //        else {
    //
    //            // Header -----------
    //
    //            headerTransform = CATransform3DTranslate(headerTransform, 0, max(-offset_HeaderStop, -offset), 0)
    //
    //            //  ------------ Label
    //
    //            let labelTransform = CATransform3DMakeTranslation(0, max(-distance_W_LabelHeader, offset_B_LabelHeader - offset), 0)
    //        //    headerLabel.layer.transform = labelTransform
    //
    //            //  ------------ Blur
    //
    //          //  headerBlurImageView?.alpha = min (1.0, (offset - offset_B_LabelHeader)/distance_W_LabelHeader)
    //
    //            // Avatar -----------
    //
    //            let avatarScaleFactor = (min(offset_HeaderStop, offset)) / profileImageView.bounds.height / 1.4 // Slow down the animation
    //            let avatarSizeVariation = ((profileImageView.bounds.height * (1.0 + avatarScaleFactor)) - profileImageView.bounds.height) / 2.0
    //            avatarTransform = CATransform3DTranslate(avatarTransform, 0, avatarSizeVariation, 0)
    //            avatarTransform = CATransform3DScale(avatarTransform, 1.0 - avatarScaleFactor, 1.0 - avatarScaleFactor, 0)
    //
    //            if offset <= offset_HeaderStop {
    //
    //                if profileImageView.layer.zPosition < headerView.layer.zPosition{
    //                    headerView.layer.zPosition = 0
    //                }
    //
    //            }else {
    //                if profileImageView.layer.zPosition >= headerView.layer.zPosition{
    //                    headerView.layer.zPosition = 2
    //                }
    //            }
    //        }
    //
    //        // Apply Transformations
    //
    //        headerView.layer.transform = headerTransform
    //        profileImageView.layer.transform = avatarTransform
    //
    //
    //
    
    
    
    
    
    //        let scrollDiff = scrollView.contentOffset.y - self.previousScrollOffset
    //
    //        let absoluteTop: CGFloat = 0;
    //        let absoluteBottom: CGFloat = scrollView.contentSize.height - scrollView.frame.size.height;
    //
    //        let isScrollingDown = scrollDiff > 0 && scrollView.contentOffset.y > absoluteTop
    //        let isScrollingUp = scrollDiff < 0 && scrollView.contentOffset.y < absoluteBottom
    //
    //        if canAnimateHeader(scrollView) {
    //
    //            // Calculate new header height
    //            var newHeight = self.headerViewheightConstraint.constant
    //            if isScrollingDown {
    //                newHeight = max(self.minHeaderHeight, self.headerViewheightConstraint.constant - abs(scrollDiff))
    //            } else if isScrollingUp {
    //                newHeight = min(self.maxHeaderHeight, self.headerViewheightConstraint.constant + abs(scrollDiff))
    //            }
    //
    //            // Header needs to animate
    //            if newHeight != self.headerViewheightConstraint.constant {
    //                self.headerViewheightConstraint.constant = newHeight
    //                self.updateHeader()
    //                self.setScrollPosition(self.previousScrollOffset)
    //            }
    //
    //            self.previousScrollOffset = scrollView.contentOffset.y
    //        }
    //   }
    
    //    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
    //        self.scrollViewDidStopScrolling()
    //    }
    //
    //    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
    //        if !decelerate {
    //            self.scrollViewDidStopScrolling()
    //        }
    //    }
    //
    //    func scrollViewDidStopScrolling() {
    //        let range = self.maxHeaderHeight - self.minHeaderHeight
    //        let midPoint = self.minHeaderHeight + (range / 2)
    //
    //        if self.headerViewheightConstraint.constant > midPoint {
    //            self.expandHeader()
    //        } else {
    //            self.collapseHeader()
    //        }
    //    }
    //
    //    func canAnimateHeader(_ scrollView: UIScrollView) -> Bool {
    //        // Calculate the size of the scrollView when header is collapsed
    //        let scrollViewMaxHeight = scrollView.frame.height + self.headerViewheightConstraint.constant - minHeaderHeight
    //
    //        // Make sure that when header is collapsed, there is still room to scroll
    //        return scrollView.contentSize.height > scrollViewMaxHeight
    //    }
    //
    //    func collapseHeader() {
    //        self.view.layoutIfNeeded()
    //        UIView.animate(withDuration: 0.2, animations: {
    //            self.headerViewheightConstraint.constant = self.minHeaderHeight
    //            self.updateHeader()
    //            self.view.layoutIfNeeded()
    //        })
    //    }
    //
    //    func expandHeader() {
    //        self.view.layoutIfNeeded()
    //        UIView.animate(withDuration: 0.2, animations: {
    //            self.headerViewheightConstraint.constant = self.maxHeaderHeight
    //            self.updateHeader()
    //            self.view.layoutIfNeeded()
    //        })
    //    }
    //
    //    func setScrollPosition(_ position: CGFloat) {
    //        self.profileTable.contentOffset = CGPoint(x: self.profileTable.contentOffset.x, y: position)
    //    }
    //
    //    func updateHeader() {
    //        let range = self.maxHeaderHeight - self.minHeaderHeight
    //        let openAmount = self.headerViewheightConstraint.constant - self.minHeaderHeight
    //        let percentage = openAmount / range
    //
    //      //  self.titleTopConstraint.constant = -openAmount + 10
    //        self.profileImageView.alpha = percentage
    //    }
}

