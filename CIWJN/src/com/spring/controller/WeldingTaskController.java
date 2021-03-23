package com.spring.controller;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.cxf.endpoint.Client;
import javax.xml.namespace.QName;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.MyUser;
import com.spring.model.Person;
import com.spring.model.WeldingMachine;
import com.spring.model.Dictionarys;
import com.spring.model.Insframework;
import com.spring.model.WeldedJunction;
import com.spring.page.Page;
import com.spring.service.InsframeworkService;
import com.spring.service.LiveDataService;
import com.spring.service.PersonService;
import com.spring.service.WeldedJunctionService;
import com.spring.util.IsnullUtil;
import com.spring.service.DictionaryService;
import com.spring.service.WeldingMachineService;
import com.spring.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/weldtask", produces = { "text/json;charset=UTF-8" })
public class WeldingTaskController {

	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;

	@Autowired
	private WeldedJunctionService wjm;
	@Autowired
	private InsframeworkService insm;
	@Autowired
	private LiveDataService lm;
	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private DictionaryService dm;
	@Autowired
	private UserService fuser;
	@Autowired
	private PersonService ps;
	
	IsnullUtil iutil = new IsnullUtil();

	@RequestMapping("/goWeldTask")
	public String goWeldTask(HttpServletRequest request){
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		if(instype==20){
			
		}else if(instype==23){
			serach = "tb_welder.Fowner="+userinsid;
		}else{
			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
			for(Insframework inns : ls ){
				if(bz==0){
					serach=serach+"(tb_welder.Fowner="+inns.getId();
				}else{
					serach=serach+" or tb_welder.Fowner="+inns.getId();
				}
				bz++;
			}
			serach=serach+" or tb_welder.Fowner="+userinsid+")";
		}
		request.setAttribute("userinsall",serach );
		return "weldingtask/weldingtask";
	}
	
