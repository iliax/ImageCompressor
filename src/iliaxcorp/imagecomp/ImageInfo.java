package iliaxcorp.imagecomp;

import iliaxcorp.imagecomp.algs.CoderAlg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageInfo implements Serializable {
	
	private static final long serialVersionUID = -1017782039457974082L;

	public int blockSize = CoderAlg.BLOCK_SIZE;
	
	public int w;
	
	public int h;
	
	public List<Integer> neuronIndexes = new ArrayList<Integer>();
	
	public ImageInfo() {
	}
	
	
}
