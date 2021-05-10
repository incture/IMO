/*global Plotly*/
sap.ui.define([
	"com/sap/incture/Incture_IOP/controller/BaseController",
	"sap/ui/model/json/JSONModel",
	"com/sap/incture/Incture_IOP/util/formatter",
	"sap/m/MessageBox",
	"sap/m/MessageStrip",
	"com/sap/incture/Incture_IOP/util/powerBiControls",
	"sap/m/BusyDialog",
	"sap/ui/export/Spreadsheet",
	"com/sap/incture/Incture_IOP/helper/TaskManagementHelper", //AN: #ScratchFilter
	"com/sap/incture/Incture_IOP/helper/LocationHistoryHelper",
	"com/sap/incture/Incture_IOP/helper/ChangeSeatHelper", //AN: #ChangeSeat
	"com/sap/incture/Incture_IOP/helper/NotificationsHelper", //AN: #Notif
	"com/sap/incture/Incture_IOP/helper/WorkbenchHelper"
], function (BaseController, JsonModel, Formatter, MessageBox, MessageStrip, PowerBiControls, BusyDialog, Spreadsheet,
	TaskManagementHelper, LocationHistoryHelper, ChangeSeatHelper, NotificationsHelper, WorkbenchHelper) {
	"use strict";
	return BaseController.extend("com.sap.incture.Incture_IOP.controller.DashBoardView", {
		formatter: Formatter,
		powerBiControls: PowerBiControls,
		onInit: function () {
			//Controller Json Model ,All local JSON to be created can be stored as properties inside this single model modelName : dashBoardModel
			this.setModel(new JsonModel(), "dashBoardModel");
			this.getModel("dashBoardModel").setSizeLimit(5000);
			this.setModel(new JsonModel(), "oReportDetailModel");
			this.getModel("oReportDetailModel").setSizeLimit(2000);
			this.setModel(new JsonModel(), "oTaskPanelDetailModel");
			this.setModel(new JsonModel(), "oAdditionalTaskModel");
			this.setModel(new JsonModel(), "oAllWellModel");
			this.oBusyInd = new BusyDialog();
			this.oBusyInd.setBusyIndicatorDelay(0);
			var oComponent = this.getOwnerComponent();
			this._router = oComponent.getRouter();
			this._router.getRoute("dashboardView").attachPatternMatched(this._handleRouteMatched, this);

		},

		_handleRouteMatched: function (oEvent) {
			//reading the Arguments from url testing git
			var oArgs = oEvent.getParameter("arguments");
			this.getModel("dashBoardModel").setProperty("/arguments", oArgs);
			ChangeSeatHelper._initializeLocalModelForChangeSeat(this, this.getModel("dashBoardModel")); //AN: #ChangeSeat
			ChangeSeatHelper.setAppStorageInfo(this, this.getModel("dashBoardModel")); //AN: #ChangeSeat
			this.getUserDetails();
			this._initializeIcons();
			this._setInitialDataForModel(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForCreateTaskScheduling(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForSuggestions(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForHirarchy(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForDownTime(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForFrac(this.getModel("dashBoardModel"));
			this._initializeLocalModelForDop(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForPOT(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForTaskScheduling(this.getModel("dashBoardModel"));
			this._initilizeLocalModelForOBXTaskScheduling(this.getModel("dashBoardModel"));
			this._inilizeBusyIndicators();
			this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
			this._bindLocationSuggestionforNonDispatch("Field");
			this._setTableUpdateFinishedListeners(); //AN: #obxEngine
			this._setScreenHeights(); //Scrolling issue in invesgationHistory
			sap.ui.Device.resize.attachHandler(function () {
				this._setScreenHeights();
				this._setTaskPanelHeights(); //AN: #scrollHeight
				this._setModuleWiseScrollHeights(); //AN: #obxEngine
				this._setTaskListHeights(); //SK: #restriction on dispatch fragment height.
				this._setNotificationPanelHeights(); //RV :#taskpanel Size set
				this._setEnergyIsolationFromHeight(); //sk: #Energy isolation form height
				this._setPTWFromHeight(); // Sk: height of the PTW forms fragment
				this._setBypassLogFromHeight(); // RV: height of Bypasslog form fragment
			}.bind(this));
			this._fileReaderFn();
			this._initClipBoard();
			TaskManagementHelper._initializeLocalModelForTaskManagementPanel(this, this.getModel("dashBoardModel")); //AN: #ScratchFilter
			this._initializeLocalModelForLeftNavPanel(this.getModel("dashBoardModel"));
			this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", false);
			this.onToolPageSidePanelExpand();
			this.getModel("dashBoardModel").setProperty("/panelProperties/isLock", false);
			this.getModel("dashBoardModel").setProperty("/showNotificationFragment", false); //#RV websocket Changes
			this.OnLockPress();
			// #RV changes for loaction history
			LocationHistoryHelper._initializeLocalModelForLocationHistoryPanel(this.getModel("dashBoardModel"));
			NotificationsHelper._initializeLocalModelForNotifications(this, this.getModel("dashBoardModel")); //AN: #Notif
			WorkbenchHelper._initilizeLocalModelForWorkbench(this, this.getModel("dashBoardModel")); //Anand-WB //AN: #msgToROC
			this.getModel("dashBoardModel").setProperty("/fracMonitoringData/isEditing", false); // Sk:Frac Update
		},
		_setInitialDataForModel: function (oModel) {
			this.getModel("dashBoardModel").setProperty("/futureTask", false);
			this.getModel("dashBoardModel").setProperty("/isUserLoggedOut", false);
			this.getModel("dashBoardModel").setProperty("/newDispatchDateValue", "");
			oModel.setProperty("/showProActiveSwitch", true); // #RV Proactive Well Candidate Fix for Visibility
			oModel.setProperty("/isSelectedHierarchy", false);
			oModel.setProperty("/isReturnedTask", false);
			oModel.setProperty("/prograssBarVisible", false);
			oModel.setProperty("/ptwSelectedKey", "Active JSA");
			oModel.setProperty("/panelProperties", {
				currentSelectKey: "pot"
			});
			oModel.setProperty("/spotFireData", {
				durationKey: "7",
				unitVisible: false // SK: Prove Unit Switch button
			});
			oModel.setProperty("/userData", {
				isROC: true,
				isPOT: false,
				isENG: false
			});
			oModel.setProperty("/dopData", {
				durationKey: "1",
				rolledUp: true
			});
			oModel.setProperty("/pwHopperData", []);
			oModel.setProperty("/pwHopperSortData", {
				statuses: [{
					key: "0",
					text: "Active"
				}, {
					key: "1",
					text: "Inprogress"
				}, {
					key: "2",
					text: "Completed"
				}, {
					key: "3",
					text: "Inactive"
				}, {
					key: "4",
					text: ""
				}],
				filters: [],
				selectedPotStatus: "",
				selectedPotStatusValue: "",
				selectedReStatus: "",
				selectedReStatusValue: "",
				selectedWwStatus: "",
				selectedWwStatusValue: "",
				selectedAlsStatus: "",
				selectedAlsStatusValue: ""

			});
			oModel.setProperty("/nonDispatchItem", {
				downtimePanelExpanded: false,
				nonDispatchItemDesc: "",
				nonDispatchItemLoc: "",
				nonDispatchItemDecValueState: "None",
				nonDispatchItemLocValueState: "None",
				nonDispatchItemDecValueStateText: "Description is Mandatroy",
				nonDispatchItemLocValueStateText: "Location is Invalid",
				isEdit: false,
				taskId: "",
				objectContext: {},
				locTypeKey: "Well",
				descTypeKey: "Pressure optimization (ex. tubing and sep)",
				locationTypes: [{
					key: "Field",
					value: "Field"
				}, {
					key: "Facility",
					value: "Facility"
				}, {
					key: "Well Pad",
					value: "Well Pad"
				}, {
					key: "Well",
					value: "Well"
				}]
			});
			var legendItems = [];
			var tempObj, sNo;
			for (var i = 1; i <= 20; i++) {
				sNo = i;
				if (i < 10) {
					sNo = "0" + i;
				}
				tempObj = {
					text: String.fromCharCode(65 + i - 1),
					type: "Type" + sNo
				};
				legendItems.push(tempObj);
			}
			oModel.setProperty("/legendAppointmentItems", legendItems);
			var sUrl = "/taskmanagementRest/nonDispatch/getTaskTypes";
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					oModel.setProperty("/nonDispatchItem/descTypes", oData.valueDtos);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
			oModel.setProperty("/selectedPage", 1);
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idLeftButton").setVisible(true);
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idRightButton").setVisible(true);
			this.getModel("dashBoardModel").setProperty("/lowerLimitPageValue", 1);
			this.getModel("dashBoardModel").setProperty("/upperLimitPageValue", 100);
			this.getModel("dashBoardModel").setProperty("/pageFactor", 100);
			this.getModel("dashBoardModel").setProperty("/dynamicContentType", { //AN: #inquire
				userType: "",
				taskType: "",
				from: null
			});
			this.getModel("dashBoardModel").setProperty("/collabTaskType", "Task Collaboration");
			this.getModel("dashBoardModel").setProperty("/isTaskCardPress", false);
			this.getModel("dashBoardModel").setProperty("/isIconPress", false);
			this.getModel("dashBoardModel").setProperty("/isTaskPanelOpen", false); //AN: #scrollHeight
			this.getModel("dashBoardModel").setProperty("/taskPanelButtonsEnablement", {
				dispatchCreateTaskEnabled: true,
				dispatchCreateTaskPREnabled: true,
				dispatchCloseTaskEnabled: true,
				dispatchClosePigTaskEnabled: true,
				dispatchUpdateTaskEnabled: true,
				dispatchIssuePLEnabled: true,
				dispatchIssuePREnabled: true,
			}); //AN: #obxUAT
			this.getModel("dashBoardModel").setProperty("/isCreateTaskFromRightSidePanel", false);
			this.getModel("dashBoardModel").setProperty("/alarmDetailKey", "All"); //AN: IOP Alarm Screen Issue: multiple select + acknowledge
			this.getModel("dashBoardModel").setProperty("/spotFireData/pageNumber", "1");
			this.getModel("dashBoardModel").setProperty("/sSystemTimeZone", "( " + this.getSystemTimeZone() + " )"); //AN: #TimeZone 
			this.getModel("dashBoardModel").setProperty("/sSelectedTimeZone", "SYSTEM"); //AN: #TimeZone 
		},
		_initializeLocalModelForLeftNavPanel: function (oModel) {
			oModel.setProperty("/panelProperties", {
				"showTaskManagementPanelIcon": false
			});
		},
		onToolPageSidePanelExpand: function (oEvent) {
			var toolPage = this.byId("toolPage");
			var oModel = this.getModel("dashBoardModel");
			var oSidePanelMain = this.byId("sideNavigationMain");
			var oAppNavList = this.byId("appsNavList");
			var oAppNavList1 = this.byId("appsNavList1");
			toolPage.setSideExpanded(!toolPage.getSideExpanded());
			oModel.setProperty("/panelProperties/showTaskManagementPanelIcon", !(oModel.getProperty(
				"/panelProperties/showTaskManagementPanelIcon")));
			if (toolPage.getSideExpanded()) {
				oSidePanelMain.addStyleClass("iopV2SidePanelBgColor");
				oAppNavList.addStyleClass(
					"	iopV2SidePanelActive iopV2SidePanelTextIconColor iopV2SidePanelSelected iopV2SidePanelBgColor iopV2SidePanelNoBorder");
				oAppNavList1.addStyleClass(
					"	iopV2SidePanelActive iopV2SidePanelTextIconColor iopV2SidePanelSelected iopV2SidePanelBgColor iopV2SidePanelNoBorder");
			} else {
				oSidePanelMain.removeStyleClass("iopV2SidePanelBgColor");
				oAppNavList.removeStyleClass(
					"	iopV2SidePanelActive iopV2SidePanelTextIconColor iopV2SidePanelSelected iopV2SidePanelBgColor iopV2SidePanelNoBorder");
				oAppNavList1.removeStyleClass(
					"iopV2SidePanelActive iopV2SidePanelTextIconColor iopV2SidePanelSelected iopV2SidePanelBgColor iopV2SidePanelNoBorder");
			}
		},
		onSideNavigationItemSelect: function (oEvent) {
			var oUserData = this.getModel("dashBoardModel").getProperty("/userData");
			var oItemText;
			var oWebSocketPropertyData = this.getModel("dashBoardModel").getProperty("/webSocketProperty/data"); //AN: #Notif
			if (typeof oEvent === "string" && oEvent !== "external") {
				oItemText = oEvent;
			} else if (oEvent === "external") {
				oItemText = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			} else oItemText = oEvent.getParameter("item").getKey();
			var toolPageNavContainer = this.byId("pageContainer");
			var oModel = this.getModel("dashBoardModel");
			if (oItemText !== "Lock") {
				oModel.setProperty("/panelProperties/currentSelectKey", oItemText);
			}
			//#RV WebSocket Changes
			if (oItemText !== "fracMonitoring") {
				this.getView().getModel("dashBoardModel").setProperty("/highlightFrac", []);
			}
			if (oItemText !== "alarms") { //AN: #Notif
				this.getView().getModel("dashBoardModel").setProperty("/highlightAlarms", []);
			}
			if (oItemText !== "pwhopper") { //AN: #Notif
				this.getView().getModel("dashBoardModel").setProperty("/highlightPWHopper", []);
			}
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "downtime") { //SH: Downtime Country Code
				this._getParentCodesforDownTime();
			}
			switch (oItemText) {
			case "tasks":
				this.onCollapseExpandPress();
				clearTimeout(this.DOPRefresh);
				break;
			case "dashboard":
				toolPageNavContainer.to(this.createId("root"));
				clearTimeout(this.DOPRefresh);
				break;
			case "alarms":
				toolPageNavContainer.to(this.createId("alarmsPanel"));
				if (oWebSocketPropertyData && oWebSocketPropertyData.alarmNotificationDto && oWebSocketPropertyData.alarmNotificationDto.alarmCount >
					0) { //AN: #Notif
					this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "Alarms"); //AN: #highlightShift
					// if (oEvent !== "external") { //AN: #highlightShift //AN: #BubbleNotifDemo
					// 	this.acknowledgeWebSocketRequest("Alarms");
					// }
				}
				if (oEvent === "external") {
					if (this.getModel("dashBoardModel").getProperty("/prograssBarVisible") === true) {
						this.getModel("dashBoardModel").setProperty("/prograssBarVisible", false);
						this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
						if (this.tick)
							clearTimeout(this.tick);
						this.tick = false;
					}
				}
				this.getModel("dashBoardModel").setProperty("/selectedPage", 1);
				this.getAlarmData();
				this.onAlarmSelectionClear();
				clearTimeout(this.DOPRefresh);
				break;
			case "ndtpv":
				toolPageNavContainer.to(this.createId("ndtpvPanel"));
				this.getModel("dashBoardModel").setProperty("/spotFireData/unit", "boed"); // SK: Prove Unit Switch button
				this.getModel("dashBoardModel").setProperty("/spotFireData/unitVisible", false); // SK: Prove Unit Switch button
				this.getProveReport();
				clearTimeout(this.DOPRefresh);
				this.getModel("dashBoardModel").setProperty("/spotFireData/paginationVisible", false);
				break;
			case "downtime":
				toolPageNavContainer.to(this.createId("downtimePanel"));
				if (oEvent !== 'external') {
					this.getModel("dashBoardModel").setProperty("/downtimeCaptureTabKey", "Well");
					var currentTab = this.getModel("dashBoardModel").getProperty("/downtimeSubTabKey");
					this.getModel("dashBoardModel").setProperty("/downtimePanelExpanded", true);
					this.onDowntimeTableIconTabbarSelect(currentTab);
				} else {
					this.downtimeCaptureTabSelect();
				}
				clearTimeout(this.DOPRefresh);
				break;
			case "permit":
				toolPageNavContainer.to(this.createId("ptwPanel"));
				var currentSelectedTab = this.getModel("dashBoardModel").getProperty("/ptwSelectedKey");
				this.getPermittoWorkList(currentSelectedTab);
				clearTimeout(this.DOPRefresh);
				break;
			case "analytics":
				toolPageNavContainer.to(this.createId("analyticsPanel"));
				if (oEvent !== "external") {
					this.byId("hierarchyWrapPanell").setExpanded(false); //MS Collapse Panel
					this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", true);
					this.onPotDataLoad();
				}
				clearTimeout(this.DOPRefresh);
				break;
			case "scheduler":
				toolPageNavContainer.to(this.createId("taskSchedulerPanel"));
				if (oEvent !== "external") {
					this.byId("hierarchyWrapPanell").setExpanded(false); //MS Collapse Panel
					this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", true);
					if (this.getModel("dashBoardModel").getProperty("/taskV2templateAvaliable") === true) {
						this.onTaskSchedulingV2TabSelect(true);
					} else {
						this.onTaskSchedulingV2TabSelect(false);

					}
					break;
				}
				clearTimeout(this.DOPRefresh);
				break;
			case "DOP":
				toolPageNavContainer.to(this.createId("dopPanel"));
				var selectedKey = this.getModel("dashBoardModel").getProperty("/dopData/selectedKey"); /*AN: #DOP-DGP*/
				if (this.oAjax) { // SK : CHG0038615 - DOP/DGP data not in sync with the location selected in Location Hierarchy 
					this.oAjax.abort();
				}
				this.onDopLoad();

				this.DOPRefresh = setInterval(function () {
					/* DOP Refresh on every 15 min*/
					/* DOP Refresh on every hour 5th or 6th min*/
					if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP") {
						var oDate = new Date();
						var iMin = oDate.getMinutes();
						if (iMin > 4 && iMin < 7) {
							this.onDopLoad();
						}
						//this.getUserDetailsFromDOPRefresh();
					}
				}.bind(this), 60000 * 2);
				//toolPageNavContainer.to(this.createId("dopPanel"));
				//this.onDopLoad();
				break;
			case "pwhopper":
				toolPageNavContainer.to(this.createId("pwhopperPanel"));
				if (oWebSocketPropertyData && oWebSocketPropertyData.pwHopperNotificationDto && oWebSocketPropertyData.pwHopperNotificationDto.hopperCount >
					0) { //AN: #Notif
					this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "PwHopper"); //AN: #highlightShift
					// if (oEvent !== "external") { //AN: #highlightShift //AN: #BubbleNotifDemo
					// 	this.acknowledgeWebSocketRequest("PwHopper");
					// }
				}
				oModel.setProperty("/busyIndicators/pwHopperTblBusy", true); //AN: #loaderWhileNotif
				this.onPWHopperLoad();
				clearTimeout(this.DOPRefresh);
				break;
			case "Lock":
				if (oEvent === 'external') {
					break;
				} else {
					this.OnLockPress("true");
					break;
				}
			case "fracMonitoring":
				//#RV WebSocket Changes
				if (oEvent !== "external" && oWebSocketPropertyData && oWebSocketPropertyData.fracNotificationDto && oWebSocketPropertyData.fracNotificationDto
					.fracCount > 0) {
					this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "Frac");
					this.byId("hierarchyWrapPanell").setExpanded(false); //MS Collapse Panel
					this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", true);
					// this.acknowledgeWebSocketRequest("Frac"); //AN: #Notif //AN: #BubbleNotifDemo
				}
				this.getAllWellsForFrac();
				this.getFracListDetails();
				toolPageNavContainer.to(this.createId("fracMonitoringPanel"));
				clearTimeout(this.DOPRefresh);
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
				break;
			case "obx":
				if (oEvent !== "external") {
					this.byId("hierarchyWrapPanell").setExpanded(false); //MS Collapse Panel
					this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", true);
					var selectedKey = this.getModel("dashBoardModel").getProperty("/OBXSchedulingData/selectedKey"); //AN: #obxEngine
					if (selectedKey === "obxScheduling") { //AN: #obxEngine
						this.getOBXUserListData();
					} else if (selectedKey === "obxEngine") {
						var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
						if (oOBXEngineData) {
							oOBXEngineData.obxAllocation = [];
							oOBXEngineData.proOperatorList = [];
							oOBXEngineData.obxOperatorList = [];
							oOBXEngineData.obxWorkLoadList = [];
						}
						this.getModel("dashBoardModel").setProperty("/ObxEngine", 0);
						this.oBusyInd.open();
						this.getOBXConfig();
						this.bDescending = false;
						var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
						var oDay = new Date().getDay();
						var oDayArr = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
						oOBXEngineData.selOBXDay = oDayArr[oDay];
						var oRoleGrp = this.getModel("dashBoardModel").getData().userData.resGroupRead.split(",")[0];
						var oFieldLoc = oRoleGrp.split("_")[3];
						if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
								"/isOBXReadOnly")) {
							oRoleGrp = "IOP_TM_ROC_Catarina";
							oFieldLoc = "Catarina";
						}
						oOBXEngineData.selOBXField = oFieldLoc;
						if (oFieldLoc.indexOf("Tilden") >= 0) {
							oFieldLoc = "Tilden";
						}
						this.onOBXFieldDaySelectChange();
						this.getModel("dashBoardModel").refresh(true);
					}
				}
				toolPageNavContainer.to(this.createId("obxPanel"));
				clearTimeout(this.DOPRefresh);
				break;
			case "locHistory": //AN: #refactor
				if (oEvent === "external") { //SK: Prove to location history issue fix
					if (this.getView().byId("appsNavList").getSelectedItem().getText() !== "Location History") {
						this.setSelectedModule();
					}
				}
				// RV: Location History Changes
				this.byId("hierarchyWrapPanell").setExpanded(true); //MS Collapse Panel
				this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", false);
				toolPageNavContainer.to(this.createId("locHistoryPanel"));
				this.getModel("dashBoardModel").setProperty("/locationHistoryData", {
					locHistoryTaskData: [],
					locHistoryDowntimeData: [],
					locHistoryWorkorderData: [],
					workOrderDetailsData: [],
					locHistoryPTWJSAData: [],
					locHistoryTaskStatusList: [],
					locHistoryDowntimeStatusList: [],
					locHistoryWorkorderStatusData: [],
					locHistoryPTWStatusData: [],
					locHistoryByPasslogStatusList: [],
					locHistoryEnergyIsolationStatusData: [],
					PTWDetailsData: [],
					downtimeDetailsData: [],
					taskDetailsData: [],
					bypassLogDetailsData: [],
					bypassLogData: [],
					energyIsolationData: [],
					locationHistoryTaskCount: 0,
					locHistoryPaginationVisibility: false,
					pageSize: 100,
					busyIndicator: false,
					bDescending: true
				});
				var oSelectedLocIconTabKey = this.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.iconFilterTab;;
				switch (oSelectedLocIconTabKey) {
				case "Task":
					LocationHistoryHelper.locHistoryIconTabSelect(this, "Task", this.getModel("dashBoardModel"));
					break;
				case "Downtime":
					LocationHistoryHelper.locHistoryIconTabSelect(this, "Downtime", this.getModel("dashBoardModel"));
					break;
				case "Workorder":
					LocationHistoryHelper.locHistoryIconTabSelect(this, "Workorder", this.getModel("dashBoardModel"));
					break;
				case "Permit To Work":
					LocationHistoryHelper.locHistoryIconTabSelect(this, "Permit To Work", this.getModel("dashBoardModel"));
					break;
				case "Bypass Log":
					LocationHistoryHelper.locHistoryIconTabSelect(this, "Bypass Log", this.getModel("dashBoardModel"));
					break;
				case "Energy Isolation":
					LocationHistoryHelper.locHistoryIconTabSelect(this, "Energy Isolation", this.getModel("dashBoardModel"));
					break;

				}
				break;
			case "workbench":
				toolPageNavContainer.to(this.createId("workbenchPanel"));
				var oInsightToActionData = this.getModel("dashBoardModel").getProperty("/InsightToActionData");
				if (!oUserData.isROC && oUserData.isPOT) { //AN: #msgToROC

					/*AN: Start of #prod-msgToROC 17-11-2020*/
					// this.getView().getModel("dashBoardModel").setProperty("/InsightToActionData/selectedKey", "wbMessage");
					/*AN: End of #prod-msgToROC 17-11-2020*/
				}
				if (oInsightToActionData.selectedKey === "wbMessage") { //AN: #msgToROC
					if (oEvent !== "external") {
						WorkbenchHelper.fnGetMsgDraftData(this, this.getModel("dashBoardModel"));
					}
				} else {
					WorkbenchHelper.fetchWorkbenchList(this, this.getModel("dashBoardModel"));
				}
				this.WbRefresh = setInterval(function () {
					/* Msg Refresh on every 5 min*/
					if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "workbench" && oInsightToActionData.selectedKey ===
						"wbMessage") {
						WorkbenchHelper.fnGetMsgDraftData(this, this.getModel("dashBoardModel"));
					}
				}.bind(this), 60000);
				break;
			}
			this.addToken();
		},
		onAlarmSegmentBtnClick: function (oEvent) {
			//This Function is getting called for Both NDTPV And Alarms
			var oItemText = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			this.onSideNavigationItemSelect(oItemText);

		},
		onCollapseExpandPress: function () {
			var oProp = this.getModel("dashBoardModel").getProperty("/taskManagementPanel/showTaskManagementPanel");
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showTaskManagementPanel", !oProp);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showBypassPanel", false); //Anand: #handover
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showOpAvailPanel", false); //AN: #OpAvail
		},
		OnLockPress: function (oEvent) {
			if (oEvent === "true") {
				this.onToolPageSidePanelExpand();
			}
			var oSidePanelMain = this.byId("sideNavigationMain");
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/isLock") === false) {
				oSidePanelMain.attachBrowserEvent("mouseenter", function () {
					if (this.getModel("dashBoardModel").getProperty("/panelProperties/showTaskManagementPanelIcon") === true && this.getModel(
							"dashBoardModel").getProperty("/panelProperties/isLock") === true) {
						this.onToolPageSidePanelExpand();
					}
				}.bind(this));
				oSidePanelMain.attachBrowserEvent("mouseleave", function () {
					if (this.getModel("dashBoardModel").getProperty("/panelProperties/showTaskManagementPanelIcon") === false && this.getModel(
							"dashBoardModel").getProperty("/panelProperties/isLock") === true) {
						this.onToolPageSidePanelExpand();
					}

				}.bind(this));
				this.getModel("dashBoardModel").setProperty("/panelProperties/isLock", true);
			} else {
				oSidePanelMain.detachBrowserEvent("mouseenter", function () {});
				oSidePanelMain.detachBrowserEvent("mouseleave", function () {});
				this.getModel("dashBoardModel").setProperty("/panelProperties/isLock", false);
			}
		},
		handleHierarchyPaneOpen: function (oEvent) {
			var ohirarchyWrapPanel = this.byId("hierarchyWrapPanell");
			ohirarchyWrapPanel.setExpanded(!ohirarchyWrapPanel.getExpanded());
			this.getModel("dashBoardModel").setProperty("/HierarchyPanelOpen", !ohirarchyWrapPanel.getExpanded());
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) { //AN: #obxEngineV2
				this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
			}
		},
		// RV: #function to get All Scenario for creating frac Pack
		getAllScenarioForFrac: function () {
			var sUrl = "/taskmanagementRest/fracPack/getFracScenario";
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (oData) {
						this.getModel("dashBoardModel").setProperty("/fracMonitoringData/scenarioDto", oData);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		_initilizeLocalModelForFrac: function (oModel) {
			var sUrl = "/taskmanagementRest/fracPack/getAllActiveFracDropDowns";
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (oData) {
						var oFracData = {
							fracData: [],
							selectedWells: null,
							deleteEnabled: false,
							markCompleteEnabled: false,
							scenarioDto: oData.fracScenarioLookUpResponseDto.fracScenarioLookUpdtoList,
							orientationDto: oData.fracOrientationResponseDto.fracOrientationDtoList,
							zoneDto: oData.fracZoneResponseDto.fracZoneDtoList,
							wellStatusDto: oData.fracWellStatusResponseDto.fracWellStatusDtoList,
							fracDescription: null,
							fracHitDate: null
						};
						oModel.setProperty("/fracMonitoringData", oFracData);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
			//	this.getAllScenarioForFrac(); //Enchancement Changes by RV
		},
		getAllWellsForFrac: function () {
			var sUrl = "/taskmanagementRest/location/getLocation";
			var oInitialDataPayload = {
				"locationType": "SEARCH",
				"navigate": "",
				"location": "*"
			};
			this.doAjax(sUrl, "POST", oInitialDataPayload, function (oData) {
					if (oData.message.statusCode === "0") {
						this.getModel("oAllWellModel").setSizeLimit(oData.dto.locationHierarchy.length);
						var aEFSWell = oData.dto.locationHierarchy.filter(well => well.location.startsWith("MUR-US-")); //SK:Create Frac - To Show only EFS well 
						this.getModel("oAllWellModel").setProperty("/allWellData", aEFSWell);
					} else {

						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		getFracListDetails: function () {
			var oRoleData = this.getModel("dashBoardModel").getProperty("/userData/sGroup");
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isFracReadOnly")) {
				var sUrl = "/taskmanagementRest/fracPack/EngView?userRole=IOP_POT_East,IOP_POT_West";
			} else {
				var sUrl = "/taskmanagementRest/fracPack/EngView?userRole=" + oRoleData;
			}
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (oData.responseMessage && oData.responseMessage.statusCode === "0") {
						var oAckData = this.getView().getModel("dashBoardModel").getData().highlightFrac;
						try { //AN: #highlightShift
							if (oAckData) {
								var oScroll = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "idfracScroll");
								var oTable = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "fracMonitoringTableView");
								for (var i = 0; i < oAckData.length; i++) {
									for (var j = 0; j < oData.fracPacks.length; j++) {
										if (oAckData[i] == oData.fracPacks[j].fracId) {
											oData.fracPacks[j].fracNotification = "notified";
											var sItem = oTable.getItems()[j];
											oScroll.scrollToElement(sItem);
											break;
										}
									}
								}
							}
						} catch (e) {

						}
						this.getModel("dashBoardModel").setProperty("/fracMonitoringData/fracData", oData.fracPacks);
					} else {
						if (oData.message) {
							this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						}
					}
				}.bind(this),
				function (oError) {
					if (oError.getId() === "requestFailed" && oError.getParameter("message") === "parsererror" &&
						oError.getParameter("statusText") === "parsererror" && oError.getParameter("statusCode") === 200) { //AN: #parseError
						this._createConfirmationMessage("Error", "The user session has timed out. Please refresh the page", "Error", "", "Close", false,
							null);
						//html error due to session timeout - msg display
					} else {
						this._createConfirmationMessage("Error", oError.getParameter("message"), "Error", "", "Close", false, null);
					}
				}.bind(this));
		},
		onFracHitRightPress: function (oEvent) {
			var oContext = oEvent.getParameters();
			var isRoc = this.getModel("dashBoardModel").getProperty("/userData/isROC");
			var isPot = this.getModel("dashBoardModel").getProperty("/userData/isPOT");
			var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			if ((isRoc || isPot || isReadOnly) && oContext.src.getBindingContext("dashBoardModel")) {
				var oFragmentId = "idFracActionList",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.FracHitActionList";
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/currentRightClickContext", oContext.src.getBindingContext(
					"dashBoardModel").getObject());
				if (!this.fracActionList) {
					this.fracActionList = this._createFragment(oFragmentId, oFragmentName);
					this.getView().addDependent(this.fracActionList);
				}
				this.fracActionList.setOffsetX(-30);
				this.fracActionList.openBy(oContext.target);
			}
		},
		onPressFracDispatch: function () {
			var oCurrContext = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/currentRightClickContext");
			var oObj = {};
			oObj.muwi = oCurrContext.wellCode;
			oObj.locationType = "Well";
			oObj.locationCode = oCurrContext.locationCode;
			oObj.location = oCurrContext.wellName; //AN: #ChangeSeat
			this.getModel("dashBoardModel").setProperty("/currAlarmObject", oObj);
			this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
			this.onDispatchPress();
		},
		onPressFracHit: function () {
			this.onTrendsPress();
		},
		onFracHitPress: function () {
			var _bvalid = true;
			var oCurrContext = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/currentRightClickContext");
			var FracHitDate = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/fracHitDate");
			if (FracHitDate === null) {
				_bvalid = false;
			}
			if (_bvalid) {
				var oPayload = {
					"fracId": oCurrContext.fracId,
					"muwi": oCurrContext.wellCode,
					"fracHitTime": FracHitDate.getTime()
				};
				var sUrl = "/taskmanagementRest/fracPack/fracHit";
				this.doAjax(sUrl, "POST", oPayload, function (oData) {
						if (oData.statusCode === "0") {
							sap.m.MessageToast.show(oData.message);
							this.onPowerBiCancel();
							this.getFracListDetails();
						} else {
							this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						}
					}.bind(this),
					function (oError) {
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			} else {
				sap.m.MessageToast.show("Enter Frac Hit Date");
			}
		},
		onFracMarkCompleted: function (oEvent) {
			var aMarkedItems = [];
			var oFracListTable = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "fracMonitoringTableView");
			var oContexts = oFracListTable.getSelectedContexts();
			oContexts.map(function (oItem) {
				var oObj = {
					"fracId": oItem.getObject().fracId,
					"muwi": oItem.getObject().wellCode
				};
				aMarkedItems.push(oObj);
			});
			this.callMarkCompleted(aMarkedItems);
		},
		callMarkCompleted: function (oItems) {
			var sUrl = "/taskmanagementRest/fracPack/markCompleted";
			this.doAjax(sUrl, "POST", oItems, function (oData) {
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						var oFracListTable = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "fracMonitoringTableView");
						oFracListTable.removeSelections();
						this.getModel("dashBoardModel").setProperty("/fracMonitoringData/markCompleteEnabled", false);
						this.getFracListDetails();
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		onWellStatusChange: function (oEvent) {
			var currentChangeContext = oEvent.getSource().getParent().getBindingContext("dashBoardModel").getObject();
			var sUrl = "/taskmanagementRest/fracPack/wellStatus";
			var oPayload = [{
				"fracId": currentChangeContext.fracId,
				"muwi": currentChangeContext.wellCode,
				"wellStatus": currentChangeContext.wellStatus
			}];
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						var oFracListTable = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "fracMonitoringTableView");
						oFracListTable.removeSelections();
						this.getModel("dashBoardModel").setProperty("/fracMonitoringData/markCompleteEnabled", false);
						this.getFracListDetails();
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		onCreateFracEngButtonPress: function (oEvent) {
			var oFragmentId = "createFracPackFragment",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.CreateFracPack";
			if (!this.createFracPackFragment) {
				this.createFracPackFragment = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.createFracPackFragment);
			}
			this.createFracPackFragment.open();
		},
		onFracCreatePress: function (oEvent) {
			if (this.FracSelectWellDialog) {
				this.FracSelectWellDialog.getItems().filter(function (oItem) {
					oItem.setSelected(false);
				});
			}
			var oFracData = this.getModel("dashBoardModel").getProperty("/fracMonitoringData");
			var oSelectedWells = oFracData.selectedWells;
			var sFracDescription = oFracData.fracDescription;
			var oWellDataToSend = [];
			var oLoggedInUserId = this.getModel("dashBoardModel").getProperty("/userData/userId");
			var oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			if (sFracDescription !== null && sFracDescription !== "" && sFracDescription.trim().length > 0) {
				oSelectedWells.map(function (oItem) {
					oWellDataToSend.push({
						"fieldCode": oItem.fieldCode,
						"wellCode": oItem.wellCode,
						"startAt": oItem.startAt !== null ? oItem.startAt.getTime() : null,
						"estBolDate": oItem.estBolDate !== null ? oItem.estBolDate.getTime() : null,
						"scenario": oItem.scenario,
						"maxTubePressure": parseFloat(oItem.maxTubePressure),
						"maxCasePressure": parseFloat(oItem.maxCasePressure),
						"boed": oItem.boed,
						"distFrac": parseFloat(oItem.distFrac),
						"orientation": oItem.orientation,
						"zone": oItem.zone,
						"userId": oLoggedInUserId,
						"userRole": oRoleData,
						"wellName": oItem.wellName
					});
				});
				var nullCheck = true;
				var EstimatedBolCheck = true;
				oWellDataToSend.map(function (value) {
					if (!value.scenario || !value.startAt || !value.maxTubePressure || !value.maxCasePressure || !value.distFrac) {
						nullCheck = false;
					}
					if (value.scenario == "10000 psi Secure for Frac" || value.scenario == "5000 psi Secure for Frac" || value.scenario ==
						"Rod pump Well: Shut down & Monitor" || value.scenario == "Flowing/Gas: Shut down & Monitor") {
						if (!value.estBolDate) {
							EstimatedBolCheck = false;
						}
					}
				});
				if (nullCheck && EstimatedBolCheck) {
					var oPayload = {
						"description": sFracDescription,
						"fracPacks": oWellDataToSend,
					};
					var sUrl = "/taskmanagementRest/fracPack/add";
					this.doAjax(sUrl, "POST", oPayload, function (oData) {
							if (oData.statusCode === "0") {
								this._showToastMessage(oData.message);
								this.getModel("dashBoardModel").setProperty("/fracMonitoringData/selectedWells", null);
								this.getModel("dashBoardModel").setProperty("/fracMonitoringData/fracDescription", null);
								this.createFracPackFragment.close();
								this.getFracListDetails();
							} else {
								this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
							}
						}.bind(this),
						function (oError) {
							this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
						}.bind(this));
				} else {
					this._showToastMessage("Please fill all the mandatory fields in the table");
				}
			} else {
				this._showToastMessage("Please enter a Frac Description");
			}
		},
		// Location History Navigation from Frac
		onFracNavToLochistory: function (oEvent) {
			var oCurrContext = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/currentRightClickContext");
			oCurrContext.locationType = "Well"; //AN: #Notif
			var pVAction = oEvent.getSource().getText();
			if (pVAction === "Location History") {
				this.getModel("dashBoardModel").setProperty("/panelProperties/currentSelectKey", "locHistory");
				this.setSelectedModule(); //AN: #Notif
				this.getView().getModel("dashBoardModel").setProperty("/locationHistorySelectTabs/iconFilterTab", "Task");
				this.setSelectedLocationExternally(oCurrContext); //AN: #Notif
			}
		},
		onFracCreateTableSelectionChange: function (oEvent) {
			if (oEvent.getSource().getSelectedItems().length > 0) {
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/deleteEnabled", true);
			} else {
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/deleteEnabled", false);
			}
		},
		onFracMainTableSelectionChange: function (oEvent) {
			if (oEvent.getSource().getSelectedItems().length > 0) {
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/markCompleteEnabled", true);
			} else {
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/markCompleteEnabled", false);
			}
		},
		onFracItemDeletePress: function (oEvent) {
			var oSelectedContexts = [];
			this.createFracPackFragment.getContent()[0].getSelectedContexts().map(function (oContext) {
				oSelectedContexts.push(
					oContext.getObject());
			});
			oSelectedContexts.filter(function (eachSelectedContext) {
				this.FracSelectWellDialog.getItems().find(function (oEachWellItems) {
					if (oEachWellItems.getBindingContext("oAllWellModel").getObject().muwi === eachSelectedContext.wellCode) {
						oEachWellItems.setSelected(false);
					}
				});
			}.bind(this));
			var oCurrentAllItems = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/selectedWells");
			var oNewFracWellArryObject = oCurrentAllItems.filter(function (objFromA) {
				return !oSelectedContexts.find(function (objFromB) {
					return objFromA.wellCode === objFromB.wellCode;
				});
			});
			this.createFracPackFragment.getContent()[0].removeSelections();
			this.getModel("dashBoardModel").setProperty("/fracMonitoringData/selectedWells", oNewFracWellArryObject);
		},
		onFracCancelPress: function () {
			this.createFracPackFragment.close();
			this.getModel("dashBoardModel").setProperty("/fracMonitoringData/selectedWells", null);
			this.getModel("dashBoardModel").setProperty("/fracMonitoringData/fracDescription", null);

			if (this.FracSelectWellDialog) {
				this.FracSelectWellDialog.getItems().filter(function (oItem) {
					oItem.setSelected(false);
				});
			}
		},
		onFracAddWellPress: function () {
			var oFragmentId = "fracAddWellFragment",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.FracSelectWellDialog";
			if (!this.FracSelectWellDialog) {
				this.FracSelectWellDialog = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.FracSelectWellDialog);
				/*	this.FracSelectWellDialog.attachEventDelegate({
						onAfterRendering: function () {
						}
					});*/
			}
			this.FracSelectWellDialog.getBinding("items").filter([]);
			this.FracSelectWellDialog.open();
			if (this.getModel("dashBoardModel").getProperty("/fracMonitoringData/isEditing")) {
				this.FracSelectWellDialog._removeSelection();
				var aedit = this.getModel("dashBoardModel").getData().fracMonitoringData.editFrac.fracPackWells;
				var a = this.FracSelectWellDialog.getItems();
				var arr = [];
				for (var i = 0; i < aedit.length; i++) {
					arr.push(aedit[i].wellName);
				}
				for (var j = 0; j < a.length; j++) {
					if (arr.includes(a[j].getTitle())) {
						a[j].setSelected(true);
					}
				}
			}

		},
		handleFracWellSearch: function (oEvent) {
			var sValue = oEvent.getParameter("value");
			var oFilter = new sap.ui.model.Filter("locationText", sap.ui.model.FilterOperator.Contains, sValue);
			var oBinding = oEvent.getSource().getBinding("items");
			oBinding.filter([oFilter]);
		},
		handleFracWellSelectConfirm: function (oEvent) {
			var aSelectedArry = [];
			var aContexts = oEvent.getParameter("selectedContexts");
			if (aContexts && aContexts.length) {
				aContexts.map(function (oContext) {
					aSelectedArry.push({
						"wellCode": oContext.getObject().muwi,
						"location": oContext.getObject().location,
						"wellName": oContext.getObject().locationText
					});
				});
				this._getCreateFracDataFromService(aSelectedArry);
			}
			oEvent.getSource().getBinding("items").filter([]);
		},
		_getCreateFracDataFromService: function (aPayloadArry) {
			var oPayload = {
				"fracPacks": aPayloadArry
			};
			var sUrl = "/taskmanagementRest/fracPack/boed";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.responseMessage.statusCode === "0") {
						if (this.getModel("dashBoardModel").getProperty("/fracMonitoringData/isEditing")) {
							var fracEdit = this.getModel("dashBoardModel").getData().fracMonitoringData.editFrac;
							var boedFarc = oData.fracPacks;
							var result = [];
							for (var i = 0; i < boedFarc.length; i++) {
								var oBoed = boedFarc[i];
								var sBoedWellName = boedFarc[i].wellName;
								var b;
								var oFarc;
								for (var j = 0; j < fracEdit.fracPackWells.length; j++) {
									oFarc = fracEdit.fracPackWells[j];
									if (sBoedWellName == fracEdit.fracPackWells[j].wellName) {
										oFarc.action = "update";
										result.push(oFarc);
										b = false;
										break;
									} else {
										b = true;
									}
								}
								if (b) {
									oBoed.fracId = oFarc.fracId;
									oBoed.isCheckbox = true;
									oBoed.isSelected = false;
									oBoed.action = "create";
									result.push(oBoed);
								}
							}

							this.getModel("dashBoardModel").setProperty("/fracMonitoringData/editFrac/fracPackWells", result);
							var aFracPackwells = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/editFrac/fracPackWells");
							for (var k = 0; k < aFracPackwells.length; k++) {
								if (aFracPackwells[k].isCheckbox === true) {
									this.getModel("dashBoardModel").setProperty("/fracMonitoringData/editFracDeleteEnabled", true);
									break;
								}
							}

						} else {
							var aAlreadySelectedWells = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/selectedWells");
							if (aAlreadySelectedWells) {
								var aNewWells = oData.fracPacks;
								$.each(aNewWells, function (indx, val) {
									aAlreadySelectedWells.map(function (oItem) {
										if (oItem.wellCode === val.wellCode) {
											aNewWells[indx] = oItem;
										}
									});
								});
								this.getModel("dashBoardModel").setProperty("/fracMonitoringData/selectedWells", aNewWells);
							} else {
								this.getModel("dashBoardModel").setProperty("/fracMonitoringData/selectedWells", oData.fracPacks);
							}
						}
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		_initilizeLocalModelForTaskScheduling: function (oModel) {
			oModel.setProperty("/taskSchedulingSelectUserId", "");
			oModel.setProperty("/schedulingV2Data/busyIndicator", false);
			oModel.setProperty("/taskSchedulingSelectUser", "");
			oModel.setProperty("/schedulingV2Data", []);
			oModel.setProperty("/schedulingV2Data/taskV2templateAvaliable", false);
			oModel.setProperty("/schedulingV2Data/view", 13);
			oModel.setProperty("/taskSchedulerV2IntervalData", "3");
			oModel.setProperty("/schedulingV2Data/widths", "auto");
			oModel.setProperty("/schedulingV2Data/fullScreenView", true);
			oModel.setProperty("/schedulingV2Data/isFullscreen", false);
			oModel.setProperty("/schedulingV2Data/isInPRogress", true);
			oModel.setProperty("/schedulingV2Data/startDate", new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 5,
				0, 0, 0));
		},

		_initilizeLocalModelForCreateTaskScheduling: function (oModel) {
			oModel.setProperty("/SchedulingData", []);
			oModel.setProperty("/SchedulingData/scheduleData", []);
			oModel.setProperty("/SchedulingData/view", 13);
			oModel.setProperty("/taskSchedulerV2IntervalData", "3");
			oModel.setProperty("/SchedulingData/widths", "auto");
			oModel.setProperty("/schedulingV2Data/busyIndicator", false);
			oModel.setProperty("/SchedulingData/startDate", new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 5,
				0, 0, 0));
		},
		_initilizeLocalModelForOBXTaskScheduling: function (oModel) {
			oModel.setProperty("/OBXSchedulingData", []);
			oModel.setProperty("/OBXSchedulingData/scheduleData", []);
			oModel.setProperty("/OBXSchedulingData/recommentedTaskList", []);
			oModel.setProperty("/OBXSchedulingData/view", 11);
			oModel.setProperty("/taskSchedulerV2IntervalData", "3");
			oModel.setProperty("/OBXSchedulingData/widths", "auto");
			oModel.setProperty("/OBXSchedulingData/busyIndicator", false);
			oModel.setProperty("/OBXSchedulingData/recommentedTasklistBusy", false);
			oModel.setProperty("/OBXSchedulingData/startDate", new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(),
				7,
				0, 0, 0));
			oModel.setProperty("/OBXSchedulingData/obxEngine", {}); //AN: #obxEngine
			oModel.setProperty("/OBXSchedulingData/selectedKey", "obxScheduling"); //AN: #obxEngine
		},
		onTaskSchedulingV2TabSelect: function (isBind) {
			var oRoleData;
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "US") {
					oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				} else {
					oRoleData = "IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
				}
			} else {
				oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			}
			var timeOffset = new Date().getTimezoneOffset();
			var oUsers = "";
			oUsers = this.getModel("dashBoardModel").getProperty("/taskSchedulingSelectUserId");
			var sUrl;
			if (typeof (isBind) === "object") {
				var oCalendarStartDate = isBind.getSource().getStartDate();
				this.getModel("dashBoardModel").setProperty("/schedulingV2Data/startDate", oCalendarStartDate);
			}
			var oDate = this.getModel("dashBoardModel").getData().schedulingV2Data.startDate;
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				// pattern: "YYYY/MM/dd"
				pattern: "yyyy/MM/dd" //George - 30/12/2020 - INC0105444
			});
			var dateFormatted = dateFormat.format(oDate);
			var aStartDate = oDate.toString().split(" ");
			var aStartTime = aStartDate[4].split(":");
			var sStarTime = aStartTime[0];
			var sEndDate;
			if (sStarTime) {
				var oEndDate = new Date(oDate);
				oEndDate = oEndDate.setDate(oEndDate.getDate() + 1);
				oEndDate = new Date(oEndDate);
				sEndDate = dateFormat.format(oEndDate);
			}
			if (oUsers !== "") {
				sUrl = "/taskmanagementRest/scheduling/getTasks?userId=" + oUsers + "&role=" + oRoleData + "&timeZoneOffSet=" + timeOffset +
					"&fromDate=" + dateFormatted + "&toDate=" + sEndDate;
			} else {
				sUrl = "/taskmanagementRest/scheduling/getTasks?role=" + oRoleData + "&timeZoneOffSet=" + timeOffset + "&fromDate=" +
					dateFormatted + "&toDate=" + sEndDate;
			}
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/busyIndicator", true);
			this.doAjax(sUrl, "GET", null, function (oData) {
				jQuery.sap.delayedCall(1000, this, function () {
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/busyIndicator", false);
				}.bind(this));
				if (oData.responseMessage.status === "SUCCESS") {
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/scheduleData", oData.taskList);
					// if (isBind) {
					// 	var isFullScreen = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/isFullscreen");
					// 	var a, b;
					// 	if (isFullScreen) {
					// 		b = sap.ui.getCore().byId("TaskSchedulingV2FullScreen--PC2");
					// 		b.bindAggregation("rows", {
					// 			path: "dashBoardModel>/schedulingV2Data/scheduleData",
					// 			template: b.getRows()[0].clone()
					// 		});
					// 	}
					// 	a = sap.ui.core.Fragment.byId(this.createId("taskschedulerFragment"), "PC2");
					// 	a.bindAggregation("rows", {
					// 		path: "dashBoardModel>/schedulingV2Data/scheduleData",
					// 		template: a.getRows()[0].clone()
					// 	});
					// }
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/taskV2templateAvaliable", true);
				}
			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/schedulingV2Data/busyIndicator", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onSliderChange: function (oEvent) {
			var oValue = parseInt(oEvent.getSource().getValue(), 10);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/view", oValue);
		},
		handleV2TaskSelect: function (oEvent) {
			var sBtnText = "Create Task";
			this.getModel("dashBoardModel").setProperty("/isTaskCardPress", true);
			if (oEvent.getParameter("appointment").getBindingContext("dashBoardModel") != undefined) {
				var oObject = oEvent.getParameter("appointment").getBindingContext("dashBoardModel").getObject();
			}
			var taskId = oObject.taskId;
			var taskStatus = oObject.status;
			this.getModel("dashBoardModel").setProperty("/taskId", taskId);
			this.getModel("dashBoardModel").setProperty("/taskStatus", taskStatus);
			this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", false);
			this.getModel("dashBoardModel").setProperty("/isInquiryCreate", false);
			this.getModel("dashBoardModel").setProperty("/processId", oObject.processId);
			this.getModel("dashBoardModel").setProperty("/taskCardData", oObject);
			this.getModel("dashBoardModel").setProperty("/processId", oObject.processId);
			this.getModel("dashBoardModel").setProperty("/createInvestigationObject", oObject); //#RV: Investigation ALS add Miles Changes
			this.onCreateTaskPress("", sBtnText);

		},
		handleChangeTaskSchedulerV2SelectPopover: function (oEvent) {
			var oAdditionalTime = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/currentClickContext/additionalTime");
			var oSplitTimeArry = oAdditionalTime.split(":");
			var oAdditionalTimeInMinutes = (parseInt(oSplitTimeArry[0], 10) * 60) + parseInt(oSplitTimeArry[1], 10);
			if (oAdditionalTimeInMinutes) {
				if (oAdditionalTimeInMinutes <= 120) {
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/additionalTimeState", "None");
				} else {
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/additionalTimeState", "Error");
				}
			}
		},
		onTaskScheduleV2PopoverOK: function (oEvent) {
			var oContextObj = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/currentClickContext/sObj");
			var newDateTime = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/currentClickContext/ChangableStartDateTime");
			var sTaskOwner = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/currentClickContext/sTaskOwner");
			var sTaskOwnerPid = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/currentClickContext/sTaskOwnerPid");
			var oAdditionalTimeInMinutes = 0;
			var oAdditionalTime = this.getModel("dashBoardModel").getProperty("/schedulingV2Data/currentClickContext/additionalTime");
			if (oAdditionalTime != "") {
				var oSplitTimeArry = oAdditionalTime.split(":");
				oAdditionalTimeInMinutes = (parseInt(oSplitTimeArry[0], 10) * 60) + parseInt(oSplitTimeArry[1], 10);
			}
			if (oAdditionalTimeInMinutes <= 120) {
				var sPath = oContextObj.sPath;
				var sParentAppointMentPath = sPath.substring(0, sPath.lastIndexOf("/"));
				var oContext = oContextObj.getObject();
				var oAppointments = this.getModel("dashBoardModel").getProperty(sParentAppointMentPath);
				var sStartTime, sEndTime;
				sStartTime = oContext.startTime;
				sEndTime = oContext.endTime;
				var inProgressCheck = true;
				if (oContext.status === "IN PROGRESS") {
					inProgressCheck = false;
				}
				var validation = this.doTaskV2OperationCheck(oContext.status, sStartTime, sEndTime, new Date().getTime(), newDateTime,
					inProgressCheck);
				this.taskSchedulingV2Popover.close();
				if (validation.status) {
					var oPayload = {
						"taskSchedulingDtoListForUser": oAppointments,
						"newStartDate": newDateTime.getTime(),
						"taskId": oContext.taskId,
						"isTaskShift": true,
						"taskOwner": sTaskOwner,
						"customOffset": oAdditionalTimeInMinutes
					};
					this._doTaskSchedulingV2Update(oPayload, sTaskOwnerPid);
				} else {
					this._showToastMessage(validation.message);
				}
			}
		},
		onTaskScheduleV2PopoverCancel: function () {
			this.taskSchedulingV2Popover.close();
		},
		handleTaskV2Drop: function (oEvent) {
			var sPath = oEvent.getParameter("appointment").getBindingContext("dashBoardModel").sPath;
			var sParentAppointMentPath = sPath.substring(0, sPath.lastIndexOf("/"));
			var oContext = oEvent.getParameter("appointment").getBindingContext("dashBoardModel").getObject();
			var oAppointments = this.getModel("dashBoardModel").getProperty(sParentAppointMentPath);
			var sStartTime, sEndTime;
			sStartTime = oContext.startTime;
			sEndTime = oContext.endTime;
			var validation = this.doTaskV2OperationCheck(oContext.status, sStartTime, sEndTime, new Date().getTime(), oEvent.getParameter(
				"startDate").getTime(), true);
			if (validation.status) {
				var oPayload = {
					"taskSchedulingDtoListForUser": oAppointments,
					"newStartDate": oEvent.getParameter("startDate").getTime(),
					"taskId": oContext.taskId,
					"isTaskShift": true,
					"taskOwner": oEvent.getSource().getTitle()
				};
				this._doTaskSchedulingV2Update(oPayload, oEvent.getParameter("appointment").getParent().getBindingContext("dashBoardModel").getObject()
					.pId);
			} else {
				this._showToastMessage(validation.message);
			}
		},
		_doTaskSchedulingV2Update: function (oPayload, taskOwnerName) {
			var sUrl = "/taskmanagementRest/scheduling/updatePriority";
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/busyIndicator", true);
			var pIdPayload = {
				"ParamRoot": {
					"Param": [{
						"userID": taskOwnerName
					}]
				}
			};
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/busyIndicator", false);
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						this.onTaskSchedulingV2TabSelect(true);
						this.sendNotification("Task priorities are rescheduled", pIdPayload, false);
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						this.getModel("dashBoardModel").setProperty("/busyIndicators/taskSchedulingBusy", false);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/schedulingV2Data/busyIndicator", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		doTaskV2OperationCheck: function (sStatus, startTime, endTime, currentTime, newStatTime, inProgressCheck) {
			if (sStatus === "RESOLVED" || sStatus === "COMPLETED") {
				return {
					"status": false,
					"message": "The task is already " + sStatus.toLowerCase()
				};
			} else if (inProgressCheck) {
				if (sStatus === "IN PROGRESS") {
					return {
						"status": false,
						"message": "The task is in-progress"
					};
				} else if (newStatTime < currentTime) {
					return {
						"status": false,
						"message": "Task cannot be scheduled to past time"
					};
				} else {
					return {
						"status": true,
						"message": ""
					};
				}
			} else {
				return {
					"status": true,
					"message": ""
				};
			}
		},
		handleTaskScheduleV2Settings: function (oEvent) {
			var oFragmentId = "TaskSchedulingV2Settings",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.TaskSchedulingV2Settings";
			if (!this.taskSchedulingV2SettingsPopover) {
				this.taskSchedulingV2SettingsPopover = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.taskSchedulingV2SettingsPopover);
			}
			this.taskSchedulingV2SettingsPopover.openBy(oEvent.getSource());
		},
		handleTaskScheduleV2Fullscreen: function () {
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/isFullscreen", true);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/fullScreenView", false);
			var oFragmentId = "TaskSchedulingV2FullScreen",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.TaskSchedulingV2FullScreen";
			if (!this.taskSchedulingV2FullscreenDialog) {
				this.taskSchedulingV2FullscreenDialog = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.taskSchedulingV2FullscreenDialog);
			}
			this.taskSchedulingV2FullscreenDialog.open();
		},

		onCloseTaskSchedulerV2FullScreen: function () {
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/fullScreenView", true);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/widths", "auto");
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/view", 13);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/widthsDefault", 0);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/isFullscreen", false);
			this.taskSchedulingV2FullscreenDialog.close();
			this.taskSchedulingV2FullscreenDialog.destroy();
			this.taskSchedulingV2FullscreenDialog = undefined;
		},

		progressBar: function (timeleft, timetotal, $element) {
			this.getModel("dashBoardModel").setProperty("/alarmRefresh", false);
			var progressBarWidth = timeleft * $element.width() / timetotal;
			$element.find('div').animate({
				width: progressBarWidth
			}, 500);
			var iMin = Math.floor(timeleft / 60);
			iMin = "0" + iMin;
			var iSec = timeleft % 60;
			if (iSec < 10) {
				iSec = "0" + iSec;
			} else {
				iSec = iSec.toString();
			}
			this.getModel("dashBoardModel").setProperty("/alarmTimerTime", iMin + ":" + iSec + " min");
			if (timeleft > 0) {
				this.tick = setTimeout(function () {
					this.progressBar(timeleft - 1, timetotal, $('#progressBar'));
				}.bind(this), 1000);
			} else {
				this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
				this.getModel("dashBoardModel").setProperty("/prograssBarVisible", false);
				this.getAlarmData();
				clearTimeout(this.tick);
				this.tick = false;
			}
		},
		onAlarmSelectionChangeProgressTimer: function (oEvent) {
			if (oEvent.getSource().getSelectedItems().length >= 1) {

				if (!this.tick) {
					this.getModel("dashBoardModel").setProperty("/prograssBarVisible", true);
					this.progressBar(120, 120, $('#progressBar'));
				}
			} else {
				this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
				this.getModel("dashBoardModel").setProperty("/prograssBarVisible", false);
				clearTimeout(this.tick);
				this.tick = false;
			}
		},

		onPotFilterTilePress: function (oEvent) {
			if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
				return;
			} else {
				var sHeader = oEvent.getSource().getHeader(),
					dCurrentDomRef = oEvent.getParameter("domRef"),
					oTileData = this.getModel("dashBoardModel").getProperty("/potData/tileData");
				switch (sHeader) {
				case "Investigation":
					if (oTileData.investigationSelected) {
						this.getModel("dashBoardModel").setProperty("/potData/tileData/investigationSelected", false);
						this._setTileBgColor(dCurrentDomRef, false);
					} else {
						this.getModel("dashBoardModel").setProperty("/potData/tileData/investigationSelected", true);
						this._setTileBgColor(dCurrentDomRef, true);
					}
					break;
				case "ALS TECH":
					if (oTileData.alsTechSelected) {
						this.getModel("dashBoardModel").setProperty("/potData/tileData/alsTechSelected", false);
						this._setTileBgColor(dCurrentDomRef, false);
					} else {
						this.getModel("dashBoardModel").setProperty("/potData/tileData/alsTechSelected", true);
						this._setTileBgColor(dCurrentDomRef, true);
					}
					break;
				case "Engineering":
					if (oTileData.engineeringSelected) {
						this.getModel("dashBoardModel").setProperty("/potData/tileData/engineeringSelected", false);
						this._setTileBgColor(dCurrentDomRef, false);
					} else {
						this.getModel("dashBoardModel").setProperty("/potData/tileData/engineeringSelected", true);
						this._setTileBgColor(dCurrentDomRef, true);
					}
					break;
				}
				var sUsrTypes = "";
				if (oTileData.investigationSelected) {
					sUsrTypes = sUsrTypes + "POT,";
				}
				if (oTileData.alsTechSelected) {
					sUsrTypes = sUsrTypes + "ALS,";
				}
				if (oTileData.engineeringSelected) {
					sUsrTypes = sUsrTypes + "ENG";
				}
				if (sUsrTypes[sUsrTypes.length - 1] === ",") {
					sUsrTypes = sUsrTypes.substring(sUsrTypes, sUsrTypes.length - 1);
				}
				this.onPotDataLoad(sUsrTypes);
			}
		},

		onPotDataLoad: function (sUsrTypes) {
			var url;
			if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) {
				var potRole = this.getModel("dashBoardModel").getProperty("/userData/potRole");
				if (sUsrTypes && sUsrTypes.length) {
					url = "/taskmanagementRest/tasks/getNDVTaskList?userType=" + sUsrTypes + "&role=" + potRole + "&loggedInUserType=POT";
				} else {
					url = "/taskmanagementRest/tasks/getNDVTaskList?userType=ALS,POT,ENG&role=" + potRole;
				}
			} else if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
				var engRole = this.getModel("dashBoardModel").getProperty("/userData/engRole");
				url = "/taskmanagementRest/tasks/getNDVTaskList?userType=ENG&role=" + engRole + "&loggedInUserType=ENG";
			} else if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isAnalyticsReadOnly") || this.getModel("dashBoardModel").getProperty("/userData/isROC")) {
				if (sUsrTypes && sUsrTypes.length) {
					url = "/taskmanagementRest/tasks/getNDVTaskList?userType=" + sUsrTypes + "&role=IOP_POT_East,IOP_POT_West";
				} else {
					url = "/taskmanagementRest/tasks/getNDVTaskList?userType=ALS,POT,ENG&role=IOP_POT_East,IOP_POT_West";
				}
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/detailReport", true);
			this.doAjax(url, "GET", null, function (oData) {
				// sap.ui.core.Fragment.byId(this.createId("appscollectioncontainer--analytics"), "idAnalyticsTableSearchField").clear();
				this.getModel("dashBoardModel").setProperty("/busyIndicators/detailReport", false);
				if (oData.responseMessage) {
					if (oData.responseMessage.statusCode === "0") {
						this.getModel("dashBoardModel").setProperty("/potData/tableData", oData);
					} else {
						var sErrorMessage = oData.responseMessage.message;
						this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
					}
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},

		onPressClearPotFilter: function (oEvent) {
			$(".potTileLayout").css("backgroundColor", "");
			this.getModel("dashBoardModel").setProperty("/potData/tileData/alsTechSelected", false);
			this.getModel("dashBoardModel").setProperty("/potData/tileData/engineeringSelected", false);
			this.getModel("dashBoardModel").setProperty("/potData/tileData/investigationSelected", false);
			this.onPotDataLoad();
		},

		_setTileBgColor: function (oDom, status) {
			if (status) {
				oDom.style.backgroundColor = "rgba(113, 172, 208, 0.29)";
			} else {
				oDom.style.backgroundColor = "";
			}
		},

		_initilizeLocalModelForPOT: function (oModel) { //AN: #inquire
			var potData = {
				tableData: [],
				tileData: {
					investigationSelected: false,
					alsTechSelected: false,
					engineeringSelected: false
				},
				showInvestigate: false,
				showInquire: false
			};
			oModel.setProperty("/potData", potData);
		},

		onSuggestionListPanelExpand: function (oEvent) {
			if (oEvent.getParameter("expand")) {
				var currObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
				var sUrl = "/taskmanagementRest/tasks/getTasksByUser?userId=" + currObj.emailId + "&userType=FIELD&origin=Dispatch";
				var paths = ["createdAtInString", "taskOwner", "location", "description"];
				var oTable = oEvent.getSource().getContent()[0];
				var oTemplate = new sap.m.ColumnListItem();
				oTemplate.addStyleClass("sapUiSizeCompact");
				for (var i = 0; i < paths.length; i++) {
					if (paths[i] === "createdAtInString") {
						oTemplate.addCell(new sap.m.Text({
							text: {
								path: paths[i],
								formatter: function (iValue) {
									var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
										pattern: "dd-MMM-yy hh:mm:ss aaa"
									});
									return oDateFormat.format(new Date(Number(iValue)));
								}
							}
						}).addStyleClass("iopFontClass iopAlarmListTextClass"));
					} else {
						oTemplate.addCell(new sap.m.Text({
							text: {
								path: paths[i]
							}
						}).addStyleClass("iopFontClass iopAlarmListTextClass"));
					}
				}
				oTable.setBusyIndicatorDelay(0);
				oTable.setBusy(true);
				this.doAjax(sUrl, "GET", null, function (oData) {
					var oTempModel = new JsonModel();
					oTempModel.setData(oData);
					oTable.setModel(oTempModel);
					oTable.bindAggregation("items", "/taskList", oTemplate);
					oTable.setBusy(false);
				}.bind(this), function (oError) {
					oTable.setBusy(false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(this));
			}
		},

		_initilizePriorityTable: function (userId) {
			var oRoleData;
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "US") {
					oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				} else {
					oRoleData = "IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
				}
			} else {
				oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			}
			var sUrl;
			if (userId !== null) {
				sUrl = "/taskmanagementRest/scheduling/getTasks?userId=" + userId + "&role=" + oRoleData + "&orderBy=vDesc";
			} else {
				sUrl = "/taskmanagementRest/scheduling/getTasks?role=" + oRoleData + "&orderBy=vDesc";
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/taskSchedulingBusy", true);
			this.doAjax(sUrl, "GET", null, function (oData) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/taskSchedulingBusy", false);
				this.getModel("dashBoardModel").setProperty("/PrioritySortableItemsandDetails", oData);
			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/taskSchedulingBusy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));

		},

		handleFieldUserValueHelp: function () {
			var oFragmentId = "fielduserValuehelp",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.FieldUserSelectPopup";
			var oRoleData;
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "US") {
					oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				} else {
					oRoleData = "IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
				}
			} else {
				oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
				//oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				oRoleData="";
			}
			if (!this.fieldUserFragment) {
				this.fieldUserFragment = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.fieldUserFragment);
			}
			this.fieldUserFragment.open();
			var sUrl = "/taskmanagementRest/scheduling/getOwners?role=" + oRoleData;
			this.getModel("dashBoardModel").setProperty("/busyIndicators/FieldUserBsy", true);
			this.doAjax(sUrl, "GET", null, function (oData) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/FieldUserBsy", false);
				this.getModel("dashBoardModel").setProperty("/SchedulingFieldUsers", oData);

			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/FieldUserBsy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},

		onFieldUserPopupClose: function () {
			if (this.oSearchControl) {
				this.oSearchControl.clear();
			}
			this.fieldUserFragment.close();
		},

		onFielduserDialogItemSelect: function (oEvent) {
			var oTable = sap.ui.core.Fragment.byId("fielduserValuehelp",
				"FieldUserPopupTable");
			var user = "",
				userId = "";
			$.each(oTable.getSelectedContexts(), function (indx, val) {
				if (indx > 0) {
					userId = userId + "," + val.getObject().taskOwner;
					user = user + "," + val.getObject().taskOwnerDisplayName;
				} else {
					userId = userId + val.getObject().taskOwner;
					user = user + val.getObject().taskOwnerDisplayName;
				}
			});
			this.getModel("dashBoardModel").setProperty("/taskSchedulingSelectUser", user);
			this.getModel("dashBoardModel").setProperty("/taskSchedulingSelectUserId", userId);
			this.onTaskSchedulingV2TabSelect(true);
			if (this.oSearchControl) {
				this.oSearchControl.clear();
			}
			this.fieldUserFragment.close();
		},

		ontaskSchedulingClear: function (oEvent) {
			if (this.getModel("dashBoardModel").getProperty("/taskSchedulingSelectUser") !== "") {
				this.getModel("dashBoardModel").setProperty("/taskSchedulingSelectUser", "");
				this.getModel("dashBoardModel").setProperty("/taskSchedulingSelectUserId", "");
				var oTable = sap.ui.core.Fragment.byId("fielduserValuehelp",
					"FieldUserPopupTable");
				oTable.removeSelections();
				this.onTaskSchedulingV2TabSelect(true);
			}
		},

		onFieldUserDialogSearch: function (oEvent) {
			this.oSearchControl = oEvent.getSource();
			var sQuery = oEvent.getSource().getValue();
			var filterParams = ["taskOwner", "taskOwnerDisplayName"];
			var aFilters;
			var filterArray = [];
			if (sQuery) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, sQuery));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			var oTable = sap.ui.core.Fragment.byId("fielduserValuehelp",
				"FieldUserPopupTable");
			oTable.getBinding("items").filter(aFilters);

		},

		_initClipBoard: function () {
			var IMAGE_MIME_REGEX = /^image\/(p?jpeg|gif|png)$/i;
			document.onpaste = function (e) {
				if (e.srcElement.id === "pastearea") {
					var items = e.clipboardData.items;
					for (var i = 0; i < items.length; i++) {
						if (IMAGE_MIME_REGEX.test(items[i].type)) {
							this._loadImageFromClipBoard(items[i].getAsFile(), "chrome");
							return;
						} else {
							e.preventDefault();
							return;
						}
					}
				}
			}.bind(this);
			$('body').on('focus', '[contenteditable]', function () {
				var $this = $(this);
				$this.data('before', $this.html());
				return $this;
			}).on('blur keyup paste input', '[contenteditable]', function () {
				var $this = $(this);
				if ($this.data('before') !== $this.html()) {
					$this.data('before', $this.html());
					$this.trigger('change');
				}
				return $this;
			});
		},

		_loadImageFromClipBoard: function (file, browser) {
			var attachmentObject = {};
			attachmentObject.fileName = file.name;
			if (browser === "ie") {
				attachmentObject.fileName = "image.png";
			}
			attachmentObject.fileSize = file.size;
			attachmentObject.fileType = file.type;
			var reader = new FileReader();
			reader.onload = function (e) {
				attachmentObject.fileDoc = e.target.result;
				attachmentObject.fileDoc = attachmentObject.fileDoc.split("base64,")[1];
				attachmentObject.compressedFile = attachmentObject.fileDoc;
				this.onUploadingFile(attachmentObject);
			}.bind(this);
			reader.readAsDataURL(file);
		},

		onPTWIconTabChange: function () {
			var oSelModule = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var currentSelectedTab;
			currentSelectedTab = this.getModel("dashBoardModel").getProperty("/ptwSelectedKey");
			this.getPermittoWorkList(currentSelectedTab);

		},
		/* SK : To get the JSA and all types of permit List*/

		//#RV: To get Rolled Up data for PTW
		getPTWList: function (currentTab, Month) {
			var oSelModule = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var pageNumber = this.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwPageNumber");
			var start = (parseInt(pageNumber) * 100) - 100;
			var sLocationType = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			var oSelectedDtos = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (oSelectedDtos.length > 0) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", true);
				this.getModel("dashBoardModel").setProperty("/ptwJsaTable", []);
				this.getModel("dashBoardModel").setProperty("/ptwPermitTable", []);
				var loc = "",
					len = oSelectedDtos.length;
				if (sLocationType === "Well" || sLocationType === "SEARCH") {
					sLocationType = "Well";
					$.each(oSelectedDtos, function (indx, val) {
						if (indx !== len - 1) {
							loc = loc + "FACILTYORSITE eq '" + val.locationText.trim() + "' or ";
						} else {
							loc = loc + "FACILTYORSITE eq '" + val.locationText.trim();
						}
					});
				} else {
					$.each(oSelectedDtos, function (indx, val) {
						if (indx !== len - 1) {
							loc = loc + "FACILTYORSITE eq '" + val.locationText.trim() + "' or ";
						} else {
							loc = loc + "FACILTYORSITE eq '" + val.locationText.trim();
						}
					});
				}
				var sUrl;
				switch (currentTab) {
				case "Active JSA":
					//	sUrl = "/ptwRest/GetJSAForROC.xsjs?Location=" + loc + "&LocationType=" + sLocationType.toUpperCase();
					var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
						// pattern: "YYYY-MM-dd"
						pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
					});
					var date = new Date();
					if (Month === "three") {
						var month = date.getMonth() - 3;
						date.setMonth(month);
						var dateFormatted = dateFormat.format(date);
					} else {
						var month = date.getMonth() - 1;
						date.setMonth(month);
						var dateFormatted = dateFormat.format(date);
					}
					start = start + "";
					if (oSelModule === "locHistory") {
						sUrl =
							"/ptwRest/getJSAList.xsodata/JSAHeader?$inlinecount=allpages&$top=100&$skip=" + start + "&$filter=(" + loc +
							"') and CREATEDDATE ge datetime'" + dateFormatted + "T00:00:00' and ISACTIVE eq 0&$format=json";
					} else {
						sUrl =
							"/ptwRest/getJSAList.xsodata/JSAHeader?$inlinecount=allpages&$top=100&$skip=" + start + "&$filter=(" + loc +
							"')and ISACTIVE eq 1&$format=json";
					}
					this.doAjax(sUrl, "GET", null, function (oData) {

						$.each(oData.d.results, function (index, value) {
							if (value.PERMITNUMBER) {
								value.PERMITNUMBERString = value.PERMITNUMBER.toString();
							}
						});
						if (oData.d.__count == 0) {
							this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
						} else {
							this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", true);
						}
						if (oData.d.__count <= 100) {
							var sText = 1 + "- " + oData.d.__count + " of " + oData.d.__count;
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/PaginationText", sText);
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwEndCount", oData.d.__count);
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", false);
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", false);
						} else {
							var pageNumber = this.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwPageNumber");
							var start = (parseInt(pageNumber) * 100) - 99;
							var end = start + 99;
							if (end > oData.d.__count) {
								end = oData.d.__count;
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", false);
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", true);
							}
							if (start > 100 && end !== oData.d.__count) {
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", true);
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", true);
							} else if (start < 100 && end !== oData.d.__count) {
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", false);
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", true);
							}
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", start);
							this.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwEndCount", end);
							var sText = start + "- " + end + " of " + oData.d.__count;
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/PaginationText", sText);
						}
						this.getModel("dashBoardModel").setProperty("/ptwTotalcountotal", oData.d.__count);
						this.getModel("dashBoardModel").setProperty("/ptwJsaTable", oData.d);
						this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
					}.bind(this), function (oError) {
						this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
						this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
						var sErrorMessage;
						sErrorMessage = oError.getParameter("statusText");
						this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
					}.bind(this));
					break;
				default:
					var loc = "";
					if (sLocationType === "Well" || sLocationType === "SEARCH") {
						sLocationType = "Well";
						$.each(oSelectedDtos, function (indx, val) {
							if (indx !== len - 1) {
								loc = loc + "LOCATION eq '" + val.locationText.trim() + "' or ";
							} else {
								loc = loc + "LOCATION eq '" + val.locationText.trim();
							}
						});
					} else {
						$.each(oSelectedDtos, function (indx, val) {
							if (indx !== len - 1) {
								loc = loc + "LOCATION eq '" + val.locationText.trim() + "' or ";
							} else {
								loc = loc + "LOCATION eq '" + val.locationText.trim();
							}
						});
					}
					start = start + "";
					sUrl = "/ptwRest/getPermitList.xsodata/PermitHeader?$inlinecount=allpages&$top=100&$skip=" + start + "&$filter=";
					if (currentTab === "Active CW") {
						sUrl = sUrl + "ISCWP eq 1 and (" + loc;
					}
					if (currentTab === "Active HW") {
						sUrl = sUrl + "ISHWP eq 1 and (" + loc;
					}
					if (currentTab === "Active CSP") {
						sUrl = sUrl + "ISCSE eq 1 and (" + loc;
					}

					if (oSelModule === "locHistory") {
						var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
							// pattern: "YYYY-MM-dd"
							pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
						});
						var date = new Date();
						if (Month === "three") {
							var month = date.getMonth() - 3;
							date.setMonth(month);
							var dateFormatted = dateFormat.format(date);
							sUrl = sUrl + "') and PLANNEDDATETIME ge datetime'" + dateFormatted + "T00:00:00'";
						} else {
							var month = date.getMonth() - 1;
							date.setMonth(month);
							var dateFormatted = dateFormat.format(date);
							sUrl = sUrl + "') and PLANNEDDATETIME ge datetime'" + dateFormatted + "T00:00:00'";
						}
						sUrl = sUrl + " and ISACTIVE eq 0&$format=json";
					} else {
						sUrl = sUrl + "') and ISACTIVE eq 1&$format=json";
					}
					this.doAjax(sUrl, "GET", null, function (oData) {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
						if (oData.d.__count == 0) {
							this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
						} else {
							this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", true);
						}
						$.each(oData.d.results, function (index, value) { //AN: #obxSearch
							if (value.PERMITNUMBER) {
								value.PERMITNUMBERString = value.PERMITNUMBER.toString();
							}
						});
						this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
						this.getModel("dashBoardModel").setProperty("/ptwPermitTable", oData.d);
						if (oData.d.__count <= 100) {
							var sText = 1 + "- " + oData.d.__count + " of " + oData.d.__count;
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/PaginationText", sText);
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwEndCount", oData.d.__count);
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", false);
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", false);
						} else {
							var pageNumber = this.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwPageNumber");
							var start = (parseInt(pageNumber) * 100) - 99;
							var end = start + 99;
							if (end > oData.d.__count) {
								end = oData.d.__count;
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", false);
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", true);
							}
							if (start > 100 && end !== oData.d.__count) {
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", true);
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", true);
							} else if (start < 100 && end !== oData.d.__count) {
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", false);
								this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", true);
							}
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", start);
							this.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwEndCount", end);
							var sText = start + "- " + end + " of " + oData.d.__count;
							this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/PaginationText", sText);
						}
						this.getModel("dashBoardModel").setProperty("/ptwTotalcountotal", oData.d.__count);

					}.bind(this), function (oError) {
						this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
						this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
						var sErrorMessage;
						sErrorMessage = oError.getParameter("statusText");
						this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
					}.bind(this));
				}
			} else {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
				this.getModel("dashBoardModel").setProperty("/ptwJsaTable", []);
				this.getModel("dashBoardModel").setProperty("/ptwPermitTable", []);
			}

		},
		getPermittoWorkList: function (currentTab, Month) {
			this.getPTWList(currentTab, Month);
			var oLocationSelected = this.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oLocationCode = "";
			if (oLocationSelected.length > 0) {
				for (var i = 0; i < oLocationSelected.length - 1; i++) {
					oLocationCode += oLocationSelected[i].locationText.trim() + ",";
				}
				oLocationCode += oLocationSelected[i].locationText.trim();
				var sUrl = "/ptwRest/getPTWLocationHistory.xsjs?date=2018-05-01&location=" + oLocationCode;
				this.doAjax(sUrl, "GET", null, function (oData) {
					if (oData && oData.status && oData.status.length > 0) {
						this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPTWStatusData", oData.status);
					} else {
						this.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPTWStatusData", []);
					}
				}.bind(this), function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					sap.m.MessageToast.show(sErrorMessage);
				}.bind(this));
			}

		},

		getCommaSeperatedLocFromDto: function (oDto, oType) {
			var loc = "",
				length = oDto.length;
			$.each(oDto, function (indx, val) {
				if (indx !== length - 1) {
					loc = loc + "'" + val[oType] + "',";
				} else {
					loc = loc + "'" + val[oType] + "'";
				}
			});
			return loc;
		},

		/*onPtwTableSerach: function (oEvent) {
			var oTable;
			var sQuery = oEvent.getSource().getValue();
			var currentSelectedTab = this.getModel("dashBoardModel").getProperty("/ptwSelectedKey");
			var filterParams;
			if (currentSelectedTab === "Active JSA") {
				oTable = sap.ui.core.Fragment.byId(this.createId("ptwFragment"), "permitToworkJsaTable");
				filterParams = ["createdBy", "taskDescription", "facilityorsite", "createdDate", "jsaPermitNumber"];
			} else {
				oTable = sap.ui.core.Fragment.byId(this.createId("ptwFragment"), "permitToWorkPermitTable");
				filterParams = ["createdBy", "taskDescription", "facilityorsite", "createdDate", "jsaPermitNumber", "ptwPermitNumber"];
			}
			var aFilters;
			var filterArray = [];
			if (sQuery) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, sQuery));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTable.getBinding("items").filter(aFilters);
		},*/
		onPTWTableSearchFromPTW: function (oEvent) {
			var oTable;
			var sQuery = oEvent.getSource().getValue();
			var currentSelectedTab = this.getModel("dashBoardModel").getProperty("/ptwSelectedKey");
			var filterParams;
			if (currentSelectedTab === "Active JSA") {
				oTable = sap.ui.core.Fragment.byId(this.createId("ptwFragment"), "permitToworkJsaTable");
				//filterParams = ["createdBy", "taskDescription", "facilityorsite", "createdDate", "jsaPermitNumber"];
				var filterParams = ["PERMITNUMBERString", "CREATEDBY", "TASKDESCRIPTION", "FACILTYORSITE"];
			} else {
				oTable = sap.ui.core.Fragment.byId(this.createId("ptwFragment"), "permitToWorkPermitTable");
				//filterParams = ["createdBy", "taskDescription", "facilityorsite", "createdDate", "jsaPermitNumber", "ptwPermitNumber"];
				filterParams = ["CREATEDBY", "LOCATION", "PERMITNUMBERString"];
			}
			var aFilters;
			var filterArray = [];
			if (sQuery) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, sQuery));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTable.getBinding("items").filter(aFilters);

		},

		getUserDetails: function () {
			var sUrl = "/services/userapi/attributes";
			var oModel = new sap.ui.model.json.JSONModel();
			var that = this;
			oModel.loadData(sUrl, true, "GET", false, false);
			oModel.attachRequestCompleted(function (oEvent) {
				if (oEvent.getParameter("success")) {
					var resultData = oEvent.getSource().getData();
					if (resultData) {
						that.getLoggedUserDetails(resultData.UserId);
					}
				} else {
					sap.m.MessageToast.show("Error in Retrieving User Details");
				}
			});
			oModel.attachRequestFailed(function (oEvent) {
				sap.m.MessageToast.show("Error in Retrieving User Details");
			});
		},
		getUserDetailsFromDOPRefresh: function () {
			var sUrl = "/services/userapi/attributes";
			var oModel = new sap.ui.model.json.JSONModel();
			var that = this;
			oModel.loadData(sUrl, true, "GET", false, false);
			oModel.attachRequestCompleted(function (oEvent) {
				if (oEvent.getParameter("success")) {
					var resultData = oEvent.getSource().getData();
					if (resultData) {
						//that.getLoggedUserDetails(resultData.userId);
					}
				} else {
					sap.m.MessageToast.show("Error in Retrieving User Details");
				}
			});
			oModel.attachRequestFailed(function (oEvent) {
				sap.m.MessageToast.show("Error in Retrieving User Details");
			});
		},
		getLoggedUserDetails: function (oUserId) {
			var dashBoardModel = this.getModel("dashBoardModel");
			var sUrl = "/destination/Incture_tenant/service/scim/Users/" + oUserId;
			var oModel = new sap.ui.model.json.JSONModel();
			var that = this;
			oModel.loadData(sUrl, true, "GET", false, false);
			oModel.attachRequestCompleted(function (oEvent) {
				if (oEvent.getParameter("success")) {
					var resultData = oEvent.getSource().getData();
					dashBoardModel.setProperty("/oUserDetail", resultData); //AN: #ChangeSeat
					if (resultData) {
						var groups = resultData.groups;
						var reRole = "";
						var wwRole = "";
						var resGroup;
						var resGroupRead = "";
						var potRole = "";
						var engRole = ""; //AN: #inquire
						var foremanRole = "";
						var isROC, isPOT, isENG, isForeman;
						var sGroup = ""; //AN: #pw
						var isALS,
							isWW,
							isRE;
						var hasCanadaRole = false,
							hasUSRole = false;
						//SH: Location hierarchy changes	
						var isIOPAdmin = false;
						dashBoardModel.setProperty("/isWebReadOnlyRole", false); //SH: WebReadOnly
						var moduleReadOnly = { //SH: Modules readonly
							"isAlarmReadOnly": false,
							"isTaskSchedulerReadOnly": false,
							"isDOPReadOnly": false,
							"isFracReadOnly": false,
							"isProveReadOnly": false,
							"isDowntimeReadOnly": false,
							"isAnalyticsReadOnly": false,
							"isLocationHistoryReadOnly": false,
							"isPWHopperReadOnly": false,
							"isOBXReadOnly": false,
							"isWorkbenchReadOnly": false
						};
						for (var i = 0; i < groups.length; i++) {
							if (groups[i].value === "IOP_WEB_READONLY") { //SH: WebReadOnly
								dashBoardModel.setProperty("/isWebReadOnlyRole", false);
								// if (resultData.addresses[0].country === "US") {
								// 	hasUSRole = true;
								// } else {
								// 	hasCanadaRole = true;
								// }
							}
							//SH: Modules readonly
							//ST: Remove read only condition for test
							// if (groups[i].value === "IOP_WEB_ALARMS_READONLY") {
							// 	moduleReadOnly.isAlarmReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_TASKSCHEDULER_READONLY") {
							// 	moduleReadOnly.isTaskSchedulerReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_DOP_READONLY") {
							// 	moduleReadOnly.isDOPReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_FRAC_READONLY") {
							// 	moduleReadOnly.isFracReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_PROVE_READONLY") {
							// 	moduleReadOnly.isProveReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_DOWNTIME_READONLY") {
							// 	moduleReadOnly.isDowntimeReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_ANALYTICS_READONLY") {
							// 	moduleReadOnly.isAnalyticsReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_LOCATIONHISTORY_READONLY") {
							// 	moduleReadOnly.isLocationHistoryReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_PWHOPPER_READONLY") {
							// 	moduleReadOnly.isPWHopperReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_OBX_READONLY") {
							// 	moduleReadOnly.isOBXReadOnly = true;
							// }
							// if (groups[i].value === "IOP_WEB_WORKBENCH_READONLY") {
							// 	moduleReadOnly.isWorkbenchReadOnly = true;
							// }

							if (groups[i].value === "IOP_ADMIN"||groups[i].value==="IMO_USER") { //check if IOPAdmin-OBXEngine
								isIOPAdmin = true;
							}
							if (groups[i].value === "IOP_FOREMAN") { //check if Foreman
								isForeman = true;
								if (foremanRole) {
									foremanRole = foremanRole + ",";
								}
								foremanRole += groups[i].value;
								if (resultData.addresses && resultData.addresses[0].country === "US") {
									hasUSRole = true;
								} else {
									hasCanadaRole = true;
								}
							} else if (!isForeman) {
								isForeman = false;
							}
							if (groups[i].value === "IOP_TM_ROC_Catarina" || groups[i].value === "IOP_TM_ROC_WestTilden" || groups[i].value ===
								"IOP_TM_ROC_CentralTilden" || groups[i].value === "IOP_TM_ROC_Karnes" || groups[i].value === "IOP_TM_ROC_Montney" || groups[
									i].value === "IOP_TM_ROC_Kaybob"||groups[i].value==="IMO_USER") { //SH: Canada Roles changes //ST:Adding IMO role
								resGroup = groups[i].value;
								sGroup = resGroup; //AN: #pw
								isROC = true;
							}
							if (groups[i].value.search("IOP_TM_ROC") >= 0) {
								if (resGroupRead) {
									resGroupRead = resGroupRead + ",";
								}
								resGroupRead += groups[i].value;
							}
							if (groups[i].value.search("IOP_POT") >= 0) {
								if (potRole) {
									potRole = potRole + ",";
								}
								potRole += groups[i].value;
								sGroup = potRole; //AN: #pw
								isPOT = true;
								moduleReadOnly.isProveReadOnly = false;
								if (resultData.addresses && resultData.addresses[0].country === "US") { //AN: hasUSRole
									hasUSRole = true;
								} else {
									hasCanadaRole = true;
								}
							} else if (!isPOT) {
								isPOT = false;
							}
							if (groups[i].value.search("IOP_Engineer") >= 0) {
								if (engRole) { //AN: #inquire
									engRole = engRole + ",";
								}
								engRole += groups[i].value; //AN: #inquire
								sGroup = engRole; //AN: #pw
								isENG = true;
								isWW = true;
								isRE = true;
								isALS = true;
								if (resultData.addresses && resultData.addresses[0].country === "US") { //AN: hasUSRole
									hasUSRole = true;
								} else {
									hasCanadaRole = true;
								}
							} else if (!isENG) {
								isENG = false;
								isWW = false;
								isRE = false;
								isALS = false;
							}
						}
						dashBoardModel.setProperty("/moduleReadOnly", moduleReadOnly);
						if (!isROC) {
							isROC = false;
						}
						var oIOPRoles = "";
						for (var j = 0; j < groups.length; j++) {

							if (groups[j].value === "IOP_DC_ROC_Catarina") {
								oIOPRoles = oIOPRoles + "ROC_Catarina" + ",";
								hasUSRole = true; //SH: Location hierarchy changes
							}
							if (groups[j].value === "IOP_DC_ROC_WestTilden") {
								oIOPRoles = oIOPRoles + "ROC_WestTilden" + ",";
								hasUSRole = true;
							}
							if (groups[j].value === "IOP_DC_ROC_CentralTilden") {
								oIOPRoles = oIOPRoles + "ROC_CentralTilden" + ",";
								hasUSRole = true;
							}
							if (groups[j].value === "IOP_DC_ROC_Karnes") {
								oIOPRoles = oIOPRoles + "ROC_Karnes" + ",";
								hasUSRole = true;
							}
							//SH: Canada Roles changes
							if (groups[j].value === "IOP_DC_ROC_Montney") {
								oIOPRoles = oIOPRoles + "ROC_Montney" + ",";
								hasCanadaRole = true;
							}
							if (groups[j].value === "IOP_DC_ROC_Kaybob") {
								oIOPRoles = oIOPRoles + "ROC_Kaybob" + ",";
								hasCanadaRole = true;
							}
							var oMultipleRoles = false;
							var oRolesStr = "";
							if (oIOPRoles === undefined) {
								oRolesStr = "";
								oMultipleRoles = true;
							} else {
								var oRolesLen = oIOPRoles.length;
								oRolesStr = oIOPRoles.slice(0, oRolesLen - 1);
								if (oRolesStr.indexOf(",") !== -1) {
									oMultipleRoles = true;
								}
							}

						}
						var oData;
						if (!oMultipleRoles) {
							oData = {
								"userId": resultData.emails["0"].value,
								"displayName": resultData.name.givenName + " " + resultData.name.familyName,
								"group": resGroup,
								"resGroupRead": resGroupRead,
								"businessRole": oRolesStr,
								"singleRole": true,
								"potRole": potRole,
								"isROC": isROC,
								"isPOT": isPOT,
								"isENG": isENG,
								"isALS": isALS,
								"isWW": isWW,
								"isRE": isRE,
								"isForeman": isForeman,
								"reRole": reRole,
								"wwRole": wwRole,
								"engRole": engRole, //AN: #inquire
								"sGroup": sGroup, //AN: #inquire
								"isIOPAdmin": isIOPAdmin,
								"foremanRole": foremanRole,
								"hasUSRole": hasUSRole, //SH: Location hierarchy changes
								"hasCanadaRole": hasCanadaRole //SH: Location hierarchy changes
							};
							if (oRolesStr === "") {
								oData.singleRole = false;
							}
						} else {
							oData = {
								"userId": resultData.emails["0"].value,
								"displayName": resultData.name.givenName + " " + resultData.name.familyName,
								"group": resGroup,
								"resGroupRead": resGroupRead,
								"businessRole": oRolesStr,
								"singleRole": false,
								"potRole": potRole,
								"isROC": isROC,
								"isPOT": isPOT,
								"isENG": isENG,
								"isALS": isALS,
								"isWW": isWW,
								"isRE": isRE,
								"isForeman": isForeman,
								"reRole": reRole,
								"wwRole": wwRole,
								"engRole": engRole, //AN: #inquire
								"sGroup": sGroup, //AN: #inquire
								"isIOPAdmin": isIOPAdmin,
								"foremanRole": foremanRole,
								"hasUSRole": hasUSRole, //SH: Location hierarchy changes
								"hasCanadaRole": hasCanadaRole //SH: Location hierarchy changes
							};
						}
						dashBoardModel.setProperty("/userData", oData);
						ChangeSeatHelper.setOrGetIOPUserRoles(that, that.getModel("dashBoardModel")); //AN: #ChangeSeat
					}
				} else {
					sap.m.MessageToast.show("Error in Retrieving User Details");
				}
			});
			oModel.attachRequestFailed(function (oEvent) {
				sap.m.MessageToast.show("Error in Retrieving User Details");
			});
		},

		_fileReaderFn: function () {
			if (FileReader.prototype.readAsBinaryString === undefined) {
				FileReader.prototype.readAsBinaryString = function (fileData) {
					var binary = "";
					var pt = this;
					var reader = new FileReader();
					reader.onload = function (e) {
						var bytes = new Uint8Array(reader.result);
						var length = bytes.byteLength;
						for (var i = 0; i < length; i++) {
							binary += String.fromCharCode(bytes[i]);
						}
						pt.content = binary;
						pt.onload();
					};
					reader.readAsArrayBuffer(fileData);
				};
			}
		},
		_setTableUpdateFinishedListeners: function () { //AN: #obxEngine
			var obxsuggestionsTableElement = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "obxsuggestions");
			var obxOperatorAllocationTableElement = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXOperatorAllocationTable");
			var obxOperatorsTableElement = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXOperatorsTable");

			var oLocHistoryTaskTable = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "locHistoryTaskTable");
			var oLocHistoryDowntimeTable = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "locHistoryDowntimeTable");
			var oLocHistoryWorkOrderTable = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "locHistoryWorkorderTable");
			var oLocHistoryPTWTable = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "idPtwTable");
			var oLocHistoryBypassTable = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "locHistoryBypassLogTable");
			var oLocHistoryEnergyIsolationTable = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"),
				"locHistoryEnergyIsolationTable");

			var oLocHistoryJSATable = sap.ui.core.Fragment.byId(this.createId("ptwFragment"),
				"permitToworkJsaTable");
			var oLocHistoryPermitTable = sap.ui.core.Fragment.byId(this.createId("ptwFragment"),
				"permitToWorkPermitTable");
			var oProveTableElement = sap.ui.core.Fragment.byId(this.createId("ndtpvFragmane"), "idSpotfireList");
			var oALaramTable = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList");
			var oFracTable = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "fracMonitoringTableView");
			oLocHistoryJSATable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oLocHistoryPermitTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));

			obxOperatorAllocationTableElement.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			obxsuggestionsTableElement.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			obxOperatorsTableElement.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));

			oLocHistoryTaskTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oLocHistoryDowntimeTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oLocHistoryWorkOrderTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oLocHistoryPTWTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oLocHistoryBypassTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oLocHistoryEnergyIsolationTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oProveTableElement.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oALaramTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
			oFracTable.attachUpdateFinished(function () {
				if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
					this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
				}
			}.bind(this));
		},
		_setScreenHeights: function () {
			var oSize = {
				width: sap.ui.Device.resize.width,
				height: (sap.ui.Device.resize.height - 120) + "px",
				taskPanelHeight: (sap.ui.Device.resize.height - 145) + "px",
				taskCommentHeight: (sap.ui.Device.resize.height - 342) + "px",
				taskAttachmentHeight: (sap.ui.Device.resize.height - 337) + "px",
				taskDetailPanelHeight: (sap.ui.Device.resize.height - 72) + "px",
				taskDetailHeight: (sap.ui.Device.resize.height - 72) + "px",
				detailReportHeight: (sap.ui.Device.resize.height - 350) + "px",
				taskReportHeight: (sap.ui.Device.resize.height - 470) + "px",
				alarmListHeight: (sap.ui.Device.resize.height - 195) + "px",
				downtimeTableHeight: (sap.ui.Device.resize.height - 190) + "px",
				ptwTableHeight: (sap.ui.Device.resize.height - 240) + "px",
				taskDetailLeftHeight: (sap.ui.Device.resize.height - 92) + "px",
				potPanelHeight: (sap.ui.Device.resize.height - 125) + "px",
				potTableHeight: (sap.ui.Device.resize.height - 323) + "px",
				potInvHistHeight: (sap.ui.Device.resize.height - 125) + "px",
				priorityTableHeight: (sap.ui.Device.resize.height - 185) + "px",
				downtimeWrapperScroll: (sap.ui.Device.resize.height - 100) + "px",
				taskScheduleHeight: (sap.ui.Device.resize.height - 155) + "px",
				activityLogHeight: (sap.ui.Device.resize.height - 170) + "px",
				powerBIWidth: sap.ui.Device.resize.width - 50 + "px",
				powerBIheight: sap.ui.Device.resize.height - 110 + "px",
				spotFireTableHeight: (sap.ui.Device.resize.height - 155) + "px",
				taskScheduleV2Height: (sap.ui.Device.resize.height - 175) + "px"
			};
			this.getModel("dashBoardModel").setProperty("/screenSize", oSize);
		},
		_setTaskPanelHeights: function () { //AN: #scrollHeight
			var oSize = {
				taskPanelScrollHeight: "auto"
			};
			this.getModel("dashBoardModel").setProperty("/taskPanelSize", oSize);
			if (this.getModel("dashBoardModel").getProperty("/isTaskPanelOpen")) {
				this.setTaskPanelSizes(this.getModel("dashBoardModel").getProperty("/isTaskPanelOpen"));
			}
		},
		_setModuleWiseScrollHeights: function () { //AN: #obxEngine
			var oSize = {
				obxUsersScrollHeight: "auto",
				obxRecommendedTaskListScrollHeight: "auto",
				obxEngineScrollHeight: "auto",
				proveScrollHeight: "auto"
			};
			this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize", oSize);
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) {
				this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
			}
		},
		onTaskRefreshPress: function () {
			TaskManagementHelper._bindRightTaskPanelModel(this, this.getModel("dashBoardModel").getProperty("/selectedTaskTab")); //AN: #ScratchFilter
		},

		_initilizeLocalModelForDownTime: function (oModel) {
			oModel.setProperty("/downTimeTable", {});
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				pattern: "yyyy-MM-dd"
			});
			var hirarchyContext = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
			var dateFormatted = dateFormat.format(new Date());
			oModel.setProperty("/downTime", {
				date: dateFormatted,
				downtimeCodeVisible: false,
				isDateValid: true,
				wellName: "",
				isReview: false,
				muwi: null
			});
			oModel.setProperty("/downtimeSubTabKey", "Designated");
			var sMuid;
			if (hirarchyContext.currentSelectedObject.length === 1) {
				sMuid = hirarchyContext.currentSelectedObject[0].muwi;
			} else {
				sMuid = undefined;
			}
			/* Rashmi 22-10-2018 Downtime Changes Start*/
			oModel.setProperty("/compDownTime", {
				date: dateFormatted,
				downtimeCodeVisible: false,
				isDateValid: true,
				locationText: "",
				locationType: "",
				locationCode: "",
				compName: "",
				compCode: "",
				isReview: false,
				muwi: null,
				isUpdated: false
			});
			oModel.setProperty("/compressorDowntimePanelExpanded", false);
			oModel.setProperty("/flareDownTime", {
				date: dateFormatted,
				downtimeCodeVisible: false,
				isDateValid: true,
				meterText: "",
				meterType: "",
				meterCode: "",
				flareText: "",
				flareCode: "",
				isReview: false,
				muwi: null,
				isUpdated: false
			});
			oModel.setProperty("/flareDowntimePanelExpanded", false);
			oModel.setProperty("/compDowntimeSubTabKey", "Submitted");
			oModel.setProperty("/flareDowntimeSubTabKey", "Submitted");
			oModel.setProperty("/downtimeCaptureTabKey", "Well");
		},

		onReviewFwdPress: function (oEvent) {
			var expanded = this.getModel("dashBoardModel").getProperty("/downtimePanelExpanded");
			var currentObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oGenModel = this.getModel("dashBoardModel");
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				// pattern: "YYYY-MM-dd"
				pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
			});
			var dateFormatted;
			var systemTime = new Date();
			dateFormatted = dateFormat.format(systemTime);
			oGenModel.setProperty("/downTime/hourkey", "24");
			oGenModel.setProperty("/downTime/minuteKey", "0");
			oGenModel.setProperty("/downTime/wellName", currentObj.well);
			oGenModel.setProperty("/downTime/downtimeParentKey", currentObj.downtimeCode);
			oGenModel.setProperty("/downTime/date", dateFormatted);
			oGenModel.setProperty("/downTime/isReview", true);
			oGenModel.setProperty("/downTime/muwi", currentObj.muwi);
			oGenModel.setProperty("/downTime/downtimeParentText", currentObj.downtimeText);
			oGenModel.setProperty("/downTime/downtimeChildText", currentObj.childText);
			this.onDownTimeParentSelect(currentObj.downtimeCode + "_" + currentObj.childCode);
			if (!expanded) {
				this.getModel("dashBoardModel").setProperty("/downtimePanelExpanded", true);
			}
		},

		onDTDesHourKeySelect: function (oEvent) {
			var key = oEvent.getSource().getSelectedKey();
			if (key === "24" || key === 24) {
				this.getModel("dashBoardModel").setProperty("/downTime/minuteKey", "0");
			}
			this.onDesignateParamsChange();
		},

		onDowntimeTableIconTabbarSelect: function (oEvent) {
			var currentSelection;
			if (typeof (oEvent) === "string") {
				currentSelection = oEvent;
			} else {
				currentSelection = oEvent.getParameter("key");
			}
			var sLocationType = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			var oSelectedDtos = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (oSelectedDtos.length > 0) {
				switch (currentSelection) {

				case "Designated":
					this.setDataForDownTimeTable("/taskmanagementRest/downtimeCapture/getDowntime", "DESIGNATED", oSelectedDtos, sLocationType);
					break;
				case "Submitted":
					this.setDataForDownTimeTable("/taskmanagementRest/downtimeCapture/getDowntime", "SUBMITTED", oSelectedDtos, sLocationType);
					break;
				case "Review":
					this.setDataForDownTimeTable("/taskmanagementRest/downtimeCapture/getDowntime", "REVIEW", oSelectedDtos, sLocationType);
					break;
				}
			} else {
				this.getModel("dashBoardModel").setProperty("/downTimeTable/tableData", []);
			}
		},

		setDataForDownTimeTable: function (url, sType, dto, sLocType) {
			if (sLocType === "SEARCH") {
				sLocType = "Well";
			}
			var country = this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy"); //SH: Downtime Country Code
			var timeZone = new Date().toString().split("(")[1].split(")")[0];
			var userTimezone = "";
			if (timeZone.toUpperCase() === "PACIFIC STANDARD TIME") {
				userTimezone = "PST";
			} else if (timeZone.toUpperCase() === "MOUNTAIN STANDARD TIME") {
				userTimezone = "MST";
			} else if (timeZone.toUpperCase() === "CENTRAL STANDARD TIME") {
				userTimezone = "CST";
			}
			var oPayload = {
				"countryCode": country,
				"statusType": sType,
				"locationType": sLocType,
				"locationHierarchy": dto,
				"userTimeZone": userTimezone
			};
			this.getModel("dashBoardModel").setProperty("/busyIndicators/downtimeTable", true);
			this.doAjax(url, "POST", oPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/downtimeTable", false);
					this.getModel("dashBoardModel").setProperty("/downTimeTable/tableData", []);
					if (oData.message && oData.message.statusCode === "0") {
						var tData = oData.dtoList;
						for (var i = 0; i < tData.length; i++) {
							tData[i].duration = "";
							tData[i].rcTime = "";
							if (tData[i].durationByRocHour) {
								tData[i].duration = tData[i].durationByRocHour + " hr ";
							}
							if (tData[i].durationByRocMinute) {
								tData[i].duration += tData[i].durationByRocMinute + " min";
							}
							if (tData[i].durationByCygnateHours) {
								tData[i].rcTime = tData[i].durationByCygnateHours + " hr ";
							}
							if (tData[i].durationByCygnateMinute) {
								tData[i].rcTime += tData[i].durationByCygnateMinute + " min";
							}
							tData[i].downtimeCode = tData[i].downtimeCode.toString();
							if (tData[i].childCode) { //SH: Downtime Country Code- no child code for Canada
								tData[i].childCode = tData[i].childCode.toString();
							}
						}
						this.getModel("dashBoardModel").setProperty("/downTimeTable/tableData", tData);
					} else {
						//	this._createConfirmationMessage("Error", oData.message.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/downtimeTable", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		_getParentCodesforDownTime: function () {
			/*	var sToken = this._fetchToken("/bpmrulesRest/rules-service/v1/rules/xsrf-token");

				var sUrl = "/bpmrulesRest/rules-service/rest/v1/rule-services/java/Integrated_Operations_Platform_Rules/getParentCodeAndName";
				var oPayload = {
					"__type__": "DTC_ParentCodeRule_InputDto",
					"input": "All"
				};
				$.ajax({
					url: sUrl,
					method: "POST",
					contentType: "application/json;charset=utf-8",
					async: true,
					data: JSON.stringify(oPayload),
					headers: {
						"X-CSRF-Token": sToken
					},
					success: function (result, xhr, data) {

						this.getModel("dashBoardModel").setProperty("/downtimeParentCodes", result);
					}.bind(this),
					error: function (error) {}
				});*/
			var country = this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy"); //SH: Get current location from hierarchy
			var sUrl = "/taskmanagementRest/downtimeCapture/getActiveParenCodeForWellDowntime?country=" + country; //SH: Downtime Country Code
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (oData) {
						this.getModel("dashBoardModel").setProperty("/downtimeParentCodes", oData.downtimeWellParentCodeDtoList);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));

		},

		onDowntimeChildSelect: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/downTime/downTimeChildValue", oEvent.getSource().getSelectedItem().getText());
			this.onDesignateParamsChange();
		},

		onDownTimeParentSelect: function (oEvent) {
			/*var sToken = this._fetchToken("/bpmrulesRest/rules-service/v1/rules/xsrf-token");
			var sUrl = "/bpmrulesRest/rules-service/rest/v1/rule-services/java/Integrated_Operations_Platform_Rules/getChildCodeAndName";
			var oPayload;
			if (typeof (oEvent) === "string") {
				var parentCode = oEvent.split("_")[0];
				var childCodeForReview = oEvent.split("_")[1];
				oPayload = {
					"__type__": "DTC_ChildCodeRule_InputDto",
					"parentCode": parentCode
				};
			} else {
				this.getModel("dashBoardModel").setProperty("/downTime/downTimeParentValue", oEvent.getSource().getSelectedItem().getText());
				oPayload = {
					"__type__": "DTC_ChildCodeRule_InputDto",
					"parentCode": oEvent.getSource().getSelectedKey().replace(/\s/g, '')
				};
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/downTimeChild", true);
			$.ajax({
				url: sUrl,
				method: "POST",
				contentType: "application/json;charset=utf-8",
				async: true,
				data: JSON.stringify(oPayload),
				headers: {
					"X-CSRF-Token": sToken
				},
				success: function (result, xhr, data) {

					if (!result.length) {
						result = [result];
					}
					this.getModel("dashBoardModel").setProperty("/busyIndicators/downTimeChild", false);
					this.getModel("dashBoardModel").setProperty("/downtimeChildCodes", result);
					this.getModel("dashBoardModel").setProperty("/downTime/downtimeChildKey", childCodeForReview);
				}.bind(this),
				error: function (error) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/downTimeChild", true);
				}.bind(this)
			});*/

			if (typeof (oEvent) === "string") {
				var parentCode = oEvent.split("_")[0];
				var childCodeForReview = oEvent.split("_")[1];
			} else {
				var parentCode = oEvent.getSource().getSelectedKey().replace(/\s/g, '');
				this.getModel("dashBoardModel").setProperty("/downTime/downTimeParentValue", oEvent.getSource().getSelectedItem().getText());
			}
			//	var parentCode = oEvent.getSource().getSelectedKey().replace(/\s/g, '');
			if (this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy") !== "CA") { //SH: Downtime Country Code - no child code for Canada
				var sUrl = "/taskmanagementRest/downtimeCapture/getActiveChildCodeForWellDowntimeByParentCode?parentCode=" + parentCode;
				this.doAjax(sUrl, "GET", null, function (oData) {
						if (oData) {
							this.getModel("dashBoardModel").setProperty("/busyIndicators/downTimeChild", false);
							this.getModel("dashBoardModel").setProperty("/downtimeChildCodes", oData.downtimeWellChildCodeDtoList);
							this.getModel("dashBoardModel").setProperty("/downTime/downtimeChildKey", childCodeForReview);
						}
					}.bind(this),
					function (oError) {
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			}
			this.onDesignateParamsChange();

		},

		_fetchToken: function (oUrl) {
			var token;
			$.ajax({
				url: oUrl,
				method: "GET",
				async: false,
				headers: {
					"X-CSRF-Token": "Fetch"
				},
				success: function (result, xhr, data) {

					token = data.getResponseHeader("X-CSRF-Token");
				},
				error: function (result, xhr, data) {
					this._createConfirmationMessage("Error", data, "Error", "", "Close", false, null);

				}.bind(this)
			});
			return token;
		},

		clearDownTime: function () {
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				// pattern: "YYYY-MM-dd"
				pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
			});
			var dateFormatted = dateFormat.format(new Date());
			this.getModel("dashBoardModel").setProperty("/downTime", {
				dateUnFormatted: "",
				date: dateFormatted,
				downtimeCodeVisible: false,
				isDateValid: true,
				muwi: null,
				wellName: this.getModel("dashBoardModel").getProperty(
					"/hierarchyDetails/currentSelectedObject/0/locationText"),
				isReview: false
			});
			this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", false);
		},

		onDowntimeUpdate: function () {
			this.onDownTimeSavePress("", false, true);
		},

		onDownTimeSavePress: function (oEvent, isDesignateCreate, isUpdate, dontSave) {
			var isProcountUpdate = true;
			var oDownTimeData = this.getModel("dashBoardModel").getProperty("/downTime"),
				sUrl = "/taskmanagementRest/downtimeCapture/create",
				hirarchyContext = this.getModel("dashBoardModel").getProperty("/hierarchyDetails"),
				locationTest, mandatoryCheck, bdateValid, bnonZeroHrs;
			locationTest = true;
			mandatoryCheck = true;
			bdateValid = true;
			bnonZeroHrs = true;
			var isDesignateFromResolvedTask = this.getModel("dashBoardModel").getProperty("/isDesignateCreateTask");
			var countryCode = this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy");
			if (isUpdate) {
				sUrl = "/taskmanagementRest/downtimeCapture/update";
			}
			if (!this.getModel("dashBoardModel").getProperty("/downTime/isDateValid")) {
				bdateValid = false;
				this._showToastMessage("Please Enter Correct Date");
			}
			if (!isDesignateCreate && !isUpdate) {
				if (hirarchyContext.currentSelectedObject.length <= 1) {
					locationTest = true;
					if (!hirarchyContext.currentSelectedObject[0] || hirarchyContext.currentSelectedObject[0].location === undefined ||
						hirarchyContext.currentSelectedObject[0].location === null ||
						(hirarchyContext.currentLocationType !== "Well" && hirarchyContext.currentLocationType !== "SEARCH")) {

						if (this.getModel("dashBoardModel").getProperty("/downTime/wellName") !== "" && this.getModel("dashBoardModel").getProperty(
								"/downTime/wellName") !== null && this.getModel("dashBoardModel").getProperty("/downTime/wellName") !== undefined) {
							locationTest = true;
						} else {
							if (this.getModel("dashBoardModel").getProperty("/downtimeFromTaskCreate")) {
								locationTest = true;
							} else {
								locationTest = false;
								this._showToastMessage("Please Select a Well from Hierarchy Panel");
							}
						}
					} else {
						locationTest = true;
					}
				} else {
					if (oDownTimeData.isReview) {
						locationTest = true;
					} else {
						if (this.getModel("dashBoardModel").getProperty("/downtimeFromTaskCreate")) {
							locationTest = true;
						} else {
							locationTest = false;
							this._showToastMessage("Downtime Creation Possible on a Single Item");
						}

					}
				}
				if (oDownTimeData.date === "" || oDownTimeData.date === undefined || oDownTimeData.hourkey === undefined || oDownTimeData.hourkey ===
					"" || oDownTimeData.hourkey === null || oDownTimeData.downtimeParentKey === undefined) {
					mandatoryCheck = false;
					if (oDownTimeData.minuteKey === undefined || oDownTimeData.minuteKey === "" || oDownTimeData.minuteKey === null) {
						oDownTimeData.minuteKey = 0;
					}
					this._showToastMessage("Please fill all the required fields");
				} else if (oDownTimeData.downtimeChildKey === undefined && countryCode === "EFS") { //SH: Downtime Country Code - No child code for Canada
					mandatoryCheck = false;
					this._showToastMessage("Please fill all the required fields");
				} else {
					mandatoryCheck = true;
				}
			} else {
				if (oDownTimeData.date === "" || oDownTimeData.date === undefined || oDownTimeData.downtimeParentKey === undefined) {
					mandatoryCheck = false;
					this._showToastMessage("Please fill all the required fields");
				} else if (oDownTimeData.downtimeChildKey === undefined && countryCode === "EFS") { //SH: Downtime Country Code - No child code for Canada
					mandatoryCheck = false;
					this._showToastMessage("Please fill all the required fields");
				} else {
					mandatoryCheck = true;
				}
			}
			var sChildValue = this.getModel("dashBoardModel").getProperty("/downTime/downTimeChildValue");
			var sParentValue = this.getModel("dashBoardModel").getProperty("/downTime/downTimeParentValue");
			if (sChildValue === undefined) {
				sChildValue =
					this.getModel("dashBoardModel").getProperty("/downTime/downtimeChildText");
			}
			if (sParentValue === undefined) {
				sParentValue =
					this.getModel("dashBoardModel").getProperty("/downTime/downtimeParentText");
			}
			if (mandatoryCheck && locationTest && bdateValid && bnonZeroHrs) {
				var oDate = new Date();
				var time = oDate.getHours() + ":" + oDate.getMinutes() + ":" + oDate.getSeconds();
				//var time= (oDate.getTime() - oDate.getMilliseconds())/1000;
				var sCreatedAt = oDownTimeData.date + " " + time;
				var oCreatedAtDate = new Date(sCreatedAt);
				var CreatedAtDateEpoch = oCreatedAtDate.getTime();
				var time = oDate.getTime();
				var oPayload = {};
				var oLoggedInUserId = this.getModel("dashBoardModel").getProperty("/userData/userId");
				if (isDesignateCreate) {
					isProcountUpdate = false;
				}
				if (isDesignateFromResolvedTask) {
					oDownTimeData.muwi = this.getModel("dashBoardModel").getProperty("/resolvedTaskMuwi");
				}
				oPayload = {
					"isProCountUpdate": isProcountUpdate,
					"countryCode": countryCode,
					"dto": {
						"muwi": oDownTimeData.muwi !== null ? oDownTimeData.muwi : hirarchyContext.currentSelectedObject[0].muwi,
						"durationByRocHour": parseInt(oDownTimeData.hourkey, 10),
						"durationByRocMinute": parseInt(oDownTimeData.minuteKey, 10),
						"downtimeCode": parseInt(oDownTimeData.downtimeParentKey, 10),
						"childCode": parseInt(oDownTimeData.downtimeChildKey, 10),
						"createdAt": CreatedAtDateEpoch + "",
						// "createdAt": oDownTimeData.date + " " + time;,
						"childText": sChildValue,
						"downtimeText": sParentValue,
						"createdBy": oLoggedInUserId
					}
				};
				if (isDesignateCreate && !isDesignateFromResolvedTask) {
					oPayload.dto.muwi = this.getModel("dashBoardModel").getProperty("/currAlarmObject/muwi");
					oPayload.dto.pointId = this.getModel("dashBoardModel").getProperty("/currAlarmObject/pointId");
					oPayload.dto.startTime = this.getModel("dashBoardModel").getProperty("/currAlarmObject/timeStamp");
				}
				if (isUpdate) {
					var oCreatedAtDateEpoch = new Date(oDownTimeData.dateUnFormatted);
					var sEpochCreatedAt = oCreatedAtDateEpoch.getTime();
					oPayload = {
						"isProCountUpdate": isProcountUpdate,
						"countryCode": countryCode,
						"dto": {
							"id": oDownTimeData.id,
							"durationByRocHour": parseInt(oDownTimeData.hourkey, 10),
							"durationByRocMinute": parseInt(oDownTimeData.minuteKey, 10),
							"downtimeCode": parseInt(oDownTimeData.downtimeParentKey, 10),
							"childCode": parseInt(oDownTimeData.downtimeChildKey, 10),
							"muwi": oDownTimeData.muwi,
							"createdAt": sEpochCreatedAt + "",
							//"createdAt": oDownTimeData.dateUnFormatted,
							"childText": sChildValue,
							"downtimeText": sParentValue,
							"updatedBy": oLoggedInUserId
						}
					};
				}
				this.getModel("dashBoardModel").setProperty("/busyIndicators/downTime", true);
				if (dontSave !== true) {
					this.doAjax(sUrl, "POST", oPayload, function (oData) {
							this.getModel("dashBoardModel").setProperty("/busyIndicators/downTime", false);
							if (!isDesignateFromResolvedTask) {
								this.clearDownTime();
							}
							var sKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
							this.onSideNavigationItemSelect(sKey);
							if (oDownTimeData.isReview)
								this.getModel("dashBoardModel").setProperty("/downtimePanelExpanded", false);
							if (isDesignateCreate || isUpdate) {
								this.onDesignateClose();
							}
							if (oData.status === "SUCCESS") {
								this._showToastMessage(oData.message);
							} else {
								if (!isDesignateFromResolvedTask) {
									this.clearDownTime();
								}
								this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
							}
						}.bind(this),
						function (oError) {
							if (!isDesignateFromResolvedTask) {
								this.clearDownTime();
							}
							this.getModel("dashBoardModel").setProperty("/busyIndicators/downTime", false);
						}.bind(this));
				}
				return true;
			} else {
				return false;
			}
		},

		_bindLocationSuggestionforNonDispatch: function (sLocType) {
			var sUrl = "/taskmanagementRest/location/getLocation";
			var oInitialDataPayload = {
				"locationType": sLocType,
				"navigate": "",
				"location": ""
			};
			this.doAjax(sUrl, "POST", oInitialDataPayload, function (oData) {
					if (oData.message.statusCode === "0") {
						var isCanada = this.getModel("dashBoardModel").getProperty("/userData/hasCanadaRole");
						var isUS = this.getModel("dashBoardModel").getProperty("/userData/hasUSRole");
						var locHierarchy = oData.dto.locationHierarchy;
						if (isCanada && isUS) {
							this.getModel("dashBoardModel").setProperty("/locationSuggestions", oData.dto.locationHierarchy);
						} else if (!isCanada && isUS) {
							var locForUs = locHierarchy.filter(function (loc) {
								if (loc.location.startsWith("MUR-US")) {
									return loc;
								}
							});
							this.getModel("dashBoardModel").setProperty("/locationSuggestions", locForUs);
						} else if (isCanada && !isUS) {
							var locForCA = locHierarchy.filter(function (loc) {
								if (loc.location.startsWith("MUR-CA")) {
									return loc;
								}
							});
							this.getModel("dashBoardModel").setProperty("/locationSuggestions", locForCA);
						}
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		onLocationSelectionChange: function (oEvent) {
			var sKey = oEvent.getSource().getSelectedKey();
			if (sKey === "BASE") {
				MessageBox.error("Cannot create non-dispatch at Base level");
			} else {
				this._bindLocationSuggestionforNonDispatch(sKey);
			}
		},

		_getCurrentLoggedInUser: function () {
			var userId = this.getModel("dashBoardModel").getProperty("/userData").userId;
			this.getModel("dashBoardModel").setProperty("/loggedInUser", userId);
			this.getModel("dashBoardModel").setProperty("/loggedInUserDisplay", userId);
			return userId;
		},

		_inilizeBusyIndicators: function () {
			this.getModel("dashBoardModel").setProperty("/busyIndicators", {
				rightPanelBusy: false,
				leftPanelBusy: true,
				downTimeChild: false,
				downTime: false,
				ptwTableBusy: false,
				downtimeTable: false,
				taskSchedulingBusy: false,
				FieldUserBsy: false,
				OBXUserListData: false,
			});
		},

		/*	_setFloatingButton: function () {
				var domFloatButton = document.createElement("a");
				var domColloborationIconContainer = document.createElement("div");
				var domStatusContainer = document.createElement("div");

				domFloatButton.setAttribute("class", "float");
				domColloborationIconContainer.setAttribute("class", "colobImage");
				domStatusContainer.setAttribute("class", "statusConatiner");
				domStatusContainer.innerText = "Open MS-Teams";
				domFloatButton.appendChild(domColloborationIconContainer);
				domFloatButton.appendChild(domStatusContainer);
				//this.byId("rocMainFixFlex").getDomRef().children[0].appendChild(domFloatButton);
				window.document.body.appendChild(domFloatButton)
				$(".float").click(function () {
					this._openMsTeams();
				}.bind(this));

			},*/

		_openMsTeams: function () {
			var prox = "https:",
				domain = "//teams.microsoft.com/l/team/";
			var sUrl = prox + domain;
			sap.m.URLHelper.redirect(sUrl, true);
		},
		onAfterLoggedInUserDetails: function (userDetail) {
			//Module Nav - Anand
			var oArgs = this.getModel("dashBoardModel").getProperty("/arguments");
			/*	if (oArgs.module) {
					this.onSideNavigationItemSelect(oArgs.module);
					this.byId("appsNavList").setSelectedKey(oArgs.module);
					return "";
				}*/
			if (oArgs["?query"]) {
				this.checkIfTaskIsActive(oArgs["?query"]); //AN: #EmailNotif
				// 	this.openTaskPanelFromNav(oArgs["?query"].taskId); //AN: #EmailNotif
			}
			//Module Nav
			var userDetails = userDetail;
			var oAppNavList = this.byId("appsNavList");
			if (userDetails.isROC || this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole")) { //SH:WebReadOnly
				oAppNavList = oAppNavList.setSelectedItem(oAppNavList.getItems()[2]);
			} else if ((userDetails.isENG || userDetails.isPOT)) {
				oAppNavList = oAppNavList.setSelectedItem(oAppNavList.getItems()[5]);
			}
			this.onSideNavigationItemSelect(oAppNavList.getSelectedItem().getKey());
		},
		onAfterRendering: function () {
			// this.onTrendsPress(); // for plotly test

			var dashBoardModel = this.getModel("dashBoardModel"); //RV: #webSocket
			/*this._setFloatingButton();*/
			window.oncontextmenu = function () {
				return false; // cancel default menu
			};
			var that = this;
			window.onresize = function (event) {
				that.relayoutScalePlot("idPlotlyDiv1");
				// Plotly.Plots.resize("idPlotlyDiv1");
			};
			this._setTaskPanelHeights(); //AN: #scrollHeight
			this._setModuleWiseScrollHeights(); //AN: #obxEngine
			this._setTaskListHeights(); //SK: #restriction on dispatch fragment height.
			this._setEnergyIsolationFromHeight(); //sk: #Energy isolation form height
			this._setPTWFromHeight(); // Sk: height of the PTW forms fragment
			this._setBypassLogFromHeight(); // RV: height of Bypasslog form fragment
			jQuery.sap.delayedCall(1000, this, function () {
				this._setNotificationPanelHeights(); //RV :#taskpanel Size set
			}.bind(this));
		},

		hirarchyFactory: function (sId, oContext) {
			var locText = "";
			var textControl;
			locText = oContext.getProperty("locationText");
			if (oContext.getProperty("locationType") === "Compressor") {
				textControl = new sap.m.Text({
					text: locText,
					wrapping: true
				}).addStyleClass("iopV2HirTextStyle iopV2HirTextStyleUnderLine iopFontClass compcolor");
			} else if (oContext.getProperty("locationType") === "Meter") {
				textControl = new sap.m.Text({
					text: locText,
					wrapping: true
				}).addStyleClass("iopV2HirTextStyle iopV2HirTextStyleUnderLine iopFontClass flarecolor");
			} else {
				textControl = new sap.m.Text({
					text: locText,
					wrapping: true
				}).addStyleClass("iopV2HirTextStyle iopV2HirTextStyleUnderLine iopFontClass");
			}
			var oUIControl = new com.sap.incture.Incture_IOP.util.Box(this.createId(sId), {}).addStyleClass(
				"iopV2HirBoxNoBorder iopBoxLayoutPaddingClass iopBoxActiveClass iopBoxSelectedClass");
			if (oContext.getProperty("childExist") === "TRUE") {
				oUIControl.addContent(textControl);
				oUIControl.attachPress(this.onHrNavigate, this);
			} else {
				textControl.removeStyleClass("iopV2HirTextStyleUnderLine");
				oUIControl.addContent(textControl);
			}
			return oUIControl;
		},

		_initilizeLocalModelForHirarchy: function (oModel) {
			oModel.setProperty("/hierarchyDetails", {
				currentPageId: "hirPage1",
				page1Data: [],
				page2Data: [],
				page3Data: [],
				currentNavContext: {},
				currentSelectedObject: [],
				currentLocationType: "Field",
				navLocations: [],
				routing: {
					page1ID: "hirPage1",
					page1ListId: "hrList1",
					page2ID: "hirPage2",
					page2ListId: "hrList2",
					page3ID: "hirPage3",
					page3ListId: "hrList3",
					fragmentId: "hierarchyPanel"
				},
				selectall: {
					checked: false
				}
			});
		},

		_bindInitialLeftPanel: function () {
			var sUrl = "/taskmanagementRest/location/getLocation";
			//var sUrl="https://imob0ot37y8l6.hana.ondemand.com/TaskManagement_Rest/imo/location/getLocation";
			var oInitialDataPayload;
			var oHirarchyModel = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
			var isCanada = this.getModel("dashBoardModel").getProperty("/userData/hasCanadaRole");
			var isUS = this.getModel("dashBoardModel").getProperty("/userData/hasUSRole");
			if (this.getModel("dashBoardModel").getProperty("/userData/singleRole")) {
				oInitialDataPayload = {
					"locationType": "Field",
					"navigate": "CHILD",
					"location": "",
					"role": this.getModel("dashBoardModel").getProperty("/userData/businessRole")
				};
				var bussRole = this.getModel("dashBoardModel").getProperty("/userData/businessRole");
				if (bussRole === "ROC_Montney" || bussRole === "ROC_Kaybob") {
					this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "CA"); //SH: Get current location from hierarchy
				} else {
					this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "EFS");
				}
			} else {
				//SH: Location hierarchy changes
				if (isCanada && isUS) {
					oInitialDataPayload = {
						"locationType": "BASE",
						"navigate": "",
						"location": ""
					};
					this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "");
				} else if (!isCanada && isUS) {
					oInitialDataPayload = {
						"locationType": "Base",
						"navigate": "CHILD",
						"location": "MUR-US-EFS"
					};
					this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "EFS");
				} else {
					/*oInitialDataPayload = {
						"locationType": "Base",
						"navigate": "CHILD",
						"location": "MUR-CA"
					};*/
					oInitialDataPayload = {
						"locationType": "Base",
						"navigate": "",
						"location": ""
					};
					this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "CA");
				}
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/leftPanelBusy", true);
			oHirarchyModel.page1Data = [];
			oHirarchyModel.page2Data = [];
			oHirarchyModel.currentClickContext = {};
			this.doAjax(sUrl, "POST", oInitialDataPayload, function (oData) {
					jQuery.sap.delayedCall(1000, this, function () {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/leftPanelBusy", false);
					}.bind(this));
					if (oData.message.statusCode === "0") {

						this.getModel("dashBoardModel").setProperty("/hierarchyDetails/page1Data", oData.dto.locationHierarchy);
						this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentLocationType", oData.dto.locationType);
						this.getModel("dashBoardModel").refresh();

					} else {
						this._navigate();
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/leftPanelBusy", false);
					this._navigate();
				}.bind(this));
		},
		_navigate: function () {
			this.getRouter().navTo("View1", {});
		},

		_clearhirSelectedItem: function () {
			// this.getModel("dashBoardModel").setProperty("/navToLocHis", false); // #RV Loction history navigation changes //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/navToModule", false); //AN: #Notif
			var oHirModel = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
			var oLeftFieldPanelId = this.getView().createId("hierarchyPanel");
			var list;
			this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentSelectedObject", []);
			this.getModel("dashBoardModel").setProperty("/downTime/wellName", "");
			this.getModel("dashBoardModel").setProperty("/compDownTime/compName", "");
			if (oHirModel.currentPageId === oHirModel.routing.page1ID) {
				list = sap.ui.core.Fragment.byId(oLeftFieldPanelId, oHirModel.routing.page1ListId);
				list.removeSelections();
			} else if (oHirModel.currentPageId === oHirModel.routing.page2ID) {
				list = sap.ui.core.Fragment.byId(oLeftFieldPanelId, oHirModel.routing.page2ListId);
				list.removeSelections();
			} else {
				list = sap.ui.core.Fragment.byId(oLeftFieldPanelId, oHirModel.routing.page3ListId);
				list.removeSelections();
			}
		},
		/**
		 * Convenience method for making ALL selection.
		 * @private
		 * @returns null
		 */
		onHirarcheySelectAll: function (oEvent) {
			var aAllitemArray = [];
			var sListId = this.getsListId();
			var oHirarcheyPanelId = this.getView().createId("hierarchyPanel");
			var oCurrentList = sap.ui.core.Fragment.byId(oHirarcheyPanelId, sListId);
			if (oEvent.getParameter("selected")) {
				oCurrentList.selectAllBox();
				for (var i = 0; i < oCurrentList.getBoxes().length; i++) {
					aAllitemArray.push(oCurrentList.getBoxes()[i].getBindingContext("dashBoardModel").getObject());
				}
				this.getModel("dashBoardModel").setProperty("/isSelectedHierarchy", true);
			} else {
				this.getModel("dashBoardModel").setProperty("/isSelectedHierarchy", false);
				this._clearhirSelectedItem();
				//onSideNavigationItemSelect instead on onAppsTabSelect
				this.onSideNavigationItemSelect("external");
				// this.onAppsTabSelect("external");
			}
			this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentSelectedObject", aAllitemArray);
			//onSideNavigationItemSelect instead on onAppsTabSelect
			this.onSideNavigationItemSelect("external");
			// this.onAppsTabSelect("external");
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) { //AN: #obxEngineV2
				this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
			}
		},
		onTokenUpdate: function (oEvent) {
			var location = oEvent.getParameters("removedTokens").removedTokens[0].getProperty("key"); //MS, Get the removed token from the list.
			this.onHrItemSelectionChange(location);
		},
		//MS, Adds the token in the selected Hierarchy
		addToken: function (oEvent) {
			var aHirSelectedArry = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			var HrToken = [];
			var HrTokenFragment = [];
			var i;
			for (i = 0; i < 4; i++) { //MS, 4 Tokens will be displayed on the screen 
				if (aHirSelectedArry[i])
					HrToken[i] = aHirSelectedArry[i];
			}
			this.getModel("dashBoardModel").setProperty("/selectedHrArry", HrToken);
			if (aHirSelectedArry.length > 4) { //MS, After 4 tokens, rest all the tokens will be added to popover
				for (i = 4; i < aHirSelectedArry.length; i++) {
					HrTokenFragment[i - 4] = aHirSelectedArry[i];
				}
				this.getModel("dashBoardModel").setProperty("/showMoreIcon", true);
				this.getModel("dashBoardModel").setProperty("/selectedHrArryMore", HrTokenFragment);
				// this.getModel("dashBoardModel").refresh(true);
			} else {
				this.getModel("dashBoardModel").setProperty("/showMoreIcon", false);
			}
		},
		onOpenSelectedHr: function (oEvent) {
			// this.getModel("dashBoardModel").refresh(true);
			var oFragmentId = "idaddHierarchyList",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.addHierarchyList";
			if (!this.addHierarchyList) {
				this.addHierarchyList = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.addHierarchyList);
			}
			this.addHierarchyList.openBy(oEvent.getSource());
		},
		onHrItemSelectionChange: function (oEvent) {
			var aHirSelectedArry = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			// this.getModel("dashBoardModel").setProperty("/navToLocHis", false); // #RV Loction history navigation changes //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/navToModule", false); //AN: #Notif
			var currentObjectContext;
			if (typeof oEvent === 'string') {
				var sListId = this.getsListId();
				var oHirarcheyPanelId = this.getView().createId("hierarchyPanel");
				var oCurrentList = sap.ui.core.Fragment.byId(oHirarcheyPanelId, sListId);
				oCurrentList = oCurrentList.getBoxes();
				var aNewArry = [];
				oCurrentList.map(function (oItem) {
					if (oItem.getBindingContext("dashBoardModel").getObject().location === oEvent) {
						currentObjectContext = oItem.getBindingContext("dashBoardModel").getObject();
						oItem.setSelected(false);
					}
				});

			} else {
				currentObjectContext = oEvent.getParameter("box").getBindingContext("dashBoardModel").getObject();
			}
			if (currentObjectContext.location.startsWith("MUR-CA")) { //SH: Get current location from hierarchy
				this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "CA");
			} else {
				this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", "EFS");
			}
			if (typeof oEvent !== 'string' && oEvent.getParameter("selected")) {
				aHirSelectedArry.push(currentObjectContext);
				this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentSelectedObject", aHirSelectedArry);

			} else {
				if (this.getModel("dashBoardModel").getProperty("/hierarchyDetails/selectall/checked")) {
					this.getModel("dashBoardModel").setProperty("/hierarchyDetails/selectall/checked", false);
				}
				var aNewArry = [];
				for (var i = 0; i < aHirSelectedArry.length; i++) {
					if (aHirSelectedArry[i].location !== currentObjectContext.location) {
						aNewArry.push(aHirSelectedArry[i]);
					}
				}
				this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentSelectedObject", aNewArry);
			}

			if (this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType") === "Well" || this.getModel(
					"dashBoardModel").getProperty("/hierarchyDetails/currentLocationType") === "SEARCH") { // || this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType") === "Compressor"
				this.getModel("dashBoardModel").setProperty("/downTime/wellName", this.getModel("dashBoardModel").getProperty(
					"/hierarchyDetails/currentSelectedObject/0/locationText"));
			}
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "ndtpv") {
				this.getModel("dashBoardModel").setProperty("/spotFireData/pageNumber", "1");
			}
			/*Rashmi 22-10-2018 Compressor Downtime changes Start*/
			var oCurrentObj = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			var oCurrentLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentNavContext");
			if (oCurrentObj.length === 0 || (oCurrentObj[0].locationType !== "Compressor")) {
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					// pattern: "YYYY-MM-dd"
					pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
				});
				var dateFormatted = dateFormat.format(new Date());
				this.getModel("dashBoardModel").setProperty("/compDownTime", {
					dateUnFormatted: "",
					date: dateFormatted,
					downtimeCodeVisible: false,
					isDateValid: true,
					locationText: "",
					locationType: "",
					locationCode: "",
					compName: "",
					compCode: "",
					isReview: false,
					muwi: null,
					isUpdated: false
				});
				this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", false);
			}
			if (oCurrentObj.length === 1 && oCurrentObj[0].locationType === "Compressor") {
				this.getModel("dashBoardModel").getData().compDownTime.muwi = oCurrentObj[0].muwi;
				this.getModel("dashBoardModel").getData().compDownTime.compName = oCurrentObj[0].locationText;
				this.getModel("dashBoardModel").getData().compDownTime.compCode = oCurrentObj[0].location;
				this.getModel("dashBoardModel").getData().compDownTime.compType = oCurrentObj[0].locationType;
				this.getModel("dashBoardModel").getData().compDownTime.locationText = oCurrentLoc.locationText;
				this.getModel("dashBoardModel").getData().compDownTime.locationCode = oCurrentLoc.location;
				this.getModel("dashBoardModel").getData().compDownTime.locationType = oCurrentLoc.locationType;
				this.getModel("dashBoardModel").getData().compDownTime.id = oCurrentLoc.id;
				/*this.getModel("dashBoardModel").getData().compDownTime.locationText = oCurrentLoc.parentLocationText;
				this.getModel("dashBoardModel").getData().compDownTime.locationCode = oCurrentLoc.parentLocation;
				this.getModel("dashBoardModel").getData().compDownTime.locationType = oCurrentLoc.parentLocationType;*/
			}
			if (oCurrentObj.length === 0 || (oCurrentObj[0].locationType !== "Meter")) {
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					// pattern: "YYYY-MM-dd"
					pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
				});
				var dateFormatted = dateFormat.format(new Date());
				this.getModel("dashBoardModel").setProperty("/flareDownTime", {
					date: dateFormatted,
					downtimeCodeVisible: false,
					isDateValid: true,
					meterText: "",
					meterType: "",
					meterCode: "",
					flareText: "",
					flareCode: "",
					isReview: false,
					muwi: null,
					isUpdated: false
				});
				this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", false);
			}
			if (oCurrentObj.length === 1 && oCurrentObj[0].locationType === "Meter") {
				this.getModel("dashBoardModel").getData().flareDownTime.muwi = oCurrentObj[0].muwi;
				this.getModel("dashBoardModel").getData().flareDownTime.meterText = oCurrentObj[0].locationText;
				this.getModel("dashBoardModel").getData().flareDownTime.meterCode = oCurrentObj[0].location;
				this.getModel("dashBoardModel").getData().flareDownTime.meterType = oCurrentObj[0].locationType;
				this.getModel("dashBoardModel").getData().flareDownTime.locationText = oCurrentLoc.locationText;
				this.getModel("dashBoardModel").getData().flareDownTime.locationCode = oCurrentLoc.location;
				this.getModel("dashBoardModel").getData().flareDownTime.locationType = oCurrentLoc.locationType;
				this.getModel("dashBoardModel").getData().flareDownTime.id = oCurrentLoc.id;
			}
			/*Rashmi 22-10-2018 Compressor Downtime changes End*/
			/*	this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentSelectedObject", oEvent.getParameter("listItem").getBindingContext(
					"dashBoardModel").getObject());*/

			/*
						var hirarchyContext = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
						if (hirarchyContext.currentSelectedObject.length === 1) {

							var sMuid = hirarchyContext.currentSelectedObject[0].muwi,
								oDownTimeData = this.getModel("dashBoardModel").getProperty("/downTime"),
								sDate = oDownTimeData.date !== undefined ? oDownTimeData.date + "T00:00:00" : null;
							this.getModel("dashBoardModel").setProperty("/downTime/downtimeCodeVisible", false);
							if (sMuid && sDate && sDate !== null) {
								this._callforDownTimeCheck(sDate, sMuid);
							}
						}*/
			//onSideNavigationItemSelect instead onAppsTabSelect
			// this.onAppsTabSelect("external");
			var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (selectedLoc.length === 0) {
				this.getModel("dashBoardModel").setProperty("/isSelectedHierarchy", false);
			} else {
				this.getModel("dashBoardModel").setProperty("/isSelectedHierarchy", true);
			}
			this.onSideNavigationItemSelect("external");
			// this.addToken();
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) { //AN: #obxEngineV2
				this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
			}
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "locHistory") {
				var currentTab = this.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
				var monthFilter = this.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/monthFilterPTW");
				this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
				this.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
				this.getPermittoWorkList(currentTab, monthFilter);
			}
		},
		getsListId: function (oEvent) {
			var sCurrentPageId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentPageId"),
				sListId;
			if (sCurrentPageId === "hirPage2") {
				sListId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/routing/page2ListId");
			} else if (sCurrentPageId === "hirPage1") {
				sListId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/routing/page1ListId");
			} else {
				sListId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/routing/page3ListId");
			}
			return sListId;
		},
		onHrNavigate: function (oEvent) {
			var oHirarchyModel = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
			// this.getModel("dashBoardModel").setProperty("/navToLocHis", false); // #RV Loction history navigation changes //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/navToModule", false); //AN: #Notif
			oHirarchyModel.currentNavContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var navLocationArr = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/navLocations");
			navLocationArr.push({
				"loc": oHirarchyModel.currentNavContext.location
			});
			this.getModel("dashBoardModel").setProperty("/hierarchyDetails/navLocations", navLocationArr);
			this._setHirarchyData("CHILD");
		},

		_setHirarchyData: function (navType) {
			this.getModel("dashBoardModel").setProperty("/hierarchyDetails/selectall/checked", false);
			// var isNavtoLocHis = this.getModel("dashBoardModel").getProperty("/navToLocHis"); // #RV Loction history navigation changes //AN: #Notif
			var isNavToModule = this.getModel("dashBoardModel").getProperty("/navToModule"); //AN: #Notif
			var oHirarchyModel = this.getModel("dashBoardModel").getProperty("/hierarchyDetails"),
				sCurrentDataLocation = "",
				sPageId = "",
				currentNavContext = oHirarchyModel.currentNavContext;
			var sUrl = "/taskmanagementRest/location/getLocation";
			var loc = currentNavContext.location;
			if (currentNavContext.locationType == "Field" && navType == "PARENT") {
				loc = "";
				this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", ""); //SH: Get current location from hierarchy
			}
			if (currentNavContext.locationType == "BASE" && navType == "CHILD") {
				var countryCode;
				if (currentNavContext.locationText == "Canada") {
					countryCode = "CA";
				} else {
					countryCode = "EFS";
				}
				this.getModel("dashBoardModel").setProperty("/currentLocationInHierarchy", countryCode); //SH: Get current location from hierarchy
			}
			var oInitialDataPayload = {
				"locationType": currentNavContext.locationType,
				"navigate": navType,
				"location": loc
			};
			if (navType === "SEARCH") {
				this.getModel("dashBoardModel").setProperty(
					"/hierarchyDetails/currentLocationType", "SEARCH");
				oInitialDataPayload = this.getModel("dashBoardModel").getProperty("/searchData");
				this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentPageId", oHirarchyModel.routing.page3ID);
				sCurrentDataLocation = "/page3Data";
			} else {
				if (oHirarchyModel.currentPageId === oHirarchyModel.routing.page1ID) {
					this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentPageId", oHirarchyModel.routing.page2ID);
					sCurrentDataLocation = "/page2Data";
				} else {
					this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentPageId", oHirarchyModel.routing.page1ID);
					sCurrentDataLocation = "/page1Data";
				}
			}
			this._onHirarchyNavTo(oHirarchyModel.currentPageId);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/leftPanelBusy", true);
			this.doAjax(sUrl, "POST", oInitialDataPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/leftPanelBusy", false);
					if (oData.message && oData.message.statusCode === "0") {
						if (navType === "SEARCH" && oData.dto === null) {
							this.getModel("dashBoardModel").setProperty(
								"/hierarchyDetails" + sCurrentDataLocation, {});
							this.getModel("dashBoardModel").refresh();
						} else {
							this.getModel("dashBoardModel").setProperty(
								"/hierarchyDetails" + sCurrentDataLocation, oData.dto.locationHierarchy);
							this.getModel("dashBoardModel").setProperty(
								"/hierarchyDetails/currentLocationType", oData.dto.locationType);
							this.getModel("dashBoardModel").refresh();
							var oPanelModuleSel = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
							// if (oPanelModuleSel === "locHistory" && isNavtoLocHis) { //AN: #Notif
							if ((oPanelModuleSel === "alarms" || oPanelModuleSel === "locHistory" || oPanelModuleSel === "pwhopper") && isNavToModule) { //AN: #Notif
								this.getModel("dashBoardModel").setProperty("/hierarchyDetails/selectall/checked", true); //#RV: Location History Changes	
								this.onHirarcheySelectAllFromLocHistory("selected");
							} else {
								this.getModel("dashBoardModel").setProperty("/hierarchyDetails/selectall/checked", false); //#RV: Location History Changes	
								this.onHirarcheySelectAllFromLocHistory("deselected");
							}
						}
					} else {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/leftPanelBusy", false);
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
			return sPageId;

		},
		onHirarcheySelectAllFromLocHistory: function (oEvent) {
			var aAllitemArray = [];
			var sListId = this.getsListId();
			var oHirarcheyPanelId = this.getView().createId("hierarchyPanel");
			var oCurrentList = sap.ui.core.Fragment.byId(oHirarcheyPanelId, sListId);
			if (oEvent === "selected") { //#RV: Location History Changes
				oCurrentList.selectAllBox();
				for (var i = 0; i < oCurrentList.getBoxes().length; i++) {
					aAllitemArray.push(oCurrentList.getBoxes()[i].getBindingContext("dashBoardModel").getObject());
				}
				this.getModel("dashBoardModel").setProperty("/isSelectedHierarchy", true);
			} else {
				this.getModel("dashBoardModel").setProperty("/isSelectedHierarchy", false);
				this._clearhirSelectedItem();
				//onSideNavigationItemSelect instead on onAppsTabSelect
				this.onSideNavigationItemSelect("external");
				// this.onAppsTabSelect("external");
			}
			this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentSelectedObject", aAllitemArray);
			//onSideNavigationItemSelect instead on onAppsTabSelect
			this.onSideNavigationItemSelect("external");
			// this.onAppsTabSelect("external");
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey")) { //AN: #obxEngineV2
				this._setModuleWiseScrollSizes(this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"));
			}

		},
		_onHirarchyNavTo: function (sPageId) {
			var oLeftFieldPanelId = this.getView().createId("hierarchyPanel");
			var oNavContainer = sap.ui.core.Fragment.byId(oLeftFieldPanelId, "fieldListNavCon");
			var sPage = sap.ui.core.Fragment.byId(oLeftFieldPanelId, sPageId);
			this._clearhirSelectedItem();
			oNavContainer.to(sPage, "slide");
		},

		onFieldListNavBack: function () {
			this.getModel("dashBoardModel").setProperty("/downTime/downtimeCodeVisible", false);
			if (this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType") === "SEARCH") {
				this._clearhirSelectedItem();
				this._initilizeLocalModelForHirarchy(this.getModel("dashBoardModel"));
				this._onHirarchyNavTo(this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentPageId"));
				this._bindInitialLeftPanel();
				this.getModel("dashBoardModel").setProperty("/search", "");
			} else {
				var sCurrentPageId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentPageId"),
					sListId;
				if (sCurrentPageId === "hirPage2") {
					sListId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/routing/page2ListId");
				} else {
					sListId = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/routing/page1ListId");
				}
				var oTaskMgtPanelId = this.getView().createId("hierarchyPanel");
				var oTaskList = sap.ui.core.Fragment.byId(oTaskMgtPanelId, sListId);
				this.getModel("dashBoardModel").setProperty("/hierarchyDetails/currentNavContext", oTaskList.getBoxes()[0].getBindingContext(
					"dashBoardModel").getObject());
				var navLocationArr = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/navLocations");
				navLocationArr.pop();
				this.getModel("dashBoardModel").setProperty("/hierarchyDetails/navLocations", navLocationArr);
				this._setHirarchyData("PARENT");
			}
		},

		onfiledWellSearch: function (oEvent) {
			this._clearhirSelectedItem();
			var oInitialDataPayload = {
				"locationType": "SEARCH",
				"navigate": "",
				"location": ""
			};
			var value = oEvent.getSource().getValue();
			oInitialDataPayload.location = value;
			this.getModel("dashBoardModel").setProperty("/searchData", oInitialDataPayload);
			this._setHirarchyData("SEARCH");
		},

		onTaskListPress: function () {
			jQuery.sap.log.error("FYI: Task List Press");
		},

		/**
		 * Method triggers when task tab changes. 
		 * @public
		 * Method changes the data inside tabs.
		 */
		onTaskTabSelection: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelFilterBusy", true); //AN: #taskFilter
			this.getModel("dashBoardModel").setProperty("/taskList", []);
			var query = oEvent.getSource().getSelectedKey();
			var taskmanagementpane1 = this.getView().createId("taskmanagementpane1"); //AN: #taskFilter
			var idTaskHeaderSegmentedButton = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskHeaderSegmentedButton"); //AN: #taskFilter
			switch (query) {
			case "All":
				TaskManagementHelper._bindRightTaskPanelModel(this, "All"); //AN: #ScratchFilter
				idTaskHeaderSegmentedButton.removeStyleClass("sapUiMediumMarginBottom"); //AN: #taskFilter
				break;
			case "Non-Dispatch":
				TaskManagementHelper._bindRightTaskPanelModel(this, "Non-Dispatch"); //AN: #ScratchFilter
				idTaskHeaderSegmentedButton.addStyleClass("sapUiMediumMarginBottom"); //AN: #taskFilter
				break;
			case "Sent Items":
				TaskManagementHelper._bindRightTaskPanelModel(this, "Sent Items"); //AN: #ScratchFilter
				idTaskHeaderSegmentedButton.removeStyleClass("sapUiMediumMarginBottom"); //AN: #taskFilter
				break;
			}
		},

		addAdditionalTasks: function (location) {
			var sGroup = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				sGroup =
					"IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes,IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
			}
			var loc = location;
			if (!loc) {
				loc = this.getModel("dashBoardModel").getProperty("/selectedLocation");
			}
			var url = "/taskmanagementRest/nonDispatch/readByLocation?group=" + sGroup + "&userType=ROC&location=" + encodeURIComponent(
				loc);
			this.doAjax(url, "GET", null, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					if (oData.responseMessage.statusCode === "0") {
						this.getModel("oAdditionalTaskModel").setData(oData);
						this.getModel("oAdditionalTaskModel").refresh();
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		createTaskPress: function (oEvent) {
			var currSelectedLocation = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (currSelectedLocation.length === 1 && currSelectedLocation[0].locationType === "Compressor") {
				if (oEvent.getSource().getText() !== "Create Task") {
					this._showToastMessage("Non-Dispatch Task cannot be created on a Compressor."); //AN: #ChangeSeat
					return;
				}
				this.getCompressorParent(oEvent.getSource(), currSelectedLocation);
			} else {
				if (oEvent.getSource().getText() === "Add Item") {
					this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
					this.getModel("dashBoardModel").setProperty("/taskCreation", "Custom");
					this.onCreateTaskPress(oEvent.getSource());
				} else {
					this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
					this.getModel("dashBoardModel").setProperty("/taskCreation", "Custom");
					this.TaskCreationHierarchyValidation();
				}
			}
		},

		getCompressorParent: function (oEvt, currSelectedLocation) {
			this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
			this.getModel("dashBoardModel").setProperty("/taskCreation", "Custom");
			var sUrl = "/taskmanagementRest/location/getParentOfCompressor?compressorId=" + currSelectedLocation[0].location;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.message.statusCode === "0") {
					var oTaskTypeDetails = { //AN: #ChangeSeat
						oCurrSelectedLocation: currSelectedLocation,
						oRespData: oData,
						sLocCode: oData.responseDto.location,
						sLocType: oData.responseDto.locationType,
						sLocText: oData.responseDto.locationText
					};
					var aTaskTypeDetails = []; //AN: #ChangeSeat
					aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
					ChangeSeatHelper.checkAuthorization(this, this.getModel("dashBoardModel"), aTaskTypeDetails, "Comp-Dispatch", "ROC"); //AN: #ChangeSeat
				} else {
					var sErrorMessage = oData.responseMessage.message;
					this._showToastMessage(sErrorMessage);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},

		/**
		 * Method triggers on Create task/Add item btn press. 
		 * @public
		 * @param Text of the button
		 * Method opens corresponding fragments to create items.
		 */
		onCreateTaskPress: function (oSource, btnText, alarmCreate, newLocationDto, compressorText) {
			this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", false);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", true);
			var taskId = this.getModel("dashBoardModel").getProperty("/taskId");
			var processId = this.getModel("dashBoardModel").getProperty("/processId");
			var creatorGroupId = this.getModel("dashBoardModel").getProperty("/creatorGroupId"); //AN: #inquire
			var taskOwner = this.getModel("dashBoardModel").getProperty("/taskOwner"); //AN: #inquire
			var dashBoardModel = this.getModel("dashBoardModel");
			var oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
			var oTaskPanelDetailModelData = oTaskPanelDetailModel.getData(); //AN: #inquire
			var currentUser = this._getCurrentLoggedInUser();
			var currSelectedLocation = dashBoardModel.getProperty("/hierarchyDetails/currentSelectedObject");
			var currLocType = dashBoardModel.getProperty("/hierarchyDetails/currentLocationType");
			var loggedInUser = this.getModel("dashBoardModel").getProperty("/userData/userId");
			var hopperuserType = this.getModel("dashBoardModel").getProperty("/userData");

			if (newLocationDto) {
				currSelectedLocation = [newLocationDto];
				currLocType = newLocationDto.locationType;
			}
			if (!btnText) {
				if (oSource) {
					btnText = oSource.getText();
				} else {
					btnText = "Create Task";
				}
				dashBoardModel.setProperty("/isCreateTask", true);
				dashBoardModel.setProperty("/taskId", "");
				dashBoardModel.setProperty("/processId", "");
				dashBoardModel.setProperty("createdByEmailId", ""); //AN: #inquire
				dashBoardModel.setProperty("taskStatus", ""); //AN: #inquire
				if (btnText === "Create Task") {
					if (currSelectedLocation.length === 1 && !alarmCreate) {
						currSelectedLocation = currSelectedLocation[0];
					} else {
						currSelectedLocation = {};
					}
					if (dashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
						var oCurrSelWBMsgData = dashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
						currSelectedLocation.location = oCurrSelWBMsgData.locationCode;
						currSelectedLocation.locationText = oCurrSelWBMsgData.locationText;
						if (oCurrSelWBMsgData.locationType === "Compressor") {
							currSelectedLocation.location = oCurrSelWBMsgData.parentLocationCode;
						}
					}

					if (alarmCreate) {
						if (dashBoardModel.getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
							dashBoardModel.setProperty("/isInquiryCreate", false);
							dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
								userType: "ROC",
								taskType: "Dispatch",
								from: "Inquiry",
								inquiryProcessId: oTaskPanelDetailModelData.taskEventDto.processId,
								inquiryCreatorId: oTaskPanelDetailModelData.taskEventDto.createdBy,
								inquiryLocationCode: oTaskPanelDetailModelData.taskEventDto.locationCode,
								inquiryLocationType: oTaskPanelDetailModelData.taskEventDto.locationType,
								inquiryLocationText: oTaskPanelDetailModelData.taskEventDto.locationText
							});
							this.getModel("dashBoardModel").setProperty("/isDispatch", true);
							dashBoardModel.setProperty("/sLocCodeToGetCurrentNearByUser", oTaskPanelDetailModelData.taskEventDto.locationCode); //AN: #operatorsAvailable
						} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP") { //AN: #inquire
							var currObj = this.getModel("dashBoardModel").getProperty("/currDopObject");
							this.getModel("dashBoardModel").setProperty("/isDispatch", true);
							dashBoardModel.setProperty("/sLocCodeToGetCurrentNearByUser", currObj.locationCode); //AN: #operatorsAvailable
						} else {
							var currObj = this.getModel("dashBoardModel").getProperty("/currAlarmObject");
							this.getModel("dashBoardModel").setProperty("/isDispatch", true);
							dashBoardModel.setProperty("/sLocCodeToGetCurrentNearByUser", currObj.locationCode); //AN: #operatorsAvailable
						}
					} else {
						this.getModel("dashBoardModel").setProperty("/isDispatch", false);
						dashBoardModel.setProperty("/sLocCodeToGetCurrentNearByUser", currSelectedLocation.location); //AN: #operatorsAvailable
					}
				}
			}
			if (dashBoardModel.getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
				dashBoardModel.setProperty("/selectedLocation", dashBoardModel.getProperty("/dynamicContentType/inquiryLocationText"));
			} else {
				dashBoardModel.setProperty("/selectedLocation", currSelectedLocation.locationText);
			}
			if (btnText === "Create Task") {
				var oFragmentId1 = "idCreateTaskPanel",
					oFragmentName1 = "com.sap.incture.Incture_IOP.fragment.createTaskPanel";
				if (this.getModel("dashBoardModel").getProperty("/isInvestigationCreate")) {
					oFragmentId1 = "CreateInvestigationPanel";
					oFragmentName1 = "com.sap.incture.Incture_IOP.fragment.CreateInvestigationPanel";
				} else if (dashBoardModel.getProperty("/isInquiryCreate")) { //AN: #inquire
					oFragmentId1 = "idCreateInquiryPanel";
					oFragmentName1 = "com.sap.incture.Incture_IOP.fragment.createInquiryPanel";
				}
				var sUrl;
				// Create task enhancement
				var sKey;
				// this.getView().getModel("dashBoardModel").getData().createTaskTabKey = "taskInfo";
				var sClassification = this.getModel("dashBoardModel").getData().taskSubject1;
				var sSubClassification = this.getModel("dashBoardModel").getData().taskSubject2;
				var sStatus = this.getModel("dashBoardModel").getData().taskStatus;
				if (sStatus === "RESOLVED" || sStatus === "RETURNED" || sStatus === "IN PROGRESS") {
					this.getModel("dashBoardModel").getData().createTaskPanelVisible = false;
				} else {
					this.getModel("dashBoardModel").getData().createTaskPanelVisible = true;
					var isInquiry = this.getModel("dashBoardModel").getData().isInquiryCreate;
					var isInves = this.getModel("dashBoardModel").getData().isInvestigationCreate;
					if ((!isInquiry && !isInves) && sStatus === "NEW") {
						if (dashBoardModel.getProperty("/taskCreation") === "Custom") { //AN: Custom dispatch creation
							var sCurrLocType = dashBoardModel.getData().hierarchyDetails.currentSelectedObject[0].locationType;
							if (sCurrLocType === "Compressor") {
								sKey = currSelectedLocation.location;
								//	sKey = this.getModel("dashBoardModel").getData().hierarchyDetails.currentNavContext.location;
								var oCurrLocType = this.getModel("dashBoardModel").getData().hierarchyDetails.currentNavContext.locationType;
								currLocType = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject[0].locationType;
								var currLocComp = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject[0];
							} else {
								sKey = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject[0].location;
							}
						} else if (dashBoardModel.getData().dynamicContentType.from === "Inquiry" && dashBoardModel.getData().dynamicContentType.taskType ===
							"Dispatch") { //AN: dispatch creation from inquiry
							sKey = dashBoardModel.getData().dynamicContentType.inquiryLocationCode;
						} else if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "DOP" && !dashBoardModel.getProperty(
								"/taskCreation")) { //AN: dispatch creation from DOP
							sKey = dashBoardModel.getData().currDopObject.locationCode;
						} else if ((dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "fracMonitoring" || dashBoardModel.getProperty(
								"/panelProperties/currentSelectKey") === "alarms") && !dashBoardModel.getProperty("/taskCreation")) { //AN: dispatch creation from Frac or Alarms
							sKey = dashBoardModel.getData().currAlarmObject.locationCode;
						} else if (dashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
							sKey = oCurrSelWBMsgData.locationCode;
							if (oCurrSelWBMsgData.locationType === "Compressor") {
								sKey = oCurrSelWBMsgData.parentLocationCode;
							}
						}
						this.getTaskHistory(sKey);
						//this.getTaskFLSOP(sClassification, sSubClassification);
					}
				}
				if (dashBoardModel.getProperty("/isCreateTask")) {
					if (currLocType === "SEARCH") {
						currLocType = "Well";
					}
					if (dashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
						// currLocType = "Well";
						currLocType = oCurrSelWBMsgData.locationType;
					}
					sUrl = "/taskmanagementRest/tasks/getHeader?taskTempId=123&type=Create";
					if (!alarmCreate) {
						if (currLocType === "Compressor") {
							if (dashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
								sUrl = sUrl + "&locType=" + oCurrSelWBMsgData.parentLocationType + "&locationCode=" + oCurrSelWBMsgData.parentLocationCode +
									"&compressor=" + oCurrSelWBMsgData.locationText;
							} else {
								sUrl = sUrl + "&locType=" + oCurrLocType + "&locationCode=" + currSelectedLocation.location + "&compressor=" + currLocComp.locationText;
							}
						} else {
							sUrl = sUrl + "&locType=" + currLocType + "&locationCode=" + currSelectedLocation.location;
						}
					} else {
						var currAlarmObj;
						if (dashBoardModel.getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
							sUrl = sUrl + "&locationCode=" + dashBoardModel.getProperty("/dynamicContentType/inquiryLocationCode") + "&locType=" +
								dashBoardModel.getProperty("/dynamicContentType/inquiryLocationType");
						} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP") { //AN: #inquire
							var currDopObj = this.getModel("dashBoardModel").getProperty("/currDopObject");
							sUrl = sUrl + "&locationCode=" + currDopObj.locationCode + "&locType=" + currDopObj.locationType;
						} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "fracMonitoring") {
							currAlarmObj = this.getModel("dashBoardModel").getProperty("/currAlarmObject");
							sUrl = sUrl + "&locationCode=" + currAlarmObj.locationCode + "&locType=" + currAlarmObj.locationType;
						} else {
							currAlarmObj = this.getModel("dashBoardModel").getProperty("/currAlarmObject");
							sUrl = sUrl + "&locationCode=" + currAlarmObj.locationCode + "&locType=" + currAlarmObj.locationType +
								"&classification=" + currAlarmObj.downTimeClassifier;
						}
					}
					if (compressorText) {
						if (sUrl.indexOf("&compressor") === -1) {
							sUrl = sUrl + "&compressor=" + compressorText;
						}
					}
				} else {
					sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=ROC&loggedInUser=" + loggedInUser;
					if (this.getModel("dashBoardModel").getProperty("/isInvestigationCreate")) {
						var sUserType = "";
						if (this.getModel("dashBoardModel").getProperty("/userData/isWW")) {
							sUserType = "WW";
						}
						if (this.getModel("dashBoardModel").getProperty("/userData/isRE")) {
							if (sUserType) {
								sUserType = sUserType + ",RE";
							} else {
								sUserType = "RE";
							}
						}
						if (this.getModel("dashBoardModel").getProperty("/userData/isALS")) {
							if (sUserType) {
								sUserType = sUserType + ",ALS";
							} else {
								sUserType = "ALS";
							}
						}
						if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) {
							if (sUserType) {
								sUserType = sUserType + ",POT";
							} else {
								sUserType = "POT";
							}
						}
						if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
							sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=ENG&loggedInUser=" + loggedInUser +
								"&hopperuserType=";
							sUrl = sUrl + sUserType;
						} else {
							if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "CA") {
								sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=ENG_POT&loggedInUser=" + loggedInUser +
									"&hopperuserType=";
								sUrl = sUrl + "WW,RE,ALS,POT";
							} else {
								sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=POT&loggedInUser=" + loggedInUser +
									"&hopperuserType=";
								sUrl = sUrl + sUserType;
							}
						}
					} else if (dashBoardModel.getProperty("/isInquiryCreate")) { //AN: #inquire--'LOGIC MIGHT CHANGE'
						var taskStatus = dashBoardModel.getProperty("/taskStatus");
						var taskID = dashBoardModel.getProperty("/taskId");
						if (taskOwner.includes(dashBoardModel.getProperty("/userData/displayName")) && taskStatus === "ASSIGNED") { //AN: #inquire
							// this.onSubmitTask("", "IN PROGRESS", taskID);
							var ssUrl = "/taskmanagementRest/tasks/updateStatus";
							var oPayload1 = {
								"userId": this.getModel("dashBoardModel").getProperty("/userData/userId"),
								"userDisplay": this.getModel("dashBoardModel").getProperty("/userData/displayName"),
								"status": "IN PROGRESS",
								"taskId": taskID
							};
							var oModel = new sap.ui.model.json.JSONModel();
							oModel.loadData(ssUrl, JSON.stringify(oPayload1), false, "POST", false, false, {
								"Content-Type": "application/json;charset=utf-8"
							});
						}
						if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
							sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=ENG&loggedInUser=" + loggedInUser +
								"&loggedInUserGrp=" +
								dashBoardModel.getProperty("/userData/engRole").split(",")[0];
						} else if (dashBoardModel.getProperty("/userData/isPOT")) {
							sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=POT&loggedInUser=" + loggedInUser +
								"&loggedInUserGrp=" +
								dashBoardModel.getProperty("/userData/potRole").split(",")[0];
						} else if (dashBoardModel.getProperty("/userData/isROC")) {
							sUrl = "/taskmanagementRest/tasks/read?taskId=" + taskId + "&userType=ROC&loggedInUser=" + loggedInUser +
								"&loggedInUserGrp=" +
								dashBoardModel.getProperty("/userData/resGroupRead");
						}
					}
				}
				this.doAjax(sUrl, "GET", null, function (oData) {
						if (oData.collabrationDtos) {
							var aCollabrationDtos = oData.collabrationDtos;
							var aCollabration = [];
							for (var i = 0; i < aCollabrationDtos.length; i++) {
								if (aCollabrationDtos[i].message !== null) {
									aCollabration.push(aCollabrationDtos[i]);
								}
							}
							if (aCollabration.length > 0) {
								oData.collabrationDtos = aCollabration;
							} else {
								oData.collabrationDtos = null;
							}
						}
						this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", false);
						if (oData.responseMessage.statusCode === "0") {
							if (oData && oData.taskEventDto && oData.taskEventDto.processId) { //AN: #msgToROC - for comments on icon click
								dashBoardModel.setProperty("/processId", oData.taskEventDto.processId)
							}
							if (dashBoardModel.getProperty("/isCreateTask")) {
								if (oData.locHierarchy.length !== 0) { //AN: #taskFieldFilter
									// var currLoc = oData.locHierarchy.pop(); //AN: #taskFieldFilter
									var currLoc = oData.locHierarchy[oData.locHierarchy.length - 1]; //AN: #taskFieldFilter
									this.addAdditionalTasks(currLoc.loc);
									dashBoardModel.setProperty("/selectedLastLocation", currLoc.loc);
									dashBoardModel.setProperty("/selectedLocation", currLoc.loc);
								}
								var tempData = oData;
								tempData.taskEventDto = {
									createdBy: currentUser,
									createdByDisplay: dashBoardModel.getProperty("/userData/displayName"),
									description: "",
									subject: "",
									status: "NEW",
									owners: [],
									ownersName: null
								};
								if (alarmCreate) {
									if (dashBoardModel.getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
										tempData.taskEventDto.locationCode = dashBoardModel.getProperty("/dynamicContentType/inquiryLocationCode");
									} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP") { //AN: #inquire
										tempData.taskEventDto.locationCode = this.getModel("dashBoardModel").getProperty("/currDopObject/locationCode");
									} else {
										tempData.taskEventDto.locationCode = this.getModel("dashBoardModel").getProperty("/currAlarmObject/locationCode");
										tempData.muwiId = this.getModel("dashBoardModel").getProperty("/currAlarmObject/muwi");
									}
								} else {
									tempData.taskEventDto.locationCode = currSelectedLocation.location;
								}
								tempData.collabrationDtos = [];
								tempData.nonDispatchTasks = null;
								tempData.ndTaskList = [];
								oTaskPanelDetailModel.setData(tempData);
							} else {
								if ((oData.taskEventDto.origin !== "Investigation" && oData.taskEventDto.origin !== "Inquiry") && (oData.taskEventDto.status ===
										"RETURNED" || oData.taskEventDto.status === "ASSIGNED" || oData.taskEventDto.status === "DRAFT")) { //AN: #inquire //AN: #workbenchRaj
									dashBoardModel.setProperty("/isReturnedTask", true);
									var locationFetched = oData.customAttr["0"].labelValue;
									dashBoardModel.setProperty("/selectedLocation", locationFetched);
									this.addAdditionalTasks(locationFetched);
									dashBoardModel.setProperty("/sLocCodeToGetCurrentNearByUser", oData.taskEventDto.locationCode); //AN: #operatorsAvailable
								}
								if (oData.customAttr && oData.taskEventDto.createdByDisplay === "SYSTEM USER" && oData.taskEventDto.parentOrigin ===
									"Pigging" && oData.taskEventDto.status === "ASSIGNED") { //AN: #piggingChange (To change the status from "Assigned" to "NEW" for the system generated task)
									for (var c = 0; c < oData.customAttr.length; c++) {
										if (oData.customAttr[c].label === "Status" && oData.customAttr[c].labelValue === "ASSIGNED") {
											oData.customAttr[c].labelValue = "NEW";
											break;
										}
									}
								}
								var oInvestigationProactiveSwitch = sap.ui.core.Fragment.byId("CreateInvestigationPanel",
									"idInvestigationPanel--idProactiveCandidateHBox");
								if (oInvestigationProactiveSwitch && !oData.checkList) {
									this.getModel("dashBoardModel").setProperty("/showProActiveSwitch", false); // #RV LOGIC TO BE FIXED
								} else if (oInvestigationProactiveSwitch && oData.checkList) {
									this.getModel("dashBoardModel").setProperty("/showProActiveSwitch", true); // #RV LOGIC TO BE FIXED
								}
								oTaskPanelDetailModel.setData(oData);
							}
							if (dashBoardModel.getProperty("/isInvestigationCreate")) {
								this.getModel("dashBoardModel").setProperty("/selectedInvestigationTab", "invDetail");
								var oTaskList1 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idVarianceGrid");
								var oTaskList2 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idObservationGrid");
								dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
									userType: "POT",
									taskType: "Investigation"
								});
								this.createDynamicCont(oTaskList1, "varianceCustomAttr");
								this.createDynamicCont(oTaskList2, "observationCustomAttr");
								var oTaskList4 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idPWChecklistVBox");
								this.createDynamicContentPWCheckList(oTaskList4, "checkList");
								this.getInvestigationHistory(oData.muwiId); //AN: #inquire
								if (oData.taskEventDto.status === "RESOLVED") {
									this.onIssueClassificationChange("", 0);
								}
								if (dashBoardModel.getProperty("/userData/isENG")) {
									var taskEventDto = oTaskPanelDetailModel.getProperty("/taskEventDto");
									if (taskEventDto.status === "ASSIGNED") {
										this.onSubmitTask("", "IN PROGRESS", taskEventDto.taskId);
									}
									this.getRootCauseList(oTaskPanelDetailModel.getProperty("/taskEventDto/taskId"), "RESOLVED", "Investigation");
								} else if (dashBoardModel.getProperty("/userData/isPOT")) {
									var displayName = dashBoardModel.getProperty("/userData/displayName");
									var owners = oTaskPanelDetailModel.getProperty("/taskEventDto/owners");
									var status = oTaskPanelDetailModel.getProperty("/taskEventDto/status");
									for (var i = 0; i < owners.length; i++) {
										if (owners[i].taskOwnerDisplayName.includes(displayName) && (status === "ASSIGNED")) {
											taskEventDto = oTaskPanelDetailModel.getProperty("/taskEventDto");
											if (taskEventDto.status === "ASSIGNED") {
												this.onSubmitTask("", "IN PROGRESS", taskEventDto.taskId);
											}
											this.getRootCauseList(oTaskPanelDetailModel.getProperty("/taskEventDto/taskId"), "RESOLVED", "Investigation");
										}
									}
								}
							} else if (dashBoardModel.getProperty("/isInquiryCreate")) { //AN: #inquire
								var oTaskList3 = sap.ui.core.Fragment.byId("idCreateInquiryPanel", "idInquiryDetailsPanel--idInquiryObservationGrid");
								if (oData.observationCustomAttr) {
									if (dashBoardModel.getProperty("/userData").isENG) {
										dashBoardModel.setProperty("/dynamicContentType", {
											userType: "ENG",
											taskType: "Inquiry"
										});
									} else if (dashBoardModel.getProperty("/userData").isPOT) {
										dashBoardModel.setProperty("/dynamicContentType", {
											userType: "POT",
											taskType: "Inquiry"
										});
									} else if (dashBoardModel.getProperty("/userData").isROC) {
										dashBoardModel.setProperty("/dynamicContentType", {
											userType: "ROC",
											taskType: "Inquiry"
										});
									}
									this.createDynamicCont(oTaskList3, "observationCustomAttr");
								}
							} else {
								var oTaskList = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idDynGrid");
								if (oTaskPanelDetailModel.getData().taskEventDto.status === "DRAFT") { //AN: #workbenchRaj
									oTaskPanelDetailModel.setProperty("/taskEventDto/owners", []);
								}
								this.createDynamicCont(oTaskList, "customAttr");
								this.onTaskClassificationChange();
								// Create task enhancement
								var ownersDispatch = oTaskPanelDetailModel.getProperty("/taskEventDto/owners");
								if (oTaskPanelDetailModel.getData().taskEventDto.parentOrigin === "Pigging" || oTaskPanelDetailModel.getData().taskEventDto.taskType ===
									"SYSTEM" || (ownersDispatch && ownersDispatch.length > 0)) {
									if (oTaskPanelDetailModel.getData().taskEventDto.status === "ASSIGNED" || oTaskPanelDetailModel.getData().taskEventDto.status ===
										"DRAFT") { //AN: #workbenchRaj
										sKey = oTaskPanelDetailModel.getData().taskEventDto.locationCode;
										this.getTaskHistory(sKey);
										this.getModel("dashBoardModel").setProperty("/createTaskPanelVisible", true);
									} else {
										this.getModel("dashBoardModel").setProperty("/createTaskPanelVisible", false);
									}
									if (oTaskPanelDetailModel.getData().taskEventDto.status !== "DRAFT") { //AN: #workbenchRaj -- DONT execute the below if the status is DRAFT, since the owner is not a field user, instead it's the ROC.
										sSubClassification = oTaskPanelDetailModel.getProperty("/taskEventDto").subject.split("/")[1];
										if (ownersDispatch && ownersDispatch.length > 0) {
											var ownerId = [{
												"userId": ownersDispatch[0].taskOwner
											}];
											this.getTaskSchedulingDataForUser(ownerId);
											if (sSubClassification && sKey && ownersDispatch[0].taskOwner) {
												this.getTaskStartDate(ownersDispatch[0].taskOwner, sKey, sSubClassification);
											}
										}
									}
								}
							}
						} else {
							this._createConfirmationMessage("Error", oData.responseMessage.message, "Error", "", "Close", false, null);
						}
					}.bind(this),
					function (oError) {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", false);
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
				if (dashBoardModel.getProperty("/isInvestigationCreate")) {
					if (!this.oInvestigationPanel) {
						this.oInvestigationPanel = this._createFragment(oFragmentId1, oFragmentName1);
						this.getView().addDependent(this.oInvestigationPanel);
					}
					this.oInvestigationPanel.open();
					sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationRightPanel--idCollaborationTab").setSelectedKey(
						"Comments");
					sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idCreateDetailLeftPanel").$().find(
							".iopwbFlexBoxClass").parent()
						.addClass("iopwbFlexBoxClass");
				} else if (dashBoardModel.getProperty("/isInquiryCreate")) { //AN: #inquire
					if (!this.oCreateInquiryFragment) {
						this.oCreateInquiryFragment = this._createFragment(oFragmentId1, oFragmentName1);
						this.getView().addDependent(this.oCreateInquiryFragment);
					}
					this.oCreateInquiryFragment.open();
				} else {
					if (!this.createTaskPanel) {
						this.createTaskPanel = this._createFragment(oFragmentId1, oFragmentName1);
						this.getView().addDependent(this.createTaskPanel);
					}
					this.createTaskPanel.open();
					dashBoardModel.setProperty("/addItemsTabKey", "All");
					sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idAdditionalItemsTab").setSelectedKey("All");
					sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailRightPanel--idCollaborationTab").setSelectedKey("Comments");
					sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idCreateDetailLeftPanel").$().find(
							".iopwbFlexBoxClass").parent()
						.addClass("iopwbFlexBoxClass");
				}
				if (window.navigator.msSaveOrOpenBlob) {
					if (this.createTaskPanel) {
						this.createTaskPanel.$().find(".iopwbCloseBtnClass").addClass("iopwbCloseBtnIEClass");
					}
					if (this.oInvestigationPanel) {
						this.oInvestigationPanel.$().find(".iopwbCloseBtnClass").addClass("iopwbCloseBtnIEClass");
					}
				}
			} else {
				var locType = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
				var currLoc = "";
				if (this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject").length === 1) {
					currLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject/0/locationText");
				}
				if (!dashBoardModel.getProperty("/nonDispatchItem/isEdit")) {
					this.getModel("dashBoardModel").setProperty("/nonDispatchItem/descTypeKey", "Pressure optimization (ex. tubing and sep)");
					this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemLoc", currLoc);
					if (locType === "SEARCH") {
						this.getModel("dashBoardModel").setProperty("/nonDispatchItem/locTypeKey", "Well");
					} else {
						this.getModel("dashBoardModel").setProperty("/nonDispatchItem/locTypeKey", locType);
					}
				}
				dashBoardModel.getProperty("/nonDispatchItem/isEdit", false);
				var sLoc = this.getModel("dashBoardModel").getProperty("/nonDispatchItem/locTypeKey");
				if (sLoc === "BASE") {
					MessageBox.error("Cannot create non-dispatch at Base level");
				} else {
					this._bindLocationSuggestionforNonDispatch(sLoc);
					var oFragmentId2 = "idAddItemPanel",
						oFragmentName2 = "com.sap.incture.Incture_IOP.fragment.addItemPanel";
					if (!this.addItemPanel) {
						this.addItemPanel = this._createFragment(oFragmentId2, oFragmentName2);
						this.getView().addDependent(this.addItemPanel);
					}
					this.addItemPanel.open();
				}
			}
		},
		/**
		 *  Checking for if only single well or compressor is selectd from hierarchy to create the task. //SK
		 */
		TaskCreationHierarchyValidation: function () {
			var aSelectedObject = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			var isBase = false; //SH: Location Hierarchy - Tasks cannot be created for Base location
			for (var i = 0; i < aSelectedObject.length; i++) {
				if (aSelectedObject[i].locationText === "Canada" || aSelectedObject[i].locationText === "United States") {
					isBase = true;
				}
			}
			if (!isBase) {
				if (aSelectedObject.length === 1 && aSelectedObject[0].locationType !== "Meter") {
					var oTaskTypeDetails = { //AN: #ChangeSeat
						aSelectedObject: aSelectedObject,
						sLocCode: aSelectedObject[0].location,
						sLocType: aSelectedObject[0].locationType,
						sLocText: aSelectedObject[0].locationText
					};
					var aTaskTypeDetails = []; //AN: #ChangeSeat
					aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
					ChangeSeatHelper.checkAuthorization(this, this.getModel("dashBoardModel"), aTaskTypeDetails, "Heirarchy-Dispatch", "ROC"); //AN: #ChangeSeat
				} else {
					if (aSelectedObject.length > 1) {
						this._showToastMessage("Please select a single location to create a task");
						this.getModel("dashBoardModel").setProperty("/taskStatus", "");
						this.getModel("dashBoardModel").setProperty("/taskCreation", "");
						return;
					} else if (aSelectedObject.length === 0) {
						this._showToastMessage("Please select a location");
						this.getModel("dashBoardModel").setProperty("/taskStatus", "");
						this.getModel("dashBoardModel").setProperty("/taskCreation", "");
						return;
					} else if (aSelectedObject.length === 1 && aSelectedObject[0].locationType === "Meter") {
						this._showToastMessage("Task Cannot be created for Flare");
						this.getModel("dashBoardModel").setProperty("/taskStatus", "");
						this.getModel("dashBoardModel").setProperty("/taskCreation", "");
						return;
					}
				}
			} else {
				var sErrorMessage = "Cannot create task for base location";
				sap.m.MessageToast.show(sErrorMessage);
			}
		},
		/**
		 *  Checking for if the dispatch task is already exists or not on that location. // SK
		 */
		checkForCreateTask: function (locationCode, alarmCreate, UpdateTaskList, newLocationDto, compressorText) { //restriction on task : SK
			var sUrl = "/taskmanagementRest/tasks/checkForCreateTask?locationCode=" + locationCode;
			this.doAjax(sUrl, "GET", null, function (oData) {
					this.oBusyInd.close();
					if (oData.responseMessage.statusCode === "0") {
						if (this.getModel("dashBoardModel").getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
							this.getModel("dashBoardModel").setProperty("/taskCreation", "");
						}
						this.getModel("dashBoardModel").setProperty("/canCreateTask", oData.canCreate);
						this.getModel("dashBoardModel").setProperty("/InProgresstaskList", oData.taskList);
						if (!oData.canCreate && !this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) {
							var oFragmentId = "idTaskListHistory",
								oFragmentName = "com.sap.incture.Incture_IOP.fragment.TaskList";
							if (!this.TaskListHistory) {
								this.TaskListHistory = this._createFragment(oFragmentId, oFragmentName);
								this.getView().addDependent(this.TaskListHistory);
							}
							this.TaskListHistory.open();
							this.getModel("dashBoardModel").setProperty("/taskCreation", "");
							this.getModel("dashBoardModel").setProperty("/taskStatus", "");
							return;
						}
						if (!UpdateTaskList) {
							this.onCreateTaskPress("", "", alarmCreate, newLocationDto, compressorText);
						}
						if (this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen") && oData.canCreate === true) {
							this.TaskListHistory.close();
							this.getModel("dashBoardModel").setProperty("/canCreateTask", true);
							this.onCreateTaskPanelClose();
						}
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		onTaskListClose: function () {
			this.TaskListHistory.close();
			this.getModel("dashBoardModel").setProperty("/canCreateTask", true);
			this.getModel("dashBoardModel").setProperty("/isCreateTaskFromRightSidePanel", false);
			this.onCreateTaskPanelClose();
			if (this.getModel("dashBoardModel").getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
				this.getModel("dashBoardModel").setProperty("/InsightToActionData/iopWBMsgInd", false);
			}
		},
		onTaskListDialogOpen: function () {
			this.getModel("dashBoardModel").setProperty("/isTaskListDailogOpen", true);
		},
		onTaskListDialogClose: function () {
			this.getModel("dashBoardModel").setProperty("/isTaskListDailogOpen", false);
		},

		/**
		 * SK: To proceed the task creation.
		 */
		onProceedTask: function () {
			this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
			var currSelectedLocation = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (this.getModel("dashBoardModel").getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
				this.getModel("dashBoardModel").setProperty("/InsightToActionData/iopWBMsgInd", false);
				this.onCreateTaskPress();
			} else if (this.getModel("dashBoardModel").getProperty("/isCreateTaskFromRightSidePanel") && currSelectedLocation.length === 1 &&
				currSelectedLocation[0].locationType !== "Compressor") {
				this.getModel("dashBoardModel").setProperty("/taskCreation", "Custom");
				this.onCreateTaskPress();
			} else if (currSelectedLocation.length === 1 && currSelectedLocation[0].locationType === "Compressor") {
				this.getModel("dashBoardModel").setProperty("/taskCreation", "Custom");
				var CompressorParentDto = this.getModel("dashBoardModel").getProperty("/CompressorParent");
				this.onCreateTaskPress("", "", "", CompressorParentDto, currSelectedLocation[0].locationText);
			} else {
				this.onCreateTaskPress("", "", true);
			}
			if (this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) {
				this.TaskListHistory.close();
				this.getModel("dashBoardModel").setProperty("/canCreateTask", true);
				this.getModel("dashBoardModel").setProperty("/isCreateTaskFromRightSidePanel", false);
			}
		},

		/**
		 * Methods trigger when close btn is pressed. 
		 * @public
		 * Closes the fragment.
		 */
		onCreateTaskPanelClose: function (oEvent) {
			if (this.createTaskPanel) {
				this.createTaskPanel.close();
				// this.getView().getModel("dashBoardModel").getData().createTaskTabKey = "taskInfo";
			}
			if (this.getModel("dashBoardModel").getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
				this.onCreateInquiryClose();
			}
			this.onClosingDetailTask();
			this.getModel("dashBoardModel").setProperty("/startDateForUser", "");
		},

		onClosingDetailTask: function () {
			var dashBoardModel = this.getModel("dashBoardModel");
			this.getModel("oTaskPanelDetailModel").setData({});
			dashBoardModel.setProperty("/currentSelectedObjects", {});
			this.getModel("dashBoardModel").setProperty("/SchedulingData/scheduleData", []);
			this.getModel("dashBoardModel").setProperty("/createTaskScheduling", false);
			dashBoardModel.setProperty("/taskId", null);
			dashBoardModel.setProperty("/processId", null);
			dashBoardModel.setProperty("/isCreateTask", false);
			dashBoardModel.setProperty("/futureTask", false);
			dashBoardModel.setProperty("/newDispatchDateValue", "");
			dashBoardModel.setProperty("/isReturnedTask", false);
			dashBoardModel.setProperty("/isInvestigationCreate", false);
			dashBoardModel.setProperty("/suggestionItemsCountry", ""); // SK: Mile or KM check
			dashBoardModel.setProperty("/upliftImg", ""); // Uplift image
			dashBoardModel.setProperty("/isInquiryCreate", false); //AN: #inquire
			dashBoardModel.setProperty("createdByEmailId", null); //AN: #inquire
			dashBoardModel.setProperty("taskStatus", null); //AN: #inquire
			var tempDynamicContentType = dashBoardModel.getProperty("/dynamicContentType"); //AN: #inquire
			if (tempDynamicContentType) { //AN: #inquire
				tempDynamicContentType.from = null; //AN: #inquire
				dashBoardModel.setProperty("/dynamicContentType", tempDynamicContentType); //AN: #inquire
			}
			var oList = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist");
			if (oList) {
				oList.removeSelections();
			}
			var oTable = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idNDTaskTbl");
			if (oTable) {
				oTable.removeSelections();
			}
			var query = dashBoardModel.getProperty("/selectedTaskTab");
			TaskManagementHelper._bindRightTaskPanelModel(this, query); //AN: #ScratchFilter
			dashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
			this.clearDownTime();
			dashBoardModel.setProperty("/showDesignateOnTaskDetails", false);
			var oTextArea1 = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailRightPanel--idTMTextArea");
			var oFileUploader1 = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailRightPanel--idTMFileUploader");
			var oTextArea2 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationRightPanel--idTMTextArea");
			var oFileUploader2 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationRightPanel--idTMFileUploader");
			if (oTextArea1) {
				oTextArea1.setValue("");
			}
			if (oFileUploader1) {
				oFileUploader1.clear();
			}
			if (oTextArea2) {
				oTextArea2.setValue("");
			}
			if (oFileUploader2) {
				oFileUploader2.clear();
			}
			this.getModel("dashBoardModel").setProperty("/isTaskCardPress", false);
			this.getModel("dashBoardModel").setProperty("/isIconPress", false);
			this.getModel("dashBoardModel").setProperty("/taskCreation", false);
			this.getModel("dashBoardModel").setProperty("/showCreatePRTask", false);
			this.getModel("dashBoardModel").setProperty("/piggingTask", {
				isPiggingTask: false,
				piggingProcessId: null,
				piggingTaskId: null
			});
			dashBoardModel.setProperty("/piggingTask/isSystemTask", false);
			if (!this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) { // Sk : Restriction on dispatch task close.
				this.getModel("dashBoardModel").setProperty("/isCreateTaskFromRightSidePanel", false);
			}
			if (this.getModel("dashBoardModel").getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
				this.getModel("dashBoardModel").setProperty("/InsightToActionData/iopWBMsgInd", false);
			}
		},

		onAddItemPanelClose: function () {
			var oDashBoardModel = this.getModel("dashBoardModel");
			oDashBoardModel.setProperty("/nonDispatchItem/nonDispatchItemDesc", "");
			oDashBoardModel.setProperty("/nonDispatchItem/nonDispatchItemLoc", "");
			oDashBoardModel.setProperty("/nonDispatchItem/taskId", "");
			oDashBoardModel.setProperty("/nonDispatchItem/nonDispatchItemDecValueState", "None");
			oDashBoardModel.setProperty("/nonDispatchItem/nonDispatchItemLocValueState", "None");
			oDashBoardModel.setProperty("/nonDispatchItem/objectContext", {});
			oDashBoardModel.setProperty("/nonDispatchItem/locTypeKey", "Well");
			oDashBoardModel.setProperty("/nonDispatchItem/isEdit", false);
			oDashBoardModel.refresh();
			this.addItemPanel.close();
		},

		_validateNonDispatch: function (oObj) {
			var _isvalid = true;
			this.getModel("dashBoardModel").setProperty("/nonDispatchItem/isLocSelected", true);
			if (this.getModel("dashBoardModel").getProperty("/nonDispatchItem/descTypeKey") === "Other" && (oObj.nonDispatchItemDesc ===
					null ||
					oObj.nonDispatchItemDesc === "" || oObj.nonDispatchItemDesc === undefined)) {
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemDecValueState", "Error");
				_isvalid = false;
			} else {
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemDecValueState", "None");
			}
			if (oObj.nonDispatchItemLoc === null || oObj.nonDispatchItemLoc === "" || oObj.nonDispatchItemLoc === undefined) {
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemLocValueState", "Error");
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemLocValueStateText", "Location is Invalid");
				_isvalid = false;
			} else if (this.getModel("dashBoardModel").getProperty("/nonDispatchItem/isLocSelected") === false) {
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemLocValueState", "Error");
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemLocValueStateText",
					"Select Location from suggestions");
				_isvalid = false;
			} else {
				this.getModel("dashBoardModel").setProperty("/nonDispatchItem/nonDispatchItemLocValueState", "None");
			}
			return _isvalid;
		},

		/**
		 * Method triggers when creating additional non-dispatch item. 
		 * @public
		 */
		onAddingAdditionalItem: function (oEvent) {
			var oDashBoardModel = this.getModel("dashBoardModel"), //AN: #ChangeSeat
				sDescription = oDashBoardModel.getProperty("/nonDispatchItem/nonDispatchItemDesc"),
				sLocation = oDashBoardModel.getProperty("/nonDispatchItem/nonDispatchItemLoc"),
				bOperation = oDashBoardModel.getProperty("/nonDispatchItem/isEdit"),
				sLoctype = oDashBoardModel.getProperty("/nonDispatchItem/locTypeKey"),
				oObjectContext = oDashBoardModel.getProperty("/nonDispatchItem/objectContext");
			if (oDashBoardModel.getProperty("/nonDispatchItem/descTypeKey") !== "Other") {
				sDescription = oDashBoardModel.getProperty("/nonDispatchItem/descTypeKey");
			} else {
				sDescription = "Other - " + sDescription;
			}
			var oTaskTypeDetails = { //AN: #ChangeSeat
				oCurrSelectedLocation: oDashBoardModel.getProperty("/nonDispatchItem"),
				oObjectContext: oObjectContext,
				bOperation: bOperation,
				sDescription: sDescription,
				sLocCode: "",
				sLocType: sLoctype,
				sLocText: sLocation
			};
			var _isValid = this._validateNonDispatch(oDashBoardModel.getProperty("/nonDispatchItem"));
			if (_isValid) {
				var aTaskTypeDetails = []; //AN: #ChangeSeat
				aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Non-Dispatch", "ROC"); //AN: #ChangeSeat
			}
		},

		/**
		 * Method triggers when close/delete icon is pressed. 
		 * @public
		 */
		onCloseItem: function (oEvent) {
			var oCurrentContext;
			if (oEvent.getSource().getBindingContext("dashBoardModel") != undefined) {
				oCurrentContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			} else {
				oCurrentContext = this._currentTaskObject;
			}
			var sMsg = "",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			if (this.getModel("dashBoardModel").getData().selectedTaskTab === "Non-Dispatch") {
				sMsg = "Are you sure you want to delete this item?";
				this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
					this._removeNonDispatchTask(oCurrentContext.taskId);
				});
			} else if (oCurrentContext.taskType === "Inquiry") { //AN: #inquire
				this.onRemoveTask("", false, oCurrentContext.processId, true);
			} else {
				if (oCurrentContext.origin === "Investigation") {
					this.onCompletingInvestigation("", oCurrentContext.processId, oCurrentContext.muwiId);
				} else if (this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) {
					sMsg = "Are you sure you want to close this task?";
					this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
						if (this.getModel("dashBoardModel").getProperty("/isCreateTaskFromRightSidePanel")) {
							var aSelctedObject = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
							var locationCode = aSelctedObject[0].location;
							this.onRemoveTask(oCurrentContext.taskId, "", "", "", "", "", locationCode);
						} else {
							this.onRemoveTask(oCurrentContext.taskId, "", "", "", "", "", oCurrentContext.locationCode);
						}
					});
				} else {
					sMsg = "Are you sure you want to close this task?";
					this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
						this.onRemoveTask(oCurrentContext.taskId);
					});
				}
			}
			var sKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			this.onSideNavigationItemSelect(sKey);
		},
		/**
		 * Method to delete a non-dispatch task 
		 * @private
		 */
		_removeNonDispatchTask: function (sTaskId) {
			var sUrl = "/taskmanagementRest/nonDispatch/delete";
			var odata = {
				"taskId": sTaskId
			};
			this.doAjax(sUrl, "POST", odata, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						TaskManagementHelper._bindRightTaskPanelModel(this, "Non-Dispatch"); //AN: #ScratchFilter
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		onRemoveTask: function (sTaskId, isInvestigation, processId, isInquiry, inquiryAction, dontShowMsg, LocationCode) { //AN: #inquire
			var dashBoardModel = this.getModel("dashBoardModel"); //AN: #inquire
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true); //AN: #inquire
			var sUrl,
				user = this.getModel("dashBoardModel").getProperty("/userData/userId");
			var currentSelectKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"); //AN: #inquire
			if (isInvestigation || (isInquiry && !inquiryAction)) { //AN: #inquire
				if (isInquiry) { //AN: #inquire
					var dynamicContentType = this.getModel("dashBoardModel").getProperty("/dynamicContentType");
					if (dynamicContentType.from === "Inquiry") {
						user = dynamicContentType.inquiryCreatorId;
					}
					sUrl = "/taskmanagementRest/tasks/completeProcess?processId=" + processId + "&userId=" + user + "&origin=" + "Inquiry";
				} else {
					sUrl = "/taskmanagementRest/tasks/completeProcess?processId=" + processId + "&userId=" + user + "&origin=" +
						"Investigation";
				}
				this.doAjax(sUrl, "GET", null, function (oData) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
					if (oData.statusCode === "0") {
						if (!dontShowMsg) { //AN: #inquire
							this._showToastMessage(oData.message);
						}
						TaskManagementHelper._bindRightTaskPanelModel(this, this.getModel("dashBoardModel").getProperty("/selectedTaskTab")); //AN: #ScratchFilter
						this.onSideNavigationItemSelect(currentSelectKey);
						if (isInvestigation) {
							this.onCreateInvestigationClose();
						} else if (isInquiry) { //AN: #inquire
							this.onCreateInquiryClose();
						}
					} else {
						this._showToastMessage(oData.message);
					}
				}.bind(this), function (oError) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				});
			} else {
				sUrl = "/taskmanagementRest/tasks/updateStatus";
				var odata = {
					"taskId": sTaskId,
					"status": "COMPLETED",
					"userId": user,
					"userDisplay": user
				};
				if (isInquiry && inquiryAction === "ResolveInquiry") { //AN: #inquire
					odata.status = "RESOLVED"; //AN: #inquire
					odata.userDisplay = this.getModel("dashBoardModel").getProperty("/userData/displayName"); //AN: #inquiryEnhancement
				}
				this.doAjax(sUrl, "POST", odata, function (oData) {
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
						this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
						var selectedTab = this.getModel("dashBoardModel").getProperty("/selectedTaskTab");
						if (oData.statusCode === "0") {
							if (!dontShowMsg) { //AN: #inquire
								this._showToastMessage(oData.message);
							}
							TaskManagementHelper._bindRightTaskPanelModel(this, selectedTab); //AN: #ScratchFilter
							this.onSideNavigationItemSelect(currentSelectKey);
							if (isInvestigation) {
								this.onCreateInvestigationClose();
							} else if (isInquiry) { //AN: #inquire
								this.onCreateInquiryClose();
							} else {
								this.onCreateTaskPanelClose();
							}
							if (this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) {
								this.checkForCreateTask(LocationCode, " ", true);
							}
						} else {
							this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						}
					}.bind(this),
					function (oError) {
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
						this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			}
		},

		onPressCloseTask: function (oEvent) {
			var updateText = oEvent.getSource().getText();
			var validate = true;
			this.getModel("dashBoardModel").setProperty("/downtimeFromTaskCreate", false);
			if (updateText === "Update & Close Task") {
				this.getModel("dashBoardModel").setProperty("/downtimeFromTaskCreate", true);
				validate = this.onDownTimeSavePress("", "", "", true);
			}

			if (validate) {
				var sMsg = "Are you sure you want to close this task?",
					sTitle = "Confirm",
					sState = "None",
					confirmYesBtn = "Yes",
					confirmNoBtn = "No";
				this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
					if (this.getModel("dashBoardModel").getProperty("/isDesignateCreateTask")) {
						this.getModel("dashBoardModel").setProperty("/downtimeFromTaskCreate", true);
						validate = this.onDownTimeSavePress("");
					}
					if (this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) { // Sk: Restriction on Dispatch Task list Close
						if (this.getModel("dashBoardModel").getProperty("/isCreateTaskFromRightSidePanel")) {
							var aSelctedObject = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
							var locationCode = aSelctedObject[0].location;
							this.onRemoveTask(this.getModel("dashBoardModel").getProperty("/taskId"), "", "", "", "", "", locationCode);
						} else {
							var locationCode = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/locationCode");
							this.onRemoveTask(this.getModel("dashBoardModel").getProperty("/taskId"), "", "", "", "", "", locationCode);
						}
					} else {
						this.onRemoveTask(this.getModel("dashBoardModel").getProperty("/taskId"));
					}
				});
			}
		},

		/**
		 * Method triggers on click of any task item. 
		 * @public
		 * Method Opens the corresponding Detail fragment
		 */
		onPressTaskItem: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", true);
			var oDashBoardModel = this.getModel("dashBoardModel");
			var sBtnText = oDashBoardModel.getProperty("/selectedTaskTab");
			if (sBtnText === "Non-Dispatch") {
				sBtnText = "Add Item";
				var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
				if (oContext.description.split("-")[0] === "Other ") {
					var descArray = oContext.description.split(" - ");
					descArray.shift();
					oDashBoardModel.setProperty("/nonDispatchItem/nonDispatchItemDesc", descArray.join(" - "));
					oDashBoardModel.setProperty("/nonDispatchItem/descTypeKey", "Other");
				} else {
					oDashBoardModel.setProperty("/nonDispatchItem/descTypeKey", oContext.description);
				}
				oDashBoardModel.setProperty("/nonDispatchItem/nonDispatchItemLoc", oContext.location);
				oDashBoardModel.setProperty("/nonDispatchItem/taskId", oContext.taskId);
				oDashBoardModel.setProperty("/nonDispatchItem/isEdit", true);
				oDashBoardModel.setProperty("/nonDispatchItem/objectContext", oContext);
				oDashBoardModel.setProperty("/nonDispatchItem/locTypeKey", oContext.locType);
				oDashBoardModel.refresh();
				this._bindLocationSuggestionforNonDispatch(oContext.locType);
			} else {
				sBtnText = "Create Task";
				var oObject;
				this.getModel("dashBoardModel").setProperty("/isTaskCardPress", true);
				if (oEvent.getSource().getBindingContext("dashBoardModel") != undefined) {
					oObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
				} else {
					oObject = this._currentTaskObject;
				}
				var taskId = oObject.taskId;
				var processId = oObject.processId;
				var createdByEmailId = oObject.createdByEmailId; //AN: #inquire
				if (oObject.parentOrigin === "Pigging" && oObject.createdByEmailId === "SYSTEM") { //AN: #piggingChange
					oObject.status = "ASSIGNED";
				}
				var taskStatus = oObject.status; //AN: #piggingChange
				var creatorGroupId = oObject.creatorGroupId; //AN: #inquire
				var taskOwner = oObject.taskOwner; //AN: #inquire
				this.getModel("dashBoardModel").setProperty("/taskId", taskId);
				this.getModel("dashBoardModel").setProperty("/processId", processId);
				oDashBoardModel.setProperty("/createdByEmailId", createdByEmailId); //AN: #inquire
				oDashBoardModel.setProperty("/taskStatus", taskStatus); //AN: #inquire
				oDashBoardModel.setProperty("/creatorGroupId", creatorGroupId); //AN: #inquire
				oDashBoardModel.setProperty("/taskOwner", taskOwner); //AN: #inquire
				if (oObject.origin === "Investigation") {
					this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", true);
					oDashBoardModel.setProperty("/isInquiryCreate", false); //AN: #Inquire
				} else if (oObject.origin === "Inquiry") { //AN: #Inquire
					oDashBoardModel.setProperty("/isInquiryCreate", true);
					oDashBoardModel.setProperty("/isInvestigationCreate", false);
				} else {
					this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", false);
					oDashBoardModel.setProperty("/isInquiryCreate", false); //AN: #Inquire
				}
				this.getModel("dashBoardModel").setProperty("/taskCardData", oObject);
				this.getModel("dashBoardModel").setProperty("/createInvestigationObject", oObject); //#RV: Investigation ALS add Miles Changes

			}
			this.onCreateTaskPress("", sBtnText);
		},
		/**
		 * Restriction on dispatch task list press.
		 * @public
		 */
		onPressTaskListitem: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", true);
			var oDashBoardModel = this.getModel("dashBoardModel");
			var sBtnText = "Create Task";
			this.getModel("dashBoardModel").setProperty("/isTaskCardPress", true);
			if (oEvent.getSource().getBindingContext("dashBoardModel") != undefined) {
				var oObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			}
			var taskId = oObject.taskId;
			var processId = oObject.processId;
			var taskStatus = oObject.status;
			var createdByEmailId = oObject.createdByEmailId;
			var creatorGroupId = oObject.creatorGroupId;
			var taskOwner = oObject.taskOwner;
			this.getModel("dashBoardModel").setProperty("/taskId", taskId);
			this.getModel("dashBoardModel").setProperty("/processId", processId);
			this.getModel("dashBoardModel").setProperty("/taskStatus", taskStatus);
			this.getModel("dashBoardModel").setProperty("/createdByEmailId", createdByEmailId);
			this.getModel("dashBoardModel").setProperty("/creatorGroupId", creatorGroupId);
			this.getModel("dashBoardModel").setProperty("/taskOwner", taskOwner);
			this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", false);
			this.getModel("dashBoardModel").setProperty("/isInquiryCreate", false);
			this.getModel("dashBoardModel").setProperty("/taskCardData", oObject);
			this.getModel("dashBoardModel").setProperty("/createInvestigationObject", oObject); //#RV: Investigation ALS add Miles Changes

			this.onCreateTaskPress("", sBtnText);
		},
		/**
		 * Method triggers when task level search happens
		 * @public
		 */
		onSearchTask: function (oEvent) {
			var value = oEvent.getSource().getValue();
			var oTaskMgtPanelId = this.getView().createId("taskmanagementpane1");
			var oTaskList = sap.ui.core.Fragment.byId(oTaskMgtPanelId, "idTaskList");
			var aFilters;
			var filterParams = ["description", "location", "commentedAt", "createdAtDisplay", "taskOwner", "latestComment", "status",
				"origin"
			];
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTaskList.getBinding("items").filter(aFilters);
		},

		createDynamicCont: function (oTaskList, sCustAttr) {
			this.getModel("dashBoardModel").setProperty("/assignedToIndex", null);
			this.getModel("dashBoardModel").setProperty("/classification1Index", null);
			this.getModel("dashBoardModel").setProperty("/classification2Index", null);
			this.getModel("dashBoardModel").setProperty("/startDateTime", null);
			//SH: Modules readonly
			var currentSelectTab = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var modVis = this.validateModuleReadOnly(currentSelectTab);
			/*var isTaskSchedulerReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isTaskSchedulerReadOnly");
						var isDOPReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isDOPReadOnly");
						var isPWHopperReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isPWHopperReadOnly");
						var isWorkbenchReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isWorkbenchReadOnly");
						var isWebReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			
						var modVis = !isWebReadOnly ? !isTaskSchedulerReadOnly && currentSelectTab === "scheduler" ? true : !isDOPReadOnly &&
							currentSelectTab === "DOP" ? true : !isWorkbenchReadOnly && currentSelectTab === "workbench" ? true : !isPWHopperReadOnly &&
							currentSelectTab === "pwhopper" ? true : false : false;
						if (this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status") == "NEW" && !modVis) {
							modVis = true;
						}*/
			oTaskList.removeAllContent();
			var dynamicControlData = this.getModel("oTaskPanelDetailModel").getData()[sCustAttr];
			for (var i = 0; i < dynamicControlData.length; i++) {
				try {
					var height = "3.5rem";
					if (dynamicControlData[i].label === "Description and comments") {
						height = "auto";
					}
					var newControl = new sap.m.VBox({
						height: height,
						justifyContent: "SpaceBetween"
					}).addStyleClass("sapUiTinyMarginBottom");
					if (dynamicControlData[i].label === "Assign to person(s)") {
						this.getModel("dashBoardModel").setProperty("/taskAssignToLabel", dynamicControlData[i].label);
						newControl.addItem(new sap.m.Label({
							text: "{dashBoardModel>/taskAssignToLabel}",
							required: dynamicControlData[i].isMandatory
						}).addStyleClass("iopFontClass iopwbTaskLabelClass iopwbLabelRequiredClass"));
					} else {
						if (dynamicControlData[i].label !== "Uplift %" || (dynamicControlData[i].label === "Uplift %" && this.getModel(
								"oTaskPanelDetailModel").getProperty("/taskEventDto/locationCode").startsWith("MUR-CA"))) {
							if (dynamicControlData[i].label === "Uplift %") {
								var oHbox = new sap.m.HBox({});
								/*newControl.addItem(new sap.m.Label({
									text: "Uplift: {oTaskPanelDetailModel>/customAttr/" + i + "/labelValue} %",
									required: dynamicControlData[i].isMandatory
								})*/
								this.getModel("oTaskPanelDetailModel").setProperty("/customAttr/" + i + "/labelValue", 0);
								oHbox.addItem(new sap.m.Label({
									text: "Uplift : ",
									required: dynamicControlData[i].isMandatory
								}).addStyleClass("iopFontClass iopwbTaskLabelClass iopwbLabelRequiredClass"));
								oHbox.addItem(new sap.m.Label({
									text: "{oTaskPanelDetailModel>/customAttr/" + i + "/labelValue} %",
									required: dynamicControlData[i].isMandatory,
									design: "Bold"
								}).addStyleClass("iopFontClass upliftText"));
								newControl.addItem(oHbox);
							} else {
								newControl.addItem(new sap.m.Label({
									text: dynamicControlData[i].label,
									required: dynamicControlData[i].isMandatory
								}).addStyleClass("iopFontClass iopwbTaskLabelClass iopwbLabelRequiredClass"));
							}
						}
					}
					if (dynamicControlData[i].dataType === "Select") {
						var selectControl;
						if (dynamicControlData[i].label === "Task Classification") { // && dynamicControlData[i].isEditable
							if (dynamicControlData[i].labelValue) {
								this.getModel("dashBoardModel").setProperty("/taskSubject1", dynamicControlData[i].labelValue);
							} else {
								this.getModel("dashBoardModel").setProperty("/taskSubject1", "Downtime");
							}
							selectControl = new sap.m.Select({
								width: "100%",
								selectedKey: "{dashBoardModel>/taskSubject1}",
								enabled: dynamicControlData[i].isEditable && modVis,
								tooltip: "{dashBoardModel>/taskSubject1}",
								change: function (oEvent) {
									this.onTaskClassificationChange(oEvent);
								}.bind(this)
							}).addStyleClass("sapUiSizeCompact iopSelectClass iopwbTaskBoderLightClass");
							this.getModel("dashBoardModel").setProperty("/classification1Index", i);
							selectControl.bindItems("oTaskPanelDetailModel>/customAttr/" + i + "/valueDtos", function (index, context) {
								var obj = context.getObject();
								return new sap.ui.core.Item({
									text: obj.value,
									key: obj.value
								});
							});
							selectControl.setSelectedKey("Downtime");
						} else if (dynamicControlData[i].label === "Sub Classification" && !this.getModel("dashBoardModel").getProperty(
								"/isInvestigationCreate")) {
							if (dynamicControlData[i].labelValue && this.getModel("dashBoardModel").getProperty("/taskSubject1") === "Downtime") {
								this.getModel("dashBoardModel").setProperty("/taskSubject2", dynamicControlData[i].labelValue);
							} else if (dynamicControlData[i].labelValue) {
								this.getModel("dashBoardModel").setProperty("/taskSubject3", dynamicControlData[i].labelValue);
							}
							if (dynamicControlData[i].labelValue) {
								for (var j = 0; j < dynamicControlData[i].valueDtos.length; j++) {
									dynamicControlData[i].valueDtos[j].dependentValue = this.getModel("dashBoardModel").getProperty("/taskSubject1");
								}
							}
							selectControl = new sap.m.Select({ //AN: #sub_class incident: changed from ComboBox to Select
								width: "100%",
								selectedKey: "{dashBoardModel>/taskSubject2}",
								enabled: dynamicControlData[i].isEditable && modVis,
								tooltip: "{dashBoardModel>/taskSubject2}",
								change: function (oEvent) { //AN: #sub_class incident: changed from SelectionChange to Change
									this.onSubClassificationChange(oEvent);
								}.bind(this),
								visible: "{= ${dashBoardModel>/taskSubject1} === 'Downtime'}"
							}).addStyleClass("sapUiSizeCompact iopSelectClass iopwbTaskBoderLightClass");
							this.getModel("dashBoardModel").setProperty("/classification2Index", i);
							selectControl.bindItems("oTaskPanelDetailModel>/customAttr/" + i + "/valueDtos", function (index, context) {
								var obj = context.getObject();
								return new sap.ui.core.Item({
									text: obj.value,
									key: obj.value
								});
							});
							this.getModel("dashBoardModel").setProperty("/subClassId", selectControl.getId());
							var multiComboBox = new sap.m.Select({ //AN: #sub_class incident: changed from ComboBox to Select
								enabled: dynamicControlData[i].isEditable && modVis,
								width: "100%",
								visible: "{= ${dashBoardModel>/taskSubject1} !== 'Downtime'}",
								selectedKey: "{dashBoardModel>/taskSubject3}",
								tooltip: "{dashBoardModel>/taskSubject3}",
								change: function (oEvent) { //AN: #sub_class incident: changed from SelectionChange to Change
									this.onSubClassificationChange(oEvent);
								}.bind(this)
							}).addStyleClass("sapUiSizeCompact iopSelectClass iopwbTaskBoderLightClass");
							multiComboBox.bindItems("oTaskPanelDetailModel>/customAttr/" + i + "/valueDtos", function (index, context) {
								var obj = context.getObject();
								return new sap.ui.core.Item({
									text: obj.value,
									key: obj.value
								});
							});
							newControl.addItem(multiComboBox);
							this.getModel("dashBoardModel").setProperty("/multiSubClassId", multiComboBox.getId());
						} else {
							if (dynamicControlData[i].label === "Assign to group") {
								this.getModel("dashBoardModel").setProperty("/assignToGroupId", i);
								//SK: Investigation for Canada - Remove ENGINEERING from Assign to group drop down.  
								if (this.getModel("dashBoardModel").getProperty("/isInvestigationCreate") && this.getModel("dashBoardModel").getProperty(
										"/userData/isPOT") && this.getModel("dashBoardModel").getProperty("/userData/hasCanadaRole")) {
									var aValueDtos = [];
									for (var n = 0; n < dynamicControlData[i].valueDtos.length; n++) {
										if (dynamicControlData[i].valueDtos[n].value !== "ENGINEERING") {
											aValueDtos.push(dynamicControlData[i].valueDtos[n]);
										}
									}
									dynamicControlData[i].valueDtos = aValueDtos;
								}
							} else if (dynamicControlData[i].label === "Issue Classification") {
								this.getModel("dashBoardModel").setProperty("/primaryClassificationId", i);
							} else if (dynamicControlData[i].label === "Sub Classification") {
								this.getModel("dashBoardModel").setProperty("/secondaryClassificationId", i);
							}
							selectControl = new sap.m.Select({
								width: "100%",
								selectedKey: dynamicControlData[i].labelValue,
								enabled: dynamicControlData[i].isEditable && modVis,
								name: dynamicControlData[i].label,
								change: function (oEvent) {
									if (oEvent.getSource().getName() === "Assign to group") {
										this.onAssignGrpChange();
									}
									if (oEvent.getSource().getName() === "Issue Classification") {
										this.onIssueClassificationChange("", 0);
									}
								}.bind(this)
							}).addStyleClass("sapUiSizeCompact iopSelectClass iopwbTaskBoderLightClass");

							if (this.getModel("dashBoardModel").getData().isInquiryCreate && dynamicControlData[i].label === "Assign to group" &&
								dynamicControlData[i].labelValue) { //AN: #inquiryEnhancement
								var f = false;
								for (var a = 0; a < dynamicControlData[i].valueDtos.length; a++) {
									if (dynamicControlData[i].valueDtos[a].value === dynamicControlData[i].labelValue) {
										break;
									} else {
										f = true;
									}
								}
								if (f) {
									selectControl.setSelectedKey(dynamicControlData[i].valueDtos[0].value);
								}
							}

							selectControl.bindItems("oTaskPanelDetailModel>/" + sCustAttr + "/" + i + "/valueDtos", function (index, context) {
								var obj = context.getObject();
								return new sap.ui.core.Item({
									text: obj.value,
									key: obj.value
								});
							});
						}
						newControl.addItem(selectControl);
					} else if (dynamicControlData[i].dataType === "Input") {
						if (dynamicControlData[i].label === "Uplift %") {
							this.getModel("dashBoardModel").setProperty("/upliftIndex", i);
							var locCode = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/locationCode");
							var upliftVis = locCode.startsWith("MUR-CA") ? true : false;
							var sTaskStatus = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status");
							if ((sTaskStatus === "NEW" || sTaskStatus === "Returned" || dynamicControlData[i].labelValue === null) && locCode.startsWith(
									"MUR-CA")) {
								this.getUpliftFactor(locCode, ""); //SH: uplift factor - only for Canada
							}
							var inputControl = new sap.m.Image({
								visible: upliftVis,
								src: "{dashBoardModel>/upliftImg}",
								width: "3rem",
								height: "2.5rem"
							}).addStyleClass("sapUiSizeCompact  upliftImageBackground");
							/*	var inputControl = new sap.m.Input({
									width: "100%",
									value: "{oTaskPanelDetailModel>/customAttr/" + i + "/labelValue}",
									visible: upliftVis,
									editable: dynamicControlData[i].isEditable && modVis,
									liveChange: function (oEvent) {
										this.onUpliftChange(oEvent);
									}.bind(this)
								}).addStyleClass("sapUiSizeCompact iopInputClass iopwbTaskBoderLightClass");*/
						} else {
							var inputControl = new sap.m.Input({
								width: "100%",
								value: dynamicControlData[i].labelValue,
								editable: dynamicControlData[i].isEditable && modVis,
							}).addStyleClass("sapUiSizeCompact iopInputClass iopwbTaskBoderLightClass");
						}
						newControl.addItem(inputControl);
					} else if (dynamicControlData[i].dataType === "Text Area") {
						var inputControl = new sap.m.TextArea({
							width: "100%",
							value: dynamicControlData[i].labelValue,
							editable: dynamicControlData[i].isEditable && modVis
						}).addStyleClass("sapUiSizeCompact iopInputClass iopwbTaskBoderLightClass");
						newControl.addItem(inputControl);
					} else if (dynamicControlData[i].dataType === "MultiSelect") {
						if (dynamicControlData[i].isEditable && modVis) {
							var oFragmentName = "com.sap.incture.Incture_IOP.fragment.SuggestionInputCustom";
							var multiSelectControl = this._createFragment("", oFragmentName);
							newControl.addItem(multiSelectControl);
							multiSelectControl.onAfterRendering = function (evt) {
								if (this.getModel("dashBoardModel").getProperty("/assignedToEditable")) {
									this._focusSuggestionList(evt);
								}
							}.bind(this);
							this.getModel("dashBoardModel").setProperty("/assignedToEditable", dynamicControlData[i].isEditable);
							this.getModel("dashBoardModel").setProperty("/assignedToIndex", i);
							var array = [];
							var owners = this.getModel("oTaskPanelDetailModel").getData().taskEventDto.owners;
							for (var j = 0; j < owners.length; j++) {
								var nameArray = owners[j].taskOwnerDisplayName.split(" ");
								var fName = nameArray.shift();
								var lName = nameArray.join(" ");
								array.push({
									"firstName": fName,
									"lastName": lName,
									"emailId": owners[j].taskOwner,
									"userId": owners[j].taskOwner,
									"pId": owners[j].pId
								});
							}
							this.getModel("dashBoardModel").setProperty("/currentSelectedObjects", array);
						} else {
							var assignedToTextControl = new sap.m.Text({
								width: "100%",
								text: dynamicControlData[i].labelValue,
								tooltip: dynamicControlData[i].labelValue
							}).addStyleClass("iopFontClass sapUiTinyMarginBottom sapUiTinyMarginBegin iopwbAssignedTextClass");
							newControl.addItem(assignedToTextControl);
						}
					} else if (dynamicControlData[i].dataType === "ComboBox") {
						var comboControl = new sap.m.ComboBox({
							width: "100%",
							selectedKey: dynamicControlData[i].labelValue,
							editable: dynamicControlData[i].isEditable && modVis
						}).addStyleClass("sapUiSizeCompact iopSelectClass iopwbTaskBoderLightClass iopwbComboBoxClass");
						comboControl.bindItems("oTaskPanelDetailModel>/customAttr/" + i + "/valueDtos", function (index, context) {
							var obj = context.getObject();
							return new sap.ui.core.Item({
								text: obj.value,
								key: obj.value
							});
						});
						newControl.addItem(comboControl);
					} else if (dynamicControlData[i].dataType === "") {
						newControl.addItem(new sap.m.HBox());
					} else if (dynamicControlData[i].dataType === "Text") {
						if (dynamicControlData[i].label === "Status" && (dynamicControlData[i].labelValue === null || dynamicControlData[i].labelValue ===
								"" || dynamicControlData[i].labelValue === undefined)) {
							dynamicControlData[i].labelValue = "NEW";
						}
						var sCSSStyle;
						if (dynamicControlData[i].label === "Status") {
							var statusVal = dynamicControlData[i].labelValue;
							if (statusVal === "NEW") {
								sCSSStyle = "iopwbNewStatusColor";
							} else if (statusVal === "ASSIGNED") {
								sCSSStyle = "iopwbDispatchedStatusColor";
							} else if (statusVal === "IN PROGRESS") {
								sCSSStyle = "iopwbInProgressStatusColor";
							} else if (statusVal === "RESOLVED") {
								sCSSStyle = "iopwbResolvedStatusColor";
							} else if (statusVal === "RETURNED") {
								sCSSStyle = "iopwbReturnedStatusColor";
							} else if (statusVal === "DRAFT") { //AN: #workbench
								sCSSStyle = "iopwbNewStatusColor";
							} else if (statusVal === "CANCELLED") {
								sCSSStyle = "iopwbReturnedStatusColor";
							} else {
								sCSSStyle = "iopwbDispatchedStatusColor";
							}
						}
						var textControl = new sap.m.Text({
							width: "100%",
							text: dynamicControlData[i].labelValue
						}).addStyleClass("sapUiSizeCompact iopFontClass sapUiTinyMarginBottom " + sCSSStyle);
						newControl.addItem(textControl);
					} else if (dynamicControlData[i].dataType === "Date") {
						var dateControl = new sap.m.DatePicker({
							width: "100%",
							value: dynamicControlData[i].labelValue,
							placeholder: "mm/dd/yyyy",
							displayFormat: "MM/dd/yyyy",
							valueFormat: "yyyy-MM-dd",
							editable: dynamicControlData[i].isEditable && modVis
						}).addStyleClass("sapUiSizeCompact iopInputClass iopDTInputClass sapUiTinyMarginBottom " + sCSSStyle);
						newControl.addItem(dateControl);
					} else if (dynamicControlData[i].dataType === "DateTime") {
						// var dateControl1 = new sap.m.DateTimePicker({
						// 	width: "100%",
						// 	value: dynamicControlData[i].labelValue,
						// 	placeholder: "dd-MMM-yy, hh:mm:ss a",
						// 	displayFormat: "dd-MMM-yy, hh:mm:ss a",
						// 	valueFormat: "dd-MMM-yy, hh:mm:ss a",
						// 	editable: dynamicControlData[i].isEditable,
						// 	change: function (oEvent) {
						// 		if (oEvent.getSource().getEditable()) {
						// 			this.onDateTimeSelectPig(oEvent);
						// 		}
						// 	}.bind(this)
						// }).addStyleClass("sapUiSizeCompact iopInputClass iopDTInputClass sapUiTinyMarginBottom " + sCSSStyle);
						var dateControl1 = new sap.m.TimePicker({ //AN: #piggingChange
							width: "100%",
							value: dynamicControlData[i].labelValue,
							placeholder: "hh:mm:ss a",
							displayFormat: "hh:mm:ss a",
							valueFormat: "hh:mm:ss a",
							editable: dynamicControlData[i].isEditable && modVis,
							enabled: dynamicControlData[i].isEnabled,
							change: function (oEvent) {
								if (oEvent.getSource().getEditable()) {
									this.onDateTimeSelectPig(oEvent);
								}
							}.bind(this)
						}).addStyleClass("sapUiSizeCompact iopInputClass iopDTInputClass sapUiTinyMarginBottom " + sCSSStyle);
						dateControl1.addEventDelegate({
							onAfterRendering: function () {
								dateControl1.$().find("input").attr("readonly", true);
							}
						});
						newControl.addItem(dateControl1);
					} else if (dynamicControlData[i].dataType === "DateTimeTask") {
						var that = this;
						this.getModel("dashBoardModel").setProperty("/startDateTime", i);
						var sDateValue = dynamicControlData[i].labelValue;
						if (sDateValue) {
							var nDate = new Date(sDateValue + " UTC");
							var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
								pattern: "dd-MMM-yy hh:mm:ss aaa"
							});
							var sLabelValue = oDateFormat.format(nDate);
						} else {
							var sLabelValue = "";
						}
						this.getModel("dashBoardModel").setProperty("/startDateTimeLablevalue", sLabelValue);
						this.getModel("dashBoardModel").setProperty("/startDateTimeValueState", "None");
						this.getModel("dashBoardModel").setProperty("/startDateTimeValueStateText", "");
						var dateControl2 = new sap.m.DateTimePicker({
							width: "100%",
							value: "{dashBoardModel>/startDateTimeLablevalue}",
							placeholder: "dd-MMM-yy, hh:mm:ss a",
							displayFormat: "dd-MMM-yy, hh:mm:ss a",
							valueFormat: "dd-MMM-yy, hh:mm:ss a",
							editable: dynamicControlData[i].isEditable && modVis,
							valueState: "{dashBoardModel>/startDateTimeValueState}",
							valueStateText: "{dashBoardModel>/startDateTimeValueStateText}",
							change: function (oEvent) {
								if (oEvent.getSource().getEditable()) {
									this.onDateTimeSelectDispatch(oEvent);
								}
							}.bind(this)
						}).addStyleClass("sapUiSizeCompact iopInputClass iopDTInputClass sapUiTinyMarginBottom " + sCSSStyle);
						dateControl2.addEventDelegate({
							onAfterRendering: function () {
								dateControl2.$().find("input").keyup(function (oEvent) {
									var sInputVal = dateControl2.$().find("input").val();
									that.onDateTimeSelectDispatch(sInputVal);
								});
								//	dateControl2.$().find("input").attr("readonly", true);
							}
						});
						newControl.addItem(dateControl2);
					}
					oTaskList.addContent(newControl);
				} catch (e) {}
			}
			if (sCustAttr === "custAttr") {
				this.onTaskClassificationChange();
			} else if (sCustAttr === "observationCustomAttr") { //AN: #inquire
				this.onAssignGrpChange();
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", false);
		},

		_focusSuggestionList: function (evt) {
			this.oSuggestionParentInput = evt.srcControl;
			var tInput = $(evt.srcControl.getDomRef()).find("input")[0];
			$(tInput).on("focus", function (oTag) {
				var oFragmentId = "oSuggestionPopover",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.SuggestionListPopover";
				if (!this.oSuggestionList) {
					this.oSuggestionList = this._createFragment(oFragmentId, oFragmentName);
					this.getView().addDependent(this.oSuggestionList);
				}
				if (!this.oSuggestionList.isOpen()) {
					this.oSuggestionList.openBy(this.oSuggestionParentInput);
					this.oSuggestionParentInput.focus();
					this.oSuggestionParentInput.fireLiveChange();
				}
			}.bind(this));
		},

		onTaskClassificationChange: function (oEvent) {
			var dashBoardModel = this.getModel("dashBoardModel");
			var id, oTaskList, key, sClassification, sSubClassification;
			var value = dashBoardModel.getProperty("/taskSubject1");
			if (value === "Downtime") {
				id = dashBoardModel.getProperty("/subClassId");
				oTaskList = sap.ui.getCore().byId(id);
				dashBoardModel.setProperty("/taskSubject3", "");
			} else {
				id = dashBoardModel.getProperty("/multiSubClassId");
				oTaskList = sap.ui.getCore().byId(id);
				dashBoardModel.setProperty("/taskSubject2", "");
			}
			var aFilters;
			var filterArray = [];
			if (value) {
				filterArray.push(new sap.ui.model.Filter("dependentValue", sap.ui.model.FilterOperator.EQ, value));
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTaskList.getBinding("items").filter(aFilters);
			if (value === "Downtime") {
				key = oTaskList.getItems()[0].getText();
				dashBoardModel.setProperty("/taskSubject2", key);
				sClassification = value;
				sSubClassification = key;
			} else {
				key = oTaskList.getItems()[0].getText();
				dashBoardModel.setProperty("/taskSubject3", key);
				sClassification = value;
				sSubClassification = key;
			}
			var sStatus = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status"); //AN: #operatorsAvailable
			if (this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status") === "NEW") {
				this.getTaskFLSOP(sClassification, sSubClassification);
			}
			if (!oEvent && sStatus === "NEW" || sStatus === "ASSIGNED" || sStatus === "RETURNED" || sStatus === "DRAFT") { //AN: #operatorsAvailable //AN: #workbenchRaj
				TaskManagementHelper.getAvailableOperators(this, sClassification, sSubClassification);
			}
			var oData = this.getModel("oTaskPanelDetailModel").getData();
			if (oEvent) {
				if (sClassification === "Operations" && sSubClassification === "Facility Walkthrough") {
					var oCurrDate = new Date();
					oCurrDate = oCurrDate + "";
					var aCurrentDate = oCurrDate.split(" ");
					var sTime = aCurrentDate[4];
					var aTime = sTime.split(":");
					if (aTime[0] >= 5) {
						var nDate = new Date();
						nDate.setDate(nDate.getDate() + 1);
					} else {
						nDate = new Date();
					}
					nDate.setHours("05", "00", "00");
					var aDateString = nDate.toDateString().split(" ");
					var sLabelValueFormat = aDateString[2] + "-" + aDateString[1] + "-" + aDateString[3].substring(2) + ", " + nDate.toLocaleTimeString();
					// dashBoardModel.setProperty("/newDispatchDateValue", nDate);
					// dashBoardModel.setProperty("/futureTask", true);
					//var startDateVale = dashBoardModel.getProperty("/newDispatchDateValue");
					// oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = sLabelValueFormat;
					// oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = startDateVale;
					// dashBoardModel.setProperty("/startDateTimeLablevalue", sLabelValueFormat);
					// dashBoardModel.setProperty("/startDateTimeValueState", "None");
				} else {
					/*	dashBoardModel.setProperty("/newDispatchDateValue", "");
						dashBoardModel.setProperty("/futureTask", false);
						dashBoardModel.setProperty("/startDateTimeLablevalue", "");
						oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = "";
						oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = "";
						dashBoardModel.setProperty("/startDateTimeValueState", "None");*/
				}
			}

		},

		attachmentListFactory: function (sId, oContext) {
			try {
				var oUIControl = null,
					oFragmentName;
				if (oContext.getProperty("message") !== "" && oContext.getProperty("message") !== null && oContext.getProperty("message") !==
					undefined) {
					return new sap.m.StandardListItem({
						visible: false
					});
				}
				if (oContext.getProperty("attachmentDetails")[0].fileType.split("/")[0] === "image") {
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.attachmentImageItem";
					oUIControl = this._createFragment("", oFragmentName);
				} else {
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.attachmentGeneralItem";
					oUIControl = this._createFragment("", oFragmentName);
				}
				return oUIControl;
			} catch (e) {

			}
		},

		commentListFactory: function (sId, oContext) {
			var oUIControl = null,
				oFragmentName;
			if (oContext.getProperty("message") === "" || oContext.getProperty("message") === null || oContext.getProperty("message") ===
				undefined) {
				return new sap.m.VBox({
					visible: false
				});
			} else {
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.taskCommentItem";
				oUIControl = this._createFragment("", oFragmentName);
			}
			return oUIControl;
		},

		onFileUploadChange: function (oEvent) {
			var file = oEvent.getParameter("files")[0];
			if (file) {
				var reader = new FileReader();
				reader.onload = function (readerEvt) {
					var binaryString;
					if (!readerEvt) {
						binaryString = reader.content;
					} else {
						binaryString = readerEvt.target.result;
					}
					var attachmentObject = {};
					attachmentObject["fileName"] = file.name;
					attachmentObject["fileSize"] = file.size;
					attachmentObject["fileType"] = file.type;
					attachmentObject["fileDoc"] = btoa(binaryString);
					attachmentObject["compressedFile"] = attachmentObject["fileDoc"];
					this.onUploadingFile(attachmentObject, oEvent);
				}.bind(this);
			}
			reader.readAsBinaryString(file);
		},

		onUploadingFile: function (attachmentObject, oEvent) {
			var oSource;
			if (oEvent) {
				oSource = oEvent.getSource();
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", true);
			var dashBoardModel = this.getModel("dashBoardModel");
			var cUserId = this._getCurrentLoggedInUser();
			var cUserDisplay = dashBoardModel.getProperty("/userData/displayName");
			var taskId = dashBoardModel.getProperty("/taskId");
			var collabDtos = this.getModel("oTaskPanelDetailModel").getData().collabrationDtos;
			if (!collabDtos) {
				collabDtos = [];
			}
			var arrOwners = [];
			var oTaskDetailModel = this.getModel("oTaskPanelDetailModel").getData();
			arrOwners = this.generatePidPayload(oTaskDetailModel.taskEventDto.owners);
			var fileSize = attachmentObject["fileSize"];
			var ext = attachmentObject["fileName"].split(".").pop().toLowerCase();
			if (fileSize >= "2097152") {
				this._showToastMessage("File size exceeded. Maximum file size accepted 2MB.");
				this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
				if (oSource) {
					oSource.clear();
				}
				return;
			}
			if (ext === "txt" || ext === "doc" || ext === "docx" || ext === "xls" || ext === "xlsx" || ext === "jpg" ||
				ext === "bmp" || ext === "png" || ext === "jpeg" || ext === "pdf") {
				var noOfAttachments = 0;
				for (var k = 0; k < collabDtos.length; k++) {
					if (collabDtos[k].message === "" || collabDtos[k].message === undefined || collabDtos[k].message === null) {
						noOfAttachments++;
					}
				}
				if (noOfAttachments >= 3) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
					this._showToastMessage("Only 3 attachments can be uploaded");
					if (oSource) {
						oSource.clear();
					}
					return;
				}
				if ((!dashBoardModel.getProperty("/isCreateTask")) && taskId && (taskId !== "")) {
					var sUrl = "/taskmanagementRest/collaboration/create";
					var oDataPayload = {
						"processId": dashBoardModel.getProperty("/processId"),
						"taskId": dashBoardModel.getProperty("/taskId"),
						"userDisplayName": cUserDisplay,
						"userId": cUserId,
						"attachmentDetails": [attachmentObject]
					};
					this.doAjax(sUrl, "POST", oDataPayload, function (oData) {
							this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
							if (oData.statusCode === "0") {
								if (oTaskDetailModel.taskEventDto.status === "IN PROGRESS") {
									this.sendNotification("New attachment added for " + oTaskDetailModel.taskEventDto.subject, arrOwners, false);
								}
								this.onTaskCollabTabSelect();
								if (oSource) {
									oSource.clear();
								}
							} else {
								this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
								this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
							}
						}.bind(this),
						function (oError) {
							this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
							this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
						}.bind(this));
				} else {
					var oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
					if (oTaskPanelDetailModel.getData().collabrationDtos && oTaskPanelDetailModel.getData().collabrationDtos.length > 0) {
						if (!(oTaskPanelDetailModel.getData().collabrationDtos instanceof Array)) {
							oTaskPanelDetailModel.getData().collabrationDtos = [oTaskPanelDetailModel.getData().collabrationDtos];
						}
					} else {
						oTaskPanelDetailModel.getData().collabrationDtos = [];
					}
					oTaskPanelDetailModel.getData().collabrationDtos.push({
						"attachmentDetails": [attachmentObject],
						"userDisplayName": cUserDisplay,
						"userId": cUserId,
						"createdAtDisplay": this.getCurrDate(),
						"status": "NEW"
					});
					oTaskPanelDetailModel.refresh();
					this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
					if (oSource) {
						oSource.clear();
					}
				}
			} else {
				this._showToastMessage("Cannot upload this type of file");
				this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
				if (oSource) {
					oSource.clear();
				}
				return;
			}
		},

		onPostComment: function () {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", true);
			var dashBoardModel = this.getModel("dashBoardModel");
			var oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
			var taskId = dashBoardModel.getProperty("/taskId");
			var processId = dashBoardModel.getProperty("/processId");
			var cUser = this._getCurrentLoggedInUser();
			var cUserDisplay = dashBoardModel.getProperty("/userData/displayName");
			var msg = dashBoardModel.getProperty("/newComment");
			if (msg === undefined || msg === "" || msg === null || msg.trim() === "") {
				this._showToastMessage("Enter a comment to post");
				this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
				return;
			}
			var objArray = {};
			if (((!dashBoardModel.getProperty("/isCreateTask")) && taskId && (taskId !== "")) || (((dashBoardModel.getProperty(
					"/isInvestigationCreate") || dashBoardModel.getProperty("/isInquiryCreate"))) && processId && (processId !== ""))) { //AN: #inquire
				var sUrl = "/taskmanagementRest/collaboration/create";
				var oData = {
					"processId": processId,
					"taskId": taskId,
					"message": msg,
					"userDisplayName": cUserDisplay,
					"userId": cUser
				};
				var arrOwners = [];
				arrOwners = this.generatePidPayload(oTaskPanelDetailModel.getData().taskEventDto.owners);
				this.doAjax(sUrl, "POST", oData, function (oData) {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
						if (oData.statusCode === "0") {
							if (oTaskPanelDetailModel.getData().taskEventDto.status === "IN PROGRESS") {
								this.sendNotification("New comment added for " + oTaskPanelDetailModel.getData().taskEventDto.subject, arrOwners, false);
							}
							this.onTaskCollabTabSelect();
						} else {
							this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
							this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
						}
					}.bind(this),
					function (oError) {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			} else {
				objArray.message = msg;
				objArray.userDisplayName = cUserDisplay;
				objArray.userId = cUser;
				objArray.createdAtDisplay = this.getCurrDate();
				if (oTaskPanelDetailModel.getData().collabrationDtos && oTaskPanelDetailModel.getData().collabrationDtos.length > 0) {
					if (!(oTaskPanelDetailModel.getData().collabrationDtos instanceof Array)) {
						oTaskPanelDetailModel.getData().collabrationDtos = [oTaskPanelDetailModel.getData().collabrationDtos];
					}
				} else {
					oTaskPanelDetailModel.getData().collabrationDtos = [];
				}
				oTaskPanelDetailModel.getData().collabrationDtos.push(objArray);
				oTaskPanelDetailModel.refresh();
				this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
			}
			dashBoardModel.setProperty("/newComment", "");
		},

		onTaskCollabTabSelect: function (oEvent) {
			if (oEvent && oEvent.getSource().getSelectedKey() === "ActivityLog") {
				return;
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", true);
			var taskId = this.getModel("dashBoardModel").getProperty("/taskId");
			var processId = this.getModel("dashBoardModel").getProperty("/processId");
			if (!taskId && !processId) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
				return;
			}
			var url;
			if (taskId && (!this.getModel("dashBoardModel").getProperty("/isInvestigationCreate"))) {
				url = "/taskmanagementRest/collaboration/readByTaskId?taskId=" + processId; //AN: #inquire(processid)
			} else {
				if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
					url = "/taskmanagementRest/collaboration/readByTaskId?taskId=" + processId + "&userType=ENG"; //AN: #inquire(processid)
				} else {
					url = "/taskmanagementRest/collaboration/readByTaskId?taskId=" + processId + "&userType=POT";
				}
			}
			this.doAjax(url, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					this.getModel("oTaskPanelDetailModel").getData().collabrationDtos = oData.collaborationDtos;
					this.getModel("oTaskPanelDetailModel").refresh();
					this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
				} else {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
				}
			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
			});
		},
		generatePidPayload: function (aOwners) {
			var arrOwners = [];
			for (var j = 0; j < aOwners.length; j++) {
				arrOwners.push({
					"userID": aOwners[j].pId
				});
			}
			var pIdPayload = {
				"ParamRoot": {
					"Param": arrOwners
				}
			};
			return pIdPayload;
		},

		onCreatingTask: function () {
			var oData = this.getModel("oTaskPanelDetailModel").getData();
			var dashBoardModel = this.getModel("dashBoardModel");
			var sKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			if (this.onCheckOptionalTasksSelected() === false) {
				return;
			}
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true);
			if (dashBoardModel.getProperty("/assignedToIndex") !== null) {
				var currObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
				oData.taskEventDto.owners = [];
				for (var i = 0; i < currObj.length; i++) {
					var lastName = currObj[i].lastName;
					var firstName = currObj[i].firstName;
					if (lastName) {
						firstName = firstName + " " + lastName;
					}
					oData.taskEventDto.owners.push({
						"taskOwner": currObj[i].emailId,
						"taskOwnerDisplayName": firstName,
						"ownerEmail": currObj[i].emailId,
						"pId": currObj[i].pId
					});
				}
			}
			oData.taskEventDto.subject = "";
			if (dashBoardModel.getProperty("/classification1Index") !== null) {
				oData.customAttr[dashBoardModel.getProperty("/classification1Index")].labelValue = dashBoardModel.getProperty(
					"/taskSubject1");
				oData.taskEventDto.subject = oData.taskEventDto.subject + dashBoardModel.getProperty("/taskSubject1") + " / ";
			}
			if (dashBoardModel.getProperty("/classification2Index") !== null) {
				if (dashBoardModel.getProperty("/taskSubject1") === "Downtime") {
					if (dashBoardModel.getProperty("/taskSubject2")) {
						oData.customAttr[dashBoardModel.getProperty("/classification2Index")].labelValue = dashBoardModel.getProperty(
							"/taskSubject2");
						oData.taskEventDto.subject = oData.taskEventDto.subject + dashBoardModel.getProperty("/taskSubject2");
						oData.taskEventDto.subClassification = dashBoardModel.getProperty("/taskSubject2");
					} else {
						this._showToastMessage("Select the sub classification to create the task");
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
						return;
					}
				} else {
					if (dashBoardModel.getProperty("/taskSubject3")) {
						oData.customAttr[dashBoardModel.getProperty("/classification2Index")].labelValue = dashBoardModel.getProperty(
							"/taskSubject3");
						oData.taskEventDto.subject = oData.taskEventDto.subject + dashBoardModel.getProperty("/taskSubject3");
						oData.taskEventDto.subClassification = dashBoardModel.getProperty("/taskSubject3");
					} else {
						this._showToastMessage("Select the sub classification to create the task");
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
						return;
					}
				}
			}
			var custAtt = oData.customAttr;
			if (oData.taskEventDto.owners.length == 0 || (!oData.taskEventDto.owners)) {
				this._showToastMessage("Select the Assignee to the task");
				dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
				//return; // ST: overwritten for testing
			}
			if (dashBoardModel.getProperty("/startDateTime") !== null) {
				var startDateVale = this.getModel("dashBoardModel").getProperty("/newDispatchDateValue");
				oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = startDateVale;
				var sLabelDateValue = "";
				if (startDateVale) {
					var oDate = new Date(startDateVale);
					var sUtcDate = oDate.toUTCString();
					var aUtcDate = sUtcDate.split(" ");
					var sDate = aUtcDate[1] + "-" + aUtcDate[2] + "-" + aUtcDate[3].substring(2, 4);
					var aTime = aUtcDate[4].split(":");
					var hh = parseInt(aTime[0]);
					var meridian = "AM";
					if (hh > 12) {
						hh = hh - 12;
						meridian = "PM";
					}
					if (hh === 0) {
						hh = "12";
					}
					if (hh > 0 && hh < 10) {
						hh = "0" + hh;
					}
					var mm = parseInt(aTime[1]);
					if (mm < 10) {
						mm = "0" + mm;
					}
					var ss = parseInt(aTime[2]);
					if (ss < 10) {
						ss = "0" + ss;
					}
					var sUtcTime = hh + ":" + mm + ":" + ss + " " + meridian;
					sLabelDateValue = sDate + ", " + sUtcTime;
				} else {
					sLabelDateValue = null;
				}
				oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = sLabelDateValue;
			}
			var sUrl = "/taskmanagementRest/tasks/createTask";
			var selectedTab = this.getModel("dashBoardModel").getProperty("/selectedTaskTab");
			var userData = dashBoardModel.getProperty("/userData");
			oData.futureTask = dashBoardModel.getProperty("/futureTask");
			oData.taskEventDto.createdBy = userData.userId;
			oData.taskEventDto.createdByDisplay = userData.displayName;
			//	oData.taskEventDto.group = userData.group; 
			var taskCreationGroup = this.getModel("dashBoardModel").getProperty("/changeSeatData/taskCreationRole");
			oData.taskEventDto.group = taskCreationGroup;
			oData.taskEventDto.origin = "Dispatch"; //AN: #inquire
			oData.taskEventDto.parentOrigin = "Custom"; //AN: #inquire
			//var tOwners = this.generatePidPayload(oData.taskEventDto.owners);
			var tOwners = oData.taskEventDto.owners;
			if (this.getModel("dashBoardModel").getProperty("/isDispatch") && !dashBoardModel.getProperty("/taskCreation")) {
				oData.pointId = this.getModel("dashBoardModel").getProperty("/currAlarmObject/pointId");
				oData.taskEventDto.origin = "Dispatch"; //AN: #inquire
				oData.taskEventDto.parentOrigin = "Alarm"; //AN: #inquire
			}
			if (oData.taskEventDto.status === "NEW") {
				oData.taskEventDto.status = "ASSIGNED";
			}
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP" && !dashBoardModel.getProperty(
					"/taskCreation")) {
				oData.taskEventDto.origin = "Dispatch"; //AN: #inquire
				oData.taskEventDto.parentOrigin = "Variance"; //AN: #inquire
			}
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "fracMonitoring" && !dashBoardModel.getProperty(
					"/taskCreation")) {
				oData.taskEventDto.origin = "Dispatch"; //ramees Frac
				oData.taskEventDto.parentOrigin = "Frac"; //ramees Frac
			}
			if (oData.customAttr[dashBoardModel.getProperty("/classification1Index")].labelValue === "OBX") {
				// cross check the scenario for entry point	for OBX	
				oData.taskEventDto.origin = "Dispatch";
				oData.taskEventDto.parentOrigin = "OBX";
			}
			var dynamicContentType = this.getModel("dashBoardModel").getProperty("/dynamicContentType"); //AN: #inquire
			if (dynamicContentType.from === "Inquiry") { //AN: #inquire
				oData.taskEventDto.origin = "Dispatch";
				oData.taskEventDto.parentOrigin = "Inquiry";
				oData.taskEventDto.prevTask = dynamicContentType.inquiryProcessId;
			}
			var piggingTask = dashBoardModel.getProperty("/piggingTask");
			if (piggingTask && piggingTask.isPiggingTask) {
				oData.taskEventDto.origin = "Dispatch";
				oData.taskEventDto.parentOrigin = "Pigging";
				oData.taskEventDto.taskId = null;
				oData.taskEventDto.processId = null;
			}
			if (dashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
				var oCurrSelWBMsgData = dashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
				oData.taskEventDto.parentOrigin = "Message";
				oData.taskEventDto.prevTask = oCurrSelWBMsgData.messageId;
				if (!taskCreationGroup) {
					oData.taskEventDto.group = userData.resGroupRead
				}
			}
			this.doAjax(sUrl, "POST", oData, function (oData) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					if (oData.statusCode === "0") {
						var piggingTask = this.getModel("dashBoardModel").getProperty("/piggingTask");
						if (piggingTask && piggingTask.isPiggingTask) {
							//close pig launch task
							this.onRemoveTask(piggingTask.piggingTaskId, "", "", "", "", true);
						}
						//this.sendNotification("You have a new task assigned", tOwners, true);
						var oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
						var isSendNotification;
						if (dashBoardModel.getProperty("/futureTask") && oTaskPanelDetailModel.getProperty("/taskEventDto/origin") === "Dispatch") {
							isSendNotification = false; //SK: Restricting Notification for Future task
						} else {
							isSendNotification = true;
						}
						if (isSendNotification) {
							var aOwnersNotif = []; //AN: #shiftExists
							for (var i = 0; i < tOwners.length; i++) { //AN: #shiftExists
								var sEmail = "";
								if (tOwners[i].ownerEmail) {
									sEmail = tOwners[i].ownerEmail;
								} else if (tOwners[i].taskOwner) {
									sEmail = tOwners[i].taskOwner;
								}
								if (sEmail) {
									var oUrl = "/taskmanagementRest/ShiftRegister/getShiftDetails?emp_email=" + sEmail;
									var oModel = new sap.ui.model.json.JSONModel();
									oModel.loadData(oUrl, "", false, "GET", false, false);
									if (oModel.getData()) {
										aOwnersNotif.push(tOwners[i]);
									}
								}
							}
							if (aOwnersNotif && aOwnersNotif.length > 0) {
								this.sendNotification("You have a new task assigned", aOwnersNotif, true);
							}
						}
						this._showToastMessage(oData.message);
						TaskManagementHelper._bindRightTaskPanelModel(this, selectedTab); //AN: #ScratchFilter
						this.onCreateTaskPanelClose();
						if (dashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
							var oCurrSelWBMsgData = dashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
							var msgDraftDetailData = dashBoardModel.getProperty("/InsightToActionData/msgDraftDetailData");
							// WorkbenchHelper.fnWBMsgUpdateStatus(this, dashBoardModel, "IN PROGRESS", msgDraftDetailData.currentOwner);
							WorkbenchHelper.fnWBMsgUpdateStatus(this, dashBoardModel, "ASSIGNED", msgDraftDetailData.currentOwner); //SH: NewFlow
							WorkbenchHelper.onWBMsgClose(this, this.getModel("dashBoardModel"));
							this.onTaskListClose();
						}
						this.onSideNavigationItemSelect(sKey);
						this.getModel("dashBoardModel").setProperty("/SchedulingData/scheduleData", []);
						if (this.getModel("dashBoardModel").getProperty("/isDispatch")) {
							this.onSideNavigationItemSelect(sKey);
						}
						this.getModel("dashBoardModel").setProperty("/classification1Index", null);
						this.getModel("dashBoardModel").setProperty("/classification2Index", null);
					} else {
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		onAddingItem: function () {
			var oTable = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idNDTaskTbl");
			var selectedItems = oTable.getSelectedItems();
			if (selectedItems.length > 0) {
				var temp;
				if (this.getModel("oTaskPanelDetailModel").getData().ndTaskList === null) {
					this.getModel("oTaskPanelDetailModel").getData().ndTaskList = [];
				}
				for (var i = 0; i < selectedItems.length; i++) {
					temp = selectedItems[i].getBindingContext("oAdditionalTaskModel").getObject();
					this.getModel("oTaskPanelDetailModel").getData().ndTaskList.push(temp);
				}
				this.getModel("oTaskPanelDetailModel").getData().ndTaskList = this.removeDuplicates(this.getModel("oTaskPanelDetailModel")
					.getData()
					.ndTaskList, "taskId");
				this.getModel("oTaskPanelDetailModel").refresh();
				this._showToastMessage("Selected Item(s) has been added");
				this.getModel("dashBoardModel").setProperty("/isOpTasksAdded", true);
			} else {
				this._showToastMessage("Select Item(s) to add");
			}
		},

		onDeletingItem: function (oEvent) {
			var sMsg = "Are you sure you want to delete this item?",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No",
				oContext = oEvent.getSource().getBindingContext("oTaskPanelDetailModel");
			this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
				var index = oContext.sPath.split("/").pop();
				this.getModel("oTaskPanelDetailModel").getData().ndTaskList.splice(index, 1);
				this.getModel("oTaskPanelDetailModel").refresh();
			}.bind(this));
		},

		onHierarchyLinkPress: function (oEvent) {
			var selectedLoc = oEvent.getSource().getText();
			if (this.onCheckOptionalTasksSelected()) {
				this.getModel("dashBoardModel").setProperty("/selectedLocation", selectedLoc);
				this.addAdditionalTasks();
				this.getModel("dashBoardModel").setProperty("/isOpTasksAdded", true);
			}
		},

		onCheckOptionalTasksSelected: function () {
			var oTable = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idNDTaskTbl");
			if (this.getModel("dashBoardModel").getProperty("/isOpTasksAdded") === false && oTable.getSelectedItems().length > 0) {
				this._showToastMessage("You have some tasks selected but not added");
				return false;
			} else {
				if (oTable) { //AN: #inquire(update investigation/inquiry)--issue
					oTable.removeSelections();
					return true;
				} else {
					return false;
				}
			}
		},

		onOptionalTaskSelectionChange: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/isOpTasksAdded", false);
		},

		getCurrDate: function (oDate) {
			var currDate;
			if (oDate) {
				var aDate = oDate.split(" ");
				var aDateArray = aDate[0].split("-");
				var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
				sDate = sDate + " " + aDate[1] + " " + aDate[2];
				currDate = new Date(sDate + " UTC");
			} else {
				currDate = new Date();
			}
			var dArray = currDate.toDateString().split(" ");
			var date = dArray[2] + "-" + dArray[1] + "-" + (currDate.getYear() - 100) + " ";
			var timeArray = currDate.toLocaleString().split(" ");
			var re =
				/[\0-\x1F\x7F-\x9F\xAD\u0378\u0379\u037F-\u0383\u038B\u038D\u03A2\u0528-\u0530\u0557\u0558\u0560\u0588\u058B-\u058E\u0590\u05C8-\u05CF\u05EB-\u05EF\u05F5-\u0605\u061C\u061D\u06DD\u070E\u070F\u074B\u074C\u07B2-\u07BF\u07FB-\u07FF\u082E\u082F\u083F\u085C\u085D\u085F-\u089F\u08A1\u08AD-\u08E3\u08FF\u0978\u0980\u0984\u098D\u098E\u0991\u0992\u09A9\u09B1\u09B3-\u09B5\u09BA\u09BB\u09C5\u09C6\u09C9\u09CA\u09CF-\u09D6\u09D8-\u09DB\u09DE\u09E4\u09E5\u09FC-\u0A00\u0A04\u0A0B-\u0A0E\u0A11\u0A12\u0A29\u0A31\u0A34\u0A37\u0A3A\u0A3B\u0A3D\u0A43-\u0A46\u0A49\u0A4A\u0A4E-\u0A50\u0A52-\u0A58\u0A5D\u0A5F-\u0A65\u0A76-\u0A80\u0A84\u0A8E\u0A92\u0AA9\u0AB1\u0AB4\u0ABA\u0ABB\u0AC6\u0ACA\u0ACE\u0ACF\u0AD1-\u0ADF\u0AE4\u0AE5\u0AF2-\u0B00\u0B04\u0B0D\u0B0E\u0B11\u0B12\u0B29\u0B31\u0B34\u0B3A\u0B3B\u0B45\u0B46\u0B49\u0B4A\u0B4E-\u0B55\u0B58-\u0B5B\u0B5E\u0B64\u0B65\u0B78-\u0B81\u0B84\u0B8B-\u0B8D\u0B91\u0B96-\u0B98\u0B9B\u0B9D\u0BA0-\u0BA2\u0BA5-\u0BA7\u0BAB-\u0BAD\u0BBA-\u0BBD\u0BC3-\u0BC5\u0BC9\u0BCE\u0BCF\u0BD1-\u0BD6\u0BD8-\u0BE5\u0BFB-\u0C00\u0C04\u0C0D\u0C11\u0C29\u0C34\u0C3A-\u0C3C\u0C45\u0C49\u0C4E-\u0C54\u0C57\u0C5A-\u0C5F\u0C64\u0C65\u0C70-\u0C77\u0C80\u0C81\u0C84\u0C8D\u0C91\u0CA9\u0CB4\u0CBA\u0CBB\u0CC5\u0CC9\u0CCE-\u0CD4\u0CD7-\u0CDD\u0CDF\u0CE4\u0CE5\u0CF0\u0CF3-\u0D01\u0D04\u0D0D\u0D11\u0D3B\u0D3C\u0D45\u0D49\u0D4F-\u0D56\u0D58-\u0D5F\u0D64\u0D65\u0D76-\u0D78\u0D80\u0D81\u0D84\u0D97-\u0D99\u0DB2\u0DBC\u0DBE\u0DBF\u0DC7-\u0DC9\u0DCB-\u0DCE\u0DD5\u0DD7\u0DE0-\u0DF1\u0DF5-\u0E00\u0E3B-\u0E3E\u0E5C-\u0E80\u0E83\u0E85\u0E86\u0E89\u0E8B\u0E8C\u0E8E-\u0E93\u0E98\u0EA0\u0EA4\u0EA6\u0EA8\u0EA9\u0EAC\u0EBA\u0EBE\u0EBF\u0EC5\u0EC7\u0ECE\u0ECF\u0EDA\u0EDB\u0EE0-\u0EFF\u0F48\u0F6D-\u0F70\u0F98\u0FBD\u0FCD\u0FDB-\u0FFF\u10C6\u10C8-\u10CC\u10CE\u10CF\u1249\u124E\u124F\u1257\u1259\u125E\u125F\u1289\u128E\u128F\u12B1\u12B6\u12B7\u12BF\u12C1\u12C6\u12C7\u12D7\u1311\u1316\u1317\u135B\u135C\u137D-\u137F\u139A-\u139F\u13F5-\u13FF\u169D-\u169F\u16F1-\u16FF\u170D\u1715-\u171F\u1737-\u173F\u1754-\u175F\u176D\u1771\u1774-\u177F\u17DE\u17DF\u17EA-\u17EF\u17FA-\u17FF\u180F\u181A-\u181F\u1878-\u187F\u18AB-\u18AF\u18F6-\u18FF\u191D-\u191F\u192C-\u192F\u193C-\u193F\u1941-\u1943\u196E\u196F\u1975-\u197F\u19AC-\u19AF\u19CA-\u19CF\u19DB-\u19DD\u1A1C\u1A1D\u1A5F\u1A7D\u1A7E\u1A8A-\u1A8F\u1A9A-\u1A9F\u1AAE-\u1AFF\u1B4C-\u1B4F\u1B7D-\u1B7F\u1BF4-\u1BFB\u1C38-\u1C3A\u1C4A-\u1C4C\u1C80-\u1CBF\u1CC8-\u1CCF\u1CF7-\u1CFF\u1DE7-\u1DFB\u1F16\u1F17\u1F1E\u1F1F\u1F46\u1F47\u1F4E\u1F4F\u1F58\u1F5A\u1F5C\u1F5E\u1F7E\u1F7F\u1FB5\u1FC5\u1FD4\u1FD5\u1FDC\u1FF0\u1FF1\u1FF5\u1FFF\u200B-\u200F\u202A-\u202E\u2060-\u206F\u2072\u2073\u208F\u209D-\u209F\u20BB-\u20CF\u20F1-\u20FF\u218A-\u218F\u23F4-\u23FF\u2427-\u243F\u244B-\u245F\u2700\u2B4D-\u2B4F\u2B5A-\u2BFF\u2C2F\u2C5F\u2CF4-\u2CF8\u2D26\u2D28-\u2D2C\u2D2E\u2D2F\u2D68-\u2D6E\u2D71-\u2D7E\u2D97-\u2D9F\u2DA7\u2DAF\u2DB7\u2DBF\u2DC7\u2DCF\u2DD7\u2DDF\u2E3C-\u2E7F\u2E9A\u2EF4-\u2EFF\u2FD6-\u2FEF\u2FFC-\u2FFF\u3040\u3097\u3098\u3100-\u3104\u312E-\u3130\u318F\u31BB-\u31BF\u31E4-\u31EF\u321F\u32FF\u4DB6-\u4DBF\u9FCD-\u9FFF\uA48D-\uA48F\uA4C7-\uA4CF\uA62C-\uA63F\uA698-\uA69E\uA6F8-\uA6FF\uA78F\uA794-\uA79F\uA7AB-\uA7F7\uA82C-\uA82F\uA83A-\uA83F\uA878-\uA87F\uA8C5-\uA8CD\uA8DA-\uA8DF\uA8FC-\uA8FF\uA954-\uA95E\uA97D-\uA97F\uA9CE\uA9DA-\uA9DD\uA9E0-\uA9FF\uAA37-\uAA3F\uAA4E\uAA4F\uAA5A\uAA5B\uAA7C-\uAA7F\uAAC3-\uAADA\uAAF7-\uAB00\uAB07\uAB08\uAB0F\uAB10\uAB17-\uAB1F\uAB27\uAB2F-\uABBF\uABEE\uABEF\uABFA-\uABFF\uD7A4-\uD7AF\uD7C7-\uD7CA\uD7FC-\uF8FF\uFA6E\uFA6F\uFADA-\uFAFF\uFB07-\uFB12\uFB18-\uFB1C\uFB37\uFB3D\uFB3F\uFB42\uFB45\uFBC2-\uFBD2\uFD40-\uFD4F\uFD90\uFD91\uFDC8-\uFDEF\uFDFE\uFDFF\uFE1A-\uFE1F\uFE27-\uFE2F\uFE53\uFE67\uFE6C-\uFE6F\uFE75\uFEFD-\uFF00\uFFBF-\uFFC1\uFFC8\uFFC9\uFFD0\uFFD1\uFFD8\uFFD9\uFFDD-\uFFDF\uFFE7\uFFEF-\uFFFB\uFFFE\uFFFF]/g;

			if (timeArray[2] === undefined) {
				if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) > 12) {
					timeArray[2] = "PM";
					if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) > 13) {
						var hmsArray = timeArray[1].split(":");
						hmsArray[0] = parseInt(hmsArray[0].replace(re, ''), 10) - 12;
						timeArray[1] = hmsArray.join(":");
					}
				} else {
					timeArray[2] = "AM";
					if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) == 0) {
						var hmsArray = timeArray[1].split(":");
						hmsArray[0] = "12";
						timeArray[1] = hmsArray.join(":");
					}
				}
			}
			date += timeArray[1] + " " + timeArray[2];
			return date;
		},

		onPressUpdateTask: function () {
			this.setTaskPanelButtonsEnablement(false); //AN: #obxUAT
			var isInvestigation = this.getModel("dashBoardModel").getProperty("/isInvestigationCreate");
			var isInquiry = this.getModel("dashBoardModel").getProperty("/isInquiryCreate"); //AN: #inquire
			// if ((!isInvestigation || !isInquiry) && this.onCheckOptionalTasksSelected() === false) { //AN: #inquire(MIGHT BE UNCOMMENTED)
			// 	return;
			// }
			var bDispatch = false; //AN: #shiftExists
			var oData = this.getModel("oTaskPanelDetailModel").getData();
			var LocationCode = oData.taskEventDto.locationCode;
			var taskOrgin = oData.taskEventDto.origin;
			var taskParentOrgin = oData.taskEventDto.parentOrigin; //AN: #msgToROC
			var prevTask = oData.taskEventDto.prevTask; //AN: #msgToROC
			if (taskOrgin != "Dispatch") {
				if (!isInquiry) { //AN: #pw
					this._checkNewUpdateCheckList();
				}
			}
			var sUrl = "/taskmanagementRest/tasks/updateTask";
			var dashBoardModel = this.getModel("dashBoardModel");
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true); //AN: #inquire
			if (dashBoardModel.getProperty("/assignedToIndex") !== null) {
				var currObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
				oData.taskEventDto.owners = [];
				if (isInvestigation || isInquiry) { //AN: #inquire
					// if (isInquiry && dashBoardModel.getData().userData.isENG && oData.taskEventDto.group.includes("Engineer") && oData.taskEventDto.origin ===
					// 	"Inquiry" && oData.taskEventDto.parentOrigin === "Inquiry") { //AN: #inquiryEnhancement
					if (isInquiry && oData.taskEventDto.origin === "Inquiry" && oData.taskEventDto.parentOrigin === "Inquiry") { //AN: #inquiryEnhancement
						if (currObj && currObj.length === 0) { //AN: #inquiryEnhancement
							currObj = dashBoardModel.getProperty("/suggestionItems");
							oData.taskEventDto.isGroupTask = "true";
						} else {
							oData.taskEventDto.isGroupTask = "false";
						}
					}
					for (var i = 0; i < currObj.length; i++) {
						var lastName = currObj[i].lastName;
						var firstName = currObj[i].firstName;
						if (lastName) {
							firstName = firstName + " " + lastName;
						}
						oData.taskEventDto.owners.push({
							"taskOwner": currObj[i].userId,
							"taskOwnerDisplayName": firstName,
							"ownerEmail": currObj[i].userId,
							"pId": currObj[i].pId
						});
					}
				} else {
					bDispatch = true; //AN: #shiftExists
					for (var i = 0; i < currObj.length; i++) {
						var lastName = currObj[i].lastName;
						var firstName = currObj[i].firstName;
						if (lastName) {
							firstName = firstName + " " + lastName;
						}
						oData.taskEventDto.owners.push({
							"taskOwner": currObj[i].emailId,
							"taskOwnerDisplayName": firstName,
							"ownerEmail": currObj[i].emailId,
							"pId": currObj[i].pId
						});
					}
				}
			}
			if (this.getModel("dashBoardModel").getProperty("/pwHopperUpdate")) {
				oData.updateType = "CheckList";
			} else {
				if (oData.taskEventDto.owners.length === 0 || (!oData.taskEventDto.owners)) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					this._showToastMessage("Select the Assignee to the task");
					this.setTaskPanelButtonsEnablement(true); //AN: #obxUAT
					return;
				}
			}
			var arrUsers = oData.taskEventDto.owners;
			var arrOwners = [];
			for (var j = 0; j < arrUsers.length; j++) {
				arrOwners.push(arrUsers[j].taskOwnerDisplayName.split(" ").join(""));
			}
			if (oData.taskEventDto.parentOrigin === "Pigging" && oData.taskEventDto.createdByDisplay === "SYSTEM USER") {
				// dashBoardModel.setProperty("/piggingTask/isSystemTask", true);
				dashBoardModel.setProperty("/piggingTask", {
					isSystemTask: true
				});
				if (oData.customAttr) { //AN: #piggingChange (To change the status from "New" to "Assigned" for the system generated task)
					for (var c = 0; c < oData.customAttr.length; c++) {
						if (oData.customAttr[c].label === "Status" && oData.customAttr[c].labelValue === "NEW") {
							oData.customAttr[c].labelValue = "ASSIGNED";
							break;
						}
					}
				}
			}
			oData.taskEventDto.createdBy = this._getCurrentLoggedInUser();
			oData.taskEventDto.createdByDisplay = dashBoardModel.getProperty("/userData/displayName");
			oData.loggedInUserGrp = dashBoardModel.getProperty("/userData/sGroup");
			if (isInquiry) { //AN: #inquire
				for (var aG = 0; aG < oData.observationCustomAttr.length; aG++) {
					if (oData.observationCustomAttr[aG].label === "Assign to group") {
						oData.forwardToGrp = oData.observationCustomAttr[aG].labelValue;
					}
				}
			}
			if (oData.checkList) {
				var userRole = oData.userType;
				if (userRole.search("WW") >= 0 || userRole.search("RE") >= 0 || userRole.search("ALS") >= 0) {
					oData.userType = "ENG";
				}
			}

			this.doAjax(sUrl, "POST", oData, function (oData) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this.getModel("dashBoardModel").setProperty("/pwHopperUpdate", false);
					this.setTaskPanelButtonsEnablement(true); //AN: #obxUAT
					if (oData.statusCode === "0") {
						// if (dashBoardModel.getProperty("/piggingTask/isSystemTask")) { //AN: #shiftExists
						// 	this.sendNotification("You have a new task assigned", arrUsers, true);
						// }
						if (bDispatch) { //AN: #shiftExists
							var aOwnersNotif = [];
							for (var i = 0; i < arrUsers.length; i++) {
								var sEmail = "";
								if (arrUsers[i].ownerEmail) {
									sEmail = arrUsers[i].ownerEmail;
								} else if (arrUsers[i].taskOwner) {
									sEmail = arrUsers[i].taskOwner;
								}
								if (sEmail) {
									var oUrl = "/taskmanagementRest/ShiftRegister/getShiftDetails?emp_email=" + sEmail;
									var oModel = new sap.ui.model.json.JSONModel();
									oModel.loadData(oUrl, "", false, "GET", false, false);
									if (oModel.getData()) {
										aOwnersNotif.push(arrUsers[i]);
									}
								}
							}
							if (aOwnersNotif && aOwnersNotif.length > 0) {
								this.sendNotification("You have a new task assigned", aOwnersNotif, true);
							}
						}
						if (this.getModel("dashBoardModel").getProperty("/isTaskListDailogOpen")) {
							if (this.getModel("dashBoardModel").getProperty("/isCreateTaskFromRightSidePanel")) {
								var aSelctedObject = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
								var locationCode = aSelctedObject[0].location;
								this.checkForCreateTask(locationCode, " ", true);
							} else {
								this.checkForCreateTask(LocationCode, " ", true);
							}
						}
						this._showToastMessage(oData.message);
						var currentSelectKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"); //AN: #pw
						this.onSideNavigationItemSelect(currentSelectKey); //AN: #pw
						if (isInvestigation) {
							this.onCreateInvestigationClose();
						} else if (isInquiry) { //AN: #inquire
							this.onCreateInquiryClose();
						} else {
							// change in createdby required.
							//chek if pigging update happened for the SYSTEM USER, then close the task.
							this.onCreateTaskPanelClose();
						}
						if (taskParentOrgin === "Message" && prevTask) { //AN: #msgToROC: if message is resolved then move the msg to IN-PROGRESS
							var oUrl = "/taskmanagementRest/message/getMessage?messageId=" + prevTask;
							var oModel = new sap.ui.model.json.JSONModel();
							oModel.loadData(oUrl, null, false);
							var msgResultData = oModel.getData();
							if (msgResultData && msgResultData.responseMessage && msgResultData.responseMessage.statusCode === "0" && msgResultData.message &&
								msgResultData.message.status.toLowerCase() === "resolved") {
								msgResultData.message.status = "IN PROGRESS";
								// WorkbenchHelper.fnWBMsgUpdateStatus(this, this.getModel("dashBoardModel"), "IN PROGRESS", false, msgResultData.message);
								WorkbenchHelper.fnWBMsgUpdateStatus(this, this.getModel("dashBoardModel"), "ASSIGNED", false, msgResultData.message); //SH: NewFlow
							}
						}

					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this.getModel("dashBoardModel").setProperty("/pwHopperUpdate", false);
					this.setTaskPanelButtonsEnablement(true); //AN: #obxUAT
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		onClickDownloadLink: function (oEvent) {
			var downloadObj = {};
			if (oEvent) {
				var oContext = oEvent.getSource().getBindingContext("oTaskPanelDetailModel").getObject().attachmentDetails["0"];
				var currIndex = oEvent.getSource().getBindingContext("oTaskPanelDetailModel").getPath().split("/").pop();
				if (oContext.fileDoc) {
					downloadObj.base64 = oContext.fileDoc;
					downloadObj.filename = oContext.fileName;
					downloadObj.fileType = oContext.fileType;
					this.downloadFile(downloadObj);
				} else {
					this.getFileDoc(oContext.fileId, oContext, currIndex, true);
				}
			}
		},

		downloadFile: function (downloadObj) {
			var Base64, filename, fileType;
			Base64 = downloadObj.base64;
			filename = downloadObj.filename;
			fileType = downloadObj.fileType;
			var u8_2 = new Uint8Array(atob(Base64).split("").map(function (c) {
				return c.charCodeAt(0);
			}));
			var blob = new Blob([u8_2], {
				type: fileType
			});
			if (window.navigator.msSaveOrOpenBlob) {
				window.navigator.msSaveOrOpenBlob(blob, filename);
			} else {
				var a = document.createElement("a");
				a.setAttribute("style", "display: none");
				setTimeout(function () {
					document.body.appendChild(a);
					try {
						var url = window.URL.createObjectURL(blob);
						a.href = url;
						a.download = filename;
						a.click();
						window.URL.revokeObjectURL(url);
					} catch (e) {}
				}, 100);
			}
		},
		onSubClassificationChange: function (oEvent) {
			// Create Task enhancement
			var dashBoardModel = this.getModel("dashBoardModel");
			var sSubKey = this.getModel("dashBoardModel").getData().taskSubject1;
			var aSelectedKeys, sKey;
			if (sSubKey !== "Downtime") {
				aSelectedKeys = oEvent.getSource().getSelectedKey();
				this.getModel("dashBoardModel").setProperty("/taskSubject2", "");
				this.getModel("dashBoardModel").setProperty("/taskSubject3", aSelectedKeys);
				this.getModel("dashBoardModel").setProperty("/taskSubject3Array", aSelectedKeys);
			} else {
				aSelectedKeys = oEvent.getSource().getSelectedKey();
				this.getModel("dashBoardModel").setProperty("/taskSubject2", aSelectedKeys);
				this.getModel("dashBoardModel").setProperty("/taskSubject3", "");
			}
			var sClassification = this.getModel("dashBoardModel").getData().taskSubject1;
			var sSubClassification = aSelectedKeys;
			if (this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status") === "NEW") {
				this.getTaskFLSOP(sClassification, sSubClassification);
			}
			if (dashBoardModel.getProperty("/taskCreation") === "Custom") { //AN: Custom dispatch creation
				sKey = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject[0].location;
			} else if (dashBoardModel.getData().dynamicContentType.from === "Inquiry" && dashBoardModel.getData().dynamicContentType.taskType ===
				"Dispatch") { //AN: dispatch creation from inquiry
				sKey = dashBoardModel.getData().dynamicContentType.inquiryLocationCode;
			} else if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "DOP" && !dashBoardModel.getProperty(
					"/taskCreation")) { //AN: dispatch creation from DOP
				sKey = dashBoardModel.getData().currDopObject.locationCode;
			} else if ((dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "fracMonitoring" || dashBoardModel.getProperty(
					"/panelProperties/currentSelectKey") === "alarms") && !dashBoardModel.getProperty("/taskCreation")) { //AN: dispatch creation from Frac or Alarms
				sKey = dashBoardModel.getData().currAlarmObject.locationCode;
			}
			var taskOwner;
			var sItems = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
			if (sItems.length > 0) {
				taskOwner = sItems[0].emailId;
			}
			if (sSubClassification && sKey && taskOwner) {
				this.getTaskStartDate(taskOwner, sKey, sSubClassification);
			}
			if (!oEvent && sSubClassification && sKey) {
				TaskManagementHelper.getAvailableOperators(this, sClassification, sSubClassification); //AN: #operatorsAvailable
			}
			var oData = this.getModel("oTaskPanelDetailModel").getData();
			if (oEvent) {
				if ((sSubKey === "Operations" && aSelectedKeys === "Facility Walkthrough") && dashBoardModel.getProperty("/startDateTime")) {
					var oCurrDate = new Date();
					oCurrDate = oCurrDate + "";
					var aCurrentDate = oCurrDate.split(" ");
					var sTime = aCurrentDate[4];
					var aTime = sTime.split(":");
					if (aTime[0] >= 5) {
						var nDate = new Date();
						nDate.setDate(nDate.getDate() + 1);
					} else {
						nDate = new Date();
					}
					nDate.setHours("05", "00", "00");
					var aDateString = nDate.toDateString().split(" ");
					var sLabelValueFormat = aDateString[2] + "-" + aDateString[1] + "-" + aDateString[3].substring(2) + ", " + nDate.toLocaleTimeString();
					// dashBoardModel.setProperty("/newDispatchDateValue", nDate);
					// dashBoardModel.setProperty("/futureTask", true);
					//var startDateVale = dashBoardModel.getProperty("/newDispatchDateValue");
					// oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = sLabelValueFormat;
					// oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = startDateVale;
					// dashBoardModel.setProperty("/startDateTimeLablevalue", sLabelValueFormat);
					// dashBoardModel.setProperty("/startDateTimeValueState", "None");
				} else {
					/*dashBoardModel.setProperty("/newDispatchDateValue", "");
					dashBoardModel.setProperty("/futureTask", false);
					dashBoardModel.setProperty("/startDateTimeLablevalue", "");
					dashBoardModel.setProperty("/startDateTimeValueState", "None");
					oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = "";
					oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = "";*/
				}
			}

		},

		handleLocationSuggest: function (oEvent) {
			var sTerm = oEvent.getParameter("suggestValue");
			var aFilters = [];
			if (sTerm) {
				aFilters.push(new sap.ui.model.Filter("location", sap.ui.model.FilterOperator.Contains, sTerm));
			}
			oEvent.getSource().getBinding("suggestionItems").filter(aFilters);
		},

		onDeletingAttachment: function (oEvent) {
			var isCreateTask = this.getModel("dashBoardModel").getProperty("/isCreateTask");
			var oContext = oEvent.getSource().getBindingContext("oTaskPanelDetailModel");
			var taskPanelDetailModel = this.getModel("oTaskPanelDetailModel").getData();
			if (isCreateTask) {
				var index = oContext.getPath().split("/").pop();
				var oData = taskPanelDetailModel.collabrationDtos;
				oData.splice(index, 1);
				this.getModel("oTaskPanelDetailModel").refresh();
			} else {
				var currStatus = taskPanelDetailModel.taskEventDto.status;
				var attStatus = oContext.getObject().status;
				if (currStatus === attStatus) {} else {
					this._showToastMessage("You cannot delete this attachment");
				}
			}
		},

		getLocGroup: function (currLoc, locType) {
			var sUrl = "/taskmanagementRest/location/getField?location=" + currLoc + "&locType=" + locType;
			var oGroup = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead").split(",");
			var res = "";
			var oModel = new sap.ui.model.json.JSONModel();
			this.getView().setModel(oModel, "oModel");
			oModel.loadData(sUrl, false, "GET", false, false);
			oModel.attachRequestCompleted(function (oEvent) {
				var oData = oEvent.getSource().getData();
				if (oData && oData.field) {
					var sLoc = oData.field;
					if (sLoc === "TILDEN WEST" || sLoc === "TILDEN NORTH") {
						sLoc = "WestTilden";
					} else if (sLoc === "TILDEN CENTRAL" || sLoc === "TILDEN EAST") {
						sLoc = "CentralTilden";
					} else if (sLoc === "CATARINA") {
						sLoc = "Catarina";
					} else if (sLoc === "KARNES") {
						sLoc = "Karnes";
					}
					for (var i = 0; i < oGroup.length; i++) {
						if (oGroup[i].search(sLoc) >= 0) {
							res = oGroup[i];
						}
					}
					this.getModel("dashBoardModel").setProperty("/userData/group", res);
				}
			}.bind(this));
			if (res !== "") {
				return res;
			} else {
				return false;
			}
		},

		onSearchAlarmData: function (oEvent) {
			var value = oEvent.getSource().getValue();
			var oAppContId = this.getView().createId("appscollectioncontainer");
			var oTaskList = sap.ui.getCore().byId(oAppContId + "--alarmList--idAlarmList");
			var aFilters;
			var filterParams = ["time", "description", "classification", "tierLevel", "route", "alarmCondition", "value"];
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTaskList.getBinding("items").filter(aFilters);
		},

		onAlarmSelectionChange: function (oEvent) {
			var selectedItems = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList").getSelectedItems();
			var length = selectedItems.length;
			var bDispatch = sap.ui.getCore().byId("idAlarmActionList--idDispatchBtn");
			var bDesignate = sap.ui.getCore().byId("idAlarmActionList--idDesignateBtn");
			var bTrends = sap.ui.getCore().byId("idAlarmActionList--idTrendBtn");
			var bAcknowledge = sap.ui.getCore().byId("idAlarmActionList--idAcknowledgeBtn");
			var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole"); //SH: WebReadOnly
			//SH: Modules readonly
			var isAlarmReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isAlarmReadOnly");
			var isDOPReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isDOPReadOnly");
			var currentSelectTab = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var dispatchVis = !isReadOnly ? !isDOPReadOnly && currentSelectTab === "DOP" ? true : !isAlarmReadOnly && currentSelectTab ===
				"alarms" ? true : false : false;

			if (length === 0 || length === 1) {
				var currObj;
				if (length === 0) {
					currObj = oEvent.getBindingContext("oReportDetailModel").getObject();
				} else {
					currObj = selectedItems[0].getBindingContext("oReportDetailModel").getObject();
				}
				this.getModel("dashBoardModel").setProperty("/currAlarmObject", currObj);
				if (!currObj.muwi) {
					bDesignate.setVisible(false);
					bTrends.setVisible(false);
				} else {
					if (!isReadOnly && !isAlarmReadOnly) {
						bDesignate.setVisible(true);
					}
					bTrends.setVisible(true);
				}
				if (!currObj.locationCode) {
					bDispatch.setVisible(false);
				} else {
					if (dispatchVis) {
						bDispatch.setVisible(true);
					}
				}
				if (!isReadOnly && !isAlarmReadOnly) {
					bAcknowledge.setVisible(true);
				}
			} else {
				if (!isReadOnly) {
					bAcknowledge.setVisible(true);
				}
				bDispatch.setVisible(false);
				bDesignate.setVisible(false);
				bTrends.setVisible(false);
			}
		},

		onAlarmSelectionClear: function () {
			var oAlarmList = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList");
			oAlarmList.removeSelections();
		},

		onPressAlarmAction: function (oEvent) {
			var action = oEvent.getSource().getText();
			var oTaskList = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList");
			var selectedItems = oTaskList.getSelectedItems();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeSeat
			var selectedAlarms = [];
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "alarms") {
				if (selectedItems.length === 1 || selectedItems.length === 0) {
					var currObj;
					if (selectedItems.length === 1) {
						currObj = selectedItems[0].getBindingContext("oReportDetailModel").getObject();
						this.getModel("dashBoardModel").setProperty("/currAlarmObject", currObj);
					} else {
						currObj = this.getModel("dashBoardModel").getProperty("/currAlarmObject");
						selectedAlarms.push(currObj);
					}
					if (currObj.downTimeClassifier !== "Downtime" && currObj.downTimeClassifier !== "Non Downtime" && action !== "Trends" &&
						action !==
						"Acknowledge") {
						this._showToastMessage("Select the classification");
						return;
					}
				} else {
					if (action === "Acknowledge") {
						for (var i = 0; i < selectedItems.length; i++) {
							var Obj = selectedItems[i].getBindingContext("oReportDetailModel").getObject();
							if (Obj.downTimeClassifier !== "Downtime" && Obj.downTimeClassifier !== "Non Downtime") {
								this._showToastMessage("Select classifications for the selected alarms");
								return;
							}
						}
					}
				}
				if (action === "Location History") { //RV: Location History Changes
					this.getModel("dashBoardModel").setProperty("/panelProperties/currentSelectKey", "locHistory");
					this.setSelectedModule(); //AN: #Notif
					this.getView().getModel("dashBoardModel").setProperty("/locationHistorySelectTabs/iconFilterTab", "Task");
					this.setSelectedLocationExternally(oDashBoardModel.getProperty("/currAlarmObject")); //AN: #Notif
					this._setModuleWiseScrollSizes("locHistory");
				}
			}
			if (action === "Designate") {
				this.onDesignatePress();
			} else if (action === "Dispatch") {
				this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
				this.onDispatchPress();
			} else if (action === "Trends") {
				this.onTrendsPress();
			} else if (action === "Acknowledge") {
				var aTaskTypeDetails = [];
				if (selectedItems.length !== 0) {
					for (var i = 0; i < selectedItems.length; i++) {
						currObj = selectedItems[i].getBindingContext("oReportDetailModel").getObject();
						selectedAlarms.push(currObj);
						var oTaskTypeDetails = { //SK: Check authorization for alarm acknowledge // Multi selected alarms
							oCurrSelectedLocation: currObj,
							sLocCode: currObj.locationCode,
							sLocType: currObj.locationType
						};
						aTaskTypeDetails.push(oTaskTypeDetails);
					}
				} else {
					if (selectedAlarms) {
						var oSelectedAlarm = selectedAlarms[0];
						var oTaskTypeDetails = { //SK: Check authorization for alarm acknowledge // right clcik alarm
							oCurrSelectedLocation: oSelectedAlarm,
							sLocCode: oSelectedAlarm.locationCode,
							sLocType: oSelectedAlarm.locationType
						};
						aTaskTypeDetails.push(oTaskTypeDetails);
					}
				}
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Alarm-Acknowledge", "ROC", selectedAlarms); //SK: Check authorization for alarm acknowledge
				//this.onAcknowledgePress(selectedAlarms);
			} else if (action === "Inquiry") { //AN: #inquire
				var oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currDopObject"),
					sLocCode: oDashBoardModel.getProperty("/currDopObject/locationCode"),
					sLocType: oDashBoardModel.getProperty("/currDopObject/locationType"),
					sLocText: oDashBoardModel.getProperty("/currDopObject/location")
				};
				var aTaskTypeDetails = []; //AN: #ChangeSeat
				aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Module-Inquiry", "ROC", oEvent); //AN: #ChangeSeat
			} else if (oDashBoardModel.getProperty("/currDopObject") && action === "Location History") { //RV: Location History Changes
				this.getModel("dashBoardModel").setProperty("/panelProperties/currentSelectKey", "locHistory");
				this.setSelectedModule(); //AN: #Notif
				this.getView().getModel("dashBoardModel").setProperty("/locationHistorySelectTabs/iconFilterTab", "Task");
				this.setSelectedLocationExternally(oDashBoardModel.getProperty("/currDopObject")); //AN: #Notif
			}
			this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
		},

		onDispatchPress: function () {
			var alarmCreate = true;
			this.oBusyInd.open();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeSeat
			var aTaskTypeDetails = [], //AN: #ChangeSeat
				oTaskTypeDetails = {}; //AN: #ChangeSeat
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP") {
				oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currDopObject"),
					alarmCreate: alarmCreate,
					sLocCode: oDashBoardModel.getProperty("/currDopObject/locationCode"),
					sLocType: oDashBoardModel.getProperty("/currDopObject/locationType"),
					sLocText: oDashBoardModel.getProperty("/currDopObject/location")
				};
				aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
			} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "alarms") {
				oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currAlarmObject"),
					alarmCreate: alarmCreate,
					sLocCode: oDashBoardModel.getProperty("/currAlarmObject/locationCode"),
					sLocType: oDashBoardModel.getProperty("/currAlarmObject/locationType"),
					sLocText: ""
				};
				aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
			} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "fracMonitoring") {
				oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currAlarmObject"),
					alarmCreate: alarmCreate,
					sLocCode: oDashBoardModel.getProperty("/currAlarmObject/locationCode"),
					sLocType: oDashBoardModel.getProperty("/currAlarmObject/locationType"),
					sLocText: oDashBoardModel.getProperty("/currAlarmObject/location")
				};
				aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
			} else {
				//this.onCreateTaskPress("", "", alarmCreate);
			}
			ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Module-Dispatch", "ROC"); //AN: #ChangeSeat
		},

		onAcknowledgePress: function (selectedAlarms) {
			var pointIds = this.getCommaSeperatedLocFromDto(selectedAlarms, "pointId");
			var oPayload = {
				"pointIds": pointIds,
				"key": "ACKNOWLEDGE",
				"value": "Y"
			};
			var sUrl = "/taskmanagementRest/alarmFeed/updateAcknowledge";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData.statusCode === "0") {
					this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
					this.getModel("dashBoardModel").setProperty("/prograssBarVisible", false);
					if (this.tick) {
						clearTimeout(this.tick);
						this.tick = false;
					}
					this._showToastMessage(oData.message);
					this.getAlarmData();
				} else {
					this._showToastMessage(oData.message);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},

		onTrendsPress: function (oEvent) {
			var oFragmentId = "powerBiPop",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.plotlyForProveAndFrac";
			if (!this.oPowerBiFragment) {
				this.oPowerBiFragment = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oPowerBiFragment);
				this.oPowerBiFragment.setEscapeHandler(function (o) {
					o.reject();
				});
			}
			this.getModel("dashBoardModel").setProperty("/plotlyDuration", 3);
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "fracMonitoring") {
				this.getModel("dashBoardModel").setProperty("/isFracMonitoring", true);
				this.getModel("dashBoardModel").setProperty("/isProve", false);
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/fracHitDate", null);
			} else if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "ndtpv") {
				this.getModel("dashBoardModel").setProperty("/isProve", true);
				this.getModel("dashBoardModel").setProperty("/isFracMonitoring", false);
				this.getModel("dashBoardModel").setProperty("/plotlyDuration", Number(this.getModel("dashBoardModel").getProperty(
					"/spotFireData/durationKey")));
			} else {
				this.getModel("dashBoardModel").setProperty("/isFracMonitoring", false);
				this.getModel("dashBoardModel").setProperty("/isProve", false);
			}
			this.oPowerBiFragment.open();
		},
		/**
		 * Function to capture "muwiId" & "type" of an investigation task, and to display the trends of an investigation
		 * @public
		 * @param {object} oEvnt - Contains Events of the control "PowerBI" after open
		 * Additional Info: 
		 * isTaskCardPress - If "Trends" button pressed after opening the task Card from task Management Panel 
		 * isIconPress - If "Trends" button pressed after clicking on icons(Investigation or Hopper) from PWHopper or DOP screen
		 * If not above, then on press of "Trends" button from right click on table(alarms, dop, frac, and prove) items
		 **/
		afterPoweBiPopupOpen: function (oEvnt) { //AN: #inquire
			var muwiId, type, taskMode;
			var dashBoardModel = this.getModel("dashBoardModel");
			if (this.getModel("dashBoardModel").getProperty("/isTaskCardPress")) {
				muwiId = this.getModel("dashBoardModel").getProperty("/taskCardData/muwiId");
				type = "INVESTIGATION7";
			} else if (this.getModel("dashBoardModel").getProperty("/isIconPress")) {
				muwiId = this.getModel("dashBoardModel").getProperty("/iconPressData/muwi");
				type = "INVESTIGATION7";
			} else {
				if (dashBoardModel.getProperty("/currAlarmObject")) {
					muwiId = dashBoardModel.getProperty("/currAlarmObject").muwi;
					type = "ALARM";
				}
				if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "ndtpv") {
					muwiId = dashBoardModel.getProperty("/currentProveObject/muwiId"); //AN: #inquire
					taskMode = dashBoardModel.getProperty("/spotFireData/durationKey"); //AN: #inquire
					type = taskMode === "7" ? "INVESTIGATION7" : "INVESTIGATION30";
				} else if (this.getModel("dashBoardModel").getProperty("/currDopObject") && this.getModel("dashBoardModel").getProperty(
						"/panelProperties/currentSelectKey") === "DOP") {
					muwiId = this.getModel("dashBoardModel").getProperty("/currDopObject").muwi;
					type = "VARIENCE";
					taskMode = dashBoardModel.getProperty("/dopData/durationKey");
				}
				if (dashBoardModel.getProperty("/isFracMonitoring")) {
					var oObj = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/currentRightClickContext");
					muwiId = oObj.wellCode;
					type = "FRAC";
				}
			}
			this.onLoadingPowerBiProperty(muwiId, type, taskMode);
		},

		onLoadingPowerBiProperty: function (muwiId, type, taskMode) {
			var sUrl = "/taskmanagementRest/powerBI/generateToken?type=" + type;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					this.powerBiControls.embedReport(oData, muwiId, type, taskMode);
				} else {
					this._showToastMessage(oData.responseMessage.message);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._showToastMessage(sErrorMessage);
			}.bind(this));
		},

		onPowerBiCancel: function () {
			this.getModel("dashBoardModel").setProperty("/isFracMonitoring", false);
			this.getModel("dashBoardModel").setProperty("/plotlyTableData", []);
			this.getModel("dashBoardModel").setProperty("/plotlyGraphData", []);
			this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyText", "");
			this.oPowerBiFragment.close();
			this.oPowerBiFragment.destroy();
			this.oPowerBiFragment = undefined;
		},

		onPowerBiFullscreen: function () {
			this.powerBiControls.fullscreen();
		},

		onDesignatePress: function (oEvent) {
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeSeat
			var aTaskTypeDetails = [], //AN: #ChangeSeat
				oTaskTypeDetails = {}; //AN: #ChangeSeat
			if (oDashBoardModel.getProperty("/panelProperties/currentSelectKey") === "downtime") {
				var currentObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
				var oGenModel = this.getModel("dashBoardModel");
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					// pattern: "YYYY-MM-dd"
					pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
				});
				var epochString = currentObj.createdAt;
				if (typeof epochString === "string") {
					var epochVal = parseInt(epochString);
				} else {
					epochVal = epochString;
				}
				var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
					pattern: "dd-MMM-yy hh:mm:ss aaa"
				});
				var convDate = new Date(epochVal);
				var epochToDate = oDateFormat.format(convDate);
				var aDate = epochToDate.split(" ");
				var aDateArray = aDate[0].split("-");
				var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
				sDate = sDate + " " + aDate[1] + " " + aDate[2];
				var dateFormatted = dateFormat.format(new Date(sDate));
				oGenModel.setProperty("/downTime/hourkey", currentObj.durationByRocHour);
				oGenModel.setProperty("/downTime/minuteKey", currentObj.durationByRocMinute);
				oGenModel.setProperty("/downTime/wellName", currentObj.well);
				oGenModel.setProperty("/downTime/downtimeParentKey", currentObj.downtimeCode);
				oGenModel.setProperty("/downTime/date", dateFormatted);
				oGenModel.setProperty("/downTime/dateUnFormatted", epochToDate);
				oGenModel.setProperty("/downTime/isReview", true);
				oGenModel.setProperty("/downTime/muwi", currentObj.muwi);
				oGenModel.setProperty("/downTime/id", currentObj.id);
				oGenModel.setProperty("/downTime/downtimeParentText", currentObj.downtimeText); //SH
				this.onDownTimeParentSelect(currentObj.downtimeCode + "_" + currentObj.childCode);
				var oFragmentId = "idDesignateFrag",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.designateView";
				if (!this.oDesignateFragment) {
					this.oDesignateFragment = this._createFragment(oFragmentId, oFragmentName);
					this.getView().addDependent(this.oDesignateFragment);
				}
				this.oDesignateFragment.open();
			} else if (oDashBoardModel.getProperty("/panelProperties/currentSelectKey") === "alarms") { //AN: #ChangeSeat
				oTaskTypeDetails = {
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currAlarmObject"),
					sLocCode: oDashBoardModel.getProperty("/currAlarmObject/locationCode"),
					sLocType: oDashBoardModel.getProperty("/currAlarmObject/locationType"),
					sLocText: ""
				};
				aTaskTypeDetails.push(oTaskTypeDetails);
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Alarm-Designate", "ROC");
			}
		},

		onDesignateClose: function () {
			this.oDesignateFragment.close();
			this.clearDownTime();
		},

		onDesignateSave: function () {
			var isDesignateCreate = true;
			this.onDownTimeSavePress("", isDesignateCreate);
		},
		onReportLoad: function () {
			var sUrl = "/taskmanagementRest/audit/getAudit";
			this.getModel("dashBoardModel").setProperty("/busyIndicators/detailReport", true);
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					this.getModel("dashBoardModel").setProperty("/reportList", oData.tasks);
					this.getModel("dashBoardModel").setProperty("/busyIndicators/detailReport", false);
				} else {
					this._showToastMessage(oData.responseMessage.message);
					this.getModel("dashBoardModel").setProperty("/busyIndicators/detailReport", true);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				this.getModel("dashBoardModel").setProperty("/busyIndicators/detailReport", true);
			}.bind(this));
		},

		onRightPress: function (oEvent) {
			var oContext = oEvent.getParameters();
			if (oContext.src.getBindingContext("oReportDetailModel") && this.getModel("dashBoardModel").getProperty("/alarmDetailKey") !==
				"Acknowledged") {
				this.getModel("dashBoardModel").setProperty("/alarmRefresh", false);
				this.onOpenAlarmAction(oContext.src, oContext.target);
			}
		},

		onOpenAlarmAction: function (oContext, sTarget) {
			var oFragmentId = "idAlarmActionList",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.alarmActionList";
			if (!this.alarmActionList) {
				this.alarmActionList = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.alarmActionList);
			}
			this.onAlarmSelectionChange(oContext);
			this.alarmActionList.setOffsetX(-30);
			this.alarmActionList.openBy(sTarget);
		},
		getAlarmData: function () {
			var oAlarmList = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList");
			oAlarmList.setMode("MultiSelect");
			var sUrl = "/taskmanagementRest/alarmFeed/getAlarm";
			var selHier = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			if (selHier === "SEARCH") {
				selHier = "Well";
			}
			var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			var isBase = false; //SH: Location Hierarchy - Alarms cant be fetched for Base location
			for (var i = 0; i < selectedLoc.length; i++) {
				if (selectedLoc[i].locationText === "Canada" || selectedLoc[i].locationText === "United States") {
					isBase = true;
				}
			}
			if (!isBase) {
				if (selectedLoc.length === 0) {
					this.getModel("dashBoardModel").setProperty("/screenSize/alarmListHeight", (sap.ui.Device.resize.height - 195) + "px");
					this.getModel("oReportDetailModel").setProperty("/alarmList", []);
					return;
				}
				var locations = this.getCommaSeperatedLocFromDto(selectedLoc, "location");
				var oPayload = {
					"locationType": selHier,
					"locations": locations,
					"acknowledged": false,
					"page": this.getModel("dashBoardModel").getProperty("/selectedPage")
				};
				if (this.getModel("dashBoardModel").getProperty("/alarmDetailKey") === "Acknowledged") {
					oAlarmList.setMode("None");
					oPayload.acknowledged = true;
				}
				this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.responseMessage && oData.responseMessage.statusCode === "0") {
						if (this.alarmActionList && this.alarmActionList.isOpen()) {
							this.alarmActionList.close();
						}
						var oAckData = this.getView().getModel("dashBoardModel").getData().highlightAlarms; //AN: #Notif
						try { //AN: #highlightShift
							if (oAckData && oData.alarmsList && oData.alarmsList.length > 0) {
								var oScroll = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmsScroll");
								var oTable = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList");
								for (var k = 0; k < oAckData.length; k++) { //AN: #Notif
									for (var j = 0; j < oData.alarmsList.length; j++) {
										if (oAckData[k] == oData.alarmsList[j].pointId) {
											oData.alarmsList[j].alarmsNotification = "notified";
											// var sItem = oTable.getItems()[j]; #AN: //Commented due to bug in prod -sprint 5
											// oScroll.scrollToElement(sItem); #AN: //Commented due to bug in prod -sprint 5
											break;
										}
									}
								}
							}
						} catch (e) {

						}
						this.getModel("oReportDetailModel").setProperty("/alarmList", oData.alarmsList);
						this.getModel("oReportDetailModel").setProperty("/alarmListOriginal", $.extend(true, [], oData.alarmsList)); //AN: #TimeZone 
						this.getModel("oReportDetailModel").setProperty("/paginatedAlarmList", oData.alarmsList);
						this.getModel("dashBoardModel").setProperty("/alarmCount", oData.totalCount);
						this.getModel("dashBoardModel").setProperty("/alarmPageCount", oData.pageCount);
						if (oData.alarmsList === null)
							sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPageNumberDiv").setVisible(false);
						else
							sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPageNumberDiv").setVisible(true);
						this.checkForPaginationButton();
						this.onAlarmSelectionClear();
						this.getModel("dashBoardModel").setProperty("/busyIndicators/alarmListBusy", false);
					} else {
						if (oData.responseMessage) {
							this._showToastMessage(oData.responseMessage.message);
						}
					}
				}.bind(this), function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					if (this.oConfirmDialog === undefined || !this.oConfirmDialog.isOpen()) {
						if (oError.getId() === "requestFailed" && oError.getParameter("message") === "error" && oError.getParameter(
								"statusText") ===
							"error" && oError.getParameter("statusCode") === 0) {
							//do Nothing Handle net::ERR_NETWORK_IO_SUSPENDED
						} else if (oError.getId() === "requestFailed" && oError.getParameter("message") === "parsererror" &&
							oError.getParameter("statusText") === "parsererror" && oError.getParameter("statusCode") === 200) { //AN: #parseError
							this._createConfirmationMessage("Error", "The user session has timed out. Please refresh the page", "Error", "", "Close",
								false,
								null);
							//html error due to session timeout - msg display
						} else {
							this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
						}
					}
				}.bind(this));
			} else {
				var sErrorMessage = "Alarms cannot be fetched for base location";
				sap.m.MessageToast.show(sErrorMessage);
			}
		},

		/**
		 * Method is called initially to create the pagination.
		 *
		 */
		generatePagination: function () {
			var oDefaultDataModel = this.getModel("dashBoardModel");
			var totalTasks = parseInt(oDefaultDataModel.getProperty("/alarmCount"), 10);
			var tasksPerPage = 10;
			var oAppContId = this.getView().createId("alarmFragment");
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPrevButton").setVisible(false);
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idNextButton").setVisible(true);
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idFirst25").setVisible(true);
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idFirst50").setVisible(true);
			sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idFirst100").setVisible(true);

			var pageCount = parseInt(totalTasks / tasksPerPage); //tasksPerPage
			if (totalTasks % tasksPerPage !== 0) {
				pageCount = pageCount + 1;
			}
			oDefaultDataModel.setProperty("/numberOfPages", pageCount);
			var array = [];
			if (pageCount > 5) {
				pageCount = 5;
			} else {
				sap.ui.getCore().byId(oAppContId + "--idNextButton").setEnabled(false);
			}
			for (var i = 1; i <= pageCount; i++) {
				var object = {
					"text": i
				};
				array.push(object);
			}
			this.getModel("dashBoardModel").setProperty('/pageArray', array);
			sap.ui.getCore().byId(oAppContId + "idCurrentPage").setText("Page : " + oDefaultDataModel.getProperty(
				"/selectedPage"));
			if (oDefaultDataModel.getProperty("/numberOfPages") && parseInt(oDefaultDataModel.getProperty("/numberOfPages")) > 1) {
				sap.ui.getCore().byId(oAppContId + "idPageNumberDiv").setVisible(true);
			} else {
				sap.ui.getCore().byId(oAppContId + "idPageNumberDiv").setVisible(true);
			}
		},

		/**
		 * Method is called when user clicks on the previous button in the pagination.
		 * @param: event - search event.
		 * 
		 */
		onScrollLeft: function () {
			var oDefaultDataModel = this.getModel("dashBoardModel");
			var oAppContId = this.getView().createId("alarmFragment");
			sap.ui.getCore().byId(oAppContId + "idPrevButton").setVisible(true);
			sap.ui.getCore().byId(oAppContId + "idNextButton").setVisible(true);

			var paginatedData = this.getView().getModel("dashBoardModel").getData().pageArray;
			var selectedPage = parseInt(oDefaultDataModel.getProperty("/selectedPage"));
			var startValue = parseInt(paginatedData[0].text);
			var startNumber = 1;
			var array = [];
			if ((startValue - 1) === 1) {
				startNumber = 1;
				sap.ui.getCore().byId(oAppContId + "idPrevButton").setVisible(true);
			} else {
				startNumber = selectedPage - 3;
			}
			for (var i = startNumber; i <= (startNumber + 4); i++) {
				var object = {
					"text": i
				};
				array.push(object);
			}
			this.getModel("dashBoardModel").setProperty('/pageArray', array);
			oDefaultDataModel.setProperty("/selectedPage", (parseInt(oDefaultDataModel.getProperty("/selectedPage")) - 1));
			this.getAlarmData();
		},

		/**
		 * Method is called when user clicks on the next button in the pagination.
		 * 
		 */
		onScrollRight: function () {
			var oDefaultDataModel = this.getModel("dashBoardModel");
			var oAppContId = this.getView().createId("alarmFragment");
			sap.ui.getCore().byId(oAppContId + "idPrevButton").setVisible(true);
			sap.ui.getCore().byId(oAppContId + "idNextButton").setVisible(true);
			var paginatedData = this.getView().getModel("dashBoardModel").getData().pageArray;
			var selectedPage = parseInt(oDefaultDataModel.getProperty("/selectedPage"));
			var startNumber = 1;
			var array = [];
			if (selectedPage > 2) {
				if ((selectedPage + 3) >= oDefaultDataModel.getProperty("/numberOfPages")) {
					sap.ui.getCore().byId(oAppContId + "idNextButton").setVisible(false);
					// sap.ui.getCore().byId(oAppContId + "--alarmList--idNextButton").setEnabled(false);
					startNumber = parseInt(oDefaultDataModel.getProperty("/numberOfPages")) - 4;
				} else {
					startNumber = selectedPage - 1;
				}
			} else {
				sap.ui.getCore().byId(oAppContId + "idPrevButton").setVisible(false);
			}
			for (var i = startNumber; i <= (startNumber + 4); i++) {
				var object = {
					"text": i
				};
				array.push(object);
			}
			this.getModel("dashBoardModel").setProperty('/pageArray', array);
			oDefaultDataModel.setProperty("/selectedPage", (parseInt(oDefaultDataModel.getProperty("/selectedPage")) + 1));
			this.getAlarmData();
		},

		/**
		 * Method is called when user clicks on the particular page number.
		 * 
		 */
		onPageClick: function (oEvent) {
			var oDefaultDataModel = this.getModel("dashBoardModel");
			var selectedPage = oEvent.getSource().getText();
			oDefaultDataModel.setProperty("/selectedPage", selectedPage);
			this.getAlarmData();
		},

		onDowntimeTblSearch: function (oEvent) {
			var value = oEvent.getSource().getValue();
			var sTabSelected = this.getView().getModel("dashBoardModel").getData().downtimeCaptureTabKey;
			if (sTabSelected === "Well") {
				var oDTList = sap.ui.core.Fragment.byId(this.createId("downtimeFragment"), "downTimeCaptureTable");
				var aFilters;
				var filterParams = ["well", "childCode", "downtimeCode", "createdAt", "duration", "rcTime", "downtimeText", "childText"];
			}
			if (sTabSelected === "Compressor") {
				var oDTList = sap.ui.core.Fragment.byId(this.createId("downtimeFragment"), "compDownTimeCaptureTable");
				var aFilters;
				var filterParams = ["well", "downtimeCode", "createdAt", "duration", "rcTime", "downtimeText", "facility", "childText"];
			}
			if (sTabSelected === "Flare") {
				var oDTList = sap.ui.core.Fragment.byId(this.createId("downtimeFragment"), "flareDownTimeCaptureTable");
				var aFilters;
				var filterParams = ["meter", "createdAt", "childCode", "childText", "flareVolume"];
			}
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oDTList.getBinding("items").filter(aFilters);
		},
		onLivechange: function (evt) {
			this.oSuggestionParentInput = evt.getSource();
			var oFragmentId = "oSuggestionPopover",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.SuggestionListPopover";
			// var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			// if (!isReadOnly) {

			if (!this.oSuggestionList) {
				this.oSuggestionList = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oSuggestionList);
			}
			var query = evt.getSource().getValue();
			var oList = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist");
			if (oList.getItems().length) {
				$.each(oList.getItems(), function (indx, value) {
					value.getContent()[0].setExpanded(false);
				}.bind(this));
			}
			this.oSuggestionList.openBy(this.oSuggestionParentInput);
			if (query && query.length > 0) {
				var filterParams = ["firstName", "lastName"];
				var filterArray = [];
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, query));
				}
				var aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			var binding = oList.getBinding("items");
			binding.filter(aFilters);
			// }
		},

		onpopoverOpen: function (oEvent) {
			this.oSuggestionParentInput.focus();
		},

		_initilizeLocalModelForSuggestions: function (oModel) {
			oModel.setProperty("/currentSelectedObjects", {});
			oModel.setProperty("/isInvestigationCreate", false);
			oModel.setProperty("/isInquiryCreate", false); //AN: #inquire
			oModel.setProperty("/suggestionItems", []);
		},

		_getCurrentNearByUser: function (lat, lng, locCode) {
			var sUrl = "/taskmanagementRest/locationGT/getNearestUsers";
			if (locCode) {
				sUrl = sUrl + "?locationCode=" + locCode;
				var isEfsOrCa = "";
				if (locCode.startsWith("MUR-US")) {
					isEfsOrCa = "EFS";
				} else if (locCode.startsWith("MUR-CA")) {
					isEfsOrCa = "CA";
				}
				oDashBoardModel.setProperty("/suggestionItemsCountry", isEfsOrCa);
			}
			this.doAjax(sUrl, "GET", null, function (oData) {
				this.getModel("dashBoardModel").setProperty("/suggestionItems", oData.nearestUsers);
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},

		onSuggestionListItemPress: function (evt) {
			this.oSuggestionParentInput.focus(); //AN: Assign-to-Person focus issue fix
			this.setSelections(evt, "press");
		},

		onSuggestionListItemSelectionChange: function (evt) {
			this.oSuggestionParentInput.focus();
			if (evt.getParameter("selected")) {
				this.setSelections(evt, "select");
			} else {
				var oCurrentObject = evt.getParameter("listItem").getBindingContext("dashBoardModel").getObject();
				this.removeSelections(oCurrentObject, "uncheck");
			}
		},

		removeSelections: function (oCurrentObject, sType) {
			var aNewArry = [];
			var oObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
			if (sType === "token") {
				var oListItems = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist").getSelectedItems();
				$.each(oListItems, function (iIndex, oObj) {
					if (this.getModel("dashBoardModel").getProperty("/isInvestigationCreate") || this.getModel("dashBoardModel").getProperty(
							"/isInquiryCreate")) { //AN: #inquire
						if (oObj.getBindingContext("dashBoardModel").getObject().userId === oCurrentObject.userId) {
							oObj.setSelected(false);
						}
					} else if (oObj.getBindingContext("dashBoardModel").getObject().emailId === oCurrentObject.emailId) {
						oObj.setSelected(false);
					}
				}.bind(this));
			}
			$.each(oObj, function (index, oValue) {
				if (this.getModel("dashBoardModel").getProperty("/isInvestigationCreate") || this.getModel("dashBoardModel").getProperty(
						"/isInquiryCreate")) { //AN: #inquire
					if (oCurrentObject.userId !== oValue.userId) {
						aNewArry.push(oValue);
					}
				} else if (oCurrentObject.emailId !== oValue.emailId) {
					aNewArry.push(oValue);
				}
			}.bind(this));
			this.getModel("dashBoardModel").setProperty("/currentSelectedObjects", aNewArry);
			var sItems = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
			this.getTaskSchedulingDataForUser(sItems);
			this.getModel("dashBoardModel").setProperty("/startDateForUser", "");
			var sKey;
			var sStatus = this.getModel("dashBoardModel").getData().taskStatus;
			if (sStatus !== "RESOLVED" || sStatus !== "RETURNED" || sStatus !== "IN PROGRESS") {
				sKey = this.getModel("oTaskPanelDetailModel").getData().taskEventDto.locationCode;
			} else if (sStatus === "ASSIGNED") {
				sKey = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject[0].location;
			}
			var value = this.getModel("dashBoardModel").getData().taskSubject1;
			var sSubClassification;
			if (value === "Downtime") {
				sSubClassification = this.getModel("dashBoardModel").getData().taskSubject2;
			} else {
				sSubClassification = this.getModel("dashBoardModel").getData().taskSubject3;
			}
			if (sSubClassification && sKey && (sItems.length > 0) && sItems[0].userId) {
				this.getTaskStartDate(sItems[0].userId, sKey, sSubClassification);
			}
		},

		setSelections: function (oEvent, sEvent) {
			this.oSuggestionParentInput.setValue("");
			if (oEvent) {
				var oListItem = oEvent.getParameter("listItem");
				var oPressedObject = oListItem.getBindingContext("dashBoardModel").getObject();
				var userName = oPressedObject.firstName + oPressedObject.lastName;
				var sUrl = "/taskmanagementRest/user/getDetails?userName=" + userName;
				var oSelectedItems = oEvent.getSource().getSelectedItems();
				var oObj;
				var aTemporarySuggestion = [];
				var sTextString = "";
				if (oSelectedItems.length > 0) {
					$.each(oSelectedItems, function (indx, val) {
						var oCurrentBindObj = val.getBindingContext("dashBoardModel").getObject();
						aTemporarySuggestion.push(oCurrentBindObj);
					}.bind(this));
				}
				if (sEvent === "press") {
					oListItem.setSelected(true);
					aTemporarySuggestion.push(oPressedObject);
				}
				oObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
				$.each(oObj, function (indx, val) {
					aTemporarySuggestion.push(val);
				}.bind(this));
				if (this.getModel("dashBoardModel").getProperty("/isInvestigationCreate") || this.getModel("dashBoardModel").getProperty(
						"/isInquiryCreate")) { //AN: #inquire
					aTemporarySuggestion = this.removeDuplicates(aTemporarySuggestion, "userId");
				} else {
					aTemporarySuggestion = this.removeDuplicates(aTemporarySuggestion, "emailId");
				}

				this.getModel("dashBoardModel").setProperty("/currentSelectedObjects", aTemporarySuggestion);
			}
			// Create Task Enhancement
			var sItems = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
			var sKey;
			var sStatus = this.getModel("dashBoardModel").getData().taskStatus;
			if (sStatus !== "RESOLVED" || sStatus !== "RETURNED" || sStatus !== "IN PROGRESS") {
				sKey = this.getModel("oTaskPanelDetailModel").getData().taskEventDto.locationCode;
			} else if (sStatus === "ASSIGNED") {
				sKey = this.getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject[0].location;
			}
			this.getTaskSchedulingDataForUser(sItems);
			var value = this.getModel("dashBoardModel").getData().taskSubject1;
			var sSubClassification;
			if (value === "Downtime") {
				sSubClassification = this.getModel("dashBoardModel").getData().taskSubject2;
			} else {
				sSubClassification = this.getModel("dashBoardModel").getData().taskSubject3;
			}
			if (sSubClassification && sKey && sItems[0].emailId) {
				this.getTaskStartDate(sItems[0].emailId, sKey, sSubClassification);
			}
			this.oSuggestionParentInput.fireLiveChange();
		},

		setSelectionsInvestigation: function (oEvent) {
			this.oSuggestionParentInput.setValue("");
			var oSelectedItems = oEvent.getSource().getSelectedItems();
			var aTemporarySuggestion = [];
			if (oSelectedItems.length > 0) {
				$.each(oSelectedItems, function (indx, val) {
					var oCurrentBindObj = val.getBindingContext("dashBoardModel").getObject();
					aTemporarySuggestion.push(oCurrentBindObj);
				}.bind(this));
			}
			var oObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
			$.each(oObj, function (indx, val) {
				aTemporarySuggestion.push(val);
			}.bind(this));
			aTemporarySuggestion = this.removeDuplicates(aTemporarySuggestion, "userId");
			this.getModel("dashBoardModel").setProperty("/currentSelectedObjects", aTemporarySuggestion);
			this.oSuggestionParentInput.fireLiveChange();
		},

		onTokenDelete: function (evt) {
			var tokenObj = evt.getParameter("token").getBindingContext("dashBoardModel").getObject();
			this.removeSelections(tokenObj, "token");
		},
		onTokenDeselect: function (evt) {
			if (evt.getParameter("type") === "removed") {
				var tokenObj = evt.getParameter("removedTokens")[0].getBindingContext("dashBoardModel").getObject();
				this.removeSelections(tokenObj, "token");
			}
		},
		removeDuplicates: function (originalArray, prop) {
			var newArray = [];
			var lookupObject = {};
			for (var i in originalArray) {
				lookupObject[originalArray[i][prop]] = originalArray[i];
			}
			for (i in lookupObject) {
				newArray.push(lookupObject[i]);
			}
			return newArray;
		},

		onPressDownloadReport: function () {
			var sSelKey = this.getModel("dashBoardModel").getProperty("/dopData/selectedKey"); //AN: #DOP-DGP
			var oPayload = {
				"fileFormate": "Excel"
			};
			var locations = [];
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "analytics") {
				oPayload.reportName = "AuditReport";
			} else {
				var selHier = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
				if (selHier === "SEARCH") {
					selHier = "Well";
				}
				var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
				for (var i = 0; i < selectedLoc.length; i++) {
					locations.push(selectedLoc[i].location);
				}
				var uiRequestDto = {};
				uiRequestDto.duration = parseInt(this.getModel("dashBoardModel").getProperty("/dopData/durationKey"));
				uiRequestDto.rolledUp = this.getModel("dashBoardModel").getProperty("/dopData/rolledUp");
				uiRequestDto.locationCodeList = locations;
				uiRequestDto.locationType = selHier;
				uiRequestDto.countryCode = this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy"); //AN: #DOP-DGP
				if (sSelKey === "dop") { //AN: #DOP-DGP
					uiRequestDto.reportId = "DP01";
					// oPayload.reportName = "ProductionVarianceReport";
				} else if (sSelKey === "dgp") {
					uiRequestDto.reportId = "DG01";
					// oPayload.reportName = "DGPProductionVarianceReport";
				}
				oPayload.reportName = "ProductionVarianceReport";
				oPayload.uiRequestDto = uiRequestDto;

			}

			if (locations.length === 0 && this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") !==
				"analytics") {
				this._showToastMessage(" Data not available. Please select a location");
			} else {
				var sUrl = "/taskmanagementRest/report/download";
				this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData) {
						this.downloadFile(oData);
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this), function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(this));
			}
		},

		alarmActionListClose: function () {
			this.getModel("dashBoardModel").setProperty("/alarmRefresh", true);
		},

		onPressCreateInvestigation: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/isCreateTask", false);
			this.getModel("dashBoardModel").setProperty("/showProActiveSwitch", true); // #RV Proactive Well Candidate Fix for Visibility
			this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", true);
			var duration = "7";
			var currentUser = this._getCurrentLoggedInUser();
			var dashBoardModel = this.getModel("dashBoardModel");
			var oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
			var oFragmentId = "CreateInvestigationPanel",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.CreateInvestigationPanel";
			var currSelectedLocation = {}; //AN: #inquire
			this.getModel("oTaskPanelDetailModel").setProperty("/visibleChecklist", false);
			if (!this.oInvestigationPanel) {
				this.oInvestigationPanel = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oInvestigationPanel);
			}
			if (dashBoardModel.getProperty("/InsightToActionData/isWBInvestigation")) { //AN: #msgToROC
				var oCurrSelWBMsgData = dashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
				currSelectedLocation.locationCode = oCurrSelWBMsgData.locationCode;
				currSelectedLocation.well = oCurrSelWBMsgData.locationText;
				currSelectedLocation.muwiId = oCurrSelWBMsgData.muwi;
				dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
					userType: "POT",
					taskType: "Investigation"
				});
				this.getModel("dashBoardModel").setProperty("/createInvestigationObject", currSelectedLocation);
			} else if (dashBoardModel.getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
				var oTaskPanelDetailModelData = oTaskPanelDetailModel.getData();
				currSelectedLocation.muwiId = oTaskPanelDetailModelData.muwiId;
				currSelectedLocation.locationCode = oTaskPanelDetailModelData.taskEventDto.locationCode;
				for (var pd = 0; pd < oTaskPanelDetailModelData.observationCustomAttr.length; pd++) {
					if (oTaskPanelDetailModelData.observationCustomAttr[0].label === "Location") {
						currSelectedLocation.well = oTaskPanelDetailModelData.observationCustomAttr[0].labelValue;
						break;
					}
				}
				dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
					userType: "POT",
					taskType: "Investigation",
					from: "Inquiry",
					inquiryProcessId: oTaskPanelDetailModelData.taskEventDto.processId,
					inquiryCreatorId: oTaskPanelDetailModelData.taskEventDto.createdBy,
					inquiryLocation: currSelectedLocation.well
				});
				this.getModel("dashBoardModel").setProperty("/createInvestigationObject", currSelectedLocation); //#RV: Investigation ALS add Miles Changes
			} else if (dashBoardModel.getProperty("/dynamicContentType/from") === "PwHopper") { //AN: #pw
				currSelectedLocation = dashBoardModel.getProperty("/currentPwHopperObject");
				currSelectedLocation.muwiId = currSelectedLocation.muwi;
				currSelectedLocation.well = currSelectedLocation.location;
				dashBoardModel.setProperty("/dynamicContentType", {
					userType: "POT",
					taskType: "Investigation",
					from: "PwHopper"
				});
				this.getModel("dashBoardModel").setProperty("/createInvestigationObject", currSelectedLocation); //#RV: Investigation ALS add Miles Changes
			} else {
				currSelectedLocation = dashBoardModel.getProperty("/currentProveObject");
				dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
					userType: "POT",
					taskType: "Investigation"
				});
				duration = dashBoardModel.getProperty("/spotFireData/durationKey");
				this.getModel("dashBoardModel").setProperty("/createInvestigationObject", currSelectedLocation); //#RV: Investigation ALS add Miles Changes
			}
			this.oInvestigationPanel.open();
			var sUrl = "/taskmanagementRest/tasks/getHeader?taskTempId=VAR_OBS&muwi=" + currSelectedLocation.muwiId;
			sUrl = sUrl + "&location=" + encodeURIComponent(currSelectedLocation.well) + "&locType=Well&locationCode=" +
				encodeURIComponent(currSelectedLocation.locationCode) + "&duration=" + duration;
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true); //AN: #inquire
			this.doAjax(sUrl, "GET", null, function (oData) {
				dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
				if (dashBoardModel.getProperty("/InsightToActionData/isWBInvestigation")) { //AN: #msgToROC
					dashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
				}
				if (oData.responseMessage.statusCode === "0") {
					var tempData = oData;
					tempData.taskEventDto = {
						createdBy: currentUser,
						createdByDisplay: dashBoardModel.getProperty("/userData/displayName"),
						description: "",
						subject: "",
						status: "NEW",
						owners: [],
						ownersName: null,
						locationCode: currSelectedLocation.locationCode
					};
					tempData.collabrationDtos = [];
					tempData.status = "ASSIGNED";
					tempData.muwiId = currSelectedLocation.muwiId;
					tempData.taskEventDto.taskMode = this.getModel("dashBoardModel").getProperty("/spotFireData/durationKey");
					var checkListVisibility = oTaskPanelDetailModel.getProperty("/visibleChecklist");
					oTaskPanelDetailModel.setData(tempData);
					if (tempData.checkList && tempData.checkList.length > 0) {
						checkListVisibility = true;
					}
					oTaskPanelDetailModel.setProperty("/visibleChecklist", checkListVisibility);
					this.getInvestigationHistory(currSelectedLocation.muwiId); //AN: #inquire
					if (window.navigator.msSaveOrOpenBlob) {
						if (this.oInvestigationPanel) {
							this.oInvestigationPanel.$().find(".iopwbCloseBtnClass").addClass("iopwbCloseBtnIEClass");
						}
					}
					var oTaskList1 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idVarianceGrid");
					var oTaskList2 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idObservationGrid");
					this.createDynamicCont(oTaskList1, "varianceCustomAttr");
					this.createDynamicCont(oTaskList2, "observationCustomAttr");
					this.onIssueClassificationChange();
					/*	if (dashBoardModel.getProperty("/dynamicContentType/from") === "PwHopper") { //AN: #pw
							var oTaskList3 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idPWChecklistVBox"); //AN: #pw
							this.createDynamicContentPWCheckList(oTaskList3, "checkList"); //AN: #pw
						}*/
					//#RV Proactive Enhacement Changes
					var oModel = this.getModel("oTaskPanelDetailModel");
					oModel.setProperty("/PWData", {
						buttonEnable: true,
						buttonState: false
					});
					if (checkListVisibility) { //AN: #pw
						var oTaskList4 = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idPWChecklistVBox");
						this.createDynamicContentPWCheckList(oTaskList4, "checkList");
					}
				} else {
					this._createConfirmationMessage("Error", oData.responseMessage.message, "Error", "", "Close", false, null);
					this.oInvestigationPanel.close();
				}
			}.bind(this), function (oError) {
				this.oInvestigationPanel.close();
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},

		onCreateInvestigationClose: function () {
			if (this.oInvestigationPanel) {
				this.oInvestigationPanel.close();
			}
			if (this.getModel("dashBoardModel").getProperty("/dynamicContentType/from") === "Inquiry") { //AN: #inquire
				this.onCreateInquiryClose();
			}
			this.onClosingDetailTask();
			if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) {
				this.onPotDataLoad();
			}
		},

		onAssign: function () {
			var oStatus = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status");
			if (oStatus && oStatus === "ASSIGNED") {
				this.onPressUpdateTask();
			} else {
				this.onCreatingInvestigation();
			}
		},

		onUpdatePWHopperData: function () {
			this.getModel("dashBoardModel").setProperty("/pwHopperUpdate", true);
			this.onPressUpdateTask();
		},

		onCreatingInvestigation: function () {
			var dashBoardModel = this.getModel("dashBoardModel");
			this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", true);
			var oData = this.getModel("oTaskPanelDetailModel").getData();
			var sUrl = "/taskmanagementRest/tasks/createTask";
			var userData = this.getModel("dashBoardModel").getProperty("/userData");
			var currObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
			if (this.getModel("dashBoardModel").getProperty("/dynamicContentType/from") === "PwHopper") { //AN: #pw
				this._checkNewUpdateCheckList();
			}
			if (!currObj || currObj.length === 0) {
				this._showToastMessage("Please select assignee to create investigation");
				this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", false);
				return;
			}
			oData.taskEventDto.owners = [];
			for (var i = 0; i < currObj.length; i++) {
				var lastName = currObj[i].lastName;
				var firstName = currObj[i].firstName;
				if (lastName) {
					firstName = firstName + " " + lastName;
				}
				oData.taskEventDto.owners.push({
					"taskOwner": currObj[i].userId,
					"taskOwnerDisplayName": firstName,
					"ownerEmail": currObj[i].userId,
					"pId": currObj[i].pId
				});
			}
			//var arrOwners = this.generatePidPayload(oData.taskEventDto.owners);
			var arrOwners = oData.taskEventDto.owners;
			oData.taskEventDto.origin = "Investigation";
			var oGridContent = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idObservationGrid").getContent();
			var assignToGroupId = this.getModel("dashBoardModel").getProperty("/assignToGroupId");
			var primaryClassificationId = this.getModel("dashBoardModel").getProperty("/primaryClassificationId");
			var secondaryClassificationId = this.getModel("dashBoardModel").getProperty("/secondaryClassificationId");
			var assignToGroupValue = oGridContent[assignToGroupId].getItems()[1].getSelectedKey();
			var primaryClassificationValue = oGridContent[primaryClassificationId].getItems()[1].getSelectedKey();
			var secondaryClassificationValue = oGridContent[secondaryClassificationId].getItems()[1].getSelectedKey();
			oData.observationCustomAttr[assignToGroupId].labelValue = assignToGroupValue;
			oData.observationCustomAttr[primaryClassificationId].labelValue = primaryClassificationValue;
			oData.observationCustomAttr[secondaryClassificationId].labelValue = secondaryClassificationValue;
			var custAtt = oData.observationCustomAttr;
			for (var i = 0; i < custAtt.length; i++) {
				if (custAtt[i].isMandatory && (custAtt[i].labelValue === "" || custAtt[i].labelValue === null || custAtt[i].labelValue ===
						undefined) && custAtt[i].label !== "Assign to person(s)") {
					this._showToastMessage("Fill all mandatory fields");
					return;
				}
			}
			var oPayload = {};
			if (oData.taskEventDto.processId) {
				oPayload.observationCustomAttr = oData.observationCustomAttr;
				oPayload.taskEventDto = oData.taskEventDto;
				oData.taskEventDto.subject = oData.observationCustomAttr[0].labelValue +
					" - " + primaryClassificationValue + " / " + secondaryClassificationValue;
			} else {
				oData.taskEventDto.createdBy = userData.userId;
				oData.taskEventDto.createdByDisplay = userData.displayName;
				//	oData.taskEventDto.group = userData.potRole.split(",")[0]; w
				var taskCreationGroup = this.getModel("dashBoardModel").getProperty("/changeSeatData/taskCreationRole");
				if (!taskCreationGroup && userData.potRole) { //AN: #inquiryEnhancement
					taskCreationGroup = userData.potRole.split(",")[0];
				}
				oData.taskEventDto.group = taskCreationGroup;
				oData.taskEventDto.subject = this.getModel("dashBoardModel").getProperty("/currentProveObject/well") +
					" - " + primaryClassificationValue + " / " + secondaryClassificationValue;
				oPayload = oData;
			}
			oPayload.taskEventDto.status = "ASSIGNED";
			if (oPayload.taskEventDto.parentOrigin !== "Message") { //AN: #msgToROC
				oPayload.taskEventDto.parentOrigin = "Investigation"; //AN: #pw
			}
			var dynamicContentType = this.getModel("dashBoardModel").getProperty("/dynamicContentType"); //AN: #inquire
			if (dynamicContentType.from === "Inquiry") { //AN: #inquire
				oPayload.taskEventDto.parentOrigin = "Inquiry";
				oPayload.taskEventDto.prevTask = dynamicContentType.inquiryProcessId;
				if (!oData.taskEventDto.processId) {
					oPayload.taskEventDto.subject = dynamicContentType.inquiryLocation + " - " + primaryClassificationValue + " / " +
						secondaryClassificationValue;
				}
			} else if (dynamicContentType.from === "PwHopper") { //AN: #pw
				if (!oData.taskEventDto.processId) {
					oPayload.taskEventDto.subject = this.getModel("dashBoardModel").getProperty("/currentPwHopperObject/well") + " - " +
						primaryClassificationValue + " / " +
						secondaryClassificationValue;
				}
			} else if (dashBoardModel.getProperty("/InsightToActionData/isWBInvestigation")) { //AN: #msgToROC
				var oCurrSelWBMsgData = dashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
				oPayload.taskEventDto.parentOrigin = "Message";
				oPayload.taskEventDto.prevTask = oCurrSelWBMsgData.messageId;
				oPayload.taskEventDto.subject = oCurrSelWBMsgData.locationText + " - " + primaryClassificationValue + " / " +
					secondaryClassificationValue;
			}
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", false);
					if (oData.statusCode === "0") {
						this.sendNotification("You have a new task assigned", arrOwners, true);
						this._showToastMessage(oData.message);
						this.getProveReport();
						if (dynamicContentType.from === "Inquiry") { //AN: #inquire
							this.onRemoveTask(this.getModel("dashBoardModel").getProperty("/taskId"), false, dynamicContentType.inquiryProcessId, true,
								"ResolveInquiry", true);
						} else if (dynamicContentType.from === "PwHopper") { //AN: #pw
							this.getModel("dashBoardModel").setProperty("/busyIndicators/pwHopperTblBusy", true); //AN: #loaderWhileNotif
							this.onPWHopperLoad();
						} else if (dashBoardModel.getProperty("/InsightToActionData/isWBInvestigation")) { //AN: #
							var msgDraftDetailData = dashBoardModel.getProperty("/InsightToActionData/msgDraftDetailData");
							// WorkbenchHelper.fnWBMsgUpdateStatus(this, dashBoardModel, "IN PROGRESS", msgDraftDetailData.currentOwner);
							WorkbenchHelper.fnWBMsgUpdateStatus(this, dashBoardModel, "ASSIGNED", msgDraftDetailData.currentOwner); //SH: NewFlow
							WorkbenchHelper.onWBMsgClose(this, this.getModel("dashBoardModel"));
						} else if (oPayload.taskEventDto.parentOrigin === "Message" && oPayload.taskEventDto.prevTask) { //AN: #msgToROC: if message is resolved then move the msg to IN-PROGRESS
							var oUrl = "/taskmanagementRest/message/getMessage?messageId=" + oPayload.taskEventDto.prevTask;
							var oModel = new sap.ui.model.json.JSONModel();
							oModel.loadData(oUrl, null, false);
							var msgResultData = oModel.getData();
							if (msgResultData && msgResultData.responseMessage && msgResultData.responseMessage.statusCode === "0" && msgResultData.message &&
								msgResultData.message.status.toLowerCase() === "resolved") {
								msgResultData.message.status = "IN PROGRESS";
								// WorkbenchHelper.fnWBMsgUpdateStatus(this, this.getModel("dashBoardModel"), "IN PROGRESS", false, msgResultData.message);
								WorkbenchHelper.fnWBMsgUpdateStatus(this, this.getModel("dashBoardModel"), "ASSIGNED", false, msgResultData.message); //SH: NewFlow
							}
						}
						this.onCreateInvestigationClose();
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/taskPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		onAssignGrpChange: function (oEvent) { //AN: #inquire
			var dashBoardModel = this.getModel("dashBoardModel"); //AN: #inquire
			var dynamicContentType = dashBoardModel.getProperty("/dynamicContentType"); //AN: #inquire
			var oGridContent, assignToGroupValue;
			var assignToGroupId = dashBoardModel.getProperty("/assignToGroupId");
			var userType = "";
			var taskOwner = this.getModel("dashBoardModel").getProperty("/taskOwner"); //AN: #inquire
			if (dynamicContentType.taskType === "Inquiry") { //AN: #inquire
				oGridContent = sap.ui.getCore().byId("idCreateInquiryPanel--idInquiryDetailsPanel--idInquiryObservationGrid").getContent();
				assignToGroupValue = oGridContent[assignToGroupId].getItems()[1].getSelectedKey();

				if (assignToGroupValue === "") { //AN: #inquiryEnhancement
					var keyToSet = this.getModel("oTaskPanelDetailModel").getData().observationCustomAttr[assignToGroupId].valueDtos[0].value;
					oGridContent[assignToGroupId].getItems()[1].setSelectedKey(keyToSet);
					assignToGroupValue = keyToSet;
				}
				userType = assignToGroupValue;

				if (dynamicContentType.userType === "POT") {
					// oGridContent[assignToGroupId].getItems()[1].setSelectedKey("ROC");
					// assignToGroupValue = "ROC";
					// userType = assignToGroupValue;
					assignToGroupValue = dashBoardModel.getProperty("/userData/potRole");
				} else if (dynamicContentType.userType === "ENG") {
					// if (assignToGroupValue === "" || assignToGroupValue === "POT") { //AN: #inquiryEnhancement
					// 	oGridContent[assignToGroupId].getItems()[1].setSelectedKey("POT");
					// 	assignToGroupValue = "POT";
					// } else {
					// 	assignToGroupValue = "ROC";
					// }
					// if (assignToGroupValue === "") { //AN: #inquiryEnhancement
					// 	var keyToSet = this.getModel("oTaskPanelDetailModel").getData().observationCustomAttr[assignToGroupId].valueDtos[0].value;
					// 	oGridContent[assignToGroupId].getItems()[1].setSelectedKey(keyToSet);
					// 	assignToGroupValue = keyToSet;
					// }
					// userType = assignToGroupValue;
					assignToGroupValue = dashBoardModel.getProperty("/userData/engRole");
				} else {
					// oGridContent[assignToGroupId].getItems()[1].setSelectedKey("POT");
					// assignToGroupValue = "POT";
					// userType = assignToGroupValue;
					assignToGroupValue = dashBoardModel.getProperty("/userData/resGroupRead");
				}
			} else if (dynamicContentType.taskType === "Investigation") {
				oGridContent = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idObservationGrid").getContent();
				assignToGroupValue = oGridContent[assignToGroupId].getItems()[1].getSelectedKey();
				if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
					return;
				}
				if (assignToGroupValue === "") {
					oGridContent[assignToGroupId].getItems()[1].setSelectedKey("ALS");
					assignToGroupValue = "ALS";
				}
				userType = assignToGroupValue;
				assignToGroupValue = dashBoardModel.getProperty("/userData/potRole");
			}
			var status = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status");
			//var sUrl = "/taskmanagementRest/group/getUsersById?groupId=" + assignToGroupValue + "&userType=" + userType;
			var oAssignToGroup = oGridContent[assignToGroupId].getItems()[1].getSelectedKey();
			var sUrl = "";
			var sTaskType = dynamicContentType.taskType;
			//#RV: Investigation ALS add Miles Changes
			if (oAssignToGroup === "ALS") {
				var oLocCode = "";
				var oSelLoc = this.getModel("dashBoardModel").getProperty("/createInvestigationObject");
				oLocCode = oSelLoc.locationCode;
				sUrl = "/taskmanagementRest/locationGT/getNearestUsers?locationCode=" + oLocCode + "&groupId=" + assignToGroupValue +
					"&userType=" + userType + "&taskType=" + sTaskType;
				var isEfsOrCa = "";
				if (oLocCode.startsWith("MUR-US")) {
					isEfsOrCa = "EFS";
				} else if (oLocCode.startsWith("MUR-CA")) {
					isEfsOrCa = "CA";
				}
				dashBoardModel.setProperty("/suggestionItemsCountry", isEfsOrCa);
			} else {
				sUrl = "/taskmanagementRest/group/getUsersById?groupId=" + assignToGroupValue + "&userType=" + userType + "&taskType=" + sTaskType;
			}
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (oData && oData.responseMessage.statusCode === "0") {
						//#RV: Investigation ALS add Miles Changes
						if (oAssignToGroup === "ALS") {
							dashBoardModel.setProperty("/suggestionItems", oData.nearestUsers);
						} else {
							dashBoardModel.setProperty("/suggestionItems", oData.userList);
						}
						//dashBoardModel.setProperty("/suggestionItems", oData.userList);
						if (dynamicContentType.taskType === "Inquiry") { //AN: #inquire
							if (taskOwner) {
								if (taskOwner.includes(dashBoardModel.getProperty("/userData/displayName")) || status === "RESOLVED") {
									dashBoardModel.setProperty("/currentSelectedObjects", []);
								}
							} else if (status !== "ASSIGNED") {
								dashBoardModel.setProperty("/currentSelectedObjects", []);
							}
						} else {
							if (status !== "ASSIGNED") {
								dashBoardModel.setProperty("/currentSelectedObjects", []);
							}
						}
						var oList = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist");
						if (oList) {
							oList.removeSelections();
						}
					} else {
						if (dynamicContentType.taskType === "Investigation" && oAssignToGroup === "ALS") {
							sUrl = "/taskmanagementRest/group/getUsersById?groupId=" + assignToGroupValue + "&userType=" + userType;
							this.doAjax(sUrl, "GET", null, function (oData) {
								if (oData && oData.responseMessage.statusCode === "0") {
									if (status !== "ASSIGNED") {
										dashBoardModel.setProperty("/suggestionItems", oData.userList);
										dashBoardModel.setProperty("/currentSelectedObjects", []);
										var oList = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist");
										if (oList) {
											oList.removeSelections();
										}
									}
								} else {
									this._showToastMessage("User fetching failed");
								}
							}.bind(this));
						} else {
							this._showToastMessage("User fetching failed");
						}
						//	
					}
				}.bind(this),
				function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(this));
		},

		onDesignateParamsChange: function () {
			if (this.createTaskPanel && this.createTaskPanel.isOpen()) {
				this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", true);
				this.getModel("dashBoardModel").setProperty("/resolvedTaskMuwi", this.getModel("oTaskPanelDetailModel").getProperty(
					"/muwiId"));
			}
		},

		onCompletingInvestigation: function (oEvt, processId, muwi) {
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				pattern: "yyyy-MM-dd"
			});
			var systemDate = new Date();
			var dateFormatted = dateFormat.format(systemDate);
			this.getModel("oTaskPanelDetailModel").setProperty("/proCountDate", dateFormatted);
			if (processId) {
				this.getModel("oTaskPanelDetailModel").setProperty("/taskEventDto", {
					"processId": processId
				});
				this.getModel("oTaskPanelDetailModel").setProperty("/muwiId", muwi);
				this.getModel("oTaskPanelDetailModel").setProperty("/procountUpdateFromProve", false);
			}
			this.getPrevProcountComment(dateFormatted, this.getModel("oTaskPanelDetailModel").getProperty("/muwiId"));
			var oFragmentId = "idProCountUpdateFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.ProcountCommentPanel";
			if (!this.oProcountFragment) {
				this.oProcountFragment = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oProcountFragment);
			}
			this.oProcountFragment.open();
			sap.ui.getCore().byId("idProCountUpdateFrag--idProCountDate").setMaxDate(systemDate);
		},

		getPrevProcountComment: function (sDate, sMuwi) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/procountCommentPanel", true);
			var sUrl = "/taskmanagementRest/commentsTb/getComments?uwiId=" + sMuwi + "&originalDateEntered=" + sDate;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.commentsTbDto && oData.commentsTbDto.comments) {
					var aComments = oData.commentsTbDto.comments.split("#$@$#");
					var comment = "";
					for (var i = 0; i < aComments.length; i++) {
						var aComm = aComments[i].split("[");
						var sDate = aComm[1].split("]")[0];
						sDate = sDate.trim() + " UTC";
						var oDate = new Date(sDate);
						var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
							pattern: "yyyy-MM-dd"
						});
						var dateFormatted = dateFormat.format(oDate);
						var oHour = oDate.getHours();
						var oMin = oDate.getMinutes();
						var oSec = oDate.getSeconds();
						var meridian = "AM";
						if (oHour >= 12) {
							meridian = "PM";
							if (oHour > 12) {
								oHour = oHour - 12;
							}
						}
						if (oMin < 10) {
							oMin = "0" + oMin;
						}
						if (oSec < 10) {
							oSec = "0" + oSec;
						}
						var sFormatedDate = dateFormatted + " " + oHour + ":" + oMin + ":" + oSec + " " + meridian;
						if (i === 0) {
							comment = comment + aComm[0] + "[" + sFormatedDate + "]" + aComm[1].split("]")[1];
						} else {
							comment = comment + "#$@$#" + aComm[0] + "[" + sFormatedDate + "]" + aComm[1].split("]")[1];
						}
					}
					this.getModel("oTaskPanelDetailModel").setProperty("/proCountComment", comment);
					this.getModel("oTaskPanelDetailModel").setProperty("/proCountCommentUTC", oData.commentsTbDto.comments);
					this.getModel("oTaskPanelDetailModel").setProperty("/isPrevCommentExist", true);
				} else {
					this.getModel("oTaskPanelDetailModel").setProperty("/proCountComment", "");
					this.getModel("oTaskPanelDetailModel").setProperty("/isPrevCommentExist", false);
				}
				this.getModel("dashBoardModel").setProperty("/busyIndicators/procountCommentPanel", false);
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},

		onProCommentDateChange: function (oEvent) {
			var date = oEvent.getSource().getValue();
			this.getModel("oTaskPanelDetailModel").setProperty("/proCountComment", "");
			this.getModel("oTaskPanelDetailModel").setProperty("/isPrevCommentExist", false);
			this.getPrevProcountComment(date, this.getModel("oTaskPanelDetailModel").getProperty("/muwiId"));
		},

		onUpdateProComment: function (oEvent) {
			var type = oEvent.getSource().getText();
			var country = this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy");
			if (type === "Update") {
				var sUrl;
				// if (country === "EFS") {
				sUrl = "/taskmanagementRest/commentsTb/updateComments";
				/*} else {
					sUrl = "/taskmanagementRest/commentsTb/createComments";
				}*/
				// var oPreComment = this.getModel("oTaskPanelDetailModel").getProperty("/proCountComment");
				var oPreComment = this.getModel("oTaskPanelDetailModel").getProperty("/proCountCommentUTC"); //SH: send previous comments in UTC
				var oCurrComment = this.getModel("oTaskPanelDetailModel").getProperty("/proCurrentCountComment");
				if (oPreComment) {
					var oComment = oPreComment + "#$@$#" + oCurrComment;
				} else {
					var oComment = oCurrComment;
				}
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					pattern: "yyyy-MM-dd"
				});
				var oSelDate = this.getModel("oTaskPanelDetailModel").getProperty("/proCountDate");
				var systemDate = new Date();
				//var dateFormatted = dateFormat.format(systemDate);
				var UTCYear = systemDate.getUTCFullYear();
				var UTCMonth = systemDate.getUTCMonth() + 1;
				var UTCDate = systemDate.getUTCDate();
				// if (country === "EFS") {
				var dateFormatted = UTCYear + "-" + UTCMonth + "-" + UTCDate;
				/*} else {
					var dateFormatted = UTCYear + "-" + UTCMonth + "-" + UTCDate;
				}*/
				//var SelDateFormatted = dateFormat.format(oSelDate);
				var oHour = systemDate.getUTCHours();
				var oMin = systemDate.getUTCMinutes();
				var oSec = systemDate.getUTCSeconds();
				if (oMin < 10) {
					oMin = "0" + oMin;
				}
				if (oSec < 10) {
					oSec = "0" + oSec;
				}
				var oPayload;
				// if (country === "EFS") {
				oPayload = {
					"comments": oComment,
					"enteredDate": dateFormatted + " " + oHour + ":" + oMin + ":" + oSec,
					"uwiId": this.getModel("oTaskPanelDetailModel").getProperty("/muwiId"),
					"userName": this.getModel("dashBoardModel").getProperty("/userData/displayName"),
					"prodDate": oSelDate
				};
				if (!oCurrComment || !oCurrComment.trim() || !oPayload.enteredDate) {
					this._showToastMessage("Fill all mandatory fields");
					return;
				}
				/*} else {
					oPayload = {
						"comments": oComment,
						"timeStamp": dateFormatted + " " + oHour + ":" + oMin + ":" + oSec,
						"muwi": this.getModel("oTaskPanelDetailModel").getProperty("/muwiId"),
						"foremanName": this.getModel("dashBoardModel").getProperty("/userData/displayName"),
						"date": oSelDate
					};
					if (!oCurrComment || !oCurrComment.trim() || !oPayload.timeStamp) {
						this._showToastMessage("Fill all mandatory fields");
						return;
					}
				}*/
				/*	var oPayload = {
					"comments": oComment,
					"enteredDate": dateFormatted + " " + oHour + ":" + oMin + ":" + oSec,
					"uwiId": this.getModel("oTaskPanelDetailModel").getProperty("/muwiId"),
					"userName": this.getModel("dashBoardModel").getProperty("/userData/displayName")
				};*/

				this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						var isDirectProcount = this.getModel("oTaskPanelDetailModel").getProperty("/procountUpdateFromProve");
						if (!isDirectProcount) {
							this.callRemoveTask();
						} else {
							this.oProcountFragment.close();
						}
						this.getModel("oTaskPanelDetailModel").setProperty("/proCurrentCountComment", "");
					} else {
						var sErrorMessage = oData.message;
						this._showToastMessage(sErrorMessage);
					}
				}.bind(this), function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				});
			} else {
				var isDirectProcount = this.getModel("oTaskPanelDetailModel").getProperty("/procountUpdateFromProve");
				if (!isDirectProcount) {
					this.callRemoveTask();
				} else {
					this.oProcountFragment.close();
					this.getModel("oTaskPanelDetailModel").setProperty("/procountUpdateFromProve", false);
					this.getModel("oTaskPanelDetailModel").setProperty("/proCurrentCountComment", "");
				}
			}
		},

		callRemoveTask: function () {
			this.oProcountFragment.close();
			this.getModel("oTaskPanelDetailModel").setProperty("/proCountComment", "");
			this.getModel("oTaskPanelDetailModel").setProperty("/proCountDate", "");
			this.getModel("oTaskPanelDetailModel").setProperty("/isPrevCommentExist", false);
			var sMsg = "Are you sure you want to complete this task?",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
				this.onRemoveTask("", true, this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/processId"));
			});
		},
		onUpliftChange: function (evt) {
			var iUpliftIndex = this.getModel("dashBoardModel").getProperty("/upliftIndex");
			var sUplift = evt.getSource().getValue();
			if (typeof sUplift === "string") {
				sUplift = sUplift.replace(/,/g, "");
				//sUplift = parseFloat(sUplift);
			}
			if (isNaN(sUplift)) {
				sUplift = 0;
			}
			//	var oFormat = sap.ui.core.format.NumberFormat.getFloatInstance();
			if (sUplift && sUplift >= 0 && sUplift <= 100) {
				var a = sUplift.split(".");
				if (a[1] && a[1].length > 2) {
					sUplift = parseFloat(sUplift);
					sUplift = a[0] + "." + a[1].substring(0, 2);
				}
				if (sUplift == 0 && sUplift.length > 1 && !sUplift.includes(".")) {
					sUplift = 0;
				}
				evt.getSource().setValue(sUplift);
				evt.getSource().setValueState("None");
				evt.getSource().setValueStateText("");
				var sUpliftValue = parseFloat(sUplift) + "";
				this.getModel("oTaskPanelDetailModel").setProperty("/customAttr/" + iUpliftIndex + "/labelValue", sUpliftValue);
			} else {
				evt.getSource().setValue("");
				evt.getSource().setValueState("Error");
				evt.getSource().setValueStateText("Uplift % should be between 0 to 100");
				this.getModel("oTaskPanelDetailModel").setProperty("/customAttr/" + iUpliftIndex + "/labelValue", null);
			}
		},
		onPotListSelectionChange: function (oEvent) {
			var processId = oEvent.getSource().getBindingContext("dashBoardModel").getObject().processId;
			var sBtnText = "Create Task";
			this.getModel("dashBoardModel").setProperty("/taskId", null);
			this.getModel("dashBoardModel").setProperty("/processId", processId);
			this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", true);
			this.onCreateTaskPress("", sBtnText);
		},

		onSubmitTask: function (oEvent, status, taskId) {
			var sKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var sUrl = "/taskmanagementRest/tasks/updateStatus";
			var rootCauseList;
			var oPayload = {
				"userId": this.getModel("dashBoardModel").getProperty("/userData/userId"),
				"userDisplay": this.getModel("dashBoardModel").getProperty("/userData/displayName")
			};
			if (this.getModel("dashBoardModel").getProperty("/isInquiryCreate")) { // AN: #inquire
				oPayload.status = status;
				oPayload.taskId = taskId;
			} else if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) {
				var oGridContent = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idObservationGrid").getContent();
				var primaryClassificationId = this.getModel("dashBoardModel").getProperty("/primaryClassificationId");
				var secondaryClassificationId = this.getModel("dashBoardModel").getProperty("/secondaryClassificationId");
				var primaryClassificationValue = oGridContent[primaryClassificationId].getItems()[1].getSelectedKey();
				var secondaryClassificationValue = oGridContent[secondaryClassificationId].getItems()[1].getSelectedKey();
				if (status) {
					oPayload.status = status;
					oPayload.taskId = taskId;
				} else {
					rootCauseList = [{
						"rootCause": primaryClassificationValue,
						"status": null,
						"subClassification": "Issue Classification",
						"classification": "Issue Classification",
						"description": null
					}, {
						"rootCause": secondaryClassificationValue,
						"status": null,
						"subClassification": "Sub Classification",
						"classification": "Sub Classification",
						"description": null
					}];
					oPayload.status = "RESOLVED"; //SUBMITTED
					oPayload.rootCauseList = rootCauseList;
					oPayload.taskId = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskId");
				}
			} else {
				if (status) {
					oPayload.status = status;
					oPayload.taskId = taskId;
				} else {
					rootCauseList = this.getModel("oTaskPanelDetailModel").getProperty("/rootCauseList");
					for (var i = 0; i < rootCauseList.length; i++) {
						if (!rootCauseList[i].rootCause) {
							this._showToastMessage("Fill all mandatory fields");
							return;
						}
					}
					oPayload.status = "RESOLVED"; //SUBMITTED
					oPayload.rootCauseList = rootCauseList;
					oPayload.taskId = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskId");
				}
			}
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					if (oData.statusCode === "0") {
						if (!status) {
							this._showToastMessage(oData.message);
							TaskManagementHelper._bindRightTaskPanelModel(this, this.getModel("dashBoardModel").getProperty("/selectedTaskTab")); //AN: #ScratchFilter
							this.onSideNavigationItemSelect(sKey);
							this.onCreateInvestigationClose();
						}
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},

		getInvestigationHistory: function (muwi) {
			var sUrl = "/taskmanagementRest/alsStaging/read?uwiId=" + muwi;
			this.doAjax(sUrl, "GET", null, function (oData) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/investigationHistoryBusy", false); //AN: #inquire
				if (oData.responseMessage.statusCode === "0") {
					this.getModel("oTaskPanelDetailModel").setProperty("/investigationHistoryList", oData.investigationHistoryDtoList);
				} else {
					var sErrorMessage = oData.responseMessage.message;
					this._showToastMessage(sErrorMessage);
				}
			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/investigationHistoryBusy", false); //AN: #inquire
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},

		getRootCauseList: function (taskId, status, origin) {
			var sUrl = "/taskmanagementRest/rootCause/getByStatus?taskId=" + taskId + "&status=" + status + "&origin=" + origin;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					var newList = {
						issueList: [],
						subList: []
					};
					var issueList = oData.rootCauseList[0].rootCauseList;
					var subList = oData.rootCauseList[1].subClassList;
					for (var i = 0; i < issueList.length; i++) {
						newList.issueList.push({
							key: issueList[i]
						});
					}
					for (var j = 0; j < subList.length; j++) {
						var tempList = subList[j];
						for (var k = 0; k < tempList.length; k++) {
							newList.subList.push({
								key: tempList[k],
								dependentValue: issueList[j]
							});
						}
					}
					this.getModel("oTaskPanelDetailModel").setProperty("/rootCauseList", oData.rootCauseList);
					this.getModel("oTaskPanelDetailModel").setProperty("/newIssueList", newList);
					this.onEnggClassChange();
				} else {
					var sErrorMessage = oData.responseMessage.message;
					this._showToastMessage(sErrorMessage);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},

		onEnggClassChange: function (oEvent) {
			var filterArray = [];
			var value = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idEngIssueClass").getSelectedKey();
			if (!oEvent && !value) {
				value = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idEngIssueClass").getBinding("items").oList["0"]
					.key;
			}
			var oTaskList = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idEngSubClass");
			if (value) {
				filterArray.push(new sap.ui.model.Filter("dependentValue", sap.ui.model.FilterOperator.EQ, value));
				var aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTaskList.getBinding("items").filter(aFilters);
		},

		onRightPressSpotFire: function (oEvent) { //AN: #inquire
			var oContext = oEvent.getParameters();
			var oBindingContext = oContext.src.getBindingContext("dashBoardModel");
			if (oBindingContext) {
				var oFragmentId = "idAlarmActionList",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.ProdVarianceActionList";
				var dashBoardModel = this.getModel("dashBoardModel");
				var isPOT = dashBoardModel.getProperty("/userData/isPOT");
				var isENG = dashBoardModel.getProperty("/userData/isENG");
				var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole"); //SH: WebReadOnly
				var isProveReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isProveReadOnly"); //SH: Modules readonly
				if (!this.ProdVarianceActionList) {
					this.ProdVarianceActionList = this._createFragment(oFragmentId, oFragmentName);
					this.getView().addDependent(this.ProdVarianceActionList);
				}
				if (isPOT && oBindingContext.getObject().investigationInProgress !== true && !isReadOnly && !isProveReadOnly) {
					dashBoardModel.setProperty("/potData/showInvestigate", true);
				} else {
					dashBoardModel.setProperty("/potData/showInvestigate", false);
				}
				/*if ((isPOT || isENG) && (oBindingContext.getObject().inquiryInProgress !== true && oBindingContext.getObject().investigationInProgress !==
						true)) {
					dashBoardModel.setProperty("/potData/showInquire", true);
				} else {
					dashBoardModel.setProperty("/potData/showInquire", false);
				}*/
				if (isPOT || isENG && !isReadOnly && !isProveReadOnly) { //SK:Multiple Inquiry
					dashBoardModel.setProperty("/potData/showInquire", true);
				} else {
					dashBoardModel.setProperty("/potData/showInquire", false);
				}
				this.ProdVarianceActionList.setOffsetX(-30);
				this.ProdVarianceActionList.openBy(oContext.target);
				this.getModel("dashBoardModel").setProperty("/currentProveObject", oBindingContext.getObject());
			}
		},
		getProveReport: function () {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/spotFireTblBusy", true);
			var pageNumber = this.getModel("dashBoardModel").getProperty("/spotFireData/pageNumber");
			var sUrl = "/taskmanagementRest/proveReport/fetchProveVarianceData";
			var selHier = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			if (selHier === "SEARCH") {
				selHier = "Well";
			}
			var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (selectedLoc.length === 0) {
				this.getModel("dashBoardModel").setProperty("/spotFireData/tableData", []);
				this.getModel("dashBoardModel").setProperty("/spotFireData/tableDataOriginal", []); //AN: #provePerformance
				this.getModel("dashBoardModel").setProperty("/busyIndicators/spotFireTblBusy", false);
				return;
			}
			var locations = [];
			var isCanadaLocation = false;
			for (var i = 0; i < selectedLoc.length; i++) {
				locations.push(selectedLoc[i].location);
				if (selectedLoc[i].location.startsWith("MUR-CA")) { //SK:Boed E3m3 change	
					var isCanadaLocation = true;
				}
			}
			if (!isCanadaLocation) { //SK:Boed E3m3 change	
				this.getModel("dashBoardModel").setProperty("/spotFireData/unit", "boed");
				this.getModel("dashBoardModel").setProperty("/spotFireData/unitVisible", false);
			} else {
				this.getModel("dashBoardModel").setProperty("/spotFireData/unitVisible", true);
			}
			var oPayload = {
				"locationType": selHier,
				"locationCodeList": locations,
				"duration": parseInt(this.getModel("dashBoardModel").getProperty("/spotFireData/durationKey")),
				"page": null
			};
			if (this.getModel("dashBoardModel").getProperty("/spotFireData/unit") === "boed") {
				oPayload.uom = "boe"
			} else if (this.getModel("dashBoardModel").getProperty("/spotFireData/unit") === "E3m3") {
				oPayload.uom = "E3m3"
			} else {
				oPayload.uom = "boe"
			}
			if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) { //AN: #inquire
				oPayload.userType = "POT";
			} else if (this.getModel("dashBoardModel").getProperty("/userData/isENG")) {
				oPayload.userType = "ENG";
			}
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData && oData.uiResponseDto.responseMessage.statusCode === "0") {
						var totalCount = oData.uiResponseDto.enersightProveMonthlyDtoList.length; //AN: #provePerformance
						var iRowsPerPage = 25; //AN: #provePerformance
						var startIndex = (parseInt(pageNumber) * iRowsPerPage) - (iRowsPerPage - 1); //AN: #provePerformance
						var endIndex = ((parseInt(pageNumber) - 1) * iRowsPerPage) + iRowsPerPage; //AN: #provePerformance
						if (endIndex > totalCount) {
							endIndex = totalCount;
						}
						this.getModel("dashBoardModel").setProperty("/spotFireData/tableDataOriginal", $.extend(true, [], oData.uiResponseDto.enersightProveMonthlyDtoList)); //AN: #provePerformance
						this.getModel("dashBoardModel").setProperty("/spotFireData/paginationVisible", true);
						var paginationText = startIndex + "-" + endIndex + " of " + totalCount;
						this.getModel("dashBoardModel").setProperty("/spotFireData/text", paginationText);
						var oTable = sap.ui.core.Fragment.byId(this.createId("ndtpvFragmane"), "idSpotfireList"); //AN: #provePerformance
						this.fnProveSort(oTable, oData.uiResponseDto.enersightProveMonthlyDtoList, startIndex, endIndex); //AN: #provePerformance
						if (parseInt(pageNumber) === 1) {
							this.getModel("dashBoardModel").setProperty("/spotFireData/leftButtonActive", false);
							this.getModel("dashBoardModel").setProperty("/spotFireData/rightButtonActive", true);
						} else if (parseInt(pageNumber) === Math.ceil(totalCount / iRowsPerPage)) { //AN: #provePerformance
							this.getModel("dashBoardModel").setProperty("/spotFireData/leftButtonActive", true);
							this.getModel("dashBoardModel").setProperty("/spotFireData/rightButtonActive", false);
						} else {
							this.getModel("dashBoardModel").setProperty("/spotFireData/leftButtonActive", true);
							this.getModel("dashBoardModel").setProperty("/spotFireData/rightButtonActive", true);
						}
					} else {
						var sErrorMessage = oData.uiResponseDto.responseMessage.message;
						this._showToastMessage(sErrorMessage);
					}
					this.getModel("dashBoardModel").setProperty("/busyIndicators/spotFireTblBusy", false);
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/spotFireTblBusy", false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				});
		},

		onIssueClassificationChange: function (oEvent) {
			var dashBoardModel = this.getModel("dashBoardModel"); //AN: #inquire
			var dynamicContentType = dashBoardModel.getProperty("/dynamicContentType"); //AN: #inquire
			if (dynamicContentType.taskType === "Inquiry") { //AN: #inquire
				return true;
			}
			var filterArray = [];
			var oGridContent = sap.ui.getCore().byId("CreateInvestigationPanel--idInvestigationPanel--idObservationGrid").getContent();
			var primaryClassificationId = this.getModel("dashBoardModel").getProperty("/primaryClassificationId");
			var secondaryClassificationId = this.getModel("dashBoardModel").getProperty("/secondaryClassificationId");
			var value = oGridContent[primaryClassificationId].getItems()[1].getSelectedKey();
			if (!oEvent && !value) {
				value = oGridContent[primaryClassificationId].getItems()[1].getBinding("items").oList["0"].value;
			}
			var oTaskList = oGridContent[secondaryClassificationId].getItems()[1];
			if (value) {
				filterArray.push(new sap.ui.model.Filter("dependentValue", sap.ui.model.FilterOperator.EQ, value));
				var aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oTaskList.getBinding("items").filter(aFilters);
		},

		onNDTPVSortClick: function (oEvent) {

			var oFragmentId = "idNDTPVSort",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.NDTPVSort";
			if (!this.NDTPVSort) {
				this.NDTPVSort = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.NDTPVSort);
			}
			this.NDTPVSort.setOffsetX(-30);
			this.NDTPVSort.openBy(oEvent.getSource());
		},

		onNDTPVTableSortClick: function () {
			var oFragmentId = "idNDTPVTableSortFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.NDTPVSortList";
			if (!this.NDTPVSortList) {
				this.NDTPVSortList = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.NDTPVSortList);
			}
			this.NDTPVSortList.open();
		},

		onNDTPVTableSort: function (oEvent) {
			var oTable = sap.ui.core.Fragment.byId(this.createId("ndtpvFragmane"), "idSpotfireList");
			var aTableDataOriginal = this.getModel("dashBoardModel").getProperty("/spotFireData/tableDataOriginal"); //AN: #provePerformance
			var pageNumber = this.getModel("dashBoardModel").getProperty("/spotFireData/pageNumber");
			var totalCount = aTableDataOriginal.length; //AN: #provePerformance
			var startIndex = (parseInt(pageNumber) * 25) - 24; //AN: #provePerformance
			var endIndex = ((parseInt(pageNumber) - 1) * 25) + 25; //AN: #provePerformance
			if (endIndex > totalCount) {
				endIndex = totalCount;
			}
			this.fnProveSort(oTable, $.extend(true, [], aTableDataOriginal), startIndex, endIndex); //AN: #provePerformance
		},
		fnProveSort: function (oTable, aProveData, s, e) { //AN: #provePerformance
			if (aProveData && aProveData.length > 0) {
				var sPath = "avgVarToForecastBoed";
				var bDescending = false;
				if (this.NDTPVSortList) {
					for (var i = 0; i < this.NDTPVSortList.getSortItems().length; i++) {
						if (this.NDTPVSortList.getSortItems()[i].sId === this.NDTPVSortList.getSelectedSortItem()) {
							sPath = this.NDTPVSortList.getSortItems()[i].getKey();
							break;
						}
					}
					bDescending = this.NDTPVSortList.getSortDescending();
				}
				if (bDescending) {
					if (typeof (aProveData[0][sPath]) === "string") {
						aProveData.sort((a, b) => b[sPath].localeCompare(a[sPath]));
					} else if (typeof (aProveData[0][sPath]) === "number") {
						aProveData.sort(function (a, b) {
							return b[sPath] - a[sPath];
						});
					}
				} else {
					if (typeof (aProveData[0][sPath]) === "string") {
						aProveData.sort((a, b) => a[sPath].localeCompare(b[sPath]));
					} else if (typeof (aProveData[0][sPath]) === "number") {
						aProveData.sort(function (a, b) {
							return a[sPath] - b[sPath];
						});
					}
				}
				this.getModel("dashBoardModel").setProperty("/spotFireData/tableData", aProveData.slice(s - 1, e));
			}
		},

		onAnalyticsTableSortClick: function () {
			var oFragmentId = "idAnalyticsTableSortFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.AnalyticsTableSortList";
			if (!this.AnalyticsTableSort) {
				this.AnalyticsTableSort = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.AnalyticsTableSort);
			}
			this.AnalyticsTableSort.open();
		},

		onAnalyticsTableSort: function (oEvent) {
			var oTable = sap.ui.core.Fragment.byId(this.createId("analyticspotFragment"), "idPotAnalyticsTable");
			this.commonSort(oEvent, oTable);
		},

		commonSort: function (oEvent, oTable) {
			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items");
			var aSorters = [];
			var sPath = mParams.sortItem.getKey();
			var bDescending = mParams.sortDescending;
			var sortData = new sap.ui.model.Sorter(sPath, bDescending, null);
			aSorters.push(sortData);
			oBinding.sort(aSorters);
		},

		onNDTPVTableSearch: function (oEvent) {
			var items = sap.ui.core.Fragment.byId(this.createId("ndtpvFragmane"), "idSpotfireList").getItems();
			var property = this.getModel("dashBoardModel").getData().spotFireData.tableData;
			this.commonTableSearch(oEvent, items, property);
		},

		onAnalyticsTableSearch: function (oEvent) {
			var items = sap.ui.core.Fragment.byId(this.createId("analyticspotFragment"), "idPotAnalyticsTable").getItems();
			var property = this.getModel("dashBoardModel").getData().potData.tableData.taskList;
			this.commonTableSearch(oEvent, items, property);
		},

		commonTableSearch: function (oEvt, items, property) {
			var searchValue = oEvt.getSource().getValue();
			searchValue = searchValue.toLowerCase();
			var v;
			var count = 0;
			var g = null;
			var C = 0;
			for (var i = 0; i < items.length; i++) {
				if (items[i] instanceof sap.m.GroupHeaderListItem) {
					if (g) {
						if (C === 0) {
							g.setVisible(false);
						} else {
							g.setVisible(true);
							g.setCount(C);
						}
					}
					g = items[i];
					C = 0;
				} else {
					v = this.applySearchPatternToListItem(items[i], searchValue, property);
					items[i].setVisible(v);
					if (v) {
						count++;
						C++;
					}
				}
			}
			if (g) {
				if (C == 0) {
					g.setVisible(false);
				} else {
					g.setVisible(true);
					g.setCount(C);
				}
			}
			return count;
		},

		applySearchPatternToListItem: function (i, searchValue, data) {
			if (searchValue == "") {
				return true;
			}
			var property = data[i.getBindingContextPath().split("/").pop()];
			for (var k in property) {
				if (k == "well" || k == "avgVarToForecastBoed" || k == "avgPerVarToForecastBoed" || k == "avgActualBoed" || k ==
					"avgForecastBoed" ||
					k == "avgOil" || k == "avgWater" || k == "avgGas" || k == "lastProdDate" || k == "requestId" || k == "wellName" || k ==
					"varToFcstBOED" || k == "perVarToFcstBOED" || k == "mileStoneStep" || k == "status") {
					var v = property[k];
					if (typeof v == "number") {
						v = v.toString();
					}
					if (typeof v == "string") {
						if (v.toLowerCase().indexOf(searchValue) != -1) {
							return true;
						}
					}
				}
				if (k == "createdAtInString") {
					var v = property[k];
					v = this.getCurrDate(v);
					if (v.toLowerCase().indexOf(searchValue) != -1) {
						return true;
					}
				}
			}
			return false;
		},

		onPressReadMore: function (oEvent) {
			var text = oEvent.getSource().getText();
			var wrap;
			if (text === "Read More") {
				text = "Read Less";
				wrap = true;
			} else {
				text = "Read More";
				wrap = false;
			}
			oEvent.getSource().getParent().getItems()[0].setWrapping(wrap);
			oEvent.getSource().setText(text);
		},
		sendNotification: function (message, pIdPayload, isCreate) {
			if (isCreate) {
				this.sendNotificationWithCount(message, pIdPayload);
			} else {
				var aUrlForLoginNameFetch = "/getLoginNameRest/http/userdetails";
				this.doAjax(aUrlForLoginNameFetch, "POST", pIdPayload, function (oData) {
					var payload = {
						"notification": {
							"alert": message,
							"sound": "iphone_alarm.caf"
						},
						"users": [oData.username]
					};
					var aUrl = "/destination/PushNotification_Dest/mobileservices/restnotification/application/com.incture.taskmanagement/user";
					this.doAjax(aUrl, "POST", payload, function (data) {
						if (data.statusCode === "0") {
							//success
						}
					});
				}.bind(this));
			}
		},
		sendNotificationWithCount: function (message, pIdPayload) {
			var sPids = "";
			pIdPayload.map(function (oItem) {
				sPids = sPids === "" ? sPids + oItem.pId : sPids + "," + oItem.pId;
			});
			var aUrlForLoginNameFetch = "/taskmanagementRest/tasks/getUsersTasksCount?pIDs=" + sPids;
			this.doAjax(aUrlForLoginNameFetch, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					var oArryUsersandCount = oData.userTaskCountList;
					for (var i = 0; i < oArryUsersandCount.length; i++) {
						var payload = {
							"notification": {
								"alert": message,
								"sound": "iphone_alarm.caf",
								"badge": oArryUsersandCount[i].count
							},
							"users": [oArryUsersandCount[i].userName]
						};
						var aUrl = "/destination/PushNotification_Dest/mobileservices/restnotification/application/com.incture.taskmanagement/user";
						this.doAjax(aUrl, "POST", payload, function (oData) {
							if (oData.statusCode === "0") {
								//success
							}
						});
					}
				}

			}.bind(this));
		},
		onAvatarImagePress: function (oEvent) {
			var dashBoardModel = this.getModel("dashBoardModel");
			dashBoardModel.setProperty("/busyIndicators/lightBoxBusy", true);
			var currObj = oEvent.getSource().getBindingContext("oTaskPanelDetailModel").getObject().attachmentDetails["0"];
			var currIndex = oEvent.getSource().getBindingContext("oTaskPanelDetailModel").getPath().split("/").pop();
			var imageSource = oEvent.getSource().getAggregation("detailBox").getAggregation("imageContent")[0];
			if (!currObj.fileDoc) {
				imageSource.setImageSrc("data:" + currObj.fileType + ";base64," + currObj.compressedFile);
				this.getFileDoc(currObj.fileId, imageSource, currIndex);
			} else {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/lightBoxBusy", false);
			}
		},

		getFileDoc: function (fileId, imageSource, currIndex, isDownload) {
			var sUrl = "/taskmanagementRest/collaboration/getAttachmentById?fileId=" + fileId;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.message.statusCode === "0") {
					var currObj = oData.dto;
					if (isDownload) {
						var downloadObj = {};
						downloadObj.base64 = currObj.fileDoc;
						downloadObj.filename = currObj.fileName;
						downloadObj.fileType = currObj.fileType;
						this.downloadFile(downloadObj);
					} else {
						imageSource.setImageSrc("data:" + currObj.fileType + ";base64," + currObj.fileDoc);
						this.getModel("dashBoardModel").setProperty("/busyIndicators/lightBoxBusy", false);
					}
				} else {
					var sErrorMessage = oData.responseMessage.message;
					this._showToastMessage(sErrorMessage);
					this.getModel("dashBoardModel").setProperty("/busyIndicators/lightBoxBusy", false);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				this.getModel("dashBoardModel").setProperty("/busyIndicators/lightBoxBusy", false);
			});
		},

		onExit: function () {
			clearInterval(this.oAlarmRefresh);
			clearInterval(this.oTimerRefresh);
			clearInterval(this.WbRefresh);
		},
		onDopLoad: function () {
			var sSelKey = this.getModel("dashBoardModel").getProperty("/dopData/selectedKey"); //AN: #DOP-DGP
			this.getModel("dashBoardModel").setProperty("/dopData/showDesignate", false);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/dopTblBusy", true);
			var sUrl = "/taskmanagementRest/variance/fetchDOPDGPData";
			var selHier = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			if (selHier === "SEARCH") {
				selHier = "Well";
			}
			var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (selectedLoc.length === 0) {
				this.getModel("dashBoardModel").setProperty("/dopData/tableData", []);
				this.getModel("dashBoardModel").setProperty("/busyIndicators/dopTblBusy", false);
				return;
			}
			var locations = [];
			for (var i = 0; i < selectedLoc.length; i++) {
				locations.push(selectedLoc[i].location);
			}

			var oPayload = {
				"locationType": selHier,
				"locationCodeList": locations,
				"duration": parseInt(this.getModel("dashBoardModel").getProperty("/dopData/durationKey"), 10),
				"countryCode": this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy") //AN: #DOP-DGP
			}
			if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) { //AN: #inquire
				oPayload.userType = "POT";
			} else if (this.getModel("dashBoardModel").getProperty("/userData/isROC")) {
				oPayload.userType = "ROC";
			}
			if (this.getModel("dashBoardModel").getProperty("/dopData/rolledUp") === true) {
				oPayload.rolledUp = true;
			} else if (this.getModel("dashBoardModel").getProperty("/dopData/rolledUp") === false) {
				oPayload.rolledUp = false;
			}
			if (sSelKey === "dop") { //AN: #DOP-DGP
				oPayload.reportId = "DP01";
			} else if (sSelKey === "dgp") {
				oPayload.reportId = "DG01";
			}
			this.getModel("dashBoardModel").setProperty("/dopData/dateInEpoch", new Date().getTime()); //AN: #DOP-DGP
			//	this.doAjax(sUrl, "POST", oPayload, function (oData) {
			// SK : CHG0038615 - DOP/DGP data not in sync with the location selected in Location Hierarchy 
			this.doJquerryAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData) {
						this.getModel("dashBoardModel").setProperty("/dopData/date", oData.dataAsOffDisplay);
						// this.getModel("dashBoardModel").setProperty("/dopData/dateInEpoch", oData.dataAsOffDisplayEpoch); //AN: #DOP-DGP
						this.getModel("dashBoardModel").setProperty("/dopData/tableData", oData.dopVarianceDtoList);
						if (oPayload.rolledUp) {
							this.getModel("dashBoardModel").setProperty("/dopData/currentHierarchyLevel", oData.locationType); //MS Setting new Field For Dop Location		
						} else {
							this.getModel("dashBoardModel").setProperty("/dopData/currentHierarchyLevel", "Well"); //AN: #DOP-DGP
						}
						if (this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy") === "CA" && oData.locationType === "Field") { //AN: #DOP-DGP
							this._showToastMessage(oData.responseMessage.message);
						}
					} else {
						var sErrorMessage = oData.responseMessage.message;
						this._showToastMessage(sErrorMessage);
					}
					this.getModel("dashBoardModel").setProperty("/busyIndicators/dopTblBusy", false);
				}.bind(this),
				function (oError) {
					if (oError.statusText && !oError.statusText === "Abort") {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/dopTblBusy", false);
						var sErrorMessage;
						sErrorMessage = oError.getParameter("statusText");
						this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
					}
				}.bind(this));
		},
		onRightPressDop: function (oEvent) {
			if (oEvent.getParameters().src.getBindingContext("dashBoardModel").getObject().locationType === "Well") {
				var oContext = oEvent.getParameters();
				if (oContext.src.getBindingContext("dashBoardModel")) {
					this.onOpenDopAction(oContext.src, oContext.target);
				}
			}
		},
		_initializeLocalModelForDop: function (oModel) {
			this.getModel("dashBoardModel").setProperty("/dopData/showDesignate", false);
			this.getModel("dashBoardModel").setProperty("/dopData/showInquire", false); //AN: #inquire
			oModel.setProperty("/dopData/selectedKey", "dop"); /*AN: #DOP-DGP*/
		},
		onOpenDopAction: function (oContext, sTarget) {
			var oFragmentId = "idDopActionList",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.alarmActionList";
			var dashBoardModel = this.getModel("dashBoardModel"); //AN: #inquire
			var oBindingContext = oContext.getBindingContext("dashBoardModel"); //AN: #inquire
			var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole"); //SH: WebReadOnly
			var isDOPReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isDOPReadOnly"); //SH: Modules readonly
			if (!this.dopActionList) {
				this.dopActionList = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.dopActionList);
			}
			/*	if (dashBoardModel.getProperty("/userData/isROC") && oBindingContext.getObject().hasInquiry !== true) { //AN: #inquire
					dashBoardModel.setProperty("/dopData/showInquire", true);
				} else {
					dashBoardModel.setProperty("/dopData/showInquire", false);
				}*/
			if (dashBoardModel.getProperty("/userData/isROC")) { //SK:Multiple Inquiry
				if (!isReadOnly && !isDOPReadOnly) {
					dashBoardModel.setProperty("/dopData/showInquire", true);
				}
			} else {
				dashBoardModel.setProperty("/dopData/showInquire", false);
			}
			this.onDopSelectionChange(oContext);
			this.dopActionList.setOffsetX(-30);
			this.dopActionList.openBy(sTarget);
		},
		onDopSelectionChange: function (oEvent) {
			var oAppContId = this.getView().createId("appscollectioncontainer"),
				bDispatch = sap.ui.getCore().byId("idDopActionList--idDispatchBtn"),
				bTrends = sap.ui.getCore().byId("idDopActionList--idTrendBtn"),
				bAcknowledge = sap.ui.getCore().byId("idDopActionList--idAcknowledgeBtn"),
				currObj = oEvent.getBindingContext("dashBoardModel").getObject();
			var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole"); //SH: WebReadOnly
			var isDOPReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isDOPReadOnly"); //SH: Modules readonly
			this.getModel("dashBoardModel").setProperty("/currDopObject", currObj);
			bTrends.setVisible(true);
			bAcknowledge.setVisible(false);
			if (this.getModel("dashBoardModel").getProperty("/userData/isROC") && !isReadOnly && !isDOPReadOnly) {
				bDispatch.setVisible(true);
			} else {
				bDispatch.setVisible(false);
			}
		},

		onIconPress: function (oEvent) {
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeSeat
			var oUserData = oDashBoardModel.getProperty("/userData");
			var aTaskTypeDetails = [], //AN: #ChangeSeat
				oTaskTypeDetails = {}, //AN: #ChangeSeat
				sWho = "",
				oObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			oDashBoardModel.setProperty("/pwHopperObjForPlotly", oObject);
			var sTooltip = oEvent.getSource().getTooltip(); //AN: #inquiryEnhancement
			var sTaskType = "IconClick"; //AN: #inquiryEnhancement
			if (oUserData.isROC) {
				sWho = "ROC";
			} else if (oUserData.isENG) {
				sWho = "Engineer";
			} else if (oUserData.isPOT) {
				sWho = "POT";
			}
			oTaskTypeDetails = { //AN: #ChangeSeat
				sTooltip: oEvent.getSource().getTooltip(),
				oCurrSelectedLocation: oObject,
				sLocCode: oObject.locationCode,
				sLocType: oObject.locationType,
				sLocText: oObject.location
			};
			aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
			if (sTooltip === "Investigation" || sTooltip.includes("Proactive")) { //AN: #inquiryEnhancement
				sTaskType = "IconClick-Investigation";
			} else if (sTooltip.includes("Dispatch")) {
				sTaskType = "IconClick-Dispatch";
			}
			ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, sTaskType, sWho); //AN: #ChangeSeat //AN: #inquiryEnhancement
		},
		onDopTableSortClick: function () {
			var oFragmentId = "idDopTableSortFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.DopSortList";
			if (!this.DopTableSort) {
				this.DopTableSort = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.DopTableSort);
			}
			this.DopTableSort.open();
		},

		onDopTableSort: function (oEvent) {
			var selectedKey = this.getModel("dashBoardModel").getProperty("/dopData/selectedKey"); /*AN: #DOP-DGP*/
			if (selectedKey === "dop") { /*AN: #DOP-DGP*/
				var oTable = sap.ui.core.Fragment.byId(this.createId("dopFragment"), "idDopList");
			} else if (selectedKey === "dgp") {
				var oTable = sap.ui.core.Fragment.byId(this.createId("dopFragment"), "idDgpList");
			}
			this.commonSort(oEvent, oTable);
		},
		onPressMyTask: function (oEvent) {
			var oWebSocketPropertyData = this.getModel("dashBoardModel").getProperty("/webSocketProperty/data"); //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			this.getModel("dashBoardModel").setProperty("/taskList", []);
			this.getModel("dashBoardModel").setProperty("/selectedTaskTab", "All");
			if (oWebSocketPropertyData && oWebSocketPropertyData.taskNotificationDto && oWebSocketPropertyData.taskNotificationDto
				.taskCount > 0) { //AN: #Notif
				this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "Tasks"); //AN: #highlightShift
				// this.acknowledgeWebSocketRequest("Tasks"); //AN: #BubbleNotifDemo
			}
			TaskManagementHelper._bindRightTaskPanelModel(this, "All"); //AN: #ScratchFilter
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showTaskManagementPanel", true);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showBypassPanel", false); //Anand: #handover
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showOpAvailPanel", false); //AN: #OpAvail
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showHandoverNotesPanel", false); //SH: HandoverNotes
			this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", "All");
		},
		onPressSentItems: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			this.getModel("dashBoardModel").setProperty("/taskList", []);
			this.getModel("dashBoardModel").setProperty("/selectedTaskTab", "Sent Items");
			this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", "Sent Items");
			TaskManagementHelper._bindRightTaskPanelModel(this, "Sent Items"); //AN: #ScratchFilter
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showTaskManagementPanel", true);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showBypassPanel", false); //Anand: #handover
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showOpAvailPanel", false); //AN: #OpAvail
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showHandoverNotesPanel", false); //SH: HandoverNotes
		},
		onPressNonDispatch: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			this.getModel("dashBoardModel").setProperty("/taskList", []);
			this.getModel("dashBoardModel").setProperty("/selectedTaskTab", "Non-Dispatch");
			this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", "Non-Dispatch");
			TaskManagementHelper._bindRightTaskPanelModel(this, "Non-Dispatch"); //AN: #ScratchFilter
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showTaskManagementPanel", true);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showBypassPanel", false); //Anand: #handover
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showOpAvailPanel", false); //AN: #OpAvail
			var taskmanagementpane1 = this.getView().createId("taskmanagementpane1");
			var idTaskHeaderSegmentedButton = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskHeaderSegmentedButton"); //AN: #taskFilter
			idTaskHeaderSegmentedButton.addStyleClass("sapUiMediumMarginBottom");
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showHandoverNotesPanel", false); //SH: HandoverNotes
		},

		onPressBack: function () {
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showTaskManagementPanel", false);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showBypassPanel", false); //Anand: #handover
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showOpAvailPanel", false); //AN: #OpAvail
			this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", undefined);
			this.getView().getModel("dashBoardModel").setProperty("/highlightShiftHandoverList", []); //AN: #highlightShift
		},
		onOpenTaskOperationSuggestion: function (oEvent) {
			this._currentTaskObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			this.getModel("dashBoardModel").setProperty("/taskCardData", this._currentTaskObject);
			this.getModel("dashBoardModel").refresh(true);
			var oFragmentId = "idTaskManagementTaskOperationSuggestion",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.TaskManagementTaskOperationSuggestion";
			if (!this.TaskManagementTaskOperationSuggestion) {
				this.TaskManagementTaskOperationSuggestion = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.TaskManagementTaskOperationSuggestion);
			}
			this.TaskManagementTaskOperationSuggestion.openBy(oEvent.getSource());
		},
		onSelectAlarmClassification: function (oEvent) {
			var oContext = oEvent.getSource().getBindingContext("oReportDetailModel").getObject();
			var sBtnText /* = oEvent.getSource().getSelectedButton().getText()*/ ;
			if (oEvent.getParameters().state === true) {
				sBtnText = "Downtime";
			} else {
				sBtnText = "Non Downtime";
			}
			oContext.downTimeClassifier = sBtnText;
			var sUrl = "/taskmanagementRest/alarmFeed/updateClassifier";
			this.doAjax(sUrl, "POST", oContext, function (oData) {
				if (oData.statusCode === "0") {
					this.getAlarmData();
				} else {
					this._showToastMessage(oData.responseMessage.message);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},

		onSelectionPageSize: function (oEvent) {
			var oDefaultDataModel = this.getModel("dashBoardModel"),
				totalTasks = parseInt(oDefaultDataModel.getProperty("/alarmCount"), 10),
				pageValue = oEvent.getParameters().item.getText(),
				lowerLimit = 1,
				upperLimit = pageValue,
				totalAlarmListCount = totalTasks;

			this.getModel("dashBoardModel").setProperty("/lowerLimitPageValue", lowerLimit);
			this.getModel("dashBoardModel").setProperty("/upperLimitPageValue", upperLimit);
			this.getModel("dashBoardModel").setProperty("/pageFactor", upperLimit);
			this.getModel("dashBoardModel").setProperty("/selectedPage", 1);
			this.getAlarmData();
		},
		onScrollLeftPage: function (oEvent) {
			var pageNumber = parseInt(this.getModel("dashBoardModel").getProperty("/selectedPage"), 10),
				tasksPerPage = parseInt(this.getModel("dashBoardModel").getProperty("/alarmPageCount"), 10),
				originalTasksPerPage = parseInt(this.getModel("dashBoardModel").getProperty("/pageFactor"), 10);
			this.getModel("dashBoardModel").setProperty("/selectedPage", pageNumber - 1);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/alarmListBusy", true);
			var oAppContId = this.getView().createId("alarmFragment");
			sap.ui.getCore().byId(oAppContId + "--idLeftButton").setEnabled(false);
			sap.ui.getCore().byId(oAppContId + "--idRightButton").setEnabled(false);
			this.getAlarmData();
			this.getPageValueBoundaries("left", pageNumber, originalTasksPerPage);
		},
		onScrollRightPage: function (oEvent) {
			var pageNumber = parseInt(this.getModel("dashBoardModel").getProperty("/selectedPage"), 10),
				tasksPerPage = parseInt(this.getModel("dashBoardModel").getProperty("/alarmPageCount"), 10),
				originalTasksPerPage = parseInt(this.getModel("dashBoardModel").getProperty("/pageFactor"), 10);
			this.getModel("dashBoardModel").setProperty("/selectedPage", pageNumber + 1);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/alarmListBusy", true);
			var oAppContId = this.getView().createId("alarmFragment");
			sap.ui.getCore().byId(oAppContId + "--idLeftButton").setEnabled(false);
			sap.ui.getCore().byId(oAppContId + "--idRightButton").setEnabled(false);
			this.getAlarmData();
			this.getPageValueBoundaries("right", pageNumber, originalTasksPerPage);
		},
		getPageValueBoundaries: function (side, pageNumber, tasksPerPage) {
			var lowerLimitPageValue, upperLimitPageValue,
				totalTasks = parseInt(this.getModel("dashBoardModel").getProperty("/alarmCount"), 10);
			if (side === "left") {
				lowerLimitPageValue = (tasksPerPage * (pageNumber - 2)) + 1;
				upperLimitPageValue = (tasksPerPage * (pageNumber - 1));
			} else {
				lowerLimitPageValue = (tasksPerPage * (pageNumber)) + 1;
				upperLimitPageValue = (tasksPerPage * (pageNumber + 1));
			}
			this.getModel("dashBoardModel").setProperty("/lowerLimitPageValue", lowerLimitPageValue);
			this.getModel("dashBoardModel").setProperty("/upperLimitPageValue", upperLimitPageValue);

			if (upperLimitPageValue >= totalTasks) {
				sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPageRange").setText(lowerLimitPageValue + " - " +
					totalTasks + " of " + totalTasks);
			} else {
				sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPageRange").setText(lowerLimitPageValue + " - " +
					upperLimitPageValue + " of " + totalTasks);
			}
		},
		checkForPaginationButton: function () {
			var oDefaultDataModel = this.getModel("dashBoardModel"),
				pageNumber = parseInt(this.getModel("dashBoardModel").getProperty("/selectedPage"), 10),
				totalTasks = parseInt(oDefaultDataModel.getProperty("/alarmCount"), 10),
				tasksPerPage = parseInt(oDefaultDataModel.getProperty("/alarmPageCount"), 10),
				originalTasksPerPage = parseInt(this.getModel("dashBoardModel").getProperty("/pageFactor"), 10),
				oAppContId = this.getView().createId("alarmFragment");

			this.getModel("dashBoardModel").setProperty("/lowerLimitPageValue", 1);
			this.getModel("dashBoardModel").setProperty("/upperLimitPageValue", originalTasksPerPage);
			sap.ui.getCore().byId(oAppContId + "--idLeftButton").setEnabled(true);
			sap.ui.getCore().byId(oAppContId + "--idRightButton").setEnabled(true);
			if (pageNumber === 1) {
				this.getModel("dashBoardModel").setProperty("/lowerLimitPageValue", 1);
				this.getModel("dashBoardModel").setProperty("/upperLimitPageValue", originalTasksPerPage);
				if (totalTasks <= originalTasksPerPage) {
					sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPageRange").setText(" 1 - " + totalTasks + " of " +
						totalTasks);
				} else {
					sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idPageRange").setText(" 1 - " + originalTasksPerPage + " of " +
						totalTasks);
				}
				sap.ui.getCore().byId(oAppContId + "--idLeftButton").setEnabled(false);
				if ((totalTasks / originalTasksPerPage) <= pageNumber) {
					sap.ui.getCore().byId(oAppContId + "--idRightButton").setEnabled(false);
				}
			}
			if ((totalTasks / originalTasksPerPage) <= pageNumber) {
				sap.ui.getCore().byId(oAppContId + "--idRightButton").setEnabled(false);
			}
		},
		onPressCreateTaskVBox: function () {
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showHandoverNotesPanel", false); //SH: HandoverNotes
			this.getModel("dashBoardModel").setProperty("/taskStatus", "NEW");
			this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", "Create");
			this.getModel("dashBoardModel").setProperty("/taskCreation", "Custom");
			var currSelectedLocation = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (currSelectedLocation.length === 1 && currSelectedLocation[0].locationType === "Compressor") {
				this.getCompressorParent("", currSelectedLocation);
			} else {
				this.TaskCreationHierarchyValidation();
			}
			//	this.onCreateTaskPress();
		},
		onPressMyTaskMain: function (oEvent) {
			var filterFields; //SH: Filter based on ChangeSeat
			var hasCanadaRole = this.getModel("dashBoardModel").getProperty("/userData/hasCanadaRole");
			var hasUSRole = this.getModel("dashBoardModel").getProperty("/userData/hasUSRole");
			if (hasUSRole && hasCanadaRole) {
				filterFields = [{
					key: "MUR-US-EFS-CT00",
					text: "Catarina"
				}, {
					key: "MUR-US-EFS-KN00",
					text: "Karnes North"
				}, {
					key: "MUR-US-EFS-KS00",
					text: "Karnes South"
				}, {
					key: "MUR-US-EFS-TC00",
					text: "Tilden Central"
				}, {
					key: "MUR-US-EFS-TE00",
					text: "Tilden East"
				}, {
					key: "MUR-US-EFS-TN00",
					text: "Tilden North"
				}, {
					key: "MUR-US-EFS-TW00",
					text: "Tilden West"
				}, {
					key: "MUR-CA-KAY",
					text: "Kaybob"
				}, {
					key: "MUR-CA-MNT",
					text: "Montney"
				}];
			} else if (hasUSRole && !hasCanadaRole) {
				filterFields = [{
					key: "MUR-US-EFS-CT00",
					text: "Catarina"
				}, {
					key: "MUR-US-EFS-KN00",
					text: "Karnes North"
				}, {
					key: "MUR-US-EFS-KS00",
					text: "Karnes South"
				}, {
					key: "MUR-US-EFS-TC00",
					text: "Tilden Central"
				}, {
					key: "MUR-US-EFS-TE00",
					text: "Tilden East"
				}, {
					key: "MUR-US-EFS-TN00",
					text: "Tilden North"
				}, {
					key: "MUR-US-EFS-TW00",
					text: "Tilden West"
				}];
			} else if (!hasUSRole && hasCanadaRole) {
				filterFields = [{
					key: "MUR-CA-KAY",
					text: "Kaybob"
				}, {
					key: "MUR-CA-MNT",
					text: "Montney"
				}];
			}
			this.getModel("dashBoardModel").setProperty("/taskFilterData/fields", filterFields);
			this.getModel("dashBoardModel").setProperty("/taskFilterData/tempFields", filterFields);
			var selectedVBox = this.getModel("dashBoardModel").getData().selectedTaskManagementVBox;
			if (selectedVBox !== "All" && selectedVBox !== "Non-Dispatch" && selectedVBox !== "Sent Items" && selectedVBox !== "Create" &&
				selectedVBox !== "ShiftHandover" && selectedVBox !== "OpAvail" && selectedVBox !== "HandoverNotes") { //AN: #OpAvail SH: HandoverNotes
				this.onPressMyTask();
			}
			if (selectedVBox === "Create") {
				this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", undefined);
			}
			if (selectedVBox === "HandoverNotes") { //SH: HandoverNotes
				// this.getModel("dashBoardModel").setProperty("/selectedTaskManagementVBox", "HandoverNotes");
			}
		},

		onPressProdVarianceAction: function (oEvent) { //AN: #inquire
			var pVAction = oEvent.getSource().getText();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeSeat
			var oUserData = oDashBoardModel.getProperty("/userData"); //AN: #ChangeSeat
			var sWho = ""; //AN: #ChangeSeat
			var aTaskTypeDetails = [], //AN: #ChangeSeat
				oTaskTypeDetails = {}; //AN: #ChangeSeat
			if (oUserData.isPOT) { //AN: #ChangeSeat
				sWho = "POT";
			} else if (oUserData.isENG) {
				sWho = "Engineer";
			}
			if (pVAction === "Inquiry") {
				oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currentProveObject"),
					sLocCode: oDashBoardModel.getProperty("/currentProveObject/locationCode"),
					sLocType: "Well",
					sLocText: oDashBoardModel.getProperty("/currentProveObject/well")
				};
				aTaskTypeDetails.push(oTaskTypeDetails);
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Module-Inquiry", sWho, oEvent); //AN: #ChangeSeat
			} else if (pVAction === "Investigate") {
				oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currentProveObject"),
					sLocCode: oDashBoardModel.getProperty("/currentProveObject/locationCode"),
					sLocType: "Well",
					sLocText: oDashBoardModel.getProperty("/currentProveObject/well")
				};
				aTaskTypeDetails.push(oTaskTypeDetails);
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Module-Investigation", sWho, oEvent); //AN: #ChangeSeat
			} else if (pVAction === "Trends") {
				this.onTrendsPress();
			} else if (pVAction === "History") {
				this.viewInvestigationHistory(oEvent);
			} else if (pVAction === "Procount" || pVAction === "ProdView") {
				this.getModel("oTaskPanelDetailModel").setProperty("/procountUpdateFromProve", true);
				var muwi = this.getModel("dashBoardModel").getProperty("/currentProveObject/muwiId");
				this.getModel("oTaskPanelDetailModel").setProperty("/muwiId", muwi);
				// this.onCompletingInvestigation(oEvent);
				oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currentProveObject"),
					sLocCode: oDashBoardModel.getProperty("/currentProveObject/locationCode"),
					sLocType: "Well",
					sLocText: oDashBoardModel.getProperty("/currentProveObject/well")
				};
				aTaskTypeDetails.push(oTaskTypeDetails);
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Prove-Comment", sWho, oEvent); //AN: #ChangeSeat
			} else if (pVAction === "Location History") {
				this.getModel("dashBoardModel").setProperty("/panelProperties/currentSelectKey", "locHistory");
				this.setSelectedModule(); //AN: #Notif
				this.getView().getModel("dashBoardModel").setProperty("/locationHistorySelectTabs/iconFilterTab", "Task");
				this.setSelectedLocationExternally(oDashBoardModel.getProperty("/currentProveObject"), "LocationHistory"); //AN: #Notif
			}
		},
		viewInvestigationHistory: function (oEvent) { //AN: #inquire
			this.getModel("dashBoardModel").setProperty("/busyIndicators/investigationHistoryBusy", true);
			var oFragmentId = "investigationHistoryId",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.investigationHistory";
			if (!this.oInvestigationHistoryFragment) {
				this.oInvestigationHistoryFragment = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oInvestigationHistoryFragment);
			}
			this.oInvestigationHistoryFragment.open();
			this.getInvestigationHistory(this.getModel("dashBoardModel").getProperty("/currentProveObject/muwiId"));
		},
		onInvestigationHistoryClose: function (oEvent) { //AN: #inquire
			if (this.oInvestigationHistoryFragment) {
				this.oInvestigationHistoryFragment.close();
			}
		},
		onPressCreateInquiry: function (oEvent, currSelectedLocation) { //AN: #inquire
			var dashBoardModel = this.getModel("dashBoardModel");
			var oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
			dashBoardModel.setProperty("/isInquiryCreate", true);
			var currentUser = this._getCurrentLoggedInUser();
			var userType, durationKey, selectedLocation, muwi;
			var oFragmentId = "idCreateInquiryPanel",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.createInquiryPanel";
			if (!this.oCreateInquiryFragment) {
				this.oCreateInquiryFragment = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oCreateInquiryFragment);
			}
			if (dashBoardModel.getProperty("/userData").isENG) {
				userType = "ENG";
				durationKey = dashBoardModel.getProperty("/spotFireData/durationKey");
				dashBoardModel.setProperty("/dynamicContentType", {
					userType: "ENG",
					taskType: "Inquiry"
				});
				selectedLocation = currSelectedLocation.well;
				muwi = currSelectedLocation.muwiId;
			} else if (dashBoardModel.getProperty("/userData").isROC) {
				userType = "ROC";
				durationKey = dashBoardModel.getProperty("/dopData/durationKey");
				dashBoardModel.setProperty("/dynamicContentType", {
					userType: "ROC",
					taskType: "Inquiry"
				});
				selectedLocation = currSelectedLocation.location;
				muwi = currSelectedLocation.muwi;
				if (dashBoardModel.getProperty("/InsightToActionData/isWBInquiry")) { //AN: #msgToROC
					selectedLocation = currSelectedLocation.locationText;
				} else if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "ndtpv" && dashBoardModel.getProperty("/userData").isPOT) {
					userType = "POT";
					durationKey = dashBoardModel.getProperty("/spotFireData/durationKey");
					dashBoardModel.setProperty("/dynamicContentType", {
						userType: "POT",
						taskType: "Inquiry"
					});
					selectedLocation = currSelectedLocation.well;
					muwi = currSelectedLocation.muwiId;
				}
			} else if (dashBoardModel.getProperty("/userData").isPOT) {
				userType = "POT";
				durationKey = dashBoardModel.getProperty("/spotFireData/durationKey");
				dashBoardModel.setProperty("/dynamicContentType", {
					userType: "POT",
					taskType: "Inquiry"
				});
				selectedLocation = currSelectedLocation.well;
				muwi = currSelectedLocation.muwiId;
			}
			var sUrl = "/taskmanagementRest/tasks/getHeader?taskTempId=INQ&muwi=" + muwi;
			sUrl = sUrl + "&location=" + encodeURIComponent(selectedLocation) + "&locType=Well&locationCode=" +
				encodeURIComponent(currSelectedLocation.locationCode) + "&userType=" + userType;
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true); //AN:
			this.doAjax(sUrl, "GET", null, function (oData) {
				dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
				if (oData.responseMessage.statusCode === "0") {
					this.oCreateInquiryFragment.open();
					var tempData = oData;
					tempData.taskEventDto = {
						createdBy: currentUser,
						createdByDisplay: dashBoardModel.getProperty("/userData/displayName"),
						description: "",
						subject: "",
						status: "NEW",
						owners: [],
						ownersName: null,
						locationCode: currSelectedLocation.locationCode
					};
					tempData.collabrationDtos = [];
					tempData.status = "ASSIGNED";
					tempData.muwiId = currSelectedLocation.muwiId;
					tempData.taskEventDto.taskMode = durationKey;
					oTaskPanelDetailModel.setData(tempData);
					var oTaskList1 = sap.ui.core.Fragment.byId("idCreateInquiryPanel", "idInquiryDetailsPanel--idInquiryObservationGrid");
					if (oData.observationCustomAttr) {
						this.createDynamicCont(oTaskList1, "observationCustomAttr");
					}
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					if (dashBoardModel.getProperty("/InsightToActionData/isWBInquiry")) { //AN: #msgToROC
						dashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
					}
				} else {
					this._createConfirmationMessage("Error", oData.responseMessage.message, "Error", "", "Close", false, null);
				}
			}.bind(this), function (oError) {
				dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onCreateInquiryClose: function (oEvent) { //AN: #inquire
			if (this.oCreateInquiryFragment) {
				this.oCreateInquiryFragment.close();
			}
			this.onClosingDetailTask();
		},
		onInquiryDescLiveChange: function (oEvent) { //AN: #inquire
			var taskEventDto = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto");
			taskEventDto.description = oEvent.getSource().getValue();
			this.getModel("oTaskPanelDetailModel").setProperty("/taskEventDto", taskEventDto);
		},
		onPressInquiryButtons: function (oEvent) { //AN: #inquire
			var dashBoardModel = this.getModel("dashBoardModel");
			switch (oEvent.getSource().getText()) {
			case "Create Inquiry":
				this.onCreatingInquiry(oEvent);
				break;
			case "Update Inquiry":
				var inquiryStatus = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/status");
				if (inquiryStatus === "RESOLVED") {
					this.onCreatingInquiry(oEvent);
				} else if (inquiryStatus === "ASSIGNED") {
					this.onPressUpdateTask(oEvent);
				}
				break;
			case "Resolve":
				this.onResolveInquiry(oEvent);
				break;
			case "Close Inquiry":
				this.onCompletingInquiry(oEvent);
				break;
			case "Forward":
				this.onForwardInquiry(oEvent);
				break;
			case "Investigate":
				dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
					userType: "POT",
					taskType: "Investigation",
					from: "Inquiry"
				});
				this.onPressCreateInvestigation(oEvent);
				this.getModel("dashBoardModel").setProperty("/isInvestigationCreate", true);
				break;
			case "Dispatch":
				dashBoardModel.setProperty("/dynamicContentType", { //AN: #inquire
					userType: "ROC",
					taskType: "Dispatch",
					from: "Inquiry"
				});
				//Sk: Check Authoriztion.
				//	dashBoardModel.setProperty("/taskStatus", "NEW");
				var oCurrentObject = dashBoardModel.getData().taskCardData;
				var oTaskTypeDetails = {
					oCurrSelectedLocation: [oCurrentObject],
					sLocCode: oCurrentObject.locationCode,
					sLocType: "well",
					sLocText: oCurrentObject.location
				};
				var aTaskTypeDetails = [];
				aTaskTypeDetails.push(oTaskTypeDetails);
				ChangeSeatHelper.checkAuthorization(this, dashBoardModel, aTaskTypeDetails, "Inquiry-Dispatch", "ROC", oEvent);
				//	this.onCreateTaskPress("", "", true);
				break;
			default:
				this.onCreateInquiryClose();
				break;
			}
		},
		onCreatingInquiry: function (oEvent) { //AN: #inquire
			var dashBoardModel = this.getModel("dashBoardModel");
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true);
			var dynamicContentType = dashBoardModel.getProperty("/dynamicContentType"); //AN: #inquire
			var oTaskPanelDetailData = this.getModel("oTaskPanelDetailModel").getData();
			var sUrl = "/taskmanagementRest/tasks/createTask";
			var userData = dashBoardModel.getProperty("/userData");
			var currObj = dashBoardModel.getProperty("/currentSelectedObjects");

			if (!currObj || currObj.length === 0) { //AN: #inquiryEnhancement
				// if no users are selected then send all the users in the list. Special case.
				currObj = dashBoardModel.getProperty("/suggestionItems");
				oTaskPanelDetailData.taskEventDto.isGroupTask = "true";
			} else {
				oTaskPanelDetailData.taskEventDto.isGroupTask = "false";
			}

			// if (dynamicContentType.userType === "ENG") { //AN: #inquiryEnhancement
			// 	if (!currObj || currObj.length === 0) { // if no users are selected then send all the users in the list. Special case.
			// 		currObj = dashBoardModel.getProperty("/suggestionItems");
			// 		oTaskPanelDetailData.taskEventDto.isGroupTask = "true";
			// 	} else {
			// 		oTaskPanelDetailData.taskEventDto.isGroupTask = "false";
			// 	}
			// } else {
			// 	if (!currObj || currObj.length === 0) {
			// 		this._showToastMessage("Please select assignee to create inquiry");
			// 		dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
			// 		return;
			// 	}
			// }

			oTaskPanelDetailData.taskEventDto.owners = [];
			for (var i = 0; i < currObj.length; i++) {
				var lastName = currObj[i].lastName;
				var firstName = currObj[i].firstName;
				if (lastName) {
					firstName = firstName + " " + lastName;
				}
				oTaskPanelDetailData.taskEventDto.owners.push({
					"taskOwner": currObj[i].userId,
					"taskOwnerDisplayName": firstName,
					"ownerEmail": currObj[i].userId,
					"pId": currObj[i].pId
				});
			}
			oTaskPanelDetailData.taskEventDto.origin = "Inquiry";
			oTaskPanelDetailData.taskEventDto.parentOrigin = "Inquiry";
			var oGridContent = sap.ui.getCore().byId("idCreateInquiryPanel--idInquiryDetailsPanel--idInquiryObservationGrid").getContent();
			var assignToGroupId = dashBoardModel.getProperty("/assignToGroupId");
			var primaryClassificationId = dashBoardModel.getProperty("/primaryClassificationId");
			var assignToGroupValue = oGridContent[assignToGroupId].getItems()[1].getSelectedKey();
			var primaryClassificationValue = oGridContent[primaryClassificationId].getItems()[1].getSelectedKey();
			oTaskPanelDetailData.observationCustomAttr[assignToGroupId].labelValue = assignToGroupValue;
			oTaskPanelDetailData.observationCustomAttr[primaryClassificationId].labelValue = primaryClassificationValue;
			var custAtt = oTaskPanelDetailData.observationCustomAttr;
			for (var j = 0; j < custAtt.length; j++) {
				if (custAtt[j].isMandatory && (custAtt[j].labelValue === "" || custAtt[j].labelValue === null || custAtt[j].labelValue ===
						undefined) && custAtt[j].label !== "Assign to person(s)") {
					this._showToastMessage("Fill all mandatory fields");
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					return;
				}
			}
			if (!oTaskPanelDetailData.taskEventDto.description) {
				this._showToastMessage("Fill all mandatory fields");
				dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
				return;
			}
			var oPayload = {};
			if (oTaskPanelDetailData.taskEventDto.processId) {
				oPayload.observationCustomAttr = oTaskPanelDetailData.observationCustomAttr;
				oPayload.taskEventDto = oTaskPanelDetailData.taskEventDto;
				oPayload.taskEventDto.subject = oTaskPanelDetailData.observationCustomAttr[0].labelValue +
					" - " + primaryClassificationValue;
			} else {
				oTaskPanelDetailData.taskEventDto.createdBy = userData.userId;
				oTaskPanelDetailData.taskEventDto.createdByDisplay = userData.displayName;
				if (dynamicContentType.userType === "ENG") {
					var taskCreationGroup = dashBoardModel.getProperty("/changeSeatData/taskCreationRole");
					oTaskPanelDetailData.taskEventDto.group = taskCreationGroup;
					//	oTaskPanelDetailData.taskEventDto.group = userData.engRole.split(",")[0];
					oTaskPanelDetailData.taskEventDto.subject = dashBoardModel.getProperty("/currentProveObject/well") +
						" - " + primaryClassificationValue;
				} else if (dynamicContentType.userType === "POT") {
					taskCreationGroup = dashBoardModel.getProperty("/changeSeatData/taskCreationRole");
					oTaskPanelDetailData.taskEventDto.group = taskCreationGroup;
					//	oTaskPanelDetailData.taskEventDto.group = userData.potRole.split(",")[0];
					oTaskPanelDetailData.taskEventDto.subject = dashBoardModel.getProperty("/currentProveObject/well") +
						" - " + primaryClassificationValue;
				} else {
					oTaskPanelDetailData.taskEventDto.group = userData.group;
					oTaskPanelDetailData.taskEventDto.subject = dashBoardModel.getProperty("/currDopObject/location") +
						" - " + primaryClassificationValue;
					if (dashBoardModel.getProperty("/InsightToActionData/isWBInquiry")) { //AN: #msgToROC
						oTaskPanelDetailData.taskEventDto.subject = dashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData/locationText") +
							" - " + primaryClassificationValue;
					}
				}
				oPayload = oTaskPanelDetailData;
			}
			oPayload.taskEventDto.status = "ASSIGNED";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "ndtpv") {
							this.getProveReport();
						} else if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "DOP") {
							this.onDopLoad();
						}
						if (this.getModel("dashBoardModel").getProperty("/selectedTaskTab") === "Sent Items") {
							TaskManagementHelper._bindRightTaskPanelModel(this, this.getModel("dashBoardModel").getProperty("/selectedTaskTab")); //AN: #ScratchFilter
						}
						this.onCreateInquiryClose();
						if (this.oWorkbenchMsgToROCPanel && dashBoardModel.getProperty("/InsightToActionData/isWBInquiry")) { //AN: #msgToROC
							WorkbenchHelper.onWBMsgClose(this, this.getModel("dashBoardModel"));
						}
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		onResolveInquiry: function (oEvent) { //AN: #inquire
			this.onRemoveTask(this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskId"), false, "", true,
				"ResolveInquiry");
		},
		onCompletingInquiry: function (oEvent) { //AN: #inquire
			var sMsg = "Are you sure you want to complete this task?",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
				this.onRemoveTask("", false, this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/processId"), true);
			});
		},
		onForwardInquiry: function (oEvent) { //AN: #inquire
			var sUrl = "/taskmanagementRest/tasks/updateTask";
			var oPayload = this.getModel("oTaskPanelDetailModel").getData();
			var dashBoardModel = this.getModel("dashBoardModel");
			if (dashBoardModel.getProperty("/assignedToIndex") !== null) {
				var currObj = this.getModel("dashBoardModel").getProperty("/currentSelectedObjects");
				oPayload.taskEventDto.owners = [];

				if (!currObj || currObj.length === 0) { //AN: #inquiryEnhancement
					// if no users are selected then send all the users in the list. Special case.
					currObj = dashBoardModel.getProperty("/suggestionItems");
					oPayload.taskEventDto.isGroupTask = "true";
				} else {
					oPayload.taskEventDto.isGroupTask = "false";
				}

				for (var i = 0; i < currObj.length; i++) {
					var lastName = currObj[i].lastName;
					var firstName = currObj[i].firstName;
					if (lastName) {
						firstName = firstName + " " + lastName;
					}
					oPayload.taskEventDto.owners.push({
						"taskOwner": currObj[i].userId,
						"taskOwnerDisplayName": firstName,
						"ownerEmail": currObj[i].userId,
						"pId": currObj[i].pId
					});
				}
				// if (oPayload.taskEventDto.owners.length === 0 || (!oPayload.taskEventDto.owners)) {
				// 	this._showToastMessage("Select the Assignee to the task");
				// 	return;
				// }

				var arrUsers = oPayload.taskEventDto.owners;
				var arrOwners = [];
				for (var j = 0; j < arrUsers.length; j++) {
					arrOwners.push(arrUsers[j].taskOwnerDisplayName.split(" ").join(""));
				}
				oPayload.taskEventDto.status = "FORWARDED";
				if (dashBoardModel.getProperty("/userData/isPOT") || dashBoardModel.getProperty("/userData/isROC")) { //AN: #inquiryEnhancement
					// oPayload.forwardToGrp = "ROC";
					// } else if (dashBoardModel.getProperty("/userData/isROC")) {
					// oPayload.forwardToGrp = "POT";
					for (var a = 0; a < oPayload.observationCustomAttr.length; a++) {
						if (oPayload.observationCustomAttr[a].label === "Assign to group") {
							oPayload.forwardToGrp = oPayload.observationCustomAttr[2].valueDtos[0].value;
							break;
						}
					}
				}
				oPayload.taskEventDto.forwardedBy = dashBoardModel.getProperty("/userData/userId");
			}
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanel2Busy", false);
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						TaskManagementHelper._bindRightTaskPanelModel(this, dashBoardModel.getProperty("/selectedTaskTab")); //AN: #ScratchFilter
						this.onCreateInquiryClose();
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		createDynamicContentPWCheckList: function (oTaskList, sCustAttr) {
			oTaskList.removeAllItems();
			var currentLogeddUserType = this.onGetUserDataPWHopper(),
				dynamicControlData = this.getModel("oTaskPanelDetailModel").getData()[sCustAttr];
			if (dynamicControlData === null) {
				this.getModel("oTaskPanelDetailModel").setProperty("/visibleChecklist", false);
			}
			if (dynamicControlData !== null) {
				var dynamicContInvToBeCreated = [],
					dynamicContInvUserTypes = [];
				for (var i = 0; i < dynamicControlData.length; i++) {
					var userType = dynamicControlData[i].userType;
					for (var inv = 0; inv < currentLogeddUserType.length; inv++) { //AN: #pw
						if (userType === currentLogeddUserType[inv]) {
							dynamicContInvToBeCreated.push(dynamicControlData[i]);
							dynamicContInvUserTypes.push(userType);
							break;
						}
					}
					var newControl = new sap.m.Panel({
						height: "100%",
						headerText: this.formatter.fnGetPWChecklistText(dynamicControlData[i].userType),
						expandable: true,
						expanded: true
					}).addStyleClass("sapUiTinyMarginBottom sapUiSizeCompact pWChecklistRespPanelPadding");
					var newGridControl = new sap.ui.layout.Grid({
						defaultSpan: "L6 M6 S12",
						vSpacing: 0
					}).addStyleClass("sapUiTinyMarginTop sapUiSizeCompact");
					newControl.addContent(newGridControl);
					var checkListItem = dynamicControlData[i].checkList;
					for (var j = 0; j < checkListItem.length; j++) {
						if (checkListItem[j].dataType === "Input") {
							var inputControl = new sap.m.HBox({
								width: "100%",
								items: [new sap.m.Input({
										width: "100%",
										editable: false,
										value: checkListItem[j].value
									}).addStyleClass("sapUiSizeCompact iopInputClass iopwbTaskBoderLightClass"),
									new sap.m.Label({
										text: checkListItem[j].checkListItem
									}).addStyleClass("iopFontClass iopwbTaskLabelClass sapUiTinyMargin sapUiSizeCompact iopwbLabelRequiredClass")
								]
							}).addStyleClass("sapUiSizeCompact pwInputGridHSpace1");
							newGridControl.addContent(inputControl);
						} else if (checkListItem[j].dataType === "CheckBox") {
							inputControl = new sap.m.CheckBox({
								width: "100%",
								text: checkListItem[j].checkListItem,
								editable: false,
								selected: checkListItem[j].selected
							});
							newGridControl.addContent(inputControl);
						}
					}
					oTaskList.addItem(newControl);
				}
				this.createDynamicContentInv(dynamicContInvToBeCreated, dynamicContInvUserTypes); //AN: #pw
			}
		},
		onGetUserDataPWHopper: function () { //AN: #pw
			var sUserType = [];
			if (this.getModel("dashBoardModel").getProperty("/userData/isPOT")) {
				if (this.getModel("dashBoardModel").getProperty("/userData/hasCanadaRole")) { //SK: Canada POT should have access to all the Checklist
					sUserType = ["POT", "WW", "RE", "ALS"];
				} else {
					sUserType.push("POT");
				}
			}
			if (this.getModel("dashBoardModel").getProperty("/userData/isWW")) {
				sUserType.push("WW");
			}
			if (this.getModel("dashBoardModel").getProperty("/userData/isRE")) {
				sUserType.push("RE");
			}
			if (this.getModel("dashBoardModel").getProperty("/userData/isALS")) {
				sUserType.push("ALS");
			}
			return sUserType;
		},
		createDynamicContentInv: function (content, userType) {
			var oTaskList = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idUserRoleChecklistVBox");
			oTaskList.removeAllItems();
			var oModel = this.getModel("oTaskPanelDetailModel");
			var buttonStatusPWProactive = this.getModel("oTaskPanelDetailModel").getData().proactive;
			//SH: Modules readonly
			var currentSelectTab = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var modVis = this.validateModuleReadOnly(currentSelectTab);
			/*var isDOPReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isDOPReadOnly");
			var isPWHopperReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isPWHopperReadOnly");
			var isWebReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			var currentSelectTab = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var modVis = !isWebReadOnly ? !isDOPReadOnly && currentSelectTab === "DOP" ? true : !isPWHopperReadOnly && currentSelectTab ===
				"pwhopper" ? true : false : false;*/

			if (userType.indexOf("POT") >= 0) { //AN: #pw
				if (buttonStatusPWProactive === true) {
					oModel.setProperty("/PWData", {
						buttonEnable: false,
						buttonState: true
					});
				} else if (buttonStatusPWProactive === false) {
					oModel.setProperty("/PWData", {
						buttonEnable: true,
						buttonState: false
					});
				}
			}
			for (var cont = 0; cont < content.length; cont++) { //AN: #pw
				oTaskList.addItem(new sap.m.Label({
					text: content[cont].userType + " Check List",
					width: "13.35rem"
				}).addStyleClass("iopFontClass iopwbInvTitleClass sapUiTinyMarginTop sapUiSmallMarginBegin"));
				var newControl = new sap.ui.layout.Grid({
					defaultSpan: "L6 M6 S12",
					vSpacing: 0
				}).addStyleClass("sapUiTinyMarginTop sapUiSizeCompact pwGridHSpace1");
				var checkListItem = content[cont].checkList;
				for (var j = 0; j < checkListItem.length; j++) {
					if (checkListItem[j].dataType === "Input") {
						var inputControl = new sap.m.HBox({
							width: "100%",
							items: [new sap.m.Input({
									width: "100%",
									editable: checkListItem[j].editable && modVis,
									value: checkListItem[j].value
								}).addStyleClass("sapUiSizeCompact iopInputClass iopwbTaskBoderLightClass"),
								new sap.m.Label({
									text: checkListItem[j].checkListItem
								}).addStyleClass("iopFontClass iopwbTaskLabelClass sapUiTinyMargin sapUiSizeCompact iopwbLabelRequiredClass")
							]
						}).addStyleClass("sapUiSizeCompact pwInputGridHSpace1");
						newControl.addContent(inputControl);
					} else if (checkListItem[j].dataType === "CheckBox") {
						inputControl = new sap.m.CheckBox({
							width: "100%",
							text: checkListItem[j].checkListItem,
							editable: checkListItem[j].editable && modVis,
							selected: checkListItem[j].selected
						});
						newControl.addContent(inputControl);
					}
				}
				oTaskList.addItem(newControl);
			}
		},
		onSwitchPWHopperProactive: function (oEvent) {
			var newState = oEvent.getParameters().state,
				sMsg = "",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			sMsg = "Are you sure you want to make this proactive workover candidate?";
			var oDialog = new sap.m.Dialog({
				title: sTitle,
				type: 'Message',
				state: sState,
				content: new sap.m.Text({
					text: sMsg
				}),
				beginButton: new sap.m.Button({
					text: confirmYesBtn,
					visible: true,
					press: function () {
						this.onUpdateProactiveSwitch(newState);
						oDialog.close();
					}.bind(this)
				}),
				endButton: new sap.m.Button({
					text: confirmNoBtn,
					press: function () {
						this.getModel("oTaskPanelDetailModel").setProperty("/PWData/buttonState", false);
						oDialog.close();
					}.bind(this)
				}),
				afterClose: function () {
					oDialog.destroy();
					oDialog = undefined;
				}.bind(this)
			}).addStyleClass("sapUiSizeCompact");
			oDialog.open();
		},
		onUpdateProactiveSwitch: function (newState) {
			var oModel = this.getModel("oTaskPanelDetailModel");
			if (newState === true) {
				oModel.setProperty("/PWData", {
					buttonEnable: false,
					buttonState: true
				});
			}
			oModel.setProperty("/proactive", true);
		},
		onPressRemoveHopper: function (oEvent) {
			var sMsg = "",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			sMsg = "Are you sure you want to Remove PW Hopper?";
			this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
				var locationCode = this.getModel("oTaskPanelDetailModel").getData().taskEventDto.locationCode,
					sUrl = "/taskmanagementRest/pwhopper/removeProactive?locationCode=" + locationCode;
				this.doAjax(sUrl, "GET", null, function (oData) {
						if (oData.statusCode === "0") {
							var currentSelectKey = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey"); //AN: #pw	
							this.onSideNavigationItemSelect(currentSelectKey);
							this.onCreateInvestigationClose();
							this._showToastMessage(oData.message);
						} else {
							this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						}
					}.bind(this),
					function (oError) {
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			}.bind(this));
		},
		_checkNewUpdateCheckList: function () { //AN: #pw
			var oTaskList = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationPanel--idUserRoleChecklistVBox");
			if (!oTaskList) {
				return;
			}
			var items = oTaskList.getItems();
			var dynamicControlData = this.getModel("oTaskPanelDetailModel").getData()["checkList"];
			if (!dynamicControlData) {
				return;
			}
			var checkListToUpdateArray = [];
			var cIndex = 0;
			for (var it = items.length - 1; it >= 1; it -= 2) {
				var checkListToUpdateObj = {
					userType: "",
					content: items[it].getContent()
				};
				if (items[it - 1].getText().search("POT") >= 0) {
					checkListToUpdateObj.userType = "POT";
				} else if (items[it - 1].getText().search("RE") >= 0) {
					checkListToUpdateObj.userType = "RE";
				} else if (items[it - 1].getText().search("WW") >= 0) {
					checkListToUpdateObj.userType = "WW";
				} else if (items[it - 1].getText().search("ALS") >= 0) {
					checkListToUpdateObj.userType = "ALS";
				}
				checkListToUpdateArray.push(checkListToUpdateObj);
			}
			for (var u = 0; u < checkListToUpdateArray.length; u++) { //AN: #pw (Actual CheckList(s) to be updated)
				for (var dy = 0; dy < dynamicControlData.length; dy++) { //AN: #pw (Check for which checkList(s) to be modified)
					if (checkListToUpdateArray[u].userType === dynamicControlData[dy].userType) { //AN: #pw (Comparison for userType)
						cIndex = dy;
						for (var ch = 0; ch < checkListToUpdateArray[u].content.length; ch++) { //AN: #pw (loop to update the checkList)
							if (!(checkListToUpdateArray[u].content[ch].getAggregation("items"))) { //AN: #pw (Update checkboxes)
								this.getModel("oTaskPanelDetailModel").getData()["checkList"][cIndex].checkList[ch].selected = checkListToUpdateArray[
									u].content[
									ch].getSelected();
							} else { //AN: #pw (Update inputs)
								this.getModel("oTaskPanelDetailModel").getData()["checkList"][cIndex].checkList[ch].value = checkListToUpdateArray[u].content[
										ch].getItems()[0]
									.getValue();
							}
						}
						break;
					}
				}
			}
		},
		onPWHopperLoad: function () {
			var sUrl = "/taskmanagementRest/pwhopper/getpwHopperList";
			var selHier = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			if (selHier === "SEARCH") {
				selHier = "Well";
			}
			var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			if (selectedLoc.length === 0) {
				this.getModel("dashBoardModel").setProperty("/pwHopperData/tableData", []);
				this.getModel("dashBoardModel").setProperty("/busyIndicators/pwHopperTblBusy", false);
				return;
			}
			var locations = [];
			for (var i = 0; i < selectedLoc.length; i++) {
				locations.push(selectedLoc[i].location);
			}
			var oPayload = {
				"locationType": selHier,
				"locationCodeList": locations,
			};
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/pwHopperTblBusy", false);
					var oAckData = this.getView().getModel("dashBoardModel").getData().highlightPWHopper; //AN: #Notif
					try { //AN: #highlightShift
						if (oAckData) {
							var oScroll = sap.ui.core.Fragment.byId(this.createId("pwhopperFragment"), "idPwHopperScroll");
							var oTable = sap.ui.core.Fragment.byId(this.createId("pwhopperFragment"), "idPwHopperList");
							for (var k = 0; k < oAckData.length; k++) { //AN: #Notif
								for (var j = 0; j < oData.hopperDtoList.length; j++) {
									if (oAckData[k] == oData.hopperDtoList[j].muwi) {
										oData.hopperDtoList[j].pwHopperNotification = "notified";
										var sItem = oTable.getItems()[j];
										oScroll.scrollToElement(sItem);
										break;
									}
								}
							}
						}
					} catch (e) {

					}
					this.getModel("dashBoardModel").setProperty("/pwHopperData/date", oData.dataAsOffDisplay);
					this.getModel("dashBoardModel").setProperty("/pwHopperData/tableData", oData.hopperDtoList);
				} else {
					var sErrorMessage = oData.responseMessage.message;
					this.getModel("dashBoardModel").setProperty("/busyIndicators/pwHopperTblBusy", false);
					this._showToastMessage(sErrorMessage);
				}
			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/pwHopperTblBusy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onPwHopperTableSortClick: function () {
			var oFragmentId = "idPwHopperTableSortFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.PwHopperSortList";
			if (!this.PwHopperTableSort) {
				this.PwHopperTableSort = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.PwHopperTableSort);
			}
			this.PwHopperTableSort.open();
		},

		onPwHopperTableSort: function (oEvent) {
			var oTable = sap.ui.core.Fragment.byId(this.createId("pwhopperFragment"), "idPwHopperList");
			this.commonSort(oEvent, oTable);
		},
		onRightPressPwHopper: function (oEvent) { //AN: #pw
			var oContext = oEvent.getParameters();
			var oBindingContext = oContext.src.getBindingContext("dashBoardModel");
			if (oBindingContext && oBindingContext.getObject().locationType === "Well") {
				var dashBoardModel = this.getModel("dashBoardModel");
				var isPOT = dashBoardModel.getProperty("/userData/isPOT");
				if (isPOT && oBindingContext.getObject().hasInvestigation !== true) {
					dashBoardModel.setProperty("/potData/showInvestigate", true);
					this.onOpenPwHopperAction(oContext.src, oContext.target);
				} else {
					dashBoardModel.setProperty("/potData/showInvestigate", false);
				}
				dashBoardModel.setProperty("/currentPwHopperObject", oBindingContext.getObject());
				dashBoardModel.setProperty("/pwHopperObjForPlotly", oBindingContext.getObject());
			}
		},
		onOpenPwHopperAction: function (oContext, sTarget) { //AN: #pw
			var oFragmentId = "idPwHopperActionList",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.PwActionList";
			if (!this.pwActionList) {
				this.pwActionList = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.pwActionList);
			}
			this.pwActionList.setOffsetX(-30);
			this.pwActionList.openBy(sTarget);
		},
		onPWAction: function (oEvent) { //AN: #pw
			var pWAction = oEvent.getSource().getText();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeSeat
			oDashBoardModel.setProperty("/showProActiveSwitch", true); // #RV Proactive Well Candidate Fix for Visibility
			if (pWAction === "Investigate") {
				oDashBoardModel.setProperty("/dynamicContentType", { //AN: #pw
					userType: "POT",
					taskType: "Investigation",
					from: "PwHopper"
				});
				var oTaskTypeDetails = { //AN: #ChangeSeat
					oCurrSelectedLocation: oDashBoardModel.getProperty("/currentPwHopperObject"),
					sLocCode: oDashBoardModel.getProperty("/currentPwHopperObject/locationCode"),
					sLocType: oDashBoardModel.getProperty("/currentPwHopperObject/locationType"),
					sLocText: oDashBoardModel.getProperty("/currentPwHopperObject/location")
				};
				var aTaskTypeDetails = []; //AN: #ChangeSeat
				aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
				ChangeSeatHelper.checkAuthorization(this, oDashBoardModel, aTaskTypeDetails, "Module-Investigation", "POT", oEvent); //AN: #ChangeSeat
			}
		},
		// 18-10-2018 Rashmi function to get compressor downtime code
		_getCompressorDowntimeCode: function (oEvent) {
			var sUrl = "/taskmanagementRest/compressorDowntime/getDowntimeCodes";
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.message.statusCode === "0") {
					this.getModel("dashBoardModel").setProperty("/CompressorDowntimeCodes", oData.dtoList);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		// 23-10-2018 Rashmi function on change of compressor downtime duration- hour select
		onCompHourKeySelect: function (oEvent) {
			var key = oEvent.getSource().getSelectedKey();
			if (key === "24" || key === 24) {
				this.getModel("dashBoardModel").setProperty("/compDownTime/minuteKey", "0");
			}
		},
		// 22-10-2018 function on change of compressor downtime code 
		onCompDowntimeCodeSelect: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/compDownTime/downTimeCodeValue", oEvent.getSource().getSelectedItem().getText());
		},
		// 23-10-2018 function on change of downtime capture tab
		downtimeCaptureTabSelect: function (oEvent) {
			var oTabDownTimeSelKey = this.getView().getModel("dashBoardModel").getData().downtimeCaptureTabKey;
			switch (oTabDownTimeSelKey) {
			case 'Well':
				var currentTab = this.getModel("dashBoardModel").getProperty("/downtimeSubTabKey");
				this.getModel("dashBoardModel").setProperty("/downtimePanelExpanded", true);
				this.onDowntimeTableIconTabbarSelect(currentTab);
				break;
			case 'Compressor':
				var currentTab = this.getModel("dashBoardModel").getProperty("/compDowntimeSubTabKey");
				this.getModel("dashBoardModel").setProperty("/compressorDowntimePanelExpanded", true);
				this.onDowntimeTablesIconTabbarSelect(currentTab);
				break;
			case 'Flare':
				var currentTab = this.getModel("dashBoardModel").getProperty("/flareDowntimeSubTabKey");
				this.getModel("dashBoardModel").setProperty("/flareDowntimePanelExpanded", true);
				this.onDowntimeTablesIconTabbarSelect(currentTab);
				break;
			}
		},
		// 22-10-2018 function for clearing compressor downtime maunal data
		clearDownTimeData: function () {
			var sTabKey = this.getView().getModel("dashBoardModel").getData().downtimeCaptureTabKey;
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				// pattern: "YYYY-MM-dd"
				pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
			});
			var dateFormatted = dateFormat.format(new Date());
			if (sTabKey === "Compressor") {
				this.getModel("dashBoardModel").setProperty("/compDownTime", {
					dateUnFormatted: "",
					date: dateFormatted,
					downtimeCodeVisible: false,
					isDateValid: true,
					compName: this.getModel("dashBoardModel").getProperty("/compDownTime/compName"),
					compCode: this.getModel("dashBoardModel").getProperty("/compDownTime/compCode"),
					compType: this.getModel("dashBoardModel").getProperty("/compDownTime/compType"),
					locationText: this.getModel("dashBoardModel").getProperty("/compDownTime/locationText"),
					locationCode: this.getModel("dashBoardModel").getProperty("/compDownTime/locationCode"),
					locationType: this.getModel("dashBoardModel").getProperty("/compDownTime/locationType"),
					muwi: this.getModel("dashBoardModel").getProperty("/compDownTime/muwi"),
					id: this.getModel("dashBoardModel").getProperty("/compDownTime/id"),
					isUpdated: false,
					isReview: false
				});
				this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", false);
			}
			if (sTabKey === "Flare") {
				this.getModel("dashBoardModel").setProperty("/flareDownTime", {
					dateUnFormatted: "",
					date: dateFormatted,
					downtimeCodeVisible: false,
					isDateValid: true,
					meterText: this.getModel("dashBoardModel").getProperty("/flareDownTime/meterText"),
					meterCode: this.getModel("dashBoardModel").getProperty("/flareDownTime/meterCode"),
					meterType: this.getModel("dashBoardModel").getProperty("/flareDownTime/meterType"),
					muwi: this.getModel("dashBoardModel").getProperty("/flareDownTime/muwi"),
					id: this.getModel("dashBoardModel").getProperty("/flareDownTime/id"),
					isUpdated: false,
					isReview: false
				});
				this.getModel("dashBoardModel").setProperty("/isDesignateCreateTask", false);
			}
		},
		// 23-10-2018 function to get compressor downtime data from hana database
		onDowntimeTablesIconTabbarSelect: function (oEvent) {
			var currentSelection;
			if (typeof (oEvent) === "string") {
				currentSelection = oEvent;
			} else {
				currentSelection = oEvent.getParameter("key");
			}
			var sLocationType = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			var oSelectedDtos = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			var sTabKey = this.getModel("dashBoardModel").getProperty("/downtimeCaptureTabKey");
			if (sTabKey === "Compressor") {
				var sUrl = "/taskmanagementRest/compressorDowntime/getDowntime";
				var oCompDownTimeData = this.getModel("dashBoardModel").getData().compDownTime;
				if (oCompDownTimeData.locationType === "Facility") {
					for (var i = 0; i < oSelectedDtos.length; i++) {
						oSelectedDtos[i].facility = oCompDownTimeData.locationText;
					}
				}
				if (oCompDownTimeData.locationType === "Field") {
					for (i = 0; i < oSelectedDtos.length; i++) {
						oSelectedDtos[i].field = oCompDownTimeData.locationText;
					}
				}
				if (oCompDownTimeData.locationType === "WellPad") {
					for (i = 0; i < oSelectedDtos.length; i++) {
						oSelectedDtos[i].wellpad = oCompDownTimeData.locationText;
					}
				}
			}
			if (sTabKey === "Flare") {
				var sUrl = "/taskmanagementRest/flareDowntime/getDowntime";
			}
			if (oSelectedDtos.length > 0) {
				switch (currentSelection) {
				case "Submitted":
					this.setDataForDownTimeTables(sUrl, "SUBMITTED", oSelectedDtos, sLocationType);
					break;
				case "Review":
					this.setDataForDownTimeTables(sUrl, "REVIEW", oSelectedDtos, sLocationType);
					break;
				}
			} else {
				this.getModel("dashBoardModel").setProperty("/compDownTimeTable", []);
			}
		},
		setDataForDownTimeTables: function (url, sType, dto, sLocType) {
			var country = this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy"); //SH: Downtime Country Code
			var oPayload = {
				"countryCode": country,
				"statusType": sType,
				"locationType": sLocType,
				"locationHierarchy": dto
			};
			var sTabKey = this.getModel("dashBoardModel").getProperty("/downtimeCaptureTabKey");
			if (sTabKey === "Compressor") {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/compDowntimeTable", true);
				this.getModel("dashBoardModel").setProperty("/compDownTimeTable", []);
			}
			if (sTabKey === "Flare") {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/flareDowntimeTable", true);
				this.getModel("dashBoardModel").setProperty("/flareDownTimeTable", []);
			}
			this.doAjax(url, "POST", oPayload, function (oData) {
					if (sTabKey === "Compressor") {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/compDowntimeTable", false);

						if (oData.message && oData.message.statusCode === "0") {
							var tData = oData.dtoList;
							for (var i = 0; i < tData.length; i++) {
								tData[i].duration = "";
								tData[i].rcTime = "";
								if (tData[i].durationByRocHour) {
									tData[i].duration = tData[i].durationByRocHour + " hr ";
								}
								if (tData[i].durationByRocMinute) {
									tData[i].duration += tData[i].durationByRocMinute + " min";
								}
								if (tData[i].durationByCygnateHours) {
									tData[i].rcTime = tData[i].durationByCygnateHours + " hr ";
								}
								if (tData[i].durationByCygnateMinute) {
									tData[i].rcTime += tData[i].durationByCygnateMinute + " min";
								}
								tData[i].downtimeCode = tData[i].childCode.toString();
							}
							this.getModel("dashBoardModel").getData().compDownTimeTable = tData;
						} else {
							this.getModel("dashBoardModel").refresh(true);
						}
					}
					if (sTabKey === "Flare") {
						this.getModel("dashBoardModel").setProperty("/busyIndicators/flareDowntimeTable", false);
						if (oData.message && oData.message.statusCode === "0") {
							var tData = oData.dtoList;
							for (var i = 0; i < tData.length; i++) {
								if (tData[i].flareVolume) {
									tData[i].flareVolume = (tData[i].flareVolume).toString();
								}
							}
							this.getModel("dashBoardModel").getData().flareDownTimeTable = tData;
						} else {
							this.getModel("dashBoardModel").refresh(true);
						}
					}
					this.getModel("dashBoardModel").refresh(true);
				}.bind(this),
				function (oError) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/compDownTimeTable", false);
					this.getModel("dashBoardModel").refresh(true);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		// 24-10-2018 function on press of save compressor downtime
		onCreateCompressorDowntime: function () {
			var sUrl = "/taskmanagementRest/compressorDowntime/create";
			var oCompDownTimeData = this.getModel("dashBoardModel").getProperty("/compDownTime");
			var oData = this.getModel("dashBoardModel").getData();
			var isProcountUpdate = true;
			var hirarchyContext = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
			var locationTest = true;
			var mandatoryCheck = true;
			var isUpdate = oCompDownTimeData.isUpdated;
			if (oData.compDownTime.date === "" || oData.compDownTime.date === undefined || oData.compDownTime.hourkey === undefined ||
				oData.compDownTime
				.hourkey ===
				"" || oData.compDownTime.hourkey === null || oData.compDownTime.downtimeCode === undefined) {
				mandatoryCheck = false;
				if (oCompDownTimeData.minuteKey === undefined || oCompDownTimeData.minuteKey === "" || oCompDownTimeData.minuteKey ===
					null) {
					oCompDownTimeData.minuteKey = 0;
				}
				this._showToastMessage("Please fill all the required fields");
			} else {
				mandatoryCheck = true;
			}

			if (oData.hierarchyDetails.currentSelectedObject.length <= 1) {
				locationTest = true;
				if (!hirarchyContext.currentSelectedObject[0] || hirarchyContext.currentSelectedObject[0].location === undefined ||
					hirarchyContext.currentSelectedObject[0].location === null ||
					(hirarchyContext.currentLocationType !== "Compressor")) {

					if (this.getModel("dashBoardModel").getProperty("/compDownTime/compName") !== "" && this.getModel("dashBoardModel").getProperty(
							"/compDownTime/compName") !== null && this.getModel("dashBoardModel").getProperty("/compDownTime/compName") !==
						undefined) {
						locationTest = true;
					} else {
						locationTest = false;
						this._showToastMessage("Please Select a Compressor from Hierarchy Panel");
					}
				} else {
					locationTest = true;
				}
			} else {
				if (oCompDownTimeData.isReview) {
					locationTest = true;
				} else {
					locationTest = false;
					this._showToastMessage("Downtime Creation Possible on a Single Item");
				}
			}
			if (mandatoryCheck && locationTest) {
				//getting the systemDate for time		
				var oDate = new Date();
				var time = oDate.getHours() + ":" + oDate.getMinutes() + ":" + oDate.getSeconds();
				var oCreatedAtDate = new Date(oCompDownTimeData.date + " " + time);
				var sEpochCreatedAt = oCreatedAtDate.getTime();
				var oLoggedInUserId = this.getModel("dashBoardModel").getProperty("/userData/userId");
				if (isUpdate) {
					sUrl = "/taskmanagementRest/compressorDowntime/update";
					var oDate = oCompDownTimeData.date;
					/*var sTimeStap = parseInt(oCompDownTimeData.unFormattedDate);
					var oCreatedDate = new Date(sTimeStap);
					var sEpochCreatedAt = oCreatedDate.getTime();*/
					var oPayloadComp = {
						"dto": {
							"type": oCompDownTimeData.compType,
							"childCode": parseInt(oCompDownTimeData.downtimeCode, 10),
							"childText": oCompDownTimeData.downTimeCodeValue,
							"createdAt": oCompDownTimeData.unFormattedDate,
							//"createdAt": sEpochCreatedAt + "",
							"createdBy": oLoggedInUserId,
							"id": oCompDownTimeData.id,
							"durationByRocHour": parseInt(oCompDownTimeData.hourkey, 10),
							"durationByRocMinute": parseInt(oCompDownTimeData.minuteKey, 10),
							"muwi": oCompDownTimeData.muwi,
							"updatedBy": oLoggedInUserId
						},
						"isProCountUpdate": isProcountUpdate
					};
				} else {
					var oPayloadComp = {
						"dto": {
							"type": oCompDownTimeData.compType,
							"childCode": oCompDownTimeData.downtimeCode,
							"childText": oCompDownTimeData.downTimeCodeValue,
							//"createdAt": oCompDownTimeData.date + " " + time,
							"createdAt": sEpochCreatedAt + "",
							"createdBy": oLoggedInUserId,
							"durationByRocHour": oCompDownTimeData.hourkey,
							"durationByRocMinute": oCompDownTimeData.minuteKey,
							"muwi": oCompDownTimeData.muwi
						},
						"isProCountUpdate": isProcountUpdate
					};
					if (oData.locationType === "Facility") {
						oPayloadComp.facility = oCompDownTimeData.locationCode;
					}
					if (oData.locationType === "Field") {
						oPayloadComp.field = oCompDownTimeData.locationCode;
					}
					if (oData.locationType === "WellPad") {
						oPayloadComp.wellpad = oCompDownTimeData.locationCode;
					}
				}
				this.createDowntime(sUrl, oPayloadComp, true, false);
			}
		},
		// 23-10-2018 function to get compressor downtime data from hana database
		onCompDowntimeTableIconTabbarSelect: function (oEvent) {
			var currentSelection;
			if (typeof (oEvent) === "string") {
				currentSelection = oEvent;
			} else {
				currentSelection = oEvent.getParameter("key");
			}
			var sLocationType = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentLocationType");
			var oSelectedDtos = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject");
			var oCompDownTimeData = this.getModel("dashBoardModel").getData().compDownTime;
			if (oCompDownTimeData.locationType === "Facility") {
				for (var i = 0; i < oSelectedDtos.length; i++) {
					oSelectedDtos[i].facility = oCompDownTimeData.locationText;
				}
			}
			if (oCompDownTimeData.locationType === "Field") {
				for (i = 0; i < oSelectedDtos.length; i++) {
					oSelectedDtos[i].field = oCompDownTimeData.locationText;
				}
			}
			if (oCompDownTimeData.locationType === "WellPad") {
				for (i = 0; i < oSelectedDtos.length; i++) {
					oSelectedDtos[i].wellpad = oCompDownTimeData.locationText;
				}
			}
			if (oSelectedDtos.length > 0) {
				switch (currentSelection) {
				case "Submitted":
					this.setDataForCompDownTimeTable("/taskmanagementRest/compressorDowntime/getDowntime", "SUBMITTED", oSelectedDtos,
						sLocationType);
					break;
				case "Review":
					this.setDataForCompDownTimeTable("/taskmanagementRest/compressorDowntime/getDowntime", "REVIEW", oSelectedDtos,
						sLocationType);
					break;
				}
			} else {
				this.getModel("dashBoardModel").setProperty("/compDownTimeTable", []);
			}
		},
		// 23-10-2018 function to create compressor downtime
		createDowntime: function (sUrl, oPayload, isDesignateCreate, isUpdate) {
			var sTabKey = this.getModel("dashBoardModel").getProperty("/downtimeCaptureTabKey");
			this.getModel("dashBoardModel").setProperty("/busyIndicators/flareDownTime", true);
			this.getModel("dashBoardModel").setProperty("/busyIndicators/compDownTime", true);
			this.doAjax(sUrl, "POST", oPayload, function (oDataS) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/compDownTime", false);
					var isUpdateComp = this.getView().getModel("dashBoardModel").getData().compDownTime.isUpdated;
					var isUpdateflare = this.getView().getModel("dashBoardModel").getData().flareDownTime.isUpdated;
					var sSubTabSelect = this.getView().getModel("dashBoardModel").getData().compDowntimeSubTabKey;
					if (isUpdateComp && this.oUpdateDowntimeFrag) {
						this.oUpdateDowntimeFrag.close();
						this.onDowntimeTableIconTabbarSelect(sSubTabSelect);
					}
					if (isUpdateflare && this.oUpdateDowntimeFrag) {
						this.oUpdateDowntimeFrag.close();
					}
					if (oDataS.status === "SUCCESS") {
						this._showToastMessage(oDataS.message);
						this.clearDownTimeData();
						this.onSideNavigationItemSelect("external");
					} else {
						this._createConfirmationMessage("Error", oDataS.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					var isUpdateComp = this.getView().getModel("dashBoardModel").getData().compDownTime.isUpdated;
					var isUpdateflare = this.getView().getModel("dashBoardModel").getData().flareDownTime.isUpdated;
					if (isUpdateComp && this.oUpdateDowntimeFrag) {
						this.oUpdateDowntimeFrag.close();
					}
					if (isUpdateflare && this.oUpdateDowntimeFrag) {
						this.oUpdateDowntimeFrag.close();
					}
					this.clearDownTimeData();
					this.getModel("dashBoardModel").setProperty("/busyIndicators/compDownTime", false);
				}.bind(this));
		},
		//Rashmi 24-10-2018 function to update compressor downtime
		onCompressorUpdate: function () {
			var sTabKey = this.getModel("dashBoardModel").getProperty("/downtimeCaptureTabKey");
			if (sTabKey === "Compressor") {
				this.getView().getModel("dashBoardModel").getData().compDownTime.isUpdated = true;
				this.onCreateCompressorDowntime();
			}
			if (sTabKey === "Flare") {
				this.getView().getModel("dashBoardModel").getData().flareDownTime.isUpdated = true;
				this.onFlareDownTimeCreate();
			}
		},
		onCompUpdateClose: function () {
			this.oUpdateDowntimeFrag.close();
			this.clearDownTimeData();
		},
		// 24-10-2018 Rashmi function for compressor carry forward
		onCompReviewFwdPress: function (oEvent) {
			var expanded;
			var currentObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oGenModel = this.getModel("dashBoardModel");
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
				// pattern: "YYYY-MM-dd"
				pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
			});
			var dateFormatted;
			var systemTime = new Date();
			dateFormatted = dateFormat.format(systemTime);
			var sTabKey = this.getModel("dashBoardModel").getProperty("/downtimeCaptureTabKey");
			if (sTabKey === "Compressor") {
				expanded = this.getModel("dashBoardModel").getProperty("/compressorDowntimePanelExpanded");
				oGenModel.setProperty("/compDownTime/locationText", currentObj.facility);
				oGenModel.setProperty("/compDownTime/hourkey", "24");
				oGenModel.setProperty("/compDownTime/minuteKey", "0");
				oGenModel.setProperty("/compDownTime/compName", currentObj.well);
				oGenModel.setProperty("/compDownTime/compType", currentObj.type);
				oGenModel.setProperty("/compDownTime/downtimeCode", currentObj.downtimeCode);
				oGenModel.setProperty("/compDownTime/date", dateFormatted);
				oGenModel.setProperty("/compDownTime/unFormattedDate", currentObj.createdAt);
				oGenModel.setProperty("/compDownTime/isReview", true);
				oGenModel.setProperty("/compDownTime/muwi", currentObj.muwi);
				oGenModel.setProperty("/compDownTime/downTimeCodeValue", currentObj.childText);
				if (!expanded) {
					this.getModel("dashBoardModel").setProperty("/compressorDowntimePanelExpanded", true);
				}
			}
			if (sTabKey === "Flare") {
				oGenModel.setProperty("/flareDownTime/meterText", currentObj.meter);
				oGenModel.setProperty("/flareDownTime/flareVolume", currentObj.flareVolume);
				oGenModel.setProperty("/flareDownTime/id", currentObj.id);
				oGenModel.setProperty("/flareDownTime/date", dateFormatted);
				oGenModel.setProperty("/flareDownTime/unFormattedDate", currentObj.createdAt);
				oGenModel.setProperty("/flareDownTime/isReview", true);
				oGenModel.setProperty("/flareDownTime/muwi", currentObj.merrickId);
				oGenModel.setProperty("/flareDownTime/merrickId", currentObj.merrickId);
				oGenModel.setProperty("/flareDownTime/childText", currentObj.childText);
				oGenModel.setProperty("/flareDownTime/flareCode", currentObj.childCode);
				oGenModel.setProperty("/flareDownTime/createdBy", currentObj.createdBy);
				oGenModel.setProperty("/flareDownTime/createdAt", currentObj.createdAt);
				if (!expanded) {
					this.getModel("dashBoardModel").setProperty("/flareDowntimePanelExpanded", true);
				}
			}
		},
		//Rashmi 24-10-2018 function to open compressor update fragment
		onUpdatePress: function (oEvent) {
			if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "downtime") {
				var currentObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
				var oGenModel = this.getModel("dashBoardModel");
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					// pattern: "YYYY-MM-dd"
					pattern: "yyyy-MM-dd" //George - 30/12/2020 - INC0105444
				});
				var epochString = currentObj.createdAt;
				if (typeof epochString === "string") {
					var epochVal = parseInt(epochString);
				} else {
					epochVal = epochString;
				}
				var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
					pattern: "dd-MMM-yy hh:mm:ss aaa"
				});
				var convDate = new Date(epochVal);
				var epochToDate = oDateFormat.format(convDate);
				var aDate = epochToDate.split(" ");
				var aDateArray = aDate[0].split("-");
				var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
				sDate = sDate + " " + aDate[1] + " " + aDate[2];
				var dateFormatted = dateFormat.format(new Date(sDate));
				var sTabKey = this.getModel("dashBoardModel").getProperty("/downtimeCaptureTabKey");
				var dateFormatted;
				var systemTime = new Date();
				dateFormatted = dateFormat.format(systemTime);
				if (sTabKey === "Compressor") {
					oGenModel.setProperty("/compDownTime/locationText", currentObj.facility);
					oGenModel.setProperty("/compDownTime/hourkey", currentObj.durationByRocHour);
					oGenModel.setProperty("/compDownTime/minuteKey", currentObj.durationByRocMinute);
					oGenModel.setProperty("/compDownTime/compName", currentObj.well);
					oGenModel.setProperty("/compDownTime/downtimeCode", currentObj.downtimeCode);
					oGenModel.setProperty("/compDownTime/date", dateFormatted);
					oGenModel.setProperty("/compDownTime/unFormattedDate", epochToDate);
					oGenModel.setProperty("/compDownTime/isReview", true);
					oGenModel.setProperty("/compDownTime/muwi", currentObj.muwi);
					oGenModel.setProperty("/compDownTime/id", currentObj.id);
					oGenModel.setProperty("/compDownTime/downTimeCodeValue", currentObj.childText);
				}
				if (sTabKey === "Flare") {
					oGenModel.setProperty("/flareDownTime/meterText", currentObj.meter);
					oGenModel.setProperty("/flareDownTime/flareVolume", currentObj.flareVolume);
					oGenModel.setProperty("/flareDownTime/id", currentObj.id);
					oGenModel.setProperty("/flareDownTime/date", dateFormatted);
					oGenModel.setProperty("/flareDownTime/unFormattedDate", epochToDate);
					oGenModel.setProperty("/flareDownTime/isReview", true);
					oGenModel.setProperty("/flareDownTime/muwi", currentObj.muwi);
					oGenModel.setProperty("/flareDownTime/merrickId", currentObj.merrickId);
					oGenModel.setProperty("/flareDownTime/flareText", currentObj.childText);
					oGenModel.setProperty("/flareDownTime/childText", currentObj.childText);
					oGenModel.setProperty("/flareDownTime/flareCode", currentObj.childCode);
					oGenModel.setProperty("/flareDownTime/createdBy", currentObj.createdBy);
					oGenModel.setProperty("/flareDownTime/createdAt", epochToDate);
				}
			}
			var oFragmentId = "idCompUpdateFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.updateDowntime";
			if (!this.oUpdateDowntimeFrag) {
				this.oUpdateDowntimeFrag = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.oUpdateDowntimeFrag);
			}
			this.oUpdateDowntimeFrag.open();
		},
		onRollUpChangeDOP: function (oEvent) {
			var sKey = oEvent.getSource().getSelectedKey();
			if (sKey === "NRUP") {
				this.getModel("dashBoardModel").setProperty("/dopData/rolledUp", false);
			} else if (sKey === "RUP") {
				this.getModel("dashBoardModel").setProperty("/dopData/rolledUp", true);
			}
			this.onDopLoad();
		},

		/* Rashmi Vatsyayana 29-10-2018 Flare Downtime Capture Start*/
		_getFlareCode: function (oEvent) {
			var sUrl = "/taskmanagementRest/flareDowntime/getDowntimeCodes";
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.message.statusCode === "0") {
					this.getModel("dashBoardModel").setProperty("/FlareDowntimeCodes", oData.dtoList);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onflareDowntimeCodeSelect: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/flareDownTime/flareText", oEvent.getSource().getSelectedItem().getText());
		},
		onFlareDownTimeCreate: function () {
			var sUrl = "/taskmanagementRest/flareDowntime/create";
			var oData = this.getModel("dashBoardModel").getData();
			var hirarchyContext = this.getModel("dashBoardModel").getProperty("/hierarchyDetails");
			var locationTest = true;
			var mandatoryCheck = true;
			var isUpdate = oData.flareDownTime.isUpdated;
			if (isUpdate) {
				sUrl = "/taskmanagementRest/flareDowntime/update";
			}
			if (oData.flareDownTime.date === "" || oData.flareDownTime.date === undefined || oData.flareDownTime.flareCode === undefined ||
				oData.flareDownTime.flareCode === "" || oData.flareDownTime.flareVolume === undefined ||
				oData.flareDownTime.flareVolume === "") {
				mandatoryCheck = false;
				this._showToastMessage("Please fill all the required fields");
			} else {
				var flareVolume = oData.flareDownTime.flareVolume;
				var floatNum = /^\d+(?:\.\d+)?$/;
				if (floatNum.test(flareVolume)) {
					if (flareVolume > parseFloat(99999.99)) {
						this._showToastMessage("Flare Volume can not exceed \'99999.99\'!");
						mandatoryCheck = false;
					} else {
						mandatoryCheck = true;
					}
				} else {
					this._showToastMessage("Only interger and float values are allowed for Flare Volume");
					mandatoryCheck = false;
				}
			}
			if (oData.hierarchyDetails.currentSelectedObject.length <= 1) {
				locationTest = true;
				if (!hirarchyContext.currentSelectedObject[0] || hirarchyContext.currentSelectedObject[0].location === undefined ||
					hirarchyContext.currentSelectedObject[0].location === null ||
					(hirarchyContext.currentLocationType !== "Meter")) {

					if (this.getModel("dashBoardModel").getProperty("/flareDownTime/meterText") !== "" && this.getModel(
							"dashBoardModel").getProperty(
							"/flareDownTime/meterText") !== null && this.getModel("dashBoardModel").getProperty("/flareDownTime/meterText") !==
						undefined) {
						locationTest = true;
					} else {
						locationTest = false;
						this._showToastMessage("Please Select a Meter from Hierarchy Panel");
					}
				} else {
					locationTest = true;
				}
			} else {
				if (oData.flareDownTime.isReview) {
					locationTest = true;
				} else {
					locationTest = false;
					this._showToastMessage("Downtime Creation Possible on a Single Item");
				}
			}
			if (mandatoryCheck && locationTest) {
				var oDate = new Date();
				var oPayloadFlare;
				var time = oDate.getHours() + ":" + oDate.getMinutes() + ":" + oDate.getSeconds();
				var oUpdateDate = new Date(oData.flareDownTime.date + " " + time);
				var sUpdateEpoch = oUpdateDate.getTime();
				var oLoggedInUserId = this.getModel("dashBoardModel").getProperty("/userData/userId");
				if (isUpdate) {
					oDate = oData.flareDownTime.date;
					oPayloadFlare = {
						"dto": {
							"childCode": oData.flareDownTime.flareCode,
							"childText": oData.flareDownTime.flareText,
							//"updatedAt": oDate + " " + time,
							"updatedAt": sUpdateEpoch + "",
							"updatedBy": oLoggedInUserId,
							"flareVolume": parseFloat(oData.flareDownTime.flareVolume),
							"id": oData.flareDownTime.id
						},
						"isProCountUpdate": true
					};
				} else {
					var oCreatedAtDate = new Date(oData.flareDownTime.date + " " + time);
					var sEpochCreatedAt = oCreatedAtDate.getTime();
					oPayloadFlare = {
						"dto": {
							"type": "Flare",
							"childCode": oData.flareDownTime.flareCode,
							"childText": oData.flareDownTime.flareText,
							//"createdAt": oData.flareDownTime.date + " " + time,
							"createdAt": sEpochCreatedAt + "",
							"createdBy": oLoggedInUserId,
							"flareVolume": parseFloat(oData.flareDownTime.flareVolume),
							"merrickId": oData.flareDownTime.muwi
						},
						"isProCountUpdate": true
					};
				}
				this.createDowntime(sUrl, oPayloadFlare, true, false);
			}
		},
		onTaskPanelBeforeOpen: function (oEvent) {
			var collabTaskType = oEvent.getSource().data("collabTaskType");
			var collabTaskTypeProp = "Task Collaboration";
			if (collabTaskType) {
				collabTaskTypeProp = collabTaskType + " Collaboration";
			}
			this.getModel("dashBoardModel").setProperty("/collabTaskType", collabTaskTypeProp);
		},
		onPwHopperRoleSortNew: function (oEvent) {
			var oFragmentId = "idPwHopperRoleSortNew",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.PwHopperRoleLevelSortNew";
			if (!this.PwHRoleSortNew) {
				this.PwHRoleSortNew = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.PwHRoleSortNew);
			}
			this.PwHRoleSortNew.openBy(oEvent.getSource());
		},
		setValueForStatus: function (value, filterSettings) {
			switch (value) {
			case "Active":
				return "#fd5959"; //red
				break;
			case "Inprogress":
				return "#ffa500"; //orange
				break;
			case "Completed":
				return "#00b500"; //green
				break;
			case "Inactive":
				return "#808080"; //grey
				break;
			}
		},
		selectionChangePwHopperFilter: function (oEvent) {
			var filterSettings = this.getModel("dashBoardModel").getProperty("/pwHopperSortData");
			var sourcePoint = oEvent.getSource().getParent().getLabel().getText(),
				selectedValue = oEvent.getSource().getValue(),
				convertedValue = this.setValueForStatus(selectedValue, filterSettings);
			switch (sourcePoint) {
			case "POT":
				filterSettings.selectedPotStatus = selectedValue;
				filterSettings.selectedPotStatusValue = convertedValue;
				break;

			case "RE":
				filterSettings.selectedReStatus = selectedValue;
				filterSettings.selectedReStatusValue = convertedValue;
				break;

			case "WW":
				filterSettings.selectedWwStatus = selectedValue;
				filterSettings.selectedWwStatusValue = convertedValue;
				break;

			case "ALS":
				filterSettings.selectedAlsStatus = selectedValue;
				filterSettings.selectedAlsStatusValue = convertedValue;
				break;
			}
			this.configureCustomSetting(filterSettings);

		},
		configureCustomSetting: function (filterSettings) {
			var aFilters = [];
			aFilters = this.createOverAllFilters(filterSettings);
			this.getModel("dashBoardModel").setProperty("/pwHopperSortData/filters", aFilters);
			this.applyFilter(aFilters);
		},
		createOverAllFilters: function (filterSettings) {
			var aFilters = [],
				potStatusFilter = [],
				wwStatusFilters = [],
				reStatusFilters = [],
				alsStatusFilters = [];

			if (filterSettings.selectedPotStatus) {
				potStatusFilter.push(new sap.ui.model.Filter("potStatus", "EQ", filterSettings.selectedPotStatusValue));
				aFilters = aFilters.concat(potStatusFilter);
			}
			if (filterSettings.selectedReStatus) {
				reStatusFilters.push(new sap.ui.model.Filter("reStatus", "EQ", filterSettings.selectedReStatusValue));
				aFilters = aFilters.concat(reStatusFilters);
			}
			if (filterSettings.selectedWwStatus) {
				wwStatusFilters.push(new sap.ui.model.Filter("wwStatus", "EQ", filterSettings.selectedWwStatusValue));
				aFilters = aFilters.concat(wwStatusFilters);
			}
			if (filterSettings.selectedAlsStatus) {
				alsStatusFilters.push(new sap.ui.model.Filter("alsStatus", "EQ", filterSettings.selectedAlsStatusValue));
				aFilters = aFilters.concat(alsStatusFilters);
			}
			return aFilters;
		},
		applyFilter: function (systemFilters) {
			var oTable = sap.ui.core.Fragment.byId(this.createId("pwhopperFragment"), "idPwHopperList"),
				oBinding = oTable.getBinding("items");
			if (oBinding) {
				oBinding.filter(systemFilters);
			}
		},
		/* Rashmi 21-11-2018 Create Task Enhancement*/
		getTaskHistory: function (location) {
			var sUrl = "/taskmanagementRest/tasks/getTaskHistory?locationCode=" + location;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.status === "SUCCESS") {
					this.getModel("dashBoardModel").setProperty("/taskHistory", oData.taskList);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		getTaskStartDate: function (oUser, location, subClassification) {
			var sUrl = "/taskmanagementRest/scheduling/getStartTimeForUser?userId=" + oUser + "&locationCode=" + location +
				"&subClassification=" + subClassification;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.status === "SUCCESS") {
					this.getModel("dashBoardModel").setProperty("/startDateForUser", oData);
					this.getModel("dashBoardModel").refresh(true);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		getTaskFLSOP: function (sClassificationKey, sSubClassificationKey) {
			var sUrl = "/taskmanagementRest/tasks/getFLSOP?classification=" + sClassificationKey + "&subClassification=" +
				sSubClassificationKey;
			var objArray = {};
			objArray.userDisplayName = "SYSTEM USER";
			objArray.userId = "SYSTEM";
			objArray.createdAtDisplay = this.getCurrDate();
			var oTaskPanelDetailModel = this.getView().getModel("oTaskPanelDetailModel");
			if (oTaskPanelDetailModel.getData().collabrationDtos && oTaskPanelDetailModel.getData().collabrationDtos.length > 0) {
				if (!(oTaskPanelDetailModel.getData().collabrationDtos instanceof Array)) {
					oTaskPanelDetailModel.getData().collabrationDtos = [oTaskPanelDetailModel.getData().collabrationDtos];
				}
			} else {
				oTaskPanelDetailModel.getData().collabrationDtos = [];
			}
			this.getModel("dashBoardModel").setProperty("/busyIndicators/collabPanelBusy", false);
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.status === "SUCCESS") {
					if ((this.getModel("dashBoardModel").getData().isFLSOP)) {
						oTaskPanelDetailModel.getData().collabrationDtos.splice(0, 1);
					}
					if (oData.flsop) {
						objArray.message = oData.flsop.split("\\n").join("\n");
						oTaskPanelDetailModel.getData().collabrationDtos.unshift(objArray);
						oTaskPanelDetailModel.refresh();
						this.getModel("dashBoardModel").getData().isFLSOP = true;
						this.getModel("dashBoardModel").setProperty("/taskFLSOP", objArray.message);
					} else {
						this.getModel("dashBoardModel").getData().isFLSOP = false;
						oTaskPanelDetailModel.refresh();
					}
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("message");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		getTaskSchedulingDataForUser: function (sUsers) {
			var oRoleData;
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "US") {
					oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				} else {
					oRoleData = "IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
				}
			} else {
				oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			}
			this.getModel("dashBoardModel").setProperty("/SchedulingData/scheduleData", []);
			var timeOffset = new Date().getTimezoneOffset();
			var oUser = "";
			if (sUsers && sUsers.length > 0) {
				oUser = sUsers[0].userId;
				var sUrl;
				var oDate = this.getModel("dashBoardModel").getData().SchedulingData.startDate;
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					// pattern: "YYYY/MM/dd"
					pattern: "yyyy/MM/dd" //George - 30/12/2020 - INC0105444
				});
				var dateFormatted = dateFormat.format(oDate);
				var aStartDate = oDate.toString().split(" ");
				var aStartTime = aStartDate[4].split(":");
				var sStarTime = aStartTime[0];
				var sEndDate;
				if (sStarTime >= 12) {
					var oEndDate = new Date(oDate);
					oEndDate = oEndDate.setDate(oEndDate.getDate() + 1);
					oEndDate = new Date(oEndDate);
					sEndDate = dateFormat.format(oEndDate);
					//	dateFormatted = dateFormatted + "," + sEndDate;
				} else {
					sEndDate = dateFormatted;
				}
				if (oUser !== "") {
					sUrl = "/taskmanagementRest/scheduling/getTasks?userId=" + oUser + "&role=" + oRoleData + "&timeZoneOffSet=" + timeOffset +
						"&fromDate=" + dateFormatted + "&toDate=" + sEndDate;
				} else {
					sUrl = "/taskmanagementRest/scheduling/getTasks?role=" + oRoleData + "&timeZoneOffSet=" + timeOffset + "&fromDate=" +
						dateFormatted + "&toDate=" + sEndDate;
				}
				this.getModel("dashBoardModel").setProperty("/SchedulingData/busyIndicator", true);
				this.doAjax(sUrl, "GET", null, function (oData) {
					jQuery.sap.delayedCall(1000, this, function () {
						this.getModel("dashBoardModel").setProperty("/SchedulingData/busyIndicator", false);
					}.bind(this));
					if (oData.responseMessage.status === "SUCCESS") {
						this.getModel("dashBoardModel").setProperty("/createTaskScheduling", true);
						this.getModel("dashBoardModel").setProperty("/SchedulingData/scheduleData", oData.taskList);
						this.getModel("dashBoardModel").setProperty("/SchedulingData/taskV2templateAvaliable", true);
					}
				}.bind(this), function (oError) {
					this.getModel("dashBoardModel").setProperty("/SchedulingData/busyIndicator", false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(this));
			}
		},
		onTaskPanelAfterOpen: function (oEvent) { //AN: #scrollHeight
			this.setTaskPanelSizes(oEvent.getSource().data("collabTaskType"));
		},
		onPressIssuePigLaunch: function () {
			this.onPressUpdateTask();
		},
		onDateTimeSelectPig: function (oEvent) { //AN: #piggingChange
			var currentDate = new Date().getTime();
			// create Date object for current location
			var d1 = new Date();
			// convert to msec
			// add local time zone offset 
			// get UTC time in msec
			var utc = d1.getTime() + (d1.getTimezoneOffset() * 60000);
			// create new Date object for different city
			// using supplied offset
			var offset = "-6.0";
			var nd = new Date(utc + (3600000 * offset));
			currentDate = nd.getTime();

			// newPigTime = new Date(oEvent.getSource().getValue()).getTime();
			var h = oEvent.getSource().getValue().split(" ")[0].split(":")[0];
			var m = oEvent.getSource().getValue().split(" ")[0].split(":")[1];
			var s = oEvent.getSource().getValue().split(" ")[0].split(":")[2];
			var d = new Date();
			var isAmPm = oEvent.getSource().getValue().split(" ")[1];
			if (h === "12") {
				h = "00";
			}
			if (isAmPm.toLowerCase() === "pm") {
				h = +h + 12;
			}
			d.setHours(h);
			d.setMinutes(m);
			d.setSeconds(s);
			var newPigTime = d.getTime();
			if (newPigTime !== null) {
				if (currentDate > newPigTime) {
					oEvent.getSource().setValue();
					oEvent.getSource().setValueState("Error");
					oEvent.getSource().setValueStateText("Pigging Time should be future time");
				} else {
					oEvent.getSource().setValueState("None");
					var oData = this.getModel("oTaskPanelDetailModel").getData();
					if (oData.customAttr) {
						for (var c = 0; c < oData.customAttr.length; c++) {
							if (oData.customAttr[c].label === "Estimated pig retrieval time") {
								oData.customAttr[c].labelValue = oEvent.getSource().getValue();
								oData.customAttr[c].valueDtos[0].value = oEvent.getSource().getValue().split(" ")[1].toUpperCase();
							}
						}
					}
				}
			}
		},
		onPressIssuePigRetrievalAuthorizationCheck: function (oEvent) {
			var dashBoardModel = this.getModel("dashBoardModel"),
				oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true);
			var oCurrentObject = dashBoardModel.getData().taskCardData;
			var sLoocationType = oTaskPanelDetailModel.getData().taskEventDto.locationType;
			var oTaskTypeDetails = {
				oCurrSelectedLocation: [oCurrentObject],
				sLocCode: oCurrentObject.locationCode,
				sLocType: sLoocationType,
				sLocText: oCurrentObject.location
			};
			var aTaskTypeDetails = [];
			aTaskTypeDetails.push(oTaskTypeDetails);
			ChangeSeatHelper.checkAuthorization(this, dashBoardModel, aTaskTypeDetails, "Issue-PiggRetrival", "ROC", oEvent);
		},
		onPressIssuePigRetrieval: function (oEvent) {
			var dashBoardModel = this.getModel("dashBoardModel"),
				oTaskPanelDetailModel = this.getModel("oTaskPanelDetailModel");
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true);
			/*	var oCurrentObject = dashBoardModel.getData().taskCardData;
				var sLoocationType = oTaskPanelDetailModel.getData().taskEventDto.locationType;
				var oTaskTypeDetails = {
					oCurrSelectedLocation: [oCurrentObject],
					sLocCode: oCurrentObject.locationCode,
					sLocType: sLoocationType,
					sLocText: oCurrentObject.location
				};
				var aTaskTypeDetails = [];
				aTaskTypeDetails.push(oTaskTypeDetails);
				ChangeSeatHelper.checkAuthorization(this, dashBoardModel, aTaskTypeDetails, "Module-Dispatch", "ROC");*/
			this.setTaskPanelButtonsEnablement(false); //AN: #obxUAT
			var sUrl = "/taskmanagementRest/pigging/createPayload",
				payload = this.getModel("oTaskPanelDetailModel").getData();
			payload.taskEventDto.createdBy = this._getCurrentLoggedInUser();
			payload.taskEventDto.createdByDisplay = dashBoardModel.getProperty("/userData/displayName");
			dashBoardModel.setProperty("/piggingTask", {
				piggingTaskId: payload.taskEventDto.taskId
			});
			dashBoardModel.setProperty("/isCreateTask", true);
			this.doAjax(sUrl, "POST", payload, function (oData) {
					if (oData.responseMessage.statusCode === "0") {
						this.getModel("dashBoardModel").getData().createTaskPanelVisible = true;
						dashBoardModel.setProperty("/showCreatePRTask", true);
						// var currLoc = oData.locHierarchy.pop();
						// this.addAdditionalTasks(currLoc.loc);
						// dashBoardModel.setProperty("/selectedLastLocation", currLoc.loc);
						// dashBoardModel.setProperty("/selectedLocation", currLoc.loc);
						oData.collabrationDtos = null;
						dashBoardModel.setProperty("/taskId", null);
						dashBoardModel.setProperty("/processId", null);
						oTaskPanelDetailModel.setData(oData);
						var oTaskList = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idDynGrid");
						if (oData.customAttr) {
							oTaskPanelDetailModel.getData().taskEventDto.status = "NEW";
							for (var i = 0; i < oData.customAttr.length; i++) {
								if (oData.customAttr[i].label === "Status") {
									oTaskPanelDetailModel.getData().customAttr[i].labelValue = null;
									oTaskPanelDetailModel.refresh(true);
									break;
								}
							}
							this.createDynamicCont(oTaskList, "customAttr");
						}
						dashBoardModel.setProperty("/sLocCodeToGetCurrentNearByUser", oData.taskEventDto.locationCode); //AN: #operatorsAvailable
						this.onTaskClassificationChange();
						var sKey = oTaskPanelDetailModel.getData().taskEventDto.locationCode;
						this.getTaskHistory(sKey);
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
						this.setTaskPanelButtonsEnablement(true); //AN: #obxUAT
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
						this.setTaskPanelButtonsEnablement(true); //AN: #obxUAT
					}
				}.bind(this),
				function (oError) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false);
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this.setTaskPanelButtonsEnablement(true); //AN: #obxUAT
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		onCreatingTaskPR: function (oEvent) {
			var dashBoardModel = this.getModel("dashBoardModel");
			dashBoardModel.setProperty("/piggingTask/isPiggingTask", true);
			this.onCreatingTask();
		},
		onPressClosePigTask: function () {
			var sMsg = "Are you sure you want to complete this task?",
				sTitle = "Confirm",
				sState = "None",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
				this.onRemoveTask(this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskId"));
			});
		},
		/* OBX Changes By Rashmi */
		getOBXUserListData: function () {
			var oRoleData;
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "US") {
					oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				} else {
					oRoleData = "IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
				}
			} else {
				oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			}
			var sUrl = "/taskmanagementRest/OBXScheduler/getOBXUsers?roles=" + oRoleData;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.status === "SUCCESS") {
					this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/OBXUserList", oData.ownerList);
					this.getModel("dashBoardModel").refresh(true);
					//this.getModel("dashBoardModel").setProperty("/busyIndicators/OBXUserListData", false);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				//this.getModel("dashBoardModel").setProperty("/busyIndicators/OBXUserListData", false);
				if (oError.getId() === "requestFailed" && oError.getParameter("message") === "parsererror" &&
					oError.getParameter("statusText") === "parsererror" && oError.getParameter("statusCode") === 200) { //AN: #parseError
					this._createConfirmationMessage("Error", "The user session has timed out. Please refresh the page", "Error", "", "Close",
						false,
						null);
					//html error due to session timeout - msg display
				} else {
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}
			}.bind(this));
		},
		onOBXUserListSelection: function (oEvent) {
			var oCurrentContext = oEvent.getParameter("listItem").getBindingContext("dashBoardModel").getObject();

			this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/currentObxObj", oCurrentContext);

			var sUser = oCurrentContext.ownerEmail;
			this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/gainTime", oCurrentContext.gainTime);
			this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/currentObxUserName", oCurrentContext.taskOwnerDisplayName);
			this.getOBXTaskSchedulingDataForUser(sUser);
			this.getOBXLatandLngData(sUser);
		},
		getOBXTaskSchedulingDataForUser: function (oUser) {
			var oRoleData;
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/moduleReadOnly/isTaskSchedulerReadOnly")) {
				if (this.getModel("dashBoardModel").getProperty("/oUserDetail").addresses[0].country === "US") {
					oRoleData = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes";
				} else {
					oRoleData = "IOP_TM_ROC_Kaybob,IOP_TM_ROC_Montney";
				}
			} else {
				oRoleData = this.getModel("dashBoardModel").getProperty("/userData/resGroupRead");
			}
			this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/scheduleData", []);
			var timeOffset = new Date().getTimezoneOffset();
			var sUrl;
			if (oUser) {
				var sStartDate;
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					// pattern: "YYYY/MM/dd"
					pattern: "yyyy/MM/dd" //George - 30/12/2020 - INC0105444
				});
				sStartDate = dateFormat.format(new Date());
				sUrl = "/taskmanagementRest/scheduling/getTasks?userId=" + oUser + "&role=" + oRoleData + "&timeZoneOffSet=" + timeOffset +
					"&fromDate=" + sStartDate + "&toDate=" + sStartDate;
				// sUrl = "/taskmanagementRest/scheduling/getTasks?userId=" + oUser + "&role=" + oRoleData + "&timeZoneOffSet=" + timeOffset;
				this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/busyIndicator", true);
				this.doAjax(sUrl, "GET", null, function (oData) {
					jQuery.sap.delayedCall(1000, this, function () {
						this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/busyIndicator", false);
					}.bind(this));
					if (oData.responseMessage.status === "SUCCESS") {
						this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/scheduleData", oData.taskList);
						this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/taskV2templateAvaliable", true);
					}
				}.bind(this), function (oError) {
					this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/busyIndicator", false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(this));
			}

		},
		getOBXLatandLngData: function (sUser) {
			var sUrl = "/taskmanagementRest/OBXScheduler/getUserLocation?userId=" + sUser;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.response.status === "SUCCESS") {
					this.getOBXListUserDetailsData(oData.coordinates.latitude, oData.coordinates.longitude, sUser);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		getOBXListUserDetailsData: function (lat, lng, sUser) {
			this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/recommentedTasklistBusy", true);
			this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/recommentedTaskList", []);
			var sUrl = "/taskmanagementRest/OBXScheduler/nearbyAssignedTask?latitude=" + lat + "&longitude=" + lng + "&userId=" + sUser;
			this.doAjax(sUrl, "GET", null, function (oData) {
				this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/recommentedTasklistBusy", false);
				if (oData.response.status === "SUCCESS") {
					$.each(oData.nearByTasks, function (index, value) { //AN: #obxSearch
						if (value.estimatedCompletionTime) {
							value.estimatedCompletionTimeString = value.estimatedCompletionTime.toString();
						}
						if (value.estimatedTotalDuration) {
							value.estimatedTotalDurationString = value.estimatedTotalDuration.toString();
						}
						if (value.estimatedTravelTime) {
							value.estimatedTravelTimeString = value.estimatedTravelTime.toString();
						}
					});
					this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/recommentedTaskList", oData.nearByTasks);
				} else {
					this._createConfirmationMessage("Error", oData.response.message, "Error", "", "Close", false, null);
				}

			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/recommentedTasklistBusy", false);
				var sErrorMessage;
				sErrorMessage = "Failed to fetch data";
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onObxRecommendationRightPress: function (oEvent) {
			var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			var isOBXReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly/isOBXReadOnly"); //SH: Modules readonly
			if (!isReadOnly && !isOBXReadOnly) {
				var oActualContext = oEvent.getParameters();
				var oContext = oActualContext.src;
				this.getModel("dashBoardModel").setProperty("/OBXSchedulingData/currentSelectedRecommend", oContext.getBindingContext(
					"dashBoardModel").getObject());
				var sTarget = oActualContext.target;
				var oFragmentId = "idObxActionList",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.ObxActionList";
				if (!this.obxActionList) {
					this.obxActionList = this._createFragment(oFragmentId, oFragmentName);
					this.getView().addDependent(this.obxActionList);
				}
				if (oContext.getBindingContext("dashBoardModel")) {

					this.obxActionList.setOffsetX(-50);
					this.obxActionList.openBy(sTarget);
				}
			}
		},
		onObxTaskAssignPress: function () {
			var oCussSelcectRecObj = this.getModel("dashBoardModel").getProperty("/OBXSchedulingData/currentSelectedRecommend");
			var oObxTaskSchedulrerData = this.getModel("dashBoardModel").getProperty("/OBXSchedulingData/scheduleData");
			var currentObx = this.getModel("dashBoardModel").getProperty("/OBXSchedulingData/currentObxObj");
			var sUrl = "/taskmanagementRest/OBXScheduler/updateOBXTask";
			var obxEndtimeList = [];
			if (oObxTaskSchedulrerData) {
				var d = new Date(); //AN: #obxUAT
				oObxTaskSchedulrerData[0].appointments.map(function (obj) {
					if (obj.status === "IN PROGRESS" && (obj.startTime >= d.getTime())) { //AN: #obxUAT
						var oObj = {
							taskId: obj.taskId,
							endTime: obj.endTime
						};
						obxEndtimeList.push(oObj);
					}
				});
			}
			var userData = this.getModel("dashBoardModel").getProperty("/userData"); //AN: #obxUAT
			var oInitialDataPayload = {
				"taskId": oCussSelcectRecObj.taskId,
				"processId": oCussSelcectRecObj.processId,
				"locationCode": oCussSelcectRecObj.locationCode,
				"assignUserEmail": currentObx.ownerEmail,
				"userUpdatedBy": this.getModel("dashBoardModel").getProperty("/userData/userId"),
				"userUpdatedAt": new Date().getTime(),
				"status": "ASSIGNED",
				"dispatchTaskEmail": oCussSelcectRecObj.assignee,
				"obxTaskIdEndTimeList": obxEndtimeList,
				"userGroup": userData.group //AN: #obxUAT
			};
			this.doAjax(sUrl, "POST", oInitialDataPayload, function (oData) {
					if (oData.statusCode === "0") {
						this._showToastMessage(oData.message);
						this.getOBXTaskSchedulingDataForUser(currentObx.ownerEmail);
						this.getOBXLatandLngData(currentObx.ownerEmail);
						var aOwnersNotif = [];
						if (currentObx.ownerEmail) { //SK: # Notification on reassigning.
							var oUrl = "/taskmanagementRest/ShiftRegister/getShiftDetails?emp_email=" + currentObx.ownerEmail;
							var oModel = new sap.ui.model.json.JSONModel();
							oModel.loadData(oUrl, "", false, "GET", false, false);
							if (oModel.getData()) {
								var oPayload = {
									taskOwner: currentObx.ownerEmail,
									taskOwnerDisplayName: currentObx.taskOwnerDisplayName,
									ownerEmail: currentObx.ownerEmail,
									pId: currentObx.pId
								}
								aOwnersNotif.push(oPayload);
								this.sendNotification("You have a new task assigned", aOwnersNotif, true);
							}
						}
					} else {

						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		/* OBX Changes By Rashmi End*/
		/** Event to capture IconTabBar Select
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOBXTabSelected: function (oEvent) { //AN: #obxEngine
			var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
			if (oOBXEngineData) {
				oOBXEngineData.obxAllocation = [];
				oOBXEngineData.proOperatorList = [];
				oOBXEngineData.obxOperatorList = [];
				oOBXEngineData.obxWorkLoadList = [];
			}
			var selectedKey = oEvent.getSource().getSelectedKey();
			if (selectedKey === "obxScheduling") {
				this.getOBXUserListData();
			} else if (selectedKey === "obxEngine") {
				this.getModel("dashBoardModel").setProperty("/ObxEngine", 0);
				this.oBusyInd.open();
				// Call service to fetch the configurations
				this.getOBXConfig();
				this.bDescending = false;
				var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
				var oDay = new Date().getDay();
				var oDayArr = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
				oOBXEngineData.selOBXDay = oDayArr[oDay];
				var oRoleGrp = this.getModel("dashBoardModel").getData().userData.resGroupRead.split(",")[0];
				var oFieldLoc = oRoleGrp.split("_")[3];
				if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
						"/isOBXReadOnly")) {
					oRoleGrp = "IOP_TM_ROC_Catarina";
					oFieldLoc = "Catarina";
				}
				if (oFieldLoc !== undefined) {
					if (oFieldLoc.indexOf("Tilden") >= 0) {
						oFieldLoc = "Tilden";
					}
					oOBXEngineData.selOBXField = oFieldLoc;
					this.onOBXFieldDaySelectChange(); //set day and location for OBX allocation
					this.getModel("dashBoardModel").refresh(true);
				} else {
					this.oBusyInd.close();
				}
			}
		},

		//OBXEngine-function call to get OBX configurations
		getOBXConfig: function () {
			this.getModel("dashBoardModel").setProperty("busyIndicators/OBXEngine", true);
			var sUrl = "/taskmanagementRest/OBXScheduler/getConfigDetails";
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (!oData[0].responseMessage) {
						this.oBusyInd.close();
						return "";
					}
					if (oData[0].responseMessage.statusCode === "0") {
						this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine.config = oData[0].responseConfigMap;
						this.getModel("dashBoardModel").refresh(true);
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						this.oBusyInd.close();
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					this.oBusyInd.close();
				}.bind(this));
		},

		//OBXEngine-function triggered on live change of OBX Configurations
		onOBXConfigInputChange: function (oEvent) {
			var oInp = oEvent.getSource();
			oInp.setValueState("None");
			var oInpVal = parseFloat(oInp.getValue());
			var oMin = parseFloat(oInp.getCustomData()[0].getValue());
			var oMax = parseFloat(oInp.getCustomData()[1].getValue());
			var oLabel = "";
			if (oInp.getCustomData()[2]) {
				oLabel = oInp.getCustomData()[2].getValue();
			}
			if (oInpVal !== 0) {
				if (!oInpVal) { // as !0 will be returned as true and we need 0 as value
					oInp.setValueState("Error");
					oInp.setValue("");
					return "";
				}
			}
			if (oInpVal > oMax || oInpVal < oMin) {
				oInp.setValue("");
				oInp.setValueState("Error");
				sap.m.MessageToast.show("Please Enter value between " + oMin + " and " + oMax);
				return "";
			}
			var oDashBoardModel = this.getModel("dashBoardModel");
			var oOBXEngineData = oDashBoardModel.getData().OBXSchedulingData.obxEngine.config;
			if (oLabel === "shiftDuration") {
				//if shift duration is changed make Workload factor to be 0
				oOBXEngineData.UPLOAD_WORKFACTOR_PERCENT = "0";
			}
			if (oLabel === "workload") {
				var oShiftDur = parseFloat(oOBXEngineData.SHIFT_DURATION);
				if (!oShiftDur) {
					return "";
				}
				var oWorkLoad = oInpVal;
				var oPercentValue = (oWorkLoad / 100) * oShiftDur;
				var oNewShiftDur = oShiftDur - oPercentValue;
				if (oNewShiftDur > 24 || oNewShiftDur < 1) {
					oOBXEngineData.SHIFT_DURATION = "";
					sap.m.MessageToast.show("Shift Duration should be between 1 and 24");
				} else {
					oOBXEngineData.SHIFT_DURATION = oNewShiftDur.toFixed(2);
				}
			}
			oDashBoardModel.refresh(true);
		},

		/** Button press Event to run the obx Engine
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onRunOBXEngine: function (oEvent) { //AN: #obxEngine
			var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine.config;
			// if (!oOBXEngineData.DURATION_STOP_BY_WELLS || !oOBXEngineData.NUM_OBX_OPERATOR_EFS || !oOBXEngineData.UPLOAD_WORKFACTOR_PERCENT ||
			// 	!oOBXEngineData.SHIFT_DURATION) { //AN: #obxUAT
			if (!oOBXEngineData.DURATION_STOP_BY_WELLS || !oOBXEngineData.UPLOAD_WORKFACTOR_PERCENT ||
				!oOBXEngineData.SHIFT_DURATION) { //AN: #obxUAT
				sap.m.MessageToast.show("Please Enter All Mandatory Fields");
				return "";
			}
			var oCheckFlag = this.checkEngineRun();
			if (!oCheckFlag) {
				MessageBox.error("Please wait as OBX Engine is already running!", {
					styleClass: "sapUiSizeCompact"
				});
				return "";
			}
			var sMsg = "Are you sure you want to run the OBX Engine?",
				sTitle = "Warning",
				sState = "Warning",
				confirmYesBtn = "Yes",
				confirmNoBtn = "No";
			this._createConfirmationMessage(sTitle, sMsg, sState, confirmYesBtn, confirmNoBtn, true, function () {
				this.oBusyInd.open();
				this.oBusyInd.setText("Running OBX Engine. Please Wait...");
				var oDate = new Date();
				var yyyy = oDate.getFullYear();
				var MM = oDate.getMonth() + 1;
				if (MM < 10) {
					MM = "0" + MM;
				}
				var dd = oDate.getDate();
				if (dd < 10) {
					dd = "0" + dd;
				}
				var hh = oDate.getHours();
				if (hh < 10) {
					hh = "0" + hh;
				}
				var mm = oDate.getMinutes();
				if (mm < 10) {
					mm = "0" + mm;
				}
				var ss = oDate.getSeconds();
				if (ss < 10) {
					ss = "0" + ss;
				}
				var oDateString = yyyy.toString() + "-" + MM.toString() + "-" + dd.toString() + " " + hh + ":" + mm + ":" + ss + ".000";
				var oUserName = this.getModel("dashBoardModel").getData().userData.displayName;
				var oPayload = {
					"obxEngineUpdatedBy": oUserName,
					"lastUpdatedDateTime": oDateString,
					"shiftDuration": oOBXEngineData.SHIFT_DURATION,
					"uploadWorkFactor": oOBXEngineData.UPLOAD_WORKFACTOR_PERCENT,
					"durationStopWells": oOBXEngineData.DURATION_STOP_BY_WELLS,
					// "numObxOperator": oOBXEngineData.NUM_OBX_OPERATOR_EFS, //AN: #obxUAT
					"obxEngineRunningFlag": "TRUE"
				};
				var sUrl = "/taskmanagementRest/OBXScheduler/runObxEngineTask";
				this.doAjax(sUrl, "POST", oPayload, function (oData) {
						if (oData.statusCode === "0") {
							this._showToastMessage(oData.message);
							this.getOBXConfig();
							this.bDescending = false;
							var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
							var oDay = new Date().getDay();
							var oDayArr = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
							oOBXEngineData.selOBXDay = oDayArr[oDay];
							var oRoleGrp = this.getModel("dashBoardModel").getData().userData.resGroupRead.split(",")[0];
							var oFieldLoc = oRoleGrp.split("_")[3];
							if (oFieldLoc.indexOf("Tilden") >= 0) {
								oFieldLoc = "Tilden";
							}
							oOBXEngineData.selOBXField = oFieldLoc;
							this.onOBXFieldDaySelectChange();
							this.oBusyInd.setText("");
							this.getModel("dashBoardModel").refresh(true);
						} else {
							//this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
							MessageBox.error(oData.message, {
								styleClass: "sapUiSizeCompact"
							});
							this.oBusyInd.setText("");
							this.oBusyInd.close();
						}
					}.bind(this),
					function (oError) {
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
						this.oBusyInd.setText("");
						this.oBusyInd.close();
					}.bind(this));
			});
		},
		//OBXEngine-function triggered on Day or Field Selection Change
		onOBXFieldDaySelectChange: function () {
			this.fetchOBXOprAllocation();
			var oDashBoardModelData = this.getModel("dashBoardModel").getData();
			var oAllRoleGrp = oDashBoardModelData.userData.resGroupRead.toUpperCase();
			var oToday = new Date();
			var oDay = oToday.getDay();
			var oDayArr = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
			var oCurrDay = oDayArr[oDay];
			var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
			var oSelOBXField = oOBXEngineData.selOBXField;
			var oSelOBXDay = oOBXEngineData.selOBXDay;
			oOBXEngineData.isEditableOperators = false;
			//Reallocation disabled if All Fields or All Days are selected
			// if (oSelOBXField.toUpperCase() === "ALL" || oSelOBXDay.toUpperCase() === "ALL") { //AN: #obxDemo
			if (oSelOBXField.toUpperCase() === "ALL") { //AN: #obxDemo
				return "";
			}
			//Reallocation to be only enabled for 12AM-5:30AM and for that day and the allocated Fields
			if (oToday.getHours() < 5 || (oToday.getHours() === 5 && oToday.getMinutes() <= 30)) {
				// if (oAllRoleGrp.indexOf(oSelOBXField.toUpperCase()) === -1 || oSelOBXDay !== oCurrDay) { //AN: #obxDemo
				if (oAllRoleGrp.indexOf(oSelOBXField.toUpperCase()) === -1) { //AN: #obxDemo
					oOBXEngineData.isEditableOperators = false;
				} else {
					oOBXEngineData.isEditableOperators = true;
				}
			}
		},
		/** Select Change Event to reassign obx operator to the well
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOBXOperatorSelectChange: function (oEvent) { //AN: #obxEngine
			var oCheckFlag = this.checkEngineRun();
			if (!oCheckFlag) {
				MessageBox.error("Please wait as OBX Engine is already running!", {
					styleClass: "sapUiSizeCompact"
				});
				this.fetchOBXOprAllocation();
				return "";
			}
			var oContextObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oSelUserEmail = oEvent.getSource().getSelectedKey();
			var oDashBoardModelData = this.getModel("dashBoardModel").getData();
			var oSelOBXDay = oDashBoardModelData.OBXSchedulingData.obxEngine.selOBXDay;
			oContextObj.proOperatorEmail = "";
			var oPayload = {
				"taskOwnerEmail": oSelUserEmail,
				"updatedByEmail": oDashBoardModelData.userData.userId,
				"locationCode": oContextObj.locationCode,
				"selectedDay": oSelOBXDay,
				"isObxUser": "True"
			};
			this.oBusyInd.open();
			var sUrl = "/taskmanagementRest/OBXScheduler/updateTaskForOperator";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.statusCode === "0") {
						this.fetchOBXOprAllocation();
					} else {
						// oEvent.getSource().setSelectedKey("");
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						this.oBusyInd.close();
					}
				}.bind(this),
				function (oError) {
					oEvent.getSource().setSelectedKey("");
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					this.oBusyInd.close();
				}.bind(this));
		},
		/** Select Change Event to reassign pro operator to the well
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onPROOperatorSelectChange: function (oEvent) { //AN: #obxEngine
			var oCheckFlag = this.checkEngineRun();
			if (!oCheckFlag) {
				MessageBox.error("Please wait as OBX Engine is already running!", {
					styleClass: "sapUiSizeCompact"
				});
				this.fetchOBXOprAllocation();
				return "";
			}
			var oContextObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oSelUserEmail = oEvent.getSource().getSelectedKey();
			var oDashBoardModelData = this.getModel("dashBoardModel").getData();
			var oSelOBXDay = oDashBoardModelData.OBXSchedulingData.obxEngine.selOBXDay;
			oContextObj.obxOperatorEmail = "";
			var oPayload = {
				"taskOwnerEmail": oSelUserEmail,
				"updatedByEmail": oDashBoardModelData.userData.userId,
				"locationCode": oContextObj.locationCode,
				"selectedDay": oSelOBXDay,
				"isObxUser": "False"
			};
			this.oBusyInd.open();
			var sUrl = "/taskmanagementRest/OBXScheduler/updateTaskForOperator";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.statusCode === "0") {
						this.fetchOBXOprAllocation();
					} else {
						// oEvent.getSource().setSelectedKey("");
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						this.oBusyInd.close();
					}
				}.bind(this),
				function (oError) {
					oEvent.getSource().setSelectedKey("");
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					this.oBusyInd.close();
				}.bind(this));
		},

		//OBXEngine-function to fetch OBX Operator allocation
		fetchOBXOprAllocation: function () {
			this.oBusyInd.open();
			var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
			var oRoleGrp = this.getModel("dashBoardModel").getData().userData.resGroupRead.split(",")[0];
			if (this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole") || this.getModel("dashBoardModel").getProperty(
					"/isOBXReadOnly")) {
				var sUrl = "/taskmanagementRest/OBXScheduler/getObxTaskAllocated?roles=IOP_TM_ROC_Catarina&field=Catarina" +
					"&selectedDay=" + oOBXEngineData.selOBXDay;
			} else {
				var sUrl = "/taskmanagementRest/OBXScheduler/getObxTaskAllocated?roles=" + oRoleGrp + "&field=" + oOBXEngineData.selOBXField +
					"&selectedDay=" + oOBXEngineData.selOBXDay;
			}
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (!oData.responseMessage) {
						this.oBusyInd.close();
						return "";
					}
					if (oData.responseMessage.statusCode === "0") {
						$.each(oData.obxList, function (index, value) { //AN: #obxSearch
							if (value.clusterdId) {
								value.clusterdIdSrting = value.clusterdId.toString();
							}
						});
						oOBXEngineData.obxAllocation = oData.obxList;
						oOBXEngineData.proOperatorList = oData.proOperatorList;
						oOBXEngineData.obxOperatorList = oData.obxOperatorList;
						oOBXEngineData.unAssignedWellsNum = oData.unAssignedWellsNum;
						if (oOBXEngineData.unAssignedWellsNum) {
							oOBXEngineData.obxEngineInfoToolBarText = "Number of Unassigned Wells- " + oOBXEngineData.unAssignedWellsNum;
						} else {
							oOBXEngineData.obxEngineInfoToolBarText = "All Wells are Assigned.";
						}
						this.getOBXOprWorkload();

					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						this.oBusyInd.close();
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					this.oBusyInd.close();
				}.bind(this));
		},

		//function to fetch the workload of OBX opertaors
		getOBXOprWorkload: function () {
			this.oBusyInd.open();
			var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
			var sUrl = "/taskmanagementRest/OBXScheduler/getObxWorkload?field=" + oOBXEngineData.selOBXField +
				"&selectedDay=" + oOBXEngineData.selOBXDay;
			oOBXEngineData.obxWorkLoadList = [];
			this.doAjax(sUrl, "GET", null, function (oData) {
					if (!oData.responseMessage) {
						this.oBusyInd.close();
						return "";
					}
					if (oData.responseMessage.statusCode === "0") {
						$.each(oData.obxWorkLoadList, function (index, value) { //AN: #obxSearch
							if (value.clusterId) {
								value.clusterIdSrting = value.clusterId.toString();
							}
							if (value.workLoad) {
								value.workLoadString = value.workLoad.toString();
							}
						});
						oOBXEngineData.obxWorkLoadList = oData.obxWorkLoadList;
						this.getModel("dashBoardModel").refresh(true);
						this.oBusyInd.close();

					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						this.oBusyInd.close();
					}
				}.bind(this),
				function (oError) {
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					this.oBusyInd.close();
				}.bind(this));

		},
		/** Search liveChnage Event to search OBX Operator Allocation table Data
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOBXOperatorAllocationSearch: function (oEvent) { //AN: #obxEngine
			var value = oEvent.getSource().getValue();
			var oDTList = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXOperatorAllocationTable");
			var aFilters;
			// var filterParams = ["well", "tier", "obxOperator", "proOperator"]; // Map the relevant parameters
			var filterParams = ["well", "tier", "obxOperator", "proOperator", "clusterdIdSrting"]; // Map the relevant parameters //AN: #obxSearch
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oDTList.getBinding("items").filter(aFilters);
		},

		/** Search liveChange Event to search OBX Operator Allocation table Data
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOBXOperatorsTableSearch: function (oEvent) { //AN: #obxEngine
			var value = oEvent.getSource().getValue();
			var oDTList = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXOperatorsTable");
			var aFilters;
			// var filterParams = ["obxOperatorfullName"]; // Map the relevant parameters
			var filterParams = ["obxOperatorfullName", "clusterIdSrting", "workLoadString"]; // Map the relevant parameters //AN: #obxSearch
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oDTList.getBinding("items").filter(aFilters);
		},
		/** Event to capture obx operator legend Info icon click
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOperatorLegendPress: function (oEvent) { //AN: #obxEngine
			var oFragmentId = "OBXMOckupPopover",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.ObxWorkloadLegend";
			if (!this.ObxWorkloadLegendPopover) {
				this.ObxWorkloadLegendPopover = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.ObxWorkloadLegendPopover);
			}
			this.ObxWorkloadLegendPopover.openBy(oEvent.getSource());
		},

		//function to Sort OBX Operators based on Workload
		onOBXOperatorSort: function () {
			var oDTList = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXOperatorsTable");
			var aSorters = [];
			if (this.bDescending) {
				this.bDescending = false;
			} else {
				this.bDescending = true;
			}
			aSorters.push(new sap.ui.model.Sorter("workLoad", this.bDescending));
			oDTList.getBinding("items").sort(aSorters);
		},

		//OBX Engine-function to check if Engine is Running before Excel Export
		checkEngineRun: function () {
			var that = this;
			this.oBusyInd.open();
			var sUrl = "/taskmanagementRest/OBXScheduler/checkObxRunningFlag";
			var oEngineRunCheck = false;
			$.ajax({
				url: sUrl,
				method: "GET",
				async: false,
				success: function (result, xhr, data) {
					if (result.statusCode === "0" && result.message === "false") {
						that.oBusyInd.close();
						oEngineRunCheck = true;
					} else {
						that.oBusyInd.close();
					}

				},
				error: function (result, xhr, data) {
					MessageBox.error(data, {
						styleClass: "sapUiSizeCompact"
					});
					that.oBusyInd.close();
				}
			});
			return oEngineRunCheck;
		},

		//OBX Engine-function to export OBX Allocation as excel
		onExportExcel: function () {
			var oCheckFlag = this.checkEngineRun();
			if (!oCheckFlag) {
				MessageBox.error("Please wait as OBX Engine is already running!", {
					styleClass: "sapUiSizeCompact"
				});
				return "";
			}
			var that = this;
			var oExportModel = new sap.ui.model.json.JSONModel();
			this.getView().setModel(oExportModel, "oExportModel");
			var oTable = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXOperatorAllocationTable");
			var oAllItems = oTable.getItems();
			var oTableArray = [];
			for (var i = 0; i < oAllItems.length; i++) {
				var oRowData = oAllItems[i].getBindingContext("dashBoardModel").getObject();
				oTableArray.push(oRowData);
			}
			oExportModel.setData({
				"results": oTableArray
			});
			oExportModel.refresh(true);

			/*export as xls*/
			var aCols = [];
			aCols = [{
					label: 'FIELD',
					property: 'field',
					type: 'string'
				}, {
					label: 'DAY',
					property: 'day',
					type: 'string'
				}, {
					label: 'CLUSTER ID',
					property: 'clusterdId',
					type: 'string'
				}, {
					label: 'WELL',
					property: 'well',
					type: 'string'
				}, {
					label: 'TIER',
					property: 'tier',
					type: 'string'
				}, {
					label: 'OBX OPERATOR',
					property: 'obxOperator',
					type: 'string'
				},
				// {
				// 	label: 'PRO OPERATOR',
				// 	property: 'proOperator',
				// 	type: 'string'
				// },
				{
					label: 'SEQUENCE',
					property: 'sequence',
					type: 'string'
				}, {
					label: 'DRIVE TIME (in mins)', //AN: #obxDemo
					property: 'driveTime',
					type: 'string'
				}, {
					label: 'STOP TIME (in mins)', //AN: #obxDemo
					property: 'taskTime',
					type: 'string'
				}
			];
			var oOBXEngineData = this.getModel("dashBoardModel").getData().OBXSchedulingData.obxEngine;
			if (oOBXEngineData.selOBXDay === "All") {
				var oFieldName = "WEEK";
			} else {
				oFieldName = oOBXEngineData.selOBXDay;
			}
			var oToday = new Date();
			var dd = oToday.getDate();
			if (dd < 10) {
				dd = "0" + dd;
			}
			var mm = oToday.getMonth() + 1;
			if (mm < 10) {
				mm = "0" + mm;
			}
			var yyyy = oToday.getFullYear();
			var oDate = mm + "-" + dd + "-" + yyyy;
			var oTime = oToday.getHours() + "-" + oToday.getMinutes() + "-" + oToday.getSeconds();
			var oFileName = "OBX_SCHEDULE" + "_" + oOBXEngineData.selOBXField + "_" + oFieldName + "_" + oDate + "T" + oTime;

			var oSettings = {
				workbook: {
					columns: aCols
				},
				dataSource: oExportModel.getData().results,
				worker: false,
				fileName: oFileName,
				showProgress: false
			};

			new Spreadsheet(oSettings).build();
		},
		/** Search liveChnage Event to search OBX Scheduler Operator Gain/loss table Data
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOBXSchedulerOperatorsTableSearch: function (oEvent) { //AN: #obxSchedulingNewView2
			var value = oEvent.getSource().getValue();
			var oDTList = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "obxppl");
			var aFilters;
			var filterParams = ["taskOwnerDisplayName", "gainTime"]; // Map the relevant parameters
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oDTList.getBinding("items").filter(aFilters);
		},
		/** Search liveChnage Event to search OBX Scheduler Recommended Task List table Data
		 * @public
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onOBXSchedulerRecommendedTaskListTableSearch: function (oEvent) { //AN: #obxSchedulingNewView2
			var value = oEvent.getSource().getValue();
			var oDTList = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "obxsuggestions");
			var aFilters;
			// var filterParams = ["taskDescription", "locationText", "assigneeName"]; // Map the relevant parameters
			var filterParams = ["taskDescription", "locationText", "assigneeName", "estimatedCompletionTimeString",
				"estimatedTotalDurationString", "estimatedTravelTimeString"
			]; // Map the relevant parameters //AN: #obxSearch
			var filterArray = [];
			if (value) {
				for (var i = 0; i < filterParams.length; i++) {
					filterArray.push(new sap.ui.model.Filter(filterParams[i], sap.ui.model.FilterOperator.Contains, value));
				}
				aFilters = new sap.ui.model.Filter({
					filters: filterArray,
					and: false
				});
			}
			oDTList.getBinding("items").filter(aFilters);
		},
		/** 
		 * Event to capture task management filter panel's expansion
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskFilterPanelExpand: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskFilterPanelExpand(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture task management filter panel's expansion
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskfilterLinkPress: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskfilterLinkPress(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Field filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onFieldFilterSelectionChange: function (oEvent) { //AN: #taskFieldFilter
			TaskManagementHelper.onFieldFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Task Type filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskTypeFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskTypeFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture task location filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onLocationFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onLocationFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Task Classification filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskClassFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskClassFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Sub Classification filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onSubClassFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onSubClassFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Issue Classification filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onIssueClassFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onIssueClassFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Task Status filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskStatusFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskStatusFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Assigned To filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskAssignedToFilterSelectionChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskAssignedToFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Task Created On filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskCreatedOnFilterChange: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskCreatedOnFilterChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * Event to capture Task Created By filter selection change
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onCreatedByFilterSelectionChange: function (oEvent) { //AN: #taskV2Demo
			TaskManagementHelper.onCreatedByFilterSelectionChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		/** 
		 * To clear all the task filters
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskClearFilter: function (oEvent) { //AN: #taskFilter
			TaskManagementHelper.onTaskClearFilter(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		setTaskPanelButtonsEnablement: function (enable) { //AN: #obxUAT
			if (enable) {
				this.getModel("dashBoardModel").setProperty("/taskPanelButtonsEnablement", {
					dispatchCreateTaskEnabled: true,
					dispatchCreateTaskPREnabled: true,
					dispatchCloseTaskEnabled: true,
					dispatchClosePigTaskEnabled: true,
					dispatchUpdateTaskEnabled: true,
					dispatchIssuePLEnabled: true,
					dispatchIssuePREnabled: true,
				});
			} else {
				this.getModel("dashBoardModel").setProperty("/taskPanelButtonsEnablement", {
					dispatchCreateTaskEnabled: false,
					dispatchCreateTaskPREnabled: false,
					dispatchCloseTaskEnabled: false,
					dispatchClosePigTaskEnabled: false,
					dispatchUpdateTaskEnabled: false,
					dispatchIssuePLEnabled: false,
					dispatchIssuePREnabled: false,
				});
			}
		},
		onLocHistoryIconTabSelect: function (oEvent) {
			var oSelectedLocTab = this.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.iconFilterTab;
			LocationHistoryHelper.locHistoryIconTabSelect(this, oSelectedLocTab, this.getModel("dashBoardModel"));
		},
		onGetWorkorderDetails: function (oEvent) {
			LocationHistoryHelper.getWorkorderDetails(this, oEvent, this.getModel("dashBoardModel"));
		},
		onCloseDetailsFragment: function (oEvent) {
			LocationHistoryHelper.onCloseDetails(this, oEvent, this.getModel("dashBoardModel"));
		},
		onSegmentedButtonClick: function (oEvent) {
			var oSelectedLocTab = this.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.iconFilterTab;
			LocationHistoryHelper.locHistoryIconTabSelect(this, oSelectedLocTab, this.getModel("dashBoardModel"));
		},
		onGetTaskDetails: function (oEvent) {
			LocationHistoryHelper.getTaskDetails(this, oEvent, this.getModel("dashBoardModel"));
		},
		onGetBypassLogDetails: function (oEvent) {
			LocationHistoryHelper.getBypassLogDetails(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPreviousPagePress: function (oEvent) {
			LocationHistoryHelper.previousPagePress(this, oEvent, this.getModel("dashBoardModel"));
		},
		onNextPagePress: function (oEvent) {
			LocationHistoryHelper.nextPagePress(this, oEvent, this.getModel("dashBoardModel"));
		},
		onGetJSADetails: function (oEvent) {
			LocationHistoryHelper.PTWJSATableItemPress(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPTWFilterChange: function (oEvent) {
			LocationHistoryHelper.PTWFilterChange(this, oEvent, this.getModel("dashBoardModel"));
		},
		onGetPTWPermitDetails: function (oEvent) {
			LocationHistoryHelper.PTWPermitTableItemPress(this, oEvent, this.getModel("dashBoardModel"));
		},
		onLinkPress: function (oEvent) {
			LocationHistoryHelper.attachmentDownload(this, oEvent, this.getModel("dashBoardModel"));
		},
		onDateSort: function (oEvent) {
			LocationHistoryHelper.sortingOnDate(this, oEvent, this.getModel("dashBoardModel"));
		},
		onLHDowntimeChange: function (oEvent) {
			var oSelectedKeys = oEvent.getSource().getSelectedKeys();
			var oDowntimeKeys = "";
			if (oSelectedKeys.length >= 1) {
				for (var i = 0; i < oSelectedKeys.length - 1; i++) {
					oDowntimeKeys += oSelectedKeys[i] + ",";
				}
				oDowntimeKeys += oSelectedKeys[i];
			}
			this.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.LocHistoryDowntimeFilterKeys = oDowntimeKeys;
			LocationHistoryHelper.locHistoryIconTabSelect(this, "Downtime", this.getModel("dashBoardModel"));
		},
		onBypassLinkPress: function (oEvent) {
			LocationHistoryHelper.attachmentDownload(this, oEvent, this.getModel("dashBoardModel"));
		},
		onFromClose: function () {
			if (this.oJSAForm) {
				this.oJSAForm.close();
			}
			if (this.oCWPForm) {
				this.oCWPForm.close();
			}
			if (this.oEIForm) {
				this.oEIForm.close();
			}
			if (this.oHWPForm) {
				this.oHWPForm.close();
			}
			if (this.oCSPForm) {
				this.oCSPForm.close();
			}
			if (this.oBypassLogDialog) {
				this.oBypassLogDialog.close();
			}
		},
		onPTWDropDownFilterChange: function (oEvent) {
			LocationHistoryHelper.PTWDropDownFilterChange(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPTWMonthFilterChange: function (oEvent) {
			LocationHistoryHelper.PTWMonthFilterChange(this, oEvent, this.getModel("dashBoardModel"));
		},
		onMonthFilterEnergyIsolationChange: function (oEvent) {
			LocationHistoryHelper.EnergyIsolationMonthFilterChange(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPtwTableSearch: function (oEvent) {
			LocationHistoryHelper.PtwTableSearch(this, oEvent, this.getModel("dashBoardModel"));
		},
		onTableSearch: function (oEvent) {
			LocationHistoryHelper.onSearch(this, oEvent, this.getModel("dashBoardModel"));
		},
		onGetEnergyIsolationDetails: function (oEvent) {
			LocationHistoryHelper.EnergyIsolationItemClick(this, oEvent, this.getModel("dashBoardModel"));

		},
		onEnergyIsolationFormClose: function (oEvent) {
			this.oEnergyIsolationForm.close();
		},
		//Module Nav - Anand
		openTaskPanelFromNav: function (oTaskId) {
			var sBtnText = "Create Task";
			var oDashBoardModel = this.getModel("dashBoardModel");
			oDashBoardModel.setProperty("/taskId", oTaskId);
			oDashBoardModel.setProperty("/isIconPress", true);
			oDashBoardModel.setProperty("/isInvestigationCreate", false);
			oDashBoardModel.setProperty("/isDispatch", false);
			oDashBoardModel.setProperty("/isInquiryCreate", true);
			oDashBoardModel.setProperty("/taskOwner", "");
			this.onCreateTaskPress("", sBtnText);
		},
		checkIfTaskIsActive: function (oArgsQuery) { //AN: #EmailNotif
			var oDashBoardModel = this.getModel("dashBoardModel");
			var oUserData = oDashBoardModel.getProperty("/userData");
			var oUserGroup = "";
			if (oUserData.isENG) {
				oUserGroup = oUserData.engRole;
			} else if (oUserData.isROC) {
				oUserGroup = oUserData.resGroupRead;
			} else if (oUserData.isPOT) {
				oUserGroup = oUserData.potRole;
			}
			var sUrl = "/taskmanagementRest/tasks/taskActive?taskId=" + oArgsQuery.taskId + "&processId=" + oArgsQuery.processId +
				"&userEmailId=" + oUserData.userId + "&userGroup=" + oUserGroup;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.statusCode === "0") {
					if (oData.message === "INQ_Atv") {
						this.openTaskPanelFromNav(oArgsQuery.taskId);
					} else if (oData.message) {
						sap.m.MessageToast.show(oData.message);
					}
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);

			}.bind(this));
		},

		onPrintCW: function (oEvent) {
			this.getModel("dashBoardModel").getData().locationHistorySelectTabs.selectedForm = "ColdWorkPermit";
			LocationHistoryHelper.onPrintPage(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPrintHW: function (oEvent) {
			this.getModel("dashBoardModel").getData().locationHistorySelectTabs.selectedForm = "HotWorkPermit";
			LocationHistoryHelper.onPrintPage(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPrintJSA: function (oEvent) {
			this.getModel("dashBoardModel").getData().locationHistorySelectTabs.selectedForm = "JSAPermit";
			LocationHistoryHelper.onPrintPage(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPrintCSP: function (oEvent) {
			this.getModel("dashBoardModel").getData().locationHistorySelectTabs.selectedForm = "CSPPermit";
			LocationHistoryHelper.onPrintPage(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPrintBypassLog: function (oEvent) {
			this.getModel("dashBoardModel").getData().locationHistorySelectTabs.selectedForm = "BypassLogForm";
			LocationHistoryHelper.onPrintPage(this, oEvent, this.getModel("dashBoardModel"));
		},
		onPrintEnergyIsolation: function (oEvent) {
			this.getModel("dashBoardModel").getData().locationHistorySelectTabs.selectedForm = "EnergyIsolationForm";
			LocationHistoryHelper.onPrintPage(this, oEvent, this.getModel("dashBoardModel"));
		},
		onChangeSeatCreatedByMeCheckboxSelect: function (oEvent) { //AN: #ChangeSeat
			ChangeSeatHelper.onChangeSeatCreatedByMeCheckboxSelect(this, this.getModel("dashBoardModel"), oEvent);
		},
		onChangeSeatProceedButtonPress: function (oEvent) { //AN: #ChangeSeat
			ChangeSeatHelper.onChangeSeatProceedButtonPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onFracAlertClose: function (oEvent) { //RV: #webSocket //AN: #Notif
			NotificationsHelper.onFracAlertClose(this, this.getModel("dashBoardModel"), oEvent);
		},
		onFracListItemPress: function (oEvent) { //AN: #Notif
			NotificationsHelper.onFracListItemPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onAcknowledgeFracNotif: function (oEvent) { //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/webSocketProperty/fracInd", true); //AN: #loaderWhileNotif
			this.acknowledgeWebSocketRequest("Frac");
		},
		onTaskAlertClose: function (oEvent) { //AN: #Notif
			NotificationsHelper.onTaskAlertClose(this, this.getModel("dashBoardModel"), oEvent);
		},
		onTaskListItemPress: function (oEvent) { //AN: #Notif
			NotificationsHelper.onTaskListItemPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onAcknowledgeTasksNotif: function (oEvent) { //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/webSocketProperty/tasksInd", true); //AN: #loaderWhileNotif
			this.acknowledgeWebSocketRequest("Tasks");
		},
		onAlarmAlertClose: function (oEvent) { //AN: #Notif
			NotificationsHelper.onAlarmAlertClose(this, this.getModel("dashBoardModel"), oEvent);
		},
		onAlarmListItemPress: function (oEvent) { //AN: #Notif
			NotificationsHelper.onAlarmListItemPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onAcknowledgeAlarmsNotif: function (oEvent) { //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/webSocketProperty/alarmsInd", true); //AN: #loaderWhileNotif
			this.acknowledgeWebSocketRequest("Alarms");
		},
		onPwHopperAlertClose: function (oEvent) { //AN: #Notif
			NotificationsHelper.onPwHopperAlertClose(this, this.getModel("dashBoardModel"), oEvent);
		},
		onPwHopperListItemPress: function (oEvent) { //AN: #Notif
			NotificationsHelper.onPwHopperListItemPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onAcknowledgePwHopperNotif: function (oEvent) { //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/webSocketProperty/pwHopperInd", true); //AN: #loaderWhileNotif
			this.acknowledgeWebSocketRequest("PwHopper");
		},
		onBypassLogAlertClose: function (oEvent) { //AN: #Notif
			NotificationsHelper.onBypassLogAlertClose(this, this.getModel("dashBoardModel"), oEvent);
		},
		onBypassLogListItemPress: function (oEvent) { //AN: #Notif
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeShiftButtonVisibility
			oContext.ssdBypassId = oContext.objectId;
			oDashBoardModel.setProperty("/webSocketProperty/bypassLogInd", true); //AN: #loaderWhileNotif
			if (oContext.status !== "Created") { //AN: #ChangeShiftButtonVisibility
				oDashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", true); //AN: #Notif
			} else {
				oDashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", false);
			}
			TaskManagementHelper.onPressActiveBypassListitem(this, this.getModel("dashBoardModel"), oContext);
			this.acknowledgeWebSocketRequest("BypassLog", oContext, "External");
		},
		onAcknowledgeBypassLogNotif: function (oEvent) { //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/webSocketProperty/bypassLogInd", true); //AN: #loaderWhileNotif
			this.acknowledgeWebSocketRequest("BypassLog");
		},
		onEnergyIsoAlertClose: function (oEvent) { //AN: #Notif
			NotificationsHelper.onEnergyIsoAlertClose(this, this.getModel("dashBoardModel"), oEvent);
		},
		onEnergyIsoListItemPress: function (oEvent) { //AN: #Notif
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeShiftButtonVisibility
			oContext.ssdBypassId = oContext.objectId;
			oDashBoardModel.setProperty("/webSocketProperty/energyIsoInd", true); //AN: #loaderWhileNotif
			if (oContext.status !== "Created") { //AN: #ChangeShiftButtonVisibility
				oDashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", true); //AN: #Notif
			} else {
				oDashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", false);
			}
			TaskManagementHelper.onPressEnergyIsoListitem(this, this.getModel("dashBoardModel"), oContext);
			this.acknowledgeWebSocketRequest("EnergyIso", oContext, "External");
		},
		onAcknowledgeEnergyIsoNotif: function (oEvent) { //AN: #Notif
			this.getModel("dashBoardModel").setProperty("/webSocketProperty/energyIsoInd", true); //AN: #loaderWhileNotif
			this.acknowledgeWebSocketRequest("EnergyIso");
		},
		onOpenNotification: function (oEvent) { //AN: #Notif
			NotificationsHelper.onOpenNotification(this, this.getModel("dashBoardModel"), oEvent);
		},

		onPressActiveBypassVBox: function () { //Anand: #handover
			TaskManagementHelper.onPressActiveBypassVBox(this, this.getModel("dashBoardModel"));
		},
		fetchAllActiveData: function () { //Anand: #handover
			TaskManagementHelper.fetchAllActiveData(this, this.getModel("dashBoardModel"));
		},
		onPressActiveListitem: function (oEvent) { //Anand: #handover
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var oDashBoardModel = this.getModel("dashBoardModel"); //AN: #ChangeShiftButtonVisibility
			if (oContext.source === "Bypass Log") {
				oDashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", true); //AN: #ChangeShiftButtonVisibility
				TaskManagementHelper.onPressActiveBypassListitem(this, this.getModel("dashBoardModel"), oContext);
			} else if (oContext.source === "Energy Isolation") {
				oDashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", true); //AN: #ChangeShiftButtonVisibility
				TaskManagementHelper.onPressEnergyIsoListitem(this, this.getModel("dashBoardModel"), oContext);
			}
		},
		onChangeBypassResp: function (oEvent) { //Anand: #handover
			var oDashBoardModel = this.getModel("dashBoardModel");
			oDashBoardModel.setProperty("/bypassObj/selPersonUserId", "");
			oDashBoardModel.setProperty("/bypassObj/selPersonName", "");
			var oComboBox = oEvent.getSource();
			var oContext = oComboBox.getSelectedItem().getBindingContext("dashBoardModel");
			var oPid = oContext.getObject().pId;
			var oName = oContext.getObject().firstName + " " + oContext.getObject().lastName;

			var aUrlForLoginNameFetch = "/taskmanagementRest/bypassLog/getUserLoginByPid?pid=" + oPid;
			this.doAjax(aUrlForLoginNameFetch, "GET", null, function (oData) {
				if (oData.statusCode === "0") {
					if (!oData.userLoginName) {
						sap.m.MessageToast.show("User Name Fetch Failed");
						oComboBox.setSelectedKey("");
						return "";
					}
					oDashBoardModel.setProperty("/bypassObj/selPersonUserId", oData.userLoginName);
					oDashBoardModel.setProperty("/bypassObj/selPersonName", oName);
				} else {
					sap.m.MessageToast.show("User Name Fetch Failed");
					oComboBox.setSelectedKey("");
					return "";
				}
			}.bind(this));
		},
		onSearchActiveBypass: function (oEvent) { //Anand: #handover
			TaskManagementHelper.onSearchActiveBypass(this, this.getModel("dashBoardModel"), oEvent);
		},
		onPressBypassLogButtons: function (oEvent) { //AN: #Notif
			TaskManagementHelper.onPressBypassLogButtons(this, this.getModel("dashBoardModel"), oEvent);
		},
		onPressEnergyIsoLogButtons: function (oEvent) {
			TaskManagementHelper.onPressEnergyIsoLogButtons(this, this.getModel("dashBoardModel"), oEvent);
		},
		onRefreshShiftHandoverData: function (oEvent) { //AN: #loaderWhileNotif
			this.getModel("dashBoardModel").setProperty("/shiftHandover/busyInd", true);
			TaskManagementHelper.fetchAllActiveData(this, this.getModel("dashBoardModel"));
		},
		rightPressAppointment: function (oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext", []);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/oContext", oContext);
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/ChangableStartDateTime", new Date(oContext.startTime));
			// this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/additionalTime", "00:00");
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/additionalTime", "");
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/additionalTimeState", "None");
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/sObj", oEvent.getSource().getBindingContext(
				"dashBoardModel"));
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/sTaskOwner", oEvent.getSource().getTitle());
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/sTaskOwnerName", oEvent.getSource().getText());
			this.getModel("dashBoardModel").setProperty("/schedulingV2Data/currentClickContext/sTaskOwnerPid", oEvent.getSource().getParent()
				.getBindingContext(
					"dashBoardModel").getObject().pId);
			var sStartTime, sEndTime, sCurrentTime;
			sCurrentTime = new Date().getTime();
			sStartTime = oContext.startTime;
			sEndTime =
				oContext.endTime;
			if (oContext.status === "IN PROGRESS") {
				this.getModel("dashBoardModel").setProperty("/schedulingV2Data/isInPRogress", true);
			} else {
				this.getModel("dashBoardModel").setProperty("/schedulingV2Data/isInPRogress", false);
			}
			var oFragmentId = "TaskSchedulingV2SelectPopover",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.TaskSchedulingV2SelectPopover";
			if (!this.taskSchedulingV2Popover) {
				this.taskSchedulingV2Popover = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.taskSchedulingV2Popover);
			}
			this.taskSchedulingV2Popover.openBy(oEvent.getSource().getDomRef());

		},
		//function to apply sorting/grouping to the task items
		onWBViewSettingsConfirm: function (oEvent) {
			WorkbenchHelper.onWBViewSettingsConfirm(this, oEvent);
		},

		//Function to open Sorting/Grouping Dialog
		onViewSettingsPress: function () {
			WorkbenchHelper.onViewSettingsPress(this);
		},
		//Function to search Workbench tasks
		onWBTasksSearch: function (oEvent) {
			WorkbenchHelper.onWBTasksSearch(this, oEvent);
		},

		//Function triggered on Accepting the workbench task
		onAcceptWBTask: function (oEvent) {
			var currObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			WorkbenchHelper.onAcceptWBTask(this, this.getModel("dashBoardModel"), currObj);
		},
		//Function triggered on Rejecting the workbench task
		onRejectWBTask: function (oEvent) {
			var currObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			WorkbenchHelper.onRejectWBTask(this, this.getModel("dashBoardModel"), currObj);
		},
		onPostShiftChangeComment: function (oEvent) {
			TaskManagementHelper.onPostShiftChangeComment(this, this.getModel("dashBoardModel"));
		},
		onWBCustListPress: function (oEvent) {
			WorkbenchHelper.onWBCustListPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBTabSelected: function (oEvent) { //AN: #wbBacklog
			WorkbenchHelper.onWBTabSelected(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWbDraftToggleViewPress: function (oEvent) { //AN: #wbBacklog
			WorkbenchHelper.onWbDraftToggleViewPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWbBacklogToggleViewPress: function (oEvent) { //AN: #wbBacklog
			WorkbenchHelper.onWbBacklogToggleViewPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBDraftRightPress: function (oEvent) {
			WorkbenchHelper.onWBDraftRightPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onAcceptWBTableTask: function (oEvent) { //AN: #wbBacklog
			var currObj = this.getModel("dashBoardModel").getProperty("/InsightToActionData/currContext");
			WorkbenchHelper.onAcceptWBTask(this, this.getModel("dashBoardModel"), currObj);
		},
		onRejectWBTableTask: function (oEvent) { //AN: #wbBacklog
			var currObj = this.getModel("dashBoardModel").getProperty("/InsightToActionData/currContext");
			WorkbenchHelper.onRejectWBTask(this, this.getModel("dashBoardModel"), currObj);
		},
		onViewSettingsBacklogPress: function () { //AN: #wbBacklog
			WorkbenchHelper.onViewSettingsBacklogPress(this);
		},
		//function to apply sorting/grouping to the task items
		onWBViewSettingsBacklogConfirm: function (oEvent) { //AN: #wbBacklog
			WorkbenchHelper.onWBViewSettingsBacklogConfirm(this, oEvent);
		},
		onPressOpAvailVBox: function () { //AN: #OpAvail
			var oDashBoardModel = this.getModel("dashBoardModel");
			oDashBoardModel.setProperty("/selectedTaskManagementVBox", "OpAvail");
			oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", true);
			oDashBoardModel.setProperty("/taskManagementPanel/showTaskManagementPanel", true);
			oDashBoardModel.setProperty("/taskManagementPanel/showOpAvailPanel", true);
			oDashBoardModel.setProperty("/taskManagementPanel/showHandoverNotesPanel", false); //SH: HandoverNotes
			// TaskManagementHelper.onPressOpAvailVBox(this, this.getModel("dashBoardModel"));
			this.fetchOpAvailability();
		},
		onOpAvailPanelExpandPress: function (oEvent) { //AN: #OpAvail
			TaskManagementHelper.onOpAvailPanelExpandPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onRefreshOpAvail: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			this.fetchOpAvailability();
		},
		fetchOpAvailability: function () {
			TaskManagementHelper.fetchOpAvailability(this, this.getModel("dashBoardModel"));
		},
		onOpAvailSort: function (oEvent) {
			TaskManagementHelper.onOpAvailSort(this, this.getModel("dashBoardModel"), oEvent);
		},
		onSearchOpAvail: function (oEvent) {
			TaskManagementHelper.onSearchOpAvail(this, this.getModel("dashBoardModel"), oEvent);
		},
		//SK: Start Date Time Select for Dispatch task(Future task).
		onDateTimeSelectDispatch: function (oEvent) {
			var oData = this.getModel("oTaskPanelDetailModel").getData();
			var dashBoardModel = this.getModel("dashBoardModel");
			if (typeof (oEvent) === "string") {
				dashBoardModel.setProperty("/startDateTimeLablevalue", "");
				dashBoardModel.setProperty("/startDateTimeValueState", "Error");
				dashBoardModel.setProperty("/startDateTimeValueStateText", "Please select the Start Date and Time from Date Time picker");
				dashBoardModel.setProperty("/futureTask", false);
				dashBoardModel.setProperty("/newDispatchDateValue", "");
				oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = "";
				oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = "";
				return;
			} else {
				var sValue = oEvent.getSource().getValue();
				var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
					pattern: "dd-MMM-yy, hh:mm:ss a"
				});
				var sStartDateTimeValue = dateFormat.format(oEvent.getSource().getDateValue());
				var locCode = this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/locationCode");
				var startDate = Math.round(new Date(sValue).getTime() / 1000);
				if (locCode.startsWith("MUR-CA")) {
					this.getUpliftFactor(locCode, startDate); //SH: uplift factor- only for Canada
				}
			}
			if (sValue.length === 22 && (sValue === sStartDateTimeValue)) {
				var currentDate1 = new Date().getTime();
				var newDispatchDateValue = oEvent.getSource().getDateValue();
				var newDispatchTime = oEvent.getSource().getDateValue().getTime();
				if (newDispatchTime > currentDate1) {
					oEvent.getSource().setValueState("None");
					dashBoardModel.setProperty("/startDateTimeValueState", "None");
					if (oData.customAttr) {
						for (var e = 0; e < oData.customAttr.length; e++) {
							if (oData.customAttr[e].label === "Start Date") {
								oData.customAttr[e].labelValue = oEvent.getSource().getValue();
								dashBoardModel.setProperty("/newDispatchDateValue", newDispatchDateValue);
								dashBoardModel.setProperty("/futureTask", true);
							}
						}
					}
				} else {
					dashBoardModel.setProperty("/startDateTimeLablevalue", "");
					dashBoardModel.setProperty("/startDateTimeValueState", "Error");
					dashBoardModel.setProperty("/startDateTimeValueStateText", "Please select the Start Date and Time from Date Time picker");
					oEvent.getSource().setValueStateText("Start Date and Time should be in future");
					dashBoardModel.setProperty("/futureTask", false);
					dashBoardModel.setProperty("/newDispatchDateValue", "");
					oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = "";
					oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = "";
				}
			} else {
				dashBoardModel.setProperty("/startDateTimeLablevalue", "");
				dashBoardModel.setProperty("/futureTask", false);
				dashBoardModel.setProperty("/newDispatchDateValue", "");
				oData.customAttr[dashBoardModel.getProperty("/startDateTime")].labelValue = "";
				oData.customAttr[dashBoardModel.getProperty("/startDateTime")].dateValue = "";
				if (sValue === "") {
					oEvent.getSource().setValueState("None");
					dashBoardModel.setProperty("/startDateTimeValueState", "None");
				} else {
					dashBoardModel.setProperty("/startDateTimeValueState", "Error");
					dashBoardModel.setProperty("/startDateTimeValueStateText", "Please select the Start Date and Time from Date Time picker");
				}
			}
		},
		//SK: #comments Empty Issue Fix temp
		onCommentLiveChnage: function (oEvent) {
			var commentValue = oEvent.getSource().getValue();
			this.getModel("dashBoardModel").setProperty("/newComment", commentValue);
		},
		onEnergyIsolationComment: function (oEvent) {
			TaskManagementHelper.onEnergyIsolationComment(this, this.getModel("dashBoardModel"));
		},
		//SK: Follow up on Resolved tasks.
		onFollowUpNeeded: function (oEvnt) {
			var tOwners = this.getModel("oTaskPanelDetailModel").getData().taskEventDto.owners;
			var oPayload = this.getModel("oTaskPanelDetailModel").getData();
			var LocationCode = oPayload.taskEventDto.locationCode;
			var taskOrgin = oPayload.taskEventDto.origin;
			var taskParentOrgin = oPayload.taskEventDto.parentOrigin; //AN: #msgToROC
			var prevTask = oPayload.taskEventDto.prevTask; //AN: #msgToROC
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			var sUrl = "/taskmanagementRest/tasks/updateTask";
			var dashBoardModel = this.getModel("dashBoardModel");
			dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true); //AN: #inquire
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
					if (oData.statusCode === "0") {
						this.getModel("dashBoardModel").setProperty("/pwHopperUpdate", false);
						this.onCreateTaskPanelClose();
						if (taskParentOrgin === "Message" && prevTask) { //AN: #msgToROC: if message is resolved then move the msg to IN-PROGRESS
							var oUrl = "/taskmanagementRest/message/getMessage?messageId=" + prevTask;
							var oModel = new sap.ui.model.json.JSONModel();
							oModel.loadData(oUrl, null, false);
							var msgResultData = oModel.getData();
							if (msgResultData && msgResultData.responseMessage && msgResultData.responseMessage.statusCode === "0" && msgResultData.message &&
								msgResultData.message.status.toLowerCase() === "resolved") {
								msgResultData.message.status = "IN PROGRESS";
								// WorkbenchHelper.fnWBMsgUpdateStatus(this, this.getModel("dashBoardModel"), "IN PROGRESS", false, msgResultData.message);
								WorkbenchHelper.fnWBMsgUpdateStatus(this, this.getModel("dashBoardModel"), "ASSIGNED", false, msgResultData.message); //SH: NewFlow
							}
						}
						// this.sendNotification("You have a new task assigned", tOwners, true);
						var aOwnersNotif = []; //AN: #shiftExists
						for (var i = 0; i < tOwners.length; i++) { //AN: #shiftExists
							var sEmail = "";
							if (tOwners[i].ownerEmail) {
								sEmail = tOwners[i].ownerEmail;
							} else if (tOwners[i].taskOwner) {
								sEmail = tOwners[i].taskOwner;
							}
							if (sEmail) {
								var oUrl = "/taskmanagementRest/ShiftRegister/getShiftDetails?emp_email=" + sEmail;
								var oModel = new sap.ui.model.json.JSONModel();
								oModel.loadData(oUrl, "", false, "GET", false, false);
								if (oModel.getData()) {
									aOwnersNotif.push(tOwners[i]);
								}
							}
						}
						if (aOwnersNotif && aOwnersNotif.length > 0) {
							this.sendNotification("You have a new task assigned", aOwnersNotif, true);
						}
					} else {
						this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				}.bind(this),
				function (oError) {
					dashBoardModel.setProperty("/busyIndicators/taskPanelBusy", false); //AN: #inquire
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
					this.getModel("dashBoardModel").setProperty("/pwHopperUpdate", false);
					this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		//SK: Frac Pack edit for Eng.
		/*	onFracEditPress: function (oEvent) {
				var oCuurentFarcEditWell = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/currentRightClickContext");
				var aFracData = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/fracData");
				var currentFracId = oCuurentFarcEditWell.fracId;
				var aCurrentFracPackEdit = aFracData.filter(function (e) {
					return e.fracId == currentFracId;
				});
				var oFragmentId = "EditFracPackFragment",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.FracEdit";
				if (!this.EditFracPackFragment) {
					this.EditFracPackFragment = this._createFragment(oFragmentId, oFragmentName);
					this.getView().addDependent(this.EditFracPackFragment);
				}
				this.EditFracPackFragment.open();
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/isEditing", true);
				for (var i = 0; i < aCurrentFracPackEdit.length; i++) {
					aCurrentFracPackEdit[i].estBolDate = new Date(aCurrentFracPackEdit[i].estBolDate);
					aCurrentFracPackEdit[i].fracDate = new Date(aCurrentFracPackEdit[i].fracDate);
					aCurrentFracPackEdit[i].isCheckbox = false;
				}
				var oEditFracPAck = {
					"description": oCuurentFarcEditWell.description,
					"fracPackWells": aCurrentFracPackEdit
				};
				this.getView().getModel("dashBoardModel").setProperty("/fracMonitoringData/editFrac", oEditFracPAck);
				this.getView().getModel("dashBoardModel").setProperty("/fracMonitoringData/editFracDeleteEnabled", false);
			},*/
		//SK: Frac Pack update for Eng.
		/*	onFracUpdatePress: function (oEvent) {
				var oEditFrac = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/editFrac");
				var check;
				var oLoggedInUserId = this.getModel("dashBoardModel").getProperty("/userData/userId");
				var oRoleData = this.getModel("dashBoardModel").getProperty("/userData/sGroup");
				if (oEditFrac.description === null || oEditFrac.description === "") {
					this._showToastMessage("Please enter a Frac Description");
					return;
				}
				var a = oEditFrac.fracPackWells;
				var aPayload = [];
				for (var i = 0; i < a.length; i++) {
					if (!a[i].scenario || !a[i].fracDate || !a[i].estBolDate || !a[i].maxTubePressure || !a[i].maxCasePressure || !a[i].distFrac || !
						a[i].orientation || !a[i].zone) {
						check = true;
						break;
					} else {
						check = false;
						var ofracPack = a[i];
						var oPayload = {
							"fracId": ofracPack.fracId,
							//	"fieldCode": ofracPack.funcLocation,
							"wellCode": ofracPack.wellCode,
							"wellName": ofracPack.wellName,
							//	"startAt": ofracPack.startAt,
							"estBolDate": ofracPack.estBolDate.getTime(),
							"scenario": ofracPack.scenario,
							//	"prodImpact": ofracPack.prodImpact,
							"maxTubePressure": parseFloat(ofracPack.maxTubePressure),
							"maxCasePressure": parseFloat(ofracPack.maxCasePressure),
							"boed": ofracPack.boed,
							"distFrac": parseFloat(ofracPack.distFrac),
							"orientation": ofracPack.orientation,
							"zone": ofracPack.zone,
							"description": oEditFrac.description,
							"wellStatus": ofracPack.wellStatus
						};
						if (ofracPack.fracStatus) {
							oPayload.fracStatus = ofracPack.fracStatus;
						} else {
							oPayload.fracStatus = "IN PROGRESS";
						}
						if (ofracPack.locationCode) {
							delete oPayload['locationCode'];
						}
						if (ofracPack.funcLocation) {
							oPayload.fieldCode = ofracPack.funcLocation;
						} else {
							oPayload.fieldCode = ofracPack.fieldCode;
						}
						if (ofracPack.userId) {
							oPayload.userId = ofracPack.userId;
						} else {
							oPayload.userId = oLoggedInUserId;
						}
						if (ofracPack.userRole) {
							oPayload.userRole = ofracPack.userRole;
						} else {
							oPayload.userRole = oRoleData;
						}
						if (ofracPack.startAt) {
							oPayload.startAt = ofracPack.startAt;
						} else {
							oPayload.startAt = ofracPack.fracDate.getTime();
						}
						if (ofracPack.action) {
							oPayload.action = ofracPack.action;
						} else {
							oPayload.action = "update";
						}
						aPayload.push(oPayload);
					}
				}
				if (check) {
					this._showToastMessage("Please fill all the mandatory fields in the table");
					return;
				}
				var sUrl = "/taskmanagementRest/fracPack/updateFracPack";
				this.doAjax(sUrl, "POST", aPayload, function (oData) {
						if (oData.statusCode === "0") {
							this._showToastMessage(oData.message);
							this.getModel("dashBoardModel").setProperty("/fracMonitoringData/selectedWells", null);
							this.getModel("dashBoardModel").setProperty("/fracMonitoringData/fracDescription", null);
							if (this.EditFracPackFragment) {
								this.EditFracPackFragment.close();
								this.getModel("dashBoardModel").setProperty("/fracMonitoringData/isEditing", false);
							}
							this.getFracListDetails();
						} else {
							this._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
						}
					}.bind(this),
					function (oError) {
						this._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			},*/
		/*	onFracEditCancelPress: function (oEvent) {
				if (this.EditFracPackFragment) {
					this.EditFracPackFragment.close();
					this.getModel("dashBoardModel").setProperty("/fracMonitoringData/isEditing", false);
				}
			},*/
		/*	onFracEditItemDeletePress: function (oEvent) {
				var aSelectedObject = [];
				var aFracPackWells = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/editFrac/fracPackWells");
				//	var aSelectedContext = this.EditFracPackFragment.getContent()[0].getSelectedContexts();
				for (var k = 0; k < aFracPackWells.length; k++) {
					if (aFracPackWells[k].isSelected) {
						aSelectedObject.push(aFracPackWells[k]);
					}
				}
				var aFracPack = this.getModel("dashBoardModel").getData().fracMonitoringData.editFrac.fracPackWells;
				var aNewFracPack = [];
				var aSelectedFracWellName = [];
				for (var i = 0; i < aSelectedObject.length; i++) {
					aSelectedFracWellName.push(aSelectedObject[i].wellName);
				}
				for (var j = 0; j < aFracPack.length; j++) {
					if (!aSelectedFracWellName.includes(aFracPack[j].wellName)) {
						aNewFracPack.push(aFracPack[j]);
					}
				}
				this.EditFracPackFragment.getContent()[0].removeSelections();
				this.getModel("dashBoardModel").setProperty("/fracMonitoringData/editFrac/fracPackWells", aNewFracPack);

			},*/
		/*onFracEditAddWellSelect: function (oEvent) {
			var oSelectedObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oEvent.getSource().getSelected()) {
				oSelectedObject.isSelected = true;
			} else {
				oSelectedObject.isSelected = false;
			}
		},*/
		//SK: Prove Screen Pagination.
		onProveLeftPage: function (oEvent) { //AN: #provePerformance
			var sCurrentPageNumber = this.getModel("dashBoardModel").getProperty("/spotFireData/pageNumber");
			var PreviousPageNumber = parseInt(sCurrentPageNumber) - 1;
			var sPreviousPageNumber = PreviousPageNumber + "";
			this.getModel("dashBoardModel").setProperty("/spotFireData/pageNumber", sPreviousPageNumber);
			this.getProveReport();
		},
		onProveRightPage: function (oEvent) { //AN: #provePerformance
			var sCurrentPageNumber = this.getModel("dashBoardModel").getProperty("/spotFireData/pageNumber");
			var NextPageNumber = parseInt(sCurrentPageNumber) + 1;
			var sNextPageNumber = NextPageNumber + "";
			this.getModel("dashBoardModel").setProperty("/spotFireData/pageNumber", sNextPageNumber);
			this.getProveReport();
		},
		afterPlotlyPopupOpen: function (oEvent) {
			var payload = this.createPayload(3);
			this.getModel("dashBoardModel").setProperty("/fracHitPressed", false);
			// this.getModel("dashBoardModel").setProperty("/plotlyDaysFilter", "days");
			this.onPlotlyFilterChange();
			this.loadGraph(payload);
		},
		createPayload: function (dur, startDate, endDate, flag) {
			var country;
			if (this.getModel("dashBoardModel").getProperty("/currentLocationInHierarchy") === "EFS") {
				country = "US";
			} else {
				country = "CA";
			}
			var muwiId, type, taskMode, wellName, reportId, duration;
			var dashBoardModel = this.getModel("dashBoardModel");
			/*	if (this.getModel("dashBoardModel").getProperty("/isTaskCardPress")) {
					muwiId = this.getModel("dashBoardModel").getProperty("/taskCardData/muwiId");
					// type = "INVESTIGATION7";
				} else if (this.getModel("dashBoardModel").getProperty("/isIconPress")) {
					muwiId = this.getModel("dashBoardModel").getProperty("/iconPressData/muwi");
					// type = "INVESTIGATION7";
				} else {*/
			if (this.getModel("dashBoardModel").getProperty("/isIconPress")) {
				this.getModel("dashBoardModel").setProperty("/plotlyIsAlarm", false);
				muwiId = dashBoardModel.getProperty("/pwHopperObjForPlotly").muwi;
				wellName = dashBoardModel.getProperty("/pwHopperObjForPlotly/location");
				reportId = "PR01";
				if (flag) {
					duration = dur;
				} else {
					if (this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskMode") !== undefined) {
						duration = Number(this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskMode"));
					} else {
						duration = Number(dashBoardModel.getProperty("/spotFireData/durationKey"));
					}
				}
				this.getModel("dashBoardModel").setProperty("/currPlotlyMod", "Prove");
				this.getModel("dashBoardModel").setProperty("/isProve", true);
				this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
			} else {
				if (dashBoardModel.getProperty("/currAlarmObject")) {
					this.getModel("dashBoardModel").setProperty("/plotlyIsAlarm", true);
					muwiId = dashBoardModel.getProperty("/currAlarmObject").muwi;
					// var facilityDesc = this.getModel("dashBoardModel").getProperty("/currAlarmObject/facDescription");
					wellName = this.getModel("dashBoardModel").getProperty("/currAlarmObject/facDescription");
					reportId = "AL01"
					duration = dur;
					this.getModel("dashBoardModel").setProperty("/currPlotlyMod", "Alarm");
					this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
					// type = "ALARM";
				}
				if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "ndtpv") {
					this.getModel("dashBoardModel").setProperty("/plotlyIsAlarm", false);
					muwiId = dashBoardModel.getProperty("/currentProveObject/muwiId"); //AN: #inquire
					wellName = this.getModel("dashBoardModel").getProperty("/currentProveObject/well");
					reportId = "PR01";
					if (flag) {
						duration = dur;
					} else {
						duration = Number(dashBoardModel.getProperty("/spotFireData/durationKey"));
					}
					this.getModel("dashBoardModel").setProperty("/currPlotlyMod", "Prove");
					this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
					// taskMode = dashBoardModel.getProperty("/spotFireData/durationKey"); //AN: #inquire
					// type = taskMode === "7" ? "INVESTIGATION7" : "INVESTIGATION30";
				} else if (this.getModel("dashBoardModel").getProperty("/currDopObject") && this.getModel("dashBoardModel").getProperty(
						"/panelProperties/currentSelectKey") === "DOP") {
					this.getModel("dashBoardModel").setProperty("/plotlyIsAlarm", false);
					muwiId = this.getModel("dashBoardModel").getProperty("/currDopObject").muwi;
					wellName = this.getModel("dashBoardModel").getProperty("/currDopObject/location");
					var selDOPTab = this.getModel("dashBoardModel").getProperty("/dopData/selectedKey");
					if (selDOPTab === "dop") {
						reportId = "DP01";
					} else {
						reportId = "DG01";
					}
					duration = dur;
					this.getModel("dashBoardModel").setProperty("/currPlotlyMod", "DOP");
					this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
					// type = "VARIENCE";
					// taskMode = dashBoardModel.getProperty("/dopData/durationKey");
				}
				if (dashBoardModel.getProperty("/isFracMonitoring")) {
					this.getModel("dashBoardModel").setProperty("/plotlyIsAlarm", false);
					var oObj = this.getModel("dashBoardModel").getProperty("/fracMonitoringData/currentRightClickContext");
					muwiId = oObj.wellCode;
					wellName = oObj.wellName;
					reportId = "FH01"
					duration = dur;
					this.getModel("dashBoardModel").setProperty("/currPlotlyMod", "Frac Monitoring");
					this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
					// type = "FRAC";
				}
			}
			/*if (dashBoardModel.getProperty("/panelProperties/currentSelectKey") === "pwhopper") {
				this.getModel("dashBoardModel").setProperty("/plotlyIsAlarm", false);
				muwiId = dashBoardModel.getProperty("/pwHopperObjForPlotly").muwi;
				wellName = dashBoardModel.getProperty("/pwHopperObjForPlotly/location")
				reportId = "PR01";
				if (flag) {
					duration = dur;
				} else {
					if (this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskMode") !== undefined) {
						duration = Number(this.getModel("oTaskPanelDetailModel").getProperty("/taskEventDto/taskMode"));
					} else {
						duration = Number(dashBoardModel.getProperty("/spotFireData/durationKey"));
					}
				}
				this.getModel("dashBoardModel").setProperty("/currPlotlyMod", "Prove");
				this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
			}*/
			if (startDate && endDate) {
				var oPayload = {
					"countryCode": country,
					"duration": "",
					"muwi": muwiId,
					"reportId": reportId,
					"wellName": wellName,
					"startDate": startDate,
					"endDate": endDate
				};
			} else {
				var oPayload = {
					"countryCode": country,
					"duration": duration,
					"muwi": muwiId,
					"reportId": reportId,
					"wellName": wellName,
					"startDate": "",
					"endDate": ""
				};
			}
			return oPayload;
			// }

		},
		plotlyHamPress: function (oEvent) {
			var oButton = oEvent.getSource();
			// create menu only once
			if (!this._menu) {
				this._menu = sap.ui.xmlfragment(
					"com.sap.incture.Incture_IOP.fragment.ChartOptionsMenu",
					this
				);
				this.getView().addDependent(this._menu);
			}
			var eDock = sap.ui.core.Popup.Dock;
			this._menu.open(this._bKeyboard, oButton, eDock.EndTop, eDock.EndBottom, oButton);
		},
		loadGraph: function (oPayload) {
			var plotId = "idPlotlyDiv1";
			var sUrl = "/taskmanagementRest/plotly/fetchData";
			this.busyDialogForPlotly = new sap.m.BusyDialog();
			this.busyDialogForPlotly.open();
			this.doAjax(sUrl, "POST", oPayload, function (oSuccess) {
					var refreshedDate = sap.ui.core.format.DateFormat.getDateTimeInstance({
						pattern: "MMM dd, yyyy hh:mm:ss aaa"
					}).format(new Date());
					this.getView().getModel("dashBoardModel").setProperty("/plotlyRefDate", refreshedDate);
					if (oSuccess.output && oSuccess.statusCode !== 500) {
						this.getModel("dashBoardModel").setProperty("/plotlyGraphData", oSuccess.output);
						var oData = [];
						if (this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey") === "DOP" && !this.getModel(
								"dashBoardModel").getProperty("/isIconPress")) {
							oData = this.formatDopResponse(oSuccess.output, oData);
						} else {
							oData = this.FormatResponse(oSuccess.output, oData);
						}
						this.getModel("dashBoardModel").setProperty("/plotlyFormatedGraphData", oData);
						if (oData.length) {
							var Errortext = [];
							var noDataForGraph = false;
							if (oSuccess.output.message) {
								if (oSuccess.output.message.includes("No data available in Canary for") || oSuccess.output.message.includes(
										"Tag not available in Canary")) {
									noDataForGraph = true;
									Errortext.push(oSuccess.output.wellName + " - " + oSuccess.output.message);
								}
							}
							this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyText", Errortext);
							if (noDataForGraph === true) {
								this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyTextVisibility", true);
							} else {
								this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyTextVisibility", false);
							}
							if (oSuccess.output.trendsDtoList) {
								this.getView().getModel("dashBoardModel").setProperty("/plotlyTableData", oSuccess.output.trendsDtoList);
							} else if (oSuccess.output.fracHitTimeList) {
								var arr = [];
								for (var i in oSuccess.output.fracHitTimeList) {
									arr.push({
										"fracHitTime": oSuccess.output.fracHitTimeList[i]
									});
								}
								this.getView().getModel("dashBoardModel").setProperty("/plotlyTableData", arr);
							}
							this.ClearGraphData(plotId);
							this.PlotGraphWithID(plotId, oData);
						} else {
							this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyTextVisibility", true);
							this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyText", "No Data available for Canary Tags");
							this.PlotGraphWithID(plotId, oData);
						}
					} else {
						this.busyDialogForPlotly.close();
						this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyTextVisibility", true);
						this.getView().getModel("dashBoardModel").setProperty("/noDataPlotlyText", "No Data available for Canary Tags");
					}
				}.bind(this),
				function (oError) {
					this.busyDialogForPlotly.close();
				}.bind(this));
		},
		FormatResponse: function (oSuccess, oData) {
			this.axisName = {};
			this.axisRange = {};
			var i = oSuccess;
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "yyyy-MM-dd HH:mm:ss"
			});
			var maxY = 0,
				minY = 0;
			// for (var i of oSuccess) {
			for (var j of Object.keys(i.values)) {
				i.values[j].y.forEach(element => {
					if (element > maxY)
						maxY = element;
					if (element < minY)
						minY = element;
				});
				if (this.axisName[i.values[j].axis] && this.axisName[i.values[j].axis].indexOf(i.values[j].displayName) === -1)
					this.axisName[i.values[j].axis] += ", " + i.values[j].displayName;
				else
					this.axisName[i.values[j].axis] = i.values[j].displayName;

				if (i.values[j].ymin !== "" && i.values[j].ymax !== "")
					this.axisRange[i.values[j].axis] = [+i.values[j].ymin, +i.values[j].ymax];

				// var x = i.values[j].x_epoch; //string dates used

				//convert epoch dates to system date
				var convEpoch = i.values[j].epochSecond.map(epoch => oDateFormat.format(new Date(epoch * 1000))); // receiving ZonedDateTime so multiply by 1000 to convert to millisec
				var x = convEpoch;
				var sDisplayName = i.values[j].displayName;

				oData.push(this._createData(
					sDisplayName,
					i.values[j].axis,
					x,
					i.values[j].y,
					i.values[j].displayName,
					"plotId",
					i.values[j].x_min,
					i.values[j].y
				));
			}
			// }
			this.getView().getModel("dashBoardModel").setProperty("/minY", minY);
			this.getView().getModel("dashBoardModel").setProperty("/maxY", maxY);
			return oData;
		},
		_createData: function (name, axis, dataX, dataY, groupBy, sSelTab, xminData, yminData) {
			var x = [],
				y = [];
			var xAndY = [];
			if (xminData && yminData) {
				xAndY.push({
					"xData": xminData
				});
				xAndY.push({
					"yData": yminData
				});
			}
			if (dataX && dataY) {
				x = dataX;
				y = dataY;
			}
			var oData = {
				x: x,
				y: y,
				name: name,
				type: 'scatter',
				mode: 'lines',
				opacity: 0.7,
				hoverinfo: 'text+name',
				hoverlabel: {
					namelength: -1
				},
				customData: xAndY,
				hovertemplate: '<b>Value</b>: %{text:.2f}' +
					'<br><b>X</b>: %{x}',
				/*George - 03/18/2020 - Show complete test on hover*/
				text: y,
			};
			if (x[0] === 0) {
				oData.hovertemplate = "<b>Value</b>: %{text:.2f}" +
					"<br><b>X</b>: %{x:.2f}";
			}

			if (axis) {
				oData['yaxis'] = axis;
			}
			if (groupBy) {
				oData['legendgroup'] = groupBy;
			}
			return oData;
		},
		getLayout: function (bHideLeg, bWholeX, binterval, sBarMode, aRange) {
			var xTitile = "Elapsed time";
			this.axisName = this.removeUnit(this.axisName);
			var fontSize = 11;
			if (!this.axisRange) {
				this.axisRange = {};
			}
			var oLay = {
				uirevision: true,
				barmode: 'stack',
				showlegend: !bHideLeg,
				autosize: true,
				useResizeHandler: true,
				margin: {
					t: 30,
					l: 100,
					r: 20,
					b: 20
				},
				legend: {
					yanchor: 'center',
					x: 1,
					y: 0.5,
					orientation: 'v',
					font: {
						size: 12,
						color: '#878787'
					},
				},
				paper_bgcolor: "rgba(255,255,255,1)",
				plot_bgcolor: "rgba(255,255,255,1)",
				font: {
					family: 'Arial,Helvetica,sans-serif'
				},
				xaxis: {
					rangemode: 'tozero',
					automargin: true,
					showgrid: true,
					zeroline: true,
					zerolinecolor: 'rgb(233,233,233)',
					titlefont: {
						color: '#878787',

					},
					tickfont: {
						color: '#a7a7a7'
					},
					linecolor: "#a7a7a7",
					title: {
						text: xTitile,
						font: {
							size: fontSize,
							color: "#878787"
						},
					},
					domain: !bHideLeg ? [0.1, 0.95] : [0.05, 0.95],
					range: aRange ? aRange : [],
					autorange: aRange ? false : true,
					showline: true,
					linewidth: 0.5,
					tickformat: !bWholeX ? '' : ',d',
					dtick: null,
					tickmode: "auto",
					tick0: null,
				},
				yaxis: {
					showgrid: true,
					automargin: true,
					zeroline: true,
					zerolinecolor: 'rgb(233,233,233)',
					rangemode: 'tozero',
					// range: this.axisRange['y'],
					// autorange: this.axisName['y'] ? false : true,
					title: {
						text: this.axisName['y'] || '',
						font: {
							size: fontSize,
							color: "#878787"
						},
					},
					titlefont: {
						color: '#878787'
					},
					tickfont: {
						color: '#a7a7a7'
					},
					linecolor: "#a7a7a7",
					// linecolor: "#FF0000",
					showline: true,
					linewidth: 0.5,
					domain: [0.1, 1]
				},
				yaxis2: {
					showgrid: false,
					zeroline: true,
					zerolinecolor: 'rgb(233,233,233)',
					rangemode: 'tozero',
					// range: this.axisRange['y2'],
					// autorange: this.axisName['y2'] ? false : true,
					title: {
						text: this.axisName['y2'] || '',
						font: {
							size: fontSize,
							color: "#878787"
						},
					},
					font: {
						size: fontSize,
						color: "#878787"
					},
					titlefont: {
						color: '#878787'
					},
					tickfont: {
						color: '#a7a7a7'
					},
					linecolor: "#a7a7a7",
					// linecolor:"#FFA500",
					anchor: 'free',
					overlaying: 'y',
					side: 'left',
					position: -0.25,
					showline: true,
					linewidth: 0.5,
				},
				yaxis3: {
					showgrid: false,
					zeroline: true,
					zerolinecolor: 'rgb(233,233,233)',
					rangemode: 'tozero',
					// range: this.axisRange['y3'],
					// autorange: this.axisName['y3'] ? false : true,
					title: {
						text: this.axisName['y3'] || '',
						font: {
							size: fontSize,
							color: "#878787"
						},
					},
					font: {
						size: fontSize
					},
					titlefont: {
						color: '#878787'
					},
					tickfont: {
						color: '#a7a7a7'
					},
					linecolor: "#a7a7a7",
					// linecolor:"#8DB600",
					anchor: 'x',
					overlaying: 'y',
					side: 'right',
					layer: "below traces"
				},
				yaxis4: {
					showgrid: false,
					zeroline: true,
					zerolinecolor: 'rgb(233,233,233)',
					// rangemode: 'tozero',
					title: {
						text: this.axisName['y4'] || '',
						font: {
							size: fontSize,
							color: "#878787"
						},
					},
					font: {
						size: fontSize,
						color: "#878787"
					},
					titlefont: {
						color: '#878787'
					},
					tickfont: {
						color: '#a7a7a7'
					},
					linecolor: "#a7a7a7",
					anchor: 'free',
					side: 'right',
					showline: true,
					linewidth: 0.5,
					position: 1,
					showline: true,
				},
				yaxis5: {
					showgrid: false,
					zeroline: true,
					zerolinecolor: 'rgb(233,233,233)',
					// rangemode: 'tozero',
					title: {
						text: this.axisName['y5'] || '',
						font: {
							size: fontSize,
							color: "#878787"
						},
					},
					font: {
						size: fontSize
					},
					titlefont: {
						color: '#878787'
					},
					tickfont: {
						color: '#a7a7a7'
					},
					linecolor: "#a7a7a7",
					anchor: 'free',
					overlaying: 'y',
					side: 'right',
					showline: true,
					linewidth: 0.5,
					position: 1.50,
					showline: true,
				}
			};

			/*if (this.getModel("dashBoardModel").getProperty("/isFracMonitoring")) {
				var fracHits = this.getModel("dashBoardModel").getProperty("/plotlyTableData");
				var shapes = [];
				var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
					pattern: "yyyy-MM-dd hh:mm:ss"
				});
				for (var i in fracHits) {
					shapes.push({
						editable: false,
						type: 'line',
						x0: oDateFormat.format(new Date(fracHits[i].fracHitTime)),
						x1: oDateFormat.format(new Date(fracHits[i].fracHitTime)),
						y0: 0,
						line: {
							color: 'rgb(255, 0, 0)',
							width: 3,
							dash: 'dot'
						}
					});
				};
				oLay.shapes = shapes;
			}*/
			return oLay;
		},
		getConfig: function (bHideLeg, bnoZoom, bHideModeBar) {
			var that = this;
			that.getView().getModel("dashBoardModel").setProperty("/bShowLegends", false);
			return {
				displaylogo: false,
				scrollZoom: !bnoZoom,
				responsive: true,
				modeBarButtonsToRemove: ['zoomIn2d', 'zoomOut2d', 'lasso2d', 'select2d', 'autoScale2d', 'tableRotation',
					'resetCameraLastSave3d', 'hoverClosest3d', 'toImage', 'zoom3d'
				],
				// orbitRotation
				modeBarButtonsToAdd: bHideLeg ? [] : [
					[{
						name: 'Toggle Legends',
						icon: {
							name: 'Legends Show Hide',
							svg: '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 11.36"><defs><style>.cls-1{fill:#346187;}</style></defs><title/><g data-name="Layer 2" id="Layer_2"><g data-name="Layer 1" id="Layer_1-2"><path class="cls-1" d="M10,1a9.37,9.37,0,0,1,6.67,2.76l1.92,1.92L16.67,7.6a9.43,9.43,0,0,1-13.34,0L1.41,5.68,3.33,3.76A9.37,9.37,0,0,1,10,1m0-1A10.4,10.4,0,0,0,2.63,3.05L0,5.68,2.63,8.3a10.43,10.43,0,0,0,14.75,0L20,5.68,17.38,3.05A10.4,10.4,0,0,0,10,0Z"/><path class="cls-1" d="M10,2.68a3,3,0,0,0-.87.14,1.5,1.5,0,1,1-2,2A3,3,0,0,0,7,5.68a3,3,0,1,0,3-3Z"/></g></g></svg>'
						},
						toggle: true,
						click: function (gd, oEvent) {
							that.getView().getModel("dashBoardModel").setProperty("/bShowLegends", !gd.layout.showlegend);
							gd.layout.showlegend = !gd.layout.showlegend;
							Plotly.restyle(gd, {
								showlegend: gd.layout.showlegend
							})
						}
					}]
				],
				displayModeBar: !bHideModeBar
			};
		},
		removeUnit: function (axisnames) {
			var keys = Object.keys(axisnames);
			for (const key of keys) {
				if (axisnames[key].indexOf(",") !== -1) {
					if (axisnames[key].split(",").length === 3) {
						if (axisnames[key].split(",")[0].match(/\(([^)]+)\)/) !== null) {
							if ((axisnames[key].split(",")[0].match(/\(([^)]+)\)/)[1] === axisnames[key].split(",")[1].match(/\(([^)]+)\)/)[1]) && (
									axisnames[key].split(",")[1].match(/\(([^)]+)\)/)[1] === axisnames[
										key].split(",")[2].match(/\(([^)]+)\)/)[1])) {
								var unit = axisnames[key].match(/\(([^)]+)\)/)[1];
								axisnames[key] = axisnames[key].replace(/ *\([^)]*\) */g, "");
								if (axisnames[key].indexOf("Blender") !== -1) {
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
								}
								if (axisnames[key].indexOf("Conc") !== -1) {
									var strinArr = axisnames[key].split("Conc");
									axisnames[key] = strinArr[0].trim() + strinArr[1].trim() + strinArr[2].trim() + " Conc";
								}
								if (axisnames[key].indexOf("Pressure") !== -1) {
									var strinArr = axisnames[key].split("Pressure");
									axisnames[key] = strinArr[0].trim() + strinArr[1].trim() + strinArr[2].trim() + " Pressure";
								}
								if (axisnames[key].indexOf("Vol") !== -1) {
									var strinArr = axisnames[key].split("Vol");
									axisnames[key] = strinArr[0].trim() + strinArr[1].trim() + strinArr[2].trim() + " Vol";
								}
								axisnames[key] = axisnames[key] + " (" + unit + ")";
							} else {
								if (axisnames[key].indexOf("Blender") !== -1) {
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
								}
							}
						}
					}
					if (axisnames[key].split(",").length === 2) {
						if (axisnames[key].split(",")[0].match(/\(([^)]+)\)/) !== null) {
							if (axisnames[key].split(",")[0].match(/\(([^)]+)\)/)[1] === axisnames[key].split(",")[1].match(/\(([^)]+)\)/)[1]) {
								var unit = axisnames[key].match(/\(([^)]+)\)/)[1];
								axisnames[key] = axisnames[key].replace(/ *\([^)]*\) */g, "");
								if (axisnames[key].indexOf("Blender") !== -1) {
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
								}
								if (axisnames[key].indexOf("Conc") !== -1) {
									var strinArr = axisnames[key].split("Conc");
									axisnames[key] = strinArr[0].trim() + strinArr[1].trim() + " Conc";
								}
								if (axisnames[key].indexOf("Pressure") !== -1) {
									var strinArr = axisnames[key].split("Pressure");
									axisnames[key] = strinArr[0].trim() + strinArr[1].trim() + " Pressure";
								}
								if (axisnames[key].indexOf("Vol") !== -1) {
									var strinArr = axisnames[key].split("Vol");
									axisnames[key] = strinArr[0].trim() + strinArr[1].trim() + " Vol";
								}
								axisnames[key] = axisnames[key] + " (" + unit + ")";
							} else {
								if (axisnames[key].indexOf("Blender") !== -1) {
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
									axisnames[key] = axisnames[key].replace("Blender", "Bld");
								}
							}
						}
					}
				}
			}
			return axisnames;
		},
		PlotGraphWithID: function (sId, oData, bOffset, aRange) {
			Plotly.react(sId, oData, this.getLayout(false, null, null, null, aRange), this.getConfig(true, null, true)).then(success => {
				this.relayoutScalePlot(sId);
				this.busyDialogForPlotly.close();
			});
			this.busyDialogForPlotly.close();
		},
		relayoutScalePlot: function (id) {
			setTimeout(() => {
				try {
					Plotly.relayout(id, {
						'xaxis.autorange': true
					});
				} catch (e) {

				}
			}, 250);
		},
		ClearGraphData: function (sId) {
			if (document.getElementById(sId) !== null)
				Plotly.react(sId, []);
		},
		handleChartToolCapturePress: function (oEvent) {
			var sId = "idPlotlyDiv1";
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "MMM dd yyyy"
			});
			var module = this.getModel("dashBoardModel").getProperty("/currPlotlyMod");
			var startDate = oDateFormat.format(this.getModel("dashBoardModel").getProperty("/plotlyStartDate"));
			var endDate = oDateFormat.format(this.getModel("dashBoardModel").getProperty("/plotlyEndDate"));
			var wellName = this.getModel("dashBoardModel").getProperty("/plotlyGraphData");
			Plotly.downloadImage(sId, {
				format: 'png',
				width: 1400,
				height: 600,
				filename: module + "_Trends_" + wellName + "_" + startDate + "-" + endDate
			});
		},
		handleChartToolClosestPress: function (oEvent, bCompare, bOffset) {
			var sCloset = "closest";
			if (bCompare)
				sCloset = "x";
			var sId = "#idPlotlyDiv1";
			var oLayout = $(sId)[0].layout;
			oLayout.hovermode = sCloset;
			Plotly.animate(sId.replace("#", ""), {
				layout: oLayout
			}, {
				transition: {
					duration: 500,
					easing: 'cubic-in-out'
				},
				frame: {
					duration: 500
				}
			});
		},
		handleChartToolLegendsPress: function (oEvent, bOffset) {
			var sId = "#idPlotlyDiv1";
			var oLayout = $(sId)[0].layout;
			oLayout.showlegend = !oLayout.showlegend;
			Plotly.restyle(sId.replace("#", ""), {
				showlegend: oLayout.showlegend
			});

		},
		onPlotlyDurationBtnPress: function (oEvent) {
			var duration = parseInt(oEvent.getSource().getText().split(" ")[0]);
			this.getModel("dashBoardModel").setProperty("/plotlyDuration", duration);
			this.getModel("dashBoardModel").setProperty("/plotlyDaysFilter", "days");
			this.onPlotlyFilterChange();
			var payload = this.createPayload(duration, null, null, true);
			this.loadGraph(payload);
		},
		onPlotlyFilterChange: function (oEvent) {
			var duration = this.getModel("dashBoardModel").getProperty("/plotlyDuration");

			if (!oEvent) {
				var endDate = new Date();
				var endDateEpoch = endDate.getTime();
				var startDate = new Date(endDateEpoch - duration * 86400000);
				var maxStartDate = new Date(endDateEpoch - 30 * 86400000);
				this.getModel("dashBoardModel").setProperty("/plotlyStartDate", startDate);
				this.getModel("dashBoardModel").setProperty("/plotlyEndDate", endDate);
				this.getModel("dashBoardModel").setProperty("/plotlyMaxStartDate", maxStartDate);
			}

			if (oEvent) {
				var endDate = this.getModel("dashBoardModel").getProperty("/plotlyEndDate");
				var endDateEpoch = endDate.getTime();
				var startDate = this.getModel("dashBoardModel").getProperty("/plotlyStartDate");
				var startDateEpoch = startDate.getTime();

				var payload = this.createPayload(null, startDateEpoch, endDateEpoch);
				this.loadGraph(payload);
			}

		},
		onToggleFracHit: function () {
			var fracFlag = this.getModel("dashBoardModel").getProperty("/fracHitPressed");
			var fracHits = this.getModel("dashBoardModel").getProperty("/plotlyTableData");
			var fracs = [];
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "yyyy-MM-dd HH:mm:ss"
			});
			var sId = "#idPlotlyDiv1";
			if (fracFlag && fracHits !== undefined) {
				if ($(sId)[0].layout["shapes"] == null || $(sId)[0].layout["shapes"] == undefined) {
					fracs = [];
				} else {
					fracs = $(sId)[0].layout["shapes"];
				}
				for (var i in fracHits) {
					fracs.push({
						editable: false,
						type: 'line',
						x0: oDateFormat.format(new Date(fracHits[i].fracHitTime)),
						x1: oDateFormat.format(new Date(fracHits[i].fracHitTime)),
						y0: this.getView().getModel("dashBoardModel").getProperty("/minY"),
						y1: this.getView().getModel("dashBoardModel").getProperty("/maxY"),
						line: {
							color: 'rgb(0, 0, 0)',
							width: 3,
							dash: 'dot'
						}
					});
				};
			} else {
				fracs = [];
			}
			var oLayout = $(sId)[0].layout;
			oLayout["shapes"] = fracs;
			Plotly.animate(sId.replace("#", ""), {
				layout: oLayout
			}, {
				transition: {
					duration: 500,
					easing: 'cubic-in-out'
				},
				frame: {
					duration: 500
				}
			});
		},
		formatDopResponse: function (oSuccess, oData) {
			this.axisName = {
				y: "Actual(Canary), Forecast(Enersight), Projected(HANA)"
			};
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "yyyy-MM-dd HH:mm:ss"
			});

			if (oSuccess.actual.epochSecond) {
				if (oSuccess.actual.epochSecond.length) {
					var xEpoch = oSuccess.actual.epochSecond.map(epoch => oDateFormat.format(new Date(epoch * 1000)));
				} else {
					var xEpoch = [oDateFormat.format(new Date(oSuccess.actual.epochSecond * 1000))];
				}

				var data = {
					x: xEpoch,
					y: oSuccess.actual.y,
					name: "Actual(Canary)",
					type: 'scatter',
					mode: 'lines',
					line: {
						// color: 'rgb(0, 0, 0)',
						width: 3
					},
					opacity: 0.7,
					hoverinfo: 'text+name',
					hoverlabel: {
						namelength: -1
					},
					customData: [],
					hovertemplate: '<b>Value</b>: %{y:.2f}' + //%{text:.2f}  
						'<br><b>X</b>: %{x}',
					text: oSuccess.actual.y,
					yaxis: "y",
					legendgroup: "Actual"
				};
				if (xEpoch[0] === 0 || xEpoch[0] === undefined) {
					data.hovertemplate = "<b>Value</b>: %{y:.2f}" +
						"<br><b>X</b>: %{x:.2f}";
				}
				oData.push(data);
			}
			if (oSuccess.forecast.epochSecond) {
				var data = {
					x: oSuccess.forecast.epochSecond.map(epoch => oDateFormat.format(new Date(epoch * 1000))),
					y: oSuccess.forecast.y,
					name: "Forecast(Enersight)",
					mode: 'lines',
					opacity: 0.7,
					line: {
						color: 'rgb(0, 0, 0)',
						dash: 'dash',
						width: 3
					},
					hoverinfo: 'text+name',
					hoverlabel: {
						namelength: -1
					},
					customData: [],
					hovertemplate: '<b>Value</b>: %{text:.2f}' +
						'<br><b>X</b>: %{x}',
					text: oSuccess.forecast.y,
					yaxis: "y",
					legendgroup: "Forecast"
				};
				oData.push(data);
			}
			if (oSuccess.projected.epochSecond) {
				var data = {
					x: oSuccess.projected.epochSecond.map(epoch => oDateFormat.format(new Date(epoch * 1000))),
					y: oSuccess.projected.y,
					name: "Projected(HANA)",
					// type: 'scatter',
					mode: 'lines',
					line: {
						color: 'rgb(255, 0, 0)',
						dash: 'dot',
						width: 3
					},
					opacity: 0.7,
					hoverinfo: 'text+name',
					hoverlabel: {
						namelength: -1
					},
					customData: [],
					hovertemplate: '<b>Value</b>: %{text:.2f}' +
						'<br><b>X</b>: %{x}',
					text: oSuccess.projected.y,
					yaxis: "y",
					legendgroup: "Projected"
				};
				oData.push(data);
			}
			return oData;
		},
		onDOPTabSelected: function (oEvent) { //AN: #DOP-DGP
			var selectedKey = oEvent.getSource().getSelectedKey();
			this.onDopLoad();
		},
		onRollUpChangeDGP: function (oEvent) { //AN: #DOP-DGP
			var sKey = oEvent.getSource().getSelectedKey();
			if (sKey === "NRUP") {
				this.getModel("dashBoardModel").setProperty("/dgpData/rolledUp", false);
			} else if (sKey === "RUP") {
				this.getModel("dashBoardModel").setProperty("/dgpData/rolledUp", true);
			}
			this.onDopLoad();
		},
		getUpliftFactor: function (locCode, startDate) {
			var sUrl = "/taskmanagementRest/upliftfactor/dispatch?locationCode=" + locCode + "&startDate=" + startDate;
			var iUpliftIndex = this.getModel("dashBoardModel").getProperty("/upliftIndex");
			this.doAjax(sUrl, "GET", null, function (oData) {
				this.getModel("oTaskPanelDetailModel").setProperty("/customAttr/" + iUpliftIndex + "/labelValue", oData.value);
				var sUpliftImgUrl = "";
				if (oData.icon) {
					sUpliftImgUrl = "http://openweathermap.org/img/wn/" + oData.icon + ".png";
				}
				this.getModel("dashBoardModel").setProperty("/upliftImg", sUpliftImgUrl);
				this.getModel("oTaskPanelDetailModel").refresh(true);
			}.bind(this), function (oError) {
				this.getModel("oTaskPanelDetailModel").setProperty("/customAttr/" + iUpliftIndex + "/labelValue", "0");
				this.getModel("oTaskPanelDetailModel").refresh(true);
			}.bind(this));
		},
		validateModuleReadOnly: function (currentSelectTab) { //SH: Modules readonly - for create Task, check whether the module is ReadOnly
			var isModReadOnly;
			var moduleReadOnly = this.getModel("dashBoardModel").getProperty("/moduleReadOnly");
			switch (currentSelectTab) {
			case "alarms":
				isModReadOnly = moduleReadOnly.isAlarmReadOnly;
				break;
			case "scheduler":
				isModReadOnly = moduleReadOnly.isTaskSchedulerReadOnly;
				break;
			case "DOP":
				isModReadOnly = moduleReadOnly.isDOPReadOnly;
				break;
			case "ndtpv":
				isModReadOnly = moduleReadOnly.isProveReadOnly;
				break;
			case "fracMonitoring":
				isModReadOnly = moduleReadOnly.isFracReadOnly;
				break;
			case "downtime":
				isModReadOnly = moduleReadOnly.isDowntimeReadOnly;
				break;
			case "obx":
				isModReadOnly = moduleReadOnly.isOBXReadOnly;
				break;
			case "pwhopper":
				isModReadOnly = moduleReadOnly.isPWHopperReadOnly;
				break;
			case "workbench":
				isModReadOnly = moduleReadOnly.isWorkbenchReadOnly;
				break;
			}
			var isWebReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			return !isModReadOnly && !isWebReadOnly;
		},
		onWBMsgClose: function () { //AN: #msgToROC
			WorkbenchHelper.onWBMsgClose(this, this.getModel("dashBoardModel"));
		},
		onPressWBMsgButtons: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onPressWBMsgButtons(this, this.getModel("dashBoardModel"), oEvent);
		},
		onBeforeWorkbenchMsgToROCPanelOpen: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onBeforeWorkbenchMsgToROCPanelOpen(this, this.getModel("dashBoardModel"), oEvent);
		},
		//SH: HandoverNotes
		onPressHandoverNotesVBox: function () {
			var oDashBoardModel = this.getModel("dashBoardModel");
			oDashBoardModel.setProperty("/selectedTaskManagementVBox", "HandoverNotes");
			oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", true);
			oDashBoardModel.setProperty("/taskManagementPanel/showTaskManagementPanel", true);
			oDashBoardModel.setProperty("/taskManagementPanel/showHandoverNotesPanel", true);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showBypassPanel", false);
			this.getModel("dashBoardModel").setProperty("/taskManagementPanel/showOpAvailPanel", false);
			this.getModel("dashBoardModel").setProperty("/selectedHanoverNotesTab", "My Notes");
			var userName = this.getModel("dashBoardModel").getProperty("/userData/userId");
			var fromDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/fromDate");
			var toDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/toDate");
			var isReadOnly = this.getModel("dashBoardModel").getProperty("/isWebReadOnlyRole");
			var isENG = this.getModel("dashBoardModel").getProperty("/userData/isENG");
			var isForeman = this.getModel("dashBoardModel").getProperty("/userData/isForeman");
			/*var formattedFromDate = "",
				formattedToDate = "";
			if (fromDate !== "") {
				formattedFromDate = new Date(fromDate).getTime();
			}
			if (toDate !== "") {
				formattedToDate = new Date(toDate).getTime();
			}*/
			var field = this.getModel("dashBoardModel").getProperty("/handoverFilterData/selectedField");
			var shift = this.getModel("dashBoardModel").getProperty("/handoverFilterData/selectedShift");
			var noteType = "My Notes";

			if (isReadOnly || isENG || isForeman) {
				this.getModel("dashBoardModel").setProperty("/selectedHanoverNotesTab", "Search Notes");
				noteType = "Search Notes";
			}
			this.fetchHandoverNotesByNoteType(userName, noteType, fromDate, toDate, field, shift);
		},
		onHandoverNotesFilterPanelExpand: function (oEvent) {
			TaskManagementHelper.onHanoverNotesFilterPanelExpand(this, this.getModel("dashBoardModel"), oEvent);
		},
		onHandoverfilterLinkPress: function (oEvent) {
			TaskManagementHelper.onHandoverfilterLinkPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onHandoverFieldFilterChange: function (oEvent) { //AN: #taskFieldFilter
			TaskManagementHelper.onHandoverFieldFilterChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		idHandoverShiftFilterChange: function (oEvent) { //AN: #taskFieldFilter
			TaskManagementHelper.idHandoverShiftFilterChange(this, this.getModel("dashBoardModel"), oEvent); //AN: #ScratchFilter
		},
		onNotesTabSelection: function (oEvent) {
			var query = oEvent.getSource().getSelectedKey();
			/*var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "yyyy-MM-dd"
			});*/
			var fromDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/fromDate");
			var toDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/toDate");
			/*var formattedFromDate = "",
				formattedToDate = "";
			if (fromDate !== "") {
				formattedFromDate = new Date(fromDate).getTime();
			}
			if (toDate !== "") {
				formattedToDate = new Date(toDate).getTime();
			}*/
			var userName = this.getModel("dashBoardModel").getProperty("/userData/userId");
			var field = this.getModel("dashBoardModel").getProperty("/handoverFilterData/selectedField");
			var shift = this.getModel("dashBoardModel").getProperty("/handoverFilterData/selectedShift");

			switch (query) {
			case "My Notes":
				this.fetchHandoverNotesByNoteType(userName, query, fromDate, toDate, field, shift);
				break;
			case "Search Notes":
				if (fromDate === "" || toDate === "") {
					this.getModel("dashBoardModel").setProperty("/handoverNotesList", []);
					sap.m.MessageToast.show("Please select both the dates to search for notes");
				} else {
					this.fetchHandoverNotesByNoteType(userName, query, fromDate, toDate, field, shift);
				}
				break;
			}
		},
		onHandoverFetchPress: function () {
			/*var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "yyyy-MM-dd"
			});*/
			var selSegBtn = this.getModel("dashBoardModel").getProperty("/selectedHanoverNotesTab");
			var fromDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/fromDate");
			var toDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/toDate");
			/*var formattedFromDate = "",
				formattedToDate = "";
			if (fromDate !== "") {
				formattedFromDate = new Date(fromDate).getTime();
			}
			if (toDate !== "") {
				formattedToDate = new Date(toDate).getTime();
			}*/
			var userID = this.getModel("dashBoardModel").getProperty("/userData/userId");
			var field = this.getModel("dashBoardModel").getProperty("/handoverFilterData/selectedField");
			var shift = this.getModel("dashBoardModel").getProperty("/handoverFilterData/selectedShift");
			if (selSegBtn === "Search Notes") {
				if (fromDate === "" || toDate === "") {
					this.getModel("dashBoardModel").setProperty("/handoverNotesList", []);
					sap.m.MessageToast.show("Please select both the dates to search for notes");
				} else {
					this.fetchHandoverNotesByNoteType(userID, selSegBtn, fromDate, toDate, field, shift);
				}
			} else {
				var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
					pattern: "yyyy-MM-dd"
				});
				if (fromDate !== "" && toDate === "") {
					var toDateMax = this.getModel("dashBoardModel").getProperty("/handoverFilterData/handoverMaxDate");
					toDate = oDateFormat.format(toDateMax);
					this.getModel("dashBoardModel").setProperty("/handoverFilterData/toDate", toDate);
				} else if (fromDate === "" && toDate !== "") {
					/*fromDate = this.getModel("dashBoardModel").getProperty("/handoverFilterData/handoverMinDate");
					formattedFromDate = fromDate.getTime();
					this.getModel("dashBoardModel").setProperty("/handoverFilterData/fromDate", oDateFormat.format(fromDate));*/
				}
				this.fetchHandoverNotesByNoteType(userID, selSegBtn, fromDate, toDate, field, shift);
			}
		},
		onNotePress: function (oEvent) {
			var noteId;
			if (typeof oEvent === "object") {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
				var selectedObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
				this.getModel("dashBoardModel").setProperty("/currentHandoverNote", selectedObj);
				this.getModel("dashBoardModel").setProperty("/currentHandoverNoteContext", oEvent.getSource().getBindingContext("dashBoardModel"));
				noteId = selectedObj.noteId;
			} else {
				noteId = oEvent;
			}
			var notesData = {
				"SSVClose": "",
				"tasks": "",
				"inquiry": "",
				"CFWalkthrough": "",
				"HNForROC": "",
				"HNForForeman": "",
				"HNForLeadership": ""
			};
			this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData", notesData);

			var sUrl = "/taskmanagementRest/handoverNotesTOROC/getNotesForNoteId?noteId=" + noteId;
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage.statusCode === "0" && oData.handoverNotesDtoList.length > 0) {
					this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesDto", oData.handoverNotesDtoList);
					this.getModel("dashBoardModel").setProperty("/enableNoteEdit", false);

					for (var i = 0; i < oData.handoverNotesDtoList.length; i++) {
						switch (oData.handoverNotesDtoList[i].noteCategoryId) {
						case "N1":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/SSVClose", oData.handoverNotesDtoList[i].note);
							break;
						case "N2":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/tasks", oData.handoverNotesDtoList[i].note);
							break;
						case "N3":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/inquiry", oData.handoverNotesDtoList[i].note);
							break;
						case "N4":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/CFWalkthrough", oData.handoverNotesDtoList[i].note);
							break;
						case "N5":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/HNForROC", oData.handoverNotesDtoList[i].note);
							break;
						case "N6":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/HNForForeman", oData.handoverNotesDtoList[i].note);
							break;
						case "N7":
							this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/HNForLeadership", oData.handoverNotesDtoList[i].note);
							break;
						}
					}
					var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
						pattern: "dd-MMM-yy hh:mm:ss aaa"
					});
					if (oData.ssvCloseResponseList && oData.ssvCloseResponseList.length > 0) {
						var SSVCloseInHTML = "<ol>";
						for (var i = 0; i < oData.ssvCloseResponseList.length; i++) {
							SSVCloseInHTML += "<li>" + oData.ssvCloseResponseList[i].wellName + " - " + oData.ssvCloseResponseList[i].parentCode + " / " +
								oData.ssvCloseResponseList[i].childCode + " - ";

							if (oData.ssvCloseResponseList[i].startDate !== null) {
								SSVCloseInHTML += "Start Date: " + oDateFormat.format(new Date(oData.ssvCloseResponseList[i].startDate));
							}
							if (oData.ssvCloseResponseList[i].endDate !== null) {
								SSVCloseInHTML += ", End Date: " + oDateFormat.format(new Date(oData.ssvCloseResponseList[i].endDate)) + " - ";
							} else {
								SSVCloseInHTML += " - ";
							}
							if (oData.ssvCloseResponseList[i].downTimeInHours > 0) {
								SSVCloseInHTML += oData.ssvCloseResponseList[i].downTimeInHours;
								if (oData.ssvCloseResponseList[i].downTimeInHours > 1) {
									SSVCloseInHTML += "Hrs ";
								} else {
									SSVCloseInHTML += "Hr ";
								}
							}
							if (oData.ssvCloseResponseList[i].downTimeMinutes > 0) {
								SSVCloseInHTML += oData.ssvCloseResponseList[i].downTimeMinutes + "Mins ";
							}
							SSVCloseInHTML += "</li>"
							if (i === oData.ssvCloseResponseList.length - 1) {
								SSVCloseInHTML += "</ol>";
							}
						}
						this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/SSVClose", SSVCloseInHTML);
					} else {
						this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/SSVClose",
							"<p><em>No downtime created yet for the shift</em></p>");
					}
					if (oData.taskList && oData.taskList.length > 0) {
						var taskInHTML = "<ol>";
						for (var i = 0; i < oData.taskList.length; i++) {
							taskInHTML += "<li>" + oData.taskList[i].wellName + " - " + oData.taskList[i].classification + " / " +
								oData.taskList[i].subClassification;
							if (oData.taskList[i].description !== null && oData.taskList[i].description !== "") {
								taskInHTML += " - " + oData.taskList[i].description;
							} else {
								taskInHTML += " - <em>No task description entered during creation </em>";
							}
							if (oData.taskList[i].taskStatus !== null && oData.taskList[i].taskStatus !== "") {
								taskInHTML += " - " + oData.taskList[i].taskStatus;
							}
							if (oData.taskList[i].rootCause !== null && oData.taskList[i].rootCause !== "") {
								taskInHTML += " - " + oData.taskList[i].rootCause;
							}
							if (oData.taskList[i].rootCauseDesc !== null && oData.taskList[i].rootCauseDesc !== "") {
								taskInHTML += " - " + oData.taskList[i].rootCauseDesc;
							}

							taskInHTML += "</li>";
							if (i === oData.taskList.length - 1) {
								taskInHTML += "</ol>";
							}
						}
						this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/tasks", taskInHTML);
					} else {
						this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/tasks",
							"<p><em>No task created yet for the shift</em></p>");
					}
					if (oData.enquiryList && oData.enquiryList.length > 0) {
						var inquiryInHTML = "<ol>";
						for (var i = 0; i < oData.enquiryList.length; i++) {
							inquiryInHTML += "<li>" + oData.enquiryList[i].wellName + " - " + oData.enquiryList[i].classification;
							// + " / " +oData.enquiryList[i].subClassification; // no sub classification for inquiry
							if (oData.enquiryList[i].description !== null && oData.enquiryList[i].description !== "") {
								inquiryInHTML += " - " + oData.enquiryList[i].description;
							} else {
								inquiryInHTML += " - <em>No inquiry description entered during creation </em>";
							}
							if (oData.enquiryList[i].taskStatus !== null && oData.enquiryList[i].taskStatus !== "") {
								inquiryInHTML += " - " + oData.enquiryList[i].taskStatus;
							}
							inquiryInHTML += "</li>";
							if (i === oData.enquiryList.length - 1) {
								inquiryInHTML += "</ol>";
							}
						}
						this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/inquiry", inquiryInHTML);
					} else {
						this.getModel("dashBoardModel").setProperty("/currentHandoverNote/notesData/inquiry",
							"<p><em>No inquiry created yet for the shift</em></p>");
					}
					var oFragmentId = "handoverNotesTextEditor",
						oFragmentName = "com.sap.incture.Incture_IOP.fragment.handoverNotesTextEditor";
					if (!this.handoverNotesTextEditor) {
						this.handoverNotesTextEditor = this._createFragment(oFragmentId, oFragmentName);
						this.getView().addDependent(this.handoverNotesTextEditor);
					}
					this.handoverNotesTextEditor.open();
					if (this.oBusyIndForCreateHandoverNote) {
						this.oBusyIndForCreateHandoverNote.close();
					}
					if (this.createHandoverNotesPopup) {
						this.createHandoverNotesPopup.close();
						this.createHandoverNotesPopup.destroy();
						this.createHandoverNotesPopup = undefined;
					}
				} else {
					if (this.oBusyIndForCreateHandoverNote) {
						this.oBusyIndForCreateHandoverNote.close();
					}
					MessageBox.error("Data fetch failed");
				}
				this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
			}.bind(this), function (oError) {
				if (this.oBusyIndForCreateHandoverNote) {
					this.oBusyIndForCreateHandoverNote.close();
				}
				this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
				var sErrorMessage = "Data fetch failed";
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onTextEditorAfterOpen: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/currentIconTabForHandoverNote", "SSVClose");
		},
		onCreateNotePress: function () {
			var currentSeat = this.getModel("dashBoardModel").getProperty("/changeSeatData/changeSeatIOPUserDisplayRoles");
			var roles = currentSeat.split(",");
			var fields = [];
			for (var i in roles) {
				if (roles[i].includes("ROC")) {
					if (roles[i].includes("Catarina")) {
						fields.push({
							text: "Catarina"
						});
					}
					if (roles[i].includes("Karnes")) {
						fields.push({
							text: "Karnes North"
						});
						fields.push({
							text: "Karnes South"
						});
					}
					if (roles[i].includes("CentralTilden")) {
						fields.push({
							text: "Tilden Central"
						});
						fields.push({
							text: "Tilden East"
						});
					}
					if (roles[i].includes("WestTilden")) {
						fields.push({
							text: "Tilden West"
						});
						fields.push({
							text: "Tilden North"
						});
					}
				}
			}
			var shift = [{
				"text": "Day"
			}, {
				"text": "Night"
			}];
			var createHandoverNoteData = {
				"fields": fields,
				"shift": shift
			};
			this.getModel("dashBoardModel").setProperty("/createHandoverNoteData", createHandoverNoteData);
			var oFragmentId = "createHandoverNote",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.createHandoverNote";
			if (!this.createHandoverNotesPopup) {
				this.createHandoverNotesPopup = this._createFragment(oFragmentId, oFragmentName);
				this.getView().addDependent(this.createHandoverNotesPopup);
			}
			this.createHandoverNotesPopup.open();
		},
		onConfirmCreateNote: function (oEvent) {
			switch (oEvent.getSource().getText()) {
			case "Create":
				var date = this.getModel("dashBoardModel").getProperty("/createHandoverNoteData").date;
				var field = sap.ui.core.Fragment.byId("createHandoverNote", "createNoteField").getSelectedKey();
				var shift = sap.ui.core.Fragment.byId("createHandoverNote", "createNoteShift").getSelectedKey();
				if (date === "" || date === undefined || date === null || field === "" || shift === "") {
					sap.m.MessageToast.show("Please fill all the fields");
				} else {
					this.oBusyIndForCreateHandoverNote = new BusyDialog();
					this.oBusyIndForCreateHandoverNote.setBusyIndicatorDelay(0);
					this.oBusyIndForCreateHandoverNote.open();
					/*var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
						pattern: "yyyy-MM-dd"
					});*/
					// var formatedDate = new Date(date).getTime();
					var oPayload = {
						"noteType": "My Notes",
						"field": field,
						"shift": shift,
						"userId": this.getModel("dashBoardModel").getProperty("/userData/userId"),
						"fromDate": date,
						"toDate": date
					};
					var sUrl = "/taskmanagementRest/handoverNotesTOROC/getNotesByNoteType";
					this.doAjax(sUrl, "POST", oPayload, function (oData) {
						if (oData.responseMessage.statusCode === "0") {
							if (oData.handoverNotesDtoList.length > 0) {
								this.getModel("dashBoardModel").setProperty("/currentHandoverNote", oData.handoverNotesDtoList[0]);
								this.onNotePress(oData.handoverNotesDtoList[0].noteId);
							} else {
								this.createNewNote(date, field, shift);
							}
						} else {
							this.oBusyIndForCreateHandoverNote.close();
							MessageBox.error(oData.responseMessage.message);
						}
					}.bind(this), function (oError) {
						this.oBusyIndForCreateHandoverNote.close();
						var sErrorMessage = "Data fetch failed";
						this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
					}.bind(this));

				}
				break;
			case "Cancel":
				if (this.createHandoverNotesPopup) {
					this.createHandoverNotesPopup.close();
					this.createHandoverNotesPopup.destroy();
					this.createHandoverNotesPopup = undefined;
				}
				break;
			}
		},
		onTextEditorClose: function () {
			this.getModel("dashBoardModel").setProperty("/enableNoteEdit", false);
			if (this.handoverNotesTextEditor) {
				this.handoverNotesTextEditor.close();
				this.handoverNotesTextEditor.destroy();
				this.handoverNotesTextEditor = undefined;
			}
		},
		onTextEditorSave: function (oEvent) {
			if (oEvent.getSource().getText() === "Edit") {
				this.getModel("dashBoardModel").setProperty("/enableNoteEdit", true);
			} else {
				var userId = this.getModel("dashBoardModel").getProperty("/userData/userId");
				var userName = this.getModel("dashBoardModel").getProperty("/userData/displayName");
				var currentNote = this.getModel("dashBoardModel").getProperty("/currentHandoverNote");
				var noteData = this.getModel("dashBoardModel").getProperty("/currentHandoverNote/notesData");
				var non_editables = [{
					"noteId": currentNote.noteId,
					"note": noteData.SSVClose,
					"noteCategoryId": "N1",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}, {
					"noteId": currentNote.noteId,
					"note": noteData.tasks,
					"noteCategoryId": "N2",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}, {
					"noteId": currentNote.noteId,
					"note": noteData.inquiry,
					"noteCategoryId": "N3",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}];
				var oPayload = [{
					"noteId": currentNote.noteId,
					"note": noteData.CFWalkthrough,
					"noteCategoryId": "N4",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}, {
					"noteId": currentNote.noteId,
					"note": noteData.HNForROC,
					"noteCategoryId": "N5",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}, {
					"noteId": currentNote.noteId,
					"note": noteData.HNForForeman,
					"noteCategoryId": "N6",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}, {
					"noteId": currentNote.noteId,
					"note": noteData.HNForLeadership,
					"noteCategoryId": "N7",
					"date": currentNote.date,
					"shift": currentNote.shift,
					"field": currentNote.field,
					"userId": userId,
					"userName": userName
				}];
				var sUrl = "/taskmanagementRest/handoverNotesTOROC/saveOrUpdateHandoverNotes";
				this.oBusyInd.open();
				this.doAjax(sUrl, "POST", oPayload, function (oData) {
					this.oBusyInd.close();
					if (oData.statusCode === "0") {
						sap.m.MessageToast.show("Notes saved");
						this.getModel("dashBoardModel").setProperty("/enableNoteEdit", false);
						this.handoverNotesTextEditor.close();
						this.handoverNotesTextEditor.destroy();
						this.handoverNotesTextEditor = undefined;
						this.fetchHandoverNotesByNoteType(userId, "My Notes");
					} else {
						MessageBox.error("Error while saving notes");
					}
					this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
				}.bind(this), function (oError) {
					this.oBusyInd.close();
					var sErrorMessage = "Error while saving notes";
					this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(this));

			}
		},
		fetchHandoverNotesByNoteType: function (userId, noteType, fromDate, toDate, field, shift) {
			if (this.getModel("dashBoardModel").getProperty("/selectedHanoverNotesTab") === "Search Notes" && (fromDate === "" || toDate === "")) {
				this.getModel("dashBoardModel").setProperty("/handoverNotesList", []);
				sap.m.MessageToast.show("Please select both the dates to search for notes");
				this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
				return;
			}
			var oPayload = {
				"noteType": noteType,
				"field": field,
				"shift": shift,
				"userId": userId,
				"fromDate": fromDate,
				"toDate": toDate
			};
			this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", true);
			var sUrl = "/taskmanagementRest/handoverNotesTOROC/getNotesByNoteType";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					this.getModel("dashBoardModel").setProperty("/handoverNotesList", oData.handoverNotesDtoList);
				} else {
					this.getModel("dashBoardModel").setProperty("/handoverNotesList", []);
					MessageBox.error(oData.responseMessage.message);
				}
				this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
			}.bind(this), function (oError) {
				this.getModel("dashBoardModel").setProperty("/busyIndicators/rightPanelBusy", false);
				var sErrorMessage = "Data fetch failed";
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		createNewNote: function (date, field, shift) {
			var notesDataObj = {
				"SSVClose": "",
				"tasks": "",
				"inquiry": "",
				"CFWalkthrough": "",
				"HNForROC": "",
				"HNForForeman": "",
				"HNForLeadership": ""
			};
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "yyyy-MM-dd"
			});
			// var convDate = oDateFormat.format(new Date(date));
			// var utcEpoch = new Date(convDate.split("-")[0] + "/" + convDate.split("-")[1] + "/" + convDate.split("-")[2] + " UTC").getTime();
			var oPayload = {
				"date": date,
				"shift": shift,
				"field": field,
				"userId": this.getModel("dashBoardModel").getProperty("/userData/userId"),
				"userName": this.getModel("dashBoardModel").getProperty("/userData/displayName"),
				"noteCreatedAt": ""
			};
			var sUrl = "/taskmanagementRest/handoverNotesTOROC/getNotedetails";
			this.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData.responseMessage.statusCode === "0") {
					if (oData.ssvCloseResponseList && oData.ssvCloseResponseList.length > 0) {
						var SSVCloseInHTML = "<ol>";
						for (var i = 0; i < oData.ssvCloseResponseList.length; i++) {
							SSVCloseInHTML += "<li>" + oData.ssvCloseResponseList[i].wellName + " - " + oData.ssvCloseResponseList[i].parentCode +
								" / " +
								oData.ssvCloseResponseList[i].childCode + " - ";

							if (oData.ssvCloseResponseList[i].startDate !== null) {
								SSVCloseInHTML += "Start Date: " + oDateFormat.format(new Date(oData.ssvCloseResponseList[i].startDate));
							}
							if (oData.ssvCloseResponseList[i].endDate !== null) {
								SSVCloseInHTML += ", End Date: " + oDateFormat.format(new Date(oData.ssvCloseResponseList[i].endDate)) + " - ";
							} else {
								SSVCloseInHTML += " - ";
							}
							if (oData.ssvCloseResponseList[i].downTimeInHours > 0) {
								SSVCloseInHTML += oData.ssvCloseResponseList[i].downTimeInHours;
								if (oData.ssvCloseResponseList[i].downTimeInHours > 1) {
									SSVCloseInHTML += "Hrs ";
								} else {
									SSVCloseInHTML += "Hr ";
								}
							}
							if (oData.ssvCloseResponseList[i].downTimeMinutes > 0) {
								SSVCloseInHTML += oData.ssvCloseResponseList[i].downTimeMinutes + "Mins ";
							}
							SSVCloseInHTML += "</li>"
							if (i === oData.ssvCloseResponseList.length - 1) {
								SSVCloseInHTML += "</ol>";
							}
						}
						notesDataObj.SSVClose = SSVCloseInHTML;
					} else {
						notesDataObj.SSVClose = "<p><em>No downtime created yet for the shift</em></p>";
					}
					if (oData.taskList && oData.taskList.length > 0) {
						var taskInHTML = "<ol>";
						for (var i = 0; i < oData.taskList.length; i++) {
							taskInHTML += "<li>" + oData.taskList[i].wellName + " - " + oData.taskList[i].classification + " / " +
								oData.taskList[i].subClassification;
							if (oData.taskList[i].description !== null && oData.taskList[i].description !== "") {
								taskInHTML += " - " + oData.taskList[i].description;
							} else {
								taskInHTML += " - <em>No task description entered during creation </em>";
							}
							if (oData.taskList[i].taskStatus !== null && oData.taskList[i].taskStatus !== "") {
								taskInHTML += " - " + oData.taskList[i].taskStatus;
							}
							if (oData.taskList[i].rootCause !== null && oData.taskList[i].rootCause !== "") {
								taskInHTML += " - " + oData.taskList[i].rootCause;
							}
							if (oData.taskList[i].rootCauseDesc !== null && oData.taskList[i].rootCauseDesc !== "") {
								taskInHTML += " - " + oData.taskList[i].rootCauseDesc;
							}
							taskInHTML += "</li>";
							if (i === oData.taskList.length - 1) {
								taskInHTML += "</ol>";
							}
						}
						notesDataObj.tasks = taskInHTML;
					} else {
						notesDataObj.tasks = "<p><em>No task created yet for the shift</em></p>";
					}
					if (oData.enquiryList && oData.enquiryList.length > 0) {
						var inquiryInHTML = "<ol>";
						for (var i = 0; i < oData.enquiryList.length; i++) {
							inquiryInHTML += "<li>" + oData.enquiryList[i].wellName + " - " + oData.enquiryList[i].classification;
							// + " / " +oData.enquiryList[i].subClassification; // no subclassification for inquiry
							if (oData.enquiryList[i].description !== null && oData.enquiryList[i].description !== "") {
								inquiryInHTML += " - " + oData.enquiryList[i].description;
							} else {
								inquiryInHTML += " - <em>No inquiry description entered during creation </em>";
							}
							if (oData.enquiryList[i].taskStatus !== null && oData.enquiryList[i].taskStatus !== "") {
								inquiryInHTML += " - " + oData.enquiryList[i].taskStatus;
							}
							inquiryInHTML += "</li>";
							if (i === oData.enquiryList.length - 1) {
								inquiryInHTML += "</ol>";
							}
						}
						notesDataObj.inquiry = inquiryInHTML;
					} else {
						notesDataObj.inquiry = "<p><em>No inquiry created yet for the shift</em></p>";
					}
					var noteObj = {
						"date": date,
						"field": field,
						"note": null,
						"noteCategoryDescription": null,
						"noteCategoryId": null,
						"noteId": null,
						"notesData": notesDataObj,
						"shift": shift,
						"userId": this.getModel("dashBoardModel").getProperty("/userData/userId"),
						"userName": this.getModel("dashBoardModel").getProperty("/userData/displayName")
					};
					this.getModel("dashBoardModel").setProperty("/currentHandoverNote", noteObj);
					var oFragmentId = "handoverNotesTextEditor",
						oFragmentName = "com.sap.incture.Incture_IOP.fragment.handoverNotesTextEditor";
					if (!this.handoverNotesTextEditor) {
						this.handoverNotesTextEditor = this._createFragment(oFragmentId, oFragmentName);
						this.getView().addDependent(this.handoverNotesTextEditor);
					}
					this.oBusyIndForCreateHandoverNote.close();
					this.handoverNotesTextEditor.open();
					if (this.createHandoverNotesPopup) {
						this.createHandoverNotesPopup.close();
						this.createHandoverNotesPopup.destroy();
						this.createHandoverNotesPopup = undefined;
					}
				} else {
					this.oBusyIndForCreateHandoverNote.close();
					if (this.createHandoverNotesPopup) {
						this.createHandoverNotesPopup.close();
						this.createHandoverNotesPopup.destroy();
						this.createHandoverNotesPopup = undefined;
					}
					MessageBox.error(oData.responseMessage.message);
				}
			}.bind(this), function (oError) {
				this.oBusyIndForCreateHandoverNote.close();
				if (this.createHandoverNotesPopup) {
					this.createHandoverNotesPopup.close();
					this.createHandoverNotesPopup.destroy();
					this.createHandoverNotesPopup = undefined;
				}
				var sErrorMessage = "Data fetch failed";
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		onHandoverIconTabSelect: function (oEvent) {
			this.getModel("dashBoardModel").setProperty("/currentIconTabForHandoverNote", oEvent.getSource().getSelectedKey());
		},
		onSuggestLocations: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onSuggestLocations(this, this.getModel("dashBoardModel"), oEvent);
		},
		onSearchLocations: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onSearchLocations(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMsgSearchLocations: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMsgSearchLocations(this, this.getModel("dashBoardModel"), oEvent);
		},
		onViewSettingsMessagePress: function () { //AN: #wbBacklog
			WorkbenchHelper.onViewSettingsMessagePress(this);
		},
		onWBMsgRejectOKPress: function () { //AN: #msgToROC
			WorkbenchHelper.onWBMsgRejectOKPress(this, this.getModel("dashBoardModel"));
		},
		onWBMsgRejectCancelPress: function () { //AN: #msgToROC
			WorkbenchHelper.onWBMsgRejectCancelPress(this, this.getModel("dashBoardModel"));
		},
		onBeforeWBMsgLocationsDialogOpen: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onBeforeWBMsgLocationsDialogOpen(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMsgLocItemSelect: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMsgLocItemSelect(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMSgLocCountryChange: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMSgLocCountryChange(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMSgLocFieldChange: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMSgLocFieldChange(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMSgLocFacilityChange: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMSgLocFacilityChange(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMSgLocWellPadChange: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMSgLocWellPadChange(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBMSgLocWellChange: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMSgLocWellChange(this, this.getModel("dashBoardModel"), oEvent);
		},
		onHandoverNotePrintToPDF: function () { //SH: HandoverNotes - Print to PDF
			var oHTML = "";
			var sSrc = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.images", '/murphyLogo.png');
			var noteObj = this.getModel("dashBoardModel").getProperty("/currentHandoverNote");
			if (noteObj.notesData.CFWalkthrough === null) {
				noteObj.notesData.CFWalkthrough = "";
			}
			if (noteObj.notesData.HNForROC === null) {
				noteObj.notesData.HNForROC = "";
			}
			if (noteObj.notesData.HNForForeman === null) {
				noteObj.notesData.HNForForeman = "";
			}
			if (noteObj.notesData.HNForLeadership === null) {
				noteObj.notesData.HNForLeadership = "";
			}

			oHTML += "<div><img height='50px' width='100px' src='" + sSrc + "' style='margin: 0px 10px 0px'></div><hr><br>";
			oHTML +=
				"<div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 5px 10px 5px'><span style='font-size:15px;'><b>" +
				"Handover Note" +
				"</b></span></div><br>";
			oHTML +=
				"<div class='sapMFlexItemAlignAuto sapMFlexBoxBGTransparent sapMFlexItem iopFontClass'><div class='sapUiRespGrid sapUiRespGridMedia-Std-Desktop sapUiRespGridHSpace1 sapUiRespGridVSpace1 sapUiSmallMarginBegin'><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span><strong>Operator:  </strong></span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span class='sapUiSmallMarginEnd'>" +
				noteObj.userName +
				"</span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS3'><span><strong>Created On:</strong></span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span class='sapUiSmallMarginEnd'>" +
				noteObj.date +
				"</span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span><strong>Field:  </strong></span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span class='sapUiSmallMarginEnd'>" +
				noteObj.field +
				"</span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span><strong>Shift:  </strong></span></div><div class='sapUiRespGridSpanXL3 sapUiRespGridSpanL3 sapUiRespGridSpanM3 sapUiRespGridSpanS6'><span class='sapUiSmallMarginEnd'>" +
				noteObj.shift +
				"</span></div></div></div></div>";

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'> <div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"SSV Close" +
				"</b></span></div><hr>";
			var oSSVCloseDiv =
				"<div  style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.SSVClose +
				"</span></div>";
			oHTML += oSSVCloseDiv;
			oHTML += "</div>"

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"Tasks" +
				"</b></span></div><hr>";
			var oTaskDiv =
				"<div style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.tasks +
				"</span></div>";
			oHTML += oTaskDiv;
			oHTML += "</div>";

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"Inquiry" +
				"</b></span></div><hr>";
			var oInquiryDiv =
				"<div  style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.inquiry +
				"</span></div>";
			oHTML += oInquiryDiv;
			oHTML += "</div>";

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"CF Walkthrough" +
				"</b></span></div><hr>";
			var oCFWalkthroughDiv =
				"<div  style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.CFWalkthrough +
				"</span></div>";
			oHTML += oCFWalkthroughDiv;
			oHTML += "</div>";

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"Handover Note for ROC" +
				"</b></span></div><hr>";
			var oHNForROCDiv =
				"<div  style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.HNForROC +
				"</span></div>";
			oHTML += oHNForROCDiv;
			oHTML += "</div>";

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"Handover Note for Foreman" +
				"</b></span></div><hr>";
			var oHNForForemanDiv =
				"<div  style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.HNForForeman +
				"</span></div>";
			oHTML += oHNForForemanDiv;
			oHTML += "</div>";

			oHTML +=
				"<br><br><div style='border: 1px solid Gray; margin: 0px 10px 0px;'><div class='iopFontClass' style='margin: 10px'><span style='font-size:15px;'><b>" +
				"Handover Note for Leadership" +
				"</b></span></div><hr>";
			var oHNForLeadershipDiv =
				"<div  style='margin: 0px 10px 0px'><span class='sapUiTinyMarginTop sapUiMediumMarginBegin iopFontClass'>" + noteObj.notesData.HNForLeadership +
				"</span></div>";
			oHTML += oHNForLeadershipDiv;
			oHTML += "</div>";

			var printCssUrl = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.css", "/style.css");
			var link = '<link rel="stylesheet" href="' + printCssUrl + '" type="text/css" />';
			var sURI = sap.ui.core.IconPool.getIconURI("accept");
			var url = sap.ui.require.toUrl(sURI);
			link = link + '<link rel="stylesheet" href="' + url + '" />';
			var hContent = '<html><head>' + link + '</head><body>';
			// var hContent = '<html><head></head><body>';
			var bodyContent = oHTML;
			var closeContent = "</body></html>";
			var htmlpage = hContent + bodyContent + closeContent;
			var win = window.open("", "myWindow");
			win.document.open();
			win.document.write(htmlpage);
			$.each(document.styleSheets, function (index, oStyleSheet) {
				if (oStyleSheet.href) {
					var link = document.createElement("link");
					link.type = oStyleSheet.type;
					link.rel = "stylesheet";
					link.href = oStyleSheet.href;
					win.document.head.appendChild(link);
				}
			});
			setTimeout(function () {
				win.print();
				win.document.close();
				win.close();
			}, 2000);
		},
		onAfterWorkbenchMsgToROCPanelOpen: function (oEvent) { //AN: #msgToROC
			this.setTaskPanelSizes("Message");
		},
		onWBMsgRadioBtnPress: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBMsgRadioBtnPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBViewSettingsMessageConfirm: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBViewSettingsMessageConfirm(this, oEvent);
		},
		onWbMsgScrollLeftPage: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWbMsgScrollLeftPage(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWbMsgScrollRightPage: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWbMsgScrollRightPage(this, this.getModel("dashBoardModel"), oEvent);
		},
		onWBIconPress: function (oEvent) { //AN: #msgToROC
			WorkbenchHelper.onWBIconPress(this, this.getModel("dashBoardModel"), oEvent);
		},
		onProveUnitChange: function (oEvent) {
			if (oEvent.getSource().getState()) {
				this.getModel("dashBoardModel").setProperty("/spotFireData/unit", "boed");
			} else {
				this.getModel("dashBoardModel").setProperty("/spotFireData/unit", "E3m3");
			}
			this.getProveReport();
		}
	});
});