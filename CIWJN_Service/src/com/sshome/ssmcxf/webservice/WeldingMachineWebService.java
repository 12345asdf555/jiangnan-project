package com.sshome.ssmcxf.webservice;

import java.math.BigInteger;

public interface WeldingMachineWebService {

	/**
	 * 查询所有焊机信息
	 */
	Object getWeldingMachineAll();
	
	/**
	 * 查询所有厂商信息
	 */
	Object getManuAll();
	
	/**
	 * 新增设备
	 */
	BigInteger addWeldingMachine(String object);
	
	/**
	 * 修改设备
	 */
	boolean editWeldingMachine(String object);
	
	/**
	 * 删除设备
	 */
	boolean deleteWeldingChine(String object);
	
	/**
	 * 根据焊机编号查找id
	 */
	BigInteger getWeldingMachineByEno(String object);
	
	/**
	 * 判断焊机编号是否存在
	 */
	int getEquipmentnoCount(String object);
	
	/**
	 * 判断采集序号是否存在
	 */
	int getGatheridCount(String object);
	
	/**
	 * 根据厂商值和类型查找厂商id
	 */
	BigInteger getManuidByValue(String object);
	
	/**
	 * 根据id查找记录
	 */
	Object getWeldingMachineById(String object);
	
	/**
	 * 根据项目名称获取项目id
	 */
	BigInteger getInsframeworkByName(String object);
	
	/**
	 * 获取某厂商下的焊机总数
	 */
	BigInteger getMachineCountByManu(String object);
	
	/**
	 * 查询所有采集模块和对应焊机
	 */
	Object getGatherMachine();
}
