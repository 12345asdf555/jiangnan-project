package com;

import java.io.*;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class UpReport {
	//更新优化报表
	String timework1 = null;
	String timework2 = null;
	String time1 = null;
	public String ip=null;
    public String ip1=null;
    public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/"; 
    public String connet3 = "?user="; 
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";
	private String connet;
	public java.sql.Connection conn = null;
    public java.sql.Statement stmt =null;
	
    public void run(){
    	try {
			//File file = new File("PC/IPconfig.txt");
			File file = new File("IPconfig.txt");
			String filePath = file.getCanonicalPath();
			FileInputStream in = new FileInputStream(filePath);
    		//FileInputStream in = new FileInputStream("IPconfig.txt");
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");  
            BufferedReader bufReader = new BufferedReader(inReader);  
            String line = null; 
            int writetime=0;
    		
    	    while((line = bufReader.readLine()) != null){ 
    	    	if(writetime==0){
                    ip=line;
                    writetime++;
    	    	}
    	    	else{
    	    		ip1=line;
    	    		writetime=0;
    	    	}
            }  
    		
    	    String[] values = ip.split(",");
    		
    		connet=connet1+values[0]+connet2+values[1]+connet3+values[2]+connet4+values[3]+connet5;
    	    
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		conn = DriverManager.getConnection(connet);
            stmt= conn.createStatement();
            
            String sqlfirstwork1 = "SELECT tb_live_data.fWeldTime FROM tb_live_data ORDER BY tb_live_data.fWeldTime ASC LIMIT 0,1";
            ResultSet rs1 =stmt.executeQuery(sqlfirstwork1);
        	while (rs1.next()) {
        		timework1 = rs1.getString("fWeldTime");
        	}
        	
            String[] values1 = ip1.split("to");
            
        	long datetime1 = DateTools.parse("yy-MM-dd HH:mm:ss",values1[0]).getTime();
            //System.out.println(datetime1);
            
            String sqlfirstwork2 = "SELECT tb_live_data.fWeldTime FROM tb_live_data ORDER BY tb_live_data.fWeldTime DESC LIMIT 0,1";
            ResultSet rs2 =stmt.executeQuery(sqlfirstwork2);
        	while (rs2.next()) {
        		timework2 = rs2.getString("fWeldTime");
        	}
        	
        	long datetime2 = DateTools.parse("yy-MM-dd HH:mm:ss",values1[1]).getTime();
            //System.out.println(datetime2);
        	
            for(long i=datetime1;i<=datetime2;i+=3600000){
            	
            	Date d1 = new Date(i);
            	String t1 = DateTools.format("yy-MM-dd HH:mm:ss", d1);
            	Date d2 = new Date(i+3600000);
            	String t2 = DateTools.format("yy-MM-dd HH:mm:ss", d2);
            	//System.out.println(t1);
            	//System.out.println(t2);
            	
            	String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
                		+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime) SELECT "
                		+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
                		+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
                String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                		+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime) SELECT tb_live_data.fwelder_id,"
                		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "WHERE tb_live_data.fstatus = '3' AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
                String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                		+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime) SELECT tb_live_data.fwelder_id,"
                		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "LEFT JOIN tb_welded_junction ON tb_live_data.fjunction_id = tb_welded_junction.fwelded_junction_no "
                		+ "WHERE fstatus= '3' and tb_welded_junction.fitemid = tb_live_data.fitemid and (tb_live_data.fvoltage > tb_welded_junction.fmax_valtage OR tb_live_data.felectricity > tb_welded_junction.fmax_electricity "
                		+ "OR tb_live_data.fvoltage < tb_welded_junction.fmin_valtage OR tb_live_data.felectricity < tb_welded_junction.fmin_electricity)"
                		+ " AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
                stmt.executeUpdate(sqlstandby);
                stmt.executeUpdate(sqlwork);
                stmt.executeUpdate(sqlalarm);
                
    			Thread.sleep(50);
    				
            }
            
            //System.out.println("Done");
            
    	} catch (ClassNotFoundException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	} catch (FileNotFoundException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		//e.printStackTrace();
    	}
    }
}
