package cn.com.kuic.card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.yang.serialport.exception.NoSuchPort;
import com.yang.serialport.exception.NotASerialPort;
import com.yang.serialport.exception.PortInUse;
import com.yang.serialport.exception.SerialPortParameterFailure;
import com.yang.serialport.exception.TooManyListeners;
import com.yang.serialport.manage.SerialPortManager;
import com.yang.serialport.ui.MainFrame;
import com.yang.serialport.utils.ByteUtils;
import com.yang.serialport.utils.ShowUtils;

import cn.com.kuic.mainthread.MainThread;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import jdbc.myJDBC;
import propertyIO.Prop;

public class Loginadmin extends JFrameEx implements Runnable {

	private SerialPort serialport;
	private static Loginadmin login;
	private JTabbedPane tabbedpane = new JTabbedPane();
	private JPanel pan1, pan2, pan4, pan5, pan6;
	private JPanelExt pan3, pan7;
	private JLabel jl1, jl2, jl3, jl4, jl5, jl6;
	private JLabel serialPortLabel = new JLabel("\u4e32\u53e3");
	private JLabel portMessageLabel;
	private JButton jb1, jb2, jb3;
	private JTextField jtx1, jtx2;
	private int portnumber = 0;
	private JTextField portXTextField = new JTextField();
	private JTextField portYTextField = new JTextField();
	private JTextField portThetaField = new JTextField();
	private JTextField portednum = new JTextField();
	private JTextField controlernum = new JTextField();
	private JTextField detecternum = new JTextField();
	private List<String> commList = null;
	private StringBuffer sb = new StringBuffer();
	private String subs = "", subscontain = "", regionNo = "", totalNo = "", leftNo = "", hexstringdata = "",
			hexstringatline = "", binarystringdata = "", binarystringatline = "", binarystringdatatobyte = "",
			binarystringatlinetobyte = "";
	private JComboBox commChoice = new JComboBox();
	private int mycontrolernum = 0, mydetecternum = 0;

	/*
	 * private JButton btnConnect = new JButton("\u8FDE\u63A5"); private JButton
	 * btnDisconnect = new JButton("\u65AD\u5F00"); private JLabel
	 * deviceTypeLabel = new JLabel("\u8BBE\u5907\u7C7B\u578B"); private JButton
	 * buttonReadEPC = new JButton("\u5237\u5361"); private JButton
	 * buttonStopRead=new JButton("\u505c\u6b62\u5237\u5361"); private JButton
	 * buttonCleanlogin=new JButton("\u6e05\u7a7a\u767b\u8bb0\u8868");
	 */
	private JButton btnsetPosition = new JButton("\u8bbe\u7f6e\u505c\u8f66\u4f4d");
	private JButton btncancelPosition=new JButton("\u5220\u9664\u505c\u8f66\u4f4d");
	private JButton btndeleteAllPosition=new JButton("\u5220\u9664\u6240\u6709\u70b9\u4f4d");
	private JButton btncararrived = new JButton("\u8f66\u5df2\u5165\u4f4d");
	private JButton btncarout = new JButton("\u8f66\u5df2\u51fa\u4f4d");
	private JButton btnFileChooser = new JButton("\u8bf7\u9009\u62e9\u5730\u56fe\u6587\u4ef6");
	private JButton serialPortOperate = new JButton("\u6253\u5f00\u4e32\u53e3");
	private JButton mySerialPortconfig = new JButton("\u914d\u7f6e\u4e32\u53e3\u4fe1\u606f");
	private boolean readingEPCData = false;
	private SortTable tableEPCData = null;
	private myJDBC myjdbc = myJDBC.getInitJDBCUtil();
	private Connection con = null;
	private Statement statement = null, stmt1 = null, stmt2 = null,stmt3=null;
	private String sql ,sql2= null;
	private String EPCStr;
	private String nickname;
	private int result = 0;
	private ResultSet rs, rs1, rs2,rs3 = null;
	private Date time;
	private String timestr;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private boolean isread = false;
	private JTextField portOutnum = new JTextField();
	private int screenWidth = 0;
	private int screenHeight;
	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();

