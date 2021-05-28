package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsManualDto;

@Repository
public class JsaHazardsManualDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsManual(String permitNumber, JsaHazardsManualDto jsaHazardsManualDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSMANUAL\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsManualDto.getManualHandling());
			query.setParameter(3, jsaHazardsManualDto.getAssessManualTask());
			query.setParameter(4, jsaHazardsManualDto.getLimitLoadSize());
			query.setParameter(5, jsaHazardsManualDto.getProperLiftingTechnique());
			query.setParameter(6, jsaHazardsManualDto.getConfirmStabilityOfLoad());
			query.setParameter(7, jsaHazardsManualDto.getGetAssistanceOrAid());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsManualDto getJsaHazardsManualDto(String permitNum) {
		List<Object[]> obj;
		JsaHazardsManualDto jsaHazardsManualDto = new JsaHazardsManualDto();
		try {
			String sql = "select distinct PERMITNUMBER, MANUALHANDLING,ASSESSMANUALTASK,LIMITLOADSIZE, "
					+ " PROPERLIFTINGTECHNIQUE,CONFIRMSTABILITYOFLOAD,GETASSISTANCEORAID "
					+ " from IOP.JSAHAZARDSMANUAL where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();

			for (Object[] a : obj) {
				jsaHazardsManualDto.setPermitNumber((Integer) a[0]);
				jsaHazardsManualDto.setManualHandling(Integer.parseInt(a[1].toString()));
				jsaHazardsManualDto.setAssessManualTask(Integer.parseInt(a[2].toString()));
				jsaHazardsManualDto.setLimitLoadSize(Integer.parseInt(a[3].toString()));
				jsaHazardsManualDto.setProperLiftingTechnique(Integer.parseInt(a[4].toString()));
				jsaHazardsManualDto.setConfirmStabilityOfLoad(Integer.parseInt(a[5].toString()));
				jsaHazardsManualDto.setGetAssistanceOrAid(Integer.parseInt(a[6].toString()));

			}
			return jsaHazardsManualDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsManual(JsaHazardsManualDto jsaHazardsManualDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSMANUAL\" SET  \"MANUALHANDLING\"=?,\"ASSESSMANUALTASK\"=?,\"LIMITLOADSIZE\"=?,"
					+ "\"PROPERLIFTINGTECHNIQUE\"=?,\"CONFIRMSTABILITYOFLOAD\"=?,\"GETASSISTANCEORAID\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsManual sql" + sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, jsaHazardsManualDto.getManualHandling());
			query.setParameter(2, jsaHazardsManualDto.getAssessManualTask());
			query.setParameter(3, jsaHazardsManualDto.getLimitLoadSize());
			query.setParameter(4, jsaHazardsManualDto.getProperLiftingTechnique());
			query.setParameter(5, jsaHazardsManualDto.getConfirmStabilityOfLoad());
			query.setParameter(6, jsaHazardsManualDto.getGetAssistanceOrAid());
			query.setParameter(7, jsaHazardsManualDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
