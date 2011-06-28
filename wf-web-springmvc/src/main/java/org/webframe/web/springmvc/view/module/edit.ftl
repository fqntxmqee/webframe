<#import "/commons/common.ftl" as com>

<#escape x as x?html>

<@com.page title="Edit Entry">
	<script type="text/javascript">
		function update(obj) {
			var url = "${ctx}${action}/${model.id}"
			var $form = $(obj).closest("form");
			$form.attr("action", url);
			$form.submit();
		}
	</script>
	
	<@form.form method="put" action=ctx + action modelAttribute="model">
		<input id="submitButton" type="button" value="修改" onclick="update(this)" />
		<input type="button" value="返回列表" onclick="query(this)"/>
		
		<#-- 加载隐藏的valuelist 查询条件 -->
		<#include "/commons/valuelist-query-hidden.ftl">
		
		<#-- 加载隐藏的form 查询条件 -->
		<#include "/commons/form-query-hidden.ftl">
		
		<#-- 加载form模板 -->
		<#include "/commons/form.ftl">
	</@form.form>
</@com.page>

</#escape>