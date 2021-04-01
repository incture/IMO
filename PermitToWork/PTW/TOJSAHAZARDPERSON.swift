//
//  TOJSAHAZARDPERSON.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDPERSON : NSObject
{
    var permitNumber : Int = 0
    var personnel : Int = 0
    var performInduction : Int = 0
    var mentorCoachSupervise : Int = 0
    var verifyCompetencies : Int = 0
    var addressLimitations : Int = 0
    var manageLanguageBarriers : Int = 0
    var wearSeatBelts : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["personnel"] as? Int {
            self.personnel = temp1
        }
        if let temp5 = JSON["performInduction"] as? Int {
            self.performInduction = temp5
        }
        if let temp5 = JSON["mentorCoachSupervise"] as? Int {
            self.mentorCoachSupervise = temp5
        }
        if let temp5 = JSON["verifyCompetencies"] as? Int {
            self.verifyCompetencies = temp5
        }
        if let temp5 = JSON["addressLimitations"] as? Int {
            self.addressLimitations = temp5
        }
        if let temp5 = JSON["manageLanguageBarriers"] as? Int {
            self.manageLanguageBarriers = temp5
        }
        if let temp5 = JSON["wearSeatBelts"] as? Int {
            self.wearSeatBelts = temp5
        }
    }
}
