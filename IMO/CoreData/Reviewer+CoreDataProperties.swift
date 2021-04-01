//
//  Reviewer+CoreDataProperties.swift
//  MurphyDFT-Final
//
//  Created by prakash on 24/01/20.
//  Copyright © 2020 SAP. All rights reserved.
//
//

import Foundation
import CoreData


extension Reviewer {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Reviewer> {
        return NSFetchRequest<Reviewer>(entityName: "Reviewer")
    }

    @NSManaged public var department: String?
    @NSManaged public var facility: String?
    @NSManaged public var field: String?
    @NSManaged public var response: NSArray?
    @NSManaged public var well: String?
    @NSManaged public var wellPad: String?

}
