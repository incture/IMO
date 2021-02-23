package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.blackline.Coordinates;
import com.murphy.geotab.NearestUserDto;
import com.murphy.geotab.NearestUserDtoResponse;
import com.murphy.taskmgmt.dto.BaseDto;
import com.murphy.taskmgmt.dto.OperatorsAvailabilityDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.StopTimeDto;
import com.murphy.taskmgmt.dto.UserIDPMappingDto;
import com.murphy.taskmgmt.entity.BaseDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
import com.murphy.taskmgmt.util.SpringContextBridge;

@Repository("BlackLineDao")
public class BlackLineDao extends BaseDao<BaseDo, BaseDto> {

	private static final Logger logger = LoggerFactory.getLogger(BlackLineDao.class);
	
	@Autowired
	private StopTimeDao stopTimeDao;

	@SuppressWarnings("unchecked")
	public Coordinates getLatLongByLocationCode(String locationCode) {
		Coordinates coordinates = null;
		if (!ServicesUtil.isEmpty(locationCode)) {
			Query query = this.getSession().createSQLQuery(
					"SELECT LATITUDE, LONGITUDE FROM LOCATION_COORDINATE WHERE LOCATION_CODE = '" + locationCode + "'");
			List<Object[]> result = query.list();
			if (!ServicesUtil.isEmpty(result) && result.size() > 0) {
				Object[] object = result.get(0);
				if (!ServicesUtil.isEmpty(object)) {
					coordinates = new Coordinates(
							ServicesUtil.isEmpty(object[0]) ? null : ServicesUtil.getBigDecimal(object[0]),
							ServicesUtil.isEmpty(object[1]) ? null : ServicesUtil.getBigDecimal(object[1]));
				}
			}
		}
		return coordinates;
	}

	
	@SuppressWarnings("unchecked")
	public List<UserIDPMappingDto> getUserAndTaskCountDetails(List<String> roles) {  //getUserAndTaskCountDetails(List<String> roles)
		
		List<UserIDPMappingDto> userDtoList = null;
		UserIDPMappingDto user = null;
		String querString = null ;
		if(!ServicesUtil.isEmpty(roles)){
			querString ="SELECT SERIAL_ID,USER_EMAIL,TASK_ASSIGNABLE,USER_FIRST_NAME,USER_LAST_NAME,TASK_COUNT,P_ID,BLACKLINE_ID FROM (SELECT IDP.SERIAL_ID AS SERIAL_ID , IDP.USER_EMAIL AS USER_EMAIL, IDP.TASK_ASSIGNABLE AS TASK_ASSIGNABLE, IDP.USER_FIRST_NAME AS USER_FIRST_NAME, "
					+ "IDP.USER_LAST_NAME AS USER_LAST_NAME,IDP.USER_ROLE AS USER_ROLE , COUNT(TE.TASK_ID) AS TASK_COUNT,IDP.P_ID AS P_ID,IDP.BLACKLINE_ID AS BLACKLINE_ID FROM TM_USER_IDP_MAPPING AS IDP LEFT OUTER JOIN TM_TASK_OWNER AS TO ON IDP.USER_EMAIL = TO.TASK_OWNER "
					+ " LEFT OUTER JOIN TM_TASK_EVNTS AS TE ON TO.TASK_ID = TE.TASK_ID "
					+ " AND TE.STATUS IN ('"+ MurphyConstant.ASSIGN +"','"+ MurphyConstant.INPROGRESS +"') AND TE.ORIGIN IN ('"+MurphyConstant.DISPATCH_ORIGIN+"') "
					+ " GROUP BY IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME,IDP.P_ID,IDP.USER_ROLE,IDP.BLACKLINE_ID ) WHERE ";
		    
		    int i = 0;
			for(String role : roles){
				if(i != 0){
					querString = querString + 	" OR ";
				}else{
					i++;
				}
				querString = querString + " USER_ROLE like '%"+role+"%'";	
			}
		}else{
			querString ="SELECT IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME, "
				+ " COUNT(TE.TASK_ID) AS TASK_COUNT,IDP.P_ID,IDP.BLACKLINE_ID FROM TM_USER_IDP_MAPPING AS IDP LEFT OUTER JOIN TM_TASK_OWNER AS TO ON IDP.USER_EMAIL = TO.TASK_OWNER "
				+ " LEFT OUTER JOIN TM_TASK_EVNTS AS TE ON TO.TASK_ID = TE.TASK_ID "
				+ " AND TE.STATUS IN ('"+ MurphyConstant.ASSIGN +"','"+ MurphyConstant.INPROGRESS +"') AND TE.ORIGIN IN ('"+MurphyConstant.DISPATCH_ORIGIN+"') "
				+ " GROUP BY IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME,IDP.P_ID,IDP.BLACKLINE_ID";
		}
		logger.error("[UserIDPMappingDao][getUserAndTaskCountDetails][querString] : "+ querString);
		
//		List<UserIDPMappingDto> userDtoList = null;
//		UserIDPMappingDto user = null;
//		Query query = this.getSession().createSQLQuery("SELECT IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME, "
//				+ " COUNT(TE.TASK_ID) AS TASK_COUNT,IDP.P_ID,IDP.BLACKLINE_ID FROM TM_USER_IDP_MAPPING AS IDP LEFT OUTER JOIN TM_TASK_OWNER AS TO ON IDP.USER_EMAIL = TO.TASK_OWNER "
//				+ " LEFT OUTER JOIN TM_TASK_EVNTS AS TE ON TO.TASK_ID = TE.TASK_ID "
//				+ " AND TE.STATUS IN ('"+ MurphyConstant.ASSIGN +"','"+ MurphyConstant.INPROGRESS +"') AND TE.ORIGIN IN ('"+MurphyConstant.DISPATCH_ORIGIN+"') "
//				+ " GROUP BY IDP.SERIAL_ID, IDP.USER_EMAIL, IDP.TASK_ASSIGNABLE, IDP.USER_FIRST_NAME, IDP.USER_LAST_NAME,IDP.P_ID,IDP.BLACKLINE_ID");
//		logger.error("[Murphy][UserIDPMappingDao][getUserDetailsFromDB][Query]"+query);
		
		Query query = this.getSession().createSQLQuery(querString);
		List<Object[]> resultList = query.list();
		if(!ServicesUtil.isEmpty(resultList)) {
			userDtoList = new ArrayList<UserIDPMappingDto>();
			for(Object[] object : resultList) {
				user = new UserIDPMappingDto();
				if(!ServicesUtil.isEmpty(object[0]))
				user.setSerialId((String) object[0]);
				user.setUserEmail((String) object[1]);
				user.setTaskAssignable((String) object[2]);
				user.setUserFirstName((String) object[3]);
				user.setUserLastName((String) object[4]);
				user.setTaskCount(ServicesUtil.isEmpty(object[5]) ? null : ((BigInteger) object[5]).intValue());
				user.setpId((String) object[6]);
				if(!ServicesUtil.isEmpty(object[7]))
			    user.setBlackLineId((String) object[7]);
				userDtoList.add(user);
				//				logger.error("[UserIDPMappingDao][getUserDetailsFromDB][user] : "+ user);
			}
		}
		//		logger.error("[UserIDPMappingDao][getUserDetailsFromDB][userDtoList] : "+ userDtoList);
		return userDtoList;
	}
	@Override
	protected BaseDo importDto(BaseDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseDto exportDto(BaseDo entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getBlackLineId(String userEmail){
		return userEmail;
		
	}
	
	@SuppressWarnings("unchecked")
	public String getBlackLineIdbyEmail(String userEmail) {

		try {
			String queryString = "SELECT BLACKLINE_ID FROM TM_USER_IDP_MAPPING WHERE USER_EMAIL ='" + userEmail + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<String> response = (List<String>) q.list();
			if (!ServicesUtil.isEmpty(response)) {
				return response.get(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][BlackLineDao][getBlackLineIdbyEmail][Exception]" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public NearestUserDtoResponse getUsersByAbility(OperatorsAvailabilityDto dto) {
		String commaSeparatedDesignation = "";
		List<String> designationList = new ArrayList<String>();
		List<String> emailList = new ArrayList<String>();
		NearestUserDtoResponse NearestUserDtoResponse = new NearestUserDtoResponse();
		List<NearestUserDto> nearestUsersList = new ArrayList<NearestUserDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			StopTimeDto competency = stopTimeDao.getStopTimeByCategory(dto.getClassification(),
					dto.getSubClassifiaction());
			logger.error("[Murphy][BlackLineDao][getUsersByAbility][competencyOfclassificationAndSubClassification]"
					+ competency);
			if(competency == null){
				NearestUserDtoResponse.setNearestUsers(dto.getNearestUserDto());
				responseMessage.setMessage("No Competency Available for selected Classifiaction and SubClassification");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				NearestUserDtoResponse.setResponseMessage(responseMessage);
				return NearestUserDtoResponse;
			}
			if (competency.getProA() != 0)
				designationList.add(MurphyConstant.PRO_A);
			if (competency.getProB() != 0)
				designationList.add(MurphyConstant.PRO_B);
			if (competency.getObx() != 0){
				designationList.add(MurphyConstant.OBX_B);
			    designationList.add(MurphyConstant.OBX_C);
			}
			if (competency.getSse() != 0)
				designationList.add(MurphyConstant.SSE);

			
			for (int i = 0; i < designationList.size(); i++) {
				if (i < designationList.size() - 1)
					commaSeparatedDesignation += designationList.get(i) + "','";
				else
					commaSeparatedDesignation += designationList.get(i);
			}
			String query = "SELECT EMP_EMAIL FROM EMP_INFO WHERE DESIGNATION IN ('" + commaSeparatedDesignation + "')";
			logger.error("[Murphy][BlackLineDao][getUsersByAbility][query]" + query);
			Query q = this.getSession().createSQLQuery(query);
			List<String> empEmailList = q.list();
			if (!ServicesUtil.isEmpty(empEmailList)) {
				for (String email : empEmailList) {
					if (email != null)
						emailList.add(email);
				}
			}

			for (NearestUserDto nearestUserDto : dto.getNearestUserDto()) {
				if (!ServicesUtil.isEmpty(nearestUserDto.getEmailId())) {
					for (String email : emailList) {

						if (nearestUserDto.getEmailId().equalsIgnoreCase(email)) {
							nearestUsersList.add(nearestUserDto);
						}
					}
				}
			}

			NearestUserDtoResponse.setNearestUsers(nearestUsersList);
			logger.error("[Murphy][BlackLineDao][getUsersByAbility][NearestUserDtoResponse]" + NearestUserDtoResponse);

			if (!ServicesUtil.isEmpty(nearestUsersList)) {
				responseMessage.setMessage("Nearest users After filtering By Designation ");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
				NearestUserDtoResponse.setResponseMessage(responseMessage);
			}

			return NearestUserDtoResponse;
		} catch (Exception e) {
			logger.error("[Murphy][BlackLineDao][getUsersByAbility][Exception]" + e.getMessage());
		}
		return null;
	}
	
	public Map<String, List<String>> getEmpByShift() {
		Map<String, List<String>> empDetails = null;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			return stopTimeDao.getEmpByShift(calendar);
			
		}catch(Exception e){
			logger.error("[StopTimeDao][getEmpByShift][Exception] "+e.getMessage());
		}
		return empDetails;
	}


}
