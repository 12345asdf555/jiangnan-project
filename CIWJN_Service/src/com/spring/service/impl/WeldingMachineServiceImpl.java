package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.EquipmentManufacturerMapper;
import com.spring.dao.InsframeworkMapper;
import com.spring.dao.LiveDataMapper;
import com.spring.dao.WeldingMachineMapper;
import com.spring.dto.ModelDto;
import com.spring.model.EquipmentManufacturer;
import com.spring.model.WeldingMachine;
import com.spring.service.WeldingMachineService;

@Service
@Transactional
public class WeldingMachineServiceImpl implements WeldingMachineService {
	
	@Autowired
	private WeldingMachineMapper wmm;
	@Autowired
	private EquipmentManufacturerMapper em;
	@Autowired
	private InsframeworkMapper im;
	
	@Override
	public List<WeldingMachine> getWeldingMachineAll() {
		try{
			return wmm.getWeldingMachineAll();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean addWeldingMachine(WeldingMachine wm) {
		try{
			int count = wmm.addWeldingMachine(wm);
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
	public boolean editWeldingMachine(WeldingMachine wm) {
		try{
			int count = wmm.editWeldingMachine(wm);
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
	public BigInteger getWeldingMachineByEno(String eno) {
		try{
			return wmm.getWeldingMachineByEno(eno);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<EquipmentManufacturer> getManuAll() {
		try{
			return em.getmanuAll(null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getEquipmentnoCount(String eno) {
		try{
			return wmm.getEquipmentnoCount(eno);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int getGatheridCount(BigInteger itemid,String gather) {
		try{
			return wmm.getGatheridCount(itemid,gather);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public BigInteger getManuidByValue(String value,String type) {
		try{
			return em.getManuidByValue(value,type);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public WeldingMachine getWeldingMachineById(BigInteger wid) {
		try{
			return wmm.getWeldingMachineById(wid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean deleteWeldingChine(BigInteger wid) {
		try{
			int count = wmm.deleteWeldingMachine(wid);
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
	public BigInteger getInsframeworkByName(String name) {
		try{
			return im.getInsframeworkByName(name);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger getMachineCountByManu(BigInteger mid, BigInteger id) {
		try{
			return wmm.getMachineCountByManu(mid, id);
		}catch(Exception e){
			return null;
		}
	}

}
