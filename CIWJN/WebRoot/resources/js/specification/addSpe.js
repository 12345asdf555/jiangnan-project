/**
 * 
 */
/**
 * 
 */
var data1;
var da;
var yshu;
var yshu1;
var node11;
var symbol=0;
var symbol1=0;
var symbol2;
var x=0;
var xx=0;
var rows1;
var itemrow;
var machga;
$(function(){
	gfsd();
//	inscombobox();
	$('#dlg').dialog( {
		onClose : function() {
			$("#fm").form("disableValidation");
		}
	})
	$("#fm").form("disableValidation");
	document.getElementById("body").style.display="none";
	insframeworkTree();
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : "td/AllTdbf",  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {
	          if (result) {
	        	  data1 = eval(result.web_socket);
	          }  
	      },
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	 });
	$.ajax({  
	    type : "post",  
	    async : false,
	    url : "datastatistics/getAllInsframework",  
	    data : {},  
	    dataType : "json", //返回数据形式为json  
	    success : function(result) {  
	        if (result) {
	        	itemrow=eval(result);
	            var optionStr = '';
	            for (var i = 0; i < result.ary.length; i++) {  
	                optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
	                        + result.ary[i].name + "</option>";
	            }
	            $("#item").html(optionStr);
	        }  
	    },  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    }  
		});
	
		$("#item").combobox();
		
		$.ajax({  
		      type : "post",  
		      async : false,
		      url : "weldingMachine/getMachineGather",  
		      data : {},  
		      dataType : "json", //返回数据形式为json  
		      success : function(result) {
		          if (result) {
		        	  machga = eval(result.rows);
		          }  
		      },
		      error : function(errorMsg) {  
		          alert("数据请求失败，请联系系统管理员!");  
		      }  
		 });
 })

$(document).ready(function () {
	$("#chanel").combobox({
		onSelect: function (record) {
    		$('#fgas').combobox('unselect');
    		$('#fdiameter').combobox('unselect');
    		$('#fmaterial').combobox('unselect');
    		$('#fselect').combobox('unselect');
		if(node11!=null){
			document.getElementById("yiyuan1").style.display="none";
			document.getElementById("yiyuan3").style.display="none";
			document.getElementById("gebie1").style.display="block";
			document.getElementById("gebie3").style.display="block";
			$.ajax({  
			      type : "post",  
			      async : false,
			      url : "wps/getAllSpe?machine="+node11.id+"&chanel="+record.value,  
			      data : {},  
			      dataType : "json", //返回数据形式为json  
			      success : function(result) {
			          if (result) {
			        	yshu = eval(result.rows);
			        	if(yshu.length!=0){
				      		$('#chanel').combobox('select',yshu[0].FWPSNum);
				      		$('#fselect').combobox('select',yshu[0].Fweld_I_MAX);
				    		$("#ftime").numberbox('setValue',yshu[0].ftime);
				    		$("#fadvance").numberbox('setValue',yshu[0].fadvance);
				    		$("#fini_ele").numberbox('setValue',yshu[0].fini_ele);
				    		$("#fini_vol").numberbox('setValue',yshu[0].fini_vol);
				    		$("#fini_vol1").numberbox('setValue',yshu[0].fini_vol1);
				    		$("#fweld_vol").numberbox('setValue',yshu[0].fweld_vol);
				    		$("#fweld_vol1").numberbox('setValue',yshu[0].fweld_vol1);
				    		$("#farc_vol").numberbox('setValue',yshu[0].farc_vol);
				    		$("#farc_vol1").numberbox('setValue',yshu[0].farc_vol1);
				    		$("#fweld_ele").numberbox('setValue',yshu[0].fweld_ele);
				    		$("#farc_ele").numberbox('setValue',yshu[0].farc_ele);
				    		$("#fhysteresis").numberbox('setValue',yshu[0].fhysteresis);
				    		$("#fcharacter").numberbox('setValue',yshu[0].Fweld_V_MAX);
				    		$('#fgas').combobox('select',yshu[0].Fweld_Alter_V);
				    		$('#fdiameter').combobox('select',yshu[0].Fweld_PreChannel);
				    		$('#fmaterial').combobox('select',yshu[0].Fweld_Alter_I);
				    		$("#fweld_tuny_ele").numberbox('setValue',yshu[0].fweld_tuny_ele);
				    		$("#fweld_tuny_vol").numberbox('setValue',yshu[0].fweld_tuny_vol);
				    		$("#farc_tuny_ele").numberbox('setValue',yshu[0].farc_tuny_ele);
				    		$("#farc_tuny_vol").numberbox('setValue',yshu[0].Fdiameter);
				    		$("#farc_tuny_vol1").numberbox('setValue',yshu[0].Fdiameter);
				    		$("#fweld_tuny_vol1").numberbox('setValue',yshu[0].fweld_tuny_vol);
			    		if(yshu[0].Fweld_I=="1"){
			    			$("#finitial").prop("checked",true);
			    		}
			    		$('#farc').combobox('select',yshu[0].Fweld_I_MIN);
			    		if(yshu[0].Fweld_V=="1"){
			    			$("#fcontroller").prop("checked",true);
			    		}
			    		if(yshu[0].Fweld_V_MIN=="1"){
			    			$("#fmode").prop("checked",true);
			    		}
			        	}else{
			        		chushihua();
			        		alert("未查询到相关数据，已初始化，也可尝试索取。");
			        	}
			          }
			      },
			      error : function(errorMsg) {  
			          alert("数据请求失败，请联系系统管理员!");  
			      }  
			 });
			}
		}
	});
});

function chushihua(){
	document.getElementById("yiyuan1").style.display="none";
	document.getElementById("yiyuan3").style.display="none";
	document.getElementById("gebie1").style.display="block";
	document.getElementById("gebie3").style.display="block";
	$("#fmode").prop("checked",false);
	$("#finitial").prop("checked",false);
	$("#fcontroller").prop("checked",false);
	$('#fselect').combobox('select',102);
	$("#ftime").numberbox('setValue',30.0);
	$("#fadvance").numberbox('setValue',1.0);
	$("#fini_ele").numberbox('setValue',100);
	$("#fweld_ele").numberbox('setValue',100);
	$("#farc_ele").numberbox('setValue',100);
	$("#fhysteresis").numberbox('setValue',1.0);
	$("#fcharacter").numberbox('setValue',0);
	$('#fgas').combobox('select',121);
	$('#fdiameter').combobox('select',132);
	$('#fmaterial').combobox('select',91);
	$("#fweld_tuny_ele").numberbox('setValue',0);
	$("#farc_tuny_ele").numberbox('setValue',0);
	$("#fini_vol").numberbox('setValue',19.0);
	$("#fweld_vol").numberbox('setValue',19.0);
	$("#farc_vol").numberbox('setValue',19.0);
	$("#fweld_tuny_vol").numberbox('setValue',0.0);
	$("#farc_tuny_vol").numberbox('setValue',0.0);
	$("#fini_vol1").numberbox('setValue',0);
	$("#fweld_vol1").numberbox('setValue',0);
	$("#farc_vol1").numberbox('setValue',0);
	$("#fweld_tuny_vol1").numberbox('setValue',0);
	$("#farc_tuny_vol1").numberbox('setValue',0);
}

