package iliaxcorp.imagecomp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.Main;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.algs.VectorQuantizer;
import iliaxcorp.imagecomp.algs.Vectorization;

//unused
public class MathUtils {

	static List<Double> diffs = new ArrayList<Double>();

	static List<P<Integer, Integer>> xy = new ArrayList<P<Integer,Integer>>();
	
	static int randCoordsCount = 250;
	
	static void genXY(Image ideal, Image result){
		Random rand = new Random(123456);
		int maxH = result.getH() - CoderAlg.BLOCK_SIZE - 10;
		int maxW = result.getW() - CoderAlg.BLOCK_SIZE - 10;
		for (int i = 0; i < randCoordsCount; i++) {
			int x = rand.nextInt(maxW);
			int y = rand.nextInt(maxH);
			xy.add(new P<Integer, Integer>(x,y));
		}
	}
	
	static public void printRandomDiff(Image ideal, Image result) {
		if(xy.isEmpty()){
			genXY(ideal, result);
		}
		double sum = 0;
		for (int i = 0; i < randCoordsCount; i++) {
			P<Integer, Integer> xyP = xy.get(i); 
			ColoredBlock cb1 = Vectorization.getBlock(xyP.a, xyP.b, ideal);
			ColoredBlock cb2 = Vectorization.getBlock(xyP.a, xyP.b, result);
			sum += VectorQuantizer.getDiff(cb1.block, cb2.block);
		}
		diffs.add(sum);
		IOUtils.serializeJsonObject("diffs.json", diffs);
		Main.print("\n~~~ Difference = " + sum + " ~~~\n");
	}

	static public double[] rgb_to_xyz(Color c) {
		double red = c.getRed();
		double green = c.getGreen();
		double blue = c.getBlue();

		double r = red / 255.0;
		double g = green / 255.0;
		double b = blue / 255.0;

		if (r > 0.04045) {
			r = (r + 0.055) / 1.055;
			r = Math.pow(r, 2.4);
		} else {
			r = r / 12.92;
		}

		if (g > 0.04045) {
			g = (g + 0.055) / 1.055;
			g = Math.pow(g, 2.4);
		} else {
			g = g / 12.92;
		}

		if (b > 0.04045) {
			b = (b + 0.055) / 1.055;
			b = Math.pow(b, 2.4);
		} else {
			b = b / 12.92;
		}

		r *= 100.0;
		g *= 100.0;
		b *= 100.0;

		double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
		double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
		double z = r * 0.0193 + g * 0.1192 + b * 0.9505;
		return new double[] { x, y, z };
	}

	static public double[] xyz_to_lab(double[] xyz) {
		double x = xyz[0] / 95.047;
		double y = xyz[1] / 100.0;
		double z = xyz[2] / 108.883;

		if (x > 0.008856) {
			x = Math.pow(x, 1.0 / 3.0);
		} else {
			x = 7.787 * x + 16.0 / 116.0;
		}

		if (y > 0.008856) {
			y = Math.pow(y, 1.0 / 3.0);
		} else {
			y = 7.787 * y + 16.0 / 116.0;
		}

		if (z > 0.008856) {
			z = Math.pow(z, 1.0 / 3.0);
		} else {
			z = 7.787 * z + 16.0 / 116.0;
		}

		double l = 116.0 * y - 16.0;
		double a = 500.0 * (x - y);
		double b = 200.0 * (y - z);

		return new double[] { l, a, b };
	}

	static public double getDiff_CIE1994(Color c1, Color c2) {
		double[] c1xyz = rgb_to_xyz(c1);
		double[] c2xyz = rgb_to_xyz(c2);

		double[] c1lab = xyz_to_lab(c1xyz);
		double[] c2lab = xyz_to_lab(c2xyz);

		return getDiff_CIE1994(c1lab, c2lab);
	}

	static public double getDiff_CIE1994(double[] lab1, double[] lab2) {
		double c1 = Math.sqrt(lab1[1] * lab1[1] + lab1[2] * lab1[2]);
		double c2 = Math.sqrt(lab2[1] * lab2[1] + lab2[2] * lab2[2]);

		double dc = c1 - c2;

		double dl = lab1[0] - lab2[0];
		double da = lab1[1] - lab2[1];
		double db = lab1[2] - lab2[2];
		double dh = Math.sqrt((da * da) + (db * db) - (dc * dc));

		double f = dl;
		double s = dc / (1.0 + 0.045 * c1);
		double t = dh / (1.0 + 0.015 * c1);

		return /* Math.sqrt */(f * f + s * s + t * t); // TODO sqrt disabled
	}
}
