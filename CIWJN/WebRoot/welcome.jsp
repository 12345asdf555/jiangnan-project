<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>欢迎使用</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/echarts.js"></script>
	<script type="text/javascript" src="resources/js/getTime.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<script type="text/javascript" src="resources/js/welcome/online.js"></script>
	<script type="text/javascript" src="resources/js/welcome/welcome.js"></script>
	<script type="text/javascript" src="resources/js/paho-mqtt.js"></script>
	<script type="text/javascript" src="resources/js/paho-mqtt-min.js"></script>
	
	<style type="text/css">
		.datagrid{
			border:1px solid #c0c0c0;
			margin-left:4%;
		}
	</style>
  </head>
  
  <body style="background:#ffffff;">
		<div class="leftdiv1">
			<div class="divheader">&nbsp;&nbsp;设备员工在线状态</div>
			<div id="machine"></div>
			<div id="person"></div>
		</div>
		<div class="rightdiv1">
			<div class="divheader">&nbsp;&nbsp;生产信息概括</div>
			<div id="taskcharts" style="height:50%;width:90%;padding-bottom: 10px;"></div>
			<table id="taskdg" style="table-layout: fixed; width:100%;margin-left:20px;margin:auto"></table>
		</div>
		<div class="leftdiv2">
			<div class="divheader">&nbsp;&nbsp;月度焊接时长</div>
			<div id="monthcharts" style="height:90%;width:90%;padding-bottom: 10px;border:none;"></div>
		</div>
  </body>
</html>