function gfsd() {
    var mySelect=$("#fgas option");
	$("#fselect").combobox({
		onSelect: function (record) {
		if(node11!=null){
		if(record.value==102){
			document.getElementById("yiyuan1").style.display="none";
			document.getElementById("yiyuan3").style.display="none";
			document.getElementById("gebie1").style.display="block";
			document.getElementById("gebie3").style.display="block";
		}else{
			document.getElementById("yiyuan1").style.display="block";
			document.getElementById("yiyuan3").style.display="block";
			document.getElementById("gebie1").style.display="none";
			document.getElementById("gebie3").style.display="none";
		}
			}
		}
	});

	$("#farc").combobox({
		onSelect: function (record) {
			if(record.value==111){
				$('#farc_ele').numberbox("disable",true);
				$('#farc_vol').numberbox("disable",true);
				$('#farc_tuny_ele').numberbox("disable",true);
				$('#farc_tuny_vol').numberbox("disable",true);
				$('#farc_tuny_vol1').numberbox("disable",true);
				$('#farc_vol1').numberbox("disable",true);
				$('#ftime').numberbox("disable",true);
				$('#fini_ele').numberbox("disable",true);
				$('#fini_vol').numberbox("disable",true);
				$('#fini_vol1').numberbox("disable",true);
			}else if(record.value==112){
				$('#farc_ele').numberbox("enable",true);
				$('#farc_vol').numberbox("enable",true);
				$('#farc_tuny_ele').numberbox("enable",true);
				$('#farc_tuny_vol').numberbox("enable",true);
				$('#farc_tuny_vol1').numberbox("enable",true);
				$('#farc_vol1').numberbox("enable",true);
				$('#ftime').numberbox("disable",true);
				if($("#finitial").is(":checked")){
					$('#fini_ele').numberbox("enable",true);
					$('#fini_vol').numberbox("enable",true);
					$('#fini_vol1').numberbox("enable",true);
				}else{
					$('#fini_ele').numberbox("disable",true);
					$('#fini_vol').numberbox("disable",true);
					$('#fini_vol1').numberbox("disable",true);
				}
			}else if(record.value==113){
				$('#farc_ele').numberbox("enable",true);
				$('#farc_vol').numberbox("enable",true);
				$('#farc_tuny_ele').numberbox("enable",true);
				$('#farc_tuny_vol').numberbox("enable",true);
				$('#farc_tuny_vol1').numberbox("enable",true);
				$('#farc_vol1').numberbox("enable",true);
				$('#ftime').numberbox("disable",true);
				if($("#finitial").is(":checked")){
					$('#fini_ele').numberbox("enable",true);
					$('#fini_vol').numberbox("enable",true);
					$('#fini_vol1').numberbox("enable",true);
				}else{
					$('#fini_ele').numberbox("disable",true);
					$('#fini_vol').numberbox("disable",true);
					$('#fini_vol1').numberbox("disable",true);
				}
			}else{
				$('#farc_ele').numberbox("disable",true);
				$('#farc_vol').numberbox("disable",true);
				$('#farc_tuny_ele').numberbox("disable",true);
				$('#farc_tuny_vol').numberbox("disable",true);
				$('#farc_tuny_vol1').numberbox("disable",true);
				$('#farc_vol1').numberbox("disable",true);
				$('#fini_ele').numberbox("disable",true);
				$('#fini_vol').numberbox("disable",true);
				$('#fini_vol1').numberbox("disable",true);
				$('#ftime').numberbox("enable",true);
				$('#ftime').numberbox("enable",true);
			}
		}
	});
	
	$("#finitial").click(function(){
		if($("#finitial").is(":checked")){
			if($('#farc').combobox('getValue')==112||$('#farc').combobox('getValue')==113){
				$('#fini_ele').numberbox("enable",true);
				$('#fini_vol').numberbox("enable",true);
				$('#fini_vol1').numberbox("enable",true);
			}else{
				$('#fini_ele').numberbox("disable",true);
				$('#fini_vol').numberbox("disable",true);
				$('#fini_vol1').numberbox("disable",true);
			}
		}else{
			$('#fini_ele').numberbox("disable",true);
			$('#fini_vol').numberbox("disable",true);
			$('#fini_vol1').numberbox("disable",true);
		}
	});
	
	$("#fmaterial").combobox({
		onSelect: function (record) {
			if(record.value==91){
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [{"text": "CO2", "value": "121"},{"text": "MAG", "value": "122"}]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [{"text": "Φ1.0", "value": "131"},{"text": "Φ1.2", "value": "132"},{"text": "Φ1.4", "value": "133"},{"text": "Φ1.6", "value": "134"}]);
			}else if(record.value==92){
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [{"text": "MIG", "value": "123"}]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [{"text": "Φ1.2", "value": "132"},{"text": "Φ1.6", "value": "134"}]);
			}else if(record.value==93){
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [{"text": "CO2", "value": "121"}]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [{"text": "Φ1.2", "value": "132"},{"text": "Φ1.4", "value": "133"},{"text": "Φ1.6", "value": "134"}]);
			}else{
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [{"text": "CO2", "value": "121"}]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [{"text": "Φ1.2", "value": "132"},{"text": "Φ1.6", "value": "134"}]);
			}
			var fgas = $('#fgas').combobox('getData');
			var fdiameter = $('#fdiameter').combobox('getData');
			$('#fgas').combobox('select',fgas[0].value);
			$('#fdiameter').combobox('select',fdiameter[0].value);
		}
	});
};

 var url = "";
var flag = 1;
function addSpe(){
	flag = 1;
	$('#dlg').window( {
		title : "新增工艺",
		modal : true
	});
	$('#dlg').window('open');
	$('#fm').form('clear');
	url = "wps/addWps";
}

function editWps(){
	flag = 2;
	$('#fm').form('clear');
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$('#dlg').window( {
			title : "修改工艺",
			modal : true
		});
		$('#dlg').window('open');
		$('#fm').form('load', row);
		url = "wps/updateWps?FID="+ row.FID;
	}
}
//提交
function save(value){
    if($('#fadvance').numberbox('getValue')<0||$('#fadvance').numberbox('getValue')>100){
      	 alert("提前送气范围：0~100");
      	 return;
       }
       if($('#fini_ele').numberbox('getValue')<30||$('#fini_ele').numberbox('getValue')>550){
      	 alert("初期电流范围：30~550");
      	 return;
       }
       if($('#fini_vol').numberbox('getValue')<12||$('#fini_vol').numberbox('getValue')>50){
      	 alert("初期电压范围：12~50");
      	 return;
       }
       if($('#fini_vol1').numberbox('getValue')<(-30)||$('#fini_vol1').numberbox('getValue')>(30)){
      	 alert("初期电压一元范围：-30~30");
      	 return;
       }
       if($('#fweld_ele').numberbox('getValue')<30||$('#fweld_ele').numberbox('getValue')>550){
      	 alert("焊接电流范围：30~550");
      	 return;
       }
       if($('#fweld_vol').numberbox('getValue')<12||$('#fweld_vol').numberbox('getValue')>50){
      	 alert("焊接电压范围：12~50");
      	 return;
       }
       if($('#fweld_vol1').numberbox('getValue')<(-30)||$('#fweld_vol1').numberbox('getValue')>(30)){
      	 alert("焊接电压一元范围：-30~30");
      	 return;
       }
       if($('#farc_ele').numberbox('getValue')<30||$('#farc_ele').numberbox('getValue')>550){
      	 alert("收弧电流范围：30~550");
      	 return;
       }
       if($('#farc_vol').numberbox('getValue')<12||$('#farc_vol').numberbox('getValue')>50){
      	 alert("收弧电压范围：12~50");
      	 return;
       }
       if($('#farc_vol1').numberbox('getValue')<(-30)||$('#farc_vol1').numberbox('getValue')>(30)){
      	 alert("收弧电压一元范围：-30~30");
      	 return;
       }
       if($('#fhysteresis').numberbox('getValue')<0||$('#fhysteresis').numberbox('getValue')>100){
      	 alert("滞后送气范围：0~100");
      	 return;
       }
       if($('#fcharacter').numberbox('getValue')<(-99)||$('#fcharacter').numberbox('getValue')>(99)){
      	 alert("电弧特性范围：-99~99");
      	 return;
       }
   	var url2 = "";
   	var finitial;
   	var fcontroller;
   	var fmode;
       if($("#finitial").is(":checked")==true){
           finitial = 1;
       }else{
       	finitial = 0;
       }
       if($("#fcontroller").is(":checked")==true){
       	fcontroller = 1;
       }else{
       	fcontroller = 0;
       }
       if($("#finitial").is(":checked")==true){
       	fmode = 1;
       }else{
       	fmode = 0;
       }
       var fselect = $('#fselect').combobox('getValue');
       var farc = $('#farc').combobox('getValue');
       var fmaterial = $('#fmaterial').combobox('getValue');
//       var fmaterial = document.getElementById('fmaterial').value;
//       var fgas = document.getElementById('fgas').value;
//       var fdiameter = document.getElementById('fdiameter').value;
       var fgas = $('#fgas').combobox('getValue');
       var fdiameter = $('#fdiameter').combobox('getValue');
       var chanel = $('#chanel').combobox('getValue');
   	var ftime = $('#ftime').numberbox('getValue');
   	var fadvance = $('#fadvance').numberbox('getValue');
   	var fini_ele = $('#fini_ele').numberbox('getValue');
   	var fweld_ele = $('#fweld_ele').numberbox('getValue');
   	var farc_ele = $('#farc_ele').numberbox('getValue');
   	var fhysteresis = $('#fhysteresis').numberbox('getValue');
   	var fcharacter = $('#fcharacter').numberbox('getValue');
   	var fweld_tuny_ele = $('#fweld_tuny_ele').numberbox('getValue');
   	var farc_tuny_ele = $('#farc_tuny_ele').numberbox('getValue');
   	var fini_vol = $('#fini_vol').numberbox('getValue');
   	var fweld_vol = $('#fweld_vol').numberbox('getValue');
   	var farc_vol = $('#farc_vol').numberbox('getValue');
   	var fini_vol1 = $('#fini_vol1').numberbox('getValue');
   	var fweld_vol1 = $('#fweld_vol1').numberbox('getValue');
   	var farc_vol1 = $('#farc_vol1').numberbox('getValue');
   	if(fselect==102){
   		var fweld_tuny_vol = $('#fweld_tuny_vol').numberbox('getValue');
   		var farc_tuny_vol = $('#farc_tuny_vol').numberbox('getValue');
   	}else{
   		var fweld_tuny_vol = $('#fweld_tuny_vol1').numberbox('getValue');
   		var farc_tuny_vol = $('#farc_tuny_vol1').numberbox('getValue');
   	}
       var machine = node11.id;
   	messager = "保存成功！";
   	url2 = "wps/apSpe"+"?finitial="+finitial+"&fcontroller="+fcontroller+"&fmode="+fmode+"&fselect="+fselect+"&farc="+farc+"&fmaterial="+fmaterial+"&fgas="+fgas+"&fdiameter="+fdiameter+"&chanel="+chanel+"&ftime="+ftime+"&fadvance="+fadvance+"&fini_ele="+fini_ele+"&fweld_ele="+fweld_ele+"&farc_ele="+farc_ele+"&fhysteresis="+fhysteresis+"&fcharacter="+fcharacter+"&machine="+machine+"&fweld_tuny_ele="+fweld_tuny_ele+"&farc_tuny_ele="+farc_tuny_ele+"&fini_vol="+fini_vol+"&fini_vol1="+fini_vol1+"&fweld_vol="+fweld_vol+"&fweld_vol1="+fweld_vol1+"&farc_vol="+farc_vol+"&farc_vol1="+farc_vol1+"&fweld_tuny_vol="+fweld_tuny_vol+"&farc_tuny_vol="+farc_tuny_vol;
//   	url2 = "wps/apSpe";
   	$.ajax({  
   	      type : "post",  
   	      async : false,
   	      url : url2,  
   	      data : {},  
   	      dataType : "json", //返回数据形式为json  
   	      success : function(result) {
   				if (!result.success) {
   					if(value==0){
   					$.messager.show( {
   						title : 'Error',
   						msg : result.errorMsg
   					});
   					}
   				} else {
   					if(value==0){
   					$.messager.alert("提示", messager);
   					$('#dlg').dialog('close');
   					$('#dg').datagrid('reload');
   					}
   				}
   	      },
   	      error : function(errorMsg) {  
   	          alert("数据请求失败，请联系系统管理员!");  
   	      }  
   	 });
}

