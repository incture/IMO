//
//  DateConversion.swift
//  
//
//  Created by Mahabaleshwar Hegde on 20/04/18.
//

import Foundation

extension Date {
    
    enum DateFormat: String {
        //        case yearMonthDateBarSeparator = "MM-dd-yyyy"
        case yearMonthDateHypenSeparator = "YYYY-MM-dd"
        case monthDateYearHiphenSeparator = "MM-dd-yyyy"
        case monthDateYearSlashSeperator = "MM/dd/yyyy"
        case long = "yyyy-MM-dd HH:mm:ss"
        case monthDateYearLong = "MM-dd-yyyy HH:mm:ss"
        case dayMonthYear = "dd MMM yyyy"
        case short = "hh:mm a"
        case hourMinuteSeconds = "HH:mm:ss"
        case hourMinuteSeconds12Hour = "hh:mm:ss a"
        case dayMonthYearwithTime = "dd, MMM yyyy hh:mm a"
        case yearMonthDateTime = "yyyy-MM-dd'T'HH:mm:ss"
        case monthDateYearTime = "MMM dd, yyyy h:mm:ss a"
         case dateMonthYearTime = "dd-MMM-yyyy h:mm:ss a"
        case taskMgmtFormat = "dd,MMM  h:mm:ss a"
        case investFormat = "dd-MMM-yyyy"
    }
    
    /**
     * Returns date only(without time) for specified style
     */
    func getDateString(forStyle dateStyle: DateFormatter.Style, timeStyle:  DateFormatter.Style = .none) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = dateStyle
        dateFormatter.timeStyle = timeStyle
        let formmattedDate = dateFormatter.string(from: self)
        return formmattedDate
    }

    
    func toDateFormat(_ format: DateFormat, isUTCTimeZone: Bool = false) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format.rawValue
        if isUTCTimeZone {
            dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        }
        let dateString = dateFormatter.string(from: self)
        return dateString
        
    }
    
    ///Returns Month of the date
    var month: Int? {
        get {
            return self.getCalender(component: .month)
        }
    }
    
    ///Returns day of the date
    var day: Int? {
        get {
            return self.getCalender(component: .day)
        }
    }
    
    ///Returns only hour of the date
    var hour: Int? {
        get {
            return self.getCalender(component: .hour)
        }
    }
    
    ///Returns year of the date
    var year: Int? {
        get {
            return self.getCalender(component: .year)
        }
    }
    
    ///Returns minutes of the date
    var minutes: Int? {
        get {
            return self.getCalender(component: .minute)
        }
    }
    
    var seconds: Int? {
        get {
            return self.getCalender(component: .second)
        }
    }
    
    
    var weekDay: Int? {
        
        return self.getCalender(component: .weekday)
    }
    
    
    var timZone: TimeZone? {
        get {
            let units: Set<Calendar.Component> = [.timeZone]
            let calenderComponents = Calendar.current.dateComponents(units, from: self)
            return calenderComponents.timeZone
        }
    }
    
    
    var localTime: String {
        var returnValue = ""
        if var time = self.hour, let minute = self.minutes {
            var timeSymbol = ""
            if time >= 12 && time < 24 {
                timeSymbol = "PM"
                if time != 12 {
                    time = time - 12
                }
            } else {
                timeSymbol = "AM"
            }
            returnValue = returnValue + " " + time.description + ":" + String(format: "%02d", minute) + " " + timeSymbol
        }
        return returnValue
    }
    
    static func convertTimeStamp(timeStamp : String, format: Date.DateFormat) -> String {
        let start =  String.Index(encodedOffset: 6)
        let end =  String.Index(encodedOffset: 16)
        let time = String(timeStamp[start ..< end])
        let unixDate = Double(time)
        let date = Date(timeIntervalSince1970: unixDate ?? 0.0)
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC") //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = format.rawValue
        let strDate = dateFormatter.string(from: date)
        return strDate
    }
    static func decodeTime(timeString : String) -> String{
        
        
        var start = String.Index(encodedOffset : 2)
        var end = String.Index(encodedOffset : 4)

        let hour = timeString[start ..< end] // returns String "o, worl"
        start = String.Index(encodedOffset : 5)
        end = String.Index(encodedOffset : 7)
        let minutes = timeString[start ..< end]
        start = String.Index(encodedOffset : 8)
        end = String.Index(encodedOffset : 10)
        let seconds = timeString[start ..< end]
        
        let timeData = hour + ":" + minutes + ":" + seconds
        
        let dateFormatter = DateFormatter()
        
        dateFormatter.dateFormat = "HH:mm:ss"
        
        let myDate = dateFormatter.date(from: timeData)!
        
        dateFormatter.dateFormat = "hh:mm a"
        
        let somedateString = dateFormatter.string(from: myDate)
        
        return somedateString
        
    }
    
    
    private func getCalender(component: Calendar.Component) -> Int?  {
        let units: Set<Calendar.Component> = [.hour, .day, .month, .year, .minute, .second, .weekday]
        let calenderComponents = Calendar.current.dateComponents(units, from: self)
        
        var returnValue: Int?
        switch component {
        case .hour:
            returnValue = calenderComponents.hour
            break
        case .day:
            returnValue = calenderComponents.day
            break
        case .month:
            returnValue = calenderComponents.month
            break
        case .year:
            returnValue = calenderComponents.year
            break
        case .minute:
            returnValue = calenderComponents.minute
        case .second:
            returnValue = calenderComponents.second
        case .weekday:
            returnValue = calenderComponents.weekday
        default:
            break
        }
        
        return returnValue
    }
}

