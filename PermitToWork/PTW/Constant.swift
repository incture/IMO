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
    
    static var apiURL = ConstantServer.qaURL  //Change 1
    static var docURL = ConstantServer.qaDocURL  //Chnage 2
    static var alanUrl = ConstantServer.alanDev   //Chnage 3
    static var socketURL = ConstantServer.socketUrldev
    static var touchLessKey = ConstantServer.devtouchless
    static var touchlessUrl = ConstantServer.devUrlTouch

}

struct ConstantServer {
    
    static var devURL = "https://mobile-d998e5467.us2.hana.ondemand.com"//MCD
    static var qaURL = "https://mobile-d7e367960.us2.hana.ondemand.com"//MCQ
    static var prodURL = "https://mobile-dee8964f1.us2.hana.ondemand.com"//MCP
    static var iodURL = "https://mobile-dfe0918b2.us2.hana.ondemand.com"//IOD
    
    static var enchancementDevURL = "https://mobile-hi61zfjqlt.us2.hana.ondemand.com"//MED
    static var enhancementQaURL = "https://mobile-zqc750d9c2.us2.hana.ondemand.com"//MEQ
    
    //Document Service URL's
    static var qaDocURL = "https://dmsappd7e367960.us2.hana.ondemand.com/AppDownload/murphy/documents/"//MCD and MCQ
    static var prodDocURL = "https://dmsappdee8964f1.us2.hana.ondemand.com/AppDownload/murphy/documents/"//MCD
    
    //Alan Config URL's
    static let alanDev = "1cd32d2938ac4e3e8fb358883c9f3b3b2e956eca572e1d8b807a3e2338fdd0dc/stage"
    static let alanProd = "1cd32d2938ac4e3e8fb358883c9f3b3b2e956eca572e1d8b807a3e2338fdd0dc/prod"
    
    static let socketUrldev = "wss://taskmanagementrestd998e5467.us2.hana.ondemand.com/TaskManagement_Rest/refreshAccessToken"
    static let socketUrlmed = "wss://taskmanagementresthi61zfjqlt.us2.hana.ondemand.com/TaskManagement_Rest/refreshAccessToken"
    static let socketUrlQa = "wss://taskmanagementrestd7e367960.us2.hana.ondemand.com/TaskManagement_Rest/refreshAccessToken"
    
    //Touchless API key
    static let qaTouchless = "AIzaSyCrVOQJWxMSFlnXnXmgKA7o5Dp4FIIMM5M"
    static let devtouchless = "AIzaSyCGL5svYm6kEOqB54AXB1PWOYGsKVA5AWw"
    
    //Touchless API Urls
    
    static let devUrlTouch = "https://dialogflow.googleapis.com/v2/projects/taskmanagement-dev-ogrc/agent/sessions/"
    static let qaUrlTouch = "https://dialogflow.googleapis.com/v2/projects/taskmanagement-qa-lptb/agent/sessions/"

    
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
