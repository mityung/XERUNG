//
//  PublicMemberViewController.swift
//  Xerung
//
//  Created by mityung on 16/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class PublicMemberViewController: UIViewController,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    var groupID:String!
    var groupName:String!
    
    struct address {
        var name = ""
        var phone = ""
        var address = ""
        var cityName = ""
    }
    
    var data = [address]()
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = groupName
        // Do any additional setup after loading the view.
        
        tableView.separatorStyle = .none
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "PublicMemberCell", for: indexPath) as! PublicMemberCell
        cell.nameLabel.text = data[indexPath.row].name
        cell.contactLabel.text = data[indexPath.row].phone
        cell.addressLabel.text = data[indexPath.row].address
        cell.placeLabel.text = data[indexPath.row].cityName
        cell.selectionStyle = .none
        return cell
        
    }
    
    
    
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        let sendJson: [String: String] = [
            "PGROUPID": groupID,
            "PSERACHTEXT":"",
            "PGROUPACCESSTYPE":"2"
        ]
            startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData2(sendJson, path: "GroupSearch", successBlock: { (response) in
            print(response)
        
            for i in 0 ..< response.count {
                var number = ""
                if response[i]["PHONENUMBER"].stringValue == "" {
                    number = "Not Available."
                }else{
                     number = response[i]["PHONENUMBER"].stringValue
                }
                self.data.append(
                    address.init(
                        name: response[i]["NAME"].stringValue,
                        phone: number,
                        address: response[i]["ADDRESS"].stringValue,
                        cityName: response[i]["CITYNAME"].stringValue)
                    )
            }
            stopLoader()
            self.saveDataOffLine()
        
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
                stopLoader()
            })
        }) { (error) in
            print(error)
        }
        } else {
            self.showAlert("Alert", message: "No internet connectivity.")
            self.getOffLineData()
            self.tableView.reloadData()
        
        }
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    // save data into sqlite
    func saveDataOffLine(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM PublicDirectoryMember where groupId = '\(groupID)' "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< data.count {
                let insertSQL = "INSERT INTO PublicDirectoryMember (name ,phone , address, cityName ,groupId) VALUES ('\(data[i].name)', '\(data[i].phone)','\(data[i].address)', '\(data[i].cityName)', '\(groupID)')"
                let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                
                if !result! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
            }
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    // get data from sqlite
    func getOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        self.data = []
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM PublicDirectoryMember where groupId = '\(groupID)' "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
           
            while((results?.next()) == true){
                self.data.append(address.init(
                    name: results!.string(forColumn: "name")!,
                    phone: results!.string(forColumn: "phone")!,
                    address: results!.string(forColumn: "address")!,
                    cityName: results!.string(forColumn: "cityName")!
                    ))
            }
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
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
