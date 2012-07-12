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
		
		//TODO: Initialize all arguments to SRMcopy here
		
		//TODO: assign the value of turl
		
		
		ArrayList<String> argsList = new ArrayList<String>();
		
		
		argsList.add(surl);
		
		argsList.add(turl);
			
		
		String args[] = new String[argsList.size()];
		
		for(int i=0; i<argsList.size(); i++){
			args[i] = argsList.get(i).toString();
		}
		
		
		
//		${JAVA_HOME}/bin/java ${MY_CERT_DIR} ${MY_USER_PROXY} ${MY_GTCPPR} ${MY_MEM_OPT} ${MY_SMEM_OPT} -DSRM.HOME=${SRM_HOME} -Dorg.globus.ogsa.client.timeout=0 -Dlog4j.configuration=${SRM_HOME}/properties/log4j.properties -Djava.security.auth.login.config=${SRM_HOME}/properties/authmod-unix.properties -Djava.endorsed.dirs=${SRM_HOME}/lib/endorsed gov.lbl.srm.client.main.SRMClientN $*

		//TODO: Set environment variables
		String srm_home = System.getProperty("SRM_HOME");
		if(srm_home==null || srm_home.equalsIgnoreCase("")){
			System.setProperty("SRM_HOME", "/usr/local/bestman-2.2.1.3.27");
		}
		
		String java_home = System.getProperty("JAVA_HOME");
		if(java_home==null || java_home.equalsIgnoreCase("")){
			System.setProperty("JAVA_HOME", "/usr");
		}
		
		//Finally Call function
		SRMClientN.main(args);
				
		return 0;
	}
	
	
	//TODO: Remove Later:
	
	public static void main(String[] args) throws IOException{
		System.out.println("Check ProcessBuilder:");
		
		ProcessBuilder pb = new ProcessBuilder("mkdir", "testdir");
		Map<String, String> env = pb.environment();
//		env.put("VAR1", "myValue");
//		env.remove("OTHERVAR");
//		env.put("VAR2", env.get("VAR1") + "suffix");
		pb.directory(new File("/Users/e1g/Desktop/"));
		Process p = pb.start();
	}
}
