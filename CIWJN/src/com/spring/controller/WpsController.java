package com.spring.controller;

import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.Dictionarys;
import com.spring.model.MyUser;
import com.spring.model.Td;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.DictionaryService;
import com.spring.service.TdService;
import com.spring.service.WpsService;
import com.spring.util.IsnullUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/wps",produces = { "text/json;charset=UTF-8" })
public class WpsController {
	
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	private String wpsfid;
	private String wpspre;
	@Autowired
	private WpsService wpsService;
	@Autowired
	private TdService tdService;
	@Autowired
	private DictionaryService dm;
	
    public static final String IP_ADDR = "121.196.222.216";//服务器地址   
    public static final int PORT = 5555;//服务器端口号  
	
	IsnullUtil iutil = new IsnullUtil();
	private SocketChannel socketChannel;
	private String strdata;
	
	/**
	 * 获取所有用户列表
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/AllWps")
	public String AllUser(HttpServletRequest request){
		return "weldwps/allWps";
	}
	
	@RequestMapping("/goWpslib")
	public String goWpslib(HttpServletRequest request){
		return "wpslib/allWpslib";
	}
	
	@RequestMapping("/AllSpe")
	public String AllSpe(HttpServletRequest request){
		return "specification/allSpe";
	}

	@RequestMapping("/getAllWps")
	@ResponseBody
	public String getAllWps(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		page = new Page(pageIndex,pageSize,total);
		List<Wps> findAll = wpsService.findAll(page,parent,search);
		long total = 0;
		
		if(findAll != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(findAll);
			total = pageinfo.getTotal();
		}

		request.setAttribute("wpsList", findAll);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:findAll){
				String creat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFcreatedate());
				String update = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFupdatedate());
				json.put("FID", wps.getFid());
				json.put("FWPSNum", wps.getFwpsnum());
				json.put("Fweld_I", wps.getFweld_i());
				json.put("Fweld_V", wps.getFweld_v());
				json.put("Fweld_I_MAX",wps.getFweld_i_max());
				json.put("Fweld_I_MIN", wps.getFweld_i_min());
				json.put("Fweld_V_MAX", wps.getFweld_v_max());
				json.put("Fweld_V_MIN", wps.getFweld_v_min());
				json.put("Fweld_Alter_I", wps.getFweld_alter_i());
				json.put("Fweld_Alter_V", wps.getFweld_alter_v());
				json.put("Fweld_PreChannel", wps.getFweld_prechannel());
				json.put("FCReateDate",creat);
				json.put("FUpdateDate", update);
				json.put("Fowner",wps.getInsname());
				json.put("insid",wps.getInsid());
				json.put("Fback", wps.getFback());
				json.put("Fname", wps.getFname());
				json.put("Fdiameter", wps.getFdiameter());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getAllSpe")
	@ResponseBody
	public String getAllSpe(HttpServletRequest request){
		BigInteger machine = new BigInteger(request.getParameter("machine"));
		BigInteger chanel = new BigInteger(request.getParameter("chanel"));
		List<Wps> findAll = wpsService.findAllSpe(machine,chanel);

		request.setAttribute("wpsList", findAll);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			/*if(findAll.size()==0){
				json.put("FWPSNum", 1);//通道号
				json.put("Fweld_I", 62);//初期条件
				json.put("Fweld_V", 62);//溶孔深度
				json.put("Fweld_I_MAX",102);//一元个别
				json.put("Fweld_I_MIN", 111);//收弧模式
				json.put("Fweld_V_MAX", 0);//电弧特性
				json.put("Fweld_V_MIN", 0);//模式
				json.put("Fweld_Alter_I", 91);//材料
				json.put("Fweld_Alter_V", 121);//气体
				json.put("Fweld_PreChannel", 132);//半径
				json.put("ftime", 30.0);
				json.put("fadvance", 1.0);
				json.put("fhysteresis", 1.0);
				json.put("fini_ele", 100.0);//初期电流
				json.put("fweld_ele", 100.0);//焊接电流
				json.put("farc_ele", 100.0);//收弧电流
				json.put("fweld_tuny_ele", 0.0);//焊接微调电流
				json.put("fweld_tuny_vol", 0.0);//焊接微调电压
				json.put("farc_tuny_ele", 0.0);//收弧微调电流
				if(Integer.valueOf(cla.toString())==102){
					json.put("fini_vol", 19.0);//初期电压
					json.put("fweld_vol", 19.0);//焊接电压
					json.put("farc_vol", 19.0);//收弧电压
					json.put("fweld_tuny_vol", 0.0);//焊接微调电压
					json.put("Fdiameter", 0.0);//收弧微调电压
				}else{
					json.put("fini_vol", 0.0);//初期电压
					json.put("fweld_vol", 0.0);//焊接电压
					json.put("farc_vol", 0.0);//收弧电压
					json.put("fweld_tuny_vol", 0.0);//焊接微调电压
					json.put("Fdiameter", 0.0);//收弧微调电压
				}
				ary.add(json);
			}else{*/
			for(Wps wps:findAll){
				json.put("FID", wps.getFid());
				json.put("FWPSNum", wps.getWelderid());
				json.put("Fweld_I", wps.getFinitial());
				json.put("Fweld_V", wps.getFcontroller());
				json.put("Fweld_I_MAX",wps.getInsname());
				json.put("Fweld_I_MIN", wps.getWeldername());
				json.put("Fweld_V_MAX", wps.getFweld_v_max());
				json.put("Fweld_V_MIN", wps.getFmode());
				json.put("Fweld_Alter_I", wps.getUpdatename());
				json.put("Fweld_Alter_V", wps.getFback());
				json.put("Fweld_PreChannel", wps.getFname());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("Fdiameter", wps.getFdiameter());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/Spe")
	@ResponseBody
	public String Spe(HttpServletRequest request){
		BigInteger machine = new BigInteger(request.getParameter("machine"));
		String ch = request.getParameter("chanel");
		List<Wps> findAll = wpsService.AllSpe(machine,ch);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:findAll){
				json.put("FID", wps.getFid());
				json.put("FWPSNum", wps.getWelderid());
				json.put("Fweld_I", wps.getFinitial());
				json.put("Fweld_V", wps.getFcontroller());
				json.put("Fweld_I_MAX",wps.getInsname());
				json.put("Fweld_I_MIN", wps.getWeldername());
				json.put("Fweld_V_MAX", wps.getFweld_v_max());
				json.put("Fweld_V_MIN", wps.getFmode());
				json.put("Fweld_Alter_I", wps.getUpdatename());
				json.put("Fweld_Alter_V", wps.getFback());
				json.put("Fweld_PreChannel", wps.getFname());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("Fdiameter", wps.getFdiameter());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/toAddWps")
	public String toAddUser(HttpServletRequest request){
		return "weldwps/addWps";
	}
	
	@RequestMapping("/toAddSpe")
	public String toAddSpe(HttpServletRequest request){
		return "specification/addSpe";
	}
	
	
	@RequestMapping("/toUpdateWps")
	public String toUpdateWps(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("wps", wps);
		request.setAttribute("update", sdf.format(wps.getFupdatedate()));
		request.setAttribute("create", sdf.format(wps.getFcreatedate()));
		return "weldwps/editWps";
	}
	
	@RequestMapping("/toUpdateSpe")
	public String toUpdateSpe(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findSpeById(fid);
		request.setAttribute("wps", wps);
		return "specification/editSpe";
	}
	
	@RequestMapping("/toDestroyWps")
	public String toDestroyWps(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("wps", wps);
		request.setAttribute("update", sdf.format(wps.getFupdatedate()));
		request.setAttribute("create", sdf.format(wps.getFcreatedate()));
		return "weldwps/destroyWps";
	}
	
	@RequestMapping("/toDestroySpe")
	public String toDestroySpe(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findSpeById(fid);
		request.setAttribute("wps", wps);
		return "specification/destroySpe";
	}
	
	@RequestMapping("/addMainWps")
	@ResponseBody
	public String addMainWps(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		JSONObject obj = new JSONObject();
		long fid = new Long(request.getParameter("fid"));
		Integer finitial = Integer.valueOf(request.getParameter("finitial"));
		Integer fcontroller = Integer.valueOf(request.getParameter("fcontroller"));
		Integer fmode = Integer.valueOf(request.getParameter("fmode"));
		Integer fselect = Integer.valueOf(request.getParameter("fselect"));
		Integer farc = Integer.valueOf(request.getParameter("farc"));
		Integer fmaterial = Integer.valueOf(request.getParameter("fmaterial"));
		Integer fgas = Integer.valueOf(request.getParameter("fgas"));
		BigInteger fdiameter = new BigInteger(request.getParameter("fdiameter"));
		Integer chanel = Integer.valueOf(request.getParameter("chanel"));
		double ftime = Double.valueOf(request.getParameter("ftime"));
		double fadvance = Double.valueOf(request.getParameter("fadvance"));
		double fini_ele = Double.valueOf(request.getParameter("fini_ele"));
		double fweld_ele = Double.valueOf(request.getParameter("fweld_ele"));
		double farc_ele = Double.valueOf(request.getParameter("farc_ele"));
		double fhysteresis = Double.valueOf(request.getParameter("fhysteresis"));
		int fcharacter = Integer.valueOf(request.getParameter("fcharacter"));
		double fweld_tuny_ele = Double.valueOf(request.getParameter("fweld_tuny_ele"));
		double farc_tuny_ele = Double.valueOf(request.getParameter("farc_tuny_ele"));
		double fini_vol = Double.valueOf(request.getParameter("fini_vol"));
		double fweld_vol = Double.valueOf(request.getParameter("fweld_vol"));
		double farc_vol = Double.valueOf(request.getParameter("farc_vol"));
		double fini_vol1 = Double.valueOf(request.getParameter("fini_vol1"));
		double fweld_vol1 = Double.valueOf(request.getParameter("fweld_vol1"));
		double farc_vol1 = Double.valueOf(request.getParameter("farc_vol1"));
		double fweld_tuny_vol = Double.valueOf(request.getParameter("fweld_tuny_vol"));
		double farc_tuny_vol = Double.valueOf(request.getParameter("farc_tuny_vol"));
		double fwarn_ele_up = Double.valueOf(request.getParameter("fwarn_ele_up"));
		double fwarn_ele_down = Double.valueOf(request.getParameter("fwarn_ele_down"));
		double fwarn_vol_up = Double.valueOf(request.getParameter("fwarn_vol_up"));
		double fwarn_vol_down = Double.valueOf(request.getParameter("fwarn_vol_down"));
		int fprocess = Integer.valueOf(request.getParameter("fprocess"));
		int ftorch = Integer.valueOf(request.getParameter("ftorch"));
		try{
			wps.setFweld_i_max(chanel);
			wps.setFweld_i_min(finitial);
			wps.setFweld_alter_i(fcontroller);
			wps.setFweld_v_min(fmode);
			wps.setFweld_i(fselect);
			wps.setFweld_v(farc);
			wps.setFweld_v_max(fcharacter);
			wps.setFweld_prechannel(fmaterial);
			wps.setFweld_alter_v(fgas);
			wps.setInsid(fdiameter);
			wps.setFtime(ftime);
			wps.setFadvance(fadvance);
			wps.setFhysteresis(fhysteresis);
			wps.setFini_ele(fini_ele);
			wps.setFini_vol(fini_vol);
			wps.setFini_vol1(fini_vol1);
			wps.setFweld_ele(fweld_ele);
			wps.setFweld_vol(fweld_vol);
			wps.setFweld_vol1(fweld_vol1);
			wps.setFarc_ele(farc_ele);
			wps.setFarc_vol(farc_vol);
			wps.setFarc_vol1(farc_vol1);
			wps.setFweld_tuny_ele(fweld_tuny_ele);
			wps.setFweld_tuny_vol(fweld_tuny_vol);
			wps.setFarc_tuny_ele(farc_tuny_ele);
			wps.setFdiameter(farc_tuny_vol);
			wps.setFcreater(myuser.getId());
			wps.setFupdater(myuser.getId());
			wps.setFid(fid);
			wps.setFprocessid(fprocess);
			wps.setFtorch(ftorch);
			wps.setFwarn_ele_up(fwarn_ele_up);
			wps.setFwarn_ele_down(fwarn_ele_down);
			wps.setFwarn_vol_up(fwarn_vol_up);
			wps.setFwarn_vol_down(fwarn_vol_down);
/*			if(wpsService.findCount(machine,chanel.toString())<=0){
				wpsService.saveSpe(wps);
			}else{
				wpsService.updateSpe(wps);
			}*/
			wpsService.saveSpe(wps);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateMainWps")
	@ResponseBody
	public String updateMainWps(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		JSONObject obj = new JSONObject();
		long fid = new Long(request.getParameter("fid"));
		Integer finitial = Integer.valueOf(request.getParameter("finitial"));
		Integer fcontroller = Integer.valueOf(request.getParameter("fcontroller"));
		Integer fmode = Integer.valueOf(request.getParameter("fmode"));
		Integer fselect = Integer.valueOf(request.getParameter("fselect"));
		Integer farc = Integer.valueOf(request.getParameter("farc"));
		Integer fmaterial = Integer.valueOf(request.getParameter("fmaterial"));
		Integer fgas = Integer.valueOf(request.getParameter("fgas"));
		BigInteger fdiameter = new BigInteger(request.getParameter("fdiameter"));
		Integer chanel = Integer.valueOf(request.getParameter("chanel"));
		double ftime = Double.valueOf(request.getParameter("ftime"));
		double fadvance = Double.valueOf(request.getParameter("fadvance"));
		double fini_ele = Double.valueOf(request.getParameter("fini_ele"));
		double fweld_ele = Double.valueOf(request.getParameter("fweld_ele"));
		double farc_ele = Double.valueOf(request.getParameter("farc_ele"));
		double fhysteresis = Double.valueOf(request.getParameter("fhysteresis"));
		int fcharacter = Integer.valueOf(request.getParameter("fcharacter"));
		double fweld_tuny_ele = Double.valueOf(request.getParameter("fweld_tuny_ele"));
		double farc_tuny_ele = Double.valueOf(request.getParameter("farc_tuny_ele"));
		double fini_vol = Double.valueOf(request.getParameter("fini_vol"));
		double fweld_vol = Double.valueOf(request.getParameter("fweld_vol"));
		double farc_vol = Double.valueOf(request.getParameter("farc_vol"));
		double fini_vol1 = Double.valueOf(request.getParameter("fini_vol1"));
		double fweld_vol1 = Double.valueOf(request.getParameter("fweld_vol1"));
		double farc_vol1 = Double.valueOf(request.getParameter("farc_vol1"));
		double fweld_tuny_vol = Double.valueOf(request.getParameter("fweld_tuny_vol"));
		double farc_tuny_vol = Double.valueOf(request.getParameter("farc_tuny_vol"));
		double fwarn_ele_up = Double.valueOf(request.getParameter("fwarn_ele_up"));
		double fwarn_ele_down = Double.valueOf(request.getParameter("fwarn_ele_down"));
		double fwarn_vol_up = Double.valueOf(request.getParameter("fwarn_vol_up"));
		double fwarn_vol_down = Double.valueOf(request.getParameter("fwarn_vol_down"));
		int fprocess = Integer.valueOf(request.getParameter("fprocess"));
		int ftorch = Integer.valueOf(request.getParameter("ftorch"));
		try{
			wps.setFweld_i_max(chanel);
			wps.setFweld_i_min(finitial);
			wps.setFweld_alter_i(fcontroller);
			wps.setFweld_v_min(fmode);
			wps.setFweld_i(fselect);
			wps.setFweld_v(farc);
			wps.setFweld_v_max(fcharacter);
			wps.setFweld_prechannel(fmaterial);
			wps.setFweld_alter_v(fgas);
			wps.setInsid(fdiameter);
			wps.setFtime(ftime);
			wps.setFadvance(fadvance);
			wps.setFhysteresis(fhysteresis);
			wps.setFini_ele(fini_ele);
			wps.setFini_vol(fini_vol);
			wps.setFini_vol1(fini_vol1);
			wps.setFweld_ele(fweld_ele);
			wps.setFweld_vol(fweld_vol);
			wps.setFweld_vol1(fweld_vol1);
			wps.setFarc_ele(farc_ele);
			wps.setFarc_vol(farc_vol);
			wps.setFarc_vol1(farc_vol1);
			wps.setFweld_tuny_ele(fweld_tuny_ele);
			wps.setFweld_tuny_vol(fweld_tuny_vol);
			wps.setFarc_tuny_ele(farc_tuny_ele);
			wps.setFdiameter(farc_tuny_vol);
			wps.setFcreater(myuser.getId());
			wps.setFupdater(myuser.getId());
			wps.setFid(fid);
			wps.setFprocessid(fprocess);
			wps.setFtorch(ftorch);
			wps.setFwarn_ele_up(fwarn_ele_up);
			wps.setFwarn_ele_down(fwarn_ele_down);
			wps.setFwarn_vol_up(fwarn_vol_up);
			wps.setFwarn_vol_down(fwarn_vol_down);
			
/*			if(wpsService.findCount(machine,chanel.toString())<=0){
				wpsService.saveSpe(wps);
			}else{
				wpsService.updateSpe(wps);
			}*/
			wpsService.updateSpe(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/removeMainWps")
	@ResponseBody
	public String removeMainWps(HttpServletRequest request){
			BigInteger fid = new BigInteger(request.getParameter("fid"));
			JSONObject obj = new JSONObject();
			try{
				wpsService.deleteMainWps(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/saveCopy")
	@ResponseBody
	public String saveCopy(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		String ch = request.getParameter("chanel");
		String str = request.getParameter("str");
		BigInteger mac = new BigInteger(request.getParameter("mac"));
		List<Wps> findAll = wpsService.findSpe(mac,ch);
		try{
	        if(null!=str&&""!=str){
	        String[] ss = str.split(",");
	        for (int i = 0; i < ss.length; i++) {
			for(Wps spe:findAll){
			if(wpsService.findCount(new BigInteger(ss[i]),String.valueOf(spe.getFweld_i_max()))<=0){
				spe.setMacid(new BigInteger(ss[i]));
				spe.setFcreater(myuser.getId());
				spe.setFupdater(myuser.getId());
				wpsService.saveSpe(spe);
			}else{
				spe.setMacid(new BigInteger(ss[i]));
				spe.setFupdater(myuser.getId());
				wpsService.updateSpe(spe);
			}
			}
	        }
	        }
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/findCount")
	@ResponseBody
	public String findCount(HttpServletRequest request){
		Wps wps = new Wps();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		String ch = request.getParameter("chanel");
		String str = request.getParameter("str");
		BigInteger mac = new BigInteger(request.getParameter("mac"));
		try{
			int co;
			if(null!=ch&&""!=ch){
				co=1;
			}else{
				co = wpsService.findCount(mac,ch);
			}
			BigInteger parent = null;
			List<Td> getAP = tdService.getAllPosition(parent,null);
			for(Td td:getAP){
	        if(null!=str&&""!=str){
	        String[] ss = str.split(",");
	        for (int i = 0; i < ss.length; i++) {
	        	if(td.getId()==Long.valueOf(ss[i])){
				json.put("machineid", td.getFequipment_no());
				json.put("insname", td.getFcn());
				json.put("num", "1-"+co);
				json.put("readynum", 0);
				ary.add(json);
	        	}
	        }
	        }
		}
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		obj.put("rows", ary);
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/addWps")
	@ResponseBody
	public String addUser(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		try{
			wps.setFwpsnum(request.getParameter("fwn"));
			wps.setFweld_i(Integer.valueOf(request.getParameter("Fweld_I")));
			wps.setFweld_v(Integer.valueOf(request.getParameter("Fweld_V")));
			wps.setFweld_i_max(Integer.valueOf(request.getParameter("Fweld_I_MAX")));
			wps.setFweld_i_min(Integer.valueOf(request.getParameter("Fweld_I_MIN")));
			wps.setFweld_v_max(Integer.valueOf(request.getParameter("Fweld_V_MAX")));
			wps.setFweld_v_min(Integer.valueOf(request.getParameter("Fweld_V_MIN")));
			wps.setFweld_alter_i(Integer.valueOf(request.getParameter("Fweld_Alter_I")));
			wps.setFweld_alter_v(Integer.valueOf(request.getParameter("Fweld_Alter_V")));
			wps.setFweld_prechannel(Integer.valueOf(request.getParameter("Fweld_PreChannel")));
			wps.setFname(request.getParameter("Fname"));
			wps.setFback(request.getParameter("Fback"));
			wps.setFdiameter(Double.valueOf(request.getParameter("Fdiameter")));
			wps.setFcreater(myuser.getId());
			wps.setFupdater(myuser.getId());
			wps.setFowner(Long.parseLong(request.getParameter("ins")));
			wps.setFcreatedate(sdf.parse(sdf.format((new Date()).getTime())));
			wps.setFupdatedate(sdf.parse(sdf.format((new Date()).getTime())));
			wpsService.save(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/updateWps")
	@ResponseBody
	public String updateWps(Wps wps,HttpServletRequest request){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		try{
			wps.setFid(Long.parseLong(request.getParameter("FID")));
			wps.setFupdater(myuser.getId());
	        wps.setFowner(Long.parseLong(request.getParameter("ins")));
//	        wps.setFcreatedate(sdf.parse(request.getParameter("FCReateDate")));
	        wps.setFwpsnum(request.getParameter("FWPSNum"));
	        wps.setFweld_i( Integer.parseInt(request.getParameter("Fweld_I")));
	        wps.setFweld_v( Integer.parseInt(request.getParameter("Fweld_V")));
	        wps.setFweld_i_max(Integer.parseInt(request.getParameter("Fweld_I_MAX")));
	        wps.setFweld_i_min(Integer.parseInt(request.getParameter("Fweld_I_MIN")));
	        wps.setFweld_v_max(Integer.parseInt(request.getParameter("Fweld_V_MAX")));
	        wps.setFweld_v_min(Integer.parseInt(request.getParameter("Fweld_V_MIN")));
	        wps.setFweld_alter_i(Integer.parseInt(request.getParameter("Fweld_Alter_I")));
	        wps.setFweld_alter_v(Integer.parseInt(request.getParameter("Fweld_Alter_V")));
	        wps.setFweld_prechannel(Integer.parseInt(request.getParameter("Fweld_PreChannel")));
	        wps.setFupdatedate(sdf.parse(sdf.format((new Date()).getTime())));
	        wps.setFback(request.getParameter("Fback"));
	        wps.setFname(request.getParameter("Fname"));
	        wps.setFdiameter(Double.valueOf(request.getParameter("Fdiameter")));
		    wpsService.update(wps);
			obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();

	}
	
	@RequestMapping("/destroyWps")
	@ResponseBody
	public String destroyWps(@RequestParam BigInteger fid){

			JSONObject obj = new JSONObject();
			try{
				wpsService.delete(fid);
				wpsService.deleteHistory(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/wpsvalidate")
	@ResponseBody
	private String wpsvalidate(@RequestParam String fwpsnum){
		boolean data = true;
		int count = wpsService.getUsernameCount(fwpsnum);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/selectwps")
	public String selectwps(HttpServletRequest request){
		return "weldwps/selectWps";
	}
	
	@RequestMapping("/selectmachine")
	public String selectmachine(HttpServletRequest request){
		 wpsfid = request.getParameter("fid");
		 wpspre = request.getParameter("pre");
		return "weldwps/selectMachine";
	}
	
	@RequestMapping("/giveWM")
	@ResponseBody
	public String giveWM(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String wpsid = request.getParameter("wpsid");
		String panelnum = request.getParameter("panelnum");
		String machid = request.getParameter("machid");
		String[] wfid = wpsid.split(",");
		String[] wpre = panelnum.split(",");
		String[] mmid = machid.split(",");
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<wfid.length;i++){
				wps.setFid(Long.parseLong(wfid[i]));
		        wps.setFweld_prechannel(Integer.parseInt(wpre[i]));
				wps.setFcreater(myuser.getId());
				wps.setFupdater(myuser.getId());
				wps.setInsid(wpsService.findByUid(myuser.getId()));
				wps.setFcreatedate(sdf.parse(sdf.format((new Date()).getTime())));
				wps.setFupdatedate(sdf.parse(sdf.format((new Date()).getTime())));
				for(int j=0;j<mmid.length;j++){
					wps.setMacid(new BigInteger(mmid[j]));
					wpsService.give(wps);
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
	
	@RequestMapping("/findHistory")
	@ResponseBody
	public String findHistory(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		page = new Page(pageIndex,pageSize,total);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		page = new Page(pageIndex,pageSize,total);
		List<Wps> findHistory = wpsService.findHistory(page,parent);
		long total = 0;	
		if(findHistory != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(findHistory);
			total = pageinfo.getTotal();
		}
		try{
			for(Wps wps:findHistory){
				json.put("FWPSNum", wps.getFwpsnum());
				json.put("Fweld_PreChannel", wps.getFweld_prechannel());
				json.put("FCReateDate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFcreatedate()));
				json.put("Fname", wps.getFname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/getWpslibList")
	@ResponseBody
	public String getWpslibList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<Wps> getWpslibList = wpsService.getWpslibList(page,search);
		long total = 0;
		if(getWpslibList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(getWpslibList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:getWpslibList){
				json.put("fid", wps.getFid());
				json.put("wpslibName", wps.getFwpsnum());
				json.put("createdate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFcreatedate()));
				json.put("status", wps.getInsname());
				json.put("statusId", wps.getFstatus());
				json.put("model", wps.getMacid());
				json.put("modelname", wps.getFname());
				json.put("manu", wps.getConname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addWpslib")
	@ResponseBody
	public String addWpslib(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		JSONObject obj = new JSONObject();
		String wpslibName = request.getParameter("wpslibName");
		String machineModel = request.getParameter("machineModel");
		int status = Integer.valueOf(request.getParameter("fstatus"));
		try{
			wps.setFwpsnum(wpslibName);
			wps.setFback(machineModel);
			wps.setFcreater(myuser.getId());
			wps.setFstatus(status);
			wpsService.saveWpslib(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateWpslib")
	@ResponseBody
	public String updateWpslib(HttpServletRequest request){
		Wps wps = new Wps();
		JSONObject obj = new JSONObject();
		String wpslibName = request.getParameter("wpslibName");
		int status = Integer.valueOf(request.getParameter("fstatus"));
		long fid = new Long(request.getParameter("fid"));
		try{
			wps.setFid(fid);
			wps.setFwpsnum(wpslibName);
			wps.setFstatus(status);
			wpsService.updateWpslib(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/removeWpslib")
	@ResponseBody
	public String removeWpslib(HttpServletRequest request){
			BigInteger fid = new BigInteger(request.getParameter("fid"));
			JSONObject obj = new JSONObject();
			try{
				wpsService.deleteWpslib(fid);
				wpsService.deleteWpsBelongLib(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/getMainwpsList")
	@ResponseBody
	public String getMainwpsList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		List<Wps> getMainwpsList = wpsService.getMainwpsList(page,parent);
		long total = 0;
		if(getMainwpsList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(getMainwpsList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:getMainwpsList){
				json.put("fid", wps.getFid());
				json.put("fchanel", wps.getWelderid());
				json.put("finitial", "否");
				json.put("initial", "0");
				if(Integer.valueOf(wps.getFinitial())==1){
					json.put("finitial", "是");
					json.put("initial", "1");
				}
				json.put("fcontroller", "否");
				json.put("controller", "0");
				if(Integer.valueOf(wps.getFcontroller())==1){
					json.put("fcontroller", "是");
					json.put("controller", "1");
				}
				json.put("fselect",wps.getInsname());
				json.put("fselectname",wps.getSelectname());
				json.put("farc", wps.getWeldername());
				json.put("farcname", wps.getArcname());
				json.put("fcharacter", wps.getFweld_v_max());
				json.put("fmode", "否");
				json.put("mode", "0");
				if(Integer.valueOf(wps.getFmode())==1){
					json.put("fmode", "是");
					json.put("mode", "1");
				}
				json.put("ftorch", "否");
				json.put("torch", "0");
				if(wps.getFtorch()==1){
					json.put("ftorch", "是");
					json.put("torch", "1");
				}
				json.put("fmaterial", wps.getUpdatename());
				json.put("fmaterialname", wps.getMaterialname());
				json.put("fgas", wps.getFback());
				json.put("fgasname", wps.getGasname());
				json.put("fdiameter", wps.getFname());
				json.put("fdiametername", wps.getDianame());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("farc_tuny_vol", wps.getFdiameter());
				json.put("fweldprocess", wps.getFprocessid());
				json.put("fprocessname", wps.getFprocessname());
				json.put("fwarn_ele_up", wps.getFwarn_ele_up());
				json.put("fwarn_ele_down", wps.getFwarn_ele_down());
				json.put("fwarn_vol_up", wps.getFwarn_vol_up());
				json.put("fwarn_vol_down", wps.getFwarn_vol_down());
				
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/wlvalidate")
	@ResponseBody
	private String gidvalidate(HttpServletRequest request){
		String wpsName = request.getParameter("wpsName");
		boolean data = true;
		int count = wpsService.getWpslibNameCount(wpsName);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	/**
	 * 获取工艺库状态
	 * @return
	 */
	@RequestMapping("/getStatusAll")
	@ResponseBody
	public String getStatusAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Wps> getWpslibStatus = wpsService.getWpslibStatus();
			for(Wps wps:getWpslibStatus){
				json.put("id", wps.getInsid());
				json.put("name", wps.getInsname());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取工艺库状态
	 * @return
	 */
	@RequestMapping("/getCountByWpslibidChanel")
	@ResponseBody
	public String getCountByWpslibidChanel(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		BigInteger wpslibid = new BigInteger(request.getParameter("wpslibid"));
		int chanel = Integer.valueOf(request.getParameter("chanel"));
		int count = 0;
		try{
			count = wpsService.getCountByWpslibidChanel(wpslibid,chanel);
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("count", count);
		return obj.toString();
	}
	

	@RequestMapping("/getSxWpsList")
	@ResponseBody
	public String getSxWpsList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String parentId = request.getParameter("fwpslib_id");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		List<Wps> list = wpsService.getSxWpsList(page, parent);
		long total = 0;
		if(list != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				json.put("fid", list.get(i).getFid());
				json.put("fwpsnum", list.get(i).getFwpsnum());
				json.put("sxfcharacter", list.get(i).getFcharacter());
				json.put("ftime", list.get(i).getFtime());
				json.put("fhysteresis",list.get(i).getFhysteresis());
				json.put("fadvance",list.get(i).getFadvance());
				json.put("fini_ele", list.get(i).getFini_ele());
				json.put("fini_vol", list.get(i).getFini_vol());
				json.put("fini_vol1", list.get(i).getFini_vol1());
				json.put("fweld_ele", list.get(i).getFweld_ele());
				json.put("fweld_vol", list.get(i).getFweld_vol());
				json.put("fweld_vol1", list.get(i).getFweld_vol1());
				json.put("farc_ele", list.get(i).getFarc_ele());
				json.put("farc_vol", list.get(i).getFarc_vol());
				json.put("farc_vol1", list.get(i).getFarc_vol1());
				json.put("fweld_tuny_ele", list.get(i).getFweld_tuny_ele());
				json.put("fweld_tuny_vol", list.get(i).getFweld_tuny_vol());
				json.put("farc_tuny_vol", list.get(i).getFarc_tuny_vol());
				json.put("farc_tuny_ele", list.get(i).getFarc_tuny_ele());
				json.put("fpreset_ele_top", list.get(i).getFpreset_ele_top());
				json.put("fpreset_vol_top", list.get(i).getFpreset_vol_top());
				json.put("fpreset_ele_bottom", list.get(i).getFpreset_ele_bottom());
				json.put("fpreset_vol_bottom", list.get(i).getFpreset_vol_bottom());
				json.put("farc_vol_top", list.get(i).getFarc_vol_top());
				json.put("fpreset_ele_warn_top", list.get(i).getFpreset_ele_warn_top());
				json.put("fpreset_vol_warn_top", list.get(i).getFpreset_vol_warn_top());
				json.put("fpreset_ele_warn_bottom", list.get(i).getFpreset_ele_warn_bottom());
				json.put("fpreset_vol_warn_bottom", list.get(i).getFpreset_vol_warn_bottom());
				json.put("fini_ele_warn_top", list.get(i).getFini_ele_warn_top());
				json.put("fini_vol_warn_top", list.get(i).getFini_vol_warn_top());
				json.put("fini_ele_warn_bottom", list.get(i).getFini_ele_warn_bottom());
				json.put("fini_vol_warn_bottom", list.get(i).getFini_vol_warn_bottom());
				json.put("farc_ele_warn_top", list.get(i).getFarc_ele_warn_top());
				json.put("farc_vol_warn_top", list.get(i).getFarc_vol_warn_top());
				json.put("farc_ele_warn_bottom", list.get(i).getFarc_ele_warn_bottom());
				json.put("farc_vol_warn_bottom", list.get(i).getFarc_vol_warn_bottom());
				json.put("farc_delay_time", list.get(i).getFarc_delay_time());
				json.put("fwarn_delay_time", list.get(i).getFwarn_delay_time());
				json.put("fwarn_stop_time", list.get(i).getFwarn_stop_time());
				json.put("fflow_top", list.get(i).getFflow_top());
				json.put("fflow_bottom", list.get(i).getFflow_bottom());
				json.put("fdelay_time", list.get(i).getFdelay_time());
				json.put("fover_time", list.get(i).getFover_time());
				json.put("ffixed_cycle", list.get(i).getFfixed_cycle());
				json.put("selectname", list.get(i).getSelectname());
				json.put("gasname", list.get(i).getGasname());
				json.put("dianame", list.get(i).getDianame());
				json.put("materialname", list.get(i).getMaterialname());
				json.put("fcontrollername", list.get(i).getConname());
				json.put("farcname", list.get(i).getArcname());
				json.put("ininame", list.get(i).getFinitial());
				json.put("fselect", list.get(i).getFselect());
				json.put("farc", list.get(i).getFarc());
				json.put("fmaterial", list.get(i).getFmaterial());
				json.put("fdiameter", list.get(i).getFdiameter());
				json.put("fcontroller", list.get(i).getFcontroller());
				json.put("finitial", list.get(i).getFini());
				json.put("fgas", list.get(i).getFgas());
				json.put("charactername", list.get(i).getFcharacter()==0?"停机":"不停机");
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addSxWps")
	@ResponseBody
	public String addSxWps(HttpServletRequest request,Wps wps){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject obj = new JSONObject();
		try{
			wps.setFcreater(myuser.getId());
			wps.setFupdater(myuser.getId());
			wps.setFcharacter(Integer.parseInt(request.getParameter("sxfcharacter")));
			wpsService.saveSxWps(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return obj.toString();
	}

	
	@RequestMapping("/editSxWps")
	@ResponseBody
	public String editSxWps(HttpServletRequest request,Wps wps){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject obj = new JSONObject();
		try{
			wps.setFupdater(myuser.getId());
			wps.setFcharacter(Integer.parseInt(request.getParameter("sxfcharacter")));
			wpsService.editSxWps(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	/**
	 * 获取字典值
	 * @return
	 */
	@RequestMapping("/getDictionary")
	@ResponseBody
	public String getMaterial(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(Integer.parseInt(request.getParameter("typeid")));
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

	@RequestMapping("/CRC7Check")
	@ResponseBody
	public String crc7Check(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		String[] str = request.getParameter("crc7_str").split(",");
		
		byte[] by1 = new byte[str.length];
		for(int i=0;i<str.length;i++){
			by1[i] = (byte)Integer.parseInt(str[i],16);
		}
		byte[] crc7byte = {
				0x00,0x09,0x12,0x1b,0x24,0x2d,0x36,0x3f,
				0x48,0x41,0x5a,0x53,0x6c,0x65,0x7e,0x77,
				0x19,0x10,0x0b,0x02,0x3d,0x34,0x2f,0x26,
				0x51,0x58,0x43,0x4a,0x75,0x7C,0x67,0x6e,
				0x32,0x3b,0x20,0x29,0x16,0x1f,0x04,0x0d,
				0x7a,0x73,0x68,0x61,0x5e,0x57,0x4c,0x45,
				0x2b,0x22,0x39,0x30,0x0f,0x06,0x1d,0x14,
				0x63,0x6a,0x71,0x78,0047,0x4e,0x55,0x5c,
				0x64,0x6d,0x76,0x7f,0x40,0x49,0x52,0x5b,
				0x2c,0x25,0x3e,0x37,0x08,0x01,0x1a,0x13,
				0x7d,0x74,0x6f,0x66,0x59,0x50,0x4b,0x42,
				0x35,0x3c,0x27,0x2e,0x11,0x18,0x03,0x0a,
				0x56,0x5f,0x44,0x4d,0x72,0x7b,0x60,0x69,
				0x1e,0x17,0x0c,0x05,0x3a,0x33,0x28,0x21,
				0x4f,0x46,0x5d,0x54,0x6b,0x62,0x79,0x70,
				0x07,0x0e,0x15,0x1c,0x23,0x2a,0x31,0x38,
				0x41,0x48,0x53,0x5a,0x65,0x6C,0x77,0x7e,
				0x09,0x00,0x1b,0x12,0x2d,0x24,0x3f,0x36,
				0x58,0x51,0x4a,0x43,0x7c,0x75,0x6e,0x67,
				0x10,0x19,0x02,0x0b,0x34,0x3d,0x26,0x2f,
				0x73,0x7a,0x61,0x68,0x57,0x5e,0x45,0x4c,
				0x3b,0x32,0x29,0x20,0x1f,0x16,0x0d,0x04,
				0x6a,0x63,0x78,0x71,0x4e,0x47,0x5c,0x55,
				0x22,0x2b,0x30,0x39,0x06,0x0f,0x14,0x1d,
				0x25,0x2C,0x37,0x3e,0x01,0x08,0x13,0x1a,
				0x6d,0x64,0x7f,0x76,0x49,0x40,0x5b,0x52,
				0x3C,0x35,0x2e,0x27,0x18,0x11,0x0a,0x03,
				0x74,0x7d,0x66,0x6f,0x50,0x59,0x42,0x4b,
				0x17,0x1e,0x05,0x0c,0x33,0x3a,0x21,0x28,
				0x5f,0x56,0x4d,0x44,0x7b,0x72,0x69,0x60,
				0x0e,0x07,0x1c,0x15,0x2a,0x23,0x38,0x31,
				0x46,0x4f,0x54,0x5d,0x62,0x6b,0x70,0x79};
		byte result = 0;  
        for (int i = 0; i < (0 + by1.length); ++i) {  
        	result = crc7byte[(0x00ff & ((result <<1) ^ by1[i]))];  
        }  
        obj.put("CRC7_code", result);
        return obj.toString();
	}
	
	@RequestMapping("/getWpslibMachineHistory")
	@ResponseBody
	public String getWpslibMachineHistory(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String machineNum = request.getParameter("machineNum");
		String wpslibName = request.getParameter("wpslibName");
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		if(iutil.isNull(machineNum)){
			machineNum = "'%" + machineNum + "%'";
		}
		if(iutil.isNull(wpslibName)){
			wpslibName = "'%" + wpslibName + "%'";
		}
		WeldDto dto = new WeldDto();
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		List<Wps> list = wpsService.getWpslibMachineHistoryList(page, machineNum, wpslibName, dto);
		long total = 0;
		if(list != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				if(String.valueOf(list.get(i).getMacid()).equals("171")){
					json.put("fid", list.get(i).getFid());
					json.put("machineNum", list.get(i).getInsname());
					json.put("wpslibName", list.get(i).getFwpsnum());
					json.put("machineModel",list.get(i).getMacid());
					json.put("chanel",list.get(i).getInsid());
					json.put("updateTime", list.get(i).getUpdatename());
					json.put("weld_ele", list.get(i).getFweld_ele());
					json.put("warn_ele_up", list.get(i).getFwarn_ele_up());
					json.put("warn_ele_down", list.get(i).getFwarn_ele_down());
					json.put("weld_vol", list.get(i).getFweld_vol());
					json.put("warn_vol_up", list.get(i).getFwarn_vol_up());
					json.put("warn_vol_down", list.get(i).getFwarn_vol_down());
					ary.add(json);
				}else{
					json.put("fid", list.get(i).getFid());
					json.put("machineNum", list.get(i).getInsname());
					json.put("wpslibName", list.get(i).getFwpsnum());
					json.put("machineModel",list.get(i).getMacid());
					json.put("chanel",list.get(i).getInsid());
					json.put("updateTime", list.get(i).getUpdatename());
					json.put("weld_ele", "");
					json.put("warn_ele_up", list.get(i).getFpreset_ele_warn_top());
					json.put("warn_ele_down", list.get(i).getFpreset_ele_warn_bottom());
					json.put("weld_vol", "");
					json.put("warn_vol_up", list.get(i).getFpreset_vol_warn_top());
					json.put("warn_vol_down", list.get(i).getFpreset_vol_warn_bottom());
					ary.add(json);
				}
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getSpeDetail")
	@ResponseBody
	public String getSpeDetail(HttpServletRequest request){
		String machineId = request.getParameter("machineId");
		String chanel = request.getParameter("chanel");
		String time = request.getParameter("time");
		String machineModel = request.getParameter("machineModel");
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			if(machineModel.equals("171")){
				Wps wps = wpsService.getOtcDetail(machineId,chanel,time);
				json.put("fid", wps.getFid());
				json.put("fchanel", wps.getWelderid());
				json.put("finitial", "否");
				json.put("initial", "0");
				if(Integer.valueOf(wps.getFinitial())==1){
					json.put("finitial", "是");
					json.put("initial", "1");
				}
				json.put("fcontroller", "否");
				json.put("controller", "0");
				if(Integer.valueOf(wps.getFcontroller())==1){
					json.put("fcontroller", "是");
					json.put("controller", "1");
				}
				json.put("fselect",wps.getInsname());
				json.put("fselectname",wps.getSelectname());
				json.put("farc", wps.getWeldername());
				json.put("farcname", wps.getArcname());
				json.put("fcharacter", wps.getFweld_v_max());
				json.put("fmode", "否");
				json.put("mode", "0");
				if(Integer.valueOf(wps.getFmode())==1){
					json.put("fmode", "是");
					json.put("mode", "1");
				}
				json.put("ftorch", "否");
				json.put("torch", "0");
				if(wps.getFtorch()==1){
					json.put("ftorch", "是");
					json.put("torch", "1");
				}
				json.put("fmaterial", wps.getUpdatename());
				json.put("fmaterialname", wps.getMaterialname());
				json.put("fgas", wps.getFback());
				json.put("fgasname", wps.getGasname());
				json.put("fdiameter", wps.getFname());
				json.put("fdiametername", wps.getDianame());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("farc_tuny_vol", wps.getFdiameter());
				json.put("fweldprocess", wps.getFprocessid());
				json.put("fprocessname", wps.getFprocessname());
				json.put("fwarn_ele_up", wps.getFwarn_ele_up());
				json.put("fwarn_ele_down", wps.getFwarn_ele_down());
				json.put("fwarn_vol_up", wps.getFwarn_vol_up());
				json.put("fwarn_vol_down", wps.getFwarn_vol_down());
				ary.add(json);
			}else{
				Wps list = wpsService.getSxDetail(machineId,chanel,time);
				json.put("fid", list.getFid());
				json.put("fwpsnum", list.getFwpsnum());
				json.put("sxfcharacter", list.getFcharacter());
				json.put("ftime", list.getFtime());
				json.put("fhysteresis",list.getFhysteresis());
				json.put("fadvance",list.getFadvance());
				json.put("fini_ele", list.getFini_ele());
				json.put("fini_vol", list.getFini_vol());
				json.put("fini_vol1", list.getFini_vol1());
				json.put("fweld_ele", list.getFweld_ele());
				json.put("fweld_vol", list.getFweld_vol());
				json.put("fweld_vol1", list.getFweld_vol1());
				json.put("farc_ele", list.getFarc_ele());
				json.put("farc_vol", list.getFarc_vol());
				json.put("farc_vol1", list.getFarc_vol1());
				json.put("fweld_tuny_ele", list.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", list.getFweld_tuny_vol());
				json.put("farc_tuny_vol", list.getFarc_tuny_vol());
				json.put("farc_tuny_ele", list.getFarc_tuny_ele());
				json.put("fpreset_ele_top", list.getFpreset_ele_top());
				json.put("fpreset_vol_top", list.getFpreset_vol_top());
				json.put("fpreset_ele_bottom", list.getFpreset_ele_bottom());
				json.put("fpreset_vol_bottom", list.getFpreset_vol_bottom());
				json.put("farc_vol_top", list.getFarc_vol_top());
				json.put("fpreset_ele_warn_top", list.getFpreset_ele_warn_top());
				json.put("fpreset_vol_warn_top", list.getFpreset_vol_warn_top());
				json.put("fpreset_ele_warn_bottom", list.getFpreset_ele_warn_bottom());
				json.put("fpreset_vol_warn_bottom", list.getFpreset_vol_warn_bottom());
				json.put("fini_ele_warn_top", list.getFini_ele_warn_top());
				json.put("fini_vol_warn_top", list.getFini_vol_warn_top());
				json.put("fini_ele_warn_bottom", list.getFini_ele_warn_bottom());
				json.put("fini_vol_warn_bottom", list.getFini_vol_warn_bottom());
				json.put("farc_ele_warn_top", list.getFarc_ele_warn_top());
				json.put("farc_vol_warn_top", list.getFarc_vol_warn_top());
				json.put("farc_ele_warn_bottom", list.getFarc_ele_warn_bottom());
				json.put("farc_vol_warn_bottom", list.getFarc_vol_warn_bottom());
				json.put("farc_delay_time", list.getFarc_delay_time());
				json.put("fwarn_delay_time", list.getFwarn_delay_time());
				json.put("fwarn_stop_time", list.getFwarn_stop_time());
				json.put("fflow_top", list.getFflow_top());
				json.put("fflow_bottom", list.getFflow_bottom());
				json.put("fdelay_time", list.getFdelay_time());
				json.put("fover_time", list.getFover_time());
				json.put("ffixed_cycle", list.getFfixed_cycle());
				json.put("selectname", list.getSelectname());
				json.put("gasname", list.getGasname());
				json.put("dianame", list.getDianame());
				json.put("materialname", list.getMaterialname());
				json.put("fcontrollername", list.getConname());
				json.put("farcname", list.getArcname());
				json.put("ininame", list.getFinitial());
				json.put("fselect", list.getFselect());
				json.put("farc", list.getFarc());
				json.put("fmaterial", list.getFmaterial());
				json.put("fdiameter", list.getFdiameter());
				json.put("fcontroller", list.getFcontroller());
				json.put("finitial", list.getFini());
				json.put("fgas", list.getFgas());
				json.put("charactername", list.getFcharacter()==0?"停机":"不停机");
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/saveGiveWpsHistory")
	@ResponseBody
	public String saveGiveWpsHistory(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray wpsary = new JSONArray();
		JSONArray machineary = new JSONArray();
		JSONObject obj = new JSONObject();
		String mainwps = request.getParameter("mainwps");
		String machine = request.getParameter("machine");
		String wpslib = request.getParameter("wpslib");
		int flag = Integer.valueOf(request.getParameter("flag"));
		wpsary = JSONArray.fromObject(mainwps);
		machineary = JSONArray.fromObject(machine);
		try{
			Wps wps = new Wps();
			if(flag==0){
				for(int i=0;i<machineary.size();i++){
					for(int j=0;j<wpsary.size();j++){
						wps.setFweld_i_max(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fchanel"))));
						wps.setFweld_i_min(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("initial"))));
						wps.setFweld_alter_i(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("controller"))));
						wps.setFweld_v_min(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("mode"))));
						wps.setFweld_i(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fselect"))));
						wps.setFweld_v(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc"))));
						wps.setFweld_v_max(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fcharacter"))));
						wps.setFweld_prechannel(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fmaterial"))));
						wps.setFweld_alter_v(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fgas"))));
						wps.setInsid(new BigInteger(String.valueOf(wpsary.getJSONObject(j).get("fdiameter"))));
						wps.setFtime(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("ftime"))));
						wps.setFadvance(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fadvance"))));
						wps.setFhysteresis(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fhysteresis"))));
						wps.setFini_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele"))));
						wps.setFini_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol"))));
						wps.setFini_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol1"))));
						wps.setFweld_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_ele"))));
						wps.setFweld_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol"))));
						wps.setFweld_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol1"))));
						wps.setFarc_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele"))));
						wps.setFarc_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol"))));
						wps.setFarc_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol1"))));
						wps.setFweld_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_ele"))));
						wps.setFweld_tuny_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_vol"))));
						wps.setFarc_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_ele"))));
						wps.setFdiameter(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_vol"))));
						wps.setFid(Long.valueOf(wpslib));
						wps.setFprocessid(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweldprocess"))));
						wps.setFtorch(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("torch"))));
						wps.setFwarn_ele_up(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_ele_up"))));
						wps.setFwarn_ele_down(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_ele_down"))));
						wps.setFwarn_vol_up(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_vol_up"))));
						wps.setFwarn_vol_down(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_vol_down"))));
						wps.setMacid(new BigInteger(String.valueOf(machineary.getJSONObject(i).get("id"))));
						wpsService.saveOtcWpsHistory(wps);
						obj.put("success", true);
					}
				}
			}else{
				for(int i=0;i<machineary.size();i++){
					for(int j=0;j<wpsary.size();j++){
						wps.setFid(Long.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fid"))));
						wps.setFwpsnum(String.valueOf(wpsary.getJSONObject(j).get("fwpsnum")));
						wps.setFcharacter(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("sxfcharacter"))));
						wps.setFtime(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("ftime"))));
						wps.setFhysteresis(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fhysteresis"))));
						wps.setFadvance(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fadvance"))));
						wps.setFini_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele"))));
						wps.setFini_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol"))));
						wps.setFini_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol1"))));
						wps.setFweld_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_ele"))));
						wps.setFweld_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol"))));
						wps.setFweld_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol1"))));
						wps.setFarc_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele"))));
						wps.setFarc_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol"))));
						wps.setFarc_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol1"))));
						wps.setFweld_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_ele"))));
						wps.setFweld_tuny_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_vol"))));
						wps.setFarc_tuny_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_vol"))));
						wps.setFarc_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_ele"))));
						wps.setFpreset_ele_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_top"))));
						wps.setFpreset_vol_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_top"))));
						wps.setFpreset_ele_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_bottom"))));
						wps.setFpreset_vol_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_bottom"))));
						wps.setFarc_vol_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol_top"))));
						wps.setFpreset_ele_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_warn_top"))));
						wps.setFpreset_vol_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_warn_top"))));
						wps.setFpreset_ele_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_warn_bottom"))));
						wps.setFpreset_vol_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_warn_bottom"))));
						wps.setFini_ele_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele_warn_top"))));
						wps.setFini_vol_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol_warn_top"))));
						wps.setFini_ele_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele_warn_bottom"))));
						wps.setFini_vol_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol_warn_bottom"))));
						wps.setFarc_ele_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele_warn_top"))));
						wps.setFarc_vol_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol_warn_top"))));
						wps.setFarc_ele_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele_warn_bottom"))));
						wps.setFarc_vol_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol_warn_bottom"))));
						wps.setFarc_delay_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_delay_time"))));
						wps.setFwarn_delay_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_delay_time"))));
						wps.setFwarn_stop_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_stop_time"))));
						wps.setFflow_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fflow_top"))));
						wps.setFflow_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fflow_bottom"))));
						wps.setFdelay_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fdelay_time"))));
						wps.setFover_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fover_time"))));
						wps.setFfixed_cycle(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("ffixed_cycle"))));
						wps.setFselect(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fselect"))));
						wps.setFarc(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc"))));
						wps.setFmaterial(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fmaterial"))));
						wps.setFdiameter(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fdiameter"))));
						wps.setFcontroller(String.valueOf(wpsary.getJSONObject(j).get("fcontroller")));
						wps.setFinitial(String.valueOf(wpsary.getJSONObject(j).get("finitial")));
						wps.setFgas(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fgas"))));
						wps.setMacid(new BigInteger(String.valueOf(machineary.getJSONObject(i).get("id"))));
						wps.setFwpslib_id(new BigInteger(wpslib));
						wpsService.saveSxWpsHistory(wps);
						obj.put("success", true);
					}
				}
			}
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			e.getMessage();
		}
		return obj.toString();
	}
	
}