//
//  TOJSAHAZARDTOOLS.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDTOOLS : NSObject
{
    var permitNumber : String = ""
    var EquipmentAndTools : Int = 0
    var inspectEquipmentTool : Int = 0
    var brassToolsNecessary : Int = 0
    var useProtectiveGuards : Int = 0
    var useCorrectTools : Int = 0
    var checkForSharpEdges : Int = 0
    var applyHandSafetyPrinciple : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["EquipmentAndTools"] as? Int {
            self.EquipmentAndTools = temp1
        }
        if let temp5 = JSON["inspectEquipmentTool"] as? Int {
            self.inspectEquipmentTool = temp5
        }
        if let temp5 = JSON["brassToolsNecessary"] as? Int {
            self.brassToolsNecessary = temp5
        }
        if let temp5 = JSON["useProtectiveGuards"] as? Int {
            self.useProtectiveGuards = temp5
        }
        if let temp5 = JSON["useCorrectTools"] as? Int {
            self.useCorrectTools = temp5
        }
        if let temp5 = JSON["checkForSharpEdges"] as? Int {
            self.checkForSharpEdges = temp5
        }
        if let temp5 = JSON["applyHandSafetyPrinciple"] as? Int {
            self.applyHandSafetyPrinciple = temp5
        }
    }
}
