// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class ZMURPURCHASEGROUPSRVEntities<Provider: DataServiceProvider>: DataService<Provider> {
    public override init(provider: Provider) {
        super.init(provider: provider)
        self.provider.metadata = ZMURPURCHASEGROUPSRVEntitiesMetadata.document
    }

    @available(swift, deprecated: 4.0, renamed: "fetchChangeSet")
    open func changeSet(query: DataQuery = DataQuery()) throws -> Array<Change> {
        return try self.fetchChangeSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchChangeSet")
    open func changeSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Change>?, Error?) -> Void) {
        self.fetchChangeSet(matching: query, completionHandler: completionHandler)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchCreateSet")
    open func createSet(query: DataQuery = DataQuery()) throws -> Array<Create> {
        return try self.fetchCreateSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchCreateSet")
    open func createSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Create>?, Error?) -> Void) {
        self.fetchCreateSet(matching: query, completionHandler: completionHandler)
    }

    open func fetchChange(matching query: DataQuery) throws -> Change {
        return try CastRequired<Change>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.changeSet)).requiredEntity())
    }

    open func fetchChange(matching query: DataQuery, completionHandler: @escaping (Change?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Change = try self.fetchChange(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchChangeSet(matching query: DataQuery = DataQuery()) throws -> Array<Change> {
        return try Change.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.changeSet)).entityList())
    }

    open func fetchChangeSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Change>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<Change> = try self.fetchChangeSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchCreate(matching query: DataQuery) throws -> Create {
        return try CastRequired<Create>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.createSet)).requiredEntity())
    }

    open func fetchCreate(matching query: DataQuery, completionHandler: @escaping (Create?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Create = try self.fetchCreate(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchCreateSet(matching query: DataQuery = DataQuery()) throws -> Array<Create> {
        return try Create.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.createSet)).entityList())
    }

    open func fetchCreateSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Create>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<Create> = try self.fetchCreateSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetNxtPurGrpByCtry(matching query: DataQuery) throws -> GetNxtPurGrpByCtry {
        return try CastRequired<GetNxtPurGrpByCtry>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getNxtPurGrpByCtrySet)).requiredEntity())
    }

    open func fetchGetNxtPurGrpByCtry(matching query: DataQuery, completionHandler: @escaping (GetNxtPurGrpByCtry?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: GetNxtPurGrpByCtry = try self.fetchGetNxtPurGrpByCtry(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetNxtPurGrpByCtrySet(matching query: DataQuery = DataQuery()) throws -> Array<GetNxtPurGrpByCtry> {
        return try GetNxtPurGrpByCtry.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getNxtPurGrpByCtrySet)).entityList())
    }

    open func fetchGetNxtPurGrpByCtrySet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetNxtPurGrpByCtry>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<GetNxtPurGrpByCtry> = try self.fetchGetNxtPurGrpByCtrySet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetPurGrp(matching query: DataQuery) throws -> GetPurGrp {
        return try CastRequired<GetPurGrp>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpSet)).requiredEntity())
    }

    open func fetchGetPurGrp(matching query: DataQuery, completionHandler: @escaping (GetPurGrp?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: GetPurGrp = try self.fetchGetPurGrp(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetPurGrpByUserName(matching query: DataQuery) throws -> GetPurGrpByUserName {
        return try CastRequired<GetPurGrpByUserName>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpByUserNameSet)).requiredEntity())
    }

    open func fetchGetPurGrpByUserName(matching query: DataQuery, completionHandler: @escaping (GetPurGrpByUserName?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: GetPurGrpByUserName = try self.fetchGetPurGrpByUserName(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetPurGrpByUserNameSet(matching query: DataQuery = DataQuery()) throws -> Array<GetPurGrpByUserName> {
        return try GetPurGrpByUserName.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpByUserNameSet)).entityList())
    }

    open func fetchGetPurGrpByUserNameSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetPurGrpByUserName>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<GetPurGrpByUserName> = try self.fetchGetPurGrpByUserNameSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetPurGrpRequestDetails(matching query: DataQuery) throws -> GetPurGrpRequestDetails {
        return try CastRequired<GetPurGrpRequestDetails>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpRequestDetailsSet)).requiredEntity())
    }

    open func fetchGetPurGrpRequestDetails(matching query: DataQuery, completionHandler: @escaping (GetPurGrpRequestDetails?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: GetPurGrpRequestDetails = try self.fetchGetPurGrpRequestDetails(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetPurGrpRequestDetailsSet(matching query: DataQuery = DataQuery()) throws -> Array<GetPurGrpRequestDetails> {
        return try GetPurGrpRequestDetails.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpRequestDetailsSet)).entityList())
    }

    open func fetchGetPurGrpRequestDetailsSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetPurGrpRequestDetails>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<GetPurGrpRequestDetails> = try self.fetchGetPurGrpRequestDetailsSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetPurGrpSet(matching query: DataQuery = DataQuery()) throws -> Array<GetPurGrp> {
        return try GetPurGrp.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getPurGrpSet)).entityList())
    }

    open func fetchGetPurGrpSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetPurGrp>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<GetPurGrp> = try self.fetchGetPurGrpSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetResponsiblePerson(matching query: DataQuery) throws -> GetResponsiblePerson {
        return try CastRequired<GetResponsiblePerson>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getResponsiblePersonSet)).requiredEntity())
    }

    open func fetchGetResponsiblePerson(matching query: DataQuery, completionHandler: @escaping (GetResponsiblePerson?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: GetResponsiblePerson = try self.fetchGetResponsiblePerson(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchGetResponsiblePersonSet(matching query: DataQuery = DataQuery()) throws -> Array<GetResponsiblePerson> {
        return try GetResponsiblePerson.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.getResponsiblePersonSet)).entityList())
    }

    open func fetchGetResponsiblePersonSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetResponsiblePerson>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<GetResponsiblePerson> = try self.fetchGetResponsiblePersonSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchReject(matching query: DataQuery) throws -> Reject {
        return try CastRequired<Reject>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.rejectSet)).requiredEntity())
    }

    open func fetchReject(matching query: DataQuery, completionHandler: @escaping (Reject?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Reject = try self.fetchReject(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchRejectSet(matching query: DataQuery = DataQuery()) throws -> Array<Reject> {
        return try Reject.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.rejectSet)).entityList())
    }

    open func fetchRejectSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Reject>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<Reject> = try self.fetchRejectSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchRequest(matching query: DataQuery) throws -> Request {
        return try CastRequired<Request>.from(self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.requestSet)).requiredEntity())
    }

    open func fetchRequest(matching query: DataQuery, completionHandler: @escaping (Request?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Request = try self.fetchRequest(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    open func fetchRequestSet(matching query: DataQuery = DataQuery()) throws -> Array<Request> {
        return try Request.array(from: self.executeQuery(query.fromDefault(ZMURPURCHASEGROUPSRVEntitiesMetadata.EntitySets.requestSet)).entityList())
    }

    open func fetchRequestSet(matching query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Request>?, Error?) -> Void) {
        self.addBackgroundOperation {
            do {
                let result: Array<Request> = try self.fetchRequestSet(matching: query)
                self.completionQueue.addOperation {
                    completionHandler(result, nil)
                }
            } catch let error {
                self.completionQueue.addOperation {
                    completionHandler(nil, error)
                }
            }
        }
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetNxtPurGrpByCtrySet")
    open func getNxtPurGrpByCtrySet(query: DataQuery = DataQuery()) throws -> Array<GetNxtPurGrpByCtry> {
        return try self.fetchGetNxtPurGrpByCtrySet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetNxtPurGrpByCtrySet")
    open func getNxtPurGrpByCtrySet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetNxtPurGrpByCtry>?, Error?) -> Void) {
        self.fetchGetNxtPurGrpByCtrySet(matching: query, completionHandler: completionHandler)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetPurGrpByUserNameSet")
    open func getPurGrpByUserNameSet(query: DataQuery = DataQuery()) throws -> Array<GetPurGrpByUserName> {
        return try self.fetchGetPurGrpByUserNameSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetPurGrpByUserNameSet")
    open func getPurGrpByUserNameSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetPurGrpByUserName>?, Error?) -> Void) {
        self.fetchGetPurGrpByUserNameSet(matching: query, completionHandler: completionHandler)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetPurGrpRequestDetailsSet")
    open func getPurGrpRequestDetailsSet(query: DataQuery = DataQuery()) throws -> Array<GetPurGrpRequestDetails> {
        return try self.fetchGetPurGrpRequestDetailsSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetPurGrpRequestDetailsSet")
    open func getPurGrpRequestDetailsSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetPurGrpRequestDetails>?, Error?) -> Void) {
        self.fetchGetPurGrpRequestDetailsSet(matching: query, completionHandler: completionHandler)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetPurGrpSet")
    open func getPurGrpSet(query: DataQuery = DataQuery()) throws -> Array<GetPurGrp> {
        return try self.fetchGetPurGrpSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetPurGrpSet")
    open func getPurGrpSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetPurGrp>?, Error?) -> Void) {
        self.fetchGetPurGrpSet(matching: query, completionHandler: completionHandler)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetResponsiblePersonSet")
    open func getResponsiblePersonSet(query: DataQuery = DataQuery()) throws -> Array<GetResponsiblePerson> {
        return try self.fetchGetResponsiblePersonSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchGetResponsiblePersonSet")
    open func getResponsiblePersonSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<GetResponsiblePerson>?, Error?) -> Void) {
        self.fetchGetResponsiblePersonSet(matching: query, completionHandler: completionHandler)
    }

    open override func refreshMetadata() throws {
        objc_sync_enter(self)
        defer { objc_sync_exit(self) }
        do {
            try ProxyInternal.refreshMetadata(service: self, fetcher: nil, options: ZMURPURCHASEGROUPSRVEntitiesMetadataParser.options)
            ZMURPURCHASEGROUPSRVEntitiesMetadataChanges.merge(metadata: self.metadata)
        }
    }

    @available(swift, deprecated: 4.0, renamed: "fetchRejectSet")
    open func rejectSet(query: DataQuery = DataQuery()) throws -> Array<Reject> {
        return try self.fetchRejectSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchRejectSet")
    open func rejectSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Reject>?, Error?) -> Void) {
        self.fetchRejectSet(matching: query, completionHandler: completionHandler)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchRequestSet")
    open func requestSet(query: DataQuery = DataQuery()) throws -> Array<Request> {
        return try self.fetchRequestSet(matching: query)
    }

    @available(swift, deprecated: 4.0, renamed: "fetchRequestSet")
    open func requestSet(query: DataQuery = DataQuery(), completionHandler: @escaping (Array<Request>?, Error?) -> Void) {
        self.fetchRequestSet(matching: query, completionHandler: completionHandler)
    }
}
