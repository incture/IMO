//
//  IMOEndpoints.swift
//  PermitToWork
//
//  Created by Rajat Jain on 19/04/21.
//

import Foundation

struct IMOEndpoints {
    
    static let getListOfActiveWorkers = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/getListOfActiveWorker?"
    static let getJSAByLocation = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/getjsabylocation?"
    static let approveJSA = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/approvejsa"
    static let locationService = "https://imob0ot37y8l6.hana.ondemand.com/TaskManagement_Rest/imo/location/getLocation"
    static let getPermits = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/getpermitsbylocation?facility="
    static let createService = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/createservice"
    static let getJSAByPermitNumber = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/getjsabypermitnumber?"
    static let getPtwRecordResult  = "https://ptwb0ot37y8l6.hana.ondemand.com/ptw/getPtwRecordResult?permitNumber="
}
