//
//  FieldTicket.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 05/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation
class FieldTicket : NSObject, NSCoding{
    
    @objc var FieldTicketNum : String?
    @objc var AribaSesNumber : String?
    @objc var serviceType : String?
    @objc var Department : String?
    @objc var Location : String?
    @objc var Status : String?
    
    @objc var field : String?
    @objc var fieldCode : String?
    @objc var facility : String?
    @objc var facilityCode : String?
    @objc var wellPad : String?
    @objc var wellPadCode : String?
    @objc var well : String?
    @objc var wellCode : String?
    
    @objc var CreatedByName : String?
    @objc var ServiceProviderId : String?
    @objc var vendorID : String?
    @objc var vendorName : String?
    @objc var vendorAdminID : String?
    @objc var vendorAddress : String?
    @objc var CreatedOn : String?
    @objc var CreationTime : String?
    @objc var UIcreatedOn : String?
    @objc var UIcreationTime : String?
    @objc var startDate : String?
    @objc var endDate : String?
    @objc var startTime : String?
    @objc var endTime : String?
    @objc var createdBy : String?
    @objc var sesNumber : String?
    
    @objc var costObject : String?
    
    @objc var poNumber : String?
    @objc var poText : String?
    
    @objc var workOrder : String?
    @objc var workText : String?
    
    @objc var costCenter : String?
    @objc var costCenterText : String?
    
    @objc var accounting_categ : String?
    @objc var sesApprover : String?
    
    @objc var wbsElement : String?
    @objc var wbsText : String?
    
    @objc var comment : String?
    @objc var commentBy : String?
    @objc var commentByName : String?
    @objc var murphyEngineerId : String?
    @objc var murphyEngineerEmail : String?
    @objc var murphyEngineerName : String?
    @objc var deviceType : String?
    @objc var deviceId : String?
    @objc var ServiceProviderMail : String?
    @objc var createCalendarDate : Date?
    @objc var createTimestampTime : Date?
    @objc var timeFlag : String?
    @objc var tempId : String?
    @objc var JobPerformedBy : String?
    @objc var quantity : String = ""
    @objc var unit : String = ""
    
    @objc var start_timestamp : String = ""
    @objc var end_timestamp : String = ""
    
    

    
    override init(){
    }
    
    convenience init?(dictionaryData : NSDictionary?) {
        guard let data = dictionaryData else{
            return nil
        }
        self.init(JSON: data)
    }
    
    
    init(JSON: AnyObject) {
        FieldTicketNum = JSON.value(forKey: "FieldTicketNum") as! String?
        Department = JSON.value(forKey: "Department") as! String?
        Location = JSON.value(forKey: "Location") as! String?
        if let create_time = JSON.value(forKey: "CreationTime"){
            CreationTime = create_time as? String
        }
        if let create_date = JSON.value(forKey: "CreatedOn"){
            CreatedOn = create_date as? String
        }
        serviceType = JSON.value(forKey: "Service_Type") as? String
        if serviceType != "SALTWATER"{
            serviceType = "General DFT"
        }
        else{
            serviceType = "Salt Water Disposal"
        }
        
        Status = JSON.value(forKey: "Status") as! String?
        AribaSesNumber = JSON.value(forKey: "AribaSESNo") as! String?
        field = JSON.value(forKey: "Field") as! String?
        fieldCode = JSON.value(forKey: "FieldCode") as! String?
        wellPad = JSON.value(forKey: "WellPad") as! String?
        wellPadCode = JSON.value(forKey: "WellpadCode") as! String?
        well = JSON.value(forKey: "Well") as! String?
        wellCode = JSON.value(forKey: "WellCode") as! String?
        facility = JSON.value(forKey: "Facility") as! String?
        facilityCode = JSON.value(forKey: "FacilityCode") as! String?
        CreatedByName = JSON.value(forKey: "CreatedByName") as! String?
        ServiceProviderId = JSON.value(forKey: "ServiceProviderId") as! String?
        vendorID = JSON.value(forKey: "VendorId") as! String?
        vendorName = JSON.value(forKey: "VendorName") as! String?
        vendorAddress = JSON.value(forKey: "VendorAddress") as! String?
        vendorAdminID = JSON.value(forKey: "VendorAdminId") as! String?
        if let create_date = JSON.value(forKey: "UIcreatedOn"){
            UIcreatedOn = create_date as? String
        }
        //UIcreatedOn = JSON.value(forKey: "UIcreatedOn") as! String?
        if let create_time = JSON.value(forKey: "UIcreationTime"){
            UIcreationTime = create_time as? String
        }
        //UIcreationTime = JSON.value(forKey: "UIcreationTime") as! String?
        if let start_date = JSON.value(forKey: "StartDate"){
             startDate = start_date as? String
        }
        if let end_date = JSON.value(forKey: "EndDate"){
            endDate = end_date as? String
        }
        startTime = JSON.value(forKey: "StartTime") as! String?
        endTime = JSON.value(forKey: "EndTime") as! String?
        createdBy = JSON.value(forKey: "CreatedBy") as! String?
        sesNumber = JSON.value(forKey: "SESNumber") as! String?
        costObject = JSON.value(forKey: "CostObject") as! String?
        poNumber = JSON.value(forKey: "MurphyPoNumber") as! String?
        poText = JSON.value(forKey: "PO_Text") as! String?
        workOrder = JSON.value(forKey: "WorkOrderNo") as! String?
        workText = JSON.value(forKey: "WO_Text") as! String?
        costCenter = JSON.value(forKey: "CostCenter") as! String?
        costCenterText = JSON.value(forKey: "CC_Text") as! String?
        accounting_categ = JSON.value(forKey: "AccountingCategory") as! String?
        sesApprover = JSON.value(forKey: "MurphySesApprover") as! String?
        wbsElement = JSON.value(forKey: "WbsElement") as! String?
        wbsText = JSON.value(forKey: "WBS_Text") as! String?
        ServiceProviderMail = JSON.value(forKey: "ServiceProviderMail") as! String?
        murphyEngineerId = JSON.value(forKey: "MurphyFieldReviewerId") as! String?
        murphyEngineerName = JSON.value(forKey: "MurphyFieldReviewerName") as! String?
        murphyEngineerEmail = JSON.value(forKey: "MurphyFieldReviewer") as! String?
        deviceType = JSON.value(forKey: "DeviceType") as! String?
        deviceId = JSON.value(forKey: "DeviceId") as! String?
        timeFlag = JSON.value(forKey: "TimeFlag") as! String?
        tempId = JSON.value(forKey: "tempId") as! String?
        JobPerformedBy = JSON.value(forKey: "JobPerformedBy") as! String?
        
    }
    
