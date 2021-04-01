//
//  TOJSAHAZARDDROPPED.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDDROPPED : NSObject
{
    var permitNumber : Int = 0
    var droppedObjects : Int = 0
    var markRestrictEntry : Int = 0
    var useLiftingEquipmentToRaise : Int = 0
    var secureTools : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["droppedObjects"] as? Int {
            self.droppedObjects = temp1
        }
        if let temp5 = JSON["markRestrictEntry"] as? Int {
            self.markRestrictEntry = temp5
        }
        if let temp5 = JSON["useLiftingEquipmentToRaise"] as? Int {
            self.useLiftingEquipmentToRaise = temp5
        }
        if let temp5 = JSON["secureTools"] as? Int {
            self.secureTools = temp5
        }
    }
}
