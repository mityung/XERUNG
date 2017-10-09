//
//  ShowProfileViewController.swift
//  Xerung
//
//  Created by Mac on 06/04/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class ShowProfileViewController: UIViewController,UINavigationControllerDelegate, UIImagePickerControllerDelegate , UITextFieldDelegate , DropDownViewControllerDelegate , UIPopoverPresentationControllerDelegate {
    
    @IBOutlet var userImage: UIImageView!
    @IBOutlet var nameLabel: UILabel!
    @IBOutlet var emailLabel: UILabel!
    @IBOutlet var numberLabel: UILabel!
    @IBOutlet var view1: UIView!
    
    
    @IBOutlet var alternateText: UITextField!
    @IBOutlet var bloodGroupText: UITextField!
    @IBOutlet var companyText: UITextField!
    @IBOutlet var professionText: UITextField!
    @IBOutlet var cityText: UITextField!
    @IBOutlet var addressText: UITextField!
    @IBOutlet var view2: UIView!
    
    @IBOutlet var alternateView: UIView!
    @IBOutlet var bloodView: UIView!
    @IBOutlet var companyView: UIView!
    @IBOutlet var professionView: UIView!
    @IBOutlet var addressView: UIView!
    @IBOutlet var cityView: UIView!
    
    @IBOutlet var submitButton: UIButton!
    
    
    var cityId = [String]()
    var cityName = [String]()
    var keyboardHeight:CGFloat!
    
    var countryName = [String]()
    var countryCode = [String]()
    var countryPhoneCode = [String]()
    let numberToolbar:UIToolbar = UIToolbar()
    
    var imagePicker = UIImagePickerController()
    var tmpTextField:UITextField!
    var base64Image:String!
    
    @IBOutlet var countryLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "Profile"
        base64Image = profileJson["PHOTO"].stringValue
        self.tabBarController?.tabBar.isHidden = true
        self.fetchProfile()
        self.getOffLineData()
        self.getOffLineData1()
        view1.dropShadow()
        view2.dropShadow()
        self.fillData()
        
        alternateText.delegate = self
        bloodGroupText.delegate = self
        companyText.delegate = self
        professionText.delegate = self
        cityText.delegate = self
        addressText.delegate = self
        
        alternateText.autocapitalizationType = .sentences
        bloodGroupText.autocapitalizationType = .sentences
        companyText.autocapitalizationType = .sentences
        professionText.autocapitalizationType = .sentences
        cityText.autocapitalizationType = .sentences
        addressText.autocapitalizationType = .sentences
        
        
        alternateText.tag = 1
        bloodGroupText.tag = 2
        companyText.tag = 3
        professionText.tag = 4
        cityText.tag = 5
        addressText.tag = 6
        
        numberToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.Apply))
            
        ]
        numberToolbar.sizeToFit()
        
        
        /*submitButton.layer.cornerRadius = 5
        submitButton.layer.borderWidth = 1
        submitButton.layer.borderColor = themeColor.cgColor
    
        
        submitButton.addTarget(self, action: #selector(self.SaveData(_:)), for: UIControlEvents.touchUpInside)*/
        
        let dropdown = UIImageView(image: UIImage(named: "drop"))
        dropdown.frame = CGRect(x: 0.0, y: 0.0, width: dropdown.image!.size.width+10.0, height: dropdown.image!.size.height);
        dropdown.contentMode = UIViewContentMode.center
        bloodGroupText.rightView = dropdown;
        bloodGroupText.rightViewMode = UITextFieldViewMode.always
        
        
        let dropdown1 = UIImageView(image: UIImage(named: "drop"))
        dropdown1.frame = CGRect(x: 0.0, y: 0.0, width: dropdown1.image!.size.width+10.0, height: dropdown1.image!.size.height);
        dropdown1.contentMode = UIViewContentMode.center
        cityText.rightView = dropdown1
        cityText.rightViewMode = UITextFieldViewMode.always
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(self.SaveData(_:)))

        
        /*let backgroundImage = UIImageView(frame: self.view.bounds)
        backgroundImage.image = UIImage(named: "backScreen.png")
        self.view.insertSubview(backgroundImage, at: 0)*/
        
       // let image = UIImage(named: "backScreen.png")!
       // self.view.backgroundColor = UIColor(patternImage: image )
        
        
    }
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        var allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        
        if textField.tag == 1 {
            allowedCharacter = "0123456789"
        }else if textField.tag == 2 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ+-"
        }else if textField.tag == 3 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ."
        }else if textField.tag == 4 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ."
        }else if textField.tag == 5 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ."
        }else if textField.tag == 6 {
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz .-"
        }else {
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        }
        
        
        let aSet = CharacterSet(charactersIn:allowedCharacter).inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        if string == numberFiltered{
            let newText = (textField.text! as NSString).replacingCharacters(in: range, with: string)
            let numberOfChars = newText.characters.count
            
            if textField.tag == 1 {
                return numberOfChars < 13
            }else if textField.tag == 2 {
                return numberOfChars < 4
            }else if textField.tag == 3 {
                return numberOfChars < 35
            }else if textField.tag == 4 {
                return numberOfChars < 35
            }else if textField.tag == 5 {
                return numberOfChars < 31
            }else if textField.tag == 6 {
                return numberOfChars < 35
            }else {
                return numberOfChars < 51
            }
            
        }else{
            return string == numberFiltered
        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = false
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        
        if textField.tag == 1 {
            alternateView.backgroundColor =  themeColor
        }else if textField.tag == 2 {
            bloodGroupText.backgroundColor =  themeColor
        }else if textField.tag == 3 {
            companyView.backgroundColor =  themeColor
        }else if textField.tag == 4 {
            professionView.backgroundColor =  themeColor
        }else if textField.tag == 5 {
            cityView.backgroundColor =  themeColor
        }else if textField.tag == 6 {
            addressView.backgroundColor =  themeColor
        }

    }
    
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 1 {
            alternateView.backgroundColor =  UIColor.lightGray
        }else if textField.tag == 2 {
            bloodGroupText.backgroundColor =  UIColor.lightGray
        }else if textField.tag == 3 {
            companyView.backgroundColor = UIColor.lightGray
        }else if textField.tag == 4 {
            professionView.backgroundColor =  UIColor.lightGray
        }else if textField.tag == 5 {
            cityView.backgroundColor =  UIColor.lightGray
        }else if textField.tag == 6 {
            addressView.backgroundColor =  UIColor.lightGray
        }
    }

    
    func fillData(){
        alternateText.keyboardType = .numberPad
        alternateText.inputAccessoryView = numberToolbar
        
        nameLabel.text = self.showData(profileJson["NAME"].stringValue)
        numberLabel.text = profileJson["PHONENUMBER"].stringValue
        emailLabel.text = self.showData(profileJson["EMAIL"].stringValue)
        
        alternateText.text = self.showData(profileJson["ALTERNATENO"].stringValue)
        
        
        addressText.text = self.showData(profileJson["ADDRESS"].stringValue)
        bloodGroupText.text =  self.showData(profileJson["BLOODGROUP"].stringValue)
        
        if self.cityId.count != 0 {
            if profileJson["CITYID"].stringValue == "0" || profileJson["CITYID"].stringValue == "" {
                
            }else{
                let temp =  cityId.index(of: profileJson["CITYID"].stringValue)
                cityText.text = self.cityName[temp!]
            }
            
        }
        countryLabel.text = CountryName
        
        companyText.text = self.showData(profileJson["COMPANYNAME"].stringValue)
        professionText.text =  self.showData(profileJson["PROFESSION"].stringValue)
        
        
        if profileJson["PHOTO"].stringValue != "" && profileJson["PHOTO"].stringValue != "0" {
            if  let imageData = Data(base64Encoded: profileJson["PHOTO"].stringValue , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                userImage.image = DecodedImage
            }
        }else{
            userImage.image = UIImage(named: "user.png")
            
        }
    
    }
    
    func Apply () {
        self.view.endEditing(true)
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
    
    @IBAction func SaveData(_ sender: AnyObject) {
        
        self.Apply()
        
        if (alternateText.text?.trim().characters.count)! < 10 {
            self.showAlert("Alert", message: "Please update valid alternate mobile number.")
            return
        
        }
        
        var city = "0"
        if cityText.text?.characters.count == 0 {
            city = "0"
        }else{
            let temp =  cityName.index(of: cityText.text!)
            city = cityId[temp!]
        }
        
        if Reachability.isConnectedToNetwork() {
            let sendJson: [String: String] = [
                "PPHONENUMBER":getMobileNumber(),
                "PEMAIL":emailLabel.text!,
                "PNAME":nameLabel.text!,
                "PADDRESS":addressText.text!,
                "PCITYID": city,
                "PSTATEID":"0",
                "PCOUNTRYCODEID":CountryPhoneCode + "#" + bloodGroupText.text!,
                "PCOUNTRYNAME":companyText.text!,
                "POTPID":"0",
                "PSTATUSID":"0",
                "PPROFESSION": getProfessionDetail(),
                "PLOGINFLAG":"1"
            ]
            
            print(sendJson)
            startLoader(view: self.view)
            
            DataProvider.sharedInstance.getServerData2(sendJson, path: "RegLogin", successBlock: { (response) in
                
                
                print(response["STATUS"])
                
                if response["STATUS"].intValue == 1 {
                    self.showAlert("Confirmation", message: "Profile updated successfully.")
                    
                }
                stopLoader()
            }) { (error) in
                stopLoader()
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
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
    
    func getMobileNumber() -> String{
        
        
        var mob:String = "";
        if alternateText.text?.trim().characters.count == 0{
            
            mob = "\((mobileNo.trim()))#0";
        }else{
            
            mob = "\((mobileNo.trim()))#\((alternateText.text?.trim())!)";
        }
        return mob;
    }
    
    func getProfessionDetail() -> String{
        let prof = professionText.text?.trim();
        let comp = companyText.text?.trim();
        
        if(prof?.characters.count == 0 && comp?.characters.count == 0){
            
            return "0"+"#"+"0";
        }else if(prof?.characters.count == 0 && comp?.characters.count != 0){
            
            return "\((comp)!)#0";
        }else if(prof?.characters.count != 0 && comp?.characters.count == 0){
            
            return "0#\((prof)!)";
        }else{
            
            return "\((comp)!)#\((prof)!)";
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
        if textField.tag == 2 {
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
        tmpTextField = textField
    }
    
    func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle{
        return UIModalPresentationStyle.none
    }
    
    func saveString(_ strText: NSString) {
        tmpTextField.text = strText as String
        
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if textField.tag == 5{
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
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