function insframeworkTree(){
	$("#speTree").tree({  
		onClick : function(node){
			$('#chanel').combobox('select',0);
			node11 = $(this).tree("getSelected");
			var leve = $(this).tree("getLevel", node11.target);
			if((leve==1)||(leve==2)||(leve==3)||(leve==4)){
				 alert("请选择对应的焊机！！！");
			}else{
				document.getElementById("body").style.display="block";
				document.getElementById("bodyy").style.display="none";
				$('#chanel').combobox('select',1);
				$("#ro").datagrid( {
					fitColumns : true,
					height : $("#tab").height(),
					width : $("#tab").width(),
					idField : 'id',
					url : "td/getMachine?mach="+node11.id,
					singleSelect : false,
					rownumbers : true,
			        columns : [ [ {
					    field:'ck',
						checkbox:true
					},{
						field : 'fid',
						title : '序号',
						width : 50,
						halign : "center",
						align : "left",
						hidden:true
					}, {
						field : 'fequipment_no',
						title : '目标焊机编号',
						width : 80,
						halign : "center",
						align : "left"
					}, {
						field : 'finsname',
						title : '所属班组',
						width : 80,
						halign : "center",
						align : "left"
					}
					] ]
				});
			}
		 }
	})
}

$(document).ready(function () {
	$("#item").combobox({
		onSelect: function (record) {
			$("#ro").datagrid('load',{
				"parent" : record.value
			})
		}
	});
});

function suoqu(){
	var socketfc=null;
	symbol=0;
	if(typeof(WebSocket) == "undefined") {
    	WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
    	WEB_SOCKET_DEBUG = true;
	}
	socketfc = new WebSocket(data1);
	//打开事件
	socketfc.onopen = function() {
		
	var chanel = parseInt($('#chanel').numberbox('getValue')).toString(16);
	if(chanel.length<2){
        var length = 2 - chanel.length;
        for(var i=0;i<length;i++){
        	chanel = "0" + chanel;
        }
      }
	var mach;
	if(machga!=null){
		for(var q=0;q<machga.length;q++){
			if(machga[q].id==node11.id){
				mach = parseInt(machga[q].gatherId).toString(16);
				if(mach.length<4){
					var length = 4 - mach.length;
			        for(var i=0;i<length;i++){
			        	mach = "0" + mach;
			        };
			        break;
				}
			}
		}
	}
	var xxx = "7E0901010156"+mach+chanel;
	var check = 0;
	for (var i = 0; i < (xxx.length/2); i++)
	{
		var tstr1=xxx.substring(i*2, i*2+2);
		var k=parseInt(tstr1,16);
		check += k;
	}

	var checksend = parseInt(check).toString(16);
	var a2 = checksend.length;
	checksend = checksend.substring(a2-2,a2);
	checksend = checksend.toUpperCase();
	socketfc.send(xxx+checksend+"7D");
	if(symbol==0){
		window.setTimeout(function() {
			if(symbol==0){
				socketfc.close();
				alert("索取失败");
			}
		}, 5000)
	}
	socketfc.onmessage = function(msg) {
		var da=msg.data;
/*        var strdata2=strdata1.replace(/7C20/g, '00');
        var strdata3=strdata2.replace(/7C5E/g, '7E');
        var strdata4=strdata3.replace(/7C5C/g, '7C');
        var da =strdata4.replace(/7C5D/g, '7D');*/
		if(da.substring(0,2)=="7E"&&da.substring(10,12)=="56"){
        if(da.substring(18,20)=="FF"){
    		symbol++;
    		socketfc.close();
    		if(socketfc.readyState!=1){
        		alert("此通道没有规范，请尝试新建规范，可恢复默认值进行参考");
    			}
        }else{
		$('#chanel').combobox('select',parseInt(da.substring(18,20),16));
		$("#ftime").numberbox('setValue',parseInt(da.substring(20,24),16));
		$("#fadvance").numberbox('setValue',parseInt(da.substring(24,28),16));
		$("#fini_ele").numberbox('setValue',parseInt(da.substring(28,32),16));
		$("#fini_vol").numberbox('setValue',(parseInt(da.substring(32,36),16)/10).toFixed(1));
		$("#fini_vol1").numberbox('setValue',(parseInt(da.substring(36,40),16)/10).toFixed(1));
		$("#fweld_ele").numberbox('setValue',parseInt(da.substring(40,44),16));
		$("#fweld_vol").numberbox('setValue',(parseInt(da.substring(44,48),16)/10).toFixed(1));
		$("#fweld_vol1").numberbox('setValue',(parseInt(da.substring(48,52),16)/10).toFixed(1));
		$("#farc_ele").numberbox('setValue',parseInt(da.substring(52,56),16));
		$("#farc_vol").numberbox('setValue',(parseInt(da.substring(56,60),16)/10).toFixed(1));
		$("#farc_vol1").numberbox('setValue',(parseInt(da.substring(60,64),16)/10).toFixed(1));
		$("#fhysteresis").numberbox('setValue',parseInt(da.substring(64,68),16));
		$("#fcharacter").numberbox('setValue',parseInt(da.substring(68,72),16));
		if(parseInt(da.substring(72,74),16)==0){
			$('#fgas').combobox('select',121);
		}else if(parseInt(da.substring(72,74),16)==1){
			$('#fgas').combobox('select',122);
		}else{
			$('#fgas').combobox('select',122);
		}
		if(parseInt(da.substring(74,76),16)==10){
			$('#fdiameter').combobox('select',131);
		}else if(parseInt(da.substring(74,76),16)==12){
			$('#fdiameter').combobox('select',132);
		}else if(parseInt(da.substring(74,76),16)==14){
			$('#fdiameter').combobox('select',133);
		}else{
			$('#fdiameter').combobox('select',134);
		}
		if(parseInt(da.substring(76,78),16)==0){
			$('#fmaterial').combobox('select',91);
		}else if(parseInt(da.substring(76,78),16)==1){
			$('#fmaterial').combobox('select',92);
		}else if(parseInt(da.substring(76,78),16)==4){
			$('#fmaterial').combobox('select',93);
		}else{
			$('#fmaterial').combobox('select',94);
		}
		var sconx = parseInt(da.substring(82,84),16);
		sconx = sconx.toString(2);
		if(sconx.length<8){
	        var length = 8 - sconx.length;
	        for(var i=0;i<length;i++){
	        	sconx = "0" + sconx;
	        }
	      }
		if(sconx.substring(7,8)=="1"){
			$("#finitial").prop("checked",true);
		}else{
			$("#finitial").prop("checked",false);
		}
		if(sconx.substring(6,7)=="0"){
			$('#farc').combobox('select',111);
		}else{
			$('#farc').combobox('select',112);
		}
		if(sconx.substring(5,6)=="1"){
			$('#farc').combobox('select',113);
		}
		if(sconx.substring(4,5)=="1"){
			$('#farc').combobox('select',114);
		}
		if(sconx.substring(2,3)=="0"){
			$('#fselect').combobox('select',102);
		}else{
			$('#fselect').combobox('select',101);
		}
		if(sconx.substring(1,2)=="1"){
			$("#fcontroller").prop("checked",true);
		}else{
			$("#fcontroller").prop("checked",false);
		}
		if(sconx.substring(0,1)=="1"){
			$("#fmode").prop("checked",true);
		}else{
			$("#fmode").prop("checked",false);
		}
//		parsevar(da.substring(68,70),16);
		$("#fweld_tuny_ele").numberbox('setValue',parseInt(da.substring(84,86),16));
		$("#farc_tuny_ele").numberbox('setValue',parseInt(da.substring(88,90),16));
		$("#fweld_tuny_vol").numberbox('setValue',(parseInt(da.substring(86,88),16)/10).toFixed(1));
		$("#farc_tuny_vol").numberbox('setValue',(parseInt(da.substring(90,92),16)/10).toFixed(1));
		$("#fweld_tuny_vol1").numberbox('setValue',(parseInt(da.substring(86,88),16)/10).toFixed(1));
		$("#farc_tuny_vol1").numberbox('setValue',(parseInt(da.substring(90,92),16)/10).toFixed(1));
		
		symbol++;
		socketfc.close();
		if(socketfc.readyState!=1){
			alert("索取成功");
			}
		}}
	}
}
}

