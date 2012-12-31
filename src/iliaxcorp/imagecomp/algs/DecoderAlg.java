package iliaxcorp.imagecomp.algs;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.ImageInfo;
import iliaxcorp.imagecomp.persistance.NeuronStorage;

public class DecoderAlg extends Alg<Image, Void>{

	ImageInfo imgInfo;
	Image img;
	NeuronStorage ns;
	
	public DecoderAlg(Image aImg, ImageInfo aImgInfo, NeuronStorage aNs) {
		imgInfo = aImgInfo;
		ns = aNs;
		img = aImg;
	}
	
	@Override
	public Image processAlg(Void _v) {
		int horBlocks = img.getW() / CoderAlg.BLOCK_SIZE;
		int vertBlocks = img.getH() / CoderAlg.BLOCK_SIZE;
		int blockSize = CoderAlg.BLOCK_SIZE;
		
		ns.init();
		
		int nIndx = 0;
		for(int i = 0; i < vertBlocks; i ++){
			int ii = i * blockSize;
			for(int j = 0; j < horBlocks; j++){
				int jj = j*blockSize;
				addBlock(img, jj, ii, imgInfo.neuronIndexes.get(nIndx++));
			}
		}
		
		return img;
	}

	void addBlock(Image img, int x, int y, int nInd){
		int link=0;
		for(int i=y; i<y+CoderAlg.BLOCK_SIZE; i++){
			for(int j=x; j<x+CoderAlg.BLOCK_SIZE; j++){
				img.setColorAt(j, i, ns.getNeuronByIndex(nInd).getLinks().get(link++));
			}
		}
	}
}
