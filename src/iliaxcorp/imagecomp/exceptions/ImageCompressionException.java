package iliaxcorp.imagecomp.exceptions;

public class ImageCompressionException extends RuntimeException {

	private static final long serialVersionUID = 4062240300452237371L;

	public ImageCompressionException() {
		super();
	}
	
	public ImageCompressionException(String mess){
		super(mess);
	}
	
	public ImageCompressionException(String mess, Exception cause){
		super(mess, cause);
	}
	
	public ImageCompressionException(Exception cause){
		super(cause);
	}
}
