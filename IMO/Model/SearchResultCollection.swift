//
//  SearchResultCollection.swift
//  MurphyDFT-Final
//
//  Created by Mohan on 17/07/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
protocol GenericItem {
    var ItemValue1:String?{get set}
    var ItemValue2:String?{get set}
    var ItemValue3:String?{get set}

    static var identifier : String{ get}
}
class CostCenter:GenericItem {
    var ItemValue3: String?
    var ItemValue1: String?
    var ItemValue2: String?
    
    static var identifier: String{
        get{
            return "CostCenter"
        }
    }
    
    init(item : NSObject) {
        self.ItemValue1 = item.value(forKey: "KOSTL")as? String
        self.ItemValue2 = item.value(forKey: "LTEXT") as? String
    }
}

class SESApprover:GenericItem {
    var ItemValue3: String?
    var ItemValue1: String?
    var ItemValue2: String?
    
    static var identifier: String{
        get{
            return "SESApprover"
        }
    }
    
    init(item : NSObject) {
        self.ItemValue1 = item.value(forKey: "EmailId")as? String
    }
}
class PO:GenericItem {
    var ItemValue3: String?
    var ItemValue1: String?
    var ItemValue2: String?
    
    static var identifier: String{
        get{
            return "PO"
        }
    }
    
    init(item : NSObject) {
        if let ponum = item.value(forKey: "PO_Number"){
            self.ItemValue1 = ponum as? String
        }
        if let ponum = item.value(forKey: "PO_Desc"){
            self.ItemValue2 = ponum as? String
        }
        if let ponum = item.value(forKey: "PO_Amount"){
            self.ItemValue3 = ponum as? String
        }
    }
}
class WO:GenericItem {
    var ItemValue3: String?
    var ItemValue1: String?
    var ItemValue2: String?
    
    static var identifier: String{
        get{
            return "WO"
        }
    }
    
    init(item : NSObject) {
        self.ItemValue1 = item.value(forKey: "WO_Number")as? String
        self.ItemValue2 = item.value(forKey: "Work_Order_text") as? String
    }
}
class WBS:GenericItem {
    var ItemValue3: String?
    var ItemValue1: String?
    var ItemValue2: String?
    
    static var identifier: String{
        get{
            return "WBS"
        }
    }
    
    init(item : NSObject) {
        self.ItemValue1 = item.value(forKey: "posid")as? String
        self.ItemValue2 = item.value(forKey: "post1")as? String
    }
}
class MurphyReviewer:GenericItem {
    var ItemValue3: String?
    var ItemValue1: String?
    var ItemValue2: String?
    
    static var identifier: String{
        get{
            return "MurphyReviewer"
        }
    }
    
    init(item : [String : String]) {
        self.ItemValue1 = item["first_name"]!  + " " + item["last_name"]!
        self.ItemValue2 = item["email_id"]!
        self.ItemValue3 = item["puser_id"]!
}
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

