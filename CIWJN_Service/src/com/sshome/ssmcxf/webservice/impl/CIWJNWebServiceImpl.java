package com.sshome.ssmcxf.webservice.impl;

import java.lang.reflect.Method;

import javax.jws.WebService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sshome.ssmcxf.webservice.CIWJNWebService;
import com.sshome.ssmcxf.webservice.impl.staticAppliceFClass.AppliceIoc;

import net.sf.json.JSONObject;

@Transactional
@Service
@WebService(endpointInterface = "com.sshome.ssmcxf.webservice.CIWJNWebService", serviceName = "CIWJNWebService")
public class CIWJNWebServiceImpl implements CIWJNWebService {
	@Override
	public Object enterTheWS(String obj1, String obj2) {
		/*try{
			JSONObject json1 = JSONObject.fromObject(obj1);
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			Class<?>  cls = context.getBean(json1.getString("CLASSNAME")).getClass();
			Method m = cls.getDeclaredMethod(json1.getString("METHOD"),new Class[]{String.class});
			Object obj = m.invoke(context.getBean(json1.getString("CLASSNAME")),new Object[]{obj2});
			return obj;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}*/
		try{
			Object obj=AppliceIoc.enterTheWS(obj1,obj2);
			return obj;
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
	}
	
	@Override
	public Object enterTheIDU(String obj1, String obj2) {
		/*try{
			JSONObject json1 = JSONObject.fromObject(obj1);
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			Class<?>  cls = context.getBean(json1.getString("CLASSNAME")).getClass();
			Method m = cls.getDeclaredMethod(json1.getString("METHOD"),new Class[]{String.class,String.class});
			Object obj = m.invoke(context.getBean(json1.getString("CLASSNAME")),new Object[]{obj1,obj2});
			return obj;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}*/
		try{
			Object obj=AppliceIoc.enterTheIDU(obj1,obj2);
			return obj;
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public Object enterNoParamWs(String obj1) {
		/*try{
			JSONObject json1 = JSONObject.fromObject(obj1);
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-common2.xml"});
			Class<?>  cls = context.getBean(json1.getString("CLASSNAME")).getClass();
			Method m = cls.getDeclaredMethod(json1.getString("METHOD"));
			Object obj = m.invoke(context.getBean(json1.getString("CLASSNAME")));
	        
			return obj;
			
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}*/
		try{
			Object obj=AppliceIoc.enterNoParamWs(obj1);
			return obj;
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
		
	}

}
