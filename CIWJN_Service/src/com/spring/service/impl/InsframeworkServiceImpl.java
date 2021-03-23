package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.InsframeworkMapper;
import com.spring.dao.WeldingMachineMapper;
import com.spring.dao.WeldingMaintenanceMapper;
import com.spring.model.Insframework;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.service.InsframeworkService;

@Service
@Transactional
public class InsframeworkServiceImpl implements InsframeworkService {
	@Autowired
	private InsframeworkMapper im;
	
	@Autowired
	private WeldingMachineMapper wmm;
	
	@Autowired
	private WeldingMaintenanceMapper wm;

	@Override
	public List<Insframework> getInsframeworkAll(BigInteger parent, String str) {
		try{
			return im.getInsframeworkAll(parent,str);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addInsframework(Insframework ins) {
		try{
			int count = im.addInsframework(ins);
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
	public boolean editInsframework(Insframework ins) {
		try{
			int count = 	im.editInsframework(ins);
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
	public boolean deleteInsframework(BigInteger id) {
		try{
			//删除维修记录
			List<WeldingMachine> weldingmachine = wmm.getWeldingMachineByInsf(id);
			for(WeldingMachine welding:weldingmachine){
				List<WeldingMaintenance> list = wm.getMaintainByWeldingMachinId(welding.getId());
				for(WeldingMaintenance w:list){
					WeldingMaintenance m = wm.getWeldingMaintenanceById(w.getId());
					wm.deleteMaintenanceRecord(m.getMaintenance().getId());
					wm.deleteWeldingMaintenance(m.getId());
				}
			}
			//删除焊机
			wmm.deleteByInsf(id);
			//删除项目
			int count = im.deleteInsframework(id);
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
	public int getInsframeworkNameCount(String name) {
		try{
			return im.getInsframeworkNameCount(name);
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public String getInsframeworkById(BigInteger id) {
		try{
			return im.getInsframeworkById(id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Insframework getInsfAllById(BigInteger id) {
		try{
			return im.getInsfAllById(id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Insframework> getConmpany(BigInteger value1) {
		try{
			return im.getConmpany(value1);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Insframework> getCause(BigInteger id,BigInteger value2) {
		try{
			return im.getCause(id,value2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Insframework> getWeldingMachineInsf(BigInteger parent) {
		try{
			return im.getInsframework(parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Insframework getParent(BigInteger id) {
		try{
			return im.getParent(id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Insframework> getInsByType(int type,BigInteger parent) {
		try{
			return im.getInsByType(type,parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getUserInsfType(BigInteger uid) {
		try{
			return im.getUserInsfType(uid);
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public BigInteger getUserInsfId(BigInteger uid) {
		try{
			return im.getUserInsfId(uid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Insframework getBloc() {
		try{
			return im.getBloc();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getTypeById(BigInteger id) {
		try{
			return im.getTypeById(id);
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public BigInteger getParentById(BigInteger id) {
		try{
			return im.getParentById(id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Insframework> getInsByUserid(BigInteger uid) {
		try{
			return im.getInsByUserid(uid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Insframework> getInsByType(int type) {
		try{
			return im.getInsfByType(type);
		}catch(Exception e){
			return null;
		}
	}
	
}
