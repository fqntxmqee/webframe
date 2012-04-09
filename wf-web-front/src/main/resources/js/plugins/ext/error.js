/** 
 * 通用错误提示窗 
 */
/*
 * 代码未生效，未找出原因，用override方式替换
 *	Ext.Ajax.on("requestexception", function(conn, res, options) {
		show_error_message_win(res);
	});
*/
Ext.override(Ext.data.Connection, {
    handleFailure : Ext.data.Connection.prototype.handleFailure.createSequence(function(res){
    	show_error_message_win(res);
    })
})
Ext.Ajax.on("requestcomplete", function(conn, res, options) {
	// 判断session过期
	if (res.responseText.indexOf('j_spring_security_check') != -1) {
		var msg = '登陆已超时，点击确认按钮返回登陆页面；点击取消按钮，按住键盘"Ctrl + N"，打开页面登录后，返回之前页面保存信息!';
		Ext.Msg.confirm("登陆超时", msg, function (confirm) {
			if (confirm == 'yes') window.location.reload();
		});
	}
});
function show_error_message_win(res) {
	var resultjson, errorjson;
	if (res.responseText) { //需要转换
		try {
			resultjson = eval("(" + res.responseText + ")");
		} catch (e) {
			//返回的文本有特殊字符，待解决
		}
		errorjson = resultjson.msg || resultjson.error;
	} else { //不需要转换
		errorjson = res;
	}
	if (Ext.getCmp('error_message_win') != null) {
		try {
			Ext.getCmp('error_message_win_grid').store.add(new Ext.data.Record( {
				brief : errorjson.brief,
				detail : errorjson.detail
			}));
		} catch (e) {
			return;
		}
		Ext.getCmp('error_message_win').show();
		return;
	}
	var error_win = new Ext.Window({
		id : 'error_message_win',
		title : '系统提示',
		width : 500,
		height : 200,
		closeAction : 'hide',
		iconCls : 'error',
		border : false,
		resizable : false,
		layout : 'border',
		defaults : {
			margins : "2",
			layout : 'fit',
			border : false,
			autoScroll : false
		},
		items : [{
			region : 'north',
			height : 130,
			items : [ {
				id : 'error_message_win_grid',
				xtype : 'grid',
				store : new Ext.data.ArrayStore({
					fields : [ {
						name : 'brief'
					}, {
						name : 'detail'
					} ],
					data : [ [ errorjson.brief, errorjson.detail ] ]
				}),
				columns : [{
					id : 'brief',
					header : '错误信息',
					width : 160,
					sortable : true,
					dataIndex : 'brief',
					renderer : function(v, d, o) {
						if (errorjson.secondary){
							v += "<br/>" + errorjson.secondary;
						}
						return "<img src='" + ctx + "/images/system/warning.gif' />" + v;
					}
				}, {
					dataIndex : 'detail',
					hidden : true
				} ],
				autoExpandColumn : 'brief',
				stripeRows : true,
				height : 350,
				width : 600,
				listeners : {
					"rowclick" : function(o, index, e) {
						Ext.getCmp('error_message_win_textarea').setValue(o.store.getAt(index).data.detail);
					}
				},
				stateful : true,
				stateId : 'grid'
			} ]
		}, {
			region : 'center',
			items : [ {
				id : 'error_message_win_textarea',
				border : false,
				width : 500,
				xtype : 'textarea',
				style : 'background:#FFFBE7;',
				value : errorjson.detail
			} ]
		} ],
		buttons : [ {
			text : '确定',
			handler : function() {
				error_win.close();
			}
		}, {
			text : '详情 >>',
			flag : 1,
			handler : function() {
				if (this.flag == 1) {
					this.setText("<< 详情");
					error_win.setHeight(350);
					this.flag = 2;
				} else {
					this.setText("详情 >>");
					error_win.setHeight(200);
					this.flag = 1;
				}
			}
		} ]
	});
	error_win.show();
}
