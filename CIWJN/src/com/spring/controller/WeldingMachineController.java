package com.spring.controller;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.model.Dictionarys;
import com.spring.model.Gather;
import com.spring.model.Insframework;
import com.spring.model.MyUser;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.page.Page;
import com.spring.service.DictionaryService;
import com.spring.service.GatherService;
import com.spring.service.InsframeworkService;
import com.spring.service.MaintainService;
import com.spring.service.WeldingMachineService;
import com.spring.util.IsnullUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/weldingMachine", produces = { "text/json;charset=UTF-8" })
public class WeldingMachineController {
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	
	@Autowired
	private WeldingMachineService wmm;
	
	@Autowired
	private MaintainService maintain;
	
	@Autowired
	private InsframeworkService im;
	
	@Autowired
	private GatherService gm;
	
	@Autowired
	private DictionaryService dm;
	
	
	IsnullUtil iutil = new IsnullUtil();
	
	/**
	 * 焊机设备管理
	 * @return
	 */
	@RequestMapping("/goWeldingMachine")
	public String goWeldingMahine(){
		return "weldingMachine/weldingmachine";
	}
	/**
	 * CAT厂商焊机管理
	 * @return
	 */
	@RequestMapping("/gomathine")
	public String gomathine(){
		return "Mathine/Mathine";
	}
	/**
	 * 维修记录
	 * @param request
	 * @param wid
	 * @return
	 */
	@RequestMapping("/goMaintain")
	public String goMaintain(HttpServletRequest request, @RequestParam String wid){
		WeldingMachine weld = wmm.getWeldingMachineById(new BigInteger(wid));
		if(weld.getIsnetworking()==0){
			request.setAttribute("isnetworking", "是");
		}else{
			request.setAttribute("isnetworking", "否");
		}
		request.setAttribute("w", weld);
		return "maintain/weldingmaintenance";
	}
	
	/**
	 * 新增焊机设备
	 * @return
	 */
	@RequestMapping("/goAddWeldingMachine")
	public String goAddWeldingMachine(){
		return "weldingMachine/addweldingmachine";
	}
	
	/**
	 * 修改焊机设备
	 * @param request
	 * @param wid
	 * @return
	 */
	@RequestMapping("/goEditWeldingMachine")
	public String goEditWeldingMachine(HttpServletRequest request, @RequestParam String wid){
		WeldingMachine weld = wmm.getWeldingMachineById(new BigInteger(wid));
		request.setAttribute("w", weld);
		return "weldingMachine/editweldingmachine";
	}
	
	/**
	 * 删除焊机信息
	 * @param request
	 * @param wid
	 * @return
	 */
	@RequestMapping("/goremoveWeldingMachine")
	public String goremoveWeldingMahine(HttpServletRequest request, @RequestParam String wid){
		WeldingMachine weld = wmm.getWeldingMachineById(new BigInteger(wid));
		if(weld.getIsnetworking()==0){
			request.setAttribute("isnetworking", "是");
		}else{
			request.setAttribute("isnetworking", "否");
		}
		request.setAttribute("w", weld);
		return "weldingMachine/removeweldingmachine";
	}
	
	/**
	 * 焊机规范管理
	 */
	@RequestMapping("/goSpecify")
	public String goSpecify(){
		return "specification/specification";
	}
	
