//
//  JSA.swift
// 
//
//  Created by Parul Thakur77 on 13/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation


class JSA : NSObject, NSCoding {
    
    var templateName = String()
    var createJSA = CreateJSA()
    var riskAssesment = RiskAsssesment()
    var hazardControls = HazardCategories()
    var hazardCategories = HazardCategories()
    var potentialHazards = [PotentialHazard]()
    var addNewHazard = PotentialHazard()
    var stopTheJob = [String]()
    var addNewJob = String()
    var peopleList = [People]()
    var addNewpeople = People()
    var addPeopleIndex : Int?
    var hasCWP = 0
    var hasHWP = 0
    var hasCSP = 0
    var isCWP = 0
    var isHWP = 0
    var isCSP = 0
    var status = ""
    var createdBy = ""
    var createdDate = ""
    var approvedBy = ""
    var approvedDate = ""
    var updatedBy = ""
    var updatedDate = ""
    var location = [Location]()
    var permitNumber : Int = 0
    var jsaPermitNumber = ""
    var checkedCW = false
    var checkedHW = false
    var checkedCSE = false
    var CWP = ColdPermit()
    var HWP = ColdPermit()
    var CSEP = ColdPermit()
    var atmosphericTesting = AtmosphericTesting()
    var testResult = TestResult()
    var currentFlow = FlowType.JSA
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.createJSA = decoder.decodeObject(forKey: "createJSA") as? CreateJSA ?? CreateJSA()
        self.riskAssesment = decoder.decodeObject(forKey: "riskAssesment") as? RiskAsssesment ?? RiskAsssesment()
        self.hazardControls = decoder.decodeObject(forKey: "hazardControls") as? HazardCategories ?? HazardCategories()
        self.hazardCategories = decoder.decodeObject(forKey: "hazardCategories") as? HazardCategories ?? HazardCategories()
        self.potentialHazards = decoder.decodeObject(forKey: "potentialHazards") as? [PotentialHazard] ?? [PotentialHazard]()
        self.addNewHazard = decoder.decodeObject(forKey: "addNewHazard") as? PotentialHazard ?? PotentialHazard()
        self.stopTheJob = decoder.decodeObject(forKey: "stopTheJob") as? [String] ?? [String]()
        self.addNewJob = decoder.decodeObject(forKey: "addNewJob") as? String ?? ""
        self.peopleList = decoder.decodeObject(forKey: "peopleList") as? [People] ?? [People]()
        self.addNewpeople = decoder.decodeObject(forKey: "People") as? People ?? People()
        self.addPeopleIndex = decoder.decodeObject(forKey: "addPeopleIndex") as? Int ?? 0
        self.hasCWP = decoder.decodeInteger(forKey: "hasCWP")
        self.hasHWP = decoder.decodeInteger(forKey: "hasHWP")
        self.hasCSP = decoder.decodeInteger(forKey: "hasCSP")
        self.isCWP = decoder.decodeInteger(forKey: "isCWP")
        self.isHWP = decoder.decodeInteger(forKey: "isHWP")
        self.isCSP = decoder.decodeInteger(forKey: "isCSP")
        self.status = decoder.decodeObject(forKey: "status") as? String ?? ""
        self.createdBy = decoder.decodeObject(forKey: "createdBy") as? String ?? ""
        self.createdDate = decoder.decodeObject(forKey: "createdDate") as? String ?? ""
        self.approvedBy = decoder.decodeObject(forKey: "approvedBy") as? String ?? ""
        self.approvedDate = decoder.decodeObject(forKey: "approvedDate") as? String ?? ""
        self.updatedBy = decoder.decodeObject(forKey: "updatedBy") as? String ?? ""
        self.updatedDate = decoder.decodeObject(forKey: "updatedDate") as? String ?? ""
        self.location = decoder.decodeObject(forKey: "location") as? [Location] ?? [Location]()
        self.permitNumber = decoder.decodeInteger(forKey: "permitNumber")
        self.jsaPermitNumber = decoder.decodeObject(forKey: "jsaPermitNumber") as? String ?? ""
        self.checkedCW = decoder.decodeObject(forKey: "checkedCW") as? Bool ?? false
        self.checkedHW = decoder.decodeObject(forKey: "checkedHW") as? Bool ?? false
        self.checkedCSE = decoder.decodeObject(forKey: "checkedCSE") as? Bool ?? false
        self.CWP = decoder.decodeObject(forKey: "CWP") as? ColdPermit ?? ColdPermit()
        self.HWP = decoder.decodeObject(forKey: "HWP") as? ColdPermit ?? ColdPermit()
        self.CSEP = decoder.decodeObject(forKey: "CSEP") as? ColdPermit ?? ColdPermit()
        self.currentFlow = FlowType(rawValue: decoder.decodeObject(forKey: "currentFlow") as? String ?? "noFlow")!
        self.atmosphericTesting = decoder.decodeObject(forKey: "atmosphericTesting") as? AtmosphericTesting ?? AtmosphericTesting()
        self.testResult = decoder.decodeObject(forKey: "testResult") as? TestResult ?? TestResult()
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(createJSA, forKey: "createJSA")
        coder.encode(riskAssesment, forKey: "riskAssesment")
        coder.encode(hazardControls, forKey: "hazardControls")
        coder.encode(hazardCategories, forKey: "hazardCategories")
        coder.encode(potentialHazards, forKey: "potentialHazards")
        coder.encode(addNewHazard, forKey: "addNewHazard")
        coder.encode(stopTheJob, forKey: "stopTheJob")
        coder.encode(addNewJob, forKey: "addNewJob")
        coder.encode(peopleList, forKey: "peopleList")
        coder.encode(addNewpeople, forKey: "addNewpeople")
        coder.encode(addPeopleIndex, forKey: "addPeopleIndex")
        coder.encode(hasCWP, forKey: "hasCWP")
        coder.encode(hasHWP, forKey: "hasHWP")
        coder.encode(hasCSP, forKey: "hasCSP")
        coder.encode(isCWP, forKey: "isCWP")
        coder.encode(isHWP, forKey: "isHWP")
        coder.encode(isCSP, forKey: "isCSP")
        coder.encode(status, forKey: "status")
        coder.encode(createdBy, forKey: "createdBy")
        coder.encode(createdDate, forKey: "createdDate")
        coder.encode(approvedBy, forKey: "approvedBy")
        coder.encode(approvedDate, forKey: "approvedDate")
        coder.encode(updatedBy, forKey: "updatedBy")
        coder.encode(updatedDate, forKey: "updatedDate")
        coder.encode(location, forKey: "location")
        coder.encode(permitNumber, forKey: "permitNumber")
        coder.encode(jsaPermitNumber, forKey: "jsaPermitNumber")
        coder.encode(checkedCW, forKey: "checkedCW")
        coder.encode(checkedHW, forKey: "checkedHW")
        coder.encode(checkedCSE, forKey: "checkedCSE")
        coder.encode(CWP, forKey: "CWP")
        coder.encode(HWP, forKey: "HWP")
        coder.encode(CSEP, forKey: "CSEP")
        coder.encode(currentFlow.rawValue, forKey: "currentFlow")
        coder.encode(atmosphericTesting, forKey: "atmosphericTesting")
        coder.encode(testResult, forKey: "testResult")
    }
}

