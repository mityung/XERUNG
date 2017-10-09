//
//  ViewController.swift
//  Xerung
//
//  Created by mityung on 14/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import CoreTelephony
import SwiftyJSON

var userID = ""
class ViewController: UIViewController, UITextFieldDelegate , DropDownViewControllerDelegate , UIPopoverPresentationControllerDelegate {

    
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var mobileView: UIView!
    @IBOutlet weak var countryView: UIView!
    @IBOutlet weak var mobileNumberText: UITextField!
    @IBOutlet weak var countryNameText: UITextField!
    @IBOutlet weak var loginView: UIView!
    
    
    var OTP = 0
    
    var countryName = [String]()
    var countryCode = [String]()
    var countryPhoneCode = [String]()
    
    var tmpTextField:UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        var Login = ""
        
        if let temp  = UserDefaults.standard.string(forKey: "Login") {
            Login = temp
        }
        // check user alerdy login or not
        
        if Login == "Yes" {
            
            if let temp = UserDefaults.standard.string(forKey: "profileJson"){
                
                let objectData = temp.data(using: String.Encoding.utf8)
                
                do {
                let json = try JSONSerialization.jsonObject(with: objectData!, options: JSONSerialization.ReadingOptions.mutableContainers)
                profileJson = JSON(json)
                }catch  let error as NSError {
                    print(error)
                }
                
            }
            
            
            
            if let temp = UserDefaults.standard.string(forKey: "name") {
                name = temp
            }
            if let temp =  UserDefaults.standard.string(forKey: "mobileNo") {
                mobileNo = temp
            }
            if let temp = UserDefaults.standard.string(forKey: "uid") {
                userID = temp
            }
            
            if let temp = UserDefaults.standard.string(forKey: "CountryPhoneCode") {
                CountryPhoneCode = temp
            }
            if let temp =  UserDefaults.standard.string(forKey: "CountryCode") {
                CountryCode = temp
            }
            if let temp = UserDefaults.standard.string(forKey: "CountryName") {
                CountryName = temp
            }
            
           self.nextView()
        }

