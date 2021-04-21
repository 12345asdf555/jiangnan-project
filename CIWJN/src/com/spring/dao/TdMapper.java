package com.spring.dao;

import com.spring.model.Td;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface TdMapper {
	List<Td> findAll(@Param("str")String str);
	List<Td> findAllpro(long ins);
	List<Td> findAllcom();
	List<Td> findAlldiv(long ins);
	List<Td> getAllPosition(@Param("parent")BigInteger parent,@Param("str")String str);
	List<Td> getMachine(@Param("mach")BigInteger mach,@Param("parent")BigInteger parent);
	long findAllIns(long uid);
	long findInsid(String insname);
	String findweld(String weldid);
	String findInsname(long uid);
	String findPosition(String equip);
	List<Td> allWeldname(@Param("parent")BigInteger parent);
	List<Td> findMachine(String fposition);
	
	Td getLiveTime(@Param("startTime")String time,@Param("machineId")BigInteger machineId);

	String getBootTime(@Param("time")String time,@Param("machineId")BigInteger machineId,
					   @Param("nowTableName") String nowTableName, @Param("orderType") String orderType);

	Td getJunctionIdByRtdata(@Param("machineId")BigInteger machineId,@Param("startTime") String startTime,
									 @Param("tableName") String tableName);
}