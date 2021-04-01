//
//  MurphyEngineer.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 08/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class MurphyEngineer : NSObject, NSCoding{
    
    var id : String = ""
    var emailID : String = ""
    var displayName :String = ""
    
    override init(){
    }
    
    func encode(with aCoder: NSCoder) {
        aCoder.encode(id, forKey: "id")
        aCoder.encode(emailID, forKey: "emailID")
        aCoder.encode(displayName, forKey: "displayName")
    }
    
    required init?(coder aDecoder: NSCoder) {
        self.id = aDecoder.decodeObject(forKey: "id") as? String ?? ""
        self.emailID = aDecoder.decodeObject(forKey: "emailID") as? String ?? ""
        self.displayName = aDecoder.decodeObject(forKey: "displayName") as? String ?? ""
    }
    
    
    
}
