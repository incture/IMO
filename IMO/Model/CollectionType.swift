//
// CollectionType.swift
// MurphyDFT-Final
//
// Created by SAP Cloud Platform SDK for iOS Assistant application on 01/02/18
//

import Foundation

enum CollectionType: String {

    case createSet = "CreateSet"
    case changeSet = "ChangeSet"
    case getPurGrpSet = "GetPurGrpSet"
    case rejectSet = "RejectSet"
    case requestSet = "RequestSet"
    case getPurGrpByUserNameSet = "GetPurGrpByUserNameSet"
    case getPurGrpRequestDetailsSet = "GetPurGrpRequestDetailsSet"
    case getResponsiblePersonSet = "GetResponsiblePersonSet"
    case getNxtPurGrpByCtrySet = "GetNxtPurGrpByCtrySet"
    case none = ""

    static let all = [
        createSet, changeSet, getPurGrpSet, rejectSet, requestSet, getPurGrpByUserNameSet, getPurGrpRequestDetailsSet, getResponsiblePersonSet, getNxtPurGrpByCtrySet,
    ]
}