class CreateJSA : NSObject, NSCoding
{
    var facility = ""
    var permitNo = ""
    var jsaPermit = ""
    var permitType = ""
    var taskDescription = ""
    var injuryDescription = ""
    var isActive = 0
    var status = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? String {
            self.permitNo = temp0
        }
        if let temp1 = JSON["jsapermitNumber"] as? String {
            self.jsaPermit = temp1
        }
        if let temp5 = JSON["taskDescription"] as? String {
            self.taskDescription = temp5
        }
        if let temp6 = JSON["identifyMostSeriousPotentialInjury"] as? String {
            self.injuryDescription = temp6
        }
        if let temp7 = JSON["isActive"] as? Int {
            self.isActive = temp7
        }
        if let temp8 = JSON["status"] as? String {
            self.status = temp8
        }
        
    }
    
    required init(coder decoder: NSCoder) {
        self.facility = decoder.decodeObject(forKey: "facility") as? String ?? ""
        self.permitNo = decoder.decodeObject(forKey: "permitNo") as? String ?? ""
        self.jsaPermit = decoder.decodeObject(forKey: "jsaPermit") as? String ?? ""
        self.permitType = decoder.decodeObject(forKey: "permitType") as? String ?? ""
        self.taskDescription = decoder.decodeObject(forKey: "taskDescription") as? String ?? ""
        self.injuryDescription = decoder.decodeObject(forKey: "injuryDescription") as? String ?? ""
        self.status = decoder.decodeObject(forKey: "hasSignedJSA") as? String ?? ""
        self.isActive = decoder.decodeInteger(forKey: "isActive")
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(facility, forKey: "facility")
        coder.encode(permitNo, forKey: "permitNo")
        coder.encode(jsaPermit, forKey: "jsaPermit")
        coder.encode(permitType, forKey: "permitType")
        coder.encode(taskDescription, forKey: "taskDescription")
        coder.encode(injuryDescription, forKey: "injuryDescription")
        coder.encode(status, forKey: "status")
        coder.encode(isActive, forKey: "isActive")
    }
    
}

