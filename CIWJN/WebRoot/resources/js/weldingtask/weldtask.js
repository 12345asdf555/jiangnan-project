$(function(){
	weldedJunctionDatagrid();
//	dayinDatagrid();
//	exporttable();
	statusChange();
	resultCombobox();
	itemcombobox();
});

function statusChange(){
//	$("#status").combobox({
//		onChange : function(newValue,oldValue){
//			var searchStr = "";
//			if(newValue==1){
//				searchStr = " foperatetype=1";
//			}else if(newValue==0){
//				searchStr = " (foperatetype=0 or foperatetype=2)";
//			}else if(newValue==3){
//				searchStr = " foperatetype is null"
//			}
//			$("#weldTaskTable").datagrid('reload',{
//				"searchStr" : searchStr
//			})
//		}
//	})
	
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
					        	$("#bitem").combobox({disabled: false});
					        	$("#bitem").combobox('select',0);
				        }  
				    },  
				    error : function(errorMsg) {  
				        alert("数据请求失败，请联系系统管理员!");  
				    }  
					}); 
			}
		}
	})
	var searchStr = "",parent = "";
	$("#bitem").combobox({
		onChange : function(newValue,oldValue){
			searchStr = "";
			parent = "";
			var itemid = $("#bitem").combobox("getValue");
			var status = $("#status").combobox("getValue");
			if(itemid!=0){
				parent = "i.fid = "+itemid;
			}
			if(status==1){
				searchStr = " foperatetype=1";
			}else if(status==0){
				searchStr = " (foperatetype=0 or foperatetype=2)";
			}else if(status==3){
				searchStr = " foperatetype is null"
			}
			$("#weldTaskTable").datagrid('reload',{
				"searchStr" : searchStr,
				"parent" : parent
			})
		}
	})
	
	$("#status").combobox({
		onChange : function(newValue,oldValue){
			searchStr = "";
			parent = "";
			var itemid = $("#bitem").combobox("getValue");
			var status = $("#status").combobox("getValue");
			if(itemid!=0){
				parent = "i.fid = "+itemid;
			};
			if(status==1){
				searchStr = " foperatetype=1";
			}else if(status==0){
				searchStr = " (foperatetype=0 or foperatetype=2)";
			}else if(status==3){
				searchStr = " foperatetype is null"
			};
			$("#weldTaskTable").datagrid('reload',{
				"searchStr" : searchStr,
				"parent" : parent
			});
		}
	})
	
//	$("#hideFinished").click(function(){
//		searchStr = "";
//		parent = "";
//		var itemid = $("#bitem").combobox("getValue");
//		var status = $("#status").combobox("getValue");
//		if(itemid!=0){
//			parent = "i.fid = "+itemid;
//		};
//		if(this.checked==true){
//			if(parent==""){
//				parent = " r.foperatetype!=1"
//			}else{
//				parent += " and r.foperatetype!=1"
//			}
//		}
//		if(status==1){
//			searchStr = " foperatetype=1";
//		}else if(status==0){
//			searchStr = " (foperatetype=0 or foperatetype=2)";
//		}else if(status==3){
//			searchStr = " foperatetype is null"
//		};
//		$("#weldTaskTable").datagrid('reload',{
//			"searchStr" : searchStr,
//			"parent" : parent
//		});
//	})
}

