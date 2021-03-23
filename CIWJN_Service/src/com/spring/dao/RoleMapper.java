package com.spring.dao;


	import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.Role;

	public interface RoleMapper {
		int saveAuthority(Role role);
		int save(Role role);
		int saveUser(Role role);
		boolean update(Role role);
		boolean delete(int id);
		boolean delete1(int id);
		boolean delete2(String roleName);
		boolean deleteAuthority(int id);
		boolean deleteUser(int id);
		Role findById(int id);
		String findByAuthorityId(Integer id);
		String findByUserId(Integer id);
		List<Role> findAll(@Param("str")String str);
		List<Role> findAllAuthority();
		List<Role> findAuthority(Integer id);
		String updateRoleAuthority(Integer findByAuthorityId);
		String updateRoleUser(Integer findByAuthorityId);
		List<Role> findAllUser();
		List<Role> findUser(Integer id);
		int getRolenameCount(@Param("roleName")String roleName);
	}