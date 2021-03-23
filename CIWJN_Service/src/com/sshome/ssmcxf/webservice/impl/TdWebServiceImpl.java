package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Td;
import com.spring.service.TdService;
import com.sshome.ssmcxf.webservice.TdWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class TdWebServiceImpl implements TdWebService{
	@Autowired
	private TdService ts;

	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public Object findAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Td> list = ts.findAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("NAME",jutil.setValue(list.get(i).getFname()));
				obj.put("WELDERNO",jutil.setValue(list.get(i).getFwelder_no()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object findAlldiv(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Td> list =  ts.findAlldiv(json.getLong("INSFID"));
			for(int i=0;i<list.size();i++){
				obj.put("INSFID", jutil.setValue(list.get(i).getInsfId()));
				obj.put("INSFNAME",jutil.setValue(list.get(i).getInsfName()));
				obj.put("INSFPARENT",jutil.setValue(list.get(i).getInsfParent()));
				obj.put("INSFTYPE",jutil.setValue(list.get(i).getInsfType()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getAllPosition(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Td> list = ts.getAllPosition(BigInteger.valueOf(json.getLong("INSFID")));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("MACHINENO",jutil.setValue(list.get(i).getFequipment_no()));
				obj.put("POSITION",jutil.setValue(list.get(i).getFposition()));
				obj.put("INSFID",jutil.setValue(list.get(i).getFci()));
				obj.put("INSFNAME",jutil.setValue(list.get(i).getFcn()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public long findIns(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ts.findIns(json.getLong("UID"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public long findInsid(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ts.findInsid(json.getString("INSFNAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public String findweld(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ts.findweld(json.getString("WELDNO"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String findInsname(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ts.findInsname(json.getLong("INSFID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String findPosition(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ts.findPosition(json.getString("MACHINENO"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object allWeldname() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Td> list = ts.allWeldname();
			for(int i=0;i<list.size();i++){
				obj.put("NAME",jutil.setValue(list.get(i).getFname()));
				obj.put("WELDERNO",jutil.setValue(list.get(i).getFwelder_no()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int findDic(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ts.findDic(json.getLong("UID"));
		}catch(Exception e){
			return -1;
		}
	}
}
