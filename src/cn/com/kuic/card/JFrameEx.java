package cn.com.kuic.card;

import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.plaf.FontUIResource;

public class JFrameEx extends JFrame {
	private static final long serialVersionUID = -8957628904857480236L;
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 300;
	private static int currentSkinIndex = 1;
	private boolean m_exitConfirm=false;
	
	public JFrameEx() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		centerWindow();
		setIconImage(loadImage("main.png"));
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);//设置捕获窗口事件
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public JFrameEx(String title)
	{
		this();
		setTitle(title);
	}
	
	public JFrameEx(String title, int width, int height)
	{
		this(title);
		setSize(new Dimension(width, height));
		centerWindow();
	}
	
	public JFrameEx(String title, int width, int height, boolean exitConfirm)
	{
		this(title);
		setSize(new Dimension(width, height));
		centerWindow();
		m_exitConfirm=exitConfirm;
	}
	
	public void centerWindow()
	{
		centerWindow(this);
	}
	
	public static void centerWindow(Component wnd)
	{
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize=wnd.getSize();
		wnd.setLocation(screenSize.width/2-windowSize.width/2,screenSize.height/2-windowSize.height/2);
	}
	
	public static ImageIcon loadImageIcon(String url)
	{
		try{
			System.out.println(GetModuleFolder());
			URLClassLoader urlLoader = (URLClassLoader)JFrameEx.class.getClassLoader();
			URL location=urlLoader.findResource(url);
			if(location!=null)
				return new ImageIcon(location);
			else
				return null;
		}catch(ClassCastException err){
			return new ImageIcon(GetModuleFolder()+url);
		}
	}
	
	public static Image loadImage(String url)
	{
		try{
			URLClassLoader urlLoader = (URLClassLoader)JFrameEx.class.getClassLoader();
			URL location=urlLoader.findResource(url);
			if(location!=null)
				return new ImageIcon(location).getImage()/*.getScaledInstance(width, height, Image.SCALE_DEFAULT)*/;
			else
				return null;
		}catch(ClassCastException err){
			return new ImageIcon(GetModuleFolder()+url).getImage()/*.getScaledInstance(width, height, Image.SCALE_DEFAULT)*/;
		}
	}
	
	public  void changeSkin()
	{
		LookAndFeelInfo skin[]=UIManager.getInstalledLookAndFeels();
		try {
			if(currentSkinIndex>=skin.length)
				currentSkinIndex=0;
			System.out.println(currentSkinIndex+"\t"+skin[currentSkinIndex].getName());
			UIManager.setLookAndFeel(skin[currentSkinIndex++].getClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public boolean showConfirmBox(Object message)
	{
		return showConfirmBox(JFrameEx.this, message, "");
	}
	
	public static boolean showConfirmBox(JFrame parentComponent, Object message, String title)
	{	
		int option = JOptionPane.showConfirmDialog(parentComponent, message, title.equals("")?parentComponent.getTitle():title, 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return option == JOptionPane.YES_OPTION;
	}
	
	public void showMessageBox(Object message)
	{
		showMessageBox(JFrameEx.this,message,"",false);
	}
	
	public void showMessageBox(Object message, boolean warning)
	{
		showMessageBox(JFrameEx.this,message,"",warning);
	}
	
	public static void showMessageBox(JFrame parentComponent, Object message, String title, boolean warning)
	{
		JOptionPane.showMessageDialog(parentComponent, message, title.equals("")?parentComponent.getTitle():title, warning?JOptionPane.WARNING_MESSAGE:JOptionPane.INFORMATION_MESSAGE);
	}
	
	public String showInputBox(Object message)
	{
		return showInputBox(JFrameEx.this, message, "");
	}
	
	public static String showInputBox(JFrame parentComponent, Object message, String title)
	{
		return JOptionPane.showInputDialog(parentComponent, message, title.equals("")?parentComponent.getTitle():title, JOptionPane.QUESTION_MESSAGE);
	}
	
	public String showInputBox(Object message, Object[] selectionValues)
	{
		return (String)showInputBox(JFrameEx.this, message, "", selectionValues);
	}
	
	public static String showInputBox(JFrame parentComponent, Object message, String title, Object[] selectionValues)
	{
		return (String) JOptionPane.showInputDialog(parentComponent, message, title.equals("")?parentComponent.getTitle():title,
				JOptionPane.QUESTION_MESSAGE, null, selectionValues, selectionValues[0]);
	}
	
	public static void sleep(long miniSeconds)
	{
		try {
			Thread.sleep(miniSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//InitGlobalFont(new Font("宋体", Font.PLAIN, 12));
	public static void InitGlobalFont(Font font)
	{
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys();keys.hasMoreElements();)
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
			{
				UIManager.put(key, fontRes);
			}
		}
	}
	
	public static String GetModuleFolder()
	{
		//如果打包到fatjar里面，路径形式为：file:/D:/MyDocuments/MyEclipse/UhfReader/UhfReader_fat.jar!/main/main.jar
		String LOCATION="";
	    try {
	        LOCATION = URLDecoder.decode(JFrameEx.class.getProtectionDomain()
	            .getCodeSource().getLocation().getFile(), "UTF-8");
	        if(LOCATION.length()<=4)
	        	return LOCATION;
	        if(LOCATION.substring(0,1).compareTo("/")==0){
	        	LOCATION=LOCATION.substring(1);
	        }else if(LOCATION.length()>8){
	        	if(LOCATION.substring(0, 6).compareToIgnoreCase("file:/")==0){
	        		LOCATION=LOCATION.substring(6);
	        	}
	        }
	        
	        LOCATION=LOCATION.replace("!/main/main.jar", "");
	        if(LOCATION.length()>8){
	        	if(LOCATION.substring(LOCATION.length()-4).compareToIgnoreCase(".jar")==0){
	        		int lastIndex = LOCATION.lastIndexOf('/');
	        		if(lastIndex>0){
	        			LOCATION=LOCATION.substring(0, lastIndex+1);
	        		}
	        	}
	        }
	    } catch (java.io.UnsupportedEncodingException e) {
	        LOCATION = "";
	    }
		return LOCATION;
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			if(m_exitConfirm){
				if(showConfirmBox("确定退出吗？"))
					super.processWindowEvent(e);
			}
			else
				super.processWindowEvent(e);
		}
	}
}

class JPanelEx extends JPanel
{
	private static final long serialVersionUID = -270300400469257599L;
	private JFrameEx frame=null;
	
	public JPanelEx() {
		super();
		JButton btn = new JButton("更换皮肤");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(frame!=null)
					frame.changeSkin();
			}
		});
		this.add(btn);
	}
	
	public JPanelEx(JFrameEx frame) {
		this();
		this.frame=frame;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("hello", 20, 40);
	}
}