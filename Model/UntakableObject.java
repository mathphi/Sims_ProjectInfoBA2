package Model;

import java.awt.Color;

import Tools.Point;
import Tools.Size;

public abstract class UntakableObject extends GameObject{
	private static final long serialVersionUID = 8196057108781097015L;

	public UntakableObject(Point pos, Size sz, String name, int price) {
		super(pos, sz, Color.ORANGE);
		// TODO Auto-generated constructor stub
	}

}
