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
package com.pump.showcase;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.pump.awt.BrushStroke;
import com.pump.blog.ResourceSample;

/**
 * A simple demo program showing off the {@link com.pump.awt.BrushStroke}.
 * 
 *
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/BrushStrokeDemo/sample.png"
 * alt="new&#160;com.bric.awt.BrushStrokeDemo()"> <!-- ======== END OF
 * AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = "new com.bric.awt.BrushStrokeDemo()")
public class BrushStrokeDemo extends StrokeDemo {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			BrushStrokeDemo d = new BrushStrokeDemo();
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.getContentPane().add(d);
			f.pack();
			f.setVisible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public BrushStrokeDemo() throws NoSuchMethodException {
		super(BrushStroke.class, BrushStroke.class.getConstructor(new Class[] {
				Float.TYPE, Float.TYPE }), new JLabel[] {
				new JLabel("Starting Width:"), new JLabel("Thickness:") },
				new JSpinner[] {
						new JSpinner(new SpinnerNumberModel(1, .05, 20, .1)),
						new JSpinner(new SpinnerNumberModel(.5, 0, 1, .05)), });
	}
}