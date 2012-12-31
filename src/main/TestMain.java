package main;

import java.util.Random;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.algs.CoderAlg;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Image img = new Image("test1.jpg");
		Color black = new Color(java.awt.Color.black.getRGB());
		for(int i = 0; i < 100; i ++){
			int ii = i * 4;
			for(int j = 0; j < 100; j++){
				int jj = j*4;
				ColoredBlock cb = new ColoredBlock();
				for(int l=0; l<16; l++){
					cb.block.add(black);
				}
				addBlock(img, jj, ii,cb);
			}
		}
		img.saveImg();
		
		
		Color grey = new Color(java.awt.Color.DARK_GRAY.getRGB());
		Color red = new Color(java.awt.Color.RED.getRGB());
		Color blue = new Color(java.awt.Color.BLUE.getRGB());
		Color yell = new Color(java.awt.Color.YELLOW.getRGB());
		
		Color[] cls = new Color[]{grey, red, blue , yell};
		
		Random r = new Random(System.currentTimeMillis());
		for(int i = 0; i < 100; i ++){
			int ii = i * 4;
			for(int j = 0; j < 100; j++){
				int jj = j*4;
				ColoredBlock cb = new ColoredBlock();
				Color c =(cls[r.nextInt(cls.length-1)]);
				for(int l=0; l<16; l++){
					cb.block.add(c);
				}
				addBlock(img, jj, ii,cb);
				//img.saveImg();
			}
		}
		img.saveImg();
	}

	static void addBlock(Image img, int x, int y, ColoredBlock cb){
		int link=0;
		for(int i=y; i<y+CoderAlg.BLOCK_SIZE; i++){
			for(int j=x; j<x+CoderAlg.BLOCK_SIZE; j++){
				img.setColorAt(j, i, cb.block.get(link++));
			}
		}
	}
}
