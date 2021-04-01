//
//  JSAList.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class JSAList : NSObject ,NSCoding{
    
    @objc var jsaPermitNumber : String = ""
    @objc var localPermitInteger : String = ""
    @objc var taskDescription : String = ""
    @objc var status : String = ""
    @objc var createdBy : String = ""
    @objc var createdDate : String = ""
    @objc var permitNumber : String = ""
    @objc var HWP : String = ""
    @objc var CWP : String = ""
    @objc var CSE : String = ""
    @objc var fieldOrSite = [String]()
    @objc var lastUpdatedDate = ""
    @objc var longFormatDate = ""
    
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    init(JSON: AnyObject) {
        self.jsaPermitNumber = (JSON.value(forKey: "jsaPermitNumber") as? String)!
        
        if let temp0 = JSON["taskDescription"] as? String {
            self.taskDescription = temp0
        }
        if let temp1 = JSON["status"] as? String {
            self.status = temp1
        }
        
        if let temp2 = JSON["createdBy"] as? String {
            self.createdBy = temp2
        }
        
        if let temp2 = JSON["permitNumber"] as? String {
            self.permitNumber = temp2
        }
        
        if let temp3 = JSON["createdDate"] as? Int64 {
            self.createdDate = temp3.convertEpocToDatePTW()
        }
        
        if let temp3 = JSON["facilityorsite"] as? [String] {
            self.fieldOrSite = temp3
        }
        if let temp3 = JSON["lastUpdatedDate"] as? Int64 {
            self.lastUpdatedDate = temp3.convertEpocToDatePTW()
        }
        
        if let temp4 = JSON["ptwPermitNumber"] as? [String] {
            if temp4.count > 0{
                for each in temp4{
                    if each.contains("CSE"){
                        CSE = each
                    }
                    else if each.contains("CWP"){
                        CWP = each
                    }
                    else{
                        HWP = each
                    }
                    
                }
            }
        }
        
    }
    
    required init(coder decoder: NSCoder) {
        self.jsaPermitNumber = decoder.decodeObject(forKey: "jsaPermitNumber") as? String ?? ""
        self.localPermitInteger = decoder.decodeObject(forKey: "localPermitInteger") as? String ?? ""
        self.taskDescription = decoder.decodeObject(forKey: "taskDescription") as? String ?? ""
        self.permitNumber = decoder.decodeObject(forKey: "permitNumber") as? String ?? ""
        self.createdDate = decoder.decodeObject(forKey: "createdDate") as? String ?? ""
        self.createdBy = decoder.decodeObject(forKey: "createdBy") as? String ?? ""
        self.status = decoder.decodeObject(forKey: "status") as? String ?? ""
        self.fieldOrSite = decoder.decodeObject(forKey: "fieldOrSite") as? [String] ?? [String]()
        self.lastUpdatedDate = decoder.decodeObject(forKey: "lastUpdatedDate") as? String ?? ""
        self.longFormatDate = decoder.decodeObject(forKey: "longFormatDate") as? String ?? ""
        self.CSE = decoder.decodeObject(forKey: "CSE") as? String ?? ""
        self.CWP = decoder.decodeObject(forKey: "CWP") as? String ?? ""
        self.HWP = decoder.decodeObject(forKey: "HWP") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(jsaPermitNumber, forKey: "jsaPermitNumber")
        coder.encode(localPermitInteger, forKey: "localPermitInteger")
        coder.encode(taskDescription, forKey: "taskDescription")
        coder.encode(permitNumber, forKey: "permitNumber")
        coder.encode(createdDate, forKey: "createdDate")
        coder.encode(createdBy, forKey: "createdBy")
        coder.encode(status, forKey: "status")
        coder.encode(fieldOrSite, forKey: "fieldOrSite")
        coder.encode(lastUpdatedDate, forKey: "lastUpdatedDate")
        coder.encode(longFormatDate, forKey: "longFormatDate")
        coder.encode(CSE, forKey: "CSE")
        coder.encode(CWP, forKey: "CWP")
        coder.encode(HWP, forKey: "HWP")
        
    }
}

class PermitList : NSObject ,NSCoding{
    
    var jsaPermitNumber : String = ""
    var ptwPermitNumber : String = ""
    var status : String = ""
    var createdBy : String = ""
    var createdDate : String = ""
    var localPermitInteger : String = ""
    var fieldOrSite = [String]()
    var lastUpdatedDate = ""
    var longFormatDate = ""
    var dateValue = Date()
    @objc var permitNumber : String = ""
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    init(JSON: AnyObject) {
       // self.jsaPermitNumber = (JSON.value(forKey: "jsaPermitNumber") as? String)!
        
        if let temp0 = JSON["ptwPermitNumber"] as? String {
            self.ptwPermitNumber = temp0
        }
        if let temp0 = JSON["jsaPermitNumber"] as? String {
            self.jsaPermitNumber = temp0
        }
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["status"] as? String {
            self.status = temp1
        }
        
        if let temp2 = JSON["createdBy"] as? String {
            self.createdBy = temp2
        }
        
        if let temp3 = JSON["createdDate"] as? Int64 {
            self.createdDate = temp3.convertEpocToDatePTW()
        }
        
        if let temp3 = JSON["facilityorsite"] as? [String] {
            self.fieldOrSite = temp3
        }
        if let temp3 = JSON["lastUpdatedDate"] as? Int64 {
            self.lastUpdatedDate = temp3.convertEpocToDatePTW()
        }
    }
    
    required init(coder decoder: NSCoder) {
        self.jsaPermitNumber = decoder.decodeObject(forKey: "jsaPermitNumber") as? String ?? ""
        self.localPermitInteger = decoder.decodeObject(forKey: "localPermitInteger") as? String ?? ""
        self.ptwPermitNumber = decoder.decodeObject(forKey: "ptwPermitNumber") as? String ?? ""
        self.permitNumber = decoder.decodeObject(forKey: "permitNumber") as? String ?? ""
        self.createdDate = decoder.decodeObject(forKey: "createdDate") as? String ?? ""
        self.createdBy = decoder.decodeObject(forKey: "createdBy") as? String ?? ""
        self.status = decoder.decodeObject(forKey: "status") as? String ?? ""
        self.fieldOrSite = decoder.decodeObject(forKey: "fieldOrSite") as? [String] ?? [String]()
        self.lastUpdatedDate = decoder.decodeObject(forKey: "lastUpdatedDate") as? String ?? ""
        self.longFormatDate = decoder.decodeObject(forKey: "longFormatDate") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(jsaPermitNumber, forKey: "jsaPermitNumber")
        coder.encode(localPermitInteger, forKey: "localPermitInteger")
        coder.encode(ptwPermitNumber, forKey: "ptwPermitNumber")
        coder.encode(permitNumber, forKey: "permitNumber")
        coder.encode(createdDate, forKey: "createdDate")
        coder.encode(createdBy, forKey: "createdBy")
        coder.encode(status, forKey: "status")
        coder.encode(fieldOrSite, forKey: "fieldOrSite")
        coder.encode(lastUpdatedDate, forKey: "lastUpdatedDate")
        coder.encode(longFormatDate, forKey: "longFormatDate")
        
    }
}

extension Int64 {
    func convertEpocToDatePTW() -> String{
        return Date(timeIntervalSince1970: Double(exactly: self)!/1000).toDateFormat(.long)
    }
}
