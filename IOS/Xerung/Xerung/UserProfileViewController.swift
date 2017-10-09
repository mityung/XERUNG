//
//  UserProfileViewController.swift
//  Xerung
//
//  Created by mityung on 03/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import SwiftyJSON

class UserProfileViewController: UIViewController ,UINavigationControllerDelegate, UIImagePickerControllerDelegate , UITextFieldDelegate , DropDownViewControllerDelegate , UIPopoverPresentationControllerDelegate{

    
    var imagePicker = UIImagePickerController()
    var userImage:UIImageView!
    var base64Image:String!
    
    @IBOutlet var profileImage: UIImageView!
    @IBOutlet var nameLabel: UILabel!
    @IBOutlet var emailLabel: UILabel!
    @IBOutlet var locationLabel: UILabel!
    @IBOutlet var companyLabel: UILabel!
    @IBOutlet var professionLabel: UILabel!
    @IBOutlet var alternateNumberlabel: UILabel!
    @IBOutlet var numberLabel: UILabel!
    @IBOutlet var editButton: UIButton!
    @IBOutlet var backButton: UIButton!
    
    var cityId = [String]()
    var cityName = [String]()
    var keyboardHeight:CGFloat!
    
    var countryName = [String]()
    var countryCode = [String]()
    var countryPhoneCode = [String]()
    let numberToolbar:UIToolbar = UIToolbar()
    
    @IBOutlet var keyboardHeightLayoutConstraint: NSLayoutConstraint?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        //tableView.backgroundView = UIImageView(image: UIImage(named: "backScreen.png"))
        self.title = "Profile"
        print(profileJson)
        base64Image = profileJson["PHOTO"].stringValue
        self.tabBarController?.tabBar.isHidden = true
        self.fetchProfile()
        self.getOffLineData()
        self.getOffLineData1()
        
