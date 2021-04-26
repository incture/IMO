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

	@SuppressWarnings({ "unchecked", "deprecation" })
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
				ptwTestResultsDto.setSerialNo(Integer.parseInt(o[0].toString()));
				ptwTestResultsDto.setPermitNumber(Integer.parseInt(o[1].toString()));
				ptwTestResultsDto.setIsCwp(Integer.parseInt(o[2].toString()));
				ptwTestResultsDto.setIsHwp(Integer.parseInt(o[3].toString()));
				ptwTestResultsDto.setIsCse(Integer.parseInt(o[4].toString()));
				ptwTestResultsDto.setPreStartOrWorkTest((String) o[5]);
				ptwTestResultsDto.setOxygenPercentage(Float.parseFloat(o[6].toString()));
				ptwTestResultsDto.setToxicType((String) o[7]);
				ptwTestResultsDto.setToxicResult(Float.parseFloat(o[8].toString()));
				ptwTestResultsDto.setFlammableGas((String) o[9]);
				ptwTestResultsDto.setOthersType((String) o[10]);
				ptwTestResultsDto.setOthersResult(Float.parseFloat(o[11].toString()));
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
				ptwTestRecordDto.setSerialNo(Integer.parseInt(o[0].toString()));
				ptwTestRecordDto.setPermitNumber(Integer.parseInt(o[1].toString()));
				ptwTestRecordDto.setIsCwp(Integer.parseInt(o[2].toString()));
				ptwTestRecordDto.setIsHwp(Integer.parseInt(o[3].toString()));
				ptwTestRecordDto.setIsCse(Integer.parseInt(o[4].toString()));
				ptwTestRecordDto.setDetectorUsed((String) o[5]);
				ptwTestRecordDto.setDateOfLastCalibration(new Date(o[6].toString()));
				ptwTestRecordDto.setTestingFrequency((String) o[7]);
				ptwTestRecordDto.setContinuousGasMonitoring(Integer.parseInt(o[8].toString()));
				ptwTestRecordDto.setPriorToWorkCommencing(Integer.parseInt(o[9].toString()));
				ptwTestRecordDto.setEachWorkPeriod(Integer.parseInt(o[10].toString()));
				ptwTestRecordDto.setEveryHour(Integer.parseInt(o[11].toString()));
				ptwTestRecordDto.setGasTester((String) o[12]);
				ptwTestRecordDto.setGasTesterComments((String) o[13]);
				ptwTestRecordDto.setAreaToBeTested((String) o[14]);
				ptwTestRecordDto.setDeviceSerialNo((String) o[15]);
				ptwTestRecordDto.setIso2(Integer.parseInt(o[16].toString()));
				ptwTestRecordDto.setIslels(Integer.parseInt(o[17].toString()));
				ptwTestRecordDto.setIsh2s(Integer.parseInt(o[18].toString()));
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
