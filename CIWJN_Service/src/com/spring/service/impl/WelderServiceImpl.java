package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dao.WelderMapper;
import com.spring.dto.JudgeUtil;
import com.spring.model.Welder;
import com.spring.service.WelderService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Transactional
@Service
public class WelderServiceImpl implements WelderService {
	@Autowired
	private WelderMapper wm;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public Object getWelderAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Welder> list = wm.getWelderAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("WELDERID", jutil.setValue(list.get(i).getId()));
//				obj.put("NAME",jutil.setValue(list.get(i).getName()));
				obj.put("WELDERNO",jutil.setValue(list.get(i).getWelderno()));
/*				obj.put("ITEMID",jutil.setValue(list.get(i).getIid()));
				obj.put("ITEMNAME",jutil.setValue(list.get(i).getIname()));
				obj.put("PHONE",jutil.setValue(list.get(i).getPhone()));
				obj.put("CARDNUM",jutil.setValue(list.get(i).getCardnum()));
				obj.put("LEVEL",jutil.setValue(list.get(i).getLevel()));
				obj.put("QUALITY",jutil.setValue(list.get(i).getQuality()));*/
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger addWelder(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Welder we = new Welder();
			we.setName(json.getString("NAME"));
			we.setWelderno(json.getString("WELDERNO"));
			we.setIid(new BigInteger(json.getString("INSFID")));
			we.setCreator(json.getString("CREATOR"));
			if(wm.addWelder(we)){
				return we.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean editWelder(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Welder we = new Welder();
			we.setId(new BigInteger(json.getString("ID")));
			we.setName(json.getString("NAME"));
			we.setWelderno(json.getString("WELDERNO"));
			we.setIid(new BigInteger(json.getString("INSFID")));
			we.setModifier(json.getString("MODIFIER"));
			return wm.editWelder(we);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeWelder(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return wm.removeWelder(new BigInteger(json.getString("ID")));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int getWeldernoCount(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return wm.getWeldernoCount(json.getString("WELDERNO"));
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Object getWelderById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Welder list = wm.getWelderById(new BigInteger(json.getString("ID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("NAME",jutil.setValue(list.getName()));
				obj.put("WELDERNO",jutil.setValue(list.getWelderno()));
				obj.put("ITEMNAME",jutil.setValue(list.getIname()));
				obj.put("PHONE",jutil.setValue(list.getPhone()));
				obj.put("CARDNUM",jutil.setValue(list.getCardnum()));
				obj.put("LEVEL",jutil.setValue(list.getLevel()));
				obj.put("QUALITY",jutil.setValue(list.getQuality()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getWelderByNum(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			Welder list = wm.getWelderByNum(json.getString("WELDERNO"));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("NAME",jutil.setValue(list.getName()));
				obj.put("WELDERNO",jutil.setValue(list.getWelderno()));
				obj.put("ITEMID", jutil.setValue(list.getIid()));
				obj.put("ITEMNAME",jutil.setValue(list.getIname()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
