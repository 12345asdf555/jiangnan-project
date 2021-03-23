<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>班组生产数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />

	<script type="text/javascript" src="resources/js/load.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js//datastatistics/taskdetail.js"></script>
	<style type="text/css">
		.textbox-text {
			width:100px;
		}
	</style>
  </head>
  
  <body>
    <div id="body">
		<div id="chartLoading" style="width:100%;height:100%;">
			<div id="chartShow" style="width:160px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>
		</div>
	  	<div id="dg_btn">
	  		<div style="margin-bottom: 5px;">
	 			所属作业区：
				<select class="easyui-combobox" style="width:120px;" name="zitem" id="zitem" data-options="editable:false"></select>
				所属班组：
				<select class="easyui-combobox" style="width:120px;" name="bitem" id="bitem" data-options="editable:false"></select>
				焊工编号：
				<input class="easyui-textbox" style="width:60px;" name="welderno" id="welderno"/>
				任务编号：
				<input class="easyui-textbox" style="width:60px;" name="taskno" id="taskno"/>
				时间：
				<input class="easyui-datetimebox" style="width:120px;" name="dtoTime1" id="dtoTime1">--
				<input class="easyui-datetimebox" style="width:120px;" name="dtoTime2" id="dtoTime2">
				<a href="javascript:serach();" class="easyui-linkbutton" iconCls="icon-select" >搜索</a>
				<a href="javascript:exportExcel();" class="easyui-linkbutton" iconCls="icon-export">导出</a>
			</div>
		</div>
	    <table id="dg" style="table-layout: fixed; width:100%;"></table>
    </div>
  </body>
</html>
