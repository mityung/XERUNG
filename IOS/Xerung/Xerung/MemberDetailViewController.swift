//
//  MemberDetailViewController.swift
//  Xerung
//
//  Created by mityung on 21/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class MemberDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

        struct person {
        var name:String = ""
        var number:String = ""
        var uid:String = ""
    }
    
    var type:String!
    var query:String!
    
    var data = [person]()
    
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "Member Details"
       self.findBloodGroup()
        
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        //tableView.tableFooterView = UIView(frame: CGRect.zero)
       // tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "HeaderCell") as! HeaderCell
        
        
        
        cell.directoryMoto.text = selectedGroupCalled
        cell.dirtectoryType.text = selectedGroupName
        cell.aboutLabel.text = selectedGroupAbout
        
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
            cell.directoryImage.image = UIImage(named: "defaultDirectory")
        }
        
        tableView.tableHeaderView = cell
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MemberDetailCell", for: indexPath) as! MemberDetailCell
        
        if data[indexPath.row].name == "" {
            cell.nameLabel.text = "Unknown"
        }else{
            cell.nameLabel.text = data[indexPath.row].name.uppercaseFirst
        }
        cell.numberLabel.text = data[indexPath.row].number
        
        cell.selectionStyle = .none
        cell.backgroundColor = UIColor.clear
        return cell
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
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }

    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
        viewController.pid = data[indexPath.row].uid
       
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func findBloodGroup(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            var querySQL = ""
            if type == "blood" {
                 querySQL = "SELECT * FROM GroupData where bloodGroup = '\(query!)' "
            }else if type == "city" {
                 querySQL = "SELECT * FROM GroupData where city = '\(query!)' "
            } else if type == "profession" {
                 querySQL = "SELECT * FROM GroupData where profession = '\(query!)' "
            }
            print(querySQL)
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            while((results?.next()) == true){
                
               data.append(person.init(
                    name: results!.string(forColumn: "name")!,
                    number: results!.string(forColumn: "number")!,
                    uid: results!.string(forColumn: "UID")!
                ))
            }
            
            tableView.reloadData()
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
        
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
