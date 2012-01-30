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
	private HashMap<String, String> args;
	
	@Before
	public void setup(){
		s = new ShellFileProcessingStrategy();
		args = new HashMap<String, String>();
	}
	
	@Test
	public void shouldReturnSimpleEchoedOutput(){
		String expectedOutput = "Success!";
		
		File f = new File("test/variations/simple-shell.sh");
		String output = s.processFile(f, args);
		
		assertEquals("Output doesn't match", expectedOutput, output);
	}
	
	@Test
	public void shouldReturnEchoedOutputWithArguments(){
		String argument = "Lars";
		String expectedOutput = "Output: "+argument;
		
		File f = new File("test/variations/argumented-shell.sh");
		
		args.put("name", argument);
		
		String output = s.processFile(f, args);
		
		assertEquals("Argumented output doesn't match.", expectedOutput, output);
	}
	
	@Test
	public void shouldReturnMultilineEchoedOutput(){
		File f = new File("test/variations/multiline-shell.sh");
		
		String expectedOutput = "Line 1\nLine 2";
		String output = s.processFile(f, args);
		
		System.out.println(output);
		
		assertEquals("Multiline output doesn't match.", expectedOutput, output);
	}
}
