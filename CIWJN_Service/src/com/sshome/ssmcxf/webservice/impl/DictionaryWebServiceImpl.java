package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.spring.dto.JudgeUtil;
import com.spring.model.Dictionarys;
import com.spring.model.Insframework;
import com.spring.service.DictionaryService;
import com.spring.service.InsframeworkService;
import com.sshome.ssmcxf.webservice.DictionaryWebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class DictionaryWebServiceImpl implements DictionaryWebService{

	@Autowired
	private DictionaryService ds;

	@Autowired
	private InsframeworkService is;
	
	private JudgeUtil jutil = new JudgeUtil();
	
	@Override
	public Object getAllDictionary(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Dictionarys> list = ds.getAllDictionary(json.getString("STR"));
			for(int i=0;i<list.size();i++){
				obj.put("ID", list.get(i).getId());
				obj.put("TYPEID",jutil.setValue(list.get(i).getTypeid()));
				obj.put("VALUE",jutil.setValue(list.get(i).getValue()));
				obj.put("VALUENAME", jutil.setValue(list.get(i).getValueName()));
				obj.put("BACK",jutil.setValue(list.get(i).getBack()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object addDictionary(String obj1,String obj2) {
		try{
			//webservice获取request
			MessageContext ctx = new WebServiceContextImpl().getMessageContext();
			HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			JSONObject json = JSONObject.fromObject(obj2);
			Dictionarys d = new Dictionarys();
			d.setBack(json.getString("BACK"));
			d.setTypeid(json.getInt("TYPEID"));
			d.setValueName(json.getString("VALUENAME"));
			d.setCreator(json.getString("CREATOR"));
			boolean flag = ds.addDictionary(d);
			obj2 = obj2.substring(0,obj2.length()-1)+",\"ID\":\""+d.getId()+"\"}";
			//获取集团下所有公司
			BigInteger parent=null;
			String companystr = "",itemstr = "",resultstr = "";
			List<Insframework> company = is.getConmpany(parent);
			for(Insframework i:company){
				String companyurl = request.getSession().getServletContext().getInitParameter(i.getId().toString());
				if(companyurl!=null && !"".equals(companyurl)){
					Client companyclient = dcf.createClient(companyurl);
					jutil.Authority(companyclient);
					Object[] companyobj = companyclient.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
					companyobj[0].toString();
				}else{
					companystr = "公司";
				}
			}
			List<Insframework> item = is.getInsByType(23);
			//获取公司下所有项目部
			for(Insframework insf:item){
				String itemurl = request.getSession().getServletContext().getInitParameter(insf.getId().toString());
				if(itemurl!=null && !"".equals(itemurl)){
					Client itemclient = dcf.createClient(itemurl);
					jutil.Authority(itemclient);
					Object[] itemobj = itemclient.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2}); 
					itemobj[0].toString();
				}else{
					itemstr = "项目部";
				}
			}
			resultstr = "未找到相关";
			if(!"".equals(companystr) && !"".equals(itemstr)){
				resultstr += companystr +"、"+ itemstr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else if(!"".equals(companystr)){
				resultstr += companystr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else if(!"".equals(itemstr)){
				resultstr += itemstr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else{
				return flag;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object editDictionary(String obj1,String obj2) {
		try{
			//webservice获取request
			MessageContext ctx = new WebServiceContextImpl().getMessageContext();
			HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			JSONObject json = JSONObject.fromObject(obj2);
			Dictionarys d = new Dictionarys();
			d.setId(new BigInteger(json.getString("ID")));
			d.setBack(json.getString("BACK"));
			d.setTypeid(json.getInt("TYPEID"));
			d.setValueName(json.getString("VALUENAME"));
			d.setModifier(json.getString("MODIFIER"));
			boolean flag = ds.editDictionary(d);
			//获取集团下所有公司
			BigInteger parent=null;String companystr = "",itemstr = "",resultstr = "";
			List<Insframework> company = is.getConmpany(parent);
			for(Insframework i:company){
				String companyurl = request.getSession().getServletContext().getInitParameter(i.getId().toString());
				if(companyurl!=null && !"".equals(companyurl)){
					Client companyclient = dcf.createClient(companyurl);
					jutil.Authority(companyclient);
					Object[] companyobj = companyclient.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
					companyobj[0].toString();
				}else{
					companystr = "公司";
				}
				//未找到该项目部，请检查网络连接情况或是否部署服务
			}
			List<Insframework> item = is.getInsByType(23);
			//获取公司下所有项目部
			for(Insframework insf:item){
				String itemurl = request.getSession().getServletContext().getInitParameter(insf.getId().toString());
				if(itemurl!=null && !"".equals(itemurl)){
					Client itemclient = dcf.createClient(itemurl);
					jutil.Authority(itemclient);
					Object[] itemobj = itemclient.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2}); 
					itemobj[0].toString();
				}else{
					itemstr = "项目部";
				}
			}
			resultstr = "未找到相关";
			if(!"".equals(companystr) && !"".equals(itemstr)){
				resultstr += companystr +"、"+ itemstr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else if(!"".equals(companystr)){
				resultstr += companystr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else if(!"".equals(itemstr)){
				resultstr += itemstr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else{
				return flag;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object getDictionaryByFid(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			Dictionarys list = ds.getDictionaryByFid(json.getInt("ID"));
			if(list!=null){
				obj.put("ID", list.getId());
				obj.put("TYPEID",jutil.setValue(list.getTypeid()));
				obj.put("VALUE",jutil.setValue(list.getValue()));
				obj.put("VALUENAME", jutil.setValue(list.getValueName()));
				obj.put("BACK",jutil.setValue(list.getBack()));
			}
			return JSON.toJSONString(obj);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object deleteDictionary(String obj1,String obj2) {
		try{
			//webservice获取request
			MessageContext ctx = new WebServiceContextImpl().getMessageContext();
			HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			JSONObject json = JSONObject.fromObject(obj2);
			boolean flag = ds.deleteDictionary(json.getInt("ID"));
			//获取集团下所有公司
			BigInteger parent=null;
			String companystr = "",itemstr = "",resultstr = "";
			List<Insframework> company = is.getConmpany(parent);
			for(Insframework i:company){
				String companyurl = request.getSession().getServletContext().getInitParameter(i.getId().toString());
				if(companyurl!=null && !"".equals(companyurl)){
					Client companyclient = dcf.createClient(companyurl);
					jutil.Authority(companyclient);
					Object[] companyobj = companyclient.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
					companyobj[0].toString();
				}else{
					companystr = "公司";
				}
				//未找到该项目部，请检查网络连接情况或是否部署服务
			}
			List<Insframework> item = is.getInsByType(23);
			//获取公司下所有项目部
			for(Insframework insf:item){
				String itemurl = request.getSession().getServletContext().getInitParameter(insf.getId().toString());
				if(itemurl!=null && !"".equals(itemurl)){
					Client itemclient = dcf.createClient(itemurl);
					jutil.Authority(itemclient);
					Object[] itemobj = itemclient.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2}); 
					itemobj[0].toString();
				}else{
					itemstr = "项目部";
				}
			}
			resultstr = "未找到相关";
			if(!"".equals(companystr) && !"".equals(itemstr)){
				resultstr += companystr +"、"+ itemstr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else if(!"".equals(companystr)){
				resultstr += companystr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else if(!"".equals(itemstr)){
				resultstr += itemstr +"，请检查网络连接情况或是否部署服务";
				return resultstr;
			}else{
				return flag;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object getDictionaryValue(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Dictionarys> list = ds.getDictionaryValue(json.getInt("TYPEID"));
			for(int i=0;i<list.size();i++){
				obj.put("VALUE",jutil.setValue(list.get(i).getValue()));
				obj.put("VALUENAME", jutil.setValue(list.get(i).getValueName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getDicValueByValue(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Dictionarys> list = ds.getDicValueByValue(json.getInt("TYPEID"), json.getInt("VALUE"));
			for(int i=0;i<list.size();i++){
				obj.put("VALUE",jutil.setValue(list.get(i).getValue()));
				obj.put("VALUENAME", jutil.setValue(list.get(i).getValueName()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getvaluebyname(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ds.getvaluebyname(json.getInt("TYPEID"), json.getString("VALUENAME"));
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public Object getDicValueByType(String object) {
		try{
			JSONObject json = JSONObject.fromObject(object);
			return ds.getDicValueByType(json.getInt("VALUEID"));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Object getBack() {
		try{
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			List<Dictionarys> list = ds.getBack();
			for(int i=0;i<list.size();i++){
				obj.put("TYPEID",jutil.setValue(list.get(i).getTypeid()));
				obj.put("BACK",jutil.setValue(list.get(i).getBack()));
				ary.add(obj);
			}
			return JSON.toJSONString(ary);
		}catch(Exception e){
			return null;
		}
	}

}
