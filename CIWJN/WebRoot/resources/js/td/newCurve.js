var insfid;
var lockReconnect = false;//避免重复连接
var websocketURL, symbol = 0, welderName, taskNum, socket, mqttClintId;
var showflag = 0, timeflag;
var liveary = new Array(), machine = new Array();
var offFlag = 0;
var off = new Array(), on = new Array(), warn = new Array(), stand = new Array(), cleardata = new Array();
$(function () {
    loadtree();
    websocketUrl();
//	websocket();
    mqttTest();
    //状态发生改变
    $("#status").combobox({
        onChange: function (newValue, oldValue) {
            statusClick(newValue);
        }
    });
})

function loadtree() {
    $("#myTree").tree({
        url: 'insframework/getConmpany', //请求路径
        onLoadSuccess: function (node, data) {
            var tree = $(this);
            if (data) {
                $(data).each(function (index, d) {
                    if (this.state == 'closed') {
                        tree.tree('expandAll');
                    }
                    $('#_easyui_tree_1 .tree-icon').css("background", "url(resources/images/menu_1.png) no-repeat center center");
                    var nownodes = $('#myTree').tree('find', data[0].id);
                    //判断是否拥有子节点,改变子节点图标
                    if (nownodes.children != null) {
                        for (var i = 0; i < nownodes.children.length; i++) {
                            var nextnodes1 = nownodes.children[i];
                            $('#' + nextnodes1.domId + ' .tree-icon').css("background", "url(resources/images/menu_2.png) no-repeat center center");
                            if (nextnodes1.children != null) {
                                for (var j = 0; j < nextnodes1.children.length; j++) {
                                    var nextnodes2 = nextnodes1.children[j];
                                    $('#' + nextnodes2.domId + ' .tree-icon').css("background", "url(resources/images/menu_3.png) no-repeat center center");
                                    if (nextnodes2.children != null) {
                                        for (var x = 0; x < nextnodes2.children.length; x++) {
                                            var nextnodes3 = nextnodes2.children[x];
                                            $('#' + nextnodes3.domId + ' .tree-icon').css("background", "url(resources/images/menu_3.png) no-repeat center center");
                                        }
                                    }
                                }
                            }

                        }
                    }
                });
            }
            if (data.length > 0) {
                //找到第一个元素
                var nownodes = $('#myTree').tree('find', data[0].id);
                insfid = nownodes.id;
                //默认选中第一个项目部
                $('#myTree').tree('select', nownodes.target);
                getMachine(insfid);
            }

        },
        //树形菜单点击事件,获取项目部id，默认选择当前组织机构下的第一个
        onClick: function (node) {
            showflag = 0;
            var nownodes = $('#myTree').tree('find', node.id);
            insfid = nownodes.id;
            $("#bodydiv").html("");
            getMachine(insfid);
        }
    });
}

function websocketUrl() {
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
}

