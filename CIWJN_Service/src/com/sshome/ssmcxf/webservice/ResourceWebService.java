package com.sshome.ssmcxf.webservice;

public interface ResourceWebService {
	int saveResource( String object);
	boolean updateResource(String object);
	boolean deleteResource(String object);
	Object findResourceById(String object);
	Object findResourceAll(String object);
	int getResourcenameCount(String object);
	Object getAuthByRes(String object);
}
