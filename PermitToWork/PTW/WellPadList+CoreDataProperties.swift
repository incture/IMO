//
//  WellPadList+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension WellPadList {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<WellPadList> {
        return NSFetchRequest<WellPadList>(entityName: "WellPadList")
    }

    @NSManaged public var facility: String?
    @NSManaged public var wellPads: NSArray?

}
