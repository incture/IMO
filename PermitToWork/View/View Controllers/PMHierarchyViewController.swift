//
//  PMHierarchyViewController.swift
////
//  Created by Mohan on 27/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
import SAPFiori
import NotificationCenter
//protocol HierarchyViewDelegate: class {
//    func didSelectItem(Location: String)
//}

protocol GenericItem {
    var ItemValue1:String?{get set}
    var ItemValue2:String?{get set}
    var ItemValue3:String?{get set}
    
    static var identifier : String{ get}
}

class HierarchyItem:GenericItem {
    var ItemValue3: String?
    
    static var identifier: String{
        get{
            return "HierarchyItem"
        }
    }
    var ItemValue1: String?
    var ItemValue2: String?
    var ItemType : String?
    init(item : NSDictionary) {
        self.ItemValue1 = item.value(forKey: "locationText") as? String
        self.ItemValue2 = item.value(forKey: "location") as? String
        self.ItemType = item.value(forKey: "locationType") as? String
    }
}

extension Notification.Name {
    
    static let DidSelectLocation = Notification.Name(rawValue: "didSelectLocation")
}
