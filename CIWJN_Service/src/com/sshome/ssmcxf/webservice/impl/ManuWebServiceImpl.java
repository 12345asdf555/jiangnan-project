package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.ManufacturerService;
import com.sshome.ssmcxf.webservice.ManuWebService;

@Service
@Transactional
public class ManuWebServiceImpl implements ManuWebService {
	@Autowired
	private ManufacturerService ms;
	
	@Override
	public Object getmanuAll(String object) {
		return ms.getmanuAll(object);
	}

	@Override
	public BigInteger getManuidByValue(String object) {
		return ms.getManuidByValue(object);
	}

	@Override
	public int getManuCount(String object) {
		return ms.getManuCount(object);
	}

	@Override
	public Object getManuById(String object) {
		return ms.getManuById(object);
	}

	@Override
	public BigInteger addManu(String object) {
		return ms.addManu(object);
	}

	@Override
	public boolean editManu(String object) {
		return ms.editManu(object);
	}

	@Override
	public boolean deleteManu(String object) {
		return ms.deleteManu(object);
	}

}
