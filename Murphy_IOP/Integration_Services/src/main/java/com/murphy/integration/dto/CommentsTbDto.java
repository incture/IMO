package com.murphy.integration.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class CommentsTbDto {

private String comments;
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
private Date enteredDate;
private String uwiID;
private String userName;
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
private Date prodDate;

public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public Date getEnteredDate() {
	return enteredDate;
}
public void setEnteredDate(Date enteredDate) {
	this.enteredDate = enteredDate;
}
public  String getUwiID() {
	return uwiID;
}
public void setUwiId(String uwiID) {
	this.uwiID = uwiID;
}

public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public Date getProdDate() {
	return prodDate;
}
public void setProdDate(Date prodDate) {
	this.prodDate = prodDate;
}
public void setUwiID(String uwiID) {
	this.uwiID = uwiID;
}
@Override
public String toString() {
	return "CommentsTbDto [comments=" + comments + ", enteredDate=" + enteredDate + ", uwiID=" + uwiID + ", userName="
			+ userName + ", prodDate=" + prodDate + "]";
}

}
