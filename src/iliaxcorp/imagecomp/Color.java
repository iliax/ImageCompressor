package iliaxcorp.imagecomp;

import java.io.Serializable;

import iliaxcorp.imagecomp.utils.P3;

public class Color implements Serializable {

	private static final long serialVersionUID = 5391614136738005314L;

	public static P3<Integer, Integer, Integer> getRGBfromInt(int color) {
//		int Blue = color & 255;
//		int Green = (color >> 8) & 255;
//		int Red = (color >> 16) & 255;
		java.awt.Color c = new java.awt.Color(color);
		P3<Integer, Integer, Integer> p = 
				new P3<Integer, Integer, Integer> (c.getRed(), c.getGreen(), c.getBlue());
		return p;
	}

	public static int getIntFromRGB(int red, int green, int blue){
		
		//TODO diffenrent vals!
		//int rgb = ((red&0x0ff)<<16)|((green&0x0ff)<<8)|(blue&0x0ff);
		
		java.awt.Color c = new java.awt.Color(red, green, blue);
		/*if(c.getRGB() != rgb){
			System.out.print("rgb =" + rgb + " " + c.getRGB());
			System.exit(0);
		}*/
		return c.getRGB();
		//return rgb;
	}
	
	// ----------------------------------------------------------------------
	
	private int val;

	transient private boolean rgbCalculated = false;
	
	transient private int red;
	
	transient private int green;
	
	transient private int blue;
	
	public Color(int color) {
		val = color;
	}
	
	public Color(int r, int g, int b){
		red = r;
		green = g;
		blue = b;
		rgbCalculated = true;
		val = getIntFromRGB(r, g, b);
	}
	
	public int getVal() {
		return val;
	}
	
	public void setVal(int val) {
		this.val = val;
		rgbCalculated = false;
	}
	
	public int getRed() {
		loadRGB();
		return red;
	}
	
	public int getGreen() {
		loadRGB();
		return green;
	}
	
	public int getBlue() {
		loadRGB();
		return blue;
	}
	
	private void loadRGB(){
		if(!rgbCalculated){
			P3<Integer, Integer, Integer> res = getRGBfromInt(val);
			red = res.a;
			green = res.b;
			blue = res.c;
			rgbCalculated = true;
		}
	}
}
