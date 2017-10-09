//
//  SendRequestViewController.swift
//  Xerung
//
//  Created by mityung on 08/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class SendRequestViewController: UIViewController , UITableViewDelegate,UITableViewDataSource {

    var name = [String]()
    var number = [String]()
    
    var groupId:String!
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Group Admin"
       // tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
        self.getData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return name.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SendRequestCell", for: indexPath) as! SendRequestCell
        cell.nameLabel.text = name[indexPath.row]
        cell.backgroundColor = UIColor.clear
        cell.sendButton.tag = indexPath.row
        cell.sendButton.addTarget(self, action: #selector(self.send(sender:)), for: UIControlEvents.touchUpInside)
        
        cell.selectionStyle  = .none
        return cell
    }
    
    
    func send(sender:UIButton) {
        let str = "3:" + name[sender.tag] + ":" + number[sender.tag]
        self.sendRequest(str)
    }
    
    
   
    
    func getData(){
        let sendJson: [String: String] = [
            "PGROUPID": groupId,
            "PSERACHTEXT":"",
            "PGROUPACCESSTYPE":"1"
        ]
        
        startLoader(view: self.view)
        if Reachability.isConnectedToNetwork() {
        
        DataProvider.sharedInstance.getServerData2(sendJson, path: "GroupSearch", successBlock: { (response) in
            print(response)
            
            for i in 0 ..< response.count {
                self.name.append(response[i]["NAME"].stringValue)
                self.number.append(response[i]["PHONENUMBER"].stringValue)
            
            }
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
    
    
    func sendRequest(_ str:String) {
        let sendJson: [String: String] = [
            "PSENDBYNO":mobileNo,
            "PSENDTONO":str,
            "PGROUPID":groupId,
            "PINVITATIONFLAGID":"2"
        ]
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData2(sendJson, path: "sendINV", successBlock: { (response) in
            print(response)
            stopLoader()
            if response["STATUS"].stringValue == "SUCCESS" {
               self.showAlert("Confirmation", message: "Request sent Successfully.")
            }
            
            }) { (error) in
                print(error)
                stopLoader()
        }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
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
