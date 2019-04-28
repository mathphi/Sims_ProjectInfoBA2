package Tools;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ObjectRestorer {
	private ObjectInputStream objectStream;
	private int openedVersion;
	
	public ObjectRestorer(String filepath) {		
		try {
			FileInputStream file = new FileInputStream(filepath);
	        objectStream = new ObjectInputStream(file);
	        
	        openedVersion = objectStream.readInt();
		}
		catch (Exception e) {
            e.printStackTrace();
		}
	}
	
	public boolean versionMatches(int versionID) {
		return openedVersion == versionID;
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
