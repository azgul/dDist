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

	public void processFile(File f, Map<String, String> hm, OutputStream out) {
		try {
			FileInputStream file = new FileInputStream(f);
			byte[] buffer = new byte[1000];
			while (file.available()>0) 
				out.write(buffer, 0, file.read(buffer));
		}
		catch(IOException e){
		}
	}

	public String getContentType(File f) {
		return URLConnection.guessContentTypeFromName(f.getPath());
	}
	
	
}
