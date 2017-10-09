//
//  RegisterViewController.swift
//  Xerung
//
//  Created by mityung on 14/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import SwiftyJSON

class RegisterViewController: UIViewController , UITableViewDelegate, UITableViewDataSource,UITextFieldDelegate , DropDownViewControllerDelegate , UIPopoverPresentationControllerDelegate{

    @IBOutlet weak var tableView: UITableView!
    
    var address:UITextField!
    var alternateNumber:UITextField!
    var cityText:UITextField!
    var emailAddress:UITextField!
    var mobileNumber:UITextField!
    var nameText:UITextField!
    var countryText:UITextField!
    let numberToolbar: UIToolbar = UIToolbar()
    var OTP = 0
    var countryName:String = ""
    var tmpTextField:UITextField!
    var cityId = [String]()
    var cityName = [String]()
    
    var countryName1 = [String]()
    var countryCode = [String]()
    var countryPhoneCode = [String]()
    
    @IBOutlet var keyboardHeightLayoutConstraint: NSLayoutConstraint?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "Register"
        tableView.separatorStyle = .none
        
               
        numberToolbar.barStyle = UIBarStyle.default
        numberToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.Apply))
            
        ]
        numberToolbar.sizeToFit()
        
       
        self.getOffLineData1()
       
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardNotification(notification:)), name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
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
    
    func  fetchCityName() {
        let sendJson: [String: String] = [
            "PCOUNTRYID": CountryCode
        ]
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData2(sendJson, path: "fetchCity", successBlock: { (response) in
                print(response)
                self.cityId = []
                self.cityName = []
                for i in 0 ..< response.count{
                    self.cityId.append(response[i]["CITYID"].stringValue)
                    self.cityName.append(response[i]["CITYNAME"].stringValue)
                }
                stopLoader()
                self.saveDataOffLine()
            
            }) { (error) in
                stopLoader()
                print(error)
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func  fetchCountryName() {
        let sendJson: [String: String] = [
            "PFLAG": "1"
        ]
        
        if Reachability.isConnectedToNetwork() {
            
            DataProvider.sharedInstance.getServerData2(sendJson, path: "CountryList", successBlock: { (response) in
                
                for i in 0 ..< response.count {
                    self.countryName1.append(response[i]["COUNTRYNAME"].stringValue)
                    self.countryCode.append(response[i]["COUNTRYCODEID"].stringValue)
                    self.countryPhoneCode.append(response[i]["PHONECOUNTRYCODE"].stringValue)
                }
                
                self.saveDataOffLine1()
                
            }) { (error) in
                print(error)
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    
    @IBAction func backButton(_ sender: AnyObject) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RegisterCell", for: indexPath) as! RegisterCell
        cell.addressText.delegate = self
        self.address = cell.addressText
        
        cell.alternateNumber.delegate = self
        cell.alternateNumber.keyboardType = .numberPad
        self.alternateNumber = cell.alternateNumber
        
        cell.cityText.delegate = self
        cell.cityText.tag = 1
        self.cityText = cell.cityText
        
        cell.emailAddress.delegate = self
        cell.emailAddress.tag = 3
        self.emailAddress = cell.emailAddress
        
        cell.mobileNumber.delegate = self
        cell.mobileNumber.keyboardType = .numberPad
        self.mobileNumber = cell.mobileNumber
        mobileNumber.inputAccessoryView = numberToolbar
        alternateNumber.inputAccessoryView = numberToolbar
        
        cell.nameText.delegate = self
        cell.nameText.tag = 4
        self.nameText = cell.nameText
        
        cell.countryText.delegate = self
        cell.countryText.tag = 2
       
        self.countryText = cell.countryText
        
        cell.submitButton.addTarget(self, action: #selector(self.submitButton(_:)), for: UIControlEvents.touchUpInside)
        
        cell.selectionStyle = .none
        return cell
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func isValidEmail(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
        
        
        let emailRegEx = "^(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?(?:(?:(?:[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+(?:\\.[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+)*)|(?:\"(?:(?:(?:(?: )*(?:(?:[!#-Z^-~]|\\[|\\])|(?:\\\\(?:\\t|[ -~]))))+(?: )*)|(?: )+)\"))(?:@)(?:(?:(?:[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)(?:\\.[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)*)|(?:\\[(?:(?:(?:(?:(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))\\.){3}(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))))|(?:(?:(?: )*[!-Z^-~])*(?: )*)|(?:[Vv][0-9A-Fa-f]+\\.[-A-Za-z0-9._~!$&'()*+,;=:]+))\\])))(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?$"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        let result = emailTest.evaluate(with: testStr)
        return result
    }
    
    
    func submitButton(_ sender:UIButton){
        
        if nameText.text?.trim().characters.count == 0 {
            self.showAlert("Alert", message: "Please fill name.")
            return
        }
       
        
        if emailAddress.text?.trim().characters.count == 0 {
            self.showAlert("Alert", message: "Please fill email address.")
            return
        }else if self.isValidEmail(testStr: emailAddress.text!) == false {
            self.showAlert("Alert", message: "Please fill valid email address.")
            return
        }
        
        if mobileNumber.text?.trim().characters.count != 10 {
            self.showAlert("Alert", message: "Please Insert correct mobile number.")
            return
        }
        
        if countryText.text?.characters.count == 0 {
            self.showAlert("Alert", message: "Please select country.")
            return
        }
        
        

        
        var city = "0"
        if cityText.text?.characters.count == 0 {
            city = "0"
        }else{
            let temp =  cityName.index(of: cityText.text!)
            city = cityId[temp!]
        }
        
        let temp = countryName1.index(of: countryText.text!)
        
        CountryName = countryText.text!
        CountryCode = countryCode[temp!]
        CountryPhoneCode = "+" + countryPhoneCode[temp!]
        
    
        self.view.endEditing(true)
        let sendJson: [String: String] = [
            "PPHONENUMBER":(CountryPhoneCode + mobileNumber.text!) ,
            "PEMAIL": "",
            "PNAME":"",
            "PADDRESS":"",
            "PCITYID": city,
            "PSTATEID":"0",
            "PCOUNTRYCODEID":CountryPhoneCode,
            "PCOUNTRYNAME":CountryName,
            "POTPID":"0",
            "PSTATUSID":"0",
            "PPROFESSION":"",
            "PLOGINFLAG":"0"
        ]
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData(sendJson, path: "RegLogin", successBlock: { (response) in
            print(response)
            self.OTP = response["OTP"].intValue
            self.callSendOptService(String(response["OTP"].intValue))
            self.CheckOTP()
            stopLoader()
            
        }) { (error) in
            print(error)
            stopLoader()
        }
            
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
        
    }
    
    func callSendOptService(_ otp:String){
        let data = DataString.encodeText(CountryPhoneCode.replacingOccurrences(of: "+", with: "", options: NSString.CompareOptions.literal, range: nil)+""+(mobileNumber.text!))+"#"+DataString.encodeText(String(otp)+" "+"is your verification code for Xerung.");
        //updateCounter();
        sendOtpMsg(data);
    }
    
    
    func sendOTPtoServer(_ otp:String) {
        
        
        let sendJson: [String: String] = [
            "PPHONENUMBER":(CountryPhoneCode + mobileNumber.text!) ,
            "PEMAIL": emailAddress.text!,
            "PNAME":nameText.text!,
            "PADDRESS": "",
            "PCITYID":"0",
            "PSTATEID":"0",
            "PCOUNTRYCODEID":CountryPhoneCode,
            "PCOUNTRYNAME":CountryName,
            "POTPID":otp,
            "PSTATUSID":"0",
            "PPROFESSION":"",
            "PLOGINFLAG":"0"
        ]
        print(sendJson)
        if Reachability.isConnectedToNetwork() {
        startLoader(view: self.view)
        DataProvider.sharedInstance.getServerData(sendJson, path: "RegLogin", successBlock: { (response) in
            // self.fetchProfile(String(response["UID"].intValue))
            print(response)
            userID = String(response["UID"].intValue)
            
             self.fetchProfile(userID)
            stopLoader()
        }) { (error) in
            print(error)
            stopLoader()
        }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    
    func UpdateProfile() {
        
        var city = "0"
        if cityText.text?.characters.count == 0 {
            city = "0"
        }else{
            let temp =  cityName.index(of: cityText.text!)
            city = cityId[temp!]
        }

        print(CountryPhoneCode)
        
        let sendJson: [String: String] = [
            "PPHONENUMBER":getMobileNumber(),
            "PEMAIL":emailAddress.text!,
            "PNAME":nameText.text!,
            "PADDRESS":address.text!,
            "PCITYID": city,
            "PSTATEID":"0",
            "PCOUNTRYCODEID":CountryPhoneCode + "#" + "",
            "PCOUNTRYNAME":countryText.text!,
            "POTPID":"0",
            "PSTATUSID":"0",
            "PPROFESSION": "0"+"#"+"0",
            "PLOGINFLAG":"1"
        ]
        
        print(sendJson)
        
        if Reachability.isConnectedToNetwork() {
            startLoader(view: self.view)
            DataProvider.sharedInstance.getServerData(sendJson, path: "RegLogin", successBlock: { (response) in
                // self.fetchProfile(String(response["UID"].intValue))
                print(response)
               self.nextView()
                stopLoader()
            }) { (error) in
                print(error)
                stopLoader()
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func getMobileNumber() -> String{
        var mob:String = "";
        if alternateNumber.text?.trim().characters.count == 0{
            
            mob = "\((mobileNumber.text?.trim())!)#0";
        }else{
            
            mob = "\((mobileNumber.text?.trim())!)#\((alternateNumber.text?.trim())!)";
        }
        return mob;
    }

    
    func fetchProfile(_ uid:String) {
        let sendJson: [String: String] = [
            "PUID":uid
        ]
        
        if Reachability.isConnectedToNetwork() {
        startLoader(view: self.view)
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
            UserDefaults.standard.setValue("Yes", forKey: "Login")
            UserDefaults.standard.setValue(CountryPhoneCode, forKey: "CountryPhoneCode")
            UserDefaults.standard.setValue(CountryCode, forKey: "CountryCode")
            UserDefaults.standard.setValue(CountryName, forKey: "CountryName")
            UserDefaults.standard.synchronize()
            stopLoader()
            self.UpdateProfile()
            
        }) { (error) in
            print(error)
            stopLoader()
        }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func nextView(){
        DispatchQueue.main.async(execute: {
            self.performSegue(withIdentifier: "register", sender: nil)
        })
    }

    
    func CheckOTP(){
        
        let title = "Please enter the OTP."
        
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
        if !Reachability.isConnectedToNetwork(){
            
                showAlert("Alert", message: "No internet connectivity.")
            
            return;
        }
        
        //  print("sendOTP Message.....\(sendData)");
        
        let myUrl = URL(string: DataProvider.sharedInstance.msgURL);
        let request = NSMutableURLRequest(url:myUrl!);
        request.httpMethod = "POST";
        request.setValue("text/plain",forHTTPHeaderField : "Content-Type");
        request.setValue("Content-Language",forHTTPHeaderField : "en-US");
        // Compose a query string
        let sendJsons = sendData;
        request.httpBody = String(sendJsons).data(using: String.Encoding.utf8);
        let task = URLSession.shared.dataTask(with: request as! URLRequest, completionHandler: {
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
        
    }
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
         var allowedCharacter = ""
        
        if textField.tag == 3 {
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@."
        }else if textField.tag == 4 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        }else{
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        }
        
        let aSet = CharacterSet(charactersIn:allowedCharacter).inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        
            return string == numberFiltered
        
    }
    
    // when click on textfield show the popup for select the subject
    func dropDown(_ textField:UITextField ) {
        
        
        
        let popoverContent = (self.storyboard?.instantiateViewController(withIdentifier: "DropDownViewController"))! as!        DropDownViewController
        popoverContent.delegate = self
        if textField.tag == 1 {
            popoverContent.data = cityName
        }else{
             popoverContent.data = countryName1
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
        
        if tmpTextField.tag == 2 {
            cityText.text = ""
            let temp = countryName1.index(of: countryText.text!)
            CountryName = countryText.text!
            CountryCode = countryCode[temp!]
            CountryPhoneCode = countryPhoneCode[temp!]
            self.fetchCityName()
        }
        
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if textField.tag == 1{
            self.dropDown(textField)
            return false
        }else if textField.tag == 2{
            self.dropDown(textField)
            return false
        }else{
            return true
        }
    }
    
    
    // show alert using this method
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
        }))
        present(refreshAlert, animated: true, completion: nil)
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

    
    
    func getOffLineData1(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        if (contactDB?.open())! {
            let querySQL = "SELECT * FROM Country "
            let results:FMResultSet? = contactDB?.executeQuery(querySQL,withArgumentsIn: nil)
            
            self.countryName1 = []
            self.countryCode = []
            self.countryPhoneCode = []
            
            while((results?.next()) == true){
                self.countryName1.append(results!.string(forColumn: "countryName")!)
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
    func saveDataOffLine1(){
        let databasePath = UserDefaults.standard.url(forKey: "DataBasePath")!
        let contactDB = FMDatabase(path: String(describing: databasePath))
        
        
        if (contactDB?.open())! {
            let querySQL = "DELETE FROM Country "
            _ = contactDB!.executeUpdate(querySQL, withArgumentsIn: nil)
            
            for i in 0 ..< countryName1.count {
                let insertSQL = "INSERT INTO Country (countryName, countryCodeId, phoneCountryCode) VALUES ('\(countryName1[i])', '\(countryCode[i])', '\(countryPhoneCode[i])')"
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

/*class func getCountryPhonceCode (_ country : String) -> String
 {
 var countryDictionary  = ["AF":"93",
 "AL":"355",
 "DZ":"213",
 "AS":"1",
 "AD":"376",
 "AO":"244",
 "AI":"1",
 "AG":"1",
 "AR":"54",
 "AM":"374",
 "AW":"297",
 "AU":"61",
 "AT":"43",
 "AZ":"994",
 "BS":"1",
 "BH":"973",
 "BD":"880",
 "BB":"1",
 "BY":"375",
 "BE":"32",
 "BZ":"501",
 "BJ":"229",
 "BM":"1",
 "BT":"975",
 "BA":"387",
 "BW":"267",
 "BR":"55",
 "IO":"246",
 "BG":"359",
 "BF":"226",
 "BI":"257",
 "KH":"855",
 "CM":"237",
 "CA":"1",
 "CV":"238",
 "KY":"345",
 "CF":"236",
 "TD":"235",
 "CL":"56",
 "CN":"86",
 "CX":"61",
 "CO":"57",
 "KM":"269",
 "CG":"242",
 "CK":"682",
 "CR":"506",
 "HR":"385",
 "CU":"53",
 "CY":"537",
 "CZ":"420",
 "DK":"45",
 "DJ":"253",
 "DM":"1",
 "DO":"1",
 "EC":"593",
 "EG":"20",
 "SV":"503",
 "GQ":"240",
 "ER":"291",
 "EE":"372",
 "ET":"251",
 "FO":"298",
 "FJ":"679",
 "FI":"358",
 "FR":"33",
 "GF":"594",
 "PF":"689",
 "GA":"241",
 "GM":"220",
 "GE":"995",
 "DE":"49",
 "GH":"233",
 "GI":"350",
 "GR":"30",
 "GL":"299",
 "GD":"1",
 "GP":"590",
 "GU":"1",
 "GT":"502",
 "GN":"224",
 "GW":"245",
 "GY":"595",
 "HT":"509",
 "HN":"504",
 "HU":"36",
 "IS":"354",
 "IN":"91",
 "ID":"62",
 "IQ":"964",
 "IE":"353",
 "IL":"972",
 "IT":"39",
 "JM":"1",
 "JP":"81",
 "JO":"962",
 "KZ":"77",
 "KE":"254",
 "KI":"686",
 "KW":"965",
 "KG":"996",
 "LV":"371",
 "LB":"961",
 "LS":"266",
 "LR":"231",
 "LI":"423",
 "LT":"370",
 "LU":"352",
 "MG":"261",
 "MW":"265",
 "MY":"60",
 "MV":"960",
 "ML":"223",
 "MT":"356",
 "MH":"692",
 "MQ":"596",
 "MR":"222",
 "MU":"230",
 "YT":"262",
 "MX":"52",
 "MC":"377",
 "MN":"976",
 "ME":"382",
 "MS":"1",
 "MA":"212",
 "MM":"95",
 "NA":"264",
 "NR":"674",
 "NP":"977",
 "NL":"31",
 "AN":"599",
 "NC":"687",
 "NZ":"64",
 "NI":"505",
 "NE":"227",
 "NG":"234",
 "NU":"683",
 "NF":"672",
 "MP":"1",
 "NO":"47",
 "OM":"968",
 "PK":"92",
 "PW":"680",
 "PA":"507",
 "PG":"675",
 "PY":"595",
 "PE":"51",
 "PH":"63",
 "PL":"48",
 "PT":"351",
 "PR":"1",
 "QA":"974",
 "RO":"40",
 "RW":"250",
 "WS":"685",
 "SM":"378",
 "SA":"966",
 "SN":"221",
 "RS":"381",
 "SC":"248",
 "SL":"232",
 "SG":"65",
 "SK":"421",
 "SI":"386",
 "SB":"677",
 "ZA":"27",
 "GS":"500",
 "ES":"34",
 "LK":"94",
 "SD":"249",
 "SR":"597",
 "SZ":"268",
 "SE":"46",
 "CH":"41",
 "TJ":"992",
 "TH":"66",
 "TG":"228",
 "TK":"690",
 "TO":"676",
 "TT":"1",
 "TN":"216",
 "TR":"90",
 "TM":"993",
 "TC":"1",
 "TV":"688",
 "UG":"256",
 "UA":"380",
 "AE":"971",
 "GB":"44",
 "US":"1",
 "UY":"598",
 "UZ":"998",
 "VU":"678",
 "WF":"681",
 "YE":"967",
 "ZM":"260",
 "ZW":"263",
 "BO":"591",
 "BN":"673",
 "CC":"61",
 "CD":"243",
 "CI":"225",
 "FK":"500",
 "GG":"44",
 "VA":"379",
 "HK":"852",
 "IR":"98",
 "IM":"44",
 "JE":"44",
 "KP":"850",
 "KR":"82",
 "LA":"856",
 "LY":"218",
 "MO":"853",
 "MK":"389",
 "FM":"691",
 "MD":"373",
 "MZ":"258",
 "PS":"970",
 "PN":"872",
 "RE":"262",
 "RU":"7",
 "BL":"590",
 "SH":"290",
 "KN":"1",
 "LC":"1",
 "MF":"590",
 "PM":"508",
 "VC":"1",
 "ST":"239",
 "SO":"252",
 "SJ":"47",
 "SY":"963",
 "TW":"886",
 "TZ":"255",
 "TL":"670",
 "VE":"58",
 "VN":"84",
 "VG":"284",
 "VI":"340"]
 if countryDictionary[country] != nil {
 return countryDictionary[country]!
 }
 
 else {
 return ""
 }
 
 }*/
