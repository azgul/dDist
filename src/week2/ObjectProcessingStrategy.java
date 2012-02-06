/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week2;

import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author Lars Rasmussen
 */
public interface ObjectProcessingStrategy {
	public boolean process(String[] path, Map<String, String> map, OutputStream out);
	
	/**
	 * Defines the content type this strategy returns
	 * 
	 * @return content type this strategy returns
	 */
	public String getContentType();
}
