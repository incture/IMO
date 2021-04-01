// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class GetResponsiblePerson: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var userID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.property(withName: "UserId")

    public static var fname: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.property(withName: "Fname")

    public static var lname: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson.property(withName: "Lname")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getResponsiblePerson)
    }

    open class func array(from: EntityValueList) -> Array<GetResponsiblePerson> {
        return ArrayConverter.convert(from.toArray(), Array<GetResponsiblePerson>())
    }

    open func copy() -> GetResponsiblePerson {
        return CastRequired<GetResponsiblePerson>.from(self.copyEntity())
    }

    open var fname: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetResponsiblePerson.fname))
        }
        set(value) {
            self.setOptionalValue(for: GetResponsiblePerson.fname, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(userID: String?) -> EntityKey {
        return EntityKey().with(name: "UserId", value: StringValue.of(optional: userID))
    }

    open var lname: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetResponsiblePerson.lname))
        }
        set(value) {
            self.setOptionalValue(for: GetResponsiblePerson.lname, to: StringValue.of(optional: value))
        }
    }

    open var old: GetResponsiblePerson {
        return CastRequired<GetResponsiblePerson>.from(self.oldEntity)
    }

    open var userID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetResponsiblePerson.userID))
        }
        set(value) {
            self.setOptionalValue(for: GetResponsiblePerson.userID, to: StringValue.of(optional: value))
        }
    }
}
