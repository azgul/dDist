package week1;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author Martin
 */
public class HTTPServer {
	int port;
	String wwwhome;
	Map<String,FileProcessingStrategy> fileStrategies;
	
	Socket con;
	BufferedReader in;
	OutputStream out;
	PrintStream pout;
	
	HTTPServer(int p, String www) {
		port = p;
		wwwhome = www;
		fileStrategies = new HashMap<String,FileProcessingStrategy>();
		fileStrategies.put("application/x-shar", new ShellFileProcessingStrategy());
		GeneralFileProcessingStrategy general = new GeneralFileProcessingStrategy();
		fileStrategies.put("default", general);
	}
	
	public static void main(String[] args) {
		if (args.length!=2) {
			System.out.println("Usage: java FileServer <port> <wwwhome>");
			System.exit(-1);
		}
		int port = Integer.parseInt(args[0]);
		String wwwhome = args[1];
		HTTPServer fs = new HTTPServer(port, wwwhome);
		fs.run();
	}
	
	private void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not start server: "+e);
			System.exit(-1);
		}
		System.out.println("FileServer accepting connections on port "+ port);
		
		while (true) {
			try {
				con = ss.accept();
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				out = new BufferedOutputStream(con.getOutputStream());
				pout = new PrintStream(out);
				
				String request = in.readLine();
				con.shutdownInput(); //ignore the rest
				log(con, request);
				
				processRequest(request);
				pout.flush();
			} catch (IOException e) {
				System.err.println(e);
			}
			try {
				if (con!= null) {
					con.close();
				}
			} catch (IOException e) {
				System.err.println(e);
				}
			}
		}
	
	private void doGet(String request) {
		String[] parts = request.split(" ");
		String[] path = parts[1].split("\\?");
		Map<String,String> map;
		if(path.length > 1)
			map = makeMap(path[1]);
		else
			map = new HashMap<String,String>();
		
		try {
			File f = getFile(path[0]);
			String inputtype = URLConnection.guessContentTypeFromName(f.getPath());
			FileProcessingStrategy strategy;
			if(fileStrategies.containsKey(inputtype))
				strategy = fileStrategies.get(inputtype);
			else
				strategy = fileStrategies.get("default");
				
			String response = strategy.processFile(f, map);
			log(con, "Response: " + response);
			String contenttype = strategy.getContentType(f);
			
			pout.print("HTTP/1.0 200 OK\r\n");
			if (contenttype!=null) 
				pout.print("Content-Type: "+contenttype+"\r\n");
			pout.print("Date: "+new Date() + "\r\n"+
					"Server: dDist HTTPServer 1.0\r\n\r\n");
			pout.print(response);
			log(con, "200 OK");
		}
		catch(FileNotFoundException e){
			errorReport(pout, con, "404", "Not Found",
							"The requested URL was not found on this server.");
		}
	}
	
	private void doPost(String request) {
		
	}
	
	private Map<String,String> makeMap(String vars){
		String[] parts = vars.split("&");
		Map<String,String> map = new HashMap<String,String>();
		for(String part : parts){
			String[] keyValue = part.split("=");
			if(keyValue.length > 1)
				map.put(keyValue[0], keyValue[1]);
			else
				map.put(keyValue[0], "");
		}
		return map;
	}
	
	private File getFile(String path) throws FileNotFoundException {
		path = wwwhome+path;
		File f = new File(path);
		
		// Check for directory and get index file
		if(f.isDirectory()){
			if(!path.endsWith("/"))
				path = path + "/";
			path = path + "index.html";
			f = new File(path);
		}
		
		if(f.exists())
			return f;
		
		throw new FileNotFoundException();
	}
	
	private void processRequest(String request) 
			throws IOException {
		
		if(!isValidRequest(request)){
			errorReport(pout, con, "400", "Bad Request", 
					"Your Browser sent a request that this server could not understand.");
		}
		
		if(request.startsWith("POST"))
			doPost(request);
		
		if(request.startsWith("GET"))
			doGet(request);
		
		/*if (!request.startsWith("GET") || 
				request.length()<14 || 
				!(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1")) || 
				request.charAt(4) != '/'){
			errorReport(pout, con, "400", "Bad Request", 
					"Your Browser sent a request that this server could not understand.");
		} else {
			String req = request.substring(4, request.length()-9).trim();
			if (req.indexOf("/.")!= -1 || req.endsWith("~")) {
				errorReport(pout, con, "403", "Forbidden",
					"You don't have permission to access the requested URL.");
			} else {
				String path = wwwhome+"/"+req;
				File f = new File(path);
				if (f.isDirectory() && !path.endsWith("/")) {
					pout.print("HTTP/1.0 301 Moved Permanently\r\n"+
							"Location: http://"+ 
							con.getLocalAddress().getHostAddress()+":"+
							con.getLocalPort()+req+"/\r\n\r\n");
					log(con, "301 Moved Permanently");
				} else {
					if (f.isDirectory()) {
						path = path+"index.html";
						f = new File(path);
					}
				}
				try {
					InputStream file = new FileInputStream(f);
					String contenttype = URLConnection.guessContentTypeFromName(path);
					pout.print("HTTP/1.0 200 OK\r\n");
					if (contenttype!=null) 
						pout.print("Content-Type: "+contenttype+"\r\n");
					pout.print("Date: "+new Date() + "\r\n"+
							"Server: IXWT FileServer 1.0\r\n\r\n");
					sendFile(file, out); // send raw file
					log(con, "200 OK");
				} catch (FileNotFoundException e) {
					errorReport(pout, con, "404", "Not Found",
							"The requested URL was not found on this server.");
				}
			}
		} */
	}
	
	private boolean isValidRequest(String request){
		
		// Check for valid method types
		if(!request.startsWith("POST") && !request.startsWith("GET")){
			return false;
		}
		
		// Check for valid HTTP version
		if(!request.endsWith("HTTP/1.0") && !request.endsWith("HTTP/1.1"))
			return false;
		
		// Check that the requested path starts with a /
		String[] parts = request.split(" ");
		if(!parts[1].startsWith("/"))
			return false;
		
		return true;
	}
	
	private void log (Socket con, String msg) {
		System.err.println(new Date()+" ["+
				con.getInetAddress().getHostAddress()+
				":"+con.getPort()+"] "+ msg);
	}
	
	private void errorReport(PrintStream pout, Socket con, String code, String title, String msg) {
		pout.print("HTTP/1.0 "+code+" "+title+"\r\n"+
				"\r\n"+
				"<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n"+
				"<head><title>"+code+" "+title+"</title>\r\n"+
				"</head><body>\r\n"+
				"<h1>"+title+"</h1>\r\n"+msg+"<p>\r\n"+
				"<hr><address>IXWT FileServer 1.0 at "+
				con.getLocalAddress().getHostName()+
				" Port"+con.getLocalPort()+"</address>\r\n"+
				"</body></html>\r\n");
		log(con, code+" "+title);
	}
	
	private void sendFile(InputStream file, OutputStream out) throws IOException {
		byte[] buffer = new byte[1000];
		while (file.available()>0) 
			out.write(buffer, 0, file.read(buffer));
	}
}
