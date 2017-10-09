//
//  RequestViewController.swift
//  Xerung
//
//  Created by mityung on 03/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class RequestViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{
    
    struct request {
        var INVITATIONSENDBY = ""
        var GROUPID = ""
        var INVITATIONFLAGID = ""
        var NAME = ""
        var GROUPNAME = ""
    }
    
    var data = [request]()

    var label:UILabel!
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()

      //  tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
       self.title = "Request"
        self.getData()
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "RequestCell", for: indexPath) as! RequestCell
        cell.contactNumber.text = data[indexPath.row].INVITATIONSENDBY
        cell.GroupNameText.text = data[indexPath.row].GROUPNAME
        cell.nameText.text = data[indexPath.row].NAME
        cell.acceptButton.tag = indexPath.row
        cell.acceptButton.addTarget(self, action: #selector(self.acceptRequest(sender:)), for: UIControlEvents.touchUpInside)
        
        cell.backgroundColor = UIColor.clear
        return cell
    }
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        
        let sendJson: [String: String] = [
            "PUID":userID,
            "PGROUPID":"0",
            "PCOUNTRYCODEID":CountryCode,
            "PFLAG":"2",
            "PSTATUSID":"1",
            "ADMINFLAG":"0"
        ]
            startLoader(view: self.view)
        
        DataProvider.sharedInstance.getServerData2(sendJson, path: "fetchINV", successBlock: { (response) in
            print(response)
            print(response.count)
           
            for i in 0 ..< response.count - 1 {
                if response[i]["GROUPID"].stringValue != "" {
                    self.data.append(request.init(
                        INVITATIONSENDBY: response[i]["INVITATIONSENDBY"].stringValue ,
                        GROUPID: response[i]["GROUPID"].stringValue,
                        INVITATIONFLAGID: response[i]["INVITATIONFLAGID"].stringValue,
                        NAME: response[i]["NAME"].stringValue,
                        GROUPNAME: response[i]["GROUPNAME"].stringValue
                        ))
                }
            }
            
            if response[0]["GROUPID"].stringValue == "" {
                self.showAlert("Alert", message: "No record found.")
               /* let screenSize: CGRect = UIScreen.main.bounds
                let totalWidth = screenSize.width
                let totalheight = screenSize.height
                
               /* et label = UILabel(frame: CGRect(x: 0, y: 0, width: 200, height: 21))
                label.center = CGPoint(x: 160, y: 285)
                label.textAlignment = .center
                label.text = "I'am a test label"*/
                
                self.label = UILabel(frame: CGRect(x: 0,y: 0, width: 300,height: 50))
                self.label.textAlignment = NSTextAlignment.center
                self.label.center = CGPoint(x: 160, y: 285)
                
                self.label.text = "You don’t have any clinical history."
                self.label.numberOfLines = 0
                self.label.textColor = headingTextColor
                DispatchQueue.main.async(execute: {
                    self.view.addSubview(self.label)
                })

                
                self.label.isHidden = false
                self.tableView.isHidden = true*/
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
    
    
    func acceptRequest(sender:UIButton){
        var msg:String = "";
        
        if data[sender.tag].INVITATIONFLAGID == "1"{
            msg = "Do you want to add yourself in directory known as "+self.data[sender.tag].GROUPNAME+".";
        }else{
            msg = "Do you want to add "+data[sender.tag].NAME+" in directory known as "+self.data[sender.tag].GROUPNAME+".";
        }
        
        let refreshAlert = UIAlertController(title: "Request received", message: msg, preferredStyle: .actionSheet )
        
        refreshAlert.addAction(UIAlertAction(title: "Accept", style: .default , handler: { (action: UIAlertAction!) in
            self.AcceptRequest(self.data[sender.tag].GROUPID, sendBy: self.data[sender.tag].INVITATIONSENDBY, statusId: "4")
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Reject", style: .default , handler: { (action: UIAlertAction!) in
            self.AcceptRequest(self.data[sender.tag].GROUPID, sendBy: self.data[sender.tag].INVITATIONSENDBY, statusId: "0")
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel , handler: { (action: UIAlertAction!) in
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }
    

    func AcceptRequest(_ groupId:String,sendBy:String,statusId:String){
    
        let sendJson: [String: String] = [
            "PPHONENUMBER":mobileNo + "#"+sendBy,
            "PGROUPID":groupId,
            "PCOUNTRYCODEID":CountryPhoneCode,
            "PFLAG":"1",
            "PSTATUSID":statusId
        ]
        
        if Reachability.isConnectedToNetwork() {
        startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData(sendJson, path: "acceptINV", successBlock: { (response) in
            print(response)
            if response[].stringValue == "" {
                    self.showAlert("Confirmation", message: "Request accepted Successfully.")
            }
            stopLoader()
            }) { (error) in
                stopLoader()
                print(error)
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
