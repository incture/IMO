package com.incture.iopptw.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="DFT_DEPARTMENTS")
@Data
public class DftDepartmentsDo {
	
	@Id
	private Integer id;
	
	@Column(name = "DEPARTMENTS")
	private String department;
}
