/*
 * MainFrame.java
 *
 * Created on 2016.8.19
 */

package com.yang.serialport.ui;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.yang.serialport.exception.NoSuchPort;
import com.yang.serialport.exception.NotASerialPort;
import com.yang.serialport.exception.PortInUse;
import com.yang.serialport.exception.SendDataToSerialPortFailure;
import com.yang.serialport.exception.SerialPortOutputStreamCloseFailure;
import com.yang.serialport.exception.SerialPortParameterFailure;
import com.yang.serialport.exception.TooManyListeners;
import com.yang.serialport.manage.SerialPortManager;
import com.yang.serialport.utils.ByteUtils;
import com.yang.serialport.utils.ShowUtils;

/**
 * 涓荤晫闈�
 * 
 * @author yangle
 */
public class MainFrame extends JFrame {

	/**
	 * 绋嬪簭鐣岄潰瀹藉害
	 */
	public static final int WIDTH = 500;

	/**
	 * 绋嬪簭鐣岄潰楂樺害
	 */
	public static final int HEIGHT = 360;

	private JTextArea dataView = new JTextArea();
	private StringBuffer sb = new StringBuffer();
	private String subs="",subscontain="",regionNo="",totalNo="",leftNo="",hexstringdata="",hexstringatline="",binarystringdata="",binarystringatline="";
	private JScrollPane scrollDataView = new JScrollPane(dataView);

	// 涓插彛璁剧疆闈㈡澘
	private JPanel serialPortPanel = new JPanel();
	private JLabel serialPortLabel = new JLabel("\u4e32\u53e3");
	private JLabel baudrateLabel = new JLabel("\u6ce2\u7279\u7387");
	private JComboBox commChoice = new JComboBox();
	private JComboBox baudrateChoice = new JComboBox();

	// 鎿嶄綔闈㈡澘
	private JPanel operatePanel = new JPanel();
	private JTextField dataInput = new JTextField();
	private JButton serialPortOperate = new JButton("\u6253\u5f00\u4e32\u53e3");
	private JButton sendData = new JButton("\u53d1\u9001\u6570\u636e");

	private List<String> commList = null;
	private SerialPort serialport;

	public MainFrame() {
		initView();
		initComponents();
		actionListener();
		initData();
	}

	private void initView() {
		// 鍏抽棴绋嬪簭
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		// 绂佹绐楀彛鏈�澶у寲
		setResizable(false);

		// 璁剧疆绋嬪簭绐楀彛灞呬腑鏄剧ず
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
		this.setLayout(null);

		setTitle("\u4e32\u53e3\u901a\u8baf");
	}

