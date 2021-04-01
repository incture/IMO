//
//  TOJSAHAZARDCSE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDCSE : NSObject
{
    var permitNumber : Int = 0
    var confinedSpaceEntry : Int = 0
    var discussWorkPractice : Int = 0
    var conductAtmosphericTesting : Int = 0
    var monitorAccess : Int = 0
    var protectSurfaces : Int = 0
    var prohibitMobileEngine : Int = 0
    var provideObserver : Int = 0
    var developRescuePlan : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["confinedSpaceEntry"] as? Int {
            self.confinedSpaceEntry = temp1
        }
        if let temp5 = JSON["discussWorkPractice"] as? Int {
            self.discussWorkPractice = temp5
        }
        if let temp5 = JSON["conductAtmosphericTesting"] as? Int {
            self.conductAtmosphericTesting = temp5
        }
        if let temp5 = JSON["monitorAccess"] as? Int {
            self.monitorAccess = temp5
        }
        if let temp5 = JSON["protectSurfaces"] as? Int {
            self.protectSurfaces = temp5
        }
        if let temp5 = JSON["prohibitMobileEngine"] as? Int {
            self.prohibitMobileEngine = temp5
        }
        if let temp5 = JSON["provideObserver"] as? Int {
            self.provideObserver = temp5
        }
        if let temp5 = JSON["developRescuePlan"] as? Int {
            self.developRescuePlan = temp5
        }
    }
}
