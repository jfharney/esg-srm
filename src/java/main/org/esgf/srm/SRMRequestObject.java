package org.esgf.srm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import gov.lbl.srm.client.main.*;

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
	
	public int formBeStManCopyRequest(){
		
		String surl = url;
		String turl = "";
		
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
		if(env.get("X509_USER_PROXY")!=null || env.get("X509_USER_PROXY")!=""){
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
		
		System.setProperty("SRM.HOME", "${SRM_HOME}");
		
		//TODO: SET THESE... FIGURE OUT
//		${MY_MEM_OPT} ${MY_SMEM_OPT}
		
		System.setProperty("org.globus.ogsa.client.timeout", "0");
		
		System.setProperty("log4j.configuration", SRM_HOME+"/properties/log4j.properties");
		
		System.setProperty("java.security.auth.login.config", SRM_HOME+"/properties/authmod-unix.properties");
		
		System.setProperty("java.endorsed.dirs", SRM_HOME+"/lib/endorsed");
		
		//Finally Call function
		SRMClientN.main(args);
				
		return 0;
	}
	
	public int callBeStManCopyScript() throws IOException{
		String turl = "/tmp/temp_turl.nc";
		ProcessBuilder pb = new ProcessBuilder("srm-copy", "url", turl);
		pb.directory(new File("/usr/local/bestman/bin"));
		pb.start();
		return 0;
	}
	
	
	//TODO: Remove Later:
	
	public static void main(String[] args) throws IOException{
		System.out.println("Check ProcessBuilder:");
		
		ProcessBuilder pb = new ProcessBuilder("mkdir", "testdir");
		Map<String, String> env = pb.environment();
		
		System.out.println("JAVA_HOME = "+env.get("JAVA_HOME"));
		
		env.put("JAVA_HOME","/usr");
		System.out.println("JAVA_HOME = "+env.get("JAVA_HOME"));
		
//		env.put("VAR1", "myValue");
//		env.remove("OTHERVAR");
//		env.put("VAR2", env.get("VAR1") + "suffix");
		pb.directory(new File("/Users/e1g/Desktop/"));
		Process p = pb.start();
		
//		System.out.println("Check System.getenv():");
//		System.out.println(System.getenv("PATH"));
	}
}