function xiafa(){
    if($('#fadvance').numberbox('getValue')<0||$('#fadvance').numberbox('getValue')>100){
   	 alert("提前送气范围：0~100");
   	 return;
    }
    if($('#fini_ele').numberbox('getValue')<30||$('#fini_ele').numberbox('getValue')>550){
   	 alert("初期电流范围：30~550");
   	 return;
    }
    if($('#fini_vol').numberbox('getValue')<12||$('#fini_vol').numberbox('getValue')>50){
   	 alert("初期电压范围：12~50");
   	 return;
    }
    if($('#fini_vol1').numberbox('getValue')<(-30)||$('#fini_vol1').numberbox('getValue')>(30)){
   	 alert("初期电压一元范围：-30~30");
   	 return;
    }
    if($('#fweld_ele').numberbox('getValue')<30||$('#fweld_ele').numberbox('getValue')>550){
   	 alert("焊接电流范围：30~550");
   	 return;
    }
    if($('#fweld_vol').numberbox('getValue')<12||$('#fweld_vol').numberbox('getValue')>50){
   	 alert("焊接电压范围：12~50");
   	 return;
    }
    if($('#fweld_vol1').numberbox('getValue')<(-30)||$('#fweld_vol1').numberbox('getValue')>(30)){
   	 alert("焊接电压一元范围：-30~30");
   	 return;
    }
    if($('#farc_ele').numberbox('getValue')<30||$('#farc_ele').numberbox('getValue')>550){
   	 alert("收弧电流范围：30~550");
   	 return;
    }
    if($('#farc_vol').numberbox('getValue')<12||$('#farc_vol').numberbox('getValue')>50){
   	 alert("收弧电压范围：12~50");
   	 return;
    }
    if($('#farc_vol1').numberbox('getValue')<(-30)||$('#farc_vol1').numberbox('getValue')>(30)){
   	 alert("收弧电压一元范围：-30~30");
   	 return;
    }
    if($('#fhysteresis').numberbox('getValue')<0||$('#fhysteresis').numberbox('getValue')>100){
   	 alert("滞后送气范围：0~100");
   	 return;
    }
    if($('#fcharacter').numberbox('getValue')<(-99)||$('#fcharacter').numberbox('getValue')>(99)){
   	 alert("电弧特性范围：-99~99");
   	 return;
    }
	if($('#fselect').combobox('getValue')==102){
		if(($('#fweld_tuny_vol').numberbox('getValue')>5||$('#fweld_tuny_vol').numberbox('getValue')<0)){
			alert("个别模式下微调电压范围为0~5")
		}else{
			var socketfc=null;
			if(typeof(WebSocket) == "undefined") {
		    	WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
		    	WEB_SOCKET_DEBUG = true;
			}
			symbol1=0;
			socketfc = new WebSocket(data1);
			if(symbol1==0){
			window.setTimeout(function() {
				if(symbol1==0){
					alert("下发失败");
					socketfc.close();
				}
			}, 5000)
			}
			socketfc.onopen = function() {
				var chanel = parseInt($('#chanel').numberbox('getValue')).toString(16);
				if(chanel.length<2){
			        var length = 2 - chanel.length;
			        for(var i=0;i<length;i++){
			        	chanel = "0" + chanel;
			        }
			      }
				var ftime = parseInt($('#ftime').numberbox('getValue')).toString(16);
				if(ftime.length<4){
					var length = 4 - ftime.length;
			        for(var i=0;i<length;i++){
			        	ftime = "0" + ftime;
			        }
			      }
				var fadvance = parseInt($('#fadvance').numberbox('getValue')).toString(16);
				if(fadvance.length<4){
					var length = 4 - fadvance.length;
			        for(var i=0;i<length;i++){
			        	fadvance = "0" + fadvance;
			        }
			      }
				var fini_ele = parseInt($('#fini_ele').numberbox('getValue')).toString(16);
				if(fini_ele.length<4){
					var length = 4 - fini_ele.length;
			        for(var i=0;i<length;i++){
			        	fini_ele = "0" + fini_ele;
			        }
			      }
				var fini_vol = parseInt($('#fini_vol').numberbox('getValue')*10).toString(16);
				if(fini_vol.length<4){
					var length = 4 - fini_vol.length;
			        for(var i=0;i<length;i++){
			        	fini_vol = "0" + fini_vol;
			        }
			      }
				var fini_vol1 = parseInt($('#fini_vol1').numberbox('getValue')).toString(16);
				if(fini_vol1.length<4){
					var length = 4 - fini_vol1.length;
			        for(var i=0;i<length;i++){
			        	fini_vol1 = "0" + fini_vol1;
			        }
			      }
				var fweld_ele = parseInt($('#fweld_ele').numberbox('getValue')).toString(16);
				if(fweld_ele.length<4){
					var length = 4 - fweld_ele.length;
			        for(var i=0;i<length;i++){
			        	fweld_ele = "0" + fweld_ele;
			        }
			      }
				var fweld_vol = parseInt($('#fweld_vol').numberbox('getValue')*10).toString(16);
				if(fweld_vol.length<4){
					var length = 4 - fweld_vol.length;
			        for(var i=0;i<length;i++){
			        	fweld_vol = "0" + fweld_vol;
			        }
			      }
				var fweld_vol1 = parseInt($('#fweld_vol1').numberbox('getValue')).toString(16);
				if(fweld_vol1.length<4){
					var length = 4 - fweld_vol1.length;
			        for(var i=0;i<length;i++){
			        	fweld_vol1 = "0" + fweld_vol1;
			        }
			      }
				var farc_ele = parseInt($('#farc_ele').numberbox('getValue')).toString(16);
				if(farc_ele.length<4){
					var length = 4 - farc_ele.length;
			        for(var i=0;i<length;i++){
			        	farc_ele = "0" + farc_ele;
			        }
			      }
				var farc_vol = parseInt($('#farc_vol').numberbox('getValue')*10).toString(16);
				if(farc_vol.length<4){
					var length = 4 - farc_vol.length;
			        for(var i=0;i<length;i++){
			        	farc_vol = "0" + farc_vol;
			        }
			      }
				var farc_vol1 = parseInt($('#farc_vol1').numberbox('getValue')).toString(16);
				if(farc_vol1.length<4){
					var length = 4 - farc_vol1.length;
			        for(var i=0;i<length;i++){
			        	farc_vol1 = "0" + farc_vol1;
			        }
			      }
				var fhysteresis = parseInt($('#fhysteresis').numberbox('getValue')).toString(16);
				if(fhysteresis.length<4){
					var length = 4 - fhysteresis.length;
			        for(var i=0;i<length;i++){
			        	fhysteresis = "0" + fhysteresis;
			        }
			      }
				var fcharacter = parseInt($('#fcharacter').numberbox('getValue')).toString(16);
				if(fcharacter.length<4){
					var length = 4 - fcharacter.length;
			        for(var i=0;i<length;i++){
			        	fcharacter = "0" + fcharacter;
			        }
			      }
//				alert($('#fgas').combobox('getValue'));
				var fgas = parseInt($('#fgas').combobox('getValue')).toString(16);
				if(fgas==parseInt(121).toString(16)){
					fgas="0";
				}else if(fgas==parseInt(122).toString(16)){
					fgas="1";
				}else{
					fgas="3";
				}
				if(fgas.length<2){
					var length = 2 - fgas.length;
			        for(var i=0;i<length;i++){
			        	fgas = "0" + fgas;
			        }
			      }
//				alert($('#fdiameter').combobox('getValue'));
				var fdiameter = parseInt($('#fdiameter').combobox('getValue')).toString(16);
				if(fdiameter==parseInt(131).toString(16)){
					fdiameter="A";
				}else if(fdiameter==parseInt(132).toString(16)){
					fdiameter="C";
				}else if(fdiameter==parseInt(133).toString(16)){
					fdiameter="E";
				}else{
					fdiameter="10";
				}
				if(fdiameter.length<2){
					var length = 2 - fdiameter.length;
			        for(var i=0;i<length;i++){
			        	fdiameter = "0" + fdiameter;
			        }
			      }
//				alert($('#fmaterial').combobox('getValue'));
				var fmaterial = parseInt($('#fmaterial').combobox('getValue')).toString(16);
				if(fmaterial==parseInt(91).toString(16)){
					fmaterial="0";
				}else if(fmaterial==parseInt(92).toString(16)){
					fmaterial="1";
				}else if(fmaterial==parseInt(93).toString(16)){
					fmaterial="4";
				}else{
					fmaterial="5";
				}
				if(fmaterial.length<2){
			        var length = 2 - fmaterial.length;
			        for(var i=0;i<length;i++){
			        	fmaterial = "0" + fmaterial;
			        }
			      }
				var fweld_tuny_ele = parseInt($('#fweld_tuny_ele').numberbox('getValue')).toString(16);
				if(fweld_tuny_ele.length<2){
					var length = 2 - fweld_tuny_ele.length;
			        for(var i=0;i<length;i++){
			        	fweld_tuny_ele = "0" + fweld_tuny_ele;
			        }
			      }
				var fweld_tuny_vol = parseInt($('#fweld_tuny_vol').numberbox('getValue')*10).toString(16);
				if(fweld_tuny_vol.length<2){
					var length = 2 - fweld_tuny_vol.length;
			        for(var i=0;i<length;i++){
			        	fweld_tuny_vol = "0" + fweld_tuny_vol;
			        }
			      }
				var farc_tuny_ele = parseInt($('#farc_tuny_ele').numberbox('getValue')).toString(16);
				if(farc_tuny_ele.length<2){
					var length = 2 - farc_tuny_ele.length;
			        for(var i=0;i<length;i++){
			        	farc_tuny_ele = "0" + farc_tuny_ele;
			        }
			      }
				var farc_tuny_vol = parseInt($('#farc_tuny_vol').numberbox('getValue')*10).toString(16);
				if(farc_tuny_vol.length<2){
					var length = 2 - farc_tuny_vol.length;
			        for(var i=0;i<length;i++){
			        	farc_tuny_vol = "0" + farc_tuny_vol;
			        }
			      }
				var con="";
				if($('#finitial').is(':checked')){
					con="1"+con;
				}else{
					con="0"+con;
				}
				if($('#farc').combobox('getValue')==111){
					con="0000"+con;
				}else if($('#farc').combobox('getValue')==112){
					con="0001"+con;
				}else if($('#farc').combobox('getValue')==113){
					con="0010"+con;
				}else{
					con="0100"+con;
				}
				if($('#fselect').combobox('getValue')==101){
					con="1"+con;
				}else{
					con="0"+con;
				}
				if($('#fcontroller').is(':checked')){
					con="1"+con;
				}else{
					con="0"+con;
				}
				if($('#fmode').is(':checked')){
					con="1"+con;
				}else{
					con="0"+con;
				}
				con = parseInt(con,2);
				con = parseInt(con).toString(16);
				if(con.length<2){
					var length = 2 - con.length;
			        for(var i=0;i<length;i++){
			        	con = "0" + con;
			        }
			      }
				
				var mach;
				if(machga!=null){
					for(var q=0;q<machga.length;q++){
						if(machga[q].id==node11.id){
							if(machga[q].gatherId){
								mach = parseInt(machga[q].gatherId).toString(16);
								if(mach.length<4){
									var length = 4 - mach.length;
							        for(var i=0;i<length;i++){
							        	mach = "0" + mach;
							        };
							        break;
								}
							}else{
								alert("该焊机未对应采集编号!!!");
								socketfc.close();
								return;
							}
						}
					}
				}
				
			var xiafasend1 = mach+chanel+ftime+fadvance+fini_ele+fini_vol+fini_vol1+fweld_ele+fweld_vol+fweld_vol1+farc_ele+farc_vol+farc_vol1+fhysteresis+fcharacter+fgas
			+fdiameter+fmaterial+"0000"+con+fweld_tuny_ele+fweld_tuny_vol+farc_tuny_ele+farc_tuny_vol;
			
		/*	var xiafasend2 = xiafasend1.replace(/00/g, '7C20');
			var xiafasend3 = xiafasend2.replace(/7E/g, '7C5E');
			var xiafasend4 = xiafasend3.replace(/7C/g, '7C5C');
			var xiafasend = xiafasend4.replace(/7D/g, '7C5D').toUpperCase();*/
			var xxx = xiafasend1.toUpperCase();
			var data_length = ((parseInt(xxx.length)+12)/2).toString(16);
			if(data_length.length<2){
				var length = 2 - data_length.length;
		        for(var i=0;i<length;i++){
		        	data_length = "0" + data_length;
		        }
		    };
		    xxx="7E"+data_length+"01010152"+xiafasend1;
		    var check = 0;
			for (var i = 0; i < (xxx.length/2); i++)
			{
				var tstr1=xxx.substring(i*2, i*2+2);
				var k=parseInt(tstr1,16);
				check += k;
			}

			var checksend = parseInt(check).toString(16);
			var a2 = checksend.length;
			checksend = checksend.substring(a2-2,a2);
			checksend = checksend.toUpperCase();
			
			var xiafasend2 = (xxx+checksend).substring(2);
/*			var xiafasend4 = xiafasend2.replace(/7C/g, '7C5C');
			var xiafasend3 = xiafasend4.replace(/7E/g, '7C5E');
			var fuer="";
			for(var er=0;er<(xiafasend3.length/2);er++){
				if(xiafasend3.substring(er*2,er*2+2)=="00"){
					fuer = fuer+"7C20"
				}else{
					fuer = fuer+xiafasend3.substring(er*2,er*2+2);
				}
			}
			var xiafasend5 = fuer.replace(/7D/g, '7C5D').toUpperCase();
			
			var xiafasend = "7E" + xiafasend5 + "7D";
			socketfc.send(xiafasend);*/
			socketfc.send("7E"+xiafasend2+"7D");
			socketfc.onmessage = function(msg) {
				var fan = msg.data;
//				fan = fan.replace(/7C20/g, '00').toUpperCase();
				if(fan.substring(0,2)=="7E"&&fan.substring(10,12)=="52"){
					symbol1++;
					if(parseInt(fan.substring(18,20),16)==1){
						socketfc.close();
						if(socketfc.readyState!=1){
							alert("下发失败");
							}
					}else{
						socketfc.close();
						if(socketfc.readyState!=1){
//							save(1);
							alert("下发成功");
							}
					}
				}
			}
		}
		}
	}else{
		if(($('#fweld_tuny_vol1').numberbox('getValue')>20||$('#fweld_tuny_vol1').numberbox('getValue')<0)){
			alert("一元模式下微调电压范围为0~20")
		}else{
			var socketfc=null;
			if(typeof(WebSocket) == "undefined") {
		    	WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
		    	WEB_SOCKET_DEBUG = true;
			}
			/*$.messager.alert("提示", "正在下发，请稍等。。。");*/
			symbol1=0;
			socketfc = new WebSocket(data1);
			if(symbol1==0){
			window.setTimeout(function() {
				if(symbol1==0){
					alert("下发失败");
					socketfc.close();
				}
			}, 5000)
			}
				socketfc.onopen = function() {
					var chanel = parseInt($('#chanel').numberbox('getValue')).toString(16);
					if(chanel.length<2){
				        var length = 2 - chanel.length;
				        for(var i=0;i<length;i++){
				        	chanel = "0" + chanel;
				        }
				      }
					var ftime = parseInt($('#ftime').numberbox('getValue')).toString(16);
					if(ftime.length<4){
						var length = 4 - ftime.length;
				        for(var i=0;i<length;i++){
				        	ftime = "0" + ftime;
				        }
				      }
					var fadvance = parseInt($('#fadvance').numberbox('getValue')).toString(16);
					if(fadvance.length<4){
						var length = 4 - fadvance.length;
				        for(var i=0;i<length;i++){
				        	fadvance = "0" + fadvance;
				        }
				      }
					var fini_ele = parseInt($('#fini_ele').numberbox('getValue')).toString(16);
					if(fini_ele.length<4){
						var length = 4 - fini_ele.length;
				        for(var i=0;i<length;i++){
				        	fini_ele = "0" + fini_ele;
				        }
				      }
					var fini_vol = parseInt($('#fini_vol').numberbox('getValue')*10).toString(16);
					if(fini_vol.length<4){
						var length = 4 - fini_vol.length;
				        for(var i=0;i<length;i++){
				        	fini_vol = "0" + fini_vol;
				        }
				      }
					var fini_vol1 = parseInt($('#fini_vol1').numberbox('getValue')).toString(16);
					if(fini_vol1.length<4){
						var length = 4 - fini_vol1.length;
				        for(var i=0;i<length;i++){
				        	fini_vol1 = "0" + fini_vol1;
				        }
				      }
					var fweld_ele = parseInt($('#fweld_ele').numberbox('getValue')).toString(16);
					if(fweld_ele.length<4){
						var length = 4 - fweld_ele.length;
				        for(var i=0;i<length;i++){
				        	fweld_ele = "0" + fweld_ele;
				        }
				      }
					var fweld_vol = parseInt($('#fweld_vol').numberbox('getValue')*10).toString(16);
					if(fweld_vol.length<4){
						var length = 4 - fweld_vol.length;
				        for(var i=0;i<length;i++){
				        	fweld_vol = "0" + fweld_vol;
				        }
				      }
					var fweld_vol1 = parseInt($('#fweld_vol1').numberbox('getValue')).toString(16);
					if(fweld_vol1.length<4){
						var length = 4 - fweld_vol1.length;
				        for(var i=0;i<length;i++){
				        	fweld_vol1 = "0" + fweld_vol1;
				        }
				      }
					var farc_ele = parseInt($('#farc_ele').numberbox('getValue')).toString(16);
					if(farc_ele.length<4){
						var length = 4 - farc_ele.length;
				        for(var i=0;i<length;i++){
				        	farc_ele = "0" + farc_ele;
				        }
				      }
					var farc_vol = parseInt($('#farc_vol').numberbox('getValue')*10).toString(16);
					if(farc_vol.length<4){
						var length = 4 - farc_vol.length;
				        for(var i=0;i<length;i++){
				        	farc_vol = "0" + farc_vol;
				        }
				      }
					var farc_vol1 = parseInt($('#farc_vol1').numberbox('getValue')).toString(16);
					if(farc_vol1.length<4){
						var length = 4 - farc_vol1.length;
				        for(var i=0;i<length;i++){
				        	farc_vol1 = "0" + farc_vol1;
				        }
				      }
					var fhysteresis = parseInt($('#fhysteresis').numberbox('getValue')).toString(16);
					if(fhysteresis.length<4){
						var length = 4 - fhysteresis.length;
				        for(var i=0;i<length;i++){
				        	fhysteresis = "0" + fhysteresis;
				        }
				      }
					var fcharacter = parseInt($('#fcharacter').numberbox('getValue')).toString(16);
					if(fcharacter.length<4){
						var length = 4 - fcharacter.length;
				        for(var i=0;i<length;i++){
				        	fcharacter = "0" + fcharacter;
				        }
				      }
					var fgas = parseInt($('#fgas').combobox('getValue')).toString(16);
					if(fgas==parseInt(121).toString(16)){
						fgas="0";
					}else if(fgas==parseInt(122).toString(16)){
						fgas="1";
					}else{
						fgas="3";
					}
					if(fgas.length<2){
						var length = 2 - fgas.length;
				        for(var i=0;i<length;i++){
				        	fgas = "0" + fgas;
				        }
				      }
					var fdiameter = parseInt($('#fdiameter').combobox('getValue')).toString(16);
					if(fdiameter==parseInt(131).toString(16)){
						fdiameter="A";
					}else if(fdiameter==parseInt(132).toString(16)){
						fdiameter="C";
					}else if(fdiameter==parseInt(133).toString(16)){
						fdiameter="E";
					}else{
						fdiameter="10";
					}
					if(fdiameter.length<2){
						var length = 2 - fdiameter.length;
				        for(var i=0;i<length;i++){
				        	fdiameter = "0" + fdiameter;
				        }
				      }
					var fmaterial = parseInt($('#fmaterial').combobox('getValue')).toString(16);
					if(fmaterial==parseInt(91).toString(16)){
						fmaterial="0";
					}else if(fmaterial==parseInt(92).toString(16)){
						fmaterial="1";
					}else if(fmaterial==parseInt(93).toString(16)){
						fmaterial="4";
					}else{
						fmaterial="5";
					}
					if(fmaterial.length<2){
				        var length = 2 - fmaterial.length;
				        for(var i=0;i<length;i++){
				        	fmaterial = "0" + fmaterial;
				        }
				      }
					var fweld_tuny_ele = parseInt($('#fweld_tuny_ele').numberbox('getValue')).toString(16);
					if(fweld_tuny_ele.length<2){
						var length = 2 - fweld_tuny_ele.length;
				        for(var i=0;i<length;i++){
				        	fweld_tuny_ele = "0" + fweld_tuny_ele;
				        }
				      }
					var fweld_tuny_vol = parseInt($('#fweld_tuny_vol1').numberbox('getValue')).toString(16);
					if(fweld_tuny_vol.length<2){
						var length = 2 - fweld_tuny_vol.length;
				        for(var i=0;i<length;i++){
				        	fweld_tuny_vol = "0" + fweld_tuny_vol;
				        }
				      }
					var farc_tuny_ele = parseInt($('#farc_tuny_ele').numberbox('getValue')).toString(16);
					if(farc_tuny_ele.length<2){
						var length = 2 - farc_tuny_ele.length;
				        for(var i=0;i<length;i++){
				        	farc_tuny_ele = "0" + farc_tuny_ele;
				        }
				      }
					var farc_tuny_vol = parseInt($('#farc_tuny_vol1').numberbox('getValue')).toString(16);
					if(farc_tuny_vol.length<2){
						var length = 2 - farc_tuny_vol.length;
				        for(var i=0;i<length;i++){
				        	farc_tuny_vol = "0" + farc_tuny_vol;
				        }
				      }
					var con="";
					if($('#finitial').is(':checked')){
						con="1"+con;
					}else{
						con="0"+con;
					}
					if($('#farc').combobox('getValue')==111){
						con="0000"+con;
					}else if($('#farc').combobox('getValue')==112){
						con="0001"+con;
					}else if($('#farc').combobox('getValue')==113){
						con="0010"+con;
					}else{
						con="0100"+con;
					}
					if($('#fselect').combobox('getValue')==101){
						con="1"+con;
					}else{
						con="0"+con;
					}
					if($('#fcontroller').is(':checked')){
						con="1"+con;
					}else{
						con="0"+con;
					}
					if($('#fmode').is(':checked')){
						con="1"+con;
					}else{
						con="0"+con;
					}
					con = parseInt(con,2);
					con = parseInt(con).toString(16);
					if(con.length<2){
						var length = 2 - con.length;
				        for(var i=0;i<length;i++){
				        	con = "0" + con;
				        }
				      }
					
					var mach;
					if(machga!=null){
						for(var q=0;q<machga.length;q++){
							if(machga[q].id==node11.id){
								mach = parseInt(machga[q].gatherId).toString(16);
								if(mach.length<4){
									var length = 4 - mach.length;
							        for(var i=0;i<length;i++){
							        	mach = "0" + mach;
							        };
							        break;
								}
							}
						}
					}
					
			var xiafasend1 = mach+chanel+ftime+fadvance+fini_ele+fini_vol+fini_vol1+fweld_ele+fweld_vol+fweld_vol1+farc_ele+farc_vol+farc_vol1+fhysteresis+fcharacter+fgas
			+fdiameter+fmaterial+"0000"+con+fweld_tuny_ele+fweld_tuny_vol+farc_tuny_ele+farc_tuny_vol;
			
		/*	var xiafasend2 = xiafasend1.replace(/00/g, '7C20');
			var xiafasend3 = xiafasend2.replace(/7E/g, '7C5E');
			var xiafasend4 = xiafasend3.replace(/7C/g, '7C5C');
			var xiafasend = xiafasend4.replace(/7D/g, '7C5D').toUpperCase();*/
			var xxx = xiafasend1.toUpperCase();
			var data_length = ((parseInt(xxx.length)+12)/2).toString(16);
			if(data_length.length<2){
				var length = 2 - data_length.length;
		        for(var i=0;i<length;i++){
		        	data_length = "0" + data_length;
		        }
		    };
		    xxx="7E"+data_length+"01010152"+xiafasend1;
		    var check = 0;
			for (var i = 0; i < (xxx.length/2); i++)
			{
				var tstr1=xxx.substring(i*2, i*2+2);
				var k=parseInt(tstr1,16);
				check += k;
			}
			var checksend = parseInt(check).toString(16);
			var a2 = checksend.length;
			checksend = checksend.substring(a2-2,a2);
			checksend = checksend.toUpperCase();
			
			var xiafasend2 = (xxx+checksend).substring(2);
/*			var xiafasend4 = xiafasend2.replace(/7C/g, '7C5C');
			var xiafasend3 = xiafasend4.replace(/7E/g, '7C5E');
			var fuer="";
			for(var er=0;er<(xiafasend3.length/2);er++){
				if(xiafasend3.substring(er*2,er*2+2)=="00"){
					fuer = fuer+"7C20"
				}else{
					fuer = fuer+xiafasend3.substring(er*2,er*2+2);
				}
			}
			var xiafasend5 = fuer.replace(/7D/g, '7C5D').toUpperCase();
			
			var xiafasend = "7E" + xiafasend5 + "7D";
			socketfc.send(xiafasend);*/
			socketfc.send("7E"+xiafasend2+"7D");
			socketfc.onmessage = function(msg) {
				var fan = msg.data;
//				fan = fan.replace(/7C20/g, '00').toUpperCase();
				if(fan.substring(0,2)=="7E"&&fan.substring(10,12)=="52"){
					symbol1++;
					if(parseInt(fan.substring(18,20),10)==1){
						socketfc.close();
						if(socketfc.readyState==1){
							alert("下发失败");
							}
					}else{
						socketfc.close();
						if(socketfc.readyState!=1){
//							save(1);
							alert("下发成功");
							}
					}
				}
			}
		}
		}
	}
}

