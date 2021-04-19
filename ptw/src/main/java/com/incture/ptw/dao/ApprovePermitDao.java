package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.ApprovePermitDto;

@Repository("ApprovePermitDao")
public class ApprovePermitDao extends BaseDao {
	public Integer approvePermit(ApprovePermitDto approvePermitDto) {
		try {
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
			Integer serialNumber =  (Integer) q2.getSingleResult();
			
			String sql3 = " INSERT INTO IOP.PTWAPPROVAL VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Query q3 = getSession().createNativeQuery(sql2);
			q3.setParameter(1, serialNumber);
			q3.setParameter(2, approvePermitDto.getPtwApprovalDto().getPermitNumber());
			q3.setParameter(3, approvePermitDto.getPtwApprovalDto().getIsCwp());
			q3.setParameter(4, approvePermitDto.getPtwApprovalDto().getIsHwp());
			q3.setParameter(5, approvePermitDto.getPtwApprovalDto().getIsCse());
			q3.setParameter(6, approvePermitDto.getPtwApprovalDto().getIsWorkSafeToPerform());
			q3.setParameter(7, approvePermitDto.getPtwApprovalDto().getPreJobWalkThroughBy());
			q3.setParameter(8, approvePermitDto.getPtwApprovalDto().getApprovedBy());
			q3.setParameter(9, approvePermitDto.getPtwApprovalDto().getApprovalDate());
			q3.setParameter(10, approvePermitDto.getPtwApprovalDto().getControlBoardDistribution());
			q3.setParameter(11, approvePermitDto.getPtwApprovalDto().getWorkSiteDistribution());
			q3.setParameter(12, approvePermitDto.getPtwApprovalDto().getSimopsDistribution());
			q3.setParameter(13, approvePermitDto.getPtwApprovalDto().getOtherDistribution());
			q3.setParameter(14, approvePermitDto.getPtwApprovalDto().getPicName());
			q3.setParameter(15, approvePermitDto.getPtwApprovalDto().getPicDate());
			q3.setParameter(16, approvePermitDto.getPtwApprovalDto().getSuperItendentName());
			q3.setParameter(17, approvePermitDto.getPtwApprovalDto().getSuperItendentDate());
			logger.info("3rd sql : " + sql3);
			q3.executeUpdate();

			return approvePermitDto.getPtwApprovalDto().getPermitNumber();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
