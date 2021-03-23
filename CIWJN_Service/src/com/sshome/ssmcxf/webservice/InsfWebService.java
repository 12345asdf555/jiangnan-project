package com.sshome.ssmcxf.webservice;

import java.math.BigInteger;

public interface InsfWebService {
	/**
	 * 获取组织机构
	 * @param str
	 * @return
	 */
	Object getInsframeworkAll(String object);
	
	/**
	 * 新增组织机构
	 * @param ins
	 */
    BigInteger addInsframework(String obj1,String obj2);
	/**
	 * 修改组织机构
	 * @param ins
	 */
	boolean editInsframework(String obj1,String obj2);
	
	/**
	 * 删除组织机构
	 * @param id
	 */
	boolean deleteInsframework(String obj1,String obj2);
	
	/**
	 * 判断name是否存在
	 * @param name 项目名称
	 * @return
	 */
	int getInsframeworkNameCount(String object);
	
	/**
	 * 根据id查找name
	 * @param id 项目id
	 * @return
	 */
	String getInsframeworkById(String object);
	
	/**
	 * 根据id查找所有
	 * @param id 组织机构id
	 * @return
	 */
	Object getInsfAllById(String object);
	
	/**
	 * 查看集团
	 * @return
	 */
	Object getBloc();
	/**
	 * 查看公司级
	 * @return
	 */
	Object getCompany(String object);
	
	/**
	 * 查看公司级子级
	 * @return
	 */
	Object getCause(String object);
	
	/**
	 * 焊机获取项目
	 * @return
	 */
	Object getWeldingMachineInsf(String object);
	
	/**
	 * 获取父级
	 * @param id 子级编号
	 * @return
	 */
	Object getParent(String object);
		
	/**
	 * 获取某层级的单位名称
	 * @param type单位类型
	 * @return
	 */
	Object getInsByType(String object);
	
	/**
	 * 获取用户所属层级
	 * @param uid 用户id
	 * @return
	 */
	int getUserInsfType(String object);
	
	/**
	 * 获取用户所属部门id
	 * @param uid 用户id
	 * @return
	 */
	BigInteger getUserInsfId(String object);
	
	/**
	 * 根据id获取类型
	 * @param id
	 * @return
	 */
	int getTypeById(String object);
	
	/**
	 * 根据id获取父类
	 * @param id 自己的id
	 * @return
	 */
	BigInteger getParentById(String object);
	
	/**
	 * 根据用户id查找组织机构id及类型
	 * @param uid
	 * @return
	 */
	Object getInsByUserid(String object);
	
}