class People : NSObject, NSCoding
{
    @objc var fullName = ""
    var firstName = ""
    var middleName = ""
    var lastName = ""
    var designation = ""
    var contactNumber = ""
    var hasSignedJSA = 0
    var hasSignedHWP = 0
    var hasSignedCWP = 0
    var hasSignedCSE = 0
    var isCheckedHWP = 0
    var isCheckedCWP = 0
    var isCheckedCSE = 0
    var isCheckedJSA = 0
    var location = ""
    var permitNumber : Int = 0
    var logIn = ""
    var logout = ""
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["firstName"] as? String {
            self.firstName = temp1
        }
        if let temp5 = JSON["lastName"] as? String {
            self.lastName = temp5
        }
        if let temp6 = JSON["contactNumber"] as? String {
            self.contactNumber = temp6
            print(self.contactNumber)
        }
    
    }
    
    required init(coder decoder: NSCoder) {
        self.permitNumber = decoder.decodeObject(forKey: "permitNumber") as? Int ?? 0
        self.fullName = decoder.decodeObject(forKey: "fullName") as? String ?? ""
        self.firstName = decoder.decodeObject(forKey: "firstName") as? String ?? ""
        self.middleName = decoder.decodeObject(forKey: "middleName") as? String ?? ""
        self.lastName = decoder.decodeObject(forKey: "lastName") as? String ?? ""
        self.designation = decoder.decodeObject(forKey: "designation") as? String ?? ""
        self.contactNumber = decoder.decodeObject(forKey: "contactNumber") as? String ?? ""
        self.hasSignedJSA = decoder.decodeInteger(forKey: "hasSignedJSA")
        self.hasSignedHWP = decoder.decodeInteger(forKey: "hasSignedHWP")
        self.hasSignedCWP = decoder.decodeInteger(forKey: "hasSignedCWP")
        self.hasSignedCSE = decoder.decodeInteger(forKey: "hasSignedCSE")
        self.isCheckedHWP = decoder.decodeInteger(forKey: "isCheckedHWP")
        self.isCheckedCWP = decoder.decodeInteger(forKey: "isCheckedCWP")
        self.isCheckedCSE = decoder.decodeInteger(forKey: "isCheckedCSE")
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(permitNumber, forKey: "permitNumber")
        coder.encode(fullName, forKey: "fullName")
        coder.encode(firstName, forKey: "firstName")
        coder.encode(middleName, forKey: "middleName")
        coder.encode(lastName, forKey: "lastName")
        coder.encode(designation, forKey: "designation")
        coder.encode(contactNumber, forKey: "contactNumber")
        coder.encode(hasSignedJSA, forKey: "hasSignedJSA")
        coder.encode(hasSignedHWP, forKey: "hasSignedHWP")
        coder.encode(hasSignedCWP, forKey: "hasSignedCWP")
        coder.encode(hasSignedCSE, forKey: "hasSignedCSE")
        coder.encode(isCheckedHWP, forKey: "isCheckedHWP")
        coder.encode(isCheckedCWP, forKey: "isCheckedCWP")
        coder.encode(isCheckedCSE, forKey: "isCheckedCSE")
    }
}

class PeopleAddedList : NSObject, NSCoding{
    
    @objc var peopleArray = [[People]]()
    @objc var locationArray = [String]()
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.peopleArray = decoder.decodeObject(forKey: "peopleArray") as? [[People]] ?? [[People]]()
        self.locationArray = decoder.decodeObject(forKey: "locationArray") as? [String] ?? [String]()
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(peopleArray, forKey: "peopleArray")
        coder.encode(locationArray, forKey: "locationArray")
    }
}

