package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.blackline.Coordinates;
import com.murphy.taskmgmt.dto.EmpDetailsDto;
import com.murphy.taskmgmt.dto.EmpDetailsResponseDto;
import com.murphy.taskmgmt.dto.EmpInfoDto;
import com.murphy.taskmgmt.entity.EmpDetailsDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

import weka.core.DenseInstance;
import weka.core.Instance;

@Repository("EmpDetailsDao")
public class EmpDetailsDao extends BaseDao<EmpDetailsDo, EmpDetailsDto> {

	private static final Logger logger = LoggerFactory.getLogger(EmpDetailsDao.class);
	
	@Override
	protected EmpDetailsDo importDto(EmpDetailsDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		EmpDetailsDo entity = new EmpDetailsDo();

		if (!ServicesUtil.isEmpty(fromDto.getEmpId()))
			entity.setEmpId(fromDto.getEmpId());
		if (!ServicesUtil.isEmpty(fromDto.getEmpDesignation()))
			entity.setDesignation(fromDto.getEmpDesignation());
		if (!ServicesUtil.isEmpty(fromDto.getEmpEmail()))
			entity.setEmpEmail(fromDto.getEmpEmail());
		if (!ServicesUtil.isEmpty(fromDto.getEmpName()))
			entity.setEmpName(fromDto.getEmpName());
		if (!ServicesUtil.isEmpty(fromDto.getEndDate()))
			entity.setEndDate(fromDto.getEndDate());
		if (!ServicesUtil.isEmpty(fromDto.getForemanDesignation()))
			entity.setForemanDesignation(fromDto.getForemanDesignation());
		if (!ServicesUtil.isEmpty(fromDto.getForemanEmail()))
			entity.setForemanEmail(fromDto.getForemanEmail());
		if (!ServicesUtil.isEmpty(fromDto.getForemanId()))
			entity.setForemanId(fromDto.getForemanId());
		if (!ServicesUtil.isEmpty(fromDto.getForemanName()))
			entity.setForemanName(fromDto.getForemanName());
		if (!ServicesUtil.isEmpty(fromDto.getStartDate()))
			entity.setStartDate(fromDto.getStartDate());
		if (!ServicesUtil.isEmpty(fromDto.getSuperintendentDesignation()))
			entity.setSuperintendentDesignation(fromDto.getSuperintendentDesignation());
		if (!ServicesUtil.isEmpty(fromDto.getSuperintendentEmail()))
			entity.setSuperintendentEmail(fromDto.getSuperintendentEmail());
		if (!ServicesUtil.isEmpty(fromDto.getSuperintendentId()))
			entity.setSuperintendentId(fromDto.getSuperintendentId());
		if (!ServicesUtil.isEmpty(fromDto.getSuperintendentName()))
			entity.setSuperintendentName(fromDto.getSuperintendentName());
		if (!ServicesUtil.isEmpty(fromDto.getFieldTaskUser()))
			entity.setFieldTaskUser(fromDto.getFieldTaskUser());
		if (!ServicesUtil.isEmpty(fromDto.getFieldTaskUser()))
			entity.setFieldTaskUser(fromDto.getFieldTaskUser());
		if (!ServicesUtil.isEmpty(fromDto.getEmplStatus()))
			entity.setEmplStatus(fromDto.getEmplStatus());

		return entity;
	}

