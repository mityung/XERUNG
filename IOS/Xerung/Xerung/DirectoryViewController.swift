//
//  DirectoryViewController.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import SwiftyJSON

class DirectoryViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate ,UISearchBarDelegate{
   
    var groupId:String!
    struct directory {
        var MADEBYPHONENO = ""
        var MADEBYNAME = ""
        var GROUPID = ""
        var GROUPNAME = ""
        var TAGNAME = ""
        var DESCRITION = ""
        var GROUPPHOTO = ""
        var MEMBERCOUNT = ""
        var ADMINFLAG = ""
        var GROUPACCESSTYPE = ""
    }
    
    struct publicDirectory {
        var TAGNAME = ""
        var DESCRITION = ""
        var GROUPACCESSTYPE = ""
        var GROUPNAME = ""
        var GROUPPHOTO = ""
        var GROUPID = ""
    }
    var information = [publicDirectory]()
    
    var count = 1
    var searchFlag = 0
    var searchBar = UISearchBar()
    var searchBarButtonItem: UIBarButtonItem?
    var logoImageView   : UIImageView!
    
     var searchController : UISearchController!
    var data = [directory]()

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var createDirectoryButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "Private Directory"
        print(CountryPhoneCode)
        createDirectoryButton.layer.borderWidth = 1
        createDirectoryButton.layer.borderColor = themeColor.cgColor
        createDirectoryButton.layer.cornerRadius = 5
        createDirectoryButton.layer.masksToBounds = true
        
        //self.tableView.hidden = true
       // tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        
       
        //tableView.tableFooterView = UIView(frame: CGRect.zero)
        
        let button =  UIButton(frame: CGRect(x: self.view.frame.width - 65 , y: self.view.frame.height - 115, width: 60, height: 60))
      //  button.setTitle("setting", forState: UIControlState.Normal)
        button.setImage(UIImage(named: "setting") , for: UIControlState())
        
        button.layer.cornerRadius = 30
        button.backgroundColor = themeColor
        button.layer.shadowColor =  UIColor(red: 0.0/255.0, green: 0.0/255.0, blue: 0.0/255.0, alpha: 0.4).cgColor
        button.layer.shadowOffset = CGSize(width: 0, height: 3.0);
        button.layer.shadowOpacity = 1.0;
        button.layer.shadowRadius = 10.0;
        button.addTarget(self, action: #selector(self.setting(_:)), for: UIControlEvents.touchUpInside)
       // self.view.addSubview(button)
        
        searchBar.delegate = self
        searchBar.searchBarStyle = UISearchBarStyle.minimal
        searchBar.showsCancelButton = true
        searchBar.placeholder = "Search Directories..."
        searchBar.setTextColor(color: .white)
        searchBar.setTextFieldColor(color: UIColor.clear)
        searchBar.setPlaceholderTextColor(color: .white)
        searchBar.setSearchImageColor(color: .white)
        searchBar.setTextFieldClearButtonColor(color: .white)
        
        //UIImage *imgClear = [UIImage imageNamed:@"clear"];
        //[mySearchBar setImage:imgClear forSearchBarIcon:UISearchBarIconClear state:UIControlStateNormal];
        
        let imgClear = UIImage(named: "close")
        searchBar.setImage(imgClear, for: UISearchBarIcon.clear, state: .normal)
        
        
        self.tabBarController?.tabBar.tintColor = themeColor
        
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
        
       
        
    }
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = false
        self.tabBarController?.tabBar.isHidden = false
        
