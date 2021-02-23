package com.murphy.taskmgmt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.geotab.Coordinates;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.LocationDistancesDao;
import com.murphy.taskmgmt.dto.ArcGISResponseDto;
import com.murphy.taskmgmt.dto.LocationDistancesDto;
import com.murphy.taskmgmt.service.interfaces.LocationDistancesFacadeLocal;
import com.murphy.taskmgmt.util.ArcGISUtil;
import com.murphy.taskmgmt.util.GeoTabUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("LocationDistancesFacade")
public class LocationDistancesFacade implements LocationDistancesFacadeLocal{
	
	private static final Logger logger = LoggerFactory.getLogger(LocationDistancesFacade.class);
	
	@Autowired
	LocationDistancesDao locationDistanceDao;
	
	@Autowired
	HierarchyDao hierarchyDao;

	@Override
	public void insertMissingWellDistances(){
		
			List<String> uniqueWellDistances=locationDistanceDao.getUniqueWells();
//			HashMap<String, String> fields = hierarchyDao.getLocationByLocType(MurphyConstant.FIELD);
			LocationDistancesDto locationDistanceDto=null;
//			for(Entry<String, String> field:fields.entrySet()){
				try{
//					List<String> fieldList=new ArrayList<String>();
//					fieldList.add(field.getValue());
					List<String> fieldWells = hierarchyDao.getAllWellsLocCode();
//					logger.error("fieldWells "+fieldWells);
					for(int wellIndex=0;wellIndex<fieldWells.size();wellIndex++){
						String fromLocation = fieldWells.get(wellIndex);
						if(!uniqueWellDistances.contains(fromLocation)){
							Coordinates fromCoordinate = hierarchyDao.getCoordByCode(fromLocation);
							if(!ServicesUtil.isEmpty(fromCoordinate)){
							for(int i=0;i<fieldWells.size();i++){
								if(i!=wellIndex){
									String toLocation = fieldWells.get(i);
								Coordinates toCoordinate = hierarchyDao.getCoordByCode(toLocation);
								if(!ServicesUtil.isEmpty(fromCoordinate)&&!ServicesUtil.isEmpty(toCoordinate)){
									try{
								ArcGISResponseDto arcResponse = ArcGISUtil.getRoadDistance(fromCoordinate, toCoordinate);
								if (arcResponse.getResponseMessage().getStatusCode().equals(MurphyConstant.CODE_SUCCESS)) {
									locationDistanceDto=new LocationDistancesDto();
									locationDistanceDto.setFromLatitude(fromCoordinate.getLatitude());
									locationDistanceDto.setFromLongitude(fromCoordinate.getLongitude());
									locationDistanceDto.setToLatitude(toCoordinate.getLatitude());
									locationDistanceDto.setToLongitude(toCoordinate.getLongitude());
									locationDistanceDto.setFromLocCode(fromLocation);
									locationDistanceDto.setToLocCode(toLocation);
									locationDistanceDto.setRoadDriveTime(arcResponse.getTotalDriveTime());
									locationDistanceDto.setRoadTotalTime(arcResponse.getTotalTime());
									locationDistanceDto.setRoadLength(arcResponse.getTotalLength());
									locationDistanceDto.setCrowFlyLength(GeoTabUtil.getDistance(fromCoordinate, toCoordinate));
									locationDistanceDao.insertWell(locationDistanceDto);
								}
							}catch (Exception e) {
								logger.error("[LocationDistancesFacade][insertMissingWellDistances] Exception While Calling carl's Api for "+fromCoordinate+" "+ toCoordinate+" "+e.getMessage());
							}}else{
								logger.error("[LocationDistancesFacade][insertMissingWellDistances] Coordinates not found for "+toLocation);
							}}
						}}
							else{
								logger.error("[LocationDistancesFacade][insertMissingWellDistances] Coordinates not found for "+fromLocation);
							}
					}
			}
					}
				catch (Exception e) {
					logger.error("[LocationDistancesFacade][insertMissingWellDistances] Exception"+e.getMessage());
				}
//		}
			logger.error("[LocationDistancesFacade][insertMissingWellDistances] Insertion Completed");
		
	}
}