	public Loginadmin() {

		super("Card Demo");

		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		this.setBounds(0, 0, screenWidth, screenHeight);
		setTitle("车位引导系统--奎创科技");
		JFrameEx.InitGlobalFont(new Font("宋体", Font.PLAIN, 14));
		getContentPane().setLayout(null);

		Container c = getContentPane();

		tabbedpane.setBounds(0, 0, screenWidth, screenHeight);

		portMessageLabel = new JLabel();
		pan1 = new JPanel();
		pan1.setBounds(0, 0, screenWidth, screenHeight);
		pan2 = new JPanel();
		pan3 = new JPanelExt("nomap.jpg", screenWidth, screenHeight);
		pan4 = new JPanel();
		pan5 = new JPanel();
		pan6 = new JPanel();
		pan7 = new JPanelExt("nomap.jpg", screenWidth, screenHeight);
		pan3.setLayout(null);
		pan7.setLayout(null);
		pan2.setLayout(null);

		con = myjdbc.getConnection();
		if (con != null) {
			showMessageBox("连接数据库成功");
			try {
				statement = con.createStatement();
				stmt1 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				stmt3 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				sql = "CREATE TABLE IF NOT EXISTS carport (id INT PRIMARY KEY AUTO_INCREMENT , port_id INT NOT NULL UNIQUE,portx INT,porty INT,theta DOUBLE)"
						+ " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
				result = statement.executeUpdate(sql);

				sql = "CREATE TABLE IF NOT EXISTS mycarport (id INT PRIMARY KEY AUTO_INCREMENT , car_id VARCHAR(128),port_id INT NOT NULL UNIQUE)"
						+ " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
				result = statement.executeUpdate(sql);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block

			}
		} else {
			showMessageBox("连接数据库失败");
		}

		File f = new File("D:\\myProperty.properties");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Prop prop = new Prop(f, "utf-8");
		String mapPath = prop.get("mapPath");
		if (mapPath != null) {
			pan3.setImgpath(mapPath);
			pan7.setImgpath(mapPath);
		}

		// pan4.add(jl3);

		pan5.setLayout(null);
		JLabel portLableX = new JLabel("\u4f4d\u7f6e\uff1a\u0058\u003a");
		portLableX.setBounds(10, 10, 80, 25);
		pan5.add(portLableX);

		portXTextField.setBounds(80, 12, 66, 21);
		pan5.add(portXTextField);
		portXTextField.setColumns(30);
		portXTextField.setText("0");

		JLabel portLableY = new JLabel("\u0059\u003a");
		portLableY.setBounds(148, 10, 58, 25);
		pan5.add(portLableY);

		portYTextField.setBounds(180, 12, 66, 21);
		pan5.add(portYTextField);
		portYTextField.setColumns(30);
		portYTextField.setText("0");

		JLabel portTheta = new JLabel("\u89d2\u5ea6");
		portTheta.setBounds(300, 10, 58, 25);
		pan5.add(portTheta);

		portThetaField.setBounds(340, 12, 66, 21);
		pan5.add(portThetaField);
		portThetaField.setColumns(30);
		portThetaField.setText("0");

		JLabel controlernumber = new JLabel("\u63a7\u5236\u5668\u53f7\u7801");
		controlernumber.setBounds(420, 12, 108, 25);
		pan5.add(controlernumber);

		controlernum.setBounds(528, 12, 66, 21);
		pan5.add(controlernum);
		controlernum.setColumns(10);
		controlernum.setText("0");

		JLabel detecternumber = new JLabel("\u63a2\u6d4b\u5668\u53f7\u7801");
		detecternumber.setBounds(598, 12, 108, 25);
		pan5.add(detecternumber);

		detecternum.setBounds(706, 12, 66, 21);
		pan5.add(detecternum);
		detecternum.setColumns(10);
		detecternum.setText("0");
		btnsetPosition.setBounds(800, 12, 100, 21);
		btnsetPosition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				int controlernumberfield = Integer.parseInt(controlernum.getText());
				int detecternumberfield = Integer.parseInt(detecternum.getText());

				String contlnum = new DecimalFormat("00").format(controlernumberfield);
				String denum = new DecimalFormat("00").format(detecternumberfield);

				/*
				 * if((portnumber<37)||((portnumber>64)&&(portnumber<75))){ URL
				 * url= this.getClass().getResource("vip.jpg"); ImageIcon icon=
				 * new ImageIcon(url);
				 * icon.setImage(icon.getImage().getScaledInstance(screenSize.
				 * width/80,screenSize.height/80, Image.SCALE_DEFAULT));
				 * port.setIcon(icon); port.setText(""); }
				 */

				if (controlernumberfield != 0 && detecternumberfield != 0) {
					if (controlernumberfield == mycontrolernum && detecternumberfield == mydetecternum) {
						ShowUtils.errorMessage("输入号码不能重复");
					} else {
						int x = Integer.parseInt(portXTextField.getText());
						int y = Integer.parseInt(portYTextField.getText());
						double theta = Double.parseDouble(portThetaField.getText());
						JLabelExt port = new JLabelExt(contlnum + denum, theta);
						port.setFont(new Font("宋体", Font.PLAIN, 20));
						port.setBounds(x, y, 100, 100);
						myListener m = new myListener();
						port.addMouseMotionListener(m);
						port.addMouseListener(m);

						
						sql = "Select port_id from mycarport where car_id='" + contlnum + denum + "'";
						try {
							rs2 = stmt3.executeQuery(sql);
							
							if (!rs2.next()) {
							    sql="Select port_id from carport";
							    rs3=stmt3.executeQuery(sql);
							    while(rs3.next()){
							    	int port_id= rs3.getInt("port_id");
							    	
							    	if(portnumber==port_id){
							    		portnumber++;
							    	}
							    }
								sql2 = "Insert into mycarport(car_id,port_id) values ('" + contlnum + denum + "',"
										+ portnumber + ")";
								System.out.println(sql2);
								result = statement.executeUpdate(sql2);
								pan3.add(port,portnumber);
								
							} 
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					ShowUtils.errorMessage("输入的号码不能是零");
				}

				/*
				 * String carid= portnumber+""; int x1=port.getBounds().x; int
				 * y1=port.getBounds().y; try {
				 * 
				 * sql="Insert into carport(car_id,portx,porty) values ('"+carid
				 * +"',"+x1+","+y1+")"; System.out.println(sql); result=
				 * statement.executeUpdate(sql);
				 * 
				 * } catch (SQLException e1) { // TODO Auto-generated catch
				 * block sql="Update carport set portx="+x1+", porty="
				 * +y1+" where car_id='"+carid+"'"; try {
				 * result=statement.executeUpdate(sql); } catch (SQLException
				 * e2) { // TODO Auto-generated catch block
				 * e2.printStackTrace(); } }
				 */

				mycontrolernum = controlernumberfield;
				mydetecternum = detecternumberfield;
				Loginadmin.this.invalidate();
				Loginadmin.this.repaint();
				
			}
		});
		pan5.add(btnsetPosition);

		btncancelPosition.setBounds(940, 12, 100, 21);
		btncancelPosition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				int controlernumberfield = Integer.parseInt(controlernum.getText());
				int detecternumberfield = Integer.parseInt(detecternum.getText());

				String contlnum = new DecimalFormat("00").format(controlernumberfield);
				String denum = new DecimalFormat("00").format(detecternumberfield);

				/*
				 * if((portnumber<37)||((portnumber>64)&&(portnumber<75))){ URL
				 * url= this.getClass().getResource("vip.jpg"); ImageIcon icon=
				 * new ImageIcon(url);
				 * icon.setImage(icon.getImage().getScaledInstance(screenSize.
				 * width/80,screenSize.height/80, Image.SCALE_DEFAULT));
				 * port.setIcon(icon); port.setText(""); }
				 */

