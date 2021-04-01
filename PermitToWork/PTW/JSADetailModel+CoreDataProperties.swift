//
//  JSADetailModel+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 22/05/18.
//
//

import Foundation
import CoreData


extension JSADetailModel {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<JSADetailModel> {
        return NSFetchRequest<JSADetailModel>(entityName: "JSADetailModel")
    }

    @NSManaged private var detailData: JSA?
    @NSManaged public var permitNumber: Int16
    
    func setJSA(jsa : JSA){
        detailData = jsa
    }
    
    func getJSA() -> JSA{
        return detailData!
    }

}
