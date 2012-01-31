/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package variations;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import week1.ShellFileProcessingStrategy;

/**
 *
 * @author larss
 */
public class TestShellFileProcessingStrategy {
	private ShellFileProcessingStrategy s;
	private Map<String, String> args;
	
	@Before
	public void setup(){
		s = new ShellFileProcessingStrategy();
		args = new HashMap<String, String>();
	}
	
	@Test
	public void shouldReturnSimpleEchoedOutput(){
		String expectedOutput = "Success!";
		
		File f = new File("test/variations/simple-shell.sh");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		s.processFile(f, args, output);
		
		assertEquals("Output doesn't match", expectedOutput, output.toString());
	}
	
	@Test
	public void shouldReturnEchoedOutputWithArguments(){
		String argument = "Lars";
		String expectedOutput = "Output: "+argument;
		
		File f = new File("test/variations/argumented-shell.sh");
		
		args.put("name", argument);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		s.processFile(f, args, output);
		
		assertEquals("Argumented output doesn't match.", expectedOutput, output.toString());
	}
	
	@Test
	public void shouldReturnMultilineEchoedOutput(){
		File f = new File("test/variations/multiline-shell.sh");
		
		String expectedOutput = "Line 1\nLine 2";
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		s.processFile(f, args, output);
		
		assertEquals("Multiline output doesn't match.", expectedOutput, output.toString());
	}
}
