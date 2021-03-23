function importConfigFile(){
	//获取读取我文件的File对象
	var flag=0;
	var file = $("#file").val();
	if(file == null || file == ""){
		$.messager.alert("提示", "请选择要上传的文件！");
		return;
	}else{
		if(!+[1,]){
			$('#importfm').form('submit', {
				url : "configfile/uploadConfigFile",
				success : function(result) {
					if(result){
						var result = eval('(' + result + ')');
						if (!result.success) {
							$.messager.show( {
								title : 'Error',
								msg : result.msg
							});
						} else {
							$.messager.alert("提示", result.msg);
						}
					}
					
				},  
			    error : function(errorMsg) {  
			        alert("数据请求失败，请联系系统管理员!");  
			    } 
			});
			return;
		}
		var selectedFile = document.getElementById('file').files
	    if(selectedFile.length){
		    selectedFile = selectedFile[0];
	/*	    var size = selectedFile.size;//读取选中文件的大小
		    console.log("文件名:"+name+"大小:"+size);*/
		
		    var reader = new FileReader();//这是核心,读取操作就是由它完成.
		    reader.readAsText(selectedFile);//读取文件的内容,也可以读取文件的URL
		    reader.onload = function () {
		        //当读取完成后回调这个函数,然后此时文件的内容存储到了result中,直接操作即可
		        var test = this.result;
		        var err = validateXML(test);
		        if(err.msg=="格式正确"){
		        	save();
		        }else{
		        	alert(err.msg);
		        	return;
		        }
		    }
	    }
	}
}

//检测导入xml文件是否缝合规范
function validateXML(xmlContent){ 
	//errorCode 0是xml正确，1是xml错误，2是无法验证 
	var xmlDoc,errorMessage,errorCode = 0; 
	// code for IE 
	if (window.ActiveXObject){
	    xmlDoc  = new ActiveXObject("Microsoft.XMLDOM"); 
	    xmlDoc.async="false"; 
	    xmlDoc.loadXML(xmlContent);
	    if(xmlDoc.parseError.errorCode!=0){ 
	    	errorMessage="错误位置: " + xmlDoc.parseError.line;
	/*     	errorMessage="错误code: " + xmlDoc.parseError.errorCode + "\n"; 
	        errorMessage=errorMessage+"错误原因: " + xmlDoc.parseError.reason; 
	        errorMessage=errorMessage+"错误位置: " + xmlDoc.parseError.line;  */
	        errorCode = 1; 
	    }else{ 
        errorMessage = "格式正确"; 
	    } 
	 // code for Mozilla, Firefox, Opera, chrome, safari,etc. 
	}else if (document.implementation.createDocument){ 
	    var parser=new DOMParser(); 
	    xmlDoc = parser.parseFromString(xmlContent,"text/xml"); 
	    var error = xmlDoc.getElementsByTagName("parsererror"); 
	    if (error.length > 0){ 
           if(xmlDoc.documentElement.nodeName=="parsererror"){ 
        	   errorCode = 1; 
        	   errorMessage = xmlDoc.documentElement.childNodes[0].nodeValue; 
           }else{ 
        	   errorCode = 1; 
        	   errorMessage = xmlDoc.getElementsByTagName("parsererror")[0].innerHTML; 
           } 
	    }else{ 
	    	errorMessage = "格式正确"; 
	    } 
	}else{ 
	    errorCode = 2; 
	    errorMessage = "浏览器不支持验证，无法验证xml正确性"; 
	} 
	return { 
	    "msg":errorMessage,  
	    "error_code":errorCode 
	}; 
};

//上传并保存xml文件
function save(){
	var file = $("#file").val();
	$('#importfm').form('submit', {
		url : "configfile/uploadConfigFile",
		success : function(result) {
			if(result){
				var result = eval('(' + result + ')');
				if (!result.success) {
					$.messager.show( {
						title : 'Error',
						msg : result.msg
					});
				} else {
					$('#importdiv').dialog('close');
					$('#configfileTable').datagrid('reload');
					$.messager.alert("提示", result.msg);
				}
			}
			
		},  
	    error : function(errorMsg) {  
	        alert("数据请求失败，请联系系统管理员!");  
	    } 
	});		
}