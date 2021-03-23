package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.Welder;

import tk.mybatis.mapper.common.Mapper;

public interface WelderMapper extends Mapper<Welder> {
	List<Welder> getWelderAll(@Param("str")String str);
	
	boolean addWelder(Welder we);
	
	boolean editWelder(Welder we);
	
	boolean removeWelder(BigInteger id);
	
	int getWeldernoCount(@Param("wno")String wno);
	
	Welder getWelderById(@Param("id")BigInteger id);
	
	Welder getWelderByNum(@Param("welderno")String welderno);
}
