package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.User;
import com.spring.service.UserService;
import com.sshome.ssmcxf.webservice.UserWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Transactional
@Service
public class UserWebServiceImpl implements UserWebService {
	@Autowired
	private UserService us;
	
	private JudgeUtil jutil = new JudgeUtil();

	@Override
	public int save(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			User user = new User();
			user.setUserName(json.getString("UNAME"));
			user.setUserPassword(json.getString("PASSWORD"));
			user.setUserLoginName(json.getString("LOGINNAME"));
			user.setUserEmail(json.getString("EMAIL"));
			user.setUserPhone(json.getString("PHONE"));
			user.setUserInsframework(json.getLong("INSFID"));
			user.setUserPosition(json.getString("POSITION"));
			user.setStatus(json.getInt("STATUSID"));
			user.setCreator(json.getString("CREATOR"));
			if(us.save(user)){
				return user.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public boolean update(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			User user = new User();
			user.setId(json.getInt("UID"));
			user.setUserName(json.getString("UNAME"));
			user.setUserPassword(json.getString("PASSWORD"));
			user.setUserLoginName(json.getString("LOGINNAME"));
			user.setUserEmail(json.getString("EMAIL"));
			user.setUserPhone(json.getString("PHONE"));
			user.setUserInsframework(json.getLong("INSFID"));
			user.setUserPosition(json.getString("POSITION"));
			user.setStatus(json.getInt("STATUSID"));
			user.setModifier("MODIFIER");
			return us.update(user);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean delete(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return us.delete(json.getInt("UID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public  Object findById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			User list = us.findById(json.getInt("UID"));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("NAME",jutil.setValue(list.getUserName()));
				obj.put("PASSWORD",jutil.setValue(list.getUserPassword()));
				obj.put("LOGINNAME",jutil.setValue(list.getUserLoginName()));
				obj.put("PHONE",jutil.setValue(list.getUserPhone()));
				obj.put("EMAIL",jutil.setValue(list.getUserEmail()));
				obj.put("POSITION",jutil.setValue(list.getUserPosition()));
				obj.put("STATUSID",jutil.setValue(list.getStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.getStatusname()));
				obj.put("INSFRAMEWORKID",jutil.setValue(list.getInsid()));
				obj.put("INSFRAMEWORKNAME",jutil.setValue(list.getInsname()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String findByRoleId(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return us.findByRoleId(json.getInt("ROLEID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int findByName(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return us.findByName(json.getString("LOGINNAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Object findAll(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<User> list = us.findAll(new BigInteger(json.getString("INSFID")), json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("NAME",jutil.setValue(list.get(i).getUserName()));
				obj.put("PASSWORD",jutil.setValue(list.get(i).getUserPassword()));
				obj.put("LOGINNAME",jutil.setValue(list.get(i).getUserLoginName()));
				obj.put("PHONE",jutil.setValue(list.get(i).getUserPhone()));
				obj.put("EMAIL",jutil.setValue(list.get(i).getUserEmail()));
				obj.put("POSITION",jutil.setValue(list.get(i).getUserPosition()));
				obj.put("STATUSID",jutil.setValue(list.get(i).getStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.get(i).getStatusname()));
				obj.put("INSFRAMEWORKID",jutil.setValue(list.get(i).getInsid()));
				obj.put("INSFRAMEWORKNAME",jutil.setValue(list.get(i).getInsname()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findRole(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<User> list = us.findRole(json.getInt("UID"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("NAME",jutil.setValue(list.get(i).getUserName()));
				obj.put("PASSWORD",jutil.setValue(list.get(i).getUserPassword()));
				obj.put("LOGINNAME",jutil.setValue(list.get(i).getUserLoginName()));
				obj.put("PHONE",jutil.setValue(list.get(i).getUserPhone()));
				obj.put("EMAIL",jutil.setValue(list.get(i).getUserEmail()));
				obj.put("POSITION",jutil.setValue(list.get(i).getUserPosition()));
				obj.put("STATUSID",jutil.setValue(list.get(i).getStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.get(i).getStatusname()));
				obj.put("INSFRAMEWORKID",jutil.setValue(list.get(i).getInsid()));
				obj.put("INSFRAMEWORKNAME",jutil.setValue(list.get(i).getInsname()));
				obj.put("ROLEID",jutil.setValue(list.get(i).getRoleId()));
				obj.put("ROLENAME",jutil.setValue(list.get(i).getRoleName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAllRole() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<User> list = us.findAllRole();
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("ROLENAME",jutil.setValue(list.get(i).getRoleName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getUsernameCount(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return us.getUsernameCount(json.getString("LOGINNAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Object LoadUser(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			User list = us.LoadUser(json.getString("LOGINNAME"));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("PASSWORD",jutil.setValue(list.getUserPassword()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getAuthoritiesByUsername(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			List<String> list = us.getAuthoritiesByUsername(json.getString("LOGINNAME"));
			return JSON.toJSONString(list);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getIns(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<User> list = us.getIns(new BigInteger(json.getString("INSFID")));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getInsid()));
				obj.put("NAME",jutil.setValue(list.get(i).getInsname()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getUserInsframework(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			User list = us.getUserInsframework(new BigInteger(json.getString("UID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("NAME",jutil.setValue(list.getUserName()));
				obj.put("INSFRAMEWORKNAME",jutil.setValue(list.getInsname()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getInsUser(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<User> list = us.getInsUser(json.getInt("INSFID"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("NAME",jutil.setValue(list.get(i).getUserName()));
				obj.put("PASSWORD",jutil.setValue(list.get(i).getUserPassword()));
				obj.put("LOGINNAME",jutil.setValue(list.get(i).getUserLoginName()));
				obj.put("PHONE",jutil.setValue(list.get(i).getUserPhone()));
				obj.put("EMAIL",jutil.setValue(list.get(i).getUserEmail()));
				obj.put("POSITION",jutil.setValue(list.get(i).getUserPosition()));
				obj.put("STATUSID",jutil.setValue(list.get(i).getStatus()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

}
