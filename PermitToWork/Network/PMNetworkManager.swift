//
//  PMNetworkManager.swift
//  pm
//
//  Created by Mohan on 11/04/19.
//  
//

import Foundation
import SAPFoundation

class ImoPtwNetworkManager {
    
    static var shared = ImoPtwNetworkManager()
    var urlSession : SAPURLSession!
    
    /*
    func authentication(_ request: URLRequest, completion: @escaping (String?, Error?) -> Void) -> Void {
        
        let task = self.urlSession.dataTask(with: request) { (data, response, error) in
            let urlResponse = response as? HTTPURLResponse
            
            if let csrfToken = urlResponse?.allHeaderFields["x-csrf-token"] as? String {
                completion(csrfToken, nil)
            } else {
                print("Token not validated")
                guard let responseCode = response?.httpStatusCode else { return }
                var errorMessage: String = "Something went wrong, please try again in sometime"
                if responseCode >= 400 && responseCode < 500 {
                    errorMessage = ErrorHandlerClass.userDataIssue.errorMessage()
                } else if responseCode >= 500 && responseCode < 600 {
                    errorMessage = ErrorHandlerClass.serverGatewayIssue.errorMessage()
                } else if responseCode >= 200 && responseCode < 300 {
                    errorMessage = ErrorHandlerClass.tokenValidation.errorMessage()
                } else {
                    errorMessage = ErrorHandlerClass.connectivityIssue.errorMessage()
                }
                
                let err = NSError(domain: errorMessage, code: response?.httpStatusCode ?? 500, userInfo: nil)
                completion(nil, err as Error)
            }
        }
        task.resume()
    }
    
    func getObject(_ request: URLRequest, completion: @escaping (MetadataResponse?, Error?) -> Void) -> Void {
        let task = self.urlSession.dataTask(with: request) { (data, response, error) in
            guard let data = data else { return completion(nil, self.getFailureError(response: response)) }
            do {
                print(try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) )
                let metadataResponse = try JSONDecoder().decode(MetadataResponse.self, from: data)
                completion(metadataResponse, nil)
            } catch {
                completion(nil, self.getFailureError(response: response))
            }
        }
        task.resume()
    }
    func getObjectValues(_ request: URLRequest, completion: @escaping (EquipmentLookUpModel?, Error?) -> Void) -> Void {
        let task = self.urlSession?.dataTask(with: request) { (data, response, error) in
            guard let data = data else { return completion(nil, self.getFailureError(response: response)) }
            do {
                print(try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) )
                let metadataResponse = try JSONDecoder().decode(EquipmentLookUpModel.self, from: data)
                completion(metadataResponse, nil)
            } catch {
                completion(nil, self.getFailureError(response: response))
            }
        }
        task?.resume()
    }
    func getObjectValuesData(_ request: URLRequest, completion: @escaping (BypassEquipmentLookUpModel?, Error?) -> Void) -> Void {
        let task = self.urlSession?.dataTask(with: request) { (data, response, error) in
            guard let data = data else { return completion(nil, self.getFailureError(response: response)) }
            do {
                print(try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) )
                let metadataResponse = try JSONDecoder().decode(BypassEquipmentLookUpModel.self, from: data)
                completion(metadataResponse, nil)
            } catch {
                completion(nil, self.getFailureError(response: response))
            }
        }
        task?.resume()
    }
    private func getFailureError(response: URLResponse?) -> Error? {
        
        guard let responseCode = response?.httpStatusCode else {
            let errorMessage: String = ErrorHandlerClass.connectivityIssue.errorMessage()
            let err = NSError(domain: errorMessage, code: -1001, userInfo: nil)
            return err
        }
        
        var errorMessage: String = "Something went wrong, please try again in sometime"
        if responseCode >= 400 && responseCode < 500 {
            errorMessage = ErrorHandlerClass.userDataIssue.errorMessage()
        } else if responseCode >= 500 && responseCode < 600 {
            errorMessage = ErrorHandlerClass.serverGatewayIssue.errorMessage()
        } else {
            errorMessage = ErrorHandlerClass.connectivityIssue.errorMessage()
        }
        
        let err = NSError(domain: errorMessage, code: response?.httpStatusCode ?? 500, userInfo: nil)
        
        return err as Error
    }*/
}

enum ErrorHandlerClass: Error {
    case serverGatewayIssue
    case userDataIssue
    case connectivityIssue
    case tokenValidation
    
    func errorMessage() -> String {
        switch self {
        case .userDataIssue : return "Something went wrong, please clear the data and try again"
        case .connectivityIssue: return "Unable to connect to server, please check your internet connection"
        case .serverGatewayIssue: return "Something when wrong, please try again"
        case .tokenValidation: return "Unauthorised User"
        }
    }
}
//extension PMNetworkManager: SAPURLSessionDiscoverable {}
