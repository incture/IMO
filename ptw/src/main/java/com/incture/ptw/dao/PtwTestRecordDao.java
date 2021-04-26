package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwApprovalDto;
import com.incture.ptw.dto.PtwTestRecordDto;

@Repository
public class PtwTestRecordDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertPtwTestRecord(String permitNumber,PtwTestRecordDto ptwTestRecordDto) {
		try {
			Query query = getSession().createNativeQuery(
					"INSERT INTO \"IOP\".\"PTWTESTRECORD\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, keyGeneratorDao.getPTWTESTREC());
			query.setParameter(2, permitNumber);
			query.setParameter(3, ptwTestRecordDto.getIsCwp());
			query.setParameter(4, ptwTestRecordDto.getIsHwp());
			query.setParameter(5, ptwTestRecordDto.getIsCse());
			query.setParameter(6, ptwTestRecordDto.getDetectorUsed());
			query.setParameter(7, ptwTestRecordDto.getDateOfLastCalibration());
			query.setParameter(8, ptwTestRecordDto.getTestingFrequency());
			query.setParameter(9, ptwTestRecordDto.getContinuousGasMonitoring());
			query.setParameter(10, ptwTestRecordDto.getPriorToWorkCommencing());
			query.setParameter(11, ptwTestRecordDto.getEachWorkPeriod());
			query.setParameter(12, ptwTestRecordDto.getEveryHour());
			query.setParameter(13, ptwTestRecordDto.getGasTester());
			query.setParameter(14, ptwTestRecordDto.getGasTesterComments());
			query.setParameter(15, ptwTestRecordDto.getAreaToBeTested());
			query.setParameter(16, ptwTestRecordDto.getDeviceSerialNo());
			query.setParameter(17, ptwTestRecordDto.getIso2());
			query.setParameter(18, ptwTestRecordDto.getIslels());
			query.setParameter(19, ptwTestRecordDto.getIsh2s());
			query.setParameter(20, ptwTestRecordDto.getOther());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public PtwTestRecordDto getPtwTestRec(String permitNumber) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
