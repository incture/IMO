//
//  CloseOut.swift
//  Task-Management
//
//  Created by Soumya Singh on 28/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class CloseOut : NSObject, NSCoding {
    
    var permitNo : Int = 0
    var picName : String = ""
    var workCompleted : Int = 0
    var closedBy : String = ""
    var closedDate : String = ""
    var workStatusComments : String = ""
    var permitHolderConfirmation : Int = 0
    var PICconfirmation : Int = 0
    
    override init(){
    }
    
    required init(coder decoder: NSCoder) {
        self.permitNo = decoder.decodeInteger(forKey: "permitNo")
        self.picName = decoder.decodeObject(forKey: "picName") as? String ?? ""
        self.workCompleted = decoder.decodeInteger(forKey: "workCompleted")
        self.closedBy = decoder.decodeObject(forKey: "closedBy") as? String ?? ""
        self.closedDate = decoder.decodeObject(forKey: "closedDate") as? String ?? ""
        self.workStatusComments = decoder.decodeObject(forKey: "workStatusComments") as? String ?? ""
        self.permitHolderConfirmation = decoder.decodeInteger(forKey: "permitHolderConfirmation")
        self.PICconfirmation = decoder.decodeInteger(forKey: "PICconfirmation")
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(permitNo, forKey: "permitNo")
        coder.encode(picName, forKey: "picName")
        coder.encode(workCompleted, forKey: "workCompleted")
        coder.encode(closedBy, forKey: "closedBy")
        coder.encode(closedDate, forKey: "closedDate")
        coder.encode(workStatusComments, forKey: "workStatusComments")
        coder.encode(permitHolderConfirmation, forKey: "permitHolderConfirmation")
        coder.encode(PICconfirmation, forKey: "PICconfirmation")
    }
}
