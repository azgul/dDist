package week1;

import java.io.*;
import java.util.*;

/**
 *
 * @author Martin
 */
public class GeneralFileProcessingStrategy implements FileProcessingStrategy {

	@Override
	public String processFile(File f, HashMap<String, String> hm) {
		byte[] buffer = new byte[(int) f.length()];
		BufferedInputStream inputstream = null;
		
		try {
			inputstream.read(buffer);
		} catch (IOException e) {
			return null;
		}
		
		return new String(buffer);
	}
	
}
