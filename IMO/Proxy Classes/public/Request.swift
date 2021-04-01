// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class Request: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Country")

    public static var systemID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "SystemId")

    public static var persnRespnsble: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "PersnRespnsble")

    public static var statusText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "StatusText")

    public static var `internal`: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Internal")

    public static var messageType: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "MessageType")

    public static var reqID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "ReqId")

    public static var userName: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "UserName")

    public static var purchaseGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "PurchaseGroup")

    public static var message: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Message")

    public static var description: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Description")

    public static var purGrpTelNr: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "PurGrpTelNr")

    public static var outputDevice: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "OutputDevice")

    public static var faxNumber: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "FaxNumber")

    public static var telNumber: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "TelNumber")

    public static var telExtens: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "TelExtens")

    public static var emailAddress: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "EmailAddress")

    public static var activity: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Activity")

    public static var status: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "Status")

    public static var transportRequest: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "TransportRequest")

    public static var createdOn: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "CreatedOn")

    public static var changedOn: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "ChangedOn")

    public static var createdBy: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "CreatedBy")

    public static var changeBy: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "ChangeBy")

    public static var rejectionText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request.property(withName: "RejectionText")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.request)
    }

    open var `internal`: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.internal))
        }
        set(value) {
            self.setOptionalValue(for: Request.internal, to: StringValue.of(optional: value))
        }
    }

    open var activity: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.activity))
        }
        set(value) {
            self.setOptionalValue(for: Request.activity, to: StringValue.of(optional: value))
        }
    }

    open class func array(from: EntityValueList) -> Array<Request> {
        return ArrayConverter.convert(from.toArray(), Array<Request>())
    }

    open var changeBy: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.changeBy))
        }
        set(value) {
            self.setOptionalValue(for: Request.changeBy, to: StringValue.of(optional: value))
        }
    }

    open var changedOn: LocalDateTime? {
        get {
            return LocalDateTime.castOptional(self.optionalValue(for: Request.changedOn))
        }
        set(value) {
            self.setOptionalValue(for: Request.changedOn, to: value)
        }
    }

    open func copy() -> Request {
        return CastRequired<Request>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.country))
        }
        set(value) {
            self.setOptionalValue(for: Request.country, to: StringValue.of(optional: value))
        }
    }

    open var createdBy: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.createdBy))
        }
        set(value) {
            self.setOptionalValue(for: Request.createdBy, to: StringValue.of(optional: value))
        }
    }

    open var createdOn: LocalDateTime? {
        get {
            return LocalDateTime.castOptional(self.optionalValue(for: Request.createdOn))
        }
        set(value) {
            self.setOptionalValue(for: Request.createdOn, to: value)
        }
    }

    open var description: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.description))
        }
        set(value) {
            self.setOptionalValue(for: Request.description, to: StringValue.of(optional: value))
        }
    }

    open var emailAddress: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.emailAddress))
        }
        set(value) {
            self.setOptionalValue(for: Request.emailAddress, to: StringValue.of(optional: value))
        }
    }

    open var faxNumber: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.faxNumber))
        }
        set(value) {
            self.setOptionalValue(for: Request.faxNumber, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(reqID: String?, purchaseGroup: String?) -> EntityKey {
        return EntityKey().with(name: "ReqId", value: StringValue.of(optional: reqID)).with(name: "PurchaseGroup", value: StringValue.of(optional: purchaseGroup))
    }

    open var message: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.message))
        }
        set(value) {
            self.setOptionalValue(for: Request.message, to: StringValue.of(optional: value))
        }
    }

    open var messageType: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.messageType))
        }
        set(value) {
            self.setOptionalValue(for: Request.messageType, to: StringValue.of(optional: value))
        }
    }

    open var old: Request {
        return CastRequired<Request>.from(self.oldEntity)
    }

    open var outputDevice: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.outputDevice))
        }
        set(value) {
            self.setOptionalValue(for: Request.outputDevice, to: StringValue.of(optional: value))
        }
    }

    open var persnRespnsble: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.persnRespnsble))
        }
        set(value) {
            self.setOptionalValue(for: Request.persnRespnsble, to: StringValue.of(optional: value))
        }
    }

    open var purGrpTelNr: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.purGrpTelNr))
        }
        set(value) {
            self.setOptionalValue(for: Request.purGrpTelNr, to: StringValue.of(optional: value))
        }
    }

    open var purchaseGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.purchaseGroup))
        }
        set(value) {
            self.setOptionalValue(for: Request.purchaseGroup, to: StringValue.of(optional: value))
        }
    }

    open var rejectionText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.rejectionText))
        }
        set(value) {
            self.setOptionalValue(for: Request.rejectionText, to: StringValue.of(optional: value))
        }
    }

    open var reqID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.reqID))
        }
        set(value) {
            self.setOptionalValue(for: Request.reqID, to: StringValue.of(optional: value))
        }
    }

    open var status: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.status))
        }
        set(value) {
            self.setOptionalValue(for: Request.status, to: StringValue.of(optional: value))
        }
    }

    open var statusText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.statusText))
        }
        set(value) {
            self.setOptionalValue(for: Request.statusText, to: StringValue.of(optional: value))
        }
    }

    open var systemID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.systemID))
        }
        set(value) {
            self.setOptionalValue(for: Request.systemID, to: StringValue.of(optional: value))
        }
    }

    open var telExtens: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.telExtens))
        }
        set(value) {
            self.setOptionalValue(for: Request.telExtens, to: StringValue.of(optional: value))
        }
    }

    open var telNumber: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.telNumber))
        }
        set(value) {
            self.setOptionalValue(for: Request.telNumber, to: StringValue.of(optional: value))
        }
    }

    open var transportRequest: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.transportRequest))
        }
        set(value) {
            self.setOptionalValue(for: Request.transportRequest, to: StringValue.of(optional: value))
        }
    }

    open var userName: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Request.userName))
        }
        set(value) {
            self.setOptionalValue(for: Request.userName, to: StringValue.of(optional: value))
        }
    }
}
