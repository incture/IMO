sap.ui.define([
	"com/sap/incture/IMO_PM/controller/BaseController",
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/json/JSONModel",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
	"sap/m/BusyDialog",
	"com/sap/incture/IMO_PM/formatter/formatter",
	"com/sap/incture/IMO_PM/util/util",
	"sap/m/MessageBox"
], function (BaseController, Controller, JSONModel, Filter, FilterOperator, BusyDialog, formatter, util, MessageBox) {
	"use strict";

	return BaseController.extend("com.sap.incture.IMO_PM.controller.CalendarView", {
		formatter: formatter,

		util: util,

		onInit: function () {
			this._router = this.getOwnerComponent().getRouter();
			this.fnInitCalendarView("Wo_Calendar");
			var oViewSettings = {
				"CalstartDate": new Date(),
				"iTop": 50,
				"iSkip": 0,
				"sCreatedOnStart": formatter.GetMonthsBackDate(90), // Date Range for Enter Date
				"sCreatedOnEnd": new Date().toLocaleDateString(),
				"sCalendarInterval": 30,
				"sReactiveorder": true,
				"sOrderTypeSel": "",
				"sEquip": "",
				"sFunLoc": "",
				"sPrior": ""

			};
			this.mLookupModel.setProperty("/", oViewSettings);
			this.busy = new BusyDialog();
			this.getLoggedInUser();
			var that = this;
			this.router = sap.ui.core.UIComponent.getRouterFor(this);
			this.router.attachRoutePatternMatched(function (oEvent) {
				that.routePatternMatched(oEvent);
			});
		},
		routePatternMatched: function (oEvent) {
			if (oEvent.getParameter("name") === "CalendarView") {
				this.getLoggedInUser();
				this.mLookupModel.setProperty("/CalstartDate", new Date());
				this.mLookupModel.setProperty("/sCalendarInterval", 30);
				this.mLookupModel.setProperty("/sOrderTypeSel", "");
				this.mLookupModel.setProperty("/sReactiveorder", true);
				this.mLookupModel.setProperty("/sEquip", "");
				this.mLookupModel.setProperty("/sFunLoc", "");
				this.mLookupModel.setProperty("/sPrior", "");
			}
			this.mLookupModel.setProperty("/CalstartDate", new Date());
			// this.mLookupModel.setProperty("/sCalendarInterval", 30);
		},
		getLoggedInUser: function () {
			var that = this;
			var sUrl = "/UserDetailsSet('')";
			var oPortalDataModel = this.oPortalDataModel;
			var mLookupModel = this.mLookupModel;
			oPortalDataModel.read(sUrl, {
				success: function (oData) {
					mLookupModel.setProperty("/userName", oData.UserName);
					mLookupModel.setProperty("/userRole", oData.Role);
					mLookupModel.setProperty("/userPlant", oData.UserPlant);
					that.fnInitializeWO();
				},
				error: function (oData) {
					mLookupModel.setProperty("/userName", "");
				}
			});
		},
		fnInitializeWO: function () {
			this.getOrderType();
			this.getWOPriorities();
			this.getFavEquips();
			this.getWorkCenters();
			this.fnFetchWOList();
		},
		fnFetchWOList: function () {
			var that = this;
			var mLookupModel = this.mLookupModel;
			var iTop = mLookupModel.getProperty("/iTop");
			if (iTop === null || iTop === undefined) {
				iTop = 50;
			}
			var iSkip = mLookupModel.getProperty("/iSkip");
			if (iSkip === null || iSkip === undefined) {
				iSkip = 0;
			}
			var userPlant = mLookupModel.getProperty("/userPlant");
			var aWorkOrderListSet = mLookupModel.getProperty("/aWorkOrderListSet");
			var sCreatedOnStart = mLookupModel.getProperty("/sCreatedOnStart"); // Date Range for Enter Date
			var sCreatedOnEnd = mLookupModel.getProperty("/sCreatedOnEnd");
			var startDate = mLookupModel.getProperty("/CalstartDate");
			if (!sCreatedOnStart) {
				sCreatedOnStart = formatter.GetMonthsBackDate(90);
				//if (startDate<=sCreatedOnStart){}
			} else {
				sCreatedOnStart = new Date(sCreatedOnStart);
			}
			if (sCreatedOnStart <= startDate) {
				sCreatedOnStart = formatter.getMonthbackDateofgiven(30, startDate);
			}
			if (!sCreatedOnEnd) {
				sCreatedOnEnd = new Date();
			} else {
				sCreatedOnEnd = new Date(sCreatedOnEnd);
			}
			if (sCreatedOnEnd >= startDate) {
				sCreatedOnEnd = formatter.getMonthbackDateofgiven(-30, startDate);
			}
			this.busy.open();
			var sUrl = "/WorkOrderListSet";
			var oPortalDataModel = this.oPortalDataModel;
			var oFilter = [];
			oFilter.push(new Filter("Orderid", "EQ", mLookupModel.getProperty("/iWONumFilter") === null || mLookupModel.getProperty(
				"/iWONumFilter") === undefined ? "" : mLookupModel.getProperty("/iWONumFilter")));

			oFilter.push(new Filter("OrderType", "EQ", mLookupModel.getProperty("/sOrderTypeSelFilter") === null || mLookupModel.getProperty(
				"/sOrderTypeSelFilter") === undefined ? "" : mLookupModel.getProperty("/sOrderTypeSelFilter")));

			oFilter.push(new Filter("SysStatus", "EQ", mLookupModel.getProperty("/sStatusSelFilter") === null || mLookupModel.getProperty(
				"/sStatusSelFilter") === undefined ? "" : mLookupModel.getProperty("/sStatusSelFilter")));

			oFilter.push(new Filter("WoDes", "EQ", mLookupModel.getProperty("/sWorderIdDesFilter") === null || mLookupModel.getProperty(
				"/sWorderIdDesFilter") === undefined ? "" : mLookupModel.getProperty("/sWorderIdDesFilter")));

			oFilter.push(new Filter("EnteredByName", "EQ", mLookupModel.getProperty("/sCreatedBy") === null || mLookupModel.getProperty(
				"/sCreatedBy") === undefined ? "" : mLookupModel.getProperty("/sCreatedBy")));

			oFilter.push(new Filter("Priority", "EQ", mLookupModel.getProperty("/sPriorSelFilter") === null || mLookupModel.getProperty(
				"/sPriorSelFilter") === undefined ? "" : mLookupModel.getProperty("/sPriorSelFilter")));

			oFilter.push(new Filter("AssignedTech", "EQ", mLookupModel.getProperty("/sAssignedTo") === null || mLookupModel.getProperty(
				"/sAssignedTo") === undefined ? "" : mLookupModel.getProperty("/sAssignedTo")));

			oFilter.push(new Filter("Equipment", "EQ", mLookupModel.getProperty("/sEquipFilter") === null || mLookupModel.getProperty(
				"/sEquipFilter") === undefined ? "" : mLookupModel.getProperty("/sEquipFilter")));

			oFilter.push(new Filter("MnWkCtr", "EQ", mLookupModel.getProperty("/sWorkCenterFilter") === null || mLookupModel.getProperty(
				"/sWorkCenterFilter") === undefined ? "" : mLookupModel.getProperty("/sWorkCenterFilter")));
			oFilter.push(new Filter("FunctLoc", "EQ", ""));
			oFilter.push(new Filter("Plant", "EQ", userPlant));
			oFilter.push(new Filter({
				filters: [new Filter("EnterDate", "GE", formatter.fnDatewithTimezoneoffset(sCreatedOnStart)),
					new Filter("EnterDate", "LE", formatter.fnDatewithTimezoneoffset(sCreatedOnEnd))
				],
				and: true
			}));
			oPortalDataModel.read(sUrl, {
				filters: oFilter,
				urlParameters: {
					"$top": iTop,
					"$skip": iSkip
				},
				success: function (oData) {
					aWorkOrderListSet = oData.results;
					if (iSkip !== 0) {
						aWorkOrderListSet = mLookupModel.getProperty("/aWorkOrderListSet").concat(aWorkOrderListSet);
					}
					$.each(aWorkOrderListSet, function (index, value) {
						value.PriorityDes = formatter.fnPriorityConversion(value.Priority);
						value.SysStatusDes = formatter.fnWOStatusConversion(value.SysStatus);
						if (value.StartDate) {
							value.StartDateString = that.fnDateConversionToSting(value.StartDate);
						}
						if (value.EnterDate) {
							value.EnterDateString = that.fnDateConversionToSting(value.EnterDate);
						}
						if (value.FinishDate) {
							value.FinishDateString = that.fnDateConversionToSting(value.FinishDate);
						}
						// if (value.SysStatusDes === "TECO"){

						// 	aWorkOrderListSet.splice(index);
						// }
					});
					var aActiveWorkOrders = [],
						TECOorder = [];
					for (var i = 0; i < aWorkOrderListSet.length; i++) {
						if (aWorkOrderListSet[i].SysStatusDes === "TECO") {
							TECOorder.push(aWorkOrderListSet.splice(i, 1)[0]);
						} else if (aWorkOrderListSet[i].StartDate < startDate && aWorkOrderListSet[i].FinishDate > startDate) {
							aActiveWorkOrders.push(aWorkOrderListSet[i]);
						}
					}
					mLookupModel.setProperty("/aWorkOrderListSet", aWorkOrderListSet);
					mLookupModel.setProperty("/ActiveWorkOrders", aActiveWorkOrders);
					//mLookupModel.setProperty("/iDisplayedWOCount", aWorkOrderListSet.length);
					mLookupModel.refresh();
					that.busy.close();
				},
				error: function (oData) {
					mLookupModel.setProperty("/aWorkOrderListSet", []);
					mLookupModel.refresh();
					that.busy.close();
				}
			});
		},
		getWorkCenters: function () {
			var sUrl = "/WorkcenterLookUpSet";
			var mLookupModel = this.mLookupModel;
			var oLookupDataModel = this.oLookupDataModel;
			var userPlant = mLookupModel.getProperty("/userPlant");
			var oFilter = [];
			oFilter.push(new Filter("Plant", "EQ", userPlant));
			oLookupDataModel.read(sUrl, {
				filters: oFilter,
				success: function (oData) {
					var aWorkCenterSet = oData.results;
					mLookupModel.setProperty("/aWorkCenterSet", aWorkCenterSet);
					mLookupModel.refresh();
				},
				error: function (oData) {
					mLookupModel.setProperty("/aWorkCenterSet", []);
					mLookupModel.refresh();
				}
			});
		},
		handleCalendarSelect: function (oEvent) {
			var oCalendar = oEvent.getSource();
			var mLookupModel = this.mLookupModel;
			var oSelectedDateRange = oCalendar.getSelectedDates()[0];
			var oSelectedDate = oSelectedDateRange.getStartDate();
			var oStartDate = mLookupModel.getProperty("/CalstartDate");
			var sWorkOrderList = mLookupModel.getProperty("/aWorkOrderListSet");
			if (oStartDate.toDateString() !== oSelectedDate.toDateString()) {
				mLookupModel.setProperty("/CalstartDate", oSelectedDate);
				var oDate = formatter.fnGetValDate(oSelectedDate);
				var sLastStartDate = sWorkOrderList[sWorkOrderList.length - 1].StartDate;
				if (sLastStartDate >= oDate) {
					this.handleLoadMore();
					// var iSkip = mLookupModel.getProperty("/iSkip") + 50;
					// mLookupModel.setProperty("/iSkip", iSkip);
					// mLookupModel.refresh(true);
					// this.fnFetchWOList();
				} else {
					var aActiveWorkOrders = [];
					for (var i = 0; i < sWorkOrderList.length; i++) {
						if (sWorkOrderList[i].SysStatusDes === "TECO") {
							sWorkOrderList.splice(i);
						} else if (sWorkOrderList[i].StartDate < oSelectedDate && sWorkOrderList[i].FinishDate > oSelectedDate) {
							aActiveWorkOrders.push(sWorkOrderList[i]);
						}
					}

					mLookupModel.setProperty("/ActiveWorkOrders", aActiveWorkOrders);
				}

			}
		},
		handleLoadMore: function () {
			var mLookupModel = this.mLookupModel;

			var iSkip = mLookupModel.getProperty("/iSkip") + 50;
			mLookupModel.setProperty("/iSkip", iSkip);
			mLookupModel.refresh(true);
			this.fnFetchWOList();
		},
		Intervalchange: function (oEvent) {
			var sInterval = oEvent.getSource().getCustomData()[0].getValue();

			if (sInterval === "1 week") {
				this.mLookupModel.setProperty("/sCalendarInterval", 7);
			} else if (sInterval === "2 weeks") {
				this.mLookupModel.setProperty("/sCalendarInterval", 14);
			} else if (sInterval === "1 month") {
				this.mLookupModel.setProperty("/sCalendarInterval", 30);
			}
		},
		onCreateButton: function () {
			if (!this._CreateWoDialog) {
				this._CreateWoDialog = sap.ui.xmlfragment("com.sap.incture.IMO_PM.fragment.CreateOrderinCalendar", this);
				this.getView().addDependent(this._CreateWoDialog);
			}
			this._CreateWoDialog.open();
		},
		onSelectOrder: function (oEvent) {
			var bReactiveorderType = this.mLookupModel.getProperty("/sReactiveorder");
			if (!bReactiveorderType) {
				this.mLookupModel.setProperty("/sOrderTypeSel", "PM03");
			}
		},
		onCancelCreate: function () {
			this._CreateWoDialog.close();
			this._CreateWoDialog.destroy();
			this._CreateWoDialog = null;
		},
		equipmentValueHelp: function (oEvent) {
			if (!this.equipmentsListDialog) {
				this.equipmentsListDialog = sap.ui.xmlfragment("com.sap.incture.IMO_PM.fragment.equipmentsListWO", this);
				this.getView().addDependent(this.equipmentsListDialog);
			}
			this.equipmentsListDialog.open();
		},

		//Function to select a Equipment and auto-populate Functional location
		onEquipSelect: function (oEvent) {
			var mLookupModel = this.mLookupModel;
			var oSource = oEvent.getParameter("listItem");
			var sPath = oSource.getBindingContextPath();
			var iEqId = mLookupModel.getProperty(sPath + "/Equnr");
			var iFunLoc = mLookupModel.getProperty(sPath + "/Tplnr");
			var equipDesc = mLookupModel.getProperty(sPath + "/Eqktu");
			var technicalId = mLookupModel.getProperty(sPath + "/Tidnr");
			var sWorkCenterSel = mLookupModel.getProperty(sPath + "/Gewrk");
			var sPlanGrpSel = mLookupModel.getProperty(sPath + "/Ingrp");
			var sCatelogProf = mLookupModel.getProperty(sPath + "/Rbnr");
			mLookupModel.setProperty("/sEquip", iEqId);
			mLookupModel.setProperty("/sWorkCenterSel", sWorkCenterSel);
			mLookupModel.setProperty("/sPlanGrpSel", sPlanGrpSel);
			mLookupModel.setProperty("/sCatelogProf", sCatelogProf);
			mLookupModel.setProperty("/sEquipFilter", iEqId);
			mLookupModel.setProperty("/sNotifEquipFilter", iEqId);
			mLookupModel.setProperty("/sFunLoc", iFunLoc);
			mLookupModel.setProperty("/EquipDesc", equipDesc);
			mLookupModel.setProperty("/AssetId", technicalId);
			this.setEquipWorkCenter(sWorkCenterSel, mLookupModel);
			this.getEquipsAssmebly(iEqId);
			// this.equipmentsListDialog.close();
			this.onCancelDialogEquip();
		},

		//Function to set Work center for a selected 
		setEquipWorkCenter: function (sWorkCenterSel, mLookupModel) {
			var workCenterId = "";
			var aWorkCenterSet = mLookupModel.getProperty("/aWorkCenterSet");
			aWorkCenterSet.filter(function (obj, i) {
				if (obj.ObjectId === sWorkCenterSel) {
					workCenterId = obj.WorkcenterId;
				}
			});
			mLookupModel.setProperty("/sWorkCenterSel", workCenterId);
		},
		// onSearchWOFilter: function (oEvent) {
		// 	var that = this;
		// 	//this.busy.open();
		// 	var mLookupModel = this.mLookupModel;
		// 	var oPortalDataModel = this.oPortalDataModel;
		// 	var userPlant = this.oUserDetailModel.getProperty("/userPlant");
		// 	var TechId = mLookupModel.getProperty("/TechId");
		// 	if (!TechId) {
		// 		TechId = "";
		// 	}
		// 	var EqIdDes = mLookupModel.getProperty("/EqIdDes");
		// 	if (!EqIdDes) {
		// 		EqIdDes = "";
		// 	}

		// 	var oFilter = [];
		// 	oFilter.push(new Filter("Equnr", "EQ", EqIdDes.toUpperCase()));
		// 	oFilter.push(new Filter("Tidnr", "EQ", TechId.toUpperCase()));
		// 	oFilter.push(new Filter("Eqktu", "EQ", EqIdDes.toUpperCase()));
		// 	oFilter.push(new Filter("plant", "EQ", userPlant));

		// 	oPortalDataModel.read("/EquipmentDetailsSet", {
		// 		filters: oFilter,
		// 		success: function (oData, oResponse) {
		// 			var aEquipmentsList = oData.results;
		// 			mLookupModel.setProperty("/aEquipmentsList", aEquipmentsList);
		// 			that.busy.close();
		// 		},
		// 		error: function (oResponse) {
		// 			mLookupModel.setProperty("/aEquipmentsList", []);
		// 			that.busy.close();
		// 		}
		// 	});
		// },
		onCancelDialogEquip: function (oEvent) {
			this.mLookupModel.setProperty("/iSkipEquip",0);
			this.equipmentsListDialog.close();
			this.equipmentsListDialog.destroy();
			this.equipmentsListDialog = null;
		},
		fnLocValueHelp: function () {
			if (!this.functionalLocationListDialog) {
				this.functionalLocationListDialog = sap.ui.xmlfragment("idFunctionalLocationFrag",
					"com.sap.incture.IMO_PM.fragment.functionalLocationList", this);
				this.getView().addDependent(this.functionalLocationListDialog);
			}
			this.functionalLocationListDialog.open();
		},
		// onCancelDialogFunLoc: function () {
			
		// 	this.functionalLocationListDialog.close();
		// 	this.functionalLocationListDialog.destroy();
		// 	this.functionalLocationListDialog = null;
		// },
		onFnLocSelect: function (oEvent) {
			this.onFunlocChange();
			var mLookupModel = this.mLookupModel;

			var oSource = oEvent.getParameter("listItem");
			var sPath = oSource.getBindingContextPath();
			var iFunLoc = mLookupModel.getProperty(sPath + "/FuncLoc");
			mLookupModel.setProperty("/sFunLoc", iFunLoc);

			this.onCancelDialogFunLoc();
		},
		//function to clear equipment details on change of functional location
		onFunlocChange: function () {
			var mLookupModel = this.mLookupModel;

			mLookupModel.setProperty("/sEquip", "");
			mLookupModel.setProperty("/sWorkCenterSel", "");
			mLookupModel.setProperty("/sEquipFilter", "");

			mLookupModel.setProperty("/sCatelogProf", "");

			mLookupModel.setProperty("/aEquipAssemblyList", "");

		},
		handleEquipIconTabSelect: function (oEvent) {

			var that = this;
			var selectedKey = oEvent.getSource().getSelectedKey();
			if (selectedKey === "idEqFunLoc") {
				this.busy.open();
				var mLookupModel = this.mLookupModel;

				var sFunctionalLocation = mLookupModel.getProperty("/sFunLoc");
				var sfunLoc = "'" + sFunctionalLocation.replace(/['"]+/g, '') + "'";
				var oPortalDataModel = this.oPortalDataModel;
				var userPlant = this.oUserDetailModel.getProperty("/userPlant");
				var oFilter = [];
				oFilter.push(new Filter("Equnr", "EQ", ''));
				oFilter.push(new Filter("Tidnr", "EQ", ''));
				oFilter.push(new Filter("Eqktu", "EQ", ''));
				oFilter.push(new Filter("plant", "EQ", userPlant));
				oFilter.push(new Filter("Tplnr ", "EQ", sfunLoc));
				oPortalDataModel.read("/EquipmentDetailsSet", {
					filters: oFilter,
					success: function (oData, oResponse) {
						var aEqListOfFunLoc = oData.results;
						mLookupModel.setProperty("/aEqListOfFunLoc", aEqListOfFunLoc);
						mLookupModel.refresh();
						that.busy.close();
					},
					error: function (oResponse) {
						mLookupModel.setProperty("/aEqListOfFunLoc", []);
						that.busy.close();
					}
				});
			}
		},
		onEquipOfFunLocSelect: function (oEvent) {
			var mLookupModel = this.mLookupModel;

			var oSource = oEvent.getParameter("listItem");
			var sPath = oSource.getBindingContextPath();
			var iEqId = mLookupModel.getProperty(sPath + "/EquipId");
			var iFunLoc = mLookupModel.getProperty("/sFunLoc");
			mLookupModel.setProperty("/sEquip", iEqId);
			mLookupModel.setProperty("/sFunLoc", iFunLoc);
			this.getEquipsAssmebly(iEqId);
			this.equipmentsListDialog.close();
		},
		onCreateOrder: function (oEvent) {
			var mLookupModel = this.mLookupModel;
			var bVal = this.validateMandatoryFields();
			if (bVal) {
				var orderType = mLookupModel.getProperty("/sOrderTypeSel");
				//this.getOrderTypeTasks(orderType);
				this.setFieldValOnDetailNav();
				this.router.navTo("detailWO", {
					workOrderID: "CREATE_WO_CAL"
				});
			}
		},
		validateMandatoryFields: function () {
			var oErrorMsg = "";
			var mLookupModel = this.mLookupModel;
			var oResourceModel = this.oResourceModel;
			var orderType = mLookupModel.getProperty("/sOrderTypeSel");
			var equipment = mLookupModel.getProperty("/sEquip");
			var priority = mLookupModel.getProperty("/sPrior");
			if (orderType) {
				if (equipment) {
					if (priority) {
						return true;
					} else {
						oErrorMsg = oResourceModel.getText("CREATE_SELECT_PRIORITY");
						this.showMessage(oErrorMsg);
					}
				} else {
					oErrorMsg = oResourceModel.getText("CREATE_SELECT_EQUIPMENT");
					this.showMessage(oErrorMsg);
				}
			} else {
				oErrorMsg = oResourceModel.getText("CREATE_SELECT_ORDER_TYPE");
				this.showMessage(oErrorMsg);
			}
			return false;
		},

		//Function to set mandatory fields from Create WO to Detail WO view
		setFieldValOnDetailNav: function () {
			var mLookupModel = this.mLookupModel;
			var oWorkOrderDetailModel = this.oWorkOrderDetailModel;
			var orderType = mLookupModel.getProperty("/sOrderTypeSel");
			var equipment = mLookupModel.getProperty("/sEquip");
			var funcLocation = mLookupModel.getProperty("/sFunLoc");
			var priority = mLookupModel.getProperty("/sPrior");
			var sWorkCenterSel = mLookupModel.getProperty("/sWorkCenterSel");
			var equipDesc = mLookupModel.getProperty("/EquipDesc");
			var plannerGrp = mLookupModel.getProperty("/sPlanGrpSel");
			var technicalId = mLookupModel.getProperty("/AssetId");
			// var setOrderStatus = mLookupModel.getProperty("/SetOrderStatus");
			//nischal -- Setting RequiredStartDate and EndDate Value Based on Priority in WorkOrderDetailViewModel
			this.fnSetEndDateBasedOnPriority(priority);
			oWorkOrderDetailModel.setProperty("/OrderType", orderType);
			oWorkOrderDetailModel.setProperty("/Equipment", equipment);
			oWorkOrderDetailModel.setProperty("/FunctLoc", funcLocation);
			oWorkOrderDetailModel.setProperty("/Priority", priority);
			oWorkOrderDetailModel.setProperty("/MnWkCtr", sWorkCenterSel);
			oWorkOrderDetailModel.setProperty("/EquipDesc", equipDesc);
			oWorkOrderDetailModel.setProperty("/Plangroup", plannerGrp);
			this.oWorkOrderDetailViewModel.setProperty("/AssetId", technicalId);
			oWorkOrderDetailModel.refresh();
		},
		fnSetEndDateBasedOnPriority: function (sVal) {
			var oWorkOrderDetailViewModel = this.oWorkOrderDetailViewModel;
			var tempStartDate = this.mLookupModel.getProperty("/CalstartDate");
			var tempEndDate = new Date();
			var oReqStartDate, oReqEndDate, someFormattedDate;
			if (sVal === "1") {
				tempEndDate.setDate(tempStartDate.getDate() + 7);
				someFormattedDate = this.getFormattedDate(tempEndDate);
				oReqEndDate = new Date(someFormattedDate);

			} else if (sVal === "2") {

				tempEndDate.setDate(tempStartDate.getDate() + 30);
				someFormattedDate = this.getFormattedDate(tempEndDate);
				oReqEndDate = new Date(someFormattedDate);
			} else if (sVal === "3") {

				tempEndDate.setDate(tempStartDate.getDate() + 90);
				someFormattedDate = this.getFormattedDate(tempEndDate);
				oReqEndDate = new Date(someFormattedDate);

			} else if (sVal === "4") {

				tempEndDate.setDate(tempStartDate.getDate() + 12);
				someFormattedDate = this.getFormattedDate(tempEndDate);
				oReqEndDate = new Date(someFormattedDate);
			} else if (sVal === "E") {
				tempEndDate.setDate(tempStartDate.getDate() + 3);
				someFormattedDate = this.getFormattedDate(tempEndDate);
				oReqEndDate = new Date(someFormattedDate);

			}
			oReqStartDate = tempStartDate;
			oWorkOrderDetailViewModel.setProperty("/oRequiredStartDate", oReqStartDate);
			oWorkOrderDetailViewModel.setProperty("/oRequiredEndDate", oReqEndDate);

		},
		getFormattedDate: function (sDate) {
			var dd = sDate.getDate();
			var mm = sDate.getMonth() + 1;
			var y = sDate.getFullYear();
			var someFormattedDate = y + '/' + mm + '/' + dd;
			return someFormattedDate;
		},
		onWorkOrderDetailpress: function (oEvent) {
			var sWorkOrderPath = oEvent.getSource().getBindingContext("mLookupModel").sPath;
			var sWorkOrderId = this.mLookupModel.getProperty(sWorkOrderPath).Orderid;
			this._router.navTo("detailTabWO", {
				workOrderID: sWorkOrderId
			});
		},
		//Function to open Task List and show in a pop-up
		fnOpenTaskListPopup: function () {
			var oResourceModel = this.oResourceModel;
			if (!this.taskListPopup) {
				this.taskListPopup = sap.ui.xmlfragment("com.sap.incture.IMO_PM.fragment.addTasks", this);
				this.getView().addDependent(this.taskListPopup);
			}
			var table = this.taskListPopup.getContent()[0];
			this.fnResetFilers(table, "oWorkOrderDetailViewModel");
			var oWorkOrderDetailModel = this.oWorkOrderDetailModel;
			var planGrp = oWorkOrderDetailModel.getProperty("/Plangroup");
			var workCenter = oWorkOrderDetailModel.getProperty("/MnWkCtr");
			if (workCenter) {
				if (planGrp) {
					this.fnGetTaskHeaderList(planGrp);
					this.taskListPopup.open();
				} else {
					this.showMessage(oResourceModel.getText("SEL_PLANNER_GP"));
				}
			} else {
				this.showMessage(oResourceModel.getText("plsselworkcener"));
			}
		},

		//Function to close Task List pop-up
		onCloseTaskListPopup: function (oEvent) {
			this.taskListPopup.close();
		},

		//Function to get task list for a selected Operation header
		onSelectTaskHeader: function (oEvent) {
			var oWorkOrderDetailViewModel = this.oWorkOrderDetailViewModel;
			var sPath = oEvent.getParameters().rowContext.getPath();
			var selectedTaskHeader = oWorkOrderDetailViewModel.getProperty(sPath);
			this.fnGetTaskList(selectedTaskHeader.Plnnr, selectedTaskHeader.Plnal);
			// this.fnGetComponentsList(selectedTaskHeader.Plnnr, selectedTaskHeader.Plnal); //SH: function to get Components from TaskList
		},

		//Function to update WO operations with tasks list
		updateWOOperations: function (taskList) {
			var oWorkOrderDetailModel = this.oWorkOrderDetailModel;
			var operations = oWorkOrderDetailModel.getProperty("/HEADERTOOPERATIONSNAV");
			var workCenter = oWorkOrderDetailModel.getProperty("/MnWkCtr");
			if (operations.length === 0) {
				taskList = this.formatOperationTasklist(taskList, [], workCenter);
			} else if (operations.length >= 1) {
				taskList = this.formatOperationTasklist(taskList, operations, workCenter);
			}
			oWorkOrderDetailModel.setProperty("/HEADERTOOPERATIONSNAV", taskList);
			oWorkOrderDetailModel.refresh();
			this.onCloseTaskListPopup();
		},

		//Function to get formatted Task list that is to be added on HeaderOperationsNav
		formatOperationTasklist: function (taskList, operations, workCenter) {
			var userPlant = this.oUserDetailModel.getProperty("/userPlant");
			for (var i = 0; i < taskList.length; i++) {
				var newOperationId = this.generateOperationId(operations);
				var oTempobj = {
					"Activity": newOperationId,
					"WorkCntr": workCenter,
					"LongText": taskList[i].OperationLtext, //Long text not available
					"Plant": userPlant,
					"Description": taskList[i].Ltxa1,
					"Systcond": "",
					"MyWork": "",
					"TWork": "",
					"T": "",
					"CompletedOn": new Date(),
					"SubActivity": "",
					"ControlKey": "PM01",
					"VendorNo": "",
					"PurchOrg": "",
					"PurGroup": "",
					"MatlGroup": "",
					"CalcKey": "",
					"Acttype": "",
					"Assembly": "",
					"Equipment": "",
					"BusArea": "",
					"WbsElem": "",
					"ProfitCtr": "",
					"OperCode": "C",
					"systemstatustext": ""
				};
				operations.push(oTempobj);
			}
			return operations;
		},
		onMtnPlanOverView:function(){
			this._router.navTo("MTNPlanView");
		}

	});

});