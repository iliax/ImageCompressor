package main;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationEvent;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.algs.CoderAlg;
import iliaxcorp.imagecomp.algs.DecoderAlg;
import iliaxcorp.imagecomp.algs.StorageInitializer;
import iliaxcorp.imagecomp.algs.VectorQuantizer;
import iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.IOUtils;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class Main {
	public static NeuronStorage ns;

	public static int iteration = 1; 
	
	public static void print(String s){
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		new Main().testMain();
/*
		StorageInitializer si = new StorageInitializer(500, 4);
		NeuronStorage ns = si.processAlg();
		Map<String, String> m = new HashMap<String, String>();
		m.put("path", "neuron_store.ser");
		((InMemoryNeuronStorage)ns).setParams(m);
		ns.persist();
*/
		
		VectorQuantizer.L = 0.8;
		ImageInfo ii = null;
		for(int i=0; i<100; i++){
			CoderAlg c = new CoderAlg("test1.jpg", true);
			ii = c.processAlg();
			
			if(VectorQuantizer.L > 0.15){
				VectorQuantizer.L -= 0.005;
			} 
			else if(VectorQuantizer.L > 0.01){
				VectorQuantizer.L -= 0.0006;
			} else if(VectorQuantizer.L > 0.0005){
				VectorQuantizer.L -= 0.00005;
			}
			
			if(iteration % 10 == 0){
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
//		for(int i = 0; i<mi.getHeight(); i++){
//			for(int j=0; j < mi.getWidth(); j++){
//				mi.setIntColor(j, i, -20061);
//			}
//		}
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
