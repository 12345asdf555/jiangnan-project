<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户管理</title>
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
	<script type="text/javascript" src="resources/js/insframework/insframeworktree.js"></script>
	<script type="text/javascript" src="resources/js/user/alluser.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/user/addedituser.js"></script>

  </head>
  
<body class="easyui-layout">
  	<jsp:include  page="../insframeworktree.jsp"/>
  	<div  id="bodys" region="center"  hide="true"  split="true" >
	    <div class="functiondiv">
	        <div>
	        	<a href="javascript:saveUser();" class="easyui-linkbutton" iconCls="icon-newadd">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
	        	<a href="javascript:insertSearchUser();" class="easyui-linkbutton" iconCls="icon-select">查找</a>   
	    	</div>
	    </div>
  		<div id="body">
	        <table id="dg" style="table-layout:fixed;width:100%"></table>
	    </div>
	    <div id="div1" class="easyui-dialog" style="width:400px;height:400px" closed="true" buttons="#dlg-ro"algin="center">
	        <table id="ro" style="table-layout:fixed;width:100%;" ></table>
        </div>
		<div id="dlg-ro">
			<a href="javascript:$('#div1').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
    	<!-- 自定义多条件查询 -->
	    <div id="searchdiv" class="easyui-dialog" style="width:800px; height:400px;" closed="true" buttons="#searchButton" title="自定义条件查询">
	    	<div id="div0">
		    	<select class="fields" id="fields"></select>
		    	<select class="condition" id="condition"></select>
		    	<input class="content" id="content"/>
		    	<select class="joint" id="joint"></select>
		    	<a href="javascript:newSearchUser();" class="easyui-linkbutton" iconCls="icon-add"></a>
		    	<a href="javascript:removeSerachByUser();" class="easyui-linkbutton" iconCls="icon-remove"></a>
	    	</div>
	    </div>
	    <div id="searchButton">
			<a href="javascript:searchUser();" class="easyui-linkbutton" iconCls="icon-ok">查询</a>
			<a href="javascript:close();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
		</div>
		
		<!-- 添加修改 -->
		<div id="dlg" class="easyui-dialog" style="width: 700px; height: 70%; padding:10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
				<div class="fitem" style="padding-right:100px;">
	            	<lable style="width: 200px;"><span class="required">*</span>用户名</lable>
	                <input name="userName" id="userName" class="easyui-textbox" data-options="required:true">
	            	<lable style="width: 100px;"><span class="required">*</span>登录名</lable>
	            	<input id="validName" type="hidden" >
	                <input name="userLoginName" class="easyui-textbox" data-options="validType:'userValidate',required:true">
	            </div>
	            <div class="fitem" style="padding-right:100px;">
	            	<lable style="width: 200px;"><span class="required">*</span>密码</lable>
	                <input name="userPassword" type="password" class="easyui-textbox" data-options="required:true">
	            	<lable style="width: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</lable>
	                <input name="userPhone" class="easyui-textbox" data-options="validType:'phoneNum',required:false">
	            </div>
	            <div class="fitem" style="padding-right:100px;">
	            	<lable style="width: 200px;">邮箱</lable>
	                <input name="userEmail" class="easyui-textbox" data-options="validType:'email',required:false" invalidMessage="请输入正确的邮箱">
	            	<lable style="width: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="required">*</span>岗位</lable>
	                <input name="userPosition" class="easyui-textbox" data-options="required:true">
	            </div>
	            <div class="fitem" style="padding-right:100px;">
					<lable style="width: 200px;"><span class="required">*</span>部门</lable>
					<select class="easyui-combobox" name="insid" id="insid" data-options="required:true,editable:false"></select>
	        		<lable style="width: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态</lable>&nbsp;&nbsp;
	   				<span id="radios"></span>
	        	</div>
		        <div align="center">
		        	<table id="tt" name="tt" title="角色列表" checkbox="true" style="table-layout:fixed"></table>
		        </div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:save();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:close1();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除 -->
		<div id="rdlg" class="easyui-dialog" style="width: 700px; height: 70%; padding:10px 20px" closed="true" buttons="#remove-buttons">
			<form id="rfm" class="easyui-form" method="post" data-options="novalidate:true"><br/>
				<div class="fitem">
	                <input name="id" id="id" type="hidden" >
	            </div>
	            <div class="fitem" style="padding-right:120px;">
	            	<lable style="width: 200px;">用户名</lable>
	                <input name="userName" id="userName" class="easyui-textbox" readonly="true">
	            	<lable style="width: 100px;">&nbsp;&nbsp;登录名</lable>
	                <input name="userLoginName" class="easyui-textbox" readonly="true">
	            </div>
	            <div class="fitem" style="padding-right:120px;">
	            	<lable style="width: 200px;">密码</lable>
	                <input name="userPassword" class="easyui-textbox" type="password" readonly="true">
	            	<lable style="width: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</lable>
	                <input name="userPhone" class="easyui-textbox" readonly="true" >
	            </div>
	            <div class="fitem" style="padding-right:120px;">
	            	<lable style="width: 200px;">邮箱</lable>
	                <input name="userEmail" class="easyui-textbox" readonly="true" >
	            	<lable style="width: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;岗位</lable>
	                <input id="userPosition" name="userPosition" class="easyui-textbox" readonly="true">
	            </div>
	            <div class="fitem" style="padding-right:120px;">
	            	<lable style="width: 200px;">部门</lable>
	            	<input class="easyui-textbox" name="users_insframework" id="users_insframework"  readonly="true" />
					<lable style="width: 100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态</lable>
					<input name="status" class="easyui-textbox" readonly="true"/>
	            </div>
		        <div align="center">
			        <table id="rtt" title="角色列表" checkbox="true" readonly="true" style="table-layout:fixed;width:100%"></table>
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
 
 
