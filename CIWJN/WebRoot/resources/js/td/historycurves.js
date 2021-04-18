$(function () {
    Junction();
    $("#little").hide();
    $("#body1").height($("#elebody").height() - 30);
});
var chartStr = "";

function setParam() {
    var parent = $("#parent").val();
    var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
    var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
    chartStr = "?parent=" + parent + "&dtoTime1=" + dtoTime1 + "&dtoTime2=" + dtoTime2;
}

var time1 = new Array();
var vol = new Array();
var ele = new Array();
var machineList = new Array();
var junctionList = new Array();
var welderList = new Array();

function Junction() {
    setParam();
    //datagridTable();
    loadAllInfo();
    loadDataForES();
}

function loadAllInfo() {
    $.ajax({
        url: 'weldedjunction/getAllInfoNoPage',
        type: 'post',
        dataType: 'json',
        async: false,
        data: {},
        success: function (result) {
            if (result) {
                machineList = result.machineList;
                junctionList = result.junctionList;
                welderList = result.welderList;
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function loadDataForES() {
    var qeryTerm = "{}";
    if ($("#machineId").val() != null && $("#machineId").val() !== '') {
        qeryTerm = "{'term': {'fmachine_id': " + $('#machineId').val() + "}}";
    } else if ($("#welderid").val() != null && $("#welderid").val() !== '') {
        qeryTerm = "{'term': {'fwelder_no': " + $('#welderid').val() + "}}";
    } else if ($("#wjno").val() != null && $("#wjno").val() !== '') {
        qeryTerm = "{'term': {'fjunction_no': " + $('#wjno').val() + "}}";
    }
    var query = {
        "query": {
            "bool": {
                "must": [
                    qeryTerm,
                    {
                        "term": {
                            "fstatus": 3
                        }
                    }
                ],
                "must_not": [
                    {
                        "term": {
                            "fjunction_id": 0
                        }
                    }

                ]
            }
        },
        "size": 0,
        "aggs": {
            "data": {
                "filter": {
                    "range": {
                        "FWeldTime": {
                            "gte": new Date($("#dtoTime1").datetimebox('getValue')).toISOString(),
                            "lte": new Date($("#dtoTime2").datetimebox('getValue')).toISOString()
                        }
                    }
                },
                "aggs": {
                    "data": {
                        "terms": {
                            "field": "fjunction_id",
                            "order": {
                                "startTime": "asc"
                            }
                        },
                        "aggs": {
                            "startTime": {
                                "min": {
                                    "field": "FWeldTime"
                                }
                            },
                            "endTime": {
                                "max": {
                                    "field": "FWeldTime"
                                }
                            },
                            "fwelderId": {
                                "max": {
                                    "field": "fwelder_id"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    $("#dg").datagrid({
        fitColumns: true,
        height: $("body").height() / 2,
        width: $("body").width(),
        idField: 'id',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "",
        singleSelect: true,
        rownumbers: true,
        showPageList: false,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'key',
            title: '任务编号',
            width: 100,
            halign: "center",
            align: "left",
            formatter: function (value) {
                if (junctionList.length > 0) {
                    for (var jun in junctionList) {
                        if (value == junctionList[jun].id) {
                            return junctionList[jun].weldedJunctionno;
                        }
                    }
                } else {
                    return "";
                }
            }
        }, {
            field: 'fmachine_id',
            title: '焊机编号',
            width: 100,
            halign: "center",
            align: "left",
            formatter: function (value) {
                if (machineList.length > 0) {
                    for (var index in machineList) {
                        if ($("#machineId").val() == machineList[index].id) {
                            return machineList[index].equipmentNo;
                        }
                    }
                }
            }
        }, {
            field: 'fwelderId',
            title: '焊工姓名',
            width: 100,
            halign: "center",
            align: "left",
            formatter: function (value) {
                if (welderList.length > 0) {
                    for (var welder in welderList) {
                        if (value.value == welderList[welder].id) {
                            return welderList[welder].name;
                        }
                    }
                } else {
                    return "";
                }
            }
        }, {
            field: 'startTime',
            title: '开始时间',
            width: 150,
            halign: "center",
            align: "left",
            formatter: function (value) {
                return formatDate(new Date(value.value_as_string));
            }
        }, {
            field: 'endTime',
            title: '终止时间',
            width: 150,
            halign: "center",
            align: "left",
            formatter: function (value) {
                return formatDate(new Date(value.value_as_string));
            }
        }, {
            field: 'doc_count',
            title: '焊接时间(h)',
            width: 150,
            halign: "center",
            align: "left",
            formatter: function (value) {
                return (value / 3600).toFixed(3);
            }
        }]],
        pagination: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onClickRow: function (index, row) {
            loadChart(row);
        },
        onSelect: function (index, row) {
            loadChart(row);
        }
    });
    $.ajax({
        url: 'http://localhost:9200/realtimedata/_search?pretty=true',
        type: 'post',
        contentType: 'application/json',
        crossDomain: true,
        async: false,
        data: JSON.stringify(query),
        dataType: 'json',
        processData: false,
        success: function (json, text, xhr) {
            var list = json.aggregations.data.data.buckets;
            $("#dg").datagrid("loadData", list);
        },
        error: function (xhr, message, error) {
            console.log(message);
            throw (error);
        }
    });
}

function formatDate(date) {
    var datestr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" +
        date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" +
        date.getSeconds();
    return datestr;
}

function datagridTable() {
    $("#dg").datagrid({
        fitColumns: true,
        height: $("body").height() / 2,
        width: $("body").width(),
        idField: 'id',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "weldedjunction/getWeldingJun" + chartStr + "&wjno=" + $("#wjno").val() + "&welderid=" + $("#welderid").val() + "&machineId=" + $("#machineId").val(),
        singleSelect: true,
        rownumbers: true,
        showPageList: false,
        columns: [[{
            field: 'ck',
            checkbox: true
        }, {
            field: 'key',
            title: '任务编号',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'machine_num',
            title: '焊机编号',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'welderName',
            title: '焊工姓名',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'startTime.value_as_string',
            title: '开始时间',
            width: 150,
            halign: "center",
            align: "left"
        }, {
            field: 'endTime',
            title: '终止时间',
            width: 150,
            halign: "center",
            align: "left"
        }, {
            field: 'doc_count',
            title: '焊接时间(h)',
            width: 150,
            halign: "center",
            align: "left"
        }, {
            field: 'machid',
            title: '焊机id',
            halign: "center",
            align: "left",
            hidden: true
        }]],
        pagination: true,
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onClickRow: function (index, row) {
            loadChart(row);
        },
        onSelect: function (index, row) {
            loadChart(row);
        }
    });
}

function loadChart(row) {
    time1 = new Array();
    vol = new Array();
    ele = new Array();
    document.getElementById("load").style.display = "block";
    var sh = '<div id="show" style="width:150px;" align="center"><img src="resources/images/load1.gif"/>数据加载中，请稍候...</div>';
    $("#bodys").append(sh);
    document.getElementById("show").style.display = "block";
    //chartStr = "";
    setParam();
    //loadHistoryCurveData(row);
    // alert("任务编号：" + row.key);
    // alert("焊机id：" + $("#machineId").val());
    // alert("焊工编号：" + row.fwelderId.value);
    var query = {
        "query": {
            "bool": {
                "must": [
                    {"match": {"fmachine_id": $("#machineId").val()}}
                ],
                "filter": {
                    "range": {
                        "FWeldTime": {
                            "gte": new Date(row.startTime.value_as_string).toISOString(),
                            "lte": new Date(row.endTime.value_as_string).toISOString()
                        }
                    }
                }
            }
        },
        "from": 0,
        "size": 5000,
        "sort": [
            {
                "FWeldTime": {
                    "order": "asc"
                }
            }
        ]
    }
    $.ajax({
        url: 'http://localhost:9200/realtimedata/_search?pretty=true',
        type: 'post',
        contentType: 'application/json',
        crossDomain: true,
        async: false,
        data: JSON.stringify(query),
        dataType: 'json',
        processData: false,
        success: function (json, text, xhr) {
            var result = json.hits.hits;
            for (var i in result) {
                ele.push(result[i]._source.felectricity);
                vol.push(result[i]._source.fvoltage);
                time1[i] = utc2beijing(result[i]._source.FWeldTime);
            }
            eleChart();
            volChart();
            document.getElementById("load").style.display = 'none';
            document.getElementById("show").style.display = 'none';
        },
        error: function (xhr, message, error) {
            console.log(message);
            throw (error);
        }
    });
}

function loadHistoryCurveData(row) {
    $.ajax({
        type: "post",
        url: "rep/historyCurve" + chartStr + "&fid=" + row.key + "&mach=" + $("#machineId").val() + "&welderid=" + row.fwelderId.value,
        dataType: "json",
        data: {},
        success: function (result) {
            if (result) {
                var date = eval(result.rows);
                if (date.length === 0) {
                    document.getElementById("load").style.display = 'none';
                    document.getElementById("show").style.display = 'none';
                    alert("该时间内未查询到相关数据");
                } else {
                    for (var i = 0; i < date.length; i++) {
                        ele.push(date[i].ele);
                        vol.push(date[i].vol);
                        time1[i] = date[i].time;
                    }
                    eleChart();
                    volChart();
                    document.getElementById("load").style.display = 'none';
                    document.getElementById("show").style.display = 'none';
                }
            }
        },
        error: function (e) {
            alert('error' + e.code);
        }
    });
}

function utc2beijing(utc_datetime) {
    // 转为正常的时间格式 年-月-日 时:分:秒
    var T_pos = utc_datetime.indexOf('T');
    var Z_pos = utc_datetime.indexOf('Z');
    var year_month_day = utc_datetime.substr(0, T_pos);
    var hour_minute_second = utc_datetime.substr(T_pos + 1, Z_pos - T_pos - 1);
    var new_datetime = year_month_day + " " + hour_minute_second; // 2017-03-31 08:02:06

    // 处理成为时间戳
    timestamp = new Date(Date.parse(new_datetime));
    timestamp = timestamp.getTime();
    timestamp = timestamp / 1000;

    // 增加8个小时，北京时间比utc时间多八个时区
    var timestamp = timestamp + 8 * 60 * 60;

    // 时间戳转为时间
    var beijing_datetime = new Date(parseInt(timestamp) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
    return beijing_datetime; // 2017-03-31 16:02:06
}

function eleChart() {
    var myChart = echarts.init(document.getElementById('body1'));
    var option = {
        backgroundColor: '#fff',
        title: {
            text: '电流'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: false
                },
                dataView: {
                    show: false,
                    readOnly: false
                },
                restore: {
                    show: false
                }
            }
        },
        dataZoom: [
            {
                type: 'slider',
                show: true,
                xAxisIndex: [0]
            },
            {
                type: 'inside',
                xAxisIndex: [0]
            }
        ],
        grid: {
            left: '8%',//组件距离容器左边的距离
            right: '5%',
            top: "5%",
            bottom: 60
        },
        xAxis: [{
            type: 'category',
            data: time1
        }],
        yAxis: [{
            type: 'value',
            max: 500,
            min: 0
        }],
        series: [{
            symbolSize: 5,//气泡大小
            name: '电流',
            type: 'line',//折线图
            data: ele,
            itemStyle: {
                normal: {
                    color: "#A020F0",
                    lineStyle: {
                        color: "#A020F0"
                    }
                }
            }
        }]
    };
    myChart.setOption(option);
}


function volChart() {
    var myChart = echarts.init(document.getElementById('body2'));
    var option = {
        backgroundColor: '#fff',
        title: {
            text: '电压'
        },
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: false
                },
                dataView: {
                    show: false,
                    readOnly: false
                },
                restore: {
                    show: false
                }
            }
        },
        dataZoom: [//缩放
            {
                type: 'slider',
                show: true,
                xAxisIndex: [0]
            },
            {
                type: 'inside',
                xAxisIndex: [0]
            }
        ],
        grid: {
            left: '8%',//组件距离容器左边的距离
            right: '5%',
            top: "5%",
            bottom: 60,
            y2: $("#body2").height()//图表高度
        },
        xAxis: [{
            type: 'category',
            data: time1
        }],
        yAxis: [{
            type: 'value',
            max: 60,
            min: 0
        }],
        series: [{
            symbolSize: 5,//气泡大小
            name: '电压',
            type: 'line',//折线图
            data: vol,
            itemStyle: {
                normal: {
                    color: "#87CEFA",
                    lineStyle: {
                        color: "#87CEFA"
                    }
                }
            }
        }]
    };
    myChart.setOption(option);
}

function serachCompanyOverproof() {
    Junction();
}

function fullScreen() {
    var row = $("#dg").datagrid('getSelected');
    if (row == null) {
        alert("请先选择焊口");
    } else {
        $("#elebody").height('50%');
        $("#elebody").css({'top': '0px'});
        $("#body1").height($("#elebody").height() - 23);
        $("#body2").height('50%');
        $("#body2").css({'top': '50%'});
        echarts.init(document.getElementById('body1')).resize();
        echarts.init(document.getElementById('body2')).resize();
        $("#full").hide();
        $("#little").show();
    }
}

function theSmallScreen() {
    $("#elebody").height('25%');
    $("#elebody").css({'top': '58%'});
    $("#body1").height($("#elebody").height() - 23);
    $("#body2").height('20%');
    $("#body2").css({'top': '82%'});
    echarts.init(document.getElementById('body1')).resize();
    echarts.init(document.getElementById('body2')).resize();
    $("#full").show();
    $("#little").hide();
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#dg").datagrid('resize', {
        height: $("#dgtb").height() / 2,
        width: $("#dgtb").width()
    });
    if ($("#full").is(":hidden")) {//全屏模式
        $("#elebody").height('50%');
        $("#elebody").css({'top': '0px'});
        $("#body1").height($("#elebody").height() - 23);
        $("#body2").height('50%');
        $("#body2").css({'top': '50%'});
    } else {
        $("#elebody").height('25%');
        $("#elebody").css({'top': '58%'});
        $("#body1").height($("#elebody").height() - 23);
        $("#body2").height('20%');
        $("#body2").css({'top': '82%'});
    }
    echarts.init(document.getElementById('body1')).resize();
    echarts.init(document.getElementById('body2')).resize();
}