    required init(coder decoder: NSCoder) {
        self.serviceType = decoder.decodeObject(forKey: "serviceType") as? String ?? ""
        self.AribaSesNumber = decoder.decodeObject(forKey: "AribaSesNumber") as? String ?? ""
        self.FieldTicketNum = decoder.decodeObject(forKey: "FieldTicketNum") as? String ?? ""
        self.Department = decoder.decodeObject(forKey: "Department") as? String ?? ""
        self.Location = decoder.decodeObject(forKey: "Location") as? String ?? ""
        self.Status = decoder.decodeObject(forKey: "Status") as? String ?? ""
        self.well = decoder.decodeObject(forKey: "well") as? String ?? ""
        self.wellCode = decoder.decodeObject(forKey: "wellCode") as? String ?? ""
        self.facility = decoder.decodeObject(forKey: "facility") as? String ?? ""
        self.facilityCode = decoder.decodeObject(forKey: "facilityCode") as? String ?? ""
        self.CreatedByName = decoder.decodeObject(forKey: "CreatedByName") as? String ?? ""
        self.ServiceProviderId = decoder.decodeObject(forKey: "ServiceProviderId") as? String ?? ""
        self.vendorID = decoder.decodeObject(forKey: "vendorID") as? String ?? ""
        self.vendorName = decoder.decodeObject(forKey: "vendorName") as? String ?? ""
        self.vendorAdminID = decoder.decodeObject(forKey: "vendorAdminID") as? String ?? ""
        self.CreatedOn = decoder.decodeObject(forKey: "CreatedOn") as? String ?? ""
        self.CreationTime = decoder.decodeObject(forKey: "CreationTime") as? String ?? ""
        self.UIcreatedOn = decoder.decodeObject(forKey: "UIcreatedOn") as? String ?? ""
        self.UIcreationTime = decoder.decodeObject(forKey: "UIcreationTime") as? String ?? ""
        self.startDate = decoder.decodeObject(forKey: "startDate") as? String ?? ""
        self.endDate = decoder.decodeObject(forKey: "endDate") as? String ?? ""
        self.startTime = decoder.decodeObject(forKey: "startTime") as? String ?? ""
        self.endTime = decoder.decodeObject(forKey: "endTime") as? String ?? ""
        self.createdBy = decoder.decodeObject(forKey: "createdBy") as? String ?? ""
        self.sesNumber = decoder.decodeObject(forKey: "sesNumber") as? String ?? ""
        self.comment = decoder.decodeObject(forKey: "comment") as? String ?? ""
        self.commentBy = decoder.decodeObject(forKey: "commentBy") as? String ?? ""
        self.commentByName = decoder.decodeObject(forKey: "commentByName") as? String ?? ""
        self.murphyEngineerId = decoder.decodeObject(forKey: "murphyEngineerId") as? String ?? ""
        self.murphyEngineerEmail = decoder.decodeObject(forKey: "murphyEngineerEmail") as? String ?? ""
        self.murphyEngineerName = decoder.decodeObject(forKey: "murphyEngineerName") as? String ?? ""
        self.ServiceProviderMail = decoder.decodeObject(forKey: "ServiceProviderMail") as? String ?? ""
        self.createCalendarDate = decoder.decodeObject(forKey: "createCalendarDate") as? Date ?? Date()
        self.createTimestampTime = decoder.decodeObject(forKey: "createTimestampTime") as? Date ?? Date()
        self.tempId = decoder.decodeObject(forKey: "tempId") as? String ?? ""
        self.poNumber = decoder.decodeObject(forKey: "poNumber") as? String ?? ""
        self.workOrder = decoder.decodeObject(forKey: "workOrder") as? String ?? ""
        self.costCenter = decoder.decodeObject(forKey: "costCenter") as? String ?? ""
        self.accounting_categ = decoder.decodeObject(forKey: "accounting_categ") as? String ?? ""
        self.sesApprover = decoder.decodeObject(forKey: "sesApprover") as? String ?? ""
        self.wbsElement = decoder.decodeObject(forKey: "wbsElement") as? String ?? ""
        self.timeFlag = decoder.decodeObject(forKey: "timeFlag") as? String ?? ""
        self.deviceType = decoder.decodeObject(forKey: "deviceType") as? String ?? ""
        self.deviceId = decoder.decodeObject(forKey: "deviceId") as? String ?? ""
        self.quantity = decoder.decodeObject(forKey: "quantity") as? String ?? ""
        self.unit = decoder.decodeObject(forKey: "unit") as? String ?? ""
        self.start_timestamp = decoder.decodeObject(forKey: "start_timestamp") as? String ?? ""
        self.end_timestamp = decoder.decodeObject(forKey: "end_timestamp") as? String ?? ""
        
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(serviceType, forKey: "serviceType")
        coder.encode(AribaSesNumber, forKey: "AribaSesNumber")
        coder.encode(FieldTicketNum, forKey: "FieldTicketNum")
        coder.encode(Department, forKey: "Department")
        coder.encode(Location, forKey: "Location")
        coder.encode(Status, forKey: "Status")
        coder.encode(well, forKey: "well")
        coder.encode(facility, forKey: "facility")
        coder.encode(facilityCode, forKey: "facilityCode")
        coder.encode(CreatedByName, forKey: "CreatedByName")
        coder.encode(ServiceProviderId, forKey: "ServiceProviderId")
        coder.encode(vendorID, forKey: "vendorID")
        coder.encode(vendorName, forKey: "vendorName")
        coder.encode(vendorAdminID, forKey: "vendorAdminID")
        coder.encode(CreatedOn, forKey: "CreatedOn")
        coder.encode(CreationTime, forKey: "CreationTime")
        coder.encode(UIcreatedOn, forKey: "UIcreatedOn")
        coder.encode(UIcreationTime, forKey: "UIcreationTime")
        coder.encode(startDate, forKey: "startDate")
        coder.encode(endDate, forKey: "endDate")
        coder.encode(startTime, forKey: "startTime")
        coder.encode(endTime, forKey: "endTime")
        coder.encode(createdBy, forKey: "createdBy")
        coder.encode(sesNumber, forKey: "sesNumber")
        coder.encode(comment, forKey: "comment")
        coder.encode(commentBy, forKey: "commentBy")
        coder.encode(commentByName, forKey: "commentByName")
        coder.encode(murphyEngineerId, forKey: "murphyEngineerId")
        coder.encode(murphyEngineerEmail, forKey: "murphyEngineerEmail")
        coder.encode(murphyEngineerName, forKey: "murphyEngineerName")
        coder.encode(ServiceProviderMail, forKey: "ServiceProviderMail")
        coder.encode(createCalendarDate, forKey: "createCalendarDate")
        coder.encode(createTimestampTime, forKey: "createTimestampTime")
        coder.encode(tempId, forKey: "tempId")
        coder.encode(poNumber, forKey: "poNumber")
        coder.encode(workOrder, forKey: "workOrder")
        coder.encode(costCenter, forKey: "costCenter")
        coder.encode(accounting_categ, forKey: "accounting_categ")
        coder.encode(sesApprover, forKey: "sesApprover")
        coder.encode(wbsElement, forKey: "wbsElement")
        coder.encode(timeFlag, forKey: "timeFlag")
        coder.encode(deviceType, forKey: "deviceType")
        coder.encode(deviceId, forKey: "deviceId")
        coder.encode(quantity, forKey: "quantity")
        coder.encode(unit, forKey: "unit")
        coder.encode(start_timestamp, forKey: "unit")
        coder.encode(end_timestamp, forKey: "unit")
        
    }
}


