<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> -->
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>工艺管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/datagrid.css" />
	<link rel="stylesheet" type="text/css" href="resources/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="resources/css/base.css" />
	
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="resources/js/datagrid-filter.js"></script>
	<script type="text/javascript" src="resources/js/specification/allSpe.js"></script>
	<script type="text/javascript" src="resources/js/search/search.js"></script>
	<script type="text/javascript" src="resources/js/specification/addSpe.js"></script>
	<script type="text/javascript" src="resources/js/specification/destroySpe.js"></script>
	<script type="text/javascript" src="resources/js/specification/specificationtree.js"></script>
	<script type="text/javascript" src="resources/js/swfobject.js"></script>
	<script type="text/javascript" src="resources/js/web_socket.js"></script>
	<style type="text/css">
		table tr td{
			font-size:12px;
			height:30px;
		}
		.leftTd{
			text-align: right;
			/* width : 150px; */
		}
		.textbox-text{
			width:85px;
		}
	</style>
  </head>
  
  <body class="easyui-layout">
  	<jsp:include  page="../specificationtree.jsp"/>
  	<div  id="bodys" region="center"  hide="true"  split="true" >
	  	<div class="functiondiv">
			<div>
				<a href="javascript:suoqu();" class="easyui-linkbutton" iconCls="icon-getwps">索取规范</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:xiafa();" class="easyui-linkbutton" iconCls="icon-setwps">下发规范</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:copy(1);" class="easyui-linkbutton" iconCls="icon-copy">焊机参数复制</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:copy(0);" class="easyui-linkbutton" iconCls="icon-copys">单通道复制</a></td>
			</div>
		</div>
  		<div id=bodyy style="text-align:center"><p>欢迎使用！请先选择焊机。。。</p></div>
  		<div id="body">
  			<form id="fm" class="easyui-form" method="post" data-options="novalidate:true">
	        	<div region="left" style="padding-left:20px;">
	            	<table>
	            		<tr>
			  				<td class="leftTd"><lable>通道号：</lable></td>
			  				<td class="rightTd">
			  					<select class="easyui-combobox" name="chanel" style="width:85px;" id="chanel" data-options="editable:false">
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
				  				<td class="leftTd" width="70"><lable><span class="required">*</span>初期电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_ele" id="fini_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="70"><lable><lable><span class="required">*</span>收弧电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_ele" id="farc_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊接电流：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_ele" id="fweld_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  			</tr>
				  		</table>
			  		</div>
					<div id="gebie1" >
		            	<table>
		            		<tr>
				  				<td class="leftTd" width="70"><lable><span class="required">*</span>初期电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol" id="fini_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			    <td class="leftTd" width="70"><lable><span class="required">*</span>收弧电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol" id="farc_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  				<td class="leftTd" width="70"><lable><span class="required">*</span>焊接电压：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol" id="fweld_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			</tr>
		            	</table>
		            </div>
			  		<div id="yiyuan1" >
		            	<table>
		            		<tr>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>焊接电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_vol1" id="fweld_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>收弧电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_vol1" id="farc_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  				<td class="leftTd" width="120"><lable><span class="required">*</span>初期电压（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fini_vol1" id="fini_vol1" class="easyui-numberbox" data-options="required:true">(±1)</td>
				  			</tr>
		            	</table>
		            </div>
					<div id="yiyuan2" >
			            <table>
			            	<tr>
				  				<td class="leftTd" width="100"><lable><span class="required">*</span>焊接电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_ele" id="fweld_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  				<td class="leftTd" width="100"><lable><span class="required">*</span>收弧电流微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_ele" id="farc_tuny_ele" class="easyui-numberbox" data-options="required:true">(A)</td>
				  			</tr>
			            </table>
		            </div>
		            <div id="gebie3" >
		            	<table>
		            		<tr>
				  				<td class="leftTd" width="100"><lable><span class="required">*</span>焊接电压微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_vol" id="fweld_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  				<td class="leftTd" width="100"><lable><span class="required">*</span>收弧电压微调：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_vol" id="farc_tuny_vol" class="easyui-numberbox" data-options="required:true,precision:1">(V)</td>
				  			</tr>
		            	</table>
		            </div>
		            <div id="yiyuan3" >
		            	<table>
		            		<tr>
				  				<td class="leftTd" width="150"><lable><span class="required">*</span>焊接电压微调(一元)：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="fweld_tuny_vol1" id="fweld_tuny_vol1" class="easyui-numberbox" data-options="required:true">(%)</td>
				  				<td class="leftTd" width="150"><lable><span class="required">*</span>收弧电压微调（一元）：</lable></td>
				  				<td class="rightTd" width="120"><input style="width:85px;" name="farc_tuny_vol1" id="farc_tuny_vol1" class="easyui-numberbox" data-options="required:true">(%)</td>
				  			</tr>
		            	</table>
		            </div>
	            </div>
	            <div style="margin-top:40px;text-align: right">
  					<a href="javascript:save(0);" class="easyui-linkbutton" iconCls="icon-save">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
  					<a href="javascript:chushihua();" class="easyui-linkbutton" iconCls="icon-default">恢复默认值</a>
			  	</div>
			</form>
	    </div>
	    <div id="divro" class="easyui-dialog" style="width:400px;height:400px" closed="true" buttons="#dlg-ro"algin="center">
	    	<div style="text-align:center;height:25px">
	    		<lable id="mu"></lable>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    		所属班组：<select class="easyui-combobox" name="item" id="item" data-options="editable:false" onChange="changeValue(current,old)"></select>
	    	</div>
	    	<div id="tab" style="text-align:center;height:300px">
	    		<table id="ro" style="table-layout:fixed;width:100%;" ></table>
	    	</div>
        </div>
        <div id="dlg-ro">
			<a href="javascript:savecopy();" class="easyui-linkbutton" iconCls="icon-ok">下一步</a>
			<a href="javascript:divroclose();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
		
		<div id="divro1" class="easyui-dialog" style="width:400px;height:400px" closed="true" buttons="#dlg-ro1"algin="center">
	        <table id="ro1" style="table-layout:fixed;width:100%;" ></table>
        </div>
        <div id="dlg-ro1">
			<a href="javascript:$('#divro1').dialog('close');" class="easyui-linkbutton" iconCls="icon-ok">确定</a>
			<a href="javascript:divro1close();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
		</div>
    </div>
</body>
</html>
 