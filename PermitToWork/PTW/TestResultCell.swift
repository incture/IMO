//
//  TestResultCell.swift
//  IOP_iOS
//
//  Created by Soumya Singh on 28/03/18.
//  Copyright © 2018 Parul Thakur77. All rights reserved.
//

import UIKit

class TestResultCell: UITableViewCell {

    @IBOutlet var topCell: UILabel!
    @IBOutlet var bottomCell: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(test : TestsModel){
        topCell.text = test.flammableGas
        var date = test.Date
        if !date.isEmpty, let formmatedValue = date.convertToDateString(format: .monthDateYearHiphenSeparator, currentDateStringFormat: .long, shouldConvertToUTC : false) {
            date = formmatedValue
        }
        bottomCell.text = "Date : \(date)"
    }
    
}
