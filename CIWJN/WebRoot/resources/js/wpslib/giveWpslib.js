/**
 * 
 */
var websocketURL,mqttClintId;
var selectflag;
var wpslibindex;
var wpslibId;
var datagridFlag=0;
$(function() {
	itemcombobox();
	$.ajax({
		type : "post",
		async : false,
		url : "td/AllTdbf",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				websocketURL = eval(result.web_socket);
				mqttClintId = result.userName;
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	mqttTest();
})

//选择工艺
function selectMainWps(value,model){
	wpslibId = value;
	if(model == 149){
		flag = 1;
		$('#sxSelectdlg').window( {
			title : "选择工艺",
			modal : true
		});
		$('#sxSelectdlg').window('open');
		showSelectSxWps(value);
		return;
	}
	$('#smwfm').form('clear');
	$('#smwdlg').window( {
		title : "选择工艺",
		modal : true
	});
	$("#mainWpsTable").datagrid( {
		height : $("#smwdlg").height(),
		width : $("#smwdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "wps/getMainwpsList?parent="+value,
		singleSelect : false,
		rownumbers : true,
		showPageList : false, 
        columns : [ [ {
		    field:'ck',
			checkbox:true
		},{ 
			field : 'fid',
			title : 'id',
			halign : "center",
			align : "left",
			width : 30,
			hidden:true
		},{ 
			field : 'fchanel',
			title : '通道号',
			halign : "center",
			align : "left"
		},{ 
			field : 'finitial',
			title : '初期条件',
			halign : "center",
			align : "left"
		},{ 
			field : 'fcontroller',
			title : '熔深控制',
			halign : "center",
			align : "left"
		}, {
			field : 'fselectname',
			title : '一元/个别',
			halign : "center",
			align : "left"
		}, {
			field : 'fselect',
			title : '一元/个别id',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'farc',
			title : '收弧id',
			halign : "center",
			align : "left",
			hidden: true
		},{
			field : 'farcname',
			title : '收弧',
			halign : "center",
			align : "left"
		}, {
			field : 'fcharacter',
			title : '电弧特性',
			halign : "center",
			align : "left"
		}, {
			field : 'fmode',
			title : '柔软电弧模式',
			halign : "center",
			align : "left"
		}, {
			field : 'fmaterialname',
			title : '焊丝材质',
			halign : "center",
			align : "left"
		},{
			field : 'fmaterial',
			title : '焊丝材质id',
			halign : "center",
			align : "left",
			hidden : true
		},{
			field : 'fgasname',
			title : '气体',
			halign : "center",
			align : "left"
		},{
			field : 'fgas',
			title : '气体id',
			halign : "center",
			align : "left",
			hidden : true
		},{
			field : 'fdiametername',
			title : '焊丝直径',
			halign : "center",
			align : "left"
		},{
			field : 'fdiameter',
			title : '焊丝直径id',
			halign : "center",
			align : "left",
			hidden : true
		},{
			field : 'ftime',
			title : '点焊时间',
			halign : "center",
			align : "left"
		},{
			field : 'fadvance',
			title : '提前送气',
			halign : "center",
			align : "left"
		},{
			field : 'fhysteresis',
			title : '滞后送气',
			halign : "center",
			align : "left"
		},{
			field : 'fini_ele',
			title : '初期电流',
			halign : "center",
			align : "left"
		},{
			field : 'fini_vol',
			title : '初期电压',
			halign : "center",
			align : "left"
		},{
			field : 'fini_vol1',
			title : '初期电压一元',
			halign : "center",
			align : "left"
		},{
			field : 'fweld_ele',
			title : '焊接电流',
			halign : "center",
			align : "left"
		},{
			field : 'fweld_vol',
			title : '焊接电压',
			halign : "center",
			align : "left"
		},{
			field : 'fweld_vol1',
			title : '焊接电压一元',
			halign : "center",
			align : "left"
		},{
			field : 'farc_ele',
			title : '收弧电流',
			halign : "center",
			align : "left"
		},{
			field : 'farc_vol',
			title : '收弧电压',
			halign : "center",
			align : "left"
		},{
			field : 'farc_vol1',
			title : '收弧电压一元',
			halign : "center",
			align : "left"
		},{
			field : 'fweld_tuny_ele',
			title : '焊接电流微调',
			halign : "center",
			align : "left"
		},{
			field : 'fweld_tuny_vol',
			title : '焊接电压微调',
			halign : "center",
			align : "left"
		},{
			field : 'farc_tuny_ele',
			title : '收弧电流微调',
			halign : "center",
			align : "left"
		},{
			field : 'farc_tuny_vol',
			title : '收弧电压微调',
			halign : "center",
			align : "left"
		},{
			field : 'fwarn_ele_up',
			title : '报警电流上限',
			halign : "center",
			align : "left"
		},{
			field : 'fwarn_ele_down',
			title : '报警电流下限',
			halign : "center",
			align : "left"
		},{
			field : 'fwarn_vol_up',
			title : '报警电压上限',
			halign : "center",
			align : "left"
		},{
			field : 'fwarn_vol_down',
			title : '报警电压下限',
			halign : "center",
			align : "left"
		}
		] ],
		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
//	                color.class="rowColor";
                return color;
            }
        }
	});
	$('#smwdlg').window('open');
/*	if($('#ddv-'+value).datagrid()){
		var rows = $('#ddv-'+value).datagrid('getSelections');
		if(rows.length==0){
			alert("请先选择工艺!!!");
		}else{
			selectMachineList(1);
		}
	}*/
}

//松下工艺选择
function showSelectSxWps(id) {
	$("#sxSelectWpsTab").datagrid({
		height : ($("#sxSelectdlg").height()),
		width : $("#sxSelectdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "wps/getSxWpsList?fwpslib_id="+id,
		rownumbers : true,
		pagination : true,
		showPageList : false,
		columns : [ [ {
		    field:'ck',
			checkbox:true
		},{ 
			field : 'fid',
			title : 'FID',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'fwpsnum',
			title : '通道号',
			halign : "center",
			align : "left"
		}, {
			field : 'fmaterial',
			title : '材质',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'materialname',
			title : '材质',
			halign : "center",
			align : "left"
		}, {
			field : 'fdiameter',
			title : '丝径',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'dianame',
			title : '丝径',
			halign : "center",
			align : "left"
		}, {
			field : 'fgas',
			title : '气体',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'gasname',
			title : '气体',
			halign : "center",
			align : "left"
		}, {
			field : 'fcontroller',
			title : '焊接控制',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'fcontrollername',
			title : '焊接控制',
			halign : "center",
			align : "left"
		}, {
			field : 'farc',
			title : '脉冲有无',
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'farcname',
			title : '脉冲有无',
			halign : "center",
			align : "left"
		}, {
			field : 'selectname',
			title : '分别/一元',
			halign : "center",
			align : "left"
		}, /*{
			field : 'ininame',
			title : '干伸长度',
			halign : "center",
			align : "left"
		}, */{
			field : 'fselect',
			title : '分别/一元',
			halign : "center",
			align : "left",
			hidden : true
		},  {
			field : 'ftime',
			title : '点焊时间',
			halign : "center",
			align : "left"
		}, {
			field : 'farc_delay_time',
			title : '启弧延时时间',
			halign : "center",
			align : "left"
		}, {
			field : 'fwarn_delay_time',
			title : '报警延时时间',
			halign : "center",
			align : "left"
		},/*{
			field : 'finitial',
			title : '干伸长度',
			halign : "center",
			align : "left",
			hidden : true
		},*/ {
			field : 'fpreset_ele_top',
			title : '预置电流上限',
			halign : "center",
			align : "left"
		}, {
			field : 'fpreset_ele_bottom',
			title : '预置电流下限',
			halign : "center",
			align : "left"
		}, {
			field : 'fpreset_vol_top',
			title : '预置电压上限',
			halign : "center",
			align : "left"
		}, {
			field : 'fpreset_vol_bottom',
			title : '预置电压下限',
			halign : "center",
			align : "left"
		}, {
			field : 'fini_vol1',
			title : '初期电流上限',
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_tuny_ele',
			title : '初期电流下限',
			halign : "center",
			align : "left"
		}, {
			field : 'farc_vol',
			title : '初期电压上限',
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_tuny_vol',
			title : '初期电压下线',
			halign : "center",
			align : "left"
		}, {
			field : 'fweld_vol1',
			title : '收弧电流上线',
			halign : "center",
			align : "left"
		}, {
			field : 'farc_tuny_ele',
			title : '收弧电流下线',
			halign : "center",
			align : "left"
		}, {
			field : 'farc_vol_top',
			title : '收弧电压上线',
			halign : "center",
			align : "left"
		}, {
			field : 'farc_tuny_vol',
			title : '收弧电压下线',
			halign : "center",
			align : "left"
		} ] ],
		rowStyler : function(index, row) {
			if ((index % 2) != 0) {
				//处理行代背景色后无法选中
				var color = new Object();
				return color;
			}
		}
	});
}


//选择焊机
function selectMachineList(value){
	if(value==1){
		var rows = $('#mainWpsTable').datagrid('getSelections');
		if(rows.length==0){
			alert("请先选择工艺!!!");
			return;
		}
	}
	var url = "";
	if(value==0||value==1){
		var wpslibrow = $('#wpslibTable').datagrid('getSelected');
		url = "weldingMachine/getWedlingMachineListNoPage?searchStr="+"w.fmodel="+wpslibrow.model;
	}else if(value==2){
		url = "weldingMachine/getWedlingMachineListNoPage?searchStr="+"w.fmanufacturer_id="+147;
		if(!$('#passwd').numberbox('getValue')){
			alert("密码不能为空");
			return;
		}
		if(parseInt($('#passwd').numberbox('getValue'))<1||parseInt($('#passwd').numberbox('getValue'))>999){
			alert("密码范围是1~999");
			return;
		}
	}else if(value==3){
		url = "weldingMachine/getWedlingMachineListNoPage?searchStr="+"w.fmanufacturer_id="+147;
	}else if(value==4){
		url = "weldingMachine/getWedlingMachineListNoPage?searchStr="+"w.fmanufacturer_id="+149;
	}else if(value==5){
		url = "weldingMachine/getWedlingMachineListNoPage?searchStr="+"w.fmanufacturer_id="+149;
	}
	$('#smfm').form('clear');
	$('#smdlg').window( {
		title : "选择焊机",
		modal : true
	});
	if(datagridFlag==0){
	$("#weldingmachineTable").datagrid( {
		height : $("#smdlg").height()-40,
		width : $("#smdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : url,
		singleSelect : true,
//		rownumbers : true,
//		showPageList : false, 
        columns : [ [ {
		    field:'ck',
			checkbox:true
		},{
			field : 'id',
			title : '序号',
			width : 50,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'equipmentNo',
			title : '固定资产编号',
//				width : 80,
			halign : "center",
			align : "left"
		}, {
			field : 'typeName',
			title : '设备类型',
//				width : 80,
			halign : "center",
			align : "left"
		}, /*{
				field : 'joinTime',
				title : '入厂时间',
//				width : 150,
				halign : "center",
				align : "left"
			}, */{
				field : 'insframeworkName',
				title : '所属项目',
//				width : 80,
				halign : "center",
				align : "left"
			}, {
			field : 'statusName',
			title : '状态',
//				width : 80,
			halign : "center",
			align : "left"
		} , {
			field : 'manufacturerName',
			title : '厂家',
//				width : 150,
			halign : "center",
			align : "left"
		}, /*{
				field : 'isnetworking',
				title : '是否在网',
//				width : 80,
				halign : "center",
				align : "left"
			},*/ {
			field : 'gatherId',
			title : '采集序号',
//				width : 100,
			halign : "center",
			align : "left"
		}, /*{
				field : 'position',
				title : '位置',
//				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'ip',
				title : 'ip地址',
//				width : 100,
				halign : "center",
				align : "left"
			}, {
				field : 'model',
				title : '设备型号',
//				width : 100,
				halign : "center",
				align : "left"
			},*/ {
			field : 'statusId',
			title : '状态id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'isnetworkingId',
			title : '是否联网id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'manuno',
			title : '厂商id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'typeId',
			title : '类型id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'iId',
			title : '项目id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}, {
			field : 'gid',
			title : '采集id',
			width : 100,
			halign : "center",
			align : "left",
			hidden: true
		}
		] ],
//		pagination : true,
//			fitColumns : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
//	                color.class="rowColor";
                return color;
            }
        }
	});
	datagridFlag = 1;
	}
	if(value==1){
		$("#weldingmachineTable").datagrid({
			singleSelect: false
		});
		selectflag=1;
	}else if(value==2){
		$("#weldingmachineTable").datagrid({
			singleSelect: true
		});
		selectflag=2;
	}else if(value==3){
		$("#weldingmachineTable").datagrid({
			singleSelect: true
		});
		selectflag=3;
	}else if(value==0){
		$("#weldingmachineTable").datagrid({
			singleSelect: true
		});
		selectflag=0;
	}else if(value==4){
		$("#weldingmachineTable").datagrid({
			singleSelect: true
		});
		selectflag=4;
	}else if(value==5){
		$("#weldingmachineTable").datagrid({
			singleSelect: true
		});
		selectflag=5;
	}
	$('#smdlg').window('open');
}

function selectModel(){
	if(selectflag==0){
		requestWps();
	}else if(selectflag==1){
		giveMainWps();
	}else if(selectflag==2){
		passfun();
	}else if(selectflag==3){
		controlfun();
	}else if(selectflag==4){
		sxMachineIsLock(1);
	}else if(selectflag==5){
		sxMachineIsLock(0);
	}else{
		return;
	}
}

function showResult(){
	$('#resultfm').form('clear');
	$('#resultdlg').window( {
		title : "下发中，请稍等。。。",
		modal : true
	});
	$("#giveResultTable").datagrid( {
		height : $("#resultdlg").height(),
		width : $("#resultdlg").width(),
		idField : 'id',
/*		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],*/
		url : "/",
		singleSelect : true,
		rownumbers : false,
//		showPageList : false, 
        columns : [ [ {
			field : 'machineNo',
			title : '焊机编号',
			width : 80,
			halign : "center",
			align : "left"
		}, {
			field : 'gatherNo',
			title : '采集序号',
			width : 80,
			halign : "center",
			align : "left"
		}, {
			field : 'successNum',
			title : '成功通道',
			width : 300,
			halign : "center",
			align : "left"
		}, {
			field : 'failNum',
			title : '失败通道',
			width : 300,
			halign : "center",
			align : "left"
		}, {
			field : 'noNum',
			title : '未响应通道',
			width : 300,
			halign : "center",
			align : "left"
		}
		] ],
//		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        }
	});
	$('#resultdlg').window('open');
}

//索取规范
function requestWps() {
	var selectMachine = $('#weldingmachineTable').datagrid('getSelected');
	if (selectMachine) {
		if (!selectMachine.gatherId) {
			alert("该焊机未绑定采集模块");
			return;
		}
	} else {
		alert("请选择焊机");
		return;
	}
	var flag = 0;
//	var websocket = null;
//	if (typeof (WebSocket) == "undefined") {
//		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
//		WEB_SOCKET_DEBUG = true;
//	}
//	websocket = new WebSocket(websocketUrl);
//	websocket.onopen = function() {
		var chanel = $('#fchanel').combobox('getValue').toString(16);
		if (chanel.length < 2) {
			var length = 2 - chanel.length;
			for (var i = 0; i < length; i++) {
				chanel = "0" + chanel;
			}
		}
		var mach = parseInt(selectMachine.gatherId).toString(16);
		if (mach.length < 4) {
			var length = 4 - mach.length;
			for (var i = 0; i < length; i++) {
				mach = "0" + mach;
			}
			;
		}
		var xxx = "7E0901010156" + mach + chanel;
		var check = 0;
		for (var i = 0; i < (xxx.length / 2); i++) {
			var tstr1 = xxx.substring(i * 2, i * 2 + 2);
			var k = parseInt(tstr1, 16);
			check += k;
		}

		var checksend = parseInt(check).toString(16);
		var a2 = checksend.length;
		checksend = checksend.substring(a2 - 2, a2);
		checksend = checksend.toUpperCase();
		var symbol = 0;
		var message = new Paho.MQTT.Message(xxx + checksend + "7D");
		message.destinationName = "weldmes/downparams";
		client.send(message);
//		websocket.send(xxx + checksend + "7D");
//		if (flag == 0) {
//			var jctimer = window.setTimeout(function() {
//				if (flag == 0) {
//					websocket.close();
//					alert("焊机长时间未响应，索取失败!!!");
//				}
//			}, 60000)
//		}
		var oneMinuteTimer = window.setTimeout(function() {
			if (symbol == 0) {
				client.unsubscribe("weldmes/upparams", {
					onSuccess : function(e) {
						console.log("取消订阅成功");
					},
					onFailure : function(e) {
						console.log(e);
					}
				})
//				$('#buttonCancel').linkbutton('enable');
//				$('#buttonOk').linkbutton('enable');
				alert("下发超时");
			}
		}, 5000);
		client.subscribe("weldmes/upparams", {
			qos: 0,
			onSuccess:function(e){  
	            console.log("订阅成功");  
	        },
	        onFailure: function(e){  
	            console.log(e);  
	        }
		})
		client.onMessageArrived = function(e){
			console.log("onMessageArrived:" + e.payloadString);
			var da = e.payloadString;
			if (da.substring(0, 2) == "7E" && da.substring(10, 12) == "56") {
				if (da.substring(18, 20) == "FF") {
					flag++;
//					websocket.close();
//					if (websocket.readyState != 1) {
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					});
					alert("此通道没有规范!!!");
					flag = 0;
					window.clearTimeout(oneMinuteTimer);
					symbol = 1;
//					}
				} else {
					var wpslibrow = $('#wpslibTable').datagrid("getSelected");
					if (wpslibrow.model == 174) {
						EPWGET(da);
					} else if (wpslibrow.model == 175) {
						EPSGET(da);
					} else if (wpslibrow.model == 176) {
						WBMLGET(da);
					} else if (wpslibrow.model == 177) {
						WBPGET(da);
					} else if (wpslibrow.model == 178) {
						WBLGET(da);
					} else if (wpslibrow.model == 171) {
						CPVEWGET(da);
					}
					flag++;
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					});
					flag = 0;
					window.clearTimeout(oneMinuteTimer);
					symbol = 1;
					alert("索取成功");
					$('#smdlg').window('close');
					$('#weldingmachineTable').datagrid('clearSelections');
					$('#smdlg').form('clear');
				}
			}
		}
