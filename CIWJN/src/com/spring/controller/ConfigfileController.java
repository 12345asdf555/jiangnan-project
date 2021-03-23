package com.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.util.UploadUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/configfile", produces = { "text/json;charset=UTF-8" })
public class ConfigfileController {
	
	/**
	 * 上传配置文件
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadConfigFile")
	@ResponseBody
	public String getWeldingMachine(HttpServletRequest request,HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			u.uploadConfigFile(request, response);
            obj.put("success", true);
			obj.put("msg", "上传成功");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("msg", "上传失败");
		}
		return obj.toString();
	}
}
