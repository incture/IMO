//
//  TOJSAHAZARDPRESS.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDPRESS : NSObject
{
    var permitNumber : Int = 0
    var presurizedEquipment : Int = 0
    var performIsolation : Int = 0
    var depressurizeDrain : Int = 0
    var relieveTrappedPressure : Int = 0
    var doNotWorkInLineOfFire : Int = 0
    var anticipateResidual : Int = 0
    var secureAllHoses : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["presurizedEquipment"] as? Int {
            self.presurizedEquipment = temp1
        }
        if let temp5 = JSON["performIsolation"] as? Int {
            self.performIsolation = temp5
        }
        if let temp5 = JSON["depressurizeDrain"] as? Int {
            self.depressurizeDrain = temp5
        }
        if let temp5 = JSON["relieveTrappedPressure"] as? Int {
            self.relieveTrappedPressure = temp5
        }
        if let temp5 = JSON["doNotWorkInLineOfFire"] as? Int {
            self.doNotWorkInLineOfFire = temp5
        }
        if let temp5 = JSON["anticipateResidual"] as? Int {
            self.anticipateResidual = temp5
        }
        if let temp5 = JSON["secureAllHoses"] as? Int {
            self.secureAllHoses = temp5
        }
    }
}
