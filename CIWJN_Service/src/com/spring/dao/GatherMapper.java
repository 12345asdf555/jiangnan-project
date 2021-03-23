package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.Gather;

import tk.mybatis.mapper.common.Mapper;

public interface GatherMapper extends Mapper<Gather>{
	List<Gather> getGatherAll(@Param("str")String str,@Param("parent")BigInteger parent);
	
	BigInteger getGatherByNo(@Param("gatherno")String gatherno);
	
	int getGatherNoCount(@Param("gatherno")String gatherno);
	
	Gather getGatherById(@Param("id")BigInteger id);
	
	int addGather(Gather ins);
	
	int editGather(Gather ins);
	
	int deleteGather(@Param("id")BigInteger id);
}
