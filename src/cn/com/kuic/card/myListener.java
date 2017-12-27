package cn.com.kuic.card;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

public class myListener extends MouseAdapter{

	
		  
		//这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置  
		  
		  
		int newX,newY,oldX,oldY;  
		  
		private int portnumber=0;
		//这两个坐标为组件当前的坐标  
		  
		  
		int startX,startY;  
	
		  
		@Override  
		  
		  
		public void mousePressed(MouseEvent e) {  
		  
		  
		  
		//此为得到事件源组件  
		  
		  
		  
		Component cp = (Component)e.getSource();  
		  
		  
		  
		//当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置  
		  
		  
		  
		oldX = e.getX();  
		  
		  
		  
		oldY = e.getY();  
		  
		  
		  
		
		  
		  
		}  
		  
		  
		@Override  
		  
		  
		public void mouseDragged(MouseEvent e) {  
		  
		  
		  
		Component cp = (Component)e.getSource();  
		  
		
		  
		//拖动的时候记录新坐标  
		  
		
		newX = e.getX();  
		  
		  
		  
		newY = e.getY();  
		
		startX=cp.getLocation().x;
		startY=cp.getLocation().y;
		  
		//设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加  
		  
		
		
		cp.setBounds(startX+newX-oldX, startY+newY-oldY, cp.getWidth(), cp.getHeight());  
		
		
		}


		
		  
		 
		}  


