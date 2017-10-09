//
//  ContactViewCell.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class ContactViewCell: UITableViewCell {
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet var view: UIView!
    @IBOutlet var firstLetterLabel: UILabel!

    @IBOutlet var callButton: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        view.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        view.dropShadow()
        firstLetterLabel.layer.cornerRadius = firstLetterLabel.frame.height/2
        firstLetterLabel.layer.masksToBounds = true
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
