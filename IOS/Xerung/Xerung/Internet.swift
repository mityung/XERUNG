//
//  Internet.swift
//  Xerung
//
//  Created by mityung on 06/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import Foundation
import SystemConfiguration

open class Reachability {
    class func isConnectedToNetwork() -> Bool {
        var zeroAddress = sockaddr_in()
        zeroAddress.sin_len = UInt8(MemoryLayout.size(ofValue: zeroAddress))
        zeroAddress.sin_family = sa_family_t(AF_INET)
        let defaultRouteReachability = withUnsafePointer(to: &zeroAddress) {
            $0.withMemoryRebound(to: sockaddr.self, capacity: 1) {zeroSockAddress in
                SCNetworkReachabilityCreateWithAddress(nil, zeroSockAddress)
            }
        }
        var flags = SCNetworkReachabilityFlags()
        if !SCNetworkReachabilityGetFlags(defaultRouteReachability!, &flags) {
            return false
        }
        let isReachable = (flags.rawValue & UInt32(kSCNetworkFlagsReachable)) != 0
        let needsConnection = (flags.rawValue & UInt32(kSCNetworkFlagsConnectionRequired)) != 0
        return (isReachable && !needsConnection)
    }
}

class DataString: NSObject {
    
    
    var Number: String      = String()
    
    class func encodeText(_ plainString:String) -> String{
        let plainData = (plainString as NSString).data(using: String.Encoding.utf8.rawValue)
        let base64String = plainData!.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
        //print(base64String) // bXkgcGxhbmkgdGV4dA==
        return base64String;
    }
    
    class func decodeText(_ base64String:String) -> String{
        let decodedData = Data(base64Encoded: base64String, options:NSData.Base64DecodingOptions(rawValue: 0))
        let decodedString = NSString(data: decodedData!, encoding: String.Encoding.utf8.rawValue)
        // print(decodedString) // my plain data
        return decodedString as! String;
    }
    
    class func encodeImage(_ base64String:String)-> Data {
        var decodedData:Data;
        do{
            decodedData = Data(base64Encoded: base64String, options: NSData.Base64DecodingOptions.ignoreUnknownCharacters)!
            return decodedData;
        }catch let error as NSError {
            return decodedData;
        }
        
    }
    
    
    //    class func decodeImageBase64(imgString : UIImage) -> String {
    //        let imageData = UIImagePNGRepresentation(imgString);
    //        let base64String = imageData!.base64EncodedStringWithOptions(.Encoding64CharacterLineLength);
    //        return base64String
    //    }
}
