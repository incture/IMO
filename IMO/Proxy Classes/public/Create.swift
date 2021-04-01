// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class Create: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var reqID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "ReqId")

    public static var status: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "Status")

    public static var userName: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "UserName")

    public static var rejectionText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "RejectionText")

    public static var purchaseGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "PurchaseGroup")

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "Country")

    public static var messageType: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "MessageType")

    public static var message: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create.property(withName: "Message")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.create)
    }

    open class func array(from: EntityValueList) -> Array<Create> {
        return ArrayConverter.convert(from.toArray(), Array<Create>())
    }

    open func copy() -> Create {
        return CastRequired<Create>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.country))
        }
        set(value) {
            self.setOptionalValue(for: Create.country, to: StringValue.of(optional: value))
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
            return StringValue.optional(self.optionalValue(for: Create.message))
        }
        set(value) {
            self.setOptionalValue(for: Create.message, to: StringValue.of(optional: value))
        }
    }

    open var messageType: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.messageType))
        }
        set(value) {
            self.setOptionalValue(for: Create.messageType, to: StringValue.of(optional: value))
        }
    }

    open var old: Create {
        return CastRequired<Create>.from(self.oldEntity)
    }

    open var purchaseGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.purchaseGroup))
        }
        set(value) {
            self.setOptionalValue(for: Create.purchaseGroup, to: StringValue.of(optional: value))
        }
    }

    open var rejectionText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.rejectionText))
        }
        set(value) {
            self.setOptionalValue(for: Create.rejectionText, to: StringValue.of(optional: value))
        }
    }

    open var reqID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.reqID))
        }
        set(value) {
            self.setOptionalValue(for: Create.reqID, to: StringValue.of(optional: value))
        }
    }

    open var status: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.status))
        }
        set(value) {
            self.setOptionalValue(for: Create.status, to: StringValue.of(optional: value))
        }
    }

    open var userName: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Create.userName))
        }
        set(value) {
            self.setOptionalValue(for: Create.userName, to: StringValue.of(optional: value))
        }
    }
}
