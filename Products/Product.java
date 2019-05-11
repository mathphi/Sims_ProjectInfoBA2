package Products;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

import Controller.ImagesFactory;
import Tools.Size;

public abstract class Product implements Serializable {
	private static final long serialVersionUID = 3734644445361401634L;
	
	private int price;
	private String name;
	private String description;
	
	private String imgID;

	protected int moodImpact = 0;
	protected int energyImpact = 0;
	protected int hungerImpact = 0;
	protected int hygieneImpact = 0;
	protected int generalKnowledgeImpact = 0;
	protected int otherImpressionImpact = 0;

	public Product(String name, String description, int price, String imgID) {
		this.name = name;
		this.price = price;
		this.description = description;
		
		this.imgID = imgID;
	}

	public int getMoodImpact() {
		return moodImpact;
	}
	
	public int getEnergyImpact() {
		return energyImpact;
	}
	
	public int getHungerImpact() {
		return hungerImpact;
	}
	
	public int getHygieneImpact() {
		return hygieneImpact;
	}
	
	public int getGeneralKnowledgeImpact() {
		return generalKnowledgeImpact;
	}
	
	public int getOtherImpressionImpact() {
		return otherImpressionImpact;
	}
	
	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ImageIcon getIcon(Size s) {
		return new ImageIcon(
				ImagesFactory.getImage(imgID)
					.getScaledInstance(s.getWidth(), s.getWidth(), Image.SCALE_SMOOTH));
	}
}
