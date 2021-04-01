//
//  JSADetailModelService.swift
//  Task-Management
//
//  Created by Soumya Singh on 22/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class JSADetailModelService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "JSADetailModel", in: context)!
    }
    
    // Creates a new JSADetailModel
    func create(jsa : JSA) -> JSADetailModel {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "JSADetailModel", into: context) as! JSADetailModel
        
        newItem.setJSA(jsa: jsa)
        newItem.permitNumber = Int16(jsa.permitNumber as Int)
        return newItem
    }
    
    // Gets a JSADetailModel by id
    func getById(id: NSManagedObjectID) -> JSADetailModel? {
        return context.object(with: id) as? JSADetailModel
    }
    
    // Gets all.
    func getAll() -> [JSADetailModel]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [JSADetailModel]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "JSADetailModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [JSADetailModel]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [JSADetailModel]()
        }
    }
    
    // Updates a TaskList
    //  func update(updatedFilter: TaskList, task : Task){
    func update(withPredicate queryPredicate: NSPredicate, jsa : JSA ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "JSADetailModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [JSADetailModel]
            value[0].setValue(jsa, forKey: "JSADetailModel")
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
