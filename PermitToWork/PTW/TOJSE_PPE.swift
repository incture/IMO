//
//  TOJSE_PPE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSE_PPE : NSObject
{
    var permitNumber : String = ""
    var hardHat : Int = 0
    var safetyBoot : Int = 0
    var goggles : Int = 0
    var faceShield : Int = 0
    var safetyGlasses : Int = 0
    var singleEar : Int = 0
    var doubleEars : Int = 0
    var respiratorTypeDescription : String = ""
    var needSCBA : Int = 0
    var needDustMask : Int = 0
    var cottonGlove : Int = 0
    var leatherGlove : Int = 0
    var impactProtection : Int = 0
    var gloveDescription : String = ""
    var chemicalGloveDescription : String = ""
    var fallProtection : Int = 0
    var fallRestraint : Int = 0
    var chemicalSuit : Int = 0
    var apron : Int = 0
    var flameResistantClothing : Int = 0
    var otherPPEDescription : String = ""
    var needFoulWeatherGear : Int = 0
    var haveConsentOfTaskLeader : Int = 0
    var companyOfTaskLeader : String = ""
    
    
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["hardHat"] as? Int {
            self.hardHat = temp1
        }
        if let temp2 = JSON["safetyBoot"] as? Int {
            self.safetyBoot = temp2
        }
        if let temp3 = JSON["goggles"] as? Int {
            self.goggles = temp3
        }
        if let temp4 = JSON["faceShield"] as? Int {
            self.faceShield = temp4
        }
        if let temp5 = JSON["safetyGlasses"] as? Int {
            self.safetyGlasses = temp5
        }
        if let temp6 = JSON["singleEar"] as? Int {
            self.singleEar = temp6
        }
        if let temp7 = JSON["doubleEars"] as? Int {
            self.doubleEars = temp7
        }
        if let temp8 = JSON["respiratorTypeDescription"] as? String {
            self.respiratorTypeDescription = temp8
        }
        if let temp9 = JSON["needSCBA"] as? Int {
            self.needSCBA = temp9
        }
        if let temp10 = JSON["needDustMask"] as? Int {
            self.needDustMask = temp10
        }
        if let temp11 = JSON["cottonGlove"] as? Int {
            self.cottonGlove = temp11
        }
        if let temp12 = JSON["leatherGlove"] as? Int {
            self.leatherGlove = temp12
        }
        if let temp13 = JSON["impactProtection"] as? Int {
            self.impactProtection = temp13
        }
        if let temp14 = JSON["gloveDescription"] as? String {
            self.gloveDescription = temp14
        }
        if let temp15 = JSON["chemicalGloveDescription"] as? String {
            self.chemicalGloveDescription = temp15
        }
        if let temp16 = JSON["fallProtection"] as? Int {
            self.fallProtection = temp16
        }
        if let temp17 = JSON["fallRestraint"] as? Int {
            self.fallRestraint = temp17
        }
        if let temp18 = JSON["chemicalSuit"] as? Int {
            self.chemicalSuit = temp18
        }
        if let temp19 = JSON["apron"] as? Int {
            self.apron = temp19
        }
        if let temp20 = JSON["flameResistantClothing"] as? Int {
            self.flameResistantClothing = temp20
        }
        if let temp21 = JSON["haveConsentOfTaskLeader"] as? Int {
            self.flameResistantClothing = temp21
        }
        if let temp0 = JSON["otherPPEDescription"] as? String {
            self.otherPPEDescription = temp0
        }
        
        //if let temp0 = JSON["needFoulWeatherGear"] as? Int {
        if let temp0 = Int(JSON["needFoulWeatherGear"] as? String ?? "0") {
            self.needFoulWeatherGear = temp0
        }
        if let temp0 = JSON["companyOfTaskLeader"] as? String {
            self.companyOfTaskLeader = temp0
        }
    }
}
