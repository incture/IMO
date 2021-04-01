//
//  TOJSALOCATION.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSALOCATION : NSObject
{
    var permitNumber : Int = 0
    var serialNo : Int = 0
    var facilityOrSite : String = ""
    var hierachyLevel : String = ""
    var facility : String = ""
    var muwi : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp0 = JSON["serialNo"] as? Int {
            self.serialNo = temp0
        }
        if let temp0 = JSON["facilityOrSite"] as? String {
            self.facilityOrSite = temp0
        }
        if let temp0 = JSON["hierachyLevel"] as? String {
            self.hierachyLevel = temp0
        }
        if let temp0 = JSON["facility"] as? String {
            self.facility = temp0
        }
        if let temp0 = JSON["muwi"] as? String {
            self.muwi = temp0
        }
        
    }
}
