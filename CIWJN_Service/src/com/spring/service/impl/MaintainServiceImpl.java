package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.WeldingMachineMapper;
import com.spring.dao.WeldingMaintenanceMapper;
import com.spring.model.MaintenanceRecord;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.service.MaintainService;

@Service
@Transactional
public class MaintainServiceImpl implements MaintainService {
	@Autowired
	private WeldingMaintenanceMapper wmm;
	@Autowired
	private WeldingMachineMapper wm;
	
	@Override
	public List<WeldingMaintenance> getWeldingMaintenanceAll(String str) {
		try{
			return wmm.getWeldingMaintenanceAll(null,str);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean addMaintian(WeldingMaintenance w, MaintenanceRecord mr,BigInteger wid) {
		try{
			int count2 = wmm.addWeldingMaintenance(w);
			int count3 = 0;
			if("".equals(mr.getEndTime())  ||mr.getEndTime()==null){
				//修焊机状态为维护中
				count3= wm.editstatus(wid,33);
			}
			if(count2>0 || count3>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean addMaintenanceRecord(MaintenanceRecord mr) {
		try{
			int count = wmm.addMaintenanceRecord(mr);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public List<WeldingMachine> getEquipmentno() {
		try{
			return wm.getEquipmentno();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean updateEndtime(BigInteger wid) {
		try{
			int count = wmm.updateEndtime(wid);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean updateMaintenanceRecord(MaintenanceRecord mr) {
		try{
			int count = wmm.updateMaintenanceRecord(mr);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteMaintenanceRecord(BigInteger mid) {
		try{
			int count = wmm.deleteMaintenanceRecord(mid);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteWeldingMaintenance(BigInteger wid) {
		try{
			int count = wmm.deleteWeldingMaintenance(wid);	
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public List<WeldingMaintenance> getMaintainByWeldingMachinId(BigInteger wid) {
		try{
			return wmm.getMaintainByWeldingMachinId(wid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public WeldingMaintenance getWeldingMaintenanceById(BigInteger wid) {
		try{
			return wmm.getWeldingMaintenanceById(wid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<WeldingMaintenance> getEndtime(BigInteger wid) {
		try{
			return wmm.getEndtime(wid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean editstatus(BigInteger wid, int status) {
		try{
			int count = wm.editstatus(wid, status);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public BigInteger getInsfidByMachineid(BigInteger mid) {
		try{
			return wmm.getInsfidByMachineid(mid);
		}catch(Exception e){
			return null;
		}
	}

}
