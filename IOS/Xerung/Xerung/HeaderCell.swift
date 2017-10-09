//
//  HeaderCell.swift
//  Xerung
//
//  Created by mityung on 16/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class HeaderCell: UITableViewCell {

    @IBOutlet var aboutLabel: UILabel!
    @IBOutlet weak var directoryMoto: UILabel!
    @IBOutlet weak var dirtectoryType: UILabel!
    @IBOutlet weak var directoryImage: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
