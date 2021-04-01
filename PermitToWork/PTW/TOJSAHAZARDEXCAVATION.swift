//
//  TOJSAHAZARDEXCAVATION.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDEXCAVATION : NSObject
{
    var permitNumber : String = ""
    var excavations : Int = 0
    var haveExcavationPlan : Int = 0
    var locatePipesByHandDigging : Int = 0
    var deEnergizeUnderground : Int = 0
    var cseControls : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["excavations"] as? Int {
            self.excavations = temp1
        }
        if let temp5 = JSON["haveExcavationPlan"] as? Int {
            self.haveExcavationPlan = temp5
        }
        if let temp5 = JSON["locatePipesByHandDigging"] as? Int {
            self.locatePipesByHandDigging = temp5
        }
        if let temp5 = JSON["deEnergizeUnderground"] as? Int {
            self.deEnergizeUnderground = temp5
        }
        if let temp5 = JSON["cseControls"] as? Int {
            self.cseControls = temp5
        }
    }
}
