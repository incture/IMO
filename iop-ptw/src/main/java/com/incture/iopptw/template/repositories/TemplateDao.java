package com.incture.iopptw.template.repositories;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaStopTriggerDto;
import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.repositories.BaseDao;
import com.incture.iopptw.template.dtos.CreateTemplateDto;
import com.incture.iopptw.template.dtos.TemplateDto;

@Repository
public class TemplateDao extends BaseDao {
	
	public void createTemplate(TemplateDto templateDto) {
		String sql = "insert into Template values(?,?)";
		Query q = getSession().createNativeQuery(sql);
		q.setParameter(1, templateDto.getId());
		q.setParameter(2, templateDto.getName());
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<TemplateDto> getAllTemplateList() {
		List<Object[]> obj;
		List<TemplateDto> data = new ArrayList<TemplateDto>();
		try{
			String sql = "select TMPID,NAME from IOP.TEMPLATE";
			Query q = getSession().createNativeQuery(sql);
			obj = q.getResultList();
			for (Object[] a : obj) {
				TemplateDto temp = new TemplateDto();
				temp.setId((Integer)a[0]);
				temp.setName((String)a[1]);
				data.add(temp);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<TemplateDto>();
		}
		System.out.println(data);
		return data;
	}
}
