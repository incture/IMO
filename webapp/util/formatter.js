jQuery.sap.declare("com.sap.incture.Incture_IOP.util.formatter");

com.sap.incture.Incture_IOP.util.formatter = {
	addMiles: function (prop, country) {
		if (prop) {
			if (country === "EFS") {
				return prop + " miles";
			} else if (country === "CA") {
				var sKm = prop * 1.6093;
				sKm = sKm.toFixed(2);
				return sKm + " KM";
			} else {
				return prop + " miles";
			}
		} else {
			return "";
		}
	},
	addPsi: function (oVal) {
		if (oVal !== null) {
			return oVal + " psi";
		} else {
			return oVal;
		}
	},
	addMts: function (oVal) {
		if (oVal !== null) {
			return oVal + " mts";
		} else {
			return oVal;
		}
	},
	changeTextColor: function (oVal) {
		if (oVal) {

			if (parseInt(oVal.split(":")[0]) >= 1) {
				return "None";
			} else {

				return "Error";
			}
		}

	},
	showlessTextforOverFlow: function (sText) {
		if (sText && sText.length) {
			return sText.split(",").length > 1 ? sText.split(",")[0] + "..." : sText;
		}
	},
	navBackButtonEnable: function (sProp) {
		var _bEnabled = true;
		if (sProp === "BASE" || sProp === "Base" || sProp === null || sProp === undefined || sProp === "") {
			_bEnabled = false;
		}
		return _bEnabled;
	},
	callControllerFileFunc: function () {
		if (sap.ui.Device.browser.name === "ie") {
			var controller = sap.ui.getCore().byId("__xmlview1").getController();
			controller._loadImageFromClipBoard(window.clipboardData.files[0], "ie");
		}
	},

	callChangePasteArea: function (oEvent) {
		$("#pastearea").html("Focus here and paste your snip content");
	},

	showWell: function (loc, locationtype, review) {

		if (review) {
			return loc;
		} else {
			if (locationtype === "Well" || locationtype === "SEARCH") {
				return loc;
			} else {
				return "";
			}

		}
	},
	getinitials: function (sFirstName, sLastname) {
		if (sFirstName && sFirstName.charAt(0) === " ") {
			sFirstName = sFirstName.substr(1);
		}
		if (sLastname && sLastname.charAt(0) === " ") {
			sLastname = sLastname.substr(1);
		}
		var sFirstLetterFname = "",
			sFirstLetterLname = "",
			sInitial = "";
		if (sFirstName && sFirstName !== null && sFirstName !== undefined && sFirstName !== "") {
			sFirstLetterFname = sFirstName.substr(0, 1);
		}
		if (sLastname && sLastname !== null && sLastname !== undefined && sLastname !== "") {
			sFirstLetterLname = sLastname.substr(0, 1);
		} else if (sFirstName) {
			var oArray = sFirstName.split(" ");
			if (oArray.length > 1) {
				sFirstLetterLname = oArray.pop().substr(0, 1);
			} else {
				sFirstLetterLname = sFirstName.substr(1, 1);
			}
		}
		sInitial = sFirstLetterFname + sFirstLetterLname;
		return sInitial;

	},
	/*Formatters for Task Panel--Kiruthika*/

	/*To change the button text in task panel*/
	fnBtnText: function (oValue) {
		if (oValue == "Non-Dispatch") {
			return "Add Item";
		} else {
			return "Create Task";
		}
	},

	/*To show items of All/Sent Items*/
	fnShowTaskDetails: function (oValue, oLatestComment, oCreatedAt) {
		if (oValue === "Non-Dispatch" || (oCreatedAt && oLatestComment)) {
			return false;
		} else if ((oLatestComment !== undefined) && (oLatestComment === null || oLatestComment === "")) {
			if (!oCreatedAt) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	},

	/*To show items of Non-Dispatch Items*/
	fnShowTaskItems: function (oValue) {
		if (oValue == "Non-Dispatch") {
			return true;
		} else {
			return false;
		}
	},
	// To hide grip button for Non-Dispatch 
	fnShowGripIcon: function (oValue) {
		if (oValue == "Non-Dispatch") {
			return false;
		} else {
			return true;
		}
	},
	/*To change the highlight color of Items*/
	fnHighlightColor: function (oValue, oStatus) {
		if (oValue) {
			this.removeStyleClass("iopwbHighlightChangedClass");
			this.removeStyleClass("iopwbListResolvedStatusBgColor");
			this.removeStyleClass("iopwbListInProgressStatusBgColor");
			this.removeStyleClass("iopwbListDispatchedStatusBgColor");
			this.removeStyleClass("iopwbListReturnedStatusBgColor");
			this.removeStyleClass("iopwbListHighlightOrangeClass");
			this.removeStyleClass("iopwbListSubmittedStatusBgColor");
			if (oValue == "Non-Dispatch") {
				this.addStyleClass("iopwbListHighlightOrangeClass");
				this.addStyleClass("iopwbHighlightChangedClass");
			} else {
				if (oStatus === "ASSIGNED") {
					this.addStyleClass("iopwbListDispatchedStatusBgColor");
				} else if (oStatus === "RESOLVED") {
					this.addStyleClass("iopwbListResolvedStatusBgColor");
				} else if (oStatus === "IN PROGRESS") {
					this.addStyleClass("iopwbListInProgressStatusBgColor");
				} else if (oStatus === "RETURNED") {
					this.addStyleClass("iopwbListReturnedStatusBgColor");
				} else if (oStatus === "SUBMITTED") {
					this.addStyleClass("iopwbListSubmittedStatusBgColor");
				} else {
					this.addStyleClass("iopwbListDispatchedStatusBgColor");
				}
			}
		}
		return true;
	},

	fnFileIcon: function (oType) {
		if (oType == "doc") {}
	},

	fnGetTblMode: function (oValue) {
		if (oValue === true) {
			return "Delete";
		} else {
			return "None";
		}
	},

	showSubClassItems: function (oDependent, oMainClass) {
		if (oDependent === oMainClass) {
			return true;
		} else {
			return false;
		}
	},

	getSrc: function (oType, oBase) {
		var src = "data:" + oType + ";base64," + oBase;
		return src;
	},

	showStatus: function (oCreated, oReturned, oStatus) {
		if (oCreated === false && oReturned === false) {
			return true;
		}
		/* else if (oCreated === false && oReturned === true && oStatus === "RESOLVED") {
					return true;
				}*/
		return false;
	},

	showDelBtn: function (oCreated, oReturned, oStatus) {
		if (oCreated === true || oReturned === true) {
			return true;
		} else {
			return false;
		}
	},

	fnGetStatusBgColor: function (oValue, oStatus) {
		this.removeStyleClass("iopwbResolvedStatusBgColor");
		this.removeStyleClass("iopwbInProgressStatusBgColor");
		this.removeStyleClass("iopwbDispatchedStatusBgColor");
		this.removeStyleClass("iopwbReturnedStatusBgColor");
		this.removeStyleClass("iopwbSubmittedStatusBgColor");
		this.removeStyleClass("iopwbReAssignedStatusBgColor");
		this.removeStyleClass("iopwbCancelledStatusBgColor");
		if (oStatus === "ASSIGNED") {
			this.addStyleClass("iopwbDispatchedStatusBgColor");
		} else if (oStatus === "RESOLVED") {
			this.addStyleClass("iopwbResolvedStatusBgColor");
		} else if (oStatus === "IN PROGRESS") {
			this.addStyleClass("iopwbInProgressStatusBgColor");
		} else if (oStatus === "RETURNED") {
			this.addStyleClass("iopwbReturnedStatusBgColor");
		} else if (oStatus === "SUBMITTED") {
			this.addStyleClass("iopwbSubmittedStatusBgColor");
		} else if (oStatus === "NEW") { //AN: #piggingChange
			this.addStyleClass("iopwbNewStatusBgColor");
		} else if (oStatus === "CANCELLED") {
			this.addStyleClass("iopwbReturnedStatusBgColor");
		} else {
			this.addStyleClass("iopwbDispatchedStatusBgColor");
		}
		if (oValue === "Non-Dispatch") {
			return false;
		} else {
			return true;
		}
	},

	fngetStatusIcon: function (oStatus) {
		if (oStatus === "RESOLVED") {
			return "sap-icon://sys-enter";
		} else {
			return "sap-icon://sys-cancel";
		}
	},

	getLocalDate: function (oDate, oString, oCreate, oMsgId, oInvestigation, oInquiry) { //AN: #inquire
		var returnString = "";
		if (oString) {
			returnString = oString + " ";
		}
		if (!oDate) {
			return returnString;
		}
		if (oCreate || (!oMsgId && (oInvestigation || oInquiry))) { //AN: #inquire
			return oDate;
		}
		var re =
			/[\0-\x1F\x7F-\x9F\xAD\u0378\u0379\u037F-\u0383\u038B\u038D\u03A2\u0528-\u0530\u0557\u0558\u0560\u0588\u058B-\u058E\u0590\u05C8-\u05CF\u05EB-\u05EF\u05F5-\u0605\u061C\u061D\u06DD\u070E\u070F\u074B\u074C\u07B2-\u07BF\u07FB-\u07FF\u082E\u082F\u083F\u085C\u085D\u085F-\u089F\u08A1\u08AD-\u08E3\u08FF\u0978\u0980\u0984\u098D\u098E\u0991\u0992\u09A9\u09B1\u09B3-\u09B5\u09BA\u09BB\u09C5\u09C6\u09C9\u09CA\u09CF-\u09D6\u09D8-\u09DB\u09DE\u09E4\u09E5\u09FC-\u0A00\u0A04\u0A0B-\u0A0E\u0A11\u0A12\u0A29\u0A31\u0A34\u0A37\u0A3A\u0A3B\u0A3D\u0A43-\u0A46\u0A49\u0A4A\u0A4E-\u0A50\u0A52-\u0A58\u0A5D\u0A5F-\u0A65\u0A76-\u0A80\u0A84\u0A8E\u0A92\u0AA9\u0AB1\u0AB4\u0ABA\u0ABB\u0AC6\u0ACA\u0ACE\u0ACF\u0AD1-\u0ADF\u0AE4\u0AE5\u0AF2-\u0B00\u0B04\u0B0D\u0B0E\u0B11\u0B12\u0B29\u0B31\u0B34\u0B3A\u0B3B\u0B45\u0B46\u0B49\u0B4A\u0B4E-\u0B55\u0B58-\u0B5B\u0B5E\u0B64\u0B65\u0B78-\u0B81\u0B84\u0B8B-\u0B8D\u0B91\u0B96-\u0B98\u0B9B\u0B9D\u0BA0-\u0BA2\u0BA5-\u0BA7\u0BAB-\u0BAD\u0BBA-\u0BBD\u0BC3-\u0BC5\u0BC9\u0BCE\u0BCF\u0BD1-\u0BD6\u0BD8-\u0BE5\u0BFB-\u0C00\u0C04\u0C0D\u0C11\u0C29\u0C34\u0C3A-\u0C3C\u0C45\u0C49\u0C4E-\u0C54\u0C57\u0C5A-\u0C5F\u0C64\u0C65\u0C70-\u0C77\u0C80\u0C81\u0C84\u0C8D\u0C91\u0CA9\u0CB4\u0CBA\u0CBB\u0CC5\u0CC9\u0CCE-\u0CD4\u0CD7-\u0CDD\u0CDF\u0CE4\u0CE5\u0CF0\u0CF3-\u0D01\u0D04\u0D0D\u0D11\u0D3B\u0D3C\u0D45\u0D49\u0D4F-\u0D56\u0D58-\u0D5F\u0D64\u0D65\u0D76-\u0D78\u0D80\u0D81\u0D84\u0D97-\u0D99\u0DB2\u0DBC\u0DBE\u0DBF\u0DC7-\u0DC9\u0DCB-\u0DCE\u0DD5\u0DD7\u0DE0-\u0DF1\u0DF5-\u0E00\u0E3B-\u0E3E\u0E5C-\u0E80\u0E83\u0E85\u0E86\u0E89\u0E8B\u0E8C\u0E8E-\u0E93\u0E98\u0EA0\u0EA4\u0EA6\u0EA8\u0EA9\u0EAC\u0EBA\u0EBE\u0EBF\u0EC5\u0EC7\u0ECE\u0ECF\u0EDA\u0EDB\u0EE0-\u0EFF\u0F48\u0F6D-\u0F70\u0F98\u0FBD\u0FCD\u0FDB-\u0FFF\u10C6\u10C8-\u10CC\u10CE\u10CF\u1249\u124E\u124F\u1257\u1259\u125E\u125F\u1289\u128E\u128F\u12B1\u12B6\u12B7\u12BF\u12C1\u12C6\u12C7\u12D7\u1311\u1316\u1317\u135B\u135C\u137D-\u137F\u139A-\u139F\u13F5-\u13FF\u169D-\u169F\u16F1-\u16FF\u170D\u1715-\u171F\u1737-\u173F\u1754-\u175F\u176D\u1771\u1774-\u177F\u17DE\u17DF\u17EA-\u17EF\u17FA-\u17FF\u180F\u181A-\u181F\u1878-\u187F\u18AB-\u18AF\u18F6-\u18FF\u191D-\u191F\u192C-\u192F\u193C-\u193F\u1941-\u1943\u196E\u196F\u1975-\u197F\u19AC-\u19AF\u19CA-\u19CF\u19DB-\u19DD\u1A1C\u1A1D\u1A5F\u1A7D\u1A7E\u1A8A-\u1A8F\u1A9A-\u1A9F\u1AAE-\u1AFF\u1B4C-\u1B4F\u1B7D-\u1B7F\u1BF4-\u1BFB\u1C38-\u1C3A\u1C4A-\u1C4C\u1C80-\u1CBF\u1CC8-\u1CCF\u1CF7-\u1CFF\u1DE7-\u1DFB\u1F16\u1F17\u1F1E\u1F1F\u1F46\u1F47\u1F4E\u1F4F\u1F58\u1F5A\u1F5C\u1F5E\u1F7E\u1F7F\u1FB5\u1FC5\u1FD4\u1FD5\u1FDC\u1FF0\u1FF1\u1FF5\u1FFF\u200B-\u200F\u202A-\u202E\u2060-\u206F\u2072\u2073\u208F\u209D-\u209F\u20BB-\u20CF\u20F1-\u20FF\u218A-\u218F\u23F4-\u23FF\u2427-\u243F\u244B-\u245F\u2700\u2B4D-\u2B4F\u2B5A-\u2BFF\u2C2F\u2C5F\u2CF4-\u2CF8\u2D26\u2D28-\u2D2C\u2D2E\u2D2F\u2D68-\u2D6E\u2D71-\u2D7E\u2D97-\u2D9F\u2DA7\u2DAF\u2DB7\u2DBF\u2DC7\u2DCF\u2DD7\u2DDF\u2E3C-\u2E7F\u2E9A\u2EF4-\u2EFF\u2FD6-\u2FEF\u2FFC-\u2FFF\u3040\u3097\u3098\u3100-\u3104\u312E-\u3130\u318F\u31BB-\u31BF\u31E4-\u31EF\u321F\u32FF\u4DB6-\u4DBF\u9FCD-\u9FFF\uA48D-\uA48F\uA4C7-\uA4CF\uA62C-\uA63F\uA698-\uA69E\uA6F8-\uA6FF\uA78F\uA794-\uA79F\uA7AB-\uA7F7\uA82C-\uA82F\uA83A-\uA83F\uA878-\uA87F\uA8C5-\uA8CD\uA8DA-\uA8DF\uA8FC-\uA8FF\uA954-\uA95E\uA97D-\uA97F\uA9CE\uA9DA-\uA9DD\uA9E0-\uA9FF\uAA37-\uAA3F\uAA4E\uAA4F\uAA5A\uAA5B\uAA7C-\uAA7F\uAAC3-\uAADA\uAAF7-\uAB00\uAB07\uAB08\uAB0F\uAB10\uAB17-\uAB1F\uAB27\uAB2F-\uABBF\uABEE\uABEF\uABFA-\uABFF\uD7A4-\uD7AF\uD7C7-\uD7CA\uD7FC-\uF8FF\uFA6E\uFA6F\uFADA-\uFAFF\uFB07-\uFB12\uFB18-\uFB1C\uFB37\uFB3D\uFB3F\uFB42\uFB45\uFBC2-\uFBD2\uFD40-\uFD4F\uFD90\uFD91\uFDC8-\uFDEF\uFDFE\uFDFF\uFE1A-\uFE1F\uFE27-\uFE2F\uFE53\uFE67\uFE6C-\uFE6F\uFE75\uFEFD-\uFF00\uFFBF-\uFFC1\uFFC8\uFFC9\uFFD0\uFFD1\uFFD8\uFFD9\uFFDD-\uFFDF\uFFE7\uFFEF-\uFFFB\uFFFE\uFFFF]/g;

		if (oDate.split(" ").length === 1) {
			var iDate = parseInt(oDate);
			var nDate = new Date(iDate);
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "dd-MMM-yy hh:mm:ss aaa"
			});
			return oDateFormat.format(nDate);
		}

		var aDate = oDate.split(" ");
		var aDateArray = aDate[0].split("-");
		var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
		sDate = sDate + " " + aDate[1] + " " + aDate[2];
		var d = new Date(sDate + " UTC");
		//var d = new Date(Date.UTC('2018', '04' - 1, '18', '06', '16', '52'));
		var dArray = d.toDateString().split(" ");
		var date = dArray[2] + "-" + dArray[1] + "-" + (d.getYear() - 100) + " ";
		var timeArray = d.toLocaleString().split(" ");
		if (timeArray[2] === undefined) {
			if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) >= 12) {
				timeArray[2] = "PM";
				if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) >= 13) {
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
		returnString += date;
		return returnString;
	},
	getOnlyLocalDate: function (oDate, oString, oCreate, oMsgId, oInvestigation, oInquiry) { //AN: #inquire
		var returnString = "";
		if (oString) {
			returnString = oString + " ";
		}
		if (!oDate) {
			return returnString;
		}
		if (oCreate || (!oMsgId && (oInvestigation || oInquiry))) { //AN: #inquire
			return oDate;
		}
		var re =
			/[\0-\x1F\x7F-\x9F\xAD\u0378\u0379\u037F-\u0383\u038B\u038D\u03A2\u0528-\u0530\u0557\u0558\u0560\u0588\u058B-\u058E\u0590\u05C8-\u05CF\u05EB-\u05EF\u05F5-\u0605\u061C\u061D\u06DD\u070E\u070F\u074B\u074C\u07B2-\u07BF\u07FB-\u07FF\u082E\u082F\u083F\u085C\u085D\u085F-\u089F\u08A1\u08AD-\u08E3\u08FF\u0978\u0980\u0984\u098D\u098E\u0991\u0992\u09A9\u09B1\u09B3-\u09B5\u09BA\u09BB\u09C5\u09C6\u09C9\u09CA\u09CF-\u09D6\u09D8-\u09DB\u09DE\u09E4\u09E5\u09FC-\u0A00\u0A04\u0A0B-\u0A0E\u0A11\u0A12\u0A29\u0A31\u0A34\u0A37\u0A3A\u0A3B\u0A3D\u0A43-\u0A46\u0A49\u0A4A\u0A4E-\u0A50\u0A52-\u0A58\u0A5D\u0A5F-\u0A65\u0A76-\u0A80\u0A84\u0A8E\u0A92\u0AA9\u0AB1\u0AB4\u0ABA\u0ABB\u0AC6\u0ACA\u0ACE\u0ACF\u0AD1-\u0ADF\u0AE4\u0AE5\u0AF2-\u0B00\u0B04\u0B0D\u0B0E\u0B11\u0B12\u0B29\u0B31\u0B34\u0B3A\u0B3B\u0B45\u0B46\u0B49\u0B4A\u0B4E-\u0B55\u0B58-\u0B5B\u0B5E\u0B64\u0B65\u0B78-\u0B81\u0B84\u0B8B-\u0B8D\u0B91\u0B96-\u0B98\u0B9B\u0B9D\u0BA0-\u0BA2\u0BA5-\u0BA7\u0BAB-\u0BAD\u0BBA-\u0BBD\u0BC3-\u0BC5\u0BC9\u0BCE\u0BCF\u0BD1-\u0BD6\u0BD8-\u0BE5\u0BFB-\u0C00\u0C04\u0C0D\u0C11\u0C29\u0C34\u0C3A-\u0C3C\u0C45\u0C49\u0C4E-\u0C54\u0C57\u0C5A-\u0C5F\u0C64\u0C65\u0C70-\u0C77\u0C80\u0C81\u0C84\u0C8D\u0C91\u0CA9\u0CB4\u0CBA\u0CBB\u0CC5\u0CC9\u0CCE-\u0CD4\u0CD7-\u0CDD\u0CDF\u0CE4\u0CE5\u0CF0\u0CF3-\u0D01\u0D04\u0D0D\u0D11\u0D3B\u0D3C\u0D45\u0D49\u0D4F-\u0D56\u0D58-\u0D5F\u0D64\u0D65\u0D76-\u0D78\u0D80\u0D81\u0D84\u0D97-\u0D99\u0DB2\u0DBC\u0DBE\u0DBF\u0DC7-\u0DC9\u0DCB-\u0DCE\u0DD5\u0DD7\u0DE0-\u0DF1\u0DF5-\u0E00\u0E3B-\u0E3E\u0E5C-\u0E80\u0E83\u0E85\u0E86\u0E89\u0E8B\u0E8C\u0E8E-\u0E93\u0E98\u0EA0\u0EA4\u0EA6\u0EA8\u0EA9\u0EAC\u0EBA\u0EBE\u0EBF\u0EC5\u0EC7\u0ECE\u0ECF\u0EDA\u0EDB\u0EE0-\u0EFF\u0F48\u0F6D-\u0F70\u0F98\u0FBD\u0FCD\u0FDB-\u0FFF\u10C6\u10C8-\u10CC\u10CE\u10CF\u1249\u124E\u124F\u1257\u1259\u125E\u125F\u1289\u128E\u128F\u12B1\u12B6\u12B7\u12BF\u12C1\u12C6\u12C7\u12D7\u1311\u1316\u1317\u135B\u135C\u137D-\u137F\u139A-\u139F\u13F5-\u13FF\u169D-\u169F\u16F1-\u16FF\u170D\u1715-\u171F\u1737-\u173F\u1754-\u175F\u176D\u1771\u1774-\u177F\u17DE\u17DF\u17EA-\u17EF\u17FA-\u17FF\u180F\u181A-\u181F\u1878-\u187F\u18AB-\u18AF\u18F6-\u18FF\u191D-\u191F\u192C-\u192F\u193C-\u193F\u1941-\u1943\u196E\u196F\u1975-\u197F\u19AC-\u19AF\u19CA-\u19CF\u19DB-\u19DD\u1A1C\u1A1D\u1A5F\u1A7D\u1A7E\u1A8A-\u1A8F\u1A9A-\u1A9F\u1AAE-\u1AFF\u1B4C-\u1B4F\u1B7D-\u1B7F\u1BF4-\u1BFB\u1C38-\u1C3A\u1C4A-\u1C4C\u1C80-\u1CBF\u1CC8-\u1CCF\u1CF7-\u1CFF\u1DE7-\u1DFB\u1F16\u1F17\u1F1E\u1F1F\u1F46\u1F47\u1F4E\u1F4F\u1F58\u1F5A\u1F5C\u1F5E\u1F7E\u1F7F\u1FB5\u1FC5\u1FD4\u1FD5\u1FDC\u1FF0\u1FF1\u1FF5\u1FFF\u200B-\u200F\u202A-\u202E\u2060-\u206F\u2072\u2073\u208F\u209D-\u209F\u20BB-\u20CF\u20F1-\u20FF\u218A-\u218F\u23F4-\u23FF\u2427-\u243F\u244B-\u245F\u2700\u2B4D-\u2B4F\u2B5A-\u2BFF\u2C2F\u2C5F\u2CF4-\u2CF8\u2D26\u2D28-\u2D2C\u2D2E\u2D2F\u2D68-\u2D6E\u2D71-\u2D7E\u2D97-\u2D9F\u2DA7\u2DAF\u2DB7\u2DBF\u2DC7\u2DCF\u2DD7\u2DDF\u2E3C-\u2E7F\u2E9A\u2EF4-\u2EFF\u2FD6-\u2FEF\u2FFC-\u2FFF\u3040\u3097\u3098\u3100-\u3104\u312E-\u3130\u318F\u31BB-\u31BF\u31E4-\u31EF\u321F\u32FF\u4DB6-\u4DBF\u9FCD-\u9FFF\uA48D-\uA48F\uA4C7-\uA4CF\uA62C-\uA63F\uA698-\uA69E\uA6F8-\uA6FF\uA78F\uA794-\uA79F\uA7AB-\uA7F7\uA82C-\uA82F\uA83A-\uA83F\uA878-\uA87F\uA8C5-\uA8CD\uA8DA-\uA8DF\uA8FC-\uA8FF\uA954-\uA95E\uA97D-\uA97F\uA9CE\uA9DA-\uA9DD\uA9E0-\uA9FF\uAA37-\uAA3F\uAA4E\uAA4F\uAA5A\uAA5B\uAA7C-\uAA7F\uAAC3-\uAADA\uAAF7-\uAB00\uAB07\uAB08\uAB0F\uAB10\uAB17-\uAB1F\uAB27\uAB2F-\uABBF\uABEE\uABEF\uABFA-\uABFF\uD7A4-\uD7AF\uD7C7-\uD7CA\uD7FC-\uF8FF\uFA6E\uFA6F\uFADA-\uFAFF\uFB07-\uFB12\uFB18-\uFB1C\uFB37\uFB3D\uFB3F\uFB42\uFB45\uFBC2-\uFBD2\uFD40-\uFD4F\uFD90\uFD91\uFDC8-\uFDEF\uFDFE\uFDFF\uFE1A-\uFE1F\uFE27-\uFE2F\uFE53\uFE67\uFE6C-\uFE6F\uFE75\uFEFD-\uFF00\uFFBF-\uFFC1\uFFC8\uFFC9\uFFD0\uFFD1\uFFD8\uFFD9\uFFDD-\uFFDF\uFFE7\uFFEF-\uFFFB\uFFFE\uFFFF]/g;

		if (oDate.split(" ").length === 1) {
			var iDate = parseInt(oDate);
			var nDate = new Date(iDate);
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "dd-MMM-yy"
			});
			return oDateFormat.format(nDate);
		}
		var aDate = oDate.split(" ");
		var aDateArray = aDate[0].split("-");
		var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
		sDate = sDate;
		var d = new Date(sDate + " UTC");
		//var d = new Date(Date.UTC('2018', '04' - 1, '18', '06', '16', '52'));
		var dArray = d.toDateString().split(" ");
		var date = dArray[2] + "-" + dArray[1] + "-" + (d.getYear() - 100) + " ";
		returnString += date;
		return returnString;
	},
	showDeleteAttachment: function (aStatus, currStatus, isCreate) {
		if ((aStatus === currStatus) || isCreate) {
			return true;
		} else {
			return false;
		}
	},

	getAlarmColor: function (oStatus, oAlarmType) {
		oStatus = oStatus.split("-").pop();
		this.removeStyleClass("iopAlarmColorOrangeClass");
		this.removeStyleClass("iopAlarmColorGreenClass");
		this.removeStyleClass("iopAlarmColorRedClass");
		this.removeStyleClass("iopAlarmColorBlackClass");
		this.removeStyleClass("iopAlarmColorPurpleClass");
		if (!oAlarmType || oAlarmType === "All") {
			if (oStatus === "Frozen") {
				this.addStyleClass("iopAlarmColorGreenClass");
			} else if (oStatus === "Warning") {
				this.addStyleClass("iopAlarmColorOrangeClass");
			} else if (oStatus === "Alarm") {
				this.addStyleClass("iopAlarmColorRedClass");
			} else if (oStatus === "Unreliable") {
				this.addStyleClass("iopAlarmColorPurpleClass");
			} else {
				this.addStyleClass("iopAlarmColorBlackClass");
			}
		}
		return true;
	},

	getAlarmDTColor: function (oStatus, oAlarmType) {
		oStatus = oStatus.split("-").pop();
		this.removeStyleClass("iopAlarmColorOrangeClass");
		this.removeStyleClass("iopAlarmColorGreenClass");
		this.removeStyleClass("iopAlarmColorRedClass");
		this.removeStyleClass("iopAlarmColorBlackClass");
		this.removeStyleClass("iopAlarmColorPurpleClass");
		if (oStatus === "Frozen") {
			this.addStyleClass("iopAlarmColorGreenClass");
		} else if (oStatus === "Warning") {
			this.addStyleClass("iopAlarmColorOrangeClass");
		} else if (oStatus === "Alarm") {
			this.addStyleClass("iopAlarmColorRedClass");
		} else if (oStatus === "Unreliable") {
			this.addStyleClass("iopAlarmColorPurpleClass");
		} else {
			this.addStyleClass("iopAlarmColorBlackClass");
		}
		if (!oAlarmType || oAlarmType === "All") {
			return true;
		}
		return false;
	},

	getDOPAlarmColor: function (oStatus) {
		this.removeStyleClass("iopAlarmColorOrangeClass");
		this.removeStyleClass("iopAlarmColorGreenClass");
		this.removeStyleClass("iopAlarmColorRedClass");
		this.removeStyleClass("iopAlarmColorBlackClass");
		this.removeStyleClass("iopAlarmColorPurpleClass");
		if (oStatus) {
			if (oStatus === "Frozen") {
				this.addStyleClass("iopAlarmColorGreenClass");
			} else if (oStatus === "Warning") {
				this.addStyleClass("iopAlarmColorOrangeClass");
			} else if (oStatus === "Alarm") {
				this.addStyleClass("iopAlarmColorRedClass");
			} else if (oStatus === "Unreliable") {
				this.addStyleClass("iopAlarmColorPurpleClass");
			}
			// Compare the code for Black color
			else if (oStatus === "Normal") {
				this.addStyleClass("iopAlarmColorBlackClass");
			}
		} else {
			// this.addStyleClass("iopAlarmColorBlackClass");
		}
		if (oStatus !== null)
			return true;

		else return false;
	},

	showAlarmIcons: function (oValue) {
		if (oValue === "Y") {
			return true;
		} else if (oValue === "N") {
			return false;
		} else {
			return false;
		}
	},

	wellCheck: function (oValue) {
		if (oValue) {
			return true;
		} else {
			return false;
		}
	},

	alarmClassification: function (oValue) {
		if (oValue === "Downtime") {
			return "DT";
		} else if (oValue === "Non Downtime") {
			return "NDT";
		}
	},

	showDesignateBox: function (oStatus, oDTValue, oMuwi) {
		if (oMuwi && oStatus === "RESOLVED" && oDTValue === "Downtime") {
			this.getModel("dashBoardModel").setProperty("/showDesignateOnTaskDetails", true);
			return true;
		}
		this.getModel("dashBoardModel").setProperty("/showDesignateOnTaskDetails", false);
		return false;
	},

	showInvestigationObservation: function (oStatus, isENG) {
		if (oStatus !== 'NEW' && isENG === false) {
			return true;
		}
		return false;
	},
	/*End of Formatters for Task Panel--Kiruthika*/

	taskSchedulerAppointmentTooltip: function (classification, subClassification, estResolveTime, travelTime, totalEstTime, createdAtDisplay,
		startTimeInString, endTimeInString) {
		if (travelTime === -1) {
			travelTime = "Location Unavailable";
		} else {
			travelTime += " minutes\n";
		}
		return classification + "-" + subClassification + " \nEstimated Resolve Time : " + estResolveTime + " minutes \nTravel Time : " +
			travelTime + "\nTotal Estimated Time : " + totalEstTime + " minutes\n";
		/*"\nCreation Time : " + createdAtDisplay +
		"\n Start Time : " +
		startTimeInString + "\n End Time : " + endTimeInString;*/
	},
	taskSchedulerStartTime: function (startTime) {
		return new Date(startTime);
	},
	taskSchedulerEndTime: function (endTime) {
		return new Date(endTime);
	},
	taskSchedulerTooltip: function (totalWorkTime, availableTime, shift) {
		return "Total Worktime : " + totalWorkTime + " minutes\nAvailable Time : " + availableTime + " minutes\nShift : " + shift;
	},
	taskSchedulerTitle: function (classification, subClassification) {
		return classification + " - " + subClassification;
	},

	taskSchedulerAppointmentColor: function (priority) {
		switch (priority) {
			//remove case 0
		case 0:
			return "#ffe05f";
			break;
		case 1:
			return "#ffe05f";
			break;
		case 2:
			return "#ffe67b";
			break;
		case 3:
			return "#ffe780";
			break;
		case 4:
			return "#ffe98d";
			break;
		case 5:
			return "#ffeb99";
			break;
		case 6:
			return "#ffeda1";
			break;
		case 7:
			return "#ffefae";
			break;
		case 8:
			return "#fff2bb";
			break;
		case 9:
			return "#fff4c5";
			break;
		case 10:
			return "#fff5cc";
			break;
		case 11:
			return "#fff8d9";
			break;
		case 12:
			return "#fffae6";
			break;

		}
	},
	taskSchedulerAppointmentType: function (status, tier) {
		if (status === "RESOLVED" || status === "COMPLETED") {
			if (tier && tier === "Tier A") {
				return "Type01";
			} else if (tier && tier === "Tier B") {
				return "Type02";
			} else if (tier && tier === "Tier C") {
				return "Type03";
			} else {
				return "Type04";
			}
		} else {
			if (tier && tier === "Tier A") {
				return "Type05";
			} else if (tier && tier === "Tier B") {
				return "Type06";
			} else if (tier && tier === "Tier C") {
				return "Type07";
			} else {
				return "Type08";
			}
		}
	},

	getCloseTaskText: function (bValue) {
		if (bValue === true) {
			return "Update & Close Task";
		} else {
			return "Close Task";
		}
	},

	fnGetTheTaskStatusIcon: function (oValue) {
		if (oValue && (oValue === "ASSIGNED" || oValue === "IN PROGRESS")) {
			return jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.images", "/inProgress.png");
		} else {
			return jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.images", "/completed.png");
		}
	},

	getImagePath: function (path) {
		return jQuery.sap.getModulePath("com.sap.incture.Incture_IOP.images", path);
	},

	showActivityLog: function (oInvestigation, oInquiry, oPOT, oTaskList) { //AN: #inquire
		if ((!oInvestigation && !oInquiry) || !oTaskList) {
			return false;
		}
		return true;
	},

	fnShowOrigin: function (origin) {
		var type = origin;
		if (origin === "Investigation") {
			type = "Investigation";
		} else if (origin === "Custom" || origin === "Alarm" || origin === "Variance") {
			type = "Dispatch";
		}
		this.getBindingInfo("text").binding.getContext().getObject().taskType = type;
		//this.getModel("oTaskPanelDetailModel").refresh();
		return type;
	},

	showCreatedDate: function (oStatus, oDate) {
		if (!oDate || oStatus === "ASSIGNED" || oStatus === "IN PROGRESS") {
			return true;
		}
		return false;
	},

	showCompletedDate: function (oStatus, oDate) {
		if (oDate && (oStatus === "COMPLETED" || oStatus === "RESOLVED")) {
			return true;
		}
		return false;
	},

	showSummary: function (oSummary) {
		if (oSummary === "-" || oSummary === null || oSummary === "") {
			return false;
		} else {
			if (oSummary) {
				if (oSummary.trim()) {
					return true;
				}
			}
			return false;
		}
	},
	showCloseBtn: function (isPOT, owners, displayName, status) {

		if (isPOT) {

			if (owners && owners.includes(displayName) && (status === "ASSIGNED" || status === "IN PROGRESS")) {
				return false;
			}

		} else return true;
	},

	showSubmitBtn: function (isENG, isPOT, owners, displayName, taskId, status, isReadOnly, moduleReadOnly, currentSelectTab) {
		//SH: Modules readonly
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (!taskId || isModReadOnly || isReadOnly)
			return false;
		else if (isENG) {
			if (status === "COMPLETED" || status === "RESOLVED") {
				return false;
			} else {
				return true;
			}
		} else if (isPOT && owners) {
			for (var i = 0; i < owners.length; i++) {
				if (owners[i].taskOwnerDisplayName.includes(displayName) && (status === "ASSIGNED" || status === "IN PROGRESS")) {
					return true;
				}
			}
		}
		return false;
	},

	showUpdateBtn: function (isROC, isENG, isPOT, owners, displayName, checkList, status, isReadOnly, moduleReadOnly, currentSelectTab) {
		//SH: Modules readonly
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (!isROC && (checkList && checkList.length > 0) && (status && status != "NEW") && !isModReadOnly && !isReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	showCloseInvestigationBtn: function (isROC, isENG, isPOT, owners, displayName, status, isReadOnly, moduleReadOnly, currentSelectTab,
		hasCanadaRole) {
		//SH: Modules readonly
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (isENG || isROC || isModReadOnly || isReadOnly) {
			return false;
		}
		if (status === "RESOLVED")
			return true;
		if (status === "COMPLETED") {
			return false;
		}

		if ((status !== "NEW" && isENG !== true)) {
			if (isPOT && owners) {
				for (var i = 0; i < owners.length; i++) {
					if (owners[i].taskOwnerDisplayName.includes(displayName) && (status === "ASSIGNED" || status === "IN PROGRESS")) {
						return false;
					}
				}
			} else return true;

		} else return false;

	},
	showEngineerObservation: function (oStatus, oEng) {
		if (oEng && oStatus) {
			if (oStatus == "RESOLVED" || oStatus == "COMPLETED") {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	},
	getHighlightColorforFrac: function (oStatus) {
		this.removeStyleClass("AlarmHighlightColorOrangeClass");
		this.removeStyleClass("AlarmHighlightColorOrangeGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorRedGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorRedClass");

		if (oStatus === "amber") {
			this.addStyleClass("AlarmHighlightColorOrangeClass");
			this.addStyleClass("AlarmHighlightColorOrangeGradeOutClass");
		} else if (oStatus === "red") {
			this.addStyleClass("AlarmHighlightColorRedClass");
			this.addStyleClass("AlarmHighlightColorRedGradeOutClass");
		} else {

			this.addStyleClass("whiteBackground");
		}
		return true;
	},
	/*End of Formatters for Task Panel--Kiruthika*/
	// Safwan changes for Alarm
	getHighlightColor: function (oStatus, oAlarmType) {
		this.removeStyleClass("AlarmHighlightColorOrangeClass");
		this.removeStyleClass("AlarmHighlightColorOrangeGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorGreenClass");
		this.removeStyleClass("AlarmHighlightColorGreenGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorRedClass");
		this.removeStyleClass("AlarmHighlightColorRedGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorPurpleClass");
		this.removeStyleClass("AlarmHighlightColorPurpleGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorBlackClass");
		this.removeStyleClass("AlarmHighlightColorBlackGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorNotifiedClass"); //AN: #Notif
		this.removeStyleClass("AlarmHighlightColorNotifiedGradeOutClass"); //AN: #Notif

		if (oStatus === null) {
			this.addStyleClass("whiteBackground");
			return;
		}
		oStatus = oStatus.split("-").pop();

		if (!oAlarmType || oAlarmType === "All") {
			if (oStatus === "Frozen") {
				this.addStyleClass("AlarmHighlightColorGreenClass");
				this.addStyleClass("AlarmHighlightColorGreenGradeOutClass");
			} else if (oStatus === "Warning") {
				this.addStyleClass("AlarmHighlightColorOrangeClass");
				this.addStyleClass("AlarmHighlightColorOrangeGradeOutClass");
			} else if (oStatus === "Alarm") {
				this.addStyleClass("AlarmHighlightColorRedClass");
				this.addStyleClass("AlarmHighlightColorRedGradeOutClass");
			} else if (oStatus === "Unreliable") {
				this.addStyleClass("AlarmHighlightColorPurpleClass");
				this.addStyleClass("AlarmHighlightColorPurpleGradeOutClass");
			} else {
				this.addStyleClass("AlarmHighlightColorBlackClass");
				this.addStyleClass("AlarmHighlightColorBlackGradeOutClass");
			}
		}
		if (oAlarmType === "notified") { //AN: #Notif
			this.addStyleClass("AlarmHighlightColorNotifiedClass");
			this.addStyleClass("AlarmHighlightColorNotifiedGradeOutClass");
		}

		return true;
	},
	getClassificationIndex: function (oValue) {
		if (oValue === "Downtime") {
			return true;
		} else if (oValue === "Non Downtime") {
			return false;
		} else if (oValue === "" || oValue === null) {
			return -1;
		}
	},
	getDOPEngInvestigation: function (isInvestigation) {
		if (!isInvestigation) {
			return false;
		} else {
			return true;
		}
	},
	/**
	 * Function to decide the visibility of Resolve/Forward button on Inquiry Panel
	 * @public
	 * @param {boolean} isROC - To check if logged in user is an ROC
	 * @param {boolean} isPOT - To check if logged in user is a POT
	 * @param {array} owners - To check if logged in user is also the assignee for the Inquiry task
	 * @param {string} status - To check the status of the Inquiry task
	 * @param {string} loggedInUser - Used for comparing with the owners
	 * @param {boolean} hasDispatchTask - To check if dispatch is created or not
	 * @returns {boolean} - Visibility indicator
	 **/
	showResolveForwardFromInquiryBtn: function (isROC, isPOT, owners, status, loggedInUser, hasDispatchTask, origin, parentOrigin, group,
		isEditable, isReadOnly) { //AN: #inquire
		if (isReadOnly) {
			return false;
		}
		if ((isROC || isPOT) && parentOrigin === "Inquiry" && origin === "Inquiry" && !isEditable) { //AN: #inquiryEnhancement
			return false;
		}
		if ((isROC === true || isPOT === true) && !hasDispatchTask) {
			if (owners) {
				var isAssignee = false;
				for (var i = 0; i < owners.length; i++) {
					if (owners[i].taskOwner === loggedInUser) {
						isAssignee = true;
						break;
					} else {
						isAssignee = false;
					}
				}
				return isAssignee;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Create Investigation button on Inquiry Panel
	 * @public
	 * @param {boolean} isPOT - To check if logged in user is a POT
	 * @param {array} owners - To check if logged in user is also the assignee for the Inquiry task
	 * @param {string} status - To check the status of the Inquiry task
	 * @param {string} loggedInUser - Used for comparing with the owners
	 * @param {boolean} hasDispatchTask - To check if dispatch is created or not
	 * @returns {boolean} - Visibility indicator
	 **/
	showInvestigationFromInquiryBtn: function (isPOT, owners, status, loggedInUser, hasDispatchTask, origin, parentOrigin, group, isEditable,
		isReadOnly) { //AN: #inquire
		if (isReadOnly) {
			return false;
		}
		if (isPOT && parentOrigin === "Inquiry" && origin === "Inquiry" && !isEditable) { //AN: #inquiryEnhancement
			return false;
		}
		if (isPOT === true && !hasDispatchTask) {
			if (owners) {
				var isAssignee = false;
				for (var i = 0; i < owners.length; i++) {
					if (owners[i].taskOwner === loggedInUser) {
						isAssignee = true;
						break;
					} else {
						isAssignee = false;
					}
				}
				return isAssignee;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Dispatch button on Inquiry Panel
	 * @public
	 * @param {boolean} isROC - To check if logged in user is an ROC
	 * @param {array} owners - To check if logged in user is also the assignee for the Inquiry task
	 * @param {string} status - To check the status of the Inquiry task
	 * @param {string} loggedInUser - Used for comparing with the owners
	 * @param {boolean} hasDispatchTask - To check if dispatch is created or not
	 * @returns {boolean} - Visibility indicator
	 **/
	showDispatchFromInquiryBtn: function (isROC, owners, status, loggedInUser, hasDispatchTask, origin, parentOrigin, group, isEditable,
		isReadOnly) { //AN: #inquire
		if (isReadOnly) {
			return false;
		}
		if (isROC && parentOrigin === "Inquiry" && origin === "Inquiry" && !isEditable) { //AN: #inquiryEnhancement
			return false;
		}
		if (isROC === true && !hasDispatchTask) {
			if (owners) {
				var isAssignee = false;
				for (var i = 0; i < owners.length; i++) {
					if (owners[i].taskOwner === loggedInUser) {
						isAssignee = true;
						break;
					} else {
						isAssignee = false;
					}
				}
				return isAssignee;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Close button on Inquiry Panel
	 * @public
	 * @param {string} status - To check the status of the Inquiry task
	 * @param {string} createdByGroup - Group belonging to the creator of the task (used to check it with the logged in user's group)
	 * @param {boolean} hasDispatchTask - To check if dispatch is created or not
	 * @param {string} sGroup - To check the logged in user's group
	 * @returns {boolean} - Visibility indicator
	 **/
	showCloseFromInquiryBtn: function (status, createdByGroup, hasDispatchTask, sGroup, isReadOnly) { //AN: #inquire
		if (isReadOnly) {
			return false;
		}
		var sGroups = sGroup.split(",");
		var isGroup = false;
		if (status !== "NEW" && sGroups && createdByGroup && !hasDispatchTask) {
			for (var g = 0; g < sGroups.length; g++) {
				if (createdByGroup.search(sGroups[g]) >= 0) {
					isGroup = true;
					break;
				} else {
					isGroup = false;
				}
			}
			return isGroup;
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Update button on Inquiry Panel
	 * @public
	 * @param {string} status - To check the status of the Inquiry task
	 * @param {string} createdByGroup - Group belonging to the creator of the task (used to check it with the logged in user's group)
	 * @param {string} sGroup - To check the logged in user's group
	 * @returns {boolean} - Visibility indicator
	 **/
	showUpdateFromInquiryBtn: function (status, createdByGroup, sGroup, isReadOnly) { //AN: #inquire
		if (isReadOnly) {
			return false;
		}
		var sGroups = sGroup.split(",");
		var isGroup = false;
		if ((status === "ASSIGNED" || status === "RESOLVED") && sGroups && createdByGroup) {
			for (var g = 0; g < sGroups.length; g++) {
				if (createdByGroup.search(sGroups[g]) >= 0) {
					isGroup = true;
					break;
				} else {
					isGroup = false;
				}
			}
			return isGroup;
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Close button on task Management Panel's card
	 * @public
	 * @param {string} sGroup - To check the logged in user's group
	 * @param {string} status - To check the status of the task
	 * @param {string} createdByGroup - Group belonging to the creator of the task (used to check it with the logged in user's group)
	 * @param {string} taskType - To check the task type
	 * @param {boolean} hasDispatchTask - To check if dispatch is created or not
	 * @returns {boolean} - Visibility indicator
	 **/
	showCloseFromTaskCardSuggestion: function (sGroup, status, createdByGroup, taskType, hasDispatchTask) {
		var sGroups = sGroup.split(",");
		var isGroup = false;
		if (taskType === "Inquiry" && sGroups && createdByGroup && status !== "NEW" && !hasDispatchTask) {
			for (var g = 0; g < sGroups.length; g++) {
				if (createdByGroup.search(sGroups[g]) >= 0) {
					isGroup = true;
					break;
				} else {
					isGroup = false;
				}
			}
			return isGroup;
		} else {
			return true;
		}
	},
	/**
	 * Function to decide the visibility of Assigned To Label on task Management Panel's card
	 * @public
	 * @param {string} taskOwner - To check the Task Owners' name
	 * @param {string} displayName - To check the logged in user's display Name
	 * @returns {boolean} - Visibility indicator
	 **/
	showTaskCardAssignedTo: function (taskOwner, displayName) {
		if (taskOwner) {
			if (taskOwner.includes(displayName)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Assigned By Label on task Management Panel's card
	 * @public
	 * @param {string} taskOwner - To check the Task Owners' name
	 * @param {string} displayName - To check the logged in user's display Name
	 * @returns {boolean} - Visibility indicator
	 **/
	showTaskCardAssignedBy: function (taskOwner, displayName) {
		if (taskOwner) {
			if (taskOwner.includes(displayName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	},
	// PW Hopper Status text
	getPWHopperStatus: function (status) {
		if (status && status === "#fd5959") { //red
			return "Active";
		} else if (status && status === "#00b500") { //green
			return "Completed";
		} else if (status && status === "#ffa500") { //orange
			return "Inprogress";
		} else if (status && status === "#808080") { //grey
			return "Inactive";
		}
	},
	fnGetPWChecklistText: function (checkListType) {
		if (checkListType && checkListType === "RE") {
			return "Reservoir Engineer";
		} else if (checkListType && checkListType === "WW") {
			return "Well Work Engineer";
		} else {
			return checkListType;
		}
	},
	dispalyStatus: function (oStatus) {
		if (oStatus) {
			if (oStatus === "INVESTIGATION CREATED" || oStatus === "DISPATCH CREATED" || oStatus === "FORWARDED") {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	fracDateTimeDisplayFormat: function (oDate) {
		if (oDate === null) {
			return null;
		} else {
			var dateObj = new Date(oDate);
			var formatted = dateObj.toLocaleString().split("/").join("-");
			return formatted.split(",")[0];
		}

	},
	fnStatusBgColor: function (oValue, oStatus) {
		this.removeStyleClass("iopwbTaskResolvedStatusBgColor");
		this.removeStyleClass("iopwbTaskInProgressStatusBgColor");
		this.removeStyleClass("iopwbTaskDispatchedStatusBgColor");
		this.removeStyleClass("iopwbTaskReturnedStatusBgColor");
		this.removeStyleClass("iopwbTaskSubmittedStatusBgColor");
		this.removeStyleClass("iopwbTaskCompletedStatusBgColor");
		this.removeStyleClass("iopwbTaskNewStatusBgColor");
		this.removeStyleClass("iopwbListReAssignedStatusBgColor");
		this.removeStyleClass("iopwbListCancelledStatusBgColor");
		if (oStatus === "ASSIGNED") {
			this.addStyleClass("iopwbTaskDispatchedStatusBgColor");
		} else if (oStatus === "RESOLVED") {
			this.addStyleClass("iopwbTaskResolvedStatusBgColor");
		} else if (oStatus === "IN PROGRESS") {
			this.addStyleClass("iopwbTaskInProgressStatusBgColor");
		} else if (oStatus === "RETURNED") {
			this.addStyleClass("iopwbTaskReturnedStatusBgColor");
		} else if (oStatus === "SUBMITTED") {
			this.addStyleClass("iopwbTaskSubmittedStatusBgColor");
		} else if (oStatus === "COMPLETED") {
			this.addStyleClass("iopwbTaskCompletedStatusBgColor");
		} else if (oStatus === "NEW" || oStatus === "DRAFT") { //AN: #workbench
			this.addStyleClass("iopwbTaskNewStatusBgColor");
		} else if (oStatus === "CANCELLED") {
			this.addStyleClass("iopwbTaskReturnedStatusBgColor");
		} else {
			this.addStyleClass("iopwbTaskDispatchedStatusBgColor");
		}
		if (oValue === "Non-Dispatch") {
			return false;
		} else {
			return true;
		}
	},
	/**
	 * Function to decide the visibility of Pig Launch button on Dispatch Panel
	 * @public
	 * @param {string} status - To check the status of the task
	 * @param {string} createdByDisplay - To check the logged in user's display name
	 * @param {string} sCustAttr - Dynamically populated value for respective task
	 * @param {string} parentOrigin - Original origin of the task
	 * @returns {boolean} - Visibility indicator
	 **/
	handlePLButton: function (status, createdByDisplay, sCustAttr, parentOrigin, isROC, isReadOnly, moduleReadOnly, currentSelectTab) { //AJ: #Pigging
		//SH: Modules readonly
		var isModReadOnly;
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
		if (isROC && !isModReadOnly && !isReadOnly) { //AN: #ChangeSeat
			if (parentOrigin === "Pigging" && status === "ASSIGNED" && createdByDisplay === "SYSTEM USER") {
				var pigType = false;
				if (sCustAttr) {
					for (var i = 0; i < sCustAttr.length; i++) {
						if (sCustAttr[i].labelValue === "Pigging Launch") {
							pigType = true;
							break;
						}
					}
				}
				return pigType;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Pig Retrieval button on Dispatch Panel
	 * @public
	 * @param {string} status - To check the status of the task
	 * @param {string} createdByDisplay - To check the logged in user's display name
	 * @param {string} sCustAttr - Dynamically populated value for respective task
	 * @param {string} parentOrigin - Original origin of the task
	 * @returns {boolean} - Visibility indicator
	 **/
	handlePRButton: function (status, createdByDisplay, sCustAttr, parentOrigin, isROC, isReadOnly, moduleReadOnly, currentSelectTab) { //AJ: #Pigging
		//SH: Modules readonly
		var isModReadOnly;
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
		if (isROC && !isModReadOnly && !isReadOnly) { //AN: #ChangeSeat
			if (parentOrigin === "Pigging" && status === "RESOLVED" && (createdByDisplay && createdByDisplay !== "SYSTEM USER")) {
				var pigType = false;
				if (sCustAttr) {
					for (var i = 0; i < sCustAttr.length; i++) {
						if (sCustAttr[i].labelValue === "Pigging Launch") {
							pigType = true;
							break;
						}
					}
				}
				return pigType;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Close Pig button on Dispatch Panel
	 * @public
	 * @param {string} status - To check the status of the task
	 * @param {string} createdByDisplay - To check the logged in user's display name
	 * @param {string} sCustAttr - Dynamically populated value for respective task
	 * @param {string} parentOrigin - Original origin of the task
	 * @param {boolean} isROC - To check if Roc is present or not
	 * @returns {boolean} - Visibility indicator
	 **/
	handleClosePigTaskButton: function (status, createdByDisplay, sCustAttr, parentOrigin, isROC, isReadOnly, moduleReadOnly,
		currentSelectTab) { //AJ: #Pigging
		//SH: Modules readonly
		var isModReadOnly;
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
		if (isROC && !isModReadOnly && !isReadOnly) { //AN: #ChangeSeat
			if (parentOrigin === "Pigging" && (createdByDisplay && createdByDisplay !== "SYSTEM USER")) {
				var pigType = false;
				if (sCustAttr) {
					for (var i = 0; i < sCustAttr.length; i++) {
						if ((sCustAttr[i].labelValue === "Pigging Launch" && (status === "RETURNED" || status === "ASSIGNED")) || (sCustAttr[i].labelValue ===
								"Pigging Retrieval" &&
								status === "RESOLVED")) {
							pigType = true;
							break;
						}
					}
				}
				return pigType;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Update button on Dispatch Panel
	 * @public
	 * @param {string} isReturnedTask - To check return status of the task
	 * @param {string} status - To check the status of the task
	 * @param {string} parentOrigin - Original origin of the task
	 * @param {string} createdByDisplay - To check the logged in user's display name
	 * @param {boolean} isROC - To check if ROC is present or not
	 * @returns {boolean} - Visibility indicator
	 **/
	handleUpdateTaskButton: function (isReturnedTask, status, parentOrigin, createdByDisplay, isROC, isReadOnly, moduleReadOnly,
		currentSelectTab) { //AJ: #Pigging
		//SH: Modules readonly
		var isModReadOnly;
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
		if (isROC && !isModReadOnly && !isReadOnly) { //AN: #ChangeSeat
			if (parentOrigin === "Pigging") {
				if ((status === "ASSIGNED" || status === "RETURNED") && (createdByDisplay && createdByDisplay !== "SYSTEM USER")) {
					return true;
				} else {
					return false;
				}
			} else {
				if (isReturnedTask === true || status === "ASSIGNED" || status === "DRAFT") { //AN: #workbenchRaj
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	},
	/**
	 * Function to decide the visibility of Creating Pig Retrieval task button on Dispatch Panel
	 * @public
	 * @param {string} status - To check the status of the task
	 * @param {string} showCreatePRTask - To check the successful call of pig recieve 
	 * @param {string} sCustAttr - Dynamically populated value for respective task
	 * @param {string} parentOrigin - Original origin of the task
	 * @returns {boolean} - Visibility indicator
	 **/
	handleCreateTaskPRButton: function (status, showCreatePRTask, sCustAttr, parentOrigin, isReadOnly, moduleReadOnly, currentSelectTab) { //AJ: #Pigging
		//SH: Modules readonly
		var isModReadOnly;
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

		if (parentOrigin === "Pigging" && status === "NEW" && showCreatePRTask && !isModReadOnly && !isReadOnly) {
			var pigType = false;
			if (sCustAttr) {
				for (var i = 0; i < sCustAttr.length; i++) {
					if (sCustAttr[i].labelValue === "Pigging Retrieval") {
						pigType = true;
						break;
					}
				}
			}
			return pigType;
		} else {
			return false;
		}
	},
	getGainColor: function (oVal) {
		if (oVal) {
			var aArr = oVal.split(".");
			var hrs = parseInt(aArr[0], 10);
			var mins = parseInt(aArr[1], 10);
			if (hrs > 0) {
				return "green";
			} else if (hrs < 0) {
				return "red";
			} else {
				if (mins > 0) {
					return "green";
				} else {
					return "grey";
				}

			}
		}

	},
	getGainStatus: function (oVal) {
		if (oVal) {
			var aArr = oVal.split(".");
			var hrs = parseInt(aArr[0], 10);
			var mins = parseInt(aArr[1], 10);
			if (hrs > 0) {
				return "Gain";
			} else if (hrs < 0) {
				return "Loss";
			} else {
				if (mins > 0) {
					return "Gain";
				} else {
					return "No Gain/Loss";
				}

			}
		}

	},
	formatObxMinHr: function (oVal) {
		if (oVal) {
			var aArr = oVal.split(".");
			return aArr[0] + " hr(s) " + aArr[1] + " mins";
		}
	},
	formatOBXLastRun: function (oDateTime, oUser) {
		if (!oDateTime || !oUser) {
			return "";
		}
		var dateObj = new Date(oDateTime);
		var oDay = dateObj.getDay();
		var oMon = dateObj.getMonth();
		var oDayArr = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
		var oMonArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var hh = dateObj.getHours();
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
		var mm = dateObj.getMinutes();
		if (mm < 10) {
			mm = "0" + mm;
		}
		var ss = dateObj.getSeconds();
		if (ss < 10) {
			ss = "0" + ss;
		}
		var oTxt = "last run by " + oUser + " on " + oDayArr[oDay] + ", " + oMonArr[oMon] + " " + dateObj.getDate() + ", " + dateObj.getFullYear() +
			" " + hh + ":" + mm + ":" + ss + " " + meridian;
		return oTxt;

	},
	getWorkLoadColour: function (oVal) {
		if (!oVal) {
			return "None";
		}
		if (oVal < 80) {
			return "Success";
		}
		if (oVal <= 100) {
			return "Warning";
		}
		if (oVal > 100) {
			return "Error";
		}

	},
	/**
	 * for restriction on dispatch task list.
	 **/
	fnGetStatusBgColorforTaskList: function (oStatus) {
		this.removeStyleClass("iopwbResolvedStatusBgColor");
		this.removeStyleClass("iopwbInProgressStatusBgColor");
		this.removeStyleClass("iopwbDispatchedStatusBgColor");
		this.removeStyleClass("iopwbReturnedStatusBgColor");
		this.removeStyleClass("iopwbSubmittedStatusBgColor");
		this.removeStyleClass("iopwbListReAssignedStatusBgColor");
		this.removeStyleClass("iopwbListCancelledStatusBgColor");
		if (oStatus === "ASSIGNED") {
			this.addStyleClass("iopwbDispatchedStatusBgColor");
		} else if (oStatus === "RESOLVED") {
			this.addStyleClass("iopwbResolvedStatusBgColor");
		} else if (oStatus === "IN PROGRESS") {
			this.addStyleClass("iopwbInProgressStatusBgColor");
		} else if (oStatus === "RETURNED") {
			this.addStyleClass("iopwbReturnedStatusBgColor");
		} else if (oStatus === "SUBMITTED") {
			this.addStyleClass("iopwbSubmittedStatusBgColor");
		} else if (oStatus === "NEW") { //AN: #piggingChange
			this.addStyleClass("iopwbNewStatusBgColor");
		} else if (oStatus === "CANCELLED") {
			this.addStyleClass("iopwbReturnedStatusBgColor");
		} else {
			this.addStyleClass("iopwbDispatchedStatusBgColor");
		}
	},
	fnShowTaskDetailsforTaskList: function (oLatestComment, oCreatedAt) {
		if ((oLatestComment !== undefined) && (oLatestComment === null || oLatestComment === "")) {
			if (!oCreatedAt) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	},
	TaskListWarning: function (oTaskList) {
		if (oTaskList) {
			if (oTaskList.length === 1) {
				return "Task already exists on this location. Please close the below task or proceed with the task creation.";
			} else {
				return "Tasks already exist on this location. Please close the below tasks or proceed with the task creation.";
			}
		}
	},
	TaskListHeading: function (oTaskList) {
		if (oTaskList) {
			if (oTaskList.length === 1) {
				return "Task exists";
			} else {
				return "Tasks exist";
			}
		}
		return "Tasks exist";
	},
	getHighlightColorforFracNotification: function (oStatus) {
		this.removeStyleClass("AlarmHighlightColorOrangeClass");
		this.removeStyleClass("AlarmHighlightColorOrangeGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorRedGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorRedClass");
		this.removeStyleClass("AlarmHighlightColorNotifiedGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorNotifiedClass");
		if (oStatus === "notified") {
			this.addStyleClass("AlarmHighlightColorNotifiedClass");
			this.addStyleClass("AlarmHighlightColorNotifiedGradeOutClass");
		}
		return false;
	},
	TaskListCount: function (oTaskList) {
		if (oTaskList) {
			if (oTaskList.length > 0) {
				return "Number of task(s) : " + oTaskList.length;
			} else {
				return "No task exists";
			}
		}

	},
	procountPreviousComments: function (oComment) {
		if (oComment) {
			var oComList = oComment.split("#$@$#");
			if (oComList.length > 1) {
				oComment = oComList.join("\n--------------------------------------------\n");
			}

		}
		return oComment;
	},
	isPreviousComments: function (oComment) {
		if (oComment) {
			return true;

		}
		return false;
	},
	JSARiskAssessmentRadioButtons: function (oVal) {
		if (oVal === 0) {
			return 1;
		} else {
			return 0;
		}
	},
	JSACheckBox: function (oVal) {
		if (oVal === 1) {
			return true;
		} else {
			return false;
		}
	},
	JSACheckBoxForDescription: function (oVal) {
		if (oVal !== "") {
			return true;
		} else {
			return false;
		}
	},
	/*#RV Location history Formatter*/
	fnLocHisStatus: function (oStatus) {
		this.removeStyleClass("iopwbNewStatusBgColor");
		this.removeStyleClass("iopLocHisResolvedStatusBgColor");
		this.removeStyleClass("iopLocHisInProgressStatusBgColor");
		this.removeStyleClass("iopLocHistoryDispatchedStatusBgColor");
		this.removeStyleClass("iopLocHisReturnedStatusBgColor");
		this.removeStyleClass("iopLocHisSubmittedStatusBgColor");
		this.removeStyleClass("iopwbTaskCompletedStatusBgColor");
		this.removeStyleClass("iopwbListReAssignedStatusBgColor");
		this.removeStyleClass("iopwbListCancelledStatusBgColor");
		if (oStatus === "ASSIGNED") {
			this.addStyleClass("iopLocHistoryDispatchedStatusBgColor");
		} else if (oStatus === "RESOLVED") {
			this.addStyleClass("iopLocHisResolvedStatusBgColor");
		} else if (oStatus === "IN PROGRESS") {
			this.addStyleClass("iopLocHisInProgressStatusBgColor");
		} else if (oStatus === "RETURNED") {
			this.addStyleClass("iopLocHisReturnedStatusBgColor");
		} else if (oStatus === "SUBMITTED") {
			this.addStyleClass("iopLocHisSubmittedStatusBgColor");
		} else if (oStatus === "NEW" || oStatus === "DRAFT") { //AN: #wbBacklog
			this.addStyleClass("iopwbNewStatusBgColor");
		} else if (oStatus === "COMPLETED") {
			this.addStyleClass("iopwbTaskCompletedStatusBgColor");
		} else if (oStatus === "CLOSED") {
			this.addStyleClass("iopwbTaskCompletedStatusBgColor");
		} else if (oStatus === "CANCELLED") {
			this.addStyleClass("iopLocHisReturnedStatusBgColor");
		} else {
			this.addStyleClass("iopLocHistoryDispatchedStatusBgColor");
		}
		return true;
	},
	/*#RV Location history Workorder status Formatter*/
	fnLocHisWorkorderStatus: function (oStatus) {
		this.removeStyleClass("iopwbReleasedStatusBgColor");
		this.removeStyleClass("iopLocHistoryDispatchedStatusBgColor");
		this.removeStyleClass("iopwbSubmittedStatusBgColor");
		this.removeStyleClass("iopwbTaskCompletedStatusBgColor");
		this.removeStyleClass("iopLocHisResolvedStatusBgColor");
		if (oStatus === "CREATED") {
			this.addStyleClass("iopLocHistoryDispatchedStatusBgColor");
		} else if (oStatus === "RELEASED") {
			this.addStyleClass("iopwbReleasedStatusBgColor");
		} else if (oStatus === "TECO") {
			this.addStyleClass("iopLocHisResolvedStatusBgColor");
		} else if (oStatus === "CLOSED") {
			this.addStyleClass("iopwbTaskCompletedStatusBgColor");
		} else {
			this.addStyleClass("iopLocHistoryDispatchedStatusBgColor");
		}
		return true;
	},
	/*#RV Location history Workorder Date Time Formatters Start*/
	iopDateformatformStringDate: function (oVal) {
		var isTime = false;
		if (!oVal) {
			return "";
		} else {
			var oTime = oVal.split(" ").length;
			if (oTime > 1) {
				isTime = true;
			}
		}
		var oLocDate = new Date(oVal + " UTC");
		var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var yyyy = oLocDate.getFullYear().toString();
		var mmm = oMonthArr[oLocDate.getMonth()];
		var dd = oLocDate.getDate();
		if (dd < 10) {
			dd = "0" + dd.toString();
		}
		var dateString = mmm + " " + dd + ", " + yyyy;
		var meridian = "AM";
		var hh = oLocDate.getHours();
		if (hh >= 12) {
			meridian = "PM";
			if (hh > 12) {
				hh = hh - 12;
			}
		}
		if (hh < 10) {
			hh = "0" + hh;
		}
		var mm = oLocDate.getMinutes();
		if (mm < 10) {
			mm = "0" + mm;
		}
		var ss = oLocDate.getSeconds();
		if (ss < 10) {
			ss = "0" + ss;
		}
		var timeString = hh.toString() + ":" + mm.toString() + ":" + ss.toString() + " " + meridian;
		if (isTime) {
			return dateString + " " + timeString;
		} else {
			return dateString + " --:--:-- --";
		}
	},
	iopLongTextDescription: function (oValue) {
		if (oValue.length > 20) {
			return oValue.substring(0, 20) + " ...";
		} else {
			return oValue;
		}
	},
	iopDateFormatTimeStampValueDate: function (oVal) {
		if (!oVal) {
			return "";
		}
		var oValCheck = Number(oVal);
		if (isNaN(oValCheck)) {
			var oLocDate = eval('new ' + oVal.replace(/\//g, ''));
		} else {
			var oLocDate = new Date(oVal);
		}
		var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var yyyy = oLocDate.getFullYear().toString();
		var mmm = oMonthArr[oLocDate.getMonth()];
		var dd = oLocDate.getDate();
		if (dd < 10) {
			dd = "0" + dd.toString();
		}
		var dateString = mmm + " " + dd + ", " + yyyy;
		return dateString;
	},
	iopDateFormatTimeStampValueDateTime: function (oVal) {
		if (!oVal) {
			return "";
		}
		var oLocDate = new Date(oVal);
		var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var yyyy = oLocDate.getFullYear().toString();
		var mmm = oMonthArr[oLocDate.getMonth()];
		var dd = oLocDate.getDate();
		if (dd < 10) {
			dd = "0" + dd.toString();
		}
		var dateString = mmm + " " + dd + ", " + yyyy;
		var meridian = "AM";
		var hh = oLocDate.getHours();
		if (hh >= 12) {
			meridian = "PM";
			if (hh > 12) {
				hh = hh - 12;
			}
		}
		if (hh < 10) {
			hh = "0" + hh;
		}
		var mm = oLocDate.getMinutes();
		if (mm < 10) {
			mm = "0" + mm;
		}
		var ss = oLocDate.getSeconds();
		if (ss < 10) {
			ss = "0" + ss;
		}
		var timeString = hh.toString() + ":" + mm.toString() + ":" + ss.toString() + " " + meridian;
		return dateString + " " + timeString;
	},
	PtwGetTime: function (oValue) {
		if (oValue) {
			var oLocDate = new Date(oValue);
			//var oTime = oValue.substring(11);
			//var hh = oValue.split(":");
			var hh = oLocDate.getHours();
			var mm = oLocDate.getMinutes();
			var ss = oLocDate.getSeconds();
			var meridian = "AM";
			if (hh >= 12) {
				meridian = "PM";
				if (hh > 12) {
					hh = hh - 12;
				}
			}
			if (hh < 10) {
				hh = "0" + hh;
			}
			var oTime = hh + ":" + mm + ":" + ss + " ";
			return oTime + " " + meridian;
		}
	},
	PtwGetDate: function (oVal) {
		/*	if (oValue) {
				var oDate = oValue.split(" ")[0];
				var oDateArr = oDate.split("-");
				var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
				var oMonth = oMonthArr[Number(oDateArr[1])];
				return oMonth + " " + oDateArr[2] + ", " + oDateArr[0];
			}*/

		if (!oVal) {
			return "";
		}
		var oLocDate = new Date(oVal);
		var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var yyyy = oLocDate.getFullYear().toString();
		var mmm = oMonthArr[oLocDate.getMonth()];
		var dd = oLocDate.getDate();
		if (dd < 10) {
			dd = "0" + dd.toString();
		}
		var dateString = mmm + " " + dd + ", " + yyyy;
		return dateString;

	},
	/*#RV Location history Workorder Date Time Formatters End*/
	/*dateFormateForBypassLog: function (oVal) {
		var returnString = "";
		var re =
			/[\0-\x1F\x7F-\x9F\xAD\u0378\u0379\u037F-\u0383\u038B\u038D\u03A2\u0528-\u0530\u0557\u0558\u0560\u0588\u058B-\u058E\u0590\u05C8-\u05CF\u05EB-\u05EF\u05F5-\u0605\u061C\u061D\u06DD\u070E\u070F\u074B\u074C\u07B2-\u07BF\u07FB-\u07FF\u082E\u082F\u083F\u085C\u085D\u085F-\u089F\u08A1\u08AD-\u08E3\u08FF\u0978\u0980\u0984\u098D\u098E\u0991\u0992\u09A9\u09B1\u09B3-\u09B5\u09BA\u09BB\u09C5\u09C6\u09C9\u09CA\u09CF-\u09D6\u09D8-\u09DB\u09DE\u09E4\u09E5\u09FC-\u0A00\u0A04\u0A0B-\u0A0E\u0A11\u0A12\u0A29\u0A31\u0A34\u0A37\u0A3A\u0A3B\u0A3D\u0A43-\u0A46\u0A49\u0A4A\u0A4E-\u0A50\u0A52-\u0A58\u0A5D\u0A5F-\u0A65\u0A76-\u0A80\u0A84\u0A8E\u0A92\u0AA9\u0AB1\u0AB4\u0ABA\u0ABB\u0AC6\u0ACA\u0ACE\u0ACF\u0AD1-\u0ADF\u0AE4\u0AE5\u0AF2-\u0B00\u0B04\u0B0D\u0B0E\u0B11\u0B12\u0B29\u0B31\u0B34\u0B3A\u0B3B\u0B45\u0B46\u0B49\u0B4A\u0B4E-\u0B55\u0B58-\u0B5B\u0B5E\u0B64\u0B65\u0B78-\u0B81\u0B84\u0B8B-\u0B8D\u0B91\u0B96-\u0B98\u0B9B\u0B9D\u0BA0-\u0BA2\u0BA5-\u0BA7\u0BAB-\u0BAD\u0BBA-\u0BBD\u0BC3-\u0BC5\u0BC9\u0BCE\u0BCF\u0BD1-\u0BD6\u0BD8-\u0BE5\u0BFB-\u0C00\u0C04\u0C0D\u0C11\u0C29\u0C34\u0C3A-\u0C3C\u0C45\u0C49\u0C4E-\u0C54\u0C57\u0C5A-\u0C5F\u0C64\u0C65\u0C70-\u0C77\u0C80\u0C81\u0C84\u0C8D\u0C91\u0CA9\u0CB4\u0CBA\u0CBB\u0CC5\u0CC9\u0CCE-\u0CD4\u0CD7-\u0CDD\u0CDF\u0CE4\u0CE5\u0CF0\u0CF3-\u0D01\u0D04\u0D0D\u0D11\u0D3B\u0D3C\u0D45\u0D49\u0D4F-\u0D56\u0D58-\u0D5F\u0D64\u0D65\u0D76-\u0D78\u0D80\u0D81\u0D84\u0D97-\u0D99\u0DB2\u0DBC\u0DBE\u0DBF\u0DC7-\u0DC9\u0DCB-\u0DCE\u0DD5\u0DD7\u0DE0-\u0DF1\u0DF5-\u0E00\u0E3B-\u0E3E\u0E5C-\u0E80\u0E83\u0E85\u0E86\u0E89\u0E8B\u0E8C\u0E8E-\u0E93\u0E98\u0EA0\u0EA4\u0EA6\u0EA8\u0EA9\u0EAC\u0EBA\u0EBE\u0EBF\u0EC5\u0EC7\u0ECE\u0ECF\u0EDA\u0EDB\u0EE0-\u0EFF\u0F48\u0F6D-\u0F70\u0F98\u0FBD\u0FCD\u0FDB-\u0FFF\u10C6\u10C8-\u10CC\u10CE\u10CF\u1249\u124E\u124F\u1257\u1259\u125E\u125F\u1289\u128E\u128F\u12B1\u12B6\u12B7\u12BF\u12C1\u12C6\u12C7\u12D7\u1311\u1316\u1317\u135B\u135C\u137D-\u137F\u139A-\u139F\u13F5-\u13FF\u169D-\u169F\u16F1-\u16FF\u170D\u1715-\u171F\u1737-\u173F\u1754-\u175F\u176D\u1771\u1774-\u177F\u17DE\u17DF\u17EA-\u17EF\u17FA-\u17FF\u180F\u181A-\u181F\u1878-\u187F\u18AB-\u18AF\u18F6-\u18FF\u191D-\u191F\u192C-\u192F\u193C-\u193F\u1941-\u1943\u196E\u196F\u1975-\u197F\u19AC-\u19AF\u19CA-\u19CF\u19DB-\u19DD\u1A1C\u1A1D\u1A5F\u1A7D\u1A7E\u1A8A-\u1A8F\u1A9A-\u1A9F\u1AAE-\u1AFF\u1B4C-\u1B4F\u1B7D-\u1B7F\u1BF4-\u1BFB\u1C38-\u1C3A\u1C4A-\u1C4C\u1C80-\u1CBF\u1CC8-\u1CCF\u1CF7-\u1CFF\u1DE7-\u1DFB\u1F16\u1F17\u1F1E\u1F1F\u1F46\u1F47\u1F4E\u1F4F\u1F58\u1F5A\u1F5C\u1F5E\u1F7E\u1F7F\u1FB5\u1FC5\u1FD4\u1FD5\u1FDC\u1FF0\u1FF1\u1FF5\u1FFF\u200B-\u200F\u202A-\u202E\u2060-\u206F\u2072\u2073\u208F\u209D-\u209F\u20BB-\u20CF\u20F1-\u20FF\u218A-\u218F\u23F4-\u23FF\u2427-\u243F\u244B-\u245F\u2700\u2B4D-\u2B4F\u2B5A-\u2BFF\u2C2F\u2C5F\u2CF4-\u2CF8\u2D26\u2D28-\u2D2C\u2D2E\u2D2F\u2D68-\u2D6E\u2D71-\u2D7E\u2D97-\u2D9F\u2DA7\u2DAF\u2DB7\u2DBF\u2DC7\u2DCF\u2DD7\u2DDF\u2E3C-\u2E7F\u2E9A\u2EF4-\u2EFF\u2FD6-\u2FEF\u2FFC-\u2FFF\u3040\u3097\u3098\u3100-\u3104\u312E-\u3130\u318F\u31BB-\u31BF\u31E4-\u31EF\u321F\u32FF\u4DB6-\u4DBF\u9FCD-\u9FFF\uA48D-\uA48F\uA4C7-\uA4CF\uA62C-\uA63F\uA698-\uA69E\uA6F8-\uA6FF\uA78F\uA794-\uA79F\uA7AB-\uA7F7\uA82C-\uA82F\uA83A-\uA83F\uA878-\uA87F\uA8C5-\uA8CD\uA8DA-\uA8DF\uA8FC-\uA8FF\uA954-\uA95E\uA97D-\uA97F\uA9CE\uA9DA-\uA9DD\uA9E0-\uA9FF\uAA37-\uAA3F\uAA4E\uAA4F\uAA5A\uAA5B\uAA7C-\uAA7F\uAAC3-\uAADA\uAAF7-\uAB00\uAB07\uAB08\uAB0F\uAB10\uAB17-\uAB1F\uAB27\uAB2F-\uABBF\uABEE\uABEF\uABFA-\uABFF\uD7A4-\uD7AF\uD7C7-\uD7CA\uD7FC-\uF8FF\uFA6E\uFA6F\uFADA-\uFAFF\uFB07-\uFB12\uFB18-\uFB1C\uFB37\uFB3D\uFB3F\uFB42\uFB45\uFBC2-\uFBD2\uFD40-\uFD4F\uFD90\uFD91\uFDC8-\uFDEF\uFDFE\uFDFF\uFE1A-\uFE1F\uFE27-\uFE2F\uFE53\uFE67\uFE6C-\uFE6F\uFE75\uFEFD-\uFF00\uFFBF-\uFFC1\uFFC8\uFFC9\uFFD0\uFFD1\uFFD8\uFFD9\uFFDD-\uFFDF\uFFE7\uFFEF-\uFFFB\uFFFE\uFFFF]/g;
		var aDate = oVal.split(" ");
		var aDateArray = aDate[0].split("-");
		var sDate = "20" + aDateArray[2] + "/" + aDateArray[1] + "/" + aDateArray[0];
		sDate = sDate + " " + aDate[1] + " " + aDate[2];
		var d = new Date(sDate + " UTC");
		var dArray = d.toDateString().split(" ");
		var date = dArray[1] + " " + dArray[2] + ", " + d.getFullYear() + " ";
		var timeArray = d.toLocaleString().split(" ");
		if (timeArray[2] === undefined) {
			if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) >= 12) {
				timeArray[2] = "PM";
				if (parseInt(timeArray[1].split(":")[0].replace(re, ''), 10) >= 13) {
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
		returnString += date;
		return returnString;
	},
	dateFormateForWorkorderLog: function (oVal) {
		if (!oVal) {
			return "";
		}
		var oLocDate = new Date(oVal + " UTC" );
		var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var yyyy = oLocDate.getFullYear().toString();
		var mmm = oMonthArr[oLocDate.getMonth()];
		var dd = oLocDate.getDate();
		if (dd < 10) {
			dd = "0" + dd.toString();
		}
		var dateString = mmm + " " + dd + ", " + yyyy;
		var meridian = "AM";
		var hh = oLocDate.getHours();
		if (hh >= 12) {
			meridian = "PM";
			if (hh > 12) {
				hh = hh - 12;
			}
		}
		if (hh < 10) {
			hh = "0" + hh;
		}
		var mm = oLocDate.getMinutes();
		if (mm < 10) {
			mm = "0" + mm;
		}
		var ss = oLocDate.getSeconds();
		if (ss < 10) {
			ss = "0" + ss;
		}
		var timeString = hh.toString() + ":" + mm.toString() + ":" + ss.toString() + " " + meridian;
		return dateString;
	},
	dateTimeFormateForBypassLog: function (oVal) {
		if (!oVal) {
			return "";
		}
		var oLocDate = new Date(oVal  + " UTC" );
		var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var yyyy = oLocDate.getFullYear().toString();
		var mmm = oMonthArr[oLocDate.getMonth()];
		var dd = oLocDate.getDate();
		if (dd < 10) {
			dd = "0" + dd.toString();
		}
		var dateString = mmm + " " + dd + ", " + yyyy;
		var meridian = "AM";
		var hh = oLocDate.getHours();
		if (hh >= 12) {
			meridian = "PM";
			if (hh > 12) {
				hh = hh - 12;
			}
		}
		if (hh < 10) {
			hh = "0" + hh;
		}
		var mm = oLocDate.getMinutes();
		if (mm < 10) {
			mm = "0" + mm;
		}
		var ss = oLocDate.getSeconds();
		if (ss < 10) {
			ss = "0" + ss;
		}
		var timeString = hh.toString() + ":" + mm.toString() + ":" + ss.toString() + " " + meridian;
		return dateString + " " + timeString;
	},*/

	iopdateFormate: function (oVal) {
		var oDateFormate = new Date(oVal);
		var oDate = oDateFormate.toDateString().split(" ");

		var oDateString = oDate[1] + " " + oDate[2] + ", " + oDate[3];
		return oDateString;
	},

	/*	PtwGetDate: function (oValue) {
		if (oValue) {
			var oDate = oValue.split(" ")[0];
			var oDateArr = oDate.split("-");
			var oMonthArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
			var oMonth = oMonthArr[Number(oDateArr[1])];
			return oMonth + " " + oDateArr[2] + ", " + oDateArr[0];
		}
	},
	PtwGetTime: function (oValue) {
		if (oValue) {
			return oValue.substring(11);
		}
	},
	PtwgetDateFromTimeStamp: function (oValue) {
		var TimeStamp = parseInt(oValue.substring(6, oValue.length - 2));
		var oDateFormate = new Date(TimeStamp);
		var oDate = oDateFormate.toDateString().split(" ");
		var oHour = oDateFormate.getHours();
		var isDay = "";
		if (oHour > 12) {
			oHour = oHour - 12;
			isDay = "PM";
		} else {
			isDay = "AM";
		}
		var oMin = oDateFormate.getMinutes();
		var oSec = oDateFormate.getSeconds();
		if (oMin < 10) {
			oMin = "0" + oMin;
		}
		if (oSec < 10) {
			oSec = "0" + oSec;
		}
		var oDateString = oDate[1] + " " + oDate[2] + ", " + oDate[3] + " " + oHour + ":" + oMin + ":" + oSec + " " + isDay;
		return oDateString;
	},*/

	PtwJSAPermitType: function (hasCWP, hasHWP, hasCSP) {
		var text = "";
		if (hasCWP === 0 && hasHWP === 0 && hasCSP === 0) {

			return " ";
		}
		if (hasCWP === 1) {
			text = text + "CWP";
		}
		if (hasHWP === 1) {
			text = text + "HWP";
		}
		if (hasCSP === 1) {
			text = text + "CSP";
		}
		if (text.length === 6) {
			return text.slice(0, 3) + ", " + text.slice(3, 6);
		} else if (text.length === 9) {
			return text.slice(0, 3) + ", " + text.slice(3, 6) + ", " + text.slice(6, 9);
		} else {
			return text;
		}

	},
	ptwTableVisible: function (oEvent) {
		if (oEvent) {
			if (oEvent.length > 0) {
				return false;
			} else {
				return true;
			}
		}
	},
	locHisBypasslogApproverStatus: function (oStatus) {
		this.removeStyleClass("iopTextApprovedStatusFormat");
		this.removeStyleClass("iopTextPendingStatusFormat");
		this.removeStyleClass("iopTextDeniedStatusFormat");
		if (oStatus === "approved") {
			this.addStyleClass("iopTextApprovedStatusFormat");
		}
		if (oStatus === "pending") {
			this.addStyleClass("iopTextPendingStatusFormat");
		}
		if (oStatus === "denied") {
			this.addStyleClass("iopTextDeniedStatusFormat");
		}
		return true;
	},
	EnergyIsolationgetTime: function (TimeStamp) {
		if (!TimeStamp) {
			return "";
		}
		var oDateFormate = new Date(TimeStamp);
		var oHour = oDateFormate.getHours();
		var isDay = "";
		if (oHour > 12) {
			oHour = oHour - 12;
			isDay = "PM";
		} else {
			isDay = "AM";
		}
		var oMin = oDateFormate.getMinutes();
		var oSec = oDateFormate.getSeconds();
		if (oMin < 10) {
			oMin = "0" + oMin;
		}
		if (oSec < 10) {
			oSec = "0" + oSec;
		}
		var oDateString = oHour + ":" + oMin + ":" + oSec + " " + isDay;
		return oDateString;
	},
	//#RV:location history Hyphen In case of empty value
	checkForEmptyValue: function (oVal) {
		if (!oVal) {
			return "- - ";
		} else {
			return oVal;
		}
	},
	//#RV:location history Hyphen In case of empty value
	checkForEmptyValueFlare: function (oVal) {
		if (!oVal) {
			return "- -";
		} else {
			return oVal.toFixed(2);
		}
	},
	//#RV:location history Hyphen In case of empty value
	checkForEmptyValueFlare: function (oVal) {
		if (!oVal) {
			return "- -";
		} else {
			return oVal.toFixed(2);
		}
	},
	//#RV:location history Formatter for turn around values
	turnAroundFormatter: function (oVal) {
		if (!oVal) {
			return "- -";
		}
		this.removeStyleClass("iopLocHistoryPositiveVarianceText");
		this.removeStyleClass("iopLocHistoryNegVarianceText");
		var oValue = oVal.split(" ");
		if (oValue.length > 0) {
			if (oValue.length === 4) {
				var oMin = (Number(oValue[0]) * 60) + (Number(oValue[2]));
			} else if (oValue.length === 2) {
				var oMin = Number(oValue[0]);
			} else {
				return;
			}
			if (oMin > 0) {
				this.addStyleClass("iopLocHistoryPositiveVarianceText");
			} else if (oMin < 0) {
				this.addStyleClass("iopLocHistoryNegVarianceText");
			}
		}
		return oVal;
	},
	/** 
	 * Formatter to change the background color of the status
	 * @param {String} oStatus - Takes Status as an input
	 * @returns {String} oStatus - Returns the status
	 */
	fnTaskNotifStatus: function (oStatus) { //AN: #Notif
		this.removeStyleClass("iopNotifTaskNewStatusBgColor");
		this.removeStyleClass("iopNotifTaskResolvedStatusBgColor");
		this.removeStyleClass("iopNotifTaskInProgressStatusBgColor");
		this.removeStyleClass("iopNotifTaskAssignedStatusBgColor");
		this.removeStyleClass("iopNotifTaskReturnedStatusBgColor");
		this.removeStyleClass("iopNotifTaskSubmittedStatusBgColor");
		this.removeStyleClass("iopNotifTaskCompletedStatusBgColor");
		if (oStatus === "ASSIGNED") {
			this.addStyleClass("iopNotifTaskAssignedStatusBgColor");
		} else if (oStatus === "RESOLVED") {
			this.addStyleClass("iopNotifTaskResolvedStatusBgColor");
		} else if (oStatus === "IN PROGRESS") {
			this.addStyleClass("iopNotifTaskInProgressStatusBgColor");
		} else if (oStatus === "RETURNED") {
			this.addStyleClass("iopNotifTaskReturnedStatusBgColor");
		} else if (oStatus === "SUBMITTED") {
			this.addStyleClass("iopNotifTaskSubmittedStatusBgColor");
		} else if (oStatus === "NEW") {
			this.addStyleClass("iopNotifTaskNewStatusBgColor");
		} else if (oStatus === "COMPLETED") {
			this.addStyleClass("iopNotifTaskCompletedStatusBgColor");
		} else {
			this.addStyleClass("iopNotifTaskAssignedStatusBgColor");
		}
		return oStatus;
	},
	/** 
	 * Formatter to return visibility of No New Notifications text
	 * @param {Array} oData - contains notifications data
	 * @return {boolean} - returns true or false
	 */
	fnNotifTotalCountLabel: function (oData) { //AN: #Notif
		if (oData) {
			if (parseInt(oData.notifTotalCount, 10) > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	},
	/** 
	 * Formatter to return visibility of bubbles in task panel
	 * @param {Object} oUserData - contains logged in user's data
	 * @param {Array} oNotifDto - contains notification dto (Task)
	 * @return {boolean} - returns true or false
	 */
	fnNotifListItem: function (oUserData, oNotifDto) { //AN: #Notif
		if (oNotifDto) {
			if (oNotifDto.taskCount && parseInt(oNotifDto.taskCount, 10) > 0) {
				return true;
			} else if (oUserData.isROC && oNotifDto.alarmCount && parseInt(oNotifDto.alarmCount, 10) > 0) {
				return true;
			} else if ((oUserData.isENG || oUserData.isROC) && oNotifDto.fracCount && parseInt(oNotifDto.fracCount, 10) > 0) {
				return true;
			} else if ((oUserData.isENG || oUserData.isPOT) && oNotifDto.hopperCount && parseInt(oNotifDto.hopperCount, 10) > 0) {
				return true;
			} else if (oUserData.isROC && oNotifDto.byPassLogCount && parseInt(oNotifDto.byPassLogCount, 10) > 0) {
				return true;
			} else if (oUserData.isROC && oNotifDto.energyIsolationCount && parseInt(oNotifDto.energyIsolationCount, 10) > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	/** 
	 * Formatter to return visibility of bubbles in task panel
	 * @param {Array} oNotifDto - contains notification dto (Task)
	 * @return {boolean} - returns true or false
	 */
	fnNotifBubble: function (oNotifDto) { //AN: #Notif
		if (oNotifDto) {
			if (oNotifDto.taskCount && parseInt(oNotifDto.taskCount, 10) > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	EnergyIsolationEnergyTypeCheckbox: function (oValue) {
		if (oValue === "Electrical" || oValue === "Mechanical" || oValue === "Instrumentation" || oValue === "Pressure") {
			return false;
		} else {
			return true;
		}
	},
	/** 
	 * 
	 * @param {Boolean} oStatus - notification boolean value for hopper
	 * @returns {Boolean} - false
	 */
	getHighlightColorforPwHopperNotification: function (oStatus) {
		this.removeStyleClass("AlarmHighlightColorNotifiedGradeOutClass");
		this.removeStyleClass("AlarmHighlightColorNotifiedClass");
		if (oStatus === "notified") {
			this.addStyleClass("AlarmHighlightColorNotifiedClass");
			this.addStyleClass("AlarmHighlightColorNotifiedGradeOutClass");
		}
		return false;
	},
	PtwTextDecode: function (utf8String) {
		var x = utf8String;
		var r = /\\u([\d\w]{4})/gi;
		if (x) {
			x = x.replace(r, function (match, grp) {
				return String.fromCharCode(parseInt(grp, 16));
			});
			x = unescape(x);
		}
		return x;
	},
	/** 
	 * Formatter to return visibility of bubbles for Shift Handover
	 * @param {Array} aBypassList - contains notification dto (Task)
	 * @return {boolean} - returns true or false
	 */
	fnShiftHandoverCount: function (aBypassList) { //AN: #ShiftHandover
		if (aBypassList) {
			if (aBypassList.length > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	iopShiftRegisterNumberText: function (sSource) { //AN: #ShiftHandover
		if (sSource && sSource === "Bypass Log") {
			return "Bypass Number";
		} else if (sSource && sSource === "Energy Isolation") {
			return "Energy Isolation Number";
		} else {
			return "";
		}

	},
	iopShiftRegisterReasonText: function (sSource) { //AN: #ShiftHandover
		if (sSource && sSource === "Bypass Log") {
			return "Reason for Bypass";
		} else if (sSource && sSource === "Energy Isolation") {
			return "Reason for Isolation";
		} else {
			return "";
		}

	},
	getHighlightColorforShiftHandoverNotification: function (oStatus) { //AN: #highlightShift
		this.removeStyleClass("iopShiftHandoverHighlight");
		if (oStatus === "notified") {
			this.addStyleClass("iopShiftHandoverHighlight");
		}
		return false;
	},
	getHighlightColorforTasksNotification: function (oStatus) { //AN: #highlightShift
		this.removeStyleClass("iopShiftHandoverHighlight");
		if (oStatus === "notified") {
			this.addStyleClass("iopShiftHandoverHighlight");
		}
		return false;
	},
	fnAlarmsNotifTitle: function (facilityDesc, locationText) {
		var title = "";
		if (facilityDesc) {
			title = "Facility Description: " + facilityDesc;
		} else {
			title = "Location: " + locationText;
		}
		return title;
	},
	fnAlarmsNotifDesc: function (facilityDesc, longDesc) {
		var desc = "";
		if (facilityDesc) {
			desc = "Description: " + longDesc;
		}
		return desc;
	},
	ByPasslogDeatilPageHeader: function (bypassNumber, byPassLogPersonRespEditable) {
		// if (byPassLogPersonRespEditable) {
		// 	return "Bypass Log- " + bypassNumber;
		// } else {
		// 	return "Review Bypass Log- " + bypassNumber;
		// }
		return "Bypass Log- " + bypassNumber;
	},
	fnBypassLofActivityLogNameTitleCase: function (sOperatorType) {
		var opType = "";
		if (sOperatorType) {
			opType = sOperatorType.charAt(0).toUpperCase() + sOperatorType.slice(1);
		}
		return opType;
	},
	locHistoryDuration: function (oHour, oMin) {
		if (!oHour && !oMin) {
			return "- -";
		} else {
			return oHour + " hr " + oMin + " min";
		}

	},
	handleDispCloseButton: function (isCreateTask, parentOrigin, isROC, sStatus, isReadOnly, moduleReadOnly, currentSelectTab) { //AN: #changeSeat
		//SH: Modules readonly
		var isModReadOnly;
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

		if (isROC && !isModReadOnly && !isReadOnly) {
			if (parentOrigin !== "Pigging" && !isCreateTask && sStatus !== "DRAFT") { //AN: #workbenchRaj
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	},
	iopWBTierLocationType: function (locationType, tier) { //AN: #workbench
		var sReturnString = "";
		if (locationType && locationType === "Well") {
			sReturnString = tier;
		} else {
			sReturnString = locationType;
		}
		return sReturnString;
	},
	iopWBCustListItemActive: function (sTaskType) { //AN: #workbenchRaj
		if (sTaskType && sTaskType === "Dispatch") {
			return "Active"; //UNCOMMENT LATER
			//return "Inactive"; //DELETE LATER
		} else {
			return "Inactive";
		}
	},
	fnFieldAvail: function (nFieldAvail) { //AN: #OpAvail
		var sValueuColor = "Neutral";
		if (nFieldAvail) {
			if (nFieldAvail && nFieldAvail >= 80) {
				sValueuColor = "Error";
			} else if (nFieldAvail && nFieldAvail < 80 && nFieldAvail >= 20) {
				sValueuColor = "Critical";
			} else if (nFieldAvail && nFieldAvail < 20) {
				sValueuColor = "Good";
			}
		}
		return sValueuColor;
	},
	fnOpAvail: function (nOpAvail) { //AN: #OpAvail
		var sState = "None";
		if (nOpAvail) {
			if (nOpAvail && nOpAvail >= 80) {
				sState = "Error";
			} else if (nOpAvail && nOpAvail < 80 && nOpAvail >= 20) {
				sState = "Warning";
			} else if (nOpAvail && nOpAvail < 20) {
				sState = "Success";
			}
		}
		return sState;
	},
	fnGetNotifStatusColor: function (oStatus) {
		this.removeStyleClass("iopwbResolvedStatusBgColor");
		this.removeStyleClass("iopwbInProgressStatusBgColor");
		this.removeStyleClass("iopwbDispatchedStatusBgColor");
		this.removeStyleClass("iopwbReturnedStatusBgColor");
		this.removeStyleClass("iopwbSubmittedStatusBgColor");
		this.removeStyleClass("iopwbListReAssignedStatusBgColor");
		this.removeStyleClass("iopwbListCancelledStatusBgColor");
		if (oStatus === "ASSIGNED") {
			this.addStyleClass("iopwbDispatchedStatusBgColor");
		} else if (oStatus === "RESOLVED") {
			this.addStyleClass("iopwbResolvedStatusBgColor");
		} else if (oStatus === "IN PROGRESS") {
			this.addStyleClass("iopwbInProgressStatusBgColor");
		} else if (oStatus === "RETURNED") {
			this.addStyleClass("iopwbReturnedStatusBgColor");
		} else if (oStatus === "SUBMITTED") {
			this.addStyleClass("iopwbSubmittedStatusBgColor");
		} else if (oStatus === "NEW") { //AN: #piggingChange
			this.addStyleClass("iopwbNewStatusBgColor");
		} else if (oStatus === "CANCELLED") {
			this.addStyleClass("iopwbReturnedStatusBgColor");
		} else {
			this.addStyleClass("iopwbDispatchedStatusBgColor");
		}
	},
	setAlaramListHeight: function (alarmTableHeight, prograssBarVisible) {
		if (prograssBarVisible) {
			alarmTableHeight = alarmTableHeight.substring(0, alarmTableHeight.length - 2);
			alarmTableHeight = parseInt(alarmTableHeight) - (parseInt(alarmTableHeight) * 0.14);
			/*var prograssBarHeight = this.getParent().$().height() - parseInt(alarmTableHeight);*/
			//	alarmTableHeight =  parseInt(alarmTableHeight) - prograssBarHeight;
			return alarmTableHeight + "px";
		} else {
			return alarmTableHeight;
		}
	},
	fnLocHisTaskDetailStatus: function (oStatus) {
		var statusVal = oStatus;
		this.removeStyleClass("iopwbHighlightChangedClass");
		this.removeStyleClass("iopwbNewStatusColor");
		this.removeStyleClass("iopwbDispatchedStatusColor");
		this.removeStyleClass("iopwbInProgressStatusColor");
		this.removeStyleClass("iopwbResolvedStatusColor");
		this.removeStyleClass("iopwbReturnedStatusColor");
		if (statusVal === "NEW") {
			this.addStyleClass("iopwbNewStatusColor");
		} else if (statusVal === "ASSIGNED") {
			this.addStyleClass("iopwbDispatchedStatusColor");
		} else if (statusVal === "IN PROGRESS") {
			this.addStyleClass("iopwbInProgressStatusColor");
		} else if (statusVal === "RESOLVED") {
			this.addStyleClass("iopwbResolvedStatusColor");
		} else if (statusVal === "RETURNED") {
			this.addStyleClass("iopwbReturnedStatusColor");
		} else if (statusVal === "DRAFT") {
			this.addStyleClass("iopwbNewStatusColor");
		} else if (statusVal === "CANCELLED") {
			this.addStyleClass("iopwbReturnedStatusColor");
		} else if (oStatus === "COMPLETED") {
			this.addStyleClass("iopwbCompletedStatusColor");
		} else {
			this.addStyleClass("iopwbListDispatchedStatusBgColor");
		}
		return true;
	},
	// for epoch to date conversion
	epochToDateTime: function (epochString) {
		if (typeof epochString === "string") {
			var epochVal = parseInt(epochString);
		} else {
			epochVal = epochString;
		}
		var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
			pattern: "dd-MMM-yy hh:mm:ss aaa"
		});
		var convDate = new Date(epochVal);
		return oDateFormat.format(convDate);

	},

	epochToDateProve: function (epochVal) {
		if (epochVal === null || epochVal === "-") {
			return "-";
		}
		var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
			pattern: "dd-MMM-yy"
		});
		if (typeof epochVal === "string") {
			epochVal = parseInt(epochVal);
		}
		var convDate = new Date(epochVal);
		return dateFormat.format(convDate);
	},

	epochToDateDOP: function (epochVal) {
		if (epochVal !== null && epochVal) {
			if (typeof epochVal === "string") {
				epochVal = parseInt(epochVal);
			}
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "dd-MMM-yy hh:mm:ss aaa"
			});
			var convDate = new Date(epochVal);
			return oDateFormat.format(convDate);
		} else {
			return epochVal;
		}
	},

	epochToDateLocHistory: function (epochVal) {
		if (epochVal !== null) {
			if (typeof epochVal === "string") {
				epochVal = parseInt(epochVal);
			}
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "MMM dd, yyyy hh:mm:ss aaa"
			});
			var convDate = new Date(epochVal);
			return oDateFormat.format(convDate);
		} else {
			return epochVal;
		}
	},
	epochToDateLocHistoryDowntime: function (epochVal) {
		if (epochVal !== null) {
			if (typeof epochVal === "string") {
				epochVal = parseInt(epochVal);
			}
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "MMM dd, yyyy hh:mm:ss aaa"
			});
			var convDate = new Date(epochVal);
			return oDateFormat.format(convDate);
		} else {
			return epochVal;
		}
	},
	epochToDateEnergyIsolation: function (epochVal) {
		if (epochVal !== null) {
			if (typeof epochVal === "string") {
				epochVal = parseInt(epochVal);
			}
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "MMM dd, yyyy"
			});
			var convDate = new Date(epochVal);
			return oDateFormat.format(convDate);
		} else {
			return epochVal;
		}
	},
	epochToTimeEnergyIsolation: function (epochVal) {
		if (epochVal !== null) {
			if (typeof epochVal === "string") {
				epochVal = parseInt(epochVal);
			}
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "hh:mm:ss aaa"
			});
			var convDate = new Date(epochVal);
			return oDateFormat.format(convDate);
		} else {
			return epochVal;
		}
	},
	plotlyDateConv: function (epochVal) {
		if (epochVal) {
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "MMM dd, yyyy hh:mm:ss aaa"
			});
			return oDateFormat.format(new Date(Number(epochVal)));
		}
	},
	plotlyDateConv2: function (epochVal) {
		if (epochVal) {
			var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
				pattern: "MMM dd, yyyy"
			});
			return oDateFormat.format(new Date(Number(epochVal)));
		}
	},
	plotlyButtonType: function (duration) {
		if (duration == 3 && this.getText() == "3 D") {
			return "Emphasized";
		} else if (duration == 7 && this.getText() == "7 D") {
			return "Emphasized";
		} else if (duration == 30 && this.getText() == "30 D") {
			return "Emphasized";
		} else {
			return "Default";
		}
	},
	//SH: Modules readonly - Begin
	handleCreateTaskButton: function (isCreateTask, parentOrigin, isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		if (isCreateTask && parentOrigin !== 'Pigging' && !isReadOnly && !isModReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	handleVisForModuleReadonly: function (isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		if (!isReadOnly && !isModReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	handleTextAreaEnbaleForModuleReadonly: function (status, isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		if ((status === "NEW" || status === "ASSIGNED" || status === "RETURNED" || status === "DRAFT") && !isReadOnly && !isModReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	handleTableModeforModReadonly: function (isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		if (!isReadOnly && !isModReadOnly) {
			return "MultiSelect";
		} else {
			return "None";
		}
	},
	handleCreateTaskVisible: function (isROC, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		if (isROC && !isModReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	handleFollowUpButton: function (status, isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		if (status === "RESOLVED" && !isModReadOnly && !isReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	manageTaskManagementPanelForModReadOnly: function (isReadOnly, moduleReadOnly, currentSelectTab, country) {
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (isReadOnly && country === "CA") {
			return false;
		}
		// if (!isModReadOnly) {
		// 	return true;
		// } else {
		// 	return false;
		// }
		return true;
	},
	manageTMPanelWidthForModReadOnly: function (isReadOnly, moduleReadOnly, currentSelectTab, country) {
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (isReadOnly && country === "CA") {
			return "0%";
		}
		if (!isModReadOnly) {
			return "5%";
		} else {
			return "0%";
		}
	},
	handleCreateInvestigationButton: function (status, isENG, isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (!isReadOnly && !isModReadOnly && status === "NEW" && !isENG) {
			return true;
		} else {
			return false;
		}
	},
	handleAssignButton: function (status, isPOT, isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if ((status === "RESOLVED" || status === "ASSIGNED") && !isReadOnly && !isModReadOnly && isPOT) {
			return true;
		} else {
			return false;
		}
	},
	handleRemovePWHopperButton: function (buttonState, isPOT, isReadOnly, moduleReadOnly, currentSelectTab) {
		var isModReadOnly;
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (buttonState && isPOT && !isReadOnly && !isModReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	//SH: Modules readonly - End

	handleScrollHeight: function (HierarchyPanelOpen, downtimePanelExpanded, isWebReadOnlyRole, isDowntimeReadOnly) {
		if (!HierarchyPanelOpen) {
			if ((!downtimePanelExpanded || isWebReadOnlyRole || isDowntimeReadOnly)) {
				return "calc(100vh - 35rem)";
			} else if (downtimePanelExpanded && !isWebReadOnlyRole && !isDowntimeReadOnly) {
				return "calc(100vh - 44rem)";
			}
		} else {
			if ((!downtimePanelExpanded || isWebReadOnlyRole || isDowntimeReadOnly)) {
				return "calc(100vh - 22rem)";
			} else if (downtimePanelExpanded && !isWebReadOnlyRole && !isDowntimeReadOnly) {
				return "calc(100vh - 31rem)";
			}
		}
	},
	fnWbStatus: function (oStatus) { //AN: #msgToROC
		this.removeStyleClass("iopWBNewStatus");
		this.removeStyleClass("iopWBAssignedStatus");
		this.removeStyleClass("iopWBInProgressStatus");
		this.removeStyleClass("iopWBResolvedStatus");
		if (oStatus.toLowerCase() === "assigned") {
			this.addStyleClass("iopWBAssignedStatus");
		} else if (oStatus.toLowerCase() === "new" || oStatus.toLowerCase() === "draft") {
			this.addStyleClass("iopWBNewStatus");
		} else if (oStatus.toLowerCase() === "in progress") {
			this.addStyleClass("iopWBInProgressStatus");
		} else if (oStatus.toLowerCase() === "resolved") {
			this.addStyleClass("iopWBResolvedStatus");
		}
		return true;
	},
	getHighlightColorforHandoverNote: function (isNewNote) { //AN: #highlightShift
		this.removeStyleClass("iopHandoverNotesHighlight");
		if (isNewNote) {
			this.addStyleClass("iopHandoverNotesHighlight");
		}
		return false;
	},
	handleHandoverNoteEdit: function (enableEdit) {
		this.removeStyleClass("textEditorReadOnly");
		if (enableEdit) {
			this.removeStyleClass("textEditorReadOnly");
			return true;
		} else {
			this.addStyleClass("textEditorReadOnly");
			return false;
		}
	},
	fnWbMsgLocTextVisibility: function (sLoc) { //AN: #msgToROC
		if (sLoc) {
			return true;
		} else {
			return false;
		}
	},
	fnWbMsgLocTextlblMndt: function (sLoc) { //AN: #msgToROC
		if (sLoc) {
			return false;
		} else {
			return true;
		}
	},
	iopWbMsgAssignedTo: function (sOwner, isROC, isPOT, sDispName) { //AN: #msgToROC
		if (sOwner && sOwner.includes("@")) {
			return sDispName;
		} else if (isROC) {
			return "ROC";
		} else {
			return "POT";
		}
	},
	epochToDateForHandoverNote: function (oDate) {
		/*var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
			pattern: "dd-MMM-yy"
		});
		return (oDateFormat.format(new Date(Number(oDate))));*/
		var month;
		var dateSplit = oDate.split("-");
		var day = dateSplit[2];
		var year = dateSplit[0];
		switch (dateSplit[1]) {
		case "01":
			month = "Jan";
			break;
		case "02":
			month = "Feb";
			break;
		case "03":
			month = "Mar";
			break;
		case "04":
			month = "Apr";
			break;
		case "05":
			month = "May";
			break;
		case "06":
			month = "Jun";
			break;
		case "07":
			month = "Jul";
			break;
		case "08":
			month = "Aug";
			break;
		case "09":
			month = "Sep";
			break;
		case "10":
			month = "Oct";
			break;
		case "11":
			month = "Nov";
			break;
		case "12":
			month = "Dec";
			break;
		}
		return day + "-" + month + "-" + year.split('')[2] + year.split('')[3];
	},
	//SH: New fields for Canada
	handleByPassLogFieldVisibility: function (locCode) {
		if (locCode !== undefined) {
			if (locCode.startsWith("MUR-CA")) {
				return true;
			} else {
				return false;
			}
		}
	},
	iopWbMsgDescText: function (sMessage) { //AN: #msgToROC
		var sMessageEscaped = $('<textarea />').html(sMessage).text();
		sMessageEscaped = sMessageEscaped.replace("<at>IOP</at>", "");
		return sMessageEscaped.trim();
	},
	handlePWHopperSwitchForReadOnly: function (buttonEnable, isWebReadOnlyRole, moduleReadOnly, currentSelectKey) {
		var isModReadOnly;
		switch (currentSelectKey) {
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
		case "analytics":
			isModReadOnly = moduleReadOnly.isAnalyticsReadOnly;
			break;
		case "locHistory":
			isModReadOnly = moduleReadOnly.isLocationHistoryReadOnly;
			break;
		}
		if (buttonEnable && !isWebReadOnlyRole && !isModReadOnly) {
			return true;
		} else {
			return false;
		}
	},
	removeDecimal: function (iValue) {
		var sValue = iValue;
		if (iValue) {
			sValue = iValue.toFixed(0);
		}
		return sValue;
	},
	iopWBShowReadMore: function (oSummary) {
		var bFlag = true;
		var $element = $(this.getParent().getItems()[0].getTextDomRef());
		// var $c = $element
		// 	.clone()
		// 	.css({
		// 		display: 'inline',
		// 		width: 'auto',
		// 		visibility: 'hidden'
		// 	})
		// 	.appendTo('body');

		// if ($c.width() > $element.width()) {
		// 	// text was truncated. 
		// 	bFlag = true;
		// } else {
		// 	bFlag = false;
		// }
		if ($element[0] && $element.width() < $element[0].scrollWidth) {
			// text was truncated. 
			bFlag = true;
		} else {
			bFlag = false;
		}

		// $c.remove();
		return bFlag;
	},
	// for epoch to date conversion
	epochToDateTimeRevised: function (epochString, timeZone) { //AN: #TimeZone 
		if (typeof epochString === "string") {
			var epochVal = parseInt(epochString);
		} else {
			epochVal = epochString;
		}
		var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({
			pattern: "dd-MMM-yy hh:mm:ss aaa"
		});
		var sSelectedTimeZone = this.getModel("dashBoardModel").getProperty("/sSelectedTimeZone");
		if (sSelectedTimeZone && sSelectedTimeZone !== "SYSTEM") {
			switch (timeZone) {
			case "CST":
				epochVal = new Date(new Date(epochVal).toLocaleString("en-US", {
					timeZone: "America/Chicago"
				})).getTime();
				break;
			case "PST":
				epochVal = new Date(new Date(epochVal).toLocaleString("en-US", {
					timeZone: "America/Vancouver"
				})).getTime();
				break;
			case "MST":
				epochVal = new Date(new Date(epochVal).toLocaleString("en-US", {
					timeZone: "America/Edmonton"
				})).getTime();
				break;
			default:
				break;
			}
		}
		var convDate = new Date(epochVal);
		if (sSelectedTimeZone.toLowerCase() === "field") {
			return oDateFormat.format(convDate) + " ( " + timeZone + " )";
		} else {
			return oDateFormat.format(convDate);
		}

	},
	fnConvertCFRole:function(groups){
		var aGrpName;
		for(var i=0;i<groups.length;i++){
			aGrpName=groups[i].value.split("_");
			if(aGrpName[aGrpName.length-1]==="CF"){
				groups[i].value="";
				for(var j=0;j<aGrpName.length-1;j++){
					if(groups[i].value){
						groups[i].value=groups[i].value+"_";
					}
					groups[i].value+=aGrpName[j];
				}
			}
		}
		return groups;
	}
};