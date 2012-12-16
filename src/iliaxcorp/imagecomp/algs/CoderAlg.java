package iliaxcorp.imagecomp.algs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Main;

import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.IOUtils;
import iliaxcorp.imagecomp.utils.P;

public class CoderAlg extends Alg<ImageInfo>{

	private String path;
	private boolean study;
	public static final int BLOCK_SIZE = 4;
	
	public CoderAlg(String path, boolean study) {
		this.path = path;
		this.study = study;
	}
	
	@Override
	public ImageInfo processAlg() {
		Image img = IOUtils.loadImage(path);
		Vectorization v = new Vectorization(img, BLOCK_SIZE);
		List<ColoredBlock> cbs =  v.processAlg();
		
		DCT dct = new DCT(cbs);
		List<ColoredBlock> cbs2 = dct.processAlg();
		
		NeuronStorage ns =new  InMemoryNeuronStorage();
		if(Main.ns != null){
			ns = Main.ns;
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("path", "neuron_store.ser");
			ns.init(params);
		}
		
		VectorQuantizer vq = new VectorQuantizer(ns, cbs2, study);
		P<NeuronStorage, List<Integer>> res = vq.processAlg();
		
		ns = res.a;
		if(study){
			ns.persist();
		}
		
		Main.ns = ns;
		
		ImageInfo imgInfo = new ImageInfo();
		imgInfo.blockSize = BLOCK_SIZE;
		imgInfo.h = img.getH();
		imgInfo.w = img.getW();
		imgInfo.neuronIndexs = res.b;
		return imgInfo;
	}

}
