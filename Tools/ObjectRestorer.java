package Tools;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ObjectRestorer {
	private ObjectInputStream objectStream;
	
	public ObjectRestorer(String filepath) {		
		try {
			FileInputStream file = new FileInputStream(filepath);
	        objectStream = new ObjectInputStream(file);
		}
		catch (Exception e) {
            e.printStackTrace();
		}
	}

    public Object readNextObjectFromSave() {
    	try {
        	return objectStream.readObject();
    	}
	    catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
    }
    
    public void closeSaveFile() {
    	try {
	    	objectStream.close();
    	}
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
    }
}
