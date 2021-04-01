//
//  TaskList.swift
//  Workbox
//
//  Created by Vaishak iyer on 04/06/17.
//  Copyright © 2017 Incture Technologies. All rights reserved.
//

import Foundation
import CoreData




class TaskService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "TaskList", in: context)!
    }
    
    // Creates a new Person
    func create(instanceId: String, taskType : String, empId : String, dateSync : String, taskListObject : NSData) -> TaskList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "TaskList", into: context) as! TaskList
        
        newItem.instanceId = instanceId as String
        newItem.taskType = taskType as String
        newItem.empId = empId as String
        newItem.dateSync = ""//Helper.stringForDate(NSDate(), format: "ddMMyyyy")
        
        //encrpt string to data
        newItem.taskListObject = taskListObject
        
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> TaskList? {
        return context.object(with: id) as? TaskList
    }
    
    // Gets all.
    func getAll() -> [TaskList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [TaskList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "TaskList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [TaskList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [TaskList]()
        }
    }
    
    // Updates a person
    func update(updatedFilter: TaskList){
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
