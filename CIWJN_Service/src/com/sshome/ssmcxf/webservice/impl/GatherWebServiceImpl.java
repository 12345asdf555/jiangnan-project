package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Gather;
import com.spring.service.GatherService;
import com.sshome.ssmcxf.webservice.GatherWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class GatherWebServiceImpl implements GatherWebService{

	@Autowired
	private GatherService gs;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public Object getGatherAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			String id = json.getString("INSFID");
			BigInteger insfid = null;
			if(id!=null && !"".equals(id)){
				insfid = new BigInteger(id);
			}
			List<Gather> list = gs.getGatherAll(json.getString("STR"), insfid);
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("GATHERNO",jutil.setValue(list.get(i).getGatherNo()));
				obj.put("STATUS",jutil.setValue(list.get(i).getStatus()));
				obj.put("PROTOCOL",jutil.setValue(list.get(i).getProtocol()));
				obj.put("IPURL",jutil.setValue(list.get(i).getIpurl()));
				obj.put("MACURL",jutil.setValue(list.get(i).getMacurl()));
				obj.put("LEAVETIME",jutil.setValue(list.get(i).getLeavetime()));
				obj.put("ITEMNAME",jutil.setValue(list.get(i).getItemname()));
				obj.put("ITEMID",jutil.setValue(list.get(i).getItemid()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger getGatherByNo(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return gs.getGatherByNo(json.getString("GATHERNO"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getGatherNoCount(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return gs.getGatherNoCount(json.getString("GATHERNO"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Object getGatherById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Gather list = gs.getGatherById(new BigInteger(json.getString("ID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("GATHERNO",jutil.setValue(list.getGatherNo()));
				obj.put("STATUS",jutil.setValue(list.getStatus()));
				obj.put("PROTOCOL",jutil.setValue(list.getProtocol()));
				obj.put("IPURL",jutil.setValue(list.getIpurl()));
				obj.put("MACURL",jutil.setValue(list.getMacurl()));
				obj.put("LEAVETIME",jutil.setValue(list.getLeavetime()));
				obj.put("ITEMNAME",jutil.setValue(list.getItemname()));
				obj.put("ITEMID",jutil.setValue(list.getItemid()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public BigInteger addGather(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Gather g = new Gather();
			g.setGatherNo(json.getString("GATHERNO"));
			g.setIpurl(json.getString("IPURL"));
			g.setItemid(new BigInteger(json.getString("INSFID")));
			String leavetime =json.getString("LEAVETIME");
			if(leavetime!=null && !"".equals(leavetime)){
				g.setLeavetime(leavetime);
			}
			g.setMacurl(json.getString("MACURL"));
			g.setProtocol(json.getString("PROTOCOL"));
			g.setStatus(json.getString("STATUS"));
			g.setCreator(json.getString("CREATOR"));
			if(gs.addGather(g)){
				return g.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public boolean editGather(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			Gather g = new Gather();
			g.setId(new BigInteger(json.getString("ID")));
			g.setGatherNo(json.getString("GATHERNO"));
			g.setIpurl(json.getString("IPURL"));
			g.setItemid(new BigInteger(json.getString("INSFID")));
			String leavetime =json.getString("LEAVETIME");
			if(leavetime!=null && !"".equals(leavetime)){
				g.setLeavetime(leavetime);
			}
			g.setMacurl(json.getString("MACURL"));
			g.setProtocol(json.getString("PROTOCOL"));
			g.setStatus(json.getString("STATUS"));
			g.setModifier(json.getString("MODIFIER"));
			return gs.editGather(g);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean deleteGather(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return gs.deleteGather(new BigInteger(json.getString("ID")));
		}catch(Exception e){
			return false;
		}
	}
}
