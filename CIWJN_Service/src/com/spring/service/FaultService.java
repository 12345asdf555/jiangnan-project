package com.spring.service;

import java.math.BigInteger;

public interface FaultService {
	/**
	 * 查询所有故障代码
	 * @param page 分页
	 * @param str 查询条件
	 * @return
	 */
	Object getFaultAll(String object);
	
	/**
	 * 根据id查询故障代码
	 * @param id 故障代码id
	 * @return
	 */
	Object getFaultById(String object);
	
	/**
	 * 新增故障代码
	 * @param fault 故障代码model
	 * @return
	 */
	BigInteger addFault(String object);
	
	/**
	 * 修改故障代码
	 * @param fault 故障代码model
	 * @return
	 */
	boolean editFault(String object);
	
	/**
	 * 删除故障代码
	 * @param id 故障代码id
	 * @return
	 */
	boolean deleteFault(String object);
}
