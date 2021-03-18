package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMP_INFO")
public class EmpDetailsDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EMP_ID", length = 100)
	private String empId;

	@Column(name = "EMP_EMAIL", length = 100)
	private String empEmail;

	@Column(name = "EMP_STATUS", length = 100)
	private String emplStatus;

	@Column(name = "EMP_NAME", length = 100)
	private String empName;

	@Column(name = "DESIGNATION", length = 100)
	private String designation;

	@Column(name = "FOREMAN_NAME", length = 100)
	private String foremanName;

	@Column(name = "FIELD_TASK_USER")
	private int fieldTaskUser;

	@Column(name = "START_DATE", length = 100)
	private String startDate;

	@Column(name = "END_DATE", length = 100)
	private String endDate;

	@Column(name = "SUPERINTENDENT_ID", length = 100)
	private String superintendentId;

	@Column(name = "FOREMAN_ID", length = 100)
	private String foremanId;

	@Column(name = "FOREMAN_EMAIL", length = 100)
	private String foremanEmail;

	@Column(name = "FOREMAN_DESIGNATION", length = 100)
	private String foremanDesignation;

	@Column(name = "SUPERINTENDENT_NAME", length = 100)
	private String superintendentName;

	@Column(name = "SUPERINTENDENT_EMAIL", length = 100)
	private String superintendentEmail;

	@Column(name = "SUPERINTENDENT_DESIGNATION", length = 100)
	private String superintendentDesignation;

	public String getEmplStatus() {
		return emplStatus;
	}

	public void setEmplStatus(String emplStatus) {
		this.emplStatus = emplStatus;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getForemanName() {
		return foremanName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "EmpDetailsDo [empId=" + empId + ", empEmail=" + empEmail + ", emplStatus=" + emplStatus + ", empName="
				+ empName + ", designation=" + designation + ", foremanName=" + foremanName + ", fieldTaskUser="
				+ fieldTaskUser + ", startDate=" + startDate + ", endDate=" + endDate + ", superintendentId="
				+ superintendentId + ", foremanId=" + foremanId + ", foremanEmail=" + foremanEmail
				+ ", foremanDesignation=" + foremanDesignation + ", superintendentName=" + superintendentName
				+ ", superintendentEmail=" + superintendentEmail + ", superintendentDesignation="
				+ superintendentDesignation + "]";
	}

	public void setForemanName(String foremanName) {
		this.foremanName = foremanName;
	}

	public int getFieldTaskUser() {
		return fieldTaskUser;
	}

	public void setFieldTaskUser(int fieldTaskUser) {
		this.fieldTaskUser = fieldTaskUser;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSuperintendentId() {
		return superintendentId;
	}

	public void setSuperintendentId(String superintendentId) {
		this.superintendentId = superintendentId;
	}

	public String getForemanId() {
		return foremanId;
	}

	public void setForemanId(String foremanId) {
		this.foremanId = foremanId;
	}

	public String getForemanEmail() {
		return foremanEmail;
	}

	public void setForemanEmail(String foremanEmail) {
		this.foremanEmail = foremanEmail;
	}

	public String getForemanDesignation() {
		return foremanDesignation;
	}

	public void setForemanDesignation(String foremanDesignation) {
		this.foremanDesignation = foremanDesignation;
	}

	public String getSuperintendentName() {
		return superintendentName;
	}

	public void setSuperintendentName(String superintendentName) {
		this.superintendentName = superintendentName;
	}

	public String getSuperintendentEmail() {
		return superintendentEmail;
	}

	public void setSuperintendentEmail(String superintendentEmail) {
		this.superintendentEmail = superintendentEmail;
	}

	public String getSuperintendentDesignation() {
		return superintendentDesignation;
	}

	public void setSuperintendentDesignation(String superintendentDesignation) {
		this.superintendentDesignation = superintendentDesignation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Object getPrimaryKey() {
		return empId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((empId == null) ? 0 : empId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpDetailsDo other = (EmpDetailsDo) obj;
		if (empId == null) {
			if (other.empId != null)
				return false;
		} else if (!empId.equals(other.empId))
			return false;
		return true;
	}

}
