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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

import com.pump.blog.ResourceSample;

/**
 * An icon of layers being vertically stacked.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/StackIcon/sample.png"
 * alt="new&#160;com.bric.swing.resources.StackIcon(&#160;12,&#160;12)"> <!--
 * ======== END OF AUTOGENERATED SAMPLES ======== -->
 * 
 * @see ColumnIcon
 * @see ListIcon
 * @see TileIcon
 */
@ResourceSample(sample = { "new com.bric.swing.resources.StackIcon( 12, 12)" })
public class StackIcon implements Icon {
	final int w, h;
	int layers = 2;

	public StackIcon(int width, int height) {
		w = width;
		h = height;
	}

	public int getIconHeight() {
		return h;
	}

	public int getIconWidth() {
		return w;
	}

	public void paintIcon(Component c, Graphics g0, int x, int y) {
		Graphics2D g = (Graphics2D) g0.create();

		g.setColor(Color.darkGray);
		g.translate(x, y);

		int bottom = h - 5;
		int right = w - 5;
		g.drawRect(0, 0, right, bottom);
		for (int a = 1; a < layers; a++) {
			int newRight = right + 2;
			int newBottom = bottom + 2;
			g.drawLine(right, a * 2, newRight, a * 2);
			g.drawLine(newRight, a * 2, newRight, newBottom);
			g.drawLine(newRight, newBottom, a * 2, newBottom);
			g.drawLine(a * 2, newBottom, a * 2, bottom);

			bottom = newBottom;
			right = newRight;
		}
		g.dispose();
	}
}