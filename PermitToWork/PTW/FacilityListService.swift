//
//  FacilityListService.swift
//  Task-Management
//
//  Created by Soumya Singh on 22/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class FacilityListService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "FacilityList", in: context)!
    }
    
    // Creates a new TaskList
    func create(facility: [String]) -> FacilityList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "FacilityList", into: context) as! FacilityList
        
        newItem.facility = facility as NSArray
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> FacilityList? {
        return context.object(with: id) as? FacilityList
    }
    
    // Gets all.
    func getAll() -> [FacilityList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [FacilityList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "FacilityList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [FacilityList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [FacilityList]()
        }
    }
    
    // Updates a TaskList
    //  func update(updatedFilter: TaskList, task : Task){
    func update(withPredicate queryPredicate: NSPredicate, facility: [String] ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "FacilityList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [FacilityList]
            value[0].setValue(facility as NSArray, forKey: "FacilityList")
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

