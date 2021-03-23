package com.sshome.ssmcxf.webservice.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.FaultService;
import com.sshome.ssmcxf.webservice.FaultWebService;
@Transactional
@Service
public class FaultWebServiceImpl implements FaultWebService {
	@Autowired
	private FaultService fs;
	@Override
	public Object getFaultAll(String object) {
		return fs.getFaultAll(object);
	}

	@Override
	public Object getFaultById(String object) {
		return fs.getFaultById(object);
	}

	@Override
	public BigInteger addFault(String object) {
		return fs.addFault(object);
	}

	@Override
	public boolean editFault(String object) {
		return fs.editFault(object);
	}

	@Override
	public boolean deleteFault(String object) {
		return fs.deleteFault(object);
	}

}
