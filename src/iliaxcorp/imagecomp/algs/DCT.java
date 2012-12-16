package iliaxcorp.imagecomp.algs;

import java.util.List;

import iliaxcorp.imagecomp.ColoredBlock;

public class DCT extends Alg<List<ColoredBlock>>{

	private List<ColoredBlock> blocks;
	
	public DCT(List<ColoredBlock> blcks) {
		blocks = blcks;
	}
	
	@Override
	public List<ColoredBlock> processAlg() {
		//TODO impl!
		return blocks;
	}

}
