<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>工艺管理</title>
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
	<script type="text/javascript" src="resources/js/datagrid-detailview.js" charset="utf-8"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/easyui-extend-check.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/allWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/addeditWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/removeWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/giveWpslib.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/differentMachine.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/control.js"></script>
	<script type="text/javascript" src="resources/js/wpslib/comboboxCheck.js"></script>
	<script type="text/javascript" src="resources/js/getTimeToHours.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<script type="text/javascript" src="resources/js/paho-mqtt.js"></script>
	<script type="text/javascript" src="resources/js/paho-mqtt-min.js"></script>
	<style type="text/css">
		table tr td{
			font-size:12px;
		}
		.leftTd{
			text-align: right;
		}
		.textbox-text{
			width:85px;
		}
	</style>
  </head>
  
  <body>
  	<div class="functiondiv">
		<div>
			<a href="javascript:addWpslib();" class="easyui-linkbutton" iconCls="icon-newadd">新增工艺库</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:openCondlg();" class="easyui-linkbutton" iconCls="icon-newadd">控制命令下发</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:openHistorydlg();" class="easyui-linkbutton" iconCls="icon-newadd"> 下发历史查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:selectMachineList(5);" class="easyui-linkbutton" iconCls="icon-history"> 松下焊机通道锁定</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:selectMachineList(4);" class="easyui-linkbutton" iconCls="icon-reload"> 松下焊机通道解锁</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
	</div>
  	<div id="body" >
	    <table id="wpslibTable" style="table-layout: fixed; width:100%;"></table>

	    <!-- 添加修改工艺库 -->
		<div id="wltdlg" class="easyui-dialog" style="width: 400px; height: 225px; padding:10px 20px" closed="true" buttons="#wltdlg-buttons">
			<form id="wltfm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>工艺库名称</lable>
					<input type="hidden" id="validwl">
					<input class="easyui-textbox" name="wpslibName" id="wpslibName"  data-options="validType:['wpslibValidate'],required:true"/>
				</div>
				<div class="fitem">
					<lable><span class="required">*</span>焊机型号</lable>
					<select class="easyui-combobox" name="model" id="model" data-options="required:true,editable:false""></select>
				</div>
				<div class="fitem">
					<lable>状态</lable>
	   				<span id="radios"></span>
				</div>
			</form>
		</div>
		<div id="wltdlg-buttons">
			<a href="javascript:saveWpslib();" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:closeDialog('wltdlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除工艺库 -->
		<div id="rmwltdlg" class="easyui-dialog" style="width: 400px; height: 170px; padding:10px 20px" closed="true" buttons="#rmwltdlg-buttons">
			<form id="rmwltfm" class="easyui-form" method="post" data-options="novalidate:true">
				<div class="fitem">
					<lable><span class="required">*</span>工艺库名称</lable>
					<input type="hidden" id="validwl">
					<input class="easyui-textbox" name="wpslibName" id="wpslibName"/>
				</div>
				<div class="fitem">
					<lable>状态</lable>
					<input name="status" class="easyui-textbox" readonly="true" />
				</div>
			</form>
		</div>
		<div id="rmwltdlg-buttons">
			<a href="javascript:removeWpslib();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 添加修改工艺 -->
		<div id="mwdlg" class="easyui-dialog" style="width: 900px; height: 510px; padding:10px 20px" closed="true" buttons="#mwdlg-buttons">
			<form id="mwfm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div region="left" style="padding-left:20px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable>通道号：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" id="fchanel" name="fchanel" style="width:85px;" id="chanel" data-options="editable:false">
				                	<option value="1">通道号1</option>
								    <option value="2">通道号2</option>
								    <option value="3">通道号3</option>
								    <option value="4">通道号4</option>
								    <option value="5">通道号5</option>
								    <option value="6">通道号6</option>
								    <option value="7">通道号7</option>
								    <option value="8">通道号8</option>
								    <option value="9">通道号9</option>
								    <option value="10">通道号10</option>
								    <option value="11">通道号11</option>
								    <option value="12">通道号12</option>
								    <option value="13">通道号13</option>
								    <option value="14">通道号14</option>
								    <option value="15">通道号15</option>
								    <option value="16">通道号16</option>
								    <option value="17">通道号17</option>
								    <option value="18">通道号18</option>
								    <option value="19">通道号19</option>
								    <option value="20">通道号20</option>
								    <option value="21">通道号21</option>
								    <option value="22">通道号22</option>
								    <option value="23">通道号23</option>
								    <option value="24">通道号24</option>
								    <option value="25">通道号25</option>
								    <option value="26">通道号26</option>
								    <option value="27">通道号27</option>
								    <option value="28">通道号28</option>
								    <option value="29">通道号29</option>
								    <option value="30">通道号30</option>
				                </select>
				            </td>
			  				<td></td>
			  				<td></td>
			  			</tr>
	            	</table>
	            </div>
        	    <div style="border:1px solid green;border-radius: 8px;padding:15px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd" width="50"><lable><span class="required">*</span>收弧：</lable></td>
			  				<td class="rightTd" width="90">
			  					<select class="easyui-combobox" style="width:85px;" name="farc" id="farc" data-options="editable:false">
				                	<option value="111">无</option>
								    <option value="112">有</option>
								    <option value="113">反复</option>
								    <option value="114">点焊</option>
				                </select>
			  				</td>
			  				<td class="leftTd" width="90"><lable><span class="required">*</span>一元/个别：</lable></td>
			  				<td class="rightTd" width="90">
			  					<select class="easyui-combobox" name="fselect" style="width:85px;" id="fselect" data-options="editable:false" onChange="changeValue(current,old)">
				                    <option value="102">个别</option>
								    <option value="101">一元</option>
				                </select>
			  				</td>
			  				<td class="leftTd" width="70"><lable>初期条件：</lable></td>
			  				<td class="rightTd" width="20"><input name="finitial" id="finitial" type="checkbox" value="1" style="width:30px;"/></td>
			  				<td class="leftTd" width="70"><lable>熔深控制：</lable></td>
			  				<td class="rightTd" width="20"><input style="width:30px;" name="fcontroller" id="fcontroller" type="checkbox" value="1"/></td>
			  				<td id="dmodel" class="leftTd" width="100"><lable>柔软电弧模式：</lable></td>
			  				<td id="imodel" class="rightTd" width="30"><input style="width:30px;" name="fmode" id="fmode" type="checkbox" value="1"></td>
			  				<td id="dtorch" class="leftTd" width="100" style="display:none"><lable>水冷焊枪：</lable></td>
			  				<td id="itorch" class="rightTd" width="30" style="display:none"><input style="width:30px;" name="ftorch" id="ftorch" type="checkbox" value="0"></td>
			  			</tr>
	            	</table>
	            </div>
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>电弧特性：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" id="fcharacter" name="fcharacter" class="easyui-numberbox">(±1)</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊丝材质：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" style="width:85px;" name="fmaterial" id="fmaterial" data-options="editable:false">
				                	<option value="91">低碳钢实心</option>
								    <option value="92">不锈钢实心</option>
								    <option value="93">低碳钢药芯</option>
								    <option value="94">不锈钢药芯</option>
				                </select>
				            </td>
				            <td class="leftTd" width="70"><lable><span class="required">*</span>提前送气：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" name="fadvance" id="fadvance" class="easyui-numberbox" data-options="precision:1">(0.1s)</td>
			  				<td class="leftTd" width="50"><lable><span class="required">*</span>气体：</lable></td>
			  				<td class="rightTd" width="70">
			  					<select class="easyui-combobox" style="width:85px;" name="fgas" id="fgas" data-options="editable:false">
				                	<option value="121">CO2</option>
								    <option value="122">MAG</option>
								    <option value="123">MIG</option>
				                </select>
				            </td>
			  			</tr>
	            		<tr>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>点焊时间：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" name="ftime" id="ftime" class="easyui-numberbox" data-options="precision:1">(0.1s)</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊丝直径：</lable></td>
			  				<td class="rightTd" width="70">
			  					<select class="easyui-combobox" style="width:85px;" name="fdiameter" id="fdiameter" data-options="editable:false">
				                	<option value="131">Φ1.0</option>
				                	<option value="132">Φ1.2</option>
				                	<option value="133">Φ1.4</option>
				                	<option value="134">Φ1.6</option>
				                </select>
			  				</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>滞后送气：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" name="fhysteresis" id="fhysteresis" class="easyui-numberbox" data-options="precision:1">(0.1s)</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊接过程：</lable></td>
			  				<td class="rightTd" width="70"><select class="easyui-combobox" style="width:85px;" name="fweldprocess" id="fweldprocess" data-options="editable:false"></select>
			  				</td>
		  				</tr>
	            	</table>
	            </div>
	            
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;">
	            	<div>
	            		<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_ele" id="fini_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_ele" id="fweld_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_ele" id="farc_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol" id="fini_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol" id="fweld_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			    <td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol" id="farc_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol1" id="fweld_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol1" id="farc_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol1" id="fini_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  			</tr>
			            	<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_ele" id="fweld_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_ele" id="farc_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<!-- <td class="leftTd" width="120"><lable><span class="required">*</span>报警电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_tuny_ele" id="fwarn_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td> -->
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压微调：</lable></td>
				  				<td class="rightTd" width="130"><input style="width:85px;" name="fweld_tuny_vol" id="fweld_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V/%)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压微调：</lable></td>
				  				<td class="rightTd" width="130"><input style="width:85px;" name="farc_tuny_vol" id="farc_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V/%)</td>
				  				<!-- <td class="leftTd" width="120"><lable><span class="required">*</span>报警电压微调：</lable></td>
				  				<td class="rightTd" width="130"><input style="width:85px;" name="fwarn_tuny_vol" id="fwarn_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V/%)</td> -->
				  			</tr>
				  			<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警电流上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_ele_up" id="fwarn_ele_up" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警电流下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_ele_down" id="fwarn_ele_down" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<!-- <td class="leftTd" width="120"><lable><span class="required">*</span>报警电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_tuny_ele" id="fwarn_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td> -->
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警电压上限：</lable></td>
				  				<td class="rightTd" width="130"><input style="width:85px;" name="fwarn_vol_up" id="fwarn_vol_up" class="easyui-numberbox" data-options="required:true,precision:1">(V/%)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警电压下限：</lable></td>
				  				<td class="rightTd" width="130"><input style="width:85px;" name="fwarn_vol_down" id="fwarn_vol_down" class="easyui-numberbox" data-options="required:true,precision:1">(V/%)</td>
				  				<!-- <td class="leftTd" width="120"><lable><span class="required">*</span>报警电压微调：</lable></td>
				  				<td class="rightTd" width="130"><input style="width:85px;" name="fwarn_tuny_vol" id="fwarn_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V/%)</td> -->
				  			</tr>
		            	</table>
		            </div>
	            </div>
			</form>
		</div>
		<div id="mwdlg-buttons">
			<a href="javascript:selectMachineList(0);" class="easyui-linkbutton" iconCls="icon-getwps" id="otcgetWpsBut">索取规范</a>
			<a href="javascript:saveMainWps();" class="easyui-linkbutton" iconCls="icon-ok" id="otcsaveWpsBut">保存</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 删除工艺 -->
		<div id="rmmwdlg" class="easyui-dialog" style="width: 900px; height: 510px; padding:10px 20px" closed="true" buttons="#rmmwdlg-buttons">
			<form id="rmmwfm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div region="left" style="padding-left:20px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable>通道号：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" id="fchanel" name="fchanel" style="width:85px;" id="chanel" data-options="editable:false">
				                	<option value="1">通道号1</option>
								    <option value="2">通道号2</option>
								    <option value="3">通道号3</option>
								    <option value="4">通道号4</option>
								    <option value="5">通道号5</option>
								    <option value="6">通道号6</option>
								    <option value="7">通道号7</option>
								    <option value="8">通道号8</option>
								    <option value="9">通道号9</option>
								    <option value="10">通道号10</option>
								    <option value="11">通道号11</option>
								    <option value="12">通道号12</option>
								    <option value="13">通道号13</option>
								    <option value="14">通道号14</option>
								    <option value="15">通道号15</option>
								    <option value="16">通道号16</option>
								    <option value="17">通道号17</option>
								    <option value="18">通道号18</option>
								    <option value="19">通道号19</option>
								    <option value="20">通道号20</option>
								    <option value="21">通道号21</option>
								    <option value="22">通道号22</option>
								    <option value="23">通道号23</option>
								    <option value="24">通道号24</option>
								    <option value="25">通道号25</option>
								    <option value="26">通道号26</option>
								    <option value="27">通道号27</option>
								    <option value="28">通道号28</option>
								    <option value="29">通道号29</option>
								    <option value="30">通道号30</option>
				                </select>
				            </td>
			  				<td></td>
			  				<td></td>
			  			</tr>
	            	</table>
	            </div>
        	    <div style="border:1px solid green;border-radius: 8px;padding:15px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd" width="50"><lable><span class="required">*</span>收弧：</lable></td>
			  				<td class="rightTd" width="90">
			  					<select class="easyui-combobox" style="width:85px;" name="farc" id="farc" data-options="editable:false">
				                	<option value="111">无</option>
								    <option value="112">有</option>
								    <option value="113">反复</option>
								    <option value="114">点焊</option>
				                </select>
			  				</td>
			  				<td class="leftTd" width="90"><lable><span class="required">*</span>一元/个别：</lable></td>
			  				<td class="rightTd" width="90">
			  					<select class="easyui-combobox" name="fselect" style="width:85px;" id="fselect" data-options="editable:false" onChange="changeValue(current,old)">
				                    <option value="102">个别</option>
								    <option value="101">一元</option>
				                </select>
			  				</td>
			  				<td class="leftTd" width="70"><lable>初期条件：</lable></td>
			  				<td class="rightTd" width="20"><input name="finitial" id="finitial" type="checkbox" value="1" style="width:30px;"/></td>
			  				<td class="leftTd" width="70"><lable>熔深控制：</lable></td>
			  				<td class="rightTd" width="20"><input style="width:30px;" name="fcontroller" id="fcontroller" type="checkbox" value="1"/></td>
			  				<td class="leftTd" width="100"><lable>柔软电弧模式：</lable></td>
			  				<td class="rightTd" width="30"><input style="width:30px;" name="fmode" id="fmode" type="checkbox" value="1"></td>
			  			</tr>
	            	</table>
	            </div>
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>电弧特性：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" id="fcharacter" name="fcharacter" class="easyui-numberbox">(±1)</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊丝材质：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" style="width:85px;" name="fmaterial" id="fmaterial" data-options="editable:false">
				                	<option value="91">低碳钢实心</option>
								    <option value="92">不锈钢实心</option>
								    <option value="93">低碳钢药芯</option>
								    <option value="94">不锈钢药芯</option>
				                </select>
				            </td>
				            <td class="leftTd" width="70"><lable><span class="required">*</span>提前送气：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" name="fadvance" id="fadvance" class="easyui-numberbox" data-options="precision:1">(0.1s)</td>
			  				<td class="leftTd" width="50"><lable><span class="required">*</span>气体：</lable></td>
			  				<td class="rightTd" width="70">
			  					<select class="easyui-combobox" style="width:85px;" name="fgas" id="fgas" data-options="editable:false">
				                	<option value="121">CO2</option>
								    <option value="122">MAG</option>
								    <option value="123">MIG</option>
				                </select>
				            </td>
			  			</tr>
	            		<tr>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>点焊时间：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" name="ftime" id="ftime" class="easyui-numberbox" data-options="precision:1">(0.1s)</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊丝直径：</lable></td>
			  				<td class="rightTd" width="70">
			  					<select class="easyui-combobox" style="width:85px;" name="fdiameter" id="fdiameter" data-options="editable:false">
				                	<option value="131">Φ1.0</option>
				                	<option value="132">Φ1.2</option>
				                	<option value="133">Φ1.4</option>
				                	<option value="134">Φ1.6</option>
				                </select>
			  				</td>
			  				<td class="leftTd" width="70"><lable><span class="required">*</span>滞后送气：</lable></td>
			  				<td class="rightTd" width="130"><input style="width:85px;" name="fhysteresis" id="fhysteresis" class="easyui-numberbox" data-options="precision:1">(0.1s)</td>
			  				<td></td>
			  				<td></td>
		  				</tr>
	            	</table>
	            </div>
	            
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;">
	            	<div>
	            		<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_ele" id="fini_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><lable><span class="required">*</span>收弧电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_ele" id="farc_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_ele" id="fweld_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  			</tr>
		            		<tr id="trgebie">
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol" id="fini_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol" id="fweld_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			    <td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol" id="farc_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			</tr>
		            		<tr id="tryiyuan">
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol1" id="fweld_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol1" id="farc_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol1" id="fini_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  			</tr>
			            	<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_ele" id="fweld_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_ele" id="farc_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_tuny_ele" id="fwarn_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_vol" id="fweld_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_vol" id="farc_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_tuny_ele" id="fwarn_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  			</tr>
		            	</table>
		            </div>
	            </div>
			</form>
		</div>
		<div id="rmmwdlg-buttons">
			<a href="javascript:removeMainwps();" class="easyui-linkbutton" iconCls="icon-ok">删除</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择工艺 -->
		<div id="smwdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#smwdlg-buttons">
			<form id="smwfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="mainWpsTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="smwdlg-buttons">
			<a href="javascript:selectMachineList(1);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择焊机 -->
		<div id="smdlg" class="easyui-dialog" style="width: 600px; height: 600px; padding:10px 20px" closed="true" buttons="#smdlg-buttons">
			<form id="smfm" class="easyui-form" method="post" data-options="novalidate:true">
				作业区：
				<select class="easyui-combobox" name="zitem" id="zitem" data-options="editable:false"></select>
				班组：
				<select class="easyui-combobox" name="bitem" id="bitem" data-options="editable:false"></select>
				<label>焊机编号：<input style="width:50px;" id="otcMachineId" name="otcMachineId" class="easyui-textbox"></label>
				<button id="otcMachineSearch">搜索</button>
				<table id="weldingmachineTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="smdlg-buttons">
			<a href="javascript:selectModel();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 下发结果表格 -->
		<div id="resultdlg" class="easyui-dialog" style="width: 1120px; height: 600px; padding:10px 20px" closed="true" buttons="#resultdlg-buttons">
			<form id="resultfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="giveResultTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="resultdlg-buttons">
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 控制命令下发 -->
		<div id="condlg" class="easyui-dialog" style="width: 600px; height: 300px; padding:10px 20px" closed="true" buttons="#condlg-buttonss">
			<form id="confm" class="easyui-form" method="post" data-options="novalidate:true">
				<table width="100%" height="94%" border="1" style="text-align: center;">
					  <tr height="30px">
					    <td colspan="2" align="center">
					    	<font face="黑体" size="5">控制命令</font>
					    </td>
					  </tr>
					  <tr height="30px">
					    <td align="center" bgcolor="#FFFAF0">工作：</td>
					    <td>
					    	<input id ="free" name="free" type="radio" value="1" checked="checked"/>工作不可自由调节
			  				<input id ="free" name="free" type="radio" value="0"/>工作自由调节
			  			</td>
					  </tr>
					  <tr height="30px">
					    <td colspan="2" align="center">					
							<a href="javascript:selectMachineList(3);" class="easyui-linkbutton" iconCls="icon-ok">下发控制命令</a>
							<a href="javascript:openPassDlg();" class="easyui-linkbutton" iconCls="icon-ok">密码下发</a>			
						</td>
					  </tr>
				</table>
			</form>
		</div>
		<div id="condlg-buttonss">
<!-- 			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>  -->
			<a href="javascript:closeDialog('condlg');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 密码框 -->
		<div id="pwd" class="easyui-dialog" style="text-align:center;width:400px;height:200px" closed="true" buttons="#dlg-pwd"algin="center">
	        <br><br><lable><span class="required">*</span>密码：</lable>
	        <input name="passwd" id="passwd" type="password" class="easyui-numberbox"><br/>
	        <lable style="color:red;">（注：密码范围是1~999）</lable>
        </div>
        <div id="dlg-pwd">
			<a href="javascript:selectMachineList(2);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:closeDialog('pwd');" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 添加修改松下工艺 -->
		<div id="editSxDlg" class="easyui-dialog" style="width: 900px; height: 430px; padding:10px 20px" closed="true" buttons="#sxdlg-buttons">
			<form id="sxfm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div region="left" style="padding-left:20px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable>通道号：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" id="sxfwpsnum" name="fwpsnum" style="width:85px;" data-options="editable:false">
				                	<option value="1">通道号1</option>
								    <option value="2">通道号2</option>
								    <option value="3">通道号3</option>
								    <option value="4">通道号4</option>
								    <option value="5">通道号5</option>
								    <option value="6">通道号6</option>
								    <option value="7">通道号7</option>
								    <option value="8">通道号8</option>
								    <option value="9">通道号9</option>
								    <option value="10">通道号10</option>
				                </select>
				            </td>
			  				<td></td>
			  				<td><input type="hidden" id="sxchanel"/></td>
			  			</tr>
	            	</table>
	            </div>
        	    <div style="border:1px solid green;border-radius: 8px;padding:15px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>材质：</lable></td>
			  				<td class="rightTd" width="120">
			  					<select class="easyui-combobox" style="width:85px;" name="fmaterial" id="sxfmaterial" data-options="editable:false">
				                </select>
			  				</td>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>丝径：</lable></td>
			  				<td class="rightTd" width="120">
			  					<select class="easyui-combobox" style="width:85px;" name="fdiameter" id="sxfdiameter" data-options="editable:false">
				                </select>
			  				</td>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>气体：</lable></td>
			  				<td class="rightTd" width="120">
			  					<select class="easyui-combobox" style="width:85px;" name="fgas" id="sxfgas" data-options="editable:false">
				                </select>
			  				</td>
			  			</tr>
	            		<tr>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接控制：</lable></td>
			  				<td class="rightTd" width="120">
			  					<select class="easyui-combobox" style="width:85px;" name="fcontroller" id="sxfcontroller" data-options="editable:false">
				                </select>
			  				</td>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>脉冲有无：</lable></td>
			  				<td class="rightTd" width="120">
			  					<select class="easyui-combobox" style="width:85px;" name="farc" id="sxfarc" data-options="editable:false">
				                </select>
			  				</td>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>一元/个别：</lable></td>
			  				<td class="rightTd" width="120">
			  					<select class="easyui-combobox" name="fselect" style="width:85px;" id="sxfselect" data-options="editable:false">
				                </select>
			  				</td>
			  			</tr>
			  			<tr>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>点焊时间：</lable></td>
			  				<td class="rightTd" width="120"><input style="width:85px;" name="ftime" id="sxftime" class="easyui-numberbox" data-options="required:true,min:0,max:6553.5,precision:1"></td>
            				<td class="leftTd" width="120"><lable><span class="required">*</span>起弧延时时间：</lable></td>
			  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_delay_time" id="sxfarc_delay_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:3,precision:1">(A)</td>
			  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警延时时间：</lable></td>
			  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_delay_time" id="sxfwarn_delay_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1">(A)</td>
	            		</tr>
	            		<tr>
			  				<td class="leftTd" width="120" ><lable><span class="required">*</span>超规范报警停机：</lable></td>
			  				<td class="rightTd" colspan="2">
			  					停机<input type="radio" value="0" style="width:50px;" name="sxfcharacter"/>
			  					不停机<input type="radio" value="1" style="width:50px;" name="sxfcharacter"/>
			  				</td>
			  				<td class="rightTd" width="120"></td>
			  				<td class="leftTd" width="120"></td>
			  				<td class="rightTd" width="120"></td>
			  			</tr> 
	            	</table>
	            </div>
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;display:none">
	            	<div>
	            		<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol" id="sxfweld_vol" class="easyui-numberbox" data-options="required:true,max:65535">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_ele" id="sxfini_ele" class="easyui-numberbox" data-options="required:true,max:65535">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_ele" id="sxfarc_ele" class="easyui-numberbox" data-options="required:true,max:65535">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_ele" id="sxfweld_ele" class="easyui-numberbox" data-options="required:true,max:65535">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol" id="sxfini_vol" class="easyui-numberbox" data-options="required:true,max:65535">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol" id="sxfarc_vol" class="easyui-numberbox" data-options="required:true,max:65535">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>延时时间：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fadvance" id="sxfadvance" class="easyui-numberbox" data-options="required:true,max:255"></td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>修正周期：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fhysteresis" id="sxfhysteresis" class="easyui-numberbox" data-options="required:true,max:255"></td>
<!-- 				  				<td class="leftTd" width="120"><lable><span class="required">*</span>点焊时间：</lable></td> -->
<!-- 				  				<td class="rightTd" width="120"><input style="width:85px;" name="ftime" id="sxftime" class="easyui-numberbox" data-options="required:true,min:0,max:6553.5,precision:1"></td> -->
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>干伸长度：</lable></td>
				  				<td class="rightTd" width="120">
				  					<select class="easyui-combobox" name="finitial" style="width:85px;" id="sxfinitial" data-options="editable:false">
					                </select>
				  				</td>
				  				<td class="leftTd" width="120"><lable></lable></td>
				  				<td class="rightTd" width="120"></td>
				  				<td class="leftTd" width="120"><lable></lable></td>
				  				<td class="rightTd" width="120"></td>
				  			</tr>
		            	</table>
		            </div>
	            </div>
	            
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;display:none">
	            	<div>
	            		<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>流量上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fflow_top" id="sxfflow_top" class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>流量下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fflow_bottom" id="sxfflow_bottom" fflow_bottom class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1">(A)</td>
				  				<td class="leftTd" width="120"></td>
				  				<td class="rightTd" width="120"></td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>延时时间：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fdelay_time" id="sxfdelay_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1"></td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>超限时间：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fover_time" id="sxfover_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>修正周期：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="ffixed_cycle" id="sxffixed_cycle" class="easyui-numberbox" data-options="required:true,min:0.1,max:10,precision:1"></td>
				  			</tr>
		            		<tr>
<!-- 				  				<td class="leftTd" width="120"><lable><span class="required">*</span>起弧延时时间：</lable></td> -->
<!-- 				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_delay_time" id="sxfarc_delay_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:3,precision:1">(A)</td> -->
<!-- 				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警延时时间：</lable></td> -->
<!-- 				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_delay_time" id="sxfwarn_delay_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1">(A)</td> -->
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>报警停机时间：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fwarn_stop_time" id="sxfwarn_stop_time" class="easyui-numberbox" data-options="required:true,min:0.1,max:25,precision:1">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120" ><lable><span class="required">*</span>超规范报警停机：</lable></td>
				  				<td class="rightTd" colspan="2">
				  					停机<input type="radio" value="0" style="width:50px;" name="sxfcharacter"/>
				  					不停机<input type="radio" value="1" style="width:50px;" name="sxfcharacter"/>
				  				</td>
				  				<td class="rightTd" width="120"></td>
				  				<td class="leftTd" width="120"></td>
				  				<td class="rightTd" width="120"></td>
				  			</tr>
		            	</table>
		            </div>
	            </div>
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;">
	            	<div>
	            		<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电流上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_ele_top" id="sxfpreset_ele_top" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电压上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_vol_top" id="sxfpreset_vol_top" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电流上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol1" id="sxfini_vol1" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电流下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_ele_bottom" id="sxfpreset_ele_bottom" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电压下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_vol_bottom" id="sxfpreset_vol_bottom" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电流下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_ele" id="sxfweld_tuny_ele" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol1" id="sxfweld_vol1" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol_top" id="sxfarc_vol_top" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol1" id="sxfarc_vol1" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_ele" id="sxfarc_tuny_ele" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_vol" id="sxfarc_tuny_vol" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_vol" id="sxfweld_tuny_vol" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  			</tr>
				  			<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电流报警上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_ele_warn_top" id="sxfpreset_ele_warn_top" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电压报警上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_vol_warn_top" id="sxfpreset_vol_warn_top" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  			</tr>
				  			<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电流报警下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_ele_warn_bottom" id="sxfpreset_ele_warn_bottom" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>预置电压报警下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fpreset_vol_warn_bottom" id="sxfpreset_vol_warn_bottom" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  			</tr>
		            	</table>
		            </div>
	            </div>
	            <div style="border:1px solid green;border-radius: 8px;padding:15px;margin-top:5px;display:none">
	            	<div>
	            		<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电流报警上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_ele_warn_top" id="sxfini_ele_warn_top" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电流报警下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_ele_warn_bottom" id="sxfini_ele_warn_bottom" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流报警上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_ele_warn_top" id="sxfarc_ele_warn_top" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压报警上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol_warn_top" id="sxfarc_vol_warn_top" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压报警上限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol_warn_top" id="sxfini_vol_warn_top" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  			</tr>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电流下报警限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_ele_warn_bottom" id="sxfarc_ele_warn_bottom" class="easyui-numberbox" data-options="required:true,max:65535,precision:1">(A)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压下报警限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol_warn_bottom" id="sxfarc_vol_warn_bottom" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压报警下限：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol_warn_bottom" id="sxfini_vol_warn_bottom" class="easyui-numberbox" data-options="required:true,max:6553.5,precision:1">(V)</td>
				  			</tr>
		            	</table>
		            </div>
	            </div>
			</form>
		</div>
		<div id="sxdlg-buttons" id="sxeditDiv">
			<a href="javascript:selectSxMachineList(0);" class="easyui-linkbutton" iconCls="icon-getwps" id="sxgetWpsBut">索取规范</a>
			<a href="javascript:saveMainWps();" class="easyui-linkbutton" iconCls="icon-ok" id="sxSaveWpsBut">保存</a>
			<a href="javascript:removeSxwps();" class="easyui-linkbutton" iconCls="icon-ok" id="sxRemoveWpsBut">删除</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择松下工艺 -->
		<div id="sxSelectdlg" class="easyui-dialog" style="width: 600px; height: 400px; padding:10px 20px" closed="true" buttons="#sxSelectdlg-buttons">
			<form id="smwfm" class="easyui-form" method="post" data-options="novalidate:true">
				<table id="sxSelectWpsTab" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
		<div id="sxSelectdlg-buttons">
			<a href="javascript:selectSxMachineList(1);" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 选择松下焊机 -->
		<div id="sxMachinedlg" class="easyui-dialog" style="width: 600px; height: 600px; padding:10px 20px" closed="true" buttons="#sxmachinedlg-buttons">
			<form id="sxmachinefm" class="easyui-form" method="post" data-options="novalidate:true">
				作业区：
				<select class="easyui-combobox" name="szitem" id="szitem" data-options="editable:false"></select>
				班组：
				<select class="easyui-combobox" name="sbitem" id="sbitem" data-options="editable:false"></select>
				<label>焊机编号：<input style="width:50px;" id="machineId" name="machineId" class="easyui-textbox"></label>
				<button id="sxMachineSearch">搜索</button>
				<table id="sxMachineTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div> 
		<div id="sxmachinedlg-buttons">
			<a href="javascript:selectSxModel();" class="easyui-linkbutton" iconCls="icon-ok">确认</a>
			<a href="javascript:closedlg();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<!-- 下发历史查询 -->
		<div id="wmhistorydlg" class="easyui-dialog" style="width: 950px; height: 520px; padding:10px 20px" closed="true">
			<form id="wmhistoryfm" class="easyui-form" method="post" data-options="novalidate:true">
			  	<div id="dg_btn">
					<div style="margin-bottom: 5px;">
			 			焊机编号：
						<input class="easyui-numberbox" name="machineNum" id="machineNum">
						工艺库名称：
						<input class="easyui-textbox" name="theWpslibName" id="theWpslibName">
						时间：
						<input class="easyui-datetimebox" name="dtoTime1" id="dtoTime1">--
						<input class="easyui-datetimebox" name="dtoTime2" id="dtoTime2">
						<a href="javascript:searchHistory();" class="easyui-linkbutton" iconCls="icon-select" >搜索</a>
					</div>
				</div>
				<table id="historyTable" style="table-layout: fixed; width:100%;"></table>
			</form>
		</div>
	</div>
  </body>
</html>