function weldedJunctionDatagrid(){
	$("#weldTaskTable").datagrid( {
//		fitColumns : true,
//		view: detailview,
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getWeldTaskList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
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
		}, */{
			field : 'levelname',
			title : '任务等级',
//			width : 150,
			halign : "center",
			align : "left"
		}, /*{
			field : 'pipelineNo',
			title : '预设焊工',
//			width : 90,
			halign : "center",
			align : "left"
		},{
			field : 'realwelder',
			title : '实际焊工',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'roomNo',
			title : '焊工资质',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'welderid',
			title : '焊工id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'quali',
			title : '资质id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},*/{
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
		},{
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
		},{
			field : 'status',
			title : '状态值',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},/*{
			field : 'operatetype',
			title : '任务状态',
			width : 90,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = "";
				if(row.status==0){
					str = '<a id="confirm" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
				if(row.status==1){
					str = '<a id="confirm1" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
				if(row.status==2){
					str = '<a id="confirm2" class="easyui-linkbutton" href="javascript:confirm()" disabled="true"/>';
				}
				return str;
			}
		},*/{
			field : 'edit',
			title : '编辑',
			width : 300,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = '<a id="edit" class="easyui-linkbutton" href="javascript:editWeldedjunction()"/>';
				str += '<a id="remove" class="easyui-linkbutton" href="javascript:removeWeldedjunction()"/>';
				if(row.status==0){
					str += '<a id="confirm" class="easyui-linkbutton" href="javascript:confirmComplete()"/>';
				}
				if(row.status==1){
					str += '<a id="confirm1" class="easyui-linkbutton" href="javascript:confirmComplete()" disabled="true"/>';
				}
				if(row.status==2){
					str += '<a id="confirm2" class="easyui-linkbutton" href="javascript:confirmComplete()" disabled="true"/>';
				}
				str += '<a id="evaluation" class="easyui-linkbutton" href="javascript:evaluation()"/>';
				return str;
			}
		}] ],
		pagination : true,
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
/*		detailFormatter:function(index,row2){//严重注意喔
			return '<div"><table id="ddv-' + index + '" style=""></table></div>';
		},
		onExpandRow: function(index,row){//嵌套第一层，严重注意喔
			var ddv = $(this).datagrid('getRowDetail',index).find('#ddv-'+index);//严重注意喔
			ddv.datagrid({
//				fitColumns : true,
				idField : 'id',
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				url : "weldtask/getRealWelder?searchStr="+row.id,
				singleSelect : true,
				rownumbers : true,
				showPageList : false,
				columns : [ [ { 
					field : 'id',
					title : '序号',
					width : 30,
					halign : "center",
					align : "left",
					hidden:true
				}, {
					field : 'welderno',
					title : '焊工编号',
					halign : "center",
					align : "left",
					width : 200
				}, {
					field : 'weldername',
					title : '焊工姓名',
					halign : "center",
					align : "left",
					width : 200
				}
				] ],
				pagination : true,
				onResize:function(){
					$('#weldTaskTable').datagrid('fixDetailRowHeight',index);
				},
				onLoadSuccess:function(){
					$('#weldTaskTable').datagrid("selectRow", index)
					setTimeout(function(){
						$('#weldTaskTable').datagrid('fixDetailRowHeight',index);
					},0);
				}
			});
			$('#weldTaskTable').datagrid('fixDetailRowHeight',index);
		}*/
	});
}

function dayinDatagrid(){
	$("#dayintable").datagrid( {
//		fitColumns : true,
		height : $("#dayin").height(),
		width : $("#dayin").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getWeldTaskList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
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
		}, */{
			field : 'levelname',
			title : '任务等级',
//			width : 150,
			halign : "center",
			align : "left"
		}, /*{
			field : 'pipelineNo',
			title : '预设焊工',
//			width : 90,
			halign : "center",
			align : "left"
		},*/{
			field : 'realwelder',
			title : '实际焊工',
//			width : 90,
			halign : "center",
			align : "left"
		}, /*{
			field : 'roomNo',
			title : '焊工资质',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'welderid',
			title : '焊工id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'quali',
			title : '资质id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},*/{
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
		},{
			field : 'dyne',
			title : '焊工id',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'status',
			title : '状态值',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}] ],
		pagination : true,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        }
	});
}

function exporttable(){
	$("#exporttable").datagrid( {
//		fitColumns : true,
		height : $("#exportdlg").height(),
		width : $("#exportdlg").width(),
		idField : 'id',
//		pageSize : 10,
//		pageList : [ 10, 20, 30, 40, 50 ],
		url : "import/",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		columns : [ [ {
			field : 'taskNo',
			title : '任务编号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'levelname',
			title : '任务等级',
//			width : 90,
			halign : "center",
			align : "left"
//			hidden:true
		},{
			field : 'levelid',
			title : '任务等级id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, /*{
			field : 'welderNo',
			title : '焊工工号',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'quali',
			title : '焊工资质',
//			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'welderId',
			title : '焊工id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'qualiid',
			title : '资质id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},*/{
			field : 'insId',
			title : '项目id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'insName',
			title : '所属班组',
//			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'start',
			title : '计划开始时间',
//			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'end',
			title : '计划结束时间',
//			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'str',
			title : '错误描述',
//			width : 150,
			halign : "center",
			align : "left"
		}] ]
	});
}

function openDayin(){
	dayinDatagrid();
	$('#dayin').dialog('open');
}

//打印
function printWeldedjunction(){
	CreateFormPage("datagrid",$("#dayintable"),"任务列表");
	$('#dayin').dialog('close');
}

//导入
function importclick(){
	$("#importdiv").dialog("open").dialog("setTitle","从excel导入数据");
}

