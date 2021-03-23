package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.EquipmentManufacturer;
import com.spring.model.WeldingMachine;

public interface WeldingMachineService {
	
	/**
	 * 查询所有焊机信息
	 */
	List<WeldingMachine> getWeldingMachineAll();
	
	/**
	 * 查询所有厂商信息
	 * @return
	 */
	List<EquipmentManufacturer> getManuAll();
	
	/**
	 * 新增设备
	 */
	boolean addWeldingMachine(WeldingMachine wm);
	
	/**
	 * 修改设备
	 */
	boolean editWeldingMachine(WeldingMachine wm);
	
	/**
	 * 删除设备
	 * @param wid
	 */
	boolean deleteWeldingChine(BigInteger wid);
	
	/**
	 * 根据焊机编号查找id
	 * @return
	 */
	BigInteger getWeldingMachineByEno(String eno);
	
	/**
	 * 判断焊机编号是否存在
	 * @param eno
	 * @return
	 */
	int getEquipmentnoCount(String eno);
	
	/**
	 * 判断采集序号是否存在
	 * @param gatherid
	 * @return
	 */
	int getGatheridCount(BigInteger itemid,String gather);
	
	/**
	 * 根据厂商值和类型查找厂商id
	 * @param value 厂商名字
	 * @param type 厂商类型
	 * @return
	 */
	BigInteger getManuidByValue(String value,String type);
	
	/**
	 * 根据id查找记录
	 * @param wid
	 * @return
	 */
	WeldingMachine getWeldingMachineById(BigInteger wid);
	
	/**
	 * 根据项目名称获取项目id
	 * @param name
	 * @return
	 */
	BigInteger getInsframeworkByName(String name);
	
	/**
	 * 获取某厂商下的焊机总数
	 * @param mid 厂商id
	 * @return
	 */
	BigInteger getMachineCountByManu(BigInteger mid, BigInteger id);
	
}
