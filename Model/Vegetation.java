package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Controller.ImagesFactory;
import Tools.Point;
import Tools.Size;

public abstract class Vegetation extends GameObject {
	private static final long serialVersionUID = 6204022128223112846L;
	
	private Size viewSize;

	public Vegetation(Point pos, Size size, Size viewSize) {
		super(pos, size, Color.green);
		this.viewSize = viewSize;
	}
	
	@Override
	public boolean isObstacle() {
		return true;
	}
	
	@Override
	public BufferedImage getCurrentImage() {		
		String imgID = String.format("%s", this.getClass().getSimpleName());
		return ImagesFactory.getImage(imgID);
	}

	@Override
	public void paint(Graphics g, int BLOC_SIZE) {
		BufferedImage img = getCurrentImage();
		
		// If we have an image, paint it
		if (img != null) {
			g.drawImage(
					img,
					(int)(getPos().getX() * BLOC_SIZE - (viewSize.getWidth() - getSize().getWidth()) / 2.0 * BLOC_SIZE - 1),
					(int)(getPos().getY() * BLOC_SIZE - (viewSize.getHeight() - getSize().getHeight()) * BLOC_SIZE - 1),
					viewSize.getWidth() * BLOC_SIZE,
					viewSize.getHeight() * BLOC_SIZE,
					null);
		}
		// Fallback coloured tiles
		else {
			g.setColor(getColor());
	        g.fillRect(
	        		(int)(getPos().getX() * BLOC_SIZE-1),
	        		(int)(getPos().getY() * BLOC_SIZE-1),
	        		(BLOC_SIZE * getSize().getWidth()),
	        		(BLOC_SIZE * getSize().getHeight()));
		}
	}
}
