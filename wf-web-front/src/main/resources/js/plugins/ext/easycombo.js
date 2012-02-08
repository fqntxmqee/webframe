/**
 * @author 黄国庆
 * simple
var selectRoleComb = new Ext.form.EasyComboBox({
	id:'id_selectRoleComb',
	key : 'role_combo',//adapter
	triggerAction: "all", //不加该语句选中某项后
	listeners:{
		"select" : function(combo, record, index) {
		}
	},
	storeCfg:{
		listeners:{
			"load" : function() {
				Ext.getCmp('id_selectRoleComb').setValue("");//默认设置空
			}
		}
	}
});
 */
(function () {
	var comboUrl = '/ext/combo';
	
	Ext.form.EasyComboBox = Ext.extend(Ext.form.ComboBox, {
		width : 100,
		selectOnFocus : true,	 
		allowBlank : false,
		editable : false,
		displayField : "name",
		valueField : "valtext",
		key: "",
		storeCfg : {},
		initComponent : function(){
			Ext.form.EasyComboBox.superclass.initComponent.call(this);
			if(!this.url){
				this.url = ctx + comboUrl;
			}
			var url_ =  this.url + ((this.url.indexOf("?") > -1)? "&decorate=no": "?decorate=no");
			var id_ = this.id;
			var baseParams_ = this.baseParams? this.baseParams: {};
			baseParams_.key = this.key;
			this.store = new Ext.data.Store({
				url : url_,
				baseParams : baseParams_,
				remoteSort : true,
				autoLoad : true,
				reader : new Ext.data.JsonReader({}, [ {
					name : 'name'
				}, {
					name : 'valtext'
				} ]),
				listeners : this.storeCfg.listeners? this.storeCfg.listeners: {
					"load" : function() {
						Ext.getCmp(id_).setValue("");//默认所有
					}
				}
			});
		}
	});
	Ext.reg('easycombo', Ext.form.EasyComboBox);
})();