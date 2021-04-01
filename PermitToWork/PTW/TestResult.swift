//
//  TestResult.swift
//  Murphy_PWT_iOS
//
//  Created by Soumya Singh on 12/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class TestResult : NSObject, NSCoding{
    
    var specialPrecaution : String = ""
    var Name : String = ""
    var serialNumber : String = ""
    var serialNo : Int = 0
    var preStartTests = [TestsModel]()
    var workPeriodTests = [TestsModel]()
    
    override init(){
    }
    required init(coder decoder: NSCoder) {
        self.specialPrecaution = decoder.decodeObject(forKey: "specialPrecaution") as? String ?? ""
        self.Name = decoder.decodeObject(forKey: "Name") as? String ?? ""
        self.serialNumber = decoder.decodeObject(forKey: "serialNumber") as? String ?? ""
        self.preStartTests = decoder.decodeObject(forKey: "preStartTests") as? [TestsModel] ?? [TestsModel]()
        self.workPeriodTests = decoder.decodeObject(forKey: "workPeriodTests") as? [TestsModel] ?? [TestsModel]()
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(specialPrecaution, forKey: "specialPrecaution")
        coder.encode(Name, forKey: "Name")
        coder.encode(serialNumber, forKey: "serialNumber")
        coder.encode(preStartTests, forKey: "preStartTests")
        coder.encode(workPeriodTests, forKey: "workPeriodTests")
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