        // set the shadow of view
        loginView.backgroundColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.6)
        loginView.layer.shadowRadius = 10
        loginView.layer.shadowOffset = CGSize(width: 0, height: 0)
        loginView.layer.shadowColor = UIColor.black.cgColor
        loginView.clipsToBounds = false
        self.loginView.layer.shadowOpacity = 0.3;
        
        loginView.layer.cornerRadius = 10
       // loginView.layer.masksToBounds = true
        
        loginButton.layer.cornerRadius = loginButton.frame.height/2
        loginButton.layer.masksToBounds = true
        loginButton.addTarget(self, action: #selector(self.submitData(_:)), for: .touchUpInside)
        
        
        
        
        countryNameText.delegate = self
        countryNameText.tag = 1
        mobileNumberText.delegate = self
        mobileNumberText.keyboardType = .numberPad
        mobileNumberText.tag = 2
        
       
        
       /* let networkInfo = CTTelephonyNetworkInfo()
        let carrier = networkInfo.subscriberCellularProvider
        
        print(carrier)
        print(carrier?.mobileCountryCode)
        print(carrier?.isoCountryCode)
        
        
        let currentLocale1 = NSLocale.currentLocale()
        let countryCode = currentLocale1.objectForKey(NSLocaleCountryCode) as! String//get the set country name, code of your iphone
        print("country code is \(countryCode)")*/
        
        // get the localized country name (in my case, it's US English)
        let englishLocale : NSLocale = NSLocale.init(localeIdentifier :  "en_US")
        
        // get the current locale
        let currentLocale = NSLocale.current
        
        let theEnglishName : String? = englishLocale.displayName(forKey: NSLocale.Key.identifier, value: currentLocale.identifier)
        if let theEnglishName = theEnglishName
        {
            let countryName = theEnglishName.sliceFrom("(", to: ")")
            print("the localized country name is \(countryName)")
            
            countryNameText.text = countryName!
        }
       
        
        
        self.gradient()
        let numberToolbar: UIToolbar = UIToolbar()
        numberToolbar.barStyle = UIBarStyle.default
        numberToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.Apply))
            
        ]
        numberToolbar.sizeToFit()
        mobileNumberText.inputAccessoryView = numberToolbar
            
        self.getOffLineData()
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        var allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        
        if textField.tag == 1 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
        }else if textField.tag == 2 {
            allowedCharacter = "0123456789"
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
                return numberOfChars < 21
            }else if textField.tag == 2 {
                return numberOfChars < 13
            }else {
                return numberOfChars < 51
            }
            
        }else{
            return string == numberFiltered
        }
        
    }
   
    
    
    
    func  fetchCountryName() {
        let sendJson: [String: String] = [
            "PFLAG": "1"
        ]
        
        if Reachability.isConnectedToNetwork() {
            
            DataProvider.sharedInstance.getServerData2(sendJson, path: "CountryList", successBlock: { (response) in
                
                print(response)
                for i in 0 ..< response.count {
                    self.countryName.append(response[i]["COUNTRYNAME"].stringValue)
                     self.countryCode.append(response[i]["COUNTRYCODEID"].stringValue)
                     self.countryPhoneCode.append(response[i]["PHONECOUNTRYCODE"].stringValue)
                }
                
                
                self.saveDataOffLine()
                
            }) { (error) in
                print(error)
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }

   
    @IBAction func registerNowButton(_ sender: AnyObject) {
        //let view = storyboard?.instantiateViewControllerWithIdentifier("RegisterViewController") as! RegisterViewController
        //self.navigationController?.pushViewController(view, animated: true)
    }
    
    func Apply () {
        self.view.endEditing(true)
    }
    
   
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func submitData(_ sender:UIButton){
        
        if (mobileNumberText.text?.characters.count)! < 10 {
            self.showAlert("Alert", message: "Please Insert correct mobile number.")
            return
        }
        
        if countryNameText.text?.characters.count == 0 {
            self.showAlert("Alert", message: "Please select country.")
            return
        }
        
        let temp = countryName.index(of: countryNameText.text!)
        
        CountryName = countryNameText.text!
        CountryCode = countryCode[temp!]
       
        CountryPhoneCode = countryPhoneCode[temp!]
        
        if CountryPhoneCode == "91" {
            CountryPhoneCode = "+91"
        }
        
       print(CountryPhoneCode)
        
         self.view.endEditing(true)
        let sendJson: [String: String] = [
            "PPHONENUMBER":(CountryPhoneCode + mobileNumberText.text!) ,
            "PEMAIL":"",
            "PNAME":"",
            "PADDRESS":"",
            "PCITYID":"0",
            "PSTATEID":"0",
            "PCOUNTRYCODEID":CountryPhoneCode,
            "PCOUNTRYNAME":CountryName,
            "POTPID":"0",
            "PSTATUSID":"0",
            "PPROFESSION":"",
            "PLOGINFLAG":"0"
        ]
        
        if Reachability.isConnectedToNetwork() {
        
        DataProvider.sharedInstance.getServerData(sendJson, path: "RegLogin", successBlock: { (response) in
                //self.sendOTPtoServer(String(response["OTP"].intValue))
            print(response)
                if response["STATUS"].stringValue == "0"{
                    self.OTP = response["OTP"].intValue
                    self.callSendOptService(String(response["OTP"].intValue))
                    print(self.OTP)
                    OTPText = String(self.OTP)
                    mobileNo = self.mobileNumberText.text!
                    
                    DispatchQueue.main.async(execute: {
                        self.performSegue(withIdentifier: "signup", sender: nil)
                    })
                    
                }else{
                    self.OTP = response["OTP"].intValue
                    self.callSendOptService(String(response["OTP"].intValue))
                    print(self.OTP)
                    self.CheckOTP()
                }
            
                
            
            }) { (error) in
                print(error)
        }
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    
    func sendOTPtoServer(_ otp:String) {
        let sendJson: [String: String] = [
            "PPHONENUMBER":(CountryPhoneCode + mobileNumberText.text!) ,
            "PEMAIL":"",
            "PNAME":"",
            "PADDRESS":"",
            "PCITYID":"0",
            "PSTATEID":"0",
            "PCOUNTRYCODEID":CountryPhoneCode,
            "PCOUNTRYNAME":CountryName,
            "POTPID":otp,
            "PSTATUSID":"0",
            "PPROFESSION":"",
            "PLOGINFLAG":"0"
        ]
        
        if Reachability.isConnectedToNetwork() {
        
        DataProvider.sharedInstance.getServerData(sendJson, path: "RegLogin", successBlock: { (response) in
          // self.fetchProfile(String(response["UID"].intValue))
            print(response)
            userID = String(response["UID"].intValue)
            self.fetchProfile(userID)
           
        }) { (error) in
            print(error)
        }
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func nextView(){
       
        DispatchQueue.main.async(execute: {
            
            let storyBoard = UIStoryboard(name: "Main", bundle: nil) as UIStoryboard
            let mfSideMenuContainer = storyBoard.instantiateViewController(withIdentifier: "MFSideMenuContainerViewController") as! MFSideMenuContainerViewController
            let dashboard = storyBoard.instantiateViewController(withIdentifier: "Directory_ViewController") as! UITabBarController
            let leftSideMenuController = storyBoard.instantiateViewController(withIdentifier: "SideMenuViewController") as! SideMenuViewController
            mfSideMenuContainer.leftMenuViewController = leftSideMenuController
            mfSideMenuContainer.centerViewController = dashboard
            let appDelegate  = UIApplication.shared.delegate as! AppDelegate
            appDelegate.window?.rootViewController = mfSideMenuContainer
            
            //self.performSegue(withIdentifier: "login", sender: nil)
        })

        
    }
    
    func fetchProfile(_ uid:String) {
        
        
        UserDefaults.standard.setValue(name, forKey: "name")
        UserDefaults.standard.setValue(mobileNo, forKey: "mobileNo")
        UserDefaults.standard.setValue(uid, forKey: "uid")
        
        UserDefaults.standard.setValue(CountryPhoneCode, forKey: "CountryPhoneCode")
        UserDefaults.standard.setValue(CountryCode, forKey: "CountryCode")
        UserDefaults.standard.setValue(CountryName, forKey: "CountryName")
        
        UserDefaults.standard.setValue("Yes", forKey: "Login")
        UserDefaults.standard.synchronize()
        
        
        let sendJson: [String: String] = [
            "PUID":uid
        ]
        print(sendJson)
        
        if Reachability.isConnectedToNetwork() {
        
        DataProvider.sharedInstance.getServerData(sendJson, path: "fetchProfile", successBlock: { (response) in
            print(response)
            profileJson = response
            name = response["NAME"].stringValue
            mobileNo = response["PHONENUMBER"].stringValue
            
            
            
             UserDefaults.standard.set(String(describing: profileJson), forKey: "profileJson")
           // NSUserDefaults.standardUserDefaults().setValue(profileJson, forKey: "profileJson")
            UserDefaults.standard.setValue(name, forKey: "name")
            UserDefaults.standard.setValue(mobileNo, forKey: "mobileNo")
            UserDefaults.standard.setValue(uid, forKey: "uid")
            
            UserDefaults.standard.setValue(CountryPhoneCode, forKey: "CountryPhoneCode")
            UserDefaults.standard.setValue(CountryCode, forKey: "CountryCode")
            UserDefaults.standard.setValue(CountryName, forKey: "CountryName")
            
            UserDefaults.standard.setValue("Yes", forKey: "Login")
            UserDefaults.standard.synchronize()

            
             self.nextView()
        }) { (error) in
            print(error)
        }
        
    }else{
            showAlert("Alert", message: "No internet connectivity.")
    }

    }

    func callSendOptService(_ otp:String){
        let data = DataString.encodeText(CountryCode.replacingOccurrences(of: "+", with: "", options: NSString.CompareOptions.literal, range: nil)+"" +  (mobileNumberText.text!))+"#"+DataString.encodeText(String(otp)+" "+"is your verification code for Xerung.");
        print(data)
        //updateCounter();
        sendOtpMsg(data);
    }

    
    func CheckOTP(){
        
        let title = "Please enter OTP."
        
        let alert = UIAlertController(title: "OTP", message:title, preferredStyle: .alert)
        alert.addTextField(configurationHandler: { (textField) -> Void in
            textField.layer.cornerRadius = 5
            textField.keyboardType = .numberPad
        })
        alert.addAction(UIAlertAction(title: "CANCEL", style: .default, handler: { (action: UIAlertAction!) in
        }))
        
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action) -> Void in
            let textField = alert.textFields![0] as UITextField
            
            if String(self.OTP) == textField.text! {
                self.sendOTPtoServer(textField.text!)
            }else{
                self.showAlert("Alert", message: "OTP is not Match!")
            }
        }))
        
        
        self.present(alert, animated: true, completion: nil)
    }
    
    func sendOtpMsg(_ sendData:String){
        if Reachability.isConnectedToNetwork(){
           
        
        
        //  print("sendOTP Message.....\(sendData)");
        
        let myUrl = URL(string: DataProvider.sharedInstance.msgURL);
        let request = NSMutableURLRequest(url:myUrl!);
        request.httpMethod = "POST";
        request.setValue("text/plain",forHTTPHeaderField : "Content-Type");
        request.setValue("Content-Language",forHTTPHeaderField : "en-US");
        // Compose a query string
        let sendJsons = sendData;
        request.httpBody = String(sendJsons).data(using: String.Encoding.utf8);
        let task = URLSession.shared.dataTask(with: request as URLRequest, completionHandler: {
            data, response, error in
            if error != nil
            {
                
                return
            }
            // Print out response body
            let responseString = NSString(data: data!, encoding: String.Encoding.utf8.rawValue)
            //  print("msgResponse \(responseString)");
        }) 
        task.resume()
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
        
    }
    
    
    // show alert using this method
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            
            if message == "OTP is not Match!" {
                self.CheckOTP()
            }
            
        }))
        present(refreshAlert, animated: true, completion: nil)
    }

       
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField.tag == 1 {
            countryView.backgroundColor = UIColor(red: 26.0/255.0, green: 159.0/255.0, blue: 154.0/255.0, alpha: 1.0)
        }else if textField.tag == 2 {
             mobileView.backgroundColor = UIColor(red: 26.0/255.0, green: 159.0/255.0, blue: 154.0/255.0, alpha: 1.0)
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 1 {
            countryView.backgroundColor = UIColor(red: 125.0/255.0, green: 125.0/255.0, blue: 125.0/255.0, alpha: 1.0)
        }else if textField.tag == 2 {
            mobileView.backgroundColor = UIColor(red: 125.0/255.0, green: 125.0/255.0, blue: 125.0/255.0, alpha: 1.0)
        }
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
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
        
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if textField.tag == 1{
            self.dropDown(textField)
            return false
        }else{
            return true
        }
    }
    

    
    func gradient() {
        
        let gradient: CAGradientLayer = CAGradientLayer()
        gradient.frame = self.view.bounds
        gradient.colors = [UIColor(red: 50.0/255.0, green: 194.0/255.0, blue: 77.0/255.0, alpha: 0.2).cgColor,  UIColor(red: 50.0/255.0, green: 194.0/255.0, blue: 77.0/255.0, alpha: 0.5).cgColor , UIColor(red: 50.0/255.0, green: 194.0/255.0, blue: 77.0/255.0, alpha: 0.9).cgColor ]
        gradient.startPoint = CGPoint(x: 0, y: 0)
        gradient.endPoint = CGPoint(x: 1, y: 1)
        self.view.layer.insertSublayer(gradient, at: 0)
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
            if countryCode.count == 0 {
                self.fetchCountryName()
            }
           
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
            let querySQL = "DELETE FROM Country "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< countryName.count {
                let insertSQL = "INSERT INTO Country (countryName, countryCodeId, phoneCountryCode) VALUES ('\(countryName[i])', '\(countryCode[i])', '\(countryPhoneCode[i])')"
                let result = contactDB?.executeUpdate(insertSQL , withArgumentsIn: nil)
                
                if !result! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
            }
        } else {
            print("Error: \(contactDB?.lastErrorMessage())")
        }
    }
    
    
    


}




extension String {
    func sliceFrom(_ start: String, to: String) -> String? {
        return (range(of: start)?.upperBound).flatMap { sInd in
            (range(of: to, range: sInd..<endIndex)?.lowerBound).map { eInd in
                substring(with: sInd..<eInd)
            }
        }
    }
}

