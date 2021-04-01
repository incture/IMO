//
//  ICTextField.swift
//  Task-Management
//
//  Created by Mahabaleshwar Hegde on 26/04/18.
//  Copyright © 2018 SAP. All rights reserved.
//

import UIKit
var selectedClassification = ""
var selectedClassificationIndex = -1

@objc
protocol UIDatePickerDelegate: UITextFieldDelegate {
    @objc optional func datePickerValueChanged(date: Date, textField: ICTextField)
}

@objc
protocol UIDropDownDelegate: UITextFieldDelegate {
    @objc optional func datePickerDidSelect(row: Int)
}

@IBDesignable
final class ICTextField: UITextField {
    
    
    private var pickerView: UIPickerView?
    
    /// Textfield leftView or rightViewMode
    ///
    /// - left: leftView Mode enabled
    /// - right: rightView Mode enabled
    public enum ViewModePostion {
        case left
        case right
    }
    /// Date picker content dispaly type.
    ///
    /// - time: Displays hour, minute, and optionally AM/PM designation depending on the locale setting (e.g. 6 | 53 | PM)
    /// - `date`: Displays month, day, and year depending on the locale setting (e.g. November | 15 | 2007)
    /// - dateAndTime:  Displays date, hour, minute, and optionally AM/PM designation depending on the locale setting (e.g. Wed Nov 15 | 6 | 53 | PM)
    /// - countDownTimer: Displays hour and minute (e.g. 1 | 53)
    enum DatePickerMode: Int {
        case time = 1
        case date
        case dateAndTime
        case countDownTimer
    }
    
    
    /// To show the date or time input from textfield set this value to true
    /// Default is false.
    @IBInspectable public var isDatePicker: Bool = false {
        didSet {
            if self.isDatePicker {
               // self.datePicker.backgroundColor = UIColor.white
                self.inputView = self.datePicker
                self.inputAccessoryView = self.configureToolBar()
            }
            
            self.reloadInputViews()
        }
    }
    /// Legtview image name
    @IBInspectable public var leftDrawable: UIImage? = nil {
        didSet {
            guard let _ = self.leftDrawable else {
                self.rightViewMode = .never
                return
            }
            self.updateDrwableViewMode(forPostion: .left)
        }
    }
    
    /// Right view image name
    @IBInspectable public var rightDrawable: UIImage? = nil {
        didSet {
            guard let _ = self.rightDrawable else {
                self.rightViewMode = .never
                return
            }
            self.updateDrwableViewMode(forPostion: .right)
        }
    }
    
    /// Textfield placeholder color
    @IBInspectable public var placeholderColor: UIColor? = UIColor.white.withAlphaComponent(0.5) {
        didSet {
            guard let placeholder = self.placeholder, let color = self.placeholderColor else { return }
            let attributes = [NSAttributedString.Key.foregroundColor: color]
            self.attributedPlaceholder = NSAttributedString(string: placeholder, attributes: attributes)
        }
    }
    
    /// Shows the inputview as UITableView. able to select multiple option, default is false
    @IBInspectable public var isDropDown: Bool = false {
        didSet {
            if isDropDown {
                self.pickerView = UIPickerView()
                self.pickerView?.dataSource = self
                self.pickerView?.delegate = self
                self.inputAccessoryView = self.configureToolBar()
                self.inputView = self.pickerView
                self.reloadInputViews()
            } else {
                self.inputView = nil
                self.reloadInputViews()
            }
        }
    }

    
    //MARK:- Public Properties
    
