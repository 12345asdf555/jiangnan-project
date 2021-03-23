package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dao.EquipmentManufacturerMapper;
import com.spring.dto.JudgeUtil;
import com.spring.model.EquipmentManufacturer;
import com.spring.service.ManufacturerService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {
	@Autowired
	private  EquipmentManufacturerMapper em;
	
	private JudgeUtil jutil = new JudgeUtil();

	@Override
	public Object getmanuAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<EquipmentManufacturer> list = em.getmanuAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("NAME",jutil.setValue(list.get(i).getName()));
				obj.put("TYPEID",jutil.setValue(list.get(i).getTypeid()));
				obj.put("TYPENAME",jutil.setValue(list.get(i).getType()));
				obj.put("TYPEVALUE",jutil.setValue(list.get(i).getTypeValue()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger getManuidByValue(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return em.getManuidByValue(json.getString("NAME"), json.getString("TYPENAME"));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getManuCount(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return em.getManuCount(json.getString("NAME"), json.getInt("TYPEID"));
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Object getManuById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			EquipmentManufacturer list = em.getManuById(new BigInteger(json.getString("ID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("NAME",jutil.setValue(list.getName()));
				obj.put("TYPEID",jutil.setValue(list.getTypeid()));
				obj.put("TYPENAME",jutil.setValue(list.getType()));
				obj.put("TYPEVALUE",jutil.setValue(list.getTypeValue()));
				obj.put("CREATOR",jutil.setValue(list.getCreator()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger addManu(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			EquipmentManufacturer e = new EquipmentManufacturer();
			e.setName(json.getString("NAME"));
			e.setType(json.getString("TYPEID"));
			e.setTypeValue(json.getString("TYPEVALUE"));
			e.setCreator(json.getString("CREATOR"));
			if(em.addManu(e)){
				return e.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean editManu(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			EquipmentManufacturer e = new EquipmentManufacturer();
			e.setId(new BigInteger(json.getString("ID")));
			e.setName(json.getString("NAME"));
			e.setType(json.getString("TYPEID"));
			e.setTypeValue(json.getString("TYPEVALUE"));
			e.setModifier(json.getString("MODIFIER"));
			return em.editManu(e);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteManu(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return em.deleteManu(new BigInteger(json.getString("ID")));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
