/**
 * 
 */
//var websocketURL;
var client;
$(function(){
//	$.ajax({
//	      type : "post",  
//	      async : false,
//	      url : "td/AllTdbf",  
//	      data : {},  
//	      dataType : "json", //返回数据形式为json  
//	      success : function(result) {
//	          if (result) {
//	        	  websocketURL = eval(result.web_socket);
//	          }  
//	      },
//	      error : function(errorMsg) {  
//	          alert("数据请求失败，请联系系统管理员!");  
//	      }  
//	});
//	mqttTest();
})

function controlfun(){
	var symbol = 0;
// 	if(typeof(WebSocket) == "undefined") {
//    	WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
//    	WEB_SOCKET_DEBUG = true;
//	}
//	var websocket = new WebSocket(WebSocket_Url);
//	websocket.onopen = function() {
//		window.setTimeout(function() {
//			if(pwdflag==0){
//				alert("下发失败");
//				websocket.close();
//				$('#smdlg').window("close")
//				$('#condlg').window("close");
//			}
//		}, 10000)
		var con = $("input[name='free']:checked").val();
		if(con.length<2){
			var length = 2 - con.length;
	        for(var i=0;i<length;i++){
	        	con = "0" + con;
	        }
	    };
	    var machine;
	    var selectMachine = $('#weldingmachineTable').datagrid('getSelected');
	    if(selectMachine.gatherId==null||selectMachine.gatherId==""){
	    	alert("该焊机未对应采集编号!!!");
			websocket.close();
			return;
	    }else{
			machine = parseInt(selectMachine.gatherId).toString(16);
			if(machine.length<4){
				var length = 4 - machine.length;
		        for(var i=0;i<length;i++){
		        	machine = "0" + machine;
		        };
			}
	    };
		var xiafasend1 = machine+con;
		var xxx = xiafasend1.toUpperCase();
		var data_length = ((parseInt(xxx.length)+12)/2).toString(16);
		if(data_length.length<2){
			var length = 2 - data_length.length;
	        for(var i=0;i<length;i++){
	        	data_length = "0" + data_length;
	        }
	    };
	    xxx="7E"+data_length+"01010154"+xiafasend1;
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
		var message = new Paho.MQTT.Message("7E"+xiafasend2+"7D");
		message.destinationName = "weldmes/downparams";
		client.send(message);
//		websocket.send("7E"+xiafasend2+"7D");
//		websocket.onmessage = function(msg) {
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
			var fan = e.payloadString;
			if(fan.substring(0,2)=="7E"&&fan.substring(10,12)=="54"){
				client.unsubscribe("weldmes/upparams", {
					onSuccess : function(e) {
						console.log("取消订阅成功");
					},
					onFailure : function(e) {
						console.log(e);
					}
				});
				window.clearTimeout(oneMinuteTimer);
				symbol = 1;
				if(parseInt(fan.substring(16,18),16)==1){
					alert("下发失败");
					$('#smdlg').window("close")
					$('#condlg').window("close");
				}else{
					alert("下发成功");
					$('#smdlg').window("close")
					$('#condlg').window("close");
				}
		};
	}
}

