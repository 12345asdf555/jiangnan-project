var url = "";
function removeWeldedjunction(){
	$('#rfm').form('clear');
	var row = $('#weldTaskTable').datagrid('getSelected');
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : "weldedjunction/getCouneByTaskid?taskid="+row.id+"&type="+"",  
	      data : {},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {  
	          if (result==false) {
	        	  alert("任务已被执行或者已完成，无法进行操作！！");
	          }else{
	        		if (row) {
	        			$('#rdlg').window( {
	        				title : "删除任务",
	        				modal : true
	        			});
	        			$('#rdlg').window('open');
	        			$('#rfm').form('load', row);
	        			url = "weldtask/removeWeldTask?id="+row.id+"&insfid="+row.itemid;
	        		}
	          }
	      },  
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	}); 
}

function remove(){
	$.messager.confirm('提示', '此操作不可撤销，是否确认删除?', function(flag) {
		if (flag) {
			document.getElementById("load").style.display="block";
			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
			$("#body").append(sh);
			document.getElementById("show").style.display="block";
			$.ajax({  
		        type : "post",  
		        async : false,
		        url : url,  
		        data : {},  
		        dataType : "json", //返回数据形式为json  
		        success : function(result) {
		            if (result) {
		            	if (!result.success) {
				    		document.getElementById("load").style.display ='none';
				    		document.getElementById("show").style.display ='none';
							$.messager.show( {
								title : 'Error',
								msg : result.msg
							});
						} else {
				    		document.getElementById("load").style.display ='none';
				    		document.getElementById("show").style.display ='none';
							$.messager.alert("提示", "删除成功！");
							$('#rdlg').dialog('close');
							$('#weldTaskTable').datagrid('reload');
//							var url = "weldedjunction/goWeldedJunction";
//							var img = new Image();
//						    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
//						    url = img.src;  // 此时相对路径已经变成绝对路径
//						    img.src = null; // 取消请求
//							window.location.href = encodeURI(url);
						}
		            }  
		        },  
		        error : function(errorMsg) {  
		            alert("数据请求失败，请联系系统管理员!");  
		        }  
		   }); 
		}
	});
}

function openDeleteDialog(){
	$("#batchDeleteTable").datagrid( {
//		fitColumns : true,
//		view: detailview,
		height : $("#bdt").height(),
		width : $("#bdt").width(),
		idField : 'id',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getWeldTaskListNoPage",
		singleSelect : false,
//		rownumbers : true,
//		showPageList : false,
		columns : [ [ {
		    field:'ck',
			checkbox:true
		},{
			field : 'id',
			title : '序号',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'weldedJunctionno',
			title : '任务编号',
//			width : 90,
			halign : "center",
			align : "left"
		}, /*{
			field : 'serialNo',
			title : '任务描述',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'levelname',
			title : '任务等级',
//			width : 150,
			halign : "center",
			align : "left"
		}, */{
			field : 'itemid',
			title : '项目id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'itemname',
			title : '所属班组',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'levelid',
			title : '任务等级id',
//			width : 150,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'dtoTime1',
			title : '计划开始时间',
//			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'dtoTime2',
			title : '计划结束时间',
//			width : 150,
			halign : "center",
			align : "left"
		},/*{
			field : 'dyne',
			title : '焊工id',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},*/{
			field : 'taskResultId',
			title : '任务执行id',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},/*{
			field : 'realStartTime',
			title : '实际开始时间',
			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'realEndTime',
			title : '实际结束时间',
			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'resultid',
			title : '评价id',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'result',
			title : '任务评价',
			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'resultName',
			title : '评价等级',
			width : 90,
			halign : "center",
			align : "left"
		},*/{
			field : 'status',
			title : '状态值',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'edit',
			title : '编辑',
			width : 300,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = '<a id="edit" class="easyui-linkbutton"/>';
				str += '<a id="remove" class="easyui-linkbutton"/>';
				if(row.status==0){
					str += '<a id="confirm" class="easyui-linkbutton"/>';
				}
				if(row.status==1){
					str += '<a id="confirm1" class="easyui-linkbutton" disabled="true"/>';
				}
				if(row.status==2){
					str += '<a id="confirm2" class="easyui-linkbutton" disabled="true"/>';
				}
				str += '<a id="evaluation" class="easyui-linkbutton"/>';
				return str;
			}
		}] ],
//		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onLoadSuccess: function(data){
	        $("a[id='edit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
	        $("a[id='remove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
	        if($("#confirm").length!=0){
				$("a[id='confirm']").linkbutton({text:'确认完成',plain:true,iconCls:'icon-unfinished'});
			};
			if($("#confirm1").length!=0){
				$("a[id='confirm1']").linkbutton({text:'已完成',plain:true,iconCls:'icon-finish'});
			};
			if($("#confirm2").length!=0){
				$("a[id='confirm2']").linkbutton({text:'未领取',plain:true,iconCls:'icon-assign'});
			};
			$("a[id='evaluation']").linkbutton({text:'评价',plain:true,iconCls:'icon-newadd'});
		}
	});
	$('#bdt').window( {
		title : "批量删除任务",
		modal : true
	});
	$('#bdt').window('open');
}

function batchDelete() {
	var rows = $("#batchDeleteTable").datagrid("getSelections");
	if(rows.length==0){
		alert("请选择任务后再进行删除！！！");
		return;
	}
	var str = "";
	for (var i = 0; i < rows.length; i++) {
		str += rows[i].id + ",";
	}
	$.ajax({
		type : "post",
		url : "weldtask/batchDelete?str="+str,
		dataType : "json",
		data : {},
		success : function(result) {
			if (result) {
				var result = eval(result);
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$.messager.alert("提示", "删除成功");
					$('#bdt').dialog('close');
					$('#weldTaskTable').datagrid('reload');
				}
			}
		},
		error : function() {
			alert('error');
		}
	});
}