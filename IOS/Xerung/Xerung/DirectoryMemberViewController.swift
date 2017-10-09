//
//  DirectoryMemberViewController.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import XLPagerTabStrip
class DirectoryMemberViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , IndicatorInfoProvider {
    public func indicatorInfo(for pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
         return IndicatorInfo(title: "All User")
    }


    @IBOutlet weak var tableView: UITableView!
    var groupId:String!
    var data = [String]()
    var pid:String!
    var adminFlag:String!
    var admin = 0
    var count = 1
    
    let searchController = UISearchController(searchResultsController: nil)
    var filterData = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "Directory Member"
        
        //tableView.tableFooterView = UIView(frame: CGRect.zero)
        tableView.separatorStyle = .none
        self.tabBarController?.tabBar.isHidden = true
        let button =  UIButton(frame: CGRect(x: self.view.frame.width - 65 , y: self.view.frame.height - 115 , width: 50, height: 50))
        //button.setTitle("Edit", forState: UIControlState.Normal)
        button.setImage(UIImage(named: "setting1") , for: UIControlState())
        button.layer.cornerRadius = 25
        button.backgroundColor = themeColor
        
        button.layer.shadowColor =  UIColor(red: 0.0/255.0, green: 0.0/255.0, blue: 0.0/255.0, alpha: 0.4).cgColor
        button.layer.shadowOffset = CGSize(width: 0, height: 3.0);
        button.layer.shadowOpacity = 1.0;
        button.layer.shadowRadius = 10.0;
        button.addTarget(self, action: #selector(self.setting(_:)), for: UIControlEvents.touchUpInside)
        
        self.view.addSubview(button)
        
         self.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
        groupId = selectedGroupId
       print(groupId)
        self.getData()
        
        
        //tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
       /* let cell = tableView.dequeueReusableCell(withIdentifier: "HeaderCell") as! HeaderCell
        cell.directoryMoto.text = selectedGroupAbout
        cell.dirtectoryType.text = selectedGroupCalled
        
        cell.directoryImage.layer.cornerRadius = cell.directoryImage.frame.width/2
        cell.directoryImage.layer.masksToBounds = true
        cell.directoryImage.layer.borderWidth = 1
        cell.directoryImage.layer.borderColor = themeColor.cgColor
        if selectedGroupPhoto != "" && selectedGroupPhoto != "0"  {
            if  let imageData = Data(base64Encoded: selectedGroupPhoto , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                cell.directoryImage.image = DecodedImage
            }
        }else{
            cell.directoryImage.image = UIImage(named: "user.png")
        }

        
        tableView.tableHeaderView = cell*/
        
        searchController.searchResultsUpdater = self
        searchController.searchBar.placeholder = "Search Member..."
        searchController.dimsBackgroundDuringPresentation = false
        searchController.searchBar.delegate = self
        tableView.tableHeaderView = searchController.searchBar
        
        
        
         searchController.searchBar.searchBarStyle = UISearchBarStyle.minimal
         searchController.searchBar.showsCancelButton = true
         searchController.searchBar.setTextColor(color: themeColor)
         searchController.searchBar.setTextFieldColor(color: UIColor.clear)
         searchController.searchBar.setPlaceholderTextColor(color: themeColor)
         searchController.searchBar.setSearchImageColor(color: themeColor)
         searchController.searchBar.setTextFieldClearButtonColor(color: themeColor)
        
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        searchController.dismiss(animated: true, completion: nil)
    }
    
    // set tab title
    func indicatorInfoForPagerTabStrip(_ pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return IndicatorInfo(title: "All User")
    }

