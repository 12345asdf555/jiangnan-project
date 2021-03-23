package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.MaintenanceRecord;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.service.MaintainService;
import com.sshome.ssmcxf.webservice.MaintainWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class MaintainWebServiceImpl implements MaintainWebService {
	@Autowired
	private MaintainService ms;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public Object getWeldingMaintenanceAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<WeldingMaintenance> list = ms.getWeldingMaintenanceAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("INSFID",jutil.setValue(list.get(i).getInsfid()));
				if(list.get(i).getMaintenance()!=null){
					obj.put("MONEY",jutil.setValue(list.get(i).getMaintenance().getMoney()));
					obj.put("MAINTENANCEID",jutil.setValue(list.get(i).getMaintenance().getId()));
					obj.put("VICEMAN",jutil.setValue(list.get(i).getMaintenance().getViceman()));
					obj.put("TYPEID",jutil.setValue(list.get(i).getMaintenance().getTypeId()));
					obj.put("TYPENAME",jutil.setValue(list.get(i).getMaintenance().getTypename()));
					obj.put("STARTTIME",jutil.setValue(list.get(i).getMaintenance().getStartTime()));
					obj.put("ENDTIME",jutil.setValue(list.get(i).getMaintenance().getEndTime()));
					obj.put("DESC",jutil.setValue(list.get(i).getMaintenance().getDesc()));
				}else{
					obj.put("MONEY", "");
					obj.put("MAINTENANCEID", "");
					obj.put("VICEMAN", "");
					obj.put("TYPEID", "");
					obj.put("TYPENAME", "");
					obj.put("STARTTIME", "");
					obj.put("ENDTIME", "");
					obj.put("DESC", "");
				}
				if(list.get(i).getWelding()!=null){
					obj.put("MACHINEID",jutil.setValue(list.get(i).getWelding().getId()));
					obj.put("MACHINENO",jutil.setValue(list.get(i).getWelding().getEquipmentNo()));
				}else{
					obj.put("MACHINEID", "");
					obj.put("MACHINENO", "");
				}
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getEndtime(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<WeldingMaintenance> list = ms.getEndtime(new BigInteger(json.getString("WID")));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				if(list.get(i).getMaintenance()!=null){
					obj.put("VICEMAN",jutil.setValue(list.get(i).getMaintenance().getViceman()));
					obj.put("TYPEID",jutil.setValue(list.get(i).getMaintenance().getTypeId()));
					obj.put("STARTTIME",jutil.setValue(list.get(i).getMaintenance().getStartTime()));
					obj.put("DESC",jutil.setValue(list.get(i).getMaintenance().getDesc()));
				}else{
					obj.put("VICEMAN", "");
					obj.put("TYPEID", "");
					obj.put("STARTTIME", "");
					obj.put("DESC", "");
				}
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getWeldingMaintenanceById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			WeldingMaintenance list = ms.getWeldingMaintenanceById(new BigInteger(json.getString("WID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				if(list.getMaintenance()!=null){
					obj.put("MONEY",jutil.setValue(list.getMaintenance().getMoney()));
					obj.put("MAINTENANCEID",jutil.setValue(list.getMaintenance().getId()));
					obj.put("VICEMAN",jutil.setValue(list.getMaintenance().getViceman()));
					obj.put("TYPEID",jutil.setValue(list.getMaintenance().getTypeId()));
					obj.put("STARTTIME",jutil.setValue(list.getMaintenance().getStartTime()));
					obj.put("ENDTIME",jutil.setValue(list.getMaintenance().getEndTime()));
					obj.put("DESC",jutil.setValue(list.getMaintenance().getDesc()));
				}else{
					obj.put("MONEY", "");
					obj.put("MAINTENANCEID", "");
					obj.put("VICEMAN", "");
					obj.put("TYPEID", "");
					obj.put("STARTTIME", "");
					obj.put("ENDTIME", "");
					obj.put("DESC", "");
				}
				if(list.getWelding()!=null){
					obj.put("MACHINEID",jutil.setValue(list.getWelding().getId()));
					obj.put("MACHINENO",jutil.setValue(list.getWelding().getEquipmentNo()));
				}else{
					obj.put("MACHINEID", "");
					obj.put("MACHINENO", "");
				}
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getEquipmentno() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<WeldingMachine> list = ms.getEquipmentno();
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("MACHINENO", jutil.setValue(list.get(i).getEquipmentNo()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigInteger addMaintian(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			WeldingMaintenance wm = new WeldingMaintenance();
			MaintenanceRecord mr = new MaintenanceRecord();
			mr.setId(new BigInteger(json.getString("RID")));
			mr.setEndTime(json.getString("ENDTIME"));
			wm.setMaintenance(mr);
			wm.setCreator(json.getString("CREATOR"));
			WeldingMachine w = new WeldingMachine();
			BigInteger wid = new BigInteger(json.getString("WELDID"));
			w.setId(wid);
			wm.setWelding(w);
			if(ms.addMaintian(wm,mr,wid)){
				return wm.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public BigInteger addMaintenanceRecord(String object){
		try{
			JSONObject json = JSONObject.fromObject(object);
			MaintenanceRecord mr = new MaintenanceRecord();
			mr.setViceman(json.getString("VICEMAN"));
			mr.setStartTime(json.getString("STARTTIME"));
			mr.setMoney(json.getInt("MONEY"));
			String endTime = json.getString("ENDTIME");
			if(endTime!=null && !"".equals(endTime)){
				mr.setEndTime(json.getString("ENDTIME"));
			}
			mr.setDesc(json.getString("DESC"));
			mr.setTypeId(json.getInt("TYPEID"));
			mr.setCreator(json.getString("CREATOR"));
			if(ms.addMaintenanceRecord(mr)){
				return mr.getId();
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean updateEndtime(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			boolean flag = ms.updateEndtime(new BigInteger(json.getString("MID")));
			BigInteger weldingid = new BigInteger(json.getString("WELDINGID"));
			List<WeldingMaintenance> list =  ms.getEndtime(weldingid);
			if(list.isEmpty()){
				//如果维修结束时间没有为空的则修改状态为启用
				ms.editstatus(weldingid, 31);
			}
			return flag;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateMaintenanceRecord(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			MaintenanceRecord mr = new MaintenanceRecord();
			mr.setId(new BigInteger(json.getString("MID")));
			mr.setViceman(json.getString("VICEMAN"));
			mr.setMoney(json.getInt("MONEY"));
			mr.setStartTime(json.getString("STARTTIME"));
			String endTime = json.getString("ENDTIME");
			if(endTime!=null && !"".equals(endTime)){
				mr.setEndTime(json.getString("ENDTIME"));
			}
			mr.setDesc(json.getString("DESC"));
			mr.setTypeId(json.getInt("TYPEID"));
			mr.setModifier(json.getString("MODIFIER"));
			boolean flag = ms.updateMaintenanceRecord(mr);
			BigInteger wid = new BigInteger(json.getString("WID"));
			List<WeldingMaintenance> list =  ms.getEndtime(wid);
			if(endTime==null || "".equals(endTime)){
				//修改焊机状态为维修中
				ms.editstatus(wid, 33);
			}
			if(list.isEmpty()){
				//如果维修结束时间没有为空的则修改状态为启用
				ms.editstatus(wid, 31);
			}
			return flag;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteMaintenanceRecord(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			WeldingMaintenance wm = ms.getWeldingMaintenanceById(new BigInteger(json.getString("MID")));
			boolean flag = ms.deleteMaintenanceRecord(wm.getMaintenance().getId());
			boolean flags = ms.deleteWeldingMaintenance(wm.getId());
			if(flag && flags){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteWeldingMaintenance(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ms.deleteWeldingMaintenance(new BigInteger(json.getString("WID")));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object getMaintainByWeldingMachinId(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<WeldingMaintenance> list = ms.getMaintainByWeldingMachinId(new BigInteger(json.getString("WID")));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("MAINTENANCEID", jutil.setValue(list.get(i).getMaintenance().getId()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean editstatus(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ms.editstatus(new BigInteger(json.getString("WID")), json.getInt("STATUSID"));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public BigInteger getInsfidByMachineid(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ms.getInsfidByMachineid(new BigInteger(json.getString("WID")));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
