package com.murphy.taskmgmt.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.util.ServicesUtil;
import com.murphy.taskmgmt.dao.ActivityLogDao;
import com.murphy.taskmgmt.dao.EICommentDao;
import com.murphy.taskmgmt.dao.EIContractorDao;
import com.murphy.taskmgmt.dao.EIContractorNameDao;
import com.murphy.taskmgmt.dao.EIFormDao;
import com.murphy.taskmgmt.dao.EIReasonDao;
import com.murphy.taskmgmt.dao.EIShiftConfigDao;
import com.murphy.taskmgmt.dao.EiAttachmentDao;
import com.murphy.taskmgmt.dao.EnergyIsolationPdfDao;
import com.murphy.taskmgmt.dao.IsolationDetailDao;
import com.murphy.taskmgmt.dao.SsdBypassHeaderDao;
import com.murphy.taskmgmt.dto.ActivityLogDto;
import com.murphy.taskmgmt.dto.AffectedPersonnelDto;
import com.murphy.taskmgmt.dto.EIContractorDto;
import com.murphy.taskmgmt.dto.EIFormDto;
import com.murphy.taskmgmt.dto.EIFormListResponseDto;
import com.murphy.taskmgmt.dto.EiAttachmentDto;
import com.murphy.taskmgmt.dto.EnergyIsolationDto;
import com.murphy.taskmgmt.dto.IsolationDetailDto;
import com.murphy.taskmgmt.dto.IsolationDetailsDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.interfaces.EnergyIsolationFacadeLocal;
import com.murphy.taskmgmt.util.MailAlertUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.UniqueSeq;

@Service("energyIsolationFacade")
public class EnergyIsolationFacade implements EnergyIsolationFacadeLocal {


	private static final Logger logger = LoggerFactory.getLogger(EnergyIsolationFacade.class);

	public EnergyIsolationFacade() {
	}
	
	@Autowired
	private EIFormDao eIFormDao;
	
	@Autowired
	private EIContractorDao eIContractorDao;
	
	@Autowired
	private EICommentDao eICommentDao;
	
	@Autowired
	private ActivityLogDao activityLogDao;
	
	@Autowired
	private IsolationDetailDao isolationDetailDao;
	
	@Autowired
	private EiAttachmentDao eiAttachmentDao;
	
	@Autowired
	private EIReasonDao eIReasonDao;
	
	@Autowired
	private SsdBypassHeaderDao ssdBypassHeaderDao;
	
	@Autowired
	private EIContractorNameDao eIContractorNameDao;
	
	@Autowired
	private EIShiftConfigDao eIShiftConfigDao;
	
