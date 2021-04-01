//
//  TOJSAHAZARDLIFT.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDLIFT : NSObject
{
    var permitNumber : Int = 0
    var liftingEquipment : Int = 0
    var confirmEquipmentCondition : Int = 0
    var obtainApprovalForLifts : Int = 0
    var haveDocumentedLiftPlan : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["liftingEquipment"] as? Int {
            self.liftingEquipment = temp1
        }
        if let temp5 = JSON["confirmEquipmentCondition"] as? Int {
            self.confirmEquipmentCondition = temp5
        }
        if let temp5 = JSON["obtainApprovalForLifts"] as? Int {
            self.obtainApprovalForLifts = temp5
        }
        if let temp5 = JSON["haveDocumentedLiftPlan"] as? Int {
            self.haveDocumentedLiftPlan = temp5
        }
    }
}
