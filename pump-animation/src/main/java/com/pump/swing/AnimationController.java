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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.pump.blog.ResourceSample;
import com.pump.icon.DarkenedIcon;

/**
 * A strip of GUI components that control an animation. The default size is very
 * narrow; it should be stretched horizontally to fill the width of its
 * container.
 *
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/AnimationController/sample.png"
 * alt="new&#160;com.pump.swing.animation.AnimationController()"> <!-- ========
 * END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = "new com.pump.swing.animation.AnimationController()")
public class AnimationController extends JPanel {
	private static final long serialVersionUID = 1L;

	static Image playImage = Toolkit.getDefaultToolkit().createImage(
			AnimationController.class.getResource("playImage.png"));
	static Image pauseImage = Toolkit.getDefaultToolkit().createImage(
			AnimationController.class.getResource("pauseImage.png"));
	static ImageIcon playIcon = new ImageIcon(playImage);
	static ImageIcon pauseIcon = new ImageIcon(pauseImage);
	JButton playButton = new JButton(playIcon);
	private static int SLIDER_MAXIMUM = 1000;
	JSlider slider = new JSlider(0, SLIDER_MAXIMUM);

	/**
	 * A client property that maps to a Boolean indicating whether this
	 * controller should loop.
	 */
	public static String LOOP_PROPERTY = AnimationController.class.getName()
			+ ".loop";
	/**
	 * A client property that maps to a Number indicating the current time (in
	 * seconds).
	 */
	public static String TIME_PROPERTY = AnimationController.class.getName()
			+ ".time";
	/**
	 * A client property that maps to a Number indicating the current duration
	 * (in seconds).
	 */
	public static String DURATION_PROPERTY = AnimationController.class
			.getName() + ".duration";
	/**
	 * A client property that maps to a Boolean indicating whether this
	 * controller is playing.
	 */
	public static String PLAYING_PROPERTY = AnimationController.class.getName()
			+ ".playing";

	ActionListener buttonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (playButton.getIcon() == playIcon) {
				play();
			} else {
				pause();
			}
		}
	};

	static Timer timer = new Timer(1000 / 25, null);
	static {
		timer.start();
	}
	int adjustingSlider = 0;
	ChangeListener sliderListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			if (adjustingSlider > 0)
				return;
			float f = slider.getValue();
			f = f / (SLIDER_MAXIMUM);
			f = f * getDuration();
			setTime(f);
		}
	};

	public AnimationController() {
		this(new JButton[] {});
	}

	public AnimationController(JButton[] buttons) {
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		add(playButton, c);
		for (int a = 0; a < buttons.length; a++) {
			c.gridx++;
			add(buttons[a], c);
			buttons[a].setOpaque(false);
			buttons[a].setRolloverIcon(new DarkenedIcon(buttons[a], .5f));
			buttons[a].setPressedIcon(new DarkenedIcon(buttons[a], .75f));
			buttons[a].setBorder(new PartialLineBorder(Color.gray, new Insets(
					1, 0, 1, 1)));
		}
		c.weightx = 1;
		c.gridx++;
		c.fill = GridBagConstraints.BOTH;
		add(slider, c);
		playButton.setOpaque(false);
		playButton.setBorder(new LineBorder(Color.gray));
		playButton.setRolloverIcon(new DarkenedIcon(playButton, .5f));
		playButton.setPressedIcon(new DarkenedIcon(playButton, .75f));
		slider.setOpaque(false);
		slider.setBorder(new PartialLineBorder(Color.gray, new Insets(1, 0, 1,
				1)));
		Dimension d = slider.getPreferredSize();
		d.width = 60;
		d.height = 25;
		slider.setPreferredSize((Dimension) d.clone());
		d.width = d.height;
		playButton.setPreferredSize(d);
		for (int a = 0; a < buttons.length; a++) {
			buttons[a].setPreferredSize(d);
		}
		playButton.addActionListener(buttonListener);
		slider.setValue(0);
		setTime(0);
		slider.addChangeListener(sliderListener);

		InputMap inputMap = slider.getInputMap(JComponent.WHEN_FOCUSED);
		inputMap.put(KeyStroke.getKeyStroke(' '), "togglePlay");
		slider.getActionMap().put("togglePlay", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				playButton.doClick();
			}
		});

		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (DURATION_PROPERTY.equals(evt.getPropertyName())) {
					updateSlider();
				} else if (TIME_PROPERTY.equals(evt.getPropertyName())) {
					if (isPlaying()) {
						lastStartTime = System.currentTimeMillis()
								- (long) (getTime() * 1000);
					}
					updateSlider();
				} else if (PLAYING_PROPERTY.equals(evt.getPropertyName())) {
					if (isPlaying()) {
						lastStartTime = System.currentTimeMillis()
								- (long) (getTime() * 1000);
						timer.addActionListener(actionListener);
						playButton.setIcon(pauseIcon);
					} else {
						timer.removeActionListener(actionListener);
						playButton.setIcon(playIcon);
					}
				} else if ("enabled".equals(evt.getPropertyName())) {
					playButton.setEnabled(isEnabled());
					slider.setEnabled(isEnabled());
				}
			}
		});
	}

	protected void updateSlider() {
		float percent = getTime() / getDuration();
		int v = (int) (percent * SLIDER_MAXIMUM);
		if (v > SLIDER_MAXIMUM)
			v = SLIDER_MAXIMUM;
		adjustingSlider++;
		slider.setValue(v);
		adjustingSlider--;
	}

	private transient long lastStartTime = 0;
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (isPlaying() == false)
				return;

			float duration = getDuration();
			if (duration == 0)
				throw new RuntimeException(
						"Can't play an animation with a duration of 0 s.");
			long period = (long) (1000 * duration);
			long t = System.currentTimeMillis() - lastStartTime;
			if (isLooping()) {
				long k = t % period;
				float f = k;
				f = f / 1000f;
				setTime(f);
			} else {
				if (t < period) {
					float f = t;
					f = f / 1000f;
					setTime(f);
				} else {
					setTime(duration);
					pause();
				}
			}
		}
	};

	/** Returns the duration (in seconds) */
	public float getDuration() {
		Number n = (Number) getClientProperty(DURATION_PROPERTY);
		if (n == null)
			n = 1;
		return n.floatValue();
	}

	public void play() {
		if (isPlaying())
			return;

		if (Math.abs(getTime() - getDuration()) < .001) {
			setTime(0);
		}

		putClientProperty(PLAYING_PROPERTY, true);
	}

	public boolean isPlaying() {
		Boolean b = (Boolean) getClientProperty(PLAYING_PROPERTY);
		if (b == null)
			return false;
		return b;
	}

	public void pause() {
		putClientProperty(PLAYING_PROPERTY, false);
	}

	/** Return the playback slider. */
	public JSlider getSlider() {
		return slider;
	}

	/** Return the play/pause button. */
	public JButton getPlayButton() {
		return playButton;
	}

	public boolean isLooping() {
		Boolean b = (Boolean) getClientProperty(LOOP_PROPERTY);
		if (b == null)
			return false;
		return b;
	}

	public void setLooping(boolean b) {
		putClientProperty(LOOP_PROPERTY, b);
	}

	public void setDuration(float f) {
		putClientProperty(DURATION_PROPERTY, f);
	}

	public float getTime() {
		Number n = (Number) getClientProperty(TIME_PROPERTY);
		if (n == null)
			n = 0;
		return n.floatValue();
	}

	public void setTime(float f) {
		putClientProperty(TIME_PROPERTY, f);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(new GradientPaint(0, 0, Color.lightGray, 0, getHeight(),
				Color.white));
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
}