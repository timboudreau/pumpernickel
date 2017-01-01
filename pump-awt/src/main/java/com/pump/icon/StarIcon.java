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
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;

import com.pump.blog.ResourceSample;

/** A star icon.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>Here are some samples:
 * <table summary="Resource&#160;Samples&#160;for&#160;com.bric.swing.resources.StarIcon"><tr>
 * <td></td>
 * <td><img src="https://javagraphics.java.net/resources/samples/StarIcon/Filled.png" alt="new&#160;com.bric.swing.resources.StarIcon(&#160;16,&#160;16,&#160;false)"></td>
 * <td><img src="https://javagraphics.java.net/resources/samples/StarIcon/Empty.png" alt="new&#160;com.bric.swing.resources.StarIcon(&#160;16,&#160;16,&#160;true)"></td>
 * </tr><tr>
 * <td></td>
 * <td>Filled</td>
 * <td>Empty</td>
 * </tr><tr>
 * </tr></table>
 * <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample( sample= { 
		"new com.bric.swing.resources.StarIcon( 16, 16, false)",
		"new com.bric.swing.resources.StarIcon( 16, 16, true)" },
		names = {"Filled", "Empty"} )
public class StarIcon implements Icon {
	int width, height;
	
	Paint fill;
	Paint edge;
	
	public StarIcon(int w,int h,boolean empty) {
		this.width = w;
		this.height = h;
		if(empty) {
			fill = new GradientPaint(
					0, 0, new Color(255,255,255,50),
					0, h, new Color(100,100,100,50) );
			edge = new Color(0,0,0,50);
		} else {
			fill = new GradientPaint(
					0, 0, new Color(255,255,0),
					0, h, new Color(255,200,0) );
			edge = new Color(0,0,0,50);
		}
	}

	public int getIconHeight() {
		return height;
	}

	public int getIconWidth() {
		return width;
	}

	static GeneralPath starShape;
	static float idealSize = 16;
	
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D)g;

		if(starShape==null) {
			starShape = new GeneralPath();
			starShape.moveTo(8, 1);
			starShape.lineTo(8+2, 8-2);
			starShape.lineTo(8+7, 8-1);
			starShape.lineTo(8+2, 8+2);
			starShape.lineTo(8+4, 8+7);
			starShape.lineTo(8, 8+4);
			starShape.lineTo(8-4, 8+7);
			starShape.lineTo(8-2, 8+2);
			starShape.lineTo(8-7, 8-1);
			starShape.lineTo(8-2, 8-2);
			starShape.closePath();
		}
		
		
		g2.translate(x, y);
		float scaleX = (getIconWidth())/idealSize;
		float scaleY = (getIconHeight())/idealSize;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.scale(scaleX, scaleY);
		g2.setPaint(fill);
		g2.fill(starShape);
		g2.setPaint(edge);
		g2.setStroke(new BasicStroke(.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(starShape);
	}
}