package iliaxcorp.imagecomp.algs;

import java.util.ArrayList;
import java.util.List;

import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Image;

public class Vectorization extends Alg<List<ColoredBlock>, Void> {

	private Image img;
	
	
	public Vectorization(Image input) {
		img = input;
	}

	public static ColoredBlock getBlock(int x, int y, Image img){
		ColoredBlock cb = new ColoredBlock();
		for(int i = y; i < y + CoderAlg.BLOCK_SIZE; i++){
			for(int j = x; j < x + CoderAlg.BLOCK_SIZE; j++){
				cb.block.add(img.getColorAt(j, i));
			}
		}
		return cb;
	}
	
	@Override
	public List<ColoredBlock> processAlg(Void _v) {
		int horBlocks = img.getW() / CoderAlg.BLOCK_SIZE;
		int vertBlocks = img.getH() / CoderAlg.BLOCK_SIZE;
		

		List<ColoredBlock> blocks = new ArrayList<ColoredBlock>();
		for(int i = 0; i < vertBlocks; i ++){
			int ii = i * CoderAlg.BLOCK_SIZE;
			for(int j = 0; j < horBlocks; j++){
				int jj = j*CoderAlg.BLOCK_SIZE;
				blocks.add(getBlock(jj, ii, img));
			}
			
		}
		return blocks;
	}
}
