//
//  ActList+CoreDataProperties.swift
//
//
//  Created by Raghu Mohan on 04/06/17.
//
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

import Foundation
import CoreData

extension ActList {
    
    @NSManaged var dateSync: String?
    @NSManaged var empId: String?
    @NSManaged var actionType: String?
    @NSManaged var extraParam: String?
    @NSManaged var postObj: NSData?
    
}
