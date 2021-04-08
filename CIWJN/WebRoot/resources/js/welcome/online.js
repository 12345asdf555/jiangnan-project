var work = new Array();
var off = new Array(), on = new Array(), stand = new Array(), cleardata = new Array();
var wait = new Array();
var weld = new Array();
var mall = new Array();
var warn = new Array();
var machineary = [],
    mallary = [];
var websocketURL, mqttClintId;
var socket;
var redata;
var symbol = 0;
var machine;
var namex = new Array(), welderId = new Array();
var worknum = 0,
    waitnum = 0,
    warnnum = 0,
    offnum = 0,
    weldnum = 0,
    personnum = 0,
    machineflag = 0,
    personfalg = 0;
var lockReconnect = false;//避免重复连接
$(function () {
    welder();
    getMachineArray();
    websocketurl();
});

$(document).ready(function () {
    showPersonChart();
    showWelderChart();
});

function welder() {
    //焊工总数name.length
    $.ajax({
        type: "post",
        async: false,
        url: "td/allWeldname",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                namex = eval(result.rows);
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function getMachineArray() {
    //焊机总数machine.length
    $.ajax({
        type: "post",
        async: false,
        url: "td/getAllPosition",
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                machine = eval(result.rows);
                for (var i = 0; i < machine.length; i++) {
                    machineary.push(machine[i].fid);
                    off.push(machine[i].fid);
                }
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function websocketurl() {
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
                mqttTest();
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });
}

function mqttTest() {
//	var clientId = Math.random().toString().substr(3,8) + Date.now().toString(36);
    var clientId = mqttClintId + "_RTC_INDEX";
    index_client = new Paho.MQTT.Client(websocketURL.split(":")[0], parseInt(websocketURL.split(":")[1]), clientId);
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
    index_client.onConnectionLost = onConnectionLost;
    index_client.onMessageArrived = onMessageArrived;

    //connect the client
    index_client.connect(options);
}

//called when the client connects
function onConnect() {
    // Once a connection has been made, make a subscription and send a message.
    console.log("onConnect");
//	client.publish('/public/TEST/SHTH', 'SHTHCS', 0, false);
    index_client.subscribe("weldmes/rtcdata", {
        qos: 0,
        onSuccess: function (e) {
            console.log("订阅成功");
            if (document.getElementById('loadingDiv')) {
                var loadingMask = document.getElementById('loadingDiv');
                loadingMask.parentNode.removeChild(loadingMask);
            }
        },
        onFailure: function (e) {
            console.log(e);
            if (document.getElementById('loadingDiv')) {
                var loadingMask = document.getElementById('loadingDiv');
                loadingMask.parentNode.removeChild(loadingMask);
            }
        }
    })
}

//called when the client loses its connection
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
//		console.log("onConnectionLost:"+responseObject.errorMessage);
    }
}

//客户端收到服务端发过来的的消息
function onMessageArrived(message) {
//	console.log("onMessageArrived:"+message.payloadString);
    redata = message.payloadString;
    if (redata.length % 111 === 0) {
        if (symbol === 0) {
            window.setInterval(function () {
                clearDataFun();
                showWelderChart();
                showPersonChart();
            }, 3000);
            symbol = 1;
        }

        for (var i = 0; i < redata.length; i += 111) {
            if (redata.substring(0 + i, 4 + i) != "0000" && $.inArray(parseInt(redata.substring(0 + i, 4 + i), 10), namex) != -1) {
                //组织机构与焊工编号都与数据库中一致则录入
                if (weld.length == 0) {
                    weld.push(redata.substring(0 + i, 4 + i));
                } else {
                    if ($.inArray(redata.substring(0 + i, 4 + i), weld) == -1) {
                        weld.push(redata.substring(0 + i, 4 + i));
                    } else {
                        break;
                    }
                }
            }
            if (parseInt(redata.substring(4 + i, 8 + i), 10) != 0 && $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), machineary) != -1) {
                var cleardataIndex = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), cleardata);
                if (cleardataIndex == (-1)) {
                    cleardata.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                    cleardata.push(new Date().getTime());
                } else {
                    cleardata.splice(cleardataIndex + 1, 1, new Date().getTime());
                }
                var mstatus = redata.substring(36 + i, 38 + i);
                var livestatus, livestatusid, liveimg;
                if (mstatus == "00") {
                    var num;
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), stand);
                    if (num == (-1)) {
                        stand.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), warn);
                    if (num != (-1)) {
                        warn.splice(num, 1);
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), off);
                    if (num != (-1)) {
                        off.splice(num, 1);
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), on);
                    if (num != (-1)) {
                        on.splice(num, 1);
                    }
                } else if (mstatus == "03" || mstatus == "05" || mstatus == "07") {
                    var num;
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), on);
                    if (num == (-1)) {
                        on.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), warn);
                    if (num != (-1)) {
                        warn.splice(num, 1);
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), off);
                    if (num != (-1)) {
                        off.splice(num, 1);
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), stand);
                    if (num != (-1)) {
                        stand.splice(num, 1);
                    }
                } else {
                    var num;
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), warn);
                    if (num == (-1)) {
                        warn.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), on);
                    if (num != (-1)) {
                        on.splice(num, 1);
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), off);
                    if (num != (-1)) {
                        off.splice(num, 1);
                    }
                    num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), stand);
                    if (num != (-1)) {
                        stand.splice(num, 1);
                    }
                }
            }
        }
    }
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
        clearDataFun();
    };
    socket.onmessage = function (msg) {
        var xxx = msg.data;
        if (xxx.length == 333 || xxx.length == 111) {
//			if (xxx.substring(0, 2) != "7E") {
            redata = msg.data;
            if (symbol == 0) {
                window.setTimeout(function () {
                    showWelderChart();
                    showPersonChart();
                }, 60000);
                symbol = 1;
            }

            for (var i = 0; i < redata.length; i += 111) {
                if (redata.substring(0 + i, 4 + i) != "0000" && $.inArray(parseInt(redata.substring(0 + i, 4 + i), 10), namex) != -1) {
                    //组织机构与焊工编号都与数据库中一致则录入
                    if (weld.length == 0) {
                        weld.push(redata.substring(0 + i, 4 + i));
                    } else {
                        if ($.inArray(redata.substring(0 + i, 4 + i), weld) == -1) {
                            weld.push(redata.substring(0 + i, 4 + i));
                        } else {
                            break;
                        }
                    }
                }
                if (parseInt(redata.substring(4 + i, 8 + i), 10) != 0 && $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), machineary) != -1) {
                    var cleardataIndex = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), cleardata);
                    if (cleardataIndex == (-1)) {
                        cleardata.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                        cleardata.push(new Date().getTime());
                    } else {
                        cleardata.splice(cleardataIndex + 1, 1, new Date().getTime());
                    }
                    var mstatus = redata.substring(36 + i, 38 + i);
                    var livestatus, livestatusid, liveimg;
                    if (mstatus == "00") {
                        var num;
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), stand);
                        if (num == (-1)) {
                            stand.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), warn);
                        if (num != (-1)) {
                            warn.splice(num, 1);
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), off);
                        if (num != (-1)) {
                            off.splice(num, 1);
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), on);
                        if (num != (-1)) {
                            on.splice(num, 1);
                        }
                    } else if (mstatus == "03" || mstatus == "05" || mstatus == "07") {
                        var num;
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), on);
                        if (num == (-1)) {
                            on.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), warn);
                        if (num != (-1)) {
                            warn.splice(num, 1);
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), off);
                        if (num != (-1)) {
                            off.splice(num, 1);
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), stand);
                        if (num != (-1)) {
                            stand.splice(num, 1);
                        }
                    } else {
                        var num;
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), warn);
                        if (num == (-1)) {
                            warn.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), on);
                        if (num != (-1)) {
                            on.splice(num, 1);
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), off);
                        if (num != (-1)) {
                            off.splice(num, 1);
                        }
                        num = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), stand);
                        if (num != (-1)) {
                            stand.splice(num, 1);
                        }
                    }
                }

            }
