Ext.onReady(function() {
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [ {
			region : 'north',
			height : 94,
			contentEl : 'head',
			border : false,
			margins : '1 1 1 1'
		}, {
			region : 'center',
			margins : '0 2 2 2',
			bodyStyle : 'overflow-y:auto;overflow-x:hidden;',
			border : false,
			autoScroll : true,
			contentEl : 'content'
		}, {
			region: 'south',
			height: 80,
			title: "底部"
		} ]
	});
});