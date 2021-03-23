package com;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Android {

	private String str;
	private java.sql.Statement stmt;
	public DB_Connectionandroid db;
	public ArrayList<String> listarray1;
	public ArrayList<String> listarray2;
	
	public Android() {
		db = new DB_Connectionandroid();
	}
    
	public void Androidrun(String str) {
		// TODO Auto-generated constructor stub
		this.str = str;
		
		//记录开始处理Android数据时间
		db.DB_androidinit1(str);
		
		//解析数据插入数据库
		byte[] b = null;
		try {
			b = str.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
         str="";
         for(int i=0;i<b.length;i++){
          	
          	//判断为数字还是字母，若为字母+256取正数
          	if(b[i]<0){
          		String r = Integer.toHexString(b[i]+256);
          		String rr=r.toUpperCase();
              	//数字补为两位数
              	if(rr.length()==1){
          			rr='0'+rr;
              	}
              	//strdata为总接收数据
          		str += rr;
          	}
          	else{
          		String r = Integer.toHexString(b[i]);
              	if(r.length()==1)
          			r='0'+r;
              	r=r.toUpperCase();
          		str+=r;	
          	}
          }
		
         	String [] stringArr = str.split("FD");
   	    	
            for(int i =0;i < stringArr.length;i++)
	        {
            	try{
            	
        	     //校验第一位是否为FE
	       	     String check1 =stringArr[i].substring(0,2);
	       	     if(check1.equals("FE")){
	       	    	 
	       	    	 //校验长度
	           	     int check2=stringArr[i].length();
	           	     if(check2==54){
	           	    	 
                    	 if(stringArr[i].length()>30){
                    		 
                    		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                            BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                            BigDecimal sensor_Num1 = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(12, 16).toString(),16));
                            String sensor_Num = String.valueOf(sensor_Num1);
                            if(sensor_Num.length()<4){
                           	 int num=4-sensor_Num.length();
                           	 for(int i1=0;i1<num;i1++){
                           		 sensor_Num="0"+sensor_Num;
                           	 }
                            }
                            String gather_id1 = Integer.toString(Integer.valueOf(stringArr[i].subSequence(16, 20).toString(),16));
                            String gather_id = String.valueOf(gather_id1);
                            if(gather_id.length()<4){
                           	 int num=4-gather_id.length();
                           	 for(int i1=0;i1<num;i1++){
                           		gather_id="0"+gather_id;
                           	 }
                            }
                            String welder_id1 = Integer.toString(Integer.valueOf(stringArr[i].subSequence(20, 24).toString(),16));
                            String welder_id = String.valueOf(welder_id1);
                            if(welder_id.length()<4){
                           	 int num=4-welder_id.length();
                           	 for(int i1=0;i1<num;i1++){
                           		 welder_id="0"+welder_id;
                           	 }
                            }
                            String code1 = Integer.toString(Integer.valueOf(stringArr[i].subSequence(24, 32).toString(),16));
                            String code = String.valueOf(code1);
                            if(code.length()<8){
                           	 int num=8-code.length();
                           	 for(int i1=0;i1<num;i1++){
                           		 code="0"+code;
                           	 }
                            }
                            BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                            String yearstr = String.valueOf(year);
                            BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                            String monthstr = String.valueOf(month);
                            BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                            String daystr = String.valueOf(day);
                            BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                            String hourstr = String.valueOf(hour);
                            BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                            String minutestr = String.valueOf(minute);
                            BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                            String secondstr = String.valueOf(second);
                            Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());
                    	
                            String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                            Date time = DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
							Timestamp timesql = new Timestamp(time.getTime());
                            
                            db.DB_Connectionandroidrun(electricity,voltage,sensor_Num,gather_id,welder_id,code,timesql,status,listarray2); 
                            str="";
                            
                    	 } 
	           	     
	           	     }   
	           	     else{
	           	    	 System.out.print("数据接收长度错误");
	           	    	 str="";
	           	     }
       	         }
	       	     else{
	   	        	 System.out.print("数据接收首末位错误");
	   	        	 str="";
	       	     }
	       	     
            	} catch (Exception e) {
        			str="";
                    //System.out.println("S: Error 2");  
                    //e.printStackTrace();  
                }     
   	     	} 	
         
            //更新状态报表和实时表
            db.DB_androidupdate();
            
            //记录处理Android数据结束时间
            db.DB_androidinit2();
            
	}
		


	/*@Override
	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1) {
		// TODO Auto-generated method stub
		
		this.str=str;
        this.connet=connet;
        //this.listarray1=listarray1;
		
		try{
			
            if (str.length() == 110) {  

            //У���һλ�Ƿ�ΪFAĩλ�Ƿ�ΪF5
       	     String check1 =str.substring(0,2);
       	     String check11=str.substring(108,110);
       	     if(check1.equals("FA") && check11.equals("F5")){
	        		
           	     //У�鳤��
           	     int check2=str.length();
           	     if(check2==110){
           	        			
               	     //У��λУ��
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
               	        				
               	    	 for(int i=0;i<78;i+=26){
               	    		 
               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                             long sensor_Num1 = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                             String sensor_Num = String.valueOf(sensor_Num1);
                             if(sensor_Num.length()<4){
                            	 int num=4-sensor_Num.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 sensor_Num="0"+sensor_Num;
                            	 }
                             }
                             long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             String machine_id = String.valueOf(machine_id1);
                             if(machine_id.length()<4){
                            	 int num=4-machine_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 machine_id="0"+machine_id;
                            	 }
                             }
                             long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             String welder_id = String.valueOf(welder_id1);
                             if(welder_id.length()<4){
                            	 int num=4-welder_id.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 welder_id="0"+welder_id;
                            	 }
                             }
                             long code1 = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             String code = String.valueOf(code1);
                             if(code.length()<8){
                            	 int num=8-code.length();
                            	 for(int i1=0;i1<num;i1++){
                            		 code="0"+code;
                            	 }
                             }
                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                             String yearstr = String.valueOf(year);
                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                             String monthstr = String.valueOf(month);
                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                             String daystr = String.valueOf(day);
                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                             String hourstr = String.valueOf(hour);
                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                             String minutestr = String.valueOf(minute);
                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                             String secondstr = String.valueOf(second);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());
               	    		 
                             String timestr = yearstr+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr+":"+secondstr;
                             try {
	                            Date time = com.DateTools.parse("yy-MM-dd HH:mm:ss",timestr);
                            	//java.util.Date time1 = timeshow.parse(timestr);
								Timestamp timesql = new Timestamp(time.getTime());

               	    		 BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26+i, 30+i).toString(),16));
                             BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30+i, 34+i).toString(),16));
                             long sensor_Num = Integer.valueOf(str.subSequence(34+i, 38+i).toString(),16);
                             long machine_id = Integer.valueOf(str.subSequence(10, 14).toString(),16);
                             long welder_id = Integer.valueOf(str.subSequence(14, 18).toString(),16);
                             long code = Integer.valueOf(str.subSequence(18, 26).toString(),16);
                             long year = Integer.valueOf(str.subSequence(40+i, 42+i).toString(),16);
                             long month = Integer.valueOf(str.subSequence(42+i, 44+i).toString(),16);
                             long day = Integer.valueOf(str.subSequence(44+i, 46+i).toString(),16);
                             long hour = Integer.valueOf(str.subSequence(46+i, 48+i).toString(),16);
                             long minute = Integer.valueOf(str.subSequence(48+i, 50+i).toString(),16);
                             long second = Integer.valueOf(str.subSequence(50+i, 52+i).toString(),16);
                             int status = Integer.parseInt(str.subSequence(38+i, 40+i).toString());
                                	
								String fitemid = str.substring(106, 108);
							 
							//com.DB_Connectionmysql a = new com.DB_Connectionmysql(electricity,voltage,sensor_Num,machine_id,welder_id,code,status,fitemid,timesql,connet,listarray1);
							} catch (Exception e) {
								str="";
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}

               	    	 }
 	                    ////System.out.println(str);
                   	    str="";
               	     }
               	        			
               	     else{
               	        //У��λ����
               	    	 System.out.print("���ݽ���У��λ����");
               	    	 str="";
               	     }
                               
           	     }
           	        		
           	     else{
           	        //���ȴ���
           	    	 System.out.print("���ݽ��ճ��ȴ���");
           	    	 str="";
           	     }
       	        		
   	        	}
   	        	else{
   	        		//��λ����FA
   	        		//System.out.println("11");
   	        		System.out.print("���ݽ�����ĩλ����");
   	        		str="";
   	        	}
       	     
           }else if(str.length()>=300 && str.length()!= 118){
        	
       	    	String [] stringArr = str.split("FD");
       	    	
                for(int i =0;i < stringArr.length;i++)
		        {
	        	     //У���һλ�Ƿ�ΪFE
		       	     String check1 =stringArr[i].substring(0,2);
		       	     if(check1.equals("FE")){
		       	    	 
		       	    	 //У�鳤��
		           	     int check2=stringArr[i].length();
		           	     if(check2==54){
		           	    	 
                        	 if(stringArr[i].length()>30){
                        		 
                        		BigDecimal electricity = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(4, 8).toString(),16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(stringArr[i].subSequence(8, 12).toString(),16));
                                BigInteger sensor_Num = new BigInteger(stringArr[i].subSequence(12, 16).toString());
                                String machine_id = stringArr[i].subSequence(16, 20).toString();
                                String welder_id = stringArr[i].subSequence(20, 24).toString();
                                String code = stringArr[i].subSequence(24, 32).toString();
                                BigInteger year = new BigInteger(Integer.valueOf(stringArr[i].subSequence(32, 34).toString(),16).toString());
                                BigInteger month = new BigInteger(Integer.valueOf(stringArr[i].subSequence(34, 36).toString(),16).toString());
                                BigInteger day = new BigInteger(Integer.valueOf(stringArr[i].subSequence(36, 38).toString(),16).toString());
                                BigInteger hour = new BigInteger(Integer.valueOf(stringArr[i].subSequence(38, 40).toString(),16).toString());
                                BigInteger minute = new BigInteger(Integer.valueOf(stringArr[i].subSequence(40, 42).toString(),16).toString());
                                BigInteger second = new BigInteger(Integer.valueOf(stringArr[i].subSequence(42, 44).toString(),16).toString());
                                Integer status = Integer.valueOf(stringArr[i].subSequence(44, 46).toString());
                        	
                                com.DB_Connectionandroid a = new com.DB_Connectionandroid(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,connet);
                                str="";
                                
                        	 } 
		           	     
		           	     }   
		           	     else{
		           	    //���ȴ���
		           	    	 System.out.print("���ݽ��ճ��ȴ���");
		           	    	 str="";
		           	     }
	       	         }
		       	     else{
		       	    	 //��λ����FE
		       	    	//System.out.println("12");
		   	        	 System.out.print("���ݽ�����ĩλ����");
		   	        	 str="";
		       	     }
	       	     }
        	   
	           }else if(str.length() == 118){
	        	   
	        	   str="";
	        	   
	           }
            
		} catch (Exception e) {
			str="";
            //System.out.println("S: Error 2");  
            //e.printStackTrace();  
        }  
		
	}*/

}
