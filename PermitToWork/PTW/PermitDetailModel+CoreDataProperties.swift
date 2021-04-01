//
//  PermitDetailModel+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension PermitDetailModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<PermitDetailModel> {
        return NSFetchRequest<PermitDetailModel>(entityName: "PermitDetailModel")
    }

    @NSManaged private var detailData: JSA?
    @NSManaged public var permitNumber: Int16
    @NSManaged public var permitType: String?
    
    func setPermitData(detailData : JSA){
        self.detailData = detailData
    }
    
    func getPermitData() -> JSA{
        return self.detailData!
    }

}
