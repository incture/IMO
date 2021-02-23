package com.murphy.taskmgmt.entity;

public class Node {
	

    public int NodeId;
    public double Node_X ,Node_Y; //Node Coordinates
    public String Code; //Node Demand if Customer
    public boolean IsRouted;
    private boolean IsDepot; //True if it Depot Node

    public Node(double depot_x,double depot_y) //Cunstructor for depot
    {
        this.NodeId = 0;
        this.Node_X = depot_x;
        this.Node_Y = depot_y;
        this.IsDepot = true;
    }

    public Node(int id ,double x, double y, String code) //Cunstructor for Customers
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.Code = code;
        this.IsRouted = false;
        this.IsDepot = false;
    }


}