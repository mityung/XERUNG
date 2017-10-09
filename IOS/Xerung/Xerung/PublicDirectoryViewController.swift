//
//  PublicDirectoryViewController.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class PublicDirectoryViewController: UIViewController, UITableViewDelegate,UITableViewDataSource , UISearchBarDelegate {
    @IBOutlet weak var tableView: UITableView!

    struct publicDirectory {
        var TAGNAME = ""
        var DESCRITION = ""
        var GROUPACCESSTYPE = ""
        var GROUPNAME = ""
        var GROUPPHOTO = ""
        var GROUPID = ""
    }
    var information = [publicDirectory]()
    var groupId:String!
    var groupName:String!
    
    var searchBar = UISearchBar()
    var searchBarButtonItem: UIBarButtonItem?
    var logoImageView   : UIImageView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "Public Directory"
        
        // Do any additional setup after loading the view.
        self.getData()
        
        
        tableView.separatorStyle = .none
        searchBar.delegate = self
        searchBar.searchBarStyle = UISearchBarStyle.minimal
        
        //searchBarButtonItem = navigationItem.rightBarButtonItem
        searchBar.showsCancelButton = true
        searchBar.placeholder = "Search Directories..."
        searchBar.setTextColor(color: .white)
        searchBar.setTextFieldColor(color: UIColor.clear)
        searchBar.setPlaceholderTextColor(color: .white)
        searchBar.setSearchImageColor(color: .white)
        searchBar.setTextFieldClearButtonColor(color: .white)

        let imgClear = UIImage(named: "close")
        searchBar.setImage(imgClear, for: UISearchBarIcon.clear, state: .normal)
        
        //tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        
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
        return information.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PublicDirectoryCell", for: indexPath) as! PublicDirectoryCell
        cell.directoryName.text = information[indexPath.row].GROUPNAME
        cell.directoryService.text = information[indexPath.row].DESCRITION
        cell.directorylist.text = information[indexPath.row].TAGNAME
        
        if information[indexPath.row].GROUPPHOTO != "" {
            if  let imageData = Data(base64Encoded: information[indexPath.row].GROUPPHOTO , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                cell.publicDirectoryImage.image = DecodedImage
                
            }
        }else{
                cell.publicDirectoryImage.image = UIImage(named: "defaultDirectory")
        }
        cell.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.2)
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        groupId = information[indexPath.row].GROUPID
        groupName = information[indexPath.row].GROUPNAME
        
        if information[indexPath.row].GROUPACCESSTYPE == "2" {
            performSegue(withIdentifier: "publicMember", sender: nil)
        }else{
            let nextView = storyboard?.instantiateViewController(withIdentifier: "SendRequestViewController") as! SendRequestViewController
            nextView.groupId = groupId
            
            self.navigationController?.pushViewController(nextView, animated: true)
        
        }
    }
    
    
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        
        let sendJson: [String: String] = [
            "PGROUPID": "0",
            "PSERACHTEXT":"",
            "PGROUPACCESSTYPE":"2"
        ]
            
            startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData2(sendJson, path: "GroupSearch", successBlock: { (response) in
            
            self.information = []
           
            for i in 0 ..< response.count {
               self.information.append(publicDirectory.init(
                TAGNAME: response[i]["TAGNAME"].stringValue,
                DESCRITION: response[i]["DESCRITION"].stringValue,
                GROUPACCESSTYPE: response[i]["GROUPACCESSTYPE"].stringValue,
                GROUPNAME: response[i]["GROUPNAME"].stringValue,
                GROUPPHOTO: response[i]["GROUPPHOTO"].stringValue,
                GROUPID: response[i]["GROUPID"].stringValue))
            }
            self.saveDataOffLine()
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
            
            self.getOffLineData()
            self.tableView.reloadData()
        }
    }
    
    @IBAction func searchButtonPressed(_ sender: AnyObject) {
        showSearchBar()
    }
    
    
    func showSearchBar() {
        searchBar.alpha = 0
        navigationItem.titleView = searchBar
        navigationItem.setLeftBarButton(nil, animated: true)
        UIView.animate(withDuration: 0.5, animations: {
            self.searchBar.alpha = 1
            }, completion: { finished in
                self.searchBar.becomeFirstResponder()
        })
    }
    
    func hideSearchBar() {
        navigationItem.setLeftBarButton(searchBarButtonItem, animated: true)
       searchBar.text = ""
        UIView.animate(withDuration: 0.3, animations: {
            self.navigationItem.titleView = self.logoImageView
            
            }, completion: { finished in
                
        })
    }
    
    
    //MARK: UISearchBarDelegate
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        hideSearchBar()
        self.getData()
    }

    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.endEditing(true)
        
        if (searchBar.text?.characters.count)! < 3 {
            self.showAlert("Alert", message: "Please enter minimum three characters.")
            return
        }
        
        let sendJson: [String: String] = [
            "PGROUPID": "0",
            "PSERACHTEXT":searchBar.text!,
            "PGROUPACCESSTYPE":"0"
        ]
        
        print(sendJson)
        
        if Reachability.isConnectedToNetwork() {
        
        DataProvider.sharedInstance.getServerData2(sendJson, path: "GroupSearch", successBlock: { (response) in
            startLoader(view: self.view)
            self.information = []
            for i in 0 ..< response.count {
                self.information.append(publicDirectory.init(
                    TAGNAME: response[i]["TAGNAME"].stringValue,
                    DESCRITION: response[i]["DESCRITION"].stringValue,
                    GROUPACCESSTYPE: response[i]["GROUPACCESSTYPE"].stringValue,
                    GROUPNAME: response[i]["GROUPNAME"].stringValue,
                    GROUPPHOTO: response[i]["GROUPPHOTO"].stringValue,
                    GROUPID: response[i]["GROUPID"].stringValue))
            }
            
            if response.count == 0 {
                self.showAlert("Alert", message: "No record found.")
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
    
    
    // save data into sqlite
    func saveDataOffLine(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM PublicDirectory "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< information.count {
                let insertSQL = "INSERT INTO PublicDirectory (TAGNAME ,DESCRITION , GROUPACCESSTYPE, GROUPNAME , GROUPPHOTO,GROUPID) VALUES ('\(information[i].TAGNAME)', '\(information[i].DESCRITION)','\(information[i].GROUPACCESSTYPE)', '\(information[i].GROUPNAME)', '\(information[i].GROUPPHOTO)','\(information[i].GROUPID)')"
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
        self.information = []
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM PublicDirectory "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            while((results?.next()) == true){
                self.information.append(publicDirectory.init(
                    TAGNAME:results!.string(forColumn: "TAGNAME")! ,
                    DESCRITION: results!.string(forColumn: "DESCRITION")!,
                    GROUPACCESSTYPE: results!.string(forColumn: "GROUPACCESSTYPE")!,
                    GROUPNAME: results!.string(forColumn: "GROUPNAME")!,
                    GROUPPHOTO: results!.string(forColumn: "GROUPPHOTO")!,
                    GROUPID: results!.string(forColumn: "GROUPID")!))
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
    
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
        if segue.identifier == "publicMember" {
            let detailVC = segue.destination as! PublicMemberViewController
            detailVC.groupID = groupId
            detailVC.groupName = groupName
        }
    }
    

}
