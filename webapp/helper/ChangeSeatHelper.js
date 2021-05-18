sap.ui.define([
	"sap/m/MessageToast",
	"sap/m/MessageBox",
	"sap/ui/model/json/JSONModel"
], function (MessageToast, MessageBox, JsonModel) {
	"use strict";
	return {
		/** 
		 * Function to initialize model for Change Seat feature
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		_initializeLocalModelForChangeSeat: function (oDashBoardScope, oDashBoardModel) {
			oDashBoardModel.setProperty("/changeSeatData", {
				changeSeatIOPUserRoles: [],
				changeSeatIOPUserRoleKeys: [],
				changeSeatIOPUserDisplayRoles: "",
				changeSeatIOPUserRoleSelectEnabled: true,
				changeSeatIOPUserRoleVisible: false,
				changeSeatIOPUserRoleCreatedByMe: false,
				changeSeatIOPUserRoleUserId: "",
				changeSeatIOPLogOutError: "",
				changeSeatIOPUserRolesValueState: "None",
				changeSeatIOPUserRolesValueStateText: ""
			});
			oDashBoardScope.setModel(new JsonModel(), "oIOPRoles");
		},
		/** 
		 * Function to set Storage and capture logout Event.
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		setAppStorageInfo: function (oDashBoardScope, oDashBoardModel) {
			oDashBoardScope._oStorage = jQuery.sap.storage(jQuery.sap.storage.Type.local);
			try {
				oDashBoardScope._oShellContainer = jQuery.sap.getObject("sap.ushell.Container");
				oDashBoardScope._oShellContainer.attachLogoutEvent(function (oEvent) {
					if (oDashBoardScope._oStorage.get("ChangeSeatUserRole")) {
						oDashBoardScope._oStorage.clear("ChangeSeatUserRole");
						oDashBoardScope._oStorage.clear("ChangeSeatIOPUserRoleCreatedByMe");
					}
				});
			} catch (error) {
				oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPLogOutError", error);
			}
		},
		/** 
		 * Function to Set or Get IOP Roles
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		setOrGetIOPUserRoles: function (oDashBoardScope, oDashBoardModel) {
			var oUserData = oDashBoardModel.getProperty("/userData");
			var oArgs = oDashBoardModel.getProperty("/arguments");
			var isReadOnly = oDashBoardModel.getProperty("/isWebReadOnlyRole");
			var oFragmentId = "idChangeSeatDialog",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.ChangeSeatDialog";
			if (!oDashBoardScope.ChangeSeatFragment) {
				oDashBoardScope.ChangeSeatFragment = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				oDashBoardScope.getView().addDependent(oDashBoardScope.ChangeSeatFragment);
				oDashBoardScope.ChangeSeatFragment.setEscapeHandler(function (oEvent) {
					oEvent.reject();
				});
			}
			var aIopUserRoles = [],
				aKeysIopUserRoles = [],
				vIopUserRoles = "";
			if (oUserData.isROC) {
				oDashBoardModel.setProperty("/appsTab", {
					currentSelectKey: "alarms"
				});
				vIopUserRoles = oDashBoardModel.getProperty("/userData").resGroupRead;
				if (vIopUserRoles) {
					var aIopUserGroups = vIopUserRoles.split(",");
					aKeysIopUserRoles = aIopUserGroups;
					aIopUserRoles = [{
						keyIopUserRole: "IOP_TM_ROC_Catarina",
						valueIopUserRole: "ROC_Catarina"
					}, {
						keyIopUserRole: "IOP_TM_ROC_Karnes",
						valueIopUserRole: "ROC_Karnes"
					}, {
						keyIopUserRole: "IOP_TM_ROC_CentralTilden",
						valueIopUserRole: "ROC_CentralTilden"
					}, {
						keyIopUserRole: "IOP_TM_ROC_WestTilden",
						valueIopUserRole: "ROC_WestTilden"
					}, {
						keyIopUserRole: "IOP_TM_ROC_Montney",
						valueIopUserRole: "ROC_Montney"
					}, {
						keyIopUserRole: "IOP_TM_ROC_Kaybob",
						valueIopUserRole: "ROC_Kaybob"       //ST:Adding IMO_USER for test
					}, {
						keyIopUserRole: "IOP_TM_ROC_Field1",
						valueIopUserRole: "ROC_Field1"       //ST:Adding IMO_USER for test
					}, {
						keyIopUserRole: "IMO_USER",
						valueIopUserRole: "IMO_USER"
					}];
				}
				if (oUserData.isPOT) {
					var vIopUserPotRoles = oDashBoardModel.getProperty("/userData").potRole;
					if (vIopUserPotRoles) {
						var aIopUserPotRolesTemp = vIopUserPotRoles.split(",");
						for (var j = 0; j < aIopUserPotRolesTemp.length; j++) {
							aKeysIopUserRoles.push(aIopUserPotRolesTemp[j]);
						}
						aIopUserRoles.push({
							keyIopUserRole: "IOP_POT_East",
							valueIopUserRole: "POT_East"
						}, {
							keyIopUserRole: "IOP_POT_West",
							valueIopUserRole: "POT_West"
						}, {
							keyIopUserRole: "IOP_POT_Montney",
							valueIopUserRole: "POT_Montney"
						}, {
							keyIopUserRole: "IOP_POT_Kaybob",
							valueIopUserRole: "POT_Kaybob"
						});
					}
				}
				/*	if (oUserData.isForeman) {
						// aIopUserRoles.push({
						// 	keyIopUserRole: "IOP_FOREMAN",
						// 	valueIopUserRole: "Foreman"
						// });
						aKeysIopUserRoles.push(oUserData.foremanRole);
					}*/
			} else if (oUserData.isPOT) {
				oDashBoardModel.setProperty("/appsTab", {
					currentSelectKey: "pot"
				});
				vIopUserRoles = oDashBoardModel.getProperty("/userData").potRole;
				if (vIopUserRoles) {
					aKeysIopUserRoles = vIopUserRoles.split(",");
					aIopUserRoles = [{
						keyIopUserRole: "IOP_POT_East",
						valueIopUserRole: "POT_East"
					}, {
						keyIopUserRole: "IOP_POT_West",
						valueIopUserRole: "POT_West"
					}, {
						keyIopUserRole: "IOP_POT_Montney",
						valueIopUserRole: "POT_Montney"
					}, {
						keyIopUserRole: "IOP_POT_Kaybob",
						valueIopUserRole: "POT_Kaybob"
					}];
				}
			} else if (oUserData.isENG) {
				oDashBoardModel.setProperty("/appsTab", {
					currentSelectKey: "pot"
				});
				vIopUserRoles = oDashBoardModel.getProperty("/userData").engRole;
				if (vIopUserRoles) {
					aKeysIopUserRoles = vIopUserRoles.split(",");
					aIopUserRoles = [{
						keyIopUserRole: "IOP_Engineer_East",
						valueIopUserRole: "Engineer_East"
					}, {
						keyIopUserRole: "IOP_Engineer_West",
						valueIopUserRole: "Engineer_West"
					}];
					/*	aIopUserRoles = [{
							keyIopUserRole: "IOP_Engineer_Catarina",
							valueIopUserRole: "Engineer_Catarina"
						}, {
							keyIopUserRole: "IOP_Engineer_WestTilden",
							valueIopUserRole: "Engineer_WestTilden"
						}, {
							keyIopUserRole: "IOP_Engineer_CentralTilden",
							valueIopUserRole: "Engineer_CentralTilden"
						}, {
							keyIopUserRole: "IOP_Engineer_Karnes",
							valueIopUserRole: "Engineer_Karnes"
						}];*/
				}
			} else if (isReadOnly) { //SK: Readonly changes
				aKeysIopUserRoles = ["IOP_WEB_READONLY"];
				aIopUserRoles = [{
					keyIopUserRole: "IOP_WEB_READONLY",
					valueIopUserRole: "WEB_READONLY"
				}];
			}
			if (!oDashBoardScope._oStorage.get("ChangeSeatUserRole") && !oArgs["?query"] && !oUserData.isIOPAdmin && !isReadOnly) {
				oDashBoardScope.ChangeSeatFragment.open();
			} else {
				this.proceedWithApp(oDashBoardScope, oDashBoardModel);
			}
			oDashBoardModel.setProperty("/changeSeatData", {
				changeSeatIOPUserRoles: aIopUserRoles,
				changeSeatIOPUserRoleKeys: aKeysIopUserRoles,
				changeSeatIOPUserRoleSelectEnabled: true,
				changeSeatIOPUserRoleVisible: false,
				changeSeatIOPUserRoleCreatedByMe: false,
				changeSeatIOPUserRoleUserId: oDashBoardModel.getProperty("/userData/userId"),
				changeSeatIOPUserRolesValueState: "None",
				changeSeatIOPUserRolesValueStateText: ""
			});
			if (oDashBoardScope._oStorage.get("ChangeSeatIOPUserRoleCreatedByMe")) {
				oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRoleCreatedByMe", true);
			}
			var idChangeSeatMultiComboBox = sap.ui.core.Fragment.byId("idChangeSeatDialog", "idChangeSeatMultiComboBox");
			var aIopUserSelectedRoles = [],
				vIopUserSelectedRoles = "";
			for (var i = 0; i < idChangeSeatMultiComboBox.getSelectedItems().length; i++) {
				aIopUserSelectedRoles.push(idChangeSeatMultiComboBox.getSelectedItems()[i].getText());
				aIopUserSelectedRoles.sort();
			}
			if (aIopUserSelectedRoles.length > 1) {
				vIopUserSelectedRoles = aIopUserSelectedRoles.join(", ");
			} else {
				vIopUserSelectedRoles = aIopUserSelectedRoles[0];
			}
			if (isReadOnly) {
				vIopUserSelectedRoles = "WEB_READONLY"; //SK: Readonly changes
			}
			oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserDisplayRoles", vIopUserSelectedRoles);
			oDashBoardScope._oStorage.put("ChangeSeatUserRole", vIopUserRoles);
			this.getUserFieldMapping(oDashBoardScope, oDashBoardModel);
			idChangeSeatMultiComboBox.setSelectedKeys(aKeysIopUserRoles);
			if (oUserData.isROC) { //AN: #ShiftHandover Calling to display the count on Shift Handover button
				oDashBoardScope.fetchAllActiveData();
				//	oDashBoardScope.fetchOpAvailability(); //AN: #OpAvail // Calling to display the radial %age of available op //AN: UNCOMMENT LAter
			}
		},
		/** 
		 * Function to Proceed with the App
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		proceedWithApp: function (oDashBoardScope, oDashBoardModel) {
			var oUserData = oDashBoardModel.getProperty("/userData");
			oDashBoardScope._bindInitialLeftPanel();
			oDashBoardScope.onAfterLoggedInUserDetails(oDashBoardModel.getProperty("/userData"));
			var bTaskManagementPanel = oDashBoardModel.getProperty("/taskManagementPanel");
			oDashBoardScope.oTimerRefresh = setInterval(function () {
				if (bTaskManagementPanel.showTaskManagementPanel && !bTaskManagementPanel.showBypassPanel && !bTaskManagementPanel.showOpAvailPanel) { //AN: #piggingChange //AN: #OpAvail
					oDashBoardScope.onTaskRefreshPress();
				}
			}, 15000);
			oDashBoardScope.oAlarmRefresh = setInterval(function () {
				if (oDashBoardModel.getProperty("/alarmRefresh") && oDashBoardModel.getProperty(
						"/panelProperties/currentSelectKey") === "alarms") {
					oDashBoardScope.getAlarmData();
				}
				if ((oDashBoardModel.getProperty("/panelProperties/currentSelectKey") === "obx") && oDashBoardModel.getProperty(
						"/OBXSchedulingData/selectedKey") === "obxScheduling") { //AN: #obxEngine
					oDashBoardScope.getOBXUserListData();
				}
			}, 15000);
			oDashBoardScope.oAlarmRefresh = setInterval(function () {
				if ((oDashBoardModel.getProperty("/panelProperties/currentSelectKey") === "fracMonitoring")) { //Sk: #Farc Monitoring
					oDashBoardScope.getFracListDetails();
				}
			}, 300000);
			if (oUserData.isROC) {
				oDashBoardScope._getParentCodesforDownTime();
				oDashBoardScope._getCompressorDowntimeCode();
				oDashBoardScope._getFlareCode();
			}
			if (oUserData.isROC || oUserData.isENG || oUserData.isPOT) { //RV: #webSocket
				oDashBoardScope.handleWebSocket(oDashBoardModel); //RV: #webSocket				
			}
		},
		/** 
		 * Function to get Field Mapping based on User Role
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		getUserFieldMapping: function (oDashBoardScope, oDashBoardModel) {
			var oChangeSeatData = oDashBoardModel.getProperty("/changeSeatData");
			var sUserTechRoles = "";
			if (oChangeSeatData.changeSeatIOPUserRoleKeys.length > 0) {
				for (var k = 0; k < oChangeSeatData.changeSeatIOPUserRoleKeys.length; k++) {
					if (sUserTechRoles !== "") {
						sUserTechRoles = sUserTechRoles + "," + oChangeSeatData.changeSeatIOPUserRoleKeys[k];
					} else {
						sUserTechRoles = sUserTechRoles + oChangeSeatData.changeSeatIOPUserRoleKeys[k];
					}
				}
				var sUrl = "/taskmanagementRest/userIdp/getFieldByRoleId?techRole=" + sUserTechRoles + "&bizRole=" + oChangeSeatData.changeSeatIOPUserDisplayRoles;
				oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
					if (oData.length !== 0) {
						oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserFields", oData);
						var oTaskFilterData = oDashBoardModel.getProperty("/taskFilterData");
						var oUserData = oDashBoardModel.getProperty("/userData");
						if (!oUserData.isIOPAdmin) {
							oTaskFilterData.fields = [];
							var dotROC = oData.ROC;
							var dotPOT = oData.POT;
							var dotENG = oData.Engineer;
							if (dotROC) {
								for (var r = 0; r < oTaskFilterData.tempFields.length; r++) {
									if (dotROC.indexOf(oTaskFilterData.tempFields[r].text) !== -1) {
										oTaskFilterData.fields.push(oTaskFilterData.tempFields[r]);
									}
								}
							}
							if (dotPOT) {
								for (var p = 0; p < oTaskFilterData.tempFields.length; p++) {
									if (dotPOT.indexOf(oTaskFilterData.tempFields[p].text) !== -1) {
										if (dotROC && oTaskFilterData.fields.length !== 0) {
											if (dotROC.indexOf(oTaskFilterData.tempFields[p].text) === -1) {
												oTaskFilterData.fields.push(oTaskFilterData.tempFields[p]);
											}
										} else {
											oTaskFilterData.fields.push(oTaskFilterData.tempFields[p]);
										}
									}
								}
							}
							if (dotENG) {
								for (var e = 0; e < oTaskFilterData.tempFields.length; e++) {
									if (dotENG.indexOf(oTaskFilterData.tempFields[e].text) !== -1) {
										oTaskFilterData.fields.push(oTaskFilterData.tempFields[e]);
									}
								}
							}
							if (oTaskFilterData.fields.length === 0) {
								oTaskFilterData.fields = oTaskFilterData.tempFields;
							}
						}
					}
				});
			}
		},
		/** 
		 * Function to handle created by me checkbox
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onChangeSeatCreatedByMeCheckboxSelect: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var vIsSelected = oEvent.getSource().getSelected();
			oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRoleCreatedByMe", vIsSelected);
			if (vIsSelected) {
				oDashBoardScope._oStorage.put("ChangeSeatIOPUserRoleCreatedByMe", true);
			} else {
				jQuery.sap.storage.clear("ChangeSeatIOPUserRoleCreatedByMe");
			}
		},
		/** 
		 * Functionality for changing the roles
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oEvent - Event parameters
		 */
		onChangeSeatProceedButtonPress: function (oDashBoardScope, oDashBoardModel, oEvent) {
			var idChangeSeatMultiComboBox = sap.ui.core.Fragment.byId("idChangeSeatDialog", "idChangeSeatMultiComboBox");
			if (idChangeSeatMultiComboBox.getSelectedItems() && idChangeSeatMultiComboBox.getSelectedItems().length !== 0) {
				var aIopUserRoles = [];
				for (var i = 0; i < idChangeSeatMultiComboBox.getSelectedItems().length; i++) {
					aIopUserRoles.push(idChangeSeatMultiComboBox.getSelectedItems()[i].getText());
				}
				/*if (aIopUserRoles.includes("Foreman")) {
					if (aIopUserRoles.includes("ROC_Catarina") || aIopUserRoles.includes("ROC_Karnes") ||
						aIopUserRoles.includes("ROC_CentralTilden") || aIopUserRoles.includes("ROC_WestTilden")) {
						oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueState", "None");
						oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueStatetext", "");
						oDashBoardModel.refresh();
						this.updateIOPUserData(oDashBoardScope, oDashBoardModel, aIopUserRoles);
					} else {
						oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueStateText", "Foreman sholud have atleast one ROC role");
						oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueState", "Error");
					}
				} else {*/
				oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueState", "None");
				oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueStatetext", "");
				oDashBoardModel.refresh();
				this.updateIOPUserData(oDashBoardScope, oDashBoardModel, aIopUserRoles);
				// }
			} else {
				oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueStateText", "Select atleast one role");
				oDashBoardModel.setProperty("/changeSeatData/changeSeatIOPUserRolesValueState", "Error");
			}
		},
		/** 
		 * Function to update IOP User Data
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Array} aSelectedUserRoles -Array of Selected Roles
		 */
		updateIOPUserData: function (oDashBoardScope, oDashBoardModel, aSelectedUserRoles) {
			oDashBoardModel.setProperty("/oUserDetail/selectedRoles", aSelectedUserRoles);
			var oUserDetail = oDashBoardModel.getProperty("/oUserDetail");
			var oGroupsArray = [];
			if (oUserDetail.selectedRoles.length > 0) {
				for (var k = 0; k < oUserDetail.selectedRoles.length; k++) {
					this.getTechnicalRoles(oDashBoardScope, oDashBoardModel, oUserDetail.selectedRoles[k]);
				}
				var oRoles = oDashBoardScope.getView().getModel("oIOPRoles").getData();
				for (var i = 0; i < oUserDetail.selectedRoles.length; i++) {
					if (oRoles[oUserDetail.selectedRoles[i]] && oRoles[oUserDetail.selectedRoles[i]].length > 0) {
						for (var j = 0; j < oRoles[oUserDetail.selectedRoles[i]].length; j++) {
							var oObj = {
								"value": oRoles[oUserDetail.selectedRoles[i]][j].technicalRoles,
								"display": oRoles[oUserDetail.selectedRoles[i]][j].technicalRoles
							};
							oGroupsArray.push(oObj);
						}
					}
				}
			}
			oGroupsArray = this.removeDuplicates(oGroupsArray, "value");
			if (oGroupsArray.length !== 0) {
				$.each(oUserDetail.groups, function (indx, val) {
					//to fetch all the user roles which are not related to IOP and the IOP Amdin role is retained
					if (val.value.substr(0, 3) !== "IOP" || val.value.toUpperCase() === "IOP_ADMIN") {
						oGroupsArray.push(val);
					}
				});
				oGroupsArray = this.removeDuplicates(oGroupsArray, "value");
				oUserDetail.groups = oGroupsArray;
				var sRoles = "";
				$.each(oUserDetail.selectedRoles, function (indx, val) {
					if (val !== "") {
						if (sRoles !== "") {
							sRoles = sRoles + "," + val;
						} else {
							sRoles = sRoles + val;
						}
					}
				});
				oDashBoardScope.oTempStageTableData = {
					"serialId": "",
					"userFirstName": oUserDetail.name.givenName,
					"userLastName": oUserDetail.name.familyName,
					"userEmail": oUserDetail.emails[0].value,
					"userRole": sRoles,
					"userLoginName": oUserDetail.userName,
					"taskAssignable": "",
					"pId": oUserDetail.id
				};
				var aAttributes = oUserDetail["urn:sap:cloud:scim:schemas:extension:custom:2.0:User"].attributes;
				for (var a = 0; a < aAttributes.length; a++) {
					if (aAttributes[a].name === "customAttribute6") {
						oDashBoardScope.oTempStageTableData.serialId = aAttributes[a].value;
					} else if (aAttributes[a].name === "customAttribute7") {
						oDashBoardScope.oTempStageTableData.taskAssignable = aAttributes[a].value;
					}
				}
				delete oUserDetail.passwordStatus;
				delete oUserDetail.selectedRoles;
				var oPayload = oUserDetail;
				var oHeader = {
					"Content-Type": "application/scim+json"
				};
				var sUrl = "/destination/MurphyCloudIdPDest/service/scim/Users/" + oUserDetail.id;
				var oModel = new JsonModel();
				oDashBoardScope.oBusyInd.open();
				oModel.loadData(sUrl, JSON.stringify(oPayload), true, "PUT", false, false, oHeader);
				oModel.attachRequestCompleted(function (oEvent) {
					if (oEvent.getParameter("success")) {
						var sIOPUrl = "/taskmanagementRest/userIdp/createOrUpdateUser";
						$.ajax({
							url: sIOPUrl,
							type: "POST",
							data: JSON.stringify(oDashBoardScope.oTempStageTableData),
							async: true,
							contentType: "application/scim+json",
							success: function (oData) {
								oDashBoardScope.getUserDetails();
								oDashBoardScope.oBusyInd.close();
								oDashBoardScope.ChangeSeatFragment.close();
							},
							error: function (oError) {
								oDashBoardScope.oBusyInd.close();
								sap.m.MessageToast.show("Staging Update Failed", {
									width: "30em"
								});
							}
						});
					} else {
						oDashBoardScope.oBusyInd.close();
						MessageBox.error("Error while updating user details", {
							styleClass: "fldtckMessageBoxClass"
						});
					}
				});
				oModel.attachRequestFailed(function (oEvent) {
					oDashBoardScope.oBusyInd.close();
					MessageBox.error("Error while updating user details", {
						styleClass: "fldtckMessageBoxClass"
					});
				});
			} else {
				oDashBoardScope.oBusyInd.close();
				MessageToast.show("Error in Updating User Details");
			}
		},
		/** 
		 * Function to remove duplicates
		 * @param {Array} originalArray- the original array 
		 * @param {String} prop - Property
		 * @returns {Array} newArray - returns the  new array
		 */
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
		/** 
		 * Function to get Technical roles for business role
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {String} sBusinessRole - Conatains the business role
		 */
		getTechnicalRoles: function (oDashBoardScope, oDashBoardModel, sBusinessRole) {
			oDashBoardScope.oBusyInd.open();
			var sToken = this._fetchToken(oDashBoardScope, "/bpmrulesRest/rules-service/v1/rules/xsrf-token");
			var sUrl = "/bpmrulesRest/rules-service/rest/v1/rule-services/java/Murphy_User_Provisioning_Rules/getTechnicalRoles";
			var oPayload = {
				"__type__": "user_Provisioning_Input_Dto",
				"appType": "IOP",
				"businessRole": sBusinessRole
			};
			$.ajax({
				url: sUrl,
				method: "POST",
				contentType: "application/json;charset=utf-8",
				async: false,
				data: JSON.stringify(oPayload),
				headers: {
					"X-CSRF-Token": sToken
				},
				success: function (result, xhr, data) {
					oDashBoardScope.getView().getModel("oIOPRoles").setProperty("/" + sBusinessRole, result);
				},
				error: function (error) {
					oDashBoardScope.oBusyInd.close();
					MessageToast.show("Error in retreving roles");
				}
			});
		},
		/** 
		 * Function to fetch the token
		 * @constructor 
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {String} oUrl - Rules url
		 * @returns {String} token - returns the token
		 */
		_fetchToken: function (oDashBoardScope, oUrl) {
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
					oDashBoardScope.oBusyInd.close();
					oDashBoardScope._createConfirmationMessage("Error", data, "Error", "", "Close", false, null);
				}.bind(this)
			});
			return token;
		},
		/** 
		 * Function to check the authorization for creating tasks.
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Array} aTaskTypeDetails - Task Type Details
		 * @param {String} sTaskType - Task Type (Inquiry/Dispatch/Investigation/Non-Dispatch)
		 * @param {String} sWho - ROC/POT/ENG
		 * @param {Object} oEvent - Eventing parameter
		 */
		checkAuthorization: function (oDashBoardScope, oDashBoardModel, aTaskTypeDetails, sTaskType, sWho, oEvent) {
			var oUserData = oDashBoardModel.getProperty("/userData");
			var isAuthorized = true,
				aLocCode = [],
				sAuthorizedErrorText = "User not authorized to ",
				sUrl = "/taskmanagementRest/location/";
			for (var c = 0; c < aTaskTypeDetails.length; c++) { //AN: This will be executed more than once ONLY in case of Acknowledging the alarms from Alarms Module.
				var vLocCode = aTaskTypeDetails[c].sLocCode,
					vLocType = aTaskTypeDetails[c].sLocType,
					vLocText = aTaskTypeDetails[c].sLocText;
				if (vLocType && (vLocText || vLocCode)) {
					if (sTaskType === "Non-Dispatch") {
						sUrl = sUrl + "getField?location=" + vLocText + "&locType=" + vLocType;
					} else {
						aLocCode.push(vLocCode);
						sUrl = sUrl + "getFieldTextLoc?locationList=" + aLocCode + "&locType=" + vLocType;
					}
					oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
						if (oData.responseMessage.statusCode === "0") {
							var sField = oData.field;
							var aChangeSeatIOPUserFields = oDashBoardModel.getProperty("/changeSeatData/changeSeatIOPUserFields");
							var aTechnicalRole = Object.keys(aChangeSeatIOPUserFields);
							/*	if (oUserData.isIOPAdmin) { 
									isAuthorized = true;
								} else {*/
							if (sTaskType === "Alarm-Acknowledge" && isAuthorized === false) { //SK: Don't check again if the user has no authorization for Alarm-Acknowledge
								return;
							}
							for (var k = 0; k < aTechnicalRole.length; k++) {
								sWho = aTechnicalRole[k];
								for (var f = 0; f < aChangeSeatIOPUserFields[sWho].length; f++) {
									if (aChangeSeatIOPUserFields[sWho][f].trim() === sField.trim()) {
										// AN: Additional checks, if logged in using cross roles, such as ROC and POT
										if (oUserData.isPOT && oUserData.isROC) { //AN: #inquiryEnhancement
											if (sWho.includes("POT") && (sTaskType.includes("Dispatch") || sTaskType === "Alarm-Designate" || sTaskType ===
													"Issue-PiggRetrival" || sTaskType === "Alarm-Acknowledge")) { // POT trying to create dispatch or Alarm-Acknowledge
												oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", "");
												isAuthorized = false;
											} else if (sWho.includes("ROC") && (sTaskType.includes("Investigation") || sTaskType === "Prove-Comment")) { // ROC trying to create investigation
												oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", "");
												isAuthorized = false;
											} else if (sWho.includes("ROC") && sTaskType.includes("Inquiry") && oDashBoardModel.getProperty(
													"/panelProperties/currentSelectKey") === "ndtpv") { // ROC trying to create Inquiry from PROVE
												oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", "");
												isAuthorized = false;
											} else if (sWho.includes("POT") && sTaskType.includes("Inquiry") && oDashBoardModel.getProperty(
													"/panelProperties/currentSelectKey") === "DOP") { // POT trying to create Inquiry from DOP
												oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", "");
												isAuthorized = false;
											} else {
												isAuthorized = true;
												oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", sWho);
												break;
											}
										} else {
											isAuthorized = true;
											oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", sWho);
											break;
										}
									} else {
										oDashBoardModel.setProperty("/changeSeatData/taskCreationRole", "");
										isAuthorized = false;
									}
								}
								if (isAuthorized === true && oDashBoardModel.getProperty("/changeSeatData/taskCreationRole")) {
									break;
								}
							}
							//}
							if (c === aTaskTypeDetails.length && isAuthorized) { //AN: All the Iterations are over, proceed with the below block
								if (sTaskType === "Module-Dispatch" || sTaskType === "Heirarchy-Dispatch") {
									this.proceedWithDispatch(oDashBoardScope, oDashBoardModel, aTaskTypeDetails[0], sTaskType);
								} else if (sTaskType === "Non-Dispatch") {
									this.proceedWithNonDispatch(oDashBoardScope, oDashBoardModel, aTaskTypeDetails[0]);
								} else if (sTaskType === "Comp-Dispatch") {
									this.proceedWithCompressorDispatch(oDashBoardScope, oDashBoardModel, aTaskTypeDetails[0]);
								} else if (sTaskType === "Module-Inquiry") {
									oDashBoardScope.onPressCreateInquiry(oEvent, aTaskTypeDetails[0].oCurrSelectedLocation);
								} else if (sTaskType === "Module-Investigation") {
									oDashBoardScope.onPressCreateInvestigation(oEvent);
								} else if (sTaskType === "Alarm-Designate") {
									this.proceedWithAlarmDesignate(oDashBoardScope, oDashBoardModel, aTaskTypeDetails[0]);
								} else if (sTaskType.includes("IconClick")) { //AN: #inquiryEnhancement
									this.proceedWithIconClick(oDashBoardScope, oDashBoardModel, aTaskTypeDetails[0]);
								} else if (sTaskType === "Issue-PiggRetrival") {
									oDashBoardScope.onPressIssuePigRetrieval(oEvent);
								} else if (sTaskType === "Inquiry-Dispatch") {
									oDashBoardModel.setProperty("/taskStatus", "NEW");
									oDashBoardScope.onCreateTaskPress("", "", true);
								} else if (sTaskType === "Alarm-Acknowledge") {
									oDashBoardScope.onAcknowledgePress(oEvent); //Sk: Alarm Acknowledge
								} else if (sTaskType === "Prove-Comment") {
									oDashBoardScope.onCompletingInvestigation(oEvent);
								}
							} else if (!isAuthorized) { //AN: Irrespective of the iteration, in case of NOT AUTHORIZED execute the below block and exit out of the loop
								if (sTaskType === "Module-Dispatch" || sTaskType === "Heirarchy-Dispatch" || sTaskType === "Comp-Dispatch" || sTaskType ===
									"Inquiry-Dispatch") { //AN: #inquiryEnhancement
									sAuthorizedErrorText = sAuthorizedErrorText + "Dispatch";

									if (oDashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
										oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
									}

								} else if (sTaskType === "Non-Dispatch") {
									sAuthorizedErrorText = sAuthorizedErrorText + "Non-Dispatch";
								} else if (sTaskType === "Module-Inquiry") {
									sAuthorizedErrorText = sAuthorizedErrorText + "Inquire";
								} else if (sTaskType === "Module-Investigation") {
									sAuthorizedErrorText = sAuthorizedErrorText + "Investigate";

									if (oDashBoardModel.getProperty("/InsightToActionData/isWBInvestigation")) { //AN: #msgToROC
										oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
									}

								} else if (sTaskType === "Alarm-Acknowledge") {
									sAuthorizedErrorText = sAuthorizedErrorText + "Acknowledge";
								} else if (sTaskType === "Alarm-Designate") {
									sAuthorizedErrorText = sAuthorizedErrorText + "Designate";
								} else if (sTaskType.includes("IconClick")) { //AN: #inquiryEnhancement
									sAuthorizedErrorText = sAuthorizedErrorText + "perform this action";
								} else if (sTaskType === "Prove-Comment") { //AN: #inquiryEnhancement
									sAuthorizedErrorText = sAuthorizedErrorText + "perform this action";
								}
								oDashBoardScope.oBusyInd.close();
								oDashBoardScope._showToastMessage(sAuthorizedErrorText);
								return;
							}
						} else {
							if (oDashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
								oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
							}
							oDashBoardScope._showToastMessage(oData.responseMessage.message);
						}
					}.bind(this), function (oError) {
						var sErrorMessage;
						sErrorMessage = oError.getParameter("statusText");
						oDashBoardScope._createConfirmationMessage("Error", sErrorMessage, "Error", "", "Close", false, null);
						if (oDashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
							oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
						}
					});
				} else {
					oDashBoardScope.oBusyInd.close();
					oDashBoardScope._showToastMessage("Please select a location");
					if (oDashBoardModel.getProperty("/InsightToActionData/isWBDispatch")) { //AN: #msgToROC
						oDashBoardModel.setProperty("/InsightToActionData/iopWBMsgInd", false);
					}
				}
			}
			if (aTaskTypeDetails.length === 0) {
				oDashBoardScope.oBusyInd.close();
			}
		},
		/** 
		 * Function to Create Dispatch Task
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oTaskTypeDetails - Task Type Details
		 * @param {String} sTaskType - Module-Dispatch or Heirarchy-Dispatch
		 */
		proceedWithDispatch: function (oDashBoardScope, oDashBoardModel, oTaskTypeDetails, sTaskType) {
			if (sTaskType === "Heirarchy-Dispatch") {
				var loaction = oTaskTypeDetails.aSelectedObject[0].location;
				oDashBoardModel.setProperty("/isCreateTaskFromRightSidePanel", true);
				oDashBoardScope.oBusyInd.open();
				oDashBoardScope.checkForCreateTask(loaction);
			} else if (sTaskType === "Module-Dispatch") {
				var locationCode = oTaskTypeDetails.oCurrSelectedLocation.locationCode;
				oDashBoardScope.checkForCreateTask(locationCode, oTaskTypeDetails.alarmCreate);
			}
		},
		/** 
		 * Function to Create Compressor Dispatch Task
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oTaskTypeDetails - Task Type Details
		 */
		proceedWithCompressorDispatch: function (oDashBoardScope, oDashBoardModel, oTaskTypeDetails) {
			oDashBoardModel.setProperty("/CompressorParent", oTaskTypeDetails.oRespData.responseDto); //sk: on proceed task creation on compressor
			oDashBoardScope.checkForCreateTask(oTaskTypeDetails.oRespData.responseDto.location, "", "", oTaskTypeDetails.oRespData.responseDto,
				oTaskTypeDetails.oCurrSelectedLocation[0].locationText);
		},
		/** 
		 * Function to Create or update Non-Dispatch Task
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oTaskTypeDetails - Task Type Details
		 */
		proceedWithNonDispatch: function (oDashBoardScope, oDashBoardModel, oTaskTypeDetails) {
			var sUrl, oPayloadData = {};
			if (oTaskTypeDetails.bOperation) {
				sUrl = "/taskmanagementRest/nonDispatch/update";
				oTaskTypeDetails.oObjectContext.description = oTaskTypeDetails.sDescription;
				oTaskTypeDetails.oObjectContext.location = oTaskTypeDetails.sLocText;
				oTaskTypeDetails.oObjectContext.locType = oTaskTypeDetails.sLocType;
				oPayloadData = oTaskTypeDetails.oObjectContext;
			} else {
				sUrl = "/taskmanagementRest/nonDispatch/create";
				oPayloadData = {
					"description": oTaskTypeDetails.sDescription,
					"location": oTaskTypeDetails.sLocText,
					"createdBy": oDashBoardScope._getCurrentLoggedInUser(),
					"locType": oTaskTypeDetails.sLocType,
					"group": oDashBoardModel.getProperty("/userData/group")
				};
			}
			oDashBoardScope.doAjax(sUrl, "POST", oPayloadData, function (oData) {
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					if (oData.statusCode === "0") {
						oDashBoardScope.onAddItemPanelClose();
						oDashBoardScope._showToastMessage(oData.message);
						oDashBoardScope._bindRightTaskPanelModel("Non-Dispatch");
					} else {
						oDashBoardScope.onAddItemPanelClose();
						oDashBoardScope._createConfirmationMessage("Error", oData.message, "Error", "", "Close", false, null);
					}
				},
				function (oError) {
					oDashBoardScope.onAddItemPanelClose();
					oDashBoardModel.setProperty("/busyIndicators/rightPanelBusy", false);
					oDashBoardScope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
				});
		},
		/** 
		 * Function to Designate from Alarms Module
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oTaskTypeDetails - Task Type Details
		 */
		proceedWithAlarmDesignate: function (oDashBoardScope, oDashBoardModel, oTaskTypeDetails) {
			oDashBoardScope._getParentCodesforDownTime();
			var oFragmentId = "idDesignateFrag",
				oFragmentName = "com.sap.incture.Incture_IOP.fragment.designateView";
			if (!oDashBoardScope.oDesignateFragment) {
				oDashBoardScope.oDesignateFragment = oDashBoardScope._createFragment(oFragmentId, oFragmentName);
				oDashBoardScope.getView().addDependent(oDashBoardScope.oDesignateFragment);
			}
			oDashBoardScope.oDesignateFragment.open();
		},
		/** 
		 * Function for Icon Click functionality from DOP and PWHopper Modules
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 * @param {Object} oTaskTypeDetails - Task Type Details
		 */
		proceedWithIconClick: function (oDashBoardScope, oDashBoardModel, oTaskTypeDetails) {
			oDashBoardModel.setProperty("/busyIndicators/taskPanelBusy", true);
			var sBtnText = "Create Task";
			var dispatchTaskId = oTaskTypeDetails.oCurrSelectedLocation.dispatchTaskId,
				investigationProcessId = oTaskTypeDetails.oCurrSelectedLocation.investigationProcessId,
				investigationTaskId = oTaskTypeDetails.oCurrSelectedLocation.investigationTaskId;
			oDashBoardModel.setProperty("/taskId", dispatchTaskId);
			oDashBoardModel.setProperty("/processId", investigationProcessId);
			if (oTaskTypeDetails.sTooltip !== "Dispatched") {
				oDashBoardModel.setProperty("/isInvestigationCreate", true);
				oDashBoardModel.setProperty("/isIconPress", true);
				oDashBoardModel.setProperty("/iconPressData", oTaskTypeDetails.oCurrSelectedLocation);
				if (investigationTaskId) {
					oDashBoardModel.setProperty("/taskId", investigationTaskId);
					oDashBoardScope.onCreateTaskPress("", sBtnText);
				} else {
					var sUrl = "/taskmanagementRest/tasks/getTaskId?locationCode=" + oTaskTypeDetails.oCurrSelectedLocation.locationCode;
					oDashBoardScope.doAjax(sUrl, "GET", "", function (oData) {
							if (oData.responseMessage.statusCode === "0") {
								oDashBoardModel.setProperty("/taskId", oData.taskId);
								oDashBoardScope.onCreateTaskPress("", sBtnText);
							} else {
								oDashBoardScope._createConfirmationMessage("Error", oData.responseMessage.message, "Error", "", "Close", false, null);
							}
						},
						function (oError) {
							oDashBoardScope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
						});
				}
			} else {
				oDashBoardModel.setProperty("/isInvestigationCreate", false);
				oDashBoardModel.setProperty("/taskStatus", "");
				oDashBoardScope.onCreateTaskPress("", sBtnText);
			}
			//oDashBoardScope.onCreateTaskPress("", sBtnText);
		},
		/** 
		 * Function tofetch wells based on user role
		 * @param {Object} oDashBoardScope - Scope(this) of DashBoard Controller
		 * @param {Object} oDashBoardModel - dashBoard Model
		 */
		getAllWellsForFrac: function (oDashBoardScope, oDashBoardModel) {
			var oChangeSeatData = oDashBoardModel.getProperty("/changeSeatData");
			var sUserTechRoles = "";
			if (oChangeSeatData.changeSeatIOPUserRoleKeys.length > 0) {
				for (var k = 0; k < oChangeSeatData.changeSeatIOPUserRoleKeys.length; k++) {
					if (sUserTechRoles !== "") {
						sUserTechRoles = sUserTechRoles + "," + oChangeSeatData.changeSeatIOPUserRoleKeys[k];
					} else {
						sUserTechRoles = sUserTechRoles + oChangeSeatData.changeSeatIOPUserRoleKeys[k];
					}
				}
				var sUrl = "/taskmanagementRest/userIdp/getWellByGroup?techRole=" + sUserTechRoles + "&bizRole=" + oChangeSeatData.changeSeatIOPUserDisplayRoles;
				oDashBoardScope.doAjax(sUrl, "GET", null, function (oData) {
						if (oData.message.statusCode === "0") {
							oDashBoardScope.getModel("oAllWellModel").setSizeLimit(oData.dto.locationHierarchy.length);
							oDashBoardScope.getModel("oAllWellModel").setProperty("/allWellData", oData.dto.locationHierarchy);
						} else {
							oDashBoardScope._createConfirmationMessage("Error", oData.message.message, "Error", "", "Close", false, null);
						}
					},
					function (oError) {
						oDashBoardScope._createConfirmationMessage("Error", oError.statusText, "Error", "", "Close", false, null);
					});
			}
		}
	};
});