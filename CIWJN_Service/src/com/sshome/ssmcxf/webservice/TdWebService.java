package com.sshome.ssmcxf.webservice;

public interface TdWebService {
	Object findAll(String object);
	Object findAlldiv(String object);
	Object getAllPosition(String object);
	long findIns(String object);
	long findInsid(String object);
	String findweld(String object);
	String findInsname(String object);
	String findPosition(String object);
	Object allWeldname();
	int findDic(String object);
}
