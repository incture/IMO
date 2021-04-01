//
//  PermitHeader.swift
//  Murphy_PWT_iOS
//
//  Created by Soumya Singh on 11/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class PermitHeader : NSObject, NSCoding {
    
    var permitNo : Int = 0
    var plannedDateTime : String = ""
    var startTime : String = ""
    var location : String = ""
    var permitHolder : String = ""
    var contractorPerformingWork : String = ""
    var estimatedTimeOfCompletion : String = ""
    var equipmentID : String = ""
    var workOrderNumber : String = ""
    var DateValue : String = ""
    var status : String = ""
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    required init(coder decoder: NSCoder) {
        self.permitNo = decoder.decodeInteger(forKey: "permitNo")
        self.plannedDateTime = decoder.decodeObject(forKey: "plannedDateTime") as? String ?? ""
        self.location = decoder.decodeObject(forKey: "location") as? String ?? ""
        self.startTime = decoder.decodeObject(forKey: "startTime") as? String ?? ""
        self.permitHolder = decoder.decodeObject(forKey: "permitHolder") as? String ?? ""
        self.contractorPerformingWork = decoder.decodeObject(forKey: "contractorPerformingWork") as? String ?? ""
        self.estimatedTimeOfCompletion = decoder.decodeObject(forKey: "estimatedTimeOfCompletion") as? String ?? ""
        self.equipmentID = decoder.decodeObject(forKey: "equipmentID") as? String ?? ""
        self.workOrderNumber = decoder.decodeObject(forKey: "workOrderNumber") as? String ?? ""
        self.DateValue = decoder.decodeObject(forKey: "DateValue") as? String ?? ""
        self.status = decoder.decodeObject(forKey: "status") as? String ?? ""
    }

    func encode(with coder: NSCoder) {
        coder.encode(permitNo, forKey: "permitNo")
        coder.encode(plannedDateTime, forKey: "plannedDateTime")
        coder.encode(location, forKey: "location")
        coder.encode(startTime, forKey: "startTime")
        coder.encode(permitHolder, forKey: "permitHolder")
        coder.encode(contractorPerformingWork, forKey: "contractorPerformingWork")
        coder.encode(estimatedTimeOfCompletion, forKey: "estimatedTimeOfCompletion")
        coder.encode(equipmentID, forKey: "equipmentID")
        coder.encode(workOrderNumber, forKey: "workOrderNumber")
        coder.encode(DateValue, forKey: "DateValue")
        coder.encode(status, forKey: "status")
    }
    
    init(JSON: AnyObject) {
       
    }
    
    
    
}
