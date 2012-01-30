package week1;

import java.io.*;
import java.util.*;
import java.io.*;
import java.net.URLConnection;

/**
 *
 * @author Martin
 */
public class GeneralFileProcessingStrategy implements FileProcessingStrategy {

	public String processFile(File f, Map<String, String> hm) {
		byte[] buffer = new byte[(int) f.length()];
		
		try {
			BufferedInputStream inputstream = new BufferedInputStream(new FileInputStream(f));
			inputstream.read(buffer);
		} catch (IOException e) {
			return null;
		}
		
		return new String(buffer);
	}

	public String getContentType(File f) {
		return URLConnection.guessContentTypeFromName(f.getPath());
	}
	
	
}
