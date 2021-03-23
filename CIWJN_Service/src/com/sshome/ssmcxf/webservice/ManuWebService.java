package com.sshome.ssmcxf.webservice;

import java.math.BigInteger;

public interface ManuWebService {
	/**
	 * 查询所有厂商信息
	 * @param page 分页
	 * @param str 查询条件
	 * @return
	 */
	Object getmanuAll(String object);
	
	/**
	 * 根据厂商名及类型查找id
	 * @param name 厂商名
	 * @param type 类型
	 * @return
	 */
	BigInteger getManuidByValue(String object);
	
	/**
	 * 判断厂商名及类型是否存在
	 * @param name 厂商名
	 * @param type 类型id
	 * @return
	 */
	int getManuCount(String object);
	
	/**
	 * 根据id查找厂商
	 * @param id
	 * @return
	 */
	Object getManuById(String object);
	
	/**
	 * 新增厂商
	 * @param manu 厂商类
	 * @return
	 */
	BigInteger addManu(String object);

	/**
	 * 修改厂商
	 * @param manu 厂商类
	 * @return
	 */
	boolean editManu(String object);
	
	/**
	 * 删除厂商
	 * @param id 厂商id
	 * @return
	 */
	boolean deleteManu(String object);

}
