package com.spring.service;

import java.util.List;

import com.spring.model.Authority;


public interface AuthorityService {
	boolean save(Authority authority);
	boolean saveResource(Authority authority);
	boolean update(Authority authority);
	boolean delete(int id);
	boolean delete1(int id);
	boolean delete2(int id);
	boolean deleteResource(int id);
	String findByResourceId(Integer id);
	Authority findById(Integer id);
	List<Authority> findAll(String search);
	List<Authority> findAllResource();
	List<Authority> findResource(Integer id);
	String updateAuthorityResource(Integer findByRoleId);
	int getAuthoritynameCount(String authorityName);
	List<Authority> getAllAuthoritys();
	int findAuthId(String authName);
}