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

import com.murphy.taskmgmt.dto.IsolationDetailDto;
import com.murphy.taskmgmt.entity.IsolationDetailDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("IsolationDetailDao")
@Transactional
public class IsolationDetailDao extends BaseDao<IsolationDetailDo, IsolationDetailDto> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private EiAttachmentDao eiAttachmentDao;
	
	private static final Logger logger = LoggerFactory.getLogger(IsolationDetailDao.class);
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][EIFormDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}

	protected IsolationDetailDo importDto(IsolationDetailDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		IsolationDetailDo entity = new IsolationDetailDo();
		entity.setId(fromDto.getId());
		entity.setFormId(fromDto.getFormId());
		entity.setDescription(fromDto.getDescription());
		entity.setEIStored(fromDto.isEIStored());
		entity.setEquipTested(fromDto.isEquipTested());
		entity.setIsolationDate(fromDto.getIsolationDate());
		entity.setReinstatement(fromDto.getReinstatement());
		entity.setDeleted(fromDto.isDeleted());
		return entity;
	}

	protected IsolationDetailDto exportDto(IsolationDetailDo entity) {
		IsolationDetailDto toDto = new IsolationDetailDto();
		toDto.setId(entity.getId());
		toDto.setFormId(entity.getFormId());
		toDto.setDescription(entity.getDescription());
		toDto.setEIStored(entity.isEIStored());
		toDto.setEquipTested(entity.isEquipTested());
		toDto.setIsolationDate(entity.getIsolationDate());
		toDto.setReinstatement(entity.getReinstatement());
		toDto.setDeleted(entity.isDeleted());
		return toDto;
	}
	
	@SuppressWarnings("unchecked")
	public List<IsolationDetailDto> getByFk(String fk) {
		List<IsolationDetailDto> responseList = null;
		String query = "select ID, FORM_ID, DESCRIPTION, IS_EI_STORED, IS_EQUIP_TESTED, ISOLATION_DATE, REINSTATEMENT, IS_DELETED "
				+ "from EI_ISOLATION_DETAIL ei where ei.FORM_ID = '" + fk + "' and IS_DELETED <> True";
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			responseList = new ArrayList<IsolationDetailDto>();
			IsolationDetailDto isolation = null;
			for (Object[] obj : resultList) {
				isolation = new IsolationDetailDto();
				isolation.setId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				isolation.setFormId(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				isolation.setDescription(ServicesUtil.isEmpty(obj[2]) ? null : (String) obj[2]);
				isolation.setEIStored(ServicesUtil.isEmpty(obj[3]) ? false : "1".equals(obj[3].toString()));
				isolation.setEquipTested(ServicesUtil.isEmpty(obj[4]) ? false : "1".equals(obj[4].toString()));
				isolation.setIsolationDate(ServicesUtil.isEmpty(obj[5]) ? null : (Date) obj[5]);
				isolation.setReinstatement(ServicesUtil.isEmpty(obj[6]) ? null : (Date) obj[6]);
				isolation.setDeleted(ServicesUtil.isEmpty(obj[7]) ? null : "1".equals(obj[7].toString()));
				isolation.setAttachmentList(eiAttachmentDao.getAttachment(fk, isolation.getId()));
				responseList.add(isolation);
			}
		}
		return responseList;
	}
	
	public void createList(List<IsolationDetailDto> list, String formId) {
		if (list != null) {
			for (IsolationDetailDto isolationDetailDto : list) {
				isolationDetailDto.setFormId(formId);
				try {
					if (isolationDetailDto.getId() == null) {
						isolationDetailDto.setDeleted(false);
						isolationDetailDto.setId(UUID.randomUUID().toString().replaceAll("-", ""));
						create(isolationDetailDto);
					}
					if (isolationDetailDto.getAttachmentList() != null) {
						eiAttachmentDao.createList(isolationDetailDto.getAttachmentList(), formId, isolationDetailDto.getId());
					}
				} catch (ExecutionFault e) {
					e.printStackTrace();
				} catch (InvalidInputFault e) {
					e.printStackTrace();
				} catch (NoResultFault e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateList(List<IsolationDetailDto> list, String formId) {
		if (list != null) {
			for (IsolationDetailDto isolationDetailDto : list) {
				isolationDetailDto.setFormId(formId);
				try {
					if (isolationDetailDto.getId() == null) {
						isolationDetailDto.setId(UUID.randomUUID().toString().replaceAll("-", ""));
						create(isolationDetailDto);
					} else {
						String query = "update EI_ISOLATION_DETAIL set "
								+ "IS_DELETED = " + isolationDetailDto.isDeleted() + ", "
								+ "DESCRIPTION = '" + isolationDetailDto.getDescription() + "', "
								+ "IS_EI_STORED = " + isolationDetailDto.isEIStored() + ", "
								+ "IS_EQUIP_TESTED = " + isolationDetailDto.isEquipTested() + ", "
								+ "ISOLATION_DATE = TO_TIMESTAMP('"+ServicesUtil.convertFromZoneToZoneString(isolationDetailDto.getIsolationDate(), null, "", "","", MurphyConstant.DATE_DB_FORMATE_SD)+"', 'yyyy-MM-dd HH24:mi:ss'), "
								+ "REINSTATEMENT = TO_TIMESTAMP('"+ServicesUtil.convertFromZoneToZoneString(isolationDetailDto.getReinstatement(), null, "", "","", MurphyConstant.DATE_DB_FORMATE_SD)+"', 'yyyy-MM-dd HH24:mi:ss') "
								+ "where FORM_ID = '" + formId + "' and ID = '" + isolationDetailDto.getId() + "'";
						Query q = this.getSession().createSQLQuery(query);
						q.executeUpdate();
//						update(isolationDetailDto);
					}
					if (isolationDetailDto.getAttachmentList() != null) {
						eiAttachmentDao.createList(isolationDetailDto.getAttachmentList(), formId, isolationDetailDto.getId());
					}
				} catch (ExecutionFault e) {
					e.printStackTrace();
				} catch (InvalidInputFault e) {
					e.printStackTrace();
				} catch (NoResultFault e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String deleteLock(String id){
		IsolationDetailDto dto = new IsolationDetailDto();
		dto.setId(id);
		try {
			delete(dto);
			return MurphyConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return MurphyConstant.FAILURE;
		}
	}
	
}