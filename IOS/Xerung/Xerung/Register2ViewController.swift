//
//  Register2ViewController.swift
//  Xerung
//
//  Created by Mac on 30/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class Register2ViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet var submitButton: UIButton!
    @IBOutlet var otpText: UITextField!
    @IBOutlet var emailText: UITextField!
    @IBOutlet var fullNameText: UITextField!
    @IBOutlet var view2: UIView!
    @IBOutlet var view1: UIView!
    @IBOutlet var fullNameView: UIView!
    
    @IBOutlet var otpView: UIView!
    @IBOutlet var emailView: UIView!
    @IBOutlet var numberLabel: UILabel!
    var mobileNumber:String!
    
    var otp:String!
    
    override func viewDidLoad() {
        super.viewDidLoad()

       mobileNumber = mobileNo
        submitButton.layer.cornerRadius = 5
        submitButton.layer.masksToBounds = true
        submitButton.tintColor = UIColor.white
        submitButton.backgroundColor = themeColor
        
        view1.dropShadow()
        view2.dropShadow()
        
        otpText.delegate = self
        otpText.keyboardType = .numberPad
        otpText.tag = 3
        emailText.delegate = self
        emailText.keyboardType = .emailAddress
        emailText.tag = 2
        fullNameText.delegate = self
        fullNameText.tag = 1
        numberLabel.text = mobileNo
        submitButton.addTarget(self, action: #selector(self.sendOTPtoServer), for: UIControlEvents.touchUpInside)
        
        let numberToolbar: UIToolbar = UIToolbar()
        numberToolbar.barStyle = UIBarStyle.default
        numberToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.Apply))
            
        ]
        numberToolbar.sizeToFit()
        otpText.inputAccessoryView = numberToolbar
        
    }
    
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
       
        var allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.,/ "
        
        if textField.tag == 1 {
            allowedCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
        }else if textField.tag == 2 {
            allowedCharacter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?.@_"
        }else if textField.tag == 3 {
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
                return numberOfChars < 31
            }else if textField.tag == 2 {
                return numberOfChars < 50
            }else if textField.tag == 3 {
                return numberOfChars < 6
            }else {
                return numberOfChars < 51
            }
            
        }else{
            return string == numberFiltered
        }
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField.tag == 1 {
            fullNameView.backgroundColor =  themeColor
        }else if textField.tag == 2 {
            emailView.backgroundColor = themeColor
        }else if textField.tag == 3 {
            otpView.backgroundColor = themeColor
        }
    }
    
    @available(iOS 10.0, *)
    func textFieldDidEndEditing(_ textField: UITextField, reason: UITextFieldDidEndEditingReason) {
        if textField.tag == 1 {
            fullNameView.backgroundColor =  UIColor.lightGray
        }else if textField.tag == 2 {
            emailView.backgroundColor =  UIColor.lightGray
        }else if textField.tag == 3 {
            otpView.backgroundColor =  UIColor.lightGray
        }
    }
    
    
    func Apply () {
        self.view.endEditing(true)
    }
    
    @IBAction func editButton(_ sender: Any) {
         self.dismiss(animated: true, completion: nil)
    }

    @IBAction func backButton(_ sender: AnyObject) {
        self.dismiss(animated: true, completion: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    func sendOTPtoServer(_ sender: AnyObject) {
        
        self.Apply()
        
        if fullNameText.text?.trim().characters.count == 0 {
            self.showAlert("Alert", message: "Please fill name.")
            return
        }
        
        
        if emailText.text?.trim().characters.count == 0 {
            self.showAlert("Alert", message: "Please fill email address.")
            return
        }else if self.isValidEmail(testStr: emailText.text!) == false {
            self.showAlert("Alert", message: "Please fill valid email address.")
            return
        }
        
        if otpText.text?.trim().characters.count == 0 {
            self.showAlert("Alert", message: "Please fill OTP.")
            return
        }
        
        if otpText.text! != OTPText {
            self.showAlert("Alert", message: "OTP is not matched.")
            return
        
        }
        
        
        let sendJson: [String: String] = [
            "PPHONENUMBER":(CountryPhoneCode + mobileNumber) ,
            "PEMAIL": emailText.text!,
            "PNAME":fullNameText.text!.trim(),
            "PADDRESS": "",
            "PCITYID":"0",
            "PSTATEID":"0",
            "PCOUNTRYCODEID":CountryPhoneCode,
            "PCOUNTRYNAME":CountryName,
            "POTPID":otpText.text!,
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
                
                if response["STATUS"].stringValue == "3" {
                    self.showAlert("Alert", message: "Email Address already register.")
                }else{
                    userID = String(response["UID"].intValue)
                    self.fetchProfile(userID)
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
                self.nextView()
                
            }) { (error) in
                print(error)
                stopLoader()
            }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
        }
    }
    
    func isValidEmail(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
    
        let emailRegEx = "^(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?(?:(?:(?:[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+(?:\\.[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+)*)|(?:\"(?:(?:(?:(?: )*(?:(?:[!#-Z^-~]|\\[|\\])|(?:\\\\(?:\\t|[ -~]))))+(?: )*)|(?: )+)\"))(?:@)(?:(?:(?:[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)(?:\\.[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)*)|(?:\\[(?:(?:(?:(?:(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))\\.){3}(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))))|(?:(?:(?: )*[!-Z^-~])*(?: )*)|(?:[Vv][0-9A-Fa-f]+\\.[-A-Za-z0-9._~!$&'()*+,;=:]+))\\])))(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?$"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        let result = emailTest.evaluate(with: testStr)
        return result
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
            
           // self.performSegue(withIdentifier: "register", sender: nil)
        })
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    // show alert using this method
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
        }))
        present(refreshAlert, animated: true, completion: nil)
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
