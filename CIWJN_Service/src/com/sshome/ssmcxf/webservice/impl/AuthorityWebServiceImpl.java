package com.sshome.ssmcxf.webservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Authority;
import com.spring.service.AuthorityService;
import com.sshome.ssmcxf.webservice.AuthorityWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class AuthorityWebServiceImpl implements AuthorityWebService {
	@Autowired
	private AuthorityService as;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public int saveAuthority(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Authority auth = new Authority();
			auth.setAuthorityName(json.getString("AUTHORITYNAME"));
			auth.setAuthorityDesc(json.getString("AUTHORITYDESC"));
			auth.setStatus(json.getInt("STATUSID"));
			auth.setCreator(json.getString("CREATOR"));
			if(as.save(auth)){
				return auth.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public int saveAuthorityResource(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Authority auth = new Authority();
			auth.setId(json.getInt("AUTHID"));
			auth.setResourceId(json.getInt("RESOURCEID"));
			auth.setCreator(json.getString("CREATOR"));
			if(as.saveResource(auth)){
				return auth.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public boolean updateAuthority(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Authority auth = new Authority();
			auth.setId(json.getInt("AUTHID"));
			auth.setAuthorityName(json.getString("AUTHORITYNAME"));
			auth.setAuthorityDesc(json.getString("AUTHORITYDESC"));
			auth.setStatus(json.getInt("STATUSID"));
			auth.setModifier(json.getString("MODIFIER"));
			return as.update(auth);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteAuthority(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return as.delete(json.getInt("AUTHID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteAuthoritiesResources(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return as.delete1(json.getInt("AUTHID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteRolesAuthorities(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return as.delete2(json.getInt("AUTHID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String findByResourceId(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return as.findByResourceId(json.getInt("RESOURCEID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAuthorityById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			Authority list = as.findById(json.getInt("AUTHID"));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("AUTHORITYNAME",jutil.setValue(list.getAuthorityName()));
				obj.put("AUTHORITYDESC",jutil.setValue(list.getAuthorityDesc()));
				obj.put("STATUSID",jutil.setValue(list.getStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.getStatusname()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAllAuthority(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Authority> list = as.findAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("AUTHORITYNAME",jutil.setValue(list.get(i).getAuthorityName()));
				obj.put("AUTHORITYDESC",jutil.setValue(list.get(i).getAuthorityDesc()));
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
	public Object findAllResource() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Authority> list =  as.findAllResource();
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("RESOURCENAME",jutil.setValue(list.get(i).getResourceName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAuthorityResource(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			List<Authority> list = as.findResource(json.getInt("AUTHID"));
			return JSON.toJSONString(list);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String fineAuthorityNameById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return as.updateAuthorityResource(json.getInt("AUTHID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getAuthoritynameCount(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return as.getAuthoritynameCount(json.getString("AUTHNAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Object getAllAuthoritys() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Authority> list = as.getAllAuthoritys();
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("AUTHORITYNAME",jutil.setValue(list.get(i).getAuthorityName()));
				obj.put("AUTHORITYDESC",jutil.setValue(list.get(i).getAuthorityDesc()));
				obj.put("STATUSID",jutil.setValue(list.get(i).getStatus()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int findAuthId(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return as.findAuthId(json.getString("AUTHNAME"));
		}catch(Exception e){
			return -1;
		}
	}
}
