package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHeaderTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHeaderTemplate(String id, JsaheaderDto jsaheaderDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHEADER VALUES (?,?,?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("insertJsaHeader sql " + sql);
			query.setParameter(1, null);
			query.setParameter(2, null);
			query.setParameter(3, jsaheaderDto.getHasCWP());
			query.setParameter(4, jsaheaderDto.getHasHWP());
			query.setParameter(5, jsaheaderDto.getHasCSE());
			query.setParameter(6, jsaheaderDto.getTaskDescription());
			query.setParameter(7, jsaheaderDto.getIdentifyMostSeriousPotentialInjury());
			query.setParameter(8, jsaheaderDto.getIsActive());
			query.setParameter(9, id);
			query.setParameter(10, jsaheaderDto.getStatus());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaheaderDto getJsaHeader(Integer id){
		JsaheaderDto jsaheaderDto = new JsaheaderDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, JSAPERMITNUMBER,HASCWP,HASHWP, "
					+ " HASCSE,TASKDESCRIPTION,IDENTIFYMOSTSERIOUSPOTENTIALINJURY,ISACTIVE,STATUS from "
					+ " IOP.TMPJSAHEADER where TMPID = :id";
			logger.info("JSAHEADER sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaheaderDto.setPermitNumber((Integer) a[0]);
				jsaheaderDto.setJsaPermitNumber((String) a[1]);
				jsaheaderDto.setHasCWP(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setHasHWP(Integer.parseInt(a[3].toString()));
				jsaheaderDto.setHasCSE(Integer.parseInt(a[4].toString()));
				jsaheaderDto.setTaskDescription((String) a[5]);
				jsaheaderDto.setIdentifyMostSeriousPotentialInjury((String) a[6]);
				jsaheaderDto.setIsActive(Integer.parseInt(a[7].toString()));
				jsaheaderDto.setStatus((String) a[8]);
			}
			logger.info(jsaheaderDto.toString());
			System.out.println(jsaheaderDto);
			return jsaheaderDto;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
