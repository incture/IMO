//
//  PeopleList+CoreDataProperties.swift
//  
//
//  Created by Soumya Singh on 30/05/18.
//
//

import Foundation
import CoreData


extension PeopleList {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<PeopleList> {
        return NSFetchRequest<PeopleList>(entityName: "PeopleList")
    }

    @NSManaged public var facilityOrSite: String?
    @NSManaged private var peopleAddedList: PeopleAddedList?
    
    func setPeopleList(peopleList : PeopleAddedList){
        self.peopleAddedList = peopleList
    }
    
    func getPeopleList() -> PeopleAddedList{
        return self.peopleAddedList!
    }

}
