package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.MaintenanceRecord;
import com.spring.model.WeldingMaintenance;

import tk.mybatis.mapper.common.Mapper;

public interface WeldingMaintenanceMapper extends Mapper<WeldingMaintenance> {
	List<WeldingMaintenance> getWeldingMaintenanceAll(@Param("wid")BigInteger wid,@Param("str")String str);
	
	List<WeldingMaintenance> getMaintainByWeldingMachinId(@Param("wid")BigInteger wid);
	
	List<WeldingMaintenance> getEndtime(@Param("wid") BigInteger wid);
	
	WeldingMaintenance getWeldingMaintenanceById(@Param("wid") BigInteger wid);
	
	int addMaintenanceRecord(MaintenanceRecord mr);
	
	int addWeldingMaintenance(WeldingMaintenance wm);
	
	int updateEndtime(@Param("wid")BigInteger wid);
	
	int updateMaintenanceRecord(MaintenanceRecord mr);
	
	int deleteMaintenanceRecord(@Param("mid")BigInteger mid);
	
	int deleteWeldingMaintenance(@Param("wid")BigInteger wid);

	BigInteger getInsfidByMachineid(@Param("mid")BigInteger mid);
}
