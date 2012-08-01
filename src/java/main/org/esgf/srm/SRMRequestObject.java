package org.esgf.srm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import emailer.EmailNotifier;
import gov.lbl.srm.StorageResourceManager.*;
import gov.lbl.srm.client.wsdl.*;

import org.apache.log4j.PropertyConfigurator;


public class SRMRequestObject {
	private String openId;
	private String proxyId;
	private String proxyPwd;
	private String url;
	private String toemail;
	
	public SRMRequestObject(HttpServletRequest request){
		this.openId = request.getParameter("openid");
		
		//If null or empty OpenId
		if(this.getOpenId()==null || this.getOpenId().contentEquals("")){
			System.out.println("Null openid");
		}
		
		this.proxyId = request.getParameter("proxyid");
		
		//If null or empty ProxyId
		if(this.getProxyId()==null || this.getProxyId().contentEquals("")){
			System.out.println("Null ProxId");
		}
		
		this.proxyPwd = request.getParameter("pass");
		
		//If null or empty Password
		if(this.getProxyPwd()==null || this.getProxyPwd().contentEquals("")){
			System.out.println("Null Proxy Password");
		}
	
		this.url = request.getParameter("url");
		
		//If null or empty url
		if(this.getUrl()==null || this.getUrl().contentEquals("")){
			System.out.println("Null URL");
			url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
					"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/" +
					"ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc";
		}
		
		this.toemail = request.getParameter("email");
		//If null or empty url
		if(this.getUrl()==null || this.getUrl().contentEquals("")){
			System.out.println("NULL to email id");
		}
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getProxyId() {
		return proxyId;
	}
	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}
	public String getProxyPwd() {
		return proxyPwd;
	}
	public void setProxyPwd(String proxyPwd) {
		this.proxyPwd = proxyPwd;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getToemail() {
		return toemail;
	}
	public void setToemail(String toemail) {
		this.toemail = toemail;
	}


	public void sendSubmissionConfirmation(){
		String subject = "Your request has been submitted.";
		String body = "Your request for file(s) in link\n"+ url +"\n has been submitted to SRM. It may take some time to retreive the" +
         		" data. You will receive another email when the data is ready for download along with the download link. " +
         		"\nThe link will be active for about 4 days after which it will be deactivated and you will be asked to resubmit " +
         		"your request. \n\nThanks.";
        sendEmail(subject, body);
	}
	
	public void sendRequestCompletion(String turl){
		String subject = "Your request has been completed successfuly.";
		String body = "Your request for file(s) in link\n"+ url +"\n has been retreived successfully from SRM. You may download the " +
				"file(s) from the following link: \n" + turl + "\n "+
         		"\nThe link will be active for about 4 days after which it will be deactivated and you will be asked to resubmit " +
         		"your request. \n\nThanks.";
        sendEmail(subject, body);
	}
	
	public void sendRequestFailed(String status){
		String subject = "Your request could not be completed.";
		String body = "Your request for file(s) in link\n"+ url +"\n could not be completed by SRM. The reason for the failure was " +
				"cited as the error code \n" + status + ". Please try again later to check if the problem has been resolved." +
						"\n\nThanks.";
        sendEmail(subject, body);
	}
	
	public void sendEmail(String subject, String body){
		String to = this.toemail;
		String host="smtp.ornl.gov";
		String port="25";
		String from= "";
		String password = "";
		String user = "e1g";
		Properties prop = new Properties();
		try {
            prop.load(new FileInputStream("./src/java/main/mail.properties"));
//            prop.load(new FileInputStream("./mail.properties"));

            
            host = prop.getProperty("mailHost");
            port = prop.getProperty("mailPort");
            from = prop.getProperty("mailFrom");
            user = prop.getProperty("user");
            
            password = prop.getProperty("pass");
            
            List<String> argList = new ArrayList<String>();
            
            argList.add("-to");
            argList.add(to);
            System.out.println("email to: "+ to);
            
            argList.add("-from");
            argList.add(from);
            System.out.println("email from: "+ from);
            
            argList.add("-user");
            argList.add(user);
            System.out.println("user: "+ user);
            
            argList.add("-host");
            argList.add(host);
            System.out.println("host: "+ host);
            
            argList.add("-port");
            argList.add(port);
            System.out.println("port: "+ port);
            
            argList.add("-subject");
            argList.add(subject);
            
            argList.add("-body");
            argList.add(body);
            
            argList.add("-password");
            argList.add(password);
            System.out.println("password: "+ password);
            
            
            String args[] = argList.toArray(new String[argList.size()]);
            
            for(int i=0; i < argList.size(); i++){
            	args[i] = argList.get(i);
            }
            EmailNotifier submitted = new EmailNotifier(args);
            
            submitted.sendMail();
            
            
	    } catch (Exception e) {
	    	System.out.println("SRMRequestObject->sendSubmissionConfirmation: Something went wrong while reading the property file");
	    	e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("static-access")
	public String runBeStManGetRequest() throws Exception{
		String serverUrl = "";
	    String uid="";
	    String logPath="";
	    String log4jlocation="";
	    String storageInfo="";
	    String fileType="volatile";
	    String retentionPolicy="replica";
	    String accessLatency="online";
	    boolean debug = false;
	    boolean delegationNeeded=false;
	    
	    String ttemp = System.getProperty("log4j.configuration");
	    System.out.println("ttemp = "+ ttemp);
	    if(ttemp != null && !ttemp.equals("")) {
	       log4jlocation = ttemp;
	    }
	    
	    
	    String[] surl = new String[1];
	    surl[0] = url;
	    
	    String retStr = "";
	    
	    if(surl == null || surl.length == 0) {
	       System.out.println("Please provide the surls");
	       System.exit(1);
	    }
	    
	    serverUrl = url.substring(0, url.indexOf("?"));
	    
	    System.out.println("Server URL = "+serverUrl);
	    System.out.println("SURL = "+surl[0]);
	    
	    try{
	    	if(!storageInfo.equals("")) {
		       delegationNeeded=true;
		    }
		    SRMServer cc = new SRMServer(log4jlocation, logPath, debug, delegationNeeded);
		    
		    System.out.println("CC Initialized");
		    cc.connect(serverUrl);
		    System.out.println("Connection Established");
		    cc.ping(uid);
		    SRMRequest req = new SRMRequest();
		    req.setSRMServer(cc);
		    req.setAuthID(uid);
		    req.setRequestType("get");
		    req.addFiles(surl, null,null);
		    req.setStorageSystemInfo(storageInfo);
		    req.setFileStorageType(fileType);
		    req.setRetentionPolicy(retentionPolicy);
		    req.setAccessLatency(accessLatency);
		    req.submit();
		    
		    //TODO: Send Email Notifying that the request has been submitted
		    sendSubmissionConfirmation();
		    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		    
		    req.checkStatus();
		    int sleepTime  = 10;
		    SRMRequestStatus response = req.getStatus();
		    
		    if(response != null){
		    	while(!(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS ||
		                response.getReturnStatus().getStatusCode() == TStatusCode.SRM_FILE_PINNED)){
		    		response = req.getStatus();
		        	System.out.println("\nStatus.code="+response.getReturnStatus().getStatusCode());
			        System.out.println("\nStatus.exp="+response.getReturnStatus().getExplanation());
			        
			    	System.out.println("SRM-CLIENT: Next status call in "+ sleepTime + " secs");
		        	Thread.currentThread().sleep(sleepTime * 1000);
		        	sleepTime*=2;
		        	
		        	if(sleepTime>=600){
		        		sleepTime=600;
		        	}
		        	
		        	//If failed to extract then exit
		        	if(!(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS || 
		        			response.getReturnStatus().getStatusCode() != TStatusCode.SRM_FILE_PINNED ||
		        			response.getReturnStatus().getStatusCode() != TStatusCode.SRM_REQUEST_QUEUED ||
		        			response.getReturnStatus().getStatusCode() != TStatusCode.SRM_REQUEST_INPROGRESS)){
		        		System.out.println("SRM failed to extract file. Exiting.");
		        		retStr = "<srm_error>" + response.getReturnStatus().getStatusCode().toString() + "</srm_error>";
		        		sendRequestFailed(response.getReturnStatus().getStatusCode().toString());
		        		return retStr;
		        	}
			    }
		    	
		    	if(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS ||
    	          response.getReturnStatus().getStatusCode() == TStatusCode.SRM_FILE_PINNED) {
    	          HashMap map = response.getFileStatuses();
    	          Set set = map.entrySet();
    	          Iterator i = set.iterator();
    	          while(i.hasNext()) {
    	             Map.Entry me = (Map.Entry) i.next();
    	             String key =  (String) me.getKey();
    	             Object value = me.getValue();
    	             if(value != null) {
    	                FileStatus fileStatus = (FileStatus) value;
    	                org.apache.axis.types.URI uri = fileStatus.getTransferSURL();
    	                System.out.println("\nTransferSURL="+uri);
    	                
    	                
    	                //Notify by e-mail that request has been completed successfully.//
    	                sendRequestCompletion(uri.toString());
    	                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    	        		
    	                retStr = "<srm_url>"+uri.toString()+"</srm_url>";	//Return value
    	                
    	             }
    	          }//end while
    	       }//end if
		    	
		    }
		      
		    
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    
		return retStr;
	}
	
	
	//TODO: Remove Later:
	
	public static void main(String[] args) throws IOException{
		Properties props = System.getProperties();
		String userTemp = props.getProperty("user.name");
		
		System.out.println(userTemp);
		
		System.out.println("Check ProcessBuilder:");
		
		ProcessBuilder pb = new ProcessBuilder("mkdir", "testdir");
		Map<String, String> env = pb.environment();
		
		System.out.println("JAVA_HOME = "+env.get("JAVA_HOME"));
		
//		env.put("JAVA_HOME","/usr");
//		System.out.println("JAVA_HOME = "+env.get("JAVA_HOME"));
//		
//		env.put("VAR1", "myValue");
//		env.remove("OTHERVAR");
//		env.put("VAR2", env.get("VAR1") + "suffix");
		pb.directory(new File("/Users/e1g/Desktop/"));
		Process p = pb.start();
		
//		System.out.println("Check System.getenv():");
//		System.out.println(System.getenv("PATH"));
	}

}
