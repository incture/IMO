//
//  MPConfig.swift
//  DORT
//
//  Created by Mahabaleshwar Hegde on 09/05/18.
//  Copyright © 2018 Murphy. All rights reserved.
//

import Foundation

class MPConfig {
    
    //    private let host: String
    //    private let newtworkProtocol: String
    let baseURL: String = BaseUrl.apiURL + "/"
    
    //    var baseURL: String! {
    //        return self.newtworkProtocol + "://" + self.host + "/"
    //    }
    
    static let shared: MPConfig = MPConfig()
    
    private init() {
        
    }
}
