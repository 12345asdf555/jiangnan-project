package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.MaintenanceRecord;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;

public interface MaintainService {
	
	/**
	 * 获取所有维修记录
	 * @return
	 */
	List<WeldingMaintenance> getWeldingMaintenanceAll(String str);

	/**
	 * 获取维修结束时间
	 * @param wid
	 * @return
	 */
	List<WeldingMaintenance> getEndtime(BigInteger wid);
	
	/**
	 * 根据id查询所有信息
	 * @param wid
	 * @return
	 */
	WeldingMaintenance getWeldingMaintenanceById(BigInteger wid);
	
	/**
	 * 获取设备编码
	 * @return
	 */
	List<WeldingMachine> getEquipmentno();
	
	/**
	 * 新增维修记录
	 * @param wm
	 */
	boolean addMaintian(WeldingMaintenance wm, MaintenanceRecord mr, BigInteger wid);
	boolean addMaintenanceRecord(MaintenanceRecord mr);
	
	/**
	 * 修改结束时间为当前时间
	 * @param wid
	 */
	boolean updateEndtime(BigInteger wid);
	
	/**
	 * 修改
	 * @param mr
	 */
	boolean updateMaintenanceRecord(MaintenanceRecord mr);
	
	/**
	 * 删除维修记录
	 * @param mid
	 */
	boolean deleteMaintenanceRecord(BigInteger mid);
	
	/**
	 * 删除焊机维修记录
	 * @param wid
	 */
	boolean deleteWeldingMaintenance(BigInteger wid);
	
	/**
	 * 根据焊机id查找维修记录id
	 * @param wid
	 * @return
	 */
	List<WeldingMaintenance> getMaintainByWeldingMachinId(BigInteger wid);
	
	/**
	 * 修改焊机维修状态
	 * @param wid
	 * @param status
	 */
	boolean editstatus(BigInteger wid,int status);

	/**
	 * 根据焊机id获取组织机构id
	 * @param mid
	 * @return
	 */
	BigInteger getInsfidByMachineid(BigInteger mid);
}