	@Autowired
	private EnergyIsolationPdfDao energyIsolationPdfDao;
	
	
	@SuppressWarnings("static-access")
	public ResponseMessage createForm(EIFormDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.EI_FORM + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		EnergyIsolationDto energyIsolationDto=new EnergyIsolationDto();
		try {
			dto.setUpdatedAt(new Date());
			String formId = null;
			String id = null;
			if (dto.isUpdate()) {
				String idTemp = dto.getId();
				dto.setId(dto.getIdOrg());
				formId = dto.getFormId();
				eIFormDao.update(dto);
				eIContractorDao.createContractor(dto.getContractorDetailList(), formId,MurphyConstant.UPDATE);
				activityLogDao.updateList(dto.getActivityLogList(), dto.getId(), formId);
				isolationDetailDao.updateList(dto.getIsolationList(), formId);
				eiAttachmentDao.createList(dto.getAttachmentList(), formId, null);
				eICommentDao.updateEIComment(dto.getCommentList(), formId);
				if (dto.getStatus().equals("Closed")) {
					responseMessage.setMessage(MurphyConstant.EI_FORM + " " + idTemp + " " + MurphyConstant.CLOSED + " " + MurphyConstant.SUCCESSFULLY);
				} else {
					responseMessage.setMessage(MurphyConstant.EI_FORM + " " + idTemp + " " + MurphyConstant.UPDATE_SUCCESS);
				}
			} else {
				formId = UUID.randomUUID().toString().replaceAll("-", "");
				id = String.valueOf(UniqueSeq.getInstance().getNext());
				dto.setFormId(formId);
				dto.setIdOrg(id);
				dto.setCreatedAt(new Date());
				dto.setLastShiftChange(new Date());
				dto.setAcknowledged(true);
				dto.setUserGroup(getUserGroupByLocation(dto.getLocationId()));
				eIFormDao.create(dto);
				
				eIContractorDao.createContractor(dto.getContractorDetailList(), formId,MurphyConstant.CREATE);
				if (dto.getActivityLogList() == null || dto.getActivityLogList().isEmpty()) {
					dto.setActivityLogList(new ArrayList<ActivityLogDto>());
					dto.getActivityLogList().add(activityLogDao.createActivity(formId, dto.getPermIssueName(), dto.getPermIssueLoginName()));
				}
				activityLogDao.createList(dto.getActivityLogList(), formId);
				isolationDetailDao.createList(dto.getIsolationList(), formId);
				eiAttachmentDao.createList(dto.getAttachmentList(), formId, null);
				eICommentDao.createEIComment(dto.getCommentList(), formId);
				responseMessage.setMessage(MurphyConstant.EI_FORM + " " + id.substring(id.length() - 6) + " " + MurphyConstant.CREATED_SUCCESS);
				energyIsolationDto=importObjectAtoObjectB(dto);
				generateEIPdfTemplate(energyIsolationDto);
			}
			
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
		catch(Exception e) {
			logger.error("[Murphy][EnergyIsolation][createForm][error] " + e.getMessage());
		}
		return responseMessage;
	}
	
	
	//Wrapper Class For Pdf Generation
	private EnergyIsolationDto importObjectAtoObjectB(EIFormDto dto) {
		EnergyIsolationDto energyIsolation=new EnergyIsolationDto();
		try{
		    energyIsolation.setFormId(dto.getIdOrg().substring(dto.getIdOrg().length() - 6));
			energyIsolation.setPermitNo(dto.getIdOrg());
			energyIsolation.setPlannedDateofWork(ServicesUtil.convertFromZoneToZoneString(dto.getDate(),null,"","","",MurphyConstant.DISPLAY_DATETIME_FORMAT));
			energyIsolation.setFacility(dto.getLocationName());
			energyIsolation.setPermitHolder(dto.getPermHoldName());
			energyIsolation.setContractorPerformingWork("");
			energyIsolation.setEstCompletionTime(ServicesUtil.convertFromZoneToZoneString(dto.getExpectTime(),null,"","","",MurphyConstant.DISPLAY_DATETIME_FORMAT));
			energyIsolation.setEquipmentTag(dto.getEquipId());
			energyIsolation.setWorkOrderNo(dto.getWorkOrderNum());
            energyIsolation.setEnergyIsolatedType(dto.getEnergyType());
            energyIsolation.setIsolationReason(dto.getReason());
            energyIsolation.setJsaIsReviewed(dto.isJSAReviewed()+"");
            energyIsolation.setIsLOTONotified(dto.isEmpNotified()+"");
            energyIsolation.setPreJobPermitIssuer(dto.getPermIssueName());
            energyIsolation.setPreJobPermitHolder(dto.getPermHoldName());
            energyIsolation.setOtherHazards(dto.getOtherHazards());
            energyIsolation.setServicePermitHolder(dto.getPermHoldName());
            energyIsolation.setServicePermitIssuer(dto.getPermIssueName());
            energyIsolation.setIsolationPermitHolder(dto.getPermHoldName());
            energyIsolation.setIsolationPermitIssuer(dto.getPermIssueName());
            energyIsolation.setIsolationHolderDate(ServicesUtil.convertFromZoneToZoneString(dto.getPermHoldTime(),null,"","","",MurphyConstant.DISPLAY_DATE_FORMAT));
            energyIsolation.setIsolationIssuerDate(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.DISPLAY_DATE_FORMAT));
            energyIsolation.setIsolationHolderTime(ServicesUtil.convertFromZoneToZoneString(dto.getPermHoldTime(),null,"","","",MurphyConstant.PIG_DATE_DISPLAY_FORMAT));
            energyIsolation.setIsolationIssuerTime(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.PIG_DATE_DISPLAY_FORMAT));
            energyIsolation.setPreJobSafetyDate1(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.DISPLAY_DATETIME_FORMAT));
            energyIsolation.setPreJobSafetyDate2(ServicesUtil.convertFromZoneToZoneString(dto.getPermHoldTime(),null,"","","",MurphyConstant.DISPLAY_DATETIME_FORMAT));
            energyIsolation.setServiceHolderDate(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.DISPLAY_DATE_FORMAT));
            energyIsolation.setServiceHolderTime(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.PIG_DATE_DISPLAY_FORMAT));
            energyIsolation.setServiceIssuerDate(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.DISPLAY_DATE_FORMAT));
            energyIsolation.setServiceIssuerTime(ServicesUtil.convertFromZoneToZoneString(dto.getPermIssueTime(),null,"","","",MurphyConstant.PIG_DATE_DISPLAY_FORMAT));
            energyIsolation.setIsLOTORemovalNotified(dto.isLotoRemoved()+"");
            energyIsolation.setIsAffectedLockRemoved(dto.isWAInspected()+"");
            energyIsolation.setIsWorkAreaInspected(dto.isWAInspected()+"");
            energyIsolation.setIsolationDetailsDtoList(importISolationObjectData(dto.getIsolationList(),dto.getLocationName()));
            energyIsolation.setAffectedPersonnelList(importAffectedPersonnelDetails(dto.getContractorDetailList()));
            energyIsolation.setAffectedPersonnelIdList(dto.getAffectedPersonnelIdList());
            logger.error("EnergyIsolationDto"+energyIsolation);
            return energyIsolation;			
			
		}
		catch(Exception e){
			logger.error("[Murphy][EnergyIsolation][createForm][error] " + e.getMessage());
            e.printStackTrace();
		}
		return energyIsolation;
		
	}

    //Wrapper Class For AffectedPersonnel Table
	private List<AffectedPersonnelDto> importAffectedPersonnelDetails(List<EIContractorDto> contractorDetailList) {
	List<AffectedPersonnelDto> affectedPersonnelDtosList=new ArrayList<>();
	AffectedPersonnelDto affectedPersonal=new AffectedPersonnelDto();
	try{
		 if(!ServicesUtil.isEmpty(contractorDetailList)){
	     for(EIContractorDto contractorDto:contractorDetailList){
	     affectedPersonal=new AffectedPersonnelDto();
	     affectedPersonal.setName(contractorDto.getContractorName());
	     affectedPersonal.setEmail(contractorDto.getEmailId());
	     affectedPersonal.setSignature(contractorDto.getSignatureContent());
	     affectedPersonal.setContractorPerformingWork(contractorDto.getContractorPerformingWork());
	     affectedPersonnelDtosList.add(affectedPersonal);
	     }
		 }
	}
	catch(Exception e){
		logger.error("[Murphy][EnergyIsolation][createForm][error] " + e.getMessage());
        e.printStackTrace();	
	}
	return affectedPersonnelDtosList;
	}

  //Wrapper Class For IsolationDetailTable Table
	private List<IsolationDetailsDto> importISolationObjectData(List<IsolationDetailDto> isolationList,String location) {
        List<IsolationDetailsDto> isolationDtoList=new ArrayList<>();
		IsolationDetailsDto dto=new IsolationDetailsDto();
		try{
		   if(!ServicesUtil.isEmpty(isolationList)){
		   for(IsolationDetailDto isolation :isolationList){
		   dto=new IsolationDetailsDto();
		   dto.setIsolationDetail(isolation.getDescription());
		   dto.setLocation(location);
		   dto.setIsEnergyStored(isolation.isEIStored()+"");
		   dto.setIsEquipmentTested(isolation.isEquipTested()+"");
		   dto.setIsolationDate(ServicesUtil.convertFromZoneToZoneString(isolation.getIsolationDate(),null,"","","","MMM dd, yyyy hh:mm:ss a"));
		   dto.setReInstateMentDate(ServicesUtil.convertFromZoneToZoneString(isolation.getReinstatement(),null,"","","","MMM dd, yyyy hh:mm:ss a"));
		   isolationDtoList.add(dto);
		   }
		   }
		}
		catch(Exception e){
		logger.error("[Murphy][EnergyIsolation][importISolationObjectData][Exception] " + e.getMessage());
            e.printStackTrace();
		}
		return isolationDtoList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EIFormListResponseDto getFormsByLocation(String locationCode, String locationType, String monthTime, int weekTime,
			int page, int page_size,boolean isActive) {
		EIFormListResponseDto response = null;
		ArrayList<String> locCodeList = null;
		String locCodeString = null;
		if (!ServicesUtil.isEmpty(locationCode)) {
			List<String> locCodes = Arrays.asList(locationCode.split(","));
			locCodeList = ssdBypassHeaderDao.getLocationHierarchy(locCodes, new ArrayList());
			locCodeString = ServicesUtil.getStringFromList(locCodeList);
		}
		response = eIFormDao.getEIFormByLoc(locCodeString, monthTime, weekTime, page, page_size,isActive);
		response.setStatusCountList(eIFormDao.rolledUpData(locCodeString));
		return response;
	}

	public EIFormDto getFormById(String formId) {
		EIFormDto response = new EIFormDto();
		response.setFormId(formId);
		try {
			response = eIFormDao.getByKeys(response);
			response.setActivityLogList(activityLogDao.getByFk(formId));
			response.setIsolationList(isolationDetailDao.getByFk(formId));
			response.setAttachmentList(eiAttachmentDao.getAttachment(formId, null));
			response.setCommentList(eICommentDao.getByFk(formId));
			response.setContractorDetailList(eIContractorDao.getByFk(formId));
			
			// START 03rd Sep 2019
			List<EiAttachmentDto> attachments = eiAttachmentDao.getAttachment(formId, null);
			
			if (attachments == null) {
				attachments = new ArrayList<EiAttachmentDto>();
			}
			
			if (response.getIsolationList() != null) {
				for (IsolationDetailDto isolationDetailDto : response.getIsolationList()) {
					if (isolationDetailDto.getAttachmentList() != null) {
						attachments.addAll(isolationDetailDto.getAttachmentList());
					}
				}
			}
			
			if (attachments.size() == 0) {
				attachments = null;
			}
			
			response.setAttachmentList(attachments);
			// END 03rd Sep 2019
			
		} catch (Exception e) {
			logger.error("[Murphy][EnergyIsolation][getFormById][error] " + e.getMessage());
		}
		return response;
	}
	
	public ResponseMessage updateActivityStatus(String id, String value) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.EI_FORM + " " + MurphyConstant.UPDATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			String tempId = activityLogDao.updateById(id, value);
			responseMessage.setMessage(MurphyConstant.EI_FORM + " " + tempId.substring(tempId.length() - 6) + " " + MurphyConstant.UPDATE_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][EnergyIsolation][updateActivityStatus][error] " + e.getMessage());
		}
		return responseMessage;
	}
	
	public ResponseMessage createActivity(ActivityLogDto activity) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.EI_FORM + " " + MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			activity.setId(id);
			activity.setIsApproved("Null");
			activity.setPermIssueTime(new Date());
			activity.setType("PERMIT ISSUER");
			activityLogDao.create(activity);
			EIFormDto eiDto = new EIFormDto();
			String formId = activity.getFormId();
			eiDto.setFormId(formId);
			eiDto = eIFormDao.getByKeys(eiDto);
			
			List<String> formIdList = new ArrayList<String>();
			formIdList.add(formId);
			eIFormDao.setAck(formIdList);
			
			String locationName = eiDto.getLocationName();
			String equipId = eiDto.getEquipId();
			String tempId = eiDto.getId();
			activityLogDao.notifyUser(activity, locationName, equipId);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 15);
			Timer timer = new Timer();
			
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					String isApproved = activityLogDao.getActivityStatus(id);
					if (isApproved.equals("Null")) {
						activityLogDao.updateStatusById(id, "False");
						List<String> formIdList = new ArrayList<String>();
						formIdList.add(formId);
						eIFormDao.resetAck(formIdList);
					}
				}
			}, cal.getTime());
			
			responseMessage.setMessage(MurphyConstant.EI_FORM + " " + tempId.substring(tempId.length() - 6) + " " + MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (ExecutionFault e) {
			e.printStackTrace();
		} catch (InvalidInputFault e) {
			e.printStackTrace();
		} catch (NoResultFault e) {
			e.printStackTrace();
		}
		return responseMessage;
	}
	
	public List<String> getContractorList() {
		List<String> contractorList = null;
		contractorList = eIContractorNameDao.getAllContractorNames();
		return contractorList;
	}
	
	public List<String> getShiftList() {
		List<String> shiftList = null;
		shiftList = eIShiftConfigDao.getAllShifts();
		return shiftList;
	}
	
	public List<String> getReasonList() {
		List<String> reasonList = null;
		reasonList = eIReasonDao.getAllReasons();
		return reasonList;
	}
	
	// original pushDataForNotification method
