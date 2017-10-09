//
//  UserProfileCell.swift
//  Xerung
//
//  Created by mityung on 03/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class UserProfileCell: UITableViewCell {

    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var countryText: UITextField!
    @IBOutlet weak var CityText: UITextField!
    @IBOutlet weak var AddressText: UITextField!
    @IBOutlet weak var ProfessionText: UITextField!
    @IBOutlet weak var companyName: UITextField!
    @IBOutlet weak var BloodText: UITextField!
    @IBOutlet weak var alternateMobileText: UITextField!
    @IBOutlet weak var mobileText: UITextField!
    @IBOutlet weak var nameText: UITextField!
    @IBOutlet weak var emailText: UITextField!
    override func awakeFromNib() {
        super.awakeFromNib()
        userImage.layer.cornerRadius = userImage.frame.height/2
        userImage.layer.masksToBounds = true
        
        
        
        
        saveButton.layer.cornerRadius = 5
        saveButton.layer.borderWidth = 1
        saveButton.layer.borderColor = themeColor.cgColor
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
