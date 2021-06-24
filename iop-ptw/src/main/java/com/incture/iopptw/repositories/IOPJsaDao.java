package com.incture.iopptw.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.IOPJsaDto;

@Repository("IOPJsaDao")
public class IOPJsaDao extends BaseDao{
	@SuppressWarnings("unchecked")
	public List<IOPJsaDto> getJsaList(String facilityOrSite, Integer isActive) {
		List<Object[]> obj;
		List<IOPJsaDto> data = new ArrayList<IOPJsaDto>();
		try {
			String sql = "select a.PERMITNUMBER,a.JSAPERMITNUMBER," + " a.HASCWP,A.HASHWP,a.HASCSE,a.TASKDESCRIPTION,"
					+ " a.IDENTIFYMOSTSERIOUSPOTENTIALINJURY,a.ISACTIVE,a.STATUS,"
					+ " b.CREATEDBY,b.APPROVEDBY,b.APPROVEDDATE,b.LASTUPDATEDBY,"
					+ " b.LASTUPDATEDDATE,b.CREATEDDATE,c.FACILTYORSITE," + " c.HIERARCHYLEVEL,c.FACILITY,c.MUWI"
					+ " from ((IOP.JSAHEADER as a Inner Join IOP.JSAREVIEW"
					+ " as b on a.PERMITNUMBER = b.PERMITNUMBER)" + " Inner join IOP .JSA_LOCATION as c on"
					+ " a.PERMITNUMBER = c.PERMITNUMBER) where "
					+ "c.FACILTYORSITE = :facilityOrSite AND a.ISACTIVE = :isActive";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("facilityOrSite", facilityOrSite);
			q.setParameter("isActive", isActive);
			obj = q.getResultList();
			if(obj == null)
				return null;
			for (Object[] a : obj) {
				IOPJsaDto temp = new IOPJsaDto();
				temp.setPERMITNUMBER((Integer)a[0]);
				temp.setJSAPERMITNUMBER((String)a[1]);
				temp.setHASCWP((Byte)a[2]);
				temp.setHASHWP((Byte)a[3]);
				temp.setHASCSE((Byte)a[4]);
				temp.setTASKDESCRIPTION((String)a[5]);
				temp.setIDENTIFYMOSTSERIOUSPOTENTIALINJURY((String)a[6]);
				temp.setISACTIVE((Byte)a[7]);
				temp.setSTATUS((String)a[8]);
				temp.setCREATEDBY((String)a[9]);
				temp.setAPPROVEDBY((String)a[10]);
				temp.setAPPROVEDDATE((Date)a[11]);
				temp.setLASTUPDATEDBY((String)a[12]);
				temp.setLASTUPDATEDDATE((Date)a[13]);
				temp.setCREATEDDATE((Date)a[14]);
				temp.setFACILTYORSITE((String)a[15]);
				temp.setHIERARCHYLEVEL((String)a[16]);
				temp.setFACILITY((String)a[17]);
				temp.setMUWI((String)a[18]);
				data.add(temp);
			}
			return data;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

}
