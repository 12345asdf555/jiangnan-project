$(function(){
	$.extend($.fn.tree.methods, {
	    getLevel:function(jq,target){
	        var l = $(target).parentsUntil("ul.tree","ul");
	        return l.length+1;
	    }
	});
	insftrameworkTree();
})
function insftrameworkTree(){
	 $("#speTree").tree({  
         url:'insframework/getMachine',//请求路径
         onLoadSuccess:function(node,data){  
              var tree = $(this);  
              if(data){  
                  $(data).each(function(index,d) {  
                      if (this.state=='closed') {  
                          tree.tree('expandAll');  
                      }
                      $('#_easyui_tree_1 .tree-icon').css("background", "url(resources/images/menu_1.png) no-repeat center center");
  					var nownodes = $('#speTree').tree('find', data[0].id);
  					//判断是否拥有子节点,改变子节点图标
  					if (nownodes.children != null) {
  						for(var i=0;i<nownodes.children.length;i++){
  							var nextnodes1 = nownodes.children[i];
  							$('#' + nextnodes1.domId + ' .tree-icon').css("background", "url(resources/images/menu_2.png) no-repeat center center");
  							for(var j=0;j<nextnodes1.children.length;j++){
  								var nextnodes2 = nextnodes1.children[j];
  								$('#' + nextnodes2.domId + ' .tree-icon').css("background", "url(resources/images/menu_3.png) no-repeat center center");
  								for(var x=0;x<nextnodes2.children.length;x++){
  									var nextnodes3 = nextnodes2.children[x];
  									$('#' + nextnodes3.domId + ' .tree-icon').css("background", "url(resources/images/menu_3.png) no-repeat center center");
  									if(nextnodes3.children!=null && nextnodes3.children!=""){
  	  									for(var y=0;y<nextnodes3.children.length;y++){
  	  	  									var nextnodes4 = nextnodes3.children[y];
  	  	  									$('#' + nextnodes4.domId + ' .tree-icon').css("background", "url(resources/images/menu_4.png) no-repeat center center");
  	  	  								}
  									}
  								}
  							}
  							
  						}
  					}
                  });  
              }  
         }
     });
}