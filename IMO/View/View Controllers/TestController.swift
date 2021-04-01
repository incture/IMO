////
////  TestController.swift
////  DFT
////
////  Created by Soumya Singh on 29/01/18.
////  Copyright © 2018 SAP. All rights reserved.
////
//
//import UIKit
//
//class TestController: UIViewController, XMLParserDelegate {
//    
//    var csrfToken : String?
//    var parser = XMLParser()
//    var element = NSString()
//    var appID : String?
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        
//        // Do any additional setup after loading the view.
//    }
//    
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//        // Dispose of any resources that can be recreated.
//    }
//    
//    
//    @IBAction func getButtonPressed(_ sender: UIButton) {
//        
//        if let cookies = HTTPCookieStorage.shared.cookies {
//            
//            setCookies(cookies: cookies)
//            for cookie in cookies {
//                NSLog("\(cookie)")
//                
//            }
//        }
//        ODataGET()
//       // RESTcsrfFetch()
//        //ODataPOST()
//        //regNewUser()
//        
//    }
//    
//    @IBAction func postButtonPressed(_ sender: UIButton) {
//        
//    //hitSampleService()
//    }
//    
//    func parser(_ parser: XMLParser, didStartElement elementName: String, namespaceURI: String?, qualifiedName qName: String?, attributes attributeDict: [String : String] = [:]) {
//        
//        
//        
//        element = elementName as NSString
//        
//        print(element)
//        
//    }
//    
//    
//    
//    func parser(_ parser: XMLParser, foundCharacters string: String) {
//        
//        
//        
//        if element.isEqual(to: "d:ApplicationConnectionId") {
//            
//            //the new X-SMP-CID
//            
//            self.appID = string
//            
//            //UserDefaults.standard.set(string, forKey: "cid")
//            csrfFetch()
//            //ODataPOST()
//        }
//        
//    }
//    
//    
//    
//    func parser(_ parser: XMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?) {
//        
//        
//        
//    }
//    
//    func genAppCID(){
//    
//        let body = "<?xml version=\"1.0\" encoding=\"utf-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\"><content type=\"application/xml\"><m:properties><d:DeviceType>Android</d:DeviceType></m:properties></content></entry>"
//        //create dictionary with your parameters
//        let Username = "aisurya.puhan@incture.com"//usernameField.text""
//        let Password = "newjersy#1986"//passwordField.text
//        let credentialData = "\(Username):\(Password)".data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))!
//        let base64Credentials = credentialData.base64EncodedString()
//        var xmlRequest = URLRequest(url: NSURL(string: "https://mobile-d998e5467.us2.hana.ondemand.com/odata/applications/v4/saml.test.29jan/Connections")! as URL)
//        xmlRequest.httpBody = body.data(using: String.Encoding.utf8, allowLossyConversion: true)
//        xmlRequest.httpMethod = "POST"
//        xmlRequest.addValue("application/xml", forHTTPHeaderField: "Content-Type")
//        xmlRequest.setValue("Basic \(base64Credentials)", forHTTPHeaderField: "Authorization")
//        xmlRequest.setValue("", forHTTPHeaderField: "X-SMP-APPCID")
//        
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: xmlRequest) {[weak self] (data, response, error) in
//            
//            guard self != nil else { return }
//            if error == nil {
//                DispatchQueue.main.async {
//                    
//                    print(response)
//                    let xmlValue = String(data: data!, encoding: .utf8)
//                    let data = xmlValue?.data(using: .utf8)
//                    self!.parser = XMLParser(data: data!)
//                    self!.parser.delegate = self!
//                    self!.parser.parse()
//                    
//                    
//                }
//                
//            }
//        }
//        task.resume()
//        
//        /*Arlamofire.request(xmlRequest)
//            .responseString { response in
//                print(response)
//                let xmlValue = response.result.value
//                let data = xmlValue?.data(using: .utf8)
//                self.parser = XMLParser(data: data!)
//                self.parser.delegate = self
//                self.parser.parse()
//        }*/
//        
//        
//    }
//    
//    func setCookies(cookies: [HTTPCookie]){
//        Alamofire.SessionManager.default.session.configuration.httpCookieStorage?.setCookies(cookies, for: NSURL(string: "https://gwaas-d998e5467.us2.hana.ondemand.com") as URL?, mainDocumentURL: nil)
//    }
//    
//    func csrfFetch()
//    {//ODATA CSRF FETCH
//        let header = [ "x-csrf-token" : "fetch"]
//        
//        
//        
//        let urlString = "https://mobile-d998e5467.us2.hana.ondemand.com/cc1/RequestSet"
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.allHTTPHeaderFields = header
//        urlRequest.httpMethod = "get"
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//            
//            guard self != nil else { return }
//            if error == nil {
//                DispatchQueue.main.async {
//                    if let response = response as? HTTPURLResponse{
//                        if let xcsrf = response.allHeaderFields["x-csrf-token"] as? String {
//                            
//                            self!.csrfToken  = xcsrf
//                            self!.ODataPOST()
//                            print(xcsrf)
//                        }
//                    }
//                }
//                
//            }
//        }
//        task.resume()
//        
//     
//    }
//    
//    func ODataGET() {
//      
//        
//       
//        
//        
//        
//        let urlString = "https://mobile-d998e5467.us2.hana.ondemand.com/com.OData.dest/DFT_HeaderSet?$filter=ServiceProviderId%20eq%20%27123%27&$format=json"
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.allHTTPHeaderFields = nil
//        urlRequest.httpMethod = "get"
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//            
//            guard self != nil else { return }
//            if error == nil {
//                DispatchQueue.main.async {
//                    if let response = response as? HTTPURLResponse{
//                        if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
//                            
//                            self!.csrfToken  = xcsrf
//                            print(xcsrf)
//                        }
//                    }
//                }
//                
//            }
//        }
//        task.resume()
//        
//       
//    }
//    
//    func ODataPOST() {
//        
//        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
//        
//        let postContent = ["Country":"AU","ReqId":"","UserName":"REQUESTER","PurchaseGroup":"A07","Description":"TestMob","PurGrpTelNr":"","OutputDevice":"","FaxNumber":"","TelNumber":"","TelExtens":"","EmailAddress":"","Activity":"01","PersnRespnsble":"6000847","SystemId":"FIORIDEV"]
//      
//        let urlString = "https://mobile-d998e5467.us2.hana.ondemand.com/cc1/RequestSet"
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.allHTTPHeaderFields = header
//        
//        do{
//            let requestBody = try JSONSerialization.data(withJSONObject: postContent, options: .fragmentsAllowed)
//            urlRequest.httpBody = requestBody
//        }
//        catch{
//            print("error in creating the data object from json")
//        }
//        
//        
//        urlRequest.httpMethod = "post"
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//            
//            guard self != nil else { return }
//            if error == nil {
//                DispatchQueue.main.async {
//                    if let response = response as? HTTPURLResponse{
//                        if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
//                            self!.csrfToken  = xcsrf
//                            print(xcsrf)
//                        }
//                    }
//                }
//                
//            }
//        }
//        task.resume()
//        
//        
//        /*
//        
//        _ = Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/cc1/RequestSet", method: .post, parameters: postContent, encoding: JSONEncoding.default, headers: header)
//            .responseString{ response in
//                
//                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//        }*/
//    }
//    
//    func RESTcsrfFetch()
//    {
//        let header = [ "x-csrf-token" : "fetch"]
//        // running rules
//        //        Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/bpmrulesdest/rules-service/v1/rules/xsrf-token", method: .get, parameters: nil, encoding: URLEncoding.default, headers: header)
//        //            .responseString{ response in
//        //
//        //                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//        //                self.RESTODataPOST()
//        //
//        //        }
//        
//        
//        let urlString = "https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/xsrf-token"
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.allHTTPHeaderFields = header
//        urlRequest.httpMethod = "get"
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//            
//            guard self != nil else { return }
//            if error == nil {
//                DispatchQueue.main.async {
//                    if let response = response as? HTTPURLResponse{
//                        if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
//                            self!.csrfToken  = xcsrf
//                            print(xcsrf)
//                        }
//                    }
//                    self!.RESTODataPOST()
//                    //  self.RESTODataGET()
//                }
//                
//            }
//        }
//        task.resume()
//        
//        
//       /*
//       //  to fetch csrf in rest
//        Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/xsrf-token", method: .get, parameters: nil, encoding: URLEncoding.default, headers: header)
//            .responseString{ response in
//
//                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//                self.RESTODataPOST()
//                //  self.RESTODataGET()
//        }
//        */
//        
//       // To get logged In User
////        Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/com.getloggeduser.dest/services/userapi/currentUser", method: .get, parameters: nil, encoding: URLEncoding.default, headers: nil)
////            .responseString{ response in
////
////                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
////               // self.RESTODataPOST()
////                //  self.RESTODataGET()
////        }
//        // Get user Detail
////        Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/com.cloudidp.dest/service/scim/Users/P000025", method: .get, parameters: nil, encoding: URLEncoding.default, headers: nil)
////            .responseString{ response in
////
////                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
////                // self.RESTODataPOST()
////                //  self.RESTODataGET()
////        }
//    }
//    
//    func RESTCodeCSRFFetch()
//    {
//        
//        let Username = "Aisurya.puhan@incture.com"//usernameField.text""
//        let Password = "Incture123"//passwordField.tex
//        if Username != "" && Password != "" {
//            let credentialData = "\(Password)".data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))!
//            let base64Credentials = credentialData.base64EncodedString()
//            let credentialDataHeader = "\(Username):\(Password)".data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))!
//            let base64CredentialsHeader = credentialDataHeader.base64EncodedString()
//            let header = ["Authorization": "Basic \(base64CredentialsHeader)", "x-csrf-token" : "fetch"]//, "X-SMP-APPCID" : appID!]
//            // let header = ["x-csrf-token" : "fetch"]
//            
//            
//            
//            let urlString = "https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/xsrf-token"
//            var urlRequest = URLRequest(url: URL(string: urlString)!)
//            urlRequest.allHTTPHeaderFields = header
//            urlRequest.httpMethod = "get"
//            let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//                
//                guard self != nil else { return }
//                if error == nil {
//                    DispatchQueue.main.async {
//                        if let response = response as? HTTPURLResponse{
//                            if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
//                                self!.csrfToken  = xcsrf
//                                print(xcsrf)
//                            }
//                        }
//                        self!.RESTCodeHitSampleService()
//                    }
//                    
//                }
//            }
//            task.resume()
//            
//            
//            
//            
//            /*Arlamofire.request("https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/xsrf-token", method: .get, parameters: nil, encoding: URLEncoding.default, headers: header)
//                .responseString{ response in
//                    self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//                    self.RESTCodeHitSampleService()
//            }
//            */
//            
//            
//        }
//    }
//    
//    
//    func RESTCodeHitSampleService()
//    {
//        let Username = "P000025"//usernameField.text""
//        
//        let Password = "Murphy@111"//passwordField.text
//        
//        if Username != "" && Password != "" {
//            
//            
//            let credentialDataHeader = "\(Username):\(Password)".data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))!
//            
//            let base64CredentialsHeader = credentialDataHeader.base64EncodedString()
//            
//            //let postParam = ["userId":Username,"scode":base64Credentials]
//            let ticket = ["Ticket":"FT00006"]
//            let postParam = ["definitionId": "demo_mobility", "context" : ticket] as [String : Any]
//            
//            let header = ["Authorization": "Basic \(base64CredentialsHeader)"
//                , "X-CSRF-Token" : csrfToken!]//, "Content-Type" : "application/json"]
//            
//            
//            
//            
//            let urlString = "https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/workflow-instances"
//            var urlRequest = URLRequest(url: URL(string: urlString)!)
//            urlRequest.allHTTPHeaderFields = header
//            urlRequest.httpMethod = "post"
//            
//            do{
//                let requestBody = try JSONSerialization.data(withJSONObject: postParam, options: .fragmentsAllowed)
//                urlRequest.httpBody = requestBody
//            }
//            catch{
//                print("error in creating the data object from json")
//            }
//            
//            
//            let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) {[weak self] (data, response, error) in
//                
//                guard self != nil else { return }
//                if error == nil {
//                    DispatchQueue.main.async {
//                       print(String(data: data!, encoding: .utf8))
//                    }
//                    
//                }
//            }
//            task.resume()
//            
//            
//            
//            /*
//            _ = Arlamofire.request("https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/workflow-instances", method: .post, parameters: postParam, encoding: JSONEncoding.default, headers: header)
//                .responseString{ response in
//                    
//                    print(response.result.value)
//            }*/
//        }
//    }
//    
//    func RESTODataGET() {
//        
//        //        Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/cc1/GetPurGrpSet?$format=json", method: .get, parameters: nil, encoding: URLEncoding.default, headers: nil)
//        //            .responseString{ response in
//        //
//        //                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//        //
//        //        }
//        
//        let urlString = "https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/workflow-instances?$format=json"
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.httpMethod = "get"
//        
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
//            if error == nil{
//                DispatchQueue.main.async{
//                    guard let data = data else {
//                        return
//                    }
//                    do{
//                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
//                        //print(JSON)
//                        print(response)
//                        if let response = response as? HTTPURLResponse{
//                            if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
//                                
//                                self.csrfToken = xcsrf
//                                print(xcsrf)
//                            }
//                        }
//                    }catch {
//                        print(error.localizedDescription, "StatusCode: \(response!)")
//                    }
//                }
//            }
//        }
//        task.resume()
//        
//        
//        
//      /*  Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/workflow-instances?$format=json", method: .get, parameters: nil, encoding: URLEncoding.default, headers: nil)
//            .responseString{ response in
//                
//                
//                print(response)
//                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//                
//        }*/
//    }
//    
//    func RESTODataPOST() {
//        
//        let header = ["x-csrf-token" : csrfToken!, "Content-Type": "application/json"]
//        
//        //   let postContent = [ "__type__":"RoutingRuleInputDto","department": "Maintenance"]
//        
//        let contextualData : [String : Any] = ["appId" : "A1234",
//                                               "fieldTicketId" : "FT00000008",
//                                               "department" : "Operation",
//                                               "location" : "US",
//                                               "isApprovedByMurphyEngineer": "true",
//                                               "workflowTriggerTimeStamp" : "",
//                                               "vendorId" : "abc",
//                                               "vendorName" :  "abc",
//                                               "vendorEmailId" : "abc@gmail.com",
//                                               "murphyEngineerId" : "P12345",
//                                               "murphyEngineerEmailId" : "xyz@gmail.com"]
//        let postData = [ "definitionId" : "digital_field_ticket_workflow", "context" : contextualData as [String : Any]] as [String : Any]
//        
//        
//        //        _ = Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/bpmrulesdest/rules-service/rest/v1/rule-services/java/Digital_Field_Ticket_Rules/getDetailsByDepartment", method: .post, parameters: postData, encoding: JSONEncoding.default, headers: header)
//        //            .responseString{ response in
//        //
//        //                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//        //        }
//        
//        
//    
//        let urlString = "https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/workflow-instances"
//        var urlRequest = URLRequest(url: URL(string: urlString)!)
//        urlRequest.allHTTPHeaderFields = header
//        do{
//            let requestBody = try JSONSerialization.data(withJSONObject: postData, options: .fragmentsAllowed)
//            urlRequest.httpBody = requestBody
//        }
//        catch{
//            print("error in creating the data object from json")
//        }
//
//        urlRequest.httpMethod = "post"
//        
//        let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
//            var numberOfObjects = 0
//            if error == nil{
//                DispatchQueue.main.async{
//                    guard let data = data else {
//                        return
//                    }
//                    do{
//                        let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
//                        //print(JSON)
//                        
//                        if let response = response as? HTTPURLResponse{
//                            if let xcsrf = response.allHeaderFields["X-CSRF-Token"] as? String {
//                                
//                                self.csrfToken = xcsrf
//                                print(xcsrf)
//                            }
//                        }
//                    }catch {
//                        print(error.localizedDescription, "StatusCode: \(response!)")
//                    }
//                }
//            }else{
//                
//                DispatchQueue.main.async{
//                }
//            }
//        }
//        task.resume()
//
//        
//        /*_ = Arlamofire.request("https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/workflow-instances", method: .post, parameters: postData, encoding: JSONEncoding.default, headers: header)
//            .responseString{ response in
//                
//                self.csrfToken = response.response?.allHeaderFields["X-CSRF-Token"] as! String?
//        }*/
//        //  https://mobile-d998e5467.us2.hana.ondemand.com/bpmworkflowruntime/workflow-instances
//    }
//    
//    func regNewUser() {
//        
//        let headers = [
//            "authorization": "Basic UDAwMDA1NzpJbmN0dXJlQDEyMw==",
//            "content-type": "application/xml",
//            "cache-control": "no-cache",
//            "postman-token": "c61a8da7-f04a-ec6c-8da9-a24cb9a7dddb"
//        ]
//        
//        let postData = NSData(data: "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"><content type=\"application/xml\"><m:properties><d:DeviceType>iPhone</d:DeviceType></m:properties></content></entry>".data(using: String.Encoding.utf8)!)
//        
//        let request = NSMutableURLRequest(url: NSURL(string: "https://mobile-d998e5467.us2.hana.ondemand.com/odata/applications/v4/Basic.30Jan/Connections")! as URL,
//                                          cachePolicy: .useProtocolCachePolicy,
//                                          timeoutInterval: 10.0)
//        request.httpMethod = "POST"
//        request.allHTTPHeaderFields = headers
//        request.httpBody = postData as Data
//        
//        let session = URLSession.shared
//        let dataTask = session.dataTask(with: request as URLRequest, completionHandler: { (data, response, error) -> Void in
//            if (error != nil) {
//                print(error)
//            } else {
//                let httpResponse = response as? HTTPURLResponse
//                print(httpResponse)
//            }
//        })
//        
//        dataTask.resume()
//        
//    }
//    
//    
//    func hitSampleService()
//    {
//        let Username = "aisurya.puhan@incture.com"//usernameField.text""
//        
//        let Password = "newjersy#1986"//passwordField.text
//        
//        if Username != "" && Password != "" {
//            
//            
//            let credentialDataHeader = "\(Username):\(Password)".data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))!
//            
//            let base64CredentialsHeader = credentialDataHeader.base64EncodedString()
//            
//            //let postParam = ["userId":Username,"scode":base64Credentials]
//            let ticket = ["Ticket":"FT00006"]
//            let postParam = ["definitionId": "demo_mobility", "context" : ticket] as [String : Any]
//            
//            let header = ["Authorization": "Basic \(base64CredentialsHeader)"
//                , "X-CSRF-Token" : csrfToken!]//, "Content-Type" : "application/json"]
//            
//            
//            let urlString = "https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/workflow-instances"
//            var urlRequest = URLRequest(url: URL(string: urlString)!)
//            urlRequest.httpMethod = "post"
//            urlRequest.allHTTPHeaderFields = header
//            do{
//                let requestBody = try JSONSerialization.data(withJSONObject: postParam, options: .fragmentsAllowed)
//                urlRequest.httpBody = requestBody
//            }
//            catch{
//                print("error in creating the data object from json")
//            }
//
//            let task = DFTNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
//               
//                if error == nil{
//                    DispatchQueue.main.async{
//                        guard let data = data else {
//                            return
//                        }
//                        do{
//                            let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
//                            //print(JSON)
//                            print(String(data: data, encoding: .utf8))
//                            
//                        }catch {
//                            print(error.localizedDescription, "StatusCode: \(response!)")
//                        }
//                    }
//                }
//            }
//            task.resume()
//            
//            /*
//            _ = Arlamofire.request("https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/workflow-instances", method: .post, parameters: postParam, encoding: JSONEncoding.default, headers: header)
//                .responseString{ response in
//                    
//                    print(response.result.value)
//                    //self.displayField.text = response.result.value
//                    //                    switch response.result {
//                    //                    case .success(let JSON):
//                    //                        print(JSON)
//                    //                        self.displayData = JSON as! String
//                    //                        self.viewDidLoad()
//                    //
//                    //
//                    //                    case .failure(let error):
//                    //                        let message = error.localizedDescription
//                    //
//                    //                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertControllerStyle.alert)
//                    //                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertActionStyle.cancel, handler: nil)
//                    //                        alertController.addAction(okAction)
//                    //                        self.present(alertController, animated: true, completion: nil)
//                    //
//                    //                    }
//            }*/
//        }
//    }
//    /*
//     // MARK: - Navigation
//     
//     // In a storyboard-based application, you will often want to do a little preparation before navigation
//     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//     // Get the new view controller using segue.destinationViewController.
//     // Pass the selected object to the new view controller.
//     }
//     */
//    
//}
//
//
