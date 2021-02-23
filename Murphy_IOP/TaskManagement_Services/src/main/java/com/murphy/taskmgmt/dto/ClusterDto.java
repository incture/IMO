package com.murphy.taskmgmt.dto;

import java.util.Arrays;

import weka.core.Instances;

public class ClusterDto {

	private int[] assignments;
	private Instances centers;
	public int[] getAssignments() {
		return assignments;
	}
	public void setAssignments(int[] assignments) {
		this.assignments = assignments;
	}
	public Instances getCenters() {
		return centers;
	}
	public void setCenters(Instances centers) {
		this.centers = centers;
	}
	@Override
	public String toString() {
		return "ClusterDto [assignments=" + Arrays.toString(assignments) + ", centers=" + centers + "]";
	}
	
	
}
