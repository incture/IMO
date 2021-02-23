package com.murphy.taskmgmt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.interfaces.EnersightProveDailyLocal;
import com.murphy.integration.service.EnersightProveDaily;
import com.murphy.taskmgmt.dao.CanaryStagingDao;
import com.murphy.taskmgmt.dao.FracOrientationDao;
import com.murphy.taskmgmt.dao.FracScenarioLookUpDao;
import com.murphy.taskmgmt.dao.FracWellStatusDao;
import com.murphy.taskmgmt.dao.FracZoneDao;
import com.murphy.taskmgmt.dao.HierarchyDao;
import com.murphy.taskmgmt.dao.OffsetFracPackDao;
import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.dto.FracDropDownResponseDto;
import com.murphy.taskmgmt.dto.FracHitDto;
import com.murphy.taskmgmt.dto.FracOrientationDto;
import com.murphy.taskmgmt.dto.FracOrientationResponseDto;
import com.murphy.taskmgmt.dto.FracPackEngViewDto;
import com.murphy.taskmgmt.dto.FracPackEngViewResponseDto;
import com.murphy.taskmgmt.dto.FracScenarioDto;
import com.murphy.taskmgmt.dto.FracScenarioLookUpDto;
import com.murphy.taskmgmt.dto.FracScenarioLookUpResponseDto;
import com.murphy.taskmgmt.dto.FracWellStatusDto;
import com.murphy.taskmgmt.dto.FracWellStatusResponseDto;
import com.murphy.taskmgmt.dto.FracZoneDto;
import com.murphy.taskmgmt.dto.FracZoneResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.OffsetFracPackDto;
import com.murphy.taskmgmt.dto.OffsetFracPackRequestDto;
import com.murphy.taskmgmt.dto.OffsetFracPackResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.OffsetFracPackFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.SequenceNumberGenLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.RestUtil;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("offsetFracPackFacade")
@Transactional
public class OffsetFracPackFacade implements OffsetFracPackFacadeLocal {

	@Autowired
	OffsetFracPackDao fracPackDao;

	@Autowired
	SequenceNumberGenLocal seqGenService;

	@Autowired
	CanaryStagingDao canaryStagingDao;

	@Autowired
	HierarchyDao hierarchyDao;

	@Autowired
	FracScenarioLookUpDao fracScenarioLookUpDao;

	@Autowired
	FracWellStatusDao fracWellStatusDao;

	@Autowired
	FracOrientationDao fracOrientationDao;

	@Autowired
	FracZoneDao fracZoneDao;
	// @Autowired
	// EnersightProveMonthlyLocal enersightProveMonthly;

	private static final Logger logger = LoggerFactory.getLogger(OffsetFracPackFacade.class);

