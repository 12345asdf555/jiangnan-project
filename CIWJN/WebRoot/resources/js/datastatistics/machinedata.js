$(function(){
	dgDatagrid();
	itemcombobox();
})

var chartStr = "";
function setParam(){
	var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
	var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
	var zitem = $("#zitem").combobox('getValue');
	var bitem = $("#bitem").combobox('getValue');
	var item = "";
	if(zitem!=0){
		item = zitem;
	}
	if(bitem!=0){
		item = bitem;
	}
	chartStr += "?item="+item+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2;
}

function dgDatagrid(){
	setParam();
	var column = new Array();
	$('#dg').datagrid({
		 fitColumns : true,
		 height : $("#body").height(),
		 width : $("#body").width(),
		 url : "datastatistics/getMachineData"+chartStr,
		 pageSize : 10,
		 pageList : [ 10, 20, 30, 40, 50 ],
		 singleSelect : true,
		 rownumbers : true,
		 showPageList : false,
		 pagination : true,
		 remoteSort : false,
		 nowrap : false,
		 columns : [[
			 {
				field : 't1',
				title : '设备编号',
				width : 100,
				halign : "center",
				align : "left",
				sortable : true,
				sorter : function(a,b){
					return (a>b?1:-1);
				}
			 }, {
				field : 't0',
				title : '班组',
				width : 100,
				halign : "center",
				align : "left",
				sortable : true,
				sorter : function(a,b){
					return (a>b?1:-1);
				}
			 }, {
				field : 't2',
				title : '焊接任务数',
				width : 100,
				halign : "center",
				align : "left",
				sortable : true,
				sorter : function(a,b){
					return (a>b?1:-1);
				}
			 }, {
				field : 't3',
				title : '焊接时间',
				width : 100,
				halign : "center",
				align : "left",
				sortable : true,
				sorter : function(a,b){
					return (a>b?1:-1);
				}
			 }, {
				field : 't4',
				title : '工作时间',
				width : 100,
				halign : "center",
				align : "left",
				sortable : true,
				sorter : function(a,b){
					return (a>b?1:-1);
				}
			 }, {
				field : 't5',
				title : '焊接效率',
				width : 100,
				halign : "center",
				align : "left",
				sortable : true,
				sorter : function(a,b){
					return (a>b?1:-1);
				}
			 } 
		 ]],
		 rowStyler: function(index,row){
			 if ((index % 2)!=0){
           	 //处理行代背景色后无法选中
           	 var color=new Object();
                return color;
            }
        },
        onBeforeLoad : function(param){
    		$("#chartLoading").hide();
        }
}); 
	return;
//	$.ajax({  
//        type : "post",  
//        async : false,
//        url : "datastatistics/getMachineData"+chartStr,
//        data : {},  
//        dataType : "json", //返回数据形式为json  
//        success : function(result) {
//            if (result) {
//            	 for(var i=0;i<result.ary.length;i++){
//            		 column.push({field:"t"+i,title:result.ary[i].title,width:100,halign : "center",align : "left",sortable : true,
//            				sorter : function(a,b){
//            					return (a>b?1:-1);
//            				}});
//             	 }
//            	 var grid = {
//            			 fitColumns : true,
//        				 height : $("#body").height(),
//        				 width : $("#body").width(),
//        				 url : "datastatistics/getMachineData"+chartStr,
//        				 pageSize : 10,
//        				 pageList : [ 10, 20, 30, 40, 50 ],
//        				 singleSelect : true,
//        				 rownumbers : true,
//        				 showPageList : false,
//        				 pagination : true,
//        				 remoteSort : false,
//        				 nowrap : false,
//        				 columns : [column],
//        				 rowStyler: function(index,row){
//        					 if ((index % 2)!=0){
//        		            	 //处理行代背景色后无法选中
//        		            	 var color=new Object();
//        		                 return color;
//        		             }
//        		         },
//        		         onBeforeLoad : function(param){
//     		        		$("#chartLoading").hide();
//        		         }
//                 };
//            	 $('#dg').datagrid(grid);  
//            	 $('#dg').datagrid('loadData', result.rows);
//            }  
//        },  
//        error : function(errorMsg) {  
//            alert("数据请求失败，请联系系统管理员!");  
//        }  
//   }); 
}

function itemcombobox(){
/*	$.ajax({  
      type : "post",  
      async : false,
      url : "datastatistics/getAllInsframework",  
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {  
          if (result) {
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
	$("#item").combobox();*/
	$("#zitem").combobox({
		onChange : function(newValue,oldValue){
			if(oldValue!=""){
				$.ajax({  
				    type : "post",  
				    async : false,
				    url : "weldtask/getTeam?searchStr="+" and i.fparent="+newValue,  
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
					        	$("#bitem").combobox('select',0);
					        	$("#bitem").combobox({disabled: false});
				        }  
				    },  
				    error : function(errorMsg) {  
				        alert("数据请求失败，请联系系统管理员!");  
				    }  
					}); 
			}
		}
	})

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
//		        	$("#zitem").combobox({disabled: true});
	        	}else{
	        		$("#bitem").combobox({disabled: true});
	        		var zoptionStr = '<option value="0">请选择</option>';
	                for (var i = 0; i < result.ary.length; i++) {  
	                    zoptionStr += "<option value=\"" + result.ary[i].id + "\" >"  
	                            + result.ary[i].name + "</option>";
	                }
	                $("#zitem").html(zoptionStr);
		        	$("#zitem").combobox();
		        	$("#zitem").combobox('select',result.ary[i].id);
	        	}
	        	
	        }  
	    },  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    }  
		}); 
	
	
	
}

function serach(){
	$("#chartLoading").show();
	chartStr = "";
	setTimeout(function(){
		dgDatagrid();
	},500);
}

//导出到Excel
function exportExcel(){
	chartStr = "";
	setParam();
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exportMachineData";
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = encodeURI(url+chartStr);
		}
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