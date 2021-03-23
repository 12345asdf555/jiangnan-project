package com.sshome.ssmcxf.webservice;

public interface AuthorityWebService {
	int saveAuthority(String object);
	int saveAuthorityResource(String object);
	boolean updateAuthority(String object);
	boolean deleteAuthority(String object);
	boolean deleteAuthoritiesResources(String object);
	boolean deleteRolesAuthorities(String object);
	String findByResourceId(String object);
	Object findAuthorityById(String object);
	Object findAllAuthority(String object);
	Object findAllResource();
	Object findAuthorityResource(String object);
	String fineAuthorityNameById(String object);
	int getAuthoritynameCount(String object);
	Object getAllAuthoritys();
	int findAuthId(String object);
}
