//
//  TestsModel.swift
//  
//
//  Created by Soumya Singh on 12/04/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import Foundation
class TestsModel : NSObject, NSCoding {
    
//    var O2 : String = ""
//    var toxicType : String = ""
//    var toxicResult : String = ""
//    var flammableGas : String = ""
//    var othersType : String = ""
//    var othersResult : String = ""
//    var Date : String = ""
//    var Time : String = ""
    
    var O2 : Float = 0.0
    var toxicType : String = ""
    var toxicResult : Float = 0.0
    var flammableGas : String = ""
    var othersType : String = ""
    var othersResult : Float = 0.0
    var Date : String = ""
    var Time : String = ""
    
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.O2 = decoder.decodeFloat(forKey: "O2")
        self.toxicType = decoder.decodeObject(forKey: "toxicType") as? String ?? ""
        self.toxicResult = decoder.decodeFloat(forKey: "toxicResult")
        self.flammableGas = decoder.decodeObject(forKey: "flammableGas") as? String ?? ""
        self.othersType = decoder.decodeObject(forKey: "othersType") as? String ?? ""
        self.othersResult = decoder.decodeFloat(forKey: "othersResult")
        self.Date = decoder.decodeObject(forKey: "Date") as? String ?? ""
        self.Time = decoder.decodeObject(forKey: "Time") as? String ?? ""
    
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(O2, forKey: "O2")
        coder.encode(toxicType, forKey: "toxicType")
        coder.encode(flammableGas, forKey: "flammableGas")
        coder.encode(toxicResult, forKey: "toxicResult")
        coder.encode(othersType, forKey: "othersType")
        coder.encode(othersResult, forKey: "othersResult")
        coder.encode(Date, forKey: "Date")
        coder.encode(Time, forKey: "Time")
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
