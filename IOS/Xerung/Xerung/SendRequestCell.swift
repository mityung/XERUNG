//
//  SendRequestCell.swift
//  Xerung
//
//  Created by mityung on 09/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class SendRequestCell: UITableViewCell {

    
    @IBOutlet var sendButton: UIButton!
   
    @IBOutlet var view: UIView!
    @IBOutlet weak var nameLabel: UILabel!
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
