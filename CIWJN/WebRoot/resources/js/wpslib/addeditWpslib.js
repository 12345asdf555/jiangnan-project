/**
 * 
 */
var oldchanel = 0;
$(function() {
	//	rule();
	statusRadio();
	addWpslib();
	machineModel();
	getDictionary(10,"sxfselect");
	getDictionary(24,"sxfgas");
	getDictionary(23,"sxfdiameter");
	getDictionary(18,"sxfmaterial");
	getDictionary(19,"sxfcontroller");
	getDictionary(20,"sxfinitial");
	getDictionary(21,"sxfarc");
	
	$('#editSxDlg').dialog( {
		onClose : function() {
			$('#sxfwpsnum').combobox('clear');
			$('#sxfselect').combobox('clear');
			$('#sxfgas').combobox('clear');
			$('#sxfdiameter').combobox('clear');
			$('#sxfmaterial').combobox('clear');
			$('#sxfinitial').combobox('clear');
			$('#sxfcontroller').combobox('clear');
			$("#sxfm").form("disableValidation");
		}
	})
	$('#smwdlg').dialog({
		onClose : function() {
			$('#mainWpsTable').datagrid('clearSelections');
		}
	})
	$('#smdlg').dialog({
		onClose : function() {
			$('#weldingmachineTable').datagrid('clearSelections');
		}
	})
	$('#sxSelectdlg').dialog( {
		onClose : function() {
			$('#sxSelectWpsTab').datagrid('clearSelections');
		}
	})
	$('#sxMachinedlg').dialog( {
		onClose : function() {
			$('#sxMachineTable').datagrid('clearSelections');
		}
	})
	$('#wltdlg').dialog({
		onClose : function() {
			$("#wltfm").form("disableValidation");
		}
	})
	$("#wltfm").form("disableValidation");
	$("#sxfm").form("disableValidation");
})

var url = "";
var flag = 1;
function addWpslib() {
	flag = 1;
	$('#wltfm').form('clear');
	$('#wltdlg').window({
		title : "新增工艺库",
		modal : true
	});
	$('#wltdlg').window('open');
	var statusId = document.getElementsByName("statusId");
	statusId[0].checked = 'checked';
	$('#model').combobox('enable');
	url = "wps/addWpslib";
}

function editWpslib() {
	flag = 2;
	$('#wltfm').form('clear');
	var row = $('#wpslibTable').datagrid('getSelected');
	if (row) {
		$('#wltdlg').window({
			title : "修改工艺库",
			modal : true
		});
		$('#wltdlg').window('open');
		$('#wltfm').form('load', row);
		$('#validwl').val(row.wpslibName);
		$('#model').combobox('disable', true);
		url = "wps/updateWpslib?fid=" + row.fid;
	}
}

function saveWpslib() {
	var wpslibName = $('#wpslibName').val();
	var fstatus = $("input[name='statusId']:checked").val();
	var messager = "";
	var url2 = "";
	if (flag == 1) {
		var machineModel = $('#model').combobox('getValue');
		messager = "新增成功！";
		url2 = url + "?fstatus=" + fstatus + "&wpslibName=" + encodeURI(wpslibName) + "&machineModel=" + encodeURI(machineModel);
	} else {
		messager = "修改成功！";
		url2 = url + "&fstatus=" + fstatus + "&wpslibName=" + encodeURI(wpslibName);
	}
	$('#wltfm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if (result) {
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$.messager.alert("提示", messager);
					$('#wltdlg').dialog('close');
					$('#wpslibTable').datagrid('reload');
					$("#validwl").val("");
				}
			}

		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

