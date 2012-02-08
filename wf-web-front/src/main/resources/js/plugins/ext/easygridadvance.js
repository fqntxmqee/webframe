/**
 * @author lixiaokui
 * @function 添加自定义列功能
 * @param adapter
 * @param gridId
 * @return
 */
function selColumn(adapter, gridId, adapter_id) {
	if (adapter == null || gridId == null) {
		return;
	}
	var gridSelColumn = new Ext.grid.EasyGridPanel( {
		id : 'gridSelColumn_' + gridId,
		title : '自定义列',
		key : 'dicAllColumnAdapter',
		baseParams : {
			key_ : adapter,
			adapter_id : adapter_id
		},
		width : 398,
		isForceFit : true,
		border : true,
		isPage : true,
		callback : function(cm) {
			cm.setHidden(cm.findColumnIndex('编号'), true);
			cm.setRenderer(cm.findColumnIndex('操作'), function(value, p, record) {
				var id = record.data.编号;
				var str = "";
				if (value == "1") {
					str = "<a href='#' onclick=\"javascript:checkDisplay('"
							+ id
							+ "',1,'"
							+ gridId
							+ "')\">取消</a>";
				} else {
					str = "<a href='#' onclick=\"javascript:checkDisplay('"
							+ id
							+ "',0,'"
							+ gridId
							+ "')\">选择</a>";
				}
				str += "  <a href='#' onclick=\"javascript:deleteSelColumnRow('"
						+ id
						+ "','"
						+ gridId
						+ "')\">删除</a>";
				return str;
			});
		}
	});
	var advancedColumnWindow_ = new Ext.Window( {
		width : 970,
		height : 450,
		layout : "border",
		modal : true,
		border : false,
		id : "selColumn_win_" + gridId,
		closeAction : 'hide',
		items : [{
			id : 'selColumn_tree_panel_' + gridId,
			region : 'west',
			title : '指标',
			height : 410,
			width : 205,
			xtype : 'treepanel',
			margins : '2 2 0 2',
			// enableDD:true,
			autoScroll : true,
			root : {
				text : '查询指标',
				draggable : false,
				id : '-1'
			},
			loader : new Ext.tree.TreeLoader({
				dataUrl : "-"
			}),
			listeners : {
				'click' : function(node, e) {
					addquery(node, "selColumnSql_" + gridId);
				}
			}
		}, {
			region : 'center',
			title : '编辑器',
			width : 320,
			height : 410,
			margins : '2 2 0 2',
			items : new Ext.Panel({
				layout : 'table',
				border : false,
				// bodyStyle:'padding:5px',
				layoutConfig : {
					columns : 5
				},
				defaults : {
					frame : false,
					width : 40
				},
				items : [{
					xtype : "textarea",
					id : "selColumnSql_" + gridId,
					width : 320,
					height : 160,
					enableKeyEvents : true,
					hideLabel : true,
					colspan : 5,
					style : {
						marginBottom : '10px',
						imeMode : 'disabled' // 屏蔽输入法
					},
					listeners : {
						'render' : function(e) {
							// 屏蔽粘贴和鼠标右键
							Ext.get('selColumnSql_' + gridId).dom.oncontextmenu = function(e) {
								return false;
							};
							Ext.get('selColumnSql_' + gridId).dom.onpaste = function(e) {
								return false;
							};
						},
						'keypress' : function(e) {// 屏蔽键盘按键
							event.keyCode = 0;
							event.returnvalue = false;
						}
					}
				}, {
					border : false,
					html : " ",
					width : 30
				}, {
					xtype : "button",
					text : "7",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "8",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "9",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " || ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					border : false,
					html : "",
					width : 5
				}, {
					xtype : "button",
					text : "4",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "5",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "6",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " - ",
					minWidth : '40',
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					border : false,
					html : "",
					width : 5
				}, {
					xtype : "button",
					text : "1",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "2",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "3",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " * ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					border : false,
					html : "",
					width : 5
				}, {
					xtype : "button",
					text : "0",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : ".",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " % ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " / ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					border : false,
					html : "",
					width : 10
				}, {
					xtype : "button",
					text : " ( ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " ) ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : "\'",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : ",",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					border : false,
					html : "",
					width : 10
				}, {
					xtype : "button",
					text : "\"",
					minWidth : '40',
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					}
				}, {
					xtype : "button",
					text : " substring ",
					minWidth : '90',
					handler : function() {
						addquery(this, 'selColumnSql_' + gridId);
					},
					colspan : 2
				}, {
					border : false,
					html : "",
					colspan : 2
				}, {
					border : false,
					html : "",
					colspan : 4
				}, {
					border : false,
					html : "",
					colspan : 4
				}, {
					border : false,
					html : "",
					colspan : 1
				}, {
					xtype : "button",
					text : " 保存 ",
					handler : function() {
						if (Ext.getCmp('selColumnSql_' + gridId).getValue() == "") {
							alert("内容不能为空");
							return;
						}
						Ext.MessageBox.prompt('提示', '请输入自定义列名称:', function(a, b) {
							if (a == 'cancel') {
								return;
							}
							if (b.trim() == "") {
								alert("名称不能为空");
								return;
							}
							Ajax.add({
								success : function(response, options) {
									gridSelColumn.reload();
								},
								failure : function() {
									alert('修改失败!');
								},
								params : {
									Constant_TableName_ : 'dic_grid_column',
									Is_Ajax_ : 'true',
									is_display_ : 1,
									key_ : gridSelColumn.getParam('key_'),
									text_ : Ext.getCmp('selColumnSql_' + gridId).getValue(),
									label_ : b,
									detail_query_id_ : gridSelColumn.getParam('adapter_id')
								}
							});
						});
					}
				}, {
					xtype : "button",
					text : " 查询 ",
					handler : function() {
						Ext.getCmp(gridId).load();
						advancedColumnWindow_.hide();
					}
				}, {
					xtype : "button",
					text : " 清除 ",
					handler : function() {
						Ext.getCmp('selColumnSql_' + gridId).setValue('');
					}
				} ]
			})
		}, {
			id : 'selColumnGridPanel_' + gridId,
			region : 'east',
			title : '',
			border : false,
			margins : '2 2 0 2',
			height : 410,
			width : 400,
			items : [ gridSelColumn ]
		} ]
	});// Ext.Window
}
/**
 * @function 自定义查询条件
 * @param adapter
 * @param gridId
 */
