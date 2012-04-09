/** 
 * @memo 简化创建Ext-grid方法 
 * @sample 粘贴用
 var grid = new Ext.grid.EasyGridPanel({
 	 parseHeader:true,
	 id:'grid',
	 title:'',
	 key:'key',
	 databaseAdapter:true,
	 adapter_id_:'1',
	 width:all_width,
	 height:300,
	 baseParams:{},
	 pageSize:20,
	 isRemoteExport:false,
	 isRemoteSort:true,
	 isForceFit:true,
	 isEdit:false,
	 isPage:true,
	 isRank:false,
	 isCheck:true,
	 isLock:true,
	 lockedField:[],
	 checkId:'编号',
	 singleSelect:true,
	 checkOnly:true,
	 form:form,
	 enableHdMenu:true,
	 enableColumnMove:true,
	 sortInfo:{field: '收入', direction: "ASC"},
	 groupField:'客户',
	 tools:[{
	 	id:'gear', 
	 	handler: function(e, target, panel){
	 		//点击事件 
	 }}];,
	 tbar:[{
    text:'Add Something',
    tooltip:'Add a new row',
    iconCls:'add'
  }],
  bbar:[],
  buttons:[
  	{text:'Save'},
  	{text:'Cancel'}
  ],
  buttonAlign:'center',
	 callback:function(cm){
		cm.setRenderer(cm.findColumnIndex('法人码'),function(value,p,record){
			return value+"-"+record.data.行业代码;
		});
		cm.setHidden(cm.findColumnIndex('法人码'),true);
		cm.setColumnWidth(cm.findColumnIndex('企业名称'),200);
		cm.setEditable(cm.findColumnIndex('删除'),false);
		cm.setEditor(cm.findColumnIndex('年'),edityear);
	 }
 });
		 
 * @detail 注释
 var grid = new Ext.grid.EasyGridPanel({
     isAlign:true,                    //可选参数,是否居中显示
 	 parseHeader:true,                //可选参数,下一次加载时是否重新自动解析表头,默认不重新解析,当setKey或setCallBack时会强制设置为true
	 id:'grid',                        //必选参数,唯一标示
	 title:year+"年焊工工作量前10",       //必选参数,标题,可以使用grid.setTitle()更换
	 key:'workLoadViewList',           //必选参数,sql语句的key
	 databaseAdapter:true,				  //可选参数,是从数据库查sql语句的情况,当这个为true时必须配置adapter_id_
	 adapter_id_:'1',						  //可选参数,数据库查sql语句的id
	 width:width/2-215,                //必选参数,宽,当layout:'fit'时可省略
	 height:440,							  //可选参数,高,不写则为autoHeight
	 baseParams:{},						  //可选参数,基本参数,默认是{}
	 pageSize:20,							  //可选参数,每页显示条数,默认是20
	 isRemoteExport:false,				  //可选参数,是否后台导出,true:后台导出,false前台导出,默认null不可导出
	 isRemoteSort:true,					  //可选参数,是否后台排序,true:后台排序,false前台排序,默认null不可排序
	 isForceFit:true,						  //可选参数,是否强制充满,true:充满,默认false不充满
	 isEdit:false,							  //可选参数,是否可编辑,true:可编辑,默认false不可编辑
	 isPage:true,							  //可选参数,是否可分页,true:可分页,默认false不可分页
	 isRank:false,							  //可选参数,是否显示排名编号,true:显示,默认false不显示
	 isCheck:true,							  //可选参数,是否显示多选框,true:显示,默认false不显示
	 isLock:true,							  //可选参数,是否可以锁定列,true:可以,默认false不可以,不能与isRank或isCheck同时使用
	 lockedField:[],						  //可选参数,默认锁定字段的名称(数组)
	 checkId:'编号',							  //可选参数,如果多选需要批量更新删除,请指明id字段
	 singleSelect:true,						  //可选参数,单选or多选,默认多选
	 checkOnly:true,						  //可选参数,点一行任意位置是否会选中checkbox,默认会选中
	 tools:null,							  //可选参数,tools按钮,默认null无tools 例如传入: [{id:'gear', handler: function(e, target, panel){//点击事件 }}];
	 form:form,							     //可选参数,绑定表单,默认null
	 enableHdMenu:false,					  //可选参数,是否显示表头菜单,默认false
	 enableColumnMove:false,           //可选参数,表头是否可以移动
	 sortInfo:{field: '收入', direction: "ASC"}, //可选参数,排序信息
	 groupField:'客户',					//可选参数,表格分组
	 selColumn:false,					//可选参数,是否添加自定义列功能
	 selQuery:false,					//可选参数,是否添加自定义查询功能
	 tbar:[{									  //可选参数,top-bar,默认null
    text:'Add Something',
    tooltip:'Add a new row',
    iconCls:'add'
  }],
  bbar:[],								  //可选参数,bottom-bar,默认null,注意会替换分页控件
  buttons:[								  //可选参数,表下面的按钮,默认右对齐
  	{text:'Save'},
  	{text:'Cancel'}
  ],
  buttonAlign:'center',				  //可选参数,表下面的按钮位置
	 callback:function(cm){				  //可选参数,回调设置表头,默认null
	 	//设置Renderer
		cm.setRenderer(cm.findColumnIndex ('法人码'),function(value,p,record){
			// value 当前列
			// record.data.? 当前行的某一列 例如 record.data.行业代码  
			return value+"-"+record.data.行业代码;
		});
		//设置是否可见
		cm.setHidden(cm.findColumnIndex ('法人码'),true);
		//设置宽度
		cm.setColumnWidth(cm. findColumnIndex ('企业名称'),200);
		//设置是否可编辑
		cm.setEditable(cm. findColumnIndex ('删除'),false);
		//编辑器渲染成控件
		cm.setEditor(cm.findColumnIndex('年'),edityear);
	 }
 });
 * @method
	grid.setKey(key) 重新设置key ,用来显示其他sql语句。
	grid.setCallBack (callback) 重新设置callback ,用来重新配置cm。	
	grid.load() 重新加载表格,会自动传start:0和limit:创建时定义的pagesize。	
	grid.reload() 刷新当前页。	
	grid.getHead() 得到表头json。
	grid.exportData(boolean) 导出数据,传入true为后台导出,传入false为前台导出。 
	grid.getSelectionsByCheckId() 获得选中id,返回字符串以逗号分隔,例如'1','2','3'。
	grid.getSelections(列名) 获得选中行,返回字符串以逗号分隔。
	grid.getParam(参数名) 获得该参数值。
	grid.setParam(参数名,值) 设置该参数值。
	grid.setParams({}) 设置多个参数值。
 */
