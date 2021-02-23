/**
 * 
 */
package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.BypassRiskLevelDto;
import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassActivityLogDto;
import com.murphy.taskmgmt.dto.SsdBypassHeaderDto;
import com.murphy.taskmgmt.dto.SsdBypassListDto;
import com.murphy.taskmgmt.dto.SsdBypassLogResponseDto;
import com.murphy.taskmgmt.entity.SsdBypassActivityLogDo;
import com.murphy.taskmgmt.entity.SsdBypassAttachmentsDo;
import com.murphy.taskmgmt.entity.SsdBypassCommentsDo;
import com.murphy.taskmgmt.entity.SsdBypassHeaderDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Kamlesh.Choubey
 *
 */

@Repository("SsdBypassHeaderDao")
public class SsdBypassHeaderDao extends BaseDao<SsdBypassHeaderDo, SsdBypassHeaderDto> {

	private static final Logger logger = LoggerFactory.getLogger(SsdBypassHeaderDao.class);

	@Autowired
	SsdBypassCommentDao ssdBypassCommentDao;

	@Autowired
	SsdBypassAttachmentDao ssdBypassAttachmentsDao;

	@Autowired
	SsdBypassActivityLogDao ssdBypassActivityLogDao;

	@Autowired
	private HierarchyDao locDao;

	@Override
	protected SsdBypassHeaderDo importDto(SsdBypassHeaderDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		SsdBypassHeaderDo entity = new SsdBypassHeaderDo();

		if (!ServicesUtil.isEmpty(fromDto.getSsdBypassId())) {
			entity.setSsdBypassId(fromDto.getSsdBypassId());
		}
		if (!ServicesUtil.isEmpty(fromDto.getSsdBypassNum())) {
			entity.setSsdBypassNum(fromDto.getSsdBypassNum());
		}
		if (!ServicesUtil.isEmpty(fromDto.getDeviceBypassed())) {
			entity.setDeviceBypassed(fromDto.getDeviceBypassed());
		}
		if (!ServicesUtil.isEmpty(fromDto.getReasonForBypass())) {
			entity.setReasonForBypass(fromDto.getReasonForBypass());
		}
		if (!ServicesUtil.isEmpty(fromDto.getBypassStartedBy())) {
			entity.setBypassStartedBy(fromDto.getBypassStartedBy());
		}
		if (!ServicesUtil.isEmpty(fromDto.getBypassStartTime())) {
			entity.setBypassStartTime(fromDto.getBypassStartTime());
		}
		if (!ServicesUtil.isEmpty(fromDto.getBypassCompleteTime())) {
			entity.setBypassCompleteTime(fromDto.getBypassCompleteTime());
		}
		if (!ServicesUtil.isEmpty(fromDto.getIsOpApprovalObtained())) {
			entity.setIsOpApprovalObtained(fromDto.getIsOpApprovalObtained());
		}
		if (!ServicesUtil.isEmpty(fromDto.getIsBypassTagAttached())) {
			entity.setIsBypassTagAttached(fromDto.getIsBypassTagAttached());
		}
		if (!ServicesUtil.isEmpty(fromDto.getIsDcsPlcControlledDevice())) {
			entity.setIsDcsPlcControlledDevice(fromDto.getIsDcsPlcControlledDevice());
		}
		if (!ServicesUtil.isEmpty(fromDto.getIsAffectedPersonnelNotified())) {
			entity.setIsAffectedPersonnelNotified(fromDto.getIsAffectedPersonnelNotified());
		}
		if (!ServicesUtil.isEmpty(fromDto.getFirstLineSupvApprovalStatus())) {
			entity.setFirstLineSupvApprovalStatus(fromDto.getFirstLineSupvApprovalStatus());
		}
		if (!ServicesUtil.isEmpty(fromDto.getFirstLineSupvApprovalInitBy())) {
			entity.setFirstLineSupvApprovalInitBy(fromDto.getFirstLineSupvApprovalInitBy());
		}
		if (!ServicesUtil.isEmpty(fromDto.getFirstLineSupvApprovalInitAt())) {
			entity.setFirstLineSupvApprovalInitAt(fromDto.getFirstLineSupvApprovalInitAt());
		}
		if (!ServicesUtil.isEmpty(fromDto.getFieldPlantSuptApprovalStatus())) {
			entity.setFieldPlantSuptApprovalStatus(fromDto.getFieldPlantSuptApprovalStatus());
			;
		}
		if (!ServicesUtil.isEmpty(fromDto.getFieldPlantSuptApprovalInitBy())) {
			entity.setFieldPlantSuptApprovalInitBy(fromDto.getFieldPlantSuptApprovalInitBy());
		}
		if (!ServicesUtil.isEmpty(fromDto.getFieldPlantSuptApprovalInitAt())) {
			entity.setFieldPlantSuptApprovalInitAt(fromDto.getFieldPlantSuptApprovalInitAt());
		}
		if (!ServicesUtil.isEmpty(fromDto.getOpMgrApprovalStatus())) {
			entity.setOpMgrApprovalStatus(fromDto.getOpMgrApprovalStatus());
		}
		if (!ServicesUtil.isEmpty(fromDto.getOpMgrApprovalInitBy())) {
			entity.setOpMgrApprovalInitBy(fromDto.getOpMgrApprovalInitBy());
		}
		if (!ServicesUtil.isEmpty(fromDto.getOpMgrApprovalInitAt())) {
			entity.setOpMgrApprovalInitAt(fromDto.getOpMgrApprovalInitAt());
		}
		if (!ServicesUtil.isEmpty(fromDto.getIsEmocCreated())) {
			entity.setIsEmocCreated(fromDto.getIsEmocCreated());
		}
		if (!ServicesUtil.isEmpty(fromDto.getEmocNumber())) {
			entity.setEmocNumber(fromDto.getEmocNumber());
		}
		if (!ServicesUtil.isEmpty(fromDto.getBypassStatus())) {
			entity.setBypassStatus(fromDto.getBypassStatus());
		}
		if (!ServicesUtil.isEmpty(fromDto.getLocation())) {
			entity.setLocation(fromDto.getLocation());
		}

		if (!ServicesUtil.isEmpty(fromDto.getOperatorId())) {
			entity.setOperatorId(fromDto.getOperatorId());
		}

		if (!ServicesUtil.isEmpty(fromDto.getSeverity())) {
			entity.setSeverity(fromDto.getSeverity());
		}

		if (!ServicesUtil.isEmpty(fromDto.getLocationCode())) {
			entity.setLocationCode(fromDto.getLocationCode());
		}
		if (!ServicesUtil.isEmpty(fromDto.getShiftChangeAcknowledged())) {
			entity.setShiftChangeAcknowledged(fromDto.getShiftChangeAcknowledged());
		}
		if (!ServicesUtil.isEmpty(fromDto.getEquipmentId())) {
			entity.setEquipment_id(fromDto.getEquipmentId());
		}
		if (!ServicesUtil.isEmpty(fromDto.getEquipmentDesc())) {
			entity.setEquipment_desc(fromDto.getEquipmentDesc());
		}
		if (!ServicesUtil.isEmpty(fromDto.getRisk())) {
			entity.setRisk(fromDto.getRisk());
		}

		return entity;
	}

