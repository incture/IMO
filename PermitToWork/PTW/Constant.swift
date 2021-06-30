//
//  Constant.swift
//  IOP_iOS
//
//  Created by Parul Thakur77 on 20/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
import UIKit

var JSAObject = JSA()
var readJSAObject = ReadJSA()
var readCWObject = ReadPermit()
var currentLocation = Location()
var CWObject = ColdPermit()


var pushToPeopleListController = false
var pushToPermitController = false
struct Storyboard {
    static let Login: UIStoryboard = UIStoryboard.init(name: "Login", bundle: nil)
    static let DashBoard: UIStoryboard = UIStoryboard.init(name: "CreateJSA", bundle: nil)
    static let initialBoard: UIStoryboard = UIStoryboard.init(name: "start", bundle: nil)
    static let launcher: UIStoryboard = UIStoryboard.init(name: "launcher", bundle: nil)
    static let permit: UIStoryboard = UIStoryboard.init(name: "Permit", bundle: nil)

}
/*
 Steps To Chnage Config
 1.Change 1
 2.Change 2
 3.Chnage 3
 4.Change Tenant ID
 5.Change Bundle ID And Display Name
 */

struct BaseUrl{
    
    static var apiURL = ConstantServer.imoUrl

}

struct ConstantServer {
  

    static var imoUrl = "https://mobile-b0ot37y8l6.hana.ondemand.com"
    
}

extension UIViewController{
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}


extension String {
    
    public func isPhone()->Bool {
        if self.isAllDigits() == true {
            let phoneRegex = "[235689][0-9]{6}([0-9]{3})?"
            let predicate = NSPredicate(format: "SELF MATCHES %@", phoneRegex)
            return  predicate.evaluate(with: self)
        }else {
            return false
        }
    }
    
    private func isAllDigits()->Bool {
        let charcterSet  = NSCharacterSet(charactersIn: "+0123456789").inverted
        let inputString = self.components(separatedBy: charcterSet)
        let filtered = inputString.joined(separator: "")
        return  self == filtered
    }
    
    func utf8DecodedString()-> String {
        let data = self.data(using: .utf8)
        if let message = String(data: data!, encoding: .nonLossyASCII){
            return message
        }
        return ""
    }
    
    func utf8EncodedString()-> String {
        let messageData = self.data(using: .nonLossyASCII)
        let text = String(data: messageData!, encoding: .utf8)
        return text!
    }
}

enum PermitType : String {
    
    case CWP = "CWP"
    case HWP = "HWP"
    case CSEP = "CSEP"
    
}

enum FlowType : String {
    
    case JSA = "JSA"
    case CWP = "CWP"
    case HWP = "HWP"
    case CSEP = "CSEP"
    case noFlow = "NF"
    
}


enum ActionType : String {
    
    //Task Management Actions
    case Comment = "Comment"
    case Attachment = "Attachment"
    case NonDispatch = "NonDispatch"
    case ReturnTask = "ReturnTask"
    case InProgress = "InProgress"
    case ResolveTask = "ResolveTask"
    
    //PTW Actions
    case JSACreate = "JSACreate"
    case JSAUpdate = "JSAUpdate"
    case JSAApprove = "JSAApprove"
    case PermitUpdate = "PermitUpdate"
    case PermitApprove = "PermitApprove"
    case PermitCloseOut = "PermitCloseOut"
    
}
