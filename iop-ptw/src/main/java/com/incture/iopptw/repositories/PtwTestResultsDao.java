package com.incture.iopptw.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.PtwTestResultsDto;

@Repository
public class PtwTestResultsDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;
	@Autowired
	private SessionFactory sessionFactory;

	public void insertPtwTestResults(String permitNumber, PtwTestResultsDto ptwTestResultsDto) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createNativeQuery("INSERT INTO \"IOP\".\"PTWTESTRESULTS\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		query.setParameter(1, keyGeneratorDao.getPTWATESTRES());
		query.setParameter(2, permitNumber);
		query.setParameter(3, ptwTestResultsDto.getIsCWP());
		query.setParameter(4, ptwTestResultsDto.getIsHWP());
		query.setParameter(5, ptwTestResultsDto.getIsCSE());
		query.setParameter(6, ptwTestResultsDto.getPreStartOrWorkTest());
		query.setParameter(7, ptwTestResultsDto.getOxygenPercentage());
		query.setParameter(8, ptwTestResultsDto.getToxicType());
		query.setParameter(9, ptwTestResultsDto.getToxicResult());
		query.setParameter(10, ptwTestResultsDto.getFlammableGas());
		query.setParameter(11, ptwTestResultsDto.getOthersType());
		query.setParameter(12, ptwTestResultsDto.getOthersResult());
		query.setParameter(13, ptwTestResultsDto.getDate());
		query.setParameter(14, ptwTestResultsDto.getTime());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public List<PtwTestResultsDto> getPtwTestRes(String permitNumber) {
		try {
			String sql = "select * from IOP.PTWTESTRESULTS where PERMITNUMBER= :permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber", permitNumber);
			logger.info("getPtwPeople Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			if (result.isEmpty())
				return null;
			List<PtwTestResultsDto> ptwTestResultsDtoList = new ArrayList<PtwTestResultsDto>();
			for (Object[] a : result) {
				PtwTestResultsDto ptwTestResultsDto = new PtwTestResultsDto();
				ptwTestResultsDto.setSerialNo(Integer.parseInt(a[0].toString()));
				ptwTestResultsDto.setPermitNumber(Integer.parseInt(a[1].toString()));
				ptwTestResultsDto.setIsCWP(Integer.parseInt(a[2].toString()));
				ptwTestResultsDto.setIsHWP(Integer.parseInt(a[3].toString()));
				ptwTestResultsDto.setIsCSE(Integer.parseInt(a[4].toString()));
				ptwTestResultsDto.setPreStartOrWorkTest((String) a[5]);
				ptwTestResultsDto.setOxygenPercentage(((BigDecimal) a[6]).doubleValue());
				ptwTestResultsDto.setToxicType((String) a[7]);
				ptwTestResultsDto.setToxicResult(((BigDecimal) a[8]).doubleValue());
				ptwTestResultsDto.setFlammableGas((String) a[9]);
				ptwTestResultsDto.setOthersType((String) a[10]);
				ptwTestResultsDto.setOthersResult(((BigDecimal) a[11]).doubleValue());
				ptwTestResultsDto.setDate((Date) a[12]);
				ptwTestResultsDto.setTime(a[13].toString());
				ptwTestResultsDtoList.add(ptwTestResultsDto);
			}
			return ptwTestResultsDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void deletePtwTestResults(String permitNumber) {

		logger.info("permitNumber: " + permitNumber);
		String sql = "DELETE FROM \"IOP\".\"PTWTESTRESULTS\" WHERE PERMITNUMBER =? ";
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createNativeQuery(sql);
		query.setParameter(1, permitNumber);
		logger.info("sql " + sql);
		query.executeUpdate();
		tx.commit();
		session.close();

	}
}
