package com.spring.service;

import java.util.List;

import com.spring.model.Resources;


public interface ResourceService {
	boolean save(Resources resource);
	boolean update(Resources resource);
	boolean delete(int id);
	Resources findById(Integer id);
	List<Resources> findAll(String search);
	List<String> getResourcesByAuthName(String authName);
	int getResourcenameCount(String resourceName);
	List<String> getAuthByRes(String url);
}