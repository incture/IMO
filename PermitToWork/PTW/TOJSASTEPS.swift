//
//  TOJSASTEPS.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSASTEPS : NSObject
{
    var serialNo : Int = 0
    var permitNumber : String = ""
    var taskSteps : String = ""
    var potentialHazards : String = ""
    var hazardControls : String = ""
    var personResponsible : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["serialNo"] as? Int {
            self.serialNo = temp0
        }
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp0 = JSON["taskSteps"] as? String {
            self.taskSteps = temp0
        }
        if let temp0 = JSON["potentialHazards"] as? String {
            self.potentialHazards = temp0
        }
        if let temp0 = JSON["hazardControls"] as? String {
            self.hazardControls = temp0
        }
        if let temp0 = JSON["personResponsible"] as? String {
            self.personResponsible = temp0
        }
       
    }
}
