package week1;

import java.net.*;
import java.io.*;
import java.util.*;
import week2.*;
/**
 *
 * @author Martin
 */
public class HTTPServer {
	int port;
	String wwwhome;
	Map<String,FileProcessingStrategy> fileStrategies;
	Map<String,List<Object>> objects;
	
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
		
		// Create available object list
		objects = new HashMap<String,List<Object>>();
		List banks = new ArrayList<BankInterface>();
		BankInterface firstBank = new Bank();
		banks.add(firstBank);
		objects.put("Bank", banks);
		List accounts = new ArrayList<AccountInterface>();
		objects.put("Account", accounts);
	}
	
	public static void main(String[] args) {
		if (args.length!=2) {
			System.out.println("Usage: java HTTPServer <port> <wwwhome>");
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
		System.out.println("AwesomeServer accepting connections on port "+ port);
		
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
			} catch (Exception e){
				errorReport(pout, con, "500", "Internal Server Error",
						"An error occured.");
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
			else{
				strategy = fileStrategies.get("default");
			}
			String contenttype = strategy.getContentType(f);
			
			pout.print("HTTP/1.0 200 OK\r\n");
			if (contenttype!=null) 
				pout.print("Content-Type: "+contenttype+"\r\n");
			pout.print("Date: "+new Date() + "\r\n"+
					"Server: dDist HTTPServer 1.0\r\n\r\n");
			strategy.processFile(f, map, out);
			log(con, "200 OK");
		}
		catch(FileNotFoundException e){
			if(!processAsObject(path, map)){
				errorReport(pout, con, "404", "Not Found",
							"The requested URL was not found on this server.");
			}
		}
	}
	
	private boolean processAsObject(String[] path, Map<String,String> map){
		String[] pathParts = path[0].split("\\/");
		String cls = pathParts[1];
		if(!objects.containsKey(cls))
			return false;
		String method = pathParts[3];
		int id = Integer.parseInt(pathParts[2]);
		String code = "200 OK";
		String body = "";
		
		if(cls.equals("Bank"))
		{
			if(id < objects.get("Bank").size()){
				Bank bank = (Bank)objects.get("Bank").get(id);
				if(method.equals("getAccount")){
					// do some more stuff here
					Account account = bank.getAccount(map.get("name"));
					if(objects.get("Account").contains(account)){
						body = "" + objects.get("Account").indexOf(account);
					}
					else{
						objects.get("Account").add(account);
						body = "" + (objects.get("Account").size()-1);
						code = "201 Created";
					}
				}
			}else{
				body = "No such bank.";
				code = "404 Not Found";
			}
		}
		else if(cls.equals("Account")) {
			if(id < objects.get("Account").size()){
				Account account = (Account)objects.get("Account").get(id);
				if(method.equals("getName")){
					body = account.getName();
				}
				else if(method.equals("getBalance")) {
					body = "" + account.getBalance();
				}
				else if(method.equals("deposit")){
					account.deposit(Double.parseDouble(map.get("amount")));
					body = "Deposit successful.";
				}
				else if(method.equals("withdraw")){
					account.withdraw(Double.parseDouble(map.get("amount")));
					body = "Withdraw successful.";
				}
			}else{
				body = "No such account.";
				code = "404 Not Found";
			}
		}
		
		if(body.isEmpty())
			return false;
		else{			
			pout.print("HTTP/1.0 "+ code +"\r\n");
			pout.print("Content-Type: text/plain\r\n");
			pout.print("Date: "+new Date() + "\r\n"+
					"Server: dDist HTTPServer 1.0\r\n\r\n");
			pout.print(body);
			log(con, code);
			return true;
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
				map.put(keyValue[0], URLDecoder.decode(keyValue[1]));
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
		
		// Check for uri starting with /
		String[] parts = request.split(" ");
		if(!parts[1].startsWith("/")){
			errorReport(pout, con, "400", "Bad Request", 
					"Your Browser sent a request that this server could not understand.");
		}
		
		// Check for unimplemented methods
		if(!request.startsWith("GET"))
			errorReport(pout, con, "501", "Not Implemented",
					"Request method was not implemented.");
		
		if(parts[1].contains("../") || parts[1].startsWith("~"))
			errorReport(pout, con, "403", "Forbidden",
					"Forbidden resource requested.");
		
		if(request.startsWith("POST"))
			doPost(request);
		
		if(request.startsWith("GET"))
			doGet(request);
	}
	
	private void log (Socket con, String msg) {
		System.err.println(new Date()+" ["+
				con.getInetAddress().getHostAddress()+
				":"+con.getPort()+"] "+ msg);
	}
	
	private void errorReport(PrintStream pout, Socket con, String code, String title, String msg) {
		pout.print("HTTP/1.0 "+code+" "+title+"\r\n"+
				"\r\n"+
				"<!DOCTYPE html>\r\n"+
				"<head>\r\n<title>"+code+" "+title+"</title>\r\n"+
				"</head>\r\n<body>\r\n"+
				"<h1>"+title+"</h1>\r\n<p>"+msg+"</p>\r\n"+
				"<hr>\r\n<address>dDist HTTPServer 1.0 at "+
				con.getLocalAddress().getHostName()+
				" Port"+con.getLocalPort()+"</address>\r\n"+
				"</body>\r\n</html>\r\n");
		log(con, code+" "+title);
	}
}
