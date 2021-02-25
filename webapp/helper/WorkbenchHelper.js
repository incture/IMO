sap.ui.define(["sap/m/MessageToast",
	"sap/m/MessageBox",
	"sap/m/Button",
	"sap/m/Dialog",
	"sap/m/Text",
	"sap/m/TextArea",
	"com/sap/incture/Incture_IOP/helper/ChangeSeatHelper", //AN: #msgToROC
	"sap/ui/model/json/JSONModel" //AN: #msgToROC
], function (MessageToast, MessageBox, Button, Dialog, Text, TextArea, ChangeSeatHelper, JSONModel) {
	"use strict";
	return {
		/** 
		 * Function to initialize model for Workbench Module
		 * @param {Object} oDashBoardModel- dashBoardModel
		 */
		_initilizeLocalModelForWorkbench: function (oDashBoardScope, oDashBoardModel) { //AN: #msgToROC
			var oUserData = oDashBoardModel.getProperty("/userData");
			oDashBoardModel.setProperty("/InsightToActionData", {
				iTaDraftData: [],
				iTaBacklogData: [],
				obxDraftData: [],
				msgDraftData: [], //AN: #msgToROC
				msgDraftDetailData: {}, //AN: #msgToROC
				iopWBBusyInd: false,
				iopWBMsgInd: false, //AN: #msgToROC
				iopWBMsgRejectInd: false, //AN: #msgToROC
				iopWBMsgLocationsCntryInd: false, //AN: #msgToROC
				iopWBMsgLocationsFieldInd: false, //AN: #msgToROC
				iopWBMsgLocationsFacilityInd: false, //AN: #msgToROC
				iopWBMsgLocationsWellPadInd: false, //AN: #msgToROC
				iopWBMsgLocationsWellInd: false, //AN: #msgToROC
				selectedKey: "wbDraft", //AN: #wbBacklog
				toggleViewDraft: "Table View", //AN: #wbBacklog
				toggleViewIconDraft: "sap-icon://table-view", //AN: #wbBacklog
				draftGridView: true,
				toggleViewBacklog: "Table View", //AN: #wbBacklog
				toggleViewIconBacklog: "sap-icon://table-view", //AN: #wbBacklog
				backlogGridView: true,
				oCurrSelWBMsgData: {}, //AN: #msgToROC
				bEmptyLoc: true, //AN: #msgToROC
				isWBInquiry: false, //AN: #msgToROC
				isWBDispatch: false, //AN: #msgToROC
				isWBInvestigation: false, //AN: #msgToROC
				hierarchyDetails: [], //AN: #msgToROC
				selectedCountry: "",
				selectedField: "",
				selectedFacility: "",
				selectedWellPad: "",
				selectedWell: "",
				regExp: "$#&#$", //AN: #msgToROC
				isSubmitEnabled: false, //AN: #msgToROC
				selectedIndex: -1, //AN: #msgToROC
				selectedRadioGrpKey: "", //AN: #msgToROC
				// currSelectedLocation: "",
				// currSelectedLocationCode: "",
				oWbMsgLocData: [{
						"Location": "Briggs 10H",
						"LocationCode": "MUR-US-EFS-CT00-BNWP-P011-W01",
						"LocationType": "Well"
					}, {
						"Location": "Briggs 11H",
						"LocationCode": "MUR-US-EFS-CT00-BNWP-P011-W02",
						"LocationType": "Well"
					}] //AN: #msgToROC
			});
			this.oPageIndex = 1; //AN: #msgToROC
			this.oPageSize = 20; //AN: #msgToROC
			sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavLeftButton").setEnabled(true); //AN: #msgToROC
			sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavRightButton").setEnabled(true); //AN: #msgToROC
			/*If only POT is logged in*/
			
			/*AN: Start of #prod-msgToROC 17-11-2020*/
			// if (!oUserData.isROC && oUserData.isPOT) { //AN: #wbBacklog
			// 	oDashBoardModel.setProperty("/InsightToActionData/selectedKey", "wbMessage");
			// 	oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
			// 	this.fnGetMsgDraftData(oDashBoardScope, oDashBoardModel);
			// }
			/*AN: End of #prod-msgToROC 17-11-2020*/
		},
		fetchWorkbenchList: function (oDashBoardScope, oDashBoardModel) {
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			var aSelectedLocs = oDashBoardModel.getProperty("/hierarchyDetails/currentSelectedObject");
			oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
			if (aSelectedLocs && aSelectedLocs.length > 0) {
				var aLocations = [];
				var sLocationCode = "";
				for (var i = 0; i < aSelectedLocs.length; i++) {
					aLocations.push(aSelectedLocs[i].location);
				}
				if (aLocations.length > 1) {
					sLocationCode = aLocations.join(", ");
				} else {
					sLocationCode = aLocations[0];
				}
				var oChangeSeatData = oDashBoardModel.getProperty("/changeSeatData");
				var sUrl = "/taskmanagementRest/workbench/getTaskList?";
				var techRoles;
				if (oDashBoardModel.getProperty("/isWebReadOnlyRole") || oDashBoardModel.getProperty("/moduleReadOnly/isWorkbenchReadOnly")) {
					techRoles = "IOP_TM_ROC_Catarina,IOP_TM_ROC_WestTilden,IOP_TM_ROC_CentralTilden,IOP_TM_ROC_Karnes,IOP_POT_East,IOP_POT_West";
				} else {
					techRoles = oDashBoardScope.getLoggedInTechRoles(oDashBoardModel);
				}
				var oUrlParameters = {
					sSortingOrder: "descending",
					sSortObject: "",
					sGroupObject: "none",
					sTechRole: techRoles,
					sBizRole: oChangeSeatData.changeSeatIOPUserDisplayRoles,
					sLocationCode: sLocationCode,
					sLocationType: aSelectedLocs[0].locationType
				};
				sUrl = sUrl + "sortingOrder=" + oUrlParameters.sSortingOrder + "&sortObject=" + oUrlParameters.sSortObject + "&groupObject=" +
					oUrlParameters.sGroupObject + "&technicalRole=" + oUrlParameters.sTechRole + "&locationCode=" + oUrlParameters.sLocationCode +
					"&locationType=" + oUrlParameters.sLocationType;
				if (oInsightToActionData.selectedKey === "wbDraft") {
					sUrl = sUrl + "&status=Draft";
				} else if (oInsightToActionData.selectedKey === "wbBacklog") {
					sUrl = sUrl + "&status=New";
				} else if (oInsightToActionData.selectedKey === "obxtask") {
					sUrl = sUrl + "&status=RETURNED&isObx=TRUE";
				}
				oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
					if (oData.responseMessage && oData.responseMessage.statusCode === "0") {
						if (oInsightToActionData.selectedKey === "wbDraft") {
							oInsightToActionData.iTaDraftData = oData.workBenchDtoList;
							for (var a = 0; a < oInsightToActionData.iTaDraftData.length; a++) {
								if (oInsightToActionData.iTaDraftData[a].createdOn) {
									oInsightToActionData.iTaDraftData[a].createdAtInString = oDashBoardScope.formatter.iopDateFormatTimeStampValueDateTime(
										oInsightToActionData.iTaDraftData[a].createdOn);
								}
							}
						} else if (oInsightToActionData.selectedKey === "wbBacklog") {
							oInsightToActionData.iTaBacklogData = oData.workBenchDtoList;
							for (var c = 0; c < oInsightToActionData.iTaBacklogData.length; c++) {
								if (oInsightToActionData.iTaBacklogData[c].createdOn) {
									oInsightToActionData.iTaBacklogData[c].createdAtInString = oDashBoardScope.formatter.iopDateFormatTimeStampValueDateTime(
										oInsightToActionData.iTaBacklogData[c].createdOn);
								}
							}
						} else if (oInsightToActionData.selectedKey === "obxtask") {
							oInsightToActionData.obxDraftData = oData.workBenchDtoList;
							for (var c = 0; c < oInsightToActionData.obxDraftData.length; c++) {
								if (oInsightToActionData.obxDraftData[c].createdOn) {
									oInsightToActionData.obxDraftData[c].createdAtInString = oDashBoardScope.formatter.iopDateFormatTimeStampValueDateTime(
										oInsightToActionData.obxDraftData[c].createdOn);
								}
							}
						}
						oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", false);
					}
				}, function (oError) {
					oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", false);
					oDashBoardScope._createConfirmationMessage("Error", oError.getParameter("statusText"), "Error", "", "Close", false, null);
				});
			} else {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", false);
				oDashBoardModel.setProperty("/InsightToActionData/iTaDraftData", []);
				oDashBoardModel.setProperty("/InsightToActionData/iTaBacklogData", []);
				oDashBoardModel.setProperty("/InsightToActionData/obxDraftData", []);
				// oDashBoardModel.setProperty("/InsightToActionData/msgDraftData", []); //AN: #msgToROC
			}
		},

		fnGetMsgDraftData: function (oDashBoardScope, oDashBoardModel) { //AN: #msgToROC
			var oUserData = oDashBoardModel.getProperty("/userData");
			var sCountry = oDashBoardModel.getProperty("/currentLocationInHierarchy");
			var sCntry = "US";
			if (sCountry === "CA") {
				sCntry = 'CA';
			}
			var loggedInUser = oDashBoardModel.getProperty("/userData/userId");
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			// oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
			var sUrl = "/taskmanagementRest/message/getAllActiveMessages?user=" + loggedInUser + "&pageNo=" + this.oPageIndex;
			if (oUserData.isROC && oUserData.isPOT) {
				sUrl = sUrl + "&userType=" + ["ROC", "POT"];
			} else if (oUserData.isROC) {
				sUrl = sUrl + "&userType=" + ["ROC"];
			} else if (oUserData.isPOT) {
				sUrl = sUrl + "&userType=" + ["POT"];
			}

			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", false);
				if (oData.response && oData.response.statusCode === "0") {
					oInsightToActionData.msgDraftData = oData.messageList;
					for (var c = 0; c < oInsightToActionData.msgDraftData.length; c++) {
						if (oInsightToActionData.msgDraftData[c].createdAt) {
							oInsightToActionData.msgDraftData[c].createdAtInString = oDashBoardScope.formatter.iopDateFormatTimeStampValueDateTime(
								oInsightToActionData.msgDraftData[c].createdAt);
							oInsightToActionData.msgDraftData[c].messageIdInString = oInsightToActionData.msgDraftData[c].messageId.toString();
						}
					}
					if (oInsightToActionData.msgDraftData && oInsightToActionData.msgDraftData.length > 0) {
						sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBPageNumberDiv").setVisible(true);
					} else {
						sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBPageNumberDiv").setVisible(false);
					}
					var oLastIndex = this.oPageSize * this.oPageIndex;
					var oStartIndex = oLastIndex - (this.oPageSize - 1);
					if (oLastIndex > oData.totalCount) {
						oLastIndex = oData.totalCount;
					}
					if (!oInsightToActionData.msgDraftData.length) {
						oStartIndex = 0;
					}
					sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgRange").setText("Showing " + oStartIndex + "-" +
						oLastIndex + " of " + oData.totalCount + " messages");
					if (this.oPageIndex === 1) {
						sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavLeftButton").setEnabled(false);
					}
					if (oData.totalCount === oLastIndex) {
						sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavRightButton").setEnabled(false);
					}
					oDashBoardModel.refresh(true);
				} else {
					MessageToast.show(oData.response.message);
				}
			}.bind(this), function (oError) {
				oDashBoardModel.setProperty("/InsightToActionData/msgDraftData", []);
				oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", false);
				oDashBoardScope._createConfirmationMessage("Error", oError.getParameter("statusText"), "Error", "", "Close", false, null);
			}.bind(this));
		},

		/** 
		 * Function to Search Tasks
		 * @param {Object} oDashBoardScope - this
		 * @param {Object} oEvent oInsightToActionData.msgDraftData.length.msgDraftData.length- oEvent
		 */
		onWBTasksSearch: function (oDashBoardScope, oEvent) {
			var value = oEvent.getSource().getValue();
			var selectedKey = oDashBoardScope.getModel("dashBoardModel").getProperty("/InsightToActionData/selectedKey"); //AN: #wbBacklog
			var oDTList, oDTable; //AN: #wbBacklog
			var filterParams = ["taskLocationText", "createdBy", "taskType", "tier", "createdAtInString", "locationType", "status"]; //AN: #msgToROC
			if (selectedKey === "wbDraft") { //AN: #wbBacklog
				oDTList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBGrid");
				oDTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbDraftTable");
			} else if (selectedKey === "wbBacklog") { //AN: #wbBacklog
				oDTList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBBacklogGrid");
				oDTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbBacklogTable");
			} else if (selectedKey === "obxtask") {
				oDTList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBObxGrid");
				oDTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbObxDraftTable");
			} else if (selectedKey === "wbMessage") { //AN: #msgToROC
				oDTList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBMsgGrid");
				oDTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgTable");
				filterParams = ["locationText", "message", "currentOwner", "createdBy", "createdAtInString", "status", "messageIdInString"]; //AN: #msgToROC
			}
			var aFilters;

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
			oDTable.getBinding("items").filter(aFilters); //AN: #wbBacklog
		},

		/** 
		 * Function to open Sorting/Grouping Dialog
		 * @param {Object} oDashBoardScope - this
		 */
		onViewSettingsPress: function (oDashBoardScope) {
			var oFragmentId = "idWorkbenchViewSettingsDialog",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchViewSettings";
			if (!oDashBoardScope.oWBVeiwSettingsDialog) {
				oDashBoardScope.oWBVeiwSettingsDialog = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				oDashBoardScope.getView().addDependent(oDashBoardScope.oWBVeiwSettingsDialog);
			}
			oDashBoardScope.oWBVeiwSettingsDialog.open();
		},

		/** 
		 * function to apply sorting/grouping to the task items
		 * @param {Object,Object} oDashBoardScope,oEvent - this,oEvent
		 */
		//
		onWBViewSettingsConfirm: function (oDashBoardScope, oEvent) {
			var oTable, oGrid;
			oGrid = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBGrid");
			oTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbDraftTable");
			var mParams = oEvent.getParameters();
			var oBindingGrid = oGrid.getBinding("items"); //AN: #wbBacklog
			var oBindingTable = oTable.getBinding("items"); //AN: #wbBacklog
			// apply sorter to binding
			// (grouping comes before sorting)
			var sPath;
			var bDescending;
			var vGroup;
			var aSorters = [];
			if (mParams.groupItem) {
				sPath = mParams.groupItem.getKey();
				bDescending = mParams.groupDescending;
				var mGroupFunctions = {
					source: function (oContext) {
						var name = oContext.getProperty("taskSource");
						return {
							key: name,
							text: name
						};
					},
					taskType: function (oContext) {
						var name = oContext.getProperty("taskType");
						return {
							key: name,
							text: name
						};
					},
					taskLocationText: function (oContext) {
						var name = oContext.getProperty("taskLocationText");
						return {
							key: name,
							text: name
						};
					}
				};
				vGroup = mGroupFunctions[sPath];
				aSorters.push(new sap.ui.model.Sorter(sPath, bDescending, vGroup));
			}
			sPath = mParams.sortItem.getKey();
			bDescending = mParams.sortDescending;
			aSorters.push(new sap.ui.model.Sorter(sPath, bDescending));
			oBindingGrid.sort(aSorters); //AN: #wbBacklog
			oBindingTable.sort(aSorters); //AN: #wbBacklog
		},

		//Function triggered on Accepting the workbench task
		onAcceptWBTask: function (oDashBoardScope, oDashBoardModel, oCurrObj) {
			oCurrObj.action = "Accepted";
			this.updateWorkbenchStatus(oDashBoardScope, oDashBoardModel, oCurrObj);
		},

		//Function triggered on Rejecting the workbench task
		onRejectWBTask: function (oDashBoardScope, oDashBoardModel, oCurrObj) {
			var that = this;
			var dialog = new Dialog({
				title: "Rejection",
				type: "Message",
				state: "Error",
				content: [
					new Text({
						text: "Are you sure you want to reject this task?"
					}),
					new TextArea({
						width: "100%",
						placeholder: "Rejection Reason (Required)",
						liveChange: function (oEvent) {
							oEvent.getSource().setValueState("None");
						}
					})
				],
				beginButton: new Button({
					text: "Reject",
					press: function (oEvent) {
						var oRejReason = oEvent.getSource().getParent().getContent()[1].getValue();
						if (!oRejReason) {
							oEvent.getSource().getParent().getContent()[1].setValueState("Error");
						} else {
							oCurrObj.reason = oRejReason;
							that.onConfirmRejectTask(oDashBoardScope, oDashBoardModel, oCurrObj, dialog);
						}
					}
				}),
				endButton: new Button({
					text: "Cancel",
					press: function () {
						dialog.close();
					}
				}),
				afterClose: function () {
					dialog.destroy();
				}
			});

			dialog.open();
		},

		// Function triggred on Confirmation of Reject Task
		onConfirmRejectTask: function (oDashBoardScope, oDashBoardModel, oCurrObj, dialog) {
			oCurrObj.action = "Rejected";
			this.updateWorkbenchStatus(oDashBoardScope, oDashBoardModel, oCurrObj, dialog);
			dialog.close();
		},
		/** 
		 * Function to update the draft
		 * @param {Object} oDashBoardScope - this
		 * @param {Object} oDashBoardModel - model
		 * @param {Object} oCurrObj - selected object
		 * @param {Object} dialog - dialog data 
		 */
		updateWorkbenchStatus: function (oDashBoardScope, oDashBoardModel, oCurrObj, dialog) {
			var oUserData = oDashBoardModel.getProperty("/userData");
			var sUrl = "/taskmanagementRest/workbench/updateTaskStatus";
			var dateFormatted = new Date(oCurrObj.createdOn);

			var oDay = dateFormatted.getDay();
			var oMon = dateFormatted.getMonth();
			var oYear = dateFormatted.getFullYear();
			if (oDay < 10) {
				oDay = "0" + oDay;
			}
			if (oMon < 10) {
				oMon = "0" + oMon;
			}
			var oHour = dateFormatted.getHours();
			var oMin = dateFormatted.getMinutes();
			var oSec = dateFormatted.getSeconds();
			var oMerid = "";
			if (oHour > 12) {
				oMerid = "PM";
				oHour = oHour - 12;
			} else {
				oMerid = "AM";
			}
			if (oMin < 10) {
				oMin = "0" + oMin;
			}
			if (oSec < 10) {
				oSec = "0" + oSec;
			}

			var oPayload = {
				"taskId": oCurrObj.taskId,
				"taskType": oCurrObj.taskType,
				"tier": oCurrObj.tier,
				"locationCode": oCurrObj.taskLocCode,
				"locationType": oCurrObj.locationType,
				"source": oCurrObj.taskSource,
				"action": oCurrObj.action,
				"actionBy": oUserData.displayName,
				"actionByEmail": oUserData.userId,
				"createdAt": oYear + "-" + oMon + "-" + oDay + " " + oHour + ":" + oMin + ":" + oSec + " " + oMerid,
				/*"createdAt": oDashBoardScope.formatter.iopDateFormatTimeStampValueDateTime(oCurrObj.createdOn)*/
			};
			if (oPayload.action === "Rejected") {
				oPayload.reason = oCurrObj.reason;
			}
			oDashBoardScope.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData && oData.statusCode === "0") {
					MessageToast.show(oData.message);
					this.fetchWorkbenchList(oDashBoardScope, oDashBoardModel);
				}
			}.bind(this), function (oError) {
				oDashBoardScope._createConfirmationMessage("Error", oError.getParameter("statusText"), "Error", "", "Close", false, null);
			});
		},
		onWBCustListPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			var oCurrObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			if (oInsightToActionData.selectedKey === "wbMessage") {
				var oFragmentId = "idWBMsgToROCPanel",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchMsgToROCPanel";
				if (!oDashBoardScope.oWorkbenchMsgToROCPanel) {
					oDashBoardScope.oWorkbenchMsgToROCPanel = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
					oDashBoardScope.getView().addDependent(oDashBoardScope.oWorkbenchMsgToROCPanel);
				}
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData", oCurrObj);
				oDashBoardScope.oWorkbenchMsgToROCPanel.open();
			} else {
				var sBtnText = "Create Task";
				oDashBoardModel.setProperty("/taskId", oCurrObj.taskId);
				oDashBoardModel.setProperty("/processId", oCurrObj.processId);
				oDashBoardModel.setProperty("/taskStatus", "NEW");
				if (oCurrObj.taskType === "Dispatch") {
					oDashBoardModel.setProperty("/isInquiryCreate", false);
					oDashBoardModel.setProperty("/isInvestigationCreate", false);
				} else if (oCurrObj.taskType === "Inquiry") {
					oDashBoardModel.setProperty("/isInquiryCreate", true);
					oDashBoardModel.setProperty("/isInvestigationCreate", false);
				}
				oDashBoardScope.onCreateTaskPress("", sBtnText);
			}
		},
		onWBTabSelected: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #wbBacklog
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			if (oInsightToActionData.selectedKey === "wbMessage") {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
				this.fnGetMsgDraftData(oDashBoardScope, oDashBoardModel);
			} else {
				this.fetchWorkbenchList(oDashBoardScope, oDashBoardModel);
			}
		},
		onWbDraftToggleViewPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #wbBacklog
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			if (oInsightToActionData.toggleViewDraft === "Table View") {
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewDraft", "Grid View");
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewIconDraft", "sap-icon://grid");
				oDashBoardModel.setProperty("/InsightToActionData/draftGridView", false);
			} else if (oInsightToActionData.toggleViewDraft === "Grid View") {
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewDraft", "Table View");
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewIconDraft", "sap-icon://table-view");
				oDashBoardModel.setProperty("/InsightToActionData/draftGridView", true);
			}
		},
		onWbBacklogToggleViewPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #wbBacklog
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			if (oInsightToActionData.toggleViewBacklog === "Table View") {
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewBacklog", "Grid View");
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewIconBacklog", "sap-icon://grid");
				oDashBoardModel.setProperty("/InsightToActionData/backlogGridView", false);
			} else if (oInsightToActionData.toggleViewBacklog === "Grid View") {
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewBacklog", "Table View");
				oDashBoardModel.setProperty("/InsightToActionData/toggleViewIconBacklog", "sap-icon://table-view");
				oDashBoardModel.setProperty("/InsightToActionData/backlogGridView", true);
			}
		},
		onWBDraftRightPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #wbBacklog
			var isReadOnly = oDashBoardModel.getProperty("/isWebReadOnlyRole");
			var isWorkbenchReadOnly = oDashBoardModel.getProperty("/moduleReadOnly/isWorkbenchReadOnly");
			if (!isReadOnly && !isWorkbenchReadOnly) {
				var oContext = oEvent.getParameters();
				oDashBoardModel.setProperty("/InsightToActionData/currContext", oContext.src.getBindingContext("dashBoardModel").getObject());
				var oFragmentId = "idWBFragList",
					oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchActionList";
				if (!oDashBoardScope.wbActionList) {
					oDashBoardScope.wbActionList = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
					oDashBoardScope.getView().addDependent(oDashBoardScope.wbActionList);
				}
				oDashBoardScope.wbActionList.setOffsetX(-30);
				oDashBoardScope.wbActionList.openBy(oContext.target);
			}
		},
		/** 
		 * Function to open Sorting/Grouping Backlog Dialog
		 * @param {Object} oDashBoardScope - this
		 */
		onViewSettingsBacklogPress: function (oDashBoardScope) { //AN: #wbBacklog
			var oFragmentId = "idWorkbenchViewSettingsDialog",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchBacklogViewSettings";
			if (!oDashBoardScope.oWBVeiwSettingsBacklogDialog) {
				oDashBoardScope.oWBVeiwSettingsBacklogDialog = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				oDashBoardScope.getView().addDependent(oDashBoardScope.oWBVeiwSettingsBacklogDialog);
			}
			oDashBoardScope.oWBVeiwSettingsBacklogDialog.open();
		},
		/** 
		 * function to apply sorting/grouping to the backlog task items
		 * @param {Object,Object} oDashBoardScope,oEvent - this,oEvent
		 */
		//
		onWBViewSettingsBacklogConfirm: function (oDashBoardScope, oEvent) { //AN: #wbBacklog
			var oTable, oGrid;
			oGrid = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBBacklogGrid");
			oTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbBacklogTable");
			var mParams = oEvent.getParameters();
			var oBindingGrid = oGrid.getBinding("items");
			var oBindingTable = oTable.getBinding("items");
			// apply sorter to binding
			// (grouping comes before sorting)
			var sPath;
			var bDescending;
			var vGroup;
			var aSorters = [];
			if (mParams.groupItem) {
				sPath = mParams.groupItem.getKey();
				bDescending = mParams.groupDescending;
				var mGroupFunctions = {
					source: function (oContext) {
						var name = oContext.getProperty("taskSource");
						return {
							key: name,
							text: name
						};
					},
					taskType: function (oContext) {
						var name = oContext.getProperty("taskType");
						return {
							key: name,
							text: name
						};
					},
					taskLocationText: function (oContext) {
						var name = oContext.getProperty("taskLocationText");
						return {
							key: name,
							text: name
						};
					}
				};
				vGroup = mGroupFunctions[sPath];
				aSorters.push(new sap.ui.model.Sorter(sPath, bDescending, vGroup));
			}
			sPath = mParams.sortItem.getKey();
			bDescending = mParams.sortDescending;
			aSorters.push(new sap.ui.model.Sorter(sPath, bDescending));
			oBindingGrid.sort(aSorters);
			oBindingTable.sort(aSorters);
		},
		onWBMsgRadioBtnPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC
			oDashBoardModel.setProperty("/InsightToActionData/isSubmitEnabled", true);
			oDashBoardModel.setProperty("/InsightToActionData/selectedRadioGrpKey", oEvent.getSource().getSelectedButton().getText());
		},
		onPressWBMsgButtons: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC
			// var sCountry = oDashBoardModel.getProperty("/currentLocationInHierarchy");
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			var oMsgDraftDetailData = oDashBoardModel.getProperty("/InsightToActionData/msgDraftDetailData");
			var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
			var selectedRadioGrpKey = oDashBoardModel.getProperty("/InsightToActionData/selectedRadioGrpKey");
			var aTaskTypeDetails = [];
			switch (oEvent.getSource().getText()) {
			case "Submit":
				if (selectedRadioGrpKey === "Forward To POT") {
					if (this.fnCheckMndt(oDashBoardScope, oDashBoardModel)) {
						if (oCurrSelWBMsgData.locationType !== "Well") {
							oDashBoardScope._showToastMessage("Message cannot be forwarded with location type as: " + oCurrSelWBMsgData.locationType);
						} else {
							oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
							// this.fnWBMsgUpdateStatus(oDashBoardScope, oDashBoardModel, "ASSIGNED", "POT");
							this.fnWBMsgUpdateStatus(oDashBoardScope, oDashBoardModel, "NEW", "POT"); //SH: NewFlow
						}
					} else {
						MessageToast.show("Please select a location");
					}
				} else if (selectedRadioGrpKey === "Investigate") {
					if (this.fnCheckMndt(oDashBoardScope, oDashBoardModel)) {
						if (oCurrSelWBMsgData.locationType !== "Well") {
							oDashBoardScope._showToastMessage("Investigation cannot be created for " + oCurrSelWBMsgData.locationType);
						} else {
							// open investigate panel
							oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
							oDashBoardModel.setProperty("/InsightToActionData/isWBInvestigation", true);
							var oTaskTypeDetails = {
								oCurrSelectedLocation: oCurrSelWBMsgData,
								sLocCode: oCurrSelWBMsgData.locationCode,
								sLocType: oCurrSelWBMsgData.locationType,
								sLocText: oCurrSelWBMsgData.locationText
							};
							aTaskTypeDetails.push(oTaskTypeDetails);
							ChangeSeatHelper.checkAuthorization(oDashBoardScope, oDashBoardModel, aTaskTypeDetails, "Module-Investigation", "POT");
						}
					} else {
						MessageToast.show("Please select a location");
					}
				} else if (selectedRadioGrpKey === "Dispatch") {
					if (this.fnCheckMndt(oDashBoardScope, oDashBoardModel)) {
						if (oCurrSelWBMsgData.locationType === "Meter") {
							oDashBoardScope._showToastMessage("Task Cannot be created for Flare");
						} else {
							oDashBoardModel.setProperty("/taskStatus", "NEW");
							// oDashBoardModel.setProperty("/taskCreation", "Custom");
							oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
							oDashBoardModel.setProperty("/InsightToActionData/isWBDispatch", true);
							// oDashBoardScope.onCreateTaskPress();
							var oTaskTypeDetails = {
								oCurrSelectedLocation: oCurrSelWBMsgData,
								sLocCode: oCurrSelWBMsgData.locationCode,
								sLocType: oCurrSelWBMsgData.locationType,
								sLocText: oCurrSelWBMsgData.locationText
							};
							if (oCurrSelWBMsgData.locationType === "Compressor") {
								oTaskTypeDetails.sLocCode = oCurrSelWBMsgData.parentLocationCode;
								oTaskTypeDetails.sLocType = oCurrSelWBMsgData.parentLocationType;
								oTaskTypeDetails.location = oCurrSelWBMsgData.locationCode;
								aTaskTypeDetails.push(oTaskTypeDetails);
								oDashBoardScope.getCompressorParent("", aTaskTypeDetails);
							} else {
								aTaskTypeDetails.push(oTaskTypeDetails);
								ChangeSeatHelper.checkAuthorization(oDashBoardScope, oDashBoardModel, aTaskTypeDetails, "Module-Dispatch", "ROC");
							}
						}
					} else {
						MessageToast.show("Please select a location");
					}
				} else if (selectedRadioGrpKey === "Resolve") {

					if (oInsightToActionData.rejectReason) {
						oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
						this.fnWBMsgUpdateStatus(oDashBoardScope, oDashBoardModel, "RESOLVED", false);
					} else {
						MessageToast.show("Please mention a reason for resolution");
					}
				}
				break;
				// case "Forward To POT":
				// 	if (this.fnCheckMndt(oDashBoardScope, oDashBoardModel)) {
				// 		if (oCurrSelWBMsgData.locationType !== "Well") {
				// 			oDashBoardScope._showToastMessage("Message cannot be forwarded with location type as: " + oCurrSelWBMsgData.locationType);
				// 		} else {
				// 			oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
				// 			this.fnWBMsgUpdateStatus(oDashBoardScope, oDashBoardModel, "ASSIGNED", "POT");
				// 		}
				// 		// var oTaskTypeDetails = { //AN: #ChangeSeat
				// 		// 	oCurrSelectedLocation: oMsgDraftDetailData,
				// 		// 	sLocCode: oMsgDraftDetailData.locationCode,
				// 		// 	sLocType: "Well",
				// 		// 	sLocText: oMsgDraftDetailData.locationText
				// 		// };
				// 		// var aTaskTypeDetails = []; //AN: #ChangeSeat
				// 		// aTaskTypeDetails.push(oTaskTypeDetails); //AN: #ChangeSeat
				// 		// oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
				// 		// oDashBoardModel.setProperty("/InsightToActionData/isWBInquiry", true);
				// 		// ChangeSeatHelper.checkAuthorization(oDashBoardScope, oDashBoardModel, aTaskTypeDetails, "Module-Inquiry", "ROC", oEvent); //AN: #ChangeSeat
				// 	} else {
				// 		MessageToast.show("Please select a location");
				// 	}
				// 	break;
				// case "Reject":
				// call service to close the message
				// var oFragmentId = "idWorkbenchMsgRejectDialog",
				// 	oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchMsgRejectDialog";
				// if (!oDashBoardScope.oWorkbenchMsgRejectDialog) {
				// 	oDashBoardScope.oWorkbenchMsgRejectDialog = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				// 	oDashBoardScope.getView().addDependent(oDashBoardScope.oWorkbenchMsgRejectDialog);
				// }
				// oDashBoardScope.oWorkbenchMsgRejectDialog.open();
				// break;
				// case "Investigate":
				// 	if (this.fnCheckMndt(oDashBoardScope, oDashBoardModel)) {
				// 		if (oCurrSelWBMsgData.locationType !== "Well") {
				// 			oDashBoardScope._showToastMessage("Investigation cannot be created for " + oCurrSelWBMsgData.locationType);
				// 		} else {
				// 			// open investigate panel
				// 			oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
				// 			oDashBoardModel.setProperty("/InsightToActionData/isWBInvestigation", true);
				// 			// oDashBoardScope.onPressCreateInvestigation();
				// 			var oTaskTypeDetails = {
				// 				oCurrSelectedLocation: oCurrSelWBMsgData,
				// 				sLocCode: oCurrSelWBMsgData.locationCode,
				// 				sLocType: oCurrSelWBMsgData.locationType,
				// 				sLocText: oCurrSelWBMsgData.locationText
				// 			};
				// 			var aTaskTypeDetails = [];
				// 			aTaskTypeDetails.push(oTaskTypeDetails);
				// 			ChangeSeatHelper.checkAuthorization(oDashBoardScope, oDashBoardModel, aTaskTypeDetails, "Module-Investigation", "POT");
				// 		}
				// 	} else {
				// 		MessageToast.show("Please select a location");
				// 	}
				// 	break;
				// case "Dispatch":
				// 	if (this.fnCheckMndt(oDashBoardScope, oDashBoardModel)) {
				// 		if (oCurrSelWBMsgData.locationType === "Meter") {
				// 			oDashBoardScope._showToastMessage("Task Cannot be created for Flare");
				// 		} else {
				// 			oDashBoardModel.setProperty("/taskStatus", "NEW");
				// 			// oDashBoardModel.setProperty("/taskCreation", "Custom");
				// 			oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
				// 			oDashBoardModel.setProperty("/InsightToActionData/isWBDispatch", true);
				// 			// oDashBoardScope.onCreateTaskPress();
				// 			var oTaskTypeDetails = {
				// 				oCurrSelectedLocation: oCurrSelWBMsgData,
				// 				sLocCode: oCurrSelWBMsgData.locationCode,
				// 				sLocType: oCurrSelWBMsgData.locationType,
				// 				sLocText: oCurrSelWBMsgData.locationText
				// 			};
				// 			var aTaskTypeDetails = [];
				// 			aTaskTypeDetails.push(oTaskTypeDetails);
				// 			ChangeSeatHelper.checkAuthorization(oDashBoardScope, oDashBoardModel, aTaskTypeDetails, "Module-Dispatch", "ROC");
				// 		}
				// 	} else {
				// 		MessageToast.show("Please select a location");
				// 	}
				// 	break;
			default:
				this.onWBMsgClose(oDashBoardScope, oDashBoardModel);
				break;
			}
		},
		fnCheckMndt: function (oDashBoardScope, oDashBoardModel) { //AN: #msgToROC
			// var oSearchFieldLocations = sap.ui.core.Fragment.byId("idWBMsgToROCPanel",
			// 	"idWorkbenchMsgToROCDetailsPanel--idSearchLocations");
			var oMsgDraftDetailData = oDashBoardModel.getProperty("/InsightToActionData/msgDraftDetailData");
			// var sCurrSelectedLocation = oDashBoardModel.getProperty("/InsightToActionData/currSelectedLocation");
			var sCurrSelectedLocation = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData/locationText");
			// var bIsEmptyLoc = oDashBoardModel.getProperty("/InsightToActionData/bEmptyLoc");
			if (oMsgDraftDetailData.locationText || sCurrSelectedLocation) {
				return true;
				// } else if (sCurrSelectedLocation) {
				// } else if (sCurrSelectedLocation && !oMsgDraftDetailData.locationText) {
				// return true;
			} else {
				return false;
			}
		},
		onWBMsgClose: function (oDashBoardScope, oDashBoardModel) { //AN: #msgToROC
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData", {});
			oDashBoardModel.setProperty("/InsightToActionData/isWBInquiry", false);
			oDashBoardModel.setProperty("/InsightToActionData/isWBDispatch", false);
			oDashBoardModel.setProperty("/InsightToActionData/isWBInvestigation", false);

			/*AN: location hierarchy selection parameters*/
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails", []);
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", "");
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", "");
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedCountry", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedField", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedFacility", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWellPad", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
			oDashBoardModel.setProperty("/InsightToActionData/isSubmitEnabled", false);
			oDashBoardModel.setProperty("/InsightToActionData/selectedIndex", -1);
			oDashBoardModel.setProperty("/InsightToActionData/selectedRadioGrpKey", "");

			var oSearchFieldLocations = sap.ui.core.Fragment.byId("idWBMsgToROCPanel",
				"idWorkbenchMsgToROCDetailsPanel--idSearchLocations");
			oDashBoardScope.oWorkbenchMsgToROCPanel.close();
			// oSearchFieldLocations.setValue("");
			// oDashBoardModel.setProperty("/InsightToActionData/currSelectedLocation", "");
			// oDashBoardModel.setProperty("/InsightToActionData/currSelectedLocationCode", "");
			oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
			this.fnGetMsgDraftData(oDashBoardScope, oDashBoardModel);
			if (oDashBoardModel.getProperty("/InsightToActionData/rejectReason")) {
				oDashBoardModel.setProperty("/InsightToActionData/rejectReason", "");
			}
		},
		onBeforeWorkbenchMsgToROCPanelOpen: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC

			var aHierarchyDetails = oDashBoardModel.getProperty("/InsightToActionData/hierarchyDetails");
			var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
			if (aHierarchyDetails && !aHierarchyDetails.countries && !oCurrSelWBMsgData.locationText) {
				var oInitialDataPayload = {
					"locationType": "BASE",
					"navigate": "",
					"location": ""
				};
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/fields", []);
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/facilities", []);
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wellpads", []);
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", []);
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", "");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", "");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedCountry", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedField", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedFacility", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedWellPad", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsCntryInd", true);
				this.fnGetHeirarchyDetails(oDashBoardScope, oDashBoardModel, oInitialDataPayload);
			}

			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			// var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
			var oUserData = oDashBoardModel.getProperty("/userData");
			var isWorkbenchReadOnly = oDashBoardModel.getProperty("/moduleReadOnly/isWorkbenchReadOnly");
			var isWebReadOnly = oDashBoardModel.getProperty("/isWebReadOnlyRole");
			//call update status service, then 	//call readMsgService
			oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", true);
			if (oCurrSelWBMsgData.status.toLowerCase() === "new" && !isWorkbenchReadOnly && !isWebReadOnly) {
				var oUrl = "/taskmanagementRest/message/update";
				var oPayload = $.extend(true, {}, oCurrSelWBMsgData);
				if (!oCurrSelWBMsgData.currentOwner || oCurrSelWBMsgData.currentOwner === "ROC" || oCurrSelWBMsgData.currentOwner === "POT") {
					oPayload.currentOwner = oUserData.userId;
				}
				// oPayload.status = "IN PROGRESS";
				oPayload.status = "DRAFT"; //SH: NewFlow
				var oModel = new JSONModel();
				oModel.loadData(oUrl, JSON.stringify(oPayload), false, "POST", false, false, {
					"Content-Type": "application/json;charset=utf-8"
				});
			}

			var sUrl = "/taskmanagementRest/message/getMessage?messageId=" + oCurrSelWBMsgData.messageId;
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				if (oData.responseMessage && oData.responseMessage.statusCode === "0") {
					oInsightToActionData.msgDraftDetailData = oData.message;
					oInsightToActionData.msgDraftDetailData.hasInvestigation = oCurrSelWBMsgData.hasInvestigation;
					oInsightToActionData.msgDraftDetailData.hasDispatch = oCurrSelWBMsgData.hasDispatch;
					if (oInsightToActionData.msgDraftDetailData.status.toLowerCase() === "resolved" || oInsightToActionData.msgDraftDetailData.hasInvestigation ||
						oInsightToActionData.msgDraftDetailData.hasDispatch) {
						oDashBoardScope.oWorkbenchMsgToROCPanel.removeStyleClass("iopwbMsgDetailDialogClass");
						oDashBoardScope.oWorkbenchMsgToROCPanel.removeStyleClass("iopwbMsgDetailDialogClas1");
						oDashBoardScope.oWorkbenchMsgToROCPanel.addStyleClass("iopwbMsgDetailDialogClass2");
					} else if (oInsightToActionData.msgDraftDetailData.locationText) {
						oDashBoardScope.oWorkbenchMsgToROCPanel.removeStyleClass("iopwbMsgDetailDialogClass");
						oDashBoardScope.oWorkbenchMsgToROCPanel.removeStyleClass("iopwbMsgDetailDialogClass2");
						oDashBoardScope.oWorkbenchMsgToROCPanel.addStyleClass("iopwbMsgDetailDialogClass1");
					} else {
						oDashBoardScope.oWorkbenchMsgToROCPanel.removeStyleClass("iopwbMsgDetailDialogClass1");
						oDashBoardScope.oWorkbenchMsgToROCPanel.removeStyleClass("iopwbMsgDetailDialogClass2");
						oDashBoardScope.oWorkbenchMsgToROCPanel.addStyleClass("iopwbMsgDetailDialogClass");
					}
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
				}
			}, function (oError) {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
				oDashBoardScope._createConfirmationMessage("Error", oError.getParameter("statusText"), "Error", "", "Close", false, null);
			});
		},
		onSuggestLocations: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC
			var oSearchField = oEvent.getSource();
			var oSearchVal = oSearchField.getValue();
			if (oSearchVal.length >= 1) {
				var oSearchFieldLocations = sap.ui.core.Fragment.byId("idWBMsgToROCPanel",
					"idWorkbenchMsgToROCDetailsPanel--idSearchLocations");
				var filter = new sap.ui.model.Filter({
					path: "Location",
					operator: sap.ui.model.FilterOperator.Contains,
					value1: oSearchVal
				});
				// call getLocationForMsgToRoc service

				oSearchFieldLocations.getBinding("suggestionItems").filter(filter);
				oSearchField.suggest();
			}
		},
		onSearchLocations: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC
			var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
			var oItem = oEvent.getParameter("suggestionItem");
			if (oItem) {
				oDashBoardModel.setProperty("/InsightToActionData/bEmptyLoc", false);
				oCurrSelWBMsgData.locationText = oItem.getText(); //REMOVE LATER
				oCurrSelWBMsgData.locationType = "Well"; //REMOVE LATER
				oCurrSelWBMsgData.locationCode = oItem.getKey(); //REMOVE LATER
			} else {
				oDashBoardModel.setProperty("/InsightToActionData/bEmptyLoc", true);
			}
		},
		onWBMsgSearchLocations: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC
			var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
			var oFragmentId = "idWorkbenchMsgLocationsDialog",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchMsgLocationsDialog";
			if (!oDashBoardScope.oWorkbenchMsgLocationsDialog) {
				oDashBoardScope.oWorkbenchMsgLocationsDialog = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				oDashBoardScope.getView().addDependent(oDashBoardScope.oWorkbenchMsgLocationsDialog);
			}
			oDashBoardScope.oWorkbenchMsgLocationsDialog.openBy(oEvent.getSource());
		},
		/** 
		 * Function to open Sorting/Grouping Message Dialog
		 * @param {Object} oDashBoardScope - this
		 */
		onViewSettingsMessagePress: function (oDashBoardScope) { //AN: #msgToROC
			var oFragmentId = "idWorkbenchViewSettingsMsgDialog",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.workbenchMessageViewSettings";
			if (!oDashBoardScope.oWBVeiwSettingsMessageDialog) {
				oDashBoardScope.oWBVeiwSettingsMessageDialog = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				oDashBoardScope.getView().addDependent(oDashBoardScope.oWBVeiwSettingsMessageDialog);
			}
			oDashBoardScope.oWBVeiwSettingsMessageDialog.open();
		},
		/** 
		 * function to apply sorting/grouping to the Message task items
		 * @param {Object,Object} oDashBoardScope,oEvent - this,oEvent
		 */
		//
		onWBViewSettingsMessageConfirm: function (oDashBoardScope, oEvent) { //AN: #msgToROC
			var oTable, oGrid;
			oGrid = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWBMsgGrid");
			oTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgTable");
			var mParams = oEvent.getParameters();
			var oBindingGrid = oGrid.getBinding("items");
			var oBindingTable = oTable.getBinding("items");
			// apply sorter to binding
			// (grouping comes before sorting)
			var sPath;
			var bDescending;
			var vGroup;
			var aSorters = [];
			if (mParams.groupItem) {
				sPath = mParams.groupItem.getKey();
				bDescending = mParams.groupDescending;
				var mGroupFunctions = {
					locationText: function (oContext) {
						var name = oContext.getProperty("locationText");
						return {
							key: name,
							text: name
						};
					}
				};
				vGroup = mGroupFunctions[sPath];
				aSorters.push(new sap.ui.model.Sorter(sPath, bDescending, vGroup));
			}
			sPath = mParams.sortItem.getKey();
			bDescending = mParams.sortDescending;
			aSorters.push(new sap.ui.model.Sorter(sPath, bDescending));
			oBindingGrid.sort(aSorters);
			oBindingTable.sort(aSorters);
		},
		onWBMsgRejectOKPress: function (oDashBoardScope, oDashBoardModel) { //AN: #msgToROC
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			if (oInsightToActionData.rejectReason) {
				this.fnWBMsgUpdateStatus(oDashBoardScope, oDashBoardModel, "RESOLVED", false);
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgRejectInd", true);
			} else {
				MessageToast.show("Please mention a reason for rejection");
			}
		},
		onWBMsgRejectCancelPress: function (oDashBoardScope, oDashBoardModel) { //AN: #msgToROC
			oDashBoardScope.oWorkbenchMsgRejectDialog.close();
		},
		fnWBMsgUpdateStatus: function (oDashBoardScope, oDashBoardModel, sStatus, sChangedAssignee, oPayload) {
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			var sUrl = "/taskmanagementRest/message/update";
			if (!oPayload) {
				var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
				var oMsgDraftDetailData = oInsightToActionData.msgDraftDetailData;
				var oPayload = $.extend(true, {}, oMsgDraftDetailData);
				oPayload.status = sStatus;
				if (sChangedAssignee) { //AN: Forward to POT scenario
					oPayload.currentOwner = sChangedAssignee;
					oPayload.locationCode = oCurrSelWBMsgData.locationCode;
					if (oCurrSelWBMsgData.locationType === "Compressor") {
						oPayload.locationCode = oCurrSelWBMsgData.parentLocationCode;
					}
				} else { //AN: reject scenario
					oPayload.comment = oInsightToActionData.rejectReason;
				}
			}

			oDashBoardScope.doAjax(sUrl, "POST", oPayload, function (oData) {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
				if (oData && oData.statusCode === "0") {
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgRejectInd", false);
					if (sChangedAssignee === "POT" || sStatus.toLowerCase() === "resolved") {
						MessageToast.show(oData.message);
					}
					// if (sStatus.toLowerCase() === "resolved") {
					// 	oDashBoardScope.oWorkbenchMsgRejectDialog.close();
					// }
					oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
					this.fnGetMsgDraftData(oDashBoardScope, oDashBoardModel);
					this.onWBMsgClose(oDashBoardScope, oDashBoardModel);
				}
			}.bind(this), function (oError) {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgRejectInd", false);
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
				oDashBoardScope._createConfirmationMessage("Error", oError.getParameter("statusText"), "Error", "", "Close", false, null);
			});
		},
		onBeforeWBMsgLocationsDialogOpen: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #msgToROC
			var aHierarchyDetails = oDashBoardModel.getProperty("/InsightToActionData/hierarchyDetails");
			if (aHierarchyDetails && !aHierarchyDetails.countries) {
				var oInitialDataPayload = {
					"locationType": "BASE",
					"navigate": "",
					"location": ""
				};
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/fields", []);
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/facilities", []);
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wellpads", []);
				oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", []);
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", "");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", "");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedCountry", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedField", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedFacility", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedWellPad", "");
				oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsCntryInd", true);
				this.fnGetHeirarchyDetails(oDashBoardScope, oDashBoardModel, oInitialDataPayload);
			}
		},
		fnGetHeirarchyDetails: function (oDashBoardScope, oDashBoardModel, oInitialDataPayload) {
			var sUrl = "/taskmanagementRest/location/getLocation";
			oDashBoardScope.doAjax(sUrl, "POST", oInitialDataPayload, function (oData) {
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsCntryInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsFieldInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsFacilityInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsWellPadInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsWellInd", false);
					if (oData.message.statusCode === "0") {
						var oEmpty = {
							locationText: "",
							location: ""
						};
						if (oInitialDataPayload.locationType === "BASE" && !oInitialDataPayload.navigate) {
							oData.dto.locationHierarchy.reverse();
							oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/countries", oData.dto.locationHierarchy);
							this.onWBMSgLocCountryChange(oDashBoardScope, oDashBoardModel, oData.dto.locationHierarchy[0].location);
							oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", "");
							oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", "");
							oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", "");
						} else if (oInitialDataPayload.locationType === "BASE") {
							oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/fields", oData.dto.locationHierarchy);
							this.onWBMSgLocFieldChange(oDashBoardScope, oDashBoardModel, oData.dto.locationHierarchy[0].location);
							oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oData.dto.locationHierarchy[0].location);
							oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oData.dto.locationHierarchy[0].locationText);
							oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oData.dto.locationHierarchy[0].locationType);
						} else if (oInitialDataPayload.locationType === "Field") {
							oData.dto.locationHierarchy.unshift(oEmpty);
							oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/facilities", oData.dto.locationHierarchy);
						} else if (oInitialDataPayload.locationType === "Facility") {
							oData.dto.locationHierarchy.unshift(oEmpty);
							oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wellpads", oData.dto.locationHierarchy);
						} else if (oInitialDataPayload.locationType === "Well Pad") {
							oData.dto.locationHierarchy.unshift(oEmpty);
							oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", oData.dto.locationHierarchy);
						}

					}
				}.bind(this),
				function (oError) {
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsCntryInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsFieldInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsFacilityInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsWellPadInd", false);
					oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsWellInd", false);
				}.bind(this));
		},
		onWBMSgLocCountryChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sRegExp = oDashBoardModel.getProperty("/InsightToActionData/regExp");
			var oInitialDataPayload = {
				"locationType": "BASE",
				"navigate": "CHILD"
			};
			if (typeof oEvent === "string") {
				oInitialDataPayload.location = oEvent;
			} else {
				oInitialDataPayload.location = oEvent.getSource().getSelectedKey().split(sRegExp)[0].trim();
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", "");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", "");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", "");
			}
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/fields", []);
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/facilities", []);
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wellpads", []);
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", []);
			oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsFieldInd", true);
			oDashBoardModel.setProperty("/InsightToActionData/selectedField", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedFacility", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWellPad", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
			this.fnGetHeirarchyDetails(oDashBoardScope, oDashBoardModel, oInitialDataPayload);
		},
		onWBMSgLocFieldChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sRegExp = oDashBoardModel.getProperty("/InsightToActionData/regExp");
			var oInitialDataPayload = {
				"locationType": "Field",
				"navigate": "CHILD"
			};
			if (typeof oEvent === "string") {
				oInitialDataPayload.location = oEvent;
			} else {
				oInitialDataPayload.location = oEvent.getSource().getSelectedKey().split(sRegExp)[0].trim();
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oEvent.getSource().getSelectedKey().split(sRegExp)[
					0].trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oEvent.getSource().getSelectedItem().getText());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oEvent.getSource().getSelectedKey().split(sRegExp)[
					1].trim());
			}
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/facilities", []);
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wellpads", []);
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", []);
			oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsFacilityInd", true);
			oDashBoardModel.setProperty("/InsightToActionData/selectedFacility", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWellPad", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
			this.fnGetHeirarchyDetails(oDashBoardScope, oDashBoardModel, oInitialDataPayload);
		},
		onWBMSgLocFacilityChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sRegExp = oDashBoardModel.getProperty("/InsightToActionData/regExp");
			var oInitialDataPayload = {
				"locationType": "Facility",
				"navigate": "CHILD",
				"location": oEvent.getSource().getSelectedKey().split(sRegExp)[0].trim()
			};
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oEvent.getSource().getSelectedKey().split(sRegExp)[
				0].trim());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oEvent.getSource().getSelectedItem().getText());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oEvent.getSource().getSelectedKey().split(sRegExp)[
				1].trim());
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wellpads", []);
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", []);
			oDashBoardModel.setProperty("/InsightToActionData/selectedWellPad", "");
			oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/parentLocationType", oEvent.getSource().getSelectedKey().split(
				sRegExp)[1].trim());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/parentLocationCode", oEvent.getSource().getSelectedKey().split(
				sRegExp)[0].trim());
			if (oEvent.getSource().getSelectedKey() && oEvent.getSource().getSelectedKey().split(sRegExp)[1].trim() === "Facility") {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsWellPadInd", true);
				this.fnGetHeirarchyDetails(oDashBoardScope, oDashBoardModel, oInitialDataPayload);
			} else {
				var oIdLocField = sap.ui.core.Fragment.byId("idWBMsgToROCPanel", "idWorkbenchMsgToROCDetailsPanel--idLocField");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oIdLocField.getSelectedKey().split(sRegExp)[0].trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oIdLocField.getSelectedItem().getText().trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oIdLocField.getSelectedKey().split(sRegExp)[1].trim());
			}
		},
		onWBMSgLocWellPadChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sRegExp = oDashBoardModel.getProperty("/InsightToActionData/regExp");
			var oInitialDataPayload = {
				"locationType": "Well Pad",
				"navigate": "CHILD",
				"location": oEvent.getSource().getSelectedKey().split(sRegExp)[0].trim()
			};
			oDashBoardModel.setProperty("/InsightToActionData/hierarchyDetails/wells", []);
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oEvent.getSource().getSelectedKey().split(sRegExp)[
				0].trim());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oEvent.getSource().getSelectedItem().getText());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oEvent.getSource().getSelectedKey().split(sRegExp)[
				1].trim());
			oDashBoardModel.setProperty("/InsightToActionData/selectedWell", "");
			if (oEvent.getSource().getSelectedKey() && oEvent.getSource().getSelectedKey().split(sRegExp)[1].trim() === "Well Pad") {
				oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgLocationsWellInd", true);
				this.fnGetHeirarchyDetails(oDashBoardScope, oDashBoardModel, oInitialDataPayload);
			} else if (!oEvent.getSource().getSelectedKey().split(sRegExp)[1].trim()) { // could be a case of compressor / flare / meter, then ignore the  below
				var oIdLocFacility = sap.ui.core.Fragment.byId("idWBMsgToROCPanel", "idWorkbenchMsgToROCDetailsPanel--idLocFacility");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oIdLocFacility.getSelectedKey().split(sRegExp)[0]
					.trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oIdLocFacility.getSelectedItem().getText().trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oIdLocFacility.getSelectedKey().split(sRegExp)[1]
					.trim());

				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/parentLocationCode", oIdLocFacility.getSelectedKey().split(
					sRegExp)[0].trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/parentLocationType", oIdLocFacility.getSelectedKey().split(
					sRegExp)[1].trim());
			}
		},
		onWBMSgLocWellChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sRegExp = oDashBoardModel.getProperty("/InsightToActionData/regExp");
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oEvent.getSource().getSelectedKey().split(sRegExp)[
				1].trim());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oEvent.getSource().getSelectedKey().split(sRegExp)[
				0].trim());
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oEvent.getSource().getSelectedItem().getText());

			if (!oEvent.getSource().getSelectedKey().split(sRegExp)[0].trim()) {
				var oIdLocWellPad = sap.ui.core.Fragment.byId("idWBMsgToROCPanel", "idWorkbenchMsgToROCDetailsPanel--idLocWellPad");
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationCode", oIdLocWellPad.getSelectedKey().split(sRegExp)[0]
					.trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationText", oIdLocWellPad.getSelectedItem().getText().trim());
				oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData/locationType", oIdLocWellPad.getSelectedKey().split(sRegExp)[1]
					.trim());
			}
		},
		onWbMsgScrollLeftPage: function (oDashBoardScope, oDashBoardModel, oEvent) {
			this.oPageIndex -= 1;
			sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavLeftButton").setEnabled(true);
			sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavRightButton").setEnabled(true);
			oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
			this.fnGetMsgDraftData(oDashBoardScope, oDashBoardModel);
		},
		onWbMsgScrollRightPage: function (oDashBoardScope, oDashBoardModel, oEvent) {
			this.oPageIndex += 1;
			sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavLeftButton").setEnabled(true);
			sap.ui.core.Fragment.byId(oDashBoardScope.createId("workbenchFragment"), "idWbMsgNavRightButton").setEnabled(true);
			oDashBoardModel.setProperty("/InsightToActionData/iopWBBusyInd", true);
			this.fnGetMsgDraftData(oDashBoardScope, oDashBoardModel);
		},
		onWBIconPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var oInsightToActionData = oDashBoardModel.getProperty("/InsightToActionData");
			var oCurrObj = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			oDashBoardModel.setProperty("/InsightToActionData/oCurrSelWBMsgData", oCurrObj);
			var oUserData = oDashBoardModel.getProperty("/userData");
			var aTaskTypeDetails = [], //AN: #ChangeSeat
				oTaskTypeDetails = {}, //AN: #ChangeSeat
				sWho = "",
				oObject = oCurrObj;
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
			ChangeSeatHelper.checkAuthorization(oDashBoardScope, oDashBoardModel, aTaskTypeDetails, sTaskType, sWho); //AN: #ChangeSeat //AN: #inquiryEnhancement
			// if (oEvent.getSource().getTooltip() === "Investigation") {
			// 	oDashBoardModel.setProperty("/InsightToActionData/isWBInvestigation", true);
			// } 
			// else if (oEvent.getSource().getTooltip() === "Dispatch") {
			// 	oDashBoardModel.setProperty("/InsightToActionData/isWBDispatch", true);
			// }
		}

	};
});