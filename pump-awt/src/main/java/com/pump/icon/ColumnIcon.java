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

import javax.swing.Icon;

import com.pump.blog.ResourceSample;

/**
 * One of three icons used to toggle views in file browsers/dialogs.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ColumnIcon/sample.png"
 * alt="new&#160;com.bric.swing.resources.ColumnIcon(12,&#160;12)"> <!--
 * ======== END OF AUTOGENERATED SAMPLES ======== -->
 * 
 * @see ListIcon
 * @see TileIcon
 * @see StackIcon
 */
@ResourceSample(sample = { "new com.bric.swing.resources.ColumnIcon(12, 12)" })
public class ColumnIcon implements Icon {
	final int w, h;

	public ColumnIcon(int width, int height) {
		w = width;
		h = height;
	}

	public int getIconHeight() {
		return h;
	}

	public int getIconWidth() {
		return w;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(Color.darkGray);
		int k = 5; // column width
		int dx = (w - k * (w / k)) / 2;
		g.translate(dx, 0);
		for (int myX = x; myX + k - 1 < x + w; myX += k) {
			g.drawRect(myX, y, k, h);
		}
		g.translate(-dx, 0);
	}
}
