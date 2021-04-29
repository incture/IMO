package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaHazardsDroppedDto;

@Repository
public class JsaHazardsDroppedDao extends BaseDao {
	public void insertJsaHazardsDropped(String permitNumber, JsaHazardsDroppedDto jsaHazardsDroppedDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSDROPPED\" VALUES (?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsDroppedDto.getDroppedObjects());
			query.setParameter(3, jsaHazardsDroppedDto.getMarkRestrictEntry());
			query.setParameter(4, jsaHazardsDroppedDto.getUseLiftingEquipmentToRaise());
			query.setParameter(5, jsaHazardsDroppedDto.getSecureTools());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsDroppedDto getJsaHazardsDroppedDto(String permitNum){
		List<Object[]> obj;
		JsaHazardsDroppedDto jsaHazardsDroppedDto = new JsaHazardsDroppedDto();
		try{
			String sql = "select distinct PERMITNUMBER, DROPPEDOBJECTS,MARKRESTRICTENTRY,USELIFTINGEQUIPMENTTORAISE, "
					+ " SECURETOOLS from IOP.JSAHAZARDSDROPPED where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			
			for (Object[] a : obj) {
				jsaHazardsDroppedDto.setPermitNumber((Integer) a[0]);
				jsaHazardsDroppedDto.setDroppedObjects(Integer.parseInt(a[1].toString()));
				jsaHazardsDroppedDto.setMarkRestrictEntry(Integer.parseInt(a[2].toString()));
				jsaHazardsDroppedDto.setUseLiftingEquipmentToRaise(Integer.parseInt(a[3].toString()));
				jsaHazardsDroppedDto.setSecureTools(Integer.parseInt(a[4].toString()));
			}
			return jsaHazardsDroppedDto;
			
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsDropped(JsaHazardsDroppedDto jsaHazardsDroppedDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSDROPPED\" SET  \"DROPPEDOBJECTS\"=?,\"MARKRESTRICTENTRY\"=?,\"USELIFTINGEQUIPMENTTORAISE\"=?," +
        "\"SECURETOOLS\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsDropped sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsDroppedDto.getDroppedObjects());
			query.setParameter(2, jsaHazardsDroppedDto.getMarkRestrictEntry());
			query.setParameter(3, jsaHazardsDroppedDto.getUseLiftingEquipmentToRaise());
			query.setParameter(4, jsaHazardsDroppedDto.getSecureTools());
			query.setParameter(5, jsaHazardsDroppedDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
