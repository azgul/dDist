package week1;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author Randi K. Hillerøe <silwing@gmail.com>
 */
public interface FileProcessingStrategy {
	public String processFile(File f, HashMap<String, String> args);
}