//			}
//				var option = weldercharts.getOption();
//				option.series[0].data = [
//		            {value:on.length,
//						name : '工作',
//						itemStyle : {
//							normal : {
//								color : '#66b731'
//							}
//						}},
//		            {value:stand.length,
//						name : '待机',
//						itemStyle : {
//							normal : {
//								color : '#2da2f1'
//							}
//						}},
//		            {value:warn.length,
//						name : '故障',
//						itemStyle : {
//							normal : {
//								color : '#dc0201'
//							}
//						}},
//		            {value:off.length,
//						name : '关机',
//						itemStyle : {
//							normal : {
//								color : '#ebebeb'
//							}
//						}}
//		        ];
//				weldercharts.setOption(option);
        }
        ;
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

var personcharts;

function showPersonChart() {
    //初始化echart实例
    personcharts = echarts.init(document.getElementById("person"));
    //显示加载动画效果
    personcharts.showLoading({
        text: '稍等片刻,精彩马上呈现...',
        effect: 'whirling'
    });
    option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        tooltip: {
            trigger: 'item',
            formatter: function (param) {
                if (param.name == "其它") {
                    return "";
                } else {
                    return '焊工在线统计<br/>' + param.name + '：' + param.value;
                }
            }
        },
        legend: {
            orient: 'vertical',
            x: 'right',
            top: 70,
            data: ['焊工', '在线'],
            formatter: function (name) {
                var index = 0;
                var clientlabels = ['焊工', '在线'];
                var clientcounts = [namex.length, weld.length];
                $.each(clientlabels, function (i, value) {
                    if (value == name) {
                        index = i;
                    }
                })
                return name + "：" + clientcounts[index];
            }
        },
        series: [
            {
                name: '焊工在线统计',
                type: 'pie',
                radius: ['45%', '65%'],
                center: ['40%', '50%'],
                color: ['#abced2'],
                data: [
                    {
                        value: namex.length,
                        name: '焊工',
                        itemStyle: {
                            normal: {
                                color: '#abced2'
                            }
                        }
                    }
                ].sort(function (a, b) {
                    return a.value - b.value;
                }),
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    }
                },
                animationType: 'scale',
                animationEasing: 'elasticOut',
                animationDelay: function (idx) {
                    return Math.random() * 200;
                }
            }, {
                name: '焊工在线统计',
                type: 'pie',
                radius: ['25%', '40%'],
                center: ['40%', '50%'],
                color: ['#67b73e', '#ffffff'],
                data: [
                    {
                        value: weld.length,
                        name: '在线',
                        itemStyle: {
                            normal: {
                                color: '#67b73e'
                            }
                        }
                    },
                    {
                        value: namex.length - weld.length,
                        name: '其它',
                        itemStyle: {
                            normal: {
                                color: '#dbdbdb'
                            }
                        }
                    }
                ],
                hoverAnimation: false, //鼠标悬停区域不放大
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    }
                },
                itemStyle: {
                    normal: {
                        label: {
                            formatter: function (param) {
                                return param.name + "：" + param.value + "%";
                            }
                        }
                    }
                }
            }
        ]
    }
    //为echarts对象加载数据
    personcharts.setOption(option);
    //隐藏动画加载效果
    personcharts.hideLoading();
