package com.spring.service;

import com.spring.model.Welder;
import com.spring.page.Page;

import java.math.BigInteger;
import java.util.List;

public interface WelderService {
	/**
	 * 获取所有焊工信息
	 * @param page
	 * @param we
	 * @return
	 */
	List<Welder> getWelderAll(Page page, String str);
	
	/**
	 * 新增焊工信息
	 * @param we
	 */
	void addWelder(Welder we);
	
	/**
	 * 修改焊工信息
	 * @param we
	 */
	void editWelder(Welder we);
	
	/**
	 * 删除焊工信息
	 * @param id
	 */
	void removeWelder(BigInteger id);
	
	/**
	 * 判断焊工编号是否存在
	 * @param wno
	 * @return
	 */
	int getWeldernoCount(String wno);

	/**
	 * 查询所有焊工不分页
	 * @return
	 */
	List<Welder> getWelderAllNoPage();
}