	@Override
	protected SsdBypassHeaderDto exportDto(SsdBypassHeaderDo entity) {
		SsdBypassHeaderDto dto = new SsdBypassHeaderDto();
		if (!ServicesUtil.isEmpty(entity.getSsdBypassId())) {
			dto.setSsdBypassId(entity.getSsdBypassId());
		}
		if (!ServicesUtil.isEmpty(entity.getSsdBypassNum())) {
			dto.setSsdBypassNum(String.valueOf(Long.parseLong(entity.getSsdBypassNum()) % 1000000));
		}
		if (!ServicesUtil.isEmpty(entity.getDeviceBypassed())) {
			dto.setDeviceBypassed(entity.getDeviceBypassed());
		}
		if (!ServicesUtil.isEmpty(entity.getReasonForBypass())) {
			dto.setReasonForBypass(entity.getReasonForBypass());
		}
		if (!ServicesUtil.isEmpty(entity.getBypassStartedBy())) {
			dto.setBypassStartedBy(entity.getBypassStartedBy());
		}
		if (!ServicesUtil.isEmpty(entity.getBypassStartTime())) {
			dto.setBypassStartTime(entity.getBypassStartTime());
		}
		if (!ServicesUtil.isEmpty(entity.getBypassCompleteTime())) {
			dto.setBypassCompleteTime(entity.getBypassCompleteTime());
		}
		if (!ServicesUtil.isEmpty(entity.getIsOpApprovalObtained())) {
			dto.setIsOpApprovalObtained(entity.getIsOpApprovalObtained());
		}
		if (!ServicesUtil.isEmpty(entity.getIsBypassTagAttached())) {
			dto.setIsBypassTagAttached(entity.getIsBypassTagAttached());
		}
		if (!ServicesUtil.isEmpty(entity.getIsDcsPlcControlledDevice())) {
			dto.setIsDcsPlcControlledDevice(entity.getIsDcsPlcControlledDevice());
		}
		if (!ServicesUtil.isEmpty(entity.getIsAffectedPersonnelNotified())) {
			dto.setIsAffectedPersonnelNotified(entity.getIsAffectedPersonnelNotified());
		}
		if (!ServicesUtil.isEmpty(entity.getFirstLineSupvApprovalStatus())) {
			dto.setFirstLineSupvApprovalStatus(entity.getFirstLineSupvApprovalStatus());
		}
		if (!ServicesUtil.isEmpty(entity.getFirstLineSupvApprovalInitBy())) {
			dto.setFirstLineSupvApprovalInitBy(entity.getFirstLineSupvApprovalInitBy());
		}
		if (!ServicesUtil.isEmpty(entity.getFirstLineSupvApprovalInitAt())) {
			dto.setFirstLineSupvApprovalInitAt(entity.getFirstLineSupvApprovalInitAt());
		}
		if (!ServicesUtil.isEmpty(entity.getFieldPlantSuptApprovalStatus())) {
			dto.setFieldPlantSuptApprovalStatus(entity.getFieldPlantSuptApprovalStatus());
		}
		if (!ServicesUtil.isEmpty(entity.getFieldPlantSuptApprovalInitBy())) {
			dto.setFieldPlantSuptApprovalInitBy(entity.getFieldPlantSuptApprovalInitBy());
		}
		if (!ServicesUtil.isEmpty(entity.getFieldPlantSuptApprovalInitAt())) {
			dto.setFieldPlantSuptApprovalInitAt(entity.getFieldPlantSuptApprovalInitAt());
		}
		if (!ServicesUtil.isEmpty(entity.getOpMgrApprovalStatus())) {
			dto.setOpMgrApprovalStatus(entity.getOpMgrApprovalStatus());
		}
		if (!ServicesUtil.isEmpty(entity.getOpMgrApprovalInitBy())) {
			dto.setOpMgrApprovalInitBy(entity.getOpMgrApprovalInitBy());
		}
		if (!ServicesUtil.isEmpty(entity.getOpMgrApprovalInitAt())) {
			dto.setOpMgrApprovalInitAt(entity.getOpMgrApprovalInitAt());
		}
		if (!ServicesUtil.isEmpty(entity.getIsEmocCreated())) {
			dto.setIsEmocCreated(entity.getIsEmocCreated());
		}
		if (!ServicesUtil.isEmpty(entity.getEmocNumber())) {
			dto.setEmocNumber(entity.getEmocNumber());
		}
		if (!ServicesUtil.isEmpty(entity.getBypassStatus())) {
			dto.setBypassStatus(entity.getBypassStatus());
		}

		if (!ServicesUtil.isEmpty(entity.getLocation())) {
			dto.setLocation(entity.getLocation());
		}

		if (!ServicesUtil.isEmpty(entity.getOperatorId())) {
			dto.setOperatorId(entity.getOperatorId());
		}

		if (!ServicesUtil.isEmpty(entity.getSeverity())) {
			dto.setSeverity(entity.getSeverity());
		}

		if (!ServicesUtil.isEmpty(entity.getLocationCode())) {
			dto.setLocationCode(entity.getLocationCode());
		}

		if (!ServicesUtil.isEmpty(entity.getShiftChangeAcknowledged())) {
			dto.setShiftChangeAcknowledged(entity.getShiftChangeAcknowledged());
		}
		if (!ServicesUtil.isEmpty(entity.getEquipment_id())) {
			dto.setEquipmentId(entity.getEquipment_id());
		}
		if (!ServicesUtil.isEmpty(entity.getEquipment_desc())) {
			dto.setEquipmentDesc(entity.getEquipment_desc());
		}
		if (!ServicesUtil.isEmpty(entity.getRisk())) {
			dto.setRisk(entity.getRisk());
		}

		return dto;
	}

