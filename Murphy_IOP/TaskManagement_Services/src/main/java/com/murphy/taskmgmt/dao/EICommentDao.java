package com.murphy.taskmgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.EICommentDto;
import com.murphy.taskmgmt.dto.EiAttachmentDto;
import com.murphy.taskmgmt.dto.IsolationDetailDto;
import com.murphy.taskmgmt.entity.EICommentDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("EICommentDao")
@Transactional
public class EICommentDao extends BaseDao<EICommentDo, EICommentDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(EICommentDao.class);
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][EICommentDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}
	}
	
	@Override
	protected EICommentDo importDto(EICommentDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		EICommentDo entity = new EICommentDo();
		if(fromDto.getId() != null) {
			entity.setId(fromDto.getId());
		} else {
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		entity.setComment(fromDto.getComment());
		entity.setFormId(fromDto.getFormId());
		entity.setUpdatedAt(fromDto.getUpdatedAt());
		entity.setUpdatedBy(fromDto.getUpdatedBy());
		return entity;
	}

	@Override
	protected EICommentDto exportDto(EICommentDo entity) {
		EICommentDto toDto = new EICommentDto();
		toDto.setId(entity.getId());
		toDto.setComment(entity.getComment());
		toDto.setFormId(entity.getFormId());
		toDto.setUpdatedAt(entity.getUpdatedAt());
		toDto.setUpdatedBy(entity.getUpdatedBy());
		return toDto;
	}
	
	public String createEIComment(List<EICommentDto> dto, String formId) {
		String reponse = MurphyConstant.FAILURE;
		try {
			if(dto != null) {
				for (EICommentDto eiCommentDto : dto) {
					eiCommentDto.setFormId(formId);
					eiCommentDto.setUpdatedAt(new Date());
					
					create(eiCommentDto);
				}
			}
			reponse = MurphyConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[Murphy][EICommentDao][createEiComment][error]" + e.getMessage());
		}
		return reponse;
	}
	
	public String updateEIComment(List<EICommentDto> dto, String formId) {
		String reponse = MurphyConstant.FAILURE;
		try {
			if(dto != null) {
				for (EICommentDto eiCommentDto : dto) {
					if(eiCommentDto.getId() == null || eiCommentDto.getId() == "") {
						eiCommentDto.setFormId(formId);
						eiCommentDto.setUpdatedAt(new Date());
						create(eiCommentDto);
					} else {
						eiCommentDto.setUpdatedAt(new Date());
						update(eiCommentDto);
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
	public List<EICommentDto> getByFk(String fk) {
		List<EICommentDto> responseList = null;
		String query = "select ID, EI_FORM_ID, COMMENT, UPDATED_BY, UPDATED_AT from EI_COMMENT ei where ei.EI_FORM_ID = '" + fk + "'";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			responseList = new ArrayList<EICommentDto>();
			EICommentDto eiComment = null;
			for (Object[] obj : resultList) {
				eiComment = new EICommentDto();
				eiComment.setId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				eiComment.setFormId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				eiComment.setComment(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				eiComment.setUpdatedBy(ServicesUtil.isEmpty(obj[3]) ? null : (String) obj[3]);
				eiComment.setUpdatedAt(ServicesUtil.isEmpty(obj[4]) ? null : (Date) obj[4]);
				responseList.add(eiComment);
			}
		}
		return responseList;
	}
	
}
