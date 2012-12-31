package main;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.algs.DecoderAlg;
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
	
	public static void main(String[] args) {
		new Main().testMain();
		
	/*
		StorageInitializer si = new StorageInitializer(2500, 4);
		NeuronStorage ns = si.processAlg(null);
		Map<String, String> m = new HashMap<String, String>();
		m.put("path", "neuron_store.ser");
		((iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage)ns).setParams(m);
		ns.persist();
		*/
		
		double L = 0.75;
	
		ImageInfo ii = null;
		for(int i=0; i<1000; i++){
			CoderAlg c = new CoderAlg("test1.jpg", L, true);
			ii = c.processAlg(null);
			
			L *= 0.99;
			if(iteration % 5 == 0){
				CoderAlg cсc = new CoderAlg("test1.jpg", L, false);
				ImageInfo iii = cсc.processAlg(null);
				printImg(iii, iteration+"");
			}
			
			System.out.println("DONE! L=" + L + " - "+i + " iter = "+iteration);
			iteration++;
		}
		
		IOUtils.serializeObject("ii.ser", ii);
		printImg(ii,"");
		System.out.println("\nDONE!!! " + L);
	}
	
	static void printImg(ImageInfo ii, String postf){
		MarvinImage mi = MarvinImageIO.loadImage("test1.jpg");
		String path = "testOut"+postf+".png";
		if(!postf.isEmpty()){
			path = "outs//"+path;
		}
		MarvinImageIO.saveImage(mi, path);
		
		Image miOutAfter = new DecoderAlg(new Image(path), ii, CoderAlg.ns).processAlg(null); 
		miOutAfter.saveImg();
	}

	void testMain(){
		MarvinImage mi = MarvinImageIO.loadImage("test1.jpg");
		int check = 0;
		for(int i = 0; i<mi.getHeight(); i++){
			for(int j=0; j < mi.getWidth(); j++){
				if(mi.getIntColor(j, i) >= 0){
					print("ololo ");
					check++;
				}
			}
		}
		if(check > 0){
			System.exit(0);
		}
		
		print("checked");
	}
	
}
