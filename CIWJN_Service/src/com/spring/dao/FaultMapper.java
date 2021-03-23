package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.Fault;

import tk.mybatis.mapper.common.Mapper;

public interface FaultMapper extends Mapper<Fault>{
	List<Fault> getFaultAll(@Param("str")String str);
	
	Fault getFaultById(@Param("id")BigInteger id);
	
	boolean addFault(Fault ins);
	
	boolean editFault(Fault ins);
	
	boolean deleteFault(@Param("id")BigInteger id);
}
