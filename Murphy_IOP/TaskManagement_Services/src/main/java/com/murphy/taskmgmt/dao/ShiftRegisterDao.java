package com.murphy.taskmgmt.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.Query;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EmpShiftDetailsDto;
import com.murphy.taskmgmt.dto.ShiftAuditLogDto;
import com.murphy.taskmgmt.dto.ShiftRegisterDto;
import com.murphy.taskmgmt.entity.ShiftRegisterDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.service.GeoTabFacade;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("ShiftRegisterDao")
@Transactional
public class ShiftRegisterDao extends BaseDao<ShiftRegisterDo, ShiftRegisterDto> {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ShiftRegisterDao.class);

	@Autowired
	ConfigDao configDao;

	@Autowired
	HierarchyDao hierarchyDao;

	@Autowired
	RouteLocationDao routeLocationDao;

	@Autowired
	ShiftAuditLogDao shiftAuditLogDao;

	@Override
	protected ShiftRegisterDo importDto(ShiftRegisterDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ShiftRegisterDo entity = new ShiftRegisterDo();
		entity.setEmpId(fromDto.getEmpId());
		entity.setEmpName(fromDto.getEmpName());
		entity.setEmpEmail(fromDto.getEmpEmail());
		entity.setFri(fromDto.getFri());
		entity.setId(fromDto.getId());
		entity.setMon(fromDto.getMon());
		entity.setSat(fromDto.getSat());
		entity.setSun(fromDto.getSun());
		entity.setThur(fromDto.getThur());
		entity.setTue(fromDto.getTue());
		entity.setWed(fromDto.getWed());
		return entity;

	}

	@Override
	protected ShiftRegisterDto exportDto(ShiftRegisterDo entity) {
		ShiftRegisterDto toDto = new ShiftRegisterDto();
		toDto.setEmpId(entity.getEmpId());
		toDto.setEmpEmail(entity.getEmpEmail());
		toDto.setEmpName(entity.getEmpName());
		toDto.setFri(entity.getFri());
		toDto.setId(entity.getId());
		toDto.setMon(entity.getMon());
		toDto.setSat(entity.getSat());
		toDto.setSun(entity.getSun());
		toDto.setThur(entity.getThur());
		toDto.setTue(entity.getTue());
		toDto.setWed(entity.getWed());
		return toDto;
	}

	// Returns a list of EMP_EMAIL who are available on current shift (5am to
	// 5pm) OR (5pm to 5am)
	@SuppressWarnings("unchecked")
	public List<String> getEmpByShift() {
		List<String> employeeEmailList = null;

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			// calendar.getTime();

			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd");
			// LocalDate localDate = formatter.parseLocalDate(date);
			String dayOfWeek = null;
			// LocalDate.now().getDayOfWeek().name();
			int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayNum == 2)
				dayOfWeek = "Monday";
			if (dayNum == 3)
				dayOfWeek = "Tuesday";
			if (dayNum == 4)
				dayOfWeek = "Wednesday";
			if (dayNum == 5)
				dayOfWeek = "Thursday";
			if (dayNum == 6)
				dayOfWeek = "Friday";
			if (dayNum == 7)
				dayOfWeek = "Saturday";
			if (dayNum == 1)
				dayOfWeek = "Sunday";
			logger.error("CST Day =" + dayOfWeek);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.MINUTE);
			String shift = checkShift(hour, min);
			logger.error("CST Time = " + hour + ":" + min);
			String queryString = "select EMP_EMAIL,EMP_NAME from EMP_INFO where EMP_STATUS='Active' and (" + dayOfWeek
					+ " = '3' OR " + dayOfWeek + " = '" + shift + "')";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				employeeEmailList = new ArrayList<String>();
				for (Object[] objects : resultList) {
					employeeEmailList.add(ServicesUtil.isEmpty(objects[0]) ? null : (String) objects[0]);
				}
			}
			return employeeEmailList;
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][getEmpByShift][Exception]" + e.getMessage());
		}
		return null;
	}

	// check for shift
	private String checkShift(int hour, int min) {
		if (5 <= hour && hour < 17) {
			return "1";
		} else {
			return "2";
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpShiftDetailsDto> empShiftDetails(String date, String Email) {
		List<EmpShiftDetailsDto> EmpShiftDetailsDto = new ArrayList<EmpShiftDetailsDto>();
		EmpShiftDetailsDto empShiftDetailsDto = null;
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
			Date dt1 = format1.parse(date);
			DateFormat format2 = new SimpleDateFormat("EEEE");

			String dayOfWeek = format2.format(dt1);
			String queryString = "SELECT  EI.EMP_NAME,EI." + dayOfWeek
					+ ",EI.DESIGNATION,EI.EMP_EMAIL,IDP.USER_ROLE,EI.LOCATION,EI.EMP_ID FROM EMP_INFO EI JOIN TM_USER_IDP_MAPPING IDP "
					+ "ON EI.EMP_ID = IDP.USER_LOGIN_NAME WHERE EI.FOREMAN_ID =(SELECT EMP_ID FROM EMP_INFO WHERE EMP_EMAIL= '"+ Email+ "') and"
//					+ " UPPER(IDP.USER_ROLE) NOT LIKE ('%KAYBOB%') AND UPPER(IDP.USER_ROLE) NOT LIKE('%MONTNEY%')  and"
					+ " EI.FOREMAN_ID<>EI.EMP_ID AND EMP_STATUS='Active'" + "order by EI.EMP_NAME ";
			logger.error("query = " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] objects : resultList) {
					empShiftDetailsDto = new EmpShiftDetailsDto();
					empShiftDetailsDto.setEmpName(ServicesUtil.isEmpty(objects[0]) ? null : (String) objects[0]);
					empShiftDetailsDto.setDesignation(ServicesUtil.isEmpty(objects[2]) ? null : (String) objects[2]);
					empShiftDetailsDto.setEmpEmail(ServicesUtil.isEmpty(objects[3]) ? null : (String) objects[3]);
					empShiftDetailsDto.setUserRole(ServicesUtil.isEmpty(objects[4]) ? null : (String) objects[4]);
					empShiftDetailsDto.setLocation(ServicesUtil.isEmpty(objects[5]) ? null : (String) objects[5]);
					empShiftDetailsDto.setEmpId(ServicesUtil.isEmpty(objects[6]) ? null : (String) objects[6]);
					empShiftDetailsDto.setDate(date);
					int shiftCheck = ServicesUtil.isEmpty(objects[1]) ? 0 : (int) objects[1];

					if (shiftCheck == 1)
						empShiftDetailsDto.setIsMorning("true");

					if (shiftCheck == 2)
						empShiftDetailsDto.setIsEvening("true");

					if (shiftCheck == 3) {
						empShiftDetailsDto.setIsMorning("true");
						empShiftDetailsDto.setIsEvening("true");
					}

					// //Fetch all Facilities by UserRole
					// Map<String,String>
					// facilityNameList=getRouteDropDownByUserRole(empShiftDetailsDto.getUserRole());
					//
					//
					// //Get Route Location Mapping Details by EmpId
					// empShiftDetailsDto.setRouteLocationDtoList(
					// routeLocationDao.fetchMappedLocByEmpId(empShiftDetailsDto.getEmpId(),facilityNameList));

					// Assign Default Location
					if (ServicesUtil.isEmpty(empShiftDetailsDto.getLocation())) {
						if (!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().contains(MurphyConstant.OBX_CATARINA)
										|| (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_CATARINA)))) {
							empShiftDetailsDto.setLocation("Kone West");
						} else if (!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().contains(MurphyConstant.OBX_KARNES)
										|| (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_KARNES)))) {
							empShiftDetailsDto.setLocation("Drees South");

						} else if ((!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().contains(MurphyConstant.OBX_TILDEN)
										|| (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_TILDEN))))) {
							empShiftDetailsDto.setLocation("Jambers West");
						} else if ((!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_KAYBOB)))) {
							empShiftDetailsDto.setLocation("Kaybob East");
						} else if ((!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_MONTNEY)))) {
							empShiftDetailsDto.setLocation("Tupper Main");
						}

					} else if (!ServicesUtil.isEmpty(empShiftDetailsDto.getLocation())) {

						empShiftDetailsDto
								.setLocation(hierarchyDao.getLocationByLocCode(empShiftDetailsDto.getLocation()));
					}

					// Fetch all Facilities by UserRole
					List<Map<String, String>> facilityNameList = getRouteDropDownByUserRole(
							empShiftDetailsDto.getUserRole());
					if (!ServicesUtil.isEmpty(facilityNameList))
						empShiftDetailsDto.setFacilityList(facilityNameList);

					EmpShiftDetailsDto.add(empShiftDetailsDto);
				}
				logger.error("EmpShiftDetailsDto =" + EmpShiftDetailsDto);
			}

			return EmpShiftDetailsDto;
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][empShiftDetails][Exception]" + e.getMessage());
		}
		return null;
	}

	public String updateEmpShift(EmpShiftDetailsDto dto) {
		String shift = null;
		try {
			if (!ServicesUtil.isEmpty(dto.getIsMorning()) && dto.getIsMorning().equalsIgnoreCase(MurphyConstant.TRUE)
					&& ServicesUtil.isEmpty(dto.getIsEvening()))
				shift = "1";
			if (!ServicesUtil.isEmpty(dto.getIsEvening()) && dto.getIsEvening().equalsIgnoreCase(MurphyConstant.TRUE)
					&& ServicesUtil.isEmpty(dto.getIsMorning()))
				shift = "2";
			if (!ServicesUtil.isEmpty(dto.getIsMorning()) && !ServicesUtil.isEmpty(dto.getIsEvening())
					&& (dto.getIsMorning().equalsIgnoreCase(MurphyConstant.TRUE)
							&& dto.getIsEvening().equalsIgnoreCase(MurphyConstant.TRUE)))
				shift = "3";
			if (ServicesUtil.isEmpty(dto.getIsMorning()) && ServicesUtil.isEmpty(dto.getIsEvening()))
				shift = "0";

			SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
			Date dt1 = format1.parse(dto.getDate());
			DateFormat format2 = new SimpleDateFormat("EEEE");
			String dayOfWeek = format2.format(dt1);
			String loc=ServicesUtil.isEmpty(dto.getLocation()) ? "" :dto.getLocation();
			String queryString = "UPDATE EMP_INFO SET " + dayOfWeek + " = '" + shift + "',LOCATION='"
					+ loc + "' where EMP_EMAIL ='" + dto.getEmpEmail() + "'";
			logger.error("query = " + queryString);
			int result = this.getSession().createSQLQuery(queryString).executeUpdate();
			if (result > 0) {
				return MurphyConstant.SUCCESS;
			}


		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][updateEmpShift][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}
		return null;
	}

	@SuppressWarnings("unused")
	public String shiftRegisterScheduler() {
		try {
			Map<String, ShiftAuditLogDto> empDetails = new HashMap<String, ShiftAuditLogDto>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			String dayOfWeek = null;
			String nextDay = null;
			int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayNum == 2) {
				dayOfWeek = "Monday";
				nextDay = "Tuesday";
			}
			if (dayNum == 3) {
				dayOfWeek = "Tuesday";
				nextDay = "Wednesday";
			}
			if (dayNum == 4) {
				dayOfWeek = "Wednesday";
				nextDay = "Thursday";
			}
			if (dayNum == 5) {
				dayOfWeek = "Thursday";
				nextDay = "Friday";
			}
			if (dayNum == 6) {
				dayOfWeek = "Friday";
				nextDay = "Saturday";
			}
			if (dayNum == 7) {
				dayOfWeek = "Saturday";
				nextDay = "Sunday";
			}
			if (dayNum == 1) {
				dayOfWeek = "Sunday";
				nextDay = "Monday";
			}
			logger.error("today day =" + dayOfWeek);
			logger.error("tomarrows day =" + nextDay);
			empDetails = getShiftDetailsForScheduler(dayOfWeek);
			int result = 0;
			for (String empId : empDetails.keySet()) {
				String queryString = "UPDATE EMP_INFO SET " + nextDay + " = '"
						+ empDetails.get(empId).getCurrentShift() + "' where EMP_ID ='" + empId + "'";
				result = this.getSession().createSQLQuery(queryString).executeUpdate();
				if (result > 0) {
					// Create Audit Log for the system Update
					shiftAuditLogDao.createInstance(empDetails.get(empId),false);
				}
			}
			logger.error("result number =" + result);
			if (result > 0) {
				return MurphyConstant.SUCCESS;
			}

		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][updateEmpShift][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}
		return MurphyConstant.SUCCESS;
	}

	private Map<String, ShiftAuditLogDto> getShiftDetailsForScheduler(String dayOfWeek) {
		Map<String, ShiftAuditLogDto> empDetails = new HashMap<String, ShiftAuditLogDto>();
		ShiftAuditLogDto auditLogDto = null;
		// String dayOfWeek = "";
		try {
			// if (dayNum == 2)
			// dayOfWeek = "Sunday";
			// if (dayNum == 3)
			// dayOfWeek = "Monday";
			// if (dayNum == 4)
			// dayOfWeek = "Tuesday";
			// if (dayNum == 5)
			// dayOfWeek = "Wednesday";
			// if (dayNum == 6)
			// dayOfWeek = "Thursday";
			// if (dayNum == 7)
			// dayOfWeek = "Friday";
			// if (dayNum == 8)
			// dayOfWeek = "Saturday";
			// logger.error("yesterday day =" + dayOfWeek);
			// String stringQuery = "SELECT EMP_ID," + dayOfWeek +
			// ",EMP_NAME,LOCATION FROM EMP_INFO";
			String stringQuery = "SELECT EI.EMP_ID,EI." + dayOfWeek
					+ ",EI.EMP_NAME,EI.LOCATION,IDP.USER_ROLE FROM EMP_INFO EI "
					+ "JOIN TM_USER_IDP_MAPPING IDP ON EI.EMP_ID=IDP.USER_LOGIN_NAME WHERE EI.EMP_STATUS='Active'";
//					+ "AND UPPER(IDP.USER_ROLE) NOT LIKE '%KAYBOB%' AND UPPER(IDP.USER_ROLE) NOT LIKE'%MONTNEY%'";

			Query query = this.getSession().createSQLQuery(stringQuery);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					auditLogDto = new ShiftAuditLogDto();
					String location = ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3];
					int shiftInfo = ServicesUtil.isEmpty(obj[1]) ? 0 : (int) obj[1];
					auditLogDto.setPreviousShift(shiftInfo+"");
					auditLogDto.setCurrentShift(auditLogDto.getPreviousShift());
					auditLogDto.setModifiedBy(MurphyConstant.SYSTEM);
					auditLogDto.setResource(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					auditLogDto.setShiftDay(MurphyConstant.TOMORROW);
					if (!ServicesUtil.isEmpty(location)) {
						auditLogDto.setPrevBaseLoc(hierarchyDao.getLocationByLocCode(location));
						auditLogDto.setCurrentBaseLoc(auditLogDto.getPrevBaseLoc());
					} else {
						String userRole = ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4];

						if (!ServicesUtil.isEmpty(userRole)
								&& userRole.toUpperCase().contains(MurphyConstant.OBX_CATARINA)
								|| userRole.contains(MurphyConstant.PRO_CATARINA)) {
							auditLogDto.setPrevBaseLoc("Kone West");
							auditLogDto.setCurrentBaseLoc("Kone West");
						} else if (!ServicesUtil.isEmpty(userRole)
								&& userRole.toUpperCase().contains(MurphyConstant.OBX_KARNES)
								|| userRole.toUpperCase().contains(MurphyConstant.PRO_KARNES)) {
							auditLogDto.setPrevBaseLoc("Drees South");
							auditLogDto.setCurrentBaseLoc("Drees South");

						} else if ((!ServicesUtil.isEmpty(userRole)
								&& userRole.toUpperCase().contains(MurphyConstant.OBX_TILDEN)
								|| userRole.contains(MurphyConstant.PRO_TILDEN))) {
							auditLogDto.setPrevBaseLoc("Jambers West");
							auditLogDto.setCurrentBaseLoc("Jambers West");
						} else if ((!ServicesUtil.isEmpty(userRole)
								&& userRole.toUpperCase().contains(MurphyConstant.PRO_KAYBOB.toUpperCase()))) {
							auditLogDto.setPrevBaseLoc("Kaybob East");
							auditLogDto.setCurrentBaseLoc("Kaybob East");
						} else if ((!ServicesUtil.isEmpty(userRole)
								&& userRole.toUpperCase().contains(MurphyConstant.PRO_MONTNEY.toUpperCase()))) {
							auditLogDto.setPrevBaseLoc("Tupper Main");
							auditLogDto.setCurrentBaseLoc("Tupper Main");
						}

					}

					empDetails.put((String) obj[0], auditLogDto);
				}
			}
			logger.error("empMap size for yesterday shift =" + empDetails.size());
			return empDetails;
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][getShiftDetailsForScheduler][Exception]" + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<EmpShiftDetailsDto> empShiftDetailsUnderROC(String date, String Email, List<String> userRole) {
		List<EmpShiftDetailsDto> EmpShiftDetailsDto = new ArrayList<EmpShiftDetailsDto>();
		EmpShiftDetailsDto empShiftDetailsDto = null;
		boolean isEfsUser = false;
		boolean isCanadaUser = false;
		String queryString = null;
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
			Date dt1 = format1.parse(date);
			DateFormat format2 = new SimpleDateFormat("EEEE");
			String dayOfWeek = format2.format(dt1);

			if (!ServicesUtil.isEmpty(userRole)) {
				if (userRole.contains(MurphyConstant.KAYBOB_USER) || userRole.contains(MurphyConstant.MONTNEY_USER)) {
					isCanadaUser = true;
				}
			}
			if (!ServicesUtil.isEmpty(userRole)) {
				if (userRole.contains(MurphyConstant.ROLE_CATARINA) || userRole.contains(MurphyConstant.ROLE_KARNES)
						|| userRole.contains(MurphyConstant.ROLE_WTILDEN)
						|| userRole.contains(MurphyConstant.ROLE_CTILDEN)) {
					isEfsUser = true;
				}
			}

			if (isEfsUser && isCanadaUser) {
				// queryString = "SELECT EI.EMP_NAME,EI." + dayOfWeek
				// +
				// ",EI.DESIGNATION,EI.EMP_EMAIL,IDP.USER_ROLE,EI.LOCATION,EI.EMP_ID
				// FROM EMP_INFO AS EI INNER JOIN TM_USER_IDP_MAPPING AS IDP ON
				// EI.EMP_ID = IDP.USER_LOGIN_NAME "
				// + "WHERE IDP.USER_ROLE LIKE '%OBX_CATARINA%' OR IDP.USER_ROLE
				// LIKE '%OBX_KARNES%' OR IDP.USER_ROLE LIKE '%OBX_TILDEN%' OR "
				// + "IDP.USER_ROLE LIKE '%PRO_CATARINA%' OR IDP.USER_ROLE LIKE
				// '%PRO_TILDEN%' OR IDP.USER_ROLE LIKE '%PRO_KARNES%' OR "
				// + "IDP.USER_ROLE LIKE '%PRO_KAYBOB%' OR IDP.USER_ROLE LIKE
				// '%PRO_MONTNEY%' AND EMP_STATUS = 'Active' ORDER BY
				// EI.EMP_NAME ASC";
				queryString = "SELECT EI.EMP_NAME,EI." + dayOfWeek
						+ ",EI.DESIGNATION,EI.EMP_EMAIL,IDP.USER_ROLE,EI.LOCATION,EI.EMP_ID FROM EMP_INFO AS EI INNER JOIN TM_USER_IDP_MAPPING AS IDP ON EI.EMP_ID = IDP.USER_LOGIN_NAME "
						+ "WHERE (UPPER(IDP.USER_ROLE) LIKE '%OBX_CATARINA%' OR UPPER(IDP.USER_ROLE) LIKE '%OBX_KARNES%' OR UPPER(IDP.USER_ROLE) LIKE '%OBX_TILDEN%' OR "
						+ "UPPER(IDP.USER_ROLE) LIKE '%PRO_CATARINA%' OR UPPER(IDP.USER_ROLE) LIKE '%PRO_TILDEN%' OR UPPER(IDP.USER_ROLE) LIKE '%PRO_KARNES%' "
						+ "OR UPPER(IDP.USER_ROLE) LIKE '%ALS_EAST%' OR UPPER(IDP.USER_ROLE) LIKE '%ALS_WEST%') AND EMP_STATUS = 'Active' ORDER BY EI.EMP_NAME ASC";

			} else if (isEfsUser) {
				queryString = "SELECT EI.EMP_NAME,EI." + dayOfWeek
						+ ",EI.DESIGNATION,EI.EMP_EMAIL,IDP.USER_ROLE,EI.LOCATION,EI.EMP_ID FROM EMP_INFO AS EI INNER JOIN TM_USER_IDP_MAPPING AS IDP ON EI.EMP_ID = IDP.USER_LOGIN_NAME "
						+ "WHERE (UPPER(IDP.USER_ROLE) LIKE '%OBX_CATARINA%' OR UPPER(IDP.USER_ROLE) LIKE '%OBX_KARNES%' OR UPPER(IDP.USER_ROLE) LIKE '%OBX_TILDEN%' OR "
						+ "UPPER(IDP.USER_ROLE) LIKE '%PRO_CATARINA%' OR UPPER(IDP.USER_ROLE) LIKE '%PRO_TILDEN%' OR UPPER(IDP.USER_ROLE) LIKE '%PRO_KARNES%' "
						+ "OR UPPER(IDP.USER_ROLE) LIKE '%ALS_EAST%' OR UPPER(IDP.USER_ROLE) LIKE '%ALS_WEST%') AND EMP_STATUS = 'Active' ORDER BY EI.EMP_NAME ASC";

			}
			// else if(isCanadaUser){
			// queryString = "SELECT EI.EMP_NAME,EI." + dayOfWeek
			// +
			// ",EI.DESIGNATION,EI.EMP_EMAIL,IDP.USER_ROLE,EI.LOCATION,EI.EMP_ID
			// FROM EMP_INFO AS EI INNER JOIN TM_USER_IDP_MAPPING AS IDP ON
			// EI.EMP_ID = IDP.USER_LOGIN_NAME "
			// + "WHERE IDP.USER_ROLE LIKE '%PRO_KAYBOB%' OR IDP.USER_ROLE LIKE
			// '%PRO_MONTNEY%' AND EMP_STATUS = 'Active' ORDER BY EI.EMP_NAME
			// ASC";
			// }

			logger.error("query = " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] objects : resultList) {
					empShiftDetailsDto = new EmpShiftDetailsDto();
					empShiftDetailsDto.setEmpName(ServicesUtil.isEmpty(objects[0]) ? null : (String) objects[0]);
					empShiftDetailsDto.setDesignation(ServicesUtil.isEmpty(objects[2]) ? null : (String) objects[2]);
					empShiftDetailsDto.setEmpEmail(ServicesUtil.isEmpty(objects[3]) ? null : (String) objects[3]);
					empShiftDetailsDto.setUserRole(ServicesUtil.isEmpty(objects[4]) ? null : (String) objects[4]);
					empShiftDetailsDto.setLocation(ServicesUtil.isEmpty(objects[5]) ? null : (String) objects[5]);
					empShiftDetailsDto.setEmpId(ServicesUtil.isEmpty(objects[6]) ? null : (String) objects[6]);
					empShiftDetailsDto.setDate(date);
					int shiftCheck = ServicesUtil.isEmpty(objects[1]) ? 0 : (int) objects[1];

					if (shiftCheck == 1)
						empShiftDetailsDto.setIsMorning("true");

					if (shiftCheck == 2)
						empShiftDetailsDto.setIsEvening("true");

					if (shiftCheck == 3) {
						empShiftDetailsDto.setIsMorning("true");
						empShiftDetailsDto.setIsEvening("true");
					}

					// Fetch all Facilities & Location Code by UserRole
					// Map<String, String> facilityNameList =
					// getRouteDropDownByUserRole(empShiftDetailsDto.getUserRole());

					// Get Route Location Mapping Details by EmpId
					// empShiftDetailsDto.setRouteLocationDtoList(
					// routeLocationDao.fetchMappedLocByEmpId(empShiftDetailsDto.getEmpId(),
					// facilityNameList));

					// Assign Default Location
					if (ServicesUtil.isEmpty(empShiftDetailsDto.getLocation())) {

						if (!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().toUpperCase().contains(MurphyConstant.OBX_CATARINA)
										|| (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_CATARINA)))) {
							empShiftDetailsDto.setLocation("Kone West");
						} else if (!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().toUpperCase().contains(MurphyConstant.OBX_KARNES)
										|| (empShiftDetailsDto.getUserRole().toUpperCase()
												.contains(MurphyConstant.PRO_KARNES)))) {
							empShiftDetailsDto.setLocation("Drees South");

						} else if ((!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole())
								&& (empShiftDetailsDto.getUserRole().toUpperCase().contains(MurphyConstant.OBX_TILDEN)
										|| (empShiftDetailsDto.getUserRole().contains(MurphyConstant.PRO_TILDEN))))) {
							empShiftDetailsDto.setLocation("Jambers West");
						} else if ((!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole()) && (empShiftDetailsDto
								.getUserRole().toUpperCase().contains(MurphyConstant.PRO_KAYBOB.toUpperCase())))) {
							empShiftDetailsDto.setLocation("Kaybob East");
						} else if ((!ServicesUtil.isEmpty(empShiftDetailsDto.getUserRole()) && (empShiftDetailsDto
								.getUserRole().toUpperCase().contains(MurphyConstant.PRO_MONTNEY.toUpperCase())))) {
							empShiftDetailsDto.setLocation("Tupper Main");
						}

					} else if (!ServicesUtil.isEmpty(empShiftDetailsDto.getLocation())) {

						empShiftDetailsDto.setLocation(
								hierarchyDao.getLocationByLocCode(empShiftDetailsDto.getLocation()).trim());
					}

					// Fetch all Facilities by UserRole
					List<Map<String, String>> facilityNameList = getRouteDropDownByUserRole(
							empShiftDetailsDto.getUserRole());
					if (!ServicesUtil.isEmpty(facilityNameList))
						empShiftDetailsDto.setFacilityList(facilityNameList);

					EmpShiftDetailsDto.add(empShiftDetailsDto);
				}
				logger.error("EmpShiftDetailsDto =" + EmpShiftDetailsDto);
			}

			return EmpShiftDetailsDto;
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][empShiftDetailsUnderROC][Exception]" + e.getMessage());
		}
		return null;
	}

	private List<Map<String, String>> getRouteDropDownByUserRole(String userRole) {
		StringBuilder fieldName = new StringBuilder();
		if (!ServicesUtil.isEmpty(userRole)) {

			if (userRole.toUpperCase().contains(MurphyConstant.PRO_CATARINA)
					|| userRole.toUpperCase().contains(MurphyConstant.OBX_CATARINA)) {
				fieldName.append(MurphyConstant.CATARINA_CODE).append(",");
			}
			if (userRole.toUpperCase().contains(MurphyConstant.PRO_KARNES)
					|| userRole.toUpperCase().contains(MurphyConstant.OBX_KARNES)) {
				fieldName.append(MurphyConstant.KARNES_CODE).append(",");
			}
			if (userRole.toUpperCase().contains(MurphyConstant.PRO_TILDEN)
					|| userRole.toUpperCase().contains(MurphyConstant.OBX_TILDEN)) {
				fieldName.append(MurphyConstant.TILDEN_CODE).append(",");
			}
			if (userRole.toUpperCase().contains(MurphyConstant.PRO_KAYBOB.toUpperCase())) {
				fieldName.append(MurphyConstant.KAYBOB_CODE).append(",");
			}
			if (userRole.toUpperCase().contains(MurphyConstant.PRO_MONTNEY.toUpperCase())) {
				fieldName.append(MurphyConstant.MONTNEY_CODE).append(",");
			}
//			if (userRole.toUpperCase().contains(MurphyConstant.ALS_EAST.toUpperCase())){
//				fieldName.append(MurphyConstant.ALS_EAST_LOC).append(",");
//			}
//			if (userRole.toUpperCase().contains(MurphyConstant.ALS_WEST.toUpperCase())){
//				fieldName.append(MurphyConstant.ALS_WEST_LOC).append(",");
//			}
			if (!ServicesUtil.isEmpty(fieldName))
				fieldName.deleteCharAt(fieldName.length() - 1);

			// get FacilityRouteDetails by Field
			return hierarchyDao.getFacilityByField(fieldName.toString());

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getShiftEndTimingsbyEmp(String emp_email, String country) {
		String shift_end_time = "", shift_end_time_utc = "";
		String from_zone = MurphyConstant.CST_ZONE;
		try {
			Calendar calendar = Calendar.getInstance();
			if (country.equalsIgnoreCase("US"))
				calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			else if (country.equalsIgnoreCase("CA")) {
				String roles = "", country_role = "Montney";
				String query = "SELECT USER_ROLE FROM tm_user_idp_mapping WHERE lower(USER_EMAIL) = '"
						+ emp_email.toLowerCase() + "'";

				logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][rolequery] : " + query);
				Object obj = this.getSession().createSQLQuery(query).uniqueResult();
				roles = ServicesUtil.isEmpty(obj) ? null : (String) obj;
				// Checking if user is from Kaybob/Montney based on roles
				if (roles.toLowerCase().contains(MurphyConstant.PRO_KAYBOB.toLowerCase()))
					country_role = "Kaybob";
				else if (roles.toLowerCase().contains(MurphyConstant.PRO_MONTNEY.toLowerCase()))
					country_role = "Montney";

				// Setting time zone for Canada
				if (country_role.equalsIgnoreCase("Kaybob")) {
					country = "Kaybob";
					from_zone = MurphyConstant.PST_ZONE;
					calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.PST_ZONE));
				} else if (country_role.equalsIgnoreCase("Montney")) {
					country = "Montney";
					from_zone = MurphyConstant.MST_ZONE;
					calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.MST_ZONE));
				}
			}

			String dayOfWeek = null;
			int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayNum == 2)
				dayOfWeek = "Monday";
			if (dayNum == 3)
				dayOfWeek = "Tuesday";
			if (dayNum == 4)
				dayOfWeek = "Wednesday";
			if (dayNum == 5)
				dayOfWeek = "Thursday";
			if (dayNum == 6)
				dayOfWeek = "Friday";
			if (dayNum == 7)
				dayOfWeek = "Saturday";
			if (dayNum == 1)
				dayOfWeek = "Sunday";

			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int shift_slot = 0;
			String stringQuery = "SELECT " + dayOfWeek + " FROM EMP_INFO where EMP_STATUS='Active' "
					+ "and lower(emp_email) = '" + emp_email.toLowerCase() + "'";

			logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][Query shift] : " + stringQuery);
			Object obj = this.getSession().createSQLQuery(stringQuery).uniqueResult();
			shift_slot = ServicesUtil.isEmpty(obj) ? 0 : (int) obj;

			logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][shift_slot] : " + shift_slot + " [hour] "
					+ hour + " [dayOfWeek] " + dayOfWeek + "[country] " + country);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			if (country.equalsIgnoreCase("US")) {
				// Night Shift
				if (shift_slot == 2) {
					if ((hour >= 17 && hour <= 23)) {
						calendar.add(Calendar.DATE, 1);
						shift_end_time = sdf.format(calendar.getTime()) + " " + "05:00:00";
					} else if ((hour >= 0 && hour <= 4)) {
						shift_end_time = sdf.format(calendar.getTime()) + " " + "05:00:00";
					} else {
						shift_end_time = sdf.format(calendar.getTime()) + " " + "05:00:00";
					}
				}
				// Day shift
				else if (shift_slot == 1) {
					shift_end_time = sdf.format(calendar.getTime()) + " " + "17:00:00";
				}
				// both the shifts, then consider day shift end time
				else if (shift_slot == 3) {
					calendar.add(Calendar.DATE, 1);
					shift_end_time = sdf.format(calendar.getTime()) + " " + "17:00:00";
				}
			} else if (country.equalsIgnoreCase("Kaybob")) {
				// Night Shift
				if (shift_slot == 2) {
					if ((hour >= 20 && hour <= 23)) {
						calendar.add(Calendar.DATE, 1);
						shift_end_time = sdf.format(calendar.getTime()) + " " + "08:00:00";
					} else if ((hour >= 0 && hour <= 7)) {
						shift_end_time = sdf.format(calendar.getTime()) + " " + "08:00:00";
					} else {
						shift_end_time = sdf.format(calendar.getTime()) + " " + "08:00:00";
					}
				}
				// Day shift
				else if (shift_slot == 1) {
					shift_end_time = sdf.format(calendar.getTime()) + " " + "20:00:00";
				}
				// both the shifts, then consider day shift end time
				else if (shift_slot == 3) {
					calendar.add(Calendar.DATE, 1);
					shift_end_time = sdf.format(calendar.getTime()) + " " + "20:00:00";
				}
			} else if (country.equalsIgnoreCase("Montney")) {
				// Night Shift
				if (shift_slot == 2) {
					if ((hour >= 20 && hour <= 23)) {
						calendar.add(Calendar.DATE, 1);
						shift_end_time = sdf.format(calendar.getTime()) + " " + "08:00:00";
					} else if ((hour >= 0 && hour <= 7)) {
						shift_end_time = sdf.format(calendar.getTime()) + " " + "08:00:00";
					} else {
						shift_end_time = sdf.format(calendar.getTime()) + " " + "08:00:00";
					}
				}
				// Day shift
				else if (shift_slot == 1) {
					shift_end_time = sdf.format(calendar.getTime()) + " " + "20:00:00";
				}
				// both the shifts, then consider day shift end time
				else if (shift_slot == 3) {
					calendar.add(Calendar.DATE, 1);
					shift_end_time = sdf.format(calendar.getTime()) + " " + "20:00:00";
				}
			}
			shift_end_time_utc = ServicesUtil.convertFromZoneToZoneString(null, shift_end_time, from_zone,
					MurphyConstant.UTC_ZONE, MurphyConstant.DATE_IOS_FORMAT, MurphyConstant.DATE_IOS_FORMAT);
			logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][shift_end_time] " + shift_end_time
					+ " shift_end_time_utc : " + shift_end_time_utc);
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][Exception]" + e.getMessage());
		}
		return shift_end_time_utc;
	}

	// To show task only when operator is in shift, else no task
	/*
	 * @SuppressWarnings("unchecked") public String
	 * getShiftEndTimingsbyEmp(String emp_email) { String shift_end_time = "";
	 * 
	 * try{ Calendar calendar = Calendar.getInstance();
	 * calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
	 * 
	 * String dayOfWeek = null; int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
	 * if(dayNum == 2) dayOfWeek = "Monday"; if(dayNum == 3) dayOfWeek =
	 * "Tuesday"; if(dayNum == 4) dayOfWeek = "Wednesday"; if(dayNum == 5)
	 * dayOfWeek = "Thursday"; if(dayNum == 6) dayOfWeek = "Friday"; if(dayNum
	 * == 7) dayOfWeek = "Saturday"; if(dayNum == 1) dayOfWeek = "Sunday";
	 * 
	 * int hour = calendar.get(Calendar.HOUR_OF_DAY); int shift_slot = 0; String
	 * stringQuery = "SELECT " + dayOfWeek +
	 * " FROM EMP_INFO where EMP_STATUS='Active' " + "and lower(emp_email) = '"+
	 * emp_email.toLowerCase() + "'";
	 * 
	 * logger.
	 * error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][Query shift] : "
	 * + stringQuery ); Object obj =
	 * this.getSession().createSQLQuery(stringQuery).uniqueResult(); shift_slot
	 * = ServicesUtil.isEmpty(obj) ? 0 : (int) obj;
	 * 
	 * logger.
	 * error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][shift_slot] : "
	 * + shift_slot + " [hour] " + hour + " [dayOfWeek] " + dayOfWeek);
	 * 
	 * // Fetch Night Shift timings String configuration_night =
	 * configDao.getConfigurationByRef("NIGHT_SHIFT_START_TIME"); int
	 * night_shift_time = Integer.parseInt(configuration_night); // Fetch Day
	 * Shift timings String configuration_day =
	 * configDao.getConfigurationByRef("DAY_SHIFT_START_TIME"); int
	 * day_shift_time = Integer.parseInt(configuration_day);
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * // Night Shift if(shift_slot == 2){ if(((hour >= 17 && hour <= 23) &&
	 * hour >= night_shift_time)) { calendar.add(Calendar.DATE, 1);
	 * shift_end_time = sdf.format(calendar.getTime()) +" "+"05:00:00"; } else
	 * if((hour >= 0 && hour <= 4) && hour < day_shift_time){ shift_end_time =
	 * sdf.format(calendar.getTime()) +" "+"05:00:00"; } } // Day shift else
	 * if(shift_slot == 1) { if(hour >= day_shift_time && hour <
	 * night_shift_time){ shift_end_time = sdf.format(calendar.getTime())
	 * +" "+"17:00:00"; } } // both the shifts, then consider day shift end time
	 * else if(shift_slot == 3){ shift_end_time = sdf.format(calendar.getTime())
	 * +" "+"17:00:00"; } logger.
	 * error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][shift_end_time] "
	 * +shift_end_time); } catch(Exception e){ logger.error(
	 * "[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][Exception]" +
	 * e.getMessage()); } return shift_end_time; }
	 */

	@SuppressWarnings("unchecked")
	public boolean getShiftDetailsForEmp(String emp_email) {
		boolean inShift = false;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));

			String dayOfWeek = null;
			int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayNum == 2)
				dayOfWeek = "Monday";
			if (dayNum == 3)
				dayOfWeek = "Tuesday";
			if (dayNum == 4)
				dayOfWeek = "Wednesday";
			if (dayNum == 5)
				dayOfWeek = "Thursday";
			if (dayNum == 6)
				dayOfWeek = "Friday";
			if (dayNum == 7)
				dayOfWeek = "Saturday";
			if (dayNum == 1)
				dayOfWeek = "Sunday";

			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int shift_slot = 0;
			String stringQuery = "SELECT " + dayOfWeek + " FROM EMP_INFO where EMP_STATUS='Active' "
					+ "and lower(emp_email) = '" + emp_email.toLowerCase() + "'";

			logger.error("[Murphy][ShiftRegisterDao][getShiftDetailsForEmp][Query shift] : " + stringQuery);
			Object obj = this.getSession().createSQLQuery(stringQuery).uniqueResult();
			shift_slot = ServicesUtil.isEmpty(obj) ? 0 : (int) obj;

			logger.error("[Murphy][ShiftRegisterDao][getShiftDetailsForEmp][shift_slot] : " + shift_slot + " [hour] "
					+ hour + " [dayOfWeek] " + dayOfWeek);
			// Night Shift
			if (shift_slot == 2) {
				if ((hour >= 17 && hour <= 23) || (hour >= 0 && hour <= 4)) {
					inShift = true;
				}
			}
			// Day shift
			else if (shift_slot == 1) {
				if (hour >= 5 && hour < 17) {
					inShift = true;
				}
			}
			// both the shifts
			else if (shift_slot == 3) {
				inShift = true;
			}

			logger.error("[Murphy][ShiftRegisterDao][getShiftDetailsForEmp][inShift] " + inShift);
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][getShiftDetailsForEmp][Exception]" + e.getMessage());
		}
		return inShift;
	}

	public Map<String, String> getAllWellPadByFacility(String empId, String facility) throws Exception {
		return hierarchyDao.getWellPadLocationByFacility(facility);
	}

	@SuppressWarnings("unchecked")
	public String getShiftStartTimingsbyEmp(String emp_email) {
		String shift_start_time = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
			String dayOfWeek = null;
			int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayNum == 2)
				dayOfWeek = "Monday";
			if (dayNum == 3)
				dayOfWeek = "Tuesday";
			if (dayNum == 4)
				dayOfWeek = "Wednesday";
			if (dayNum == 5)
				dayOfWeek = "Thursday";
			if (dayNum == 6)
				dayOfWeek = "Friday";
			if (dayNum == 7)
				dayOfWeek = "Saturday";
			if (dayNum == 1)
				dayOfWeek = "Sunday";

			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int shift_slot = 0;
			String stringQuery = "SELECT " + dayOfWeek + " FROM EMP_INFO where EMP_STATUS='Active' "
					+ "and lower(emp_email) = '" + emp_email.toLowerCase() + "'";
			logger.error("[Murphy][ShiftRegisterDao][getShiftStartTimingsbyEmp][Query shift] : " + stringQuery);
			Object obj = this.getSession().createSQLQuery(stringQuery).uniqueResult();
			shift_slot = ServicesUtil.isEmpty(obj) ? 0 : (int) obj;
			logger.error("[Murphy][ShiftRegisterDao][getShiftStartTimingsbyEmp][shift_slot] : " + shift_slot
					+ " [hour] " + hour + " [dayOfWeek] " + dayOfWeek);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// Night Shift
			if (shift_slot == 2) {
				if ((hour >= 17 && hour <= 23)) {
					shift_start_time = sdf.format(calendar.getTime()) + " " + "17:00:00";
				} else if ((hour >= 0 && hour <= 4)) {
					calendar.add(Calendar.DATE, -1);
					shift_start_time = sdf.format(calendar.getTime()) + " " + "17:00:00";
				} else {
					shift_start_time = sdf.format(calendar.getTime()) + " " + "17:00:00";
				}
			}
			// Day shift
			else if (shift_slot == 1) {
				shift_start_time = sdf.format(calendar.getTime()) + " " + "05:00:00";
			}
			// both the shifts, then consider day shift start time
			else if (shift_slot == 3) {
				calendar.add(Calendar.DATE, -1);
				shift_start_time = sdf.format(calendar.getTime()) + " " + "05:00:00";
			}
			logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][shift_end_time] " + shift_start_time);
		} catch (Exception e) {
			logger.error("[Murphy][ShiftRegisterDao][getShiftEndTimingsbyEmp][Exception]" + e.getMessage());
		}

		return shift_start_time;
	}
}
