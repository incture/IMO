// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class Reject: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var reqID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "ReqId")

    public static var ekgrp: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Ekgrp")

    public static var eknam: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Eknam")

    public static var land1: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Land1")

    public static var ektel: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Ektel")

    public static var nrind: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Nrind")

    public static var ldest: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Ldest")

    public static var telfx: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Telfx")

    public static var telNumber: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "TelNumber")

    public static var telExtens: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "TelExtens")

    public static var smtpAddr: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "SmtpAddr")

    public static var xubname: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Xubname")

    public static var activity: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Activity")

    public static var status: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Status")

    public static var trkorr: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Trkorr")

    public static var createdOn: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "CreatedOn")

    public static var changedOn: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "ChangedOn")

    public static var createdBy: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "CreatedBy")

    public static var changeBy: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "ChangeBy")

    public static var statusText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "StatusText")

    public static var rejectionText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "RejectionText")

    public static var messageType: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "MessageType")

    public static var message: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Message")

    public static var systemID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "SystemId")

    public static var sbnamag: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject.property(withName: "Sbnamag")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.reject)
    }

    open var activity: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.activity))
        }
        set(value) {
            self.setOptionalValue(for: Reject.activity, to: StringValue.of(optional: value))
        }
    }

    open class func array(from: EntityValueList) -> Array<Reject> {
        return ArrayConverter.convert(from.toArray(), Array<Reject>())
    }

    open var changeBy: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.changeBy))
        }
        set(value) {
            self.setOptionalValue(for: Reject.changeBy, to: StringValue.of(optional: value))
        }
    }

    open var changedOn: LocalDateTime? {
        get {
            return LocalDateTime.castOptional(self.optionalValue(for: Reject.changedOn))
        }
        set(value) {
            self.setOptionalValue(for: Reject.changedOn, to: value)
        }
    }

    open func copy() -> Reject {
        return CastRequired<Reject>.from(self.copyEntity())
    }

    open var createdBy: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.createdBy))
        }
        set(value) {
            self.setOptionalValue(for: Reject.createdBy, to: StringValue.of(optional: value))
        }
    }

    open var createdOn: LocalDateTime? {
        get {
            return LocalDateTime.castOptional(self.optionalValue(for: Reject.createdOn))
        }
        set(value) {
            self.setOptionalValue(for: Reject.createdOn, to: value)
        }
    }

    open var ekgrp: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.ekgrp))
        }
        set(value) {
            self.setOptionalValue(for: Reject.ekgrp, to: StringValue.of(optional: value))
        }
    }

    open var eknam: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.eknam))
        }
        set(value) {
            self.setOptionalValue(for: Reject.eknam, to: StringValue.of(optional: value))
        }
    }

    open var ektel: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.ektel))
        }
        set(value) {
            self.setOptionalValue(for: Reject.ektel, to: StringValue.of(optional: value))
        }
    }

    open override var isProxy: Bool {
        return true
    }

    open class func key(reqID: String?) -> EntityKey {
        return EntityKey().with(name: "ReqId", value: StringValue.of(optional: reqID))
    }

    open var land1: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.land1))
        }
        set(value) {
            self.setOptionalValue(for: Reject.land1, to: StringValue.of(optional: value))
        }
    }

    open var ldest: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.ldest))
        }
        set(value) {
            self.setOptionalValue(for: Reject.ldest, to: StringValue.of(optional: value))
        }
    }

    open var message: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.message))
        }
        set(value) {
            self.setOptionalValue(for: Reject.message, to: StringValue.of(optional: value))
        }
    }

    open var messageType: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.messageType))
        }
        set(value) {
            self.setOptionalValue(for: Reject.messageType, to: StringValue.of(optional: value))
        }
    }

    open var nrind: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.nrind))
        }
        set(value) {
            self.setOptionalValue(for: Reject.nrind, to: StringValue.of(optional: value))
        }
    }

    open var old: Reject {
        return CastRequired<Reject>.from(self.oldEntity)
    }

    open var rejectionText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.rejectionText))
        }
        set(value) {
            self.setOptionalValue(for: Reject.rejectionText, to: StringValue.of(optional: value))
        }
    }

    open var reqID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.reqID))
        }
        set(value) {
            self.setOptionalValue(for: Reject.reqID, to: StringValue.of(optional: value))
        }
    }

    open var sbnamag: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.sbnamag))
        }
        set(value) {
            self.setOptionalValue(for: Reject.sbnamag, to: StringValue.of(optional: value))
        }
    }

    open var smtpAddr: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.smtpAddr))
        }
        set(value) {
            self.setOptionalValue(for: Reject.smtpAddr, to: StringValue.of(optional: value))
        }
    }

    open var status: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.status))
        }
        set(value) {
            self.setOptionalValue(for: Reject.status, to: StringValue.of(optional: value))
        }
    }

    open var statusText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.statusText))
        }
        set(value) {
            self.setOptionalValue(for: Reject.statusText, to: StringValue.of(optional: value))
        }
    }

    open var systemID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.systemID))
        }
        set(value) {
            self.setOptionalValue(for: Reject.systemID, to: StringValue.of(optional: value))
        }
    }

    open var telExtens: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.telExtens))
        }
        set(value) {
            self.setOptionalValue(for: Reject.telExtens, to: StringValue.of(optional: value))
        }
    }

    open var telNumber: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.telNumber))
        }
        set(value) {
            self.setOptionalValue(for: Reject.telNumber, to: StringValue.of(optional: value))
        }
    }

    open var telfx: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.telfx))
        }
        set(value) {
            self.setOptionalValue(for: Reject.telfx, to: StringValue.of(optional: value))
        }
    }

    open var trkorr: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.trkorr))
        }
        set(value) {
            self.setOptionalValue(for: Reject.trkorr, to: StringValue.of(optional: value))
        }
    }

    open var xubname: String? {
        get {
            return StringValue.optional(self.optionalValue(for: Reject.xubname))
        }
        set(value) {
            self.setOptionalValue(for: Reject.xubname, to: StringValue.of(optional: value))
        }
    }
}
