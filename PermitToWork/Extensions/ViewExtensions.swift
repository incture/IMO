//
//  ViewExtensions.swift
//  Task-Management
//
//  Created by Prakash on 24/06/19.
//  Copyright © 2019 SAP. All rights reserved.
//

import UIKit

extension UIView{
    
    func activityStartAnimating(activityColor: UIColor) {
        let backgroundView = UIView()
        backgroundView.frame = CGRect.init(x: 0, y: 0, width: self.bounds.width, height: self.bounds.height)
        backgroundView.backgroundColor = UIColor.clear
        backgroundView.tag = 475647
        
        var activityIndicator: UIActivityIndicatorView = UIActivityIndicatorView()
        activityIndicator = UIActivityIndicatorView(frame: CGRect.init(x: 0, y: 0, width: 40, height: 40))
        activityIndicator.center = self.center
        activityIndicator.hidesWhenStopped = true
        activityIndicator.style = UIActivityIndicatorView.Style.gray
        activityIndicator.color = activityColor
        activityIndicator.startAnimating()
        self.isUserInteractionEnabled = false
        UIApplication.shared.keyWindow!.addSubview(activityIndicator)
        activityIndicator.bringSubviewToFront(self)
   //     UIApplication.shared.isNetworkActivityIndicatorVisible = true
        backgroundView.addSubview(activityIndicator)
        self.addSubview(backgroundView)
        
    }
    
    func activityStopAnimating() {
        DispatchQueue.main.async {
            if let background = self.viewWithTag(475647){
                background.removeFromSuperview()
            }
            self.isUserInteractionEnabled = true
        }
    }
    func statusBar(view:UIView) -> UIView{
        let statusbarView = UIView()

         if #available(iOS 13.0, *) {
            let app = UIApplication.shared
            let statusBarHeight: CGFloat = app.statusBarFrame.size.height

            statusbarView.backgroundColor = UIColor(red: 1/255.0, green: 38/255.0, blue: 90/255.0, alpha: 1.0)
            view.addSubview(statusbarView)

            statusbarView.translatesAutoresizingMaskIntoConstraints = false
            statusbarView.heightAnchor
                .constraint(equalToConstant: statusBarHeight).isActive = true
            statusbarView.widthAnchor
                .constraint(equalTo: view.widthAnchor, multiplier: 1.0).isActive = true
            statusbarView.topAnchor
                .constraint(equalTo: view.topAnchor).isActive = true
            statusbarView.centerXAnchor
                .constraint(equalTo: view.centerXAnchor).isActive = true
        }
        return statusbarView

    }
}
extension UIViewController {
    func showAlert(message: String, title: String = "") {
        DispatchQueue.main.async {
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let OKAction = UIAlertAction(title: "Ok", style: .default, handler: nil)
        alertController.addAction(OKAction)
        self.present(alertController, animated: true, completion: nil)
        }
}
}
extension UIApplication {


class var statusBarBackgroundColor: UIColor? {
    get {
        return statusBarUIView?.backgroundColor
    } set {
        statusBarUIView?.backgroundColor = newValue
    }
}

class var statusBarUIView: UIView? {
    if #available(iOS 13.0, *) {
        let tag = 987654321

        if let statusBar = UIApplication.shared.keyWindow?.viewWithTag(tag) {
            return statusBar
        }
        else {
            let statusBarView = UIView(frame: UIApplication.shared.statusBarFrame)
            statusBarView.tag = tag

            UIApplication.shared.keyWindow?.addSubview(statusBarView)
            return statusBarView
        }
    } else {
        if responds(to: Selector(("statusBar"))) {
            return value(forKey: "statusBar") as? UIView
        }
    }
    return nil
}}
extension UITextField{
   @IBInspectable var placeHolderColor: UIColor? {
        get {
            return self.placeHolderColor
        }
        set {
            self.attributedPlaceholder = NSAttributedString(string:self.placeholder != nil ? self.placeholder! : "", attributes:[NSAttributedString.Key.foregroundColor: newValue!])
        }
    }
}

