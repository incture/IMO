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
        
        
        if let temp = jsaObject.value(forKey: "TOJSAHEADER") as? NSDictionary{
            self.TOJSAHEADERValue = TOJSAHEADER(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAREVIEW") as? NSDictionary{
            self.TOJSAREVIEWValue = TOJSAREVIEW(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSARISKASS") as? NSDictionary{
            self.TOJSARISKASSValue = TOJSARISKASS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSE_PPE") as? NSDictionary{
            self.TOJSE_PPEValue = TOJSE_PPE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDPRESS") as? NSDictionary{
            self.TOJSAHAZARDPRESSValue = TOJSAHAZARDPRESS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDVISIBLE") as? NSDictionary{
            self.TOJSAHAZARDVISIBLEValue = TOJSAHAZARDVISIBLE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDPERSON") as? NSDictionary{
            self.TOJSAHAZARDPERSONValue = TOJSAHAZARDPERSON(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDCSE") as? NSDictionary{
            self.TOJSAHAZARDCSEValue = TOJSAHAZARDCSE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDSIMULTAN") as? NSDictionary{
            self.TOJSAHAZARDSIMULTANValue = TOJSAHAZARDSIMULTAN(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDIGNITION") as? NSDictionary{
            self.TOJSAHAZARDIGNITIONValue = TOJSAHAZARDIGNITION(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDSUBS") as? NSDictionary{
            self.TOJSAHAZARDSUBSValue = TOJSAHAZARDSUBS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDSPILL") as? NSDictionary{
            self.TOJSAHAZARDSPILLValue = TOJSAHAZARDSPILL(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDWEATHER") as? NSDictionary{
            self.TOJSAHAZARDWEATHERValue = TOJSAHAZARDWEATHER(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDNOISE") as? NSDictionary{
            self.TOJSAHAZARDNOISEValue = TOJSAHAZARDNOISE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDDROPPED") as? NSDictionary{
            self.TOJSAHAZARDDROPPEDValue = TOJSAHAZARDDROPPED(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDLIFT") as? NSDictionary{
            self.TOJSAHAZARDLIFTValue = TOJSAHAZARDLIFT(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDHEIGHT") as? NSDictionary{
            self.TOJSAHAZARDHEIGHTValue = TOJSAHAZARDHEIGHT(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDELECTRICAL") as? NSDictionary{
            self.TOJSAHAZARDELECTRICALValue = TOJSAHAZARDELECTRICAL(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDMOVING") as? NSDictionary{
            self.TOJSAHAZARDMOVINGValue = TOJSAHAZARDMOVING(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDMANUAL") as? NSDictionary{
            self.TOJSAHAZARDMANUALValue = TOJSAHAZARDMANUAL(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDTOOLS") as? NSDictionary{
            self.TOJSAHAZARDTOOLSValue = TOJSAHAZARDTOOLS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDFALLS") as? NSDictionary{
            self.TOJSAHAZARDFALLSValue = TOJSAHAZARDFALLS(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDVOLTAGE") as? NSDictionary{
            self.TOJSAHAZARDVOLTAGEValue = TOJSAHAZARDVOLTAGE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDEXCAVATION") as? NSDictionary{
            self.TOJSAHAZARDEXCAVATIONValue = TOJSAHAZARDEXCAVATION(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSAHAZARDMOBILE") as? NSDictionary{
            self.TOJSAHAZARDMOBILEValue = TOJSAHAZARDMOBILE(JSON : temp)
        }
        if let temp = jsaObject.value(forKey: "TOJSALOCATION") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOJSALOCATION(JSON : each)
                self.TOJSALOCATIONValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "TOJSASTEPS") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOJSASTEPS(JSON : each)
                self.TOJSASTEPSArrayValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "TOJSASTOP") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOJSASTOP(JSON : each)
                self.TOJSASTOPArrayValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "TOPTWPEOPLE") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOPTWPEOPLE(JSON : each) as TOPTWPEOPLE
                self.TOPTWPEOPLEArrayValue.append(temp00)
            }
        }
        
    }
}
