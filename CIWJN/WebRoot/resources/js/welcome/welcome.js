$(document).ready(function(){
	getMonthWorkTime();
	getMonthTask();
})

var array1 = new Array();
var array2 = new Array();
function getMonthWorkTime(){
	$.ajax({
		type:'post',
		asyn:false,
		url:'companyChart/getMonthWorkTime',
		dataType:'json',
		success:function(result){
			for(var i=0;i<result.rows.length;i++){
            	array1.push(result.rows[i].month);
            	array2.push(result.rows[i].time);
            }
			showChart();
		}
	})
}

var monthcharts;
function showChart(){
	//初始化echart实例
	monthcharts = echarts.init(document.getElementById("monthcharts"));
	//显示加载动画效果
	monthcharts.showLoading({
		text: '稍等片刻,精彩马上呈现...',
		effect:'whirling'
	});
	option = {
		title:{
			text: ""
		},
		tooltip:{
			trigger: 'axis'//坐标轴触发，即是否跟随鼠标集中显示数据
		},
		grid:{
			left:'50',//组件距离容器左边的距离
			right:'4%',
			bottom:'20',
			containLaber:true//区域是否包含坐标轴刻度标签
		},
		xAxis:{
			type:'category',
			data: array1
		},
		yAxis:{
			type: 'value'//value:数值轴，category:类目轴，time:时间轴，log:对数轴
		},
		series:[
			{
				name:'时长(h)',
				type:'bar',
				color : [ '#34b34c' ],
	            barMaxWidth:20,//最大宽度
				data:array2,
				label: {
 		            normal: {
 		                position: 'top',
 		                show: true//显示每个折点的值
 		            }
 		        }
			}
		]
	}
	//为echarts对象加载数据
	monthcharts.setOption(option);
	//隐藏动画加载效果
	monthcharts.hideLoading();
}


var array3 = new Array();
var array4 = new Array();
var array5 = new Array();
var array6 = new Array();
var column = new Array();
function getMonthTask() {
	$.ajax({
		type : 'post',
		asyn : false,
		url : 'companyChart/getMonthTask',
		dataType : 'json',
		success : function(result) {
			var width = $("body").width() / result.rows.length;
			column.push({
				field : "title",
				width : 120,
				halign : "center",
				align : "left"
			});
			for (var i = 0; i < result.ary.length; i++) {
				array3.push(result.ary[i].tasknum);
				array4.push(result.ary[i].taskoknum);
				array5.push(result.ary[i].overrate);
				array6.push(result.ary[i].month);
				column.push({
					field : "a" + i,
					title : result.ary[i].month,
					width : 80,
					halign : "center",
					align : "left"
				});
			}
			$("#taskdg").datagrid({
				fitColumns : true,
				height : '40%',
				width : '90%',
				idField : 'id',
				url : 'companyChart/getMonthTask',
				singleSelect : true,
				columns : [ column ]
			})
			showtaskChart();
		}
	})
}

var taskcharts;
function showtaskChart(){
	//初始化echart实例
	taskcharts = echarts.init(document.getElementById("taskcharts"));
	//显示加载动画效果
	taskcharts.showLoading({
		text: '稍等片刻,精彩马上呈现...',
		effect:'whirling'
	});
	option = {
		title:{
			text: ""
		},
		tooltip:{
			trigger: 'axis'//坐标轴触发，即是否跟随鼠标集中显示数据
		},
		grid:{
			left:'50',//组件距离容器左边的距离
			right:'4%',
			bottom:'40',
			containLaber:true//区域是否包含坐标轴刻度标签
		},
		xAxis:{
			type:'category',
			data: array6
		},
		yAxis:{
			type: 'value'//value:数值轴，category:类目轴，time:时间轴，log:对数轴
		},
		series:[
			{
				name:'任务数',
				type:'bar',
				color : [ '#91ccdc' ],
	            barMaxWidth:20,//最大宽度
				data:array3,
				label: {
 		            normal: {
 		                position: 'top',
 		                show: true//显示每个折点的值
 		            }
 		        }
			},
			{
				name:'完成数',
				type:'bar',
				color : [ '#00b052' ],
	            barMaxWidth:20,//最大宽度
				data:array4,
				label: {
 		            normal: {
 		                position: 'top',
 		                show: true//显示每个折点的值
 		            }
 		        }
			},{
				name:'完成率',
				type:'line',
				color : [ '#cf5c0b' ],
	            barMaxWidth:20,//最大宽度
				data:array5,
				itemStyle : {
	      			normal: {
	      				label : {
	      					show: true,//显示每个折点的值
	      					formatter: '{c}%'  
	      				}
	      			}
	      		}
			}
		]
	}
	//为echarts对象加载数据
	taskcharts.setOption(option);
	//隐藏动画加载效果
	taskcharts.hideLoading();
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#taskdg").datagrid('resize', {
		height : '40%',
		width : '90%'
	});
	monthcharts.resize();
	taskcharts.resize();
	weldercharts.resize();
	personcharts.resize();
}