package iliaxcorp.imagecomp;

import iliaxcorp.imagecomp.exceptions.ImageCompressionException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

	private BufferedImage img;
	
	private String path;
	
	public Image(String path){
		this.path = path;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			throw new ImageCompressionException(e);
		}
	}
	
	public void saveImg(){
		try {
			ImageIO.write(img, "PNG", new File(path));
		} catch (IOException e) {
			throw new ImageCompressionException(e);
		}
	}
	
	public Color getColorAt(int x, int y){
		Color c = new Color(img.getRGB(x, y));
		return c;
	}
	
	public int getW(){
		return img.getWidth();
	}
	
	public int getH(){
		return img.getHeight();
	}
	
	public void setColorAt(int x, int y, Color color){
		img.setRGB(x, y, color.getVal());
	}
	
}
