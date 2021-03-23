package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class DB_Connectioncode {
	public  String datasend="";

    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
	
	public String getId() {
		return datasend;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}
    public Server server;
    
    public String inSql = "";

	private String code;

	private String select;

	private Statement stmt;
	
	private Connection conn;

	private String connet;

    public DB_Connectioncode(java.sql.Statement stmt, Connection conn, String connet)

    {
    	
    	this.stmt = stmt;
    	this.conn = conn;
    	this.connet=connet;
    	/*String inSql;
    	
        java.sql.Connection conn = null;

        java.sql.Statement stmt =null;


        try {  

            Class.forName("com.mysql.jdbc.Driver");  

        } catch (ClassNotFoundException e) {  

            //System.out.println("Broken driver");

            //e.printStackTrace();  

        }  

         try {

             conn = DriverManager.getConnection(connet);

             stmt= conn.createStatement();

        } catch (SQLException e) {

            //System.out.println("Broken conn");

            //e.printStackTrace();

        }*/

    	
    	 //查焊工
        inSql = "SELECT fid,Fowner,fwelder_no FROM `tb_welder`";

        try {
        	
        	try {
    			if(stmt==null || stmt.isClosed()==true || !conn.isValid(1))
    			{
    				try {
    					Class.forName("com.mysql.cj.jdbc.Driver");
    					conn = DriverManager.getConnection(connet);
    					stmt = conn.createStatement();
    			    } catch (ClassNotFoundException e) {  
    			        //System.out.println("Broken driver");
    			        //e.printStackTrace();
    			        return;
    			    } catch (SQLException e) {
    			        //System.out.println("Broken conn");
    			        //e.printStackTrace();
    			        return;
    			    }  
    			}
    		} catch (SQLException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        	
        	ResultSet rs =stmt.executeQuery(inSql);
            
            while (rs.next()) {
	           	String welderid = rs.getString("fid");
	        	if(welderid.length()!=4){
	        		int lenth=4-welderid.length();
	        		for(int i=0;i<lenth;i++){
	        			welderid="0"+welderid;
	        		}
	        	} 
	       	
	        	String fwelder_no = rs.getString("fwelder_no");
	        	String Fowner = rs.getString("Fowner");
	        	
	        	listarray1.add(welderid);
	        	listarray1.add(fwelder_no);
	        	listarray1.add(Fowner);
            	
            }

        } catch (SQLException e) {

            //System.out.println("Broken insert");

            //e.printStackTrace();

        } 
    	
    	
    	
       //查焊机
         inSql = "select tb_gather.fid,tb_welding_machine.fequipment_no,tb_gather.fgather_no,tb_welding_machine.finsframework_id from tb_gather left join tb_welding_machine on tb_gather.fid=tb_welding_machine.fgather_id";
         
         try {

        	 try {
     			if(stmt==null || stmt.isClosed()==true || !conn.isValid(1))
     			{
     				try {
     					Class.forName("com.mysql.cj.jdbc.Driver");
     					conn = DriverManager.getConnection(connet);
     					stmt = conn.createStatement();
     			    } catch (ClassNotFoundException e) {  
     			        //System.out.println("Broken driver");
     			        //e.printStackTrace();
     			        return;
     			    } catch (SQLException e) {
     			        //System.out.println("Broken conn");
     			        //e.printStackTrace();
     			        return;
     			    }  
     			}
     		} catch (SQLException e1) {
     			// TODO Auto-generated catch block
     			e1.printStackTrace();
     		}
        	 
         	ResultSet rs =stmt.executeQuery(inSql);
             
             while (rs.next()) {
            	String fid = rs.getString("fid");
            	String fequipment_no = rs.getString("fequipment_no");
             	String fgather_no = rs.getString("fgather_no");
             	String fitemId = rs.getString("finsframework_id");
             	listarray2.add(fid);
             	listarray2.add(fequipment_no);
             	listarray2.add(fgather_no);
             	listarray2.add(fitemId);
             	
             }
             	
         } catch (SQLException e) {

             //System.out.println("Broken insert");

             //e.printStackTrace();

         }
         
         
         
         //查最大最小电流
         inSql = "select fid,fitemId,fwelded_junction_no,fmax_electricity, fmin_electricity, fmax_valtage, fmin_valtage from tb_welded_junction ";

         try {

        	 try {
     			if(stmt==null || stmt.isClosed()==true || !conn.isValid(1))
     			{
     				try {
     					Class.forName("com.mysql.cj.jdbc.Driver");
     					conn = DriverManager.getConnection(connet);
     					stmt = conn.createStatement();
     			    } catch (ClassNotFoundException e) {  
     			        //System.out.println("Broken driver");
     			        //e.printStackTrace();
     			        return;
     			    } catch (SQLException e) {
     			        //System.out.println("Broken conn");
     			        //e.printStackTrace();
     			        return;
     			    }  
     			}
     		} catch (SQLException e1) {
     			// TODO Auto-generated catch block
     			e1.printStackTrace();
     		}
        	 
         	 ResultSet rs =stmt.executeQuery(inSql);
             
             while (rs.next()) {
            	String weldjunctionid = rs.getString("fid");
             	if(weldjunctionid.length()!=8){
             		int lenth=8-weldjunctionid.length();
             		for(int i=0;i<lenth;i++){
             			weldjunctionid="0"+weldjunctionid;
             		}
             	} 
            	
             	int fmax_electricity1 = rs.getInt("fmax_electricity");
             	String fmax_electricity = String.valueOf(fmax_electricity1);
             	if(fmax_electricity.length()!=3){
             		int lenth=3-fmax_electricity.length();
             		for(int i=0;i<lenth;i++){
             			fmax_electricity="0"+fmax_electricity;
             		}
             	}
             	int fmin_electricity1 = rs.getInt("fmin_electricity");
             	String fmin_electricity = String.valueOf(fmin_electricity1);
             	if(fmin_electricity.length()!=3){
             		int lenth=3-fmin_electricity.length();
             		for(int i=0;i<lenth;i++){
             			fmin_electricity="0"+fmin_electricity;
             		}
             	}
             	int fmax_valtage1 = rs.getInt("fmax_valtage");
             	String fmax_valtage = String.valueOf(fmax_valtage1);
             	if(fmax_valtage.length()!=3){
             		int lenth=3-fmax_valtage.length();
             		for(int i=0;i<lenth;i++){
             			fmax_valtage="0"+fmax_valtage;
             		}
             	}
             	int fmin_valtage1 = rs.getInt("fmin_valtage");
             	String fmin_valtage = String.valueOf(fmin_valtage1);
             	if(fmin_valtage.length()!=3){
             		int lenth=3-fmin_valtage.length();
             		for(int i=0;i<lenth;i++){
             			fmin_valtage="0"+fmin_valtage;
             		}
             	}
             	
             	String weldjunction = rs.getString("fwelded_junction_no");
             	String fitemId = rs.getString("fitemId");
             	
             	listarray3.add(weldjunction);
             	listarray3.add(fmax_electricity);
             	listarray3.add(fmin_electricity);
             	listarray3.add(fmax_valtage);
             	listarray3.add(fmin_valtage);
             	listarray3.add(weldjunctionid);
             	listarray3.add(fitemId);
             	
             }

         } catch (SQLException e) {

             //System.out.println("Broken insert");

             //e.printStackTrace();

         } 
         
         
        /*try {

            stmt.close();
            conn.close(); 

        } catch (SQLException e) {

            //System.out.println("Broken close");

            //e.printStackTrace();

        }*/

         return; 



    }
    
    public ArrayList<String> getId1() {
		return listarray1;
	}
    
	public void setId1(ArrayList<String> listarray1) {
		this.listarray1 = listarray1;
	}
	
	public ArrayList<String> getId2() {
		return listarray2;
	}
    
	public void setId2(ArrayList<String> listarray2) {
		this.listarray2 = listarray2;
	}
	
	public ArrayList<String> getId3() {
		return listarray3;
	}
    
	public void setId3(ArrayList<String> listarray3) {
		this.listarray3 = listarray3;
	}

    
}




































/*import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;

public class com.DB_Connectioncode {
	public  String datasend="";
	
	public String getId() {
		return datasend;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}
    public com.Server server;
    public String connet = "jdbc:mysql://121.196.222.216:3306/Weld?"
    + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
    public String inSql = "";

	String Connection="jdbc:mysql://121.196.222.216:3306/Weld?"+

            "user=root&password=123456&characterEncoding=UTF8";

    String uri = "jdbc:mysql://121.196.222.216:3306/Weld?";

    String user = "user=root&password=123456&characterEncoding=UTF8";



     String connet = "jdbc:mysql://121.196.222.216:3306/Weld?"

             + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";

    String inSql = null;

	private String code;

    public com.DB_Connectioncode(String code,String connet)

    {
    	String inSql;
    	
    	this.code = code;
    	
        java.sql.Connection conn = null;

        java.sql.Statement stmt =null;


        try {  

            Class.forName("com.mysql.jdbc.Driver");  

        } catch (ClassNotFoundException e) {  

            //System.out.println("Broken driver");

            //e.printStackTrace();  

        }  

        //锟斤拷锟斤拷锟斤拷锟斤拷

         try {

             conn = DriverManager.getConnection(connet);

             //锟斤拷取锟斤拷锟绞�

             stmt= conn.createStatement();



        } catch (SQLException e) {

            //System.out.println("Broken conn");

            //e.printStackTrace();

        }



         Date date = new Date();//锟斤拷锟较低呈憋拷锟�.

         String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//锟斤拷时锟斤拷锟绞阶拷锟斤拷煞锟斤拷锟絋imestamp要锟斤拷母锟绞�.

         Timestamp goodsC_date = Timestamp.valueOf(nowTime);//锟斤拷时锟斤拷转锟斤拷



        // if(state ==1)

             inSql = "select fmax_electricity, fmin_electricity, fmax_valtage, fmin_valtage from tb_welded_junction where fwelded_junction_no = " +code;

         else {

             inSql = "insert into sj(id,bed_Number,state,Dtime) values('"+ id +"','" + bedNumber + "','leave','" + goodsC_date + "')";

        }

         try {

        	ResultSet rs =stmt.executeQuery(inSql);
            
            while (rs.next()) {
            	int fmax_electricity1 = rs.getInt("fmax_electricity");
            	String fmax_electricity = String.valueOf(fmax_electricity1);
            	if(fmax_electricity.length()!=3){
            		int lenth=3-fmax_electricity.length();
            		for(int i=0;i<lenth;i++){
            			fmax_electricity="0"+fmax_electricity;
            		}
            	}
            	int fmin_electricity1 = rs.getInt("fmin_electricity");
            	String fmin_electricity = String.valueOf(fmin_electricity1);
            	if(fmin_electricity.length()!=3){
            		int lenth=3-fmin_electricity.length();
            		for(int i=0;i<lenth;i++){
            			fmin_electricity="0"+fmin_electricity;
            		}
            	}
            	int fmax_valtage1 = rs.getInt("fmax_valtage");
            	String fmax_valtage = String.valueOf(fmax_valtage1);
            	if(fmax_valtage.length()!=3){
            		int lenth=3-fmax_valtage.length();
            		for(int i=0;i<lenth;i++){
            			fmax_valtage="0"+fmax_valtage;
            		}
            	}
            	int fmin_valtage1 = rs.getInt("fmin_valtage");
            	String fmin_valtage = String.valueOf(fmin_valtage1);
            	if(fmin_valtage.length()!=3){
            		int lenth=3-fmin_valtage.length();
            		for(int i=0;i<lenth;i++){
            			fmin_valtage="0"+fmin_valtage;
            		}
            	}
            	
            	datasend+=fmax_electricity+fmin_electricity+fmax_valtage+fmin_valtage;
            	
            }

        } catch (SQLException e) {

            //System.out.println("Broken insert");

            //e.printStackTrace();

        } 



        try {

            stmt.close();

             conn.close(); 

        } catch (SQLException e) {

            //System.out.println("Broken close");

            //e.printStackTrace();

        }  

         return; 



    }

    
}*/