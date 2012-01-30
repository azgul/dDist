package week1;

import java.io.*;
import java.util.*;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public interface FileProcessingStrategy {
	public String processFile(File f, Map<String, String> args);
	
	/**
	 * Defines the content type this strategy returns
	 * 
	 * @return content type this strategy returns
	 */
	public String getContentType(File f);
}
