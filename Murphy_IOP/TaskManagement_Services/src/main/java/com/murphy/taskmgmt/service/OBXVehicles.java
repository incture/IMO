package com.murphy.taskmgmt.service;

import java.util.ArrayList;

import com.murphy.taskmgmt.entity.Node;

public class OBXVehicles {
	
	public int VehId;
    public ArrayList<Node> Route = new ArrayList<Node>();
    public int capacity;
    public int load;
    public int CurLoc;
    public boolean Closed;

    public OBXVehicles(int id, int cap)
    {
        this.VehId = id;
        this.capacity = cap;
        this.load = 20;
        this.CurLoc = 0; //In depot Initially
        this.Closed = false;
        this.Route.clear();
    }

    public void AddNode(Node Customer )//Add Customer to Vehicle Route
    {
        Route.add(Customer);
    //    this.load +=  Customer.demand;
        this.CurLoc = Customer.NodeId;
    }

    public boolean CheckIfFits(double dem) //Check if we have Capacity Violation
    {
        return ((load + dem <= capacity));
    }


}
