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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import com.pump.geom.AbstractShape;
import com.pump.geom.ParametricPathIterator;
import com.pump.geom.Spiral2D;

/**
 * This demos the {@link Spiral2D} class
 * <p>
 * Here is a sample screenshot of this showcase demo:
 * <p>
 * <img src=
 * "https://github.com/mickleness/pumpernickel/raw/master/resources/showcase/Spiral2DDemo.png"
 * alt="A screenshot of the Spiral2DDemo.">
 */
public class Spiral2DDemo extends JPanel implements ShowcaseDemo {
	private static final long serialVersionUID = 1L;

	JLabel coilGapLabel = new JLabel("Coil Gap:");
	JSpinner coilGap = new JSpinner(new SpinnerNumberModel(50, 1, 300, 1));

	JLabel coilsLabel = new JLabel("Coils:");
	JSpinner coils = new JSpinner(new SpinnerNumberModel(1, .1, 100, .1));

	JLabel angleOffsetLabel = new JLabel("Angle Offset:");
	JLabel coilOffsetLabel = new JLabel("Coil Offset:");
	JSlider angleOffset = new JSlider();
	JSpinner coilOffset = new JSpinner(new SpinnerNumberModel(0, 0, 10, .01));

	JCheckBox outward = new JCheckBox("Outward");
	JCheckBox animate = new JCheckBox("Animate");
	JCheckBox clockwise = new JCheckBox("Clockwise");
	JCheckBox showFlattenedSpiral = new JCheckBox("Show Flattened Spiral");
	JCheckBox showControlPoints = new JCheckBox("Show Control Points");

	PreviewPanel preview = new PreviewPanel();

	ActionListener animateListener = new ActionListener() {
		private boolean increasingCoilOffset = true;
		private boolean decreasingCoilOffset = false;
		double coilIncr = .05;

		public void actionPerformed(ActionEvent e) {
			timerChange++;
			try {
				int angleValue = angleOffset.getValue();
				angleValue++;
				if (angleValue > angleOffset.getMaximum())
					angleValue = angleOffset.getMinimum();
				angleOffset.setValue(angleValue);

				if (increasingCoilOffset) {
					double coilValue = ((Number) coilOffset.getValue())
							.doubleValue();
					if (coilValue + coilIncr > 3) {
						decreasingCoilOffset = true;
						increasingCoilOffset = false;
						coilValue -= coilIncr;
					} else {
						coilValue += coilIncr;
					}
					coilOffset.setValue(coilValue);
				} else if (decreasingCoilOffset) {
					double coilValue = ((Number) coilOffset.getValue())
							.doubleValue();
					if (coilValue - coilIncr < 0) {
						increasingCoilOffset = true;
						decreasingCoilOffset = false;
						coilValue += coilIncr;
					} else {
						coilValue -= coilIncr;
					}
					coilOffset.setValue(coilValue);
				}
			} finally {
				timerChange--;
			}
		}
	};
	int timerChange = 0;
	Timer animateTimer = new Timer(50, animateListener);

	public Spiral2DDemo() {
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(3, 3, 3, 3);
		add(coilGapLabel, c);
		c.gridy++;
		add(coilsLabel, c);
		c.gridy++;
		add(angleOffsetLabel, c);
		c.gridy++;
		add(coilOffsetLabel, c);
		c.gridx++;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		add(coilGap, c);
		c.gridy++;
		add(coils, c);
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(angleOffset, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		add(coilOffset, c);
		c.gridy++;
		add(clockwise, c);
		c.gridy++;
		add(outward, c);
		outward.setToolTipText("This should produce no visual difference.");

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(preview, c);

		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(showFlattenedSpiral, c);
		c.gridy++;
		add(showControlPoints, c);
		c.gridy++;
		add(animate, c);

		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				preview.repaint();
				if (timerChange == 0) {
					if (e.getSource() == animate) {
						if (animate.isSelected()) {
							animateTimer.start();
						} else {
							animateTimer.stop();
						}
					} else if (animate.isSelected()) {
						animate.setSelected(false);
						animateTimer.stop();
					}
				}
			}
		};
		outward.addChangeListener(changeListener);
		animate.addChangeListener(changeListener);
		angleOffset.addChangeListener(changeListener);
		coilOffset.addChangeListener(changeListener);
		showFlattenedSpiral.addChangeListener(changeListener);
		showControlPoints.addChangeListener(changeListener);
		coilGap.addChangeListener(changeListener);
		coils.addChangeListener(changeListener);
		clockwise.addChangeListener(changeListener);

