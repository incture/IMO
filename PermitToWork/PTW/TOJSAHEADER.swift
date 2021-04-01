//
//  TOJSAHEADER.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHEADER : NSObject{
    
    var permitNumber : Int = 0
    var jsapermitNumber : String = ""
    var hasCWP : Int = 0
    var hasHWP : Int = 0
    var hasCSE : Int = 0
    var taskDescription : String = ""
    var identifyMostSeriousPotentialInjury : String = ""
    var isActive : Int = 0
    var status : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["jsapermitNumber"] as? String {
            self.jsapermitNumber = temp1
        }
        if let temp5 = JSON["taskDescription"] as? String {
            self.taskDescription = temp5
        }
        if let temp6 = JSON["identifyMostSeriousPotentialInjury"] as? String {
            self.identifyMostSeriousPotentialInjury = temp6
        }
        if let temp7 = JSON["isActive"] as? Int {
            self.isActive = temp7
        }
        if let temp8 = JSON["status"] as? String {
            self.status = temp8
        }
         if let temp9 = JSON["hasCWP"] as? Int {
            self.hasCWP = temp9
        }
         if let temp10 = JSON["hasHWP"] as? Int {
            self.hasHWP = temp10
        }
         if let temp11 = JSON["hasCSE"] as? Int {
            self.hasCSE = temp11
        }
        
    }
}
