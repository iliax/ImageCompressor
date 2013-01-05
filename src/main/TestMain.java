package main;

import java.util.Random;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.utils.IOUtils;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file = "test1.jpg";
		CoderAlg cсc = new CoderAlg(file, 0.01, false);
		ImageInfo iii = cсc.processAlg(null);
		IOUtils.serializeObject("iii.ser", iii);
		IOUtils.serializeJsonObject("iii.json", iii);
		Main.printImg(iii, file, "");
	}

}
