/**
 *  单个焊机的实时界面展示
 */
function back() {
    var url = "td/AllTd";
    var img = new Image();
    img.src = url; // 设置相对路径给Image, 此时会发送出请求
    url = img.src; // 此时相对路径已经变成绝对路径
    img.src = null; // 取消请求
    window.location.href = url;
}
;

var historytime;

var machine = new Array();
var time = new Array();
var ele = new Array();
var vol = new Array();
var machstatus = new Array();
var work = new Array();
var wait = new Array();
var worktime = new Array();
var dglength;
var websocketURL, mqttClintId;
var welderName;
var taskNum;
var symbol = 0;
var symbol1 = 0;
var sym = 0;
var timerele;
var timervol;
var socket;
var redata;
var rowdex = 0;
var maxele = 0;
var minele = 0;
var maxvol = 0;
var minvol = 0;
var warnele_up = 0, warnele_down = 0, warnvol_up = 0, warnvol_down = 0;
var rows;
var fmch;
var tongdao;
var sint = 0;
var series;
var chart;
var series1;
var chart1;
var time1 = 0, time2 = 0, timeFlag = 0;
var led = ["0,1,2,4,5,6", "2,5", "0,2,3,4,6", "0,2,3,5,6", "1,2,3,5", "0,1,3,5,6", "0,1,3,4,5,6", "0,2,5", "0,1,2,3,4,5,6", "0,1,2,3,5,6"];
var lockReconnect = false;//避免重复连接
$(function () {
    var type = $("#type").val(), imgnum = 0;
    var manuturer = $("#manufacture").val();
    if (type == 41) {
        imgnum = 1;
    } else if (type == 42) {
        imgnum = 2;
    } else if (type == 43) {
        if (manuturer == 147) {
            imgnum = 4;
        } else {
            imgnum = 2;
        }
        //imgnum = 3;
    }
    $("#mrjpg").attr("src", "resources/images/welder_4" + imgnum + ".png");
    var livewidth = $("#livediv").width() * 0.9;
    var liveheight = $("#livediv").height() / 2;
    $("#body31").width(livewidth);
    $("#body31").height(liveheight);
    $("#body32").width(livewidth);
    $("#body32").height(liveheight);
    var width = $("#treeDiv").width();
    var machinemodel = $("#machinemodel").val();
    if (machinemodel == 171) {
        $("#r5").textbox('setValue', 30);
    } else {
        $("#r5").textbox('setValue', 100);
    }
    $.ajax({
        type: "post",
        async: false,
        url: "td/AllTdbf",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                websocketURL = eval(result.web_socket);
                mqttClintId = result.userName;
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $.ajax({
        type: "post",
        async: false,
        url: "td/getLiveWelder",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                welderName = eval(result.rows);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    //查询任务信息
    $.ajax({
        type: "post",
        async: true,
        url: "weldtask/getWeldTask",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                taskNum = eval(result.rows);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    //查询开关机时间和焊接时长
    $.ajax({
        type: "post",
        async: true,
        url: "td/findWeldTimeInfo",
        data: {
            machineId: $("#machineid").val()
        },
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result){
                $("#r1").textbox('setValue', result.starttime);
                $("#r2").textbox('setValue', result.endtime);
                $("#junctionNo").val(result.junctionNo);
                time2 = time2 + Number(result.worktime);
                var t2 = secondToDate(time2);
                $("#r4").textbox('setValue', t2);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
    $("#l3").html($("#junctionNo").val());
    mqttTest();
    setInterval("serach()", 60000);// 注意函数名没有引号和括弧！
});

function serach() {
    var timebuf = historytime;
    if (null != timebuf) {
        var date = new Date().getTime();
        if (date - timebuf > 60000) {
            $("#l5").val("关机");
            $("#l5").css("background-color", "#818181");
            $("#mrjpg").attr("src", "resources/images/welder_03.png");
            $("#l3").html($("#junctionNo").val());
        }
    }
}

var client, clientId;

function mqttTest() {
//	clientId = Math.random().toString().substr(3,8) + Date.now().toString(36);
    clientId = mqttClintId + "_RTC_TWO";
    client = new Paho.MQTT.Client(websocketURL.split(":")[0], parseInt(websocketURL.split(":")[1]), clientId);
    var options = {
        timeout: 5,
        keepAliveInterval: 60,
        cleanSession: false,
        useSSL: false,
        onSuccess: onConnect,
        onFailure: function (e) {
            console.log(e);
        },
        reconnect: true
    }

    //set callback handlers
    client.onConnectionLost = onConnectionLost;
    client.onMessageArrived = onMessageArrived;

    //connect the client
    client.connect(options);
}

//called when the client connects
function onConnect() {
    // Once a connection has been made, make a subscription and send a message.
    console.log("onConnect");
//	client.publish('/public/TEST/SHTH', 'SHTHCS', 0, false);
    client.subscribe("weldmes/rtcdata", {
        qos: 0,
        onSuccess: function (e) {
            console.log("订阅成功");
        },
        onFailure: function (e) {
            console.log(e);
        }
    });
    if (document.getElementById('loadingDiv')) {
        var loadingMask = document.getElementById('loadingDiv');
        loadingMask.parentNode.removeChild(loadingMask);
    }
}

//called when the client loses its connection
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
    }
}

//called when a message arrives
function onMessageArrived(message) {
//	console.log("onMessageArrived:"+message.payloadString);
    redata = message.payloadString;
    iview();
    /*	message = new Paho.MQTT.Message("1");
        message.destinationName = "api2";
        client.send(message);*/
}

function websocket() {
    if (typeof (WebSocket) == "undefined") {
        WEB_SOCKET_SWF_LOCATION = "resources/js/WebSocketMain.swf";
        WEB_SOCKET_DEBUG = true;
    }
    createWebSocket();
}

function createWebSocket() {
    try {
        socket = new WebSocket(websocketURL);
        webclient();
    } catch (e) {
        console.log('catch');
        reconnect();
    }
}

function webclient() {
    socket.onopen = function () {
        //				datatable();
        //监听加载状态改变
        document.onreadystatechange = completeLoading();

        //加载状态为complete时移除loading效果
        function completeLoading() {
            if (document.getElementById('loadingDiv')) {
                var loadingMask = document.getElementById('loadingDiv');
                loadingMask.parentNode.removeChild(loadingMask);
            }
        }

        lockReconnect = false;
    };
    socket.onmessage = function (msg) {
        redata = msg.data;
        iview();
        if (timeFlag == 0) {
            timeFlag = 1;
        }
    };
    //关闭事件
    socket.onclose = function (e) {
        if (lockReconnect == true) {
            return;
        }
        reconnect();
    };
    //发生了错误事件
    socket.onerror = function (e) {
        if (lockReconnect == true) {
            return;
        }
        reconnect();
    }
}

function reconnect() {
    if (lockReconnect == true) {
        return;
    }
    lockReconnect = true;
    var tt = window.setInterval(function () {
        if (lockReconnect == false) {
            window.clearInterval(tt);
        }
        try {
            createWebSocket();
        } catch (e) {
            console.log(e.message);
        }
    }, 10000);
}

function elecurve() {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

    $('#body31').highcharts({
        chart: {
            type: 'spline',
            animation: false, // don't animate in old IE
            marginRight: 70,
            events: {
                load: function () {
                    // set up the updating of the chart each second
                    series = this.series[0],
                        chart = this,
                        timerele = window.setInterval(function () {
                        }, 1000);
                }
            }
        },
        title: {
            text: ''
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 150,
            lineColor: '#FFFFFF',
            tickWidth: 0,
            labels: {
                enabled: false
            }
        },
        yAxis: [{
            max: 500, // 定义Y轴 最大值
            min: 0, // 定义最小值
            minPadding: 0.2,
            maxPadding: 0.2,
            tickInterval: 100,
            color: '#A020F0',
            title: {
                text: '',
                style: {
                    color: '#A020F0'
                }
            }
        }],
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false // 禁用版权信息
        },
        series: [{
            color: '#A020F0',
            name: '电流',

            data: (function () {
                // generate an array of random data
                var data = [],
                    /*time = new Date(Date.parse("0000-00-00 00:00:00")),*/
                    i;
                for (i = -19; i <= 0; i += 1) {
                    data.push({
                        x: time[0] - 1000 + i * 1000,
                        /*x: time + i*1000,*/
                        y: 0
                    });
                }
                return data;
            }())
        }]
    }, function (c) {
        activeLastPointToolip(c)
    });
    activeLastPointToolip(chart);
}

function volcurve() {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

    $('#body32').highcharts({
        chart: {
            type: 'spline',
            animation: false, // don't animate in old IE
            marginRight: 70,
            events: {
                load: function () {
                    // set up the updating of the chart each second
                    series1 = this.series[0],
                        chart1 = this;
                }
            }
        },
        title: {
            text: false
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 150 /*,
	  		        tickWidth:0,
		  		    labels:{
		  		    	enabled:false
		  		    }*/
        },
        yAxis: [{
            max: 50, // 定义Y轴 最大值
            min: 0, // 定义最小值
            minPadding: 0.2,
            maxPadding: 0.2,
            tickInterval: 10,
            color: '#87CEFA',
            title: {
                text: '',
                style: {
                    color: '#87CEFA'
                }
            }
        }],
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false // 禁用版权信息
        },
        series: [{
            name: '电压',
            data: (function () {
                // generate an array of random data
                var data = [],
                    i;
                for (i = -19; i <= 0; i += 1) {
                    data.push({
                        x: time[0] - 1000 + i * 1000,
                        y: 0
                    });
                }
                return data;
            }())
        }]
    }, function (c) {
        activeLastPointToolip1(c)
    });
    activeLastPointToolip1(chart1);

}

function iview() {
    var z = 0;
    time.length = 0;
    vol.length = 0;
    ele.length = 0;
    if (redata.length % 111 === 0) {
        for (var i = 0; i < redata.length; i += 111) {
            if (parseInt(redata.substring(4 + i, 8 + i), 10).toString() === $("#machineid").val()) {
                //if (time2 != 0) {
                historytime = new Date().getTime();
                time1++;
                var t1 = secondToDate(time1);
                $("#r3").textbox('setValue', t1);
                if (redata.substring(36 + i, 38 + i) !== "00") {
                    time2++;
                    var t2 = secondToDate(time2);
                    $("#r4").textbox('setValue', t2);
                }
                //}
                ele.push(parseInt(redata.substring(38 + i, 42 + i), 10));
                vol.push(parseFloat((parseInt(redata.substring(42 + i, 46 + i), 10) / 10).toFixed(2)));
                $("#r10").textbox('setValue', parseInt(redata.substring(38 + i, 42 + i), 10) * parseFloat((parseInt(redata.substring(42 + i, 46 + i), 10) / 10).toFixed(2)));
                var ttme = redata.substring(54 + i, 73 + i);
                ttme = ttme.replace(/-/g, '/');
                time.push(Date.parse(new Date(ttme)));
                machstatus.push(redata.substring(36 + i, 38 + i));
                maxele = parseInt(redata.substring(75 + i, 79 + i), 10);
                minele = parseInt(redata.substring(79 + i, 83 + i), 10);
                maxvol = parseFloat(parseInt(redata.substring(83 + i, 87 + i), 10) / 10).toFixed(2);
                minvol = parseFloat(parseInt(redata.substring(87 + i, 91 + i), 10) / 10).toFixed(2);
                warnele_up = parseInt(redata.substring(95 + i, 99 + i), 10);
                warnele_down = parseInt(redata.substring(99 + i, 103 + i), 10);
                warnvol_up = parseFloat(parseInt(redata.substring(103 + i, 107 + i), 10) / 10).toFixed(2);
                warnvol_down = parseFloat(parseInt(redata.substring(107 + i, 111 + i), 10) / 10).toFixed(2);
                if (symbol == 0) {
                    elecurve();
                    volcurve();
                    symbol++;
                }
                $("#l3").html("--");
                $("#l4").html("--");
                $("#r13").html(parseInt(redata.substring(46 + i, 50 + i), 10));
                $("#r14").html(parseFloat((parseInt(redata.substring(50 + i, 54 + i), 10) / 10).toFixed(2)));
                $("#c1").html(parseInt(redata.substring(38 + i, 42 + i), 10));
                $("#c2").html((parseInt(redata.substring(42 + i, 46 + i), 10) / 10).toFixed(1));
                if (parseInt(redata.substring(91 + i, 95 + i), 10) == 255) {
                    $("#r6").textbox('setValue', "自由调节状态");
                } else {
                    $("#r6").textbox('setValue', parseInt(redata.substring(91 + i, 95 + i), 10));
                }
                for (var k = 0; k < welderName.length; k++) {
                    if (welderName[k].fid == parseInt(redata.substring(0 + i, 4 + i), 10)) {
                        $("#l4").html(welderName[k].fname);
                    }
                }
                for (var t = 0; t < taskNum.length; t++) {
                    if (taskNum[t].id == parseInt(redata.substring(12 + i, 16 + i), 10)) {
                        $("#l3").html(taskNum[t].weldedJunctionno);
                    }
                }
                $("#l2").html(worktime.machineno);
                var type = $("#type").val(), imgnum = 0;
                var manuturer = $("#manufacture").val();
                if (type == 41) {
                    imgnum = 1;
                } else if (type == 42) {
                    imgnum = 3;
                } else if (type == 43) {
                    if (manuturer == 147) {
                        imgnum = 4;
                    } else {
                        imgnum = 2;
                    }
                }
                var mstatus = redata.substring(36 + i, 38 + i);
                switch (mstatus) {
                    case "00":
                        $("#l5").val("待机");
                        $("#l5").css("background-color", "#55a7f3");
                        $("#mrjpg").attr("src", "resources/images/welder_2" + imgnum + ".png");
                        break;
                    case "01":
                        $("#l5").val("E-010 焊枪开关OFF等待");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "02":
                        $("#l5").val("E-000工作停止");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "03":
                        $("#l5").val("焊接");
                        $("#l5").css("background-color", "#7cbc16");
                        $("#mrjpg").attr("src", "resources/images/welder_1" + imgnum + ".png");
                        break;
                    case "04":
                        $("#l5").val("电流过低");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "05":
                        $("#l5").val("收弧");
                        $("#l5").css("background-color", "#7cbc16");
                        $("#mrjpg").attr("src", "resources/images/welder_1" + imgnum + ".png");
                        break;
                    case "06":
                        $("#l5").val("电流过高");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "07":
                        $("#l5").val("启弧");
                        $("#l5").css("background-color", "#7cbc16");
                        $("#mrjpg").attr("src", "resources/images/welder_1" + imgnum + ".png");
                        break;
                    case "08":
                        $("#l5").val("电压过低");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "09":
                        $("#l5").val("电压过高");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "10":
                        $("#l5").val("E-100控制电源异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "15":
                        $("#l5").val("E-150一次输入电压过高");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "16":
                        $("#l5").val("E-160一次输入电压过低");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "20":
                        $("#l5").val("E-200一次二次电流检出异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "21":
                        $("#l5").val("E-210电压检出异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "22":
                        $("#l5").val("E-220逆变电路反馈异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "30":
                        $("#l5").val("E-300温度异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "70":
                        $("#l5").val("E-700输出过流异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "71":
                        $("#l5").val("E-710输入缺相异常");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "98":
                        $("#l5").val("超规范停机");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                    case "99":
                        $("#l5").val("超规范报警");
                        $("#l5").css("background-color", "#fe0002");
                        $("#mrjpg").attr("src", "resources/images/welder_3" + imgnum + ".png");
                        break;
                }
                if ($("#l5").val() == "关机") {
                    var endtime = $("#endtime").val();
                    $("#r2").textbox('setValue', endtime);
                } else {
                    $("#r2").textbox('setValue', "--");
                }
                if ($("#l5").val() == "关机") {
                    var junctionNo = $("#junctionNo").val();
                    $("#l3").textbox('setValue', junctionNo);
                }
                if (time.length != 0 && z < time.length) {
                    var x = time[z],
                        y = ele[z],
                        v = vol[z];
                    if (z == 0) {
                        series.addPoint([x, y], true, true);
                        activeLastPointToolip(chart);
                        series1.addPoint([x, v], true, true);
                        activeLastPointToolip1(chart1);

                    } else {
                        if (x > time[z - 1]) {
                            series.addPoint([x, y], true, true);
                            activeLastPointToolip(chart);
                            series1.addPoint([x, v], true, true);
                            activeLastPointToolip1(chart1);
                        }
                    }
                }
            }
            z++;
        }
    }
    if ((time.length) % 3 == 1) {
        ele[time.length] = ele[time.length - 1];
        ele[time.length + 1] = ele[time.length - 1];
        vol[time.length] = vol[time.length - 1];
        vol[time.length + 1] = vol[time.length - 1];
        time[time.length] = time[time.length - 1] + 1000;
        time[time.length + 1] = time[time.length - 1] + 2000;
    }
    if (time.length % 3 == 2) {
        ele[time.length] = ele[time.length - 1];
        vol[time.length] = vol[time.length - 1];
        time[time.length] = time[time.length - 1] + 1000;
    }
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    var livewidth = $("#livediv").width() * 0.9 - 10;
    var liveheight = $("#livediv").height() * 0.5 - 10;
    $("#body31").width(livewidth);
    $("#body31").height(liveheight);
    $("#body32").width(livewidth);
    $("#body32").height(liveheight);
//	$('#body31').highcharts().reflow();
//	$('#body32').highcharts().reflow();
}


function activeLastPointToolip(chart) {
    var points = chart.series[0].points;
    chart.yAxis[0].removePlotLine('plot-line-6');
    chart.yAxis[0].removePlotLine('plot-line-7');
    chart.yAxis[0].addPlotLine({ //在y轴上增加
        value: warnele_up, //在值为2的地方
        width: 2, //标示线的宽度为2px
        color: 'red', //标示线的颜色
        dashStyle: 'longdashdot',
        id: 'plot-line-6', //标示线的id，在删除该标示线的时候需要该id标示 });
        label: {
            text: '报警电流上限', //标签的内容
            align: 'center', //标签的水平位置，水平居左,默认是水平居中center
            x: 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
        }
    });
    chart.yAxis[0].addPlotLine({ //在y轴上增加
        value: warnele_down, //在值为2的地方
        width: 2, //标示线的宽度为2px
        color: 'red', //标示线的颜色
        dashStyle: 'longdashdot',
        id: 'plot-line-7', //标示线的id，在删除该标示线的时候需要该id标示 });
        label: {
            text: '报警电流下限', //标签的内容
            align: 'center', //标签的水平位置，水平居左,默认是水平居中center
            x: 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
        }
    })
    /*	chart.yAxis[0].addPlotLine({ //在y轴上增加
            value : (minele + maxele) / 2, //在值为2的地方
            width : 2, //标示线的宽度为2px
            color : 'red', //标示线的颜色
            dashStyle : 'longdashdot',
            id : 'plot-line-0', //标示线的id，在删除该标示线的时候需要该id标示 });
            label : {
                text : '预置电流', //标签的内容
                align : 'center', //标签的水平位置，水平居左,默认是水平居中center
                x : 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
            }
        })*/
}

function activeLastPointToolip1(chart) {
    var points = chart.series[0].points;
    chart.yAxis[0].removePlotLine('plot-line-8');
    chart.yAxis[0].removePlotLine('plot-line-9');
    chart.yAxis[0].addPlotLine({ //在y轴上增加
        value: warnvol_up, //在值为2的地方
        width: 2, //标示线的宽度为2px
        color: 'black', //标示线的颜色
        dashStyle: 'longdashdot',
        id: 'plot-line-8', //标示线的id，在删除该标示线的时候需要该id标示 });
        label: {
            text: '报警电压上限', //标签的内容
            align: 'center', //标签的水平位置，水平居左,默认是水平居中center
            x: 10
        }
    })
    chart.yAxis[0].addPlotLine({ //在y轴上增加
        value: warnvol_down, //在值为2的地方
        width: 2, //标示线的宽度为2px
        color: 'black', //标示线的颜色
        dashStyle: 'longdashdot',
        id: 'plot-line-9', //标示线的id，在删除该标示线的时候需要该id标示 });
        label: {
            text: '报警电压下限', //标签的内容
            align: 'center', //标签的水平位置，水平居左,默认是水平居中center
            x: 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
        }
    });
}

//显示系统当前时间
setInterval(function () {
    var date = new Date();
    year = date.getFullYear();
    month = date.getMonth() + 1;
    day = date.getDate();
    hours = date.getHours();
    oldminutes = date.getMinutes();
    oldseconds = date.getSeconds();
    seconds = oldseconds;
    minutes = oldminutes;
    if (oldminutes < 10) {
        minutes = "0" + oldminutes;
    }
    if (seconds < 10) {
        seconds = "0" + oldseconds;
    }
    $("#systemtime").html(year + "-" + month + "-" + day + "  " + hours + ":" + minutes + ":" + seconds);

}, 1000);

function secondToDate(result) {
    var h = Math.floor(result / 3600) < 10 ? '0' + Math.floor(result / 3600) : Math.floor(result / 3600);
    var m = Math.floor((result / 60 % 60)) < 10 ? '0' + Math.floor((result / 60 % 60)) : Math.floor((result / 60 % 60));
    var s = Math.floor((result % 60)) < 10 ? '0' + Math.floor((result % 60)) : Math.floor((result % 60));
    return result = h + ":" + m + ":" + s;
}