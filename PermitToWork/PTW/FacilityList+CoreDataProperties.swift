//
//  FacilityList+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension FacilityList {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<FacilityList> {
        return NSFetchRequest<FacilityList>(entityName: "FacilityList")
    }

    @NSManaged public var facility: NSArray?

}