//	public void pushDataForNotification() {
////		Get all active forms. Filter out those which are of previous shift.
//		List<String> activeForms = eIFormDao.getActiveForms();
//		if(ServicesUtil.isEmpty(activeForms)) {
//			return;
//		}
//		if (activeForms != null && activeForms.size() > 0) {
//			activityLogDao.createRocActivity(activeForms);
////      Delete old ROC activities
//			activityLogDao.deleteRocActivities(activeForms);
////		Create ROC activity.
////			Reset acknowledged flag in Ei form.
//			eIFormDao.resetAck(activeForms);
////		Push data in database.
//			eIFormDao.pushDataToNotifyRoc(activeForms);
//		}
//		logger.error("Push data method ends");
//	}
	
	public String getUserGroupByLocation(String loc) {
		String userGroup = null;
		List<String> locationCodes = new ArrayList<>();
		locationCodes.add(loc);
		loc = ssdBypassHeaderDao.getLocationByLocationCode(locationCodes);
		if ("Catarina".equalsIgnoreCase(loc.trim())) {
			userGroup = "IOP_TM_ROC_Catarina";
		} else if ("Tilden Central".equalsIgnoreCase(loc.trim()) || "Tilden North".equalsIgnoreCase(loc.trim())
				|| "Tilden East".equalsIgnoreCase(loc.trim())) {
			loc = "CentralTilden";
			userGroup = "IOP_TM_ROC_CentralTilden";
		} else if ("Tilden West".equalsIgnoreCase(loc.trim())) {
			loc = "WestTilden";
			userGroup = "IOP_TM_ROC_WestTilden";
		} else if ("Karnes North".equalsIgnoreCase(loc.trim()) || "Karnes South".equalsIgnoreCase(loc.trim())) {
			loc = "Karnes";
			userGroup = "IOP_TM_ROC_Karnes";
		}
		return userGroup;
	}
	
	public String test(String id, String value) {
		activityLogDao.updateById(id, value);
		return "Success";
	}
	
	public String deleteLock(String id) {
		return isolationDetailDao.deleteLock(id);
	}

	@Override
	public String generateEIPdfTemplate(EnergyIsolationDto dto) {
		MailAlertUtil mailAlert=new MailAlertUtil();
		File file=null;
		StringBuilder builder=new StringBuilder();
		String submittedDate=null;
		
		try{
			submittedDate=ServicesUtil.convertFromZoneToZoneString(new Date(),null,"","","","MMM dd, yyyy");
			//Generate Dynamic Pdf with JRXML File
			file=energyIsolationPdfDao.generateEIPdfTemplate(dto);
			builder.append("Attached is the Energy Isolation Form ");
			builder.append(dto.getFormId()+" for the job on site "+dto.getFacility()+" submitted on "+submittedDate);
			builder.append(" for your review.");
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			return mailAlert.sendMailAlert(dto.getAffectedPersonnelIdList(),MurphyConstant.ENERGYISOLATION_SUBJECT+dto.getFormId()+" - "+dto.getFacility(),builder.toString(), "Energy Isolation - "+dto.getFormId()+" - "+submittedDate+" - "+dto.getFacility()+".pdf",null,file);
			
			
		}
		catch(Exception e){
			logger.error("[Murphy][EnergyIsolationFacade][generateEIPdfTemplate][Exception]"+e.getMessage());
		}
		return MurphyConstant.FAILURE;
	}


	@Override
	public ResponseMessage sendEmail(String formId, String affectedPersonnelIdList) {
		ResponseMessage msg=new ResponseMessage();
		msg.setMessage("Mail Triggered Failed");
		msg.setStatus(MurphyConstant.FAILURE);
		msg.setStatusCode(MurphyConstant.CODE_FAILURE);
		EIFormDto response = new EIFormDto();
		EnergyIsolationDto energyIsolation=new EnergyIsolationDto();
		String mailStatus=null;
		try{
			response.setFormId(formId);
			//Retrieve Data from DB By FormId
			response = eIFormDao.getByKeys(response);
            response.setIsolationList(isolationDetailDao.getByFk(formId));
            response.setContractorDetailList(eIContractorDao.getByFk(formId));
            response.setAffectedPersonnelIdList(affectedPersonnelIdList);
            //Wrapping the Data to Pdf Dto Object
            energyIsolation=importObjectAtoObjectB(response);
            //Method to generate Jrxml File and trigger mail to EI Users
            mailStatus=generateEIPdfTemplate(energyIsolation);
            if(mailStatus.equalsIgnoreCase(MurphyConstant.SUCCESS)){
            	msg.setMessage("Mail Triggered Successfully");
        		msg.setStatus(MurphyConstant.SUCCESS);
        		msg.setStatusCode(MurphyConstant.CODE_SUCCESS);
            }
            return msg;
		}
		catch(Exception ex){
			logger.error("[Murphy][EnergyIsolationFacade][generateEIPdfTemplate][Exception]"+ex.getMessage());
			ex.printStackTrace();
			msg.setMessage("Mail Triggered Failed");
			msg.setStatus(MurphyConstant.FAILURE);
			msg.setStatusCode(MurphyConstant.CODE_FAILURE);

		}
		return msg;
	}
	
	
	//added as part of EI incident
	public void pushDataForNotification() {
		// Get all active forms. Filter out those which are of previous shift.
		List<String> activeForms = eIFormDao.getActiveForms();
		if(ServicesUtil.isEmpty(activeForms)) {
		return;
		}
		if (activeForms != null && activeForms.size() > 0) {
		// Delete old ROC activities
		activityLogDao.deleteRocActivities(activeForms);
		// Create ROC activity.
		activityLogDao.createRocActivity(activeForms);

		 

		// Reset acknowledged flag in Ei form.
		 eIFormDao.resetAck(activeForms);
		// Push data in database.
		eIFormDao.pushDataToNotifyRoc(activeForms);
		}
		logger.error("Push data method ends");
		}
}