function passfun(){
	if($('#passwd').numberbox('getValue')){
		if(parseInt($('#passwd').numberbox('getValue'))<1||parseInt($('#passwd').numberbox('getValue'))>999){
			alert("密码范围是1~999");
		}else{
//			var pwdflag=0;
//		 	if(typeof(WebSocket) == "undefined") {
//		    	WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
//		    	WEB_SOCKET_DEBUG = true;
//			}
//			var websocket = new WebSocket(WebSocket_Url);
//			websocket.onopen = function() {
//				window.setTimeout(function() {
//					if(pwdflag==0){
//						alert("下发失败");
//						websocket.close();
//						$('#smdlg').window("close")
//						$('#pwd').window('close');
//						$('#condlg').window("close");
//					}
//				}, 5000)
				var con = parseInt($('#passwd').numberbox('getValue')).toString(16);
				if(con.length<4){
					var length = 4 - con.length;
			        for(var i=0;i<length;i++){
			        	con = "0" + con;
			        }
			    };
			    var machine;
			    var selectMachine = $('#weldingmachineTable').datagrid('getSelected');
			    if(selectMachine.gatherId==null||selectMachine.gatherId==""){
			    	alert("该焊机未对应采集编号!!!");
					websocket.close();
					return;
			    }else{
					machine = parseInt(selectMachine.gatherId).toString(16);
					if(machine.length<4){
						var length = 4 - machine.length;
				        for(var i=0;i<length;i++){
				        	machine = "0" + machine;
				        };
					}
			    };
				var xiafasend1 = machine+con;
				var xxx = xiafasend1.toUpperCase();
				var data_length = ((parseInt(xxx.length)+12)/2).toString(16);
				if(data_length.length<2){
					var length = 2 - data_length.length;
			        for(var i=0;i<length;i++){
			        	data_length = "0" + data_length;
			        }
			    };
			    xxx="7E"+data_length+"01010153"+xiafasend1;
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
				var symbol = 0;
				var message = new Paho.MQTT.Message("7E"+xiafasend2+"7D");
				message.destinationName = "weldmes/downparams";
				client.send(message);
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
//				websocket.send("7E"+xiafasend2+"7D");
//				websocket.onmessage = function(msg) {
				client.onMessageArrived = function(e){
					var fan = e.payloadString;
					if(fan.substring(0,2)=="7E"&&fan.substring(10,12)=="53"){
						client.unsubscribe("weldmes/upparams", {
							onSuccess : function(e) {
								console.log("取消订阅成功");
							},
							onFailure : function(e) {
								console.log(e);
							}
						});
						window.clearTimeout(oneMinuteTimer);
						symbol = 1;
						if(parseInt(fan.substring(16,18),16)==1){
							alert("下发失败");
							$('#smdlg').window("close")
							$('#pwd').window('close');
							$('#condlg').window("close");
						}else{
							alert("下发成功");
							$('#smdlg').window("close")
							$('#pwd').window('close');
							$('#condlg').window("close");
						}
				};
			}

		}
	}else{
		alert("密码不能为空");
	}
}

function openPassDlg(){
	$('#pwd').window( {
		title : "密码下发",
		modal : true
	});
	$('#pwd').window('open');
}

function sxMachineIsLock(value){
	var selectMachine = $('#weldingmachineTable').datagrid('getSelections');
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
	var flag = 0;
	var sochet_send_data = new Array();
//	websocket.onopen = function() {
		for(var s=0;s<selectMachine.length;s++){
			var mach = (parseInt(selectMachine[s].gatherId,10)).toString(16);
			if (mach.length < 4) {
				var length = 4 - mach.length;
				for (var i = 0; i < length; i++) {
					mach = "0" + mach;
				}
			}
			sochet_send_data.push("FE5AA5001A"+mach+"000000000000000000000000000212020"+value+"0000");
		}
		var symbol = 0;
		var timer = window.setInterval(function() {
			if (sochet_send_data.length != 0) {
//				var popdata = sochet_send_data.pop();
				var message = new Paho.MQTT.Message(sochet_send_data.pop());
				message.destinationName = "weldmes/downparams";
				client.send(message);
//				websocket.send(popdata);//下发
			} else {
				window.clearInterval(timer);
			}
		}, 1000)
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
//		websocket.onmessage = function(msg) {
			if(e.payloadString.substring(0,6)=="FE5AA5" && e.payloadString.substring(40,44)=="0212"){
				if(e.payloadString.substring(e.payloadString.length-2)=="01"){
					alert("操作成功");
					$('#weldingmachineTable').datagrid('clearSelections');
					$('#smdlg').window("close");
//					websocket.close();
				}else{
					alert("操作失败");
					$('#weldingmachineTable').datagrid('clearSelections');
					$('#smdlg').window("close");
//					websocket.close();
				}
		}
	}
}

//function mqttTest(){
//	var clientId = Math.random().toString().substr(3,8) + Date.now().toString(36);
//	client = new Paho.MQTT.Client(websocketURL.split(":")[0], parseInt(websocketURL.split(":")[1]), clientId);
//	var options = {
//        timeout: 5,  
//        keepAliveInterval: 60,  
//        cleanSession: false,  
//        useSSL: false,  
//        onSuccess: onConnect,  
//        onFailure: function(e){  
//            console.log(e);  
//        },
//        reconnect : true
//	}
//	
//	//set callback handlers
//	client.onConnectionLost = onConnectionLost;
////	client.onMessageArrived = onMessageArrived;
//
//	//connect the client
//	client.connect(options);
//}
//
////called when the client connects
//function onConnect() {
//	// Once a connection has been made, make a subscription and send a message.
//	console.log("onConnect");
//}
//
////called when the client loses its connection
//function onConnectionLost(responseObject) {
//	if (responseObject.errorCode !== 0) {
//		console.log("onConnectionLost:"+responseObject.errorMessage);
//	}
//}