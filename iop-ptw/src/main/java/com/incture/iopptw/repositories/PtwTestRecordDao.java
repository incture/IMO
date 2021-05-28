package com.incture.iopptw.repositories;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.PtwTestRecordDto;

@Repository
public class PtwTestRecordDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;
	@Autowired
	private SessionFactory sessionFactory;

	public void insertPtwTestRecord(String permitNumber, PtwTestRecordDto ptwTestRecordDto) {
		logger.info("ptwTestRecordDto" + ptwTestRecordDto);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(
					"INSERT INTO \"IOP\".\"PTWTESTRECORD\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, keyGeneratorDao.getPTWTESTREC());
			query.setParameter(2, permitNumber);
			query.setParameter(3, ptwTestRecordDto.getIsCWP());
			query.setParameter(4, ptwTestRecordDto.getIsHWP());
			query.setParameter(5, ptwTestRecordDto.getIsCSE());
			query.setParameter(6, ptwTestRecordDto.getDetectorUsed());
			query.setParameter(7, ptwTestRecordDto.getDateOfLastCalibration());
			query.setParameter(8, ptwTestRecordDto.getTestingFrequency());
			query.setParameter(9, ptwTestRecordDto.getContinuousGasMonitoring());
			query.setParameter(10, ptwTestRecordDto.getPriorToWorkCommencing());
			query.setParameter(11, ptwTestRecordDto.getEachWorkPeriod());
			query.setParameter(12, ptwTestRecordDto.getEveryHour());
			query.setParameter(13, ptwTestRecordDto.getGasTester());
			query.setParameter(14, ptwTestRecordDto.getGasTesterComments());
			query.setParameter(15, ptwTestRecordDto.getAreaTobeTested());
			query.setParameter(16, ptwTestRecordDto.getDeviceSerialNo());
			query.setParameter(17, ptwTestRecordDto.getIsO2());
			query.setParameter(18, ptwTestRecordDto.getIsLELs());
			query.setParameter(19, ptwTestRecordDto.getIsH2S());
			query.setParameter(20, ptwTestRecordDto.getOther());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public PtwTestRecordDto getPtwTestRec(String permitNumber) {
		try {
			String sqlString = "select * from IOP.PTWTESTRECORD where PERMITNUMBER = ?";
			Query query = getSession().createNativeQuery(sqlString);
			query.setParameter(1, permitNumber);
			PtwTestRecordDto ptwTestRecordDto = new PtwTestRecordDto();
			@SuppressWarnings("unchecked")
			List<Object[]> list = query.getResultList();
			if (list.isEmpty())
				return null;
			for (Object[] o : list) {
				ptwTestRecordDto.setSerialNo(Integer.parseInt(o[0].toString()));
				ptwTestRecordDto.setPermitNumber(Integer.parseInt(o[1].toString()));
				ptwTestRecordDto.setIsCWP(Integer.parseInt(o[2].toString()));
				ptwTestRecordDto.setIsHWP(Integer.parseInt(o[3].toString()));
				ptwTestRecordDto.setIsCSE(Integer.parseInt(o[4].toString()));
				ptwTestRecordDto.setDetectorUsed((String) o[5]);
				ptwTestRecordDto.setDateOfLastCalibration((Date) o[6]);
				ptwTestRecordDto.setTestingFrequency((String) o[7]);
				ptwTestRecordDto.setContinuousGasMonitoring(Integer.parseInt(o[8].toString()));
				ptwTestRecordDto.setPriorToWorkCommencing(Integer.parseInt(o[9].toString()));
				ptwTestRecordDto.setEachWorkPeriod(Integer.parseInt(o[10].toString()));
				ptwTestRecordDto.setEveryHour(Integer.parseInt(o[11].toString()));
				ptwTestRecordDto.setGasTester((String) o[12]);
				ptwTestRecordDto.setGasTesterComments((String) o[13]);
				ptwTestRecordDto.setAreaTobeTested((String) o[14]);
				ptwTestRecordDto.setDeviceSerialNo((String) o[15]);
				ptwTestRecordDto.setIsO2(Integer.parseInt(o[16].toString()));
				ptwTestRecordDto.setIsLELs(Integer.parseInt(o[17].toString()));
				ptwTestRecordDto.setIsH2S(Integer.parseInt(o[18].toString()));
				ptwTestRecordDto.setOther((String) o[19]);
				break;
			}
			return ptwTestRecordDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void deletePtwTestRecord(String permitNumber) {
		try {
			logger.info("permitNumber: " + permitNumber);
			String sql = "DELETE FROM \"IOP\".\"PTWTESTRECORD\" WHERE PERMITNUMBER =? ";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}

	}

	public void updatePtwTestRecord(PtwTestRecordDto ptwTestRecordDto) {
		try {
			String sql = "UPDATE IOP.PTWTESTRECORD SET  ISCWP=?, ISHWP=?, ISCSE=?, DETECTORUSED=?, "
					+ "DATEOFLASTCALIBRATION=?, TESTINGFREQUENCY=?, CONTINUOUSGASMONITORING=?,"
					+ "PRIORTOWORKCOMMENCING=?,EACHWORKPERIOD=?, EVERYHOUR=?, GASTESTER=?,"
					+ "GASTESTERCOMMENTS=?,AREATOBETESTSED=?,DEVICESERIALNO=?,ISO2=?,ISLELS=?,"
					+ "ISH2S=?,OTHER=? where SERIALNO=?";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, ptwTestRecordDto.getIsCWP());
			query.setParameter(2, ptwTestRecordDto.getIsHWP());
			query.setParameter(3, ptwTestRecordDto.getIsCSE());
			query.setParameter(4, ptwTestRecordDto.getDetectorUsed());
			query.setParameter(5, ptwTestRecordDto.getDateOfLastCalibration());
			query.setParameter(6, ptwTestRecordDto.getTestingFrequency());
			query.setParameter(7, ptwTestRecordDto.getContinuousGasMonitoring());
			query.setParameter(8, ptwTestRecordDto.getPriorToWorkCommencing());
			query.setParameter(9, ptwTestRecordDto.getEachWorkPeriod());
			query.setParameter(10, ptwTestRecordDto.getEveryHour());
			query.setParameter(11, ptwTestRecordDto.getGasTester());
			query.setParameter(12, ptwTestRecordDto.getGasTesterComments());
			query.setParameter(13, ptwTestRecordDto.getAreaTobeTested());
			query.setParameter(14, ptwTestRecordDto.getDeviceSerialNo());
			query.setParameter(15, ptwTestRecordDto.getIsO2());
			query.setParameter(16, ptwTestRecordDto.getIsLELs());
			query.setParameter(17, ptwTestRecordDto.getIsH2S());
			query.setParameter(18, ptwTestRecordDto.getOther());
			query.setParameter(19, ptwTestRecordDto.getSerialNo());
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}
	}

}