        numberToolbar.barStyle = UIBarStyle.default
        numberToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.Apply))
            
        ]
        
        self.fillData()
        numberToolbar.sizeToFit()
        
        self.navigationController?.navigationBar.isHidden = true
        let image = UIImage(named: "backScreen.png")!
       // self.view.backgroundColor = UIColor(patternImage: image )
        
       // profileImage.layer.cornerRadius = profileImage.frame.height/2
        profileImage.layer.masksToBounds = true
        profileImage.layer.borderWidth = 2
        profileImage.layer.borderColor = UIColor.white.cgColor
        //profileImage.dropShadow()
        
        profileImage.layer.shadowRadius = 10
        profileImage.layer.shadowOffset = CGSize(width: 0, height: 0)
        profileImage.layer.shadowColor = UIColor.black.cgColor
        //profileImage.clipsToBounds = false
        profileImage.layer.shadowOpacity = 0.3;
        
        backButton.addTarget(self, action: #selector(self.back(sender:)), for: UIControlEvents.touchUpInside)
        editButton.addTarget(self, action: #selector(self.showRequest), for: UIControlEvents.touchUpInside)
        
        editButton.layer.borderWidth = 1
        editButton.layer.borderColor = UIColor.white.cgColor
        editButton.layer.cornerRadius = 5
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardNotification(notification:)), name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
    }
    
    override func viewDidAppear(_ animated: Bool) {
         self.fetchProfile()
    }
    
    func back(sender:UIButton) {
        //self.navigationController?.popViewController(animated: true)
        self.menuContainerViewController.toggleLeftSideMenuCompletion { () -> Void in
            
        }
    }
    
    func showRequest() {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "ShowProfileViewController") as! ShowProfileViewController
        self.navigationController?.pushViewController(viewController, animated: true)
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
    }


    
    
    func fillData(){
        
        
        nameLabel.text = self.showData(profileJson["NAME"].stringValue)
        numberLabel.text = profileJson["PHONENUMBER"].stringValue
        emailLabel.text = self.showData(profileJson["EMAIL"].stringValue)
        
        if self.showData(profileJson["ALTERNATENO"].stringValue) != "" {
            alternateNumberlabel.text = self.showData(profileJson["ALTERNATENO"].stringValue)
        }else{
            alternateNumberlabel.text = "N/A"
        }
        
        
        var address = self.showData(profileJson["ADDRESS"].stringValue)
        //bloodGroupText.text =  self.showData(profileJson["BLOODGROUP"].stringValue)
        var city = ""
        if self.cityId.count != 0 {
            if profileJson["CITYID"].stringValue == "0" || profileJson["CITYID"].stringValue == "" {
                
            }else{
                let temp =  cityId.index(of: profileJson["CITYID"].stringValue)
                 city = self.cityName[temp!]
            }
            
        }
        
        if address != "" {
            address = address + ","
        }
        
        if city != "" {
            city = city + ","
        }
        
        locationLabel.text = address + city + CountryName
        //countryLabel.text = CountryName
        
        if self.showData(profileJson["COMPANYNAME"].stringValue) != "" {
            companyLabel.text = self.showData(profileJson["COMPANYNAME"].stringValue)
        }else{
            companyLabel.text = "N/A"
        }
        
        if self.showData(profileJson["PROFESSION"].stringValue) != "" {
            professionLabel.text = self.showData(profileJson["PROFESSION"].stringValue)
        }else{
            professionLabel.text = "N/A"
        }
        
        //companyLabel.text = self.showData(profileJson["COMPANYNAME"].stringValue)
        //professionLabel.text =  self.showData(profileJson["PROFESSION"].stringValue)
        
        
        if profileJson["PHOTO"].stringValue != "" && profileJson["PHOTO"].stringValue != "0" {
            if  let imageData = Data(base64Encoded: profileJson["PHOTO"].stringValue , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                profileImage.image = DecodedImage
            }
        }else{
            profileImage.image = UIImage(named: "user.png")
            
        }
        
    }
    
    
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    func keyboardNotification(notification: NSNotification) {
        if let userInfo = notification.userInfo {
            let endFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue
            let duration:TimeInterval = (userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue ?? 0
            let animationCurveRawNSN = userInfo[UIKeyboardAnimationCurveUserInfoKey] as? NSNumber
            let animationCurveRaw = animationCurveRawNSN?.uintValue ?? UIViewAnimationOptions.curveEaseInOut.rawValue
            let animationCurve:UIViewAnimationOptions = UIViewAnimationOptions(rawValue: animationCurveRaw)
            if (endFrame?.origin.y)! >= UIScreen.main.bounds.size.height {
                self.keyboardHeightLayoutConstraint?.constant = 0.0
            } else {
                self.keyboardHeightLayoutConstraint?.constant = endFrame?.size.height ?? 0.0
            }
            UIView.animate(withDuration: duration,
                           delay: TimeInterval(0),
                           options: animationCurve,
                           animations: { self.view.layoutIfNeeded() },
                           completion: nil)
        }
    }
    


    func Apply () {
        self.view.endEditing(true)
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func  fetchCityName() {
        let sendJson: [String: String] = [
            "PCOUNTRYID": CountryCode
        ]
        
        if Reachability.isConnectedToNetwork() {
        
            DataProvider.sharedInstance.getServerData2(sendJson, path: "fetchCity", successBlock: { (response) in
                print(response)
                for i in 0 ..< response.count{
                    self.cityId.append(response[i]["CITYID"].stringValue)
                    self.cityName.append(response[i]["CITYNAME"].stringValue)
                }
            
                
                self.saveDataOffLine()
            
            }) { (error) in
                print(error)
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func fetchProfile() {
        let sendJson: [String: String] = [
            "PUID":userID
        ]
         if Reachability.isConnectedToNetwork() {
            DataProvider.sharedInstance.getServerData(sendJson, path: "fetchProfile", successBlock: { (response) in
                print(response)
                profileJson = response
                UserDefaults.standard.set(String(describing: profileJson), forKey: "profileJson")
                UserDefaults.standard.synchronize()
                DispatchQueue.main.async(execute: {
                   self.fillData()
                })
            }) { (error) in
                print(error)
            }
         }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
        
    }
    
    
    
    
    func showData(_ str:String) -> String {
        if str == "0"{
            return ""
        }else{
            return str
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        var allowedCharacter = ""
        
        if textField.tag == 3 {
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@."
        }else{
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        }
        
        let aSet = CharacterSet(charactersIn:allowedCharacter).inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        
        return string == numberFiltered
        
    }

    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
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
        
        base64Image = imageData!.base64EncodedString(options: [])
        userImage.image = image1
        
        self.savePhoto()
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

    
    func savePhoto() {
        
        if Reachability.isConnectedToNetwork() {
        
        let sendJson: [String: String] = [
            "PPHONENUMBER":mobileNo,
            "PPHOTO":base64Image,
            "PFLAG":"1"
        ]
        
        print(sendJson)
        
        DataProvider.sharedInstance.getServerData2(sendJson, path: "updatePhotoandAccess", successBlock: { (response) in
            print(response)
            if response["STATUS"].intValue == 1 {
                profileJson["PHOTO"].stringValue = self.base64Image
                self.showAlert("Confirmation", message: "Profile Edited Successfully.")
                
            }
        }) { (error) in
            
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
    
    // when click on textfield show the popup for select the subject
    func dropDown(_ textField:UITextField ) {
        
        let selector =  ["A+","A-","B+","B-","AB+","AB-","O+","O-"]
        
        let popoverContent = (self.storyboard?.instantiateViewController(withIdentifier: "DropDownViewController"))! as!        DropDownViewController
        popoverContent.delegate = self
        if textField.tag == 1 {
            popoverContent.data = selector
        }else{
            popoverContent.data = cityName
        }
        
        let nav = UINavigationController(rootViewController: popoverContent)
        nav.modalPresentationStyle = UIModalPresentationStyle.popover
        let popover = nav.popoverPresentationController
        popoverContent.preferredContentSize = CGSize(width: 300,height: 265)
        
        popover!.permittedArrowDirections = .any
        popover!.delegate = self
        popover!.sourceView = textField
        popover!.sourceRect = CGRect(x: textField.frame.width/3, y: 20, width: 0, height: 0)
        self.present(nav, animated: true, completion: nil)
        //tmpTextField = textField
    }
    
    func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle{
        return UIModalPresentationStyle.none
    }
    
    func saveString(_ strText: NSString) {
       // tmpTextField.text = strText as String
        
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if textField.tag == 1{
            self.dropDown(textField)
            return false
        }else if textField.tag == 2{
            self.dropDown(textField)
            return false
        } else{
            return true
        }
    }
    
    
    func getOffLineData1(){
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
    
    
    func getOffLineData(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
       
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM City "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            while((results?.next()) == true){
                self.cityId.append(results!.string(forColumn: "cityId")!)
                self.cityName.append(results!.string(forColumn: "cityName")!)
            }
            if cityId.count == 0 {
                self.fetchCityName()
            }
            
           /* if self.cityId.count != 0 {
                if profileJson["CITYID"].stringValue == "0" {
                    
                }else{
                    let temp =  cityId.indexOf(profileJson["CITYID"].stringValue)
                    self.cityText.text = self.cityName[temp!]
                }
                
            }*/
            
            
            contactDB?.close()
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    // save data into sqlite
    func saveDataOffLine(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM City "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< cityId.count {
                let insertSQL = "INSERT INTO City (cityId,cityName) VALUES ('\(cityId[i])', '\(cityName[i])')"
                let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                
                if !result! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
            }
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
