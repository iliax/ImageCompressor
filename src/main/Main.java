package main;

import java.util.HashMap;
import java.util.Map;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.algs.DecoderAlg;
import iliaxcorp.imagecomp.algs.StorageInitializer;
import iliaxcorp.imagecomp.algs.VectorQuantizer;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.IOUtils;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Main {
	public static NeuronStorage ns;

	public static int iteration = 0; 
	
	public static void print(String s){
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		new Main().testMain();
	/*
		StorageInitializer si = new StorageInitializer(1000, 4);
		NeuronStorage ns = si.processAlg();
		Map<String, String> m = new HashMap<String, String>();
		m.put("path", "neuron_store.ser");
		((iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage)ns).setParams(m);
		ns.persist();
		*/
		
		VectorQuantizer.L = 0.7531841195208;
		ImageInfo ii = null;
		for(int i=0; i<1500; i++){
			CoderAlg c = new CoderAlg("test1.jpg", true);
			ii = c.processAlg();
			
			VectorQuantizer.L *= 0.99;
			/*
			if(VectorQuantizer.L > 0.15){
				VectorQuantizer.L -= 0.003;
				//VectorQuantizer.L -= 0.008;
			} 
			else if(VectorQuantizer.L > 0.01){
				VectorQuantizer.L -= 0.0005;
			} else if(VectorQuantizer.L > 0.001){
				VectorQuantizer.L -= 0.00005;
			}
			*/
			if(iteration % 3 == 0){
				printImg(ii, iteration+"");
			}
			
			iteration++;
			System.out.println("DONE! L=" +VectorQuantizer.L + " - "+i + " iter = "+iteration);
		}
		
		IOUtils.serializeObject("ii.ser", ii);
		printImg(ii,"");
		System.out.println("\nDONE!!! " + VectorQuantizer.L);
	}
	
	static void printImg(ImageInfo ii, String postf){
		MarvinImage mi = MarvinImageIO.loadImage("test1.jpg");
		String path = "testOut"+postf+".jpg";
		if(!postf.isEmpty()){
			path = "outs//"+path;
		}
		MarvinImageIO.saveImage(mi, path);
		
		Image miOutAfter = new DecoderAlg(new Image(path), ii, ns).processAlg(); 
		MarvinImageIO.saveImage(miOutAfter.getMarvinImage(), path);	
	}

	void testMain(){
		MarvinImage mi = MarvinImageIO.loadImage("test1.jpg");
		int check = 0;
		for(int i = 0; i<mi.getHeight(); i++){
			for(int j=0; j < mi.getWidth(); j++){
				if(mi.getIntColor(j, i) >= 0){
					print("ololo");
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
