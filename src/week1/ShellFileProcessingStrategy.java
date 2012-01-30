/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week1;

import java.io.*;
import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author larss
 */
public class ShellFileProcessingStrategy implements FileProcessingStrategy {

	public void processFile(File f, Map<String, String> args, OutputStream out) {
		// Create an arraylist which will eventually contain the path to our shell-script and it arguments
		ArrayList<String> callString = new ArrayList<String>();
		
		// Append the path to the shell-script
		callString.add(f.getAbsolutePath());
		
		// Add all of our arguments (if any)
		for(String v : args.values())
			callString.add(v);
		
		
		// Create a processbuilder which will
		ProcessBuilder pb = new ProcessBuilder(callString);
		
		// Start the process and get output
		try{
			Process p = pb.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = r.readLine()) != null){
				sb.append(line);
				sb.append("\n");
			}
			
			sb.deleteCharAt(sb.length()-1);
			
			r.close();
				
			PrintStream pout = new PrintStream(out);
			pout.print(sb.toString());
		}catch (IOException e){
			// IOException
		}
	}
	
	public String getContentType(File f){
		return "text/html";
	}
}