(function () {
var gridUrl = "/ext/easygrid";//grid数据后台请求url
var gridExportUrl = "/ext/gridexport";

function initCheckBox(gridObj) {
	var sm = new Ext.grid.CheckboxSelectionModel({
		gridId : gridObj.id,
		singleSelect : gridObj.singleSelect,
		checkOnly : gridObj.checkOnly
	});
	gridObj.selModel = sm;
	if(!Ext.isEmpty(gridObj.checkId)){ //多选自动保存编号,需要配置checkId
		sm.on("rowselect", function(o,index,record){ //保存选中行,翻页保存用
			var grid = Ext.getCmp(o.gridId);
			var flag = false;
			for(var i = 0;i<grid.checkedRecord.length;i++){
				if(grid.checkedRecord[i][grid.checkId] == record.data[grid.checkId]){
					flag = true;
					break;
				}
			}
			if(!flag){
				grid.checkedRecord.push(record.data);
			}
		});
		sm.on("rowdeselect", function(o,index,record){ //删除选中行,翻页保存用
			var grid = Ext.getCmp(o.gridId);
			for(var i = 0;i<grid.checkedRecord.length;i++){
				if(grid.checkedRecord[i][grid.checkId] == record.data[grid.checkId]){
					grid.checkedRecord.splice(i,1);
					return;
				}
			}
		});
	}
}

Ext.data.GridHttpProxy = Ext.extend(Ext.data.HttpProxy, {
	createCallback : function(action, rs) {
		var _this = this;
		return function(o, success, response) {
			if (typeof _this.preload === 'function') {
				_this.preload(o, response.responseText);
			}
			this.activeRequest[action] = undefined;
			if (!success) {
				if (action === Ext.data.Api.actions.read) {
					this.fireEvent('loadexception', this, o, response);
				}
				this.fireEvent('exception', this, 'response', action, o,
						response);
				o.request.callback.call(o.request.scope, null,
						o.request.arg, false);
				return;
			}
			if (action === Ext.data.Api.actions.read) {
				this.onRead(action, o, response);
			} else {
				this.onWrite(action, o, response, rs);
			}
		};
	}
});

Ext.override(Ext.grid.EditorGridPanel, {
	
});

Ext.grid.EasyGridPanel = Ext.extend(Ext.grid.EditorGridPanel,{
	/**
	 * 初始化属性
	 */
    customercolumn:'_customer_column_',
	bodyStyle:'padding:0px',
	clicksToEdit:1,
	stripeRows:true,
	trackMouseOver:true,
	iconCls:'icon-grid',
	loadMask:{msg:'正在加载数据，请稍候……'},
	cm:null,
	enableColumnMove:false,
	enableHdMenu:false,
	parseHeader:true, //是否第一次加载,第一次加载会自动创建header和reader
	jsonData:null,//存放grid的原始json数据	add by lilizhao  法人表查询页面用到
	toolTemplate: new Ext.XTemplate(
        '<tpl if="id==\'save\'">',
             '<div class="x-export-excel">&#160;</div>',
        '</tpl>',
        '<tpl if="id==\'savedbf\'">',
        '<div class="x-export-dbf">&#160;</div>',
        '</tpl>',
        '<tpl if="id!=\'save\'&&id!=\'savedbf\'">',
            '<div class="x-tool x-tool-{id}">&#160;</div>',
        '</tpl>'
    ),
	/**
   * 初始化方法
   */
	initComponent : function(){
		Ext.grid.EasyGridPanel.superclass.initComponent.call(this);
		this.pageSize=this.pageSize||20;
		this.baseParams=this.baseParams||{};
		this.baseParams.key=this.key;
		this.baseParams.exportKey = this.exportKey;    //添加一个导出用的key,避免隐藏字段被后台导出
		this.tools=this.tools||[];
		this.isAlign=this.isAlign||false;
		if(!Ext.isEmpty(this.isRemoteExport)){
		 	this.tools.push({
		 		id:'save',
		 		qtip:'导出Excel',
		 		handler: function(e, t, p){
		 			p.exportData(p.isRemoteExport);
		 		}
		 	});
		}
		this.viewConfig= this.viewConfig||{ 
			 columnsText: '显示列', 
			 sortAscText: '升序', 
			 sortDescText: '降序',
			 forceFit:this.isForceFit	
		};
		if(this.isCheck&&Ext.isEmpty(this.singleSelect)) {
			this.singleSelect = false;
		}
		if(this.isCheck&&Ext.isEmpty(this.checkOnly)) {
			this.checkOnly = false;
		}
		if (this.isCheck) initCheckBox(this);
		if(Ext.isEmpty(this.height)){
			this.autoHeight=true;
		}
		if(this.databaseAdapter){
			this.baseParams.adapter_id_ = this.adapter_id_;
		}
		if(!this.url){
			this.url = ctx + gridUrl;
		}
		if(this.autoLoad == null) {
			this.autoLoad = {params:{start:0,limit:this.pageSize}};
		}
		var url_ = this.url+(this.url.indexOf("?")>-1?"&decorate=no":"?decorate=no");
		var storeParam = {
			id : this.id + '-ds',
			gridId : this.id,
			remoteSort : this.isRemoteSort,
			baseParams : this.baseParams,
			form : this.form,
			autoLoad : this.autoLoad,
			head:'',
		  	reader:new Ext.data.JsonReader({root: 'data', totalProperty: 'totalCount'} ),
		  	sortInfo:((!Ext.isEmpty(this.groupField)) && Ext.isEmpty(this.sortInfo))?{field: this.groupField, direction: "ASC"}:this.sortInfo,
		  	groupField:this.groupField,
		  	proxy: new Ext.data.GridHttpProxy(new Ext.data.Connection({url:url_,timeout: 300000000,method:'POST'}))
		};
		var _id = this.id;
		storeParam.proxy.preload = function (a, b) {
			Ext.getCmp(_id).reconfig(a, b, _id)
		};
		var ds;
		if(Ext.isEmpty(this.groupField)){
			ds=new Ext.data.Store(storeParam);
		} else {
			ds=new Ext.data.GroupingStore(storeParam);
			this.view = new Ext.grid.GroupingView({forceFit:true,groupTextTpl: '{text} ({[values.rs.length]} 条记录)' });
		}
		this.store = ds;
		if(this.isPage){
			//var Message='{1} 条记录/页  当前第 {0} 页   共 {2} 条 ';
			var Message='<font style="font-size:14;font-weight:bold;">显示 {0} - {1} 条记录,共 {2}条</font>';
			if(this.showShort==true){
				Message='';
			}
			this.bbar=new Ext.PagingToolbar({
			 	height:30, 
			 	afterPageText: '/ {0}',
			 	beforePageText: '页',
			 	firstText: '第一页',
			 	prevText: '前一页',
			 	nextText: '后一页',
			 	lastText: '最后一页',
			 	refreshText: '刷新',
			 	store: this.store, 
			 	pageSize: this.pageSize,
			 	displayInfo: true, 
			 	displayMsg: Message, 
			 	emptyMsg: "没有数据" 
			});
			if(this.bbar){
				this.elements += ',bbar';
				if(typeof this.bbar == 'object'){
					this.bottomToolbar = this.bbar;
				}
				delete this.bbar;
			}
		}
		this.ds = ds;
		ds.on("beforeload",function(o,options){
			if(!Ext.isEmpty(this.form)){
				var params=this.form.getForm().getValues();
				function eachItem(item,index,length) {
					if (item.isXType("xlovcombo")) {
						if(item.getValue() != null && item.getValue().trim()!="''"){
							params[item.id] = item.getValue();
						}else{
							params[item.id] = null;
						}
					}
				}   
				this.form.items.each(eachItem); 
				Ext.apply(this.baseParams,params);
				Ext.apply(options.params,params);
			}
		});
		//可多选时能自动选中，为了翻页
		if(!Ext.isEmpty(this.checkId)){ //多选自动保存编号,需要配置checkId
			this.store.on('load',function(store, records, options){
				var grid = Ext.getCmp(store.gridId);
				var index = [];
				for(var i = 0;i<records.length;i++){
					for(var j = 0;j<grid.checkedRecord.length;j++){
						if(records[i].data[grid.checkId] == grid.checkedRecord[j][grid.checkId]){
							index.push(i);
							break;
						}
					}
				}
				grid.getSelectionModel().selectRows(index,true);
			});
		}
		var firstColnum = this.selModel || {};
		if(this.isLock){
			this.colModel = new Ext.ux.grid.LockingColumnModel([firstColnum]);
			this.view = new Ext.ux.grid.LockingGridView();
		} else {
			this.colModel = new Ext.grid.ColumnModel([firstColnum]);
		}
		this.colModel.defaultSortable = (this.isRemoteSort!=null||false);
		if(this.selColumn) {
			//判断是否有自定义列功能，设为true时页面需要引入easygridadvanced.js
			this.tools.push({
			    id:'gear',
			    qtip:'自定义列',
			    handler: function(e, target, grid) {
				 	if(Ext.getCmp('selColumn_win_'+grid.id) == undefined) {
						selColumn(grid.key,grid.id,grid.adapter_id_);
					}
				 	var root = Ext.getCmp('selColumn_tree_panel_'+grid.id).root;
				 	root.expand();
					if(!root.firstChild) {
						root.appendChild(grid.getTree());
					}
					Ext.getCmp('selColumn_win_'+grid.id).show();
				}
			});
		}
		if(this.selQuery) {
			//判断是否有自定义查询功能，设为true时页面需要引入easygridadvanced.js
			this.tools.push({
				id:'pin',
				qtip:'高级查询',
				handler: function(e, target, grid) {
					if(Ext.getCmp('selQuery_win_'+grid.id) == undefined) {
				 		selQuery(grid.key,grid.id,grid.adapter_id_);
				 		Ext.getCmp('selQuery_tree_panel_'+grid.id).root.expand();
			 			Ext.getCmp('selQuery_tree_panel_'+grid.id).root.appendChild(grid.getTree());
				 	}
					Ext.getCmp('selQuery_win_'+grid.id).show();
					Ext.getCmp('gridSelQuery_'+grid.id).load();
				}
			});
		}
		this.checkedRecord=this.checkedRecord||[];
	},
	/**
	 * 方法
	 */	
	setKey:function(key){
		this.ds.baseParams.key = key;
		this.parseHeader = true; //需要重新配置header
	},
	getKey:function(){
		return this.ds.baseParams.key;
	},
	setCallBack:function(callback){
		this.ds.callback = callback;
		this.parseHeader = true; //需要重新配置header
	},
	load:function(){
		this.ds.load({params:{start:0,limit:this.pageSize}});
	},
	reload:function(){
		this.store.load();
	},
	getParam:function(p){
		return this.ds.baseParams[p];
	},
	setParam:function(p,v){
		this.ds.baseParams[p]=v;
	},
	setParams : function(j){
		for(var p in j) this.setParam(p,j[p]);
	},
	getHead : function(){
		return this.ds.head;
	},
	exportData:function(flag){
		if(flag){
			//动态创建form提交
			var url = ctx + gridExportUrl;
			var exportForm = document.createElement("FORM");
			document.body.appendChild(exportForm);
			
			var decorate = document.createElement("<input type='hidden'>");
			exportForm.appendChild(decorate);
			decorate.name = "decorate";
			decorate.value = "no";
			
			var key = document.createElement("<input type='hidden'>");
			exportForm.appendChild(key);
			key.name = "key";
			key.value = this.ds.baseParams.exportKey||this.ds.baseParams.key;
			
			var filename = document.createElement("<input type='hidden'>");
			exportForm.appendChild(filename);
			filename.name = "filename";
			filename.value = encodeURI(this.title);
			
			var isExcelRank = document.createElement("<input type='hidden'>");
			exportForm.appendChild(isExcelRank);
			isExcelRank.name = "isExcelRank";
			isExcelRank.value = this.isExcelRank;
			
			for(var p in this.ds.baseParams){
				// 解决 value中带引号的问题
				var hidden_value = document.createElement("<input type='hidden' name='" + p + "'>");
				hidden_value.value = this.ds.baseParams[p];
				exportForm.appendChild(hidden_value);
			}
			if(this.databaseAdapter) {
				var adapter_id_ = document.createElement("<input type='hidden'>");
				exportForm.appendChild(adapter_id_);
				adapter_id_.name = "adapter_id_";
				adapter_id_.value = this.adapter_id_;
				
				var start = document.createElement("<input type='hidden'>");
				exportForm.appendChild(start);
				start.name = "start";
				start.value = 0;
				
				var limit = document.createElement("<input type='hidden'>");
				exportForm.appendChild(limit);
				limit.name = "limit";
				limit.value = this.ds.getTotalCount();
				
			}
			
			exportForm.method = "POST";
			exportForm.action = url;
			exportForm.submit();
		} else {
			if (typeof (this.excelExport) === 'function')
				this.excelExport(this.id, this.title);
			else Ext.Msg.alert('请在页面中配置脚本"/js/plugins/ext/excelutil.js"!');
		}
	},
	getTree:function(){
		var j1 = this.ds.head;
		var j2 = "[{}";
		for ( var p = 0; p < j1.length; p++) {
			if (j1[p].dataIndex.indexOf(this.customercolumn) == -1)
				j2 += ",{'text':'" + j1[p].header + "',expanded: false,leaf:true}";
		}
		;
		j2 += "]";
		j2 = eval(j2.replace("{},", ""));
		return j2;
	},
	getSelectionsByCheckId:function(){
		var result = '';
		for ( var i = 0; i < this.checkedRecord.length; i++) {
			result += "'" + this.checkedRecord[i][this.checkId] + "'";
			if (i + 1 < this.checkedRecord.length) result += ',';
		}
		return result;
	},
	getSelections:function(target){
		var rows = this.getSelectionModel().getSelections();
		if (rows == '') return null;
		var result = '';
		for ( var i = 0; i < rows.length; i++) {
			result += rows[i].get(target);
			if (i + 1 < rows.length)
				result += ',';
		}
		return result;
	},
	reconfig:function(o, j){
		var _this = this, data;
		var sort = null, edit = null, lock = null;
		var j_, flag = true; //是否回调的标志
		var reader=[], head=[];
		try{
			data = eval("(" + j + ")");
			_this.jsonData = data;
			j_ = data.data[0];
		}catch(e){
			this.parseHeader = true; //数据出错,下次加载数据时需要重配表头
			return;
		}
		if(data && data.norecord){
			//没数据
			flag = false;
		} else if(data && data.msg){
			//错误信息
			flag = false;
			if (typeof (show_error_message_win == "function"))
				show_error_message_win(data.msg);
		}
		//解析header
		if(_this.parseHeader || flag == false){
			if (_this.isRemoteSort != null && flag)
				sort = true;
			if (_this.isRank && flag)
				head.push(new Ext.grid.RowNumberer());
			if (_this.isCheck && flag)
				head.push(_this.selModel);
			edit = _this.isEdit == true ? (new Ext.form.TextField({allowBlank : true})) : null;
			for(var p in j_){
				if(p && Ext.util.Format.trim(p) != '' && p != 'ordertype' && p != 'recordnum'){
					if (_this.lockedField && _this.lockedField.indexOf(p) != -1)
						lock = true;
					else
						lock = false;
					var _obj = {
						header : p.replace(_this.customercolumn, ''),
						dataIndex : p,
						locked : lock,
						sortable : sort,
						editor : edit
					};
					if (_this.isAlign) {
						_obj.align = 'center';
					}
					head.push(_obj);
					lock = null;
				}
			}
			_this.ds.head = head;
			_this.getColumnModel().setConfig(head);
			var cm = _this.getColumnModel();
			cm.setEditor=function(i,c){
				cm.config[i].editor.destroy();
				cm.config[i].editor = new Ext.grid.GridEditor(c);
			};	
			if(flag && _this.callback) _this.callback(cm);
			if(flag && _this.events.callback_) _this.fireEvent("callback_",cm);
			_this.parseHeader = false;
		}
		//解析reader
		for(var p in j_){
			if(p && Ext.util.Format.trim(p) != '' && p != 'ordertype' && p != 'recordnum'){
				reader.push({name:p});
			}
		}
		o.reader = new Ext.data.JsonReader({root: 'data', totalProperty: 'totalCount'},reader );
		o.recordType = o.reader.recordType;
		_this.ds.fields = o.recordType.prototype.fields;
		if(!flag) _this.parseHeader = true;
	}
});

Ext.reg('easygrid', Ext.grid.EasyGridPanel);
})();