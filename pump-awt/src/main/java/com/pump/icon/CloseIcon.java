/**
 * This software is released as part of the Pumpernickel project.
 * 
 * All com.pump resources in the Pumpernickel project are distributed under the
 * MIT License:
 * https://raw.githubusercontent.com/mickleness/pumpernickel/master/License.txt
 * 
 * More information about the Pumpernickel project is available here:
 * https://mickleness.github.io/pumpernickel/
 */
package com.pump.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.Icon;

import com.pump.blog.ResourceSample;

/** An icon of a circle with an x inside it.
 * 
 * <p>This is meant to be displayed at a smaller size than its cousin the {@link com.bric.swing.JFancyBox.FancyCloseIcon}.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>Here are some samples:
 * <table summary="Resource&#160;Samples&#160;for&#160;com.bric.swing.resources.CloseIcon"><tr>
 * <td></td>
 * <td>Normal</td>
 * <td>Rollover</td>
 * <td>Pressed</td>
 * </tr><tr>
 * <td>32x32</td>
 * <td><img src="https://javagraphics.java.net/resources/samples/CloseIcon/sample.png" alt="new&#160;com.bric.swing.resources.CloseIcon(32)"></td>
 * <td><img src="https://javagraphics.java.net/resources/samples/CloseIcon/sample2.png" alt="new&#160;com.bric.swing.resources.CloseIcon(32,&#160;com.bric.swing.resources.CloseIcon$State.ROLLOVER&#160;)"></td>
 * <td><img src="https://javagraphics.java.net/resources/samples/CloseIcon/sample3.png" alt="new&#160;com.bric.swing.resources.CloseIcon(32,&#160;com.bric.swing.resources.CloseIcon$State.PRESSED&#160;)"></td>
 * </tr><tr>
 * <td>12x12</td>
 * <td><img src="https://javagraphics.java.net/resources/samples/CloseIcon/sample4.png" alt="new&#160;com.bric.swing.resources.CloseIcon(12)"></td>
 * <td><img src="https://javagraphics.java.net/resources/samples/CloseIcon/sample5.png" alt="new&#160;com.bric.swing.resources.CloseIcon(12,&#160;com.bric.swing.resources.CloseIcon$State.ROLLOVER&#160;)"></td>
 * <td><img src="https://javagraphics.java.net/resources/samples/CloseIcon/sample6.png" alt="new&#160;com.bric.swing.resources.CloseIcon(12,&#160;com.bric.swing.resources.CloseIcon$State.PRESSED&#160;)"></td>
 * </tr><tr>
 * </tr></table>
 * <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample( sample= { 
		"new com.bric.swing.resources.CloseIcon(32)", 
		"new com.bric.swing.resources.CloseIcon(32, com.bric.swing.resources.CloseIcon$State.ROLLOVER )", 
		"new com.bric.swing.resources.CloseIcon(32, com.bric.swing.resources.CloseIcon$State.PRESSED )",
		"new com.bric.swing.resources.CloseIcon(12)", 
		"new com.bric.swing.resources.CloseIcon(12, com.bric.swing.resources.CloseIcon$State.ROLLOVER )", 
		"new com.bric.swing.resources.CloseIcon(12, com.bric.swing.resources.CloseIcon$State.PRESSED )" },
		columnNames = { "Normal", "Rollover", "Pressed" },
		rowNames = { "32x32", "12x12" },
		columnCount = 3 )
public class CloseIcon implements Icon {
	
	public static enum State { NORMAL, ROLLOVER, PRESSED };
	
	int size;
	Color bkgndTop = new Color(0x6C6C6C);
	Color bkgndBottom = new Color(0x838383);
	Color borderTop = new Color(0x5A5A5A);
	Color borderBottom = new Color(0x838383);
	Color xColor = new Color(0xDFDFDF);
	BasicStroke stroke;
	
	State state;

	public CloseIcon(int size) {
		this(size, State.NORMAL);
	}
	
	public CloseIcon(int size,State state) {
		this.size = size;
		this.state = state;
		stroke = new BasicStroke( ((float)size)/5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL );
	
		if(state==State.PRESSED) {
			borderTop = new Color(0x212121);
			borderBottom = new Color(0x484848);
			bkgndTop = borderTop;
			bkgndBottom = borderBottom;
		} else if(state==State.ROLLOVER) {
			borderTop = new Color(0x3D3D3D);
			borderBottom = new Color(0x656565);
			bkgndTop = borderTop;
			bkgndBottom = borderBottom;
		}
	}
	
	@Override
	public String toString() {
		if(state==State.NORMAL)
			return "Close "+size;
		return "Close "+size+" "+state;
	}

	public int getIconHeight() {
		return size+2;
	}

	public int getIconWidth() {
		return size;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.translate(0,1);
		
		Ellipse2D fill = new Ellipse2D.Float(x,y,size,size);
		Ellipse2D border = new Ellipse2D.Float(x,y,size-1,size-1);
		
		//draw the light shadow/bevel
		g2.translate(0, 1);
		g2.setColor(new Color(255,255,255,120));
		g2.fill(fill);
		g2.translate(0, -1);
		
		g2.setPaint(new GradientPaint(0,0,bkgndTop,0,size,bkgndBottom));
		g2.fill(fill);
		
		//draw the X
		g2.setPaint(xColor);
		g2.setStroke(stroke);
		Line2D line = new Line2D.Float();
		double r = size/2*Math.sin(Math.PI/4)-1;
		line.setLine(x+size/2-r-.5, y+size/2-r-.5, x+size/2+r, y+size/2+r);
		g2.draw(line);
		line.setLine(x+size/2+r-.5, y+size/2-r-.5, x+size/2-r, y+size/2+r);
		g2.draw(line);

		//draw the border
		g2.setStroke(new BasicStroke(1));
		g2.setPaint(new GradientPaint(0,0,borderTop,0,size,borderBottom));
		g2.draw(border);
	}

}