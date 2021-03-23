package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dao.FaultMapper;
import com.spring.dto.JudgeUtil;
import com.spring.model.Fault;
import com.spring.service.FaultService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class FaultServiceImpl implements FaultService {
	@Autowired
	private FaultMapper fm;
	
	private JudgeUtil jutil = new JudgeUtil();

	@Override
	public Object getFaultAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Fault> list = fm.getFaultAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("CODE",jutil.setValue(list.get(i).getCode()));
				obj.put("TYPEID",jutil.setValue(list.get(i).getType()));
				obj.put("TYPENAME",jutil.setValue(list.get(i).getValuename()));
				obj.put("DESC",jutil.setValue(list.get(i).getDesc()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getFaultById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Fault list = fm.getFaultById(new BigInteger(json.getString("ID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("CODE",jutil.setValue(list.getCode()));
				obj.put("TYPEID",jutil.setValue(list.getType()));
				obj.put("TYPENAME",jutil.setValue(list.getValuename()));
				obj.put("CREATOR",jutil.setValue(list.getCreator()));
				obj.put("DESC",jutil.setValue(list.getDesc()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger addFault(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Fault f = new Fault();
			f.setCode(json.getString("CODE"));
			f.setDesc(json.getString("DESC"));
			f.setType(json.getInt("TYPEID"));
			f.setCreator(json.getString("CREATOR"));
			if(fm.addFault(f)){
				return f.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean editFault(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Fault f = new Fault();
			f.setId(new BigInteger(json.getString("ID")));
			f.setCode(json.getString("CODE"));
			f.setDesc(json.getString("DESC"));
			f.setType(json.getInt("TYPEID"));
			f.setModifier(json.getString("MODIFIER"));
			return fm.editFault(f);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteFault(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return fm.deleteFault(new BigInteger(json.getString("ID")));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
