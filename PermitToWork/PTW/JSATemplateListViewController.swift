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
    
    @IBOutlet weak var noTempLabel: UILabel!
    var selectStatus : Bool = false
    var selectedIndex : Int = 0
    var tempDict: [Template] = []
    var indicator: UIActivityIndicatorView = UIActivityIndicatorView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationTitle.title = "Template list"
        tableView.delegate = self
        tableView.dataSource = self
        noTempLabel.isHidden =  true
        let selectNib = UINib(nibName: "TemplateTableViewCell", bundle: nil)
        tableView.register(selectNib, forCellReuseIdentifier: "TemplateTableViewCell")
        tableView.tableFooterView = UIView()
        

    }

    override func viewDidAppear(_ animated: Bool) {
        getTemplates()
    }
    
    func loaderStart()
    {
        indicator.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        indicator.center = CGPoint(x: self.view.frame.size.width/2, y: self.view.frame.size.height/2)
        indicator.bounds = UIScreen.main.bounds
        UIApplication.shared.keyWindow!.addSubview(indicator)
        indicator.bringSubviewToFront(view)
     //   UIApplication.shared.isNetworkActivityIndicatorVisible = true
        indicator.startAnimating()
        
    }
    
    func loaderStop()
    {
        indicator.stopAnimating()
    }
    
    func getTemplateDetails(){
        DispatchQueue.main.async {
            self.loaderStart()
        }
        
        let urlString : String = IMOEndpoints.getTemplateById + "\(tempDict[selectedIndex].id ?? 0)"
        var urlRequest = URLRequest(url: URL(string: urlString)!)
        urlRequest.httpMethod = "get"
        
        ImoPtwNetworkManager.shared.urlSession.dataTask(with: urlRequest) { (data, response, error) in
            
            if error == nil{
                guard let data = data else {
                    return
                }
                do{
                    let JSON = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
                    DispatchQueue.main.async{
                    if let jsonDict = JSON as? NSDictionary {
                        readJSAObject = ReadJSA(JSON:jsonDict.value(forKey: "data") as? NSDictionary ?? NSDictionary())
                        self.readModelToJSAModel()
                            self.loaderStop()
                    }
                    }
                }catch {
                    print(error.localizedDescription, "StatusCode: \(response!)")
                }   }else{
                    DispatchQueue.main.async {
                        self.loaderStop()
                        let message = error!.localizedDescription
                        let alertController = UIAlertController.init(title: "", message:message , preferredStyle: UIAlertController.Style.alert)
                        let okAction = UIAlertAction.init(title: "OK", style: UIAlertAction.Style.cancel, handler: nil)
                        alertController.addAction(okAction)
                        self.present(alertController, animated: true, completion: nil)
                    }
                    
                }
        }.resume()
    }
    
    
    
    func readModelToJSAModel(){
        // Header Values
//        JSAObject.createdBy = (readJSAObject.TOJSAREVIEWValue?.createdBy)!
//        JSAObject.createdDate = (readJSAObject.TOJSAREVIEWValue?.createdDate)!
//        JSAObject.updatedDate = (readJSAObject.TOJSAREVIEWValue?.lastUpdatedDate)!
//        JSAObject.updatedBy = (readJSAObject.TOJSAREVIEWValue?.lastUpdatedBy)!
//        JSAObject.approvedBy = (readJSAObject.TOJSAREVIEWValue?.approvedBy)!
//        JSAObject.approvedDate = (readJSAObject.TOJSAREVIEWValue?.approvedDate)!
//        JSAObject.hasCSP = readJSAObject.TOJSAHEADERValue!.hasCSE
//        JSAObject.hasCWP = readJSAObject.TOJSAHEADERValue!.hasCWP
//        JSAObject.hasHWP = readJSAObject.TOJSAHEADERValue!.hasHWP
//        JSAObject.status = readJSAObject.TOJSAHEADERValue!.status
//        JSAObject.permitNumber = readJSAObject.TOJSAHEADERValue!.permitNumber
//        JSAObject.jsaPermitNumber = readJSAObject.TOJSAHEADERValue!.jsapermitNumber
        
        //self.navigationTitle.title = "JSA - \(JSAObject.permitNumber)"
        
        // create JSA
        
        JSAObject.createJSA.injuryDescription = readJSAObject.TOJSAHEADERValue!.identifyMostSeriousPotentialInjury.utf8DecodedString()
        JSAObject.createJSA.taskDescription = readJSAObject.TOJSAHEADERValue!.taskDescription.utf8DecodedString()
        JSAObject.createJSA.jsaPermit = readJSAObject.TOJSAHEADERValue!.jsapermitNumber
        
        // Risk Assesment
        
        
        //need to uncomment
        JSAObject.riskAssesment.mustExistingWork = (readJSAObject.TOJSARISKASSValue?.mustModifyExistingWorkPractice)!
        JSAObject.riskAssesment.afterMitigation = (readJSAObject.TOJSARISKASSValue?.hasContinuedRisk)!
        
        JSAObject.riskAssesment.hardHat = (readJSAObject.TOJSE_PPEValue?.hardHat)!
        JSAObject.riskAssesment.safetyShoes = (readJSAObject.TOJSE_PPEValue?.safetyBoot)!
        JSAObject.riskAssesment.single = (readJSAObject.TOJSE_PPEValue?.singleEar)!
        JSAObject.riskAssesment.double = (readJSAObject.TOJSE_PPEValue?.doubleEars)!
        JSAObject.riskAssesment.SCBA = (readJSAObject.TOJSE_PPEValue?.needSCBA)!
        JSAObject.riskAssesment.dustMask = (readJSAObject.TOJSE_PPEValue?.needDustMask)!
        JSAObject.riskAssesment.fallProtection = (readJSAObject.TOJSE_PPEValue?.fallProtection)!
        JSAObject.riskAssesment.fallRestraint = (readJSAObject.TOJSE_PPEValue?.fallRestraint)!
        JSAObject.riskAssesment.flameResistantClothing = (readJSAObject.TOJSE_PPEValue?.flameResistantClothing)!
        JSAObject.riskAssesment.safetyGlasses = (readJSAObject.TOJSE_PPEValue?.safetyGlasses)!
        JSAObject.riskAssesment.faceShield = (readJSAObject.TOJSE_PPEValue?.faceShield)!
        JSAObject.riskAssesment.goggles = (readJSAObject.TOJSE_PPEValue?.goggles)!
        JSAObject.riskAssesment.cotton = (readJSAObject.TOJSE_PPEValue?.cottonGlove)!
        JSAObject.riskAssesment.leather = (readJSAObject.TOJSE_PPEValue?.leatherGlove)!
        JSAObject.riskAssesment.impactProtection = (readJSAObject.TOJSE_PPEValue?.impactProtection)!
        JSAObject.riskAssesment.chemicalSuit = (readJSAObject.TOJSE_PPEValue?.chemicalSuit)!
        JSAObject.riskAssesment.apron = (readJSAObject.TOJSE_PPEValue?.apron)!
        JSAObject.riskAssesment.foulWeatherGear = (readJSAObject.TOJSE_PPEValue?.needFoulWeatherGear)!
        JSAObject.riskAssesment.respiratorType = (readJSAObject.TOJSE_PPEValue?.respiratorTypeDescription)!.utf8DecodedString()
        JSAObject.riskAssesment.otherPPE = (readJSAObject.TOJSE_PPEValue?.otherPPEDescription)!.utf8DecodedString()
        JSAObject.riskAssesment.other = (readJSAObject.TOJSE_PPEValue?.gloveDescription)!.utf8DecodedString()
        JSAObject.riskAssesment.chemical = (readJSAObject.TOJSE_PPEValue?.chemicalGloveDescription)!.utf8DecodedString()
        
        
        // Hazard control
        
        //Pressureized Equipment
        JSAObject.hazardCategories.categories[0][0] = (readJSAObject.TOJSAHAZARDPRESSValue?.performIsolation)!
        JSAObject.hazardCategories.categories[0][1] = (readJSAObject.TOJSAHAZARDPRESSValue?.depressurizeDrain)!
        JSAObject.hazardCategories.categories[0][2] = (readJSAObject.TOJSAHAZARDPRESSValue?.relieveTrappedPressure)!
        JSAObject.hazardCategories.categories[0][3] = (readJSAObject.TOJSAHAZARDPRESSValue?.doNotWorkInLineOfFire)!
        JSAObject.hazardCategories.categories[0][4] = (readJSAObject.TOJSAHAZARDPRESSValue?.anticipateResidual)!
        JSAObject.hazardCategories.categories[0][5] = (readJSAObject.TOJSAHAZARDPRESSValue?.secureAllHoses)!
        
        //poor lighting
        JSAObject.hazardCategories.categories[1][0] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.provideAlternateLighting)!
        JSAObject.hazardCategories.categories[1][1] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.waitUntilVisibilityImprove)!
        JSAObject.hazardCategories.categories[1][2] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.deferUntilVisibilityImprove)!
        JSAObject.hazardCategories.categories[1][3] = (readJSAObject.TOJSAHAZARDVISIBLEValue?.knowDistanceFromPoles)!
        
        //personnel
        JSAObject.hazardCategories.categories[2][0] = (readJSAObject.TOJSAHAZARDPERSONValue?.performInduction)!
        JSAObject.hazardCategories.categories[2][1] = (readJSAObject.TOJSAHAZARDPERSONValue?.mentorCoachSupervise)!
        JSAObject.hazardCategories.categories[2][2] = (readJSAObject.TOJSAHAZARDPERSONValue?.verifyCompetencies)!
        JSAObject.hazardCategories.categories[2][3] = (readJSAObject.TOJSAHAZARDPERSONValue?.addressLimitations)!
        JSAObject.hazardCategories.categories[2][4] = (readJSAObject.TOJSAHAZARDPERSONValue?.manageLanguageBarriers)!
        JSAObject.hazardCategories.categories[2][5] = (readJSAObject.TOJSAHAZARDPERSONValue?.wearSeatBelts)!
        
        //confined space entry
        JSAObject.hazardCategories.categories[3][0] = (readJSAObject.TOJSAHAZARDCSEValue?.discussWorkPractice)!
        JSAObject.hazardCategories.categories[3][1] = (readJSAObject.TOJSAHAZARDCSEValue?.conductAtmosphericTesting)!
        JSAObject.hazardCategories.categories[3][2] = (readJSAObject.TOJSAHAZARDCSEValue?.monitorAccess)!
        JSAObject.hazardCategories.categories[3][3] = (readJSAObject.TOJSAHAZARDCSEValue?.protectSurfaces)!
        JSAObject.hazardCategories.categories[3][4] = (readJSAObject.TOJSAHAZARDCSEValue?.prohibitMobileEngine)!
        JSAObject.hazardCategories.categories[3][5] = (readJSAObject.TOJSAHAZARDCSEValue?.provideObserver)!
        JSAObject.hazardCategories.categories[3][6] = (readJSAObject.TOJSAHAZARDCSEValue?.developRescuePlan)!
        
        //simultaneous operations
        JSAObject.hazardCategories.categories[4][0] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.followSimopsMatrix)!
        JSAObject.hazardCategories.categories[4][1] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.mocRequiredFor)!
        JSAObject.hazardCategories.categories[4][2] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.interfaceBetweenGroups)!
        JSAObject.hazardCategories.categories[4][3] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.useBarriersAnd)!
        JSAObject.hazardCategories.categories[4][4] = (readJSAObject.TOJSAHAZARDSIMULTANValue?.havePermitSigned)!
        
        //ignition sources
        JSAObject.hazardCategories.categories[5][0] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.removeCombustibleMaterials)!
        JSAObject.hazardCategories.categories[5][1] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.provideFireWatch)!
        JSAObject.hazardCategories.categories[5][2] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.implementAbrasiveBlastingControls)!
        JSAObject.hazardCategories.categories[5][3] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.conductContinuousGasTesting)!
        JSAObject.hazardCategories.categories[5][4] = (readJSAObject.TOJSAHAZARDIGNITIONValue?.earthForStaticElectricity)!
        
        //hazardous substances
        JSAObject.hazardCategories.categories[6][0] = (readJSAObject.TOJSAHAZARDSUBSValue?.drainEquipment)!
        JSAObject.hazardCategories.categories[6][1] = (readJSAObject.TOJSAHAZARDSUBSValue?.followSdsControls)!
        JSAObject.hazardCategories.categories[6][2] = (readJSAObject.TOJSAHAZARDSUBSValue?.implementHealthHazardControls)!
        JSAObject.hazardCategories.categories[6][3] = (readJSAObject.TOJSAHAZARDSUBSValue?.testMaterial)!
        
        //potential Spills
        JSAObject.hazardCategories.categories[7][0] = (readJSAObject.TOJSAHAZARDSPILLValue?.drainEquipment)!
        JSAObject.hazardCategories.categories[7][1] = (readJSAObject.TOJSAHAZARDSPILLValue?.connectionsInGoodCondition)!
        JSAObject.hazardCategories.categories[7][2] = (readJSAObject.TOJSAHAZARDSPILLValue?.spillContainmentEquipment)!
        JSAObject.hazardCategories.categories[7][3] = (readJSAObject.TOJSAHAZARDSPILLValue?.haveSpillCleanupMaterials)!
        JSAObject.hazardCategories.categories[7][4] = (readJSAObject.TOJSAHAZARDSPILLValue?.restrainHosesWhenNotInUse)!
        
        //Weather
        JSAObject.hazardCategories.categories[8][0] = (readJSAObject.TOJSAHAZARDWEATHERValue?.controlsForSlipperySurface)!
        JSAObject.hazardCategories.categories[8][1] = (readJSAObject.TOJSAHAZARDWEATHERValue?.heatBreak)!
        JSAObject.hazardCategories.categories[8][2] = (readJSAObject.TOJSAHAZARDWEATHERValue?.coldHeaters)!
        JSAObject.hazardCategories.categories[8][3] = (readJSAObject.TOJSAHAZARDWEATHERValue?.lightning)!
        
        //High Noise
        JSAObject.hazardCategories.categories[9][0] = (readJSAObject.TOJSAHAZARDNOISEValue?.wearCorrectHearing)!
        JSAObject.hazardCategories.categories[9][1] = (readJSAObject.TOJSAHAZARDNOISEValue?.manageExposureTimes)!
        JSAObject.hazardCategories.categories[9][2] = (readJSAObject.TOJSAHAZARDNOISEValue?.shutDownEquipment)!
        JSAObject.hazardCategories.categories[9][3] = (readJSAObject.TOJSAHAZARDNOISEValue?.useQuietTools)!
        JSAObject.hazardCategories.categories[9][4] = (readJSAObject.TOJSAHAZARDNOISEValue?.soundBarriers)!
        JSAObject.hazardCategories.categories[9][5] = (readJSAObject.TOJSAHAZARDNOISEValue?.provideSuitableComms)!
        
        //Dropped Objects
        JSAObject.hazardCategories.categories[10][0] = (readJSAObject.TOJSAHAZARDDROPPEDValue?.markRestrictEntry)!
        JSAObject.hazardCategories.categories[10][1] = (readJSAObject.TOJSAHAZARDDROPPEDValue?.useLiftingEquipmentToRaise)!
        JSAObject.hazardCategories.categories[10][2] = (readJSAObject.TOJSAHAZARDDROPPEDValue?.secureTools)!
        
        //Lifting Equipment
        JSAObject.hazardCategories.categories[11][0] = (readJSAObject.TOJSAHAZARDLIFTValue?.confirmEquipmentCondition)!
        JSAObject.hazardCategories.categories[11][1] = (readJSAObject.TOJSAHAZARDLIFTValue?.obtainApprovalForLifts)!
        JSAObject.hazardCategories.categories[11][2] = (readJSAObject.TOJSAHAZARDLIFTValue?.haveDocumentedLiftPlan)!
        
        //work at heights
        JSAObject.hazardCategories.categories[12][0] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.discussWorkingPractice)!
        JSAObject.hazardCategories.categories[12][1] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.verifyFallRestraint)!
        JSAObject.hazardCategories.categories[12][2] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.useFullBodyHarness)!
        JSAObject.hazardCategories.categories[12][3] = (readJSAObject.TOJSAHAZARDHEIGHTValue?.useLockTypeSnaphoooks)!
        
        //portable electrical equipment
        JSAObject.hazardCategories.categories[13][0] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.inspectToolsForCondition)!
        JSAObject.hazardCategories.categories[13][1] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.implementGasTesting)!
        JSAObject.hazardCategories.categories[13][2] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.protectElectricalLeads)!
        JSAObject.hazardCategories.categories[13][3] = (readJSAObject.TOJSAHAZARDELECTRICALValue?.identifyEquipClassification)!
        
        //moving equipment
        JSAObject.hazardCategories.categories[14][0] = (readJSAObject.TOJSAHAZARDMOVINGValue?.confirmMachineryIntegrity)!
        JSAObject.hazardCategories.categories[14][1] = (readJSAObject.TOJSAHAZARDMOVINGValue?.provideProtectiveBarriers)!
        JSAObject.hazardCategories.categories[14][2] = (readJSAObject.TOJSAHAZARDMOVINGValue?.observerToMonitorProximityPeopleAndEquipment)!
        JSAObject.hazardCategories.categories[14][3] = (readJSAObject.TOJSAHAZARDMOVINGValue?.lockOutEquipment)!
        JSAObject.hazardCategories.categories[14][4] = (readJSAObject.TOJSAHAZARDMOVINGValue?.doNotWorkInLineOfFire)!
        
        //manual handling
        JSAObject.hazardCategories.categories[15][0] = (readJSAObject.TOJSAHAZARDMANUALValue?.assessManualTask)!
        JSAObject.hazardCategories.categories[15][1] = (readJSAObject.TOJSAHAZARDMANUALValue?.limitLoadSize)!
        JSAObject.hazardCategories.categories[15][2] = (readJSAObject.TOJSAHAZARDMANUALValue?.properLiftingTechnique)!
        JSAObject.hazardCategories.categories[15][3] = (readJSAObject.TOJSAHAZARDMANUALValue?.confirmStabilityOfLoad)!
        JSAObject.hazardCategories.categories[15][4] = (readJSAObject.TOJSAHAZARDMANUALValue?.getAssistanceOrAid)!
        
        //equipment & Tools
        JSAObject.hazardCategories.categories[16][0] = (readJSAObject.TOJSAHAZARDTOOLSValue?.inspectEquipmentTool)!
        JSAObject.hazardCategories.categories[16][1] = (readJSAObject.TOJSAHAZARDTOOLSValue?.brassToolsNecessary)!
        JSAObject.hazardCategories.categories[16][2] = (readJSAObject.TOJSAHAZARDTOOLSValue?.useProtectiveGuards)!
        JSAObject.hazardCategories.categories[16][3] = (readJSAObject.TOJSAHAZARDTOOLSValue?.useCorrectTools)!
        JSAObject.hazardCategories.categories[16][4] = (readJSAObject.TOJSAHAZARDTOOLSValue?.checkForSharpEdges)!
        JSAObject.hazardCategories.categories[16][5] = (readJSAObject.TOJSAHAZARDTOOLSValue?.applyHandSafetyPrinciple)!
        
        //Slip Trips & Tools
        JSAObject.hazardCategories.categories[17][0] = (readJSAObject.TOJSAHAZARDFALLSValue?.identifyProjections)!
        JSAObject.hazardCategories.categories[17][1] = (readJSAObject.TOJSAHAZARDFALLSValue?.flagHazards)!
        JSAObject.hazardCategories.categories[17][2] = (readJSAObject.TOJSAHAZARDFALLSValue?.secureCables)!
        JSAObject.hazardCategories.categories[17][3] = (readJSAObject.TOJSAHAZARDFALLSValue?.cleanUpLiquids)!
        JSAObject.hazardCategories.categories[17][4] = (readJSAObject.TOJSAHAZARDFALLSValue?.barricadeHoles)!
        
        //High Energy
        JSAObject.hazardCategories.categories[18][0] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.restrictAccess)!
        JSAObject.hazardCategories.categories[18][1] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.dischargeEquipment)!
        JSAObject.hazardCategories.categories[18][2] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.observeSafeWorkDistance)!
        JSAObject.hazardCategories.categories[18][3] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.useFlashBurn)!
        JSAObject.hazardCategories.categories[18][4] = (readJSAObject.TOJSAHAZARDVOLTAGEValue?.useInsulatedGloves)!
        
        //Excavations
        JSAObject.hazardCategories.categories[19][0] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.haveExcavationPlan)!
        JSAObject.hazardCategories.categories[19][1] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.locatePipesByHandDigging)!
        JSAObject.hazardCategories.categories[19][2] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.deEnergizeUnderground)!
        JSAObject.hazardCategories.categories[19][3] = (readJSAObject.TOJSAHAZARDEXCAVATIONValue?.cseControls)!
        
        //mobile equipment
        JSAObject.hazardCategories.categories[20][0] = (readJSAObject.TOJSAHAZARDMOBILEValue?.assessEquipmentCondition)!
        JSAObject.hazardCategories.categories[20][1] = (readJSAObject.TOJSAHAZARDMOBILEValue?.controlAccess)!
        JSAObject.hazardCategories.categories[20][2] = (readJSAObject.TOJSAHAZARDMOBILEValue?.monitorProximity)!
        JSAObject.hazardCategories.categories[20][3] = (readJSAObject.TOJSAHAZARDMOBILEValue?.manageOverheadHazards)!
        JSAObject.hazardCategories.categories[20][4] = (readJSAObject.TOJSAHAZARDMOBILEValue?.adhereToRules)!
        
        //potential hazards
        
        for val in readJSAObject.TOJSASTEPSArrayValue
        {
            let obj = PotentialHazard()
            obj.taskStep = val.taskSteps.utf8DecodedString()
            obj.potentialHazards = val.potentialHazards.utf8DecodedString()
            obj.hazardControls = val.hazardControls.utf8DecodedString()
            obj.personResponsible = val.personResponsible.utf8DecodedString()
            
            JSAObject.potentialHazards.append(obj)
        }
        
        //stop the job
        
        for val in readJSAObject.TOJSASTOPArrayValue
        {
            var triggerArray = [String]()
            if val.lineDescription != ""
            {
                //                let obj = Trigger()
                //                obj.triggers.append(val.lineDescription)
                triggerArray.append(val.lineDescription.utf8DecodedString())
            }
            JSAObject.stopTheJob.append(contentsOf: triggerArray)
        }
        
        