class RiskAsssesment : NSObject, NSCoding
{
    var mustExistingWork = 0
    var afterMitigation = 0
    var hardHat = 1
    var safetyShoes = 1
    var safetyGlasses = 1
    var faceShield = 0
    var goggles = 0
    var single = 0
    var double = 0
    var SCBA = 0
    var dustMask = 0
    var cotton = 0
    var leather = 0
    var impactProtection = 0
    var other = ""
    var chemical = ""
    var respiratorType = ""
    var fallProtection = 0
    var fallRestraint = 0
    var chemicalSuit = 0
    var apron = 0
    var flameResistantClothing = 1
    var foulWeatherGear = 0
    var otherPPE = ""
    
    override init(){
        
    }
    
    required init(coder decoder: NSCoder) {
        self.mustExistingWork = decoder.decodeInteger(forKey: "mustExistingWork")
        self.afterMitigation = decoder.decodeInteger(forKey: "afterMitigation")
        self.hardHat = decoder.decodeInteger(forKey: "hardHat")
        self.safetyShoes = decoder.decodeInteger(forKey: "safetyShoes")
        self.safetyGlasses = decoder.decodeInteger(forKey: "safetyGlasses")
        self.faceShield = decoder.decodeInteger(forKey: "faceShield")
        self.goggles = decoder.decodeInteger(forKey: "goggles")
        self.single = decoder.decodeInteger(forKey: "single")
        self.double = decoder.decodeInteger(forKey: "double")
        self.SCBA = decoder.decodeInteger(forKey: "SCBA")
        self.dustMask = decoder.decodeInteger(forKey: "dustMask")
        self.cotton = decoder.decodeInteger(forKey: "cotton")
        self.leather = decoder.decodeInteger(forKey: "leather")
        self.impactProtection = decoder.decodeInteger(forKey: "impactProtection")
        self.other = decoder.decodeObject(forKey: "other") as? String ?? ""
        self.chemical = decoder.decodeObject(forKey: "chemical") as? String ?? ""
        self.respiratorType = decoder.decodeObject(forKey: "respiratorType") as? String ?? ""
        self.fallProtection = decoder.decodeInteger(forKey: "fallProtection")
        self.fallRestraint = decoder.decodeInteger(forKey: "fallRestraint")
        self.chemicalSuit = decoder.decodeInteger(forKey: "chemicalSuit")
        self.apron = decoder.decodeInteger(forKey: "apron")
        self.flameResistantClothing = decoder.decodeInteger(forKey: "flameResistantClothing")
        self.foulWeatherGear = decoder.decodeInteger(forKey: "foulWeatherGear")
        self.otherPPE = decoder.decodeObject(forKey: "otherPPE") as? String ?? ""
        
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(mustExistingWork, forKey: "mustExistingWork")
        coder.encode(afterMitigation, forKey: "afterMitigation")
        coder.encode(hardHat, forKey: "hardHat")
        coder.encode(safetyShoes, forKey: "safetyShoes")
        coder.encode(safetyGlasses, forKey: "safetyGlasses")
        coder.encode(faceShield, forKey: "faceShield")
        coder.encode(goggles, forKey: "goggles")
        coder.encode(single, forKey: "single")
        coder.encode(double, forKey: "double")
        coder.encode(SCBA, forKey: "SCBA")
        coder.encode(dustMask, forKey: "dustMask")
        coder.encode(cotton, forKey: "cotton")
        coder.encode(leather, forKey: "leather")
        coder.encode(impactProtection, forKey: "impactProtection")
        coder.encode(other, forKey: "other")
        coder.encode(chemical, forKey: "chemical")
        coder.encode(respiratorType, forKey: "respiratorType")
        coder.encode(fallProtection, forKey: "fallProtection")
        coder.encode(fallRestraint, forKey: "fallRestraint")
        coder.encode(chemicalSuit, forKey: "chemicalSuit")
        coder.encode(apron, forKey: "apron")
        coder.encode(flameResistantClothing, forKey: "flameResistantClothing")
        coder.encode(foulWeatherGear, forKey: "foulWeatherGear")
        coder.encode(otherPPE, forKey: "otherPPE")
    }
}

class Trigger : NSObject
{
    var triggers = [String]()
}

//Hazard Control

