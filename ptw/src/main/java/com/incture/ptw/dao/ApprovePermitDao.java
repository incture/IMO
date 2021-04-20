package com.incture.ptw.dao;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.ApprovePermitDto;

@Repository("ApprovePermitDao")
public class ApprovePermitDao extends BaseDao {
	public Integer approvePermit(ApprovePermitDto approvePermitDto) {
		try {
			logger.info("ApprovePermitDto: " + approvePermitDto);
			String sql1 = " UPDATE IOP.PTWHEADER SET STATUS=:status where PERMITNUMBER=:permitNumber AND ISCWP=:iscwp AND ISHWP=:ishwp AND ISCSE=:iscse ";
			Query q1 = getSession().createNativeQuery(sql1);
			q1.setParameter("status", approvePermitDto.getStatus());
			q1.setParameter("permitNumber", approvePermitDto.getPtwApprovalDto().getPermitNumber());
			q1.setParameter("iscwp", approvePermitDto.getPtwApprovalDto().getIsCwp());
			q1.setParameter("ishwp", approvePermitDto.getPtwApprovalDto().getIsHwp());
			q1.setParameter("iscse", approvePermitDto.getPtwApprovalDto().getIsCse());
			logger.info("1st sql : " + sql1);
			q1.executeUpdate();

			String sql2 = "select IOP.PTWAPPROVAL_SEQ.NEXTVAL FROM DUMMY";
			Query q2 = getSession().createNativeQuery(sql2);
			logger.info("2nd sql : " + sql2);
			BigInteger serialNumber = (BigInteger) q2.getSingleResult();
			logger.info("serialNumber: " + serialNumber);

			String sql3 = " INSERT INTO IOP.PTWAPPROVAL(SERIALNO,PERMITNUMBER,ISCWP,ISHWP,ISCSE,"
					+ "ISWORKSAFETOPERFORM,PREJOBWALKTHROUGHBY,APPROVEDBY,APPROVALDATE,CONTROLBOARDDISTRIBUTION,"
					+ "WORKSITEDISTRIBUTION,SIMOPSDISTRIBUTION,OTHERDISTRIBUTION,PICNAME,PICDATE,SUPERITENDENTNAME,"
					+ "SUPERITENDENTDATE) VALUES"
					+ " (:sNo,:pNo,:cwp,:hwp,:cse,:wsp,:pjwt,:approvedBy,:approvalDate,:cbd,:wsd,:sd,:od,:picName,:picDate,:sName,:sDate)";
			Query q3 = getSession().createNativeQuery(sql2);
			q3.setParameter("sNo", serialNumber.intValue());
			q3.setParameter("pNo", approvePermitDto.getPtwApprovalDto().getPermitNumber());
			q3.setParameter("cwp", approvePermitDto.getPtwApprovalDto().getIsCwp());
			q3.setParameter("hwp", approvePermitDto.getPtwApprovalDto().getIsHwp());
			q3.setParameter("cse", approvePermitDto.getPtwApprovalDto().getIsCse());
			q3.setParameter("wsp", approvePermitDto.getPtwApprovalDto().getIsWorkSafeToPerform());
			q3.setParameter("pjwt", approvePermitDto.getPtwApprovalDto().getPreJobWalkThroughBy());
			q3.setParameter("approvedBy", approvePermitDto.getPtwApprovalDto().getApprovedBy());
			if (approvePermitDto.getPtwApprovalDto().getApprovalDate() == null) {
				Date d1 = new Date();
				q3.setParameter("approvalDate", d1);
			} else {
				q3.setParameter("approvalDate", approvePermitDto.getPtwApprovalDto().getApprovalDate());
			}

			q3.setParameter("cbd", approvePermitDto.getPtwApprovalDto().getControlBoardDistribution());
			q3.setParameter("wsd", approvePermitDto.getPtwApprovalDto().getWorkSiteDistribution());
			q3.setParameter("sd", approvePermitDto.getPtwApprovalDto().getSimopsDistribution());
			q3.setParameter("od", approvePermitDto.getPtwApprovalDto().getOtherDistribution());
			q3.setParameter("picName", approvePermitDto.getPtwApprovalDto().getPicName());
			if (approvePermitDto.getPtwApprovalDto().getPicDate() == null) {
				Date d2 = new Date();
				q3.setParameter("picDate", d2);
			} else {
				q3.setParameter("picDate", approvePermitDto.getPtwApprovalDto().getPicDate());
			}

			q3.setParameter("sName", approvePermitDto.getPtwApprovalDto().getSuperItendentName());
			if (approvePermitDto.getPtwApprovalDto().getSuperItendentDate() == null) {
				Date d3 = new Date();
				q3.setParameter("sDate", d3);
			} else {
				q3.setParameter("sDate", approvePermitDto.getPtwApprovalDto().getSuperItendentDate());
			}

			logger.info("3rd sql : " + sql3);
			q3.executeUpdate();

			return approvePermitDto.getPtwApprovalDto().getPermitNumber();

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

};