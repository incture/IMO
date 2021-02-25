sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/core/routing/History",
	"sap/m/MessageToast",
	"sap/m/Dialog",
	"sap/m/Label",
	"sap/m/TextArea",
	"sap/m/Button",
	"sap/m/Text",
	"sap/ui/core/IconPool",
	"sap/ui/core/ws/WebSocket", //RV: #webSocket
	"com/sap/incture/Incture_IOP/helper/TaskManagementHelper" //AN: #Notif
], function (Controller, History, MessageToast, Dialog, Label, TextArea, Button, Text, IconPool, WebSocket, TaskManagementHelper) {

	"use strict";

	return Controller.extend("com.sap.incture.Incture_IOP.controller.BaseController", {
		/**
		 * Convenience method for accessing the router in every controller of the application.
		 * @public
		 * @returns {sap.ui.core.routing.Router} the router for this component
		 */

		getRouter: function () {
			return this.getOwnerComponent().getRouter();
		},

		/**
		 * Convenience method for accessing the owner component in every controller of the application.
		 * @public
		 * @returns the component
		 */
		getOwner: function () {
			return this.getOwnerComponent();
		},

		/**
		 * Convenience method for getting the view model by name in every controller of the application.
		 * @public
		 * @param {string} sName the model name
		 * @returns {sap.ui.model.Model} the model instance
		 */
		getModel: function (sName) {
			return this.getView().getModel(sName);
		},

		/**
		 * Convenience method for setting the view model in every controller of the application.
		 * @public
		 * @param {sap.ui.model.Model} oModel the model instance
		 * @param {string} sName the model name
		 * @returns {sap.ui.mvc.View} the view instance
		 */
		setModel: function (oModel, sName) {
			return this.getView().setModel(oModel, sName);
		},
		/**
		 * Creates the fragment from the fragment name provided to the delegate and assigns a generated id.
		 * @param {String} sFragmentID The Fragment ID
		 * @param {String} sFragmentName The Fragment Name
		 * @returns {Object} Fragment Object.
		 * @private
		 */
		_createFragment: function (sFragmentID, sFragmentName) {

			jQuery.sap.assert(sFragmentName, "Trying to instantiate fragment but fragmentName is not provided.");
			var oFragment = sap.ui.xmlfragment(sFragmentID, sFragmentName, this);
			return oFragment;
		},

		/**
		 * Convenience method for showing a toast message.
		 * @public
		 * @params sMsg {string} message to be toasted
		 * @returns null
		 */
		_showToastMessage: function (sMsg) {
			MessageToast.show(sMsg, {
				duration: 5000
			});
		},

		/**
		 * Convenience method for getting the resource bundle.
		 * @public
		 * @returns {sap.ui.model.resource.ResourceModel} the resourceModel of the component
		 */
		getResourceBundle: function () {
			return this.getOwnerComponent().getModel("i18n").getResourceBundle();
		},

		/**
		 * Convenience method for navigating across views.
		 * @public
		 * @returns null
		 */
		_doNavigate: function (sRouteName, oParams) {
			this.oRouter.navTo(sRouteName, oParams, false);
		},
		/**
		 * Event handler  for navigating back.
		 * It checks if there is a history entry. If yes, history.go(-1) will happen.
		 * If not, it will replace the current entry of the browser history with the master route.
		 * @public
		 */
		onNavBack: function () {
			var sPreviousHash = History.getInstance().getPreviousHash();

			if (sPreviousHash !== undefined) {
				// The history contains a previous entry
				history.go(-1);
			} else {
				// Otherwise we go backwards with a forward history
				var bReplace = true;
				this.getRouter().navTo("", {}, bReplace);
			}
		},
		/**
		 * Event handler  for doing an HTTP request (Non Odata).
		 * @public 
		 * @params 
		 * sUrl 	- api URL - {string}
		 * sMethod  - the method -GET or POST or PUT or DELETE (PUT,DELETE -be careful about browser compatibility) -{string}
		 * oData - null if method is GET or the Request Body -{object}
		 * rSuccess - Success callback {function}
		 * rErrror - Error callback {function}
		 * @returns {object} the response data receieved through callback
		 */
		doAjax: function (sUrl, sMethod, oData, rSuccess, rError) {
			if (oData) {
				oData = JSON.stringify(oData);
			}
			var tempJsonModel = new sap.ui.model.json.JSONModel();
			this.getView().setModel(tempJsonModel, "tempJsonModel");
			tempJsonModel.loadData(sUrl, oData, true, sMethod, false, false, {
				"Content-Type": "application/json;charset=utf-8"
			});
			tempJsonModel.attachRequestCompleted(function (oEvent) {
				rSuccess(oEvent.getSource().getData());
			}.bind(rSuccess));
			tempJsonModel.attachRequestFailed(function (oEvent) {
				rError(oEvent);
			}.bind(rError));
			/*	$.ajax({
					url: sUrl,
					data: oData,
					async: true,
					dataType: "json",
					contentType: "application/json; charset=utf-8",
					error: function(err) {

						rError(err);
					},
					success: function(data) {

						rSuccess(data);
					},
					type: sMethod
				});*/

		},
		//SK: CHG0038615 - DOP/DGP data not in sync with the location selected in Location Hierarchy 
		doJquerryAjax: function (sUrl, sMethod, oData, rSuccess, rError) {
			if (oData) {
				oData = JSON.stringify(oData);
			}
			this.oAjax = $.ajax({
				url: sUrl,
				data: oData,
				async: true,
				dataType: "json",
				contentType: "application/json; charset=utf-8",
				error: function (err) {
					rError(err);
				},
				success: function (data) {
					rSuccess(data);
				},
				type: sMethod
			});
		},
		/**
		 * Registering Custom Icons
		 * @public 
		 */
		_initializeIcons: function () {
			//Registering icons
			IconPool.addIcon('alarms', 'InctureIopv2', 'icomoon', 'e901'); //Alarm Icon
			IconPool.addIcon('ndtpv', 'InctureIopv2', 'icomoon', 'e902'); //NDTPV Icon
			IconPool.addIcon('downtime', 'InctureIopv2', 'icomoon', 'e903'); //DownTime Icon
			IconPool.addIcon('permitTowork', 'InctureIopv2', 'icomoon', 'e904'); //PTW Icon
			IconPool.addIcon('analytics', 'InctureIopv2', 'icomoon', 'e905'); //Analytics Icon
			IconPool.addIcon('taskscheduler', 'InctureIopv2', 'icomoon', 'e906'); //TaskScheduler Icon
			IconPool.addIcon('dop', 'InctureIopv2', 'icomoon', 'e907'); //DOP Icon
			IconPool.addIcon('hopper', 'InctureIopv2', 'icomoon', 'e90f'); //Hopper Rabbit Icon/Image
			IconPool.addIcon('inquire', 'InctureIopv2', 'icomoon', 'e90a'); //Inquire Icon 
			IconPool.addIcon('well', 'InctureIopv2', 'icomoon', 'e914'); //Alarm well Icon/Image
			IconPool.addIcon('dispatch', 'InctureIopv2', 'icomoon', 'e908'); //Alarm Dispatch Icon/Image 
			IconPool.addIcon('designate', 'InctureIopv2', 'icomoon', 'e909'); //Alarm designate Icon/Image 
			IconPool.addIcon('mytask', 'InctureIopv2', 'icomoon', 'e90b'); //MyTask Icon
			IconPool.addIcon('send', 'InctureIopv2', 'icomoon', 'e90c'); //Send Icon
			IconPool.addIcon('nondispatch', 'InctureIopv2', 'icomoon', 'e90d'); //NonDispatch Icon
			IconPool.addIcon('create', 'InctureIopv2', 'icomoon', 'e90e'); //Create Icon
			IconPool.addIcon('more', 'InctureIopv2', 'icomoon', 'e900'); //More Icon
			IconPool.addIcon('createfrac', 'InctureIopv2', 'icomoon', 'e910'); //Create Frac Icon
			IconPool.addIcon('addwell', 'InctureIopv2', 'icomoon', 'e911'); //AddWell Icon
			IconPool.addIcon('fracAcknowledge', 'InctureIopv2', 'icomoon', 'e912'); //FracAcknowledge Icon
			IconPool.addIcon('fracHit', 'InctureIopv2', 'icomoon', 'e913'); //FracHit Icon
			IconPool.addIcon('locked', 'InctureIopv2', 'icomoon', 'e915'); //Lock Icon
			IconPool.addIcon('unlocked', 'InctureIopv2', 'icomoon', 'e916'); //Unlock Icon
			IconPool.addIcon('road', 'InctureIopv2', 'icomoon', 'e917'); //Road Icon
			IconPool.addIcon('crow', 'InctureIopv2', 'icomoon', 'e91a'); //Crow Icon
			IconPool.addIcon('OBX1', 'InctureIopv2', 'icomoon', 'e918'); //OBX1 Icon
			IconPool.addIcon('OBX2', 'InctureIopv2', 'icomoon', 'e919'); //Crow Icon
			//#RV Location History Changes Location Icon Addition to Icon Pool
			IconPool.addIcon('locationHistory', 'InctureIopv2', 'icomoon', 'e920'); //Location History Icon
		},
		/**
		 * Method to create a dialog of different states.
		 * @public 
		 * @params 
		 * confirmTitle  {string}
		 * confirmMsg  -{string}
		 * sState -  state of the dialog --Error,Success..etc {string}
		 * confirmYesBtn -  {string}
		 * confirmNoBtn - {string}
		 * actionButtonVisible  decides whethr the begin button should be visible or not {boolian}
		 * closehandler - callback for begin button press -{function}
		 * @returns {object} the response data receieved through callback
		 */
		_createConfirmationMessage: function (confirmTitle, confirmMsg, sState, confirmYesBtn, confirmNoBtn, actionButtonVisible,
			closehandler) {
			this.closehandler = closehandler;
			if (confirmMsg === "parsererror" || confirmMsg === "The user session has timed out. Please refresh the page") {
				confirmMsg = "The user session has timed out. Please refresh the page";
				this.getModel("dashBoardModel").setProperty("/isUserLoggedOut", true);
				confirmNoBtn = "Refresh";
			}
			this.oConfirmDialog = new Dialog({
				title: confirmTitle,
				type: 'Message',
				state: sState,
				content: new Text({
					text: confirmMsg
				}),
				beginButton: new Button({
					text: confirmYesBtn,
					visible: actionButtonVisible,
					press: function () {
						if (closehandler !== null) {
							this.closehandler();
						}

						this.oConfirmDialog.close();
					}.bind(this)
				}),
				endButton: new Button({
					text: confirmNoBtn,
					press: function () {
						this.oConfirmDialog.close();
						if (this.getModel("dashBoardModel").getProperty("/isUserLoggedOut")) {
							this.getModel("dashBoardModel").setProperty("/isUserLoggedOut", false);
							window.location.reload();
						}
					}.bind(this)
				}),
				afterClose: function () {
					this.oConfirmDialog.destroy();
					this.oConfirmDialog = undefined;
				}.bind(this)
			}).addStyleClass("sapUiSizeCompact");
			this.oConfirmDialog.open();
		},
		/**
		 * Method to define scroll heights for Task panels.
		 * @public 
		 * @param {string} taskType - used to determine which task panel is open (Inquiry, Investigation, Dispatch, etc)
		 * */
		setTaskPanelSizes: function (taskType) { //AN: #scrollHeight
			var tdDialogHeight, collabTaskHBox, collabTaskHBoxHeight;
			switch (taskType) {
			case "Inquiry":
				tdDialogHeight = this.oCreateInquiryFragment.$().height();
				collabTaskHBox = sap.ui.core.Fragment.byId("idCreateInquiryPanel", "idInquiryDetailRightPanel--idCollabTaskHBox");
				this.getModel("dashBoardModel").setProperty("/isTaskPanelOpen", "Inquiry");
				break;
			case "Investigation":
				tdDialogHeight = this.oInvestigationPanel.$().height();
				collabTaskHBox = sap.ui.core.Fragment.byId("CreateInvestigationPanel", "idInvestigationRightPanel--idCollabTaskHBox");
				this.getModel("dashBoardModel").setProperty("/isTaskPanelOpen", "Investigation");
				break;
			case "Message": //AN: #msgToROC
				tdDialogHeight = this.oWorkbenchMsgToROCPanel.$().height();
				collabTaskHBox = sap.ui.core.Fragment.byId("idWBMsgToROCPanel", "idWorkbenchMsgToROCDetailsPanel--idCollabTaskHBox");
				break;
			default:
				tdDialogHeight = this.createTaskPanel.$().height();
				collabTaskHBox = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailRightPanel--idCollabTaskHBox");
				this.getModel("dashBoardModel").setProperty("/isTaskPanelOpen", "Dispatch");
				break;
			}
			collabTaskHBoxHeight = collabTaskHBox.$().innerHeight();
			var tdRightScrollContHeight = (tdDialogHeight - collabTaskHBoxHeight) + "px";
			this.getModel("dashBoardModel").setProperty("/taskPanelSize/taskPanelScrollHeight", tdRightScrollContHeight);
		},
		/** 
		 * Function to set modulewise table scroll heights
		 * @constructor 
		 * @param {string} currentSelectKey - To chek the selected Tab
		 */
		_setModuleWiseScrollSizes: function (currentSelectKey) { //AN: #obxEngine
			var hierarchyButtonElementHeight = this.getView().byId("toolPage").getMainContents()[0].getContent()[0].$().height(); //AN: #obxEngineV2
			var hierarchyBreadcrumHeight = 0; //AN: #obxEngineV2
			var hierarchyWrapPanellHeight = 0; //AN: #obxEngineV2
			if (this.getView().byId("hierarchyWrapPanell").getExpanded()) { //AN: #obxEngineV2
				hierarchyWrapPanellHeight = 202;
			}
			var selectedLoc = this.getModel("dashBoardModel").getProperty("/hierarchyDetails/currentSelectedObject"); //AN: #obxEngineV2
			if (selectedLoc.length !== 0) { //AN: #obxEngineV2
				hierarchyBreadcrumHeight = 32;
			}
			var hierarchyBreadcrumPaddingOffset = 16; //AN: #obxEngineV2
			switch (currentSelectKey) {
			case "obx":
				var oBXIconTabBarElementParentHeight = 0,
					oBXIconTabBarHeaderheight = 0,
					oBXIconTabBarEngineConfigPanelheight = 0,
					oBxOperatorAllocationScrollHeight = 0,
					oBxSchedulingCalenderHeight = 0,
					oBxSchedulingPanelHeight = 0, //AN: #obxSchedulingNewView2
					oBxUsersScrollHeight = 0;
				var oBXIconTabBarElement = sap.ui.core.Fragment.byId(this.createId("obxFragment"), "idOBXIconTabBar");
				if (oBXIconTabBarElement.getAggregation("_header")) {
					oBXIconTabBarElementParentHeight = oBXIconTabBarElement.getParent().$().height();
					oBXIconTabBarHeaderheight = oBXIconTabBarElement.getAggregation("_header").$().height();
				}
				if (oBXIconTabBarElement.getSelectedKey() === "obxScheduling") {
					// oBxSchedulingCalenderHeight = oBXIconTabBarElement.getItems()[0].getContent()[0].getItems()[1].getItems()[0].$().height();
					// oBxUsersScrollHeight = (oBXIconTabBarElementParentHeight - oBXIconTabBarHeaderheight) + "px";
					// oBxOperatorAllocationScrollHeight = (oBXIconTabBarElementParentHeight - (oBxSchedulingCalenderHeight +
					// 	oBXIconTabBarHeaderheight)) + "px";
					// this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize/obxUsersScrollHeight",
					// 	oBxUsersScrollHeight);
					// this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize/obxRecommendedTaskListScrollHeight",
					// 	oBxOperatorAllocationScrollHeight);

					//AN: #obxSchedulingNewView2
					oBxSchedulingPanelHeight = oBXIconTabBarElement.getItems()[0].getContent()[0].$().height();
					// oBxUsersScrollHeight = (oBXIconTabBarElementParentHeight - (oBXIconTabBarHeaderheight + oBxSchedulingPanelHeight)) + "px";
					// oBxOperatorAllocationScrollHeight = (oBXIconTabBarElementParentHeight - (oBxSchedulingPanelHeight +
					// 	oBXIconTabBarHeaderheight)) + "px";
					oBxUsersScrollHeight = (oBXIconTabBarElementParentHeight - (oBxSchedulingPanelHeight +
						oBXIconTabBarHeaderheight + hierarchyButtonElementHeight + hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
						hierarchyBreadcrumPaddingOffset)) + "px"; //AN: #obxEngineV2
					oBxOperatorAllocationScrollHeight = (oBXIconTabBarElementParentHeight - (oBxSchedulingPanelHeight +
						oBXIconTabBarHeaderheight + hierarchyButtonElementHeight + hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
						hierarchyBreadcrumPaddingOffset)) + "px"; //AN: #obxEngineV2	
					this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize/obxUsersScrollHeight",
						oBxUsersScrollHeight);
					this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize/obxRecommendedTaskListScrollHeight",
						oBxOperatorAllocationScrollHeight);
				} else if (oBXIconTabBarElement.getSelectedKey() === "obxEngine") {
					oBXIconTabBarEngineConfigPanelheight = oBXIconTabBarElement.getItems()[1].getContent()[0].$().height();
					// oBxOperatorAllocationScrollHeight = (oBXIconTabBarElementParentHeight - (oBXIconTabBarEngineConfigPanelheight +
					// 	oBXIconTabBarHeaderheight)) + "px";
					oBxOperatorAllocationScrollHeight = (oBXIconTabBarElementParentHeight - (oBXIconTabBarEngineConfigPanelheight +
						oBXIconTabBarHeaderheight + hierarchyButtonElementHeight + hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
						hierarchyBreadcrumPaddingOffset)) + "px"; //AN: #obxEngineV2
					this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize/obxEngineScrollHeight", oBxOperatorAllocationScrollHeight);
				}
				break;
			case "DOP":
				//AN: #DOP-DGP
				var oDopFragment = sap.ui.core.Fragment.byId(this.createId("dopFragment"), "idDopIconTabBar");
				if (oDopFragment.getAggregation("_header")) {
					var oDopFragmentHeight = oDopFragment.getParent().$().height();
					var oDopFragmentIconTabHeaderHeight = oDopFragment.getAggregation("_header").$().height();
				}
				var dopScrollPanelHeight = (oDopFragmentHeight - (hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
					hierarchyButtonElementHeight + hierarchyBreadcrumPaddingOffset +
					oDopFragmentIconTabHeaderHeight + 50)) + "px";
				this.getModel("dashBoardModel").setProperty("/dopScrollPanelHeight", dopScrollPanelHeight);
				break;
			case "locHistory":
				var footerHeight = 42;
				var infoToolbarHeight = 32;
				var oLocationHistoryFragment = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "idLocHistoryIconTabBar");
				if (oLocationHistoryFragment.getAggregation("_header")) {
					var oLocationHistoryFragmentHeight = oLocationHistoryFragment.getParent().$().height();
					var oLocationHistoryFragmentIconTabHeaderHeight = oLocationHistoryFragment.getAggregation("_header").$().height();
				}
				var oPaginatorHeight = 0;
				var isPaginator = this.getModel("dashBoardModel").getProperty("/locationHistoryData/locHistoryPaginationVisibility");
				if (isPaginator) {
					oPaginatorHeight = sap.ui.core.Fragment.byId(this.createId("locHistoryFragment"), "idLocHistoryPageNo").$().height();
				}
				var locationHistoryScrollHeight = (oLocationHistoryFragmentHeight - (hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
					hierarchyButtonElementHeight + hierarchyBreadcrumPaddingOffset +
					oLocationHistoryFragmentIconTabHeaderHeight + infoToolbarHeight + oPaginatorHeight + footerHeight)) + "px";
				this.getModel("dashBoardModel").setProperty("/locationHistoryScrollPanelHeight", locationHistoryScrollHeight);
				break;
			case "permit":
				var footerHeight = 42;
				var oPTWFragment = sap.ui.core.Fragment.byId(this.createId("ptwFragment"), "ptwIconTabBar");
				if (oPTWFragment.getAggregation("_header")) {
					var oPTWFragmentHeight = oPTWFragment.getParent().$().height();
					//var oPTWFragmentIconTabHeaderHeight = oPTWFragment.getAggregation("_header").$().height();
					var oPTWIconHeight = oPTWFragment.$().height();
				}
				var oPTWFragmentScrollHeight = (oPTWFragmentHeight - (hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
					hierarchyButtonElementHeight + hierarchyBreadcrumPaddingOffset +
					oPTWIconHeight + footerHeight)) + "px";
				this.getModel("dashBoardModel").setProperty("/PTWScrollPanelHeight", oPTWFragmentScrollHeight);

				break;
			case "ndtpv":
				var footerHeight = 40;
				var infoToolbarHeight = 16;
				var oPaginatorHeight = 0;
				var isPaginator = this.getModel("dashBoardModel").getProperty("/spotFireData/paginationVisible");
				if (isPaginator) {
					oPaginatorHeight = sap.ui.core.Fragment.byId(this.createId("ndtpvFragmane"), "idProvePageNumberDiv").$().height();
				}
				var oVboxMainProve = sap.ui.core.Fragment.byId(this.createId("ndtpvFragmane"), "idVboxMainProve");
				var vboxMainProveHeight = oVboxMainProve.$().height();
				var proveScrollHeight = (vboxMainProveHeight - (hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
					hierarchyButtonElementHeight + hierarchyBreadcrumPaddingOffset + infoToolbarHeight + oPaginatorHeight + footerHeight)) + "px";
				this.getModel("dashBoardModel").setProperty("/moduleWiseScrollSize/proveScrollHeight", proveScrollHeight);
				break;
			case "alarms":
				var footerHeight = 40;
				var infoToolbarHeight = 32;
				var oAlarmFargment = sap.ui.core.Fragment.byId(this.createId("alarmFragment"), "idAlarmList");
				var oAlarmFargmentHeight = oAlarmFargment.getParent().$().height();
				var oAlaramtableHeader = oAlarmFargment.getAggregation("headerToolbar").$().height();
				var oAlramPaginatorHeight = this.getView().$().height() * 0.01;
				var alarmScrollHeight = (this.getView().$().height() - (hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
					hierarchyButtonElementHeight + hierarchyBreadcrumPaddingOffset + infoToolbarHeight + oAlaramtableHeader + oAlramPaginatorHeight
				)) + "px";
				this.getModel("dashBoardModel").setProperty("/alramListHeight", alarmScrollHeight);
				break;
			default:
			case "fracMonitoring":
				var oFracFargment = sap.ui.core.Fragment.byId(this.createId("fracMonitorFragment"), "fracMonitoringTableView");
				var oFractableHeader = oFracFargment.getAggregation("headerToolbar").$().height();
				var FracScrollHeight = (this.getView().$().height() - (hierarchyBreadcrumHeight + hierarchyWrapPanellHeight +
					hierarchyButtonElementHeight + hierarchyBreadcrumPaddingOffset + oFractableHeader)) + "px";
				this.getModel("dashBoardModel").setProperty("/fracListHeight", FracScrollHeight);
				break;
			}
		},
		/** 
		 * Function to handle Web Socket
		 * @param {Object} dashBoardModel- model
		 */
		handleWebSocket: function (dashBoardModel) { //RV: #webSocket
			this.establishHandShake(dashBoardModel);
			this.webSocket.attachMessage(function (oControlEvent) {
				if (oControlEvent.getParameters("data").data) {
					var oData = JSON.parse(oControlEvent.getParameters("data").data);
					oData.notifTotalCount = this.fnGetTotalNotifByRole(oData);
					dashBoardModel.setProperty("/webSocketProperty/data", oData);
					var sComment = this.getView().getModel("dashBoardModel").getProperty("/newComment"); //SK: #commentsEmptyIssueFix temp
					this.getView().getModel("dashBoardModel").refresh(true);
					if (this.getView().getModel("dashBoardModel") === "") {
						this.getView().getModel("dashBoardModel").setProperty("/newComment", sComment); //SK: #commentsEmptyIssueFixn temp
					}
					var sCurrentSelectKey = dashBoardModel.getProperty("/panelProperties/currentSelectKey"), //AN: #Notif
						sSelectedTaskManagementVBox = dashBoardModel.getProperty("/selectedTaskManagementVBox"), //AN: #Notif
						oUserData = dashBoardModel.getProperty("/userData"), //AN: #ShiftHandover
						bTaskManagementPanel = dashBoardModel.getProperty("/taskManagementPanel"); //AN: #ShiftHandover
					if (sCurrentSelectKey && sCurrentSelectKey === "fracMonitoring" && oData.fracNotificationDto && oData.fracNotificationDto.fracCount >
						0) { //AN: #Notif
						this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "Frac");
						this.getFracListDetails();
						// this.acknowledgeWebSocketRequest("Frac"); //AN: #BubbleNotifDemo
					} else if (sCurrentSelectKey && sCurrentSelectKey === "alarms" && oData.alarmNotificationDto && oData.alarmNotificationDto.alarmCount >
						0 && dashBoardModel.getProperty("/alarmRefresh")) { //AN: IOP Alarm Screen Issue: multiple select + acknowledge
						this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "Alarms"); //AN: #highlightShift
						this.getAlarmData(); //AN: #highlightShift
						// this.acknowledgeWebSocketRequest("Alarms"); //AN: #BubbleNotifDemo
					} else if (sCurrentSelectKey && sCurrentSelectKey === "pwhopper" && oData.pwHopperNotificationDto && oData.pwHopperNotificationDto
						.hopperCount > 0) {
						this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "PwHopper"); //AN: #highlightShift
						this.onPWHopperLoad(); //AN: #highlightShift
						// this.acknowledgeWebSocketRequest("PwHopper"); //AN: #BubbleNotifDemo
					}
					if (sSelectedTaskManagementVBox && sSelectedTaskManagementVBox === "ShiftHandover" && (oData.byPassLogNotificationDto && oData.byPassLogNotificationDto
							.byPassLogCount > 0) || (oData.energyIsolationNotificationDto && oData.energyIsolationNotificationDto
							.energyIsolationCount > 0) && bTaskManagementPanel.showBypassPanel) {
						this.setRowIndexToHighlightForNotif(dashBoardModel, "ShiftHandover"); //AN: #highlightShift
						// this.fetchAllActiveData(); //AN: #highlightShift
						// this.acknowledgeWebSocketRequest("ShiftHandover"); //AN: #BubbleNotifDemo
					} else if (sSelectedTaskManagementVBox && sSelectedTaskManagementVBox === "All" && oData.taskNotificationDto && oData.taskNotificationDto
						.taskCount > 0) {
						this.setRowIndexToHighlightForNotif(this.getModel("dashBoardModel"), "Tasks"); //AN: #highlightShift
						TaskManagementHelper._bindRightTaskPanelModel(this, "All"); //AN: #highlightShift
						// this.acknowledgeWebSocketRequest("Tasks"); //AN: #BubbleNotifDemo
					}
					if (oData.shiftChnage && oData.shiftChnage === "ShiftChange" && oUserData.isROC) { //AN: #ShiftHandover
						this.fetchAllActiveData(); //AN: #ShiftHandover
						// this.acknowledgeWebSocketRequest("ShiftChangeAcceptedCount"); //AN: #ShiftHandover UNCOMMENT LATER
						MessageToast.show(oData.action, {
							duration: 5000
						});
					}
				}
				sComment = this.getView().getModel("dashBoardModel").getProperty("/newComment"); //SK: #commentsEmptyIssueFix temp
				dashBoardModel.refresh(true);
				if (this.getView().getModel("dashBoardModel") === "") {
					this.getView().getModel("dashBoardModel").setProperty("/newComment", sComment); //SK: #commentsEmptyIssueFixn temp
				}
			}.bind(this));
			this.webSocket.attachClose(function (oControlEvent) {
				this.establishHandShake(dashBoardModel);
			}.bind(this));
		},
		fnGetTotalNotifByRole: function (oWebSocketData) {
			var oUserData = this.getView().getModel("dashBoardModel").getProperty("/userData");
			var iCount = 0;
			if (oUserData.isROC && oUserData.isPOT) {
				iCount = oWebSocketData.fracNotificationDto.fracCount + oWebSocketData.taskNotificationDto.taskCount + oWebSocketData.pwHopperNotificationDto
					.hopperCount + oWebSocketData.alarmNotificationDto.alarmCount + +oWebSocketData.byPassLogNotificationDto.byPassLogCount +
					oWebSocketData.energyIsolationNotificationDto.energyIsolationCount;
			} else if (oUserData.isROC) {
				iCount = oWebSocketData.fracNotificationDto.fracCount + oWebSocketData.taskNotificationDto.taskCount +
					oWebSocketData.alarmNotificationDto.alarmCount + +oWebSocketData.byPassLogNotificationDto.byPassLogCount + oWebSocketData.energyIsolationNotificationDto
					.energyIsolationCount;
			} else if (oUserData.isPOT) {
				iCount = oWebSocketData.pwHopperNotificationDto.hopperCount + oWebSocketData.taskNotificationDto.taskCount;
			} else if (oUserData.isENG) {
				iCount = oWebSocketData.pwHopperNotificationDto.hopperCount + oWebSocketData.taskNotificationDto.taskCount + oWebSocketData.fracNotificationDto
					.fracCount;
			}
			return iCount;
		},
		/** 
		 * Function to initiate the web socket connection
		 * @param {Object} dashBoardModel - MOdel
		 */
		establishHandShake: function (dashBoardModel) { //RV: #webSocket
			var oUserData = dashBoardModel.getProperty("/userData"); //AN: #Notif
			var sUserName = oUserData.userId; //AN: #Notif
			var oAppUrl = window.location.origin;
			var oUrl;
			if (oAppUrl.indexOf("d998e5467") >= 0) {
				oUrl = "wss://taskmanagementrestd998e5467.us2.hana.ondemand.com/TaskManagement_Rest";
			} else if (oAppUrl.indexOf("d7e367960") >= 0) {
				oUrl = "wss://taskmanagementrestd7e367960.us2.hana.ondemand.com/TaskManagement_Rest";
			} else if (oAppUrl.indexOf("dee8964f1") >= 0) {
				oUrl = "wss://taskmanagementrestdee8964f1.us2.hana.ondemand.com/TaskManagement_Rest";
			} else if (oAppUrl.indexOf("hi61zfjqlt") >= 0) {
				oUrl = "wss://taskmanagementresthi61zfjqlt.us2.hana.ondemand.com/TaskManagement_Rest";
			} else if (oAppUrl.indexOf("zqc750d9c2") >= 0) {
				oUrl = "wss://taskmanagementrestzqc750d9c2.us2.hana.ondemand.com/TaskManagement_Rest";
			}
			// oUrl = "wss://taskmanaged7e367960.us2.hana.ondemand.com/TaskManagement_Rest"; //AN: REMOVE LATER //AN: #Notif
			// oUrl = "wss://taskmanagementrestd7e367960.us2.hana.ondemand.com/TaskManagement_Rest"; //AN: REMOVE LATER //AN: #Notif
			var sLoggedInTechRoles = this.getLoggedInTechRoles(dashBoardModel);
			var sUrl = oUrl + "/fracEndPoint/" + sUserName + "/" + sLoggedInTechRoles; //AN: #Notif
			this.webSocket = new WebSocket(sUrl);
		},
		/** 
		 * Function to acknowledge web socket message
		 * @param {String} sNotifType - Type of Notification (Frac, Alarms, task, PwHopper or Bypass Log)
		 * @param {Object} oContext - Payload
		 * @param {String} oTriggerPoint - Whether external or undefined
		 * @param {Object} dashBoardModel - MOdel
		 */
		acknowledgeWebSocketRequest: function (sNotifType, oContext, oTriggerPoint) { //RV: #webSocket //AN: #Notif
			var sUrl = "",
				oDashBoardModel = this.getModel("dashBoardModel"),
				oPayload = [],
				oInitialDataPayload,
				oUserData = oDashBoardModel.getProperty("/userData"),
				oWebSocketData = oDashBoardModel.getProperty("/webSocketProperty/data"),
				oNotifData;
			switch (sNotifType) {
			case "Frac":
				sUrl = "/taskmanagementRest/frachit/updateNotifyWell";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.fracNotificationDto.fracNotificationList; //AN: #Notif
				}
				break;
			case "Alarms":
				sUrl = "/taskmanagementRest/frachit/updateBubbleAck";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.alarmNotificationDto.alarmNotificationList; //AN: #Notif
				}
				break;
			case "Tasks":
				sUrl = "/taskmanagementRest/frachit/updateBubbleAck";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.taskNotificationDto.taskNotificationList; //AN: #Notif
				}
				break;
			case "PwHopper":
				sUrl = "/taskmanagementRest/frachit/updateBubbleAck";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList; //AN: #Notif
				}
				break;
			case "BypassLog":
				sUrl = "/taskmanagementRest/frachit/updateSafetyAppAckStatus";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList; //AN: #Notif
				}
				break;
			case "EnergyIso":
				sUrl = "/taskmanagementRest/frachit/updateSafetyAppAckStatus";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList; //AN: #Notif
				}
				break;
			case "ShiftHandover":
				sUrl = "/taskmanagementRest/frachit/updateSafetyAppAckStatus";
				if (!oTriggerPoint) {
					oNotifData = oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList.concat(oWebSocketData.energyIsolationNotificationDto
						.energyIsolationNotificationList); //AN: #Notif
					var aTempNotifData = [];
					var aBypassList = oDashBoardModel.getProperty("/bypassObj/bypassList");
					for (var k = 0; k < oNotifData.length; k++) { //AN: loop to filter out data and create a new array for acknowledging
						if (oNotifData[k].status !== "Created") {
							aTempNotifData.push(oNotifData[k]);
						}
						// else if (oNotifData[k].status === "Created") {
						// 	for (var j = 0; j < aBypassList.length; j++) {
						// 		if (oNotifData[k].objectId === aBypassList[j].ssdBypassId) {
						// 			break;
						// 		} else if (j === aBypassList.length) {
						// 			aTempNotifData.push(oNotifData[k]);
						// 			break;
						// 		}
						// 	}
						// }
					}
					oNotifData = aTempNotifData;
					if (oNotifData && oNotifData.length > 0) {
						this.fetchAllActiveData();
					}
				}
				break;
			case "ShiftChangeAcceptedCount":
				sUrl = "";
				break;
			default:
				break;
			}
			var sLoggedInTechRoles = this.getLoggedInTechRoles(oDashBoardModel);
			if (oTriggerPoint === "External") {
				oInitialDataPayload = oContext;
				oInitialDataPayload.userId = oUserData.userId;
				oInitialDataPayload.userGroup = sLoggedInTechRoles;
				oInitialDataPayload.isAcknowledged = "true";
				oPayload.push(oInitialDataPayload);
			} else if (!oTriggerPoint && oNotifData) {
				for (var i = 0; i < oNotifData.length; i++) {
					oInitialDataPayload = oNotifData[i];
					oInitialDataPayload.userId = oUserData.userId;
					oInitialDataPayload.userGroup = sLoggedInTechRoles;
					oInitialDataPayload.isAcknowledged = "true";
					oPayload.push(oInitialDataPayload);
				}
			} else { //AN: nothing to acknowledge
				return;
			}
			this.doAjax(sUrl, "POST", oPayload, function (oServiceData) {
					if (oServiceData.statusCode === "0") {
						this.refreshNotifList(sNotifType, oContext, oTriggerPoint); //AN: #manualRefresh
						this.setNotifBusy(oDashBoardModel); //AN: #loaderWhileNotif
					} else {
						this.setNotifBusy(oDashBoardModel); //AN: #loaderWhileNotif
					}
				}.bind(this),
				function (oError) {
					this.setNotifBusy(oDashBoardModel); //AN: #loaderWhileNotif
					sap.m.MessageToast.show(oError.statusText); //AN: REMOVE LATER
				});
		},
		_setTaskListHeights: function () {
			var screenheight = sap.ui.Device.resize.height;
			var tasklistheight = screenheight * 0.65;
			tasklistheight = tasklistheight + "px";
			this.getModel("dashBoardModel").setProperty("/TaskListFragmentScrollHeight", tasklistheight);
		},
		_setNotificationPanelHeights: function () {
			var notificationPanelHeight = this.getView().$().height() - 40 + "px";
			this.getModel("dashBoardModel").setProperty("/notificationPanelHeight", notificationPanelHeight);
			this.getModel("dashBoardModel").refresh(true);
		},
		_setEnergyIsolationFromHeight: function () {
			var screenheight = sap.ui.Device.resize.height;
			var FormHeight = screenheight * 0.70;
			FormHeight = FormHeight + "px";
			this.getModel("dashBoardModel").setProperty("/EnergyIsolationFromHeight", FormHeight);
		},
		/*SK : To set the height of the PTW form fragment*/
		_setPTWFromHeight: function () {
			var screenheight = sap.ui.Device.resize.height;
			var FormHeight = screenheight * 0.90;
			FormHeight = FormHeight + "px";
			this.getModel("dashBoardModel").setProperty("/PTWFromHeight", FormHeight);
		},
		_setBypassLogFromHeight: function () {
			var screenheight = sap.ui.Device.resize.height;
			var FormHeight = screenheight * 0.60;
			FormHeight = FormHeight + "px";
			this.getModel("dashBoardModel").setProperty("/BypassLogFromHeight", FormHeight);
		},
		/** 
		 * Function to get all the logged in tech roles
		 * @param oDashboardModel - Dashboard model
		 * @returns sLoggedInTechRoles - Logged in Tech Roles
		 */
		getLoggedInTechRoles: function (oDashboardModel) { //AN: #Notif
			var oUserData = oDashboardModel.getProperty("/userData"),
				sLoggedInTechRoles = "";
			if (oUserData.isROC) {
				sLoggedInTechRoles = oUserData.resGroupRead;
				if (oUserData.isPOT) {
					sLoggedInTechRoles = sLoggedInTechRoles + "," + oUserData.potRole;
				}
			} else if (oUserData.isPOT) {
				sLoggedInTechRoles = oUserData.potRole;
			} else if (oUserData.isENG) {
				sLoggedInTechRoles = oUserData.engRole;
			}
			return sLoggedInTechRoles;
		},
		/** 
		 * Function to set row index for highlighting for bubble notification
		 * @param oDashboardModel- contains dashboard model
		 * @param sSelectedKey - current selected key
		 */
		setRowIndexToHighlightForNotif: function (oDashboardModel, sSelectedKey) { //AN: #Notif
			var oWebSocketData = oDashboardModel.getData().webSocketProperty.data,
				oIndex = [],
				i = 0;
			switch (sSelectedKey) {
			case "Alarms":
				for (i; oWebSocketData.alarmNotificationDto.alarmNotificationList && i < oWebSocketData.alarmNotificationDto.alarmNotificationList
					.length; i++) {
					oIndex.push(oWebSocketData.alarmNotificationDto.alarmNotificationList[i].objectId);
				}
				oDashboardModel.getData().highlightAlarms = oIndex;
				break;
			case "Frac":
				for (i; oWebSocketData.fracNotificationDto.fracNotificationList && i < oWebSocketData.fracNotificationDto.fracNotificationList.length; i++) {
					oIndex.push(oWebSocketData.fracNotificationDto.fracNotificationList[i].fracId);
				}
				oDashboardModel.getData().highlightFrac = oIndex;
				break;
			case "PwHopper":
				for (i; oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList && i < oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList
					.length; i++) {
					oIndex.push(oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList[i].objectId);
				}
				oDashboardModel.getData().highlightPWHopper = oIndex;
				break;
			case "ShiftHandover":
				for (i; oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList && i < oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList
					.length; i++) {
					if (oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList[i].status !== "Created") {
						oIndex.push(oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList[i].objectId);
					}
				}
				for (i; oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList && i < oWebSocketData.energyIsolationNotificationDto
					.energyIsolationNotificationList
					.length; i++) {
					if (oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList[i].status !== "Created") {
						oIndex.push(oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList[i].objectId);
					}
				}
				oDashboardModel.getData().highlightShiftHandoverList = oIndex;
				break;
			case "Tasks":
				for (i; oWebSocketData.taskNotificationDto.taskNotificationList && i < oWebSocketData.taskNotificationDto.taskNotificationList
					.length; i++) {
					oIndex.push(oWebSocketData.taskNotificationDto.taskNotificationList[i].objectId);
				}
				oDashboardModel.getData().highlightTasks = oIndex;
				break;
			default:
				break;
			}
		},
		/** 
		 * Function to initiate search for wells and facility based on location history and notifications
		 * @param oContext - contains the object data of Alarms, DOP, Frac, and Notif(Alarms, PWHopper)
		 * @param sModule - Where is this to be triggerred (Location History/Alarms/PWHopper) to ?
		 */
		setSelectedLocationExternally: function (oContext, sModule) { //AN: #Notif
			this._clearhirSelectedItem();
			this.getModel("dashBoardModel").setProperty("/hierarchyDetails/selectall/checked", false);
			var sUrl = "";
			if (!oContext.locationType && sModule === "LocationHistory") {
				oContext.locationType = "Well";
			}
			if (oContext.locationType && oContext.locationCode) {
				sUrl = "/taskmanagementRest/location/getFieldTextLoc?locationList=" + oContext.locationCode + "&locType=" + oContext.locationType;
			}
			this.doAjax(sUrl, "GET", null, function (oData) {
				if (oData && oData.responseMessage.statusCode === "0") {
					var oInitialDataPayload;
					if (oContext.locationType === "Well") {
						oInitialDataPayload = {
							"locationType": "SEARCH",
							"navigate": "",
							"location": oData.locationText
						};
					} else {
						oInitialDataPayload = {
							"location": oData.locationText,
							"locationType": "SEARCH",
							"navigate": "",
							"forFacility": "true"
						};
					}
					this.getModel("dashBoardModel").setProperty("/searchData", oInitialDataPayload);
					this.getModel("dashBoardModel").setProperty("/navToModule", true);
					this._setHirarchyData("SEARCH");
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				this._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(this));
		},
		setSelectedModule: function () {
			var oDashBoardModel = this.getModel("dashBoardModel");
			var sCurrentSelectKey = oDashBoardModel.getProperty("/panelProperties/currentSelectKey");
			var sItemList = this.getView().byId("appsNavList").getItems();
			var sIndex;
			for (var i = 0; i < sItemList.length; i++) {
				if (sItemList[i].getKey() === sCurrentSelectKey) {
					sIndex = i;
				}
			}
			if (sIndex > 0) {
				this.getView().byId("appsNavList").setSelectedItem(this.getView().byId("appsNavList").getItems()[sIndex]);
			}
		},
		setNotifBusy: function (oDashBoardModel) { //AN: #loaderWhileNotif
			oDashBoardModel.setProperty("/webSocketProperty/fracInd", false);
			oDashBoardModel.setProperty("/webSocketProperty/tasksInd", false);
			oDashBoardModel.setProperty("/webSocketProperty/alarmsInd", false);
			oDashBoardModel.setProperty("/webSocketProperty/pwHopperInd", false);
			oDashBoardModel.setProperty("/webSocketProperty/bypassLogInd", false);
			oDashBoardModel.setProperty("/webSocketProperty/energyIsoInd", false);
		},
		refreshNotifList: function (sNotifType, oContext, oTriggerPoint) {
			var oDashBoardModel = this.getModel("dashBoardModel"),
				oWebSocketData = oDashBoardModel.getProperty("/webSocketProperty/data"),
				i = 0;
			switch (sNotifType) {
			case "Frac":
				if (oTriggerPoint === "External" && oContext) {
					for (i; i < oWebSocketData.fracNotificationDto.fracNotificationList.length; i++) {
						if (oWebSocketData.fracNotificationDto.fracNotificationList[i].fracId === oContext.fracId) {
							oWebSocketData.fracNotificationDto.fracNotificationList.splice(i, 1);
							oWebSocketData.fracNotificationDto.fracCount = oWebSocketData.fracNotificationDto.fracCount - 1;
							break;
						}
					}
				} else if (!oTriggerPoint) {
					oWebSocketData.fracNotificationDto.fracNotificationList = [];
					oWebSocketData.fracNotificationDto.fracCount = 0;
				}
				break;
			case "Tasks":
				if (oTriggerPoint === "External") {
					for (i; i < oWebSocketData.taskNotificationDto.taskNotificationList.length; i++) {
						if (oWebSocketData.taskNotificationDto.taskNotificationList[i].objectId === oContext.objectId) {
							oWebSocketData.taskNotificationDto.taskNotificationList.splice(i, 1);
							oWebSocketData.taskNotificationDto.taskCount = oWebSocketData.taskNotificationDto.taskCount - 1;
							break;
						}
					}
				} else if (!oTriggerPoint) {
					oWebSocketData.taskNotificationDto.taskNotificationList = [];
					oWebSocketData.taskNotificationDto.taskCount = 0;
				}
				break;
			case "Alarms":
				if (oTriggerPoint === "External" && oContext) {
					for (i; i < oWebSocketData.alarmNotificationDto.alarmNotificationList.length; i++) {
						if (oWebSocketData.alarmNotificationDto.alarmNotificationList[i].objectId === oContext.objectId) {
							oWebSocketData.alarmNotificationDto.alarmNotificationList.splice(i, 1);
							oWebSocketData.alarmNotificationDto.alarmCount = oWebSocketData.alarmNotificationDto.alarmCount - 1;
							break;
						}
					}
				} else if (!oTriggerPoint) {
					oWebSocketData.alarmNotificationDto.alarmNotificationList = [];
					oWebSocketData.alarmNotificationDto.alarmCount = 0;
				}
				break;
			case "PwHopper":
				if (oTriggerPoint === "External" && oContext) {
					for (i; i < oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList.length; i++) {
						if (oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList[i].objectId === oContext.objectId) {
							oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList.splice(i, 1);
							oWebSocketData.pwHopperNotificationDto.hopperCount = oWebSocketData.pwHopperNotificationDto.hopperCount - 1;
							break;
						}
					}
				} else if (!oTriggerPoint) {
					oWebSocketData.pwHopperNotificationDto.pwHopperNotificationList = [];
					oWebSocketData.pwHopperNotificationDto.hopperCount = 0;
				}
				break;
			case "BypassLog":
				if (oTriggerPoint === "External" && oContext) {
					for (i; i < oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList.length; i++) {
						if (oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList[i].objectId === oContext.objectId) {
							oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList.splice(i, 1);
							oWebSocketData.byPassLogNotificationDto.byPassLogCount = oWebSocketData.byPassLogNotificationDto.byPassLogCount - 1;
							break;
						}
					}
				} else if (!oTriggerPoint) {
					oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList = [];
					oWebSocketData.byPassLogNotificationDto.byPassLogCount = 0;
				}
				break;
			case "EnergyIso":
				if (oTriggerPoint === "External" && oContext) {
					for (i; i < oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList.length; i++) {
						if (oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList[i].objectId === oContext.objectId) {
							oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList.splice(i, 1);
							oWebSocketData.energyIsolationNotificationDto.energyIsolationCount = oWebSocketData.energyIsolationNotificationDto.energyIsolationCount -
								1;
							break;
						}
					}
				} else if (!oTriggerPoint) {
					oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList = [];
					oWebSocketData.energyIsolationNotificationDto.energyIsolationCount = 0;
				}
				break;
			case "ShiftHandover":
				if (!oTriggerPoint) {
					var aBypassList = oDashBoardModel.getProperty("/bypassObj/bypassList");
					for (var k = 0; k < oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList.length; k++) { //AN: loop to filter out data and create a new array for acknowledging
						if (oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList[k].status !== "Created") {
							oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList.splice(k, 1);
							oWebSocketData.byPassLogNotificationDto.byPassLogCount = oWebSocketData.byPassLogNotificationDto.byPassLogCount - 1;
						}
						// else if (oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList[k].status === "Created") {
						// 	for (var j = 0; j < aBypassList.length; j++) {
						// 		if (oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList[k].objectId === aBypassList[j].ssdBypassId) {
						// 			break;
						// 		} else if (j === aBypassList.length) {
						// 			oWebSocketData.byPassLogNotificationDto.byPassLogNotificationList.splice(k, 1);
						// 			oWebSocketData.byPassLogNotificationDto.byPassLogCount = oWebSocketData.byPassLogNotificationDto.byPassLogCount - 1;
						// 			break;
						// 		}
						// 	}
						// }
					}
					for (var k = 0; k < oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList.length; k++) {
						if (oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList[k].status !== "Created") {
							oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList.splice(k, 1);
							oWebSocketData.energyIsolationNotificationDto.energyIsolationCount = oWebSocketData.energyIsolationNotificationDto.energyIsolationCount -
								1;
						}
						// else if (oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList[k].status === "Created") {
						// 	for (var j = 0; j < aBypassList.length; j++) {
						// 		if (oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList[k].objectId === aBypassList[j].ssdBypassId) {
						// 			break;
						// 		} else if (j === aBypassList.length) {
						// 			oWebSocketData.energyIsolationNotificationDto.energyIsolationNotificationList.splice(k, 1);
						// 			oWebSocketData.energyIsolationNotificationDto.energyIsolationCount = oWebSocketData.energyIsolationNotificationDto.energyIsolationCount -
						// 				1;
						// 			break;
						// 		}
						// 	}
						// }
					}
				}
				break;
			default:
				break;
			}
			oWebSocketData.notifTotalCount = this.fnGetTotalNotifByRole(oWebSocketData);
			// oWebSocketData.notifTotalCount = oWebSocketData.fracNotificationDto.fracCount + oWebSocketData.taskNotificationDto.taskCount +
			// 	oWebSocketData.alarmNotificationDto.alarmCount + oWebSocketData.pwHopperNotificationDto.hopperCount + oWebSocketData.byPassLogNotificationDto
			// 	.byPassLogCount + oWebSocketData.energyIsolationNotificationDto.energyIsolationCount;
			if (oWebSocketData.notifTotalCount === 0) {
				oWebSocketData.notifTotalCount = null;
			}
			oDashBoardModel.refresh(true);
		},
		getSystemTimeZone: function () { //AN: #TimeZone 
			if (new Date().toString().split("(")[1].split(")")[0]) {
				var sCurrTimeZoneTemp = new Date().toString().split("(")[1].split(")")[0];
			}
			var sCurrTimeZone = "";
			for (var i = 0; i < sCurrTimeZoneTemp.split(" ").length; i++) {
				sCurrTimeZone += sCurrTimeZoneTemp.split(" ")[i].charAt(0);
			}
			return sCurrTimeZone;
		},
		onTimeZoneChange: function () { //AN: #TimeZone 
			var oDashBoardModel = this.getModel("dashBoardModel");
			var currentSelectKey = oDashBoardModel.getProperty("/panelProperties/currentSelectKey");
			switch (currentSelectKey) {
			case "alarms":
				// oDashBoardModel.setProperty("/busyIndicators/alarmListBusy", true);
				this.getModel("oReportDetailModel").setProperty("/alarmList", []); //AN: to change the binding, so that the formatter is invoked again
				this.getModel("oReportDetailModel").setProperty("/alarmList", this.getModel("oReportDetailModel").getProperty("/alarmListOriginal"));
				// this.getAlarmData();
				break;
			}
		}
	});

});