/**
 * 
 */
var z=20;
var zz=20;
var maxele;
var minele;
var maxvol;
var minvol;
var time1 = new Array();
var vol = new Array();
var ele = new Array();
var timer1;
var timer2;
var chart,chart1;
var series,series1;
var lable=0;
		$(function(){
			weldingMachineDatagrid();
		})
		
		var chartStr = "";
		function setParam(){
			var parent = $("#parent").val();
			var otype = $("input[name='otype']:checked").val();
			var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
			var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
			chartStr = "?otype="+otype+"&parent="+parent+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2;
		}
		function serachCompanyOverproof(){
			$('#body1').html("");
			$('#body2').html("");
			chartStr = "";
			var rows = $("#dg").datagrid("getSelections");
			if(rows.length==0){
				alert("请先选择任务");
			}else{
				setParam();
				var fid = $('#dg').datagrid('getSelected').weldedJunctionno;
				   $.ajax({
					   type: "post", 
					   url: "rep/getWpsByMid"+"?fid="+fid,
					   dataType: "json",
					   data: {},
					   success: function (result) {
					      if (result) {
					    	  var wps = eval(result.rows);
					    	  maxele=wps[0].maxele;
					    	  minele=wps[0].minele;
					    	  maxvol=wps[0].maxvol;
					    	  minvol=wps[0].minvol;
					      }
					   },
					   error: function () {
					      alert('error');
					   }
					});

				   $.ajax({
					   type: "post", 
					   url: "rep/historyCurve"+chartStr+"&fid="+fid,
					   dataType: "json",
					   data: {},
					   success: function (result) {
					      if (result) {
					    	  var date = eval(result.rows);
					    	  for(var i=0;i<date.length;i++){
					    		  ele[i] = date[i].ele;
					    		  vol[i] = date[i].vol;
					    		  time1[i] = Date.parse(date[i].time);
					    	  }

								curve();
								curve1();
					      }
					   },
					   error: function () {
					      alert('error');
					   }
					});
			}
		}
		
		function weldingMachineDatagrid(){
			$("#dg").datagrid( {

				fitColumns : true,				
				height : $("#dgtb").height(),
				width : $("#dgtb").width(),
				idField : 'id',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				url : "weldedjunction/getWeldingJun",
				singleSelect : true,
				rownumbers : true,
				showPageList : false,
				columns : [ [  {
				    field:'ck',
					checkbox:true
				}, {
					field : 'id',
					title : '序号',
					width : 30,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'weldedJunctionno',
					title : '编号',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'serialNo',
					title : '序列号',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'pipelineNo',
					title : '管线号',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'roomNo',
					title : '房间号',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'unit',
					title : '机组',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'area',
					title : '区域',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'systems',
					title : '系统',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'children',
					title : '子项',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'externalDiameter',
					title : '上游外径',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'nextexternaldiameter',
					title : '下游外径',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'wallThickness',
					title : '上游壁厚',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'nextwall_thickness',
					title : '下游璧厚',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'material',
					title : '上游材质',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'next_material',
					title : '下游材质',
					width : 90,
					halign : "center",
					align : "left"
				}, /*{
					field : 'dyne',
					title : '达因',
					width : 90,
					halign : "center",
					align : "left"
				},*/ {
					field : 'specification',
					title : '规格',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'maxElectricity',
					title : '电流上限',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'minElectricity',
					title : '电流下限',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'maxValtage',
					title : '电压上限',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'minValtage',
					title : '电压下限',
					width : 90,
					halign : "center",
					align : "left"
				}, {
					field : 'itemname',
					title : '所属项目',
					width : 150,
					halign : "center",
					align : "left"
				}, {
					field : 'startTime',
					title : '开始时间',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'endTime',
					title : '完成时间',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'creatTime',
					title : '创建时间',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'updateTime',
					title : '修改时间',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'updatecount',
					title : '修改次数',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'valtage_unit',
					title : '电压单位',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'electricity_unit',
					title : '电流单位',
					width : 90,
					halign : "center",
					align : "left",
					hidden:true
//				}, {
//					field : 'edit',
//					title : '编辑',
//					width : 120,
//					halign : "center",
//					align : "left",
//					formatter: function(value,row,index){
//						var rows = row.weldedJunctionno+","+row.serialNo+","+row.pipelineNo+","+row.roomNo+","+row.unit
//						+","+row.area+","+row.systems+","+row.children+","+row.externalDiameter+","+row.wallThickness+","+row.dyne+","+row.specification+","+row.maxElectricity+","+
//						row.minElectricity+","+row.maxValtage+","+row.minValtage+","+row.material+","+row.nextexternaldiameter+","+row.itemname+","+row.startTime+","+row.endTime+
//						","+row.creatTime+","+row.updateTime+","+row.updatecount+","+row.nextwall_thickness+","+row.next_material+","+row.valtage_unit+","+row.electricity_unit;
//						var str = '<a id="look" class="easyui-linkbutton" href="weldedjunction/goShowMoreJunction?rows='+rows+'"/>';
//						return str;
//					}
				}] ],
				toolbar : '#disctionaryTable_btn',
				pagination : true,
//				onLoadSuccess: function(data){
//					$("a[id='look']").linkbutton({text:'查看更多',plain:true,iconCls:'icon-add'});
//				}
			});
		}
	var time=1000;
	function refresh1(){
		timer1 = window.setInterval(function () {
              /*var x = (new Date()).getTime()+t,*/ // current time
            var x = time1[z],
                  y = ele[z];
              z++;
              series.addPoint([x, y], true, false);
              activeLastPointToolip1(chart);
      		if(z==ele.length){
    			window.clearInterval(timer1);
    		}
          }, time);
	}
	
	function refresh2(){
		timer2=window.setInterval(function () {
              /*var x = (new Date()).getTime()+t,*/ // current time
             var x = time1[zz],
                  y = vol[zz];
              zz++;
              series1.addPoint([x, y], true, false);
              activeLastPointToolip2(chart);
      		if(zz==vol.length){
    			window.clearInterval(timer2);
    		}
          }, time);
	}
	
	//加速
	function addtime(){
		time -= 500;
		if(time<=100){
			time = 100;
		}
		window.clearInterval(timer1);
		window.clearInterval(timer2);
		refresh1();
		refresh2();
	}

	//减速
	function reducetime(){
		time += 500;
		if(time>=10000){
			time = 10000;
		}
		window.clearInterval(timer1);
		window.clearInterval(timer2);
		refresh1();
		refresh2();
	}
	
	//暂停
	function stoptime(){
		if(lable!=1){
		window.clearInterval(timer1);
		window.clearInterval(timer2);
		lable++;
		}else{
			alert("已暂停");
		}
	}
	
	//开始
	function starttime(){
		if(lable==1){
		refresh1();
		refresh2();
		lable--;
		}else{
			alert("正在播放");
		}
	}
		
	function activeLastPointToolip1(chart) {
		    var points = chart.series[0].points;
/*  		    chart.tooltip.refresh(points[points.length -1]);
		    chart.tooltip.refresh(points1[points1.length -1]);*/
//		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
//		  		value:maxele, //在值为2的地方 
//		  		width:2, //标示线的宽度为2px 
//		  		color: 'red', //标示线的颜色 
//		  	    dashStyle:'longdashdot',
//		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
//	          label:{
//		            text:'最高电流',     //标签的内容
//		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
//		            x:10                         //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
//		        }
//		  	})
//		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
//		  		value:minele, //在值为2的地方 
//		  		width:2, //标示线的宽度为2px 
//		  		color: 'red', //标示线的颜色 
//		  	    dashStyle:'longdashdot',
//		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
//	          label:{
//		            text:'最低电流',     //标签的内容
//		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
//		            x:10                     //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
//		        }
//		  	})	  	
		}
		function activeLastPointToolip2(chart) {
  		    var points = chart.series[0].points;
/*  		    chart.tooltip.refresh(points[points.length -1]);
  		    chart.tooltip.refresh(points1[points1.length -1]);*/
//  		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
//  		  		value:maxvol, //在值为2的地方 
//  		  		width:2, //标示线的宽度为2px 
//  		  		color: 'black', //标示线的颜色 
//  		  	    dashStyle:'longdashdot',
//  		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
//		          label:{
//    		            text:'最高电压',     //标签的内容
//    		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
//    		            x:10  
//    		        }
//  		  	})
//  		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
//  		  		value:minvol, //在值为2的地方 
//  		  		width:2, //标示线的宽度为2px 
//  		  		color: 'black', //标示线的颜色 
//  		  	    dashStyle:'longdashdot',
//  		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
//		          label:{
//    		            text:'最低电压',     //标签的内容
//    		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
//    		            x:10                         //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
//    		        }
//  		  	})
  		  	  		  	
  		}
		
	function curve(){
  		Highcharts.setOptions({
  		    global: {
  		        useUTC: false
  		    },
  	        lang: {
  	            resetZoom: '重置',
  	            resetZoomTitle: '重置缩放比例'
  	        }
  		});
  		function activeLastPointToolip1(chart) {
		    var points = chart.series[0].points;
/*  		    chart.tooltip.refresh(points[points.length -1]);
		    chart.tooltip.refresh(points1[points1.length -1]);*/
		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
		  		value:maxele, //在值为2的地方 
		  		width:2, //标示线的宽度为2px 
		  		color: 'red', //标示线的颜色 
		  	    dashStyle:'longdashdot',
		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
	          label:{
		            text:'最高电流',     //标签的内容
		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
		            x:10                         //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
		        }
		  	})
		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
		  		value:minele, //在值为2的地方 
		  		width:2, //标示线的宽度为2px 
		  		color: 'red', //标示线的颜色 
		  	    dashStyle:'longdashdot',
		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
	          label:{
		            text:'最低电流',     //标签的内容
		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
		            x:10                     //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
		        }
		  	})	  	
		}
  		$('#body1').highcharts({
  		    chart: {
  	            panning: true,
  	            panKey: 'ctrl',
  		    	zoomType: 'x',
  		        type: 'spline',
  		        animation: false, // don't animate in old IE
  		        marginRight: 70,
  		        events: {
  		            load: function () {
  		                // set up the updating of the chart each second
  		                	series = this.series[0];
  		                    chart = this;
  		                    refresh1();
//  		                	window.setInterval(function () {
//  		                    /*var x = (new Date()).getTime()+t,*/ // current time
//  		                  var x = time1[z],
//  		                        y = ele[z];
//  		                    z++;
//  		                    series.addPoint([x, y], true, true);
//  		                    activeLastPointToolip(chart);
//  		                }, 1000);
  		            }
  		        }
  		    },
  		    title: {
  		        text: '电压电流实时监测'
  		    },
            mapNavigation: {
                enabled: true,
                enableButtons: false
            },
  		    xAxis: {
  		        type: 'datetime',
  		        tickPixelInterval: 150,
  		        lineColor:'#FFFFFF',
  		        tickWidth:0,
	  		    labels:{
	  		    	enabled:false
	  		    }
  		    },
  		    yAxis: [{
                max:800, // 定义Y轴 最大值  
                min:0, // 定义最小值  
                minPadding: 0.2,   
                maxPadding: 0.2,  
                tickInterval:40,
                color:'#A020F0',
  		        title: {
  		            text: '电流',
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
  		        },
  		    },
  		    legend: {
  		        enabled: false
  		    },
  		    exporting: {
  		        enabled: false
  		    },
  		    credits:{
  		      enabled:false // 禁用版权信息
  		    },
  		    series: [{
  		    	color:'#A020F0',
  		        name: '电流',
  		        
  		        data: (function () {
  		            // generate an array of random data
  		            var data = [],
  		                time = (new Date()).getTime(),
  		                /*time = new Date(Date.parse("0000-00-00 00:00:00")),*/
  		                i;
  		            for (i = -19; i <= 0; i += 1) {
  		                data.push({
  		                    x: time1[i+19],
  		                	/*x: time + i*1000,*/
  		                    y: ele[i+19]
  		                });
  		            }
  		            return data;
  		        }())
  		    }]
  		}, function(c) {
  		    activeLastPointToolip1(c)
  		});
	}
	
	function curve1(){
  		Highcharts.setOptions({
  		    global: {
  		        useUTC: false
  		    },
        lang: {
            resetZoom: '重置',
            resetZoomTitle: '重置缩放比例'
        },
        panning: true,
        panKey: 'shift'
  		});
  		
		function activeLastPointToolip2(chart) {
  		    var points = chart.series[0].points;
/*  		    chart.tooltip.refresh(points[points.length -1]);
  		    chart.tooltip.refresh(points1[points1.length -1]);*/
  		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
  		  		value:maxvol, //在值为2的地方 
  		  		width:2, //标示线的宽度为2px 
  		  		color: 'black', //标示线的颜色 
  		  	    dashStyle:'longdashdot',
  		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
		          label:{
    		            text:'最高电压',     //标签的内容
    		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
    		            x:10  
    		        }
  		  	})
  		  	chart.yAxis[0].addPlotLine({ //在y轴上增加 
  		  		value:minvol, //在值为2的地方 
  		  		width:2, //标示线的宽度为2px 
  		  		color: 'black', //标示线的颜色 
  		  	    dashStyle:'longdashdot',
  		  		id: 'plot-line-1', //标示线的id，在删除该标示线的时候需要该id标示 });
		          label:{
    		            text:'最低电压',     //标签的内容
    		            align:'center',                //标签的水平位置，水平居左,默认是水平居中center
    		            x:10                         //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
    		        }
  		  	})
  		  	  		  	
  		}
  		$('#body2').highcharts({
  		    chart: {
  	            panning: true,
  	            panKey: 'ctrl',
  		    	zoomType: 'x',
  		        type: 'spline',
  		        animation: false, // don't animate in old IE
  		        marginRight: 70,
  		        events: {
  		            load: function () {
  		                // set up the updating of the chart each second
  		            		series1 = this.series[0];
  		                    chart1 = this;
  		                    refresh2();
//  		                timer5=window.setInterval(function () {
//  		                    /*var x = (new Date()).getTime()+t,*/ // current time
//  		                   var x = time1[zz],
//  		                        y = vol[zz];
//  		                    zz++;
//  		                    series.addPoint([x, y], true, true);
//  		                    activeLastPointToolip2(chart);
//  		                }, 1000);
  		            }
  		        }
  		    },
  		    title: {
  		        text: false
  		    },
            mapNavigation: {
                enabled: true,
                enableButtons: false
            },
  		    xAxis: {
  		        type: 'datetime',
  		        tickPixelInterval: 150
  		    },
  		    yAxis: [{
                max:105, // 定义Y轴 最大值  
                min:0, // 定义最小值  
                minPadding: 0.2,   
                maxPadding: 0.2,  
                tickInterval:15,
                color:'#87CEFA',
  		    	title: {
  		            text: '电压',
  	                style: {  
  	                    color: '#87CEFA'  
  	                }  
  		        },
  		    }],
  		    tooltip: {
  		        formatter: function () {
  		            return '<b>' + this.series.name + '</b><br/>' +
  		                Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
  		                Highcharts.numberFormat(this.y, 2);
  		        },
  		    },
  		    legend: {
  		        enabled: false
  		    },
  		    exporting: {
  		        enabled: false
  		    },
  		    credits:{
  		      enabled:false // 禁用版权信息
  		    },
  		    series: [{
  		        name: '电压',
  		        data: (function () {
  		            // generate an array of random data
  		            var data = [],
  		                time = (new Date()).getTime(),
  		                i;
  		            for (i = -19; i <= 0; i += 1) {
  		                data.push({
  		                    x: time1[i+19],
  		                    /*x: time + i*1000,*/
  		                    y: vol[i+19]
  		                });
  		            }
  		            return data;
  		        }()),
  		      
  		    }]
  		}, function(c) {
  		    activeLastPointToolip2(c)
  		});
	}
	
    //监听窗口大小变化
    window.onresize = function() {
    	setTimeout(domresize, 500);
    }

    //改变表格高宽
    function domresize() {
    	$("#dg").datagrid('resize', {
    		height : $("#body").height(),
    		width : $("#body").width()
    	});
    }