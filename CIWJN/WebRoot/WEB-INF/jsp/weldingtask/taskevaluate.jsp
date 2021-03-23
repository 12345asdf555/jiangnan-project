<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>任务确认评价</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/taskevaluate.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/weldingtask/addedittaskresult.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/removetaskresult.js"></script> -->
	
  </head>
  <body>
 	<div class="functiondiv">
		<div style="float:left;">
 			所属作业区：
			<select class="easyui-combobox" name="zitem" id="zitem" data-options="editable:false"></select>
			所属班组：
			<select class="easyui-combobox" name="bitem" id="bitem" data-options="editable:false"></select>
			任务状态：
			<select class="easyui-combobox" name="status" id="status" data-options="editable:false">
				<option value="999">请选择</option>
				<option value="1">已完成</option>
				<option value="0">进行中</option>
			</select>
			<a href="javascript:complete();" class="easyui-linkbutton" iconCls="icon-ok">批量完成</a>
		</div>
		 <div style="float:right;"><span class="required"></span>班组信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <input class="easyui-textbox" id="XXX"  name="XXX" value="${userinsframework}" readonly="readonly"/></div>
	</div>
  	<div id="body">
  		<div id="load" style="width:100%;height:100%;"></div>
  		
	    <table id="weldTaskTable" style="table-layout: fixed; width:100%;"></table>
	    	<!-- 选择任务编号-->
			<div id="fdlg" class="easyui-dialog" style="width: 650px; height: 450px;" title="选择任务编号" closed="true" buttons="#fdlg-buttons">
    			<table id="dg" style="table-layout: fixed; width:100%;"></table>
			</div>
			<div id="fdlg-buttons">
				<a href="javascript:saveWeldingMachine();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
				<a href="javascript:dlgclose('fdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
			</div> 
		<!-- 确认完成-->
			<div id="sdlg" class="easyui-dialog" style="width: 650px; height: 450px;" title="任务状态更改" closed="true" buttons="#sdlg-buttons">
    			<table id="weg" style="table-layout: fixed; width:100%;"></table>
			</div>
			<div id="sdlg-buttons">
				<a href="javascript:saveWeldingnumber();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
				<a href="javascript:dlgclose('sdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
			</div>
		<!--评价 -->
		<div id="mdlg" class="easyui-dialog" style="width: 380px; height: 400px; padding:3px 6px" closed="true" buttons="#mdlg-buttons">
			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true"> 
				<div class="fitem">
					<lable><span class="required">*</span>评价等级</lable>
					<select class="easyui-combobox" id="resultid"  name="resultid" data-options="required:true,editable:false"></select>
				</div>
				<div class="fitem">
          			<lable>评价</lable>
          			<textarea name="result" id="result" style="height:60px;width:150px"></textarea>
       			</div> 
			</form>
		</div>
		<div id="mdlg-buttons">
			<a href="javascript:saveconment();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:dlgclose('mdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		</div>
		<style type="text/css">
		    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
			#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
		</style>
  </body>
</html>