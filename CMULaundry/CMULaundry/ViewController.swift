//
//  ViewController.swift
//  CMULaundry
//
//  Created by Ian Lo on 2016-02-08.
//  Copyright © 2016 Ian Lo. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {
    // MARK: Properties
    @IBOutlet weak var machineTextField: UITextField!
    @IBOutlet weak var machineNumberLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Handle the text field's user input through delegate callbacks.
        machineTextField.delegate = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: UITextFieldDelegate
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        // Hide the keyboard.
        textField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        machineNumberLabel.text = textField.text
    }
    
    // MARK: Actions
    @IBAction func setDefaultLabelText(sender: UIButton) {
        machineNumberLabel.text = "Default Text"
    }
    
}