	@RequestMapping("/goTaskResult")
	public String goTaskResult(HttpServletRequest request){
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		if(instype==20){
			
		}else if(instype==23){
			serach = "and w.Fowner="+userinsid;
		}else{
			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
			for(Insframework inns : ls ){
				if(bz==0){
					serach=serach+"and (w.Fowner="+inns.getId();
				}else{
					serach=serach+" or w.Fowner="+inns.getId();
				}
				bz++;
			}
			serach=serach+" or w.Fowner="+userinsid+")";
		}
		request.setAttribute("userid",serach );
		request.setAttribute("userinsframework", fuser.getUserInsframework(new BigInteger(String.valueOf(user.getId()))).getInsname());
		return "weldingtask/taskresult";
	}
	@RequestMapping("/goTaskEvaluate")
	public String goTaskEvaluate(HttpServletRequest request){
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		request.setAttribute("userinsframework", fuser.getUserInsframework(new BigInteger(String.valueOf(user.getId()))).getInsname());
		return "weldingtask/taskevaluate";
	}
	@RequestMapping("/getWeldTaskList")
	@ResponseBody
	public String getWeldTaskList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		String parent = request.getParameter("parent");
		if(iutil.isNull(parent)){
			serach = parent;
		}else{
			if(instype==20){
				
			}else if(instype==23){
				serach = "j.fitemId="+userinsid;
			}else{
				List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+userinsid+")";
			}
		}
		if(!"".equals(request.getParameter("searchStr"))&&request.getParameter("searchStr")!=null&&serach!=null&&serach!=""){
			serach=serach+" and "+request.getParameter("searchStr");
		}
		if(!"".equals(request.getParameter("searchStr"))&&request.getParameter("searchStr")!=null&&(serach==null||serach=="")){
			serach=request.getParameter("searchStr");
		}
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getWeldedJunctionAll(page, serach);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("weldedJunctionno", w.getWeldedJunctionno());
//				json.put("serialNo", w.getSerialNo());
//				json.put("pipelineNo", w.getPipelineNo());
//				json.put("roomNo", w.getRoomNo());
//				json.put("levelid", w.getSystems());
				json.put("levelid", w.getRoomNo());
				json.put("levelname", w.getArea());
//				json.put("realwelder", w.getNext_material());
				json.put("itemname", w.getIname());
				json.put("itemid", w.getIid());
				if(w.getMaterial()==null){
					json.put("status", 2);
				}else if(Integer.valueOf(w.getMaterial())==1){
					json.put("status", 1);
				}else{
					json.put("status", 0);
				}
//				json.put("welderid", w.getDyne());
//				json.put("quali", w.getExternalDiameter());
				json.put("dtoTime1",w.getStartTime());
				json.put("dtoTime2", w.getEndTime());
				json.put("realStartTime", w.getCreatTime());
				json.put("realEndTime", w.getUpdateTime());
				json.put("taskResultId", w.getDyne());
				json.put("resultid", w.getUnit());
				json.put("result", w.getSystems());
				json.put("resultName", w.getChildren());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getWeldTaskListNoPage")
	@ResponseBody
	public String getWeldTaskListNoPage(HttpServletRequest request){
		String serach="";
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		int bz=0;
		String parent = "";
		if(iutil.isNull(parent)){
			serach = parent;
			serach+=" foperatetype is null";
		}else{
			if(instype==20){
				
			}else if(instype==23){
				serach = "j.fitemId="+userinsid;
			}else{
				List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+userinsid+")";
			}
			serach+=" and foperatetype is null";
		}
//		if(!"".equals(request.getParameter("searchStr"))&&request.getParameter("searchStr")!=null&&serach!=null&&serach!=""){
//			serach=serach+" and "+request.getParameter("searchStr");
//		}
//		if(!"".equals(request.getParameter("searchStr"))&&request.getParameter("searchStr")!=null&&(serach==null||serach=="")){
//			serach=request.getParameter("searchStr");
//		}
//		page = new Page(pageIndex,pageSize,total);  
		List<WeldedJunction> list = wjm.getWeldedJunctionAll(serach);
//		long total = 0;
//		
//		if(list != null){
//			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
//			total = pageinfo.getTotal();
//		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("weldedJunctionno", w.getWeldedJunctionno());
//				json.put("serialNo", w.getSerialNo());
//				json.put("pipelineNo", w.getPipelineNo());
//				json.put("roomNo", w.getRoomNo());
//				json.put("levelid", w.getSystems());
				json.put("levelid", w.getRoomNo());
				json.put("levelname", w.getArea());
//				json.put("realwelder", w.getNext_material());
				json.put("itemname", w.getIname());
				json.put("itemid", w.getIid());
				if(w.getMaterial()==null){
					json.put("status", 2);
				}else if(Integer.valueOf(w.getMaterial())==1){
					json.put("status", 1);
				}else{
					json.put("status", 0);
				}
//				json.put("welderid", w.getDyne());
//				json.put("quali", w.getExternalDiameter());
				json.put("dtoTime1",w.getStartTime());
				json.put("dtoTime2", w.getEndTime());
				json.put("realStartTime", w.getCreatTime());
				json.put("realEndTime", w.getUpdateTime());
				json.put("taskResultId", w.getDyne());
				json.put("resultid", w.getUnit());
				json.put("result", w.getSystems());
				json.put("resultName", w.getChildren());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
//		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getWeldTask")
	@ResponseBody
	public String getWeldTask(HttpServletRequest request){
		String serach="";
//		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
//		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
//		int bz=0;
//		if(instype==20){
//			
//		}else if(instype==23){
//			serach = "j.fitemId="+userinsid;
//		}else{
//			List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
//			for(Insframework inns : ls ){
//				if(bz==0){
//					serach=serach+"(j.fitemId="+inns.getId();
//				}else{
//					serach=serach+" or j.fitemId="+inns.getId();
//				}
//				bz++;
//			}
//			serach=serach+" or j.fitemId="+userinsid+")";
//		}
//		if(request.getParameter("searchStr")!=null&&serach!=null&&serach!=""){
//			serach=serach+" and "+request.getParameter("searchStr");
//		}
//		if(request.getParameter("searchStr")!=null&&(serach==null||serach=="")){
//			serach=serach+request.getParameter("searchStr");
//		}
		List<WeldedJunction> list = wjm.getWeldedJunction(serach);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("weldedJunctionno", w.getWeldedJunctionno());
				json.put("serialNo", w.getSerialNo());
				json.put("pipelineNo", w.getPipelineNo());
				json.put("roomNo", w.getRoomNo());
				json.put("levelid", w.getSystems());
				json.put("levelname", w.getArea());
				json.put("realwelder", w.getNext_material());
				json.put("itemname", w.getIname());
				json.put("itemid", w.getIid());
				if(w.getMaterial()==null){
					json.put("status", 2);
				}else if(Integer.valueOf(w.getMaterial())==1){
					json.put("status", 1);
				}else{
					json.put("status", 0);
				}
				json.put("welderid", w.getDyne());
				json.put("quali", w.getExternalDiameter());
				json.put("dtoTime1",w.getStartTime());
				json.put("dtoTime2", w.getEndTime());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getTaskResultList")
	@ResponseBody
	public String getTaskResultList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		String parent = request.getParameter("parent");
		String serach = "";
		page = new Page(pageIndex,pageSize,total);
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
		BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
		if(iutil.isNull(parent)){
			serach = parent;
		}else{
			int bz=0;
			if(instype==20){
				serach="";
			}else if(instype==23){
				serach = "j.fitemId="+userinsid;
			}else{
				List<Insframework> ls = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+userinsid+")";
			}
		}

		if(iutil.isNull(str)){
			if(iutil.isNull(serach)){
				serach += " and "+str;
			}else{
				serach = str;
			}
		}
		List<WeldedJunction> list = wjm.getTaskResultAll(page, serach);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("taskid", w.getCounts());
/*				json.put("welderid", w.getInsfid());
				json.put("machineid", w.getMachid());*/
				json.put("operateid",w.getDyne());
				json.put("result", w.getPipelineNo());
				json.put("resultid", w.getUpdatecount());
				json.put("taskNo",w.getWeldedJunctionno());
/*				json.put("welderNo", w.getSerialNo());
				json.put("machineNo", w.getMachine_num());*/
				json.put("resultName", w.getRoomNo());
				json.put("getdatatime", w.getUpdateTime());
				json.put("starttime", w.getStartTime());
				json.put("endtime", w.getEndTime());
				json.put("fitemid", w.getArea());
				json.put("user", w.getMaterial());
				json.put("itemname", w.getIname());
				json.put("itemid", w.getIid());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getRealWelder")
	@ResponseBody
	public String getRealWelder(HttpServletRequest request){
		String str = request.getParameter("searchStr");
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getRealWelder(page,new BigInteger(str));
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("id", w.getId());
				json.put("taskid", w.getCreater());
				json.put("taskno", w.getWeldedJunctionno());
				json.put("welderid", w.getIid());
				json.put("welderno", w.getRoomNo());
				json.put("weldername", w.getIname());
				json.put("machid", w.getMachid());
				json.put("machno", w.getMachine_num());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getJunctionByWelder")
	@ResponseBody
	public String getJunctionByWelder(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String welder = request.getParameter("welder");
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getJunctionByWelder(page, welder, dto);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("weldedJunctionno", w.getWeldedJunctionno().substring(2, 8));
				json.put("maxElectricity", w.getMaxElectricity());
				json.put("minElectricity", w.getMinElectricity());
				json.put("maxValtage", w.getMaxValtage());
				json.put("minValtage", w.getMinValtage());
				json.put("itemname", w.getIname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}

	/**
	 * 显示焊机列表
	 * @return
	 */
	@RequestMapping("/getWedlingMachineList")
	@ResponseBody
	public String getWedlingMachineList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String searchStr = request.getParameter("searchStr");
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		request.getSession().setAttribute("searchStr", searchStr);
		page = new Page(pageIndex,pageSize,total);
		List<WeldingMachine> list = wmm.getWeldingMachineAll(page,parent,searchStr);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldingMachine> pageinfo = new PageInfo<WeldingMachine>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldingMachine wm:list){
				json.put("id", wm.getId());
				json.put("ip", wm.getIp());
				json.put("equipmentNo", wm.getEquipmentNo());
				json.put("position", wm.getPosition());
				json.put("gatherId", wm.getGatherId());
				if(wm.getIsnetworking()==0){
					json.put("isnetworking", "是");
				}else{
					json.put("isnetworking", "否");
				}
				json.put("isnetworkingId", wm.getIsnetworking());
				json.put("joinTime", wm.getJoinTime());
				json.put("typeName",wm.getTypename());
				json.put("typeId", wm.getTypeId());
				json.put("statusName", wm.getStatusname());
				json.put("statusId", wm.getStatusId());
				json.put("manufacturerName", wm.getMvaluename());
				json.put("manuno", wm.getMvalueid());
				if( wm.getInsframeworkId()!=null && !"".equals(wm.getInsframeworkId())){
					json.put("insframeworkName", wm.getInsframeworkId().getName());
					json.put("iId", wm.getInsframeworkId().getId());
				}
				json.put("model",wm.getModel());
				if(wm.getGatherId()!=null && !("").equals(wm.getGatherId())){
					json.put("gatherId", wm.getGatherId().getGatherNo());
					json.put("gid", wm.getGatherId().getId());
				}else{
					json.put("gatherId", null);
					json.put("gid", null);
				}
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}

	/**
	 * 获取评价等级
	 * @return
	 */
	@RequestMapping("/getStatusAll")
	@ResponseBody
	public String getStatusAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(16);
			for(Dictionarys d:dictionary){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("ary", ary);
		return obj.toString();
	}

	@RequestMapping("/addWeldTask")
	@ResponseBody
	public String addWeldTask(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			//客户端执行操作
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
			iutil.Authority(client);
			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"addJunction\"}";
			String obj2 = "{\"JUNCTIONNO\":\""+request.getParameter("weldedJunctionno")+"\",\"DYNE\":\""+request.getParameter("fwelderid")+"\",\"TASKLEVEL\":\""+request.getParameter("tasklevel")+"\","+
					"\"INSFID\":\""+request.getParameter("fitemid")+"\",\"STARTTIME\":\""+request.getParameter("dtoTime1")+"\",\"ENDTIME\":\""+request.getParameter("dtoTime2")+"\",\"EXTERNALDIAMETER\":\""+request.getParameter("quali")+"\"}";
			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else if(!objects[0].toString().equals("false")){
				obj.put("success", true);
				obj.put("msg", objects[0].toString());
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}

	@RequestMapping("/editWeldTask")
	@ResponseBody
	public String editWeldTask(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			//客户端执行操作
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
			iutil.Authority(client);
			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"updateJunction\"}";
			String obj2 = "{\"ID\":\""+request.getParameter("id")+"\",\"JUNCTIONNO\":\""+request.getParameter("weldedJunctionno")+"\",\"DYNE\":\""+request.getParameter("fwelderid")+"\",\"TASKLEVEL\":\""+request.getParameter("tasklevel")+"\","+
					"\"INSFID\":\""+request.getParameter("fitemid")+"\",\"STARTTIME\":\""+request.getParameter("dtoTime1")+"\",\"ENDTIME\":\""+request.getParameter("dtoTime2")+"\",\"EXTERNALDIAMETER\":\""+request.getParameter("quali")+"\"}";
			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else if(!objects[0].toString().equals("false")){
				obj.put("success", true);
				obj.put("msg", objects[0].toString());
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}

	@RequestMapping("/removeWeldTask")
	@ResponseBody
	public String removeWeldTask(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			//客户端执行操作
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
			iutil.Authority(client);
			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"deleteJunction\"}";
			String obj2 = "{\"ID\":\""+request.getParameter("id")+"\",\"INSFID\":\""+request.getParameter("insfid")+"\"}";
			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else if(!objects[0].toString().equals("false")){
				obj.put("success", true);
				obj.put("msg", objects[0].toString());
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}
	
	@RequestMapping("/batchDelete")
	@ResponseBody
	public String batchDelete(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			String str = request.getParameter("str");
			if (str != null && !"".equals(str)) {
				String[] s = str.split(",");
				for (int i = 0; i < s.length; i++) {
					wjm.deleteJunction(new BigInteger(s[i]));
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}
	
	@RequestMapping("/wjNoValidate")
	@ResponseBody
	private String wjNoValidate(@RequestParam String wjno){
		boolean data = true;
		int count = wjm.getWeldedjunctionByNo(wjno);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/getWeldingJun")
	@ResponseBody
	public String getWeldingJun(HttpServletRequest request){
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String parentId = request.getParameter("parent");
		String wjno = request.getParameter("wjno");
		String welderid = request.getParameter("welderid");
		WeldDto dto = new WeldDto();
		if(!iutil.isNull(parentId)){
			//数据权限处理
			BigInteger uid = lm.getUserId(request);
			String afreshLogin = (String)request.getAttribute("afreshLogin");
			if(iutil.isNull(afreshLogin)){
				return "0";
			}
			int types = insm.getUserInsfType(uid);
			if(types==21){
				parentId = insm.getUserInsfId(uid).toString();
			}
		}
		BigInteger parent = null;
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(wjno)){
			dto.setSearch(wjno);//用来保存任务编号
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getJMByWelder(page, dto ,welderid);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction w:list){
				json.put("firsttime", wjm.getFirsttime(dto, w.getMachid(),welderid , w.getWeldedJunctionno()));
				json.put("lasttime", wjm.getLasttime(dto, w.getMachid(),welderid , w.getWeldedJunctionno()));
				json.put("fweldingtime", new DecimalFormat("0.0000").format((float)Integer.valueOf(w.getCounts().toString())/3600));
				json.put("id", w.getId());
				json.put("machid",w.getMachid());
				json.put("machine_num", w.getMachine_num());
				json.put("weldedJunctionno", w.getWeldedJunctionno().substring(2, 8));
				json.put("dyne", w.getDyne());
				json.put("maxElectricity", w.getMaxElectricity());
				json.put("minElectricity", w.getMinElectricity());
				json.put("maxValtage", w.getMaxValtage());
				json.put("minValtage", w.getMinValtage());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	@RequestMapping("/getEvaluate")
	@ResponseBody
	public String getEvaluate(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//客户端执行操作
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
			iutil.Authority(client);
			String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"giveToServer\"}";
			String obj2 = "{\"TASKNO\":\""+request.getParameter("taskNo")+"\",\"WELDERNO\":\""+request.getParameter("welderNo")+"\",\"MACHINENO\":\""+request.getParameter("machineNo")+"\",\"STATUS\":\""+request.getParameter("operateid")+"\",\"TASKID\":\""+request.getParameter("taskid")+"\",\"WELDERID\":\""+request.getParameter("welderid")+"\",\"MACHINEID\":\""+request.getParameter("machineid")+"\",\"OPERATOR\":\""+user.getId()+"\",\"ID\":\""+request.getParameter("id")+"\",\"RESULT\":\""+request.getParameter("result")+"\",\"RESULTID\":\""+request.getParameter("resultid")+"\",\"STARTTIME\":\""+request.getParameter("starttime")+"\",\"ENDTIME\":\""+request.getParameter("endtime")+"\"}";
			Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else if(!objects[0].toString().equals("false")){
				obj.put("success", true);
				obj.put("msg", objects[0].toString());
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
}
	
	@RequestMapping("/taskImport")
	@ResponseBody
	public String taskImport(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			WeldedJunction wj = new WeldedJunction();
			String taskstr = request.getParameter("taskstr");
			ary = JSONArray.fromObject(taskstr);
			for(int i=0;i<ary.size();i++){
				obj = ary.getJSONObject(i); 
				wj.setWeldedJunctionno(String.valueOf(obj.get("taskNo")));
				if(obj.get("levelid")==null||obj.get("levelid")==""){
					wj.setRoomNo(null);
				}else{
					wj.setRoomNo(String.valueOf(obj.get("levelid")));
				}
/*				if(obj.get("welderId")==null||obj.get("welderId")==""){
					wj.setUnit(null);
				}else{
					wj.setUnit(String.valueOf(obj.get("welderId")));
				}			
				if(obj.get("qualiid")==null||obj.get("qualiid")==""){
					wj.setExternalDiameter(null);
				}else{
					wj.setExternalDiameter(String.valueOf(obj.get("qualiid")));
				}*/
/*				Insframework itemid = new Insframework();
				itemid.setId(new BigInteger(String.valueOf(obj.get("insId"))));*/
				wj.setIid(new BigInteger(String.valueOf(obj.get("insId"))));
				if(obj.get("start")==null||obj.get("start")==""){
					wj.setStartTime(null);
				}else{
					wj.setStartTime(String.valueOf(obj.get("start")));
				}
				if(obj.get("start")==null||obj.get("start")==""){
					wj.setEndTime(null);
				}else{
					wj.setEndTime(String.valueOf(obj.get("end")));
				}
				wjm.addTask(wj);
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}

	//后台解析json
	@RequestMapping("/taskImportion")
	@ResponseBody
	public String taskImportion(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//客户端执行操作
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
		iutil.Authority(client);
		try{
			WeldedJunction wj = new WeldedJunction();
			String taskstr = request.getParameter("taskstr");
			ary = JSONArray.fromObject(taskstr);
			Object[] objects = null;
			for(int i=0;i<ary.size();i++){
				obj = ary.getJSONObject(i); 
				wj.setWeldedJunctionno(String.valueOf(obj.get("taskNo")));
				wj.setSerialNo(String.valueOf(obj.get("desc")));
				if(obj.get("welderNo")==null||obj.get("welderNo")==""){
					wj.setUnit(null);
				}else{
					wj.setUnit(String.valueOf(obj.get("welderNo")));
				}			
				if(obj.get("machineNo")==null||obj.get("machineNo")==""){
					wj.setExternalDiameter(null);
				}else{
					wj.setExternalDiameter(String.valueOf(obj.get("machineNo")));
				}
/*				Insframework itemid = new Insframework();
				itemid.setId(new BigInteger(String.valueOf(obj.get("id"))));*/
//				wj.setIid(new BigInteger(String.valueOf(obj.get("insId"))));
				wj.setStartTime(String.valueOf(obj.get("starttime")));
				wj.setEndTime(sdf.format(new Date()));
				System.out.println(sdf.format(new Date()));
/*				wjm.addTask(wj);*/
				String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"giveToServer\"}";
				String obj2 = "{\"TASKNO\":\""+obj.get("taskNo")+"\",\"WELDERNO\":\""+obj.get("welderNo")+"\",\"MACHINENO\":\""+obj.get("machineNo")+"\",\"STATUS\":\""+1+"\",\"TASKID\":\""+obj.get("taskid")+"\",\"WELDERID\":\""+obj.get("welderid")+"\",\"MACHINEID\":\""+obj.get("machineid")+"\",\"OPERATOR\":\""+user.getId()+"\",\"ID\":\""+obj.get("id")+"\",\"RESULT\":\"\",\"RESULTID\":\"\",\"STARTTIME\":\""+obj.get("starttime")+"\",\"ENDTIME\":\""+sdf.format(new Date())+"\"}";
				objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
				
			}
			if(objects[0].toString().equals("true")){
				obj.put("success", true);
			}else{
				obj.put("success", false);
				obj.put("errorMsg", "操作失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/getFreeWelder")
	@ResponseBody
	public String getFreeWelder(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<Person> list = ps.getFreeWelder(page,str);
		long total = 0;
		if(list != null){
			PageInfo<Person> pageinfo = new PageInfo<Person>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				json.put("id", list.get(i).getId());
				json.put("name", list.get(i).getName());
				json.put("welderno", list.get(i).getWelderno());
				json.put("insname", list.get(i).getLevename());
				json.put("qualiname", list.get(i).getQualiname());
				json.put("back", list.get(i).getBack());
				json.put("owner", list.get(i).getInsid());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getFreeJunction")
	@ResponseBody
	public String getFreeJunction(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String str = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<WeldedJunction> list = wjm.getFreeJunction(page, str);
		long total = 0;
		if(list != null){
			PageInfo<WeldedJunction> pageinfo = new PageInfo<WeldedJunction>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				json.put("id", list.get(i).getId());
				json.put("junctionno", list.get(i).getWeldedJunctionno());
				json.put("desc", list.get(i).getSerialNo());
				json.put("itemid", list.get(i).getInsfid());
				json.put("itemname", list.get(i).getUnit());
//				json.put("welderno", list.get(i).getPipelineNo());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getInsframework")
	@ResponseBody
	public String getInsframework(HttpServletRequest request,BigInteger id){
		JSONObject obj = new JSONObject();
		String serach="";
		try{
			int instype = insm.getTypeById(id);
			int bz=0;
			if(instype==20){
				
			}else if(instype==23){
				serach = "j.fitemId="+id;
			}else{
				List<Insframework> ls = insm.getInsIdByParent(id,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+id+")";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("success", serach);
		return obj.toString();
	}
	
	@RequestMapping("/getOperateArea")
	@ResponseBody
	public String getOperateArea(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONObject jsonb = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONArray aryb = new JSONArray();
		int instype = 0;
		try{
			List<Insframework> ls;
			String serach="";
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			instype = insm.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
			BigInteger userinsid = insm.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
			int bz=0;
			if(instype==20){
				ls = insm.getOperateArea(serach, 22);
				for(Insframework insf : ls ){
					json.put("id", insf.getId());
					json.put("name", insf.getName());
					ary.add(json);
				}
			}else if(instype==23){
				serach = "and i.fid="+userinsid;
				ls = insm.getOperateArea(serach, 23);
				json.put("id", insm.getParent(userinsid).getId());
				json.put("name", insm.getParent(userinsid).getName());
				ary.add(json);
				for(Insframework insf : ls ){
					jsonb.put("id", insf.getId());
					jsonb.put("name", insf.getName());
					aryb.add(jsonb);
				}
			}else if(instype==21){
				List<Insframework> lns = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : lns ){
					if(bz==0){
						serach=serach+"and (i.fid="+inns.getId();
					}else{
						serach=serach+" or i.fid="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or i.fid="+userinsid+")";
				ls = insm.getOperateArea(serach, 22);
				for(Insframework insf : ls ){
					json.put("id", insf.getId());
					json.put("name", insf.getName());
					ary.add(json);
				}
			}else{
				List<Insframework> lns = insm.getInsIdByParent(userinsid,24);
				for(Insframework inns : lns ){
					if(bz==0){
						serach=serach+"and (i.fid="+inns.getId();
					}else{
						serach=serach+" or i.fid="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or i.fid="+userinsid+")";
				ls = insm.getOperateArea(serach, 23);
				json.put("id", userinsid);
				json.put("name", insm.getInsframeworkById(userinsid));
				ary.add(json);
				for(Insframework insf : ls ){
					jsonb.put("id", insf.getId());
					jsonb.put("name", insf.getName());
					aryb.add(jsonb);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		obj.put("banzu", aryb);
		obj.put("type", instype);
		return obj.toString();
	}
	
	@RequestMapping("/getTeam")
	@ResponseBody
	public String getTeam(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			String serach="";
			serach = request.getParameter("searchStr");
			List<Insframework> ls = insm.getOperateArea(serach, 23);
			for(Insframework insf : ls ){
				json.put("id", insf.getId());
				json.put("name", insf.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
}
