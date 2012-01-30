package week1;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public interface FileProcessingStrategy {
	public void processFile(File f, Map<String, String> args, OutputStream out);
	
	/**
	 * Defines the content type this strategy returns
	 * 
	 * @return content type this strategy returns
	 */
	public String getContentType(File f);
}