//        //location
//        JSAObject.location.removeAll()
//        for each in readJSAObject.TOJSALOCATIONValue {
//
//            let location = Location()
//            location.facilityOrSite = each.facilityOrSite
//            location.hierarchyLevel = each.hierachyLevel
//            location.facility = each.facility
//            location.muwi = each.muwi
//            location.serialNo = each.serialNo
//            JSAObject.location.append(location)
//
//        }
        
//        // people
//        var peopleList = [People]()
//        for val in readJSAObject.TOPTWPEOPLEArrayValue
//        {
//
//            let people = People()
//            people.firstName = val.firstName
//            people.lastName = val.lastName
//            people.contactNumber = val.contactNumber
//            people.hasSignedCSE = val.hasSignedCSE
//            people.hasSignedJSA = val.hasSignedJSA
//            people.hasSignedCWP = val.hasSignedCWP
//            people.hasSignedHWP = val.hasSignedHWP
//            peopleList.append(people)
//
//        }
//        JSAObject.peopleList = peopleList
        
//        if JSAObject.status.lowercased() == "approved"
//        {
//            isJsaApproved = true
//            addPeopleBottomBar.isHidden = false
//
//        }
//        else
//        {
//            isJsaApproved = false
//            addPeopleBottomBar.isHidden = true
//        }
//
//        if JSAObject.hasCWP == 1 && (JSAObject.hasHWP == 0 || JSAObject.hasCSP == 0){
//            getAtmosphericTest()
//        }
//        else{
//
//            let jsaDetailService = JSADetailModelService(context: self.context)
//            let searchPredicate = NSPredicate(format: "permitNumber == %@", NSNumber(value: Int(selectedJSA)!))
//            let jsaDetail = jsaDetailService.get(withPredicate: searchPredicate)
//            if jsaDetail.count>0{
//                jsaDetailService.delete(id: jsaDetail[0].objectID)
//            }
//            _ = jsaDetailService.create(jsa: JSAObject)
//            jsaDetailService.saveChanges()
//        }
       
        if #available(iOS 10.0, *) {
            let dashBoardVC = Storyboard.DashBoard.instantiateViewController(withIdentifier: "CreateJSAVC") as! CreateJSAVC
            self.modalPresentationStyle = .overFullScreen
            self.navigationController?.pushViewController(dashBoardVC, animated: true)
            //            let navigationController = UINavigationController(rootViewController : dashBoardVC)
            //            self.present(navigationController, animated: false, completion: nil)
        } else {
            // Fallback on earlier versions
        }
        
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
            
            getTemplateDetails()
            
        }
  
    }

    func getTemplates()
    {
        tempDict = []
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
                        
                        if let jsonDict = JSON?.value(forKey: "data") as? NSArray ?? []{
                            if jsonDict.count == 0{
                                self.noTempLabel.isHidden = false
                            }
                            for json in jsonDict {
                                let template = json as! NSDictionary
                                let id = Int(template.value(forKey: "id") as? String ?? "0")
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
