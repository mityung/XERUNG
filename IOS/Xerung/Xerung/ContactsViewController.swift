//
//  ContactsViewController.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class ContactsViewController: UIViewController, UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    var name = [String]()
    var dataDict = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "All Contacts"
       // tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none

        // Do any additional setup after loading the view.
        getContactDetails { (response) in
            DispatchQueue.main.async(execute: {
               
                self.tableView.reloadData()
                for i in 0 ..< phoneBook.count {
                    self.dataDict.updateValue(phoneBook[i].phone, forKey: phoneBook[i].name)
                }
        })
        }
        
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBAction func sideMenu(_ sender: Any) {
        self.menuContainerViewController.toggleLeftSideMenuCompletion { () -> Void in
            
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return phoneBook.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ContactViewCell", for: indexPath) as! ContactViewCell
        
        let temp = phoneBook[indexPath.row].name
        let index = temp.characters.index(temp.startIndex, offsetBy: 0)
         cell.firstLetterLabel.text = String(temp[index])
        
        cell.firstLetterLabel.textColor = UIColor.white
        cell.firstLetterLabel.backgroundColor = UIColor.randomColor()
        
        cell.nameLabel.text = phoneBook[indexPath.row].name
        cell.numberLabel.text = phoneBook[indexPath.row].phone
        cell.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.2)
        cell.callButton.tag = indexPath.row
        cell.callButton.addTarget(self, action: #selector(self.call(sender:)), for: UIControlEvents.touchUpInside)
        cell.selectionStyle = .none
        
        return cell
    }
    
    
    func call(sender:UIButton) {
       
        if let url = URL(string: "tel://\(phoneBook[sender.tag].phone.removingWhitespaces())"), UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.openURL(url)
        }
    }
    
    
    
    @IBAction func syncContacts(_ sender: AnyObject) {
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
                    showAlert("Alert", message: "No internet connectivity.")
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
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
