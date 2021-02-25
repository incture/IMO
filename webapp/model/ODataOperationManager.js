sap.ui.define([
	"sap/ui/model/json/JSONModel",
	"sap/ui/model/odata/ODataModel",
	"sap/ui/base/Object",
	"com/sap/incture/Incture_IOP/util/ErrorHandler"

], function(JSONModel, ODataModel, Object, ErrorHandler) {
	"use strict";

	/**
	 * Constructor
	 */
	var RocAppModel = Object.extend("com.sap.incture.Incture_IOP.model.ODataOperationManager", {
		_oDataModel: null,
		_oComponent: null,
		oJsonModel: new sap.ui.model.json.JSONModel(),
		constructor: function(oComponent) {
			this._oComponent = oComponent;
			this._oDataModel = oComponent.getModel();
			//this.ErrorHandler = new ErrorHandler(this._oComponent);
		}
	});

	RocAppModel.prototype.getJson = function() {
		return this.oJsonModel;
	};
	RocAppModel.prototype.getOdataModel = function() {
		return this._oDataModel;
	};

	/*
	 * Method to set/remove busy Indicator 
	 *
	 * @param bParam - Boolian Parameter- true to enable Busy - false to disable busy Indicator.
	 * @param iDelay - Number Parameter - will saet the deley for Busy Indicator.
	 * @public
	 */
	RocAppModel.prototype.busyIndicatorDisplay = function(bParam, iDelay) {
		if (iDelay === null || iDelay === undefined || iDelay < 0) {
			iDelay = 0;
		}
		this._oComponent.getRootControl().getModel("appView").setProperty("/delay", iDelay);
		this._oComponent.getRootControl().getModel("appView").setProperty("/busy", bParam);

	};
	/*
	 * Method to read an oData Entity
	 *
	 * @param fSuccess - Success callback. OData model returned as parameter.
	 * @param fError - Error callback. Error object returned as parameter.
	 * @public
	 */
	RocAppModel.prototype.readData = function(sUrl, oParameters, fSuccess, fError) {

		this._oDataModel.read(encodeURI(sUrl), {
			urlParameters: oParameters,
			success: function(oData) {
				//Result has .results for Entitysets but not for queries
				if (fSuccess) {
					//this.getJson().setProperty(sRefreshBindingTarget, false);
					fSuccess(oData);
				}
			},
			error: function(oError) {
				fError(oError);
			}
		});

	};

	RocAppModel.prototype.deleteOdata = function(sUrl, fSuccess, fError) {
		this._oDataModel.remove(encodeURI(sUrl), {
			method: "DELETE",
			success: function(data) {
				fSuccess(data);
			},
			error: function(oError) {
				fError(oError);
			}

		});
	};

	/*
	 * Method to Create the  the OData service.
	 * @param oData - Data passed for the creation 
	 * @param fSuccess - Success callback. OData model returned as parameter.
	 * @param fError - Error callback. Error object returned as parameter.
	 * @public
	 */
	RocAppModel.prototype.createoData = function(sUrl, oData, fSuccess, fError) {
		this._oDataModel.create(sUrl, oData, {
			success: function(data, res) {
				fSuccess(data);
			},
			error: function(error) {
				fError(error);
			}
		});
	};

	RocAppModel.prototype.updateoData = function(sUrl, oData, fSuccess, fError) {
		this._oDataModel.update(sUrl, oData, {
			success: function(data) {
				fSuccess(data);
			},
			error: function(error) {
				fError(error);
			}
		});
	};

	RocAppModel.prototype.updateoDataBatch = function(sUrl, oData, mParam) {
		this._oDataModel.update(sUrl, oData, mParam);

	};
	RocAppModel.prototype.createoDataBatch = function(sUrl, oData, mParam) {
		this._oDataModel.create(sUrl, oData, mParam);

	};
	RocAppModel.prototype.deleteoDataBatch = function(sUrl, mParam) {
		this._oDataModel.remove(sUrl, mParam);

	};
	RocAppModel.prototype.submitChangeBatch = function(sGroupId) {
		this._oDataModel.setDeferredGroups([sGroupId]);
		this._oDataModel.submitChanges({
			batchGroupId: sGroupId
		}, function(oData) {

		}, function(oError) {

		});
	};

	return RocAppModel;
});