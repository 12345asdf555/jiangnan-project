package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.WeldDto;
import com.spring.model.User;
import com.spring.model.Wps;
import com.spring.page.Page;

public interface WpsService {
	List<Wps> findAll(Page page, BigInteger parent,String str);
	List<Wps> findAllSpe(BigInteger machine,BigInteger chanel);
	List<Wps> findSpe(BigInteger machine,String ch);
	List<Wps> findHistory(Page page, BigInteger parent);
	List<Wps> AllSpe(BigInteger machine,String ch);
	void give(Wps wps);
	BigInteger findByUid(long uid);
	void save(Wps wps);
	void update(Wps wps);
	int getUsernameCount(String fwpsnum);
	Wps findById(BigInteger fid);
	void delete(BigInteger fid);
	String findIpById(BigInteger fid);
	void deleteHistory(BigInteger fid);
	Wps findSpeById(BigInteger fid);
	int findCount(BigInteger machine, String string);
	void saveSpe(Wps wps);
	void updateSpe(Wps wps);
	List<Wps> getWpslibList(Page page, String search);
	List<Wps> getMainwpsList(Page page, BigInteger parent);
	int getWpslibNameCount(String wpsName);
	void saveWpslib(Wps wps);
	void updateWpslib(Wps wps);
	List<Wps> getWpslibStatus();
	void deleteWpslib(BigInteger fid);
	void deleteWpsBelongLib(BigInteger fid);
	void deleteMainWps(BigInteger fid);
	int getCountByWpslibidChanel(BigInteger wpslibid,int chanel);
	
	/**
	 * 获取松下wps
	 * @param parent
	 * @return
	 */
	List<Wps> getSxWpsList(Page page, BigInteger parent);
	
	/**
	 * 松下焊机wps新增
	 * @param wps
	 * @return
	 */
	boolean saveSxWps(Wps wps);
	
	/**
	 * 松下焊机wps新增(历史表)
	 * @param wps
	 * @return
	 */
	boolean saveSxWpsHistory(Wps wps);
	
	/**
	 * OTC焊机wps新增(历史表)
	 * @param wps
	 * @return
	 */
	boolean saveOtcWpsHistory(Wps wps);
	
	/**
	 * 松下焊机wps修改
	 * @param wps
	 * @return
	 */
	boolean editSxWps(Wps wps);
	
	/**
	 * 工艺库与下发焊机的对应列表
	 * @param wps
	 * @return
	 */
	List<Wps> getWpslibMachineHistoryList(Page page, String machineNum, String wpslibName, WeldDto dto);
	
	/**
	 * 查询OTC参数明细
	 * @param machineId 焊机id
	 * @param chanel 通道
	 * @param machineModel 保存时间
	 * @return
	 */
	Wps getOtcDetail(String machineId, String chanel, String time);
	
	/**
	 * 查询松下参数明细
	 * @param machineId 焊机id
	 * @param chanel 通道
	 * @param time 保存时间
	 * @return
	 */
	Wps getSxDetail(String machineId, String chanel, String time);
}
