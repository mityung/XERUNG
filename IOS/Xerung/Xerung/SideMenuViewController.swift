//
//  SideMenuViewController.swift
//  Xerung
//
//  Created by Mac on 07/04/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class SideMenuViewController: UIViewController {
    @IBOutlet var userImage: UIImageView!

    @IBOutlet var syncButton: UIButton!
    @IBOutlet var createDirectoryButton: UIButton!
    @IBOutlet var searchButton: UIButton!
    @IBOutlet var dashBoardButton: UIButton!
    @IBOutlet var LogoutButton: UIButton!
    @IBOutlet var profileButton: UIButton!
    
    @IBOutlet var emailAddress: UILabel!
    @IBOutlet var NameLabel: UILabel!
    
    
    var dataDict = [String:String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dashBoardButton.tag = 1
        self.searchButton.tag = 2
        self.createDirectoryButton.tag = 3
        self.syncButton.tag = 4
        self.LogoutButton.tag = 5
        self.profileButton.tag = 6
        
        NameLabel.text = profileJson["NAME"].stringValue
        emailAddress.text = mobileNo
        
        self.dashBoardButton.addTarget(self, action: #selector(self.performAction(sender:)), for: UIControlEvents.touchUpInside)
        self.searchButton.addTarget(self, action: #selector(self.performAction(sender:)), for: UIControlEvents.touchUpInside)
        self.createDirectoryButton.addTarget(self, action: #selector(self.performAction(sender:)), for: UIControlEvents.touchUpInside)
        self.syncButton.addTarget(self, action: #selector(self.performAction(sender:)), for: UIControlEvents.touchUpInside)
        self.LogoutButton.addTarget(self, action: #selector(self.performAction(sender:)), for: UIControlEvents.touchUpInside)
        self.profileButton.addTarget(self, action: #selector(self.performAction(sender:)), for: UIControlEvents.touchUpInside)
        
        userImage.layer.shadowRadius = userImage.frame.height/2
        userImage.layer.masksToBounds = true
        
        userImage.layer.masksToBounds = true
        userImage.layer.borderWidth = 2
        userImage.layer.borderColor = UIColor.white.cgColor
        
        userImage.layer.shadowRadius = 10
        userImage.layer.shadowOffset = CGSize(width: 0, height: 0)
        userImage.layer.shadowColor = UIColor.black.cgColor
        userImage.layer.shadowOpacity = 0.3;
        
        if profileJson["PHOTO"].stringValue != "" && profileJson["PHOTO"].stringValue != "0" {
            if  let imageData = Data(base64Encoded: profileJson["PHOTO"].stringValue , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                userImage.image = DecodedImage
            }
        }else{
            userImage.image = UIImage(named: "user.png")
            
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func performAction(sender:UIButton) {
        
        if sender.tag == 1 {
            let storyBoard = UIStoryboard(name: "Main", bundle: nil) as UIStoryboard
            let mfSideMenuContainer = storyBoard.instantiateViewController(withIdentifier: "MFSideMenuContainerViewController") as! MFSideMenuContainerViewController
            let dashboard = storyBoard.instantiateViewController(withIdentifier: "Directory_ViewController") as! UITabBarController
            let leftSideMenuController = storyBoard.instantiateViewController(withIdentifier: "SideMenuViewController") as! SideMenuViewController
            mfSideMenuContainer.leftMenuViewController = leftSideMenuController
            mfSideMenuContainer.centerViewController = dashboard
            let appDelegate  = UIApplication.shared.delegate as! AppDelegate
            appDelegate.window?.rootViewController = mfSideMenuContainer
        
        }else if sender.tag == 2 {
            let center = storyboard?.instantiateViewController(withIdentifier: "SearchDirectoryViewController") as! SearchDirectoryViewController
            let nav = UINavigationController(rootViewController: center)
            self.menuContainerViewController.centerViewController = nav
        
        }else if sender.tag == 3 {
            let center = storyboard?.instantiateViewController(withIdentifier: "CreateDirectoryViewController") as! CreateDirectoryViewController
            let nav = UINavigationController(rootViewController: center)
            self.menuContainerViewController.centerViewController = nav
            
        }else if sender.tag == 4 {
            
            getContactDetails { (response) in
                
                DispatchQueue.main.async(execute: {
                    for i in 0 ..< phoneBook.count {
                        self.dataDict.updateValue(phoneBook[i].phone, forKey: phoneBook[i].name)
                    }
                    self.syncContacts()
                })
                
                
            }
            
        }else if sender.tag == 5 {
            
            
            let title = "Are you sure you want to logout?"
            
            let refreshAlert = UIAlertController(title: "Alert", message: title, preferredStyle: UIAlertControllerStyle.alert)
            
            refreshAlert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { (action: UIAlertAction!) in
                
                    self.deleteOffLineData()
                    UserDefaults.standard.removeObject(forKey: "profileJson")
                    UserDefaults.standard.removeObject(forKey: "name")
                    UserDefaults.standard.removeObject(forKey: "mobileNo")
                    UserDefaults.standard.removeObject(forKey: "uid")
                    UserDefaults.standard.setValue("No", forKey: "Login")
                    UserDefaults.standard.synchronize()
                    let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                    let nextViewController = storyBoard.instantiateViewController(withIdentifier: "ViewController") as! ViewController
                    self.present(nextViewController, animated:true, completion:nil)

        
            }))
            
            refreshAlert.addAction(UIAlertAction(title: "No", style: .default, handler: { (action: UIAlertAction!) in
                
            }))
            
            present(refreshAlert, animated: true, completion: nil)
            
            
        }else if sender.tag == 6 {
            let center = storyboard?.instantiateViewController(withIdentifier: "UserProfileViewController") as! UserProfileViewController
            let nav = UINavigationController(rootViewController: center)
            self.menuContainerViewController.centerViewController = nav
            
        }
        
        self.menuContainerViewController.toggleLeftSideMenuCompletion(nil)
        
    }
    
    func deleteOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM Directory "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            let querySQL1 = "DELETE FROM DirectoryMember "
            _ = contactDB!.executeUpdate(querySQL1, withArgumentsIn: nil)
            
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    @IBAction func syncContacts() {
        if phoneBook.count == 0 {
            return
        }else {
            self.sync()
        }
    }
    
    func sync(){
        
        
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dataDict, options: JSONSerialization.WritingOptions.prettyPrinted)
            let decoded = try JSONSerialization.jsonObject(with: jsonData, options: [])
            if let dictFromJSON = decoded as? [String:String] {
                
                let sendJson: [String: String] = [
                    "PFLAG": "1",
                    "PUID":userID,
                    "PBACKUPLIST" : String(describing: dictFromJSON)
                ]
                
                if Reachability.isConnectedToNetwork() {
                    startLoader(view: self.view)
                    DataProvider.sharedInstance.getServerData2(sendJson, path: "Mibackupsend", successBlock: { (response) in
                        stopLoader()
                        print(response)
                        
                    }) { (error) in
                        print(error)
                        stopLoader()
                    }
                }else{
                    self.showAlert("Alert", message: "No internet connectivity.")
                }
                
            }
        } catch let error as NSError {
            print(error)
        }
    }
    
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
