//
//  User.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import  UIKit
class User : NSObject{ //, NSCoding{
    
    var firstname : String?
    var name : String?
    var email : String?
    var lastname : String?
    
    var loginName : String?
    {
        //        didSet{
        //            if loginName != nil && loginName != ""{
        //                self.getPersonnelNumber()
        //            }
        //        }
        didSet{
            if self.loginName?.contains("_") ?? false{
                if let lName = self.loginName?.split(separator: "_").last{
                    self.loginName = String(lName)
                }
            }
        }
    }
    var personnelNumber : String?
    
    var roles : [String]?
    var apps : [String]?
    
    var fullName : String?
    var country : String = "US"
    var isReadOnly:Bool =  false
    var isCanadianUser = false
    
    override init(){
    }
    
    func getPersonnelNumber(){
        let url = URL(string: MPConfig.shared.baseURL + "XSJSDest/PM_App/getUserDetails.xsjs?sapId='\(String(describing: self.loginName ?? ""))'")
        var headers = ["Content-Type":"application/json"]
        
        var urlRequest = URLRequest(url: url!)
        urlRequest.httpMethod = "get"
        urlRequest.allHTTPHeaderFields = headers
        
        //let urlRequest = try! URLRequest(url: url!, method: .get, headers: headers)
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest, completionHandler: {(data, response, error) in
            guard let httpResponse = response as? HTTPURLResponse else {
                return
            }
            guard let data = data else{return}
            do{
                let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? JSON
                if let details = json?["personDetails"] as? [[String:String]] {
                    for each in details {
                        self.personnelNumber = each["personnelNo"]
                        print(self.personnelNumber ?? "")
                    }
                }
            }
            catch{
                
            }
        }).resume()
    }
    
}