//	}
}

//下发规范
function giveMainWps(){
	var wpslibrow = $('#wpslibTable').datagrid("getSelected");
	if(wpslibrow.model==174){
		if(EPW()==false){
			return;
		}
	}else if(wpslibrow.model==175){
		if(EPS()==false){
			return;
		}
	}else if(wpslibrow.model==176){
		if(WBML()==false){
			return;
		}
	}else if(wpslibrow.model==177){
		if(WBP()==false){
			return;
		}
	}else if(wpslibrow.model==178){
		if(WBL()==false){
			return;
		}
	}else if(wpslibrow.model==171){
		if(CPVEW(wpslibId)==false){
			return;
		}
	}
}

//控制命令下发
function openCondlg(){
	$('#condlg').window( {
		title : "控制命令下发",
		modal : true
	});
	$('#condlg').window("open");
}

//松下选择焊机
function selectSxMachineList(status){
	if(status==1){
		var rows = $('#sxSelectWpsTab').datagrid('getSelections');
		if(rows.length==0){
			alert("请先选择工艺!!!");
			return;
		}
	}else{
		if(!$('#sxfwpsnum').combobox('getValue')){
			alert("请先选择通道号！");
			return;
		}
	}
	$('#sxmachinefm').form('clear');
	$('#sxMachinedlg').window( {
		title : "选择焊机",
		modal : true
	});
	if(status==1){
		$("#sxMachineTable").datagrid( {
			height : $("#sxMachinedlg").height()-40,
			width : $("#sxMachinedlg").width(),
			idField : 'id',
//			pageSize : 10,
//			pageList : [ 10, 20, 30, 40, 50 ],
			url : "weldingMachine/getWedlingMachineListNoPage?searchStr=w.fmanufacturer_id=149",
			singleSelect : false,
//			rownumbers : true,
//			showPageList : false, 
	        columns : [ [ {
			    field:'ck',
				checkbox:true
			},{
				field : 'id',
				title : '序号',
				width : 50,
				halign : "center",
				align : "left",
				hidden:true
			}, {
				field : 'equipmentNo',
				title : '固定资产编号',
				halign : "center",
				align : "left"
			}, {
				field : 'typeName',
				title : '设备类型',
				halign : "center",
				align : "left"
			}, {
				field : 'statusName',
				title : '状态',
				halign : "center",
				align : "left"
			}, {
				field : 'manufacturerName',
				title : '厂家',
				halign : "center",
				align : "left"
			}, {
				field : 'gatherId',
				title : '采集序号',
				halign : "center",
				align : "left"
			}, {
				field : 'insframeworkName',
				title : '所属项目',
//				width : 80,
				halign : "center",
				align : "left"
			}, {
				field : 'statusId',
				title : '状态id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'isnetworkingId',
				title : '是否联网id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'manuno',
				title : '厂商id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'typeId',
				title : '类型id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'iId',
				title : '项目id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'gid',
				title : '采集id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}
			] ],
//			pagination : true,
			rowStyler: function(index,row){
	            if ((index % 2)!=0){
	            	//处理行代背景色后无法选中
	            	var color=new Object();
	                return color;
	            }
	        }
		});
		selectflag=1;
	}else{
		$("#sxMachineTable").datagrid( {
			height : $("#sxMachinedlg").height()-40,
			width : $("#sxMachinedlg").width(),
			idField : 'id',
//			pageSize : 10,
//			pageList : [ 10, 20, 30, 40, 50 ],
			url : "weldingMachine/getWedlingMachineListNoPage?searchStr=w.fmanufacturer_id=149",
			singleSelect : true,
//			rownumbers : true,
//			showPageList : false, 
	        columns : [ [ {
			    field:'ck',
				checkbox:true
			},{
				field : 'id',
				title : '序号',
				width : 50,
				halign : "center",
				align : "left",
				hidden:true
			}, {
				field : 'equipmentNo',
				title : '固定资产编号',
				halign : "center",
				align : "left"
			}, {
				field : 'typeName',
				title : '设备类型',
				halign : "center",
				align : "left"
			}, {
				field : 'statusName',
				title : '状态',
				halign : "center",
				align : "left"
			}, {
				field : 'manufacturerName',
				title : '厂家',
				halign : "center",
				align : "left"
			}, {
				field : 'gatherId',
				title : '采集序号',
				halign : "center",
				align : "left"
			}, {
				field : 'insframeworkName',
				title : '所属项目',
//				width : 80,
				halign : "center",
				align : "left"
			}, {
				field : 'statusId',
				title : '状态id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'isnetworkingId',
				title : '是否联网id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'manuno',
				title : '厂商id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'typeId',
				title : '类型id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'iId',
				title : '项目id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}, {
				field : 'gid',
				title : '采集id',
				width : 100,
				halign : "center",
				align : "left",
				hidden: true
			}
			] ],
//			pagination : true,
			rowStyler: function(index,row){
	            if ((index % 2)!=0){
	            	//处理行代背景色后无法选中
	            	var color=new Object();
	                return color;
	            }
	        }
		});
		selectflag=0;
	}
	$('#sxMachinedlg').window('open');
}


function selectSxModel(){
	if(selectflag==0){
		getSxMainWps();//索取
	}else if(selectflag==1){
		setSxMainWps();//下发
	}else{
		return;
	}
}

//松下下发规范
function setSxMainWps() {
	var selectMainWpsRows = $('#sxSelectWpsTab').datagrid('getSelections');
	var selectMachine = $('#sxMachineTable').datagrid('getSelections');
	if (selectMachine.length == 0) {
		alert("请先选择焊机!!!");
		return;
	}
	for (var m = 0; m < selectMachine.length; m++) {
		if (!selectMachine[m].gatherId) {
			alert(selectMachine[m].equipmentNo + "未绑定采集模块，请重新选择!!!");
			return;
		}
	}
	var symbol = 0;
//	var websocket = null;
//	if (typeof (WebSocket) == "undefined") {
//		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
//		WEB_SOCKET_DEBUG = true;
//	}
//	symbol = 0;
//	websocket = new WebSocket(websocketUrl);
	var sochet_send_data = new Array();
	var giveArray = new Array();
	var resultData = new Array();
	var noReceiveGiveChanel = new Array();
	var realLength = 0;
//	websocket.onopen = function() {
		var checkLength = selectMachine.length * selectMainWpsRows.length;
		for (var smindex = 0; smindex < selectMachine.length; smindex++) {
			noReceiveGiveChanel.length = 0;
			//处理工艺，转换成16进制
			for (var mwindex = 0; mwindex < selectMainWpsRows.length; mwindex++) {
				var crc7_str = [];
				crc7_str.push("FE");
				crc7_str.push("5A");
				crc7_str.push("A5");
				var data_length = parseInt("110").toString(16);
				crc7_str.push(data_length);
				if (data_length.length < 4) {
					var length = 4 - data_length.length;
					for (var i = 0; i < length; i++) {
						data_length = "0" + data_length;
					}
				}
				var equipmentNo = (parseInt(selectMachine[smindex].gatherId)).toString(16);
				crc7_str.push(equipmentNo);
				if (equipmentNo.length < 4) {
					var length = 4 - equipmentNo.length;
					for (var i = 0; i < length; i++) {
						equipmentNo = "0" + equipmentNo;
					}
				}
				crc7_str.push("0");
				crc7_str.push("211");
				crc7_str.push("2");
				var chanel = parseInt(selectMainWpsRows[mwindex].fwpsnum).toString(16);
				crc7_str.push(chanel);
				if (chanel.length < 2) {
					var length = 2 - chanel.length;
					for (var i = 0; i < length; i++) {
						chanel = "0" + chanel;
					}
				}
				var fpreset_ele_top = parseInt(selectMainWpsRows[mwindex].fpreset_ele_top).toString(16);
				crc7_str.push(fpreset_ele_top);
				if (fpreset_ele_top.length < 4) {
					var length = 4 - fpreset_ele_top.length;
					for (var i = 0; i < length; i++) {
						fpreset_ele_top = "0" + fpreset_ele_top;
					}
				}
				var fpreset_vol_top = parseInt(selectMainWpsRows[mwindex].fpreset_vol_top * 10).toString(16);
				crc7_str.push(fpreset_vol_top);
				if (fpreset_vol_top.length < 4) {
					var length = 4 - fpreset_vol_top.length;
					for (var i = 0; i < length; i++) {
						fpreset_vol_top = "0" + fpreset_vol_top;
					}
				}
				var fpreset_ele_bottom = parseInt(selectMainWpsRows[mwindex].fpreset_ele_bottom).toString(16);
				crc7_str.push(fpreset_ele_bottom);
				if (fpreset_ele_bottom.length < 4) {
					var length = 4 - fpreset_ele_bottom.length;
					for (var i = 0; i < length; i++) {
						fpreset_ele_bottom = "0" + fpreset_ele_bottom;
					}
				}
				var fpreset_vol_bottom = parseInt(selectMainWpsRows[mwindex].fpreset_vol_bottom * 10).toString(16);
				crc7_str.push(fpreset_vol_bottom);
				if (fpreset_vol_bottom.length < 4) {
					var length = 4 - fpreset_vol_bottom.length;
					for (var i = 0; i < length; i++) {
						fpreset_vol_bottom = "0" + fpreset_vol_bottom;
					}
				}
				var fini_vol1 = parseInt(selectMainWpsRows[mwindex].fini_vol1).toString(16);
				crc7_str.push(fini_vol1);
				if (fini_vol1.length < 4) {
					var length = 4 - fini_vol1.length;
					for (var i = 0; i < length; i++) {
						fini_vol1 = "0" + fini_vol1;
					}
				}
				var farc_vol1 = parseInt(selectMainWpsRows[mwindex].farc_vol1 * 10).toString(16);
				crc7_str.push(farc_vol1);
				if (farc_vol1.length < 4) {
					var length = 4 - farc_vol1.length;
					for (var i = 0; i < length; i++) {
						farc_vol1 = "0" + farc_vol1;
					}
				}
				var fweld_tuny_ele = parseInt(selectMainWpsRows[mwindex].fweld_tuny_ele).toString(16);
				crc7_str.push(fweld_tuny_ele);
				if (fweld_tuny_ele.length < 4) {
					var length = 4 - fweld_tuny_ele.length;
					for (var i = 0; i < length; i++) {
						fweld_tuny_ele = "0" + fweld_tuny_ele;
					}
				}
				var fweld_tuny_vol = parseInt(selectMainWpsRows[mwindex].fweld_tuny_vol * 10).toString(16);
				crc7_str.push(fweld_tuny_vol);
				if (fweld_tuny_vol.length < 4) {
					var length = 4 - fweld_tuny_vol.length;
					for (var i = 0; i < length; i++) {
						fweld_tuny_vol = "0" + fweld_tuny_vol;
					}
				}
				var fweld_vol1 = parseInt(selectMainWpsRows[mwindex].fweld_vol1).toString(16);
				crc7_str.push(fweld_vol1);
				if (fweld_vol1.length < 4) {
					var length = 4 - fweld_vol1.length;
					for (var i = 0; i < length; i++) {
						fweld_vol1 = "0" + fweld_vol1;
					}
				}
				var farc_vol_top = parseInt(selectMainWpsRows[mwindex].farc_vol_top * 10).toString(16);
				crc7_str.push(farc_vol_top);
				if (farc_vol_top.length < 4) {
					var length = 4 - farc_vol_top.length;
					for (var i = 0; i < length; i++) {
						farc_vol_top = "0" + farc_vol_top;
					}
				}
				var farc_tuny_ele = parseInt(selectMainWpsRows[mwindex].farc_tuny_ele).toString(16);
				crc7_str.push(farc_tuny_ele);
				if (farc_tuny_ele.length < 4) {
					var length = 4 - farc_tuny_ele.length;
					for (var i = 0; i < length; i++) {
						farc_tuny_ele = "0" + farc_tuny_ele;
					}
				}
				var farc_tuny_vol = parseInt(selectMainWpsRows[mwindex].farc_tuny_vol * 10).toString(16);
				crc7_str.push(farc_tuny_vol);
				if (farc_tuny_vol.length < 4) {
					var length = 4 - farc_tuny_vol.length;
					for (var i = 0; i < length; i++) {
						farc_tuny_vol = "0" + farc_tuny_vol;
					}
				}
				var fmaterial = parseInt(selectMainWpsRows[mwindex].fmaterial).toString(16);//材质
				crc7_str.push(fmaterial);
				if (fmaterial.length < 2) {
					var length = 2 - fmaterial.length;
					for (var i = 0; i < length; i++) {
						fmaterial = "0" + fmaterial;
					}
				}
				var fdiameter = parseInt(selectMainWpsRows[mwindex].fdiameter).toString(16);
				crc7_str.push(fdiameter);
				if (fdiameter.length < 2) {
					var length = 2 - fdiameter.length;
					for (var i = 0; i < length; i++) {
						fdiameter = "0" + fdiameter;
					}
				}
				var fgas = parseInt(selectMainWpsRows[mwindex].fgas).toString(16);
				crc7_str.push(fgas);
				if (fgas.length < 2) {
					var length = 2 - fgas.length;
					for (var i = 0; i < length; i++) {
						fgas = "0" + fgas;
					}
				}
				var fcontroller = parseInt(selectMainWpsRows[mwindex].fcontroller).toString(16);
				crc7_str.push(fcontroller);
				if (fcontroller.length < 2) {
					var length = 2 - fcontroller.length;
					for (var i = 0; i < length; i++) {
						fcontroller = "0" + fcontroller;
					}
				}
				var farc = parseInt(selectMainWpsRows[mwindex].farc).toString(16);
				crc7_str.push(farc);
				if (farc.length < 2) {
					var length = 2 - farc.length;
					for (var i = 0; i < length; i++) {
						farc = "0" + farc;
					}
				}
				var ftime = parseInt(selectMainWpsRows[mwindex].ftime * 10).toString(16);
				crc7_str.push(ftime);
				if (ftime.length < 4) {
					var length = 4 - ftime.length;
					for (var i = 0; i < length; i++) {
						ftime = "0" + ftime;
					}
				}
				var fselect = parseInt(selectMainWpsRows[mwindex].fselect).toString(16);
				crc7_str.push(fselect);
				if(fselect==parseInt(101).toString(16)){
					fselect="1";
				}else if(fselect==parseInt(102).toString(16)){
					fselect="0";
				}
				if (fselect.length < 2) {
					var length = 2 - fselect.length;
					for (var i = 0; i < length; i++) {
						fselect = "0" + fselect;
					}
				}
				var finitial = (parseInt(selectMainWpsRows[mwindex].finitial)).toString(16);
				crc7_str.push(finitial);
				if (finitial.length < 2) {
					var length = 2 - finitial.length;
					for (var i = 0; i < length; i++) {
						finitial = "0" + finitial;
					}
				}
				crc7_str.push("0");
				var fweld_vol = (parseInt(selectMainWpsRows[mwindex].fweld_vol)).toString(16);
				crc7_str.push(fweld_vol);
				if (fweld_vol.length < 4) {
					var length = 4 - fweld_vol.length;
					for (var i = 0; i < length; i++) {
						fweld_vol = "0" + fweld_vol;
					}
				}
				var fweld_ele = (parseInt(selectMainWpsRows[mwindex].fweld_ele)).toString(16);
				crc7_str.push(fweld_ele);
				if (fweld_ele.length < 4) {
					var length = 4 - fweld_ele.length;
					for (var i = 0; i < length; i++) {
						fweld_ele = "0" + fweld_ele;
					}
				}
				var fini_ele = (parseInt(selectMainWpsRows[mwindex].fini_ele)).toString(16);
				crc7_str.push(fini_ele);
				if (fini_ele.length < 4) {
					var length = 4 - fini_ele.length;
					for (var i = 0; i < length; i++) {
						fini_ele = "0" + fini_ele;
					}
				}
				var fini_vol = (parseInt(selectMainWpsRows[mwindex].fini_vol)).toString(16);
				crc7_str.push(fini_vol);
				if (fini_vol.length < 4) {
					var length = 4 - fini_vol.length;
					for (var i = 0; i < length; i++) {
						fini_vol = "0" + fini_vol;
					}
				}
				var farc_ele = (parseInt(selectMainWpsRows[mwindex].farc_ele)).toString(16);
				crc7_str.push(farc_ele);
				if (farc_ele.length < 4) {
					var length = 4 - farc_ele.length;
					for (var i = 0; i < length; i++) {
						farc_ele = "0" + farc_ele;
					}
				}
				var farc_vol = (parseInt(selectMainWpsRows[mwindex].farc_vol)).toString(16);
				crc7_str.push(farc_vol);
				if (farc_vol.length < 4) {
					var length = 4 - farc_vol.length;
					for (var i = 0; i < length; i++) {
						farc_vol = "0" + farc_vol;
					}
				}
				var fadvance = parseInt(selectMainWpsRows[mwindex].fadvance).toString(16);//延时时间
				crc7_str.push(fadvance);
				if (fadvance.length < 2) {
					var length = 2 - fadvance.length;
					for (var i = 0; i < length; i++) {
						fadvance = "0" + fadvance;
					}
				}
				var fhysteresis = parseInt(selectMainWpsRows[mwindex].fhysteresis).toString(16);
				crc7_str.push(fhysteresis);
				if (fhysteresis.length < 2) {
					var length = 2 - fhysteresis.length;
					for (var i = 0; i < length; i++) {
						fhysteresis = "0" + fhysteresis;
					}
				}
				var fpreset_ele_warn_top = parseInt(selectMainWpsRows[mwindex].fpreset_ele_warn_top).toString(16);
				crc7_str.push(fpreset_ele_warn_top);
				if (fpreset_ele_warn_top.length < 4) {
					var length = 4 - fpreset_ele_warn_top.length;
					for (var i = 0; i < length; i++) {
						fpreset_ele_warn_top = "0" + fpreset_ele_warn_top;
					}
				}
				var fpreset_vol_warn_top = parseInt(selectMainWpsRows[mwindex].fpreset_vol_warn_top * 10).toString(16);
				crc7_str.push(fpreset_vol_warn_top);
				if (fpreset_vol_warn_top.length < 4) {
					var length = 4 - fpreset_vol_warn_top.length;
					for (var i = 0; i < length; i++) {
						fpreset_vol_warn_top = "0" + fpreset_vol_warn_top;
					}
				}
				var fpreset_ele_warn_bottom = parseInt(selectMainWpsRows[mwindex].fpreset_ele_warn_bottom).toString(16);
				crc7_str.push(fpreset_ele_warn_bottom);
				if (fpreset_ele_warn_bottom.length < 4) {
					var length = 4 - fpreset_ele_warn_bottom.length;
					for (var i = 0; i < length; i++) {
						fpreset_ele_warn_bottom = "0" + fpreset_ele_warn_bottom;
					}
				}
				var fpreset_vol_warn_bottom = parseInt(selectMainWpsRows[mwindex].fpreset_vol_warn_bottom * 10).toString(16);
				crc7_str.push(fpreset_vol_warn_bottom);
				if (fpreset_vol_warn_bottom.length < 4) {
					var length = 4 - fpreset_vol_warn_bottom.length;
					for (var i = 0; i < length; i++) {
						fpreset_vol_warn_bottom = "0" + fpreset_vol_warn_bottom;
					}
				}
				var fini_ele_warn_top = parseInt(selectMainWpsRows[mwindex].fini_ele_warn_top).toString(16);
				crc7_str.push(fini_ele_warn_top);
				if (fini_ele_warn_top.length < 4) {
					var length = 4 - fini_ele_warn_top.length;
					for (var i = 0; i < length; i++) {
						fini_ele_warn_top = "0" + fini_ele_warn_top;
					}
				}
				var fini_vol_warn_top = parseInt(selectMainWpsRows[mwindex].fini_vol_warn_top * 10).toString(16);
				crc7_str.push(fini_vol_warn_top);
				if (fini_vol_warn_top.length < 4) {
					var length = 4 - fini_vol_warn_top.length;
					for (var i = 0; i < length; i++) {
						fini_vol_warn_top = "0" + fini_vol_warn_top;
					}
				}
				var fini_ele_warn_bottom = parseInt(selectMainWpsRows[mwindex].fini_ele_warn_bottom).toString(16);
				crc7_str.push(fini_ele_warn_bottom);
				if (fini_ele_warn_bottom.length < 4) {
					var length = 4 - fini_ele_warn_bottom.length;
					for (var i = 0; i < length; i++) {
						fini_ele_warn_bottom = "0" + fini_ele_warn_bottom;
					}
				}
				var fini_vol_warn_bottom = parseInt(selectMainWpsRows[mwindex].fini_vol_warn_bottom * 10).toString(16);
				crc7_str.push(fini_vol_warn_bottom);
				if (fini_vol_warn_bottom.length < 4) {
					var length = 4 - fini_vol_warn_bottom.length;
					for (var i = 0; i < length; i++) {
						fini_vol_warn_bottom = "0" + fini_vol_warn_bottom;
					}
				}
				var farc_ele_warn_top = parseInt(selectMainWpsRows[mwindex].farc_ele_warn_top).toString(16);
				crc7_str.push(farc_ele_warn_top);
				if (farc_ele_warn_top.length < 4) {
					var length = 4 - farc_ele_warn_top.length;
					for (var i = 0; i < length; i++) {
						farc_ele_warn_top = "0" + farc_ele_warn_top;
					}
				}
				var farc_vol_warn_top = parseInt(selectMainWpsRows[mwindex].farc_vol_warn_top * 10).toString(16);
				crc7_str.push(farc_vol_warn_top);
				if (farc_vol_warn_top.length < 4) {
					var length = 4 - farc_vol_warn_top.length;
					for (var i = 0; i < length; i++) {
						farc_vol_warn_top = "0" + farc_vol_warn_top;
					}
				}
				var farc_ele_warn_bottom = parseInt(selectMainWpsRows[mwindex].farc_ele_warn_bottom).toString(16);
				crc7_str.push(farc_ele_warn_bottom);
				if (farc_ele_warn_bottom.length < 4) {
					var length = 4 - farc_ele_warn_bottom.length;
					for (var i = 0; i < length; i++) {
						farc_ele_warn_bottom = "0" + farc_ele_warn_bottom;
					}
				}
				var farc_vol_warn_bottom = parseInt(selectMainWpsRows[mwindex].farc_vol_warn_bottom * 10).toString(16);
				crc7_str.push(farc_vol_warn_bottom);
				if (farc_vol_warn_bottom.length < 4) {
					var length = 4 - farc_vol_warn_bottom.length;
					for (var i = 0; i < length; i++) {
						farc_vol_warn_bottom = "0" + farc_vol_warn_bottom;
					}
				}
				var farc_delay_time = parseInt(selectMainWpsRows[mwindex].farc_delay_time * 10).toString(16);
				crc7_str.push(farc_delay_time);
				if (farc_delay_time.length < 2) {
					var length = 2 - farc_delay_time.length;
					for (var i = 0; i < length; i++) {
						farc_delay_time = "0" + farc_delay_time;
					}
				}
				var fwarn_delay_time = parseInt(selectMainWpsRows[mwindex].fwarn_delay_time * 10).toString(16);
				crc7_str.push(fwarn_delay_time);
				if (fwarn_delay_time.length < 2) {
					var length = 2 - fwarn_delay_time.length;
					for (var i = 0; i < length; i++) {
						fwarn_delay_time = "0" + fwarn_delay_time;
					}
				}
				var fwarn_stop_time = parseInt(selectMainWpsRows[mwindex].fwarn_stop_time * 10).toString(16);
				crc7_str.push(fwarn_stop_time);
				if (fwarn_stop_time.length < 2) {
					var length = 2 - fwarn_stop_time.length;
					for (var i = 0; i < length; i++) {
						fwarn_stop_time = "0" + fwarn_stop_time;
					}
				}
				var fcharacter = "0" + selectMainWpsRows[mwindex].sxfcharacter;//标志
				crc7_str.push(selectMainWpsRows[mwindex].sxfcharacter);
				
				var fflow_top = parseInt(selectMainWpsRows[mwindex].fflow_top * 10).toString(16);
				crc7_str.push(fflow_top);
				if (fflow_top.length < 2) {
					var length = 2 - fflow_top.length;
					for (var i = 0; i < length; i++) {
						fflow_top = "0" + fflow_top;
					}
				}
				var fflow_bottom = parseInt(selectMainWpsRows[mwindex].fflow_bottom * 10).toString(16);
				crc7_str.push(fflow_bottom);
				if (fflow_bottom.length < 2) {
					var length = 2 - fflow_bottom.length;
					for (var i = 0; i < length; i++) {
						fflow_bottom = "0" + fflow_bottom;
					}
				}
				var fdelay_time = parseInt(selectMainWpsRows[mwindex].fdelay_time * 10).toString(16);
				crc7_str.push(fdelay_time);
				if (fdelay_time.length < 2) {
					var length = 2 - fdelay_time.length;
					for (var i = 0; i < length; i++) {
						fdelay_time = "0" + fdelay_time;
					}
				}
				var fover_time = parseInt(selectMainWpsRows[mwindex].fover_time * 10).toString(16);
				crc7_str.push(fover_time);
				if (fover_time.length < 2) {
					var length = 2 - fover_time.length;
					for (var i = 0; i < length; i++) {
						fover_time = "0" + fover_time;
					}
				}
				var ffixed_cycle = parseInt(selectMainWpsRows[mwindex].ffixed_cycle * 10).toString(16);
				crc7_str.push(ffixed_cycle);
				if (ffixed_cycle.length < 2) {
					var length = 2 - ffixed_cycle.length;
					for (var i = 0; i < length; i++) {
						ffixed_cycle = "0" + ffixed_cycle;
					}
				}
				crc7_str.push("0");
				var xiafasend1 = chanel + "0000" + fpreset_ele_top + fpreset_vol_top +  fpreset_ele_bottom + fpreset_vol_bottom + fini_vol1 + farc_vol1 + fweld_tuny_ele + fweld_tuny_vol + fweld_vol1 + farc_vol_top + farc_tuny_ele + farc_tuny_vol + fmaterial + fdiameter
					+ fgas + fcontroller + farc + ftime + fselect + finitial + "000000" + fweld_vol + fweld_ele + fini_ele + fini_vol + farc_ele + farc_vol + fadvance + fhysteresis + fpreset_ele_warn_top + fpreset_vol_warn_top + fpreset_ele_warn_bottom
					+ fpreset_vol_warn_bottom + fini_ele_warn_top + fini_vol_warn_top + fini_ele_warn_bottom + fini_vol_warn_bottom + farc_ele_warn_top + farc_vol_warn_top + farc_ele_warn_bottom + farc_vol_warn_bottom + farc_delay_time + fwarn_delay_time + fwarn_stop_time
					+ fcharacter + fflow_top + fflow_bottom + fdelay_time + fover_time + ffixed_cycle + "00";
				
				var xxx = xiafasend1.toUpperCase();
				
				//CRC7校验
				$.ajax({
				      type : "post",  
				      async : false,
				      url : "wps/CRC7Check?crc7_str="+crc7_str,  
				      data : {},  
				      dataType : "json", //返回数据形式为json  
				      success : function(result) {
				          if (result) {
								var CRC7_check = parseInt(result.CRC7_code) .toString(16);
								if (CRC7_check.length < 2) {
									var length = 2 - CRC7_check.length;
									for (var i = 0; i < length; i++) {
										CRC7_check = "0" + CRC7_check;
									}
								}
								var xiafasend2 = "FE5AA5" + data_length + equipmentNo + "000000000000000000000000" + CRC7_check + "021102" + xiafasend1;
								sochet_send_data.push(xiafasend2);
								noReceiveGiveChanel.push(parseInt(selectMainWpsRows[mwindex].fwpsnum));
				          }  
				      },
				      error : function(errorMsg) {  
				          alert("数据请求失败，请联系系统管理员!");  
				      }  
				 });
			}
			var jsonstr = {
				"gatherNo" : selectMachine[smindex].gatherId,
				"machineNo" : selectMachine[smindex].equipmentNo,
				"successNum" : 0,
				"failNum" : 0,
				"noNum" : noReceiveGiveChanel.join(",")
			};
			resultData.push(jsonstr);
			if (giveArray.length == 0) {
				giveArray.push(selectMachine[smindex].equipmentNo);
				giveArray.push(parseInt(selectMachine[smindex].gatherId));
				giveArray.push(0);//成功
				giveArray.push(0);//失败
				giveArray.push(0);//未响应
			} else {
				if ($.inArray(selectMachine[smindex],giveArray) == (-1)) {
					giveArray.push(selectMachine[smindex].equipmentNo);
					giveArray.push(parseInt(selectMachine[smindex].gatherId));
					giveArray.push(0);
					giveArray.push(0);
					giveArray.push(0);
				}
			}
		}
		var oneMinuteTimer = window.setTimeout(function() {
//			websocket.close();
			client.unsubscribe("weldmes/upparams", {
				onSuccess : function(e) {
					console.log("取消订阅成功");
				},
				onFailure : function(e) {
					console.log(e);
				}
			});
			alert("下发超时");
			$('#sxSelectdlg').window('close');
			$('#sxMachinedlg').window('close');
			$('#sxSelectWpsTab').datagrid('clearSelections');
			$('#sxMachineTable').datagrid('clearSelections');
			selectMainWpsRows.length = 0;
			selectMachine.length = 0;
			sochet_send_data.length = 0;
			giveArray.length = 0;
			noReceiveGiveChanel.length = 0;
			resultData.length = 0;
			realLength = 0;
		}, selectMachine.length*selectMainWpsRows.length*30000);
		showResult();
		$("#giveResultTable").datagrid('loadData', resultData);//下发结果展示
		var timer = window.setInterval(function() {
			if (sochet_send_data.length != 0) {
				var popdata = sochet_send_data.pop();
//				websocket.send(popdata);//下发
				var message = new Paho.MQTT.Message(popdata);
				message.destinationName = "weldmes/downparams";
				client.send(message);
			} else {
				window.clearInterval(timer);
			}
		}, 300)
		client.subscribe("weldmes/upparams", {
			qos: 0,
			onSuccess:function(e){  
	            console.log("订阅成功");  
	        },
	        onFailure: function(e){  
	            console.log(e);  
	        }
		})
		client.onMessageArrived = function(e){
			var fan = e.payloadString;
			if (fan.substring(0, 6) == "FE5AA5" && fan.substring(6,10) == "001A") {
				var rows = $('#giveResultTable').datagrid("getRows");
				if (parseInt(fan.substring(44, 46), 16) != 2) {//控制，2表示OK
					realLength++;
					var frchanel = parseInt(fan.substring(46, 48), 16);
					var indexNum = $.inArray(parseInt(fan.substring(10, 14),16),giveArray);
					if (indexNum != -1) {
						giveArray[indexNum + 2] = frchanel;
						if (rows[(indexNum - 1) / 5].noNum != "0") {
							var onNumArr = rows[(indexNum - 1) / 5].noNum.split(",");
							onNumArr.splice($.inArray(frchanel,onNumArr), 1);
							var nowNoArr = onNumArr;
							if (nowNoArr.length != 0) {
								rows[(indexNum - 1) / 5].noNum = nowNoArr.join(",");
							} else {
								rows[(indexNum - 1) / 5].noNum = 0;
							}
						} else {
							rows[(indexNum - 1) / 5].noNum = 0;
						}
						if (parseInt(rows[(indexNum - 1) / 5].failNum) != 0) {
							rows[(indexNum - 1) / 5].failNum = rows[(indexNum - 1) / 5].failNum + "," + giveArray[indexNum + 2];
						} else {
							rows[(indexNum - 1) / 5].failNum = giveArray[indexNum + 2];
						}
						$('#giveResultTable').datagrid('refreshRow', (indexNum - 1) / 5);
					}
					if (realLength == checkLength) {//焊机数据*工艺数据=收到的数目
						client.unsubscribe("weldmes/upparams", {
							onSuccess : function(e) {
								console.log("取消订阅成功");
							},
							onFailure : function(e) {
								console.log(e);
							}
						});
//						if (websocket.readyState != 1) {
							window.clearTimeout(oneMinuteTimer);
							$.ajax({  
							      type : "post",  
							      async : false,
							      url : "wps/saveGiveWpsHistory", 
							      data : {mainwps:JSON.stringify(selectMainWpsRows),machine:JSON.stringify(selectMachine),wpslib:wpslibId,flag:1},  
							      dataType : "json", //返回数据形式为json  
							      success : function(result) {
							          if (result.success) {
											alert("下发完成");
							          } else{
							        	  	alert("下发完成，存储下发记录失败")
							          }
									  $('#sxSelectdlg').window('close');
									  $('#sxMachinedlg').window('close');
									  $('#sxSelectWpsTab').datagrid('clearSelections');
									  $('#sxMachineTable').datagrid('clearSelections');
									  selectMainWpsRows.length = 0;
									  selectMachine.length = 0;
									  sochet_send_data.length = 0;
									  noReceiveGiveChanel.length = 0;
									  giveArray.length = 0;
									  resultData.length = 0;
									  realLength = 0;
							      },  
							      error : function(errorMsg) {  
							          alert("数据请求失败，请联系系统管理员!");  
							      }  
							}); 
//						}
					}
				} else {
					realLength++;
					var frchanel = parseInt(fan.substring(46, 48), 16);
					var indexNum = $.inArray(parseInt(fan.substring(10, 14), 16),giveArray);
					if (indexNum != -1) {
						giveArray[indexNum + 1] = frchanel;
						if (rows[(indexNum - 1) / 5].noNum != "0") {
							var onNumArr = rows[(indexNum - 1) / 5].noNum.split(",");
							onNumArr.splice($.inArray(frchanel,onNumArr), 1);
							var nowNoArr = onNumArr;
							if (nowNoArr.length != 0) {
								rows[(indexNum - 1) / 5].noNum = nowNoArr.join(",");
							} else {
								rows[(indexNum - 1) / 5].noNum = 0;
							}
						} else {
							rows[(indexNum - 1) / 5].noNum = 0;
						}
						if (parseInt(rows[(indexNum - 1) / 5].successNum) != 0) {
							rows[(indexNum - 1) / 5].successNum = rows[(indexNum - 1) / 5].successNum + "," + giveArray[indexNum + 1];
						} else {
							rows[(indexNum - 1) / 5].successNum = giveArray[indexNum + 1];
						}
						$('#giveResultTable').datagrid('refreshRow', (indexNum - 1) / 5);
					}
					if (realLength == checkLength) {
						client.unsubscribe("weldmes/upparams", {
							onSuccess : function(e) {
								console.log("取消订阅成功");
							},
							onFailure : function(e) {
								console.log(e);
							}
						});
//						if (websocket.readyState != 1) {
							window.clearTimeout(oneMinuteTimer);
							$.ajax({  
							      type : "post",  
							      async : false,
							      url : "wps/saveGiveWpsHistory", 
							      data : {mainwps:JSON.stringify(selectMainWpsRows),machine:JSON.stringify(selectMachine),wpslib:wpslibId,flag:1},  
							      dataType : "json", //返回数据形式为json  
							      success : function(result) {
							          if (result.success) {
											alert("下发完成");
							          } else{
							        	  	alert("下发完成，存储下发记录失败")
							          }
									  $('#sxSelectdlg').window('close');
									  $('#sxMachinedlg').window('close');
									  $('#sxSelectWpsTab').datagrid('clearSelections');
									  $('#sxMachineTable').datagrid('clearSelections');
									  selectMainWpsRows.length = 0;
									  selectMachine.length = 0;
									  sochet_send_data.length = 0;
									  noReceiveGiveChanel.length = 0;
									  giveArray.length = 0;
									  resultData.length = 0;
									  realLength = 0;
							      },  
							      error : function(errorMsg) {  
							          alert("数据请求失败，请联系系统管理员!");  
							      }  
							});
//						}
					}
				}
			}
		}
//	}
}


//松下索取规范
function getSxMainWps() {
	var selectMachine = $('#sxMachineTable').datagrid('getSelected');
	if (selectMachine) {
		if (!selectMachine.gatherId) {
			alert("该焊机未绑定采集模块");
			return;
		}
	} else {
		alert("请选择焊机");
		return;
	}
	var flag = 0;
//	var websocket = null;
//	if (typeof (WebSocket) == "undefined") {
//		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
//		WEB_SOCKET_DEBUG = true;
//	}
//	websocket = new WebSocket(websocketUrl);
//	websocket.onopen = function() {
		var crc7_str = [];
		crc7_str.push("FE");
		crc7_str.push("5A");
		crc7_str.push("A5");
		var data_length = parseInt("26").toString(16);
		crc7_str.push(data_length);
		if (data_length.length < 4) {
			var length = 4 - data_length.length;
			for (var i = 0; i < length; i++) {
				data_length = "0" + data_length;
			}
		}
		var mach = parseInt(selectMachine.gatherId).toString(16);
		crc7_str.push(mach);
		if (mach.length < 4) {
			var length = 4 - mach.length;
			for (var i = 0; i < length; i++) {
				mach = "0" + mach;
			}
		}
		crc7_str.push("0");
		crc7_str.push("211");
		crc7_str.push("2");
		var chanel = $('#sxfwpsnum').combobox('getValue').toString(16);
		crc7_str.push(chanel);
		if (chanel.length < 2) {
			var length = 2 - chanel.length;
			for (var i = 0; i < length; i++) {
				chanel = "0" + chanel;
			}
		}
		crc7_str.push("0");
		//CRC7校验
		$.ajax({
		      type : "post",  
		      async : false,
		      url : "wps/CRC7Check?crc7_str="+crc7_str,  
		      data : {},  
		      dataType : "json", //返回数据形式为json  
		      success : function(result) {
		          if (result) {
						var CRC7_check = parseInt(result.CRC7_code) .toString(16);
						if (CRC7_check.length < 2) {
							var length = 2 - CRC7_check.length;
							for (var i = 0; i < length; i++) {
								CRC7_check = "0" + CRC7_check;
							}
						}
						var sendMesssager = "FE5AA5"+ data_length + mach + "000000000000000000000000" + CRC7_check + "021101" + chanel + "0000";
						var message = new Paho.MQTT.Message(sendMesssager);
						message.destinationName = "weldmes/downparams";
						client.send(message);
		          }  
		      },
		      error : function(errorMsg) {  
		          alert("数据请求失败，请联系系统管理员!");  
		      }  
		 });
		
		if (flag == 0) {
			var jctimer = window.setTimeout(function() {
				if (flag == 0) {
//					websocket.close();
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					})
					alert("焊机长时间未响应，索取失败!!!");
				}
			}, 30000)
		}
		client.subscribe("weldmes/upparams", {
			qos: 0,
			onSuccess:function(e){  
	            console.log("订阅成功");  
	        },
	        onFailure: function(e){  
	            console.log(e);  
	        }
		})
		client.onMessageArrived = function(e){
			var da = e.payloadString;
			if (da.substring(0, 6) == "FE5AA5") {
				if (da.substring(6,10) == "001A") {
					flag++;
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					})
					alert("此通道没有规范!!!");
					flag = 0;
				} else {
					$('#sxfwpsnum').combobox('select', parseInt(da.substring(46, 48), 16));
					$("#sxfpreset_ele_top").numberbox('setValue', (parseInt(da.substring(52, 56), 16)).toFixed(1));
					$("#sxfpreset_vol_top").numberbox('setValue', (parseInt(da.substring(56, 60), 16) / 10).toFixed(1));
					$("#sxfpreset_ele_bottom").numberbox('setValue', (parseInt(da.substring(60, 64), 16)).toFixed(1));
					$("#sxfpreset_vol_bottom").numberbox('setValue', (parseInt(da.substring(64, 68), 16) / 10).toFixed(1));
					$("#sxfini_vol1").numberbox('setValue', (parseInt(da.substring(68, 72), 16)).toFixed(1));
					$("#sxfarc_vol1").numberbox('setValue', (parseInt(da.substring(72, 76), 16) / 10).toFixed(1));
					$("#sxfweld_tuny_ele").numberbox('setValue', (parseInt(da.substring(76, 80), 16)).toFixed(1));
					$("#sxfweld_tuny_vol").numberbox('setValue', (parseInt(da.substring(80, 84), 16) / 10).toFixed(1));
					$("#sxfweld_vol1").numberbox('setValue', (parseInt(da.substring(84, 88), 16)).toFixed(1));
					$("#sxfarc_vol_top").numberbox('setValue', (parseInt(da.substring(88, 92), 16) / 10).toFixed(1));
					$("#sxfarc_tuny_ele").numberbox('setValue', (parseInt(da.substring(92, 96), 16)).toFixed(1));
					$("#sxfarc_tuny_vol").numberbox('setValue', (parseInt(da.substring(96, 100), 16) / 10).toFixed(1));
					
					$("#sxfmaterial").combobox('setValue', parseInt(da.substring(100, 102), 16));
					$("#sxfdiameter").combobox('setValue', parseInt(da.substring(102, 104), 16));
					$("#sxfgas").combobox('setValue', parseInt(da.substring(104, 106), 16));
					$("#sxfcontroller").combobox('setValue', parseInt(da.substring(106, 108), 16));
					$("#sxfarc").combobox('setValue', parseInt(da.substring(108, 110), 16));
					$("#sxftime").numberbox('setValue', (parseInt(da.substring(110, 114), 16) / 10).toFixed(1));
					if (parseInt(da.substring(114, 116)) == "0") {
						$('#sxfselect').combobox('select', 102);
					} else {
						$('#sxfselect').combobox('select', 101);
					}
					
					$("#sxfinitial").combobox('setValue', parseInt(da.substring(116, 118), 16));
					
					$("#sxfweld_vol").numberbox('setValue', parseInt(da.substring(124, 128), 16));
					$("#sxfweld_ele").numberbox('setValue', parseInt(da.substring(128, 132), 16));
					$("#sxfini_ele").numberbox('setValue', parseInt(da.substring(132, 136), 16));
					$("#sxfini_vol").numberbox('setValue', parseInt(da.substring(136, 140), 16));
					$("#sxfarc_ele").numberbox('setValue', parseInt(da.substring(140, 144), 16));
					$("#sxfarc_vol").numberbox('setValue', parseInt(da.substring(144, 148), 16));
					
					$("#sxfadvance").numberbox('setValue', parseInt(da.substring(148, 150), 16));
					$("#sxfhysteresis").numberbox('setValue', parseInt(da.substring(150, 152), 16));
					
					$("#sxfpreset_ele_warn_top").numberbox('setValue', (parseInt(da.substring(152, 156), 16)).toFixed(1));
					$("#sxfpreset_vol_warn_top").numberbox('setValue', (parseInt(da.substring(156, 160), 16) / 10).toFixed(1));
					$("#sxfpreset_ele_warn_bottom").numberbox('setValue', (parseInt(da.substring(160, 164), 16)).toFixed(1));
					$("#sxfpreset_vol_warn_bottom").numberbox('setValue', (parseInt(da.substring(164, 168), 16) / 10).toFixed(1));
					
					$("#sxfini_ele_warn_top").numberbox('setValue', (parseInt(da.substring(168, 172), 16)).toFixed(1));
					$("#sxfini_vol_warn_top").numberbox('setValue', (parseInt(da.substring(172, 176), 16) / 10).toFixed(1));
					$("#sxfini_ele_warn_bottom").numberbox('setValue', (parseInt(da.substring(176, 180), 16)).toFixed(1));
					$("#sxfini_vol_warn_bottom").numberbox('setValue', (parseInt(da.substring(180, 184), 16) / 10).toFixed(1));
					$("#sxfarc_ele_warn_top").numberbox('setValue', (parseInt(da.substring(184, 188), 16)).toFixed(1));
					$("#sxfarc_vol_warn_top").numberbox('setValue', (parseInt(da.substring(188, 192), 16) / 10).toFixed(1));
					$("#sxfarc_ele_warn_bottom").numberbox('setValue', (parseInt(da.substring(192, 196), 16)).toFixed(1));
					$("#sxfarc_vol_warn_bottom").numberbox('setValue', (parseInt(da.substring(196, 200), 16) / 10).toFixed(1));

					$("#sxfarc_delay_time").numberbox('setValue', (parseInt(da.substring(200, 202), 16) / 10).toFixed(1));
					$("#sxfwarn_delay_time").numberbox('setValue', (parseInt(da.substring(202, 204), 16) / 10).toFixed(1));
					$("#sxfwarn_stop_time").numberbox('setValue', (parseInt(da.substring(204, 206), 16) / 10).toFixed(1));
					
					if(parseInt(da.substring(206, 208))==0){
					    $("input[name='sxfcharacter']").eq(0).prop("checked",true);
					}else{
					    $("input[name='sxfcharacter']").eq(1).prop("checked",true);
					}
				
					$("#sxfflow_top").numberbox('setValue', (parseInt(da.substring(208, 210), 16) / 10).toFixed(1));
					$("#sxfflow_bottom").numberbox('setValue', (parseInt(da.substring(210, 212), 16) / 10).toFixed(1));
					$("#sxfdelay_time").numberbox('setValue', (parseInt(da.substring(212, 214), 16) / 10).toFixed(1));
					$("#sxfover_time").numberbox('setValue', (parseInt(da.substring(214, 216), 16) / 10).toFixed(1));
					$("#sxffixed_cycle").numberbox('setValue', (parseInt(da.substring(216, 218), 16) / 10).toFixed(1));

					flag++;
					client.unsubscribe("weldmes/upparams", {
						onSuccess : function(e) {
							console.log("取消订阅成功");
						},
						onFailure : function(e) {
							console.log(e);
						}
					})
					window.clearTimeout(jctimer);
					alert("索取成功");
					flag = 0;
					$('#sxMachinedlg').window('close');
					$('#sxMachineTable').datagrid('clearSelections');
					$('#sxmachinefm').form('clear');
				}
			}
		}
