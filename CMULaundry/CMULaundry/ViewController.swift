//
//  ViewController.swift
//  CMULaundry
//
//  Created by Ian Lo on 2016-02-08.
//  Copyright © 2016 Ian Lo. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    // MARK: Properties
    @IBOutlet weak var machineTextField: UITextField!
    @IBOutlet weak var machineNumberLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: Actions
    @IBAction func setDefaultLabelText(sender: UIButton) {
        machineNumberLabel.text = "Default Text"
    }
    
}

