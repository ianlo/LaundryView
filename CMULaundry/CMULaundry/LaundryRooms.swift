//
//  LaundryRooms.swift
//  CMULaundry
//
//  Created by Ian Lo on 2016-01-29.
//  Copyright © 2016 Ian Lo. All rights reserved.
//

import Foundation

class LaundryRoom {
    // The location of the LaundryRoom.
    var name: String
    // The URL containg the data for that location.
    var url: String
    init(name: String, url: String) {
        self.name = name
        self.url = url
    }
}