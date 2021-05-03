//
//  TOPTWTESTREC.swift
//  Task-Management
//
//  Created by Soumya Singh on 25/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation

class TOPTWTESTREC : NSObject{
    
    var permitNumber : Int = 0
    var serialNo : Int = 0
    var isCWP : Int = 0
    var isHWP : Int = 0
    var isCSE : Int = 0
    var detectorUsed : String = ""
    var DateOfLastCalibration : String = ""
    var testingFrequency : String = ""
    var gasTester : String = ""
    var gasTesterComments : String = ""
    var areaToBeTested : String = ""
    var deviceSerialNo : String = ""
    var Other : String = ""
    var continuousGasMonitoring : Int = 0
    var priorToWorkCommencing : Int = 0
    var eachWorkPeriod : Int = 0
    var everyHour : Int = 0
    var isO2 : Int = 0
    var isLELS : Int = 0
    var isH2S : Int = 0

    
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
        if let temp1 = JSON["detectorUsed"] as? String {
            self.detectorUsed = temp1
        }
        if let temp1 = JSON["dateOfLastCalibration"] as? Int64 {
            self.DateOfLastCalibration = temp1.convertEpocToDatePTW()
        }
        if let temp1 = JSON["testingFrequency"] as? String {
            self.testingFrequency = temp1
        }
        if let temp1 = JSON["gasTester"] as? String {
            self.gasTester = temp1
        }
        if let temp1 = JSON["gasTesterComments"] as? String {
            self.gasTesterComments = temp1
        }
        if let temp1 = JSON["areaToBeTested"] as? String {
            self.areaToBeTested = temp1
        }
        if let temp1 = JSON["deviceSerialNo"] as? String {
            self.deviceSerialNo = temp1
        }
        if let temp1 = JSON["other"] as? String {
            self.Other = temp1
        }
        if let temp11 = JSON["continuousGasMonitoring"] as? Int {
            self.continuousGasMonitoring = temp11
        }
        if let temp11 = JSON["priorToWorkCommencing"] as? Int {
            self.priorToWorkCommencing = temp11
        }
        if let temp11 = JSON["eachWorkPeriod"] as? Int {
            self.eachWorkPeriod = temp11
        }
        if let temp11 = JSON["everyHour"] as? Int {
            self.everyHour = temp11
        }
        if let temp11 = JSON["isO2"] as? Int {
            self.isO2 = temp11
        }
        if let temp11 = JSON["isLELS"] as? Int {
            self.isLELS = temp11
        }
        if let temp11 = JSON["isH2S"] as? Int {
            self.isH2S = temp11
        }
        
        
    }
}
