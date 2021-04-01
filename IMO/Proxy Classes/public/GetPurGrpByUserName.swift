// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class GetPurGrpByUserName: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var purchGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "PurchGroup")

    public static var description: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Description")

    public static var xubname: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Xubname")

    public static var reqID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "ReqId")

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Country")

    public static var activity: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Activity")

    public static var status: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName.property(withName: "Status")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpByUserName)
    }

    open var activity: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.activity))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.activity, to: StringValue.of(optional: value))
        }
    }

    open class func array(from: EntityValueList) -> Array<GetPurGrpByUserName> {
        return ArrayConverter.convert(from.toArray(), Array<GetPurGrpByUserName>())
    }

    open func copy() -> GetPurGrpByUserName {
        return CastRequired<GetPurGrpByUserName>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.country))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.country, to: StringValue.of(optional: value))
        }
    }

    open var description: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.description))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.description, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(reqID: String?) -> EntityKey {
        return EntityKey().with(name: "ReqId", value: StringValue.of(optional: reqID))
    }

    open var old: GetPurGrpByUserName {
        return CastRequired<GetPurGrpByUserName>.from(self.oldEntity)
    }

    open var purchGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.purchGroup))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.purchGroup, to: StringValue.of(optional: value))
        }
    }

    open var reqID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.reqID))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.reqID, to: StringValue.of(optional: value))
        }
    }

    open var status: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.status))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.status, to: StringValue.of(optional: value))
        }
    }

    open var xubname: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpByUserName.xubname))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpByUserName.xubname, to: StringValue.of(optional: value))
        }
    }
}
