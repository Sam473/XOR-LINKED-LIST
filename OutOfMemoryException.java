
@SuppressWarnings("serial")//No intention of serialising the class so no need for the warning
public class OutOfMemoryException extends Exception { //ADDED FUNCTIONALITY
	
	public OutOfMemoryException(String message) { //create my own exception for when the memory runs out
		super(message); //tell user there is no memory left
	}
}
