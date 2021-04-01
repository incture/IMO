//
//  TOJSAHAZARDSUBS.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDSUBS : NSObject
{
    var permitNumber : Int = 0
    var hazardousSubstances : Int = 0
    var drainEquipment : Int = 0
    var followSdsControls : Int = 0
    var implementHealthHazardControls : Int = 0
    var testMaterial : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["hazardousSubstances"] as? Int {
            self.hazardousSubstances = temp1
        }
        if let temp5 = JSON["drainEquipment"] as? Int {
            self.drainEquipment = temp5
        }
        if let temp5 = JSON["followSdsControls"] as? Int {
            self.followSdsControls = temp5
        }
        if let temp5 = JSON["implementHealthHazardControls"] as? Int {
            self.implementHealthHazardControls = temp5
        }
        if let temp5 = JSON["testMaterial"] as? Int {
            self.testMaterial = temp5
        }
    }
}
