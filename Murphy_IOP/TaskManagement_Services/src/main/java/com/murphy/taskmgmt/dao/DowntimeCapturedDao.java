package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.integration.dao.DowntimeCaptureDao;
import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.integration.entity.DowntimeCaptureDo;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.service.DowntimeCapture;
import com.murphy.integration.util.DBConnections;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.HandoverNotesDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHistoryRolledUpDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.SSVCloseResponseDto;
import com.murphy.taskmgmt.entity.DowntimeCapturedDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("downtimeCapturedDao")
public class DowntimeCapturedDao extends BaseDao<DowntimeCapturedDo, DowntimeCapturedDto> {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCapturedDao.class);

	public DowntimeCapturedDao() {
	}

	@Autowired
	HierarchyDao locDao;

	@Autowired
	CygnetAlarmFeedDao alarmDao;
	
	@Autowired
	ProductionLocationDao productionLocationDao;
	
	@Autowired
	HierarchyDao hierarchyDao;

	@Override
	protected DowntimeCapturedDo importDto(DowntimeCapturedDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		DowntimeCapturedDo entity = new DowntimeCapturedDo();

		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getFacility()))
			entity.setFacility(fromDto.getFacility());
		if (!ServicesUtil.isEmpty(fromDto.getMuwi()))
			entity.setMuwi(fromDto.getMuwi());
		if (!ServicesUtil.isEmpty(fromDto.getEquipmentId()))
			entity.setEquipmentId(fromDto.getEquipmentId());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getType()))
			entity.setType(fromDto.getType());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedBy()))
			entity.setUpdatedBy(fromDto.getUpdatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getWell()))
			entity.setWell(fromDto.getWell());
		if (!ServicesUtil.isEmpty(fromDto.getChildCode()))
			entity.setChildCode(fromDto.getChildCode());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(ServicesUtil.resultAsDateDowntime(fromDto.getCreatedAt()));
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(ServicesUtil.resultAsDateDowntime((fromDto.getUpdatedAt())));
		if (!ServicesUtil.isEmpty(fromDto.getDowntimeCode()))
			entity.setDowntimeCode(fromDto.getDowntimeCode());
		if (!ServicesUtil.isEmpty(fromDto.getDurationByRocMinute()))
			entity.setDurationByRoc(fromDto.getDurationByRocMinute());
		if (!ServicesUtil.isEmpty(fromDto.getDurationByCygnateMinute()))
			entity.setDurationInMinute(fromDto.getDurationByCygnateMinute());
		if (!ServicesUtil.isEmpty(fromDto.getEndTime()))
			entity.setEndTime(fromDto.getEndTime());
		if (!ServicesUtil.isEmpty(fromDto.getStartTime()))
			entity.setStartTime(fromDto.getStartTime());
		if (!ServicesUtil.isEmpty(fromDto.getChildText()))
			entity.setChildText(fromDto.getChildText());
		if (!ServicesUtil.isEmpty(fromDto.getDowntimeText()))
			entity.setDowntimeText(fromDto.getDowntimeText());
		if (!ServicesUtil.isEmpty(fromDto.getPointId()))
			entity.setPointId(fromDto.getPointId());
		if (!ServicesUtil.isEmpty(fromDto.getCountryCode()))
			entity.setCountryCode(fromDto.getCountryCode());
		return entity;
	}

	@Override
	protected DowntimeCapturedDto exportDto(DowntimeCapturedDo entity) {

		DowntimeCapturedDto dto = new DowntimeCapturedDto();

		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getCreatedBy()))
			dto.setCreatedBy(entity.getCreatedBy());
		if (!ServicesUtil.isEmpty(entity.getFacility()))
			dto.setFacility(entity.getFacility());
		if (!ServicesUtil.isEmpty(entity.getMuwi()))
			dto.setMuwi(entity.getMuwi());
		if (!ServicesUtil.isEmpty(entity.getEquipmentId()))
			dto.setEquipmentId(entity.getEquipmentId());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			dto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getType()))
			dto.setType(entity.getType());
		if (!ServicesUtil.isEmpty(entity.getUpdatedBy()))
			dto.setUpdatedBy(entity.getUpdatedBy());
		if (!ServicesUtil.isEmpty(entity.getWell()))
			dto.setWell(entity.getWell());
		if (!ServicesUtil.isEmpty(entity.getChildCode()))
			dto.setChildCode(entity.getChildCode());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(ServicesUtil.isEmpty(entity.getCreatedAt()) ? null
					: ServicesUtil.resultDateAsStringDowntime(entity.getCreatedAt()));
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			dto.setUpdatedAt(ServicesUtil.isEmpty(entity.getUpdatedAt()) ? null
					: ServicesUtil.resultDateAsStringDowntime(entity.getUpdatedAt()));
		if (!ServicesUtil.isEmpty(entity.getDowntimeCode()))
			dto.setDowntimeCode(entity.getDowntimeCode());
		if (!ServicesUtil.isEmpty(entity.getDurationByRoc()))
			dto.setDurationByRocMinute(entity.getDurationByRoc());
		if (!ServicesUtil.isEmpty(entity.getDurationInMinute()))
			dto.setDurationByCygnateMinute(entity.getDurationInMinute());
		if (!ServicesUtil.isEmpty(entity.getEndTime()))
			dto.setEndTime(entity.getEndTime());
		if (!ServicesUtil.isEmpty(entity.getStartTime()))
			dto.setStartTime(ServicesUtil.isEmpty(entity.getStartTime()) ? null : (entity.getStartTime()));
		if (!ServicesUtil.isEmpty(entity.getPointId()))
			dto.setPointId(entity.getPointId());
		if (!ServicesUtil.isEmpty(entity.getCountryCode()))
			dto.setCountryCode(entity.getCountryCode());
		return dto;
	}

	@SuppressWarnings("unchecked")
	public ResponseMessage createDowntimeCaptured(DowntimeUpdateDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		Boolean proCountValue = dto.getIsProCountUpdate();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Downtime " + MurphyConstant.CREATE_FAILURE);
		DowntimeCapturedDto capturedDto = dto.getDto();
		try {
			/* change start time from utc to cst */

			if (dto.getIsProCountUpdate().equals(true)) {
				if (!ServicesUtil.isEmpty(capturedDto.getDurationByRocMinute())
						|| !ServicesUtil.isEmpty(capturedDto.getDurationByRocHour()))
					capturedDto.setStatus(MurphyConstant.DOWNTIME_COMPLETE);
				else
					capturedDto.setStatus(MurphyConstant.DOWNTIME_PENDING);
			} else if (dto.getIsProCountUpdate().equals(false)) {

				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				// SOC: CST to UTC
				format1.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
				String dateStr = format1.format(new Date(capturedDto.getStartTime().getTime()));
				Date utcStartTime = ServicesUtil.resultAsDateDowntime(dateStr);
				capturedDto.setStartTime(utcStartTime);
				// EOC : CST to UTC

				capturedDto.setStatus(MurphyConstant.DOWNTIME_PENDING);
			}
			// SOC : Date to be stored in UTC while creating in Hana
			logger.error("CreatedAT time Millisec from UI: " + capturedDto.getCreatedAt());
			long millis = Long.parseLong(capturedDto.getCreatedAt());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(millis);
			capturedDto.setCreatedAt(sdf.format(cal.getTime()));
			capturedDto.setCountryCode(dto.getCountryCode());
			logger.error("[Created AT UTC]:" + capturedDto.getCreatedAt());
			// EOC
			if (!ServicesUtil.isEmpty(capturedDto.getDurationByRocHour()))
				capturedDto.setDurationByRocMinute((capturedDto.getDurationByRocHour() * 60)
						+ (!ServicesUtil.isEmpty(capturedDto.getDurationByRocMinute())
								? capturedDto.getDurationByRocMinute() : 0));

			if (!ServicesUtil.isEmpty(capturedDto.getMuwi())) {

				String getWellQuery = "select p1.location_text from production_location p1 join well_muwi wm on wm.location_code=p1.location_code "
						+ "where wm.muwi='" + capturedDto.getMuwi() + "'";
				Query q = this.getSession().createSQLQuery(getWellQuery);
				List<String> response = q.list();
				if (!ServicesUtil.isEmpty(response)) {
					String wellName = (String) response.get(0);
					capturedDto.setWell(wellName);
				}
			}
			create(capturedDto);

			String designateResult = MurphyConstant.SUCCESS;
			/* update is_designate filed in alarm table */
			if (!ServicesUtil.isEmpty(dto.getDto().getPointId())) {
				designateResult = alarmDao.updateAlarmFeed("'" + dto.getDto().getPointId() + "'",
						MurphyConstant.ALARM_DESIGNATE, "Y");
			}
			if (designateResult.equals(MurphyConstant.SUCCESS)) {
				if (proCountValue.equals(false) || ServicesUtil.isEmpty(proCountValue)) {
					responseDto.setMessage("Alarm designated successfully");
				} else if (proCountValue.equals(true)) {
					responseDto.setMessage("Downtime " + MurphyConstant.CREATED_SUCCESS);
				}

				responseDto.setStatus(MurphyConstant.SUCCESS);
				responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			} else
				responseDto
						.setMessage(MurphyConstant.UPDATE_FAILURE + " For Is_DESIGNATE " + MurphyConstant.FIELD + "");
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCaptureDao][createRecord][error]" + e.getMessage());
		}

		return responseDto;
	}

	public String updateDowntimeCaptured(DowntimeCapturedDto dto) {
		String response = MurphyConstant.FAILURE;
		String subQuery = "";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocHour()))
			dto.setDurationByRocMinute((dto.getDurationByRocHour() * 60)
					+ (!ServicesUtil.isEmpty(dto.getDurationByRocMinute()) ? dto.getDurationByRocMinute() : 0));
		if (!ServicesUtil.isEmpty(dto.getDowntimeCode()))
			subQuery = subQuery + " dc.DOWNTIME_CODE =" + dto.getDowntimeCode() + ",";
		if (!ServicesUtil.isEmpty(dto.getChildCode()))
			subQuery = subQuery + " dc.CHILD_CODE =" + dto.getChildCode() + ",";
		if (!ServicesUtil.isEmpty(dto.getDowntimeText()))
			subQuery = subQuery + " dc.DOWNTIME_TEXT ='" + dto.getDowntimeText() + "',";
		if (!ServicesUtil.isEmpty(dto.getChildText()))
			subQuery = subQuery + " dc.CHILD_TEXT ='" + dto.getChildText() + "',";
		if (!ServicesUtil.isEmpty(dto.getUpdatedBy()))
			subQuery = subQuery + " dc.UPDATED_BY ='" + dto.getUpdatedBy() + "',";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocMinute()))
			subQuery = subQuery + " dc.DURATION_MINUTE_ROC =" + dto.getDurationByRocMinute() + ",";
		if (!ServicesUtil.isEmpty(dto.getEndTime()))
			subQuery = subQuery + " dc.END_TIME ='" + dto.getEndTime() + "',";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocMinute())) {
			subQuery = subQuery + " dc.UPDATED_AT = current_timestamp , " + "dc.status='" + MurphyConstant.COMPLETE
					+ "' ,";
		}

		// SOC : Date to be stored in UTC while creating in Hana
		logger.error("[Update] CreatedAT time Millisec from UI: " + dto.getCreatedAt());
		long millis = Long.parseLong(dto.getCreatedAt());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		dto.setCreatedAt(sdf.format(cal.getTime()));
		System.out.println("[Update DT ][Created At UTC]:" + dto.getCreatedAt());
		// EOC

		subQuery = subQuery.substring(0, subQuery.length() - 1);
		String updateQuery = "UPDATE DOWNTIME_CAPTURE dc SET " + subQuery + "  where id= '" + dto.getId() + "' ";
		int updatedRow = this.getSession().createSQLQuery(updateQuery).executeUpdate();
		if (updatedRow > 0) {
			response = MurphyConstant.SUCCESS;
		}
		return response;
	}

	public String updateCaDowntimeCaptured(DowntimeCapturedDto dto) {
		String response = MurphyConstant.FAILURE;
		String subQuery = "";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocHour()))
			dto.setDurationByRocMinute((dto.getDurationByRocHour() * 60)
					+ (!ServicesUtil.isEmpty(dto.getDurationByRocMinute()) ? dto.getDurationByRocMinute() : 0));
		if (!ServicesUtil.isEmpty(dto.getDowntimeCode()))
			subQuery = subQuery + " dc.DOWNTIME_CODE =" + dto.getDowntimeCode() + ",";
		if (!ServicesUtil.isEmpty(dto.getDowntimeText()))
			subQuery = subQuery + " dc.DOWNTIME_TEXT ='" + dto.getDowntimeText() + "',";
		if (!ServicesUtil.isEmpty(dto.getUpdatedBy()))
			subQuery = subQuery + " dc.UPDATED_BY ='" + dto.getUpdatedBy() + "',";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocMinute()))
			subQuery = subQuery + " dc.DURATION_MINUTE_ROC =" + dto.getDurationByRocMinute() + ",";
		if (!ServicesUtil.isEmpty(dto.getEndTime()))
			subQuery = subQuery + " dc.END_TIME ='" + dto.getEndTime() + "',";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocMinute())) {
			subQuery = subQuery + " dc.UPDATED_AT = current_timestamp , " + "dc.status='" + MurphyConstant.COMPLETE
					+ "' ,";
		}

		// SOC : Date to be stored in UTC while creating in Hana
		logger.error("[Update] CreatedAT time Millisec from UI: " + dto.getCreatedAt());
		long millis = Long.parseLong(dto.getCreatedAt());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		dto.setCreatedAt(sdf.format(cal.getTime()));
		System.out.println("[Update DT ][Created At UTC]:" + dto.getCreatedAt());
		// EOC

		subQuery = subQuery.substring(0, subQuery.length() - 1);
		String updateQuery = "UPDATE DOWNTIME_CAPTURE dc SET " + subQuery + "  where id= '" + dto.getId() + "' ";
		int updatedRow = this.getSession().createSQLQuery(updateQuery).executeUpdate();
		if (updatedRow > 0) {
			response = MurphyConstant.SUCCESS;
			logger.error("Record updated Success11");
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public DowntimeResponseDto getDowntime(DowntimeRequestDto dto) {
		logger.error("[Murphy][DowntimeCaptureDao][getDowntime]Method Started");
		List<LocationHierarchyDto> dtoList = dto.getLocationHierarchy();
		List<DowntimeCapturedDto> feedDtos = null;
		DowntimeCapturedDto proDcDto = null;
		String locationType = dto.getLocationType();
		String locations = locDao.getLocationUsinglocationType(dtoList, locationType);
		// Location History
		DowntimeResponseDto dtDto = new DowntimeResponseDto();
		List<LocationHistoryRolledUpDto> wellCountList = new ArrayList<LocationHistoryRolledUpDto>();
		LocationHistoryRolledUpDto lht = new LocationHistoryRolledUpDto();
		Query query = null;
		String recomCygnetDwnTmeDate=null;
		String currentDateInString=null;
		String nextDateInString=null;
		try {
			
			 String currentRecomTime=ServicesUtil.convertFromZoneToZoneString(new Date(),null,"",MurphyConstant.CST_ZONE,
					 MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD);
			recomCygnetDwnTmeDate=ServicesUtil.convertFromZoneToZoneString(new Date(),null,"",MurphyConstant.UTC_ZONE,
					MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(MurphyConstant.EFS_CODE.equalsIgnoreCase(dto.getCountryCode()) || MurphyConstant.CST_ZONE.equalsIgnoreCase(dto.getUserTimeZone())){
			format1.setTimeZone(TimeZone.getTimeZone("CST"));
			}else if(ServicesUtil.isEmpty(dto.getUserTimeZone()) || MurphyConstant.MST_ZONE.equalsIgnoreCase(dto.getUserTimeZone())){
				format1.setTimeZone(TimeZone.getTimeZone("MST"));
			}else if(MurphyConstant.PST_ZONE.equalsIgnoreCase(dto.getUserTimeZone())){
				format1.setTimeZone(TimeZone.getTimeZone("PST"));
			}
			String dateStr = format1.format(new Date());
			Date currentDate = ServicesUtil.resultAsDateDowntime(dateStr); 
			Date nextDate = ServicesUtil.getNextDate(currentDate);
			Boolean time = timeBetweenOrNot(currentDate, dto.getCountryCode());

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			Calendar currentCal = Calendar.getInstance();
			Integer countResult = 0;

			if (time.equals(true)) {
				nextDate = currentDate;
				currentDate = ServicesUtil.getPrevDate(currentDate);
			}
			
			if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
				nextDate = currentDate;
				currentDate = ServicesUtil.getPrevDate(currentDate);
			}
			
			currentDateInString=sdf.format(currentDate);
			nextDateInString=sdf.format(nextDate);
			
			if (!ServicesUtil.isEmpty(locations)) {

				String commonQuery = "select dc.ID, dc.TYPE,dc.WELL, dc.FACILITY, dc.DOWNTIME_CODE,dc.CHILD_CODE, dc.CREATED_AT,"
						+ " dc.CREATED_BY, dc.UPDATED_AT, dc.UPDATED_BY, dc.MUWI,  dc.EQUIPMENT_ID, "
						+ " dc.START_TIME,  dc.END_TIME,  dc.STATUS, dc.DURATION_MINUTE, "
						+ " dc.DURATION_MINUTE_ROC ,dc.DOWNTIME_TEXT,dc.CHILD_TEXT from Downtime_capture dc ";
				String locationQuery = " where dc.muwi in ";
				String statusQuery = " and dc.status= ";
				String countryCodeQuery = " and dc.COUNTRY_CODE= '" + dto.getCountryCode() + "'";
				
				// Soc : Date time in UTC earlier in CST
				String timeQuery = null;
				if (MurphyConstant.EFS_CODE.equalsIgnoreCase(dto.getCountryCode()) || MurphyConstant.CST_ZONE.equalsIgnoreCase(dto.getUserTimeZone())) {
					
					timeQuery = " and dc.created_At between '"
							+ ServicesUtil.convertFromZoneToZoneString(null, currentDateInString+" 07:00:00", MurphyConstant.CST_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD)
							+ "' AND '" + ServicesUtil.convertFromZoneToZoneString(null, nextDateInString+" 06:59:59",
									MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD)
							+ "' ";
					
//					timeQuery = " and dc.created_At between '"
//					+ ServicesUtil.convertFromZoneToZoneString(currentDate, null, MurphyConstant.CST_ZONE,
//							MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
//					+ " 12:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null,
//							MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
//					+ " 11:59:59' ";
					
					
					
				} else if(ServicesUtil.isEmpty(dto.getUserTimeZone()) || MurphyConstant.MST_ZONE.equalsIgnoreCase(dto.getUserTimeZone())){
					
					timeQuery = " and dc.created_At between '"
							+ ServicesUtil.convertFromZoneToZoneString(null, currentDateInString+" 08:00:00", MurphyConstant.MST_ZONE,
									MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD)
							+ "' AND '" + ServicesUtil.convertFromZoneToZoneString(null, nextDateInString+" 07:59:59",
									MurphyConstant.MST_ZONE, MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD)
							+ "' ";
					
					
//					timeQuery = " and dc.created_At between '"
//					+ ServicesUtil.convertFromZoneToZoneString(currentDate, null, MurphyConstant.MST_ZONE,
//							MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
//					+ " 14:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null,
//							MurphyConstant.MST_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
//					+ " 13:59:59' ";
					
					
				}else if(MurphyConstant.PST_ZONE.equalsIgnoreCase(dto.getUserTimeZone())){
					
						timeQuery = " and dc.created_At between '"
								+ ServicesUtil.convertFromZoneToZoneString(null,  currentDateInString+" 08:00:00", MurphyConstant.PST_ZONE,
										MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD)
								+ "' AND '" + ServicesUtil.convertFromZoneToZoneString(null,  nextDateInString+" 07:59:59",
										MurphyConstant.PST_ZONE, MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DB_FORMATE_SD, MurphyConstant.DATE_DB_FORMATE_SD)
								+ "' ";
						
//						timeQuery = " and dc.created_At between '"
//						+ ServicesUtil.convertFromZoneToZoneString(currentDate, null, MurphyConstant.MST_ZONE,
//								MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
//						+ " 14:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null,
//								MurphyConstant.MST_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
//						+ " 13:59:59' ";
						
						
					}
				
				// EOC : Date time in UTC earlier in CST
				String endQuery = "order by dc.created_at desc";

				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_DESIGNATED)) {
					statusQuery = statusQuery + "'" + MurphyConstant.DOWNTIME_PENDING + "' ";
					String cygnetQuery = "UPDATE DC SET DC.DURATION_MINUTE= (CASE WHEN (DC.POINT_ID=AL.POINT_ID ) THEN (CASE WHEN ((AL.END_TIME IS NULL) OR "
							+ "(AL.END_TIME ='')) THEN (CASE WHEN DC.START_TIME BETWEEN TO_VARCHAR(AL.START_TIME) "
							+ "AND '"+recomCygnetDwnTmeDate+"' THEN (SECONDS_BETWEEN(AL.START_TIME,'"+currentRecomTime+"')/60) ELSE(0) END) "
							+ "ELSE ( CASE WHEN DC.START_TIME BETWEEN TO_VARCHAR(AL.START_TIME) "
							+ "AND '"+recomCygnetDwnTmeDate+"' THEN ((AL.CYGNET_RECOM_DURATION)/60000) ELSE(0) END ) END ) ELSE(0) END) "
							+ "FROM DOWNTIME_CAPTURE DC JOIN CYGNET_RECOM_DOWNTIME AL ON DC.POINT_ID=AL.POINT_ID WHERE DC.STATUS = 'PENDING'";
					
					logger.error("[Murphy][DowntimeCapturedDao][getDowntime][Update][cygnetDate]"
							+ ServicesUtil.resultDateAsStringDowntime(currentDate) + " [cygnetQuery]" + cygnetQuery);

					this.getSession().createSQLQuery(cygnetQuery).executeUpdate();
				}

				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_SUBMITTED)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (!ServicesUtil.isEmpty(locations)) {
					locationQuery += " (" + locations + ")";
				}

				String queryString = commonQuery + locationQuery + statusQuery + countryCodeQuery;

				if (!dto.getStatusType().equals(MurphyConstant.DOWNTIME_DESIGNATED))
					queryString += timeQuery;

				queryString += endQuery;

				// Location History
				if (!ServicesUtil.isEmpty(dto.getMonthTime())) {
					if (dto.getMonthTime().equalsIgnoreCase("one"))
						cal.add(Calendar.DATE, -30);
					else if (dto.getMonthTime().equalsIgnoreCase("three"))
						cal.add(Calendar.DATE, -90);

					// SOC : Rework of Downtime Location History - From ProCount
					String fromOriginaldate = sdf.format(cal.getTime());
					String toOriginalDate = sdf.format(currentCal.getTime());
					DowntimeCaptureLocal downtimeLoc = new DowntimeCapture();
					DowntimeCaptureFetchResponseDto dcFetchRespDto = new DowntimeCaptureFetchResponseDto();
					List<String> muwiIDList = locDao.getMuwiUsingByLocCodeProCount(dtoList, dto.getLocationType());
					if (!ServicesUtil.isEmpty(muwiIDList) && muwiIDList.size() > 0) {
						dcFetchRespDto = downtimeLoc.fetchRecordForProvidedUwiIds(muwiIDList, fromOriginaldate,
								toOriginalDate, dto.getPage(), dto.getPage_size(), false);
						countResult = Integer.valueOf(dcFetchRespDto.getTotalCount());
						logger.error("countResult : " + countResult);
						logger.error("list dcFetchRespDto.getDcDtoList() : " + dcFetchRespDto.getDcDtoList());
					}
					feedDtos = new ArrayList<DowntimeCapturedDto>();
					for (DowntimeCaptureDto d : dcFetchRespDto.getDcDtoList()) {
						proDcDto = new DowntimeCapturedDto();
						proDcDto.setChildText(ServicesUtil.isEmpty(getChildDescFromCode(d.getChildCode())) ? null
								: getChildDescFromCode(d.getChildCode()));
						proDcDto.setChildCode(Integer.parseInt((d.getChildCode())));
						proDcDto.setDurationByRocHour(d.getDurationInHours());
						proDcDto.setDurationByRocMinute(d.getDurationInMinutes());
						proDcDto.setCreatedAt(ServicesUtil.isEmpty(d.getStartDate()) ? null
								: ServicesUtil.resultDateAsStringDowntime(d.getStartDate()));
						proDcDto.setMerrickId(ServicesUtil.isEmpty(d.getMerrickId()) ? null : d.getMerrickId());
						proDcDto.setType("Well");
						proDcDto.setWell(ServicesUtil.isEmpty(d.getMerrickId()) ? null
								: fetchWellNameForProvidedMerrick(d.getMerrickId()));

						logger.error("First from ProCount " + proDcDto.getCreatedAt());
						// For Date Format
						DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
						// formatterIST.setTimeZone(TimeZone.getTimeZone("UTC"));
						Date date = formatterIST.parse(proDcDto.getCreatedAt());
						// in milliseconds
						// For Mobile
						proDcDto.setInMilliSec(String.valueOf((date.getTime())));
						proDcDto.setCreatedAt(String.valueOf((date.getTime())));
						// DateFormat formatterUTC = new
						// SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
						// formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
						// // UTC timezone
						// For Web
						proDcDto.setCreatedAtUTC(proDcDto.getCreatedAt());
						feedDtos.add(proDcDto);

					}
					dtDto.setDtoList(feedDtos);
					// EOC : Rework of Downtime Location History - From ProCount

				} else {
					logger.error("[Murphy][DowntimeCapturedDao][getDowntime][DTmeQuery]" + queryString);
					Query q = this.getSession().createSQLQuery(queryString);
					List<Object[]> resultList = q.list();
					if (!ServicesUtil.isEmpty(resultList)) {
						feedDtos = new ArrayList<DowntimeCapturedDto>();
						for (Object[] obj : resultList) {
							feedDtos.add(getDowntimeDto(obj));
						}
					}
					dtDto.setDtoList(feedDtos);
				}

				lht.setCount(new BigDecimal(countResult));
				lht.setStatus("WELL");
				wellCountList.add(lht);
				dtDto.setPageCount(new BigDecimal(dto.getPage_size()));
				dtDto.setTotalCount(new BigDecimal(countResult));
				dtDto.setItemList(wellCountList);
			}

		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedDao][getDowntime][error]" + e.getMessage());
		}

		return dtDto;
	}

	public DowntimeCapturedDto getDowntimeDto(Object[] obj) {
		DowntimeCapturedDto feedDto = new DowntimeCapturedDto();
		try {
			feedDto.setId((String) obj[0]);
			feedDto.setType(ServicesUtil.isEmpty((String) obj[1]) ? "Well" : (String) obj[1]);
			feedDto.setWell((String) obj[2]);
			feedDto.setDowntimeCode((Integer) obj[4]);
			feedDto.setChildCode((Integer) obj[5]);
			feedDto.setCreatedAt(ServicesUtil.isEmpty((Date) obj[6]) ? null
					: ServicesUtil.resultDateAsStringDowntime((Date) obj[6]));
			logger.error("Line 477 Created at : " + feedDto.getCreatedAt());

			DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
			Date date = formatterIST.parse(feedDto.getCreatedAt());
			logger.error("In Millisec:  " + String.valueOf((date.getTime())) + " [ after  Date : ]: " + date);
			feedDto.setInMilliSec(String.valueOf((date.getTime())));
			feedDto.setCreatedAt(String.valueOf((date.getTime())));
			feedDto.setCreatedAtUTC(ServicesUtil.isEmpty((Date) obj[6]) ? null
					: ServicesUtil.resultDateAsStringDowntime((Date) obj[6]));

			feedDto.setCreatedBy((String) obj[7]);
			feedDto.setUpdatedAt(ServicesUtil.isEmpty((Date) obj[8]) ? null
					: ServicesUtil.resultDateAsStringDowntime((Date) obj[8]));
			feedDto.setUpdatedBy((String) obj[9]);
			feedDto.setMuwi((String) obj[10]);
			feedDto.setStatus((String) obj[14]);
			feedDto.setDowntimeText((String) obj[17]);
			feedDto.setChildText((String) obj[18]);
			if (!ServicesUtil.isEmpty((Integer) obj[16])) {
				int rocMinute = (Integer) obj[16];
				int rocHour = rocMinute / 60;
				rocMinute = rocMinute - (rocHour * 60);
				feedDto.setDurationByRocMinute(rocMinute);
				feedDto.setDurationByRocHour(rocHour);
			} else {
				feedDto.setDurationByRocMinute(0);
				feedDto.setDurationByRocHour(0);
			}
			if (!ServicesUtil.isEmpty((Integer) obj[15])) {
				int rocMinute = (Integer) obj[15];
				int rocHour = rocMinute / 60;
				rocMinute = rocMinute - (rocHour * 60);
				feedDto.setDurationByCygnateMinute(rocMinute);
				feedDto.setDurationByCygnateHours(rocHour);
			} else {
				feedDto.setDurationByCygnateMinute(0);
				feedDto.setDurationByCygnateHours(0);
			}
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedDao][getDowntimeDto][error]" + e.getMessage());
		}
		return feedDto;
	}

	@SuppressWarnings("unchecked")
	public String getDuration(DowntimeCapturedDto dto, String countryCode) {

		String date = dto.getCreatedAt();
		Date todayDate = null, nextDate = null;
		Boolean time = false;
		String durationQuery = null;
		try {
			Date dateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

			time = timeBetweenOrNotInUTCZone(dateNew, countryCode);

			if (time.equals(true)) {
				todayDate = ServicesUtil.getPrevDate(dateNew);
				nextDate = dateNew;
			}

			else {
				todayDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				nextDate = ServicesUtil.getNextDate(todayDate);
			}
		} catch (ParseException e) {
			logger.error("[DowntimeCaptureDao][getDuration]Exception while Calculating Duration" + e.getMessage());
			e.printStackTrace();
		}

		if (MurphyConstant.CA_CODE.equalsIgnoreCase(dto.getCountryCode())) {
			durationQuery = "select cast(sum(duration_minute_roc) as VARCHAR(10))  AS DURATION from downtime_capture dc where muwi='"
					+ dto.getMuwi() + "' " + "and created_at between ('"
					+ ServicesUtil.convertFromZoneToZoneString(todayDate, null, "", "", "",
							MurphyConstant.DATE_STANDARD)
					+ " 14:00:00') and ('"
					+ ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "", MurphyConstant.DATE_STANDARD)
					+ " 13:59:59')";
		} else {
			durationQuery = "select cast(sum(duration_minute_roc) as VARCHAR(10))  AS DURATION from downtime_capture dc where muwi='"
					+ dto.getMuwi() + "' " + "and created_at between ('"
					+ ServicesUtil.convertFromZoneToZoneString(todayDate, null, "", "", "",
							MurphyConstant.DATE_STANDARD)
					+ " 12:00:00') and ('"
					+ ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "", MurphyConstant.DATE_STANDARD)
					+ " 11:59:59')";
		}

		logger.error("Duration Query printed" + durationQuery);
		// logger.error("[Murphy][DowntimeCapturedDao][][getTotalDurationFor
		// ProCount][query]" + durationQuery
		// + "todayDate : " + todayDate + "nextDate : " + nextDate);
		Query q = this.getSession().createSQLQuery(durationQuery);
		List<String> obj = q.list();
		if (!ServicesUtil.isEmpty(obj)) {
			// logger.error("[Murphy][DowntimeCapturedDao][][getTotalDurationFor
			// ProCount]" + obj.get(0));
			return obj.get(0);

		}
		return null;
	}

	public Boolean timeBetweenOrNotInUTCZone(Date date, String countryCode) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date0 = null;
		String date1 = null;
		String date2 = null;

		if (MurphyConstant.EFS_CODE.equalsIgnoreCase(countryCode)) {

			// date1 and date2 are UTC times for EFS ,CST timing of 12AM-7AM
			date0 = df.format(date);
//			date1 = "05:00:00";
//			date2 = "11:59:59";
			date1 = "06:00:00";
			date2 = "12:59:59";
		} else {

			// date1 and date2 are in UTC for Canada MST timing of 12AM -8AM
			date0 = df.format(date);
//			date1 = "06:00:00";
//			date2 = "13:59:59";
			date1 = "07:00:00";
			date2 = "14:59:59";
		}
		// logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][DATE to be
		// checked]" + date0 + " intial DATE " + date1
		// + " final DATE " + date2);
		DateFormat newDf = new SimpleDateFormat("HH:mm:ss");

		Date time0 = null, time1 = null, time2 = null;

		try {
			time0 = newDf.parse(date0);
			time1 = newDf.parse(date1);
			time2 = newDf.parse(date2);

			// logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][TIME to be
			// checked]" + time0 + " intial TIME " + time1
			// + " final TIME " + time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (!ServicesUtil.isEmpty(time0) && !ServicesUtil.isEmpty(time1) && !ServicesUtil.isEmpty(time1)) {
			if (time0.after(time1) && time0.before(time2)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	// public Boolean timeBetweenOrNotForCSTZone(Date date, String location) {
	//
	// DateFormat df = new SimpleDateFormat("HH:mm:ss");
	// String date0=null;
	// String date1=null;
	// String date2=null;
	// if (MurphyConstant.EFS_CODE.equalsIgnoreCase(location)) {
	// date0 = df.format(date);
	// date1 = "00:00:00";
	// date2 = "06:59:59";
	// } else {
	// date0 = df.format(date);
	// date1 = "00:00:00";
	// date2 = "08:59:59";
	// }
	// // logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][DATE to be
	// // checked]" + date0 + " intial DATE " + date1
	// // + " final DATE " + date2);
	// DateFormat newDf = new SimpleDateFormat("HH:mm:ss");
	//
	// Date time0 = null, time1 = null, time2 = null;
	//
	// try {
	// time0 = newDf.parse(date0);
	// time1 = newDf.parse(date1);
	// time2 = newDf.parse(date2);
	//
	// // logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][TIME to be
	// // checked]" + time0 + " intial TIME " + time1
	// // + " final TIME " + time2);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// if (!ServicesUtil.isEmpty(time0) && !ServicesUtil.isEmpty(time1) &&
	// !ServicesUtil.isEmpty(time1)) {
	// if (time0.after(time1) && time0.before(time2)) {
	// return true;
	// } else
	// return false;
	// }
	// return false;
	// }

	public Boolean timeBetweenOrNot(Date date, String location) {

		// date1 and date2 are in UTC for Canada 12AM -8PM
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date0 = null;
		String date1 = null;
		String date2 = null;
		if (MurphyConstant.EFS_CODE.equalsIgnoreCase(location)) {
			date0 = df.format(date);
			date1 = "00:00:00";
			date2 = "06:59:59";
		} else {

			date0 = df.format(date);
			date1 = "00:00:00";
			date2 = "07:59:59";
		}
		// logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][DATE to be
		// checked]" + date0 + " intial DATE " + date1
		// + " final DATE " + date2);
		DateFormat newDf = new SimpleDateFormat("HH:mm:ss");

		Date time0 = null, time1 = null, time2 = null;

		try {
			time0 = newDf.parse(date0);
			time1 = newDf.parse(date1);
			time2 = newDf.parse(date2);

			// logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][TIME to be
			// checked]" + time0 + " intial TIME " + time1
			// + " final TIME " + time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (!ServicesUtil.isEmpty(time0) && !ServicesUtil.isEmpty(time1) && !ServicesUtil.isEmpty(time1)) {
			if (time0.after(time1) && time0.before(time2)) {
				return true;
			} else
				return false;
		}
		return false;
	}

	// For Well Downtime Child Code description
	@SuppressWarnings("unchecked")
	public String getChildDescFromCode(String childCode) {
		String childCodeDesc = null;
		try {
			String query = "select CHILD_CODE_DESCRIPTION from  DT_WELL_CHILD_CODE where CHILD_CODE in ('" + childCode
					+ "')";
			Object obj = this.getSession().createSQLQuery(query).uniqueResult();
			if (!ServicesUtil.isEmpty(obj))
				childCodeDesc = obj.toString();
		} catch (Exception e) {
			logger.error("[getChildDescFromCode][childCodeDesc][error]" + e.getMessage());
		}
		logger.error("childCodeDesc : " + childCodeDesc);
		return childCodeDesc;
	}

	public String fetchWellNameForProvidedMerrick(int merrickID) {
		Connection connection = DBConnections.createConnectionForProcount();
		String wellName = null;

		if (connection != null) {
			try {
				logger.error("[fetchWellNameForProvidedMerrick] : INFO- Connection to DB successful");
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				try {
					String query = "select WellName from [DBO].[CompletionTb] where MerrickID " + " in ( " + merrickID
							+ " )";
					logger.error("[fetchWellNameForProvidedMerrick][query]" + query);
					stmt = connection.prepareStatement(query);
					resultSet = stmt.executeQuery();

					if (resultSet != null) {
						boolean hasNext = resultSet.next();
						while (hasNext) {
							wellName = resultSet.getString("WellName");
							hasNext = resultSet.next();
						}
					}
				} catch (Exception e) {
					logger.error(
							"[Murphy][DowntimeCapturedDao][fetchWellNameForProvidedMerrick][error]" + e.getMessage());
				} finally {
					try {
						stmt.close();
						resultSet.close();
					} catch (SQLException e) {
						logger.error("[fetchWellNameForProvidedMerrick] : ERROR- Exception while cleaning environment"
								+ e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("[fetchWellNameForProvidedMerrick] : ERROR- Exception while interacting with database "
						+ e.getMessage());
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("[fetchWellNameForProvidedMerrick] : ERROR- Exception while closing Connection " + e);
				}
			}
		}
		logger.error("[fetchWellNameForProvidedMerrick][wellName]" + wellName);
		return wellName;
	}

	public List<SSVCloseResponseDto> fetchSSVCloseDetails(HandoverNotesDto handOverDto){
		List<SSVCloseResponseDto> ssvCloseList = new ArrayList<>();
		SSVCloseResponseDto ssvCloseResponeDto=null;
		String fetchQuery = null;
		Boolean time = false;
		Date nextDate = null;
		Date currentDate = null;
		String currentDateInString=null;
		String nextDateInString=null;
		try {

			if(!ServicesUtil.isEmpty(handOverDto)){
				
				String locationCode = productionLocationDao.getLocationCodeByLocationtext(handOverDto.getField());
                locationCode=ServicesUtil.isEmpty(locationCode) ? "" :locationCode.substring(0,15);
				
			if (handOverDto.getField().equalsIgnoreCase("Kaybob")
					|| handOverDto.getField().equalsIgnoreCase("Montney")) {

				String dateStr=ServicesUtil.convertFromZoneToZoneString(null,handOverDto.getDate(),MurphyConstant.UTC_ZONE,
						MurphyConstant.MST_ZONE,MurphyConstant.DATE_STANDARD,MurphyConstant.DATE_DB_FORMATE_SD);
				currentDate = ServicesUtil.resultAsDateDowntime(dateStr);
				nextDate = ServicesUtil.getNextDate(currentDate);
				time = timeBetweenOrNot(currentDate, MurphyConstant.CA_CODE);

				if (time.equals(true)) {
					nextDate = currentDate;
					currentDate = ServicesUtil.getPrevDate(currentDate);
				}

				if(handOverDto.getShift().equalsIgnoreCase("Day")){
					fetchQuery = "SELECT WELL,DOWNTIME_TEXT,CHILD_TEXT,CREATED_AT,DURATION_MINUTE_ROC FROM DOWNTIME_CAPTURE WHERE CREATED_BY='"+handOverDto.getUserId()+"' "
							+ "AND created_at between ('"+ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", "", "",
									MurphyConstant.DATE_STANDARD)+" 14:00:00') and ('"+ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "",
											MurphyConstant.DATE_STANDARD)+" 01:59:59') ";
				}else{
					fetchQuery = "SELECT WELL,DOWNTIME_TEXT,CHILD_TEXT,CREATED_AT,DURATION_MINUTE_ROC FROM DOWNTIME_CAPTURE WHERE CREATED_BY='"+handOverDto.getUserId()+"' "
							+ "AND created_at between ('"+ServicesUtil.convertFromZoneToZoneString(currentDate, null, "", "", "",
									MurphyConstant.DATE_STANDARD)+" 02:00:00') and ('"+ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "",
											MurphyConstant.DATE_STANDARD)+" 13:59:59') ";
				}
			}else{
			    
//				String dateStr=ServicesUtil.convertFromZoneToZoneString(null,handOverDto.getDate(),MurphyConstant.UTC_ZONE,
//						MurphyConstant.CST_ZONE,MurphyConstant.DATE_STANDARD,MurphyConstant.DATE_DB_FORMATE_SD);
//				currentDate = ServicesUtil.resultAsDateDowntime(dateStr);
//				nextDate = ServicesUtil.getNextDate(currentDate);
//				time = timeBetweenOrNot(currentDate, MurphyConstant.EFS_CODE);
//
//				if (time.equals(true)) {
//					nextDate = currentDate;
//					currentDate = ServicesUtil.getPrevDate(currentDate);
//				}
				
				//Find Current and Next Day By Handover Created Date
				currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(handOverDto.getDate());
			    nextDate = ServicesUtil.getNextDate(currentDate);

				currentDateInString = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
				nextDateInString = new SimpleDateFormat("yyyy-MM-dd").format(nextDate);
				if(handOverDto.getShift().equalsIgnoreCase("Day")){
					
					
					fetchQuery = "SELECT dc.WELL,dc.DOWNTIME_TEXT,dc.CHILD_TEXT,dc.CREATED_AT,"
							+ "dc.DURATION_MINUTE_ROC FROM DOWNTIME_CAPTURE DC JOIN WELL_MUWI wm "
							+ "ON wm.MUWI=DC.MUWI WHERE dc.CREATED_BY='"+handOverDto.getUserId()+"' AND COUNTRY_CODE='"+MurphyConstant.EFS_CODE+"' "
							+ "AND created_at between ('"+ServicesUtil.convertFromZoneToZoneString(null,currentDateInString+" 07:00:00",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
									MurphyConstant.DATE_DB_FORMATE_SD)+"') and ('"+ServicesUtil.convertFromZoneToZoneString(null,currentDateInString+" 18:59:59",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
											MurphyConstant.DATE_DB_FORMATE_SD)+"') AND wm.LOCATION_CODE LIKE'"+locationCode+"%'";
				}else{
					fetchQuery = "SELECT dc.WELL,dc.DOWNTIME_TEXT,dc.CHILD_TEXT,dc.CREATED_AT,"
							+ "dc.DURATION_MINUTE_ROC FROM DOWNTIME_CAPTURE DC JOIN WELL_MUWI wm "
							+ "ON wm.MUWI=DC.MUWI WHERE dc.CREATED_BY='"+handOverDto.getUserId()+"' AND COUNTRY_CODE='"+MurphyConstant.EFS_CODE+"' "
							+ "AND created_at between ('"+ServicesUtil.convertFromZoneToZoneString(null,currentDateInString+" 19:00:00",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
									MurphyConstant.DATE_DB_FORMATE_SD)+"') and ('"+ServicesUtil.convertFromZoneToZoneString(null,nextDateInString+" 06:59:59",MurphyConstant.CST_ZONE,MurphyConstant.UTC_ZONE,MurphyConstant.DATE_DB_FORMATE_SD,
											MurphyConstant.DATE_DB_FORMATE_SD)+"') AND wm.LOCATION_CODE LIKE'"+locationCode+"%'";
				}
			}

			logger.error("FetchQuery"+fetchQuery);
			Query q = this.getSession().createSQLQuery(fetchQuery);
			List<Object[]> resultList = q.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				ssvCloseResponeDto=new SSVCloseResponseDto();
				int rocMinute=0;
				int rocHour=0;
				for (Object[] obj : resultList) {
					ssvCloseResponeDto=new SSVCloseResponseDto();
					rocMinute=ServicesUtil.isEmpty((int) obj[4]) ? 0 : (int) obj[4];
					if (rocMinute!=0) {
					    rocHour = rocMinute / 60;
						rocMinute = rocMinute - (rocHour * 60);
					}

					ssvCloseResponeDto.setWellName(ServicesUtil.isEmpty((String) obj[0]) ? null : (String) obj[0]);
					ssvCloseResponeDto.setParentCode(ServicesUtil.isEmpty((String) obj[1]) ? null : (String) obj[1]);
					ssvCloseResponeDto.setChildCode(ServicesUtil.isEmpty((String) obj[2]) ? null : (String) obj[2]);
					ssvCloseResponeDto.setStartDate(ServicesUtil.isEmpty((Date) obj[3]) ? null : (Date) obj[3]);
					ssvCloseResponeDto.setDownTimeInHours(rocHour);
					ssvCloseResponeDto.setDownTimeMinutes(rocMinute);
					ssvCloseList.add(ssvCloseResponeDto);

					}
			}
			}
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedDao][fetchSSVCloseDetails]" + e.getMessage());
		}
		return ssvCloseList;

	}
	
	
	//added for location history for downtime of canada muwis
	@SuppressWarnings({ "unchecked", "unused" })
	public DowntimeResponseDto getLocationHistoryForDowntimeInCanda(DowntimeRequestDto dto) {
		logger.error("[Murphy][DowntimeCaptureDao][getLocationHistoryForDowntimeInCanda]Method Started");
		List<LocationHierarchyDto> dtoList = dto.getLocationHierarchy();
		List<DowntimeCapturedDto> feedDtos = null;
		DowntimeCapturedDto proDcDto = null;
		String locationType = dto.getLocationType();
		String locations = locDao.getLocationUsinglocationType(dtoList, locationType);
		// Location History
		DowntimeResponseDto dtDto = new DowntimeResponseDto();
		List<LocationHistoryRolledUpDto> wellCountList = new ArrayList<LocationHistoryRolledUpDto>();
		LocationHistoryRolledUpDto lht = new LocationHistoryRolledUpDto();
		Query query = null;
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (MurphyConstant.EFS_CODE.equalsIgnoreCase(dto.getCountryCode())) {
				format1.setTimeZone(TimeZone.getTimeZone("CST"));
			} else {
				format1.setTimeZone(TimeZone.getTimeZone("MST"));
			}
			String dateStr = format1.format(new Date());
			
			Date currentDate = ServicesUtil.resultAsDateDowntime(dateStr);
			Date nextDate = ServicesUtil.getNextDate(currentDate);
			Boolean time = timeBetweenOrNot(currentDate, dto.getCountryCode());

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			Calendar currentCal = Calendar.getInstance();
			Integer countResult = 0;

			if (time.equals(true)) {
				nextDate = currentDate;
				currentDate = ServicesUtil.getPrevDate(currentDate);
			}

			if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
				nextDate = currentDate;
				currentDate = ServicesUtil.getPrevDate(currentDate);
			}
			if (!ServicesUtil.isEmpty(locations)) {
				String statusQuery ="";
				String commonQuery = "select dc.ID, dc.TYPE,dc.WELL, dc.FACILITY, dc.DOWNTIME_CODE,dc.CHILD_CODE, dc.CREATED_AT,"
						+ " dc.CREATED_BY, dc.UPDATED_AT, dc.UPDATED_BY, dc.MUWI,  dc.EQUIPMENT_ID, "
						+ " dc.START_TIME,  dc.END_TIME,  dc.STATUS, dc.DURATION_MINUTE, "
						+ " dc.DURATION_MINUTE_ROC ,dc.DOWNTIME_TEXT,dc.CHILD_TEXT from Downtime_capture dc ";
				String locationQuery = " where dc.muwi in ";
				if(dto.getStatusType().equalsIgnoreCase(MurphyConstant.DOWNTIME_DESIGNATED))
				{
				statusQuery = " and dc.status NOT IN ";
				}
				else{
					statusQuery = " and dc.status= ";
				}
				String countryCodeQuery = " and dc.COUNTRY_CODE= '" + dto.getCountryCode() + "'";
				
				// Soc : Date time in UTC earlier in CST
				String timeQuery = null;
				if (MurphyConstant.EFS_CODE.equalsIgnoreCase(dto.getCountryCode())) {
					timeQuery = " and dc.created_At between '"
							+ ServicesUtil.convertFromZoneToZoneString(currentDate, null, MurphyConstant.CST_ZONE,
									MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
							+ " 12:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null,
									MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
							+ " 11:59:59' ";
				} else {
				
					if (!ServicesUtil.isEmpty(dto.getMonthTime())) {
						if (dto.getMonthTime().equalsIgnoreCase("one"))
							cal.add(Calendar.DATE, -30);
						else if (dto.getMonthTime().equalsIgnoreCase("three"))
							cal.add(Calendar.DATE, -90);
						String fromOriginaldate = sdf.format(cal.getTime());
						String toOriginalDate = sdf.format(currentCal.getTime());
 						Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromOriginaldate);
						Date date2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toOriginalDate);
					timeQuery = " and dc.created_At between '"
							+ ServicesUtil.convertFromZoneToZoneString(date1, null, MurphyConstant.MST_ZONE,
									MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
							+ " 14:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(date2, null,
									MurphyConstant.MST_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
							+ " 13:59:59' ";
					}
					else{
						timeQuery = " and dc.created_At between '"
								+ ServicesUtil.convertFromZoneToZoneString(currentDate, null, MurphyConstant.MST_ZONE,
										MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
								+ " 14:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null,
										MurphyConstant.MST_ZONE, MurphyConstant.UTC_ZONE, "", MurphyConstant.DATE_STANDARD)
								+ " 13:59:59' ";
					}
				}
				// EOC : Date time in UTC earlier in CST
				String endQuery = "order by dc.created_at desc";

				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_DESIGNATED)) {
					statusQuery = statusQuery + "('" + MurphyConstant.DOWNTIME_PENDING + "')";
					// updating the cygnate time in case of EFS location history not required for canada as we need only for submitted and reviwed
//					String cygnetQuery = "UPDATE DC SET DC.DURATION_MINUTE= (CASE WHEN ((DC.POINT_ID=AL.POINT_ID)"
//							+ " AND TO_VARCHAR(DC.START_TIME,'yyyy-MM-dd HH24:MI:SS') = TO_VARCHAR(AL.START_TIME,'yyyy-MM-dd HH24:MI:SS')) THEN (CASE WHEN ((AL.END_TIME IS NULL) OR (AL.END_TIME ='')) "
//							+ "THEN (SECONDS_BETWEEN(AL.START_TIME,'"
//							+ ServicesUtil.resultDateAsStringDowntimeCygnate(currentDate)
//							+ "')/60) ELSE ((AL.CYGNET_RECOM_DURATION)/60000) END) ELSE (0) END ) FROM DOWNTIME_CAPTURE DC JOIN CYGNET_RECOM_DOWNTIME AL ON DC.POINT_ID=AL.POINT_ID AND to_varchar(dc.start_time,'yyyy-MM-dd HH24:MI:SS') = to_varchar(al.start_time,'yyyy-MM-dd HH24:MI:SS') WHERE DC.STATUS = 'PENDING'";
//					logger.error("[Murphy][DowntimeCapturedDao][getDowntime][Update][cygnetDate]"
//							+ ServicesUtil.resultDateAsStringDowntime(currentDate) + " [cygnetQuery]" + cygnetQuery);
//
//					this.getSession().createSQLQuery(cygnetQuery).executeUpdate();
				}

				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_SUBMITTED)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (!ServicesUtil.isEmpty(locations)) {
					locationQuery += " (" + locations + ")";
				}

				String queryString = commonQuery + locationQuery + statusQuery + countryCodeQuery;

				if (!dto.getStatusType().equals(MurphyConstant.DOWNTIME_DESIGNATED))
					queryString += timeQuery;
				
				
				String paginationQuery = " ";
				if(dto.getPage() > 0){
					int first = (dto.getPage() - 1) * dto.getPage_size();
					int last = dto.getPage_size();
					//paginationQuery += " OFFSET " + first + " ROWS FETCH NEXT "+ last +" ROWS ONLY";
					paginationQuery+="limit "+last+" offset "+first+" ";
					
				}
				endQuery+=paginationQuery;

				queryString += endQuery;

				
					logger.error("[Murphy][DowntimeCapturedDao][getLocationHistoryForDowntimeInCanda][DTmeQuery]" + queryString);
					Query q = this.getSession().createSQLQuery(queryString);
					List<Object[]> resultList = q.list();
					if (!ServicesUtil.isEmpty(resultList)) {
						feedDtos = new ArrayList<DowntimeCapturedDto>();
						for (Object[] obj : resultList) {
							feedDtos.add(getDowntimeDto(obj));
						}
					}
					dtDto.setDtoList(feedDtos);
				
				countResult = feedDtos.size();
				lht.setCount(new BigDecimal(countResult));
				lht.setStatus("WELL");
				wellCountList.add(lht);
				dtDto.setPageCount(new BigDecimal(dto.getPage_size()));
				dtDto.setTotalCount(new BigDecimal(countResult));
				dtDto.setItemList(wellCountList);
			}

		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedDao][getLocationHistoryForDowntimeInCanda][error]" + e.getMessage());
		}

		return dtDto;
	}

}