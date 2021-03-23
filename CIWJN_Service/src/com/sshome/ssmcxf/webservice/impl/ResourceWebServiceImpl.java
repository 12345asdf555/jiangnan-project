package com.sshome.ssmcxf.webservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Resources;
import com.spring.service.ResourceService;
import com.sshome.ssmcxf.webservice.ResourceWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class ResourceWebServiceImpl implements ResourceWebService{
	@Autowired
	private ResourceService rs;

	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public int saveResource(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			Resources r = new Resources();
			r.setResourceName(json.getString("RESOURCENAME"));
			r.setResourceAddress(json.getString("ADDRESS"));
			r.setResourceType(json.getString("TYPEID"));
			r.setResourceDesc(json.getString("DESC"));
			r.setStatus(json.getInt("STATUSID"));
			r.setCreator(json.getString("CREATOR"));
			if(rs.save(r)){
				return r.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public boolean updateResource(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			Resources r = new Resources();
			r.setId(json.getInt("ID"));
			r.setResourceName(json.getString("RESOURCENAME"));
			r.setResourceAddress(json.getString("ADDRESS"));
			r.setResourceType(json.getString("TYPEID"));
			r.setResourceDesc(json.getString("DESC"));
			r.setStatus(json.getInt("STATUSID"));
			r.setModifier(json.getString("MODIFIER"));
			return rs.update(r);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteResource(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return rs.delete(json.getInt("ID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Object findResourceById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Resources list = rs.findById(json.getInt("ID"));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("RESOURCENAME",jutil.setValue(list.getResourceName()));
				obj.put("RESOURCETYPE",jutil.setValue(list.getResourceType()));
				obj.put("RESOURCEADDRESS",jutil.setValue(list.getResourceAddress()));
				obj.put("RESOURCEDESC",jutil.setValue(list.getResourceDesc()));
				obj.put("STATUSID",jutil.setValue(list.getStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.getStatusname()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findResourceAll(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Resources> list = rs.findAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("RESOURCENAME",jutil.setValue(list.get(i).getResourceName()));
				obj.put("RESOURCETYPE",jutil.setValue(list.get(i).getResourceType()));
				obj.put("RESOURCEADDRESS",jutil.setValue(list.get(i).getResourceAddress()));
				obj.put("RESOURCEDESC",jutil.setValue(list.get(i).getResourceDesc()));
				obj.put("STATUSID",jutil.setValue(list.get(i).getStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.get(i).getStatusname()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getResourcenameCount(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return rs.getResourcenameCount(json.getString("NAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Object getAuthByRes(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			List<String> list = rs.getAuthByRes(json.getString("ADDRESS"));
			return JSON.toJSONString(list);
		}catch(Exception e){
			return null;
		}
	}

}
