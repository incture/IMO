//
//  IMOEndpoints.swift
//  PermitToWork
//
//  Created by Rajat Jain on 19/04/21.
//

import Foundation

struct IMOEndpoints {

    static let getListOfActiveWorkers = BaseUrl.apiURL + "/JAVA_API_DEST/getListOfActiveWorker?"
    static let getJSAByLocation =  BaseUrl.apiURL + "/JAVA_API_DEST/getjsabylocation?"
    static let approveJSA = BaseUrl.apiURL + "/JAVA_API_DEST/approvejsa"

    static let locationService = "https://iop.cfapps.eu10.hana.ondemand.com/imo/location/getLocation"

    static let getPermits =  BaseUrl.apiURL + "/JAVA_API_DEST/getpermitsbylocation?facility="
    static let createService =  BaseUrl.apiURL + "/JAVA_API_DEST/createservice"
    static let getJSAByPermitNumber = BaseUrl.apiURL + "/JAVA_API_DEST/getjsabypermitnumber?"
    static let getPermitDetails = BaseUrl.apiURL + "/JAVA_API_DEST/permitdetails/get?"
    static let getPtwRecordResult  = BaseUrl.apiURL + "/JAVA_API_DEST/getPtwRecordResult?permitNumber="
    static let closeOut = BaseUrl.apiURL + "/JAVA_API_DEST/closeoutservice"
    static let approvePermit = BaseUrl.apiURL + "/JAVA_API_DEST/approvepermit"
    static let updateJSA = BaseUrl.apiURL + "/JAVA_API_DEST/updatejsa"
    static let updatePermit = BaseUrl.apiURL + "/JAVA_API_DEST/updatepermitservice"
}
