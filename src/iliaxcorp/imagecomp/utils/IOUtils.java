package iliaxcorp.imagecomp.utils;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.exceptions.ImageCompressionException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;

public class IOUtils {

	static public void serializeObject(String path, Object obj) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			throw new ImageCompressionException(i);
		}
	}

	@SuppressWarnings("unchecked")
	static public <T> T deserializeObject(String path) {
		Object o = null;
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			o = in.readObject();
			in.close();
			fileIn.close();
			return (T) o;
		} catch (Exception i) {
			throw new ImageCompressionException(i);
		}
	}

	static public void serializeJsonObject(String path, Object obj) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			String json = new Gson().toJson(obj).toString();
			out.write(json);
			out.close();
		} catch (Exception e) {
			throw new ImageCompressionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	static public <T> T deserializeJsonObject(String path) {
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringBuffer sb = new StringBuffer();
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine);
			}
			in.close();
			return (T) new Gson().fromJson(sb.toString(), ImageInfo.class);
		} catch (Exception e) {
			throw new ImageCompressionException(e);
		}

	}
	
	public static Image loadImage(String path) {
		Image img = new Image(path);
		return img;
	}

}
