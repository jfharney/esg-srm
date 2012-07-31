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

	
	/*public int runBeStManCopyRequest(){
		
		String surl = url;
		String turl = "file:///tmp"+url.substring(url.lastIndexOf("/"));
		
		System.out.println("TURL="+turl);
		
		if(surl==null || turl == null){
			return -1;
		}
		
		//Initialize all arguments to SRMcopy here
		
		//TODO: assign the value of turl
		
		
		ArrayList<String> argsList = new ArrayList<String>();
		
		
		argsList.add(surl);
		
		argsList.add(turl);
			
		
		String args[] = new String[argsList.size()];
		
		for(int i=0; i<argsList.size(); i++){
			args[i] = argsList.get(i).toString();
		}
		
		
		
//		${JAVA_HOME}/bin/java ${MY_CERT_DIR} ${MY_USER_PROXY} ${MY_GTCPPR} ${MY_MEM_OPT} ${MY_SMEM_OPT} -DSRM.HOME=${SRM_HOME} -Dorg.globus.ogsa.client.timeout=0 -Dlog4j.configuration=${SRM_HOME}/properties/log4j.properties -Djava.security.auth.login.config=${SRM_HOME}/properties/authmod-unix.properties -Djava.endorsed.dirs=${SRM_HOME}/lib/endorsed gov.lbl.srm.client.main.SRMClientN $*

		//Set environment variables, Exactly the same as the script
		ProcessBuilder pb = new ProcessBuilder("echo","dummyPB");
		Map<String, String> env = pb.environment();
		
		String SRM_HOME = System.getenv("SRM_HOME");
		if(SRM_HOME == null || "".equalsIgnoreCase(SRM_HOME)){
			SRM_HOME = "/usr/local/bestman-2.2.1.3.27";
			env.put("SRM_HOME", SRM_HOME);
		}
		
		if(SRM_HOME.endsWith("/")){
			SRM_HOME = SRM_HOME.substring(0, SRM_HOME.length()-1);
		}
		
		String JAVA_HOME = System.getenv("JAVA_HOME");
		if(JAVA_HOME == null || "".equalsIgnoreCase(JAVA_HOME)){
			JAVA_HOME = "/usr";
			env.put("JAVA_HOME", JAVA_HOME);
		}
		
		if(JAVA_HOME.endsWith("/")){
			JAVA_HOME = JAVA_HOME.substring(0, JAVA_HOME.length()-1);
		}
		
		String GLOBUS_TCP_PORT_RANGE = System.getenv("GLOBUS_TCP_PORT_RANGE");
		if(GLOBUS_TCP_PORT_RANGE == null){
			GLOBUS_TCP_PORT_RANGE = "";
		}
		
		String GLOBUS_TCP_SOURCE_RANGE = System.getenv("GLOBUS_TCP_SOURCE_RANGE");
		if(GLOBUS_TCP_SOURCE_RANGE == null){
			GLOBUS_TCP_SOURCE_RANGE = "";
		}
		
		String X509_CERT_DIR = System.getenv("X509_CERT_DIR");
		if(X509_CERT_DIR == null){
			X509_CERT_DIR = "/etc/grid-security/certificates";
		}
		
		String GSI_DAEMON_TRUSTED_CA_DIR = System.getenv("GSI_DAEMON_TRUSTED_CA_DIR");
		if(GSI_DAEMON_TRUSTED_CA_DIR == null){
			GSI_DAEMON_TRUSTED_CA_DIR = "/etc/grid-security/certificates";
		}
		
		//TODO: Set JAVA_MAX_HEAP, etc. and -X JVM Parameters. If needed
		
		File srmHomeFile = new File(SRM_HOME + "/bin/srm-copy");
		if (!srmHomeFile.exists()){
			System.out.println("Cannot find the $SRM_HOME location, \nPlease define $SRM_HOME correctly");
		}
		
		//TODO: echo "srm-copy 'cat $SRM_HOME/version'" if needed
		
		
		File javaHomeFile = new File(JAVA_HOME + "/bin/java");
		if (!javaHomeFile.exists()){
			System.out.println("Cannot find the java executable in the $JAVA_HOME/bin location, \nPlease define $JAVA_HOME correctly");
		}

		if(GLOBUS_TCP_PORT_RANGE != null && !GLOBUS_TCP_PORT_RANGE.equalsIgnoreCase("")){
			env.put("GLOBUS_TCP_PORT_RANGE", GLOBUS_TCP_PORT_RANGE);
			System.setProperty("GLOBUS_TCP_PORT_RANGE", GLOBUS_TCP_PORT_RANGE);
		}
		
		if(GLOBUS_TCP_SOURCE_RANGE != null && !GLOBUS_TCP_SOURCE_RANGE.equalsIgnoreCase("")){
			env.put("GLOBUS_TCP_SOURCE_RANGE", GLOBUS_TCP_SOURCE_RANGE);
		}
		
		if(X509_CERT_DIR != null && !X509_CERT_DIR.equalsIgnoreCase("")){
			env.put("X509_CERT_DIR", X509_CERT_DIR);
			env.put("GSI_DAEMON_TRUSTED_CA_DIR", GSI_DAEMON_TRUSTED_CA_DIR);
			System.setProperty("X509_CERT_DIR", X509_CERT_DIR);
		}
		
		
		//Probably makes no difference to remove the following if block
		if(env.get("X509_USER_PROXY")!=null && env.get("X509_USER_PROXY")!=""){
			System.setProperty("X509_USER_PROXY", env.get("X509_USER_PROXY"));
		}
		
		File ogsalibsFolder = new File(SRM_HOME + "/lib/globus");
		File[] ogsalibsFiles = ogsalibsFolder.listFiles();
		
		File ogsalibs2Folder = new File(SRM_HOME + "/lib/globus/client");
		File[] ogsalibs2Files = ogsalibs2Folder.listFiles();
		
		String tmpClassPath = ".";
		
//		System.out.println("ogsalibsFiles.length = "+ ogsalibsFiles.length);
		
		for(File i : ogsalibsFiles){
			if(i.getName().endsWith(".jar")){
				tmpClassPath=tmpClassPath+":"+ SRM_HOME + "/lib/globus/" + i.getName();
			}
		}
		
//		System.out.println("ogsalibs2Files.length = "+ ogsalibs2Files.length);
		
		for(File i : ogsalibs2Files){
			if(i.getName().endsWith(".jar")){
				tmpClassPath=tmpClassPath+":"+ SRM_HOME + "/lib/globus/client/" + i.getName();
			}
		}
		
		tmpClassPath = tmpClassPath + ":" + SRM_HOME + "/lib/endorsed/xalan.jar";
		
//		System.out.println("CLASSPATH="+env.get("CLASSPATH"));
		
		String classPath = env.get("CLASSPATH");
		if(env.get("CLASSPATH")!=null){
			classPath += ":"+ SRM_HOME+"/lib/Berkeley.StorageResourceManager-client.jar:" + 
					SRM_HOME+"/lib/Berkeley.StorageResourceManager-client-stub.jar:" + tmpClassPath;
		}
		else{
			classPath = SRM_HOME+"/lib/Berkeley.StorageResourceManager-client.jar:" + 
					SRM_HOME+"/lib/Berkeley.StorageResourceManager-client-stub.jar:" + tmpClassPath;
		}
				
		//env.put("CLASSPATH", classPath);
		
//		System.out.println("CLASSPATH="+env.get("CLASSPATH"));
		
		System.setProperty("SRM.HOME", "${SRM_HOME}");
		
		//TODO: SET THESE... FIGURE OUT
//		${MY_MEM_OPT} ${MY_SMEM_OPT}
		
		System.setProperty("org.globus.ogsa.client.timeout", "0");
		
		System.setProperty("log4j.configuration", SRM_HOME+"/properties/log4j.properties");
		
		System.setProperty("java.security.auth.login.config", SRM_HOME+"/properties/authmod-unix.properties");
		
		System.setProperty("java.endorsed.dirs", SRM_HOME+"/lib/endorsed");
		
		//Finally Call function
		SRMClientN client = new SRMClientN(args, null);
		
		System.out.println("Call to ClientN complete");
				
		return 0;
	}*/


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
//            prop.load(new FileInputStream("./src/java/main/mail.properties"));
            prop.load(new FileInputStream("./mail.properties"));

            
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
	
	/*public int runBeStManCopyScript() throws IOException{
		String turl = "file:///tmp"+url.substring(url.lastIndexOf("/"));
		
		System.out.println("TURL="+turl);
		ProcessBuilder pb = new ProcessBuilder("/usr/local/bestman/bin/srm-copy", "url", turl);
//		pb.directory(new File("/usr/local/bestman/bin/"));
//		pb.directory(new File("/ccs/home/e1g"));
		
		pb.start();
		return 0;
	}*/
	
	/*public int runBeStManLSRequest(){
		String surl = url;
		//Initialize all arguments to SRMls here
		
		ArrayList<String> argsList = new ArrayList<String>();
		
		argsList.add(surl);	
		
		String args[] = new String[argsList.size()];
		
		for(int i=0; i<argsList.size(); i++){
			args[i] = argsList.get(i).toString();
		}
		
		//Set environment variables, Exactly the same as the script
		ProcessBuilder pb = new ProcessBuilder("echo","dummyPB");
		Map<String, String> env = pb.environment();
		
		String SRM_HOME = System.getenv("SRM_HOME");
		if(SRM_HOME == null || "".equalsIgnoreCase(SRM_HOME)){
			SRM_HOME = "/usr/local/bestman-2.2.1.3.27";
			env.put("SRM_HOME", SRM_HOME);
		}
		
		if(SRM_HOME.endsWith("/")){
			SRM_HOME = SRM_HOME.substring(0, SRM_HOME.length()-1);
		}
		
		String JAVA_HOME = System.getenv("JAVA_HOME");
		if(JAVA_HOME == null || "".equalsIgnoreCase(JAVA_HOME)){
			JAVA_HOME = "/usr";
			env.put("JAVA_HOME", JAVA_HOME);
		}
		
		if(JAVA_HOME.endsWith("/")){
			JAVA_HOME = JAVA_HOME.substring(0, JAVA_HOME.length()-1);
		}
		
		String GLOBUS_TCP_PORT_RANGE = System.getenv("GLOBUS_TCP_PORT_RANGE");
		if(GLOBUS_TCP_PORT_RANGE == null){
			GLOBUS_TCP_PORT_RANGE = "";
		}
		
		String GLOBUS_TCP_SOURCE_RANGE = System.getenv("GLOBUS_TCP_SOURCE_RANGE");
		if(GLOBUS_TCP_SOURCE_RANGE == null){
			GLOBUS_TCP_SOURCE_RANGE = "";
		}
		
		String X509_CERT_DIR = System.getenv("X509_CERT_DIR");
		if(X509_CERT_DIR == null){
			X509_CERT_DIR = "/etc/grid-security/certificates";
		}
		
		String GSI_DAEMON_TRUSTED_CA_DIR = System.getenv("GSI_DAEMON_TRUSTED_CA_DIR");
		if(GSI_DAEMON_TRUSTED_CA_DIR == null){
			GSI_DAEMON_TRUSTED_CA_DIR = "/etc/grid-security/certificates";
		}
		
		//TODO: Set JAVA_MAX_HEAP, etc. and -X JVM Parameters. If needed
		
		File srmHomeFile = new File(SRM_HOME + "/bin/srm-copy");
		if (!srmHomeFile.exists()){
			System.out.println("Cannot find the $SRM_HOME location, \nPlease define $SRM_HOME correctly");
		}
		
		//TODO: echo "srm-copy 'cat $SRM_HOME/version'" if needed
		
		
		File javaHomeFile = new File(JAVA_HOME + "/bin/java");
		if (!javaHomeFile.exists()){
			System.out.println("Cannot find the java executable in the $JAVA_HOME/bin location, \nPlease define $JAVA_HOME correctly");
		}

		if(GLOBUS_TCP_PORT_RANGE != null && !GLOBUS_TCP_PORT_RANGE.equalsIgnoreCase("")){
			env.put("GLOBUS_TCP_PORT_RANGE", GLOBUS_TCP_PORT_RANGE);
		}
		
		if(GLOBUS_TCP_SOURCE_RANGE != null && !GLOBUS_TCP_SOURCE_RANGE.equalsIgnoreCase("")){
			env.put("GLOBUS_TCP_SOURCE_RANGE", GLOBUS_TCP_SOURCE_RANGE);
		}
		
		if(X509_CERT_DIR != null && !X509_CERT_DIR.equalsIgnoreCase("")){
			env.put("X509_CERT_DIR", X509_CERT_DIR);
			env.put("GSI_DAEMON_TRUSTED_CA_DIR", GSI_DAEMON_TRUSTED_CA_DIR);
			System.setProperty("X509_CERT_DIR", X509_CERT_DIR);
		}
		
		
		//Probably makes no difference to remove the following if block
		if(env.get("X509_USER_PROXY")!=null && env.get("X509_USER_PROXY")!=""){
			System.setProperty("X509_USER_PROXY", env.get("X509_USER_PROXY"));
		}
		
		File ogsalibsFolder = new File(SRM_HOME + "/lib/globus");
		File[] ogsalibsFiles = ogsalibsFolder.listFiles();
		
		File ogsalibs2Folder = new File(SRM_HOME = "/lib/globus/client");
		File[] ogsalibs2Files = ogsalibs2Folder.listFiles();
		
		
		String tmpClassPath = ".";
		
		for(File i : ogsalibsFiles){
			if(i.getName().endsWith(".jar")){
				tmpClassPath=tmpClassPath+":"+ SRM_HOME + "/lib/globus/" + i.getName();
			}
		}
		
		for(File i : ogsalibs2Files){
			if(i.getName().endsWith(".jar")){
				tmpClassPath=tmpClassPath+":"+ SRM_HOME + "/lib/globus/client/" + i.getName();
			}
		}
		
		tmpClassPath = tmpClassPath + ":" + SRM_HOME + "/lib/endorsed/xalan.jar";
		
		String classPath = env.get("CLASSPATH") +":"+ SRM_HOME+"/lib/Berkeley.StorageResourceManager-client.jar:" + 
					SRM_HOME+"/lib/Berkeley.StorageResourceManager-client-stub.jar:" + tmpClassPath;
		env.put("CLASSPATH", classPath);
		
		
		System.out.println("Took out classpath");
		
		System.setProperty("SRM.HOME", "${SRM_HOME}");
		
		//TODO: SET THESE... FIGURE OUT
//		${MY_MEM_OPT} ${MY_SMEM_OPT}
		
		System.setProperty("org.globus.ogsa.client.timeout", "0");
		
		System.setProperty("log4j.configuration", SRM_HOME+"/properties/log4j.properties");
		
		System.setProperty("java.security.auth.login.config", SRM_HOME+"/properties/authmod-unix.properties");
		
		System.setProperty("java.endorsed.dirs", SRM_HOME+"/lib/endorsed");

		//Finally Call function
		SRMClientLs.main(args);
		

		return 0;
	}*/

	/*public int runBeStManLSScript() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("./srm-ls", "url");
		pb.directory(new File("/usr/local/bestman/bin"));
		pb.start();
		return 0;
	}*/
	
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
