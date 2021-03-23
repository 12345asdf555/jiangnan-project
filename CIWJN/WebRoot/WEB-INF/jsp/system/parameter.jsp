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
    
    <title>系统参数</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/system/para.js"></script>
	<style type="text/css">
		.functiondiv .textbox-text{
			width:250px;
		}
		.textbox-text{
			width:60px;
		}
		.leftTd{
			text-align: right;
		}
		fieldset{
			border:1px solid green;
		}
		legend{
			color:green;padding-left:20px;padding-right:20px;
		}
		table{
			font-size:12px;
		}
	</style>
  </head>
  
  <body>
  	<form id="fm" method="post" data-options="novalidate:true">
	  	<div class="functiondiv">
			<div>
				<lable>公司名称：</lable>
		        <input name="id" id="id" class="easyui-textbox">
			    <input name="companyName" id="companyName" style="width:250px;" class="easyui-textbox" data-options="required:true">
			</div>
		</div>
		<div id="body">
			<div style="float:left;width:65%;font-size: 12px;">
				<fieldset>
					<legend>停机设置</legend>
					<div>
						<table>
							<tr>
								<td class="leftTd" width="150">监测电流电压限定值有效：</td>
								<td width="40"><input name="term1" id="term1" style="width:30px;" type="checkbox" value="0"/></td>
								<td class="leftTd" width="100">超限停机有效：</td>
								<td width="40"><input name="term2" id="term2" style="width:30px;" type="checkbox" value="1"/></td>
								<td width="60"></td>
							</tr>
							<tr>
								<td class="leftTd">基础停机时间：</td>
								<td colspan="4">
									<input name="hour1" id="hour1" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>时</label>
							    	<input name="minute1" id="minute1" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>分</label>
							    	<input name="second1" id="second1" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>秒</label>
								</td>
							</tr>
							<tr>
								<td class="leftTd">最大停机浮动时间：</td>
								<td colspan="4">
									<input name="hour2" id="hour2" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>时</label>
							    	<input name="minute2" id="minute2" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>分</label>
							    	<input name="second2" id="second2" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>秒</label>
								</td>
							</tr>
							<tr><td colspan="5"></td></tr>
						</table>
		    		</div>
				</fieldset>
			</div>
			<div style="float:left;width:30%;margin-left:5px;">
				<fieldset>
					<legend>班制设置</legend>
					<div>
						<table>
							<tr>
								<td class="leftTd" width="150">白班时间：</td>
								<td>
									<input name="day" id="day" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
								</td>
							</tr>
							<tr>
								<td class="leftTd">中班时间：</td>
								<td>
									<input name="after" id="after" style="width:60px;" class="easyui-textbox"   data-options="required:true"/>
								</td>
							</tr>
							<tr>
								<td class="leftTd">晚班时间：</td>
								<td>
									<input name="night" id="night" style="width:60px;" class="easyui-textbox"   data-options="required:true"/>
								</td>
							</tr>
							<tr><td colspan="2"></td></tr>
						</table>
		    		</div>
				</fieldset>
			</div>
			<div style="float:left;width:65%;margin-top:5px;">
				<fieldset>
					<legend>超限设置</legend>
					<div>
						<table>
							<tr>
								<td class="leftTd" width="150">连续超限时间：</td>
								<td>
									<input name="hour1" id="hour3" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>时</label>
							    	<input name="minute1" id="minute3" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>分</label>
							    	<input name="second1" id="second3" style="width:60px;" class="easyui-textbox" data-options="required:true"/>
							    	<label>秒</label>
								</td>
							</tr>
							<tr>
								<td class="leftTd">最大超限次数：</td>
								<td>
									<input name="times" id="times" style="width:60px;" class="easyui-textbox" data-options="required:true"/>次
								</td>
							</tr>
							<tr><td colspan="2"></td></tr>
						</table>
		    		</div>
				</fieldset>
			</div>
			<div style="float:left;width:65%;margin-top:5px;">
				<fieldset>
					<legend>焊丝线密度</legend>
					<div>
						<table>
							<tr>
								<td>
									Φ0.8：<input name="eight" id="eight" style="width:60px;" class="easyui-textbox"  data-options="required:true"/>Kg/m
									<span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
									Φ1.0：<input name="one" id="one" style="width:60px;" class="easyui-textbox" data-options="required:true"/>Kg/m
		    						<span>&nbsp;&nbsp;|&nbsp;&nbsp;</span> 
		    						Φ1.2：<input name="two" id="two" style="width:60px;" class="easyui-textbox" data-options="required:true"/>Kg/m
		    						<span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
		    						Φ1.6：<input name="six" id="six" style="width:60px;" class="easyui-textbox" data-options="required:true"/>Kg/m
								</td>
							</tr>
							<tr><td></td></tr>
						</table>
		    		</div>
				</fieldset>
			</div>
			<div style="float:left;width:65%;margin-top:5px;">
				<fieldset>
					<legend>估算信息</legend>
					<div>
						<table>
							<tr>
								<td class="leftTd" width="150">气流量：</td>
								<td>
									<input name="airflow" id="airflow" style="width:60px;" class="easyui-textbox" style="width:10px;" data-options="required:true"/>L/min
								</td>
							</tr>
							<tr>
								<td class="leftTd">送丝速度：</td>
								<td>
									<input name="speed" id="speed" style="width:60px;" class="easyui-textbox"   data-options="required:true"/>m/min
								</td>
							</tr>
							<tr>
								<td class="leftTd">待机功率：</td>
								<td>
									<input name="wait" id="wait" style="width:60px;" class="easyui-textbox"   data-options="required:true"/>w
								</td>
							</tr>
							<tr>
								<td class="leftTd">焊接功率=</td>
								<td>焊接电流*焊接电压*n,功率因子 n=
									<input name="weld" id="weld" style="width:60px;" class="easyui-textbox"   data-options="required:true"/>
								</td>
							</tr>
							<tr><td colspan="2"></td></tr>
						</table>
		    		</div>
				</fieldset>
			</div>
			
			<div style="float:left;text-align:right;width:100%;margin-top:50px;">
		    	<a href="javascript:savePara();" class="easyui-linkbutton" iconCls="icon-savewps" style="border:1px solid #cbc9c9;">保存</a>
		    </div>
		</div>
	</form>
  </body>
</html>
