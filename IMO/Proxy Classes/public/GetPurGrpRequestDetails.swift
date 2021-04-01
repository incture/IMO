// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

open class GetPurGrpRequestDetails: EntityValue {
    public required init(from decoder: Decoder) throws {
        try super.init(from: decoder)
    }

    public static var reqID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "ReqId")

    public static var purchGroup: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "PurchGroup")

    public static var description: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Description")

    public static var country: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Country")

    public static var telnopurchgp: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Telnopurchgp")

    public static var numberRangeInd: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "NumberRangeInd")

    public static var outputDevice: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "OutputDevice")

    public static var faxNumber: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "FaxNumber")

    public static var telephone: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Telephone")

    public static var `extension`: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Extension")

    public static var eMailAddress: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "EMailAddress")

    public static var xubname: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Xubname")

    public static var activity: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Activity")

    public static var status: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Status")

    public static var trNo: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "TRNo")

    public static var createdOn: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "CreatedOn")

    public static var changedOn: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "ChangedOn")

    public static var createdBy: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "CreatedBy")

    public static var changeBy: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "ChangeBy")

    public static var statusText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "StatusText")

    public static var rejectionText: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "RejectionText")

    public static var messageType: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "MessageType")

    public static var message: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Message")

    public static var systemID: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "SystemId")

    public static var personrespnsble: Property = ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails.property(withName: "Personrespnsble")

    public init(withDefaults: Bool = true) {
        super.init(withDefaults: withDefaults, type: ZMURPURCHASEGROUPSRVEntitiesMetadata.EntityTypes.getPurGrpRequestDetails)
    }

    open var `extension`: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.extension))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.extension, to: StringValue.of(optional: value))
        }
    }

    open var activity: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.activity))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.activity, to: StringValue.of(optional: value))
        }
    }

    open class func array(from: EntityValueList) -> Array<GetPurGrpRequestDetails> {
        return ArrayConverter.convert(from.toArray(), Array<GetPurGrpRequestDetails>())
    }

    open var changeBy: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.changeBy))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.changeBy, to: StringValue.of(optional: value))
        }
    }

    open var changedOn: LocalDateTime? {
        get {
            return LocalDateTime.castOptional(self.optionalValue(for: GetPurGrpRequestDetails.changedOn))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.changedOn, to: value)
        }
    }

    open func copy() -> GetPurGrpRequestDetails {
        return CastRequired<GetPurGrpRequestDetails>.from(self.copyEntity())
    }

    open var country: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.country))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.country, to: StringValue.of(optional: value))
        }
    }

    open var createdBy: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.createdBy))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.createdBy, to: StringValue.of(optional: value))
        }
    }

    open var createdOn: LocalDateTime? {
        get {
            return LocalDateTime.castOptional(self.optionalValue(for: GetPurGrpRequestDetails.createdOn))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.createdOn, to: value)
        }
    }

    open var description: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.description))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.description, to: StringValue.of(optional: value))
        }
    }

    open var eMailAddress: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.eMailAddress))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.eMailAddress, to: StringValue.of(optional: value))
        }
    }

    open var faxNumber: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.faxNumber))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.faxNumber, to: StringValue.of(optional: value))
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
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.message))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.message, to: StringValue.of(optional: value))
        }
    }

    open var messageType: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.messageType))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.messageType, to: StringValue.of(optional: value))
        }
    }

    open var numberRangeInd: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.numberRangeInd))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.numberRangeInd, to: StringValue.of(optional: value))
        }
    }

    open var old: GetPurGrpRequestDetails {
        return CastRequired<GetPurGrpRequestDetails>.from(self.oldEntity)
    }

    open var outputDevice: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.outputDevice))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.outputDevice, to: StringValue.of(optional: value))
        }
    }

    open var personrespnsble: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.personrespnsble))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.personrespnsble, to: StringValue.of(optional: value))
        }
    }

    open var purchGroup: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.purchGroup))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.purchGroup, to: StringValue.of(optional: value))
        }
    }

    open var rejectionText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.rejectionText))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.rejectionText, to: StringValue.of(optional: value))
        }
    }

    open var reqID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.reqID))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.reqID, to: StringValue.of(optional: value))
        }
    }

    open var status: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.status))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.status, to: StringValue.of(optional: value))
        }
    }

    open var statusText: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.statusText))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.statusText, to: StringValue.of(optional: value))
        }
    }

    open var systemID: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.systemID))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.systemID, to: StringValue.of(optional: value))
        }
    }

    open var telephone: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.telephone))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.telephone, to: StringValue.of(optional: value))
        }
    }

    open var telnopurchgp: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.telnopurchgp))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.telnopurchgp, to: StringValue.of(optional: value))
        }
    }

    open var trNo: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.trNo))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.trNo, to: StringValue.of(optional: value))
        }
    }

    open var xubname: String? {
        get {
            return StringValue.optional(self.optionalValue(for: GetPurGrpRequestDetails.xubname))
        }
        set(value) {
            self.setOptionalValue(for: GetPurGrpRequestDetails.xubname, to: StringValue.of(optional: value))
        }
    }
}
