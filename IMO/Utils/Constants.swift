//
//  Constants.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 09/10/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import Foundation


// Cutover for Production
// 1. Change api URL to prodURL
// 2. Change configurationList url
// 3. change App Name
// 4. Change app Bundle Identifier
// 5. Change app Update URL (CurrentApp)
// 6. Change custom url (URL Types in Info.plist)
// 7. Change app Version (Build Number)

struct BaseUrl{
    
    static var apiURL = ConstantServer.qaURL
}

struct ConstantServer {
    
   
    static var qaURL = "https://hrapps-imo-dft-com-incture-imodft.cfapps.eu10.hana.ondemand.com"
    
    
}
//com.incture.MurphyDFT.Dev // dev instance
//com.incture.MurphyDFT//QA instance
//com.incture.MurphyDFT.Prod

struct CurrentApp{
    static var appStandard = AppUpdate.qaApp
}

struct AppUpdate {
    static var qaApp = "DFTQA"
    static var prodApp = "DFT"
}

//struct ImageFileType {
//    var uti: CFString
//    var fileExtention: String
//
//    // This list can include anything returned by CGImageDestinationCopyTypeIdentifiers()
//    // I'm including only the popular formats here
//    static let bmp = ImageFileType(uti: kUTTypeBMP, fileExtention: "bmp")
//    static let gif = ImageFileType(uti: kUTTypeGIF, fileExtention: "gif")
//    static let jpg = ImageFileType(uti: kUTTypeJPEG, fileExtention: "jpg")
//    static let png = ImageFileType(uti: kUTTypePNG, fileExtention: "png")
//    static let tiff = ImageFileType(uti: kUTTypeTIFF, fileExtention: "tiff")
//}


enum ActionType : String {
    
    case Create = "CREATE"
    case Verify = "VERIFY"
    case Reject = "REJECT"
    case Review = "REVIEW"
    case Unsync = "UNSYNCED"
    case UnknownType
    
}

enum Filters : String {
    
    case Create = "Created"
    case Verify = "Verified"
    case Reject = "Rejected"
    case Review = "Reviewed"
    case Unsync = "Not Synced"
    case UnknownType
}

enum TicketStatus : String {
    
    case Create = "Ticket Created"
    case Verify = "Ticket Verified"
    case Reject = "Ticket Rejected"
    case Review = "Ticket Reviewed"
    case Unsync = "Not Synced"
    case UnknownType
    
}
