package com.incture.iopptw.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.IOPPtwDto;

@Repository("IOPPtwDao")
public class IOPPtwDao extends BaseDao {

	public List<IOPPtwDto> getPtwList(int isCwp, int isHwp, int isCse, String location, int isActive) {
		List<Object[]> obj;
		List<IOPPtwDto> data = new ArrayList<IOPPtwDto>();
		try {
			String sql = "select P.PERMITNUMBER,P.PTWPERMITNUMBER,P.ISCWP,P.ISHWP,P.ISCSE,P.PLANNEDDATETIME,"
					+ " P.LOCATION,P.CREATEDBY,P.CONTRACTORPERFORMINGWORK,P.ESTIMATEDTIMEOFCOMPLETION,"
					+ " P.EQUIPMENTID,P.WORKORDERNUMBER,P.STATUS,J.ISACTIVE"
					+ " FROM (IOP.PTWHEADER AS P INNER JOIN IOP.JSAHEADER AS J "
					+ " ON P.PERMITNUMBER = J.PERMITNUMBER) WHERE LOCATION ='" + location + "'" + "AND ISACTIVE ='"
					+ isActive + "'";
			if (isCwp == 1)
				sql += "AND ISCWP = '" + isCwp + "'";

			else if (isHwp == 1)
				sql += "AND ISHWP = '" + isHwp + "'";

			else if (isCse == 1)
				sql += "AND ISCSE = '" + isCse + "'";

			else
				return null;
			Query q = getSession().createNativeQuery(sql);
			obj = q.getResultList();
			if (obj == null)
				return null;
			for (Object[] a : obj) {
				IOPPtwDto temp = new IOPPtwDto();
				temp.setPermitNumber((Integer)a[0]);
				temp.setPtwPermitNumber((String)a[1]);
				temp.setIsCwp((Byte)a[2]);
				temp.setIsHwp((Byte)a[3]);
				temp.setIsCse((Byte)a[4]);
				temp.setPlannedDateTime((Date)a[5]);
				temp.setLocation((String)a[6]);
				temp.setCreatedBy((String)a[7]);
				temp.setContractorPerformingWork((String)a[8]);
				temp.setEstimatedTimeOfCompletion((Date)a[9]);
				temp.setEquipmentId((String)a[10]);
				temp.setWorkOrderNumber((String)a[11]);
				temp.setStatus((String)a[12]);
				temp.setIsActive((Byte)a[13]);
				data.add(temp);
			}
			return data;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
