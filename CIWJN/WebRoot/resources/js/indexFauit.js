/**
 * 
 */
var socket;
var redata;
//var welder_no = new Array();
//var welder_name = new Array();
var machine_no = new Array();
var insf_name = new Array();
var ary = new Array();
var fauitContent,timer,fauitstr;
$(function(){
	fauitContent = document.getElementById('fauitContent');
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : "td/AllTdbf",  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {
	          if (result) {
	        	  websocketURL = eval(result.web_socket);
	          }  
	      },
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	 });
	getWelderMachine();
    websocket();
    $("#fauitContent").width($("body").width()-260);//260为图片的宽度+滚动栏及图片距右侧间距
})

//获取所有焊工编号和姓名
function getWelderMachine(){
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : "welders/getWelderMachine",  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {
	          if (result) {
//	        	  for(var i=0;i<result.welderary.length;i++){
//	        		  welder_no.push(result.welderary[i].welderno);
//	        		  welder_name.push(result.welderary[i].weldername);
//	        	  }
	        	  for(var i=0;i<result.machineary.length;i++){
	        		  machine_no.push(result.machineary[i].machineno);
	        		  insf_name.push(result.machineary[i].insfname);
	        	  }
	          }  
	      },
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	 });
}

function websocket() {
	if(typeof(WebSocket) == "undefined") {
		WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
		WEB_SOCKET_DEBUG = true;
	}
	webclient();
};
function webclient(){
	try{
		socket = new WebSocket(websocketURL);
	}catch(err){
//		alert("地址请求错误，请清除缓存重新连接！！！")
	}
	setTimeout(function(){
		if(socket.readyState!=1){
//			alert("与服务器连接失败,请检查网络设置!");
		}
	},10000);
	socket.onopen = function() {
	};
	socket.onmessage = function(msg) {
		redata=msg.data;
		view();
	};
	//关闭事件
	socket.onclose = function(e) {
		console.log(e);
        if (e.code == 4001 || e.code == 4002 || e.code == 4003 || e.code == 4005 || e.code == 4006){
            //如果断开原因为4001 , 4002 , 4003 不进行重连.
            return;
        }else{
            return;
        }
        // 重试3次，每次之间间隔5秒
        if (tryTime < 3) {
            setTimeout(function () {
                socket = null;
                tryTime++;
                var _PageHeight = document.documentElement.clientHeight,  
                _PageWidth = document.documentElement.clientWidth;   
                var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,  
                	_LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;  
                var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;background:#f3f8ff;opacity:0.8;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #fff url(resources/images/load.gif) no-repeat scroll 5px 10px; border: 2px solid #95B8E7; color: #696969;">""正在尝试第"'+tryTime+'"次重连，请稍候..."</div></div>';  
            	document.write(_LoadingHtml);
                ws();
            }, 5000);
        } else {
            tryTime = 0;
        }
    };
	//发生了错误事件
	socket.onerror = function(e) {
		console.log(e);
		alert("发生异常，正在尝试重新连接服务器！！！");
	}
}

function view(){
	for(var i = 0;i < redata.length;i+=111){
		var mstatus=redata.substring(0+i, 2+i);//故障状态
		switch (mstatus){
//			case "00":
//				content("待机",i);
//				break;
			case "01":
				content("E-010 焊枪开关OFF等待",i);
				break;
			case "02":
				content("E-000工作停止",i);
				break;
			case "10":
				content("E-100控制电源异常",i);
				break;
			case "15":
				content("E-150一次输入电压过高",i);
				break;
			case "16":
				content("E-160一次输入电压过低",i);
				break;
			case "20":
				content("E-200一次二次电流检出异常",i);
				break;
			case "21":
				content("E-210电压检出异常",i);
				break;
			case "22":
				content("E-220逆变电路反馈异常",i);
				break;
			case "30":
				content("E-300温度异常",i);
				break;
			case "70":
				content("E-700输出过流异常",i);
				break;
			case "71":
				content("E-710输入缺相异常",i);
				break;
			default:
				fauitstr = "";
				break;
		}
	}
};

function content(fauit,index){
//	var weldername;
	var insfname,str;
	var machineno = redata.substring(4+index, 8+index);//焊机编号
//	var welderno = redata.substring(8+index, 12+index);//焊工编号
	var time = redata.substring(20+index, 39+index);//发生时间
//	for(var i=0;i<welder_no.length;i++){
//		if(welder_no[i]==welderno){
//			weldername = welder_name[i];
//			break;
//		}
//	}
	for(var j=0;j<machine_no.length;j++){
		if(machine_no[j]==machineno){
			insfname = insf_name[j];
			break;
		}
	}
//	if(!weldername){
//		weldername == "未知";
//	}
	if(!insfname){
		insfname == "未知";
	}
//	str = "<span style='color:red'>！</span> 焊机编号："+ machineno +"，焊机归属："+ insfname +"，焊工编号："+ welderno +"，焊工名称："
//	+ weldername +"，<span style='color:#0254ad'><a href='javascript:openLive()'>故障代码及名称："+ fauit +"</a></span>，发生时间："+ time+"&nbsp;&nbsp;&nbsp;";
	str = "<span style='color:red'>！</span> 焊机编号："+ machineno +"，焊机归属："+ insfname +"<span style='color:#0254ad'><a href='javascript:openLive()'>，故障代码及名称："+ fauit +"</a></span>，发生时间："+ time+"&nbsp;&nbsp;&nbsp;";
	ary.push(str);
	fauitstr = str;
	console.log("str数据："+fauitstr);
}

function  openLive(){
	window.open('td/AllTd');
}

window.setTimeout(function() {
	var num = 5;
	if(ary.length<5){
		num = ary.length;
	}
	for(var i=0;i<num;i++){
		$("#content").append(ary[i]);
	}
	ary.length = 0;
//	for(var i=0;i<num;i++){
//		ary.shift();//从前端开始删除数组
//	}
//	if(!$("#content").html()){
//		$("#content").append("云智能焊接管控系统欢迎您");
//	}
	timer = window.setInterval(move, 10);
    fauitContent.onmouseover = function () {
        window.clearInterval(timer);
    };
    fauitContent.onmouseout = function () {
        timer = window.setInterval(move, 10);
    };
}, 5000);

function move() {
    if(!fauitstr){
    	$("#content").html("");
    }else{
	    fauitContent.scrollLeft++;
		if(fauitContent.scrollLeft+$("#fauitContent").width()>=$("#content").width()){//当前数据是否已经显示完成
	//		if(ary.length>0){
				$("#content").append(ary[ary.length-1]);
				console.log(ary[ary.length-1]);
//				fauitstr = "";
	//		}
		}
    }
}

//监听窗口大小变化
window.onresize = function() {
    $("#fauitContent").width($("body").width()-260);
}

window.setInterval(function() {
	welder_no = new Array();
	welder_name = new Array();
	getWelderMachine();
}, 60000);
