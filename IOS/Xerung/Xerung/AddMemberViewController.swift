//
//  AddMemberViewController.swift
//  Xerung
//
//  Created by mityung on 07/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class AddMemberViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , UITextFieldDelegate , DropDownViewControllerDelegate , UIPopoverPresentationControllerDelegate{

    @IBOutlet weak var tableView: UITableView!
    var playerIndex = [Int]()
    var groupId:String!
    
    var countryName = [String]()
    var countryCode = [String]()
    var countryPhoneCode = [String]()
    
    var tmpTextField:UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Add Member"
        self.navigationItem.setRightBarButton(UIBarButtonItem(barButtonSystemItem: .done , target: self, action: #selector(self.saveData)), animated: true)
        print(groupId)
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        //tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
        self.getOffLineData()

        getContactDetails { (response) in
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
            })
        }

    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return phoneBook.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "AddMemberCell2", for: indexPath) as! AddMemberCell2
        
        cell.nameLabel.text = phoneBook[indexPath.row].name
        cell.numberLabel.text = phoneBook[indexPath.row].phone
        cell.countryText.text = phoneBook[indexPath.row].country
        cell.countryText.delegate = self
        cell.countryText.tag = indexPath.row
        
        if playerIndex.contains(indexPath.row) == true {
            cell.tickImage.image = UIImage(named: "checked")
        }else{
           cell.tickImage.image = UIImage(named: "uncheck")
        }
        
        cell.selectionStyle = .none
        cell.backgroundColor = UIColor.clear
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if checkNumber(phoneBook[indexPath.row].phone) == true {
            if playerIndex.contains(indexPath.row) == true  {
                playerIndex = playerIndex.filter{$0 != indexPath.row}
                tableView.reloadRows(at: [indexPath], with: .fade)
            }else{
                playerIndex.append(indexPath.row)
                tableView.reloadRows(at: [indexPath], with: .fade)
            }
        }else{
            self.showAlert("Alert", message: "Please select valid contact number.")
        }
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func checkNumber(_ number:String) -> Bool {
        let num = number.removingWhitespaces()
        if num.characters.count > 9 &&  num.characters.count < 14 {
            return true
        }else {
            return false
        }
    }
    
    func saveData() {

        
        if playerIndex.count == 0 {
            self.showAlert("Alert", message: "Please select the contacts.")
            return
        }
        
        var  str = ""
        for i in 0 ..< playerIndex.count {
            str  = str + phoneBook[playerIndex[i]].name + ":" + self.getNumber(phoneBook[playerIndex[i]].phone , country: phoneBook[playerIndex[i]].country) + ":"
        }
        
        let count = playerIndex.count * 2 + 1
        str = String(count) + ":" + str

        
        let sendJson: [String: String] = [
            "PSENDBYNO":mobileNo,
            "PSENDTONO":str,
            "PGROUPID":groupId,
            "PINVITATIONFLAGID":"1"
        ]
        print(str)
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData(sendJson, path: "sendINV", successBlock: { (response) in
                print(response)
                stopLoader()
                if response["STATUS"] == "SUCCESS" {
                    self.sendNotification()
                    self.showAlert("Confirmation", message: "Memeber added successfully.")
                }
                
                }) { (error) in
                    print(error)
                    stopLoader()
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func sendNotification() {
        let sendJson: [String: String] = [
            "PPHONENUMBER": mobileNo,
            "PFLAG": "1",
            "PPHONETYPE": "0" ,
            ]
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData(sendJson, path: "sendNotiMSG", successBlock: { (response) in
                
                print(response)
                
                stopLoader()
                
            }) { (error) in
                print(error)
                stopLoader()
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
        
    }
    
    func getNumber(_ str:String ,country:String ) -> String {
        let str1 = str.removingWhitespaces()
        let temp = countryName.index(of: country)
        if countryPhoneCode[temp!] == "91" {
            return "+91" + String(str1.characters.suffix(10))
        }else{
            return countryPhoneCode[temp!] + String(str1.characters.suffix(10))
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

    
    // when click on textfield show the popup for select the subject
    func dropDown(_ textField:UITextField ) {
        
        let selector =  countryName
        
        let popoverContent = (self.storyboard?.instantiateViewController(withIdentifier: "DropDownViewController"))! as!        DropDownViewController
        popoverContent.delegate = self
        popoverContent.data = selector
        let nav = UINavigationController(rootViewController: popoverContent)
        nav.modalPresentationStyle = UIModalPresentationStyle.popover
        let popover = nav.popoverPresentationController
        popoverContent.preferredContentSize = CGSize(width: 300,height: 265)
        
        popover!.permittedArrowDirections = .any
        popover!.delegate = self
        popover!.sourceView = textField
        popover!.sourceRect = CGRect(x: textField.frame.width/3, y: 20, width: 0, height: 0)
        self.present(nav, animated: true, completion: nil)
        tmpTextField = textField
    }
    
    func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle{
        return UIModalPresentationStyle.none
    }
    
    func saveString(_ strText: NSString) {
        tmpTextField.text = strText as String
        phoneBook[tmpTextField.tag].country = strText as String
        
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
            self.dropDown(textField)
            return false
    }
    
    func gradient() {
        let gradient: CAGradientLayer = CAGradientLayer()
        gradient.frame = self.view.bounds
        gradient.colors = [UIColor(red: 26.0/255.0, green: 159.0/255.0, blue: 154.0/255.0, alpha: 0.2).cgColor,  UIColor(red: 26.0/255.0, green: 159.0/255.0, blue: 154.0/255.0, alpha: 0.5).cgColor , UIColor(red: 26.0/255.0, green: 159.0/255.0, blue: 154.0/255.0, alpha: 0.9).cgColor ]
        gradient.startPoint = CGPoint(x: 0, y: 0)
        gradient.endPoint = CGPoint(x: 1, y: 1)
        self.view.layer.insertSublayer(gradient, at: 0)
    }
    
    
    func getOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM Country "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            self.countryName = []
            self.countryCode = []
            self.countryPhoneCode = []
            
            while((results?.next()) == true){
                self.countryName.append(results!.string(forColumn: "countryName")!)
                self.countryCode.append(results!.string(forColumn: "countryCodeId")!)
                self.countryPhoneCode.append(results!.string(forColumn: "phoneCountryCode")!)
            }
            
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
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
