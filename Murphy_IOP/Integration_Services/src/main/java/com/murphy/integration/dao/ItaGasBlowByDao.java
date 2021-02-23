package com.murphy.integration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.dto.GasBlowByDto;
import com.murphy.integration.util.DBConnections;
import com.murphy.integration.util.ServicesUtil;

public class ItaGasBlowByDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ItaGasBlowByDao.class);

	public Map<Integer,GasBlowByDto> getDailyOil(Connection connection, String oilMeterMerrickId, String meterName, Double dailyOilValue) {
		
		//PreparedStatement stmt = null;
		ResultSet resultSet = null;
		
		GasBlowByDto dailyOilDto=null;
		
		Map<Integer,GasBlowByDto> dailyOilDtoMap=new HashMap<>();
		
		String meterNames=ServicesUtil.getLikeQueryForGasBlowBy(meterName);
		
		try
		{

			
			String query = "Select c.UWI as Muwi, " + "c.MerrickID as CompMID," + "c.WellPlusCompletionName "
					+ ",mo.MeterName as OilMeter" + ",mo.Merrickid as OilMeterMID" + ",dailyoil.EstOilVol "
					+ ",dailyoil.RecordDate "
					+ "from (select * from [dbo].CompletionTb where activeflag = 1 and areaid = 1) c "
					+ "inner join "
					+ "(select * from [dbo].connecttb where EndDate ='1/1/2999' and UpstreamType = 1 and DownstreamType =2 "
					+ "and downstreamid in (select merrickid from [dbo].metertb where MeterType=210 ) "
					+ ")ct1 " + "on ct1.UpstreamID = c.MerrickID " + "inner join "
					+ "(select * from [dbo].metertb where "+meterNames
					+ " and MerrickID not in("+oilMeterMerrickId+")) mo "
					+ "on mo.MerrickID = ct1.DownstreamID " + "left join "
					+ "(select EstOilVol,RecordDate,md.MerrickID,MeterName from (select * from [dbo].metertb "
					+ "where MerrickID in (select downstreamid from [dbo].connecttb where EndDate  ='1/1/2999'and UpstreamType = 1 and DownstreamType =2) "
					+ "and MeterType=210) m left join (select * from [dbo].MeterDailyTb where 1=1 and RecordDate=  CAST( GETDATE()-1 AS DATE )) md on md.MerrickID=m.MerrickID ) dailyoil "
					+ "on dailyoil.MerrickID= mo.MerrickID where EstOilVol> "+dailyOilValue+" order by CompMID asc";
			
			resultSet=connection.prepareStatement(query).executeQuery();
			if(resultSet!=null)
			{
				while(resultSet.next())
				{
					if(!ServicesUtil.isEmpty(resultSet.getInt(2)))
					{
						dailyOilDto=new GasBlowByDto();
						dailyOilDto.setMuwiId(resultSet.getString(1));
						dailyOilDto.setMerrickID(resultSet.getInt(2));
						dailyOilDto.setWellName(resultSet.getString(3));
						dailyOilDto.setOilMeterName(resultSet.getString(4));
						dailyOilDto.setOilMeterMID(resultSet.getInt(5));
						dailyOilDto.setEstOilVol(Double.parseDouble((String.valueOf(resultSet.getFloat(6)))));
						dailyOilDto.setRecordDate(resultSet.getDate(7));
						
					
					dailyOilDtoMap.put(dailyOilDto.getMerrickID(), dailyOilDto);
					}
					
				}
			}
			
			return dailyOilDtoMap;
			
		}catch (Exception e) {
			logger.error("[Murphy][ItaGasBlowByDao][getDailyOil] "+e.getMessage());
			e.printStackTrace();
		}
		return dailyOilDtoMap;
	}

	public Map<Integer, GasBlowByDto> getDailyGas(Connection connection,Map<Integer, GasBlowByDto> oilGasMap, String gasMeterMerrickId, String meterName) {
		

		
		
		ResultSet resultSet = null;
		
		GasBlowByDto dailyGasDto=null;
		
		String meterNames=ServicesUtil.getLikeQueryForGasBlowBy(meterName);
		try
		{
			String query = "Select c.UWI as Muwi, " + "c.MerrickID as CompMID," + "c.WellPlusCompletionName "
					+ ",mg.MeterName as GasMeter" + ",mg.MerrickId as GasMeterMID" + ",dailygas.EstGasVolMCF "
					+ ",dailygas.RecordDate "
					+ "from (select * from [dbo].CompletionTb where activeflag = 1 and areaid = 1) c "
					+ "inner join "
					+ "(select * from [dbo].connecttb where EndDate ='1/1/2999' and UpstreamType = 1 and DownstreamType =2 "
					+ "and downstreamid in (select merrickid from [dbo].metertb where MeterType=220 ) "
					+ ")ct1 " + "on ct1.UpstreamID = c.MerrickID " + "inner join "
					+ "(select * from [dbo].metertb where "+meterNames
					+ " and MerrickID not in("+gasMeterMerrickId+")) mg "
					+ "on mg.MerrickID = ct1.DownstreamID " + "left join "
					+ "(select EstGasVolMCF,RecordDate,md.MerrickID,MeterName from (select * from [dbo].metertb "
					+ "where MerrickID in (select downstreamid from [dbo].connecttb where EndDate  ='1/1/2999'and UpstreamType = 1 and DownstreamType =2) "
					+ "and MeterType=220) m left join (select * from [dbo].MeterDailyTb where 1=1 and RecordDate=  CAST( GETDATE()-1 AS DATE )) md on md.MerrickID=m.MerrickID ) dailygas "
					+ "on dailygas.MerrickID= mg.MerrickID " + "order by CompMID asc";
			
			resultSet=connection.prepareStatement(query).executeQuery();
			if(resultSet!=null)
			{
				while(resultSet.next())
				{
					if(!ServicesUtil.isEmpty(resultSet.getInt(2)))
					{
						
						if(oilGasMap.containsKey(resultSet.getInt(2)))
						{
							dailyGasDto=oilGasMap.get(resultSet.getInt(2));
							
							dailyGasDto.setGasMeterName(resultSet.getString(4));
							dailyGasDto.setGasMeterMID(resultSet.getInt(5));
							dailyGasDto.setEstGasVol(Double.parseDouble((String.valueOf(resultSet.getFloat(6)))));
							dailyGasDto.setRecordDate(resultSet.getDate(7));
							
							oilGasMap.put(dailyGasDto.getMerrickID(), dailyGasDto);
						}
						
					
						
					}
					
				}
			}
			
			return oilGasMap;
			
		}catch (Exception e) {
			logger.error("[Murphy][ItaGasBlowByDao][getDailyGas] "+e.getMessage());
			e.printStackTrace();
		}
		return oilGasMap;
	
		
	}

	public Map<Integer, GasBlowByDto> getYearlyOil(Connection connection,Map<Integer, GasBlowByDto> oilMap, String oilMeterMerrickId, String meterName) {

		
		
		ResultSet resultSet = null;
		
		GasBlowByDto dailyOilDto=null;
		
		String meterNames=ServicesUtil.getLikeQueryForGasBlowBy(meterName);
		
		
		
		try
		{

			
			String query = "Select c.UWI as Muwi, " + "c.MerrickID as CompMID, " + "c.WellPlusCompletionName "
					+ ",mo.MeterName as OilMeter " + ",mo.Merrickid as OilMeterMID " + ",yearlyoil.SumOil "

					+ "from (select * from [dbo].CompletionTb where activeflag = 1 and areaid = 1) c "

					+ "inner join "
					+ "(select * from [dbo].connecttb where EndDate ='1/1/2999' and UpstreamType = 1 and DownstreamType =2 "
					+ "and downstreamid in (select merrickid from [dbo].metertb where MeterType=210) "
					+ ") ct2 " + "on ct2.UpstreamID = c.MerrickID " + "inner join "

					+ "(select * from [dbo].metertb where "+meterNames
					+ " and MerrickID not in("+oilMeterMerrickId+")) mo "
					+ "on mo.MerrickID = ct2.DownstreamID  "

					+ "left join "

					+ "(select sum(EstOilVol) as SumOil, md.Merrickid from "
					+ "(select * from [dbo].metertb where metertype = 210) m " + "inner join "
					+ "[dbo].meterdailytb md " + "on md.MerrickID=m.MerrickID "
					+ "left join (select * from [dbo].ConnectTb where enddate = '1/1/2999') c on m.MerrickID=c.DownstreamID and DownstreamType=2 "
					+ "left join [dbo].completiontb o on c.UpstreamID=o.MerrickID and UpstreamType=1 "
					+ "left join [dbo].DowntimeReasonTb dtr on o.MerrickID=dtr.ObjectMerrickID and dtr.ObjectType=1 and dtr.StartDate=md.RecordDate "
					+ "where RecordDate  between GETDATE ()-360 and GETDATE () " + "and dtr.StartDate is null "
					+ "group by md.MerrickID) yearlyoil " + "on yearlyoil.MerrickID= mo.MerrickID "

					+ "order by CompMID asc";
			
			resultSet=connection.prepareStatement(query).executeQuery();
			if(resultSet!=null)
			{
				while(resultSet.next())
				{
					if(!ServicesUtil.isEmpty(resultSet.getInt(2)))
					{
						if(oilMap.containsKey(resultSet.getInt(2)))
						{
							dailyOilDto=oilMap.get(resultSet.getInt(2));
							dailyOilDto.setTotEstOilVol(Double.parseDouble(String.valueOf(resultSet.getFloat(6))));
							
							oilMap.put(dailyOilDto.getMerrickID(), dailyOilDto);
						}
						
					
					
					}
					
				}
			}
			
			return oilMap;
			
		}catch (Exception e) {
			logger.error("[Murphy][ItaGasBlowByDao][getYearlyOil] "+e.getMessage());
			e.printStackTrace();
		}
		return oilMap;
	
	}

	public Map<Integer, GasBlowByDto> getYearlyGas(Connection connection,Map<Integer, GasBlowByDto> gasMap, String gasMeterMerrickId, String meterName) {

		
		
		ResultSet resultSet = null;
		
		GasBlowByDto dailyGasDto=null;
		
		Double yearlyGas;
		Double dayCount;
		
		String meterNames=ServicesUtil.getLikeQueryForGasBlowBy(meterName);
	
		
		try
		{

			
			String query = "Select c.UWI as Muwi," + "c.MerrickID as CompMID, " + "c.WellPlusCompletionName"
					+ ",mg.MeterName as GasMeter" + ",mg.MerrickId as GasMeterMID" + ",yearlygas.SumGas" + ",yearlygas.DayCount "
					+ "from (select * from [dbo].CompletionTb where activeflag = 1 and areaid = 1) c "

					+ "inner join "

					+ "(select * from [dbo].connecttb where EndDate ='1/1/2999' and UpstreamType = 1 and DownstreamType =2 "
					+ "and downstreamid in (select merrickid from [dbo].metertb where MeterType=220 ) "
					+ ")ct1 " + "on ct1.UpstreamID = c.MerrickID "

					+ "inner join "

					+ "(select * from [dbo].metertb where "+meterNames
					+ " and MerrickID not in("+gasMeterMerrickId+")) mg "
					+ "on mg.MerrickID = ct1.DownstreamID "

					+ "left join "

					+ "(select sum(EstGasVolMCF) as SumGas,count(EstGasVolMCF) as DayCount, md.Merrickid from "
					+ "(select * from [dbo].metertb where metertype = 220) m " + "inner join "
					+ "[dbo].meterdailytb md " + "on md.MerrickID=m.MerrickID "
					+ "	  left join (select * from [dbo].ConnectTb where enddate = '1/1/2999') c on m.MerrickID=c.DownstreamID and DownstreamType=2 "
					+ "	  left join [dbo].completiontb o on c.UpstreamID=o.MerrickID and UpstreamType=1 "
					+ "	  left join [dbo].DowntimeReasonTb dtr on o.MerrickID=dtr.ObjectMerrickID and dtr.ObjectType=1 and dtr.StartDate=md.RecordDate "
					+ "where RecordDate  between GETDATE ()-360 and GETDATE () " + "and dtr.StartDate is null "
					+ "group by md.MerrickID) yearlygas " + "on yearlygas.MerrickID= mg.MerrickID "
					+ "order by CompMID asc";
			
			resultSet=connection.prepareStatement(query).executeQuery();
			if(resultSet!=null)
			{
				while(resultSet.next())
				{
					if(!ServicesUtil.isEmpty(resultSet.getInt(2)))
					{
						if(gasMap.containsKey(resultSet.getInt(2)))
						{
							dailyGasDto=gasMap.get(resultSet.getInt(2));
							dailyGasDto.setTotEstGasVol(Double.parseDouble(String.valueOf(resultSet.getFloat(6))));
							dailyGasDto.setDayCount(String.valueOf(resultSet.getInt(7)));
							
							//Calculating avg. yearly gas
							
							
							yearlyGas = (dailyGasDto.getTotEstGasVol()==0)?0.00001:dailyGasDto.getTotEstGasVol();
							dayCount = Double.parseDouble(dailyGasDto.getDayCount());

							if (dayCount > 0) {
								Double avgYearlyGas = yearlyGas / dayCount;

								dailyGasDto.setAvgYearlyGas(avgYearlyGas);
							}

							 //
							
							gasMap.put(dailyGasDto.getMerrickID(), dailyGasDto);
						}
						
						
						
						
					
					}
					
				}
			}
			
			return gasMap;
			
		}catch (Exception e) {
			logger.error("[Murphy][ItaGasBlowByDao][getYearlyGas] "+e.getMessage());
			e.printStackTrace();
		}
		return gasMap;
	
	}

	public Map<Integer, GasBlowByDto> getOilGasRatio(Map<Integer, GasBlowByDto> oilGasMap) {
		
		GasBlowByDto blowByDto;
		Double dailyOilGasRatio;
		Double yearlyOilGasRatio;
		Double dailyGas;
		Double yearlyGas;
		
		try
		{
		
			for(Map.Entry<Integer,GasBlowByDto> map:oilGasMap.entrySet())
		{

			blowByDto = map.getValue();

			
				
			if(!ServicesUtil.isEmpty(blowByDto.getEstGasVol()) && !ServicesUtil.isEmpty(blowByDto.getTotEstGasVol()))
			{	
			dailyGas=(blowByDto.getEstGasVol()==0)?0.00001:blowByDto.getEstGasVol();
			yearlyGas=(blowByDto.getTotEstGasVol()==0)?0.00001:blowByDto.getTotEstGasVol();
				
			
				/// Daily oil gas ratio//

				if (dailyGas > blowByDto.getAvgYearlyGas()) {
					dailyOilGasRatio = blowByDto.getEstOilVol() / dailyGas;
					map.getValue().setDailyOilGasRatio(dailyOilGasRatio);
				} else if (dailyGas < blowByDto.getAvgYearlyGas()) {
					dailyOilGasRatio = blowByDto.getEstOilVol() / blowByDto.getAvgYearlyGas();
					map.getValue().setDailyOilGasRatio(dailyOilGasRatio);
				}

				/// Yearly oil gas ratio///
					
				yearlyOilGasRatio = blowByDto.getTotEstOilVol() / yearlyGas;
				map.getValue().setYearlyOilGasRatio(yearlyOilGasRatio);
				

			}

		}
			
			return oilGasMap;
			
		}catch (Exception e) {
			logger.error("[Murphy][ItaGasBlowByDao][getOilGasRatio "+e.getMessage());
			e.printStackTrace();
		}
		return oilGasMap;
	}

	public List<String> getLocCode(List<String> gasBlowByMuwiList) throws Exception {
		
		List<String>locCodeList=new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Connection connection=null;
		
		try
		{
		
		connection=DBConnections.createConnectionForHana();
		String muwiList=ServicesUtil.getStringFromList(gasBlowByMuwiList);
		
		
		if(connection!=null)
		{
			
			String query="select location_code from well_muwi where muwi in(?)";
			
			stmt=connection.prepareStatement(query);
			stmt.setString(1, muwiList);
			resultSet=stmt.executeQuery();
			
			if(resultSet!=null){
				while(resultSet.next())
				{
				locCodeList.add(resultSet.getString(1));
				}
			}
			
		}
		
		return locCodeList;
		
	}catch (Exception e) {

		logger.error("[ItaGasBlowByDao][getLocCode] : ERROR- Exception while fetching LocCode from database " + e.getMessage());
		e.printStackTrace();
		throw e;
	
		
	}finally
		{

		try {
			stmt.close();
			resultSet.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("[ItaGasBlowByDao][getLocCode] : ERROR- Exception while cleaning environment" + e.getMessage());
			e.printStackTrace();
		}
	
		}
	}


	
	
	
	
	
	

}
