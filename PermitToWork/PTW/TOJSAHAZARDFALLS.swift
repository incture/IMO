//
//  TOJSAHAZARDFALLS.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDFALLS : NSObject
{
    var permitNumber : Int = 0
    var slipsTripsAndFalls : Int = 0
    var identifyProjections : Int = 0
    var flagHazards : Int = 0
    var secureCables : Int = 0
    var cleanUpLiquids : Int = 0
    var barricadeHoles : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["slipsTripsAndFalls"] as? Int {
            self.slipsTripsAndFalls = temp1
        }
        if let temp5 = JSON["identifyProjections"] as? Int {
            self.identifyProjections = temp5
        }
        if let temp5 = JSON["flagHazards"] as? Int {
            self.flagHazards = temp5
        }
        if let temp5 = JSON["secureCables"] as? Int {
            self.secureCables = temp5
        }
        if let temp5 = JSON["cleanUpLiquids"] as? Int {
            self.cleanUpLiquids = temp5
        }
        if let temp5 = JSON["barricadeHoles"] as? Int {
            self.barricadeHoles = temp5
        }
    }
}
