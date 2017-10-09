//
//  RegisterCell.swift
//  Xerung
//
//  Created by mityung on 15/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class RegisterCell: UITableViewCell {

    @IBOutlet weak var submitButton: UIButton!
    @IBOutlet weak var countryText: UITextField!
    @IBOutlet weak var cityText: UITextField!
    @IBOutlet weak var addressText: UITextField!
    @IBOutlet weak var alternateNumber: UITextField!
    @IBOutlet weak var mobileNumber: UITextField!
    @IBOutlet weak var emailAddress: UITextField!
    @IBOutlet weak var nameText: UITextField!
    override func awakeFromNib() {
        super.awakeFromNib()
        
        submitButton.layer.cornerRadius = 5
        submitButton.layer.borderWidth = 1
        submitButton.layer.borderColor = themeColor.cgColor
        
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