function yanzheng(){
     if($('#fadvance').numberbox('getValue')<0||$('#fadvance').numberbox('getValue')>100){
    	 alert("提前送气范围：0~100");
    	 return;
     }
     if($('#fini_ele').numberbox('getValue')<30||$('#fini_ele').numberbox('getValue')>550){
    	 alert("初期电流范围：30~550");
    	 return;
     }
     if($('#fini_vol').numberbox('getValue')<12||$('#fini_vol').numberbox('getValue')>50){
    	 alert("初期电压范围：12~50");
    	 return;
     }
     if($('#fini_vol1').numberbox('getValue')<(-30)||$('#fini_vol1').numberbox('getValue')>(30)){
    	 alert("初期电压一元范围：-30~30");
    	 return;
     }
     if($('#fweld_ele').numberbox('getValue')<30||$('#fweld_ele').numberbox('getValue')>550){
    	 alert("焊接电流范围：30~550");
    	 return;
     }
     if($('#fweld_vol').numberbox('getValue')<12||$('#fweld_vol').numberbox('getValue')>50){
    	 alert("焊接电压范围：12~50");
    	 return;
     }
     if($('#fweld_vol1').numberbox('getValue')<(-30)||$('#fweld_vol1').numberbox('getValue')>(30)){
    	 alert("焊接电压一元范围：-30~30");
    	 return;
     }
     if($('#farc_ele').numberbox('getValue')<30||$('#farc_ele').numberbox('getValue')>550){
    	 alert("收弧电流范围：30~550");
    	 return;
     }
     if($('#farc_vol').numberbox('getValue')<12||$('#farc_vol').numberbox('getValue')>50){
    	 alert("收弧电压范围：12~50");
    	 return;
     }
     if($('#farc_vol1').numberbox('getValue')<(-30)||$('#farc_vol1').numberbox('getValue')>(30)){
    	 alert("收弧电压一元范围：-30~30");
    	 return;
     }
     if($('#fhysteresis').numberbox('getValue')<0||$('#fhysteresis').numberbox('getValue')>100){
    	 alert("滞后送气范围：0~100");
    	 return;
     }
     if($('#fcharacter').numberbox('getValue')<(-99)||$('#fcharacter').numberbox('getValue')>(99)){
    	 alert("电弧特性范围：-99~99");
    	 return;
     }
/*	if($('#ftime').numberbox('getValue')==""){
		$("#ftime").numberbox('setValue',30.0);
	};
	if($('#fadvance').numberbox('getValue')==""){
		$("#fadvance").numberbox('setValue',1.0);
	}
	if($('#fini_ele').numberbox('getValue')==""){
		$("#fini_ele").numberbox('setValue',100.0);
	}
	if($('#fweld_ele').numberbox('getValue')==""){
		$("#fweld_ele").numberbox('setValue',100.0);
	}
	if($('#farc_ele').numberbox('getValue')==""){
		$("#farc_ele").numberbox('setValue',100.0);
	}
	if($('#fhysteresis').numberbox('getValue')==""){
		$("#fhysteresis").numberbox('setValue',1.0);
	}
	if($('#fcharacter').numberbox('getValue')==""){
		$("#fcharacter").numberbox('setValue',0);
	}
	if($('#fweld_tuny_ele').numberbox('getValue')==""){
		$("#fweld_tuny_ele").numberbox('setValue',0.0);
	}
	if($('#farc_tuny_ele').numberbox('getValue')==""){
		$("#farc_tuny_ele").numberbox('setValue',0.0);
	}
	if($('#fini_vol').numberbox('getValue')==""){
		$("#fini_vol").numberbox('setValue',19.0);
	}
	if($('#fweld_vol').numberbox('getValue')==""){
		$("#fweld_vol").numberbox('setValue',19.0);
	}
	if($('#farc_vol').numberbox('getValue')==""){
		$("#farc_vol").numberbox('setValue',19.0);
	}
	if($('#fini_vol1').numberbox('getValue')==""){
		$("#fini_vol1").numberbox('setValue',0.0);
	}
	if($('#fweld_vol1').numberbox('getValue')==""){
		$("#fweld_vol1").numberbox('setValue',0.0);
	}
	if($('#farc_vol1').numberbox('getValue')==""){
		$("#farc_vol1").numberbox('setValue',0.0);
	}
	if($('#fweld_tuny_vol').numberbox('getValue')==""){
		$("#fweld_tuny_vol").numberbox('setValue',0.0);
	}
	if($('#farc_tuny_vol').numberbox('getValue')==""){
		$("#farc_tuny_vol").numberbox('setValue',0.0);
	}
	if($('#fweld_tuny_vol1').numberbox('getValue')==""){
		$("#fweld_tuny_vol1").numberbox('setValue',0);
	}
	if($('#farc_tuny_vol1').numberbox('getValue')==""){
		$("#farc_tuny_vol1").numberbox('setValue',0);
	}*/
}

