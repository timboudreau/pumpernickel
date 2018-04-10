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
package com.pump.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.Border;

import com.pump.blog.ResourceSample;
import com.pump.math.MathG;

/**
 * 
 *
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/DashedBorder/sample.png"
 * alt="new&#160;com.bric.swing.DashedBorder(&#160;8,&#160;2,&#160;6)"> <!--
 * ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = "new com.bric.swing.DashedBorder( 8, 2, 6)")
public class DashedBorder implements Border {

	int padding, strokeWidth, dashLength, arcCurve;
	Color color;

	public DashedBorder(int padding, int strokeWidth, int dashLength) {
		this(padding, strokeWidth, dashLength, Color.gray);
	}

	public DashedBorder(int padding, int strokeWidth, int dashLength,
			Color color) {
		this(padding, strokeWidth, dashLength, color, 20);
	}

	public DashedBorder(int padding, int strokeWidth, int dashLength,
			Color color, int arcCurve) {
		this.padding = padding;
		this.arcCurve = arcCurve;
		this.strokeWidth = strokeWidth;
		this.dashLength = dashLength;
		this.color = color;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(padding, padding, padding, padding);
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		paintBorder((Graphics2D) g, x, y, width, height, color, strokeWidth,
				dashLength, arcCurve);
	}

	public static void paintBorder(Graphics2D g, float x, float y, float width,
			float height, Color color, int strokeWidth, int dashLength,
			int arcCurve) {
		int k = MathG.ceilInt(((double) strokeWidth) / 2.0);
		RoundRectangle2D r = new RoundRectangle2D.Float(x + k, y + k, width - 2
				* k, height - 2 * k, arcCurve, arcCurve);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10, new float[] { dashLength,
						dashLength }, 0));
		g2.draw(r);
		g2.dispose();
	}

}