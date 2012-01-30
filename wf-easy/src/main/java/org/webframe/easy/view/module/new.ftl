<#import "/commons/common.ftl" as com>

<#escape x as x?html>

<@com.page title="Add Entry">
	<script type="text/javascript">
		function create() {
			var url = "${ctx}${action}/${model.id}"
			var $form = $("#model");
			$form.attr("action", url);
			$form.submit();
		}
	</script>
	<@form.form method="post" action=ctx + action modelAttribute="model">
		<input id="submitButton" type="submit" value="提交" />
		<input type="button" value="返回列表" onclick="query(this)"/>
		
		<#-- 隐藏列表的查询条件 -->
		<#include "/commons/valuelist-query-hidden.ftl">
		
		<#-- 加载隐藏的form 查询条件 -->
		<#include "/commons/form-query-hidden.ftl">
		
		<#-- 加载form模板 -->
		<#include "/commons/form.ftl">
	</@form.form>
</@com.page>

</#escape>