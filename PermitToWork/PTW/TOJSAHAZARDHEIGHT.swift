//
//  TOJSAHAZARDHEIGHT.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDHEIGHT : NSObject
{
    var permitNumber : Int = 0
    var workAtHeights : Int = 0
    var discussWorkingPractice : Int = 0
    var verifyFallRestraint : Int = 0
    var useFullBodyHarness : Int = 0
    var useLockTypeSnaphoooks : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = Int(temp0 as String)!
        }
        if let temp1 = JSON["workAtHeights"] as? Int {
            self.workAtHeights = temp1
        }
        if let temp5 = JSON["discussWorkingPractice"] as? Int {
            self.discussWorkingPractice = temp5
        }
        if let temp5 = JSON["verifyFallRestraint"] as? Int {
            self.verifyFallRestraint = temp5
        }
        if let temp5 = JSON["useFullBodyHarness"] as? Int {
            self.useFullBodyHarness = temp5
        }
        if let temp5 = JSON["useLockTypeSnaphoooks"] as? Int {
            self.useLockTypeSnaphoooks = temp5
        }
    }
}
