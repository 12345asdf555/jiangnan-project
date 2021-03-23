package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.WelderService;
import com.sshome.ssmcxf.webservice.WelderWebService;

@Transactional
@Service
public class WelderWebServiceImpl implements WelderWebService {
	@Autowired
	private WelderService ws;
	
	@Override
	public Object getWelderAll(String object) {
		return ws.getWelderAll(object);
	}

	@Override
	public BigInteger addWelder(String object) {
		return ws.addWelder(object);
	}

	@Override
	public boolean editWelder(String object) {
		return ws.editWelder(object);
	}

	@Override
	public boolean removeWelder(String object) {
		return ws.removeWelder(object);
	}

	@Override
	public int getWeldernoCount(String object) {
		return ws.getWeldernoCount(object);
	}

	@Override
	public Object getWelderById(String object) {
		return ws.getWelderById(object);
	}
	
	@Override
	public Object getWelderByNum(String object) {
		return ws.getWelderByNum(object);
	}

}
