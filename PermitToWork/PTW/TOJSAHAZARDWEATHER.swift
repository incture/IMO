//
//  TOJSAHAZARDWEATHER.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDWEATHER : NSObject
{
    var permitNumber : Int = 0
    var weather : Int = 0
    var controlsForSlipperySurface : Int = 0
    var heatBreak : Int = 0
    var coldHeaters : Int = 0
    var lightning : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["weather"] as? Int {
            self.weather = temp1
        }
        if let temp5 = JSON["controlsForSlipperySurface"] as? Int {
            self.controlsForSlipperySurface = temp5
        }
        if let temp5 = JSON["heatBreak"] as? Int {
            self.heatBreak = temp5
        }
        if let temp5 = JSON["coldHeaters"] as? Int {
            self.coldHeaters = temp5
        }
        if let temp5 = JSON["lightning"] as? Int {
            self.lightning = temp5
        }
    }
}
