package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.Dictionarys;

import tk.mybatis.mapper.common.Mapper;

public interface DictionaryMapper extends Mapper<Dictionarys> {
	List<Dictionarys> getDictionaryAll(@Param("str")String str);
	
	int addDictionary(Dictionarys d);
	
	int editDictionary(Dictionarys d);
	
	int getDictionaryMaxValue(int typeid);
	
	Dictionarys getDictionaryByFid(int id);
	
	int deleteDictionary(int id);
	
	BigInteger getDictionaryByValue(@Param("value")int value);
	
	List<Dictionarys> getDictionaryValue(@Param("typeid")int typeid);
	
	List<Dictionarys> getDicValueByValue(@Param("typeid")int typeid,@Param("value")int value);
	
	int getvaluebyname(@Param("typeid")int typeid,@Param("valuename")String valuename);

	String getDicValueByType(@Param("value")int value);
	
	List<Dictionarys> getBack();
}
