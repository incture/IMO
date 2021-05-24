package com.incture.iopptw.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Test")
@Data
public class TestDo {
	@Id
	private int id;
}
