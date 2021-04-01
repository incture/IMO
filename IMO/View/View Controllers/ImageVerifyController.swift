//
//  ImageVerifyController.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 15/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class ImageVerifyController: UIViewController {

    @IBOutlet var signatureImage: UIImageView!
    @IBOutlet var disclaimerView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        createNavBar()
       // getSignature()
        let htmlText = "<ul style = 'color : #868686 ; font-size : 14;'><li><i>All Commercial Terms are to be in accordance to Murphy Oil MSA and originating Work Order.</i></li><li><i>Field/Wellsite Operations Stamp is a confirmation of services/materials received only.</i></li></ul>"
        let attrStr = try! NSAttributedString(
            data: htmlText.data(using: String.Encoding.unicode, allowLossyConversion: true)!,
            options: [ .documentType: NSAttributedString.DocumentType.html],
            documentAttributes: nil)
        disclaimerView.attributedText = attrStr

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        let base64String = UserDefaults.standard.string(forKey: "digisign")!
        let data: Data = Data(base64Encoded: base64String , options: .ignoreUnknownCharacters)!
        // turn  Decoded String into Data
        let dataImage = UIImage(data:data,scale:1.0)
        self.signatureImage.image = dataImage
    }
    
    func createNavBar() {
        
        navigationController?.navigationBar.tintColor = UIColor.white
        navigationItem.title = "Verify Signature"
        let backItem = UIBarButtonItem.init(image: UIImage(named : "Back")?.withRenderingMode(.alwaysTemplate), style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.dismissScreen))
        let updateItem = UIBarButtonItem.init(title: "UPDATE", style: UIBarButtonItem.Style.plain, target: self, action: #selector(self.updateSignature))
        navigationItem.rightBarButtonItem = updateItem
        navigationItem.leftBarButtonItem = backItem
        
    }
    
    @objc func updateSignature(){
        
        if ConnectionCheck.isConnectedToNetwork(){
            let splitViewController = UIStoryboard(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "DigitalSignatureController") as! DigitalSignatureController
            splitViewController.isUpdate = true
            let navController = UINavigationController(rootViewController: splitViewController)
            navController.modalPresentationStyle = .fullScreen
            self.present(navController, animated: true, completion: nil)
        }
        else{
            let message = "This device is currently offline.Please go online to use this feature."
            let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
            let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
            alertController.addAction(okAction)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @objc func dismissScreen()
    {
        self.dismiss(animated: false, completion: nil)
    }
    
    ///Dft_SignatureSet?$filter=ApproverId eq '77'

    func getSignature() {
        
        let id = UserDefaults.standard.string(forKey: "id")
        let url = "\(BaseUrl.apiURL)/com.OData.dest/Dft_SignatureSet(ApproverId='\(id!)')?$format=json"
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
       
        var urlRequest = URLRequest(url: URL(string: encodedUrl!)!)
        urlRequest.httpMethod = "get"
        
        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                DispatchQueue.main.async{
                    guard let data = data else {
                        return
                    }
                    do{
                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                        //print(JSON)
                        if let jsonDict = JSON as? NSDictionary {
                            if let d = jsonDict.value(forKey: "d") as? NSDictionary{
                                let base64String = d.value(forKey: "DigitalSignature") as? String
                                let data: Data = Data(base64Encoded: base64String! , options: .ignoreUnknownCharacters)!
                                // turn  Decoded String into Data
                                let dataImage = UIImage(data:data,scale:1.0)
                                self.signatureImage.image = dataImage
                            }
                            
                        }
                        
                    }catch {
                        print(error.localizedDescription, "StatusCode: \(response!)")
                    }
                }
            }else{
                
                DispatchQueue.main.async{
                    let message = error!.localizedDescription
                    let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                    let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                    alertController.addAction(okAction)
                    self.present(alertController, animated: true, completion: nil)
                }
            }
        }
        task.resume()


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
