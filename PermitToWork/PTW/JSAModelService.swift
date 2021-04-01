//
//  JSAListService.swift
//  Task-Management
//
//  Created by Soumya Singh on 22/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class JSAModelService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "JSAModel", in: context)!
    }
    
    // Creates a new TaskList
    func create(listData : JSAList, permitNumber : Int, facilityOrSite : String ) -> JSAModel {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "JSAModel", into: context) as! JSAModel
        newItem.setJSAList(jsaList: listData)
        newItem.permitNumber = Int16(permitNumber)
        newItem.facilityOrSite = facilityOrSite
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> JSAModel? {
        return context.object(with: id) as? JSAModel
    }
    
    // Gets all.
    func getAll() -> [JSAModel]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [JSAModel]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "JSAModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [JSAModel]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [JSAModel]()
        }
    }
    
    // Updates a TaskList
    //  func update(updatedFilter: TaskList, task : Task){
    func update(withPredicate queryPredicate: NSPredicate, jsaModel: JSAModel) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "JSAModel")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [JSAModel]
            value[0].setValue(jsaModel, forKey: "JSAModel")
            // return true
            
        } catch let error as NSError {
            // failure
            print(error)
            //return false
        }
    }
    
    // Deletes a TaskList
    func delete(id: NSManagedObjectID){
        DispatchQueue.main.async {
            if let personToDelete = self.getById(id: id){
                self.context.delete(personToDelete)
            }
        }
    }
    
    func deleteAll() {
        DispatchQueue.main.async {
            let fList = self.getAll()
        for fitem in fList {
            self.delete(id: fitem.objectID)
        }
            self.saveChanges()
        }
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


