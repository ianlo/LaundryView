//
//  Machine.swift
//  CMULaundry
//
//  Created by Ian Lo on 2016-01-31.
//  Copyright © 2016 Ian Lo. All rights reserved.
//

import Foundation

class Machine {
    // Constants for the status of the Machine.
    static let AVAILABLE = 0
    static let RUNNING = 1
    static let ENDED = 2
    static let OUTOFSERVICE = 3
    static let UNKNOWN = 4

    // Dryer or Washer.
    static let DRYER = 0
    static let WASHER = 1
    
    // Local fields.
    var type: Int
    var status: Int
    var number: Int
    
    init(type: Int, status: Int, number: Int) {
        self.type = type
        self.status = status
        self.number = number
    }
}