//
//  ProfileViewController.swift
//  Xerung
//
//  Created by mityung on 22/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import Contacts

class ProfileViewController: UIViewController , UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var tableView: UITableView!
    var pid = ""
    var data = [String]()
    var labelArray = [String]()
    var photo:String!
    var adminFlag = ""
    var mobileNumber:String!
    var admin = 0
    var number:String!
    var name:String!
    var phoneNumber:String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        photo = ""
        
        self.title = "Member Profile"
        labelArray = ["User Email","Alternate Number","Blood Group","Company Name","Profession","Address","City","Country"]
        
       self.getData()
        
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        self.tableView.separatorStyle = .none
        
      
        //tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        
        if selectedAdminFlag == "1" && self.pid != userID && Reachability.isConnectedToNetwork() {
            
            self.navigationItem.rightBarButtonItems = [UIBarButtonItem(barButtonSystemItem: .trash, target: self, action: #selector(self.deleteMember)),UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(self.addContact))]
        }else{
            if Reachability.isConnectedToNetwork() {
                navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(self.addContact))
            }
        }
        
    }

    func deleteMember(){
        
        let refreshAlert = UIAlertController(title: "Alert", message: "Are you sure you want to delete this member.", preferredStyle: UIAlertControllerStyle.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            if self.number == mobileNo {
                self.showAlert("Alert", message: "You can not delete this member.")
                self.tableView.reloadData()
                return
            }
            
            
            
            
            if selectedAdminFlag == "1" {
                
                let sendJson: [String: String] = [
                    "PUID": userID,
                    "PGROUPID":selectedGroupId,
                    "PPHONENUMBER": self.number
                ]
                
                if Reachability.isConnectedToNetwork() {
                    startLoader(view: self.view)
                    DataProvider.sharedInstance.getServerData2(sendJson, path: "DeleteMember", successBlock: { (response) in
                        
                        stopLoader()
                        
                        if response["STATUS"].stringValue == "SUCCESS" {
                            self.showAlert("Confirmation", message: "Member deleted Successfully.")
                        }
                        
                        DispatchQueue.main.async(execute: {
                            self.tableView.reloadData()
                        })
                    }, errorBlock: { (error) in
                        print(error)
                        stopLoader()
                    })
                    
                }else{
                    self.showAlert("Alert", message: "No internet connectivity.")
                }
            }

            
            
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { (action: UIAlertAction!) in
            
            
            
        }))
        
        present(refreshAlert, animated: true, completion: nil)
        
    }
    
    
    func addContact()  {
        
        let refreshAlert = UIAlertController(title: "Confirmation", message: "Are you sure you want to add this contact.", preferredStyle: UIAlertControllerStyle.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            
            
                getContactDetails { (response) in
                    DispatchQueue.main.async(execute: {
                        var flag = "1"
                            for k in 0 ..< phoneBook.count {
                                if phoneBook[k].phone.contains(String(self.number.characters.suffix(10))) == true {
                                    flag = "0"
                                    break
                                }
                            }
                        if flag == "1" {
                            let newContact = CNMutableContact()
                            newContact.givenName = self.name
                            newContact.jobTitle = self.data[3]
                            
                            let workEmail = CNLabeledValue(label:CNLabelWork, value:self.data[0] as NSString)
                            newContact.emailAddresses = [workEmail]
                            newContact.phoneNumbers = [CNLabeledValue(
                                label:CNLabelPhoneNumberiPhone,
                                value:CNPhoneNumber(stringValue:self.number))]
                            do {
                                let saveRequest = CNSaveRequest()
                                saveRequest.add(newContact, toContainerWithIdentifier: nil)
                                try AppDelegate.getAppDelegate().contactStore.execute(saveRequest)
                                
                            }
                            catch {
                                AppDelegate.getAppDelegate().showMessage("Unable to save the new contact.")
                            }
                        
                        }else{
                            self.showAlert("Alert", message: "Contact number already exist.")
                        
                        }
                        
                    })
                }
            
            
            
           
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { (action: UIAlertAction!) in
            
            
            
        }))
        
        present(refreshAlert, animated: true, completion: nil)
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
        
    }
    
  
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "profileCell", for: indexPath) as! profileCell
        if data[indexPath.row] == "" {
            cell.contentLabel.text = "N/A"
        }else{
            cell.contentLabel.text = data[indexPath.row].uppercaseFirst
        }
        
        cell.dataLabel.text = labelArray[indexPath.row]
        cell.selectionStyle = .none
        cell.backgroundColor = UIColor.clear
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        
        let sendJson: [String: String] = [
            "PUID": self.pid
        ]
            startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData2(sendJson, path: "fetchProfile", successBlock: { (response) in
           
          
            self.name = self.showData(response["NAME"].stringValue)
            self.number = self.showData(response["PHONENUMBER"].stringValue)
            
            self.data = [
                self.showData(response["EMAIL"].stringValue),
                self.showData(response["ALTERNATENO"].stringValue),
                self.showData(response["BLOODGROUP"].stringValue),
                self.showData(response["COMPANYNAME"].stringValue),
                self.showData(response["PROFESSION"].stringValue),
                self.showData(response["ADDRESS"].stringValue),
                self.showData(response["CITYNAME"].stringValue),
                self.showData(response["COUNTRYNAME"].stringValue)
            ]
            self.mobileNumber = response["PHONENUMBER"].stringValue
            self.photo = response["PHOTO"].stringValue
            
            
            
            /*let cell = self.tableView.dequeueReusableCell(withIdentifier: "ProfileHeaderCell") as! ProfileHeaderCell
            
            cell.makeadminButton.addTarget(self, action: #selector(self.makeAdmin(_:)), for: UIControlEvents.touchUpInside)
            
            if self.adminFlag == "1" {
                cell.makeadminButton.isHidden = true
            }else {
                if self.admin == 1 {
                    cell.makeadminButton.isHidden = false
                }else {
                    cell.makeadminButton.isHidden = true
                }
            }
            
            cell.userImage.layer.cornerRadius = cell.userImage.frame.width/2
            cell.userImage.layer.masksToBounds = true
            cell.userImage.layer.borderWidth = 1
            cell.userImage.layer.borderColor = themeColor.cgColor
            if self.photo != "" {
                if  let imageData = Data(base64Encoded: self.photo , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                    let DecodedImage = UIImage(data: imageData)
                    cell.userImage.image = DecodedImage
                }
            }else{
                cell.userImage.image = UIImage(named: "user.png")
                
            }
            
            self.tableView.tableHeaderView = cell*/
            stopLoader()
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
            })
        }) { (error) in
            print(error)
            stopLoader()
        }
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func showData(_ str:String) -> String {
        if str == "0"{
            return ""
        }else{
            return str
        }
    }
    
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileHeaderCell") as! ProfileHeaderCell
        
        cell.makeadminButton.addTarget(self, action: #selector(self.makeAdmin(_:)), for: UIControlEvents.touchUpInside)
        if adminFlag == "1" {
            cell.makeadminButton.isHidden = true
        }else {
            if admin == 1 {
                cell.makeadminButton.isHidden = false
            }else {
                cell.makeadminButton.isHidden = true
            }
        }
        
        cell.nameLabel.text = self.name
        cell.numberLabel.text = self.number
        
        cell.userImage.layer.cornerRadius = cell.userImage.frame.width/2
        cell.userImage.layer.masksToBounds = true
        cell.userImage.layer.borderWidth = 1
        cell.userImage.layer.borderColor = themeColor.cgColor
        if photo != "" {
            if  let imageData = Data(base64Encoded: photo , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                cell.userImage.image = DecodedImage
            }
        }else{
             cell.userImage.image = UIImage(named: "defaultDirectory")
        
        }
        
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 100
    }
    
    func makeAdmin(_ sender:UIButton) {
        
        if Reachability.isConnectedToNetwork() {
        
        let sendJson: [String: String] = [
            "PPHONENUMBER":mobileNumber,
            "PPHOTO":"",
            "PFLAG":"2"
        ]
        startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData(sendJson, path: "updatePhotoandAccess", successBlock: { (response) in
            stopLoader()
            print(response)
            
            if response["STATUS"].stringValue == "SUCCESS" {
                self.showAlert("Confirmation", message: "Make Admin Successfully.")
            }
            
            }) { (error) in
              stopLoader()
        }
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            
            if title == "Confirmation" {
                self.navigationController?.popToRootViewController(animated: true)
            }
            
        }))
        present(refreshAlert, animated: true, completion: nil)
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