				if (controlernumberfield != 0 && detecternumberfield != 0) {
					
						/*int x = Integer.parseInt(portXTextField.getText());
						int y = Integer.parseInt(portYTextField.getText());
						double theta = Double.parseDouble(portThetaField.getText());
						JLabelExt port = new JLabelExt(contlnum + denum, theta);
						port.setFont(new Font("宋体", Font.PLAIN, 20));
						port.setBounds(x, y, 100, 100);
						

						pan3.add(port);*/
						
						sql= "Select port_id from mycarport where car_id='"+contlnum+denum+"'";
						
						try {
							rs3=stmt3.executeQuery(sql);
							if(rs3.next()){
								int port_id=rs3.getInt("port_id");
						
								JLabelExt port=(JLabelExt)pan3.getComponent(port_id);
								port.setText("");
								port.setIcon(null);
								pan3.remove(port_id);
								pan3.add(port,port_id);
								pan3.repaint();
							}
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						sql = "DELETE from mycarport where car_id='" + contlnum+ denum+"'";
						try {
							result= statement.executeUpdate(sql);
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					
				} else {
					ShowUtils.errorMessage("输入的号码不能是零");
				}

				/*
				 * String carid= portnumber+""; int x1=port.getBounds().x; int
				 * y1=port.getBounds().y; try {
				 * 
				 * sql="Insert into carport(car_id,portx,porty) values ('"+carid
				 * +"',"+x1+","+y1+")"; System.out.println(sql); result=
				 * statement.executeUpdate(sql);
				 * 
				 * } catch (SQLException e1) { // TODO Auto-generated catch
				 * block sql="Update carport set portx="+x1+", porty="
				 * +y1+" where car_id='"+carid+"'"; try {
				 * result=statement.executeUpdate(sql); } catch (SQLException
				 * e2) { // TODO Auto-generated catch block
				 * e2.printStackTrace(); } }
				 */

				mycontrolernum = controlernumberfield;
				mydetecternum = detecternumberfield;
				Loginadmin.this.invalidate();
				Loginadmin.this.repaint();
				
			}
		});
		pan5.add(btncancelPosition);
	
		mySerialPortconfig.setBounds(10, 42, 200, 21);
		mySerialPortconfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new MainFrame().setVisible(true);
			}
		});
		pan5.add(mySerialPortconfig);
		
		btndeleteAllPosition.setBounds(230, 42, 200, 21);
		btndeleteAllPosition.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sql = "Drop table carport";
				sql2="Drop table mycarport";
				try {
					result=statement.executeUpdate(sql);
					result=statement.executeUpdate(sql2);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				pan3.removeAll();
				pan7.removeAll();
				pan3.repaint();
				pan7.repaint();
				sql = "CREATE TABLE IF NOT EXISTS carport (id INT PRIMARY KEY AUTO_INCREMENT , port_id INT NOT NULL UNIQUE,portx INT,porty INT,theta DOUBLE)"
						+ " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
				try {
					result = statement.executeUpdate(sql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				sql = "CREATE TABLE IF NOT EXISTS mycarport (id INT PRIMARY KEY AUTO_INCREMENT , car_id VARCHAR(128),port_id INT NOT NULL UNIQUE)"
						+ " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
				try {
					result = statement.executeUpdate(sql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
        pan5.add(btndeleteAllPosition);
		/*
		 * JLabel ported = new JLabel("\u8f66\u4f4d"); ported.setBounds(10, 42,
		 * 58, 25); pan5.add(ported);
		 * 
		 * portednum.setBounds(80,42,66,21); pan5.add(portednum);
		 * portednum.setColumns(30); portednum.setText("0");
		 * 
		 * btncararrived.setBounds(148, 42, 100, 21);
		 * btncararrived.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO
		 * Auto-generated method stub int n =
		 * Integer.parseInt(portednum.getText()); JLabel port
		 * =(JLabel)pan3.getComponent(n); ImageIcon icon= new
		 * ImageIcon("carport.jpg");
		 * icon.setImage(icon.getImage().getScaledInstance(30,20,
		 * Image.SCALE_DEFAULT)); port.setIcon(icon); port.setText("");
		 * pan3.remove(n);
		 * 
		 * pan3.add(port, n); pan7.remove(n); pan7.add(port, n); } });
		 * pan5.add(btncararrived);
		 * 
		 * JLabel portOut = new JLabel("\u7a7a\u51fa\u8f66\u4f4d");
		 * portOut.setBounds(300, 42, 58, 25); pan5.add(portOut);
		 * 
		 * portOutnum.setBounds(380,42,66,21); pan5.add(portOutnum);
		 * portOutnum.setColumns(30); portOutnum.setText("0");
		 * 
		 * btncarout.setBounds(448, 42, 100, 21);
		 * btncarout.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO
		 * Auto-generated method stub int n =
		 * Integer.parseInt(portednum.getText()); JLabel port
		 * =(JLabel)pan3.getComponent(n); ImageIcon icon= new ImageIcon();
		 * port.setIcon(icon); port.setText("<html>\u8f66<br>\u4f4d<br>"+n);
		 * pan3.remove(n); pan3.add(port, n); pan7.remove(n); pan7.add(port, n);
		 * } }); pan5.add(btncarout);
		 */
		btnFileChooser.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "选择");
				File f = new File("D:\\myProperty.properties");
				if (!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					showMessageBox("请选择jpg文件");
				} else if (file.isFile()) {
					Loginadmin.this.update_properies("mapPath", file.getAbsolutePath(), "D:\\myProperty.properties");
					pan3.setImgpath(file.getPath());
					pan7.setImgpath(file.getPath());
					Loginadmin.this.repaint();
				}

			}
		});
		btnFileChooser.setBounds(10, 80, 170, 23);

		pan5.add(btnFileChooser);
		
		

		portMessageLabel.setBounds(0, 100, screenWidth / 8, screenHeight / 8);
		portMessageLabel.setText("\u6b22\u8fce\u5149\u4e34");
		pan2.add(portMessageLabel);
		/*
		 * serialPortLabel.setForeground(Color.gray);
		 * serialPortLabel.setBounds(10, 25, 40, 20); pan2.add(serialPortLabel);
		 * 
		 * commChoice.setFocusable(false); commChoice.setBounds(40, 25, 100,
		 * 20); pan2.add(commChoice);
		 * 
		 * serialPortOperate.setFocusable(false);
		 * serialPortOperate.setBounds(180, 25, 90, 20);
		 * pan2.add(serialPortOperate);
		 * 
		 * portMessageLabel.setBounds(20, 55, 300, 20);
		 * pan2.add(portMessageLabel);
		 */
		/*
		 * baudrateComboBox.setModel(new DefaultComboBoxModel<String>(new
		 * String[] {"9600", "19200", "57600", "115200"}));
		 * baudrateComboBox.setSelectedIndex(3); baudrateComboBox.setBounds(206,
		 * 12, 86, 21); pan5.add(baudrateComboBox);
		 * 
		 * btnConnect.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { int []boudrates={}; int selIndex =
		 * baudrateComboBox.getSelectedIndex(); if(selIndex>=0 &&
		 * selIndex<boudrates.length) {
		 * if(!mReader.Connect(portTextField.getText(),
		 * boudrates[selIndex]))//ע�Ⲩ����Ϊ115200�����������޷��򿪶˿� {
		 * showMessageBox("连接串口失败，请确认串口号以及波特率",true); return; }
		 * btnConnect.setEnabled(false); jl3.setText("请刷卡登记");
		 * con=myjdbc.getConnection(); try { statement=con.createStatement();
		 * stmt1=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet
		 * .CONCUR_UPDATABLE);
		 * stmt2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet
		 * .CONCUR_UPDATABLE);
		 * sql="CREATE TABLE IF NOT EXISTS login (id INT PRIMARY KEY AUTO_INCREMENT , EPC_log VARCHAR(128) NOT NULL UNIQUE,time VARCHAR(128) NOT NULL)"
		 * + " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
		 * result=statement.executeUpdate(sql); } catch (SQLException e2) { //
		 * TODO Auto-generated catch block
		 * 
		 * 
		 * } btnDisconnect.setEnabled(true); //getDeviceInfo(); } } });
		 * btnConnect.setBounds(302, 11, 66, 23); pan5.add(btnConnect);
		 * btnDisconnect.setEnabled(false); btnDisconnect.addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) {
		 * //mReader.Disconnect();//�Ͽ����� btnConnect.setEnabled(true);
		 * jl3.setText("请连接读卡器"); myjdbc.closeConnection(rs1, statement, con);
		 * myjdbc.closeConnection(rs2, stmt1, con); myjdbc.closeConnection(rs2,
		 * stmt2, con); btnDisconnect.setEnabled(false); } });
		 * btnDisconnect.setBounds(379, 11, 66, 23); pan5.add(btnDisconnect);
		 * 
		 * deviceTypeLabel.setBounds(555, 10, 156, 25);
		 * pan5.add(deviceTypeLabel);
		 * 
		 * 
		 * 
		 * 
		 * buttonReadEPC.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { //��ȡ��ǩ if(!mReader.isConnect()) {
		 * showMessageBox("请先连接串口"); return; }
		 * 
		 * isread=true;
		 * 
		 * } }); buttonReadEPC.setBounds(10, 50, 107, 23);
		 * pan5.add(buttonReadEPC);
		 * 
		 * 
		 * buttonStopRead.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { //��ȡ��ǩ
		 * 
		 * isread=false; buttonReadEPC.setText("\u5237\u5361"); } });
		 * buttonStopRead.setBounds(150, 50, 107, 23); pan5.add(buttonStopRead);
		 * 
		 * buttonCleanlogin.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { //��ȡ��ǩ
		 * 
		 * try { sql="DROP TABLE login"; statement.executeUpdate(sql);
		 * 
		 * sql="CREATE TABLE IF NOT EXISTS login (id INT PRIMARY KEY AUTO_INCREMENT , EPC_log VARCHAR(128) NOT NULL UNIQUE,time VARCHAR(128) NOT NULL)"
		 * + " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
		 * result=statement.executeUpdate(sql); } catch (SQLException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); } } });
		 * buttonCleanlogin.setBounds(300, 50, 107, 23);
		 * pan5.add(buttonCleanlogin);
		 */

		/*
		 * jb1.addActionListener(new ActionListener() {
		 * 
		 * 
		 * public void actionPerformed(ActionEvent e) { // TODO Auto-generated
		 * method stub
		 * 
		 * if (con!=null){ try {
		 * 
		 * sql="create table if not exists register (id int primary key auto_increment , nickname varchar(128) not null,EPC varchar(128) not null)"
		 * + " ENGINE=InnoDB DEFAULT CHARSET=utf8 ";
		 * result=statement.executeUpdate(sql); EPCStr=jtx1.getText();
		 * nickname=jtx2.getText();
		 * 
		 * if (result!=-1){ showMessageBox("数据库创建成功");
		 * sql="insert into register(nickname,EPC) values('"+nickname+"','"+
		 * EPCStr+"')"; result=statement.executeUpdate(sql);
		 * showMessageBox("数据存储成功"+nickname); jtx1.setText("");
		 * jtx2.setText(""); int row=-1; int existRowIndex = -1; for (int orgRow
		 * = 0; orgRow < tableEPCData.getRowCount(); orgRow++) {
		 * if(tableEPCData.GetString(orgRow,
		 * COL_DATA.COL_DATA_EPC.ordinal()).compareTo(EPCStr) == 0) {
		 * existRowIndex=orgRow; break; } } if(existRowIndex>=0)
		 * row=existRowIndex; else row = tableEPCData.AddNew();
		 * sql="select EPC,nickname from register";
		 * rs=statement.executeQuery(sql); while(rs.next()){
		 * tableEPCData.SetString(row, COL_DATA.COL_DATA_EPC.ordinal(),
		 * rs.getString(1)); tableEPCData.SetString(row,
		 * COL_DATA.COL_DATA_NICKNAME.ordinal(), rs.getString(2));
		 * 
		 * }
		 * 
		 * 
		 * }
		 * 
		 * } catch (Exception e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 * 
		 * } } });
		 */

		/*try {
			sql = "SELECT car_id,theta,portx,porty From carport,mycarport where carport.port_id=mycarport.port_id";
			rs = statement.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				String car_id = rs.getString("car_id");
				
				//System.out.println();
				double theta = rs.getDouble("theta");
				JLabelExt port1 = new JLabelExt(new DecimalFormat("0000").format(Integer.parseInt(car_id)), theta);
				int x1 = rs.getInt("portx");
				int y1 = rs.getInt("porty");
				port1.setBounds(x1, y1, screenWidth / 8, screenHeight / 8);
				port1.setFont(new Font("宋体", Font.PLAIN, 20));

				myListener m = new myListener();
				port1.addMouseMotionListener(m);
				port1.addMouseListener(m);

				
				pan3.add(port1, i);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		try {
			sql = "SELECT theta,portx,porty From carport";
			rs = statement.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				/*String car_id = rs.getString("car_id");*/
				double theta = rs.getDouble("theta");
				// System.out.println();
				
				JLabelExt port1 = new JLabelExt("", theta);
				int x1 = rs.getInt("portx");
				int y1 = rs.getInt("porty");
				port1.setBounds(x1, y1, screenWidth / 8, screenHeight / 8);
				port1.setFont(new Font("宋体", Font.PLAIN, 20));
                sql= "SELECT car_id from mycarport where port_id="+i+"";
                rs2=stmt2.executeQuery(sql);
                if(rs2.next()){
                	String car_id = rs2.getString("car_id");
                	port1.setText(car_id);
                }
                myListener m = new myListener();
        		port1.addMouseMotionListener(m);
        		port1.addMouseListener(m);
                
				pan3.add(port1, i);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		try {
			sql = "SELECT theta,portx,porty From carport";
			rs = statement.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				/*String car_id = rs.getString("car_id");*/
				double theta = rs.getDouble("theta");
				// System.out.println();
				
				JLabelExt port1 = new JLabelExt("", theta);
				int x1 = rs.getInt("portx");
				int y1 = rs.getInt("porty");
				port1.setBounds(x1, y1, screenWidth / 8, screenHeight / 8);
				port1.setFont(new Font("宋体", Font.PLAIN, 20));
                sql= "SELECT car_id from mycarport where port_id="+i+"";
                rs2=stmt2.executeQuery(sql);
                if(rs2.next()){
                	String car_id = rs2.getString("car_id");
                	port1.setText(car_id);
                }
                
                
				pan7.add(port1, i);
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pan1.setLayout(null);
		pan6.setLayout(null);
		pan5.setBounds(0, 0, screenWidth, screenHeight / 8);
		pan3.setBounds(0, screenHeight / 8, screenWidth, screenHeight / 8 * 6);
		pan4.setBounds(0, screenHeight / 8 * 7, screenWidth, screenHeight / 8 * 2);
		pan7.setBounds(0, screenHeight / 8, screenWidth, screenHeight / 8 * 6);
		pan2.setBounds(0, 0, screenWidth, screenHeight / 8);
		pan1.add(pan5);
		pan1.add(pan3);
		pan1.add(pan4);

		pan6.add(pan7);
		pan6.add(pan2);

		/*
		 * tableEPCData=new SortTable(); tableEPCData.AppendColumns(new
		 * String[]{"EPC标签号","微信昵称"}); tableEPCData.offsetColumnWidths(new
		 * int[]{200,150});
		 */

		/*
		 * tableEPCData.getSelectionModel().addListSelectionListener(new
		 * ListSelectionListener() {
		 * 
		 * @Override public void valueChanged(ListSelectionEvent e) {
		 * if(!e.getValueIsAdjusting()){ selectLabel(); } } });
		 */

		/*
		 * JScrollPane tableEPCScroll = new JScrollPane(tableEPCData);
		 * tableEPCScroll.setBounds(10, 88, 647, 300); pan2.add(tableEPCScroll);
		 */
		tabbedpane.addTab("\u9884\u89c8", null, pan6, "First panel");
		tabbedpane.addTab("\u8bbe\u7f6e", null, pan1, "Second panel");

		c.add(tabbedpane);
		c.setBackground(Color.WHITE);
		this.openSerialPort();
		// 设置关闭时什么也不做
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// 监听关闭按钮的点击操作
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				closeSerialPort();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		this.setVisible(true);

	}

	private boolean selectLabel() {
		int rowIndex = tableEPCData.getSelectedRow();
		if (rowIndex < 0) {
			showMessageBox("请先选中表格中的一行标签记录");
			return false;
		}
		String EPC = tableEPCData.GetString(rowIndex, COL_DATA.COL_DATA_EPC.ordinal());
		/*
		 * if(!mReader.AddFilter(0,0,1,32,32,0,EPC.toString()))//ʹ����������ı�ǩ�
		 * ��ݣ�����ѡ�� { showMessageBox("标签选择失败"); return false; }
		 */
		return true;
	}

	/*
	 * private void getDeviceInfo() { //String info=mReader.GetVersion();
	 * deviceTypeLabel.setText(info); }
	 */

	enum COL_DATA {
		COL_DATA_EPC, COL_DATA_NICKNAME
	}

	@Override
	public void run() {
		while (true) {

			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < pan3.getComponentCount(); i++) {
				JLabelExt port = (JLabelExt) pan3.getComponent(i);
				int x1 = port.getBounds().x;
				int y1 = port.getBounds().y;
				double theta = port.getTheta();
				sql = "Select portx,porty,theta from carport where port_id=" + i + "";
				try {
					rs = statement.executeQuery(sql);
					if (rs.next()) {
						sql = "Update carport set portx=" + x1 + ", porty=" + y1 + ",theta=" + theta
								+ " where port_id=" + i + "";
						try {
							result = statement.executeUpdate(sql);
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					} else {

						sql = "Insert into carport(port_id,portx,porty,theta) values ('" + i + "'," + x1 + "," + y1
								+ "," + theta + ")";
						System.out.println(sql);
						result = statement.executeUpdate(sql);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sql = "Select car_id from mycarport where port_id=" + i + "";
				try {
					rs2 = stmt2.executeQuery(sql);
					if(rs2.next()){
						String car_id = rs2.getString("car_id");
						JLabelExt port1 = new JLabelExt(new DecimalFormat("0000").format(Integer.parseInt(car_id)), theta);
						port1.setFont(new Font("宋体", Font.PLAIN, 20));
						port1.setBounds(x1, y1, 100, 100);

						if (pan7.getComponentCount() != 0) {

							try {
								pan7.remove(i);
							} catch (ArrayIndexOutOfBoundsException e) {

							}

						}
						pan7.add(port1, i);
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

		// TODO Auto-generated method stub
		/*
		 * while(true){
		 * 
		 * try { Thread.sleep(1000); } catch (InterruptedException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * 
		 * if(!readingEPCData&&isread) { int readCount = 0;
		 * 
		 * if(readCount<=0) { readCount=1;
		 * 
		 * }
		 * 
		 * //���̶߳�ȡģʽ if(readCount==1) { new Thread(){ public void run() {
		 * readingEPCData=true; buttonReadEPC.setText("停止读取");
		 * 
		 * int readCount = 0;
		 * 
		 * if(readCount<=0) { readCount=1;
		 * 
		 * } int row = -1; for (int i = 0; i < readCount && readingEPCData ;
		 * i++) { StringBuffer PC=new StringBuffer(); StringBuffer EPC=new
		 * StringBuffer(); StringBuffer RSSI=new StringBuffer();
		 * if(mReader.InventorySingleTag(PC, EPC, RSSI)) { EPCStr =
		 * EPC.toString(); time=new Date(); timestr=sdf.format(time); File
		 * file=new File("e:/icon/"+EPCStr+".jpg"); if(file.exists()){
		 * jl2.setIcon(new ImageIcon("e:/icon/"+EPCStr+".jpg")); } else {
		 * jl2.setIcon(new ImageIcon("e:/e.jpg")); }
		 * sql="INSERT INTO login (EPC_log,time) VALUES ('"+EPCStr+"','"+timestr
		 * +"')"; try { result=statement.executeUpdate(sql); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * 
		 * }
		 * 
		 * sql="SELECT nickname From register WHERE EPC_reg='"+EPCStr+"'";
		 * 
		 * try { rs=statement.executeQuery(sql); while(rs.next()){ String
		 * nickname=rs.getString("nickname");
		 * showMessageBox("nickname="+nickname);
		 * 
		 * new MainThread(stmt1,stmt2,rs1,rs2,myjdbc,nickname); } } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * 
		 * // if(i%100==0){ // tableEPCData.invalidate(); // } } else {
		 * if(readCount==1) { showMessageBox("读取标签出错"); break; } else{ if(i>1)
		 * i--; if(readingEPCData) continue; } } }
		 * 
		 * 
		 * readingEPCData=false; buttonReadEPC.setText("读取标签"); } }.start();
		 * }//���̶߳�ȡģʽEND else { //˫�̶߳�ȡģʽ mReader.ReadInventory();//�������ٶ�
		 * new Thread(){ public void run() { readingEPCData=true;
		 * buttonReadEPC.setText("停止读取");
		 * 
		 * int readCount = 0;
		 * 
		 * if(readCount<=0) { readCount=1;
		 * 
		 * } int row = -1; for (int i = 0; i < readCount && readingEPCData ;
		 * i++) { StringBuffer PC=new StringBuffer(); StringBuffer EPC=new
		 * StringBuffer(); StringBuffer RSSI=new StringBuffer();
		 * 
		 * if(mReader.RecvData(PC, EPC, RSSI)) { EPCStr = EPC.toString();
		 * 
		 * time=new Date(); timestr=sdf.format(time);
		 * 
		 * File file=new File("e:/icon/"+EPCStr+".jpg"); if(file.exists()){
		 * jl2.setIcon(new ImageIcon("e:/icon/"+EPCStr+".jpg")); } else {
		 * jl2.setIcon(new ImageIcon("e:/e.jpg")); }
		 * sql="INSERT INTO login (EPC_log,time) VALUES ('"+EPCStr+"','"+timestr
		 * +"')"; try { result=statement.executeUpdate(sql); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * 
		 * }
		 * 
		 * sql="SELECT nickname From register where EPC_reg='"+EPCStr+"'";
		 * 
		 * try { rs=statement.executeQuery(sql); while(rs.next()){ String
		 * nickname=rs.getString("nickname"); new
		 * MainThread(stmt1,stmt2,rs1,rs2,myjdbc,nickname); } } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * // if(i%100==0){ // tableEPCData.invalidate(); // } } else {
		 * if(readCount==1) { showMessageBox("读取标签出错"); break; } else{ if(i>1)
		 * i--; if(readingEPCData) continue; } } }
		 * 
		 * mReader.StopOperation();//ע�������һ��Ҫ�ǵ�ֹͣ���� readingEPCData=false;
		 * buttonReadEPC.setText("读取标签"); } }.start(); }//˫�߳�ģʽEND } else {
		 * readingEPCData=false;
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 */
	}

	public static void update_properies(String key, String value, String path) {

		File file = new File(path);
		Properties prop = new Properties();
		InputStream inputFile = null;
		OutputStream outputFile = null;
		try {
			inputFile = new FileInputStream(file);
			prop.load(inputFile);
			// inputFile.close();//一定要在修改值之前关闭inputFile
			outputFile = new FileOutputStream(file);
			// 设值-保存
			prop.setProperty(key, value);
			// 添加注释
			prop.store(outputFile, "Update '" + key + "'+ '" + value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != outputFile) {
					outputFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (null != inputFile) {
					inputFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void initData() {
		commList = SerialPortManager.findPort();
		// 妫�鏌ユ槸鍚︽湁鍙敤涓插彛锛屾湁鍒欏姞鍏ラ�夐」涓�
		if (commList == null || commList.size() < 1) {
			ShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			for (String s : commList) {
				commChoice.addItem(s);
			}
		}

	}

	/**
	 * 鎵撳紑涓插彛
	 * 
	 * @param evt
	 *            鐐瑰嚮浜嬩欢
	 */
	private void openSerialPort() {
		File f = new File("D:\\myProperty.properties");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		Prop prop = new Prop(f, "utf-8");

		String commName = prop.get("commName");
		// 鑾峰彇娉㈢壒鐜�
		int baudrate = 4800;
		if (prop.getInt("bps") != null)
			baudrate = prop.getInt("bps");

		// 妫�鏌ヤ覆鍙ｅ悕绉版槸鍚﹁幏鍙栨纭�
		if (commName == null || commName.equals("")) {
			ShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			try {
				serialport = SerialPortManager.openPort(commName, baudrate);
				if (serialport != null) {

					serialPortOperate.setText("\u5173\u95ed\u4e32\u53e3");
					ShowUtils.warningMessage("串口已打开");
				}
			} catch (SerialPortParameterFailure e) {
				ShowUtils.warningMessage("串口调用失败！");
			} catch (NotASerialPort e) {
				ShowUtils.warningMessage("打开非串口！");
			} catch (NoSuchPort e) {
				ShowUtils.warningMessage("无此串口！");
			} catch (PortInUse e) {
				e.printStackTrace();
				ShowUtils.warningMessage("串口已被占用！");
			}
		}

		try {
			SerialPortManager.addListener(serialport, new SerialListener());
		} catch (TooManyListeners e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			ShowUtils.warningMessage("串口监听失败！");
		}
	}

	/**
	 * 鍏抽棴涓插彛
	 * 
	 * @param evt
	 *            鐐瑰嚮浜嬩欢
	 */
	private void closeSerialPort() {
		SerialPortManager.closePort(serialport);
	}

	private class SerialListener implements SerialPortEventListener {
		/**
		 * 澶勭悊鐩戞帶鍒扮殑涓插彛浜嬩欢
		 */
		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {

			case SerialPortEvent.BI: // 10 閫氳涓柇
				ShowUtils.errorMessage("与串口设备通讯中断");
				break;

			case SerialPortEvent.OE: // 7 婧綅锛堟孩鍑猴級閿欒

			case SerialPortEvent.FE: // 9 甯ч敊璇�

			case SerialPortEvent.PE: // 8 濂囧伓鏍￠獙閿欒

			case SerialPortEvent.CD: // 6 杞芥尝妫�娴�

			case SerialPortEvent.CTS: // 3 娓呴櫎寰呭彂閫佹暟鎹�

			case SerialPortEvent.DSR: // 4 寰呭彂閫佹暟鎹噯澶囧ソ浜�

			case SerialPortEvent.RI: // 5 鎸搩鎸囩ず

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 杈撳嚭缂撳啿鍖哄凡娓呯┖
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 涓插彛瀛樺湪鍙敤鏁版嵁
				byte[] data = null;
				try {
					if (serialport == null) {
						ShowUtils.errorMessage("串口对象为空！监听失败！");
					} else {
						// 璇诲彇涓插彛鏁版嵁
						data = SerialPortManager.readFromPort(serialport);

						sb.append(ByteUtils.byteArrayToHexString(data, false));
						System.out.println(sb);
						int start = sb.lastIndexOf("FAFBFE00AA");
						int ends = sb.lastIndexOf("FAFBFEFFAA");

						if ((start > 0) && (ends > 0) && (start < ends)) {
							subs = sb.substring(start, ends + 10);

							int startcontain = subs.indexOf("FEFBFA");
							int alltotalNo = 0;
							int alltotalleft = 0;
							
							/*try {
								sql = "SELECT car_id,theta,portx,porty From carport,mycarport where carport.port_id=mycarport.port_id";
								rs = statement.executeQuery(sql);
								int i = 0;
								while (rs.next()) {
									String car_id = rs.getString("car_id");

									// System.out.println();
									double theta = rs.getDouble("theta");
									JLabelExt port1 = new JLabelExt(new DecimalFormat("0000").format(Integer.parseInt(car_id)), theta);
									int x1 = rs.getInt("portx");
									int y1 = rs.getInt("porty");
									port1.setBounds(x1, y1, screenWidth / 8, screenHeight / 8);
									port1.setFont(new Font("宋体", Font.PLAIN, 20));
                                    pan7.remove(i);
									pan7.add(port1, i);
									i++;
								}

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							while (startcontain > 0) {

								subscontain = subs.substring(startcontain, startcontain + 46);
								regionNo = subscontain.substring(6, 8);
								totalNo = subscontain.substring(8, 10);
								leftNo = subscontain.substring(10, 12);

								hexstringdata = subscontain.substring(12, 28);
								hexstringatline = subscontain.substring(28, 44);

								binarystringdata = ByteUtils.hexString2binaryString(hexstringdata);
								binarystringatline = ByteUtils.hexString2binaryString(hexstringatline);

								for (int i = 0; i < binarystringdata.length(); i += 8) {

									switch (i) {
									case 0:
										binarystringatlinetobyte = binarystringatline.substring(0, 8);
										binarystringdatatobyte = binarystringdata.substring(0, 8);
										break;
									case 8:
										binarystringatlinetobyte = binarystringatline.substring(8, 16);
										binarystringdatatobyte = binarystringdata.substring(8, 16);
										break;
									case 16:
										binarystringatlinetobyte = binarystringatline.substring(16, 24);
										binarystringdatatobyte = binarystringdata.substring(16, 24);
										break;
									case 24:
										binarystringatlinetobyte = binarystringatline.substring(24, 32);
										binarystringdatatobyte = binarystringdata.substring(24, 32);
										break;
									case 32:
										binarystringatlinetobyte = binarystringatline.substring(32, 40);
										binarystringdatatobyte = binarystringdata.substring(32, 40);
										break;
									case 40:
										binarystringatlinetobyte = binarystringatline.substring(40, 48);
										binarystringdatatobyte = binarystringdata.substring(40, 48);
										break;
									case 48:
										binarystringatlinetobyte = binarystringatline.substring(48, 56);
										binarystringdatatobyte = binarystringdata.substring(48, 56);
										break;
									case 56:
										binarystringatlinetobyte = binarystringatline.substring(56, 64);
										binarystringdatatobyte = binarystringdata.substring(56, 64);
										break;

									}

									StringBuffer sbuilderdata = new StringBuffer(binarystringdatatobyte);
									StringBuffer sbuilderatline = new StringBuffer(binarystringatlinetobyte);
									binarystringdatatobyte = sbuilderdata.reverse().toString();
									binarystringatlinetobyte = sbuilderatline.reverse().toString();
									for(int j=0;j<8;j++){
										
									    String num=new DecimalFormat("00").format(i+j+1);
									    String regionnum=new DecimalFormat("00").format(Integer.parseInt(regionNo,16));
										char a = binarystringdatatobyte.charAt(j);
										char b = binarystringatlinetobyte.charAt(j);
										try {
											sql = "SELECT port_id From mycarport where car_id='"+regionnum+num+"'";
											rs2 = stmt2.executeQuery(sql);
		                                    if(rs2.next()){
		                                    	int port_id=rs2.getInt("port_id");
		                                    	
		                                    	if(Integer.parseInt(String.valueOf(b))==1){
													if(Integer.parseInt(String.valueOf(a))==0){
														JLabelExt port =(JLabelExt)pan7.getComponent(port_id);
														URL url=this.getClass().getResource("cararrived.jpg");
														ImageIcon icon= new ImageIcon(url);
														icon.setImage(icon.getImage().getScaledInstance(screenSize.width/80,screenSize.height/80, Image.SCALE_DEFAULT));
														port.setIcon(icon);
														port.setText("");
														pan7.remove(port_id);
														pan7.add(port, port_id);
														
														pan7.repaint();
														
														
														/*if((j+i+alltotalNo)<28){
															
															
															
																
																JLabelExt port =(JLabelExt)pan7.getComponent(j+i+alltotalNo+37);
																URL url=this.getClass().getResource("cararrived.jpg");
																ImageIcon icon= new ImageIcon(url);
																icon.setImage(icon.getImage().getScaledInstance(screenSize.width/80,screenSize.height/80, Image.SCALE_DEFAULT));
																port.setIcon(icon);
																port.setText("");
																pan7.remove(j+i+alltotalNo+37);
																pan7.add(port, j+i+alltotalNo+37);
																
																pan7.repaint();
																
															
														}else{
															JLabelExt port =(JLabelExt)pan7.getComponent(j+i+alltotalNo+37+10);
															URL url=this.getClass().getResource("cararrived.jpg");
															ImageIcon icon= new ImageIcon(url);
															icon.setImage(icon.getImage().getScaledInstance(screenSize.width/80,screenSize.height/80, Image.SCALE_DEFAULT));
															port.setIcon(icon);
															port.setText("");
															pan7.remove(i+j+alltotalNo+37+10);
															pan7.add(port, i+j+alltotalNo+37+10);
															pan7.repaint();
															
														}*/
														
													}else{
														JLabelExt port =(JLabelExt)pan7.getComponent(port_id);
														URL url=this.getClass().getResource("carport.jpg");
														ImageIcon icon= new ImageIcon(url);
														icon.setImage(icon.getImage().getScaledInstance(screenSize.width/80,screenSize.height/80, Image.SCALE_DEFAULT));
														port.setIcon(icon);
														port.setText("");
														pan7.remove(port_id);
														pan7.add(port, port_id);
														pan7.repaint();
														/*if((j+i+alltotalNo)<28){
															JLabelExt port =(JLabelExt)pan7.getComponent(j+i+alltotalNo+37);
															URL url=this.getClass().getResource("carport.jpg");
															ImageIcon icon= new ImageIcon(url);
															icon.setImage(icon.getImage().getScaledInstance(screenSize.width/80,screenSize.height/80, Image.SCALE_DEFAULT));
															port.setIcon(icon);
															port.setText("");
															pan7.remove(j+i+alltotalNo+37);
															pan7.add(port, j+i+alltotalNo+37);
															pan7.repaint();
															
														}else{
															JLabelExt port =(JLabelExt)pan7.getComponent(j+i+alltotalNo+37+10);
															URL url=this.getClass().getResource("carport.jpg");
															ImageIcon icon= new ImageIcon(url);
															icon.setImage(icon.getImage().getScaledInstance(screenSize.width/80,screenSize.height/80, Image.SCALE_DEFAULT));
															port.setIcon(icon);
															port.setText("");
															pan7.remove(i+j+alltotalNo+37+10);
															pan7.add(port, i+j+alltotalNo+37+10);
															pan7.repaint();
															
														}*/
													}
											}
		                                    }									

										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									
								
									
									
								}
								}
								System.out.println("区域控制器：" + regionNo + "总车位：" + totalNo + "剩余车位：" + leftNo + "数据："
										+ binarystringdata + "在线：" + binarystringdata);

								startcontain = subs.indexOf("FEFBFA", startcontain + 1);
								alltotalNo += Integer.parseInt(totalNo);
								alltotalleft += Integer.parseInt(leftNo);
							}

							System.out.println("总车位：" + alltotalNo + "总剩余车位：" + alltotalleft);
							portMessageLabel.setText("总车位：" + alltotalNo + "总剩余车位：" + alltotalleft);
						}
					}
				} catch (Exception e) {

				}
				break;
			}
		}
	}
}
