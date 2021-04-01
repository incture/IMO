// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

public class ZMURPURCHASEGROUPSRVEntitiesMetadata {
    public static var document: CSDLDocument = ZMURPURCHASEGROUPSRVEntitiesMetadata.resolve()

    private static func resolve() -> CSDLDocument {
        try! ZMURPURCHASEGROUPSRVEntitiesFactory.registerAll()
        ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.hasGeneratedProxies = true
        return ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed
    }

    public class EntityTypes {
        public static var change: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Change")

        public static var create: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Create")

        public static var getNxtPurGrpByCtry: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetNxtPurGrpByCtry")

        public static var getPurGrp: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetPurGrp")

        public static var getPurGrpByUserName: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetPurGrpByUserName")

        public static var getPurGrpRequestDetails: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetPurGrpRequestDetails")

        public static var getResponsiblePerson: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.GetResponsiblePerson")

        public static var reject: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Reject")

        public static var request: EntityType = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entityType(withName: "ZMUR_PURCHASE_GROUP_SRV.Request")
    }

    public class EntitySets {
        public static var changeSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "ChangeSet")

        public static var createSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "CreateSet")

        public static var getNxtPurGrpByCtrySet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "GetNxtPurGrpByCtrySet")

        public static var getPurGrpByUserNameSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "GetPurGrpByUserNameSet")

        public static var getPurGrpRequestDetailsSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "GetPurGrpRequestDetailsSet")

        public static var getPurGrpSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "GetPurGrpSet")

        public static var getResponsiblePersonSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "GetResponsiblePersonSet")

        public static var rejectSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "RejectSet")

        public static var requestSet: EntitySet = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parsed.entitySet(withName: "RequestSet")
    }
}
