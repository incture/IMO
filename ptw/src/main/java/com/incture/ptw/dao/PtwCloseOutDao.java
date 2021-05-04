package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwCloseOut1Dto;
import com.incture.ptw.dto.PtwCloseOutDto;

@Repository
public class PtwCloseOutDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public List<PtwCloseOutDto> getPtwCloseOut(String permitNumber) {
		try {
			String sql = "select * from IOP.PTWCLOSEOUT where PERMITNUMBER= :permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber", permitNumber);
			logger.info("getPtwHwpWork Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			List<PtwCloseOutDto> ptwCloseOutDtoList = new ArrayList<PtwCloseOutDto>();
			for (Object[] a : result) {
				PtwCloseOutDto ptwCloseOutDto = new PtwCloseOutDto();
				ptwCloseOutDto.setSerialNo(Integer.parseInt(a[0].toString()));
				ptwCloseOutDto.setPermitNumber(Integer.parseInt(a[1].toString()));
				ptwCloseOutDto.setIsCWP(Integer.parseInt(a[2].toString()));
				ptwCloseOutDto.setIsHWP(Integer.parseInt(a[3].toString()));
				ptwCloseOutDto.setIsCSE(Integer.parseInt(a[4].toString()));
				ptwCloseOutDto.setPicName((String) a[5]);
				ptwCloseOutDto.setWorkCompleted(Integer.parseInt(a[6].toString()));
				ptwCloseOutDto.setClosedBy((String) a[7]);
				ptwCloseOutDto.setClosedDate((Date) a[8]);
				ptwCloseOutDto.setWorkStatusComment((String) a[9]);
				ptwCloseOutDtoList.add(ptwCloseOutDto);
			}
			return ptwCloseOutDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void insertPtwCloseOut(PtwCloseOut1Dto p) {
		logger.info("PtwCloseOutDao | insertPtwCloseOut" + p);
			Query query = getSession()
					.createNativeQuery("INSERT INTO \"IOP\".\"PTWCLOSEOUT\" VALUES (?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, keyGeneratorDao.getTOPTWCLOSEOUT());
			query.setParameter(2, p.getPermitNumber());
			query.setParameter(3, p.getIsCWP());
			query.setParameter(4, p.getIsHWP());
			query.setParameter(5, p.getIsCSE());
			query.setParameter(6, p.getPicName());
			query.setParameter(7, p.getWorkCompleted());
			query.setParameter(8, p.getClosedBy());
			query.setParameter(9, p.getClosedDate());
			query.setParameter(10, p.getWorkStatusComments());
			query.executeUpdate();

	}
	
	public void insertPtwCloseOut(PtwCloseOutDto p) {
		logger.info("PtwCloseOutDao | insertPtwCloseOut" + p);
		try {
			Query query = getSession()
					.createNativeQuery("INSERT INTO \"IOP\".\"PTWCLOSEOUT\" VALUES (?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, keyGeneratorDao.getTOPTWCLOSEOUT());
			query.setParameter(2, p.getPermitNumber());
			query.setParameter(3, p.getIsCWP());
			query.setParameter(4, p.getIsHWP());
			query.setParameter(5, p.getIsCSE());
			query.setParameter(6, p.getPicName());
			query.setParameter(7, p.getWorkCompleted());
			query.setParameter(8, p.getClosedBy());
			query.setParameter(9, p.getClosedDate());
			query.setParameter(10, p.getWorkStatusComment());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void updatePtwCloseOut(PtwCloseOutDto p) {
		try{
			String sql = "UPDATE \"IOP\".\"PTWCLOSEOUT\" SET  \"ISCWP\"= ? \"ISHWP\"= ? \"ISCSE\"=? \"PICNAME\"= ? \"WORKCOMPLETED\"= ? \"CLOSEDBY\"= ? \"CLOSEDDATE\"= ? \"WORKSTATUSCOMMENT\"= ? where \"SERIALNO\"= ?";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, p.getIsCWP());
			query.setParameter(2, p.getIsHWP());
			query.setParameter(3, p.getIsCSE());
			query.setParameter(4, p.getPicName());
			query.setParameter(5, p.getWorkCompleted());
			query.setParameter(6, p.getClosedBy());
			query.setParameter(7, p.getClosedDate());
			query.setParameter(8, p.getWorkStatusComment());
			query.setParameter(9, p.getSerialNo());
			query.executeUpdate();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
}
