<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>执行任务修改</title>
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
	<script type="text/javascript" src="resources/js/weldingtask/taskresult.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/addedittaskresult.js"></script>
	<script type="text/javascript" src="resources/js/weldingtask/removetaskresult.js"></script>
	
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
		</div>
		<div style="float:right"><span class="required"></span>班组信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="easyui-textbox" id="XXX"  name="XXX" value="${userinsframework}" readonly="readonly"/></div>
	</div>
  	<div id="body">
  		<div id="load" style="width:100%;height:100%;"></div>
  		<input id="userid"  name="userid" value="${userid}" type="hidden"/>
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
	    <!-- 选择任务编号-->
			<div id="fdlg" class="easyui-dialog" style="width: 650px; height: 450px;" title="选择任务编号" closed="true" buttons="#fdlg-buttons">
    			<table id="dg" style="table-layout: fixed; width:100%;"></table>
			</div>
			<div id="fdlg-buttons">
				<a href="javascript:saveWeldingMachine();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
				<a href="javascript:$('#fdlg').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
			</div>
			<!-- 选择焊机编号-->
			<div id="ddlg" class="easyui-dialog" style="width: 650px; height: 450px;" title="选择任务编号" closed="true" buttons="#ddlg-buttons">
					<input type="radio"  id ="rd1" name="radios" value="1"  checked="checked"/> <span class="required">*部门焊机编号</span>
					<input type="radio"  id ="rd2" name="radios" value="2"  /><span class="required">*所有焊机编号</span>
<!-- 					<input type="checkbox" name="checkbox" value="1" id="checkbox"   OnClick="Chgform"><Span ID="CheckBox" ><span class="required">*所有焊机编号</Span> -->
<!-- 					<Input type="CheckBox"  name="checkbox" id="checkbox1" value="2" checked="checked" OnClick="Chgform1"><Span ID="CheckBox1_Span"><span class="required">*部门焊机编号</Span> -->
    				<div id="egdiv"><table id="wg" style="table-layout: fixed; width:100%;"></table></div>
    				<div id="wediv"><table id="we" style="table-layout: fixed; width:100%;"></table></div>
			</div>
			<div id="ddlg-buttons">
				<a href="javascript:saveWeldingnumber();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
				<a href="javascript:$('#ddlg').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
			</div>
		<!-- 添加修改 -->
		<div id="dlg" class="easyui-dialog" style="width: 500px; height: 450px; padding:3px 6px" closed="true" buttons="#dlg-buttons">
			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true">
				<div align="center">
				<div class="fitem" >
					<lable><span class="required">*</span>焊工编号:</lable>
						<input type="text" id="welderNo" name="welderNo" size="20" readonly="readonly" style="border-left:0px;border-top:0px;border-right:0px;border-bottom:1px ">
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>  任务编号</lable>
						<input type="hidden" id="oldno" />
						<input type="hidden" id="taskid" />
						<input class="easyui-textbox" id="taskNo"  name="taskNo" data-options="validType:['wjNoValidate'],required:true" readonly="readonly" />
						<a href="javascript:selectMachine();" class="easyui-linkbutton" data-options="iconCls:'icon-large-picture',size:'large',iconAlign:'top'">选择任务编号</a>
				</div>
				<div class="fitem" style="margin-left: -100px;" >
					<lable><span class="required">*</span>焊机编号</lable>
						<input type="hidden" id="machineid" />
						<input class="easyui-textbox" id="machineNo"  name="machineNo" data-options="required:true,editable:false">
<!-- 					<a href="javascript:selectMachinenumber();" class="easyui-linkbutton" id="selectMachinenumber">选择焊机</a> -->
						<a href="javascript:selectMachinenumber();" class="easyui-linkbutton" data-options="iconCls:'icon-large-clipart',size:'large',iconAlign:'top'">选择焊机</a>
				</div>
<!-- 				<div> -->
<!-- 					<lable><span class="required">*</span>说明</lable> -->
<!--           			<textarea name="text" id="text" style="height:60px;width:150px"></textarea> -->
<!--        			 </div>   -->
<!-- 				</div> -->
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveedit();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		<!-- 删除 -->
		<div id="rdlg" class="easyui-dialog" style="width: 450px; height: 400px; padding:3px 6px" closed="true" buttons="#remove-buttons">
			<form id="rfm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
			<div align="center">
				<div class="fitem">
					<lable><span class="required">*</span>任务编号</lable>
					<input type="hidden" id="oldno" />
					<input class="easyui-textbox" id="taskid"  name="taskNo" data-options="required:true" readonly="readonly" />
				</div>
<!-- 				<div class="fitem">
					<lable><span class="required">*</span>焊工编号</lable>
					<input class="easyui-textbox" id="welderid"  name="welderNo" data-options="editable:false"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>焊机编号</lable>
					
					<input class="easyui-textbox" id="machineNo"  name="machineNo" data-options="editable:false"></select>
				</div> -->
				</div>
			</form>
		</div>
		<div id="remove-buttons">
			<a href="javascript:remove();" class="easyui-linkbutton" iconCls="icon-ok">任务取消</a>
			<a href="javascript:close2();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		</div>
		<style type="text/css">
		    #load{ display: none; position: absolute; left:0; top:0;width: 100%; height: 40%; background-color: #555753; z-index:10001; -moz-opacity: 0.4; opacity:0.5; filter: alpha(opacity=70);}
			#show{display: none; position: absolute; top: 45%; left: 45%; width: 180px; height: 5%; padding: 8px; border: 8px solid #E8E9F7; background-color: white; z-index:10002; overflow: auto;}
		</style>
</body>
</html>