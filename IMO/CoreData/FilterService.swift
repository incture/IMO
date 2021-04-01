//
//  FilterList.swift
//  Workbox
//
//  Created by Vaishak iyer on 04/06/17.
//  Copyright © 2017 Incture Technologies. All rights reserved.
//

import Foundation
import CoreData




class FilterService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "FilterList", in: context)!
    }
    
    // Creates a new Person
    func create(instanceId: String, taskType : String, empId : String, dateSync : String, FilterListObject : NSData) -> FilterList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "FilterList", into: context) as! FilterList
        
        newItem.instanceId = instanceId as String
        newItem.taskType = taskType as String
        newItem.empId = empId as String
        newItem.dateSync = ""//Helper.stringForDate(NSDate(), format: "ddMMyyyy")
        
        //encrpt string to data
        newItem.filterListObject = FilterListObject
        
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> FilterList? {
        return context.object(with: id) as? FilterList
    }
    
    // Gets all.
    func getAll() -> [FilterList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [FilterList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "FilterList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [FilterList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [FilterList]()
        }
    }
    
    // Updates a person
    func update(updatedFilter: FilterList){
        if let filter = getById(id: updatedFilter.objectID){
            filter.empId = updatedFilter.empId
            filter.instanceId = updatedFilter.instanceId
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
