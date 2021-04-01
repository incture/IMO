//
//  Location+CoreDataProperties.swift
//  MurphyDFT-Final
//
//  Created by prakash on 24/01/20.
//  Copyright © 2020 SAP. All rights reserved.
//
//

import Foundation
import CoreData


extension Location {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Location> {
        return NSFetchRequest<Location>(entityName: "Location")
    }

    @NSManaged public var locationName: String?
    @NSManaged public var locationType: String?
    @NSManaged public var response: NSDictionary?

}
