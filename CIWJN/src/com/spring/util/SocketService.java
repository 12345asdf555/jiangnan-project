package com.spring.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
/** 这个socket主要还是为了 flash的socket
 * @author jxfsuda
 *
 */
public class SocketService {
 
	public static Object locker = new Object();
	private ServerSocket s = null;
	/**
	 * 线程�?,JDK5 新增
	 */
	private static ExecutorService executorService = null;// 线程�?

	public synchronized void initSocketServer() {
		try {
			if (executorService == null) {
				executorService = Executors.newFixedThreadPool(Runtime
						.getRuntime().availableProcessors() * 20);
			} else {
				return;
			}

			s = new ServerSocket(843);
			Runnable r = new Server();
			Thread t = new Thread(r);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeServer() {
		locker = null;
		if (s != null && !s.isClosed()) {
			try {
				s.close();
				System.out.println("socket 停止成功");
			} catch (IOException e) {
				 
			} finally {
				System.out.println("socket 停止....");
			}
		}
	}

	class Server implements Runnable {

		@Override
		public void run() {
			try {

				System.out.println("启动ServerSocket..");
				while (locker != null) {
					if (s == null || s.isClosed()) {
					 
						continue;
					}
					Socket incoming = s.accept();
					Runnable r = new ThreadClient(incoming);
					executorService.execute(r);
					 
				}
			} catch (Exception e) {
				 
			}

		}

	}

	class ThreadClient implements Runnable {
		private Socket incoming;

		public ThreadClient(Socket s) {
			incoming = s;
		}

		public void run() {
			try {
				System.out.println("处理线程启动");
				InputStream i = incoming.getInputStream();
				OutputStream o = incoming.getOutputStream();
				Scanner s = new Scanner(i);
				PrintWriter p = new PrintWriter(o);
				//这段话就是flash �? socket 安全策略.做过flex和web交互的同学应该知�?
				p.println("<cross-domain-policy> <site-control permitted-cross-domain-policies=\"all\"/> <allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy> \0");
				p.flush();
 
			} catch (Exception e) {
				 
			} finally {
				try {
					incoming.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
