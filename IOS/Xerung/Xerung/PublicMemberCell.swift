//
//  PublicMemberCell.swift
//  Xerung
//
//  Created by mityung on 16/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class PublicMemberCell: UITableViewCell {

    @IBOutlet var view1: UIView!
    @IBOutlet weak var placeLabel: UILabel!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var contactLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        view1.dropShadow()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