	public String createBypassLog(SsdBypassHeaderDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			dto.setLocation(locDao.getLocationByLocCode(dto.getLocationCode()));
			// dto.setSsdBypassNum(getSeqVal());
			create(dto);
			reponse = MurphyConstant.SUCCESS;
			this.getSession().flush();

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][createBypassLog][error]" + e.getMessage());

		}
		return reponse;

	}

	public SsdBypassLogResponseDto getBypassLogById(String bypassId) {
		SsdBypassLogResponseDto ssdBypassLogResponseDto = new SsdBypassLogResponseDto();
		SsdBypassHeaderDo ssdBypassHeaderDo = new SsdBypassHeaderDo();
		List<SsdBypassCommentsDo> SsdBypassCommentsDoList = new ArrayList<>();
		List<SsdBypassAttachmentsDo> ssdBypassAttachementDoList = new ArrayList<>();
		List<SsdBypassActivityLogDo> SsdBypassActivityLogDoList = new ArrayList<>();

		try {

			ssdBypassHeaderDo = (SsdBypassHeaderDo) (this.getSession().get(SsdBypassHeaderDo.class, bypassId));
			Criteria commentCriteria = this.getSession().createCriteria(SsdBypassCommentsDo.class);
			/*
			 * commentCriteria.add(Restrictions.eq("ssdBypassId", bypassId));
			 * commentCriteria.addOrder(Order.desc(""));
			 */
			SsdBypassCommentsDoList = (this.getSession().createCriteria(SsdBypassCommentsDo.class)
					.add(Restrictions.eq("ssdBypassId", bypassId)).list());
			ssdBypassAttachementDoList = (this.getSession().createCriteria(SsdBypassAttachmentsDo.class)
					.add(Restrictions.eq("bypassId", bypassId)).list());
			SsdBypassActivityLogDoList = (this.getSession().createCriteria(SsdBypassActivityLogDo.class)
					.add(Restrictions.eq("ssdBypassId", bypassId)).addOrder(Order.asc("bypassStatusReviewedAt")).list());

			SsdBypassHeaderDto ssdBypassHeaderDto = exportDto(ssdBypassHeaderDo);
			String innerQuery = "select PERSON_RESPONSIBLE from ssd_bypass_activity_log where ssd_bypass_id = '"
					+ bypassId
					+ "' and activity_type='ASSIGNED' and is_approval_obtained = true order by bypass_status_reviewed_at desc limit 1 ";
			String assignedTo = (String) this.getSession().createSQLQuery(innerQuery).uniqueResult();
			ssdBypassHeaderDto.setAssignedTo(assignedTo);
			ssdBypassLogResponseDto.setSsdBypassLogHeaderdto(ssdBypassHeaderDto);
			ssdBypassLogResponseDto
					.setSsdBypassCommentDtoList(ssdBypassCommentDao.exportDtoList(SsdBypassCommentsDoList));
			ssdBypassLogResponseDto
					.setSsdBypassAttachementDtoList(ssdBypassAttachmentsDao.exportDtoList(ssdBypassAttachementDoList));
			ssdBypassLogResponseDto
					.setSsdBypassActivityLogDtoList(ssdBypassActivityLogDao.exportDtoList(SsdBypassActivityLogDoList));
			
			for (int i = 0; i < ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().size() ; i++){
				logger.error("[Murphy][SsdBypassHeaderDao][getBypassLogById][activityLog] : LogId : " 
				+ ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().get(i).getSsdBypassLogId()
				+ " IsApprovalObtained : " + ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().get(i).getIsApprovalObtained() 
				+ " Person responsible : " + ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().get(i).getPersonResponsible() 
				+ " ReviewedAt : " + ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().get(i).getBypassStatusReviewedAt()
				+ " Activity Type : "+ ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().get(i).getActivityType()
				+ " Operator Type : "+ ssdBypassLogResponseDto.getSsdBypassActivityLogDtoList().get(i).getOperatorType());
			}

			this.getSession().flush();
			// this.getSession().close();

		} catch (Exception e) {

			logger.error("[Murphy][SsdBypassHeaderDao][getBypassLogById][error]" + e.getMessage());

		}

		return ssdBypassLogResponseDto;
	}

	public String updateBypassLog(SsdBypassHeaderDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			update(dto);
			reponse = MurphyConstant.SUCCESS;
			// this.getSession().flush();
			// this.getSession().close();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][updateBypassLog][error]" + e.getMessage());

		}
		return reponse;
	}

	public String getSeqVal() {
		String ssdBypassNum = "BPL";
		int id = 0;
		try {
			String query = "select max(bypassSeq) from SEQUENCE_TABLE";
			id = (Integer) this.getSession().createSQLQuery(query).uniqueResult();
			if (!ServicesUtil.isEmpty(id)) {
				id++;
				String queryString = "insert into SEQUENCE_TABLE(bypassSeq) values(" + id + ")";
				this.getSession().createSQLQuery(queryString).executeUpdate();
				ssdBypassNum = ssdBypassNum + id;

			} else {
				ssdBypassNum = "BPL1";
			}
			this.getSession().flush();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}
		System.err.println("ssdBypassNum :" + ssdBypassNum);

		return ssdBypassNum;
	} 

	public Map<List<SsdBypassListDto>, Map<String, Integer>> getBypassLogList(String locationList, int timePeriod,
			int pageNo, int pageSize, String bypassLogStatus , String locationType,boolean isActive) {
		Map<List<SsdBypassListDto>, Map<String, Integer>> ssdBypassLogMap = new LinkedHashMap();
		int count = 0;
		int limit = pageSize;
		int offset = 0;
		String status = "";
		int statusCount = 0;
		String query = "";
		String queryForTotalCount = "";
		Map<String, Integer> statusCountList = new LinkedHashMap<>();
		if (pageNo > 0) {
			offset = ((pageNo - 1) * pageSize);
			limit = pageSize;

		}

		List<SsdBypassListDto> sdBypassListDto = new ArrayList<>();
		try {
			String locations = "";
			if (!ServicesUtil.isEmpty(locationList)) {
				locations = ServicesUtil.getStringForInQuery(locationList);
				// queryForTotalCount = "select count(*)" + " from
				// SSD_BYPASS_HEADER where BYPASS_STATUS= '" +
				// MurphyConstant.INPROGRESS +"'";
			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -timePeriod * 30);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (bypassLogStatus.equalsIgnoreCase("all")) {
				/*query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
						+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locations
						+ " ) and BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
						+ "', 'YYYY-MM-DD') order by BYPASS_START_TIME desc limit " + limit + " offset " + offset;

				queryForTotalCount = "select count(*)" + " from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locations
						+ " ) and BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime()) + "', 'YYYY-MM-DD')";*/
				
				// Web show all in-progress task with month-wise data and drill down
				String locationCodes = ServicesUtil.getStringFromList(getLocationHierarchy(Arrays.asList(locationList.split(",")) , new ArrayList()));
				query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
						+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locationCodes
						+ " ) and ( BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
						+ "', 'YYYY-MM-DD') or BYPASS_STATUS= '" + MurphyConstant.INPROGRESS
						+ "' ) order by BYPASS_STATUS desc, BYPASS_START_TIME desc limit " + limit + " offset " + offset;

				queryForTotalCount = "select count(*)" + " from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locationCodes
						+ " ) and (BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD')  or BYPASS_STATUS= '" + MurphyConstant.INPROGRESS + "')";
			} else if (bypassLogStatus.equalsIgnoreCase("mobile")) {
				if (!ServicesUtil.isEmpty(locationType)) {
						String locationCodes = ServicesUtil.getStringFromList(getLocationHierarchy(Arrays.asList(locationList.split(",")) , new ArrayList()));
					//locations = ServicesUtil.getStringFromList(locDao.getLocCodeByLocationTypeAndCode(locationType, Arrays.asList(locationList.split(","))));
					query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
							+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locationCodes
							+ " ) and ( BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD') or BYPASS_STATUS= '" + MurphyConstant.INPROGRESS
							+ "' ) order by BYPASS_STATUS desc, BYPASS_START_TIME desc limit " + limit + " offset " + offset;
					// Get only in-progress tasks 
					if(isActive){
						query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE"
								+ " from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locationCodes
								+ " ) and BYPASS_STATUS= '" + MurphyConstant.INPROGRESS
								+ "' order by BYPASS_START_TIME desc limit " + limit + " offset " + offset;			
					}

					queryForTotalCount = "select count(*)" + " from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locationCodes
							+ " ) and (BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD')  or BYPASS_STATUS= '" + MurphyConstant.INPROGRESS + "')";
					
				}else{
					cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -(MurphyConstant.BYPASS_LOG_DATA_FOR_DAYS));
					query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
							+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locations
							+ " ) and ( BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD') or BYPASS_STATUS= '" + MurphyConstant.INPROGRESS
							+ "' ) order by BYPASS_START_TIME desc limit " + limit + " offset " + offset;

					queryForTotalCount = "select count(*)" + " from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locations
							+ " ) and ( BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD')  and BYPASS_STATUS= '" + MurphyConstant.INPROGRESS + "' )";
				}
				

			} else {
				if (ServicesUtil.isEmpty(locations)) {
					query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
							+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where BYPASS_STATUS= '" + MurphyConstant.INPROGRESS
							+ "' order by BYPASS_START_TIME desc limit " + limit + " offset " + offset;
					queryForTotalCount = "select count(*)" + " from SSD_BYPASS_HEADER where BYPASS_STATUS= '"
							+ MurphyConstant.INPROGRESS + "'";
				} else {
					query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
							+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locations
							+ " ) and BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD') and BYPASS_STATUS= '" + bypassLogStatus
							+ "' order by BYPASS_START_TIME desc limit " + limit + " offset " + offset;

					queryForTotalCount = "select count(*)" + " from SSD_BYPASS_HEADER where LOCATION_CODE in ( "
							+ locations + " ) and BYPASS_START_TIME > TO_DATE('" + sdf.format(cal.getTime())
							+ "', 'YYYY-MM-DD')  and BYPASS_STATUS= '" + bypassLogStatus + "'";
				}

			}
			logger.error("Bypass query : "+ query);

			count = ((BigInteger) (this.getSession().createSQLQuery(queryForTotalCount).uniqueResult())).intValue();
			List<Object[]> bypassLogFields = this.getSession().createSQLQuery(query).list();
			statusCountList.put("totalCount", count);
			for (Object[] bypassLogField : bypassLogFields) {
				SsdBypassListDto ssdBypassListDto = new SsdBypassListDto();
				ssdBypassListDto.setSsdBypassId((String) bypassLogField[0]);
				ssdBypassListDto.setSsdBypassNum(String.valueOf(Long.parseLong((String) bypassLogField[1]) % 1000000));
				ssdBypassListDto.setDeviceBypassed((String) bypassLogField[2]);
				ssdBypassListDto.setReasonForBypass((String) bypassLogField[3]);
				ssdBypassListDto.setBypassStartTime((Date) bypassLogField[4]);
				status = (String) bypassLogField[5];
				if (!statusCountList.containsKey(status)) {

					statusCount = 1;
					statusCountList.put(status, statusCount);
				} else {
					statusCount = statusCountList.get(status);
					statusCount++;
					statusCountList.put(status, statusCount);

				}
				ssdBypassListDto.setBypassStatus(status);
				ssdBypassListDto.setLocation((String) bypassLogField[6]);
				ssdBypassListDto.setLocationCode((String) bypassLogField[7]);
				ssdBypassListDto.setEquipment_desc((String) bypassLogField[8]);
				if(!ServicesUtil.isEmpty(ssdBypassListDto.getSsdBypassId())){
					String innerQuery = "select PERSON_RESPONSIBLE from ssd_bypass_activity_log where ssd_bypass_id = '"
							+ ssdBypassListDto.getSsdBypassId()
							+ "' and activity_type='ASSIGNED' and is_approval_obtained = true order by bypass_status_reviewed_at desc limit 1 ";
					String assignedTo = (String) this.getSession().createSQLQuery(innerQuery).uniqueResult();
					ssdBypassListDto.setAssignedTo(assignedTo);
				}
				sdBypassListDto.add(ssdBypassListDto);

			}
			ssdBypassLogMap.put(sdBypassListDto, statusCountList);
			this.getSession().flush();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getBypassLogList][error]" + e.getMessage());

		}
		return ssdBypassLogMap;

	}

	public Map<String, String> getOperatorsIdForNotification(int hours) {
		Map<String, String> operatorsMap = new LinkedHashMap<>();
		String bypassId = "";
		String operatorId = "";

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
		Calendar cal1 = Calendar.getInstance();

		cal1.add(Calendar.MINUTE, -(hours * 60));
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MINUTE, -((hours * 60) + 5));
		try {
			Session session = this.getSession();
			String query = "";
			if (hours == 12) {
				query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk,bh.location_code from SSD_BYPASS_HEADER bh"
						+ " where bh.BYPASS_START_TIME > TO_TIMESTAMP('" + sdf.format(cal2.getTime())
						+ "') and bh.BYPASS_START_TIME <= TO_TIMESTAMP('" + sdf.format(cal1.getTime())
						+ "' ) and BYPASS_STATUS = 'IN PROGRESS' and (bh.SEVERITY = 'regular' or bh.risk = 'MEDIUM' or bh.risk = 'LOW')";
				logger.info("[getOperatorsIdForNotification]hours : " + hours + " query : " + query);
			}

			if (hours == 24) {
				query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk,bh.location_code from SSD_BYPASS_HEADER bh"
						+ " where bh.BYPASS_START_TIME > TO_TIMESTAMP('" + sdf.format(cal2.getTime())
						+ "') and bh.BYPASS_START_TIME <= TO_TIMESTAMP('" + sdf.format(cal1.getTime())
						+ "' ) and BYPASS_STATUS = 'IN PROGRESS'";
				logger.info("[getOperatorsIdForNotification]hours : " + hours + " query : " + query);
			}

			if (hours == 70) {
				query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk,bh.location_code from SSD_BYPASS_HEADER bh"
						+ " where bh.BYPASS_START_TIME > TO_TIMESTAMP('" + sdf.format(cal2.getTime())
						+ "') and bh.BYPASS_START_TIME <= TO_TIMESTAMP('" + sdf.format(cal1.getTime())
						+ "' ) and BYPASS_STATUS = 'IN PROGRESS' ";
				logger.info("[getOperatorsIdForNotification]hours : " + hours + " query : " + query);
			}

			if (hours == 10) {
				query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk,bh.location_code from SSD_BYPASS_HEADER bh"
						+ " where bh.BYPASS_START_TIME > TO_TIMESTAMP('" + sdf.format(cal2.getTime())
						+ "' ) and bh.BYPASS_START_TIME <= TO_TIMESTAMP('" + sdf.format(cal1.getTime())
						+ "') and (bh.SEVERITY = 'critical' or bh.risk = 'HIGH') and BYPASS_STATUS = 'IN PROGRESS'";
				logger.info("[getOperatorsIdForNotification]hours : " + hours + " query : " + query);
			}
			if (hours == 22) {
				query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk,bh.location_code from SSD_BYPASS_HEADER bh"
						+ " where bh.BYPASS_START_TIME > TO_TIMESTAMP('" + sdf.format(cal2.getTime())
						+ "' ) and bh.BYPASS_START_TIME <= TO_TIMESTAMP('" + sdf.format(cal1.getTime())
						+ "') and (bh.SEVERITY = 'regular' or bh.risk = 'MEDIUM' or bh.risk = 'LOW') and BYPASS_STATUS = 'IN PROGRESS'";
				logger.info("[getOperatorsIdForNotification]hours : " + hours + " query : " + query);
			}
			logger.error("[getOperatorsIdForNotification]hours : query : " + query);
			List<Object[]> objects = session.createSQLQuery(query).list();
			for (Object opr[] : objects) {
				// Check for severity(EFS)/risk(Canada) and change shift of foreman according to country
				String severity = null;
				LocalTime start = null, stop = null;
				String loc_code = ServicesUtil.isEmpty(opr[4]) ? null : (String)opr[4];
				// EFS
				if(!ServicesUtil.isEmpty(((String) opr[2]))){
					severity = ((String) opr[2]);
					// Start and stop time is in UTC which is equivalent to 6AM - 4PM CST
					start = LocalTime.parse("12:00:00");
					stop = LocalTime.parse("22:00:00");
				}
				// Canada
				else if(!ServicesUtil.isEmpty(((String) opr[3]))){
					severity = ((String) opr[3]);
					String base = ServicesUtil.getLocationByLocationCode(loc_code);
					// Start and stop time is in UTC which is equivalent to 8AM - 4PM PST for Kaybob
					if(base.equals(MurphyConstant.KAY_BASE_LOC_CODE)){
						start = LocalTime.parse("16:00:00");
						stop = LocalTime.parse("00:00:00");
					}
					// Start and stop time is in UTC which is equivalent to 8AM - 4PM MST for Montney
					else{
						start = LocalTime.parse("15:00:00");
						stop = LocalTime.parse("23:00:00");
					}
				}
				if(!ServicesUtil.isEmpty(severity)){
					if (severity.equalsIgnoreCase("regular") || severity.equalsIgnoreCase("MEDIUM") || 
							severity.equalsIgnoreCase("LOW")) {
						String currentTime = new SimpleDateFormat("kk:mm:ss").format(new Date());
						LocalTime target = LocalTime.parse(currentTime);
						logger.error("Foreman shift hours: " + start + " , to " + stop + " target : " + target);
						if (target.isAfter(start) && target.isBefore(stop)) {
							bypassId = (String) opr[0];
							operatorId = (String) opr[1];
							operatorsMap.put(bypassId, operatorId);
							System.err.println("bypassId: " + bypassId + " , operatorId: " + operatorId);
	
						}
	
					} else {
						bypassId = (String) opr[0];
						operatorId = (String) opr[1];
						operatorsMap.put(bypassId, operatorId);
						System.err.println("bypassId: " + bypassId + " , operatorId: " + operatorId);
	
					}
				}

				this.getSession().flush();
			}
			logger.error("operator details: " + operatorsMap);

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][bypassLogListForEscalation][error]" + e.getMessage());

		}
		return operatorsMap;

	}

	public Map<String, String> getManagerDetails(String operatorId, String level) {
		Map<String, String> managerDetails = new LinkedHashMap<>();
		String managerId = "";
		String managerName = "";

		try {
			String query = "";
			if (!ServicesUtil.isEmpty(operatorId)) {
				if (level.equalsIgnoreCase("foreman")) {
					// query = "select manager_id , manager_name from EMP_INFO
					// where emp_id = (select manager_id from EMP_INFO where
					// emp_id = '"+operatorId+"')";
					query = "select foreman_id , foreman_name from emp_info where emp_id = '" + operatorId + "'";
				} else if (level.equalsIgnoreCase("superintendent")) {
					// query = "select manager_id , manager_name from EMP_INFO
					// where emp_id = (select manager_id from EMP_INFO where
					// emp_id = (select manager_id from EMP_INFO where emp_id =
					// '"+operatorId+"'))";
					query = "select superintendent_id , superintendent_name from emp_info where emp_id = '" + operatorId
							+ "'";
				} /*
					 * else { //query =
					 * "select manager_id , manager_name from EMP_INFO where emp_id = '"
					 * + operatorId + "'"; }
					 */
				List<Object[]> objects = this.getSession().createSQLQuery(query).list();
				for (Object[] obj : objects) {
					managerId = (String) obj[0];
					managerName = (String) obj[1];
					managerDetails.put(managerId, managerName);
				}
				this.getSession().flush();

			}
			logger.info("Manager details: " + managerDetails.toString());

		} catch (

		Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getManagerId][error]" + e.getMessage());

		}
		return managerDetails;
	}

	public String sendNotificationForShiftChange(String zone) {
		List<String> requiredRocIdList = new ArrayList<String>();
		List<String> locationList = new ArrayList<String>();
		String response = "failure";
		String bypassId = "";
		String activityLogId = "";
		String module = "BypassLog";
		String locationText = "";
		String isAcknowledged = "false";
		String userGroup = "";
		String userId = "";
		String activityType = "ShiftChange";
		String loc = "";
		String locationCode = "";
		String bypassNum = "";
		String severity = "";
		// String locationType = "";

		try {
			ssdBypassActivityLogDao.updateShiftChangeAcknowledged(zone);
			List<String> locations = new ArrayList<>();
			Session session = this.getSession();
			// Transaction transaction = session.beginTransaction();
			String loc_code_like = "";
			if(zone.equals("CST"))
				loc_code_like = "MUR-US%'";
			else if(zone.equals("PST"))
				loc_code_like = "MUR-CA-KAY%'";
			else if(zone.equals("MST"))
				loc_code_like = "MUR-CA-MTM%' or location_code like 'MUR-CA-MTW%'";
			
			String query = "select bh.SSD_BYPASS_ID,  bh.LOCATION_CODE , bh.LOCATION , bh.OPERATOR_ID , bh.SSD_BYPASS_NUMBER , bh.SEVERITY, "
					+ "bh.RISK from SSD_BYPASS_HEADER bh where BYPASS_STATUS = 'IN PROGRESS' and location_code like '" + loc_code_like;
			List<Object[]> objects = session.createSQLQuery(query).list();
			for (Object[] obj : objects) {
				SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();
				activityLogId = UUID.randomUUID().toString().replaceAll("-", "");
				ssdBypassActivityLogDto.setSsdBypassLogId(activityLogId);
				bypassId = (String) obj[0];
				ssdBypassActivityLogDto.setSsdBypassId(bypassId);
				ssdBypassActivityLogDto.setActivityType(activityType);
				ssdBypassActivityLogDto.setBypassStatusReviewedAt(new Date());
				ssdBypassActivityLogDto.setIsApprovalObtained(true);
				ssdBypassActivityLogDto.setOperatorType("ROC");

				List<String> locationCodes = new ArrayList<>();
				locationCodes.add((String) obj[1]);
				loc = getLocationByLocationCode(locationCodes);
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
				else if ("Montney".equalsIgnoreCase(loc.trim())){
					userGroup = "IOP_TM_ROC_Montney";
				}
				else if("Kaybob".equalsIgnoreCase(loc.trim())){
					userGroup = "IOP_TM_ROC_Kaybob";
				}

				String rocId = "ROC_" + loc;
				ssdBypassActivityLogDto.setPersonId(rocId);
				ssdBypassActivityLogDto.setPersonResponsible(loc + " Field ROC");

				// ssdBypassActivityLogDao.create(ssdBypassActivityLogDto);
				locationText = (String) obj[2];
				userId = (String) obj[3];
				bypassNum = (String) obj[4];
				locationCode = (String) obj[1];
				// severity for EFS
				if(!ServicesUtil.isEmpty((String) obj[5]))
					severity = (String) obj[5];
				// risk for Canada
				else if(!ServicesUtil.isEmpty((String) obj[6])) 
					severity = (String) obj[6];

				int row = insertDataForPushNotificationToRoc(bypassId, activityLogId, module, locationText,
						isAcknowledged, userGroup, userId, activityType, locationCode, severity, bypassNum);

				System.err.println(row + bypassId + "," + activityLogId + "," + module + "," + locationText + ","
						+ isAcknowledged + "," + userGroup + "," + userId + "," + activityType);

			}
			response = "success";

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][sendNotificationForShiftChange][error]" + e.getMessage());

		}
		return response;

	}

	public int insertDataForPushNotificationToRoc(String objectId, String activityLogId, String module,
			String locationText, String isAcknowledged, String userGroup, String userId, String status,
			String locationCode, String severity, String bypassNum) {
		int response = 0;
		String locationType = "";
		String bypassNumber = "";
		try {
			if ((!ServicesUtil.isEmpty(objectId)) && (!ServicesUtil.isEmpty(activityLogId))
					&& (!ServicesUtil.isEmpty(userGroup))) {
				bypassNumber = String.valueOf(Long.parseLong(bypassNum) % 1000000);
				locationType = (String) this.getSession().createSQLQuery(
						"select location_type from production_location where location_code = '" + locationCode + "'")
						.uniqueResult();
				String query = "INSERT INTO safety_app_notification VALUES( '" + objectId + "' , '" + activityLogId
						+ "' , '" + module + "' , '" + locationText + "' , '" + isAcknowledged + "' , '" + userGroup
						+ "' , '" + userId + "' , '" + status + "' , '' , null  , '" + locationCode + "' , '"
						+ locationType + "' , '" + severity + "' , '" + bypassNumber + "')";

				response = this.getSession().createSQLQuery(query).executeUpdate();
			}

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][insertDataForPushNotificationToRoc][error]" + e.getMessage());

		}

		return response;

	}

	public String getBypassField(String bypassId, String fieldName) {
		String fieldValue = "";
		try {

			fieldValue = (String) this.getSession()
					.createSQLQuery(
							"select " + fieldName + " from ssd_bypass_header where SSD_BYPASS_ID = '" + bypassId + "'")
					.uniqueResult();
			this.getSession().flush();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getBypassNum][error]" + e.getMessage());

		}
		return fieldValue;
	}

	public String getLocationByLocationCode(List<String> locationCodes) {
		String loc = "";
		try {
			for (String locCode : locationCodes) {
				if (!ServicesUtil.isEmpty(locCode)) {
					String queryForLocType = "select location_type from production_location where location_code = '"
							+ locCode + "'";
					String locType = (String) this.getSession().createSQLQuery(queryForLocType).uniqueResult();
					FieldResponseDto fieldResponseDto = locDao.getFieldText(locationCodes, locType);
					loc = fieldResponseDto.getField();
				}
			}
			this.getSession().flush();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getBypassId][error]" + e.getMessage());

		}
		return loc;

	}

	public void updateEscalationInfoInBypassHeader(String bypassId, String status, String initBy,
			String escalationLevel) {
		try {
			String query = "";
			if (escalationLevel.equalsIgnoreCase("first")) {
				query = "update ssd_bypass_header set first_line_supv_approval_status = '" + status
						+ "' where ssd_bypass_id  = '" + bypassId + "'";
			}

			if (escalationLevel.equalsIgnoreCase("second")) {
				query = "update ssd_bypass_header set field_plant_supt_approval_status = '" + status
						+ "' where ssd_bypass_id  = '" + bypassId + "'";
			}

			this.getSession().createSQLQuery(query).executeUpdate();

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][updateEscalationInfoInBypassHeader][error]" + e.getMessage());

		}
	}

	public int updateOperatorResponse(String activityLogId, boolean responseValue) {
		int response = 0;
		try {
			String updateQuery = "update ssd_bypass_activity_log set IS_APPROVAL_OBTAINED = " + responseValue
					+ " where SSD_BYPASS_LOG_ID = '" + activityLogId + "'";
			response = this.getSession().createSQLQuery(updateQuery).executeUpdate();
			SsdBypassActivityLogDto ssdBypassActivityLogDto = ssdBypassActivityLogDao.getActivityLogById(activityLogId);
			if (ssdBypassActivityLogDto.getActivityType().equalsIgnoreCase("ASSIGNED") && responseValue == true) {
				this.getSession()
						.createSQLQuery(
								"update ssd_bypass_header set SHIFT_CHANGE_ACKNOWLEDGED = true where SSD_BYPASS_ID = '"
										+ ssdBypassActivityLogDto.getSsdBypassId() + "'")
						.executeUpdate();
			}

			if (ssdBypassActivityLogDto.getActivityType().equalsIgnoreCase("ASSIGNED") && responseValue == false) {
				String updateQueryheader = "update ssd_bypass_header set SHIFT_CHANGE_ACKNOWLEDGED = " + Boolean.FALSE 
						+ " where SSD_BYPASS_ID = '" + ssdBypassActivityLogDto.getSsdBypassId() + "'";
				int result = this.getSession().createSQLQuery(updateQueryheader).executeUpdate();
				
				logger.error("[Murphy][SsdBypassHeaderDao][updateOperatorRsponse][updateQueryheadertable] " 
				  + updateQueryheader + " result : " + result);
			}

			String status = "";
			String escalationLevel = "";
			if (responseValue == false) {
				status = "denied";
			} else if (responseValue == true) {
				status = "approved";
			}
			if (ssdBypassActivityLogDto.getOperatorType().equalsIgnoreCase("foreman")) {
				escalationLevel = "first";
			}
			if (ssdBypassActivityLogDto.getOperatorType().equalsIgnoreCase("superintendent")) {
				escalationLevel = "second";
			}
			if (ssdBypassActivityLogDto.getActivityType().equalsIgnoreCase("escalation")) {
				updateEscalationInfoInBypassHeader(ssdBypassActivityLogDto.getSsdBypassId(), status,
						ssdBypassActivityLogDto.getPersonResponsible(), escalationLevel);
			}

			this.getSession().flush();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][updateOperatorRsponse][error]" + e.getMessage());

		}
		return response;
	}

	public List<SsdBypassListDto> getBypassLogListByUserGroup(String technicalRole, String businessRole) {
		List<SsdBypassListDto> ssdBypassListDtoList = new ArrayList<>();
		SsdBypassListDto ssdBypassListDto = null;
		try {
			Session session = this.getSession();
			String technicalRoles = ServicesUtil.getStringForInQuery(technicalRole);
			String businessRoles = ServicesUtil.getStringForInQuery(businessRole);
			String fieldQuery = "select distinct field from tm_role_mapping where businesserole in (" + businessRoles
					+ ") and technicalrole in (" + technicalRoles + ")";
			logger.error("[getBypassLogListByUserGroup] Field query: " + fieldQuery);
			// String updateQuery = "update ssd_bypass_header set
			// SHIFT_CHANGE_ACKNOWLEDGED = false where BYPASS_STATUS = 'IN
			// PROGRESS'";
			// logger.error("Update query: " + updateQuery);
			// session.createSQLQuery(updateQuery).executeUpdate();
			List<Object> objects = session.createSQLQuery(fieldQuery).list();
			for (Object object : objects) {

				String field = (String) object;
				// Handling locations under Montney
				if(field.equalsIgnoreCase("MUR-CA-MNT"))
					field = "MUR-CA-MTM%' or location_code like 'MUR-CA-MTW";
				String locationCodesQuery = "select location_code from production_location where location_code like '"
						+ field.trim() + "%'";
				logger.error("[getBypassLogListByUserGroup] Location code query: " + locationCodesQuery);
				
				List<Object> locationCodeListObjects = session.createSQLQuery(locationCodesQuery).list();
				List<String> locationCodeList = new ArrayList<>();
				for (Object obj : locationCodeListObjects) {
					locationCodeList.add(((String) obj).trim());
				}
				if (!ServicesUtil.isEmpty(locationCodeList)) {
					String locationCodes = ServicesUtil.getStringFromList(locationCodeList);
					String query = "select SSD_BYPASS_ID , SSD_BYPASS_NUMBER , DEVICE_BYPASSED , REASON_FOR_BYPASS , BYPASS_START_TIME , BYPASS_STATUS , LOCATION , LOCATION_CODE,"
							+ " EQUIPMENT_DESC from SSD_BYPASS_HEADER where LOCATION_CODE in ( " + locationCodes
							+ " ) and BYPASS_STATUS = '" + MurphyConstant.INPROGRESS
							+ "' and SHIFT_CHANGE_ACKNOWLEDGED = false ORDER BY BYPASS_START_TIME DESC";
					
					List<Object[]> bypassLogFields = session.createSQLQuery(query).list();

					for (Object[] bypassLogField : bypassLogFields) {
						String bypassId = (String) bypassLogField[0];
						String innerQuery = "select PERSON_RESPONSIBLE from ssd_bypass_activity_log where ssd_bypass_id = '"
								+ bypassId
								+ "' and activity_type='ASSIGNED' and is_approval_obtained = true order by bypass_status_reviewed_at desc limit 1 ";
						String assignedTo = (String) session.createSQLQuery(innerQuery).uniqueResult();
						ssdBypassListDto = new SsdBypassListDto();
						ssdBypassListDto.setSsdBypassId(bypassId);
						ssdBypassListDto
								.setSsdBypassNum(String.valueOf(Long.parseLong((String) bypassLogField[1]) % 1000000));
						ssdBypassListDto.setDeviceBypassed(ServicesUtil.isEmpty(bypassLogField[2]) ? null : (String) bypassLogField[2]);
						ssdBypassListDto.setReasonForBypass((String) bypassLogField[3]);
						ssdBypassListDto.setBypassStartTime((Date) bypassLogField[4]);
						// status = (String) bypassLogField[5];
						ssdBypassListDto.setBypassStatus((String) bypassLogField[5]);
						ssdBypassListDto.setLocation((String) bypassLogField[6]);
						ssdBypassListDto.setLocationCode((String) bypassLogField[7]);
						ssdBypassListDto.setEquipment_desc(ServicesUtil.isEmpty(bypassLogField[8]) ? null : (String) bypassLogField[8]);
						ssdBypassListDto.setAssignedTo(assignedTo);
						ssdBypassListDto.setSource("Bypass Log");
						ssdBypassListDtoList.add(ssdBypassListDto);

					}

					// Energy Isolation appended with this. START.
					query = "select FORM_ID, ID, PERM_ISSUE_NAME, REASON, CREATED_AT, STATUS, LOCATION_NAME, LOCATION_ID"
							+ " from EI_FORM where LOCATION_ID in ( " + locationCodes + " ) "
							+ " and STATUS = 'IN PROGRESS' and IS_ACKNOWLEDGED = False ORDER BY CREATED_AT DESC";
					logger.error("[getBypassLogListByUserGroup] Location codes: " + locationCodes);

					bypassLogFields = session.createSQLQuery(query).list();

					for (Object[] bypassLogField : bypassLogFields) {
						ssdBypassListDto = new SsdBypassListDto();
						ssdBypassListDto.setSsdBypassId(
								ServicesUtil.isEmpty(bypassLogField[0]) ? null : (String) bypassLogField[0]);
						String idTemp = ServicesUtil.isEmpty(bypassLogField[1]) ? null : (String) bypassLogField[1];
						if (idTemp != null) {
							idTemp = idTemp.substring(idTemp.length() - 6);
						}
						ssdBypassListDto.setSsdBypassNum(idTemp);
						ssdBypassListDto.setAssignedTo(
								ServicesUtil.isEmpty(bypassLogField[2]) ? null : (String) bypassLogField[2]);
						ssdBypassListDto.setReasonForBypass(
								ServicesUtil.isEmpty(bypassLogField[3]) ? null : (String) bypassLogField[3]);
						ssdBypassListDto.setBypassStartTime(
								ServicesUtil.isEmpty(bypassLogField[4]) ? null : (Date) bypassLogField[4]);
						ssdBypassListDto.setBypassStatus(
								ServicesUtil.isEmpty(bypassLogField[5]) ? null : (String) bypassLogField[5]);
						ssdBypassListDto.setLocation(
								ServicesUtil.isEmpty(bypassLogField[6]) ? null : (String) bypassLogField[6]);
						ssdBypassListDto.setLocationCode(
								ServicesUtil.isEmpty(bypassLogField[7]) ? null : (String) bypassLogField[7]);
						ssdBypassListDto.setSource("Energy Isolation");
						ssdBypassListDtoList.add(ssdBypassListDto);

					}
					// Energy Isolation. END.
				}
			}
			session.flush();

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getBypassLogListByUserGroup][error]" + e.getMessage());

		}
		return ssdBypassListDtoList;

	}

	public List<String> getApproversList(String bypassId) {
		List<String> approversIdList = new ArrayList<>();
		try {
			String query = "select PERSON_ID from ssd_bypass_activity_log where SSD_BYPASS_ID = '" + bypassId
					+ "' and ACTIVITY_TYPE = 'escalation'";
			approversIdList = this.getSession().createSQLQuery(query).list();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getApproversList][error]" + e.getMessage());

		}
		return approversIdList;
	}

	public String getAssignedPersonId(String bypassId) {
		String assignedPersonId = "";
		try {
			String innerQuery = "select PERSON_ID from ssd_bypass_activity_log where ssd_bypass_id = '" + bypassId
					+ "' and activity_type='ASSIGNED' and is_approval_obtained = true order by bypass_status_reviewed_at desc limit 1 ";
			assignedPersonId = (String) this.getSession().createSQLQuery(innerQuery).uniqueResult();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getAssignedPersonId][error]" + e.getMessage());
		}

		return assignedPersonId;
	}

	public String getUserLoginByPid(String pid) {
		String userLoginName = "";
		try {
			String query = "select user_login_name from tm_user_idp_mapping where p_id = '" + pid + "'";
			userLoginName = (String) this.getSession().createSQLQuery(query).uniqueResult();

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getUserLoginByPid][error]" + e.getMessage());
		}
		return userLoginName;
	}

	public Map<String, String> outShiftNotificationOperatorIds(String notificationTime, int hours,String zone) {
		Map<String, String> operatorsMap = new LinkedHashMap<>();
		String bypassId = "";
		String operatorId = "";

		try {
			Session session = this.getSession();
			String query = "";
			
			// location like query on basis of timezone
			String loc_code_like = "";
			if(zone.equals("CST"))
				loc_code_like = "MUR-US%'";
			else if(zone.equals("PST"))
				loc_code_like = "MUR-CA-KAY%'";
			else if(zone.equals("MST"))
				loc_code_like = "MUR-CA-MTM%' or bh.location_code like 'MUR-CA-MTW%'";
			
			if (notificationTime.equalsIgnoreCase("morning")) {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
				Calendar cal1 = Calendar.getInstance();
				cal1.add(Calendar.HOUR, -14);
				Calendar cal2 = Calendar.getInstance();
				// cal2.add(Calendar.MINUTE, -((hours * 60) + 5));
				
				if (hours == 12) {
					query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk from SSD_BYPASS_HEADER bh inner join SSD_BYPASS_ACTIVITY_LOG al on bh.SSD_BYPASS_ID = al.SSD_BYPASS_ID "
							+ " where ADD_SECONDS(bh.BYPASS_START_TIME , 12*60*60) >= TO_TIMESTAMP('"
							+ sdf.format(cal1.getTime())
							+ "') and ADD_SECONDS(bh.BYPASS_START_TIME , 12*60*60) < TO_TIMESTAMP('"
							+ sdf.format(cal2.getTime())
							+ "') and BYPASS_STATUS = 'IN PROGRESS' and (bh.SEVERITY = 'regular' or bh.risk = 'MEDIUM' or bh.risk = 'LOW') and "
							+ "bh.location_code like '" + loc_code_like + " and "
							+ "(select count(*) from SSD_BYPASS_activity_log where ssd_bypass_id =  bh.SSD_BYPASS_ID and operator_type = 'foreman')=0";
					logger.info("query: " + hours + query);
				}
				if (hours == 24) {
					query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk from SSD_BYPASS_HEADER bh inner join SSD_BYPASS_ACTIVITY_LOG al on bh.SSD_BYPASS_ID = al.SSD_BYPASS_ID "
							+ " where ADD_SECONDS(bh.BYPASS_START_TIME , 24*60*60) >= TO_TIMESTAMP('"
							+ sdf.format(cal1.getTime())
							+ "') and ADD_SECONDS(bh.BYPASS_START_TIME , 24*60*60) < TO_TIMESTAMP('"
							+ sdf.format(cal2.getTime())
							+ "') and BYPASS_STATUS = 'IN PROGRESS' and (bh.SEVERITY = 'regular' or bh.risk = 'MEDIUM' or bh.risk = 'LOW') and "
							+ "bh.location_code like '" + loc_code_like + " and "
							+ "(select count(*) from SSD_BYPASS_activity_log where ssd_bypass_id =  bh.SSD_BYPASS_ID and operator_type = 'superintendent')=0";

					logger.info("query: " + hours + query);
				}
				logger.error("[outShiftNotificationOperatorIds]query: " + query);
				List<Object[]> objects = session.createSQLQuery(query).list();
				for (Object opr[] : objects) {

					bypassId = (String) opr[0];
					operatorId = (String) opr[1];
					operatorsMap.put(bypassId, operatorId);
					System.err.println("bypassId: " + bypassId + " , operatorId: " + operatorId);
				}

			} else if (notificationTime.equalsIgnoreCase("evening")) {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
				Calendar cal1 = Calendar.getInstance();
				cal1.set(Calendar.HOUR_OF_DAY, 10);
				cal1.set(Calendar.MINUTE, 0);
				cal1.set(Calendar.SECOND, 0);
				Calendar cal2 = Calendar.getInstance();
				cal2.set(Calendar.HOUR_OF_DAY, 22);
				cal2.set(Calendar.MINUTE, 0);
				cal2.set(Calendar.SECOND, 0);
				// cal2.add(Calendar.MINUTE, -((hours * 60) + 5));
				if (hours == 12) {
					query = "select bh.SSD_BYPASS_ID , bh.OPERATOR_ID , bh.SEVERITY, bh.risk from SSD_BYPASS_HEADER bh"
							+ " where bh.BYPASS_START_TIME >= TO_TIMESTAMP('" + sdf.format(cal1.getTime())
							+ "') and bh.BYPASS_START_TIME < TO_TIMESTAMP('" + sdf.format(cal2.getTime())
							+ "') and BYPASS_STATUS = 'IN PROGRESS' and (bh.SEVERITY = 'regular' or bh.risk = 'MEDIUM' or bh.risk = 'LOW') "
							+ "and bh.location_code like '" + loc_code_like ;
					logger.info("query: " + hours + query);
				}
				logger.error("[outShiftNotificationOperatorIds]query: " + query);
				List<Object[]> objects = session.createSQLQuery(query).list();
				for (Object opr[] : objects) {

					bypassId = (String) opr[0];
					operatorId = (String) opr[1];
					operatorsMap.put(bypassId, operatorId);
					System.err.println("bypassId: " + bypassId + " , operatorId: " + operatorId);
				}

			}

			this.getSession().flush();

			logger.error("operator details: " + operatorsMap);

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][bypassLogListForEscalation][error]" + e.getMessage());

		}
		return operatorsMap;

	}
	
	public ArrayList<String> getLocationHierarchy(List<String> locCodes , ArrayList<String> locCodeList){
		try{
			locCodeList.addAll(locCodes);
			ArrayList<String> hierarchyLocCodes = new ArrayList<>();
			String locationCodes = ServicesUtil.getStringFromList(locCodes);
				String queryString = "select location_type , location_code from production_location where parent_code in ("+locationCodes+")";
				List<Object[]> locDetailsList = this.getSession().createSQLQuery(queryString).list();
				for(Object obj[]:locDetailsList){
					hierarchyLocCodes.add((String)obj[1]);
				}
				
				String locType = (String)locDetailsList.get(0)[0];
				if(!(locType.equalsIgnoreCase(MurphyConstant.WELL))){
					getLocationHierarchy(hierarchyLocCodes , locCodeList);
				}else{
					locCodeList.addAll(hierarchyLocCodes);
				}
			
		}catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getLocationHierarchy][error]" + e.getMessage());

		}
		return locCodeList;
	}
	
	public List<BypassRiskLevelDto> getRiskLevelList(){
		List<BypassRiskLevelDto> listDto = new ArrayList<>();
		try{
			String queryString = "SELECT risk_level , active_flag FROM RISK_LEVEL_FOR_BYPASS where"
					+ " ACTIVE_FLAG = '" + MurphyConstant.ACTIVE + "'";
			List<Object[]> objects = this.getSession().createSQLQuery(queryString).list();
			
			for(Object obj[]:objects){
				BypassRiskLevelDto bypassRiskLevelDto = new BypassRiskLevelDto();
				bypassRiskLevelDto.setRiskLevel(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				bypassRiskLevelDto.setActiveFlag((ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]));
				
				listDto.add(bypassRiskLevelDto);
			}
		}
		catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getRiskLevelList][error]" + e.getMessage());

		}		
		return listDto;		
	}

}
