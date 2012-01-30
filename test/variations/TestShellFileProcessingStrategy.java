/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package variations;

import java.io.File;
import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;
import week1.ShellFileProcessingStrategy;

/**
 *
 * @author larss
 */
public class TestShellFileProcessingStrategy {
	private ShellFileProcessingStrategy s;
	
	@Before
	public void setup(){
		s = new ShellFileProcessingStrategy();
	}
	
	@Test
	public void shouldReturnSimpleEchoedOutput(){
		String expectedOutput = "Success!";
		
		File f = new File("test/variations/simple-shell.sh");
		HashMap<String, String> h = new HashMap<String, String>();
		String output = s.processFile(f, h);
		
		assertEquals("Output doesn't match", expectedOutput, output);
	}
	
	@Test
	public void shouldReturnEchoedOutputWithArguments(){
		String argument = "Lars";
		String expectedOutput = "Output: "+argument;
		
		File f = new File("test/variations/argumented-shell.sh");
		HashMap<String, String> h = new HashMap<String, String>();
		
		h.put("name", argument);
		
		String output = s.processFile(f, h);
		
		assertEquals("Output doesn't match.", expectedOutput, output);
	}
}
