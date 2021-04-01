//
//  CreateTicket.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 06/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class CreateTicket : NSObject{
    
    var VendorRefNo : String = ""
    var Department : String = ""
    var CreatedDate : String = ""
    var Location : String = ""
    var Facility : String = ""
    var CreationTime : String = ""
    var startDate : String = ""
    var endDate : String = ""
    var startTime : String = ""
    var endTime : String = ""
    var jobPerformedBy : String = ""
    var MurphyReviewer : MurphyEngineer = MurphyEngineer()
    //Mohan
    var ServiceType : String = ""
    var Quantity : String = ""
    var Unit : String = ""
    
    var attachments = [Attachment]()
    var activity = [Activity]()
    var comments = [Comment]()
    var returnHeader : Return = Return()
    
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(dictionaryData: data)
    }
}
