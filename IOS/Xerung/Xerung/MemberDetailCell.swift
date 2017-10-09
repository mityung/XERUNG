//
//  MemberDetailCell.swift
//  Xerung
//
//  Created by mityung on 22/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class MemberDetailCell: UITableViewCell {
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    
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
