package cn.com.kuic.card;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelExt  extends JPanel{

	
	
	 private String imgpath;
	 private int width;
	 private int height;
	 
	 public JPanelExt(String imgPath,int width, int height) {
	 this.imgpath=imgPath;
	 this.width=width;
	 this.height=height;
	 }
	 
	 
	 public JPanelExt() {
	
	 }
	
	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	protected void paintComponent(Graphics g) {
        ImageIcon icon = new ImageIcon(imgpath);
        Image img = icon.getImage();
         img.getScaledInstance(width,height/8*6,Image.SCALE_DEFAULT);
//        g.drawImage(img, 0, 0, width, hight, this);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; //����ǹؼ���֮ǰû����䣬��ʾ������ͼƬ������ǲ�����ʾ�����������ʾ��ͼƬ�ϡ�
        g2.drawImage(img, 0, 0,width,height/8*6, null);
    }

}