class HazardCategories : NSObject, NSCoding
{
    //    var pressurizedEquipment : PresurizedEquipment?
    //    var poorLighting : PoorLighting?
    //    var personnel : Personnel?
    //    var confinedSpaceEntry : ConfinedSpaceEntry?
    //    var simultaneousOperations : SimultaneousOperations?
    //    var ignitionSources : IgnitionSources?
    //    var hazardousSubstances : HazardousSubstances?
    //    var potentialSpills : PotentialSpills?
    //    var weather : Weather?
    //    var highNoise : HighNoise?
    //    var droppedObjects : DroppedObjects?
    //    var liftingEquipment : LiftingEquipment?
    //    var workAtHeights : WorkAtHeights?
    //    var portableElectricalEquipment : PortableElectricalEquipment?
    //    var movingEquipment : MovingEquipment?
    //    var manualHandling : ManualHandling?
    //    var equipmentAndTools : EquipmentAndTools?
    //    var slipsTripsAndFalls : SlipsTripsAndFalls?
    //    var highEnergy : HighEnergyHighVoltage?
    //    var excavations : Excavations?
    //    var mobileEquipment : MobileEquipment?
    
    //    var pressurizedEquipment : [String]?
    //    var poorLighting : [String]?
    //    var personnel : [String]?
    //    var confinedSpaceEntry : [String]?
    //    var simultaneousOperations : [String]?
    //    var ignitionSources : [String]?
    //    var hazardousSubstances : [String]?
    //    var potentialSpills : [String]?
    //    var weather : [String]?
    //    var highNoise : [String]?
    //    var droppedObjects : [String]?
    //    var liftingEquipment : [String]?
    //    var workAtHeights : [String]?
    //    var portableElectricalEquipment : [String]?
    //    var movingEquipment : [String]?
    //    var manualHandling : [String]?
    //    var equipmentAndTools : [String]?
    //    var slipsTripsAndFalls : [String]?
    //    var highEnergy : [String]?
    //    var excavations : [String]?
    //    var mobileEquipment : [String]?
    
    var categories = [[Int]]()
    
    override init(){
        for _ in 0...20{
            var element = [Int]()
            for _ in 0...10{
                element.append(0)
            }
            categories.append(element)
        }
    }
    
    required init(coder decoder: NSCoder) {
        self.categories = decoder.decodeObject(forKey: "categories") as? [[Int]] ?? [[Int]]()
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(categories, forKey: "categories")
    }
}

class PresurizedEquipment : NSObject
{
    var performIsolation = false
    var depressurize = false
    var relieveTrappedPressure = false
    var doNotWork = false
    var anticipateResidual = false
    var secureAllHoses = false
    
}

class PoorLighting : NSObject
{
    var provideAlternate = false
    var waitOrDefer = false
    var deferUntil = false
    var knowDistance = false
    
}

class Personnel : NSObject
{
    var performInduction = false
    var mentor = false
    var verifyCompetencies = false
    var addressApplicable = false
    var manageLanguage = false
    var seatBelts = false
    
}

class ConfinedSpaceEntry : NSObject
{
    var discussConfinedSpace = false
    var conductAtmospheric = false
    var monitorAccess = false
    var protectSurfaces = false
    var prohibitMobile = false
    var provideObserver = false
    var developRescuePlan = false
    
}

class SimultaneousOperations : NSObject
{
    var followSIMOPS = false
    var MOCRequired = false
    var interfaceBetween = false
    var useBarriers = false
    var havePermitSigned = false
    
}

class IgnitionSources : NSObject
{
    var remove_isolate = false
    var provideFirefighting = false
    var implementAbrasive = false
    var conductContinous = false
    var bondOrEarth = false
    
}

class HazardousSubstances : NSObject
{
    var drainOrPurge = false
    var followSDS = false
    var implementHealth = false
    var test = false
    
}

class PotentialSpills : NSObject
{
    var drainEquipment = false
    var hoses_connections = false
    var spillContainment = false
    var haveSpillCleanup = false
    var restrainAndIsolate = false
    
}

class Weather : NSObject
{
    var implementControls = false
    var heat_hydration = false
    var cold_PPE = false
    var lightning_Tool = false
    
}

class HighNoise : NSObject
{
    var wearCorrectHearing = false
    var manageExposure = false
    var shutDown = false
    var useQuiet = false
    var soundBarriers = false
    var provideObserver = false
    var provideOrUse = false
    
}

class DroppedObjects : NSObject
{
    var useSigns = false
    var useLifting = false
    var secureTools = false
    
}

class LiftingEquipment : NSObject
{
    var confirmLifting = false
    var obtainApproval = false
    var haveADocumented = false
    
}

class WorkAtHeights : NSObject
{
    var discussWorking = false
    var verifyFall = false
    var fullBody = false
    var lockingType = false
    
}

class PortableElectricalEquipment : NSObject
{
    var inspectEquipment = false
    var implementContinuous = false
    var protectElectrical = false
    var identifyEquipment = false
    
}

