package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwPeopleDto;

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
			query.setParameter(6, ptwPeopleDto.getHasSignedJsa());
			query.setParameter(7, ptwPeopleDto.getHasSignedCwp());
			query.setParameter(8, ptwPeopleDto.getHasSignedHwp());
			query.setParameter(9, ptwPeopleDto.getHasSignedCse());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	public List<PtwPeopleDto> getPtwPeople(String permitNumber) {
		try {

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
