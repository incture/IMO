sap.tnt.NavigationListItem.extend("com.sap.incture.Incture_IOP.util.NavigationListItem",{
	metadata:{
		properties:{
			secondryValue:{
				type: "integer",
				defaultValue:0,
				 group: "Misc"
			}	
		}
	},
	renderer:{}
});
c.prototype._renderText = function(r) {
        r.write('<span');
        r.addClass("sapMText");
        r.addClass("sapTntNavLIText");
        r.addClass("sapMTextNoWrap");
        r.writeClasses();
        var t = this.getTextDirection();
        if (t !== sap.ui.core.TextDirection.Inherit) {
            r.writeAttribute("dir", t.toLowerCase());
        }
        var d = R.getTextAlign(sap.ui.core.TextAlign.Begin, t);
        if (d) {
            r.addStyle("text-align", d);
            r.writeStyles();
        }
        r.write(">");
        r.writeEscaped(this.getText());
        r.write("</span>");
    }
    ;