function selQuery(adapter, gridId, adapter_id) {
	var selectedIndex = null;
	var gridSelQuery = new Ext.grid.EasyGridPanel({
		id : 'gridSelQuery_' + gridId,
		title : '自定义查询条件',
		key : 'dicAllQueryConditionAdapter',
		baseParams : {
			key_ : adapter,
			adapter_id : adapter_id
		},
		width : 398,
		autoLoad : false,
		isForceFit : true,
		border : true,
		isPage : true,
		isCheck : true,
		singleSelect : true,
		callback : function(cm) {
			cm.setHidden(cm.findColumnIndex('编号'), true);
			cm.setRenderer(cm.findColumnIndex('操作'), function(value, p, record, rowIndex) {
				var id = record.data.编号;
				var str = "";
				if (value == '1') {
					selectedIndex = rowIndex;
				}
				str += "  <a href='#' onclick=\"javascript:deleteSelQueryRow('"
						+ id + "','" + gridId + "')\">删除</a>";
				return str;
			});
		}
	});
	gridSelQuery.store.on('load', function(store, records, options) {
		gridSelQuery.selModel.selectRow(selectedIndex, true);
	});
	var advancedQueryWindow_ = new Ext.Window( {
		title : '高级查询',
		width : 970,
		height : 450,
		layout : "border",
		modal : true,
		id : "selQuery_win_" + gridId,
		closeAction : 'hide',
		items : [ {
			id : 'selQuery_tree_panel_' + gridId,
			region : 'west',
			title : '',
			margins : '0 2 2 2',
			cmargins : '2 2 2 2',
			height : 410,
			width : 205,
			x : 1,
			y : 5,
			xtype : 'treepanel',
			margins : '2 2 0 2',
			// enableDD:true,
			autoScroll : true,
			root : {
				text : '查询指标',
				draggable : false,
				id : '-1'
			},
			loader : new Ext.tree.TreeLoader({
				dataUrl : "-"
			}),
			listeners : {
				'click' : function(node, e) {
					addquery(node, "selQuerySql_" + gridId);
				}
			}
		}, {
			region : 'center',
			title : '编辑器',
			width : 320,
			height : 410,
			margins : '2 2 0 2',
			items : new Ext.Panel({
				layout : 'table',
				border : false,
				// bodyStyle:'padding:5px',
				layoutConfig : {
					columns : 5
				},
				defaults : {
					frame : false,
					width : 40
				},
				items : [ {
					xtype : "textarea",
					id : "selQuerySql_" + gridId,
					width : 320,
					height : 200,
					style : {
						marginBottom : '10px'
					},
					colspan : 5
				}, {
					xtype : "button",
					text : " || ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " - ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " * ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " / ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " = ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " < ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " > ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : "%",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " ( ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " ) ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : "\'",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : ",",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " IN ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " AND ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " OR ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " LIKE ",
					minWidth : '40',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					}
				}, {
					xtype : "button",
					text : " BETWEEN ",
					minWidth : '80',
					style : {
						marginBottom : '5px'
					},
					handler : function() {
						addquery(this, "selQuerySql_" + gridId);
					},
					colspan : 4
				}, {
					border : false,
					html : "",
					colspan : 5
				}, {
					border : false,
					html : "",
					colspan : 1
				}, {
					xtype : "button",
					text : " 保存 ",
					handler : function() {
						if (Ext.getCmp('selQuerySql_' + gridId).getValue() == "") {
							alert("内容不能为空");
							return;
						}
						Ext.MessageBox.prompt('提示', '请输入自定义查询条件名称:', function(a, text) {
							if (a == 'cancel') {
								return;
							}
							if (text.trim() == "") {
								alert("名称不能为空");
								return;
							}
							Ajax.add({
								success : function(response, options) {
									gridSelQuery.reload();
								},
								failure : function() {
									alert('修改失败!');
								},
								params : {
									Constant_TableName_ : 'dic_grid_query_condition',
									Is_Ajax_ : 'true',
									is_display_ : 0,
									key_ : gridSelQuery.getParam('key_'),
									text_ : Ext.getCmp('selQuerySql_' + gridId).getValue(),
									label_ : text,
									detail_query_id_ : gridSelQuery.getParam('adapter_id')
								}
							});
						});
					}
				}, {
					xtype : "button",
					text : " 查询 ",
					handler : function() {
						var selectedRow = Ext.getCmp('gridSelQuery_' + gridId).getSelectionModel().getSelected();
						var queryConditionId = (selectedRow == undefined) ? null : selectedRow.get('编号');
						updateQueryCondition(queryConditionId, gridSelQuery.getParam('adapter_id'), gridId, adapter);
					}
				}, {
					xtype : "button",
					text : " 重置 ",
					handler : function() {
						Ext.getDom("selQuerySql_" + gridId).value = "";
					}
				} ]
			})
		}, {
			id : 'selQueryGridPanel_' + gridId,
			region : 'east',
			title : '',
			border : false,
			margins : '2 2 0 2',
			height : 410,
			width : 400,
			items : [ gridSelQuery ]
		} ]
	});// Ext.Window结束
}
function addquery(o, sqlTextareaId) {
	var tc = Ext.getDom(sqlTextareaId);
	var tclen = tc.value.length;
	tc.focus();
	if (typeof document.selection != "undefined") {
		document.selection.createRange().text = o.text;
	} else {
		tc.setValue(tc.value.substr(0, tc.selectionStart) + o.text + tc.value.substring(tc.selectionStart, tclen));
	}
}
function checkDisplay(id, value, gridId) {
	Ajax.update({
		success : function(response, options) {
			Ext.getCmp('gridSelColumn_' + gridId).reload();
		},
		failure : function() {
			alert('修改失败!');
		},
		params : {
			Constant_TableName_ : 'dic_grid_column',
			Constant_PK_ : id,
			Is_Ajax_ : 'true',
			is_display_ : value == 1 ? 0 : 1
		}
	});
}
// 删除自定义列
function deleteSelColumnRow(id, gridId) {
	if (!confirm('确定删除?'))
		return;
	Ajax.del({
		success : function(response, options) {
			Ext.getCmp('gridSelColumn_' + gridId).reload();
		},
		failure : function() {
			alert('修改失败!');
		},
		params : {
			Constant_TableName_ : 'dic_grid_column',
			Constant_PK_ : id,
			Is_Ajax_ : 'true'
		}
	});
}
// 修改查询条件
function updateQueryCondition(queryConditionId, adapter_id, gridId, adapter) {
	Ext.Ajax.request({
		url : getRootPath() + '/queryConditionUpdate.report',
		success : function(response, options) {
			Ext.getCmp(gridId).load();
			Ext.getCmp('selQuery_win_' + gridId).hide();
		},
		failure : function() {
			alert("查询出错");
		},
		params : {
			queryConditionId : queryConditionId,
			adapter : adapter,
			adapter_id : adapter_id
		}
	});
}
// 删除自定义查询条件
function deleteSelQueryRow(id, gridId) {
	if (!confirm('确定删除?'))
		return;
	Ajax.del({
		success : function(response, options) {
			Ext.getCmp('gridSelQuery_' + gridId).reload();
		},
		failure : function() {
			alert('修改失败!');
		},
		params : {
			Constant_TableName_ : 'dic_grid_query_condition',
			Constant_PK_ : id,
			Is_Ajax_ : 'true'
		}
	});
}