<#-- 隐藏列表的查询条件 -->
<#if attrList??>
	<#list attrList as attr>
		<#if attr.query == true>
			<#if attr.queryConditionType == "interval">
				<#assign beginAttribute="begin" + attr.property>
				<#assign endAttribute="end" + attr.property>
				<input type="hidden" name="attribute(${beginAttribute})" value="${queryMap[beginAttribute]}"/>
				<input type="hidden" name="attribute(${endAttribute})" value="${queryMap[endAttribute]}"/>
			<#else>
				<#if queryMap[attr.property]?? && queryMap[attr.property]?is_boolean>
					<#assign defaultValue=queryMap[attr.property]?string>
				</#if>
				<input type="hidden" name="attribute(${attr.property})" value="${defaultValue}"/>
			</#if>
		</#if>
	</#list>
</#if>