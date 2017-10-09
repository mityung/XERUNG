//
//  SearchDirectoryViewController.swift
//  Xerung
//
//  Created by Mac on 07/04/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class SearchDirectoryViewController: UIViewController  ,UITableViewDataSource,UITableViewDelegate ,UISearchBarDelegate  {
    
    @IBOutlet var tableView: UITableView!
    var groupId:String!
    var searchBar = UISearchBar(frame: CGRect(x: 0, y: 0, width: 200, height: 0))
    
    struct publicDirectory {
        var TAGNAME = ""
        var DESCRITION = ""
        var GROUPACCESSTYPE = ""
        var GROUPNAME = ""
        var GROUPPHOTO = ""
        var GROUPID = ""
    }
    var information = [publicDirectory]()
    
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
    
    var Id = [String]()
    var logoImageView   : UIImageView!
   var data = [directory]()
    var searchBarButtonItem: UIBarButtonItem?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "Search Directory"
        searchBar.delegate = self
        searchBar.searchBarStyle = UISearchBarStyle.minimal
        searchBar.showsCancelButton = true
        searchBar.placeholder = "Search Directories..."
        searchBar.setTextColor(color: .white)
        searchBar.setTextFieldColor(color: UIColor.clear)
        searchBar.setPlaceholderTextColor(color: .white)
        searchBar.setSearchImageColor(color: .white)
        searchBar.setTextFieldClearButtonColor(color: .white)
        let imgClear = UIImage(named: "close")
        searchBar.setImage(imgClear, for: UISearchBarIcon.clear, state: .normal)
        self.showSearchBar()
        
    
        tableView.separatorStyle = .none
        self.tableView.estimatedRowHeight = 350
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
        
        self.getOffLineData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
            cell.directoryService.text = information[indexPath.row].TAGNAME
            cell.directorylist.text = information[indexPath.row].DESCRITION
            if information[indexPath.row].GROUPPHOTO != "" && information[indexPath.row].GROUPPHOTO != "0"{
                if  let imageData = Data(base64Encoded: information[indexPath.row].GROUPPHOTO , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                    let DecodedImage = UIImage(data: imageData)
                    cell.publicDirectoryImage.image = DecodedImage
                    
                }
            }else{
                cell.publicDirectoryImage.image = UIImage(named: "defaultDirectory")
                
            }
            cell.selectionStyle = .none
            cell.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.2)
            return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        searchBar.endEditing(true)
        
            
            groupId = information[indexPath.row].GROUPID
            if information[indexPath.row].GROUPACCESSTYPE == "2" {
                let nextView = storyboard?.instantiateViewController(withIdentifier: "PublicMemberViewController") as! PublicMemberViewController
                nextView.groupID = groupId
                self.navigationController?.pushViewController(nextView, animated: true)
            }else{
                
                if Id.contains(groupId) == true {
                    let index = self.Id.index(of: groupId)
                    
                    selectedGroupId = data[index!].GROUPID
                    selectedGroupName = data[index!].GROUPNAME
                    selectedGroupAbout = data[index!].DESCRITION
                    selectedGroupCalled = data[index!].TAGNAME
                    selectedGroupPhoto = data[index!].GROUPPHOTO
                    selectedAdminFlag = data[index!].ADMINFLAG
                    selectedMemberCount = Int(data[index!].MEMBERCOUNT)!
                    // performSegueWithIdentifier("memberDirectory", sender: nil)
                    
                    let nextView = storyboard?.instantiateViewController(withIdentifier: "DirectoryDetailViewController") as! DirectoryDetailViewController
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
    
    
    @IBAction func sideMenu(_ sender: Any) {
        self.menuContainerViewController.toggleLeftSideMenuCompletion { () -> Void in
            
        }
    }
    
    
    func showSearchBar() {
        
        searchBar.alpha = 0
        navigationItem.titleView = searchBar
        //tableView.tableHeaderView = searchBar
        navigationItem.setLeftBarButton(nil, animated: true)
        UIView.animate(withDuration: 0.5, animations: {
            self.searchBar.alpha = 1
        }, completion: { finished in
            self.searchBar.becomeFirstResponder()
        })
    }
    
    func hideSearchBar() {
      //  navigationItem.setLeftBarButton(searchBarButtonItem, animated: true)
       // navigationItem.setRightBarButton(searchBarButtonItem, animated: true)
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .search, target: self, action: #selector(self.showSearchBar))
        
        let btn1 = UIButton(type: .custom)
        btn1.setImage(UIImage(named: "bar"), for: .normal)
        btn1.frame = CGRect(x: 0, y: 0, width: 30, height: 30)
        btn1.addTarget(self, action: #selector(self.sideMenu(_:)), for: .touchUpInside)
        let item1 = UIBarButtonItem(customView: btn1)
        
        self.navigationItem.setLeftBarButton(item1, animated: true)
        
        
        searchBar.text = ""
        
        UIView.animate(withDuration: 0.3, animations: {
            self.navigationItem.titleView = self.logoImageView
            self.searchBar.alpha = 0
        }, completion: { finished in
            
        })
    }
    
    
    //MARK: UISearchBarDelegate
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        hideSearchBar()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        
        if (searchBar.text?.characters.count)! < 4 {
            self.showAlert("Alert", message: "Please enter minimum four characters.")
            return
        }
        
        
        searchBar.endEditing(true)
        let sendJson: [String: String] = [
            "PGROUPID": "0",
            "PSERACHTEXT":searchBar.text!,
            "PGROUPACCESSTYPE":"0"
        ]
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData2(sendJson, path: "GroupSearch", successBlock: { (response) in
                print(response)
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
                
                
                DispatchQueue.main.async(execute: {
                    self.tableView.reloadData()
                })
                
                if response.count == 0 {
                    self.showAlert("Alert", message: "No record found.")
                }
                
                stopLoader()
                
                
                
                
            }) { (error) in
                print(error)
                stopLoader()
            }
        } else {
            
                showAlert("Alert", message: "No internet connectivity.")
            
            self.getOffLineDataByKeyword(str: searchBar.text!)
        }
    }
    
   
    
    
    // show alert using this method
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
        }))
        present(refreshAlert, animated: true, completion: nil)
    }
    
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
                
                self.Id.append(results!.string(forColumn: "GROUPID")!)
                
                
            }
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }

    func getOffLineDataByKeyword(str:String){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        self.information = []
        if (contactDB?.open())! {
           // let querySQL = "SELECT * FROM Directory where GROUPNAME = '\(str)' or TAGNAME =  '\(str)' or DESCRITION =  '\(str)' or GROUPNAME = '\(str.uppercaseFirst)' or TAGNAME =  '\(str.uppercaseFirst)' or DESCRITION =  '\(str.uppercaseFirst)' or GROUPNAME = '\(str.lowercaseFirst)' or TAGNAME =  '\(str.lowercaseFirst)' or DESCRITION =  '\(str.lowercaseFirst)' "
            
            let querySQL = "SELECT * FROM Directory where GROUPNAME like '%\(str)%' or TAGNAME like  '%\(str)%' or DESCRITION like  '%\(str)%'  "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            while((results?.next()) == true){
            
                    
                self.information.append(publicDirectory.init(
                    TAGNAME: results!.string(forColumn: "TAGNAME")!,
                    DESCRITION: results!.string(forColumn: "DESCRITION")!,
                    GROUPACCESSTYPE: results!.string(forColumn: "GROUPACCESSTYPE")!,
                    GROUPNAME: results!.string(forColumn: "GROUPNAME")!,
                    GROUPPHOTO: results!.string(forColumn: "GROUPPHOTO")!,
                    GROUPID: results!.string(forColumn: "GROUPID")!))
                
               /* self.data.append(directory.init(
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
                
                self.Id.append(results!.string(forColumn: "GROUPID")!)*/
                
                
            }
            if self.information.count == 0 {
                self.showAlert("Alert", message: "No record found.")
            }
            
            contactDB?.close()
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
            })
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }



    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
