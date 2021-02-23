package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HSE_DOCUMENTS")
public class HseDocumentDo implements BaseDo {
	@Id
	@Column(name = "ID", length = 100)
	private String docId = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "DOCUMENT_ID", length = 100)
	private String documentId;

	@Column(name = "URL", length = 500)
	private String url;

	@Column(name = "VERSION")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int docVersion;

	@Column(name = "DATE_UPLOADED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(int docVersion) {
		this.docVersion = docVersion;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
