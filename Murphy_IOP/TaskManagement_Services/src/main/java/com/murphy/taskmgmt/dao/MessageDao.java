package com.murphy.taskmgmt.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.MessageDto;
import com.murphy.taskmgmt.entity.MessageDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("MessageDao")
@Transactional
public class MessageDao extends BaseDao<MessageDo, MessageDto> {

	private static final Logger logger = LoggerFactory.getLogger(MessageDao.class);

	@Override
	protected MessageDo importDto(MessageDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		MessageDo entity = new MessageDo();
		entity.setMessageId(fromDto.getMessageId());
		entity.setCreatedBy(fromDto.getCreatedBy());
		entity.setCreatedAt(new Date(fromDto.getCreatedAt()));
		entity.setLocationCode(fromDto.getLocationCode());
		entity.setCurrentOwner(fromDto.getCurrentOwner());
		entity.setUpdatedAt(new Date(fromDto.getUpdatedAt()));
		entity.setStatus(fromDto.getStatus());
		entity.setMessage(fromDto.getMessage());
		entity.setCountry(fromDto.getCountry());
		entity.setConversationId(fromDto.getConversationId());
		entity.setTeamsChannelId(fromDto.getTeamsChannelId());
		entity.setTeamsTeamId(fromDto.getTeamsTeamId());
		entity.setComment(fromDto.getComment());
		return entity;
	}

	@Override
	protected MessageDto exportDto(MessageDo entity) {
		MessageDto dto = new MessageDto();
		dto.setMessageId(entity.getMessageId());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		dto.setLocationCode(entity.getLocationCode());
		dto.setCurrentOwner(entity.getCurrentOwner());
		dto.setUpdatedAt(entity.getUpdatedAt().getTime());
		dto.setStatus(entity.getStatus());
		dto.setMessage(entity.getMessage());
		dto.setCountry(entity.getCountry());
		dto.setConversationId(entity.getConversationId());
		dto.setTeamsChannelId(entity.getTeamsChannelId());
		dto.setTeamsTeamId(entity.getTeamsTeamId());
		dto.setComment(entity.getComment());
		return dto;
	}

	@Override
	public MessageDto getByKeys(MessageDto dto) throws NoResultFault {
		MessageDo entity = (MessageDo) getSession().get(MessageDo.class, dto.getMessageId());
		if (!ServicesUtil.isEmpty(entity)) {
			dto = exportDto(entity);
		} else {
			throw new NoResultFault("MessageDao.getByKeys()::No Records found for the key id::" + dto.getMessageId());
		}
		return dto;
	}

