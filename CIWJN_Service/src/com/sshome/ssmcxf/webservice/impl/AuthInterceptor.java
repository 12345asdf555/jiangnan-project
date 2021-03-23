package com.sshome.ssmcxf.webservice.impl;


import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Node;
public class AuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
	
 private SAAJInInterceptor saa = new SAAJInInterceptor();
 
 public AuthInterceptor() {
	  super(Phase.PRE_PROTOCOL);
	  getAfter().add(SAAJInInterceptor.class.getName());
 }
 public void handleMessage(SoapMessage message) throws Fault {
	 SOAPMessage mess = message.getContent(SOAPMessage.class);
	  if (mess == null) {
	   saa.handleMessage(message);
	   mess = message.getContent(SOAPMessage.class);
	  }
	  try {
		  SOAPHeader header = mess.getSOAPHeader();
		  if (header != null) {
			  Node userNameNode = header.getElementsByTagName("userName").item(0);
			  Node passwordNode = header.getElementsByTagName("password").item(0);
			  String userName = "", password = "";
			  if(userNameNode != null && passwordNode != null){
				  userName = userNameNode.getTextContent();
				  password = passwordNode.getTextContent();
				  if (userName != null && !"".equals(userName ) && password != null && !"".equals(password )) {
					  if(userName.equals("admin") && password.equals("123456")){
						  System.out.println("认证成功！！！");
					  }else{
						  SOAPException soapExc = new SOAPException("认证错误");
						  throw new Fault(soapExc);
					  }
				  }else{
					  SOAPException soapExc = new SOAPException("认证错误");
					  throw new Fault(soapExc);
				  }
			  }else{
				  SOAPException soapExc = new SOAPException("认证错误");
				  throw new Fault(soapExc);
			  }
		  } else {
			  SOAPException soapExc = new SOAPException("认证错误");
			  throw new Fault(soapExc);
		  }
	  } catch (SOAPException e) {
		  e.printStackTrace();
	  }
 	}
}
