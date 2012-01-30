<#global c=JspTaglibs["c.tld"]>
<#global fmt=JspTaglibs["fmt.tld"]>
<#global fn=JspTaglibs["fn.tld"]>
<#global vlh=JspTaglibs["valuelist.tld"]>
<#global form=JspTaglibs["spring-form.tld"]>

<#macro page title>
<html>
	<head>
		<title>FreeMarker Struts Example - ${title?html}</title>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
		<script type="text/javascript">
			function restForm(form, url, method) {
				form.attr("method", method);
				form.attr("action", url);
				$("[name='_method']", form).val(method);
			}
			
			/************全选功能************/
			function cb_selectAll(){
				var allBox = $("#allBox");
				var boxes = document.getElementsByName("ids");
				if(allBox[0].checked){
					for(var i=0;i<boxes.length;i++){
						boxes[i].checked=true;
					}
				}else{
					for(var i=0;i<boxes.length;i++){
						boxes[i].checked=false;
					}
				}
			}
			
			/************获得选中项************/
			function getCheckedItem(name){
				var checkItem = [];				
				var cbArr = document.getElementsByName(name);
				var cbSize = cbArr.length;
				for(var i = 0; i < cbSize; i++) {
					if(cbArr[i].checked) {
						checkItem[checkItem.length] = cbArr[i].value;
					}
				}
				return checkItem.join(",");
			}
			
			/************取消全选************/
			function cb_cancelSelectAll(obj){
				if(!obj.checked){
					var allBox = $("#allBox");
					allBox[0].checked=false;
				}
			}
		</script>
	</head>
	<body>
		<h1>${title?html}</h1>
		<hr>
		<#nested>
		<hr>
		<table border="0" cellspacing=0 cellpadding=0 width="100%">
			<tr valign="middle">
				<td align="left">
				<i>FreeMarker Struts Example</i>
				<td align="right">
		</table>
	</body>
</html>
</#macro>