	public String save(MessageDto dto) {
		String response = MurphyConstant.FAILURE;
		try {
			saveOrUpdate(importDto(dto));
			response = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][MessageDao][save][error] " + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<MessageDto> getAllActiveMessagesCreatedBy(String user) {
		List<MessageDto> messageDtos = new ArrayList<MessageDto>();

		String queryString = "SELECT MESSAGE_ID, CREATED_BY, CREATED_AT, LOCATION_TEXT, STATUS, CURRENT_OWNER, UPDATED_AT, MESSAGE, COUNTRY, CONVERSATION_ID, TEAMS_CHANNEL_ID, TEAMS_TEAM_ID FROM TM_MESSAGE AS tm left join PRODUCTION_LOCATION AS pl on tm.LOCATION_CODE=pl.LOCATION_CODE "
				+ "WHERE CREATED_BY= '" + user + "' AND STATUS IN ('" + MurphyConstant.INPROGRESS + "','"
				+ MurphyConstant.ASSIGN + "') ORDER BY CREATED_AT asc";

		logger.error("[Murphy][MessageDao][getAllActiveMessagesCreatedBy][query] " + queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> responseList = q.list();
		if (!ServicesUtil.isEmpty(responseList)) {
			MessageDto dto = null;
			List<Long> messageIdList = new ArrayList<>();
			for (Object[] obj : responseList) {
				if (!messageIdList.contains(ServicesUtil.isEmpty(obj[0]) ? null : ((BigInteger) obj[0]).longValue())) {
					dto = new MessageDto();
					dto.setMessageId(ServicesUtil.isEmpty(obj[0]) ? null : ((BigInteger) obj[0]).longValue());
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					Date created_at = ServicesUtil.isEmpty(obj[2]) ? null : (Date) obj[2];
					dto.setCreatedAt(created_at.getTime());
					dto.setLocationCode(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setCurrentOwner(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					Date updated_at = ServicesUtil.isEmpty(obj[6]) ? null : (Date) obj[6];
					dto.setUpdatedAt(updated_at.getTime());
					dto.setMessage(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					dto.setCountry(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
					dto.setConversationId(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					dto.setTeamsChannelId(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					dto.setTeamsTeamId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
					messageDtos.add(dto);
					messageIdList.add(((BigInteger) obj[0]).longValue());
				}
			}
		}
		return messageDtos;
	}

	@SuppressWarnings("unchecked")
	public List<MessageDto> getAllActiveMessagesOwnedBy(String user, List<String> userType, List<String> country,
			Integer pageNo) {

		List<MessageDto> messageDtos = new ArrayList<MessageDto>();

		String queryString = "SELECT tm.MESSAGE_ID, tm.CREATED_BY, tm.CREATED_AT, tm.LOCATION_CODE, tm.STATUS, tm.CURRENT_OWNER, tm.UPDATED_AT, tm.MESSAGE, tm.COUNTRY, tm.CONVERSATION_ID, tm.TEAMS_CHANNEL_ID, tm.TEAMS_TEAM_ID, pl.LOCATION_TEXT, wm.MUWI, wt.TIER, pl.LOCATION_TYPE, te.TASK_ID, te.ORIGIN, te.PROCESS_ID FROM TM_MESSAGE tm LEFT JOIN PRODUCTION_LOCATION pl ON tm.LOCATION_CODE = pl.LOCATION_CODE LEFT JOIN WELL_MUWI wm ON pl.LOCATION_CODE=wm.LOCATION_CODE LEFT JOIN WELL_TIER wt ON pl.LOCATION_CODE=wt.LOCATION_CODE LEFT OUTER JOIN TM_TASK_EVNTS te ON CAST(tm.MESSAGE_ID AS VARCHAR) = te.PREV_TASK"
				+ " WHERE tm.CURRENT_OWNER IN ('" + user + "'," + "'" + String.join("','", userType) + "') AND (te.STATUS <> '"+MurphyConstant.COMPLETE+"' OR te.STATUS IS NULL) AND tm.COMMENT IS NULL";

		if (!ServicesUtil.isEmpty(country)) {
			String countries = "'" + String.join("','", country) + "'";
			queryString = queryString + " AND COUNTRY IN ( " + countries + " )";
		}
		queryString = queryString + " ORDER BY CREATED_AT desc";

		if (pageNo > 0) {
			queryString = queryString + " limit " + 20 + " offset " + (pageNo - 1) * 20;
		}

		logger.error("[Murphy][MessageDao][getAllActiveMessagesOwnedBy][query] " + queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> responseList = q.list();
		if (!ServicesUtil.isEmpty(responseList)) {
			MessageDto dto = null;
			List<Long> messageIdList = new ArrayList<>();
			for (Object[] obj : responseList) {
				if (!messageIdList.contains(ServicesUtil.isEmpty(obj[0]) ? null : ((BigInteger) obj[0]).longValue())) {
					dto = new MessageDto();
					dto.setMessageId(ServicesUtil.isEmpty(obj[0]) ? null : ((BigInteger) obj[0]).longValue());
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					Date created_at = ServicesUtil.isEmpty(obj[2]) ? null : (Date) obj[2];
					dto.setCreatedAt(created_at.getTime());
					dto.setLocationCode(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
					dto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setCurrentOwner(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					Date updated_at = ServicesUtil.isEmpty(obj[6]) ? null : (Date) obj[6];
					dto.setUpdatedAt(updated_at.getTime());
					dto.setMessage(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					dto.setCountry(ServicesUtil.isEmpty(obj[8]) ? null : (String) obj[8]);
					dto.setConversationId(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					dto.setTeamsChannelId(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					dto.setTeamsTeamId(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
					dto.setLocationText(ServicesUtil.isEmpty(obj[12]) ? null : (String) obj[12]);
					dto.setMuwi(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
					dto.setTier(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
					dto.setLocationType(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
					dto.setHasDispatch(false);
					dto.setHasInvestigation(false);
					if(!ServicesUtil.isEmpty(obj[17]) && ((String) obj[17]).equals(MurphyConstant.DISPATCH_ORIGIN)){
						dto.setDispatchTaskId(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
						dto.setHasDispatch(true);
					}
					else if(!ServicesUtil.isEmpty(obj[17]) && ((String) obj[17]).equals(MurphyConstant.INVESTIGATON)){
						dto.setInvestigationTaskId(ServicesUtil.isEmpty(obj[16]) ? null : (String) obj[16]);
						dto.setInvestigationProcessId(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
						dto.setHasInvestigation(true);
					}
					messageDtos.add(dto);
					messageIdList.add(((BigInteger) obj[0]).longValue());
				}
			}
		}
		return messageDtos;

	}

	public int getAllActiveMessageCount(String user, List<String> userType, List<String> country) {
		int count = 0;
		String queryString = "SELECT COUNT(tm.MESSAGE_ID) FROM TM_MESSAGE tm LEFT OUTER JOIN TM_TASK_EVNTS te ON CAST(tm.MESSAGE_ID AS VARCHAR) = te.PREV_TASK " + "WHERE CURRENT_OWNER IN ('" + user + "','" + String.join("','", userType) + "') AND (te.STATUS <> '"+MurphyConstant.COMPLETE+"' OR te.STATUS IS NULL) AND tm.COMMENT IS NULL";
		
		if (!ServicesUtil.isEmpty(country)) {
			String countries = "'" + String.join("','", country) + "'";
			queryString = queryString + "AND COUNTRY IN ( " + countries + " ) ";
		}
		logger.error("[Murphy][MessageDao][getAllActiveMessageCount][query] " + queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		count = ((BigInteger) q.uniqueResult()).intValue();
		return count;
	}

	@SuppressWarnings("unchecked")
	public String[] getLocationDetail(String locationCode) {
		String queryString = "SELECT LOCATION_TEXT, MUWI, TIER, LOCATION_TYPE FROM PRODUCTION_LOCATION pl LEFT JOIN WELL_MUWI wm ON pl.LOCATION_CODE=wm.LOCATION_CODE LEFT JOIN WELL_TIER wt ON pl.LOCATION_CODE=wt.LOCATION_CODE "
				+ "WHERE pl.LOCATION_CODE= '" + locationCode + "'";
		String[] result = new String[4];
		logger.error("[Murphy][MessageDao][getLocationDetail][query] " + queryString);
		Query q = this.getSession().createSQLQuery(queryString);
		List<Object[]> responseList = q.list();
		if (!ServicesUtil.isEmpty(responseList)) {

			result[0] = ServicesUtil.isEmpty(responseList.get(0)[0]) ? null : (String) responseList.get(0)[0];
			result[1] = ServicesUtil.isEmpty(responseList.get(0)[1]) ? null : (String) responseList.get(0)[1];
			result[2] = ServicesUtil.isEmpty(responseList.get(0)[2]) ? null : (String) responseList.get(0)[2];
			result[3] = ServicesUtil.isEmpty(responseList.get(0)[3]) ? null : (String) responseList.get(0)[3];
		}
		return result;
	}
	
	public String getChannelUrl(String teamId, String channelId){
		String queryString = "SELECT URL FROM TM_WEBHOOK_MASTER "
				+ "WHERE TEAM_ID= '" + teamId + "' AND CHANNEL_ID= '"+channelId+"' AND VALID_FOR_USAGE=TRUE";
		Query q = this.getSession().createSQLQuery(queryString);
		String url =  q.uniqueResult().toString();
		return url;
	}
}
