package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Insframework;
import com.spring.service.InsframeworkService;
import com.sshome.ssmcxf.webservice.InsfWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class InsfWebServiceImpl implements InsfWebService {
	@Autowired
	private InsframeworkService is;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public Object getInsframeworkAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Insframework> list = is.getInsframeworkAll(new BigInteger(json.getString("INSFID")), json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", list.get(i).getId());
				obj.put("NAME",list.get(i).getName());
				obj.put("LOGOGRAM",jutil.setValue(list.get(i).getLogogram()));
				obj.put("CODE",jutil.setValue(list.get(i).getCode()));
				obj.put("PARENTID",jutil.setValue(list.get(i).getParent()));
				obj.put("PARENTNAME",jutil.setValue(list.get(i).getParentname()));
				obj.put("TYPEID",jutil.setValue(list.get(i).getType()));
				obj.put("TYPENAME",jutil.setValue(list.get(i).getTypename()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger addInsframework(String obj1,String obj2) {
		try{
			JSONObject json = JSONObject.fromObject(obj2);
			Insframework i = new Insframework();
			i.setName(json.getString("NAME"));
			i.setLogogram(json.getString("LOGOGRAM"));
			i.setCode(json.getString("CODE"));
			i.setParent(new BigInteger(json.getString("PARENT")));
			i.setType(json.getInt("TYPEID"));
			i.setCreator(json.getString("CREATOR"));
			if(is.addInsframework(i)){
				return i.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean editInsframework(String obj1,String obj2) {
		try{
			JSONObject json = JSONObject.fromObject(obj2);
			Insframework i = new Insframework();
			i.setId(new BigInteger(json.getString("INSFID")));
			i.setName(json.getString("NAME"));
			i.setLogogram(json.getString("LOGOGRAM"));
			i.setCode(json.getString("CODE"));
			i.setParent(new BigInteger(json.getString("PARENT")));
			i.setType(json.getInt("TYPEID"));
			i.setModifier(json.getString("MODIFIER"));
			return is.editInsframework(i);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteInsframework(String obj1,String obj2) {
		try{
			JSONObject json = JSONObject.fromObject(obj2);
			return is.deleteInsframework(new BigInteger(json.getString("INSFID")));
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public int getInsframeworkNameCount(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return is.getInsframeworkNameCount(json.getString("NAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public String getInsframeworkById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return is.getInsframeworkById(new BigInteger(json.getString("INSFID")));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getInsfAllById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Insframework list = is.getInsfAllById(new BigInteger(json.getString("INSFID")));
			if(list!=null){
				obj.put("ID", list.getId());
				obj.put("NAME",list.getName());
				obj.put("LOGOGRAM",jutil.setValue(list.getLogogram()));
				obj.put("CODE",jutil.setValue(list.getCode()));
				obj.put("PARENTID",jutil.setValue(list.getParent()));
				obj.put("TYPEID",jutil.setValue(list.getType()));
				obj.put("TYPENAME",jutil.setValue(list.getTypename()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getBloc() {
		try{
			JSONObject obj = new JSONObject();
			Insframework list = is.getBloc();
			if(list!=null){
				obj.put("ID", list.getId());
				obj.put("NAME",list.getName());
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getCompany(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			String id = json.getString("INSFID");
			BigInteger insfid = null;
			if(id!=null && !"".equals(id)){
				insfid = new BigInteger(id);
			}
			List<Insframework> list =  is.getConmpany(insfid);
			for(int i=0;i<list.size();i++){
				obj.put("ID", list.get(i).getId());
				obj.put("NAME",list.get(i).getName());
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getCause(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			String id = json.getString("INSFID");
			BigInteger insfid = null;
			if(id!=null && !"".equals(id)){
				insfid = new BigInteger(id);
			}
			List<Insframework> list =  is.getCause(new BigInteger(json.getString("COMPANYID")),insfid);
			for(int i=0;i<list.size();i++){
				obj.put("ID", list.get(i).getId());
				obj.put("NAME",list.get(i).getName());
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getWeldingMachineInsf(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			String id = json.getString("INSFID");
			BigInteger insfid = null;
			if(id!=null && !"".equals(id)){
				insfid = new BigInteger(id);
			}
			List<Insframework> list =  is.getWeldingMachineInsf(insfid);
			for(int i=0;i<list.size();i++){
				obj.put("ID", list.get(i).getId());
				obj.put("NAME",list.get(i).getName());
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getParent(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			String id = json.getString("INSFID");
			BigInteger insfid = null;
			if(id!=null && !"".equals(id)){
				insfid = new BigInteger(id);
			}
			Insframework list = is.getParent(insfid);
			if(list!=null){
				obj.put("ID", list.getId());
				obj.put("NAME",list.getName());
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getInsByType(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			String id = json.getString("INSFID");
			BigInteger insfid = null;
			if(id!=null && !"".equals(id)){
				insfid = new BigInteger(id);
			}
			List<Insframework> list =  is.getInsByType(json.getInt("TYPEID"), insfid);
			for(int i=0;i<list.size();i++){
				obj.put("ID", list.get(i).getId());
				obj.put("NAME",list.get(i).getName());
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getUserInsfType(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return is.getUserInsfType(new BigInteger(json.getString("USERID")));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public BigInteger getUserInsfId(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return is.getUserInsfId(new BigInteger(json.getString("USERID")));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getTypeById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return is.getTypeById(new BigInteger(json.getString("INSFID")));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public BigInteger getParentById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return is.getParentById(new BigInteger(json.getString("INSFID")));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getInsByUserid(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			List<Insframework> list =   is.getInsByUserid(new BigInteger(json.getString("USERID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.get(0).getId()));
				obj.put("TYPEID",jutil.setValue(list.get(0).getType()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}
}