	private void initComponents() {
		// 鏁版嵁鏄剧ず
		dataView.setFocusable(false);
		scrollDataView.setBounds(10, 10, 475, 200);
		add(scrollDataView);

		// 涓插彛璁剧疆
		serialPortPanel.setBorder(BorderFactory.createTitledBorder("\u4e32\u53e3\u8bbe\u7f6e"));
		serialPortPanel.setBounds(10, 220, 170, 100);
		serialPortPanel.setLayout(null);
		add(serialPortPanel);

		serialPortLabel.setForeground(Color.gray);
		serialPortLabel.setBounds(10, 25, 40, 20);
		serialPortPanel.add(serialPortLabel);

		commChoice.setFocusable(false);
		commChoice.setBounds(60, 25, 100, 20);
		serialPortPanel.add(commChoice);

		baudrateLabel.setForeground(Color.gray);
		baudrateLabel.setBounds(10, 60, 40, 20);
		serialPortPanel.add(baudrateLabel);

		baudrateChoice.setFocusable(false);
		baudrateChoice.setBounds(60, 60, 100, 20);
		serialPortPanel.add(baudrateChoice);

		// 鎿嶄綔
		operatePanel.setBorder(BorderFactory.createTitledBorder("\u64cd\u4f5c"));
		operatePanel.setBounds(200, 220, 285, 100);
		operatePanel.setLayout(null);
		add(operatePanel);

		dataInput.setBounds(25, 25, 235, 20);
		operatePanel.add(dataInput);

		serialPortOperate.setFocusable(false);
		serialPortOperate.setBounds(45, 60, 90, 20);
		operatePanel.add(serialPortOperate);

		sendData.setFocusable(false);
		sendData.setBounds(155, 60, 90, 20);
		operatePanel.add(sendData);
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
        baudrateChoice.addItem("4800");
		baudrateChoice.addItem("9600");
		baudrateChoice.addItem("19200");
		baudrateChoice.addItem("38400");
		baudrateChoice.addItem("57600");
		baudrateChoice.addItem("115200");
		
		File f = new File("D:\\myProperty.properties");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	private void actionListener() {
		serialPortOperate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ("\u6253\u5f00\u4e32\u53e3".equals(serialPortOperate.getText())
						&& serialport == null) {
					openSerialPort(e);
				} else {
					closeSerialPort(e);
				}
			}
		});

		sendData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendData(e);
			}
		});
	}

	/**
	 * 鎵撳紑涓插彛
	 * 
	 * @param evt
	 *            鐐瑰嚮浜嬩欢
	 */
	private void openSerialPort(java.awt.event.ActionEvent evt) {
		// 鑾峰彇涓插彛鍚嶇О
		String commName = (String) commChoice.getSelectedItem();
		// 鑾峰彇娉㈢壒鐜�
		int baudrate = 9600;
		String bps = (String) baudrateChoice.getSelectedItem();
		baudrate = Integer.parseInt(bps);

		this.update_properies("commName", commName, "D:\\myProperty.properties");
		this.update_properies("bps", baudrate+"", "D:\\myProperty.properties");
		// 妫�鏌ヤ覆鍙ｅ悕绉版槸鍚﹁幏鍙栨纭�
		if (commName == null || commName.equals("")) {
			ShowUtils.warningMessage("没有搜索到有效串口！");
		} else {
			try {
				serialport = SerialPortManager.openPort(commName, baudrate);
				if (serialport != null) {
					dataView.setText("\u4e32\u53e3\u5df2\u6253\u5f00" + "\r\n");
					serialPortOperate.setText("\u5173\u95ed\u4e32\u53e3");
				}
			} catch (SerialPortParameterFailure e) {
				e.printStackTrace();
			} catch (NotASerialPort e) {
				e.printStackTrace();
			} catch (NoSuchPort e) {
				e.printStackTrace();
			} catch (PortInUse e) {
				e.printStackTrace();
				ShowUtils.warningMessage("串口已被占用！");
			}
		}

		try {
			SerialPortManager.addListener(serialport, new SerialListener());
		} catch (TooManyListeners e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍏抽棴涓插彛
	 * 
	 * @param evt
	 *            鐐瑰嚮浜嬩欢
	 */
	private void closeSerialPort(java.awt.event.ActionEvent evt) {
		SerialPortManager.closePort(serialport);
		dataView.setText("\u4e32\u53e3\u5df2\u5173\u95ed" + "\r\n");
		serialPortOperate.setText("\u6253\u5f00\u4e32\u53e3");
	}

	/**
	 * 鍙戦�佹暟鎹�
	 * 
	 * @param evt
	 *            鐐瑰嚮浜嬩欢
	 */
	private void sendData(java.awt.event.ActionEvent evt) {
		String data = dataInput.getText().toString();
		try {
			SerialPortManager.sendToPort(serialport,
					ByteUtils.hexStr2Byte(data));
		} catch (SendDataToSerialPortFailure e) {
			e.printStackTrace();
		} catch (SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
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
						
						sb.append(ByteUtils.byteArrayToHexString(data,
								false));
						int start = sb.lastIndexOf("FAFBFE00AA");
						int ends = sb.lastIndexOf("FAFBFEFFAA");
						
						if((start>0)&&(ends>0)&&(start<ends)){
							subs=sb.substring(start, ends+10);
							
							int startcontain=subs.indexOf("FEFBFA");
							
							while(startcontain>0){
								
				
								subscontain=subs.substring(startcontain, startcontain+46);
								regionNo=subscontain.substring(6,8);
								totalNo=subscontain.substring(8,10);
								leftNo=subscontain.substring(10,12);
								if(Integer.parseInt(totalNo)<=8){
									hexstringdata=subscontain.substring(12,14);
									hexstringatline=subscontain.substring(28,30);
								}else if(Integer.parseInt(totalNo)>8&&Integer.parseInt(totalNo)<=16){
									
									hexstringdata=subscontain.substring(12,16);
									hexstringatline=subscontain.substring(28,32);
								}else if(Integer.parseInt(totalNo)>16&&Integer.parseInt(totalNo)<=24){
									hexstringdata=subscontain.substring(12,18);
									hexstringatline=subscontain.substring(28,34);
								}else if(Integer.parseInt(totalNo)>24&&Integer.parseInt(totalNo)<=32){
									hexstringdata=subscontain.substring(12,20);
						            hexstringatline=subscontain.substring(28,36);			
								}else if(Integer.parseInt(totalNo)>32&&Integer.parseInt(totalNo)<=40){
									
									hexstringdata=subscontain.substring(12,22);
									hexstringatline=subscontain.substring(28,38);
								}else if(Integer.parseInt(totalNo)>40&&Integer.parseInt(totalNo)<=48){
									hexstringdata=subscontain.substring(12,24);
									hexstringatline=subscontain.substring(28, 40);
								}else if(Integer.parseInt(totalNo)>48&&Integer.parseInt(totalNo)<=56){
									hexstringdata=subscontain.substring(12,26);
									hexstringatline=subscontain.substring(28, 42);
								}else if(Integer.parseInt(totalNo)>56&&Integer.parseInt(totalNo)<=64){
									hexstringdata=subscontain.substring(12,28);
									hexstringatline=subscontain.substring(28, 44);
								}
								
								
								binarystringdata=ByteUtils.hexString2binaryString(hexstringdata);
								binarystringatline=ByteUtils.hexString2binaryString(hexstringatline);
								for (int i=0;i<binarystringdata.length();i++){
									char a = binarystringdata.charAt(i);
									if(Integer.parseInt(String.valueOf(a))==0){
										
									}
								}
								System.out.println("区域控制器："+regionNo+"总车位："+totalNo+"剩余车位："+leftNo+"数据："+binarystringdata+"在线："+binarystringatline);
								dataView.append("区域控制器："+regionNo+"总车位："+totalNo+"剩余车位："+leftNo+"数据："+binarystringdata+"在线："+binarystringatline);
								startcontain=subs.indexOf("FEFBFA", startcontain+1);
								
								
							}
							
							
						}
					}
				} catch (Exception e) {
					ShowUtils.errorMessage(e.toString());
					// 鍙戠敓璇诲彇閿欒鏃舵樉绀洪敊璇俊鎭悗閫�鍑虹郴缁�
					System.exit(0);
				}
				break;
			}
		}
	}
	
	public static void update_properies(String key,String value,String path){
		   
		   File file = new File(path); 
		   Properties prop = new Properties(); 
		   InputStream inputFile = null;  
		        OutputStream outputFile = null;  
		        try {  
		            inputFile = new FileInputStream(file);  
		            prop.load(inputFile);  
		           // inputFile.close();//一定要在修改值之前关闭inputFile  
		            outputFile = new FileOutputStream(file); 
		          //设值-保存
		            prop.setProperty(key, value); 
		            //添加注释
		            prop.store(outputFile, "Update '" + key + "'+ '"+value);  
		        } catch (IOException e) {
		       e.printStackTrace();  
		        }  
		        finally{  
		            try {  
		           if(null!=outputFile){
		           outputFile.close();  
		           }
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            } 
		            try {  
		           if(null!=inputFile){
		           inputFile.close(); 
		           } 
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            } 
		        }
		           
		 }
	
	
}