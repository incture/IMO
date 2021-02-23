package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
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

import com.murphy.taskmgmt.dto.FlareDowntimeDto;
import com.murphy.taskmgmt.dto.FlareDowntimeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeUpdateDto;
import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCaptureFetchResponseDto;
import com.murphy.integration.dto.FlareCaptureFetchResponseDto;
import com.murphy.integration.dto.FlareDowntimeCaptureDto;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.interfaces.FlareDowntimeCaptureLocal;
import com.murphy.integration.service.DowntimeCapture;
import com.murphy.integration.service.FlareDowntimeCapture;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.FlareCodeDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHistoryRolledUpDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.FlareDowntimeDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("flareDowntimeCapturedDao")
public class FlareDowntimeCaptureDao extends BaseDao<FlareDowntimeDo, FlareDowntimeDto> {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCapturedDao.class);

	public FlareDowntimeCaptureDao() {
	}

	@Autowired
	HierarchyDao locDao;

	@Override
	protected FlareDowntimeDo importDto(FlareDowntimeDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		FlareDowntimeDo entity = new FlareDowntimeDo();

		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getMerrickId()))
			entity.setMerrickId(fromDto.getMerrickId());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getMeter()))
			entity.setMeter(fromDto.getMeter());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getType()))
			entity.setType(fromDto.getType());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedBy()))
			entity.setUpdatedBy(fromDto.getUpdatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getChildCode()))
			entity.setChildCode(fromDto.getChildCode());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(ServicesUtil.resultAsDateDowntime(fromDto.getCreatedAt()));
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(ServicesUtil.resultAsDateDowntime((fromDto.getUpdatedAt())));
		if (!ServicesUtil.isEmpty(fromDto.getDurationByRocMinute()))
			entity.setDurationByRoc(fromDto.getDurationByRocMinute());
		if (!ServicesUtil.isEmpty(fromDto.getEndTime()))
			entity.setEndTime(fromDto.getEndTime());
		if (!ServicesUtil.isEmpty(fromDto.getStartTime()))
			entity.setStartTime(fromDto.getStartTime());
		if (!ServicesUtil.isEmpty(fromDto.getChildText()))
			entity.setChildText(fromDto.getChildText());
		if (!ServicesUtil.isEmpty(fromDto.getFlareVolume()))
			entity.setFlareVolume(fromDto.getFlareVolume());

		return entity;
	}

	@Override
	protected FlareDowntimeDto exportDto(FlareDowntimeDo entity) {
		FlareDowntimeDto dto = new FlareDowntimeDto();

		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getCreatedBy()))
			dto.setCreatedBy(entity.getCreatedBy());
		if (!ServicesUtil.isEmpty(entity.getMerrickId()))
			dto.setMerrickId(entity.getMerrickId());
		if (!ServicesUtil.isEmpty(entity.getMeter()))
			dto.setMeter(entity.getMeter());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			dto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getType()))
			dto.setType(entity.getType());
		if (!ServicesUtil.isEmpty(entity.getUpdatedBy()))
			dto.setUpdatedBy(entity.getUpdatedBy());
		if (!ServicesUtil.isEmpty(entity.getChildCode()))
			dto.setChildCode(entity.getChildCode());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(ServicesUtil.isEmpty(entity.getCreatedAt()) ? null
					: ServicesUtil.resultDateAsStringDowntime(entity.getCreatedAt()));
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			dto.setUpdatedAt(ServicesUtil.isEmpty(entity.getUpdatedAt()) ? null
					: ServicesUtil.resultDateAsStringDowntime(entity.getUpdatedAt()));
		if (!ServicesUtil.isEmpty(entity.getDurationByRoc()))
			dto.setDurationByRocMinute(entity.getDurationByRoc());
		if (!ServicesUtil.isEmpty(entity.getEndTime()))
			dto.setEndTime(entity.getEndTime());
		if (!ServicesUtil.isEmpty(entity.getFlareVolume()))
			dto.setFlareVolume(entity.getFlareVolume());
		if (!ServicesUtil.isEmpty(entity.getStartTime()))
			dto.setStartTime(ServicesUtil.isEmpty(entity.getStartTime()) ? null : (entity.getStartTime()));

		return dto;
	}

	@SuppressWarnings("unchecked")
	public ResponseMessage createFlareDowntime(FlareDowntimeUpdateDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Flare Downtime " + MurphyConstant.CREATE_FAILURE);
		FlareDowntimeDto capturedDto = dto.getDto();
		capturedDto.setUpdatedAt(capturedDto.getCreatedAt());
		capturedDto.setUpdatedBy(capturedDto.getCreatedBy());
		try {

			if (dto.getIsProCountUpdate().equals(true)) {
				// if
				// (!ServicesUtil.isEmpty(capturedDto.getDurationByRocMinute())
				// || !ServicesUtil.isEmpty(capturedDto.getDurationByRocHour()))
				capturedDto.setStatus(MurphyConstant.DOWNTIME_COMPLETE);
				// else
				// capturedDto.setStatus(MurphyConstant.DOWNTIME_PENDING);
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
			logger.error("[Created AT UTC]:"+ capturedDto.getCreatedAt());
			//EOC

			if (!ServicesUtil.isEmpty(capturedDto.getDurationByRocHour()))
				capturedDto.setDurationByRocMinute((capturedDto.getDurationByRocHour() * 60)
						+ (!ServicesUtil.isEmpty(capturedDto.getDurationByRocMinute())
								? capturedDto.getDurationByRocMinute() : 0));

			if (!ServicesUtil.isEmpty(capturedDto.getMerrickId())) {

				String getWellQuery = "select p1.meter_name from tm_flare_meter p1 " + "where p1.merrick_id='"
						+ capturedDto.getMerrickId() + "'";
				Query q = this.getSession().createSQLQuery(getWellQuery);
				List<String> response = q.list();
				if (!ServicesUtil.isEmpty(response)) {
					String meterName = (String) response.get(0);
					capturedDto.setMeter(meterName);
				}
			}
			create(capturedDto);
			logger.error("[Murphy][FlareDowntimeCaptureDao][createFlareDowntime][error]" + "");
			responseDto.setMessage("Downtime " + MurphyConstant.CREATED_SUCCESS);
			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][FlareDowntimeCaptureDao][createFlareDowntime][error]" + e.getMessage());
		}

		return responseDto;
	}

	public String updateFlareDowntime(FlareDowntimeDto dto) {
		String response = MurphyConstant.FAILURE;
		String subQuery = "";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocHour()))
			dto.setDurationByRocMinute((dto.getDurationByRocHour() * 60)
					+ (!ServicesUtil.isEmpty(dto.getDurationByRocMinute()) ? dto.getDurationByRocMinute() : 0));
		if (!ServicesUtil.isEmpty(dto.getChildCode()))
			subQuery = subQuery + " dc.CHILD_CODE ='" + dto.getChildCode() + "',";
		if (!ServicesUtil.isEmpty(dto.getChildText()))
			subQuery = subQuery + " dc.CHILD_TEXT ='" + dto.getChildText() + "',";
		if (!ServicesUtil.isEmpty(dto.getUpdatedBy()))
			subQuery = subQuery + " dc.UPDATED_BY ='" + dto.getUpdatedBy() + "',";
		if (!ServicesUtil.isEmpty(dto.getFlareVolume()))
			subQuery = subQuery + " dc.FLARE_VOLUME =" + dto.getFlareVolume() + ",";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocMinute()))
			subQuery = subQuery + " dc.DURATION_MINUTE_ROC =" + dto.getDurationByRocMinute() + ",";
		if (!ServicesUtil.isEmpty(dto.getDurationByRocMinute())) {
			subQuery = subQuery + " dc.UPDATED_AT = current_timestamp , " + "dc.status='" + MurphyConstant.COMPLETE
					+ "' ,";
		}
		
		//SOC : Date to be stored in UTC while creating in Hana
		logger.error("[Update] UpdatedAt time Millisec from UI: "+dto.getUpdatedAt());
		long millis= Long.parseLong(dto.getUpdatedAt());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		dto.setUpdatedAt(sdf.format(cal.getTime()));
		System.out.println("[Update DT ][Updated At UTC]:"+ dto.getUpdatedAt());
		logger.error("[Update DT ][Updated At UTC]:"+ dto.getUpdatedAt());
		//EOC
		
		subQuery = subQuery.substring(0, subQuery.length() - 1);
		String updateQuery = "UPDATE FLARE_DOWNTIME dc SET " + subQuery + "  where id= '" + dto.getId() + "' ";
		logger.error(
				"[Murphy][FlareDowntimeCapturedDao][updateFlareDowntime][query]" + " & [queryString] " + updateQuery);
		int updatedRow = this.getSession().createSQLQuery(updateQuery).executeUpdate();
		logger.error("[Murphy][FlareDowntimeCapturedDao][updateFlareDowntime][query]" + " & [queryString] "
				+ updateQuery + "  " + updatedRow);
		if (updatedRow > 0) {
			response = MurphyConstant.SUCCESS;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public String getMerrickId(FlareDowntimeDto dto) {

		String getQuery = "SELECT MERRICK_ID FROM FLARE_DOWNTIME WHERE id= '" + dto.getId() + "' ";
		logger.error("[Murphy][FlareDowntimeCapturedDao][getMerrickId][query]" + " & [queryString] " + getQuery);
		Query q = this.getSession().createSQLQuery(getQuery);
		List<String> obj = q.list();
		logger.error("[Murphy][FlareDowntimeCapturedDao][getMerrickId][query]" + " & [queryString] " + getQuery
				+ "[response] " + obj.get(0));
		if (!ServicesUtil.isEmpty(obj)) {

			return obj.get(0);

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Date getCreatedAt(FlareDowntimeDto dto) {

		String getQuery = "SELECT CREATED_AT FROM FLARE_DOWNTIME WHERE id= '" + dto.getId() + "' ";
		logger.error("[Murphy][FlareDowntimeCapturedDao][getMerrickId][query]" + " & [queryString] " + getQuery);
		Query q = this.getSession().createSQLQuery(getQuery);
		List<Date> obj = q.list();
		logger.error("[Murphy][FlareDowntimeCapturedDao][getMerrickId][query]" + " & [queryString] " + getQuery
				+ "[created_at] " + obj.get(0));
		if (!ServicesUtil.isEmpty(obj)) {

			return obj.get(0);

		}
		return null;
	}

	@SuppressWarnings("unchecked") // List<FlareDowntimeDto>
	public FlareDowntimeResponseDto getDowntime(DowntimeRequestDto dto) {
		List<LocationHierarchyDto> dtoList = dto.getLocationHierarchy();
		//logger.error("dtoList : " + dtoList.get(12));
		List<FlareDowntimeDto> feedDtos = null;
		FlareDowntimeResponseDto dtDto = new FlareDowntimeResponseDto(); 
		String locationType = dto.getLocationType();
		String locations = locDao.getMeterUsinglocationType(dtoList, locationType);
		// Location History
		FlareDowntimeDto proDcDto = null;
		List<LocationHistoryRolledUpDto> flareCountList = new ArrayList<LocationHistoryRolledUpDto>();
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

			if (time.equals(true)) {
				nextDate = cstDate;
				cstDate = ServicesUtil.getPrevDate(cstDate);
			}

			if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
				nextDate = cstDate;
				cstDate = ServicesUtil.getPrevDate(cstDate);
			}
			if (!ServicesUtil.isEmpty(locations)) {
				String commonQuery = "select dc.ID, dc.TYPE,dc.METER,dc.CHILD_CODE, dc.CREATED_AT,"
						+ " dc.CREATED_BY, dc.UPDATED_AT, dc.UPDATED_BY, dc.MERRICK_ID,"
						+ " dc.STATUS, dc.DURATION_MINUTE, "
						+ " dc.DURATION_MINUTE_ROC ,dc.CHILD_TEXT ,dc.FLARE_VOLUME from FLARE_DOWNTIME dc ";
				String locationQuery = " where dc.merrick_id in ";
				String statusQuery = " and dc.status= ";
				String typeQuery = " and dc.type= ";
				/*String timeQuery = " and dc.created_At between '"
						+ ServicesUtil.convertFromZoneToZoneString(cstDate, null, "", "", "",
								MurphyConstant.DATE_STANDARD)
						+ " 07:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null, "", "", "",
								MurphyConstant.DATE_STANDARD)
						+ " 06:59:59' "; */
				//Soc : Date time in UTC earlier in CST
				String timeQuery = " and dc.created_At between '"
						+ ServicesUtil.convertFromZoneToZoneString(cstDate, null, MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "",
								MurphyConstant.DATE_STANDARD)
						+ " 12:00:00' and '" + ServicesUtil.convertFromZoneToZoneString(nextDate, null, MurphyConstant.CST_ZONE, MurphyConstant.UTC_ZONE, "",
								MurphyConstant.DATE_STANDARD)
						+ " 11:59:59' ";
				//EOC : Date time in UTC earlier in CST
				String endQuery = "order by dc.created_at desc";
	
				typeQuery = typeQuery + "'Flare'";
				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_SUBMITTED)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (dto.getStatusType().equals(MurphyConstant.DOWNTIME_REVIEW)) {
					statusQuery = statusQuery + "'" + MurphyConstant.COMPLETE + "' ";
				}
				if (!ServicesUtil.isEmpty(locations)) {
					locationQuery += " (" + locations + ")";
				}

				String queryString = commonQuery + locationQuery + statusQuery + typeQuery;

				if (!dto.getStatusType().equals(MurphyConstant.DOWNTIME_DESIGNATED))
					queryString += timeQuery;

				queryString += endQuery;

					// Location History
					if (!ServicesUtil.isEmpty(dto.getMonthTime())) {
						if (dto.getMonthTime().equalsIgnoreCase("one"))
							cal.add(Calendar.DATE, -30);
						else if (dto.getMonthTime().equalsIgnoreCase("three"))
							cal.add(Calendar.DATE, -90);
						/*queryString = commonQuery + locationQuery + " and dc.status IN (" + "'" + MurphyConstant.COMPLETE
								+ "') and dc.type = 'Flare' and to_date(dc.created_At) >= '" + sdf.format(cal.getTime())
								+ "' and to_date(dc.created_At) <= '" + sdf.format(currentCal.getTime()) + "'" + endQuery;
	
						String countQueryString = " SELECT COUNT(*) AS COUNT FROM " + "(" + queryString + ")";
						query = this.getSession().createSQLQuery(countQueryString);
						countResult = ((BigInteger) query.uniqueResult()).intValue();
	
						if (dto.getPage() > 0) {
							int first = (dto.getPage() - 1) * dto.getPage_size();
							int last = dto.getPage_size();
							queryString += " LIMIT " + last + " OFFSET " + first + "";
						}*/
						
						//SOC : Rework of Downtime Location History - From ProCount
						String fromOriginaldate = sdf.format(cal.getTime());
						String toOriginalDate = sdf.format(currentCal.getTime());
						FlareDowntimeCaptureLocal falreDowntimeLoc = new FlareDowntimeCapture();
						FlareCaptureFetchResponseDto flareFetchRespDto = new FlareCaptureFetchResponseDto();
						List<String> listOfMerrick = new ArrayList<String>();
						
						for (LocationHierarchyDto locationDto : dtoList) {
							listOfMerrick.add(locationDto.getMuwi());
						}
						if(!ServicesUtil.isEmpty(listOfMerrick) && listOfMerrick.size() > 0){
							flareFetchRespDto = falreDowntimeLoc.fetchRecordForMerrickIds(listOfMerrick, fromOriginaldate, toOriginalDate, dto.getPage(),dto.getPage_size());
							countResult = Integer.valueOf( flareFetchRespDto.getTotalCount());
							logger.error("flareFetchRespDto countResult : "+countResult);
							logger.error("list flareFetchRespDto.getDcDtoList() : " + flareFetchRespDto.getDcDtoList());
						}
						feedDtos = new ArrayList<FlareDowntimeDto>();
						for (FlareDowntimeCaptureDto d : flareFetchRespDto.getDcDtoList()) {
							proDcDto = new FlareDowntimeDto();
							proDcDto.setMerrickId(ServicesUtil.isEmpty(d.getMerrickId()) ? null : String.valueOf(d.getMerrickId()));
							proDcDto.setChildCode(ServicesUtil.isEmpty(d.getFlareCode()) ? null : d.getFlareCode().trim());
							logger.error("child code :" +d.getFlareCode());
							proDcDto.setChildText(ServicesUtil.isEmpty(d.getFlareCode()) ? null : getChildDescFromCode(d.getFlareCode().trim()));
							proDcDto.setFlareVolume(ServicesUtil.isEmpty(d.getFlareVolume()) ? null : (double)d.getFlareVolume());
							proDcDto.setCreatedAt(ServicesUtil.isEmpty(d.getRecordDate()) ? null : d.getRecordDate());
							proDcDto.setType("Flare");
							proDcDto.setMeter(ServicesUtil.isEmpty(proDcDto.getMerrickId()) ? null : fetchComNameForProvidedMerrick(proDcDto.getMerrickId()));
							
							//For Date Format
							/*DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
							//Note : On deploying in PROD ,change zone to CST
							//formatterIST.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Use in MCQ
							formatterIST.setTimeZone(TimeZone.getTimeZone("CST"));
							Date date = formatterIST.parse(proDcDto.getCreatedAt());
							logger.error("First from ProCount "+formatterIST.format(date));
							//For Mobile
							proDcDto.setInMilliSec(String.valueOf((date.getTime())));
							
							// Increment date by one day
							Calendar cal1 = Calendar.getInstance();
							try{
								   cal1.setTime(formatterIST.parse(formatterIST.format(date)));
								}catch(Exception e){
								   e.printStackTrace();
								}
							cal1.add(Calendar.DAY_OF_MONTH, 1);
							
							DateFormat formatterUTC = new SimpleDateFormat("dd-MMM-yy");
							formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
							//For Web
							proDcDto.setCreatedAtUTC(formatterUTC.format(cal1.getTime())); */
							// Changed to epoch
							DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
							Date date = formatterIST.parse(proDcDto.getCreatedAt());
							logger.error("First from ProCount "+formatterIST.format(date));
							proDcDto.setInMilliSec(String.valueOf((date.getTime())));
							proDcDto.setCreatedAt(String.valueOf((date.getTime())));
							proDcDto.setCreatedAtUTC(String.valueOf((date.getTime())));			
							
							feedDtos.add(proDcDto);
							
						}
						dtDto.setDtoList(feedDtos);
						//EOC : Rework of Downtime Location History - From ProCount
					}
					else {
					logger.error("[Murphy][FlareDowntimeCaptureDao][getDowntime][Query] " + queryString);
	
					Query q = this.getSession().createSQLQuery(queryString);
					List<Object[]> resultList = q.list();
						if (!ServicesUtil.isEmpty(resultList)) {
							feedDtos = new ArrayList<FlareDowntimeDto>();
							for (Object[] obj : resultList) {
								feedDtos.add(getDowntimeDto(obj));
							}
						}
					}
			
			lht.setCount(new BigDecimal(countResult));
			lht.setStatus("FLARE");
			flareCountList.add(lht);
			dtDto.setDtoList(feedDtos);
			dtDto.setPageCount(new BigDecimal(dto.getPage_size()));
			dtDto.setTotalCount(new BigDecimal(countResult));
			dtDto.setItemList(flareCountList);
			}
		} catch (Exception e) {
			logger.error("[Murphy][FlareDowntimeCaptureDao][getDowntime][error]" + e.getMessage());
		}
		return dtDto;
	}

	public FlareDowntimeDto getDowntimeDto(Object[] obj) {
		FlareDowntimeDto feedDto = new FlareDowntimeDto();
		try{
			feedDto.setId((String) obj[0]);
			feedDto.setType(ServicesUtil.isEmpty((String) obj[1]) ? "Flare" : (String) obj[1]);
			feedDto.setMeter((String) obj[2]);
			feedDto.setChildCode((String) obj[3]);
			feedDto.setCreatedAt(
					ServicesUtil.isEmpty((Date) obj[4]) ? null : ServicesUtil.resultDateAsStringDowntime((Date) obj[4]));
			
			// Changed to epoch
			DateFormat formatterIST = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
			Date date = formatterIST.parse(feedDto.getCreatedAt());
				logger.error("In Millisec:  "+String.valueOf((date.getTime())) + " [ after  Date : ]: "+date);
			feedDto.setInMilliSec(String.valueOf((date.getTime())));
			feedDto.setCreatedAt(String.valueOf((date.getTime())));
			feedDto.setCreatedAtUTC(ServicesUtil.isEmpty((Date) obj[4]) ? null : ServicesUtil.resultDateAsStringDowntime((Date) obj[4]));
			
			feedDto.setCreatedBy((String) obj[5]);
			feedDto.setUpdatedAt(
					ServicesUtil.isEmpty((Date) obj[6]) ? null : ServicesUtil.resultDateAsStringDowntime((Date) obj[6]));
			feedDto.setUpdatedBy((String) obj[7]);
			feedDto.setMerrickId((String) obj[8]);
			feedDto.setStatus((String) obj[9]);
			feedDto.setFlareVolume((Double) obj[13]);
			feedDto.setChildText((String) obj[12]);
			if (!ServicesUtil.isEmpty((Integer) obj[10])) {
				int rocMinute = (Integer) obj[10];
				int rocHour = rocMinute / 60;
				rocMinute = rocMinute - (rocHour * 60);
				feedDto.setDurationByRocMinute(rocMinute);
				feedDto.setDurationByRocHour(rocHour);
			} else {
				feedDto.setDurationByRocMinute(0);
				feedDto.setDurationByRocHour(0);
			}
			
		}catch(Exception e){
			logger.error("[Murphy][FlareDowntimeCaptureDao][getDowntimeDto][error]" + e.getMessage());	
		}
		return feedDto;
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
	public List<FlareCodeDto> getFlareDowntimeCodes(String activeFlag) {
		List<FlareCodeDto> responseDto = new ArrayList<FlareCodeDto>();

		// String queryString = "select fd.code, fd.description from flarecode
		// fd";
		String queryString = "select fd.code, fd.description from flarecode fd  where fd.ACTIVE_FLAG='" + activeFlag
				+ "'";
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> responseList = q.list();
		if (!ServicesUtil.isEmpty(responseList)) {

			FlareCodeDto dto = null;
			for (Object[] obj : responseList) {
				dto = new FlareCodeDto();
				dto.setChildCode(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				dto.setChildText(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);

				responseDto.add(dto);
			}
		}

		return responseDto;
	}
	
	//For Flare Child Code description
	@SuppressWarnings("unchecked")
	public String getChildDescFromCode(String childCode) {
		String childCodeDesc = null;
		try {
		String query = "select DESCRIPTION from FLARECODE where CODE in ('" + childCode + "')";
		logger.error("[getChildDescFromCode][query] :" +query);
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
	
	public String fetchComNameForProvidedMerrick(String merrickId)
	{
		String flareName = null;
		try {
			String query = "select distinct METER from FLARE_DOWNTIME where MERRICK_ID in ('" + merrickId + "')";
			logger.error("[fetchComNameForProvidedMerrick][query] :" +query);
			Object obj = this.getSession().createSQLQuery(query).uniqueResult();
			if(!ServicesUtil.isEmpty(obj))
				flareName = obj.toString();
			}
			catch(Exception e)
			{
				logger.error("[fetchComNameForProvidedMerrick][flareName][error]" + e.getMessage());
			}
		logger.error("flareName : " + flareName);	
		return flareName;
	}

}
