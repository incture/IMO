package com.murphy.taskmgmt.dao;

import java.sql.Timestamp;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.StopTimeDto;
import com.murphy.taskmgmt.entity.StopTimeDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("StopTimeDao")
@Transactional
public class StopTimeDao extends BaseDao<StopTimeDo, StopTimeDto> {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(StopTimeDao.class);

	@Override
	protected StopTimeDo importDto(StopTimeDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		StopTimeDo entity = new StopTimeDo();
		entity.setClassification(fromDto.getClassification());
		entity.setId(fromDto.getId());
		entity.setObx(fromDto.getObx());
		entity.setProA(fromDto.getProA());
		entity.setProB(fromDto.getProB());
		entity.setSse(fromDto.getSse());
		entity.setSubClassification(fromDto.getSubClassification());
		return entity;
	}

	@Override
	protected StopTimeDto exportDto(StopTimeDo entity) {
		StopTimeDto toDto = new StopTimeDto();
		toDto.setClassification(entity.getClassification());
		toDto.setId(entity.getId());
		toDto.setObx(entity.getObx());
		toDto.setProA(entity.getProA());
		toDto.setProB(entity.getProB());
		toDto.setSse(entity.getSse());
		toDto.setSubClassification(entity.getSubClassification());
		return toDto;
	}

	@SuppressWarnings("unchecked")
	public StopTimeDto getStopTimeByCategory(String classification, String subClassification) {
		StopTimeDto timeSet = null;
		String queryString = "select PRO_A, PRO_B, OBX, SSE from TM_TASK_STOP_TIME_BY_ROLE where CLASSIFICATION = '"
				+ classification + "' AND " + "SUB_CLASSIFICATION = '" + subClassification + "'";
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> response = (List<Object[]>) q.list();
		if (!ServicesUtil.isEmpty(response)) {
			timeSet = new StopTimeDto();
			for (Object[] obj : response) {
				timeSet.setProA(ServicesUtil.isEmpty(obj[0]) ? null : (int) obj[0]);
				timeSet.setProB(ServicesUtil.isEmpty(obj[1]) ? null : (int) obj[1]);
				timeSet.setObx(ServicesUtil.isEmpty(obj[2]) ? null : (int) obj[2]);
				timeSet.setSse(ServicesUtil.isEmpty(obj[3]) ? null : (int) obj[3]);
			}
			timeSet.setClassification(classification);
			timeSet.setSubClassification(subClassification);
		}
		return timeSet;
	}

