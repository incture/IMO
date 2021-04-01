//
//  PermitHeader.swift
//  Murphy_PWT_iOS
//
//  Created by Soumya Singh on 11/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class WorkTypeCW : NSObject, NSCoding{
    
    var criticalLift : Int = 0
    var Crane : Int = 0
    var groundDist : Int = 0
    var handlingChem : Int = 0
    var workAtHeight : Int = 0
    var paintBlast : Int = 0
    var workOnPressurizedSystems : Int = 0
    var erectingScaffolding : Int = 0
    var breakingContainment : Int = 0
    var closeProximity : Int = 0
    var removalOfIdleEquip : Int = 0
    var higherRisk : Int = 0
    var descriptionOfWork : String = ""
    var otherText : String = ""
    
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
        self.criticalLift = decoder.decodeInteger(forKey: "criticalLift")
        self.Crane = decoder.decodeInteger(forKey: "Crane")
        self.groundDist = decoder.decodeInteger(forKey: "groundDist")
        self.handlingChem = decoder.decodeInteger(forKey: "handlingChem")
        self.workAtHeight = decoder.decodeInteger(forKey: "workAtHeight")
        self.paintBlast = decoder.decodeInteger(forKey: "paintBlast")
        self.workOnPressurizedSystems = decoder.decodeInteger(forKey: "workOnPressurizedSystems")
        self.erectingScaffolding = decoder.decodeInteger(forKey: "erectingScaffolding")
        self.breakingContainment = decoder.decodeInteger(forKey: "breakingContainment")
        self.closeProximity = decoder.decodeInteger(forKey: "closeProximity")
        self.removalOfIdleEquip = decoder.decodeInteger(forKey: "removalOfIdleEquip")
        self.higherRisk = decoder.decodeInteger(forKey: "higherRisk")
        self.descriptionOfWork = decoder.decodeObject(forKey: "descriptionOfWork") as? String ?? ""
        self.otherText = decoder.decodeObject(forKey: "otherText") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(criticalLift, forKey: "criticalLift")
        coder.encode(Crane, forKey: "Crane")
        coder.encode(groundDist, forKey: "groundDist")
        coder.encode(handlingChem, forKey: "handlingChem")
        coder.encode(workAtHeight, forKey: "workAtHeight")
        coder.encode(paintBlast, forKey: "paintBlast")
        coder.encode(workOnPressurizedSystems, forKey: "workOnPressurizedSystems")
        coder.encode(erectingScaffolding, forKey: "erectingScaffolding")
        coder.encode(breakingContainment, forKey: "breakingContainment")
        coder.encode(closeProximity, forKey: "closeProximity")
        coder.encode(removalOfIdleEquip, forKey: "removalOfIdleEquip")
        coder.encode(higherRisk, forKey: "higherRisk")
        coder.encode(descriptionOfWork, forKey: "descriptionOfWork") 
        coder.encode(otherText, forKey: "otherText")
    }
}



class WorkTypeHW : NSObject, NSCoding {
    
    var cutting : Int = 0
    var welding : Int = 0
    var electricalPoweredEquipment : Int = 0
    var grinding : Int = 0
    var abrasiveBlasting : Int = 0
    var otherCheck: Int = 0
    var descriptionOfWork : String = ""
    var otherText : String = ""
    
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
        self.cutting = decoder.decodeInteger(forKey: "cutting")
        self.welding = decoder.decodeInteger(forKey: "welding")
        self.electricalPoweredEquipment = decoder.decodeInteger(forKey: "electricalPoweredEquipment")
        self.grinding = decoder.decodeInteger(forKey: "grinding")
        self.abrasiveBlasting = decoder.decodeInteger(forKey: "abrasiveBlasting")
        self.otherCheck = decoder.decodeInteger(forKey: "otherCheck")
        self.descriptionOfWork = decoder.decodeObject(forKey: "descriptionOfWork") as? String ?? ""
        self.otherText = decoder.decodeObject(forKey: "otherText") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(cutting, forKey: "cutting")
        coder.encode(welding, forKey: "welding")
        coder.encode(electricalPoweredEquipment, forKey: "electricalPoweredEquipment")
        coder.encode(grinding, forKey: "grinding")
        coder.encode(abrasiveBlasting, forKey: "abrasiveBlasting")
        coder.encode(otherCheck, forKey: "otherCheck")
        coder.encode(descriptionOfWork, forKey: "descriptionOfWork")
        coder.encode(otherText, forKey: "otherText")
    }
}

class WorkTypeCSP : NSObject, NSCoding {
    
    var tank : Int = 0
    var vessel : Int = 0
    var excavation : Int = 0
    var pit : Int = 0
    var tower : Int = 0
    var other : String = ""
    var reasonForCSE : String = ""
    
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
        self.tank = decoder.decodeInteger(forKey: "tank")
        self.vessel = decoder.decodeInteger(forKey: "vessel")
        self.excavation = decoder.decodeInteger(forKey: "excavation")
        self.pit = decoder.decodeInteger(forKey: "pit")
        self.tower = decoder.decodeInteger(forKey: "tower")
        self.reasonForCSE = decoder.decodeObject(forKey: "reasonForCSE") as? String ?? ""
        self.other = decoder.decodeObject(forKey: "other") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(tank, forKey: "tank")
        coder.encode(vessel, forKey: "vessel")
        coder.encode(excavation, forKey: "excavation")
        coder.encode(pit, forKey: "pit")
        coder.encode(tower, forKey: "tower")
        coder.encode(reasonForCSE, forKey: "reasonForCSE")
        coder.encode(other, forKey: "other")
    }
}

