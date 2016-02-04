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
    var status: Int = 0 {
        didSet {
            if (self.status != Machine.RUNNING) {
                timeRemaining = 0
            }
        }
    }
    var number: Int
    var timeRemaining = 0
    
    init(type: Int, status: Int, number: Int) {
        self.type = type
        self.status = status
        self.number = number
    }
    
    func getStringStatus() -> String{
        switch type {
        case Machine.AVAILABLE:
            return "Available"
        case Machine.RUNNING:
            return "Running (" + String(timeRemaining) + " min left)"
        case Machine.ENDED:
            return "Cycle Ended"
        case Machine.OUTOFSERVICE:
            return "Out of Service"
        case Machine.UNKNOWN:
            return "Status Unknown"
        default:
            return "Status Unknown"
        }
    }
}