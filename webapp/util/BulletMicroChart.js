jQuery.sap.require("sap.suite.ui.microchart.BulletMicroChart");
sap.suite.ui.microchart.BulletMicroChart.extend("com.sap.incture.Incture_IOP.util.BulletMicroChart", {

	onBeforeRendering: function () {
		this._addTitleAttribute = function () {
			if (!this.$().attr("title")) {
				var obj = this.getBindingContext("dashBoardModel").getObject();
				var currentTab = this.getModel("dashBoardModel").getProperty("/panelProperties/currentSelectKey");
				var mCustomText;
				switch (currentTab) {
				case "ndtpv":
					mCustomText = "Actual BOED: " + obj.avgActualBoed + "\nForcast BOED: " + obj.avgForecastBoed;
					break;
				case "DOP":
					mCustomText = "Projected BOED: " + obj.projectedBoed + "\nForcast BOED: " + obj.forecastBoed;
					break;
				}
				this.$().attr("title", mCustomText);
			}
		};
	},
	renderer: {}
});