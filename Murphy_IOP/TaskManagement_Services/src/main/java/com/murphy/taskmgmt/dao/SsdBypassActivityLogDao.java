/**
 * 
 */
package com.murphy.taskmgmt.dao;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.SsdBypassActivityLogDto;
import com.murphy.taskmgmt.entity.SsdBypassActivityLogDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

/**
 * @author Kamlesh.Choubey
 *
 */

@Repository("ssdBypassActivityLogDao")
public class SsdBypassActivityLogDao extends BaseDao<SsdBypassActivityLogDo, SsdBypassActivityLogDto> {

	private static final Logger logger = LoggerFactory.getLogger(SsdBypassActivityLogDao.class);

	/*
	 * @Autowired SsdBypassHeaderDao ssdBypassHeaderDao;
	 */

	@Override
	protected SsdBypassActivityLogDo importDto(SsdBypassActivityLogDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SsdBypassActivityLogDo entity = new SsdBypassActivityLogDo();

		if (!ServicesUtil.isEmpty(fromDto.getSsdBypassLogId()))
			entity.setSsdBypassLogId(fromDto.getSsdBypassLogId());

		if (!ServicesUtil.isEmpty(fromDto.getSsdBypassId()))
			entity.setSsdBypassId(fromDto.getSsdBypassId());

		if (!ServicesUtil.isEmpty(fromDto.getPersonResponsible()))
			entity.setPersonResponsible(fromDto.getPersonResponsible());

		if (!ServicesUtil.isEmpty(fromDto.getBypassStatusReviewedAt()))
			entity.setBypassStatusReviewedAt(fromDto.getBypassStatusReviewedAt());

		if (!ServicesUtil.isEmpty(fromDto.getOperatorType()))
			entity.setOperatorType(fromDto.getOperatorType());

		if (!ServicesUtil.isEmpty(fromDto.getIsApprovalObtained()))
			entity.setIsApprovalObtained(fromDto.getIsApprovalObtained());
		if (!ServicesUtil.isEmpty(fromDto.getPersonId()))
			entity.setPersonId(fromDto.getPersonId());

		if (!ServicesUtil.isEmpty(fromDto.getActivityType()))
			entity.setActivityType(fromDto.getActivityType());

		return entity;
	}

	@Override
	protected SsdBypassActivityLogDto exportDto(SsdBypassActivityLogDo entity) {

		SsdBypassActivityLogDto dto = new SsdBypassActivityLogDto();

		if (!ServicesUtil.isEmpty(entity.getSsdBypassLogId()))
			dto.setSsdBypassLogId(entity.getSsdBypassLogId());

		if (!ServicesUtil.isEmpty(entity.getSsdBypassId()))
			dto.setSsdBypassId(entity.getSsdBypassId());

		if (!ServicesUtil.isEmpty(entity.getPersonResponsible()))
			dto.setPersonResponsible(entity.getPersonResponsible());

		if (!ServicesUtil.isEmpty(entity.getBypassStatusReviewedAt()))
			dto.setBypassStatusReviewedAt(entity.getBypassStatusReviewedAt());

		if (!ServicesUtil.isEmpty(entity.getOperatorType()))
			dto.setOperatorType(entity.getOperatorType());

		if (!ServicesUtil.isEmpty(entity.getIsApprovalObtained()))
			dto.setIsApprovalObtained(entity.getIsApprovalObtained());

		if (!ServicesUtil.isEmpty(entity.getPersonId()))
			dto.setPersonId(entity.getPersonId());

		if (!ServicesUtil.isEmpty(entity.getActivityType()))
			dto.setActivityType(entity.getActivityType());

		return dto;
	}

	/*
	 * public List<SsdBypassActivityLogDto>
	 * exportDtoList(List<SsdBypassActivityLogDo> entities) {
	 * List<SsdBypassActivityLogDto> ssdBypassActivityLogDtoList = new
	 * ArrayList<>(); for (SsdBypassActivityLogDo entity : entities) {
	 * ssdBypassActivityLogDtoList.add(exportDto(entity)); } return
	 * ssdBypassActivityLogDtoList; }
	 */

