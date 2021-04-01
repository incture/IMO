//
//  ActionModel+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension ActionModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<ActionModel> {
        return NSFetchRequest<ActionModel>(entityName: "ActionModel")
    }

    @NSManaged public var actionType: String?
    @NSManaged public var permitNumber: Int16
    @NSManaged public var postData: NSDictionary?

}
