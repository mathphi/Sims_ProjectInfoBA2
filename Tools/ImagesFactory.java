package Tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImagesFactory implements Serializable {
	private static final long serialVersionUID = -7238205571545656262L;
	private static final String IMAGES_DIRECTORY_PATH = "src/Data/images";
	
	private static HashMap<String,BufferedImage> imagesDatabase = null;
	
	public static void loadAllImages() throws IOException {
		imagesDatabase = new HashMap<String,BufferedImage>();
		
		File folder = new File(IMAGES_DIRECTORY_PATH);
		loadDirectory(folder);
	}
	
	private static void loadDirectory(File folder) throws IOException {
		if (!folder.exists() || !folder.isDirectory()) {
			throw new IOException("Unable to open the images directory");
		}
		
		File[] filesList = folder.listFiles();
		
		for (File imgFile : filesList) {
			// Check if the file is a directory and load it where appropriate
			if (imgFile.isDirectory()) {
				loadDirectory(imgFile);
				continue;
			}
			
			// Check the file has an image extension
			if (!imgFile.getName().matches("^.*\\.(jpg|jpeg|png|gif)$"))
				continue;
			
			// Get the name without extension
			String name = imgFile.getName();
			int pos = name.lastIndexOf(".");
			if (pos > 0) {
			    name = name.substring(0, pos);
			}
			
			// Read the image content
			BufferedImage img = ImageIO.read(imgFile);
			
			imagesDatabase.put(name, img);
		}
	}
	
	public static BufferedImage getImage(String name) {
		if (imagesDatabase == null) {
			try {
				loadAllImages();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return imagesDatabase.getOrDefault(name, null);
	}
}
