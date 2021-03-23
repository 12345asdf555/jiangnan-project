package com.sshome.ssmcxf.webservice;

import java.util.List;

import com.spring.dto.ModelDto;
import com.spring.dto.WeldDto;

public interface LiveDataWebService {
	/**
	 * 鏌ヨ浜嬩笟閮ㄧ剨鎺ュ伐鏃�
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param parent 鐖秈d
	 * @return
	 */
	Object getCausehour(String object);
	
	/**
	 * 鏌ヨ鍏徃鐒婃帴宸ユ椂
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param parent鐖秈d
	 * @return
	 */
	Object getCompanyhour(String object);
	
	/**
	 * 椤圭洰閮ㄧ剨鎺ュ伐鏃�
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getItemhour(String object);
	
	/**
	 * 鐒婂彛鐒婃帴宸ユ椂
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getJunctionHous(String object);
	
	/**
	 * 浜嬩笟閮ㄥ伐鑹鸿秴鏍囩粺璁�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param parent 鐖秈d
	 * @return
	 */
	Object getCauseOverproof(String object);
	
	/**
	 * 椤圭洰閮ㄥ伐鑹鸿秴鏍囩粺璁�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param id 椤圭洰id
	 * @return
	 */
	Object getItemOverproof(String object);
	
	/**
	 * 鑾峰彇褰撳墠鎵�鍖呭惈鐨勯」鐩�
	 * @param parent 涓婄骇椤圭洰id
	 * @return
	 */
	Object getAllInsf(String object);
	
	/**
	 * 鑾峰彇褰撳墠璺ㄥ害鎵�鍖呭惈鐨勬椂闂�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getAllTime(String object);
	
	/**
	 * 鍏徃宸ヨ壓瓒呮爣缁熻
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getCompanyOverproof(String object);
	
	/**
	 * 鑾峰彇鍏徃瓒呮椂寰呮満缁熻
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param num 瓒呮椂鐐�
	 * @return
	 */
	Object getcompanyOvertime(String object);
	
	/**
	 * 鑾峰彇浜嬩笟閮ㄨ秴鏃跺緟鏈虹粺璁�
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param num瓒呮椂鐐�
	 * @param parent涓婄骇id
	 * @return
	 */
	Object getCaustOvertime(String object);
	
	/**
	 * 鑾峰彇椤圭洰閮ㄨ秴鏃跺緟鏈虹粺璁�
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param num瓒呮椂鐐�
	 * @param parent涓婄骇id
	 * @return
	 */
	Object getItemOvertime(String object);
	
	/**
	 * 鑾峰彇鎵�鏈夌剨鍙�
	 * @param parent 鎵�灞為」鐩甶d
	 * @return
	 */
	Object getJunction(String object);
	
	/**
	 * 寰呮満鏄庣粏
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param num瓒呮椂鐐�
	 * @param parent 椤圭洰id
	 * @return
	 */
	Object getDetailovertime(String object);
	
	/**
	 * 鍏徃璐熻嵎鐜�
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getCompanyLoads(String object);
	
	/**
	 * 浜嬩笟閮ㄨ礋鑽风巼
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param parent涓婄骇id
	 * @return
	 */
	Object getCaustLoads(String object);
	
	/**
	 * 椤圭洰閮ㄨ礋鑽风巼
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param parent涓婄骇id
	 * @return
	 */
	Object getItemLoads(String object);
	
	/**
	 * 鑾峰彇鎵�鏈夌剨鏈�
	 * @param parent 椤圭洰id
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getMachine(String object);
	
	/**
	 * 鑾峰彇璐熻嵎鐜囨槑缁�
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param machineno鐒婃満缂栧彿
	 * @return
	 */
	Object getDetailLoads(String object);
	
	/**
	 * 鑾峰彇鍏徃绌鸿浇鐜�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getCompanyNoLoads(String object);

	/**
	 * 鑾峰彇浜嬩笟閮ㄧ┖杞界巼
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param parent 鐖秈d
	 * @return
	 */
	Object getCaustNOLoads(String object);
	
	/**
	 * 鑾峰彇椤圭洰閮ㄧ┖杞界巼
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param parent 鐖秈d
	 * @return
	 */
	Object getItemNOLoads(String object);
	
	/**
	 * 鍏徃闂茬疆鐜�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getCompanyIdle(String object);
	
	/**
	 * 浜嬩笟閮ㄩ棽缃巼
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param parent涓婄骇id
	 * @return
	 */
	Object getCaustIdle(String object);
	
	/**
	 * 椤圭洰閮ㄩ棽缃巼
	 * @param dto鎵╁睍鍙傛暟绫�
	 * @param itemid椤圭洰id
	 * @return
	 */
	Object getItemIdle(String object);
	
	/**
	 * 鑾峰彇椤圭洰鎵�鏈夌剨鏈烘暟閲�
	 * @param id 椤圭洰id
	 * @return
	 */
	int getMachineCount(String object);
	
