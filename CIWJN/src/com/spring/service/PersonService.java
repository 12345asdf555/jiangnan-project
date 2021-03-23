package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.Person;
import com.spring.page.Page;

public interface PersonService {

	List<Person> findAll(Page page, BigInteger parent,String str);
	List<Person> findAll(BigInteger parent);
	List<Person> findAll(BigInteger parent,String str);
	List<Person> findLeve(int type);
	List<Person> dic();
	List<Person> ins();
	void save(Person welder);
	Person findById(BigInteger fid);
	int getUsernameCount(String welderno);
	void update(Person welder);
	void delete(BigInteger fid);

	/**
	 * 获取所有焊工
	 * @return
	 */
	List<Person> getWelder();
	
	/**
	 * 根据焊工编号获取id
	 * @param welderno
	 * @return
	 */
	Person getIdByWelderno(String welderno);

	/**
	 * 获取所有空闲焊工
	 * @return
	 */
	List<Person> getFreeWelder(Page page,String str);
	/**
	 * 以焊工编号为条件更新焊工信息
	 * @param welder
	 */
	void updateByWelderno(Person welder);
}