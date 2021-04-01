//
//  FailedTicket.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 07/03/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class FailedTicket : NSObject{
    
    var errorMessage : String?
    var Department : String?
    var CreatedDate : String?
    var Location : String?
    var CreatedTime : String?
    var vendorRefNo : String?
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(dictionaryData: data)
    }
    
    init(JSON: AnyObject) {
        errorMessage = JSON.value(forKey: "Message") as! String?
        vendorRefNo = JSON.value(forKey: "VendorRefNumber") as! String?
        Department = JSON.value(forKey: "Department") as! String?
        Location = JSON.value(forKey: "Location") as! String?
        //UIcreatedOn = JSON.value(forKey: "UIcreatedOn") as! String?
        if let create_time = JSON.value(forKey: "UicreatedOn"){
            CreatedDate = create_time as? String
        }
        //UIcreationTime = JSON.value(forKey: "UIcreationTime") as! String?
        if let start_date = JSON.value(forKey: "UicreationTime"){
            CreatedTime = start_date as? String
        }
        
        
    }
}
