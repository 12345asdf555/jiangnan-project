package com.sshome.ssmcxf.webservice;

import java.math.BigInteger;

public interface GatherWebService {
	/**
	 * 查询采集列表
	 * @param str 查询信息
	 * @return
	 */
	Object getGatherAll(String object);
	
	/**
	 * 根据编号查询id
	 * @param gatherno采集编号
	 * @return
	 */
	BigInteger getGatherByNo(String object);
	
	/**
	 * 判断采集编号是否存在
	 * @param gatherno采集编号
	 * @return
	 */
	int getGatherNoCount(String object);
	
	/**
	 * 根据id查询采集信息
	 * @param id 采集id
	 * @return
	 */
	Object getGatherById(String object);
	
	/**
	 * 添加采集信息
	 * @param ins采集对象
	 */
	BigInteger addGather(String object);
	
	/**
	 * 修改采集信息
	 * @param ins采集对象
	 */
	boolean editGather(String object);
	
	/**
	 * 删除采集信息
	 * @param id采集id
	 */
	boolean deleteGather(String object);
}
