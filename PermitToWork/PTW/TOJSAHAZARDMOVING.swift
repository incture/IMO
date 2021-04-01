//
//  TOJSAHAZARDMOVING.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDMOVING : NSObject
{
    var permitNumber : Int = 0
    var movingEquipment : Int = 0
    var confirmMachineryIntegrity : Int = 0
    var provideProtectiveBarriers : Int = 0
    var observerToMonitorProximityPeopleAndEquipment : Int = 0
    var lockOutEquipment : Int = 0
    var doNotWorkInLineOfFire : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["movingEquipment"] as? Int {
            self.movingEquipment = temp1
        }
        if let temp5 = JSON["confirmMachineryIntegrity"] as? Int {
            self.confirmMachineryIntegrity = temp5
        }
        if let temp5 = JSON["provideProtectiveBarriers"] as? Int {
            self.provideProtectiveBarriers = temp5
        }
        if let temp5 = JSON["observerToMonitorProximityPeopleAndEquipment"] as? Int {
            self.observerToMonitorProximityPeopleAndEquipment = temp5
        }
        if let temp5 = JSON["lockOutEquipment"] as? Int {
            self.lockOutEquipment = temp5
        }
        if let temp5 = JSON["doNotWorkInLineOfFire"] as? Int {
            self.doNotWorkInLineOfFire = temp5
        }
    }
}
