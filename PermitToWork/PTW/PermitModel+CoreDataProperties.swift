//
//  PermitModel+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension PermitModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<PermitModel> {
        return NSFetchRequest<PermitModel>(entityName: "PermitModel")
    }

    @NSManaged private var listData: PermitList?
    @NSManaged public var permitNumber: Int16
    @NSManaged public var permitType: String?
    @NSManaged public var facilityOrSite: String?
    
    func setPermitList(permitList : PermitList){
        listData = permitList
    }
    
    func getPermitList() -> PermitList{
        return listData!
    }

}
