//
//  Loader.swift
//  Xerung
//
//  Created by Mac on 25/03/17.
//  Copyright © 2017 mityung. All rights reserved.
//

import Foundation
import UIKit

public class Loader {
    
    var containerView = UIView()
    var progressView = UIView()
    var activityIndicator = UIActivityIndicatorView()
    
    public class var shared: Loader {
        struct Static {
            static let instance: Loader = Loader()
        }
        return Static.instance
    }
    
    public func showProgressView(view: UIView) {
        containerView.frame = view.frame
        containerView.center = view.center
        containerView.backgroundColor = UIColor(hex: 0xffffff, alpha: 0.3)
        
        
        progressView.frame = CGRect(x: 0, y: 0, width: 80, height: 80)
        progressView.center = view.center
        progressView.backgroundColor = UIColor(hex: 0x444444, alpha: 0.7)
        progressView.clipsToBounds = true
        progressView.layer.cornerRadius = 10
        
        activityIndicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        activityIndicator.activityIndicatorViewStyle = .whiteLarge
       // activityIndicator.center = CGPointMake(progressView.bounds.width / 2, progressView.bounds.height / 2)
        activityIndicator.center = CGPoint(x: progressView.bounds.width / 2, y: progressView.bounds.height / 2)
        
        progressView.addSubview(activityIndicator)
        containerView.addSubview(progressView)
        view.addSubview(containerView)
        
        activityIndicator.startAnimating()
    }
    
    public func hideProgressView() {
        activityIndicator.stopAnimating()
        containerView.removeFromSuperview()
    }
}

func startLoader(view:UIView){
    DispatchQueue.main.async(execute: {
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        Loader.shared.showProgressView(view: view);
    })
}

func stopLoader(){
    DispatchQueue.main.async(execute: {
        UIApplication.shared.isNetworkActivityIndicatorVisible = false
        Loader.shared.hideProgressView()
    })
}

extension UIColor {
    
    convenience init(hex: UInt32, alpha: CGFloat) {
        let red = CGFloat((hex & 0xFF0000) >> 16)/256.0
        let green = CGFloat((hex & 0xFF00) >> 8)/256.0
        let blue = CGFloat(hex & 0xFF)/256.0
        
        self.init(red: red, green: green, blue: blue, alpha: alpha)
    }
}
