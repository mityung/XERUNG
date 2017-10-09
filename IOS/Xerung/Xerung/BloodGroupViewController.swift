//
//  BloodGroupViewController.swift
//  Xerung
//
//  Created by mityung on 20/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import XLPagerTabStrip

class BloodGroupViewController: UIViewController ,UITableViewDelegate ,UITableViewDataSource, IndicatorInfoProvider{
    public func indicatorInfo(for pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
         return IndicatorInfo(title: "Blood")
    }


    @IBOutlet weak var tableView: UITableView!
   // var groupId:String!
    var data = [String]()
    var groupType = [String]()
    var groupCount = [String]()
    override func viewDidLoad() {
        super.viewDidLoad()

        //self.getOffLineData()
        //print(data)
        //self.saveData()
        self.findBloodGroup()
        //tableView.tableFooterView = UIView(frame: CGRect.zero)
        tableView.separatorStyle = .none
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        
       // tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
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
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title:"", style:.plain, target:nil, action:nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // set tab title
    func indicatorInfoForPagerTabStrip(_ pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return IndicatorInfo(title: "Blood")
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return groupType.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BloodCell", for: indexPath) as! BloodCell
        cell.bloodGroup.text = groupType[indexPath.row].uppercaseFirst
        cell.groupCount.text = groupCount[indexPath.row]
        cell.selectionStyle = .none
        cell.backgroundColor = UIColor.clear
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
  /*  func getOffLineData(){
        let databasePath = NSUserDefaults.standardUserDefaults().URLForKey("DataBasePath")!
        let contactDB = FMDatabase(path: String(databasePath))
        
       
        if contactDB.open() {
            let querySQL = "SELECT * FROM DirectoryMember where groupId = '\(groupId)'"
            let results:FMResultSet? = contactDB.executeQuery(querySQL,withArgumentsInArray: nil)
            
            while((results?.next()) == true){
                self.data.append(results!.stringForColumn("GroupData")!)
            }
            contactDB.close()
        } else {
            print("Error: \(contactDB.lastErrorMessage())")
        }
    }*/

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "MemberDetailViewController") as! MemberDetailViewController
        viewController.type = "blood"
        if groupType[indexPath.row] == "Unknown" {
            viewController.query = "-1"
        }else{
            viewController.query = groupType[indexPath.row]
        }
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func findBloodGroup(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "SELECT count(*) as Num_row , bloodGroup FROM GroupData GROUP BY bloodGroup "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            
            
            while((results?.next()) == true){
               
                if results!.string(forColumn: "bloodGroup")! == "-1" {
                    groupType.append("Unknown")
                }else{
                    groupType.append(results!.string(forColumn: "bloodGroup")!)
                }
                
               groupCount.append(results!.string(forColumn: "Num_row")!)
            }
            
            tableView.reloadData()
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }

    }
    
  /*  func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
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
    
    
  /*  func saveData(){
        let databasePath = NSUserDefaults.standardUserDefaults().URLForKey("DataBasePath")!
        let contactDB = FMDatabase(path: String(databasePath))
        
        
        if contactDB.open() {
            let querySQL = "DELETE FROM GroupData "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsInArray: nil)
            
            for i in 0 ..< data.count {
                
                var info = data[i].componentsSeparatedByString("|")
                var Blood = "-1"
                if info[5] != "" && info[5] != " "{
                    Blood = info[5]
                }
                
                
                let insertSQL = "INSERT INTO GroupData (name,number,flag,address,UID,bloodGroup,city,profession,Date) VALUES ('\(info[0])', '\(info[1])','\(info[2])', '\(info[3])', '\(info[4])','\(Blood)', '\(info[6])', '\(info[7])', '\(info[8])')"
                let result = contactDB.executeUpdate(insertSQL , withArgumentsInArray: nil)
                
                if !result {
                    print("Error: \(contactDB.lastErrorMessage())")
                }
            }
        } else {
            print("Error: \(contactDB.lastErrorMessage())")
        }
    }*/
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
