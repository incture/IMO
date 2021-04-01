//
//  FullImageViewer.swift
//  MurphyDFT-Final
//
//  Created by Soumya Singh on 16/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class FullImageViewer: UIViewController {

    @IBOutlet var fullImageView: UIImageView!
    @IBOutlet var dismissButton: UIButton!
    var fullImagebase64 : String?
    override func viewDidLoad() {
        super.viewDidLoad()
        customizeUI()
        let data: NSData = NSData(base64Encoded: fullImagebase64! , options: .ignoreUnknownCharacters)!
        // turn  Decoded String into Data
        let dataImage = UIImage(data: data as Data)
        fullImageView.image = dataImage

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func OnPressDismiss(_ sender: Any) {
        
        self.dismiss(animated: false, completion: nil)
    }
    
    func customizeUI(){
        
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.95)
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