	@Override
	protected EmpDetailsDto exportDto(EmpDetailsDo entity) {

		EmpDetailsDto dto = new EmpDetailsDto();

		if (!ServicesUtil.isEmpty(entity.getEmpId()))
     		dto.setEmpId(entity.getEmpId());
		if (!ServicesUtil.isEmpty(entity.getDesignation()))
     		dto.setEmpDesignation(entity.getDesignation());
		if (!ServicesUtil.isEmpty(entity.getEmpEmail()))
     		dto.setEmpEmail(entity.getEmpEmail());
		if (!ServicesUtil.isEmpty(entity.getEmpName()))
     		dto.setEmpName(entity.getEmpName());
		if (!ServicesUtil.isEmpty(entity.getEndDate()))
     		dto.setEndDate(entity.getEndDate());
		if (!ServicesUtil.isEmpty(entity.getForemanDesignation()))
     		dto.setForemanDesignation(entity.getForemanDesignation());
		if (!ServicesUtil.isEmpty(entity.getForemanEmail()))
     		dto.setForemanEmail(entity.getForemanEmail());
		if (!ServicesUtil.isEmpty(entity.getForemanId()))
     		dto.setForemanId(entity.getForemanId());
		if (!ServicesUtil.isEmpty(entity.getForemanName()))
     		dto.setForemanName(entity.getForemanName());
		if (!ServicesUtil.isEmpty(entity.getStartDate()))
     		dto.setStartDate(entity.getStartDate());
		if (!ServicesUtil.isEmpty(entity.getSuperintendentDesignation()))
     		dto.setSuperintendentDesignation(entity.getSuperintendentDesignation());
		if (!ServicesUtil.isEmpty(entity.getSuperintendentEmail()))
     		dto.setSuperintendentEmail(entity.getSuperintendentEmail());
		if (!ServicesUtil.isEmpty(entity.getSuperintendentId()))
     		dto.setSuperintendentId(entity.getSuperintendentId());
		if (!ServicesUtil.isEmpty(entity.getSuperintendentName()))
     		dto.setSuperintendentName(entity.getSuperintendentName());
		if (!ServicesUtil.isEmpty(entity.getFieldTaskUser()))
     		dto.setFieldTaskUser(entity.getFieldTaskUser());
		if (!ServicesUtil.isEmpty(entity.getEmplStatus()))
			dto.setEmplStatus(entity.getEmplStatus());

		return dto;
	}


//	@SuppressWarnings("unchecked")
//	public List<EmpDetailsResponseDto> getUserLoginName() {
//		List<EmpDetailsResponseDto> empDetailsResponseDtoList = new ArrayList<EmpDetailsResponseDto>();
//		EmpDetailsResponseDto empDetailsResponseDto = null;
//		try {
//			String qryString = "SELECT USER_EMAIL,USER_LOGIN_NAME FROM TM_USER_IDP_MAPPING";
//			Query query = this.getSession().createSQLQuery(qryString);
//			List<Object[]> resultList = query.list();
//			if (!ServicesUtil.isEmpty(resultList)) {
//				for (Object[] obj : resultList) {
//					empDetailsResponseDto = new EmpDetailsResponseDto();
//					if (!ServicesUtil.isEmpty(obj[0]))
//						empDetailsResponseDto.setUserEmail((String) obj[0]);
//					if (!ServicesUtil.isEmpty(obj[1]))
//						empDetailsResponseDto.setUserLoginName((String) obj[1]);
//
//					empDetailsResponseDtoList.add(empDetailsResponseDto);
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error("[Murphy][EmpDetailsFacade][getUserLoginName][Exception]" + e.getMessage());
//		}
//		return empDetailsResponseDtoList;
//	}

	@SuppressWarnings("unchecked")
	public List<Integer> getEmailList() {
		List<Integer> emailList = new ArrayList<Integer>();
		try {
			String qryString = "SELECT DISTINCT USER_LOGIN_NAME FROM TM_USER_IDP_MAPPING";
			Query query = this.getSession().createSQLQuery(qryString);
			List<String> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (String loginNmae : resultList) {
					if (!ServicesUtil.isEmpty(loginNmae))
						if(isNumber(loginNmae)){
							emailList.add(Integer.parseInt(loginNmae));
						}
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][getEmailList][Exception]" + e.getMessage());
		}

		return emailList;
		
	}
	
