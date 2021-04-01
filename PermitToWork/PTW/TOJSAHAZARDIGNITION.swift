//
//  TOJSAHAZARDIGNITION.swift
//  Task-Management
//
//  Created by Soumya Singh on 16/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class TOJSAHAZARDIGNITION : NSObject
{
    var permitNumber : Int = 0
    var ignitionSources : Int = 0
    var removeCombustibleMaterials : Int = 0
    var provideFireWatch : Int = 0
    var implementAbrasiveBlastingControls : Int = 0
    var conductContinuousGasTesting : Int = 0
    var earthForStaticElectricity : Int = 0
    
    override init(){
    }
    
    init(JSON : NSDictionary){
        
        if let temp0 = JSON["permitNumber"] as? Int {
            self.permitNumber = temp0
        }
        if let temp1 = JSON["ignitionSources"] as? Int {
            self.ignitionSources = temp1
        }
        if let temp5 = JSON["removeCombustibleMaterials"] as? Int {
            self.removeCombustibleMaterials = temp5
        }
        if let temp5 = JSON["provideFireWatch"] as? Int {
            self.provideFireWatch = temp5
        }
        if let temp5 = JSON["implementAbrasiveBlastingControls"] as? Int {
            self.implementAbrasiveBlastingControls = temp5
        }
        if let temp5 = JSON["conductContinuousGasTesting"] as? Int {
            self.conductContinuousGasTesting = temp5
        }
        if let temp5 = JSON["earthForStaticElectricity"] as? Int {
            self.earthForStaticElectricity = temp5
        }
    }
}
