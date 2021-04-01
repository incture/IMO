//
//  TOPTWHEADER.swift
//  Task-Management
//
//  Created by Soumya Singh on 25/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation

class TOPTWHEADER : NSObject{
    
    var permitNumber : Int = 0
    var ptwPermitNumber : String = ""
    var isCWP : Int = 0
    var isHWP : Int = 0
    var isCSE : Int = 0
    var plannedDateTime : String = ""
    var createdBy : String = ""
    var location : String = ""
    var contractorPerformingWork : String = ""
    var estimatedTimeOfCompletion : String = ""
    var equipmentID : String = ""
    var workOrderNumber : String = ""
    var status : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["ptwPermitNumber"] as? String {
            self.ptwPermitNumber = temp1
        }
        if let temp5 = JSON["plannedDateTime"] as? Int64 {
            self.plannedDateTime = temp5.convertEpocToDatePTW()
        }
        if let temp6 = JSON["createdBy"] as? String {
            self.createdBy = temp6
        }
        if let temp6 = JSON["location"] as? String {
            self.location = temp6
        }
        if let temp6 = JSON["contractorPerformingWork"] as? String {
            self.contractorPerformingWork = temp6
        }
        if let temp6 = JSON["estimatedTimeOfCompletion"] as? Int64 {
            self.estimatedTimeOfCompletion = temp6.convertEpocToDatePTW()
        }
        if let temp6 = JSON["equipmentID"] as? String {
            self.equipmentID = temp6
        }
        if let temp7 = JSON["workOrderNumber"] as? String {
            self.workOrderNumber = temp7
        }
        if let temp8 = JSON["status"] as? String {
            self.status = temp8
        }
        if let temp9 = JSON["isCWP"] as? Int {
            self.isCWP = temp9
        }
        if let temp10 = JSON["isHWP"] as? Int {
            self.isHWP = temp10
        }
        if let temp11 = JSON["isCSE"] as? Int {
            self.isCSE = temp11
        }
        
    }
}
