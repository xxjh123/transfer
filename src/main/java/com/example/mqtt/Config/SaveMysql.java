package com.example.mqtt.Config;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@Component
public class SaveMysql {
    private static String driver="com.mysql.jdbc.Driver";
    private static String url="jdbc:mysql://localhost/transfer?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private static String user="root";
    private static String password="root";
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
    public void save(String name, String subtopic, String submessage, String subdate, String sendtopic, String sendmessage, String senddate, String state) {
        String sql = "update message set subtopic = '"+subtopic+"',submessage = '"+submessage+"',subdate = '"+senddate+"',sendtopic = '"+sendtopic+"',sendmessage = '"+sendmessage+"',senddate ='"+senddate+"',state = '"+state+"' where name = '"+name+"'";
        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
            stmt=conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(stmt!=null) stmt.close();
                if(conn!=null) conn.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
