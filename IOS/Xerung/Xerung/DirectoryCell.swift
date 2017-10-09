//
//  DirectoryCell.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class DirectoryCell: UITableViewCell {

    @IBOutlet var countLabel: UILabel!
    @IBOutlet var view: UIView!
    @IBOutlet weak var directoryMember: UILabel!
    @IBOutlet weak var directoryName: UILabel!
    @IBOutlet weak var directoryImage: UIImageView!
    @IBOutlet var typeLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        directoryImage.layer.cornerRadius = directoryImage.frame.width/2
        directoryImage.layer.masksToBounds = true
        directoryImage.layer.borderWidth = 1
        directoryImage.layer.borderColor = themeColor.cgColor
       // view.dropShadow()
        view.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        view.dropShadow()
        
        countLabel.backgroundColor = themeColor
        countLabel.layer.cornerRadius = countLabel.frame.height/2
        countLabel.layer.masksToBounds = true
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
