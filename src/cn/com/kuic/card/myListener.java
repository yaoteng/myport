package cn.com.kuic.card;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

public class myListener extends MouseAdapter{

	
		  
		//������x��yΪ������ʱ����Ļ��λ�ú��϶�ʱ���ڵ�λ��  
		  
		  
		int newX,newY,oldX,oldY;  
		  
		private int portnumber=0;
		//����������Ϊ�����ǰ������  
		  
		  
		int startX,startY;  
	
		  
		@Override  
		  
		  
		public void mousePressed(MouseEvent e) {  
		  
		  
		  
		//��Ϊ�õ��¼�Դ���  
		  
		  
		  
		Component cp = (Component)e.getSource();  
		  
		  
		  
		//�������µ�ʱ���¼�����ǰ����������굱ǰ����Ļ��λ��  
		  
		  
		  
		oldX = e.getX();  
		  
		  
		  
		oldY = e.getY();  
		  
		  
		  
		
		  
		  
		}  
		  
		  
		@Override  
		  
		  
		public void mouseDragged(MouseEvent e) {  
		  
		  
		  
		Component cp = (Component)e.getSource();  
		  
		
		  
		//�϶���ʱ���¼������  
		  
		
		newX = e.getX();  
		  
		  
		  
		newY = e.getY();  
		
		startX=cp.getLocation().x;
		startY=cp.getLocation().y;
		  
		//����bounds,������ʱ��¼�������ʼ����������϶��ľ������  
		  
		
		
		cp.setBounds(startX+newX-oldX, startY+newY-oldY, cp.getWidth(), cp.getHeight());  
		
		
		}


		
		  
		 
		}  


