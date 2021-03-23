package com;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Reciver {

	private Selector selector;
	private ServerSocketChannel ssc;
	private Callback callback1=null;
	private Callback callback2=null;
	private Callback callback3=null;
	private ArrayList<String> listarray1;
	private ArrayList<String> listarray2;
	private ArrayList<String> listarray3;
	private HashMap<String, Socket> websocket;
    private HashMap<String, SocketChannel> clientList = new HashMap<>();
	private String ip1;
	private String connet;
	private boolean type;
	private int clientcount=0;
	
	

	public void setCallback(Callback callback1,Callback callback2,Callback callback3,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1,ServerSocketChannel ssc,Selector selector,boolean type)
    {
		this.ip1=ip1;
		this.ssc=ssc;
		this.type=type;
        this.connet=connet;
        this.selector=selector;
        this.websocket=websocket;
        this.callback1=callback1;
        this.callback2=callback2;
        this.callback3=callback3;
        this.listarray1=listarray1;
        this.listarray2=listarray2;
        this.listarray3=listarray3;
    }
	
	public void reciver(){

		 try {  
			 
			 while(true){  // will block the thread  
				 synchronized(this) {
		             SocketChannel sc;
					try {
					 sc = ssc.accept();
					 
					 startWRThread(selector);
					 
		             //Get the server socket and set to non blocking mode 
		             clientcount++;
		             String countclient = Integer.toString(clientcount);
		             clientList.put(countclient,sc); 
		             sc.configureBlocking(false);  
		             sc.register(selector, SelectionKey.OP_READ);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//System.out.println("1-����");
						//e.printStackTrace();
					}  
		         } 
			 }
		 } finally{  
	            try {
	            	//System.out.println("2-����");
					selector.close(); 
					ssc.close();
	            } catch (IOException e) {
					// TODO Auto-generated catch block
	            	//System.out.println("2-����");
					//e.printStackTrace();
				} 
	     }
		
		//callback.taskResult(data);
		
	}
	
	
	private void startWRThread(Selector selector) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			SelectionKey readyKey;
			SocketChannel sc;
			String str;
			
			@Override  
            public void run() {   
                while(true){
                	synchronized(this) {
	                	try {
		                    while(selector.selectNow() > 0){  
		
		                        Iterator<SelectionKey> it = selector.selectedKeys().iterator();  
		                        //// Walk through the ready keys collection and process date requests.  
		                        while(it.hasNext()){  
		                            readyKey = it.next();  
		                            if(readyKey.isReadable()){  
		                            	try{
		                            		sc = (SocketChannel) readyKey.channel();  
		                                	str = SendAndReceiveUtil.receiveData(sc);
		                                
		                                    
		                                /* if(msg != null && !msg.equals("")) {  
		                                	 
		                                         //System.out.println(msg);  
		                                         com.SendAndReceiveUtil.sendData(sc,msg);
		                                         sc.shutdownOutput(); 
		                                 }  */
		                                
		                                //�·�����
		                                if(str.subSequence(0, 2).equals("FA") && str.subSequence(6, 8).equals("52")){
		                                    	
		  
		                                    // ���δ���selector�ϵ�ÿ����ѡ���SelectionKey  
		                                	for (Entry<String, SocketChannel> entry : clientList.entrySet()) {
		                                		sc=entry.getValue();
		                                		String clientadd = sc.getRemoteAddress().toString().replace("/", "");
		                                		String[] clientdetail = clientadd.split(":");
		                                		String clientip = clientdetail[0];
		                                		if(!clientip.equals("121.196.222.216")){
		                                			
		                                			SendAndReceiveUtil.sendData(sc,str);
		                                			
		                                		}
		                                	}
		                                }
		                                }catch(Exception e){  
		                                	//System.out.println("4-����");
		                                	//e.printStackTrace();  
		                                	readyKey.cancel();
		                                	/*try {
		            							sc.socket().close();
		            							sc.close();
		            	                	} catch (IOException e1) {
		            							// TODO Auto-generated catch block
		            	                		//System.out.println("5");
		            							e1.printStackTrace();
		            						}*/
		            	                	return;
		                                } /*else{
		                                	sqlwritetype=1;
		                                    sockettype=1;
		                                    if(str.substring(0, 2).equals("FA")){
		                		                    websendtype=1;
		                	                    }
		                                    }*/
		                            	if(str.subSequence(0, 2).equals("FE")){
		                            		callback1.taskResult(str, connet,listarray1,listarray2,listarray3,websocket,ip1);
		                            	}
		                            	else{
		                            		callback1.taskResult(str, connet,listarray1,listarray2,listarray3,websocket,ip1);
		                            		callback2.taskResult(str, connet,listarray1,listarray2,listarray3,websocket,ip1);
		                            	}
	                                	
	                                	//callback3.taskResult(str, connet,listarray1,listarray2,listarray3,websocket,ip1);
	                                	try{
	                                		it.remove(); 
	                                	}catch (Exception e) {  
	                	                    // TODO Auto-generated catch block
	                                		//System.out.println("6-����");
	                	                    //e.printStackTrace();  
	                	                } 
	                                }  
	  
	                                //execute((ServerSocketChannel) readyKey.channel());  
		                        }  
		                    }  
		                } catch (IOException e) {  
		                    // TODO Auto-generated catch block 
		                	//System.out.println("3-����");
		                	readyKey.cancel();
		                	////e.printStackTrace();  
		                } 
                	}
                }   
			}
		}).start(); 
	}
	
	
	
}
