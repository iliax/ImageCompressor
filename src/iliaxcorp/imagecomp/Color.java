package iliaxcorp.imagecomp;

import iliaxcorp.imagecomp.utils.P3;

public class Color {

	public static P3<Integer, Integer, Integer> getRGBfromInt(int color) {
		int Blue = color & 255;
		int Green = (color >> 8) & 255;
		int Red = (color >> 16) & 255;
		P3<Integer, Integer, Integer> p = 
				new P3<Integer, Integer, Integer> (Red, Green, Blue);
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
	
	public int val;

	public Color(int color) {
		val = color;
	}
}
