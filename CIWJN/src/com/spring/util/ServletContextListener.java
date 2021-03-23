package com.spring.util;

import javax.servlet.ServletContextEvent;

import com.spring.util.SocketService;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {
	SocketService service;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		service.closeServer();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		service = new SocketService();
		service.initSocketServer();
	}

}