        count = 1
        self.getData()
        
    }
    @IBAction func sideMenu(_ sender: Any) {
        self.menuContainerViewController.toggleLeftSideMenuCompletion { () -> Void in
            
        }
    }
    
        
    @IBAction func createDirectory() {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "CreateDirectoryViewController") as! CreateDirectoryViewController
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @IBAction func showRequest() {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "RequestViewController") as! RequestViewController
        self.navigationController?.pushViewController(viewController, animated: true)

    }
    
    @IBAction func notification(_ sender: Any) {
        
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "RequestViewController") as! RequestViewController
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if searchFlag == 0 {
            
            if directorCount > data.count {
                return data.count + 1
            }else{
                return data.count
            }

        }else{
            return information.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if searchFlag == 0 {
            
            if indexPath.row == data.count {
                let cell = UITableViewCell()
                cell.textLabel?.text = "Load More..."
                cell.textLabel?.textAlignment = .center
                cell.selectionStyle = .none
                return cell
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "DirectoryCell", for: indexPath) as! DirectoryCell
                cell.directoryName.text = data[indexPath.row].GROUPNAME.uppercaseFirst
                cell.directoryMember.text = data[indexPath.row].TAGNAME
                if data[indexPath.row].GROUPPHOTO != "" && data[indexPath.row].GROUPPHOTO != "0" {
                    if  let imageData = Data(base64Encoded: data[indexPath.row].GROUPPHOTO , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                        let DecodedImage = UIImage(data: imageData)
                        cell.directoryImage.image = DecodedImage
                    }
                }else{
                    cell.directoryImage.image = UIImage(named: "defaultDirectory")
                }
                cell.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.2)
                cell.selectionStyle = .none
                
                cell.countLabel.text = data[indexPath.row].MEMBERCOUNT
                if data[indexPath.row].ADMINFLAG == "1" {
                    cell.typeLabel.text = "Admin"
                }else{
                    cell.typeLabel.text = "Member"
                }
                cell.layer.transform = CATransform3DMakeScale(0.1, 0.1, 1)
                UIView.animate(withDuration: 0.3, animations: {
                    cell.layer.transform = CATransform3DMakeScale(1.05, 1.05, 1)
                },completion: { finished in
                    UIView.animate(withDuration: 0.1, animations: {
                        cell.layer.transform = CATransform3DMakeScale(1, 1, 1)
                    })
                })
                
                cell.backgroundColor = UIColor.clear
                
                return cell
            }
        }else{
            
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "PublicDirectoryCell", for: indexPath) as! PublicDirectoryCell
            cell.directoryName.text = information[indexPath.row].TAGNAME
            cell.directoryService.text = information[indexPath.row].DESCRITION
            cell.directorylist.text = information[indexPath.row].GROUPNAME
            if information[indexPath.row].GROUPPHOTO != "" && information[indexPath.row].GROUPPHOTO != "0"{
                if  let imageData = Data(base64Encoded: information[indexPath.row].GROUPPHOTO , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                    let DecodedImage = UIImage(data: imageData)
                    cell.publicDirectoryImage.image = DecodedImage
                    
                }
            }else{
                cell.publicDirectoryImage.image = UIImage(named: "user.png")
                
            }
            cell.selectionStyle = .none
            cell.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.2)
            return cell
            
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        searchBar.endEditing(true)
        if searchFlag == 0 {
            
            if indexPath.row == data.count {
                count = count + 1
                self.loadMore()
            
            }else {
                
                // groupId = data[indexPath.row].GROUPID
                selectedGroupId = data[indexPath.row].GROUPID
                selectedGroupName = data[indexPath.row].GROUPNAME
                selectedGroupAbout = data[indexPath.row].DESCRITION
                selectedGroupCalled = data[indexPath.row].TAGNAME
                selectedGroupPhoto = data[indexPath.row].GROUPPHOTO
                selectedAdminFlag = data[indexPath.row].ADMINFLAG
                selectedMemberCount = Int(data[indexPath.row].MEMBERCOUNT)!
            // performSegueWithIdentifier("memberDirectory", sender: nil)
            
                let nextView = storyboard?.instantiateViewController(withIdentifier: "DirectoryDetailViewController") as! DirectoryDetailViewController
                self.navigationController?.pushViewController(nextView, animated: true)
            }
        
        }else{
            
            groupId = information[indexPath.row].GROUPID
            if information[indexPath.row].GROUPACCESSTYPE == "2" {
                let nextView = storyboard?.instantiateViewController(withIdentifier: "PublicMemberViewController") as! PublicMemberViewController
                nextView.groupID = groupId
                self.navigationController?.pushViewController(nextView, animated: true)
            }else{
                let nextView = storyboard?.instantiateViewController(withIdentifier: "SendRequestViewController") as! SendRequestViewController
                nextView.groupId = groupId
                self.navigationController?.pushViewController(nextView, animated: true)
                
            }
        
        }
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        
            startLoader(view: self.view)
        let sendJson: [String: String] = [
            "PUID": userID,
            "PGROUPID":"0",
            "PTIMESTAMP":"",
            "PPAGENUMBER":"1",
            "PRECORDCOUNT":"10"
        ]
        DataProvider.sharedInstance.getServerData(sendJson, path: "FetchGroupDelta", successBlock: { (response) in
        
            self.data = []
            print(response)
            
        for i in 0 ..< response.count {
            self.data.append(directory.init(
                MADEBYPHONENO: response[i]["MADEBYPHONENO"].stringValue,
                MADEBYNAME: response[i]["MADEBYNAME"].stringValue,
                GROUPID: response[i]["GROUPID"].stringValue,
                GROUPNAME: response[i]["GROUPNAME"].stringValue,
                TAGNAME: response[i]["TAGNAME"].stringValue,
                DESCRITION: response[i]["DESCRITION"].stringValue,
                GROUPPHOTO: response[i]["GROUPPHOTO"].stringValue,
                MEMBERCOUNT: response[i]["MEMBERCOUNT"].stringValue,
                ADMINFLAG: response[i]["ADMINFLAG"].stringValue,
                GROUPACCESSTYPE: response[i]["GROUPACCESSTYPE"].stringValue
                ))
            }
            
            if response.count == 0 {
                
                    self.showAlert("Alert", message: "No record found.")
            
            }
            
            
            self.saveDataOffLine()
            
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
            })
            stopLoader()
            
             self.getNotificationCount()
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
    
    func loadMore() {
        
        if Reachability.isConnectedToNetwork() {
            
            startLoader(view: self.view)
            let sendJson: [String: String] = [
                "PUID": userID,
                "PGROUPID":"0",
                "PTIMESTAMP":"",
                "PPAGENUMBER": String(count),
                "PRECORDCOUNT":"10"
            ]
            DataProvider.sharedInstance.getServerData(sendJson, path: "FetchGroupDelta", successBlock: { (response) in
                
               
               
                
                for i in 0 ..< response.count {
                    self.data.append(directory.init(
                        MADEBYPHONENO: response[i]["MADEBYPHONENO"].stringValue,
                        MADEBYNAME: response[i]["MADEBYNAME"].stringValue,
                        GROUPID: response[i]["GROUPID"].stringValue,
                        GROUPNAME: response[i]["GROUPNAME"].stringValue,
                        TAGNAME: response[i]["TAGNAME"].stringValue,
                        DESCRITION: response[i]["DESCRITION"].stringValue,
                        GROUPPHOTO: response[i]["GROUPPHOTO"].stringValue,
                        MEMBERCOUNT: response[i]["MEMBERCOUNT"].stringValue,
                        ADMINFLAG: response[i]["ADMINFLAG"].stringValue,
                        GROUPACCESSTYPE: response[i]["GROUPACCESSTYPE"].stringValue
                    ))
                }
                
               
                self.saveDataOffLine()
                
                
                DispatchQueue.main.async(execute: {
                    self.tableView.reloadData()
                })
                stopLoader()
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
    
    @IBAction func showProfile() {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "UserProfileViewController") as! UserProfileViewController
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    
    func setting(_ sender:UIButton) {
        let refreshAlert = UIAlertController(title: "", message: "", preferredStyle: .actionSheet )
        
        refreshAlert.addAction(UIAlertAction(title: "Profile", style: .default , handler: { (action: UIAlertAction!) in
            self.showProfile()
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Request Notification", style: .default , handler: { (action: UIAlertAction!) in
            self.showRequest()
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Create Directory", style: .default , handler: { (action: UIAlertAction!) in
            self.createDirectory()
            
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel , handler: { (action: UIAlertAction!) in
            
            
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Logout", style: .default , handler: { (action: UIAlertAction!) in
            
            
            UserDefaults.standard.removeObject(forKey: "profileJson")
            UserDefaults.standard.removeObject(forKey: "name")
            UserDefaults.standard.removeObject(forKey: "mobileNo")
            UserDefaults.standard.removeObject(forKey: "uid")
            UserDefaults.standard.setValue("No", forKey: "Login")
            UserDefaults.standard.synchronize()
            
            let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
            let nextViewController = storyBoard.instantiateViewController(withIdentifier: "ViewController") as! ViewController
            self.present(nextViewController, animated:true, completion:nil)
            
          // self.dismiss(animated: true, completion: nil)
        
        }))
        
        present(refreshAlert, animated: true, completion: nil)
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
            self.searchBar.alpha = 1
            }, completion: { finished in
                
        })
    }
    
    
    //MARK: UISearchBarDelegate
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        hideSearchBar()
        searchFlag = 0
        count = 1
        getData()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        
        if (searchBar.text?.characters.count)! < 3 {
            self.showAlert("Alert", message: "Please enter minimum three characters.")
            return
        }
        searchFlag = 1
        
        searchBar.endEditing(true)
        let sendJson: [String: String] = [
            "PGROUPID": "0",
            "PSERACHTEXT":searchBar.text!,
            "PGROUPACCESSTYPE":"0"
        ]
        
        if Reachability.isConnectedToNetwork() {
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
            
            if response.count == 0 {
                self.showAlert("Alert", message: "No record found.")
            }
            
            stopLoader()
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
            })
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

    
    func saveDataOffLine(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
           
            
            for i in 0 ..< data.count {
                let findGroupQuery = "SELECT count(*) as record FROM Directory where GROUPID = '\(data[i].GROUPID)' "
                let results:FMResultSet? = contactDB?.executeQuery(findGroupQuery,withArgumentsIn: nil)
                
                var record = ""
                
                if((results?.next()) == true){
                    record = results!.string(forColumn: "record")!
                }
                
                if record == "1" {
                    
                    let updateStatementString = "UPDATE Directory SET MADEBYPHONENO = '\(data[i].MADEBYPHONENO)',MADEBYNAME = '\(data[i].MADEBYNAME)',GROUPNAME = '\(data[i].GROUPNAME)',TAGNAME = '\(data[i].TAGNAME)',DESCRITION = '\(data[i].DESCRITION)' ,GROUPPHOTO = '\(data[i].GROUPPHOTO)',MEMBERCOUNT = '\(data[i].MEMBERCOUNT)',ADMINFLAG = '\(data[i].ADMINFLAG)',GROUPACCESSTYPE = '\(data[i].GROUPACCESSTYPE)' WHERE GROUPID = '\(data[i].GROUPID)' "
                    let result = contactDB?.executeUpdate(updateStatementString , withArgumentsIn: nil)
                    
                    if !result! {
                        print("Error: \(contactDB?.lastErrorMessage())")
                    }
               
                }else{
                    let insertSQL = "INSERT INTO Directory (MADEBYPHONENO,MADEBYNAME,GROUPID,GROUPNAME,TAGNAME,DESCRITION,GROUPPHOTO,MEMBERCOUNT,ADMINFLAG,GROUPACCESSTYPE) VALUES ('\(data[i].MADEBYPHONENO)', '\(data[i].MADEBYNAME)','\(data[i].GROUPID)', '\(data[i].GROUPNAME)', '\(data[i].TAGNAME)','\(data[i].DESCRITION)', '\(data[i].GROUPPHOTO)','\(data[i].MEMBERCOUNT)', '\(data[i].ADMINFLAG)', '\(data[i].GROUPACCESSTYPE)')"
                    let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                    
                    if !result! {
                        print("Error: \(contactDB?.lastErrorMessage())")
                    }
                
                }
            }
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    // save data into sqlite
   /* func saveDataOffLine(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM Directory "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< data.count {
                let insertSQL = "INSERT INTO Directory (MADEBYPHONENO,MADEBYNAME,GROUPID,GROUPNAME,TAGNAME,DESCRITION,GROUPPHOTO,MEMBERCOUNT,ADMINFLAG,GROUPACCESSTYPE) VALUES ('\(data[i].MADEBYPHONENO)', '\(data[i].MADEBYNAME)','\(data[i].GROUPID)', '\(data[i].GROUPNAME)', '\(data[i].TAGNAME)','\(data[i].DESCRITION)', '\(data[i].GROUPPHOTO)','\(data[i].MEMBERCOUNT)', '\(data[i].ADMINFLAG)', '\(data[i].GROUPACCESSTYPE)')"
                let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                
                if !result! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
            }
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }*/
    
    
    // get data from sqlite
    func getOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        self.data = []
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM Directory "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            while((results?.next()) == true){
               self.data.append(directory.init(
                MADEBYPHONENO: results!.string(forColumn: "MADEBYPHONENO")!,
                MADEBYNAME: results!.string(forColumn: "MADEBYNAME")!,
                GROUPID: results!.string(forColumn: "GROUPID")!,
                GROUPNAME: results!.string(forColumn: "GROUPNAME")!,
                TAGNAME: results!.string(forColumn: "TAGNAME")!,
                DESCRITION: results!.string(forColumn: "DESCRITION")!,
                GROUPPHOTO: results!.string(forColumn: "GROUPPHOTO")!,
                MEMBERCOUNT: results!.string(forColumn: "MEMBERCOUNT")!,
                ADMINFLAG: results!.string(forColumn: "ADMINFLAG")!,
                GROUPACCESSTYPE: results!.string(forColumn: "GROUPACCESSTYPE")!))
                
            }
            
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    
    
    func getNotificationCount() {
        
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
                
                
               let count = response[response.count - 1]["COUNT"].stringValue
                
                if count != "" && count != "0" {
                    self.navigationItem.rightBarButtonItem!.badgeValue = count
                    
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
        
        if segue.identifier == "memberDirectory" {
            let detailVC = segue.destination as! DirectoryMemberViewController
            detailVC.groupId = groupId
        }
    }
 

}
