//
//  Extension+MarqueeLabel.swift
//  PermitToWork
//
//  Created by Rajat.Jain on 24/02/21.
//

import Foundation


extension MarqueeLabel{
    func setUp(){
       // self.type = .continuous
        self.textAlignment = .left
        self.lineBreakMode = .byTruncatingHead
        self.speed = .duration(10.0)
        self.fadeLength = 15.0
        self.leadingBuffer = 40.0
    }
}
