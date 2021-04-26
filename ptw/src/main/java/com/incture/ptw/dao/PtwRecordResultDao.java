package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwRecordResultResponse;
import com.incture.ptw.dto.PtwTestRecordDto;
import com.incture.ptw.dto.PtwTestResultsDto;

@Repository
public class PtwRecordResultDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public PtwRecordResultResponse getPtwRecordResult(String permitNumber) {
		try {
			String sql1 = "select * from IOP.PTWTESTRESULTS where PERMITNUMBER= ?";
			Query q = getSession().createNativeQuery(sql1);
			logger.info("1st sql1 : " + sql1);
			q.setParameter(1, permitNumber);
			List<Object[]> rs = q.getResultList();
			List<PtwTestResultsDto> ptwTestResultsDtoList = new ArrayList<PtwTestResultsDto>();
			for (Object[] o : rs) {
				PtwTestResultsDto ptwTestResultsDto = new PtwTestResultsDto();
				ptwTestResultsDto.setSerialNo((Integer) o[0]);
				ptwTestResultsDto.setPermitNumber((Integer) o[1]);
				ptwTestResultsDto.setIsCwp((Integer) o[2]);
				ptwTestResultsDto.setIsHwp((Integer) o[3]);
				ptwTestResultsDto.setIsCse((Integer) o[4]);
				ptwTestResultsDto.setPreStartOrWorkTest((String) o[5]);
				ptwTestResultsDto.setOxygenPercentage((Float) o[6]);
				ptwTestResultsDto.setToxicType((String) o[7]);
				ptwTestResultsDto.setToxicResult((Float) o[8]);
				ptwTestResultsDto.setFlammableGas((String) o[9]);
				ptwTestResultsDto.setOthersType((String) o[10]);
				ptwTestResultsDto.setOthersResult((Float) o[11]);
				ptwTestResultsDto.setDate((Date) o[12]);
				ptwTestResultsDto.setTime((Date) o[13]);
				ptwTestResultsDtoList.add(ptwTestResultsDto);
			}
			PtwRecordResultResponse ptwRecordResultResponse = new PtwRecordResultResponse();
			ptwRecordResultResponse.setPtwTestResultsDtoList(ptwTestResultsDtoList);

			String sql2 = "select * from IOP.PTWTESTRECORD where PERMITNUMBER= ?";
			Query q1 = getSession().createNativeQuery(sql1);
			logger.info("2nd sql : " + sql2);
			q1.setParameter(1, permitNumber);
			List<Object[]> rs1 = q.getResultList();
			for (Object o[] : rs1) {
				PtwTestRecordDto ptwTestRecordDto = new PtwTestRecordDto();
				ptwTestRecordDto.setSerialNo((Integer) o[0]);
				ptwTestRecordDto.setPermitNumber((Integer) o[1]);
				ptwTestRecordDto.setIsCwp((Integer) o[2]);
				ptwTestRecordDto.setIsHwp((Integer) o[3]);
				ptwTestRecordDto.setIsCse((Integer) o[4]);
				ptwTestRecordDto.setDetectorUsed((String) o[5]);
				ptwTestRecordDto.setDateOfLastCalibration((Date) o[6]);
				ptwTestRecordDto.setTestingFrequency((String) o[7]);
				ptwTestRecordDto.setContinuousGasMonitoring((Integer) o[8]);
				ptwTestRecordDto.setPriorToWorkCommencing((Integer) o[9]);
				ptwTestRecordDto.setEachWorkPeriod((Integer) o[10]);
				ptwTestRecordDto.setEveryHour((Integer) o[11]);
				ptwTestRecordDto.setGasTester((String) o[12]);
				ptwTestRecordDto.setGasTesterComments((String) o[13]);
				ptwTestRecordDto.setAreaToBeTested((String) o[14]);
				ptwTestRecordDto.setDeviceSerialNo((String) o[15]);
				ptwTestRecordDto.setIso2((Integer) o[16]);
				ptwTestRecordDto.setIslels((Integer) o[17]);
				ptwTestRecordDto.setIsh2s((Integer) o[18]);
				ptwTestRecordDto.setOther((String) o[19]);
				ptwRecordResultResponse.setPtwTestRecordDto(ptwTestRecordDto);
				break;
			}
			return ptwRecordResultResponse;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
