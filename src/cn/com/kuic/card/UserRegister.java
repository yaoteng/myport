package cn.com.kuic.card;
/*
 * 功能：登录界面带着注册功能，弹出注册界面。
 *    将注册的信息保存在数据库中，并且可以进行登录操作。
 * author：ywq
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserRegister extends JFrame implements ActionListener{
	
	//门面模式对象
	Facade fcd=new Facade();
	
	//定义登录界面的组件
		JButton jb1,jb3=null;
		JRadioButton jrb1,jrb2=null;
		JPanel jp1,jp2,jp3=null;
	    JTextField jtf=null;
		JLabel jlb1,jlb2=null;
		JPasswordField jpf=null;
			
	
	public static void main(String[] args)
	{
		UserRegister ur=new UserRegister();
	}
	
	public UserRegister()
	{
		//创建组件
		 //创建组件
		jb1=new JButton("登录");
		
		jb3=new JButton("退出");
		//设置监听
		jb1.addActionListener(this);
		
		jb3.addActionListener(this);
		
		jlb1=new JLabel("用户名：");
		jlb2=new JLabel("密    码：");
		
		jtf=new JTextField(10);
		jpf=new JPasswordField(10);
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		
		jp1.add(jlb1);
		jp1.add(jtf);
		
		jp2.add(jlb2);
		jp2.add(jpf);
		
		jp3.add(jb1);
	
		jp3.add(jb3);
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(new GridLayout(3,1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(300, 200, 300, 180);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//监听各个按钮
		if(e.getActionCommand()=="退出")
		{
			System.exit(0);
		}else if(e.getActionCommand()=="登录")
		{
			//进行判断，为空则不进行登录操作。
			if(jtf.getText().isEmpty()||jpf.getText().isEmpty())
				{JOptionPane.showMessageDialog(null, "请输入用户名和密码", "提示信息", JOptionPane.WARNING_MESSAGE);}
			else if(jtf.getText().equals("admin")&&jpf.getText().equals("admin")){
				this.loginadmin();
			}else{
				//调用登录方法
				this.login();
			}
			
		}
		
	}
	
	private void loginadmin() {
		// TODO Auto-generated method stub
		this.dispose();
		Loginadmin l = new Loginadmin();
	    Thread t = new Thread(l);
	    t.start();
	}

	//注册方法
     public void Regis() {
    	 
    	 
    	 this.dispose();  //关闭当前界面
    	 new UI();   //打开新界面
    	 
		
		
	}

	//登录方法
	public void login() {
		
//		SQLserver s=SQLserver.getInstance();
//		s.ConnectSQL();
//		s.SQLverify(jtf.getText(), jpf.getText());
		
		//使用门面模式
		this.dispose();
		Login l = new Login();
	    Thread t = new Thread(l);
	    t.start();
	}

}
