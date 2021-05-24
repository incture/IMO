package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsPersonnelDto;

@Repository
public class JsaHazardsPersonnelDao extends BaseDao {

	public void insertJsaHazardsPersonnel(String permitNumber, JsaHazardsPersonnelDto jsaHazardsPersonnelDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSPERSONNEL VALUES (?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsPersonnelDto.getPersonnel());
			query.setParameter(3, jsaHazardsPersonnelDto.getPerformInduction());
			query.setParameter(4, jsaHazardsPersonnelDto.getMentorCoachSupervise());
			query.setParameter(5, jsaHazardsPersonnelDto.getVerifyCompetencies());
			query.setParameter(6, jsaHazardsPersonnelDto.getAddressLimitations());
			query.setParameter(7, jsaHazardsPersonnelDto.getManageLanguageBarriers());
			query.setParameter(8, jsaHazardsPersonnelDto.getWearSeatBelts());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public JsaHazardsPersonnelDto getJsaPersonnel(String permitNum) {
		List<Object[]> obj;
		JsaHazardsPersonnelDto jsaHazardsPersonnelDto = new JsaHazardsPersonnelDto();
		try {
			String sql = "select distinct PERMITNUMBER, PERSONNEL,PERFORMINDUCTION,MENTORCOACHSUPERVISE, "
					+ " VERIFYCOMPETENCIES,ADDRESSLIMITATIONS,MANAGELANGUAGEBARRIERS,WEARSEATBELTS "
					+ " from IOP.JSAHAZARDSPERSONNEL where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSPERSONNEL sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsPersonnelDto.setPermitNumber((Integer) a[0]);
				jsaHazardsPersonnelDto.setPersonnel(Integer.parseInt(a[1].toString()));
				jsaHazardsPersonnelDto.setPerformInduction(Integer.parseInt(a[2].toString()));
				jsaHazardsPersonnelDto.setMentorCoachSupervise(Integer.parseInt(a[3].toString()));
				jsaHazardsPersonnelDto.setVerifyCompetencies(Integer.parseInt(a[4].toString()));
				jsaHazardsPersonnelDto.setAddressLimitations(Integer.parseInt(a[5].toString()));
				jsaHazardsPersonnelDto.setManageLanguageBarriers(Integer.parseInt(a[6].toString()));
				jsaHazardsPersonnelDto.setWearSeatBelts(Integer.parseInt(a[7].toString()));
			}
			return jsaHazardsPersonnelDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsPersonnel(JsaHazardsPersonnelDto jsaHazardsPersonnelDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSPERSONNEL\" SET  \"PERSONNEL\"=?,\"PERFORMINDUCTION\"=?,\"MENTORCOACHSUPERVISE\"=?,"
					+ "\"VERIFYCOMPETENCIES\"=?,\"ADDRESSLIMITATIONS\"=?,\"MANAGELANGUAGEBARRIERS\"=?,\"WEARSEATBELTS\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsPersonnel sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsPersonnelDto.getPersonnel());
			query.setParameter(2, jsaHazardsPersonnelDto.getPerformInduction());
			query.setParameter(3, jsaHazardsPersonnelDto.getMentorCoachSupervise());
			query.setParameter(4, jsaHazardsPersonnelDto.getVerifyCompetencies());
			query.setParameter(5, jsaHazardsPersonnelDto.getAddressLimitations());
			query.setParameter(6, jsaHazardsPersonnelDto.getManageLanguageBarriers());
			query.setParameter(7, jsaHazardsPersonnelDto.getWearSeatBelts());
			query.setParameter(8, jsaHazardsPersonnelDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}
}
