package com.spring.controller;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.model.MyUser;
import com.spring.model.Person;
import com.spring.model.WeldingMachine;
import com.spring.page.Page;
import com.spring.service.InsframeworkService;
import com.spring.service.PersonService;
import com.spring.service.WeldingMachineService;
import com.spring.util.IsnullUtil;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/welders",produces = { "text/json;charset=UTF-8" })
public class PersonController {
	
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	@Autowired
	private PersonService welderService;

	@Autowired
	private WeldingMachineService  machineService;
	
	@Autowired
	private InsframeworkService insm;
	
	IsnullUtil iutil = new IsnullUtil();
	
	/**
	 * 获取所有用户列表
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/AllWelder")
	public String AllUser(HttpServletRequest request){
		return "welder/allWelder";
	}

	@RequestMapping("/getAllWelder")
	@ResponseBody
	public String getAllWelder(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
/*		if(search!=null&&search!="null"){
		String ss[] = search.split("'");
		System.out.println(ss[0].substring(0, 11));
		if(ss[0].substring(0, 11).equals(" fwelder_no")){
			String sea = Integer.toHexString(Integer.valueOf(ss[1]));
			if(sea.length()!=4){
                int lenth=4-sea.length();
                for(int i=0;i<lenth;i++){
                	sea="0"+sea;
                }
              }
			search = " fwelder_no = '"+sea+"'";
		}
		}*/
		String parentId = request.getParameter("parent");
		parentId = String.valueOf(insm.getUserInsframework());
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		page = new Page(pageIndex,pageSize,total);
		List<Person> findAll = welderService.findAll(page,parent,search);
		long total = 0;
		
