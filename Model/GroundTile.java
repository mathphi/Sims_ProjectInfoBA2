package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Controller.ImagesFactory;
import Tools.Point;
import Tools.Size;

public class GroundTile extends GameObject {
	private static final long serialVersionUID = -5500377942506217655L;

	private String type;
	
	public GroundTile(Point pos, String type, int size) {
		super(pos, new Size(size, size), new Color(230, 191, 131));
		this.type = type;
	}
	
	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public GameObject clone() {
		return (GameObject) new GroundTile(getPos(), type, getSize().getWidth());
	}
	
	@Override
	public BufferedImage getCurrentImage() {
		String orient = "H";
		
		if (getDirection() == Direction.EAST || getDirection() == Direction.WEST) {
			orient = "V";
		}
		
		String imgID = String.format("%s_%s_%s", this.getClass().getSimpleName(), type, orient);
		BufferedImage img = ImagesFactory.getImage(imgID);
		
		if (img == null) {
			// Try to get the default image
			imgID = String.format("%s_%s", this.getClass().getSimpleName(), type);
			img = ImagesFactory.getImage(imgID);
		}
		
		return img;
	}

	@Override
	public void paint(Graphics g, int BLOC_SIZE) {
		BufferedImage img = getCurrentImage();
		
		// If we have an image, paint it repeatedly
		if (img != null) {
			for (int i = 0 ; i < getSize().getWidth() ; i++) {
				for (int j = 0 ; j < getSize().getHeight() ; j++) {
					g.drawImage(
							img,
							(int)(getPos().getX() * BLOC_SIZE + i * BLOC_SIZE - 1),
							(int)(getPos().getY() * BLOC_SIZE + j * BLOC_SIZE - 1),
							BLOC_SIZE,
							BLOC_SIZE,
							null);
				}
			}
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
