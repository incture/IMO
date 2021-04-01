//
//  WellPadListService.swift
//  Task-Management
//
//  Created by Soumya Singh on 22/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class WellPadListService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "WellPadList", in: context)!
    }
    
    // Creates a new TaskList
    func create(facility: String, wellPads : [String]) -> WellPadList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "WellPadList", into: context) as! WellPadList
        
        newItem.facility = facility
        newItem.wellPads = wellPads as NSArray
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> WellPadList? {
        return context.object(with: id) as? WellPadList
    }
    
    // Gets all.
    func getAll() -> [WellPadList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [WellPadList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "WellPadList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [WellPadList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [WellPadList]()
        }
    }
    
    // Updates a TaskList
    func update(withPredicate queryPredicate: NSPredicate, wellPads: [String] ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "WellPadList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [WellPadList]
            value[0].setValue(wellPads as NSArray, forKey: "WellPadList")
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


