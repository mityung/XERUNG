//
//  DirectoryDetailViewController.swift
//  Xerung
//
//  Created by mityung on 21/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class DirectoryDetailViewController: ButtonBarPagerTabStripViewController , UIPopoverPresentationControllerDelegate {

    let blueInstagramColor = UIColor(red: 71/255.0, green: 71/255.0, blue: 71/255.0, alpha: 1.0)
    
    @IBOutlet var view1: UIView!
    @IBOutlet var directoryAbout: UILabel!
    @IBOutlet var directoryTagName: UILabel!
    @IBOutlet var directoryName: UILabel!
    @IBOutlet var userImage: UIImageView!
    override func viewDidLoad() {
        
        
        settings.style.buttonBarBackgroundColor = .white
        settings.style.buttonBarItemBackgroundColor = .white
        settings.style.selectedBarBackgroundColor = themeColor
        settings.style.buttonBarItemFont = UIFont(name: "Helvetica", size: 16)!
        
        settings.style.selectedBarHeight = 2.0
        settings.style.buttonBarMinimumLineSpacing = 0
        settings.style.buttonBarItemTitleColor = UIColor(red: 146/255.0, green: 147/255.0, blue: 156/255.0, alpha: 0.8)
        
        settings.style.buttonBarItemTitleColor = UIColor.green
        settings.style.buttonBarItemsShouldFillAvailiableWidth = true
        settings.style.buttonBarLeftContentInset = 0
        settings.style.buttonBarRightContentInset = 0
        containerView.isScrollEnabled = false
        
        changeCurrentIndexProgressive = { [weak self] (oldCell: ButtonBarViewCell?, newCell: ButtonBarViewCell?, progressPercentage: CGFloat, changeCurrentIndex: Bool, animated: Bool) -> Void in
            guard changeCurrentIndex == true else { return }
            oldCell?.label.textColor = UIColor(red: 146/255.0, green: 147/255.0, blue: 156/255.0, alpha: 0.8)
            newCell?.label.textColor = self?.blueInstagramColor
            
           /* if newCell?.label.text == "All User" {
                self?.containerView.isScrollEnabled = false
            }else{
                self?.containerView.isScrollEnabled = true
            }*/
            
            

            
        }
        
        
        super.viewDidLoad()
        self.title = selectedGroupName
        self.tabBarController?.tabBar.isHidden = true
        
        if selectedAdminFlag == "1" {
            navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named:"bar"), style: .plain, target: self, action: #selector(self.setting(_:)))
            
            //self.navigationItem.setRightBarButton(UIBarButtonItem(barButtonSystemItem: .edit , target: self, action: #selector(self.setting(_:))), animated: true)
        }
        directoryName.text = selectedGroupName
        directoryAbout.text = selectedGroupAbout
        directoryTagName.text = selectedGroupCalled
        
        userImage.layer.cornerRadius = userImage.frame.width/2
        userImage.layer.masksToBounds = true
        userImage.layer.borderWidth = 1
        userImage.layer.borderColor = themeColor.cgColor
        if selectedGroupPhoto != "" && selectedGroupPhoto != "0"  {
            if  let imageData = Data(base64Encoded: selectedGroupPhoto , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                userImage.image = DecodedImage
            }
        }else{
            userImage.image = UIImage(named: "defaultDirectory")
        }
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
        view1.dropShadow()
        
        /*let backgroundImage = UIImageView(frame: self.view1.bounds)
        backgroundImage.image = UIImage(named: "backScreen.png")
        self.view1.insertSubview(backgroundImage, at: 0)*/
        
       // let image = UIImage(named: "backScreen.png")!
        //self.view1.backgroundColor = UIColor(patternImage: image )
        
    }
    
    
    func setting(_ sender:UIButton) {
        let refreshAlert = UIAlertController(title: "", message: "", preferredStyle: .actionSheet )
        
      let pendingMember =  UIAlertAction(title: "Pending Member", style: .default , handler: { (action: UIAlertAction!) in
            let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "PendingMemberViewController") as! PendingMemberViewController
            viewController.groupId = selectedGroupId
            self.navigationController?.pushViewController(viewController, animated: true)
        })
        
        pendingMember.setValue(UIImage(named: "pendingmember")?.withRenderingMode(.alwaysOriginal) , forKey: "image")
        refreshAlert.addAction(pendingMember)
         
        
       let addMember =  UIAlertAction(title: "Add Member", style: .default , handler: { (action: UIAlertAction!) in
            let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "AddMemberViewController") as! AddMemberViewController
            viewController.groupId = selectedGroupId
            self.navigationController?.pushViewController(viewController, animated: true)
        })
        
        addMember.setValue(UIImage(named: "addmember")?.withRenderingMode(.alwaysOriginal) , forKey: "image")
        refreshAlert.addAction(addMember)
        
      let editDirectory =  UIAlertAction(title: "Edit Directory", style: .default , handler: { (action: UIAlertAction!) in
            let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "EditDirectoryViewController") as! EditDirectoryViewController
            self.navigationController?.pushViewController(viewController, animated: true)
        })
        
        editDirectory.setValue(UIImage(named: "edit")?.withRenderingMode(.alwaysOriginal) , forKey: "image")
        refreshAlert.addAction(editDirectory)
        
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel , handler: { (action: UIAlertAction!) in
            
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }

    
    
    /*internal func viewControllersForPagerTabStrip(_ pagerTabStripController: PagerTabStripViewController) -> [UIViewController] {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let childOneVC = storyboard.instantiateViewController(withIdentifier: "BloodGroupViewController")
        let childTwoVC = storyboard.instantiateViewController(withIdentifier: "CityViewController")
        let childThreeVC = storyboard.instantiateViewController(withIdentifier: "ProfessionViewController")
        let childFourVC = storyboard.instantiateViewController(withIdentifier: "DirectoryMemberViewController")
        return [childFourVC,childOneVC, childTwoVC , childThreeVC ]
    }*/
    
    override public func viewControllers(for pagerTabStripController: PagerTabStripViewController) -> [UIViewController] {
        let childOneVC = storyboard?.instantiateViewController(withIdentifier: "BloodGroupViewController")
        let childTwoVC = storyboard?.instantiateViewController(withIdentifier: "CityViewController")
        let childThreeVC = storyboard?.instantiateViewController(withIdentifier: "ProfessionViewController")
        let childFourVC = storyboard?.instantiateViewController(withIdentifier: "DirectoryMemberViewController")
        return [childFourVC!,childOneVC!, childTwoVC! , childThreeVC! ]
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
