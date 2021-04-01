//
//  Task.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 13/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class Task : NSObject{
    
    @objc var FieldTicketNum : String?
    @objc var CreatedByID : String?
    @objc var Department : String?
    @objc var CreatedByName : String?
    @objc var CreatedDate : String?
    @objc var CreatedTime : String?
    @objc var instanceID : String?
    @objc var createdCalendarDate : Date?
    @objc var vendorID : String?
    @objc var vendorRefNum : String?
    @objc var vendorName : String?
    
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    
    init(JSON: AnyObject) {
        FieldTicketNum = JSON.value(forKey: "TaskTitle") as! String?
        CreatedByID = JSON.value(forKey: "CreatedBy") as! String?
        CreatedDate = JSON.value(forKey: "CreatedOn") as! String?
        CreatedTime = ""
        instanceID = JSON.value(forKey : "InstanceID") as! String?
    }
}
