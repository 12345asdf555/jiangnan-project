package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.EquipmentManufacturer;

import tk.mybatis.mapper.common.Mapper;

public interface EquipmentManufacturerMapper extends Mapper<EquipmentManufacturer> {
	List<EquipmentManufacturer> getmanuAll(@Param("str")String str);

	BigInteger getManuidByValue(@Param("name")String name,@Param("type")String type);
	
	int getManuCount(@Param("name")String name,@Param("type")int type);
	
	EquipmentManufacturer getManuById(@Param("id")BigInteger id);
	
	boolean addManu(EquipmentManufacturer manu);

	boolean editManu(EquipmentManufacturer manu);

	boolean deleteManu(@Param("id")BigInteger id);
}
