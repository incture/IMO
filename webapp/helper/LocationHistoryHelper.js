sap.ui.define(["sap/m/MessageToast",
	"sap/m/MessageBox"
], function (MessageToast, MessageBox) {
	"use strict";
	return {
		/** 
		 * Function to initialize model for location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		_initializeLocalModelForLocationHistoryPanel: function (oModel) {
			//this.oBusyDialog = new sap.m.BusyDialog();
			oModel.setProperty("/locationHistoryData", {
				locHistoryTaskData: [],
				locHistoryDowntimeData: [],
				locHistoryWorkorderData: [],
				workOrderDetailsData: [],
				locHistoryPTWJSAData: [],
				locHistoryTaskStatusList: [],
				locHistoryWellDowntimeStatusList: 0,
				locHistoryCompressorDowntimeStatusList: 0,
				locHistoryFlareDowntimeStatusList: 0,
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
				bDescending: true,
				byPassLogPersonRespEditable: false //AN: #Notif

			});
			oModel.setProperty("/locationHistorySelectTabs", {
				iconFilterTab: "Task",
				LocHistoryDowntimeFilterKey: "Well",
				LocHistoryDowntimeFilterKeys: "Well",
				LocHistoryPTWFilterKey: "Active JSA",
				monthFilterWorkorder: "1",
				monthFilterPTW: "one",
				monthFilterTask: "one",
				monthFilterDowntime: "one",
				monthFilterEnergyIsolation: "one",
				monthFilterBypassLog: "1",
				selectedForm: ""
			});
			oModel.setProperty("/locationHistoryPagination", {
				LocHistoryDowntimePageNo: "1",
				ptwPageNumber: "1",
				LocHistoryTaskPageNo: "1",
				LocHistoryWorkOrderPageNo: "1",
				LocHistoryEnergyIsolationPageNo: "1",
				LocHistoryBypassLogPageNo: "1",
				LocHistoryNextButtonEnable: true,
				LocHistoryPreviousButtonEnable: false
			});
		},
		/** 
		 * Function to Call on selection change of location History Icon tab bar- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		locHistoryIconTabSelect: function (scope, oEvent, dashBoardModel) {
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oSelectedKey = oEvent;
			scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
			if (!oLocationSelected.length) {
				var sText = "Select a location to fetch " + oSelectedKey + " History!";
				MessageToast.show(sText);
			} else {
				switch (oSelectedKey) {
				case "Task":
					this.getTaskList(scope, oEvent, dashBoardModel);
					break;
				case "Downtime":
					this.getAllDowntimeList(scope, oEvent, dashBoardModel);
					//	this.getDowntimeList(scope, oEvent, dashBoardModel);
					break;
				case "Workorder":
					this.getWorkorderList(scope, oEvent, dashBoardModel);
					break;
				case "Permit To Work":
					var PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
					var PTWMonthFilterKey = dashBoardModel.getProperty("/locationHistorySelectTabs/monthFilterPTW");

					scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
					scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
					scope.getPermittoWorkList(PTWFilterKey, PTWMonthFilterKey);
					break;
				case "Bypass Log":
					this.getBypassLogList(scope, oEvent, dashBoardModel);
					break;
				case "Energy Isolation":
					scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/EnergyIsolationPageNumber", 1);
					this.getEnergyIsolationList(scope, oEvent, dashBoardModel);
					break;
				}
			}
		},
		/** 
		 * Function to get Task History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getTaskList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryTaskPageNo;
			var oPageSize = scope.getView().getModel("dashBoardModel").getData().locationHistoryData.pageSize;
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oLocationCode = "";
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterTask;
			if (oLocationSelected.length > 0) {
				for (var i = 0; i < oLocationSelected.length - 1; i++) {
					oLocationCode += oLocationSelected[i].location + ",";
				}
				oLocationCode += oLocationSelected[i].location;
				var sUrl = "/taskmanagementRest/locationHistory/getLocTaskHistory?locationCode=" + oLocationCode + "&monthTime=" + oMonth +
					"&page=" + oPageNo + "&page_size=" + oPageSize;
				scope.doAjax(sUrl, "GET", null, function (oData) {
					if (oData && oData.responseMessage.statusCode === "0") {
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryTaskData", oData);
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryTaskStatusList", oData.statusCountList);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryTaskPageNo;
						var oTotalCount = oData.totalCount;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
					}
				}.bind(scope), function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					sap.m.MessageToast.show(sErrorMessage);
					/*scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);*/
				}.bind(scope));
			}
		},
		/** 
		 * Function to get Task Details- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getTaskDetails: function (scope, oEvent, dashBoardModel) {
			var that = this;
			//	scope.oBusyInd.open();
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", true);
			var sProcessId = oEvent.getSource().getBindingContext("dashBoardModel").getObject().processId;
			var oCurrentTaskObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/curreentTaskObject", oCurrentTaskObject);
			var sUserRole = scope.getView().getModel("dashBoardModel").getData().userData.sGroup;
			var oUrl = "/taskmanagementRest/collaboration/readByTaskId?taskId=" + sProcessId + "&userType=" + sUserRole;
			var oFragmentId = "idLocationHistoryTaskDetailsFragment",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.LocationHistoryTaskDetails";
			if (!scope.oTaskDialog) {
				scope.oTaskDialog = scope._createFragment(oFragmentId, oFragmentName);
				scope.getView().addDependent(scope.oTaskDialog);
			}
			scope.oTaskDialog.open();
			//oFragmentName = "com.sap.incture.Incture_IOP.fragment.LocationHistoryBypassLogDetailsForm";
			scope.doAjax(oUrl, "GET", null, function (oData) {
				//	scope.oBusyInd.close();
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
				if (oData.responseMessage.statusCode === "0" && oData.collaborationDtos) {
					var oAttachment = [];
					var oComments = [];
					for (var i = 0; i < oData.collaborationDtos.length; i++) {
						if (oData.collaborationDtos[i].attachmentDetails && oData.collaborationDtos[i].attachmentDetails.length > 0) {
							for (var j = 0; j < oData.collaborationDtos[i].attachmentDetails.length; j++) {
								var oAttach = {
									compressedFile: oData.collaborationDtos[i].attachmentDetails[j].compressedFile,
									fileDoc: oData.collaborationDtos[i].attachmentDetails[j].fileDoc,
									fileId: oData.collaborationDtos[i].attachmentDetails[j].fileId,
									fileName: oData.collaborationDtos[i].attachmentDetails[j].fileName,
									fileType: oData.collaborationDtos[i].attachmentDetails[j].fileType,
									mappingId: oData.collaborationDtos[i].attachmentDetails[j].mappingId,
									validForUsage: oData.collaborationDtos[i].attachmentDetails[j].validForUsage,
									documentUrl: oData.collaborationDtos[i].attachmentDetails[j].documentUrl,
									createdAt: oData.collaborationDtos[i].createdAt,
									createdAtDisplay: oData.collaborationDtos[i].createdAtDisplay,
									message: oData.collaborationDtos[i].message,
									messageId: oData.collaborationDtos[i].messageId,
									processId: oData.collaborationDtos[i].processId,
									taskId: oData.collaborationDtos[i].taskId,
									userDisplayName: oData.collaborationDtos[i].userDisplayName,
									userId: oData.collaborationDtos[i].userId
								};
								oAttachment.push(oAttach);
							}
						}
						if (oData.collaborationDtos[i].message) {
							var oComment = {
								createdAt: oData.collaborationDtos[i].createdAt,
								createdAtDisplay: oData.collaborationDtos[i].createdAtDisplay,
								message: oData.collaborationDtos[i].message,
								messageId: oData.collaborationDtos[i].messageId,
								processId: oData.collaborationDtos[i].processId,
								taskId: oData.collaborationDtos[i].taskId,
								userDisplayName: oData.collaborationDtos[i].userDisplayName,
								userId: oData.collaborationDtos[i].userId
							};
							oComments.push(oComment);
						}
					}
					var oTaskDetails = {
						AttachmentDto: oAttachment,
						CommentDto: oComments
					};
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/taskDetailsData", oTaskDetails);
				} else {
					oTaskDetails = {
						AttachmentDto: [],
						CommentDto: []
					};
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/taskDetailsData", oTaskDetails);
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
					//scope.oBusyInd.close();
					//sap.m.MessageToast.show(oData.responseMessage.message);
					sap.m.MessageToast.show("No Comment or Attachment found");
				}
			}.bind(scope), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				sap.m.MessageToast.show(sErrorMessage);
				var oTaskDetails = {
					AttachmentDto: [],
					CommentDto: []
				};
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/taskDetailsData", oTaskDetails);
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
				//scope.oBusyInd.close();
			}.bind(scope));

		},
		getAllDowntimeList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var sUrl = "/taskmanagementRest/locationHistoryMobile/getDowntimeMob";
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryDowntimePageNo;
			var oPageSize = scope.getView().getModel("dashBoardModel").getData().locationHistoryData.pageSize;
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterDowntime;
			var oLoactionType = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentLocationType;
			var oFilterSel = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.LocHistoryDowntimeFilterKeys;
			var country = scope.getView().getModel("dashBoardModel").getProperty("/currentLocationInHierarchy");
			var statusType;
			if (oLoactionType === "SEARCH") {
				oLoactionType = "Well";
			}
			if (!oFilterSel) {
				oFilterSel = "None";
			}
			var oPayload = {
				"statusType": "DESIGNATED",
				"locationType": oLoactionType,
				"monthTime": oMonth,
				"page": Number(oPageNo),
				"locationHierarchy": oLocationSelected,
				"page_size": oPageSize,
				"type_selected": oFilterSel,
				"countryCode": country
			};
			if (country === "EFS") {
				oPayload.countryCode = "US";
			}
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
			scope.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData && oData.responseMessage.status === "SUCCESS") {
						/*	for (var i = 0; i < oData.custm.length; i++) {
								var oDate = oData.dtoList[i].createdAt;
								var sDate = new Date(oDate);
								oData.custm[i].createdOnstring = created_At;
							}*/
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", oData.custm);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", oData.itemsList);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryDowntimePageNo;
						var oTotalCount = oData.totalCount;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", "");
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
					}

				}.bind(this),
				function (oError) {
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
					scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", "");
					scope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));

		},
		/** 
		 * Function to get Downtime History List Based on DropDown Value select- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getDowntimeList: function (scope, oEvent, dashBoardModel) {
			var sDowntimeSelectItem = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.LocHistoryDowntimeFilterKey;
			switch (sDowntimeSelectItem) {
			case "Well":
				this.getWellDownTimeList(scope, oEvent, dashBoardModel);
				break;
			case "Compressor":
				this.getCompressorDownTimeList(scope, oEvent, dashBoardModel);
				break;
			case "Flare":
				this.getFlareDownTimeList(scope, oEvent, dashBoardModel);
				break;
			}
		},
		/** 
		 * Function to get Well Downtion Time History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getWellDownTimeList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var sUrl = "/taskmanagementRest/downtimeCapture/getDowntime";
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryDowntimePageNo;
			var oPageSize = scope.getView().getModel("dashBoardModel").getData().locationHistoryData.pageSize;
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterDowntime;
			var oLoactionType = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentLocationType;
			if (oLoactionType === "SEARCH") {
				oLoactionType = "Well";
			}
			var oPayload = {
				"statusType": "DESIGNATED",
				"locationType": oLoactionType,
				"monthTime": oMonth,
				"page": Number(oPageNo),
				"locationHierarchy": oLocationSelected,
				"page_size": oPageSize

			};
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
			scope.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData && oData.message.status === "SUCCESS") {
						for (var i = 0; i < oData.dtoList.length; i++) {
							var oDate = oData.dtoList[i].createdAt;
							var sDate = new Date(oDate);
							oData.dtoList[i].createdOnstring = sDate;
						}
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", oData.dtoList);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", oData.itemList);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryDowntimePageNo;
						var oTotalCount = oData.totalCount;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", "");
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
					}

				}.bind(this),
				function (oError) {
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
					scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", "");
					scope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		/** 
		 * Function to get Compressor Downtion Time History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getCompressorDownTimeList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var sUrl = "/taskmanagementRest/compressorDowntime/getDowntime";
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryDowntimePageNo;
			var oPageSize = scope.getView().getModel("dashBoardModel").getData().locationHistoryData.pageSize;
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterDowntime;
			var oLoactionType = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentLocationType;
			var oPayload = {
				"statusType": "SUBMITTED",
				"locationType": oLoactionType,
				"monthTime": oMonth,
				"page": Number(oPageNo),
				"locationHierarchy": oLocationSelected,
				"page_size": oPageSize
			};
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
			scope.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.message.status === "SUCCESS") {
						for (var i = 0; i < oData.dtoList.length; i++) {
							var oDate = oData.dtoList[i].createdAt;
							var sDate = new Date(oDate);
							oData.dtoList[i].createdOnstring = sDate;
						}
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", oData.dtoList);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", oData.itemList);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryDowntimePageNo;
						var oTotalCount = oData.totalCount;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryCompressorDowntimeStatusList", "");
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
					}

				}.bind(this),
				function (oError) {
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
					scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryCompressorDowntimeStatusList", "");
					scope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		/** 
		 * Function to get Flare Downtion Time History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getFlareDownTimeList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var sUrl = "/taskmanagementRest/flareDowntime/getDowntime";
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryDowntimePageNo;
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterDowntime;
			var oPageSize = scope.getView().getModel("dashBoardModel").getData().locationHistoryData.pageSize;
			var oLoactionType = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentLocationType;
			var oPayload = {
				"statusType": "SUBMITTED",
				"locationType": oLoactionType,
				"monthTime": oMonth,
				"page": Number(oPageNo),
				"locationHierarchy": oLocationSelected,
				"page_size": oPageSize
			};
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
			scope.doAjax(sUrl, "POST", oPayload, function (oData) {
					if (oData.message.status === "SUCCESS") {
						for (var i = 0; i < oData.dtoList.length; i++) {
							var oDate = oData.dtoList[i].createdAt;
							var sDate = new Date(oDate);
							oData.dtoList[i].createdOnstring = sDate;
						}
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", oData.dtoList);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryWellDowntimeStatusList", oData.itemList);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryDowntimePageNo;
						var oTotalCount = oData.totalCount;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryFlareDowntimeStatusList", "");
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
					}

				}.bind(this),
				function (oError) {
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryDowntimeData", []);
					scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryFlareDowntimeStatusList", "");
					scope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				}.bind(this));
		},
		/** 
		 * Function for Pagination Implementation in Location History- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		updatePagination: function (scope, oEvent, dashBoardModel, oPageNo, oTotalCount) {
			if (oTotalCount > 0) {
				dashBoardModel.setProperty("/locationHistoryData/locHistoryPaginationVisibility", true);
				var oStartIndex = (oPageNo - 1) * 100 + 1;
				var oEndIndex = oStartIndex + 99;
				if (oEndIndex > oTotalCount) {
					oEndIndex = oTotalCount;
				}
				if (oStartIndex > 100) {
					dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", true);
				} else {
					dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryPreviousButtonEnable", false);
				}
				if (oEndIndex < oTotalCount) {
					dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", true);
				} else {
					dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryNextButtonEnable", false);
				}
				var sText = oStartIndex + "- " + oEndIndex + " of " + oTotalCount;
				dashBoardModel.setProperty("/locationHistoryPagination/PaginationText", sText);
			} else {
				dashBoardModel.setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
			}
		},
		/** 
		 * Function to get Work Order History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getWorkorderList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryWorkOrderPageNo;
			var oTop = (oPageNo - 1) * 100;
			var oRecord = 100;
			var sUrl = "/destination/ODATA_DEST/sap/opu/odata/sap/ZMUR_IOP_LOCATION_HISTORY_SRV";
			var oUrl = "WorkorderNumberSet";
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oLocationCode = "";
			var oDate = new Date();
			var sDay = oDate.getDate();
			var sMonth = Number(oDate.getMonth()) + 1;
			var sYear = oDate.getFullYear();
			var sDate = "" + sYear + "-" + sMonth + "-" + sDay + "T00:00:00";
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterWorkorder;
			if (oLocationSelected.length > 0) {
				for (var i = 0; i < oLocationSelected.length - 1; i++) {
					oLocationCode += oLocationSelected[i].location + ",";
				}
				oLocationCode += oLocationSelected[i].location;
				//this.oBusyDialog.open();
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", true);
				var oPayload = {
					"Skip": Number(oTop),
					"Top": Number(oRecord),
					"FunctionalLocation": oLocationCode,
					"OrderType": "",
					"Period": oMonth,
					"Date": sDate,
					"ORDERTOHEADERNAV": [],
					"ORDERTOSTATUSNAV": [],
					"ORDERTORETURNNAV": {
						"Status": "",
						"Message": ""
					}
				};
				var oDataModel = new sap.ui.model.odata.ODataModel(sUrl, true);
				oDataModel.create(oUrl, oPayload, null, function (oEventData) {
					//that.oBusyDialog.close();
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
					if (oEventData && oEventData.ORDERTOHEADERNAV) {
						dashBoardModel.setProperty("/locationHistoryData/locationHistoryTaskCount", oEventData.Count);
						dashBoardModel.setProperty("/locationHistoryData/locHistoryWorkorderData", oEventData.ORDERTOHEADERNAV.results);
						dashBoardModel.setProperty("/locationHistoryData/locHistoryWorkorderStatusData", oEventData.ORDERTOSTATUSNAV.results);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryWorkOrderPageNo;
						var oTotalCount = oEventData.Count;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						//MessageBox.error("Error in Retrieving Work Order Details");
						sap.m.MessageToast.show(oEventData.ORDERTORETURNNAV.Message);
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
						//that.oBusyDialog.close();
					}
					//that.oBusyDialog.close();
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
				}, function (oEventData) {
					//MessageBox.error("Error in Retrieving  Work Order Details");
					sap.m.MessageToast.show(oEventData.ORDERTORETURNNAV.Message);
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
					//that.oBusyDialog.close();
				});
			}
		},
		/** 
		 * Function to get Work Order Details of a particular Work Order List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getWorkorderDetails: function (scope, oEvent, dashBoardModel) {
			//this.oBusyDialog.open();
			var that = this;
			var oWorkorderNo = oEvent.getSource().getBindingContext("dashBoardModel").getObject().WorkorderNo;
			var sUrl = "/destination/ODATA_DEST/sap/opu/odata/sap/ZMUR_IOP_LOCATION_HISTORY_SRV";
			var oUrl = "/WorkorderHeaderSet(WorkorderNo='" + oWorkorderNo + "')?$expand=HEADERTOOPERATIONSNAV&$format=json";
			var oDataModel = new sap.ui.model.odata.ODataModel(sUrl, true);
			oDataModel.read(oUrl, null, null, true, function (oEventData) {
					if (oEventData) {
						var oWorkOrderDetailsData = oEventData.HEADERTOOPERATIONSNAV.results[0];
						dashBoardModel.setProperty("/locationHistoryData/workOrderDetailsData", [oWorkOrderDetailsData]);
						var oFragmentId = "idLocationHistoryDetailsFragment",
							oFragmentName = "com.sap.incture.Incture_IOP.fragment.LocationHistoryWorkorderDetails";
						if (!scope.oWorkorderDialog) {
							scope.oWorkorderDialog = scope._createFragment(oFragmentId, oFragmentName);
							scope.getView().addDependent(scope.oWorkorderDialog);
						}
						scope.oWorkorderDialog.open();
						//that.oBusyDialog.close();
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
					} else {
						MessageBox.error("Error in Retrieving Work Order Details");

					}
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
					//that.oBusyDialog.close();
				},
				function (oEventData) {
					MessageBox.error("Error in Retrieving Work Ordert Details");
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
					//that.oBusyDialog.close();
				});
		},
		/** 
		 * Function to Close dialogs releted to Location history module- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		onCloseDetails: function (scope, oEvent, dashBoardModel) {
			if (scope.oWorkorderDialog) {
				scope.oWorkorderDialog.close();
			}
			if (scope.oTaskDialog) {
				scope.oTaskDialog.close();
			}
			if (scope.oBypassLogDialog) {
				scope.oBypassLogDialog.close();
			}
		},
		/** 
		 * Function to download Attachment in Location history module- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		attachmentDownload: function (scope, oEvent, dashBoardModel) {
			var oAttachment = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var type = oAttachment.fileType;
			var base64 = oAttachment.compressedFile;
			var fileName = oAttachment.fileName;

			//to check if browser is IE
			if (window.navigator.msSaveOrOpenBlob) {
				var u8_Array = new Uint8Array(atob(base64).split("").map(function (c) {
					return c.charCodeAt(0);
				}));
				var blobData = new Blob([u8_Array], {
					type: type
				});
				window.navigator.msSaveOrOpenBlob(blobData, fileName);
			} else {
				var oUrl = oAttachment.documentUrl;
				if (oUrl) {
					//	window.open(oUrl);
					window.location.href = oUrl;
				} else {
					var link = document.createElement("a");
					var uri = "data:" + type + ";base64," + escape(base64); //data:image/png;base64,SmFp
					link.href = uri;
					link.style = "visibility:hidden";
					link.download = fileName;
					document.body.appendChild(link);
					link.click();
					document.body.removeChild(link);
				}
			}

		},
		/** 
		 * Function to get Energy Isolation History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getEnergyIsolationList: function (scope, oEvent, dashBoardModel) {
			var oPageNo = scope.getModel("dashBoardModel").getProperty("/locationHistoryPagination/EnergyIsolationPageNumber");
			var month = dashBoardModel.getProperty("/locationHistorySelectTabs/monthFilterEnergyIsolation");
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oLocationCode = "";
			if (oLocationSelected.length > 0) {
				for (var i = 0; i < oLocationSelected.length - 1; i++) {
					oLocationCode += oLocationSelected[i].location + ",";
				}
			}
			oLocationCode += oLocationSelected[i].location;
			var sUrl = "/taskmanagementRest/energyIsolation/formLocation?locationCode=" + oLocationCode + "&monthTime=" + month + "&weekTime=0" +
				"&page=" +
				oPageNo +
				"&page_size=100";
			if (oLocationSelected.length > 0) {
				scope.doAjax(sUrl, "GET", "", function (oData) {
						if (oData.responseMessage.status === "SUCCESS") {
							scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/energyIsolationData", oData.formList);
							scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryEnergyIsolationStatusData", oData.statusCountList);
							var pageNo = dashBoardModel.getData().locationHistoryPagination.EnergyIsolationPageNumber;
							var oTotalCount = oData.totalCount;
							this.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
						} else {
							scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/energyIsolationData", []);
							scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
						}
					}.bind(this),
					function (oError) {
						scope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					}.bind(this));
			} else {
				MessageToast.show("Select a location to fetch Energy Isolation History!");
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
				//scope.oBusyDialog.close();
			}
		},
		/** 
		 * Function to get Bypass Log History List- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getBypassLogList: function (scope, oEvent, dashBoardModel) {
			var that = this;
			var oPageNo = scope.getView().getModel("dashBoardModel").getData().locationHistoryPagination.LocHistoryBypassLogPageNo;
			var oPageSize = scope.getView().getModel("dashBoardModel").getData().locationHistoryData.pageSize;
			var oLocationSelected = scope.getView().getModel("dashBoardModel").getData().hierarchyDetails.currentSelectedObject;
			var oLocationCode = "";
			var oMonth = scope.getView().getModel("dashBoardModel").getData().locationHistorySelectTabs.monthFilterBypassLog;
			if (oLocationSelected.length > 0) {
				for (var i = 0; i < oLocationSelected.length - 1; i++) {
					oLocationCode += oLocationSelected[i].location + ",";
				}
				oLocationCode += oLocationSelected[i].location;
				/*oLocationCode="test";*/
				var sUrl = "/taskmanagementRest/bypassLog/getBypassLogList?locations=" + oLocationCode + "&timePeriod=" + oMonth + "&pageNo=" +
					oPageNo + "&pageSize=" + oPageSize + "&bypassLogStatus=ALL";
				scope.doAjax(sUrl, "GET", null, function (oData) {
					if (oData && oData.responseMessage.statusCode === "0") {
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/bypassLogTable", oData.ssdBypassListDto);
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryByPasslogStatusList", oData.statusCountList);
						var pageNo = dashBoardModel.getData().locationHistoryPagination.LocHistoryBypassLogPageNo;
						var oTotalCount = oData.totalCount;
						that.updatePagination(scope, oEvent, dashBoardModel, pageNo, oTotalCount);
					} else {
						scope.getView().getModel("dashBoardModel").setProperty("/locationHistoryData/bypassLogData", []);
						scope.getModel("dashBoardModel").setProperty("/locationHistoryData/locHistoryPaginationVisibility", false);
					}
				}.bind(scope), function (oError) {
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(scope));
			}

		},
		/** 
		 * Function to get Task Details- location History Module
		 * @param {Object} dashBoardModel - Model
		 */
		getBypassLogDetails: function (scope, oEvent, dashBoardModel) {
			//this.oBusyDialog.open();
			scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", true); //AN: #Notif
			dashBoardModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", false);
			var that = this;
			var sBypassId = oEvent.getSource().getBindingContext("dashBoardModel").getObject().ssdBypassId;
			var sUrl = "/taskmanagementRest/bypassLog/getBypassLogById?bypassId=" + sBypassId;
			var oFragmentId = "idLocationHistoryBypassLogDetailsFragment",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.LocationHistoryBypassLogDetailsForm";
			scope.doAjax(sUrl, "GET", null, function (oData) {
				//that.oBusyDialog.close();
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
				if (oData && oData.responseMessage.statusCode === "0") {
					scope.getModel("dashBoardModel").setProperty("/locationHistoryData/bypassLogDetailsData", oData);
					if (!scope.oBypassLogDialog) {
						scope.oBypassLogDialog = scope._createFragment(oFragmentId, oFragmentName);
						scope.getView().addDependent(scope.oBypassLogDialog);
					}
					scope.oBypassLogDialog.open();
				} else {
					MessageBox.error(oData.responseMessage.message);
				}
			}.bind(scope), function (oError) {
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				scope.getModel("dashBoardModel").setProperty("/locationHistoryData/busyIndicator", false);
				//that.oBusyDialog.close();
			}.bind(scope));

		},
		PTWFilterChange: function (scope, oEvent, dashBoardModel) {
			var PTWFilterKey = oEvent.getSource().getSelectedKey();
			var PTWMonthFilterKey = dashBoardModel.getProperty("/locationHistorySelectTabs/monthFilterPTW");
			scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
			scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
			scope.getPermittoWorkList(PTWFilterKey, PTWMonthFilterKey);
		},
		/*SK: to open the JSA on clik of JSA table row*/
		PTWJSATableItemPress: function (scope, oEvent, Model) {
			var oItem = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var PermitNumber = oItem.PERMITNUMBER;
			var sUrl = "/ptwRest/JSA_Details_By_PermitNumber.xsjs?permitNumber=" + PermitNumber;
			scope.doAjax(sUrl, "GET", null, function (oJsaDetails) {
				if (oJsaDetails) {
					scope.getModel("dashBoardModel").setProperty("/ptwJsaDetails", oJsaDetails);
					this.SetDataForJsa(scope, oJsaDetails);
				}
			}.bind(this), function (oError) {
				scope.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(scope));

			var oFragmentId = "idJSA",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.PTWJSAForm";
			if (!scope.oJSAForm) {
				scope.oJSAForm = scope._createFragment(oFragmentId, oFragmentName);
				scope.getView().addDependent(scope.oJSAForm);
			}
			scope.oJSAForm.open();
		},
		/*SK: To set the data for PTW JSA form Hazard List */
		SetDataForJsa: function (scope, oJsaDetails) {
			var JsaHazardListData = {
				dataList: [{
						header: "PRESURIZED EQUIPMENT",
						headerSelected: oJsaDetails.TOJSAHAZARDPRESS.presurizedEquipment,
						icon: "/PresurizedEquipment.png",
						aList: [{
							Text: "Perform isolation – LOTO,blinding or defeat",
							selected: oJsaDetails.TOJSAHAZARDPRESS.performIsolation
						}, {
							Text: "Depressurize, drain, purge,and vent ",
							selected: oJsaDetails.TOJSAHAZARDPRESS.depressurizeDrain
						}, {
							Text: "Relieve trapped pressure",
							selected: oJsaDetails.TOJSAHAZARDPRESS.relieveTrappedPressure
						}, {
							Text: "Do not work in the line of fire",
							selected: oJsaDetails.TOJSAHAZARDPRESS.doNotWorkInLineOfFire
						}, {
							Text: "Anticipate residual pressure or fluids",
							selected: oJsaDetails.TOJSAHAZARDPRESS.anticipateResidual
						}, {
							Text: "Secure all hoses",
							selected: oJsaDetails.TOJSAHAZARDPRESS.secureAllHoses
						}]
					}, {
						header: "POOR LIGHTING / VISIBILITY",
						headerSelected: oJsaDetails.TOJSAHAZARDVISIBLE.poorLighting,
						icon: "/PoorLighting.png",
						aList: [{
							Text: "Provide alternate lighting",
							selected: oJsaDetails.TOJSAHAZARDVISIBLE.provideAlternateLighting
						}, {
							Text: "Wait or defer until visibility improves",
							selected: oJsaDetails.TOJSAHAZARDVISIBLE.waitUntilVisibilityImprove
						}, {
							Text: "Defer until visibility improves",
							selected: oJsaDetails.TOJSAHAZARDVISIBLE.deferUntilVisibilityImprove
						}, {
							Text: "Know distance from poles and other equipment",
							selected: oJsaDetails.TOJSAHAZARDVISIBLE.knowDistanceFromPoles
						}]
					},

					{
						header: "PERSONNEL",
						headerSelected: oJsaDetails.TOJSAHAZARDPERSON.personnel,
						icon: "/Personnel.png",
						aList: [{
							Text: "Perform induction or training for new workers",
							selected: oJsaDetails.TOJSAHAZARDPERSON.performInduction
						}, {
							Text: "Mentor / coach / supervise",
							selected: oJsaDetails.TOJSAHAZARDPERSON.mentorCoachSupervise
						}, {
							Text: "Verify competencies, skills and experience",
							selected: oJsaDetails.TOJSAHAZARDPERSON.verifyCompetencies
						}, {
							Text: "Address applicable limitations(fatigue, exhaustion)",
							selected: oJsaDetails.TOJSAHAZARDPERSON.addressLimitations
						}, {
							Text: "Manage language barriers",
							selected: oJsaDetails.TOJSAHAZARDPERSON.manageLanguageBarriers
						}, {
							Text: "Seat belts while driving",
							selected: oJsaDetails.TOJSAHAZARDPERSON.wearSeatBelts
						}]
					}, {
						header: "CONFINED SPACE ENTRY",
						headerSelected: oJsaDetails.TOJSAHAZARDCSE.confinedSpaceEntry,
						icon: "/ConfinedSpaceEntry.png",
						aList: [{
							Text: "Discuss confined space entry safe work practice",
							selected: oJsaDetails.TOJSAHAZARDCSE.discussWorkPractice
						}, {
							Text: "Conduct atmospheric testing",
							selected: oJsaDetails.TOJSAHAZARDCSE.conductAtmosphericTesting
						}, {
							Text: "Monitor access or entry",
							selected: oJsaDetails.TOJSAHAZARDCSE.monitorAccess
						}, {
							Text: "Protect surfaces from inadvertent contact",
							selected: oJsaDetails.TOJSAHAZARDCSE.protectSurfaces
						}, {
							Text: "Prohibit mobile engines near confined spaces",
							selected: oJsaDetails.TOJSAHAZARDCSE.prohibitMobileEngine
						}, {
							Text: "Provide Observer",
							selected: oJsaDetails.TOJSAHAZARDCSE.provideObserver
						}, {
							Text: "Develop rescue plan",
							selected: oJsaDetails.TOJSAHAZARDCSE.developRescuePlan
						}]
					}, {
						header: "SIMULTANEOUS OPERATIONS ",
						headerSelected: oJsaDetails.TOJSAHAZARDSIMULTAN.simultaneousOperations,
						icon: "/SimulataneousOperation.png",
						aList: [{
							Text: "Follow SIMOPS matrix",
							selected: oJsaDetails.TOJSAHAZARDSIMULTAN.followSimopsMatrix
						}, {
							Text: "MOC required for deviation from SIMOPS restrictions",
							selected: oJsaDetails.TOJSAHAZARDSIMULTAN.mocRequiredFor
						}, {
							Text: "Interface between groups",
							selected: oJsaDetails.TOJSAHAZARDSIMULTAN.interfaceBetweenGroups
						}, {
							Text: "Use barriers and signs to segregate activities",
							selected: oJsaDetails.TOJSAHAZARDSIMULTAN.useBarriersAnd
						}, {
							Text: "Have permit signed by leader of affected groups",
							selected: oJsaDetails.TOJSAHAZARDSIMULTAN.havePermitSigned
						}]
					}, {
						header: "IGNITION SOURCES ",
						headerSelected: oJsaDetails.TOJSAHAZARDIGNITION.ignitionSources,
						icon: "/IgnitionSource.png",
						aList: [{
							Text: "Remove, isolate, or contain combustible materials",
							selected: oJsaDetails.TOJSAHAZARDIGNITION.removeCombustibleMaterials
						}, {
							Text: "Provide firefighting equipment and fire watch",
							selected: oJsaDetails.TOJSAHAZARDIGNITION.provideFireWatch
						}, {
							Text: "Implement abrasive blasting controls",
							selected: oJsaDetails.TOJSAHAZARDIGNITION.implementAbrasiveBlastingControls
						}, {
							Text: "Conduct continuous gas testing ",
							selected: oJsaDetails.TOJSAHAZARDIGNITION.conductContinuousGasTesting
						}, {
							Text: "Bond or earth for static electricity for cathodic protection",
							selected: oJsaDetails.TOJSAHAZARDIGNITION.earthForStaticElectricity
						}]
					}, {
						header: " HAZARDOUS SUBSTANCES",
						headerSelected: oJsaDetails.TOJSAHAZARDSUBS.hazardousSubstances,
						icon: "/HazardousSubstances.png",
						aList: [{
							Text: "Drain or purge equipment",
							selected: oJsaDetails.TOJSAHAZARDSUBS.drainEquipment
						}, {
							Text: "Follow SDS controls",
							selected: oJsaDetails.TOJSAHAZARDSUBS.followSdsControls
						}, {
							Text: "Implement health hazard controls (Lead, Asbestos, Benzene, H2S, Iron Sulfide, Sulfur Dioxide, NORM)",
							selected: oJsaDetails.TOJSAHAZARDSUBS.implementHealthHazardControls
						}, {
							Text: "Test or analyze material",
							selected: oJsaDetails.TOJSAHAZARDSUBS.testMaterial
						}]
					}, {
						header: "POTENTIAL WEATHER SPILLS ",
						headerSelected: oJsaDetails.TOJSAHAZARDSPILL.potentialSpills,
						icon: "/PotentialWeatherSpills.png",
						aList: [{
							Text: "Drain equipment",
							selected: oJsaDetails.TOJSAHAZARDSPILL.drainEquipment
						}, {
							Text: "Hoses, connections in good condition",
							selected: oJsaDetails.TOJSAHAZARDSPILL.connectionsInGoodCondition
						}, {
							Text: "Spill containment equipment",
							selected: oJsaDetails.TOJSAHAZARDSPILL.spillContainmentEquipment
						}, {
							Text: "Have spill cleanup materials and equipment on hand",
							selected: oJsaDetails.TOJSAHAZARDSPILL.haveSpillCleanupMaterials
						}, {
							Text: " Restrain and isolate hoses when not in use",
							selected: oJsaDetails.TOJSAHAZARDSPILL.restrainHosesWhenNotInUse
						}]
					}, {
						header: "WEATHER",
						headerSelected: oJsaDetails.TOJSAHAZARDWEATHER.weather,
						icon: "/Weather.png",
						aList: [{
							Text: "Implement controls for slippery surfaces",
							selected: oJsaDetails.TOJSAHAZARDWEATHER.controlsForSlipperySurface
						}, {
							Text: "Heat – hydration, breaks",
							selected: oJsaDetails.TOJSAHAZARDWEATHER.heatBreak
						}, {
							Text: "Cold – PPE, heaters",
							selected: oJsaDetails.TOJSAHAZARDWEATHER.coldHeaters
						}, {
							Text: "Lightning – tool selection, defer work",
							selected: oJsaDetails.TOJSAHAZARDWEATHER.lightning
						}]
					}, {
						header: "HIGH NOISE",
						headerSelected: oJsaDetails.TOJSAHAZARDNOISE.highNoise,
						icon: "/HighNoise.png",
						aList: [{
							Text: "Wear correct hearing PPE",
							selected: oJsaDetails.TOJSAHAZARDNOISE.wearCorrectHearing
						}, {
							Text: "Manage exposure times",
							selected: oJsaDetails.TOJSAHAZARDNOISE.manageExposureTimes
						}, {
							Text: "Shut down equipment",
							selected: oJsaDetails.TOJSAHAZARDNOISE.shutDownEquipment
						}, {
							Text: "Use “quiet” tools",
							selected: oJsaDetails.TOJSAHAZARDNOISE.useQuietTools
						}, {
							Text: "Sound barriers or curtains",
							selected: oJsaDetails.TOJSAHAZARDNOISE.soundBarriers
						}, {
							Text: "Provide or use suitable communication techniques",
							selected: oJsaDetails.TOJSAHAZARDNOISE.provideSuitableComms
						}]
					}, {
						header: " DROPPED OBJECTS",
						headerSelected: oJsaDetails.TOJSAHAZARDDROPPED.droppedObjects,
						icon: "/DroppedObjects.png",
						aList: [{
							Text: "Use signs and barriers to restrict entry or access underwork at elevation",
							selected: oJsaDetails.TOJSAHAZARDDROPPED.markRestrictEntry
						}, {
							Text: "Use lifting equipment to raise tools to or from the work platform",
							selected: oJsaDetails.TOJSAHAZARDDROPPED.useLiftingEquipmentToRaise
						}, {
							Text: "Secure tools (tie-off)",
							selected: oJsaDetails.TOJSAHAZARDDROPPED.secureTools
						}]
					}, {
						header: "LIFTING EQUIPMENT",
						headerSelected: oJsaDetails.TOJSAHAZARDLIFT.liftingEquipment,
						icon: "/LiftingEquipment.png",
						aList: [{
							Text: "Confirm lifting equipment condition and certification",
							selected: oJsaDetails.TOJSAHAZARDLIFT.confirmEquipmentCondition
						}, {
							Text: "Obtain approval for lifts over processing equipment",
							selected: oJsaDetails.TOJSAHAZARDLIFT.obtainApprovalForLifts
						}, {
							Text: "Have a documented and approved lift plan",
							selected: oJsaDetails.TOJSAHAZARDLIFT.haveDocumentedLiftPlan
						}]
					}, {
						header: "WORK AT HEIGHTS ",
						headerSelected: oJsaDetails.TOJSAHAZARDHEIGHT.workAtHeights,
						icon: "/WortAtHeights.png",
						aList: [{
							Text: "Discuss working at heights safe work practice",
							selected: oJsaDetails.TOJSAHAZARDHEIGHT.discussWorkingPractice
						}, {
							Text: "Verify fall restraint and arrest equipment certification",
							selected: oJsaDetails.TOJSAHAZARDHEIGHT.verifyFallRestraint
						}, {
							Text: "Full body harness is required",
							selected: oJsaDetails.TOJSAHAZARDHEIGHT.useFullBodyHarness
						}, {
							Text: "Locking type snaphooks must be used",
							selected: oJsaDetails.TOJSAHAZARDHEIGHT.useLockTypeSnaphoooks
						}]
					}, {
						header: "PORTABLE ELECTRICAL EQUIPMENT",
						headerSelected: oJsaDetails.TOJSAHAZARDELECTRICAL.portableElectricalEquipment,
						icon: "/PortableElectricalEquipment.png",
						aList: [{
							Text: "Inspect equipment and tools for condition and test date currency",
							selected: oJsaDetails.TOJSAHAZARDELECTRICAL.inspectToolsForCondition
						}, {
							Text: "Implement continuous gas testing",
							selected: oJsaDetails.TOJSAHAZARDELECTRICAL.implementGasTesting
						}, {
							Text: "Protect electrical leads from impact or damage",
							selected: oJsaDetails.TOJSAHAZARDELECTRICAL.protectElectricalLeads
						}, {
							Text: "Identify equip. classification",
							selected: oJsaDetails.TOJSAHAZARDELECTRICAL.identifyEquipClassification
						}]
					}, {
						header: "MOVING EQUIPMENT",
						headerSelected: oJsaDetails.TOJSAHAZARDMOVING.movingEquipment,
						icon: "/MovingEquipment.png",
						aList: [{
							Text: "Confirm machinery guard integrity",
							selected: oJsaDetails.TOJSAHAZARDMOVING.confirmMachineryIntegrity
						}, {
							Text: "Provide protective barriers",
							selected: oJsaDetails.TOJSAHAZARDMOVING.provideProtectiveBarriers
						}, {
							Text: "Observer to monitor proximity of people and equipment",
							selected: oJsaDetails.TOJSAHAZARDMOVING.observerToMonitorProximityPeopleAndEquipment
						}, {
							Text: "Shut down or lock out equipment",
							selected: oJsaDetails.TOJSAHAZARDMOVING.lockOutEquipment
						}, {
							Text: "Do not work in the line of fire",
							selected: oJsaDetails.TOJSAHAZARDMOVING.doNotWorkInLineOfFire
						}]
					}, {
						header: "MANUAL HANDLING ",
						headerSelected: oJsaDetails.TOJSAHAZARDMANUAL.manualHandling,
						icon: "/ManualHandling.png",
						aList: [{
							Text: "Assess manual handling task",
							selected: oJsaDetails.TOJSAHAZARDMANUAL.assessManualTask
						}, {
							Text: "Limit load size",
							selected: oJsaDetails.TOJSAHAZARDMANUAL.limitLoadSize
						}, {
							Text: "Proper lifting technique",
							selected: oJsaDetails.TOJSAHAZARDMANUAL.properLiftingTechnique
						}, {
							Text: "Confirm stability of load and work platform",
							selected: oJsaDetails.TOJSAHAZARDMANUAL.confirmStabilityOfLoad
						}, {
							Text: "Get assistance or mechanical aid to avoid pinch points",
							selected: oJsaDetails.TOJSAHAZARDMANUAL.getAssistanceOrAid
						}]
					}, {
						header: "EQUIPMENT AND TOOLS ",
						headerSelected: oJsaDetails.TOJSAHAZARDTOOLS.EquipmentAndTools,
						icon: "/EquipmentAndTools.png",
						aList: [{
							Text: "Inspect equipment and tools",
							selected: oJsaDetails.TOJSAHAZARDTOOLS.inspectEquipmentTool
						}, {
							Text: "Brass tools necessary",
							selected: oJsaDetails.TOJSAHAZARDTOOLS.brassToolsNecessary
						}, {
							Text: "Use protective guards",
							selected: oJsaDetails.TOJSAHAZARDTOOLS.useProtectiveGuards
						}, {
							Text: "Use correct tools and equipment for task",
							selected: oJsaDetails.TOJSAHAZARDTOOLS.useCorrectTools
						}, {
							Text: "Protect or remove sharp edges",
							selected: oJsaDetails.TOJSAHAZARDTOOLS.checkForSharpEdges
						}, {
							Text: "Apply hand safety principles",
							selected: oJsaDetails.TOJSAHAZARDTOOLS.applyHandSafetyPrinciple
						}]
					},

					{
						header: " SLIPS, TRIPS, AND FALLS",
						headerSelected: oJsaDetails.TOJSAHAZARDFALLS.slipsTripsAndFalls,
						icon: "/SlipsTripsFalls.png",
						aList: [{
							Text: "Identify and shield uneven surface or projections",
							selected: oJsaDetails.TOJSAHAZARDFALLS.identifyProjections
						}, {
							Text: "Flag hazards as necessary",
							selected: oJsaDetails.TOJSAHAZARDFALLS.flagHazards
						}, {
							Text: "Secure or cover cables, cords, and tubing",
							selected: oJsaDetails.TOJSAHAZARDFALLS.secureCables
						}, {
							Text: "Clean up liquids",
							selected: oJsaDetails.TOJSAHAZARDFALLS.cleanUpLiquids
						}, {
							Text: "Barricade or rope off openings and holes",
							selected: oJsaDetails.TOJSAHAZARDFALLS.barricadeHoles
						}]
					}, {
						header: " HIGH ENERGY / HIGH VOLTAGE",
						headerSelected: oJsaDetails.TOJSAHAZARDVOLTAGE.highVoltage,
						icon: "/HighEnergyHighVoltage.png",
						aList: [{
							Text: "Restrict access to authorized personnel only",
							selected: oJsaDetails.TOJSAHAZARDVOLTAGE.restrictAccess
						}, {
							Text: " Discharge equipment and make electrically dead",
							selected: oJsaDetails.TOJSAHAZARDVOLTAGE.dischargeEquipment
						}, {
							Text: "Observe safe work distances for live cables",
							selected: oJsaDetails.TOJSAHAZARDVOLTAGE.observeSafeWorkDistance
						}, {
							Text: "Use flash burn PPE",
							selected: oJsaDetails.TOJSAHAZARDVOLTAGE.useFlashBurn
						}, {
							Text: "Use insulated gloves, tools, and mats, GFCI’s.",
							selected: oJsaDetails.TOJSAHAZARDVOLTAGE.useInsulatedGloves
						}]
					},

					{
						header: "EXCAVATIONS",
						headerSelected: oJsaDetails.TOJSAHAZARDEXCAVATION.excavations,
						icon: "/Excavations.png",
						aList: [{
							Text: "Have an excavation plan or safe work practice",
							selected: oJsaDetails.TOJSAHAZARDEXCAVATION.haveExcavationPlan
						}, {
							Text: "Locate underground pipes or cables by hand digging",
							selected: oJsaDetails.TOJSAHAZARDEXCAVATION.locatePipesByHandDigging
						}, {
							Text: "De-energize underground services",
							selected: oJsaDetails.TOJSAHAZARDEXCAVATION.deEnergizeUnderground
						}, {
							Text: "Implement confined space entry controls",
							selected: oJsaDetails.TOJSAHAZARDEXCAVATION.cseControls
						}]
					}, {
						header: "MOBILE EQUIPMENT",
						headerSelected: oJsaDetails.TOJSAHAZARDMOBILE.mobileEquipment,
						icon: "/MobileEquipment.png",
						aList: [{
							Text: "Assess equipment condition",
							selected: oJsaDetails.TOJSAHAZARDMOBILE.assessEquipmentCondition
						}, {
							Text: "Implement controls on users or access",
							selected: oJsaDetails.TOJSAHAZARDMOBILE.controlAccess
						}, {
							Text: "Limit and monitor proximity to live equipment on users or access",
							selected: oJsaDetails.TOJSAHAZARDMOBILE.monitorProximity
						}, {
							Text: "Manage overhead hazards",
							selected: oJsaDetails.TOJSAHAZARDMOBILE.manageOverheadHazards
						}, {
							Text: "Adhere to road / site rules",
							selected: oJsaDetails.TOJSAHAZARDMOBILE.adhereToRules
						}]
					}
				]
			};
			scope.getModel("dashBoardModel").setProperty("/ptwJsaHazards", JsaHazardListData);
		},
		/*SK: TO get the details of PTW Forms*/
		PTWPermitTableItemPress: function (scope, oEvent, Model) {
			var oSelModule = scope.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
			var PTWFilterKey;
			if (oSelModule === "locHistory") {
				PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
			} else {
				PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/ptwSelectedKey");
			}
			//var PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
			var oItem = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var PermitNumber = oItem.PERMITNUMBER;
			switch (PTWFilterKey) {
			case "Active CW":
				var sUrl = "/ptwRest/GetPermitDetails.xsjs?permitNumber=" + PermitNumber + "&permitType=COLD";
				scope.doAjax(sUrl, "GET", null, function (oData) {
					scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail", oData);
					var aTableData = scope.getModel("dashBoardModel").getProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLE");
					if (aTableData !== undefined && aTableData.length > 0) {
						if (aTableData.length % 2 !== 0) {
							aTableData.push({});
						}
						var halflength = aTableData.length / 2;
						scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLEOne", aTableData.slice(0, halflength));
						scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLETwo", aTableData.slice(halflength,
							aTableData.length));
					}
				}.bind(scope), function (oError) {
					scope.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(scope));

				var oFragmentIdCW = "idCWP",
					oFragmentNameCW = "com.sap.incture.Incture_IOP.fragment.PTWCWPForm";
				if (!scope.oCWPForm) {
					scope.oCWPForm = scope._createFragment(oFragmentIdCW, oFragmentNameCW);
					scope.getView().addDependent(scope.oCWPForm);
				}
				scope.oCWPForm.open();
				break;
			case "Active HW":
				var sUrl = "/ptwRest/GetPermitDetails.xsjs?permitNumber=" + PermitNumber + "&permitType=HOT";
				scope.doAjax(sUrl, "GET", null, function (oData) {
					scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail", oData);
					var aTableData = scope.getModel("dashBoardModel").getProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLE");
					if (aTableData !== undefined && aTableData.length > 0) {
						if (aTableData.length % 2 !== 0) {
							aTableData.push({});
						}
						var halflength = aTableData.length / 2;
						scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLEOne", aTableData.slice(0, halflength));
						scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLETwo", aTableData.slice(halflength,
							aTableData.length));
					}
				}.bind(scope), function (oError) {
					scope.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(scope));

				var oFragmentIdHWP = "idHWP",
					oFragmentNameHWP = "com.sap.incture.Incture_IOP.fragment.PTWHWPForm";
				if (!scope.oHWPForm) {
					scope.oHWPForm = scope._createFragment(oFragmentIdHWP, oFragmentNameHWP);
					scope.getView().addDependent(scope.oHWPForm);
				}
				scope.oHWPForm.open();
				break;
			case "Active CSP":
				var sUrl = "/ptwRest/GetPermitDetails.xsjs?permitNumber=" + PermitNumber + "&permitType=CSE";
				scope.doAjax(sUrl, "GET", null, function (oData) {
					scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail", oData);
					var aTableData = scope.getModel("dashBoardModel").getProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLE");
					if (aTableData !== undefined && aTableData.length > 0) {
						if (aTableData.length % 2 !== 0) {
							aTableData.push({});
						}
						var halflength = aTableData.length / 2;
						scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLEOne", aTableData.slice(0, halflength));
						scope.getModel("dashBoardModel").setProperty("/ptwPermitDetail/PTWDetails/TOPTWPEOPLETwo", aTableData.slice(halflength,
							aTableData.length));
					}
				}.bind(scope), function (oError) {
					scope.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
					var sErrorMessage;
					sErrorMessage = oError.getParameter("statusText");
					scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
				}.bind(scope));

				var oFragmentIdCSP = "idCSP",
					oFragmentNameCSP = "com.sap.incture.Incture_IOP.fragment.PTWCSPForm";
				if (!scope.oCSPForm) {
					scope.oCSPForm = scope._createFragment(oFragmentIdCSP, oFragmentNameCSP);
					scope.getView().addDependent(scope.oCSPForm);
				}
				scope.oCSPForm.open();
				break;
			}

		},
		previousPagePress: function (scope, oEvent, dashBoardModel) {
			var oIconTabSelect = dashBoardModel.getData().locationHistorySelectTabs.iconFilterTab;
			var oPageSelect;
			switch (oIconTabSelect) {
			case "Task":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryTaskPageNo);
				if (oPageSelect > 1) {
					oPageSelect = oPageSelect - 1;
				}
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryTaskPageNo", oPageSelect);
				this.getTaskList(scope, oEvent, dashBoardModel);
				break;
			case "Downtime":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryDowntimePageNo);
				if (oPageSelect > 1) {
					oPageSelect = oPageSelect - 1;
				}
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryDowntimePageNo", oPageSelect);
				this.getDowntimeList(scope, oEvent, dashBoardModel);
				break;
			case "Workorder":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryWorkOrderPageNo);
				if (oPageSelect > 1) {
					oPageSelect = oPageSelect - 1;
				}
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryWorkOrderPageNo", oPageSelect);
				this.getWorkorderList(scope, oEvent, dashBoardModel);
				break;
			case "Permit To Work":
				var PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
				var PTWMonthFilterKey = dashBoardModel.getProperty("/locationHistorySelectTabs/monthFilterPTW");

				var pageNumber = scope.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwPageNumber");
				var start = parseInt(pageNumber) - 1;
				scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", start);
				scope.getPermittoWorkList(PTWFilterKey, PTWMonthFilterKey);
				break;
			case "Bypass Log":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryBypassLogPageNo);
				if (oPageSelect > 1) {
					oPageSelect = oPageSelect - 1;
				}
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryBypassLogPageNo", oPageSelect);
				break;
			case "Energy Isolation":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryEnergyIsolationPageNo);
				if (oPageSelect > 1) {
					oPageSelect = oPageSelect - 1;
				}
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryEnergyIsolationPageNo", oPageSelect);
				break;
			}
		},
		nextPagePress: function (scope, oEvent, dashBoardModel) {
			var oIconTabSelect = dashBoardModel.getData().locationHistorySelectTabs.iconFilterTab;
			var oPageSelect;
			switch (oIconTabSelect) {
			case "Task":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryTaskPageNo) + 1;
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryTaskPageNo", oPageSelect);
				this.getTaskList(scope, oEvent, dashBoardModel);
				break;
			case "Downtime":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryDowntimePageNo) + 1;
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryDowntimePageNo", oPageSelect);
				this.getDowntimeList(scope, oEvent, dashBoardModel);
				break;
			case "Workorder":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryWorkOrderPageNo);
				oPageSelect = oPageSelect + 1;
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryWorkOrderPageNo", oPageSelect);
				this.getWorkorderList(scope, oEvent, dashBoardModel);
				break;
			case "Permit To Work":
				var PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
				var PTWMonthFilterKey = dashBoardModel.getProperty("/locationHistorySelectTabs/monthFilterPTW");

				var pageNumber = scope.getModel("dashBoardModel").getProperty("/locationHistoryPagination/ptwPageNumber");
				var start = parseInt(pageNumber) + 1;
				scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", start);
				scope.getPermittoWorkList(PTWFilterKey, PTWMonthFilterKey);
				break;
			case "Bypass Log":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryBypassLogPageNo) + 1;
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryBypassLogPageNo", oPageSelect);
				break;
			case "Energy Isolation":
				oPageSelect = Number(dashBoardModel.getData().locationHistoryPagination.LocHistoryEnergyIsolationPageNo) + 1;
				dashBoardModel.setProperty("/locationHistoryPagination/LocHistoryEnergyIsolationPageNo", oPageSelect);
				break;
			}
		},
		sortingOnDate: function (scope, oEvent, dashBoardModel) {
			var oIconTabSelect = dashBoardModel.getData().locationHistorySelectTabs.iconFilterTab;
			var oTable;
			var oColumnName;
			switch (oIconTabSelect) {
			case "Task":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryTaskTable");
				oColumnName = "createdAt";
				break;
			case "Downtime":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryDowntimeTable");
				oColumnName = "created_At";
				break;
			case "Workorder":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryWorkorderTable");
				oColumnName = "CreatedOn";
				break;
			case "Permit To Work":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "idPtwTable");
				oColumnName = "CREATEDDATE";
				var filter = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
				if (filter === "Active CW" || filter === "Active HW" || filter === "Active CSP") {
					oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "idPtwPermitTable");
					oColumnName = "PLANNEDDATETIME";
				}
				break;
			case "Bypass Log":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryBypassLogTable");
				oColumnName = "bypassStartTime";
				break;
			case "Energy Isolation":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryEnergyIsolationTable");
				oColumnName = "createdAt";
				break;
			}
			var aSorters = [];
			var isDesc = dashBoardModel.getProperty("/locationHistoryPagination/bDescending");
			if (isDesc) {
				dashBoardModel.setProperty("/locationHistoryPagination/bDescending", false);
			} else {
				dashBoardModel.setProperty("/locationHistoryPagination/bDescending", true);
			}
			aSorters.push(new sap.ui.model.Sorter(oColumnName, isDesc));
			oTable.getBinding("items").sort(aSorters);
		},

		PTWDropDownFilterChange: function (scope, oEvent, dashBoardModel) {
			var PTWFilterKey = oEvent.getSource().getSelectedKey();
			var PTWMonthFilterKey = dashBoardModel.getProperty("/locationHistorySelectTabs/monthFilterPTW");
			scope.getPermittoWorkList(PTWFilterKey, PTWMonthFilterKey);
		},
		PTWMonthFilterChange: function (scope, oEvent, Model) {
			var selectedPtMonthWFilter = oEvent.getSource().getSelectedKey();
			var PTWFilterKey = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
			if (selectedPtMonthWFilter === "oneMonth") {
				Model.setProperty("/locationHistorySelectTabs/monthFilterPTW", "one");
				scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
				scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
				scope.getPermittoWorkList(PTWFilterKey, "one");
			} else {
				Model.setProperty("/locationHistorySelectTabs/monthFilterPTW", "three");
				scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwStartCount", "1");
				scope.getModel("dashBoardModel").setProperty("/locationHistoryPagination/ptwPageNumber", "1");
				scope.getPermittoWorkList(PTWFilterKey, "three");
			}
		},
		EnergyIsolationMonthFilterChange: function (scope, oEvent, dashBoardModel) {
			this.getEnergyIsolationList(scope, oEvent, dashBoardModel);
		},
		//#RV: Search in Location History
		onSearch: function (scope, oEvent, oModel) {
			var value = oEvent.getSource().getValue();
			var oIconTabSelect = oModel.getData().locationHistorySelectTabs.iconFilterTab;
			var oTable;
			var filterParams;
			switch (oIconTabSelect) {
			case "Task":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryTaskTable");
				filterParams = ["location", "origin", "taskOwner"];
				break;
			case "Downtime":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryDowntimeTable");
				filterParams = ["well", "childText"];
				break;
			case "Workorder":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryWorkorderTable");
				filterParams = ["FunctionalLocation", "WorkorderNo"];
				break;
			case "Bypass Log":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryBypassLogTable");
				filterParams = ["location", "ssdBypassNum"];
				break;
			case "Energy Isolation":
				oTable = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "locHistoryEnergyIsolationTable");
				filterParams = ["locationName", "id"];
				break;
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
			oTable.getBinding("items").filter(aFilters);
		},
		/*SK : PTW table search */
		PtwTableSearch: function (scope, oEvent, oModel) {
			var value = oEvent.getSource().getValue();
			var JsaorPermit = scope.getModel("dashBoardModel").getProperty("/locationHistorySelectTabs/LocHistoryPTWFilterKey");
			if (JsaorPermit === "Active JSA") {
				var oPtwList = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "idPtwTable");
				var filterParams = ["PERMITNUMBERString", "CREATEDBY", "TASKDESCRIPTION", "FACILTYORSITE"];
			} else {
				oPtwList = sap.ui.core.Fragment.byId(scope.createId("locHistoryFragment"), "idPtwPermitTable");
				filterParams = ["CREATEDBY", "LOCATION", "PERMITNUMBERString"];
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
			oPtwList.getBinding("items").filter(aFilters);
		},
		EnergyIsolationItemClick: function (scope, oEvent, oModel) {
			oModel.setProperty("/locationHistoryData/byPassLogPersonRespEditable", false); //Anand: #Handover
			var oObject = oEvent.getSource().getBindingContext("dashBoardModel").getObject();
			var formId = oObject.formId;
			var sUrl = "/taskmanagementRest/energyIsolation/form?formId=" + formId;
			scope.doAjax(sUrl, "GET", null, function (oData) {
				scope.getModel("dashBoardModel").setProperty("/ptwEnergyIsolationDetail", oData);
			}.bind(scope), function (oError) {
				scope.getModel("dashBoardModel").setProperty("/busyIndicators/ptwTableBusy", false);
				var sErrorMessage;
				sErrorMessage = oError.getParameter("statusText");
				scope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
			}.bind(scope));

			var oFragmentIdEI = "idEngeryIsolationForm",
				oFragmentNameEI = "com.sap.incture.Incture_IOP.fragment.EnergyIsolationForm";
			if (!scope.oEnergyIsolationForm) {
				scope.oEnergyIsolationForm = scope._createFragment(oFragmentIdEI, oFragmentNameEI);
				scope.getView().addDependent(scope.oEnergyIsolationForm);
			}
			scope.oEnergyIsolationForm.open();
		},
		onPrintPage: function (scope, oEvent, oModel) {
			var oForm = oModel.getData().locationHistorySelectTabs.selectedForm;
			var oTarget;
			if (oForm === "ColdWorkPermit") {
				oTarget = scope.oCWPForm;
			} else if (oForm === "HotWorkPermit") {
				oTarget = scope.oHWPForm;
			} else if (oForm === "JSAPermit") {
				oTarget = scope.oJSAForm;
			} else if (oForm === "CSPPermit") {
				oTarget = scope.oCSPForm;
			} else if (oForm === "BypassLogForm") {
				oTarget = scope.oBypassLogDialog;
				var oTarget2 = oTarget.getContent()[2].getAggregation('items')[0];
				var oTarget1 = oTarget.getContent()[1];
			} else if (oForm === "EnergyIsolationForm") {
				oTarget = scope.oEnergyIsolationForm;
				oTarget = oTarget.getContent()[1].getAggregation('items')[0];
			} else {
				return;
			}
			/*AN: Start of #ThemeBug*/
			var isDarkTheme = false;
			if (sap.ui.getCore().getConfiguration().getTheme().includes("dark")) {
				isDarkTheme = true;
			}
			/*AN: End of #ThemeBug*/

			if (oTarget) {
				oTarget.rerender();
				if (oForm === "BypassLogForm") {
					var $domTarget1 = oTarget1.getDomRef(),
						$domTarget2 = oTarget2.getDomRef(),
						sTargetContent = $domTarget1.innerHTML + $domTarget2.innerHTML,
						sOriginalContent = document.body.innerHTML;
				} else {
					var $domTarget = oTarget.getDomRef(),
						sTargetContent = $domTarget.innerHTML,
						sOriginalContent = document.body.innerHTML;
				}
				/*	document.body.innerHTML = sTargetContent;
					window.print();
					document.body.innerHTML = sOriginalContent;*/

				var printCssUrl = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.css", "/style.css");
				var link = '<link rel="stylesheet" href="' + printCssUrl + '" type="text/css" />';
				var printCssUrl1 = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.css", "/taskpanel.css");
				link = link + '<link rel="stylesheet" href="' + printCssUrl1 + '" type="text/css" />';
				var printCssUrl2 = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.css", "/fonts.css");
				link = link + '<link rel="stylesheet" href="' + printCssUrl2 + '" type="text/css" />';
				var printCssUrl3 = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.css", "/boxstyles.css");
				link = link + '<link rel="stylesheet" href="' + printCssUrl3 + '" type="text/css" />';
				var printCssUrl4 = jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.css", "/fonts");
				link = link + '<link rel="stylesheet" href="' + printCssUrl4 + '" type="text/css" />';
				var sURI = sap.ui.core.IconPool.getIconURI("accept");
				var url = sap.ui.require.toUrl(sURI);
				link = link + '<link rel="stylesheet" href="' + url + '" />';

				var hContent = '<html><head>' + link + '</head><body>';
				var bodyContent = sTargetContent;
				var closeContent = "</body></html>";
				var htmlpage = hContent + bodyContent + closeContent;

				var win = window.open("", "PrintWindow");
				win.document.open();
				win.document.write(htmlpage);
				$.each(document.styleSheets, function (index, oStyleSheet) {
					if (oStyleSheet.href) {
						var link = document.createElement("link");
						link.type = oStyleSheet.type;
						link.rel = "stylesheet";
						link.href = oStyleSheet.href;
						win.document.head.appendChild(link);
						//	win.document.getElementsByTagName("head")[0].innerHTML = win.document.getElementsByTagName("head")[0].innerHTML + link.outerHTML;
					}
				});
				setTimeout(function () {
					if (oForm === "JSAPermit") {
						var id8 = win.document.getElementsByClassName("gridId")[0].children[1].children[8].id;
						win.document.getElementById(id8).style.marginTop = "4.9cm";
						var id9 = win.document.getElementsByClassName("gridId")[0].children[1].children[9].id;
						win.document.getElementById(id9).style.marginTop = "4.9cm";
						var id10 = win.document.getElementsByClassName("gridId")[0].children[1].children[10].id;
						win.document.getElementById(id10).style.marginTop = "4.9cm";
						var id11 = win.document.getElementsByClassName("gridId")[0].children[1].children[11].id;
						win.document.getElementById(id11).style.marginTop = "4.9cm";
					}

					/*AN: Start of #ThemeBug*/
					if (isDarkTheme) {
						win.document.body.bgColor = "#1c2228";
					}

					/*AN: End of #ThemeBug*/

					win.print();
					win.document.close();
					win.close();
				}, 1000);

			} else {
				jQuery.sap.log.error("Print needs a valid target container");
			}

		}
	};
});