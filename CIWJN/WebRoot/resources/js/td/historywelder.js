/**
 * 焊工历史曲线
 */
$(function () {
    var width = $("#treeDiv").width();
    $(".easyui-layout").layout({
        onCollapse: function () {
            $("#dg").datagrid({
                height: $("#body").height(),
                width: $("#body").width()
            })
        },
        onExpand: function () {
            $("#dg").datagrid({
                height: $("#body").height(),
                width: $("#body").width()
            })
        }
    });
    insframeworkTree();
});

$(function () {
    $("#dg").datagrid({
        height: $("#body").height() + 30,
        width: $("#body").width(),
        idField: 'id',
        pageSize: 10,
        pageList: [10, 20, 30, 40, 50],
        url: "welders/getAllWelder",
        singleSelect: true,
        rownumbers: true,
        showPageList: false,
        pagination: true,
        fitColumns: true,
        columns: [[{
            field: 'id',
            title: '序号',
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'name',
            title: '姓名',
            width: 80,
            halign: "center",
            align: "left"
        }, {
            field: 'welderno',
            title: '焊工编号',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'cardnum',
            title: '卡号',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'quali',
            title: '资质id',
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'owner',
            title: '部门id',
            halign: "center",
            align: "left",
            hidden: true
        }, {
            field: 'ownername',
            title: '部门',
            width: 100,
            halign: "center",
            align: "left"
        }, {
            field: 'edit',
            title: '操作',
            width: 120,
            halign: "center",
            align: "left",
            formatter: function (value, row, index) {
                var str = "";
                str += '<a id="wj" class="easyui-linkbutton" href="weldedjunction/getWeldJun?fid=' + row.welderno + '"/>';
                return str;
            }
        }]],
        toolbar: '#welderTable_btn',
        rowStyler: function (index, row) {
            if ((index % 2) != 0) {
                //处理行代背景色后无法选中
                var color = new Object();
                return color;
            }
        },
        onLoadSuccess: function (data) {
            $("a[id='wj']").linkbutton({text: '焊工曲线', plain: true, iconCls: 'icon-search'});
        }
    });
});

function insframeworkTree() {
    $("#myTree").tree({
        onClick: function (node) {
            $("#dg").datagrid('load', {
                "parent": node.id
            });
        }
    });
}

//监听窗口大小变化
window.onresize = function () {
    setTimeout(domresize, 500);
}

//改变表格高宽
function domresize() {
    $("#dg").datagrid('resize', {
        height: $("#body").height(),
        width: $("#body").width()
    });
}