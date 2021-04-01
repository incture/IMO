//
//  ActList.swift
//  Workbox
//
//  Created by Vaishak iyer on 04/06/17.
//  Copyright © 2017 Incture Technologies. All rights reserved.
//

import Foundation
import CoreData

class ActService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "ActList", in: context)!
    }
    
    // Creates a new Person
    func create(extraParam: String, actionType : String, empId : String, dateSync : String, PostObj : NSData) -> ActList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "ActList", into: context) as! ActList
        
        newItem.extraParam = extraParam as String
        newItem.actionType = actionType as String
        newItem.empId = empId as String
        newItem.dateSync = dateSync as String//""//Helper.stringForDate(NSDate(), format: "ddMMyyyy")
        
        //encrpt string to data
        newItem.postObj = PostObj
        
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> ActList? {
        return context.object(with: id) as? ActList
    }
    
    // Gets all.
    func getAll() -> [ActList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [ActList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "ActList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [ActList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [ActList]()
        }
    }
    
    // Updates a person
    func update(updatedFilter: ActList){
        if let filter = getById(id: updatedFilter.objectID){
            filter.empId = updatedFilter.empId
            filter.extraParam = updatedFilter.extraParam
        }
    }
    
    // Deletes a person
    func delete(id: NSManagedObjectID){
        if let personToDelete = getById(id: id){
            context.delete(personToDelete)
        }
    }
    
    func deleteAll() {
        let fList = getAll()
        
        for fitem in fList {
            delete(id: fitem.objectID)
        }
        saveChanges()
    }
    
    // Saves all changes
    func saveChanges(){
        do{
            try context.save()
        } catch let error as NSError {
            // failure
            print(error)
        }
    }
}
