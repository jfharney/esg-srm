package org.esgf.srm;

import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

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
	
	@SuppressWarnings("unchecked")
	public int formBeStManRequest(){
		String request = "";
		//Code to form request
		
		//Arguments for BeStMan
		String serverURL = "";
		String logPath = "";	
		String storageInfo = "";	
		String fileType = "";
		String targetStorageInfo = "";
		String surl = "";
		String turl = "";
		
		//TODO: Initialize all arguments here
		
		boolean debug = false;
		boolean silent = false;
		
		ArrayList argsList = new ArrayList<String>();
		
		argsList.add("-serviceurl");
		argsList.add(serverURL);
		
		argsList.add("-logPath");
		argsList.add(logPath);
		
		argsList.add("-storageinfo");
		argsList.add(storageInfo);
		
		argsList.add("-filetype");
		argsList.add(fileType);
		
		argsList.add("-targetstorageinfo");
		argsList.add(targetStorageInfo);
		
		argsList.add("-surls");
		argsList.add(surl);
		
		argsList.add("-turls");
		argsList.add(turl);
			
		if(debug){
			argsList.add("-debug");
		}
		if(silent){
			argsList.add("-silent");
		}
		
		String args[] = new String[argsList.size()];
		
		for(int i=0; i<argsList.size(); i++){
			args[i] = argsList.get(i).toString();
		}
		
		//TODO: Call the function with args as arguments.
				
		return 0;
	}
	
}
