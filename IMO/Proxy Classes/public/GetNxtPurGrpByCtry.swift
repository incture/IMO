// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class GetNxtPurGrpByCtry: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry.property(withName: "Country")

    public static var purchGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry.property(withName: "PurchGroup")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getNxtPurGrpByCtry)
    }

    open class func array(from: EntityValueList) -> Array<GetNxtPurGrpByCtry> {
        return ArrayConverter.convert(from.toArray(), Array<GetNxtPurGrpByCtry>())
    }

    open func copy() -> GetNxtPurGrpByCtry {
        return CastRequired<GetNxtPurGrpByCtry>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetNxtPurGrpByCtry.country))
        }
        set(value) {
            self.setOptionalValue(for: GetNxtPurGrpByCtry.country, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(country: String?) -> EntityKey {
        return EntityKey().with(name: "Country", value: StringValue.of(optional: country))
    }

    open var old: GetNxtPurGrpByCtry {
        return CastRequired<GetNxtPurGrpByCtry>.from(self.oldEntity)
    }

    open var purchGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetNxtPurGrpByCtry.purchGroup))
        }
        set(value) {
            self.setOptionalValue(for: GetNxtPurGrpByCtry.purchGroup, to: StringValue.of(optional: value))
        }
    }
}
