package com.murphy.taskmgmt.dao;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EIContractorDto;
import com.murphy.taskmgmt.entity.AttachmentDo;
import com.murphy.taskmgmt.entity.EIContractorDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("EIContractorDao")
@Transactional
public class EIContractorDao extends BaseDao<EIContractorDo, EIContractorDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(EIContractorDao.class);

	@Override
	protected EIContractorDo importDto(EIContractorDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EIContractorDo entity = new EIContractorDo();
		entity.setContractorName(fromDto.getContractorName());
		entity.setContractorPerformingWork(fromDto.getContractorPerformingWork());
		entity.setEmailId(fromDto.getEmailId());
		entity.setId(fromDto.getId());
		entity.setSignatureContent(fromDto.getSignatureContent());
		entity.setCreatedAt(fromDto.getCreatedAt());
		entity.setUpdatedAt(fromDto.getUpdatedAt());
		entity.setFormId(fromDto.getFormId());
		return entity;
	}

	@Override
	protected EIContractorDto exportDto(EIContractorDo entity) {
		EIContractorDto toDto = new EIContractorDto();
		toDto.setContractorName(entity.getContractorName());
		toDto.setContractorPerformingWork(entity.getContractorPerformingWork());
		toDto.setEmailId(entity.getEmailId());
		toDto.setId(entity.getId());
		toDto.setSignatureContent(entity.getSignatureContent());
		toDto.setCreatedAt(entity.getCreatedAt());
		toDto.setUpdatedAt(entity.getUpdatedAt());
		toDto.setFormId(entity.getFormId());
		return toDto;
	}
	
	public String createContractor(List<EIContractorDto> dto, String formId,String type) {
		String reponse = MurphyConstant.FAILURE;
		try {
			if(dto != null) {
				for (EIContractorDto eIContractorDto : dto) {
					if(ServicesUtil.isEmpty(eIContractorDto.getId())){
						String id = UUID.randomUUID().toString().replaceAll("-", "");
						eIContractorDto.setId(id);
						eIContractorDto.setFormId(formId);
						if(type.equalsIgnoreCase(MurphyConstant.UPDATE)){
						eIContractorDto.setUpdatedAt(new Date());
						}else{
						eIContractorDto.setCreatedAt(new Date());
						}
						create(eIContractorDto);
					}
				}
			}
			
			reponse = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][EICommentDao][createEiComment][error]" + e.getMessage());
		}
		return reponse;
	}
	
	@SuppressWarnings("unchecked")
	public List<EIContractorDto> getByFk(String fk) {
		String query = "select ei from EIContractorDo ei where ei.formId = '" + fk + "'";
		Query q = this.getSession().createQuery(query);
		List<EIContractorDo> response = (List<EIContractorDo>) q.list();
		List<EIContractorDto> responseList = exportDtoList(response);
		
//		List<EIContractorDto> responseList = null;
//		String query = "select ID, FORM_ID, CONTRACTOR_NAME, EMAIL_ID, CONTRACTOR_PERFORM_WORK, SIGNATURE_CONTENT, UPDATED_AT, CREATED_AT "
//				+ "from EI_CONTRACTOR_DATA ei where ei.FORM_ID = '" + fk + "'";
//		Query q = this.getSession().createSQLQuery(query);
//		List<Object[]> resultList = q.list();
//		if (!ServicesUtil.isEmpty(resultList)) {
//			responseList = new ArrayList<EIContractorDto>();
//			EIContractorDto activity = null;
//			for (Object[] obj : resultList) {
//				activity = new EIContractorDto();
//				activity.setId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
//				activity.setFormId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
//				activity.setContractorName(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
//				activity.setEmailId(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
//				activity.setContractorPerformingWork(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
//				activity.setSignatureContent(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
//				activity.setUpdatedAt(ServicesUtil.isEmpty(obj[6]) ? null : (Date) obj[6]);
//				activity.setCreatedAt(ServicesUtil.isEmpty(obj[7]) ? null : (Date) obj[7]);
//				responseList.add(activity);
//			}
//		}
		return responseList;
	}
	
}
