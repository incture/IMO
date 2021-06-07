package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.entities.DftDepartmentsDo;

@Repository
public class DftDepartmentsDao extends BaseDao{
	@SuppressWarnings("unchecked")
	public List<DftDepartmentsDo> getAll()
	{
		Query q=getSession().createQuery("from DftDepartmentsDo");
		return q.getResultList();
	}
}
