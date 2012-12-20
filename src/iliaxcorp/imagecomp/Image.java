package iliaxcorp.imagecomp;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Image {

	private MarvinImage img;
	
	private String path;
	
	public Image(String path){
		this.path = path;
		img = MarvinImageIO.loadImage(path);
	}
	
	public void saveImg(String path){
		MarvinImageIO.saveImage(img, path);
	}
	
	public void saveImg(){
		MarvinImageIO.saveImage(img, path);
	}
	
	public Color getColorAt(int x, int y){
		return new Color(img.getIntColor(x, y));
	}
	
	public int getW(){
		return img.getWidth();
	}
	
	public int getH(){
		return img.getHeight();
	}
	
	public void setColorAt(int x, int y, int color){
		img.setIntColor(x, y, color);
	}
	
	public MarvinImage getMarvinImage() {
		return img;
	}
	
}
