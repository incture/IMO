package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.entities.TestDo;

@Repository
public class TestDao extends BaseDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<TestDo> getAllData() {
		String sql="select * from Test";
		Query q=getSession().createNativeQuery(sql);
		return q.getResultList();
	}
	
	public String saveData(int id) {
		Session session= sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql="INSERT INTO IOP.TEST VALUES (?)";
		Query q=session.createNativeQuery(sql);
		q.setParameter(1, id);
		q.executeUpdate();
		tx.commit();
		session.close();
		return "success";
	}
}
