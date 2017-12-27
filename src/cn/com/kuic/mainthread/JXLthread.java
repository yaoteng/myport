package cn.com.kuic.mainthread;


import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jdbc.myJDBC;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class JXLthread {
       
	
	    private String sql;
	    private int size=0;
	    private File tempFile1=null,tempFile2=null;
	    private long oldValue;
	    private long newValue;
	    private String oldEPC;
	    private String newEPC;
	    
	    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    private Date d;
	public JXLthread(Statement stmt1,Statement stmt2,ResultSet rs1,ResultSet rs2,myJDBC myjdbc) {
		// TODO Auto-generated constructor stub
		System.out.println("JXL线程开启——————");
		
		
		tempFile1=new File("d:/temp/output.xls");
		tempFile2=new File("d:/temp/output2.xls");
		if(!tempFile1.exists()){
			
			try {
				tempFile1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!tempFile2.exists()){
			
			try {
				tempFile2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 WriteBook2(stmt1,stmt2,rs1,rs2,myjdbc);
		 WriteBook1(stmt1,stmt2,rs1,rs2,myjdbc);
		
		
		
		
	}
	
	public void WriteBook1(Statement stmt1,Statement stmt2,ResultSet rs1,ResultSet rs2,myJDBC myjdbc){
		
		
		sql="select EPC_log,nickname,count(nickname)from login left join register on login.EPC_log=register.EPC_reg group by EPC_log";
		try {
			rs1=stmt1.executeQuery(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		WritableWorkbook workbook=null;
		try {
			workbook = Workbook.createWorkbook(tempFile1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WritableSheet sheet = workbook.createSheet("TestCreateExcel", 0);
		//一些临时变量，用于写到excel中
		Label l,l1 = null,l2=null;
		
		jxl.write.Number n=null;
		jxl.write.DateTime d=null;
		//预定义的一些字体和格式，同一个Excel中最好不要有太多格式
		WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, 
		false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);
		WritableCellFormat headerFormat = new WritableCellFormat (headerFont);
		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, 
		false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);
		WritableCellFormat titleFormat = new WritableCellFormat (titleFont);
		WritableFont detFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, 
		false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat detFormat = new WritableCellFormat (detFont);
		DateFormat df=new DateFormat("yyyy-MM-dd");
		//用于日期的
		WritableCellFormat dateFormat = new WritableCellFormat (detFont, df);
		//剩下的事情，就是用上面的内容和格式创建一些单元格，再加到sheet中
		
		try {
			l=new Label(0, 0, "用于测试的Excel文件",headerFormat);
			sheet.addCell(l);
			int column=0;
			l=new Label(column++, 2, "EPC标签号", titleFormat);
			sheet.addCell(l);
			l=new Label(column++, 2, "微信昵称", titleFormat);
			sheet.addCell(l);
			l=new Label(column++, 2, "登陆次数", titleFormat);
			sheet.addCell(l);
			l=new Label(column++, 2, "授权情况", titleFormat);
			sheet.addCell(l);
			
			//add detail
			try {
				
					    int i=0;
					while(rs1.next()){
						
						column=0;
						l=new Label(column++, i+3, rs1.getString("EPC_log"), detFormat);
						sheet.addCell(l);
						l=new Label(column++, i+3, rs1.getString("nickname"),detFormat);
						sheet.addCell(l);
						if(!rs1.getString("count(nickname)").equals("0")){
							l1=new Label(column++, i+3,rs1.getString("count(nickname)"), detFormat);
							l2=new Label(column++, i+3,"已授权", detFormat);
						}else{
							sql= "select EPC_log,count(EPC_log)from login where EPC_log='"+rs1.getString("EPC_log")+"'";
							rs2=stmt2.executeQuery(sql);
							while(rs2.next()){
								l1=new Label(column++, i+3,rs2.getString("count(EPC_log)"), detFormat);
								l2=new Label(column++, i+3,"未授权", detFormat);
							}
							
						}
						sheet.addCell(l1);
						sheet.addCell(l2);
						
						
						i++;
					}
					
					column=0;
					sheet.setColumnView(column++, 50);
					sheet.setColumnView(column++, 20);
					sheet.setColumnView(column++, 10);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//add Title
		
		
		try {
			myjdbc.closeResultset(rs1);
			myjdbc.closeResultset(rs2);
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void WriteBook2(Statement stmt1, Statement stmt2, ResultSet rs1, ResultSet rs2, myJDBC myjdbc) {
		
		
		
		sql="select EPC_log,nickname,time,count(nickname) from login left join register on login.EPC_log=register.EPC_reg group by time";
		try {
			rs1=stmt1.executeQuery(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		WritableWorkbook workbook=null;
		try {
			workbook = Workbook.createWorkbook(tempFile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WritableSheet sheet = workbook.createSheet("TestCreateExcel", 0);
		//一些临时变量，用于写到excel中
		Label l,l1 = null,l2=null;
		
		jxl.write.Number n=null;
	
		//预定义的一些字体和格式，同一个Excel中最好不要有太多格式
		WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, 
		false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);
		WritableCellFormat headerFormat = new WritableCellFormat (headerFont);
		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, 
		false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);
		WritableCellFormat titleFormat = new WritableCellFormat (titleFont);
		WritableFont detFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, 
		false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat detFormat = new WritableCellFormat (detFont);
		DateFormat df=new DateFormat("yyyy-MM-dd");
		//用于日期的
		WritableCellFormat dateFormat = new WritableCellFormat (detFont, df);
		//剩下的事情，就是用上面的内容和格式创建一些单元格，再加到sheet中
		
		try {
			l=new Label(0, 0, "用于测试的Excel文件",headerFormat);
			sheet.addCell(l);
			int column=0;
			l=new Label(column++, 2, "EPC标签号", titleFormat);
			sheet.addCell(l);
			l=new Label(column++, 2, "微信昵称", titleFormat);
			sheet.addCell(l);
			l=new Label(column++, 2, "登陆时间", titleFormat);
			sheet.addCell(l);
			l=new Label(column++, 2, "授权情况", titleFormat);
			sheet.addCell(l);
			
			//add detail
			try {
				
				
					    int i=0;
					while(rs1.next()){
						
						column=0;
						l=new Label(column++, i+3, rs1.getString("EPC_log"), detFormat);
						sheet.addCell(l);
						l=new Label(column++, i+3, rs1.getString("nickname"),detFormat);
						sheet.addCell(l);
						
						l1=new Label(column++, i+3,rs1.getString("time"), detFormat);
						if(!rs1.getString("count(nickname)").equals("0")){
							
							l2=new Label(column++, i+3,"已授权", detFormat);
						}else{
							
							l2=new Label(column++, i+3,"未授权", detFormat);
							
						}
						sheet.addCell(l1);
						sheet.addCell(l2);
						
						
						i++;
					}
					
					column=0;
					sheet.setColumnView(column++, 50);
					sheet.setColumnView(column++, 20);
					sheet.setColumnView(column++, 60);
				    sheet.setColumnView(column++, 20);
				   
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//add Title
		
		
		try {
			myjdbc.closeResultset(rs1);
			
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	
}
