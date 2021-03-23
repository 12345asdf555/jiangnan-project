package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.GatherMapper;
import com.spring.dao.WeldingMachineMapper;
import com.spring.model.Gather;
import com.spring.service.GatherService;
@WebService
@Service
@Transactional
public class GatherServiceImpl implements GatherService {
	@Autowired
	private GatherMapper gm;
	
	@Autowired
	private WeldingMachineMapper wmm;

	@Override
	public BigInteger getGatherByNo(String gatherno) {
		try{
			return gm.getGatherByNo(gatherno);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getGatherNoCount(String gatherno) {
		try{
			return gm.getGatherNoCount(gatherno);
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Gather getGatherById(BigInteger id) {
		try{
			return gm.getGatherById(id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean addGather(Gather ins) {
		try{
			int count = gm.addGather(ins);
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
	public boolean editGather(Gather ins) {
		try{
			int count = gm.editGather(ins);
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
	public boolean deleteGather(BigInteger id) {
		try{
			//修改焊机采集序号为空
			BigInteger wid = wmm.getIdByGatherid(id);
			wmm.editGatherid(wid);
			int count = gm.deleteGather(id);
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
	public List<Gather> getGatherAll(String str,BigInteger parent) {
		try{
			return gm.getGatherAll(str,parent);
		}catch(Exception e){
			return null;
		}
	}

}
