//
//  TOJSAHAZARDELECTRICAL.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDELECTRICAL : NSObject
{
    var permitNumber : Int = 0
    var portableElectricalEquipment : Int = 0
    var inspectToolsForCondition : Int = 0
    var implementGasTesting : Int = 0
    var protectElectricalLeads : Int = 0
    var identifyEquipClassification : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["portableElectricalEquipment"] as? Int {
            self.portableElectricalEquipment = temp1
        }
        if let temp5 = JSON["inspectToolsForCondition"] as? Int {
            self.inspectToolsForCondition = temp5
        }
        if let temp5 = JSON["implementGasTesting"] as? Int {
            self.implementGasTesting = temp5
        }
        if let temp5 = JSON["protectElectricalLeads"] as? Int {
            self.protectElectricalLeads = temp5
        }
        if let temp5 = JSON["identifyEquipClassification"] as? Int {
            self.identifyEquipClassification = temp5
        }
    }
}