//	}
}

function itemcombobox(){
	$.ajax({  
	    type : "post",  
	    async : false,
	    url : "weldtask/getOperateArea",  
	    data : {},  
	    dataType : "json", //返回数据形式为json  
	    success : function(result) {  
	        if (result) {
	        	if(result.type==23){
	        		var zoptionStr = "";
	        		var boptionStr = "";
	                for (var i = 0; i < result.ary.length; i++) {  
	                    zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"  
	                            + result.ary[i].name + "</option>";
	                }
	                for (var j = 0; j < result.banzu.length; j++) {  
	                    boptionStr += "<option value=\"" + result.banzu[j].id + "\" >"  
	                            + result.banzu[j].name + "</option>";
	                }
	                $("#zitem").html(zoptionStr);
	                $("#bitem").html(boptionStr);
		        	$("#zitem").combobox();
		        	$("#zitem").combobox('select',result.ary[0].id);
		        	$("#bitem").combobox();
		        	$("#bitem").combobox('select',result.banzu[0].id);
	                $("#szitem").html(zoptionStr);
	                $("#sbitem").html(boptionStr);
		        	$("#szitem").combobox();
		        	$("#szitem").combobox('select',result.ary[0].id);
		        	$("#sbitem").combobox();
		        	$("#sbitem").combobox('select',result.banzu[0].id);
//		        	$("#zitem").combobox({disabled: true});
//		        	$("#bitem").combobox({disabled: true});
	        	}else if(result.type==22){
	        		var zoptionStr = "";
	        		var boptionStr = '<option value="0">请选择</option>';
	                for (var i = 0; i < result.ary.length; i++) {  
	                    zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"  
	                            + result.ary[i].name + "</option>";
	                }
	                for (var j = 0; j < result.banzu.length; j++) {  
	                    boptionStr += "<option value=\"" + result.banzu[j].id + "\" >"  
	                            + result.banzu[j].name + "</option>";
	                }
	                $("#zitem").html(zoptionStr);
	                $("#bitem").html(boptionStr);
		        	$("#zitem").combobox();
		        	$("#zitem").combobox('select',result.ary[0].id);
		        	$("#bitem").combobox();
		        	$("#bitem").combobox('select',0);
	                $("#szitem").html(zoptionStr);
	                $("#sbitem").html(boptionStr);
		        	$("#szitem").combobox();
		        	$("#szitem").combobox('select',result.ary[0].id);
		        	$("#sbitem").combobox();
		        	$("#sbitem").combobox('select',0);
//		        	$("#zitem").combobox({disabled: true});
	        	}else{
	        		$("#bitem").combobox({disabled: true});
	        		$("#sbitem").combobox({disabled: true});
	        		var zoptionStr = '<option value="0">请选择</option>';
	                for (var i = 0; i < result.ary.length; i++) {  
	                    zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"  
	                            + result.ary[i].name + "</option>";
	                }
	                $("#zitem").html(zoptionStr);
		        	$("#zitem").combobox();
		        	$("#zitem").combobox('select',0);
	                $("#szitem").html(zoptionStr);
		        	$("#szitem").combobox();
		        	$("#szitem").combobox('select',0);
	        	}
	        	
	        }  
	    },  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    }  
		});
	
	$("#zitem").combobox({
		onSelect : function(newValue,oldValue){
			if(oldValue!=""){
				$.ajax({  
				    type : "post",  
				    async : false,
				    url : "weldtask/getTeam?searchStr="+" and i.fparent="+newValue.value,  
				    data : {},  
				    dataType : "json", //返回数据形式为json  
				    success : function(result) {  
				        if (result) {
				        		var boptionStr = '<option value="0">请选择</option>';
				                for (var i = 0; i < result.ary.length; i++) {  
				                    boptionStr += "<option value=\"" + result.ary[i].id + "\" >"  
				                            + result.ary[i].name + "</option>";
				                }
				                $("#bitem").html(boptionStr);
					        	$("#bitem").combobox();
					        	$("#bitem").combobox({disabled: false});
					        	$("#bitem").combobox('select',0);
				        }  
				    },  
				    error : function(errorMsg) {  
				        alert("数据请求失败，请联系系统管理员!");  
				    }  
					}); 
			}
		}
	})
	
		$("#szitem").combobox({
		onSelect : function(newValue,oldValue){
			if(oldValue!=""){
				$.ajax({  
				    type : "post",  
				    async : false,
				    url : "weldtask/getTeam?searchStr="+" and i.fparent="+newValue.value,  
				    data : {},  
				    dataType : "json", //返回数据形式为json  
				    success : function(result) {  
				        if (result) {
				        		var boptionStr = '<option value="0">请选择</option>';
				                for (var i = 0; i < result.ary.length; i++) {  
				                    boptionStr += "<option value=\"" + result.ary[i].id + "\" >"  
				                            + result.ary[i].name + "</option>";
				                }
				                $("#sbitem").html(boptionStr);
					        	$("#sbitem").combobox();
					        	$("#sbitem").combobox({disabled: false});
					        	$("#sbitem").combobox('select',0);
				        }  
				    },  
				    error : function(errorMsg) {  
				        alert("数据请求失败，请联系系统管理员!");  
				    }  
					}); 
			}
		}
	})
	
	var searchStr = "",parent = "";
	$("#bitem").combobox({
		onChange : function(newValue,oldValue){
			searchStr = "";
			parent = "";
			var itemid = $("#bitem").combobox("getValue");
//			var status = $("#status").combobox("getValue");
			if(itemid!=0){
				parent = itemid;
			}
//			if(status==1){
//				searchStr = " foperatetype=1";
//			}else if(status==0){
//				searchStr = " (foperatetype=0 or foperatetype=2)";
//			}else if(status==3){
//				searchStr = " foperatetype is null"
//			}
			$("#weldingmachineTable").datagrid('reload',{
//				"searchStr" : searchStr,
				"parent" : parent
			})
		}
	})
	
	$("#sbitem").combobox({
		onChange : function(newValue,oldValue){
			searchStr = "";
			parent = "";
			var itemid = $("#sbitem").combobox("getValue");
//			var status = $("#status").combobox("getValue");
			if(itemid!=0){
				parent = itemid;
			}
//			if(status==1){
//				searchStr = " foperatetype=1";
//			}else if(status==0){
//				searchStr = " (foperatetype=0 or foperatetype=2)";
//			}else if(status==3){
//				searchStr = " foperatetype is null"
//			}
			$("#sxMachineTable").datagrid('reload',{
//				"searchStr" : searchStr,
				"parent" : parent
			})
		}
	})
	
	$("#sxMachineSearch").click(function(){
	　　　　if($("#machineId").val()){
				searchStr = "";
				parent = "";
				if($("#sbitem").val()){
					var itemid = $("#sbitem").combobox("getValue");
					if(itemid!=0){
						parent = itemid;
					}
				}
				searchStr = " and w.fequipment_no='"+$("#machineId").val()+"'";
				$("#sxMachineTable").datagrid('reload',{
					"str" : searchStr,
					"parent" : parent
				})
	　　　　}else{
			$("#sxMachineTable").datagrid('reload',{
				"str" : "",
				"parent" : ""
			})
	　　　　}
	});
	
	$("#otcMachineSearch").click(function(){
	　　　　if($("#otcMachineId").val()){
				searchStr = "";
				parent = "";
				if($("#bitem").val()){
					var itemid = $("#bitem").combobox("getValue");
					if(itemid!=0){
						parent = itemid;
					}
				}
				searchStr = " and w.fequipment_no='"+$("#otcMachineId").val()+"'";
				$("#weldingmachineTable").datagrid('reload',{
					"str" : searchStr,
					"parent" : parent
				})
	　　　　}else{
			$("#weldingmachineTable").datagrid('reload',{
				"str" : "",
				"parent" : ""
			})
	　　　　}
	});
}

var client,clientId;
function mqttTest(){
//	var clientId = Math.random().toString().substr(3,8) + Date.now().toString(36);
	var clientId = mqttClintId+"_upLoad";
	client = new Paho.MQTT.Client(websocketURL.split(":")[0], parseInt(websocketURL.split(":")[1]), clientId);
	var options = {
        timeout: 5,  
        keepAliveInterval: 60,  
        cleanSession: false,  
        useSSL: false,  
        onSuccess: onConnect,  
        onFailure: function(e){  
            console.log(e);  
        },
        reconnect : true
	}
	
	//set callback handlers
	client.onConnectionLost = onConnectionLost;
//	client.onMessageArrived = onMessageArrived;

	//connect the client
	client.connect(options);
}

//called when the client connects
function onConnect() {
	// Once a connection has been made, make a subscription and send a message.
	console.log("onConnect");
}

//called when the client loses its connection
function onConnectionLost(responseObject) {
	if (responseObject.errorCode !== 0) {
		console.log("onConnectionLost:"+responseObject.errorMessage);
	}
}