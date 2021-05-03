//
//  ReadPermit.swift
//  Task-Management
//
//  Created by Soumya Singh on 25/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class ReadPermit : NSObject
{
    var TOPTWHEADERValue = [TOPTWHEADER]()
    var TOPTWREQDOCValue = [TOPTWREQDOC]()
    var TOPTWTESTRECValue = TOPTWTESTREC()
    var TOPTWTESTRESValue = [TOPTWTESTRES]()
    var TOPTWCWPWORKValue = TOPTWCWPWORK()
    var TOPTWHWPWORKValue = TOPTWHWPWORK()
    var TOPTWCSEWORKValue = TOPTWCSPWORK()
    var TOPTWPEOPLEArrayValue = [TOPTWPEOPLE]()
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        let jsaObject = JSON
        if let temp = jsaObject.value(forKey: "ptwHeaderDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOPTWHEADER(JSON : each) as TOPTWHEADER
                self.TOPTWHEADERValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "ptwRequiredDocumentDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOPTWREQDOC(JSON : each) as TOPTWREQDOC
                self.TOPTWREQDOCValue.append(temp00)
            }
        }
        if let temp = jsaObject.value(forKey: "TOPTWTESTREC") as? NSDictionary{
            //for each in temp{
                //let temp00 = TOPTWTESTREC(JSON : each) as TOPTWTESTREC
                self.TOPTWTESTRECValue = TOPTWTESTREC(JSON : temp)
            //}
        }
        if let temp = jsaObject.value(forKey: "TOPTWTESTRES") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOPTWTESTRES(JSON : each) as TOPTWTESTRES
                self.TOPTWTESTRESValue.append(temp00)
            }
        }
        
        if let temp = jsaObject.value(forKey: "ptwCwpWorkTypeDto") as? NSDictionary{
            self.TOPTWCWPWORKValue = TOPTWCWPWORK(JSON : temp)
        }
        
        if let temp = jsaObject.value(forKey: "ptwHwpWorkTypeDto") as? NSDictionary{
            self.TOPTWHWPWORKValue = TOPTWHWPWORK(JSON : temp)
        }
        
        if let temp = jsaObject.value(forKey: "ptwCseWorkTypeDto") as? NSDictionary{
            self.TOPTWCSEWORKValue = TOPTWCSPWORK(JSON : temp)
        }
        
        if let temp = jsaObject.value(forKey: "ptwPeopleDtoList") as? [NSDictionary]{
            for each in temp{
                let temp00 = TOPTWPEOPLE(JSON : each) as TOPTWPEOPLE
                self.TOPTWPEOPLEArrayValue.append(temp00)
            }
        }
        
    }
}
