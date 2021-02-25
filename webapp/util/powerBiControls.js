jQuery.sap.declare("com.sap.incture.Incture_IOP.util.powerBiControls");

com.sap.incture.Incture_IOP.util.powerBiControls = {
	takeScreenShot: function () {
		html2canvas(document.getElementById("powerBiPop--powerBiDialog-cont"), {
			onrendered: function (canvas) {
				var tempcanvas = document.createElement('canvas');
				tempcanvas.width = 450;
				tempcanvas.height = 450;
				var context = tempcanvas.getContext('2d');
				context.drawImage(canvas, 0, 0, 1440, 638, 0, 0, 1440, 638);
				var link = document.createElement("a");
				link.href = tempcanvas.toDataURL("image/jpg"); //function blocks CORS
				link.download = "screenshot.jpg";
				link.click();
			}
		});
	},
	embedReport: function (reportData, muwiId, type, taskMode) {
		this.embedReportAndSetTokenListener(reportData, muwiId, type, taskMode);
	},

	fullscreen: function () {
		var element = $("#embedContainer")[0];
		var report = powerbi.get(element);
		report.fullscreen();
	},

	createConfig: function (embedToken, reportId, groupId, muwiId, type, taskMode) {
		var tableName = "Well";
		var columnName = "MUWI";
		var filterValue = muwiId;
		if (type === "FRAC") {
			tableName = "Well";
		}
		var Filter1 = {
			$schema: "http://powerbi.com/product/schema#advanced",
			target: {
				table: tableName,
				column: columnName
			},
			operator: "In",
			values: [filterValue]
		};

		//	'https://app.powerbi.com/reportEmbed?reportId=bac25fa7-d58d-40b6-8b01-606d165c3b43&groupId=be8908da-da25-452e-b220-163f52476cdd'
		var models = window['powerbi-client'].models;
		var permissions = models.Permissions.All;
		var embedUrl = 'https://app.powerbi.com/reportEmbed?reportId=' + reportId + '&groupId=' + groupId;
		var embedConfiguration = {
			type: 'report',
			id: reportId,
			embedUrl: embedUrl,
			tokenType: models.TokenType.Embed,
			permissions: permissions,
			filters: [Filter1],
			settings: {
				navContentPaneEnabled: false,
				customLayout: {
					pageSize: {
						type: models.PageSizeType.Custom,
						width: 1600,
						height: 1200
					}
				},
				displayOption: models.DisplayOption.ActualSize,
				filterPaneEnabled: false,
				extensions: [{
					command: {
						name: "copy",
						title: "copy",
						icon: "",
						extend: {
							visualContextMenu: {
								title: "Copy Key Value"
							}
						}
					}
				}]
			},
			accessToken: embedToken
		};
		return embedConfiguration;
	},
	embedReportAndSetTokenListener: function (reportData, muwiId, type, taskMode) {

		var embedContainer = $('#embedContainer')[0];

		// set config for embedding report
		var reportId = reportData.reportId;
		var groupId = reportData.groupId;
		var embedToken = reportData.accessToken;
		var config = this.createConfig(embedToken, reportId, groupId, muwiId, type, taskMode);

		//  console.log(config);
		// Get a reference to the embedded report HTML element

		// Embed the report and display it within the div container.
		var report = powerbi.embed(embedContainer, config);

		// Report.off removes a given event handler if it exists.

		// report.off("loaded");
		// 		var tableName = "getTrends";
		// 		var columnName = "MUWI";
		// 		var filterValue = muwiId;
		// 		if (type === "INVESTIGATION") {
		// 			tableName = "getALS";
		// 			if (taskMode === "7") {
		// 				columnName = "MUWI_X";
		// 				filterValue = muwiId+"-Y";
		// 			}
		// 		}
		// 		else if (type === "VARIENCE") {
		// 			tableName = "DOP";
		// 			columnName = "MUWI_X";
		// 			if (taskMode === "7") {
		// 				filterValue = muwiId+"-Weekly";
		// 			} else {
		// 				filterValue = muwiId+"-Daily";
		// 			}
		// 		}
		// 		// var embedContainer = $('#embedContainer')[0];
		var embdedReport = powerbi.get(embedContainer);
		// 		var Filter1 = {
		// 			$schema: "http://powerbi.com/product/schema#advanced",
		// 			target: {
		// 				table: tableName,
		// 				column: columnName
		// 			},
		// 			operator: "In",
		// 			values: [filterValue]
		// 		};

		// embdedReport.setFilters([filter]).catch(function (errors) {
		// 	Log.log(errors);
		// });

		// report.on("loaded", function() {
		// 	$("#embedContainer").height("30em");  
		// 	$("#embedContainer").width("80em");  
		// });

		// report.on('loaded', event => {
		// 	$("#embedContainer").height("30em");  
		// 	$("#embedContainer").width("76em");
		// 	report.getFilters().then(filters => {
		//  			filters.push(Filter1); 
		//  			return report.setFilters(filters);
		// 	});
		// });

		report.on('loaded', function () {
			var width = sap.ui.Device.resize.width - 40 + "px";
			var height = sap.ui.Device.resize.height - 170 + "px";
			$("#embedContainer").height(height);
			$("#embedContainer").width(width);
			// 			report.getFilters().then(function(filters) {
			// 				filters.push(Filter1);
			// 				return report.setFilters(filters);
			// 			});
		});

		// report.on("dataSelected", function(event) {
		// 	var data = event.detail;
		// 	console.log(data);
		// });

	}
};