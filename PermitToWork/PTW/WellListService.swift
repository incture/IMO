//
//  WellListService.swift
//  Task-Management
//
//  Created by Soumya Singh on 22/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class WellListService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "WellList", in: context)!
    }
    
    // Creates a new TaskList
    func create(facility: String, wellPad : String, wells : [String], muwi : [String]) -> WellList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "WellList", into: context) as! WellList
        
        newItem.facility = facility
        newItem.wellPad = wellPad
        newItem.wellList = wells as NSArray
        newItem.muwiList = muwi as NSArray
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> WellList? {
        return context.object(with: id) as? WellList
    }
    
    // Gets all.
    func getAll() -> [WellList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [WellList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "WellList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [WellList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [WellList]()
        }
    }
    
    // Updates a TaskList
    func update(withPredicate queryPredicate: NSPredicate, wells : [String] ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "WellList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [WellList]
            value[0].setValue(wells  as NSArray, forKey: "WellList")
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

