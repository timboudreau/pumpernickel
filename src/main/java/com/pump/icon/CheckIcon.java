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
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;

import com.pump.blog.ResourceSample;
import com.pump.geom.ShapeBounds;
import com.pump.geom.ShapeStringUtils;
import com.pump.geom.TransformUtils;

/**
 * A checkmark icon.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * Here are some samples:
 * <table summary="Resource&#160;Samples&#160;for&#160;com.pump.swing.resources.CheckIcon">
 * <tr>
 * <td></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/resources/samples/CheckIcon/12x12.png"
 * alt="new&#160;com.pump.swing.resources.CheckIcon(12,&#160;12)"></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/resources/samples/CheckIcon/24x24.png"
 * alt="new&#160;com.pump.swing.resources.CheckIcon(24,&#160;24)"></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/resources/samples/CheckIcon/36x36.png"
 * alt="new&#160;com.pump.swing.resources.CheckIcon(36,&#160;36)"></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/resources/samples/CheckIcon/48x48.png"
 * alt="new&#160;com.pump.swing.resources.CheckIcon(48,&#160;48)"></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>12x12</td>
 * <td>24x24</td>
 * <td>36x36</td>
 * <td>48x48</td>
 * </tr>
 * <tr>
 * </tr>
 * </table>
 * <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = { "new com.pump.swing.resources.CheckIcon(12, 12)",
		"new com.pump.swing.resources.CheckIcon(24, 24)",
		"new com.pump.swing.resources.CheckIcon(36, 36)",
		"new com.pump.swing.resources.CheckIcon(48, 48)" }, names = { "12x12",
		"24x24", "36x36", "48x48" })
public class CheckIcon implements Icon {
	Shape checkShape = ShapeStringUtils
			.createGeneralPath("m 19.488281 -15.234375 q 19.488281 -13.863281 18.246094 -12.5625 l 18.117188 -12.433594 l 10.875 -4.8046875 q 8.566406 -2.3789062 6.9433594 -1.0546875 q 5.3203125 0.26953125 4.6523438 0.26953125 q 3.9492188 0.26953125 2.90625 -0.39257812 q 1.8632812 -1.0546875 1.5585938 -1.7578125 q 1.3007812 -2.34375 1.0722656 -4.2421875 q 0.84375 -6.140625 0.84375 -8.355469 q 0.84375 -9.5625 1.8515625 -10.587891 q 2.859375 -11.613281 4.078125 -11.613281 q 5.2382812 -11.613281 5.4609375 -9.4921875 q 5.484375 -9.246094 5.4960938 -9.128906 q 5.6835938 -7.5 5.9296875 -6.8554688 q 6.1757812 -6.2109375 6.5976562 -6.2109375 q 6.7734375 -6.2109375 7.2539062 -6.591797 q 7.734375 -6.9726562 8.402344 -7.6289062 l 15.84375 -15.0 q 16.78125 -15.9375 17.519531 -16.417969 q 18.257812 -16.898438 18.773438 -16.898438 q 19.160156 -16.898438 19.324219 -16.605469 q 19.488281 -16.3125 19.488281 -15.621094 z");
	int width, height;
	Color color;
	String name;

	/**
	 * Create a CheckIcon that is dark gray.
	 * 
	 * @param width
	 *            the width (in pixels) of this icon.
	 * @param height
	 *            the height (in pixels) of this icon.
	 */
	public CheckIcon(int width, int height) {
		this(width, height, null);
	}

	/**
	 * Create a CheckIcon.
	 * 
	 * @param width
	 *            the width (in pixels) of this icon.
	 * @param height
	 *            the height (in pixels) of this icon.
	 * @param c
	 *            the optional color of this icon. If null: then a dark gray is
	 *            used by default.
	 */
	public CheckIcon(int width, int height, Color c) {
		this.width = width;
		this.height = height;
		this.color = c == null ? Color.DARK_GRAY : c;
		name = "Check " + width + "x" + height;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		Rectangle2D abstractBounds = ShapeBounds.getBounds(checkShape);
		Rectangle2D realBounds = new Rectangle(0, 0, width, height);
		g2.transform(TransformUtils.createAffineTransform(abstractBounds,
				realBounds));
		g2.fill(checkShape);
		g2.dispose();
	}

	@Override
	public String toString() {
		return name;
	}

	public int getIconWidth() {
		return width;
	}

	public int getIconHeight() {
		return height;
	}

}