	/**
	 * 显示焊机列表
	 * @return
	 */
	@RequestMapping("/getWedlingMachineList")
	@ResponseBody
	public String getWeldingMachine(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String searchStr = request.getParameter("searchStr");
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}else {
			parent = im.getUserInsframework();
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
				if(wm.getModel()!=null && !("").equals(wm.getModelname())){
					json.put("model",wm.getModel());
					json.put("modelname",wm.getModelname());
				}else{
					json.put("model",null);
					json.put("modelname",null);
				}
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
	 * 显示焊机列表
	 * @return
	 */
	@RequestMapping("/getWedlingMachineListNoPage")
	@ResponseBody
	public String getWedlingMachineListNoPage(HttpServletRequest request){
		String searchStr = request.getParameter("searchStr");
		String str = request.getParameter("str");
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		parent = im.getUserInsframework();
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		if(str!=null && !"".equals(str)) {
			searchStr+=str;
		}
		request.getSession().setAttribute("searchStr", searchStr);
		List<WeldingMachine> list = wmm.getWeldingMachineAllNoPage(parent,searchStr);
		
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
				if(wm.getModel()!=null && !("").equals(wm.getModelname())){
					json.put("model",wm.getModel());
					json.put("modelname",wm.getModelname());
				}else{
					json.put("model",null);
					json.put("modelname",null);
				}
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
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 获取焊机及其对应的采集模块
	 * @return
	 */
	@RequestMapping("/getMachineGather")
	@ResponseBody
	public String getMachineGather(HttpServletRequest request){
		List<WeldingMachine> list = wmm.getMachineGather();
		
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldingMachine wm:list){
				json.put("id", wm.getId());
				json.put("gatherId", wm.getGatherId());
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
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 获取设备类型
	 * @return
	 */
	@RequestMapping("/getTypeAll")
	@ResponseBody
	public String getTypeAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(4);
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
	
	/**
	 * 获取项目类型
	 * @return
	 */
	@RequestMapping("/getInsframeworkAll")
	@ResponseBody
	public String getInsframeworkAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			String str = " and (i.fid='"+im.getUserInsframework()+"' or ins.fid='"+im.getUserInsframework()+"' or insf.fid='"+im.getUserInsframework()+"' or insf.fparent='"+im.getUserInsframework()+"')";
			List<Insframework> list = im.getOperateArea(str,23);
			for(Insframework i:list){
				json.put("id", i.getId());
				json.put("name", i.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取用户所属层级及下面的组织机构
	 * @return
	 */
	@RequestMapping("/getUserInsAll")
	@ResponseBody
	public String getUserInsAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			String serach="";
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int instype = im.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
			BigInteger userinsid = im.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
			int bz=0;
			if(instype==20){
				
			}else if(instype==23){
				serach = "fid="+userinsid;
			}else{
				List<Insframework> ls = im.getInsIdByParent(userinsid,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(fid="+inns.getId();
					}else{
						serach=serach+" or fid="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or fid="+userinsid+")";
			}
			List<Insframework> list = im.getUserInsAll(serach);
			for(Insframework i:list){
				json.put("id", i.getId());
				json.put("name", i.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取采集编号
	 * @return
	 */
	@RequestMapping("/getGatherAll")
	@ResponseBody
	public String getGatherAll(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		String itemid = request.getParameter("itemid");
		BigInteger item = null;
		if(iutil.isNull(itemid)){
			item = new BigInteger(itemid);
		}
		try{
			List<Gather> list = gm.getGatherByInsfid(item);
			for(Gather g:list){
				json.put("id", g.getId());
				json.put("name", g.getGatherNo());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取焊机状态
	 * @return
	 */
	@RequestMapping("/getStatusAll")
	@ResponseBody
	public String getStatusAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(3);
			for(Dictionarys d:dictionary){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取厂商
	 * @return
	 */
	@RequestMapping("/getManuAll")
	@ResponseBody
	public String getManuAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(14);
			for(Dictionarys d:dictionary){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	@RequestMapping("/findAllweldmachine")
	@ResponseBody
	public String getAllAuthority(HttpServletRequest request){
		
		List<WeldingMachine> list = wmm.findAllweldmachine();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldingMachine w:list){
				json.put("id", w.getId());
				json.put("weldmachinetype", w.getTypename());
				json.put("machinevalue", w.getMvaluename());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	/**
	 * 获取设备厂商下的焊机型号
	 * @return
	 */
	@RequestMapping("/getModelAll")
	@ResponseBody
	public String getModelAll(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			String str = request.getParameter("str");
			if(str!=null&&!("").equals(str)){
				int num = Integer.valueOf(request.getParameter("str"));
				List<Dictionarys> dictionary = dm.getModelOfManu(num);
				for(Dictionarys d:dictionary){
					json.put("id", d.getId());
					json.put("name", d.getValueName());
					ary.add(json);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 修改厂商焊机绑定关系
	 * @return
	 */
	@RequestMapping("/getfactoryType")
	@ResponseBody
	public String getfactoryType(HttpServletRequest request){
		WeldingMachine wm = new WeldingMachine();
		JSONObject obj = new JSONObject();
		try{
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			wm.setUpdater(new BigInteger(user.getId()+""));
			wm.setStatusId(Integer.parseInt(request.getParameter("back")));
			wm.setFmachingname("1");
			wmm.deletefactory(new BigInteger(wm.getStatusId()+""));
			String[] str = request.getParameter("str").split(",");
			for(int i=0;i<str.length;i++){
				wm.setTypeId(Integer.parseInt(str[i]));
				wmm.addfactoryType(wm);
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping("/addWeldingMachine")
	@ResponseBody
	public String addWeldingMachine(HttpServletRequest request){
		WeldingMachine wm = new WeldingMachine();
		JSONObject obj = new JSONObject();
		try{
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			wm.setCreater(new BigInteger(user.getId()+""));
			wm.setIp(request.getParameter("ip"));
			wm.setModel(request.getParameter("model"));
			System.out.println(request.getParameter("equipmentNo"));
			wm.setEquipmentNo(request.getParameter("equipmentNo"));
			if(iutil.isNull(request.getParameter("joinTime"))){
				wm.setJoinTime(request.getParameter("joinTime"));
			}
			if(iutil.isNull(request.getParameter("position"))){
				wm.setPosition(request.getParameter("position"));
			}
			if(iutil.isNull(request.getParameter("gatherId"))){
				Gather g = new Gather();
				g.setId(new BigInteger(request.getParameter("gatherId")));
				wm.setGatherId(g);
			}
			wm.setIsnetworking(Integer.parseInt(request.getParameter("isnetworkingId")));
			wm.setTypeId(Integer.parseInt(request.getParameter("tId")));
			Insframework ins = new Insframework();
			ins.setId(new BigInteger(request.getParameter("iId")));
			wm.setInsframeworkId(ins);
			wm.setStatusId(Integer.parseInt(request.getParameter("sId")));
			wm.setMvalueid(Integer.parseInt(request.getParameter("manuno")));
			wmm.addWeldingMachine(wm);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping("/editWeldingMachine")
	@ResponseBody
	public String editWeldingMachine(HttpServletRequest request){
		WeldingMachine wm = new WeldingMachine();
		JSONObject obj = new JSONObject();
		try{
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			wm.setUpdater(new BigInteger(user.getId()+""));
			wm.setId(new BigInteger(request.getParameter("wid")));
			wm.setEquipmentNo(request.getParameter("equipmentNo"));
			if(iutil.isNull(request.getParameter("joinTime"))){
				wm.setJoinTime(request.getParameter("joinTime"));
			}
			if(iutil.isNull(request.getParameter("position"))){
				wm.setPosition(request.getParameter("position"));
			}
			if(iutil.isNull(request.getParameter("gatherId"))){
				Gather g = new Gather();
				g.setId(new BigInteger(request.getParameter("gatherId")));
				wm.setGatherId(g);
			}
			wm.setIsnetworking(Integer.parseInt(request.getParameter("isnetworkingId")));
			wm.setTypeId(Integer.parseInt(request.getParameter("tId")));
			Insframework ins = new Insframework();
			ins.setId(new BigInteger(request.getParameter("iId")));
			wm.setInsframeworkId(ins);
			wm.setStatusId(Integer.parseInt(request.getParameter("sId")));
			wm.setIp(request.getParameter("ip"));
			wm.setModel(request.getParameter("model"));
			//修改焊机状态为启用时，结束所有维修任务
			int sid = wm.getStatusId();
			if(sid == 31){
				List<WeldingMaintenance> list =  maintain.getEndtime(wm.getId());
				for(WeldingMaintenance w : list){
					if(w.getMaintenance().getEndTime()==null || w.getMaintenance().getEndTime()==""){
						maintain.updateEndtime(w.getId());
					}
				}
			}
			wm.setMvalueid(Integer.parseInt(request.getParameter("manuno")));
			wmm.editWeldingMachine(wm);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	/**
	 * 删除焊机设备
	 * @param wid
	 * @return
	 */
	@RequestMapping("/removeWeldingMachine")
	@ResponseBody
	private String removeWeldingMachine(@RequestParam String wid){
		JSONObject obj = new JSONObject();
		try{
			wmm.deleteWeldingChine(new BigInteger(wid));
			wmm.deleteHistory(new BigInteger(wid));
			List<WeldingMaintenance> list = maintain.getMaintainByWeldingMachinId(new BigInteger(wid));
			for(WeldingMaintenance wm : list){
				//删除维修记录
				maintain.deleteWeldingMaintenance(wm.getId());
				maintain.deleteMaintenanceRecord(wm.getMaintenance().getId());
			}
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", true);
			obj.put("msg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/enovalidate")
	@ResponseBody
	private String enovalidate(@RequestParam String eno){
		boolean data = true;
		int count = wmm.getEquipmentnoCount(eno);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/gidvalidate")
	@ResponseBody
	private String gidvalidate(HttpServletRequest request){
		String gather = request.getParameter("gather");
		String itemid = request.getParameter("itemid");
		BigInteger item = null;
		if(iutil.isNull(itemid)){
			item = new BigInteger(itemid);
		}
		boolean data = true;
		int count = wmm.getGatheridCount(item,gather);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	/**
	 * 获取厂商及其对应的焊机类型
	 * @return
	 */
	@RequestMapping("/getManuModel")
	@ResponseBody
	public String getManuModel(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getManuModel();
			for(Dictionarys d:dictionary){
				json.put("manu", d.getTypeid());
				json.put("model", d.getValue());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
}
