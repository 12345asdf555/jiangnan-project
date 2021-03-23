package com.spring.controller;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.Insframework;
import com.spring.model.MyUser;
import com.spring.model.Resources;
import com.spring.model.User;
import com.spring.service.InsframeworkService;
import com.spring.service.ResourceService;
import com.spring.service.UserService;
import com.spring.util.IsnullUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/hierarchy", produces = { "text/json;charset=UTF-8" })
public class MainController {
	@Autowired
	private UserService user;
	
	@Autowired
	private InsframeworkService is;

	@Autowired
	private ResourceService rs;
	
	IsnullUtil iutil = new IsnullUtil();
	
	/**
	 * 跳转index页面进行分层
	 * @return
	 */
	@RequestMapping("/goIndex")
	@ResponseBody
	public String goGather(HttpServletRequest request){
		String hierarchy = request.getSession().getServletContext().getInitParameter("hierarchy");
		request.setAttribute("hierarchy", hierarchy);
		JSONObject obj = new JSONObject();
		obj.put("hierarchy", hierarchy);
		return obj.toString();
	}
	
	@RequestMapping("/getUserInsframework")
	@ResponseBody
	public String getUserInsframework(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(object==null){
			obj.put("uname", "请登录");
			obj.put("insframework", "无");
			return obj.toString();
		}
		MyUser u = (MyUser)object;
		User list = user.getUserInsframework(new BigInteger(u.getId()+""));
		List<String> name = rs.getAuthName((int)u.getId());
		List<Resources> menu = null;
		boolean flag = true;
		for(int i=0;i<name.size();i++){
			if(name.get(i).equalsIgnoreCase("ROLE_admin")){
				flag = false;
				menu = rs.getResourceByAdmin();
				break;
			}
		}
		if(flag){
			menu = rs.getResourceByUserid((int)u.getId());
		}
		for(int i=0;i<menu.size();i++){
			json.put("resource", menu.get(i).getResourceAddress().substring(1));
			ary.add(json);
		}
		//获取服务器ip地址
		String ipurl = request.getSession().getServletContext().getInitParameter("ipurl");
		obj.put("ipurl", ipurl);
		obj.put("uname", list.getUserName());
		obj.put("insframework", list.getInsname());
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getHierarchy")
	@ResponseBody
	public String getHierarchy(){
		JSONObject obj = new JSONObject();
		JSONArray ary1 = new JSONArray();
		JSONArray ary2 = new JSONArray();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		List<Insframework> company = is.getConmpany();
		for(Insframework i:company){
			json1.put("companyid", i.getId());
			json1.put("companyname", i.getName());
			ary1.add(json1);
			List<Insframework> caust = is.getCause(i.getId());
			for(Insframework j:caust){
				json2.put("companyid", i.getId());
				json2.put("caustid", j.getId());
				json2.put("caustname", j.getName());
				ary2.add(json2);
			}
		}
		obj.put("ary1", ary1);
		obj.put("ary2", ary2);
		return obj.toString();
	}
	
	/**
	 * 获取隐藏菜单
	 * @return
	 */
	@RequestMapping("/getHiddenMenu")
	@ResponseBody
	public String getHiddenMenu(){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		List<User> menu = user.getHiddenMenu();
		for(int i=0;i<menu.size();i++){
			json.put("name", menu.get(i).getMenuName());
			ary.add(json);
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
}