function importWeldingMachine(){
	var file = $("#file").val();
	if(file == null || file == ""){
		$.messager.alert("提示", "请选择要上传的文件！");
		return false;
	}else{
		document.getElementById("load").style.display="block";
		var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
		$("#body").append(sh);
		document.getElementById("show").style.display="block";
		$('#importfm').form('submit', {
			url : "import/importWeldTask",
			success : function(result) {
				if(result){
					var result = eval('(' + result + ')');
					if (result) {
			    		document.getElementById("load").style.display ='none';
			    		document.getElementById("show").style.display ='none';
						$('#importdiv').dialog('close');
						$('#exportdlg').window( {
							title : "任务确认与导入",
							modal : true
						});
						if(result.biaozhi==1){
							$('#imexcel').linkbutton('disable');
						}else{
							$('#imexcel').linkbutton('enable');
						}
						$('#exportdlg').window('open');
						exporttable();
						$("#exporttable").datagrid("loadData", result.rows);
					}
				}
				
			},  
		    error : function(errorMsg) {  
		        alert("数据请求失败，请联系系统管理员!");  
		    } 
		});
		
	}
}

//确认完成
function confirmComplete(){
	var url2="";
	var temp=1;
	$.messager.confirm('提示', '此操作不可撤销，是否确认?', function(flag) {
		if(flag){
			document.getElementById("load").style.display="block";
			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
			$("#body").append(sh);
			document.getElementById("show").style.display="block";
			var row = $('#weldTaskTable').datagrid('getSelected');
			url = "weldtask/getEvaluate?id="+row.taskResultId+"&taskid="+row.id+"&welderid="+null+"&machineid="+null;
			url2=url+"&result="+""+"&resultid="+""+"&welderNo="+""+"&operateid="+temp+"&taskNo="+""+"&machineNo="+""+"&starttime="+row.realStartTime+"&endtime="+getNowFormatDate();
			$.ajax({  
			      type : "post",  
			      async : false,
			      url : url2,  
			      data : {},  
			      dataType : "json", //返回数据形式为json  
			      success : function(result) {
			          if (result) {
							var result = eval(result);
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
								$('#weldTaskTable').datagrid('reload');
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

//获取当前时间并格式化
function getNowFormatDate() {
	  var date = new Date();
	  var seperator1 = "-";
	  var seperator2 = ":";
	  var month = date.getMonth() + 1;
	  var strDate = date.getDate();
	  if (month >= 1 && month <= 9) {
	      month = "0" + month;
	  }
	  if (strDate >= 0 && strDate <= 9) {
	      strDate = "0" + strDate;
	  }
	  var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	          + " " + date.getHours() + seperator2 + date.getMinutes()
	          + seperator2 + date.getSeconds();
	  return currentdate;
}

function evaluation(){
	var flag = 1;
	var row = $('#weldTaskTable').datagrid('getSelected');
	if(row.status!=1){
		 alert("任务未完成，无法进行评价"); 
	}else{
		if (row) {
			$('#mdlg').window( {
				title : "工作评价",
				modal : true
			});
			$('#mdlg').window('open');
			$('#mfm').form('load', row);
			if(row.resultid==0||row.resultid==""||row.resultid==null){
				var data = $('#resultid').combobox('getData');
				$('#resultid').combobox('select',data[0].value);
			}
			//$('#resultid').combobox('select', row.resultName);
			url = "weldtask/getEvaluate?id="+row.taskResultId+"&taskid="+row.id+"&welderid="+""+"&machineid="+""+"&starttime="+row.realStartTime+"&endtime="+row.realEndTime;
		}
	}
}

//评价等级下拉框
function resultCombobox(){
	$.ajax({  
      type : "post",  
      async : false,
      url : "weldtask/getStatusAll",  
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {
          if (result) {
              var optionStr = '';  
              for (var i = 0; i < result.ary.length; i++) { 
                  optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                          + result.ary[i].name + "</option>";  
              } 
              $("#resultid").append(optionStr);
          }  
      },  
      error : function(errorMsg) {  
          alert("数据请求失败，请联系系统管理员!");  
      }  
 }); 
	$("#resultid").combobox();
}

//评价的保存
function saveconment(){
	var temp;
	var url2;
	//提示转圈等待
	document.getElementById("load").style.display="block";
	var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
	$("#body").append(sh);
	document.getElementById("show").style.display="block";
	//var resultname=resultName.options[this.selectedIndex];
	var resultName = $('#resultid').combobox('getValue');
/*	alert(resultName.value);*/
	var result=document.getElementById("result").value;
	//alert(result.length);
  var rows = $("#weldTaskTable").datagrid("getSelections");
	temp=1;
	url2=url+"&result="+encodeURI(result)+"&resultid="+resultName+"&welderNo="+""+"&operateid="+temp+"&taskNo="+""+"&machineNo="+"";
	$('#mfm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if (result) {
				var result = eval('(' + result + ')');
				if (!result.success) {
					document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					if(!result.msg==null){
						$.messager.alert("提示", messager);
					}
					$('#mdlg').dialog('close');
					$('#weldTaskTable').datagrid('reload');
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

//任务批量完成
function complete(){
	
	$('#sdlg').window({
		title : "任务状态更改",
		modal : true
	});
	$('#sdlg').window('open');
	WeldingMachineDatagrid();
}
function WeldingMachineDatagrid() {
	$("#weg").datagrid( {
//		fitColumns : true,
		height : $("#sdlg").height(),
		width : $("#sdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getTaskResultList?searchStr= foperatetype!=1",
		rownumbers : false,
		showPageList : false,
		checkOnSelect:true,
		selectOnCheck:true,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		},{ 
			field : 'id',
			title : '序号',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'taskNo',
			title : '任务编号',
			width : 70,
			halign : "center",
			align : "left"
		},{
			field : 'welderNo',
			title : '焊工编号',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
				
		}, {
			field : 'welderid',
			title : '焊工编号id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'machineNo',
			title : '焊机编号',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'machineid',
			title : '焊机编号id',
//			width : 90,
			halign : "center",
			align : "left",
			hidden:true
	},{
		field : 'taskid',
		title : '任务编号id',
		width : 90,
		halign : "center",
		align : "left",
		hidden:true
	},{
		field : 'operateid',
		title : '状态id',
		width : 90,
		halign : "center",
		align : "left",
		hidden:true
	},{
		field : 'starttime',
		title : '开始时间',
		width : 100,
		halign : "center",
		align : "left",
		hidden:true
    },{
		field : 'endtime',
		title : '结束时间',
		width : 100,
		halign : "center",
		align : "left",
		hidden:true
    }/*,{
			field : 'operatetype',
			title : '任务状态',
			width : 100,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = '<a id="confirm" href="javascript:dgConfirm()" class="easyui-linkbutton">';
				return str;
			}
		}*/
		] ],/*
		onLoadSuccess: function(data){
			$("a[id='confirm']").linkbutton({text:'确认完成',plain:true,iconCls:'icon-unfinished'});
			
		},*/
		toolbar : '#dlgSearch',
		pagination : true,
		fitColumns : true
	});
}

function saveWeldingnumber(){
	var url2="";
	var temp=1;
	var con = window.confirm("此操作不可撤销，是否确认?");
		if(con==true){
			document.getElementById("load").style.display="block";
			var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
			$("#body").append(sh);
			document.getElementById("show").style.display="block";
			var row = $('#weg').datagrid('getSelections');
			var jsonStr = JSON.stringify(row)
			$.ajax({  
			      type : "post",  
			      async : false,
			      url : "weldtask/taskImportion", 
			      data : {taskstr:jsonStr},  
			      dataType : "json", //返回数据形式为json  
			      success : function(result) {
			          if (result) {
							var result = eval(result);
							if (!result.success) {
								document.getElementById("load").style.display ='none';
					    		document.getElementById("show").style.display ='none';
								$.messager.show( {
									title : 'Error',
									msg : result.errorMsg
								});
							} else {
								document.getElementById("load").style.display ='none';
					    		document.getElementById("show").style.display ='none';
								$('#weldTaskTable').datagrid('reload');
							}
						
			          }  
			      },  
			      error : function(errorMsg) {  
						document.getElementById("load").style.display ='none';
			    		document.getElementById("show").style.display ='none';
			          alert("数据请求失败，请联系系统管理员!");  
			      }  
			 }); 

		}else{
			return;
		}
	$('#sdlg').dialog('close');
}

//导出到excel
function exportDg(){
	$.messager.confirm("提示", "文件默认保存在浏览器的默认路径，<br/>如需更改路径请设置浏览器的<br/>“下载前询问每个文件的保存位置“属性！",function(result){
		if(result){
			var url = "export/exporWeldTask";
			var img = new Image();
		    img.src = url;  // 设置相对路径给Image, 此时会发送出请求
		    url = img.src;  // 此时相对路径已经变成绝对路径
		    img.src = null; // 取消请求
			window.location.href = encodeURI(url);
		}
	});
}

function itemcombobox(){
/*	$.ajax({  
      type : "post",  
      async : false,
      url : "weldingMachine/getInsframeworkAll",  
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {  
          if (result) {
              var optionStr = '<option value="0">请选择</option>';
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
	$("#item").combobox();
	$("#item").combobox('select',0);*/
	
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
		        	$("#zitem").combobox('select',0);
	        	}
	        	
	        }  
	    },  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    }  
		});
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
	$("#weldTaskTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}

