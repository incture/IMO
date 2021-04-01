//
//  UserDataModel.swift
//  Task-Management
//
//  Created by Naveen Kumar K N on 16/12/20.
//  Copyright © 2020 SAP. All rights reserved.
//

import Foundation

struct UserData{
    var name : String?
    var email : String?
    var appVersion : String?
    var osVersion : String?
    var iPhoneModel : String?
    var loginDate : String?
    var bundleId : String?
    init(name:String,email:String,appV:String,osV:String,iPHModel:String,logDate:String,bundleId:String) {
        self.name = name
        self.email = email
        self.appVersion = appV
        self.osVersion = osV
        self.iPhoneModel = iPHModel
        self.loginDate = logDate
        self.bundleId = bundleId
    }
    func toAnyObject() -> Any {
      return [
        "Name": name,
        "Email": email,
        "BundleId" : bundleId,
        "App_Version": appVersion,
        "OS_Version" : osVersion,
        "iPhone_Model" : iPhoneModel,
        "Login_Date" : loginDate
      ]
    }
}
