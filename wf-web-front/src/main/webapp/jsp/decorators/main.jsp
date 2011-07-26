<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
	<head>
		<title>
			<decorator:title default="Webframe框架" />
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="${pageContext.request.contextPath}/js/ext/resources/css/ext-all.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ext/ext-all.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/wf/wf-layout.js"></script>
		<decorator:head />
		
		<style type="text/css">
			html,body {
				font: normal 12px verdana;
				margin: 0;
				padding: 0;
				border: 0 none;
				overflow: hidden;
				height: 100%;
			}
		</style>
	</head>
	<body onload="<decorator:getProperty property="body.onload"/>">
	   <div id="head" >
	      <div id="main_bg">
				<div class="main_logo"></div>
				<div class="main_time">
					<span class="time_cjmg">
					</span>
				</div>
			</div>	
			<div id="toolbar">
			</div>
		</div>
		<!-- 正文 开始 -->
		<div id="content">
			<decorator:body />
		</div>
		<!-- 正文  结束 -->
	</body>
</html>