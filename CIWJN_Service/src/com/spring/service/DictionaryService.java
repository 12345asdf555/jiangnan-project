package com.spring.service;

import java.util.List;

import com.spring.model.Dictionarys;

public interface DictionaryService {
	/**
	 * 获取字典信息
	 * @param page
	 * @param str
	 * @return
	 */
	List<Dictionarys> getAllDictionary(String str);
	
    boolean addDictionary(Dictionarys d);
	
    boolean editDictionary(Dictionarys d);
	
	int getDictionaryMaxValue(int typeid);
	
	Dictionarys getDictionaryByFid(int id);
	
	boolean deleteDictionary(int id);
	
	/**
	 * 获取字典值及值名称
	 * @param typeid 类型id
	 * @return
	 */
	List<Dictionarys> getDictionaryValue(int typeid);
	
	/**
	 * 根据类型值及字典值获取字典值及值名称
	 * @param typeid 类型值
	 * @param value 字典值
	 * @return
	 */
	List<Dictionarys> getDicValueByValue(int typeid,int value);
	
	/**
	 * 根据值名称获取值
	 * @param valuename 值名称
	 * @return
	 */
	int getvaluebyname(int typeid,String valuename);
	
	/**
	 * 根据值获取值名称
	 * @param value 值
	 * @return
	 */
	String getDicValueByType(int value);
	
	/**
	 * 获取字典类型id及描述
	 * @return
	 */
	List<Dictionarys> getBack();
}