		coilGap.setEditor(new JSpinner.NumberEditor(coilGap, "#.00"));
		coils.setEditor(new JSpinner.NumberEditor(coils, "#.00"));
		coilOffset.setEditor(new JSpinner.NumberEditor(coilOffset, "0.00"));
	}

	class PreviewPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		MouseInputAdapter mouseListener = new MouseInputAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				double coilGapValue = ((Number) coilGap.getValue())
						.doubleValue();
				double coilsValue = ((Number) coils.getValue()).doubleValue();
				double centerX = getWidth() / 2.0;
				double centerY = getHeight() / 2.0;
				double endX = e.getX();
				double endY = e.getY();
				Spiral2D spiral;
				if (e.isShiftDown()) {
					spiral = Spiral2D.createWithFixedCoilCount(centerX,
							centerY, endX, endY, coilsValue);
				} else {
					spiral = Spiral2D.createWithFixedCoilGap(centerX, centerY,
							endX, endY, coilGapValue);
				}
				setSpiral(spiral);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mousePressed(e);
			}
		};

		public PreviewPanel() {
			setPreferredSize(new Dimension(200, 200));
			addMouseListener(mouseListener);
			addMouseMotionListener(mouseListener);
			setOpaque(false);
		}

		protected void setSpiral(Spiral2D spiral) {
			animate.setSelected(false);
			double fraction = ((spiral.getAngularOffset() + 4 * Math.PI) / (2 * Math.PI)) % 1.0;
			angleOffset.setValue((int) (fraction * 100));
			coilOffset.setValue(spiral.getCoilOffset());
			coilGap.setValue(new Double(spiral.getCoilGap()));
			coils.setValue(new Double(spiral.getCoils()));
			clockwise.setSelected(spiral.isClockwise());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			double coilGapValue = ((Number) coilGap.getValue()).doubleValue();
			double coilsValue = ((Number) coils.getValue()).doubleValue();
			double angleOffsetValue = (angleOffset.getValue() - angleOffset
					.getMinimum())
					* Math.PI
					* 2
					/ (angleOffset.getMaximum() - angleOffset.getMinimum());
			double coilOffsetValue = ((Number) coilOffset.getValue())
					.doubleValue();
			Spiral2D spiral = new Spiral2D(getWidth() / 2, getHeight() / 2,
					coilGapValue, coilsValue, angleOffsetValue,
					coilOffsetValue, clockwise.isSelected(),
					outward.isSelected());
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(1));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			if (showFlattenedSpiral.isSelected()) {
				g.setColor(Color.red);
				double r = (coilGapValue * coilOffsetValue);
				double lastX = spiral.getCenterX() + r
						* Math.cos(angleOffsetValue);
				double lastY = spiral.getCenterY() + r
						* Math.sin(angleOffsetValue);
				Line2D l = new Line2D.Double();
				double m = clockwise.isSelected() ? 1 : -1;
				for (double t = 0; t < coilsValue; t += .01) {
					r = (coilGapValue * (t + coilOffsetValue));
					double x = spiral.getCenterX() + r
							* Math.cos(m * t * Math.PI * 2 + angleOffsetValue);
					double y = spiral.getCenterY() + r
							* Math.sin(m * t * Math.PI * 2 + angleOffsetValue);
					l.setLine(lastX, lastY, x, y);
					g2.draw(l);
					lastX = x;
					lastY = y;
				}
				r = (coilGapValue * (coilsValue + coilOffsetValue));
				double x = spiral.getCenterX()
						+ r
						* Math.cos(m * coilsValue * Math.PI * 2
								+ angleOffsetValue);
				double y = spiral.getCenterY()
						+ r
						* Math.sin(m * coilsValue * Math.PI * 2
								+ angleOffsetValue);
				l.setLine(lastX, lastY, x, y);
				g2.draw(l);
			}

			g2.setColor(Color.blue);
			g2.draw(spiral);

			if (showControlPoints.isSelected()) {
				Ellipse2D ellipse = new Ellipse2D.Float();
				PathIterator i = spiral.getPathIterator(null);
				float[] coords = new float[6];
				while (i.isDone() == false) {
					int type = i.currentSegment(coords);
					if (type == PathIterator.SEG_CUBICTO) {
						g2.setColor(Color.green.darker());
						ellipse.setFrame(coords[0] - 2, coords[1] - 2, 4, 4);
						g2.draw(ellipse);
						g2.setColor(Color.orange.darker());
						ellipse.setFrame(coords[2] - 2, coords[3] - 2, 4, 4);
						g2.draw(ellipse);
					}
					i.next();
				}
			}
		}
	}

	@Override
	public String getTitle() {
		return "Spiral2D Demo";
	}

	@Override
	public String getSummary() {
		return "This demonstrates a new Shape class that represents a spiral.";
	}

	@Override
	public URL getHelpURL() {
		return Spiral2DDemo.class.getResource("spiral2Ddemo.html");
	}

	@Override
	public String[] getKeywords() {
		return new String[] { "spiral", "shape", "bezier", "parametric" };
	}

	@Override
	public Class<?>[] getClasses() {
		return new Class[] { Spiral2D.class, AbstractShape.class,
				ParametricPathIterator.class };
	}

}