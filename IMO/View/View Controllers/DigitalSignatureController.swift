//
//  DigitalSignatureController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 10/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit


class DigitalSignatureController: UIViewController {

    @IBOutlet var SignatureView: YPDrawSignatureView!
    @IBOutlet var legalDisclaimerView: UITextView!
    @IBOutlet var floatingButton: UIButton!
    
    
    var csrfToken : String?
    var isUpdate : Bool?
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.gray)
    var dashBoardSender : DashBoard?
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
        floatingButton.layer.cornerRadius = 30
        let htmlText = "<ul style = 'color : #868686 ; font-size : 14;'><li><i>All Commercial Terms are to be in accordance to Murphy Oil MSA and originating Work Order.</i></li><li><i>Field/Wellsite Operations Stamp is a confirmation of services/materials received only.</i></li></ul>"
        let attrStr = try! NSAttributedString(
            data: htmlText.data(using: String.Encoding.unicode, allowLossyConversion: true)!,
            options: [ .documentType: NSAttributedString.DocumentType.html],
            documentAttributes: nil)
        legalDisclaimerView.attributedText = attrStr
        

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "Digital Signature"
        let doneItem = UIBarButtonItem.init(title: "Done", style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.saveSignature))
        let backItem = UIBarButtonItem.init(title: "Back", style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        navigationItem.rightBarButtonItem = doneItem
        navigationItem.leftBarButtonItem = backItem
        
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }

    @IBAction func clearSignature(_ sender: UIButton) {
        // This is how the signature gets cleared
        self.SignatureView.clear()
    }
    
    
    //501
    func getCSRFforSignaturePost(signatureImage : UIImage)
    {
        let header = [ "x-csrf-token" : "fetch"]
        
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/Dft_SignatureSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async{
                    if let response = response as? HTTPURLResponse{
                        if let xcsrf = response.allHeaderFields["x-csrf-token"] as? String {
                            
                            self!.csrfToken = xcsrf
                            print(xcsrf)
                        }
                    }
                    if self!.csrfToken != nil{
                        self!.postDigitalSignature(signatureImage : signatureImage)
                    }
                    else{
                        
                        let alertController = UIAlertController.init(title: "", message:"CSRF fetch failed.Signature was not updated.Please retry." , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self!.present(alertController, animated: true, completion: nil)
                    }
                }
            }
            else{
                DispatchQueue.main.async{
                    let alertController = UIAlertController.init(title: "", message:"Internet Connection is not available!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }        }
        task.resume()
 
    }
  
    
    //201 success
    func postDigitalSignature(signatureImage : UIImage)
    {
        
        let imageData:NSData = signatureImage.jpegData(compressionQuality: 0.3)! as NSData
        let imageSize = imageData.length
        let value = ByteCountFormatter.string(fromByteCount: Int64(imageSize), countStyle: .file)
        print(value)
        let strBase64:String = imageData.base64EncodedString(options: .lineLength64Characters)
        let attachmentType = "image/jpeg"
        let approverId = UserDefaults.standard.string(forKey: "id")!
        let postData = [
            "ApproverId": approverId,
            "Mimetype": attachmentType,
            "DigitalSignature": strBase64
        ]
        
        print(postData)
        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
        
        
        let urlString = "\(BaseUrl.apiURL)/com.OData.dest/Dft_SignatureSet"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.allHTTPHeaderFields = header
        urlRequest.httpMethod = "post"
        do{
            let requestBody = try JSONSerialization.data(withJSONObject: postData, options: .fragmentsAllowed)
            urlRequest.httpBody = requestBody
        }
        catch{
            print("error in creating the data object from json")
        }
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
            
            guard self != nil else { return }
            if ConnectionCheck.isConnectedToNetwork() {
                DispatchQueue.main.async{
                    self!.loaderStop()
                    UserDefaults.standard.set(strBase64, forKey: "digisign")
                    UserDefaults.standard.synchronize()
                    let alertController = UIAlertController.init(title: "", message:"Signature updated!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                        self!.decision()})
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                    //route to task Screen
                }
            }
            else{
                DispatchQueue.main.async{
                    let alertController = UIAlertController.init(title: "", message:"Internet Connection is not available!" , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: { action in
                        self!.decision()})
                    alertController.addAction(okAction)
                    self!.present(alertController, animated: true, completion: nil)
                }
            }        }
        task.resume()
   
    }
    
    
    
    
    // Function for saving signature
    @objc func saveSignature() {
        // Getting the Signature Image from self.drawSignatureView using the method getSignature().
        if let signatureImage = self.SignatureView.getSignature(scale: 10) {
            
            
            
//            let text = "Dhinchak!"
//            let attributes = [
//                NSAttributedStringKey.foregroundColor: UIColor.lightGray,
//                NSAttributedStringKey.font: UIFont.systemFont(ofSize: 22)
//            ]
//            let textSize = text.size(withAttributes: attributes)
//
//            let renderer = UIGraphicsImageRenderer(size: textSize)
//            let image = renderer.image(actions: { context in
//                let rect = CGRect(origin: .zero, size: textSize)
//                text.draw(in: rect, withAttributes: attributes)
//            })
//            var attachImage = UIImage(named : "Dash2")
//
//            var finalMixedImage = getMixedImg(image1 : attachImage!, image2: signatureImage)
            
            
            loaderStart()
            getCSRFforSignaturePost(signatureImage : signatureImage)
        
            // Saving signatureImage from the line above to the Photo Roll.
            // The first time you do this, the app asks for access to your pictures.
            // UIImageWriteToSavedPhotosAlbum(signatureImage, nil, nil, nil)
            
            // Since the Signature is now saved to the Photo Roll, the View can be cleared anyway.
            self.SignatureView.clear()
        }
    }
    
    @objc func dismissScreen() {
        self.dismiss(animated: true, completion: nil)
    }
    
    @objc func decision(){
        if isUpdate!{
            
            self.dismissScreen()
        }
        else{
            
            self.firstTimeCall()
        }
    }
    
    func firstTimeCall(){
        
        self.dismissScreen()
        dashBoardSender?.backFromSignaturescreen()
        
    }
    
//    func getSignatureAvailability() {
//        
//        let id = UserDefaults.standard.string(forKey: "id")
//        let url = "\(BaseUrl.apiURL)/com.OData.dest/Dft_SignatureSet(ApproverId='122')?$format=json"
//        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
//        Arlamofire.request(encodedUrl!, method: .get, parameters: nil, encoding: URLEncoding.default, headers: nil)
//            .responseJSON{ response in
//                
//                
//                print(response.result.value)
//                switch response.result {
//                case .success(let JSON):
//                    if let jsonDict = JSON as? NSDictionary {
//                        if let d = jsonDict.value(forKey: "d") as? NSDictionary{
//                            let base64String = d.value(forKey: "DigitalSignature") as? String
//                            
//                        }
//                        
//                        
//                    }
//                case .failure(let error):
//                    let message = error.localizedDescription
//                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertControllerStyle.alert)
//                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertActionStyle.cancel, handler: nil)
//                    alertController.addAction(okAction)
//                    self.present(alertController, animated: true, completion: nil)
//                    
//                }
//        }
//    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
