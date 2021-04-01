// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class Change: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var reqID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "ReqId")

    public static var rejectionText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "RejectionText")

    public static var status: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "Status")

    public static var purchaseGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "PurchaseGroup")

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "Country")

    public static var userName: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "UserName")

    public static var messageType: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "MessageType")

    public static var message: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change.property(withName: "Message")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.change)
    }

    open class func array(from: EntityValueList) -> Array<Change> {
        return ArrayConverter.convert(from.toArray(), Array<Change>())
    }

    open func copy() -> Change {
        return CastRequired<Change>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.country))
        }
        set(value) {
            self.setOptionalValue(for: Change.country, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(reqID: String?) -> EntityKey {
        return EntityKey().with(name: "ReqId", value: StringValue.of(optional: reqID))
    }

    open var message: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.message))
        }
        set(value) {
            self.setOptionalValue(for: Change.message, to: StringValue.of(optional: value))
        }
    }

    open var messageType: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.messageType))
        }
        set(value) {
            self.setOptionalValue(for: Change.messageType, to: StringValue.of(optional: value))
        }
    }

    open var old: Change {
        return CastRequired<Change>.from(self.oldEntity)
    }

    open var purchaseGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.purchaseGroup))
        }
        set(value) {
            self.setOptionalValue(for: Change.purchaseGroup, to: StringValue.of(optional: value))
        }
    }

    open var rejectionText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.rejectionText))
        }
        set(value) {
            self.setOptionalValue(for: Change.rejectionText, to: StringValue.of(optional: value))
        }
    }

    open var reqID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.reqID))
        }
        set(value) {
            self.setOptionalValue(for: Change.reqID, to: StringValue.of(optional: value))
        }
    }

    open var status: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.status))
        }
        set(value) {
            self.setOptionalValue(for: Change.status, to: StringValue.of(optional: value))
        }
    }

    open var userName: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Change.userName))
        }
        set(value) {
            self.setOptionalValue(for: Change.userName, to: StringValue.of(optional: value))
        }
    }
}
