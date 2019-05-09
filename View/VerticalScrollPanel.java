package View;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;

/**
 * This class is made to lock the horizontal scrolling in a JScrollPane.
 * Because the JScrollPane is not able to disable a scroll direction (it can only hide it).
 */
public class VerticalScrollPanel extends JPanel implements Scrollable {
	private static final long serialVersionUID = -7652681497946095647L;

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(200, 200);
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 128;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 128;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		boolean track = true;
		Container parent = getParent();
		if (parent instanceof JViewport) {
			JViewport viewport = (JViewport) parent;
			if (viewport.getHeight() < getPreferredSize().height) {
				track = false;
			}
		}

		return track;
	}
}
