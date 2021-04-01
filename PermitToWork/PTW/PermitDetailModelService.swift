//
//  PermitDetailModelService.swift
//  Task-Management
//
//  Created by Soumya Singh on 23/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class PermitDetailModelService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "PermitDetailModel", in: context)!
    }
    
    // Creates a new TaskList
    func create(detailData: JSA, permitNumber: Int, permitType : String) -> PermitDetailModel {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "PermitDetailModel", into: context) as! PermitDetailModel
        newItem.setPermitData(detailData: detailData)
        newItem.permitNumber = Int16(permitNumber)
        newItem.permitType = permitType
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> PermitDetailModel? {
        return context.object(with: id) as? PermitDetailModel
    }
    
    // Gets all.
    func getAll() -> [PermitDetailModel]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [PermitDetailModel]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "PermitDetailModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [PermitDetailModel]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [PermitDetailModel]()
        }
    }
    
    // Updates a TaskList
    //  func update(updatedFilter: TaskList, task : Task){
    func update(withPredicate queryPredicate: NSPredicate, permitModel: PermitDetailModel ) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "PermitDetailModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [PermitDetailModel]
            value[0].setValue(permitModel, forKey: "PermitDetailModel")
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
