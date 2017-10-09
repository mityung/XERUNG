//
//  AddMemberCell2.swift
//  Xerung
//
//  Created by mityung on 24/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class AddMemberCell2: UITableViewCell {

    @IBOutlet weak var countryText: UITextField!
    @IBOutlet weak var tickImage: UIImageView!
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet var view: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
         view.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        view.dropShadow()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
