package Resources;

import java.net.URL;

public class ResourceLoader {
 
    static ResourceLoader rl = new ResourceLoader();
     
    public static String getResourcePath(String filename) {
    	URL url = rl.getClass().getResource(filename);
    	
    	if (url == null) {
    		return new String();
    	}
    	
        return url.getPath();
    }
}