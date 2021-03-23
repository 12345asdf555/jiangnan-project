<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>工序计划管理</title>
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
	
	<script type="text/javascript" src="resources/js/weldingtask/json2.js"></script>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script> -->
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/weldtask.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/addeditweldtask.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/removeweldtask.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/print.js"></script>
	
  </head>
  
  <body>
  	<div class="functiondiv">
		<div>
		 	所属作业区：
			<select class="easyui-combobox" name="zitem" id="zitem" data-options="editable:false"></select>
			所属班组：
			<select class="easyui-combobox" name="bitem" id="bitem" data-options="editable:false"></select>
			任务状态：
			<select class="easyui-combobox" name="status" id="status" data-options="editable:false">
				<option value="999">请选择</option>
				<option value="1">已完成</option>
				<option value="0">进行中</option>
				<option value="3">未领取</option>
			</select>
			<a href="javascript:addWeldedjunction();" class="easyui-linkbutton" iconCls="icon-newadd">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-import">导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:exportDg();" class="easyui-linkbutton" iconCls="icon-export">导出</a>&nbsp;&nbsp;&nbsp;&nbsp;	
			<a href="javascript:openDayin();" class="easyui-linkbutton" iconCls="icon-print">打印</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:insertsearchWT();" class="easyui-linkbutton" iconCls="icon-select" >查找</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:complete();" class="easyui-linkbutton" iconCls="icon-ok">批量完成</a>
			<a href="javascript:openDeleteDialog();" class="easyui-linkbutton" iconCls="icon-ok">批量删除</a>
			<!-- <label><input name="hideFinished" id="hideFinished" type="checkbox" style="width: 20px" checked="true"/>隐藏已完成任务</label> -->
		</div>
	</div>
  	<div id="body">
	  	<input id="userinsall"  name="userinsall" value="${userinsall}" type="hidden" />
		<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
			<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data"> 
				<div>
					<span><input type="file" name="file" id="file"></span>
					<input type="button" value="上传" onclick="importWeldingMachine()" class="upButton"/>
				</div>
			</form>
		</div>
	    <table id="weldTaskTable" style="table-layout: fixed; width:100%;"></table>
	    
	    <!-- 自定义多条件查询 -->
	    <div id="searchdiv" class="easyui-dialog" style="width:800px; height:400px;" closed="true" buttons="#searchButton" title="自定义条件查询">
	    	<div id="div0">
		    	<select class="fields" id="fields"></select>
		    	<select class="condition" id="condition"></select>
		    	<input class="content" id="content"/>
		    	<select class="joint" id="joint"></select>
		    	<a href="javascript:newSearchWT();" class="easyui-linkbutton" iconCls="icon-add"></a>
		    	<a href="javascript:removeSerach();" class="easyui-linkbutton" iconCls="icon-remove"></a>
	    	</div>
	    </div>
	    <div id="searchButton">
			<a href="javascript:searchWT();" class="easyui-linkbutton" iconCls="icon-ok">查询</a>
			<a href="javascript:close();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
		</div>
		<!-- 添加修改 -->
		<div id="dlg" class="easyui-dialog" style="width: 400px; height: 400px; padding:3px 6px" closed="true" buttons="#dlg-buttons">
			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>任务编号</lable>
					<input type="hidden" id="oldno" />
					<input class="easyui-textbox" id="weldedJunctionno"  name="weldedJunctionno" data-options="validType:['wjNoValidate'],required:true" />
				</div>
				<div class="fitem">
					<lable><span class="required"></span>任务等级</lable>
					<select class="easyui-combobox" id="levelid"  name="levelid" data-options="editable:false"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>所属班组</lable>
					<select class="easyui-combobox" id="itemid"  name="itemid" data-options="required:true,editable:false"></select>
				</div>
