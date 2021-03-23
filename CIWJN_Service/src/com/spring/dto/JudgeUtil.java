package com.spring.dto;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.spring.model.AuthorityHeaderInterceptor;
import com.spring.model.AuthorityParameter;

public class JudgeUtil {
	/**
	 * 判断值是否为null，是则返回""，用于处理json的值为null时自动隐藏
	 * @param obj
	 * @return
	 */
	public Object setValue(Object obj){
		if(obj == null){
			obj = "";
		}
		return obj;
	}
	
	/**
	 * webservice 权限认证
	 * @param client Client类
	 */
	public void Authority(Client client){
		AuthorityParameter param = new AuthorityParameter("userName", "admin", "password", "123456");
		client.getOutInterceptors().add(new AuthorityHeaderInterceptor(param)); 
		client.getOutInterceptors().add(new LoggingOutInterceptor()); 
		HTTPClientPolicy policy = ((HTTPConduit) client.getConduit()).getClient();
		policy.setConnectionTimeout(30000);
	  	policy.setReceiveTimeout(180000);
	}
}
