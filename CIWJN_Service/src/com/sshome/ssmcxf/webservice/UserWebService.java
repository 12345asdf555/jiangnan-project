package com.sshome.ssmcxf.webservice;

public interface UserWebService {
	int save( String object);
	boolean update(String object);
	boolean delete(String object);
	Object findById(String object);
	String findByRoleId(String object);
	int findByName(String object);
	Object findAll(String object);
	Object findRole(String object);
	Object findAllRole();
	int getUsernameCount(String object);
	Object LoadUser(String object);
	Object getAuthoritiesByUsername(String object);
	Object getIns(String object);
	Object getUserInsframework(String object);
	Object getInsUser(String object);
}
