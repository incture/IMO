sap.m.Table.extend("com.sap.incture.Incture_IOP.util.Table",{
	metadata:{
		properties:{
			currentContextObj:{
				type: "object"
			}	
		},
		events:{
			"rightPress":{}
		}
	},
	oncontextmenu : function(evt){
		this.fireRightPress({src:evt.srcControl,target:evt.target});
	//	this.setCurrentContextObj(evt.target);
		return false;
	},
	renderer:{}
});