//	weld.length = 0;
}

var weldercharts, flagnum = 0;

function showWelderChart() {
    if (flagnum == 0) {
        flagnum = 1;
        //初始化echart实例
        weldercharts = echarts.init(document.getElementById("machine"));
    }
    //显示加载动画效果
    weldercharts.showLoading({
        text: '稍等片刻,精彩马上呈现...',
        effect: 'whirling'
    });
    option = {
        title: {
            text: machine.length + '台',
            left: '35%',
            top: '45%', //标题显示在pie中间
            textStyle: {
                fontSize: 12,
                align: 'center'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'right',
            top: 50,
            data: ['工作', '待机', '故障', '关机'],
            formatter: function (name) {
                var index = 0;
                var clientlabels = ['工作', '待机', '故障', '关机'];
                var clientcounts = [on.length, stand.length, warn.length, off.length];
                $.each(clientlabels, function (i, value) {
                    if (value == name) {
                        index = i;
                    }
                })
                return name + "：" + clientcounts[index];
            }
        },
        series: [
            {
                name: '焊机在线统计',
                type: 'pie',
                radius: ['40%', '60%'],
                center: ['40%', '50%'],
                data: [
                    {
                        value: on.length,
                        name: '工作',
                        itemStyle: {
                            normal: {
                                color: '#66b731'
                            }
                        }
                    },
                    {
                        value: stand.length,
                        name: '待机',
                        itemStyle: {
                            normal: {
                                color: '#2da2f1'
                            }
                        }
                    },
                    {
                        value: warn.length,
                        name: '故障',
                        itemStyle: {
                            normal: {
                                color: '#dc0201'
                            }
                        }
                    },
                    {
                        value: off.length,
                        name: '关机',
                        itemStyle: {
                            normal: {
                                color: '#ebebeb'
                            }
                        }
                    }
                ].sort(function (a, b) {
                    return a.value - b.value;
                }),
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    }
                },
                labelLine: {
                    normal: {
                        length: 0,
                        show: false
                    }
                },
                animationType: 'scale',
                animationEasing: 'elasticOut',
                animationDelay: function (idx) {
                    return Math.random() * 200;
                }
            }
        ]
    }
    //为echarts对象加载数据
    weldercharts.setOption(option);
    //隐藏动画加载效果
    weldercharts.hideLoading();
}

