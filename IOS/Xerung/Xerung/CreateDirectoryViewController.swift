//
//  CreateDirectoryViewController.swift
//  Xerung
//
//  Created by mityung on 23/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class CreateDirectoryViewController: UIViewController, UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate ,UINavigationControllerDelegate, UIImagePickerControllerDelegate  , DropDownViewControllerDelegate , UIPopoverPresentationControllerDelegate , UITextViewDelegate {

    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var aboutDirectory: UITextView!
    @IBOutlet weak var privateDirectory: UITextField!
    @IBOutlet weak var directoryName: UITextField!
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet var view3: UIView!
    @IBOutlet var view2: UIView!
    @IBOutlet var view1: UIView!
    var photo = "0"
    var imagePicker = UIImagePickerController()
    var playerIndex = [Int]()
    
    var countryName = [String]()
    var countryCode = [String]()
    var countryPhoneCode = [String]()
    
    var tmpTextField:UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "Create Directory"
        aboutDirectory.delegate = self
        privateDirectory.delegate = self
        directoryName.delegate = self
        
        directoryName.autocapitalizationType = .sentences
         privateDirectory.autocapitalizationType = .sentences
        
        aboutDirectory.text = "About Directory"
        aboutDirectory.textColor = UIColor.lightGray
        privateDirectory.tag = -2
        directoryName.tag = -3
        
        getOffLineData()
        getContactDetails { (response) in
            DispatchQueue.main.async(execute: {
                self.tableView.reloadData()
            })
        }
        
        userImage.layer.cornerRadius = userImage.frame.width/2
        userImage.layer.masksToBounds = true
        userImage.layer.borderWidth = 1
        userImage.layer.borderColor = themeColor.cgColor
        userImage.image = UIImage(named: "defaultDirectory")
        self.tabBarController?.tabBar.isHidden = true
        
      //  tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        tableView.separatorStyle = .none
        self.tableView.estimatedRowHeight = 170;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        
        /*let backgroundImage = UIImageView(frame: self.view.bounds)
         backgroundImage.image = UIImage(named: "backScreen.png")
         self.view.insertSubview(backgroundImage, at: 0)*/
        
        // let image = UIImage(named: "backScreen.png")!
        //self.view1.backgroundColor = UIColor(patternImage: image )
        
        
        let btn1 = UIButton(type: .custom)
        btn1.setImage(UIImage(named: "bar"), for: .normal)
        btn1.frame = CGRect(x: 0, y: 0, width: 30, height: 30)
        btn1.addTarget(self, action: #selector(self.sideMenu(_:)), for: .touchUpInside)
        let item1 = UIBarButtonItem(customView: btn1)
        
        self.navigationItem.setLeftBarButton(item1, animated: true)
        
        
    }
    
    @IBAction func sideMenu(_ sender: Any) {
        self.menuContainerViewController.toggleLeftSideMenuCompletion { () -> Void in
            
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return phoneBook.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "AddMemberCell2", for: indexPath) as! AddMemberCell2
        
        cell.nameLabel.text = phoneBook[indexPath.row].name
        cell.numberLabel.text = phoneBook[indexPath.row].phone
        cell.countryText.text = phoneBook[indexPath.row].country
        cell.countryText.delegate = self
        cell.countryText.tag = indexPath.row
        cell.backgroundColor = UIColor.clear
        cell.selectionStyle = .none
        
        if playerIndex.contains(indexPath.row) == true {
            cell.tickImage.image = UIImage(named: "checked")
        }else{
            cell.tickImage.image = UIImage(named: "uncheck")
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if checkNumber(phoneBook[indexPath.row].phone) == true {
            if playerIndex.contains(indexPath.row) == true  {
                playerIndex = playerIndex.filter{$0 != indexPath.row}
                tableView.reloadRows(at: [indexPath], with: .fade)
            }else{
                playerIndex.append(indexPath.row)
                tableView.reloadRows(at: [indexPath], with: .fade)
            }
            
        }else{
            self.showAlert("Alert", message: "Please select valid contact number.")
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func checkNumber(_ number:String) -> Bool {
       let num = number.removingWhitespaces()
        if num.characters.count > 9 &&  num.characters.count < 14 {
             return true
        }else {
            return false
        }
    }
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        
        let allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        
        let aSet = CharacterSet(charactersIn:allowedCharacter).inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        if string == numberFiltered{
            let newText = (textField.text! as NSString).replacingCharacters(in: range, with: string)
            let numberOfChars = newText.characters.count
            
            if textField.tag == -3 {
                return numberOfChars < 21
            }else if textField.tag == -2 {
                return numberOfChars < 26
            }else if textField.tag == -1 {
                return numberOfChars < 101
            }else {
                return numberOfChars < 51
            }
            
        }else{
            return string == numberFiltered
        }
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField.tag == -3 {
            view1.backgroundColor = themeColor
            
        }else if textField.tag == -2 {
            view2.backgroundColor = themeColor
        }
    }
    
    
    @available(iOS 10.0, *)
    func textFieldDidEndEditing(_ textField: UITextField, reason: UITextFieldDidEndEditingReason) {
        
        if textField.tag == -3 {
            view1.backgroundColor = UIColor.lightGray
            if (textField.text?.characters.count)! < 4 {
                self.showAlert("Alert", message: "Please enter minimum four characters.")
            }
        }else if textField.tag == -2 {
            view2.backgroundColor = UIColor.lightGray
        }
    }

    
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        
        if text == "\n"  // Recognizes enter key in keyboard
        {
            textView.resignFirstResponder()
            return false
        }

        let allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        
        let aSet = CharacterSet(charactersIn:allowedCharacter).inverted
        let compSepByCharInSet = text.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        if text == numberFiltered{
            let newText = (textView.text! as NSString).replacingCharacters(in: range, with: text)
            let numberOfChars = newText.characters.count
            return numberOfChars < 101
            
        }else{
            return text == numberFiltered
        }

    }
    
    func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
        if textView.text == "About Directory" {
            textView.text = ""
            textView.textColor = UIColor.black
        }
        view3.backgroundColor = themeColor
        return true
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text == "" {
            textView.text = "About Directory"
            textView.textColor = UIColor.lightGray
        }
        view3.backgroundColor =  UIColor.lightGray
    }
    
    @IBAction func createDirectory(_ sender: UIBarButtonItem) {
        self.view.endEditing(true)
        
        
        if directoryName.text == "" {
            self.showAlert("Alert", message: "Please enter values in all fields.")
            return
        }
        
       
        
        if privateDirectory.text == "" {
            self.showAlert("Alert", message: "Please enter values in all fields.")
            return
        }
        
        if (directoryName.text?.characters.count)! < 4 {
            self.showAlert("Alert", message: "Please enter minimum four characters.")
        }
        
        if aboutDirectory.text == "About Directory" {
            aboutDirectory.text = ""
        }


        if aboutDirectory.text == "" {
            self.showAlert("Alert", message: "Please fill all data.")
            aboutDirectory.text = "About Directory"
            aboutDirectory.textColor = UIColor.lightGray
            return
        }
        
        
       var  str = ""
        for i in 0 ..< playerIndex.count {
            
            str  = str + phoneBook[playerIndex[i]].name + ":" + self.getNumber(phoneBook[playerIndex[i]].phone.trim() , country:phoneBook[playerIndex[i]].country) + ":"
         }
        let count = playerIndex.count * 2 + 3
        str = String(count) + ":" + str + name + ":" + mobileNo
       
        
        let sendJson: [String: String] = [
            "PGROUPID":"0",
            "PUID": userID,
            "PGROUPNAME": directoryName.text! ,
            "PMADEBY":mobileNo ,
            "PDESC": aboutDirectory.text!,
            "PSTATEID":"0",
            "PGROUPTYPEVAR":"",
            "PTAGNAME":privateDirectory.text!,
            "PGROUPPHOTO":photo,
            "PCHANGEBYPHONENO":"0",
            "AFLAG":"0",
            "AGROUPMEMBERLIST":str
        ]
        
        print(sendJson)
        if Reachability.isConnectedToNetwork() {
        startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData(sendJson, path: "GroupService", successBlock: { (response) in
          
            print(response)
            if response["STATUS"].stringValue == "SUCCESS" {
                self.sendNotification()
                self.showAlert("Confirmation", message: "Directory created successfully.")
            }else if response["STATUS"].stringValue == "Already exist" {
                self.showAlert("Alert", message: "Directory already exist.")
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
    
    
    func sendNotification() {
        let sendJson: [String: String] = [
            "PPHONENUMBER": mobileNo,
            "PFLAG": "1",
            "PPHONETYPE": "0" ,
        ]
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData(sendJson, path: "sendNotiMSG", successBlock: { (response) in
                
                print(response)
                
                stopLoader()
                
            }) { (error) in
                print(error)
                stopLoader()
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
        
    }
    
    
    func getNumber(_ str:String ,country:String ) -> String {
        let str1 = str.removingWhitespaces()
        let temp = countryName.index(of: country)
        if countryPhoneCode[temp!] == "91" {
            return "+91" + String(str1.characters.suffix(10))
        }else{
            return countryPhoneCode[temp!] + String(str1.characters.suffix(10))
        }
        
    }
    
    @IBAction func UploadPhoto(_ sender: AnyObject) {
        let refreshAlert = UIAlertController(title: "Picture", message: title, preferredStyle: UIAlertControllerStyle.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "Library", style: .default, handler: { (action: UIAlertAction!) in
            self.openLibrary()
            
        }))
        refreshAlert.addAction(UIAlertAction(title: "Camera", style: .default, handler: { (action: UIAlertAction!) in
            self.openCamera()
        }))
        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .default, handler: { (action: UIAlertAction!) in
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController!, didFinishPickingImage image1: UIImage!, editingInfo: NSDictionary!){
        self.dismiss(animated: true, completion: { () -> Void in
            
        })
        
        let image2 = self.resizeImage(image1, targetSize: CGSize(width: 300.0, height: 300.0))
        
        let imageData = UIImageJPEGRepresentation(image2, 0.5)
        
        photo = imageData!.base64EncodedString(options: [])
        userImage.image = image1
    }
    
    
    // take phota using camera
    func openCamera() {
        if UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.camera) {
            let imagePicker = UIImagePickerController()
            imagePicker.delegate = self
            imagePicker.sourceType = UIImagePickerControllerSourceType.camera;
            imagePicker.allowsEditing = false
            self.present(imagePicker, animated: true, completion: nil)
        }
    }
    
    // take photo from library
    func openLibrary() {
        if UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.savedPhotosAlbum){
            print("Button capture")
            imagePicker.delegate = self
            imagePicker.sourceType = UIImagePickerControllerSourceType.savedPhotosAlbum;
            imagePicker.allowsEditing = false
            self.present(imagePicker, animated: true, completion: nil)
        }
    }
    
    
    // this method is used to resize the image
    func resizeImage(_ image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size
        
        let widthRatio  = targetSize.width  / image.size.width
        let heightRatio = targetSize.height / image.size.height
        
        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
        } else {
            newSize = CGSize(width: size.width * widthRatio,  height: size.height * widthRatio)
        }
        
        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)
        
        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }

    
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            if title == "Confirmation" {
                let storyBoard = UIStoryboard(name: "Main", bundle: nil) as UIStoryboard
                let mfSideMenuContainer = storyBoard.instantiateViewController(withIdentifier: "MFSideMenuContainerViewController") as! MFSideMenuContainerViewController
                let dashboard = storyBoard.instantiateViewController(withIdentifier: "Directory_ViewController") as! UITabBarController
                let leftSideMenuController = storyBoard.instantiateViewController(withIdentifier: "SideMenuViewController") as! SideMenuViewController
                mfSideMenuContainer.leftMenuViewController = leftSideMenuController
                mfSideMenuContainer.centerViewController = dashboard
                let appDelegate  = UIApplication.shared.delegate as! AppDelegate
                appDelegate.window?.rootViewController = mfSideMenuContainer
            }
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }
    
    // when click on textfield show the popup for select the subject
    func dropDown(_ textField:UITextField ) {
        
        let selector =  countryName
        
        let popoverContent = (self.storyboard?.instantiateViewController(withIdentifier: "DropDownViewController"))! as!        DropDownViewController
        popoverContent.delegate = self
        popoverContent.data = selector
        let nav = UINavigationController(rootViewController: popoverContent)
        nav.modalPresentationStyle = UIModalPresentationStyle.popover
        let popover = nav.popoverPresentationController
        popoverContent.preferredContentSize = CGSize(width: 300,height: 265)
        
        popover!.permittedArrowDirections = .any
        popover!.delegate = self
        popover!.sourceView = textField
        popover!.sourceRect = CGRect(x: textField.frame.width/3, y: 20, width: 0, height: 0)
        self.present(nav, animated: true, completion: nil)
        tmpTextField = textField
    }
    
    func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle{
        return UIModalPresentationStyle.none
    }
    
    func saveString(_ strText: NSString) {
        tmpTextField.text = strText as String
        phoneBook[tmpTextField.tag].country = strText as String
        
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if textField.tag == -1 || textField.tag == -2 || textField.tag == -3 {
            return true
        }else{
            self.dropDown(textField)
            return false
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == -3 {
            if (textField.text?.characters.count)! < 4 {
                self.showAlert("Alert", message: "Please enter minimum four characters.")
            }
        }

    }
    
    
    func getOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM Country "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            self.countryName = []
            self.countryCode = []
            self.countryPhoneCode = []
            
            while((results?.next()) == true){
                self.countryName.append(results!.string(forColumn: "countryName")!)
                self.countryCode.append(results!.string(forColumn: "countryCodeId")!)
                self.countryPhoneCode.append(results!.string(forColumn: "phoneCountryCode")!)
            }
            
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