function copy(value){
	document.getElementById("mu").innerHTML="源目标焊机："+node11.text;
	$('#divro').window( {
		title : "目标焊机选择",
		modal : true
	});
	$('#divro').window('open');
	symbol2 = value;
}

function savecopy(){
	var smachine = node11.id;
	rows="";
	var rows = $("#ro").datagrid("getSelections");
    var str="";
	for(var i=0; i<rows.length; i++){
		str += rows[i].fid+",";
	};
	if(symbol2==1){
		var url="wps/findCount?mac="+smachine+"&str="+str+"&chanel="+"";
	}else{
		var chanel = $('#chanel').numberbox('getValue');
		var url="wps/findCount?mac="+smachine+"&str="+str+"&chanel="+chanel;
	}
	$("#ro1").datagrid( {
		fitColumns : true,
		height : $("#divro1").height(),
		width : $("#divro1").width(),
		idField : 'id',
		url : url,
		singleSelect : false,
		rownumbers : true,
        columns : [ [ {
			field : 'machineid',
			title : '焊机编号',
			width : 80,
			halign : "center",
			align : "left"
		},{
			field : 'insname',
			title : '所属班组',
			width : 80,
			halign : "center",
			align : "left"
		}, {
			field : 'num',
			title : '通道数',
			width : 80,
			halign : "center",
			align : "left"
		}, {
			field : 'readynum',
			title : '已完成数',
//			width : 80,
			halign : "center",
			align : "left"
		}
		] ]
	});
	var r = confirm("确认复制吗？");
	if(r==true){
		x=0;
		xx=0;
		$('#divro').dialog('close');
		$('#divro1').window( {
			title : "参数复制进行中，请稍等。。。",
			modal : true
		});
		$('#divro1').window('open');
		if(symbol2==1){
			var url1="wps/Spe?machine="+node11.id+"&chanel="+"";
		}else{
			var chanel1 = $('#chanel').numberbox('getValue');
			var url1="wps/Spe?machine="+node11.id+"&chanel="+chanel1;
		}
		$.ajax({  
		      type : "post",  
		      async : false,
		      url : url1,  
		      data : {},  
		      dataType : "json", //返回数据形式为json  
		      success : function(result) {
		          if (result) {
		        	yshu1 = eval(result.rows);
		        	}else{
		        		alert("未查询到相关数据，请尝试索取保存。");
		        	}
		      },
		      error : function(errorMsg) {  
		          alert("数据请求失败，请联系系统管理员!");  
		      }  
		 });
		
		socketfc = new WebSocket(data1);
		if(symbol1==0){
		window.setTimeout(function() {
			if(symbol1==0){
				alert("复制失败");
				rows.length=0;
				str="";
				$('#ro').datagrid('clearSelections');
//				socketfc.close();
			}
		}, 5000)
		}
		socketfc.onopen = function() {
			rows1 = $("#ro1").datagrid("getRows");
			ccp(rows[0].fequipment_no);
		}
		socketfc.onmessage = function(msg) {
			var fan = msg.data;
//			fan = fan.replace(/7C20/g, '00').toUpperCase();
			if(fan.substring(0,2)=="7E"&&fan.substring(10,12)=="52"){
				symbol1++;
				if(parseInt(fan.substring(18,20),16)==1){
					socketfc.close();
					if(socketfc.readyState==1){
						alert("复制失败");
						rows.length=0;
						str="";
						$('#ro').datagrid('clearSelections');
						}
				}else{
					rows1[xx].readynum=x;
					if("1-"+x==rows1[xx].num){
						rows1[xx].readynum="已完成";
						$('#ro1').datagrid('refreshRow', xx);
						xx++;
						if(xx==rows1.length){
								socketfc.close();
								if(socketfc.readyState!=1){
									wait();									
									alert("复制成功");
									x=0;
									xx=0;
									$('#divro1').dialog('close');
									rows1.length=0;
									rows.length=0;
									str="";
									$('#ro').datagrid('clearSelections');
							}
								
						}else{
							ccp(rows[xx].fequipment_no);
						}
					}else{
					$('#ro1').datagrid('refreshRow', xx);
					ccp(rows[xx].fequipment_no);
					}
				}
			}
			}
	}else{
	$('#divro').dialog('close');
}
}

