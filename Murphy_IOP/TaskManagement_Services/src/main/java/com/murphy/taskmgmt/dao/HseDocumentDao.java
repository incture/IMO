package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.HseDocumentDto;
import com.murphy.taskmgmt.dto.HseResponseBodyDto;
import com.murphy.taskmgmt.entity.HseDocumentDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("HSEDocumentDao")
public class HseDocumentDao extends BaseDao<HseDocumentDo, HseDocumentDto> {
	private static final Logger logger = LoggerFactory.getLogger(HseDocumentDao.class);

	@Override
	protected HseDocumentDo importDto(HseDocumentDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		HseDocumentDo entity = new HseDocumentDo();

		if (!ServicesUtil.isEmpty(fromDto.getDocumentId()))
			entity.setDocumentId(fromDto.getDocumentId());

		if (!ServicesUtil.isEmpty(fromDto.getDocId()))
			entity.setDocId(fromDto.getDocId());

		if (!ServicesUtil.isEmpty(fromDto.getAttachmentUrl()))
			entity.setUrl(fromDto.getAttachmentUrl());

		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());

		if (!ServicesUtil.isEmpty(fromDto.getDocVersion()))
			entity.setDocVersion(fromDto.getDocVersion());

		return entity;
	}

	@Override
	protected HseDocumentDto exportDto(HseDocumentDo entity) {
		HseDocumentDto dto = new HseDocumentDto();
		if (!ServicesUtil.isEmpty(entity.getDocumentId()))
			dto.setDocumentId(entity.getDocumentId());

		if (!ServicesUtil.isEmpty(entity.getDocId()))
			dto.setDocId(entity.getDocId());

		if (!ServicesUtil.isEmpty(entity.getUrl()))
			dto.setAttachmentUrl(entity.getUrl());

		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(entity.getCreatedAt());

		if (!ServicesUtil.isEmpty(entity.getDocVersion()))
			dto.setDocVersion(entity.getDocVersion());

		return dto;
	}

	public String createHSEAttachment(HseDocumentDto dto) {
		String reponse = MurphyConstant.FAILURE;
		try {
			create(dto);
			reponse = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][HSEDocumentDao][createHSEAttachment][error]" + e.getMessage());
		}
		return reponse;

	}

	// FETCH THE DETAILS OF THE DOCUMENT
	@SuppressWarnings("unchecked")
	public HseResponseBodyDto getHseDocument() {
		HseResponseBodyDto responseDto = new HseResponseBodyDto();
		try {

			String query = " select hse.DOCUMENT_ID, hse.VERSION, hse.URL" + " from HSE_DOCUMENTS hse"
					+ " ORDER BY version DESC LIMIT 1 ";

			Query executeQuery = this.getSession().createSQLQuery(query);
			logger.error("[Murphy][TaskEventsDao][getAllTasksForAdmin][query]" + executeQuery);

			List<Object[]> response = executeQuery.list();
			if (!ServicesUtil.isEmpty(response)) {

				HseDocumentDto dto = null;
				for (Object[] obj : response) {

					dto = new HseDocumentDto();
					dto.setDocumentId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					logger.error("[Murphy][HseDocumentDao][getHseDocument][Document_Id]" + dto.getDocumentId());

					dto.setDocVersion(ServicesUtil.isEmpty(obj[1]) ? null : (Integer) obj[1]);
					logger.error("[Murphy][HseDocumentDao][getHseDocument][Version]" + dto.getDocVersion());

					dto.setAttachmentUrl(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
					logger.error("[Murphy][HseDocumentDao][getHseDocument][DocumentURL]" + dto.getAttachmentUrl());

					responseDto.setDocumnetId(dto.getDocumentId());
					responseDto.setDocUrl(dto.getAttachmentUrl());
					responseDto.setVersion(dto.getDocVersion());

				}

			}

		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentDao][getHseDocument][DocumentURL]" + e.getMessage());
		}

		return responseDto;

	}

	public int updateVersion() {
		int result = 1;
		try {
			String query = "select max(version) from HSE_DOCUMENTS";

			result = (int) this.getSession().createSQLQuery(query).uniqueResult();

			if (!ServicesUtil.isEmpty(result)) {
				result++;
				logger.error("[Murphy][HseDocumentDao][updateVersion][Vesrion]" + result);
				return result;
			}

		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentDao][updateVersion][Vesrion]" + e.getMessage());
		}
		return result;
	}
	
	
	// Update query for existing string in the document
	@Transactional(value=TxType.REQUIRES_NEW)
	public int updateTable(String text, int newstringCount){
		int result = 0;
		try {
			String updateQuery = "update HSE_STRINGS set STRING_COUNT= '" + newstringCount + "' where SEARCH_STRING = " + "'" + text + "'";		
			result = this.getSession().createSQLQuery(updateQuery).executeUpdate();
		} catch (Exception e) {
			logger.error("[Murphy][HseDocumentDao][updateTable][error in updating table]" + e.getMessage());
		}
		return result;		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<String> topSearhes()
	{
		
		HseResponseBodyDto responseDto = null;
		List<String> topSearches = new ArrayList<>();		
		String query = "select ID,SEARCH_STRING, STRING_COUNT from HSE_STRINGS where SEARCH_STRING in('JSA','jsa','PPE','ppe','H2S',"
				+ "'h2s','EXCAVATION','Excavation','excavation', 'Hot Work', 'hot work') ";
		
		Query executeQ = this.getSession().createSQLQuery(query);
		logger.error("[Murphy][HseDocumentDao][topSearches][EXECUTE QUERY TOP SEARCHES]" + executeQ);
		List<Object[]> responses = executeQ.list();
		if (!ServicesUtil.isEmpty(responses)) {
			HseDocumentDto hseDto = null;
			for (Object[] obj : responses) {
				responseDto = new HseResponseBodyDto();
				hseDto = new HseDocumentDto();
				hseDto.setStringId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				hseDto.setSearchString(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				hseDto.setStringCount(ServicesUtil.isEmpty(obj[2]) ? null : (Integer) obj[2]);
				topSearches.add(hseDto.getSearchString());
				
			}
			logger.error("[Murphy][HseDopcumentDao][topSeRCH][EXECUTE QUERY TOP SEARCHES]" + topSearches);
		}
		return topSearches;	
		
	}

}