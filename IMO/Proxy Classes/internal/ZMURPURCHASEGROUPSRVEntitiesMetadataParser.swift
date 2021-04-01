// # Proxy Compiler 17.9.5-709eff-20171206

import Foundation
import SAPOData

internal class ZMURPURCHASEGROUPSRVEntitiesMetadataParser {
    internal static let options: Int = (CSDLOption.processMixedVersions | CSDLOption.retainOriginalText | CSDLOption.ignoreUndefinedTerms)

    internal static let parsed: CSDLDocument = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.parse()

    static func parse() -> CSDLDocument {
        let parser: CSDLParser = CSDLParser()
        parser.logWarnings = false
        parser.csdlOptions = ZMURPURCHASEGROUPSRVEntitiesMetadataParser.options
        let metadata: CSDLDocument = parser.parseInProxy(ZMURPURCHASEGROUPSRVEntitiesMetadataText.xml, url: "ZMUR_PURCHASE_GROUP_SRV")
        metadata.proxyVersion = "17.9.5-709eff-20171206"
        return metadata
    }
}
