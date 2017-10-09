//
//  Contacts.swift
//  Xerung
//
//  Created by mityung on 15/02/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import Foundation
import Contacts


struct person {
    var name: String = ""
    var phone: String = ""
    var email: String = ""
    var selected:Int = 0
    var country:String = ""
}

var phoneBook = [person]()

func getPlayerFromContacts(){
    // make sure user hadn't previously denied access
    
    let status = CNContactStore.authorizationStatus(for: .contacts)
    if status == .denied || status == .restricted {
        // user previously denied, so tell them to fix that in settings
        return
    }
    
    // open it
    
    let store = CNContactStore()
    store.requestAccess(for: .contacts) { granted, error in
        guard granted else {
            DispatchQueue.main.async {
                // user didn't grant authorization, so tell them to fix that in settings
                print(error)
            }
            return
        }
        
        // get the contacts
        
        var contacts = [CNContact]()
        let request = CNContactFetchRequest(keysToFetch: [CNContactIdentifierKey as CNKeyDescriptor,CNContactPhoneNumbersKey as CNKeyDescriptor, CNContactFormatter.descriptorForRequiredKeys(for: .fullName)])
        do {
            try store.enumerateContacts(with: request) { contact, stop in
                contacts.append(contact)
            }
        } catch {
            print(error)
        }
        
        // do something with the contacts array (e.g. print the names)
        
        let formatter = CNContactFormatter()
        formatter.style = .fullName
        
        //var i:Int = 0
        print(contacts)
        for contact in contacts {
            var name = ""
            var number = ""
            
            if let temp = formatter.string(from: contact){
                print(temp)
                name = temp
            }
            
            if let temp = contact.phoneNumbers.first?.value  {
                print(temp.stringValue)
                number = temp.stringValue.trim()
            }
            
           phoneBook.append(person.init(name: name, phone: number, email: "", selected: 0,country: CountryName))
            
            //contactlist.insert(formatter.stringFromContact(contact)!, atIndex: (fbNames.count + i))
            // i += 1;
        }
        
    }
    
   
}

func getContactDetails(_ successBlock:@escaping ( _ response:[person] )->Void){
    let status = CNContactStore.authorizationStatus(for: .contacts)
    if status == .denied || status == .restricted {
        // user previously denied, so tell them to fix that in settings
        return
    }
    
    // open it
    
    let store = CNContactStore()
    store.requestAccess(for: .contacts) { granted, error in
        guard granted else {
            DispatchQueue.main.async {
                // user didn't grant authorization, so tell them to fix that in settings
                print(error)
            }
            return
        }
        
        // get the contacts
        
        var contacts = [CNContact]()
        let request = CNContactFetchRequest(keysToFetch: [CNContactIdentifierKey as CNKeyDescriptor,CNContactPhoneNumbersKey as CNKeyDescriptor, CNContactFormatter.descriptorForRequiredKeys(for: .fullName)])
        do {
            try store.enumerateContacts(with: request) { contact, stop in
                contacts.append(contact)
            }
        } catch {
            print(error)
        }
        
        // do something with the contacts array (e.g. print the names)
        
        let formatter = CNContactFormatter()
        formatter.style = .fullName
        phoneBook = []
        //var i:Int = 0
       // print(contacts)
        for contact in contacts {
            var name = ""
            var number = ""
            
            if let temp = formatter.string(from: contact){
               // print(temp)
                name = temp
            }
            
            if let temp = contact.phoneNumbers.first?.value  {
               // print(temp.stringValue)
                number = temp.stringValue.trim()
            }
            
            phoneBook.append(person.init(name: name, phone: number, email: "", selected: 0 ,country: CountryName))
            
            //contactlist.insert(formatter.stringFromContact(contact)!, atIndex: (fbNames.count + i))
            // i += 1;
        }
        successBlock(phoneBook)
    }
}