function clearDataFun() {
    var temp = 0;
    window.setInterval(function () {
        var timeflag = new Date().getTime();
        for (var i = 0; i < cleardata.length; i = i + 2) {
            if (timeflag - cleardata[i + 1] >= 30000) {
                cleardata.splice(i + 1, 1);
                var num;
                num = $.inArray(cleardata[i], stand);
                if (num != (-1)) {
                    stand.splice(num, 1);
                }
                num = $.inArray(cleardata[i], warn);
                if (num != (-1)) {
                    warn.splice(num, 1);
                }
                num = $.inArray(cleardata[i], on);
                if (num != (-1)) {
                    on.splice(num, 1);
                }
                num = $.inArray(cleardata[i], off);
                if (num == (-1)) {
                    off.push(cleardata[i]);
                }
                cleardata.splice(i, 1);
                temp = 1;
            }
        }

        var option = weldercharts.getOption();
        option.series[0].data = [
            {
                value: on.length,
                name: '工作',
                itemStyle: {
                    normal: {
                        color: '#66b731'
                    }
                }
            },
            {
                value: stand.length,
                name: '待机',
                itemStyle: {
                    normal: {
                        color: '#2da2f1'
                    }
                }
            },
            {
                value: warn.length,
                name: '故障',
                itemStyle: {
                    normal: {
                        color: '#dc0201'
                    }
                }
            },
            {
                value: off.length,
                name: '关机',
                itemStyle: {
                    normal: {
                        color: '#dddcdc'
                    }
                }
            }
        ];
        if (temp == 1) {
            temp = 0;
            location.reload();
        }
        weldercharts.setOption(option);
    }, 3000);
}

//window.setInterval(function() {
//	showWelderChart();
//	showPersonChart();
////	off.length=0;
////	on.length=0;
////	stand.length=0;
////	warn.length=0;
//}, 30000);