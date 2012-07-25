package org.esgf.srm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import gov.lbl.srm.client.exception.SRMClientException;
import gov.lbl.srm.client.main.*;
import gov.lbl.srm.client.util.*;
import gov.lbl.srm.StorageResourceManager.*;
import gov.lbl.srm.client.wsdl.*;

import org.apache.log4j.PropertyConfigurator;


public class SRMRequestObject {
	private String openId;
	private String proxyId;
	private String proxyPwd;
	private String url;
	
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
					"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli048/CCSM4/B_RCP45CN/lnd/DAY_AVG/2092-2101.tar#" +
					"/tmp/work/gaoyang1/archive/B_RCP45CN_DAY/lnd/hist/2092-2101/B_RCP45CN_DAY.clm2.h1.2101-01-01-00000.nc";
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
	
	public int runBeStManCopyRequest(){
		
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
		
		/*tmpClassPath = tmpClassPath + ":" + SRM_HOME + "/lib/endorsed/xalan.jar";
		
//		System.out.println("CLASSPATH="+env.get("CLASSPATH"));
		
		String classPath = env.get("CLASSPATH");
		if(env.get("CLASSPATH")!=null){
			classPath += ":"+ SRM_HOME+"/lib/Berkeley.StorageResourceManager-client.jar:" + 
					SRM_HOME+"/lib/Berkeley.StorageResourceManager-client-stub.jar:" + tmpClassPath;
		}
		else{
			classPath = SRM_HOME+"/lib/Berkeley.StorageResourceManager-client.jar:" + 
					SRM_HOME+"/lib/Berkeley.StorageResourceManager-client-stub.jar:" + tmpClassPath;
		}*/
				
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
	}

	public int runBeStManGetRequest() throws Exception{
		String serverUrl = "";
	    String uid="";
	    String logPath="";
	    String log4jlocation="";
	    String storageInfo="";
	    String fileType="volatile";
	    String retentionPolicy="replica";
	    String accessLatency="online";
	    boolean debug = false;
	    boolean silent = false;
	    boolean releaseFile =false;
	    boolean delegationNeeded=false;
	    Vector vec = new Vector ();
	    
	    String ttemp = System.getProperty("log4j.configuration");
	    System.out.println("ttemp = "+ ttemp);
	    if(ttemp != null && !ttemp.equals("")) {
	       log4jlocation = ttemp;
	    }
	    
	    String[] surl = new String[1];
	    surl[0] = url;
	    
	    if(surl == null || surl.length == 0) {
	       System.out.println("Please provide the surls");
	       System.exit(1);
	    }
	    
	    serverUrl = url.substring(0, url.indexOf("?"));
	    
	    System.out.println("Server URL = "+serverUrl);
	    System.out.println("SURL = "+surl[0]);
	    
	    SRMServer cc = new SRMServer(log4jlocation, logPath, debug);
	    
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
	    req.checkStatus();
	      
	    System.out.println("Request Submitted; Status Checked");
	    
		
		return 0;
	}
	
	public int runBeStManCopyScript() throws IOException{
		String turl = "file:///tmp"+url.substring(url.lastIndexOf("/"));
		
		System.out.println("TURL="+turl);
		ProcessBuilder pb = new ProcessBuilder("/usr/local/bestman/bin/srm-copy", "url", turl);
//		pb.directory(new File("/usr/local/bestman/bin/"));
//		pb.directory(new File("/ccs/home/e1g"));
		
		pb.start();
		return 0;
	}
	
	public int runBeStManLSRequest(){
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
		
		/*
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
		*/
		
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
	}

	public int runBeStManLSScript() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("./srm-ls", "url");
		pb.directory(new File("/usr/local/bestman/bin"));
		pb.start();
		return 0;
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
