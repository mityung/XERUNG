//
//  EditDirectoryViewController.swift
//  Xerung
//
//  Created by mityung on 02/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit

class EditDirectoryViewController: UIViewController ,UINavigationControllerDelegate, UIImagePickerControllerDelegate , UITextFieldDelegate ,UITextViewDelegate{

    @IBOutlet var view1: UIView!
    @IBOutlet var groupNameLabel: UILabel!
    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var submitButton: UIButton!
    @IBOutlet weak var directoryName: UITextField!
    
    @IBOutlet var aboutDirectory: UITextView!
    
     var imagePicker = UIImagePickerController()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "Edit Directory"
        
        userImage.layer.cornerRadius = userImage.frame.width/2
        userImage.layer.masksToBounds = true
        userImage.layer.borderWidth = 1
        userImage.layer.borderColor = themeColor.cgColor
        
        /*submitButton.layer.cornerRadius = 5
        submitButton.layer.borderWidth = 1
        submitButton.layer.borderColor = themeColor.cgColor*/

        aboutDirectory.text = selectedGroupAbout
        aboutDirectory.tag = 1
        directoryName.text =  selectedGroupCalled
        directoryName.tag = 2
        directoryName.autocapitalizationType = .sentences
        
        aboutDirectory.delegate = self
        directoryName.delegate = self
        
        groupNameLabel.text = selectedGroupName
        
        if selectedGroupPhoto != "" && selectedGroupPhoto != "0" {
            if  let imageData = Data(base64Encoded: selectedGroupPhoto , options: NSData.Base64DecodingOptions.ignoreUnknownCharacters) {
                let DecodedImage = UIImage(data: imageData)
                userImage.image = DecodedImage
            }
        }else{
             userImage.image = UIImage(named: "defaultDirectory")
        }
        
       // submitButton.addTarget(self, action: #selector(self.submitData(_:)), for: UIControlEvents.touchUpInside)
        
        view1.dropShadow()
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(self.submitData(_:)))
        

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
        
        selectedGroupPhoto = imageData!.base64EncodedString(options: [])
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

    
    
    func submitData(_ sender:UIButton){
        self.view.endEditing(true)
        
        if self.directoryName.text?.removingWhitespaces().characters.count == 0 || self.aboutDirectory.text?.removingWhitespaces().characters.count == 0 {
            showAlert("Alert", message: "Please enter values in all fields.")
            return
        }
        
        
        if Reachability.isConnectedToNetwork() {
            
        let sendJson: [String: String] = [
            "PGROUPID":selectedGroupId,
            "PUID":userID,
            "PGROUPNAME": "",
            "PMADEBY":"",
            "PDESC":self.aboutDirectory.text!,
            "PSTATEID":"0",
            "PGROUPTYPEVAR":"",
            "PTAGNAME":self.directoryName.text!,
            "PGROUPPHOTO":selectedGroupPhoto,
            "PCHANGEBYPHONENO":mobileNo,
            "AFLAG":"1",
            "AGROUPMEMBERLIST":""
        ]

            startLoader(view: self.view)
        
        DataProvider.sharedInstance.getServerData2(sendJson, path: "GroupService", successBlock: { (response) in
            print(response)
            stopLoader()
            if response["STATUS"] == "SUCCESS"{
                self.showAlert("Confirmation", message: "Directory updated successfully.")
            }
            
        }) { (error) in
            stopLoader()
            print(error)
        }
        }else{
            showAlert("Alert", message: "No internet connectivity.")
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
            
            if textField.tag == 1 {
                return numberOfChars < 101
            }else if textField.tag == 2 {
                return numberOfChars < 26
            }else {
                return numberOfChars < 51
            }
            
        }else{
            return string == numberFiltered
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

    
    
    func showAlert(_ title:String,message:String){
        let refreshAlert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action: UIAlertAction!) in
            if title == "Confirmation" {
                self.navigationController?.popToRootViewController(animated: true)
            }

        }))
        present(refreshAlert, animated: true, completion: nil)
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
