package com;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class Socketsend implements Callback {

	private String str;
	private String ip1;
	private Socket socket;
	private SocketChannel socketChannel = null;
	
	public Socketsend(String str, String ip1) {
		// TODO Auto-generated constructor stub
		
		this.str = str;
		this.ip1 = ip1;
		
		try{
			
			if(ip1 == "null"){
				
				if (str.length() == 110) {  

		             //校验第一位是否为FA末位是否为F5
		       	     String check1 =str.substring(0,2);
		       	     String check11=str.substring(108,110);
		       	     if(check1.equals("FA") && check11.equals("F5")){
			        		
		       	    	 //校验长度
		           	     int check2=str.length();
		           	     if(check2==110){
		           	        			
		           	    	 //校验位校验
		               	     String check3=str.substring(2,104);
		               	     String check5="";
		               	     int check4=0;
		               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
		               	     {
		               	    	String tstr1=check3.substring(i11*2, i11*2+2);
		               	    	check4+=Integer.valueOf(tstr1,16);
		               	     }
			               	 if((Integer.toHexString(check4)).toUpperCase().length()==2){
			               		 check5 = ((Integer.toHexString(check4)).toUpperCase());
			           	     }else{
			           	    	check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
			           	     }
		               	     String check6 = str.substring(104,106);
		               	     if(check5.equals(check6)){
		               	    	 
		               	    	 
		               	    	try {    
		     		            	if(socketChannel==null){
		     		            		socketChannel = SocketChannel.open(); 
			     		                SocketAddress socketAddress = new InetSocketAddress(ip1, 5555);    
			     		                socketChannel.connect(socketAddress);
		     		            	}
		     		            	
		     		                SendAndReceiveUtil.sendData(socketChannel, str); 

		     		            } catch (Exception ex) {    
		     		                //System.out.println("转发过程中服务器连接失败");  
		     		            }
		               	    	 
		               	    	 
		               	        /*OutputStream outputStream;
		               	    	 
		               	    	byte[] data=new byte[str.length()/2];

								for (int i = 0; i < data.length; i++)
								{
									String tstr1=str.substring(i*2, i*2+2);
									Integer k=Integer.valueOf(tstr1, 16);
									data[i]=(byte)k.byteValue();
								}
		               	    	 
		               	    	String strdata = "";
		               	    	for(int i=0;i<data.length;i++){
		                         	
									//判断为数字还是字母，若为字母+256取正数
		                         	if(data[i]<0){
		                         		String r = Integer.toHexString(data[i]+256);
		                         		String rr=r.toUpperCase();
		                         		//数字补为两位数
		                             	if(rr.length()==1){
		                         			rr='0'+rr;
		                             	}
		                             	//strdata为总接收数据
		                         		strdata += rr;
		                         		
		                         	}
		                         	else{
		                         		String r = Integer.toHexString(data[i]);
		                             	if(r.length()==1)
		                         			r='0'+r;
		                             	r=r.toUpperCase();
		                         		strdata+=r;	
		                         		
		                         	}
		                         }

		                         
		                        byte[] bb3=new byte[strdata.length()/2];
		     					for (int i1 = 0; i1 < bb3.length; i1++)
		     					{
		     						String tstr1=strdata.substring(i1*2, i1*2+2);
		     						Integer k=Integer.valueOf(tstr1, 16);
		     						bb3[i1]=(byte)k.byteValue();
		     					}
		     					
		     					try {
									socket = new Socket(ip1, 5555);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
		                        
							
			                    try {
			                    	//发送消息
			                        // 步骤1：从Socket 获得输出流对象OutputStream
			                        // 该对象作用：发送数据
			                        outputStream = socket.getOutputStream();
			
			                        // 步骤2：写入需要发送的数据到输出流对象中
			                        outputStream.write(bb3);
			                        // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
			                    	
			                        // 步骤3：发送数据到服务端
			                        outputStream.flush();
			
			                        socket.close();
			                        
			                        str="";
			     		           
			                    } catch (IOException e1) {
			                    	str="";
			                        e1.printStackTrace();
			                    }*/
		               	    	 
		               	     }
		               	     else{
		               	    	 //校验位错误
		               	    	 System.out.print("数据接收校验位错误");
		               	    	 str="";
		               	     }
		                               
		           	     }
		           	        		
		           	     else{
		           	    	 //长度错误
		           	    	 System.out.print("数据接收长度错误");
		           	    	 str="";
		           	     }
		       	        		
		   	        	}
		   	        	else{
		   	        		//首位不是FE
		   	        		System.out.print("数据接收首末位错误");
		   	        		str="";
		   	        	}
		       	     
		           }
				else {
	        	   str="";
	               //System.out.println("Not receiver anything from client!");  
	           }
				
			}
			
		} catch (Exception e) {
			str="";
            //System.out.println("S: Error 2");  
            //e.printStackTrace();  
        }  
		
	}

	@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub
		
		try{
				
            if (str.length() == 108) {  

             //校验第一位是否为FA末位是否为F5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(106,108);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
       	    	 //校验长度
           	     int check2=str.length();
           	     if(check2==108){
           	        			
           	    	 //校验位校验
               	     String check3=str.substring(2,104);
               	     String check5="";
               	     int check4=0;
               	     for (int i11 = 0; i11 < check3.length()/2; i11++)
               	     {
               	    	String tstr1=check3.substring(i11*2, i11*2+2);
               	    	check4+=Integer.valueOf(tstr1,16);
               	     }
	               	 if((Integer.toHexString(check4)).toUpperCase().length()==2){
           	    	check5 = ((Integer.toHexString(check4)).toUpperCase());
           	     }else{
           	    	check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1,3);
           	     }
               	     String check6 = str.substring(104,106);
               	     if(check5.equals(check6)){
               	    	 
               	        OutputStream outputStream;
               	    	 
               	    	byte[] data=new byte[str.length()/2];

						for (int i = 0; i < data.length; i++)
						{
							String tstr1=str.substring(i*2, i*2+2);
							Integer k=Integer.valueOf(tstr1, 16);
							data[i]=(byte)k.byteValue();
						}
               	    	 
               	    	String strdata = "";
					for(int i=0;i<data.length;i++){
                         	
							//判断为数字还是字母，若为字母+256取正数
                         	if(data[i]<0){
                         		String r = Integer.toHexString(data[i]+256);
                         		String rr=r.toUpperCase();
                         		//数字补为两位数
                             	if(rr.length()==1){
                         			rr='0'+rr;
                             	}
                             	//strdata为总接收数据
                         		strdata += rr;
                         		
                         	}
                         	else{
                         		String r = Integer.toHexString(data[i]);
                             	if(r.length()==1)
                         			r='0'+r;
                             	r=r.toUpperCase();
                         		strdata+=r;	
                         		
                         	}
                         }

                         
                        byte[] bb3=new byte[strdata.length()/2];
     					for (int i1 = 0; i1 < bb3.length; i1++)
     					{
     						String tstr1=strdata.substring(i1*2, i1*2+2);
     						Integer k=Integer.valueOf(tstr1, 16);
     						bb3[i1]=(byte)k.byteValue();
     					}
     					
     					try {
							socket = new Socket(ip1, 5555);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
                        
					
	                    try {
	                    	//发送消息
	                        // 步骤1：从Socket 获得输出流对象OutputStream
	                        // 该对象作用：发送数据
	                        outputStream = socket.getOutputStream();
	
	                        // 步骤2：写入需要发送的数据到输出流对象中
	                        outputStream.write(bb3);
	                        // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
	                    	
	                        // 步骤3：发送数据到服务端
	                        outputStream.flush();
	
	                        socket.close();
	                        
	                        str="";
	     		           
	                    } catch (IOException e1) {
	                    	str="";
	                        e1.printStackTrace();
	                    }
               	    	 
               	     }
               	     else{
               	    	 //校验位错误
               	    	 System.out.print("数据接收校验位错误");
               	    	 str="";
               	     }
                               
           	     }
           	        		
           	     else{
           	    	 //长度错误
           	    	 System.out.print("数据接收长度错误");
           	    	 str="";
           	     }
       	        		
   	        	}
   	        	else{
   	        		//首位不是FE
   	        		System.out.print("数据接收首末位错误");
   	        		str="";
   	        	}
       	     
           }else {
	        	   str="";
	               //System.out.println("Not receiver anything from client!");  
	           }
            
		} catch (Exception e) {
			str="";
            //System.out.println("S: Error 2");  
            //e.printStackTrace();  
        }  
		
	}

}
