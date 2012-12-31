package iliaxcorp.imagecomp.algs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.IOUtils;
import iliaxcorp.imagecomp.utils.P;

public class CoderAlg extends Alg<ImageInfo, Void>{

	public static NeuronStorage ns; //TODO remove this shit
	
	private String path;
	
	private boolean study;
	
	public static final int BLOCK_SIZE = 4;
	
	private double L = 0.9;
	
	public CoderAlg(String path,  double L , boolean study) {
		this.path = path;
		this.study = study;
		this.L = L ;
	}
	
	@Override
	public ImageInfo processAlg(Void _v) {
		Image img = IOUtils.loadImage(path);
		Vectorization v = new Vectorization(img, BLOCK_SIZE);
		List<ColoredBlock> cbs =  v.processAlg(null);
		
		DCT dct = new DCT(cbs);
		List<ColoredBlock> cbs2 = dct.processAlg(null);
		
		NeuronStorage ns;
		if(CoderAlg.ns != null){
			ns = CoderAlg.ns;
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("path", "neuron_store.ser");
			ns = new InMemoryNeuronStorage(params);
			ns.init();
		}
		
		VectorQuantizer vq = new VectorQuantizer(ns, cbs2, study, L);
		P<NeuronStorage, List<Integer>> res = vq.processAlg(null);
		
		ns = res.a;
		if(study){
			ns.persist();
		}
		
		CoderAlg.ns = ns;
		
		ImageInfo imgInfo = new ImageInfo();
		imgInfo.blockSize = BLOCK_SIZE;
		imgInfo.h = img.getH();
		imgInfo.w = img.getW();
		imgInfo.neuronIndexes = res.b;
		return imgInfo;
	}

}
