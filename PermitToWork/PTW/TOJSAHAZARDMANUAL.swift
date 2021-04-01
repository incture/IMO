//
//  TOJSAHAZARDMANUAL.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDMANUAL : NSObject
{
    var permitNumber : Int = 0
    var manualHandling : Int = 0
    var assessManualTask : Int = 0
    var limitLoadSize : Int = 0
    var properLiftingTechnique : Int = 0
    var confirmStabilityOfLoad : Int = 0
    var getAssistanceOrAid : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["manualHandling"] as? Int {
            self.manualHandling = temp1
        }
        if let temp5 = JSON["assessManualTask"] as? Int {
            self.assessManualTask = temp5
        }
        if let temp5 = JSON["limitLoadSize"] as? Int {
            self.limitLoadSize = temp5
        }
        if let temp5 = JSON["properLiftingTechnique"] as? Int {
            self.properLiftingTechnique = temp5
        }
        if let temp5 = JSON["confirmStabilityOfLoad"] as? Int {
            self.confirmStabilityOfLoad = temp5
        }
        if let temp5 = JSON["getAssistanceOrAid"] as? Int {
            self.getAssistanceOrAid = temp5
        }
    }
}
