package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.LocalDate;

@Entity
@Table(name = "PIGGING_HISTORY")
public class PiggingHistoryDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name="SEQ_ID")
    private int seqId;
    
    @Column(name = "EQUIPMENT_ID", length = 20)
	private String equipmentId;

	@Column(name = "LAST_COMPLETED")
	private Date lastCompletedOn;

	@Column(name = "DUE_DATE")
	private Date dueDate;
	
	
	

	public String getEquipmentId() {
		return equipmentId;
	}




	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}




	public Date getLastCompletedOn() {
		return lastCompletedOn;
	}




	public void setLastCompletedOn(Date lastCompletedOn) {
		this.lastCompletedOn = new LocalDate(lastCompletedOn).toDate(); 
	}




	public Date getDueDate() {
		return dueDate;
	}




	public void setDueDate(Date dueDate) {
		this.dueDate = new LocalDate(dueDate).toDate();
	}




	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return seqId;
	}

}
