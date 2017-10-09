//
//  AppDelegate.swift
//  Xerung
//
//  Created by mityung on 14/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import UIKit
import Contacts
import UserNotifications


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    var contactStore = CNContactStore()


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        UIApplication.shared.statusBarStyle = .lightContent
        
        let barAppearace = UIBarButtonItem.appearance()
        barAppearace.setBackButtonTitlePositionAdjustment(UIOffsetMake(0, -60), for:UIBarMetrics.default)

        
         UINavigationBar.appearance().barTintColor = UIColor(red: 50.0/255.0, green: 194.0/255.0, blue: 77.0/255.0, alpha: 0.8)
         UINavigationBar.appearance().tintColor = UIColor.white
         UINavigationBar.appearance().titleTextAttributes = [NSForegroundColorAttributeName : UIColor.white]
        
        UITabBarItem.appearance().setTitleTextAttributes([NSFontAttributeName: UIFont(name: "helvetica", size: 12)!], for: .normal)
        
        
       /* self.navigationController?.navigationBar.barTintColor = UIColor(red: 243.0/255.0, green: 100.0/255.0, blue: 36.0/255.0, alpha: 1.0)
        self.navigationController?.navigationBar.tintColor = UIColor.whiteColor()
        self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName : UIColor.whiteColor()]
        icolor = UIColor(red: 243.0/255.0, green: 100.0/255.0, blue: 36.0/255.0, alpha: 1.0)*/
        
        
        registerForPushNotifications(application)
        
        
        if let notification = launchOptions?[UIApplicationLaunchOptionsKey.remoteNotification] as? NSDictionary {
            // 2
            UIApplication.shared.applicationIconBadgeNumber = 0
            let aps = notification["aps"] as! [String: AnyObject]
           // NotificationAlert = aps["alert"] as! String
            
            
        }

        
        
        createDatabase()
        
        return true
    }

    func registerForPushNotifications(_ application: UIApplication) {
        /*let notificationSettings = UIUserNotificationSettings(
         types: [.badge, .sound, .alert], categories: nil)
         application.registerUserNotificationSettings(notificationSettings)*/
        
        if #available(iOS 10, *) {
            UNUserNotificationCenter.current().requestAuthorization(options:[.badge, .alert, .sound]){ (granted, error) in }
            application.registerForRemoteNotifications()
        }
            // iOS 9 support
        else  {
            UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [.badge, .sound, .alert], categories: nil))
            UIApplication.shared.registerForRemoteNotifications()
        }
        
    }
    
    
    func application(_ application: UIApplication, didRegister notificationSettings: UIUserNotificationSettings) {
        if notificationSettings.types != UIUserNotificationType() {
            application.registerForRemoteNotifications()
        }
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenChars = (deviceToken as NSData).bytes.bindMemory(to: CChar.self, capacity: deviceToken.count)
        var tokenString = ""
        
        for i in 0..<deviceToken.count {
            tokenString += String(format: "%02.2hhx", arguments: [tokenChars[i]])
        }
        
        UserDefaults.standard.setValue(tokenString, forKey: "deviceToken")
        UserDefaults.standard.synchronize()
        
        print("deviceToken \(tokenString)")
        
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Failed to register:", error)
    }
    
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        print(notification.request.content.userInfo)
    }
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        //Handle the notification
        
        print(response.notification.request.content.userInfo)
    }
    
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        print(userInfo)
        UIApplication.shared.applicationIconBadgeNumber = 0
        let state: UIApplicationState = UIApplication.shared.applicationState
        
        if state == .inactive {
            print(userInfo)
            
            
        }
        
    }
    
    
    
    func application(_ application: UIApplication, handleActionWithIdentifier identifier: String?, forRemoteNotification userInfo: [AnyHashable: Any], completionHandler: @escaping () -> Void) {
        // 1
        let aps = userInfo["aps"] as! [String: AnyObject]
        //NotificationAlert = aps["alert"] as! String
        
        
        // 2
    }
    
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    
    func createDatabase(){
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
        let databasePath = documentsURL.appendingPathComponent("Xerung.sqlite")
        
        let filemgr = FileManager.default
        print(databasePath)
        
        UserDefaults.standard.set(databasePath, forKey:"DataBasePath")
        UserDefaults.standard.synchronize()
        
        
        if !filemgr.fileExists(atPath: String(describing: databasePath)) {
            
            let contactDB = FMDatabase(path: String(describing: databasePath))
            
            if contactDB == nil {
                print("Error: \(contactDB?.lastErrorMessage())")
            }
            
            
            if (contactDB?.open())! {
                let sql_stmt = "CREATE TABLE IF NOT EXISTS Directory (ID INTEGER PRIMARY KEY AUTOINCREMENT , MADEBYPHONENO TEXT , MADEBYNAME TEXT, GROUPID TEXT, GROUPNAME TEXT, TAGNAME TEXT, DESCRITION TEXT, GROUPPHOTO TEXT, MEMBERCOUNT TEXT, ADMINFLAG TEXT, GROUPACCESSTYPE TEXT)"
                if !(contactDB?.executeStatements(sql_stmt))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
                let sql_stmt1 = "CREATE TABLE IF NOT EXISTS DirectoryMember (ID INTEGER PRIMARY KEY AUTOINCREMENT , GroupData TEXT , GroupId TEXT)"
                if !(contactDB?.executeStatements(sql_stmt1))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
                               
                let sql_stmt2 = "CREATE TABLE IF NOT EXISTS PublicDirectory (ID INTEGER PRIMARY KEY AUTOINCREMENT , TAGNAME TEXT ,DESCRITION TEXT , GROUPACCESSTYPE TEXT, GROUPNAME TEXT, GROUPPHOTO TEXT,GROUPID TEXT)"
                if !(contactDB?.executeStatements(sql_stmt2))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
                
                let sql_stmt3 = "CREATE TABLE IF NOT EXISTS PublicDirectoryMember (ID INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT ,phone TEXT , address TEXT, cityName TEXT,groupId TEXT)"
                if !(contactDB?.executeStatements(sql_stmt3))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
               let sql_stmt4 = "CREATE TABLE IF NOT EXISTS GroupData (ID INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , number TEXT , flag TEXT , address TEXT,UID TEXT,bloodGroup TEXT,city TEXT,profession TEXT,Date TEXT)"
                if !(contactDB?.executeStatements(sql_stmt4))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
                let sql_stmt5 = "CREATE TABLE IF NOT EXISTS City (ID INTEGER PRIMARY KEY AUTOINCREMENT , cityId TEXT , cityName TEXT)"
                if !(contactDB?.executeStatements(sql_stmt5))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
                let sql_stmt6 = "CREATE TABLE IF NOT EXISTS Country (ID INTEGER PRIMARY KEY AUTOINCREMENT , countryName TEXT , countryCodeId TEXT, phoneCountryCode TEXT)"
                if !(contactDB?.executeStatements(sql_stmt6))! {
                    print("Error: \(contactDB?.lastErrorMessage())")
                }
                
                
                
                contactDB?.close()
            } else {
                print("Error: \(contactDB?.lastErrorMessage())")
            }
        }
    }

    
    // MARK: Custom functions
    
    class func getAppDelegate() -> AppDelegate {
        return UIApplication.shared.delegate as! AppDelegate
    }
    
    
    func showMessage(_ message: String) {
        let alertController = UIAlertController(title: "Alert", message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        let dismissAction = UIAlertAction(title: "OK", style: UIAlertActionStyle.default) { (action) -> Void in
        }
        
        alertController.addAction(dismissAction)
        
        let pushedViewControllers = (self.window?.rootViewController as! UINavigationController).viewControllers
        let presentedViewController = pushedViewControllers[pushedViewControllers.count - 1]
        
        presentedViewController.present(alertController, animated: true, completion: nil)
    }
    
    
    func requestForAccess(_ completionHandler: @escaping (_ accessGranted: Bool) -> Void) {
        let authorizationStatus = CNContactStore.authorizationStatus(for: CNEntityType.contacts)
        
        switch authorizationStatus {
        case .authorized:
            self.showMessage("Contact save Successfully.")
            completionHandler(true)
            
        case .denied, .notDetermined:
            self.contactStore.requestAccess(for: CNEntityType.contacts, completionHandler: { (access, accessError) -> Void in
                if access {
                    self.showMessage("Contact save Successfully.")
                    completionHandler(access)
                }
                else {
                    if authorizationStatus == CNAuthorizationStatus.denied {
                        DispatchQueue.main.async(execute: { () -> Void in
                            let message = "\(accessError!.localizedDescription)\n\nPlease allow the app to access your contacts through the Settings."
                            self.showMessage(message)
                        })
                    }
                }
            })
            
        default:
            completionHandler(false)
        }
    }


}

