package com.spring.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dao.WeldedJunctionMapper;
import com.spring.dto.JudgeUtil;
import com.spring.model.Insframework;
import com.spring.model.WeldedJunction;
import com.spring.service.WeldedJunctionService;

import io.netty.channel.socket.SocketChannel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Transactional
@Service
public class WeldedJunctionServiceImpl implements WeldedJunctionService {
	@Autowired
	private WeldedJunctionMapper wjm;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	public Client client = new Client(this);
	
	public SocketChannel socketChannel = null;
	
	public String data = "";
	
	@Override
	public Object getWeldedJunctionAll() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = df.format(new Date());
			String startTime = currentTime.substring(0,10)+" 00:00:00";
			List<WeldedJunction> list = wjm.getWeldedJunctionAll(startTime);
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("TASKNO",jutil.setValue(list.get(i).getWeldedJunctionno()));
				obj.put("TASKDES",jutil.setValue(list.get(i).getSerialNo()));
				obj.put("WELDERID", jutil.setValue(list.get(i).getWelderid()));
				obj.put("WELDERNO",jutil.setValue(list.get(i).getPipelineNo()));
				obj.put("WELDERNAME",jutil.setValue(list.get(i).getRoomNo()));
				obj.put("MACHINEID", jutil.setValue(list.get(i).getMachineid()));
				obj.put("MACHINENO", jutil.setValue(list.get(i).getUnit()));
				obj.put("ITEMID",jutil.setValue(list.get(i).getIid()));
				obj.put("ITEMNAME",jutil.setValue(list.get(i).getIname()));
				obj.put("OPERATESTATUS",jutil.setValue(list.get(i).getSystems()));
				obj.put("STARTTIME", jutil.setValue(list.get(i).getStartTime()));
				obj.put("ENDTIME", jutil.setValue(list.get(i).getEndTime()));
				obj.put("REWELDERID", jutil.setValue(list.get(i).getOperatorid()));
				obj.put("REWELDERNO", jutil.setValue(list.get(i).getChildren()));
				obj.put("REWELDERNAME", jutil.setValue(list.get(i).getSpecification()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getWeldedJunctionById(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			WeldedJunction list = wjm.getWeldedJunctionById(new BigInteger(json.getString("ID")));
			if(list!=null){
				obj.put("ID", jutil.setValue(list.getId()));
				obj.put("JUNCTIONNO",jutil.setValue(list.getWeldedJunctionno()));
				obj.put("SERIALNO",jutil.setValue(list.getSerialNo()));
				obj.put("PIPELINENO",jutil.setValue(list.getPipelineNo()));
				obj.put("ROOMNO",jutil.setValue(list.getRoomNo()));
				obj.put("DYNE",jutil.setValue(list.getDyne()));
				obj.put("UPDATECOUNT",jutil.setValue(list.getUpdatecount()));
				obj.put("ITEMID",jutil.setValue(list.getIid()));
				obj.put("ITEMNAME",jutil.setValue(list.getIname()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getWeldedjunctionByNo(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return wjm.getWeldedjunctionByNo(json.getString("JUNCTIONNO"));
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public boolean addJunction(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			WeldedJunction wj = new WeldedJunction();
			wj.setWeldedJunctionno(json.getString("JUNCTIONNO"));
/*			if(json.getString("DYNE").isEmpty()){
				wj.setUnit(null);
			}else{
				wj.setUnit((json.getString("DYNE")));
			}*/
			if(json.getString("TASKLEVEL").isEmpty()){
				wj.setRoomNo(null);
			}else{
				wj.setRoomNo(json.getString("TASKLEVEL"));
			}
/*			if(json.getString("EXTERNALDIAMETER").isEmpty()){
				wj.setExternalDiameter(null);
			}else{
				wj.setExternalDiameter(json.getString("EXTERNALDIAMETER"));
			}*/
			String starttime =json.getString("STARTTIME");
			if(starttime!=null && !"".equals(starttime)){
				wj.setStartTime(starttime);
			}
			String endtime =json.getString("ENDTIME");
			if(endtime!=null && !"".equals(endtime)){
				wj.setEndTime(endtime);
			}
/*			Insframework itemid = new Insframework();
			itemid.setId(new BigInteger(json.getString("INSFID")));*/
			wj.setIid(new BigInteger(json.getString("INSFID")));
			return wjm.addJunction(wj);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateJunction(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			WeldedJunction wj = new WeldedJunction();
			wj.setId(new BigInteger(json.getString("ID")));
			wj.setWeldedJunctionno(json.getString("JUNCTIONNO"));
/*			if(json.getString("DYNE").isEmpty()){
				wj.setUnit(null);
			}else{
				wj.setUnit((json.getString("DYNE")));
			}*/
			if(json.getString("TASKLEVEL").isEmpty()){
				wj.setRoomNo(null);
			}else{
				wj.setRoomNo(json.getString("TASKLEVEL"));
			}
/*			if(json.getString("EXTERNALDIAMETER").isEmpty()){
				wj.setExternalDiameter(null);
			}else{
				wj.setExternalDiameter(json.getString("EXTERNALDIAMETER"));
			}*/
			String starttime =json.getString("STARTTIME");
			if(starttime!=null && !"".equals(starttime)){
				wj.setStartTime(starttime);
			}
			String endtime =json.getString("ENDTIME");
			if(endtime!=null && !"".equals(endtime)){
				wj.setEndTime(endtime);
			}
/*			Insframework itemid = new Insframework();
			itemid.setId(new BigInteger(json.getString("INSFID")));*/
			wj.setIid(new BigInteger(json.getString("INSFID")));
			return wjm.updateJunction(wj);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteJunction(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return wjm.deleteJunction(new BigInteger(json.getString("ID")));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object getTaskResultAll(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<WeldedJunction> list = wjm.getTaskResultAll(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", jutil.setValue(list.get(i).getId()));
				obj.put("TASKNO",jutil.setValue(list.get(i).getWeldedJunctionno()));
				obj.put("WELDERNAME",jutil.setValue(list.get(i).getSerialNo()));
				obj.put("MACHINENO",jutil.setValue(list.get(i).getPipelineNo()));
				obj.put("OPERATETYPENAME",jutil.setValue(list.get(i).getRoomNo()));
				obj.put("RESULT",jutil.setValue(list.get(i).getArea()));
				obj.put("RESULTNAME",jutil.setValue(list.get(i).getUnit()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public boolean addTaskResult(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			WeldedJunction wj = new WeldedJunction();
			wj.setTaskid(new BigInteger(json.getString("TASKID")));
			wj.setWelderid(new BigInteger(json.getString("WELDERID")));
			wj.setMachineid(new BigInteger(json.getString("MACHINEID")));
			wj.setDyne(Integer.valueOf(json.getString("OPERATETYPEID")));
			wj.setOperatorid(new BigInteger(json.getString("OPERATORID")));
			wj.setChildren(json.getString("RESULTID"));
			wj.setArea(json.getString("RESULT"));
			wjm.addTaskResult(wj);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateTaskResult(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			WeldedJunction wj = new WeldedJunction();
			wj.setId(new BigInteger(json.getString("ID")));
			wj.setTaskid(new BigInteger(json.getString("TASKID")));
			wj.setWelderid(new BigInteger(json.getString("WELDERID")));
			wj.setMachineid(new BigInteger(json.getString("MACHINEID")));
			wj.setDyne(Integer.valueOf(json.getString("STATUS")));
			wj.setOperatorid(new BigInteger(json.getString("OPERATOR")));
			wj.setChildren(json.getString("RESULTID"));
			wj.setArea(json.getString("RESULT"));
			wjm.updateTaskResult(wj);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteTaskResult(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean giveToServer(String object) {
		boolean data=true;
		boolean a = false;
		int time=0;
		JSONObject json = JSONObject.fromObject(object);
		String taskno = json.getString("TASKNO");
		String welderno = json.getString("WELDERNO");
		String machineno = json.getString("MACHINENO");
		String taskid = json.getString("TASKID");
		String welderid = json.getString("WELDERID");
		String machineid = json.getString("MACHINEID");
		int status = Integer.valueOf(json.getString("STATUS"));
		WeldedJunction wj = new WeldedJunction();
		socketChannel = null;
		if(status==1){
			String operatorid = json.getString("OPERATOR");
			wj.setTaskid(new BigInteger(taskid));
/*			if(welderid==null){
				wj.setWelderid(null);
			}else{
				wj.setWelderid(new BigInteger(welderid));
			}
			if(machineid==null){
				wj.setMachineid(null);
			}else{
				wj.setMachineid(new BigInteger(machineid));
			}*/
			wj.setDyne(Integer.valueOf(status));
			wj.setOperatorid(new BigInteger(operatorid));
			wj.setArea(json.getString("RESULT"));
			if(json.getString("RESULTID").isEmpty()){
				wj.setChildren(null);
			}else{
				wj.setChildren(json.getString("RESULTID"));
			}
			client.run();
			while(!a){
				time++;
				int count1 = 0;
				while(socketChannel == null){
					count1++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(count1 == 2){
						break;
					}
				}
				if(socketChannel != null){
					try {
						socketChannel.writeAndFlush("JN"+","+taskid+","+welderid+","+machineid+","+status+","+machineno).sync();
						socketChannel.close();
						int count = wjm.getCountBySatus(new BigInteger(taskid),null,null,status);
						if(count<2){
							wj.setStartTime(json.getString("STARTTIME"));
							wj.setEndTime(json.getString("ENDTIME"));
							wjm.addTaskResult(wj);
						}else{
							wj.setId(new BigInteger(json.getString("ID")));
							wj.setStartTime(json.getString("STARTTIME"));
							wj.setEndTime(json.getString("ENDTIME"));
							wjm.updateTaskResult(wj);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					a = true;
				}else{
					if(time>10){
						data=false;
						break;
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}else if(status==2){
			String operatorid = json.getString("OPERATOR");
			wj.setTaskid(new BigInteger(taskid));
			wj.setWelderid(new BigInteger(welderid));
			wj.setMachineid(new BigInteger(machineid));
			wj.setDyne(Integer.valueOf(status));
			wj.setOperatorid(new BigInteger(operatorid));
			client.run();
			while(!a){
				time++;
				int count1 = 0;
				while(socketChannel == null){
					count1++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(count1 == 2){
						break;
					}
				}
				if(socketChannel != null){
					try {
						socketChannel.writeAndFlush("JN"+","+taskid+","+welderid+","+machineid+","+status+","+machineno).sync();
						socketChannel.close();
						int count = wjm.getCountBySatus(new BigInteger(taskid),new BigInteger(welderid),new BigInteger(machineid),status);
						if(count<1){
							wj.setStartTime(json.getString("STARTTIME"));
							wjm.addTaskResult(wj);
							wj.setId(new BigInteger(json.getString("ID")));
							wj.setDyne(3);
							wjm.updateTaskResult(wj);
						}else{
							wj.setStartTime(json.getString("STARTTIME"));
							wj.setId(new BigInteger(json.getString("ID")));
							wjm.updateTaskResult(wj);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					a = true;
				}else{
					if(time>10){
						data=false;
						break;
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}else if(status==3){
			String operatorid = json.getString("OPERATOR");
			wj.setTaskid(new BigInteger(taskid));
/*			wj.setWelderid(new BigInteger(welderid));
			wj.setMachineid(new BigInteger(machineid));*/
			wj.setDyne(Integer.valueOf(status));
			wj.setOperatorid(new BigInteger(operatorid));
			wj.setId(new BigInteger(json.getString("ID")));
			wjm.deleteTaskResult(new BigInteger(taskid));
//			client.run();
//			while(!a){
//				time++;
//				int count = 0;
//				while(socketChannel == null){
//					count++;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if(count == 2){
//						break;
//					}
//				}
//				if(socketChannel != null){
//					try {
//						socketChannel.writeAndFlush("JN"+","+taskid+","+welderid+","+machineid+","+status+","+machineno).sync();
//						socketChannel.close();
//						wjm.deleteTaskResult(new BigInteger(taskid));
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					a = true;
//				}else{
//					if(time>10){
//						data=false;
//						break;
//					}else{
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
			
		}else{
			wj.setTaskid(new BigInteger(taskid));
			wj.setWelderid(new BigInteger(welderid));
			wj.setMachineid(new BigInteger(machineid));
			wj.setDyne(Integer.valueOf(status));
			client.run();
			while(!a){
				time++;
				int count = 0;
				while(socketChannel == null){
					count++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(count == 2){
						break;
					}
				}
				if(socketChannel != null){
					try {
						socketChannel.writeAndFlush("JN"+","+taskid+","+welderid+","+machineid+","+status+","+machineno).sync();
						socketChannel.close();
						wj.setStartTime(json.getString("STARTTIME"));
						wjm.addTaskResult(wj);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					a = true;
				}else{
					if(time>10){
						data=false;
						break;
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		return data;
	}

	@Override
	public Object getMachineData() {
		
		socketChannel = null;
		data = "";
		client.run();
		int count = 0;
		while(socketChannel == null){
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count == 2){
				break;
			}
		}
		if(socketChannel != null){
			try {
				socketChannel.writeAndFlush("SS").sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int count1 = 0;
		while(data.equals("")){
			count1++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(count1 == 2){
				break;
			}
		}
		try {
			socketChannel.close().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
//		return "[{\"num\":\"0001\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0002\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0003\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0004\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0005\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0006\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0007\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0008\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0010\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0009\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0013\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0011\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0012\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"},{\"num\":\"0105\",\"ele1\":\"0\",\"vol1\":\"0\",\"time1\":\"0\",\"status1\":\"0\",\"ele2\":\"0\",\"vol2\":\"0\",\"time2\":\"0\",\"status2\":\"0\",\"ele3\":\"0\",\"vol3\":\"0\",\"time3\":\"0\",\"status3\":\"0\"}]";
	}
	
}
