<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div region="west"  hide="true"  split="true" title="组织机构菜单" id="treeDiv" style="background: witch; width:19%;" fit=“true”>
  	<div class="easyui-layout" fit="true">
		<div region="south" split="true" border="false" style="height:20%;">
			<ul>
				<li style="font-size:190%;">
					<img width="10%" height="15%" src="resources/images/weld_04.png">&nbsp;&nbsp;工作设备：<span id="work">0</span>
				</li>
				<li style="font-size:190%;">
					<img width="10%" height="15%" src="resources/images/weld_03.png">&nbsp;&nbsp;待机设备：<span id="standby">0</span>
				</li>
				<li style="font-size:190%;">
					<img width="10%" height="15%" src="resources/images/weld_02.png">&nbsp;&nbsp;故障设备：<span id="warn">0</span>
				</li>
				<li style="font-size:190%;">
					<img width="10%" height="15%" src="resources/images/weld_05.png">&nbsp;&nbsp;关机设备：<span id="off">0</span>
				</li>
			</ul>
		</div>
		<div region="center" border="false">
		<ul id="myTree" class="easyui-tree"></ul>
		</div>
	</div>
</div>