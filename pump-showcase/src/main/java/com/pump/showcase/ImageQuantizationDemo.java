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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;

import com.pump.blog.ResourceSample;
import com.pump.image.pixel.quantize.BiasedMedianCutColorQuantization;
import com.pump.image.pixel.quantize.ColorLUT;
import com.pump.image.pixel.quantize.ColorQuantization;
import com.pump.image.pixel.quantize.ColorSet;
import com.pump.image.pixel.quantize.ImageQuantization;
import com.pump.image.pixel.quantize.MedianCutColorQuantization;
import com.pump.inspector.InspectorGridBagLayout;
import com.pump.inspector.InspectorLayout;
import com.pump.io.SuffixFilenameFilter;
import com.pump.job.Job;
import com.pump.job.JobManager;
import com.pump.swing.ThrobberManager;

/**
 * A simple demo program that applies color and image quantization to an image.
 * A slider lets you configure the maximum number of colors (up to 256), and a
 * couple of other controls let you configure the quantization approach used.
 *
 *
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/QuantizationDemo/sample.png"
 * alt="new&#160;com.bric.image.pixel.quantize.QuantizationDemo(&#160;)"> <!--
 * ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = "new com.bric.image.pixel.quantize.QuantizationDemo( )")
public class ImageQuantizationDemo extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final String KEY_ORIGINAL_IMAGE = ImageQuantizationDemo.class
			+ "#originalImage";
	public static final String KEY_REDUCED_IMAGE = ImageQuantizationDemo.class
			+ "#reducedImage";
	public static final String KEY_ORIGINAL_COLOR_SET = ImageQuantizationDemo.class
			+ "#colorSet";
	public static final String KEY_REDUCED_COLOR_SET = ImageQuantizationDemo.class
			+ "#reducedColorSet";

	/** A map of color cell bounds to the color they represent. */
	Map<Rectangle, Color> cells = new HashMap<Rectangle, Color>();

	/** The upper panel of controls. */
	JPanel controls = new JPanel(new GridBagLayout());

	/** The panel that shows the current reduced image. */
	JPanel contentPanel = new JPanel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage reducedImage = (BufferedImage) ImageQuantizationDemo.this
					.getClientProperty(KEY_REDUCED_IMAGE);
			if (reducedImage != null) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.drawImage(reducedImage, 0, 0, null);
				g2.dispose();
			}
		}
	};

	JScrollPane contentScrollPane = new JScrollPane(contentPanel);

	/** The size of each color cell. */
	int CELL_SIZE = 16;

	JLabel fileLabel = new JLabel("File:");
	JTextField filePath = new JTextField();
	JButton browseButton = new JButton("Browse");
	JLabel colorCountLabel = new JLabel("Color Count:");

	/** The panel on the right that shows the current color palette. */
	JPanel pixelPanel = new JPanel() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (Rectangle r : cells.keySet()) {
				Color c = cells.get(r);
				g.setColor(c);
				g.fillRect(r.x, r.y, r.width, r.height);
			}
		}
	};
	JSlider reductionSlider = new JSlider(2, 256);
	JComboBox<String> reductionType = new JComboBox<String>();
	JComboBox<ImageQuantization> quantizationType = new JComboBox<ImageQuantization>();
	JLabel quantizationLabel = new JLabel("Quantization:");
	JobManager jobManager = new JobManager(1);
	ThrobberManager throbberManager = new ThrobberManager(jobManager);

	public ImageQuantizationDemo() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(controls, c);
		c.gridy++;
		c.weighty = 1;
		c.gridwidth = 1;
		add(contentScrollPane, c);
		c.gridx++;
		c.weightx = 0;
		add(pixelPanel, c);

		InspectorLayout layout = new InspectorGridBagLayout(controls);
		layout.addRow(fileLabel, filePath, true, browseButton);
		layout.addRow(colorCountLabel, reductionSlider, true, reductionType);
		layout.addRow(quantizationLabel, quantizationType, false,
				throbberManager.createThrobber());

		reductionType.addItem("Biased");
		reductionType.addItem("Median Cut (Simplest)");

		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = browseForFile("jpg", "jpeg", "png");
				if (f == null)
					return;
				setFile(f);
			}
		});
		filePath.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			public void changedUpdate(DocumentEvent e) {
				File f = new File(filePath.getText());
				if (f.exists()) {
					setFile(f);
				}
			}

		});

		reductionSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jobManager.addJob(new QuantizeOriginalColorSetJob());
			}
		});
		Dimension d = new Dimension(400, 400);
		contentScrollPane.setPreferredSize(d);
		pixelPanel
				.setPreferredSize(new Dimension(8 * CELL_SIZE, 32 * CELL_SIZE));
		pixelPanel.setMinimumSize(new Dimension(8 * CELL_SIZE, 32 * CELL_SIZE));

		pixelPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				jobManager.addJob(new ResizePixelCellsJob());
			}
		});

		quantizationType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jobManager.addJob(new QuantizeImageJob());
			}
		});

		reductionType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jobManager.addJob(new QuantizeOriginalColorSetJob());
			}
		});

		pixelPanel.addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				String s = null;
				for (Rectangle r : cells.keySet()) {
					if (r.contains(p)) {
						Color c = cells.get(r);
						s = c.getRed() + ", " + c.getGreen() + ", "
								+ c.getBlue();
					}
				}
				pixelPanel.setToolTipText(s);
			}
		});

		quantizationType.addItem(ImageQuantization.MOST_DIFFUSION);
		quantizationType.addItem(ImageQuantization.MEDIUM_DIFFUSION);
		quantizationType.addItem(ImageQuantization.SIMPLEST_DIFFUSION);
		quantizationType.addItem(ImageQuantization.NEAREST_NEIGHBOR);

		addPropertyChangeListener(KEY_ORIGINAL_IMAGE,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						jobManager.addJob(new UpdateOriginalColorSetJob());
					}
				});

		addPropertyChangeListener(KEY_ORIGINAL_COLOR_SET,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						jobManager.addJob(new QuantizeOriginalColorSetJob());
					}
				});

		addPropertyChangeListener(KEY_REDUCED_COLOR_SET,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						jobManager.addJob(new QuantizeImageJob());
					}
				});

		addPropertyChangeListener(KEY_REDUCED_IMAGE,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						BufferedImage reducedImage = (BufferedImage) getClientProperty(KEY_REDUCED_IMAGE);
						contentPanel.setPreferredSize(new Dimension(
								reducedImage.getWidth(), reducedImage
										.getHeight()));
						contentPanel.repaint();
						contentScrollPane.revalidate();
					}
				});
	}

	/**
	 * If invoked from within a Frame: this pulls up a FileDialog to browse for
	 * a file. If this is invoked from a secure applet: then this will throw an
	 * exception.
	 * 
	 * @param ext
	 *            an optional list of extensions
	 * @return a File, or null if the user declined to select anything.
	 */
	public File browseForFile(String... ext) {
		Window w = SwingUtilities.getWindowAncestor(this);
		if (!(w instanceof Frame))
			throw new IllegalStateException();
		Frame frame = (Frame) w;
		FileDialog fd = new FileDialog(frame);
		if (ext != null && ext.length > 0
				&& (!(ext.length == 1 && ext[0] == null)))
			fd.setFilenameFilter(new SuffixFilenameFilter(ext));
		fd.pack();
		fd.setLocationRelativeTo(null);
		fd.setVisible(true);
		String d = fd.getFile();
		if (d == null)
			return null;
		return new File(fd.getDirectory() + fd.getFile());
	}

	protected void setFile(File f) {
		String s = f.getAbsolutePath();
		if (!filePath.getText().equals(s)) {
			filePath.setText(s);
		}
		try {
			BufferedImage originalImage = ImageIO.read(f);
			putClientProperty(KEY_ORIGINAL_IMAGE, originalImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class QuantizeOriginalColorSetJob extends Job {

		@Override
		public String getReplacementId() {
			return "quantize original color set";
		}

		@Override
		protected void runJob() throws Exception {
			ColorSet originalImageColors = (ColorSet) getClientProperty(KEY_ORIGINAL_COLOR_SET);
			if (originalImageColors != null) {
				ColorQuantization cq = null;
				if (reductionType.getSelectedIndex() == 0) {
					cq = new BiasedMedianCutColorQuantization(.1f);
				} else {
					cq = new MedianCutColorQuantization();
				}
				ColorSet reducedImageColors = cq.createReducedSet(
						originalImageColors, reductionSlider.getValue(), true);
				putClientProperty(KEY_REDUCED_COLOR_SET, reducedImageColors);
			}
		}
	}

	class UpdateOriginalColorSetJob extends Job {
		@Override
		protected void runJob() throws Exception {
			BufferedImage originalImage = (BufferedImage) getClientProperty(KEY_ORIGINAL_IMAGE);
			ColorSet originalImageColors = new ColorSet();
			if (originalImage != null) {
				originalImageColors.addColors(originalImage);
				putClientProperty(KEY_ORIGINAL_COLOR_SET, originalImageColors);
			}
		}

		@Override
		public String getReplacementId() {
			return "update original color list";
		}
	}

	class QuantizeImageJob extends Job {
		@Override
		public String getReplacementId() {
			return "quantize image";
		}

		@Override
		protected void runJob() throws Exception {
			ColorSet reducedImageColors = (ColorSet) getClientProperty(KEY_REDUCED_COLOR_SET);
			BufferedImage originalImage = (BufferedImage) getClientProperty(KEY_ORIGINAL_IMAGE);
			if (reducedImageColors != null && originalImage != null) {
				IndexColorModel icm = reducedImageColors.createIndexColorModel(
						false, false);
				ColorLUT lut = new ColorLUT(icm);
				BufferedImage reducedImage = ((ImageQuantization) quantizationType
						.getSelectedItem()).createImage(originalImage, lut);
				putClientProperty(KEY_REDUCED_IMAGE, reducedImage);
				jobManager.addJob(new ResizePixelCellsJob());
			}
		}
	}

	Comparator<Color> hueComparator = new Comparator<Color>() {

		public int compare(Color c1, Color c2) {
			float[] hsb1 = new float[3];
			float[] hsb2 = new float[3];
			Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), hsb1);
			Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), hsb2);
			if (hsb1[0] < hsb2[0]) {
				return -1;
			} else if (hsb1[0] > hsb2[0]) {
				return 1;
			} else if (hsb1[1] < hsb2[1]) {
				return -1;
			} else if (hsb1[1] > hsb2[1]) {
				return 1;
			} else if (hsb1[2] < hsb2[2]) {
				return -1;
			} else if (hsb1[2] > hsb2[2]) {
				return 1;
			}
			return 0;
		}

	};

	Comparator<Color> brightnessComparator = new Comparator<Color>() {

		public int compare(Color c1, Color c2) {
			float[] hsb1 = new float[3];
			float[] hsb2 = new float[3];
			Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), hsb1);
			Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), hsb2);
			if (hsb1[2] < hsb2[2]) {
				return -1;
			} else if (hsb1[2] > hsb2[2]) {
				return 1;
			} else if (hsb1[0] < hsb2[0]) {
				return -1;
			} else if (hsb1[0] > hsb2[0]) {
				return 1;
			} else if (hsb1[1] < hsb2[1]) {
				return -1;
			} else if (hsb1[1] > hsb2[1]) {
				return 1;
			}
			return 0;
		}

	};

	class ResizePixelCellsJob extends Job {
		@Override
		public String getReplacementId() {
			return "resize pixel cells";
		}

		@Override
		protected void runJob() throws Exception {
			ColorSet reducedImageColors = (ColorSet) getClientProperty(KEY_REDUCED_COLOR_SET);

			int x = 0;
			int y = 0;
			cells.clear();
			if (reducedImageColors != null) {
				Color[] color = reducedImageColors.getColors();

				/**
				 * This is a crude hard-to-explain approach to sorting colors
				 * into clusters that humans find reasonably appealing. (That
				 * is: without a 3D representation it's hard to express a series
				 * of 3D-data points in 2D space.)
				 */

				Set<Color> tier1 = new TreeSet<Color>(hueComparator);
				Set<Color> tier2 = new TreeSet<Color>(hueComparator);
				Set<Color> tier3 = new TreeSet<Color>(brightnessComparator);

				for (int a = 0; a < color.length; a++) {
					float[] hsb = new float[3];
					Color.RGBtoHSB(color[a].getRed(), color[a].getGreen(),
							color[a].getBlue(), hsb);
					float k = hsb[1] + hsb[2];
					if (k > 1.2) {
						tier1.add(color[a]);
					} else if (k > .7) {
						tier2.add(color[a]);
					} else {
						tier3.add(color[a]);
					}
				}

				for (int a = 0; a < 3; a++) {
					Set<Color> set = null;
					if (a == 0) {
						set = tier1;
					} else if (a == 1) {
						set = tier2;
					} else if (a == 2) {
						set = tier3;
					}
					Color[] array = set.toArray(new Color[set.size()]);
					for (int b = 0; b < array.length; b++) {
						cells.put(new Rectangle(x, y, CELL_SIZE, CELL_SIZE),
								array[b]);

						x += CELL_SIZE;
						if (x >= pixelPanel.getWidth()) {
							x = 0;
							y += CELL_SIZE;
						}
					}
				}
			}
			pixelPanel.repaint();
		}

	}
}