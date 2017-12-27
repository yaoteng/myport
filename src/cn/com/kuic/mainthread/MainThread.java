package cn.com.kuic.mainthread;

import java.sql.ResultSet;
import java.sql.Statement;

import jdbc.myJDBC;

public class MainThread {

	public MainThread(Statement stmt1,Statement stmt2,ResultSet rs1,ResultSet rs2,myJDBC myjdbc,String nickname) {
		// TODO Auto-generated constructor stub
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				new JXLthread(stmt1,stmt2,rs1,rs2,myjdbc);
				
				
				
			}
			
			
			
			
		}.start();
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				new WXthread(nickname);
				
				
				
			}
			
			
			
			
		}.start();
	}

}
