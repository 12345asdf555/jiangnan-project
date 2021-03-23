package com.sshome.ssmcxf.webservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Role;
import com.spring.service.RoleService;
import com.sshome.ssmcxf.webservice.RoleWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class RoleWebServiceImpl implements RoleWebService{
	@Autowired
	private RoleService ros;
	
	private JudgeUtil jutil = new JudgeUtil();

	@Override
	public int saveRolesAuthority(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			Role r = new Role();
			r.setId(json.getInt("ROLEID"));
			r.setAuthorityId(json.getInt("AUTHID"));
			r.setCreator(json.getString("CREATOR"));
			if(ros.saveAuthority(r)){
				return r.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public int saveRoles(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			Role role = new Role();
			role.setRoleName(json.getString("ROLENAME"));
			role.setRoleDesc(json.getString("DESC"));
			role.setRoleStatus(json.getInt("STATUSID"));
			role.setCreator(json.getString("CREATOR"));
			if(ros.save(role)){
				return role.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public int saveUserRole(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			Role role = new Role();
			role.setId(json.getInt("ROLEID"));
			role.setUserId(json.getInt("USERID"));
			role.setCreator(json.getString("CREATOR"));
			if(ros.saveUser(role)){
				return role.getId();
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public boolean updateRole(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			Role role = new Role();
			role.setId(json.getInt("ROLEID"));
			role.setRoleName(json.getString("ROLENAME"));
			role.setRoleDesc(json.getString("DESC"));
			role.setRoleStatus(json.getInt("STATUSID"));
			role.setModifier(json.getString("MODIFIER"));
			return ros.update(role);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteRole(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.delete(json.getInt("ROLEID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteUsersRoles(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.delete1(json.getInt("ROLEID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteRolesAuthoritiesByRole(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.delete2(json.getString("ROLEID"));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Object findRoleById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Role list = ros.findById(json.getInt("ROLEID"));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("ROLENAME",jutil.setValue(list.getRoleName()));
				obj.put("ROLEDESC",jutil.setValue(list.getRoleDesc()));
				obj.put("STATUSID",jutil.setValue(list.getRoleStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.getStatusname()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String findAuthorityDescById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.findByAuthorityId(json.getInt("AUTHID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String findUserNameById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.findByUserId(json.getInt("USERID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findRoleAll(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Role> list = ros.findAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("ROLENAME",jutil.setValue(list.get(i).getRoleName()));
				obj.put("ROLEDESC",jutil.setValue(list.get(i).getRoleDesc()));
				obj.put("STATUSID",jutil.setValue(list.get(i).getRoleStatus()));
				obj.put("STATUSNAME",jutil.setValue(list.get(i).getStatusname()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findIdDescByAuthority() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Role> list = ros.findAllAuthority();
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("AUTHORITYDESC",jutil.setValue(list.get(i).getAuthorityDesc()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAuthorityDetail(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Role> list = ros.findAuthority(json.getInt("ROLEID"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("ROLENAME",jutil.setValue(list.get(i).getRoleName()));
				obj.put("AUTHORITYID",jutil.setValue(list.get(i).getAuthorityId()));
				obj.put("AUTHORITYDESC",jutil.setValue(list.get(i).getAuthorityDesc()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getRoleNameById(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.updateRoleAuthority(json.getInt("ROLEID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAllUser() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Role> list = ros.findAllUser();
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("USERNAME",jutil.setValue(list.get(i).getUserName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findUserRoleDetail(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Role> list = ros.findUser(json.getInt("ROLEID"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("ROLENAME",jutil.setValue(list.get(i).getRoleName()));
				obj.put("ROLEDESC",jutil.setValue(list.get(i).getRoleDesc()));
				obj.put("ROLESTATUSID",jutil.setValue(list.get(i).getRoleStatus()));
				obj.put("USERID",jutil.setValue(list.get(i).getUserId()));
				obj.put("USERNAME",jutil.setValue(list.get(i).getUserName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getRolenameCount(String object) {
		try{
			JSONObject json  = JSONObject.fromObject(object);
			return ros.getRolenameCount(json.getString("ROLENAME"));
		}catch(Exception e){
			return -1;
		}
	}


}