		if(findAll != null){
			PageInfo<Person> pageinfo = new PageInfo<Person>(findAll);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Person welder:findAll){
				json.put("id", welder.getId());
				json.put("name", welder.getName());
				json.put("welderno", welder.getWelderno());
				json.put("cellphone", welder.getCellphone());
				json.put("cardnum", welder.getCardnum());
				json.put("ownername", welder.getInsname());
				json.put("levename", welder.getValuename());
				json.put("qualiname", welder.getValuenamex());
				json.put("back", welder.getBack());
				json.put("leveid", welder.getLeveid());
				json.put("quali", welder.getQuali());
				json.put("owner", welder.getInsid());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/toAddWelder")
	public String toAddWelder(HttpServletRequest request){
		return "welder/addWelder";
	}
	
	@RequestMapping("/getLeve")
	@ResponseBody
	public String getIns(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		int we = Integer.parseInt(request.getParameter("we"));
		List<Person> findLeve = welderService.findLeve(we);
		try{
			if(we==8){
			for(Person welder:findLeve){
				json.put("leveid", welder.getVal());
				json.put("levename", welder.getValuename());
				ary.add(json);
			}
			}else{
				for(Person welder:findLeve){
					json.put("quaid", welder.getVal());
					json.put("quaname", welder.getValuename());
					ary.add(json);
				}
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("rows", ary);
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/addWelder")
	@ResponseBody
	public String addWelder(HttpServletRequest request){
		Person welder = new Person();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		String creat = String.valueOf(myuser.getId());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		try{
			welder.setQuali(Integer.parseInt(request.getParameter("qua")));
			welder.setLeveid(Integer.parseInt(request.getParameter("leve")));
			welder.setOwner(new BigInteger(request.getParameter("ins")));
/*			String sea = Integer.toHexString(Integer.valueOf(request.getParameter("welderno")));
			if(sea.length()!=4){
                int lenth=4-sea.length();
                for(int i=0;i<lenth;i++){
                	sea="0"+sea;
                }
              }*/
			welder.setWelderno(request.getParameter("welderno"));
			welder.setName(request.getParameter("name"));
			welder.setCellphone(request.getParameter("cellphone"));
			welder.setCardnum(request.getParameter("cardnum"));
			welder.setBack(request.getParameter("back"));
			welder.setCreater(new BigInteger(creat));
			welder.setUpdater(new BigInteger(creat));
//			welder.setCreatedate(sdf.parse(sdf.format((new Date()).getTime())));
//			welder.setUpdatedate(sdf.parse(sdf.format((new Date()).getTime())));
			welderService.save(welder);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/toUpdateWelder")
	public String toUpdateWps(@RequestParam BigInteger fid,HttpServletRequest request){
		Person Welder = welderService.findById(fid);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("welder", Welder);
//		request.setAttribute("update", sdf.format(Welder.getUpdatedate()));
//		request.setAttribute("create", sdf.format(Welder.getCreatedate()));
		return "welder/editWelder";
	}
	
	@RequestMapping("/updateWelder")
	@ResponseBody
	public String updateWelder(HttpServletRequest request){
		Person welder = new Person();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		String creat = String.valueOf(myuser.getId());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		try{
			welder.setId(new BigInteger(request.getParameter("FID")));
			welder.setQuali(Integer.parseInt(request.getParameter("qua")));
			welder.setLeveid(Integer.parseInt(request.getParameter("leve")));
			welder.setOwner(new BigInteger(request.getParameter("ins")));
			welder.setWelderno(request.getParameter("welderno"));
			welder.setName(request.getParameter("name"));
			welder.setCellphone(request.getParameter("cellphone"));
			welder.setCardnum(request.getParameter("cardnum"));
			welder.setBack(request.getParameter("back"));
			welder.setUpdater(new BigInteger(creat));
//			welder.setUpdatedate(sdf.parse(sdf.format((new Date()).getTime())));
//			welder.setCreatedate(sdf.parse(request.getParameter("createdate")));
		    welderService.update(welder);
			obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();

	}
	
	@RequestMapping("/toDestroyWelder")
	public String toDestroyWps(@RequestParam BigInteger fid,HttpServletRequest request){
		Person Welder = welderService.findById(fid);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("welder", Welder);
//		request.setAttribute("update", sdf.format(Welder.getUpdatedate()));
//		request.setAttribute("create", sdf.format(Welder.getCreatedate()));
		return "welder/destroyWelder";
	}
	
	@RequestMapping("/destroyWelder")
	@ResponseBody
	public String destroyWelder(@RequestParam BigInteger fid){

			JSONObject obj = new JSONObject();
			try{
				welderService.delete(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/weldersvalidate")
	@ResponseBody
	private String weldersvalidate(@RequestParam String welderno){
		boolean data = true;
		int count = welderService.getUsernameCount(welderno);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	/**
	 * 获取焊工焊机信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWelderMachine")
	@ResponseBody
	public String getWelderMachine(HttpServletRequest request){
//		JSONObject welderjson = new JSONObject();
//		JSONArray welderary = new JSONArray();
		JSONObject machinerjson = new JSONObject();
		JSONArray machineary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
//			List<Person> welderlist = welderService.getWelder();
			List<WeldingMachine> machinelist = machineService.getAllMachine();
//			for(Person welder:welderlist){
//				welderjson.put("weldername", welder.getName());
//				welderjson.put("welderno", welder.getWelderno());
//				welderary.add(welderjson);
//			}
			for(WeldingMachine machine:machinelist){
				machinerjson.put("insfname", machine.getInsframeworkId().getName());
				machinerjson.put("machineno", machine.getEquipmentNo());
				machineary.add(machinerjson);
			}
		}catch(Exception e){
			e.getMessage();
		}
//		obj.put("welderary", welderary);
		obj.put("machineary", machineary);
		return obj.toString();
	}
	
	/**
	 * 获取焊工信息(不含分页)
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWelderNoPage")
	@ResponseBody
	public String getWelderNoPage(HttpServletRequest request){
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			JSONObject obj = new JSONObject();
			IsnullUtil iutil = new IsnullUtil();
			//客户端执行操作
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient("http://localhost:8080/CIWJN_Service/cIWJNWebService?wsdl");
			iutil.Authority(client);
			String obj1 = "{\"CLASSNAME\":\"welderWebServiceImpl\",\"METHOD\":\"getWelderAll\"}";
			String obj2 = "{\"STR\":\"\"}";
			try {
			   Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"),
						new Object[] { obj1,obj2 });
			   String restr = objects[0].toString();
		       JSONArray ary = JSONArray.fromObject(restr);
		       for(int i=0;i<ary.size();i++){
		       String str = ary.getString(i);
		       JSONObject js = JSONObject.fromObject(str);
		       json.put("id", js.getString("ID"));
		       json.put("welderno", js.getString("WELDERNO"));
		       rows.add(json);
		       }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			obj.put("ary", rows);
			return obj.toString();
		}

	}