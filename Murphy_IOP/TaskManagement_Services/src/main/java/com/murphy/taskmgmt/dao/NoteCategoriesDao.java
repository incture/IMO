package com.murphy.taskmgmt.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.dto.NoteCategoriesDto;
import com.murphy.taskmgmt.entity.NoteCategoriesDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
@Repository("NoteCategoriesDao")
@Transactional
public class NoteCategoriesDao extends BaseDao<NoteCategoriesDo,NoteCategoriesDto> {
	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(NoteCategoriesDao.class);

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][NoteCategoriesDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}
	@Override
	protected NoteCategoriesDo importDto(NoteCategoriesDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		NoteCategoriesDo entity = new NoteCategoriesDo();
		entity.setNoteCategoryId(fromDto.getNoteCategoryId());
		entity.setNoteCategoryDescription(fromDto.getNoteCategoryDescription());
		return entity;
	}

	@Override
	protected NoteCategoriesDto exportDto(NoteCategoriesDo entity) {
		NoteCategoriesDto toDto = new NoteCategoriesDto();
		toDto.setNoteCategoryId(entity.getNoteCategoryId());
		toDto.setNoteCategoryDescription(entity.getNoteCategoryDescription());
		return toDto;
	}

}
