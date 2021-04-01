//
//  TOJSAHAZARDMOBILE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDMOBILE : NSObject
{
    var permitNumber : String = ""
    var mobileEquipment : Int = 0
    var assessEquipmentCondition : Int = 0
    var controlAccess : Int = 0
    var monitorProximity : Int = 0
    var manageOverheadHazards : Int = 0
    var adhereToRules : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["mobileEquipment"] as? Int {
            self.mobileEquipment = temp1
        }
        if let temp5 = JSON["assessEquipmentCondition"] as? Int {
            self.assessEquipmentCondition = temp5
        }
        if let temp5 = JSON["controlAccess"] as? Int {
            self.controlAccess = temp5
        }
        if let temp5 = JSON["monitorProximity"] as? Int {
            self.monitorProximity = temp5
        }
        if let temp5 = JSON["manageOverheadHazards"] as? Int {
            self.manageOverheadHazards = temp5
        }
        if let temp5 = JSON["adhereToRules"] as? Int {
            self.adhereToRules = temp5
        }
    }
}
