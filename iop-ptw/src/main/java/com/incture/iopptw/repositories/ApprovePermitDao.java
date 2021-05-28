package com.incture.iopptw.repositories;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.ApprovePermitDto;

@Repository("ApprovePermitDao")
public class ApprovePermitDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	@Autowired
	private SessionFactory sessionFactory;

	public Integer approvePermit(ApprovePermitDto approvePermitDto) {
		try {
			logger.info("ApprovePermitDto: " + approvePermitDto);
			String sql1 = " UPDATE IOP.PTWHEADER SET STATUS=:status where PERMITNUMBER=:permitNumber AND ISCWP=:iscwp AND ISHWP=:ishwp AND ISCSE=:iscse ";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query q1 = session.createNativeQuery(sql1);
			q1.setParameter("status", approvePermitDto.getStatus());
			q1.setParameter("permitNumber", approvePermitDto.getPtwApprovalDto().getPermitNumber());
			q1.setParameter("iscwp", approvePermitDto.getPtwApprovalDto().getIsCWP());
			q1.setParameter("ishwp", approvePermitDto.getPtwApprovalDto().getIsHWP());
			q1.setParameter("iscse", approvePermitDto.getPtwApprovalDto().getIsCSE());
			logger.info("1st sql : " + sql1);
			q1.executeUpdate();

			BigInteger serialNumber = keyGeneratorDao.getPtwApprovalSerialNo();
			logger.info("serialNumber: " + serialNumber);

			String sql3 = " INSERT INTO IOP.PTWAPPROVAL(SERIALNO,PERMITNUMBER,ISCWP,ISHWP,ISCSE,"
					+ "ISWORKSAFETOPERFORM,PREJOBWALKTHROUGHBY,APPROVEDBY,APPROVALDATE,CONTROLBOARDDISTRIBUTION,"
					+ "WORKSITEDISTRIBUTION,SIMOPSDISTRIBUTION,OTHERDISTRIBUTION,PICNAME,PICDATE,SUPERITENDENTNAME,"
					+ "SUPERITENDENTDATE) VALUES"
					+ " (:sNo,:pNo,:cwp,:hwp,:cse,:wsp,:pjwt,:approvedBy,:approvalDate,:cbd,:wsd,:sd,:od,:picName,:picDate,:sName,:sDate)";

			Query q3 = session.createNativeQuery(sql3);

			q3.setParameter("sNo", serialNumber);
			q3.setParameter("pNo", approvePermitDto.getPtwApprovalDto().getPermitNumber());
			q3.setParameter("cwp", approvePermitDto.getPtwApprovalDto().getIsCWP());
			q3.setParameter("hwp", approvePermitDto.getPtwApprovalDto().getIsHWP());
			q3.setParameter("cse", approvePermitDto.getPtwApprovalDto().getIsCSE());
			q3.setParameter("wsp", approvePermitDto.getPtwApprovalDto().getIsWorkSafeToPerform());
			q3.setParameter("pjwt", approvePermitDto.getPtwApprovalDto().getPreJobWalkthroughBy());
			q3.setParameter("approvedBy", approvePermitDto.getPtwApprovalDto().getApprovedBy());
			if (approvePermitDto.getPtwApprovalDto().getApprovalDate() == null) {
				Date d1 = new Date();
				q3.setParameter("approvalDate", d1);
			} else {
				q3.setParameter("approvalDate", approvePermitDto.getPtwApprovalDto().getApprovalDate());
			}

			q3.setParameter("cbd", approvePermitDto.getPtwApprovalDto().getControlBoardDistribution());
			q3.setParameter("wsd", approvePermitDto.getPtwApprovalDto().getWorksiteDistribution());
			q3.setParameter("sd", approvePermitDto.getPtwApprovalDto().getSimopsDistribution());
			q3.setParameter("od", approvePermitDto.getPtwApprovalDto().getOtherDistribution());
			q3.setParameter("picName", approvePermitDto.getPtwApprovalDto().getPicName());
			if (approvePermitDto.getPtwApprovalDto().getPicDate() == null) {
				Date d2 = new Date();
				q3.setParameter("picDate", d2);
			} else {
				q3.setParameter("picDate", approvePermitDto.getPtwApprovalDto().getPicDate());
			}

			q3.setParameter("sName", approvePermitDto.getPtwApprovalDto().getSuperitendentName());
			if (approvePermitDto.getPtwApprovalDto().getSuperitendentDate() == null) {
				Date d3 = new Date();
				q3.setParameter("sDate", d3);
			} else {
				q3.setParameter("sDate", approvePermitDto.getPtwApprovalDto().getSuperitendentDate());
			}

			logger.info("2nd sql : " + sql3);
			q3.executeUpdate();
			tx.commit();
			session.close();
			return approvePermitDto.getPtwApprovalDto().getPermitNumber();

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

};