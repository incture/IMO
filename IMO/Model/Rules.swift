//
//  Rules.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 05/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class Rules : NSObject, NSCoding{
    
    var facility : String?
    var location : String?
    var murphyReviewerEmailId : String?
    var murphyReviewerUserId : String?
    var well : String?
    var lastName : String?
    var firstName : String?
    var displayName : String?
    var department : String?
    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    required init(coder decoder: NSCoder) {
        self.department = decoder.decodeObject(forKey: "department") as? String ?? ""
        self.facility = decoder.decodeObject(forKey: "facility") as? String ?? ""
        self.location = decoder.decodeObject(forKey: "location") as? String ?? ""
        self.murphyReviewerEmailId = decoder.decodeObject(forKey: "murphyReviewerEmailId") as? String ?? ""
        self.murphyReviewerUserId = decoder.decodeObject(forKey: "murphyReviewerUserId") as? String ?? ""
        self.well = decoder.decodeObject(forKey: "well") as? String ?? ""
        self.lastName = decoder.decodeObject(forKey: "lastName") as? String ?? ""
        self.firstName = decoder.decodeObject(forKey: "firstName") as? String ?? ""
        self.displayName = decoder.decodeObject(forKey: "displayName") as? String ?? ""
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(department, forKey: "department")
        coder.encode(facility, forKey: "facility")
        coder.encode(location, forKey: "location")
        coder.encode(murphyReviewerEmailId, forKey: "murphyReviewerEmailId")
        coder.encode(murphyReviewerUserId, forKey: "murphyReviewerUserId")
        coder.encode(well, forKey: "well")
        coder.encode(lastName, forKey: "lastName")
        coder.encode(firstName, forKey: "firstName")
        coder.encode(displayName, forKey: "displayName")
    }
    
    init(JSON: AnyObject) {
        department = JSON.value(forKey: "department") as! String?
        facility = JSON.value(forKey: "facility") as! String?
        location = JSON.value(forKey: "location") as! String?
        murphyReviewerEmailId = JSON.value(forKey: "murphyReviewerEmailId") as! String?
        murphyReviewerUserId = JSON.value(forKey: "murphyReviewerUserId") as! String?
        well = JSON.value(forKey: "well") as! String?
        
        if let lastNameValue = JSON.value(forKey: "murphyReviewerLastName") {
            lastName = lastNameValue as? String
            firstName = JSON.value(forKey: "murphyReviewerFirstName") as! String?
            displayName = firstName! + " " + lastName!
        }
        
        
    }
    
}
