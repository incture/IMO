//
//  TOJSAHAZARDSPILL.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDSPILL : NSObject
{
    var permitNumber : Int = 0
    var potentialSpills : Int = 0
    var drainEquipment : Int = 0
    var connectionsInGoodCondition : Int = 0
    var spillContainmentEquipment : Int = 0
    var haveSpillCleanupMaterials : Int = 0
    var restrainHosesWhenNotInUse : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["potentialSpills"] as? Int {
            self.potentialSpills = temp1
        }
        if let temp5 = JSON["drainEquipment"] as? Int {
            self.drainEquipment = temp5
        }
        if let temp5 = JSON["connectionsInGoodCondition"] as? Int {
            self.connectionsInGoodCondition = temp5
        }
        if let temp5 = JSON["spillContainmentEquipment"] as? Int {
            self.spillContainmentEquipment = temp5
        }
        if let temp5 = JSON["haveSpillCleanupMaterials"] as? Int {
            self.haveSpillCleanupMaterials = temp5
        }
        if let temp5 = JSON["restrainHosesWhenNotInUse"] as? Int {
            self.restrainHosesWhenNotInUse = temp5
        }
    }
}
