//
//  FilterController.swift
//  DFT
//
//  Created by Soumya Singh on 01/02/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit

class FilterController: UIViewController {

    @IBOutlet var topView: UIView!
    
    @IBOutlet var containerView: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        containerView.layer.borderColor = UIColor.lightGray.cgColor
        containerView.layer.borderWidth =
        0.5
        let tap = UITapGestureRecognizer(target: self, action: Selector(("handleTap:")))
        topView.addGestureRecognizer(tap)
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func handleTap(sender: UITapGestureRecognizer? = nil){
        self.dismissScreen()
    }
    func dismissScreen(){
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func onUnsyncedPress(_ sender: UIButton) {
        self.dismissScreen()
    }
    
    @IBAction func onSubmitPress(_ sender: UIButton) {
        self.dismissScreen()
    }
    
    @IBAction func onReviewedPress(_ sender: UIButton) {
        self.dismissScreen()
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
