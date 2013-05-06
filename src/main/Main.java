package main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.algs.DecoderAlg;
import iliaxcorp.imagecomp.algs.ImageNSInitializer;
import iliaxcorp.imagecomp.algs.RandomStorageInitializer;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.IOUtils;
import iliaxcorp.imagecomp.utils.MathUtils;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Main {

	private static final boolean DEBUG = false;

	public static int iteration = 1;

	public static void print(String s) {
		System.out.println(s);
	}

	public static void debug(String s) {
		if (DEBUG) {
			System.err.println(s);
		}
	}

	static void initNS() {
		ImageNSInitializer si = new ImageNSInitializer("test2.jpg", 2000);
		NeuronStorage ns = si.processAlg(null);
		Map<String, String> m = new HashMap<String, String>();
		m.put("path", "neuron_store.ser");
		((iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage) ns)
				.setParams(m);
		ns.persist();
		CoderAlg.ns = ns;
		print("Neuron Storage initialized!\n~~~~");
	}

	static void initNS2() {
		RandomStorageInitializer init = new RandomStorageInitializer(2000);
		NeuronStorage ns = init.processAlg(null);
		Map<String, String> m = new HashMap<String, String>();
		m.put("path", "neuron_store.ser");
		((iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage) ns)
				.setParams(m);
		ns.persist();
		CoderAlg.ns = ns;
		print("Neuron Storage initialized!\n~~~~");
	}

	public static void main(String[] args) {
		initNS();

		 CoderAlg cсc1 = new CoderAlg("test6.jpg", 0.0, false);
		 ImageInfo iii1 = cсc1.processAlg(null);
		 IOUtils.serializeObject("ii.ser", iii1);
		 printImg(iii1, "test6.jpg", iteration + "");
		 MathUtils.printRandomDiff(new Image("test6.jpg"), new Image(
		 "outs//testOut" + iteration + ".png"));
		System.exit(0);
		
		double L = 0.8;
		ImageInfo ii = null;
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < 1000; i++) {
			String[] imgPath = new String[] { "test_1.jpg"/*, "test_2.jpg", "test4.jpg" */};

			String choosen = null;
			for (String f : imgPath) {
				choosen = f;
				CoderAlg c = new CoderAlg(choosen, L, true);
				ii = c.processAlg(null);
			}

			L *= 0.99;
			if (L < 0.2) {
				break;
			}

			if (iteration % 5 == 0) {
				CoderAlg cсc = new CoderAlg("test_1.jpg", L, false);
				ImageInfo iii = cсc.processAlg(null);
				IOUtils.serializeObject("ii.ser", iii);
				printImg(iii, "test_1.jpg", iteration + "");
				MathUtils.printRandomDiff(new Image("test_1.jpg"), new Image(
						"outs//testOut" + iteration + ".png"));
			}

			print("DONE! " + new Date() + " L=" + L + " - " + "iter = "
					+ iteration);
			iteration++;
		}

		IOUtils.serializeObject("ii.ser", ii);
		System.out.println("\nDONE!!! " + L);
	}

	static void printImg(ImageInfo ii, String img, String postf) {
		MarvinImage mi = MarvinImageIO.loadImage(img);
		String path = "testOut" + postf + ".png";
		if (!postf.isEmpty()) {
			path = "outs//" + path;
		}
		MarvinImageIO.saveImage(mi, path);

		Image miOutAfter = new DecoderAlg(new Image(path), ii, CoderAlg.ns)
				.processAlg(null);
		miOutAfter.saveImg();
	}

}
