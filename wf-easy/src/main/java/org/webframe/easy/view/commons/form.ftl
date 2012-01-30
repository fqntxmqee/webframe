<#import "/spring.ftl" as spring />

<table class="formTable">
	<#list attrList as attr>
		<#if attr.formed == true>
			<#if attr.type == "hidden">
				<@spring.formHiddenInput "model.${attr.property}", "class=\"${attr.css}\""/>
			<#else>
				<tr>
					<td class="">${attr.name}：</td>
					<td>
						<#if attr.type == "text">
							<@spring.formInput "model.${attr.property}", "class=\"${attr.css}\""/>
						</#if>
						<#if attr.type == "textarea">
							<@spring.formTextarea "model.${attr.property}", "class=\"${attr.css}\""/>
						</#if>
						<#if attr.type == "password">
							<@spring.formPasswordInput "model.${attr.property}", "class=\"${attr.css}\""/>
						</#if>
						<#if attr.type == "checkbox">
							<@spring.formCheckboxes "model.${attr.property}", attr.valueMap, "", "class=\"${attr.css}\""/>
						</#if>
						<#if attr.type == "radio">
							<@spring.formRadioButtons "model.${attr.property}", attr.valueMap, "", "class=\"${attr.css}\""/>
						</#if>
						<#if attr.type == "select">
							<@spring.formSingleSelect "model.${attr.property}", attr.valueMap, "class=\"${attr.css}\""/>
						</#if>
						<#if attr.type == "file">
						</#if>
					</td>
				</tr>
			</#if>
		</#if>
	</#list>
</table>