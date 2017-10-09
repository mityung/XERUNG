//
//  PendingMemberViewController.swift
//  Xerung
//
//  Created by mityung on 02/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class PendingMemberViewController: UIViewController, UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var tableView: UITableView!

    struct Invitation {
        var name = ""
        var number = ""
    }
    
    var data = [Invitation]()
    
    var groupId:String!
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "Pending Member"
        self.getData()
        
       // tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        
            startLoader(view: self.view)
        let sendJson: [String: String] = [
            "PUID": userID,
            "PGROUPID":groupId,
            "PCOUNTRYCODEID":CountryCode,
            "PFLAG":"1",
            "PSTATUSID":"1",
            "ADMINFLAG": "1"
        ]

        DataProvider.sharedInstance.getServerData2(sendJson, path: "fetchINV", successBlock: { (response) in
            print(response)
            
            if response.count != 0 {
                for i in 0 ..< response.count {
                    self.data.append(Invitation.init(name: "", number: response[i]["INVITATIONSENDTO"].stringValue))
                }
                self.getName()
            }else{
                self.showAlert("Alert", message: "No record found.")
            }
            stopLoader()
          
        }) { (error) in
            print(error)
            stopLoader()
        }
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func getName() {
        getContactDetails { (response) in
            DispatchQueue.main.async(execute: {
                for i in 0 ..< self.data.count {
                    for k in 0 ..< phoneBook.count {
                        if phoneBook[k].phone.contains(self.data[i].number) == true {
                            self.data[i].name = phoneBook[k].name
                        }else if self.getNumber(phoneBook[k].phone) ==  self.getNumber(self.data[i].number) {
                            self.data[i].name = phoneBook[k].name
                        }
                    }
                    if self.data[i].name == "" {
                        self.data[i].name = "Unknown"
                    }
                }
                self.tableView.reloadData()
                print(self.data)
            })
        }
    }
    
    func getNumber(_ str:String) -> String {
        let str1 = str.removingWhitespaces()
        print(str1)
        return  String(str1.characters.suffix(10))
        
    }
    

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PendingMemberCell", for: indexPath) as! PendingMemberCell
        
        cell.nameLabel.text = data[indexPath.row].name.uppercaseFirst
        cell.numberLabel.text = data[indexPath.row].number
        
        cell.backgroundColor = UIColor.clear
        cell.selectionStyle = .none
        return cell
    }
    
    
    // show alert using this method
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
