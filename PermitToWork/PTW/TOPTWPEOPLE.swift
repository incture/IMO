//
//  TOPTWPEOPLE.swift
//  Task-Management
//
//  Created by Soumya Singh on 18/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOPTWPEOPLE : NSObject
{
    var serialNo : Int = 0
    var permitNumber : Int = 0
    var firstName : String = ""
    var lastName : String = ""
    var contactNumber : String = ""
    var hasSignedJSA : Int = 0
    var hasSignedCWP : Int = 0
    var hasSignedHWP : Int = 0
    var hasSignedCSE : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["serialNo"] as? Int {
            self.serialNo = temp0
        }
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp0 = JSON["firstName"] as? String {
            self.firstName = temp0
        }
        if let temp0 = JSON["lastName"] as? String {
            self.lastName = temp0
        }
        if let temp0 = JSON["contactNumber"] as? String {
            self.contactNumber = temp0
        }
        if let temp0 = JSON["hasSignedJSA"] as? Int {
            self.hasSignedJSA = temp0
        }
        if let temp0 = JSON["hasSignedCWP"] as? Int {
            self.hasSignedCWP = temp0
        }
        if let temp0 = JSON["hasSignedHWP"] as? Int {
            self.hasSignedHWP = temp0
        }
        if let temp0 = JSON["hasSignedCSE"] as? Int {
            self.hasSignedCSE = temp0
        }
    }
}