	public Map<String, List<String>> getEmpByShift(Calendar calendar) {
		Map<String, List<String>> empDetails = null;
		try {
			List<String> proA = new ArrayList<>();
			List<String> proB = new ArrayList<>();
			List<String> obx = new ArrayList<>();
			List<String> sse = new ArrayList<>();
			
			String dayOfWeek = LocalDate.now().getDayOfWeek().name();
			logger.error("CST Day of week line 90 =" + dayOfWeek);
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
			logger.error("CST Day of week line 102 =" + dayOfWeek);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.MINUTE);
			String shift = checkShift(hour, min);
			logger.error("CST Time = " + hour + ":" + min);
			String queryString = "select DESIGNATION,EMP_EMAIL,START_DATE from EMP_INFO where (" + dayOfWeek
					+ " = '3' OR " + dayOfWeek + " = '" + shift + "')";
			logger.error("Query " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				empDetails = new HashMap<String, List<String>>();
				for (Object[] objects : resultList) {
					String designation = (String) objects[0];
					Date dt1 = calendar.getTime();
					// Timestamp timestamp1 = new Timestamp(dt1.getTime());
					logger.error("today date =" + dt1);

					DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd");
					DateTime dt2 = formatter.parseDateTime((String) objects[2]);
					logger.error("db date =" + dt2.toDate());

					long diff = dt1.getTime() - dt2.toDate().getTime();
				
					long convertToDays = 1000 * 60 * 60 * 24;
					logger.error("converting factor =" + convertToDays);
					int diffInDays = (int) (diff / convertToDays);
					logger.error("Time gap between " + dt1 + " and " + dt2.toDate() + " is ="
							+ diffInDays + " days");

					if (diffInDays > 365) {
						if (designation.equalsIgnoreCase(MurphyConstant.PRO_A))
							proA.add((String) objects[1]);
						if (designation.equalsIgnoreCase(MurphyConstant.PRO_B))
							proB.add((String) objects[1]);
						if (designation.equalsIgnoreCase(MurphyConstant.OBX_B)
								|| designation.equalsIgnoreCase(MurphyConstant.OBX_C))
							obx.add((String) objects[1]);
					} else {
						// if (designation.equalsIgnoreCase(MurphyConstant.SSE))
						sse.add((String) objects[1]);
					}

				}
			}

			empDetails.put(MurphyConstant.PRO_A, proA);
			empDetails.put(MurphyConstant.PRO_B, proB);
			empDetails.put(MurphyConstant.OBX, obx);
			empDetails.put(MurphyConstant.SSE, sse);

		} catch (Exception e) {
			logger.error("[StopTimeDao][getEmpByShift][Exception] " + e.getMessage());
		}
		return empDetails;
	}

	private String checkShift(int hour, int min) {
		if (5 <= hour && hour < 17) {
			return "1";
		} else {
			return "2";
		}
	}

	public Map<String, List<String>> getEmpByShift(Calendar calendar, Map<String, List<String>> operatorToRouteMap) {
		Map<String, List<String>> empDetails = null;
		try {
			List<String> proA = new ArrayList<>();
			List<String> proB = new ArrayList<>();
			List<String> obx = new ArrayList<>();
			List<String> sse = new ArrayList<>();
			
			String dayOfWeek = LocalDate.now().getDayOfWeek().name();
			logger.error("CST Day of week line 90 =" + dayOfWeek);
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
			logger.error("CST Day of week line 102 =" + dayOfWeek);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.MINUTE);
			String shift = checkShift(hour, min);
			logger.error("CST Time = " + hour + ":" + min);
			String queryString = "select DESIGNATION,EMP_EMAIL,START_DATE,EMP_ID,LOCATION from EMP_INFO where (" + dayOfWeek
					+ " = '3' OR " + dayOfWeek + " = '" + shift + "')";
			logger.error("Query " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				empDetails = new HashMap<String, List<String>>();
				for (Object[] objects : resultList) {
					String designation = (String) objects[0];
					Date dt1 = calendar.getTime();
					// Timestamp timestamp1 = new Timestamp(dt1.getTime());
					logger.error("today date =" + dt1);

					DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd");
					DateTime dt2 = formatter.parseDateTime((String) objects[2]);
					logger.error("db date =" + dt2.toDate());

					long diff = dt1.getTime() - dt2.toDate().getTime();
				
					long convertToDays = 1000 * 60 * 60 * 24;
					logger.error("converting factor =" + convertToDays);
					int diffInDays = (int) (diff / convertToDays);
					logger.error("Time gap between " + dt1 + " and " + dt2.toDate() + " is ="
							+ diffInDays + " days");

					
					// get the location
					String location = ServicesUtil.isEmpty(objects[4]) ? null : ((String) objects[4]).trim();
					List<String> locationList = new ArrayList<>();
					locationList.add(location);
					
					operatorToRouteMap.put((String) objects[1], locationList);
					
					if (diffInDays > 365) {

						if (!ServicesUtil.isEmpty(designation)) {

							if (designation.equalsIgnoreCase(MurphyConstant.PRO_A))
								proA.add((String) objects[1]);
							if (designation.equalsIgnoreCase(MurphyConstant.PRO_B))
								proB.add((String) objects[1]);
							if (designation.equalsIgnoreCase(MurphyConstant.OBX_B)
									|| designation.equalsIgnoreCase(MurphyConstant.OBX_C))
								obx.add((String) objects[1]);
						}
					} else {
						// if (designation.equalsIgnoreCase(MurphyConstant.SSE))
						sse.add((String) objects[1]);
					}

				}
			}

			empDetails.put(MurphyConstant.PRO_A, proA);
			empDetails.put(MurphyConstant.PRO_B, proB);
			empDetails.put(MurphyConstant.OBX, obx);
			empDetails.put(MurphyConstant.SSE, sse);

		} catch (Exception e) {
			logger.error("[StopTimeDao][getEmpByShift][Exception] " + e.getMessage());
		}
		return empDetails;
	}

}
