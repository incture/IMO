//
//  TOPTWTESTREC.swift
//  Task-Management
//
//  Created by Soumya Singh on 25/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOPTWREQDOC : NSObject{
    
    var permitNumber : Int = 0
    var serialNo : Int = 0
    var isCWP : Int = 0
    var isHWP : Int = 0
    var isCSE : Int = 0
    var atmosphericTestRecord : Int = 0
    var loto : Int = 0
    var procedure : Int = 0
    var pAndIdOrDrawing : Int = 0
    var certificate : String = ""
    var temporaryDefeat : Int = 0
    var rescuePlan : Int = 0
    var sds : Int = 0
    var otherWorkPermitDocs : String = ""
    var fireWatchChecklist : Int = 0
    var liftPlan : Int = 0
    var simopDeviation : Int = 0
    var safeWorkPractice : Int = 0
    
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
        if let temp11 = JSON["atmosphericTestRecord"] as? Int {
            self.atmosphericTestRecord = temp11
        }
        if let temp11 = JSON["loto"] as? Int {
            self.loto = temp11
        }
        if let temp11 = JSON["procedure"] as? Int {
            self.procedure = temp11
        }
        if let temp11 = JSON["pAndIdOrDrawing"] as? Int {
            self.pAndIdOrDrawing = temp11
        }
        if let temp11 = JSON["certificate"] as? String {
            self.certificate = temp11
        }
        if let temp11 = JSON["temporaryDefeat"] as? Int {
            self.temporaryDefeat = temp11
        }
        if let temp11 = JSON["rescuePlan"] as? Int {
            self.rescuePlan = temp11
        }
        if let temp11 = JSON["sds"] as? Int {
            self.sds = temp11
        }
        if let temp11 = JSON["otherWorkPermitDocs"] as? String {
            self.otherWorkPermitDocs = temp11
        }
        if let temp11 = JSON["fireWatchChecklist"] as? Int {
            self.fireWatchChecklist = temp11
        }
        if let temp11 = JSON["liftPlan"] as? Int {
            self.liftPlan = temp11
        }
        if let temp11 = JSON["simopDeviation"] as? Int {
            self.simopDeviation = temp11
        }
        if let temp11 = JSON["safeWorkPractice"] as? Int {
            self.safeWorkPractice = temp11
        }
        
    }
}
