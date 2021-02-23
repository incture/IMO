package com.murphy.taskmgmt.dto;

import java.util.Date;

public class EIFormListDto {
		private String id;
		private String formId;
		private String locationId;
		private String locationName;
		private String equipmentId;
		private String status;
		private Date createdAt;
		private Date updatedAt;
		private EIFormDto eiFormDto;
		
		public EIFormDto getEiFormDto() {
			return eiFormDto;
		}
		public void setEiFormDto(EIFormDto eiFormDto) {
			this.eiFormDto = eiFormDto;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getFormId() {
			return formId;
		}
		public void setFormId(String formId) {
			this.formId = formId;
		}
		public String getLocationId() {
			return locationId;
		}
		public void setLocationId(String locationId) {
			this.locationId = locationId;
		}
		public String getLocationName() {
			return locationName;
		}
		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}
		public String getEquipmentId() {
			return equipmentId;
		}
		public void setEquipmentId(String equipmentId) {
			this.equipmentId = equipmentId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Date getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
		}
		public Date getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
		}
		
}
