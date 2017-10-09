//
//  PublicDirectoryCell.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class PublicDirectoryCell: UITableViewCell {

    @IBOutlet var view: UIView!
    @IBOutlet weak var directorylist: UILabel!
    @IBOutlet weak var directoryService: UILabel!
    @IBOutlet weak var directoryName: UILabel!
    @IBOutlet weak var publicDirectoryImage: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
       publicDirectoryImage.layer.cornerRadius = publicDirectoryImage.frame.height/2
        publicDirectoryImage.layer.borderColor = themeColor.cgColor
        publicDirectoryImage.layer.borderWidth = 1
        publicDirectoryImage.layer.masksToBounds = true
        
        view.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        view.dropShadow()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
extension UIView {
    
    func dropShadow() {
        
        self.layer.shadowRadius = 10
        self.layer.shadowOffset = CGSize(width: 0, height: 0)
        self.layer.shadowColor = UIColor.black.cgColor
        self.clipsToBounds = false
        self.layer.shadowOpacity = 0.3;
        self.backgroundColor = UIColor.white
        
        
        /*self.backgroundColor = UIColor.white
        self.layer.masksToBounds = false
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOpacity = 0.5
        self.layer.shadowOffset = CGSize(width: -1, height: 1)
        self.layer.shadowRadius = 1
        
        self.layer.shadowPath = UIBezierPath(rect: self.bounds).cgPath
        self.layer.shouldRasterize = true*/
    }
}
