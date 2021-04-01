//
//  TOJSAHAZARDVISIBLE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDVISIBLE : NSObject
{
    var permitNumber : Int = 0
    var poorLighting : Int = 0
    var provideAlternateLighting : Int = 0
    var waitUntilVisibilityImprove : Int = 0
    var deferUntilVisibilityImprove : Int = 0
    var knowDistanceFromPoles : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["poorLighting"] as? Int {
            self.poorLighting = temp1
        }
        if let temp5 = JSON["provideAlternateLighting"] as? Int {
            self.provideAlternateLighting = temp5
        }
        if let temp5 = JSON["waitUntilVisibilityImprove"] as? Int {
            self.waitUntilVisibilityImprove = temp5
        }
        if let temp5 = JSON["deferUntilVisibilityImprove"] as? Int {
            self.deferUntilVisibilityImprove = temp5
        }
        if let temp5 = JSON["knowDistanceFromPoles"] as? Int {
            self.knowDistanceFromPoles = temp5
        }
    }
}