	public String updateEmpDetails(EmpDetailsDto dto){
		String msg = null;
		try {
//			String qryString = "UPDATE EMP_INFO SET EMP_EMAIL='"+dto.getEmpEmail()+"', FIRST_NAME='"+dto.getEmpFirstname()+"', LAST_NAME='"+dto.getEmpLastName()+"', FIELD TASK USER='"+dto.getFieldTaskUser()+"',"
//					+ "DESIGNATION = '"+dto.getEmpDesignation()+"',START_DATE='"+dto.getStartDate()+"',FOREMAN_ID='"+dto.getForemanId()+"',FOREMAN_NAME='"+dto.getForemanName()+"',FOREMAN_DESIGNATION='"+dto.getForemanDesignation()+"',"
//							+ "FOREMAN_EMAIL='"+dto.getForemanEmail()+"',SUPERINTENDENT_ID='"+dto.getSuperintendentId()+"',SUPERINTENDENT_EMAIL='"+dto.getSuperintendentEmail()+"',SUPERINTENDENT_NAME='"+dto.getSuperintendentName()+"',"
//									+ "SUPERINTENDENT_DESIGNATION='"+dto.getSuperintendentDesignation()+"' WHERE EMP_ID='"+dto.getEmpId()+"'";
//			logger.error("query ="+qryString);
//			int result = this.getSession().createSQLQuery(qryString).executeUpdate();
//			if(result>0){
//				msg = MurphyConstant.SUCCESS;
	//		}

		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][getEmailList][Exception]" + e.getMessage());
		}
		return msg;
	}

	public String saveOrUpadte(List<EmpDetailsDto> dto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		Set<EmpDetailsDo> empSet = new HashSet<EmpDetailsDo>();
		logger.error("size of list =" + dto.size());
		for (EmpDetailsDto empDetailsDto : dto) {
			EmpDetailsDo empDo = importDto(empDetailsDto);
			empSet.add(empDo);
		}
		logger.error("emp set size =" + empSet.size());
		try {

			for (EmpDetailsDo empDetailsDo : empSet) {
				logger.error("empdto size =" + dto.size());
				if (empDetailsDo.getEmpId() != null && !empDetailsDo.getEmpId().isEmpty()) {
					this.getSession().saveOrUpdate(empDetailsDo);
				}

			}
		} catch (Exception e) {
			logger.error("[Murphy][EmpDetailsFacade][saveOrUpadte][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}
		return MurphyConstant.SUCCESS;
		
	}
	
	static boolean isNumber(String s) 
    { 
        for (int i = 0; i < s.length(); i++) 
        if (Character.isDigit(s.charAt(i))  
            == false) 
            return false; 
  
        return true; 
    }
	
	@SuppressWarnings("unchecked")
	public List<EmpInfoDto> getOperatorsBaseLocation(List<String> usersList) throws NoResultFault {

		// STPE1 : GET ALL EMP LOCATIONS
		StringBuilder sql = new StringBuilder("SELECT EMP_EMAIL,LOCATION FROM EMP_INFO WHERE EMP_EMAIL IN("
				+ ServicesUtil.getStringFromList(usersList) + ")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		Map<String, String> locationTxtToCodeMap = new HashMap<String, String>();
		Map<String, Coordinates> locationCodeToCoordinateMap = new HashMap<>();
		List<EmpInfoDto> empInfoList = new ArrayList<>();
		List<Object[]> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			for (Object[] objects : resultList) {
				EmpInfoDto empInfoDto = new EmpInfoDto();
				empInfoDto.setEmpEmail(ServicesUtil.isEmpty(objects[0]) ? null : objects[0].toString());
				empInfoDto.setFacility(ServicesUtil.isEmpty(objects[1]) ? null : objects[1].toString());
				locationTxtToCodeMap.put(empInfoDto.getFacility(), null);
				empInfoList.add(empInfoDto);
			}
		} else {

			throw new NoResultFault("No Records found for the query " + sql.toString());
		}

//		// STPE2 :GET ALL LOCATION CODES BY LOCATION TEXT
//		StringBuilder productionLocSql = new StringBuilder(
//				"SELECT LOCATION_CODE,LOCATION_TEXT FROM PRODUCTION_LOCATION WHERE LOCATION_CODE IN("
//						+ ServicesUtil.getStringFromList(new ArrayList<String>(locationTxtToCodeMap.keySet())) + ")");
//		SQLQuery productionLocQuery = getSession().createSQLQuery(productionLocSql.toString());
//		List<Object[]> prodLocRes = productionLocQuery.list();
//		if (!ServicesUtil.isEmpty(prodLocRes)) {
//			for (Object[] objects : prodLocRes) {
//				locationTxtToCodeMap.put(objects[1].toString(), objects[0].toString());
//			}
//		} else {
//
//			throw new NoResultFault("No Records found for the query " + productionLocSql.toString());
//		}

		// STPE3 : GET ALL LOCATION COORDINATES BY LOCATION CODES
		logger.error("EmpDetailsDao.getOperatorsBaseLocation() STPE3 : GET ALL LOCATION COORDINATES BY LOCATION CODES");
		StringBuilder locCoordinatesSql = new StringBuilder(
				"SELECT LOCATION_CODE,LONGITUDE,LATITUDE FROM LOCATION_COORDINATE WHERE LOCATION_CODE IN ("
						+ ServicesUtil.getStringFromList(new ArrayList<String>(locationTxtToCodeMap.keySet())) + ")");
		SQLQuery locCoordinatesQuery = getSession().createSQLQuery(locCoordinatesSql.toString());
		List<Object[]> locCoordinates = locCoordinatesQuery.list();
		if (!ServicesUtil.isEmpty(locCoordinates)) {
			for (Object[] objects : locCoordinates) {

				Coordinates coordinates = new Coordinates(new BigDecimal(objects[2].toString()), new BigDecimal(objects[1].toString()));

				locationCodeToCoordinateMap.put(objects[0].toString(), coordinates);

			}
		} else {

			throw new NoResultFault("No Records found for the query " + locCoordinatesSql.toString());
		}

		// STEP 4:Prepare EmpInfo records
		logger.error("EmpDetailsDao.getOperatorsBaseLocation() STPE3 : STEP 4:Prepare EmpInfo records ");
		logger.error("EmpDetailsDao.getOperatorsBaseLocation() STPE3 : STEP 4:Prepare EmpInfo records "+empInfoList.size());
		for (EmpInfoDto empInfoDto : empInfoList) {
			empInfoDto.setLocationCode(empInfoDto.getFacility());
			Coordinates coordinates = locationCodeToCoordinateMap.get(empInfoDto.getLocationCode());

			empInfoDto.setLongitude(new BigDecimal(coordinates.getLongitude()));
			empInfoDto.setLatitude(new BigDecimal(coordinates.getLatitude()));
			
			logger.error(empInfoDto.toString());

		}

		return empInfoList;

	}
	
	
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getUsersBaseLocation(List<String> usersList) throws NoResultFault {
		StringBuilder sql = new StringBuilder("SELECT EMP_EMAIL,LOCATION FROM EMP_INFO WHERE EMP_EMAIL IN("
				+ ServicesUtil.getStringFromList(usersList) + ") AND LOCATION IS NOT NULL");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		List<Object[]> resultList = query.list();
		Map<String, String> userstoBaseLocationMap = null;
		if (!ServicesUtil.isEmpty(resultList)) {

			userstoBaseLocationMap = new HashMap<>();
			for (Object[] objects : resultList) {

				String userEmail = ServicesUtil.isEmpty(objects[0]) ? null : objects[0].toString();
				String userBaseLocation = ServicesUtil.isEmpty(objects[1]) ? null : objects[1].toString();

				if (!ServicesUtil.isEmpty(userEmail) && !ServicesUtil.isEmpty(userBaseLocation)) {
					userstoBaseLocationMap.put(userEmail, userBaseLocation);
				}
			}
		}

		if (ServicesUtil.isEmpty(userstoBaseLocationMap) || userstoBaseLocationMap.size() == 0) {
			throw new NoResultFault("No Records found for the query " + sql.toString());
		}

		return userstoBaseLocationMap;

	}
	
	public Map<String, Coordinates> getUsersBaseLocationCoordinates(List<String> usersList) {
		Map<String, Coordinates> usersToCoordinateMap=null;
		try {
			// STEP1 : get users to Base location map
			Map<String, String> usersToBaseLocationMap = getUsersBaseLocation(usersList);

			// STEP2: get Location to Coordinate Map
			Map<String, Coordinates> locationCodetoCoordinateMap = getAllCoordinates(
					new ArrayList<>(usersToBaseLocationMap.values()));

			// STEP3:Form Users to Coordinate map
			usersToCoordinateMap = new HashMap<>();

			for (String user : usersToBaseLocationMap.keySet()) {

				Coordinates coordinates = locationCodetoCoordinateMap.get(usersToBaseLocationMap.get(user));

				if (!ServicesUtil.isEmpty(coordinates)) {
					usersToCoordinateMap.put(user, coordinates);
				}
			}

		} catch (NoResultFault e) {
			logger.info("GeoTabUtil.getUsersBaseLocationCoOrdinates() " + e.getMessage());
		}

		return usersToCoordinateMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Coordinates> getAllCoordinates(List<String> locationCodes) throws NoResultFault {

		StringBuilder sql = new StringBuilder(
				"SELECT LOCATION_CODE,LONGITUDE,LATITUDE FROM LOCATION_COORDINATE WHERE LOCATION_CODE IN ("
						+ ServicesUtil.getStringFromList(locationCodes)
						+ ") AND LONGITUDE IS NOT NULL AND LATITUDE IS NOT NULL");

		SQLQuery query =  this.getSession().createSQLQuery(sql.toString());
		List<Object[]> locCoordinates = query.list();
		Map<String, Coordinates> locationToCoordinateMap = null;

		if (!ServicesUtil.isEmpty(locCoordinates)) {
			locationToCoordinateMap = new HashMap<>();
			for (Object[] objects : locCoordinates) {

				String locationCode = ServicesUtil.isEmpty(objects[0]) ? null : objects[0].toString();
				BigDecimal longitude = ServicesUtil.getBigDecimal(objects[1]);
				BigDecimal latitude = ServicesUtil.getBigDecimal(objects[2]);

				if (!ServicesUtil.isEmpty(locationCode) && !ServicesUtil.isEmpty(longitude)
						&& !ServicesUtil.isEmpty(latitude)) {
					locationToCoordinateMap.put(locationCode, new Coordinates(latitude, longitude));
				}

			}
		} 

		
		if (ServicesUtil.isEmpty(locationToCoordinateMap) || locationToCoordinateMap.size() == 0) {
			throw new NoResultFault("No Records found for the query " + sql.toString());
		}
		

		return locationToCoordinateMap;
	}
	

}
