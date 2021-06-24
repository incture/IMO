package com.incture.iopptw.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class IOPJsaDto {
	private Integer PERMITNUMBER;
	private String JSAPERMITNUMBER;
	private Byte HASCWP;
	private Byte HASHWP;
	private Byte HASCSE;
	private String TASKDESCRIPTION;
	private String IDENTIFYMOSTSERIOUSPOTENTIALINJURY;
	private Byte ISACTIVE;
	private String STATUS;
	private String CREATEDBY;
	private String APPROVEDBY;
	private Date APPROVEDDATE;
	private String LASTUPDATEDBY;
	private Date LASTUPDATEDDATE;
	private Date CREATEDDATE;
	private String FACILTYORSITE;
	private String HIERARCHYLEVEL;
	private String FACILITY;
	private String MUWI;
}
