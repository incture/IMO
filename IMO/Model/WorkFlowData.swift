//
//  WorkFlowData.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 12/03/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class WorkFlowData : NSObject, NSCoding{
    
    var ticketName : String?
    var postValue : [String : Any]
    var isTriggered : Bool?
    
    override init(){
        postValue =  [:]
    }
    
    func encode(with aCoder: NSCoder) {
        aCoder.encode(ticketName, forKey: "ticketName")
        aCoder.encode(postValue, forKey: "postData")
        aCoder.encode(postValue, forKey: "postData")
    }
    
    required init?(coder aDecoder: NSCoder) {
        self.ticketName = aDecoder.decodeObject(forKey: "ticketName") as? String ?? ""
        self.postValue = aDecoder.decodeObject(forKey: "postValue") as? [String : Any] ?? [:]
        self.isTriggered = aDecoder.decodeObject(forKey: "isTriggered") as? Bool ?? false
        
    }

}