	@Override
	public ResponseMessage createFracPack(OffsetFracPackRequestDto dto) {
		String description = dto.getDescription();
		List<OffsetFracPackDto> list = dto.getFracPacks();
		// String fracId = seqGenService.getNextSeqNumber("FRA", 4);
		// String fracId =UUID.randomUUID().toString();

		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			long fracId = fracPackDao.getLatestKey("OFFSET_FRAC_PACK", "FRAC_ID");
			//setMaxCasePSI from CANARY
			list=getCanaryFracData(list);
			
			if (!ServicesUtil.isEmpty(description))
				for (OffsetFracPackDto fracPack : list) {
					fracPack.setDescription(description);
					fracPack.setFracId(fracId);
					fracPack.setFracStatus(MurphyConstant.INPROGRESS);
					//Adding for CHG0036988 - to hold maximum of CASE PSI Reached
//					fracPack.setMaxCasePSI(fracPackDao.getActiveCasePressure(fracPack.getWellCode()));
					fracPack.setMaxCasePSI(fracPack.getActiveCasePressure());
					//CHG0036988 ends
					if (!ServicesUtil.isEmpty(fracPack.getScenario())) {
						String status = fracPackDao.getStatusBasedOnScenario(fracPack.getScenario());
						if (!ServicesUtil.isEmpty(status))
							fracPack.setWellStatus(status);
					}
				}

			fracPackDao.createFracPack(list);

			responseDto.setStatus(MurphyConstant.SUCCESS);
			responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
			responseDto.setMessage("Frac Pack " + fracId + " has been created successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[Murphy][FracPackFaced][createFracPack][error]" + e.getMessage());
			responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
			e.printStackTrace();
		}
		return responseDto;
	}

	/*
	 * @Override public ResponseMessage updateFacPack(OffsetFracPackDto dto) {
	 * return fracPackDao.updateFracPack(dto); }
	 */

	@Override
	public OffsetFracPackResponseDto getFracPack(String fracId, String fieldCode, String wellCode) {
		return fracPackDao.getFracPack(fracId, fieldCode, wellCode);
	}

	@Override
	public OffsetFracPackResponseDto getFackPacksByField(String fieldCode) {
		// TODO Auto-generated method stub
		return fracPackDao.getFracPacksByField(fieldCode);
	}

	@Override
	public OffsetFracPackRequestDto getBoed(OffsetFracPackRequestDto requestdto) {

		List<OffsetFracPackDto> list = requestdto.getFracPacks();
		Set<String> setmuwi = new HashSet<>();
		Map<String, String> mapBoed = new HashMap<>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			for (OffsetFracPackDto dto : list) {
				if (!ServicesUtil.isEmpty(dto.getWellCode()))
					setmuwi.add(dto.getWellCode());
			}

			EnersightProveDailyLocal enersightProveDaily = new EnersightProveDaily();
			mapBoed = enersightProveDaily.fetchFracData(setmuwi);

			for (OffsetFracPackDto dto : list) {

				String muwi = dto.getWellCode();
				double boed = 0;
				if (mapBoed.containsKey(dto.getWellCode()))
					boed = Double.parseDouble(mapBoed.get(dto.getWellCode()));
				String fieldCode = "";
				LocationHierarchyDto locationHierarchyDto = hierarchyDao.getWellDetailsForMuwi(muwi);
				if (!ServicesUtil.isEmpty(locationHierarchyDto))
					fieldCode = locationHierarchyDto.getField();

				dto.setFieldCode(fieldCode);

				dto.setBoed(boed);

			}
			responseMessage.setMessage("Boed Succesfully fetched");
			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			responseMessage.setMessage("Failed");
			logger.error(
					"[Murphy][FracPackFaced][getBoed][error] Exception while Interacting with DB " + e.getMessage());

		}
		requestdto.setResponseMessage(responseMessage);

		return requestdto;
	}

	@Override
	public FracPackEngViewResponseDto getEngView(String userRole) {
		FracPackEngViewResponseDto response = new FracPackEngViewResponseDto();
		List<FracPackEngViewDto> englist = new ArrayList<>();
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			List<OffsetFracPackDto> packs = fracPackDao.getFracPacks(userRole);
			List<String> muwis = new ArrayList<>();
			// List<CanaryStagingDto> canaryData = null;
			if (!ServicesUtil.isEmpty(packs)) {
				for (OffsetFracPackDto dto : packs) {
					muwis.add(dto.getWellCode());
				}

				// if (!ServicesUtil.isEmpty(getActiveValues(muwis)))
				// canaryData = getActiveValues(muwis);
				double activeTubePressure = 0, activeCasePressure = 0;
				for (OffsetFracPackDto pack : packs) {

//					activeTubePressure = fracPackDao.getActiveTubingPressure(pack.getWellCode());
//					activeCasePressure = fracPackDao.getActiveCasePressure(pack.getWellCode());
					
					activeTubePressure = ServicesUtil.isEmpty(pack.getActiveTubePressure())?0:pack.getActiveTubePressure();
					activeCasePressure =ServicesUtil.isEmpty( pack.getActiveCasePressure())?0: pack.getActiveCasePressure();
					
					FracHitDto fracHitData = getFracHit(pack.getFracId(), pack.getWellCode());
					FracPackEngViewDto dto = new FracPackEngViewDto();
					if (!ServicesUtil.isEmpty(pack.getFracId()))
						dto.setFracId(pack.getFracId());

					if (!ServicesUtil.isEmpty(pack.getStartAt()))
						dto.setFracDate(pack.getStartAt());

					if (!ServicesUtil.isEmpty(pack.getFieldCode()))
						dto.setFuncLocation(pack.getFieldCode());

					if (!ServicesUtil.isEmpty(pack.getWellName()))
						dto.setWellName(pack.getWellName());

					if (!ServicesUtil.isEmpty(pack.getWellStatus()))
						dto.setWellStatus(pack.getWellStatus());

					if (!ServicesUtil.isEmpty(pack.getWellCode()))
						dto.setWellCode(pack.getWellCode());

					if (!ServicesUtil.isEmpty(pack.getEstBolDate()))
						dto.setEstBolDate(pack.getEstBolDate());

					if (!ServicesUtil.isEmpty(pack.getBoed()))
						dto.setBoed(pack.getBoed());

					if (!ServicesUtil.isEmpty(pack.getProdImpact()))
						dto.setProdImpact(pack.getProdImpact());

					if (!ServicesUtil.isEmpty(pack.getMaxTubePressure()))
						dto.setMaxTubePressure(pack.getMaxTubePressure());

					// Adding for the incident INC0078316
					if (!ServicesUtil.isEmpty(pack.getScenario()))
						dto.setScenario(pack.getScenario());
					
					//Adding CHG0037278
					if (!ServicesUtil.isEmpty(pack.getZone()))
						dto.setZone(pack.getZone());
					
					if (!ServicesUtil.isEmpty(pack.getOrientation()))
						dto.setOrientation(pack.getOrientation());
					
					if (!ServicesUtil.isEmpty(pack.getFracStatus()))
						dto.setFracStatus(pack.getFracStatus());
					
					if (!ServicesUtil.isEmpty(pack.getStartAt()))
						dto.setStartAt(pack.getStartAt());
					
					if (!ServicesUtil.isEmpty(pack.getEndAt()))
						dto.setEndAt(pack.getEndAt());
					
					if (!ServicesUtil.isEmpty(pack.getDescription()))
						dto.setDescription(pack.getDescription());
					
					if (!ServicesUtil.isEmpty(pack.getUserId()))
						dto.setUserId(pack.getUserId());
					
					if (!ServicesUtil.isEmpty(pack.getUserRole()))
						dto.setUserRole(pack.getUserRole());
					
					//CHG0037278 change ends

					dto.setActiveCasePressure(Math.round(activeCasePressure * 100D) / 100D);
					dto.setActiveTubePressure(Math.round(activeTubePressure * 100D) / 100D);
					
//					dto.setMaxPsi(Math.round(activeTubePressure * 100D) / 100D);
					//Adding for CHG0036988
					if (!ServicesUtil.isEmpty(pack.getMaxCasePSI()))
						dto.setMaxPsi(Math.round(pack.getMaxCasePSI() * 100D) / 100D);
					//CHG0036988 ends

					if (!ServicesUtil.isEmpty(pack.getMaxCasePressure()))
						dto.setMaxCasePressure(pack.getMaxCasePressure());

					if (!ServicesUtil.isEmpty(pack.getDistFrac()))
						dto.setDistFrac(pack.getDistFrac());

					// Compare current date with Est bol date if it greater thn
					// overdue else sceduled
					Date currentDate = new Date();

					SimpleDateFormat s = new SimpleDateFormat();
					s.setTimeZone(TimeZone.getTimeZone("CST"));
					currentDate = new SimpleDateFormat().parse(s.format(currentDate));

					if (!ServicesUtil.isEmpty(pack.getEstBolDate())) {
						Date estBolDate = pack.getEstBolDate();
						estBolDate = new SimpleDateFormat().parse(s.format(estBolDate));
						if (currentDate.compareTo(estBolDate) > 0)
							dto.setFracSla("OverDue");
						else
							dto.setFracSla("Scheduled");

					} else
						dto.setFracSla("NA");

					if (!ServicesUtil.isEmpty(fracHitData)) {
						if (!ServicesUtil.isEmpty(fracHitData.getFracHitTime())) {
							dto.setTimeStamp(fracHitData.getFracHitTime());
							dto.setFracHit("HIT");
						} else
							dto.setFracHit("NO");
					}

					if (!ServicesUtil.isEmpty(pack.getMaxCasePressure())
							&& (!ServicesUtil.isEmpty(activeCasePressure))) {

						double k = (double) (pack.getMaxCasePressure() * (50.0f / 100.0f));
						double m = (double) (pack.getMaxCasePressure() * (80.0f / 100.0f));

						if (activeCasePressure >= k && activeCasePressure < m) {
							dto.setColor("amber");
						}
						if (activeCasePressure >= m) {
							dto.setColor("red");
						}
					}

					logger.error("color **************************** +" + dto.getColor());
					if (dto.getColor() == null || !dto.getColor().equals("red")) {

						logger.error("color **************************** inside");
						if ((!ServicesUtil.isEmpty(pack.getMaxTubePressure()))
								&& !ServicesUtil.isEmpty(activeTubePressure)) {

							double k = (double) (pack.getMaxTubePressure() * (50.0f / 100.0f));
							double m = (double) (pack.getMaxTubePressure() * (80.0f / 100.0f));

							if (activeTubePressure >= k && activeTubePressure < m) {
								dto.setColor("amber");
							}
							if (activeTubePressure >= m) {
								dto.setColor("red");
							}
						}
					}

					if (!ServicesUtil.isEmpty(pack.getWellCode())) {

						String muwiId = pack.getWellCode();
						String wellCode = fracPackDao.getWellCode(muwiId);
						dto.setLocationCode(wellCode);

					}

					englist.add(dto);

				}
				response.setFracPacks(englist);
				message.setMessage("Succesfully fetched Engineering View");
				message.setStatus(MurphyConstant.SUCCESS);
				message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}

			else {
				message.setMessage("No Frac Pack with userRole: " + userRole);
				message.setStatus(MurphyConstant.SUCCESS);
				message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Failed to intract with DB: " + e.getMessage());
			logger.error(
					"[Murphy][FracPackFaced][getEngView][error] Exception while Interacting with DB " + e.getMessage());
		}
		response.setResponseMessage(message);
		return response;

	}

	@Override
	public ResponseMessage insertFractHit(FracHitDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		if (!ServicesUtil.isEmpty(dto)) {
			try {
				fracPackDao.insertFractHit(dto);

				responseDto.setStatus(MurphyConstant.SUCCESS);
				responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseDto.setMessage("Frac hit captured successfully");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[Murphy][FracPackFaced][insertFractHit][error] Exception while Interacting with DB "
						+ e.getMessage());
				responseDto.setMessage(MurphyConstant.CREATE_FAILURE);
				responseDto.setStatus(MurphyConstant.FAILURE);
				responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
			}
		} else {
			responseDto.setStatus(MurphyConstant.FAILURE);
			responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
			responseDto.setMessage("Kindly provide Input");
		}
		return responseDto;
	}

	@Override
	public List<CanaryStagingDto> getActiveValues(List<String> muwis) {
		try {
			return canaryStagingDao.getActiveValues(muwis);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Murphy][FracPackFaced][getActiveValues][error] Exception while Interacting with DB "
					+ e.getMessage());
			return null;
		}
	}

	/*
	 * private double getActiveCasePressure(String muiwId,
	 * List<CanaryStagingDto> canaryData) { for (CanaryStagingDto dto :
	 * canaryData) { if (dto.getMuwiId().equals(muiwId) &&
	 * dto.getParameterType().equalsIgnoreCase("PRCASXIN")) return
	 * dto.getDataValue(); } return 0;
	 * 
	 * }
	 */

	/*
	 * private double getActiveCasePressure(String muiwId) { for
	 * (CanaryStagingDto dto : canaryData) { if (dto.getMuwiId().equals(muiwId)
	 * && dto.getParameterType().equalsIgnoreCase("PRCASXIN")) return
	 * dto.getDataValue(); } return 0;
	 * 
	 * }
	 * 
	 * private double getActiveTubePressure(String muiwId,
	 * List<CanaryStagingDto> canaryData) { for (CanaryStagingDto dto :
	 * canaryData) { if (dto.getMuwiId().equals(muiwId) &&
	 * dto.getParameterType().equalsIgnoreCase("PRTUBXIN")) return
	 * dto.getDataValue(); } return 0;
	 * 
	 * }
	 */

	/*
	 * private double getActiveTubePressure(String muiwId,
	 * List<CanaryStagingDto> canaryData) { for (CanaryStagingDto dto :
	 * canaryData) { if (dto.getMuwiId().equals(muiwId) &&
	 * dto.getParameterType().equalsIgnoreCase("PRTUBXIN")) return
	 * dto.getDataValue(); } return 0;
	 * 
	 * }
	 */

	public static void main(String[] args) {

		String color = "";
		double activeCasePressure = 65.00;
		double maxCasePresure = 100.00;
		double maxTubePresure = 200.00;
		double activeTubePressure = 180.00;
		if (!ServicesUtil.isEmpty(maxCasePresure) && (!ServicesUtil.isEmpty(activeCasePressure))) {

			double k = (double) (maxCasePresure * (50.0f / 100.0f));
			double m = (double) (maxCasePresure * (80.0f / 100.0f));

			if (activeCasePressure >= k && activeCasePressure < m) {
				color = "amber";
			}
			if (activeCasePressure >= m) {
				color = "red";
			}
		}

		if (color.isEmpty() || !color.equalsIgnoreCase("red")) {

			logger.error("color **************************** inside");
			if ((!ServicesUtil.isEmpty(maxTubePresure)) && !ServicesUtil.isEmpty(activeTubePressure)) {
				System.out.println("color1: " + color);
				double k = (double) (maxTubePresure * (50.0f / 100.0f));
				double m = (double) (maxTubePresure * (80.0f / 100.0f));
				System.out.println(activeTubePressure + " k= " + k + " and m= " + m);
				if (activeTubePressure >= k && activeTubePressure < m) {
					color = "amber";
					System.out.println("color2: " + color);
				} else if (activeTubePressure >= m) {
					color = "red";
					System.out.println("color3: " + color);
				}
			}
		}
		System.out.println("color: " + color);
	}

	@Override
	public FracHitDto getFracHit(long fracId, String muwiId) {
		try {
			return fracPackDao.getFracHit(fracId, muwiId);
		} catch (Exception e) {
			logger.error(
					"[Murphy][FracPackFaced][getFracHit][error] Exception while Interacting with DB " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ResponseMessage markCompleteFracPack(List<FracHitDto> dtos) {

		ResponseMessage response = new ResponseMessage();

		if (!ServicesUtil.isEmpty(dtos)) {

			try {
				response = fracPackDao.markComplete(dtos);
			} catch (Exception e) {
				response.setMessage("Failed To Complete the frac Pack");
				response.setStatus(MurphyConstant.FAILURE);
				response.setStatusCode(MurphyConstant.CODE_FAILURE);

			}

		} else {
			response.setMessage("Failed To Complete the frac Pack");
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(MurphyConstant.CODE_FAILURE);
		}

		return response;
	}

	@Override
	public ResponseMessage changeWellStatus(List<FracHitDto> dtos) {
		ResponseMessage response = new ResponseMessage();

		if (!ServicesUtil.isEmpty(dtos)) {

			try {
				response = fracPackDao.updateWellStatus(dtos);
			} catch (Exception e) {
				response.setMessage("Failed To Update the Well Status");
				response.setStatus(MurphyConstant.FAILURE);
				response.setStatusCode(MurphyConstant.CODE_FAILURE);

			}

		} else {
			response.setMessage("Failed To Update the Well Status");
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(MurphyConstant.CODE_FAILURE);
		}

		return response;
	}

	// Adding for incident INC0078316
	@Override
	public List<FracScenarioDto> getFracScenario() {
		return fracPackDao.getFracScenario();
	}

	// added as a part of Data Maintenance
	// SOC
	public FracScenarioLookUpResponseDto getActiveFracScenarios() {

		FracScenarioLookUpResponseDto responseDto = new FracScenarioLookUpResponseDto();
		List<FracScenarioLookUpDto> fracScenarioList = new ArrayList<FracScenarioLookUpDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			fracScenarioList = fracScenarioLookUpDao.getActiveFracScenarios(MurphyConstant.ACTIVE);
			if (!ServicesUtil.isEmpty(fracScenarioList)) {
				responseDto.setFracScenarioLookUpdtoList(fracScenarioList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][OffsetFracPackFacade][getActiveFracScenarios][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;
	}

	public FracWellStatusResponseDto getActiveFracWellStatus() {
		FracWellStatusResponseDto responseDto = new FracWellStatusResponseDto();
		List<FracWellStatusDto> fracWellStatusDtoList = new ArrayList<FracWellStatusDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			fracWellStatusDtoList = fracWellStatusDao.getActiveFracWellStatus(MurphyConstant.ACTIVE);
			if (!ServicesUtil.isEmpty(fracWellStatusDtoList)) {
				responseDto.setFracWellStatusDtoList(fracWellStatusDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][OffsetFracPackFacade][getActiveFracWellStatus][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;
	}

	public FracOrientationResponseDto getActiveFracOrientation() {

		FracOrientationResponseDto responseDto = new FracOrientationResponseDto();
		List<FracOrientationDto> fracOrientationDtoList = new ArrayList<FracOrientationDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			fracOrientationDtoList = fracOrientationDao.getActiveFracOrientation(MurphyConstant.ACTIVE);
			if (!ServicesUtil.isEmpty(fracOrientationDtoList)) {
				responseDto.setFracOrientationDtoList(fracOrientationDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][OffsetFracPackFacade][getActiveFracOrientation][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;

	}

	public FracZoneResponseDto getActiveFracZone() {

		FracZoneResponseDto responseDto = new FracZoneResponseDto();
		List<FracZoneDto> fracZoneDtoList = new ArrayList<FracZoneDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			fracZoneDtoList = fracZoneDao.getActiveFracZone(MurphyConstant.ACTIVE);
			if (!ServicesUtil.isEmpty(fracZoneDtoList)) {
				responseDto.setFracZoneDtoList(fracZoneDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][OffsetFracPackFacade][getActiveFracZone][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;

	}

	public FracDropDownResponseDto getAllActiveFracDropDowns() {

		FracDropDownResponseDto responseDto = new FracDropDownResponseDto();
		FracScenarioLookUpResponseDto fracScenarioLookUpResponseDto = getActiveFracScenarios();
		responseDto.setFracScenarioLookUpResponseDto(fracScenarioLookUpResponseDto);

		FracOrientationResponseDto fracOrientationResponseDto = getActiveFracOrientation();
		responseDto.setFracOrientationResponseDto(fracOrientationResponseDto);

		FracWellStatusResponseDto fracWellStatusResponseDto = getActiveFracWellStatus();
		responseDto.setFracWellStatusResponseDto(fracWellStatusResponseDto);

		FracZoneResponseDto fracZoneResponseDto = getActiveFracZone();
		responseDto.setFracZoneResponseDto(fracZoneResponseDto);
		return responseDto;

	}
	// EOC

	@Override
	public ResponseMessage updateFracPack(List<OffsetFracPackDto> dtos){
		ResponseMessage responseMessage = new ResponseMessage();
		try{
			responseMessage = fracPackDao.updateFracPack(dtos);
		}catch (Exception e) {
				logger.error("[Murphy][OffsetFracPackFacade][updateFracPack][error]" + e.getMessage());
		}
		return responseMessage;
	}
	
	
	public String getUserToken() {

		logger.debug("OffsetFracPackFacade.getUserToken()");
		try {
			String username = MurphyConstant.CANARY_USERNAME;
			String password = MurphyConstant.CANARY_PASSWORD;
			;
			String userTokenPayload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\","
					+ "\"timeZone\":\"" + MurphyConstant.CANARY_TIMEZONE + "\",\"application\":\""
					+ MurphyConstant.CANARY_APP + "\"}";
			String url = MurphyConstant.CANARY_API_HOST + "api/v1/getUserToken";
			String httpMethod = MurphyConstant.HTTP_METHOD_POST;
			logger.error("OffsetFracPackFacade.getUserToken() url : " + url + "username : " + username);
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			org.json.JSONObject canaryResponseObject = RestUtil.callRest(url, userTokenPayload, httpMethod, username,
					password);
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("userToken")) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(canaryResponseObject.toString());
				return (String) json.get("userToken");
			} else {
				logger.error("OffsetFracPackFacade.getUserToken()[error]: Canary API Response empty");
			}
		} catch (Exception e) {
			logger.error("OffsetFracPackFacade.getUserToken()[error]" + e.getMessage());
		}
		return null;
	} 
	
	
	
	private String prepFracPayload(List<String> wellCodes) {
		List<String> payloadList = new ArrayList<String>();
		String casingPSI = MurphyConstant.CANARY_PARAM[0];
		String tubingPSI = MurphyConstant.CANARY_PARAM[1];
		for (String wellCode : wellCodes) {

			// Casing
			String casingData = "MUWI_Prod." + wellCode + "." + casingPSI;
			if (!payloadList.contains(casingData)) {
				payloadList.add(casingData);
			}

			// Tubing
			String tubingData = "MUWI_Prod." + wellCode + "." + tubingPSI;
			if (!payloadList.contains(tubingData)) {
				payloadList.add(tubingData);
			}
		}

		String payload = "";
		for (String st : payloadList) {
			payload = payload + "\"" + st + "\",";
		}
		payload = payload.substring(0, payload.length() - 1);
		payload = "[" + payload + "]";
		return payload;
	}
	
	
	
	private List<OffsetFracPackDto> getCanaryFracData(List<OffsetFracPackDto> fracPackDtos) throws Exception {

		logger.error("OffsetFracPackFacade.getCanaryFracData(): start");

		// STEP1:Prepare Frac Payload
		Map<String, OffsetFracPackDto> wellCodeFracMap = new HashMap<>();
		for (OffsetFracPackDto offsetFracPackDto : fracPackDtos) {
			wellCodeFracMap.put(offsetFracPackDto.getWellCode(), offsetFracPackDto);
		}
		String payload = prepFracPayload(new ArrayList<>(wellCodeFracMap.keySet()));

		String aggregateName = MurphyConstant.AGGR_NAME_MAX;
		int insertInterval = 15;
		String insertIntervalType = MurphyConstant.MINUTES;
		int roundOffInterval = 15;
		String roundOffIntervalType = MurphyConstant.MINUTES;
		Date currentDate = roundDateToNearest(new Date(), roundOffInterval, roundOffIntervalType,
				MurphyConstant.CST_ZONE);
		Date startDate = ServicesUtil.getDateWithInterval(currentDate, -insertInterval, insertIntervalType);

		String startTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(startDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);
		String endTimeCanaryFormat = ServicesUtil.convertFromZoneToZoneString(currentDate, null, "",
				MurphyConstant.CST_ZONE, "", MurphyConstant.DATEFORMAT_T);

		logger.error("OffsetFracPackFacade.getCanaryFracData(): payload : " + payload);

		// STEP2:get User token
		String userToken = getUserToken();

		// STEP3:get CANARY data
		JSONObject canaryData = getCanaryData(userToken, payload, startTimeCanaryFormat, endTimeCanaryFormat,
				insertInterval, insertIntervalType, aggregateName);

		// STEP4:
		if (!ServicesUtil.isEmpty(canaryData)) {

			for (Object key : canaryData.keySet()) {
				String keyInString = (String) key;
				int indexOf = keyInString.indexOf('.', 10);
				String muwi = keyInString.substring(10, indexOf);
				JSONArray wellData = (JSONArray) canaryData.get(key);

				OffsetFracPackDto fracPackDto = wellCodeFracMap.get(muwi);

				if (!ServicesUtil.isEmpty(wellData) && !ServicesUtil.isEmpty(fracPackDto)) {

					if (keyInString.contains(MurphyConstant.CANARY_PARAM[0])) {
						for (Object value : wellData) {
							JSONArray wellList = (JSONArray) value;
							Object dataValue = wellList.get(1);
							if (dataValue != null && dataValue != "null") {
								if (dataValue.getClass().getName().equals("java.lang.Double")) {
									fracPackDto.setActiveCasePressure((Double) dataValue);
								} else if (dataValue.getClass().getName().equals("java.lang.Long")) {
									fracPackDto.setActiveCasePressure(((Long) dataValue).doubleValue());
								} else if (dataValue.getClass().getName().equals("java.lang.Integer")) {
									fracPackDto.setActiveCasePressure(((Integer) dataValue).doubleValue());
								}
							}
						}

					} else if (keyInString.contains(MurphyConstant.CANARY_PARAM[1])) {
						for (Object value : wellData) {
							JSONArray wellList = (JSONArray) value;
							Object dataValue = wellList.get(1);
							if (dataValue != null && dataValue != "null") {
								if (dataValue.getClass().getName().equals("java.lang.Double")) {
									fracPackDto.setActiveTubePressure((Double) dataValue);
								} else if (dataValue.getClass().getName().equals("java.lang.Long")) {
									fracPackDto.setActiveTubePressure(((Long) dataValue).doubleValue());
								} else if (dataValue.getClass().getName().equals("java.lang.Integer")) {
									fracPackDto.setActiveTubePressure(((Integer) dataValue).doubleValue());
								}
							}
						}
					}

				}
			}
		}

		return fracPackDtos;
	}


Date roundDateToNearest(Date currentDate, int interval, String intervalType, String zoneId) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeZone(TimeZone.getTimeZone(zoneId));
	calendar.setTime(currentDate);
	int value = 0;
	if (MurphyConstant.DAYS.equals(intervalType)) {
		value = calendar.get(Calendar.DATE);
		value -= value % interval;
		calendar.set(Calendar.DATE, value);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	} else if (MurphyConstant.HOURS.equals(intervalType)) {
		value = calendar.get(Calendar.HOUR);
		value -= value % interval;
		calendar.set(Calendar.HOUR, value);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	} else if (MurphyConstant.MINUTES.equals(intervalType)) {
		value = calendar.get(Calendar.MINUTE);
		value -= value % interval;
		calendar.set(Calendar.MINUTE, value);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	} else if (MurphyConstant.SECONDS.equals(intervalType)) {
		value = calendar.get(Calendar.SECOND);
		value -= value % interval;
		calendar.set(Calendar.SECOND, value);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	return calendar.getTime();
}


	private JSONObject getCanaryData(String userToken, String payload, String startTime, String endTime,
			int insertInterval, String insertIntervalType, String aggregateName) throws Exception {

		logger.debug("OffsetFracPackFacade.getCanaryData(userToken=" + userToken + ", payload=" + payload
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", insertInterval=" + insertInterval
				+ ", insertIntervalType=" + insertIntervalType + ", aggregateName=" + aggregateName + ")");
		try {
			String canaryUrl = "api/v1/getTagData";

			String stringInterval = "00";
			if (insertInterval < 10) {
				stringInterval = "0" + insertInterval;
			} else {
				stringInterval = "" + insertInterval;
			}

			String aggregateInterval = "";
			if (MurphyConstant.DAYS.equals(insertIntervalType)) {
				aggregateInterval = stringInterval + ":00:00:00";
			} else if (MurphyConstant.HOURS.equals(insertIntervalType)) {
				aggregateInterval = "0:" + stringInterval + ":00:00";
			} else if (MurphyConstant.MINUTES.equals(insertIntervalType)) {
				aggregateInterval = "0:00:" + stringInterval + ":00";
			} else if (MurphyConstant.SECONDS.equals(insertIntervalType)) {
				aggregateInterval = "0:00:00:" + stringInterval + "";
			}

			String canaryPayload = "{" + "\"userToken\": \"" + userToken + "\"," + "\"startTime\": \"" + startTime
					+ ":00\"," + "\"endTime\": \"" + endTime + ":00\"," + "\"aggregateName\": \"" + aggregateName
					+ "\"," + "\"aggregateInterval\": \"" + aggregateInterval + "\"," + "\"includeQuality\": false,"
					+ " \"MaxSize\": 4000000," + "\"continuation\": null," + "\"tags\": " + payload + "" + "}";

			logger.error("OffsetFracPackFacade.getCanaryData()[canaryPayload]" + canaryPayload);

			String userName = MurphyConstant.CANARY_USERNAME;
			String password = MurphyConstant.CANARY_PASSWORD;
			com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
			org.json.JSONObject canaryResponseObject = RestUtil.callRest(MurphyConstant.CANARY_API_HOST + canaryUrl,
					canaryPayload, MurphyConstant.HTTP_METHOD_POST, userName, password);
			if (!ServicesUtil.isEmpty(canaryResponseObject) && canaryResponseObject.toString().contains("data")) {
				String canaryResponse = canaryResponseObject.toString();
				logger.debug("OffsetFracPackFacade.getCanaryData()([canaryResponse]" + canaryResponse.toString());
				JSONParser parser = new JSONParser();
				JSONObject canaryJson = (JSONObject) parser.parse(canaryResponse.toString());
				return (JSONObject) canaryJson.get("data");
			}
		} catch (Exception e) {
			logger.error("OffsetFracPackFacade.getCanaryData()[error]" + e.getMessage());
			throw e;
		}
		return null;
	}

}
