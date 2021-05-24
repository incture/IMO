package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.entities.TestDo;

@Repository
public class TestDao extends BaseDao{
	public List<TestDo> getAllData() {
		String sql="select * from Test";
		Query q=getSession().createNativeQuery(sql);
		return q.getResultList();
	}
}
