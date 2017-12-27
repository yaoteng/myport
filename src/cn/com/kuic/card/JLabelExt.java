package cn.com.kuic.card;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class JLabelExt extends JLabel {
    private double theta;
	
	public JLabelExt(String text,double theta){
		super(text);
		this.theta=theta;
		
		
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		int w = getWidth(), h = getHeight();
        double theta = Math.toRadians(this.theta);
        Graphics2D g2d = (Graphics2D) g;
        // Ïû³ý¾â³Ý
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHints(renderingHints);
        g2d.rotate(theta, w / 2, h / 2);
        super.paintComponent(g);
	}
	
	
}
