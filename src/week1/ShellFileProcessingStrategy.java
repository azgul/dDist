/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author larss
 */
public class ShellFileProcessingStrategy implements FileProcessingStrategy {

	@Override
	public String processFile(File f, HashMap<String, String> args) {
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
				
			return sb.toString();
		}catch (IOException e){
			// IOException
		}
		
		// Some error occured
		return "";
	}
	
}
