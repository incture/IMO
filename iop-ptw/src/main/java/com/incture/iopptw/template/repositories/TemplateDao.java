package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.repositories.BaseDao;
import com.incture.iopptw.template.dtos.TemplateDto;

@Repository
public class TemplateDao extends BaseDao {
	
	public void createTemplate(TemplateDto templateDto) {
		String sql = "insert into Template (id,name) values(?,?)";
		Query q = getSession().createNativeQuery(sql);
		q.executeUpdate();
	}

}
