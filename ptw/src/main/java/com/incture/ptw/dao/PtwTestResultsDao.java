package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwTestResultsDto;

@Repository
public class PtwTestResultsDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertPtwTestResults(String permitNumber, PtwTestResultsDto ptwTestResultsDto) {
		try {
			Query query = getSession()
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
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public List<PtwTestResultsDto> getPtwTestRes(String permitNumber) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
