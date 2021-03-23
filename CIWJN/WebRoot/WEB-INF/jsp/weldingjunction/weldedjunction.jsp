<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>任务管理</title>
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
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/weldedjunction/weldedjunction.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/weldedjunction/addeditweldedjunction.js"></script>
	<script type="text/javascript" src="resources/js/weldedjunction/removeweldedjunction.js"></script>
	
  </head>
  
  <body>
  	<div class="functiondiv">
		<div>
			<a href="javascript:addWeldedjunction();" class="easyui-linkbutton" iconCls="icon-newadd">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:importclick();" class="easyui-linkbutton" iconCls="icon-import">导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:insertsearchWJ();" class="easyui-linkbutton" iconCls="icon-select" >查找</a>
		</div>
	</div>
  	<div id="body">
		<div id="importdiv" class="easyui-dialog" style="width:300px; height:200px;" closed="true">
			<form id="importfm" method="post" class="easyui-form" data-options="novalidate:true" enctype="multipart/form-data"> 
				<div>
					<span><input type="file" name="file" id="file"></span>
					<input type="button" value="上传" onclick="importWeldingMachine()" class="upButton"/>
				</div>
			</form>
		</div>
	    <table id="weldedJunctionTable" style="table-layout: fixed; width:100%;"></table>
	    
	    <!-- 自定义多条件查询 -->
	    <div id="searchdiv" class="easyui-dialog" style="width:800px; height:400px;" closed="true" buttons="#searchButton" title="自定义条件查询">
	    	<div id="div0">
		    	<select class="fields" id="fields"></select>
		    	<select class="condition" id="condition"></select>
		    	<input class="content" id="content"/>
		    	<select class="joint" id="joint"></select>
		    	<a href="javascript:newSearchWJ();" class="easyui-linkbutton" iconCls="icon-add"></a>
		    	<a href="javascript:removeSerach();" class="easyui-linkbutton" iconCls="icon-remove"></a>
	    	</div>
	    </div>
	    <div id="searchButton">
			<a href="javascript:searchWJ();" class="easyui-linkbutton" iconCls="icon-ok">查询</a>
			<a href="javascript:close();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
		</div>
		<!-- 添加修改 -->
		<div id="dlg" class="easyui-dialog" style="width: 850px; height: 600px; padding:10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>编号</lable>
					<input type="hidden" id="oldno" />
					<input class="easyui-textbox" id="weldedJunctionno"  name="weldedJunctionno" data-options="validType:['wjNoValidate','length[1,6]'],required:true" />
					<lable>机组</lable>
					<input class="easyui-textbox" id="unit" name="unit"/>
				</div>
				<div class="fitem">
					<lable>区域</lable>
					<input class="easyui-textbox" id="area" name="area"/>
					<lable>规格</lable>
					<input class="easyui-textbox" id="specification" name="specification"/>
				</div>
				<div class="fitem">
					<lable>系统</lable>
					<input class="easyui-textbox" id="systems" name="systems"/>
					<lable>子项</lable>
					<input class="easyui-textbox" id="children" name="children"/>
				</div>
				<div class="fitem">
					<lable>管线号</lable>
					<input class="easyui-textbox" id="pipelineNo"  name="pipelineNo" />
					<lable>房间号</lable>
					<input class="easyui-textbox" id="roomNo" name="roomNo"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>序列号</lable>
					<input class="easyui-textbox" id="serialNo" name="serialNo" data-options="required:true"/>
					<lable><span class="required">*</span>所属项目</lable>
					<select class="easyui-combobox" id="itemid"  name="itemid" data-options="required:true,editable:false"></select>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>上游外径</lable>
					<input class="easyui-textbox" id="externalDiameter" name="externalDiameter"  data-options="required:true"/>
					<lable><span class="required">*</span>下游外径</lable>
					<input class="easyui-textbox" id="nextexternaldiameter" name="nextexternaldiameter"  data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>上游璧厚</lable>
					<input class="easyui-textbox" id="wallThickness" name="wallThickness" data-options="required:true"/>
					<lable><span class="required">*</span>下游璧厚</lable>
					<input class="easyui-textbox" id="nextwall_thickness" name="nextwall_thickness" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>上游材质</lable>
					<input class="easyui-textbox" id="material"  name="material" data-options="required:true"/>
					<lable><span class="required">*</span>下游材质</lable>
					<input class="easyui-textbox" id="next_material" name="next_material" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电流上限</lable>
					<input class="easyui-numberbox"  min="0.001" precision="3"  id="maxElectricity" name="maxElectricity" data-options="required:true"/>
					<lable><span class="required">*</span>电流下限</lable>
					<input class="easyui-numberbox"  min="0.001" precision="3"  id="minElectricity" name="minElectricity" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电压上限</lable>
					<input class="easyui-numberbox"  min="0.001" precision="3"  id="maxValtage" name="maxValtage" data-options="required:true"/>
					<lable><span class="required">*</span>电压下限</lable>
					<input class="easyui-numberbox"  min="0.001" precision="3"  id="minValtage"  name="minValtage" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>电流单位</lable>
					<input class="easyui-textbox" id="electricity_unit"  name="electricity_unit" data-options="required:true"/>
					<lable><span class="required">*</span>电压单位</lable>
					<input class="easyui-textbox" id="valtage_unit"  name="valtage_unit" data-options="required:true"/>
				</div>
				<div class="fitem">
					<lable>开始时间</lable>
					<input class="easyui-datetimebox" id="startTime"  name="startTime"/>
					<lable>完成时间</lable>
					<input class="easyui-datetimebox" id="endTime"  name="endTime"/>
				</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:save();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除 -->
		<div id="rdlg" class="easyui-dialog" style="width: 850px; height: 600px; padding:10px 20px" closed="true" buttons="#remove-buttons">
			<form id="rfm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
				<div class="fitem">
					<lable>编号</lable>
					<input class="easyui-textbox" id="weldedJunctionno"  name="weldedJunctionno"  readonly="readonly"/>
					<lable>机组</lable>
					<input class="easyui-textbox" id="unit" name="unit"  readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>区域</lable>
					<input class="easyui-textbox" id="area" name="area"  readonly="readonly"/>
					<lable>规格</lable>
					<input class="easyui-textbox" id="specification" name="specification"  readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>系统</lable>
					<input class="easyui-textbox" id="systems" name="systems"  readonly="readonly"/>
					<lable>子项</lable>
					<input class="easyui-textbox" id="children" name="children"  readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>管线号</lable>
					<input class="easyui-textbox" id="pipelineNo"  name="pipelineNo"   readonly="readonly"/>
					<lable>房间号</lable>
					<input class="easyui-textbox" id="roomNo" name="roomNo"  readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>序列号</lable>
					<input class="easyui-textbox" id="serialNo" name="serialNo"  readonly="readonly"/>
					<lable>所属项目</lable>
					<select class="easyui-textbox" id="itemname"  name="itemname" readonly="readonly"></select>
				</div>
				<div class="fitem">
					<lable>上游外径</lable>
					<input class="easyui-textbox" id="externalDiameter" name="externalDiameter" readonly="readonly"/>
					<lable>下游外径</lable>
					<input class="easyui-textbox" id="nextexternaldiameter" name="nextexternaldiameter" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>上游璧厚</lable>
					<input class="easyui-textbox" id="wallThickness" name="wallThickness" readonly="readonly"/>
					<lable>下游璧厚</lable>
					<input class="easyui-textbox" id="nextwall_thickness" name="nextwall_thickness" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>上游材质</lable>
					<input class="easyui-textbox" id="material"  name="material" readonly="readonly"/>
					<lable>下游材质</lable>
					<input class="easyui-textbox" id="next_material" name="next_material" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>电流上限</lable>
					<input class="easyui-textbox" id="maxElectricity" name="maxElectricity" readonly="readonly"/>
					<lable>电流下限</lable>
					<input class="easyui-textbox" id="minElectricity" name="minElectricity" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>电压上限</lable>
					<input class="easyui-textbox" id="maxValtage" name="maxValtage" readonly="readonly"/>
					<lable>电压下限</lable>
					<input class="easyui-textbox" id="minValtage"  name="minValtage" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>电流单位</lable>
					<input class="easyui-textbox" id="electricity_unit"  name="electricity_unit" readonly="readonly"/>
					<lable>电压单位</lable>
					<input class="easyui-textbox" id="valtage_unit"  name="valtage_unit" readonly="readonly"/>
				</div>
				<div class="fitem">
					<lable>开始时间</lable>
					<input class="easyui-textbox" id="startTime"  name="startTime"readonly="readonly"/>
					<lable>完成时间</lable>
					<input class="easyui-textbox" id="endTime"  name="endTime" readonly="readonly"/>
				</div>
			</form>
		</div>
		<div id="remove-buttons">
			<a href="javascript:remove();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:close2();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
	</div>
  </body>
</html>
