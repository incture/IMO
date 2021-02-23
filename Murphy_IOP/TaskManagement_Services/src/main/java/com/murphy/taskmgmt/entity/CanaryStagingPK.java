package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class CanaryStagingPK implements BaseDo, Serializable {

	private static final long serialVersionUID = -7858573206799766633L;

	@Column(name = "MUWI_ID", length = 100, nullable = false)
	private String muwiId;

	@Column(name = "PARAM_TYPE", length = 100, nullable = false)
	private String parameterType;

	@Column(name = "CREATED_AT", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public CanaryStagingPK() {
		super();
	}

	public String getMuwiId() {
		return muwiId;
	}

	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((muwiId == null) ? 0 : muwiId.hashCode());
		result = prime * result + ((parameterType == null) ? 0 : parameterType.hashCode());
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
		CanaryStagingPK other = (CanaryStagingPK) obj;
		if (parameterType == null) {
			if (other.parameterType != null)
				return false;
		} else if (!parameterType.equals(other.parameterType))
			return false;
		if (muwiId == null) {
			if (other.muwiId != null)
				return false;
		} else if (!muwiId.equals(other.muwiId))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		return true;
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}
}