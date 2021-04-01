//
//  ReviewerService.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 31/08/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData
class ReviewerService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "Reviewer", in: context)!
    }
    // Creates a new object
    func create(department : String, field : String, facility : String, wellPad : String, well : String, response : NSArray) -> Reviewer {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "Reviewer", into: context) as! Reviewer
        
        newItem.department = department
        newItem.field = field
        newItem.facility = facility
        newItem.wellPad = wellPad
        newItem.well = well
        newItem.response = response
        
        return newItem
    }
    // Get one object
    func get(withPredicate queryPredicate: NSPredicate) -> [Reviewer]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Reviewer")
        fetchRequest.predicate = queryPredicate
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [Reviewer]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [Reviewer]()
        }
    }
    // Gets an object by id
    func getById(id: NSManagedObjectID) -> Reviewer? {
        return context.object(with: id) as? Reviewer
    }
    
    // Gets all.
    func getAll() -> [Reviewer]{
        return get(withPredicate: NSPredicate(value:true))
    }
    //    // Delete All.
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
