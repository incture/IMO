//
//  TOJSAHAZARDNOISE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDNOISE : NSObject
{
    var permitNumber : Int = 0
    var highNoise : Int = 0
    var wearCorrectHearing : Int = 0
    var manageExposureTimes : Int = 0
    var shutDownEquipment : Int = 0
    var useQuietTools : Int = 0
    var soundBarriers : Int = 0
    var provideSuitableComms : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["highNoise"] as? Int {
            self.highNoise = temp1
        }
        if let temp5 = JSON["wearCorrectHearing"] as? Int {
            self.wearCorrectHearing = temp5
        }
        if let temp5 = JSON["manageExposureTimes"] as? Int {
            self.manageExposureTimes = temp5
        }
        if let temp5 = JSON["shutDownEquipment"] as? Int {
            self.shutDownEquipment = temp5
        }
        if let temp5 = JSON["useQuietTools"] as? Int {
            self.useQuietTools = temp5
        }
        if let temp5 = JSON["soundBarriers"] as? Int {
            self.soundBarriers = temp5
        }
        if let temp5 = JSON["provideSuitableComms"] as? Int {
            self.provideSuitableComms = temp5
        }
    }
}
