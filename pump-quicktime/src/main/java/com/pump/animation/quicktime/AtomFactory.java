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
package com.pump.animation.quicktime;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.pump.io.GuardedInputStream;
import com.pump.io.IOUtils;
import com.pump.io.MeasuredInputStream;

/**
 * This is not a public class because I expect to make some significant changes
 * to this project in the next year.
 * <P>
 * Use at your own risk. This class (and its package) may change in future
 * releases.
 * <P>
 * Not that I'm promising there will be future releases. There may not be. :)
 */
class AtomFactory {
	private static boolean debug = false;

	/**
	 * This opens a file and parses the atoms. This is made private so the
	 * BlogUpdater doesn't pick it up and make a jar.
	 * 
	 * @param args
	 *            the application's arguments. (This is unused.)
	 */
	public static void main(String[] args) {
		Frame frame = new Frame();
		FileDialog fd = new FileDialog(frame);
		fd.setVisible(true);
		if (fd.getFile() == null)
			throw new NullPointerException();
		File file = new File(fd.getDirectory() + fd.getFile());
		try {
			Atom[] atom = AtomFactory.readAll(file);

			// can we write the same movie back verbatim?
			File tmp = File.createTempFile("copy", ".mov");
			FileOutputStream out = new FileOutputStream(tmp);
			try {
				for (int a = 0; a < atom.length; a++) {
					atom[a].write(out);
				}
				out.close();
				System.out.println("tmp: " + tmp.getAbsolutePath()
						+ ", tmp.length() = " + tmp.length()
						+ ", fil.length() = " + file.length());
				boolean equals = IOUtils.equals(file, tmp);
				System.out.println("write() accuracy: "
						+ (equals ? "passed" : "failed"));
				if (equals)
					tmp.delete();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("write() accuracy: failed");
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("finished");
	}

	public static synchronized Atom[] readAll(File file) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			MeasuredInputStream in2 = new MeasuredInputStream(in);
			List<Atom> v = new ArrayList<Atom>();
			while (in2.getReadBytes() < file.length()) {
				Atom atom = read(null, in2);
				v.add(atom);
			}
			return v.toArray(new Atom[v.size()]);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	static byte[] sizeArray = new byte[4];
	static byte[] bigSizeArray = new byte[8];
	static List<String> parentTypes = new ArrayList<String>();
	private static final String[] PARENT_NODES = new String[] { "moov", "udta",
			"trak", "edts", "mdia", "minf", "dinf", "stbl", "tref", "tapt" };
	static {
		for (int a = 0; a < PARENT_NODES.length; a++) {
			parentTypes.add(PARENT_NODES[a]);
		}
	}
	private static String padding = "";

	public static synchronized Atom read(Atom parent, InputStream in)
			throws IOException {
		long size = Atom.read32Int(in);
		if (in instanceof GuardedInputStream) {
			GuardedInputStream gis = (GuardedInputStream) in;
			long inputLimit = gis.getRemainingLimit() + 4;
			if (size > inputLimit) {
				System.err
						.println("AtomFactory: warning: expected size is too large ("
								+ size + ">" + inputLimit + ")");
				size = inputLimit;
			}
		}
		/**
		 * When in debugging mode, we make a copy of the incoming array and then
		 * test the Atom.write() method later by validating it against what we
		 * just read.
		 */
		byte[] debugCopy = null;
		if (debug) {
			int s = (int) size;
			try {
				debugCopy = new byte[s];
				debugCopy[0] = (byte) ((s >> 24) & 0xff);
				debugCopy[1] = (byte) ((s >> 16) & 0xff);
				debugCopy[2] = (byte) ((s >> 8) & 0xff);
				debugCopy[3] = (byte) (s & 0xff);
				Atom.read(in, debugCopy, 4, s - 4);
				in = new ByteArrayInputStream(debugCopy);
				Atom.read32Int(in);
			} catch (OutOfMemoryError e) {
				System.err
						.println("Tried to allocate array of " + s + " bytes");
				throw e;
			}
		}

		String type;
		try {
			type = Atom.read32String(in);
		} catch (IOException e) {
			type = null;
		}

		if (size == 0 || type == null) // yes, it can happen. Don't know why.
										// This kind of atom has no type.
			return new EmptyAtom(parent);

		if (size == 1) { // this is a special code indicating the size won't fit
							// in 4 bytes
			Atom.read(in, bigSizeArray);
			size = ((sizeArray[0] & 0xff) << 56)
					+ ((sizeArray[1] & 0xff) << 48)
					+ ((sizeArray[2] & 0xff) << 40)
					+ ((sizeArray[3] & 0xff) << 32)
					+ ((sizeArray[4] & 0xff) << 24)
					+ ((sizeArray[5] & 0xff) << 16)
					+ ((sizeArray[6] & 0xff) << 8)
					+ ((sizeArray[7] & 0xff) << 0);
		}

		Atom atom = null;

		if (parentTypes.contains(type)) {
			System.out.println(padding + type + ", " + size);
		}

		GuardedInputStream atomIn = new GuardedInputStream(in, size - 8, false);

		if (parentTypes.contains(type)) {
			String oldPadding = padding;
			padding = padding + "\t";
			atom = new ParentAtom(parent, type, atomIn);
			padding = oldPadding;
		} else if (type.equals("mvhd")) {
			atom = new MovieHeaderAtom(parent, atomIn);
		} else if (type.equals("mdhd")) {
			atom = new MediaHeaderAtom(parent, atomIn);
		} else if (type.equals("smhd")) {
			atom = new SoundMediaInformationHeaderAtom(parent, atomIn);
		} else if (type.equals("hdlr")) {
			atom = new HandlerReferenceAtom(parent, atomIn);
		} else if (type.equals("vmhd")) {
			atom = new VideoMediaInformationHeaderAtom(parent, atomIn);
		} else if (type.equals("tkhd")) {
			atom = new TrackHeaderAtom(parent, atomIn);
		} else if (type.equals("dref")) {
			atom = new DataReferenceAtom(parent, atomIn);
		} else if (type.equals("elst")) {
			atom = new EditListAtom(parent, atomIn);
		} else if (type.equals("edts")) {
			atom = new EditAtom(parent, atomIn);
		} else if (type.equals("stsd")) {
			if (parent == null)
				throw new NullPointerException();

			if (parent.getParent() != null
					&& ((Atom) parent.getParent())
							.getChild(VideoMediaInformationHeaderAtom.class) != null) {
				atom = new VideoSampleDescriptionAtom(parent, atomIn);
			} else if (parent.getParent() != null
					&& ((Atom) parent.getParent())
							.getChild(SoundMediaInformationHeaderAtom.class) != null) {
				atom = new SoundSampleDescriptionAtom(parent, atomIn);
			} else {
				atom = new SampleDescriptionAtom(parent, atomIn);
			}
		} else if (type.equals("stts")) {
			atom = new TimeToSampleAtom(parent, atomIn);
		} else if (type.equals("stsc")) {
			atom = new SampleToChunkAtom(parent, atomIn);
		} else if (type.equals("stsz")) {
			atom = new SampleSizeAtom(parent, atomIn);
		} else if (type.equals("stco")) {
			atom = new ChunkOffsetAtom(parent, atomIn);
		} else if (type.charAt(0) == 65449) {
			atom = new UserDataTextAtom(parent, type, atomIn);
		} else if (type.equals("WLOC")) {
			atom = new WindowLocationAtom(parent, atomIn);
		} else {
			boolean allLetters = true;
			for (int a = 0; a < type.length(); a++) {
				if (!Character.isLetterOrDigit(type.charAt(a)))
					allLetters = false;
			}
			if (!allLetters)
				System.err.println("AtomFactory: warning: unusual atom type \""
						+ type + "\", size = " + size);
			atom = new UnknownLeafAtom(parent, type, atomIn);
		}

		if (debug) {
			if (atom.getSize() != size) {
				System.err.println("Examine " + atom.getClass().getName()
						+ ", " + size + "!=" + atom.getSize());
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				atom.write(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] newCopy = out.toByteArray();

			for (int a = 0; a < debugCopy.length; a++) {
				if (debugCopy[a] != newCopy[a]) {
					ByteArrayInputStream in2 = new ByteArrayInputStream(newCopy);
					debug = false;
					Atom altAtom = read(parent, in2);
					debug = true;
					System.err.println(altAtom);
					System.err
							.println("written block unequal to parsed block ("
									+ atom.getClass().getName() + ")");
					System.err
							.println("\tdebugCopy[" + a + "] = " + debugCopy[a]
									+ ", newCopy[" + a + "] = " + newCopy[a]
									+ " (out of " + debugCopy.length + ")");
					break;
				}
				if (a == debugCopy.length - 1) {
					// System.err.println(atom.getClass().getName()+" passed inspection");
				}
			}
		}

		if (parentTypes.contains(type) == false) {
			System.out.println(padding + type + ", " + size + ", " + atom);
		}

		return atom;
	}
}