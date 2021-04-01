//
//  WellList+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 29/05/18.
//
//

import Foundation
import CoreData


extension WellList {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<WellList> {
        return NSFetchRequest<WellList>(entityName: "WellList")
    }

    @NSManaged public var facility: String?
    @NSManaged public var wellList: NSArray?
    @NSManaged public var wellPad: String?
    @NSManaged public var muwiList: NSArray?

}
