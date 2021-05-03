//
//  ReadJSA.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class ReadJSA : NSObject
{
    var TOJSAHEADERValue : TOJSAHEADER?
    var TOJSAREVIEWValue : TOJSAREVIEW?
    var TOJSARISKASSValue : TOJSARISKASS?
    var TOJSE_PPEValue : TOJSE_PPE?
    var TOJSAHAZARDPRESSValue : TOJSAHAZARDPRESS?
    var TOJSAHAZARDVISIBLEValue : TOJSAHAZARDVISIBLE?
    var TOJSAHAZARDPERSONValue : TOJSAHAZARDPERSON?
    var TOJSAHAZARDCSEValue : TOJSAHAZARDCSE?
    var TOJSAHAZARDSIMULTANValue : TOJSAHAZARDSIMULTAN?
    var TOJSAHAZARDIGNITIONValue : TOJSAHAZARDIGNITION?
    var TOJSAHAZARDSUBSValue : TOJSAHAZARDSUBS?
    var TOJSAHAZARDSPILLValue : TOJSAHAZARDSPILL?
    var TOJSAHAZARDWEATHERValue : TOJSAHAZARDWEATHER?
    var TOJSAHAZARDNOISEValue : TOJSAHAZARDNOISE?
    var TOJSAHAZARDDROPPEDValue : TOJSAHAZARDDROPPED?
    var TOJSAHAZARDLIFTValue : TOJSAHAZARDLIFT?
    var TOJSAHAZARDHEIGHTValue : TOJSAHAZARDHEIGHT?
    var TOJSAHAZARDELECTRICALValue : TOJSAHAZARDELECTRICAL?
    var TOJSAHAZARDMOVINGValue : TOJSAHAZARDMOVING?
    var TOJSAHAZARDMANUALValue : TOJSAHAZARDMANUAL?
    var TOJSAHAZARDTOOLSValue : TOJSAHAZARDTOOLS?
    var TOJSAHAZARDFALLSValue : TOJSAHAZARDFALLS?
    var TOJSAHAZARDVOLTAGEValue : TOJSAHAZARDVOLTAGE?
    var TOJSAHAZARDEXCAVATIONValue : TOJSAHAZARDEXCAVATION?
    var TOJSAHAZARDMOBILEValue : TOJSAHAZARDMOBILE?
    var TOJSASTEPSArrayValue = [TOJSASTEPS]()
    var TOJSASTOPArrayValue = [TOJSASTOP]()
    var TOJSALOCATIONValue = [TOJSALOCATION]()
    var TOPTWPEOPLEArrayValue = [TOPTWPEOPLE]()
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        let jsaObject = JSON
        
        
        if let temp = jsaObject.value(forKey: "jsaheaderDto") as? NSDictionary{
            self.TOJSAHEADERValue = TOJSAHEADER(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaReviewDto") as? NSDictionary{
            self.TOJSAREVIEWValue = TOJSAREVIEW(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaRiskAssesmentDto") as? NSDictionary{
            self.TOJSARISKASSValue = TOJSARISKASS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsappeDto") as? NSDictionary{
            self.TOJSE_PPEValue = TOJSE_PPE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsPressurizedDto") as? NSDictionary{
            self.TOJSAHAZARDPRESSValue = TOJSAHAZARDPRESS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsVisibilityDto") as? NSDictionary{
            self.TOJSAHAZARDVISIBLEValue = TOJSAHAZARDVISIBLE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsPersonnelDto") as? NSDictionary{
            self.TOJSAHAZARDPERSONValue = TOJSAHAZARDPERSON(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardscseDto") as? NSDictionary{
            self.TOJSAHAZARDCSEValue = TOJSAHAZARDCSE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsSimultaneousDto") as? NSDictionary{
            self.TOJSAHAZARDSIMULTANValue = TOJSAHAZARDSIMULTAN(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsIgnitionDto") as? NSDictionary{
            self.TOJSAHAZARDIGNITIONValue = TOJSAHAZARDIGNITION(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsSubstancesDto") as? NSDictionary{
            self.TOJSAHAZARDSUBSValue = TOJSAHAZARDSUBS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsSpillsDto") as? NSDictionary{
            self.TOJSAHAZARDSPILLValue = TOJSAHAZARDSPILL(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsWeatherDto") as? NSDictionary{
            self.TOJSAHAZARDWEATHERValue = TOJSAHAZARDWEATHER(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsHighNoiseDto") as? NSDictionary{
            self.TOJSAHAZARDNOISEValue = TOJSAHAZARDNOISE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsDroppedDto") as? NSDictionary{
            self.TOJSAHAZARDDROPPEDValue = TOJSAHAZARDDROPPED(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsLiftingDto") as? NSDictionary{
            self.TOJSAHAZARDLIFTValue = TOJSAHAZARDLIFT(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsHeightsDto") as? NSDictionary{
            self.TOJSAHAZARDHEIGHTValue = TOJSAHAZARDHEIGHT(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsElectricalDto") as? NSDictionary{
            self.TOJSAHAZARDELECTRICALValue = TOJSAHAZARDELECTRICAL(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsMovingDto") as? NSDictionary{
            self.TOJSAHAZARDMOVINGValue = TOJSAHAZARDMOVING(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsManualDto") as? NSDictionary{
            self.TOJSAHAZARDMANUALValue = TOJSAHAZARDMANUAL(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsToolsDto") as? NSDictionary{
            self.TOJSAHAZARDTOOLSValue = TOJSAHAZARDTOOLS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsFallsDto") as? NSDictionary{
            self.TOJSAHAZARDFALLSValue = TOJSAHAZARDFALLS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsVoltageDto") as? NSDictionary{
            self.TOJSAHAZARDVOLTAGEValue = TOJSAHAZARDVOLTAGE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsExcavationdDto") as? NSDictionary{
            self.TOJSAHAZARDEXCAVATIONValue = TOJSAHAZARDEXCAVATION(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaHazardsMobileDto") as? NSDictionary{
            self.TOJSAHAZARDMOBILEValue = TOJSAHAZARDMOBILE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "jsaLocationDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOJSALOCATION(JSON : each)
                self.TOJSALOCATIONValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "jsaStepsDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOJSASTEPS(JSON : each)
                self.TOJSASTEPSArrayValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "jsaStopTriggerDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOJSASTOP(JSON : each)
                self.TOJSASTOPArrayValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "ptwPeopleDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOPTWPEOPLE(JSON : each) as TOPTWPEOPLE
                self.TOPTWPEOPLEArrayValue.append(temp00)
            }
        }
        
    }
}
