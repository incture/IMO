//
//  TOJSAHAZARDCSE.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDSIMULTAN : NSObject
{
    var permitNumber : Int = 0
    var simultaneousOperations : Int = 0
    var followSimopsMatrix : Int = 0
    var mocRequiredFor : Int = 0
    var interfaceBetweenGroups : Int = 0
    var useBarriersAnd : Int = 0
    var havePermitSigned : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["simultaneousOperations"] as? Int {
            self.simultaneousOperations = temp1
        }
        if let temp5 = JSON["followSimopsMatrix"] as? Int {
            self.followSimopsMatrix = temp5
        }
        if let temp5 = JSON["mocRequiredFor"] as? Int {
            self.mocRequiredFor = temp5
        }
        if let temp5 = JSON["interfaceBetweenGroups"] as? Int {
            self.interfaceBetweenGroups = temp5
        }
        if let temp5 = JSON["useBarriersAnd"] as? Int {
            self.useBarriersAnd = temp5
        }
        if let temp5 = JSON["havePermitSigned"] as? Int {
            self.havePermitSigned = temp5
        }
    }
}

