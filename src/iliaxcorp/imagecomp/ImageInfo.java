package iliaxcorp.imagecomp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageInfo implements Serializable {
	
	private static final long serialVersionUID = -1017782039457974082L;

	public int blockSize = 4;
	
	public int w;
	
	public int h;
	
	public List<Integer> neuronIndexs = new ArrayList<Integer>();
	
	public ImageInfo() {
	}
	
	
}
