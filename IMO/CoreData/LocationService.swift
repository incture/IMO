//
//  LocationService.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 04/08/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
import CoreData
class LocationService{
    
    var context: NSManagedObjectContext
    var entity: NSEntityDescription
    init(context: NSManagedObjectContext){
        self.context = context
        self.entity = NSEntityDescription.entity(forEntityName: "Location", in: context)!
    }
    // Creates a new object
    func create(locationType : String, locationName : String,  response : NSDictionary) -> Location {
        
        let newItem = NSEntityDescription.insertNewObject(forEntityName: "Location", into: context) as! Location
        
        newItem.locationType = locationType
        newItem.locationName = locationName
        newItem.response = response
        
        return newItem
    }
    // Get one object
    func get(withPredicate queryPredicate: NSPredicate) -> [Location]{
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Location")
        fetchRequest.predicate = queryPredicate        
        do {
            let response = try context.fetch(fetchRequest)
            return response as! [Location]
            
        } catch let error as NSError {
            // failure
            print(error)
            return [Location]()
        }
    }
    // Gets an object by id
    func getById(id: NSManagedObjectID) -> Location? {
        return context.object(with: id) as? Location
    }
    
    // Gets all.
    func getAll() -> [Location]{
        return get(withPredicate: NSPredicate(value:true))
    }
    // Delete.
    func delete(id: NSManagedObjectID){
        if let personToDelete = getById(id: id){
            context.delete(personToDelete)
        }
    }
    // Delete All.
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
