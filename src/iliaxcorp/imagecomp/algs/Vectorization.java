package iliaxcorp.imagecomp.algs;

import java.util.ArrayList;
import java.util.List;

import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;

public class Vectorization extends Alg<List<ColoredBlock>, Void> {

	private Image img;
	
	private int blockSize;
	
	public Vectorization(Image input,/* 4 */ int blockSizee) {
		img = input;
		blockSize = blockSizee;
	}

	private ColoredBlock getBlock(int x, int y){
		ColoredBlock cb = new ColoredBlock();
		for(int i = y; i < y + blockSize; i++){
			for(int j = x; j < x + blockSize; j++){
				cb.block.add(img.getColorAt(j, i));
			}
		}
		return cb;
	}
	
	@Override
	public List<ColoredBlock> processAlg(Void _v) {
		int horBlocks = img.getW() / blockSize;
		int vertBlocks = img.getH() / blockSize;
		

		List<ColoredBlock> blocks = new ArrayList<ColoredBlock>();
		for(int i = 0; i < vertBlocks; i ++){
			int ii = i * blockSize;
			for(int j = 0; j < horBlocks; j++){
				int jj = j*blockSize;
				blocks.add(getBlock(jj, ii));
			}
			
		}
		return blocks;
	}
}
