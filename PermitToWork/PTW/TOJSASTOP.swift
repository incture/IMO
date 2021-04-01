//
//  TOJSASTOP.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSASTOP : NSObject
{
    var serialNo : Int = 0
    var permitNumber : String = ""
    var lineDescription : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["serialNo"] as? Int {
            self.serialNo = temp0
        }
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp0 = JSON["lineDescription"] as? String {
            self.lineDescription = temp0
        }
    }
}
