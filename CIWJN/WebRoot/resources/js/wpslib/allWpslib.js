/**
 * 
 */
var sxTableFlag=0;
var otcTableFlag=0;
$(function(){
	wpslibDatagrid();
	sxDefault();
	$("#fselect").combobox({
		onSelect : function(record) {
			if (record.value == 102) {
//				document.getElementById("tryiyuan").style.display = "none";
				$("#tryiyuan").next().hide();
			} else {
//				document.getElementById("trgebie").style.display = "none";
				$("#trgebie").css('display' ,'none')
			}
		}
	});
})

function wpslibDatagrid(){
	$("#wpslibTable").datagrid( {
//		fitColumns : true,
		view: detailview,
		height : $("#body").height(),
		width : $("#body").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "wps/getWpslibList",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		autoRowHeight:true,
		columns : [ [ {
			field : 'fid',
			title : '序号',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'manu',
			title : '厂商类型id',
			width : 30,
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'wpslibName',
			title : '工艺库名称',
			width : 270,
			halign : "center",
			align : "left"
		}, {
			field : 'model',
			title : '焊机型号id',
			width : 270,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'modelname',
			title : '焊机型号',
			width : 270,
			halign : "center",
			align : "left"
		}, {
			field : 'createdate',
			title : '创建日期',
			width : 270,
			halign : "center",
			align : "left"
		}, {
			field : 'status',
			title : '状态',
			width : 270,
			halign : "center",
			align : "left"
		}, {
			field : 'statusId',
			title : '状态id',
			width : 270,
			halign : "center",
			align : "left",
			hidden : true
		}, {
			field : 'edit',
			title : '编辑',
			width : 400,
			halign : "center",
			align : "left",
			formatter: function(value,row,index){
				var str = "";
				str += '<a id="wpslibgive" class="easyui-linkbutton" href="javascript:selectMainWps('+row.fid+','+row.manu+')"/>';
				str += '<a id="wpslibadd" class="easyui-linkbutton" href="javascript:addMainWps()"/>';
				str += '<a id="wpslibedit" class="easyui-linkbutton" href="javascript:editWpslib()"/>';
				str += '<a id="wpslibremove" class="easyui-linkbutton" href="javascript:openRemoveWpslib()"/>';
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
			$("a[id='wpslibgive']").linkbutton({text:'工艺库下发',plain:true,iconCls:'icon-setwps'});
			$("a[id='wpslibadd']").linkbutton({text:'新增工艺',plain:true,iconCls:'icon-newadd'});
	        $("a[id='wpslibedit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
	        $("a[id='wpslibremove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
		},
		detailFormatter:function(index,row2){
			return '<div id="div'+index+'"><table id="ddv-' + index + '" style="min-height:80px;"></table></div>';
		},
		onExpandRow: function(index,row){
			var ddv = $(this).datagrid('getRowDetail',index).find('#ddv-'+index);
			if(row.manu==149){
				ddv.datagrid({
					width: $("#div"+index).width,
					height: $("#div"+index).height,
					idField : 'id',
					pageSize : 10,
					pageList : [ 10, 20, 30, 40, 50 ],
					url : "wps/getSxWpsList?fwpslib_id="+row.fid,
					rownumbers : true,
					pagination : true,
					showPageList : false,
					singleSelect : true,
					columns : [ [ {
						field : 'fid',
						title : 'FID',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'fwpsnum',
						title : '通道号',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_ele_top',
						title : '预置电流上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_ele_bottom',
						title : '预置电流下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_vol_top',
						title : '预置电压上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_vol_bottom',
						title : '预置电压下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_vol1',
						title : '初期电流上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fweld_tuny_ele',
						title : '初期电流下限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_vol1',
						title : '初期电压上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fweld_tuny_vol',
						title : '初期电压下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fweld_vol1',
						title : '收弧电流上限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_tuny_ele',
						title : '收弧电流下限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_vol_top',
						title : '收弧电压上限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_tuny_vol',
						title : '收弧电压下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fmaterial',
						title : '材质',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'materialname',
						title : '材质',
						halign : "center",
						align : "left"
					}, {
						field : 'fdiameter',
						title : '丝径',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'dianame',
						title : '丝径',
						halign : "center",
						align : "left"
					}, {
						field : 'fgas',
						title : '气体',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'gasname',
						title : '气体',
						halign : "center",
						align : "left"
					}, {
						field : 'fcontroller',
						title : '焊接控制',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'fcontrollername',
						title : '焊接控制',
						halign : "center",
						align : "left"
					}, {
						field : 'farc',
						title : '脉冲有无',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'farcname',
						title : '脉冲有无',
						halign : "center",
						align : "left"
					}, {
						field : 'ftime',
						title : '点焊时间',
						halign : "center",
						align : "left"
					}, {
						field : 'selectname',
						title : '分别/一元',
						halign : "center",
						align : "left"
					},/* {
						field : 'ininame',
						title : '干伸长度',
						halign : "center",
						align : "left"
					},*/ {
						field : 'fselect',
						title : '分别/一元',
						halign : "center",
						align : "left",
						hidden : true
					},/* {
						field : 'finitial',
						title : '干伸长度',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'fweld_vol',
						title : '焊接上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fweld_ele',
						title : '焊接下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_ele',
						title : '初期上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_vol',
						title : '初期下限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_ele',
						title : '收弧上限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_vol',
						title : '收弧下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fadvance',
						title : '延时时间',
						halign : "center",
						align : "left"
					}, {
						field : 'fhysteresis',
						title : '修正周期',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_ele_warn_top',
						title : '预置电流报警上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_ele_warn_bottom',
						title : '预置电流报警下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_vol_warn_top',
						title : '预置电压报警上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fpreset_vol_warn_bottom',
						title : '预置电压报警下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_ele_warn_top',
						title : '初期电流报警上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_ele_warn_bottom',
						title : '初期电流报警下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_vol_warn_top',
						title : '初期电压报警上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fini_vol_warn_bottom',
						title : '初期电压报警下限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_ele_warn_top',
						title : '收弧电流报警上限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_ele_warn_bottom',
						title : '收弧电流报警下限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_vol_warn_top',
						title : '收弧电压报警上限',
						halign : "center",
						align : "left"
					}, {
						field : 'farc_vol_warn_bottom',
						title : '收弧电压报警下限',
						halign : "center",
						align : "left"
					}, */{
						field : 'farc_delay_time',
						title : '起弧延时时间',
						halign : "center",
						align : "left"
					}, {
						field : 'fwarn_delay_time',
						title : '报警延时时间',
						halign : "center",
						align : "left"
					},/* {
						field : 'fwarn_stop_time',
						title : '报警停机时间',
						halign : "center",
						align : "left"
					}, {
						field : 'charactername',
						title : '超规范报警停机',
						halign : "center",
						align : "left"
					}, {
						field : 'sxfcharacter',
						title : '超规范报警停机',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'fflow_top',
						title : '流量上限',
						halign : "center",
						align : "left"
					}, {
						field : 'fflow_bottom',
						title : '流量下限',
						halign : "center",
						align : "left"
					}, {
						field : 'fdelay_time',
						title : '延时时间',
						halign : "center",
						align : "left"
					}, {
						field : 'fover_time',
						title : '超限时间',
						halign : "center",
						align : "left"
					}, {
						field : 'ffixed_cycle',
						title : '修正周期',
						halign : "center",
						align : "left"
					},*/ {
						field : 'edit',
						title : '编辑',
						width : 200,
						halign : "center",
						align : "left",
						formatter : function(value, indexrow, index) {
							var str = "";
							str += '<a id="wpslibedit" class="easyui-linkbutton" href="javascript:editMainWps('+encodeURI(JSON.stringify(indexrow))+','+encodeURI(JSON.stringify(row))+')"/>';
							str += '<a id="wpslibremove" class="easyui-linkbutton" href="javascript:rmSxWps('+encodeURI(JSON.stringify(indexrow))+')"/>';
							return str;
						}
					} ] ],
					onResize:function(){
						$('#wpslibTable').datagrid('fixDetailRowHeight',index);
					},
					onLoadSuccess : function(data) {
						$("a[id='wpslibedit']").linkbutton({
							text : '修改',
							plain : true,
							iconCls : 'icon-update'
						});
						$("a[id='wpslibremove']").linkbutton({
							text : '删除',
							plain : true,
							iconCls : 'icon-delete'
						});
						if(sxTableFlag==0){
							$("#div"+index).height($("#div"+index).height()+20);
							sxTableFlag++;
						}
						$("#ddv-"+index).datagrid('resize', {
							height : $("#div"+index).height(),
							width : $("#div"+index).width()
						});
					},
					rowStyler : function(index, row) {
						if ((index % 2) != 0) {
							//处理行代背景色后无法选中
							var color = new Object();
							return color;
						}
					}
				});
			}else{
				ddv.datagrid({
					fitColumns : true,
					width: $("#div"+index).width,
					height: $("#div"+index).height,
					idField : 'id',
					pageSize : 30,
					pageList : [ 10, 20, 30, 40, 50 ],
					url : "wps/getMainwpsList?parent="+row.fid,
					singleSelect : true,
					rownumbers : true,
					showPageList : false,
					columns : [ [ { 
						field : 'fid',
						title : 'id',
						halign : "center",
//						width : 30,
						align : "left",
						hidden:true
					},{ 
						field : 'fchanel',
						title : '通道号',
						halign : "center",
						align : "left"
					},{ 
						field : 'finitial',
						title : '初期条件',
						halign : "center",
						align : "left"
					},{ 
						field : 'initial',
						title : '初期条件value值',
						halign : "center",
						align : "left",
						hidden : true
					},{ 
						field : 'fcontroller',
						title : '熔深控制',
						halign : "center",
						align : "left"
					},{ 
						field : 'controller',
						title : '熔深控制value值',
						halign : "center",
						align : "left",
						hidden : true
					},{ 
						field : 'ftorch',
						title : '水冷焊枪',
						halign : "center",
						align : "left"
					}, { 
						field : 'torch',
						title : '水冷焊枪value值',
						halign : "center",
						align : "left",
						hidden : true
					},{
						field : 'fselectname',
						title : '一元/个别',
						halign : "center",
						align : "left"
					}, {
						field : 'fselect',
						title : '一元/个别id',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'farc',
						title : '收弧id',
						halign : "center",
						align : "left",
						hidden: true
					},{
						field : 'farcname',
						title : '收弧',
						halign : "center",
						align : "left"
					}, {
						field : 'fcharacter',
						title : '电弧特性',
						halign : "center",
						align : "left"
					}, {
						field : 'fmode',
						title : '柔软电弧模式',
						halign : "center",
						align : "left"
					} ,{
						field : 'mode',
						title : '柔软电弧模式value值',
						halign : "center",
						align : "left",
						hidden : true
					}, {
						field : 'fmaterialname',
						title : '焊丝材质',
						halign : "center",
						align : "left"
					},{
						field : 'fmaterial',
						title : '焊丝材质id',
						halign : "center",
						align : "left",
						hidden : true
					},{
						field : 'fgasname',
						title : '气体',
						halign : "center",
						align : "left"
					},{
						field : 'fgas',
						title : '气体id',
						halign : "center",
						align : "left",
						hidden : true
					},{
						field : 'fdiametername',
						title : '焊丝直径',
						halign : "center",
						align : "left"
					},{
						field : 'fdiameter',
						title : '焊丝直径id',
						halign : "center",
						align : "left",
						hidden : true
					},{
						field : 'fprocessname',
						title : '焊接过程',
						halign : "center",
						align : "left"
					},{
						field : 'fweldprocess',
						title : '焊接过程id',
						halign : "center",
						align : "left",
						hidden : true
					},{
						field : 'ftime',
						title : '点焊时间',
						halign : "center",
						align : "left"
					},{
						field : 'fadvance',
						title : '提前送气',
						halign : "center",
						align : "left"
					},{
						field : 'fhysteresis',
						title : '滞后送气',
						halign : "center",
						align : "left"
					},{
						field : 'fini_ele',
						title : '初期电流',
						halign : "center",
						align : "left"
					},{
						field : 'fini_vol',
						title : '初期电压',
						halign : "center",
						align : "left"
					},{
						field : 'fini_vol1',
						title : '初期电压一元',
						halign : "center",
						align : "left"
					},{
						field : 'fweld_ele',
						title : '焊接电流',
						halign : "center",
						align : "left"
					},{
						field : 'fweld_vol',
						title : '焊接电压',
						halign : "center",
						align : "left"
					},{
						field : 'fweld_vol1',
						title : '焊接电压一元',
						halign : "center",
						align : "left"
					},{
						field : 'farc_ele',
						title : '收弧电流',
						halign : "center",
						align : "left"
					},{
						field : 'farc_vol',
						title : '收弧电压',
						halign : "center",
						align : "left"
					},{
						field : 'farc_vol1',
						title : '收弧电压一元',
						halign : "center",
						align : "left"
					},{
						field : 'fweld_tuny_ele',
						title : '焊接电流微调',
						halign : "center",
						align : "left"
					},{
						field : 'fweld_tuny_vol',
						title : '焊接电压微调',
						halign : "center",
						align : "left"
					},{
						field : 'farc_tuny_ele',
						title : '收弧电流微调',
						halign : "center",
						align : "left"
					},{
						field : 'farc_tuny_vol',
						title : '收弧电压微调',
						halign : "center",
						align : "left"
					},{
						field : 'fwarn_ele_up',
						title : '报警电流上限',
						halign : "center",
						align : "left"
					},{
						field : 'fwarn_ele_down',
						title : '报警电流下限',
						halign : "center",
						align : "left"
					},{
						field : 'fwarn_vol_up',
						title : '报警电压上限',
						halign : "center",
						align : "left"
					},{
						field : 'fwarn_vol_down',
						title : '报警电压下限',
						halign : "center",
						align : "left"
					},{
						field : 'edit',
						title : '编辑',
						width : 150,
						halign : "center",
						align : "left",
						formatter: function(value,indexrow,index){
							var str = "";
							str += '<a id="mainwpsedit" class="easyui-linkbutton" href="javascript:editMainWps('+encodeURI(JSON.stringify(indexrow))+','+encodeURI(JSON.stringify(row))+')"/>';
							str += '<a id="mainwpsremove" class="easyui-linkbutton" href="javascript:openRemoveMainWps('+encodeURI(JSON.stringify(indexrow))+')"/>';
							return str;
						}
					}
					] ],
					pagination : true,
					onResize:function(){
						$('#wpslibTable').datagrid('fixDetailRowHeight',index);
					},
					onLoadSuccess:function(){
						var wpslibrow = $('#wpslibTable').datagrid("getSelected");
						if(wpslibrow.model==171||wpslibrow.model==172||wpslibrow.model==173){
							$("#ddv-"+index).datagrid('hideColumn', 'ftorch')
						}else{
							$("#ddv-"+index).datagrid('hideColumn', 'fmode')
						}
						$('#wpslibTable').datagrid("selectRow", index)
						setTimeout(function(){
							$('#wpslibTable').datagrid('fixDetailRowHeight',index);
							$('#wpslibTable').datagrid('fixRowHeight',index);
						},0);
				        $("a[id='mainwpsedit']").linkbutton({text:'修改',plain:true,iconCls:'icon-update'});
				        $("a[id='mainwpsremove']").linkbutton({text:'删除',plain:true,iconCls:'icon-delete'});
						if(otcTableFlag==0){
							$("#div"+index).height($("#div"+index).height()+20);
							otcTableFlag++;
						}
						$("#ddv-"+index).datagrid('resize', {
							height : $("#div"+index).height(),
							width : $("#div"+index).width()
						});
					}
				});
			}
			$('#wpslibTable').datagrid('fixDetailRowHeight',index);
		}
	});
}

function closedlg(){
	if(!$("#smdlg").parent().is(":hidden")){
		$('#smdlg').window('close');
	}
	if(!$("#smwdlg").parent().is(":hidden")){
		$('#smwdlg').window('close');
	}
	if(!$("#mwdlg").parent().is(":hidden")){
		$('#mwdlg').window('close');
	}
	if(!$("#rmmwdlg").parent().is(":hidden")){
		$('#rmmwdlg').window('close');
	}
	if(!$("#wltdlg").parent().is(":hidden")){
		$('#wltdlg').window('close');
	}
	if(!$("#rmwltdlg").parent().is(":hidden")){
		$('#rmwltdlg').window('close');
	}
	if(!$("#editSxDlg").parent().is(":hidden")){
		$('#editSxDlg').window('close');
	}
	if(!$("#sxMachinedlg").parent().is(":hidden")){
		$('#sxMachinedlg').window('close');
	}
	if(!$("#sxSelectdlg").parent().is(":hidden")){
		$('#sxSelectdlg').window('close');
	}
	if(!$("#resultdlg").parent().is(":hidden")){
		$('#resultdlg').window('close');
	}
}

function sxDefault(){
	$("#sxfweld_vol").numberbox('setValue', 1234);
	$("#sxfweld_ele").numberbox('setValue', 1234);
	$("#sxfini_ele").numberbox('setValue', 1234);
	$("#sxfini_vol").numberbox('setValue', 1234);
	$("#sxfarc_ele").numberbox('setValue', 1234);
	$("#sxfarc_vol").numberbox('setValue', 1234);
	$("#sxfadvance").numberbox('setValue', 10);
	$("#sxfhysteresis").numberbox('setValue', 10);
	$("#sxfinitial").combobox('setValue', 0);
	$("#sxfflow_top").numberbox('setValue', 12.3);
	$("#sxfflow_bottom").numberbox('setValue', 12.3);
	$("#sxfdelay_time").numberbox('setValue', 12.3);
	$("#sxfover_time").numberbox('setValue', 12.3);
	$("#sxffixed_cycle").numberbox('setValue', 12.3);
	$("#sxfwarn_stop_time").numberbox('setValue', 12.3);
/*	$("#sxfpreset_ele_warn_top").numberbox('setValue', 1234);
	$("#sxfpreset_vol_warn_top").numberbox('setValue', 543.2);
	$("#sxfpreset_ele_warn_bottom").numberbox('setValue', 1234);
	$("#sxfpreset_vol_warn_bottom").numberbox('setValue', 543.2);*/
	
	$("#sxfini_ele_warn_top").numberbox('setValue', 1234);
	$("#sxfini_vol_warn_top").numberbox('setValue', 543.2);
	$("#sxfini_ele_warn_bottom").numberbox('setValue', 1234);
	$("#sxfini_vol_warn_bottom").numberbox('setValue', 543.2);
	$("#sxfarc_ele_warn_top").numberbox('setValue', 1234);
	$("#sxfarc_vol_warn_top").numberbox('setValue', 543.2);
	$("#sxfarc_ele_warn_bottom").numberbox('setValue', 1234);
	$("#sxfarc_vol_warn_bottom").numberbox('setValue', 543.2);
}

//打开历史查询窗口
function openHistorydlg(){
	$("#wmhistorydlg").dialog({
	    onClose: function () {
	    	$('#machineNum').numberbox('clear');
	    	$('#theWpslibName').textbox('clear');
	    	getOldTime();
	    	getNewTime();
	    	$('#historyTable').datagrid('loadData',{total:0,rows:[]});
	    	chartStr = "";
	    }
	});
	$('#wmhistorydlg').window( {
		title : "历史下发查询",
		modal : true
	});
	$('#wmhistorydlg').window("open");
	historyTable();
}

var chartStr = "";
function setParam(){
	var dtoTime1 = $("#dtoTime1").datetimebox('getValue');
	var dtoTime2 = $("#dtoTime2").datetimebox('getValue');
	var machineNum = $("#machineNum").numberbox('getValue');
	var wpslibName = $("#theWpslibName").textbox('getValue');
	chartStr += "?machineNum="+machineNum+"&wpslibName="+encodeURI(wpslibName)+"&dtoTime1="+dtoTime1+"&dtoTime2="+dtoTime2;
}

function historyTable(){
	setParam();
	$("#historyTable").datagrid( {
		fitColumns : true,
		height : $("#wmhistorydlg").height()*0.9,
		width : $("#wmhistorydlg").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],  
		url : "wps/getWpslibMachineHistory"+chartStr,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		showPageList : false,
		columns : [ [ {
			field : 'fid',
			title : 'fid',
			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		} ,{
			field : 'machineNum',
			title : '焊机编号',
			width : 100,
			halign : "center",
			align : "left"
		} ,{
			field : 'machineModel',
			title : '焊机型号',
			width : 100,
			halign : "center",
			align : "left",
			hidden : true
		} ,{
			field : 'wpslibName',
			title : '工艺库名称',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'chanel',
			title : '通道号',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'updateTime',
			title : '下发时间',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'weld_ele',
			title : '焊接电流',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'weld_vol',
			title : '焊接电压',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'warn_ele_up',
			title : '报警电流上限',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'warn_ele_down',
			title : '报警电流下限',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'warn_vol_up',
			title : '报警电压上限',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'warn_vol_down',
			title : '报警电压下限',
			width : 100,
			halign : "center",
			align : "left"
		}, {
			field : 'back',
			title : '备注',
			width : 100,
			halign : "center",
			align : "left",
			formatter:function(value,row,index){
			var str = "";
			str += '<a id="fdetail" class="easyui-linkbutton" href="javascript:getDetail('+encodeURI(JSON.stringify(row))+')"/>';
			return str; 
			}
		}]],
		nowrap : false,
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
		},
		onLoadSuccess:function(data){
	        $("a[id='fdetail']").linkbutton({text:'参数详情',plain:true});
	    }
	});
}

function getDetail(row){
	$.ajax({
		type : "post",
		async : false,
		url : "wps/getSpeDetail?machineId="+row.fid+"&machineModel="+row.machineModel+"&chanel="+row.chanel+"&time="+row.updateTime,
		data : {},
		dataType : "json", //返回数据形式为json  
		success : function(result) {
			if (result) {
				if(row.machineModel==171){
					$("#mwdlg").dialog({
					    onClose: function () {
							$("#otcsaveWpsBut").show();
							$("#otcgetWpsBut").show();
					    }
					});
					CPVEWINIT();
					comboboxCheck(row.model);
					$('#mwfm').form('clear');
					$('#mwdlg').window({
						title : "参数详情",
						modal : true
					});
					$("#otcsaveWpsBut").hide();
					$("#otcgetWpsBut").hide();
					$('#mwdlg').window('open');
					$('#mwfm').form('load', result.rows[0]);
					if (encodeURI(result.rows[0].initial) == "1") {
						$("#finitial").prop("checked", true);
					}
					if (encodeURI(result.rows[0].mode) == "1") {
						$("#fmode").prop("checked", true);
					}
					if (encodeURI(result.rows[0].controller) == "1") {
						$("#fcontroller").prop("checked", true);
					}
					if (encodeURI(result.rows[0].torch) == "1") {
						$("#ftorch").prop("checked", true);
					}
				}else{
					$('#editSxDlg').window( {
						title : "参数详情",
						modal : true
					});
					$("#sxRemoveWpsBut").hide();
					$("#sxgetWpsBut").show();
					$("#sxSaveWpsBut").show();
					$("#sxgetWpsBut").hide();
					$("#sxSaveWpsBut").hide();
					$('#editSxDlg').window('open');
					$('#sxfm').form('load', result.rows[0]);
					$("input[name='sxfcharacter']").eq(result.rows[0].sxfcharacter).prop("checked", true);
				}
			}
		},
		error : function(errorMsg) {
			alert("数据请求失败，请联系系统管理员!");
		}
	});
}

//搜索
function searchHistory(){
	chartStr = "";
	setTimeout(function(){
		historyTable();
	},500);
}

//监听窗口大小变化
window.onresize = function() {
	setTimeout(domresize(), 500);
}

//改变表格高宽
function domresize() {
	$("#wpslibTable").datagrid('resize', {
		height : $("#body").height(),
		width : $("#body").width()
	});
}