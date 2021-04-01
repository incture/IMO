//
//  PeopleListService.swift
//  Task-Management
//
//  Created by Soumya Singh on 30/05/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData

class PeopleListService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "PeopleList", in: context)!
    }
    
    // Creates a new TaskList
    func create(peopleList : PeopleAddedList, facilityOrSite : String ) -> PeopleList {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "PeopleList", into: context) as! PeopleList
        newItem.setPeopleList(peopleList: peopleList)
        newItem.facilityOrSite = facilityOrSite
        return newItem
    }
    
    // Gets a person by id
    func getById(id: NSManagedObjectID) -> PeopleList? {
        return context.object(with: id) as? PeopleList
    }
    
    // Gets all.
    func getAll() -> [PeopleList]{
        return get(withPredicate: NSPredicate(value:true))
    }
    
    // Gets all that fulfill the specified predicate.
    // Predicates examples:
    // - NSPredicate(format: "name == %@", "Juan Carlos")
    // - NSPredicate(format: "name contains %@", "Juan")
    func get(withPredicate queryPredicate: NSPredicate) -> [PeopleList]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "PeopleList")
        
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [PeopleList]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [PeopleList]()
        }
    }
    
    // Updates a PeopleList
    func update(withPredicate queryPredicate: NSPredicate, peopleListModel: PeopleAddedList) {
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "PeopleList")
        fetchRequest.predicate = queryPredicate
        
        do {
            let response = try context.fetch(fetchRequest)
            let value = response as! [PeopleList]
            value[0].setValue(peopleListModel, forKey: "PeopleAddedList")
            // return true
            
        } catch let error as NSError {
            // failure
            print(error)
            //return false
        }
    }
    
    // Deletes a PeopleList
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

