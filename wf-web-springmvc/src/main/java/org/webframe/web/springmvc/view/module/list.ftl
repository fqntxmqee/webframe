<#import "/commons/common.ftl" as com>
<#import "/spring.ftl" as spring />



<@com.page title="List Entry">
	<script type="text/javascript">
		function _new(obj) {
			var url = "${ctx}${action}/new";
			var $form = $(obj).closest("form");
			// restForm方法在common.ftl中定下，主要处理REST请求相关method设置
			restForm($form, url, "get");
			$form.submit();
		}
	
		function edit() {
			var checkedItems = getCheckedItem("ids");
			if(checkedItems == "" || checkedItems == null) {
				alert("请选择需要删除的${is_module_hander??}!");
				return ;
			} else {
				var url = "${ctx}${action}";
				if (checkedItems.indexOf(',') >= 0) {
					alert("修改时只能选择一条<#if is_module_hander??>${is_module_hander}</#if>信息!");
					return;
				} else {
					url += ("/" + checkedItems + "/edit");
				}
				var $form = $("#queryForm");
				// restForm方法在common.ftl中定下，主要处理REST请求相关method设置
				restForm($form, url, "get");
				$form.submit();
			}
		}
		
		function del() {
			var checkedItems = getCheckedItem("ids");
			if(checkedItems == "" || checkedItems == null) {
				alert("请选择需要删除的<#if is_module_hander??>${is_module_hander}</#if>!");
				return ;
			} else {
				var url = "${ctx}${action}";
				if (checkedItems.indexOf(',') < 0) {
					url += ("/" + checkedItems);
				}
				var $form = $("#queryForm");
				// restForm方法在common.ftl中定下，主要处理REST请求相关method设置
				restForm($form, url, "post");
				$("[name='_method']", $form).val("delete");
				$form.submit();
			}
		}
	</script>
	
	<form id="queryForm" method="get" action="${ctx}${action}">
		<#-- 功能操作 -->
		<div>
			<input type="button" value="新增" onclick="_new(this)"/>
			<input type="button" value="修改" onclick="edit()"/>
			<input type="button" value="删除" onclick="del()"/>
			<input type="button" value="查询" onclick="query(this)"/>
			<input type="hidden" name="_method" value="get"/>
		</div>
		
		<#-- 查询条件 -->
		<div>
			<#if attrList??>
				<#list attrList as attr>
					<#if attr.query == true>
						<#assign prop="attribute(" + attr.property + ")">
						<#if attr.type == "text">
							${attr.name}：
							<#if attr.queryConditionType == "interval">
								<#assign beginAttribute="begin" + attr.property>
								<#assign endAttribute="end" + attr.property>
								<input type="text" name="attribute(${beginAttribute})" value="${queryMap[beginAttribute]}"/>
								~
								<input type="text" name="attribute(${endAttribute})" value="${queryMap[endAttribute]}"/>
							<#else>
								<input type="text" name="${prop}" value="${queryMap[attr.property]}"/>
							</#if>
						</#if>
						<#if attr.type == "checkbox" || attr.type == "radio" || attr.type == "select">
							${attr.name}：
							<#if queryMap[attr.property]?? && queryMap[attr.property]?is_boolean>
								<#assign defaultValue=queryMap[attr.property]?string>
							<#elseif queryMap[attr.property]?? && queryMap[attr.property]?is_string>
								<#assign defaultValue=queryMap[attr.property]>
							</#if>
							<select name="${prop}">
								<option value="">请选择</option>
								<#list attr.valueMap?keys as key>
									<option value="${key}" <#if key==defaultValue>selected="selected"</#if> >${attr.valueMap[key]}</option>
								</#list>
							</select>
						</#if>
					</#if>
				</#list>
			</#if>
		</div>
	
		<#-- 列表table -->
		<#if vlhMap??>
			<@vlh.root url=ctx + action + "?" value="vlhMap" includeParameters="*" id="listTable" configName="microsoftLook">
			
				<#-- 隐藏列表的查询条件 -->
				<#include "/commons/valuelist-query-hidden.ftl">
			
				<div class="list_tab_01">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<#if attrList??>
							<tr>
								<#-- 列表table表头，遍历ViewElement.getViewElementList()结果集 -->
								<td align="center">
									<input type="checkbox" value="all" id="allBox" onclick="cb_selectAll()" />
								</td>
								<#list attrList as attr>
									<#if attr.column == true>
										<td>${attr.name}</td>
									</#if>
								</#list>
							</tr>
							
							<#-- 遍历valuelist 查询结果集 -->
							<#list vlhMap.list as vo>
								<tr>
									<td align="center">
										<input type="checkbox" name="ids" value="${vo.id}" onclick="cb_cancelSelectAll(this)" />
									</td>
									<#list attrList as attr>
										<#if attr.column == true>
											<td>
												<#if attr.type == "checkbox" || attr.type == "radio" || attr.type == "select">
													<#if vo[attr.property] == true>
														<span style="color: green">${attr.valueMap["true"]}</span>
													<#elseif vo[attr.property] == false>
														<span style="color: red">${attr.valueMap["false"]}</span>
													<#else>
														${attr.valueMap[vo[attr.property]]}
													</#if>
												<#elseif vo[attr.property]??>
													${vo[attr.property]}
												<#else>
													
												</#if>
											</td>
										</#if>
									</#list>
								</tr>
							</#list>
						</#if>
					</table>
				</div>
				<div class="valuelist_page">
					<#if vlhMap.valueListInfo.totalNumberOfEntries != 0>
						<div id="valuelist_footer">
							<@vlh.paging pages=10 showSummary=true>
							</@vlh.paging>
						</div>
					<#else>
						<div align="center" class="valuelist_nocontent">
							无满足条件的记录
						</div>
					</#if>
				</div>
			</@vlh.root>
		</#if>
	</form>
</@com.page>