    func pushToNextView(_ sender:UIButton) {
      let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "AddMemberViewController") as! AddMemberViewController
        viewController.groupId = groupId
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
   
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if searchController.isActive && searchController.searchBar.text != "" {
            return filterData.count
        }else if selectedMemberCount > data.count {
            return data.count + 1
        }else{
            return data.count
        }

    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        
        if searchController.isActive && searchController.searchBar.text != "" {
            let cell = tableView.dequeueReusableCell(withIdentifier: "DirectoryMemberCell", for: indexPath) as! DirectoryMemberCell
            let info = filterData[indexPath.row].components(separatedBy: "|")
            if info[0] == "" {
                cell.memberNameLabel.text = "Unknown"
            }else{
                cell.memberNameLabel.text = info[0].uppercaseFirst
            }
            cell.memberNumberlabel.text = info[1]
            if info[2] == "1" {
                cell.typeLabel.text = "Admin"
                
                
            }else {
                cell.typeLabel.text = "Member"
                cell.typeLabel.textColor = UIColor.blue
                
            }
            
            if self.getNumber(mobileNo) == self.getNumber(info[1]) {
                if info[2] == "1" {
                    admin = 1
                }
            }
            
            cell.backgroundColor = UIColor.clear
            cell.selectionStyle = .none
            return cell
        }else if indexPath.row == data.count {
            let cell = UITableViewCell()
            cell.textLabel?.text = "Load More..."
            cell.textLabel?.textAlignment = .center
            cell.selectionStyle = .none
            return cell
        }else{
        
            let cell = tableView.dequeueReusableCell(withIdentifier: "DirectoryMemberCell", for: indexPath) as! DirectoryMemberCell
            let info = data[indexPath.row].components(separatedBy: "|")
            if info[0] == "" {
                cell.memberNameLabel.text = "Unknown"
            }else{
                cell.memberNameLabel.text = info[0].uppercaseFirst
            }
            cell.memberNumberlabel.text = info[1]
            if info[2] == "1" {
                cell.typeLabel.text = "Admin"
                

            }else {
                cell.typeLabel.text = "Member"
                cell.typeLabel.textColor = UIColor.blue
                
            }
        
            if self.getNumber(mobileNo) == self.getNumber(info[1]) {
                if info[2] == "1" {
                    admin = 1
                }
            }
        
            cell.backgroundColor = UIColor.clear
            cell.selectionStyle = .none
            return cell
        }
    }
    
    func getNumber(_ str:String) -> String {
        return  String(str.characters.suffix(10))
        
    }

    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if searchController.isActive && searchController.searchBar.text != "" {
            
            searchController.dismiss(animated: true, completion: nil)
            let info = filterData[indexPath.row].components(separatedBy: "|")
            pid = info[4]
            adminFlag = info[2]
            performSegue(withIdentifier: "profile", sender: nil)
        
        }else if indexPath.row == data.count {
            count = count + 1
            self.loadMore()
            
        }else {

            let info = data[indexPath.row].components(separatedBy: "|")
            pid = info[4]
            adminFlag = info[2]
            performSegue(withIdentifier: "profile", sender: nil)
        }
    }
    
    func loadMore() {
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            let sendJson: [String: String] = [
                "PUID": userID,
                "PGROUPID":groupId,
                "PTIMESTAMP":"",
                "PPAGENUMBER":String(count),
                "PRECORDCOUNT":"10"
            ]
            DataProvider.sharedInstance.getServerData2(sendJson, path: "FetchGroupDelta", successBlock: { (response) in
                print(response)
                let data1 = (response[0]["MEMBER"].stringValue).components(separatedBy: "#")
                
                for i in 0 ..< data1.count {
                
                    self.data.append(data1[i])
                }
                
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
            tableView.reloadData()
        }
    }

    
    
    func getData() {
        
        if Reachability.isConnectedToNetwork() {
        startLoader(view: self.view)
        let sendJson: [String: String] = [
            "PUID": userID,
            "PGROUPID":groupId,
            "PTIMESTAMP":"",
            "PPAGENUMBER":"1",
            "PRECORDCOUNT":"10"
        ]
        DataProvider.sharedInstance.getServerData2(sendJson, path: "FetchGroupDelta", successBlock: { (response) in
            print(response)
             self.data = (response[0]["MEMBER"].stringValue).components(separatedBy: "#")
            print(self.data)
            self.saveDataOffLine()
            self.saveData()
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
            tableView.reloadData()
        }
    }
    
   /* func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let cell = tableView.dequeueReusableCell(withIdentifier: "HeaderCell") as! HeaderCell
       cell.directoryMoto.text = selectedGroupAbout
        cell.dirtectoryType.text = selectedGroupCalled
        
        cell.directoryImage.layer.cornerRadius = cell.directoryImage.frame.width/2
        cell.directoryImage.layer.masksToBounds = true
        cell.directoryImage.layer.borderWidth = 1
        cell.directoryImage.layer.borderColor = themeColor.cgColor
        if selectedGroupPhoto != "" && selectedGroupPhoto != "0"  {
            if  let imageData = Data(base64Encoded: selectedGroupPhoto , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                cell.directoryImage.image = DecodedImage
            }
        }else{
            cell.directoryImage.image = UIImage(named: "user.png")
        }


        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 88
    }*/
    
    
    /* func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        
        if data.count == 0 {
            return false
        }
        
        if selectedAdminFlag == "1" {
           return true
            
        }else{
            return true
        }
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        
        if searchController.isActive && searchController.searchBar.text != "" {
            
            let info = filterData[indexPath.row].components(separatedBy: "|")
            if info[1] == mobileNo {
                self.showAlert("Alert", message: "You can not delete this member.")
                self.tableView.reloadData()
                return
            }
        }else{
            
            let info = data[indexPath.row].components(separatedBy: "|")
            if info[1] == mobileNo {
                self.showAlert("Alert", message: "You can not delete this member.")
                self.tableView.reloadData()
                return 
            }
        }
        
        
         if selectedAdminFlag == "1" {
        if (editingStyle == UITableViewCellEditingStyle.delete) {
            
            var info = [String]()
            if searchController.isActive && searchController.searchBar.text != "" {
                 info = filterData[indexPath.row].components(separatedBy: "|")
            }else{
                 info = data[indexPath.row].components(separatedBy: "|")
            }
            
            let sendJson: [String: String] = [
                "PUID": userID,
                "PGROUPID":groupId,
                "PPHONENUMBER": info[1]
            ]

           if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData2(sendJson, path: "DeleteMember", successBlock: { (response) in
            
                stopLoader()
                
                if response["STATUS"].stringValue == "SUCCESS" {
                    self.data.remove(at: indexPath.row)
                    self.showAlert("Confirmation", message: "Member deleted Successfully.")
                }
                
                DispatchQueue.main.async(execute: {
                    self.tableView.reloadData()
                })
                }, errorBlock: { (error) in
                    print(error)
                    stopLoader()
            })
            
        }
        }
        }
    }*/
    
    
    
    func setting(_ sender:UIButton) {
        let refreshAlert = UIAlertController(title: "", message: "", preferredStyle: .actionSheet )
        
        refreshAlert.addAction(UIAlertAction(title: "Pending Member", style: .default , handler: { (action: UIAlertAction!) in
            let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "PendingMemberViewController") as! PendingMemberViewController
            viewController.groupId = self.groupId
            self.navigationController?.pushViewController(viewController, animated: true)
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Add Member", style: .default , handler: { (action: UIAlertAction!) in
            let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "AddMemberViewController") as! AddMemberViewController
            viewController.groupId = self.groupId
            self.navigationController?.pushViewController(viewController, animated: true)

        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Edit Directory", style: .default , handler: { (action: UIAlertAction!) in
            //let viewController =   self.storyboard?.instantiateViewControllerWithIdentifier("EditDirectoryViewController") as! EditDirectoryViewController
            let viewController =   self.storyboard?.instantiateViewController(withIdentifier: "ProfessionViewController") as! ProfessionViewController
         //   viewController.groupId = self.groupId
            self.navigationController?.pushViewController(viewController, animated: true)

            
        }))
        
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel , handler: { (action: UIAlertAction!) in
            
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }

    
    // save data into sqlite
    func saveDataOffLine(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM DirectoryMember where groupId = '\(groupId)' "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< data.count {
                let insertSQL = "INSERT INTO DirectoryMember ( GroupData, GroupId) VALUES ('\(data[i])', '\(groupId)')"
                let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                
                if !result! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
            }
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    func saveData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM GroupData "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< data.count {
                
                var info = data[i].components(separatedBy: "|")
                var Blood = "-1"
                if info[5] != "" && info[5] != " " && info[5] != "0"{
                    Blood = info[5]
                }
                
                var city = "-1"
                if info[6] != "" && info[6] != " " && info[6] != "0"{
                    city = info[6]
                }
                
                var profession = "-1"
                if info[7] != "" && info[7] != " " && info[7] != "0"{
                    profession = info[7]
                }
                
                
                let insertSQL = "INSERT INTO GroupData (name,number,flag,address,UID,bloodGroup,city,profession,Date) VALUES ('\(info[0])', '\(info[1])','\(info[2])', '\(info[3])', '\(info[4])','\(Blood)', '\(city)', '\(profession)', '\(info[8])')"
                let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                
                if !result! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
            }
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
    
    
    // get data from sqlite
    func getOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        self.data = []
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM DirectoryMember where groupId = '\(groupId)'"
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            while((results?.next()) == true){
                self.data.append(results!.string(forColumn: "GroupData")!)
            }
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "profile" {
            let detailVC = segue.destination as! ProfileViewController
            detailVC.pid = pid
            detailVC.adminFlag =  adminFlag
            detailVC.admin = self.admin
        }else if segue.identifier == "pending" {
            let detailVC = segue.destination as! PendingMemberViewController
            detailVC.groupId = groupId
        }
    }
    
    func filterContentForSearchText(_ searchText: String, scope: String = "All") {
        filterData = data.filter { data in
            return data.lowercased().contains(searchText.lowercased())
            // return candy.name.lowercaseString.containsString(searchText.lowercaseString)
        }
        
        tableView.reloadData()
    }



}

extension DirectoryMemberViewController: UISearchBarDelegate {
    // MARK: - UISearchBar Delegate
    func searchBar(_ searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
        filterContentForSearchText(searchBar.text!, scope: searchBar.scopeButtonTitles![selectedScope])
    }
}

extension DirectoryMemberViewController: UISearchResultsUpdating {
    func updateSearchResults(for searchController: UISearchController) {
        filterContentForSearchText(searchController.searchBar.text!)
    }
}

