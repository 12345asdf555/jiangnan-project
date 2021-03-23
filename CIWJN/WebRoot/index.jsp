<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>云智能焊接管控系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="icon" href="resources/images/title.ico" type="img/x-icon" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/common.css">
	<link rel="stylesheet" type="text/css" href="resources/css/iconfont.css">
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/index.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/indexFauit.js"></script> -->
<!-- 	<script type="text/javascript" src="resources/js/indexFauit2.js"></script> -->
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<style type="text/css">
		a{text-decoration:none;color:inherit;outline:none;}
	</style>
  </head>

  <body class="easyui-layout">
  	<!-- 头部 -->
  	<div region="north" style="height: 98px;" id="north">
		<div class="head-wrap">
			<a href="" class="logo"><img src="resources/images/weldmeslog.png" /></a>
			<div class="time">
				<a href="void(0)" id="headtime" style="color:#fff;"></a>
			</div>	
			<div class="search-wrap">
				<a href="void(0)" id="username">欢迎您：</a>&nbsp;|
                <img src="resources/images/1_06.png" />
				<a href="user/logout">注销</a>
			</div>					
		</div>
	</div>
  	
  	<div region="west" hide="true" split="true" title="导航菜单" style="width: 200px;" id="west" data-options="iconCls:'icon-navigation'">
	  	<div class="easyui-accordion" border="false" id="accordiondiv"></div>
	</div>
    
	<div id="mainPanle" region="center" style="background: white; overflow-y: hidden;width:400px">
		<div id="tabs" class="easyui-tabs" fit="true" border="false"></div>
		<div id="tabMenu" class="easyui-menu" style="width:150px">
			<div id="refreshtab">刷新</div>
			<div id="closetab">关闭标签页</div>
			<div id="closeLeft">关闭左侧标签页</div>
			<div id="closeRight">关闭右侧标签页</div>
			<div id="closeOther">关闭其他标签页</div>
			<div id="closeAll">关闭全部标签页</div>
	    </div>
	</div>
	
	<div data-options="region:'south',split:true" style="height:40px;">
   		<div class="tenghan-bottom">
<!--    			<div id="fauitContent"><div id="content"></div></div> -->
			<div style="float:left;color:#fff;width:23%;" id="bottomlog">
				&nbsp;
			</div>
			<div style="float:left;color:#fff;width:53%;height:55px;position: relative;" id="bottomlog">
				<div style="float:left;width:640px;margin: auto;left:0;right:0;position: absolute;text-align:center;">
					<span style="float:right;padding-top:15px;">
						&nbsp;&nbsp;&nbsp;&nbsp;重准备 强协调 控过程&nbsp;&nbsp;|&nbsp;&nbsp;抓策划 解扣子 接地气&nbsp;&nbsp;|&nbsp;&nbsp;想明白 写下来 照着干
					</span>
					<span style="float:right;padding-top:15px;font-size:16px;">改革创新  提质增效</span>
				</div>
			</div>
			<div style="float:left;color:#fff;width:23%;text-align:right;padding-top:15px;" id="bottomlog">
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-msg" style="border: none;">消息</a>
			</div>
		</div>
	</div>

  </body>
</html>