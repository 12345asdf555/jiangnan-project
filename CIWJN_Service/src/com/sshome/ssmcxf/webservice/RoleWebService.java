package com.sshome.ssmcxf.webservice;

public interface RoleWebService {
	int saveRolesAuthority(String object);
	int saveRoles(String object);
	int saveUserRole(String object);
	boolean updateRole(String object);
	boolean deleteRole(String object);
	boolean deleteUsersRoles(String object);
	boolean deleteRolesAuthoritiesByRole(String object);
	Object findRoleById(String object);
	String findAuthorityDescById(String object);
	String findUserNameById(String object);
	Object findRoleAll(String object);
	Object findIdDescByAuthority();
	Object findAuthorityDetail(String object);
	String getRoleNameById(String object);
	Object findAllUser();
	Object findUserRoleDetail(String object);
	int getRolenameCount(String object);
}
