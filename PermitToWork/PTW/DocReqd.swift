//
//  DocdReqd.swift
// 
//
//  Created by Soumya Singh on 12/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation

class DocReqd : NSObject, NSCoding{
    
    var atmosphericTest : Int = 0
    var Loto : Int = 0
    var Procedure : Int = 0
    var PnID : Int = 0
    var tempDefeat : Int = 0
    var rescuePlan : Int = 0
    var JSA : Int = 0
    var other : Int = 0
    var sds : Int = 0
    var fireWatch : Int = 0
    var liftPlan : Int = 0
    var simop : Int = 0
    var safeWorkPractice : Int = 0
    var certificate : String = ""
    var otherText : String = ""
    var serialNo : Int = 0
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    init(JSON: AnyObject) {
        
    }
    
    required init(coder decoder: NSCoder) {
        self.atmosphericTest = decoder.decodeInteger(forKey: "atmosphericTest")
        self.Loto = decoder.decodeInteger(forKey: "Loto")
        self.Procedure = decoder.decodeInteger(forKey: "Procedure")
        self.PnID = decoder.decodeInteger(forKey: "PnID")
        self.tempDefeat = decoder.decodeInteger(forKey: "tempDefeat")
        self.rescuePlan = decoder.decodeInteger(forKey: "rescuePlan")
        self.JSA = decoder.decodeInteger(forKey: "JSA")
        self.other = decoder.decodeInteger(forKey: "other")
        self.sds = decoder.decodeInteger(forKey: "sds")
        self.fireWatch = decoder.decodeInteger(forKey: "fireWatch")
        self.liftPlan = decoder.decodeInteger(forKey: "liftPlan")
        self.simop = decoder.decodeInteger(forKey: "simop")
        self.safeWorkPractice = decoder.decodeInteger(forKey: "safeWorkPractice")
        self.certificate = decoder.decodeObject(forKey: "certificate") as? String ?? ""
        self.otherText = decoder.decodeObject(forKey: "otherText") as? String ?? ""
        self.serialNo = decoder.decodeInteger(forKey: "serialNo")
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(atmosphericTest, forKey: "atmosphericTest")
        coder.encode(Loto, forKey: "Loto")
        coder.encode(Procedure, forKey: "Procedure")
        coder.encode(PnID, forKey: "PnID")
        coder.encode(tempDefeat, forKey: "tempDefeat")
        coder.encode(rescuePlan, forKey: "rescuePlan")
        coder.encode(JSA, forKey: "JSA")
        coder.encode(other, forKey: "other")
        coder.encode(sds, forKey: "sds")
        coder.encode(fireWatch, forKey: "fireWatch")
        coder.encode(liftPlan, forKey: "liftPlan")
        coder.encode(simop, forKey: "simop")
        coder.encode(safeWorkPractice, forKey: "safeWorkPractice")
        coder.encode(certificate, forKey: "certificate")
        coder.encode(otherText, forKey: "otherText")
        coder.encode(serialNo, forKey: "serialNo")
    }
}

class SignOff : NSObject, NSCoding
{
    var isSafeContinue : Int = 0
    var walkthrough : String = ""
    var name : String = ""
    var dateTime : String = ""
    var controlBoard : Int = 0
    var worksite : Int = 0
    var SIMOPS : Int = 0
    var others : String = ""
    var ihaveReviewedThisWork : Int = 0
    var energyIsolations : Int = 0
    var approvalDate : String = ""
    var superintendantName : String = ""
    var superintendantDate : String = ""
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.isSafeContinue = decoder.decodeInteger(forKey: "isSafeContinue")
        self.walkthrough = decoder.decodeObject(forKey: "walkthrough") as? String ?? ""
        self.name = decoder.decodeObject(forKey: "name") as? String ?? ""
        self.dateTime = decoder.decodeObject(forKey: "dateTime") as? String ?? ""
        self.controlBoard = decoder.decodeInteger(forKey: "controlBoard")
        self.worksite = decoder.decodeInteger(forKey: "worksite")
        self.SIMOPS = decoder.decodeInteger(forKey: "SIMOPS")
        self.others = decoder.decodeObject(forKey: "others") as? String ?? ""
        self.ihaveReviewedThisWork = decoder.decodeInteger(forKey: "ihaveReviewedThisWork")
        self.energyIsolations = decoder.decodeInteger(forKey: "energyIsolations")
        self.approvalDate = decoder.decodeObject(forKey: "approvalDate") as? String ?? ""
        self.superintendantName = decoder.decodeObject(forKey: "superintendantName") as? String ?? ""
        self.superintendantDate = decoder.decodeObject(forKey: "superintendantDate") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(isSafeContinue, forKey: "isSafeContinue")
        coder.encode(walkthrough, forKey: "walkthrough")
        coder.encode(name, forKey: "name")
        coder.encode(dateTime, forKey: "dateTime")
        coder.encode(controlBoard, forKey: "controlBoard")
        coder.encode(worksite, forKey: "worksite")
        coder.encode(SIMOPS, forKey: "SIMOPS")
        coder.encode(others, forKey: "others")
        coder.encode(ihaveReviewedThisWork, forKey: "ihaveReviewedThisWork")
        coder.encode(energyIsolations, forKey: "energyIsolations")
        coder.encode(approvalDate, forKey: "approvalDate")
        coder.encode(superintendantName, forKey: "superintendantName")
        coder.encode(superintendantDate, forKey: "superintendantDate")
    }

}

class FireWatch : NSObject, NSCoding{
    
    var fireWatch : String = ""
    var DatenTime : String = ""
    var iInspected : Int = 0
    
    override init() {
    }
    
    required init(coder decoder: NSCoder) {
        self.fireWatch = decoder.decodeObject(forKey: "fireWatch") as? String ?? ""
        self.DatenTime = decoder.decodeObject(forKey: "DatenTime") as? String ?? ""
        self.iInspected = decoder.decodeInteger(forKey: "iInspected")
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(fireWatch, forKey: "fireWatch")
        coder.encode(DatenTime, forKey: "DatenTime")
        coder.encode(iInspected, forKey: "iInspected")
    }

    
}

class RescuePlan : NSObject{
    
}
