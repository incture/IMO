package com.murphy.taskmgmt.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.murphy.taskmgmt.entity.BaseDo;
import com.murphy.taskmgmt.entity.RouteLocationDoPK;

@Entity
@Table(name = "ROUTE_LOCATION_MAPPING")
public class RouteLocationDo  implements BaseDo, Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1887869996355527183L;

	@EmbeddedId
	private RouteLocationDoPK routeLocationDoPk;
		
	@Column(name = "TYPE", length = 40)
	private String locationType;
	
	
	
	public RouteLocationDoPK getRouteLocationDoPk() {
		return routeLocationDoPk;
	}



	public void setRouteLocationDoPk(RouteLocationDoPK routeLocationDoPk) {
		this.routeLocationDoPk = routeLocationDoPk;
	}



	public String getLocationType() {
		return locationType;
	}



	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public String toString() {
		return "RouteLocationDo [routeLocationDoPk=" + routeLocationDoPk + ", locationType=" + locationType + "]";
	}



	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