extension String {
    
    func convertToDateString(format: Date.DateFormat, currentDateStringFormat: Date.DateFormat, shouldConvertFromUTC: Bool = false) -> String? {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = currentDateStringFormat.rawValue
        if shouldConvertFromUTC {
         //   dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        }
        guard let myDate = dateFormatter.date(from: self) else { return nil }
        
        dateFormatter.dateFormat = format.rawValue
        dateFormatter.timeZone = TimeZone.current
        let formattedDateString = dateFormatter.string(from: myDate)
        return formattedDateString
    }
    
    func convertToDate(format: Date.DateFormat, currentDateStringFormat: Date.DateFormat, shouldConvertFromUTC: Bool = false) -> Date? {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = currentDateStringFormat.rawValue
        if shouldConvertFromUTC {
           // dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        }
        guard let myDate = dateFormatter.date(from: self) else { return nil }
        
        dateFormatter.dateFormat = format.rawValue
        let formattedDateString = dateFormatter.string(from: myDate)
        let formattedDate = dateFormatter.date(from: formattedDateString)
        return formattedDate
    }
    
    func convertToDate(format: Date.DateFormat, currentDateStringFormat: Date.DateFormat, shouldConvertToUTC: Bool = false) -> Date? {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = currentDateStringFormat.rawValue
        guard let myDate = dateFormatter.date(from: self) else { return nil }
        
        dateFormatter.dateFormat = format.rawValue
        if shouldConvertToUTC {
          //  dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        }
        let formattedDateString = dateFormatter.string(from: myDate)
        let formattedDate = dateFormatter.date(from: formattedDateString)
        return formattedDate
    }
    
    func convertToDateString(format: Date.DateFormat, currentDateStringFormat: Date.DateFormat, shouldConvertToUTC: Bool = false) -> String? {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = currentDateStringFormat.rawValue
        guard let myDate = dateFormatter.date(from: self) else { return nil }
        
        dateFormatter.dateFormat = format.rawValue
        if shouldConvertToUTC {
          //  dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        }
        let formattedDateString = dateFormatter.string(from: myDate)
        return formattedDateString
    }
    func convertToDateToMilliseconds() -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = Date.DateFormat.long.rawValue
        guard let myDate = dateFormatter.date(from: self) else { return "" }
        return String(myDate.millisecondsSince1970)
    }
}


extension Int {
    
    var boolValue: Bool {
        return (self == 0) ? false : true
    }
    
    
    /// Returns integer neagtaion 1 or 0
    var negatedValue: Int {
        return (self == 0) ? 1 : 0
    }
    
}


extension Date {
    var millisecondsSince1970:Int64 {
        return Int64((self.timeIntervalSince1970 * 1000.0).rounded())
    }
    
    init(milliseconds:Int64) {
        self = Date(timeIntervalSince1970: TimeInterval(milliseconds) / 1000)
    }
}

extension Int64 {
    func convertEpocToDate() -> String {
        return Date(timeIntervalSince1970: Double(exactly: self)!/1000).toDateFormat(.dayMonthYearwithTime)
    }

}