//获取焊机，任务及焊工信息
function getMachine(insfid) {
    var url, welderurl;
    if (insfid == "" || insfid == null) {
        url = "td/getLiveMachine";
        welderurl = "td/getLiveWelder";
    } else {
        url = "td/getLiveMachine?parent=" + insfid;
        welderurl = "td/getLiveWelder?parent=" + insfid;
    }
    $.ajax({
        type: "post",
        async: false,
        url: url,
        data: {},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            if (result) {
                machine = eval(result.rows);
                for (var i = 0; i < machine.length; i++) {
                    var type = machine[i].type, imgnum = 0;
                    var manufacture = machine[i].manufacture;
                    if (type == 41) {
                        imgnum = 1;
                    } else if (type == 42) {
                        imgnum = 3;
                    } else if (type == 43) {
                        if (manufacture == 147) {
                            imgnum = 4;
                        } else {
                            imgnum = 2;
                        }
                    }
                    if (offFlag == 0) {
                        off.push(machine[i].fid);
                    }
                    var str = '<div id="machine' + machine[i].fid + '" style="width:240px;height:120px;float:left;margin-right:10px;display:none">' +
                        '<div style="float:left;width:40%;height:100%;"><a href="td/goNextcurve?machineId=' + machine[i].fid + '&valuename=' + machine[i].fequipment_no + '&type=' + machine[i].type + '&model=' + machine[i].model + '&manufacture=' + machine[i].manufacture + '"><img id="img' + machine[i].fid + '" src="resources/images/welder_4' + imgnum + '.png" style="height:110px;width:100%;padding-top:10px;"></a></div>' +
                        '<div style="float:left;width:60%;height:100%;">' +
                        '<ul><li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">设备编号：<span id="m1' + machine[i].fid + '">' + machine[i].fequipment_no + '</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">任务编号：<span id="m2' + machine[i].fid + '">--</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">操作人员：<span id="m3' + machine[i].fid + '">--</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">焊接电流：<span id="m4' + machine[i].fid + '">--A</span></li>' +
                        '<li style="width:100%;height:19px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">焊接电压：<span id="m5' + machine[i].fid + '">--V</span></li>' +
                        '<li style="width:100%;height:19px;">焊机状态：<span id="m6' + machine[i].fid + '">关机</span></li></ul><input id="status' + machine[i].fid + '" type="hidden" value="3"></div></div>';
                    $("#bodydiv").append(str);
                    $("#machine" + machine[i].fid).show();
                }
                showflag = 1;
                $("#off").html(off.length);
                offFlag = 1;
            }
        },
        error: function (errorMsg) {
            alert("数据请求失败，请联系系统管理员!");
        }
    });

    //获取焊工信息
    $.ajax({
        type: "post",
        async: false,
        url: welderurl,
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
    //任务
    $.ajax({
        type: "post",
        async: false,
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
}

var client, clientId;

function mqttTest() {
//	clientId = Math.random().toString().substr(3,8) + Date.now().toString(36);
    clientId = mqttClintId + "_RTC_ONE";
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
    if (redata == null || redata == "" || showflag == 0) {
        for (var i = 0; i < machine.length; i++) {
            $("#machine" + machine[i].fid).show();
        }
        showflag = 1;
    }
    iview();
    if (symbol == 0) {
        clearData();
    }
    symbol++;
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
        //			datatable();
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
        //没有数据时默认显示全部
        if (redata == null || redata == "" && showflag == 0) {
            for (var i = 0; i < machine.length; i++) {
                $("#machine" + machine[i].fid).show();
            }
            showflag = 1;
        }
        iview();
        if (symbol == 0) {
            clearData();
        }
        symbol++;
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
        if (!lockReconnect) {
            window.clearInterval(tt);
        }
        try {
            createWebSocket();
        } catch (e) {
            console.log(e.message);
        }
    }, 10000);
}

function iview() {
    if (redata.length % 111 === 0) {
        for (var i = 0; i < redata.length; i += 111) {
            for (var f = 0; f < machine.length; f++) {
                if (machine[f].fid === (parseInt(redata.substring(4 + i, 8 + i), 10))) {
                    var type = machine[f].type, imgnum = 0;
                    var manufacture = machine[f].manufacture;
                    if (type == 41) {
                        imgnum = 1;
                    } else if (type == 42) {
                        imgnum = 3;
                    } else if (type == 43) {
                        if (manufacture == 147) {
                            imgnum = 4;
                        } else {
                            imgnum = 2;
                        }
                    }
                    var cleardataIndex = $.inArray(parseInt(redata.substring(4 + i, 8 + i), 10), cleardata);
                    if (cleardataIndex == (-1)) {
                        cleardata.push(parseInt(redata.substring(4 + i, 8 + i), 10));
                        cleardata.push(new Date().getTime());
                    } else {
                        cleardata.splice(cleardataIndex + 1, 1, new Date().getTime());
                    }
                    $("#m3" + machine[f].fid).html("--");
                    $("#m2" + machine[f].fid).html("--");
                    for (var k = 0; k < welderName.length; k++) {
                        if (welderName[k].fid == parseInt(redata.substring(0 + i, 4 + i), 10)) {
                            $("#m3" + machine[f].fid).html(welderName[k].fname);
                        }
                    }
                    for (var t = 0; t < taskNum.length; t++) {
                        if (taskNum[t].id == parseInt(redata.substring(12 + i, 16 + i), 10)) {
                            $("#m2" + machine[f].fid).html(taskNum[t].weldedJunctionno);
                        }
                    }
                    var liveele = parseInt(redata.substring(38 + i, 42 + i), 10);
                    var livevol = parseFloat((parseInt(redata.substring(42 + i, 46 + i), 10) / 10).toFixed(2));
                    $("#m4" + machine[f].fid).html(liveele + "A");
                    $("#m5" + machine[f].fid).html(livevol + "V");
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
                    $("#standby").html(stand.length);
                    $("#work").html(on.length);
                    $("#off").html(off.length);
                    $("#warn").html(warn.length);
                    switch (mstatus) {
                        case "00":
                            livestatus = "待机";
                            liveimg = "resources/images/welder_2" + imgnum + ".png";
                            break;
                        case "01":
                            livestatus = "E-010 焊枪开关OFF等待";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "02":
                            livestatus = "E-000工作停止";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "03":
                            livestatus = "工作";
                            liveimg = "resources/images/welder_1" + imgnum + ".png";
                            break;
                        case "04":
                            livestatus = "电流过低";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "05":
                            livestatus = "收弧";
                            liveimg = "resources/images/welder_1" + imgnum + ".png";
                            break;
                        case "06":
                            livestatus = "电流过高";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "07":
                            livestatus = "启弧";
                            liveimg = "resources/images/welder_1" + imgnum + ".png";
                            break;
                        case "08":
                            livestatus = "电压过低";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "09":
                            livestatus = "电压过高";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "10":
                            livestatus = "E-100控制电源异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "15":
                            livestatus = "E-150一次输入电压过高";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "16":
                            livestatus = "E-160一次输入电压过低";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "20":
                            livestatus = "E-200一次二次电流检出异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "21":
                            livestatus = "E-210电压检出异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "22":
                            livestatus = "E-220逆变电路反馈异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "30":
                            livestatus = "E-300温度异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "70":
                            livestatus = "E-700输出过流异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "71":
                            livestatus = "E-710输入缺相异常";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "98":
                            livestatus = "超规范停机";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                        case "99":
                            livestatus = "超规范报警";
                            liveimg = "resources/images/welder_3" + imgnum + ".png";
                            break;
                    }
                    $("#m6" + machine[f].fid).html(livestatus);
                    $("#img" + parseInt(redata.substring(4 + i, 8 + i), 10)).attr("src", liveimg);
                    $("#machine" + parseInt(redata.substring(4 + i, 8 + i), 10)).show();
                }
            }
        }
    }
}

function clearData() {
    window.setInterval(function () {
        timeflag = new Date().getTime();
        for (var i = 0; i < cleardata.length; i = i + 2) {
            if (timeflag - cleardata[i + 1] >= 30000) {
                cleardata.splice(i + 1, 1);
                $("#img" + cleardata[i]).attr("src", "resources/images/welder_42.png");
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
                $("#standby").html(stand.length);
                $("#work").html(on.length);
                $("#off").html(off.length);
                $("#warn").html(warn.length);
                $("#machine" + cleardata[i]).show();
                cleardata.splice(i, 1);
            }
        }
    }, 30000)
}