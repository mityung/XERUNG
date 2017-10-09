//
//  ProfileHeaderCell.swift
//  Xerung
//
//  Created by mityung on 06/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class ProfileHeaderCell: UITableViewCell {

    
    @IBOutlet var numberLabel: UILabel!
    @IBOutlet var nameLabel: UILabel!
    @IBOutlet weak var makeadminButton: UIButton!
    @IBOutlet weak var userImage: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
