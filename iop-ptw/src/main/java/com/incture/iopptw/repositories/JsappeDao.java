package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsappeDto;

@Repository
public class JsappeDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;
	public void insertJsappe(String permitNumber, JsappeDto jsappeDto) {
		logger.info("jsappeDto"+jsappeDto);
		try {
			String sql ="INSERT INTO \"IOP\".\"JSA_PPE\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Session session= sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsappeDto.getHardHat());
			query.setParameter(3, jsappeDto.getSafetyBoot());
			query.setParameter(4, jsappeDto.getGoggles());
			query.setParameter(5, jsappeDto.getFaceShield());
			query.setParameter(6, jsappeDto.getSafetyGlasses());
			query.setParameter(7, jsappeDto.getSingleEar());
			query.setParameter(8, jsappeDto.getDoubleEars());
			query.setParameter(9, jsappeDto.getRespiratorTypeDescription());
			query.setParameter(10, jsappeDto.getNeedSCBA());
			query.setParameter(11, jsappeDto.getNeedDustMask());
			query.setParameter(12, jsappeDto.getCottonGlove());
			query.setParameter(13, jsappeDto.getLeatherGlove());
			query.setParameter(14, jsappeDto.getImpactProtection());
			query.setParameter(15, jsappeDto.getGloveDescription());
			query.setParameter(16, jsappeDto.getChemicalGloveDescription());
			query.setParameter(17, jsappeDto.getFallProtection());
			query.setParameter(18, jsappeDto.getFallRestraint());
			query.setParameter(19, jsappeDto.getChemicalSuit());
			query.setParameter(20, jsappeDto.getApron());
			query.setParameter(21, jsappeDto.getFlameResistantClothing());
			query.setParameter(22, jsappeDto.getOtherPPEDescription());
			query.setParameter(23, jsappeDto.getNeedFoulWeatherGear());
			query.setParameter(24, jsappeDto.getHaveConsentOfTaskLeader());
			query.setParameter(25, jsappeDto.getCompanyOfTaskLeader());
			query.executeUpdate();
			tx.commit();
			session.close();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void updateJsappe(JsappeDto jsappeDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSA_PPE\" SET  \"HARDHAT\"=?,\"SAFETYBOOT\"=?,\"GOGGLES\"=?," +
        "\"FACESHIELD\"=?,\"SAFETYGLASSES\"=?,\"SINGLEEAR\"=?,\"DOUBLEEARS\"=?,\"RESPIRATORTYPEDESCRIPTION\"=?," +
        "\"NEEDSCBA\"=?,\"NEEDDUSTMASK\"=?,\"COTTONGLOVE\"=?,\"LEATHERGLOVE\"=?," +
        "\"IMPACTPROTECTION\"=?,\"GLOVEDESCRIPTION\"=?,\"CHEMICALGLOVEDESCRIPTION\"=?,\"FALLPROTECTION\"=?," +
        "\"FALLRESTRAINT\"=?,\"CHEMICALSUIT\"=?,\"APRON\"=?,\"FLAMERESISTANTCLOTHING\"=?,\"OTHERPPEDESCRIPTION\"=?," +
        "\"NEEDFOULWEATHERGEAR\"=?,\"HAVECONSENTOFTASKLEADER\"=?,\"COMPANYOFTASKLEADER\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsappe sql" + sql);
			Session session= sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, jsappeDto.getHardHat());
			query.setParameter(2, jsappeDto.getSafetyBoot());
			query.setParameter(3, jsappeDto.getGoggles());
			query.setParameter(4, jsappeDto.getFaceShield());
			query.setParameter(5, jsappeDto.getSafetyGlasses());
			query.setParameter(6, jsappeDto.getSingleEar());
			query.setParameter(7, jsappeDto.getDoubleEars());
			query.setParameter(8, jsappeDto.getRespiratorTypeDescription());
			query.setParameter(9, jsappeDto.getNeedSCBA());
			query.setParameter(10, jsappeDto.getNeedDustMask());
			query.setParameter(11, jsappeDto.getCottonGlove());
			query.setParameter(12, jsappeDto.getLeatherGlove());
			query.setParameter(13, jsappeDto.getImpactProtection());
			query.setParameter(14, jsappeDto.getGloveDescription());
			query.setParameter(15, jsappeDto.getChemicalGloveDescription());
			query.setParameter(16, jsappeDto.getFallProtection());
			query.setParameter(17, jsappeDto.getFallRestraint());
			query.setParameter(18, jsappeDto.getChemicalSuit());
			query.setParameter(19, jsappeDto.getApron());
			query.setParameter(20, jsappeDto.getFlameResistantClothing());
			query.setParameter(21, jsappeDto.getOtherPPEDescription());
			query.setParameter(22, jsappeDto.getNeedFoulWeatherGear());
			query.setParameter(23, jsappeDto.getHaveConsentOfTaskLeader());
			query.setParameter(24, jsappeDto.getCompanyOfTaskLeader());
			query.setParameter(25, jsappeDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public JsappeDto getJsappe(String permitNum){
		JsappeDto jsappeDto = new JsappeDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, HARDHAT,SAFETYBOOT,GOGGLES,FACESHIELD,SAFETYGLASSES, "
					+ " SINGLEEAR,DOUBLEEARS,RESPIRATORTYPEDESCRIPTION,NEEDSCBA,NEEDDUSTMASK,COTTONGLOVE, "
					+ " LEATHERGLOVE,IMPACTPROTECTION,GLOVEDESCRIPTION,CHEMICALGLOVEDESCRIPTION,FALLPROTECTION, "
					+ " FALLRESTRAINT,CHEMICALSUIT,APRON,FLAMERESISTANTCLOTHING,OTHERPPEDESCRIPTION, "
					+ " NEEDFOULWEATHERGEAR,HAVECONSENTOFTASKLEADER,COMPANYOFTASKLEADER from IOP.JSA_PPE "
					+ " where PERMITNUMBER = :permitNum";
			logger.info("JSA_PPE sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsappeDto.setPermitNumber((Integer) a[0]);
				jsappeDto.setHardHat(Integer.parseInt(a[1].toString()));
				jsappeDto.setSafetyBoot(Integer.parseInt(a[2].toString()));
				jsappeDto.setGoggles(Integer.parseInt(a[3].toString()));
				jsappeDto.setFaceShield(Integer.parseInt(a[4].toString()));
				jsappeDto.setSafetyGlasses(Integer.parseInt(a[5].toString()));
				jsappeDto.setSingleEar(Integer.parseInt(a[6].toString()));
				jsappeDto.setDoubleEars(Integer.parseInt(a[7].toString()));
				jsappeDto.setRespiratorTypeDescription((String) a[8]);
				jsappeDto.setNeedSCBA(Integer.parseInt(a[9].toString()));
				jsappeDto.setNeedDustMask(Integer.parseInt(a[10].toString()));
				jsappeDto.setCottonGlove(Integer.parseInt(a[11].toString()));
				jsappeDto.setLeatherGlove(Integer.parseInt(a[12].toString()));
				jsappeDto.setImpactProtection(Integer.parseInt(a[13].toString()));
				jsappeDto.setGloveDescription((String) a[14]);
				jsappeDto.setChemicalGloveDescription((String) a[15]);
				jsappeDto.setFallProtection(Integer.parseInt(a[16].toString()));
				jsappeDto.setFallRestraint(Integer.parseInt(a[17].toString()));
				jsappeDto.setChemicalSuit(Integer.parseInt(a[18].toString()));
				jsappeDto.setApron(Integer.parseInt(a[19].toString()));
				jsappeDto.setFlameResistantClothing(Integer.parseInt(a[20].toString()));
				jsappeDto.setOtherPPEDescription((String) a[21]);
				jsappeDto.setNeedFoulWeatherGear((String) a[22]); 
				jsappeDto.setHaveConsentOfTaskLeader(Integer.parseInt(a[23].toString()));
				jsappeDto.setCompanyOfTaskLeader((String) a[24]);
			}
			return jsappeDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
