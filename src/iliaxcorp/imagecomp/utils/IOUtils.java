package iliaxcorp.imagecomp.utils;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.exceptions.ImageCompressionException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class IOUtils {

	static public void serializeObject(String path, Object obj) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
			throw new ImageCompressionException();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	static public <T> T deserializeObject(String path){
		Object o =null;
		try {
           FileInputStream fileIn =
                         new FileInputStream(path);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           o = in.readObject();
           in.close();
           fileIn.close();
           return (T)o;
       } catch(Exception i){
    	   i.printStackTrace();
			throw new ImageCompressionException();
       }
	}

	
	public static Image loadImage(String path){
		Image img = new Image(path);
		return img;
	}
	
}
