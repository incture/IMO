package com.incture.iopptw.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.PtwPeopleDto;

@Repository
public class PtwPeopleDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertPtwPeople(String permitNumber, PtwPeopleDto ptwPeopleDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"PTWPEOPLE\" VALUES (?,?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, Integer.parseInt(keyGeneratorDao.getSerialNo()));
			query.setParameter(2, Integer.parseInt(permitNumber));
			query.setParameter(3, ptwPeopleDto.getFirstName());
			query.setParameter(4, ptwPeopleDto.getLastName());
			query.setParameter(5, ptwPeopleDto.getContactNumber());
			query.setParameter(6, ptwPeopleDto.getHasSignedJSA());
			query.setParameter(7, ptwPeopleDto.getHasSignedCWP());
			query.setParameter(8, ptwPeopleDto.getHasSignedHWP());
			query.setParameter(9, ptwPeopleDto.getHasSignedCSE());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	public List<PtwPeopleDto> getPtwPeople(String permitNumber) {
		List<PtwPeopleDto> ptwPeopleDtoList = new ArrayList<PtwPeopleDto>();
		try {
			String sql = "select * from IOP.PTWPEOPLE where PERMITNUMBER= :permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber", permitNumber);
			logger.info("getPtwPeople Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();

			for (Object[] a : result) {
				PtwPeopleDto ptwPeopleDto = new PtwPeopleDto();
				ptwPeopleDto.setSerialNo(Integer.parseInt(a[0].toString()));
				ptwPeopleDto.setPermitNumber(Integer.parseInt(a[1].toString()));
				ptwPeopleDto.setFirstName((String) a[2]);
				ptwPeopleDto.setLastName((String) a[3]);
				ptwPeopleDto.setContactNumber((String) a[4]);
				ptwPeopleDto.setHasSignedJSA(Integer.parseInt(a[5].toString()));
				ptwPeopleDto.setHasSignedCWP(Integer.parseInt(a[6].toString()));
				ptwPeopleDto.setHasSignedHWP(Integer.parseInt(a[7].toString()));
				ptwPeopleDto.setHasSignedCSE(Integer.parseInt(a[8].toString()));
				ptwPeopleDtoList.add(ptwPeopleDto);

			}
			return ptwPeopleDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void deletePtwPeople(String permitNumber) {
		try {
			logger.info("permitNumber: " + permitNumber);
			String sql = "DELETE FROM \"IOP\".\"PTWPEOPLE\" WHERE PERMITNUMBER =? ";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}

	}

}
