package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.WeldDto;
import com.spring.model.WeldedJunction;

import tk.mybatis.mapper.common.Mapper;

public interface WeldedJunctionMapper extends Mapper<WeldedJunction>{
	List<WeldedJunction> getWeldedJunctionAll(@Param("str")String str);
	
	List<WeldedJunction> getTaskResultAll(@Param("str")String str);
	
	List<WeldedJunction> getJunctionByWelder(@Param("welder")String welder,@Param("dto")WeldDto dto);
	
	WeldedJunction getWeldedJunctionById(@Param("id")BigInteger id);
	
	boolean addJunction(WeldedJunction wj);

	boolean updateJunction(WeldedJunction wj);

	boolean deleteJunction(@Param("id")BigInteger id);
	
	int getWeldedjunctionByNo(@Param("wjno")String wjno);
	
	List<WeldedJunction> getJMByWelder(@Param("dto") WeldDto dto,@Param("welderid")String welderid);
	
	String getFirsttime(@Param("dto") WeldDto dto,@Param("machineid")BigInteger machineid, @Param("welderid")String welderid, @Param("junid")String junid);
	
	String getLasttime(@Param("dto") WeldDto dto,@Param("machineid")BigInteger machineid, @Param("welderid")String welderid, @Param("junid")String junid);
	
	int getCountByTaskid(@Param("taskid")BigInteger taskid,@Param("type")BigInteger type);
	
	boolean addTask(WeldedJunction wj);
	
	List<WeldedJunction> getFreeJunction(@Param("str")String str);
	
	List<WeldedJunction> getRealWelder(@Param("taskid")BigInteger taskid);
}
