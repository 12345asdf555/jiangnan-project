package com.spring.util;

import com.spring.model.AuthorityParameter;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class IsnullUtil {
	/**
	 * 判断字符串是否为空，为空则返回false，不为空则返回true
	 * @param str
	 * @return
	 */
	public boolean isNull(String str){
		if(str==null || str.equals("")){
			return false;
		}
		return true;
	}

	public boolean isNotEmpty(String str){
		if (null != str && !"".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * webservice 鏉冮檺璁よ瘉
	 * @param client Client绫�
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
