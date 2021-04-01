//
//  TOPTWCWPWORK.swift
//  Task-Management
//
//  Created by Soumya Singh on 25/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOPTWCWPWORK : NSObject{
    
    var permitNumber : Int = 0
    var otherTypeOfWork : String = ""
    var descriptionOfWorkToBePerformed : String = ""
    var criticalOrComplexLift : Int = 0
    var craneOrLiftingDevice : Int = 0
    var groundDisturbanceOrExcavation : Int = 0
    var handlingHazardousChemicals : Int = 0
    var workingAtHeight : Int = 0
    var paintingOrBlasting : Int = 0
    var workingOnPressurizedSystems : Int = 0
    var erectingOrDismantlingScaffolding : Int = 0
    var breakingContainmentOfClosedOperatingSystem : Int = 0
    var workingInCloseToHazardousEnergy : Int = 0
    var removalOfIdleEquipmentForRepair : Int = 0
    var higherRiskElectricalWork : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["otherTypeOfWork"] as? String {
            self.otherTypeOfWork = temp1
        }
        if let temp1 = JSON["descriptionOfWorkToBePerformed"] as? String {
            self.descriptionOfWorkToBePerformed = temp1
        }
        if let temp11 = JSON["criticalOrComplexLift"] as? Int {
            self.criticalOrComplexLift = temp11
        }
        if let temp11 = JSON["craneOrLiftingDevice"] as? Int {
            self.craneOrLiftingDevice = temp11
        }
        if let temp11 = JSON["groundDisturbanceOrExcavation"] as? Int {
            self.groundDisturbanceOrExcavation = temp11
        }
        if let temp11 = JSON["handlingHazardousChemicals"] as? Int {
            self.handlingHazardousChemicals = temp11
        }
        if let temp11 = JSON["workingAtHeight"] as? Int {
            self.workingAtHeight = temp11
        }
        if let temp11 = JSON["paintingOrBlasting"] as? Int {
            self.paintingOrBlasting = temp11
        }
        if let temp11 = JSON["workingOnPressurizedSystems"] as? Int {
            self.workingOnPressurizedSystems = temp11
        }
        if let temp11 = JSON["erectingOrDismantlingScaffolding"] as? Int {
            self.erectingOrDismantlingScaffolding = temp11
        }
        if let temp11 = JSON["breakingContainmentOfClosedOperatingSystem"] as? Int {
            self.breakingContainmentOfClosedOperatingSystem = temp11
        }
        if let temp11 = JSON["workingInCloseToHazardousEnergy"] as? Int {
            self.workingInCloseToHazardousEnergy = temp11
        }
        if let temp11 = JSON["removalOfIdleEquipmentForRepair"] as? Int {
            self.removalOfIdleEquipmentForRepair = temp11
        }
        if let temp11 = JSON["higherRiskElectricalWork"] as? Int {
            self.higherRiskElectricalWork = temp11
        }
    }
}




class TOPTWHWPWORK : NSObject{
    var permitNumber : Int = 0
    var cutting : Int = 0
    var welding : Int = 0
    var electricalPoweredEquipment : Int = 0
    var grinding : Int = 0
    var abrasiveBlasting : Int = 0
    var descriptionOfWork : String = ""
    var otherText : String = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["otherTypeOfWork"] as? String {
            self.otherText = temp1
        }
        if let temp1 = JSON["descriptionOfWorkToBePerformed"] as? String {
            self.descriptionOfWork = temp1
        }
        if let temp11 = JSON["abrasiveBlasting"] as? Int {
            self.abrasiveBlasting = temp11
        }
        if let temp11 = JSON["grinding"] as? Int {
            self.grinding = temp11
        }
        if let temp11 = JSON["electricalPoweredEquipment"] as? Int {
            self.electricalPoweredEquipment = temp11
        }
        if let temp11 = JSON["wielding"] as? Int {
            self.welding = temp11
        }
        if let temp11 = JSON["cutting"] as? Int {
            self.cutting = temp11
        }
    }
}




class TOPTWCSPWORK : NSObject{
    
    var permitNumber : Int = 0
    var other : String = ""
    var reasonForCSE : String = ""
    var tank : Int = 0
    var vessel : Int = 0
    var excavation : Int = 0
    var pit : Int = 0
    var tower : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["other"] as? String {
            self.other = temp1
        }
        if let temp1 = JSON["reasonForCse"] as? String {
            self.reasonForCSE = temp1
        }
        if let temp11 = JSON["tank"] as? Int {
            self.tank = temp11
        }
        if let temp11 = JSON["vessel"] as? Int {
            self.vessel = temp11
        }
        if let temp11 = JSON["excavation"] as? Int {
            self.excavation = temp11
        }
        if let temp11 = JSON["pit"] as? Int {
            self.pit = temp11
        }
        if let temp11 = JSON["tower"] as? Int {
            self.tower = temp11
        }
    }
}
