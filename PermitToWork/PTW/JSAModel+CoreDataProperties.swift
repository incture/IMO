//
//  JSAModel+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension JSAModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<JSAModel> {
        return NSFetchRequest<JSAModel>(entityName: "JSAModel")
    }

    @NSManaged private var listData: JSAList?
    @NSManaged public var permitNumber: Int16
    @NSManaged public var facilityOrSite: String?
    
    func setJSAList(jsaList : JSAList){
        listData = jsaList
    }
    
    func getJSA() -> JSAList{
        return listData!
    }

}
