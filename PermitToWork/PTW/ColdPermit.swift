//
//  ColdPermit.swift
//  
//
//  Created by Soumya Singh on 12/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class ColdPermit : NSObject, NSCoding{
    
    var header = PermitHeader()
    var workTypeCW = WorkTypeCW()
    var workTypeHW = WorkTypeHW()
    var workTypeCSE = WorkTypeCSP()
    var docs = DocReqd()
    var atmosphericTesting = AtmosphericTesting()
    var testsResult = TestResult()
    var signOff = SignOff()
    var firewatch = FireWatch()
   // var rescue = RescuePlan()
    var closeOut = CloseOut()
    var peopleList = [People]()
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    required init(coder decoder: NSCoder) {
        self.header = decoder.decodeObject(forKey: "header") as? PermitHeader ?? PermitHeader()
        self.workTypeCW = decoder.decodeObject(forKey: "workTypeCW") as? WorkTypeCW ?? WorkTypeCW()
        self.workTypeHW = decoder.decodeObject(forKey: "workTypeHW") as? WorkTypeHW ?? WorkTypeHW()
        self.workTypeCSE = decoder.decodeObject(forKey: "workTypeCSE") as? WorkTypeCSP ?? WorkTypeCSP()
        self.docs = decoder.decodeObject(forKey: "docs") as? DocReqd ?? DocReqd()
        self.atmosphericTesting = decoder.decodeObject(forKey: "atmosphericTesting") as? AtmosphericTesting ?? AtmosphericTesting()
        self.testsResult = decoder.decodeObject(forKey: "testsResult") as? TestResult ?? TestResult()
        self.signOff = decoder.decodeObject(forKey: "testsResult") as? SignOff ?? SignOff()
        self.firewatch = decoder.decodeObject(forKey: "firewatch") as? FireWatch ?? FireWatch()
        //self.rescue = decoder.decodeObject(forKey: "rescue") as! RescuePlan
        self.closeOut = decoder.decodeObject(forKey: "closeOut") as? CloseOut ?? CloseOut()
        self.peopleList = decoder.decodeObject(forKey: "peopleList") as? [People] ?? [People]()

    }

    func encode(with coder: NSCoder) {
        coder.encode(header, forKey: "header")
        coder.encode(workTypeCW, forKey: "workTypeCW")
        coder.encode(workTypeHW, forKey: "workTypeHW")
        coder.encode(workTypeCSE, forKey: "workTypeCSE")
        coder.encode(docs, forKey: "docs")
        coder.encode(atmosphericTesting, forKey: "atmosphericTesting")
        coder.encode(testsResult, forKey: "testsResult")
        coder.encode(signOff, forKey: "signOff")
        coder.encode(firewatch, forKey: "firewatch")
        //coder.encode(rescue, forKey: "rescue")
        coder.encode(closeOut, forKey: "closeOut")
        coder.encode(peopleList, forKey: "peopleList")
    }
    
    init(JSON: AnyObject) {
        
    }
}
