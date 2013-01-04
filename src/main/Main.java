package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.algs.DecoderAlg;
import iliaxcorp.imagecomp.algs.StorageInitializer;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.IOUtils;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Main {
	
	private static final boolean DEBUG = true;

	public static int iteration = 1; 
	
	public static void print(String s){
		System.out.println(s);
	}
	
	public static void debug(String s){
		if(DEBUG){
			System.err.println(s);
		}
	}
	
	static void initNS(){
		StorageInitializer si = new StorageInitializer(5000);
		NeuronStorage ns = si.processAlg(null);
		Map<String, String> m = new HashMap<String, String>();
		m.put("path", "neuron_store.ser");
		((iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage)ns).setParams(m);
		ns.persist();
		CoderAlg.ns = ns;
		print("Neuron Storage initialized!\n~~~~");
	}
	
	public static void main(String[] args) {
		//initNS();
		
		double L = 0.85;
		L = 0.33046633036309275;
		ImageInfo ii = null;
		Random rand = new Random(System.currentTimeMillis());
		for(int i=0; i<1000; i++){
			String [] imgPath = new String []{"test1.jpg" ,"test2.jpg" ,"test3.jpg"};
			String choosen = imgPath[rand.nextInt(imgPath.length)];
			CoderAlg c = new CoderAlg(choosen, L, true);
			ii = c.processAlg(null);
			L *= 0.99;
			
			if(iteration % 5 == 0){
				CoderAlg cсc = new CoderAlg(choosen, L, false);
				ImageInfo iii = cсc.processAlg(null);
				printImg(iii, choosen, iteration+"");
			}
			
			System.out.println("DONE! L=" + L + " - " + " iter = "+iteration);
			iteration++;
		}
		
		IOUtils.serializeObject("ii.ser", ii);
		System.out.println("\nDONE!!! " + L);
	}
	
	static void printImg(ImageInfo ii, String img,  String postf){
		MarvinImage mi = MarvinImageIO.loadImage(img);
		String path = "testOut"+postf+".png";
		if(!postf.isEmpty()){
			path = "outs//"+path;
		}
		MarvinImageIO.saveImage(mi, path);
		
		Image miOutAfter = new DecoderAlg(new Image(path), ii, CoderAlg.ns).processAlg(null); 
		miOutAfter.saveImg();
	}
	
}