function ccp(value){
	if("1-"+x==rows1[xx].num){
		x=0;
	}
	var chanel = parseInt(yshu1[x].FWPSNum).toString(16);
	if(chanel.length<2){
        var length = 2 - chanel.length;
        for(var i=0;i<length;i++){
        	chanel = "0" + chanel;
        }
      }
	var ftime = parseInt(yshu1[x].ftime).toString(16);
	if(ftime.length<4){
		var length = 4 - ftime.length;
        for(var i=0;i<length;i++){
        	ftime = "0" + ftime;
        }
      }
	var fadvance = parseInt(yshu1[x].fadvance).toString(16);
	if(fadvance.length<4){
		var length = 4 - fadvance.length;
        for(var i=0;i<length;i++){
        	fadvance = "0" + fadvance;
        }
      }
	var fini_ele = parseInt(yshu1[x].fini_ele).toString(16);
	if(fini_ele.length<4){
		var length = 4 - fini_ele.length;
        for(var i=0;i<length;i++){
        	fini_ele = "0" + fini_ele;
        }
      }
	var fini_vol = (parseInt(yshu1[x].fini_vol)*10).toString(16);
	if(fini_vol.length<4){
		var length = 4 - fini_vol.length;
        for(var i=0;i<length;i++){
        	fini_vol = "0" + fini_vol;
        }
      }
	var fini_vol1 = parseInt(yshu1[x].fini_vol1).toString(16);
	if(fini_vol1.length<4){
		var length = 4 - fini_vol1.length;
        for(var i=0;i<length;i++){
        	fini_vol1 = "0" + fini_vol1;
        }
      }
	var fweld_ele = parseInt(yshu1[x].fweld_ele).toString(16);
	if(fweld_ele.length<4){
		var length = 4 - fweld_ele.length;
        for(var i=0;i<length;i++){
        	fweld_ele = "0" + fweld_ele;
        }
      }
	var fweld_vol = (parseInt(yshu1[x].fweld_vol)*10).toString(16);
	if(fweld_vol.length<4){
		var length = 4 - fweld_vol.length;
        for(var i=0;i<length;i++){
        	fweld_vol = "0" + fweld_vol;
        }
      }
	var fweld_vol1 = parseInt(yshu1[x].fweld_vol1).toString(16);
	if(fweld_vol1.length<4){
		var length = 4 - fweld_vol1.length;
        for(var i=0;i<length;i++){
        	fweld_vol1 = "0" + fweld_vol1;
        }
      }
	var farc_ele = parseInt(yshu1[x].farc_ele).toString(16);
	if(farc_ele.length<4){
		var length = 4 - farc_ele.length;
        for(var i=0;i<length;i++){
        	farc_ele = "0" + farc_ele;
        }
      }
	var farc_vol = (parseInt(yshu1[x].farc_vol)*10).toString(16);
	if(farc_vol.length<4){
		var length = 4 - farc_vol.length;
        for(var i=0;i<length;i++){
        	farc_vol = "0" + farc_vol;
        }
      }
	var farc_vol1 = parseInt(yshu1[x].farc_vol1).toString(16);
	if(farc_vol1.length<4){
		var length = 4 - farc_vol1.length;
        for(var i=0;i<length;i++){
        	farc_vol1 = "0" + farc_vol1;
        }
      }
	var fhysteresis = parseInt(yshu1[x].fhysteresis).toString(16);
	if(fhysteresis.length<4){
		var length = 4 - fhysteresis.length;
        for(var i=0;i<length;i++){
        	fhysteresis = "0" + fhysteresis;
        }
      }
	var fcharacter = parseInt(yshu1[x].Fweld_V_MAX).toString(16);
	if(fcharacter.length<4){
		var length = 4 - fcharacter.length;
        for(var i=0;i<length;i++){
        	fcharacter = "0" + fcharacter;
        }
      }
	var fgas = parseInt(yshu1[0].Fweld_Alter_V).toString(16);
	if(fgas==parseInt(121).toString(16)){
		fgas="0";
	}else if(fgas==parseInt(122).toString(16)){
		fgas="1";
	}else{
		fgas="3";
	}
	if(fgas.length<2){
		var length = 2 - fgas.length;
        for(var i=0;i<length;i++){
        	fgas = "0" + fgas;
        }
      }
	var fdiameter = parseInt(yshu1[x].Fweld_PreChannel).toString(16);
	if(fdiameter==parseInt(131).toString(16)){
		fdiameter="A";
	}else if(fdiameter==parseInt(132).toString(16)){
		fdiameter="C";
	}else if(fdiameter==parseInt(133).toString(16)){
		fdiameter="E";
	}else{
		fdiameter="10";
	}
	if(fdiameter.length<2){
		var length = 2 - fdiameter.length;
        for(var i=0;i<length;i++){
        	fdiameter = "0" + fdiameter;
        }
      }
	var fmaterial = parseInt(yshu1[x].Fweld_Alter_I).toString(16);
	if(fmaterial==parseInt(91).toString(16)){
		fmaterial="0";
	}else if(fmaterial==parseInt(92).toString(16)){
		fmaterial="1";
	}else if(fmaterial==parseInt(93).toString(16)){
		fmaterial="4";
	}else{
		fmaterial="5";
	}
	if(fmaterial.length<2){
        var length = 2 - fmaterial.length;
        for(var i=0;i<length;i++){
        	fmaterial = "0" + fmaterial;
        }
      }
	var fweld_tuny_ele = parseInt(yshu1[x].fweld_tuny_ele).toString(16);
	if(fweld_tuny_ele.length<2){
		var length = 2 - fweld_tuny_ele.length;
        for(var i=0;i<length;i++){
        	fweld_tuny_ele = "0" + fweld_tuny_ele;
        }
      }
	var fweld_tuny_vol = parseInt(yshu1[x].fweld_tuny_vol).toString(16);
	if(fweld_tuny_vol.length<2){
		var length = 2 - fweld_tuny_vol.length;
        for(var i=0;i<length;i++){
        	fweld_tuny_vol = "0" + fweld_tuny_vol;
        }
      }
	var farc_tuny_ele = parseInt(yshu1[x].farc_tuny_ele).toString(16);
	if(farc_tuny_ele.length<2){
		var length = 2 - farc_tuny_ele.length;
        for(var i=0;i<length;i++){
        	farc_tuny_ele = "0" + farc_tuny_ele;
        }
      }
	var farc_tuny_vol = parseInt(yshu1[x].Fdiameter).toString(16);
	if(farc_tuny_vol.length<2){
		var length = 2 - farc_tuny_vol.length;
        for(var i=0;i<length;i++){
        	farc_tuny_vol = "0" + farc_tuny_vol;
        }
      }
	var con1="";
	con1=yshu1[x].Fweld_I+con1;
	if(yshu1[x].Fweld_I_MIN==111){
		con1="0000"+con1;
	}else if(yshu1[x].Fweld_I_MIN==112){
		con1="0001"+con1;
	}else if(yshu1[x].Fweld_I_MIN==113){
		con1="0010"+con1;
	}else{
		con1="0100"+con1;
	}
	if(yshu1[x].Fweld_I_MAX==101){
		con1="1"+con1;
	}else{
		con1="0"+con1;
	}
	con1=yshu1[x].Fweld_V+con1;
	con1=yshu1[x].Fweld_V_MIN+con1;
	con1 = parseInt(con1,2);
	con1 = parseInt(con1).toString(16);
	if(con1.length<2){
		var length = 2 - con1.length;
        for(var i=0;i<length;i++){
        	con1 = "0" + con1;
        }
      }
	var mach;
	if(machga!=null){
		for(var q=0;q<machga.length;q++){
			if(machga[q].id==node11.id){
				if(machga[q].gatherId){
					mach = parseInt(machga[q].gatherId).toString(16);
					if(mach.length<4){
						var length = 4 - mach.length;
				        for(var i=0;i<length;i++){
				        	mach = "0" + mach;
				        };
				        break;
					}
				}else{
					alert("该焊机未对应采集编号!!!");
					socketfc.close();
					return;
				}
			}
		}
	}
	
var xiafasend1 = mach+chanel+ftime+fadvance+fini_ele+fini_vol+fini_vol1+fweld_ele+fweld_vol+fweld_vol1+farc_ele+farc_vol+farc_vol1+fhysteresis+fcharacter+fgas
+fdiameter+fmaterial+"0000"+con1+fweld_tuny_ele+fweld_tuny_vol+farc_tuny_ele+farc_tuny_vol;

var xxx = xiafasend1.toUpperCase();
var data_length = ((parseInt(xxx.length)+12)/2).toString(16);
if(data_length.length<2){
	var length = 2 - data_length.length;
    for(var i=0;i<length;i++){
    	data_length = "0" + data_length;
    }
};
xxx="7E"+data_length+"01010152"+xiafasend1;
var check = 0;
for (var i = 0; i < (xxx.length/2); i++)
{
var tstr1=xxx.substring(i*2, i*2+2);
var k=parseInt(tstr1,16);
check += k;
}
var checksend = parseInt(check).toString(16);
var a2 = checksend.length;
checksend = checksend.substring(a2-2,a2);
checksend = checksend.toUpperCase();

var xiafasend2 = (xxx+checksend).substring(2);
/*var xiafasend4 = xiafasend2.replace(/7C/g, '7C5C');
var xiafasend3 = xiafasend4.replace(/7E/g, '7C5E');
var fuer="";
for(var er=0;er<(xiafasend3.length/2);er++){
if(xiafasend3.substring(er*2,er*2+2)=="00"){
	fuer = fuer+"7C20"
}else{
	fuer = fuer+xiafasend3.substring(er*2,er*2+2);
}
}
var xiafasend5 = fuer.replace(/7D/g, '7C5D').toUpperCase();

var xiafasend = "7E" + xiafasend5 + "7D";
socketfc.send(xiafasend);*/
socketfc.send("7E"+xiafasend2+"7D");
	x++;
}

function wait(){
	var smachine = node11.id;
	rows="";
	var rows = $("#ro").datagrid("getSelections");
    var str="";
	for(var i=0; i<rows.length; i++){
		str += rows[i].fid+",";
	};
	if(symbol2==1){
		var url="wps/saveCopy?mac="+smachine+"&str="+str+"&chanel="+"";
	}else{
		var chanel = $('#chanel').numberbox('getValue');
		var url="wps/saveCopy?mac="+smachine+"&str="+str+"&chanel="+chanel;
	}
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : url,  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$('#ro').datagrid('clearSelections');
					$('#ro').datagrid('reload');
					}
	      },
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	 });
}