	/**
	 * 鍏徃鍗曞彴璁惧杩愯鏁版嵁缁熻
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param parent 涓婄骇id
	 * @return
	 */
	Object getCompanyUse(String object);
	
	/**
	 * 浜嬩笟閮ㄥ崟鍙拌澶囪繍琛屾暟鎹粺璁�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param insid 椤圭洰id
	 * @return
	 */
	Object getCaustUse(String object);
	

	/**
	 * 椤圭洰閮ㄥ崟鍙拌澶囪繍琛屾暟鎹粺璁�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param insid 椤圭洰id
	 * @return
	 */
	Object getItemUse(String object);
	
	/**
	 * 闆嗗洟鐒婃帴宸ユ椂
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getBlochour(String object);
	
	/**
	 * 闆嗗洟瓒呮爣缁熻
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getBlocOverproof(String object);
	
	/**
	 * 闆嗗洟瓒呮椂寰呮満缁熻
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param num 瓒呮椂鐐�
	 * @return
	 */
	Object getBlocOvertime(String object);
	
	/**
	 * 闆嗗洟璐熻浇鐜�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getBlocLoads(String object);
	
	/**
	 * 闆嗗洟绌鸿浇鐜�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getBlocNoLoads(String object);
	
	/**
	 * 闆嗗洟闂茬疆鐜�
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object getBlocIdle(String object);
	
	/**
	 * 闆嗗洟鍗曞彴璁惧杩愯鏁版嵁缁熻
	 * @param dto  鎵╁睍鍙傛暟绫�
	 * @param parent 涓婄骇鐨勭埗id
	 * @return
	 */
	Object getBlocUse(String object);
	
	/**
	 * 鑾峰彇闆嗗洟涓嬬殑鍏徃
	 * @return
	 */
	Object getBlocChildren();
	
	/**
	 * 浜嬩笟閮ㄥ伐鏁�
	 * @param page 鍒嗛〉
	 * @param parent 鐖秈d
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object caustEfficiency(String object);
	
	/**
	 * 鍏徃宸ユ晥
	 * @param page 鍒嗛〉
	 * @param parent 鐖秈d
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object companyEfficiency(String object);
	
	/**
	 * 闆嗗洟宸ユ晥
	 * @param page 鍒嗛〉
	 * @param parent 鐖秈d
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @return
	 */
	Object blocEfficiency(String object);
	
	/**
	 * 鑾峰彇宸ユ晥鍥捐〃鏁版嵁
	 */
	Object getEfficiencyChart(String object);
	
	/**
	 * 鑾峰彇鐒婂彛鍒嗙被
	 * @param parent 鐖秈d
	 * @param material 鏉愯川
	 * @param external_diameter 澶栧緞
	 * @param wall_thickness 鐠у帤
	 * @param nextExternal_diameter 涓嬫父澶栧緞
	 * @return
	 */
	Object getHousClassify(String object);
	
	Object getDetailNoLoads(String object);
	

	/**
	 * 鏌ヨ瀹炴椂鏁版嵁闆嗗洟鐒婃満鏁伴噺
	 * @param dto 鎵╁紶鍙傛暟绫�
	 * @param parent 鍏徃id
	 * @return
	 */
	Object getBlocMachineCount(String object);

	/**
	 * 鏌ヨ瀹炴椂鏁版嵁鍏徃鐒婃満鏁伴噺
	 * @param dto 鎵╁紶鍙傛暟绫�
	 * @param parent 鍏徃id
	 * @return
	 */
	Object getCompanyMachineCount(String object);

	/**
	 * 鏌ヨ瀹炴椂鏁版嵁浜嬩笟閮�/椤圭洰閮ㄧ剨鏈烘暟閲�
	 * @param dto 鎵╁紶鍙傛暟绫�
	 * @param parent 鍏徃id
	 * @return
	 */
	Object getCaustMachineCount(String object);
	
	/**
	 * 鏍规嵁缁勭粐鏈烘瀯鍙婃椂闂寸偣鑾峰彇宸ヤ綔鎬绘椂闀�
	 * @param parent 缁勭粐鏈烘瀯id
	 * @param time 鏃堕棿鐐�
	 * @param mid 鐒婃満id
	 * @return
	 */
	Object getCountByTime(String object);
	
	/**
	 * 鏍规嵁鐒婂伐鑾峰彇鐒婂彛
	 * @param dto 鎵╁睍鍙傛暟绫�
	 * @param welder 鐒婂伐缂栧彿
	 * @return
	 */
	Object getJunctionByWelder(String object);
	
	/**
	 * 鑾峰彇瓒呮爣鍥炴函
	 * @param time 瓒呮爣鏃堕棿
	 * @param welder 鐒婂伐
	 * @param jucntion 鐒婂彛
	 * @return
	 */
	Object getExcessiveBack(String object);
	
	/**
	 * 得到焊机待机时间和总时间
	 * @param dto 截止当前时间为止的一个小时内
	 * @return
	 */
	Object getStandbytimeout(String object);
}