	public String createBypassActivityLog(SsdBypassActivityLogDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			create(dto);
			 if (dto.getActivityType().equalsIgnoreCase("ASSIGNED")) {
		this.getSession().createSQLQuery("update ssd_bypass_header set SHIFT_CHANGE_ACKNOWLEDGED = true where SSD_BYPASS_ID = '" + dto.getSsdBypassId() + "'").executeUpdate();
			 }
			reponse = MurphyConstant.SUCCESS;
			this.getSession().flush();
			// this.getSession().close();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassActivityLogDao][createBypassActivityLog][error]" + e.getMessage());

		}
		return reponse;

	}

	public Boolean checkOperatorResponse(String activityLogId) {
		Boolean response = false;
		try {
			response = (Boolean) this.getSession()
					.createSQLQuery(
							"select IS_APPROVAL_OBTAINED from ssd_bypass_activity_log where SSD_BYPASS_LOG_ID = '"
									+ activityLogId + "'")
					.uniqueResult();
			this.getSession().flush();
		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][checkOperatorResponse][error]" + e.getMessage());

		}
		return response;
	}

	/*
	 * public int updateOperatorResponse(String activityLogId, boolean
	 * responseValue) { int response = 0; try { String updateQuery =
	 * "update ssd_bypass_activity_log set IS_APPROVAL_OBTAINED = " +
	 * responseValue + " where SSD_BYPASS_LOG_ID = '" + activityLogId + "'";
	 * response = this.getSession().createSQLQuery(updateQuery).executeUpdate();
	 * SsdBypassActivityLogDto ssdBypassActivityLogDto =
	 * getActivityLogById(activityLogId); String status = ""; String
	 * escalationLevel = ""; if (responseValue == false) { status = "denied"; }
	 * else if (responseValue == false) { status = "approved"; } if
	 * (ssdBypassActivityLogDto.getOperatorType().equalsIgnoreCase("foreman")) {
	 * escalationLevel = "first"; } if
	 * (ssdBypassActivityLogDto.getOperatorType().equalsIgnoreCase(
	 * "superitendent")) { escalationLevel = "second"; } if
	 * (ssdBypassActivityLogDto.getActivityType().equalsIgnoreCase("escalation")
	 * ) { ssdBypassHeaderDao.updateEscalationInfoInBypassHeader(
	 * ssdBypassActivityLogDto.getSsdBypassId(), status,
	 * ssdBypassActivityLogDto.getPersonResponsible(), escalationLevel); }
	 * 
	 * this.getSession().flush(); } catch (Exception e) {
	 * logger.error("[Murphy][SsdBypassHeaderDao][updateOperatorRsponse][error]"
	 * + e.getMessage());
	 * 
	 * } return response; }
	 */
	public SsdBypassActivityLogDto getActivityLogById(String activityLogId) {
		SsdBypassActivityLogDto ssdBypassActivityLogDto = new SsdBypassActivityLogDto();
		try {
			ssdBypassActivityLogDto = exportDto(
					(SsdBypassActivityLogDo) (this.getSession().createCriteria(SsdBypassActivityLogDo.class)
							.add(Restrictions.eq("ssdBypassLogId", activityLogId)).uniqueResult()));

		} catch (Exception e) {
			logger.error("[Murphy][SsdBypassHeaderDao][getActivityLogById][error]" + e.getMessage());

		}

		return ssdBypassActivityLogDto;

	}
	
	 @Transactional(value=TxType.REQUIRES_NEW)
	 public void updateShiftChangeAcknowledged(String zone){
		 try {
			String loc_code_like = "";
			if(zone.equals("CST"))
				loc_code_like = "MUR-US%'";
			else if(zone.equals("PST"))
				loc_code_like = "MUR-CA-KAY%'";
			else if(zone.equals("MST"))
				loc_code_like = "MUR-CA-MTM%' or location_code like 'MUR-CA-MTW%'";
			String updateQuery = "update ssd_bypass_header set SHIFT_CHANGE_ACKNOWLEDGED = false where BYPASS_STATUS = 'IN PROGRESS' "
					+ "and location_code like '" + loc_code_like;
			this.getSession().createSQLQuery(updateQuery).executeUpdate();
		 }catch(Exception e){
			 logger.error("[Murphy][SsdBypassHeaderDao][updateShiftChangeAcknowledged][error]" + e.getMessage());
		 }
		 
	 }
	 

}
