//
//  DirectoryMemberCell.swift
//  Xerung
//
//  Created by mityung on 16/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class DirectoryMemberCell: UITableViewCell {

    @IBOutlet weak var typeLabel: UILabel!
    @IBOutlet weak var memberNumberlabel: UILabel!
    @IBOutlet var view: UIView!
    @IBOutlet weak var memberNameLabel: UILabel!
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