class MovingEquipment : NSObject
{
    var confirmMachinery = false
    var provideProtective = false
    var observer = false
    var shutDown = false
    var doNotWork = false
    
}

class ManualHandling : NSObject
{
    var assessManual = false
    var limitLoadSize = false
    var properLifting = false
    var confirmStability = false
    var getAssistance = false
    
}

class EquipmentAndTools : NSObject
{
    var inspectEquipment = false
    var brassTools = false
    var useProtectiveGuards = false
    var protectOrRemove = false
    var applyHandSafety = false
    
}

class SlipsTripsAndFalls : NSObject
{
    var identifyAndShield = false
    var flagHazards = false
    var secureOrCover = false
    var cleanUpLiquids = false
    var barricadeOrRope = false
    
}

class HighEnergyHighVoltage : NSObject
{
    var restrictAccess = false
    var dischargeEquipment = false
    var observeSafeWork = false
    var useFlashBurn = false
    var useInsulated = false
    
}

class Excavations : NSObject
{
    var haveAnExcavation = false
    var locateUnderground = false
    var deEnergizeUnderground = false
    var implementConfined = false
    
}

class MobileEquipment : NSObject
{
    var assessEquipment = false
    var implementControls = false
    var limitAndMonitor = false
    var manageOverhead = false
    var adhereToRoad = false
    
}

class PotentialHazard : NSObject, NSCoding
{
    var taskStep = ""
    var potentialHazards = ""
    var hazardControls = ""
    var personResponsible = ""
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.taskStep = decoder.decodeObject(forKey: "taskStep") as? String ?? ""
        self.potentialHazards = decoder.decodeObject(forKey: "potentialHazards") as? String ?? ""
        self.hazardControls = decoder.decodeObject(forKey: "hazardControls") as? String ?? ""
        self.personResponsible = decoder.decodeObject(forKey: "personResponsible") as? String ?? ""
    }
    func encode(with coder: NSCoder) {
        coder.encode(taskStep, forKey: "taskStep")
        coder.encode(potentialHazards, forKey: "potentialHazards")
        coder.encode(hazardControls, forKey: "hazardControls")
        coder.encode(personResponsible, forKey: "personResponsible")
    }
    
}

class Location: NSObject, NSCoding {
    var permitNumber  : Int = 0
    var serialNo : Int = 0
    var facility  = ""
    var muwi  = ""
    var facilityOrSite = ""
    var hierarchyLevel = ""
    var wellPad = ""
    var well = ""
    var wellcheck = false
    var wellCode = ""
    var wellPadCode = ""
    var facilityCode = ""
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.permitNumber = decoder.decodeInteger(forKey: "permitNumber")
        self.serialNo = decoder.decodeInteger(forKey: "serialNo")
        self.facility = decoder.decodeObject(forKey: "facility") as? String ?? ""
        self.muwi = decoder.decodeObject(forKey: "muwi") as? String ?? ""
        self.facilityOrSite = decoder.decodeObject(forKey: "facilityOrSite") as? String ?? ""
        self.hierarchyLevel = decoder.decodeObject(forKey: "hierarchyLevel") as? String ?? ""
        self.wellPad = decoder.decodeObject(forKey: "wellPad") as? String ?? ""
        self.well = decoder.decodeObject(forKey: "well") as? String ?? ""
        self.wellcheck = decoder.decodeObject(forKey: "wellcheck") as? Bool ?? false
        self.wellCode = decoder.decodeObject(forKey: "wellCode") as? String ?? ""
        self.wellPadCode = decoder.decodeObject(forKey: "wellPadCode") as? String ?? ""
        self.facilityCode = decoder.decodeObject(forKey: "facilityCode") as? String ?? ""
    }
    func encode(with coder: NSCoder) {
        coder.encode(permitNumber, forKey: "permitNumber")
        coder.encode(serialNo, forKey: "serialNo")
        coder.encode(facility, forKey: "facility")
        coder.encode(muwi, forKey: "muwi")
        coder.encode(facilityOrSite, forKey: "facilityOrSite")
        coder.encode(hierarchyLevel, forKey: "hierarchyLevel")
        coder.encode(wellPad, forKey: "wellPad")
        coder.encode(well, forKey: "well")
        coder.encode(wellcheck, forKey: "wellcheck")
        coder.encode(wellCode, forKey: "wellCode")
        coder.encode(wellPadCode, forKey: "wellPadCode")
        coder.encode(facilityCode, forKey: "facilityCode")
    }
}

