package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.AuditReportDto;
import com.murphy.taskmgmt.dto.DOPVarianceDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.ShiftAuditLogDto;
import com.murphy.taskmgmt.entity.ShiftAuditLogDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ShiftAuditLogDao")
@Transactional
public class ShiftAuditLogDao extends BaseDao<ShiftAuditLogDo, ShiftAuditLogDto> {

	private static final Logger logger = LoggerFactory.getLogger(ShiftAuditLogDao.class);

	@Override
	protected ShiftAuditLogDo importDto(ShiftAuditLogDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ShiftAuditLogDo entity = new ShiftAuditLogDo();
		if (!ServicesUtil.isEmpty(fromDto.getShiftAuditId()))
			entity.setShiftAuditId(fromDto.getShiftAuditId());
		if (!ServicesUtil.isEmpty(fromDto.getResource()))
			entity.setResource(fromDto.getResource());
		if (!ServicesUtil.isEmpty(fromDto.getModifiedBy()))
			entity.setModifiedBy(fromDto.getModifiedBy());
		if (!ServicesUtil.isEmpty(fromDto.getModifiedAt()))
			entity.setModifiedAt(fromDto.getModifiedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentShift()))
			entity.setCurrentShift(fromDto.getCurrentShift());
		if (!ServicesUtil.isEmpty(fromDto.getPreviousShift()))
			entity.setPreviousShift(fromDto.getPreviousShift());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentBaseLoc()))
			entity.setBaseLocation(fromDto.getCurrentBaseLoc());
		if (!ServicesUtil.isEmpty(fromDto.getPrevBaseLoc()))
			entity.setPreviousBaseLocation(fromDto.getPrevBaseLoc());
		if (!ServicesUtil.isEmpty(fromDto.getShiftDay()))
			entity.setShiftDay(fromDto.getShiftDay());
	

		return entity;
	}

	@Override
	protected ShiftAuditLogDto exportDto(ShiftAuditLogDo entity) {
		ShiftAuditLogDto dto = new ShiftAuditLogDto();
		if (!ServicesUtil.isEmpty(entity.getShiftAuditId()))
			dto.setShiftAuditId(entity.getShiftAuditId());
		if (!ServicesUtil.isEmpty(entity.getResource()))
			dto.setResource(entity.getResource());
		if (!ServicesUtil.isEmpty(entity.getModifiedBy()))
			dto.setModifiedBy(entity.getModifiedBy());
		if (!ServicesUtil.isEmpty(entity.getModifiedAt()))
			dto.setModifiedAt(entity.getModifiedAt());
		if (!ServicesUtil.isEmpty(entity.getCurrentShift()))
			dto.setCurrentShift(entity.getCurrentShift());
		if (!ServicesUtil.isEmpty(entity.getPreviousShift()))
			dto.setPreviousShift(entity.getPreviousShift());
		if (!ServicesUtil.isEmpty(entity.getBaseLocation()))
			dto.setCurrentBaseLoc(entity.getBaseLocation());
		if (!ServicesUtil.isEmpty(entity.getPreviousBaseLocation()))
			dto.setPrevBaseLoc(entity.getPreviousBaseLocation());
		if (!ServicesUtil.isEmpty(entity.getShiftDay()))
			dto.setShiftDay(entity.getShiftDay());
		
		return dto;
	}

	public ResponseMessage createInstance(ShiftAuditLogDto dto,boolean isDirectCreate) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.CREATE_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			dto.setShiftAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
			dto.setModifiedAt(new Date());
			
			if (isDirectCreate) {
				create(dto);
			}else{
				
			//Set Shift Values
			if (dto.getCurrentShift().equalsIgnoreCase("1")) {
				dto.setPreviousShift(MurphyConstant.MORNING_SHIFT);
				dto.setCurrentShift(MurphyConstant.MORNING_SHIFT);
			} else if (dto.getCurrentShift().equalsIgnoreCase("2")) {
				dto.setPreviousShift(MurphyConstant.NIGHT_SHIFT);
				dto.setCurrentShift(MurphyConstant.NIGHT_SHIFT);
			} else if (dto.getCurrentShift().equalsIgnoreCase("3")) {
				dto.setPreviousShift(MurphyConstant.MORNING_SHIFT+","+MurphyConstant.NIGHT_SHIFT);
				dto.setCurrentShift(MurphyConstant.MORNING_SHIFT+","+MurphyConstant.NIGHT_SHIFT);
			} else {
				dto.setPreviousShift("");
				dto.setCurrentShift("");
			}
			if(!ServicesUtil.isEmpty(dto.getCurrentShift()) || !ServicesUtil.isEmpty(dto.getCurrentBaseLoc())){
			create(dto);
			}
			}
			responseMessage.setMessage(MurphyConstant.CREATED_SUCCESS);
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][TaskManagement][ShiftAuditLogDao][createInstance][error]" + e.getMessage());
		}

		return responseMessage;

	}

	@SuppressWarnings("unchecked")
	public JSONObject getAllShiftAuditDetails(int month,int pageNum) throws Exception {
		JSONObject jsonObject=new JSONObject();
        List<ShiftAuditLogDto> shiftAuditLogDtoList=new ArrayList<ShiftAuditLogDto>();
        int totalCount=0;
        
        //Get date for last one month or 3 month
        Calendar calender = Calendar.getInstance();
		calender.setTimeZone(TimeZone.getTimeZone("CST"));
		String currentDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "UTC",
				MurphyConstant.DATE_DB_FORMATE_SD,MurphyConstant.DATE_DB_FORMATE_SD);
		calender.add(Calendar.MONTH, -month);
		String startDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "UTC",
				MurphyConstant.DATE_DB_FORMATE_SD,MurphyConstant.DATE_DB_FORMATE_SD);
		System.out.println("[Murphy][DowntimeCaptureDao][PRO_COUNT][startDate]" + startDate);
		System.out.println("[Murphy][DowntimeCaptureDao][PRO_COUNT][currentDate]" + currentDate);
        
		String queryString = "SELECT * FROM SHIFT_AUDIT_LOG WHERE MODIFIED_AT BETWEEN '"+startDate+"'"
				+ " AND '"+currentDate+"' ORDER BY MODIFIED_AT DESC";
		
		logger.error("[Murphy][getAllShiftAuditDetails][CountQuery]" + queryString);
		Query query = this.getSession().createSQLQuery(queryString.trim());
		List<Object[]> list = query.list();
		if (!ServicesUtil.isEmpty(list)) {
			totalCount=list.size();
		}
		
		if (!ServicesUtil.isEmpty(pageNum) && pageNum > 0) {
			int first = (pageNum - 1) * MurphyConstant.SHIFT_AUDIT_PGE_SIZE;
			int last = MurphyConstant.SHIFT_AUDIT_PGE_SIZE;
			queryString += " LIMIT " + last + " OFFSET " + first + "";
		}
		
		logger.error("[Murphy][getAllShiftAuditDetails][query]" + queryString);
		Query q = this.getSession().createSQLQuery(queryString.trim());
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			ShiftAuditLogDto dto = null;
			for (Object[] obj : resultList) {
				dto = new ShiftAuditLogDto();
				dto.setShiftAuditId(ServicesUtil.isEmpty(obj[0])?null:((String) obj[0]));
				dto.setResource(ServicesUtil.isEmpty(obj[1])?null:((String) obj[1]));
				dto.setModifiedBy(ServicesUtil.isEmpty(obj[2])?null:((String) obj[2]));
				dto.setModifiedAt(ServicesUtil.isEmpty(obj[3])?null:((Date) obj[3]));
				dto.setCurrentShift(ServicesUtil.isEmpty(obj[4])?null:((String) obj[4]));
				dto.setPreviousShift(ServicesUtil.isEmpty(obj[5])?null:((String) obj[5]));
				dto.setCurrentBaseLoc(ServicesUtil.isEmpty(obj[6])?null:((String) obj[6]));
				dto.setPrevBaseLoc(ServicesUtil.isEmpty(obj[7])?null:((String) obj[7]));
				dto.setShiftDay(ServicesUtil.isEmpty(obj[8])?null:((String) obj[8]));
				shiftAuditLogDtoList.add(dto);
			}
		}
		jsonObject.put("shiftAuditLogDtoList",shiftAuditLogDtoList);
		jsonObject.put("pageCount",MurphyConstant.SHIFT_AUDIT_PGE_SIZE);
		jsonObject.put("totalCount",totalCount);
		return jsonObject;
	}

	public List<ShiftAuditLogDto> getReport(int durationInMonths) {
		List<ShiftAuditLogDto> responseDto=new ArrayList<ShiftAuditLogDto>();
		try{
			//Get date for last one month or 3 month
	        Calendar calender = Calendar.getInstance();
			calender.setTimeZone(TimeZone.getTimeZone("CST"));
			String currentDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "UTC",
					MurphyConstant.DATE_DB_FORMATE_SD,MurphyConstant.DATE_DB_FORMATE_SD);
			calender.add(Calendar.MONTH, -durationInMonths);
			String startDate = ServicesUtil.convertFromZoneToZoneString(calender.getTime(), null, null, "UTC",
					MurphyConstant.DATE_DB_FORMATE_SD,MurphyConstant.DATE_DB_FORMATE_SD);
			System.out.println("[Murphy][DowntimeCaptureDao][PRO_COUNT][startDate]" + startDate);
			System.out.println("[Murphy][DowntimeCaptureDao][PRO_COUNT][currentDate]" + currentDate);
	        
			String queryString = "SELECT MODIFIED_BY,MODIFIED_AT,RESOURCE,CURRENT_SHIFT,PREVIOUS_SHIFT,BASE_LOC,PREV_BASE_LOC,SHIFT_DAY"
					+ " FROM SHIFT_AUDIT_LOG WHERE MODIFIED_AT BETWEEN '"+startDate+"'"
					+ " AND '"+currentDate+"' ORDER BY MODIFIED_AT DESC";
			
			logger.error("[Murphy][ShiftAuditLogDao][getReport]" + queryString);
			Query query = this.getSession().createSQLQuery(queryString.trim());
			List<Object[]> auditDtoList = query.list();
			
			if(!ServicesUtil.isEmpty(auditDtoList)){
				ShiftAuditLogDto auditDto=null;
				for(Object[] obj : auditDtoList){
					auditDto = new ShiftAuditLogDto();
					auditDto.setModifiedBy(ServicesUtil.isEmpty(obj[0])?null:((String) obj[0]));
					auditDto.setModifiedAt(obj[1] == null ? null : ServicesUtil.convertFromZoneToZone(null, obj[1] ,MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE,MurphyConstant.DATE_DB_FORMATE, MurphyConstant.DATE_DISPLAY_FORMAT));
					auditDto.setResource(ServicesUtil.isEmpty(obj[2])?null:((String) obj[2]));
					auditDto.setCurrentShift(ServicesUtil.isEmpty(obj[3])?null:((String) obj[3]));
					auditDto.setPreviousShift(ServicesUtil.isEmpty(obj[4])?null:((String) obj[4]));
					auditDto.setCurrentBaseLoc(ServicesUtil.isEmpty(obj[5])?null:((String) obj[5]));
					auditDto.setPrevBaseLoc(ServicesUtil.isEmpty(obj[6])?null:((String) obj[6]));
					auditDto.setShiftDay(ServicesUtil.isEmpty(obj[7])?null:((String) obj[7]));
					responseDto.add(auditDto);
				}
			}
			return responseDto;
		}catch(Exception e){
			logger.error("[Murphy][TaskManagement][ShiftAuditLogDao][getReport][error]"+e.getMessage());
		}
		return null;
	}
	
	

}
