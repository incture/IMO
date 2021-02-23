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

import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.service.DowntimeCapture;
import com.murphy.integration.util.DBConnections;
import com.murphy.taskmgmt.dto.CustomLocationHistoryDto;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHistoryRolledUpDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.DowntimeCapturedDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("compressorDowntimeDao")
public class CompressorDowntimeDao extends BaseDao<DowntimeCapturedDo, DowntimeCapturedDto> {

	private static final Logger logger = LoggerFactory.getLogger(CompressorDowntimeDao.class);

	public CompressorDowntimeDao() {
	}

	@Autowired
	HierarchyDao locDao;

	@Autowired
	CygnetAlarmFeedDao alarmDao;

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

		return dto;
	}

	@SuppressWarnings("unchecked")
	public ResponseMessage createCompressorDowntime(DowntimeUpdateDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		Boolean proCountValue = dto.getIsProCountUpdate();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Downtime " + MurphyConstant.CREATE_FAILURE);
		DowntimeCapturedDto capturedDto = dto.getDto();
		try {
			if (dto.getIsProCountUpdate().equals(true)) {
				if (!ServicesUtil.isEmpty(capturedDto.getDurationByRocMinute())
						|| !ServicesUtil.isEmpty(capturedDto.getDurationByRocHour()))
					capturedDto.setStatus(MurphyConstant.DOWNTIME_COMPLETE);
				else
					capturedDto.setStatus(MurphyConstant.DOWNTIME_PENDING);
			} else if (dto.getIsProCountUpdate().equals(false)) {

				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				//SOC: CST to UTC
				format1.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
				String dateStr = format1.format(new Date(capturedDto.getStartTime().getTime()));
				Date cstStartTime = ServicesUtil.resultAsDateDowntime(dateStr);
				capturedDto.setStartTime(cstStartTime);
				//EOC :  CST to UTC

				capturedDto.setStatus(MurphyConstant.DOWNTIME_PENDING);
			}
			//SOC : Date to be stored in UTC while creating in Hana
			logger.error("CreatedAT time Millisec from UI: "+capturedDto.getCreatedAt());
			long millis= Long.parseLong(capturedDto.getCreatedAt());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(millis);
			capturedDto.setCreatedAt(sdf.format(cal.getTime()));
			capturedDto.setCountryCode(MurphyConstant.EFS_CODE);
			logger.error("[Created AT UTC]:"+ capturedDto.getCreatedAt());
			//EOC
			if (!ServicesUtil.isEmpty(capturedDto.getDurationByRocHour()))
				capturedDto.setDurationByRocMinute((capturedDto.getDurationByRocHour() * 60)
						+ (!ServicesUtil.isEmpty(capturedDto.getDurationByRocMinute())
								? capturedDto.getDurationByRocMinute() : 0));

			if (!ServicesUtil.isEmpty(capturedDto.getMuwi()) && capturedDto.getType().equalsIgnoreCase("Compressor")) {

				String getCompressorQuery = "select p1.location_code, p1.location_text, e1.equipment_text, e1.parent_code, e1.equipment_code, em.merrick_id "
						+ "from production_location p1 left outer join production_equipment e1 on p1.location_code = e1.parent_code "
						+ "left join equipment_merrick em on e1.equipment_code = em.equipment_code  where em.merrick_id ='"
						+ capturedDto.getMuwi() + "'";

				Query q = this.getSession().createSQLQuery(getCompressorQuery);
				List<Object[]> responseList = q.list();
				if (!ServicesUtil.isEmpty(responseList)) {

					for (Object[] obj : responseList) {
						capturedDto.setFacility(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
						capturedDto.setWell(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
						capturedDto.setEquipmentId(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
						// capturedDto.add(compDto);
					}
				}
			}
			create(capturedDto);
			String designateResult = MurphyConstant.SUCCESS;
			// update is_designate filed in alarm table
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
			logger.error("[Murphy][CompressorDowntimeDao][createCompressorDowntime][error]" + e.getMessage());
		}

		return responseDto;
	}

	public String updateCompressorDowntime(DowntimeCapturedDto dto) {
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
		//SOC : Date to be stored in UTC while creating in Hana
		logger.error("[Update] CreatedAT time Millisec from UI: "+dto.getCreatedAt());
		long millis= Long.parseLong(dto.getCreatedAt());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		dto.setCreatedAt(sdf.format(cal.getTime()));
		System.out.println("[Update DT ][Created At UTC]:"+ dto.getCreatedAt());
		logger.error("[Update DT ][Created At UTC]:"+ dto.getCreatedAt());
		//EOC
		
		subQuery = subQuery.substring(0, subQuery.length() - 1);
		String updateQuery = "UPDATE DOWNTIME_CAPTURE dc SET " + subQuery + "  where id= '" + dto.getId() + "' ";
		int updatedRow = this.getSession().createSQLQuery(updateQuery).executeUpdate();
		if (updatedRow > 0) {
			response = MurphyConstant.SUCCESS;
		}
		return response;
	}

	@SuppressWarnings("unchecked") // List<DowntimeCapturedDto>
	public DowntimeResponseDto getCompressorDowntime(DowntimeRequestDto dto) {
		List<LocationHierarchyDto> dtoList = dto.getLocationHierarchy();
		List<DowntimeCapturedDto> feedDtos = null;
		String locationType = dto.getLocationType();
		String locations = locDao.getCompressorLocationUsinglocationType(dtoList, locationType);
		// Location History
		DowntimeResponseDto dtDto = new DowntimeResponseDto();
		List<LocationHistoryRolledUpDto> compressorCountList = new ArrayList<LocationHistoryRolledUpDto>();
		LocationHistoryRolledUpDto lht = new LocationHistoryRolledUpDto();
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format1.setTimeZone(TimeZone.getTimeZone("CST"));
			String dateStr = format1.format(new Date());
			Date cstDate = ServicesUtil.resultAsDateDowntime(dateStr);
			Date nextDate = ServicesUtil.getNextDate(cstDate);
			Boolean time = timeBetweenOrNot(cstDate);

			// Location History
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			Calendar currentCal = Calendar.getInstance();
			Query query = null;
			Integer countResult = 0;
			DowntimeCapturedDto proDcDto = null;

			if (time.equals(true)) {
				nextDate = cstDate;
				cstDate = ServicesUtil.getPrevDate(cstDate);
			}

			if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
				nextDate = cstDate;
				cstDate = ServicesUtil.getPrevDate(cstDate);
			}
			if (!ServicesUtil.isEmpty(locations)) {

				String commonQuery = "select dc.ID, dc.TYPE,dc.WELL, dc.FACILITY, dc.DOWNTIME_CODE,dc.CHILD_CODE, dc.CREATED_AT,"
						+ " dc.CREATED_BY, dc.UPDATED_AT, dc.UPDATED_BY, dc.MUWI,  dc.EQUIPMENT_ID, "
						+ " dc.START_TIME,  dc.END_TIME,  dc.STATUS, dc.DURATION_MINUTE, "
						+ " dc.DURATION_MINUTE_ROC ,dc.DOWNTIME_TEXT,dc.CHILD_TEXT from Downtime_capture dc ";
				String locationQuery = " where dc.muwi in ";
				String statusQuery = " and dc.status= ";
				/*String timeQuery = " and dc.created_At between '"
						+ ServicesUtil.convertFromZoneToZoneString(cstDate, null, "", "", "",
								MurphyConstant.DATE_STANDARD)
						+ " 07:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "",
								MurphyConstant.DATE_STANDARD)
						+ " 06:59:59' ";
				String endQuery = "order by dc.created_at desc"; */
				
				//Soc : Date time in UTC earlier in CST
				String timeQuery = " and dc.created_At between '"
						+ ServicesUtil.convertFromZoneToZoneString(cstDate, null, MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "",
								MurphyConstant.DATE_STANDARD)
						+ " 12:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null, MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "",
								MurphyConstant.DATE_STANDARD)
						+ " 11:59:59' ";
				//EOC : Date time in UTC earlier in CST
				String endQuery = "order by dc.created_at desc";
			
				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_SUBMITTED)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (!ServicesUtil.isEmpty(locations)) {
					locationQuery += " (" + locations + ")";
				}

				String queryString = commonQuery + locationQuery + statusQuery;

				if (!dto.getStatusType().equals(MurphyConstant.DOWNTIME_DESIGNATED))
					queryString += timeQuery;

				queryString += endQuery;
					// Location History
					if (!ServicesUtil.isEmpty(dto.getMonthTime())) {
						if (dto.getMonthTime().equalsIgnoreCase("one"))
							cal.add(Calendar.DATE, -30);
						else if (dto.getMonthTime().equalsIgnoreCase("three"))
							cal.add(Calendar.DATE, -90);
						//SOC : Rework of Downtime Location History - From ProCount
						String fromOriginaldate = sdf.format(cal.getTime());
						String toOriginalDate = sdf.format(currentCal.getTime());
						DowntimeCaptureLocal downtimeLoc = new DowntimeCapture();
						DowntimeCaptureFetchResponseDto dcFetchRespDto = new DowntimeCaptureFetchResponseDto();
						List<String> listOfMerrick = new ArrayList<String>();
						boolean isCompressor = true;
						
						for (LocationHierarchyDto locationDto : dtoList) {
							listOfMerrick.add(locationDto.getMuwi());
						}
						if(!ServicesUtil.isEmpty(listOfMerrick) && listOfMerrick.size() > 0){
							dcFetchRespDto = downtimeLoc.fetchRecordForProvidedUwiIds(listOfMerrick, fromOriginaldate, toOriginalDate, dto.getPage(),
									                                                  dto.getPage_size(),isCompressor);
							countResult = Integer.valueOf( dcFetchRespDto.getTotalCount());
							logger.error("countResult : "+countResult);
							logger.error("list dcFetchRespDto.getDcDtoList() : " + dcFetchRespDto.getDcDtoList());
						}
						feedDtos = new ArrayList<DowntimeCapturedDto>();
						for (DowntimeCaptureDto d : dcFetchRespDto.getDcDtoList()) {
							proDcDto = new DowntimeCapturedDto();
							proDcDto.setChildText(ServicesUtil.isEmpty(getChildDescFromCode(d.getChildCode())) ? null : getChildDescFromCode(d.getChildCode()));
							proDcDto.setChildCode(Integer.parseInt((d.getChildCode())));
							proDcDto.setDurationByRocHour(d.getDurationInHours());
							proDcDto.setDurationByRocMinute(d.getDurationInMinutes());
							proDcDto.setCreatedAt(ServicesUtil.isEmpty(d.getStartDate()) ? null : ServicesUtil.resultDateAsStringDowntime(d.getStartDate()));
							proDcDto.setMerrickId(ServicesUtil.isEmpty(d.getMerrickId()) ? null : d.getMerrickId());
							proDcDto.setType("Compressor");
							proDcDto.setWell(ServicesUtil.isEmpty(d.getMerrickId()) ? null : fetchComNameForProvidedMerrick(d.getMerrickId()));
							
							//For Date Format
							DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");							
							Date date = formatterIST.parse(proDcDto.getCreatedAt());
							logger.error("First from ProCount "+formatterIST.format(date));
							// Changed values to Epoch
							proDcDto.setInMilliSec(String.valueOf((date.getTime())));
							proDcDto.setCreatedAt(String.valueOf((date.getTime())));
							proDcDto.setCreatedAtUTC(String.valueOf((date.getTime())));
							feedDtos.add(proDcDto);
							
						}
						dtDto.setDtoList(feedDtos);
						//EOC : Rework of Downtime Location History - From ProCount
					}
					else {
					logger.error("[Murphy][CompressorDowntimeDao][getCompressorDowntime][Query]" + queryString);
	
					Query q = this.getSession().createSQLQuery(queryString);
					List<Object[]> resultList = q.list();
					if (!ServicesUtil.isEmpty(resultList)) {
						feedDtos = new ArrayList<DowntimeCapturedDto>();
						for (Object[] obj : resultList) {
							feedDtos.add(getDowntimeDto(obj));
						}
					}
			       }
					
			lht.setCount(new BigDecimal(countResult));
			lht.setStatus("COMPRESSOR");
			compressorCountList.add(lht);
			dtDto.setDtoList(feedDtos);
			dtDto.setPageCount(new BigDecimal(dto.getPage_size()));
			dtDto.setTotalCount(new BigDecimal(countResult));
			dtDto.setItemList(compressorCountList);
			}	
		} catch (Exception e) {
			logger.error("[Murphy][CompressorDowntimeDao][getCompressorDowntime][error]" + e.getMessage());
		}
		return dtDto;
	}

	public DowntimeCapturedDto getDowntimeDto(Object[] obj) {
		DowntimeCapturedDto feedDto = new DowntimeCapturedDto();
		try{
			feedDto.setId((String) obj[0]);
			feedDto.setType(ServicesUtil.isEmpty((String) obj[1]) ? "Compressor" : (String) obj[1]);
			feedDto.setWell((String) obj[2]);
			feedDto.setFacility((String) obj[3]);
			feedDto.setDowntimeCode((Integer) obj[4]);
			feedDto.setChildCode((Integer) obj[5]);
			feedDto.setCreatedAt(
					ServicesUtil.isEmpty((Date) obj[6]) ? null : ServicesUtil.resultDateAsStringDowntime((Date) obj[6]));
			
/*//			Note : On deploying in PROD ,change zone to CST and comment Asia/Kolkata
			DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
			//formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			formatterIST.setTimeZone(TimeZone.getTimeZone("CST")); 
			Date date = formatterIST.parse(feedDto.getCreatedAt());

			DateFormat formatterUTC = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
			formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
			
			//For Web
			feedDto.setCreatedAtUTC(formatterUTC.format(date));
			//For Mobile
			feedDto.setInMilliSec(String.valueOf((((Date) obj[6]).getTime()))); */
			
			// Changed to epoch
			DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
			Date date = formatterIST.parse(feedDto.getCreatedAt());
				logger.error("In Millisec:  "+String.valueOf((date.getTime())) + " [ after  Date : ]: "+date);
			feedDto.setInMilliSec(String.valueOf((date.getTime())));
			feedDto.setCreatedAt(String.valueOf((date.getTime())));
			feedDto.setCreatedAtUTC(ServicesUtil.isEmpty((Date) obj[6]) ? null : ServicesUtil.resultDateAsStringDowntime((Date) obj[6]));
			
			feedDto.setCreatedBy((String) obj[7]);
			feedDto.setUpdatedAt(
					ServicesUtil.isEmpty((Date) obj[8]) ? null : ServicesUtil.resultDateAsStringDowntime((Date) obj[8]));
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
		}catch(Exception e){
			logger.error("[Murphy][CompressorDowntimeDao][getCompressorDowntimeDto][error]" + e.getMessage());
		}
		return feedDto;
	}

	@SuppressWarnings("unchecked")
	public String getDuration(DowntimeCapturedDto dto) {

		String date = dto.getCreatedAt();
		Date todayDate = null, nextDate = null;
		try {
			Date dateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			Boolean time = timeBetweenOrNot(dateNew);

			if (time.equals(true)) {
				todayDate = ServicesUtil.getPrevDate(dateNew);
				nextDate = dateNew;
			}

			else {
				todayDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				nextDate = ServicesUtil.getNextDate(todayDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String durationQuery = "select cast(sum(duration_minute_roc) as VARCHAR(10))  AS DURATION from downtime_capture dc where muwi='"
				+ dto.getMuwi() + "' " + "and created_at between ('"
				+ ServicesUtil.convertFromZoneToZoneString(todayDate, null, "", "", "", MurphyConstant.DATE_STANDARD)
				+ " 07:00:00') and ('"
				+ ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "", MurphyConstant.DATE_STANDARD)
				+ " 06:59:59')";

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

	public Boolean timeBetweenOrNot(Date date) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date0 = df.format(date);
		String date1 = "00:00:00";
		String date2 = "06:59:59";
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

	@SuppressWarnings("unchecked")
	public List<DowntimeCapturedDto> getCompressorDowntimeCodes(String activeFlag) {
		List<DowntimeCapturedDto> responseDto = new ArrayList<DowntimeCapturedDto>();

		try {
			//String queryString = "select cd.downtime_code, cd.downtime_description from compressor_downtime cd";
			String queryString="select cd.downtime_code, cd.downtime_description from compressor_downtime cd where cd.active_flag ='" + activeFlag + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			List<Object[]> responseList = q.list();
			if (!ServicesUtil.isEmpty(responseList)) {

				DowntimeCapturedDto dto = null;
				for (Object[] obj : responseList) {
					dto = new DowntimeCapturedDto();
					dto.setChildCode(ServicesUtil.isEmpty(obj[0]) ? null : (Integer) obj[0]);
					dto.setChildText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);

					responseDto.add(dto);
				}
			}
		} catch (Exception e) {
			System.err.println("[Murphy][CompressorDowntimeDao][getCompressorDowntimeCodes][error]" + e.getMessage());
		}
		return responseDto;
	}
	
	//For Downtime Child Code description
	@SuppressWarnings("unchecked")
	public String getChildDescFromCode(String childCode) {
		String childCodeDesc = null;
		try {
		String query = "select downtime_description from  compressor_downtime where downtime_code in ('" + childCode + "')";
		//select CHILD_CODE_DESCRIPTION from  DT_WELL_CHILD_CODE where CHILD_CODE in
		Object obj = this.getSession().createSQLQuery(query).uniqueResult();
		if(!ServicesUtil.isEmpty(obj))
			childCodeDesc = obj.toString();
		}
		catch(Exception e)
		{
			logger.error("[getChildDescFromCode][childCodeDesc][error]" + e.getMessage());
		}
	logger.error("childCodeDesc : " + childCodeDesc);	
	return childCodeDesc;
	}
	
	public String fetchComNameForProvidedMerrick(int merrickID) {
		String comName = null;
		try {
			String query = "select EQUIPMENT_TEXT from PRODUCTION_EQUIPMENT pe inner join EQUIPMENT_MERRICK em on"+
                           " pe.EQUIPMENT_CODE = em.EQUIPMENT_CODE where em.MERRICK_ID in ('" + merrickID + "')";
			logger.error("[fetchComNameForProvidedMerrick][query] :" +query);
			Object obj = this.getSession().createSQLQuery(query).uniqueResult();
			if(!ServicesUtil.isEmpty(obj))
				comName = obj.toString();
			}
			catch(Exception e)
			{
				logger.error("[fetchComNameForProvidedMerrick][flareName][error]" + e.getMessage());
			}
		logger.error("comName : " + comName);
		return comName;
}

}