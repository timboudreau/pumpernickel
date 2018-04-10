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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

import com.pump.blog.ResourceSample;
import com.pump.plaf.AngleSliderUI;
import com.pump.plaf.AquaAngleSliderUI;
import com.pump.swing.MagnificationPanel;

/**
 * A demo class for the <code>AngleSliderUI</code> class.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/AngleSliderUIDemo/sample.png"
 * alt="new&#160;com.bric.plaf.AngleSliderUIDemo()"> <!-- ======== END OF
 * AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = { "new com.bric.plaf.AngleSliderUIDemo()" })
public class AngleSliderUIDemo extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	JLabel label1 = new JLabel("AngleSliderUI:");
	JLabel label2 = new JLabel("AquaAngleSliderUI:");
	JSlider slider1 = new JSlider();
	JSlider slider2 = new JSlider();
	JCheckBox enabled1 = new JCheckBox("Enabled", true);
	JCheckBox enabled2 = new JCheckBox("Enabled", true);
	JPanel contents = new JPanel(new GridBagLayout());
	MagnificationPanel zoomPanel = new MagnificationPanel(contents, 24, 24, 8);

	public AngleSliderUIDemo() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(4, 4, 4, 4);

		contents.add(label1, c);
		c.gridx++;
		contents.add(label2, c);
		c.gridy++;
		c.gridx = 0;
		contents.add(slider1, c);
		c.gridx++;
		contents.add(slider2, c);
		c.gridx = 0;
		c.gridy++;
		contents.add(enabled1, c);
		c.gridx++;
		contents.add(enabled2, c);

		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		add(contents, c);
		c.gridy++;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTH;
		add(zoomPanel, c);

		zoomPanel.setBorder(new EtchedBorder());

		slider1.setUI(new AngleSliderUI());
		slider2.setUI(new AquaAngleSliderUI());

		enabled1.addActionListener(this);
		enabled2.addActionListener(this);

		enabled1.setOpaque(false);
		enabled2.setOpaque(false);
	}

	public void actionPerformed(ActionEvent e) {
		slider1.setEnabled(enabled1.isSelected());
		slider2.setEnabled(enabled2.isSelected());
	}
}