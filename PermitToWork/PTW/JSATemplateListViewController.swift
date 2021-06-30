//
//  JSATemplateListViewController.swift
//  PermitToWork
//
//  Created by Rajat Jain on 03/06/21.
//

import UIKit

class Template{
    var id:Int?
    var name:String?
    
    init(id:Int, name:String) {
        self.id = id
        self.name =  name
    }
    
}

class JSATemplateListViewController: UIViewController {

    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var tableView: UITableView!
    
    var selectStatus : Bool = false
    var selectedIndex : Int = 0
    var tempDict: [Template] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationTitle.title = "Template list"
        tableView.delegate = self
        tableView.dataSource = self
        let selectNib = UINib(nibName: "TemplateTableViewCell", bundle: nil)
        tableView.register(selectNib, forCellReuseIdentifier: "TemplateTableViewCell")
        tableView.tableFooterView = UIView()

    }

    override func viewDidAppear(_ animated: Bool) {
        getTemplates()
    }
    @IBAction func homeButton(_ sender: Any)
    {
        self.navigationController?.popToViewController((self.navigationController?.viewControllers[1]) as! CreatedPermitControllerViewController, animated: true)
        JSAObject = JSA()
        //self.navigationController?.popToRootViewController(animated: true)
    }
    
    @IBAction func previousBtn(_ sender: UIButton)
    {
        self.navigationController?.popViewController(animated: true)
        JSAObject = JSA()
    }
    
   
    
    @IBAction func nextBtn(_ sender: UIButton)
    {
        if selectStatus ==  false{
            let alert = UIAlertController(title: "Please select a template", message: "", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
        else{
            
        }
  
    }

    func getTemplates()
    {
        var url = ""
        url = IMOEndpoints.getAllTemplates
        let encodedUrl = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        var urlRequest = URLRequest(url: URL(string: encodedUrl ?? "")!)
        urlRequest.httpMethod = "get"
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? NSDictionary
                    DispatchQueue.main.async {
                        
                        if let jsonDict = JSON?.value(forKey: "data") as? NSArray {
                            
                            for json in jsonDict {
                                var template = json as! NSDictionary
                                let id = template.value(forKey: "id") as? Int
                                let name = template.value(forKey: "name") as? String
                                let temp = Template(id: id ?? 0, name: name ?? "")
                                self.tempDict.append(temp)
                            }
                            DispatchQueue.main.async {
                                self.tableView.reloadData()
                            }
                            
                           
                        }
                    }
                    
                }catch{
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }}else{
                    DispatchQueue.main.async {
                        let message = error!.localizedDescription
                        
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    
                }
        }.resume()
    }
    
    @objc func buttonSelected(_ sender: UIButton)
    {
       
        if selectedIndex == sender.tag
        {
            if selectStatus == true
            {
            sender.setImage(UIImage (named: "unchecked"), for: .normal)
                selectStatus = false
            }
            else
            {
                sender.setImage(UIImage (named: "checked"), for: .normal)
                selectStatus = true
            }
        }
        else
        {
            sender.setImage(UIImage (named: "checked"), for: .normal)
            selectStatus = true
        }
        selectedIndex = sender.tag
        tableView.reloadData()
    }
    
}

extension JSATemplateListViewController :UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tempDict.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TemplateTableViewCell")! as! TemplateTableViewCell
        cell.selectionStyle = .none
        cell.selectButton.tag = indexPath.row
        if indexPath.row == selectedIndex && selectStatus == true
        {
            cell.setData(labelValue: tempDict[indexPath.row].name ?? "", selected: 1)
           // cell.selectButton.setImage(UIImage (named: "checked"), for: .normal)
        }
        else
        {
            cell.setData(labelValue: tempDict[indexPath.row].name ?? "", selected: 0)
           // cell.selectButton.setImage(UIImage (named: "unchecked"), for: .normal)
        }
        
        
        cell.selectButton?.addTarget(self, action: #selector(buttonSelected), for: .touchUpInside)
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 60
    }
}
