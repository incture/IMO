//
//  ConfinedSpacePermit.swift
////
//  Created by Soumya Singh on 12/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class ConfinedSpacePermit : NSObject{ //, NSCoding{
    
//    var header = PermitHeader()
//    var workType = [Bool]()
//    var docs = [Bool]()
//    var atmosphericTesting = AtmosphericTesting()
//    var testsResult = TestResult()
    
    
    var header = PermitHeader()
    var workType = WorkTypeCSP()
    var docs = DocReqd()
    var atmosphericTesting = AtmosphericTesting()
    var testsResult = TestResult()
    var signOff = SignOff()
    var closeOut = CloseOut()
    var peopleList = [People]()
    var personnelLog = [People]()
    
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
}
