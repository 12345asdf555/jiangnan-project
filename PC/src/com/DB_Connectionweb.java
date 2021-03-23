package com;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DB_Connectionweb {
	public String inSql;
	public String connet;
	public String datasend="";
	private ArrayList<String> listarray = new ArrayList<String>();
	
	public ArrayList<String> getId() {
		return listarray;
	}
	public void setId(String datasend) {
		this.datasend = datasend;
	}
    public Server server;

	/*String Connection="jdbc:mysql://121.196.222.216:3306/Weld?"+

            "user=root&password=123456&characterEncoding=UTF8";

    String uri = "jdbc:mysql://121.196.222.216:3306/Weld?";

    String user = "user=root&password=123456&characterEncoding=UTF8";



     String connet = "jdbc:mysql://121.196.222.216:3306/Weld?"

             + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";

    String inSql = null;*/

    public DB_Connectionweb(String connet)

    {
    	
        java.sql.Connection conn = null;

        java.sql.Statement stmt =null;


        try {  

            Class.forName("com.mysql.cj.jdbc.Driver");

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

        }



         Date date = new Date();

         String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

         Timestamp goodsC_date = Timestamp.valueOf(nowTime);



        // if(state ==1)

         //inSql = "SELECT * from ( SELECT count(num1) result1,count(num2) result2,count(num1)+count(num2) num3,eno from ( SELECT case when fstatus!=0 then 1 end num1, case when fstatus=0 then 1 end num2,m.fid, m.fequipment_no eno from tb_live_data l INNER join tb_welding_machine m on m.fid = l.fmachine_id where FWeldTime like '%"+nowTime.substring(0,10)+"%')A group by fid UNION select 0 result1,0 result2,0 num3,fequipment_no eno from tb_welding_machine where fid not in (SELECT m.fid from tb_live_data l INNER join tb_welding_machine m on m.fid = l.fmachine_id where FWeldTime like '%"+nowTime.substring(0,10)+"%') )temp";

         inSql = "SELECT * from ( SELECT count(num1) result1,count(num2) result2,count(num1)+count(num2) num3,eno from ( SELECT case when fstatus!=0 then 1 end num1, case when fstatus=0 then 1 end num2,m.fid, m.fequipment_no eno from tb_live_data l INNER join tb_welding_machine m on m.fid = l.fmachine_id where FWeldTime between '"+nowTime.substring(0,10)+" 00:00:00' and '"+nowTime.substring(0,10)+" 23:59:59')A group by fid UNION select 0 result1,0 result2,0 num3,fequipment_no eno from tb_welding_machine where fid not in (SELECT m.fid from tb_live_data l INNER join tb_welding_machine m on m.fid = l.fmachine_id where FWeldTime between '"+nowTime.substring(0,10)+" 00:00:00' and '"+nowTime.substring(0,10)+" 23:59:59') )temp";


       /*  else {

             inSql = "insert into sj(id,bed_Number,state,Dtime) values('"+ id +"','" + bedNumber + "','leave','" + goodsC_date + "')";

        }*/

         try {

        	ResultSet rs =stmt.executeQuery(inSql);
            
            while (rs.next()) {
            	String fequipment_no = rs.getString("eno");
            	String fstatus_id = rs.getString("result1");
            	String finsframework_id = rs.getString("num3");
            	listarray.add(fequipment_no);
            	listarray.add(fstatus_id);
            	listarray.add(finsframework_id);
            	
            }

        } catch (SQLException e) {

            //System.out.println("Broken insert");

            //e.printStackTrace();

        } 



        /*try {

            stmt.close();

        } catch (SQLException e) {

            //System.out.println("Broken close");

            //e.printStackTrace();

        }  
*/
         return; 



    }

    
}