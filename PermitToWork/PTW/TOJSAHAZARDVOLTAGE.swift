//
//  TOJSAHAZARDVOLTAGE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDVOLTAGE : NSObject
{
    var permitNumber : String = ""
    var highVoltage : Int = 0
    var restrictAccess : Int = 0
    var dischargeEquipment : Int = 0
    var observeSafeWorkDistance : Int = 0
    var useFlashBurn : Int = 0
    var useInsulatedGloves : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["highVoltage"] as? Int {
            self.highVoltage = temp1
        }
        if let temp5 = JSON["restrictAccess"] as? Int {
            self.restrictAccess = temp5
        }
        if let temp5 = JSON["dischargeEquipment"] as? Int {
            self.dischargeEquipment = temp5
        }
        if let temp5 = JSON["observeSafeWorkDistance"] as? Int {
            self.observeSafeWorkDistance = temp5
        }
        if let temp5 = JSON["useFlashBurn"] as? Int {
            self.useFlashBurn = temp5
        }
        if let temp5 = JSON["useInsulatedGloves"] as? Int {
            self.useInsulatedGloves = temp5
        }
    }
}