<!-- 				<div class="fitem" style="padding-right:30px">
					<lable><span class="required"></span>预设焊工</lable>
					<input id="welderid" name="welderid" type="hidden"/>
					<input class="easyui-textbox" id="pipelineNo" name="pipelineNo" readonly="readonly"/>
					<a href="javascript:selectWelder();" class="easyui-linkbutton">选择</a>
				</div>
				<div class="fitem">
					<lable><span class="required"></span>焊工资质</lable>
					<select class="easyui-combobox" id="quali"  name="quali" data-options="editable:false"></select>
				</div> -->
				<div class="fitem">
					<lable><span class="required"></span>计划开始时间</lable>
					<input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1">
				</div>
				<div class="fitem">
					<lable><span class="required"></span>计划结束时间</lable>
					<input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
				</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:save();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		<!-- 导入检测表格 -->
		<div id="exportdlg" class="easyui-dialog" style="width: 800px; height: 400px; padding:3px 6px" closed="true" buttons="#exportdlg-buttons">
			<table id="exporttable" style="table-layout: fixed; width:100%;"></table>
		</div>
		<div id="exportdlg-buttons">
	      <a id="imexcel" href="javascript:saveExportdlg();" class="easyui-linkbutton" iconCls="icon-ok">确认导入</a>
	      <a href="javascript:closeExportdlg()" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	    </div>
		
		<!-- 选择焊工 -->
	    <div id="fdlg" class="easyui-dialog" style="width: 700px; height: 530px;" title="选择焊工" closed="true" buttons="#fdlg-buttons">
	      <div id="fdlgSearch">
	        焊工编号：<input class="easyui-textbox" id="searchname"/>
	        <a href="javascript:dlgSearchGather();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	        <a href="javascript:cancelWelder();" class="easyui-linkbutton" iconCls="icon-search">取消焊工选择</a>
	      </div>
	        <table id="welderTable" style="table-layout: fixed; width:100%;"></table>
	    </div>
	    <div id="fdlg-buttons">
	      <a href="javascript:saveWelder();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	      <a href="javascript:closeFdlog()" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	    </div>
	    
	    <!-- 打印 -->
	    <div id="dayin" class="easyui-dialog" style="width: 700px; height: 530px;" title="表格打印" closed="true" buttons="#dayin-buttons">
	    	<table id="dayintable" style="table-layout: fixed; width:100%;"></table>
	    </div>
	    <div id="dayin-buttons">
	      <a href="javascript:printWeldedjunction();" class="easyui-linkbutton" iconCls="icon-ok">打印</a>
	      <a href="javascript:closeDayin()" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	    </div>
	    
		<!-- 删除 -->
		<div id="rdlg" class="easyui-dialog" style="width: 400px; height: 400px; padding:3px 6px" closed="true" buttons="#remove-buttons">
			<form id="rfm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
				<div class="fitem">
					<lable><span class="required">*</span>任务编号</lable>
					<input class="easyui-textbox" id="weldedJunctionno"  name="weldedJunctionno" readonly="readonly" />
				</div>
				<div class="fitem">
					<lable><span class="required"></span>任务等级</lable>
					<input class="easyui-textbox" id="levelname"  name="levelname" readonly="readonly">
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>所属班组</lable>
					<input class="easyui-textbox" id="itemname"  name="itemname" readonly="readonly">
				</div>
<!-- 				<div class="fitem">
					<lable><span class="required"></span>预设焊工</lable>
					<input class="easyui-textbox" id="pipelineNo" name="pipelineNo" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable><span class="required"></span>焊工资质</lable>
					<input class="easyui-textbox" id="roomNo"  name="roomNo" readonly="readonly">
				</div> -->
				<div class="fitem">
					<lable><span class="required"></span>计划开始时间</lable>
					<input class="easyui-textbox" name="dtoTime1" id="dtoTime1" readonly="readonly">
				</div>
				<div class="fitem">
					<lable><span class="required"></span>计划结束时间</lable>
					<input class="easyui-textbox" name="dtoTime2" id="dtoTime2" readonly="readonly">
				</div>
			</form>
		</div>
		<div id="remove-buttons">
			<a href="javascript:remove();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:close2();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!--评价 -->
		<div id="mdlg" class="easyui-dialog" style="width: 380px; height: 400px; padding:3px 6px" closed="true" buttons="#mdlg-buttons">
			<form id="mfm" class="easyui-form" method="post" data-options="novalidate:true"> 
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
			<a href="javascript:closeDialog('mdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 批量完成 -->
		<div id="sdlg" class="easyui-dialog" style="width: 500px; height: 450px;" title="任务状态更改" closed="true" buttons="#sdlg-buttons">
   			<table id="weg" style="table-layout: fixed; width:100%;"></table>
		</div>
		<div id="sdlg-buttons">
			<a href="javascript:saveWeldingnumber();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:closeDialog('sdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 批量删除 -->
	    <div id="bdt" class="easyui-dialog" style="width: 920px; height: 530px;" title="批量删除" closed="true" buttons="#cdt-buttons">
	    	<table id="batchDeleteTable" style="table-layout: fixed; width:100%;"></table>
	    </div>
	    <div id="cdt-buttons">
	      <a href="javascript:batchDelete();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
	      <a href="javascript:closeDialog('bdt')" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	    </div>
		
		<div id="load" style="width:100%;height:100%;"></div>
	</div>
	<style type="text/css">
	    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
		#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
	</style>
  </body>
</html>
