sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/incture/Incture_IOP/controller/BaseController"

], function(Controller, BaseController) {
	"use strict";

	return BaseController.extend("com.sap.incture.Incture_IOP.controller.View1", {
		onInit: function() {
			// this.getRouter().getRoute("View1").attachPatternMatched(this._onObjectMatched, this);

		},

		refreshapp: function() {
			window.location.href = window.location.href.slice(0, -7);
		}

	});

});