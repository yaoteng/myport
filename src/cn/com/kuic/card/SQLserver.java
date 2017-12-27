package cn.com.kuic.card;

import java.sql.*;

import javax.swing.JOptionPane;
/*
 * �����ݿ���صĲ�����������װ����
 */

class SQLserver {

	static String url = "jdbc:mysql://localhost:3306/test";
	static String name = "root";
    static String password = "";
	Connection ct;
	Statement statement;
	String sql;
	int result;
	PreparedStatement ps;
	ResultSet rs;
	String user,pwd;
	
	/*
	 * ����������Ϊ����ģʽ��
	 * 1��˽�л����캯��
	 * 2����������
	 * 3������һ��������ȡʵ����public������
	 */
	private SQLserver()
	{
		
	}
	private static final SQLserver ss=new SQLserver();
	
	public static SQLserver getInstance()
	{
		return ss;
		
	}
	
	//���������ݿ�ķ�����װΪһ������
	public void ConnectSQL()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver"); //��������
			
			ct=DriverManager.getConnection(url,name,password); //�õ�����
			
			System.out.println("�ѳɹ��������ݿ�...");
			try {
				statement=ct.createStatement();
				
				sql="CREATE TABLE IF NOT EXISTS login (id INT PRIMARY KEY AUTO_INCREMENT , longinname VARCHAR(128) NOT NULL UNIQUE,password VARCHAR(128) NOT NULL,username VARCHAR(128),)"
						+ " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
				result=statement.executeUpdate(sql);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ע���û��ķ���
	public void UserRegis(String a,String b,String c,String d)
	{
		//���������
		try {
			ps=ct.prepareStatement("insert into users values(?,?,?,?)");
			ps.setString(1,a);
			ps.setString(2,b);
			ps.setString(3,c);
			ps.setString(4,d);
			
			//ִ��
			int i=ps.executeUpdate();
			if(i==1)
			{
				JOptionPane.showMessageDialog(null, "ע��ɹ�","��ʾ��Ϣ",JOptionPane.WARNING_MESSAGE);
			    
			}else
			{
				JOptionPane.showMessageDialog(null, "ע��ʧ��","��ʾ��Ϣ",JOptionPane.ERROR_MESSAGE);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	��¼��֤����
	public void SQLverify(String a,String b)
	{
		try {
			ps=ct.prepareStatement("select * from users where �û���=? and ����=? ");
			ps.setString(1, a);
			ps.setString(2, b);
			
			// ResultSet�����,��ҿ��԰�ResultSet���ɷ���һ�ű��еĽ����
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				user = rs.getString(1);
				pwd = rs.getString(2);
				JOptionPane.showMessageDialog(null, "��¼�ɹ�������", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
				System.out.println("�ɹ���ȡ��������û���from���ݿ�");
				System.out.println(user + "\t" + pwd + "\t");
			}else
			{
				JOptionPane.showMessageDialog(null, "�û�����������������������룡", "��ʾ��Ϣ", JOptionPane.ERROR_MESSAGE);
			    
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	//ע����֤�������ж��û����Ƿ��Ѿ�����
	public void ZhuceVerify(String a)
	{
		try {
			ps=ct.prepareStatement("select * from users where �û���=?");
//			System.out.println(ps);
			ps.setString(1, a);
			
			rs=ps.executeQuery();
			if(rs.next())
			{
				JOptionPane.showMessageDialog(null, "���û����Ѿ�����", "��ʾ��Ϣ", JOptionPane.WARNING_MESSAGE);
			}else
			{
//				����ע��
//				UI ui=new UI();
				this.UserRegis(UI.jtf1.getText(),UI.jtf2.getText(),UI.jtf3.getText(),UI.jtf4.getText());
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
