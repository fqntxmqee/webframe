
<script type="text/javascript">
	function query(obj) {
		var url = "${ctx}${action}";
		var $form = $(obj).closest("form");
		// restForm方法在common.ftl中定下，主要处理REST请求相关method设置
		restForm($form, url, "get");
		$form.submit();
	}
</script>
<#-- 隐藏列表的查询条件 -->
<input type="hidden" name="pagingNumberPerlistTable" value="<#if listTable??>${listTable.pagingNumberPer}</#if>"/>
<input type="hidden" name="sortColumnlistTable" value="<#if listTable??>${listTable.sortingColumn}</#if>"/>
<input type="hidden" name="sortDirectionlistTable" value="<#if listTable??>${listTable.sortingDirection}</#if>"/>
<input type="hidden" name="pagingPagelistTable" value="<#if listTable??>${listTable.pagingPage}</#if>"/>