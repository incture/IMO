// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

internal class ZMURPURCHASEGROUPSRVEntitiesFactory {
    static func registerAll() throws {
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.registerFactory(ObjectFactory.with(create: { Change(withDefaults: false) }, createWithDecoder: { d in try Change(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.registerFactory(ObjectFactory.with(create: { Create(withDefaults: false) }, createWithDecoder: { d in try Create(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry.registerFactory(ObjectFactory.with(create: { GetNxtPurGrpByCtry(withDefaults: false) }, createWithDecoder: { d in try GetNxtPurGrpByCtry(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.registerFactory(ObjectFactory.with(create: { GetPurGrp(withDefaults: false) }, createWithDecoder: { d in try GetPurGrp(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.registerFactory(ObjectFactory.with(create: { GetPurGrpByUserName(withDefaults: false) }, createWithDecoder: { d in try GetPurGrpByUserName(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.registerFactory(ObjectFactory.with(create: { GetPurGrpRequestDetails(withDefaults: false) }, createWithDecoder: { d in try GetPurGrpRequestDetails(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.registerFactory(ObjectFactory.with(create: { GetResponsiblePerson(withDefaults: false) }, createWithDecoder: { d in try GetResponsiblePerson(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.registerFactory(ObjectFactory.with(create: { Reject(withDefaults: false) }, createWithDecoder: { d in try Reject(from: d) }))
        ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.registerFactory(ObjectFactory.with(create: { Request(withDefaults: false) }, createWithDecoder: { d in try Request(from: d) }))
    }
}
