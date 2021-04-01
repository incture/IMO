// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class GetPurGrp: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var client: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Client")

    public static var purchGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "PurchGroup")

    public static var description: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Description")

    public static var telNoPurchGrp: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "TelNoPurchGrp")

    public static var outputDevice: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "OutputDevice")

    public static var faxNumber: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "FaxNumber")

    public static var telephone: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Telephone")

    public static var `extension`: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Extension")

    public static var eMailAddress: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "EMailAddress")

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp.property(withName: "Country")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrp)
    }

    open var `extension`: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.extension))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.extension, to: StringValue.of(optional: value))
        }
    }

    open class func array(from: EntityValueList) -> Array<GetPurGrp> {
        return ArrayConverter.convert(from.toArray(), Array<GetPurGrp>())
    }

    open var client: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.client))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.client, to: StringValue.of(optional: value))
        }
    }

    open func copy() -> GetPurGrp {
        return CastRequired<GetPurGrp>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.country))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.country, to: StringValue.of(optional: value))
        }
    }

    open var description: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.description))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.description, to: StringValue.of(optional: value))
        }
    }

    open var eMailAddress: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.eMailAddress))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.eMailAddress, to: StringValue.of(optional: value))
        }
    }

    open var faxNumber: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.faxNumber))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.faxNumber, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(purchGroup: String?) -> EntityKey {
        return EntityKey().with(name: "PurchGroup", value: StringValue.of(optional: purchGroup))
    }

    open var old: GetPurGrp {
        return CastRequired<GetPurGrp>.from(self.oldEntity)
    }

    open var outputDevice: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.outputDevice))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.outputDevice, to: StringValue.of(optional: value))
        }
    }

    open var purchGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.purchGroup))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.purchGroup, to: StringValue.of(optional: value))
        }
    }

    open var telNoPurchGrp: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.telNoPurchGrp))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.telNoPurchGrp, to: StringValue.of(optional: value))
        }
    }

    open var telephone: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrp.telephone))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrp.telephone, to: StringValue.of(optional: value))
        }
    }
}
