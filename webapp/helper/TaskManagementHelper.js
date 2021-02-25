sap.ui.define(["sap/m/MessageToast",
	"sap/m/MessageBox", "sap/ui/model/json/JSONModel"
], function (MessageToast, MessageBox, JsonModel) {
	"use strict";
	return {
		/** 
		 * Function to initialize model for Task Management feature
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		_initializeLocalModelForTaskManagementPanel: function (oDashBoardScope, oDashBoardModel) {
			oDashBoardModel.setProperty("/createTaskTitle", true);
			oDashBoardModel.setProperty("/createTaskScheduling", false);
			oDashBoardModel.setProperty("/createTaskHistory", true);
			oDashBoardModel.setProperty("/createTaskPanelVisible", false);
			oDashBoardModel.setProperty("/taskManagementPanel", {
				"showTaskManagementPanel": false
			});
			oDashBoardModel.setProperty("/taskFilterData", { //AN: #taskFilter
				locationType: [{
					key: "Fld",
					text: "Field"
				}, {
					key: "Fa",
					text: "Facility"
				}, {
					key: "WP",
					text: "Well Pad"
				}, {
					key: "W",
					text: "Well"
				}],
				location: [],
				taskType: [],
				status: [],
				classification: [],
				taskClassification: [],
				subClassification: [],
				issueClassification: [],
				// tempIssueClassification: [],
				assignedTo: [],
				createdOn: [],
				createdBy: [], //AN: #taskV2Demo
				fields: [{
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
				}],
				tempFields: [{ //AN: #ChangeSeat
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
				}],
				arrAttr: {
					isTaskClassificationVisible: true,
					isSubClassificationVisible: true,
					isIssueClassificationVisible: true
				},
				selectedField: "", //AN: #taskFieldFilter
				selectedFieldKey: "None", //AN: #taskFieldFilter
				selectedTaskType: "",
				selectedLocation: "",
				selectedTaskClassification: "",
				selectedSubClassification: "",
				selectedStatus: "",
				selectedIssueClassification: "",
				selectedCreatedOn: "",
				selectedAssignedTo: "",
				selectedCreatedBy: "", //AN: #taskV2Demo
				isFilterPanelExpanded: false,
				isFilterApplied: false
			});
			var maxDateEpoch = new Date().getTime();
			var minDate = new Date(maxDateEpoch - 30 * 86400000);
			oDashBoardModel.setProperty("/handoverFilterData", { //SH: HandoverNotes
				fromDate: "",
				toDate: "",
				shift: [{
					key: "day",
					text: "Day",
				}, {
					key: "night",
					text: "Night",
				}],
				fields: [{ //AN: #ChangeSeat
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
				}],
				selectedField: "",
				selectedFieldKey: "",
				selectedShift: "",
				selectedShiftKey: "",
				isFilterPanelExpanded: false,
				isFilterApplied: false,
				handoverMinDate: minDate,
				handoverMaxDate: new Date()
			});
		},
		/** 
		 * Function to fetch and bind the task management panel data (The Task List)
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {String} sTaskType - Defines Task type ("All")
		 */
		_bindRightTaskPanelModel: function (oDashBoardScope, sTaskType) {
			var oDashBoardModel = oDashBoardScope.getModel("dashBoardModel"); //AN: #inquire
			var sUserId = oDashBoardModel.getProperty("/userData/userId"),
				sUserName = oDashBoardModel.getProperty("/userData/displayName"),
				sGroup = oDashBoardModel.getProperty("/userData/resGroupRead"),
				sPotGroup = oDashBoardModel.getProperty("/userData/potRole"),
				sEngGroup = oDashBoardModel.getProperty("/userData/engRole"), //AN: #Inquire
				bIsCreatedByMe = oDashBoardModel.getProperty("/changeSeatData/changeSeatIOPUserRoleCreatedByMe"), //AN: #ChangeSeat
				sUrl, sUserType;
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData"); //AN: #taskFieldFilter
			if (sTaskType === "Non-Dispatch") {
				sUrl = "/taskmanagementRest/nonDispatch/readByLocation?group=" + sGroup + "&userType=";
			} else {
				if (sPotGroup !== "") {
					if (sGroup !== "") {
						sGroup = sGroup + "," + sPotGroup;
					} else {
						sGroup = sPotGroup;
					}
				}
				if (sEngGroup !== "") { //AN: #Inquire
					if (sGroup !== "") {
						sGroup = sGroup + "," + sEngGroup;
					} else {
						sGroup = sEngGroup;
					}
				}
				//var taskFilterData = oDashBoardModel.getProperty("/taskFilterData"); //RV : bug fix for non-dispatch task click
				if (taskFilterData.selectedField) { //AN: #taskFieldFilter
					sUrl = "/taskmanagementRest/tasks/getTasksByUser?group=" + taskFilterData.selectedFieldKey + "&userType=";
				} else {
					sUrl = "/taskmanagementRest/tasks/getTasksByUser?group=" + sGroup + "&userType=";
				}
			}
			oDashBoardModel.getData().userData.sGroup = sGroup; //AN: #inquire
			if (oDashBoardModel.getProperty("/userData/isROC")) {
				sUserType = "ROC";
				if (sGroup === "") {
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					return;
				}
				sUrl = sUrl + sUserType + "&userId=" + sUserId; //AN: #inquire
			} else if (oDashBoardModel.getProperty("/userData/isENG")) {
				sUserType = "ENG";
				if (sGroup === "") { //AN: #inquire
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					return;
				}
				if (sTaskType === "Sent Items") { //AN: #inquire
					sUrl = sUrl + sUserType + "&userId=" + sUserId + "&origin=Inquiry";
				} else {
					sUrl = sUrl + sUserType + "&userId=" + sUserId;
				}
			} else if (oDashBoardModel.getProperty("/userData/isPOT")) {
				sUserType = "POT";
				if (sGroup === "") {
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					return;
				}
				sUrl = sUrl + sUserType + "&userId=" + sUserId; //AN: #inquire
			}
			sUrl = sUrl + "&locationCode=" + taskFilterData.selectedField;
			sUrl = sUrl + "&isCreatedByMe=" + bIsCreatedByMe; //AN: #ChangeSeat
			if (sTaskType !== "All") { //AN: #highlightShift
				oDashBoardModel.setProperty("/highlightTasks", []);
			}
			if (!oDashBoardModel.getProperty("/isWebReadOnlyRole")) {
				oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
					var aNewTaskList = [];
					var taskFilterData = oDashBoardModel.getProperty("/taskFilterData"); //AN: #taskFilter
					this.refreshTaskFilters(oDashBoardScope, oDashBoardModel); //AN: #taskFilter
					var oAckData = oDashBoardModel.getData().highlightTasks; //AN: #highlightShift
					if (sTaskType === "All") {
						$.each(oData.taskList, function (index, value) {
							if ((value.parentOrigin === "Pigging" && value.createdByEmailId === "SYSTEM")) {
								value.status = "NEW"; //AN: #piggingChange
								value.createdOn = this.convertTaskCreatedAt(oDashBoardScope, value.createdAtInString); //AN: #taskFilter
								if (value.location) {
									value.location = value.location.trim(); //AN:#obxSearch
								}
								aNewTaskList.push(value);
								this.createTaskFilterParameters(oDashBoardScope, oDashBoardModel, value); //AN: #taskFilter
							} else if ((value.commentedByDisplay && value.commentedByDisplay !== sUserName) || value.status === "RESOLVED" || value.status ===
								"RETURNED" || value.status === "SUBMITTED" || ((value.status === "ASSIGNED" || value.status === "IN PROGRESS") &&
									value.taskOwner &&
									value.taskOwner.includes(
										sUserName))) {
								value.createdOn = this.convertTaskCreatedAt(oDashBoardScope, value.createdAtInString); //AN: #taskFilter
								if (value.location) {
									value.location = value.location.trim(); //AN:#obxSearch
								}
								aNewTaskList.push(value);
								this.createTaskFilterParameters(oDashBoardScope, oDashBoardModel, value); //AN: #taskFilter
							}
						}.bind(this)); //AN: #taskFilter
						oDashBoardModel.setProperty("/taskList", aNewTaskList);
						try { //AN: #highlightShift
							if (oAckData && aNewTaskList.length > 0) {
								for (var i = 0; i < oAckData.length; i++) {
									for (var j = 0; j < aNewTaskList.length; j++) {
										if (oAckData[i] == aNewTaskList[j].taskId) {
											aNewTaskList[j].tasksNotification = "notified";
											break;
										}
									}
								}
							}
						} catch (e) {

						}
						this.removeDuplicateTaskFilterParameters(oDashBoardScope, oDashBoardModel); //AN: #taskFilter
					} else {
						if (oData.taskList) { //AN: #inquire (if NO Data is there)
							if (oData.taskList[0].creatorGroupId) { //AN: #inquire (For SENT-ITEMS task)
								$.each(oData.taskList, function (index, value) { //AN: #inquire
									var sGroups = sGroup.split(",");
									// if (oDashBoardModel.getProperty("/userData/isENG") && value.parentOrigin === "Inquiry" && value.origin === "Inquiry" &&
									// 	value.status.toLowerCase() === "assigned" && value.creatorGroupId &&
									// 	value.creatorGroupId.includes("Engineer")) { //AN: #inquiryEnhancement
									// 	value.taskOwner = value.creatorGroupId;
									// }
									for (var g = 0; g < sGroups.length; g++) {
										if (((value.creatorGroupId && value.creatorGroupId.search(sGroups[g]) >= 0) || (value.createdOnGroup && value.createdOnGroup
												.search(sGroups[g]) >=
												0)) && (!(value.parentOrigin === "Pigging" && value.createdByEmailId ===
												"SYSTEM"))) {
											value.createdOn = this.convertTaskCreatedAt(oDashBoardScope, value.createdAtInString); //AN: #taskFilter
											if (value.location) {
												value.location = value.location.trim(); //AN:#obxSearch
											}
											aNewTaskList.push(value);
											this.createTaskFilterParameters(oDashBoardScope, oDashBoardModel, value); //AN: #taskFilter
											break;
										} else if (bIsCreatedByMe && value.creatorGroupId && value.creatorGroupId.search(sGroups[g]) < 0 && value.createdByEmailId ===
											sUserId) { //AN: #ChangeSeat
											value.createdOn = this.convertTaskCreatedAt(oDashBoardScope, value.createdAtInString); //AN: #taskFilter
											if (value.location) {
												value.location = value.location.trim(); //AN:#obxSearch
											}
											aNewTaskList.push(value);
											this.createTaskFilterParameters(oDashBoardScope, oDashBoardModel, value); //AN: #taskFilter
											break;
										}
									}
								}.bind(this)); //AN: #taskFilter
								oDashBoardModel.setProperty("/taskList", aNewTaskList); //AN: #inquire
								this.removeDuplicateTaskFilterParameters(oDashBoardScope, oDashBoardModel); //AN: #taskFilter
							} else { //AN: #inquire (For NON-DISPATCH task)
								oDashBoardModel.setProperty("/taskList", oData.taskList); //AN: #inquire
							}
						} else {
							oDashBoardModel.setProperty("/taskList", aNewTaskList); //AN: #inquire
						}
					}
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					oDashBoardModel.setProperty("/busyIndicators/rightPanelFilterBusy", false); //AN: #taskFilter
				}.bind(this), function (oError) {
					var sErrorMessage;
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					oDashBoardModel.setProperty("/busyIndicators/rightPanelFilterBusy", false); //AN: #taskFilter
					sErrorMessage = oError.getParameter("statusText");
					if (oDashBoardScope.oConfirmDialog === undefined || !oDashBoardScope.oConfirmDialog.isOpen()) {
						if (oError.getId() === "requestFailed" && oError.getParameter("message") === "error" && oError.getParameter("statusText") ===
							"error" && oError.getParameter("statusCode") === 0) {
							//do Nothing Handle net::ERR_NETWORK_IO_SUSPENDED
						} else if (oError.getId() === "requestFailed" && oError.getParameter("message") === "parsererror" && oError.getParameter(
								"statusText") ===
							"OK" && oError.getParameter("statusCode") === 200) {
							//do nothing Unauthorized session
						} else if (oError.getId() === "requestFailed" && oError.getParameter("message") === "parsererror" &&
							oError.getParameter("statusText") === "parsererror" && oError.getParameter("statusCode") === 200) { //AN: #parseError
							oDashBoardScope._createConfirmationMessage("Error", "The user session has timed out. Please refresh the page", "Error", "",
								"Close",
								false,
								null);
							//html error due to session timeout - msg display
						} else {
							oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
						}
					}
				}.bind(this));
			}
		},
		/** 
		 * Event to capture task management filter panel's expansion
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskFilterPanelExpand: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			this.validateStyling(oDashBoardScope, oDashBoardModel, oEvent, oEvent.getSource().getExpanded(), taskmanagementpane1);
			if (oEvent.getSource().getExpanded()) {
				oDashBoardModel.setProperty("/taskFilterData/isFilterPanelExpanded", true);
			} else {
				oDashBoardModel.setProperty("/taskFilterData/isFilterPanelExpanded", false);
			}
			if (oDashBoardModel.getProperty("/busyIndicators/rightPanelBusy")) {
				oDashBoardModel.setProperty("/busyIndicators/rightPanelFilterBusy", true);
			}
		},
		/** 
		 * Event to capture task management filter panel's expansion
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskfilterLinkPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			var idTaskFilterPanel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskFilterPanel");
			if (idTaskFilterPanel.getExpanded()) {
				idTaskFilterPanel.setExpanded(false);
				oDashBoardModel.setProperty("/taskFilterData/isFilterPanelExpanded", false);
			} else {
				idTaskFilterPanel.setExpanded(true);
				oDashBoardModel.setProperty("/taskFilterData/isFilterPanelExpanded", true);
			}
			this.validateStyling(oDashBoardScope, oDashBoardModel, oEvent, idTaskFilterPanel.getExpanded(), taskmanagementpane1);
			if (oDashBoardModel.getProperty("/busyIndicators/rightPanelBusy")) {
				oDashBoardModel.setProperty("/busyIndicators/rightPanelFilterBusy", true);
			}
		},
		/** 
		 * Function to validate the styling of task filter panel
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 * @param {Boolean} isExpanded - States whether the filter panel is expanded or collapsed
		 * @param {Object} taskmanagementpane1 - Contains the tak filter panel element
		 */
		validateStyling: function (oDashBoardScope, oDashBoardModel, oEvent, isExpanded, taskmanagementpane1) { //AN: #taskFilter
			var idTaskHeaderBox = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskHeaderBox");
			var idTaskFilterPanel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskFilterPanel");
			if (isExpanded) {
				idTaskHeaderBox.addStyleClass("iopTaskHeaderBox");
				idTaskFilterPanel.addStyleClass("iopTaskFilter");
			} else {
				idTaskHeaderBox.removeStyleClass("iopTaskHeaderBox");
				idTaskFilterPanel.removeStyleClass("iopTaskFilter");
			}
		},
		/** 
		 * Event to capture Field filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onFieldFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFieldFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			//RV: Filter For Field Changes
			var sSelectedFieldKey = "";
			var sSelectedField = "";
			/*#RV Field Level Filter Enhacement changes */
			var oSelectedKeys = oEvent.getSource().getSelectedKeys();
			sSelectedFieldKey = oSelectedKeys.join(",");
			/*if (oSelectedKeys && oSelectedKeys.length > 0) {
				for (var i = 0; i < oSelectedKeys.length - 1; i++) {
					sSelectedFieldKey += oSelectedKeys[i] + ",";
				}
				sSelectedFieldKey += oSelectedKeys[i];
			}*/
			taskFilterData.selectedFieldKey = oDashBoardModel.getData().userData.sGroup;
			if (oEvent.getSource().getSelectedItems()) {
				taskFilterData.selectedField = sSelectedFieldKey;
			} else {
				taskFilterData.selectedField = "";
			}
			oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", true);
			oDashBoardModel.setProperty("/busyIndicators/rightPanelFilterBusy", true);
			this._bindRightTaskPanelModel(oDashBoardScope, oDashBoardModel.getProperty("/selectedTaskTab")); //AN: #ScratchFilter
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture Task Type filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskTypeFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			var selectedKey = oEvent.getSource().getSelectedKey();
			var taskmanagementpane1 = oDashBoardScope.createId("taskmanagementpane1");
			var idTaskClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskClassFilterComboBoxLabel");
			var idSubClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idSubClassFilterComboBoxLabel");
			var idIssueClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idIssueClassFilterComboBoxLabel");
			var selKeyForSwitch = selectedKey; //AN: #parentOrigin
			if (selectedKey.search("Dispatch") >= 0) { //AN: #parentOrigin
				selKeyForSwitch = "Dispatch";
			}
			switch (selKeyForSwitch) { //AN: #parentOrigin
			case "Dispatch":
				oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
					isTaskClassificationVisible: true,
					isSubClassificationVisible: true,
					isIssueClassificationVisible: false
				});
				idIssueClassFilterComboBoxLabel.setSelectedKey("None");
				taskFilterData.selectedIssueClassification = "";
				if (!taskFilterData.selectedTaskClassification) {
					idSubClassFilterComboBoxLabel.setSelectedKey("None");
					taskFilterData.selectedSubClassification = "";
					taskFilterData.subClassification = [];
				}
				// oDashBoardModel.setProperty("/taskFilterData/issueClassification", taskFilterData.totalIssueClassification);
				break;
			case "Investigation":
				oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
					isTaskClassificationVisible: false,
					isSubClassificationVisible: true,
					isIssueClassificationVisible: true
				});
				idTaskClassFilterComboBoxLabel.setSelectedKey("None");
				oDashBoardModel.getProperty("/taskFilterData").selectedTaskClassification = "";
				if (!taskFilterData.selectedIssueClassification) {
					idSubClassFilterComboBoxLabel.setSelectedKey("None");
					taskFilterData.selectedSubClassification = "";
					taskFilterData.subClassification = [];
				}
				// oDashBoardModel.setProperty("/taskFilterData/issueClassification", taskFilterData.investigationIssueClassification);
				break;
			case "Inquiry":
				oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
					isTaskClassificationVisible: false,
					isSubClassificationVisible: false,
					isIssueClassificationVisible: true
				});
				idTaskClassFilterComboBoxLabel.setSelectedKey("None");
				oDashBoardModel.getProperty("/taskFilterData").selectedTaskClassification = "";
				idSubClassFilterComboBoxLabel.setSelectedKey("None");
				oDashBoardModel.getProperty("/taskFilterData").selectedSubClassification = "";
				// oDashBoardModel.setProperty("/taskFilterData/issueClassification", taskFilterData.inquiryIssueClassification);
				break;
			default:
				oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
					isTaskClassificationVisible: true,
					isSubClassificationVisible: true,
					isIssueClassificationVisible: true
				});
				idTaskClassFilterComboBoxLabel.setSelectedKey("None");
				oDashBoardModel.getProperty("/taskFilterData").selectedTaskClassification = "";
				idSubClassFilterComboBoxLabel.setSelectedKey("None");
				oDashBoardModel.getProperty("/taskFilterData").selectedSubClassification = "";
				taskFilterData.subClassification = [];
				idIssueClassFilterComboBoxLabel.setSelectedKey("None");
				oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification = "";
				// oDashBoardModel.setProperty("/taskFilterData/issueClassification", taskFilterData.totalIssueClassification);
				break;
			}
			if (oEvent.getSource().getSelectedKey() === "None") {
				taskFilterData.selectedTaskType = "";
			} else {
				taskFilterData.selectedTaskType = oEvent.getSource().getSelectedKey(); //AN: #parentOrigin
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture task location filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onLocationFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			if (oEvent.getSource().getSelectedItem()) {
				taskFilterData.selectedLocation = oEvent.getSource().getSelectedItem().getText();
			} else {
				taskFilterData.selectedLocation = oEvent.getSource().getValue();
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture Task Classification filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskClassFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskmanagementpane1 = oDashBoardScope.createId("taskmanagementpane1");
			var idIssueClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idIssueClassFilterComboBoxLabel");
			idIssueClassFilterComboBoxLabel.setSelectedKey("None");
			oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification = "";
			this.createSubClassificationFilter(oDashBoardScope, oDashBoardModel, oEvent, "taskClassification");
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture Sub Classification filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onSubClassFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			if (oEvent.getSource().getSelectedItem()) {
				taskFilterData.selectedSubClassification = oEvent.getSource().getSelectedItem().getText();
			} else {
				taskFilterData.selectedSubClassification = oEvent.getSource().getValue();
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture Issue Classification filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onIssueClassFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskmanagementpane1 = oDashBoardScope.createId("taskmanagementpane1");
			var idTaskClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskClassFilterComboBoxLabel");
			idTaskClassFilterComboBoxLabel.setSelectedKey("None");
			oDashBoardModel.getProperty("/taskFilterData").selectedTaskClassification = "";
			this.createSubClassificationFilter(oDashBoardScope, oDashBoardModel, oEvent, "issueClassification");
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		createSubClassificationFilter: function (oDashBoardScope, oDashBoardModel, oEvent, classificationType) {
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			var selectedValue = oEvent.getSource().getValue();
			var selectedKey = oEvent.getSource().getSelectedKey();
			var subClassificationFilter = [];
			var regExp = "$#&#$"; //AN: #ScratchFilter
			$.each(taskFilterData.classification, function (index, value) {
				if (value.text.split(regExp)[0].trim() === selectedKey) {
					var obj = {
						key: value.text.split(regExp)[1].trim(),
						text: value.text.split(regExp)[1].trim()
					}
					subClassificationFilter.push(obj);
				}
			});
			if (subClassificationFilter.length) {
				subClassificationFilter.unshift({
					key: "None",
					text: ""
				});
			} else {
				var taskmanagementpane1 = oDashBoardScope.createId("taskmanagementpane1");
				var idSubClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idSubClassFilterComboBoxLabel");
				idSubClassFilterComboBoxLabel.setSelectedKey("None");
			}
			taskFilterData.selectedSubClassification = "";
			oDashBoardModel.setProperty("/taskFilterData/subClassification", subClassificationFilter);
			if (oEvent.getSource().getSelectedItem()) {
				if (classificationType === "taskClassification") {
					taskFilterData.selectedTaskClassification = oEvent.getSource().getSelectedItem().getText();
				} else if (classificationType === "issueClassification") {
					taskFilterData.selectedIssueClassification = oEvent.getSource().getSelectedItem().getText();
				}
			} else {
				if (classificationType === "taskClassification") {
					taskFilterData.selectedTaskClassification = oEvent.getSource().getValue();
				} else if (classificationType === "issueClassification") {
					taskFilterData.selectedIssueClassification = oEvent.getSource().getValue();
				}
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
		},
		/** 
		 * Event to capture Task Status filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskStatusFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			if (oEvent.getSource().getSelectedItem()) {
				taskFilterData.selectedStatus = oEvent.getSource().getSelectedItem().getText();
			} else {
				taskFilterData.selectedStatus = oEvent.getSource().getValue();
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture Task Assigned To filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskAssignedToFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			if (oEvent.getSource().getSelectedItem()) {
				taskFilterData.selectedAssignedTo = oEvent.getSource().getSelectedItem().getText();
			} else {
				taskFilterData.selectedAssignedTo = oEvent.getSource().getValue();
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Event to capture Task Created On filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskCreatedOnFilterChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			taskFilterData.selectedCreatedOn = oEvent.getSource().getValue(); //AN: #CreatonOnfixV2
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
		},
		/** 
		 * Event to capture Task Created By filter selection change
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onCreatedByFilterSelectionChange: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			if (oEvent.getSource().getSelectedItem()) {
				taskFilterData.selectedCreatedBy = oEvent.getSource().getSelectedItem().getText();
			} else {
				taskFilterData.selectedCreatedBy = oEvent.getSource().getValue();
			}
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			oEvent.getSource().close(); //AN: #CreatonOnfix
		},
		/** 
		 * Function to invoke the application of the filters
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} taskFilterData - Contains the task filter parameters to be applied
		 */
		configureTaskFilterSetting: function (oDashBoardScope, oDashBoardModel, taskFilterData) { //AN: #taskFilter
			var aFilters = [];
			aFilters = this.createOverAllTaskFilters(oDashBoardScope, oDashBoardModel, taskFilterData);
			oDashBoardModel.setProperty("/taskFilterData/filters", aFilters);
			this.validateIfFiltersAreApplied(oDashBoardScope, oDashBoardModel, taskFilterData);
			this.applyTaskFilter(oDashBoardScope, oDashBoardModel, aFilters);
		},
		/** 
		 * To create all the filters for task filtering
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} taskFilterData - Contains the task filter parameters to be applied
		 */
		createOverAllTaskFilters: function (oDashBoardScope, oDashBoardModel, taskFilterData) { //AN: #taskFilter
			var aFilters = [],
				taskTypeFilter = [],
				locationFilter = [],
				taskClassificationFilter = [],
				subClassificationFilter = [],
				inquiryIssueClassificationFilter = [], //AN: #ScratchFilter
				investigationIssueClassificationFilter = [], //AN: #ScratchFilter
				// issueClassificationFilter = [],
				totalIssueClassificationFilter = [], //AN: #ScratchFilter
				statusFilter = [],
				createdOnFilter = [],
				assignedToFilter = [],
				createdByFilter = [], //AN: #taskV2Demo
				regExp = "$#&#$"; //AN: #parentOrigin
			if (taskFilterData.selectedTaskType === "Dispatch") {
				var originFilter = [];
				originFilter.push(new sap.ui.model.Filter("origin", "Contains", taskFilterData.selectedTaskType));
				taskTypeFilter.push(new sap.ui.model.Filter("parentOrigin", "Contains", "OBX"));
				taskTypeFilter.push(new sap.ui.model.Filter("parentOrigin", "Contains", "Inquiry"));
				taskTypeFilter.push(new sap.ui.model.Filter("parentOrigin", "Contains", "Custom"));
				taskTypeFilter.push(new sap.ui.model.Filter("parentOrigin", "Contains", "Investigation"));
				var parentOriginFilters = new sap.ui.model.Filter({
					filters: taskTypeFilter,
					and: false
				});
				aFilters = aFilters.concat(parentOriginFilters);
				aFilters = aFilters.concat(originFilter);
			} else if (taskFilterData.selectedTaskType && taskFilterData.selectedTaskType.search("Dispatch") >= 0) { //AN: #parentOrigin
				taskTypeFilter.push(new sap.ui.model.Filter("origin", "Contains", taskFilterData.selectedTaskType.split(regExp)[0]));
				taskTypeFilter.push(new sap.ui.model.Filter("parentOrigin", "Contains", taskFilterData.selectedTaskType.split(regExp)[1]));
				aFilters = aFilters.concat(taskTypeFilter);
			} else if (taskFilterData.selectedTaskType) {
				taskTypeFilter.push(new sap.ui.model.Filter("origin", "Contains", taskFilterData.selectedTaskType));
				aFilters = aFilters.concat(taskTypeFilter);
			}
			if (taskFilterData.selectedLocation) {
				locationFilter.push(new sap.ui.model.Filter("location", "Contains", taskFilterData.selectedLocation));
				aFilters = aFilters.concat(locationFilter);
			}
			if (taskFilterData.selectedTaskType && taskFilterData.selectedTaskType.search("Dispatch") >= 0) { //AN: #parentOrigin
				if (taskFilterData.selectedTaskClassification) {
					taskClassificationFilter.push(new sap.ui.model.Filter("taskClassification", "EQ", taskFilterData.selectedTaskClassification));
					aFilters = aFilters.concat(taskClassificationFilter);
				}
				if (taskFilterData.selectedSubClassification) {
					subClassificationFilter.push(new sap.ui.model.Filter("subClassification", "EQ", taskFilterData.selectedSubClassification));
					aFilters = aFilters.concat(subClassificationFilter);
				}
			} else if (taskFilterData.selectedTaskType === "Investigation") {
				if (taskFilterData.selectedIssueClassification) {
					// issueClassificationFilter.push(new sap.ui.model.Filter("issueClassification", "EQ", taskFilterData.selectedIssueClassification));
					// aFilters = aFilters.concat(issueClassificationFilter);
					investigationIssueClassificationFilter.push(new sap.ui.model.Filter("issueClassification", "EQ", taskFilterData.selectedIssueClassification));
					aFilters = aFilters.concat(investigationIssueClassificationFilter);
				}
				if (taskFilterData.selectedSubClassification) {
					subClassificationFilter.push(new sap.ui.model.Filter("subClassification", "EQ", taskFilterData.selectedSubClassification));
					aFilters = aFilters.concat(subClassificationFilter);
				}
			} else if (taskFilterData.selectedTaskType === "Inquiry") {
				if (taskFilterData.selectedIssueClassification) {
					// issueClassificationFilter.push(new sap.ui.model.Filter("issueClassification", "EQ", taskFilterData.selectedIssueClassification));
					// aFilters = aFilters.concat(issueClassificationFilter);
					inquiryIssueClassificationFilter.push(new sap.ui.model.Filter("issueClassification", "EQ", taskFilterData.selectedIssueClassification));
					aFilters = aFilters.concat(inquiryIssueClassificationFilter);
				}
			} else {
				if (taskFilterData.selectedTaskClassification) {
					taskClassificationFilter.push(new sap.ui.model.Filter("taskClassification", "EQ", taskFilterData.selectedTaskClassification));
					aFilters = aFilters.concat(taskClassificationFilter);
				}
				if (taskFilterData.selectedSubClassification) {
					subClassificationFilter.push(new sap.ui.model.Filter("subClassification", "EQ", taskFilterData.selectedSubClassification));
					aFilters = aFilters.concat(subClassificationFilter);
				}
				if (taskFilterData.selectedIssueClassification) {
					// issueClassificationFilter.push(new sap.ui.model.Filter("issueClassification", "Contains", taskFilterData.selectedIssueClassification));
					// aFilters = aFilters.concat(issueClassificationFilter);
					totalIssueClassificationFilter.push(new sap.ui.model.Filter("issueClassification", "Contains", taskFilterData.selectedIssueClassification));
					aFilters = aFilters.concat(totalIssueClassificationFilter);
				}
			}
			if (taskFilterData.selectedStatus) {
				statusFilter.push(new sap.ui.model.Filter("status", "Contains", taskFilterData.selectedStatus));
				aFilters = aFilters.concat(statusFilter);
			}
			if (taskFilterData.selectedAssignedTo) {
				assignedToFilter.push(new sap.ui.model.Filter("taskOwner", "Contains", taskFilterData.selectedAssignedTo));
				aFilters = aFilters.concat(assignedToFilter);
			}
			if (taskFilterData.selectedCreatedOn) {
				createdOnFilter.push(new sap.ui.model.Filter("createdOn", "EQ", taskFilterData.selectedCreatedOn));
				aFilters = aFilters.concat(createdOnFilter);
			}
			if (taskFilterData.selectedCreatedBy) { //AN: #taskV2Demo
				createdByFilter.push(new sap.ui.model.Filter("createdBy", "EQ", taskFilterData.selectedCreatedBy));
				aFilters = aFilters.concat(createdByFilter);
			}
			return aFilters;
		},
		/** 
		 * To Apply filters to tasks
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} systemFilters - Contains the task filter parameters to be applied
		 */
		applyTaskFilter: function (oDashBoardScope, oDashBoardModel, systemFilters) {
			var oTable = sap.ui.core.Fragment.byId(oDashBoardScope.createId("taskmanagementpane1"), "idTaskList"),
				oBinding = oTable.getBinding("items");
			if (oBinding) {
				oBinding.filter(systemFilters);
			}
		},
		/** 
		 * To clear all the task filters
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Contains the eventing parameters
		 */
		onTaskClearFilter: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #taskFilter
			var taskmanagementpane1 = oDashBoardScope.createId("taskmanagementpane1");
			var idTaskClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskClassFilterComboBoxLabel");
			var idSubClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idSubClassFilterComboBoxLabel");
			var idIssueClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idIssueClassFilterComboBoxLabel");
			var idTaskTypeFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskTypeFilterComboBoxLabel");
			var idStatusFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idStatusFilterComboBoxLabel");
			var idLocationFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idLocationFilterComboBoxLabel");
			var idAssignedToFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idAssignedToFilterComboBoxLabel");
			var idCreatedOnFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idCreatedOnFilterComboBoxLabel");
			var idFieldFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idFieldFilterComboBoxLabel"); //AN: TaskFieldFilter
			var idCreatedByFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idCreatedByFilterComboBoxLabel"); //AN: #taskV2Demo
			idSubClassFilterComboBoxLabel.setSelectedKey("None");
			idTaskClassFilterComboBoxLabel.setSelectedKey("None");
			idIssueClassFilterComboBoxLabel.setSelectedKey("None");
			idStatusFilterComboBoxLabel.setSelectedKey("None");
			idTaskTypeFilterComboBoxLabel.setSelectedKey("None");
			idLocationFilterComboBoxLabel.setSelectedKey("None");
			idAssignedToFilterComboBoxLabel.setSelectedKey("None");
			idCreatedByFilterComboBoxLabel.setSelectedKey("None"); //AN: #taskV2Demo
			idFieldFilterComboBoxLabel.setSelectedKeys(""); /*#RV	Field Level Filter Enhacement changes */
			idCreatedOnFilterComboBoxLabel.setValue(""); /* #RV	Field Level Filter Enhacement changes */
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			taskFilterData.selectedField = ""; //AN: TaskFieldFilter
			taskFilterData.selectedFieldKey = "None"; //AN: TaskFieldFilter
			taskFilterData.selectedTaskType = "";
			taskFilterData.selectedLocation = "";
			taskFilterData.selectedTaskClassification = "";
			taskFilterData.selectedSubClassification = "";
			taskFilterData.selectedStatus = "";
			taskFilterData.selectedIssueClassification = "";
			taskFilterData.selectedCreatedOn = "";
			taskFilterData.selectedAssignedTo = "";
			taskFilterData.selectedCreatedBy = ""; //AN: #taskV2Demo
			taskFilterData.subClassification = [];
			oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
				isTaskClassificationVisible: true,
				isSubClassificationVisible: true,
				isIssueClassificationVisible: true
			});
			oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", true); //AN: TaskFieldFilter
			oDashBoardModel.setProperty("/busyIndicators/rightPanelFilterBusy", true); //AN: TaskFieldFilter
			this._bindRightTaskPanelModel(oDashBoardScope, oDashBoardModel.getProperty("/selectedTaskTab")); //AN: TaskFieldFilter //AN: #ScratchFilter
			this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
		},
		/** 
		 * To refresh all the task filters
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		refreshTaskFilters: function (oDashBoardScope, oDashBoardModel) { //AN: #taskFilter
			oDashBoardModel.setProperty("/taskFilterData/taskType", []);
			oDashBoardModel.setProperty("/taskFilterData/location", []);
			oDashBoardModel.setProperty("/taskFilterData/status", []);
			oDashBoardModel.setProperty("/taskFilterData/classification", []);
			oDashBoardModel.setProperty("/taskFilterData/taskClassification", []);
			oDashBoardModel.setProperty("/taskFilterData/issueClassification", []);
			oDashBoardModel.setProperty("/taskFilterData/inquiryIssueClassification", []);
			oDashBoardModel.setProperty("/taskFilterData/investigationIssueClassification", []);
			// oDashBoardModel.setProperty("/taskFilterData/totalIssueClassification", []);
			oDashBoardModel.setProperty("/taskFilterData/assignedTo", []);
			oDashBoardModel.setProperty("/taskFilterData/createdOn", []);
			oDashBoardModel.setProperty("/taskFilterData/createdBy", []); //AN: #taskV2Demo
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			var idTaskClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskClassFilterComboBoxLabel");
			if (!(idTaskClassFilterComboBoxLabel.getSelectedKey())) {
				oDashBoardModel.setProperty("/taskFilterData/subClassification", []);
			}
		},
		/** 
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} value - Conatins task value
		 */
		createTaskFilterParameters: function (oDashBoardScope, oDashBoardModel, value) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			var regExp = "$#&#$"; //AN: #ScratchFilter
			var tempClassification = "";
			var tempIssueClassification = "";
			if (value.origin === "Investigation" || value.origin === "Inquiry") {
				taskFilterData.taskType.push(value.origin);
			} else if (value.origin === "Dispatch" && value.parentOrigin) { //AN: #parentOrigin
				if (value.parentOrigin === "Inquiry" || value.parentOrigin === "Custom" || value.parentOrigin === "Investigation" || value.parentOrigin ===
					"OBX") {
					taskFilterData.taskType.push(value.origin);
				} else {
					taskFilterData.taskType.push(value.origin + regExp + value.parentOrigin);
				}
			}
			if (value.location) {
				taskFilterData.location.push(value.location);
			}
			taskFilterData.status.push(value.status);
			if (value.origin === "Inquiry" && value.issueClassification) { //AN: #ScratchFilter
				tempIssueClassification = value.issueClassification + regExp + value.origin;
				// taskFilterData.issueClassification.push(value.issueClassification); //AN: #ScratchFilter
				taskFilterData.issueClassification.push(tempIssueClassification); //AN: #ScratchFilter
				// taskFilterData.tempIssueClassification.push(tempIssueClassification); //AN: #ScratchFilter
			} else if (value.origin === "Investigation" && (value.issueClassification && value.subClassification)) {
				tempIssueClassification = value.issueClassification + regExp + value.origin;
				taskFilterData.issueClassification.push(tempIssueClassification); //AN: #ScratchFilter
				// taskFilterData.tempIssueClassification.push(tempIssueClassification); //AN: #ScratchFilter
				// taskFilterData.issueClassification.push(value.issueClassification); //AN: #ScratchFilter
				tempClassification = value.issueClassification + regExp + value.subClassification;
				taskFilterData.classification.push(tempClassification); //AN: #ScratchFilter
			} else if (value.origin === "Dispatch" && (value.taskClassification && value.subClassification)) {
				taskFilterData.taskClassification.push(value.taskClassification); //AN: #ScratchFilter
				tempClassification = value.taskClassification + regExp + value.subClassification;
				taskFilterData.classification.push(tempClassification); //AN: #ScratchFilter
			}
			if (value.taskOwner) {
				var taskOwnerLength = value.taskOwner.split(",").length;
				if (taskOwnerLength > 1) {
					for (var tO = 0; tO < taskOwnerLength; tO++) {
						taskFilterData.assignedTo.push(value.taskOwner.split(",")[tO]);
					}
				} else {
					taskFilterData.assignedTo.push(value.taskOwner);
				}
			}
			if (value.taskOwner) { //AN: #taskV2Demo
				taskFilterData.createdBy.push(value.createdBy);
			}
		},
		/** 
		 * To remove duplicate entries in a particular task filter
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		removeDuplicateTaskFilterParameters: function (oDashBoardScope, oDashBoardModel) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			var taskTypeFilter = [],
				locationFilter = [],
				statusFilter = [],
				classificationFilter = [],
				taskClassificationFilter = [],
				issueClassificationFilter = [],
				// inquiryIssueClassificationFilter = [], //AN: #ScratchFilter
				// investigationIssueClassificationFilter = [], //AN: #ScratchFilter
				// totalIssueClassificationFilter = [], //AN: #ScratchFilter
				assignedToFilter = [],
				createdOnFilter = [],
				createdByFilter = []; //AN: #taskV2Demo
			var taskType = taskFilterData.taskType;
			var status = taskFilterData.status;
			var location = taskFilterData.location;
			var classification = taskFilterData.classification;
			var taskClassification = taskFilterData.taskClassification;
			var issueClassification = taskFilterData.issueClassification;
			// var issueClassification = taskFilterData.tempIssueClassification;  //AN: #ScratchFilter
			var assignedTo = taskFilterData.assignedTo;
			var createdBy = taskFilterData.createdBy; //AN: #taskV2Demo
			var regExp = "$#&#$"; //AN: #ScratchFilter
			var taskmanagementpane1 = oDashBoardScope.createId("taskmanagementpane1");
			var idTaskClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskClassFilterComboBoxLabel");
			var idSubClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idSubClassFilterComboBoxLabel");
			var idIssueClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idIssueClassFilterComboBoxLabel");
			var loc = (location) => location.filter((v, i) => location.indexOf(v) === i)
			taskFilterData.location = loc(location);
			taskFilterData.location.sort();
			$.each(taskFilterData.location, function (index, value) {
				var obj = {
					key: value,
					text: value
				}
				locationFilter.push(obj);
			});
			if (locationFilter.length) {
				locationFilter.unshift({
					key: "None",
					text: ""
				});
			}

			var tt = (taskType) => taskType.filter((v, i) => taskType.indexOf(v) === i)
			taskFilterData.taskType = tt(taskType);
			taskFilterData.taskType.sort();
			$.each(taskFilterData.taskType, function (index, value) {
				if (value === "Dispatch") { //AN: #parentOrigin
					var obj = {
						key: value,
						text: value
					}
				} else if (value && value.search("Dispatch") >= 0) { //AN: #parentOrigin
					var obj = {
						key: value,
						text: value.split(regExp)[0] + " - " + value.split(regExp)[1]
					}
				} else {
					var obj = {
						key: value,
						text: value
					}
				}
				taskTypeFilter.push(obj);
			});
			if (taskTypeFilter.length) {
				taskTypeFilter.unshift({
					key: "None",
					text: ""
				});
			}

			var s = (status) => status.filter((v, i) => status.indexOf(v) === i)
			taskFilterData.status = s(status);
			taskFilterData.status.sort();
			$.each(taskFilterData.status, function (index, value) {
				var obj = {
					key: value,
					text: value
				}
				statusFilter.push(obj);
			});
			if (statusFilter.length) {
				statusFilter.unshift({
					key: "None",
					text: ""
				});
			}

			var c = (classification) => classification.filter((v, i) => classification.indexOf(v) === i)
			taskFilterData.classification = c(classification);
			taskFilterData.classification.sort();
			$.each(taskFilterData.classification, function (index, value) {
				var obj = {
					key: value,
					text: value
				}
				classificationFilter.push(obj);
			});

			var tc = (taskClassification) => taskClassification.filter((v, i) => taskClassification.indexOf(v) === i)
			taskFilterData.taskClassification = tc(taskClassification);
			taskFilterData.taskClassification.sort();
			$.each(taskFilterData.taskClassification, function (index, value) {
				var obj = {
					key: value,
					text: value
				}
				taskClassificationFilter.push(obj);
			});
			if (taskClassificationFilter.length) {
				taskClassificationFilter.unshift({
					key: "None",
					text: ""
				});
			}

			var ic = (issueClassification) => issueClassification.filter((v, i) => issueClassification.indexOf(v) === i)
			taskFilterData.issueClassification = ic(issueClassification);
			taskFilterData.issueClassification.sort();
			// taskFilterData.tempIssueClassification = ic(issueClassification);
			// taskFilterData.tempIssueClassification.sort();
			// $.each(taskFilterData.tempIssueClassification, function (index, value) {
			$.each(taskFilterData.issueClassification, function (index, value) {
				// if (value.split(regExp)[1] === "Inquiry") {
				// 	var obj = {
				// 		key: value.split(regExp)[0],
				// 		text: value.split(regExp)[0]
				// 	}
				// 	inquiryIssueClassificationFilter.push(obj);
				// } else if (value.split(regExp)[1] === "Investigation") {
				// 	var obj = {
				// 		key: value.split(regExp)[0],
				// 		text: value.split(regExp)[0]
				// 	}
				// 	investigationIssueClassificationFilter.push(obj);
				// }
				var obj = {
					key: value.split(regExp)[0],
					text: value.split(regExp)[0]
				}
				issueClassificationFilter.push(obj);
				// totalIssueClassificationFilter.push(obj);
			});
			// if (totalIssueClassificationFilter.length) {
			// 	totalIssueClassificationFilter.unshift({
			// 		key: "None",
			// 		text: ""
			// 	});
			// }
			if (issueClassificationFilter.length) {
				issueClassificationFilter.unshift({
					key: "None",
					text: ""
				});
			}
			// if (inquiryIssueClassificationFilter.length) {
			// 	inquiryIssueClassificationFilter.unshift({
			// 		key: "None",
			// 		text: ""
			// 	});
			// }
			// if (investigationIssueClassificationFilter.length) {
			// 	investigationIssueClassificationFilter.unshift({
			// 		key: "None",
			// 		text: ""
			// 	});
			// }

			var aT = (assignedTo) => assignedTo.filter((v, i) => assignedTo.indexOf(v) === i)
			taskFilterData.assignedTo = aT(assignedTo);
			taskFilterData.assignedTo.sort();
			$.each(taskFilterData.assignedTo, function (index, value) {
				var obj = {
					key: value.trim(),
					text: value.trim()
				}
				assignedToFilter.push(obj);
			});
			if (assignedToFilter.length) {
				assignedToFilter.unshift({
					key: "None",
					text: ""
				});
			}

			var cB = (createdBy) => createdBy.filter((v, i) => createdBy.indexOf(v) === i) //AN: #taskV2Demo
			taskFilterData.createdBy = cB(createdBy); //AN: #taskV2Demo
			taskFilterData.createdBy.sort(); //AN: #taskV2Demo
			$.each(taskFilterData.createdBy, function (index, value) { //AN: #taskV2Demo
				var obj = {
					key: value.trim(),
					text: value.trim()
				}
				createdByFilter.push(obj);
			});
			if (createdByFilter.length) { //AN: #taskV2Demo
				createdByFilter.unshift({
					key: "None",
					text: ""
				});
			}
			oDashBoardModel.setProperty("/taskFilterData/taskType", taskTypeFilter);
			oDashBoardModel.setProperty("/taskFilterData/location", locationFilter);
			oDashBoardModel.setProperty("/taskFilterData/status", statusFilter);
			oDashBoardModel.setProperty("/taskFilterData/classification", classificationFilter);
			oDashBoardModel.setProperty("/taskFilterData/taskClassification", taskClassificationFilter);
			// if (taskFilterData.selectedTaskType === "Inquiry") {
			// 	oDashBoardModel.setProperty("/taskFilterData/issueClassification", inquiryIssueClassificationFilter);
			// 	if (inquiryIssueClassificationFilter.length === 0) {
			// 		idSubClassFilterComboBoxLabel.setSelectedKey("None");
			// 		taskFilterData.selectedSubClassification = "";
			// 		taskFilterData.subClassification = [];
			// 		idIssueClassFilterComboBoxLabel.setSelectedKey("None");
			// 		oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification = "";
			// 	}
			// } else if (taskFilterData.selectedTaskType === "Investigation") {
			// 	oDashBoardModel.setProperty("/taskFilterData/issueClassification", investigationIssueClassificationFilter);
			// 	if (investigationIssueClassificationFilter.length === 0) {
			// 		idSubClassFilterComboBoxLabel.setSelectedKey("None");
			// 		taskFilterData.selectedSubClassification = "";
			// 		taskFilterData.subClassification = [];
			// 		idIssueClassFilterComboBoxLabel.setSelectedKey("None");
			// 		oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification = "";
			// 	}
			// } else if (!taskFilterData.selectedTaskType) {
			// 	oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
			// 		isTaskClassificationVisible: true,
			// 		isSubClassificationVisible: true,
			// 		isIssueClassificationVisible: true
			// 	});
			// 	oDashBoardModel.setProperty("/taskFilterData/issueClassification", totalIssueClassificationFilter);
			// 	if (totalIssueClassificationFilter.length === 0) {
			// 		idSubClassFilterComboBoxLabel.setSelectedKey("None");
			// 		taskFilterData.selectedSubClassification = "";
			// 		taskFilterData.subClassification = [];
			// 		idIssueClassFilterComboBoxLabel.setSelectedKey("None");
			// 		oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification = "";
			// 	}
			// }
			oDashBoardModel.setProperty("/taskFilterData/issueClassification", issueClassificationFilter);
			// oDashBoardModel.setProperty("/taskFilterData/inquiryIssueClassification", inquiryIssueClassificationFilter);
			// oDashBoardModel.setProperty("/taskFilterData/investigationIssueClassification", investigationIssueClassificationFilter);
			// oDashBoardModel.setProperty("/taskFilterData/totalIssueClassification", totalIssueClassificationFilter);
			oDashBoardModel.setProperty("/taskFilterData/assignedTo", assignedToFilter);
			oDashBoardModel.setProperty("/taskFilterData/createdBy", createdByFilter); //AN: #taskV2Demo
			this.checkForFilterInconsitencies(oDashBoardScope, oDashBoardModel);
		},
		/** 
		 * To re apply filters in case of any filter parameters are removed after refresh
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		checkForFilterInconsitencies: function (oDashBoardScope, oDashBoardModel) { //AN: #taskFilter
			var taskFilterData = oDashBoardModel.getProperty("/taskFilterData");
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			var idSubClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idSubClassFilterComboBoxLabel");
			var needFilterRefresh = false;

			if (taskFilterData.selectedStatus) {
				var idStatusFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idStatusFilterComboBoxLabel");
				var hasStatus = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").status.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").status[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedStatus) {
						hasStatus = true;
						break;
					} else {
						hasStatus = false;
					}
				}
				if (!hasStatus) {
					idStatusFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedStatus = "";
					needFilterRefresh = true;
				}
			}

			if (taskFilterData.selectedTaskType) {
				var idTaskTypeFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskTypeFilterComboBoxLabel");
				var hasTaskType = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").taskType.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").taskType[ss].key === oDashBoardModel.getProperty("/taskFilterData").selectedTaskType) { //AN: #parentOrigin
						hasTaskType = true;
						break;
					} else {
						hasTaskType = false;
					}
				}
				if (!hasTaskType) {
					idTaskTypeFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedTaskType = "";
					needFilterRefresh = true;
					oDashBoardModel.setProperty("/taskFilterData/arrAttr", {
						isTaskClassificationVisible: true,
						isSubClassificationVisible: true,
						isIssueClassificationVisible: true
					});
				}
			}

			if (taskFilterData.selectedTaskClassification) {
				var idTaskClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idTaskClassFilterComboBoxLabel");
				var hasTaskClassification = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").taskClassification.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").taskClassification[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedTaskClassification) {
						hasTaskClassification = true;
						break;
					} else {
						hasTaskClassification = false;
					}
				}
				if (!hasTaskClassification) {
					idTaskClassFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedTaskClassification = "";
					idSubClassFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedSubClassification = "";
					taskFilterData.subClassification = [];
					needFilterRefresh = true;
				}
			}

			if (taskFilterData.selectedLocation) {
				var idLocationFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idLocationFilterComboBoxLabel");
				var hasLocation = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").location.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").location[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedLocation) {
						hasLocation = true;
						break;
					} else {
						hasLocation = false;
					}
				}
				if (!hasLocation) {
					idLocationFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedLocation = "";
					needFilterRefresh = true;
				}
			}

			if (taskFilterData.selectedAssignedTo) {
				var idAssignedToFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idAssignedToFilterComboBoxLabel");
				var hasAssignedTo = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").assignedTo.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").assignedTo[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedAssignedTo) {
						hasAssignedTo = true;
						break;
					} else {
						hasAssignedTo = false;
					}
				}
				if (!hasAssignedTo) {
					idAssignedToFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedAssignedTo = "";
					needFilterRefresh = true;
				}
			}
			if (taskFilterData.selectedCreatedBy) { //AN: #taskV2Demo
				var idCreatedByFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idCreatedByFilterComboBoxLabel");
				var hasCreatedBy = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").createdBy.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").createdBy[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedCreatedBy) {
						hasCreatedBy = true;
						break;
					} else {
						hasCreatedBy = false;
					}
				}
				if (!hasCreatedBy) {
					idCreatedByFilterComboBoxLabel.setSelectedKey("None")
					oDashBoardModel.getProperty("/taskFilterData").selectedCreatedBy = "";
					needFilterRefresh = true;
				}
			}

			if (taskFilterData.selectedIssueClassification) {
				var idIssueClassFilterComboBoxLabel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idIssueClassFilterComboBoxLabel");
				var hasIssueClassification = true;
				// if (oDashBoardModel.getProperty("/taskFilterData").issueClassification.length === 0) {
				// 	hasIssueClassification = false;
				// }
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").issueClassification.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").issueClassification[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification) {
						hasIssueClassification = true;
						break;
					} else {
						hasIssueClassification = false;
					}
				}
				if (!hasIssueClassification) {
					idIssueClassFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedIssueClassification = "";
					idSubClassFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedSubClassification = "";
					taskFilterData.subClassification = [];
					needFilterRefresh = true;
				}
			}

			if (taskFilterData.selectedSubClassification) {
				var hasSubClassification = true;
				for (var ss = 0; ss < oDashBoardModel.getProperty("/taskFilterData").subClassification.length; ss++) {
					if (oDashBoardModel.getProperty("/taskFilterData").subClassification[ss].text === oDashBoardModel.getProperty("/taskFilterData").selectedSubClassification) {
						hasSubClassification = true;
						break;
					} else {
						hasSubClassification = false;
					}
				}
				if (!hasSubClassification) {
					idSubClassFilterComboBoxLabel.setSelectedKey("None");
					oDashBoardModel.getProperty("/taskFilterData").selectedSubClassification = "";
					needFilterRefresh = true;
				}
			}

			if (needFilterRefresh) {
				this.configureTaskFilterSetting(oDashBoardScope, oDashBoardModel, taskFilterData);
			}
		},
		/** 
		 * To format the createdAt string
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {String} createdAt - Contains the CreatedAt String
		 */
		convertTaskCreatedAt: function (oDashBoardScope, createdAt) { //AN: #taskFilter //AN: #CreatonOnfix
			if (createdAt) { //AN: #CreatonOnfixV2
				if (createdAt.split(" ").length === 1 && createdAt.length === 13) { //SK:TimeZone changes
					var iDate = parseInt(createdAt);
					var nDate = new Date(iDate);
					var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
						pattern: "dd-MMM-yy hh:mm:ss aaa"
					});
					createdAt = oDateFormat.format(nDate);
				}
				var aDate = createdAt.split(" ");
				var aDateArray = aDate[0].split("-");
				var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
				sDate = sDate + " " + aDate[1] + " " + aDate[2];
				//	var dateObj = new Date(sDate + " UTC");
				var dateObj = new Date(sDate);
				var oMon = dateObj.getMonth();
				var oMonArr = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
				var x = dateObj.getDate();
				if (dateObj.getDate().toFixed().length === 1) {
					x = dateObj.getDate().toFixed();
					x = "0" + x;
				}
				var oDate = dateObj.getFullYear() + "-" + oMonArr[oMon] + "-" + x;
				return oDate;
			}
		},
		/** 
		 * To validate if any of the filters for task management are applied
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} taskFilterData - Contains the task filter parameters to be applied
		 */
		validateIfFiltersAreApplied: function (oDashBoardScope, oDashBoardModel, taskFilterData) { //AN: #taskFilter
			if (taskFilterData.selectedTaskType || taskFilterData.selectedLocation || taskFilterData.selectedTaskClassification ||
				taskFilterData.selectedIssueClassification || taskFilterData.selectedStatus || taskFilterData.selectedAssignedTo || taskFilterData
				.selectedCreatedOn || taskFilterData.selectedField || taskFilterData.selectedCreatedBy) { //AN: #taskV2Demo
				taskFilterData.isFilterApplied = true;
			} else {
				taskFilterData.isFilterApplied = false;
			}
			oDashBoardModel.refresh(true);
		},
		/** 
		 * Function triggered on press of Shift Handover tile Icon in Right Panel
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		onPressActiveBypassVBox: function (oDashBoardScope, oDashBoardModel) { //Anand: #Handover
			var oWebSocketPropertyData = oDashBoardModel.getProperty("/webSocketProperty/data"); //AN: #Notif
			oDashBoardModel.setProperty("/selectedTaskManagementVBox", "ShiftHandover");
			oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", true);
			oDashBoardModel.setProperty("/bypassObj", {});
			oDashBoardModel.setProperty("/bypassObj/bypassList", []);
			oDashBoardModel.setProperty("/bypassObj/bypassPersonRespList", []); //AN: #Notif
			oDashBoardModel.setProperty("/taskManagementPanel/showTaskManagementPanel", true);
			oDashBoardModel.setProperty("/taskManagementPanel/showBypassPanel", true);
			if (oWebSocketPropertyData && (oWebSocketPropertyData.byPassLogNotificationDto && oWebSocketPropertyData.byPassLogNotificationDto
					.byPassLogCount > 0) || (oWebSocketPropertyData.energyIsolationNotificationDto && oWebSocketPropertyData.energyIsolationNotificationDto
					.energyIsolationCount > 0)) { //AN: #Notif
				oDashBoardScope.setRowIndexToHighlightForNotif(oDashBoardModel, "ShiftHandover"); //AN: #highlightShift
				// oDashBoardScope.acknowledgeWebSocketRequest("ShiftHandover"); //AN: #BubbleNotifDemo
			}
			oDashBoardModel.setProperty("/shiftHandover/busyInd", true); //AN: #loaderWhileNotif
			this.fetchAllActiveData(oDashBoardScope, oDashBoardModel);
			oDashBoardModel.setProperty("/taskManagementPanel/showHandoverNotesPanel", false); //SH: HandoverNotes
		},
		/** 
		 * To fetch the list of all the active Bypass
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		fetchAllActiveData: function (oDashBoardScope, oDashBoardModel) { //Anand: #Handover
			oDashBoardModel.setProperty("/bypassObj", {});
			// oDashBoardModel.setProperty("/bypassObj/busyInd", true);
			oDashBoardModel.setProperty("/bypassObj/bypassList", []);
			oDashBoardModel.setProperty("/bypassObj/bypassPersonRespList", []);
			var sLoggedInTechRoles = oDashBoardScope.getLoggedInTechRoles(oDashBoardModel);
			var oChangeSeatData = oDashBoardModel.getProperty("/changeSeatData");
			var sBusinessRoles = oChangeSeatData.changeSeatIOPUserDisplayRoles;
			var sSystemDateTime = oDashBoardScope.formatter.iopDateFormatTimeStampValueDateTime(new Date());
			var sUrl = "/taskmanagementRest/bypassLog/getBypassLogListByUserGroup?technicalRole=" + sLoggedInTechRoles + "&businessRole=" +
				sBusinessRoles;
			var oModel = new sap.ui.model.json.JSONModel();
			oModel.loadData(sUrl, true, "GET", false, false);
			oModel.attachRequestCompleted(function (oEvent) {
				if (oEvent.getParameter("success")) {
					oDashBoardModel.setProperty("/shiftHandover/busyInd", false); //AN: #loaderWhileNotif
					if (oEvent) {
						var resultData = oEvent.getSource().getData();
						if (resultData) {
							resultData.shiftHandoverCount = resultData.length; //AN: #ShiftHandover
							var oAckData = oDashBoardModel.getData().highlightShiftHandoverList; //AN: #highlightShift
							try { //AN: #highlightShift
								if (oAckData) {
									for (var i = 0; i < oAckData.length; i++) {
										for (var j = 0; j < resultData.length; j++) {
											if (oAckData[i] == resultData[j].ssdBypassId) {
												resultData[j].shiftHandoverNotification = "notified";
												break;
											}
										}
									}
								}
							} catch (e) {

							}
							oDashBoardModel.setProperty("/bypassObj/bypassList", resultData);
							oDashBoardModel.refresh(true);
						}
					}
				} else {
					oDashBoardModel.setProperty("/shiftHandover/busyInd", false); //AN: #loaderWhileNotif
				}
			});
			oModel.attachRequestFailed(function (oEvent) {
				oDashBoardModel.setProperty("/shiftHandover/busyInd", false); //AN: #loaderWhileNotif
				MessageBox.error("Error in Retrieving Bypass List");
			});
		},
		/** 
		 * Function triggered on press of Active Bypass list item
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - the Event triggered
		 */
		onPressActiveBypassListitem: function (oDashBoardScope, oDashBoardModel, oContext) { //Anand: #Handover
			var oBypassId = oContext.ssdBypassId;
			oDashBoardScope.oBusyInd.open();
			oDashBoardModel.setProperty("/newByPassComment", "");
			var that = this;
			var sUrl = "/taskmanagementRest/bypassLog/getBypassLogById?bypassId=" + oBypassId;
			var oFragmentId = "idLocationHistoryBypassLogDetailsFragment",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.LocationHistoryBypassLogDetailsForm";
			oDashBoardModel.setProperty("/bypassObj/selPersonUserId", "");
			oDashBoardModel.setProperty("/bypassObj/selPersonName", "");
			if (oDashBoardModel.getProperty("/locationHistoryData/byPassLogPersonRespEditable")) { //AN: #ChangeShiftButtonVisibility
				this.getListOfPersonsResponsible(oDashBoardScope, oDashBoardModel, oContext); //AN: #Notif
			}
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				oDashBoardScope.oBusyInd.close();
				if (oData && oData.responseMessage.statusCode === "0") {
					oDashBoardModel.setProperty("/locationHistoryData/bypassLogDetailsData", oData);
					if (!oDashBoardScope.oBypassLogDialog) {
						oDashBoardScope.oBypassLogDialog = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
						oDashBoardScope.getView().addDependent(oDashBoardScope.oBypassLogDialog);
					}
					oDashBoardScope.oBypassLogDialog.open();
				} else {
					MessageBox.error(oData.responseMessage.message);
				}
			}.bind(oDashBoardScope), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				oDashBoardScope.oBusyInd.close();
			});

		},
		/** 
		 * To search for Active Bypass in the search List
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - the Event triggered
		 */
		onSearchActiveBypass: function (oDashBoardScope, oDashBoardModel, oEvent) { //Anand: #Handover
			var value = oEvent.getSource().getValue();
			var oBypassLogList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("taskmanagementpane1"), "idBypassLogList");
			var aFilters;
			var filterParams = ["location", "assignedTo", "source", "reasonForBypass", "ssdBypassNum"]; // Map the relevant parameters 
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
			oBypassLogList.getBinding("items").filter(aFilters);
		},
		/** 
		 * To fetch list of persons responsible for bypass log
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oContext - data of selected bypass log
		 */
		getListOfPersonsResponsible: function (oDashBoardScope, oDashBoardModel, oContext) { //AN: #Notif
			oDashBoardModel.setProperty("/bypassPersonRespList", []);
			var sUrl = "/taskmanagementRest/locationGT/getNearestUsers";
			if (oContext.locationCode) {
				sUrl = sUrl + "?locationCode=" + oContext.locationCode;
			}
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				oDashBoardModel.setProperty("/bypassPersonRespList", oData.nearestUsers);
			}, function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},
		/** 
		 * Function to Update Active Bypass Log
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onPressBypassLogButtons: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #Notif
			var that = this;
			var sUrl = "",
				oPayload,
				oBypassLogDetailsData = oDashBoardModel.getProperty("/locationHistoryData/bypassLogDetailsData");
			if (!oDashBoardModel.getData().bypassObj.selPersonName) {
				MessageToast.show("Please select a user for Change Shift");
				return "";
			}
			var oBypasslogDetails = oDashBoardModel.getData().locationHistoryData.bypassLogDetailsData.ssdBypassLogHeaderdto
			switch (oEvent.getSource().getText()) {
			case "Change Shift":
				sUrl = "/taskmanagementRest/bypassLog/createBypassActivityLog";
				oPayload = {
					"ssdBypassId": oBypasslogDetails.ssdBypassId,
					"personResponsible": oDashBoardModel.getData().bypassObj.selPersonName,
					"operatorType": "operator",
					"personId": oDashBoardModel.getData().bypassObj.selPersonUserId,
					"activityType": "ASSIGNED"
				};
				break;
			default:
				break;
			}
			oDashBoardScope.oBusyInd.open();
			oDashBoardScope.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData && oData.statusCode === "0") {
					oDashBoardScope.oBypassLogDialog.close();
					oDashBoardModel.setProperty("/shiftHandover/busyInd", true); //AN: #loaderWhileNotif
					that.fetchAllActiveData(oDashBoardScope, oDashBoardModel);
					MessageToast.show("Shift Change Request Sent Successfully");
					oDashBoardScope.oBusyInd.close();
				}
			}, function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				oDashBoardScope.oBusyInd.close();
			});
		},
		/** 
		 * Function to Update Active Bypass Log Person Responsible
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oContext - Context Obj from the oEvent
		 */
		onPressEnergyIsoListitem: function (oDashBoardScope, oDashBoardModel, oContext) { //Anand: #Handover
			var oFormId = oContext.ssdBypassId;
			oDashBoardScope.oBusyInd.open();
			var that = this;
			var sUrl = "/taskmanagementRest/energyIsolation/form?formId=" + oFormId;
			oDashBoardModel.setProperty("/bypassObj/selPersonUserId", "");
			oDashBoardModel.setProperty("/bypassObj/selPersonName", "");
			if (oDashBoardModel.getProperty("/locationHistoryData/byPassLogPersonRespEditable")) { //AN: #ChangeShiftButtonVisibility
				this.getListOfPersonsResponsible(oDashBoardScope, oDashBoardModel, oContext); //AN: #Notif
			}
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				oDashBoardScope.getModel("dashBoardModel").setProperty("/ptwEnergyIsolationDetail", oData);
				oDashBoardScope.oBusyInd.close();
			}.bind(oDashBoardScope), function (oError) {
				oDashBoardScope.oBusyInd.close();
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(oDashBoardScope));

			var oFragmentIdEI = "idEngeryIsolationForm",
				oFragmentNameEI = "com.sap.incture.Incture_IOP.fragment.EnergyIsolationForm";
			if (!oDashBoardScope.oEnergyIsolationForm) {
				oDashBoardScope.oEnergyIsolationForm = oDashBoardScope._createFragment(oFragmentIdEI, oFragmentNameEI);
				oDashBoardScope.getView().addDependent(oDashBoardScope.oEnergyIsolationForm);
			}
			oDashBoardScope.oEnergyIsolationForm.open();

		},
		/** 
		 * Function to Update Energy Iso Permit Issuer
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onPressEnergyIsoLogButtons: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #Notif
			var that = this;
			var sUrl = "",
				oPayload;
			if (!oDashBoardModel.getData().bypassObj.selPersonName) {
				MessageToast.show("Please select a user for Change Shift");
				return "";
			}
			var oEnergyIsoDetails = oDashBoardModel.getData().ptwEnergyIsolationDetail;
			switch (oEvent.getSource().getText()) {
			case "Change Shift":
				sUrl = "/taskmanagementRest/energyIsolation/createActivity";
				oPayload = {
					"formId": oEnergyIsoDetails.formId,
					"permIssueName": oDashBoardModel.getData().bypassObj.selPersonName,
					"permIssueLoginName": oDashBoardModel.getData().bypassObj.selPersonUserId
				};
				break;
			default:
				break;
			}
			oDashBoardScope.oBusyInd.open();
			oDashBoardScope.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData && oData.statusCode === "0") {
					oDashBoardScope.oEnergyIsolationForm.close();
					oDashBoardModel.setProperty("/shiftHandover/busyInd", true); //AN: #loaderWhileNotif
					that.fetchAllActiveData(oDashBoardScope, oDashBoardModel);
					MessageToast.show("Shift Change Request Sent Successfully");
					oDashBoardScope.oBusyInd.close();
				}
			}, function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				oDashBoardScope.oBusyInd.close();
			});
		},
		getAvailableOperators: function (oDashBoardScope, sClassification, sSubClassification) { //AN: #operatorsAvailable
			var oTaskPanelDetailModel = oDashBoardScope.getModel("oTaskPanelDetailModel");
			var oDashBoardModel = oDashBoardScope.getModel("dashBoardModel");
			var aNearestUsers = oDashBoardModel.getProperty("/suggestionItems");
			var sLocationCode = oDashBoardModel.getProperty("/sLocCodeToGetCurrentNearByUser");
			var sUrl = "/taskmanagementRest/locationGT/getNearestUsers";
			var sTaskType = "";
			if ((!oDashBoardModel.getProperty("/isInvestigationCreate") && !oDashBoardModel.getProperty("/isInquiryCreate"))) {
				var sTaskType = "Dispatch"
			}
			if (sLocationCode) {
				if (oDashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
					var oCurrSelWBMsgData = oDashBoardModel.getProperty("/InsightToActionData/oCurrSelWBMsgData");
					if (oCurrSelWBMsgData.locationType === "Compressor") {
						sUrl = sUrl + "?locationCode=" + oCurrSelWBMsgData.parentLocationCode + "&taskType=" + sTaskType;
					} else {
						sUrl = sUrl + "?locationCode=" + sLocationCode + "&taskType=" + sTaskType;
					}
				} else {
					sUrl = sUrl + "?locationCode=" + sLocationCode + "&taskType=" + sTaskType;
				}
				var isEfsOrCa = "";
				if (sLocationCode.startsWith("MUR-US")) {
					isEfsOrCa = "EFS";
				} else if (sLocationCode.startsWith("MUR-CA")) {
					isEfsOrCa = "CA";
				}
				oDashBoardModel.setProperty("/suggestionItemsCountry", isEfsOrCa);
			}
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				oDashBoardModel.setProperty("/suggestionItems", oData.nearestUsers);
				var aBindedTokens = oDashBoardModel.getProperty("/currentSelectedObjects");
				if (aBindedTokens && aBindedTokens.length > 0) {
					if (!oData || !oData.nearestUsers || (oData && oData.nearestUsers && oData.nearestUsers.length === 0)) {
						var eSuggestionList = oDashBoardScope.oSuggestionList.getContent()[0].getItems()[0];
						eSuggestionList.removeSelections(true);
						oDashBoardModel.setProperty("/currentSelectedObjects", []);
					} else {
						for (var a = 0; a < aBindedTokens.length; a++) {
							for (var b = 0; b < oData.nearestUsers.length; b++) {
								if (aBindedTokens[a].pId === oData.nearestUsers[b].pId) {
									break;
								} else if (b === oData.nearestUsers.length) {
									oDashBoardScope.removeSelections(aBindedTokens[a], "token");
								}
							}
						}
					}

				}
				var oStatus;
				var oList = oDashBoardScope.getModel("oTaskPanelDetailModel").getData().customAttr;
				for (var i = 0; i < oList.length; i++) {
					if (oList[i].label === "Status") {
						oStatus = oList[i].labelValue;
					}
				}
				var oDefaultUser = oData.nearestUsers[0];
				if (oDefaultUser && aBindedTokens && aBindedTokens.length === 0) {
					oDashBoardModel.setProperty("/assignedToIndex", 0);
					var array = [];
					array.push({
						"firstName": oDefaultUser.firstName,
						"lastName": oDefaultUser.lastName,
						"emailId": oDefaultUser.emailId,
						"userId": oDefaultUser.userId,
						"pId": oDefaultUser.pId
					});
					oDashBoardModel.setProperty("/currentSelectedObjects", array);
					var oFragmentId = "oSuggestionPopover",
						oFragmentName = "com.sap.incture.Incture_IOP.fragment.SuggestionListPopover";
					if (!oDashBoardScope.oSuggestionList) {
						oDashBoardScope.oSuggestionList = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
						oDashBoardScope.getView().addDependent(oDashBoardScope.oSuggestionList);
					}
					var oFrag = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist");
					if (oFrag) {
						var oItems = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist").getItems();
						oItems[0].setSelected(true);
						oDashBoardScope.setSelections();
					}
				}
			}, function (oError) {
				var sErrorMessage;
				if (oTaskList && oTaskList.getContent() && oTaskList.getContent()[oBusyIndex] && oBusyIndex) {
					oTaskList.getContent()[oBusyIndex].setBusy(false);
				}
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},
		/*getAvailableOperators: function (oDashBoardScope, sClassification, sSubClassification) { //AN: #operatorsAvailable
			var oTaskPanelDetailModel = oDashBoardScope.getModel("oTaskPanelDetailModel");
			var oDashBoardModel = oDashBoardScope.getModel("dashBoardModel");
			var aNearestUsers = oDashBoardModel.getProperty("/suggestionItems");
			var sLocationCode = oDashBoardModel.getProperty("/sLocCodeToGetCurrentNearByUser");
			var sUrl = "/taskmanagementRest/locationGT/getNearestUsers";
			if (sLocationCode) {
				sUrl = sUrl + "?locationCode=" + sLocationCode;
			}
			var oTaskList = sap.ui.core.Fragment.byId("idCreateTaskPanel", "idTaskDetailPanel--idDynGrid");
			if (oTaskList && oTaskList.getContent()) {
				for (var x = 0; x < oTaskList.getContent().length; x++) {
					if (oTaskList.getContent()[x].getItems() && oTaskList.getContent()[x].getItems()[0].getText() === "Assign to person(s)") {
						oTaskList.getContent()[x].setBusyIndicatorDelay(0);
						oTaskList.getContent()[x].setBusy(true);
						var oBusyIndex = x;
						break;
					}
				}
			}
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				oDashBoardModel.setProperty("/suggestionItems", oData.nearestUsers);
				var oUrl = "/taskmanagementRest/locationBL/operatorsAvailable";
				var oPayload = {
					classification: sClassification,
					subClassifiaction: sSubClassification,
					nearestUserDto: oData.nearestUsers
				}
				oDashBoardScope.doAjax(oUrl, "POST", oPayload, function (oData) {
					oDashBoardModel.setProperty("/suggestionItems", oData.nearestUsers);
					var aBindedTokens = oDashBoardModel.getProperty("/currentSelectedObjects");
					if (aBindedTokens && aBindedTokens.length > 0) {
						if (!oData || !oData.nearestUsers || (oData && oData.nearestUsers && oData.nearestUsers.length === 0)) {
							var eSuggestionList = oDashBoardScope.oSuggestionList.getContent()[0].getItems()[0];
							eSuggestionList.removeSelections(true);
							oDashBoardModel.setProperty("/currentSelectedObjects", []);
						} else {
							for (var a = 0; a < aBindedTokens.length; a++) {
								for (var b = 0; b < oData.nearestUsers.length; b++) {
									if (aBindedTokens[a].pId === oData.nearestUsers[b].pId) {
										break;
									} else if (b === oData.nearestUsers.length) {
										oDashBoardScope.removeSelections(aBindedTokens[a], "token");
									}
								}
							}
						}

					}
					var oStatus;
					var oList = oDashBoardScope.getModel("oTaskPanelDetailModel").getData().customAttr;
					for (var i = 0; i < oList.length; i++) {
						if (oList[i].label === "Status") {
							oStatus = oList[i].labelValue;
						}
					}
					if (oStatus === "DRAFT") {
						oDashBoardModel.setProperty("/assignedToIndex", 0);
						var array = [];
						var oDefaultUser = oData.nearestUsers[0];
						if (oDefaultUser && aBindedTokens && aBindedTokens.length === 0) {
							array.push({
								"firstName": oDefaultUser.firstName,
								"lastName": oDefaultUser.lastName,
								"emailId": oDefaultUser.emailId,
								"userId": oDefaultUser.userId,
								"pId": oDefaultUser.pId
							});
							oDashBoardModel.setProperty("/currentSelectedObjects", array);
							var oFragmentId = "oSuggestionPopover",
								oFragmentName = "com.sap.incture.Incture_IOP.fragment.SuggestionListPopover";
							if (!oDashBoardScope.oSuggestionList) {
								oDashBoardScope.oSuggestionList = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
								oDashBoardScope.getView().addDependent(oDashBoardScope.oSuggestionList);
							}
							var oFrag = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist");
							if (oFrag) {
								var oItems = sap.ui.core.Fragment.byId("oSuggestionPopover", "suggestionlist").getItems();
								oItems[0].setSelected(true);
								oDashBoardScope.setSelections();
							}
						}
					}
					if (oTaskList && oTaskList.getContent() && oTaskList.getContent()[oBusyIndex] && oBusyIndex) {
						oTaskList.getContent()[oBusyIndex].setBusy(false);
					}
				}, function (oError) {
					var sErrorMessage;
					if (oTaskList && oTaskList.getContent() && oTaskList.getContent()[oBusyIndex] && oBusyIndex) {
						oTaskList.getContent()[oBusyIndex].setBusy(false);
					}
					sErrorMessage = oError.getParameter("statusText");
					oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				});
			}, function (oError) {
				var sErrorMessage;
				if (oTaskList && oTaskList.getContent() && oTaskList.getContent()[oBusyIndex] && oBusyIndex) {
					oTaskList.getContent()[oBusyIndex].setBusy(false);
				}
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},*/
		onPostShiftChangeComment: function (oDashBoardScope, oDashBoardModel) {
			oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", true);
			var sUrl = "/taskmanagementRest/bypassLog/updateBypassLog";
			var oTextArea1 = sap.ui.core.Fragment.byId("idLocationHistoryBypassLogDetailsFragment", "idBypassCommTextArea");
			// var sComment = oTextArea1.getValue();
			var sComment = oDashBoardModel.getProperty("/newByPassComment");
			if (!sComment) {
				oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
				oDashBoardScope._showToastMessage("Enter a comment to post");
				return;
			}
			var oPayload = {};
			var oSsdBypassLogHeaderdto = oDashBoardModel.getProperty("/locationHistoryData/bypassLogDetailsData/ssdBypassLogHeaderdto");
			oPayload.ssdBypassLogHeaderdto = oSsdBypassLogHeaderdto;
			oPayload.ssdBypassCommentDtoList = [{
				comment: sComment,
				updatedBy: oDashBoardModel.getProperty("/userData/displayName")
			}];
			oDashBoardScope.doAjax(sUrl, "PUT", oPayload, function (oData) {
				if (oData && oData.statusCode === "0") {
					oDashBoardModel.setProperty("/newByPassComment", "");
					var oUrl = "/taskmanagementRest/bypassLog/getBypassLogById?bypassId=" + oSsdBypassLogHeaderdto.ssdBypassId;
					oDashBoardScope.doAjax(oUrl, "GET", null, function (oData) {
						if (oData && oData.responseMessage.statusCode === "0") {
							oDashBoardModel.setProperty("/locationHistoryData/bypassLogDetailsData", oData);
							oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
						} else {
							oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
							MessageBox.error(oData.responseMessage.message);
						}
					}, function (oError) {
						var sErrorMessage;
						sErrorMessage = oError.getParameter("statusText");
						oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
						oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
					});
				} else {
					oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
					MessageBox.error(oData.message);
				}
			}, function (oError) {
				oDashBoardModel.setProperty("/busyIndicators/collabPanelBusy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});
		},
		fetchOpAvailability: function (oDashBoardScope, oDashBoardModel) { //AN: #OpAvail
			var sTechRole = "";
			var sLoggedInTechRoles = oDashBoardScope.getLoggedInTechRoles(oDashBoardModel);
			if (sLoggedInTechRoles && (sLoggedInTechRoles.search("POT") >= 0 || sLoggedInTechRoles.search("Engineer") >= 0)) {
				var aLoggedInTechRoles = sLoggedInTechRoles.split(",");
				for (var i = 0; i < aLoggedInTechRoles.length; i++) {
					if (aLoggedInTechRoles[i].search("ROC") >= 0) {
						if (sTechRole !== "") {
							sTechRole = sTechRole + "," + aLoggedInTechRoles[i];
						} else {
							sTechRole = sTechRole + aLoggedInTechRoles[i];
						}
					}
				}
			} else {
				sTechRole = sLoggedInTechRoles;
			}
			var sUrl = "/taskmanagementRest/tasks/getFieldWiseAvailability?technicalRole=" + sTechRole;
			oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
				if (oData && oData.responseMessage.statusCode === "0") {
					oDashBoardModel.setProperty("/opAvailObj", {
						opAvailCumilative: oData.cumulativePercentage,
						opAvailCumilativeInString: oData.cumulativePercent,
						opColorCumilative: "None",
						opAvailtotal: 100
					});
					for (var a = 0; a < oData.fieldAvailabilityDtoList.length; a++) {
						if (oData.fieldAvailabilityDtoList[a].noOfOperators) {
							oData.fieldAvailabilityDtoList[a].noOfOperatorsInString = oData.fieldAvailabilityDtoList[a].noOfOperators.toString();
						}
					}
					oDashBoardModel.setProperty("/opAvailObj/opAvailList", oData.fieldAvailabilityDtoList);
				} else {
					MessageBox.error(oData.responseMessage.message);
				}
				oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
			}, function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
			});
		},
		onOpAvailPanelExpandPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #OpAvail
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			var idOpAvailPanel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idOpAvailPanel");
			if (idOpAvailPanel.getExpanded()) {
				idOpAvailPanel.setExpanded(false);
			} else {
				idOpAvailPanel.setExpanded(true);
			}
		},
		onOpAvailSort: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #OpAvail
			// var oDTList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("taskmanagementpane1"), "idOpAvailTable");
			var oDTList = oEvent.getSource().getParent().getParent().getParent(); //Find BETTER SOLUTION
			var aSorters = [];
			if (this.bDescending) {
				this.bDescending = false;
			} else {
				this.bDescending = true;
			}
			aSorters.push(new sap.ui.model.Sorter("availablePercent", this.bDescending));
			oDTList.getBinding("items").sort(aSorters);
		},
		onSearchOpAvail: function (oDashBoardScope, oDashBoardModel, oEvent) { //AN: #OpAvail
			var value = oEvent.getSource().getValue();
			var oOpAvailList = sap.ui.core.Fragment.byId(oDashBoardScope.createId("taskmanagementpane1"), "idOpAvailList");
			var aFilters;
			var filterParams = ["field", "fieldAvailabilityPercentage", "noOfOperatorsInString", "firstName", "lastName", "operatorType"]; // Map the relevant parameters 
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
			oOpAvailList.getBinding("items").filter(aFilters);
		},
		onEnergyIsolationComment: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var sUrl = "/taskmanagementRest/energyIsolation/write";
			var sComment = oDashBoardModel.getProperty("/newEnergyIsolationComment");
			if (!sComment) {
				oDashBoardScope._showToastMessage("Enter a comment to post");
				return;
			}
			var oPayload = oDashBoardModel.getProperty("/ptwEnergyIsolationDetail");
			var sformId = oPayload.formId;
			var aCommentList = oPayload.commentList;
			if (aCommentList && aCommentList.length > 0) {
				oPayload.commentList.push({
					comment: sComment,
					updatedBy: oDashBoardModel.getProperty("/userData/displayName")
				})
			} else {
				oPayload.commentList = [{
					comment: sComment,
					updatedBy: oDashBoardModel.getProperty("/userData/displayName")
				}];
			}
			oPayload.update = true;
			oDashBoardScope.doAjax(sUrl, "POST", oPayload, function (oData) {
				if (oData && oData.statusCode === "0") {
					oDashBoardModel.setProperty("/newEnergyIsolationComment", "");
					var sUrl = "/taskmanagementRest/energyIsolation/form?formId=" + sformId;
					oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
						oDashBoardScope.getModel("dashBoardModel").setProperty("/ptwEnergyIsolationDetail", oData);
					}.bind(oDashBoardScope), function (oError) {
						oDashBoardScope.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
						var sErrorMessage;
						sErrorMessage = oError.getParameter("statusText");
						oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
					}.bind(oDashBoardScope));
				} else {
					MessageBox.error(oData.message);
				}
			}, function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			});

		},
		onHanoverNotesFilterPanelExpand: function (oDashBoardScope, oDashBoardModel, oEvent) { //SH: HandoverNotes
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			// this.validateStyling(oDashBoardScope, oDashBoardModel, oEvent, oEvent.getSource().getExpanded(), taskmanagementpane1);
			if (oEvent.getSource().getExpanded()) {
				oDashBoardModel.setProperty("/handoverFilterData/isFilterPanelExpanded", true);
			} else {
				oDashBoardModel.setProperty("/handoverFilterData/isFilterPanelExpanded", false);
			}
		},
		onHandoverfilterLinkPress: function (oDashBoardScope, oDashBoardModel, oEvent) { //SH: HandoverNotes
			var taskmanagementpane1 = oDashBoardScope.getView().createId("taskmanagementpane1");
			var idHandoverFilterPanel = sap.ui.core.Fragment.byId(taskmanagementpane1, "idHandoverNotesFilterPanel");
			if (idHandoverFilterPanel.getExpanded()) {
				idHandoverFilterPanel.setExpanded(false);
				oDashBoardModel.setProperty("/handoverFilterData/isFilterPanelExpanded", false);
			} else {
				idHandoverFilterPanel.setExpanded(true);
				oDashBoardModel.setProperty("/handoverFilterData/isFilterPanelExpanded", true);
			}
		},
		onHandoverFieldFilterChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var handoverFilterData = oDashBoardModel.getProperty("/handoverFilterData");
			var sSelectedFieldKey = [];
			var sSelectedField = [];
			var oSelectedItems = oEvent.getSource().getSelectedItems();
			if (oSelectedItems && oSelectedItems.length > 0) {
				for (var i in oSelectedItems) {
					sSelectedFieldKey.push(oSelectedItems[i].getProperty("key"));
					sSelectedField.push(oSelectedItems[i].getProperty("text"));
				}
			}
			if (oEvent.getSource().getSelectedItems()) {
				handoverFilterData.selectedFieldKey = sSelectedFieldKey.join(",");
				handoverFilterData.selectedField = sSelectedField.join(",");
			} else {
				handoverFilterData.selectedField = "";
				handoverFilterData.selectedFieldKey = "";
			}

			oEvent.getSource().close();
			oDashBoardScope.onHandoverFetchPress();
		},
		idHandoverShiftFilterChange: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var handoverFilterData = oDashBoardModel.getProperty("/handoverFilterData");
			var sSelectedShiftKey = [];
			var sSelectedShift = [];
			var oSelectedItems = oEvent.getSource().getSelectedItems();
			if (oSelectedItems && oSelectedItems.length > 0) {
				for (var i in oSelectedItems) {
					sSelectedShiftKey.push(oSelectedItems[i].getProperty("key"));
					sSelectedShift.push(oSelectedItems[i].getProperty("text"));
				}
			}
			if (oEvent.getSource().getSelectedItems()) {
				handoverFilterData.selectedShiftKey = sSelectedShiftKey.join(",");
				handoverFilterData.selectedShift = sSelectedShift.join(",");
			} else {
				handoverFilterData.selectedShiftKey = "";
				handoverFilterData.selectedShift = "";
			}
			oEvent.getSource().close();
			oDashBoardScope.onHandoverFetchPress();
		}
	};
});