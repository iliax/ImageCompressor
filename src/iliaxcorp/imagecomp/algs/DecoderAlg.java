package iliaxcorp.imagecomp.algs;

import java.util.HashMap;
import java.util.Map;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.persistance.NeuronStorage;

public class DecoderAlg extends Alg<Image>{

	ImageInfo imgInfo;
	Image img;
	NeuronStorage ns;
	
	public DecoderAlg(Image aImg, ImageInfo aImgInfo, NeuronStorage aNs) {
		imgInfo = aImgInfo;
		ns = aNs;
		img = aImg;
	}
	
	@Override
	public Image processAlg() {
		int horBlocks = img.getW() / CoderAlg.BLOCK_SIZE;
		int vertBlocks = img.getH() / CoderAlg.BLOCK_SIZE;
		int blockSize = CoderAlg.BLOCK_SIZE;
		
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("path", "neuron_store.ser");
		ns.init(params);
		
		int nIndx = 0;
		for(int i = 0; i < vertBlocks; i ++){
			int ii = i * blockSize;
			for(int j = 0; j < horBlocks; j++){
				int jj = j*blockSize;
				addBlock(jj, ii, imgInfo.neuronIndexs.get(nIndx++));
			}
		}
		
		return img;
	}

	void addBlock(int x, int y, int nInd){
		int link=0;
		for(int i=y; i<y+CoderAlg.BLOCK_SIZE; i++){
			for(int j=x; j<x+CoderAlg.BLOCK_SIZE; j++){
				img.setColorAt(j, i, ns.getNeuronByIndex(nInd).getLinks().get(link++));
			}
		}
	}
}
