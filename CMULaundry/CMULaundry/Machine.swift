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
    var status: Int {
        didSet {
            // If the machine isn't running, reset the timeRemaining.
            if (self.status != Machine.RUNNING) {
                timeRemaining = 0
            }
        }
    }
    var number: Int
    var timeRemaining = 0 {
        didSet {
            // If the machine has time remaining, set it to running.
            self.status = Machine.RUNNING
        }
    }
    
    // Constructor for the Machine class.
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
    
    func setStatusWithString(machineText : String) {
        // Set the status of the machines based on what is in the scraped text.
        if (machineText.rangeOfString("available") != nil) {
            self.status = Machine.AVAILABLE;
        }
        else if (machineText.rangeOfString("time remaining") != nil) {
            self.status = Machine.RUNNING;
            // Scrape the time remaining from the text and set it to the machine's time remaining.
            let startRange : Range? = machineText.rangeOfString("remaining");
            let endRange : Range? = machineText.rangeOfString("min");
            if (startRange != nil && endRange != nil) {
                let startIndex = startRange!.startIndex.advancedBy(10);
                let endIndex = endRange!.startIndex.advancedBy(-1);
                let newRange = Range(start: startIndex, end: endIndex);
                timeRemaining = Int(machineText.substringWithRange(newRange))!;
            }
        }
        else if (machineText.rangeOfString("out of service") != nil) {
            self.status = Machine.OUTOFSERVICE;
        }
        else if (machineText.rangeOfString("ended") != nil) {
            self.status = Machine.ENDED;
        }
        else if (machineText.rangeOfString("unknown") != nil) {
            self.status = Machine.UNKNOWN;
        }
        else {
            self.status = Machine.UNKNOWN;
        }
    }
}