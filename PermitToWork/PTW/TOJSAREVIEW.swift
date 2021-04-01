//
//  TOJSAREVIEW.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAREVIEW : NSObject
{
    var permitNumber : Int = 0
    var createdBy : String = ""
    var createdDate : String = ""
    var approvedBy : String = ""
    var approvedDate : String = ""
    var lastUpdatedBy : String = ""
    var lastUpdatedDate : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["createdBy"] as? String {
            self.createdBy = temp1
        }
        if let temp5 = JSON["createdDate"] as? Int64 {
            self.createdDate = temp5.convertEpocToDatePTW()
        }
        if let temp6 = JSON["approvedBy"] as? String {
            self.approvedBy = temp6
        }
        if let temp7 = JSON["lastUpdatedBy"] as? String {
            self.lastUpdatedBy = temp7
        }
        if let temp8 = JSON["approvedDate"] as? Int64 {
            self.approvedDate = temp8.convertEpocToDatePTW()
        }
        if let temp9 = JSON["lastUpdatedDate"] as? Int64 {
            self.lastUpdatedDate = temp9.convertEpocToDatePTW()
        }
        
    }
}
