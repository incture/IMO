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
// MARK: - Downloader
//class Downloader : NSObject, URLSessionDownloadDelegate {
//    
//    var url : URL?
//    var destinationUrl: String?
//    var serverVersion : Int?
//    // will be used to do whatever is needed once download is complete
////    var yourOwnObject : FieldGuideHomeController?
////
////    init(_ yourOwnObject : FieldGuideHomeController)
////    {
////        self.yourOwnObject = yourOwnObject
////    }
////
////    //is called once the download is complete
////    func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didFinishDownloadingTo location: URL)
////    {
////        //copy downloaded data to your documents directory with same names as source file
////        let documentsUrl =  FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first
////        let destinationUrl = documentsUrl!.appendingPathComponent(self.destinationUrl ?? (url?.lastPathComponent)!)
////        let dataFromURL = NSData(contentsOf: location)
////        dataFromURL?.write(to: destinationUrl, atomically: true)
////        UserDefaults.standard.set(serverVersion, forKey: "PDFVersion")
////        UserDefaults.standard.synchronize()
////        //now it is time to do what is needed to be done after the download
////        yourOwnObject!.downloadCompleted()
////    }
//    
//    //this is to track progress
//    private func URLSession(session: URLSession, downloadTask: URLSessionDownloadTask, didWriteData bytesWritten: Int64, totalBytesWritten: Int64, totalBytesExpectedToWrite: Int64)
//    {
//        print("totalsize: \(totalBytesExpectedToWrite) \n bytes downloaded: \(totalBytesWritten)")
//    }
//    
////    // if there is an error during download this will be called
////    func urlSession(_ session: URLSession, task: URLSessionTask, didCompleteWithError error: Error?)
////    {
////        if(error != nil)
////        {
////            //handle the error
////            print("Download completed with error: \(error!.localizedDescription)");
////            yourOwnObject!.downloadCompleted()
////        }
////    }
//    
//    //method to be called to download
//    func download(url: URL, destination: String, serverVer:Int)
//    {
//        self.url = url
//        self.destinationUrl = destination
//        self.serverVersion = serverVer
//        
//        //download identifier can be customized. I used the "ulr.absoluteString"
//        let sessionConfig = URLSessionConfiguration.background(withIdentifier: url.absoluteString)
//        let session = Foundation.URLSession(configuration: sessionConfig, delegate: self, delegateQueue: nil)
//        let task = session.downloadTask(with: url)
//        task.resume()
//    }
//    
//}

//Bundle ids
// Dev - com.incture.dev.Task-Management
// QA - com.incture.iop.qa
//Prod - com.incture.iop.prod


//GetLoggedInUser_Dest
//JavaAPI_Dest
//ODataProvisioning_Dest
//UserManagement_Dest

// URL Identifier :-
//  com.murphy.iop
//  com.murphy.iopqa

// URL Schemes :-
// iop
// iopqa