    let datePicker: UIDatePicker = {
        let dateDropDown = UIDatePicker()
        dateDropDown.datePickerMode = .date
        dateDropDown.addTarget(self, action: #selector(ICTextField.datePickerValueChanged(sender:)), for: .valueChanged)
        return dateDropDown
    }()
    
    /// Datepicker mode
    var datePickerMode: DatePickerMode = .date {
        didSet {
            var mode: UIDatePicker.Mode = .date
            switch self.datePickerMode {
            case .date:
                mode = .date
            case .countDownTimer:
                mode = .countDownTimer
            case .dateAndTime:
                mode = .dateAndTime
            case .time:
                mode = .time
            }
            self.datePicker.datePickerMode = mode
        }
    }
    var dropDownSource: [String] = []
    var origin = ""
    var firstTime = true
    /// Tool bar to show cancel and done on top of textfield.
    public var accessoryToolBar: UIToolbar? {
        didSet {
            self.inputAccessoryView = accessoryToolBar
            self.reloadInputViews()
        }
    }
    
    override var keyboardType: UIKeyboardType {
        didSet {
            if self.keyboardType == .numberPad || self.keyboardType == .decimalPad {
                self.inputAccessoryView = self.configureToolBar()
                self.reloadInputViews()
            }
        }
    }
    private(set) var selectedDropDownItem: (row: Int, item: String)!
    //MARK:- private Properties
    
    private var datePickerDelegate: UIDatePickerDelegate? {
        get {
            return self.delegate as? UIDatePickerDelegate
        }
    }
    
    private var dropDownDelegate: UIDropDownDelegate? {
        get {
            return self.delegate as? UIDropDownDelegate
        }
    }

    private var selectedDate: Date = Date()
    
    //MARK:- override Methods
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    
    @objc private func datePickerValueChanged(sender: UIDatePicker) {
        self.selectedDate = sender.date
        self.datePickerDelegate?.datePickerValueChanged?(date: sender.date, textField: self)
    }
    
    private func configureToolBar() -> UIToolbar {
        let frame = CGRect(x: 0, y: 0, width: self.frame.width, height: 40.0)
        let toolBar = UIToolbar(frame: frame)
        toolBar.barStyle = .default
        toolBar.barTintColor = UIColor(red: 0/255, green: 179/255, blue: 233/255, alpha: 1.0)
        let doneButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(donePressed))
        doneButton.tintColor = .black
        let cancelButton = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(cancelPressed))
        cancelButton.tintColor = .black
        let spaceButton = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        
        
        toolBar.isUserInteractionEnabled = true
        toolBar.sizeToFit()
        return toolBar
    }
    
    @objc fileprivate func donePressed() {
        if self.isDatePicker{
            self.datePickerDelegate?.datePickerValueChanged?(date: self.selectedDate, textField: self)
        }
        else if self.isDropDown{
            if self.selectedDropDownItem == nil{
                selectedClassificationIndex = 0
                self.setSelectedItem(row: 0)
            }
        }
        self.resignFirstResponder()
    }
    
    @objc private func cancelPressed() {
        if self.isDropDown{
            self.selectedDropDownItem = nil
        }
        self.text = nil
        self.resignFirstResponder()
    }
    
    //MARK:- Private Functions
    private func updateDrwableViewMode(forPostion postion: ViewModePostion) {
        switch postion {
        case .left:
            guard let image = self.leftDrawable else { return }
            self.leftViewMode = .always
            let leftView = UIImageView(frame: CGRect(x: 0.0, y: 0.0, width: self.bounds.height - 8.0, height: self.bounds.height - 16.0))
            leftView.image = image
            leftView.contentMode = .scaleAspectFill
            leftView.clipsToBounds = true
            self.leftView = leftView
            self.layoutIfNeeded()
        case .right:
            guard let image = self.leftDrawable else { return }
            self.rightViewMode = .always
            let rightView = UIImageView(frame: CGRect(x: 0.0, y: 8.0, width: 12.0, height: 12.0))
            rightView.image = image
            rightView.contentMode = .scaleAspectFill
            rightView.clipsToBounds = true
            self.rightView = rightView
            self.layoutIfNeeded()
        }
    }
}

// MARK:- UIPickerViewDataSource

extension ICTextField: UIPickerViewDataSource {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if origin == "Investigation"
        {
            if self.tag == 1
            {
                if selectedClassificationIndex == -1
                {
                    return 0
                }
                else
                {
                    if self.firstTime
                    {
                        self.firstTime = false
                        let str = self.dropDownSource[selectedClassificationIndex]
                        let arr = str.components(separatedBy: ",")
                        self.dropDownSource = arr
                        return self.dropDownSource.count
                    }
                    else
                    {
                        return self.dropDownSource.count
                    }
                }
            }
            else
            {
                return self.dropDownSource.count
            }
        }
        else
        {
            
            return self.dropDownSource.count
        }
        
    }
}

// MARK:- UIPickerViewDelegate

extension ICTextField: UIPickerViewDelegate {
    
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return self.dropDownSource[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if origin == "Investigation"
        {
            if self.tag == 0
            {
//                selectedClassification = self.dropDownSource[row]
                selectedClassificationIndex = row
            }
        }
        
        self.setSelectedItem(row: row)
        self.dropDownDelegate?.datePickerDidSelect?(row: row)
    }
    
    private func setSelectedItem(row: Int) {
        
        let item = self.dropDownSource[row]
        self.text = item
        self.selectedDropDownItem  = (row: row, item: item)
    }
    
}
