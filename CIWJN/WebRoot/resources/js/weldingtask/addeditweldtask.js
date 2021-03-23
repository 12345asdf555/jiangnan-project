$(function(){
	InsframeworkCombobox();
	WelderCombobox();
	taskCombobox();
	$('#dlg').dialog( {
		onClose : function() {
			$('#itemid').combobox('clear');
			$("#fm").form("disableValidation");
		}
	});
//	itemidChange();
	$("#fm").form("disableValidation");
/*	$("#weldedJunctionno").textbox('textbox').blur(function(){
		var wjno = $("#weldedJunctionno").val();
		if(wjno.length!=0){
			var len = wjno.length;
			if(len!=8){
				for(var i=0;i<8-len;i++){
					wjno = "0"+wjno;
				}
			}
			$("#weldedJunctionno").textbox('setValue',wjno);
		}
	});*/
})

var symbol=0;
var url = "";
var flag = 1;
function addWeldedjunction(){
	flag = 1;
	$('#dlg').window( {
		title : "新增任务",
		modal : true
	});
	$('#dlg').window('open');
	$('#fm').form('clear');
	url = "weldtask/addWeldTask";
}

function editWeldedjunction(){
	flag = 2;
	$('#fm').form('clear');
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
	              symbol=2;
	              $('#dlg').window( {
	                title : "修改任务",
	                modal : true
	              });
	              $('#dlg').window('open');
	              $('#fm').form('load', row);
	              $('#oldno').val(row.weldedJunctionno);
	              url = "weldtask/editWeldTask?id="+ row.id;
	            }
	          }
	      },  
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	}); 
}

