//
//  AtmosphericTesting.swift
//  Murphy_PWT_iOS
//
//  Created by Soumya Singh on 12/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class AtmosphericTesting : NSObject, NSCoding{
    
    var areaOrEquipmentTotest : String = ""
    var detectorUsed : String = ""
    var dateOfLastCallibration : String = ""
    var O2 : Int = 0
    var Lels : Int = 0
    var H2S : Int = 0
    var other: Int = 0
    var Other : String = ""
    var priorToWorkCommencing : Int = 0
    var eachWorkPeriod : Int = 0
    var noHours : Int = 0
    var continuousMonitoringreqd : Int = 0
    var testFrequency : String = ""
    var serialNo : Int = 0
    
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    required init(coder decoder: NSCoder) {
        self.areaOrEquipmentTotest = decoder.decodeObject(forKey: "areaOrEquipmentTotest") as? String ?? ""
        self.detectorUsed = decoder.decodeObject(forKey: "detectorUsed") as? String ?? ""
        self.dateOfLastCallibration = decoder.decodeObject(forKey: "dateOfLastCallibration") as? String ?? ""
        self.O2 = decoder.decodeInteger(forKey: "O2") 
        self.Lels = decoder.decodeInteger(forKey: "Lels")
        self.H2S = decoder.decodeInteger(forKey: "H2S")
        self.other = decoder.decodeInteger(forKey: "other")
        self.Other = decoder.decodeObject(forKey: "Other") as? String ?? ""
        self.priorToWorkCommencing = decoder.decodeInteger(forKey: "priorToWorkCommencing")
        self.eachWorkPeriod = decoder.decodeInteger(forKey: "eachWorkPeriod")
        self.noHours = decoder.decodeInteger(forKey: "noHours")
        self.continuousMonitoringreqd = decoder.decodeInteger(forKey: "continuousMonitoringreqd")
        self.testFrequency = decoder.decodeObject(forKey: "testFrequency") as? String ?? ""
        self.serialNo = decoder.decodeInteger(forKey: "serialNo")


    }

    func encode(with coder: NSCoder) {
        coder.encode(areaOrEquipmentTotest, forKey: "areaOrEquipmentTotest")
        coder.encode(detectorUsed, forKey: "detectorUsed")
        coder.encode(dateOfLastCallibration, forKey: "dateOfLastCallibration")
        coder.encode(O2, forKey: "O2")
        coder.encode(Lels, forKey: "Lels")
        coder.encode(H2S, forKey: "H2S")
        coder.encode(other, forKey: "other")
        coder.encode(Other, forKey: "Other")
        coder.encode(priorToWorkCommencing, forKey: "priorToWorkCommencing")
        coder.encode(eachWorkPeriod, forKey: "eachWorkPeriod")
        coder.encode(noHours, forKey: "noHours")
        coder.encode(continuousMonitoringreqd, forKey: "continuousMonitoringreqd")
        coder.encode(testFrequency, forKey: "testFrequency")
        coder.encode(serialNo, forKey: "serialNo")
    }
    
    init(JSON: AnyObject) {
        
    }
}

