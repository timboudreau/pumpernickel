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
package com.pump.plaf;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.pump.UserCancelledException;
import com.pump.audio.AudioPlayer;
import com.pump.audio.AudioPlayer.StartTime;
import com.pump.swing.AudioPlayerComponent;

public class AudioPlayerUI extends ComponentUI {

	private static class EDTListener implements AudioPlayer.Listener {
		AudioPlayerComponent apc;

		EDTListener(AudioPlayerComponent apc) {
			this.apc = apc;
		}

		public void playbackStarted() {
			apc.getUI().notifyPlaybackStarted(apc);
		}

		public void playbackProgress(float timeElapsed, float timeAsFraction) {
			apc.getUI()
					.notifyPlaybackProgress(apc, timeElapsed, timeAsFraction);
		}

		public void playbackStopped(Throwable t) {
			apc.getUI().notifyPlaybackStopped(apc, t);
		}

	};

	protected void notifyPlaybackStarted(AudioPlayerComponent apc) {
	}

	protected void notifyPlaybackProgress(AudioPlayerComponent apc,
			float timeElapsed, float timeAsFraction) {
	}

	protected void notifyPlaybackStopped(AudioPlayerComponent apc, Throwable t) {
	}

	PropertyChangeListener audioPlayerListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof AudioPlayerComponent) {
				AudioPlayerComponent apc = (AudioPlayerComponent) evt
						.getSource();
				String key = AudioPlayerUI.class.getName() + ".edtListener";
				EDTListener edtListener = (EDTListener) apc
						.getClientProperty(key);
				if (edtListener == null) {
					edtListener = new EDTListener(apc);
					apc.putClientProperty(key, edtListener);
				}

				AudioPlayer oldPlayer = (AudioPlayer) evt.getOldValue();
				AudioPlayer newPlayer = (AudioPlayer) evt.getNewValue();
				if (oldPlayer != null)
					oldPlayer.removeEDTListener(edtListener);
				if (newPlayer != null)
					newPlayer.addEDTListener(edtListener, 10);
			}
		}
	};

	protected void doPause(AudioPlayerComponent apc) {
		apc.pause();
	}

	protected void doPlay(AudioPlayerComponent apc, StartTime startTime) {
		apc.play(startTime);
	}

	protected void doBrowseForFile(AudioPlayerComponent apc) {
		Window w = SwingUtilities.getWindowAncestor(apc);
		if (!(w instanceof Frame))
			throw new RuntimeException(
					"cannot invoke a FileDialog if the player is not in a java.awt.Frame");
		// the button shouldn't be enabled if w isn't a Frame...
		Frame f = (Frame) w;
		FileDialog fd = new FileDialog(f);
		fd.pack();
		fd.setLocationRelativeTo(null);
		fd.setVisible(true);

		if (fd.getFile() == null)
			throw new UserCancelledException();
		File file = new File(fd.getDirectory() + fd.getFile());
		try {
			apc.setSource(file.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			apc.setSource(null);
		}
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		installDefaultSource((AudioPlayerComponent) c);
		c.addPropertyChangeListener(AudioPlayerComponent.PLAYER_KEY,
				audioPlayerListener);
	}

	@Override
	public void uninstallUI(JComponent c) {
		c.removePropertyChangeListener(AudioPlayerComponent.PLAYER_KEY,
				audioPlayerListener);
	}

	protected void installDefaultSource(AudioPlayerComponent c) {
		try {
			c.setSource(new URL(
					"https://java.net/projects/javagraphics/sources/svn/content/trunk/src/com/bric/swing/resources/Ludic.wav"));
		} catch (MalformedURLException e) {
			c.setSource(null);
			e.printStackTrace();
		}
	}
}