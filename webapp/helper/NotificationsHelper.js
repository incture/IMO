sap.ui.define(["sap/m/MessageToast",
	"sap/m/MessageBox", "sap/ui/model/json/JSONModel"
], function (MessageToast, MessageBox, JsonModel) {
	"use strict";
	return {
		/** 
		 * Function to initialize model for Notifications feature
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		_initializeLocalModelForNotifications: function (oDashBoardScope, oDashBoardModel) {
			oDashBoardModel.setProperty("/webSocketProperty", {
				data: [],
				indicator: false,
				fracInd: false,
				tasksInd: false,
				alarmsInd: false,
				bypassLogInd: false,
				energyIsoInd: false,
				pwHopperInd: false
			});
			oDashBoardModel.setProperty("/shiftHandover", { //AN: #loaderWhileNotif
				busyInd: false
			});
		},
		/** 
		 * Function to handle Frac alert on closure
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onFracAlertClose: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/fracInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("Frac", oContext, "External");
			}
		},
		/** 
		 * Function to handle Frac List Item Press
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onFracListItemPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			oDashBoardModel.setProperty("/panelProperties/currentSelectKey", "fracMonitoring");
			oDashBoardModel.setProperty("/highlightFrac", []);
			oDashBoardScope.setSelectedModule();
			var sFracID = Number(oEvent.getSource().getBindingContext("dashBoardModel").getObject().fracId);
			oDashBoardModel.getData().highlightFrac.push(sFracID);
			oDashBoardScope.onSideNavigationItemSelect("external");
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/fracInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("Frac", oContext, "External");
			}
		},
		/** 
		 * Function to handle Task alert on closure
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onTaskAlertClose: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/tasksInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("Tasks", oContext, "External");
			}
		},
		/** 
		 * Function to handle Task List Item Press
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onTaskListItemPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			oDashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true);
			var sBtnText = "Create Task";
			if (oContext) {
				oDashBoardModel.setProperty("/taskId", oContext.objectId);
				if (oContext.module === "Dispatch") {
					oDashBoardModel.setProperty("/isInvestigationCreate", false);
					oDashBoardModel.setProperty("/isInquiryCreate", false);
					oDashBoardModel.setProperty("/taskStatus", "");
				} else if (oContext.module === "Investigation") {
					oDashBoardModel.setProperty("/isIconPress", true);
					oDashBoardModel.setProperty("/isInvestigationCreate", true);
					oDashBoardModel.setProperty("/isDispatch", false);
					oDashBoardModel.setProperty("/isInquiryCreate", false);
				} else if (oContext.module === "Inquiry") {
					oDashBoardModel.setProperty("/isIconPress", true);
					oDashBoardModel.setProperty("/isInvestigationCreate", false);
					oDashBoardModel.setProperty("/isDispatch", false);
					oDashBoardModel.setProperty("/isInquiryCreate", true);
					oDashBoardModel.setProperty("/taskOwner", "");
				}
				oDashBoardScope.onCreateTaskPress("", sBtnText);
				oDashBoardModel.setProperty("/webSocketProperty/tasksInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("Tasks", oContext, "External");
			}
		},
		/** 
		 * Function to handle Alarm alert on closure
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onAlarmAlertClose: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/alarmsInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("Alarms", oContext, "External");
			}
		},
		/** 
		 * Function to handle Alarm List Item Press
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onAlarmListItemPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			oDashBoardModel.setProperty("/panelProperties/currentSelectKey", "alarms");
			oDashBoardModel.setProperty("/highlightAlarms", []);
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardScope.setSelectedModule();
				oDashBoardModel.getData().highlightAlarms.push(oContext.objectId);
				oDashBoardScope.setSelectedLocationExternally(oContext);
				oDashBoardScope.onSideNavigationItemSelect("external");
				oDashBoardModel.setProperty("/webSocketProperty/alarmsInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("Alarms", oContext, "External");
			}
		},
		/** 
		 * Function to handle PW Hopper alert on closure
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onPwHopperAlertClose: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/pwHopperInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("PwHopper", oContext, "External");
			}
		},
		/** 
		 * Function to handle PW Hopper List Item Press
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onPwHopperListItemPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			oDashBoardModel.setProperty("/panelProperties/currentSelectKey", "pwhopper");
			oDashBoardModel.setProperty("/highlightPWHopper", []);
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardScope.setSelectedModule();
				oDashBoardModel.getData().highlightPWHopper.push(oContext.objectId);
				oDashBoardScope.setSelectedLocationExternally(oContext);
				oDashBoardScope.onSideNavigationItemSelect("external");
				oDashBoardModel.setProperty("/webSocketProperty/pwHopperInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("PwHopper", oContext, "External");
			}
		},
		/** 
		 * Function to handle Bypass Log alert on closure
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onBypassLogAlertClose: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/bypassLogInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("BypassLog", oContext, "External");
			}
		},
		/** 
		 * Function to handle Energy isolation alert on closure
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onEnergyIsoAlertClose: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oContext = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oContext) {
				oDashBoardModel.setProperty("/webSocketProperty/energyIsoInd", true); //AN: #loaderWhileNotif
				oDashBoardScope.acknowledgeWebSocketRequest("EnergyIso", oContext, "External");
			}
		},
		/** 
		 * Function to Open Notifications panel
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onOpenNotification: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sStatus = oDashBoardModel.getData().showNotificationFragment;
			if (sStatus) {
				oDashBoardModel.getData().showNotificationFragment = false;
			} else {
				oDashBoardModel.getData().showNotificationFragment = true;
			}
			oDashBoardModel.refresh(true);
			var oUrl = "/taskmanagementRest/ShiftRegister/getShiftDetails?emp_email=neelam.raj@incture.com";
			oDashBoardScope.doAjax(oUrl, "GET", null, function (oData) {
				debugger
				if (oData.responseMessage.statusCode === "0") {
				} else {
					oDashBoardScope._showToastMessage(oData.responseMessage.message);
				}
			}.bind(this), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._showToastMessage(sErrorMessage);
			}.bind(this));
		}
	};
});