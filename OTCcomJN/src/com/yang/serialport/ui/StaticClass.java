package com.yang.serialport.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.tempuri.WeldServiceStub;

import io.netty.channel.socket.SocketChannel;

public class StaticClass {
	
	public static HashMap<String, SocketChannel> socketlist = new HashMap();

	public static ArrayList<String> listarrayJN = new ArrayList<String>();  //任务、焊工、焊机、状态

	public static ArrayList<String> listjunction = new ArrayList<String>();
	
	public static ArrayList<String> listwelder = new ArrayList<String>();
	
	public static ArrayList<String> listweld = new ArrayList<String>();
	
	public static SocketChannel chcli;

	public static WeldServiceStub stu = null;

	public static WeldServiceStub stu1 = null;

}
