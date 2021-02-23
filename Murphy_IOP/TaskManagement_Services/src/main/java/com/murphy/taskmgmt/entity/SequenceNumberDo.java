package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEQUENCE_NUMBER")
public class SequenceNumberDo implements BaseDo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SEQ_CATEGORY", length = 50)
	private String seqCategory;
	
	@Column(name = "NEXT_AVAILABLE_NUMBER")
	private int nextAvailableNum;
	
	public String getSeqCategory() {
		return seqCategory;
	}

	public void setSeqCategory(String seqCategory) {
		this.seqCategory = seqCategory;
	}

	public int getNextAvailableNum() {
		return nextAvailableNum;
	}

	public void setNextAvailableNum(int nextAvailableNum) {
		this.nextAvailableNum = nextAvailableNum;
	}

	@Override
	public Object getPrimaryKey() {
		return seqCategory;
	}
}
