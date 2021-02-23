package com.murphy.taskmgmt.dao;

import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.HandoverNotesDataDto;
import com.murphy.taskmgmt.dto.HandoverNotesDto;
import com.murphy.taskmgmt.entity.HandoverNotesDataDo;
import com.murphy.taskmgmt.entity.HandoverNotesDataPKKeys;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;
@Repository("HandoverNotesDataDao")
@Transactional
public class HandoverNotesDataDao extends BaseDao<HandoverNotesDataDo, HandoverNotesDataDto>{
	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(HandoverNotesDataDao.class);

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][HandoverNotesDataDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}
	@Override
	protected HandoverNotesDataDo importDto(HandoverNotesDataDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		HandoverNotesDataDo entity = new HandoverNotesDataDo();
		HandoverNotesDataPKKeys primaryKeys = new HandoverNotesDataPKKeys();
		primaryKeys.setNoteId(fromDto.getNoteId());
		primaryKeys.setNoteCategoryId(fromDto.getNoteCategoryId());
		entity.setHandoverNotesDataPKKeys(primaryKeys);
		//String value =fromDto.getNote();
		entity.setNote(  fromDto.getNote());
		
		return entity;
	}

	@Override
	protected HandoverNotesDataDto exportDto(HandoverNotesDataDo entity) {
		HandoverNotesDataDto toDto = new HandoverNotesDataDto();
		toDto.setNoteId(entity.getHandoverNotesDataPKKeys().getNoteId());
		toDto.setNoteCategoryId(entity.getHandoverNotesDataPKKeys().getNoteCategoryId());
		//toDto.setNote(ServicesUtil.getStringFromBlob(entity.getNote()));
		toDto.setNote(entity.getNote());
		return toDto;
	}
	
	public String saveHandoverNote(HandoverNotesDataDto dto)
	{
		String message = "";
		try{
			
			this.getSession().saveOrUpdate(importDto(dto));
			message = MurphyConstant.CREATED_SUCCESS;
			
			
		}catch(Exception e)
		{
			logger.error("[Murphy][HandoverNotesDataDao][saveHandoverNote][Exception]  : " + e.getMessage());
			e.printStackTrace();
			message = MurphyConstant.CREATE_FAILURE;
		}
		return message;
	}
	

}
