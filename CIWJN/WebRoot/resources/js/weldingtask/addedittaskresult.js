$(function(){
	$("#welderNo").next().hide();
	InsframeworkCombobox();
	$('#dlg').dialog( {
		onClose : function() {
			$('#itemid').combobox('clear');
			$("#fm").form("disableValidation");
		}
	});
	$("#fm").form("disableValidation");
})

function editWeldedjunction(value){ 
	flag = 2;
	var row = $('#weldTaskTable').datagrid('getSelected');
	value = eval(value);
	$.ajax({  
      type : "post",  
      async : false,
      url : "weldedjunction/getCouneByTaskid?taskid="+row.taskid+"&type="+"1", 
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {
        if (result==false) {
          alert("任务已完成，无法进行修改或取消！！！"); 
          }else{
            if (row) {
              $('#dlg').window( {
                title : "修改任务",
                modal : true
              });
              $('#dlg').window('open');
//              $('#fm').form('load', row);
              $("#oldno").val(value.taskno);   //不加会出现不能保存相同名的id这个错误
              $("#machineid").val(value.machid);
              $("#taskid").val(value.taskid);
              $("#welderNo").val(value.welderno);
//              $("#taskNo").val(row.taskNo);
//              $("#machineNo").val(value5);
              $("#taskNo").textbox('setValue',value.taskno);
              $("#machineNo").textbox('setValue',value.machno);
              url = "weldtask/getEvaluate?id="+value.id+"&result="+""+"&starttime="+row.starttime+"&endtime="+row.endtime+"&welderid="+value.welderid;

              //url = "weldtask/editWeldTask?id="+ row.id;
            }
          }
      },
      error : function(errorMsg) {  
          alert("数据请求失败，请联系系统管理员!");  
      }  
    }); 
}
function saveedit(){
	var temp;
	var url2;
	document.getElementById("load").style.display="block";
	var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
	$("#body").append(sh);
	document.getElementById("show").style.display="block";
	var taskNo= document.getElementById("taskNo").value;
	var taskid= document.getElementById("taskid").value;
	var machineid= document.getElementById("machineid").value;
	var machineNo=document.getElementById("machineNo").value;
	var resultName="";
	//alert(resultName.value);
    var rows = $("#weldTaskTable").datagrid("getSelections");
	if(flag==2){
		temp=2;
		url2=url+"&resultid="+""+"&welderNo="+rows[0].welderNo+"&taskid="+taskid+"&machineid="+machineid+"&operateid="+temp+"&taskNo="+taskNo+"&machineNo="+machineNo;
	}
	$('#fm').form('submit', {
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
					$('#dlg').dialog('close');
					$('#weldTaskTable').datagrid('reload');
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

//所属项目
function InsframeworkCombobox(){
	$.ajax({  
      type : "post",  
      async : false,
      url : "weldingMachine/getInsframeworkAll",  
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {  
          if (result) {
              var optionStr = '';
              for (var i = 0; i < result.ary.length; i++) {  
                  optionStr += "<option value=\"" + result.ary[i].id + "\" >"  
                          + result.ary[i].name + "</option>";
              }
              $("#itemid").html(optionStr);
          }  
      },  
      error : function(errorMsg) {  
          alert("数据请求失败，请联系系统管理员!");  
      }  
	}); 
	$("#itemid").combobox();
}
function selectMachine() {
	$('#fdlg').window({
		title : "任务编号",
		modal : true
	});
	$('#fdlg').window('open');
	weldingMachineDatagrid();
}
function weldingMachineDatagrid() {
	var search = "";
	var row = $("#weldTaskTable").datagrid('getSelected');
	$.ajax({
      type : "post",  
      async : false,
      url : "weldtask/getInsframework?id="+row.itemid,  
      data : {},  
      dataType : "json", //返回数据形式为json  
      success : function(result) {  
          if (result) {
        	  search = result.success;
          }  
      },  
      error : function(errorMsg) {  
          alert("数据请求失败，请联系系统管理员!");  
      }  
	}); 
	$("#dg").datagrid( {
//		fitColumns : true,
		height : $("#fdlg").height(),
		width : $("#fdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getFreeJunction?searchStr="+search,
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
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
			field : 'junctionno',
			title : '任务编号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'welderno',
			title : '焊工工号',
			width : 90,
			halign : "center",
			align : "left"
		},  {
			field : 'itemid',
			title : '项目id',
			width : 90,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'itemname',
			title : '所属项目',
			width : 150,
			halign : "center",
			align : "left"
		}, {
			field : 'desc',
			title : '任务描述',
			width : 150,
			halign : "center",
			align : "left"
		}
		] ],
		toolbar : '#dlgSearch',
		pagination : true,
		fitColumns : true
	});
}
function saveWeldingMachine() {
	var row = $("#dg").datagrid('getSelected');
	if(row==null){
		$('#fdlg').dialog('close');
	}else{
		$("#taskNo").textbox('setValue', row.junctionno);
		$("#taskid").val(row.id);
		$("#oldno").val(row.junctionno);
		//$("#itemid").val(row.itemid);
		$('#fdlg').dialog('close');
		$("#dg").datagrid("clearSelections");   //每次退出表格时去掉表格的勾选
	}
}



