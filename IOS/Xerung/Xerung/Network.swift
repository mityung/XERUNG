
//
//  Network.swift
//  Xerung
//
//  Created by mityung on 16/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import Foundation
import SwiftyJSON


class DataProvider: NSObject {
    
    class var sharedInstance:DataProvider {
        struct Singleton {
            static let instance = DataProvider()
        }
        return Singleton.instance
    }
    //let serverUrl  = "http://192.168.1.120:8084/MIPHONEDIRGATEKEEPER/webresources/gatekeeper/IOS";
    let serverUrl  = "http://www.xerung.com/MIPHONEDIRGATEKEEPER/webresources/gatekeeper/IOS";
    let msgURL  = "https://www.medicosa.com/SMSService/rs/sms";
    
    
    
    
    func getServerData(_ sendJson:[String:String], path:String, successBlock:@escaping (_ response: JSON )->Void , errorBlock: @escaping (_ error: NSError) -> Void ){
        
        let myUrl = URL(string: serverUrl);
        let request = NSMutableURLRequest(url:myUrl!);
        request.httpMethod = "POST";
        request.setValue("text/plain",forHTTPHeaderField : "Content-Type");
        request.setValue("Content-Language",forHTTPHeaderField : "en-US");
    
        let sendJsons = "\(path)^{"+String(describing: sendJson).replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "")+"}";
        
        request.httpBody = String(sendJsons).data(using: String.Encoding.utf8);
        let task = URLSession.shared.dataTask(with: request as URLRequest, completionHandler: {
            data, response, error in
            
            
            
            if error != nil
            {
                errorBlock(error! as NSError)
                
            }
            
            
            
            
            do {
                if path == "FetchGroupDelta"  || path == "GroupSearch" {
                    
                    if data != nil {
                    
                        var responseString =  NSString(data: data!, encoding: String.Encoding.utf8.rawValue)
                        let count = responseString?.substring(to: 4)
                        directorCount = Int((count?.components(separatedBy: " ")[0])!)!
                        responseString = responseString?.substring(from: 4) as NSString?
                        let objectData = responseString!.data(using: String.Encoding.utf8.rawValue)
                        let json = try JSONSerialization.jsonObject(with: objectData!, options: JSONSerialization.ReadingOptions.mutableContainers)
                        let myjson = JSON(json)
                        successBlock(myjson)
                    }else{
                        errorBlock(error as! NSError)
                    }
                }else {
                     if data != nil {
                        let myJSON = try JSONSerialization.jsonObject(with: data!, options: []) as! [String: AnyObject]
                        let json = JSON(myJSON)
                        successBlock(json)
                     }else{
                        errorBlock(error as! NSError)
                    }
                }
            }
            catch let error as NSError {
                
                print(error.localizedDescription)
                errorBlock(error)
            }
            
        }) 
        task.resume()
    }
    
    
    func getServerData2(_ sendJson:[String:String], path:String, successBlock:@escaping (_ response: JSON )->Void , errorBlock: @escaping (_ error: NSError) -> Void ){
        
        let myUrl = URL(string: serverUrl);
        let request = NSMutableURLRequest(url:myUrl!);
        request.httpMethod = "POST";
        request.setValue("text/plain",forHTTPHeaderField : "Content-Type");
        request.setValue("Content-Language",forHTTPHeaderField : "en-US");
        
        let sendJsons = "\(path)^{"+String(describing: sendJson).replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "")+"}";
        
        request.httpBody = String(sendJsons).data(using: String.Encoding.utf8);
        let task = URLSession.shared.dataTask(with: request as URLRequest
            
            , completionHandler: {
            data, response, error in
            
            if error != nil
            {
                errorBlock(error! as NSError)
                
            }
            
            
            do {
                if path == "FetchGroupDelta" || path == "GroupSearch" || path == "fetchINV" || path == "RegLogin" || path == "DeleteMember" || path == "fetchCity" || path == "CountryList" || path  == "Mibackupsend"{
                    
                    if data != nil {
                    
                        let responseString =  NSString(data: data!, encoding: String.Encoding.utf8.rawValue)
                        let objectData = responseString!.data(using: String.Encoding.utf8.rawValue)
                        let json = try JSONSerialization.jsonObject(with: objectData!, options: JSONSerialization.ReadingOptions.mutableContainers)
                        let myjson = JSON(json)
                        successBlock(myjson)
                    }else{
                            errorBlock(error as! NSError)
                        }
                    
                    
                }else{
                
                if data != nil {
                        
                    let myJSON = try JSONSerialization.jsonObject(with: data!, options: []) as! [String: AnyObject]
                    let json = JSON(myJSON)
                    successBlock(json)
                }else{
                    errorBlock(error as! NSError)
                }
                }
            }
            catch let error as NSError {
                
                print(error.localizedDescription)
                errorBlock(error)
            }
            
        }) 
        task.resume()
    }

    
  
    
    }
