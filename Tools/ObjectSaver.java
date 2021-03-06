package Tools;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class ObjectSaver {
	private ObjectOutputStream objectStream;
	
	public ObjectSaver(String filepath) {
		try {
	        FileOutputStream file = new FileOutputStream(filepath);
	        objectStream = new ObjectOutputStream(file);
		}
		catch (Exception e) {
            e.printStackTrace();
		}
	}
	
    public void addObjectToSave(Object obj) {
    	try {
        	objectStream.writeObject(obj);
    	}
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
    }
    
    public void writeSaveToFile() {
    	try {
	    	objectStream.close();
    	}
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
    }
}
