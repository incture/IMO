// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

internal class ZMURPURCHASEGROUPSRVEntitiesMetadataChanges: ObjectBase {
    override init() {
    }

    class func merge(metadata: CSDLDocument) {
        metadata.hasGeneratedProxies = true
        ZMURPURCHASEGROUPSRVEntitiesMetadata.document = metadata
        ZMURPURCHASEGROUPSRVEntitiesMetadataChanges.merge1(metadata: metadata)
        try! ZMURPURCHASEGROUPSRVEntitiesFactory.registerAll()
    }

    private class func merge1(metadata: CSDLDocument) {
        Ignore.valueOf_any(metadata)
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Change")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Create")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetNxtPurGrpByCtry")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetPurGrp")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetPurGrpByUserName")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetPurGrpRequestDetails")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetResponsiblePerson")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Reject")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request = metadata.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Request")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.changeSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.changeSet = metadata.entitySet(withName: "ChangeSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.createSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.createSet = metadata.entitySet(withName: "CreateSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getNxtPurGrpByCtrySet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getNxtPurGrpByCtrySet = metadata.entitySet(withName: "GetNxtPurGrpByCtrySet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpByUserNameSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpByUserNameSet = metadata.entitySet(withName: "GetPurGrpByUserNameSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpRequestDetailsSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpRequestDetailsSet = metadata.entitySet(withName: "GetPurGrpRequestDetailsSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpSet = metadata.entitySet(withName: "GetPurGrpSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getResponsiblePersonSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getResponsiblePersonSet = metadata.entitySet(withName: "GetResponsiblePersonSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.rejectSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.rejectSet = metadata.entitySet(withName: "RejectSet")
        }
        if !ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.requestSet.isRemoved {
            ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.requestSet = metadata.entitySet(withName: "RequestSet")
        }
        if !Change.reqID.isRemoved {
            Change.reqID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "ReqId")
        }
        if !Change.rejectionText.isRemoved {
            Change.rejectionText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "RejectionText")
        }
        if !Change.status.isRemoved {
            Change.status = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "Status")
        }
        if !Change.purchaseGroup.isRemoved {
            Change.purchaseGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "PurchaseGroup")
        }
        if !Change.country.isRemoved {
            Change.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "Country")
        }
        if !Change.userName.isRemoved {
            Change.userName = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "UserName")
        }
        if !Change.messageType.isRemoved {
            Change.messageType = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "MessageType")
        }
        if !Change.message.isRemoved {
            Change.message = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "Message")
        }
        if !Create.reqID.isRemoved {
            Create.reqID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "ReqId")
        }
        if !Create.status.isRemoved {
            Create.status = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "Status")
        }
        if !Create.userName.isRemoved {
            Create.userName = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "UserName")
        }
        if !Create.rejectionText.isRemoved {
            Create.rejectionText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "RejectionText")
        }
        if !Create.purchaseGroup.isRemoved {
            Create.purchaseGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "PurchaseGroup")
        }
        if !Create.country.isRemoved {
            Create.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "Country")
        }
        if !Create.messageType.isRemoved {
            Create.messageType = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "MessageType")
        }
        if !Create.message.isRemoved {
            Create.message = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "Message")
        }
        if !GetNxtPurGrpByCtry.country.isRemoved {
            GetNxtPurGrpByCtry.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry.property(withName: "Country")
        }
        if !GetNxtPurGrpByCtry.purchGroup.isRemoved {
            GetNxtPurGrpByCtry.purchGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry.property(withName: "PurchGroup")
        }
        if !GetPurGrp.client.isRemoved {
            GetPurGrp.client = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Client")
        }
        if !GetPurGrp.purchGroup.isRemoved {
            GetPurGrp.purchGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "PurchGroup")
        }
        if !GetPurGrp.description.isRemoved {
            GetPurGrp.description = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Description")
        }
        if !GetPurGrp.telNoPurchGrp.isRemoved {
            GetPurGrp.telNoPurchGrp = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "TelNoPurchGrp")
        }
        if !GetPurGrp.outputDevice.isRemoved {
            GetPurGrp.outputDevice = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "OutputDevice")
        }
        if !GetPurGrp.faxNumber.isRemoved {
            GetPurGrp.faxNumber = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "FaxNumber")
        }
        if !GetPurGrp.telephone.isRemoved {
            GetPurGrp.telephone = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Telephone")
        }
        if !GetPurGrp.extension.isRemoved {
            GetPurGrp.extension = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Extension")
        }
        if !GetPurGrp.eMailAddress.isRemoved {
            GetPurGrp.eMailAddress = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "EMailAddress")
        }
        if !GetPurGrp.country.isRemoved {
            GetPurGrp.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Country")
        }
        if !GetPurGrpByUserName.purchGroup.isRemoved {
            GetPurGrpByUserName.purchGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "PurchGroup")
        }
        if !GetPurGrpByUserName.description.isRemoved {
            GetPurGrpByUserName.description = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Description")
        }
        if !GetPurGrpByUserName.xubname.isRemoved {
            GetPurGrpByUserName.xubname = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Xubname")
        }
        if !GetPurGrpByUserName.reqID.isRemoved {
            GetPurGrpByUserName.reqID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "ReqId")
        }
        if !GetPurGrpByUserName.country.isRemoved {
            GetPurGrpByUserName.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Country")
        }
        if !GetPurGrpByUserName.activity.isRemoved {
            GetPurGrpByUserName.activity = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Activity")
        }
        if !GetPurGrpByUserName.status.isRemoved {
            GetPurGrpByUserName.status = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Status")
        }
        if !GetPurGrpRequestDetails.reqID.isRemoved {
            GetPurGrpRequestDetails.reqID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "ReqId")
        }
        if !GetPurGrpRequestDetails.purchGroup.isRemoved {
            GetPurGrpRequestDetails.purchGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "PurchGroup")
        }
        if !GetPurGrpRequestDetails.description.isRemoved {
            GetPurGrpRequestDetails.description = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Description")
        }
        if !GetPurGrpRequestDetails.country.isRemoved {
            GetPurGrpRequestDetails.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Country")
        }
        if !GetPurGrpRequestDetails.telnopurchgp.isRemoved {
            GetPurGrpRequestDetails.telnopurchgp = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Telnopurchgp")
        }
        if !GetPurGrpRequestDetails.numberRangeInd.isRemoved {
            GetPurGrpRequestDetails.numberRangeInd = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "NumberRangeInd")
        }
        if !GetPurGrpRequestDetails.outputDevice.isRemoved {
            GetPurGrpRequestDetails.outputDevice = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "OutputDevice")
        }
        if !GetPurGrpRequestDetails.faxNumber.isRemoved {
            GetPurGrpRequestDetails.faxNumber = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "FaxNumber")
        }
        if !GetPurGrpRequestDetails.telephone.isRemoved {
            GetPurGrpRequestDetails.telephone = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Telephone")
        }
        if !GetPurGrpRequestDetails.extension.isRemoved {
            GetPurGrpRequestDetails.extension = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Extension")
        }
        if !GetPurGrpRequestDetails.eMailAddress.isRemoved {
            GetPurGrpRequestDetails.eMailAddress = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "EMailAddress")
        }
        if !GetPurGrpRequestDetails.xubname.isRemoved {
            GetPurGrpRequestDetails.xubname = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Xubname")
        }
        if !GetPurGrpRequestDetails.activity.isRemoved {
            GetPurGrpRequestDetails.activity = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Activity")
        }
        if !GetPurGrpRequestDetails.status.isRemoved {
            GetPurGrpRequestDetails.status = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Status")
        }
        if !GetPurGrpRequestDetails.trNo.isRemoved {
            GetPurGrpRequestDetails.trNo = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "TRNo")
        }
        if !GetPurGrpRequestDetails.createdOn.isRemoved {
            GetPurGrpRequestDetails.createdOn = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "CreatedOn")
        }
        if !GetPurGrpRequestDetails.changedOn.isRemoved {
            GetPurGrpRequestDetails.changedOn = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "ChangedOn")
        }
        if !GetPurGrpRequestDetails.createdBy.isRemoved {
            GetPurGrpRequestDetails.createdBy = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "CreatedBy")
        }
        if !GetPurGrpRequestDetails.changeBy.isRemoved {
            GetPurGrpRequestDetails.changeBy = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "ChangeBy")
        }
        if !GetPurGrpRequestDetails.statusText.isRemoved {
            GetPurGrpRequestDetails.statusText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "StatusText")
        }
        if !GetPurGrpRequestDetails.rejectionText.isRemoved {
            GetPurGrpRequestDetails.rejectionText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "RejectionText")
        }
        if !GetPurGrpRequestDetails.messageType.isRemoved {
            GetPurGrpRequestDetails.messageType = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "MessageType")
        }
        if !GetPurGrpRequestDetails.message.isRemoved {
            GetPurGrpRequestDetails.message = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Message")
        }
        if !GetPurGrpRequestDetails.systemID.isRemoved {
            GetPurGrpRequestDetails.systemID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "SystemId")
        }
        if !GetPurGrpRequestDetails.personrespnsble.isRemoved {
            GetPurGrpRequestDetails.personrespnsble = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Personrespnsble")
        }
        if !GetResponsiblePerson.userID.isRemoved {
            GetResponsiblePerson.userID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.property(withName: "UserId")
        }
        if !GetResponsiblePerson.fname.isRemoved {
            GetResponsiblePerson.fname = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.property(withName: "Fname")
        }
        if !GetResponsiblePerson.lname.isRemoved {
            GetResponsiblePerson.lname = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.property(withName: "Lname")
        }
        if !Reject.reqID.isRemoved {
            Reject.reqID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "ReqId")
        }
        if !Reject.ekgrp.isRemoved {
            Reject.ekgrp = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Ekgrp")
        }
        if !Reject.eknam.isRemoved {
            Reject.eknam = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Eknam")
        }
        if !Reject.land1.isRemoved {
            Reject.land1 = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Land1")
        }
        if !Reject.ektel.isRemoved {
            Reject.ektel = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Ektel")
        }
        if !Reject.nrind.isRemoved {
            Reject.nrind = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Nrind")
        }
        if !Reject.ldest.isRemoved {
            Reject.ldest = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Ldest")
        }
        if !Reject.telfx.isRemoved {
            Reject.telfx = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Telfx")
        }
        if !Reject.telNumber.isRemoved {
            Reject.telNumber = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "TelNumber")
        }
        if !Reject.telExtens.isRemoved {
            Reject.telExtens = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "TelExtens")
        }
        if !Reject.smtpAddr.isRemoved {
            Reject.smtpAddr = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "SmtpAddr")
        }
        if !Reject.xubname.isRemoved {
            Reject.xubname = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Xubname")
        }
        if !Reject.activity.isRemoved {
            Reject.activity = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Activity")
        }
        if !Reject.status.isRemoved {
            Reject.status = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Status")
        }
        if !Reject.trkorr.isRemoved {
            Reject.trkorr = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Trkorr")
        }
        if !Reject.createdOn.isRemoved {
            Reject.createdOn = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "CreatedOn")
        }
        if !Reject.changedOn.isRemoved {
            Reject.changedOn = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "ChangedOn")
        }
        if !Reject.createdBy.isRemoved {
            Reject.createdBy = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "CreatedBy")
        }
        if !Reject.changeBy.isRemoved {
            Reject.changeBy = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "ChangeBy")
        }
        if !Reject.statusText.isRemoved {
            Reject.statusText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "StatusText")
        }
        if !Reject.rejectionText.isRemoved {
            Reject.rejectionText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "RejectionText")
        }
        if !Reject.messageType.isRemoved {
            Reject.messageType = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "MessageType")
        }
        if !Reject.message.isRemoved {
            Reject.message = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Message")
        }
        if !Reject.systemID.isRemoved {
            Reject.systemID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "SystemId")
        }
        if !Reject.sbnamag.isRemoved {
            Reject.sbnamag = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Sbnamag")
        }
        if !Request.country.isRemoved {
            Request.country = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Country")
        }
        if !Request.systemID.isRemoved {
            Request.systemID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "SystemId")
        }
        if !Request.persnRespnsble.isRemoved {
            Request.persnRespnsble = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "PersnRespnsble")
        }
        if !Request.statusText.isRemoved {
            Request.statusText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "StatusText")
        }
        if !Request.internal.isRemoved {
            Request.internal = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Internal")
        }
        if !Request.messageType.isRemoved {
            Request.messageType = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "MessageType")
        }
        if !Request.reqID.isRemoved {
            Request.reqID = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "ReqId")
        }
        if !Request.userName.isRemoved {
            Request.userName = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "UserName")
        }
        if !Request.purchaseGroup.isRemoved {
            Request.purchaseGroup = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "PurchaseGroup")
        }
        if !Request.message.isRemoved {
            Request.message = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Message")
        }
        if !Request.description.isRemoved {
            Request.description = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Description")
        }
        if !Request.purGrpTelNr.isRemoved {
            Request.purGrpTelNr = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "PurGrpTelNr")
        }
        if !Request.outputDevice.isRemoved {
            Request.outputDevice = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "OutputDevice")
        }
        if !Request.faxNumber.isRemoved {
            Request.faxNumber = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "FaxNumber")
        }
        if !Request.telNumber.isRemoved {
            Request.telNumber = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "TelNumber")
        }
        if !Request.telExtens.isRemoved {
            Request.telExtens = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "TelExtens")
        }
        if !Request.emailAddress.isRemoved {
            Request.emailAddress = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "EmailAddress")
        }
        if !Request.activity.isRemoved {
            Request.activity = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Activity")
        }
        if !Request.status.isRemoved {
            Request.status = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Status")
        }
        if !Request.transportRequest.isRemoved {
            Request.transportRequest = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "TransportRequest")
        }
        if !Request.createdOn.isRemoved {
            Request.createdOn = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "CreatedOn")
        }
        if !Request.changedOn.isRemoved {
            Request.changedOn = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "ChangedOn")
        }
        if !Request.createdBy.isRemoved {
            Request.createdBy = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "CreatedBy")
        }
        if !Request.changeBy.isRemoved {
            Request.changeBy = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "ChangeBy")
        }
        if !Request.rejectionText.isRemoved {
            Request.rejectionText = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "RejectionText")
        }
    }
}
