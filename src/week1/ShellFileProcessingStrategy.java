/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package week1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author larss
 */
public class ShellFileProcessingStrategy implements FileProcessingStrategy {

	@Override
	public String processFile(File f, HashMap<String, String> args) {
		ProcessBuilder pb = new ProcessBuilder("/Users/larss/Desktop/ddist-wwwroot/shell-script.sh");
		Map<String, String> env = pb.environment();
		
		for(String k : args.keySet()){
			env.put(k, args.get(k));
		}
		
		try{
			Process p = pb.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = r.readLine()) != null)
				sb.append(line);
				
			return line;
		}catch (IOException e){
			// IOException
		}
		
		return "";
	}
	
}
