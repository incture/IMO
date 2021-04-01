//
//  TOPTWTESTRES.swift
//  Task-Management
//
//  Created by Soumya Singh on 25/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOPTWTESTRES : NSObject{
    
    var permitNumber : Int = 0
    var serialNo : Int = 0
    var isCWP : Int = 0
    var isHWP : Int = 0
    var isCSE : Int = 0
    var preStartOrWorkTest : String = ""
    var toxicType : String = ""
    var flammableGas : String = ""
    var othersType : String = ""
    var date : String = ""
    var time : String = ""
    var oxygenPercentage : Float = 0.0
    var toxicResult : Float = 0.0
    var othersResult : Float = 0.0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["serialNo"] as? Int {
            self.serialNo = temp1
        }
        if let temp9 = JSON["isCWP"] as? Int {
            self.isCWP = temp9
        }
        if let temp10 = JSON["isHWP"] as? Int {
            self.isHWP = temp10
        }
        if let temp11 = JSON["isCSE"] as? Int {
            self.isCSE = temp11
        }
        if let temp1 = JSON["preStartOrWorkTest"] as? String {
            self.preStartOrWorkTest = temp1
        }
        if let temp1 = JSON["toxicType"] as? String {
            self.toxicType = temp1
        }
        if let temp1 = JSON["flammableGas"] as? String {
            self.flammableGas = temp1
        }
        if let temp1 = JSON["othersType"] as? String {
            self.othersType = temp1
        }
        if let temp1 = JSON["date"] as? Int64 {
            self.date = temp1.convertEpocToDatePTW()
        }
        if let temp1 = JSON["time"] as? String {
            self.time = temp1
        }
        if let temp11 = JSON["oxygenPercentage"] as? Float {
            self.oxygenPercentage = temp11
        }
        if let temp11 = JSON["toxicResult"] as? Float {
            self.toxicResult = temp11
        }
        if let temp11 = JSON["othersResult"] as? Float {
            self.othersResult = temp11
        }
    }
}
