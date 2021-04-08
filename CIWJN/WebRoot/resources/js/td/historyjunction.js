$(function(){
	insframeworkTree();
	dgDatagrid();
});

function dgDatagrid(){
	$("#dg").datagrid({
		height : $("#body").height()+30,
		width : $("#body").width(),
		idField : 'id',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url : "weldedjunction/getWeldedJunctionList?searchStr="+"j.fid in (SELECT DISTINCT ftaskid FROM tb_taskresult)",
		singleSelect : true,
		rownumbers : true,
		showPageList : false,
		pagination : true,
		fitColumns : true,
		columns : [ [ {
			field : 'id',
			title : '序号',
			halign : "center",
			align : "left",
			hidden:true
		}, {
			field : 'weldedJunctionno',
			title : '任务编号',
			width : 90,
			halign : "center",
			align : "left"
		}, {
			field : 'itemid',
			title : '项目id',
			halign : "center",
			align : "left",
			hidden:true
		},{
			field : 'itemname',
			title : '所属项目',
			width : 120,
			halign : "center",
			align : "left"
		}, {
			field : 'edit',
			title : '操作',
			width : 130,
			halign : "center",
			align : "left",
			formatter:function(value,row,index){
			var str = "";
			str += '<a id="wj" class="easyui-linkbutton" href="weldedjunction/getWeldJun?wjno='+encodeURI(row.weldedJunctionno)+'"/>';
			return str;
			}
		}] ],
		rowStyler: function(index,row){
            if ((index % 2)!=0){
            	//处理行代背景色后无法选中
            	var color=new Object();
                return color;
            }
        },
		onLoadSuccess:function(data){
	        $("a[id='wj']").linkbutton({text:'任务曲线',plain:true,iconCls:'icon-search'});
	    }
    });
}
                      
function insframeworkTree(){
	$("#myTree").tree({  
		onClick : function(node){
			$("#dg").datagrid('load',{
				"searchStr" : "i.fid="+node.id
			})
		 }
	})
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