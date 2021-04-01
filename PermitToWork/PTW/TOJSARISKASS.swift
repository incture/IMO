//
//  TOJSARISKASS.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSARISKASS : NSObject
{
    var permitNumber : Int = 0
    var mustModifyExistingWorkPractice : Int = 0
    var hasContinuedRisk : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["mustModifyExistingWorkPractice"] as? Int {
            self.mustModifyExistingWorkPractice = temp1
        }
        if let temp5 = JSON["hasContinuedRisk"] as? Int {
            self.hasContinuedRisk = temp5
        }
    }
}
