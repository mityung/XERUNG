//
//  BloodCell.swift
//  Xerung
//
//  Created by mityung on 20/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class BloodCell: UITableViewCell {

    @IBOutlet var view: UIView!
    @IBOutlet weak var groupCount: UILabel!
    @IBOutlet weak var bloodGroup: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        
        groupCount.layer.cornerRadius = groupCount.frame.height/2
        groupCount.layer.masksToBounds = true
        groupCount.backgroundColor = themeColor
        groupCount.textAlignment = .center
        groupCount.textColor = UIColor.white
        view.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        view.dropShadow()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
