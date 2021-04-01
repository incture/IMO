//
//  ActionModelService.swift
//  Task-Management
//
//  Created by Soumya Singh on 23/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class ActionModelService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "ActionModel", in: context)!
    }
    
    // Creates a new TaskList
    func create(postData: NSDictionary, permitNumber: Int, actionType : String) -> ActionModel {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "ActionModel", into: context) as! ActionModel
        newItem.postData = postData
        newItem.permitNumber = Int16(permitNumber)
        newItem.actionType = actionType
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> ActionModel? {
        return context.object(with: id) as? ActionModel
    }
    
    // Gets all.
    func getAll() -> [ActionModel]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [ActionModel]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "ActionModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [ActionModel]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [ActionModel]()
        }
    }
    
    // Updates a TaskList
    //  func update(updatedFilter: TaskList, task : Task){
    func update(withPredicate queryPredicate: NSPredicate, permitModel: ActionModel ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "ActionModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [ActionModel]
            value[0].setValue(permitModel, forKey: "ActionModel")
            // return true
            
        } catch let error as NSError {
            // failure
            print(error)
            //return false
        }
    }
    
    // Deletes a TaskList
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
