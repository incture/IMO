//
//  PermitModelService.swift
//  Task-Management
//
//  Created by Soumya Singh on 22/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class PermitModelService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "PermitModel", in: context)!
    }
    
    // Creates a new TaskList
    func create(detailData: PermitList, permitNumber: Int, permitType : String, facilityOrSite : String) -> PermitModel {
   
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "PermitModel", into: context) as! PermitModel
        newItem.setPermitList(permitList: detailData)
        newItem.permitNumber = Int16(permitNumber)
        newItem.permitType = permitType
        newItem.facilityOrSite = facilityOrSite
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> PermitModel? {
        return context.object(with: id) as? PermitModel
    }
    
    // Gets all.
    func getAll() -> [PermitModel]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [PermitModel]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "PermitModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [PermitModel]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [PermitModel]()
        }
    }
    
    // Updates a TaskList
    //  func update(updatedFilter: TaskList, task : Task){
    func update(withPredicate queryPredicate: NSPredicate, permitModel: PermitModel ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "PermitModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [PermitModel]
            value[0].setValue(permitModel, forKey: "PermitModel")
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
            DispatchQueue.main.async {
                self.context.delete(personToDelete)
            }
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
        DispatchQueue.main.async {
        do{
            try self.context.save()
        } catch let error as NSError {
            // failure
            print(error)
        }
        }
    }
}



