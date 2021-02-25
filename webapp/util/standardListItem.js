sap.ui.define(["sap/tnt/NavigationListItem", "sap/ui/core/Renderer", "sap/ui/core/IconPool"], function (NavigationListItem, R, b) {
	var n = NavigationListItem.extend("com.sap.incture.Incture_IOP.util.NavListItem", {
		metadata: {
			properties: {
				pCount: "",
				pIcon: {
					type: "sap.ui.core.URI",
					group: "Misc",
					defaultValue: ""
				}
			},
			events: {
				eHover: {}
			}
		},
		onmouseover: function () {
			this.fireEHover();
		}
	});
	n.prototype.renderGroupItem = function (r, d, i, e) {
		var f = d.getExpanded(),
			g = this.getExpanded(),
			t = this.getText(),
			pCount = this.getPCount(), //AN: #Notif
			h, j = {
				level: "1",
				posinset: i + 1,
				setsize: this._getVisibleItems(d).length
			};
		if (f && this.getItems().length !== 0) {
			j.expanded = g;
		}
		r.write("<div");
		r.addClass("sapTntNavLIItem");
		r.addClass("sapTntNavLIGroup");
		if (!this.getEnabled()) {
			r.addClass("sapTntNavLIItemDisabled");
		} else {
			r.write(' tabindex="-1"');
		}
		if (!f) {
			h = this.getTooltip_AsString() || t;
			if (h) {
				r.writeAttributeEscaped("title", h);
			}
			j.label = t;
			j.role = "button";
			j.haspopup = true;
		} else {
			j.role = "treeitem";
		}
		r.writeAccessibilityState(j);
		if (d.getExpanded()) {
			h = this.getTooltip_AsString() || t;
			if (h) {
				r.writeAttributeEscaped("title", h);
			}
			r.writeAttributeEscaped("aria-label", t);
		}
		r.writeClasses();
		r.write(">");
		this._renderIcon(r);
		if (d.getExpanded()) {
			var k = this._getExpandIconControl();
			k.setVisible(this.getItems().length > 0 && this.getHasExpander());
			k.setTooltip(this._getExpandIconTooltip(!this.getExpanded()));
			this._renderText(r);
			r.renderControl(k);
			if (pCount && pCount > 0) { //AN: #Notif
				this._renderCount(r);
			}
		}
		r.write("</div>");
	};
	n.prototype._renderIcon = function (r, d, p) {
		var i = this.getIcon();
		var j = this.getPIcon();
		d = b.getIconInfo(i);
		p = b.getIconInfo(j);
		var pCount = this.getPCount();
		if (i) {
			r.write("<span");
			r.addClass("sapUiIcon");
			r.addClass("sapTntNavLIGroupIcon");
			r.writeAttribute("aria-hidden", true);
			if (d && !d.suppressMirroring) {
				r.addClass("sapUiIconMirrorInRTL");
			}
			if (d) {
				r.writeAttribute("data-sap-ui-icon-content", d.content);
				r.addStyle("font-family", "'" + d.fontFamily + "'");
			}
			r.writeClasses();
			r.writeStyles();
			r.write("></span>");
			if (!this.getParent().getExpanded() && pCount && pCount > 0) { //AN: #Notif
				// Counter in TEXT--------------------
				// r.write('<span');
				// r.addClass("sapMText");
				// r.addClass("sapMTextNoWrap");
				// r.addStyle("text-align", "left");
				// r.addStyle("position", "absolute");
				// r.addStyle("top", 0);
				// r.addStyle("right", "3px");
				// r.addStyle("color", "red");
				// r.writeClasses();
				// r.writeStyles();
				// r.write(">");
				// r.writeEscaped(this.getPCount());
				// r.write("</span>");

				//Counter in BUTTON--------------------
				/*	r.write("<button");
					r.addClass("sapMBtn sapMBtnBase");
					r.addStyle("position", "absolute");
					r.addStyle("top", 0);
					r.addStyle("right", 0);
					r.writeClasses();
					r.writeStyles();
					r.write(">");
					r.write("<div");
					r.addClass("sapMBtnDefault sapMBtnHoverable sapMBtnInner sapMBtnText sapMFocusable");
					r.addStyle("min-width", "1.5rem");
					r.addStyle("border-radius", "1rem");
					r.addStyle("background-color", "red");
					r.addStyle("border", "none");
					r.addStyle("color", "#fff");
					r.addStyle("text-shadow", "none");
					r.writeClasses();
					r.writeStyles();
					r.write(">");
					r.write("<span");
					r.addClass("sapMBtnContent");
					r.writeStyles();
					r.writeClasses();
					r.write(">");
					r.write(this.getPCount());
					r.write("</span>");
					r.write("</div>");*/

				// ONLY BUBBLE--------------------
				r.write("<span");
				r.addClass("sapUiIcon");
				r.addClass("sapTntNavLIGroupIcon");
				r.writeAttribute("aria-hidden", true);
				if (p && !p.suppressMirroring) {
					r.addClass("sapUiIconMirrorInRTL");
				}
				if (p) {
					r.writeAttribute("data-sap-ui-icon-content", p.content);
					r.addStyle("font-family", "'" + p.fontFamily + "'");
					r.addStyle("position", "absolute");
					r.addStyle("top", "0rem");
					r.addStyle("right", "-0.6rem");
					r.addStyle("padding", 0);
					r.addStyle("color", "red");
					r.addStyle("font-size", "0.65rem");
				}
				r.writeClasses();
				r.writeStyles();
				r.write("></span>");
			}
		} else {
			r.write('<span class="sapUiIcon sapTntNavLIGroupIcon" aria-hidden="true"></span>');
		}
	};
	n.prototype._renderText = function (r) {
		r.write("<span");
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
	};
	n.prototype._renderCount = function (r) {
		r.write("<span");
		r.addClass(" sapMLIBCounter");
		r.addClass("sapUiTinyMarginEnd ");
		r.writeClasses();
		r.write(">");
		r.writeEscaped(this.getPCount());
		r.write("</span>");
	};
	return n;
});