//工艺库状态
function statusRadio() {
	$.ajax({
		type : "post",
		async : false,
		url : "wps/getStatusAll",
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				var str = "";
				for (var i = 0; i < result.ary.length; i++) {
					str += "<input type='radio' class='radioStyle' name='statusId' id='sId' value=\"" + result.ary[i].id + "\" />"
					+ result.ary[i].name;
				}
				$("#radios").html(str);
				$("input[name='statusId']").eq(0).attr("checked", true);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

var mflag = 1;
function addMainWps() {
	mflag = 1;
	$('#mwfm').form('clear');
	$('#mwdlg').window({
		title : "新增工艺",
		modal : true
	});
	var wlrow = $('#wpslibTable').datagrid('getSelected');
	url = "wps/addMainWps?fid=" + wlrow.fid;
	if (wlrow.model == 174) {
		EPWINIT();
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 175) {
		EPSINIT();
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 176) {
		WBMLINIT();
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 177) {
		WBPINIT();
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 178) {
		WBLINIT();
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 171) {
		CPVEWINIT();
		comboboxCheck(wlrow.model);
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 172) {
		CPVESINIT();
		$('#mwdlg').window('open');
		return;
	} else if (wlrow.model == 173) {
		CPVETINIT();
		$('#mwdlg').window('open');
		return;
	} else {
		$('#editSxDlg').window({
			title : "新增工艺",
			modal : true
		});
		$("#sxRemoveWpsBut").hide();
		$("#sxgetWpsBut").show();
		$("#sxSaveWpsBut").show();
		$('#sxfm').form('clear');
		sxDefault();
		$('#editSxDlg').window('open');
		$("input[name='sxfcharacter']").eq(0).prop("checked", true);
		url = "wps/addSxWps?fwpslib_id=" + wlrow.fid+"&fcharacter="+$('input[name="sxfcharacter"]:checked').val();
		return;
	}
	
}

function editMainWps(indexrow,row) {
	mflag = 2;
	$('#mwfm').form('clear');
	if (row) {
		if (row.model == 174) {
			EPWINIT();
		} else if (row.model == 175) {
			EPSINIT();
		} else if (row.model == 176) {
			WBMLINIT();
		} else if (row.model == 177) {
			WBPINIT();
		} else if (row.model == 178) {
			WBLINIT();
		} else if (row.model == 172) {
			CPVESINIT();
		} else if (row.model == 173) {
			CPVETINIT();
		} else if (row.model == 171) {
			CPVEWINIT();
			comboboxCheck(row.model);
		} else if (row.manu == 149){
			mflag = 2;
			$('#sxfm').form('clear');
			if (row) {
				$('#editSxDlg').window( {
					title : "修改工艺",
					modal : true
				});
				$("#sxRemoveWpsBut").hide();
				$("#sxgetWpsBut").show();
				$("#sxSaveWpsBut").show();
				$('#editSxDlg').window('open');
				$('#sxfm').form('load', indexrow);
				$('#sxchanel').val(indexrow.fwpsnum);
				$("input[name='sxfcharacter']").eq(indexrow.sxfcharacter).prop("checked", true);
				url = "wps/editSxWps?fid="+indexrow.fid+"&fcharacter="+$('input[name="sxfcharacter"]:checked').val();
			}
			return;
		}
		$('#mwdlg').window({
			title : "修改工艺",
			modal : true
		});
		$('#mwdlg').window('open');
		$('#mwfm').form('load', indexrow);
		if (encodeURI(indexrow.initial) == "1") {
			$("#finitial").prop("checked", true);
		}
		if (encodeURI(indexrow.mode) == "1") {
			$("#fmode").prop("checked", true);
		}
		if (encodeURI(indexrow.controller) == "1") {
			$("#fcontroller").prop("checked", true);
		}
		if (encodeURI(indexrow.torch) == "1") {
			$("#ftorch").prop("checked", true);
		}
		url = "wps/updateMainWps?fid=" + indexrow.fid;
		oldchanel = indexrow.fchanel;
	}
}

function saveMainWps() {
	var wlrow = $('#wpslibTable').datagrid('getSelected');
	if (wlrow.model == 174) {
		if (EPWCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 175) {
		if (EPSCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 176) {
		if (WBMLCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 177) {
		if (WBPCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 178) {
		if (WBLCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 171) {
		if (CPVEWCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 172) {
		if (CPVESCHECK() == false) {
			return;
		}
	} else if (wlrow.model == 173) {
		if (CPVETCHECK() == false) {
			return;
		}
	} else if (wlrow.manu == 149) {
		saveSxWps();
		return;
	}

	var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
	var index = $('#wpslibTable').datagrid('getRowIndex',wpsLibRow);
	if (parseInt(oldchanel) != $('#fchanel').combobox('getValue')) {
		var num;
		$.ajax({
			type : "post",
			async : false,
			url : "wps/getCountByWpslibidChanel?wpslibid=" + wpsLibRow.fid + "&chanel=" + $('#fchanel').combobox('getValue'),
			data : {},
			dataType : "json", //返回数据形式为json  
			success : function(result) {
				if (result) {
					num = eval(result.count);
				}
			},
			error : function(errorMsg) {
				alert("数据请求失败，请联系系统管理员!");
			}
		});
		if (num > 0) {
			alert("该通道规范已经存在!!!");
			return;
		}
	}
	var messager = "";
	if (mflag == 1) {
		messager = "新增成功！";
	} else {
		messager = "修改成功！";
	}
	var url2 = "";
	var finitial;
	var fcontroller;
	var fmode;
	var ftorch;
	if ($("#finitial").is(":checked") == true) {
		finitial = 1;
	} else {
		finitial = 0;
	}
	if ($("#fcontroller").is(":checked") == true) {
		fcontroller = 1;
	} else {
		fcontroller = 0;
	}
	if ($("#fmode").is(":checked") == true) {
		fmode = 1;
	} else {
		fmode = 0;
	}
	if ($("#ftorch").is(":checked") == true) {
		ftorch = 1;
	} else {
		ftorch = 0;
	}
	var fselect = $('#fselect').combobox('getValue');
	var farc = $('#farc').combobox('getValue');
	var fmaterial = $('#fmaterial').combobox('getValue');
	var fgas = $('#fgas').combobox('getValue');
	var fdiameter = $('#fdiameter').combobox('getValue');
	var chanel = $('#fchanel').combobox('getValue');
	var ftime = $('#ftime').numberbox('getValue');
	var fadvance = $('#fadvance').numberbox('getValue');
	var fini_ele = $('#fini_ele').numberbox('getValue');
	var fweld_ele = $('#fweld_ele').numberbox('getValue');
	var farc_ele = $('#farc_ele').numberbox('getValue');
	var fhysteresis = $('#fhysteresis').numberbox('getValue');
	var fcharacter = $('#fcharacter').numberbox('getValue');
	var fweld_tuny_ele = $('#fweld_tuny_ele').numberbox('getValue');
	var farc_tuny_ele = $('#farc_tuny_ele').numberbox('getValue');
	var fini_vol = $('#fini_vol').numberbox('getValue');
	var fweld_vol = $('#fweld_vol').numberbox('getValue');
	var farc_vol = $('#farc_vol').numberbox('getValue');
	var fini_vol1 = $('#fini_vol1').numberbox('getValue');
	var fweld_vol1 = $('#fweld_vol1').numberbox('getValue');
	var farc_vol1 = $('#farc_vol1').numberbox('getValue');
	var fweld_tuny_vol = $('#fweld_tuny_vol').numberbox('getValue');
	var farc_tuny_vol = $('#farc_tuny_vol').numberbox('getValue');
	var fprocess = $('#fweldprocess').combobox('getValue');
	var fwarn_ele_up = $('#fwarn_ele_up').numberbox('getValue');
	var fwarn_ele_down = $('#fwarn_ele_down').numberbox('getValue');
	var fwarn_vol_up = $('#fwarn_vol_up').numberbox('getValue');
	var fwarn_vol_down = $('#fwarn_vol_down').numberbox('getValue');
//	var farc_delay_time = $('#farc_delay_time').numberbox('getValue');
	url2 = url + "&finitial=" + finitial + "&fcontroller=" + fcontroller + "&fmode=" + fmode + "&fselect=" + fselect 
	+ "&farc=" + farc + "&fmaterial=" + fmaterial + "&fgas=" + fgas + "&fdiameter=" + fdiameter + "&chanel=" + chanel 
	+ "&ftime=" + ftime + "&fadvance=" + fadvance + "&fini_ele=" + fini_ele + "&fweld_ele=" + fweld_ele + "&farc_ele=" + farc_ele 
	+ "&fhysteresis=" + fhysteresis + "&fcharacter=" + fcharacter + "&fweld_tuny_ele=" + fweld_tuny_ele + "&farc_tuny_ele=" + farc_tuny_ele 
	+ "&fini_vol=" + fini_vol + "&fini_vol1=" + fini_vol1 + "&fweld_vol=" + fweld_vol + "&fweld_vol1=" + fweld_vol1 + "&farc_vol=" + farc_vol 
	+ "&farc_vol1=" + farc_vol1 + "&fweld_tuny_vol=" + fweld_tuny_vol + "&farc_tuny_vol=" + farc_tuny_vol + "&fprocess=" + fprocess 
	+ "&ftorch=" + ftorch + "&fwarn_ele_up=" + fwarn_ele_up + "&fwarn_ele_down=" + fwarn_ele_down + "&fwarn_vol_up=" + fwarn_vol_up 
	+ "&fwarn_vol_down=" + fwarn_vol_down;
	$.ajax({
		type : "post",
		async : false,
		url : url2,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (!result.success) {
				$.messager.show({
					title : 'Error',
					msg : result.errorMsg
				});
				oldchanel = 0;
			} else {
				$.messager.alert("提示", messager);
				oldchanel = 0;
				$('#mwdlg').dialog('close');
				$('#ddv-'+index).datagrid('reload');
		//		$('#wpslibTable').datagrid('reload');
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
			oldchanel = 0;
		}
	});
}

function rule() {
	$("#farc").combobox({
		onSelect : function(record) {
			if (record.value == 111) {
				$('#farc_ele').numberbox("disable", true);
				$('#farc_vol').numberbox("disable", true);
				$('#farc_tuny_ele').numberbox("disable", true);
				$('#farc_tuny_vol').numberbox("disable", true);
				$('#farc_tuny_vol1').numberbox("disable", true);
				$('#farc_vol1').numberbox("disable", true);
				$('#ftime').numberbox("disable", true);
				$('#fini_ele').numberbox("disable", true);
				$('#fini_vol').numberbox("disable", true);
				$('#fini_vol1').numberbox("disable", true);
			} else if (record.value == 112) {
				$('#farc_ele').numberbox("enable", true);
				$('#farc_vol').numberbox("enable", true);
				$('#farc_tuny_ele').numberbox("enable", true);
				$('#farc_tuny_vol').numberbox("enable", true);
				$('#farc_tuny_vol1').numberbox("enable", true);
				$('#farc_vol1').numberbox("enable", true);
				$('#ftime').numberbox("disable", true);
				if ($("#finitial").is(":checked")) {
					$('#fini_ele').numberbox("enable", true);
					$('#fini_vol').numberbox("enable", true);
					$('#fini_vol1').numberbox("enable", true);
				} else {
					$('#fini_ele').numberbox("disable", true);
					$('#fini_vol').numberbox("disable", true);
					$('#fini_vol1').numberbox("disable", true);
				}
			} else if (record.value == 113) {
				$('#farc_ele').numberbox("enable", true);
				$('#farc_vol').numberbox("enable", true);
				$('#farc_tuny_ele').numberbox("enable", true);
				$('#farc_tuny_vol').numberbox("enable", true);
				$('#farc_tuny_vol1').numberbox("enable", true);
				$('#farc_vol1').numberbox("enable", true);
				$('#ftime').numberbox("disable", true);
				if ($("#finitial").is(":checked")) {
					$('#fini_ele').numberbox("enable", true);
					$('#fini_vol').numberbox("enable", true);
					$('#fini_vol1').numberbox("enable", true);
				} else {
					$('#fini_ele').numberbox("disable", true);
					$('#fini_vol').numberbox("disable", true);
					$('#fini_vol1').numberbox("disable", true);
				}
			} else {
				$('#farc_ele').numberbox("disable", true);
				$('#farc_vol').numberbox("disable", true);
				$('#farc_tuny_ele').numberbox("disable", true);
				$('#farc_tuny_vol').numberbox("disable", true);
				$('#farc_tuny_vol1').numberbox("disable", true);
				$('#farc_vol1').numberbox("disable", true);
				$('#fini_ele').numberbox("disable", true);
				$('#fini_vol').numberbox("disable", true);
				$('#fini_vol1').numberbox("disable", true);
				$('#ftime').numberbox("enable", true);
				$('#ftime').numberbox("enable", true);
			}
		}
	});

	$("#finitial").click(function() {
		if ($("#finitial").is(":checked")) {
			if ($('#farc').combobox('getValue') == 112 || $('#farc').combobox('getValue') == 113) {
				$('#fini_ele').numberbox("enable", true);
				$('#fini_vol').numberbox("enable", true);
				$('#fini_vol1').numberbox("enable", true);
			} else {
				$('#fini_ele').numberbox("disable", true);
				$('#fini_vol').numberbox("disable", true);
				$('#fini_vol1').numberbox("disable", true);
			}
		} else {
			$('#fini_ele').numberbox("disable", true);
			$('#fini_vol').numberbox("disable", true);
			$('#fini_vol1').numberbox("disable", true);
		}
	});

	$("#fmaterial").combobox({
		onSelect : function(record) {
			if (record.value == 91) {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				}, {
					"text" : "MAG",
					"value" : "122"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.0",
					"value" : "131"
				}, {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.4",
					"value" : "133"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			} else if (record.value == 92) {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "MIG",
					"value" : "123"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			} else if (record.value == 93) {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.4",
					"value" : "133"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			} else {
				$('#fgas').combobox('clear');
				$('#fgas').combobox('loadData', [ {
					"text" : "CO2",
					"value" : "121"
				} ]);
				$('#fdiameter').combobox('clear');
				$('#fdiameter').combobox('loadData', [ {
					"text" : "Φ1.2",
					"value" : "132"
				}, {
					"text" : "Φ1.6",
					"value" : "134"
				} ]);
			}
			var fgas = $('#fgas').combobox('getData');
			var fdiameter = $('#fdiameter').combobox('getData');
			$('#fgas').combobox('select', fgas[0].value);
			$('#fdiameter').combobox('select', fdiameter[0].value);
		}
	});
}

function machineModel() {
	$.ajax({
		type : "post",
		async : false,
		url : "Dictionary/getValueByTypeid?type=" + 17,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				if (result.ary.length != 0) {
					var boptionStr = '';
					for (var i = 0; i < result.ary.length; i++) {
						boptionStr += "<option value=\"" + result.ary[i].value + "\" >"
							+ result.ary[i].name + "</option>";
					}
					$("#model").html(boptionStr);
					$("#model").combobox();
					$("#model").combobox('select', result.ary[0].value);
				}
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

function saveSxWps(){
	if(checkSxWps()==false){
		return;
	};
	var wpsLibRow = $('#wpslibTable').datagrid('getSelected');
	var index = $('#wpslibTable').datagrid('getRowIndex',wpsLibRow);
	var messager = "";
	var url2 = "";
	if(mflag==1){
		messager = "新增成功！";
		url2 = url;
	}else{
		messager = "修改成功！";
		url2 = url;
	}
	$('#sxfm').form('submit', {
		url : url2,
		onSubmit : function() {
			return $(this).form('enableValidation').form('validate');
		},
		success : function(result) {
			if(result){
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.errorMsg
					});
				}else{
					$.messager.alert("提示", messager);
					$('#editSxDlg').dialog('close');
					$('#ddv-'+index).datagrid('reload');
//					$('#wpslibTable').datagrid('reload');
				}
			}
			
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});
}

function getDictionary(typeid,id) {
	$.ajax({
		type : "post",
		async : false,
		url : "wps/getDictionary?typeid=" + typeid,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				var optionStr = '';
				for (var i = 0; i < result.ary.length; i++) {
					optionStr += "<option value=\"" + result.ary[i].id + "\" >"
						+ result.ary[i].name + "</option>";
				}
				$("#"+id).html(optionStr);
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
	$("#" + id).combobox();
}
