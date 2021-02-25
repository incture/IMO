sap.ui.define([
	"sap/ui/core/UIComponent",
	"sap/ui/Device",
	"com/sap/incture/Incture_IOP/model/models"
], function (UIComponent, Device, models) {
	"use strict";

	return UIComponent.extend("com.sap.incture.Incture_IOP.Component", {

		metadata: {
			manifest: "json"
		},

		/**
		 * The component is initialized by UI5 automatically during the startup of the app and calls the init method once.
		 * @public
		 * @override
		 */
		init: function () {
			// jQuery.sap.includeScript("sap/fiori/murphyiopwebv2/util/plotly.js");
			// call the base component's init function
			UIComponent.prototype.init.apply(this, arguments);

			// set the device model
			this.setModel(models.createDeviceModel(), "device");
			this.getRouter().initialize();
		}
	});
});