//提交
function save(){
	var wtno = $("#weldedJunctionno").textbox('getValue');
	var item = $('#itemid').combobox('getValue');
	if(wtno==""||item==""){
		alert("必选项不能为空！！！");
	}else{
		document.getElementById("load").style.display="block";
		var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
		$("#body").append(sh);
		document.getElementById("show").style.display="block";
		var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
		var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
//		var fwelderid = $('#welderid').val();
		var fitemid = $('#itemid').combobox('getValue');
//		var quali = $('#quali').combobox('getValue');
		var tasklevel = $('#levelid').combobox('getValue');
		var messager = "";
		var url2 = "";
		if(flag==1){
			messager = "新增成功！";
//			url2 = url+"?fitemid="+fitemid+"&fwelderid="+fwelderid+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2+"&quali="+quali+"&tasklevel="+tasklevel;
			url2 = url+"?fitemid="+fitemid+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2+"&tasklevel="+tasklevel;
		}else{
			messager = "修改成功！";
//			url2 = url+"&fitemid="+fitemid+"&fwelderid="+fwelderid+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2+"&quali="+quali+"&tasklevel="+tasklevel;
			url2 = url+"&fitemid="+fitemid+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2+"&tasklevel="+tasklevel;
		}
		$('#fm').form('submit', {
			url : url2,
			onSubmit : function() {
				return $(this).form('enableValidation').form('validate');
			},
			success : function(result) {
				if(result){
					var result = eval('(' + result + ')');
					if (!result.success) {
			    		document.getElementById("load").style.display ='none';
			    		document.getElementById("show").style.display ='none';
						$.messager.show( {
							title : 'Error',
							msg : result.errorMsg
						});
					}else{
			    		document.getElementById("load").style.display ='none';
			    		document.getElementById("show").style.display ='none';
						$.messager.alert("提示", messager);
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
}

//所属项目
function InsframeworkCombobox(){
	$.ajax({  
      type : "post",  
      async : false,
      url : "weldingMachine/getUserInsAll",  
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

//焊工资质
function WelderCombobox(){
	$.ajax({  
    type : "post",  
    async : false,
    url : "Dictionary/getValueByTypeid?type="+7,  
    data : {},  
    dataType : "json", //返回数据形式为json  
    success : function(result) {  
        if (result) {
            var optionStr = '';
            for (var i = 0; i < result.ary.length; i++) {  
                optionStr += "<option value=\"" + result.ary[i].value + "\" >"  
                        + result.ary[i].name + "</option>";
            }
            $("#quali").html(optionStr);
        }  
    },  
    error : function(errorMsg) {  
        alert("数据请求失败，请联系系统管理员!");  
    }  
	}); 
	$("#quali").combobox();
}

//任务等级
function taskCombobox(){
	$.ajax({  
  type : "post",  
  async : false,
  url : "Dictionary/getValueByTypeid?type="+8,  
  data : {},  
  dataType : "json", //返回数据形式为json  
  success : function(result) {  
      if (result) {
          var optionStr = '';
          for (var i = 0; i < result.ary.length; i++) {  
              optionStr += "<option value=\"" + result.ary[i].value + "\" >"  
                      + result.ary[i].name + "</option>";
          }
          $("#levelid").html(optionStr);
      }  
  },  
  error : function(errorMsg) {  
      alert("数据请求失败，请联系系统管理员!");  
  }  
	}); 
	$("#levelid").combobox();
}


function selectWelder(){
	  $('#fdlgSearch').show();
	  $('#fdlg').window( {
	    modal : true
	  });
	  $('#fdlg').window('open');
	  searchStr=$("#userinsall").val();
	  WelderDatagrid();
	  symbol=1;
}

var searchStr="";
function WelderDatagrid(){
	$("#welderTable").datagrid( {
		height : $("#fdlg").height(),
		width : $("#fdlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldtask/getFreeWelder?searchStr="+encodeURI(searchStr),
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		pagination : true,
		fitColumns : true,
		columns : [ [ {
		      field : 'ck',
		      checkbox : true
		},{
			field : 'id',
			title : '序号',
			width : 100,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'name',
			title : '姓名',
			width : 80,
			halign : "center",
			align : "left"
		}, {
			field : 'welderno',
			title : '编号',
			width : 100,
			halign : "center",
			align : "left"
		},{
			field : 'qualiname',
			title : '资质',
			width : 100,
			halign : "center",
			align : "left"
		},{
			field : 'insname',
			title : '部门',
			width : 150,
			halign : "center",
			align : "left"
		},{
			field : 'owner',
			title : '部门id',
			width : 150,
			halign : "center",
			align : "left",
			hidden : true
		},{
			field : 'back',
			title : '备注',
			width : 100,
			halign : "center",
			align : "left"
		}
		] ],
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		nowrap : false,
		toolbar : '#fdlgSearch'
	});
}

function dlgSearchGather(){
	  if($("#searchname").val()){
		  if($("#userinsall").val()){
			    searchStr +=  " and tb_welder.fwelder_no=" + $("#searchname").val();
		  }else{
			    searchStr +=  " tb_welder.fwelder_no=" + $("#searchname").val();
		  }
	  }
	  WelderDatagrid();
	  searchStr = $("#userinsall").val();
}

function saveWelder(){
	  var row = $("#welderTable").datagrid('getSelected');
	  if(row==null){
		  $('#fdlg').dialog('close');
	  }else{
		  $("#pipelineNo").textbox('setValue',row.welderno);
		  $('#welderid').val(row.id);
		  $('#itemid').combobox('setValue', row.owner);
		  $('#fdlg').dialog('close');
		  $('#welderTable').datagrid('clearSelections'); 
	  }
	}

function cancelWelder(){
	  $("#pipelineNo").textbox('clear');
	  $("#welderid").val("");
	  $('#fdlg').dialog('close');
}

function itemidChange(){
	$("#itemid").combobox({
		onSelect: function (record) {
		  if(symbol==0){
			  $("#pipelineNo").textbox('clear');
			  $("#welderid").val()
			  $('#fdlgSearch').hide();
			  if(searchStr==null || searchStr==""){
				  searchStr +=  " tb_welder.Fowner=" + record.value;
			  }else{
				  searchStr +=  " and tb_welder.Fowner=" + record.value;
			  }
			  WelderDatagrid();
			  $('#fdlg').window( {
				    title : "请选择对应组织机构下的焊工",
				    modal : true
			  });
			  $('#fdlg').window('open');
			  searchStr = $("#userinsall").val();
			  symbol=0;
		  }
		  if(symbol==2){
			  $('#fdlgSearch').hide();
			  symbol=0;
		  }
		  if(symbol==1){
			  $('#fdlgSearch').hide();
			  if(searchStr==null || searchStr==""){
				  searchStr +=  " tb_welder.Fowner=" + record.value;
			  }else{
				  searchStr +=  " and tb_welder.Fowner=" + record.value;
			  }
			  WelderDatagrid();
			  $('#fdlg').window( {
				    title : "请选择对应组织机构下的焊工",
				    modal : true
			  });
			  $('#fdlg').window('open');
			  searchStr = $("#userinsall").val();
			  symbol=0;
		  }
		}
	})
}

function saveExportdlg(){
	document.getElementById("load").style.display="block";
	var sh = '<div id="show" style="align="center""><img src="resources/images/load.gif"/>正在加载，请稍等...</div>';
	$("#body").append(sh);
	document.getElementById("show").style.display="block";
	var rows = $('#exporttable').datagrid('getData');
	var jsonStr = JSON.stringify(rows.rows)
	$.ajax({  
	      type : "post",  
	      async : false,
	      url : "weldtask/taskImport",  
	      data : {taskstr:jsonStr},  
	      dataType : "json", //返回数据形式为json  
	      success : function(result) {  
	          if (!result.success) {
		    		document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
	          }else{
		    		document.getElementById("load").style.display ='none';
		    		document.getElementById("show").style.display ='none';
					$.messager.alert("提示", "导入成功");
					$('#exportdlg').dialog('close');
					$('#weldTaskTable').datagrid('reload');
	          }
	      },  
	      error : function(errorMsg) {  
	          alert("数据请求失败，请联系系统管理员!");  
	      }  
	}); 
}

function closeFdlog(){
	$('#fdlg').dialog('close');
	$('#welderTable').datagrid('clearSelections'); 
}

function closeExportdlg(){
	$('#exportdlg').dialog('close');
}

function closeDayin(){
	$('#dayin').dialog('close');
}