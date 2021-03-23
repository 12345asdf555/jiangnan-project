package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.Gather;

public interface GatherService {
	
	/**
	 * 查询采集列表
	 * @param str 查询信息
	 * @return
	 */
	List<Gather> getGatherAll(String str,BigInteger parent);
	
	/**
	 * 根据编号查询id
	 * @param gatherno采集编号
	 * @return
	 */
	BigInteger getGatherByNo(String gatherno);
	
	/**
	 * 判断采集编号是否存在
	 * @param gatherno采集编号
	 * @return
	 */
	int getGatherNoCount(String gatherno);
	
	/**
	 * 根据id查询采集信息
	 * @param id 采集id
	 * @return
	 */
	Gather getGatherById(BigInteger id);
	
	/**
	 * 添加采集信息
	 * @param ins采集对象
	 */
	boolean addGather(Gather ins);
	
	/**
	 * 修改采集信息
	 * @param ins采集对象
	 */
	boolean editGather(Gather ins);
	
	/**
	 * 删除采集信息
	 * @param id采集id
	 */
	boolean deleteGather(BigInteger id);
}
